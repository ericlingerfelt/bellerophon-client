/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: VizExplInfoPanel.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.test.expl;

import java.awt.Frame;
import java.awt.event.*;
import java.awt.print.PrinterException;
import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstants;
import javax.swing.*;
import org.bellerophon.data.util.RegressionTest;
import org.bellerophon.export.TextSaver;
import org.bellerophon.gui.format.Borders;

/**
 * The Class VizExplInfoPanel.
 *
 * @author Eric J. Lingerfelt
 */
public class TestExplInfoPanel extends JPanel implements ActionListener{

	private JButton saveButton, copyButton, printButton;
	private JTextArea infoArea;
	private Frame parent;
	private RegressionTest rt;
	
	/**
	 * Instantiates a new viz expl info panel.
	 *
	 * @param parent the parent
	 */
	public TestExplInfoPanel(Frame parent){
	
		this.parent = parent;
		
		saveButton = new JButton("Save");
		saveButton.addActionListener(this);
		
		printButton = new JButton("Print");
		printButton.addActionListener(this);
		
		copyButton = new JButton("Copy");
		copyButton.addActionListener(this);
		
		JPanel buttonPanel = new JPanel();
		double[] colButton = {TableLayoutConstants.PREFERRED
									, 10, TableLayoutConstants.PREFERRED
									, 10, TableLayoutConstants.PREFERRED};
		double[] rowButton = {TableLayoutConstants.PREFERRED};
		buttonPanel.setLayout(new TableLayout(colButton, rowButton));
		buttonPanel.add(saveButton,       "0, 0, c, c");
		buttonPanel.add(printButton,      "2, 0, c, c");
		buttonPanel.add(copyButton,       "4, 0, c, c");
		
		infoArea = new JTextArea();
		infoArea.setWrapStyleWord(true);
		infoArea.setLineWrap(true);
		infoArea.setEditable(false);
		
		JScrollPane infoPane = new JScrollPane(infoArea);
		double[] col = {5, TableLayoutConstants.FILL, 5};
		double[] row = {5, TableLayoutConstants.FILL
							, 5, TableLayoutConstants.PREFERRED, 5};
		setLayout(new TableLayout(col, row));
		add(infoPane, 		"1, 1, f, f");
		add(buttonPanel, 	"1, 3, c, c");
		
	}
	
	/**
	 * Sets the current state.
	 *
	 * @param vs the vs
	 * @param a the a
	 * @param frame the frame
	 */
	public void setCurrentState(RegressionTest rt){
		this.rt = rt;
		String s = ""; 
		String title = "Regression Test Information";
		if(rt!=null){
			s = rt.toInfoString(); 
		}
		setBorder(Borders.getBorder(title));
		if(!s.equals(infoArea.getText())){
			infoArea.setText(s);
			infoArea.setCaretPosition(0);
		}
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent ae){
		if(ae.getSource()==saveButton){
			String filename = "regression_test_info_" + rt.getIndex() + ".txt";
			try {
				TextSaver.saveText(parent, infoArea.getText(), filename);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if(ae.getSource()==printButton){
			try {
				infoArea.print();
			} catch (PrinterException e) {
				e.printStackTrace();
			}
		}else if(ae.getSource()==copyButton){
			infoArea.setSelectionStart(0);
			infoArea.setSelectionEnd(infoArea.getDocument().getLength());
			infoArea.copy();
			infoArea.setCaretPosition(0);
		}
	}

}