package synthSim;

import crafting.CrafterClass;
import crafting.Element;

public class RecipeData
{
	private final Element	   m_aspect;
	private final CrafterClass m_class;
	private final int		   m_difficulty;
	private final int		   m_durability;
	private final int		   m_effLevel;
	private final int		   m_maxQuality;
	private final int		   m_nomLevel;

	public RecipeData(final CrafterClass p_class, final int p_nomLevel, final int p_effLevel, final int p_difficulty,
			final int p_durability, final int p_maxQuality, final Element p_aspect)
	{
		m_class = p_class;
		m_nomLevel = p_nomLevel;
		m_effLevel = p_effLevel;
		m_difficulty = p_difficulty;
		m_maxQuality = p_maxQuality;
		m_durability = p_durability;
		m_aspect = p_aspect;
	}

	public Element getAspect()
	{
		return m_aspect;
	}

	public CrafterClass getCClass()
	{
		return m_class;
	}

	public int getDifficulty()
	{
		return m_difficulty;
	}

	public int getDurability()
	{
		return m_durability;
	}

	public int getEffLevel()
	{
		return m_effLevel;
	}

	public int getMaxQuality()
	{
		return m_maxQuality;
	}

	public int getNomLevel()
	{
		return m_nomLevel;
	}

	public boolean hasAspect()
	{
		return m_aspect != Element.NONE;
	}
}
