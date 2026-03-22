/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: VizExplData.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.data.feature;

import org.bellerophon.data.Data;
import org.bellerophon.data.util.*;
import java.util.*;

/**
 * The Class VizExplData is the main data structure for the Viz Set Explorer tool.
 *
 * @author Eric J. Lingerfelt
 */
public class VizExplData extends VizData implements Data{

	private TreeMap<Integer, VizSet> selectedVizSetMap;
	
	/**
	 * The Constructor.
	 */
	public VizExplData(){
		initialize();
	}
	
	/* (non-Javadoc)
	 * @see org.bellerophon.data.feature.VizData#clone()
	 */
	public VizExplData clone(){
		VizExplData ved = new VizExplData();
		ved.vizSetMap = super.cloneVizSetMap();
		ved.selectedVizSetMap = cloneSelectedVizSetMap();
		return ved;
	}
	
	/**
	 * Clone selected viz set map.
	 *
	 * @return the tree map
	 */
	private TreeMap<Integer, VizSet> cloneSelectedVizSetMap(){
		TreeMap<Integer, VizSet> map = new TreeMap<Integer, VizSet>();
		Iterator<Integer> itr = selectedVizSetMap.keySet().iterator();
		while(itr.hasNext()){
			int index = itr.next();
			VizSet is = selectedVizSetMap.get(index);
			map.put(index, is.clone());
		}
		return map;
	}
	
	/* (non-Javadoc)
	 * @see org.bellerophon.data.feature.VizData#initialize()
	 */
	public void initialize(){
		super.initialize();
		selectedVizSetMap = null;
	}
	
	/**
	 * Gets the selected viz set map.
	 *
	 * @return the selected viz set map
	 */
	public TreeMap<Integer, VizSet> getSelectedVizSetMap(){return selectedVizSetMap;}
	
	/**
	 * Sets the selected viz set map.
	 *
	 * @param selectedVizSetMap the selected viz set map
	 */
	public void setSelectedVizSetMap(TreeMap<Integer, VizSet> selectedVizSetMap){this.selectedVizSetMap = selectedVizSetMap;}
	
}