/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: RewindDialog.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.dialog;

import info.clearthought.layout.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import org.bellerophon.data.util.VizSet;
import org.bellerophon.gui.util.WordWrapLabel;


/**
 * The Class RewindDialog.
 *
 * @author Eric J. Lingerfelt
 */
public class RewindDialog extends JDialog{
	
	public int selectedValue;
	private JTextField frameField; 
	private VizSet vs;
	
	/**
	 * Instantiates a new caution dialog.
	 *
	 * @param owner the owner
	 * @param string the string
	 * @param title the title
	 */
	public RewindDialog(Frame owner, VizSet vs){
		
		super(owner, "Rewind Selected Visualization Set", true);
		setSize(400, 200);
		setLocationRelativeTo(owner);
		
		this.vs = vs;
		
		WordWrapLabel textArea = new WordWrapLabel();
		textArea.setText("Please enter the value below for the new last frame for Visualization Set #" + vs.getIndex() + ": " + vs.getVizSetId() + ".");
		textArea.setCaretPosition(0);
		textArea.setBackground(null);
		
		JScrollPane sp = new JScrollPane(textArea
								, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
								, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		JButton yesButton = new JButton("Submit");
		yesButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				if(goodFrame()){
					selectedValue = Integer.valueOf(frameField.getText());
					setVisible(false);
				}
			}
		});
		
		JButton noButton = new JButton("Cancel");
		noButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				selectedValue = -1;
				setVisible(false);
			}
		});
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(yesButton);
		buttonPanel.add(noButton);

		JLabel frameLabel = new JLabel("<html>Enter New Last Frame<br>(must be less than " + vs.getLastFrame() + ")</html>");
		frameField = new JTextField(30);
		
		double[] colFrame = {TableLayoutConstants.PREFERRED
								, 10, TableLayoutConstants.FILL};
		double[] rowFrame = {TableLayoutConstants.PREFERRED};
		JPanel framePanel = new JPanel(new TableLayout(colFrame, rowFrame));
		framePanel.add(frameLabel, "0, 0, r, c");
		framePanel.add(frameField, "2, 0, f, c");
		
		double gap = 20;
		double[] col = {gap, TableLayoutConstants.FILL, gap};
		double[] row = {gap, TableLayoutConstants.PREFERRED
						, 20, TableLayoutConstants.PREFERRED
						, 20, TableLayoutConstants.PREFERRED, gap};
		
		Container c = getContentPane();
		c.setLayout(new TableLayout(col, row));
		
		c.add(sp, 			"1, 1, c, f");
		c.add(framePanel, 	"1, 3, c, c");
		c.add(buttonPanel, 	"1, 5, c, c");
		
	}
	
	private boolean goodFrame(){
		String s = frameField.getText();
		int lastFrame = vs.getLastFrame();
		String error = "Please enter an integer between 0 and " + (lastFrame-1) + ".";
		try{
			int frame = Integer.valueOf(s);
			if(frame<0 || frame>=lastFrame){
				ErrorDialog.createDialog(this, error);
				return false;
			}
		}catch(NumberFormatException nfe){
			ErrorDialog.createDialog(this, error);
			return false;
		}
		return true;
	}
	
	public static int createRewindDialog(Frame owner, VizSet vs){
		RewindDialog dialog = new RewindDialog(owner, vs);
		dialog.setVisible(true);
		return dialog.selectedValue;
	}
	
}
