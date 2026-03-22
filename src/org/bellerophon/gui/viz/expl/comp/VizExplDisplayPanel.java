/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: VizExplDisplayPanel.java
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

import javax.swing.*;

import org.bellerophon.data.util.*;
import org.bellerophon.enums.VizCompType;
import org.bellerophon.exception.CaughtExceptionHandler;
import org.bellerophon.gui.util.FramePanel;
import org.bellerophon.gui.viz.VizSetUpdateListener;
import org.bellerophon.gui.viz.expl.VizExplViewPanel;

/**
 * The Class VizExplViewPanel.
 *
 * @author Eric J. Lingerfelt
 */
public class VizExplDisplayPanel extends VizExplCompPanel implements ActionListener
																		, VizSetUpdateListener{
	
	private JCheckBox fitBox;
	private FramePanel framePanel;
	
	/**
	 * Instantiates a new viz expl browse panel.
	 *
	 * @param parent the parent
	 * @param panel the panel
	 * @param vs the vs
	 * @param a the a
	 */
	public VizExplDisplayPanel(Frame parent, VizExplViewPanel panel, VizSet vs, Animation a){

		super(parent, panel, vs, a, VizCompType.DISPLAY, 1);
		
		fitBox = new JCheckBox("Fit Frame to Window", true);
		fitBox.addActionListener(this);
		
		framePanel = new FramePanel();
		JScrollPane framePanelPane = new JScrollPane(framePanel);
		framePanel.setScrollPane(framePanelPane);
		framePanel.setFitToWindow(true);
		
		double[] col = {5, TableLayoutConstants.FILL, 5};
		double[] row = {5, TableLayoutConstants.FILL
						, 20, TableLayoutConstants.PREFERRED, 20};
		setLayout(new TableLayout(col, row));
		add(framePanelPane, "1, 1, f, f");
		add(fitBox,     	"1, 3, c, c");
		
		//setCurrentFrame();
	}
	
	/**
	 * Sets the current frame.
	 */
	public void setCurrentFrame(){
		setSelectedFrame();
		a.setCurrentFrame(1);
		//DisplayGraceAnimationFrameWorker worker = new DisplayGraceAnimationFrameWorker(parent, this, vs, (GraceAnimation) a, 1);
		//worker.execute();
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
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent ae){
		if(ae.getSource()==fitBox){
			framePanel.setFitToWindow(fitBox.isSelected());
		}
	}

	@Override
	public void updateState() {
		a.setCurrentFrame(1);
		//DisplayGraceAnimationFrameWorker worker = new DisplayGraceAnimationFrameWorker(parent, this, vs, (GraceAnimation) a, 1);
		//worker.execute();
	}

}