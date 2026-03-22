/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: MessageDialog.java
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


/**
 * The Class MessageDialog.
 *
 * @author Eric J. Lingerfelt
 */
public class MessageDialog extends JDialog{

	/**
	 * Instantiates a new message dialog.
	 *
	 * @param owner the owner
	 * @param string the string
	 * @param title the title
	 * @param size the size
	 */
	public MessageDialog(Frame owner, String string, String title, Dimension size){
		super(owner, title, true);
		setSize(size);
		setLocationRelativeTo(owner);
		layoutDialog(string);
	}
	
	/**
	 * Instantiates a new message dialog.
	 *
	 * @param owner the owner
	 * @param string the string
	 * @param title the title
	 */
	public MessageDialog(Frame owner, String string, String title){
		super(owner, title, true);
		setSize(320, 215);
		setLocationRelativeTo(owner);
		layoutDialog(string);
	}
	
	/**
	 * Instantiates a new message dialog.
	 *
	 * @param owner the owner
	 * @param string the string
	 * @param title the title
	 */
	public MessageDialog(Dialog owner, String string, String title){
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
		
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				setVisible(false);
				dispose();
			}
		});
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(okButton);
		
		c.add(sp, "1, 1, f, f");
		c.add(buttonPanel, "1, 3, c, c");
	}

	/**
	 * Creates the message dialog.
	 *
	 * @param owner the owner
	 * @param string the string
	 * @param title the title
	 * @param size the size
	 */
	public static void createMessageDialog(Frame owner, String string, String title, Dimension size){
		MessageDialog dialog = new MessageDialog(owner, string, title, size);
		dialog.setVisible(true);
	}
	
	/**
	 * Creates the message dialog.
	 *
	 * @param owner the owner
	 * @param string the string
	 * @param title the title
	 */
	public static void createMessageDialog(Frame owner, String string, String title){
		MessageDialog dialog = new MessageDialog(owner, string, title);
		dialog.setVisible(true);
	}
	
	/**
	 * Creates the message dialog.
	 *
	 * @param owner the owner
	 * @param string the string
	 * @param title the title
	 */
	public static void createMessageDialog(Dialog owner, String string, String title){
		MessageDialog dialog = new MessageDialog(owner, string, title);
		dialog.setVisible(true);
	}
	
}
