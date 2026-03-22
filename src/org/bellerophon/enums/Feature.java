/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: Feature.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.enums;

/**
 * The Enum Feature contains values for each Bellerophon feature.
 *
 * @author Eric J. Lingerfelt
 */
public enum Feature {
	
	TEST_EXPL("Regression Test Explorer", "<html>Regression<p>Test Explorer</html>"), 
	VIZ_EXPL("Visualization Set Explorer", "<html>Visualization<p>Set Explorer</html>"),
	VIZ_CREATE("Visualization Set Manager", "<html>Visualization<p>Set Manager</html>"),
	STAT("SVN Statistics On-Demand", "<html>SVN Statistics<p>On-Demand</html>"), 
	INFO("Important Links and Information", "<html>Important Links<p>and Information</html>"),
	LOGOUT("Log Out and Exit Bellerophon", "<html>Log Out and<p>Exit Bellerophon</html>");
	
	private String string, htmlString;
	
	/**
	 * Instantiates a new feature.
	 *
	 * @param string the string
	 * @param htmlString the html string
	 */
	Feature(String string, String htmlString){
		this.string = string;
		this.htmlString = htmlString;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	public String toString(){return string;}	
	
	/**
	 * Gets the hTML string.
	 *
	 * @return the hTML string
	 */
	public String getHTMLString(){return htmlString;}
}
