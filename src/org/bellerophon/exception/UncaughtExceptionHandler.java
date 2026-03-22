/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: UncaughtExceptionHandler.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.exception;

import java.awt.Frame;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.bellerophon.enums.Action;
import org.bellerophon.gui.dialog.MessageDialog;
import org.bellerophon.io.WebServiceCom;
import org.bellerophon.data.util.UncaughtException;


/**
 * The Class UncaughtExceptionHandler.
 *
 * @author Eric J. Lingerfelt
 */
public class UncaughtExceptionHandler implements Thread.UncaughtExceptionHandler{
	
	/* (non-Javadoc)
	 * @see java.lang.Thread.UncaughtExceptionHandler#uncaughtException(java.lang.Thread, java.lang.Throwable)
	 */
	public void uncaughtException(Thread t, Throwable e){
		if(e instanceof java.lang.ThreadDeath
				|| e instanceof java.lang.IllegalThreadStateException
				|| e instanceof java.util.NoSuchElementException){
			return;
		}
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		UncaughtException ueds = new UncaughtException();
		ueds.setStackTrace(sw.toString());
		WebServiceCom.getInstance().doWebServiceComCall(ueds, Action.LOG_JAVA_EXCEPTION);
		MessageDialog.createMessageDialog(new Frame()
				, "An error has occurred completing your request. "
				+ "The appropriate staff have been notified."
				, "Error!");
	}

}
