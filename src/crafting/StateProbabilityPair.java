package crafting;

public class StateProbabilityPair
{
	private final double m_probability;
	private final State	 m_state;

	StateProbabilityPair(final State p_state, final double p_probability)
	{
		m_state = p_state;
		m_probability = p_probability;
	}

	public double getProbability()
	{
		return m_probability;
	}

	public State getState()
	{
		return m_state;
	}
}
