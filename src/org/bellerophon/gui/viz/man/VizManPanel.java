/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: VizCreatePanel.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.viz.man;

import info.clearthought.layout.*;
import java.awt.*;
import java.util.TreeMap;

import javax.swing.*;
import org.bellerophon.data.feature.*;
import org.bellerophon.data.util.VizSet;
import org.bellerophon.gui.viz.expl.VizExplPanel;

/**
 * The Class VizCreatePanel.
 *
 * @author Eric J. Lingerfelt
 */
public class VizManPanel extends JPanel{
	
	private VizManData d = new VizManData();
	private VizExplPanel vizExplPanel;
	
	private VizManOptionPanel optionPanel;
	private VizManSelectPanel editPanel;
	private VizManSelectPanel deletePanel;
	private VizManSelectPanel rewindPanel;
	private VizManVizSetPanel vizSetPanel;
	private VizManMatplotlibAnimationPanel animationPanel;
	private VizManUploadAnimationPanel uploadPanel;
	private VizManResultsPanel resultsPanel;
	
	/**
	 * The Enum VizManMode.
	 *
	 * @author Eric J. Lingerfelt
	 */
	public enum VizManMode {OPTION
							, SELECT
							, DELETE
							, REWIND
							, VIZ_SET
							, ANIMATION
							, UPLOAD
							, RESULTS}
	
	/**
	 * Instantiates a new viz create panel.
	 *
	 * @param frame the frame
	 */
	public VizManPanel(Frame frame, VizExplPanel vizExplPanel){
		double[] col = {10, TableLayoutConstants.FILL, 10};
		double[] row = {10, TableLayoutConstants.FILL, 10};
		setLayout(new TableLayout(col, row));
		optionPanel = new VizManOptionPanel(frame, this, d);
		editPanel = new VizManSelectPanel(frame, this, d);
		deletePanel = new VizManSelectPanel(frame, this, d);
		rewindPanel = new VizManSelectPanel(frame, this, d);
		vizSetPanel = new VizManVizSetPanel(frame, this, d);
		animationPanel = new VizManMatplotlibAnimationPanel(frame, this, d);
		uploadPanel = new VizManUploadAnimationPanel(frame, this, d);
		resultsPanel = new VizManResultsPanel(this, d);
		setCurrentState(VizManMode.OPTION);
		this.vizExplPanel = vizExplPanel;
	}
	
	/**
	 * Sets the option panel state.
	 */
	public void setOptionPanelState(){
		optionPanel.setCurrentState();
	}
	
	/**
	 * Sets the edit panel state.
	 */
	public void setEditPanelState(){
		editPanel.setCurrentState(VizManMode.SELECT);
	}
	
	/**
	 * Sets the delete panel state.
	 */
	public void setDeletePanelState(){
		deletePanel.setCurrentState(VizManMode.DELETE);
	}
	
	/**
	 * Sets the rewind panel state.
	 */
	public void setRewindPanelState(){
		rewindPanel.setCurrentState(VizManMode.REWIND);
	}
	
	/**
	 * Sets the viz set panel state.
	 */
	public void setVizSetPanelState(){
		vizSetPanel.setCurrentState();
	}
	
	/**
	 * Sets the animation panel state.
	 *
	 * @param initialize the new animation panel state
	 */
	public void setMatplotlibAnimationPanelState(boolean initialize){
		animationPanel.setCurrentState(initialize);
	}
	
	public void setUploadAnimationPanelState(boolean initialize){
		uploadPanel.setCurrentState(initialize);
	}
	
	/**
	 * Sets the results panel state.
	 */
	public void setResultsPanelState(){
		resultsPanel.setCurrentState();
	}
	
	/**
	 * Sets the mode.
	 *
	 * @param mode the new mode
	 */
	private void setMode(VizManMode mode){
		removeAll();
		if(mode==VizManMode.OPTION){
			add(optionPanel, "1, 1, c, c");
		}else if(mode==VizManMode.SELECT){
			add(editPanel, "1, 1, f, f");
		}else if(mode==VizManMode.DELETE){
			add(deletePanel, "1, 1, f, f");
		}else if(mode==VizManMode.REWIND){
			add(rewindPanel, "1, 1, f, f");
		}else if(mode==VizManMode.VIZ_SET){
			add(vizSetPanel, "1, 1, f, f");
		}else if(mode==VizManMode.ANIMATION){
			add(animationPanel, "1, 1, f, f");
		}else if(mode==VizManMode.UPLOAD){
			add(uploadPanel, "1, 1, f, f");
		}else if(mode==VizManMode.RESULTS){
			updateVizExplPanelAfterVizSetModification();
			add(resultsPanel, "1, 1, c, c");
		}
		validate();
		repaint();
	}
	
	public void updateVizExplPanelAfterVizSetModification(){
		vizExplPanel.updateAfterVizSetModification();
	}
	
	public void updateVizExplPanelAfterVizSetModification(TreeMap<Integer, VizSet> vizSetMap){
		vizExplPanel.updateAfterVizSetModification(vizSetMap);
	}
	
	/**
	 * Sets the current state.
	 *
	 * @param mode the new current state
	 */
	public void setCurrentState(VizManMode mode){
		if(mode==VizManMode.OPTION){
			setOptionPanelState();
		}else if(mode==VizManMode.SELECT){
			setEditPanelState();
		}else if(mode==VizManMode.DELETE){
			setDeletePanelState();
		}else if(mode==VizManMode.REWIND){
			setRewindPanelState();
		}else if(mode==VizManMode.VIZ_SET){
			setVizSetPanelState();
		}else if(mode==VizManMode.ANIMATION){
			setMatplotlibAnimationPanelState(true);
		}else if(mode==VizManMode.UPLOAD){
			setUploadAnimationPanelState(true);
		}else if(mode==VizManMode.RESULTS){
			setResultsPanelState();
		}
		setMode(mode);
	}
	
	/**
	 * Gets the data.
	 *
	 * @return the data
	 */
	public VizManData getData(){return d;}
	
}
