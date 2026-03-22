/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: IntroPanel.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui;

import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstants;
import javax.swing.*;
import org.bellerophon.io.FileImport;

/**
 * The Class IntroPanel.
 *
 * @author Eric J. Lingerfelt
 */
public class IntroPanel extends JPanel{

	/**
	 * Instantiates a new intro panel.
	 */
	public IntroPanel(){
		
		JLabel label1 = new JLabel();
		try{
			label1.setIcon(new ImageIcon(FileImport.getFileByte("images/bellerophon_white_new.png")));
		}catch(Exception e){
			
		}
		
		JLabel label2 = new JLabel();
		try{
			label2.setIcon(new ImageIcon(FileImport.getFileByte("images/ORNL_logo.png")));
		}catch(Exception e){
			
		}
		
		JLabel label3 = new JLabel();
		try{
			label3.setIcon(new ImageIcon(FileImport.getFileByte("images/PANDIA_logo.png")));
		}catch(Exception e){
			
		}
		
		double[] col = {TableLayoutConstants.FILL, 550, TableLayoutConstants.FILL};
		double[] row = {TableLayoutConstants.PREFERRED, 225, TableLayoutConstants.FILL};
		
		setLayout(new TableLayout(col, row));
		add(label1, 	"0, 0, 2, 0, c, c");
		add(label2, 	"0, 2, l, c");
		add(label3, 	"2, 2, r, c");
		
	}
	
}
