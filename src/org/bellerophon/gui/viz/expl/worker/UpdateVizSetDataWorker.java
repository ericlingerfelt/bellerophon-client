/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: UpdateVizSetDataWorker.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.viz.expl.worker;

import java.awt.Frame;

import javax.swing.SwingWorker;

import org.bellerophon.data.util.ErrorResult;
import org.bellerophon.enums.Action;
import org.bellerophon.exception.CaughtExceptionHandler;
import org.bellerophon.gui.dialog.ErrorResultDialog;
import org.bellerophon.gui.viz.expl.VizExplPanel;
import org.bellerophon.io.WebServiceCom;

/**
 * The Class UpdateVizSetDataWorker.
 *
 * @author Eric J. Lingerfelt
 */
public class UpdateVizSetDataWorker extends SwingWorker<ErrorResult, Void>{

	private Frame frame;
	private VizExplPanel panel;
	
	/**
	 * Instantiates a new gets the animations and viz jobs worker.
	 *
	 * @param frame the frame
	 * @param panel the panel
	 */
	public UpdateVizSetDataWorker(Frame frame, VizExplPanel panel){
		this.frame = frame;
		this.panel = panel;
	}

	/* (non-Javadoc)
	 * @see SwingWorker#doInBackground()
	 */
	protected ErrorResult doInBackground(){
		ErrorResult result = new ErrorResult();
		result = WebServiceCom.getInstance().doWebServiceComCall(panel.getData(), Action.GET_VIZ_SETS);
		if(result.isError()){
			return result;
		}
		result = WebServiceCom.getInstance().doWebServiceComCall(panel.getData(), Action.GET_VIZ_JOBS);
		if(result.isError()){
			return result;
		}
		result = WebServiceCom.getInstance().doWebServiceComCall(panel.getData(), Action.GET_MATPLOTLIB_ANIMATIONS);
		if(result.isError()){
			return result;
		}
		return WebServiceCom.getInstance().doWebServiceComCall(panel.getData(), Action.GET_UPLOADED_ANIMATIONS);
	}
	
	/* (non-Javadoc)
	 * @see SwingWorker#done()
	 */
	protected void done(){
		try{
			ErrorResult result = get();
			if(!result.isError()){
				panel.updateComponents();
			}else{
				ErrorResultDialog.createDialog(frame, result);
			}
		}catch(Exception e){
			CaughtExceptionHandler.handleException(e, frame);
		}
	}
}
