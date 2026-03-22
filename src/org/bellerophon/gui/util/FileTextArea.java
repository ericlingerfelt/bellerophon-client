/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: FileTextArea.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.util;

import org.bellerophon.export.PrintableTextArea;
import org.bellerophon.gui.format.Fonts;


/**
 * The Class FileTextArea.
 *
 * @author Eric J. Lingerfelt
 */
public class FileTextArea extends PrintableTextArea{

	/**
	 * Instantiates a new file text area.
	 */
	public FileTextArea(){
		super();
		setEditable(false);
		setFont(Fonts.textFontFixedWidth);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.text.JTextComponent#setText(java.lang.String)
	 */
	public void setText(String text){
		super.setText(text);
		setCaretPosition(0);
	}
	
	/**
	 * Copy all text.
	 */
	public void copyAllText(){
		setSelectionStart(0);
		setSelectionEnd(getDocument().getLength());
		super.copy();
		setCaretPosition(0);
	}
	
}
