/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: CreateAndGetMatplotlibAnimationTarfileWorker.java
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
import org.bellerophon.data.util.MatplotlibAnimation;
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
public class CreateAndGetMatplotlibAnimationTarfileWorker extends SwingWorker<ErrorResult, Void>{

	private Frame frame;
	private MatplotlibAnimation va;
	private File file;
	private DownloadFileDialog dialog;

	/**
	 * Instantiates a new creates the and get tarfile worker.
	 *
	 * @param frame the frame
	 * @param a the a
	 * @param file the file
	 */
	public CreateAndGetMatplotlibAnimationTarfileWorker(Frame frame, MatplotlibAnimation va, File file){
		this.frame = frame;
		this.va = va;
		this.file = file;
	}

	/* (non-Javadoc)
	 * @see SwingWorker#doInBackground()
	 */
	protected ErrorResult doInBackground(){
		String string = "Please wait while the selected color map animation images and associated movie are compressed into a tarfile. "
				+ "This may take several minutes. "
				+ "The download will begin automatically.";
		DelayDialog delayDialog = new DelayDialog(frame, string, "Please wait...");
		delayDialog.open();
		ErrorResult result = WebServiceCom.getInstance().doWebServiceComCall(va, Action.CREATE_MATPLOTLIB_ANIMATION_TARFILE);
		if(result.isError()){
			delayDialog.close();
			return result;
		}
		delayDialog.close();
		MainData.setDownloading(true);
		dialog = new DownloadFileDialog(frame, file, va.getTarfile());
		dialog.open();
		try {
			IOUtilities.writeURLToFile(va.getTarfile().getPath(), file, dialog);
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
