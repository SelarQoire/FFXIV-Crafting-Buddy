package crafting;

class Step
{
	private int	   m_cpCost;
	private int	   m_durabilityCost;
	private int	   m_progressGain;
	private int	   m_qualityGain;
	private double m_successProbability;

	int getCPCost()
	{
		return m_cpCost;
	}

	int getDurabilityCost()
	{
		return m_durabilityCost;
	}

	int getProgressGain()
	{
		return m_progressGain;
	}

	int getQualityGain()
	{
		return m_qualityGain;
	}

	double getSuccessProbability()
	{
		return m_successProbability;
	}

	void setCPCost(final int p_cpCost)
	{
		m_cpCost = p_cpCost;
	}

	void setDurabilityCost(final int p_durabilityCost)
	{
		m_durabilityCost = p_durabilityCost;
	}

	void setProgressGain(final int p_bProgressGain)
	{
		m_progressGain = p_bProgressGain;
	}

	void setQualityGain(final int p_qualityGain)
	{
		m_qualityGain = p_qualityGain;
	}

	void setSuccessProbability(final double p_successProbability)
	{
		m_successProbability = p_successProbability;
	}
}
