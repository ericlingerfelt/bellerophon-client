/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: VizSetFilterPanel.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.viz;

import info.clearthought.layout.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import org.bellerophon.data.util.*;
import org.bellerophon.gui.format.Borders;
import org.bellerophon.gui.format.Buttons;
import org.bellerophon.gui.format.Colors;
import org.bellerophon.gui.format.Icons;

/**
 * The Class VizSetFilterPanel.
 *
 * @author Eric J. Lingerfelt
 */
public class VizSetFilterPanel extends JPanel implements ActionListener{
	
	private JComboBox indexBox, vizSetIdBox, progenitorBox, creatorBox;
	private JButton filterButton;
	private boolean filter;
	private VizSetFilterListener vsfl;
	
	/**
	 * Instantiates a new viz set filter panel.
	 *
	 * @param vsfl the vsfl
	 */
	public VizSetFilterPanel(VizSetFilterListener vsfl){
		
		this.vsfl = vsfl;
		
		JLabel indexLabel = new JLabel("Index");
		JLabel vizSetIdLabel = new JLabel("Unique ID");
		JLabel creatorLabel = new JLabel("Created By");
		JLabel progenitorLabel = new JLabel("Progenitor");
		
		double[] col = {5, TableLayoutConstants.FILL, 5};
		double[] row = {5, TableLayoutConstants.PREFERRED
						, 20, TableLayoutConstants.PREFERRED, 5};
		setLayout(new TableLayout(col, row));
		
		double[] colBox = {TableLayoutConstants.FILL};
		double[] rowBox = {TableLayoutConstants.PREFERRED
							, 5, TableLayoutConstants.FILL
							, 10, TableLayoutConstants.PREFERRED
							, 5, TableLayoutConstants.FILL
							, 10, TableLayoutConstants.PREFERRED
							, 5, TableLayoutConstants.FILL
							, 10, TableLayoutConstants.PREFERRED
							, 5, TableLayoutConstants.FILL};
		JPanel boxPanel = new JPanel();
		boxPanel.setLayout(new TableLayout(colBox, rowBox));
		
		indexBox = new JComboBox();
		vizSetIdBox = new JComboBox();
		vizSetIdBox.setPrototypeDisplayValue("1234567890");
		creatorBox = new JComboBox();
		progenitorBox = new JComboBox();
		progenitorBox.setPrototypeDisplayValue("1234567890");
		
		filterButton = Buttons.getIconButton("Search Filter OFF"
												, "icons/process-stop.png"
												, Buttons.IconPosition.RIGHT
												, Colors.RED
												, this
												, new Dimension(100, 50));	
		
		boxPanel.add(indexLabel,             "0, 0, l, c");
		boxPanel.add(indexBox,               "0, 2, f, c");
		boxPanel.add(vizSetIdLabel,          "0, 4, l, c");
		boxPanel.add(vizSetIdBox,            "0, 6, f, c");
		boxPanel.add(creatorLabel,           "0, 8, l, c");
		boxPanel.add(creatorBox,             "0, 10, f, c");
		boxPanel.add(progenitorLabel,        "0, 12, l, c");
		boxPanel.add(progenitorBox,          "0, 14, f, c");
		
		add(boxPanel, "1, 1, f, c");
		add(filterButton, "1, 3, f, f");
		
		setBorder(Borders.getBorder("Search Filter"));
	}
	
	/**
	 * Sets the current state.
	 *
	 * @param map the map
	 */
	public void setCurrentState(TreeMap<Integer, VizSet> map){
		
		vsfl.setFilter(new VizSetFilter());
		filterButton.setText("Search Filter OFF");
		filterButton.setIcon(Icons.createImageIcon("/resources/images/icons/process-stop.png"));
		filterButton.setForeground(Colors.RED);
		filter = false;
		vsfl.setFilter(new VizSetFilter());
		
		indexBox.removeActionListener(this);
		vizSetIdBox.removeActionListener(this);
		progenitorBox.removeActionListener(this);
		creatorBox.removeActionListener(this);
		
		indexBox.removeAllItems();
		vizSetIdBox.removeAllItems();
		progenitorBox.removeAllItems();
		creatorBox.removeAllItems();
		
		Iterator<VizSet> itr = map.values().iterator();
		
		ArrayList<Integer> indexList = new ArrayList<Integer>();
		ArrayList<String> vizSetIdList = new ArrayList<String>();
		ArrayList<String> progenitorList = new ArrayList<String>();
		ArrayList<User> creatorList = new ArrayList<User>();
		
		while(itr.hasNext()){
			VizSet vs = itr.next();
			if(!indexList.contains(vs.getIndex())){
				indexList.add(vs.getIndex());
			}
			if(!progenitorList.contains(vs.getProgenitor())){
				progenitorList.add(vs.getProgenitor());
			}
			if(!creatorList.contains(vs.getCreator())){
				creatorList.add(vs.getCreator());
			}
			if(!vizSetIdList.contains(vs.getVizSetId())){
				vizSetIdList.add(vs.getVizSetId());
			}
		}
		
		Collections.sort(indexList);
		Collections.sort(vizSetIdList);
		Collections.sort(progenitorList);
		Collections.sort(creatorList);
		
		indexBox.addItem("-");
		for(Integer i: indexList){
			indexBox.addItem(i);
		}
		
		vizSetIdBox.addItem("-");
		for(String s: vizSetIdList){
			vizSetIdBox.addItem(s);
		}

		progenitorBox.addItem("-");
		for(String s: progenitorList){
			progenitorBox.addItem(s);
		}
		
		creatorBox.addItem("-");
		for(User u: creatorList){
			creatorBox.addItem(u);
		}
		
		indexBox.addActionListener(this);
		vizSetIdBox.addActionListener(this);
		progenitorBox.addActionListener(this);
		creatorBox.addActionListener(this);
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent ae){
		if(ae.getSource()==filterButton){
			if(!filter){
				filterButton.setText("Search Filter ON");
				filterButton.setIcon(Icons.createImageIcon("/resources/images/icons/system-search.png"));
				filterButton.setForeground(Colors.BLUE);
				filter = true;
				applyFilter();
			}else{
				vsfl.setFilter(new VizSetFilter());
				filterButton.setText("Search Filter OFF");
				filterButton.setIcon(Icons.createImageIcon("/resources/images/icons/process-stop.png"));
				filterButton.setForeground(Colors.RED);
				filter = false;
				vsfl.setFilter(new VizSetFilter());
			}
		}else if(ae.getSource()==indexBox 
					|| ae.getSource()==vizSetIdBox
					|| ae.getSource()==progenitorBox
					|| ae.getSource()==creatorBox){
			if(filter){
				applyFilter();
			}
		}
	}
	
	/**
	 * Apply filter.
	 */
	private void applyFilter(){
		Integer index = indexBox.getSelectedItem().toString().equals("-") ? -1 : (Integer)indexBox.getSelectedItem();
		String vizSetId = vizSetIdBox.getSelectedItem().toString().equals("-") ? "" : (String)vizSetIdBox.getSelectedItem();
		String progenitor = progenitorBox.getSelectedItem().toString().equals("-") ? null : (String)progenitorBox.getSelectedItem();
		User creator = creatorBox.getSelectedItem().toString().equals("-") ? null : (User)creatorBox.getSelectedItem();
		
		VizSetFilter vsf = new VizSetFilter();
		vsf.setIndex(index);
		vsf.setCreator(creator);
		vsf.setProgenitor(progenitor);
		vsf.setVizSetId(vizSetId);
		vsfl.setFilter(vsf);
	}
	
}

