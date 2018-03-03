package search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import common.AVPairComparator;
import common.ActionValuePair;
import common.Constants;
import common.EndStateEvaluator;
import common.Policy;
import crafting.Action;
import crafting.Condition;
import crafting.Simulator;
import crafting.State;
import crafting.StateProbabilityPair;
import crafting.SynthData;
import nn.AllInputs;
import nn.NeuralNet;
import q.DoubleNet;
import q.DoubleNetIO;
import synthSim.SynthGenerator;

public class Expectimax extends Policy
{
	private class Worker implements Callable<ActionValuePair>
	{
		private final Action	m_action;
		private final Condition	m_condition;
		private final NeuralNet	m_net;
		private final State		m_state;

		Worker(final State p_state, final Condition p_condition, final NeuralNet p_net, final int p_maxDepth,
				final Action p_action)
		{
			m_state = p_state.clone();
			m_condition = p_condition;
			m_action = p_action;
			m_net = p_net.clone();
		}

		@Override public ActionValuePair call() throws Exception
		{
			final double value = getValue(m_state, m_condition, m_net, m_maxDepth, m_action);
			final ActionValuePair avPair = new ActionValuePair(m_action, value);
			System.out.print(".");
			return avPair;
		}
	}

	private static double getNNValue(final State p_state, final Condition p_condition, final NeuralNet p_net)
	{
		final double[] sensorVals = new double[p_net.numInputs()];
		for(int i = 0; i < p_net.numInputs(); i++)
		{
			sensorVals[i] = AllInputs.getInput(i, p_state, p_condition);
		}
		p_net.loadSensors(sensorVals);
		return p_net.getOutputValue(0);
	}

	private static double getValue(final State p_state, final Condition p_condition, final NeuralNet p_net,
			final int p_maxDepth)
	{
		final Action[] actions = p_state.getSynth().getWorthwhileActions();
		double maxVal = Double.NaN;
		for(final Action action: actions)
		{
			if(action.isUseable(p_state, p_condition))
			{
				final double value = getValue(p_state, p_condition, p_net, p_maxDepth, action);
				if(Double.isNaN(maxVal) || value > maxVal)
				{
					maxVal = value;
				}
			}
		}
		return maxVal;
	}

	private static double getValue(final State p_state, final Condition p_condition, final NeuralNet p_net,
			final int p_maxDepth, final Action action)
	{
		final StateProbabilityPair[] possibleOutcomes = Simulator.getPossibleOutcomes(p_state, p_condition, action);
		final double[] conditionBuffer = new double[4];
		Simulator.fillNextConditionDistribution(p_condition, conditionBuffer);

		double estimate = 0;
		for(final StateProbabilityPair pair: possibleOutcomes)
		{
			final double spProbability = pair.getProbability();
			final State outcomeState = pair.getState();
			for(final Condition outcomeCondition: Condition.values())
			{
				final double cProbability = conditionBuffer[outcomeCondition.ordinal()];
				final double probability = spProbability * cProbability;
				if(probability > 0)
				{
					double result = 0;
					if(Simulator.isEndState(outcomeState))
					{
						result = EndStateEvaluator.endStateValue(outcomeState);
					}
					else if(p_maxDepth == 0)
					{
						result = Constants.Q_GAMMA * getNNValue(outcomeState, outcomeCondition, p_net);
					}
					else
					{
						result = Constants.Q_GAMMA * getValue(outcomeState, outcomeCondition, p_net, p_maxDepth - 1);
					}
					estimate += result * probability;
				}
			}
		}
		return estimate;
	}

	public static void main(final String[] p_args)
	{
		final SynthGenerator gen = new SynthGenerator();

		final DoubleNet doubleNet = DoubleNetIO.selectAndLoadFromFile();
		final Expectimax bfs = new Expectimax(gen.newRandomSynth(), 3, doubleNet.getNetA());
		Simulator.runSimulation(bfs, true, true);
	}

	private final int		m_maxDepth;
	private final NeuralNet	m_net;

	public Expectimax(final SynthData p_synth, final int p_maxDepth, final NeuralNet p_net)
	{
		super(p_synth);
		m_maxDepth = p_maxDepth;
		m_net = p_net;
	}

	@Override protected ActionValuePair[] getActionsInOrder(final State p_state, final Condition p_condition)
	{
		return rateActions(p_state, p_condition, m_net, m_maxDepth);
	}

	private ActionValuePair[] rateActions(final State p_state, final Condition p_condition, final NeuralNet p_net,
			final int p_maxDepth)
	{
		final ExecutorService es = Executors.newFixedThreadPool(Constants.NUM_THREADS);
		final Collection<Worker> executables = new ArrayList<>();

		final ArrayList<ActionValuePair> ret = new ArrayList<>();
		final Action[] actions = p_state.getSynth().getWorthwhileActions();
		for(final Action action: actions)
		{
			if(action.isUseable(p_state, p_condition))
			{
				executables.add(new Worker(p_state, p_condition, p_net, p_maxDepth, action));
			}
		}

		try
		{
			final List<Future<ActionValuePair>> results = es.invokeAll(executables);
			System.out.println();
			for(final Future<ActionValuePair> future: results)
			{
				ret.add(future.get());
			}
		}
		catch(final InterruptedException | ExecutionException e)
		{
			e.printStackTrace();
		}

		final ActionValuePair[] arr = ret.toArray(new ActionValuePair[] {});
		Arrays.sort(arr, new AVPairComparator(false));

		es.shutdown();
		return arr;
	}
}
