/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: VizSetFilterListener.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.viz;

import org.bellerophon.data.util.VizSetFilter;

/**
 * The listener interface for receiving vizSetFilter events.
 * The class that is interested in processing a vizSetFilter
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addVizSetFilterListener<code> method. When
 * the vizSetFilter event occurs, that object's appropriate
 * method is invoked.
 *
 * @see VizSetFilterEvent
 */
public interface VizSetFilterListener {
	
	/**
	 * Sets the filter.
	 *
	 * @param filter the new filter
	 */
	public void setFilter(VizSetFilter filter);
}
