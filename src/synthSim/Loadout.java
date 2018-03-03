package synthSim;

public class Loadout
{
	private EquipmentData m_body;
	private EquipmentData m_ears;
	private EquipmentData m_feet;
	private EquipmentData m_finger1;
	private EquipmentData m_finger2;
	private EquipmentData m_hands;
	private EquipmentData m_head;
	private EquipmentData m_legs;
	private EquipmentData m_mainHand;
	private EquipmentData m_neck;
	private EquipmentData m_offHand;
	private EquipmentData m_waist;
	private EquipmentData m_wrists;

	private EquipmentData[] getAllEquipment()
	{
		return new EquipmentData[] {m_body, m_ears, m_feet, m_finger1, m_finger2, m_hands, m_head, m_legs, m_mainHand,
				m_neck, m_offHand, m_waist, m_wrists};
	}

	public int getTotalControl()
	{
		int control = 0;
		for(final EquipmentData eq: getAllEquipment())
		{
			control += eq.getControl();
		}

		return control;
	}

	public int getTotalCP()
	{
		int cp = 0;
		for(final EquipmentData eq: getAllEquipment())
		{
			cp += eq.getCp();
		}

		return cp;
	}

	public int getTotalCraftsmanship()
	{
		int craftsmanship = 0;
		for(final EquipmentData eq: getAllEquipment())
		{
			craftsmanship += eq.getCraftsmanship();
		}

		return craftsmanship;
	}

	public void setBody(final EquipmentData p_body)
	{
		m_body = p_body;
	}

	public void setEars(final EquipmentData p_ears)
	{
		m_ears = p_ears;
	}

	public void setFeet(final EquipmentData p_feet)
	{
		m_feet = p_feet;
	}

	public void setFinger1(final EquipmentData p_finger1)
	{
		m_finger1 = p_finger1;
	}

	public void setFinger2(final EquipmentData p_finger2)
	{
		m_finger2 = p_finger2;
	}

	public void setHands(final EquipmentData p_hands)
	{
		m_hands = p_hands;
	}

	public void setHead(final EquipmentData p_head)
	{
		m_head = p_head;
	}

	public void setLegs(final EquipmentData p_legs)
	{
		m_legs = p_legs;
	}

	public void setMainHand(final EquipmentData p_mainHand)
	{
		m_mainHand = p_mainHand;
	}

	public void setNeck(final EquipmentData p_neck)
	{
		m_neck = p_neck;
	}

	public void setOffHand(final EquipmentData p_offHand)
	{
		m_offHand = p_offHand;
	}

	public void setWaist(final EquipmentData p_waist)
	{
		m_waist = p_waist;
	}

	public void setWrists(final EquipmentData p_wrists)
	{
		m_wrists = p_wrists;
	}
}
