package crafting;

class BrandOfAction extends Action
{
	static BrandOfAction getBrandOfAction(final Element p_aspect)
	{
		switch(p_aspect)
		{
			case EARTH:
				return AllActions.BRAND_OF_EARTH;
			case FIRE:
				return AllActions.BRAND_OF_FIRE;
			case ICE:
				return AllActions.BRAND_OF_ICE;
			case LIGHTNING:
				return AllActions.BRAND_OF_LIGHTNING;
			case WATER:
				return AllActions.BRAND_OF_WATER;
			case WIND:
				return AllActions.BRAND_OF_WIND;
			default:
				throw new RuntimeException("Bad state");
		}
	}

	private final Element m_aspect;

	BrandOfAction(final Element p_aspect, final CrafterClass p_affinity)
	{
		super("Brand Of " + p_aspect.getName(), 37, p_affinity, 6, .9, 1.0, 0.0, 10, ActionType.IMMEDIATE, false);
		m_aspect = p_aspect;
	}

	Element getAspect()
	{
		return m_aspect;
	}

	NameOfAction getCorrespondingNameOfAction()
	{
		return NameOfAction.getNameOfAction(m_aspect);
	}
}
