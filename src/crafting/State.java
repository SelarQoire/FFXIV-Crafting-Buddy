package crafting;

public class State
{
	private Action				m_action;
	private int					m_bonusMaxCP;
	private int					m_cpState;
	private int					m_durabilityState;
	private final EffectTracker	m_effects;
	private int					m_lastStep;
	private int					m_nameOfElementUses;
	private int					m_progressState;
	private int					m_qualityState;
	private int					m_step;
	private final SynthData		m_synthData;

	State(final SynthData p_synthData, final int p_step, final int p_lastStep, final Action p_action,
			final int p_durabilityState, final int p_cpState, final int p_bonusMaxCP, final int p_qualityState,
			final int p_progressState, final int p_nameOfElementUses, final EffectTracker p_effects)
	{
		m_synthData = p_synthData;
		m_step = p_step;
		m_lastStep = p_lastStep;
		m_action = p_action; // the action leading to this State
		m_durabilityState = p_durabilityState;
		m_cpState = p_cpState;
		m_bonusMaxCP = p_bonusMaxCP;
		m_qualityState = p_qualityState;
		m_progressState = p_progressState;
		m_nameOfElementUses = p_nameOfElementUses;
		m_effects = p_effects;
	}

	void addCP(final int p_cp)
	{
		m_cpState += p_cp;
		if(m_cpState > m_synthData.getCrafter().getActiveClassCP() + m_bonusMaxCP)
		{
			m_cpState = m_synthData.getCrafter().getActiveClassCP() + m_bonusMaxCP;
		}
	}

	void addDurability(final int p_durability)
	{
		m_durabilityState += p_durability;
		if(m_durabilityState > m_synthData.getRecipe().getDurability())
		{
			m_durabilityState = m_synthData.getRecipe().getDurability();
		}
	}

	void addProgress(final int p_progressGain)
	{
		m_progressState += p_progressGain;
		if(m_progressState > m_synthData.getRecipe().getDifficulty())
		{
			m_progressState = m_synthData.getRecipe().getDifficulty();
		}
	}

	void addQuality(final int p_qualityGain)
	{
		m_qualityState += p_qualityGain;
		if(m_qualityState > m_synthData.getRecipe().getMaxQuality())
		{
			m_qualityState = m_synthData.getRecipe().getMaxQuality();
		}
	}

	@Override public State clone()
	{
		return new State(m_synthData, m_step, m_lastStep, m_action, m_durabilityState, m_cpState, m_bonusMaxCP,
				m_qualityState, m_progressState, m_nameOfElementUses, m_effects.clone());
	}

	public Action getAction()
	{
		return m_action;
	}

	public int getBonusMaxCP()
	{
		return m_bonusMaxCP;
	}

	public int getCPState()
	{
		return m_cpState;
	}

	public int getDurabilityState()
	{
		return m_durabilityState;
	}

	public EffectTracker getEffects()
	{
		return m_effects;
	}

	public int getNameOfElementUses()
	{
		return m_nameOfElementUses;
	}

	public double getProgressPercent()
	{
		return (double)m_progressState / m_synthData.getRecipe().getDifficulty();
	}

	public int getProgressState()
	{
		return m_progressState;
	}

	public int getQualityState()
	{
		return m_qualityState;
	}

	public int getStep()
	{
		return m_step;
	}

	public SynthData getSynth()
	{
		return m_synthData;
	}

	void incrementLastStep()
	{
		m_lastStep++;
	}

	void incrementStep()
	{
		m_step++;
	}

	void setAction(final Action p_action)
	{
		m_action = p_action;
	}

	void setBonusMaxCp(final int p_cp)
	{
		m_bonusMaxCP = p_cp;
	}

	void setCPState(final int p_newCP)
	{
		m_cpState = p_newCP;
	}

	void setDurabilityState(final int p_durabilityState)
	{
		m_durabilityState = p_durabilityState;
	}

	void setNameOfElementUses(final int p_uses)
	{
		m_nameOfElementUses = p_uses;
	}

	void subtractCP(final int p_cpCost)
	{
		m_cpState -= p_cpCost;
	}

	void subtractDurability(final int p_durabilityCost)
	{
		m_durabilityState -= p_durabilityCost;
	}
}
