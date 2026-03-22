/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: BytesReadListener.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.io;


/**
 * The listener interface for receiving bytesRead events.
 * The class that is interested in processing a bytesRead
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addBytesReadListener<code> method. When
 * the bytesRead event occurs, that object's appropriate
 * method is invoked.
 *
 * @see BytesReadEvent
 */
public interface BytesReadListener {
	
	/**
	 * Sets the bytes read.
	 *
	 * @param bytesRead the new bytes read
	 */
	public void setBytesRead(int bytesRead);
}
