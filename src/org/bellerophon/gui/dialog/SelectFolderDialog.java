/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: SelectFolderDialog.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.dialog;

import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstants;
import java.awt.Container;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import org.bellerophon.data.MainData;


/**
 * The Class SelectFolderDialog.
 *
 * @author Eric J. Lingerfelt
 */
public class SelectFolderDialog extends JDialog implements ActionListener, TreeSelectionListener{
	
	private JButton selectButton, createButton;
	private DirectoryChooser chooser;
	private JTextField dirField;
	private CreateFolderDialog createFolderDialog;
	private Frame frame;
	private String selectedValue = "";
	
	/**
	 * Creates the select folder dialog.
	 *
	 * @param owner the owner
	 * @return the string
	 */
	public static String createSelectFolderDialog(Frame owner){
		SelectFolderDialog dialog = new SelectFolderDialog(owner);
		dialog.setVisible(true);
		return dialog.selectedValue;
	}
	
	/**
	 * Instantiates a new select folder dialog.
	 *
	 * @param frame the frame
	 */
	public SelectFolderDialog(Frame frame){
		
		super(frame, "Select Download Directory", true);
		
		this.frame = frame;
		
		Container c = getContentPane();
		setSize(625, 470);
		setLocationRelativeTo(frame);
		
		double gap = 20;
		double[] column = {gap, TableLayoutConstants.FILL
							, gap, TableLayoutConstants.PREFERRED, gap};
		double[] row = {gap, TableLayoutConstants.PREFERRED
							, 10, TableLayoutConstants.PREFERRED
							, 10, TableLayoutConstants.PREFERRED
							, 10, TableLayoutConstants.PREFERRED
							, 10, TableLayoutConstants.PREFERRED
							, 10, TableLayoutConstants.FILL, gap};
		c.setLayout(new TableLayout(column, row));
		
		chooser = new DirectoryChooser(MainData.getAbsolutePath(), this);
		chooser.addTreeSelectionListener(this);
		JScrollPane chooserPane = new JScrollPane(chooser);
		
		dirField = new JTextField(20);
		dirField.setEditable(false);
		dirField.setText(chooser.getSelectedDirectory().getAbsolutePath());
		
		JButton closeButton = new JButton("Close");
		closeButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				selectedValue = "";
				setVisible(false);
			}
		});
		
		createButton = new JButton("Create New Directory");
		createButton.addActionListener(this);
		
		selectButton = new JButton("Select Directory");
		selectButton.addActionListener(this);

		JLabel dirLabel = new JLabel("Download Directory");
		
		c.add(chooserPane, "1, 1, 1, 11, f, f");
		c.add(dirLabel, "3, 1, f, c");
		c.add(dirField, "3, 3, f, c");
		c.add(selectButton, "3, 5, f, c");
		c.add(createButton, "3, 7, f, c");
		c.add(closeButton, "3, 9, f, c");	
		
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
	 */
	public void valueChanged(TreeSelectionEvent tse){
		if(chooser.getSelectedDirectory()!=null){
			dirField.setText(chooser.getSelectedDirectory().getAbsolutePath());
		}
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent ae){
		
		if(ae.getSource()==selectButton){
			if(!dirField.getText().trim().equals("")){
				selectedValue = dirField.getText().trim();
				MainData.setAbsolutePath(new File(dirField.getText()));
				setVisible(false);
			}else{
				String string = "Please select a download directory from the tree.";
				MessageDialog.createMessageDialog(frame, string, "Attention!");
			}
		}else if(ae.getSource()==createButton){
			createFolderDialog = new CreateFolderDialog(this, this);
			createFolderDialog.setVisible(true);
		}
		
		if(createFolderDialog!=null && createFolderDialog.isVisible()){
			if(ae.getSource()==createFolderDialog.getCreateButton()){
				if(!createFolderDialog.getField().getText().trim().equals("")){
					chooser.createFolder(createFolderDialog.getField().getText().trim());
					chooser.scrollPathToVisible(chooser.getSelectionPath());
					createFolderDialog.setVisible(false);
					createFolderDialog.dispose();
				}else{
					String string = "Please enter a new firectory name.";
					MessageDialog.createMessageDialog(frame, string, "Attention!");
				}
			}
		}
	}
}