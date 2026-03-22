/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: BackgroundMessageDialog.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.dialog;

import info.clearthought.layout.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.bellerophon.gui.util.*;

/**
 * The Class BackgroundDelayDialog.
 *
 * @author Eric J. Lingerfelt
 */
public class BackgroundMessageDialog extends JFrame implements ActionListener{
	
	private Frame owner;
	private JButton minimizeButton;
	
	/**
	 * Instantiates a new delay dialog.
	 *
	 * @param owner the owner
	 * @param string the string
	 * @param title the title
	 */
	public BackgroundMessageDialog(Frame owner, String string, String title){

		this.owner = owner;
		setSize(635, 155);
		setTitle(title);
		
		WordWrapLabel textArea = new WordWrapLabel();
		textArea.setText(string);
		textArea.setCaretPosition(0);
		textArea.setBackground(null);
		
		minimizeButton = new JButton("Minimize");
		minimizeButton.addActionListener(this);
		
		JScrollPane sp = new JScrollPane(textArea
								, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
								, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		Container c = this.getContentPane();
		double[] col = {10, TableLayoutConstants.FILL, 10};
		double[] row = {10, TableLayoutConstants.FILL
						, 10, TableLayoutConstants.PREFERRED, 10};
		c.setLayout(new TableLayout(col, row));
		c.add(sp, "1, 1, f, c");
		c.add(minimizeButton, "1, 3, c, c");
	}
	
	public void actionPerformed(ActionEvent ae){
		if(ae.getSource()==minimizeButton){
			setState(java.awt.Frame.ICONIFIED);
		}
	}
	
	/**
	 * Open.
	 */
	public void open(){
		setLocationRelativeTo(owner);
		setVisible(true);
	}
	
	/**
	 * Close.
	 */
	public void close(){
		setVisible(false);
		dispose();
	}
}