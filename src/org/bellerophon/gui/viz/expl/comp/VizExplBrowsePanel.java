/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: VizExplBrowsePanel.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.viz.expl.comp;

import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstants;

import java.awt.Frame;
import java.awt.event.*;
import java.io.File;
import java.text.DecimalFormat;

import javax.swing.*;
import javax.swing.event.*;

import org.bellerophon.data.MainData;
import org.bellerophon.data.util.*;
import org.bellerophon.enums.VizCompType;
import org.bellerophon.exception.CaughtExceptionHandler;
import org.bellerophon.file.CustomFileFilter;
import org.bellerophon.file.FileType;
import org.bellerophon.gui.dialog.AttentionDialog;
import org.bellerophon.gui.dialog.CautionDialog;
import org.bellerophon.gui.format.Buttons;
import org.bellerophon.gui.format.Colors;
import org.bellerophon.gui.util.FramePanel;
import org.bellerophon.gui.util.PlainFileChooserFactory;
import org.bellerophon.gui.viz.VizSetUpdateListener;
import org.bellerophon.gui.viz.expl.VizExplViewPanel;
import org.bellerophon.gui.viz.expl.worker.BrowseUploadedAnimationFrameWorker;
import org.bellerophon.gui.viz.expl.worker.BrowseMatplotlibAnimationFrameWorker;
import org.bellerophon.gui.viz.expl.worker.CreateAndGetMatplotlibAnimationPythonfileWorker;
import org.bellerophon.gui.viz.expl.worker.RecreateMatplotlibAnimationFrameWorker;

/**
 * The Class VizExplBrowsePanel.
 *
 * @author Eric J. Lingerfelt
 */
public class VizExplBrowsePanel extends VizExplCompPanel implements ActionListener
																		, ChangeListener
																		, VizSetUpdateListener{
	
	private JButton gotoButton, /*matplotlibButton,*/ recreateButton;
	private JSlider slider;
	private JTextField frameField;
	private JCheckBox fitBox;
	private FramePanel framePanel;
	private JLabel maxFrameLabel;
	private int lastFrame;
	
	/**
	 * Instantiates a new viz expl browse panel.
	 *
	 * @param parent the parent
	 * @param panel the panel
	 * @param vs the vs
	 * @param a the a
	 */
	public VizExplBrowsePanel(Frame parent, VizExplViewPanel panel, VizSet vs, Animation a){

		super(parent, panel, vs, a, VizCompType.BROWSE);
		
		if(a instanceof UploadedAnimation){
			lastFrame = ((UploadedAnimation) a).getNumFrames();
		}else{
			lastFrame = vs.getLastFrame();
		}
		
		JLabel frameLabel = new JLabel("Current Frame Is");
		maxFrameLabel = new JLabel("Out Of " + lastFrame);
		
		frameField = new JTextField(5);
		frameField.setText(String.valueOf(lastFrame));
		
		fitBox = new JCheckBox("Fit to Window", true);
		fitBox.addActionListener(this);
		
		gotoButton = Buttons.getIconButton("icons/edit-redo.png"
											, Colors.backColor
											, this
											, "Jump To Selected Frame");
		
		recreateButton = Buttons.getIconButton("icons/view-refresh.png"
											, Colors.backColor
											, this
											, "Regenerate Image");
		
		/*matplotlibButton = Buttons.getIconButton("icons/matplotlib.png"
											, Colors.MATPLOTLIB_PURPLE
											, this
											, "Download VisIt Python");*/
		
		slider = new JSlider(JSlider.HORIZONTAL, 1, lastFrame, lastFrame);
		slider.addChangeListener(this);
		
		framePanel = new FramePanel();
		JScrollPane framePanelPane = new JScrollPane(framePanel);
		framePanel.setScrollPane(framePanelPane);
		framePanel.setFitToWindow(true);
		
		JPanel inputPanel = new JPanel();
		
		if(a instanceof MatplotlibAnimation){
		
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
			inputPanel.add(gotoButton,  	 "8, 0, 8, 2, c, c");
			inputPanel.add(recreateButton, 	 "10, 0, 10, 2, c, c");
			//inputPanel.add(matplotlibButton,  	 "12, 0, 12, 2, c, c");
		
		}
		
		double[] col = {5, TableLayoutConstants.FILL, 5};
		double[] row = {5, TableLayoutConstants.FILL
						, 5, TableLayoutConstants.PREFERRED, 5};
		setLayout(new TableLayout(col, row));
		add(framePanelPane, "1, 1, f, f");
		add(inputPanel,     "1, 3, c, c");
		
		//setCurrentFrame();
	}
	
	/**
	 * Sets the current frame.
	 */
	public void setCurrentFrame(){
		a.setCurrentFrame(frame);
		setSelectedFrame();
		if(a instanceof MatplotlibAnimation){
			BrowseMatplotlibAnimationFrameWorker worker = new BrowseMatplotlibAnimationFrameWorker(parent, this, vs, (MatplotlibAnimation) a, frame);
			worker.execute();
		}else if(a instanceof UploadedAnimation){
			BrowseUploadedAnimationFrameWorker worker = new BrowseUploadedAnimationFrameWorker(parent, this, vs, (UploadedAnimation) a, frame);
			worker.execute();
		}
	}
	
	/**
	 * Sets the frame panel file.
	 *
	 * @param frameFile the new frame panel file
	 */
	public void setFramePanelFile(CustomFile frameFile){
		this.frameFile = frameFile;
		try{
			framePanel.setFile(frameFile);
		}catch(Exception e){
			e.printStackTrace();
			CaughtExceptionHandler.handleException(e, parent);
		}
	}
	
	/**
	 * Good data.
	 *
	 * @return true, if successful
	 */
	private boolean goodData(){
		try{
			if(frameField.getText().trim().equals("")){
				AttentionDialog.createDialog(parent, "Please enter an integer value between 1 and " 
													+ lastFrame
													+ " for <i>Selected Frame</i>.");
				return false;
			}
			int frame = Integer.valueOf(frameField.getText().trim());
			if(frame<1 || frame>lastFrame){
				AttentionDialog.createDialog(parent, "Please enter an integer value between 1 and " 
													+ lastFrame
													+ " for <i>Selected Frame</i>.");
				return false;
			}
		}catch(NumberFormatException nfe){
			AttentionDialog.createDialog(parent, "Please enter an integer value between 1 and " 
												+ lastFrame
												+ " for <i>Selected Frame</i>.");
			return false;
		}
		return true;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent ae){
		if(ae.getSource()==gotoButton){
			if(goodData()){
				frame = Integer.valueOf(frameField.getText().trim());
				slider.setValue(frame);
				setCurrentFrame();	
			}
		/*}else if(ae.getSource()==matplotlibButton){
			if(goodData()){
				frame = Integer.valueOf(frameField.getText().trim());
				slider.setValue(frame);
				a.setCurrentFrame(frame);
				setSelectedFrame();	
				saveImageAsPython((MatplotlibAnimation) a);
			}*/
		}else if(ae.getSource()==recreateButton){
			if(goodData()){
				frame = Integer.valueOf(frameField.getText().trim());
				slider.setValue(frame);
				a.setCurrentFrame(frame);
				setSelectedFrame();	
				RecreateMatplotlibAnimationFrameWorker worker = new RecreateMatplotlibAnimationFrameWorker(parent, this, vs, (MatplotlibAnimation) a, a.getCurrentFrame());
				worker.execute();
			}
		}else if(ae.getSource()==fitBox){
			framePanel.setFitToWindow(fitBox.isSelected());
		}
	}
	
	private void saveImageAsPython(MatplotlibAnimation va){
		File file = getPythonSaveDir(va.toStringPythonFilename(va.getCurrentFrame()));
		if(file!=null){
			CustomFile pythonfile = new CustomFile();
			pythonfile.setName(String.valueOf(MainData.getID()) 
							+ "_"
							+ String.valueOf(va.getParentIndex())
							+ "_"
							+ String.valueOf(va.getIndex())
							+ "_"
							+ new DecimalFormat("00000").format(va.getCurrentFrame())
							+ ".py");
			String pathSuffix = "tmp/" + pythonfile.getName();
			pythonfile.setPath(MainData.MEDIA_URL + "/viz_sets/" + pathSuffix);
			va.setPythonfile(pythonfile);
			va.setExportAllFramesForPython(false);
			CreateAndGetMatplotlibAnimationPythonfileWorker worker = new CreateAndGetMatplotlibAnimationPythonfileWorker(parent, va, file);
			worker.execute();
		}
	}
	
	/**
	 * Gets the tarfile save dir.
	 *
	 * @param filename the filename
	 * @return the tarfile save dir
	 */
	private File getPythonSaveDir(String filename){
		JFileChooser fileDialog = PlainFileChooserFactory.createPlainFileChooser();
		fileDialog.setAcceptAllFileFilterUsed(false);
		fileDialog.addChoosableFileFilter(new CustomFileFilter(FileType.PY));
		fileDialog.setSelectedFile(new File(filename));
		int returnVal = fileDialog.showSaveDialog(parent); 
		MainData.setAbsolutePath(fileDialog.getCurrentDirectory());
		if(returnVal==JFileChooser.APPROVE_OPTION){
			File file = fileDialog.getSelectedFile();
			String filepath = file.getAbsolutePath();
			if(new File(filepath).exists()){
				String msg = "The file " + file.getName() + " exists. Do you want to replace it?";
				int value = CautionDialog.createCautionDialog(parent, msg, "Attention!");
				if(value==CautionDialog.NO){
					getPythonSaveDir(file.getName());
				}else{
					return file;
				}
			}else{
				return file;
			}
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
	 */
	public void stateChanged(ChangeEvent ce){
		if(ce.getSource()==slider){
			frameField.setText(String.valueOf(slider.getValue()));
		}
	}

	@Override
	public void updateState() {
		if(a instanceof UploadedAnimation){
			lastFrame = ((UploadedAnimation) a).getNumFrames();
		}else{
			lastFrame = vs.getLastFrame();
		}
		slider.removeChangeListener(this);
		slider.setMaximum(lastFrame);
		maxFrameLabel.setText("Out Of " + lastFrame);
		slider.addChangeListener(this);
	}
}