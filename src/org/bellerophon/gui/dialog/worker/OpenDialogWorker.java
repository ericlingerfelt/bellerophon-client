/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: OpenDialogWorker.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.dialog.worker;

import javax.swing.JDialog;
import javax.swing.SwingWorker;


/**
 * The Class OpenDialogWorker.
 *
 * @author Eric J. Lingerfelt
 */
public class OpenDialogWorker extends SwingWorker<Void, Void>{
		
	private JDialog dialog;
	
	/**
	 * Instantiates a new open dialog worker.
	 *
	 * @param dialog the dialog
	 */
	public OpenDialogWorker(JDialog dialog){
		this.dialog = dialog;
	}
	
	/* (non-Javadoc)
	 * @see SwingWorker#doInBackground()
	 */
	protected Void doInBackground(){
		dialog.setVisible(true);
		return null;
	}
}
