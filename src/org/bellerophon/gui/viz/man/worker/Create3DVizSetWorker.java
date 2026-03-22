/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: CreateVizSetWorker.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.viz.man.worker;

import java.awt.Frame;
import java.util.Iterator;

import javax.swing.SwingWorker;

import org.bellerophon.data.util.ErrorResult;
import org.bellerophon.data.util.UploadedAnimation;
import org.bellerophon.data.util.VizSet;
import org.bellerophon.enums.Action;
import org.bellerophon.exception.CaughtExceptionHandler;
import org.bellerophon.gui.dialog.ErrorResultDialog;
import org.bellerophon.gui.dialog.UploadAnimationFramesDialog;
import org.bellerophon.gui.viz.man.VizManPanel;
import org.bellerophon.gui.viz.man.VizManPanel.VizManMode;
import org.bellerophon.io.WebServiceCom;

/**
 * The Class CreateVizSetWorker.
 *
 * @author Eric J. Lingerfelt
 */
public class Create3DVizSetWorker extends SwingWorker<ErrorResult, Void>{

	private Frame frame;
	private VizManPanel parent;
	private VizSet d;
	
	/**
	 * Instantiates a new creates the viz set worker.
	 *
	 * @param frame the frame
	 * @param parent the parent
	 * @param d the d
	 */
	public Create3DVizSetWorker(Frame frame, VizManPanel parent, VizSet d){
		this.frame = frame;
		this.parent = parent;
		this.d = d;
	}

	/* (non-Javadoc)
	 * @see SwingWorker#doInBackground()
	 */
	protected ErrorResult doInBackground(){
		ErrorResult result = WebServiceCom.getInstance().doWebServiceComCall(d, Action.CREATE_3D_VIZ_SET);
		if(result.isError()){
			return result;
		}
		
		UploadAnimationFramesDialog dialog = new UploadAnimationFramesDialog(frame);
		dialog.open();
		
		Iterator<UploadedAnimation> itr = d.getUploadedAnimationMap().values().iterator();
		while(itr.hasNext()){
			UploadedAnimation ua = itr.next();
			dialog.setAnimation(ua);
			int numFrames = ua.getNumFrames();
			for(int i=1; i<=numFrames; i++){
				dialog.setFrame(i);
				String filepath = getImageFilepath(ua.getLocalFilepath(), i);
				ua.setCurrentFrame(i);
				result = WebServiceCom.getInstance().doWebServiceComCall(ua, Action.UPLOAD_ANIMATION_FRAME, filepath);
				if(result.isError()){
					dialog.close();
					return result;
				}
			}
		}
		dialog.close();
		return result;
	}
	
	private String getImageFilepath(String filepath, int frameNumber){
		int index = filepath.lastIndexOf(".");
		return filepath.substring(0, index-5) + String.format("%05d", frameNumber) + ".png";
	}
	
	/* (non-Javadoc)
	 * @see SwingWorker#done()
	 */
	protected void done(){
		try{
			ErrorResult result = get();
			if(!result.isError()){
				parent.setCurrentState(VizManMode.RESULTS);
			}else{
				ErrorResultDialog.createDialog(frame, result);
			}
		}catch(Exception e){
			CaughtExceptionHandler.handleException(e, frame);
		}
	}
}

