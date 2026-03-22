/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: ErrorDialog.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.dialog;

import java.awt.*;
import javax.swing.*;


/**
 * The Class ErrorDialog.
 *
 * @author Eric J. Lingerfelt
 */
public class ErrorDialog extends JDialog{

	/**
	 * Creates the dialog.
	 *
	 * @param owner the owner
	 * @param error the error
	 */
	public static void createDialog(Dialog owner, String error){
		MessageDialog dialog = new MessageDialog(owner, error, "Error!");
    	dialog.setVisible(true);
	}
	
	/**
	 * Creates the dialog.
	 *
	 * @param owner the owner
	 * @param error the error
	 */
	public static void createDialog(Frame owner, String error){
		MessageDialog dialog = new MessageDialog(owner, error, "Error!");
    	dialog.setVisible(true);
	}
	
}