/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: VizManData.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.data.feature;

import org.bellerophon.data.Data;
import org.bellerophon.data.util.*;

/**
 * The Class VizManData is the main data structure for the Viz Set Manager tool.
 *
 * @author Eric J. Lingerfelt
 */
public class VizManData extends VizData implements Data{
		
	private VizSet vizSet;
	private boolean vizSetIdExists;
	private String vizSetId;
	private VizCreateState state;
	
	/**
	 * The Enum VizCreateState.
	 *
	 * @author Eric J. Lingerfelt
	 */
	public enum VizCreateState{CREATE, MOD}
	
	/**
	 * The Constructor.
	 */
	public VizManData(){
		initialize();
	}
	
	/* (non-Javadoc)
	 * @see org.bellerophon.data.feature.VizData#clone()
	 */
	public VizManData clone(){
		VizManData vcd = new VizManData();
		vcd.vizSetMap = super.cloneVizSetMap();
		vcd.state = state;
		vcd.vizSet = vizSet.clone();
		vcd.vizSetIdExists = vizSetIdExists;
		vcd.vizSetId = vizSetId;
		return vcd;
	}
	
	/* (non-Javadoc)
	 * @see org.bellerophon.data.feature.VizData#initialize()
	 */
	public void initialize(){
		super.initialize();
		vizSet = null;
		vizSetIdExists = false;
		vizSetId = "";
		state = null;
	}

	/**
	 * Gets the viz set.
	 *
	 * @return the viz set
	 */
	public VizSet getVizSet(){return vizSet;}
	
	/**
	 * Sets the viz set.
	 *
	 * @param vizSet the new viz set
	 */
	public void setVizSet(VizSet vizSet){this.vizSet = vizSet;}
	
	/**
	 * Gets the state.
	 *
	 * @return the state
	 */
	public VizCreateState getState(){return state;}
	
	/**
	 * Sets the state.
	 *
	 * @param state the new state
	 */
	public void setState(VizCreateState state){this.state = state;}
	
	/**
	 * Gets the viz set id exists.
	 *
	 * @return the viz set id exists
	 */
	public boolean getVizSetIdExists(){return vizSetIdExists;}
	
	/**
	 * Sets the viz set id exists.
	 *
	 * @param vizSetIdExists the new viz set id exists
	 */
	public void setVizSetIdExists(boolean vizSetIdExists){this.vizSetIdExists = vizSetIdExists;}
	
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
	
}

