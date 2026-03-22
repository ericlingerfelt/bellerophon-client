/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: HdfDialog.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.dialog;

import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstants;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.bellerophon.gui.util.*;


/**
 * The Class HdfDialog.
 *
 * @author Eric J. Lingerfelt
 */
public class HdfDialog extends JDialog implements ActionListener{

	private JRadioButton dumpButton, saveButton, fileButton;
	private JButton submitButton, cancelButton;
	public int selectedValue;
	public static final int SAVE = 2;
	public static final int DUMP = 1;
	public static final int CANCEL = 0;
	
	/**
	 * Instantiates a new hdf dialog.
	 *
	 * @param owner the owner
	 */
	public HdfDialog(Frame owner){
		super(owner, "Attention!", true);
		
		setSize(545, 230);
		setLocationRelativeTo(owner);

		submitButton = new JButton("Submit");
		submitButton.addActionListener(this);
		
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		
		dumpButton = new JRadioButton("View as HDF5 ASCII Dump", true);
		saveButton = new JRadioButton("Download HDF5 Binary File", false);
		fileButton = new JRadioButton("Convert to Silo and Download", false);
		fileButton.setEnabled(false);
		
		double gap = 10;
		double[] colSelect = {TableLayoutConstants.FILL};
		double[] rowSelect = {TableLayoutConstants.PREFERRED
								, 10, TableLayoutConstants.PREFERRED
								, 10, TableLayoutConstants.PREFERRED};
		JPanel selectPanel = new JPanel(new TableLayout(colSelect, rowSelect));
		selectPanel.add(dumpButton, "0, 0, l, c");
		selectPanel.add(saveButton, "0, 2, l, c");
		selectPanel.add(fileButton, "0, 4, l, c");
		
		ButtonGroup bg = new ButtonGroup();
		bg.add(dumpButton);
		bg.add(saveButton);
		bg.add(fileButton);
		
		WordWrapLabel label = new WordWrapLabel();
		label.setText("You have selected an HDF5 file. Please make a selection below and click <i>Submit</i>.");
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(submitButton);
		buttonPanel.add(cancelButton);

		double[] col = {gap, TableLayoutConstants.FILL, gap};
		double[] row = {gap, TableLayoutConstants.PREFERRED
						, 20, TableLayoutConstants.PREFERRED
						, 20, TableLayoutConstants.PREFERRED, gap};
		
		Container c = getContentPane();
		c.setLayout(new TableLayout(col, row));
		c.add(label, "1, 1, c, c");
		c.add(selectPanel, "1, 3, c, c");
		c.add(buttonPanel, "1, 5, c, c");
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent ae){
		selectedValue = CANCEL;
		if(ae.getSource()==submitButton){
			if(dumpButton.isSelected()){
				selectedValue = DUMP;
			}else{
				selectedValue = SAVE;
			}
		}
		setVisible(false);
	}
	
	/**
	 * Creates the hdf dialog.
	 *
	 * @param owner the owner
	 * @return the int
	 */
	public static int createHdfDialog(Frame owner){
		HdfDialog dialog = new HdfDialog(owner);
		dialog.setVisible(true);
		return dialog.selectedValue;
	}
	
}
