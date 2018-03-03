package common;

import crafting.AllActions;
import crafting.Simulator;
import crafting.State;

public class EndStateEvaluator
{
	private static final double	FULL_FAIL_RECOVERY_RATE	   = .5;
	private static final double	RECLAIM_FAIL_RECOVERY_RATE = .9;

	private static double endStateValue(final double p_hqProb, final double p_nqProb, final double p_reclaimFailProb,
			final double p_fullFailProb, final double p_nqValueFraction, final double p_matsValueFraction)
	{
		return p_hqProb * 1.0 + p_nqProb * p_nqValueFraction
				+ p_reclaimFailProb * p_matsValueFraction * RECLAIM_FAIL_RECOVERY_RATE
				+ p_fullFailProb * p_matsValueFraction * FULL_FAIL_RECOVERY_RATE;
	}

	public static double endStateValue(final State p_endState)
	{
		double hq = 0;
		double nq = 0;
		double reclaimFail = 0;
		double fullFail = 0;

		if(p_endState.getProgressState() >= p_endState.getSynth().getRecipe().getDifficulty())
		{
			hq = Simulator.hqPercent(p_endState.getQualityState(), p_endState.getSynth().getRecipe().getMaxQuality());
			nq = 1 - hq;
		}
		else
		{
			if(p_endState.getEffects().containsIndefinite(AllActions.RECLAIM))
			{
				reclaimFail = 1.0;
			}
			else
			{
				fullFail = 1.0;
			}
		}

		return endStateValue(hq, nq, reclaimFail, fullFail, p_endState.getSynth().getNqValueFraction(),
				p_endState.getSynth().getMatsValueFraction());
	}
}