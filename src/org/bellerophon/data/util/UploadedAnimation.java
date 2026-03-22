/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: UploadedAnimation.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.data.util;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.TreeMap;

import org.bellerophon.enums.*;
import org.bellerophon.gui.format.Calendars;
import org.bellerophon.gui.viz.expl.comp.VizExplCompPanel;

/**
 * The Class UploadedAnimation is the data structure for a matplotlib animation.
 *
 * @author Eric J. Lingerfelt
 */
public class UploadedAnimation extends Animation{
	
	private String desc, localFilepath;
	private User uploader;
	private double bounceTime;
	private int numFrames;
	private Calendar uploadDate;
	
	/**
	 * The Constructor.
	 */
	public UploadedAnimation(){
		initialize();
	}
	
	/**
	 * Gets the web service com value.
	 *
	 * @return the web service com value
	 */
	public String getWebServiceComValue(){
		String s = "";
		s += index + ":";
		s += animationId + ":";
		s += bounceTime + ":";
		s += numFrames + ":";
		s += desc + ":";
		s += localFilepath;
		return s;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		String string = "";
		if(!animationId.equals("")){
			string += animationId;
		}
		string += " : " + numFrames;
		string += " : " + Calendars.getTimestampStringShort(modDate);
		string += "";
		return string;
	}
	
	/**
	 * To string tar filename.
	 *
	 * @return the string
	 */
	public String toStringTarFilename(){
		String string = "";
		string += parentIndex + "_";
		string += index + "_";
		if(!animationId.equals("")){
			string += animationId + "_";
		}
		string += Calendars.getTimestampStringShort(Calendar.getInstance()) + ".tgz";
		return string;
	}
	
	/**
	 * To string movie filename.
	 *
	 * @return the string
	 */
	public String toStringMovieFilename(){
		String string = "";
		string += parentIndex + "_";
		string += index + "_";
		if(!animationId.equals("")){
			string += animationId;
		}
		string += ".mp4";
		return string;
	}
	
	/**
	 * To string frame filename.
	 *
	 * @param frame the frame
	 * @return the string
	 */
	public String toStringFrameFilename(int frame){
		String string = "";
		string += parentIndex + "_";
		string += index + "_";
		if(!animationId.equals("")){
			string += animationId;
		}
		string += "_" + new DecimalFormat("00000").format(frame) + ".png";
		return string;
	}
	
	/**
	 * To string info.
	 *
	 * @param showFullReport the show full report
	 * @return the string
	 */
	public String toStringInfo(boolean showFullReport){
		String s = "";
		s += "\nAnimation Information\n";
		if(showFullReport){
			s += "Animation Index = " + index + "\n";
			if(!animationId.equals("")){
				s += "Animation Unique ID = " + animationId + "\n";
			}
			s += "Number of Frames = " + numFrames + "\n";
			s += "Bounce Time = " + bounceTime + "\n";
			s += "Uploaded By = " + uploader + "\n";
			s += "Upload Date  = " + Calendars.getDateString(uploadDate) + "\n";
			if(!desc.equals("")){
				s += "Description  = " + desc + "\n";
			}
		}else{
			if(!animationId.equals("")){
				s += "Animation Unique ID = " + animationId + "\n";
			}
			s += "Number of Frames = " + numFrames + "\n";
			s += "Bounce Time = " + bounceTime + "\n";
		}
		return s;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public UploadedAnimation clone(){
		UploadedAnimation ua = new UploadedAnimation();
		ua.index = index;
		ua.panelMap = panelMap;
		ua.parentIndex = parentIndex;
		ua.animationId = animationId;
		ua.currentFrame = currentFrame;
		ua.creationDate = (Calendar)creationDate.clone();
		ua.uploadDate = (Calendar)uploadDate.clone();
		ua.frameMap = cloneFrameMap();
		ua.panelMap = clonePanelMap();
		ua.moviefile = moviefile.clone();
		ua.hot = hot;
		ua.framefile = framefile.clone();
		ua.tarfile = tarfile.clone();
		ua.desc = desc;
		ua.metadata = metadata;
		ua.uploader = uploader.clone();
		ua.bounceTime = bounceTime;
		ua.numFrames = numFrames;
		return ua;
	}
	
	/**
	 * Clone panel map.
	 *
	 * @return the tree map
	 */
	private TreeMap<VizCompType, VizExplCompPanel> clonePanelMap(){
		TreeMap<VizCompType, VizExplCompPanel> map = new TreeMap<VizCompType, VizExplCompPanel>();
		Iterator<VizCompType> itr = panelMap.keySet().iterator();
		while(itr.hasNext()){
			VizCompType type = itr.next();
			map.put(type, panelMap.get(type));
		}
		return map;
	}
	
	/**
	 * Clone frame map.
	 *
	 * @return the tree map
	 */
	private TreeMap<Integer, CustomFile> cloneFrameMap(){
		TreeMap<Integer, CustomFile> map = new TreeMap<Integer, CustomFile>();
		Iterator<Integer> itr = frameMap.keySet().iterator();
		while(itr.hasNext()){
			Integer image = itr.next();
			map.put(image, frameMap.get(image).clone());
		}
		return map;
	}
	
	/* (non-Javadoc)
	 * @see org.bellerophon.data.Data#initialize()
	 */
	public void initialize(){
		super.initialize();
		desc = "";
		uploader = null;
		uploadDate = Calendars.getDefaultCalendar();
		localFilepath = "";
		bounceTime = 0.0;
		numFrames = 0;
		panelMap.put(VizCompType.BROWSE, null);
		panelMap.put(VizCompType.CUSTOM, null);
		panelMap.put(VizCompType.MOVIE, null);
	}
	
	public String getDesc(){return desc;}
	public void setDesc(String desc){this.desc = desc;}
	
	public String getLocalFilepath(){return localFilepath;}
	public void setLocalFilepath(String localFilepath){this.localFilepath = localFilepath;}
	
	public User getUploader(){return uploader;}
	public void setUploader(User uploader){this.uploader = uploader;}
	
	public double getBounceTime(){return bounceTime;}
	public void setBounceTime(double bounceTime){this.bounceTime = bounceTime;}
	
	public int getNumFrames(){return numFrames;}
	public void setNumFrames(int numFrames){this.numFrames = numFrames;}
	
	public Calendar getUploadDate(){return uploadDate;}
	public void setUploadDate(Calendar uploadDate){this.uploadDate = uploadDate;}
	
}
