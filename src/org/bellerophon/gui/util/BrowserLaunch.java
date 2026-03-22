/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: BrowserLaunch.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.util;

import java.awt.*;

import org.bellerophon.gui.dialog.ErrorDialog;


/**
 * The Class BrowserLaunch.
 *
 * @author Eric J. Lingerfelt
 */
public class BrowserLaunch {

	/**
	 * Clean url for mac.
	 *
	 * @param url the url
	 * @return the string
	 */
	private static String cleanUrlForMac(String url){
		url = url.substring(url.indexOf("<")+1);
		return "mailto:" + url.substring(0, url.indexOf(">"));
	}

	/**
	 * Open url.
	 *
	 * @param url the url
	 * @param parent the parent
	 */
	public static void openURL(String url, Frame parent) {

		String osName = System.getProperty("os.name");
		try{
			if (osName.startsWith("Mac OS")){
				if(url.indexOf("mailto:")!=-1){
					url = cleanUrlForMac(url);
				}
				Runtime.getRuntime().exec("open " + url);
			}else if (osName.startsWith("Windows")){
				if(url.startsWith("http") || url.startsWith("mailto")){
					Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
				}else{
					Runtime.getRuntime().exec("cmd /c " + url);
				}
			}else { //assume Unix or Linux
				String[] browsers = {
						"safari", "firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape" };
				String browser = null;
				for (int count = 0; count < browsers.length && browser == null; count++)
					if (Runtime.getRuntime().exec(
							new String[] {"which", browsers[count]}).waitFor() == 0)
						browser = browsers[count];
				if (browser == null){
					throw new Exception("Could not find web browser");
				}
				Runtime.getRuntime().exec(new String[] {browser, url});
			}
		}catch (Exception e) {
			e.printStackTrace();
			ErrorDialog.createDialog(parent, "An error has occurred opening " + url + " on your computer.");
		}
	}

	/**
	 * Open url.
	 *
	 * @param url the url
	 * @param dlg the dlg
	 */
	public static void openURL(String url, Dialog dlg) {

		String osName = System.getProperty("os.name");
		try {
			if (osName.startsWith("Mac OS")) {
				if(url.indexOf("mailto:")!=-1){
					url = cleanUrlForMac(url);
				}
				Runtime.getRuntime().exec("open " + url);
			}
			else if (osName.startsWith("Windows")){
				if(url.startsWith("http") || url.startsWith("mailto")){
					Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
				}else{
					Runtime.getRuntime().exec("cmd /c " + url);
				}
			}else { //assume Unix or Linux
				String[] browsers = {
						"safari", "firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape" };
				String browser = null;
				for (int count = 0; count < browsers.length && browser == null; count++)
					if (Runtime.getRuntime().exec(
							new String[] {"which", browsers[count]}).waitFor() == 0)
						browser = browsers[count];
				if (browser == null){
					throw new Exception("Could not find web browser");
				}
				Runtime.getRuntime().exec(new String[] {browser, url});
			}
		}catch (Exception e) {
			e.printStackTrace();
			ErrorDialog.createDialog(dlg, "An error has occurred opening " + url + " on your computer.");
		}
	}

}
