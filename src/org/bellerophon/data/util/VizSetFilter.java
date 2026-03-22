/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: VizSetFilter.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.data.util;

import java.util.Calendar;
import org.bellerophon.data.Data;

/**
 * The Class VizSetFilter is a filter for viz sets.
 *
 * @author Eric J. Lingerfelt
 */
public class VizSetFilter implements Data{
	
	private int index;
	private String vizSetId;
	private String progenitor;
	private Calendar creationDate;
	private User creator;
	
	/**
	 * The Constructor.
	 */
	public VizSetFilter(){
		initialize();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public VizSetFilter clone(){
		VizSetFilter vsf = new VizSetFilter();
		vsf.index = index;
		vsf.vizSetId = vizSetId;
		vsf.progenitor = progenitor;
		vsf.creationDate = (Calendar)creationDate.clone();
		vsf.creator = creator.clone();
		return vsf;
	}
	
	/* (non-Javadoc)
	 * @see org.bellerophon.data.Data#initialize()
	 */
	public void initialize(){
		index = -1;
		vizSetId = "";
		progenitor = null;
		creationDate = null;
		creator = null;
	}

	/**
	 * Apply filter.
	 *
	 * @param vs the vs
	 * @return true, if successful
	 */
	public boolean applyFilter(VizSet vs){
		if(index!=-1 && vs.getIndex()!=index){
			return false;
		}
		if(!vizSetId.equals("") && !vs.getVizSetId().equals(vizSetId)){
			return false;
		}
		if(progenitor!=null && !vs.getProgenitor().equals(progenitor)){
			return false;
		}
		if(creator!=null && !vs.getCreator().equals(creator)){
			return false;
		}
		if(creationDate!=null && !vs.getCreationDate().equals(creationDate)){
			return false;
		}
		return true;
	}
	
	public String getProgenitor(){return progenitor;}
	public void setProgenitor(String progenitor){this.progenitor = progenitor;}
	
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
	
}

