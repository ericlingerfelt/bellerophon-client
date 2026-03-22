/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: JScrollPaneCorner.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.util;

import java.awt.*;
import javax.swing.*;

import org.bellerophon.gui.format.Colors;
import org.bellerophon.gui.format.Fonts;


/**
 * The Class JScrollPaneCorner.
 *
 * @author Eric J. Lingerfelt
 */
public class JScrollPaneCorner extends JComponent{
	
	private String string;
	
	/**
	 * Instantiates a new j scroll pane corner.
	 *
	 * @param string the string
	 */
	public JScrollPaneCorner(String string){
		this.string = string;
		setBorder(UIManager.getBorder("TableHeader.cellBorder"));
	}
	
	/**
	 * Instantiates a new j scroll pane corner.
	 */
	public JScrollPaneCorner(){
		this("");
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
        super.paintComponent(g2); 
		RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHints(hints);
		g2.setColor(Colors.backColor);
		g2.fillRect(0, 0, getWidth(), getHeight());
		g2.setColor(Colors.frontColor);
		g2.setFont(Fonts.textFont);
		int x = (int)(getWidth()-getWidth()/2.0-getFontMetrics(Fonts.textFont).stringWidth(string)/2.0);
		g2.drawString(string, x, 12);
	}
}
