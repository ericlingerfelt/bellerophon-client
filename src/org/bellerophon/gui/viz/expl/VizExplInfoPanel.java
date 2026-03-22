/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: VizExplInfoPanel.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.viz.expl;

import java.awt.Frame;
import java.awt.event.*;
import java.awt.print.PrinterException;

import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstants;

import javax.swing.*;

import org.bellerophon.data.util.Animation;
import org.bellerophon.data.util.MatplotlibAnimation;
import org.bellerophon.data.util.VizJob;
import org.bellerophon.data.util.VizSet;
import org.bellerophon.export.TextSaver;
import org.bellerophon.gui.dialog.AttentionDialog;
import org.bellerophon.gui.format.Borders;
import org.bellerophon.gui.viz.VizSetUpdateListener;

/**
 * The Class VizExplInfoPanel.
 *
 * @author Eric J. Lingerfelt
 */
public class VizExplInfoPanel extends JPanel implements ActionListener
														, VizSetUpdateListener{

	private JButton saveButton, copyButton, printButton;
	private JCheckBox reportBox;
	private JTextArea infoArea;
	private Frame parent;
	private VizSet vs;
	private Animation a;
	private int frame;
	
	/**
	 * Instantiates a new viz expl info panel.
	 *
	 * @param parent the parent
	 */
	public VizExplInfoPanel(Frame parent){
	
		this.parent = parent;
		
		saveButton = new JButton("Save");
		saveButton.addActionListener(this);
		
		printButton = new JButton("Print");
		printButton.addActionListener(this);
		
		copyButton = new JButton("Copy");
		copyButton.addActionListener(this);
		
		reportBox = new JCheckBox("Show Full Info?", true);
		reportBox.addActionListener(this);
		
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
							, 5, TableLayoutConstants.PREFERRED
							, 5, TableLayoutConstants.PREFERRED, 5};
		setLayout(new TableLayout(col, row));
		add(infoPane, 		"1, 1, f, f");
		add(reportBox, 		"1, 3, c, c");
		add(buttonPanel, 	"1, 5, c, c");
		
	}
	
	public String getFullReportText(){
		String s = ""; 
		s = vs.toInfoString(true); 
		s += a.toStringInfo(true);
		if(a instanceof MatplotlibAnimation){
			VizJob vj = vs.getVizJobForFrame(frame);
			if(vj!=null){
				s += vj.toInfoString(frame, true);
			}
		}
		return s;
	}
	
	public void setCurrentState(VizSet vs, Animation a){
		setCurrentState(vs, a, frame);
	}
	
	/**
	 * Sets the current state.
	 *
	 * @param vs the vs
	 * @param a the a
	 * @param frame the frame
	 */
	public void setCurrentState(VizSet vs, Animation a, int frame){
		this.vs = vs;
		this.a = a;
		this.frame = frame;
		String s = ""; 
		String title = "Information";
		if(vs!=null){
			s = vs.toInfoString(reportBox.isSelected()); 
			title = "Viz Set Information";
			if(a!=null){
				s += a.toStringInfo(reportBox.isSelected());
				title = "Animation Information";
				if(a instanceof MatplotlibAnimation){
					VizJob vj = vs.getVizJobForFrame(frame);
					if(vj!=null){
						s += vj.toInfoString(frame, reportBox.isSelected());
					}
				}
			}
		}
		setBorder(Borders.getBorder(title));
		if(!s.equals(infoArea.getText())){
			infoArea.setText(s);
			infoArea.setCaretPosition(0);
		}
	}

	public void updateState(){
		setCurrentState(vs, a, frame);
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent ae){
		if(ae.getSource()==reportBox){
			setCurrentState(vs, a, frame);
		}else if(ae.getSource()==saveButton){
			if(infoArea.getText().trim().equals("")){
				String string = "Please select a node from the visualization set tree.";
				AttentionDialog.createDialog(parent, string);
			}else{
				String filename = "";
				if(a!=null){
					filename = "animation_info_" + a.getIndex() + ".txt";
				}else if(vs!=null){
					filename = "viz_set_info_" + vs.getIndex() + ".txt";
				}
				try {
					TextSaver.saveText(parent, infoArea.getText(), filename);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}else if(ae.getSource()==printButton){
			if(infoArea.getText().trim().equals("")){
				String string = "Please select a node from the visualization set tree.";
				AttentionDialog.createDialog(parent, string);
			}else{
				try {
					infoArea.print();
				} catch (PrinterException e) {
					e.printStackTrace();
				}
			}
		}else if(ae.getSource()==copyButton){
			if(infoArea.getText().trim().equals("")){
				String string = "Please select a node from the visualization set tree.";
				AttentionDialog.createDialog(parent, string);
			}else{
				infoArea.setSelectionStart(0);
				infoArea.setSelectionEnd(infoArea.getDocument().getLength());
				infoArea.copy();
				infoArea.setCaretPosition(0);
			}
		}
	}

}