package nn;

import java.util.ArrayList;
import java.util.List;

/**
 * Taken from https://paginas.fe.up.pt/~ee02162/dissertacao/RPROP%20paper.pdf
 *
 * @author Steve Riley
 *
 */
public class AdamObject
{
	private static final double		BETA_1	= .9;
	private static final double		BETA_2	= .999;
	private static final double		EPSILON	= 1e-8;
	private static final double		NU		= .001;

	private final ArrayList<Double>	m_gradientList;
	private double					m_mt;

	private double					m_value;
	private double					m_vt;

	public AdamObject(final double p_initialValue)
	{
		m_gradientList = new ArrayList<>();
		setValue(p_initialValue);
	}

	public void clearCollectedGradients()
	{
		m_gradientList.clear();
	}

	public List<Double> getCollectedGradients()
	{
		return m_gradientList;
	}

	public double getValue()
	{
		return m_value;
	}

	public void setNewGradient(final double p_gradient, final GradientSetType p_type)
	{
		if(p_type == GradientSetType.UPDATE)
		{
			m_mt = BETA_1 * m_mt + (1 - BETA_1) * p_gradient;
			m_vt = BETA_2 * m_vt + (1 - BETA_2) * Math.pow(p_gradient, 2);

			final double mtHat = m_mt / (1 - BETA_1);
			final double vtHat = m_vt / (1 - BETA_2);

			final double delta = NU * mtHat / (Math.sqrt(vtHat) + EPSILON);
			m_value -= delta;
		}
		else if(p_type == GradientSetType.COLLECT)
		{
			m_gradientList.add(p_gradient);
		}
		else if(p_type == GradientSetType.IGNORE)
		{
			// do nothing
		}
	}

	public void setValue(final double p_value)
	{
		m_mt = 0;
		m_vt = 0;
		m_value = p_value;
	}
}
