/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: CustomFile.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.data.util;

import java.io.File;
import java.io.IOException;
import java.util.*;
import org.bellerophon.data.*;
import org.bellerophon.io.IOUtilities;


/**
 * The Class CustomFile is a wrapper data structure for a file on disk or in memory.
 *
 * @author Eric J. Lingerfelt
 */
public class CustomFile implements Data{

	private String path, name;
	private File file, hdfDumpFile;
	private boolean dir, pop, hdf, img;
	private byte[] contents;
	private TreeMap<String, CustomFile> fileMap;
	private int size;
	
	/**
	 * The Constructor.
	 */
	public CustomFile(){
		initialize();
	}
	
	/* (non-Javadoc)
	 * @see org.bellerophon.data.Data#initialize()
	 */
	public void initialize(){
		path = "";
		name = "";
		dir = false;
		pop = false;
		hdf = false;
		img = false;
		fileMap = null;
		contents = null;
		file = null;
		hdfDumpFile = null;
		size = -1;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public CustomFile clone(){
		CustomFile cf = new CustomFile();
		cf.path = path;
		cf.hdf = hdf;
		cf.img = img;
		cf.contents = contents;
		cf.file = file;
		cf.hdfDumpFile = hdfDumpFile;
		//cf.fileMap = cloneFileMap();
		cf.dir = dir;
		cf.name = name;
		cf.size = size;
		cf.pop = pop;
		return cf;
	}
	
	/**
	 * Clone file map.
	 *
	 * @return the tree map
	 */
	private TreeMap<String, CustomFile> cloneFileMap(){
		TreeMap<String, CustomFile> map = new TreeMap<String, CustomFile>();
		Iterator<String> itr = fileMap.keySet().iterator();
		while(itr.hasNext()){
			String name = itr.next();
			map.put(name, fileMap.get(name).clone());
		}
		return map;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o){
		if(!(o instanceof CustomFile)){
			return false;
		}
		CustomFile f = (CustomFile)o;
		return f.path.equals(path);
	}
	
	/**
	 * Gets the hDF dump path.
	 *
	 * @return the hDF dump path
	 */
	public String getHDFDumpPath(){
		return MainData.TEST_URL + "/tmp/" + MainData.getID() + "_" + name.split("\\.")[0] + ".ascii"; 
	}
	
	/**
	 * Gets the size.
	 *
	 * @return the size
	 */
	public int getSize(){return size;}
	
	/**
	 * Sets the size.
	 *
	 * @param size the new size
	 */
	public void setSize(int size){this.size = size;}
	
	/**
	 * Gets the path.
	 *
	 * @return the path
	 */
	public String getPath(){return path;}
	
	/**
	 * Sets the path.
	 *
	 * @param path the new path
	 */
	public void setPath(String path){this.path = path;}
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName(){return name;}
	
	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name){this.name = name;}
	
	/**
	 * Gets the file.
	 *
	 * @return the file
	 */
	public File getFile(){return file;}
	
	/**
	 * Sets the file.
	 *
	 * @param file the new file
	 */
	public void setFile(File file){this.file = file;}
	
	/**
	 * Gets the hdf dump file.
	 *
	 * @return the hdf dump file
	 */
	public File getHdfDumpFile(){return hdfDumpFile;}
	
	/**
	 * Sets the hdf dump file.
	 *
	 * @param hdfDumpFile the new hdf dump file
	 */
	public void setHdfDumpFile(File hdfDumpFile){this.hdfDumpFile = hdfDumpFile;}
	
	/**
	 * Checks if is pop.
	 *
	 * @return true, if is pop
	 */
	public boolean isPop(){return pop;}
	
	/**
	 * Sets the pop.
	 *
	 * @param pop the new pop
	 */
	public void setPop(boolean pop){this.pop = pop;}
	
	/**
	 * Checks if is hdf.
	 *
	 * @return true, if is hdf
	 */
	public boolean isHdf(){return hdf;}
	
	/**
	 * Sets the hdf.
	 *
	 * @param hdf the new hdf
	 */
	public void setHdf(boolean hdf){this.hdf = hdf;}
	
	/**
	 * Checks if is img.
	 *
	 * @return true, if is img
	 */
	public boolean isImg(){return img;}
	
	/**
	 * Sets the img.
	 *
	 * @param img the new img
	 */
	public void setImg(boolean img){this.img = img;}
	
	/**
	 * Checks if is dir.
	 *
	 * @return true, if is dir
	 */
	public boolean isDir(){return dir;}
	
	/**
	 * Sets the dir.
	 *
	 * @param dir the new dir
	 */
	public void setDir(boolean dir){this.dir = dir;}
	
	/**
	 * Gets the file map.
	 *
	 * @return the file map
	 */
	public TreeMap<String, CustomFile> getFileMap(){return fileMap;}
	
	/**
	 * Sets the file map.
	 *
	 * @param fileMap the file map
	 */
	public void setFileMap(TreeMap<String, CustomFile> fileMap){this.fileMap = fileMap;}
	
	/**
	 * Sets the contents.
	 *
	 * @param array the new contents
	 * @throws Exception the exception
	 */
	public void setContents(byte[] array) throws Exception{
		//if(img){
			//contents = array;
		//}else{
			file = File.createTempFile("bellerophon", null);
			file.deleteOnExit();
			IOUtilities.writeFile(file, array);
		//}
	}
	
	/**
	 * Sets the hdf dump contents.
	 *
	 * @param array the new hdf dump contents
	 * @throws Exception the exception
	 */
	public void setHdfDumpContents(byte[] array) throws Exception{
		hdfDumpFile = File.createTempFile("bellerophon", null);
		hdfDumpFile.deleteOnExit();
		IOUtilities.writeFile(hdfDumpFile, array);
	}
	
	/**
	 * Gets the contents.
	 *
	 * @return the contents
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public byte[] getContents() throws IOException{
		//if(img){
			//return contents;
		//}
		if(file==null){
			return null;
		}
		return IOUtilities.readFile(file);
	}
	
	/**
	 * Gets the hdf dump contents.
	 *
	 * @return the hdf dump contents
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public byte[] getHdfDumpContents() throws IOException{
		if(hdfDumpFile==null){
			return null;
		}
		return IOUtilities.readFile(hdfDumpFile);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){return name;}
	
}
