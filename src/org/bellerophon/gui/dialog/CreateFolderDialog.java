/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: CreateFolderDialog.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.dialog;

import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstants;
import java.awt.Container;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;


/**
 * The Class CreateFolderDialog.
 *
 * @author Eric J. Lingerfelt
 */
class CreateFolderDialog extends JDialog{

	private JButton createButton;
	private JTextField field;
	
	/**
	 * Instantiates a new creates the folder dialog.
	 *
	 * @param parent the parent
	 * @param al the al
	 */
	public CreateFolderDialog(JDialog parent, ActionListener al){
		
		super(parent, "Create New Directory", true);
		
		Container c = getContentPane();
		setSize(350, 150);
		setLocationRelativeTo(parent);
		
		double gap = 20;
		double[] column = {gap, TableLayoutConstants.FILL, gap};
		double[] row = {gap, TableLayoutConstants.PREFERRED
							, 10, TableLayoutConstants.PREFERRED
							, 10, TableLayoutConstants.PREFERRED, gap};
		c.setLayout(new TableLayout(column, row));
		
		JLabel topLabel = new JLabel("Please enter new directory name below.");
		
		createButton = new JButton("Create New Directory");
		createButton.addActionListener(al);
		
		field = new JTextField();
		
		c.add(topLabel, "1, 1, c, c");
		c.add(field, "1, 3, f, c");
		c.add(createButton, "1, 5, c, c");
		
	}
	
	/**
	 * Gets the creates the button.
	 *
	 * @return the creates the button
	 */
	public JButton getCreateButton(){
		return createButton;
	}
	
	/**
	 * Gets the field.
	 *
	 * @return the field
	 */
	public JTextField getField(){
		return field;
	}
	
}