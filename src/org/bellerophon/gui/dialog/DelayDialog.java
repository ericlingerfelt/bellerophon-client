/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: DelayDialog.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.dialog;

import info.clearthought.layout.*;
import java.awt.*;
import javax.swing.*;

import org.bellerophon.gui.dialog.worker.OpenDialogWorker;
import org.bellerophon.gui.util.*;

/**
 * The Class DelayDialog.
 *
 * @author Eric J. Lingerfelt
 */
public class DelayDialog extends JDialog{
	
	private Frame frame;
	private Dialog dialog;
	
	/**
	 * Instantiates a new delay dialog.
	 *
	 * @param owner the owner
	 * @param string the string
	 * @param title the title
	 */
	public DelayDialog(Frame owner, String string, String title){
		super(owner, title, true);
		this.frame = owner;
		setSize(325, 200);
		layoutDialog(string);
	}
	
	/**
	 * Instantiates a new delay dialog.
	 *
	 * @param owner the owner
	 * @param string the string
	 * @param title the title
	 */
	public DelayDialog(Dialog owner, String string, String title){
		super(owner, title, true);
		this.dialog = owner;
		setSize(325, 200);
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
		
		JScrollPane sp = new JScrollPane(textArea
								, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
								, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		c.add(sp, "1, 1, f, f");
	}
	
	/**
	 * Open.
	 */
	public void open(){
		if(frame!=null){
			setLocationRelativeTo(frame);
		}else if(dialog!=null){
			setLocationRelativeTo(dialog);
		}
		OpenDialogWorker task = new OpenDialogWorker(this);
		task.execute();
	}
	
	/**
	 * Close.
	 */
	public void close(){
		setVisible(false);
		dispose();
	}
}