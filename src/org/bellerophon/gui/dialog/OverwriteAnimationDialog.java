/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: OverwriteAnimationDialog.java
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

import org.bellerophon.gui.util.WordWrapLabel;

/**
 * The Class OverwriteAnimationDialog.
 *
 * @author Eric J. Lingerfelt
 */
public class OverwriteAnimationDialog extends JDialog{
	
	public int selectedValue;
	public static final int CANCEL = 0;
	public static final int OVERWRITE = 1;
	public static final int CREATE_NEW = 2;
	
	/**
	 * Instantiates a new overwrite animation dialog.
	 *
	 * @param owner the owner
	 */
	public OverwriteAnimationDialog(Dialog owner){
		super(owner, "Attention!", true);
		setSize(500, 150);
		setLocationRelativeTo(owner);
		double gap = 10;
		double[] col = {gap, TableLayoutConstants.FILL, gap};
		double[] row = {gap, TableLayoutConstants.FILL, 5, TableLayoutConstants.PREFERRED, gap};
		
		Container c = getContentPane();
		c.setLayout(new TableLayout(col, row));
		
		String string = "You have modified this animation's parameters. Do you want to replace " +
							"this animation's parameters with the current selections " +
							"or create a new animation with these parameters?";
		
		WordWrapLabel textArea = new WordWrapLabel();
		textArea.setText(string);
		
		JScrollPane sp = new JScrollPane(textArea
								, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
								, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		sp.setBorder(null);
		
		JButton overwriteButton = new JButton("Overwrite Animation");
		overwriteButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				selectedValue = 1;
				setVisible(false);
			}
		});
		
		JButton createButton = new JButton("Create New Animation");
		createButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				selectedValue = 2;
				setVisible(false);
			}
		});
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				selectedValue = 0;
				setVisible(false);
			}
		});
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(overwriteButton);
		buttonPanel.add(createButton);
		buttonPanel.add(cancelButton);

		c.add(sp, "1, 1, f, f");
		c.add(buttonPanel, "1, 3, c, c");
	}
	
	/**
	 * Creates the overwrite animation dialog.
	 *
	 * @param owner the owner
	 * @return the int
	 */
	public static int createOverwriteAnimationDialog(Dialog owner){
		OverwriteAnimationDialog dialog = new OverwriteAnimationDialog(owner);
		dialog.setVisible(true);
		return dialog.selectedValue;
	}
}

