/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: CreateAndGetMatplotlibAnimationPythonfileWorker.java
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

import org.bellerophon.data.util.ErrorResult;
import org.bellerophon.data.util.MatplotlibAnimation;
import org.bellerophon.enums.Action;
import org.bellerophon.exception.CaughtExceptionHandler;
import org.bellerophon.gui.dialog.BackgroundMessageDialog;
import org.bellerophon.gui.dialog.ErrorResultDialog;
import org.bellerophon.io.WebServiceCom;
import org.bellerophon.io.IOUtilities;

/**
 * The Class CreateAndGetMatplotlibAnimationPythonfileWorker.
 *
 * @author Eric J. Lingerfelt
 */
public class CreateAndGetMatplotlibAnimationPythonfileWorker extends SwingWorker<ErrorResult, Void>{

	private Frame frame;
	private MatplotlibAnimation va;
	private File file;
	private BackgroundMessageDialog dialog;

	/**
	 * Instantiates a new creates the and get tarfile worker.
	 *
	 * @param frame the frame
	 * @param a the a
	 * @param file the file
	 */
	public CreateAndGetMatplotlibAnimationPythonfileWorker(Frame frame, MatplotlibAnimation va, File file){
		this.frame = frame;
		this.va = va;
		this.file = file;
		
		String string = "Please wait while the selected animation is converted into a VisIt python file. "
				+ "This may take several minutes due to the need to extract time data from the Silo file associated with each frame. "
				+ "Once complete, the python file will be written to the selected filename and this dialog will close automatically.";
		dialog = new BackgroundMessageDialog(frame, string, "Creating VisIt Python for VisIt Animation Number " + va.getIndex());
	}

	/* (non-Javadoc)
	 * @see SwingWorker#doInBackground()
	 */
	protected ErrorResult doInBackground(){
		if(va.getExportAllFramesForPython()){
			dialog.open();
		}else{
			frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		}
		ErrorResult result = WebServiceCom.getInstance().doWebServiceComCall(va, Action.CREATE_MATPLOTLIB_ANIMATION_PYTHONFILE);
		if(result.isError()){
			if(va.getExportAllFramesForPython()){
				dialog.close();
			}else{
				frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
			return result;
		}
		try {
			IOUtilities.writeURLToFile(va.getPythonfile().getPath(), file);
			if(va.getExportAllFramesForPython()){
				dialog.close();
			}else{
				frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		} catch (Exception e) {
			result.setError(true);
			result.setString(e.getMessage());
			if(va.getExportAllFramesForPython()){
				dialog.close();
			}else{
				frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
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
				//YOU ARE GOOD!
			}else{
				ErrorResultDialog.createDialog(frame, result);
			}
		}catch(Exception e){
			CaughtExceptionHandler.handleException(e, frame);
		}
	}
}