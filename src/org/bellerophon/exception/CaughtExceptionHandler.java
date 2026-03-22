/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: CaughtExceptionHandler.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.bellerophon.data.util.UncaughtException;
import org.bellerophon.enums.Action;
import org.bellerophon.gui.dialog.MessageDialog;
import org.bellerophon.io.WebServiceCom;
import java.awt.*;

import javax.swing.JDialog;


/**
 * The Class CaughtExceptionHandler.
 *
 * @author Eric J. Lingerfelt
 */
public class CaughtExceptionHandler {

	/**
	 * Handle exception.
	 *
	 * @param e the e
	 * @param owner the owner
	 */
	public static void handleException(Exception e, Frame owner){
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		UncaughtException ueds = new UncaughtException();
		ueds.setStackTrace(sw.toString());
		WebServiceCom.getInstance().doWebServiceComCall(ueds, Action.LOG_JAVA_EXCEPTION);
		if(owner==null){
			owner = new Frame();
		}
		MessageDialog.createMessageDialog(owner
				, "An error has occurred completing your request. "
				+ "The appropriate staff have been notified."
				, "Error!");
	}
	
	/**
	 * Handle exception.
	 *
	 * @param e the e
	 * @param owner the owner
	 */
	public static void handleException(Exception e, JDialog owner){
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		UncaughtException ueds = new UncaughtException();
		ueds.setStackTrace(sw.toString());
		WebServiceCom.getInstance().doWebServiceComCall(ueds, Action.LOG_JAVA_EXCEPTION);
		if(owner==null){
			owner = new JDialog();
		}
		MessageDialog.createMessageDialog(owner
				, "An error has occurred completing your request. "
				+ "The appropriate staff have been notified."
				, "Error!");
	}
	
}
