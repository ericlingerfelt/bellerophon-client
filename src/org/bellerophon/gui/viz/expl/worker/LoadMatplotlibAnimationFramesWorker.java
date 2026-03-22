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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingWorker;

import org.bellerophon.data.MainData;
import org.bellerophon.data.util.CustomFile;
import org.bellerophon.data.util.ErrorResult;
import org.bellerophon.data.util.MatplotlibAnimation;
import org.bellerophon.data.util.VizSet;
import org.bellerophon.exception.CaughtExceptionHandler;
import org.bellerophon.gui.dialog.ErrorResultDialog;
import org.bellerophon.gui.viz.expl.listener.LoadMatplotlibAnimationFramesListener;
import org.bellerophon.io.IOUtilities;

/**
 * The Class LoadFramesWorker.
 *
 * @author Eric J. Lingerfelt
 */
public class LoadMatplotlibAnimationFramesWorker extends SwingWorker<ErrorResult, Void>{

	private Frame frame;
	private MatplotlibAnimation ma;
	private VizSet vs;
	private LoadMatplotlibAnimationFramesListener listener;
	private ExecutorService pool;
	private int lastFrame;
	
	public LoadMatplotlibAnimationFramesWorker(Frame frame, VizSet vs, MatplotlibAnimation ma, int lastFrame, LoadMatplotlibAnimationFramesListener listener){
		this.frame = frame;
		this.vs = vs;
		this.ma = ma;
		this.lastFrame = lastFrame;
		this.listener = listener;
	}

	protected ErrorResult doInBackground(){
		ErrorResult result = new ErrorResult();
		if(ma.getFrameMap()==null){
			ma.setFrameMap(new TreeMap<Integer, CustomFile>());
		}
		DecimalFormat df = new DecimalFormat("00000");

		pool = Executors.newFixedThreadPool(4);
		for(int frame = lastFrame; frame>=1; frame--){
			CustomFile file = new CustomFile();
			file.setImg(true);
			file.setPath(MainData.MEDIA_URL 
								+ "/viz_sets/" 
								+ vs.getIndex() 
								+ "/images/matplotlib/" 
								+ ma.getIndex() 
								+ "_" 
								+ df.format(frame)
								+ ".png");
		    pool.submit(new DownloadImage(file, frame, ma));
		}
		pool.shutdown();
		try{
			pool.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
		} catch (InterruptedException ie){
			
		}
		return result;
	}
	
	public void cancel(){
		pool.shutdownNow();
		cancel(true);
	}
	
	protected void done(){
		try{
			if(!isCancelled()){
				ErrorResult result = get();
				if(!result.isError()){
					listener.matplotlibAnimationFramesLoaded();
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

class DownloadImage implements Runnable{

	private CustomFile file;
	private int frame;
	private MatplotlibAnimation ma;
	
    public DownloadImage(CustomFile file, int frame, MatplotlibAnimation ma){
        this.file = file;
        this.frame = frame;
		this.ma = ma;
    }

    public void run(){
	    try{
			file.setContents(IOUtilities.readURL(file.getPath()));
		}catch(Exception e){
			
		}finally{
			ma.getFrameMap().put(frame, file);
		}
	}
}

