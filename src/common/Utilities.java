package common;

import java.util.Random;

public class Utilities
{
	private static Random m_random = new Random();

	public static boolean flipCoin(final double p_prob)
	{
		return m_random.nextDouble() <= p_prob;
	}

	public static double logit(final double p_x)
	{
		final double exp = Math.exp(p_x);

		if(exp == Double.POSITIVE_INFINITY)
		{
			return 1;
		}

		final double ret = exp / (1 + exp);
		if(!Double.isFinite(ret))
		{
			throw new RuntimeException();
		}

		return ret;
	}

	public static double randomDouble(final double p_minPercent, final double p_maxPercent)
	{
		final double random = m_random.nextDouble();
		return p_minPercent + (p_maxPercent - p_minPercent) * random;
	}

	public static <T> T randomFromArray(final T[] p_array)
	{
		final int index = randomInt(p_array.length);
		return p_array[index];
	}

	public static int randomInt(final int p_cap)
	{
		return m_random.nextInt(p_cap);
	}

	public static double randomNoise(final double p_scale)
	{
		return p_scale * m_random.nextGaussian();
	}
}
