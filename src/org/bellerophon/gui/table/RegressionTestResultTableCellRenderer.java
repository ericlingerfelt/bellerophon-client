/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: RegressionTestResultTableCellRenderer.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.table;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;
import org.bellerophon.enums.RegressionTestResult;
import org.bellerophon.gui.format.Colors;

/**
 * The Class RegressionTestResultTableCellRenderer.
 *
 * @author Eric J. Lingerfelt
 */
public class RegressionTestResultTableCellRenderer extends JLabel implements TableCellRenderer{

	/**
	 * Instantiates a new regression test result table cell renderer.
	 */
	public RegressionTestResultTableCellRenderer(){
		setOpaque(true);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(JTable table, 
													Object value,
													boolean isSelected, 
													boolean hasFocus,
													int row, 
													int column){

		RegressionTestResult result = (RegressionTestResult)value;
		setText(result.toString());
		setToolTipText(getText());
		setHorizontalAlignment(SwingConstants.LEFT);	
		setBackground(Colors.backColor);
		if(result==RegressionTestResult.COMP_FAILURE
				|| result==RegressionTestResult.EXEC_FAILURE){
			setForeground(Colors.RED);
		}else if(result==RegressionTestResult.UNKNOWN){
			setForeground(Colors.BLUE);
		}else{
			setForeground(Colors.GREEN);
		}
		
		if(isSelected){
			setBackground(new Color(Color.black.getRed(), Color.black.getGreen(), Color.black.getBlue(), 50));
		}
			
		return this;
		
	}
}
