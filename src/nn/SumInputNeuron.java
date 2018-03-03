
package nn;

import java.util.List;

class SumInputNeuron extends Neuron
{
	private final AdamObject   m_bias;

	private double			   m_dOutput;
	private final FunctionType m_functionType;
	private final Neuron[]	   m_inputs;
	private final NeuralNet	   m_net;
	private double			   m_output;
	private final AdamObject[] m_weights;

	SumInputNeuron(final FunctionType p_functionType, final int m_numInputs, final double p_initialBias,
			final NeuralNet p_net, final NeuronType p_type)
	{
		super(p_type);
		m_functionType = p_functionType;
		m_inputs = new Neuron[m_numInputs];
		m_weights = new AdamObject[m_numInputs];
		m_bias = new AdamObject(p_initialBias);
		m_net = p_net;
	}

	void addInput(final int p_index, final Neuron p_input, final double p_weight)
	{
		m_inputs[p_index] = p_input;
		m_weights[p_index] = new AdamObject(p_weight);
	}

	public void clearCollectedGradients()
	{
		m_bias.clearCollectedGradients();
		for(final AdamObject obj: m_weights)
		{
			obj.clearCollectedGradients();
		}
	}

	double getBias()
	{
		return m_bias.getValue();
	}

	List<Double> getCollectedBiasGradients()
	{
		return m_bias.getCollectedGradients();
	}

	List<Double> getCollectedWeightGradients(final int p_inputIndex)
	{
		return m_weights[p_inputIndex].getCollectedGradients();
	}

	double getDOutput()
	{
		if(m_net.isDirty(getType()))
		{
			throw new RuntimeException();
		}
		return m_dOutput;
	}

	Neuron getInput(final int p_index)
	{
		return m_inputs[p_index];
	}

	int getNumInputs()
	{
		return m_inputs.length;
	}

	@Override double getOutputValue()
	{
		if(m_net.isDirty(getType()))
		{
			throw new RuntimeException();
		}
		return m_output;
	}

	double getWeight(final int p_index)
	{
		return m_weights[p_index].getValue();
	}

	void recalculate()
	{
		double sum = 0;
		for(int i = 0; i < m_inputs.length; i++)
		{
			final double inputValue = m_inputs[i].getOutputValue();
			final double weight = m_weights[i].getValue();
			final double product = inputValue * weight;

			if(!Double.isFinite(product))
			{
				throw new RuntimeException();
			}

			sum += product;
		}

		final double affine = sum + m_bias.getValue();

		m_output = getFunctionValue(m_functionType, affine);
		m_dOutput = getDDx(m_functionType, affine);
	}

	void setBias(final double p_bias)
	{
		m_bias.setValue(p_bias);
	}

	void setBiasGradient(final double p_gradient, final GradientSetType p_type)
	{
		m_bias.setNewGradient(p_gradient, p_type);
	}

	void setWeight(final int p_inputIndex, final double p_weight)
	{
		m_weights[p_inputIndex].setValue(p_weight);
	}

	void setWeightGradient(final int p_inputIndex, final double p_gradient, final GradientSetType p_type)
	{
		m_weights[p_inputIndex].setNewGradient(p_gradient, p_type);
	}
}
