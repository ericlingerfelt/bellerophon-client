/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: EditMatplotlibAnimationDialog.java
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
import java.io.File;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.bellerophon.data.MainData;
import org.bellerophon.data.feature.VizManData;
import org.bellerophon.data.util.VizSet;
import org.bellerophon.gui.format.Borders;
import org.bellerophon.gui.format.Buttons;
import org.bellerophon.gui.format.Calendars;
import org.bellerophon.gui.format.Colors;
import org.bellerophon.gui.util.PlainFileChooserFactory;
import org.bellerophon.gui.util.WordWrapLabel;

/**
 * The Class EditAnimationDialog.
 *
 * @author Eric J. Lingerfelt
 */
public class EditUploadedAnimationDialog extends JDialog implements ActionListener{
	
	private JTextField animationIdField, modDateField, dirField, creatorField, bounceTimeField, numFramesField;
	private JTextArea descArea;
	private JButton saveButton, cancelButton, undoButton, browseButton, clearButton;
	private Vector<Vector<Object>> dataVector;
	private int rowIndex;
	private int animationIndex;
	private Vector<Object> v;
	private JPanel inputPanel, buttonPanel2;
	private JLabel animationIdLabel
					, descLabel
					, modDateLabel
					, creatorLabel
					, dirLabel
					, bounceTimeLabel
					, numFramesLabel;
	private JScrollPane descPane;
	
	/**
	 * The Enum Type.
	 *
	 * @author Eric J. Lingerfelt
	 */
	public enum Type {EDIT, ADD};
	private Type type;
	private VizSet vs;
	private WordWrapLabel indexLabel, vizSetLabel;
	
	/**
	 * Creates the edit animation dialog.
	 *
	 * @param frame the frame
	 * @param dataVector the data vector
	 * @param rowIndex the row index
	 * @param type the type
	 * @param d the d
	 * @return the vector
	 */
	public static Vector<Object> createEditUploadedAnimationDialog(Frame frame
																	, Vector<Vector<Object>> dataVector
																	, int rowIndex
																	, Type type
																	, VizManData d){
		String title = "";
		if(type==Type.EDIT){
			title = "Edit Selected Animation";
		}else if(type==Type.ADD){
			title = "Add New Animation";
		}
		EditUploadedAnimationDialog dialog = new EditUploadedAnimationDialog(frame, title, dataVector, rowIndex, d, type);
		dialog.setVisible(true);
		return dialog.v;
	}
	
	/**
	 * Instantiates a new edits the animation dialog.
	 *
	 * @param frame the frame
	 * @param title the title
	 * @param dataVector the data vector
	 * @param rowIndex the row index
	 * @param d the d
	 */
	private EditUploadedAnimationDialog(Frame frame, String title, Vector<Vector<Object>> dataVector, int rowIndex, VizManData d, Type type){
		
		super(frame, title, true);
		this.dataVector = dataVector;
		this.rowIndex = rowIndex;
		this.type = type;
		
		if(type==Type.ADD){
			setSize(830, 530);
		}else if(type==Type.EDIT){
			setSize(830, 580);
		}
		setLocationRelativeTo(frame);
		
		vs = d.getVizSet();
		
		saveButton = Buttons.getIconButton("Save Animation"
											, "icons/list-add.png"
											, Buttons.IconPosition.RIGHT
											, Colors.BLUE
											, this
											, new Dimension(200, 50));
		
		cancelButton = Buttons.getIconButton("Cancel"
											, "icons/process-stop.png"
											, Buttons.IconPosition.RIGHT
											, Colors.RED
											, this
											, new Dimension(200, 50));
		
		undoButton = Buttons.getIconButton("Undo Changes"
											, "icons/edit-undo.png"
											, Buttons.IconPosition.RIGHT
											, Colors.GREEN
											, this
											, new Dimension(200, 50));
		
		JPanel buttonPanel = new JPanel();
		double[] colButton = {TableLayoutConstants.PREFERRED
								, 10, TableLayoutConstants.PREFERRED
								, 10, TableLayoutConstants.PREFERRED};
		double[] rowButton = {TableLayoutConstants.PREFERRED};
		buttonPanel.setLayout(new TableLayout(colButton, rowButton));
		buttonPanel.add(saveButton,    		"0, 0, c, c");;
		buttonPanel.add(undoButton, 		"2, 0, c, c");
		buttonPanel.add(cancelButton,  		"4, 0, c, c");
		
		clearButton = new JButton("Clear Selected File");
		clearButton.addActionListener(this);
		
		browseButton = new JButton("Browse for First File in Sequence");
		browseButton.addActionListener(this);
		
		buttonPanel2 = new JPanel();
		double[] colButton2 = {TableLayoutConstants.PREFERRED
								, 10, TableLayoutConstants.PREFERRED};
		double[] rowButton2 = {TableLayoutConstants.PREFERRED};
		buttonPanel2.setLayout(new TableLayout(colButton2, rowButton2));
		buttonPanel2.add(browseButton,    	"0, 0, c, c");
		buttonPanel2.add(clearButton, 		"2, 0, c, c");
		
		indexLabel = new WordWrapLabel(true);
		indexLabel.setFont(Borders.getBorderFont());
		indexLabel.setText("Visualization Set Index = " + String.valueOf(vs.getIndex()));
		
		vizSetLabel = new WordWrapLabel(true);
		vizSetLabel.setFont(Borders.getBorderFont());
		vizSetLabel.setText("Visualization Set Unique ID = " + vs.toString());
		
		JPanel labelPanel = new JPanel();
		double[] colLabel = {TableLayoutConstants.PREFERRED
								, 50, TableLayoutConstants.PREFERRED};
		double[] rowLabel = {TableLayoutConstants.PREFERRED};
		labelPanel.setLayout(new TableLayout(colLabel, rowLabel));
		labelPanel.add(indexLabel,    "0, 0, c, c");
		labelPanel.add(vizSetLabel,   "2, 0, c, c");
		
		animationIdField = new JTextField();
		bounceTimeField = new JTextField();
		numFramesField = new JTextField();
		numFramesField.setEditable(false);
		dirField = new JTextField();
		dirField.setEditable(false);
		creatorField = new JTextField();
		creatorField.setEditable(false);
		modDateField = new JTextField();
		modDateField.setEditable(false);
		
		descArea = new JTextArea();
		descArea.setWrapStyleWord(true);
		descArea.setLineWrap(true);
		descPane = new JScrollPane(descArea);
		
		animationIdLabel = new JLabel("Unique ID (up to 6 chars)");
		bounceTimeLabel = new JLabel("Bounce Time (ms)");
		numFramesLabel = new JLabel("Number of Frames");
		descLabel = new JLabel("Description (up to 100 chars)");
		creatorLabel = new JLabel("Uploaded By");
		modDateLabel = new JLabel("Upload Date");
		dirLabel = new JLabel("Select First Frame of Animation Sequence Below (file must end in 00001.png)");
	
		inputPanel = new JPanel();
		inputPanel.setBorder(Borders.getBorder("Uploaded Animation Parameters"));

		double[] col = {10, TableLayoutConstants.FILL, 10};
		double[] row = {10, TableLayoutConstants.PREFERRED
							, 10, TableLayoutConstants.FILL
							, 10, TableLayoutConstants.PREFERRED, 10};
		setLayout(new TableLayout(col, row));
		add(labelPanel,     "1, 1, c, c");
		add(inputPanel,     "1, 3, f, f");
		add(buttonPanel,    "1, 5, c, c");
		
		setCurrentState();
	}
	
	/**
	 * Sets the current state.
	 */
	private void setCurrentState(){
		
		if(type==Type.ADD){
			
			double[] colInput = {5, TableLayoutConstants.PREFERRED
							, 10, TableLayoutConstants.FILL, 5};
			double[] rowInput = {5, TableLayoutConstants.PREFERRED
							, 10, TableLayoutConstants.PREFERRED
							, 10, TableLayoutConstants.PREFERRED
							, 15, TableLayoutConstants.PREFERRED
							, 10, TableLayoutConstants.FILL
							, 15, TableLayoutConstants.PREFERRED
							, 10, TableLayoutConstants.PREFERRED
							, 10, TableLayoutConstants.PREFERRED, 5};
			inputPanel.setLayout(new TableLayout(colInput, rowInput));
			inputPanel.add(animationIdLabel,     "1, 1, r, c");
			inputPanel.add(animationIdField,     "3, 1, f, c");
			inputPanel.add(bounceTimeLabel,      "1, 3, r, c");
			inputPanel.add(bounceTimeField,      "3, 3, f, c");
			inputPanel.add(numFramesLabel,       "1, 5, r, c");
			inputPanel.add(numFramesField,       "3, 5, f, c");
			inputPanel.add(descLabel,            "1, 7, 3, 7, l, c");
			inputPanel.add(descPane,    		 "1, 9, 3, 9, f, f");
			inputPanel.add(dirLabel,    		 "1, 11, 3, 11, f, f");
			inputPanel.add(dirField,    		 "1, 13, 3, 13, f, f");
			inputPanel.add(buttonPanel2,    	 "1, 15, 3, 15, c, c");
			
		}else if(type==Type.EDIT){
			
			creatorField.setEditable(false);
			modDateField.setEditable(false);
			
			double[] colInput = {5, TableLayoutConstants.PREFERRED
								, 10, TableLayoutConstants.FILL, 5};
			double[] rowInput = {5, TableLayoutConstants.PREFERRED
								, 10, TableLayoutConstants.PREFERRED
								, 10, TableLayoutConstants.PREFERRED
								, 10, TableLayoutConstants.PREFERRED
								, 10, TableLayoutConstants.PREFERRED
								, 15, TableLayoutConstants.PREFERRED
								, 10, TableLayoutConstants.FILL
								, 15, TableLayoutConstants.PREFERRED
								, 10, TableLayoutConstants.PREFERRED
								, 10, TableLayoutConstants.PREFERRED
								, 10, TableLayoutConstants.PREFERRED, 5};
			inputPanel.setLayout(new TableLayout(colInput, rowInput));
			
			inputPanel.add(animationIdLabel,     "1, 1, r, c");
			inputPanel.add(animationIdField,     "3, 1, f, c");
			inputPanel.add(bounceTimeLabel,      "1, 3, r, c");
			inputPanel.add(bounceTimeField,      "3, 3, f, c");
			inputPanel.add(numFramesLabel,       "1, 5, r, c");
			inputPanel.add(numFramesField,       "3, 5, f, c");
			inputPanel.add(creatorLabel,         "1, 7, r, c");
			inputPanel.add(creatorField,         "3, 7, f, c");
			inputPanel.add(modDateLabel,         "1, 9, r, c");
			inputPanel.add(modDateField,         "3, 9, f, c");
			inputPanel.add(descLabel,            "1, 11, 3, 11, l, c");
			inputPanel.add(descPane,    		 "1, 13, 3, 13, f, f");
			inputPanel.add(dirLabel,    		 "1, 17, 3, 17, f, f");
			inputPanel.add(dirField,    		 "1, 19, 3, 19, f, f");
			inputPanel.add(buttonPanel2,    	 "1, 21, 3, 21, c, f");
			
		}
		
		if(rowIndex!=-1){
			
			v = dataVector.get(rowIndex);

			animationIndex = (Integer)v.get(0);
			animationIdField.setText(String.valueOf(v.get(1)));
			bounceTimeField.setText(String.valueOf(v.get(2)));
			numFramesField.setText(String.valueOf(v.get(3)));
			creatorField.setText(String.valueOf(v.get(4)));
			modDateField.setText(Calendars.getFormattedOutputDateString((Calendar)v.get(5)));
			descArea.setText(String.valueOf(v.get(6)));
			dirField.setText(String.valueOf(v.get(7)));
			
		}else{

			animationIndex = -1;
			animationIdField.setText("");
			bounceTimeField.setText("");
			numFramesField.setText("");
			descArea.setText("");
			dirField.setText("");
			
		}
		
		validate();
		repaint();
	}
	
	/**
	 * Animation id exists.
	 *
	 * @param animationId the animation id
	 * @param rowIndex the row index
	 * @return true, if successful
	 */
	private boolean animationIdExists(String animationId, int rowIndex){
		Vector<Vector<Object>> vv = dataVector;
		Iterator<Vector<Object>> itr = vv.iterator();
		int rowCounter = 0;
		while(itr.hasNext()){
			Vector<Object> v = itr.next();
			if(rowIndex==rowCounter){
				rowCounter++;
				continue;
			}
			if(((String)v.get(1)).equals(animationId)){
				return true;
			}
			rowCounter++;
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent ae){
		if(ae.getSource()==undoButton){
			String string = "You are about to undo all recent changes to this uploaded animation's parameters.<br><br><b>Are you sure?</b>";
			int returnValue = CautionDialog.createCautionDialog(this, string, "Attention!");
			if(returnValue==CautionDialog.YES){
				setCurrentState();
			}
		}else if(ae.getSource()==saveButton){
			
			if(goodData(type)){
				
				String animationId = animationIdField.getText().trim();

				if(type==Type.ADD){
					
					v = new Vector<Object>();
					v.add(Integer.valueOf(animationIndex));
					v.add(animationId);
					v.add(Double.valueOf(bounceTimeField.getText().trim()));
					v.add(Integer.valueOf(numFramesField.getText().trim()));
					v.add(MainData.getUser());
					v.add(Calendar.getInstance());
					v.add(descArea.getText().trim());
					v.add(dirField.getText().trim());
					
				}else if(type==Type.EDIT){
					
					v.set(0, Integer.valueOf(animationIndex));
					v.set(1, animationId);
					v.set(2, Double.valueOf(bounceTimeField.getText().trim()));
					v.set(6, descArea.getText().trim());
					
					if(!dirField.getText().trim().equals("")){
						v.set(3, Integer.valueOf(numFramesField.getText().trim()));
						v.set(4, MainData.getUser());
						v.set(5, Calendar.getInstance());
						v.set(7, dirField.getText().trim());
					}
				
				}
				
				if(!animationId.equals("") && animationIdExists(animationId, rowIndex)){
					AttentionDialog.createDialog(this, "The <i>Unique ID</i> you entered already exists.");
				}else{
					setVisible(false);
				}
			}
		}else if(ae.getSource()==cancelButton){
			v = null;
			setVisible(false);
		}else if(ae.getSource()==clearButton){
			dirField.setText("");
			numFramesField.setText("");
		}else if(ae.getSource()==browseButton){
			File file = getImageFile();
			if(file!=null){
				if(goodFilenameValue(file.getName())){
					dirField.setText(file.getAbsolutePath());
					numFramesField.setText(String.valueOf(getNumFramesFromFilename(file.getAbsolutePath())));
				}
			}
		}
	}
	
	private int getNumFramesFromFilename(String filepath){
		int index = filepath.lastIndexOf(".");
		int counter = 1;
		File tempFile = new File(filepath);
		while(tempFile.exists()){
			counter++;
			tempFile = new File(filepath.substring(0, index-5) + String.format("%05d", counter) + ".png");
		}
		return counter-1;
	}
	
	private File getImageFile(){
		JFileChooser fileDialog = PlainFileChooserFactory.createPlainFileChooser();
		fileDialog.setAcceptAllFileFilterUsed(false);
		fileDialog.setFileFilter(new FileNameExtensionFilter("Portable Network Graphics (*.png)", "png"));
		fileDialog.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int returnVal = fileDialog.showOpenDialog(this); 
		MainData.setAbsolutePath(fileDialog.getCurrentDirectory());
		if(returnVal==JFileChooser.APPROVE_OPTION){
			File file = fileDialog.getSelectedFile();
			MainData.setAbsolutePath(fileDialog.getCurrentDirectory());
			return file;
		}
		MainData.setAbsolutePath(fileDialog.getCurrentDirectory());
		return null;
	}
	
	/**
	 * Good animation id.
	 *
	 * @return true, if successful
	 */
	private boolean goodAnimationId(){
		if(animationIdField.getText().trim().length()>6 && !animationIdField.getText().trim().equals("")){
			AttentionDialog.createDialog(this, "Please enter a value for <i>Unique ID</i> which is less than or equal 6 characters in length or leave it empty.");
			return false;
		}
		if(!animationIdField.getText().trim().matches("^[A-Za-z0-9_]+$") && !animationIdField.getText().trim().equals("")){
			AttentionDialog.createDialog(this, "Please enter a value for <i>Unique ID</i> which contains only alphanumeric characters or underscores or leave it empty.");
			return false;
		}
		return true;
	}
	
	private boolean goodBounceTime(){
		if(bounceTimeField.getText().trim().equals("")){
			AttentionDialog.createDialog(this, "Please enter a numeric value for <i>Bounce Time</i>.");
			return false;
		}
		try{
			Double.valueOf(bounceTimeField.getText());
		}catch(NumberFormatException nfe){
			AttentionDialog.createDialog(this, "Please enter a numeric value for <i>Bounce Time</i>.");
			return false;
		}
		return true;
	}
	
	private boolean goodFilenameValue(String filename){
		int index = filename.lastIndexOf(".");
		if(!filename.substring(index-5).equals("00001.png")){
			AttentionDialog.createDialog(this, "Please select a value for <i>First Frame of Animation Sequence</i> that ends in 00001.png.");
			return false;
		}
		return true;
	}
	
	private boolean goodFilenameValue(){
		String filePath = dirField.getText();
		File file = new File(filePath);
		String filename = file.getName();
		int index = filename.lastIndexOf(".");
		try{
			if(!filename.substring(index-5).equals("00001.png")){
				AttentionDialog.createDialog(this, "Please select a value for <i>First Frame of Animation Sequence</i> that ends in 00001.png.");
				return false;
			}
		}catch(StringIndexOutOfBoundsException sioobe){
			AttentionDialog.createDialog(this, "Please select a value for <i>First Frame of Animation Sequence</i> that ends in 00001.png.");
			return false;
		}
		return true;
	}
	
	private boolean goodDesc(){
		if(descArea.getText().trim().length()>100 && !descArea.getText().trim().equals("")){
			AttentionDialog.createDialog(this, "Please enter a value for <i>Description</i> which is less than or equal 100 characters in length or leave it empty.");
			return false;
		}
		return true;
	}
	
	private boolean goodData(Type type){
		if(type==Type.ADD){
			return goodAnimationId() && goodFilenameValue() && goodBounceTime() && goodDesc();
		}
		if(!dirField.getText().trim().equals("")){
			return goodAnimationId() && goodBounceTime() && goodDesc();
		}
		return goodAnimationId() && goodBounceTime() && goodDesc();
	}

}
