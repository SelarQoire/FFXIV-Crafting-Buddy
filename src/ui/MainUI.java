package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import crafting.CrafterClass;

public class MainUI extends JPanel
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Create the GUI and show it. For thread safety, this method should be invoked
	 * from the event dispatch thread.
	 */
	private static void createAndShowGUI()
	{
		// Create and set up the window.
		final JFrame frame = new JFrame("FFXIV Crafting Buddy");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Add content to the window.
		frame.add(new MainUI(), BorderLayout.CENTER);

		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(final String[] args)
	{
		// Schedule a job for the event dispatch thread:
		// creating and showing this application's GUI.
		SwingUtilities.invokeLater(() ->
		{
			createAndShowGUI();
		});
	}

	public MainUI()
	{
		super(new GridLayout(1, 1));

		final JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setPreferredSize(new Dimension(500, 300));

		final JComponent crafterPanel = makeCrafterPanel();
		tabbedPane.addTab("Crafter", null, crafterPanel, "Crafter stats");

		final JComponent synthPanel = makeSynthPanel();
		tabbedPane.addTab("Synth", null, synthPanel, "Recipe and misc stats");

		final JComponent crossClassPanel = makeCrossClassPanel();
		tabbedPane.addTab("Cross-class", null, crossClassPanel, "Calculate suggested cross-class actions");

		final JComponent simulationPanel = makeSimulationPanel();
		tabbedPane.addTab("Simulation", null, simulationPanel, "Run simulation");

		final JComponent panel6 = makeSettingsPanel();
		tabbedPane.addTab("Settings", null, panel6, "Adjust simulator settings");

		// Add the tabbed pane to this panel.
		add(tabbedPane);

		// The following line enables to use scrolling tabs.
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
	}

	private JLabel makeAlignedLabel(final String p_text, final int p_hAlignment, final int p_vAlignment)
	{
		final JLabel label = new JLabel(p_text);
		label.setHorizontalAlignment(p_hAlignment);
		label.setVerticalAlignment(p_vAlignment);
		return label;
	}

	private JPanel makeCrafterPanel()
	{
		final JPanel panel = new JPanel(false);
		panel.setLayout(new GridLayout(9, 5, 5, 5));

		panel.add(makeAlignedLabel("Class", JLabel.CENTER, JLabel.CENTER));
		panel.add(makeAlignedLabel("Level", JLabel.CENTER, JLabel.CENTER));
		panel.add(makeAlignedLabel("Craftsmanship", JLabel.CENTER, JLabel.CENTER));
		panel.add(makeAlignedLabel("Control", JLabel.CENTER, JLabel.CENTER));
		panel.add(makeAlignedLabel("CP", JLabel.CENTER, JLabel.CENTER));

		for(final CrafterClass cClass: CrafterClass.values())
		{
			if(cClass != CrafterClass.ALL)
			{
				panel.add(makeAlignedLabel(cClass.getName(), JLabel.CENTER, JLabel.CENTER));
				panel.add(new JTextField("0"));
				panel.add(new JTextField("0"));
				panel.add(new JTextField("0"));
				panel.add(new JTextField("0"));
			}
		}

		return panel;
	}

	private JPanel makeCrossClassPanel()
	{
		final JPanel panel = new JPanel(false);

		final JPanel wrapper = new JPanel(false);
		panel.setLayout(new BorderLayout());
		wrapper.add(new JButton("Calculate Recommended Cross Class Skills"));
		final JTextArea textArea = new JTextArea("");

		textArea.setEditable(false);
		textArea.setPreferredSize(new Dimension(400, 200));

		final JPanel wrapper2 = new JPanel(false);
		wrapper2.add(textArea);

		panel.add(wrapper, BorderLayout.NORTH);
		panel.add(wrapper2, BorderLayout.CENTER);

		return panel;
	}

	private JPanel makeSettingsPanel()
	{
		final JPanel panel = new JPanel(false);

		panel.setLayout(new GridLayout(9, 1, 5, 5));
		panel.add(new TextFieldWithLabel("Sim search depth", "3"));
		panel.add(new TextFieldWithLabel("Cross Class Recommendation Sims", "100"));

		return panel;
	}

	private JPanel makeSimulationPanel()
	{
		final JPanel panel = new JPanel(false);
		panel.setLayout(new BorderLayout());

		final JPanel buttonPanel = new JPanel(false);
		final JPanel descriptionPanel = new JPanel(false);
		final JPanel actionsPanel = new JPanel(false);

		panel.add(buttonPanel, BorderLayout.NORTH);
		panel.add(descriptionPanel, BorderLayout.CENTER);
		panel.add(actionsPanel, BorderLayout.SOUTH);

		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(new JButton("Start simulation"));
		buttonPanel.add(new JButton("Reset simulation"));

		final JTextArea textArea = new JTextArea("");
		textArea.setPreferredSize(new Dimension(400, 200));
		descriptionPanel.add(textArea);

		final JScrollPane scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(400, 200));
		scrollPane.add(new JPanel(false));
		actionsPanel.add(scrollPane);

		return panel;
	}

	private JPanel makeSynthPanel()
	{
		final JPanel panel = new JPanel(false);
		panel.setLayout(new GridLayout(7, 1, 5, 5));

		final JPanel temp = new JPanel(new GridLayout(1, 2));
		final JComboBox<String> classBox = new JComboBox<>();
		for(final CrafterClass cClass: CrafterClass.values())
		{
			if(cClass != CrafterClass.ALL)
			{
				classBox.addItem(cClass.getName());
			}
		}

		temp.add(makeAlignedLabel("Crafting class", JLabel.CENTER, JLabel.CENTER));
		temp.add(classBox);
		panel.add(temp);
		panel.add(new TextFieldWithLabel("Recipe Difficulty", "0"));
		panel.add(new TextFieldWithLabel("Recipe Max Quality", "0"));
		panel.add(new TextFieldWithLabel("Start Quality", "0"));
		panel.add(new TextFieldWithLabel("HQ Product Value", "0"));
		panel.add(new TextFieldWithLabel("NQ Product Value", "0"));
		panel.add(new TextFieldWithLabel("Materials Value", "0"));

		return panel;
	}
}
