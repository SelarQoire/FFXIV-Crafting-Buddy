package synthSim;

import java.util.HashMap;

public class EquipmentTable
{
	public static EquipmentData EMPTY = new EquipmentData("Empty", 0, 0, 0, 0);

	private static void addToMap(final String p_name, final int p_equipLevel, final int p_cp, final int p_control,
			final int p_craftsmanship, final HashMap<Integer, EquipmentData> p_toAddTo)
	{
		p_toAddTo.put(p_equipLevel, new EquipmentData(p_name, p_equipLevel, p_cp, p_control, p_craftsmanship));
	}

	private static EquipmentData bestFromMap(final int p_level, final HashMap<Integer, EquipmentData> p_map)
	{
		for(int i = p_level; i >= 0; i--)
		{
			final EquipmentData data = p_map.get(i);
			if(data != null)
			{
				return data;
			}
		}

		throw new RuntimeException();
	}

	private final HashMap<Integer, EquipmentData> m_body	 = new HashMap<>();
	private final HashMap<Integer, EquipmentData> m_ears	 = new HashMap<>();
	private final HashMap<Integer, EquipmentData> m_feet	 = new HashMap<>();
	private final HashMap<Integer, EquipmentData> m_fingers	 = new HashMap<>();
	private final HashMap<Integer, EquipmentData> m_hands	 = new HashMap<>();
	private final HashMap<Integer, EquipmentData> m_head	 = new HashMap<>();
	private final HashMap<Integer, EquipmentData> m_legs	 = new HashMap<>();
	private final HashMap<Integer, EquipmentData> m_mainHand = new HashMap<>();
	private final HashMap<Integer, EquipmentData> m_neck	 = new HashMap<>();
	private final HashMap<Integer, EquipmentData> m_offHand	 = new HashMap<>();
	private final HashMap<Integer, EquipmentData> m_waist	 = new HashMap<>();
	private final HashMap<Integer, EquipmentData> m_wrists	 = new HashMap<>();

	public EquipmentTable()
	{
		loadAll();
	}

	private void addBody(final String p_name, final int p_equipLevel, final int p_cp, final int p_control,
			final int p_craftsmanship)
	{
		addToMap(p_name, p_equipLevel, p_cp, p_control, p_craftsmanship, m_body);
	}

	private void addEars(final String p_name, final int p_equipLevel, final int p_cp, final int p_control,
			final int p_craftsmanship)
	{
		addToMap(p_name, p_equipLevel, p_cp, p_control, p_craftsmanship, m_ears);
	}

	private void addFeet(final String p_name, final int p_equipLevel, final int p_cp, final int p_control,
			final int p_craftsmanship)
	{
		addToMap(p_name, p_equipLevel, p_cp, p_control, p_craftsmanship, m_feet);
	}

	private void addFinger(final String p_name, final int p_equipLevel, final int p_cp, final int p_control,
			final int p_craftsmanship)
	{
		addToMap(p_name, p_equipLevel, p_cp, p_control, p_craftsmanship, m_fingers);
	}

	private void addHand(final String p_name, final int p_equipLevel, final int p_cp, final int p_control,
			final int p_craftsmanship)
	{
		addToMap(p_name, p_equipLevel, p_cp, p_control, p_craftsmanship, m_hands);
	}

	private void addHead(final String p_name, final int p_equipLevel, final int p_cp, final int p_control,
			final int p_craftsmanship)
	{
		addToMap(p_name, p_equipLevel, p_cp, p_control, p_craftsmanship, m_head);
	}

	private void addLegs(final String p_name, final int p_equipLevel, final int p_cp, final int p_control,
			final int p_craftsmanship)
	{
		addToMap(p_name, p_equipLevel, p_cp, p_control, p_craftsmanship, m_legs);
	}

	private void addMainHand(final String p_name, final int p_equipLevel, final int p_cp, final int p_control,
			final int p_craftsmanship)
	{
		addToMap(p_name, p_equipLevel, p_cp, p_control, p_craftsmanship, m_mainHand);
	}

	private void addNeck(final String p_name, final int p_equipLevel, final int p_cp, final int p_control,
			final int p_craftsmanship)
	{
		addToMap(p_name, p_equipLevel, p_cp, p_control, p_craftsmanship, m_neck);
	}

	private void addOffHand(final String p_name, final int p_equipLevel, final int p_cp, final int p_control,
			final int p_craftsmanship)
	{
		addToMap(p_name, p_equipLevel, p_cp, p_control, p_craftsmanship, m_offHand);
	}

	private void addWaist(final String p_name, final int p_equipLevel, final int p_cp, final int p_control,
			final int p_craftsmanship)
	{
		addToMap(p_name, p_equipLevel, p_cp, p_control, p_craftsmanship, m_waist);
	}

	private void addWrists(final String p_name, final int p_equipLevel, final int p_cp, final int p_control,
			final int p_craftsmanship)
	{
		addToMap(p_name, p_equipLevel, p_cp, p_control, p_craftsmanship, m_wrists);
	}

	public EquipmentData getBestBody(final int p_level)
	{
		return bestFromMap(p_level, m_body);
	}

	public EquipmentData getBestEars(final int p_level)
	{
		return bestFromMap(p_level, m_ears);
	}

	public EquipmentData getBestFeet(final int p_level)
	{
		return bestFromMap(p_level, m_feet);
	}

	public EquipmentData getBestFinger(final int p_level)
	{
		return bestFromMap(p_level, m_fingers);
	}

	public EquipmentData getBestHands(final int p_level)
	{
		return bestFromMap(p_level, m_hands);
	}

	public EquipmentData getBestHead(final int p_level)
	{
		return bestFromMap(p_level, m_head);
	}

	public EquipmentData getBestLegs(final int p_level)
	{
		return bestFromMap(p_level, m_legs);
	}

	public EquipmentData getBestMainHand(final int p_level)
	{
		return bestFromMap(p_level, m_mainHand);
	}

	public EquipmentData getBestNeck(final int p_level)
	{
		return bestFromMap(p_level, m_neck);
	}

	public EquipmentData getBestOffHand(final int p_level)
	{
		return bestFromMap(p_level, m_offHand);
	}

	public EquipmentData getBestWaist(final int p_level)
	{
		return bestFromMap(p_level, m_waist);
	}

	public EquipmentData getBestWrists(final int p_level)
	{
		return bestFromMap(p_level, m_wrists);
	}

	private void loadAll()
	{
		loadBody();
		loadEars();
		loadFeet();
		loadFingers();
		loadHands();
		loadHead();
		loadLegs();
		loadMainHand();
		loadNeck();
		loadOffHand();
		loadWaist();
		loadWrists();

		loadEmpties();
	}

	private void loadBody()
	{
		addBody("Hempen Kurta", 1, 0, 0, 20);
		addBody("Hempen Shepherd's Tunic", 6, 0, 0, 27);
		addBody("Amateur's Kurta", 9, 0, 10, 30);
		addBody("Hempen Doublet Vest of Crafting", 12, 0, 11, 34);
		addBody("Cotton Shepher's Tunic", 15, 2, 15, 44);
		addBody("Cotton Kurta", 18, 2, 16, 49);
		addBody("Cotton Doublet Vest of Crafting", 21, 2, 18, 53);
		addBody("Initiate's Gown", 24, 2, 19, 57);
		addBody("Velveteen Coatee of Crafting", 26, 2, 20, 60);
		addBody("Velveteen Gown", 30, 2, 22, 66);
		addBody("Velveteen Doublet Vest of Crafting", 32, 2, 23, 69);
		addBody("Vintage Coatee", 37, 3, 27, 80);
		addBody("Vintage Chef's Apron", 40, 3, 28, 85);
		addBody("Vintage Seneschal Coatee", 44, 3, 30, 90);
		addBody("Dodore Doublet", 47, 3, 32, 95);
		addBody("Artisan's Gown", 50, 3, 35, 107);
		addBody("Holy Rainbow Coatee", 53, 3, 51, 161);
		addBody("Ramie Doublet of Crafting", 55, 4, 59, 190);
		addBody("Chimerical Felt Doublet of Crafting", 60, 4, 73, 234);
		addBody("Bloodhempen Doublet of Crafting", 61, 4, 88, 281);
		addBody("Ruby Cotton Coatee", 63, 4, 100, 318);
		addBody("Kudzu Robe of Crafting", 65, 4, 105, 337);
		addBody("Marid Leather Bolero of Crafting", 68, 5, 118, 377);
		addBody("Augmented Ala Mighan Doublet of Crafting", 70, 5, 154, 491);
	}

	private void loadEars()
	{
		addEars("Fang Earrings", 15, 23, 4, 3);
		addEars("Horn Earrings", 25, 26, 6, 4);
		addEars("Wolf Earrings", 35, 30, 7, 5);
		addEars("Red Coral Earrings", 45, 34, 9, 6);
		addEars("Yeti Fang Earrings", 52, 40, 14, 9);
		addEars("Dragon Fang Earrings", 57, 48, 19, 14);
		addEars("Larch Earrings", 63, 55, 30, 21);
		addEars("Persimmon Earrings", 67, 58, 34, 24);
		addEars("Zelkova Earrings", 70, 60, 38, 27);
	}

	private void loadEmpties()
	{
		m_body.put(0, EMPTY);
		m_ears.put(0, EMPTY);
		m_feet.put(0, EMPTY);
		m_fingers.put(0, EMPTY);
		m_hands.put(0, EMPTY);
		m_head.put(0, EMPTY);
		m_legs.put(0, EMPTY);
		m_mainHand.put(0, EMPTY);
		m_neck.put(0, EMPTY);
		m_offHand.put(0, EMPTY);
		m_waist.put(0, EMPTY);
		m_wrists.put(0, EMPTY);
	}

	private void loadFeet()
	{
		addFeet("Maple Clogs", 1, 0, 7, 0);
		addFeet("Maple Pattens", 5, 0, 9, 0);
		addFeet("Amateur's Thighboots", 10, 0, 11, 0);
		addFeet("Ash Pattens", 15, 3, 15, 3);
		addFeet("Initiate's Thighboots", 20, 3, 17, 3);
		addFeet("Elm Pattens", 24, 3, 19, 4);
		addFeet("Velveteen Gaiters", 28, 3, 21, 4);
		addFeet("Walnut Pattens", 33, 4, 23, 5);
		addFeet("Linen Gaiters", 38, 4, 26, 5);
		addFeet("Vintage Thighboots", 41, 4, 29, 6);
		addFeet("Artisan's Pattens", 50, 4, 35, 6);
		addFeet("Holy Rainbow Shoes", 53, 5, 51, 11);
		addFeet("Dhalmelskin Shoes", 55, 5, 59, 13);
		addFeet("Serpentskin Shoes", 60, 6, 73, 16);
		addFeet("Gaganskin Shoes", 61, 6, 88, 19);
		addFeet("Gyuki Leather Shoes", 63, 7, 100, 21);
		addFeet("Tigerskin Boots of Crafting", 65, 7, 105, 22);
		addFeet("Marid Leather Babouches of Crafting", 68, 7, 118, 25);
		addFeet("Augmented Ala Mighan Shoes of Crafting", 70, 8, 154, 33);
	}

	private void loadFingers()
	{
		addFinger("Brass Ring of Crafting", 18, 12, 6, 5);
		addFinger("Silver Ring of Crafting", 27, 14, 8, 6);
		addFinger("Mythril Ring of Crafting", 37, 16, 10, 8);
		addFinger("Electrum Ring of Crafting", 47, 18, 12, 9);
		addFinger("Holy Cedar Ring", 52, 21, 18, 14);
		addFinger("Hallowed Chestnut Ring", 56, 24, 25, 20);
		addFinger("Larch Ring", 63, 29, 40, 32);
		addFinger("Persimmon Ring", 67, 30, 45, 36);
		addFinger("Zelkova Ring", 70, 31, 51, 41);
	}

	private void loadHands()
	{
		addHand("Ehcatl Wristgloves", 1, 0, 9, 0);
		addHand("Amateur's Smithing Gloves", 10, 0, 11, 0);
		addHand("Cotton Halfgloves", 15, 3, 15, 3);
		addHand("Initiate's Gloves", 20, 3, 17, 3);
		addHand("Velveteen Halfgloves", 24, 3, 19, 4);
		addHand("Velveteen Shortgloves", 28, 3, 21, 4);
		addHand("Linen Halfgloves", 33, 4, 23, 5);
		addHand("Linen Shortgloves", 38, 4, 26, 5);
		addHand("Vintage Smithy's Gloves", 39, 4, 28, 6);
		addHand("Felt Halfgloves", 48, 4, 31, 6);
		addHand("Artisan's Fingerstalls", 50, 4, 35, 6);
		addHand("Holy Rainbow Gloves", 53, 5, 51, 11);
		addHand("Dhalmelskin Halfgloves of Crafting", 55, 5, 59, 13);
		addHand("Serpentskin Halfgloves of Crafting", 60, 6, 73, 16);
		addHand("Gaganskin Halfgloves", 61, 6, 88, 19);
		addHand("Gyuki Leather Gloves of Crafting", 63, 7, 100, 21);
		addHand("Durium Chaplets", 65, 7, 105, 22);
		addHand("Marid Leather Gloves of Crafting", 68, 7, 118, 25);
		addHand("Augmented Ala Mhigan Gloves of Crafting", 70, 8, 154, 33);
	}

	private void loadHead()
	{
		addHead("Leather Calot", 1, 0, 7, 0);
		addHead("Hempen Bandana", 5, 0, 9, 0);
		addHead("Amateur's Headgear", 10, 0, 11, 0);
		addHead("Cotton Scarf", 15, 3, 15, 3);
		addHead("Initiate's Headgear", 20, 3, 17, 3);
		addHead("Velveteen Bandana", 23, 3, 19, 3);
		addHead("Silver Magnifiers", 25, 3, 20, 4);
		addHead("Velveteen Beret", 29, 3, 22, 4);
		addHead("Mythril Magnifiers", 34, 4, 24, 5);
		addHead("Linen Wedge Cap of Crafting", 38, 4, 26, 5);
		addHead("Vintage Chef's Cap", 41, 4, 29, 6);
		addHead("Artisan's Spectacles", 50, 4, 39, 6);
		addHead("Ramie Turban of Crafting", 55, 5, 59, 13);
		addHead("Chimerical Felt Turban of Crafting", 60, 6, 73, 16);
		addHead("Bloodhempen Turban of Crafting", 61, 6, 88, 19);
		addHead("Ruby Cotton Cap", 63, 7, 100, 21);
		addHead("Kudzu Cap of Crafting", 65, 7, 105, 22);
		addHead("Serge Turban of Crafting", 68, 7, 118, 25);
		addHead("Augmented Ala Mhigan Turban of Crafting", 70, 8, 154, 33);
	}

	private void loadLegs()
	{
		addLegs("Hempen Breeches of Crafting", 1, 0, 7, 0);
		addLegs("Hempen Chausses", 5, 0, 9, 0);
		addLegs("Hempen Bottoms", 10, 0, 11, 0);
		addLegs("Cotton Chausses", 15, 3, 15, 3);
		addLegs("Cotton Breeches of Crafting", 19, 3, 17, 3);
		addLegs("Velveteen Chausses", 24, 3, 19, 4);
		addLegs("Velveteen Bottom", 28, 3, 21, 4);
		addLegs("Linen Chausses", 33, 4, 23, 5);
		addLegs("Linen Bottom", 38, 4, 26, 5);
		addLegs("Linen Slops", 41, 4, 27, 5);
		addLegs("Woolen Chausses", 43, 4, 28, 6);
		addLegs("Woolen Slops", 46, 4, 30, 6);
		addLegs("Felt Chausses", 48, 4, 31, 6);
		addLegs("Artisan's Chausses", 50, 4, 35, 6);
		addLegs("Holy Rainbow Bottoms", 53, 5, 51, 11);
		addLegs("Ramie Skirt", 55, 5, 59, 13);
		addLegs("Chimerical Felt Skirt", 60, 6, 73, 16);
		addLegs("Bloodhempen Skirt", 61, 6, 88, 19);
		addLegs("Ruby Cotton Bottoms", 63, 7, 100, 21);
		addLegs("Kudzu Culottes of Crafting", 65, 7, 105, 22);
		addLegs("Serge Sarouel of Crafting", 68, 7, 118, 25);
		addLegs("Augmented Ala Mhigan Bottoms of Crafting", 70, 8, 154, 33);
	}

	private void loadMainHand()
	{
		addMainHand("MH1", 1, 0, 0, 24);
		addMainHand("MH8", 8, 0, 19, 34);
		addMainHand("MH12", 12, 0, 23, 40);
		addMainHand("MH16", 16, 2, 30, 53);
		addMainHand("MH19", 19, 2, 33, 58);
		addMainHand("MH23", 23, 2, 37, 65);
		addMainHand("MH28", 28, 2, 42, 74);
		addMainHand("MH31", 31, 2, 45, 78);
		addMainHand("MH34", 34, 2, 48, 84);
		addMainHand("MH39", 39, 3, 53, 92);
		addMainHand("MH43", 43, 3, 56, 99);
		addMainHand("MH48", 48, 3, 61, 107);
		addMainHand("MH50", 50, 3, 78, 139);
		addMainHand("MH52", 52, 4, 108, 200);
		addMainHand("MH55", 55, 4, 119, 222);
		addMainHand("MH57", 57, 4, 129, 240);
		addMainHand("MH60", 60, 4, 146, 273);
		addMainHand("MH62", 62, 4, 187, 349);
		addMainHand("MH65", 65, 4, 211, 393);
		addMainHand("MH67", 67, 5, 226, 421);
		addMainHand("MH70", 70, 5, 316, 589);
	}

	private void loadNeck()
	{
		addNeck("Copper Choker", 9, 17, 0, 0);
		addNeck("Brass Choker", 19, 24, 5, 3);
		addNeck("Silver Choker", 29, 28, 6, 4);
		addNeck("Mythril Choker", 39, 31, 8, 5);
		addNeck("Electrum Choker", 49, 35, 9, 6);
		addNeck("Holy Cedar Necklace", 54, 45, 17, 12);
		addNeck("Hallowed Chestnut Necklace", 59, 49, 21, 15);
		addNeck("Larch Necklace", 63, 55, 30, 21);
		addNeck("Persimmon Necklace", 67, 58, 34, 24);
		addNeck("Zelkova Necklace", 70, 60, 38, 27);
	}

	private void loadOffHand()
	{
		addOffHand("OH5", 5, 0, 17, 30);
		addOffHand("OH11", 11, 0, 22, 38);
		addOffHand("OH20", 20, 2, 34, 60);
		addOffHand("OH24", 24, 2, 38, 67);
		addOffHand("OH31", 31, 2, 45, 78);
		addOffHand("OH35", 35, 2, 49, 85);
		addOffHand("OH43", 43, 3, 56, 99);
		addOffHand("OH47", 47, 3, 60, 105);
		addOffHand("OH53", 53, 4, 114, 212);
		addOffHand("OH56", 56, 4, 124, 231);
		addOffHand("OH59", 59, 4, 140, 262);
		addOffHand("OH63", 63, 4, 199, 371);
		addOffHand("OH66", 66, 5, 217, 404);
		addOffHand("OH68", 68, 5, 236, 439);
		addOffHand("OH70", 70, 5, 256, 477);
	}

	private void loadWaist()
	{
		addWaist("Merchant's Purse", 1, 0, 7, 0);
		addWaist("Hard Leather Merchant's Pouch", 10, 0, 11, 0);
		addWaist("Goatskin Toolbelt", 18, 3, 16, 3);
		addWaist("Velveteen Half Apron", 28, 3, 21, 4);
		addWaist("Vintage Half Apron", 35, 4, 26, 5);
		addWaist("Vintage Chef's Belt", 37, 4, 27, 5);
		addWaist("Boarskin Toolbelt", 42, 4, 28, 6);
		addWaist("Raptorskin Artisan's Belt", 46, 4, 30, 6);
		addWaist("Raptorskin Merchant's Purse", 48, 4, 31, 6);
		addWaist("Holy Rainbow Apron", 54, 5, 57, 12);
		addWaist("Dragonskin Belt of Crafting", 59, 6, 70, 15);
		addWaist("Ruby Cotton Apron", 62, 6, 94, 20);
		addWaist("Serge Apron", 66, 7, 108, 23);
		addWaist("Twinsilk Apron", 70, 7, 128, 27);
	}

	private void loadWrists()
	{
		addWrists("Brass Wristelts of Crafting", 16, 23, 5, 3);
		addWrists("Silver Wristlets of Crafting", 26, 27, 6, 4);
		addWrists("Mythril Wristlets of Crafting", 36, 30, 7, 5);
		addWrists("Electrum Wristlets of Crafting", 46, 34, 9, 6);
		addWrists("Holy Cedar Armillae", 53, 43, 15, 11);
		addWrists("Holy Chestnut Armillae", 58, 48, 20, 14);
		addWrists("Larch Bracelets", 63, 55, 30, 21);
		addWrists("Persimmon Bracelets", 67, 58, 34, 24);
		addWrists("Zelkova Bracelets", 70, 60, 38, 27);
	}
}
