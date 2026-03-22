/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: LogInAuthenticator.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.util;

import java.awt.Frame;
import java.net.*;

import org.bellerophon.gui.dialog.LogInDialog;

/**
 * The Class LogInAuthenticator.
 *
 * @author Eric J. Lingerfelt
 */
public class LogInAuthenticator extends Authenticator{
	
	/* (non-Javadoc)
	 * @see java.net.Authenticator#getPasswordAuthentication()
	 */
	protected PasswordAuthentication getPasswordAuthentication(){
		PasswordAuthentication pwAuth = LogInDialog.createLogInDialog(new Frame());
		return pwAuth;
	}
}
