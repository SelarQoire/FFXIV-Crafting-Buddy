package common;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import crafting.Action;
import crafting.Condition;
import crafting.State;
import crafting.SynthData;

public abstract class Policy
{
	private static final int  MAX_CC_ACTIONS = 10;

	private final Set<Action> m_crossClassActionsUsed;

	private final SynthData	  m_synth;

	public Policy(final SynthData p_synth)
	{
		m_synth = p_synth;
		m_crossClassActionsUsed = new HashSet<>();
	}

	private boolean actionIsCurrentlyUseable(final Action p_action, final State p_state, final Condition p_condition)
	{
		if(m_synth.getCrafter().actionIsUnlocked(p_action) && p_action.isUseable(p_state, p_condition))
		{
			if(p_action.hasAffinity() && p_action.getAffinity() != m_synth.getCrafter().getActiveClass()
					&& crossClassActionListFull() && !m_crossClassActionsUsed.contains(p_action))
			{
				return false;
			}
			else
			{
				return true;
			}
		}

		return false;
	}

	protected void clearCrossClassActions()
	{
		m_crossClassActionsUsed.clear();
	}

	protected boolean crossClassActionListFull()
	{
		return m_crossClassActionsUsed.size() == MAX_CC_ACTIONS;
	}

	protected abstract ActionValuePair[] getActionsInOrder(final State p_state, final Condition p_condition);

	public Action[] getCrossClassActions()
	{
		return m_crossClassActionsUsed.toArray(new Action[] {});
	}

	public Action getNextAction(final State p_state, final Condition p_condition)
	{
		return maxUseableAction(p_state, p_condition, getActionsInOrder(p_state, p_condition)).getAction();
	}

	public ActionValuePair[] getRecommendedActions(final State p_state, final Condition p_condition)
	{
		final ActionValuePair[] avPairs = getActionsInOrder(p_state, p_condition);
		final ArrayList<ActionValuePair> recommendedActions = new ArrayList<>();
		for(final ActionValuePair avPair: avPairs)
		{
			if(actionIsCurrentlyUseable(avPair.getAction(), p_state, p_condition))
			{
				recommendedActions.add(avPair);
			}
		}

		return recommendedActions.toArray(new ActionValuePair[] {});
	}

	public SynthData getSynth()
	{
		return m_synth;
	}

	private ActionValuePair maxUseableAction(final State p_state, final Condition p_condition,
			final ActionValuePair[] actionsInOrder)
	{
		for(final ActionValuePair avPair: actionsInOrder)
		{
			final Action action = avPair.getAction();
			if(actionIsCurrentlyUseable(action, p_state, p_condition))
			{
				return avPair;
			}
		}
		throw new RuntimeException();
	}

	public void notifyActionUse(final Action p_action)
	{
		if(p_action.hasAffinity() && p_action.getAffinity() != m_synth.getCrafter().getActiveClass())
		{
			// this is a cross class action
			if(crossClassActionListFull() && !m_crossClassActionsUsed.contains(p_action))
			{
				// CCAList is full and we're trying to use something new - wtf, over
				throw new RuntimeException();
			}

			m_crossClassActionsUsed.add(p_action);
		}
	}
}
