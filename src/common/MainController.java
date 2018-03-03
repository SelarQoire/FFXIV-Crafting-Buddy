package common;

import q.DDQAgent;

public class MainController
{
	public static void main(final String[] p_args)
	{
		final DDQAgent agent = new DDQAgent();
		agent.runLearningAgent();
	}
}
