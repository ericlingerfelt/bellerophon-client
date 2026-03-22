/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: VizManOptionPanel.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.viz.man;

import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstants;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.*;

import javax.swing.*;

import org.bellerophon.data.feature.VizManData;
import org.bellerophon.data.util.Resolution;
import org.bellerophon.data.util.VizSet;
import org.bellerophon.gui.dialog.DelayDialog;
import org.bellerophon.gui.format.Buttons;
import org.bellerophon.gui.format.Colors;
import org.bellerophon.gui.viz.man.VizManPanel.VizManMode;
import org.bellerophon.gui.viz.man.worker.*;

/**
 * The Class VizManOptionPanel.
 *
 * @author Eric J. Lingerfelt
 */
public class VizManOptionPanel extends JPanel implements ActionListener{

	private Frame frame;
	private VizManPanel parent;
	private VizManData d;
	private JButton editButton, createButton, deleteButton, rewindButton;
	
	/**
	 * Instantiates a new viz create option panel.
	 *
	 * @param frame the frame
	 * @param parent the parent
	 * @param d the d
	 */
	public VizManOptionPanel(Frame frame, VizManPanel parent, VizManData d){
		
		this.frame = frame;
		this.parent = parent;
		this.d = d;
		
		JLabel topLabel = new JLabel("Select an option below.");
		
		editButton = Buttons.getIconButton("Edit Existing Visualization Set"
											, "icons/applications-accessories.png"
											, Buttons.IconPosition.RIGHT
											, Colors.RED
											, this
											, new Dimension(300, 50));

		createButton = Buttons.getIconButton("Create New Visualization Set"
											, "icons/document-new.png"
											, Buttons.IconPosition.RIGHT
											, Colors.GREEN
											, this
											, new Dimension(300, 50));
		
		deleteButton = Buttons.getIconButton("Delete Existing Visualization Set"
											, "icons/edit-delete.png"
											, Buttons.IconPosition.RIGHT
											, Colors.BLUE
											, this
											, new Dimension(300, 50));
		
		rewindButton = Buttons.getIconButton("Rewind Existing Visualization Set"
											, "icons/media-seek-backward.png"
											, Buttons.IconPosition.RIGHT
											, Colors.BLACK
											, this
											, new Dimension(300, 50));
		
		double[] colEnd = {TableLayoutConstants.PREFERRED};
		double[] rowEnd = {TableLayoutConstants.PREFERRED
							, 30, TableLayoutConstants.PREFERRED
							, 20, TableLayoutConstants.PREFERRED
							, 20, TableLayoutConstants.PREFERRED
							, 20, TableLayoutConstants.PREFERRED};
		setLayout(new TableLayout(colEnd, rowEnd));
		add(topLabel,     "0, 0, c, c");
		add(createButton, "0, 2, c, c");
		add(editButton,   "0, 4, c, c");
		add(rewindButton, "0, 6, c, c");
		add(deleteButton, "0, 8, c, c");
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent ae){
		if(ae.getSource()==createButton){
			VizSet vs = new VizSet();
			vs.setResolution(new Resolution());
			d.setVizSet(vs);
			d.setState(VizManData.VizCreateState.CREATE);
			String string = "Please wait while default Matplotlib animations are loaded for the new Visualization Set.";
			DelayDialog dialog = new DelayDialog(frame, string, "Please wait...");
			GetDefaultMatplotlibAnimationsAndProgenitorsWorker worker = new GetDefaultMatplotlibAnimationsAndProgenitorsWorker(frame, parent, dialog);
			worker.execute();
		}else if(ae.getSource()==editButton){
			d.setState(VizManData.VizCreateState.MOD);
			d.setGetAllData(true);
			String string = "Please wait while visualization data is loaded.";
			DelayDialog dialog = new DelayDialog(frame, string, "Please wait...");
			dialog.open();
			GetVizSetsAndProgenitorsWorker worker = new GetVizSetsAndProgenitorsWorker(frame, parent, dialog, VizManMode.SELECT);
			worker.execute();
		}else if(ae.getSource()==rewindButton){
			d.setGetAllData(true);
			String string = "Please wait while visualization data is loaded.";
			DelayDialog dialog = new DelayDialog(frame, string, "Please wait...");
			dialog.open();
			GetVizSetsWorker worker = new GetVizSetsWorker(frame, parent, dialog, VizManMode.REWIND);
			worker.execute();
		}else if(ae.getSource()==deleteButton){
			d.setGetAllData(true);
			String string = "Please wait while visualization data is loaded.";
			DelayDialog dialog = new DelayDialog(frame, string, "Please wait...");
			dialog.open();
			GetVizSetsWorker worker = new GetVizSetsWorker(frame, parent, dialog, VizManMode.DELETE);
			worker.execute();
		}
	}
	
	/**
	 * Sets the current state.
	 */
	public void setCurrentState(){
		
	}
	
}