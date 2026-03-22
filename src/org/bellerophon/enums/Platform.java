/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: Platform.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.enums;


/**
 * The Enum Platform contains values for each allowable super computing platform.
 *
 * @author Eric J. Lingerfelt
 */
public enum Platform {

	JAGUAR("Jaguar XT4"),
	JAGUARPF("Jaguar XT5"),
	CORI("Cori"),
	HOPPER("Hopper"),
	EDISON("Edison"),
	DARTER("Darter"),
	CARVER("Carver"),
	TITAN("Titan"),
	GENASIS("Genasis"),
	KRAKEN("Kraken"),
	LENS("Lens"),
	NAUTILUS("Nautilus"),
	NEWTON("Newton"),
	SMOKY("Smoky"), 
	UNKNOWN("Unknown");
	
	private String string;
	
	/**
	 * Instantiates a new platform.
	 *
	 * @param string the string
	 */
	Platform(String string){
		this.string = string;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	public String toString(){return string;}
	
}
