package common;

import java.util.Comparator;

public class AVPairComparator implements Comparator<ActionValuePair>
{
	private final boolean m_sortIncreasing;

	public AVPairComparator(final boolean p_sortIncreasing)
	{
		m_sortIncreasing = p_sortIncreasing;
	}

	@Override public int compare(final ActionValuePair arg1, final ActionValuePair arg2)
	{
		if(arg1.getValue() == arg2.getValue())
		{
			return 0;
		}

		if(m_sortIncreasing)
		{
			return arg1.getValue() > arg2.getValue() ? 1 : -1;
		}
		else
		{
			return arg2.getValue() > arg1.getValue() ? 1 : -1;
		}
	}
}