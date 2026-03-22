/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: StatPanel.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.stat;

import info.clearthought.layout.*;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;
import org.bellerophon.data.MainData;
import org.bellerophon.data.feature.*;
import org.bellerophon.data.util.*;
import org.bellerophon.enums.*;
import org.bellerophon.gui.dialog.*;
import org.bellerophon.gui.format.Borders;
import org.bellerophon.gui.format.Buttons;
import org.bellerophon.gui.format.Calendars;
import org.bellerophon.gui.format.Colors;
import org.bellerophon.gui.stat.worker.GetDataForRevisionWorker;
import org.bellerophon.gui.stat.worker.SVNStatWorker;
import org.bellerophon.gui.util.*;

import org.jdesktop.swingx.*;
import com.jtechlabs.ui.widget.directorychooser.*;

/**
 * The Class StatPanel.
 *
 * @author Eric J. Lingerfelt
 */
public class StatPanel extends JPanel implements ActionListener{

	private StatData d = new StatData();
	private Frame frame;
	private JPanel dataPanel, waitPanel, endPanel;
	private JTextField dirField;
	private JButton dirButton, statButton, dataButton, viewButton;
	private JComboBox revMinBox, revMaxBox, revBox, codeTypeBox;
	private JXDatePicker dateMinBox, dateMaxBox;
	private JRadioButton revMinButton, revMaxButton, dateMinButton, dateMaxButton;
	private JTextArea area, status;
	private SVNStatWorker worker;
	
	/**
	 * Instantiates a new stat panel.
	 *
	 * @param frame the frame
	 */
	public StatPanel(Frame frame){
		
		this.frame = frame;
		
		revBox = new JComboBox();
		revMinBox = new JComboBox();
		revMaxBox = new JComboBox();
		
		codeTypeBox = new JComboBox();
		for(CodeType codeType: CodeType.values()){
			if(codeType!=CodeType.UNKNOWN){
				codeTypeBox.addItem(codeType);
			}
		}
		
		WordWrapLabel codeTypeLabel = new WordWrapLabel();
		codeTypeLabel.setText("Select Chimera Code Type");
		
		revMinButton = new JRadioButton("Revision Min");
		revMinButton.addActionListener(this);
		
		revMaxButton = new JRadioButton("Revision Max");
		revMaxButton.addActionListener(this);
		
		dateMinButton = new JRadioButton("Date Min");
		dateMinButton.addActionListener(this);
		
		dateMaxButton = new JRadioButton("Date Max");
		dateMaxButton.addActionListener(this);
		
		dateMinBox = new JXDatePicker();
		dateMinBox.setFormats(new SimpleDateFormat("MM/dd/yy"));
		dateMinBox.addActionListener(this);
		
		dateMaxBox = new JXDatePicker();
		dateMaxBox.setFormats(new SimpleDateFormat("MM/dd/yy"));
		dateMaxBox.addActionListener(this);
		
		ButtonGroup minButtonGroup = new ButtonGroup();
		minButtonGroup.add(revMinButton);
		minButtonGroup.add(dateMinButton);
		
		ButtonGroup maxButtonGroup = new ButtonGroup();
		maxButtonGroup.add(revMaxButton);
		maxButtonGroup.add(dateMaxButton);
		
		area = new JTextArea();
		area.setEditable(false);
		area.setLineWrap(true);
		area.setWrapStyleWord(true);
		JScrollPane areaPane = new JScrollPane(area);
		
		status = new JTextArea();
		status.setEditable(false);
		status.setLineWrap(true);
		status.setWrapStyleWord(true);
		JScrollPane statusPane = new JScrollPane(status);
		JPanel statusPanel = new JPanel();
		double[] colStatus = {5, TableLayoutConstants.FILL, 5};
		double[] rowStatus = {5, TableLayoutConstants.FILL, 5};
		statusPanel.setLayout(new TableLayout(colStatus, rowStatus));
		statusPanel.add(statusPane, "1, 1, f, f");
		statusPanel.setBorder(Borders.getBorder("SVN Statistical Generation Status"));
		
		JLabel revLabel = new JLabel("Select Revision for Displayed Log Entry");
		WordWrapLabel waitLabel = new WordWrapLabel(true);
		waitLabel.setText("Please wait while SVN statistics are generated. " +
								"This process may take several minutes to complete. " +
								"You may use other Bellerophon features during this process.");
		JLabel endLabel = new JLabel("SVN statistics generation is complete.");
		
		dirField = new JTextField();
		dirField.setEditable(false);
		
		dirButton = new JButton("Browse");
		dirButton.addActionListener(this);
		
		statButton = Buttons.getIconButton("Generate SVN Statistics"
											, "icons/go-next.png"
											, Buttons.IconPosition.RIGHT
											, Colors.GREEN
											, this
											, new Dimension(250, 50));

		dataButton = Buttons.getIconButton("Generate More SVN Statistics"
											, "icons/go-previous.png"
											, Buttons.IconPosition.LEFT
											, Colors.GREEN
											, this
											, new Dimension(250, 50));
		
		viewButton = Buttons.getIconButton("View Generated SVN Statistics"
											, "icons/applications-internet.png"
											, Buttons.IconPosition.RIGHT
											, Colors.BLUE
											, this
											, new Dimension(250, 50));
		
		JPanel codeTypePanel = new JPanel();
		double[] colCodeType = {5, TableLayoutConstants.PREFERRED
									, 10, TableLayoutConstants.FILL, 5};
		double[] rowCodeType = {5, TableLayoutConstants.FILL, 5};
		codeTypePanel.setLayout(new TableLayout(colCodeType, rowCodeType));
		codeTypePanel.add(codeTypeLabel, "1, 1, r, c");
		codeTypePanel.add(codeTypeBox,   "3, 1, f, c");
		
		JPanel rangePanel = new JPanel();
		double[] colRange = {5, TableLayoutConstants.FILL
							, 10, TableLayoutConstants.FILL
							, 100, TableLayoutConstants.FILL
							, 10, TableLayoutConstants.FILL, 5};
		double[] rowRange = {5, TableLayoutConstants.PREFERRED
							, 10, TableLayoutConstants.PREFERRED
							, 10, TableLayoutConstants.PREFERRED, 5};
		rangePanel.setLayout(new TableLayout(colRange, rowRange));
		rangePanel.add(codeTypePanel, "1, 1, 7, 1, f, c");
		rangePanel.add(revMinButton,  "1, 3, l, c");
		rangePanel.add(revMinBox,     "3, 3, f, c");
		rangePanel.add(revMaxButton,  "5, 3, l, c");
		rangePanel.add(revMaxBox,     "7, 3, f, c");
		rangePanel.add(dateMinButton, "1, 5, l, c");
		rangePanel.add(dateMinBox,    "3, 5, f, c");
		rangePanel.add(dateMaxButton, "5, 5, l, c");
		rangePanel.add(dateMaxBox,    "7, 5, f, c");
		rangePanel.setBorder(Borders.getBorder("SVN Statistics On-Demand Parameters"));
		
		JPanel dirPanel = new JPanel();
		double[] colDir = {5, TableLayoutConstants.FILL
								, 10, TableLayoutConstants.PREFERRED, 5};
		double[] rowDir = {5, TableLayoutConstants.PREFERRED, 5};
		dirPanel.setLayout(new TableLayout(colDir, rowDir));
		dirPanel.add(dirField,   "1, 1, f, c");
		dirPanel.add(dirButton,  "3, 1, c, c");
		dirPanel.setBorder(Borders.getBorder("SVN Statistical Content Download Directory"));
		
		JPanel revPanel = new JPanel();
		double[] colRev = {5, TableLayoutConstants.PREFERRED
								, 10, TableLayoutConstants.FILL, 5};
		double[] rowRev = {5, TableLayoutConstants.PREFERRED
								, 10, TableLayoutConstants.FILL, 5};
		revPanel.setLayout(new TableLayout(colRev, rowRev));
		revPanel.add(revLabel,   "1, 1, r, c");
		revPanel.add(revBox,     "3, 1, f, c");
		revPanel.add(areaPane,   "1, 3, 3, 3, f, f");
		revPanel.setBorder(Borders.getBorder("SVN Log Entry Viewer"));
		
		dataPanel = new JPanel();
		double[] colData = {TableLayoutConstants.FILL};
		double[] rowData = {TableLayoutConstants.PREFERRED
							, 20, TableLayoutConstants.PREFERRED
							, 10, TableLayoutConstants.FILL
							, 10, TableLayoutConstants.PREFERRED};
		dataPanel.setLayout(new TableLayout(colData, rowData));
		
		dataPanel.add(rangePanel,   "0, 0, f, c");
		dataPanel.add(dirPanel,     "0, 2, f, c");
		dataPanel.add(revPanel,     "0, 4, f, f");
		dataPanel.add(statButton,   "0, 6, r, c");
		
		waitPanel = new JPanel();
		double[] colWait = {TableLayoutConstants.FILL};
		double[] rowWait = {TableLayoutConstants.PREFERRED
							, 20, TableLayoutConstants.FILL
							, 20, TableLayoutConstants.PREFERRED};
		waitPanel.setLayout(new TableLayout(colWait, rowWait));
		waitPanel.add(waitLabel, "0, 0, c, c");
		waitPanel.add(statusPanel, "0, 2, f, f");
		
		endPanel = new JPanel();
		double[] colEnd = {TableLayoutConstants.PREFERRED};
		double[] rowEnd = {TableLayoutConstants.PREFERRED
							, 20, TableLayoutConstants.PREFERRED
							, 20, TableLayoutConstants.PREFERRED};
		endPanel.setLayout(new TableLayout(colEnd, rowEnd));
		endPanel.add(endLabel, "0, 0, c, c");
		endPanel.add(viewButton, "0, 2, f, c");
		endPanel.add(dataButton, "0, 4, f, c");
		
	}

	/**
	 * Gets the data.
	 *
	 * @return the data
	 */
	public StatData getData(){return d;}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent ae){
		if(ae.getSource()==viewButton){
			BrowserLaunch.openURL(dirField.getText().trim() + "/index.html", frame);
		}else if(ae.getSource()==dirButton){
			File dir = PlainFileChooserFactory.createPlainFileChooser().getSelectedFile();
			if(dir!=null){
				MainData.setAbsolutePath(dir);
				dirField.setText(dir.getAbsolutePath() + "/stats_" + Calendars.getTimestampString(Calendar.getInstance()));
			}
		}else if(ae.getSource()==codeTypeBox){
			d.setCodeType((CodeType)codeTypeBox.getSelectedItem());
			setCurrentState();
		}else if(ae.getSource()==revBox){
			int revision = (Integer)revBox.getSelectedItem();
			if(d.getLogMap().get(d.getCodeType()).get(revision).getMessage().equals("")){
				GetDataForRevisionWorker worker = new GetDataForRevisionWorker(this, frame, d.getLogMap().get(d.getCodeType()).get(revision));
				worker.execute();
			}else{
				setRevString(d.getLogMap().get(d.getCodeType()).get(revision));
			}
		}else if(ae.getSource()==statButton){
			status.setText("");
			if(dirField.getText().trim().equals("")){
				ErrorDialog.createDialog(frame, "Please enter a value for SVN Statistical Content Download Directory.");
				return;
			}
		
			d.setRevMax(-1);
			d.setRevMin(-1);
			d.setDateMax(null);
			d.setDateMin(null);
			
			if(revMaxButton.isSelected()){
				d.setRevMax(Integer.parseInt(revMaxBox.getSelectedItem().toString()));
			}else{
				Calendar c = Calendar.getInstance();
				c.clear();
				if(dateMaxBox.getDate()!=null){
					c.setTime(dateMaxBox.getDate());
					c.set(Calendar.HOUR_OF_DAY, 23);
					c.set(Calendar.MINUTE, 59);
					c.set(Calendar.SECOND, 59);
					d.setDateMax(c);
				}
			}
			
			if(revMinButton.isSelected()){
				int revMin = -1;
				if(revMinBox.getSelectedItem().toString().equals("-")){
					revMin = -1;
				}else{
					revMin = Integer.parseInt(revMinBox.getSelectedItem().toString());
				}
				d.setRevMin(revMin);
			}else{
				Calendar c = Calendar.getInstance();
				c.clear();
				if(dateMinBox.getDate()!=null){
					c.setTime(dateMinBox.getDate());
					d.setDateMin(c);
				}
			}
			
			if(goodRange()){
				String path = dirField.getText().trim();
				String string = "You are about to generate SVN statistical content and write this content to the directory <br><br><b>" 
									+ path
									+ "</b><br><br>This process may take several minutes.<br><br><b>Are you sure?</b>";
				if(CautionDialog.createCautionDialog(frame, string, "Attention!")==CautionDialog.YES){
					gotoWaitPanel();
					worker = new SVNStatWorker(d, this, frame, path, status);
					worker.execute();
				}
			}
		}else if(ae.getSource()==dataButton){
			gotoDataPanel();
		}else if(ae.getSource()==revMinButton || ae.getSource()==dateMinButton){
			dateMinBox.setEnabled(!revMinButton.isSelected());
			revMinBox.setEnabled(revMinButton.isSelected());
		}else if(ae.getSource()==revMaxButton || ae.getSource()==dateMaxButton){
			dateMaxBox.setEnabled(!revMaxButton.isSelected());
			revMaxBox.setEnabled(revMaxButton.isSelected());
		}
	}
	
	/**
	 * Good range.
	 *
	 * @return true, if successful
	 */
	private boolean goodRange(){
		if(dateMaxButton.isSelected() && d.getDateMax()==null){
			String error = "Date maximum can not be left blank if the Date Max radio button has been selected.";
			ErrorDialog.createDialog(frame, error);
			return false;
		}else if(dateMinButton.isSelected() && dateMaxButton.isSelected()){
			if(d.getDateMin()!=null && d.getDateMax()!=null){
				if(d.getDateMax().before(d.getDateMin())){
					String error = "Please select a date maximum that comes after the date minimum.";
					ErrorDialog.createDialog(frame, error);
					return false;
				}
			}
		}else if(revMinButton.isSelected() && revMaxButton.isSelected()){
			if(d.getRevMin()!=-1){
				if(d.getRevMin()>=d.getRevMax()){
					String error = "Please select a revision minimum that is less than the " +
									"selected revision maximum or select <b>-</b> for the revision minimum.";
					ErrorDialog.createDialog(frame, error);
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Sets the current state.
	 */
	public void setCurrentState(){
		dirField.setText(MainData.getAbsolutePath().toString() + "/stats_" + Calendars.getTimestampString(Calendar.getInstance()));
		if(d.getCodeType()==null){
			d.setCodeType(CodeType.CHIMERA_NEW);
			codeTypeBox.setSelectedItem(d.getCodeType());
			codeTypeBox.addActionListener(this);
		}
		
		populateRevisions();
		if(d.getDateMin()!=null){
			dateMinBox.setEnabled(true);
			dateMinBox.setDate(d.getDateMin().getTime());
			dateMinButton.setSelected(true);
			revMinButton.setSelected(false);
			revMinBox.setEnabled(false);
		}else{
			dateMinBox.setEnabled(false);
			dateMinButton.setSelected(false);
			revMinButton.setSelected(true);
			revMinBox.setEnabled(true);
			if(d.getRevMin()==-1){
				revMinBox.setSelectedItem("-");
			}else{
				revMinBox.setSelectedItem(d.getRevMin());
			}
		}
		if(d.getDateMax()!=null){
			dateMaxBox.setEnabled(true);
			dateMaxBox.setDate(d.getDateMax().getTime());
			dateMaxButton.setSelected(true);
			revMaxButton.setSelected(false);
			revMaxBox.setEnabled(false);
		}else{
			dateMaxBox.setEnabled(false);
			dateMaxButton.setSelected(false);
			revMaxButton.setSelected(true);
			revMaxBox.setEnabled(true);
			if(d.getRevMax()==-1){
				revMaxBox.setSelectedIndex(0);
			}else{
				revMaxBox.setSelectedItem(d.getRevMax());
			}
		}
		gotoDataPanel();
	}
	
	/**
	 * Populate revisions.
	 */
	public void populateRevisions(){
		
		revBox.removeActionListener(this);
		revBox.removeAllItems();
		revMinBox.removeAllItems();
		revMaxBox.removeAllItems();
		revMinBox.addItem("-");
		
		Iterator<Integer> itr = d.getLogMap().get(d.getCodeType()).descendingKeySet().iterator();
		while(itr.hasNext()){
			Integer rev = itr.next();
			revBox.addItem(rev);
			revMinBox.addItem(rev);
			revMaxBox.addItem(rev);
		}
		
		revBox.addActionListener(this);
		revBox.setSelectedIndex(0);
		revMinBox.setSelectedIndex(0);
		revMaxBox.setSelectedIndex(0);

	}
	
	/**
	 * Goto data panel.
	 */
	public void gotoDataPanel(){
		removeAll();
		double[] col = {10, TableLayoutConstants.FILL, 10};
		double[] row = {10, TableLayoutConstants.FILL, 10};
		setLayout(new TableLayout(col, row));
		add(dataPanel, "1, 1, f, f");
		validate();
		repaint();
	}
	
	/**
	 * Goto end panel.
	 */
	public void gotoEndPanel(){
		removeAll();
		double[] col = {10, TableLayoutConstants.FILL, 10};
		double[] row = {10, TableLayoutConstants.FILL, 10};
		setLayout(new TableLayout(col, row));
		add(endPanel, "1, 1, c, c");
		validate();
		repaint();
	}
	
	/**
	 * Goto wait panel.
	 */
	public void gotoWaitPanel(){
		removeAll();
		double[] col = {10, TableLayoutConstants.FILL, 10};
		double[] row = {10, TableLayoutConstants.FILL, 10};
		setLayout(new TableLayout(col, row));
		add(waitPanel, "1, 1, f, f");
		validate();
		repaint();
	}
	
	/**
	 * Sets the rev string.
	 *
	 * @param rle the new rev string
	 */
	public void setRevString(RevisionLogEntry rle){
		String string = "";
		string += "Revision: " + rle.getRevision() + "\n";
		string += "Author: " + rle.getAuthor() + "\n";
		string += "Date: " + Calendars.getDateTimeString(rle.getDate()) + "\n";
		string += rle.getMessage() + "\n";
		area.setText(string);
		area.setCaretPosition(0);
	}
	
}