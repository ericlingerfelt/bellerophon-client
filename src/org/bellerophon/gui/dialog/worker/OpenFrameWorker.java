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

import javax.swing.JFrame;
import javax.swing.SwingWorker;


/**
 * The Class OpenDialogWorker.
 *
 * @author Eric J. Lingerfelt
 */
public class OpenFrameWorker extends SwingWorker<Void, Void>{
		
	private JFrame frame;
	
	/**
	 * Instantiates a new open dialog worker.
	 *
	 * @param dialog the dialog
	 */
	public OpenFrameWorker(JFrame frame){
		this.frame = frame;
	}
	
	/* (non-Javadoc)
	 * @see SwingWorker#doInBackground()
	 */
	protected Void doInBackground(){
		frame.setVisible(true);
		return null;
	}
}
