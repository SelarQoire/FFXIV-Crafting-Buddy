package q;

import java.util.ArrayList;
import java.util.Random;

import crafting.Action;
import crafting.Condition;
import crafting.State;

public class MemoryStore
{
	private final ArrayList<MemoryStoreData> m_list;
	private final int						 m_maxCapacity;
	private final Random					 m_random;

	public MemoryStore(final int p_maxCapacity)
	{
		m_maxCapacity = p_maxCapacity;
		m_list = new ArrayList<>(m_maxCapacity);
		m_random = new Random();
	}

	private void addMemory(final MemoryStoreData p_scp)
	{
		if(m_list.size() < m_maxCapacity)
		{
			m_list.add(p_scp);
		}
		else
		{
			final int index = m_random.nextInt(m_list.size());
			m_list.set(index, p_scp);
		}
	}

	public void addMemory(final State p_state, final Condition p_condition, final Action[] p_availableActions)
	{
		addMemory(new MemoryStoreData(p_state, p_condition, p_availableActions));
	}

	public MemoryStoreData getMemory()
	{
		final int index = m_random.nextInt(m_list.size());
		return m_list.get(index);
	}
}
