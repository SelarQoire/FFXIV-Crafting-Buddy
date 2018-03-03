package q;

import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import common.AVPairComparator;
import common.ActionValuePair;
import common.Constants;
import common.EndStateEvaluator;
import common.SummaryStats;
import common.TextIO;
import common.Utilities;
import crafting.Action;
import crafting.Condition;
import crafting.Simulator;
import crafting.State;
import crafting.StateProbabilityPair;
import crafting.SynthData;
import nn.AllInputs;
import nn.GradientSetType;
import nn.NeuralNet;
import nn.NeuronType;
import synthSim.SynthGenerator;

public class DDQAgent
{
	private static class Worker implements Callable<Object>
	{
		private double			  m_epsilon;
		private final DoubleNet	  m_net;
		private final MemoryStore m_store;
		private SynthData		  m_synth;
		private final DoubleNet	  m_template;
		private final Random	  m_workerRandom;

		Worker(final DoubleNet p_template)
		{
			m_template = p_template;
			m_net = new DoubleNet(m_template.numInputs(), m_template.numHidden(), m_template.numOutputs(), false);
			m_workerRandom = new Random();
			m_store = new MemoryStore(MAX_STATE_MEMORY_STORE);
		}

		@Override public Object call() throws Exception
		{
			m_net.copyWeightsFrom(m_template);
			m_net.clearCollectedGradients();

			for(int i = 0; i < SIMS_PER_LOOP; i++)
			{
				runSimulation(false, m_epsilon, m_synth, m_net, m_workerRandom, m_store, GradientSetType.COLLECT);
			}

			return null;
		}

		DoubleNet getDoubleNet()
		{
			return m_net;
		}

		void setEpsilon(final double p_epsilon)
		{
			m_epsilon = p_epsilon;
		}

		void setSynth(final SynthData p_synth)
		{
			m_synth = p_synth;
		}
	}

	private static final double	EPSILON_ANNEALING_TARGET = 0;
	private static final double	EPSILON_SLOPE			 = (EPSILON_ANNEALING_TARGET - 1) / Constants.Q_LEARNING_SIMS;
	private static final int	MAX_STATE_MEMORY_STORE	 = 10000;
	private static final int	NUM_HIDDEN				 = 100;
	private static final double	RUNNING_MEAN_MOMENTUM	 = .99;
	private static final int	SIMS_PER_LOOP			 = 50;

	private static DoubleNet coldStartDoubleNet()
	{
		return new DoubleNet(AllInputs.numInputs(), NUM_HIDDEN, 1, true);
	}

	private static double doDQNUpdate(final State p_state, final Condition p_condition, final Action p_bestAction,
			final DoubleNet p_doubleNet, final GradientSetType p_type)
	{
		final double evaluation = getQValue(p_state, p_condition, p_bestAction, p_doubleNet.getEvalNet());
		final double[] target = {evaluation};
		final double current = getQValue(p_state, p_condition, p_doubleNet.getMaxNet());
		if(p_type != GradientSetType.IGNORE)
		{
			loadInputsIntoSensors(p_state, p_condition, p_doubleNet.getMaxNet());
			p_doubleNet.getMaxNet().backprop(target, p_type);
			p_doubleNet.reassignNets();
		}
		return Math.abs(current - evaluation);
	}

	private static ActionValuePair getBestActionForState(final State p_state, final Condition p_condition,
			final Action[] usableActions, final NeuralNet p_net)
	{
		double maxValue = Double.NEGATIVE_INFINITY;
		ActionValuePair bestAction = null;
		for(final Action action: usableActions)
		{
			final double value = getQValue(p_state, p_condition, action, p_net);
			if(bestAction == null || value > maxValue)
			{
				maxValue = value;
				bestAction = new ActionValuePair(action, value);
			}
		}
		return bestAction;
	}

	private static double getCurrentEpsilon(final double p_numSims)
	{
		final double epochEp = Math.max(1 + p_numSims * EPSILON_SLOPE, EPSILON_ANNEALING_TARGET);
		return Math.min(1, epochEp);
	}

	private static double getQValue(final State p_state, final Condition p_condition, final Action p_action,
			final NeuralNet p_net)
	{
		if(p_state.getStep() > 200)
		{
			// probably stuck in a loop - not worth saving
			return 0;
		}

		final StateProbabilityPair[] possibleOutcomes = Simulator.getPossibleOutcomes(p_state, p_condition, p_action);
		final double[] conditionBuffer = new double[4];
		Simulator.fillNextConditionDistribution(p_condition, conditionBuffer);

		double estimate = 0;
		for(final StateProbabilityPair pair: possibleOutcomes)
		{
			final double spProbability = pair.getProbability();
			final State outcomeState = pair.getState();
			for(final Condition condition: Condition.values())
			{
				final double cProbability = conditionBuffer[condition.ordinal()];
				final double probability = spProbability * cProbability;
				if(probability > 0)
				{
					double result = 0;
					if(Simulator.isEndState(outcomeState))
					{
						result = EndStateEvaluator.endStateValue(outcomeState);
					}
					else
					{
						result = Constants.Q_GAMMA * getQValue(outcomeState, condition, p_net);
					}
					estimate += result * probability;
				}
			}
		}

		return estimate;
	}

	static double getQValue(final State p_state, final Condition p_condition, final NeuralNet p_net)
	{
		loadInputsIntoSensors(p_state, p_condition, p_net);
		return p_net.getOutputValue(0);
	}

	private static ActionValuePair[] getRecommendedActions(final State p_state, final Condition p_condition,
			final Action[] usableActions, final NeuralNet p_net, final SynthData p_synth)
	{
		final ArrayList<ActionValuePair> actionsList = new ArrayList<>();
		for(final Action action: usableActions)
		{
			if(p_synth.getCrafter().actionIsUnlocked(action) && action.isUseable(p_state, p_condition))
			{
				final double actionValue = getQValue(p_state, p_condition, action, p_net);
				final ActionValuePair pair = new ActionValuePair(action, actionValue);
				actionsList.add(pair);
			}
		}

		final ActionValuePair[] ret = actionsList.toArray(new ActionValuePair[] {});
		Arrays.sort(ret, new AVPairComparator(false));
		return ret;
	}

	private static Action[] getUsableActions(final Action[] p_candidates, final State p_state,
			final Condition p_condition)
	{
		final ArrayList<Action> ret = new ArrayList<>();
		for(final Action action: p_candidates)
		{
			if(action.isUseable(p_state, p_condition) && p_state.getSynth().getCrafter().actionIsUnlocked(action))
			{
				ret.add(action);
			}
		}
		return ret.toArray(new Action[] {});
	}

	static void loadInputsIntoSensors(final State p_state, final Condition p_condition, final NeuralNet p_net)
	{
		final double[] sensorVals = new double[p_net.numInputs()];
		for(int i = 0; i < p_net.numInputs(); i++)
		{
			sensorVals[i] = AllInputs.getInput(i, p_state, p_condition);
		}
		p_net.loadSensors(sensorVals);
	}

	public static void main(final String[] p_args)
	{
		final DDQAgent agent = new DDQAgent();
		agent.m_doubleNet = DoubleNetIO.selectAndLoadFromFile();
		if(agent.m_doubleNet == null)
		{
			agent.m_doubleNet = coldStartDoubleNet();
		}
		agent.m_doubleNet.standardize();

		final SynthGenerator gen = new SynthGenerator();
		final SynthData synth = gen.newRandomSynth();

		printInitialStateValue(agent.m_doubleNet, synth);
		runSimulation(true, 0, synth, agent.m_doubleNet, agent.m_random, null, GradientSetType.IGNORE);
	}

	private static void printInitialStateValue(final DoubleNet p_doubleNet, final SynthData p_synth)
	{
		final State startState = p_synth.newState();
		final Condition condition = Simulator.getStartCondition();
		System.out.println(getQValue(startState, condition, p_doubleNet.getNetA()));
		System.out.println(getQValue(startState, condition, p_doubleNet.getNetB()));
	}

	private static double runSimulation(final boolean p_userGuided, final double p_epsilon, final SynthData p_synth,
			final DoubleNet p_doubleNet, final Random p_random, final MemoryStore p_store, final GradientSetType p_type)
	{
		State currentState = p_synth.newState();
		Condition condition = Simulator.getStartCondition();

		boolean isEndState = false;
		while(!isEndState)
		{
			if(p_userGuided)
			{
				TextIO.printState(currentState, getQValue(currentState, condition, p_doubleNet.getMaxNet()), true);
			}

			final Action action = selectNextAction(currentState, condition, p_epsilon, p_userGuided, p_doubleNet,
					p_synth, p_random, p_store, p_type);
			final State nextState = Simulator.monteCarloStep(currentState, condition, action, p_userGuided, false);
			isEndState = Simulator.isEndState(nextState);
			currentState = nextState;
			if(!isEndState)
			{
				condition = Simulator.updateCondition(condition, p_userGuided);
			}
		}

		if(p_userGuided)
		{
			TextIO.printState(currentState, getQValue(currentState, condition, p_doubleNet.getMaxNet()), true);
			TextIO.printResult(currentState);
		}

		return EndStateEvaluator.endStateValue(currentState);
	}

	protected static Action selectNextAction(final State p_state, final Condition p_condition, final double p_epsilon,
			final boolean p_userGuided, final DoubleNet p_doubleNet, final SynthData p_synth, final Random p_random,
			final MemoryStore p_store, final GradientSetType p_type)
	{
		final Action[] usableActions = getUsableActions(p_synth.getWorthwhileActions(), p_state, p_condition);

		if(p_userGuided)
		{
			final ActionValuePair[] actionsInOrder = getRecommendedActions(p_state, p_condition, usableActions,
					p_doubleNet.getMaxNet(), p_synth);
			return ((ActionValuePair)TextIO.getResponse("Which action to take?", actionsInOrder)).getAction();
		}
		else
		{
			final ActionValuePair bestAction = getBestActionForState(p_state, p_condition, usableActions,
					p_doubleNet.getMaxNet());
			update(p_state, p_condition, bestAction.getAction(), usableActions, p_doubleNet, p_store, p_type);
			if(Utilities.flipCoin(p_epsilon))
			{
				return usableActions[p_random.nextInt(usableActions.length)];
			}

			return bestAction.getAction();
		}
	}

	private static void update(final State p_state, final Condition p_condition, final Action p_bestAction,
			final Action[] p_availableActions, final DoubleNet p_doubleNet, final MemoryStore p_store,
			final GradientSetType p_type)
	{
		doDQNUpdate(p_state, p_condition, p_bestAction, p_doubleNet, p_type);

		if(p_store != null)
		{
			p_store.addMemory(p_state, p_condition, p_availableActions);

			final MemoryStoreData memory = p_store.getMemory();
			final ActionValuePair bestActionFromMemory = getBestActionForState(memory.getState(), memory.getCondition(),
					memory.getAvailableActions(), p_doubleNet.getMaxNet());

			doDQNUpdate(memory.getState(), memory.getCondition(), bestActionFromMemory.getAction(), p_doubleNet,
					p_type);
		}
	}

	private double		 m_divisor;

	private DoubleNet	 m_doubleNet;
	private final Random m_random;
	private double		 m_runningMean;

	public DDQAgent()
	{
		m_random = new Random();
	}

	public void runLearningAgent()
	{
		m_doubleNet = DoubleNetIO.selectAndLoadFromFile();
		if(m_doubleNet == null)
		{
			m_doubleNet = coldStartDoubleNet();
		}

		runMultithreadedLearningAgent();
	}

	private void runMultithreadedLearningAgent()
	{
		final ExecutorService es = Executors.newFixedThreadPool(Constants.NUM_THREADS);
		final Collection<Worker> executables = new ArrayList<>();
		for(int i = 0; i < Constants.NUM_THREADS; i++)
		{
			executables.add(new Worker(m_doubleNet));
		}

		final SynthGenerator gen = new SynthGenerator();
		for(int i = 0; i < Constants.Q_LEARNING_SIMS; i++)
		{
			boolean first = true;
			for(final Worker worker: executables)
			{
				worker.setSynth(gen.newRandomSynth());

				if(first)
				{
					worker.setEpsilon(0); // have one worker always evaluating the policy
					first = false;
				}
				else
				{
					worker.setEpsilon(getCurrentEpsilon(i));
				}
			}

			try
			{
				es.invokeAll(executables);
			}
			catch(final InterruptedException e)
			{
				e.printStackTrace();
				break;
			}

			final ArrayList<Double> tVals = new ArrayList<>();

			double dSquaredSum = 0;

			dSquaredSum += updateWeights(executables, NeuronType.HIDDEN, DNNetType.A, tVals);
			dSquaredSum += updateWeights(executables, NeuronType.HIDDEN, DNNetType.B, tVals);
			dSquaredSum += updateWeights(executables, NeuronType.OUTPUT, DNNetType.A, tVals);
			dSquaredSum += updateWeights(executables, NeuronType.OUTPUT, DNNetType.B, tVals);

			final double val = Math.sqrt(dSquaredSum);
			m_runningMean = m_runningMean * RUNNING_MEAN_MOMENTUM + val * (1 - RUNNING_MEAN_MOMENTUM);
			m_divisor = m_divisor * RUNNING_MEAN_MOMENTUM + (1 - RUNNING_MEAN_MOMENTUM);

			System.out.println(Math.sqrt(dSquaredSum) + "\t" + m_runningMean / m_divisor + "\t" + getCurrentEpsilon(i));

			m_doubleNet.clearCollectedGradients();

			if(TextIO.keyHasBeenPressed())
			{
				DoubleNetIO.selectFileAndSave(m_doubleNet);
			}
		}

		Toolkit.getDefaultToolkit().beep();
	}

	private double updateWeights(final Collection<Worker> p_executables, final NeuronType p_topType,
			final DNNetType p_netType, final ArrayList<Double> p_tVals)
	{
		double dSquaredSum = 0;

		final NeuronType lowerType = NeuronType.nextLevelDown(p_topType);
		for(int topIndex = 0; topIndex < m_doubleNet.numNeurons(p_topType); topIndex++)
		{
			final SummaryStats biasStats = new SummaryStats();
			for(final Worker worker: p_executables)
			{
				final List<Double> collected = worker.getDoubleNet().getNet(p_netType)
						.getCollectedBiasGradients(p_topType, topIndex);
				biasStats.addAll(collected);
			}

			dSquaredSum += Math.pow(biasStats.getMean(), 2);
			m_doubleNet.getNet(p_netType).setBiasGradient(p_topType, topIndex, biasStats.getMean(),
					GradientSetType.UPDATE);

			for(int lowerIndex = 0; lowerIndex < m_doubleNet.numNeurons(lowerType); lowerIndex++)
			{
				final SummaryStats weightStats = new SummaryStats();
				for(final Worker worker: p_executables)
				{
					final List<Double> collected = worker.getDoubleNet().getNet(p_netType)
							.getCollectedWeightGradients(p_topType, topIndex, lowerIndex);
					weightStats.addAll(collected);
				}

				dSquaredSum += Math.pow(weightStats.getMean(), 2);
				m_doubleNet.getNet(p_netType).setWeightGradient(p_topType, topIndex, lowerIndex, weightStats.getMean(),
						GradientSetType.UPDATE);
			}
		}

		return dSquaredSum;
	}
}
