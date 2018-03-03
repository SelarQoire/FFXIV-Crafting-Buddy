package crafting;

import java.util.HashSet;
import java.util.Set;

import synthSim.RecipeData;

public class SynthData
{
	private final CrafterData m_crafter;
	private final double	  m_matsValueFraction;
	private final double	  m_nqValueFraction;
	private final RecipeData  m_recipe;
	private final int		  m_startQuality;
	private Action[]		  m_worthwhileActions;

	public SynthData(final CrafterData p_crafter, final RecipeData p_recipe, final int p_startQuality,
			final double p_nqValueFraction, final double p_matsValueFraction)
	{
		m_crafter = p_crafter;
		m_recipe = p_recipe;
		m_startQuality = p_startQuality;
		m_nqValueFraction = p_nqValueFraction;
		m_matsValueFraction = p_matsValueFraction;

		loadWorthwhileActions();
	}

	private boolean actionIsDominated(final Action p_action)
	{
		if(p_action == AllActions.CAREFUL_SYNTHESIS)
		{
			return m_crafter.actionIsUnlocked(AllActions.CAREFUL_SYNTHESIS_2);
		}

		if(p_action == AllActions.STANDARD_SYNTHESIS)
		{
			return m_crafter.actionIsUnlocked(AllActions.CAREFUL_SYNTHESIS_3);
		}

		return false;
	}

	public CrafterData getCrafter()
	{
		return m_crafter;
	}

	public double getMatsValueFraction()
	{
		return m_matsValueFraction;
	}

	public double getNqValueFraction()
	{
		return m_nqValueFraction;
	}

	public RecipeData getRecipe()
	{
		return m_recipe;
	}

	public Action[] getWorthwhileActions()
	{
		return m_worthwhileActions;
	}

	private void loadWorthwhileActions()
	{
		final Set<Action> worthwhileActions = new HashSet<>();

		boolean nameAdded = false;

		// first, a name and/or brand is useful if it is the same aspect of the recipe
		if(m_recipe.hasAspect())
		{
			final Element aspect = m_recipe.getAspect();
			final BrandOfAction associatedBrand = BrandOfAction.getBrandOfAction(aspect);
			final NameOfAction associatedName = NameOfAction.getNameOfAction(aspect);

			if(m_crafter.actionIsUnlocked(associatedName))
			{
				nameAdded = true;
				worthwhileActions.add(associatedName);
			}

			if(m_crafter.actionIsUnlocked(associatedBrand))
			{
				worthwhileActions.add(associatedBrand);
			}
		}

		// also, a name/brand combo is useful if it does not cost a cross-class slot
		for(final Element element: Element.values())
		{
			if(element == Element.NONE)
			{
				continue;
			}

			final BrandOfAction associatedBrand = BrandOfAction.getBrandOfAction(element);
			final NameOfAction associatedName = NameOfAction.getNameOfAction(element);

			if(associatedName.getAffinity() == m_crafter.getActiveClass() && m_crafter.actionIsUnlocked(associatedName))
			{
				nameAdded = true;
				worthwhileActions.add(associatedName);
				worthwhileActions.add(associatedBrand);
			}
		}

		if(!nameAdded)
		{
			// if no NameOfActions have been added yet, then one cross-class name/brand
			// combo may be useful
			for(final Element element: Element.values())
			{
				if(element == Element.NONE)
				{
					continue;
				}

				final NameOfAction associatedName = NameOfAction.getNameOfAction(element);
				final BrandOfAction associatedBrand = BrandOfAction.getBrandOfAction(element);
				if(m_crafter.actionIsUnlocked(associatedName))
				{
					worthwhileActions.add(associatedName);
					worthwhileActions.add(associatedBrand);
					break;
				}
			}
		}

		for(final Action action: AllActions.Z_ACTIONS_LIST)
		{
			if(!(action instanceof NameOfAction) && !(action instanceof BrandOfAction))
			{
				if(m_crafter.actionIsUnlocked(action) && !actionIsDominated(action))
				{
					worthwhileActions.add(action);
				}
			}
		}

		m_worthwhileActions = worthwhileActions.toArray(new Action[] {});
	}

	public State newState()
	{
		final int step = 1;
		final int lastStep = 0;
		final int durabilityState = m_recipe.getDurability();
		final int cpState = m_crafter.getActiveClassCP();
		final int bonusMaxCp = 0;
		final int qualityState = m_startQuality;
		final int progressState = 0;
		final int nameOfElementUses = 0;
		final EffectTracker effects = new EffectTracker();

		return new State(this, step, lastStep, AllActions.NONE, durabilityState, cpState, bonusMaxCp, qualityState,
				progressState, nameOfElementUses, effects);
	}
}
