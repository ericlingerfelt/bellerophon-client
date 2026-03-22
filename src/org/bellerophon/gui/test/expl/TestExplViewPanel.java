/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: TestExplViewPanel.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.test.expl;

import info.clearthought.layout.*;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.JTextComponent;

import org.bellerophon.data.*;
import org.bellerophon.data.feature.*;
import org.bellerophon.data.util.*;
import org.bellerophon.enums.CodeType;
import org.bellerophon.exception.*;
import org.bellerophon.export.*;
import org.bellerophon.file.CustomFileFilter;
import org.bellerophon.file.FileType;
import org.bellerophon.gui.dialog.*;
import org.bellerophon.gui.format.Borders;
import org.bellerophon.gui.format.Buttons;
import org.bellerophon.gui.format.Colors;
import org.bellerophon.gui.test.expl.worker.*;
import org.bellerophon.gui.util.*;
import org.bellerophon.io.*;


/**
 * The Class TestExplViewPanel.
 *
 * @author Eric J. Lingerfelt
 */
public class TestExplViewPanel extends JPanel implements ActionListener, ChangeListener, HyperlinkListener{
	
	private JButton tracButton, selectButton;
	private Frame frame;
	private TestExplPanel parent;
	private TestExplViewTree tree;
	private TestExplData d;
	private JButton saveDirButton, addDirButton, saveButton
					, copyButton, printButton, removeButton;
	private ArrayList<JTextComponent> areaList;
	private ArrayList<CustomFile> fileList;
	private JLabel fileLabel;
	private JCheckBox wrapBox;
	private JTabbedPane filePane;
	private WordWrapLabel selectLabel;
	private JSplitPane jsp;
	private JPanel filePanel, fileButtonPanel;
	private TestExplInfoPanel infoPanel;
	
	/**
	 * Instantiates a new test expl view panel.
	 *
	 * @param frame the frame
	 * @param parent the parent
	 * @param d the d
	 */
	public TestExplViewPanel(Frame frame, TestExplPanel parent, TestExplData d){
	
		this.parent = parent;
		this.d = d;
		this.frame = frame;
		
		fileLabel = new JLabel();
		fileLabel.setFont(Borders.getBorderFont());
		
		selectLabel = new WordWrapLabel(true);
		selectLabel.setText("Please select one or more files from the <i>Regression Test Data</i> tree on the left.");
		
		tree = new TestExplViewTree(parent, this, frame);
		JScrollPane treePane = new JScrollPane(tree);
		JPanel treePanel = new JPanel();
		double[] colTree = {5, TableLayoutConstants.FILL, 5};
		double[] rowTree = {5, TableLayoutConstants.FILL, 5};
		treePanel.setLayout(new TableLayout(colTree, rowTree));
		treePanel.add(treePane, "1, 1, f, f");
		treePanel.setBorder(Borders.getBorder("Regression Test Data"));
		treePanel.setPreferredSize(new Dimension(300, 5000));
		
		JPanel buttonPanel = new JPanel();
		
		tracButton = Buttons.getIconButton("View Trac Changeset"
												, "icons/trac.png"
												, Buttons.IconPosition.RIGHT
												, Colors.TRAC_RED
												, this
												, new Dimension(200, 50));
	
		selectButton = Buttons.getIconButton("Select New Tests"
												, "icons/go-previous.png"
												, Buttons.IconPosition.LEFT
												, Colors.GREEN
												, this
												, new Dimension(200, 50));
		
		saveDirButton = Buttons.getIconButton("Download Directory"
												, "icons/go-down.png"
												, Buttons.IconPosition.RIGHT
												, Colors.GREEN
												, this
												, new Dimension(200, 50));
		
		addDirButton = new JButton("Add Local Directory");
		addDirButton.addActionListener(this);
		addDirButton.setPreferredSize(new Dimension(100, 50));
		
		double[] colButton = {TableLayoutConstants.PREFERRED
				, 10, TableLayoutConstants.FILL
				, 10, TableLayoutConstants.FILL
				, 10, TableLayoutConstants.PREFERRED
				, 10, TableLayoutConstants.PREFERRED};
		double[] rowButton = {TableLayoutConstants.PREFERRED};
		buttonPanel.setLayout(new TableLayout(colButton, rowButton));
		JButton tmp1 = new JButton();
		tmp1.setVisible(false);
		JButton tmp2 = new JButton();
		tmp2.setVisible(false);
		buttonPanel.add(selectButton, "0, 0, f, c");
		buttonPanel.add(tmp1, "2, 0, f, c");
		buttonPanel.add(tmp2, "4, 0, f, c");
		buttonPanel.add(saveDirButton, "6, 0, f, c");
		buttonPanel.add(tracButton, "8, 0, f, c");
		
		fileButtonPanel = new JPanel();
		
		removeButton = new JButton("Remove File from Viewer");
		removeButton.addActionListener(this);
		
		saveButton = new JButton("Save File");
		saveButton.addActionListener(this);
		
		printButton = new JButton("Print File");
		printButton.addActionListener(this);
		
		copyButton = new JButton("Copy File");
		copyButton.addActionListener(this);
		
		wrapBox = new JCheckBox("Wrap Lines?");
		wrapBox.addActionListener(this);
		
		double[] colFileButton = {TableLayoutConstants.PREFERRED
				, 10, TableLayoutConstants.PREFERRED
				, 10, TableLayoutConstants.PREFERRED
				, 10, TableLayoutConstants.PREFERRED
				, 10, TableLayoutConstants.PREFERRED};
		double[] rowFileButton = {TableLayoutConstants.PREFERRED};
		fileButtonPanel.setLayout(new TableLayout(colFileButton, rowFileButton));
		fileButtonPanel.add(removeButton,     "0, 0, c, c");
		fileButtonPanel.add(saveButton,       "2, 0, c, c");
		fileButtonPanel.add(printButton,      "4, 0, c, c");
		fileButtonPanel.add(copyButton,       "6, 0, c, c");
		fileButtonPanel.add(wrapBox,          "8, 0, c, c");
		
		filePane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
		filePane.addChangeListener(this);
		
		filePanel = new JPanel();
		double[] colFile = {5, TableLayoutConstants.FILL, 5};
		double[] rowFile = {5, TableLayoutConstants.PREFERRED
							, 5, TableLayoutConstants.FILL
							, 10, TableLayoutConstants.PREFERRED, 15};
		filePanel.setLayout(new TableLayout(colFile, rowFile));
		filePanel.add(fileLabel, "1, 1, c, c");
		filePanel.add(filePane,  "1, 3, f, f");
		filePanel.add(fileButtonPanel, "1, 5, c, c");
		filePanel.setBorder(Borders.getBorder("Regression Test File Viewer"));
		
		infoPanel = new TestExplInfoPanel(frame);
		
		JSplitPane jspLeft = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, treePanel, infoPanel);
		jspLeft.setBorder(null);
		jspLeft.setResizeWeight(0.5);
		jspLeft.setDividerLocation(275);
		
		jsp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, jspLeft, filePanel);
		jsp.setBorder(null);
		jsp.setDividerLocation(350);
		
		
		double[] col = {TableLayoutConstants.FILL};
		double[] row = {TableLayoutConstants.FILL, 10, TableLayoutConstants.PREFERRED};
		setLayout(new TableLayout(col, row));
		add(jsp, "0, 0, f, f");
		add(buttonPanel, "0, 2, f, f");
		
	}
	
	/**
	 * Gets the and save hdf binary contents.
	 *
	 * @param customFile the custom file
	 * @return the and save hdf binary contents
	 */
	public void getAndSaveHdfBinaryContents(CustomFile customFile){
		try{
			File file = saveHdfBinaryContents(customFile.getName());
			if(file!=null){
				if(customFile.getContents()==null){
					GetHDFFileContentsWorker worker = new GetHDFFileContentsWorker(frame, customFile, file);
					worker.execute();
				}else{
					IOUtilities.writeFile(file, customFile.getContents());
					String string = "The HDF5 file " + file + " has been saved.";
					MessageDialog.createMessageDialog(frame, string, "File Saved!");
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			ErrorDialog.createDialog(frame, e.getMessage());
		}
	}
	
	/**
	 * Save hdf binary contents.
	 *
	 * @param filename the filename
	 * @return the file
	 */
	private File saveHdfBinaryContents(String filename){
		JFileChooser fileDialog = PlainFileChooserFactory.createPlainFileChooser();
		fileDialog.setAcceptAllFileFilterUsed(false);
		fileDialog.addChoosableFileFilter(new CustomFileFilter(FileType.H5));
		fileDialog.setSelectedFile(new File(filename));
		int returnVal = fileDialog.showSaveDialog(frame); 
		MainData.setAbsolutePath(fileDialog.getCurrentDirectory());
		if(returnVal==JFileChooser.APPROVE_OPTION){
			File file = fileDialog.getSelectedFile();
			String filepath = file.getAbsolutePath();
			if(new File(filepath).exists()){
				String msg = "The file " + file.getName() + " exists. Do you want to replace it?";
				int value = CautionDialog.createCautionDialog(frame, msg, "Attention!");
				if(value==CautionDialog.NO){
					saveHdfBinaryContents(file.getName());
				}else{
					return file;
				}
			}else{
				return file;
			}
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.event.HyperlinkListener#hyperlinkUpdate(javax.swing.event.HyperlinkEvent)
	 */
	public void hyperlinkUpdate(HyperlinkEvent e) {
		if(e.getEventType()==HyperlinkEvent.EventType.ACTIVATED){
			String link = e.getDescription();
			link = link.replaceAll("../../", "");
			String[] array = link.split(":");
			int testIndex = Integer.parseInt(fileList.get(filePane.getSelectedIndex()).getPath().split("/")[5]);
			int rev = d.getSelectedTestMap().get(testIndex).getRevision();
			CodeType codeType = d.getSelectedTestMap().get(testIndex).getCodeType();
			switch(codeType){
				case CHIMERA_NEW:
					link = "http://eagle.phys.utk.edu/chimera/trac/browser/trunk/Chimera/" 
						+ array[0] 
						+ "?rev=" 
						+ rev
						+ "#L" 
						+ array[1];
					break;
				case CHIMERA_2D:
					link = "http://eagle.phys.utk.edu/chimera/trac/browser/trunk/RadHyd/" 
						+ array[0] 
						+ "?rev=" 
						+ rev
						+ "#L" 
						+ array[1];
					break;
				case CHIMERA_3D:
					link = "http://eagle.phys.utk.edu/chimera/trac/browser/trunk/RadHyd3D/" 
						+ array[0] 
						+ "?rev=" 
						+ rev
						+ "#L" 
						+ array[1];
					break;
				case CHIMERA_YY:
					link = "http://eagle.phys.utk.edu/chimera/trac/browser/branches/yy-grid/" 
						+ array[0] 
						+ "?rev=" 
						+ rev
						+ "#L" 
						+ array[1];
					break;
				case CHIMERA_POLARIS:
					link = "http://eagle.phys.utk.edu/chimera/trac/browser/branches/Polaris/" 
						+ array[0] 
						+ "?rev=" 
						+ rev
						+ "#L" 
						+ array[1];
					break;
			}
			
			try{
				if(Desktop.isDesktopSupported()){
					Desktop.getDesktop().browse(new URI(link));
				}else{
					BrowserLaunch.openURL(link, frame);
				}
			}catch(Exception ex){
				ex.printStackTrace();
				ErrorDialog.createDialog(frame, "Bellerophon is unable to detect a browser to open this URL.");
			}
		}
	}
	
	/**
	 * Removes the file panel.
	 */
	private void removeFilePanel(){
		filePanel.removeAll();
		double[] colFile = {5, TableLayoutConstants.FILL, 5};
		double[] rowFile = {5, TableLayoutConstants.FILL, 15};
		filePanel.setLayout(new TableLayout(colFile, rowFile));
		filePanel.add(selectLabel, "1, 1, c, c");
		validate();
		repaint();
	}
	
	/**
	 * Adds the file panel.
	 */
	private void addFilePanel(){
		filePanel.removeAll();
		double[] colFile = {5, TableLayoutConstants.FILL, 5};
		double[] rowFile = {5, TableLayoutConstants.PREFERRED
							, 5, TableLayoutConstants.FILL
							, 10, TableLayoutConstants.PREFERRED, 15};
		filePanel.setLayout(new TableLayout(colFile, rowFile));
		filePanel.add(fileLabel, "1, 1, c, c");
		filePanel.add(filePane,  "1, 3, f, f");
		filePanel.add(fileButtonPanel, "1, 5, c, c");
		validate();
		repaint();
	}
	
	/**
	 * Sets the current state.
	 */
	public void setCurrentState(){
		fileLabel.setText("");
		removeFilePanel();
		filePane.removeAll();
		wrapBox.setSelected(true);
		areaList = new ArrayList<JTextComponent>();
		fileList = new ArrayList<CustomFile>();
		tree.setCurrentState(d.getSelectedTestMap());
		infoPanel.setCurrentState(null);
	}
	
	public void setSelectedRegressionTest(RegressionTest rt){
		infoPanel.setCurrentState(rt);
	}
	
	/**
	 * Sets the selected file.
	 *
	 * @param file the new selected file
	 */
	public void setSelectedFile(CustomFile file){
		if(fileList.contains(file)){
			filePane.setSelectedIndex(fileList.indexOf(file));
		}else{
			try{
				JTextComponent area;
				JScrollPane areaPane;
				if(file.getName().equals("ExecErrStacktrace")){
					area = new FileEditorPane(this);
					areaPane = new JScrollPane(area);
					((FileEditorPane)area).setText(new String(file.getContents()));
				}else{
					area = new FileTextArea();
					((FileTextArea)area).setLineWrap(wrapBox.isSelected());
					areaPane = new LineNumberScrollPane(area);
					if(file.isHdf()){
						area.setText(new String(file.getHdfDumpContents()));
					}else{
						area.setText(new String(file.getContents()));
					}
				}
				areaList.add(area);
				fileList.add(file);
				filePane.addTab(file.getName(), areaPane);
				filePane.setSelectedIndex(filePane.getTabCount()-1);
				if(filePane.getTabCount()==1){
					addFilePanel();
				}
			}catch(IOException ioe){
				CaughtExceptionHandler.handleException(ioe, frame);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
	 */
	public void stateChanged(ChangeEvent ce){
		if(ce.getSource()==filePane){
			if(filePane.getSelectedIndex()!=-1){
				fileLabel.setText(fileList.get(filePane.getSelectedIndex()).getPath().replaceAll(MainData.TEST_URL + "/", ""));
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent ae){
		if(ae.getSource()==removeButton){
			if(filePane.getSelectedIndex()!=-1){
				int selectedIndex = filePane.getSelectedIndex();
				areaList.remove(selectedIndex);
				fileList.remove(selectedIndex);
				filePane.remove(selectedIndex);
				if(filePane.getTabCount()>0){
					filePane.setSelectedIndex(filePane.getTabCount()-1);
				}else{
					fileLabel.setText("");
					removeFilePanel();
				};
			}
		}else if(ae.getSource()==wrapBox){
			for(JTextComponent comp: areaList){
				if(comp instanceof FileTextArea){
					FileTextArea area = (FileTextArea)comp;
					area.setLineWrap(wrapBox.isSelected());
				}
			}
		}else if(ae.getSource()==tracButton){
			if(d.getSelectedTestMap().size()==1){
				try{
					String string = MainData.TRAC_CHANGESET_URL
									+ "/" 
									+ d.getSelectedTestMap().values().iterator().next().getRevision();
					if(Desktop.isDesktopSupported()){
						Desktop.getDesktop().browse(new URI(string));
					}else{
						BrowserLaunch.openURL(string, frame);
					}
				}catch(Exception e){
					e.printStackTrace();
					ErrorDialog.createDialog(frame, "Bellerophon is unable to detect a browser to open this URL.");
				}
			}else{
				RegressionTest rt = SelectTestDialog.createSelectTestDialog(frame, d.getSelectedTestMap());
				if(rt!=null){
					String string = MainData.TRAC_CHANGESET_URL 
									+ "/" 
									+ d.getSelectedTestMap().values().iterator().next().getRevision();
					try{
						if(Desktop.isDesktopSupported()){
							Desktop.getDesktop().browse(new URI(string));
						}else{
							BrowserLaunch.openURL(string, frame);
						}
					}catch(Exception e){
						e.printStackTrace();
						ErrorDialog.createDialog(frame, "Bellerophon is unable to detect a browser to open this URL.");
					}
				}
			}
		}else if(ae.getSource()==selectButton){
			parent.setCurrentState(TestExplPanel.TestExplMode.SELECT);
		}else{
			try{
				if(ae.getSource()==saveButton){
					if(filePane.getSelectedIndex()!=-1){
						CustomFile selectedFile = fileList.get(filePane.getSelectedIndex());
						if(selectedFile.isHdf()){
							TextSaver.saveText(frame, new String(selectedFile.getHdfDumpContents()), selectedFile.getName());
						}else{
							TextSaver.saveText(frame, new String(selectedFile.getContents()), selectedFile.getName());
						}
					}
				}else if(ae.getSource()==printButton){
					if(filePane.getSelectedIndex()!=-1){
						areaList.get(filePane.getSelectedIndex()).print();
					}
				}else if(ae.getSource()==copyButton){
					if(filePane.getSelectedIndex()!=-1){
						JTextComponent comp = areaList.get(filePane.getSelectedIndex());
						if(comp instanceof FileTextArea){
							FileTextArea area = (FileTextArea)comp;
							area.copyAllText();
						}else if(comp instanceof FileEditorPane){
							FileEditorPane area = (FileEditorPane)comp;
							area.copyAllText();
						}
					}
				}else if(ae.getSource()==saveDirButton){
					if(d.getSelectedTestMap().size()==1){
						saveDirAsTarfile(d.getSelectedTestMap().values().iterator().next());
					}else{
						RegressionTest rt = SelectTestDialog.createSelectTestDialog(frame, d.getSelectedTestMap());
						if(rt!=null){
							saveDirAsTarfile(rt);
						}
					}
				}
			}catch(Exception e){
				e.printStackTrace();
				ErrorDialog.createDialog(frame, e.getMessage());
			}
		}
	}

	/**
	 * Save dir as tarfile.
	 *
	 * @param rt the rt
	 * @throws Exception the exception
	 */
	private void saveDirAsTarfile(RegressionTest rt) throws Exception{
		File file = getTarfileSaveDir("regression_test_" + rt.getIndex() + ".tgz");
		if(file!=null){
			if(rt.getTarfile()==null){
				CustomFile tarfile = new CustomFile();
				tarfile.setName(String.valueOf(MainData.getID()) 
								+ "_"
								+ String.valueOf(rt.getIndex())
								+ ".tgz");
				String pathSuffix = "/tmp/" + tarfile.getName();
				tarfile.setPath(MainData.TEST_URL + "/" + pathSuffix);
				rt.setTarfile(tarfile);
				CreateAndGetTarfileWorker worker = new CreateAndGetTarfileWorker(frame, rt, file);
				worker.execute();
			}else{
				IOUtilities.writeFile(file, rt.getTarfile().getContents());
			}
		}
	}
	
	/**
	 * Gets the tarfile save dir.
	 *
	 * @param filename the filename
	 * @return the tarfile save dir
	 */
	private File getTarfileSaveDir(String filename){
		JFileChooser fileDialog = PlainFileChooserFactory.createPlainFileChooser();
		fileDialog.setAcceptAllFileFilterUsed(false);
		fileDialog.addChoosableFileFilter(new CustomFileFilter(FileType.TGZ));
		fileDialog.setSelectedFile(new File(filename));
		int returnVal = fileDialog.showSaveDialog(frame); 
		MainData.setAbsolutePath(fileDialog.getCurrentDirectory());
		if(returnVal==JFileChooser.APPROVE_OPTION){
			File file = fileDialog.getSelectedFile();
			String filepath = file.getAbsolutePath();
			if(new File(filepath).exists()){
				String msg = "The file " + file.getName() + " exists. Do you want to replace it?";
				int value = CautionDialog.createCautionDialog(frame, msg, "Attention!");
				if(value==CautionDialog.NO){
					getTarfileSaveDir(file.getName());
				}else{
					return file;
				}
			}else{
				return file;
			}
		}
		return null;
	}
	
	/**
	 * Gets the hdf dump contents.
	 *
	 * @param file the file
	 * @return the hdf dump contents
	 */
	public void getHdfDumpContents(CustomFile file){
		try{
			if(file.getHdfDumpContents()==null){
				CreateAndViewHDFDumpWorker worker = new CreateAndViewHDFDumpWorker(frame, this, file);
				worker.execute();
			}else{
				setSelectedFile(file);
			}
		}catch(IOException e){
			CaughtExceptionHandler.handleException(e, frame);
		}
	}
	
}