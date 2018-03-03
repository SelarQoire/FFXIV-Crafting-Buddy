package crafting;

public class Action
{
	private final CrafterClass m_affinity;
	private final int		   m_cpCost;
	private final int		   m_durabilityCost;
	private final String	   m_name;
	private final double	   m_progressMultiplier;
	private final double	   m_qualityMultiplier;
	private final boolean	   m_specialistAction;
	private final double	   m_successProbability;
	private final ActionType   m_type;
	private final int		   m_unlockLevel;

	public Action(final String p_name, final int p_unlockLevel, final CrafterClass p_affinity, final int p_cpCost,
			final double p_successProbability, final double p_progressMultiplier, final double p_qualityMultiplier,
			final int p_durabilityCost, final ActionType p_type, final boolean p_specialistAction)
	{
		m_name = p_name;
		m_unlockLevel = p_unlockLevel;
		m_affinity = p_affinity;
		m_cpCost = p_cpCost;
		m_successProbability = p_successProbability;
		m_progressMultiplier = p_progressMultiplier;
		m_qualityMultiplier = p_qualityMultiplier;
		m_durabilityCost = p_durabilityCost;
		m_type = p_type;
		m_specialistAction = p_specialistAction;
	}

	public CrafterClass getAffinity()
	{
		return m_affinity;
	}

	public int getCPCost()
	{
		return m_cpCost;
	}

	public int getDurabilityCost()
	{
		return m_durabilityCost;
	}

	public String getName()
	{
		return m_name;
	}

	public double getProgressMultiplier()
	{
		return m_progressMultiplier;
	}

	public double getQualityMultiplier()
	{
		return m_qualityMultiplier;
	}

	public double getSuccessProbability()
	{
		return m_successProbability;
	}

	public ActionType getType()
	{
		return m_type;
	}

	public int getUnlockLevel()
	{
		return m_unlockLevel;
	}

	public boolean hasAffinity()
	{
		return m_affinity != CrafterClass.ALL;
	}

	public boolean isSpecialistAction()
	{
		return m_specialistAction;
	}

	public boolean isUseable(final State p_state, final Condition p_condition)
	{
		if(this.getType() == ActionType.INDEFINITE && p_state.getEffects().containsIndefinite(this))
		{
			return false;
		}

		if(m_cpCost > p_state.getCPState())
		{
			return false;
		}

		if(this.m_type == ActionType.UNUSABLE)
		{
			return false;
		}

		if(this == AllActions.MUSCLE_MEMORY || this == AllActions.MAKERS_MARK
				|| this == AllActions.INITIAL_PREPARATIONS)
		{
			return p_state.getStep() == 1;
		}

		if(this instanceof NameOfAction)
		{
			return p_state.getNameOfElementUses() == 0;
		}

		if(this == AllActions.BYREGOTS_BLESSING || this == AllActions.BYREGOTS_BROW || this == AllActions.RUMINATION)
		{
			return p_state.getEffects().getCountUpValue(AllActions.INNER_QUIET) >= 1;
		}

		if(this == AllActions.PRUDENT_TOUCH)
		{
			final boolean wn1or2Active = p_state.getEffects().containsCountDown(AllActions.WASTE_NOT)
					|| p_state.getEffects().containsCountDown(AllActions.WASTE_NOT_2);
			return !wn1or2Active;
		}

		if(this == AllActions.NYMEIAS_WHEEL)
		{
			final boolean whistleActive = p_state.getEffects().containsCountUp(AllActions.WHISTLE);
			return whistleActive;
		}

		if(this == AllActions.TRAINED_HAND)
		{
			final boolean iqEqualsWhistle = p_state.getEffects().checkInnerQuietEqWhistle();
			return iqEqualsWhistle;
		}

		final boolean iqIsActive = p_state.getEffects().containsCountUp(AllActions.INNER_QUIET);
		final boolean goodOrExcellent = p_condition.checkGoodOrExcellent();
		final boolean isSpecialist = p_state.getSynth().getCrafter().isActiveClassSpecialist();
		final boolean ipIsActive = p_state.getEffects().containsIndefinite(AllActions.INITIAL_PREPARATIONS);

		if(this == AllActions.SATISFACTION)
		{
			final boolean whistleStackIsThreeMutliple = p_state.getEffects().containsCountDown(AllActions.WHISTLE)
					&& p_state.getEffects().getCountDownValue(AllActions.WHISTLE) % 3 == 0;
			return isSpecialist && whistleStackIsThreeMutliple;
		}

		if(this == AllActions.PATIENT_TOUCH)
		{
			return iqIsActive;
		}

		if(this == AllActions.PRECISE_TOUCH)
		{
			return goodOrExcellent && iqIsActive;
		}

		if(this == AllActions.TRICKS_OF_THE_TRADE)
		{
			return goodOrExcellent;
		}

		if(this == AllActions.INNER_QUIET)
		{
			return !iqIsActive;
		}

		if(this == AllActions.WHISTLE || this == AllActions.INNOVATIVE_TOUCH)
		{
			return isSpecialist;
		}

		if(this == AllActions.BYREGOTS_MIRACLE)
		{
			return isSpecialist && iqIsActive;
		}

		if(this == AllActions.SPECIALTY_REFURBISH || this == AllActions.SPECIALTY_REINFORCE)
		{
			return isSpecialist && ipIsActive;
		}

		if(this == AllActions.SPECIALTY_REFLECT)
		{
			return isSpecialist && ipIsActive && iqIsActive;
		}

		return true;
	}

	public int numActiveTurns()
	{
		if(m_type != ActionType.COUNT_DOWN)
		{
			throw new RuntimeException();
		}

		if(this == AllActions.COMFORT_ZONE)
		{
			return 10;
		}
		if(this == AllActions.GREAT_STRIDES)
		{
			return 3;
		}
		if(this == AllActions.INGENUITY)
		{
			return 5;
		}
		if(this == AllActions.INGENUITY_2)
		{
			return 5;
		}
		if(this == AllActions.INNOVATION)
		{
			return 3;
		}
		if(this == AllActions.MAKERS_MARK)
		{
			// this should not be decided here - it is variable based on recipe
			// difficulty
		}
		if(this == AllActions.MANIPULATION)
		{
			return 3;
		}
		if(this == AllActions.MANIPULATION_2)
		{
			return 8;
		}
		if(this == AllActions.STEADY_HAND)
		{
			return 5;
		}
		if(this == AllActions.STEADY_HAND_2)
		{
			return 5;
		}
		if(this == AllActions.WASTE_NOT)
		{
			return 4;
		}
		if(this == AllActions.WASTE_NOT_2)
		{
			return 8;
		}
		if(this == AllActions.WHISTLE)
		{
			return 11;
		}
		if(this instanceof NameOfAction)
		{
			return 5;
		}
		throw new RuntimeException();
	}

	@Override public String toString()
	{
		return getName();
	}
}
