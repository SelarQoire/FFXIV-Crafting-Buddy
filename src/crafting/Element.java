package crafting;

public enum Element
{
	EARTH, FIRE, ICE, LIGHTNING, NONE, WATER, WIND;

	public static Element getElement(final String p_name)
	{
		if(p_name.equalsIgnoreCase("earth"))
		{
			return EARTH;
		}
		else if(p_name.equalsIgnoreCase("fire"))
		{
			return FIRE;
		}
		else if(p_name.equalsIgnoreCase("ice"))
		{
			return ICE;
		}
		else if(p_name.equalsIgnoreCase("lightning"))
		{
			return LIGHTNING;
		}
		else if(p_name.equalsIgnoreCase("water"))
		{
			return WATER;
		}
		else if(p_name.equalsIgnoreCase("wind"))
		{
			return WIND;
		}
		else
		{
			throw new RuntimeException("No element with name " + p_name);
		}
	}

	String getName()
	{
		switch(this)
		{
			case EARTH:
				return "Earth";
			case FIRE:
				return "Fire";
			case ICE:
				return "Ice";
			case LIGHTNING:
				return "Lightning";
			case NONE:
				throw new RuntimeException();
			case WATER:
				return "Water";
			case WIND:
				return "Wind";
			default:
				throw new RuntimeException();
		}
	}
}
