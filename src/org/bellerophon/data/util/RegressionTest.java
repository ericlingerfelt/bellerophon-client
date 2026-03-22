/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: RegressionTest.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.data.util;

import java.util.*;

import org.bellerophon.data.Data;
import org.bellerophon.enums.*;
import org.bellerophon.gui.format.Calendars;

/**
 * The Class RegressionTest is the data structure for a regression test.
 *
 * @author Eric J. Lingerfelt
 */
public class RegressionTest implements Data{

	private int index, revision, compStatus, execStatus;
	private Calendar checkoutDate, compDate, execDate, endDate;
	private RegressionTestResult result;
	private Platform platform;
	private CodeType codeType;
	private CustomFile dir, tarfile;
	 
	/**
	 * The Constructor.
	 */
	public RegressionTest(){
		initialize();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public RegressionTest clone(){
		RegressionTest rt = new RegressionTest();
		rt.index = index;
		rt.revision = revision;
		rt.checkoutDate = (Calendar)checkoutDate.clone();
		rt.compDate = (Calendar)compDate.clone();
		rt.execDate = (Calendar)execDate.clone();
		rt.endDate = (Calendar)endDate.clone();
		rt.result = result;
		rt.platform = platform;
		rt.codeType = codeType;
		rt.compStatus = compStatus;
		rt.execStatus = execStatus;
		rt.dir = dir.clone();
		rt.tarfile = tarfile.clone();
		return rt;
	}
	
	public String toInfoString(){
		String s = "";
		s += "Regression Test Information\n";
		s += "Regression Test Index = " + index + "\n";
		s += "Result = " + result + "\n";
		s += "Platform = " + platform + "\n";
		s += "Code Type = " + codeType + "\n";
		s += "Revision = " + revision + "\n";
		s += "Checkout Date = " + Calendars.getFormattedDateString(checkoutDate) + "\n";
		s += "Compilation Date = " + Calendars.getFormattedDateString(compDate) + "\n";
		s += "Execution Date = " + Calendars.getFormattedDateString(execDate) + "\n";
		s += "End Date = " + Calendars.getFormattedDateString(endDate) + "\n";
		return s;
	}
	
	/* (non-Javadoc)
	 * @see org.bellerophon.data.Data#initialize()
	 */
	public void initialize(){
		index = -1;
		revision = -1;
		compStatus = -1;
		execStatus = -1;
		checkoutDate = Calendars.getDefaultCalendar();
		compDate = Calendars.getDefaultCalendar();
		execDate = Calendars.getDefaultCalendar();
		endDate = Calendars.getDefaultCalendar();
		result = null;
		platform = null;
		codeType = null;
		dir = null;
		tarfile = null;
	}
	
	/**
	 * Calc result.
	 */
	public void calcResult(){
		if(compStatus>0){
			result = RegressionTestResult.COMP_FAILURE;
		}else if(execStatus==-999 || compStatus==-999){
			result = RegressionTestResult.UNKNOWN;
		}else if(execStatus>0){
			result = RegressionTestResult.EXEC_FAILURE;
		}else{
			result = RegressionTestResult.EXEC_SUCCESS;
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return String.valueOf(index);
	}
	
	/**
	 * Gets the tarfile.
	 *
	 * @return the tarfile
	 */
	public CustomFile getTarfile(){return tarfile;}
	
	/**
	 * Sets the tarfile.
	 *
	 * @param tarfile the new tarfile
	 */
	public void setTarfile(CustomFile tarfile){this.tarfile = tarfile;}
	
	/**
	 * Gets the index.
	 *
	 * @return the index
	 */
	public int getIndex(){return index;}
	
	/**
	 * Sets the index.
	 *
	 * @param index the new index
	 */
	public void setIndex(int index){this.index = index;}
	
	/**
	 * Gets the comp exit status.
	 *
	 * @return the comp exit status
	 */
	public int getCompStatus(){return compStatus;}
	
	/**
	 * Sets the comp exit status.
	 *
	 * @param compExitStatus the new comp exit status
	 */
	public void setCompStatus(int compStatus){this.compStatus = compStatus;}
	
	/**
	 * Gets the exec exit status.
	 *
	 * @return the exec exit status
	 */
	public int getExecStatus(){return execStatus;}
	
	/**
	 * Sets the exec exit status.
	 *
	 * @param execExitStatus the new exec exit status
	 */
	public void setExecStatus(int execStatus){this.execStatus = execStatus;}
	
	/**
	 * Gets the revision.
	 *
	 * @return the revision
	 */
	public int getRevision(){return revision;}
	
	/**
	 * Sets the revision.
	 *
	 * @param revision the new revision
	 */
	public void setRevision(int revision){this.revision = revision;}
	
	public Calendar getCheckoutDate(){return checkoutDate;}
	public void setCheckoutDate(Calendar checkoutDate){this.checkoutDate = checkoutDate;}
	
	public Calendar getCompDate(){return compDate;}
	public void setCompDate(Calendar compDate){this.compDate = compDate;}
	
	public Calendar getExecDate(){return execDate;}
	public void setExecDate(Calendar execDate){this.execDate = execDate;}
	
	public Calendar getEndDate(){return endDate;}
	public void setEndDate(Calendar endDate){this.endDate = endDate;}
	
	/**
	 * Gets the result.
	 *
	 * @return the result
	 */
	public RegressionTestResult getResult(){return result;}
	
	/**
	 * Sets the result.
	 *
	 * @param result the new result
	 */
	public void setResult(RegressionTestResult result){this.result = result;}
	
	/**
	 * Gets the platform.
	 *
	 * @return the platform
	 */
	public Platform getPlatform(){return platform;}
	
	/**
	 * Sets the platform.
	 *
	 * @param platform the new platform
	 */
	public void setPlatform(Platform platform){this.platform = platform;}
	
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
	 * Gets the dir.
	 *
	 * @return the dir
	 */
	public CustomFile getDir(){return dir;}
	
	/**
	 * Sets the dir.
	 *
	 * @param dir the new dir
	 */
	public void setDir(CustomFile dir){this.dir = dir;}
	
}
