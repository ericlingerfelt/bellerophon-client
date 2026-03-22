/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: AddProgenitorDialog.java
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
import org.bellerophon.gui.util.*;

public class AddProgenitorDialog extends JDialog{
	
	public String title = "Add Progenitor";
	public String selectedValue;
	public JTextField progenitorField;
	
	public AddProgenitorDialog(Frame owner){
		super(owner, "Add Progenitor", true);
		setSize(400, 150);
		setLocationRelativeTo(owner);
		layoutDialog();
	}
	
	public AddProgenitorDialog(Dialog owner){
		super(owner, "Add Progenitor", true);
		setSize(400, 150);
		setLocationRelativeTo(owner);
		layoutDialog();
	}
	
	/**
	 * Layout dialog.
	 *
	 * @param string the string
	 */
	private void layoutDialog(){
		
		double gap = 10;
		double[] col = {gap, TableLayoutConstants.FILL, gap};
		double[] row = {gap, TableLayoutConstants.FILL, 
							5, TableLayoutConstants.FILL,
							5, TableLayoutConstants.PREFERRED, gap};
		
		Container c = getContentPane();
		c.setLayout(new TableLayout(col, row));
		
		WordWrapLabel label = new WordWrapLabel();
		label.setText("Please enter a new progenitor in the field below.");
		label.setBackground(null);
		
		progenitorField = new JTextField();
		
		JButton submitButton = new JButton("Submit");
		submitButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				selectedValue = progenitorField.getText();
				setVisible(false);
			}
		});
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				selectedValue = "";
				setVisible(false);
			}
		});
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(submitButton);
		buttonPanel.add(cancelButton);
		
		c.add(label, 		"1, 1, f, c");
		c.add(progenitorField, 	"1, 3, f, c");
		c.add(buttonPanel, 	"1, 5, c, c");
	}
	
	/**
	 * Creates the message dialog.
	 *
	 * @param owner the owner
	 * @param string the string
	 * @param title the title
	 */
	public static String createAddProgenitorDialog(Frame owner){
		AddProgenitorDialog dialog = new AddProgenitorDialog(owner);
		dialog.setVisible(true);
		return dialog.selectedValue;
	}
	
	/**
	 * Creates the message dialog.
	 *
	 * @param owner the owner
	 * @param string the string
	 * @param title the title
	 */
	public static String createAddProgenitorDialog(Dialog owner){
		AddProgenitorDialog dialog = new AddProgenitorDialog(owner);
		dialog.setVisible(true);
		return dialog.selectedValue;
	}
	
}