package q;

import common.Utilities;
import nn.NeuralNet;
import nn.NeuronType;

public class DoubleNet
{
	private NeuralNet m_evalNet;
	private NeuralNet m_maxNet;
	private NeuralNet m_netA;
	private NeuralNet m_netB;

	DoubleNet()
	{
		// necessary for use in DoubleNetIO
	}

	DoubleNet(final int p_numInputs, final int p_numHidden, final int p_numOutputs, final boolean p_randomize)
	{
		m_netA = new NeuralNet(p_numInputs, p_numHidden, p_numOutputs, p_randomize);
		m_netB = new NeuralNet(p_numInputs, p_numHidden, p_numOutputs, p_randomize);

		reassignNets();
	}

	public void clearCollectedGradients()
	{
		m_netA.clearCollectedGradients();
		m_netB.clearCollectedGradients();
	}

	public void copyWeightsFrom(final DoubleNet p_template)
	{
		m_netA.copyWeightsFrom(p_template.getNetA());
		m_netB.copyWeightsFrom(p_template.getNetB());
	}

	public NeuralNet getEvalNet()
	{
		return m_evalNet;
	}

	public NeuralNet getMaxNet()
	{
		return m_maxNet;
	}

	public NeuralNet getNet(final DNNetType p_type)
	{
		switch(p_type)
		{
			case A:
				return getNetA();
			case B:
				return getNetB();
			default:
				throw new RuntimeException();
		}
	}

	public NeuralNet getNetA()
	{
		return m_netA;
	}

	public NeuralNet getNetB()
	{
		return m_netB;
	}

	public int numHidden()
	{
		return m_netA.numHidden();
	}

	public int numInputs()
	{
		return m_netA.numInputs();
	}

	public int numNeurons(final NeuronType p_type)
	{
		return m_netA.numNeurons(p_type);
	}

	public int numOutputs()
	{
		return m_netA.numOutputs();
	}

	void reassignNets()
	{
		final boolean flip = Utilities.flipCoin(.5);
		if(flip)
		{
			m_maxNet = m_netA;
			m_evalNet = m_netB;
		}
		else
		{
			m_maxNet = m_netB;
			m_evalNet = m_netA;
		}
	}

	public void setNetA(final NeuralNet p_neuralNet)
	{
		m_netA = p_neuralNet;
	}

	public void setNetB(final NeuralNet p_neuralNet)
	{
		m_netB = p_neuralNet;
	}

	public void standardize()
	{
		m_maxNet = m_netA;
		m_evalNet = m_netB;
	}
}
