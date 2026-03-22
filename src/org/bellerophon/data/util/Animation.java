/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: Animation.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.data.util;

import java.util.Calendar;
import java.util.Iterator;
import java.util.TreeMap;

import org.bellerophon.data.Data;
import org.bellerophon.enums.*;
import org.bellerophon.gui.format.Calendars;
import org.bellerophon.gui.viz.expl.comp.VizExplCompPanel;

/**
 * The Class Animation is the data structure for an animation.
 *
 * @author Eric J. Lingerfelt
 */
public class Animation implements Data{

	protected int index, parentIndex, currentFrame;
	protected boolean hot, movieCurrent;
	protected String animationId, metadata;
	protected Calendar creationDate, modDate;
	protected TreeMap<Integer, CustomFile> frameMap;
	protected CustomFile moviefile, framefile, tarfile, datafile;
	protected TreeMap<VizCompType, VizExplCompPanel> panelMap;
	
	/**
	 * The Constructor.
	 */
	public Animation(){
		initialize();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o){
		if(!(o instanceof Animation)){
			return false;
		}
		Animation a = (Animation) o;
		return a.getIndex()==index;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		String string = "";
		return string;
	}
	
	/**
	 * To string tar filename.
	 *
	 * @return the string
	 */
	public String toStringTarFilename(){
		String string = "";
		return string;
	}
	
	/**
	 * To string movie filename.
	 *
	 * @return the string
	 */
	public String toStringMovieFilename(){
		String string = "";
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
		return s;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Animation clone(){
		Animation a = new Animation();
		a.index = index;
		a.panelMap = panelMap;
		a.parentIndex = parentIndex;
		a.animationId = animationId;
		a.currentFrame = currentFrame;
		a.creationDate = (Calendar)creationDate.clone();
		a.modDate = (Calendar)modDate.clone();
		a.frameMap = cloneFrameMap();
		a.panelMap = clonePanelMap();
		a.moviefile = moviefile.clone();
		a.hot = hot;
		a.framefile = framefile.clone();
		a.datafile = datafile.clone();
		a.tarfile = tarfile.clone();
		a.metadata = metadata;
		a.movieCurrent = movieCurrent;
		return a;
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
		index = -1;
		parentIndex = -1;
		currentFrame = -1;
		animationId = "";
		frameMap = null;
		moviefile = null;
		framefile = null;
		datafile = null;
		creationDate = Calendars.getDefaultCalendar();
		modDate = Calendars.getDefaultCalendar();
		hot = false;
		tarfile = null;
		metadata = "";
		movieCurrent = false;
		panelMap = new TreeMap<VizCompType, VizExplCompPanel>();
	}
	
	public CustomFile getDatafile(){return datafile;}
	
	/**
	 * Sets the tarfile.
	 *
	 * @param tarfile the new tarfile
	 */
	public void setDatafile(CustomFile datafile){this.datafile = datafile;}
	
	/**
	 * Gets the tarfile.
	 *
	 * @return the tarfile
	 */
	public CustomFile getTarfile(){return tarfile;}
	
	/**
	 * Sets the tarfile.
	 *
	 * @param tarfile the new tarfile
	 */
	public void setTarfile(CustomFile tarfile){this.tarfile = tarfile;}
	
	/**
	 * Gets the framefile.
	 *
	 * @return the framefile
	 */
	public CustomFile getFramefile(){return framefile;}
	
	/**
	 * Sets the framefile.
	 *
	 * @param framefile the new framefile
	 */
	public void setFramefile(CustomFile framefile){this.framefile = framefile;}
	
	/**
	 * Gets the moviefile.
	 *
	 * @return the moviefile
	 */
	public CustomFile getMoviefile(){return moviefile;}
	
	/**
	 * Sets the moviefile.
	 *
	 * @param moviefile the new moviefile
	 */
	public void setMoviefile(CustomFile moviefile){this.moviefile = moviefile;}
	
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
	 * Gets the parent index.
	 *
	 * @return the parent index
	 */
	public int getParentIndex(){return parentIndex;}
	
	/**
	 * Sets the parent index.
	 *
	 * @param parentIndex the new parent index
	 */
	public void setParentIndex(int parentIndex){this.parentIndex = parentIndex;}
	
	/**
	 * Gets the current frame.
	 *
	 * @return the current frame
	 */
	public int getCurrentFrame(){return currentFrame;}
	
	/**
	 * Sets the current frame.
	 *
	 * @param currentFrame the new current frame
	 */
	public void setCurrentFrame(int currentFrame){this.currentFrame = currentFrame;}
	
	/**
	 * Gets the metadata.
	 *
	 * @return the metadata
	 */
	public String getMetadata(){return metadata;}
	
	/**
	 * Sets the metadata.
	 *
	 * @param metadata the new metadata
	 */
	public void setMetadata(String metadata){this.metadata = metadata;}
	
	/**
	 * Gets the animation id.
	 *
	 * @return the animation id
	 */
	public String getAnimationId(){return animationId;}
	
	/**
	 * Sets the animation id.
	 *
	 * @param animationId the new animation id
	 */
	public void setAnimationId(String animationId){this.animationId = animationId;}
	
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
	 * Gets the frame map.
	 *
	 * @return the frame map
	 */
	public TreeMap<Integer, CustomFile> getFrameMap(){return frameMap;}
	
	/**
	 * Sets the frame map.
	 *
	 * @param frameMap the frame map
	 */
	public void setFrameMap(TreeMap<Integer, CustomFile> frameMap){this.frameMap = frameMap;}
	
	/**
	 * Gets the panel map.
	 *
	 * @return the panel map
	 */
	public TreeMap<VizCompType, VizExplCompPanel> getPanelMap(){return panelMap;}
	
	/**
	 * Checks if is hot.
	 *
	 * @return true, if is hot
	 */
	public boolean isHot(){return hot;}
	
	/**
	 * Sets the hot.
	 *
	 * @param hot the new hot
	 */
	public void setHot(boolean hot){this.hot = hot;}
	
	public boolean isMovieCurrent(){return movieCurrent;}
	
	/**
	 * Sets the hot.
	 *
	 * @param hot the new hot
	 */
	public void setMovieCurrent(boolean movieCurrent){this.movieCurrent = movieCurrent;}
	
}
