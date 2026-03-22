/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: VizExplViewTree.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.viz.expl;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeMap;

import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.bellerophon.data.Data;
import org.bellerophon.data.util.Animation;
import org.bellerophon.data.util.UploadedAnimation;
import org.bellerophon.data.util.MatplotlibAnimation;
import org.bellerophon.data.util.VizSet;
import org.bellerophon.enums.VizSetType;
import org.bellerophon.gui.format.Icons;
import org.bellerophon.gui.viz.VizSetUpdateListener;

/**
 * The Class VizExplViewTree.
 *
 * @author Eric J. Lingerfelt
 */
public class VizExplViewTree extends JTree implements VizExplViewTreeListener
														, TreeSelectionListener
														, VizSetUpdateListener
														, TreeExpansionListener{
	
	private VizExplViewPanel panel;
	private TreeMap<Integer, TreePath> pathMap;
	private TreeMap<Integer, VizSet> map;
	private TreePath selectedTreePath;
	private ArrayList<TreePath> expandedPathList;
	private DefaultTreeModel model;
	private DefaultMutableTreeNode rootNode;
	/**
	 * Instantiates a new viz expl view tree.
	 *
	 * @param panel the panel
	 */
	public VizExplViewTree(VizExplViewPanel panel){
		this.panel = panel;
		
		ToolTipManager.sharedInstance().registerComponent(this);
		setRootVisible(false);
		setShowsRootHandles(true);
		addMouseListener(new VizExplViewTreeAdapter(this));
		addTreeExpansionListener(this);
		DefaultTreeSelectionModel selectionModel = new DefaultTreeSelectionModel();
		selectionModel.setSelectionMode(DefaultTreeSelectionModel.SINGLE_TREE_SELECTION);
		setSelectionModel(selectionModel);
		addTreeSelectionListener(this);
		setExpandsSelectedPaths(true);
		setCellRenderer(new AnimationTreeCellRenderer());
		expandedPathList = new ArrayList<TreePath>();
	
		rootNode = new DefaultMutableTreeNode("Viz Sets");
		model = new DefaultTreeModel(rootNode);
		setModel(model);
	}
	
	/**
	 * Sets the selected animation.
	 *
	 * @param a the new selected animation
	 */
	public void setSelectedAnimation(Animation a){
		setSelectionPath(pathMap.get(a.getIndex()));
	}
	
	/**
	 * Sets the current state.
	 *
	 * @param map the map
	 */
	public void setCurrentState(TreeMap<Integer, VizSet> map){
		
		this.map = map;

		removeTreeExpansionListener(this);
		rootNode.removeAllChildren();
		
		pathMap = new TreeMap<Integer, TreePath>();
		Iterator<VizSet> itr = map.values().iterator();
		
		while(itr.hasNext()){
			
			VizSet vs = itr.next();
			DefaultMutableTreeNode setNode = new DefaultMutableTreeNode(vs);
			model.insertNodeInto(setNode, rootNode, rootNode.getChildCount());

			if(vs.getVizSetType()==VizSetType.CHIMERA2D){
			
				DefaultMutableTreeNode matplotlibNode = new DefaultMutableTreeNode("Color Maps");
				model.insertNodeInto(matplotlibNode, setNode, setNode.getChildCount());
				
				Iterator<MatplotlibAnimation> itrMatplotlibAnimation = vs.getMatplotlibAnimationMap().values().iterator();
				ArrayList<MatplotlibAnimation> matplotlibAnimationList = new ArrayList<MatplotlibAnimation>();
				while(itrMatplotlibAnimation.hasNext()){
					matplotlibAnimationList.add(itrMatplotlibAnimation.next());
				}
				Collections.sort(matplotlibAnimationList, new MatplotlibAnimationComparator());
				for(MatplotlibAnimation va: matplotlibAnimationList){
					DefaultMutableTreeNode node = new DefaultMutableTreeNode(va);
					model.insertNodeInto(node, matplotlibNode, matplotlibNode.getChildCount());
					pathMap.put(va.getIndex(), new TreePath(node.getPath()));
				}
				
			}else if(vs.getVizSetType()==VizSetType.CHIMERA3D){

				DefaultMutableTreeNode uploadedNode = new DefaultMutableTreeNode("Uploaded Animations");
				model.insertNodeInto(uploadedNode, setNode, setNode.getChildCount());
				
				Iterator<UploadedAnimation> itrUploadedAnimation = vs.getUploadedAnimationMap().values().iterator();
				ArrayList<UploadedAnimation> uploadedAnimationList = new ArrayList<UploadedAnimation>();
				while(itrUploadedAnimation.hasNext()){
					uploadedAnimationList.add(itrUploadedAnimation.next());
				}
				Collections.sort(uploadedAnimationList, new UploadedAnimationComparator());
				for(UploadedAnimation ua: uploadedAnimationList){
					DefaultMutableTreeNode node = new DefaultMutableTreeNode(ua);
					model.insertNodeInto(node, uploadedNode, uploadedNode.getChildCount());
					pathMap.put(ua.getIndex(), new TreePath(node.getPath()));
				}
				
			}
			
		}
		
		removeTreeSelectionListener(this);
		
		model.reload();
		validate();
		repaint();
		
		
		for(TreePath path: expandedPathList){
			expandPath(getRealPath(path));
		}
		addTreeSelectionListener(this);
		
		if(selectedTreePath!=null){
			setSelectionPath(getRealPath(selectedTreePath));
		}
	
		addTreeExpansionListener(this);

	}
	
	private TreePath getRealPath(TreePath path){
		TreePath realPath = null;
		Object[] pathArray = path.getPath();
		if(pathArray.length==1){
			realPath = new TreePath(rootNode.getPath());
		}else if(pathArray.length==2){
			String vizSetName = pathArray[1].toString();
			realPath = findMatchingTreePath(rootNode, vizSetName);
		}else if(pathArray.length==3){
			String vizSetName = pathArray[1].toString();
			String typeName = pathArray[2].toString();
			for(int i=0; i<rootNode.getChildCount(); i++){
				if(rootNode.getChildAt(i).toString().equals(vizSetName)){
					TreeNode vizSetNode = rootNode.getChildAt(i);
					realPath = findMatchingTreePath(vizSetNode, typeName);
				}
			}
		}else if(pathArray.length==4){
			String vizSetName = pathArray[1].toString();
			String typeName = pathArray[2].toString();
			String animationName = pathArray[3].toString();
			for(int i=0; i<rootNode.getChildCount(); i++){
				if(rootNode.getChildAt(i).toString().equals(vizSetName)){
					TreeNode vizSetNode = rootNode.getChildAt(i);
					if(vizSetNode.getChildAt(0).toString().equals(typeName)){
						DefaultMutableTreeNode typeNode = (DefaultMutableTreeNode) vizSetNode.getChildAt(0);
						realPath = findMatchingTreePath(typeNode, animationName);
					}else if(vizSetNode.getChildAt(1).toString().equals(typeName)){
						DefaultMutableTreeNode typeNode = (DefaultMutableTreeNode) vizSetNode.getChildAt(1);
						realPath = findMatchingTreePath(typeNode, animationName);
					}
				}
			}
		}
		return realPath;
	}
	
	private TreePath findMatchingTreePath(TreeNode parentNode, String childname){
		for(int i=0; i<parentNode.getChildCount(); i++){
			if(parentNode.getChildAt(i).toString().equals(childname)){
				return new TreePath(((DefaultMutableTreeNode)parentNode.getChildAt(i)).getPath());
			}
		}
		return null;
	}
	
	@Override
	public void treeExpanded(TreeExpansionEvent tee) {
		expandedPathList.add(tee.getPath());
	}

	@Override
	public void treeCollapsed(TreeExpansionEvent tee) {
		TreePath treePath = tee.getPath();
		for(TreePath path: expandedPathList){
			Object[] pathArray = path.getPath();
			if(path.getPathCount()==treePath.getPathCount()){
				boolean foundIt = true;
				for(int i=0; i<pathArray.length; i++){
					if(!pathArray[i].toString().equals(treePath.getPath()[i].toString())){
						foundIt = false;
					}
				}
				if(foundIt){
					expandedPathList.remove(path);
					return;
				}
			}
		}
	}
	
	/**
	 * Gets the selected object.
	 *
	 * @return the selected object
	 */
	public Data getSelectedObject(){
		if(getSelectionPath()!=null){
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)getSelectionPath().getLastPathComponent();
			return (Data)node.getUserObject(); 
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
	 */
	public void valueChanged(TreeSelectionEvent tse){
		if(tse.getSource()==this){
			if(getSelectionPath()!=null){
				DefaultMutableTreeNode node = (DefaultMutableTreeNode)getSelectionPath().getLastPathComponent();
				selectedTreePath = getSelectionPath();
				if(node.getUserObject() instanceof VizSet){
					VizSet vs = (VizSet)node.getUserObject();
					panel.setSelectedVizSet(vs);
				}else if(node.getUserObject() instanceof Animation){
					Animation a = (Animation)node.getUserObject();
					panel.setSelectedAnimation(a);
				}
			}else{
				panel.setSelectedVizSet(null);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.bellerophon.gui.viz.expl.VizExplViewTreeListener#treeDoubleClicked()
	 */
	public void treeDoubleClicked(){
		if(getSelectionPath() != null){
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)getSelectionPath().getLastPathComponent();
			selectedTreePath = getSelectionPath();
			if(node.getUserObject() instanceof MatplotlibAnimation){
				panel.openAnimationMenu();
			}else if(node.getUserObject() instanceof UploadedAnimation){
				panel.openAnimationMenu();
			}
		}
	}

	@Override
	public void updateState(){
		setCurrentState(map);
	}

}

class AnimationTreeCellRenderer extends DefaultTreeCellRenderer {

	public Component getTreeCellRendererComponent(
			JTree tree,
			Object value,
			boolean sel,
			boolean expanded,
			boolean leaf,
			int row,
			boolean hasFocus) {

		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
		setBackgroundSelectionColor(Color.white);
		if(leaf && node.getUserObject() instanceof Animation){
			Animation a = (Animation)node.getUserObject();
			setToolTipText(a.toString());
			if(a.isHot()){
				setBorderSelectionColor(Color.red);
				setBackgroundSelectionColor(new Color(Color.red.getRed(), Color.red.getGreen(), Color.red.getBlue(), 50));
				setForeground(Color.red);
				setIcon(Icons.createImageIcon("/resources/images/icons/emblem-important.png"));
			}else{
				setForeground(Color.black);
				setBorderSelectionColor(Color.black);
				setBackgroundSelectionColor(new Color(Color.black.getRed(), Color.black.getGreen(), Color.black.getBlue(), 50));
			}
		}else{
			if(node.getUserObject() instanceof VizSet){
				VizSet vs = (VizSet)node.getUserObject();
				setToolTipText(vs.toString());
				setForeground(Color.black);
				setBorderSelectionColor(Color.black);
				setBackgroundSelectionColor(new Color(Color.black.getRed(), Color.black.getGreen(), Color.black.getBlue(), 50));
			}else if(node.getUserObject().toString().equals("Color Maps") 
					|| node.getUserObject().toString().equals("Line Plots")
					|| node.getUserObject().toString().equals("Uploaded Animations")){
				setForeground(Color.black);
				setBorderSelectionColor(Color.black);
				setBackgroundSelectionColor(new Color(Color.black.getRed(), Color.black.getGreen(), Color.black.getBlue(), 50));
			}
		}
		return this;
	}

}

class MatplotlibAnimationComparator implements Comparator<MatplotlibAnimation>{
	public int compare(MatplotlibAnimation va1, MatplotlibAnimation va2) {
		if(va1.getMatplotlibModel().toString().compareTo(va2.getMatplotlibModel().toString())==0){
			int zoom1 = Math.abs(Integer.valueOf(va1.getZoom().split(",")[0]));
			int zoom2 = Math.abs(Integer.valueOf(va2.getZoom().split(",")[0]));
			return zoom1 - zoom2;
		}
		return va1.getMatplotlibModel().toString().compareTo(va2.getMatplotlibModel().toString());
	}
}

class UploadedAnimationComparator implements Comparator<UploadedAnimation>{
	public int compare(UploadedAnimation ua1, UploadedAnimation ua2) {
		return ((Integer)ua1.getIndex()).compareTo(ua2.getIndex());
	}
}

class VizExplViewTreeAdapter extends MouseAdapter{
	
	private VizExplViewTreeListener vevtl;
	
	public VizExplViewTreeAdapter(VizExplViewTreeListener vevtl){
		this.vevtl = vevtl;
	}
	
	public void mouseClicked(MouseEvent me){
		if(me.getClickCount()>1){
			vevtl.treeDoubleClicked();
		}
	}
}

interface VizExplViewTreeListener {
	public void treeDoubleClicked();
}
