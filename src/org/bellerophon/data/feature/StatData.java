/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: StatData.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.data.feature;

import java.util.*;
import org.bellerophon.data.Data;
import org.bellerophon.data.util.RevisionLogEntry;
import org.bellerophon.enums.CodeType;

/**
 * The Class StatData is the main data structure for the SVN Statistics On Demand tool.
 *
 * @author Eric J. Lingerfelt
 */
public class StatData implements Data{

	private int revMin, revMax;
	private Calendar dateMin, dateMax;
	private CodeType codeType;
	private ArrayList<String> fileList;
	private EnumMap<CodeType, TreeMap<Integer, RevisionLogEntry>> logMap;
	
	/**
	 * The Constructor.
	 */
	public StatData(){
		initialize();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public StatData clone(){
		StatData sd = new StatData();
		sd.revMin = revMin;
		sd.revMax = revMax;
		sd.dateMin = (Calendar)dateMin.clone();
		sd.dateMax = (Calendar)dateMax.clone();
		sd.fileList = fileList;
		sd.logMap = cloneLogMap();
		sd.codeType = codeType;
		return sd;
	}
	
	/**
	 * Clone log map.
	 *
	 * @return the enum map
	 */
	private EnumMap<CodeType, TreeMap<Integer, RevisionLogEntry>> cloneLogMap(){
		EnumMap<CodeType, TreeMap<Integer, RevisionLogEntry>> map 
			= new EnumMap<CodeType, TreeMap<Integer, RevisionLogEntry>>(CodeType.class);
		for(CodeType codeType: CodeType.values()){
			map.put(codeType, new TreeMap<Integer, RevisionLogEntry>());
			Iterator<Integer> itr = logMap.get(codeType).keySet().iterator();
			while(itr.hasNext()){
				int revision = itr.next();
				map.get(codeType).put(revision, logMap.get(codeType).get(revision));
			}
		}
		return map;
	}
	
	/* (non-Javadoc)
	 * @see org.bellerophon.data.Data#initialize()
	 */
	public void initialize(){
		revMin = -1;
		revMax = -1;
		codeType = null;
		dateMin = null;
		dateMax = null;
		fileList = null;
		logMap = null;
	}
	
	/**
	 * Gets the rev min.
	 *
	 * @return the rev min
	 */
	public int getRevMin(){return revMin;}
	
	/**
	 * Sets the rev min.
	 *
	 * @param revMin the new rev min
	 */
	public void setRevMin(int revMin){this.revMin = revMin;}
	
	/**
	 * Gets the rev max.
	 *
	 * @return the rev max
	 */
	public int getRevMax(){return revMax;}
	
	/**
	 * Sets the rev max.
	 *
	 * @param revMax the new rev max
	 */
	public void setRevMax(int revMax){this.revMax = revMax;}
	
	/**
	 * Gets the date min.
	 *
	 * @return the date min
	 */
	public Calendar getDateMin(){return dateMin;}
	
	/**
	 * Sets the date min.
	 *
	 * @param dateMin the new date min
	 */
	public void setDateMin(Calendar dateMin){this.dateMin = dateMin;}
	
	/**
	 * Gets the date max.
	 *
	 * @return the date max
	 */
	public Calendar getDateMax(){return dateMax;}
	
	/**
	 * Sets the date max.
	 *
	 * @param dateMax the new date max
	 */
	public void setDateMax(Calendar dateMax){this.dateMax = dateMax;}
	
	/**
	 * Gets the code type.
	 *
	 * @return the code type
	 */
	public CodeType getCodeType(){return codeType;}
	
	/**
	 * Sets the code type.
	 *
	 * @param codeType the new code type
	 */
	public void setCodeType(CodeType codeType){this.codeType = codeType;}
	
	/**
	 * Gets the file list.
	 *
	 * @return the file list
	 */
	public ArrayList<String> getFileList(){return fileList;}
	
	/**
	 * Sets the file list.
	 *
	 * @param fileList the new file list
	 */
	public void setFileList(ArrayList<String> fileList){this.fileList = fileList;}
	
	/**
	 * Gets the log map.
	 *
	 * @return the log map
	 */
	public EnumMap<CodeType, TreeMap<Integer, RevisionLogEntry>> getLogMap(){return logMap;}
	
	/**
	 * Sets the log map.
	 *
	 * @param logMap the log map
	 */
	public void setLogMap(EnumMap<CodeType, TreeMap<Integer, RevisionLogEntry>> logMap){this.logMap = logMap;}

}
