/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: DownloadFileDialog.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.dialog;

import info.clearthought.layout.*;
import java.awt.*;
import java.io.*;
import java.text.*;
import javax.swing.*;
import org.bellerophon.data.util.*;
import org.bellerophon.io.*;
import org.bellerophon.gui.dialog.worker.OpenFrameWorker;
import java.awt.event.*;


/**
 * The Class DownloadFileDialog.
 *
 * @author Eric J. Lingerfelt
 */
public class DownloadFileDialog extends JFrame implements BytesReadListener, ActionListener{
	
	private JProgressBar bar;
	private Frame owner;
	private File file;
	private CustomFile customFile;
	private DecimalFormat format;
	private JButton minimizeButton;
	private int counter = 0;
	
	
	/**
	 * Instantiates a new download file dialog.
	 *
	 * @param owner the owner
	 * @param file the file
	 * @param customFile the custom file
	 */
	public DownloadFileDialog(Frame owner, File file, CustomFile customFile){
		
		this.owner = owner;
		this.file = file;
		this.customFile = customFile;
		
		format = new DecimalFormat("########.0");
		setSize(750, 115);
		String filename = "";
		if(file==null){
			filename = customFile.getName();
		}else{
			filename = file.getName();
		}
		setTitle("Downloading " + filename
					+ ": 0.0 kB out of " 
					+ format.format(customFile.getSize()/1024.0) + "kB");

		bar = new JProgressBar();
		bar.setStringPainted(true);
		bar.setBorderPainted(true);
		bar.setMaximum(customFile.getSize());
		bar.setMinimum(0);
		
		minimizeButton = new JButton("Minimize");
		minimizeButton.addActionListener(this);
		
		Container c = this.getContentPane();
		double[] col = {10, TableLayoutConstants.FILL, 10};
		double[] row = {10, TableLayoutConstants.FILL
						, 10, TableLayoutConstants.PREFERRED, 10};
		c.setLayout(new TableLayout(col, row));
		c.add(bar, "1, 1, f, c");
		c.add(minimizeButton, "1, 3, r, c");
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent ae){
		if(ae.getSource()==minimizeButton){
			setState(ICONIFIED);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.bellerophon.io.BytesReadListener#setBytesRead(int)
	 */
	public void setBytesRead(int bytesRead){
		if(counter==25){
			bar.setValue(bytesRead);
			String filename = "";
			if(file==null){
				filename = customFile.getName();
			}else{
				filename = file.getName();
			}
			setTitle("Downloading " + filename + ": " 
					+ format.format(bytesRead/1024.0) + " kB out of " 
					+ format.format(customFile.getSize()/1024.0) + "kB");
			validate();
			repaint();
			counter = 0;
		}else{
			counter++;
		}
	}

	/**
	 * Open.
	 */
	public void open(){
		setLocationRelativeTo(owner);
		OpenFrameWorker task = new OpenFrameWorker(this);
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