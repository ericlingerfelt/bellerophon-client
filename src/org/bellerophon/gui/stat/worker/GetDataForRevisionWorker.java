/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: GetDataForRevisionWorker.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.stat.worker;

import java.awt.Cursor;
import java.awt.Frame;

import javax.swing.SwingWorker;

import org.bellerophon.data.util.ErrorResult;
import org.bellerophon.data.util.RevisionLogEntry;
import org.bellerophon.enums.Action;
import org.bellerophon.exception.CaughtExceptionHandler;
import org.bellerophon.gui.dialog.ErrorResultDialog;
import org.bellerophon.gui.stat.StatPanel;
import org.bellerophon.io.WebServiceCom;

/**
 * The Class GetDataForRevisionWorker.
 *
 * @author Eric J. Lingerfelt
 */
public class GetDataForRevisionWorker extends SwingWorker<ErrorResult, Void>{

	private StatPanel panel;
	private Frame frame;
	private RevisionLogEntry rle;
	
	/**
	 * Instantiates a new gets the data for revision worker.
	 *
	 * @param panel the panel
	 * @param frame the frame
	 * @param rle the rle
	 */
	public GetDataForRevisionWorker(StatPanel panel, Frame frame, RevisionLogEntry rle){
		this.panel = panel;
		this.frame = frame;
		this.rle = rle;
	}

	/* (non-Javadoc)
	 * @see SwingWorker#doInBackground()
	 */
	protected ErrorResult doInBackground(){
		frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		return WebServiceCom.getInstance().doWebServiceComCall(rle, Action.GET_DATA_FOR_REVISION);
	}
	
	/* (non-Javadoc)
	 * @see SwingWorker#done()
	 */
	protected void done(){
		try{
			ErrorResult result = get();
			if(!result.isError()){
				panel.setRevString(rle);
				frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}else{
				frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				ErrorResultDialog.createDialog(frame, result);
			}
			
		}catch(Exception e){
			frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			CaughtExceptionHandler.handleException(e, frame);
		}
		
	}
}
