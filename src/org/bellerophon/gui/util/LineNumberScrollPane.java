/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: LineNumberScrollPane.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.util;

import javax.swing.JScrollPane;
import javax.swing.text.JTextComponent;


/**
 * The Class LineNumberScrollPane.
 *
 * @author Eric J. Lingerfelt
 */
public class LineNumberScrollPane extends JScrollPane{

	private JTextComponent comp;
	
	/**
	 * Instantiates a new line number scroll pane.
	 *
	 * @param comp the comp
	 */
	public LineNumberScrollPane(JTextComponent comp){
		super(comp);
		this.comp = comp;
		setRowHeaderView(new LineNumberPanel(comp));
	}  
	
	/**
	 * Show line numbers.
	 *
	 * @param showLineNumbers the show line numbers
	 */
	public void showLineNumbers(boolean showLineNumbers){
		if(showLineNumbers){
			setRowHeaderView(new LineNumberPanel(comp));
		}else{
			setRowHeaderView(null);
		}
	}
	
}
