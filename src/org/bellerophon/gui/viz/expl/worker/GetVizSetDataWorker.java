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

import javax.swing.SwingWorker;

import org.bellerophon.data.util.ErrorResult;
import org.bellerophon.enums.Action;
import org.bellerophon.exception.CaughtExceptionHandler;
import org.bellerophon.gui.BellerophonFrame;
import org.bellerophon.gui.dialog.DelayDialog;
import org.bellerophon.gui.dialog.ErrorResultDialog;
import org.bellerophon.gui.viz.expl.VizExplPanel;
import org.bellerophon.io.WebServiceCom;

public class GetVizSetDataWorker extends SwingWorker<ErrorResult, Void>{

	private BellerophonFrame frame;
	private VizExplPanel panel;
	private DelayDialog dialog;
	
	public GetVizSetDataWorker(BellerophonFrame frame, VizExplPanel panel, DelayDialog dialog){
		this.frame = frame;
		this.panel = panel;
		this.dialog = dialog;
	}

	protected ErrorResult doInBackground(){
		return WebServiceCom.getInstance().doWebServiceComCall(panel.getData(), Action.GET_VIZ_SETS);
	}
	
	protected void done(){
		try{
			ErrorResult result = get();
			if(!result.isError()){
				panel.getData().setGetAllData(false);
				panel.setCurrentState(VizExplPanel.VizExplMode.SELECT);
				frame.addPanel(panel);
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