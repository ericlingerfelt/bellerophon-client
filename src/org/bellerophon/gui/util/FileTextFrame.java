/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: FileTextFrame.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.util;

import java.awt.*;
import info.clearthought.layout.*;
import javax.swing.*;


/**
 * The Class FileTextFrame.
 *
 * @author Eric J. Lingerfelt
 */
public class FileTextFrame extends JFrame{

	private TextExportPanel buttonPanel;
	private FileTextArea area;
	
	/**
	 * Instantiates a new file text frame.
	 */
	public FileTextFrame(){
		
		setSize(900, 600);
		
		Container c = getContentPane();

		area = new FileTextArea();
		JScrollPane areaPane = new JScrollPane(area);
		
		//buttonPanel = new TextExportPanel(this, area);

		double[] col = {10, TableLayoutConstants.FILL, 10};
		double[] row = {10, TableLayoutConstants.FILL
						, 10, TableLayoutConstants.PREFERRED, 10};
		
		c.setLayout(new TableLayout(col, row));
		c.add(areaPane, "1, 1, f, f");
		//c.add(buttonPanel, "1, 3, c, c");
		
	}
	
	/**
	 * Sets the current state.
	 *
	 * @param contents the contents
	 * @param title the title
	 * @param filename the filename
	 */
	public void setCurrentState(String contents, String title, String filename){
		setTitle(title);
		//buttonPanel.setFilename(filename);
		area.setText(contents);
	}
	
}
