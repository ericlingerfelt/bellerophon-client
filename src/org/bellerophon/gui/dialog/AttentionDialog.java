/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: AttentionDialog.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.dialog;

import java.awt.*;
import javax.swing.*;


/**
 * The Class AttentionDialog.
 *
 * @author Eric J. Lingerfelt
 */
public class AttentionDialog extends JDialog{

	/**
	 * Creates the dialog.
	 *
	 * @param owner the owner
	 * @param error the error
	 */
	public static void createDialog(Dialog owner, String error){
		MessageDialog dialog = new MessageDialog(owner, error, "Attention!");
    	dialog.setVisible(true);
	}
	
	/**
	 * Creates the dialog.
	 *
	 * @param owner the owner
	 * @param error the error
	 */
	public static void createDialog(Frame owner, String error){
		MessageDialog dialog = new MessageDialog(owner, error, "Attention!");
    	dialog.setVisible(true);
	}
	
}
