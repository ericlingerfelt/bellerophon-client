/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: CreateMatplotlibAnimationMoviefileWorker.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.viz.expl.worker;

import java.awt.Cursor;
import java.awt.Frame;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.SwingWorker;

import org.bellerophon.data.MainData;
import org.bellerophon.data.util.ErrorResult;
import org.bellerophon.data.util.MatplotlibAnimation;
import org.bellerophon.enums.Action;
import org.bellerophon.exception.CaughtExceptionHandler;
import org.bellerophon.file.CustomFileFilter;
import org.bellerophon.file.FileType;
import org.bellerophon.gui.dialog.CautionDialog;
import org.bellerophon.gui.dialog.DownloadFileDialog;
import org.bellerophon.gui.dialog.ErrorResultDialog;
import org.bellerophon.gui.dialog.MessageDialog;
import org.bellerophon.gui.util.PlainFileChooserFactory;
import org.bellerophon.io.WebServiceCom;
import org.bellerophon.io.IOUtilities;

/**
 * The Class GetMoviefileWorker.
 *
 * @author Eric J. Lingerfelt
 */
public class CreateMatplotlibAnimationMoviefileWorker extends SwingWorker<ErrorResult, Void>{

	private Frame frame;
	private MatplotlibAnimation va;
	private DownloadFileDialog dialog;
	
	/**
	 * Instantiates a new gets the moviefile worker.
	 *
	 * @param frame the frame
	 * @param a the a
	 * @param file the file
	 */
	public CreateMatplotlibAnimationMoviefileWorker(Frame frame, MatplotlibAnimation va){
		this.frame = frame;
		this.va = va;
	}

	/* (non-Javadoc)
	 * @see SwingWorker#doInBackground()
	 */
	protected ErrorResult doInBackground(){
		frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		ErrorResult result = WebServiceCom.getInstance().doWebServiceComCall(va, Action.CREATE_MATPLOTLIB_ANIMATION_MOVIEFILE);
		if(result.isError()){
			frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			return result;
		}
		frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		if(va.isMovieCurrent()){
			String string = "An MP4 movie for colormap animation #" + va.getIndex() + " already exists and is up to date. "
					+ "Upon clicking the OK button, the movie will be saved to your computer.";
			MessageDialog.createMessageDialog(frame, string, "MP4 Movie Exists!");
			File file = getMovieSaveDir(va.toStringMovieFilename());
			if(file!=null){
				MainData.setDownloading(true);
				dialog = new DownloadFileDialog(frame, file, va.getMoviefile());
				dialog.open();
				try{
					IOUtilities.writeURLToFile(va.getMoviefile().getPath(), file, dialog);
				}catch(Exception e){
					result.setError(true);
					result.setString(e.getMessage());
					ErrorResultDialog.createDialog(frame, result);
				}
			}
		}else{
			String string = "The MP4 movie for colormap animation #" + va.getIndex() + " is now being created in the background on Bellerophon's web server. "
					+ "An email notification will be sent to <i>bellerophon_users@elist.ornl.gov</i>"
					+ " containing a link to this movie when it is complete. You can close Bellerophon without affecting this action.";
			MessageDialog.createMessageDialog(frame, string, "MP4 Movie Being Created!");
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
	
	/**
	 * Gets the movie save dir.
	 *
	 * @param filename the filename
	 * @return the movie save dir
	 */
	private File getMovieSaveDir(String filename){
		JFileChooser fileDialog = PlainFileChooserFactory.createPlainFileChooser();
		fileDialog.setAcceptAllFileFilterUsed(false);
		fileDialog.addChoosableFileFilter(new CustomFileFilter(FileType.MP4));
		fileDialog.setSelectedFile(new File(filename));
		int returnVal = fileDialog.showSaveDialog(frame); 
		MainData.setAbsolutePath(fileDialog.getCurrentDirectory());
		if(returnVal==JFileChooser.APPROVE_OPTION){
			File file = fileDialog.getSelectedFile();
			String filepath = file.getAbsolutePath();
			if(new File(filepath).exists()){
				String msg = "The file " + file.getName() + " exists. Do you want to replace it?";
				int value = CautionDialog.createCautionDialog(frame, msg, "Attention!");
				if(value==CautionDialog.NO){
					getMovieSaveDir(file.getName());
				}else{
					return file;
				}
			}else{
				return file;
			}
		}
		return null;
	}
}
