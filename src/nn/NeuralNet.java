package nn;

import java.util.List;
import java.util.Random;

import common.Constants;
import common.Utilities;

public class NeuralNet
{
	private static void copyOneWeightLevel(final NeuronType p_level, final NeuralNet p_template,
			final NeuralNet p_target)
	{
		final NeuronType oneLevelDown = NeuronType.nextLevelDown(p_level);
		for(int topIndex = 0; topIndex < Math.max(p_target.numNeurons(p_level),
				p_template.numNeurons(p_level)); topIndex++)
		{
			p_target.setBias(p_level, topIndex, p_template.getBias(p_level, topIndex));
			for(int lowerIndex = 0; lowerIndex < Math.max(p_target.numNeurons(oneLevelDown),
					p_template.numNeurons(oneLevelDown)); lowerIndex++)
			{
				p_target.setWeight(p_level, topIndex, lowerIndex, p_template.getWeight(p_level, topIndex, lowerIndex));
			}
		}
	}

	public static void main(final String[] p_args)
	{
		final NeuralNet nn = new NeuralNet(10, 10, 10, true);
		final Random random = new Random();
		final double[] inputs = new double[10];
		for(int i = 0; i < 10; i++)
		{
			inputs[i] = random.nextDouble();
		}
		nn.loadSensors(inputs);
		nn.backprop(new double[10], GradientSetType.UPDATE);
	}

	private final SumInputNeuron[] m_hidden;

	private boolean				   m_hiddensDirty;

	private final RawInputNeuron[] m_inputs;
	private final SumInputNeuron[] m_outputs;
	private boolean				   m_outputsDirty;

	public NeuralNet(final int p_numInputs, final int p_numHidden, final int p_numOutputs, final boolean p_randomize)
	{
		m_inputs = new RawInputNeuron[p_numInputs];
		m_hidden = new SumInputNeuron[p_numHidden];
		m_outputs = new SumInputNeuron[p_numOutputs];

		for(int i = 0; i < p_numInputs; i++)
		{
			m_inputs[i] = new RawInputNeuron();
		}

		for(int i = 0; i < p_numHidden; i++)
		{
			final double bias = p_randomize ? Utilities.randomNoise(Constants.NN_NOISE_SCALE) : 1;
			final SumInputNeuron hidden = new SumInputNeuron(FunctionType.LEAKY_RELU, p_numInputs, bias, this,
					NeuronType.HIDDEN);
			m_hidden[i] = hidden;
		}

		for(int i = 0; i < p_numOutputs; i++)
		{
			final double bias = p_randomize ? Utilities.randomNoise(Constants.NN_NOISE_SCALE) : 1;
			final SumInputNeuron output = new SumInputNeuron(FunctionType.SIGMOID, p_numHidden, bias, this,
					NeuronType.OUTPUT);
			m_outputs[i] = output;
		}

		for(int inputIndex = 0; inputIndex < p_numInputs; inputIndex++)
		{
			for(int hiddenIndex = 0; hiddenIndex < p_numHidden; hiddenIndex++)
			{
				final double weight = p_randomize ? Utilities.randomNoise(Constants.NN_NOISE_SCALE) : 1;
				m_hidden[hiddenIndex].addInput(inputIndex, m_inputs[inputIndex], weight);
			}
		}

		for(int hiddenIndex = 0; hiddenIndex < p_numHidden; hiddenIndex++)
		{
			for(int outputIndex = 0; outputIndex < p_numOutputs; outputIndex++)
			{
				final double weight = p_randomize ? Utilities.randomNoise(Constants.NN_NOISE_SCALE) : 1;
				m_outputs[outputIndex].addInput(hiddenIndex, m_hidden[hiddenIndex], weight);
			}
		}

		setToDirty();
	}

	public void backprop(final double p_targetValues[], final GradientSetType p_type)
	{
		calculateAndSetGradients(p_targetValues, p_type);
		setToDirty();
	}

	private void calculateAndSetGradients(final double[] p_targetValues, final GradientSetType p_type)
	{
		final double[] hiddenBiasGradients = new double[numHidden()];
		final double[][] hiddenWeightGradients = new double[numHidden()][numInputs()];
		for(int outputIndex = 0; outputIndex < numOutputs(); outputIndex++)
		{
			final double value = getOutputValue(outputIndex);
			final double delta = value - p_targetValues[outputIndex];// dLoss/dOutput
			if(delta == 0)
			{
				continue;
			}

			final double dOutput = getDOutput(outputIndex);// dOutput/dSum

			m_outputs[outputIndex].setBiasGradient(delta * dOutput, p_type);
			for(int hiddenIndex = 0; hiddenIndex < numHidden(); hiddenIndex++)
			{
				final double hiddenActivation = m_hidden[hiddenIndex].getOutputValue();
				final double hiddenDActivation = m_hidden[hiddenIndex].getDOutput();// dSum/dWeight
				final double weightHiddenToOutput = m_outputs[outputIndex].getWeight(hiddenIndex);// dSum/d
				m_outputs[outputIndex].setWeightGradient(hiddenIndex, delta * dOutput * hiddenActivation, p_type);

				hiddenBiasGradients[hiddenIndex] += delta * dOutput * weightHiddenToOutput * hiddenDActivation;

				for(int inputIndex = 0; inputIndex < numInputs(); inputIndex++)
				{
					final double inputActivation = m_inputs[inputIndex].getOutputValue();
					hiddenWeightGradients[hiddenIndex][inputIndex] += delta * dOutput * weightHiddenToOutput
							* hiddenDActivation * inputActivation;
				}
			}
		}

		for(int hiddenIndex = 0; hiddenIndex < numHidden(); hiddenIndex++)
		{
			m_hidden[hiddenIndex].setBiasGradient(hiddenBiasGradients[hiddenIndex], p_type);
			for(int inputIndex = 0; inputIndex < numInputs(); inputIndex++)
			{
				m_hidden[hiddenIndex].setWeightGradient(inputIndex, hiddenWeightGradients[hiddenIndex][inputIndex],
						p_type);
			}
		}
	}

	public double calculateSquareLoss(final double[] p_targetValues)
	{
		double loss = 0;
		for(int i = 0; i < m_outputs.length; i++)
		{
			loss += .5 * Math.pow(getOutputValue(i) - p_targetValues[i], 2);
		}
		return loss;
	}

	public void clearCollectedGradients()
	{
		for(int hiddenIndex = 0; hiddenIndex < numHidden(); hiddenIndex++)
		{
			getSINeuron(NeuronType.HIDDEN, hiddenIndex).clearCollectedGradients();
		}

		for(int outputIndex = 0; outputIndex < numOutputs(); outputIndex++)
		{
			getSINeuron(NeuronType.OUTPUT, outputIndex).clearCollectedGradients();
		}
	}

	@Override public NeuralNet clone()
	{
		final NeuralNet ret = new NeuralNet(numInputs(), numHidden(), numOutputs(), false);
		ret.copyWeightsFrom(this);
		return ret;
	}

	public void copyWeightsFrom(final NeuralNet p_template)
	{
		copyOneWeightLevel(NeuronType.HIDDEN, p_template, this);
		copyOneWeightLevel(NeuronType.OUTPUT, p_template, this);

		setToDirty();
	}

	public double getBias(final NeuronType p_type, final int p_neuronIndex)
	{
		return getSINeuron(p_type, p_neuronIndex).getBias();
	}

	public List<Double> getCollectedBiasGradients(final NeuronType p_type, final int p_neuronIndex)
	{
		return getSINeuron(p_type, p_neuronIndex).getCollectedBiasGradients();
	}

	public List<Double> getCollectedWeightGradients(final NeuronType p_type, final int p_neuronIndex,
			final int p_weightIndex)
	{
		return getSINeuron(p_type, p_neuronIndex).getCollectedWeightGradients(p_weightIndex);
	}

	double getDOutput(final int p_index)
	{
		if(isDirty())
		{
			recalculateAll();
		}
		return m_outputs[p_index].getDOutput();
	}

	public double getOutputValue(final int p_index)
	{
		if(isDirty())
		{
			recalculateAll();
		}
		return m_outputs[p_index].getOutputValue();
	}

	public double[] getOutputValues()
	{
		final double[] ret = new double[numOutputs()];
		for(int outputIndex = 0; outputIndex < numOutputs(); outputIndex++)
		{
			ret[outputIndex] = getOutputValue(outputIndex);
		}
		return ret;
	}

	private SumInputNeuron getSINeuron(final NeuronType p_neuronType, final int p_index)
	{
		if(p_neuronType == NeuronType.HIDDEN)
		{
			return m_hidden[p_index];
		}
		else if(p_neuronType == NeuronType.OUTPUT)
		{
			return m_outputs[p_index];
		}
		throw new RuntimeException();
	}

	public double getWeight(final NeuronType p_type, final int p_neuronIndex, final int p_weightIndex)
	{
		return getSINeuron(p_type, p_neuronIndex).getWeight(p_weightIndex);
	}

	private boolean isDirty()
	{
		return m_hiddensDirty || m_outputsDirty;
	}

	boolean isDirty(final NeuronType p_type)
	{
		switch(p_type)
		{
			case HIDDEN:
				return m_hiddensDirty;
			case INPUT:
				return false;
			case OUTPUT:
				return m_outputsDirty;
			default:
				throw new RuntimeException();
		}
	}

	public void loadSensors(final double[] p_in)
	{
		for(int i = 0; i < p_in.length; i++)
		{
			m_inputs[i].setValue(p_in[i]);
		}
		setToDirty();
	}

	public int numHidden()
	{
		return m_hidden.length;
	}

	public int numInputs()
	{
		return m_inputs.length;
	}

	public int numNeurons(final NeuronType p_level)
	{
		switch(p_level)
		{
			case HIDDEN:
				return numHidden();
			case INPUT:
				return numInputs();
			case OUTPUT:
				return numOutputs();
			default:
				throw new RuntimeException();
		}
	}

	public int numOutputs()
	{
		return m_outputs.length;
	}

	private void recalculateAll()
	{
		for(final SumInputNeuron hidden: m_hidden)
		{
			hidden.recalculate();
		}

		m_hiddensDirty = false;

		for(final SumInputNeuron output: m_outputs)
		{
			output.recalculate();
		}

		m_outputsDirty = false;
	}

	public void setBias(final NeuronType p_type, final int p_outputIndex, final double p_bias)
	{
		getSINeuron(p_type, p_outputIndex).setBias(p_bias);
		setToDirty();
	}

	public void setBiasGradient(final NeuronType p_neuronType, final int p_neuronIndex, final double p_gradient,
			final GradientSetType p_setType)
	{
		getSINeuron(p_neuronType, p_neuronIndex).setBiasGradient(p_gradient, p_setType);
		setToDirty();
	}

	private void setToDirty()
	{
		m_hiddensDirty = true;
		m_outputsDirty = true;
	}

	public void setWeight(final NeuronType p_type, final int p_outputIndex, final int p_hiddenIndex,
			final double p_weight)
	{
		getSINeuron(p_type, p_outputIndex).setWeight(p_hiddenIndex, p_weight);
		setToDirty();
	}

	public void setWeightGradient(final NeuronType p_neuronType, final int p_neuronIndex, final int p_weightIndex,
			final double p_gradient, final GradientSetType p_setType)
	{
		getSINeuron(p_neuronType, p_neuronIndex).setWeightGradient(p_weightIndex, p_gradient, p_setType);
		setToDirty();
	}
}
