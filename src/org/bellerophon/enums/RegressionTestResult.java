/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: RegressionTestResult.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.enums;

/**
 * The Enum RegressionTestResult contains values for ech type of allowable regression test result.
 *
 * @author Eric J. Lingerfelt
 */
public enum RegressionTestResult {

	COMP_FAILURE("Compilation Failure"),
	EXEC_FAILURE("Execution Failure"),
	EXEC_SUCCESS("Execution Success"),
	UNKNOWN("Unknown");
	
	private String string;
	
	/**
	 * Instantiates a new regression test result.
	 *
	 * @param string the string
	 */
	RegressionTestResult(String string){
		this.string = string;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	public String toString(){return string;}
	
}
