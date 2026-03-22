/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: GetHDFFileContentsWorker.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.test.expl.worker;

import java.awt.Frame;
import java.io.File;

import javax.swing.SwingWorker;

import org.bellerophon.data.MainData;
import org.bellerophon.data.util.CustomFile;
import org.bellerophon.data.util.ErrorResult;
import org.bellerophon.exception.CaughtExceptionHandler;
import org.bellerophon.gui.dialog.DownloadFileDialog;
import org.bellerophon.gui.dialog.ErrorResultDialog;
import org.bellerophon.io.IOUtilities;

/**
 * The Class GetHDFFileContentsWorker.
 *
 * @author Eric J. Lingerfelt
 */
public class GetHDFFileContentsWorker extends SwingWorker<ErrorResult, Void>{

	private Frame frame;
	private CustomFile customFile;
	private File file;
	private DownloadFileDialog dialog;

	/**
	 * Instantiates a new gets the hdf file contents worker.
	 *
	 * @param frame the frame
	 * @param customFile the custom file
	 * @param file the file
	 */
	public GetHDFFileContentsWorker(Frame frame
										, CustomFile customFile
										, File file){
		this.frame = frame;
		this.customFile = customFile;
		this.file = file;
	}

	/* (non-Javadoc)
	 * @see SwingWorker#doInBackground()
	 */
	protected ErrorResult doInBackground(){
		dialog = new DownloadFileDialog(frame, file, customFile);
		dialog.open();
		MainData.setDownloading(true);
		ErrorResult result = new ErrorResult();
		try {
			customFile.setContents(IOUtilities.readURL(customFile.getPath(), dialog));
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
				dialog.close();	
				MainData.setDownloading(false);
				IOUtilities.writeFile(file, customFile.getContents());
			}else{
				dialog.close();
				MainData.setDownloading(false);
				ErrorResultDialog.createDialog(frame, result);
			}
		}catch(Exception e){
			dialog.close();
			MainData.setDownloading(false);
			CaughtExceptionHandler.handleException(e, frame);
		}
	}
}