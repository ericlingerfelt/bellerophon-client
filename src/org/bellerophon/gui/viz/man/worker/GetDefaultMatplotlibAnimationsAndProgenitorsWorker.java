/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: GetAnimationsWorker.java
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
import org.bellerophon.gui.viz.man.VizManPanel.VizManMode;
import org.bellerophon.io.WebServiceCom;

/**
 * The Class GetAnimationsWorker.
 *
 * @author Eric J. Lingerfelt
 */
public class GetDefaultMatplotlibAnimationsAndProgenitorsWorker extends SwingWorker<ErrorResult, Void>{

	private Frame frame;
	private VizManPanel panel;
	private DelayDialog dialog;
	
	/**
	 * Instantiates a new gets the animations worker.
	 *
	 * @param frame the frame
	 * @param panel the panel
	 * @param dialog the dialog
	 */
	public GetDefaultMatplotlibAnimationsAndProgenitorsWorker(Frame frame, VizManPanel panel, DelayDialog dialog){
		this.frame = frame;
		this.panel = panel;
		this.dialog = dialog;
	}

	/* (non-Javadoc)
	 * @see SwingWorker#doInBackground()
	 */
	protected ErrorResult doInBackground(){
		ErrorResult result = new ErrorResult();
		result = WebServiceCom.getInstance().doWebServiceComCall(panel.getData().getVizSet(), Action.GET_DEFAULT_MATPLOTLIB_ANIMATIONS);
		if(result.isError()){
			return result;
		}
		return WebServiceCom.getInstance().doWebServiceComCall(panel.getData(), Action.GET_PROGENITORS);
	}
	
	/* (non-Javadoc)
	 * @see SwingWorker#done()
	 */
	protected void done(){
		try{
			ErrorResult result = get();
			if(!result.isError()){
				panel.setCurrentState(VizManMode.VIZ_SET);
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
