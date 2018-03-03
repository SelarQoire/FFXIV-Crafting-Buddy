package crafting;

public enum Condition
{
	EXCELLENT, GOOD, NORMAL, POOR;

	boolean checkGoodOrExcellent()
	{
		return this == GOOD || this == EXCELLENT;
	}

	public double getQualityMultiplier()
	{
		switch(this)
		{
			case EXCELLENT:
				return 4;
			case GOOD:
				return 1.5;
			case NORMAL:
				return 1;
			case POOR:
				return .5;
			default:
				throw new RuntimeException();
		}
	}
}
