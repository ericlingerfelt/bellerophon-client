/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: RegressionTestFilter.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.data.util;

import java.util.Calendar;

import org.bellerophon.data.Data;
import org.bellerophon.enums.*;


/**
 * The Class RegressionTestFilter filters regression tests.
 *
 * @author Eric J. Lingerfelt
 */
public class RegressionTestFilter implements Data{
	
	private int index, revision;
	private RegressionTestResult result;
	private Platform platform;
	private CodeType codeType;
	private Calendar checkoutDate;
	
	/**
	 * The Constructor.
	 */
	public RegressionTestFilter(){
		initialize();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public RegressionTestFilter clone(){
		RegressionTestFilter rtf = new RegressionTestFilter();
		rtf.index = index;
		rtf.revision = revision;
		rtf.result = result;
		rtf.checkoutDate = checkoutDate;
		rtf.platform = platform;
		rtf.codeType = codeType;
		return rtf;
	}
	
	/* (non-Javadoc)
	 * @see org.bellerophon.data.Data#initialize()
	 */
	public void initialize(){
		index = -1;
		revision = -1;
		result = null;
		codeType = null;
		platform = null;
		checkoutDate = null;
	}

	/**
	 * Apply filter.
	 *
	 * @param rt the rt
	 * @return true, if successful
	 */
	public boolean applyFilter(RegressionTest rt){
		if(revision!=-1 && rt.getRevision()!=revision){
			return false;
		}
		if(index!=-1 && rt.getIndex()!=index){
			return false;
		}
		if(result!=null && !rt.getResult().equals(result)){
			return false;
		}
		if(platform!=null && !rt.getPlatform().equals(platform)){
			return false;
		}
		if(codeType!=null && !rt.getCodeType().equals(codeType)){
			return false;
		}
		if(checkoutDate!=null && !rt.getCheckoutDate().equals(checkoutDate)){
			return false;
		}
		return true;
	}
	
	/**
	 * Gets the date.
	 *
	 * @return the date
	 */
	public Calendar getCheckoutDate(){return checkoutDate;}
	
	/**
	 * Sets the date.
	 *
	 * @param date the new date
	 */
	public void setCheckoutDate(Calendar checkoutDate){this.checkoutDate = checkoutDate;}
	
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
	
}
