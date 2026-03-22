/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: GenerateMatplotlibAnimationPreviewWorker.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.dialog.worker;

import java.awt.Cursor;

import javax.swing.SwingWorker;

import org.bellerophon.data.util.ErrorResult;
import org.bellerophon.data.util.MatplotlibAnimation;
import org.bellerophon.enums.Action;
import org.bellerophon.exception.CaughtExceptionHandler;
import org.bellerophon.gui.dialog.EditMatplotlibAnimationDialog;
import org.bellerophon.gui.dialog.ErrorResultDialog;
import org.bellerophon.io.WebServiceCom;

/**
 * The Class GenerateAnimationPreviewWorker.
 *
 * @author Eric J. Lingerfelt
 */
public class GenerateMatplotlibAnimationPreviewWorker extends SwingWorker<ErrorResult, Void>{

	private EditMatplotlibAnimationDialog dialog;
	private MatplotlibAnimation va;

	/**
	 * Instantiates a new generate animation preview worker.
	 *
	 * @param dialog the dialog
	 * @param a the a
	 */
	public GenerateMatplotlibAnimationPreviewWorker(EditMatplotlibAnimationDialog dialog, MatplotlibAnimation va){
		this.dialog = dialog;
		this.va = va;
	}

	/* (non-Javadoc)
	 * @see SwingWorker#doInBackground()
	 */
	protected ErrorResult doInBackground(){
		dialog.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		return WebServiceCom.getInstance().doWebServiceComCall(va, Action.GENERATE_MATPLOTLIB_ANIMATION_PREVIEW);
	}
	
	/* (non-Javadoc)
	 * @see SwingWorker#done()
	 */
	protected void done(){
		try{
			ErrorResult result = get();
			if(!result.isError()){
				dialog.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				dialog.setFramePanelFile(va.getFramefile());
			}else{
				dialog.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				ErrorResultDialog.createDialog(dialog, result);
			}
		}catch(Exception e){
			dialog.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			CaughtExceptionHandler.handleException(e, dialog);
		}
	}
}
