/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: SVNStatWorker.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.stat.worker;

import java.awt.Frame;
import java.io.File;
import java.util.Collections;
import java.util.concurrent.CancellationException;

import javax.swing.JTextArea;
import javax.swing.SwingWorker;

import org.bellerophon.data.MainData;
import org.bellerophon.data.feature.StatData;
import org.bellerophon.data.util.CustomFile;
import org.bellerophon.data.util.ErrorResult;
import org.bellerophon.enums.Action;
import org.bellerophon.exception.CaughtExceptionHandler;
import org.bellerophon.gui.dialog.ErrorResultDialog;
import org.bellerophon.gui.stat.StatPanel;
import org.bellerophon.io.WebServiceCom;
import org.bellerophon.io.IOUtilities;

/**
 * The Class SVNStatWorker.
 *
 * @author Eric J. Lingerfelt
 */
public class SVNStatWorker extends SwingWorker<ErrorResult, Void>{

	private StatPanel panel;
	private Frame frame;
	private String path;
	private StatData d;
	private JTextArea status;
	
	/**
	 * Instantiates a new sVN stat worker.
	 *
	 * @param d the d
	 * @param panel the panel
	 * @param frame the frame
	 * @param path the path
	 * @param status the status
	 */
	public SVNStatWorker(StatData d, StatPanel panel, Frame frame, String path, JTextArea status){
		this.d = d;
		this.panel = panel;
		this.frame = frame;
		this.path = path;
		this.status = status;
	}

	/* (non-Javadoc)
	 * @see SwingWorker#doInBackground()
	 */
	protected ErrorResult doInBackground(){
		status.setText("Generating SVN Statistical Content...");
		ErrorResult result =  WebServiceCom.getInstance().doWebServiceComCall(d, Action.CREATE_SVN_STATS);
		if(result.isError()){
			status.setText(status.getText() + "ERROR!");
			return result;
		}
		status.setText(status.getText() + "COMPLETE!");
		status.setText(status.getText() + "\nBegin writing content files to " + path);
		File parent = new File(path);
		parent.mkdirs();
		Collections.sort(d.getFileList());
		for(String filename: d.getFileList()){
			CustomFile file = new CustomFile();
			file.setPath(MainData.DATA_URL + "/statsvn/" + MainData.getID() + "/stats/" + filename);
			try {
				file.setContents(IOUtilities.readURL(file.getPath()));
				status.setText(status.getText() + "\nWriting " + filename + "...");
				IOUtilities.writeFile(path + "/" + filename, file.getContents());
				status.setText(status.getText() + "DONE!");
				status.setCaretPosition(status.getText().length());
			} catch (Exception e) {
				result = new ErrorResult();
				result.setError(true);
				result.setString(e.getMessage());
				return result;
			}
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
				panel.gotoEndPanel();
			}else{
				ErrorResultDialog.createDialog(frame, result);
				panel.gotoDataPanel();
			}
		}catch(CancellationException ce){
		}catch(Exception e){
			CaughtExceptionHandler.handleException(e, frame);
			panel.gotoDataPanel();
		}
	}
}
