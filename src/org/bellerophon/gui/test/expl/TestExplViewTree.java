/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: TestExplViewTree.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.test.expl;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreePath;

import org.bellerophon.data.util.CustomFile;
import org.bellerophon.data.util.RegressionTest;
import org.bellerophon.exception.CaughtExceptionHandler;
import org.bellerophon.gui.dialog.HdfDialog;
import org.bellerophon.gui.test.expl.worker.GetDirListingWorker;
import org.bellerophon.gui.test.expl.worker.GetFileContentsWorker;

/**
 * The Class TestExplViewTree.
 *
 * @author Eric J. Lingerfelt
 */
public class TestExplViewTree extends JTree implements TreeWillExpandListener
															, TestExplViewTreeListener
															, TreeSelectionListener{
	
	private ArrayList<DefaultMutableTreeNode> expandedNodeList, testNodeList;
	private DefaultTreeModel model;
	private DefaultMutableTreeNode rootNode;
	private TestExplPanel parent;
	private TestExplViewPanel panel;
	private Frame frame;
	private TreeMap<Integer, RegressionTest> map;
	
	/**
	 * Instantiates a new test expl view tree.
	 *
	 * @param parent the parent
	 * @param panel the panel
	 * @param frame the frame
	 */
	public TestExplViewTree(TestExplPanel parent, TestExplViewPanel panel, Frame frame){
		this.parent = parent;
		this.panel = panel;
		this.frame = frame;
		setRootVisible(false);
		setShowsRootHandles(true);
		addTreeWillExpandListener(this);
		addMouseListener(new TestExplViewTreeAdapter(this));
		DefaultTreeSelectionModel model = new DefaultTreeSelectionModel();
		model.setSelectionMode(DefaultTreeSelectionModel.SINGLE_TREE_SELECTION);
		setSelectionModel(selectionModel);
		setCellRenderer(new CustomFileTreeCellRenderer());
		addTreeSelectionListener(this);
	}
	
	public void valueChanged(TreeSelectionEvent tse){
		if(tse.getSource()==this){
			if(getSelectionPath()!=null){
				DefaultMutableTreeNode node = (DefaultMutableTreeNode)getSelectionPath().getPathComponent(1);
				panel.setSelectedRegressionTest(map.get(Integer.valueOf(node.toString())));
			}else{
				panel.setSelectedRegressionTest(null);
			}
		}
	}
	
	/**
	 * Adds the files to dir node.
	 *
	 * @param node the node
	 */
	public void addFilesToDirNode(DefaultMutableTreeNode node){
		CustomFile file = (CustomFile)node.getUserObject();
		if(file.isDir() && node.getChildCount()>0){
			model.removeNodeFromParent((DefaultMutableTreeNode)node.getChildAt(0));
		}
		if(file.getFileMap()!=null && file.getFileMap().size()>0){
			Iterator<CustomFile> itr = file.getFileMap().values().iterator();
			while(itr.hasNext()){
				CustomFile f = itr.next();
				DefaultMutableTreeNode tmpNode = new DefaultMutableTreeNode(f);
				model.insertNodeInto(tmpNode, node, node.getChildCount());
				if(f.isDir() && f.isPop()){
					model.insertNodeInto(new DefaultMutableTreeNode(new CustomFile()), tmpNode, tmpNode.getChildCount());
				}
			}
			expandPath(new TreePath(node.getPath()));
		}
	}
	
	/**
	 * Request files for dir node.
	 *
	 * @param node the node
	 */
	public void requestFilesForDirNode(DefaultMutableTreeNode node){
		if(!expandedNodeList.contains(node)){
			expandedNodeList.add(node);
			parent.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			GetDirListingWorker worker = new GetDirListingWorker(parent, frame, this, node);
			worker.execute();
		}
	}
	
	/**
	 * Sets the current state.
	 *
	 * @param map the map
	 */
	public void setCurrentState(TreeMap<Integer, RegressionTest> map){
		this.map = map;
		rootNode = new DefaultMutableTreeNode(new CustomFile());
		model = new DefaultTreeModel(rootNode);
		setModel(model);
		expandedNodeList = new ArrayList<DefaultMutableTreeNode>();
		testNodeList = new ArrayList<DefaultMutableTreeNode>();
		
		Iterator<RegressionTest> itr = map.values().iterator();
		while(itr.hasNext()){
			RegressionTest rt = itr.next();
			CustomFile file = rt.getDir();
			DefaultMutableTreeNode testNode = new DefaultMutableTreeNode(file);
			testNodeList.add(testNode);
			model.insertNodeInto(testNode, rootNode, rootNode.getChildCount());
			Iterator<CustomFile> itrFile = file.getFileMap().values().iterator();
			while(itrFile.hasNext()){
				CustomFile f = itrFile.next();
				DefaultMutableTreeNode node = new DefaultMutableTreeNode(f);
				model.insertNodeInto(node, testNode, testNode.getChildCount());
				if(f.isDir()){
					model.insertNodeInto(new DefaultMutableTreeNode(new CustomFile()), node, node.getChildCount());
					//requestFilesForDirNode(node);
				}
			}
		}
		
		model.reload();
		validate();
		repaint();
		
		removeTreeWillExpandListener(this);
		for(DefaultMutableTreeNode testNode: testNodeList){
			expandPath(new TreePath(testNode.getPath()));
			expandedNodeList.add(testNode);
		}
		addTreeWillExpandListener(this);
	}
	
	/* (non-Javadoc)
	 * @see org.bellerophon.gui.test.expl.TestExplViewTreeListener#treeDoubleClicked()
	 */
	public void treeDoubleClicked(){
		if(getSelectionCount()>0){
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)getSelectionPath().getLastPathComponent();
			if(node.isLeaf()){
				CustomFile file = (CustomFile)node.getUserObject();
				if(!file.isDir()){
					if(!file.isHdf()){
						try{
							if(file.getContents()==null){
								parent.setCursor(new Cursor(Cursor.WAIT_CURSOR));
								GetFileContentsWorker worker = new GetFileContentsWorker(parent, panel, frame, node);
								worker.execute();
							}else{
								panel.setSelectedFile(file);
							}
						}catch(IOException e){
							CaughtExceptionHandler.handleException(e, frame);
						}
					}else{
						int selectedValue = HdfDialog.createHdfDialog(frame);
						if(selectedValue==HdfDialog.DUMP){
							panel.getHdfDumpContents(file);
						}else if(selectedValue==HdfDialog.SAVE){
							panel.getAndSaveHdfBinaryContents(file);
						}
					}
				}
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.event.TreeWillExpandListener#treeWillCollapse(javax.swing.event.TreeExpansionEvent)
	 */
	public void treeWillCollapse(TreeExpansionEvent tee){}
	
	/* (non-Javadoc)
	 * @see javax.swing.event.TreeWillExpandListener#treeWillExpand(javax.swing.event.TreeExpansionEvent)
	 */
	public void treeWillExpand(TreeExpansionEvent tee){
		requestFilesForDirNode((DefaultMutableTreeNode)tee.getPath().getLastPathComponent());
	}
	
}

class TestExplViewTreeAdapter extends MouseAdapter{
	
	private TestExplViewTreeListener tevtl;
	
	public TestExplViewTreeAdapter(TestExplViewTreeListener tevtl){
		this.tevtl = tevtl;
	}
	
	public void mouseClicked(MouseEvent me){
		if(me.getClickCount()>1){
			tevtl.treeDoubleClicked();
		}
	}
}

interface TestExplViewTreeListener {
	public void treeDoubleClicked();
}

class CustomFileTreeCellRenderer extends DefaultTreeCellRenderer{

	public Component getTreeCellRendererComponent(JTree tree
													, Object value
													, boolean selected
													, boolean expanded
													, boolean isLeaf
													, int row
													, boolean hasFocus){
		
		JLabel renderer = (JLabel)super.getTreeCellRendererComponent(tree
																	, value
																	, selected
																	, expanded
																	, isLeaf
																	, row
																	, hasFocus);
		
		DefaultMutableTreeNode node  = (DefaultMutableTreeNode)value;
		
		if(node.getUserObject() instanceof CustomFile){
			CustomFile file = (CustomFile)node.getUserObject();
			if(file.isDir()){
				if(expanded){
					renderer.setIcon(openIcon);
				}else{
					renderer.setIcon(closedIcon);
				}
			}else{
				renderer.setIcon(leafIcon);
			}
		}
		return renderer;
	}
}