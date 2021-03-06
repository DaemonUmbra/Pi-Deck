package dev.theturkey.pideckapp.ui;


import dev.theturkey.pideckapp.Core;
import dev.theturkey.pideckapp.Util;
import dev.theturkey.pideckapp.config.Config;
import dev.theturkey.pideckapp.profile.ActionInfo;
import dev.theturkey.pideckapp.profile.Button;

import javax.swing.*;
import javax.swing.plaf.metal.MetalButtonUI;
import java.awt.*;

public class InfoPanel extends JPanel
{
	private Button currentBtn;

	private JLabel buttonIDLabel;
	private JButton bgColorButton;

	private JPanel actionsPanel;


	public InfoPanel()
	{
		setLayout(new GridBagLayout());
		setBackground(UIFrame.BACKGROUND_SECONDARY);

		setVisible(false);
		setPreferredSize(new Dimension(200, getHeight()));

		JPanel titlePanel = new JPanel();
		titlePanel.setBackground(UIFrame.BACKGROUND_SECONDARY);
		buttonIDLabel = new JLabel("Button: ");
		buttonIDLabel.setForeground(UIFrame.TEXT_PRIMARY);
		buttonIDLabel.getInsets().set(10, 0, 10, 0);
		titlePanel.add(buttonIDLabel);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(titlePanel, gbc);

		JPanel colorPanel = new JPanel();
		colorPanel.setBackground(UIFrame.BACKGROUND_SECONDARY);
		JLabel bgColorLabel = new JLabel("Color: ");
		bgColorLabel.setForeground(UIFrame.TEXT_PRIMARY);
		colorPanel.add(bgColorLabel);

		bgColorButton = new JButton();
		bgColorButton.setSize(32, 32);
		bgColorButton.setPreferredSize(new Dimension(32, 32));
		bgColorButton.setUI(new MetalButtonUI());
		bgColorButton.setOpaque(true);
		bgColorButton.setFocusPainted(false);
		bgColorButton.addActionListener(e ->
		{
			if(currentBtn == null)
				return;

			Color newColor = JColorChooser.showDialog(null, "Choose a color", bgColorButton.getBackground());
			if(newColor != null)
			{
				bgColorButton.setBackground(newColor);
				currentBtn.setBgColor(newColor);
				Core.getPiDeck().updateButton(currentBtn);
				Core.getUI().updateSim();
				Config.saveProfiles();
			}
		});
		colorPanel.add(bgColorButton);

		colorPanel.setMaximumSize(getSize());

		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridy = 1;
		add(colorPanel, gbc);

		actionsPanel = new JPanel();
		actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.Y_AXIS));
		actionsPanel.setBackground(UIFrame.BACKGROUND_PRIMARY);

		JPanel actionsPanelTopBar = new JPanel();
		actionsPanelTopBar.setBackground(UIFrame.BACKGROUND_PRIMARY);

		JLabel actionsPanelLabel = new JLabel("Actions");
		actionsPanelLabel.setForeground(UIFrame.TEXT_PRIMARY);
		actionsPanelTopBar.add(actionsPanelLabel);

		JButton addAction = new JButton("+");
		addAction.setBackground(UIFrame.PRIMARY_MAIN);
		addAction.setForeground(UIFrame.TEXT_PRIMARY);
		addAction.setUI(new MetalButtonUI());
		addAction.setOpaque(true);
		addAction.setFocusPainted(false);
		addAction.addActionListener(e ->
		{
			currentBtn.addAction(new ActionInfo());
			Config.saveProfiles();
			updateActionsPanel();
			updateUI();
		});
		actionsPanelTopBar.add(addAction);

		actionsPanel.add(actionsPanelTopBar);

		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1;
		gbc.gridy = 2;
		add(actionsPanel, gbc);

		JPanel fill = new JPanel();
		fill.setBackground(UIFrame.BACKGROUND_SECONDARY);
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridy = 3;
		gbc.weighty = 1;
		add(fill, gbc);
	}


	public void setInfoPanelButton(Button button)
	{
		currentBtn = button;
		buttonIDLabel.setText("Button: " + button.getID());

		bgColorButton.setBackground(Util.hex2Rgb(button.getBgColor()));

		updateActionsPanel();

		setPreferredSize(new Dimension(150, getHeight()));
		setVisible(true);
		updateUI();
	}

	public void updateActionsPanel()
	{
		for(int i = actionsPanel.getComponentCount() - 1; i >= 0; i--)
			if(actionsPanel.getComponent(i) instanceof ActionPanel)
				actionsPanel.remove(i);

		for(ActionInfo action : currentBtn.getActions())
			actionsPanel.add(new ActionPanel(this, currentBtn, action));
	}
}
