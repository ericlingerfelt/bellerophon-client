/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: HeaderTableCellRenderer.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.table;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 * The Class HeaderTableCellRenderer.
 *
 * @author Eric J. Lingerfelt
 */
public class HeaderTableCellRenderer extends JLabel implements TableCellRenderer{

	private TableCellRenderer tableCellRenderer;

	/**
	 * Instantiates a new header table cell renderer.
	 *
	 * @param tableCellRenderer the table cell renderer
	 */
	public HeaderTableCellRenderer(TableCellRenderer tableCellRenderer){
		this.tableCellRenderer = tableCellRenderer;
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

		Component c = tableCellRenderer.getTableCellRendererComponent(table, 
										value, isSelected, hasFocus, row, column);

		if(c instanceof JLabel){
			JLabel l = (JLabel)c;
			l.setHorizontalAlignment(SwingConstants.CENTER);	
			l.setToolTipText(l.getText());
		}
		
		return c;
		
	}
	
}