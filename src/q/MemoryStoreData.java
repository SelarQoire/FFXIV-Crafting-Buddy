package q;

import crafting.Action;
import crafting.Condition;
import crafting.State;

public class MemoryStoreData
{
	private final Action[]	m_availableActions;
	private final Condition	m_condition;
	private final State		m_state;

	public MemoryStoreData(final State p_state, final Condition p_condition, final Action[] p_availableActions)
	{
		m_state = p_state;
		m_condition = p_condition;
		m_availableActions = p_availableActions;
	}

	public Action[] getAvailableActions()
	{
		return m_availableActions;
	}

	public Condition getCondition()
	{
		return m_condition;
	}

	public State getState()
	{
		return m_state;
	}
}