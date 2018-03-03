package synthSim;

import common.Utilities;
import crafting.CrafterData;
import crafting.SynthData;

public class SynthGenerator
{
	private static final double	HALF_START_QUALITY_PERCENT = .2;
	private static final int	MAX_LEVELS_ABOVE		   = 4;

	private static final int	MAX_LEVELS_BELOW		   = 10;
	private static final double	NO_START_QUALITY_PERCENT   = .5;

	public static void main(final String[] p_args)
	{
		final SynthGenerator generator = new SynthGenerator();

		for(int i = 0; i < 10000; i++)
		{
			generator.newRandomSynth();
		}
	}

	private final CrafterSimulator m_crafterSimulator;

	private final RecipeStore	   m_recipes;

	public SynthGenerator()
	{
		m_crafterSimulator = new CrafterSimulator();
		m_recipes = new RecipeStore();
	}

	public SynthData newRandomSynth()
	{
		final CrafterData crafter = m_crafterSimulator.newRandomCrafter();

		RecipeData recipe = null;
		while(true)
		{
			final int index = Utilities.randomInt(m_recipes.numRecipes());
			recipe = m_recipes.getRecipe(index);
			if(recipe.getCClass() == crafter.getActiveClass())
			{
				final int recipeLevel = recipe.getNomLevel();
				final int crafterLevel = crafter.getActiveClassLevel();
				if(recipeLevel - crafterLevel <= MAX_LEVELS_ABOVE && crafterLevel - recipeLevel <= MAX_LEVELS_BELOW)
				{
					break;
				}
			}
		}

		int startQuality;
		final double random = Utilities.randomDouble(0, 1.0);
		if(random <= NO_START_QUALITY_PERCENT)
		{
			startQuality = 0;
		}
		else if(random <= NO_START_QUALITY_PERCENT + HALF_START_QUALITY_PERCENT)
		{
			startQuality = recipe.getMaxQuality() / 2;
		}
		else
		{
			startQuality = (int)(recipe.getMaxQuality() * Utilities.randomDouble(0, .5));
		}

		final double matsValueFraction = Utilities.randomDouble(0, 1.0);
		final double nqValueFraction = Utilities.randomDouble(0, 1.0);

		return new SynthData(crafter, recipe, startQuality, nqValueFraction, matsValueFraction);
	}
}
