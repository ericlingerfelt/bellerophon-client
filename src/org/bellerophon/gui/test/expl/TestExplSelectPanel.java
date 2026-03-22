/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: TestExplSelectPanel.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.test.expl;

import info.clearthought.layout.*;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import org.bellerophon.data.feature.*;
import org.bellerophon.data.util.*;
import org.bellerophon.enums.Action;
import org.bellerophon.exception.CaughtExceptionHandler;
import org.bellerophon.gui.dialog.DelayDialog;
import org.bellerophon.gui.dialog.ErrorResultDialog;
import org.bellerophon.gui.dialog.MessageDialog;
import org.bellerophon.gui.format.Borders;
import org.bellerophon.gui.format.Buttons;
import org.bellerophon.gui.format.Colors;
import org.bellerophon.gui.test.RegressionTestFilterPanel;
import org.bellerophon.gui.test.RegressionTestTable;
import org.bellerophon.gui.test.expl.worker.*;
import org.bellerophon.io.WebServiceCom;

/**
 * The Class TestExplSelectPanel.
 *
 * @author Eric J. Lingerfelt
 */
public class TestExplSelectPanel extends JPanel implements ActionListener, MouseListener{

	private RegressionTestTable table;
	private RegressionTestFilterPanel filterPanel;
	private RegressionTestFilter filter = new RegressionTestFilter();
	private JButton reloadButton, viewButton;
	private TestExplPanel parent;
	private TestExplData d;
	private Frame frame;
	
	/**
	 * Instantiates a new test expl select panel.
	 *
	 * @param frame the frame
	 * @param parent the parent
	 * @param d the d
	 */
	public TestExplSelectPanel(Frame frame, TestExplPanel parent, TestExplData d){
		
		this.frame = frame;
		this.parent = parent;
		this.d = d;
		
		reloadButton = Buttons.getIconButton("Refresh Test Table"
												, "icons/view-refresh.png"
												, Buttons.IconPosition.RIGHT
												, Colors.BLUE
												, this
												, new Dimension(200, 50));
		
		viewButton = Buttons.getIconButton("Explore Test Data"
												, "icons/go-next.png"
												, Buttons.IconPosition.RIGHT
												, Colors.GREEN
												, this
												, new Dimension(200, 50));
		
		filterPanel = new RegressionTestFilterPanel(this);
		
		table = new RegressionTestTable();
		table.addMouseListener(this);
		JScrollPane tablePane = new JScrollPane(table);
		JPanel tablePanel = new JPanel();
		double[] colTable = {5, TableLayoutConstants.FILL, 5};
		double[] rowTable = {5, TableLayoutConstants.FILL, 5};
		tablePanel.setLayout(new TableLayout(colTable, rowTable));
		tablePanel.add(tablePane, "1, 1, f, f");
		tablePanel.setBorder(Borders.getBorder("Regression Tests"));
		
		double[] colRight = {TableLayoutConstants.FILL};
		double[] rowRight = {TableLayoutConstants.PREFERRED
								, 10, TableLayoutConstants.FILL
								, 10, TableLayoutConstants.FILL};
		JPanel rightPanel = new JPanel(new TableLayout(colRight, rowRight));
		rightPanel.add(filterPanel, "0, 0, f, f");
		rightPanel.add(reloadButton, "0, 2, r, t");
		rightPanel.add(viewButton, "0, 4, r, b");
		
		double[] col = {TableLayoutConstants.FILL, 10, TableLayoutConstants.PREFERRED};
		double[] row = {TableLayoutConstants.FILL};
		setLayout(new TableLayout(col, row));
		add(tablePanel, "0, 0, f, f");
		add(rightPanel, "2, 0, f, f");
	}

	/**
	 * Sets the filter.
	 *
	 * @param filter the new filter
	 */
	public void setFilter(RegressionTestFilter filter){
		this.filter = filter;
		setTableState();
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent ae){
		if(ae.getSource()==reloadButton){
			String string = "Please wait while regression test data is refreshed.";
			DelayDialog dialog = new DelayDialog(frame, string, "Please wait...");
			dialog.open();
			GetRegressionTestsWorker2 worker = new GetRegressionTestsWorker2(frame, this, d, dialog);
			worker.execute();
		}else if(ae.getSource()==viewButton){
			exploreSelectedTests();
		}
	}
	
	/**
	 * Explore selected tests.
	 */
	private void exploreSelectedTests(){
		if(table.getSelectedRowCount()>0){
			TreeMap<Integer, RegressionTest> selectedTestMap = new TreeMap<Integer, RegressionTest>();
			d.setSelectedTestMap(selectedTestMap);
			for(int row: table.getSelectedRows()){
				int index = (Integer)table.getDataVector().get(table.convertRowIndexToModel(row)).get(0);
				selectedTestMap.put(index, d.getTestMap().get(index));
			}
			parent.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			RegressionTestMainDirListingWorker worker 
				= new RegressionTestMainDirListingWorker(parent, frame, d.getSelectedTestMap());
			worker.execute();
		}else{
			MessageDialog.createMessageDialog(frame, "Please select at least one regression test from the table to explore.", "Attention!");
		}
	}
	
	/**
	 * Sets the current state.
	 */
	public void setCurrentState(){
		setTableState();
		setFilterState();
	}
	
	/**
	 * Sets the table state.
	 */
	protected void setTableState(){
		table.setCurrentState(d.getTestMap(), filter);
		if(table.getDataVector().size()>0){
			table.setRowSelectionInterval(0, 0);
		}
	}
	
	/**
	 * Sets the filter state.
	 */
	private void setFilterState(){
		filterPanel.setCurrentState(d.getTestMap());
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent me){
		if(me.getClickCount()==2){
			exploreSelectedTests();
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
	
}

class RegressionTestMainDirListingWorker extends SwingWorker<ErrorResult, Void>{

	private Frame frame;
	private TestExplPanel parent;
	private TreeMap<Integer, RegressionTest> map;

	public RegressionTestMainDirListingWorker(TestExplPanel parent
												, Frame frame
												, TreeMap<Integer, RegressionTest> map){
		this.frame = frame;
		this.parent = parent;
		this.map = map;
	}

	protected ErrorResult doInBackground(){
		ErrorResult result = new ErrorResult();
		Iterator<RegressionTest> itr = map.values().iterator();
		while(itr.hasNext()){
			CustomFile dir = itr.next().getDir();
			result = WebServiceCom.getInstance().doWebServiceComCall(dir, Action.GET_DIR_LISTING);
			if(result.isError()){
				return result;
			}
		}
		return result;
	}
	
	protected void done(){
		try{
			ErrorResult result = get();
			if(!result.isError()){
				parent.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				parent.setViewPanelState();
				parent.setCurrentState(TestExplPanel.TestExplMode.VIEW);
			}else{
				parent.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				ErrorResultDialog.createDialog(frame, result);
			}
		}catch(Exception e){
			parent.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			CaughtExceptionHandler.handleException(e, frame);
		}
	}
}