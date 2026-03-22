/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: CodeType.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.enums;

/**
 * The Enum CodeType contains values for each type of code available for regression testsing.
 *
 * @author Eric J. Lingerfelt
 */
public enum CodeType {

	CHIMERA_NEW("Chimera"),
	CHIMERA_POLARIS("Chimera Polaris"),
	CHIMERA_2D("Old Chimera 2D"),
	CHIMERA_3D("Old Chimera 3D"),
	CHIMERA_YY("Old Chimera YY"),
	UNKNOWN("Unknown");
	
	/** The string. */
	private String string;
	
	/**
	 * Instantiates a new code type.
	 *
	 * @param string the string
	 */
	CodeType(String string){
		this.string = string;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	public String toString(){return string;}
	
}