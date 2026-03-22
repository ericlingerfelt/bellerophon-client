/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: DefaultTable.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.table;

import java.util.Calendar;

import javax.swing.*;

import org.bellerophon.data.util.MatplotlibColormap;
import org.bellerophon.data.util.MatplotlibModel;
import org.bellerophon.data.util.Resolution;
import org.bellerophon.data.util.User;
import org.bellerophon.enums.*;

/**
 * The Class DefaultTable.
 *
 * @author Eric J. Lingerfelt
 */
public class DefaultTable extends JTable{
	
	/**
	 * Instantiates a new default table.
	 */
	public DefaultTable(){
		
		getTableHeader().setReorderingAllowed(true);
		getTableHeader().setDefaultRenderer(new HeaderTableCellRenderer(getTableHeader().getDefaultRenderer()));
		setRowHeight(18);
		setGridColor(getTableHeader().getBackground().darker());
		setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		
		setDefaultRenderer(String.class, 				new DefaultTableCellRenderer());
		setDefaultRenderer(Integer.class, 				new DefaultTableCellRenderer());
		setDefaultRenderer(Boolean.class, 				new DefaultTableCellRenderer());
		setDefaultRenderer(Double.class, 				new DefaultTableCellRenderer());
		setDefaultRenderer(MatplotlibModel.class, 		new DefaultTableCellRenderer());
		setDefaultRenderer(MatplotlibColormap.class, 	new DefaultTableCellRenderer());
		setDefaultRenderer(User.class, 					new DefaultTableCellRenderer());
		setDefaultRenderer(Scale.class, 				new DefaultTableCellRenderer());
		setDefaultRenderer(Resolution.class, 			new DefaultTableCellRenderer());
		setDefaultRenderer(CodeType.class, 				new DefaultTableCellRenderer());
		setDefaultRenderer(Platform.class, 				new DefaultTableCellRenderer());
		setDefaultRenderer(Calendar.class, 				new DateTableCellRenderer());
		setDefaultRenderer(RegressionTestResult.class, 	new RegressionTestResultTableCellRenderer());
		
	}

}
