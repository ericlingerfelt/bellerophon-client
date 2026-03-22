/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: CautionDialog.java
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
 * The Class CautionDialog.
 *
 * @author Eric J. Lingerfelt
 */
public class CautionDialog extends JDialog{
	
	public int selectedValue;
	public static final int YES = 1;
	public static final int NO = 0;
	
	/**
	 * Instantiates a new caution dialog.
	 *
	 * @param owner the owner
	 * @param string the string
	 * @param title the title
	 */
	public CautionDialog(Frame owner, String string, String title){
		super(owner, title, true);
		setSize(320, 215);
		setLocationRelativeTo(owner);
		layoutDialog(string);
	}
	
	/**
	 * Instantiates a new caution dialog.
	 *
	 * @param owner the owner
	 * @param string the string
	 * @param title the title
	 */
	public CautionDialog(Dialog owner, String string, String title){
		super(owner, title, true);
		setSize(320, 215);
		setLocationRelativeTo(owner);
		layoutDialog(string);
	}
	
	/**
	 * Layout dialog.
	 *
	 * @param string the string
	 */
	private void layoutDialog(String string){
		
		double gap = 10;
		double[] col = {gap, TableLayoutConstants.FILL, gap};
		double[] row = {gap, TableLayoutConstants.FILL, 5, TableLayoutConstants.PREFERRED, gap};
		
		Container c = getContentPane();
		c.setLayout(new TableLayout(col, row));
		
		WordWrapLabel textArea = new WordWrapLabel();
		textArea.setText(string);
		textArea.setCaretPosition(0);
		textArea.setBackground(null);
		
		JScrollPane sp = new JScrollPane(textArea
								, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
								, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		JButton yesButton = new JButton("Yes");
		yesButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				selectedValue = 1;
				setVisible(false);
			}
		});
		
		JButton noButton = new JButton("No");
		noButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				selectedValue = 0;
				setVisible(false);
			}
		});
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(yesButton);
		buttonPanel.add(noButton);

		c.add(sp, "1, 1, f, f");
		c.add(buttonPanel, "1, 3, c, c");
	}
	
	/**
	 * Creates the caution dialog.
	 *
	 * @param owner the owner
	 * @param string the string
	 * @param title the title
	 * @return the int
	 */
	public static int createCautionDialog(Frame owner, String string, String title){
		CautionDialog dialog = new CautionDialog(owner, string, title);
		dialog.setVisible(true);
		return dialog.selectedValue;
	}
	
	/**
	 * Creates the caution dialog.
	 *
	 * @param owner the owner
	 * @param string the string
	 * @param title the title
	 * @return the int
	 */
	public static int createCautionDialog(Dialog owner, String string, String title){
		CautionDialog dialog = new CautionDialog(owner, string, title);
		dialog.setVisible(true);
		return dialog.selectedValue;
	}
}
