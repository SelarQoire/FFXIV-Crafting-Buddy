package synthSim;

import common.Utilities;
import crafting.CrafterClass;
import crafting.CrafterData;

public class CrafterSimulator
{
	private static final int	 MAX_LEVEL			  = 70;
	private static final double	 MAX_STAT_PERCENT	  = 1.0;
	private static final int	 MIN_SPECIALIST_LEVEL = 55;
	private static final double	 MIN_STAT_PERCENT	  = .4;
	
	private static final double	 SPECIALIST_CHANCE	  = .5;
	private final EquipmentTable m_table;

	public CrafterSimulator()
	{
		m_table = new EquipmentTable();
	}

	private Loadout buildLoadout(final int activeLevel)
	{
		final Loadout loadout = new Loadout();

		loadout.setBody(m_table.getBestBody(activeLevel));
		loadout.setEars(m_table.getBestEars(activeLevel));
		loadout.setFeet(m_table.getBestFeet(activeLevel));
		loadout.setFinger1(m_table.getBestFinger(activeLevel));
		loadout.setFinger2(m_table.getBestFinger(activeLevel));
		loadout.setHands(m_table.getBestHands(activeLevel));
		loadout.setHead(m_table.getBestHead(activeLevel));
		loadout.setLegs(m_table.getBestLegs(activeLevel));
		loadout.setMainHand(m_table.getBestMainHand(activeLevel));
		loadout.setNeck(m_table.getBestNeck(activeLevel));
		loadout.setOffHand(m_table.getBestOffHand(activeLevel));
		loadout.setWaist(m_table.getBestWaist(activeLevel));
		loadout.setWrists(m_table.getBestWrists(activeLevel));

		return loadout;
	}

	public CrafterData newRandomCrafter()
	{
		final CrafterData ret = new CrafterData();

		CrafterClass activeClass = null;
		do
		{
			activeClass = Utilities.randomFromArray(CrafterClass.values());
		}
		while(activeClass == CrafterClass.ALL);

		ret.setActiveClass(activeClass);
		final int activeLevel = Utilities.randomInt(MAX_LEVEL) + 1;// don't allow level 0
		ret.setLevel(activeClass, activeLevel);

		for(final CrafterClass cClass: CrafterClass.values())
		{
			if(cClass != activeClass && cClass != CrafterClass.ALL)
			{
				final int level = Utilities.randomInt(MAX_LEVEL + 1);
				ret.setLevel(cClass, level);
			}
		}

		final Loadout loadout = buildLoadout(activeLevel);

		final int baseControl = 350 + 5 * activeLevel;
		final int baseCP = 230 + activeLevel;
		final int baseCraftsmanship = 500 + 5 * activeLevel;

		final int maxControl = loadout.getTotalControl() + baseControl;
		final int maxCP = loadout.getTotalCP() + baseCP;
		final int maxCraftsmanship = loadout.getTotalCraftsmanship() + baseCraftsmanship;

		final int actualControl = (int)(Utilities.randomDouble(MIN_STAT_PERCENT, MAX_STAT_PERCENT) * maxControl);
		final int actualCP = (int)(Utilities.randomDouble(MIN_STAT_PERCENT, MAX_STAT_PERCENT) * maxCP);
		final int actualCraftsmanship = (int)(Utilities.randomDouble(MIN_STAT_PERCENT, MAX_STAT_PERCENT)
				* maxCraftsmanship);

		ret.setActiveClassControl(actualControl);
		ret.setActiveClassCP(actualCP);
		ret.setActiveClassCraftsmanship(actualCraftsmanship);

		if(activeLevel >= MIN_SPECIALIST_LEVEL && Utilities.flipCoin(SPECIALIST_CHANCE))
		{
			ret.setActiveClassIsSpecialist(true);
		}
		else
		{
			ret.setActiveClassIsSpecialist(false);
		}

		return ret;
	}
}
