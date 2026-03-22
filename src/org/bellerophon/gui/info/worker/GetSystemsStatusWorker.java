/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: GetSystemsStatusWorker.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.info.worker;

import java.io.IOException;

import javax.swing.SwingWorker;

import org.bellerophon.data.util.ErrorResult;
import org.bellerophon.gui.info.InfoPanel;
import org.bellerophon.io.IOUtilities;

/**
 * The Class GetSystemsStatusWorker.
 *
 * @author Eric J. Lingerfelt
 */
public class GetSystemsStatusWorker extends SwingWorker<ErrorResult, Void>{

	private InfoPanel panel;
	private String contents;
	private String url;
	private InfoPanel.PlatformCenter center;
	
	private final String OLCF_URL = "https://www.olcf.ornl.gov/for-users/center-status/";
	private final String NERSC_URL = "https://www.nersc.gov/users/live-status/";
	
	/**
	 * Instantiates a new gets the systems status worker.
	 *
	 * @param panel the panel
	 * @param center the center
	 */
	public GetSystemsStatusWorker(InfoPanel panel, InfoPanel.PlatformCenter center){
		this.panel = panel;
		this.center = center;
		if(center==InfoPanel.PlatformCenter.OLCF){
			url = OLCF_URL;
		}else if(center==InfoPanel.PlatformCenter.NERSC){
			url = NERSC_URL;
		}
	}

	/* (non-Javadoc)
	 * @see SwingWorker#doInBackground()
	 */
	protected ErrorResult doInBackground(){
		ErrorResult result = new ErrorResult();
		try{
			contents = new String(IOUtilities.readURL(url));
		}catch(IOException ioe){
			result.setError(true);
			result.setString(ioe.getMessage());
			ioe.printStackTrace();
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
				panel.processStatus(center, contents);
			}else{
				//ERROR!
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
