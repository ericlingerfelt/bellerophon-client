/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: RecreateMatplotlibAnimationWorker.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.viz.expl.worker;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;

import javax.swing.SwingWorker;

import org.bellerophon.data.MainData;
import org.bellerophon.data.util.ErrorResult;
import org.bellerophon.data.util.MatplotlibAnimation;
import org.bellerophon.enums.Action;
import org.bellerophon.exception.CaughtExceptionHandler;
import org.bellerophon.gui.dialog.ErrorResultDialog;
import org.bellerophon.gui.dialog.MessageDialog;
import org.bellerophon.io.WebServiceCom;

/**
 * The Class RecreateMatplotlibAnimationWorker.
 *
 * @author Eric J. Lingerfelt
 */
public class RecreateMatplotlibAnimationWorker extends SwingWorker<ErrorResult, Void>{

	private Frame frame;
	private MatplotlibAnimation va;

	/**
	 * Instantiates a new gets the moviefile worker.
	 *
	 * @param frame the frame
	 * @param a the a
	 * @param file the file
	 */
	public RecreateMatplotlibAnimationWorker(Frame frame, MatplotlibAnimation va){
		this.frame = frame;
		this.va = va;
	}

	/* (non-Javadoc)
	 * @see SwingWorker#doInBackground()
	 */
	protected ErrorResult doInBackground(){
		frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		ErrorResult result = WebServiceCom.getInstance().doWebServiceComCall(va, Action.RECREATE_MATPLOTLIB_ANIMATION);
		if(result.isError()){
			frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			return result;
		}
		frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		return result;
	}
	
	/* (non-Javadoc)
	 * @see SwingWorker#done()
	 */
	protected void done(){
		try{
			ErrorResult result = get();
			if(!result.isError()){
				String string = "The frames for colormap animation #" + va.getIndex() + " are now being regenerated in the background on Bellerophon's web server. "
						+ "An email notification will be sent to <i>bellerophon_users@elist.ornl.gov</i>"
						+ " when this animation has been completely regenerated. "
						+ "At this time, you will need to restart Bellerophon to reload the regenerated animation. "
						+ "Please pardon this inconvience. "
						+ "You can close Bellerophon without affecting this action.";
				MessageDialog.createMessageDialog(frame, string, "Colormap Animation Frames Being Regenerated!", new Dimension(550, 180));
			}else{
				ErrorResultDialog.createDialog(frame, result);
			}
		}catch(Exception e){
			CaughtExceptionHandler.handleException(e, frame);
		}
	}
	
}
