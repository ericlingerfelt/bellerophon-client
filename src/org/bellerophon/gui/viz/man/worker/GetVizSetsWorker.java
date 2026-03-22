/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: GetVizSetsWorker.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.viz.man.worker;

import java.awt.Frame;

import javax.swing.SwingWorker;

import org.bellerophon.data.util.ErrorResult;
import org.bellerophon.enums.Action;
import org.bellerophon.exception.CaughtExceptionHandler;
import org.bellerophon.gui.dialog.DelayDialog;
import org.bellerophon.gui.dialog.ErrorResultDialog;
import org.bellerophon.gui.viz.man.VizManPanel;
import org.bellerophon.io.WebServiceCom;

/**
 * The Class GetVizSetsWorker.
 *
 * @author Eric J. Lingerfelt
 */
public class GetVizSetsWorker extends SwingWorker<ErrorResult, Void>{

	private Frame frame;
	private VizManPanel panel;
	private DelayDialog dialog;
	private VizManPanel.VizManMode mode;
	
	/**
	 * Instantiates a new gets the viz sets worker.
	 *
	 * @param frame the frame
	 * @param panel the panel
	 * @param dialog the dialog
	 */
	public GetVizSetsWorker(Frame frame, VizManPanel panel, DelayDialog dialog, VizManPanel.VizManMode mode){
		this.frame = frame;
		this.panel = panel;
		this.dialog = dialog;
		this.mode = mode;
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
		return WebServiceCom.getInstance().doWebServiceComCall(panel.getData(), Action.GET_VIZ_JOBS);
	}
	
	/* (non-Javadoc)
	 * @see SwingWorker#done()
	 */
	protected void done(){
		try{
			ErrorResult result = get();
			if(!result.isError()){
				if(mode==VizManPanel.VizManMode.SELECT){
					panel.setEditPanelState();
				}else if(mode==VizManPanel.VizManMode.DELETE){
					panel.setDeletePanelState();
				}else if(mode==VizManPanel.VizManMode.REWIND){
					panel.setRewindPanelState();
				}
				panel.setCurrentState(mode);
				dialog.close();
			}else{
				dialog.close();
				ErrorResultDialog.createDialog(frame, result);
			}
		}catch(Exception e){
			dialog.close();
			CaughtExceptionHandler.handleException(e, frame);
		}
	}
}
