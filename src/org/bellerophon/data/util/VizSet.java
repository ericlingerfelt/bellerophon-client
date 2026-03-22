/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: VizSet.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.data.util;

import java.util.*;
import org.bellerophon.data.Data;
import org.bellerophon.enums.*;
import org.bellerophon.gui.format.Calendars;

/**
 * The Class VizSet is the data structure for a viz set.
 *
 * @author Eric J. Lingerfelt
 */
public class VizSet implements Data{

	private int index, newLastFrame, lastFrame;
	private TreeMap<Integer, MatplotlibAnimation> matplotlibAnimationMap;
	private TreeMap<Integer, UploadedAnimation> uploadedAnimationMap;
	private TreeMap<Integer, VizJob> vizJobMap;
	private String progenitor;
	private String notes, vizSetId;
	private Calendar creationDate, modDate, timeStampDate;
	private User creator;
	private Resolution resolution;
	private VizSetType vizSetType;
	private double bounceTime, elapsedTime;
	 
	/**
	 * The Constructor.
	 */
	public VizSet(){
		initialize();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public VizSet clone(){
		VizSet vs = new VizSet();
		vs.index = index;
		vs.matplotlibAnimationMap = cloneMatplotlibAnimationMap();
		vs.uploadedAnimationMap = cloneUploadedAnimationMap();
		vs.vizJobMap = cloneVizJobMap();
		vs.lastFrame = lastFrame;
		vs.progenitor = progenitor;
		vs.notes = notes;
		vs.vizSetId = vizSetId;
		vs.creationDate = (Calendar)creationDate.clone();
		vs.modDate = (Calendar)modDate.clone();
		vs.timeStampDate = (Calendar)timeStampDate.clone();
		vs.creator = creator.clone();
		vs.resolution = resolution;
		vs.bounceTime = bounceTime;
		vs.elapsedTime = elapsedTime;
		vs.vizSetType = vizSetType;
		return vs;
	}
	
	private TreeMap<Integer, MatplotlibAnimation> cloneMatplotlibAnimationMap(){
		TreeMap<Integer, MatplotlibAnimation> map = new TreeMap<Integer, MatplotlibAnimation>();
		Iterator<Integer> itr = matplotlibAnimationMap.keySet().iterator();
		while(itr.hasNext()){
			int index = itr.next();
			MatplotlibAnimation va = matplotlibAnimationMap.get(index);
			map.put(index, va.clone());
		}
		return map;
	}
	
	private TreeMap<Integer, UploadedAnimation> cloneUploadedAnimationMap(){
		TreeMap<Integer, UploadedAnimation> map = new TreeMap<Integer, UploadedAnimation>();
		Iterator<Integer> itr = uploadedAnimationMap.keySet().iterator();
		while(itr.hasNext()){
			int index = itr.next();
			UploadedAnimation ua = uploadedAnimationMap.get(index);
			map.put(index, ua.clone());
		}
		return map;
	}
	
	/**
	 * Clone viz job map.
	 *
	 * @return the tree map
	 */
	private TreeMap<Integer, VizJob> cloneVizJobMap(){
		TreeMap<Integer, VizJob> map = new TreeMap<Integer, VizJob>();
		Iterator<Integer> itr = vizJobMap.keySet().iterator();
		while(itr.hasNext()){
			int index = itr.next();
			VizJob vj = vizJobMap.get(index);
			map.put(index, vj.clone());
		}
		return map;
	}
	
	/* (non-Javadoc)
	 * @see org.bellerophon.data.Data#initialize()
	 */
	public void initialize(){
		index = -1;
		matplotlibAnimationMap = null;
		uploadedAnimationMap = null;
		vizJobMap = null;
		lastFrame = 0;
		newLastFrame = 0;
		progenitor = "";
		notes = "";
		vizSetId = "";
		creationDate = Calendars.getDefaultCalendar();
		modDate = Calendars.getDefaultCalendar();
		timeStampDate = Calendars.getDefaultCalendar();
		creator = null;
		resolution = null;
		bounceTime = 0.0;
		elapsedTime = 0.0;
		vizSetType = VizSetType.CHIMERA2D;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o){
		if(!(o instanceof VizSet)){
			return false;
		}
		VizSet vs = (VizSet) o;
		return vs.getIndex()==index;
	}
	
	/**
	 * Sets the hot animation.
	 *
	 * @param animationIndex the new hot animation
	 */
	public void setHotMatplotlibAnimation(int matplotlibAnimationIndex){
		Iterator<Integer> itr = matplotlibAnimationMap.keySet().iterator();
		while(itr.hasNext()){
			int index = itr.next();
			if(index==matplotlibAnimationIndex){
				matplotlibAnimationMap.get(index).setHot(true);
			}else{
				matplotlibAnimationMap.get(index).setHot(false);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return vizSetId;
	}
	
	/**
	 * Gets the viz job for frame.
	 *
	 * @param frame the frame
	 * @return the viz job for frame
	 */
	public VizJob getVizJobForFrame(int frame){
		if(vizJobMap!=null && vizJobMap.size()>0){
			Iterator<VizJob> itr = vizJobMap.values().iterator();
			while(itr.hasNext()){
				VizJob vj = itr.next();
				if(frame >= vj.getStartFrame() && frame <= vj.getEndFrame()){
					return vj;
				}
			}
		}
		return null;
	}
	
	/**
	 * To info string.
	 *
	 * @param showFullReport the show full report
	 * @return the string
	 */
	public String toInfoString(boolean showFullReport){
		String s = "";
		s += "Viz Set Information\n";
		if(showFullReport){
			s += "Viz Set Index = " + index + "\n";
			if(!vizSetId.equals("")){
				s += "Viz Set Unique ID = "   + vizSetId + "\n";
			}
			s += "Created By = "              + creator + "\n";
			s += "Creation Date = "           + Calendars.getDateString(creationDate) + "\n";
			s += "Progenitor = "       + progenitor + "\n";
			s += "Resolution = "              + resolution + "\n";
			if(lastFrame!=-1){
				s += "Last Frame = " + lastFrame + "\n";
			}
			s += "Bounce Time = " + String.format("%+7.3f", bounceTime) + "\n";
			s += "Elapsed Time = " + String.format("%+7.3f", elapsedTime) + "\n";
		}else{
			if(!vizSetId.equals("")){
				s += "Viz Set Unique ID = "   + vizSetId + "\n";
			}
			if(lastFrame!=0){
				s += "Last Frame = " + lastFrame + "\n";
			}
			s += "Bounce Time = " + String.format("%+7.3f", bounceTime) + "\n";
			s += "Elapsed Time = " + String.format("%+7.3f", elapsedTime) + "\n";
			s += "Progenitor = " + progenitor + "\n";
		}
		return s;
	}
	
	public Calendar getTimeStampDate(){return timeStampDate;}
	public void setTimeStampDate(Calendar timeStampDate){this.timeStampDate = timeStampDate;}
	
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
	public void setIndex(int index){
		this.index = index;
	}
	
	/**
	 * Gets the matplotlib animation map.
	 *
	 * @return the matplotlib animation map
	 */
	public TreeMap<Integer, MatplotlibAnimation> getMatplotlibAnimationMap(){return matplotlibAnimationMap;}
	
	/**
	 * Sets the matplotlib animation map.
	 *
	 * @param matplotlibAnimationMap the matplotlib animation map
	 */
	public void setMatplotlibAnimationMap(TreeMap<Integer, MatplotlibAnimation> matplotlibAnimationMap){this.matplotlibAnimationMap = matplotlibAnimationMap;}

	public TreeMap<Integer, UploadedAnimation> getUploadedAnimationMap(){return uploadedAnimationMap;}
	public void setUploadedAnimationMap(TreeMap<Integer, UploadedAnimation> uploadedAnimationMap){this.uploadedAnimationMap = uploadedAnimationMap;}
	
	/**
	 * Gets the viz job map.
	 *
	 * @return the viz job map
	 */
	public TreeMap<Integer, VizJob> getVizJobMap(){return vizJobMap;}
	
	/**
	 * Sets the viz job map.
	 *
	 * @param vizJobMap the viz job map
	 */
	public void setVizJobMap(TreeMap<Integer, VizJob> vizJobMap){this.vizJobMap = vizJobMap;}
	
	public int getNewLastFrame(){return newLastFrame;}	
	public void setNewLastFrame(int newLastFrame){this.newLastFrame = newLastFrame;}
	
	/**
	 * Gets the last frame.
	 *
	 * @return the last frame
	 */
	public int getLastFrame(){return lastFrame;}
	
	/**
	 * Sets the last frame.
	 *
	 * @param lastFrame the new last frame
	 */
	public void setLastFrame(int lastFrame){this.lastFrame = lastFrame;}
	
	public String getProgenitor(){return progenitor;}
	public void setProgenitor(String progenitor){this.progenitor = progenitor;}
	
	public VizSetType getVizSetType(){return vizSetType;}
	public void setVizSetType(VizSetType vizSetType){this.vizSetType = vizSetType;}
	
	/**
	 * Gets the notes.
	 *
	 * @return the notes
	 */
	public String getNotes(){return notes;}
	
	/**
	 * Sets the notes.
	 *
	 * @param notes the new notes
	 */
	public void setNotes(String notes){this.notes = notes;}
	
	/**
	 * Gets the viz set id.
	 *
	 * @return the viz set id
	 */
	public String getVizSetId(){return vizSetId;}
	
	/**
	 * Sets the viz set id.
	 *
	 * @param vizSetId the new viz set id
	 */
	public void setVizSetId(String vizSetId){this.vizSetId = vizSetId;}
	
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
	 * Gets the creation date.
	 *
	 * @return the creation date
	 */
	public Calendar getCreationDate(){return creationDate;}
	
	/**
	 * Sets the creation date.
	 *
	 * @param creationDate the new creation date
	 */
	public void setCreationDate(Calendar creationDate){this.creationDate = creationDate;}
	
	/**
	 * Gets the mod date.
	 *
	 * @return the mod date
	 */
	public Calendar getModDate(){return modDate;}
	
	/**
	 * Sets the mod date.
	 *
	 * @param modDate the new mod date
	 */
	public void setModDate(Calendar modDate){this.modDate = modDate;}
	
	/**
	 * Gets the resolution.
	 *
	 * @return the resolution
	 */
	public Resolution getResolution(){return resolution;}
	
	/**
	 * Sets the resolution.
	 *
	 * @param resolution the new resolution
	 */
	public void setResolution(Resolution resolution){this.resolution = resolution;}
	
	public double getBounceTime(){return bounceTime;}
	public void setBounceTime(double bounceTime){this.bounceTime = bounceTime;}
	
	public double getElapsedTime(){return elapsedTime;}
	public void setElapsedTime(double elapsedTime){this.elapsedTime = elapsedTime;}

}

