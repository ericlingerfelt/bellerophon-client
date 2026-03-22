/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: VizJob.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.data.util;

import java.util.Calendar;

import org.bellerophon.data.Data;
import org.bellerophon.enums.Platform;
import org.bellerophon.gui.format.Calendars;

/**
 * The Class VizJob is the data structure for a viz job.
 *
 * @author Eric J. Lingerfelt
 */
public class VizJob implements Data{

	private int index, jobId, startFrame, endFrame;
	private Platform platform;
	private String modelsDataPath, initialDataPath;
	private Calendar startDate, endDate;
	private User creator;
	
	/**
	 * The Constructor.
	 */
	public VizJob(){
		initialize();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public VizJob clone(){
		VizJob vj = new VizJob();
		vj.index = index;
		vj.platform = platform;
		vj.startFrame = startFrame;
		vj.endFrame = endFrame;
		vj.jobId = jobId;
		vj.modelsDataPath = modelsDataPath;
		vj.initialDataPath = initialDataPath;
		vj.startDate = (Calendar)startDate.clone();
		vj.endDate = (Calendar)endDate.clone();
		vj.creator = creator.clone();
		return vj;
	}
	
	/**
	 * To info string.
	 *
	 * @param frame the frame
	 * @param showFullReport the show full report
	 * @return the string
	 */
	public String toInfoString(int frame, boolean showFullReport){
		String s = "";
		if(showFullReport){
			s += "\nViz Job Information for Frame = " + frame + "\n";
			s += "Viz Job Index = " + index + "\n";
			s += "Platform = "                + platform + "\n";
			if(creator==null){
				s += "User = "					  + "Unknown" + "\n";
			}else{
				s += "User = "					  + creator + "\n";
			}
			s += "Job Id = "                  + jobId + "\n";
			s += "Job Start Date = "          + Calendars.getDateTimeString(startDate) + "\n";
			s += "Job End Date = "            + Calendars.getDateTimeString(endDate) + "\n";
			if(!modelsDataPath.equals("")){
				s += "Models Data Path = "        + modelsDataPath + "\n";
			}
			if(!initialDataPath.equals("")){
				s += "Initial Data Path = "       + initialDataPath;
			}
		}
		return s;
	}
	
	/* (non-Javadoc)
	 * @see org.bellerophon.data.Data#initialize()
	 */
	public void initialize(){
		index = -1;
		jobId = -1;
		startFrame = -1;
		endFrame = -1;
		platform = null;
		modelsDataPath = "";
		initialDataPath = "";
		startDate = Calendars.getDefaultCalendar();
		endDate = Calendars.getDefaultCalendar();
		creator = null;
	}

	/**
	 * Gets the creator.
	 *
	 * @return the creator
	 */
	public User getCreator(){return creator;}
	
	/**
	 * Sets the creator.
	 *
	 * @param creator the new creator
	 */
	public void setCreator(User creator){this.creator = creator;}
	
	/**
	 * Gets the index.
	 *
	 * @return the index
	 */
	public int getIndex(){return index;}
	
	/**
	 * Sets the index.
	 *
	 * @param index the new index
	 */
	public void setIndex(int index){this.index = index;}
	
	/**
	 * Gets the job id.
	 *
	 * @return the job id
	 */
	public int getJobId(){return jobId;}
	
	/**
	 * Sets the job id.
	 *
	 * @param jobId the new job id
	 */
	public void setJobId(int jobId){this.jobId = jobId;}
	
	/**
	 * Gets the start frame.
	 *
	 * @return the start frame
	 */
	public int getStartFrame(){return startFrame;}
	
	/**
	 * Sets the start frame.
	 *
	 * @param startFrame the new start frame
	 */
	public void setStartFrame(int startFrame){this.startFrame = startFrame;}
	
	/**
	 * Gets the end frame.
	 *
	 * @return the end frame
	 */
	public int getEndFrame(){return endFrame;}
	
	/**
	 * Sets the end frame.
	 *
	 * @param endFrame the new end frame
	 */
	public void setEndFrame(int endFrame){this.endFrame = endFrame;}
	
	/**
	 * Gets the platform.
	 *
	 * @return the platform
	 */
	public Platform getPlatform(){return platform;}
	
	/**
	 * Sets the platform.
	 *
	 * @param platform the new platform
	 */
	public void setPlatform(Platform platform){this.platform = platform;}
	
	/**
	 * Gets the models data path.
	 *
	 * @return the models data path
	 */
	public String getModelsDataPath(){return modelsDataPath;}
	
	/**
	 * Sets the models data path.
	 *
	 * @param modelsDataPath the new models data path
	 */
	public void setModelsDataPath(String modelsDataPath){this.modelsDataPath = modelsDataPath;}
	
	/**
	 * Gets the initial data path.
	 *
	 * @return the initial data path
	 */
	public String getInitialDataPath(){return initialDataPath;}
	
	/**
	 * Sets the initial data path.
	 *
	 * @param initialDataPath the new initial data path
	 */
	public void setInitialDataPath(String initialDataPath){this.initialDataPath = initialDataPath;}
	
	/**
	 * Gets the start date.
	 *
	 * @return the start date
	 */
	public Calendar getStartDate(){return startDate;}
	
	/**
	 * Sets the start date.
	 *
	 * @param startDate the new start date
	 */
	public void setStartDate(Calendar startDate){this.startDate = startDate;}
	
	/**
	 * Gets the end date.
	 *
	 * @return the end date
	 */
	public Calendar getEndDate(){return endDate;}
	
	/**
	 * Sets the end date.
	 *
	 * @param endDate the new end date
	 */
	public void setEndDate(Calendar endDate){this.endDate = endDate;}
}


