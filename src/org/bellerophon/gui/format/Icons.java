/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: Icons.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.format;

import java.awt.Frame;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.swing.ImageIcon;

import org.bellerophon.data.MainData;
import org.bellerophon.enums.ResourceType;
import org.bellerophon.io.IOUtilities;

/**
 * The Class Icons.
 *
 * @author Eric J. Lingerfelt
 */
public class Icons {

	/**
	 * Creates the image icon.
	 *
	 * @param path the path
	 * @return the image icon
	 */
	public static ImageIcon createImageIcon(String path) {
		ImageIcon icon = new ImageIcon(new byte[0]);
		if(MainData.getResourceType()==ResourceType.LOCAL){
			icon = getImageIconFromDisk(path);
		}else if (MainData.getResourceType()==ResourceType.REMOTE){
			icon = getImageIconFromJar(path);
		}
		return icon;
    }
	
	/**
	 * Gets the image icon from disk.
	 *
	 * @param path the path
	 * @return the image icon from disk
	 */
	private static ImageIcon getImageIconFromDisk(String path){
		java.net.URL imgURL = Frame.class.getResource(path);
		if (imgURL != null) {
		    return new ImageIcon(imgURL);
		}
		return new ImageIcon(new byte[0]);
	}
	
	/**
	 * Gets the image icon from jar.
	 *
	 * @param path the path
	 * @return the image icon from jar
	 */
	private static ImageIcon getImageIconFromJar(String path){
		try{
			InputStream is;
			Icons i = new Icons();
			is = i.getClass().getResourceAsStream(path);
			if(is==null){System.err.println("Error loading file: " + path);}
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			IOUtilities.readStream(is, baos);
			return new ImageIcon(baos.toByteArray());
		}catch(Exception e){
			e.printStackTrace();
			return new ImageIcon(new byte[0]);
		}
	}
	
}

