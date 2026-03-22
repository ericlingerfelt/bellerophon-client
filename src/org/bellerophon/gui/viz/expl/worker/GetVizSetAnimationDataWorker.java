/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: GetVizSetsWorker.java
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
import org.bellerophon.gui.dialog.DelayDialog;
import org.bellerophon.gui.dialog.ErrorResultDialog;
import org.bellerophon.gui.viz.expl.VizExplPanel;
import org.bellerophon.gui.viz.expl.VizExplSelectPanel;
import org.bellerophon.io.WebServiceCom;

public class GetVizSetAnimationDataWorker extends SwingWorker<ErrorResult, Void>{

	private Frame frame;
	private VizExplSelectPanel selectPanel;
	private VizExplPanel panel;
	private DelayDialog dialog;
	
	public GetVizSetAnimationDataWorker(Frame frame, VizExplSelectPanel selectPanel, VizExplPanel panel, DelayDialog dialog){
		this.frame = frame;
		this.selectPanel = selectPanel;
		this.panel = panel;
		this.dialog = dialog;
	}

	protected ErrorResult doInBackground(){
		ErrorResult result = new ErrorResult();
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
	
	protected void done(){
		try{
			ErrorResult result = get();
			if(!result.isError()){
				panel.getData().setGetAllData(false);
				selectPanel.exploreSelectedVizSets();
				panel.startUpdater();
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