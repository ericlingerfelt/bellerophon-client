/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: Data.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.data;

/**
 * The Interface Data is the top level interface for all Bellerophon data structures.
 *
 * @author Eric J. Lingerfelt
 */
public interface Data {
	
	/**
	 * Initializes the data structure!
	 */
	public void initialize();
	
	/**
	 * Clones this data structure.
	 *
	 * @return the data
	 */
	public Data clone();
}
