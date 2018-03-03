package common;

public class Constants
{
	public static final double LEAKY_MULTIPLIER	= .0001;
	public static final double NN_NOISE_SCALE	= .001;
	public static final int	   NUM_THREADS		= Runtime.getRuntime().availableProcessors();
	public static final double Q_GAMMA			= .995;
	public static final int	   Q_LEARNING_SIMS	= 10000;
	public static final double SOFTMAX_TEMP		= .1;
}
