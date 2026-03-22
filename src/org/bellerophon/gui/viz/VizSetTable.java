/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: VizSetTable.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.viz;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;
import java.text.DecimalFormat;

import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import org.bellerophon.data.util.User;
import org.bellerophon.data.util.VizSet;
import org.bellerophon.data.util.VizSetFilter;
import org.bellerophon.gui.table.DefaultTable;

/**
 * The Class VizSetTable.
 *
 * @author Eric J. Lingerfelt
 */
public class VizSetTable extends DefaultTable{
	
	private Vector<String> colNamesVector;
	private VizSetTableModel model;
	private TableRowSorter sorter;
	private ArrayList<Integer> selectedVizSetIndices;
	
	/**
	 * Instantiates a new viz set table.
	 *
	 * @param useSingleSelection the use single selection
	 */
	public VizSetTable(boolean useSingleSelection){
		
		selectedVizSetIndices = new ArrayList<Integer>();
		
		colNamesVector = new Vector<String>();
		colNamesVector.add("Index");
		colNamesVector.add("Viz Set ID");
		colNamesVector.add("Created By");
		colNamesVector.add("Progenitor");
		colNamesVector.add("Bounce Time");
		colNamesVector.add("Elapsed Time");
		colNamesVector.add("Last Frame");
		colNamesVector.add("Last Time Stamp");
		
		if(useSingleSelection){
			setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		}else{
			setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		}
		model = new VizSetTableModel();
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
	public void setCurrentState(TreeMap<Integer, VizSet> map, VizSetFilter filter){
		ArrayList<Integer> selectedRowList = new ArrayList<Integer>();
		int row = 0;
		DecimalFormat df = new DecimalFormat("0.000");
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
				VizSet vs = map.get(key);
				if(filter.applyFilter(vs)){
					if(selectedVizSetIndices.contains(vs.getIndex())){
						selectedRowList.add(row);
					}
					v.add(vs.getIndex());
					v.add(vs.getVizSetId());
					v.add(vs.getCreator());
					v.add(vs.getProgenitor());
					v.add(df.format(vs.getBounceTime()));
					v.add(df.format(vs.getElapsedTime()));
					v.add(vs.getLastFrame());
					v.add(vs.getTimeStampDate());
					dataVector.add(v);
					row++;
				}
			}
		}
		model.setDataVector(dataVector, colNamesVector);
		sorter.setSortKeys(sortKeys);
		for(Integer r: selectedRowList){
			addRowSelectionInterval(r, r);
		}
	}
	
	public void setSelectedVizSetIndices(TreeMap<Integer, VizSet> selectedVizSetMap){
		selectedVizSetIndices = new ArrayList<Integer>();
		Iterator<Integer> itr = selectedVizSetMap.keySet().iterator();
		while(itr.hasNext()){
			selectedVizSetIndices.add(itr.next());
		}
	}
	
	public void setSelectedVizSetIndex(int selectedVizSetIndex){
		selectedVizSetIndices = new ArrayList<Integer>();
		selectedVizSetIndices.add(selectedVizSetIndex);
	}
	
}

class VizSetTableModel extends DefaultTableModel{
	
	public Class<?> getColumnClass(int c){
    	if(c==0){
    		return Integer.class;
    	}else if(c==1){
    		return String.class;
    	}else if(c==2){
    		return User.class;
    	}else if(c==3){
    		return String.class;
    	}else if(c==4){
    		return String.class;
    	}else if(c==5){
    		return String.class;
    	}else if(c==6){
    		return Integer.class;
    	}else{
    		return Calendar.class;
    	}
	}

    public boolean isCellEditable(int row, int col){
    	return false;
	}
	
}

