/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: VizExplCustomPanel.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.viz.expl.comp;

import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstants;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.*;
import java.util.Iterator;

import javax.swing.*;
import javax.swing.event.*;

import org.bellerophon.data.MainData;
import org.bellerophon.data.util.*;
import org.bellerophon.enums.Scale;
import org.bellerophon.enums.VizCompType;
import org.bellerophon.exception.CaughtExceptionHandler;
import org.bellerophon.gui.dialog.AttentionDialog;
import org.bellerophon.gui.format.Buttons;
import org.bellerophon.gui.format.Colors;
import org.bellerophon.gui.format.ComboToolTipRenderer;
import org.bellerophon.gui.util.FramePanel;
import org.bellerophon.gui.viz.VizSetUpdateListener;
import org.bellerophon.gui.viz.expl.VizExplViewPanel;
import org.bellerophon.gui.viz.expl.worker.CustomMatplotlibAnimationFrameWorker;

public class VizExplCustomPanel extends VizExplCompPanel implements ActionListener
																	, ChangeListener
																	, VizSetUpdateListener{

	private JTextField xminField, xmaxField, yminField, ymaxField
							, minField, maxField
							, frameField;
	private JComboBox modelBox, colormapBox, scaleBox;
	private JButton  recreateButton, revertButton;
	private JCheckBox smoothZonesBox, displayDateBox;
	private JPanel paramPanel;
	private JLabel xminLabel, xmaxLabel, yminLabel, ymaxLabel
							, minLabel, maxLabel, modelLabel
							, colorTableLabel, requiredLabel
							, scaleLabel;
	private MatplotlibAnimation tempMatplotlibAnimation;
	private JLabel maxFrameLabel;
	private JSlider slider;
	private JCheckBox fitBox;
	private FramePanel framePanel;
	private JScrollPane paramPane;
	private MatplotlibAnimation va;
	
	public VizExplCustomPanel(Frame parent, VizExplViewPanel panel, VizSet vs, MatplotlibAnimation va){
		
		super(parent, panel, vs, va, VizCompType.CUSTOM);
		
		this.va = va;
		
		JLabel frameLabel = new JLabel("Current Frame Is");
		maxFrameLabel = new JLabel("Out Of " + vs.getLastFrame());
		
		frameField = new JTextField(5);
		frameField.setText(String.valueOf(vs.getLastFrame()));
		
		fitBox = new JCheckBox("Fit Frame to Window", true);
		fitBox.addActionListener(this);
		
		recreateButton = Buttons.getIconButton("icons/view-refresh.png"
												, Colors.BLUE
												, this
												, "Regenerate Image");
		
		revertButton = Buttons.getIconButton("icons/edit-undo.png"
												, Colors.GREEN
												, this
												, "Revert to Original");
		
		slider = new JSlider(JSlider.HORIZONTAL, 1, vs.getLastFrame(), vs.getLastFrame());
		slider.addChangeListener(this);
		
		xminField = new JTextField(10);
		xmaxField = new JTextField(10);
		yminField = new JTextField(10);
		ymaxField = new JTextField(10);
		minField = new JTextField(10);
		maxField = new JTextField(10);
		
		smoothZonesBox = new JCheckBox("Smooth Zones?");
		displayDateBox = new JCheckBox("Display Date?");
		
		frameLabel = new JLabel("Selected Frame");
		xminLabel = new JLabel("Zoom X Min [km]*");
		xmaxLabel = new JLabel("Zoom X Max [km]*");
		yminLabel = new JLabel("Zoom Y Min [km]*");
		ymaxLabel = new JLabel("Zoom Y Max [km]*");
		minLabel = new JLabel("Colortable Range Min");
		maxLabel = new JLabel("Colortable Range Max");
		modelLabel = new JLabel("Model*");
		scaleLabel = new JLabel("Scale");
		colorTableLabel = new JLabel("Color Table*");
		requiredLabel = new JLabel("* denotes required field");
		
		modelBox = new JComboBox();
		Iterator<MatplotlibModel> itrModel = MainData.getMatplotlibModelMap().values().iterator();
		while(itrModel.hasNext()){
			modelBox.addItem(itrModel.next());
		}
		modelBox.setToolTipText(modelBox.getSelectedItem().toString());
		modelBox.setRenderer(new ComboToolTipRenderer());
		modelBox.addActionListener(this);

		colormapBox = new JComboBox();
		Iterator<MatplotlibColormap> itrColormap = MainData.getMatplotlibColormapMap().values().iterator();
		while(itrColormap.hasNext()){
			colormapBox.addItem(itrColormap.next());
		}
		colormapBox.addActionListener(this);
		
		scaleBox = new JComboBox();
		for(Scale s: Scale.values()){
			scaleBox.addItem(s);
		}
		scaleBox.addActionListener(this);
		
		framePanel = new FramePanel();
		JScrollPane framePanelPane = new JScrollPane(framePanel);
		framePanel.setScrollPane(framePanelPane);
		framePanel.setFitToWindow(true);
		
		JPanel inputPanel = new JPanel();
		double[] colInput = {TableLayoutConstants.PREFERRED
						, 5, TableLayoutConstants.FILL
						, 5, TableLayoutConstants.PREFERRED
						, 10, TableLayoutConstants.PREFERRED
						, 10, TableLayoutConstants.PREFERRED
						, 10, TableLayoutConstants.PREFERRED
						, 10, TableLayoutConstants.PREFERRED};
		double[] rowInput = {TableLayoutConstants.PREFERRED
						, 5, TableLayoutConstants.PREFERRED};
		inputPanel.setLayout(new TableLayout(colInput, rowInput));
		inputPanel.add(frameLabel,       "0, 0, r, c");
		inputPanel.add(frameField,       "2, 0, c, c");
		inputPanel.add(maxFrameLabel,    "4, 0, l, c");
		inputPanel.add(fitBox,           "6, 0, c, c");
		inputPanel.add(slider,           "0, 2, 6, 2, f, c");
		inputPanel.add(recreateButton,   "8, 0, 8, 2, c, c");
		inputPanel.add(revertButton, 	 "10, 0, 10, 2, c, c");
		
		paramPanel = new JPanel();
		paramPane = new JScrollPane(paramPanel);
		
		double[] colParam = {5, TableLayoutConstants.FILL, 5};
		double[] rowParam = {5, TableLayoutConstants.PREFERRED
							, 5, TableLayoutConstants.PREFERRED
							, 10, TableLayoutConstants.PREFERRED
							, 5, TableLayoutConstants.PREFERRED
							, 10, TableLayoutConstants.PREFERRED
							, 5, TableLayoutConstants.PREFERRED
							, 5, TableLayoutConstants.PREFERRED
							, 10, TableLayoutConstants.PREFERRED
							, 5, TableLayoutConstants.PREFERRED
							, 10, TableLayoutConstants.PREFERRED
							, 5, TableLayoutConstants.PREFERRED
							, 10, TableLayoutConstants.PREFERRED
							, 5, TableLayoutConstants.PREFERRED
							, 10, TableLayoutConstants.PREFERRED
							, 5, TableLayoutConstants.PREFERRED
							, 10, TableLayoutConstants.PREFERRED
							, 5, TableLayoutConstants.PREFERRED
							, 5, TableLayoutConstants.PREFERRED
							, 5, TableLayoutConstants.PREFERRED, 5};
		paramPanel.setLayout(new TableLayout(colParam, rowParam));
		
		paramPanel.add(modelLabel,           "1, 1, l, c");
		paramPanel.add(modelBox,             "1, 3, f, c");
		paramPanel.add(colorTableLabel,      "1, 5, l, c");
		paramPanel.add(colormapBox,          "1, 7, f, c");
		paramPanel.add(minLabel,             "1, 9, l, c");
		paramPanel.add(minField,             "1, 11, f, c");
		paramPanel.add(maxLabel,             "1, 13, l, c");
		paramPanel.add(maxField,             "1, 15, f, c");
		paramPanel.add(xminLabel,            "1, 17, l, c");
		paramPanel.add(xminField,            "1, 19, f, c");
		paramPanel.add(xmaxLabel,            "1, 21, l, c");
		paramPanel.add(xmaxField,            "1, 23, f, c");
		paramPanel.add(yminLabel,            "1, 25, l, c");
		paramPanel.add(yminField,            "1, 27, f, c");
		paramPanel.add(ymaxLabel,            "1, 29, l, c");
		paramPanel.add(ymaxField,            "1, 31, f, c");
		paramPanel.add(scaleLabel,           "1, 33, l, c");
		paramPanel.add(scaleBox,             "1, 35, f, c");
		paramPanel.add(smoothZonesBox,       "1, 37, f, c");
		paramPanel.add(displayDateBox,       "1, 39, f, c");
		paramPanel.add(requiredLabel,        "1, 41, l, b");
		paramPanel.setPreferredSize(new Dimension(200, 750));	
		paramPane.setPreferredSize(new Dimension(250, paramPanel.getPreferredSize().height + 50));
		
		double[] col = {5, TableLayoutConstants.PREFERRED
							, 5, TableLayoutConstants.FILL, 5};
		double[] row = {5, TableLayoutConstants.FILL
						, 5, TableLayoutConstants.PREFERRED, 5};
		setLayout(new TableLayout(col, row));
		add(paramPane, 		"1, 1, f, f");
		add(framePanelPane, "3, 1, f, f");
		add(inputPanel,     "1, 3, 3, 3, c, c");
		
		modelBox.setSelectedItem(va.getMatplotlibModel());
		colormapBox.setSelectedItem(va.getColormap());
		String zoom = va.getZoom();
		String range = va.getRange();
		
		String[] array = zoom.split(",");
		xminField.setText(array[0].trim());
		xmaxField.setText(array[1].trim());
		yminField.setText(array[2].trim());
		ymaxField.setText(array[3].trim());
		
		if(!range.equals("")){
			array = range.split(",");
			minField.setText(array[0].trim());
			maxField.setText(array[1].trim());
		}else{
			minField.setText("");
			maxField.setText("");
		}	
		smoothZonesBox.setSelected(va.getSmoothZones());
		displayDateBox.setSelected(va.getDisplayDate());
		scaleBox.setSelectedItem(va.getScale());
		tempMatplotlibAnimation = va.clone();
		
	}
	
	public void setCurrentFrame(boolean checkData){
		setSelectedFrame();
		loadAnimationWithParameters(tempMatplotlibAnimation, checkData);
		CustomMatplotlibAnimationFrameWorker worker = new CustomMatplotlibAnimationFrameWorker(parent, this, tempMatplotlibAnimation);
		worker.execute();
	}
	
	private void loadAnimationWithParameters(MatplotlibAnimation va, boolean checkData){
		MatplotlibModel model = (MatplotlibModel)modelBox.getSelectedItem();
		
		if((checkData && goodData(model)) 
				|| !checkData){
			MatplotlibColormap colorTable = (MatplotlibColormap)colormapBox.getSelectedItem();
			String range = minField.getText().trim() + "," + maxField.getText().trim();
			if(minField.getText().trim().equals("") && maxField.getText().trim().equals("")){
				range = "";
			}
			String zoom = xminField.getText().trim() + "," + xmaxField.getText().trim() + "," 
							+ yminField.getText().trim() + "," + ymaxField.getText().trim();
			Scale scale = (Scale)scaleBox.getSelectedItem();
			boolean smoothZones = smoothZonesBox.isSelected();
			boolean displayDate = displayDateBox.isSelected();
			
			va.setCurrentFrame(frame);
			va.setMatplotlibModel(model);
			va.setColormap(colorTable);
			va.setRange(range);
			va.setZoom(zoom);
			va.setScale(scale);
			va.setSmoothZones(smoothZones);
			va.setDisplayDate(displayDate);
		}
	}
	
	public void setFramePanelFile(CustomFile frameFile){
		this.frameFile = frameFile;
		try{
			framePanel.setFile(frameFile);
		}catch(Exception e){
			e.printStackTrace();
			CaughtExceptionHandler.handleException(e, parent);
		}
	}
	
	private boolean goodFrame(){
		try{
			if(frameField.getText().trim().equals("")){
				AttentionDialog.createDialog(parent, "Please enter an integer value between 1 and " 
													+ vs.getLastFrame() 
													+ " for <i>Selected Frame</i>.");
				return false;
			}
			int frame = Integer.valueOf(frameField.getText().trim());
			if(frame<1 || frame>vs.getLastFrame()){
				AttentionDialog.createDialog(parent, "Please enter an integer value between 1 and " 
													+ vs.getLastFrame() 
													+ " for <i>Selected Frame</i>.");
				return false;
			}
		}catch(NumberFormatException nfe){
			AttentionDialog.createDialog(parent, "Please enter an integer value between 1 and " 
												+ vs.getLastFrame() 
												+ " for <i>Selected Frame</i>.");
			return false;
		}
		return true;
	}
	
	private boolean goodZoom(){
		try{
			if(xminField.getText().trim().equals("")){
				AttentionDialog.createDialog(parent, "Please enter a value for <i>Zoom X Min</i>.");
				return false;
			}
			Double.valueOf(xminField.getText().trim());
		}catch(NumberFormatException nfe){
			AttentionDialog.createDialog(parent, "Please enter a numeric value for <i>Zoom X Min</i>.");
			return false;
		}
		
		try{
			if(xmaxField.getText().trim().equals("")){
				AttentionDialog.createDialog(parent, "Please enter a value for <i>Zoom X Max</i>.");
				return false;
			}
			Double.valueOf(xmaxField.getText().trim());
		}catch(NumberFormatException nfe){
			AttentionDialog.createDialog(parent, "Please enter a numeric value for <i>Zoom X Max</i>.");
			return false;
		}
		
		try{
			if(yminField.getText().trim().equals("")){
				AttentionDialog.createDialog(parent, "Please enter a value for <i>Zoom Y Min</i>.");
				return false;
			}
			Double.valueOf(yminField.getText().trim());
		}catch(NumberFormatException nfe){
			AttentionDialog.createDialog(parent, "Please enter a numeric value for <i>Zoom Y Min</i>.");
			return false;
		}
		
		try{
			if(ymaxField.getText().trim().equals("")){
				AttentionDialog.createDialog(parent, "Please enter a value for <i>Zoom Y Max</i>.");
				return false;
			}
			Double.valueOf(ymaxField.getText().trim());
		}catch(NumberFormatException nfe){
			AttentionDialog.createDialog(parent, "Please enter a numeric value for <i>Zoom Y Max</i>.");
			return false;
		}
		
		if(Double.valueOf(xminField.getText().trim()) >= Double.valueOf(xmaxField.getText().trim())){
			AttentionDialog.createDialog(parent, "Please enter a value for <i>Zoom X Max</i> which is greater than <i>Zoom X Min</i>.");
			return false;
		}
		
		if(Double.valueOf(yminField.getText().trim()) >= Double.valueOf(ymaxField.getText().trim())){
			AttentionDialog.createDialog(parent, "Please enter a value for <i>Zoom Y Max</i> which is greater than <i>Zoom Y Min</i>.");
			return false;
		}
		
		return true;
	}
	
	private boolean goodRange(){
		try{
			if(!minField.getText().trim().equals("") || !maxField.getText().trim().equals("")){
				Double.valueOf(minField.getText().trim());
				Double.valueOf(maxField.getText().trim());
			}
		}catch(NumberFormatException nfe){
			AttentionDialog.createDialog(parent, "Please enter a numeric value for <i>Range Min</i> and <i>Range Max</i> or leave both fields empty.");
			return false;
		}
		
		if((!minField.getText().trim().equals("") 
				&& !maxField.getText().trim().equals(""))
				&& ((Scale)scaleBox.getSelectedItem()) == Scale.LOG
				&& (Double.valueOf(minField.getText().trim()) <= 0.0 || Double.valueOf(maxField.getText().trim()) <= 0.0)){
			AttentionDialog.createDialog(parent, "You have selected a Log scale. Please enter positive values for <i>Range Min</i> and <i>Range Max</i> or leave both fields empty.");
			return false;
		}
		
		if((!minField.getText().trim().equals("") && !maxField.getText().trim().equals("")) 
				&& (Double.valueOf(minField.getText().trim()) >= Double.valueOf(maxField.getText().trim()))){
			AttentionDialog.createDialog(parent, "Please enter a value for <i>Range Max</i> which is greater than <i>Range Min</i> or leave both fields empty.");
			return false;
		}
		
		return true;
	}

	private boolean goodData(MatplotlibModel m){
		return goodFrame() && goodZoom() && goodRange();
	}
	
	public void actionPerformed(ActionEvent ae){
		if(ae.getSource()==revertButton){
			modelBox.setSelectedItem(va.getMatplotlibModel());
			colormapBox.setSelectedItem(va.getColormap());
			String zoom = va.getZoom();
			String range = va.getRange();
			
			String[] array = zoom.split(",");
			xminField.setText(array[0].trim());
			xmaxField.setText(array[1].trim());
			yminField.setText(array[2].trim());
			ymaxField.setText(array[3].trim());
			
			if(!range.equals("")){
				array = range.split(",");
				minField.setText(array[0].trim());
				maxField.setText(array[1].trim());
			}else{
				minField.setText("");
				maxField.setText("");
			}	
			smoothZonesBox.setSelected(va.getSmoothZones());
			displayDateBox.setSelected(va.getDisplayDate());
			scaleBox.setSelectedItem(va.getScale());
			tempMatplotlibAnimation = va.clone();
			setCurrentFrame(true);
		}else if(ae.getSource()==recreateButton){
			MatplotlibModel model = (MatplotlibModel)modelBox.getSelectedItem();
			if(goodData(model)){
				frame = Integer.valueOf(frameField.getText().trim());
				slider.setValue(frame);
				setCurrentFrame(true);	
			}
		}else if(ae.getSource()==fitBox){
			framePanel.setFitToWindow(fitBox.isSelected());
		}else if(ae.getSource()==modelBox){
			modelBox.setToolTipText(modelBox.getSelectedItem().toString());
		}
	
	}
	
	public void stateChanged(ChangeEvent ce){
		if(ce.getSource()==slider){
			frameField.setText(String.valueOf(slider.getValue()));
		}
	}

	public void updateState(){
		slider.removeChangeListener(this);
		slider.setMaximum(vs.getLastFrame());
		slider.addChangeListener(this);
	}
	
}
