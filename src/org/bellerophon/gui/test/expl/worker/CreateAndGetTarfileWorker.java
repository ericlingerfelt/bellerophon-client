/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: CreateAndGetTarfileWorker.java
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
import org.bellerophon.data.util.ErrorResult;
import org.bellerophon.data.util.RegressionTest;
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
public class CreateAndGetTarfileWorker extends SwingWorker<ErrorResult, Void>{

	private Frame frame;
	private RegressionTest rt;
	private File file;
	private DownloadFileDialog dialog;

	/**
	 * Instantiates a new creates the and get tarfile worker.
	 *
	 * @param frame the frame
	 * @param rt the rt
	 * @param file the file
	 */
	public CreateAndGetTarfileWorker(Frame frame, RegressionTest rt, File file){
		this.frame = frame;
		this.rt = rt;
		this.file = file;
	}

	/* (non-Javadoc)
	 * @see SwingWorker#doInBackground()
	 */
	protected ErrorResult doInBackground(){
		String string = "Please wait while this regression test directory is compressed into a tarfile.";
		DelayDialog delayDialog = new DelayDialog(frame, string, "Please wait...");
		delayDialog.open();
		ErrorResult result = WebServiceCom.getInstance().doWebServiceComCall(rt, Action.CREATE_REGRESSION_TEST_TARFILE);
		if(result.isError()){
			delayDialog.close();
			return result;
		}
		delayDialog.close();
		MainData.setDownloading(true);
		dialog = new DownloadFileDialog(frame, file, rt.getTarfile());
		dialog.open();
		try {
			rt.getTarfile().setContents(IOUtilities.readURL(rt.getTarfile().getPath(), dialog));
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
				IOUtilities.writeFile(file, rt.getTarfile().getContents());
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
