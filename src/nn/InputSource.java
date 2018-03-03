package nn;

import crafting.AllActions;
import crafting.Condition;
import crafting.EffectTracker;
import crafting.Simulator;
import crafting.State;

public enum InputSource
{
	COMFORT_ZONE_COUNT, CP, DIFFICULTY, DURABILITY, GREAT_STRIDES_COUNT, GREAT_STRIDES_ON, INGENUITY_2_COUNT, INGENUITY_COUNT, INITIAL_PREPARATIONS_ON, INNOVATION_COUNT, IQ_COUNT, IQ_ON, IS_EXCELLENT, IS_GOOD, IS_POOR, IS_SPECIALIST, MAKERS_MARK_COUNT, MANIPULATION_2_COUNT, MANIPULATION_COUNT, MATS_VAL_FRACTION, MAX_CP, MAX_DURABILITY, MAX_MAKERS_MARK_COUNT, MAX_QUALITY, NAME_OF_USED, NO_EARTH_COUNT, NO_FIRE_COUNT, NO_ICE_COUNT, NO_LIGHTNING_COUNT, NO_WATER_COUNT, NO_WIND_COUNT, NQ_VAL_FRACTION, OBSERVE_WAS_LAST_ACTION, PROGRESS, QUALITY, RECLAIM_ON, STEADY_HAND_2_COUNT, STEADY_HAND_COUNT, WASTE_NOT_MAX_COUNT, WHISTLE_COUNT, WHISTLE_EQUALS_IQ, WHISTLE_IS_3_MULTIPLE;

	public double getInputValue(final State p_state, final Condition p_condition)
	{
		final EffectTracker effects = p_state.getEffects();
		double value = 0;
		switch(this)
		{
			case COMFORT_ZONE_COUNT:
				value = effects.getCountDownValue(AllActions.COMFORT_ZONE);
				break;
			case DURABILITY:
				value = p_state.getDurabilityState();
				break;
			case GREAT_STRIDES_COUNT:
				value = effects.getCountDownValue(AllActions.GREAT_STRIDES);
				break;
			case INGENUITY_2_COUNT:
				value = effects.getCountDownValue(AllActions.INGENUITY_2);
				break;
			case INGENUITY_COUNT:
				value = effects.getCountDownValue(AllActions.INGENUITY);
				break;
			case INITIAL_PREPARATIONS_ON:
				value = effects.containsIndefinite(AllActions.INITIAL_PREPARATIONS) ? 1 : 0;
				break;
			case IQ_COUNT:
				value = effects.getCountUpValue(AllActions.INNER_QUIET);
				break;
			case INNOVATION_COUNT:
				value = effects.getCountDownValue(AllActions.INNOVATION);
				break;
			case IS_EXCELLENT:
				value = p_condition == Condition.EXCELLENT ? 1 : 0;
				break;
			case IS_GOOD:
				value = p_condition == Condition.GOOD ? 1 : 0;
				break;
			case IS_POOR:
				value = p_condition == Condition.POOR ? 1 : 0;
				break;
			case MAKERS_MARK_COUNT:
				value = effects.getCountDownValue(AllActions.MAKERS_MARK);
				break;
			case MANIPULATION_2_COUNT:
				value = effects.getCountDownValue(AllActions.MANIPULATION_2);
				break;
			case MANIPULATION_COUNT:
				value = effects.getCountDownValue(AllActions.MANIPULATION);
				break;
			case NO_EARTH_COUNT:
				value = effects.getCountDownValue(AllActions.NAME_OF_EARTH);
				break;
			case NO_FIRE_COUNT:
				value = effects.getCountDownValue(AllActions.NAME_OF_FIRE);
				break;
			case NO_ICE_COUNT:
				value = effects.getCountDownValue(AllActions.NAME_OF_ICE);
				break;
			case NO_LIGHTNING_COUNT:
				value = effects.getCountDownValue(AllActions.NAME_OF_LIGHTNING);
				break;
			case NO_WATER_COUNT:
				value = effects.getCountDownValue(AllActions.NAME_OF_WATER);
				break;
			case OBSERVE_WAS_LAST_ACTION:
				value = p_state.getAction() == AllActions.OBSERVE ? 1 : 0;
				break;
			case PROGRESS:
				value = p_state.getProgressState();
				break;
			case QUALITY:
				value = p_state.getQualityState();
				break;
			case RECLAIM_ON:
				value = effects.containsIndefinite(AllActions.RECLAIM) ? 1 : 0;
				break;
			case STEADY_HAND_2_COUNT:
				value = effects.getCountDownValue(AllActions.STEADY_HAND_2);
				break;
			case STEADY_HAND_COUNT:
				final int sh1Count = effects.getCountDownValue(AllActions.STEADY_HAND);
				final int sh2Count = effects.getCountDownValue(AllActions.STEADY_HAND_2);
				value = sh1Count > sh2Count ? sh1Count - sh2Count : 0;
				break;
			case WASTE_NOT_MAX_COUNT:
				value = Math.max(effects.getCountDownValue(AllActions.WASTE_NOT),
						effects.getCountDownValue(AllActions.WASTE_NOT_2));
				break;
			case WHISTLE_COUNT:
				value = effects.getCountDownValue(AllActions.WHISTLE);
				break;
			case WHISTLE_EQUALS_IQ:
				value = effects.checkInnerQuietEqWhistle() ? 1 : 0;
				break;
			case WHISTLE_IS_3_MULTIPLE:
				value = effects.checkWhistleIs3Multiple() ? 1 : 0;
				break;
			case CP:
				value = p_state.getCPState();
				break;
			case NAME_OF_USED:
				value = p_state.getNameOfElementUses();
				break;
			case NO_WIND_COUNT:
				value = effects.getCountDownValue(AllActions.NAME_OF_WIND);
				break;
			case GREAT_STRIDES_ON:
				value = p_state.getEffects().containsCountDown(AllActions.GREAT_STRIDES) ? 1 : 0;
				break;
			case IQ_ON:
				value = p_state.getEffects().containsCountUp(AllActions.INNER_QUIET) ? 1 : 0;
				break;
			case DIFFICULTY:
				value = p_state.getSynth().getRecipe().getDifficulty();
				break;
			case MAX_CP:
				value = p_state.getSynth().getCrafter().getActiveClassCP() + p_state.getBonusMaxCP();
				break;
			case MAX_DURABILITY:
				value = p_state.getSynth().getRecipe().getDurability();
				break;
			case MAX_MAKERS_MARK_COUNT:
				value = Simulator.getMakersMarkStackSize(p_state.getSynth());
				break;
			case MAX_QUALITY:
				value = p_state.getSynth().getRecipe().getMaxQuality();
				break;
			case IS_SPECIALIST:
				value = p_state.getSynth().getCrafter().isActiveClassSpecialist() ? 1 : 0;
				break;
			case MATS_VAL_FRACTION:
				value = p_state.getSynth().getMatsValueFraction();
				break;
			case NQ_VAL_FRACTION:
				value = p_state.getSynth().getNqValueFraction();
				break;
		}

		return value / getNormalizer();
	}

	public int getNormalizer()
	{
		switch(this)
		{
			case COMFORT_ZONE_COUNT:
				return 8;
			case CP:
				return 600;
			case DURABILITY:
				return 80;
			case GREAT_STRIDES_COUNT:
				return 3;
			case INGENUITY_2_COUNT:
				return 5;
			case INGENUITY_COUNT:
				return 5;
			case INITIAL_PREPARATIONS_ON:
				return 1;
			case IQ_COUNT:
				return 10;
			case INNOVATION_COUNT:
				return 3;
			case IS_EXCELLENT:
				return 1;
			case IS_GOOD:
				return 1;
			case IS_POOR:
				return 1;
			case MAKERS_MARK_COUNT:
				return 100;
			case MANIPULATION_2_COUNT:
				return 8;
			case MANIPULATION_COUNT:
				return 3;
			case NAME_OF_USED:
				return 1;
			case NO_EARTH_COUNT:
				return 5;
			case NO_FIRE_COUNT:
				return 5;
			case NO_ICE_COUNT:
				return 5;
			case NO_LIGHTNING_COUNT:
				return 5;
			case NO_WATER_COUNT:
				return 5;
			case NO_WIND_COUNT:
				return 5;
			case OBSERVE_WAS_LAST_ACTION:
				return 1;
			case PROGRESS:
				return 5000;
			case QUALITY:
				return 5000;
			case RECLAIM_ON:
				return 1;
			case STEADY_HAND_2_COUNT:
				return 5;
			case STEADY_HAND_COUNT:
				return 5;
			case WASTE_NOT_MAX_COUNT:
				return 8;
			case WHISTLE_COUNT:
				return 11;
			case WHISTLE_EQUALS_IQ:
				return 1;
			case WHISTLE_IS_3_MULTIPLE:
				return 1;
			case GREAT_STRIDES_ON:
				return 1;
			case IQ_ON:
				return 1;
			case DIFFICULTY:
				return 5000;
			case MAX_CP:
				return 600;
			case MAX_DURABILITY:
				return 80;
			case MAX_MAKERS_MARK_COUNT:
				return 50;
			case MAX_QUALITY:
				return 5000;
			case IS_SPECIALIST:
				return 1;
			case MATS_VAL_FRACTION:
				return 1;
			case NQ_VAL_FRACTION:
				return 1;
		}

		throw new RuntimeException();
	}
}
