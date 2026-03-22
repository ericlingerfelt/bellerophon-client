/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: Bellerophon.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon;

import java.awt.*;
import java.io.IOException;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.swing.SwingWorker;

import org.bellerophon.data.*;
import org.bellerophon.data.util.ErrorResult;
import org.bellerophon.gui.*;
import org.bellerophon.gui.dialog.*;
import org.bellerophon.gui.util.LogInAuthenticator;
import org.bellerophon.io.WebServiceCom;
import org.bellerophon.exception.*;
import org.bellerophon.enums.*;

import com.alee.laf.WebLookAndFeel;

/**
 * The Class Bellerophon is the top level class for the client side application. 
 * It contains the main method as well as attempts to authenticate the user against
 * the CHIMERA Wiki Login URL.
 *
 * @author Eric J. Lingerfelt
 */
public class Bellerophon {
	
	/**
	 * Instantiates a new Bellerophon client side application.
	 */
	public Bellerophon(){
		
		Authenticator.setDefault(new LogInAuthenticator());
		try{
			URL url = new URL("http://eagle.phys.utk.edu/chimera/trac/login");
			HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
			urlConnection.getInputStream();
		}catch(UnknownHostException uhe){
			ErrorDialog.createDialog(new Frame(), "An internet connection could not be established. Please check your connection and try again.");
			System.exit(1);
		}catch(IOException ioe){
			ioe.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		new LogInWorker().execute();
	
	}
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args){
		try{
			
			if(args.length!=3 && args.length!=4){
				
				System.err.println("Usage - java Bellerophon DEV/NONDEV RELEASE/DEBUG LOCAL/REMOTE");
				System.exit(1);
				
			}else if(args.length==3
					&& (!args[0].equalsIgnoreCase("DEV") && !args[0].equalsIgnoreCase("NONDEV")
					|| !args[1].equalsIgnoreCase("RELEASE") && !args[1].equalsIgnoreCase("DEBUG")
					|| !args[2].equalsIgnoreCase("LOCAL") && !args[2].equalsIgnoreCase("REMOTE"))){
				
				System.err.println("Usage - java Bellerophon DEV/NONDEV RELEASE/DEBUG LOCAL/REMOTE");
				System.exit(1);
				
			}else{
				
				MainData.initialize();
				
				if(args[0].equalsIgnoreCase("DEV")){
					MainData.setURLType(URLType.DEV);
				}else if(args[0].equalsIgnoreCase("NONDEV")){
					MainData.setURLType(URLType.NONDEV);
				}
				
				if(args[1].equalsIgnoreCase("DEBUG")){
					MainData.setDebug(true);
				}else if(args[1].equalsIgnoreCase("RELEASE")){
					MainData.setDebug(false);
				}
				
				if(args[2].equalsIgnoreCase("LOCAL")){
					MainData.setResourceType(ResourceType.LOCAL);

					TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			            public java.security.cert.X509Certificate[] getAcceptedIssuers() { return null; }
						@Override
						public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
								throws CertificateException {		
						}
						@Override
						public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
								throws CertificateException {
						}

			        } };

			        SSLContext sc = SSLContext.getInstance("SSL");
			        sc.init(null, trustAllCerts, new java.security.SecureRandom());
			        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			        // Create all-trusting host name verifier
			        HostnameVerifier allHostsValid = new HostnameVerifier() {
						@Override
						public boolean verify(String arg0, SSLSession arg1) {
							return false;
						}
			        };
			        // Install the all-trusting host verifier
			        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

				}else if(args[2].equalsIgnoreCase("REMOTE")){
					MainData.setResourceType(ResourceType.REMOTE);
				}
				
				String osName = System.getProperty("os.name");
				if(osName.startsWith("Windows")){
					MainData.setSystemType(SystemType.WINDOWS);
		    	}else if(osName.startsWith("Mac OS")){
		    		MainData.setSystemType(SystemType.MAC);
		    	}else{
		    		MainData.setSystemType(SystemType.LINUX);
		    	}
				
				Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler());
				
				java.awt.EventQueue.invokeLater(new Runnable(){
		            public void run(){
		            	WebLookAndFeel.install();
		            	new Bellerophon();	
		            }
				});

			}
		}catch(Exception e){
			e.printStackTrace();
			System.exit(1);
		}
	}
}

class LogInWorker extends SwingWorker<ErrorResult, Void>{

	private DelayDialog dialog;
	
	protected ErrorResult doInBackground(){
		dialog = new DelayDialog(new Frame(), "Please wait while you are logged in.", "Please wait...");
		dialog.open();
		ErrorResult result = WebServiceCom.getInstance().doWebServiceComCall(MainData.getUser(), Action.GET_ID);
		if(result.isError()){
			return result;
		}
		result = WebServiceCom.getInstance().doWebServiceComCall(MainData.getUser(), Action.GET_USER_DATA);
		if(result.isError()){
			return result;
		}
		result = WebServiceCom.getInstance().doWebServiceComCall(null, Action.GET_MATPLOTLIB_MODELS);
		if(result.isError()){
			return result;
		}
		result = WebServiceCom.getInstance().doWebServiceComCall(null, Action.GET_MATPLOTLIB_COLORMAPS);
		return result;
	}
	
	protected void done(){
		try{
			ErrorResult result = get();
			if(!result.isError()){
				dialog.close();
				new BellerophonFrame().setVisible(true);
			}else{
				dialog.close();
				ErrorResultDialog.createDialog(new Frame(), result);
				System.exit(1);
			}
		}catch(Exception e){
			dialog.close();
			CaughtExceptionHandler.handleException(e, new Frame());
			System.exit(1);
		}
	}
}
