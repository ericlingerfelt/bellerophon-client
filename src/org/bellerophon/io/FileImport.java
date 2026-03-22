/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: FileImport.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.io;

import java.io.*;

/**
 * The Class FileImport.
 *
 * @author Eric J. Lingerfelt
 */
public class FileImport{
	
	private static FileImport fi = new FileImport();

	/**
	 * Gets the file.
	 *
	 * @param filename the filename
	 * @return the file
	 */
	public static String getFile(String filename){
		
		try{
			String string = "";
			InputStream is = fi.getClass().getResourceAsStream("/resources/" + filename);
			if(is==null){System.err.println("Error loading file: " + "/resources/" + filename);}
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			IOUtilities.readStream(is, baos);
			string = new String(baos.toByteArray());
			return string;
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * Gets the file byte.
	 *
	 * @param filename the filename
	 * @return the file byte
	 */
	public static byte[] getFileByte(String filename){
		try{
			InputStream is = fi.getClass().getResourceAsStream("/resources/" + filename);
			if(is==null){System.err.println("Error loading file: " + "/resources/" + filename);}
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			IOUtilities.readStream(is, baos);
			return baos.toByteArray();
		}catch(Exception e){
			e.printStackTrace();
			return new byte[0];
		}
	}
}