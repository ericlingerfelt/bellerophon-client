/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: RegressionTestTable.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import org.bellerophon.data.util.RegressionTest;
import org.bellerophon.data.util.RegressionTestFilter;
import org.bellerophon.enums.CodeType;
import org.bellerophon.enums.Platform;
import org.bellerophon.enums.RegressionTestResult;
import org.bellerophon.gui.table.DefaultTable;

/**
 * The Class RegressionTestTable.
 *
 * @author Eric J. Lingerfelt
 */
public class RegressionTestTable extends DefaultTable{
	
	private Vector<String> colNamesVector;
	private RegressionTestTableModel model;
	private TableRowSorter sorter;
	
	/**
	 * Instantiates a new regression test table.
	 */
	public RegressionTestTable(){
		
		colNamesVector = new Vector<String>();
		colNamesVector.add("Index");
		colNamesVector.add("Revision");
		colNamesVector.add("Code Type");
		colNamesVector.add("Start Date");
		colNamesVector.add("Platform");
		colNamesVector.add("Result");

		setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		model = new RegressionTestTableModel();
		setModel(model);
		
		sorter = new TableRowSorter();
		sorter.setSortsOnUpdates(true);
		sorter.setModel(model);
		setRowSorter(sorter);
	}
	
	/**
	 * Gets the data vector.
	 *
	 * @return the data vector
	 */
	public Vector<Vector<Object>> getDataVector(){
		if(isEditing()){
			for(int i=0; i<getRowCount(); i++){
				for(int j=0; j<getColumnCount(); j++){
					if(isCellEditable(i, j)){
						getCellEditor(i, j).stopCellEditing();
					}
				}
			}
		}
		return model.getDataVector();
	} 
	
	/**
	 * Sets the current state.
	 *
	 * @param map the map
	 * @param filter the filter
	 */
	public void setCurrentState(TreeMap<Integer, RegressionTest> map, RegressionTestFilter filter){
		List sortKeys = sorter.getSortKeys();
		Vector<Vector<Object>> dataVector = new Vector<Vector<Object>>();
		if(map!=null){
			ArrayList<Integer> keyList = new ArrayList<Integer>();
			Iterator<Integer> itr = map.keySet().iterator();
			while(itr.hasNext()){
				keyList.add(itr.next());
			}
			Collections.sort(keyList, Collections.reverseOrder());
			
			for(int key: keyList){
				Vector<Object> v = new Vector<Object>();
				RegressionTest rt = map.get(key);
				if(filter.applyFilter(rt)){
					v.add(rt.getIndex());
					v.add(rt.getRevision());
					v.add(rt.getCodeType());
					v.add(rt.getCheckoutDate());
					v.add(rt.getPlatform());
					v.add(rt.getResult());
					dataVector.add(v);
				}
			}
		}
		model.setDataVector(dataVector, colNamesVector);
		sorter.setSortKeys(sortKeys);
	}
	
}

class RegressionTestTableModel extends DefaultTableModel{
	
	public Class<?> getColumnClass(int c){
    	if(c==0 || c==1){
    		return Integer.class;
    	}else if(c==2){
    		return CodeType.class;
    	}else if(c==3){
    		return Calendar.class;
    	}else if(c==4){
    		return Platform.class;
    	}else{
    		return RegressionTestResult.class;
    	}
	}

    public boolean isCellEditable(int row, int col){
    	return false;
	}
	
}

