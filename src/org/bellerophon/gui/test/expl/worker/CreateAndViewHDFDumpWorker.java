/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: CreateAndViewHDFDumpWorker.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.test.expl.worker;

import java.awt.Frame;

import javax.swing.SwingWorker;

import org.bellerophon.data.MainData;
import org.bellerophon.data.util.CustomFile;
import org.bellerophon.data.util.ErrorResult;
import org.bellerophon.enums.Action;
import org.bellerophon.exception.CaughtExceptionHandler;
import org.bellerophon.gui.dialog.DelayDialog;
import org.bellerophon.gui.dialog.DownloadFileDialog;
import org.bellerophon.gui.dialog.ErrorResultDialog;
import org.bellerophon.gui.test.expl.TestExplViewPanel;
import org.bellerophon.io.WebServiceCom;
import org.bellerophon.io.IOUtilities;

/**
 * The Class CreateAndViewHDFDumpWorker.
 *
 * @author Eric J. Lingerfelt
 */
public class CreateAndViewHDFDumpWorker extends SwingWorker<ErrorResult, Void>{

	private Frame frame;
	private CustomFile file;
	private TestExplViewPanel panel;
	private DownloadFileDialog dialog;

	/**
	 * Instantiates a new creates the and view hdf dump worker.
	 *
	 * @param frame the frame
	 * @param panel the panel
	 * @param file the file
	 */
	public CreateAndViewHDFDumpWorker(Frame frame, TestExplViewPanel panel, CustomFile file){
		this.frame = frame;
		this.file = file;
		this.panel = panel;
	}

	/* (non-Javadoc)
	 * @see SwingWorker#doInBackground()
	 */
	protected ErrorResult doInBackground(){
		String string = "Please wait while the HDF5 ACSII dump of this file is created.";
		DelayDialog delayDialog = new DelayDialog(frame, string, "Please wait...");
		delayDialog.open();
		ErrorResult result = WebServiceCom.getInstance().doWebServiceComCall(file, Action.CREATE_HDF_DUMP);
		if(result.isError()){
			return result;
		}
		delayDialog.close();
		MainData.setDownloading(true);
		dialog = new DownloadFileDialog(frame, null, file);
		dialog.open();
		try {
			file.setHdfDumpContents(IOUtilities.readURL(file.getHDFDumpPath(), dialog));
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
				panel.setSelectedFile(file);
				dialog.close();
			}else{
				MainData.setDownloading(false);
				dialog.close();
				ErrorResultDialog.createDialog(frame, result);
			}
		}catch(Exception e){
			MainData.setDownloading(false);
			dialog.close();
			CaughtExceptionHandler.handleException(e, frame);
		}
	}
}
