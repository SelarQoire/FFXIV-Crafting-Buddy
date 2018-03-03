package synthSim;

public class EquipmentData
{
	private final int	 m_control;
	private final int	 m_cp;
	private final int	 m_craftsmanship;
	private final int	 m_equipLevel;
	private final String m_name;

	public EquipmentData(final String p_name, final int p_equipLevel, final int p_cp, final int p_control,
			final int p_craftsmanship)
	{
		m_name = p_name;
		m_equipLevel = p_equipLevel;
		m_control = p_control;
		m_craftsmanship = p_craftsmanship;
		m_cp = p_cp;
	}

	public int getControl()
	{
		return m_control;
	}

	public int getCp()
	{
		return m_cp;
	}

	public int getCraftsmanship()
	{
		return m_craftsmanship;
	}

	public int getEquipLevel()
	{
		return m_equipLevel;
	}

	public String getName()
	{
		return m_name;
	}
}
