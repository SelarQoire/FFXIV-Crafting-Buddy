package common;

import java.util.Collection;

public class SummaryStats
{
	private int	   m_count = 0;
	private double m_m2	   = 0;
	private double m_mean  = 0;

	public void add(final double p_double)
	{
		m_count++;
		final double delta = p_double - m_mean;
		m_mean = m_mean + delta / m_count;
		final double delta2 = p_double - m_mean;
		m_m2 = m_m2 + delta * delta2;
	}

	public void addAll(final Collection<Double> p_doubles)
	{
		for(final Double d: p_doubles)
		{
			add(d);
		}
	}

	public double getMean()
	{
		if(m_count == 0)
		{
			return Double.NaN;
		}
		return m_mean;
	}

	public double getStdDev()
	{
		return Math.sqrt(getVariance());
	}

	public double getStdErr()
	{
		return Math.sqrt(getVariance() / m_count);
	}

	public double getT()
	{
		return getMean() / getStdErr();
	}

	public double getVariance()
	{
		if(m_count < 2)
		{
			return Double.NaN;
		}
		return m_m2 / (m_count - 1);
	}
}
