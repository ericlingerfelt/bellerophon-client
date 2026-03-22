/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: CustomMatplotlibAnimationFrameWorker.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.viz.expl.worker;

import java.awt.Cursor;
import java.awt.Frame;

import javax.swing.SwingWorker;

import org.bellerophon.data.util.ErrorResult;
import org.bellerophon.data.util.MatplotlibAnimation;
import org.bellerophon.enums.Action;
import org.bellerophon.exception.CaughtExceptionHandler;
import org.bellerophon.gui.dialog.ErrorResultDialog;
import org.bellerophon.gui.viz.expl.comp.VizExplCustomPanel;
import org.bellerophon.io.WebServiceCom;

/**
 * The Class CustomFrameWorker.
 *
 * @author Eric J. Lingerfelt
 */
public class CustomMatplotlibAnimationFrameWorker extends SwingWorker<ErrorResult, Void>{

	private VizExplCustomPanel panel;
	private MatplotlibAnimation va;
	private Frame parent;

	/**
	 * Instantiates a new custom frame worker.
	 *
	 * @param parent the parent
	 * @param panel the panel
	 * @param a the a
	 */
	public CustomMatplotlibAnimationFrameWorker(Frame parent, VizExplCustomPanel panel, MatplotlibAnimation va){
		this.parent = parent;
		this.panel = panel;
		this.va = va;
	}

	/* (non-Javadoc)
	 * @see SwingWorker#doInBackground()
	 */
	protected ErrorResult doInBackground(){
		panel.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		return WebServiceCom.getInstance().doWebServiceComCall(va, Action.GENERATE_MATPLOTLIB_ANIMATION_PREVIEW);
	}
	
	/* (non-Javadoc)
	 * @see SwingWorker#done()
	 */
	protected void done(){
		try{
			ErrorResult result = get();
			if(!result.isError()){
				panel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				panel.setFramePanelFile(va.getFramefile());
			}else{
				panel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				ErrorResultDialog.createDialog(parent, result);
			}
		}catch(Exception e){
			panel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			CaughtExceptionHandler.handleException(e, parent);
		}
	}
}