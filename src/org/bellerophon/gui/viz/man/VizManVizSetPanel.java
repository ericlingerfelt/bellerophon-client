/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: VizManVizSetPanel.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.viz.man;

import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstants;

import java.awt.*;
import java.awt.event.*;
import java.util.Collections;

import javax.swing.*;

import org.bellerophon.data.MainData;
import org.bellerophon.data.feature.VizManData;
import org.bellerophon.data.util.ErrorResult;
import org.bellerophon.data.util.VizSet;
import org.bellerophon.enums.Action;
import org.bellerophon.enums.VizSetType;
import org.bellerophon.data.util.Resolution;
import org.bellerophon.gui.dialog.AddProgenitorDialog;
import org.bellerophon.gui.dialog.AttentionDialog;
import org.bellerophon.gui.dialog.DelayDialog;
import org.bellerophon.gui.dialog.ErrorDialog;
import org.bellerophon.gui.dialog.ErrorResultDialog;
import org.bellerophon.gui.format.Borders;
import org.bellerophon.gui.format.Buttons;
import org.bellerophon.gui.format.Colors;
import org.bellerophon.gui.util.WordWrapLabel;
import org.bellerophon.gui.viz.man.VizManPanel.VizManMode;
import org.bellerophon.gui.viz.man.worker.*;
import org.bellerophon.io.WebServiceCom;

/**
 * The Class VizManVizSetPanel.
 *
 * @author Eric J. Lingerfelt
 */
public class VizManVizSetPanel extends JPanel implements ActionListener{

	private Frame frame;
	private VizManPanel parent;
	private VizManData d;
	private JComboBox progenitorBox, typeBox;
	private JTextArea notesArea;
	private JTextField vizSetIdField, radField, latField, longField;
	private WordWrapLabel indexLabel;
	private JButton selectMatplotlibAnimationsButton, selectUploadedAnimationsButton, editMatplotlibAnimationsButton, editUploadedAnimationsButton, backButton, selectVizSetButton;
	private JButton addProgenitorButton;
	private JPanel paramPanel, notesPanel;
	private JLabel vizSetIdLabel, progenitorLabel, radLabel, latLabel, longLabel, typeLabel;
	private VizSetType type;
	
	/**
	 * Instantiates a new viz create viz set panel.
	 *
	 * @param frame the frame
	 * @param parent the parent
	 * @param d the d
	 */
	public VizManVizSetPanel(Frame frame, VizManPanel parent, VizManData d){
		
		this.frame = frame;
		this.parent = parent;
		this.d = d;
		
		indexLabel = new WordWrapLabel(true);
		indexLabel.setFont(Borders.getBorderFont());
		vizSetIdLabel = new JLabel("Visualization Set Unique ID (up to 60 chars)");
		progenitorLabel = new JLabel("Progenitor");
		radLabel = new JLabel("Radial Resolution");
		latLabel = new JLabel("Latitudinal Resolution");
		longLabel = new JLabel("Longitudinal Resolution");
		typeLabel = new JLabel("Visualization Set Type");
		
		progenitorBox = new JComboBox();
		progenitorBox.addActionListener(this);
		
		typeBox = new JComboBox();
		for(VizSetType t: VizSetType.values()){
			typeBox.addItem(t);
		}
		typeBox.addActionListener(this);
		
		addProgenitorButton = new JButton("Add Progenitor");
		addProgenitorButton.addActionListener(this);
		
		vizSetIdField = new JTextField();
		radField = new JTextField();
		latField = new JTextField();
		longField = new JTextField();
		
		notesArea = new JTextArea();
		notesArea.setWrapStyleWord(true);
		notesArea.setLineWrap(true);
		JScrollPane notesPane = new JScrollPane(notesArea);
		notesPanel = new JPanel();
		double[] colNotes = {5, TableLayoutConstants.FILL, 5};
		double[] rowNotes = {5, TableLayoutConstants.FILL, 5};
		notesPanel.setLayout(new TableLayout(colNotes, rowNotes));
		notesPanel.add(notesPane, "1, 1, f, f");
		notesPanel.setBorder(Borders.getBorder("Visualization Set Notes"));
		
		selectUploadedAnimationsButton = Buttons.getIconButton("Upload Animations"
														, "icons/go-next.png"
														, Buttons.IconPosition.RIGHT
														, Colors.GREEN
														, this
														, new Dimension(250, 50));
		
		editUploadedAnimationsButton = Buttons.getIconButton("Edit Animations"
														, "icons/go-next.png"
														, Buttons.IconPosition.RIGHT
														, Colors.GREEN
														, this
														, new Dimension(250, 50));
		
		selectMatplotlibAnimationsButton = Buttons.getIconButton("Select Animations"
														, "icons/go-next.png"
														, Buttons.IconPosition.RIGHT
														, Colors.GREEN
														, this
														, new Dimension(250, 50));
		
		editMatplotlibAnimationsButton = Buttons.getIconButton("Edit Animations"
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
		
		selectVizSetButton = Buttons.getIconButton("Select Different Set"
													, "icons/go-previous.png"
													, Buttons.IconPosition.LEFT
													, Colors.GREEN
													, this
													, new Dimension(250, 50));
		
		paramPanel = new JPanel();
		paramPanel.setBorder(Borders.getBorder("Visualization Set Parameters"));
	
	}
	
	/**
	 * Sets the current state.
	 */
	public void setCurrentState(){
		progenitorBox.removeAllItems();
		for(String s: MainData.getProgenitorList()){
			progenitorBox.addItem(s);
		}
		
		VizSet vs = d.getVizSet();
		indexLabel.setText("Visualization Set Index = " + String.valueOf(vs.getIndex()));
		vizSetIdField.setText(vs.getVizSetId());
		progenitorBox.setSelectedItem(vs.getProgenitor());
		if(vs.getProgenitor().equals("") && MainData.getProgenitorList().size()>0){
			progenitorBox.setSelectedIndex(0);
		}
		notesArea.setText(vs.getNotes());
		typeBox.setSelectedItem(vs.getVizSetType());
		radField.setText(String.valueOf(vs.getResolution().getRad()));
		latField.setText(String.valueOf(vs.getResolution().getLat()));
		longField.setText(String.valueOf(vs.getResolution().getLon()));

		layoutInterface(vs.getVizSetType(), vs.getIndex());
	} 
	
	private void layoutInterface(VizSetType type, int index){
		
		this.type = type;
		
		removeAll();
		paramPanel.removeAll();
		
		if(type==VizSetType.CHIMERA2D){
			
			double[] colParam = {5, TableLayoutConstants.FILL
								, 10, TableLayoutConstants.FILL
								, 10, TableLayoutConstants.FILL, 5};
			double[] rowParam = {5, TableLayoutConstants.PREFERRED
								, 10, TableLayoutConstants.PREFERRED
								, 10, TableLayoutConstants.PREFERRED
								, 10, TableLayoutConstants.PREFERRED, 5};
			paramPanel.setLayout(new TableLayout(colParam, rowParam));
			   
			paramPanel.add(vizSetIdLabel,         "1, 1, r, c");
			paramPanel.add(vizSetIdField,         "3, 1, 5, 1, f, c");
			paramPanel.add(typeLabel,             "1, 3, r, c");
			paramPanel.add(typeBox,               "3, 3, 5, 3, f, c");
			paramPanel.add(progenitorLabel,           "1, 5, r, c");
			paramPanel.add(progenitorBox,             "3, 5, f, c");
			paramPanel.add(addProgenitorButton,       "5, 5, f, c");
			
		}else if(type==VizSetType.CHIMERA3D){
			
			double[] colParam = {5, TableLayoutConstants.FILL
								, 10, TableLayoutConstants.FILL
								, 10, TableLayoutConstants.FILL, 5};
			double[] rowParam = {5, TableLayoutConstants.PREFERRED
								, 10, TableLayoutConstants.PREFERRED
								, 10, TableLayoutConstants.PREFERRED
								, 10, TableLayoutConstants.PREFERRED
								, 10, TableLayoutConstants.PREFERRED
								, 10, TableLayoutConstants.PREFERRED
								, 10, TableLayoutConstants.PREFERRED, 5};
			paramPanel.setLayout(new TableLayout(colParam, rowParam));
			   
			paramPanel.add(vizSetIdLabel,         "1, 1, r, c");
			paramPanel.add(vizSetIdField,         "3, 1, 5, 1, f, c");
			paramPanel.add(typeLabel,             "1, 3, r, c");
			paramPanel.add(typeBox,               "3, 3, 5, 3, f, c");
			paramPanel.add(progenitorLabel,           "1, 5, r, c");
			paramPanel.add(progenitorBox,             "3, 5, f, c");
			paramPanel.add(addProgenitorButton,       "5, 5, f, c");
			paramPanel.add(radLabel,              "1, 9, r, c");
			paramPanel.add(radField,              "3, 9, 5, 9, f, c");
			paramPanel.add(latLabel,              "1, 11, r, c");
			paramPanel.add(latField,         	  "3, 11, 5, 11, f, c");
			paramPanel.add(longLabel,             "1, 13, r, c");
			paramPanel.add(longField,         	  "3, 13, 5, 13, f, c");
		}
		
		double[] col = {TableLayoutConstants.FILL, 10, TableLayoutConstants.FILL};
		double[] row = {TableLayoutConstants.PREFERRED
						, 10, TableLayoutConstants.PREFERRED
						, 10, TableLayoutConstants.FILL
						, 10, TableLayoutConstants.PREFERRED};
		setLayout(new TableLayout(col, row));
		add(indexLabel,             "0, 0, 2, 0, c, c");
		add(paramPanel,             "0, 2, 2, 2, f, c");
		add(notesPanel,             "0, 4, 2, 4, f, f");
		if(index==-1 && type==VizSetType.CHIMERA2D){
			typeBox.setEnabled(true);
			add(backButton,     			"0, 6, l, c");
			add(selectMatplotlibAnimationsButton,  	"2, 6, r, c");
		}else if(index==-1 && type==VizSetType.CHIMERA3D){
			typeBox.setEnabled(true);
			add(backButton,     			"0, 6, l, c");
			add(selectUploadedAnimationsButton,  	"2, 6, r, c");
		}else if(type==VizSetType.CHIMERA2D){
			typeBox.setEnabled(false);
			add(selectVizSetButton,     "0, 6, l, c");
			add(editMatplotlibAnimationsButton,   "2, 6, r, c");
		}else if(type==VizSetType.CHIMERA3D){
			typeBox.setEnabled(false);
			add(selectVizSetButton,     "0, 6, l, c");
			add(editUploadedAnimationsButton,   "2, 6, r, c");
		}
		
		validate();
		repaint();
	}
	
	/**
	 * Gets the current state.
	 *
	 * @return the current state
	 */
	public void getCurrentState(){
		VizSet vs = d.getVizSet();
		vs.setVizSetId(vizSetIdField.getText().trim());
		vs.setNotes(notesArea.getText().trim());
		vs.setProgenitor((String)progenitorBox.getSelectedItem());
		vs.setVizSetType((VizSetType)typeBox.getSelectedItem());
		if(type==VizSetType.CHIMERA3D){
			Resolution r = new Resolution();
			r.setRad(Integer.valueOf(radField.getText()));
			r.setLat(Integer.valueOf(latField.getText()));
			r.setLon(Integer.valueOf(longField.getText()));
			vs.setResolution(r);
		}
		d.setVizSet(vs);
	}
	
	/**
	 * Good data.
	 *
	 * @return true, if successful
	 */
	private boolean goodData(){
		
		VizSetType type = (VizSetType)typeBox.getSelectedItem();
		
		if(notesArea.getText().trim().equals("")){
			AttentionDialog.createDialog(frame, "Please enter a value for <i>Notes</i>.");
			return false;
		}
		
		if(vizSetIdField.getText().trim().equals("")){
			AttentionDialog.createDialog(frame, "Please enter a value for <i>Visualization Set Unique ID</i>.");
			return false;
		}else if(vizSetIdField.getText().trim().length()>50){
			AttentionDialog.createDialog(frame, "Please enter a value for <i>Visualization Set Unique ID</i> " +
													"that is less than or equal to 60 characters long.");
			return false;
		}
		
		if(type==VizSetType.CHIMERA3D){
		
			if(radField.getText().trim().equals("")){
				AttentionDialog.createDialog(frame, "Please enter a value for <i>Radial Resolution</i>.");
				return false;
			}
			try{
				int rad = Integer.valueOf(radField.getText());
				if(rad<1){
					AttentionDialog.createDialog(frame, "Please enter a positive integer value for <i>Radial Resolution</i>.");
					return false;
				}
			}catch(NumberFormatException nfe){
				AttentionDialog.createDialog(frame, "Please enter an integer value for <i>Radial Resolution</i>.");
				return false;
			}
			
			if(latField.getText().trim().equals("")){
				AttentionDialog.createDialog(frame, "Please enter a value for <i>Latitudinal Resolution</i>.");
				return false;
			}
			try{
				int lat = Integer.valueOf(latField.getText());
				if(lat<1){
					AttentionDialog.createDialog(frame, "Please enter a positive integer value for <i>Latitudinal Resolution</i>.");
					return false;
				}
			}catch(NumberFormatException nfe){
				AttentionDialog.createDialog(frame, "Please enter an integer value for <i>Latitudinal Resolution</i>.");
				return false;
			}
			
			if(longField.getText().trim().equals("")){
				AttentionDialog.createDialog(frame, "Please enter a value for <i>Longitudinal Resolution</i>.");
				return false;
			}
			try{
				int lon = Integer.valueOf(longField.getText());
				if(lon<1){
					AttentionDialog.createDialog(frame, "Please enter a positive integer value for <i>Longitudinal Resolution</i>.");
					return false;
				}
			}catch(NumberFormatException nfe){
				AttentionDialog.createDialog(frame, "Please enter an integer value for <i>Longitudinal Resolution</i>.");
				return false;
			}
			
		}
		
		return true;
	}
	
	/**
	 * Viz set id exists.
	 *
	 * @param vizSetId the viz set id
	 * @return true, if successful
	 */
	public boolean vizSetIdExists(String vizSetId){
		d.setVizSetId(vizSetId);
		ErrorResult result = WebServiceCom.getInstance().doWebServiceComCall(d, Action.VIZ_SET_ID_EXISTS);
		if(!result.isError()){
			if(d.getVizSetIdExists()){
				ErrorDialog.createDialog(frame, "The <i>Visualization Set Unique ID</i> you entered already exists.");
				return true;
			}
			return false;
		}
		ErrorResultDialog.createDialog(frame, result);
		return true;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent ae){
		if(ae.getSource()==addProgenitorButton){
			String newProgenitor = AddProgenitorDialog.createAddProgenitorDialog(frame);
			if(!newProgenitor.equals("")){
				if(!MainData.getProgenitorList().contains(newProgenitor)){
					MainData.getProgenitorList().add(newProgenitor);
					Collections.sort(MainData.getProgenitorList());
					progenitorBox.removeAllItems();
					for(String s: MainData.getProgenitorList()){
						progenitorBox.addItem(s);
					}
					progenitorBox.setSelectedItem(newProgenitor);
				}
			}
		}else if(ae.getSource()==selectMatplotlibAnimationsButton){
			if(goodData() && !vizSetIdExists(vizSetIdField.getText().trim())){
				getCurrentState();
				parent.setCurrentState(VizManPanel.VizManMode.ANIMATION);
			}
		}else if(ae.getSource()==selectUploadedAnimationsButton){
			if(goodData() && !vizSetIdExists(vizSetIdField.getText().trim())){
				getCurrentState();
				parent.setCurrentState(VizManPanel.VizManMode.UPLOAD);
			}
		}else if(ae.getSource()==editMatplotlibAnimationsButton){
			if(goodData()){
				if((!d.getVizSet().getVizSetId().equals(vizSetIdField.getText().trim())
						&& !vizSetIdExists(vizSetIdField.getText().trim()))
						|| d.getVizSet().getVizSetId().equals(vizSetIdField.getText().trim())){
					getCurrentState();
					d.setGetAllData(true);
					String string = "Please wait while animation data is loaded.";
					DelayDialog dialog = new DelayDialog(frame, string, "Please wait...");
					dialog.open();
					GetMatplotlibAnimationsWorker worker = new GetMatplotlibAnimationsWorker(frame, parent, dialog);
					worker.execute();
				}
			}
		}else if(ae.getSource()==editUploadedAnimationsButton){
			if(goodData()){
				if((!d.getVizSet().getVizSetId().equals(vizSetIdField.getText().trim())
						&& !vizSetIdExists(vizSetIdField.getText().trim()))
						|| d.getVizSet().getVizSetId().equals(vizSetIdField.getText().trim())){
					getCurrentState();
					d.setGetAllData(true);
					String string = "Please wait while animation data is loaded.";
					DelayDialog dialog = new DelayDialog(frame, string, "Please wait...");
					dialog.open();
					GetUploadedAnimationsWorker worker = new GetUploadedAnimationsWorker(frame, parent, dialog);
					worker.execute();
				}
			}
		}else if(ae.getSource()==backButton){
			parent.setCurrentState(VizManMode.OPTION);
		}else if(ae.getSource()==selectVizSetButton){
			parent.setCurrentState(VizManMode.SELECT);
		}else if(ae.getSource()==typeBox){
			layoutInterface((VizSetType)typeBox.getSelectedItem(), d.getVizSet().getIndex());
		}
	}
}