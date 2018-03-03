package common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import crafting.Action;
import crafting.AllActions;
import crafting.Condition;
import crafting.CrafterData;
import crafting.EffectTracker;
import crafting.NameOfAction;
import crafting.Simulator;
import crafting.State;
import synthSim.RecipeData;

public class TextIO
{
	private static final BufferedReader m_in;
	static
	{
		m_in = new BufferedReader(new InputStreamReader(System.in));
	}

	public static Object getResponse(final String p_prompt, final Object[] m_responses)
	{
		return m_responses[getResponseIndex(p_prompt, m_responses)];
	}

	public static int getResponseIndex(final String p_prompt, final Object[] m_responses)
	{
		while(true)
		{
			System.out.println(p_prompt);
			for(int i = 0; i < m_responses.length; i++)
			{
				System.out.println(i + 1 + ": " + m_responses[i]);
			}

			try
			{
				final String input = m_in.readLine();
				final int value = Integer.parseInt(input.trim());
				final int index = value - 1;
				if(index < 0 || index >= m_responses.length)
				{
					System.out.println("Please enter a number between 1 and " + m_responses.length + 1);
				}
				else
				{
					return index;
				}
			}
			catch(final IOException e)
			{
				System.out.println("Error, please try again.");
				System.out.println();
			}
			catch(final NumberFormatException e)
			{
				System.out.println("Please enter a number between 1 and " + m_responses.length + 1);
			}
		}
	}

	public static boolean keyHasBeenPressed()
	{
		try
		{
			if(System.in.available() > 0)
			{
				m_in.readLine();
				return true;
			}
		}
		catch(final IOException e)
		{
			e.printStackTrace();
		}

		return false;
	}

	public static void printCondition(final Condition p_condition)
	{
		System.out.println("Condition: " + p_condition);
	}

	public static void printCrossClassActions(final Policy p_policy)
	{
		final Action[] ccActions = p_policy.getCrossClassActions();
		if(ccActions.length > 0)
		{
			System.out.println("Cross Class Actions List");
			for(final Action action: ccActions)
			{
				System.out.println(action);
			}
		}
	}

	public static void printResult(final State p_endState)
	{
		final RecipeData recipe = p_endState.getSynth().getRecipe();
		final double hqPercent = Simulator.hqPercent(p_endState.getQualityState(), recipe.getMaxQuality());

		if(p_endState.getProgressState() >= recipe.getDifficulty())
		{
			System.out.println("Success.  HQ @ " + hqPercent * 100 + "%");
		}
		else if(p_endState.getEffects().containsIndefinite(AllActions.RECLAIM))
		{
			System.out.println("Failure.  Reclaim on, 90% chance of regaining materials.");
		}
		else
		{
			System.out.println("Failure.");
		}
	}

	public static void printState(final State p_state, final double p_value, final boolean p_printValue)
	{
		final RecipeData recipe = p_state.getSynth().getRecipe();
		final CrafterData crafter = p_state.getSynth().getCrafter();
		System.out.println("Step " + p_state.getStep());
		System.out.println("CP: " + p_state.getCPState() + "/" + crafter.getActiveClassCP());
		System.out.println("Progress: " + p_state.getProgressState() + "/" + recipe.getDifficulty());
		System.out.println("Quality: " + p_state.getQualityState() + "/" + recipe.getMaxQuality());
		System.out.println(
				"HQ: " + (int)(100 * Simulator.hqPercent(p_state.getQualityState(), recipe.getMaxQuality())) + "%");
		System.out.println("Durability: " + p_state.getDurabilityState() + "/" + recipe.getDurability());

		boolean nameOfActionInUse = false;
		final EffectTracker effects = p_state.getEffects();
		if(effects.hasActiveEffects() || p_state.getNameOfElementUses() > 0)
		{
			System.out.println(" --- ");
			for(final Action indefinite: effects.getIndefinites())
			{
				System.out.println(indefinite);
			}

			for(final Action countUp: effects.getCountUps())
			{
				int count = effects.getCountUpValue(countUp);
				if(countUp == AllActions.INNER_QUIET)
				{
					count++;
				}
				System.out.println(countUp + ": " + count);
			}

			for(final Action countDown: effects.getCountDowns())
			{
				if(countDown instanceof NameOfAction)
				{
					nameOfActionInUse = true;
				}
				final int count = effects.getCountDownValue(countDown);
				System.out.println(countDown + ": " + count);
			}
		}

		if(p_state.getNameOfElementUses() > 0 && !nameOfActionInUse)
		{
			System.out.println("Nameless");
		}

		if(p_printValue)
		{
			System.out.println("Predicted value: " + p_value);
		}
		System.out.println();
	}
}
