/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: WordWrapLabel.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.util;

import javax.swing.*;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;


/**
 * The Class WordWrapLabel.
 *
 * @author Eric J. Lingerfelt
 */
public class WordWrapLabel extends JEditorPane{

	private boolean isBold;
	
	/**
	 * Instantiates a new word wrap label.
	 */
	public WordWrapLabel(){
		setEditable(false);
		setBorder(null);
		setEditorKit(new HTMLEditorKit());
		setBackground(null);
	}
	
	/**
	 * Instantiates a new word wrap label.
	 *
	 * @param isBold the is bold
	 */
	public WordWrapLabel(boolean isBold){
		this();
		this.isBold = isBold;
	}
	
	/**
	 * Sets the text.
	 *
	 * @param text the text
	 * @param foreground the foreground
	 */
	public void setText(String text, Color foreground){
		super.setText(text);
		if(isBold){
			super.setText(getText().replaceAll("<body>", "<body><font face=\"sans-serif\" size=\"3\" color=\"#FF0000\"><b>"));
		}else{
			super.setText(getText().replaceAll("<body>", "<body><font face=\"sans-serif\" size=\"3\" color=\"#FF0000\">"));
		}
		if(text.indexOf("<td>")!=-1){
			super.setText(getText().replaceAll("<td>", "<td><font face=\"sans-serif\" size=\"3\" color=\"#FF0000\">"));
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JEditorPane#setText(java.lang.String)
	 */
	public void setText(String text){
		super.setText(text);
		if(isBold){
			super.setText(getText().replaceAll("<body>", "<body><font face=\"sans-serif\" size=\"3\"><b>"));
		}else{
			super.setText(getText().replaceAll("<body>", "<body><font face=\"sans-serif\" size=\"3\">"));
		}
		if(text!=null && text.indexOf("<td>")!=-1){
			super.setText(getText().replaceAll("<td>", "<td><font face=\"sans-serif\" size=\"3\">"));
		}
	}
	
}
