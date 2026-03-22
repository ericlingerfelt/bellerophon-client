/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: UploadAnimationFramesDialog.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.dialog;

import info.clearthought.layout.*;

import java.awt.*;

import javax.swing.*;

import org.bellerophon.data.util.UploadedAnimation;
import org.bellerophon.gui.dialog.worker.OpenDialogWorker;
import org.bellerophon.gui.util.*;

/**
 * The Class DelayDialog.
 *
 * @author Eric J. Lingerfelt
 */
public class UploadAnimationFramesDialog extends JDialog{
	
	private Frame frame;
	private JLabel animationLabel, frameLabel;
	private UploadedAnimation ua;
	
	/**
	 * Instantiates a new delay dialog.
	 *
	 * @param owner the owner
	 * @param string the string
	 * @param title the title
	 */
	public UploadAnimationFramesDialog(Frame owner){
		super(owner, "Please wait...", true);
		this.frame = owner;
		setSize(550, 150);
		
		Container c = getContentPane();
		
		String string = "Please wait while the visualization set is submitted and all images are uploaded.";
		WordWrapLabel textArea = new WordWrapLabel();
		textArea.setText(string);
		
		animationLabel = new JLabel();
		frameLabel = new JLabel();
		
		JScrollPane sp = new JScrollPane(textArea
								, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
								, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		sp.setBorder(null);
		
		double gap = 20;
		double[] col = {gap, TableLayoutConstants.FILL, gap};
		double[] row = {gap, TableLayoutConstants.PREFERRED
						, 15, TableLayoutConstants.PREFERRED
						, 10, TableLayoutConstants.PREFERRED, gap};
		c.setLayout(new TableLayout(col, row));
		
		c.add(sp, 				"1, 1, c, f");
		c.add(animationLabel, 	"1, 3, c, c");
		c.add(frameLabel, 		"1, 5, c, c");
	}
	
	public void setAnimation(UploadedAnimation ua){
		this.ua = ua;
		animationLabel.setText("Currently Uploading Animation Number " + ua.getIndex());
	}
	
	public void setFrame(int frame){
		frameLabel.setText("Currently Uploading Frame Number " + frame + " Out Of " + ua.getNumFrames());
	}
	
	/**
	 * Open.
	 */
	public void open(){
		setLocationRelativeTo(frame);
		OpenDialogWorker task = new OpenDialogWorker(this);
		task.execute();
	}
	
	/**
	 * Close.
	 */
	public void close(){
		setVisible(false);
		dispose();
	}
}