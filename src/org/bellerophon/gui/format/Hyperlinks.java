/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: Hyperlinks.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.format;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import javax.swing.SwingConstants;

import org.bellerophon.gui.dialog.ErrorDialog;
import org.bellerophon.gui.util.BrowserLaunch;
import org.jdesktop.swingx.JXHyperlink;

/**
 * The Class Hyperlinks.
 *
 * @author Eric J. Lingerfelt
 */
public class Hyperlinks {
	
	/**
	 * Gets the hyperlink.
	 *
	 * @param label the label
	 * @param foreground the foreground
	 * @param url the url
	 * @param frame the frame
	 * @return the hyperlink
	 */
	public static JXHyperlink getHyperlink(String label
											, Color foreground
											, String url
											, Frame frame){
		JXHyperlink hyperlink = new JXHyperlink();
		hyperlink.setText("<html><h4><u><b>" + label + "</b></u></h4><html>");
		hyperlink.setVerticalTextPosition(SwingConstants.CENTER);
		hyperlink.setHorizontalAlignment(SwingConstants.CENTER);
		hyperlink.setVerticalAlignment(SwingConstants.CENTER);
		hyperlink.setForeground(foreground);
		hyperlink.addActionListener(new HyperlinkActionListener(url, frame));
		hyperlink.setClickedColor(foreground);
		hyperlink.setToolTipText(url);
		return hyperlink;
	}
	
	/**
	 * Gets the action hyperlink.
	 *
	 * @param label the label
	 * @param foreground the foreground
	 * @param frame the frame
	 * @param al the al
	 * @return the action hyperlink
	 */
	public static JXHyperlink getActionHyperlink(String label
											, Color foreground
											, Frame frame
											, ActionListener al){
		JXHyperlink hyperlink = new JXHyperlink();
		hyperlink.setText("<html><h4><u><b>" + label + "</b></u></h4><html>");
		hyperlink.setVerticalTextPosition(SwingConstants.CENTER);
		hyperlink.setHorizontalAlignment(SwingConstants.CENTER);
		hyperlink.setVerticalAlignment(SwingConstants.CENTER);
		hyperlink.setForeground(foreground);
		hyperlink.addActionListener(al);
		hyperlink.setClickedColor(foreground);
		hyperlink.setToolTipText(label);
		return hyperlink;
	}
	
	/**
	 * Gets the mail hyperlink.
	 *
	 * @param label the label
	 * @param foreground the foreground
	 * @param email the email
	 * @param frame the frame
	 * @return the mail hyperlink
	 */
	public static JXHyperlink getMailHyperlink(String label
													, Color foreground
													, String email
													, Frame frame){
		JXHyperlink hyperlink = new JXHyperlink();
		hyperlink.setText("<html><h4><u><b>" + label + "</b></u></h4><html>");
		hyperlink.setVerticalTextPosition(SwingConstants.CENTER);
		hyperlink.setHorizontalAlignment(SwingConstants.CENTER);
		hyperlink.setVerticalAlignment(SwingConstants.CENTER);
		hyperlink.setForeground(foreground);
		hyperlink.addActionListener(new MailHyperlinkActionListener(email, frame));
		hyperlink.setClickedColor(foreground);
		hyperlink.setToolTipText("mailto: " + email);
		return hyperlink;
	}
	
}

class MailHyperlinkActionListener implements ActionListener{
	
	private String email; 
	private Frame frame;
	
	public MailHyperlinkActionListener(String email, Frame frame){
		this.frame = frame;
		this.email = email;
	}
	
	public void actionPerformed(ActionEvent ae){
		try{
			if(Desktop.isDesktopSupported()){
				Desktop.getDesktop().mail(new URI("mailto", email, null));
			}else{
				BrowserLaunch.openURL("mailto: " + email, frame);
			}
		}catch(Exception ex){
			ex.printStackTrace();
			ErrorDialog.createDialog(frame, "Bellerophon is unable to detect a browser to open this URL.");
		}
	}
}

class HyperlinkActionListener implements ActionListener{
	
	private String url; 
	private Frame frame;
	
	public HyperlinkActionListener(String url, Frame frame){
		this.url = url;
		this.frame = frame;
	}
	
	public void actionPerformed(ActionEvent ae){
		try{
			if(Desktop.isDesktopSupported()){
				Desktop.getDesktop().browse(new URI(url));
			}else{
				BrowserLaunch.openURL(url, frame);
			}
		}catch(Exception ex){
			ex.printStackTrace();
			ErrorDialog.createDialog(frame, "Bellerophon is unable to detect a browser to open this URL.");
		}
	}
}


