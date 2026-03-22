/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: VizExplViewPanel.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.viz.expl;

import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstants;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.*;

import org.bellerophon.data.Data;
import org.bellerophon.data.MainData;
import org.bellerophon.data.feature.VizExplData;
import org.bellerophon.data.util.Animation;
import org.bellerophon.data.util.CustomFile;
import org.bellerophon.data.util.UploadedAnimation;
import org.bellerophon.data.util.MatplotlibAnimation;
import org.bellerophon.data.util.VizSet;
import org.bellerophon.enums.VizCompType;
import org.bellerophon.enums.VizSetType;
import org.bellerophon.file.CustomFileFilter;
import org.bellerophon.file.FileType;
import org.bellerophon.gui.dialog.UploadedAnimationMenuDialog;
import org.bellerophon.gui.dialog.MatplotlibAnimationMenuDialog;
import org.bellerophon.gui.dialog.AttentionDialog;
import org.bellerophon.gui.dialog.CautionDialog;
import org.bellerophon.gui.format.Borders;
import org.bellerophon.gui.format.Buttons;
import org.bellerophon.gui.format.Colors;
import org.bellerophon.gui.format.Icons;
import org.bellerophon.gui.viz.VizSetUpdateListener;
import org.bellerophon.gui.viz.expl.comp.VizExplBrowsePanel;
import org.bellerophon.gui.viz.expl.comp.VizExplCompPanel;
import org.bellerophon.gui.viz.expl.comp.VizExplCustomPanel;
import org.bellerophon.gui.viz.expl.comp.VizExplDisplayPanel;
import org.bellerophon.gui.viz.expl.comp.VizExplMoviePanel;
import org.bellerophon.gui.viz.expl.worker.CreateAndGetMatplotlibAnimationPythonfileWorker;
import org.bellerophon.gui.viz.expl.worker.CreateAndGetMatplotlibAnimationTarfileWorker;

import org.bellerophon.gui.util.PlainFileChooserFactory;
import org.bellerophon.gui.util.WordWrapLabel;
import org.bellerophon.gui.viz.expl.worker.*;

/**
 * The Class VizExplViewPanel.
 *
 * @author Eric J. Lingerfelt
 */
public class VizExplViewPanel extends JPanel implements ActionListener
														, ChangeListener{
	
	private JButton dataButton, menuButton
					, selectButton, fullButton
					, removeButton, saveButton
					, emailButton;
	private VizExplPanel parent;
	private VizExplViewTree tree;
	private VizExplInfoPanel infoPanel;
	private Frame frame;
	private VizExplData d;
	private JTabbedPane compPane;
	private JPanel treePanel, compPanel, buttonPanel, introPanel; 
	private ArrayList<VizExplCompPanel> compList;
	private JLabel vizSetLabel;
	private JSplitPane jsp;
	private WordWrapLabel selectLabel;
	private boolean fullScreenMode;
	private int windowState = Frame.NORMAL;
	private ArrayList<VizSetUpdateListener> vsulList;
	
	/**
	 * Instantiates a new viz expl view panel.
	 *
	 * @param frame the frame
	 * @param parent the parent
	 * @param d the d
	 */
	public VizExplViewPanel(Frame frame, VizExplPanel parent, VizExplData d){
		
		this.parent = parent;
		this.d = d;
		this.frame = frame;
		
		compList = new ArrayList<VizExplCompPanel>();
		vsulList = new ArrayList<VizSetUpdateListener>();
		
		selectLabel = new WordWrapLabel(true);
		selectLabel.setText("Please select one or more animations from the <i>Visualization Sets</i> tree on the left.");
		
		vizSetLabel = new JLabel();
		vizSetLabel.setFont(Borders.getBorderFont());
		
		menuButton = new JButton("Open Menu for Selected Animation");
		menuButton.addActionListener(this);
		
		tree = new VizExplViewTree(this);
		JScrollPane treePane = new JScrollPane(tree);
		treePanel = new JPanel();
		double[] colTree = {5, TableLayoutConstants.FILL, 5};
		double[] rowTree = {5, TableLayoutConstants.FILL
							, 5, TableLayoutConstants.PREFERRED, 5};
		treePanel.setLayout(new TableLayout(colTree, rowTree));
		treePanel.add(treePane,        	"1, 1, f, f");
		treePanel.add(menuButton, 		"1, 3, c, f");
		treePanel.setBorder(Borders.getBorder("Visualization Sets"));
		treePanel.setPreferredSize(new Dimension(300, 5000));
		
		buttonPanel = new JPanel();
		
		selectButton = Buttons.getIconButton("Select New Viz Sets"
												, "icons/go-previous.png"
												, Buttons.IconPosition.LEFT
												, Colors.GREEN
												, this
												, new Dimension(200, 50));
		
		dataButton = Buttons.getIconButton("Save Current Data"
												, "icons/media-floppy.png"
												, Buttons.IconPosition.RIGHT
												, Colors.BLUE
												, this
												, new Dimension(200, 50));
		
		fullButton = Buttons.getIconButton("Enter Full Screen Mode"
												, "icons/view-fullscreen.png"
												, Buttons.IconPosition.RIGHT
												, Colors.BLUE
												, this
												, new Dimension(200, 50));
		
		removeButton = Buttons.getIconButton("Remove Selected Tab"
												, "icons/list-remove.png"
												, Buttons.IconPosition.RIGHT
												, Colors.BLUE
												, this
												, new Dimension(200, 50));
		
		saveButton = Buttons.getIconButton("Save Current Image"
												, "icons/media-floppy.png"
												, Buttons.IconPosition.RIGHT
												, Colors.BLUE
												, this
												, new Dimension(200, 50));
		
		emailButton = Buttons.getIconButton("Email Image Link"
												, "icons/mail-message-new.png"
												, Buttons.IconPosition.RIGHT
												, Colors.BLUE
												, this
												, new Dimension(200, 50));
									
		double[] colButton = {TableLayoutConstants.FILL
				, 10, TableLayoutConstants.FILL
				, 10, TableLayoutConstants.FILL
				, 10, TableLayoutConstants.FILL
				, 10, TableLayoutConstants.FILL
				, 10, TableLayoutConstants.FILL};
		double[] rowButton = {TableLayoutConstants.PREFERRED};
		buttonPanel.setLayout(new TableLayout(colButton, rowButton));
		JButton tmp = new JButton();
		tmp.setVisible(false);
		buttonPanel.add(selectButton,  "0, 0, f, c");
		buttonPanel.add(removeButton,  "2, 0, f, c");
		buttonPanel.add(dataButton,    "4, 0, f, c");
		buttonPanel.add(saveButton,    "6, 0, f, c");
		buttonPanel.add(emailButton,   "8, 0, f, c");
		buttonPanel.add(fullButton,	   "10, 0, f, c");
		
		introPanel = new JPanel();
		introPanel.setBorder(Borders.getBorder("Animation Viewer"));
		double[] colIntro = {5, TableLayoutConstants.FILL, 5};
		double[] rowIntro = {5, TableLayoutConstants.FILL, 15};
		introPanel.setLayout(new TableLayout(colIntro, rowIntro));
		introPanel.add(selectLabel, "1, 1, c, c");
		
		compPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
		compPane.addChangeListener(this);
		
		compPanel = new JPanel();
		compPanel.setBorder(Borders.getBorder("Animation Viewer"));
		double[] colAnimation = {5, TableLayoutConstants.FILL, 5};
		double[] rowAnimation = {5, TableLayoutConstants.PREFERRED
								, 5, TableLayoutConstants.FILL, 5};
		compPanel.setLayout(new TableLayout(colAnimation, rowAnimation));
		compPanel.add(vizSetLabel,       "1, 1, c, c");
		compPanel.add(compPane,       	 "1, 3, f, f");
		
		infoPanel = new VizExplInfoPanel(frame);
		
		JSplitPane jspLeft = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, treePanel, infoPanel);
		jspLeft.setBorder(null);
		jspLeft.setResizeWeight(0.5);
		jspLeft.setDividerLocation(275);
		
		jsp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, jspLeft, compPanel);
		jsp.setBorder(null);
		jsp.setDividerLocation(350);
		
		double[] col = {TableLayoutConstants.FILL};
		double[] row = {TableLayoutConstants.FILL
						, 10, TableLayoutConstants.PREFERRED};
		setLayout(new TableLayout(col, row));
		add(jsp, "0, 0, f, f");
		add(buttonPanel, "0, 2, f, f");
		
		vsulList.add(infoPanel);
		vsulList.add(tree);
	}
	
	/**
	 * Sets the current state.
	 */
	public void setCurrentState(){
		vizSetLabel.setText("");
		compList.clear();
		vsulList.clear();
		removeCompPanel();
		compPane.removeAll();
		infoPanel.setCurrentState(null, null, -1);
		tree.setCurrentState(d.getSelectedVizSetMap());
	}
	
	public void updateComponents(){
		for(VizSetUpdateListener vsul: vsulList){
			vsul.updateState();
		}
	}
	
	/**
	 * Refresh tree.
	 */
	public void refreshTree(){
		tree.repaint();
	}
	
	/**
	 * Removes the comp panel.
	 */
	private void removeCompPanel(){
		if(fullScreenMode){
			exitFullScreenMode();
		}
		jsp.setRightComponent(introPanel);
	}
	
	/**
	 * Adds the comp panel.
	 */
	private void addCompPanel(){
		jsp.setRightComponent(compPanel);
	}
	
	/**
	 * Sets the selected viz set.
	 *
	 * @param vs the new selected viz set
	 */
	public void setSelectedVizSet(VizSet vs){
		infoPanel.setCurrentState(vs, null);
	}
	
	/**
	 * Sets the selected frame.
	 *
	 * @param a the a
	 * @param frame the frame
	 */
	public void setSelectedFrame(Animation a, int frame){
		if(compList.get(compPane.getSelectedIndex()).getAnimation().equals(a)){
			VizSet vs = d.getSelectedVizSetMap().get(a.getParentIndex());
			a.setCurrentFrame(frame);
			infoPanel.setCurrentState(vs, a, frame);
		}
	}
	
	/**
	 * Sets the selected animation.
	 *
	 * @param a the new selected animation
	 */
	public void setSelectedAnimation(Animation a){
		VizSet vs = d.getSelectedVizSetMap().get(a.getParentIndex());
		infoPanel.setCurrentState(vs, a);
	}
	
	/**
	 * Enter full screen mode.
	 */
	private void enterFullScreenMode(){
		fullButton.setIcon(Icons.createImageIcon("/resources/images/icons/view-restore.png"));
		fullButton.setText("Exit Full Screen Mode");
		fullScreenMode = true;
		
		windowState = frame.getExtendedState();
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		removeAll();
		double[] col = {TableLayoutConstants.FILL};
		double[] row = {TableLayoutConstants.FILL
						, 10, TableLayoutConstants.PREFERRED};
		setLayout(new TableLayout(col, row));
		if(compPane.getTabCount()>0){
			add(compPanel, "0, 0, f, f");
		}else{
			add(introPanel, "0, 0, f, f");
		}
		add(buttonPanel, "0, 2, f, f");
		validate();
		repaint();
	}
	
	/**
	 * Exit full screen mode.
	 */
	private void exitFullScreenMode(){
		fullButton.setIcon(Icons.createImageIcon("/resources/images/icons/view-fullscreen.png"));
		fullButton.setText("Enter Full Screen Mode");
		fullScreenMode = false;
		
		frame.setExtendedState(windowState);

		removeAll();
		double[] col = {TableLayoutConstants.FILL};
		double[] row = {TableLayoutConstants.FILL
						, 10, TableLayoutConstants.PREFERRED};
		setLayout(new TableLayout(col, row));
		if(compPane.getTabCount()>0){
			jsp.setRightComponent(compPanel);
		}else{
			jsp.setRightComponent(introPanel);
		}
		add(jsp, "0, 0, f, f");
		add(buttonPanel, "0, 2, f, f");
		validate();
		repaint();
	}
	
	/**
	 * Adds the viz panel.
	 *
	 * @param type the type
	 * @param vs the vs
	 * @param a the a
	 */
	private void addVizPanel(VizCompType type, VizSet vs, Animation a){
		VizExplCompPanel panel = a.getPanelMap().get(type);
		if((vs.getVizJobMap()==null || vs.getVizJobMap().size()==0) && vs.getVizSetType()==VizSetType.CHIMERA2D){
			AttentionDialog.createDialog(frame, "The animation you selected does not contain any frames yet.");
		}else{
			if(panel==null){
				switch(type){
					case BROWSE:
						panel = new VizExplBrowsePanel(frame, this, vs, a);
						break;
					case CUSTOM:
						panel = new VizExplCustomPanel(frame, this, vs, (MatplotlibAnimation) a);
						break;
					case MOVIE:
						panel = new VizExplMoviePanel(frame, this, vs, a);
						break;
					case DISPLAY:
						panel = new VizExplDisplayPanel(frame, this, vs, a);
						break;
				}
				a.getPanelMap().put(type, panel);
			}
			if(!compList.contains(panel)){
				vsulList.add(panel);
				compList.add(panel);
				compPane.addTab(a.toString(), panel);
				compPane.setSelectedIndex(compPane.getTabCount()-1);
				if(compPane.getTabCount()==1){
					addCompPanel();
				}
			}
			compPane.setSelectedComponent(panel);
			if(panel instanceof VizExplBrowsePanel){
				((VizExplBrowsePanel)panel).setCurrentFrame();
			}else if(panel instanceof VizExplCustomPanel){
				((VizExplCustomPanel)panel).setCurrentFrame(false);
			}else if(panel instanceof VizExplMoviePanel){
				((VizExplMoviePanel)panel).loadFrames();
			}else if(panel instanceof VizExplDisplayPanel){
				((VizExplDisplayPanel)panel).setCurrentFrame();
			}
		}
	}
	
	/**
	 * Open animation menu.
	 */
	public void openAnimationMenu(){
		if(tree.getSelectedObject()!=null){
			if(tree.getSelectedObject() instanceof MatplotlibAnimation){
				MatplotlibAnimation va = (MatplotlibAnimation) tree.getSelectedObject();
				VizSet vs = d.getSelectedVizSetMap().get(va.getParentIndex());
				MatplotlibAnimationMenuDialog.Selection selection = MatplotlibAnimationMenuDialog.createMatplotlibAnimationMenuDialog(frame, vs, va);
				switch(selection){
					case BROWSE:
						addVizPanel(VizCompType.BROWSE, vs, va);
						break;
					case CUSTOM:
						addVizPanel(VizCompType.CUSTOM, vs, va);
						break;
					case MOVIE:
						addVizPanel(VizCompType.MOVIE, vs, va);
						break;
					case CREATE:
						createMovie(vs, va);
						break;
					case HOT:
						setHotMatplotlibAnimation(vs, va);
						break;
					case SNAPSHOT:
						takeSnapshot(vs, va);
						break;
					case MATPLOTLIB:
						saveAnimationAsPython(va);
						break;
					case RECREATE:
						recreateMatplotlibAnimation(va);
						break;
				}
			}else if(tree.getSelectedObject() instanceof UploadedAnimation){
				UploadedAnimation ua = (UploadedAnimation) tree.getSelectedObject();
				VizSet vs = d.getSelectedVizSetMap().get(ua.getParentIndex());
				UploadedAnimationMenuDialog.Selection selection = UploadedAnimationMenuDialog.createUploadedAnimationMenuDialog(frame, vs, ua);
				switch(selection){
					case BROWSE:
						addVizPanel(VizCompType.BROWSE, vs, ua);
						break;
					case MOVIE:
						addVizPanel(VizCompType.MOVIE, vs, ua);
						break;
					case CREATE:
						createMovie(vs, ua);
						break;
					case SNAPSHOT:
						takeSnapshot(vs, ua);
						break;
				}
			}else{
				AttentionDialog.createDialog(frame, "Please select an animation from the Visualization Set tree at the left.");
			}
		}else{
			AttentionDialog.createDialog(frame, "Please select an animation from the Visualization Set tree at the left.");
		}
	}
	
	private void recreateMatplotlibAnimation(MatplotlibAnimation va){
		RecreateMatplotlibAnimationWorker worker = new RecreateMatplotlibAnimationWorker(frame, va);
		worker.execute();
	}
	
	private void saveAnimationAsPython(MatplotlibAnimation va){
		File file = getPythonSaveDir(va.toStringPythonFilename());
		if(file!=null){
			CustomFile pythonfile = new CustomFile();
			pythonfile.setName(String.valueOf(MainData.getID()) 
							+ "_"
							+ String.valueOf(va.getParentIndex())
							+ "_"
							+ String.valueOf(va.getIndex())
							+ ".py");
			String pathSuffix = "tmp/" + pythonfile.getName();
			pythonfile.setPath(MainData.MEDIA_URL + "/viz_sets/" + pathSuffix);
			va.setPythonfile(pythonfile);
			va.setExportAllFramesForPython(true);
			CreateAndGetMatplotlibAnimationPythonfileWorker worker = new CreateAndGetMatplotlibAnimationPythonfileWorker(frame, va, file);
			worker.execute();
		}
	}
	
	/**
	 * Gets the tarfile save dir.
	 *
	 * @param filename the filename
	 * @return the tarfile save dir
	 */
	private File getPythonSaveDir(String filename){
		JFileChooser fileDialog = PlainFileChooserFactory.createPlainFileChooser();
		fileDialog.setAcceptAllFileFilterUsed(false);
		fileDialog.addChoosableFileFilter(new CustomFileFilter(FileType.PY));
		fileDialog.setSelectedFile(new File(filename));
		int returnVal = fileDialog.showSaveDialog(parent); 
		MainData.setAbsolutePath(fileDialog.getCurrentDirectory());
		if(returnVal==JFileChooser.APPROVE_OPTION){
			File file = fileDialog.getSelectedFile();
			String filepath = file.getAbsolutePath();
			if(new File(filepath).exists()){
				String msg = "The file " + file.getName() + " exists. Do you want to replace it?";
				int value = CautionDialog.createCautionDialog(frame, msg, "Attention!");
				if(value==CautionDialog.NO){
					getPythonSaveDir(file.getName());
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
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent ae){
		if(ae.getSource()==menuButton){
			openAnimationMenu();
		}else if(ae.getSource()==removeButton){
			if(compPane.getSelectedIndex()!=-1){
				if(compPane.getSelectedComponent() instanceof VizExplMoviePanel){
					((VizExplMoviePanel)compPane.getSelectedComponent()).stopLoading();
				}
				compList.remove(compPane.getSelectedComponent());
				vsulList.remove(compPane.getSelectedComponent());
				compPane.removeTabAt(compPane.getSelectedIndex());
				if(compPane.getTabCount()>0){
					compPane.setSelectedIndex(compPane.getTabCount()-1);
				}else{
					vizSetLabel.setText("");
					Data data = tree.getSelectedObject();
					if(data!=null){
						VizSet vs = null;
						Animation a = null;
						if(data instanceof VizSet){
							vs = (VizSet)data;
						}else if(data instanceof Animation){
							a = (Animation)data;
							vs = d.getSelectedVizSetMap().get(a.getParentIndex());
						}
						infoPanel.setCurrentState(vs, a, -1);
					}
					removeCompPanel();
				}
			}
		}else if(ae.getSource()==dataButton){
			saveData();
		}else if(ae.getSource()==fullButton){
			if(!fullScreenMode){
				enterFullScreenMode();
			}else{
				exitFullScreenMode();
			}
		}else if(ae.getSource()==selectButton){
			for(VizExplCompPanel panel: compList){
				if(panel instanceof VizExplMoviePanel){
					((VizExplMoviePanel)panel).stopLoading();
				}
			}
			parent.setCurrentState(VizExplPanel.VizExplMode.SELECT);
		}else if(ae.getSource()==saveButton){
			if(compPane.getSelectedIndex()!=-1){
				VizExplCompPanel panel = (VizExplCompPanel)compPane.getSelectedComponent();
				panel.saveCurrentFrameAsImage();
			}else{
				String string = "Please open an animation in the Animation Viewer from the Visualization Set tree at the left.";
				AttentionDialog.createDialog(frame, string);
			}
		}else if(ae.getSource()==emailButton){
			if(compPane.getSelectedIndex()!=-1){
				emailImage();
			}else{
				String string = "Please open an animation in the Animation Viewer from the Visualization Set tree at the left.";
				AttentionDialog.createDialog(frame, string);
			}
		}
	}
	
	private void saveData(){
		if(tree.getSelectedObject()!=null && compPane.getSelectedIndex()!=-1){
			if(tree.getSelectedObject() instanceof MatplotlibAnimation){
				MatplotlibAnimation va = (MatplotlibAnimation) tree.getSelectedObject();
				VizSet vs = d.getSelectedVizSetMap().get(va.getParentIndex());
				saveMatplotlibData(vs, va);
			}else if(tree.getSelectedObject() instanceof UploadedAnimation){
				AttentionDialog.createDialog(frame, "Data files are not available for uploaded animations.");
			}else{
				AttentionDialog.createDialog(frame, "Please select an animation from the Visualization Set tree at the left.");
			}
		}else{
			AttentionDialog.createDialog(frame, "Please select an animation from the Visualization Set tree at the left.");
		}
	}
	
	private void emailImage(){
		
		if(tree.getSelectedObject()!=null && compPane.getSelectedIndex()!=-1){
			String hyperlink = "";
			VizSet vs = null;
			if(tree.getSelectedObject() instanceof MatplotlibAnimation){
				MatplotlibAnimation va = (MatplotlibAnimation) tree.getSelectedObject();
				vs = d.getSelectedVizSetMap().get(va.getParentIndex());
				hyperlink = MainData.MEDIA_URL + "/viz_sets/" + vs.getIndex() + "/images/matplotlib/" + va.getIndex() 
						+ "_" + new DecimalFormat("00000").format(va.getCurrentFrame()) + ".png";
			}else if(tree.getSelectedObject() instanceof UploadedAnimation){
				UploadedAnimation ua = (UploadedAnimation) tree.getSelectedObject();
				vs = d.getSelectedVizSetMap().get(ua.getParentIndex());
				hyperlink = MainData.MEDIA_URL +"/viz_sets/" + vs.getIndex() + "/images/uploaded/" + ua.getIndex() 
						+ "_" + new DecimalFormat("00000").format(ua.getCurrentFrame()) + ".png";
			}else{
				AttentionDialog.createDialog(frame, "Please select an animation from the Visualization Set tree at the left.");
			}
			
			if(!hyperlink.equals("")){
				Desktop d;
				if (Desktop.isDesktopSupported() && (d = Desktop.getDesktop()).isSupported(Desktop.Action.MAIL)) {
					String mailtoString = "mailto:chimera-team@elist.ornl.gov?subject=Bellerophon Image from Visualization Set " + vs.getIndex();
					if(!vs.getVizSetId().equals("")){
						mailtoString += " (" + vs.getVizSetId() + ")";
					}
					mailtoString += "&body=" + infoPanel.getFullReportText() + " \n\nHyperlink to Image = " + hyperlink;
					mailtoString = mailtoString.replaceAll(" ", "%20");
					mailtoString = mailtoString.replaceAll("\n", "%0A");
					try {
						URI mailtoURI = new URI(mailtoString);
						d.mail(mailtoURI);
					} catch (URISyntaxException e) {
						e.printStackTrace();
					}catch (IOException e) {
						e.printStackTrace();
					}
				}else{
					AttentionDialog.createDialog(frame, "Your computer does not allow Java to access your email client.");
				}
			}
			
		}else{
			AttentionDialog.createDialog(frame, "Please select an animation from the Visualization Set tree at the left.");
		}
	}
	
	private void saveMatplotlibData(VizSet vs, MatplotlibAnimation va){
		File file = getMatplotlibDataSaveFile(va.toStringDataFilename(va.getCurrentFrame()));
		if(file!=null){
			GetMatplotlibAnimationDatafileWorker worker = new GetMatplotlibAnimationDatafileWorker(frame, va, file);
			worker.execute();
		}
	}
	
	private File getMatplotlibDataSaveFile(String filename){
		JFileChooser fileDialog = PlainFileChooserFactory.createPlainFileChooser();
		fileDialog.setAcceptAllFileFilterUsed(false);
		fileDialog.addChoosableFileFilter(new CustomFileFilter(FileType.H5));
		fileDialog.setSelectedFile(new File(filename));
		int returnVal = fileDialog.showSaveDialog(this); 
		MainData.setAbsolutePath(fileDialog.getCurrentDirectory());
		if(returnVal==JFileChooser.APPROVE_OPTION){
			File file = fileDialog.getSelectedFile();
			String filepath = file.getAbsolutePath();
			if(new File(filepath).exists()){
				String msg = "The file " + file.getName() + " exists. Do you want to replace it?";
				int value = CautionDialog.createCautionDialog(frame, msg, "Attention!");
				if(value==CautionDialog.NO){
					getMatplotlibDataSaveFile(file.getName());
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
	 * Take snapshot.
	 *
	 * @param vs the vs
	 * @param a the a
	 */
	private void takeSnapshot(VizSet vs, Animation a){
		if((vs.getVizJobMap()==null || vs.getVizJobMap().size()==0) && vs.getVizSetType()==VizSetType.CHIMERA2D){
			AttentionDialog.createDialog(frame, "The animation you selected does not contain any frames yet.");
		}else{
			try {
				saveAnimationAsTarfile(a);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Sets the hot.
	 *
	 * @param vs the vs
	 * @param a the a
	 */
	private void setHotMatplotlibAnimation(VizSet vs, MatplotlibAnimation va){
		vs.setHotMatplotlibAnimation(va.getIndex());
		SetHotMatplotlibAnimationWorker worker = new SetHotMatplotlibAnimationWorker(frame, this, va);
		worker.execute();
	}
	
	private void createMovie(VizSet vs, Animation a){
		if((vs.getVizJobMap()==null || vs.getVizJobMap().size()==0) && vs.getVizSetType()==VizSetType.CHIMERA2D){
			AttentionDialog.createDialog(frame, "The animation you selected does not contain any frames yet.");
		}else{
			try {
				saveAnimationAsMovie(a);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Save animation as tarfile.
	 *
	 * @param a the a
	 * @throws Exception the exception
	 */
	private void saveAnimationAsTarfile(Animation a) throws Exception{
		File file = getTarfileSaveDir(a.toStringTarFilename());
		if(file!=null){
			CustomFile tarfile = new CustomFile();
			tarfile.setName(String.valueOf(MainData.getID()) 
							+ "_"
							+ String.valueOf(a.getParentIndex())
							+ "_"
							+ String.valueOf(a.getIndex())
							+ ".tgz");
			String pathSuffix = "/tmp/" + tarfile.getName();
			tarfile.setPath(MainData.MEDIA_URL + "/viz_sets/" + pathSuffix);
			a.setTarfile(tarfile);
			a.setMetadata(infoPanel.getFullReportText());
			if(a instanceof MatplotlibAnimation){
				CreateAndGetMatplotlibAnimationTarfileWorker worker = new CreateAndGetMatplotlibAnimationTarfileWorker(frame, (MatplotlibAnimation) a, file);
				worker.execute();
			}else if(a instanceof UploadedAnimation){
				CreateAndGetUploadedAnimationTarfileWorker worker = new CreateAndGetUploadedAnimationTarfileWorker(frame, (UploadedAnimation) a, file);
				worker.execute();
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
	 * Save animation as movie.
	 *
	 * @param a the a
	 * @throws Exception the exception
	 */
	private void saveAnimationAsMovie(Animation a) throws Exception{
		if(a instanceof MatplotlibAnimation){
			CreateMatplotlibAnimationMoviefileWorker worker = new CreateMatplotlibAnimationMoviefileWorker(frame, (MatplotlibAnimation) a);
			worker.execute();
		}else if(a instanceof UploadedAnimation){
			CreateUploadedAnimationMoviefileWorker worker = new CreateUploadedAnimationMoviefileWorker(frame, (UploadedAnimation) a);
			worker.execute();
		}
	}

	
	/* (non-Javadoc)
	 * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
	 */
	public void stateChanged(ChangeEvent ce){
		if(ce.getSource()==compPane){
			if(compPane.getSelectedIndex()!=-1){
				VizExplCompPanel panel = (VizExplCompPanel)compPane.getSelectedComponent();
				vizSetLabel.setText("Visualization Set = " + panel.getVizSet().toString());
				for(VizExplCompPanel compPanel: compList){
					if(compPanel instanceof VizExplMoviePanel){
						VizExplMoviePanel moviePanel = (VizExplMoviePanel) compPanel;
						moviePanel.pauseMovie();
					}
				}
				tree.setSelectedAnimation(panel.getAnimation());
				infoPanel.setCurrentState(panel.getVizSet(), panel.getAnimation(), panel.getFrame());
			}
		}
	}
}