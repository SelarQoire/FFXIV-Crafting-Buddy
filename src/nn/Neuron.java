package nn;

import common.Constants;
import common.Utilities;

abstract class Neuron
{
	protected static double getDDx(final FunctionType p_functionType, final double p_x)
	{
		switch(p_functionType)
		{
			case LEAKY_RELU:
				return p_x > 0 ? 1 : Constants.LEAKY_MULTIPLIER;
			case LEAKY_SIGMOID:
				return getDDx(FunctionType.SIGMOID, p_x) + Constants.LEAKY_MULTIPLIER;
			case LINEAR:
				return 1;
			case RELU:
				return p_x > 0 ? 1 : 0;
			case SIGMOID:
				final double exp = Math.exp(p_x);
				if(!Double.isFinite(exp))
				{
					return 0;
				}
				return exp / Math.pow(1 + exp, 2);
			default:
				throw new RuntimeException();
		}
	}

	protected static double getFunctionValue(final FunctionType p_functionType, final double p_x)
	{
		switch(p_functionType)
		{
			case LEAKY_RELU:
				return Math.max(p_x * Constants.LEAKY_MULTIPLIER, p_x);
			case LEAKY_SIGMOID:
				return Utilities.logit(p_x) + p_x * Constants.LEAKY_MULTIPLIER;
			case LINEAR:
				return p_x;
			case RELU:
				return Math.max(0, p_x);
			case SIGMOID:
				return Utilities.logit(p_x);
			default:
				throw new RuntimeException();
		}
	}

	private final NeuronType m_type;

	public Neuron(final NeuronType p_type)
	{
		m_type = p_type;
	}

	abstract double getOutputValue();

	public NeuronType getType()
	{
		return m_type;
	}
}
