/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: CreateAndGetGraceAnimationTarfileWorker.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.viz.expl.worker;

import java.awt.Frame;
import java.io.File;

import javax.swing.SwingWorker;

import org.bellerophon.data.MainData;
import org.bellerophon.data.util.ErrorResult;
import org.bellerophon.data.util.UploadedAnimation;
import org.bellerophon.enums.Action;
import org.bellerophon.exception.CaughtExceptionHandler;
import org.bellerophon.gui.dialog.DelayDialog;
import org.bellerophon.gui.dialog.DownloadFileDialog;
import org.bellerophon.gui.dialog.ErrorResultDialog;
import org.bellerophon.io.WebServiceCom;
import org.bellerophon.io.IOUtilities;

/**
 * The Class CreateAndGetTarfileWorker.
 *
 * @author Eric J. Lingerfelt
 */
public class CreateAndGetUploadedAnimationTarfileWorker extends SwingWorker<ErrorResult, Void>{

	private Frame frame;
	private UploadedAnimation ua;
	private File file;
	private DownloadFileDialog dialog;

	/**
	 * Instantiates a new creates the and get tarfile worker.
	 *
	 * @param frame the frame
	 * @param a the a
	 * @param file the file
	 */
	public CreateAndGetUploadedAnimationTarfileWorker(Frame frame, UploadedAnimation ua, File file){
		this.frame = frame;
		this.ua = ua;
		this.file = file;
	}

	/* (non-Javadoc)
	 * @see SwingWorker#doInBackground()
	 */
	protected ErrorResult doInBackground(){
		String string = "Please wait while the selected uploaded animation images and associated movie are compressed into a tarfile. "
				+ "This may take several minutes. "
				+ "The download will begin automatically.";
		DelayDialog delayDialog = new DelayDialog(frame, string, "Please wait...");
		delayDialog.open();
		ErrorResult result = WebServiceCom.getInstance().doWebServiceComCall(ua, Action.CREATE_UPLOADED_ANIMATION_TARFILE);
		if(result.isError()){
			delayDialog.close();
			return result;
		}
		delayDialog.close();
		MainData.setDownloading(true);
		dialog = new DownloadFileDialog(frame, file, ua.getTarfile());
		dialog.open();
		try {
			IOUtilities.writeURLToFile(ua.getTarfile().getPath(), file, dialog);
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
