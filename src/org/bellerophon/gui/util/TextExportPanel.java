/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: TextExportPanel.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.util;

import java.awt.*;
import java.awt.event.*;
import info.clearthought.layout.*;
import javax.swing.*;
import org.bellerophon.export.TextSaver;
import org.bellerophon.gui.dialog.ErrorDialog;


/**
 * The Class TextExportPanel.
 *
 * @author Eric J. Lingerfelt
 */
public class TextExportPanel extends JPanel implements ActionListener{

	private JButton saveButton, copyButton, printButton;
	private Frame parent;
	private FileEditorPane area;
	private String filename;
	
	/**
	 * Instantiates a new text export panel.
	 *
	 * @param parent the parent
	 */
	public TextExportPanel(Frame parent){
		this(parent, null);
	}
	
	/**
	 * Instantiates a new text export panel.
	 *
	 * @param parent the parent
	 * @param area the area
	 */
	public TextExportPanel(Frame parent, FileEditorPane area){
		
		this.parent = parent;
		this.area = area;
		
		saveButton = new JButton("Save");
		saveButton.addActionListener(this);
		printButton = new JButton("Print");
		printButton.addActionListener(this);
		copyButton = new JButton("Copy");
		copyButton.addActionListener(this);
		
		double[] col = {TableLayoutConstants.PREFERRED
									, 20, TableLayoutConstants.PREFERRED
									, 20, TableLayoutConstants.PREFERRED};
		double[] row = {TableLayoutConstants.PREFERRED};
		setLayout(new TableLayout(col, row));
		add(saveButton, "0, 0, c, c");
		add(printButton, "2, 0, c, c");
		add(copyButton, "4, 0, c, c");
		
	}
	
	/**
	 * Sets the filename.
	 *
	 * @param filename the new filename
	 */
	public void setFilename(String filename){
		this.filename = filename;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent ae){
		try{
			if(ae.getSource()==saveButton){
				TextSaver.saveText(parent, area.getPlainText(), filename);
			}else if(ae.getSource()==printButton){
				area.print();
			}else if(ae.getSource()==copyButton){
				area.copy();
			}
		}catch(Exception e){
			e.printStackTrace();
			ErrorDialog.createDialog(parent, e.getMessage());
		}
	}
	
}
