/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: RewindVizSetWorker.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.viz.man.worker;

import java.awt.Frame;

import javax.swing.SwingWorker;

import org.bellerophon.data.util.ErrorResult;
import org.bellerophon.data.util.VizSet;
import org.bellerophon.enums.Action;
import org.bellerophon.exception.CaughtExceptionHandler;
import org.bellerophon.gui.dialog.DelayDialog;
import org.bellerophon.gui.dialog.ErrorResultDialog;
import org.bellerophon.gui.viz.man.VizManSelectPanel;
import org.bellerophon.io.WebServiceCom;

/**
 * The Class RewindVizSetWorker.
 *
 * @author Eric J. Lingerfelt
 */
public class RewindVizSetWorker extends SwingWorker<ErrorResult, Void>{

	private Frame frame;
	private VizManSelectPanel panel;
	private VizSet vs;
	private int newLastFrame;
	private DelayDialog delayDialog;
	
	/**
	 * Instantiates a new gets the viz sets worker.
	 *
	 * @param frame the frame
	 * @param panel the panel
	 * @param dialog the dialog
	 */
	public RewindVizSetWorker(Frame frame, VizManSelectPanel panel, VizSet vs, int newLastFrame){
		this.frame = frame;
		this.panel = panel;
		this.vs = vs;
		this.newLastFrame = newLastFrame;
		
		String string = "Please wait while visualization set number " + vs.getIndex() + ": " + vs.getVizSetId() + " is rewound to frame " + newLastFrame + ".";
		delayDialog = new DelayDialog(frame, string, "Please wait...");
	}

	/* (non-Javadoc)
	 * @see SwingWorker#doInBackground()
	 */
	protected ErrorResult doInBackground(){
		delayDialog.open();
		return WebServiceCom.getInstance().doWebServiceComCall(vs, Action.REWIND_VIZ_SET);
	}
	
	/* (non-Javadoc)
	 * @see SwingWorker#done()
	 */
	protected void done(){
		try{
			ErrorResult result = get();
			if(!result.isError()){
				vs.setLastFrame(newLastFrame);
				vs.setVizJobMap(null);
				panel.updateAfterRewind();
				delayDialog.close();
			}else{
				delayDialog.close();
				ErrorResultDialog.createDialog(frame, result);
			}
		}catch(Exception e){
			delayDialog.close();
			CaughtExceptionHandler.handleException(e, frame);
		}
	}
}
