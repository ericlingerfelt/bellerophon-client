/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: MainData.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.data;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

import org.bellerophon.enums.*;
import org.bellerophon.data.util.*;
import org.bellerophon.file.FilterType;

import javax.swing.filechooser.*;


/**
 * The Class MainData is the main data structure for the application. MainData's methods are static 
 * so that these fields can be accessed without instantiation. 
 *
 * @author Eric J. Lingerfelt
 */
public class MainData {

	private static URLType urlType;
	private static SystemType systemType;
	private static ResourceType resourceType;
	private static boolean debug, warningShown, downloading;
	private static String id;
	private static java.io.File absolutePath;
	private static FilterType filter;
	private static User user;
	
	public static String BELLEROPHON_URL;
	public static String PHP_URL;
	public static String DATA_URL;
	public static String MEDIA_URL;
	public static String TEST_URL;
	public static final String TRAC_URL = "http://eagle.phys.utk.edu/chimera/trac";
	public static final String TRAC_CHANGESET_URL = TRAC_URL + "/changeset";
	public static final String SERVER_URL = "https://bellerophon2.ornl.gov";
	public static final String VERSION = "2.0.0";
	
	public static TreeMap<Integer, MatplotlibColormap> matplotlibColormapMap = new TreeMap<Integer, MatplotlibColormap>();
	public static TreeMap<String, MatplotlibModel> matplotlibModelMap = new TreeMap<String, MatplotlibModel>();
	public static ArrayList<String> progenitorList = new ArrayList<String>();
	
	/**
	 * Initializes the data structure.
	 */
	public static void initialize(){
		downloading = false;
		user = null;
		id = "";
		filter = FilterType.ALL;
		warningShown = false;
		absolutePath = new File(FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath() + "/Desktop");
	}
	
	public static ArrayList<String> getProgenitorList(){return progenitorList;} 
	public static void setProgenitorList(ArrayList<String> list){progenitorList=list;} 
	
	public static TreeMap<Integer, MatplotlibColormap> getMatplotlibColormapMap(){return matplotlibColormapMap;} 
	public static MatplotlibColormap getMatplotlibColormap(int index){return matplotlibColormapMap.get(index);} 
	public static void addMatplotlibColormap(MatplotlibColormap colormap){
		if(!matplotlibColormapMap.containsKey(colormap.getIndex())){
			matplotlibColormapMap.put(colormap.getIndex(), colormap);
		}
	}
	
	public static TreeMap<String, MatplotlibModel> getMatplotlibModelMap(){return matplotlibModelMap;} 
	public static MatplotlibModel getMatplotlibModel(int index){
		Iterator<MatplotlibModel> itr = matplotlibModelMap.values().iterator();
		while(itr.hasNext()){
			MatplotlibModel model = itr.next();
			if(model.getIndex()==index){
				return model;
			}
		}
		return null;
	} 
	public static void addMatplotlibModel(MatplotlibModel model){
		if(!matplotlibModelMap.containsKey(model.toString())){
			matplotlibModelMap.put(model.toString(), model);
		}
	}
	
	/**
	 * Gets the absolute path.
	 *
	 * @return the absolute path
	 */
	public static File getAbsolutePath(){return absolutePath;} 
	
	/**
	 * Sets the absolute path.
	 *
	 * @param file the new absolute path
	 */
	public static void setAbsolutePath(File file){absolutePath = file;}
	
	/**
	 * Gets the system type.
	 *
	 * @return the system type
	 */
	public static SystemType getSystemType(){return systemType;}
	
	/**
	 * Sets the system type.
	 *
	 * @param type the new system type
	 */
	public static void setSystemType(SystemType type){systemType = type;}
	
	/**
	 * Gets the resource type.
	 *
	 * @return the resource type
	 */
	public static ResourceType getResourceType(){return resourceType;}
	
	/**
	 * Sets the resource type.
	 *
	 * @param type the new resource type
	 */
	public static void setResourceType(ResourceType type){resourceType = type;}
	
	/**
	 * Gets the uRL type.
	 *
	 * @return the uRL type
	 */
	public static URLType getURLType(){return urlType;}
	
	/**
	 * Sets the uRL type.
	 *
	 * @param type the new uRL type
	 */
	public static void setURLType(URLType type){
		urlType = type;
		BELLEROPHON_URL = SERVER_URL + "/bellerophon";
		if(urlType==URLType.DEV){
			BELLEROPHON_URL += "_dev";
		}
		PHP_URL = BELLEROPHON_URL + "/php/bellerophon.php";
		DATA_URL = BELLEROPHON_URL + "/data";
		MEDIA_URL = BELLEROPHON_URL + "/media";
		TEST_URL = BELLEROPHON_URL + "/regression_tests";
	}
	
	/**
	 * Checks if is debug.
	 *
	 * @return true, if is debug
	 */
	public static boolean isDebug(){return debug;} 
	
	/**
	 * Sets the debug.
	 *
	 * @param flag the new debug
	 */
	public static void setDebug(boolean flag){debug = flag;}
	
	/**
	 * Checks if is downloading.
	 *
	 * @return true, if is downloading
	 */
	public static boolean isDownloading(){return downloading;} 
	
	/**
	 * Sets the downloading.
	 *
	 * @param flag the new downloading
	 */
	public static void setDownloading(boolean flag){downloading = flag;}
	
	/**
	 * Gets the user.
	 *
	 * @return the user
	 */
	public static User getUser(){return user;} 
	
	/**
	 * Sets the user.
	 *
	 * @param u the new user
	 */
	public static void setUser(User u){user = u;}
	
	/**
	 * Gets the iD.
	 *
	 * @return the iD
	 */
	public static String getID(){return id;} 
	
	/**
	 * Sets the iD.
	 *
	 * @param string the new iD
	 */
	public static void setID(String string){id = string;}
	
	/**
	 * Gets the filter.
	 *
	 * @return the filter
	 */
	public static FilterType getFilter(){return filter;} 
	
	/**
	 * Sets the filter.
	 *
	 * @param type the new filter
	 */
	public static void setFilter(FilterType type){filter = type;}
	
	/**
	 * Checks if is warning shown.
	 *
	 * @return true, if is warning shown
	 */
	public static boolean isWarningShown(){return warningShown;} 
	
	/**
	 * Sets the warning shown.
	 *
	 * @param flag the new warning shown
	 */
	public static void setWarningShown(boolean flag){warningShown = flag;}
	
}


