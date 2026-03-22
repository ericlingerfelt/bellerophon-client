/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: GetFileContentsWorker.java
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
import org.bellerophon.exception.CaughtExceptionHandler;
import org.bellerophon.gui.dialog.ErrorResultDialog;
import org.bellerophon.gui.test.expl.TestExplPanel;
import org.bellerophon.gui.test.expl.TestExplViewPanel;
import org.bellerophon.io.IOUtilities;

/**
 * The Class GetFileContentsWorker.
 *
 * @author Eric J. Lingerfelt
 */
public class GetFileContentsWorker extends SwingWorker<ErrorResult, Void>{

	private TestExplPanel parent;
	private TestExplViewPanel panel;
	private Frame frame;
	private DefaultMutableTreeNode node;

	/**
	 * Instantiates a new gets the file contents worker.
	 *
	 * @param parent the parent
	 * @param panel the panel
	 * @param frame the frame
	 * @param node the node
	 */
	public GetFileContentsWorker(TestExplPanel parent
											, TestExplViewPanel panel
											, Frame frame
											, DefaultMutableTreeNode node){
		this.parent = parent;
		this.panel = panel;
		this.frame = frame;
		this.node = node;
	}

	/* (non-Javadoc)
	 * @see SwingWorker#doInBackground()
	 */
	protected ErrorResult doInBackground(){
		ErrorResult result = new ErrorResult();
		try {
			CustomFile file = (CustomFile)node.getUserObject();
			file.setContents(IOUtilities.readURL(file.getPath()));
		} catch (Exception e) {
			result.setError(true);
			result.setString(e.getMessage());
			return result;
		}
		return result;
	}
	
	/* (non-Javadoc)
	 * @see SwingWorker#done()
	 */
	protected void done(){
		try{
			ErrorResult result = get();
			if(!result.isError()){
				panel.setSelectedFile(((CustomFile)node.getUserObject()));
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