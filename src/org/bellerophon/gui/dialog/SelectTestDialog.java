/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: SelectTestDialog.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.dialog;

import info.clearthought.layout.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Iterator;
import java.util.TreeMap;

import javax.swing.*;

import org.bellerophon.data.util.RegressionTest;
import org.bellerophon.gui.format.Calendars;
import org.bellerophon.gui.util.*;


/**
 * The Class SelectTestDialog.
 *
 * @author Eric J. Lingerfelt
 */
public class SelectTestDialog extends JDialog implements ActionListener{

	private JButton submitButton, cancelButton;
	private JComboBox testBox;
	public RegressionTest selectedTest;
	private JTextArea area;
	
	/**
	 * Instantiates a new select test dialog.
	 *
	 * @param owner the owner
	 * @param map the map
	 */
	public SelectTestDialog(Frame owner, TreeMap<Integer, RegressionTest> map){
		super(owner, "Select Regression Test", true);
		
		setSize(590, 280);
		setLocationRelativeTo(owner);

		submitButton = new JButton("Submit");
		submitButton.addActionListener(this);
		
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		
		WordWrapLabel topLabel = new WordWrapLabel();
		topLabel.setText("Please select a regression test from the dropdown menu below.");
		
		JLabel testLabel = new JLabel("Selected Regression Test");
		
		area = new JTextArea();
		area.setWrapStyleWord(true);
		area.setLineWrap(true);
		JScrollPane pane = new JScrollPane(area);
		
		testBox = new JComboBox();
		Iterator<RegressionTest> itr = map.values().iterator();
		while(itr.hasNext()){
			testBox.addItem(itr.next());
		}
		testBox.addActionListener(this);
		testBox.setSelectedIndex(0);
		
		double gap = 10;
		double[] colSelect = {TableLayoutConstants.PREFERRED, 10, TableLayoutConstants.FILL};
		double[] rowSelect = {TableLayoutConstants.PREFERRED, 10, TableLayoutConstants.FILL};
		JPanel selectPanel = new JPanel(new TableLayout(colSelect, rowSelect));
		selectPanel.add(testLabel, "0, 0, r, c");
		selectPanel.add(testBox, "2, 0, f, c");
		selectPanel.add(pane, "0, 2, 2, 2, f, f");
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(submitButton);
		buttonPanel.add(cancelButton);

		double[] col = {gap, TableLayoutConstants.FILL, gap};
		double[] row = {gap, TableLayoutConstants.PREFERRED
						, 10, TableLayoutConstants.FILL
						, 10, TableLayoutConstants.PREFERRED, gap};
		
		Container c = getContentPane();
		c.setLayout(new TableLayout(col, row));
		c.add(topLabel, "1, 1, c, c");
		c.add(selectPanel, "1, 3, f, f");
		c.add(buttonPanel, "1, 5, c, c");
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent ae){
		if(ae.getSource()==submitButton){
			selectedTest = (RegressionTest)testBox.getSelectedItem();
			setVisible(false);
		}else if(ae.getSource()==cancelButton){
			selectedTest = null;
			setVisible(false);
		}else if(ae.getSource()==testBox){
			area.setText(getAreaText((RegressionTest)testBox.getSelectedItem()));
		}
	}
	
	/**
	 * Gets the area text.
	 *
	 * @param rt the rt
	 * @return the area text
	 */
	private String getAreaText(RegressionTest rt){
		String string = "";
		string += "Regression Test Index = " + rt.getIndex() + "\n";
		string += "Revision = " + rt.getRevision() + "\n";
		string += "Start Date = " + Calendars.getDateString(rt.getCheckoutDate()) + "\n";
		string += "Platform = " + rt.getPlatform() + "\n";
		string += "Result = " + rt.getResult();
		return string;
	}
	
	/**
	 * Creates the select test dialog.
	 *
	 * @param owner the owner
	 * @param map the map
	 * @return the regression test
	 */
	public static RegressionTest createSelectTestDialog(Frame owner, TreeMap<Integer, RegressionTest> map){
		SelectTestDialog dialog = new SelectTestDialog(owner, map);
		dialog.setVisible(true);
		return dialog.selectedTest;
	}
	
}

