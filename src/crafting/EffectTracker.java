package crafting;

import java.util.ArrayList;
import java.util.HashMap;

public class EffectTracker
{
	private static final int			   INNER_QUIET_MAX = 10;
	private final HashMap<Action, Integer> m_countDowns;
	private final HashMap<Action, Integer> m_countUps;

	private final ArrayList<Action>		   m_indefinites;

	EffectTracker()
	{
		m_countUps = new HashMap<>();
		m_countDowns = new HashMap<>();
		m_indefinites = new ArrayList<>();
	}

	void addCountDown(final Action p_action, final int p_count)
	{
		if(p_action.getType() != ActionType.COUNT_DOWN)
		{
			throw new RuntimeException();
		}
		m_countDowns.put(p_action, p_count);
	}

	void addCountUp(final Action p_action)
	{
		if(m_countUps.containsKey(p_action) || p_action.getType() != ActionType.COUNT_UP)
		{
			throw new RuntimeException();
		}

		m_countUps.put(p_action, 0);
	}

	void addIndefinite(final Action p_action)
	{
		if(m_indefinites.contains(p_action) || p_action.getType() != ActionType.INDEFINITE)
		{
			throw new RuntimeException();
		}

		m_indefinites.add(p_action);
	}

	void addToCountUp(final Action p_action, final int p_value)
	{
		if(!m_countUps.containsKey(p_action))
		{
			throw new RuntimeException();
		}
		final int current = getCountUpValue(p_action);
		setCountUpValue(p_action, current + p_value);
	}

	public boolean checkInnerQuietEqWhistle()
	{
		if(!containsCountUp(AllActions.INNER_QUIET) || !containsCountDown(AllActions.WHISTLE))
		{
			return false;
		}

		return getCountUpValue(AllActions.INNER_QUIET) + 1 == getCountDownValue(AllActions.WHISTLE);
	}

	public boolean checkWhistleIs3Multiple()
	{
		if(!containsCountDown(AllActions.WHISTLE))
		{
			return false;
		}

		return getCountDownValue(AllActions.WHISTLE) % 3 == 0;
	}

	@Override public EffectTracker clone()
	{
		final EffectTracker ret = new EffectTracker();
		ret.m_countUps.putAll(m_countUps);
		ret.m_countDowns.putAll(m_countDowns);
		ret.m_indefinites.addAll(m_indefinites);
		return ret;
	}

	public boolean containsCountDown(final Action p_action)
	{
		return m_countDowns.containsKey(p_action);
	}

	public boolean containsCountUp(final Action p_action)
	{
		return m_countUps.containsKey(p_action);
	}

	public boolean containsIndefinite(final Action p_action)
	{
		return m_indefinites.contains(p_action);
	}

	void cutIQStackInHalf()
	{
		final int newValue = (int)Math.ceil(getCountUpValue(AllActions.INNER_QUIET) + 1) / 2 - 1;
		setCountUpValue(AllActions.INNER_QUIET, newValue);
	}

	void decrementCountDown(final Action p_action)
	{
		subtractFromCountDown(p_action, 1);
	}

	public Action[] getCountDowns()
	{
		return m_countDowns.keySet().toArray(new Action[] {});
	}

	public int getCountDownValue(final Action p_action)
	{
		final Integer value = m_countDowns.get(p_action);
		if(value == null)
		{
			return 0;
		}
		return value.intValue();
	}

	public Action[] getCountUps()
	{
		return m_countUps.keySet().toArray(new Action[] {});
	}

	public int getCountUpValue(final Action p_action)
	{
		final Integer value = m_countUps.get(p_action);
		if(value == null)
		{
			return 0;
		}
		return value.intValue();
	}

	public Action[] getIndefinites()
	{
		return m_indefinites.toArray(new Action[] {});
	}

	public boolean hasActiveEffects()
	{
		return m_countDowns.size() + m_countUps.size() + m_indefinites.size() != 0;
	}

	void incrementCountUp(final Action p_action)
	{
		addToCountUp(p_action, 1);
	}

	void removeCountDown(final Action p_action)
	{
		if(m_countDowns.remove(p_action) == null)
		{
			throw new RuntimeException();
		}
	}

	void removeCountUp(final Action p_innerQuiet)
	{
		if(m_countUps.remove(p_innerQuiet) == null)
		{
			throw new RuntimeException();
		}
	}

	void removeIndefinite(final Action p_action)
	{
		if(!m_indefinites.remove(p_action))
		{
			throw new RuntimeException();
		}
	}

	private void setCountDownValue(final Action p_action, final int p_value)
	{
		if(!m_countDowns.containsKey(p_action) || p_value < 0)
		{
			throw new RuntimeException();
		}

		m_countDowns.put(p_action, p_value);
	}

	private void setCountUpValue(final Action p_action, int p_value)
	{
		if(p_action == AllActions.INNER_QUIET)
		{
			p_value = Math.min(p_value, INNER_QUIET_MAX);
		}

		m_countUps.put(p_action, p_value);
	}

	private void subtractFromCountDown(final Action p_action, final int p_value)
	{
		final int currentValue = getCountDownValue(p_action);
		setCountDownValue(p_action, currentValue - p_value);
	}
}
