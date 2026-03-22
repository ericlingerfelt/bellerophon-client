/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: Buttons.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.format;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * The Class Buttons.
 *
 * @author Eric J. Lingerfelt
 */
public class Buttons {

	/**
	 * The Enum IconPosition.
	 *
	 * @author Eric J. Lingerfelt
	 */
	public enum IconPosition{
		LEFT, 
		RIGHT
	}
	
	/**
	 * Gets the icon button.
	 *
	 * @param label the label
	 * @param icon the icon
	 * @param ip the ip
	 * @param foreground the foreground
	 * @param al the al
	 * @param size the size
	 * @return the icon button
	 */
	public static JButton getIconButton(String label
										, String icon
										, IconPosition ip
										, Color foreground
										, ActionListener al
										, Dimension size){
		return Buttons.getIconButton(label, icon, ip, foreground, al, size, 15);
	}
	
	public static JButton getIconButton(String label
										, String icon
										, IconPosition ip
										, Color foreground
										, ActionListener al
										, Dimension size
										, int iconTextGap){
		JButton button = new JButton(label, Icons.createImageIcon("/resources/images/" + icon));
		button.setVerticalTextPosition(SwingConstants.CENTER);
		button.setHorizontalAlignment(SwingConstants.CENTER);
		button.setVerticalAlignment(SwingConstants.CENTER);
		if(ip==IconPosition.RIGHT){
			button.setHorizontalTextPosition(SwingConstants.LEFT);
		}else{
			button.setHorizontalTextPosition(SwingConstants.RIGHT);
		}
		button.setForeground(foreground);
		button.addActionListener(al);
		button.setPreferredSize(size);
		button.setIconTextGap(iconTextGap);
		return button;
	}
	
	/**
	 * Gets the icon button.
	 *
	 * @param icon the icon
	 * @param foreground the foreground
	 * @param al the al
	 * @param tooltip the tooltip
	 * @return the icon button
	 */
	public static JButton getIconButton(String icon
								, Color foreground
								, ActionListener al
								, String tooltip){
		JButton button = new JButton(Icons.createImageIcon("/resources/images/" + icon));
		button.setForeground(foreground);
		button.addActionListener(al);
		button.setToolTipText(tooltip);
		button.setPreferredSize(new Dimension(button.getIcon().getIconWidth() + 10
												, button.getIcon().getIconHeight() + 10));
		return button;
	}
	
}
