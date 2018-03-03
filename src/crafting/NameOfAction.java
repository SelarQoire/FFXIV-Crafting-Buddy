package crafting;

public class NameOfAction extends Action
{
	static NameOfAction getNameOfAction(final Element p_aspect)
	{
		switch(p_aspect)
		{
			case EARTH:
				return AllActions.NAME_OF_EARTH;
			case FIRE:
				return AllActions.NAME_OF_FIRE;
			case ICE:
				return AllActions.NAME_OF_ICE;
			case LIGHTNING:
				return AllActions.NAME_OF_LIGHTNING;
			case WATER:
				return AllActions.NAME_OF_WATER;
			case WIND:
				return AllActions.NAME_OF_WIND;
			default:
				throw new RuntimeException("Bad state");
		}
	}

	NameOfAction(final Element p_aspect, final CrafterClass p_class)
	{
		super("Name Of " + p_aspect.getName(), 54, p_class, 15, 1.0, 0, 0.0, 0, ActionType.COUNT_DOWN, false);
	}
}
