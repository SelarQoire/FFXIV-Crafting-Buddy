package crafting;

import java.util.HashMap;

public class CrafterData
{
	private CrafterClass						 m_activeClass;
	private int									 m_activeClassControl;
	private int									 m_activeClassCP;
	private int									 m_activeClassCraftsmanship;
	private boolean								 m_activeClassIsSpecialist;
	private final HashMap<CrafterClass, Integer> m_levels;

	public CrafterData()
	{
		m_levels = new HashMap<>();
	}

	public boolean actionIsUnlocked(final Action p_action)
	{
		if(p_action.hasAffinity() && p_action.getAffinity() != m_activeClass)
		{
			return getLevel(p_action.getAffinity()) >= p_action.getUnlockLevel();
		}
		else if(getActiveClassLevel() < p_action.getUnlockLevel())
		{
			return false;
		}
		else if(p_action.isSpecialistAction() && !m_activeClassIsSpecialist)
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	public CrafterClass getActiveClass()
	{
		return m_activeClass;
	}

	public int getActiveClassControl()
	{
		return m_activeClassControl;
	}

	public int getActiveClassCP()
	{
		return m_activeClassCP;
	}

	public int getActiveClassCraftsmanship()
	{
		return m_activeClassCraftsmanship;
	}

	public int getActiveClassLevel()
	{
		return getLevel(getActiveClass());
	}

	public int getLevel(final CrafterClass p_class)
	{
		return m_levels.get(p_class);
	}

	public boolean isActiveClassSpecialist()
	{
		return m_activeClassIsSpecialist;
	}

	public void setActiveClass(final CrafterClass p_activeClass)
	{
		m_activeClass = p_activeClass;
	}

	public void setActiveClassControl(final int p_activeClassControl)
	{
		m_activeClassControl = p_activeClassControl;
	}

	public void setActiveClassCP(final int p_activeClassCP)
	{
		m_activeClassCP = p_activeClassCP;
	}

	public void setActiveClassCraftsmanship(final int p_activeClassCraftsmanship)
	{
		m_activeClassCraftsmanship = p_activeClassCraftsmanship;
	}

	public void setActiveClassIsSpecialist(final boolean p_activeClassIsSpecialist)
	{
		m_activeClassIsSpecialist = p_activeClassIsSpecialist;
	}

	public void setLevel(final CrafterClass p_class, final int p_level)
	{
		m_levels.put(p_class, p_level);
	}
}
