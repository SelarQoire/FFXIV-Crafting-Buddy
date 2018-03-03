package common;

import crafting.Action;

public class ActionValuePair
{
	private final Action m_action;
	private final double m_value;

	public ActionValuePair(final Action p_action, final double p_value)
	{
		m_action = p_action;
		m_value = p_value;
	}

	public Action getAction()
	{
		return m_action;
	}

	public double getValue()
	{
		return m_value;
	}

	@Override public String toString()
	{
		return m_action + " (value = " + m_value + ")";
	}
}