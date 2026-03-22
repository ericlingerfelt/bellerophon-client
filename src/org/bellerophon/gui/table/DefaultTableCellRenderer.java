/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: DefaultTableCellRenderer.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.table;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * The Class DefaultTableCellRenderer.
 *
 * @author Eric J. Lingerfelt
 */
public class DefaultTableCellRenderer extends JLabel implements TableCellRenderer{
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(JTable table, 
													Object value,
													boolean isSelected, 
													boolean hasFocus,
													int row, 
													int column){
		JTable tempTable = new JTable();
		if(value instanceof Boolean){
			Component c = tempTable.getDefaultRenderer(Boolean.class).getTableCellRendererComponent(table, 
							value, 
							isSelected, 
							hasFocus, 
							row, 
							column);
			JCheckBox b = (JCheckBox)c;
			if(isSelected){
				b.setBackground(new Color(Color.black.getRed(), Color.black.getGreen(), Color.black.getBlue(), 50));
				b.setForeground(Color.BLACK);
			}
			b.setBorder(null);
			return c;
		}
		
		Component c = tempTable.getDefaultRenderer(String.class).getTableCellRendererComponent(table, 
																							value, 
																							isSelected, 
																							hasFocus, 
																							row, 
																							column);
		if(c instanceof JLabel){
			JLabel l = (JLabel)c;
			l.setToolTipText(l.getText());
			if(isSelected){
				l.setBackground(new Color(Color.black.getRed(), Color.black.getGreen(), Color.black.getBlue(), 50));
				l.setForeground(Color.BLACK);
			}
			l.setBorder(null);
		}
		return c;

	}
}
