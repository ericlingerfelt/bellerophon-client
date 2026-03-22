/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: SetHotMatplotlibAnimationWorker.java
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
import org.bellerophon.gui.viz.expl.VizExplViewPanel;
import org.bellerophon.io.WebServiceCom;

/**
 * The Class SetHotWorker.
 *
 * @author Eric J. Lingerfelt
 */
public class SetHotMatplotlibAnimationWorker extends SwingWorker<ErrorResult, Void>{

	private Frame frame;
	private VizExplViewPanel panel;
	private MatplotlibAnimation va;
	
	/**
	 * Instantiates a new sets the hot worker.
	 *
	 * @param frame the frame
	 * @param panel the panel
	 * @param a the a
	 */
	public SetHotMatplotlibAnimationWorker(Frame frame, VizExplViewPanel panel, MatplotlibAnimation va){
		this.frame = frame;
		this.panel = panel;
		this.va = va;
	}

	/* (non-Javadoc)
	 * @see SwingWorker#doInBackground()
	 */
	protected ErrorResult doInBackground(){
		frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		return WebServiceCom.getInstance().doWebServiceComCall(va, Action.SET_HOT_MATPLOTLIB_ANIMATION);
	}
	
	/* (non-Javadoc)
	 * @see SwingWorker#done()
	 */
	protected void done(){
		try{
			ErrorResult result = get();
			if(!result.isError()){
				panel.refreshTree();
				frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}else{
				frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				ErrorResultDialog.createDialog(frame, result);
			}
		}catch(Exception e){
			frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			CaughtExceptionHandler.handleException(e, frame);
		}
	}
}
