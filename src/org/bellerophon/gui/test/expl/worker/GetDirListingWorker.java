/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: GetDirListingWorker.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.test.expl.worker;

import java.awt.Cursor;
import java.awt.Frame;

import javax.swing.SwingWorker;
import javax.swing.tree.DefaultMutableTreeNode;

import org.bellerophon.data.util.CustomFile;
import org.bellerophon.data.util.ErrorResult;
import org.bellerophon.enums.Action;
import org.bellerophon.exception.CaughtExceptionHandler;
import org.bellerophon.gui.dialog.ErrorResultDialog;
import org.bellerophon.gui.test.expl.TestExplPanel;
import org.bellerophon.gui.test.expl.TestExplViewTree;
import org.bellerophon.io.WebServiceCom;

/**
 * The Class GetDirListingWorker.
 *
 * @author Eric J. Lingerfelt
 */
public class GetDirListingWorker extends SwingWorker<ErrorResult, Void>{

	private TestExplPanel parent;
	private Frame frame;
	private TestExplViewTree tree;
	private DefaultMutableTreeNode node;

	/**
	 * Instantiates a new gets the dir listing worker.
	 *
	 * @param parent the parent
	 * @param frame the frame
	 * @param tree the tree
	 * @param node the node
	 */
	public GetDirListingWorker(TestExplPanel parent
											, Frame frame
											, TestExplViewTree tree
											, DefaultMutableTreeNode node){
		this.parent = parent;
		this.frame = frame;
		this.tree = tree;
		this.node = node;
	}

	/* (non-Javadoc)
	 * @see SwingWorker#doInBackground()
	 */
	protected ErrorResult doInBackground(){
		return WebServiceCom.getInstance().doWebServiceComCall((CustomFile)node.getUserObject(), Action.GET_DIR_LISTING);
	}
	
	/* (non-Javadoc)
	 * @see SwingWorker#done()
	 */
	protected void done(){
		try{
			ErrorResult result = get();
			if(!result.isError()){
				tree.addFilesToDirNode(node);
				parent.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}else{
				parent.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				ErrorResultDialog.createDialog(frame, result);
			}
		}catch(Exception e){
			parent.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			CaughtExceptionHandler.handleException(e, frame);
		}
	}
}
