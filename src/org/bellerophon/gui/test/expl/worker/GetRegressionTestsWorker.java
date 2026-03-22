/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: GetRegressionTestsWorker.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.test.expl.worker;

import javax.swing.SwingWorker;

import org.bellerophon.data.util.ErrorResult;
import org.bellerophon.enums.Action;
import org.bellerophon.exception.CaughtExceptionHandler;
import org.bellerophon.gui.BellerophonFrame;
import org.bellerophon.gui.dialog.DelayDialog;
import org.bellerophon.gui.dialog.ErrorResultDialog;
import org.bellerophon.gui.test.expl.TestExplPanel;
import org.bellerophon.io.WebServiceCom;

/**
 * The Class GetRegressionTestsWorker.
 *
 * @author Eric J. Lingerfelt
 */
public class GetRegressionTestsWorker extends SwingWorker<ErrorResult, Void>{

	private BellerophonFrame frame;
	private TestExplPanel panel;
	private DelayDialog dialog;
	
	/**
	 * Instantiates a new gets the regression tests worker.
	 *
	 * @param frame the frame
	 * @param panel the panel
	 * @param dialog the dialog
	 */
	public GetRegressionTestsWorker(BellerophonFrame frame, TestExplPanel panel, DelayDialog dialog){
		this.frame = frame;
		this.panel = panel;
		this.dialog = dialog;
	}

	/* (non-Javadoc)
	 * @see SwingWorker#doInBackground()
	 */
	protected ErrorResult doInBackground(){
		return WebServiceCom.getInstance().doWebServiceComCall(panel.getData(), Action.GET_REGRESSION_TESTS);
	}
	
	/* (non-Javadoc)
	 * @see SwingWorker#done()
	 */
	protected void done(){
		try{
			ErrorResult result = get();
			if(!result.isError()){
				panel.setSelectPanelState();
				panel.setCurrentState(TestExplPanel.TestExplMode.SELECT);
				frame.addPanel(panel);
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
