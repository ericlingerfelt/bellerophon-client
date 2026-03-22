/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: VizData.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.data.feature;

import java.util.Iterator;
import java.util.TreeMap;
import org.bellerophon.data.Data;
import org.bellerophon.data.util.VizSet;

/**
 * The Class VizData is the parent data structure for all Viz Set tools.
 *
 * @author Eric J. Lingerfelt
 */
public class VizData implements Data{

	protected TreeMap<Integer, VizSet> vizSetMap;
	protected boolean getAllData;
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public VizData clone(){
		VizData vd = new VizData();
		vd.vizSetMap = cloneVizSetMap();
		return vd;
	}
	
	/**
	 * Clone viz set map.
	 *
	 * @return the tree map
	 */
	protected TreeMap<Integer, VizSet> cloneVizSetMap(){
		TreeMap<Integer, VizSet> map = new TreeMap<Integer, VizSet>();
		Iterator<Integer> itr = vizSetMap.keySet().iterator();
		while(itr.hasNext()){
			int index = itr.next();
			VizSet is = vizSetMap.get(index);
			map.put(index, is.clone());
		}
		return map;
	}
	
	/* (non-Javadoc)
	 * @see org.bellerophon.data.Data#initialize()
	 */
	public void initialize(){
		vizSetMap = null;
		getAllData = false;
	}
	
	public void setGetAllData(boolean getAllData){this.getAllData = getAllData;}
	public boolean getGetAllData(){return this.getAllData;}
	
	/**
	 * Gets the viz set map.
	 *
	 * @return the viz set map
	 */
	public TreeMap<Integer, VizSet> getVizSetMap(){return vizSetMap;}
	
	/**
	 * Sets the viz set map.
	 *
	 * @param vizSetMap the viz set map
	 */
	public void setVizSetMap(TreeMap<Integer, VizSet> vizSetMap){this.vizSetMap = vizSetMap;}
	
}
