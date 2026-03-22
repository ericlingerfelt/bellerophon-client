/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: MemoryMonitor.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.util;

/**
 * The Class MemoryMonitor.
 *
 * @author Eric J. Lingerfelt
 */
public class MemoryMonitor {

	/**
	 * Prints the memory.
	 */
	public static void printMemory(){
		System.out.println("-------------MEMORY START------------");
		System.out.println("FREE MEMORY = "  + Runtime.getRuntime().freeMemory()/1048576  + "MB");
		System.out.println("MAX MEMORY = "   + Runtime.getRuntime().maxMemory()/1048576   + "MB");
		System.out.println("TOTAL MEMORY = " + Runtime.getRuntime().totalMemory()/1048576 + "MB");
		System.out.println("-------------MEMORY END------------");
	}
	
}
