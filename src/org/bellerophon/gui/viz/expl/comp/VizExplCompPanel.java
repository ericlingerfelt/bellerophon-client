/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: VizExplCompPanel.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.viz.expl.comp;

import java.awt.Frame;
import java.io.File;

import javax.swing.*;

import org.bellerophon.data.MainData;
import org.bellerophon.data.util.Animation;
import org.bellerophon.data.util.CustomFile;
import org.bellerophon.data.util.VizSet;
import org.bellerophon.enums.VizCompType;
import org.bellerophon.exception.CaughtExceptionHandler;
import org.bellerophon.file.CustomFileFilter;
import org.bellerophon.file.FileType;
import org.bellerophon.gui.dialog.CautionDialog;
import org.bellerophon.gui.util.PlainFileChooserFactory;
import org.bellerophon.gui.viz.VizSetUpdateListener;
import org.bellerophon.gui.viz.expl.VizExplViewPanel;
import org.bellerophon.io.IOUtilities;

/**
 * The Class VizExplCompPanel.
 *
 * @author Eric J. Lingerfelt
 */
public abstract class VizExplCompPanel extends JPanel implements VizSetUpdateListener{
	
	protected Frame parent;
	protected VizExplViewPanel panel;
	protected VizSet vs;
	protected Animation a;
	protected VizCompType type;
	protected CustomFile frameFile;
	protected int frame;
	
	/**
	 * Instantiates a new viz expl comp panel.
	 *
	 * @param vs the vs
	 * @param a the a
	 * @param type the type
	 */
	public VizExplCompPanel(VizSet vs, Animation a, VizCompType type){
		this(null, null, vs, a, type);
	}
	
	/**
	 * Instantiates a new viz expl comp panel.
	 *
	 * @param parent the parent
	 * @param panel the panel
	 * @param vs the vs
	 * @param a the a
	 * @param type the type
	 */
	public VizExplCompPanel(Frame parent, VizExplViewPanel panel, VizSet vs, Animation a, VizCompType type){
		this.parent = parent;
		this.panel = panel;
		this.vs = vs;
		this.a = a;
		this.type = type;
		this.frame = vs.getLastFrame();
	}
	
	public VizExplCompPanel(Frame parent, VizExplViewPanel panel, VizSet vs, Animation a, VizCompType type, int frame){
		this.parent = parent;
		this.panel = panel;
		this.vs = vs;
		this.a = a;
		this.type = type;
		this.frame = frame;
	}
	
	/**
	 * Gets the animation.
	 *
	 * @return the animation
	 */
	public Animation getAnimation(){return a;}
	
	/**
	 * Gets the viz set.
	 *
	 * @return the viz set
	 */
	public VizSet getVizSet(){return vs;}
	
	/**
	 * Gets the frame.
	 *
	 * @return the frame
	 */
	public int getFrame(){return frame;}
	
	/**
	 * Sets the selected frame.
	 */
	public void setSelectedFrame(){
		panel.setSelectedFrame(a, frame);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o){
		if(!(o instanceof VizExplCompPanel)){
			return false;
		}
		VizExplCompPanel p = (VizExplCompPanel) o;
		return p.vs.equals(vs) 
				&& p.a.equals(a)
				&& p.type==type;
	}
	
	/**
	 * Save current frame as image.
	 */
	public void saveCurrentFrameAsImage(){
		File file = getImageSaveFile(a.toStringFrameFilename(frame));
		if(file!=null){
			try{
				IOUtilities.writeFile(file, frameFile.getContents());
			}catch(Exception e){
				e.printStackTrace();
				CaughtExceptionHandler.handleException(e, parent);
			}
		}
	}
	
	/**
	 * Gets the image save file.
	 *
	 * @param filename the filename
	 * @return the image save file
	 */
	private File getImageSaveFile(String filename){
		JFileChooser fileDialog = PlainFileChooserFactory.createPlainFileChooser();
		fileDialog.setAcceptAllFileFilterUsed(false);
		fileDialog.addChoosableFileFilter(new CustomFileFilter(FileType.PNG));
		fileDialog.setSelectedFile(new File(filename));
		int returnVal = fileDialog.showSaveDialog(this); 
		MainData.setAbsolutePath(fileDialog.getCurrentDirectory());
		if(returnVal==JFileChooser.APPROVE_OPTION){
			File file = fileDialog.getSelectedFile();
			String filepath = file.getAbsolutePath();
			if(new File(filepath).exists()){
				String msg = "The file " + file.getName() + " exists. Do you want to replace it?";
				int value = CautionDialog.createCautionDialog(parent, msg, "Attention!");
				if(value==CautionDialog.NO){
					getImageSaveFile(file.getName());
				}else{
					return file;
				}
			}else{
				return file;
			}
		}
		return null;
	}

}
