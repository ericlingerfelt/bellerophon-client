/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: Scale.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.enums;

/**
 * The Enum Scale contains values for each allowable type of VisIt Psuedocolor Plot scale.
 *
 * @author Eric J. Lingerfelt
 */
public enum Scale {

	LIN("Lin"),
	LOG("Log");	
	
	private String string;
	
	/**
	 * Instantiates a new scale.
	 *
	 * @param string the string
	 */
	Scale(String string){
		this.string = string;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	public String toString(){return string;}
	
}