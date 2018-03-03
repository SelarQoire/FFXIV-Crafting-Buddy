package nn;

import crafting.Action;
import crafting.AllActions;
import crafting.Condition;
import crafting.State;

public class AllInputs
{
	private static final Action[]	   m_actions;
	private static final InputSource[] m_sources;

	static
	{
		m_actions = AllActions.Z_ACTIONS_LIST;
		m_sources = InputSource.values();
	}

	public static double getInput(final int p_index, final State p_state, final Condition p_condition)
	{
		final Object o = getObject(p_index);
		if(o instanceof Action)
		{
			return p_state.getSynth().getCrafter().actionIsUnlocked((Action)o) ? 1 : 0;
		}
		else
		{
			final InputSource source = (InputSource)o;
			return source.getInputValue(p_state, p_condition);
		}
	}

	private static Object getObject(final int p_index)
	{
		if(p_index < m_actions.length)
		{
			return m_actions[p_index];
		}
		else
		{
			final int index = p_index - m_actions.length;
			return m_sources[index];
		}
	}

	public static int numInputs()
	{
		return m_actions.length + m_sources.length;
	}
}
