/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: TestExplPanel.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.test.expl;

import info.clearthought.layout.*;
import java.awt.*;
import javax.swing.*;
import org.bellerophon.data.feature.*;

/**
 * The Class TestExplPanel.
 *
 * @author Eric J. Lingerfelt
 */
public class TestExplPanel extends JPanel{

	private TestExplData d = new TestExplData();
	private TestExplViewPanel viewPanel;
	private TestExplSelectPanel selectPanel;
	
	/**
	 * The Enum TestExplMode.
	 *
	 * @author Eric J. Lingerfelt
	 */
	public enum TestExplMode {SELECT, VIEW}
	
	/**
	 * Instantiates a new test expl panel.
	 *
	 * @param frame the frame
	 */
	public TestExplPanel(Frame frame){
		double[] col = {10, TableLayoutConstants.FILL, 10};
		double[] row = {10, TableLayoutConstants.FILL, 10};
		setLayout(new TableLayout(col, row));
		viewPanel = new TestExplViewPanel(frame, this, d);
		selectPanel = new TestExplSelectPanel(frame, this, d);
	}
	
	/**
	 * Sets the select panel state.
	 */
	public void setSelectPanelState(){
		selectPanel.setCurrentState();
	}
	
	/**
	 * Sets the view panel state.
	 */
	public void setViewPanelState(){
		viewPanel.setCurrentState();
	}
	
	/**
	 * Sets the current state.
	 *
	 * @param mode the new current state
	 */
	public void setCurrentState(TestExplMode mode){
		removeAll();
		if(mode==TestExplMode.VIEW){
			add(viewPanel, "1, 1, f, f");
		}else if(mode==TestExplMode.SELECT){
			add(selectPanel, "1, 1, f, f");
		}
		validate();
		repaint();
	}
	
	/**
	 * Gets the data.
	 *
	 * @return the data
	 */
	public TestExplData getData(){return d;}
	
}