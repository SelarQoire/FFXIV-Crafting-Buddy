package ui;

import javax.swing.JPanel;
import javax.swing.JTextField;

public class WrappedTextField extends JPanel
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private final JTextField  m_textField;

	public WrappedTextField()
	{
		m_textField = new JTextField("0", 5);
		add(m_textField);
	}

	public JTextField getTextField()
	{
		return m_textField;
	}
}
