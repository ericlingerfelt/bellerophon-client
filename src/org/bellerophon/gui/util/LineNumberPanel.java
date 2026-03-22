/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: LineNumberPanel.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.util;

import java.awt.*;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.text.*;

import org.bellerophon.gui.format.Colors;


/**
 * The Class LineNumberPanel.
 *
 * @author Eric J. Lingerfelt
 */
public class LineNumberPanel extends JPanel implements CaretListener
{
	
	public final static float LEFT = 0.0f;
	public final static float CENTER = 0.5f;
	public final static float RIGHT = 1.0f;
	private final static Border OUTER = new MatteBorder(0, 0, 0, 2, Color.GRAY);
	private final static int HEIGHT = Integer.MAX_VALUE - 1000000;
	private JTextComponent component;
	private boolean updateFont;
	private int borderGap;
	private float digitAlignment;
	private int minimumDisplayDigits;
	private int lastDigits;
    private int lastLine;
	private HashMap<String, FontMetrics> fonts;

	/**
	 * Instantiates a new line number panel.
	 *
	 * @param component the component
	 */
	public LineNumberPanel(JTextComponent component){
		this(component, 3);
	}

	/**
	 * Instantiates a new line number panel.
	 *
	 * @param component the component
	 * @param minimumDisplayDigits the minimum display digits
	 */
	public LineNumberPanel(JTextComponent component, int minimumDisplayDigits){
		this.component = component;
		this.minimumDisplayDigits = minimumDisplayDigits;
		setFont(component.getFont());
		setBorderGap(5);
		setDigitAlignment(RIGHT);
		setPreferredWidth();
		component.addCaretListener(this);
	}

	/**
	 * Gets the update font.
	 *
	 * @return the update font
	 */
	public boolean getUpdateFont()
	{
		return updateFont;
	}

	/**
	 * Sets the update font.
	 *
	 * @param updateFont the new update font
	 */
	public void setUpdateFont(boolean updateFont)
	{
		this.updateFont = updateFont;
	}

	/**
	 * Gets the border gap.
	 *
	 * @return the border gap
	 */
	public int getBorderGap()
	{
		return borderGap;
	}

	/**
	 * Sets the border gap.
	 *
	 * @param borderGap the new border gap
	 */
	public void setBorderGap(int borderGap)
	{
		this.borderGap = borderGap;
		Border inner = new EmptyBorder(0, borderGap, 0, borderGap);
		setBorder( new CompoundBorder(OUTER, inner) );
		lastDigits = 0;
		setPreferredWidth();
	}

	/**
	 * Sets the digit alignment.
	 *
	 * @param digitAlignment the new digit alignment
	 */
	public void setDigitAlignment(float digitAlignment)
	{
		this.digitAlignment =
			digitAlignment > 1.0f ? 1.0f : digitAlignment < 0.0f ? -1.0f : digitAlignment;
	}

	/**
	 * Sets the preferred width.
	 */
	private void setPreferredWidth()
	{
		Element root = component.getDocument().getDefaultRootElement();
		int lines = root.getElementCount();
		int digits = Math.max(String.valueOf(lines).length(), minimumDisplayDigits);

		//  Update sizes when number of digits in the line number changes

		if (lastDigits != digits)
		{
			lastDigits = digits;
			FontMetrics fontMetrics = getFontMetrics( getFont() );
			int width = fontMetrics.charWidth( '0' ) * digits;
			Insets insets = getInsets();
			int preferredWidth = insets.left + insets.right + width;

			Dimension d = getPreferredSize();
			d.setSize(preferredWidth, HEIGHT);
			setPreferredSize( d );
			setSize( d );
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		//	Determine the width of the space available to draw the line number

		FontMetrics fontMetrics = component.getFontMetrics( component.getFont() );
		Insets insets = getInsets();
		int availableWidth = getSize().width - insets.left - insets.right;

		//  Determine the rows to draw within the clipped bounds.

		Rectangle clip = g.getClipBounds();
		int rowStartOffset = component.viewToModel( new Point(0, clip.y) );
		int endOffset = component.viewToModel( new Point(0, clip.y + clip.height) );

		while (rowStartOffset <= endOffset)
		{
			try
            {
    			if (isCurrentLine(rowStartOffset)){
    				g.setColor(Colors.selectionColor);
    				int x = 0;
    				int y = getOffsetY(rowStartOffset, fontMetrics) - 9;
    				g.fillRect(x, y, (int)getPreferredSize().getWidth(), fontMetrics.getHeight());
    			}
    			g.setColor(getForeground());

    			//  Get the line number as a string and then determine the
    			//  "X" and "Y" offsets for drawing the string.

    			String lineNumber = getTextLineNumber(rowStartOffset);
    			int stringWidth = fontMetrics.stringWidth( lineNumber );
    			int x = getOffsetX(availableWidth, stringWidth) + insets.left;
				int y = getOffsetY(rowStartOffset, fontMetrics);
    			g.drawString(lineNumber, x, y);

    			//  Move to the next row

    			rowStartOffset = Utilities.getRowEnd(component, rowStartOffset) + 1;
			}
			catch(Exception e) {}
		}
	}

	/*
	 *  We need to know if the caret is currently positioned on the line we
	 *  are about to paint so the line number can be highlighted.
	 */
	/**
	 * Checks if is current line.
	 *
	 * @param rowStartOffset the row start offset
	 * @return true, if is current line
	 */
	private boolean isCurrentLine(int rowStartOffset)
	{
		int caretPosition = component.getCaretPosition();
		Element root = component.getDocument().getDefaultRootElement();

		if (root.getElementIndex( rowStartOffset ) == root.getElementIndex(caretPosition))
			return true;
		else
			return false;
	}

	/*
	 *	Get the line number to be drawn. The empty string will be returned
	 *  when a line of text has wrapped.
	 */
	/**
	 * Gets the text line number.
	 *
	 * @param rowStartOffset the row start offset
	 * @return the text line number
	 */
	protected String getTextLineNumber(int rowStartOffset)
	{
		Element root = component.getDocument().getDefaultRootElement();
		int index = root.getElementIndex( rowStartOffset );
		Element line = root.getElement( index );

		if (line.getStartOffset() == rowStartOffset)
			return String.valueOf(index + 1);
		else
			return "";
	}

	/*
	 *  Determine the X offset to properly align the line number when drawn
	 */
	/**
	 * Gets the offset x.
	 *
	 * @param availableWidth the available width
	 * @param stringWidth the string width
	 * @return the offset x
	 */
	private int getOffsetX(int availableWidth, int stringWidth)
	{
		return (int)((availableWidth - stringWidth) * digitAlignment);
	}

	/*
	 *  Determine the Y offset for the current row
	 */
	/**
	 * Gets the offset y.
	 *
	 * @param rowStartOffset the row start offset
	 * @param fontMetrics the font metrics
	 * @return the offset y
	 * @throws BadLocationException the bad location exception
	 */
	private int getOffsetY(int rowStartOffset, FontMetrics fontMetrics)
		throws BadLocationException
	{
		//  Get the bounding rectangle of the row

		Rectangle r = component.modelToView( rowStartOffset );
		int lineHeight = fontMetrics.getHeight();
		int y = r.y + r.height;
		int descent = 0;

		//  The text needs to be positioned above the bottom of the bounding
		//  rectangle based on the descent of the font(s) contained on the row.

		if (r.height == lineHeight)  // default font is being used
		{
			descent = fontMetrics.getDescent();
		}
		else  // We need to check all the attributes for font changes
		{
			if (fonts == null)
				fonts = new HashMap<String, FontMetrics>();

			Element root = component.getDocument().getDefaultRootElement();
			int index = root.getElementIndex( rowStartOffset );
			Element line = root.getElement( index );

			for (int i = 0; i < line.getElementCount(); i++)
			{
				Element child = line.getElement(i);
				AttributeSet as = child.getAttributes();
				String fontFamily = (String)as.getAttribute(StyleConstants.FontFamily);
				Integer fontSize = (Integer)as.getAttribute(StyleConstants.FontSize);
				String key = fontFamily + fontSize;

				FontMetrics fm = fonts.get( key );

				if (fm == null)
				{
					Font font = new Font(fontFamily, Font.PLAIN, fontSize);
					fm = component.getFontMetrics( font );
					fonts.put(key, fm);
				}

				descent = Math.max(descent, fm.getDescent());
			}
		}

		return y - descent;
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.CaretListener#caretUpdate(javax.swing.event.CaretEvent)
	 */
	public void caretUpdate(CaretEvent e)
	{
		//  Get the line the caret is positioned on

		int caretPosition = component.getCaretPosition();
		Element root = component.getDocument().getDefaultRootElement();
		int currentLine = root.getElementIndex( caretPosition );
		
		//  Need to repaint so the correct line number can be highlighted

		if (lastLine != currentLine)
		{
			repaint();
			lastLine = currentLine;
		}
	}

}
