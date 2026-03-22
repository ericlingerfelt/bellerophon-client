/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: ComboToolTipRenderer.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.format;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JList;

/**
 * The Class ComboToolTipRenderer.
 *
 * @author Eric J. Lingerfelt
 */
public class ComboToolTipRenderer extends DefaultListCellRenderer {

	/* (non-Javadoc)
	 * @see javax.swing.DefaultListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
	 */
	public Component getListCellRendererComponent(JList list
													, Object value
													, int index
													, boolean isSelected
													, boolean cellHasFocus){
		JComponent comp = (JComponent)
		super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		if(value != null){
			comp.setToolTipText(String.valueOf(value));
		}else{
			comp.setToolTipText(null);
		}
		return comp;
	}
}
