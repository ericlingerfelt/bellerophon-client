/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: LoadGraceAnimationFramesWorker.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.viz.expl.worker;

import java.awt.Frame;
import java.text.DecimalFormat;
import java.util.TreeMap;

import javax.swing.SwingWorker;

import org.bellerophon.data.MainData;
import org.bellerophon.data.util.CustomFile;
import org.bellerophon.data.util.ErrorResult;
import org.bellerophon.data.util.UploadedAnimation;
import org.bellerophon.data.util.VizSet;
import org.bellerophon.exception.CaughtExceptionHandler;
import org.bellerophon.gui.dialog.ErrorResultDialog;
import org.bellerophon.gui.viz.expl.listener.UpdateUploadedAnimationFramesListener;
import org.bellerophon.io.IOUtilities;

/**
 * The Class LoadFramesWorker.
 *
 * @author Eric J. Lingerfelt
 */
public class UpdateUploadedAnimationFramesWorker extends SwingWorker<ErrorResult, Void>{

	private Frame frame;
	private UploadedAnimation ua;
	private VizSet vs;
	private int startFrame;
	private UpdateUploadedAnimationFramesListener listener;
	
	/**
	 * Instantiates a new load frames worker.
	 *
	 * @param frame the frame
	 * @param panel the panel
	 * @param vs the vs
	 * @param a the a
	 */
	public UpdateUploadedAnimationFramesWorker(Frame frame, VizSet vs, UploadedAnimation ua, int startFrame, UpdateUploadedAnimationFramesListener listener){
		this.frame = frame;
		this.vs = vs;
		this.ua = ua;
		this.startFrame = startFrame;
		this.listener = listener;
	}

	protected ErrorResult doInBackground(){
		ErrorResult result = new ErrorResult();
		if(ua.getFrameMap()==null){
			ua.setFrameMap(new TreeMap<Integer, CustomFile>());
		}
		DecimalFormat df = new DecimalFormat("00000");
		int lastFrame = ua.getNumFrames();
		for(int frame = startFrame; frame<(lastFrame+1); frame++){
			CustomFile file = new CustomFile();
			file.setImg(true);
			file.setPath(MainData.MEDIA_URL 
								+ "/viz_sets/" 
								+ vs.getIndex() 
								+ "/images/uploaded/" 
								+ ua.getIndex() 
								+ "_" 
								+ df.format(frame)
								+ ".png");
			try{
				file.setContents(IOUtilities.readURL(file.getPath()));
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				ua.getFrameMap().put(frame, file);
			}
		}
		return result;
	}

	protected void done(){
		try{
			if(!isCancelled()){
				ErrorResult result = get();
				if(!result.isError()){
					listener.uploadedAnimationFramesUpdated();
				}else{
					ErrorResultDialog.createDialog(frame, result);
				}
			}else{
			}
		}catch(Exception e){
			CaughtExceptionHandler.handleException(e, frame);
		}
	}
}

