/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: ErrorResultDialog.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.dialog;

import java.awt.*;
import javax.swing.*;
import org.bellerophon.data.util.*;


/**
 * The Class ErrorResultDialog.
 *
 * @author Eric J. Lingerfelt
 */
public class ErrorResultDialog extends JDialog{
	
	/**
	 * Creates the dialog.
	 *
	 * @param owner the owner
	 * @param result the result
	 */
	public static void createDialog(Frame owner, ErrorResult result){
		MessageDialog dialog = new MessageDialog(owner, result.getString(), "Error!");
    	dialog.setVisible(true);
	}
	
	/**
	 * Creates the dialog.
	 *
	 * @param owner the owner
	 * @param result the result
	 */
	public static void createDialog(Dialog owner, ErrorResult result){
		MessageDialog dialog = new MessageDialog(owner, result.getString(), "Error!");
    	dialog.setVisible(true);
	}
	
}


