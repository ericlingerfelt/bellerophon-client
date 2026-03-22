/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: TestExplData.java
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
 * The Class TestExplData is the main data structure for the Regression Test Explorer tool.
 *
 * @author Eric J. Lingerfelt
 */
public class TestExplData implements Data{

	private TreeMap<Integer, RegressionTest> testMap;
	private TreeMap<Integer, RegressionTest> selectedTestMap;
	
	/**
	 * The Constructor.
	 */
	public TestExplData(){
		initialize();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public TestExplData clone(){
		TestExplData ed = new TestExplData();
		ed.testMap = cloneTestMap();
		ed.selectedTestMap = cloneSelectedTestMap();
		return ed;
	}
	
	/**
	 * Clone selected test map.
	 *
	 * @return the tree map
	 */
	private TreeMap<Integer, RegressionTest> cloneSelectedTestMap(){
		TreeMap<Integer, RegressionTest> map = new TreeMap<Integer, RegressionTest>();
		Iterator<Integer> itr = selectedTestMap.keySet().iterator();
		while(itr.hasNext()){
			int index = itr.next();
			RegressionTest rt = selectedTestMap.get(index);
			map.put(index, rt.clone());
		}
		return map;
	}
	
	/**
	 * Clone test map.
	 *
	 * @return the tree map
	 */
	private TreeMap<Integer, RegressionTest> cloneTestMap(){
		TreeMap<Integer, RegressionTest> map = new TreeMap<Integer, RegressionTest>();
		Iterator<Integer> itr = testMap.keySet().iterator();
		while(itr.hasNext()){
			int index = itr.next();
			RegressionTest rt = testMap.get(index);
			map.put(index, rt.clone());
		}
		return map;
	}
	
	/* (non-Javadoc)
	 * @see org.bellerophon.data.Data#initialize()
	 */
	public void initialize(){
		testMap = null;
		selectedTestMap = null;
	}
	
	/**
	 * Gets the selected test map.
	 *
	 * @return the selected test map
	 */
	public TreeMap<Integer, RegressionTest> getSelectedTestMap(){return selectedTestMap;}
	
	/**
	 * Sets the selected test map.
	 *
	 * @param selectedTestMap the selected test map
	 */
	public void setSelectedTestMap(TreeMap<Integer, RegressionTest> selectedTestMap){this.selectedTestMap = selectedTestMap;}
	
	/**
	 * Gets the test map.
	 *
	 * @return the test map
	 */
	public TreeMap<Integer, RegressionTest> getTestMap(){return testMap;}
	
	/**
	 * Sets the test map.
	 *
	 * @param testMap the test map
	 */
	public void setTestMap(TreeMap<Integer, RegressionTest> testMap){this.testMap = testMap;}
	
}
