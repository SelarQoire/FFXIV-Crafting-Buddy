package crafting;

import java.util.HashMap;

class LevelTable
{
	private static final HashMap<Integer, Integer> m_crafterLevelsMap;
	private static final HashMap<Double, Double>   m_ingenuityMap;
	private static final HashMap<Double, Double>   m_ingenuityTwoMap;
	private static final HashMap<Integer, Integer> m_nymeiasWheelMap;

	static
	{
		m_crafterLevelsMap = new HashMap<>();

		for(int i = 0; i <= 50; i++)
		{
			addMappedCrafterLevel(i, i);
		}

		addMappedCrafterLevel(51, 120); // 120
		addMappedCrafterLevel(52, 125); // 125
		addMappedCrafterLevel(53, 130); // 130
		addMappedCrafterLevel(54, 133); // 133
		addMappedCrafterLevel(55, 136); // 136
		addMappedCrafterLevel(56, 139); // 139
		addMappedCrafterLevel(57, 142); // 142
		addMappedCrafterLevel(58, 145); // 145
		addMappedCrafterLevel(59, 148); // 148
		addMappedCrafterLevel(60, 150); // 150
		addMappedCrafterLevel(61, 260);
		addMappedCrafterLevel(62, 265);
		addMappedCrafterLevel(63, 270);
		addMappedCrafterLevel(64, 273);
		addMappedCrafterLevel(65, 276);
		addMappedCrafterLevel(66, 279);
		addMappedCrafterLevel(67, 282);
		addMappedCrafterLevel(68, 285);
		addMappedCrafterLevel(69, 288);
		addMappedCrafterLevel(70, 290);

		m_ingenuityMap = new HashMap<>();

		addIngenuityLevel(40, 36);
		addIngenuityLevel(41, 36);
		addIngenuityLevel(42, 37);
		addIngenuityLevel(43, 38);
		addIngenuityLevel(44, 39);
		addIngenuityLevel(45, 40);
		addIngenuityLevel(46, 41);
		addIngenuityLevel(47, 42);
		addIngenuityLevel(48, 43);
		addIngenuityLevel(49, 44);
		addIngenuityLevel(50, 45);
		addIngenuityLevel(55, 50); // 50_1star *** unverified
		addIngenuityLevel(70, 50); // 50_2star *** unverified
		addIngenuityLevel(90, 58); // 50_3star *** unverified
		addIngenuityLevel(110, 58); // 50_4star *** unverified
		addIngenuityLevel(115, 100); // 51 @ 169/339 difficulty
		addIngenuityLevel(120, 100); // 51 @ 210/410 difficulty
		addIngenuityLevel(125, 100); // 52
		addIngenuityLevel(130, 110); // 53
		addIngenuityLevel(133, 110); // 54
		addIngenuityLevel(136, 110); // 55
		addIngenuityLevel(139, 124); // 56
		addIngenuityLevel(142, 129.5); // 57
		addIngenuityLevel(145, 134.5); // 58
		addIngenuityLevel(148, 139); // 59
		addIngenuityLevel(150, 140); // 60
		addIngenuityLevel(160, 151); // 60_1star
		addIngenuityLevel(170, 152.085); // 60_2star
		addIngenuityLevel(180, 153.185); // 60_3star
		addIngenuityLevel(190, 154.275); // 60_4star
		addIngenuityLevel(255, 240); // 61
		addIngenuityLevel(260, 240); // 61
		addIngenuityLevel(265, 240); // 62
		addIngenuityLevel(270, 250); // 63
		addIngenuityLevel(273, 250); // 64
		addIngenuityLevel(276, 250); // 65
		addIngenuityLevel(279, 264); // 66
		addIngenuityLevel(282, 269.5); // 67
		addIngenuityLevel(285, 274.5); // 68
		addIngenuityLevel(288, 279); // 69
		addIngenuityLevel(290, 280); // 70
		addIngenuityLevel(300, 291.185); // 70_1star
		addIngenuityLevel(320, 292.75);

		m_ingenuityTwoMap = new HashMap<>();

		addIngenuityTwoLevel(40, 33);
		addIngenuityTwoLevel(41, 34);
		addIngenuityTwoLevel(42, 35);
		addIngenuityTwoLevel(43, 36);
		addIngenuityTwoLevel(44, 37);
		addIngenuityTwoLevel(45, 38);
		addIngenuityTwoLevel(46, 39);
		addIngenuityTwoLevel(47, 40);
		addIngenuityTwoLevel(48, 40);
		addIngenuityTwoLevel(49, 41);
		addIngenuityTwoLevel(50, 42);
		addIngenuityTwoLevel(55, 47); // 50_1star *** unverified
		addIngenuityTwoLevel(70, 47); // 50_2star *** unverified
		addIngenuityTwoLevel(90, 56); // 50_3star *** unverified
		addIngenuityTwoLevel(110, 56); // 50_4star *** unverified
		addIngenuityTwoLevel(115, 100); // 51 @ 169/339 difficulty
		addIngenuityTwoLevel(120, 100); // 51 @ 210/410 difficulty
		addIngenuityTwoLevel(125, 100); // 52
		addIngenuityTwoLevel(130, 110); // 53
		addIngenuityTwoLevel(133, 110); // 54
		addIngenuityTwoLevel(136, 110); // 55
		addIngenuityTwoLevel(139, 124); // 56
		addIngenuityTwoLevel(142, 129.5); // 57
		addIngenuityTwoLevel(145, 133); // 58
		addIngenuityTwoLevel(148, 136); // 59
		addIngenuityTwoLevel(150, 139); // 60
		addIngenuityTwoLevel(160, 150); // 60_1star
		addIngenuityTwoLevel(170, 151.115); // 60_2star
		addIngenuityTwoLevel(180, 152.215); // 60_3star
		addIngenuityTwoLevel(190, 153.305); // 60_4star
		addIngenuityTwoLevel(255, 240); // 61 @ 210/410 difficulty
		addIngenuityTwoLevel(260, 240); // 61 @ 210/410 difficulty
		addIngenuityTwoLevel(265, 240); // 62
		addIngenuityTwoLevel(270, 250); // 63
		addIngenuityTwoLevel(273, 250); // 64
		addIngenuityTwoLevel(276, 250); // 65
		addIngenuityTwoLevel(279, 264); // 66
		addIngenuityTwoLevel(282, 269.5); // 67
		addIngenuityTwoLevel(285, 273); // 68
		addIngenuityTwoLevel(288, 276); // 69
		addIngenuityTwoLevel(290, 279); // 70
		addIngenuityTwoLevel(300, 290); // 70_1star
		addIngenuityTwoLevel(320, 292); // 70_2star

		m_nymeiasWheelMap = new HashMap<>();

		addNymeiasWheelItem(1, 30);
		addNymeiasWheelItem(2, 30);
		addNymeiasWheelItem(3, 30);
		addNymeiasWheelItem(4, 20);
		addNymeiasWheelItem(5, 20);
		addNymeiasWheelItem(6, 20);
		addNymeiasWheelItem(7, 10);
		addNymeiasWheelItem(8, 10);
		addNymeiasWheelItem(9, 10);
		addNymeiasWheelItem(10, 10);
		addNymeiasWheelItem(11, 10);
	}

	private static void addIngenuityLevel(final double p_nominalLevel, final double p_mappedLevel)
	{
		m_ingenuityMap.put(p_nominalLevel, p_mappedLevel);
	}

	private static void addIngenuityTwoLevel(final double p_nominalLevel, final double p_mappedLevel)
	{
		m_ingenuityTwoMap.put(p_nominalLevel, p_mappedLevel);
	}

	private static void addMappedCrafterLevel(final int p_nominalLevel, final int p_effectiveLevel)
	{
		m_crafterLevelsMap.put(p_nominalLevel, p_effectiveLevel);
	}

	private static void addNymeiasWheelItem(final int p_whistleStack, final int p_durabilityRestore)
	{
		m_nymeiasWheelMap.put(p_whistleStack, p_durabilityRestore);
	}

	static boolean containsIngenuityLevel(final double p_nominalLevel)
	{
		return m_ingenuityMap.containsKey(p_nominalLevel);
	}

	static boolean containsIngenuityTwoLevel(final double p_nominalLevel)
	{
		return m_ingenuityTwoMap.containsKey(p_nominalLevel);
	}

	static int getEffectiveCrafterLevel(final int p_nominalLevel)
	{
		return m_crafterLevelsMap.get(p_nominalLevel);
	}

	static double getIngenuityLevel(final double p_nominalRecipeLevel)
	{
		return m_ingenuityMap.get(p_nominalRecipeLevel);
	}

	static double getIngenuityTwoLevel(final double p_nominalRecipeLevel)
	{
		return m_ingenuityTwoMap.get(p_nominalRecipeLevel);
	}

	static int getNymeiasWheelDurabilityRestore(final int p_whistleStack)
	{
		return m_nymeiasWheelMap.get(p_whistleStack);
	}
}
