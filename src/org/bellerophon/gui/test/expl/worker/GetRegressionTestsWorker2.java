/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: GetRegressionTestsWorker2.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.test.expl.worker;

import java.awt.Frame;

import javax.swing.SwingWorker;

import org.bellerophon.data.feature.TestExplData;
import org.bellerophon.data.util.ErrorResult;
import org.bellerophon.enums.Action;
import org.bellerophon.exception.CaughtExceptionHandler;
import org.bellerophon.gui.dialog.DelayDialog;
import org.bellerophon.gui.dialog.ErrorResultDialog;
import org.bellerophon.gui.test.expl.TestExplSelectPanel;
import org.bellerophon.io.WebServiceCom;

/**
 * The Class GetRegressionTestsWorker2.
 *
 * @author Eric J. Lingerfelt
 */
public class GetRegressionTestsWorker2 extends SwingWorker<ErrorResult, Void>{

	private Frame frame;
	private TestExplSelectPanel panel;
	private TestExplData d;
	private DelayDialog dialog;
	
	/**
	 * Instantiates a new gets the regression tests worker2.
	 *
	 * @param frame the frame
	 * @param panel the panel
	 * @param d the d
	 * @param dialog the dialog
	 */
	public GetRegressionTestsWorker2(Frame frame, TestExplSelectPanel panel, TestExplData d, DelayDialog dialog){
		this.frame = frame;
		this.panel = panel;
		this.d = d;
		this.dialog = dialog;
	}

	/* (non-Javadoc)
	 * @see SwingWorker#doInBackground()
	 */
	protected ErrorResult doInBackground(){
		return WebServiceCom.getInstance().doWebServiceComCall(d, Action.GET_REGRESSION_TESTS);
	}
	
	/* (non-Javadoc)
	 * @see SwingWorker#done()
	 */
	protected void done(){
		try{
			ErrorResult result = get();
			if(!result.isError()){
				panel.setCurrentState();
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