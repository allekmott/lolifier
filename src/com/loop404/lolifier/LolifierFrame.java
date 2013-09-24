/*
 * LolifierFrame.java
 * Created: 17-09-2013
 */

package com.loop404.lolifier;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;

import javax.swing.UIManager;

/**
 * Separate GUI stuff to promote modularity
 * = LolifierFrame being JFrame instead of
 * Lolifier.
 * @author Allek Mott
 * @since 0.0.3
 **/
public class LolifierFrame extends JFrame {

	/**
	 * Panels for, eventually, GUI... yeah.
	 * @since 0.0.4.4
	 **/
	JPanel mainPanel, leftPanel, rightPanel,
		statusPanel;

	public LolifierFrame() {
		super("Lolifier Disk Troller");
		initGUI();
	}

	void initGUI() {

		setSize(500, 500); // 500px x 500px
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Make it not ugly
		try {
			UIManager.setLookAndFeel(
			UIManager.getSystemLookAndFeelClassName());
    	} catch (Exception e) {}
	}

}