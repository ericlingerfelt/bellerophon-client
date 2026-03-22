/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: DateTableCellRenderer.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.table;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;

import org.bellerophon.gui.format.*;

/**
 * The Class DateTableCellRenderer.
 *
 * @author Eric J. Lingerfelt
 */
public class DateTableCellRenderer extends JLabel implements TableCellRenderer{

	/**
	 * Instantiates a new date table cell renderer.
	 */
	public DateTableCellRenderer(){
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

		Calendar c = (Calendar)value;
		if(c!=null){
			setText(Calendars.getFormattedOutputDateString(c));
		}
		setToolTipText(getText());
		setHorizontalAlignment(SwingConstants.RIGHT);	
		setBackground(Colors.backColor);
		setForeground(Colors.frontColor);

		if(isSelected){
			setBackground(new Color(Color.black.getRed(), Color.black.getGreen(), Color.black.getBlue(), 50));
			setForeground(Color.BLACK);
		}
			
		return this;
		
	}
	
}
