package crafting;

import java.util.Arrays;
import java.util.Random;

import common.ActionValuePair;
import common.Policy;
import common.TextIO;
import common.Utilities;

public class Simulator
{
	private static final Random	m_random;

	private static final int	MAX_BSEARCH_ITERS = 100;
	private static final double	P_EXCELLENT		  = .05;

	private static final double	P_GOOD			  = .25;

	static
	{
		m_random = new Random();
	}

	private static Step applyModifiers(final State p_state, final Condition p_condition, final Action p_action)
	{
		// Effect Modifiers
		// =================
		final int baseCraftsmanship = p_state.getSynth().getCrafter().getActiveClassCraftsmanship();
		final int baseControl = p_state.getSynth().getCrafter().getActiveClassControl();
		int control = baseControl;
		final int craftsmanship = baseCraftsmanship;
		final EffectTracker effects = p_state.getEffects();

		// Effects modifying control
		if(effects.containsCountUp(AllActions.INNER_QUIET))
		{
			control += 0.2 * effects.getCountUpValue(AllActions.INNER_QUIET) * baseControl;
		}

		if(effects.containsCountDown(AllActions.INNOVATION))
		{
			control += 0.5 * baseControl;
		}

		// Effects modifying level difference
		final int nominalCrafterLevel = p_state.getSynth().getCrafter().getActiveClassLevel();
		final int effCrafterLevel = LevelTable.getEffectiveCrafterLevel(nominalCrafterLevel);

		double effRecipeLevel = p_state.getSynth().getRecipe().getEffLevel();
		double levelDifference = effCrafterLevel - effRecipeLevel;

		if(effects.containsCountDown(AllActions.INGENUITY_2))
		{
			if(LevelTable.containsIngenuityTwoLevel(effRecipeLevel))
			{
				effRecipeLevel = LevelTable.getIngenuityTwoLevel(effRecipeLevel);
				levelDifference = effCrafterLevel - effRecipeLevel;
			}
			else
			{
				levelDifference = effCrafterLevel - (effRecipeLevel - 7); // fall
																		  // back
																		  // on
																		  // 2.2
																		  // estimate
			}

			if(levelDifference < 0)
			{
				levelDifference = Math.max(levelDifference, -5);
			}
		}
		else if(effects.containsCountDown(AllActions.INGENUITY))
		{
			if(LevelTable.containsIngenuityLevel(effRecipeLevel))
			{
				effRecipeLevel = LevelTable.getIngenuityLevel(effRecipeLevel);
				levelDifference = effCrafterLevel - effRecipeLevel;
			}
			else
			{
				levelDifference = effCrafterLevel - (effRecipeLevel - 5); // fall
																		  // back
																		  // on
																		  // 2.2
																		  // estimate
			}

			if(levelDifference < 0)
			{
				levelDifference = Math.max(levelDifference, -5);
			}
		}

		// Effects modifying probability
		double successProbability = p_action.getSuccessProbability();
		double ftSuccessProbability = AllActions.FINISHING_TOUCHES.getSuccessProbability();
		if(p_action == AllActions.FOCUSED_SYNTHESIS || p_action == AllActions.FOCUSED_TOUCH)
		{
			if(p_state.getAction() == AllActions.OBSERVE)
			{
				successProbability = 1.0;
			}
		}
		if(p_action == AllActions.BYREGOTS_BROW && p_state.getSynth().getCrafter().isActiveClassSpecialist())
		{
			successProbability += 0.3;
		}

		if(effects.containsCountDown(AllActions.STEADY_HAND_2))
		{
			successProbability += 0.3; // Assume 2 always overrides 1
			ftSuccessProbability += 0.3;
		}
		else if(effects.containsCountDown(AllActions.STEADY_HAND))
		{
			successProbability += 0.2;
			ftSuccessProbability += 0.2;
		}

		successProbability = Math.min(successProbability, 1);
		ftSuccessProbability = Math.min(ftSuccessProbability, 1);

		// Effects modifying progress increase multiplier
		double progressIncreaseMultiplier = p_action.getProgressMultiplier();
		double ftMultiplier = 1.0;

		// Brand actions
		if(p_action instanceof BrandOfAction)
		{
			final BrandOfAction boa = (BrandOfAction)p_action;
			final Element element = boa.getAspect();
			double nameOfMultiplier = 1;
			final NameOfAction correspondingName = boa.getCorrespondingNameOfAction();
			if(effects.containsCountDown(correspondingName))
			{
				nameOfMultiplier = calcNameOfMultiplier(p_state);
			}

			progressIncreaseMultiplier *= nameOfMultiplier;
			if(p_state.getSynth().getRecipe().getAspect() == element)
			{
				progressIncreaseMultiplier *= 2;
			}
		}

		// Aspected recipes give a global 50% progress penalty, and using the
		// matching Brand just negates it
		if(p_state.getSynth().getRecipe().getAspect() != Element.NONE)
		{
			progressIncreaseMultiplier *= 0.5;
			ftMultiplier *= 0.5;
		}

		// Effects modified by Whistle While You Work
		if(effects.checkWhistleIs3Multiple())
		{
			if(progressIncreaseMultiplier > 0)
			{
				progressIncreaseMultiplier += 0.5;
			}
		}

		// Effects modifying quality increase multiplier
		double qualityIncreaseMultiplier = p_action.getQualityMultiplier();

		// We can only use Byregot actions when we have at least 2 stacks of
		// inner quiet
		if(p_action == AllActions.BYREGOTS_BLESSING)
		{
			if(effects.getCountUpValue(AllActions.INNER_QUIET) >= 1)
			{
				qualityIncreaseMultiplier += 0.2 * effects.getCountUpValue(AllActions.INNER_QUIET);
			}
			else
			{
				throw new RuntimeException();
			}
		}
		if(p_action == AllActions.BYREGOTS_MIRACLE)
		{
			if(effects.getCountUpValue(AllActions.INNER_QUIET) >= 1)
			{
				qualityIncreaseMultiplier += 0.15 * effects.getCountUpValue(AllActions.INNER_QUIET);
			}
			else
			{
				throw new RuntimeException();
			}
		}
		if(p_action == AllActions.BYREGOTS_BROW)
		{
			if(effects.getCountUpValue(AllActions.INNER_QUIET) >= 1)
			{
				qualityIncreaseMultiplier += 0.1 * effects.getCountUpValue(AllActions.INNER_QUIET);
			}
			else
			{
				throw new RuntimeException();
			}
		}

		if(effects.containsCountDown(AllActions.GREAT_STRIDES))
		{
			qualityIncreaseMultiplier *= 2;
		}

		// Effects modifying progress
		int bProgressGain = (int)(progressIncreaseMultiplier * calculateBaseProgressIncrease(levelDifference,
				craftsmanship, effCrafterLevel, p_state.getSynth().getRecipe().getEffLevel()));
		if(p_action == AllActions.FLAWLESS_SYNTHESIS)
		{
			bProgressGain = 40;
		}
		else if(p_action == AllActions.PIECE_BY_PIECE)
		{
			bProgressGain = (int)((p_state.getSynth().getRecipe().getDifficulty() - p_state.getProgressState()) * 0.33);
		}
		else if(p_action == AllActions.MUSCLE_MEMORY)
		{
			if(p_state.getStep() == 1)
			{
				bProgressGain = (int)((p_state.getSynth().getRecipe().getDifficulty() - p_state.getProgressState())
						* 0.33);
			}
			else
			{
				throw new RuntimeException();
			}
		}

		// Effects modifying quality
		final int bQualityGain = (int)(qualityIncreaseMultiplier * calculateBaseQualityIncrease(levelDifference,
				control, effCrafterLevel, p_state.getSynth().getRecipe().getEffLevel()));

		// We can only use Precise Touch when state material condition is Good
		// or Excellent. Default is true for probabilistic method.
		if(p_action == AllActions.PRECISE_TOUCH)
		{
			if(!p_condition.checkGoodOrExcellent())
			{
				throw new RuntimeException();
			}
		}
		if(p_action == AllActions.TRAINED_HAND && !effects.checkInnerQuietEqWhistle())
		{
			throw new RuntimeException();
		}

		// Effects modifying durability cost
		int durabilityCost = p_action.getDurabilityCost();
		int ftDurabilityCost = AllActions.FINISHING_TOUCHES.getDurabilityCost();
		if(effects.containsCountDown(AllActions.WASTE_NOT) || effects.containsCountDown(AllActions.WASTE_NOT_2))
		{
			if(p_action == AllActions.PRUDENT_TOUCH)
			{
				throw new RuntimeException();
			}
			else
			{
				durabilityCost *= 0.5;
				ftDurabilityCost *= 0.5;
			}
		}
		if(effects.containsCountDown(AllActions.MAKERS_MARK) && p_action == AllActions.FLAWLESS_SYNTHESIS)
		{
			durabilityCost *= 0;
		}

		// Effects modifying cp cost
		int cpCost = p_action.getCPCost();
		if(effects.containsCountDown(AllActions.MAKERS_MARK) && p_action == AllActions.FLAWLESS_SYNTHESIS)
		{
			cpCost = 0;
		}

		/*
		 * If Whistle is at 1 and a good/excellent occurs, at the end of the action,
		 * whistle will decrement and Finishing Touches will occur Finishing Touches is
		 * 200% efficiency, 50% success (?) and 10 (?) durability
		 */
		if(effects.getCountDownValue(AllActions.WHISTLE) == 1 && p_condition.checkGoodOrExcellent())
		{
			final double successRand = Math.random();
			if(successRand <= ftSuccessProbability)
			{
				bProgressGain += AllActions.FINISHING_TOUCHES.getProgressMultiplier() * ftMultiplier
						* calculateBaseProgressIncrease(levelDifference, craftsmanship, effCrafterLevel,
								p_state.getSynth().getRecipe().getEffLevel());
				durabilityCost += ftDurabilityCost;
			}
		}

		final Step ret = new Step();
		ret.setSuccessProbability(successProbability);
		ret.setProgressGain(bProgressGain);
		ret.setQualityGain(bQualityGain);
		ret.setDurabilityCost(durabilityCost);
		ret.setCPCost(cpCost);

		return ret;
	}

	private static void applySpecialActionEffects(final State p_state, final Condition p_condition,
			final Action p_action, final boolean p_success)
	{
		final EffectTracker effects = p_state.getEffects();
		// STEP_02
		// Effect management
		// ==================================
		// Special Effect Actions
		if(p_action == AllActions.MASTERS_MEND)
		{
			p_state.addDurability(30);
		}

		if(p_action == AllActions.MASTERS_MEND_2)
		{
			p_state.addDurability(60);
		}

		if(effects.containsCountDown(AllActions.MANIPULATION) && p_state.getDurabilityState() > 0
				&& p_action != AllActions.MANIPULATION && p_action != AllActions.MANIPULATION_2)
		{
			p_state.addDurability(10);
		}

		if(effects.containsCountDown(AllActions.MANIPULATION_2) && p_state.getDurabilityState() > 0
				&& p_action != AllActions.MANIPULATION && p_action != AllActions.MANIPULATION_2)
		{
			p_state.addDurability(5);
		}

		if(p_action == AllActions.SPECIALTY_REINFORCE && p_state.getDurabilityState() > 0)
		{
			if(effects.containsIndefinite(AllActions.INITIAL_PREPARATIONS))
			{
				p_state.addDurability(25);
				effects.removeIndefinite(AllActions.INITIAL_PREPARATIONS);
			}
			else
			{
				throw new RuntimeException();
			}
		}

		if(p_action == AllActions.NYMEIAS_WHEEL)
		{
			if(effects.containsCountDown(AllActions.WHISTLE))
			{
				final int whistleStack = effects.getCountDownValue(AllActions.WHISTLE);
				final int durabilityRestore = LevelTable.getNymeiasWheelDurabilityRestore(whistleStack);
				p_state.addDurability(durabilityRestore);
				effects.removeCountDown(AllActions.WHISTLE);
			}
			else
			{
				throw new RuntimeException();
			}
		}

		if(p_action != AllActions.COMFORT_ZONE && effects.containsCountDown(AllActions.COMFORT_ZONE)
				&& p_state.getCPState() >= 0)
		{
			p_state.addCP(8);
		}

		if(p_action == AllActions.RUMINATION && p_state.getCPState() >= 0)
		{
			if(effects.containsCountUp(AllActions.INNER_QUIET) && effects.getCountUpValue(AllActions.INNER_QUIET) > 0)
			{
				final int cpToAdd = ruminationCPGain(effects);
				p_state.addCP(cpToAdd);
				effects.removeCountUp(AllActions.INNER_QUIET);
			}
			else
			{
				throw new RuntimeException();
			}
		}

		if(p_action == AllActions.SPECIALTY_REFURBISH && p_state.getCPState() >= 0)
		{
			if(effects.containsIndefinite(AllActions.INITIAL_PREPARATIONS))
			{
				p_state.addCP(65);
				effects.removeIndefinite(AllActions.INITIAL_PREPARATIONS);
			}
			else
			{
				throw new RuntimeException();
			}
		}

		if(p_action == AllActions.BYREGOTS_BLESSING || p_action == AllActions.BYREGOTS_BROW)
		{
			if(effects.containsCountUp(AllActions.INNER_QUIET))
			{
				effects.removeCountUp(AllActions.INNER_QUIET);
			}
			else
			{
				throw new RuntimeException();
			}
		}

		if(p_action == AllActions.BYREGOTS_MIRACLE)
		{
			// We can only use Byregot's Miracle when we have at least 2 stacks
			// of inner quiet
			if(effects.getCountUpValue(AllActions.INNER_QUIET) >= 1)
			{
				effects.cutIQStackInHalf();
			}
			else
			{
				throw new RuntimeException();
			}
		}

		if(p_action == AllActions.SPECIALTY_REFLECT)
		{
			if(effects.containsIndefinite(AllActions.INITIAL_PREPARATIONS))
			{
				effects.addToCountUp(AllActions.INNER_QUIET, 3);
				effects.removeIndefinite(AllActions.INITIAL_PREPARATIONS);
			}
			else
			{
				throw new RuntimeException();
			}
		}

		if(p_success && p_action.getQualityMultiplier() > 0 && effects.containsCountDown(AllActions.GREAT_STRIDES))
		{
			effects.removeCountDown(AllActions.GREAT_STRIDES);
		}

		if(p_action == AllActions.TRICKS_OF_THE_TRADE)
		{
			if(p_condition.checkGoodOrExcellent())
			{
				p_state.addCP(20);
			}
			else
			{
				throw new RuntimeException();
			}
		}

		// Effects modified by Whistle While You Work
		if(p_action == AllActions.SATISFACTION && p_state.getCPState() > 0)
		{
			if(effects.checkWhistleIs3Multiple())
			{
				p_state.addCP(15);
			}
			else
			{
				throw new RuntimeException();
			}
		}

		if(p_state.getStep() == 1 && p_state.getSynth().getCrafter().isActiveClassSpecialist()
				&& p_state.getSynth().getCrafter().getActiveClassLevel() >= 70 && p_state.getCPState() > 0)
		{
			effects.addIndefinite(AllActions.STROKE_OF_GENIUS);
			p_state.setBonusMaxCp(15);
			p_state.addCP(15);
		}
	}

	private static double calcNameOfMultiplier(final State p_state)
	{
		/*
		 * From http://redd.it/3ejmp2 and http://redd.it/3d3meb Assume for now that the
		 * function is linear, but capped with a minimum of 110%
		 */
		double nameOfMultiplier = -2 * (p_state.getProgressState() / p_state.getSynth().getRecipe().getDifficulty())
				+ 3;
		nameOfMultiplier = Math.max(nameOfMultiplier, 1.1);

		return nameOfMultiplier;
	}

	private static double calculateBaseProgressIncrease(final double p_levelDifference, final int p_craftsmanship,
			final int p_crafterLevel, final int p_recipeLevel)
	{
		double baseProgress = 0;
		double levelCorrectionFactor = 0;
		double levelCorrectedProgress = 0;

		if(p_crafterLevel >= 260)
		{
			baseProgress = 1.834712812e-5 * p_craftsmanship * p_craftsmanship + 1.904074773e-1 * p_craftsmanship
					+ 1.544103837;
		}
		else if(p_crafterLevel >= 120)
		{
			baseProgress = 2.09860e-5 * p_craftsmanship * p_craftsmanship + 0.196184 * p_craftsmanship + 2.68452;
		}
		else if(p_crafterLevel < 120)
		{
			baseProgress = 0.214959 * p_craftsmanship + 1.6;
		}

		// Level boost for recipes below crafter level
		if(p_levelDifference > 0)
		{
			levelCorrectionFactor += 0.05 * Math.min(p_levelDifference, 5);
		}
		if(p_levelDifference > 5)
		{
			levelCorrectionFactor += 0.02 * Math.min(p_levelDifference - 5, 10);
		}
		if(p_levelDifference > 15)
		{
			levelCorrectionFactor += 0.01 * Math.min(p_levelDifference - 15, 5);
		}
		if(p_levelDifference > 20)
		{
			levelCorrectionFactor += 0.0006 * (p_levelDifference - 20);
		}

		// Level penalty for recipes above crafter level
		// Seems to be capped at -10
		if(p_levelDifference < 0)
		{
			levelCorrectionFactor += 0.025 * Math.max(p_levelDifference, -10);
		}

		levelCorrectedProgress = (1 + levelCorrectionFactor) * baseProgress;

		return levelCorrectedProgress;
	}

	private static double calculateBaseQualityIncrease(final double p_levelDifference, final int p_control,
			final int p_crafterLevel, final int p_recipeLevel)
	{
		double baseQuality = 0;
		double recipeLevelFactor = 0;
		double levelCorrectionFactor = 0;
		double levelCorrectedQuality = 0;
		if(p_recipeLevel >= 255)
		{
			baseQuality = 3.38411e-05 * p_control * p_control + 0.340191324 * p_control + 33.85116443;

			recipeLevelFactor = -0.0002122 * p_recipeLevel;
		}
		else if(p_recipeLevel >= 115)
		{
			baseQuality = 3.3506479e-5 * p_control * p_control + 0.339276958 * p_control + 32.97846477;

			recipeLevelFactor = 3.4e-4 * (115 - p_recipeLevel);
		}
		else
		{
			baseQuality = 3.46e-5 * p_control * p_control + 0.3514 * p_control + 34.66;
		}

		// Level penalty for recipes above crafter level
		if(p_levelDifference < 0)
		{
			levelCorrectionFactor = 0.05 * Math.max(p_levelDifference, -10);
		}

		levelCorrectedQuality = baseQuality * (1 + levelCorrectionFactor) * (1 + recipeLevelFactor);

		return levelCorrectedQuality;
	}

	private static boolean decideSuccess(final Step p_step, final boolean p_userGuided, boolean p_printStatus)
	{
		if(p_step.getSuccessProbability() == 0.0)
		{
			return false;
		}

		if(p_step.getSuccessProbability() == 1.0)
		{
			if(p_printStatus)
			{
				System.out.println("Guaranteed success.");
			}
			return true;
		}

		if(p_userGuided)
		{

			final Object obj = TextIO.getResponse("Did this action succeed?",
					new Object[] {true, false, "Randomly decide"});
			if(obj instanceof Boolean)
			{
				return (Boolean)obj;
			}

			p_printStatus = true;
		}

		final double successChance = p_step.getSuccessProbability();
		final boolean success = Utilities.flipCoin(p_step.getSuccessProbability());
		if(p_printStatus)
		{
			System.out.println(
					"Probability of success: " + (int)(successChance * 100) + "%.  Success = " + success + ".");
		}
		return success;
	}

	public static void fillNextConditionDistribution(final Condition p_currentCondition, final double[] p_buffer)
	{
		Arrays.fill(p_buffer, 0);

		switch(p_currentCondition)
		{
			case EXCELLENT:
				p_buffer[Condition.POOR.ordinal()] = 1.0;
				break;
			case GOOD:
				p_buffer[Condition.NORMAL.ordinal()] = 1.0;
				break;
			case NORMAL:
				p_buffer[Condition.EXCELLENT.ordinal()] = P_EXCELLENT;
				p_buffer[Condition.GOOD.ordinal()] = P_GOOD;
				p_buffer[Condition.NORMAL.ordinal()] = 1 - P_EXCELLENT - P_GOOD;
				break;
			case POOR:
				p_buffer[Condition.NORMAL.ordinal()] = 1.0;
				break;
			default:
				throw new RuntimeException();
		}
	}

	public static int getMakersMarkStackSize(final SynthData p_synth)
	{
		int makersMarkStacks = (int)Math.ceil(p_synth.getRecipe().getDifficulty() / 100);
		if(makersMarkStacks == 0)
		{
			makersMarkStacks = 1;
		}
		return makersMarkStacks;
	}

	public static StateProbabilityPair[] getPossibleOutcomes(final State p_startState, final Condition p_condition,
			final Action p_action)
	{
		// Clone startState to keep startState immutable
		final State state = p_startState.clone();

		// Condition Evaluation
		final double condQualityIncreaseMultiplier = p_condition.getQualityMultiplier();

		// Calculate Progress, Quality and Durability gains and losses under
		// effect of modifiers
		final Step step = applyModifiers(state, p_condition, p_action);

		final boolean failurePossible = step.getSuccessProbability() < 1;

		final State successState = state;
		State failureState = null;
		StateProbabilityPair[] ret = null;

		if(failurePossible)
		{
			failureState = successState.clone();
			ret = new StateProbabilityPair[2];

			updateState(failureState, p_condition, p_action, 0, 0, step.getDurabilityCost(), step.getCPCost(), false);

			failureState.setAction(p_action);
			failureState.incrementLastStep();
			failureState.incrementStep();
			ret[1] = new StateProbabilityPair(failureState, 1 - step.getSuccessProbability());
		}
		else
		{
			ret = new StateProbabilityPair[1];
		}

		final int progressGain = step.getProgressGain();
		final int qualityGain = (int)(condQualityIncreaseMultiplier * step.getQualityGain());

		updateState(successState, p_condition, p_action, progressGain, qualityGain, step.getDurabilityCost(),
				step.getCPCost(), true);

		successState.setAction(p_action);
		successState.incrementLastStep();
		successState.incrementStep();

		ret[0] = new StateProbabilityPair(successState, step.getSuccessProbability());

		return ret;
	}

	public static Condition getStartCondition()
	{
		return Condition.NORMAL;
	}

	public static double hqPercent(final double p_quality, final double p_maxQuality)
	{
		double hqPercent = 1;
		if(p_quality <= 0)
		{
			hqPercent = 1;
		}
		else if(p_quality >= p_maxQuality)
		{
			hqPercent = 100;
		}
		else
		{
			final double qualityPercent = p_quality / p_maxQuality * 100;
			double upperHQBound = 100.0;
			double lowerHQBound = 1.0;
			hqPercent = 50;
			for(int i = 0; i < MAX_BSEARCH_ITERS; i++)
			{
				final double qualityFromHQPercent = qualityFromHqPercent(hqPercent);
				if(qualityFromHQPercent == qualityPercent || upperHQBound == lowerHQBound)
				{
					return hqPercent / 100;
				}
				else if(qualityFromHQPercent > qualityPercent)
				{
					upperHQBound = hqPercent;
				}
				else if(qualityFromHQPercent < qualityPercent)
				{
					lowerHQBound = hqPercent;
				}

				hqPercent = (upperHQBound + lowerHQBound) / 2;
			}
		}
		return hqPercent / 100;
	}

	public static boolean isEndState(final State p_state)
	{
		return p_state.getDurabilityState() <= 0
				|| p_state.getProgressState() >= p_state.getSynth().getRecipe().getDifficulty();
	}

	public static State monteCarloStep(final State p_startState, final Condition p_condition, final Action p_action,
			final boolean p_userGuided, final boolean p_printStatus)
	{
		// Clone startState to keep startState immutable
		final State state = p_startState.clone();

		// Condition Evaluation
		final double condQualityIncreaseMultiplier = p_condition.getQualityMultiplier();

		// Calculate Progress, Quality and Durability gains and losses under
		// effect of modifiers
		final Step step = applyModifiers(state, p_condition, p_action);

		// Success or Failure
		final boolean success = decideSuccess(step, p_userGuided, p_printStatus);

		// Calculate final gains / losses
		final int progressGain = success ? step.getProgressGain() : 0;
		final int qualityGain = success ? (int)(condQualityIncreaseMultiplier * step.getQualityGain()) : 0;

		updateState(state, p_condition, p_action, progressGain, qualityGain, step.getDurabilityCost(), step.getCPCost(),
				success);

		// Return final state
		state.setAction(p_action);
		state.incrementLastStep();
		state.incrementStep();
		return state;
	}

	public static void openSimulation(final Policy p_policy, final boolean p_userChoosesConditions)
	{
		while(true)
		{
			runSimulation(p_policy, true, p_userChoosesConditions);
			if(TextIO.getResponseIndex("Restart simulator?", new String[] {"YES", "NO"}) != 0)
			{
				break;
			}
		}
	}

	private static double qualityFromHqPercent(final double p_hqPercent)
	{
		final double x = p_hqPercent;
		final double y = -5.6604E-6 * Math.pow(x, 4) + 0.0015369705 * Math.pow(x, 3) - 0.1426469573 * Math.pow(x, 2)
				+ 5.6122722959 * x - 5.5950384565;
		return y;
	}

	public static int ruminationCPGain(final EffectTracker effects)
	{
		final int cpToAdd = (int)(21 * effects.getCountUpValue(AllActions.INNER_QUIET)
				- Math.pow(effects.getCountUpValue(AllActions.INNER_QUIET), 2) + 10) / 2;
		return cpToAdd;
	}

	public static void runSimulation(final Policy p_policy, final boolean p_userChoosesActions,
			final boolean p_userChoosesEverythingElse)
	{
		if(p_userChoosesActions)
		{
			TextIO.printCrossClassActions(p_policy);
		}

		State currentState = p_policy.getSynth().newState();
		Condition condition = getStartCondition();
		while(!isEndState(currentState))
		{
			if(p_userChoosesActions)
			{
				TextIO.printState(currentState, 0, false);
				TextIO.printCondition(condition);
			}

			final Action action = selectNextAction(p_policy, currentState, condition, p_userChoosesActions);
			if(action == null)
			{
				return;
			}

			currentState = monteCarloStep(currentState, condition, action, p_userChoosesEverythingElse,
					p_userChoosesActions && !p_userChoosesEverythingElse);
			condition = updateCondition(condition, p_userChoosesEverythingElse);
			p_policy.notifyActionUse(action);
		}

		if(p_userChoosesActions)
		{
			TextIO.printState(currentState, 0, false);
			TextIO.printResult(currentState);
		}
	}

	private static Action selectNextAction(final Policy p_policy, final State p_currentState,
			final Condition p_condition, final boolean p_userGuided)
	{
		if(p_userGuided)
		{
			final ActionValuePair[] actions = p_policy.getRecommendedActions(p_currentState, p_condition);
			return ((ActionValuePair)TextIO.getResponse("Which action to take?", actions)).getAction();
		}
		else
		{
			return p_policy.getNextAction(p_currentState, p_condition);
		}
	}

	public static Condition updateCondition(final Condition p_condition, final boolean p_userGuided)
	{
		switch(p_condition)
		{
			case EXCELLENT:
				return Condition.POOR;
			case GOOD:
				return Condition.NORMAL;
			case POOR:
				return Condition.NORMAL;
			case NORMAL:
				final boolean showChoice = false;
				if(p_userGuided)
				{
					final Object[] choices = new Object[Condition.values().length];
					choices[0] = Condition.EXCELLENT;
					choices[1] = Condition.GOOD;
					choices[2] = Condition.NORMAL;
					choices[3] = "Randomly decide";
					final Object choice = TextIO.getResponse("What is the next state?", choices);
					if(choice instanceof Condition)
					{
						return (Condition)choice;
					}
				}

				final double rand = m_random.nextDouble();
				Condition ret = null;
				if(rand <= P_EXCELLENT)
				{
					ret = Condition.EXCELLENT;
				}
				else if(rand <= P_GOOD + P_EXCELLENT)
				{
					ret = Condition.GOOD;
				}
				else
				{
					ret = Condition.NORMAL;
				}

				if(showChoice)
				{
					System.out.println("Condition: " + ret);
				}
				return ret;

			default:
				throw new RuntimeException();
		}

	}

	private static void updateEffectCounters(final State p_state, final Action p_action, final Condition p_condition,
			final boolean p_success)
	{
		final EffectTracker effects = p_state.getEffects();
		// STEP_03
		// Countdown / Countup Management
		// ===============================
		// Decrement countdowns
		for(final Action countDownAction: effects.getCountDowns())
		{
			if(countDownAction == AllActions.WHISTLE)
			{
				if(p_condition.checkGoodOrExcellent())
				{
					effects.decrementCountDown(AllActions.WHISTLE);
				}
			}
			else
			{
				effects.decrementCountDown(countDownAction);
			}

			if(effects.getCountDownValue(countDownAction) == 0)
			{
				effects.removeCountDown(countDownAction);
			}
		}

		if(effects.containsCountUp(AllActions.INNER_QUIET))
		{
			// Increment inner quiet countups that have conditional requirements
			if(p_action == AllActions.PATIENT_TOUCH)
			{
				if(p_success)
				{
					effects.incrementCountUp(AllActions.INNER_QUIET);
				}
				else
				{
					effects.cutIQStackInHalf();
				}
			}

			// Increment inner quiet countups that have conditional requirements
			else if(p_action == AllActions.PRECISE_TOUCH)
			{
				if(p_condition.checkGoodOrExcellent())
				{
					if(p_success)
					{
						effects.incrementCountUp(AllActions.INNER_QUIET);
					}
				}
				else
				{
					throw new RuntimeException();
				}
			}
			// Increment all other inner quiet count ups

			if(p_action.getQualityMultiplier() > 0 && p_success)
			{
				effects.incrementCountUp(AllActions.INNER_QUIET);
			}
		}

		// Initialize new effects after countdowns are managed to reset them
		// properly
		if(p_action.getType() == ActionType.COUNT_UP)
		{
			effects.addCountUp(p_action);
		}

		if(p_action.getType() == ActionType.INDEFINITE)
		{
			if(p_action == AllActions.INITIAL_PREPARATIONS)
			{
				if(p_state.getStep() == 1)
				{
					effects.addIndefinite(p_action);
				}
				else
				{
					throw new RuntimeException();
				}
			}
			else
			{
				effects.addIndefinite(p_action);
			}
		}

		if(p_action.getType() == ActionType.COUNT_DOWN)
		{
			if(p_action instanceof NameOfAction)
			{
				if(p_state.getNameOfElementUses() == 0)
				{
					effects.addCountDown(p_action, p_action.numActiveTurns());
					p_state.setNameOfElementUses(1);
				}
				else
				{
					throw new RuntimeException();
				}
			}
			else if(p_action == AllActions.MANIPULATION || p_action == AllActions.MANIPULATION_2)
			{
				if(effects.containsCountDown(AllActions.MANIPULATION))
				{
					effects.removeCountDown(AllActions.MANIPULATION);
				}
				if(effects.containsCountDown(AllActions.MANIPULATION_2))
				{
					effects.removeCountDown(AllActions.MANIPULATION_2);
				}
				effects.addCountDown(p_action, p_action.numActiveTurns());
			}
			else if(p_action == AllActions.MAKERS_MARK)
			{
				if(p_state.getStep() == 1)
				{
					// Maker's Mark has stacks equal to difficulty
					// divided by 100 rounded up http://redd.it/3ckrmk
					final int makersMarkStacks = getMakersMarkStackSize(p_state.getSynth());
					effects.addCountDown(AllActions.MAKERS_MARK, makersMarkStacks);
				}
				else
				{
					throw new RuntimeException();
				}
			}
			else
			{
				effects.addCountDown(p_action, p_action.numActiveTurns());
			}
		}

		// Innovative Touch activates innovation
		if(p_action == AllActions.INNOVATIVE_TOUCH && p_success)
		{
			effects.addCountDown(AllActions.INNOVATION, AllActions.INNOVATION.numActiveTurns());
		}
	}

	private static void updateState(final State p_state, final Condition p_condition, final Action p_action,
			final int p_progressGain, final int p_qualityGain, final int p_durabilityCost, final int p_cpCost,
			final boolean p_success)
	{
		// State tracking
		if(p_success)
		{
			p_state.addProgress(p_progressGain);
			p_state.addQuality(p_qualityGain);
		}
		p_state.subtractDurability(p_durabilityCost);
		p_state.subtractCP(p_cpCost);

		applySpecialActionEffects(p_state, p_condition, p_action, p_success);
		updateEffectCounters(p_state, p_action, p_condition, p_success);

		// Sanity checks for state variables
		if(p_state.getDurabilityState() >= -5
				&& p_state.getProgressState() >= p_state.getSynth().getRecipe().getDifficulty())
		{
			p_state.setDurabilityState(0);
		}

		final int newDurability = Math.min(p_state.getDurabilityState(),
				p_state.getSynth().getRecipe().getDurability());
		final int newCP = Math.min(p_state.getCPState(),
				p_state.getSynth().getCrafter().getActiveClassCP() + p_state.getBonusMaxCP());
		p_state.setDurabilityState(newDurability);
		p_state.setCPState(newCP);
	}
}
