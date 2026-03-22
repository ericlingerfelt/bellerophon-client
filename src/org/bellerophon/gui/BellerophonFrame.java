/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: BellerophonFrame.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui;

import info.clearthought.layout.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import org.bellerophon.data.*;
import org.bellerophon.gui.dialog.*;
import org.bellerophon.gui.format.*;
import org.bellerophon.gui.stat.*;
import org.bellerophon.gui.stat.worker.GetRevisionsWorker;
import org.bellerophon.gui.test.expl.*;
import org.bellerophon.gui.test.expl.worker.GetRegressionTestsWorker;
import org.bellerophon.gui.viz.expl.*;
import org.bellerophon.gui.viz.expl.worker.GetVizSetDataWorker;
import org.bellerophon.gui.viz.man.*;
import org.bellerophon.gui.info.*;
import org.bellerophon.enums.*;
import org.bellerophon.enums.Action;
import org.bellerophon.io.*;

/**
 * The Class BellerophonFrame.
 *
 * @author Eric J. Lingerfelt
 */
public class BellerophonFrame extends JFrame implements ActionListener{

	private FeatureButton logoutButton, testExplButton, statButton
							, vizExplButton, vizCreateButton, infoButton;
	
	private TestExplPanel testExplPanel = new TestExplPanel(this);
	private StatPanel statPanel = new StatPanel(this);
	private VizExplPanel vizExplPanel = new VizExplPanel(this);
	private VizManPanel vizManPanel = new VizManPanel(this, vizExplPanel);
	private InfoPanel infoPanel = new InfoPanel(this);
	private IntroPanel introPanel;
	private Container c;
	private JPanel toolPanel;
	private DelayDialog testExplDialog, statDialog, vizExplDialog;
	
	/**
	 * Instantiates a new bellerophon frame.
	 */
	public BellerophonFrame(){
		
		setSize(1370, 900);
		setTitle("Bellerophon");

		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we){
				if(MainData.isDownloading()){
					String string = "One or more downloads are currently active. Log out and quit anyway?";
					int returnValue = CautionDialog.createCautionDialog(BellerophonFrame.this, string, "Attention!");
					if(returnValue==CautionDialog.YES){
						exit();
					}
				}else{
					exit();
				}
			} 
		});
		
		double gap = 10;
		double[] col = {TableLayoutConstants.FILL};
		double[] row = {TableLayoutConstants.PREFERRED
							, 50, TableLayoutConstants.PREFERRED};
		
		c = getContentPane();
		c.setLayout(new TableLayout(col, row));
		
		double[] colTool = {10, TableLayoutConstants.FILL
								, gap, TableLayoutConstants.FILL
								, gap, TableLayoutConstants.FILL
								, gap, TableLayoutConstants.FILL
								, gap, TableLayoutConstants.FILL
								, gap, TableLayoutConstants.FILL, 10};
		double[] rowTool = {10, TableLayoutConstants.PREFERRED, 10};
		
		toolPanel = new JPanel(new TableLayout(colTool, rowTool));
		
		testExplButton = new FeatureButton(Feature.TEST_EXPL);
		testExplButton.addActionListener(this);
		testExplButton.setPreferredSize(new Dimension(1000, 50));
		testExplButton.setEnabled(false);
		
		vizExplButton = new FeatureButton(Feature.VIZ_EXPL);
		vizExplButton.addActionListener(this);
		vizExplButton.setPreferredSize(new Dimension(1000, 50));
		
		vizCreateButton = new FeatureButton(Feature.VIZ_CREATE);
		vizCreateButton.addActionListener(this);
		vizCreateButton.setPreferredSize(new Dimension(1000, 50));
		
		statButton = new FeatureButton(Feature.STAT);
		statButton.addActionListener(this);
		statButton.setPreferredSize(new Dimension(1000, 50));
		statButton.setEnabled(false);
		
		infoButton = new FeatureButton(Feature.INFO);
		infoButton.addActionListener(this);
		infoButton.setPreferredSize(new Dimension(1000, 50));
		infoButton.setEnabled(false);
		
		logoutButton = new FeatureButton(Feature.LOGOUT);
		logoutButton.addActionListener(this);
		logoutButton.setPreferredSize(new Dimension(1000, 50));
		
		toolPanel.add(testExplButton, "1, 1, f, c");
		toolPanel.add(vizExplButton, "3, 1, f, c");
		toolPanel.add(vizCreateButton, "5, 1, f, c");
		toolPanel.add(statButton, "7, 1, f, c");
		toolPanel.add(infoButton, "9, 1, f, c");
		toolPanel.add(logoutButton, "11, 1, f, c");
		
		introPanel = new IntroPanel();
		
		c.add(toolPanel, "0, 0, c, c");
		c.add(introPanel, "0, 2, c, c");
		
		String testExplString = "Please wait while regression test data is loaded.";
		testExplDialog = new DelayDialog(this, testExplString, "Please wait...");
		
		String vizExplString = "Please wait while visualization data is loaded.";
		vizExplDialog = new DelayDialog(this, vizExplString, "Please wait...");
		
		String statString = "Please wait while svn revision data is loaded. This may take several minutes.";
		statDialog = new DelayDialog(this, statString, "Please wait...");
		
	}
	
	/**
	 * Sets the selected button.
	 *
	 * @param button the new selected button
	 */
	public void setSelectedButton(JButton button){
		//testExplButton.setForeground(Colors.frontColor);
		vizExplButton.setForeground(Colors.frontColor);
		vizCreateButton.setForeground(Colors.frontColor);
		//statButton.setForeground(Colors.frontColor);
		//infoButton.setForeground(Colors.frontColor);
		logoutButton.setForeground(Colors.frontColor);
		button.setForeground(Colors.RED);
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent ae){
		
		if(ae.getSource() instanceof FeatureButton){
			FeatureButton button = (FeatureButton)ae.getSource();
			setSelectedButton(button);
			setTitle("Bellerophon | " + button.getFeature());
		}
		
		if(ae.getSource()==testExplButton){
			if(testExplPanel.getData().getTestMap()==null){
				testExplDialog.open();
				GetRegressionTestsWorker worker = new GetRegressionTestsWorker(this, testExplPanel, testExplDialog);
				worker.execute();
			}else{
				addPanel(testExplPanel);
			}
		}else if(ae.getSource()==vizExplButton){
			vizExplPanel.getData().setGetAllData(true);
			vizExplDialog.open();
			GetVizSetDataWorker worker = new GetVizSetDataWorker(this, vizExplPanel, vizExplDialog);
			worker.execute();
		}else if(ae.getSource()==vizCreateButton){
			addPanel(vizManPanel);
		}else if(ae.getSource()==statButton){
			if(statPanel.getData().getLogMap()==null){
				statDialog.open();
				GetRevisionsWorker worker = new GetRevisionsWorker(this, statPanel, statDialog);
				worker.execute();
			}else{
				addPanel(statPanel);
			}
		}else if(ae.getSource()==infoButton){
			infoPanel.setCurrentState();
			addPanel(infoPanel);
		}else if(ae.getSource()==logoutButton){
			if(MainData.isDownloading()){
				String string = "One or more downloads are currently active. Log out and quit anyway?";
				int returnValue = CautionDialog.createCautionDialog(BellerophonFrame.this, string, "Attention!");
				if(returnValue==CautionDialog.YES){
					exit();
				}
			}else{
				exit();
			}
		}
	}
	
	/**
	 * Exit.
	 */
	public void exit(){
		setVisible(false);
		Thread.setDefaultUncaughtExceptionHandler(null);
		WebServiceCom.getInstance().doWebServiceComCall(null, Action.LOGOUT);
		System.exit(0);
	}

	/**
	 * Adds the panel.
	 *
	 * @param panel the panel
	 */
	public void addPanel(JPanel panel){
		double[] col = {TableLayoutConstants.FILL};
		double[] row = {TableLayoutConstants.PREFERRED
							, 0, TableLayoutConstants.FILL};
		c.setLayout(new TableLayout(col, row));
		c.removeAll();
		c.add(toolPanel, "0, 0, f, c");
		c.add(panel, "0, 2, f, f");
		validate();
		repaint();
	}
	
}

class FeatureButton extends JButton{
	
	private Feature feature;
	
	public FeatureButton(Feature feature){
		super(feature.getHTMLString());
		this.feature = feature;
	}
	
	public Feature getFeature(){return feature;}
}