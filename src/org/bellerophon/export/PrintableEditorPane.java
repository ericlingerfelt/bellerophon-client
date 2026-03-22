/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: PrintableEditorPane.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.export;

import java.awt.*;
import javax.swing.*;
import javax.swing.text.html.*;
import java.awt.print.*;

/**
 * The Class PrintableEditorPane.
 *
 * @author Eric J. Lingerfelt
 */
public class PrintableEditorPane extends FormattedHTMLEditorPane implements Printable{
	
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

class FormattedHTMLEditorPane extends JEditorPane{
	
	/**
	 * Instantiates a new formatted html editor pane.
	 */
	public FormattedHTMLEditorPane(){
		setEditable(false);
		setEditorKit(new HTMLEditorKit());
	} 
	
	/* (non-Javadoc)
	 * @see javax.swing.JEditorPane#setText(java.lang.String)
	 */
	public void setText(String string){
		if(string.indexOf("<body>")!=-1){
			string = string.replaceAll("<body>", "<style type=\"text/css\">body { font-size: 12pt; font-family: sans-serif }</style>");
		}
		super.setText(string);
	}
	
}
