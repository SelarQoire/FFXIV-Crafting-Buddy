package crafting;

public enum CrafterClass
{
	ALC, ALL, ARM, BSM, CRP, CUL, GSM, LTW, WVR;

	public static CrafterClass getClass(final String p_name)
	{
		if(p_name.equalsIgnoreCase("alchemist"))
		{
			return ALC;
		}
		else if(p_name.equalsIgnoreCase("armorer"))
		{
			return ARM;
		}
		else if(p_name.equalsIgnoreCase("blacksmith"))
		{
			return BSM;
		}
		else if(p_name.equalsIgnoreCase("carpenter"))
		{
			return CRP;
		}
		else if(p_name.equalsIgnoreCase("culinarian"))
		{
			return CUL;
		}
		else if(p_name.equalsIgnoreCase("goldsmith"))
		{
			return GSM;
		}
		else if(p_name.equalsIgnoreCase("leatherworker"))
		{
			return LTW;
		}
		else if(p_name.equalsIgnoreCase("weaver"))
		{
			return WVR;
		}
		else
		{
			throw new RuntimeException();
		}
	}

	public String getName()
	{
		switch(this)
		{
			case ALC:
				return "Alchemist";
			case ALL:
				throw new RuntimeException();
			case ARM:
				return "Armorer";
			case BSM:
				return "Blacksmith";
			case CRP:
				return "Carpenter";
			case CUL:
				return "Culinarian";
			case GSM:
				return "Goldsmith";
			case LTW:
				return "Leatherworker";
			case WVR:
				return "Weaver";
			default:
				throw new RuntimeException();

		}
	}
}
