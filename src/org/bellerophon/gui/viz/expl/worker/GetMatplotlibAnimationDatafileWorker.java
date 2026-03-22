/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: GetMatplotlibAnimationDatafileWorker.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.viz.expl.worker;

import java.awt.Cursor;
import java.awt.Frame;
import java.io.File;

import javax.swing.SwingWorker;

import org.bellerophon.data.MainData;
import org.bellerophon.data.util.ErrorResult;
import org.bellerophon.data.util.MatplotlibAnimation;
import org.bellerophon.enums.Action;
import org.bellerophon.exception.CaughtExceptionHandler;
import org.bellerophon.gui.dialog.DownloadFileDialog;
import org.bellerophon.gui.dialog.ErrorResultDialog;
import org.bellerophon.io.WebServiceCom;
import org.bellerophon.io.IOUtilities;

/**
 * The Class GetMatplotlibAnimationDatafileWorker.
 *
 * @author Eric J. Lingerfelt
 */
public class GetMatplotlibAnimationDatafileWorker extends SwingWorker<ErrorResult, Void>{

	private Frame frame;
	private MatplotlibAnimation va;
	private File file;
	private DownloadFileDialog dialog;

	/**
	 * Instantiates a new gets the moviefile worker.
	 *
	 * @param frame the frame
	 * @param a the a
	 * @param file the file
	 */
	public GetMatplotlibAnimationDatafileWorker(Frame frame, MatplotlibAnimation va, File file){
		this.frame = frame;
		this.va = va;
		this.file = file;
	}

	/* (non-Javadoc)
	 * @see SwingWorker#doInBackground()
	 */
	protected ErrorResult doInBackground(){
		frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		ErrorResult result = WebServiceCom.getInstance().doWebServiceComCall(va, Action.GET_MATPLOTLIB_ANIMATION_DATAFILE_SIZE);
		if(result.isError()){
			frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			return result;
		}
		frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		MainData.setDownloading(true);
		dialog = new DownloadFileDialog(frame, file, va.getDatafile());
		dialog.open();
		try {
			IOUtilities.writeURLToFile(va.getDatafile().getPath(), file, dialog);
		} catch (Exception e) {
			result.setError(true);
			result.setString(e.getMessage());
			return result;
		}
		return result;
	}
	
	/* (non-Javadoc)
	 * @see SwingWorker#done()
	 */
	protected void done(){
		try{
			ErrorResult result = get();
			if(!result.isError()){
				MainData.setDownloading(false);
				if(dialog!=null){
					dialog.close();
				}
			}else{
				MainData.setDownloading(false);
				if(dialog!=null){
					dialog.close();
				}
				ErrorResultDialog.createDialog(frame, result);
			}
		}catch(Exception e){
			MainData.setDownloading(false);
			if(dialog!=null){
				dialog.close();
			}
			CaughtExceptionHandler.handleException(e, frame);
		}
	}
}
