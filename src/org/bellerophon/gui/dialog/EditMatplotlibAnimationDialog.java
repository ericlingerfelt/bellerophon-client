/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: EditMatplotlibAnimationDialog.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.dialog;

import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstants;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.*;

import org.bellerophon.data.MainData;
import org.bellerophon.data.feature.VizManData;
import org.bellerophon.data.util.*;
import org.bellerophon.enums.Scale;
import org.bellerophon.exception.CaughtExceptionHandler;
import org.bellerophon.file.CustomFileFilter;
import org.bellerophon.file.FileType;
import org.bellerophon.gui.dialog.worker.GenerateMatplotlibAnimationPreviewWorker;
import org.bellerophon.gui.format.Borders;
import org.bellerophon.gui.format.Buttons;
import org.bellerophon.gui.format.Colors;
import org.bellerophon.gui.format.ComboToolTipRenderer;
import org.bellerophon.gui.util.FramePanel;
import org.bellerophon.gui.util.PlainFileChooserFactory;
import org.bellerophon.gui.util.WordWrapLabel;
import org.bellerophon.io.IOUtilities;

/**
 * The Class EditAnimationDialog.
 *
 * @author Eric J. Lingerfelt
 */
public class EditMatplotlibAnimationDialog extends JDialog implements ActionListener, ChangeListener{
	
	private JTextField xminField, xmaxField, yminField, ymaxField
						, minField, maxField, animationIdField, frameField;
	private JComboBox modelBox, colormapBox, scaleBox;
	private JButton saveButton, cancelButton, previewButton
						, savePreviewButton, undoButton;
	private JCheckBox smoothZonesBox, displayDateBox;
	private Vector<Vector<Object>> dataVector;
	private int rowIndex;
	private int animationIndex;
	private Vector<Object> v, vInit;
	private JPanel inputPanel, previewPanel;
	private JLabel xminLabel, xmaxLabel, yminLabel, ymaxLabel
					, minLabel, maxLabel, matplotlibModelLabel
					, colorTableLabel, animationIdLabel
					, smoothZonesLabel, requiredLabel
					, frameLabel, scaleLabel, displayDateLabel;
	
	/**
	 * The Enum Type.
	 *
	 * @author Eric J. Lingerfelt
	 */
	public enum Type {EDIT, ADD};
	private VizSet vs, vsData;
	private JSlider slider;
	private FramePanel framePanel;
	private JCheckBox fitBox;
	private WordWrapLabel topLabel, indexLabel, vizSetLabel;
	private MatplotlibAnimation va;
	
	/**
	 * Creates the edit animation dialog.
	 *
	 * @param frame the frame
	 * @param dataVector the data vector
	 * @param rowIndex the row index
	 * @param type the type
	 * @param d the d
	 * @return the vector
	 */
	public static Vector<Object> createEditMatplotlibAnimationDialog(Frame frame
														, Vector<Vector<Object>> dataVector
														, int rowIndex
														, Type type
														, VizManData d){
		String title = "";
		if(type==Type.EDIT){
			title = "Edit Selected Animation";
		}else if(type==Type.ADD){
			title = "Add New Animation";
		}
		EditMatplotlibAnimationDialog dialog = new EditMatplotlibAnimationDialog(frame, title, dataVector, rowIndex, d);
		dialog.setVisible(true);
		return dialog.v;
	}
	
	/**
	 * Instantiates a new edits the animation dialog.
	 *
	 * @param frame the frame
	 * @param title the title
	 * @param dataVector the data vector
	 * @param rowIndex the row index
	 * @param d the d
	 */
	private EditMatplotlibAnimationDialog(Frame frame, String title, Vector<Vector<Object>> dataVector, int rowIndex, VizManData d){
		
		super(frame, title, true);
		this.dataVector = dataVector;
		this.rowIndex = rowIndex;
		setSize(1070, 750);
		setLocationRelativeTo(frame);
		
		vs = d.getVizSet();
		
		if(d.getVizSet().getLastFrame()==0){
			vsData = new VizSet();
			vsData.setIndex(227);
			vsData.setLastFrame(50);
			topLabel = new WordWrapLabel(true);
			topLabel.setText("The visualization set you have selected does not currently contain any data. " +
								"All Animation Previews will be generated using data from Visualization Set #210.", Color.red);
			topLabel.setFont(Borders.getBorderFont());
		}else{
			vsData = d.getVizSet();
		}
		
		saveButton = Buttons.getIconButton("Save Animation"
											, "icons/list-add.png"
											, Buttons.IconPosition.RIGHT
											, Colors.BLUE
											, this
											, new Dimension(200, 50));
		
		cancelButton = Buttons.getIconButton("Cancel"
											, "icons/process-stop.png"
											, Buttons.IconPosition.RIGHT
											, Colors.RED
											, this
											, new Dimension(200, 50));
		
		previewButton = Buttons.getIconButton("Refresh Preview"
											, "icons/view-refresh.png"
											, Buttons.IconPosition.RIGHT
											, Colors.BLUE
											, this
											, new Dimension(200, 50));
		
		savePreviewButton = Buttons.getIconButton("Save Preview"
											, "icons/media-floppy.png"
											, Buttons.IconPosition.RIGHT
											, Colors.BLUE
											, this
											, new Dimension(200, 50));
		
		undoButton = Buttons.getIconButton("Undo Changes"
											, "icons/edit-undo.png"
											, Buttons.IconPosition.RIGHT
											, Colors.GREEN
											, this
											, new Dimension(200, 50));
		
		JPanel buttonPanel = new JPanel();
		double[] colButton = {TableLayoutConstants.PREFERRED
								, 10, TableLayoutConstants.PREFERRED
								, 10, TableLayoutConstants.PREFERRED
								, 10, TableLayoutConstants.PREFERRED
								, 10, TableLayoutConstants.PREFERRED};
		double[] rowButton = {TableLayoutConstants.PREFERRED};
		buttonPanel.setLayout(new TableLayout(colButton, rowButton));
		buttonPanel.add(saveButton,    		"0, 0, c, c");
		buttonPanel.add(previewButton, 		"2, 0, c, c");
		buttonPanel.add(savePreviewButton, 	"4, 0, c, c");
		buttonPanel.add(undoButton, 		"6, 0, c, c");
		buttonPanel.add(cancelButton,  		"8, 0, c, c");
		
		indexLabel = new WordWrapLabel(true);
		indexLabel.setFont(Borders.getBorderFont());
		indexLabel.setText("Visualization Set Index = " + String.valueOf(vs.getIndex()));
		
		vizSetLabel = new WordWrapLabel(true);
		vizSetLabel.setFont(Borders.getBorderFont());
		vizSetLabel.setText("Visualization Set Unique ID = " + vs.toString());
		
		JPanel labelPanel = new JPanel();
		double[] colLabel = {TableLayoutConstants.PREFERRED
								, 50, TableLayoutConstants.PREFERRED};
		double[] rowLabel = {TableLayoutConstants.PREFERRED};
		labelPanel.setLayout(new TableLayout(colLabel, rowLabel));
		labelPanel.add(indexLabel,    "0, 0, c, c");
		labelPanel.add(vizSetLabel,   "2, 0, c, c");
		
		xminField = new JTextField();
		xmaxField = new JTextField();
		yminField = new JTextField();
		ymaxField = new JTextField();
		minField = new JTextField();
		maxField = new JTextField();
		animationIdField = new JTextField();
		
		smoothZonesBox = new JCheckBox("");
		displayDateBox = new JCheckBox("");
		
		frameLabel = new JLabel("Selected Frame");
		xminLabel = new JLabel("Zoom X Min [km]*");
		xmaxLabel = new JLabel("Zoom X Max [km]*");
		yminLabel = new JLabel("Zoom Y Min [km]*");
		ymaxLabel = new JLabel("Zoom Y Max [km]*");
		minLabel = new JLabel("Colortable Range Min");
		maxLabel = new JLabel("Colortable Range Max");
		matplotlibModelLabel = new JLabel("Model*");
		scaleLabel = new JLabel("Scale");
		colorTableLabel = new JLabel("Color Table*");
		animationIdLabel = new JLabel("Unique ID (up to 6 chars)");
		smoothZonesLabel = new JLabel("Smooth Zones?");
		displayDateLabel = new JLabel("Display Date?");
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
		
		frameField = new JTextField();
		frameField.setText(String.valueOf(vsData.getLastFrame()));
		
		slider = new JSlider(JSlider.HORIZONTAL, 1, vsData.getLastFrame(), vsData.getLastFrame());
		slider.addChangeListener(this);
		
		fitBox = new JCheckBox("Fit Frame to Window", true);
		fitBox.addActionListener(this);
		
		framePanel = new FramePanel();
		JScrollPane framePanelPane = new JScrollPane(framePanel);
		framePanel.setScrollPane(framePanelPane);
		framePanel.setFitToWindow(true);
		
		inputPanel = new JPanel();
		inputPanel.setBorder(Borders.getBorder("Animation Parameters"));
		inputPanel.setPreferredSize(new Dimension(350, inputPanel.getPreferredSize().height));
		double[] colInput = {5, TableLayoutConstants.PREFERRED
						, 10, TableLayoutConstants.FILL, 5};
		double[] rowInput = {5, TableLayoutConstants.PREFERRED
						, 10, TableLayoutConstants.PREFERRED
						, 10, TableLayoutConstants.PREFERRED
						, 10, TableLayoutConstants.PREFERRED
						, 10, TableLayoutConstants.PREFERRED
						, 10, TableLayoutConstants.PREFERRED
						, 10, TableLayoutConstants.PREFERRED
						, 10, TableLayoutConstants.PREFERRED
						, 10, TableLayoutConstants.PREFERRED
						, 10, TableLayoutConstants.PREFERRED
						, 10, TableLayoutConstants.PREFERRED
						, 10, TableLayoutConstants.PREFERRED
						, 10, TableLayoutConstants.FILL, 5};
		inputPanel.setLayout(new TableLayout(colInput, rowInput));
		
		inputPanel.add(matplotlibModelLabel, "1, 1, r, c");
		inputPanel.add(modelBox,             "3, 1, f, c");
		inputPanel.add(colorTableLabel,      "1, 3, r, c");
		inputPanel.add(colormapBox,          "3, 3, f, c");
		inputPanel.add(minLabel,             "1, 5, r, c");
		inputPanel.add(minField,             "3, 5, f, c");
		inputPanel.add(maxLabel,             "1, 7, r, c");
		inputPanel.add(maxField,             "3, 7, f, c");
		inputPanel.add(xminLabel,            "1, 9, r, c");
		inputPanel.add(xminField,            "3, 9, f, c");
		inputPanel.add(xmaxLabel,            "1, 11, r, c");
		inputPanel.add(xmaxField,            "3, 11, f, c");
		inputPanel.add(yminLabel,            "1, 13, r, c");
		inputPanel.add(yminField,            "3, 13, f, c");
		inputPanel.add(ymaxLabel,            "1, 15, r, c");
		inputPanel.add(ymaxField,            "3, 15, f, c");
		inputPanel.add(animationIdLabel,     "1, 17, r, c");
		inputPanel.add(animationIdField,     "3, 17, f, c");
		inputPanel.add(scaleLabel,           "1, 19, r, c");
		inputPanel.add(scaleBox,             "3, 19, f, c");
		inputPanel.add(smoothZonesLabel,     "1, 21, r, c");
		inputPanel.add(smoothZonesBox,       "3, 21, f, c");
		inputPanel.add(displayDateLabel,     "1, 23, r, c");
		inputPanel.add(displayDateBox,       "3, 23, f, c");
		inputPanel.add(requiredLabel,        "1, 25, 3, 25, r, b");
		
		previewPanel = new JPanel();
		previewPanel.setBorder(Borders.getBorder("Animation Preview"));
		if(d.getVizSet().getLastFrame()==0){
			double[] colPreview = {10, TableLayoutConstants.PREFERRED
									, 10, TableLayoutConstants.FILL
									, 10, TableLayoutConstants.FILL
									, 10, TableLayoutConstants.PREFERRED, 10};
			double[] rowPreview = {10, TableLayoutConstants.PREFERRED
									, 10, TableLayoutConstants.FILL
									, 10, TableLayoutConstants.PREFERRED, 10};
			previewPanel.setLayout(new TableLayout(colPreview, rowPreview));
			previewPanel.add(topLabel, 	        "1, 1, 7, 1, f, c");
			previewPanel.add(framePanelPane, 	"1, 3, 7, 3, f, f");
			previewPanel.add(frameLabel,     	"1, 5, r, c");
			previewPanel.add(frameField,     	"3, 5, f, c");
			previewPanel.add(slider,       		"5, 5, f, c");
			previewPanel.add(fitBox, 			"7, 5, c, c");
		}else{
			double[] colPreview = {10, TableLayoutConstants.PREFERRED
					, 10, TableLayoutConstants.FILL
					, 10, TableLayoutConstants.FILL
					, 10, TableLayoutConstants.PREFERRED, 10};
			double[] rowPreview = {10, TableLayoutConstants.FILL
					, 10, TableLayoutConstants.PREFERRED, 10};
			previewPanel.setLayout(new TableLayout(colPreview, rowPreview));
			previewPanel.add(framePanelPane, 	"1, 1, 7, 1, f, f");
			previewPanel.add(frameLabel,     	"1, 3, r, c");
			previewPanel.add(frameField,     	"3, 3, f, c");
			previewPanel.add(slider,       		"5, 3, f, c");
			previewPanel.add(fitBox, 			"7, 3, c, c");
		}
		
		double[] col = {10, TableLayoutConstants.PREFERRED
							, 10, TableLayoutConstants.FILL, 10};
		double[] row = {10, TableLayoutConstants.PREFERRED
							, 10, TableLayoutConstants.FILL
							, 10, TableLayoutConstants.PREFERRED, 10};
		setLayout(new TableLayout(col, row));
		add(labelPanel,     "1, 1, 3, 1, c, c");
		add(inputPanel,     "1, 3, c, f");
		add(previewPanel,   "3, 3, f, f");
		add(buttonPanel,    "1, 5, 3, 5, c, c");
		
		setCurrentState();
		generatePreview(vsData.getLastFrame());
	}
	
	/**
	 * Generate preview.
	 *
	 * @param frame the frame
	 */
	private void generatePreview(int frame){
		va = new MatplotlibAnimation();
		va.setParentIndex(vsData.getIndex());
		va.setCurrentFrame(slider.getValue());
		loadMatplotlibAnimationWithParameters(va);
		GenerateMatplotlibAnimationPreviewWorker worker = new GenerateMatplotlibAnimationPreviewWorker(this, va);
		worker.execute();
	}
	
	/**
	 * Load animation with parameters.
	 *
	 * @param a the a
	 */
	private void loadMatplotlibAnimationWithParameters(MatplotlibAnimation va){
		MatplotlibModel matplotlibModel = (MatplotlibModel)modelBox.getSelectedItem();
		MatplotlibColormap colormap = (MatplotlibColormap)colormapBox.getSelectedItem();
		String range = minField.getText().trim() + "," + maxField.getText().trim();
		if(minField.getText().trim().equals("") && maxField.getText().trim().equals("")){
			range = "";
		}
		String zoom = xminField.getText().trim() + "," + xmaxField.getText().trim() + "," 
						+ yminField.getText().trim() + "," + ymaxField.getText().trim();
		Scale scale = (Scale)scaleBox.getSelectedItem();
		boolean smoothZones = smoothZonesBox.isSelected();
		boolean displayDate = displayDateBox.isSelected();
		
		va.setIndex(animationIndex);
		va.setMatplotlibModel(matplotlibModel);
		va.setColormap(colormap);
		va.setRange(range);
		va.setZoom(zoom);
		va.setScale(scale);
		va.setSmoothZones(smoothZones);
		va.setDisplayDate(displayDate);
	}
	
	/**
	 * Sets the frame panel file.
	 *
	 * @param file the new frame panel file
	 */
	public void setFramePanelFile(CustomFile file){
		try{
			framePanel.setFile(file);
		}catch(Exception e){
			e.printStackTrace();
			CaughtExceptionHandler.handleException(e, this);
		}
	}
	
	/**
	 * Sets the current state.
	 */
	private void setCurrentState(){
		if(dataVector.size()>0){
			
			Vector<Object> v = null;
			vInit = null;
			if(rowIndex!=-1){
				v = dataVector.get(rowIndex);
				vInit = v;
				animationIndex = (Integer)v.get(0);
			}else{
				v = dataVector.get(dataVector.size()-1);
				animationIndex = -1;
			}

			animationIdField.setText((String)v.get(1));
			modelBox.setSelectedItem(v.get(2));
			colormapBox.setSelectedItem(v.get(3));
			String zoom = (String)v.get(4);
			String range = (String)v.get(5);
			
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
			smoothZonesBox.setSelected((Boolean)v.get(6));
			displayDateBox.setSelected((Boolean)v.get(7));
			scaleBox.setSelectedItem(v.get(8));
			
		}else{
			
			animationIndex = -1;
			
			animationIdField.setText("");
			modelBox.setSelectedItem(MainData.getMatplotlibModelMap().values().iterator().next());
			colormapBox.setSelectedItem(MainData.getMatplotlibColormapMap().values().iterator().next());
			xminField.setText("-1000");
			xmaxField.setText("1000");
			yminField.setText("0");
			ymaxField.setText("1000");
			minField.setText("");
			maxField.setText("");
			smoothZonesBox.setSelected(true);
			displayDateBox.setSelected(true);
			scaleBox.setSelectedIndex(0);
			
		}
	}
	
	/**
	 * Animation id exists.
	 *
	 * @param animationId the animation id
	 * @param rowIndex the row index
	 * @return true, if successful
	 */
	private boolean animationIdExists(String animationId, int rowIndex){
		Vector<Vector<Object>> vv = dataVector;
		Iterator<Vector<Object>> itr = vv.iterator();
		int rowCounter = 0;
		while(itr.hasNext()){
			Vector<Object> v = itr.next();
			if(rowIndex==rowCounter){
				rowCounter++;
				continue;
			}
			if(((String)v.get(1)).equals(animationId)){
				return true;
			}
			rowCounter++;
		}
		return false;
	}
	
	/**
	 * Animation exists.
	 *
	 * @param matplotlibModel the matplotlibModel
	 * @param size the size
	 * @param colorTable the color table
	 * @param zoom the zoom
	 * @param range the range
	 * @param smoothZones the smooth zones
	 * @param displayDate the display date
	 * @param scale the scale
	 * @param rowIndex the row index
	 * @return true, if successful
	 */
	private boolean animationExists(MatplotlibModel matplotlibModel
										, MatplotlibColormap colorTable
										, String zoom
										, String range
										, Boolean smoothZones
										, Boolean displayDate
										, Scale scale
										, int rowIndex){
		Vector<Vector<Object>> vv = dataVector;
		Iterator<Vector<Object>> itr = vv.iterator();
		int rowCounter = 0;
		while(itr.hasNext()){
			Vector<Object> v = itr.next();
			if(rowIndex==rowCounter && rowIndex!=-1){
				rowCounter++;
				continue;
			}
			rowCounter++;
			
			if(((MatplotlibModel)v.get(2))==matplotlibModel
					&& ((MatplotlibColormap)v.get(3))==colorTable
					&& ((String)v.get(4)).equals(zoom)
					&& ((String)v.get(5)).equals(range)
					&& ((Boolean)v.get(6))==smoothZones
					&& ((Boolean)v.get(7))==displayDate
					&& ((Scale)v.get(8))==scale){
				return true;
			}
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
	 */
	public void stateChanged(ChangeEvent ce) {
		if(ce.getSource()==slider){
			frameField.setText(String.valueOf(slider.getValue()));
		}
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent ae){
		if(ae.getSource()==undoButton){
			String string = "You are about to undo all recent changes to this animation's parameters.<br><br><b>Are you sure?</b>";
			int returnValue = CautionDialog.createCautionDialog(this, string, "Attention!");
			if(returnValue==CautionDialog.YES){
				setCurrentState();
				generatePreview(vsData.getLastFrame());
			}
		}else if(ae.getSource()==previewButton){
			MatplotlibModel matplotlibModel = (MatplotlibModel)modelBox.getSelectedItem();
			if(goodPreview(matplotlibModel)){
				int frame = Integer.valueOf(frameField.getText().trim());
				slider.setValue(frame);
				generatePreview(frame);
			}
		}else if(ae.getSource()==fitBox){
			framePanel.setFitToWindow(fitBox.isSelected());
		}else if(ae.getSource()==modelBox){
			modelBox.setToolTipText(modelBox.getSelectedItem().toString());
		}else if(ae.getSource()==savePreviewButton){
			try{
				savePreviewAsImage(va);
			}catch(Exception e){
				e.printStackTrace();
				CaughtExceptionHandler.handleException(e, this);
			}
		}else if(ae.getSource()==saveButton){
			MatplotlibModel matplotlibModel = (MatplotlibModel)modelBox.getSelectedItem();
			if(goodData(matplotlibModel)){
				String animationId = animationIdField.getText().trim();
				MatplotlibColormap colorTable = (MatplotlibColormap)colormapBox.getSelectedItem();
				String zoom = xminField.getText().trim() + "," + xmaxField.getText().trim() + "," 
								+ yminField.getText().trim() + "," + ymaxField.getText().trim();
				String range = minField.getText().trim() + "," + maxField.getText().trim();
				if((minField.getText().trim().equals("") && maxField.getText().trim().equals(""))){
					range = "";
				}
				Boolean smoothZones = smoothZonesBox.isSelected();
				Boolean displayDate = displayDateBox.isSelected();
				Scale scale = (Scale)scaleBox.getSelectedItem();

				v = new Vector<Object>();
				v.add(animationIndex);
				v.add(animationId);
				v.add(matplotlibModel);
				v.add(colorTable);
				v.add(zoom);
				v.add(range);
				v.add(smoothZones);
				v.add(displayDate);
				v.add(scale);
				
				if(!animationId.equals("") && animationIdExists(animationId, rowIndex)){
					AttentionDialog.createDialog(this, "The <i>Unique ID</i> you entered already exists.");
				}else if(animationExists(matplotlibModel, colorTable, zoom, range, smoothZones, displayDate, scale, rowIndex)){
					AttentionDialog.createDialog(this, "This animation has already been added.");
				}else if(vInit!=null){
					if(matplotlibAnimationChanged(animationId, matplotlibModel, colorTable, zoom, range, smoothZones, displayDate, scale)){
						int value = OverwriteAnimationDialog.createOverwriteAnimationDialog(this);
						if(value==OverwriteAnimationDialog.OVERWRITE){
							setVisible(false);
						}else if(value==OverwriteAnimationDialog.CREATE_NEW){
							if(!animationId.equals("") && animationIdExists(animationId, -1)){
								AttentionDialog.createDialog(this, "The <i>Unique ID</i> you entered already exists.");
							}else{
								v.add(OverwriteAnimationDialog.CREATE_NEW);
								setVisible(false);
							}
						}
					}else{
						v = null;
						setVisible(false);
					}
				}else{
					setVisible(false);
				}
			}
		}else if(ae.getSource()==cancelButton){
			v = null;
			setVisible(false);
		}
	}
	
	/**
	 * Save preview as image.
	 *
	 * @param a the a
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void savePreviewAsImage(MatplotlibAnimation va) throws IOException{
		File file = getImageSaveFile(va.toStringFrameFilename(va.getCurrentFrame()));
		if(file!=null){
			IOUtilities.writeFile(file, framePanel.getFile().getContents());
		}
	}
	
	/**
	 * Gets the image save file.
	 *
	 * @param filename the filename
	 * @return the image save file
	 */
	private File getImageSaveFile(String filename){
		JFileChooser fileDialog = PlainFileChooserFactory.createPlainFileChooser();
		fileDialog.setAcceptAllFileFilterUsed(false);
		fileDialog.addChoosableFileFilter(new CustomFileFilter(FileType.PNG));
		fileDialog.setSelectedFile(new File(filename));
		int returnVal = fileDialog.showSaveDialog(this); 
		MainData.setAbsolutePath(fileDialog.getCurrentDirectory());
		if(returnVal==JFileChooser.APPROVE_OPTION){
			File file = fileDialog.getSelectedFile();
			String filepath = file.getAbsolutePath();
			if(new File(filepath).exists()){
				String msg = "The file " + file.getName() + " exists. Do you want to replace it?";
				int value = CautionDialog.createCautionDialog(this, msg, "Attention!");
				if(value==CautionDialog.NO){
					getImageSaveFile(file.getName());
				}else{
					return file;
				}
			}else{
				return file;
			}
		}
		return null;
	}
	
	/**
	 * Animation changed.
	 *
	 * @param animationId the animation id
	 * @param matplotlibModel the matplotlibModel
	 * @param size the size
	 * @param colorTable the color table
	 * @param zoom the zoom
	 * @param range the range
	 * @param smoothZones the smooth zones
	 * @param displayDate the display date
	 * @param scale the scale
	 * @return true, if successful
	 */
	private boolean matplotlibAnimationChanged(String animationId
										, MatplotlibModel matplotlibModel
										, MatplotlibColormap colorTable
										, String zoom
										, String range
										, Boolean smoothZones
										, Boolean displayDate
										, Scale scale){
		
		
		if(((String)vInit.get(1)).equals(animationId)
					&& ((MatplotlibModel)vInit.get(2))==matplotlibModel
					&& ((MatplotlibColormap)vInit.get(3))==colorTable
					&& ((String)vInit.get(4)).equals(zoom)
					&& ((String)vInit.get(5)).equals(range)
					&& ((Boolean)vInit.get(6))==smoothZones
					&& ((Boolean)vInit.get(7))==displayDate
					&& ((Scale)vInit.get(8))==scale){
			return false;
		}

		return true;
		
	}
	
	/**
	 * Good frame.
	 *
	 * @return true, if successful
	 */
	private boolean goodFrame(){
		try{
			if(frameField.getText().trim().equals("")){
				AttentionDialog.createDialog(this, "Please enter an integer value between 1 and " 
													+ vsData.getLastFrame() 
													+ " for <i>Selected Frame</i>.");
				return false;
			}
			int frame = Integer.valueOf(frameField.getText().trim());
			if(frame<1 || frame>vsData.getLastFrame()){
				AttentionDialog.createDialog(this, "Please enter an integer value between 1 and " 
													+ vsData.getLastFrame() 
													+ " for <i>Selected Frame</i>.");
				return false;
			}
		}catch(NumberFormatException nfe){
			AttentionDialog.createDialog(this, "Please enter an integer value between 1 and " 
												+ vsData.getLastFrame() 
												+ " for <i>Selected Frame</i>.");
			return false;
		}
		return true;
	}
	
	/**
	 * Good zoom.
	 *
	 * @return true, if successful
	 */
	private boolean goodZoom(){
		try{
			if(xminField.getText().trim().equals("")){
				AttentionDialog.createDialog(this, "Please enter a value for <i>Zoom X Min</i>.");
				return false;
			}
			Double.valueOf(xminField.getText().trim());
		}catch(NumberFormatException nfe){
			AttentionDialog.createDialog(this, "Please enter a numeric value for <i>Zoom X Min</i>.");
			return false;
		}
		
		try{
			if(xmaxField.getText().trim().equals("")){
				AttentionDialog.createDialog(this, "Please enter a value for <i>Zoom X Max</i>.");
				return false;
			}
			Double.valueOf(xmaxField.getText().trim());
		}catch(NumberFormatException nfe){
			AttentionDialog.createDialog(this, "Please enter a numeric value for <i>Zoom X Max</i>.");
			return false;
		}
		
		try{
			if(yminField.getText().trim().equals("")){
				AttentionDialog.createDialog(this, "Please enter a value for <i>Zoom Y Min</i>.");
				return false;
			}
			Double.valueOf(yminField.getText().trim());
		}catch(NumberFormatException nfe){
			AttentionDialog.createDialog(this, "Please enter a numeric value for <i>Zoom Y Min</i>.");
			return false;
		}
		
		try{
			if(ymaxField.getText().trim().equals("")){
				AttentionDialog.createDialog(this, "Please enter a value for <i>Zoom Y Max</i>.");
				return false;
			}
			Double.valueOf(ymaxField.getText().trim());
		}catch(NumberFormatException nfe){
			AttentionDialog.createDialog(this, "Please enter a numeric value for <i>Zoom Y Max</i>.");
			return false;
		}
		
		if(Double.valueOf(xminField.getText().trim()) >= Double.valueOf(xmaxField.getText().trim())){
			AttentionDialog.createDialog(this, "Please enter a value for <i>Zoom X Max</i> which is greater than <i>Zoom X Min</i>.");
			return false;
		}
		
		if(Double.valueOf(yminField.getText().trim()) >= Double.valueOf(ymaxField.getText().trim())){
			AttentionDialog.createDialog(this, "Please enter a value for <i>Zoom Y Max</i> which is greater than <i>Zoom Y Min</i>.");
			return false;
		}
		
		return true;
	}
	
	/**
	 * Good range.
	 *
	 * @return true, if successful
	 */
	private boolean goodRange(){
		try{
			if(!minField.getText().trim().equals("") || !maxField.getText().trim().equals("")){
				Double.valueOf(minField.getText().trim());
				Double.valueOf(maxField.getText().trim());
			}
		}catch(NumberFormatException nfe){
			AttentionDialog.createDialog(this, "Please enter a numeric value for <i>Range Min</i> and <i>Range Max</i> or leave both fields empty.");
			return false;
		}
		
		if((!minField.getText().trim().equals("") 
				&& !maxField.getText().trim().equals(""))
				&& ((Scale)scaleBox.getSelectedItem()) == Scale.LOG
				&& (Double.valueOf(minField.getText().trim()) <= 0.0 || Double.valueOf(maxField.getText().trim()) <= 0.0)){
			AttentionDialog.createDialog(this, "You have selected a Log scale. Please enter positive values for <i>Range Min</i> and <i>Range Max</i> or leave both fields empty.");
			return false;
		}
		
		if((!minField.getText().trim().equals("") && !maxField.getText().trim().equals("")) 
				&& (Double.valueOf(minField.getText().trim()) >= Double.valueOf(maxField.getText().trim()))){
			AttentionDialog.createDialog(this, "Please enter a value for <i>Range Max</i> which is greater than <i>Range Min</i> or leave both fields empty.");
			return false;
		}
		
		return true;
	}
	
	/**
	 * Good animation id.
	 *
	 * @return true, if successful
	 */
	private boolean goodAnimationId(){
		if(animationIdField.getText().trim().length()>6 && !animationIdField.getText().trim().equals("")){
			AttentionDialog.createDialog(this, "Please enter a value for <i>Unique ID</i> which is less than or equal 6 characters in length or leave it empty.");
			return false;
		}
		if(!animationIdField.getText().trim().matches("^[A-Za-z0-9_]+$") && !animationIdField.getText().trim().equals("")){
			AttentionDialog.createDialog(this, "Please enter a value for <i>Unique ID</i> which contains only alphanumeric characters or underscores or leave it empty.");
			return false;
		}
		return true;
	}
	
	/**
	 * Good preview.
	 *
	 * @param m the m
	 * @return true, if successful
	 */
	private boolean goodPreview(MatplotlibModel m){
		return goodFrame() && goodZoom() && goodRange() && goodAnimationId();
	}
	
	/**
	 * Good data.
	 *
	 * @param m the m
	 * @return true, if successful
	 */
	private boolean goodData(MatplotlibModel m){
		return goodZoom() && goodRange() && goodAnimationId();
	}
	
}
