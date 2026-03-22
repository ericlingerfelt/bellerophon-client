/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: PrintableTextArea.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.export;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import javax.swing.JTextArea;
import javax.swing.RepaintManager;

/**
 * The Class PrintableTextArea.
 *
 * @author Eric J. Lingerfelt
 */
public class PrintableTextArea extends JTextArea implements Printable{
	
	/* (non-Javadoc)
	 * @see java.awt.print.Printable#print(java.awt.Graphics, java.awt.print.PageFormat, int)
	 */
	public int print (Graphics g, PageFormat pf, int pageIndex) throws PrinterException{
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor (Color.black);
		RepaintManager.currentManager(this).setDoubleBufferingEnabled(false);
		Dimension d = this.getSize();
		double panelWidth = d.width;
		double panelHeight = d.height;
		double pageWidth = pf.getImageableWidth();
		double pageHeight = pf.getImageableHeight();
		double scale = pageWidth / panelWidth;
		int totalNumPages = (int)Math.ceil(scale*panelHeight/pageHeight);

		// Check for empty pages
		if(pageIndex>=totalNumPages){
			return Printable.NO_SUCH_PAGE;
		}

		g2.translate(pf.getImageableX(), pf.getImageableY());
		g2.translate(0f, -pageIndex * pageHeight);
		g2.scale(scale, scale);
		this.paint(g2);
		
		return Printable.PAGE_EXISTS;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.text.JTextComponent#print()
	 */
	public boolean print(){
		
		PrinterJob job = PrinterJob.getPrinterJob();
		job.setPrintable(this);
		if(job.printDialog()){
			try{
				job.print();
			}catch(Exception e){
				return false;
			}
		}
		return true;
	}
}