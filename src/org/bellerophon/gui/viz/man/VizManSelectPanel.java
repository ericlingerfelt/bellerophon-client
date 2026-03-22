/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: VizManSelectPanel.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.viz.man;

import info.clearthought.layout.*;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.bellerophon.data.feature.*;
import org.bellerophon.data.util.*;
import org.bellerophon.enums.VizSetType;
import org.bellerophon.gui.dialog.CautionDialog;
import org.bellerophon.gui.dialog.MessageDialog;
import org.bellerophon.gui.dialog.RewindDialog;
import org.bellerophon.gui.format.Borders;
import org.bellerophon.gui.format.Buttons;
import org.bellerophon.gui.format.Colors;
import org.bellerophon.gui.viz.VizSetFilterListener;
import org.bellerophon.gui.viz.VizSetFilterPanel;
import org.bellerophon.gui.viz.VizSetTable;
import org.bellerophon.gui.viz.man.VizManPanel.VizManMode;
import org.bellerophon.gui.viz.man.worker.DeleteVizSetWorker;
import org.bellerophon.gui.viz.man.worker.RewindVizSetWorker;

/**
 * The Class VizManSelectPanel.
 *
 * @author Eric J. Lingerfelt
 */
public class VizManSelectPanel extends JPanel implements ActionListener
															, MouseListener
															, ListSelectionListener
															, VizSetFilterListener{

	private VizSetTable table;
	private VizSetFilterPanel filterPanel;
	private VizSetFilter filter = new VizSetFilter();
	private JButton selectButton, backButton, deleteButton, rewindButton;
	private VizManPanel parent;
	private VizManData d;
	private Frame frame;
	private JTextArea notesArea;
	private JPanel rightPanel;
	private VizManMode mode;
	
	/**
	 * Instantiates a new viz create select panel.
	 *
	 * @param frame the frame
	 * @param parent the parent
	 * @param d the d
	 */
	public VizManSelectPanel(Frame frame, VizManPanel parent, VizManData d){
		
		this.frame = frame;
		this.parent = parent;
		this.d = d;
		
		selectButton = Buttons.getIconButton("Edit Visualization Set"
												, "icons/go-next.png"
												, Buttons.IconPosition.RIGHT
												, Colors.GREEN
												, this
												, new Dimension(250, 50));
		
		backButton = Buttons.getIconButton("Select Different Option"
												, "icons/go-previous.png"
												, Buttons.IconPosition.LEFT
												, Colors.GREEN
												, this
												, new Dimension(250, 50));
		
		deleteButton = Buttons.getIconButton("Delete Visualization Set"
												, "icons/edit-delete.png"
												, Buttons.IconPosition.RIGHT
												, Colors.BLUE
												, this
												, new Dimension(250, 50));
		
		rewindButton = Buttons.getIconButton("Rewind Visualization Set"
											, "icons/media-seek-backward.png"
											, Buttons.IconPosition.RIGHT
											, Colors.BLACK
											, this
											, new Dimension(250, 50));
		
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
		
		table = new VizSetTable(true);
		table.addMouseListener(this);
		table.getSelectionModel().addListSelectionListener(this);
		JScrollPane tablePane = new JScrollPane(table);
		JPanel tablePanel = new JPanel();
		double[] colTable = {5, TableLayoutConstants.FILL, 5};
		double[] rowTable = {5, TableLayoutConstants.FILL, 5};
		tablePanel.setLayout(new TableLayout(colTable, rowTable));
		tablePanel.add(tablePane, "1, 1, f, f");
		tablePanel.setBorder(Borders.getBorder("Visualization Sets"));
		
		double[] colLeft = {TableLayoutConstants.FILL};
		double[] rowLeft = {TableLayoutConstants.FILL
								, 10, TableLayoutConstants.PREFERRED
								, 10, TableLayoutConstants.PREFERRED};
		JPanel leftPanel = new JPanel(new TableLayout(colLeft, rowLeft));
		leftPanel.add(tablePanel, "0, 0, f, f");
		leftPanel.add(notesPanel, "0, 2, f, f");
		leftPanel.add(backButton, "0, 4, l, c");
		
		double[] colRight = {TableLayoutConstants.FILL};
		double[] rowRight = {TableLayoutConstants.PREFERRED
								, 10, TableLayoutConstants.FILL
								, 10, TableLayoutConstants.PREFERRED};
		rightPanel = new JPanel(new TableLayout(colRight, rowRight));
		
		double[] col = {TableLayoutConstants.FILL, 10, TableLayoutConstants.PREFERRED};
		double[] row = {TableLayoutConstants.FILL};
		setLayout(new TableLayout(col, row));
		add(leftPanel, "0, 0, f, f");
		add(rightPanel, "2, 0, f, f");
	}

	/* (non-Javadoc)
	 * @see org.bellerophon.gui.viz.VizSetFilterListener#setFilter(org.bellerophon.data.util.VizSetFilter)
	 */
	public void setFilter(VizSetFilter filter){
		this.filter = filter;
		setTableState();
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent ae){
		if(ae.getSource()==selectButton){
			if(table.getSelectedRowCount()>0){
				editSelectedVizSet();
			}else{
				MessageDialog.createMessageDialog(frame, "Please select a visualization set from the table to edit.", "Attention!");
			}
		}else if(ae.getSource()==deleteButton){
			if(table.getSelectedRowCount()>0){
				deleteSelectedVizSet();
			}else{
				MessageDialog.createMessageDialog(frame, "Please select a visualization set from the table to delete.", "Attention!");
			}
		}else if(ae.getSource()==rewindButton){
			if(table.getSelectedRowCount()>0){
				rewindSelectedVizSet();
			}else{
				MessageDialog.createMessageDialog(frame, "Please select a visualization set from the table to rewind.", "Attention!");
			}
		}else if(ae.getSource()==backButton){
			parent.setCurrentState(VizManMode.OPTION);
		}
	}
	
	private void rewindSelectedVizSet(){
		if(table.getSelectedRowCount()>0){
			int row = table.getSelectedRow();
			int index = (Integer)table.getDataVector().get(table.convertRowIndexToModel(row)).get(0);
			VizSet vs = d.getVizSetMap().get(index);
			if(vs.getVizSetType()==VizSetType.CHIMERA2D){
				if(vs.getLastFrame()>0){
					table.setSelectedVizSetIndex(index);
					int newLastFrame = RewindDialog.createRewindDialog(frame, vs);
					if(newLastFrame!=-1){
						String string = "Are you sure you want to rewind visualization set number " + vs.getIndex() + ": " + vs.getVizSetId() + " to frame " + newLastFrame + "?";
						int returnValue = CautionDialog.createCautionDialog(frame, string, "Attention!");
						if(returnValue==CautionDialog.YES){
							vs.setNewLastFrame(newLastFrame);
							RewindVizSetWorker worker = new RewindVizSetWorker(frame, this, vs, newLastFrame);
							worker.execute();
						}
					}
				}else{
					MessageDialog.createMessageDialog(frame, "There are no frames in the selected visualization set to rewind.", "Attention!");
				}
			}else{
				MessageDialog.createMessageDialog(frame, "Chimera 3D visualization sets can not be rewound since they only contain uploaded animations.", "Attention!");
			}
		}else{
			MessageDialog.createMessageDialog(frame, "Please select a visualization set from the table to rewind.", "Attention!");
		}
	}
	
	public void updateAfterRewind(){
		parent.updateVizExplPanelAfterVizSetModification(d.getVizSetMap());
		table.setCurrentState(d.getVizSetMap(), filter);
	}
	
	public void updateAfterDelete(VizSet vs){
		d.getVizSetMap().remove(vs.getIndex());
		parent.updateVizExplPanelAfterVizSetModification(d.getVizSetMap());
		setCurrentState(mode);
	}
	
	private void deleteSelectedVizSet(){
		if(table.getSelectedRowCount()>0){
			int row = table.getSelectedRow();
			int index = (Integer)table.getDataVector().get(table.convertRowIndexToModel(row)).get(0);
			VizSet vs = d.getVizSetMap().get(index);
			int returnValue = CautionDialog.createCautionDialog(frame, "Are you sure you want to delete visualization set number "+ vs.getIndex() + ": " + vs.getVizSetId() + "?", "Attention!");
			if(returnValue==CautionDialog.YES){
				DeleteVizSetWorker worker = new DeleteVizSetWorker(frame, this, vs);
				worker.execute();
			}
		}else{
			MessageDialog.createMessageDialog(frame, "Please select a visualization set from the table to delete.", "Attention!");
		}
	}
	
	/**
	 * Edits the selected viz set.
	 */
	private void editSelectedVizSet(){
		if(table.getSelectedRowCount()>0){
			int row = table.getSelectedRow();
			int index = (Integer)table.getDataVector().get(table.convertRowIndexToModel(row)).get(0);
			d.setVizSet(d.getVizSetMap().get(index));
			table.setSelectedVizSetIndex(index);
			parent.setCurrentState(VizManMode.VIZ_SET);
		}else{
			MessageDialog.createMessageDialog(frame, "Please select a visualization set from the table to edit.", "Attention!");
		}
	}
	
	/**
	 * Sets the current state.
	 */
	public void setCurrentState(VizManMode mode){
		this.mode = mode;
		setTableState();
		setFilterState();
		setRightPanelState();
	}
	
	private void setRightPanelState(){
		rightPanel.removeAll();
		if(mode==VizManMode.SELECT){
			rightPanel.add(filterPanel, 	"0, 0, f, f");
			rightPanel.add(selectButton, 	"0, 4, r, c");
		}else if(mode==VizManMode.DELETE){
			rightPanel.add(filterPanel, 	"0, 0, f, f");
			rightPanel.add(deleteButton, 	"0, 4, r, c");
		}else if(mode==VizManMode.REWIND){
			rightPanel.add(filterPanel, 	"0, 0, f, f");
			rightPanel.add(rewindButton, 	"0, 4, r, c");
		}
		validate();
		repaint();
	}
	
	/**
	 * Sets the table state.
	 */
	private void setTableState(){
		table.setCurrentState(d.getVizSetMap(), filter);
		if(table.getDataVector().size()>0){
			table.setRowSelectionInterval(0, 0);
		}
	}
	
	/**
	 * Sets the filter state.
	 */
	private void setFilterState(){
		filterPanel.setCurrentState(d.getVizSetMap());
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent me){
		if(me.getClickCount()==2){
			if(mode==VizManMode.SELECT){
				editSelectedVizSet();
			}else if(mode==VizManMode.REWIND){
				rewindSelectedVizSet();
			}else if(mode==VizManMode.DELETE){
				deleteSelectedVizSet();
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent me){}
	
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent me){}
	
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent me){}
	
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent me){}

	/* (non-Javadoc)
	 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
	 */
	public void valueChanged(ListSelectionEvent e) {
		if(table.getSelectedRowCount()==0){
			notesArea.setText("");
		}else{
			int index = (Integer)table.getDataVector().get(table.getSelectedRow()).get(0);
			notesArea.setText(d.getVizSetMap().get(index).getNotes());
		}
	}
	
}