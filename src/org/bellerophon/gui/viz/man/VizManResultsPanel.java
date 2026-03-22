/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: VizManResultsPanel.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.viz.man;

import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstants;
import java.awt.Dimension;
import java.awt.event.*;
import javax.swing.*;

import org.bellerophon.data.feature.VizManData;
import org.bellerophon.gui.format.Borders;
import org.bellerophon.gui.format.Buttons;
import org.bellerophon.gui.format.Colors;
import org.bellerophon.gui.util.WordWrapLabel;
import org.bellerophon.gui.viz.man.VizManPanel.VizManMode;

/**
 * The Class VizManResultsPanel.
 *
 * @author Eric J. Lingerfelt
 */
public class VizManResultsPanel extends JPanel implements ActionListener{

	private VizManPanel parent;
	private VizManData d;
	private JButton dataButton;
	private WordWrapLabel endLabel, indexLabel, vizSetLabel;
	
	/**
	 * Instantiates a new viz create results panel.
	 *
	 * @param parent the parent
	 * @param d the d
	 */
	public VizManResultsPanel(VizManPanel parent, VizManData d){
		
		this.parent = parent;
		this.d = d;
		
		endLabel = new WordWrapLabel();
		
		indexLabel = new WordWrapLabel(true);
		indexLabel.setFont(Borders.getBorderFont());
		
		vizSetLabel = new WordWrapLabel(true);
		vizSetLabel.setFont(Borders.getBorderFont());
		
		dataButton = Buttons.getIconButton("Create or Edit Another Visualization Set"
												, "icons/go-previous.png"
												, Buttons.IconPosition.LEFT
												, Colors.GREEN
												, this
												, new Dimension(400, 50));
		
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent ae){
		if(ae.getSource()==dataButton){
			parent.setCurrentState(VizManMode.OPTION);
		}
	}
	
	/**
	 * Sets the current state.
	 */
	public void setCurrentState(){
		
		indexLabel.setText("Visualization Set Index = " + d.getVizSet().getIndex());
		vizSetLabel.setText("Visualization Set Unique ID = " + d.getVizSet().toString());
		
		if(d.getState()==VizManData.VizCreateState.MOD){
			endLabel.setText("The visualization set you modified has been successfully " +
								"submitted and is identified with the index and unique id shown above. " +
								"The frames and movie files for any animations that were modified or added are now being generated");
		}else{
			endLabel.setText("The visualization set you created has been successfully " +
								"submitted and is identified with the above index and unique id shown above.");
		}
		
		removeAll();

		JPanel centerPanel = new JPanel();
		double[] colCenter = {TableLayoutConstants.FILL};
		double[] rowCenter = {TableLayoutConstants.PREFERRED
								, 20, TableLayoutConstants.PREFERRED};
		centerPanel.setLayout(new TableLayout(colCenter, rowCenter));
		centerPanel.add(endLabel,    	"0, 0, c, c");
		centerPanel.add(dataButton,    	"0, 2, c, c");
		
		double[] col = {150, TableLayoutConstants.FILL, 150};
		double[] row = {TableLayoutConstants.PREFERRED
							, 10, TableLayoutConstants.PREFERRED
							, 20, TableLayoutConstants.FILL};
		setLayout(new TableLayout(col, row));
		add(indexLabel, 	"1, 0, c, c");
		add(vizSetLabel, 	"1, 2, c, c");
		add(centerPanel, 	"1, 4, f, c");
		
	}
	
}
