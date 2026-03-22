/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: DelayPanel.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.util;

import info.clearthought.layout.*;
import java.awt.*;

import javax.swing.*;
import org.bellerophon.gui.format.Colors;
import org.bellerophon.gui.format.Fonts;
import org.jdesktop.swingx.*;

/**
 * The Class DelayPanel.
 *
 * @author Eric J. Lingerfelt
 */
public class DelayPanel extends JPanel{

	/**
	 * Instantiates a new delay panel.
	 *
	 * @param message the message
	 */
	public DelayPanel(String message){
		
		WordWrapLabel descLabel = new WordWrapLabel(true);
		descLabel.setFont(Fonts.bigTitleFont);
		descLabel.setText(message);
		
		JXBusyLabel waitLabel = new JXBusyLabel(new Dimension(70, 71));
		waitLabel.getBusyPainter().setHighlightColor(Colors.frontColor); 
		waitLabel.getBusyPainter().setBaseColor(Colors.backColor); 
		waitLabel.getBusyPainter().setPoints(12);
		waitLabel.getBusyPainter().setTrailLength(15);
		waitLabel.setDelay(65);
		waitLabel.setBusy(true); 
		
		double[] col = {TableLayoutConstants.FILL};
		double[] row = {TableLayoutConstants.PREFERRED
						, 40, TableLayoutConstants.FILL};
		setLayout(new TableLayout(col, row));
		add(waitLabel, "0, 0, c, c"); 
        add(descLabel, "0, 2, c, c"); 
		
	}
	
}
