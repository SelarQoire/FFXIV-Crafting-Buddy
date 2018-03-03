package ui;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TextFieldWithLabel extends JPanel
{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private final JLabel	  m_label;
	private final JTextField  m_textField;

	public TextFieldWithLabel(final String p_label, final String p_defaultText)
	{
		super(new GridLayout(1, 2));

		m_label = new JLabel(p_label);
		m_label.setVerticalAlignment(JLabel.CENTER);
		m_label.setHorizontalAlignment(JLabel.CENTER);
		add(m_label);

		m_textField = new JTextField();
		m_textField.setText(p_defaultText);
		add(m_textField);
	}

	public JTextField getTextField()
	{
		return m_textField;
	}
}
