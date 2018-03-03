package nn;

class RawInputNeuron extends Neuron
{
	private double m_value;

	public RawInputNeuron()
	{
		super(NeuronType.INPUT);
	}

	@Override double getOutputValue()
	{
		return m_value;
	}

	double getValue()
	{
		return m_value;
	}

	void setValue(final double p_value)
	{
		m_value = p_value;
	}
}
