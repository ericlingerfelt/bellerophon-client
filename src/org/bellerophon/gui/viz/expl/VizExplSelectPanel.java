/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: VizExplSelectPanel.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.viz.expl;

import info.clearthought.layout.*;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.*;
import java.util.TreeMap;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.bellerophon.data.feature.*;
import org.bellerophon.data.util.*;
import org.bellerophon.gui.dialog.DelayDialog;
import org.bellerophon.gui.dialog.MessageDialog;
import org.bellerophon.gui.format.Borders;
import org.bellerophon.gui.format.Buttons;
import org.bellerophon.gui.format.Colors;
import org.bellerophon.gui.viz.VizSetFilterListener;
import org.bellerophon.gui.viz.VizSetFilterPanel;
import org.bellerophon.gui.viz.VizSetTable;
import org.bellerophon.gui.viz.expl.VizExplPanel.VizExplMode;
import org.bellerophon.gui.viz.expl.worker.GetVizSetAnimationDataWorker;
import org.bellerophon.gui.viz.expl.worker.UpdateVizSetDataWorker;

public class VizExplSelectPanel extends JPanel implements ActionListener
															, MouseListener
															, ListSelectionListener
															, VizSetFilterListener{

	private VizSetTable table;
	private VizSetFilterPanel filterPanel;
	private VizSetFilter filter = new VizSetFilter();
	private JButton selectButton, refreshButton;
	private VizExplPanel parent;
	private VizExplData d;
	private Frame frame;
	private JTextArea notesArea;
	private DelayDialog delayDialog;
	
	public VizExplSelectPanel(Frame frame, VizExplPanel parent, VizExplData d){
		
		this.frame = frame;
		this.parent = parent;
		this.d = d;
		
		selectButton = Buttons.getIconButton("Explore Viz Sets"
												, "icons/go-next.png"
												, Buttons.IconPosition.RIGHT
												, Colors.GREEN
												, this
												, new Dimension(200, 50));
		
		refreshButton = Buttons.getIconButton("Refresh Viz Sets"
												, "icons/view-refresh.png"
												, Buttons.IconPosition.RIGHT
												, Colors.BLUE
												, this
												, new Dimension(200, 50));
		
		filterPanel = new VizSetFilterPanel(this);
		
		notesArea = new JTextArea();
		notesArea.setWrapStyleWord(true);
		notesArea.setLineWrap(true);
		notesArea.setEditable(false);
		JScrollPane notesPane = new JScrollPane(notesArea);
		JPanel notesPanel = new JPanel();
		double[] colNotes = {5, TableLayoutConstants.FILL, 5};
		double[] rowNotes = {5, TableLayoutConstants.FILL, 5};
		notesPanel.setLayout(new TableLayout(colNotes, rowNotes));
		notesPanel.add(notesPane, "1, 1, f, f");
		notesPanel.setBorder(Borders.getBorder("Visualization Set Notes"));
		notesPanel.setPreferredSize(new Dimension(1000, 200));
		
		table = new VizSetTable(false);
		table.addMouseListener(this);
		table.getSelectionModel().addListSelectionListener(this);
		JScrollPane tablePane = new JScrollPane(table);
		JPanel tablePanel = new JPanel();
		double[] colTable = {5, TableLayoutConstants.FILL, 5};
		double[] rowTable = {5, TableLayoutConstants.FILL, 5};
		tablePanel.setLayout(new TableLayout(colTable, rowTable));
		tablePanel.add(tablePane, "1, 1, f, f");
		tablePanel.setBorder(Borders.getBorder("Visualization Sets"));
		
		JSplitPane jsp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, tablePanel, notesPanel);
		jsp.setBorder(null);
		jsp.setResizeWeight(1.0);
		jsp.setDividerLocation(503);
		
		double[] colRight = {TableLayoutConstants.FILL};
		double[] rowRight = {TableLayoutConstants.PREFERRED
								, 10, TableLayoutConstants.PREFERRED
								, 10, TableLayoutConstants.FILL};
		JPanel rightPanel = new JPanel(new TableLayout(colRight, rowRight));
		rightPanel.add(filterPanel, 	"0, 0, f, f");
		rightPanel.add(refreshButton, 	"0, 2, r, b");
		rightPanel.add(selectButton, 	"0, 4, r, b");
		
		double[] col = {TableLayoutConstants.FILL, 10, TableLayoutConstants.PREFERRED};
		double[] row = {TableLayoutConstants.FILL};
		setLayout(new TableLayout(col, row));
		add(jsp,  "0, 0, f, f");
		add(rightPanel, "2, 0, f, f");
		
		String string = "Please wait while visualization data is loaded.";
		delayDialog = new DelayDialog(frame, string, "Please wait...");
		
	}

	public void setFilter(VizSetFilter filter){
		this.filter = filter;
		setTableState(false);
	}

	public void actionPerformed(ActionEvent ae){
		if(ae.getSource()==selectButton){
			if(table.getSelectedRowCount()>0){
				delayDialog.open();
				GetVizSetAnimationDataWorker worker = new GetVizSetAnimationDataWorker(frame, this, parent, delayDialog);
				worker.execute();
			}else{
				MessageDialog.createMessageDialog(frame, "Please select at least one visualization set from the table to explore.", "Attention!");
			}
		}else if(ae.getSource()==refreshButton){
			UpdateVizSetDataWorker worker = new UpdateVizSetDataWorker(frame, parent);
			worker.execute();
		}
	}

	public void exploreSelectedVizSets(){
		if(table.getSelectedRowCount()>0){
			TreeMap<Integer, VizSet> selectedVizSetMap = new TreeMap<Integer, VizSet>();
			d.setSelectedVizSetMap(selectedVizSetMap);
			for(int row: table.getSelectedRows()){
				int index = (Integer)table.getDataVector().get(table.convertRowIndexToModel(row)).get(0);
				selectedVizSetMap.put(index, d.getVizSetMap().get(index));
			}
			table.setSelectedVizSetIndices(selectedVizSetMap);
			parent.setCurrentState(VizExplMode.VIEW);
		}else{
			MessageDialog.createMessageDialog(frame, "Please select at least one visualization set from the table to explore.", "Attention!");
		}
	}
	
	public void setCurrentState(){
		setTableState(true);
		setFilterState();
	}
	
	public void setTableState(boolean selectFirstrow){
		table.setCurrentState(d.getVizSetMap(), filter);
		if(table.getDataVector().size()>0 && selectFirstrow){
			table.setRowSelectionInterval(0, 0);
		}
	}
	
	private void setFilterState(){
		filterPanel.setCurrentState(d.getVizSetMap());
	}
	
	public void mouseClicked(MouseEvent me){
		if(me.getClickCount()==2){
			delayDialog.open();
			GetVizSetAnimationDataWorker worker = new GetVizSetAnimationDataWorker(frame, this, parent, delayDialog);
			worker.execute();
		}
	}
	
	public void mouseEntered(MouseEvent me){}
	public void mouseExited(MouseEvent me){}
	public void mousePressed(MouseEvent me){}
	public void mouseReleased(MouseEvent me){}

	public void valueChanged(ListSelectionEvent e) {
		if(table.getSelectedRowCount()==0){
			notesArea.setText("");
		}else{
			int index = (Integer)table.getDataVector().get(table.getSelectedRow()).get(0);
			notesArea.setText(d.getVizSetMap().get(index).getNotes());
		}
	}

	public void updateComponents() {
		setTableState(false);
	}
	
}