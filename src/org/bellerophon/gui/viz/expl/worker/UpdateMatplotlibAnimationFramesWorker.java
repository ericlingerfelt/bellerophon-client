/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: LoadMatplotlibAnimationFramesWorker.java
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
import org.bellerophon.data.util.MatplotlibAnimation;
import org.bellerophon.data.util.VizSet;
import org.bellerophon.exception.CaughtExceptionHandler;
import org.bellerophon.gui.dialog.ErrorResultDialog;
import org.bellerophon.gui.viz.expl.listener.UpdateMatplotlibAnimationFramesListener;
import org.bellerophon.io.IOUtilities;

/**
 * The Class LoadFramesWorker.
 *
 * @author Eric J. Lingerfelt
 */
public class UpdateMatplotlibAnimationFramesWorker extends SwingWorker<ErrorResult, Void>{

	private Frame frame;
	private MatplotlibAnimation va;
	private VizSet vs;
	private int startFrame;
	private UpdateMatplotlibAnimationFramesListener listener;
	
	/**
	 * Instantiates a new load frames worker.
	 *
	 * @param frame the frame
	 * @param panel the panel
	 * @param vs the vs
	 * @param a the a
	 */
	public UpdateMatplotlibAnimationFramesWorker(Frame frame, VizSet vs, MatplotlibAnimation va, int startFrame, UpdateMatplotlibAnimationFramesListener listener){
		this.frame = frame;
		this.vs = vs;
		this.va = va;
		this.startFrame = startFrame;
		this.listener = listener;
	}

	protected ErrorResult doInBackground(){
		ErrorResult result = new ErrorResult();
		if(va.getFrameMap()==null){
			va.setFrameMap(new TreeMap<Integer, CustomFile>());
		}
		DecimalFormat df = new DecimalFormat("00000");
		int lastFrame = vs.getLastFrame();
		for(int frame = startFrame; frame<(lastFrame+1); frame++){
			CustomFile file = new CustomFile();
			file.setImg(true);
			file.setPath(MainData.MEDIA_URL 
								+ "/viz_sets/" 
								+ vs.getIndex() 
								+ "/images/matplotlib/" 
								+ va.getIndex() 
								+ "_" 
								+ df.format(frame)
								+ ".png");
			try{
				file.setContents(IOUtilities.readURL(file.getPath()));
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				va.getFrameMap().put(frame, file);
			}
		}
		return result;
	}
	
	protected void done(){
		try{
			if(!isCancelled()){
				ErrorResult result = get();
				if(!result.isError()){
					listener.matplotlibAnimationFramesUpdated();
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

