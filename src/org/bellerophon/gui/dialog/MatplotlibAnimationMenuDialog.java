/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: MatplotlibAnimationMenuDialog.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.dialog;

import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstants;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import org.bellerophon.data.util.*;
import org.bellerophon.gui.format.Borders;
import org.bellerophon.gui.format.Buttons;
import org.bellerophon.gui.format.Colors;
import org.bellerophon.gui.util.WordWrapLabel;

/**
 * The Class MatplotlibAnimationMenuDialog.
 *
 * @author Eric J. Lingerfelt
 */
public class MatplotlibAnimationMenuDialog extends JDialog implements ActionListener{
	
	private JButton cancelButton, createButton, movieButton
					, browseButton, snapshotButton, hotButton
					, customButton, /*matplotlibButton,*/ recreateButton;
	private WordWrapLabel vizSetLabel, animationLabel;
	
	/**
	 * The Enum Selection.
	 *
	 * @author Eric J. Lingerfelt
	 */
	public enum Selection
	{
		CANCEL
		, MOVIE
		, BROWSE
		, CREATE
		, SNAPSHOT
		, HOT
		, CUSTOM
		, MATPLOTLIB
		, RECREATE
	}
	private Selection selection;
	
	/**
	 * Creates the animation menu dialog.
	 *
	 * @param frame the frame
	 * @param vs the vs
	 * @param a the a
	 * @return the selection
	 */
	public static Selection createMatplotlibAnimationMenuDialog(Frame frame, VizSet vs, MatplotlibAnimation va){
		MatplotlibAnimationMenuDialog dialog = new MatplotlibAnimationMenuDialog(frame, vs, va);
		dialog.setVisible(true);
		return dialog.selection;
	}
	
	/**
	 * Instantiates a new animation menu dialog.
	 *
	 * @param frame the frame
	 * @param vs the vs
	 * @param a the a
	 */
	public MatplotlibAnimationMenuDialog(Frame frame, VizSet vs, MatplotlibAnimation va){
		
		super(frame, "Select Animation Function", true);
		setSize(515, 405);
		setLocationRelativeTo(frame);
		
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we){
				if(MatplotlibAnimationMenuDialog.this.selection==null){
					MatplotlibAnimationMenuDialog.this.selection = MatplotlibAnimationMenuDialog.Selection.CANCEL;
				}
			} 
		});
		
		vizSetLabel = new WordWrapLabel(true);
		vizSetLabel.setText("Visualization Set = " + vs.toString());
		
		animationLabel = new WordWrapLabel(true);
		animationLabel.setText("Animation = " + va.toString());
		
		createButton = Buttons.getIconButton("Create MP4 Movie"
											, "icons/mp4_icon.png"
											, Buttons.IconPosition.RIGHT
											, Colors.BLUE
											, this
											, new Dimension(230, 50));
							
		cancelButton = Buttons.getIconButton("Close Animation Menu"
											, "icons/process-stop.png"
											, Buttons.IconPosition.RIGHT
											, Colors.RED
											, this
											, new Dimension(230, 50));
		
		movieButton = Buttons.getIconButton("Load Animation Frames"
											, "icons/video-x-generic.png"
											, Buttons.IconPosition.RIGHT
											, Colors.BLUE
											, this
											, new Dimension(230, 50));
		
		browseButton = Buttons.getIconButton("Browse Animation Frames"
											, "icons/system-search.png"
											, Buttons.IconPosition.RIGHT
											, Colors.BLUE
											, this
											, new Dimension(230, 50));
		
		customButton = Buttons.getIconButton("Create Custom Frames"
											, "icons/applications-accessories.png"
											, Buttons.IconPosition.RIGHT
											, Colors.BLUE
											, this
											, new Dimension(230, 50));
		
		snapshotButton = Buttons.getIconButton("Download Snapshot"
											, "icons/camera-photo.png"
											, Buttons.IconPosition.RIGHT
											, Colors.BLUE
											, this
											, new Dimension(230, 50));
		
		hotButton = Buttons.getIconButton("Set Animation Hot"
											, "icons/emblem-important-32.png"
											, Buttons.IconPosition.RIGHT
											, Colors.BLUE
											, this
											, new Dimension(230, 50));
		
		recreateButton = Buttons.getIconButton("Regenerate All Images"
											, "icons/view-refresh.png"
											, Buttons.IconPosition.RIGHT
											, Colors.BLUE
											, this
											, new Dimension(230, 50));

		/*matplotlibButton = Buttons.getIconButton("Download VisIt Python"
											, "icons/matplotlib.png"
											, Buttons.IconPosition.RIGHT
											, Colors.BLUE
											, this
											, new Dimension(230, 50)
											, 0);*/

		JPanel buttonPanel = new JPanel();
		double[] colButton = {5, TableLayoutConstants.FILL
							, 5, TableLayoutConstants.FILL, 5};
		double[] rowButton = {5, TableLayoutConstants.PREFERRED
							, 5, TableLayoutConstants.PREFERRED
							, 5, TableLayoutConstants.PREFERRED
							, 5, TableLayoutConstants.PREFERRED, 5};
		buttonPanel.setLayout(new TableLayout(colButton, rowButton));
		buttonPanel.setBorder(Borders.getBorder("Animation Functions"));
		buttonPanel.add(movieButton,	"1, 1, c, c");
		buttonPanel.add(browseButton,   "1, 3, c, c");
		buttonPanel.add(customButton,	"1, 5, c, c");
		buttonPanel.add(recreateButton,	"1, 7, c, c");
		buttonPanel.add(createButton,   "3, 1, c, c");
		buttonPanel.add(snapshotButton,	"3, 3, c, c");
		buttonPanel.add(hotButton,      "3, 5, c, c");
		//buttonPanel.add(matplotlibButton,    "3, 7, c, c");
		
		double[] col = {10, TableLayoutConstants.FILL, 10};
		double[] row = {10, TableLayoutConstants.PREFERRED
						, 10, TableLayoutConstants.PREFERRED
						, 10, TableLayoutConstants.PREFERRED
						, 10, TableLayoutConstants.PREFERRED, 10};
		setLayout(new TableLayout(col, row));
		
		add(vizSetLabel,	"1, 1, c, c");
		add(animationLabel, "1, 3, c, c");
		add(buttonPanel, 	"1, 5, c, c");
		add(cancelButton, 	"1, 7, c, c");
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent ae){
		if(ae.getSource()==movieButton){
			selection = Selection.MOVIE;
		}else if(ae.getSource()==cancelButton){
			selection = Selection.CANCEL;
		}else if(ae.getSource()==createButton){
			selection = Selection.CREATE;
		}else if(ae.getSource()==browseButton){
			selection = Selection.BROWSE;
		}else if(ae.getSource()==snapshotButton){
			selection = Selection.SNAPSHOT;
		}else if(ae.getSource()==hotButton){
			selection = Selection.HOT;
		}else if(ae.getSource()==customButton){
			selection = Selection.CUSTOM;
		//}else if(ae.getSource()==matplotlibButton){
			//selection = Selection.MATPLOTLIB;
		}else if(ae.getSource()==recreateButton){
			selection = Selection.RECREATE;
		}
		setVisible(false);
	}

}
