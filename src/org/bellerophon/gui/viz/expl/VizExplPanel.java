/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: VizExplPanel.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.viz.expl;

import info.clearthought.layout.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.TreeMap;

import javax.swing.*;

import org.bellerophon.data.feature.*;
import org.bellerophon.data.util.VizSet;
import org.bellerophon.gui.viz.expl.worker.UpdateVizSetDataWorker;

public class VizExplPanel extends JPanel implements ActionListener{

	private VizExplData d = new VizExplData();
	private VizExplViewPanel viewPanel;
	private VizExplSelectPanel selectPanel;
	private Timer timer;
	private VizExplMode mode;
	private Frame frame;
	
	public enum VizExplMode {SELECT, VIEW}
	
	public VizExplPanel(Frame frame){
		this.frame = frame;
		double[] col = {10, TableLayoutConstants.FILL, 10};
		double[] row = {10, TableLayoutConstants.FILL, 10};
		setLayout(new TableLayout(col, row));
		selectPanel = new VizExplSelectPanel(frame, this, d);
		viewPanel = new VizExplViewPanel(frame, this, d);
		timer = new Timer(300000, this);
	}

	public void setCurrentState(VizExplMode mode){
		this.mode = mode;
		removeAll();
		if(mode==VizExplMode.VIEW){
			viewPanel.setCurrentState();
			add(viewPanel, "1, 1, f, f");
		}else if(mode==VizExplMode.SELECT){
			selectPanel.setCurrentState();
			add(selectPanel, "1, 1, f, f");
		}
		validate();
		repaint();
	}
	
	public VizExplData getData(){return d;}

	public void updateComponents(){
		if(mode==VizExplMode.VIEW){
			viewPanel.updateComponents();
		}else if(mode==VizExplMode.SELECT){
			selectPanel.updateComponents();
		}
	}
	
	public void actionPerformed(ActionEvent ae) {
		if(ae.getSource()==timer){
			UpdateVizSetDataWorker worker = new UpdateVizSetDataWorker(frame, this);
			worker.execute();
		}
	}

	public void startUpdater() {
		if(!timer.isRunning()){
			// timer.start();
		}
	}

	public void updateAfterVizSetModification(){
		UpdateVizSetDataWorker worker = new UpdateVizSetDataWorker(frame, this);
		worker.execute();
	}

	public void updateAfterVizSetModification(TreeMap<Integer, VizSet> vizSetMap) {
		TreeMap<Integer, VizSet> newVizSetMap = new TreeMap<Integer, VizSet>();
		Iterator<Integer> itr = d.getVizSetMap().keySet().iterator();
		while(itr.hasNext()){
			int index = itr.next();
			if(vizSetMap.containsKey(index)){
				newVizSetMap.put(index, d.getVizSetMap().get(index));
			}
		}
		d.setVizSetMap(newVizSetMap);
		d.setSelectedVizSetMap(new TreeMap<Integer, VizSet>());
		selectPanel.updateComponents();
	}
	
}