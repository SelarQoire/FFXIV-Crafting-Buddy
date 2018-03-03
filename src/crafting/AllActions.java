package crafting;

public class AllActions
{
	public static final Action		  ADVANCED_TOUCH	   = new Action("Advanced Touch", 43, CrafterClass.ALL, 48, .9,
			0, 1.5, 10, ActionType.IMMEDIATE, false);
	public static final Action		  BASIC_SYNTHESIS	   = new Action("Basic Synthesis", 1, CrafterClass.ALL, 0, .9,
			1.0, 0, 10, ActionType.IMMEDIATE, false);
	public static final Action		  BASIC_TOUCH		   = new Action("Basic Touch", 5, CrafterClass.ALL, 18, .7, 0,
			1.0, 10, ActionType.IMMEDIATE, false);
	public static final BrandOfAction BRAND_OF_EARTH	   = new BrandOfAction(Element.EARTH, CrafterClass.LTW);
	public static final BrandOfAction BRAND_OF_FIRE		   = new BrandOfAction(Element.FIRE, CrafterClass.BSM);
	public static final BrandOfAction BRAND_OF_ICE		   = new BrandOfAction(Element.ICE, CrafterClass.ARM);
	public static final BrandOfAction BRAND_OF_LIGHTNING   = new BrandOfAction(Element.LIGHTNING, CrafterClass.WVR);
	public static final BrandOfAction BRAND_OF_WATER	   = new BrandOfAction(Element.WATER, CrafterClass.CUL);
	public static final BrandOfAction BRAND_OF_WIND		   = new BrandOfAction(Element.WIND, CrafterClass.CRP);
	public static final Action		  BYREGOTS_BLESSING	   = new Action("Byregot's Blessing", 50, CrafterClass.CRP, 24,
			.9, 0, 1.0, 10, ActionType.IMMEDIATE, false);
	public static final Action		  BYREGOTS_BROW		   = new Action("Byregot's Brow", 51, CrafterClass.ALL, 18, .7,
			0, 1.5, 10, ActionType.IMMEDIATE, false);
	public static final Action		  BYREGOTS_MIRACLE	   = new Action("Byregot's Miracle", 58, CrafterClass.ALL, 10,
			.7, 0, 1.0, 10, ActionType.IMMEDIATE, true);
	public static final Action		  CAREFUL_SYNTHESIS	   = new Action("Careful Synthesis", 15, CrafterClass.WVR, 0,
			1.0, .9, 0, 10, ActionType.IMMEDIATE, false);
	public static final Action		  CAREFUL_SYNTHESIS_2  = new Action("Careful Synthesis II", 50, CrafterClass.WVR, 0,
			1.0, 1.2, 0, 10, ActionType.IMMEDIATE, false);
	public static final Action		  CAREFUL_SYNTHESIS_3  = new Action("Careful Synthesis III", 62, CrafterClass.ALL,
			7, 1.0, 1.5, 0, 10, ActionType.IMMEDIATE, false);
	// public static final Action COLLECTABLE_SYNTHESIS
	public static final Action		  COMFORT_ZONE		   = new Action("Comfort Zone", 50, CrafterClass.ALC, 66, 1.0,
			0, 0, 0, ActionType.COUNT_DOWN, false);
	public static final Action		  FINISHING_TOUCHES	   = new Action("Finishing Touches", 55, CrafterClass.ALL, 0,
			.5, 2.0, 0, 10, ActionType.UNUSABLE, false);
	public static final Action		  FLAWLESS_SYNTHESIS   = new Action("Flawless Synthesis", 37, CrafterClass.GSM, 15,
			.9, 0, 0, 10, ActionType.IMMEDIATE, false);
	public static final Action		  FOCUSED_SYNTHESIS	   = new Action("Focused Synthesis", 67, CrafterClass.ALL, 5,
			.5, 2.0, 0, 10, ActionType.IMMEDIATE, false);
	public static final Action		  FOCUSED_TOUCH		   = new Action("Focused Touch", 68, CrafterClass.ALL, 18, .5,
			0.0, 1.5, 10, ActionType.IMMEDIATE, false);
	public static final Action		  GREAT_STRIDES		   = new Action("Great Strides", 21, CrafterClass.ALL, 32, 1.0,
			0, 0, 0, ActionType.COUNT_DOWN, false);
	public static final Action		  HASTY_TOUCH		   = new Action("Hasty Touch", 15, CrafterClass.CUL, 0, .5, 0,
			1.0, 10, ActionType.IMMEDIATE, false);
	public static final Action		  HASTY_TOUCH_2		   = new Action("Hasty Touch II", 61, CrafterClass.ALL, 5, .6,
			0, 1.0, 10, ActionType.IMMEDIATE, false);
	public static final Action		  INGENUITY			   = new Action("Ingenuity", 15, CrafterClass.BSM, 24, 1.0, 0,
			0, 0, ActionType.COUNT_DOWN, false);
	public static final Action		  INGENUITY_2		   = new Action("Ingenuity II", 50, CrafterClass.BSM, 32, 1.0,
			0, 0, 0, ActionType.COUNT_DOWN, false);
	public static final Action		  INITIAL_PREPARATIONS = new Action("Initial Preparations", 69, CrafterClass.ALL,
			50, 1.0, 0, 0, 0, ActionType.INDEFINITE, false);
	public static final Action		  INNER_QUIET		   = new Action("Inner Quiet", 11, CrafterClass.ALL, 18, 1.0, 0,
			0, 0, ActionType.COUNT_UP, false);
	public static final Action		  INNOVATION		   = new Action("Innovation", 50, CrafterClass.GSM, 18, 1.0, 0,
			0, 0, ActionType.COUNT_DOWN, false);
	public static final Action		  INNOVATIVE_TOUCH	   = new Action("Innovative Touch", 56, CrafterClass.ALL, 8, .4,
			0, 1.0, 10, ActionType.IMMEDIATE, true);
	public static final Action		  MAKERS_MARK		   = new Action("Maker's Mark", 54, CrafterClass.GSM, 20, 1.0,
			0, 0, 0, ActionType.COUNT_DOWN, false);
	public static final Action		  MANIPULATION		   = new Action("Manipulation", 15, CrafterClass.GSM, 88, 1.0,
			0, 0, 0, ActionType.COUNT_DOWN, false);
	public static final Action		  MANIPULATION_2	   = new Action("Manipulation II", 65, CrafterClass.ALL, 96,
			1.0, 0, 0, 0, ActionType.COUNT_DOWN, false);
	public static final Action		  MASTERS_MEND		   = new Action("Master's Mend", 7, CrafterClass.ALL, 92, 1.0,
			0, 0, 0, ActionType.IMMEDIATE, false);
	public static final Action		  MASTERS_MEND_2	   = new Action("Master's Mend II", 28, CrafterClass.ALL, 160,
			1.0, 0, 0, 0, ActionType.IMMEDIATE, false);
	public static final Action		  MUSCLE_MEMORY		   = new Action("Muscle Memory", 54, CrafterClass.CUL, 6, 1.0,
			0, 0, 10, ActionType.IMMEDIATE, false);
	public static final NameOfAction  NAME_OF_EARTH		   = new NameOfAction(Element.EARTH, CrafterClass.LTW);
	public static final NameOfAction  NAME_OF_FIRE		   = new NameOfAction(Element.FIRE, CrafterClass.BSM);
	public static final NameOfAction  NAME_OF_ICE		   = new NameOfAction(Element.ICE, CrafterClass.ARM);
	public static final NameOfAction  NAME_OF_LIGHTNING	   = new NameOfAction(Element.LIGHTNING, CrafterClass.WVR);
	public static final NameOfAction  NAME_OF_WATER		   = new NameOfAction(Element.WATER, CrafterClass.CUL);
	public static final NameOfAction  NAME_OF_WIND		   = new NameOfAction(Element.WIND, CrafterClass.CRP);
	public static final Action		  NONE				   = new Action("NONE", 1, CrafterClass.ALL, 0, 1.0, 0, 0, 0,
			ActionType.UNUSABLE, false);
	public static final Action		  NYMEIAS_WHEEL		   = new Action("Nymeia's Wheel", 57, CrafterClass.ALL, 18, 1.0,
			0, 0, 0, ActionType.IMMEDIATE, true);
	public static final Action		  OBSERVE			   = new Action("Observe", 13, CrafterClass.ALL, 7, 1.0, 0.0,
			0.0, 0, ActionType.IMMEDIATE, false);
	public static final Action		  PATIENT_TOUCH		   = new Action("Patient Touch", 64, CrafterClass.ALL, 6, .5, 0,
			1.0, 10, ActionType.IMMEDIATE, false);
	public static final Action		  PIECE_BY_PIECE	   = new Action("Piece By Piece", 50, CrafterClass.ARM, 15, .9,
			0, 0, 10, ActionType.IMMEDIATE, false);
	public static final Action		  PRECISE_TOUCH		   = new Action("Precise Touch", 53, CrafterClass.ALL, 18, .7,
			0, 1.0, 10, ActionType.IMMEDIATE, false);
	public static final Action		  PRUDENT_TOUCH		   = new Action("Prudent Touch", 66, CrafterClass.ALL, 21, .7,
			0, 1.0, 5, ActionType.IMMEDIATE, false);
	public static final Action		  RAPID_SYNTHESIS	   = new Action("Rapid Synthesis", 15, CrafterClass.ARM, 0, .5,
			2.5, 0, 10, ActionType.IMMEDIATE, false);
	public static final Action		  RAPID_SYNTHESIS_2	   = new Action("Rapid Synthesis II", 63, CrafterClass.ALL, 12,
			.6, 3.0, 0, 10, ActionType.IMMEDIATE, false);
	public static final Action		  RECLAIM			   = new Action("Reclaim", 50, CrafterClass.CUL, 50, 1.0, 0, 0,
			0, ActionType.INDEFINITE, false);
	public static final Action		  RUMINATION		   = new Action("Rumination", 15, CrafterClass.CRP, 0, 1.0, 0,
			0, 0, ActionType.IMMEDIATE, false);
	public static final Action		  SATISFACTION		   = new Action("Satisfaction", 55, CrafterClass.ALL, 0, 1.0, 0,
			0, 0, ActionType.IMMEDIATE, true);
	public static final Action		  SPECIALTY_REFLECT	   = new Action("Specialty:Reflect", 69, CrafterClass.ALL, 0,
			1.0, 0, 0, 0, ActionType.IMMEDIATE, false);
	public static final Action		  SPECIALTY_REFURBISH  = new Action("Specialy:Refurbish", 69, CrafterClass.ALL, 0,
			1.0, 0, 0, 0, ActionType.IMMEDIATE, true);
	public static final Action		  SPECIALTY_REINFORCE  = new Action("Specialty:Reinforce", 69, CrafterClass.ALL, 0,
			1.0, 0, 0, 0, ActionType.IMMEDIATE, true);
	public static final Action		  STANDARD_SYNTHESIS   = new Action("Standard Synthesis", 33, CrafterClass.ALL, 15,
			.9, 1.5, 0, 10, ActionType.IMMEDIATE, true);
	public static final Action		  STANDARD_TOUCH	   = new Action("Standard Touch", 18, CrafterClass.ALL, 32, .8,
			0, 1.25, 10, ActionType.IMMEDIATE, false);
	public static final Action		  STEADY_HAND		   = new Action("Steady Hand", 9, CrafterClass.ALL, 22, 1.0, 0,
			0, 0, ActionType.COUNT_DOWN, false);
	public static final Action		  STEADY_HAND_2		   = new Action("Steady Hand II", 37, CrafterClass.CUL, 25, 1.0,
			0, 0, 0, ActionType.COUNT_DOWN, false);
	public static final Action		  STROKE_OF_GENIUS	   = new Action("Stroke Of Genius", 70, CrafterClass.ALL, 0,
			1.0, 0, 0, 0, ActionType.UNUSABLE, false);
	public static final Action		  TRAINED_HAND		   = new Action("Trained Hand", 59, CrafterClass.ALL, 16, .8,
			1.5, 1.5, 10, ActionType.IMMEDIATE, true);
	public static final Action		  TRICKS_OF_THE_TRADE  = new Action("Tricks Of The Trade", 15, CrafterClass.ALC, 0,
			1.0, 0, 0, 0, ActionType.IMMEDIATE, false);
	public static final Action		  WASTE_NOT			   = new Action("Waste Not", 15, CrafterClass.LTW, 56, 1.0, 0,
			0, 0, ActionType.COUNT_DOWN, false);
	public static final Action		  WASTE_NOT_2		   = new Action("Waste Not II", 50, CrafterClass.LTW, 98, 1.0,
			0, 0, 0, ActionType.COUNT_DOWN, false);
	public static final Action		  WHISTLE			   = new Action("Whistle While You Work", 55, CrafterClass.ALL,
			36, 1.0, 0, 0, 0, ActionType.COUNT_DOWN, true);

	public static final Action[]	  Z_ACTIONS_LIST	   = {ADVANCED_TOUCH, BASIC_SYNTHESIS, BASIC_TOUCH,
			BRAND_OF_EARTH, BRAND_OF_FIRE, BRAND_OF_ICE, BRAND_OF_LIGHTNING, BRAND_OF_WATER, BRAND_OF_WIND,
			BYREGOTS_BLESSING, BYREGOTS_BROW, BYREGOTS_MIRACLE, CAREFUL_SYNTHESIS, CAREFUL_SYNTHESIS_2,
			CAREFUL_SYNTHESIS_3, COMFORT_ZONE, FLAWLESS_SYNTHESIS, FOCUSED_SYNTHESIS, FOCUSED_TOUCH, GREAT_STRIDES,
			HASTY_TOUCH, HASTY_TOUCH_2, INGENUITY, INGENUITY_2, INITIAL_PREPARATIONS, INNER_QUIET, INNOVATION,
			INNOVATIVE_TOUCH, MAKERS_MARK, MANIPULATION, MANIPULATION_2, MASTERS_MEND, MASTERS_MEND_2, MUSCLE_MEMORY,
			NAME_OF_EARTH, NAME_OF_FIRE, NAME_OF_ICE, NAME_OF_LIGHTNING, NAME_OF_WATER, NAME_OF_WIND, NYMEIAS_WHEEL,
			OBSERVE, PATIENT_TOUCH, PIECE_BY_PIECE, PRECISE_TOUCH, PRUDENT_TOUCH, RAPID_SYNTHESIS, RAPID_SYNTHESIS_2,
			RECLAIM, RUMINATION, SATISFACTION, SPECIALTY_REFLECT, SPECIALTY_REFURBISH, SPECIALTY_REINFORCE,
			STANDARD_SYNTHESIS, STANDARD_TOUCH, STEADY_HAND, STEADY_HAND_2, TRAINED_HAND, TRICKS_OF_THE_TRADE,
			WASTE_NOT, WASTE_NOT_2, WHISTLE};
}
