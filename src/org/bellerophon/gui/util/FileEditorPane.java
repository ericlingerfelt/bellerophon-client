/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: FileEditorPane.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.util;

import java.awt.Dimension;
import java.awt.print.PrinterException;

import javax.swing.JTextArea;
import javax.swing.event.HyperlinkListener;
import org.bellerophon.export.*;
import org.bellerophon.gui.format.Fonts;


/**
 * The Class FileEditorPane.
 *
 * @author Eric J. Lingerfelt
 */
public class FileEditorPane extends PrintableEditorPane{
	
	private String plainText;
	
	/**
	 * Instantiates a new file editor pane.
	 *
	 * @param hl the hl
	 */
	public FileEditorPane(HyperlinkListener hl){
		super();
		setEditable(false);
		setFont(Fonts.textFontFixedWidth);
		addHyperlinkListener(hl);
	}
	
	/**
	 * Sets the line wrap.
	 *
	 * @param flag the flag
	 * @param d the d
	 */
	public void setLineWrap(boolean flag, Dimension d){
		
	}

	/**
	 * Gets the plain text.
	 *
	 * @return the plain text
	 */
	public String getPlainText(){return plainText;}
	
	/* (non-Javadoc)
	 * @see org.bellerophon.gui.html.FormattedHTMLEditorPane#setText(java.lang.String)
	 */
	public void setText(String text){
		plainText = text;
		String string = text;
		string = string.replaceAll("\n", "<br>");
		string = insertLinks(string);
		string = "<font size=\"3\" face=\"Courier\">" + string + "</font>";
		super.setText(string);
		setCaretPosition(0);
	}
	
	/**
	 * Insert links.
	 *
	 * @param string the string
	 * @return the string
	 */
	private String insertLinks(String string){
		String newString = "";
		String[] array = string.split("<br>");
		for(String row: array){
			String address = row.split(" ")[0];
			String line = row.split(" ")[1];
			if(line.indexOf("../../")!=-1){
				line = "<a href=\""
					+ line
					+ "\">" 
					+ line
					+ "</a>";
			}
			newString += address + " " + line + "<br>";
		}
		return newString;
	}
	
	/**
	 * Copy all text.
	 */
	public void copyAllText(){
		JTextArea area = new JTextArea();
		area.setText(plainText);
		area.setSelectionStart(0);
		area.setSelectionEnd(getDocument().getLength());
		area.copy();
	}
	
	/* (non-Javadoc)
	 * @see org.bellerophon.export.PrintableEditorPane#print()
	 */
	public boolean print(){
		JTextArea area = new JTextArea();
		area.setLineWrap(true);
		area.setWrapStyleWord(true);
		area.setText(plainText);
		area.setSelectionStart(0);
		area.setSelectionEnd(getDocument().getLength());
		boolean flag = false;
		try{
			flag = area.print();
		}catch(PrinterException pe){
			return false;
		}
		return true;
	}
	
	
}
