package nn;

public enum NeuronType
{
	HIDDEN, INPUT, OUTPUT;

	public static NeuronType nextLevelDown(final NeuronType p_level)
	{
		switch(p_level)
		{
			case HIDDEN:
				return INPUT;
			case INPUT:
				throw new RuntimeException();
			case OUTPUT:
				return HIDDEN;
			default:
				throw new RuntimeException();
		}
	}
}
