/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: BrowseGraceAnimationFrameWorker.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.viz.expl.worker;

import java.awt.Cursor;
import java.awt.Frame;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;

import javax.swing.SwingWorker;

import org.bellerophon.data.MainData;
import org.bellerophon.data.util.CustomFile;
import org.bellerophon.data.util.ErrorResult;
import org.bellerophon.data.util.UploadedAnimation;
import org.bellerophon.data.util.VizSet;
import org.bellerophon.exception.CaughtExceptionHandler;
import org.bellerophon.gui.dialog.ErrorResultDialog;
import org.bellerophon.gui.viz.expl.comp.VizExplBrowsePanel;
import org.bellerophon.io.IOUtilities;

/**
 * The Class BrowseFrameWorker.
 *
 * @author Eric J. Lingerfelt
 */
public class BrowseUploadedAnimationFrameWorker extends SwingWorker<ErrorResult, Void>{

	private VizSet vs;
	private UploadedAnimation ua;
	private Frame parent;
	private CustomFile file;
	private VizExplBrowsePanel panel;
	private int frame;
	
	/**
	 * Instantiates a new browse frame worker.
	 *
	 * @param parent the parent
	 * @param panel the panel
	 * @param vs the vs
	 * @param a the a
	 * @param frame the frame
	 */
	public BrowseUploadedAnimationFrameWorker(Frame parent, VizExplBrowsePanel panel, VizSet vs, UploadedAnimation ua, int frame){
		this.parent = parent;
		this.vs = vs;
		this.ua = ua;
		this.panel = panel;
		this.frame = frame;
	}

	/* (non-Javadoc)
	 * @see SwingWorker#doInBackground()
	 */
	protected ErrorResult doInBackground(){
		panel.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		ErrorResult result = new ErrorResult();
		try{
			DecimalFormat df = new DecimalFormat("00000");
			file = new CustomFile();
			file.setImg(true);
			file.setPath(MainData.MEDIA_URL 
								+ "/viz_sets/" 
								+ vs.getIndex() 
								+ "/images/uploaded/" 
								+ ua.getIndex() 
								+ "_" 
								+ df.format(frame)
								+ ".png");
			file.setContents(IOUtilities.readURL(file.getPath()));
		}catch(FileNotFoundException fnfe){
			try {
				file.setContents(null);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}catch(Exception e){
			e.printStackTrace();
			result.setError(true);
			result.setString(e.getMessage());
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
				panel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				panel.setFramePanelFile(file);
			}else{
				panel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				ErrorResultDialog.createDialog(parent, result);
			}
		}catch(Exception e){
			panel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			CaughtExceptionHandler.handleException(e, parent);
		}
	}
}

