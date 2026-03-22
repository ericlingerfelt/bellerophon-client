/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: ModVizSetWorker.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.viz.man.worker;

import java.awt.Frame;

import javax.swing.SwingWorker;

import org.bellerophon.data.util.ErrorResult;
import org.bellerophon.data.util.VizSet;
import org.bellerophon.enums.Action;
import org.bellerophon.exception.CaughtExceptionHandler;
import org.bellerophon.gui.dialog.DelayDialog;
import org.bellerophon.gui.dialog.ErrorResultDialog;
import org.bellerophon.gui.viz.man.VizManPanel;
import org.bellerophon.gui.viz.man.VizManPanel.VizManMode;
import org.bellerophon.io.WebServiceCom;

/**
 * The Class ModVizSetWorker.
 *
 * @author Eric J. Lingerfelt
 */
public class Mod2DVizSetWorker extends SwingWorker<ErrorResult, Void>{

	private Frame frame;
	private VizManPanel parent;
	private VizSet d;
	private DelayDialog dialog;
	
	/**
	 * Instantiates a new mod viz set worker.
	 *
	 * @param frame the frame
	 * @param parent the parent
	 * @param d the d
	 * @param dialog the dialog
	 */
	public Mod2DVizSetWorker(Frame frame, VizManPanel parent, VizSet d, DelayDialog dialog){
		this.frame = frame;
		this.parent = parent;
		this.d = d;
		this.dialog = dialog;
	}

	/* (non-Javadoc)
	 * @see SwingWorker#doInBackground()
	 */
	protected ErrorResult doInBackground(){
		return WebServiceCom.getInstance().doWebServiceComCall(d, Action.MOD_2D_VIZ_SET);
	}
	
	/* (non-Javadoc)
	 * @see SwingWorker#done()
	 */
	protected void done(){
		try{
			ErrorResult result = get();
			if(!result.isError()){
				parent.setCurrentState(VizManMode.RESULTS);
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
