/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: RevisionLogEntry.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.data.util;

import java.util.*;

import org.bellerophon.data.Data;
import org.bellerophon.enums.CodeType;

/**
 * The Class RevisionLogEntry is the data structure for SVN revision logs.
 *
 * @author Eric J. Lingerfelt
 */
public class RevisionLogEntry implements Data{

	private int revision;
	private User author;
	private Calendar date;
	private String message;
	private CodeType codeType;
	
	/**
	 * The Constructor.
	 */
	public RevisionLogEntry(){
		initialize();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public RevisionLogEntry clone(){
		RevisionLogEntry rle = new RevisionLogEntry();
		rle.revision = revision;
		rle.author = author.clone();
		rle.date = (Calendar)date.clone();
		rle.message = message;
		rle.codeType = codeType;
		return rle;
	}
	
	/* (non-Javadoc)
	 * @see org.bellerophon.data.Data#initialize()
	 */
	public void initialize(){
		revision = -1;
		author = null;
		codeType = null;
		date = null;
		message = "";
	}
	
	/**
	 * Gets the revision.
	 *
	 * @return the revision
	 */
	public int getRevision(){return revision;}
	
	/**
	 * Sets the revision.
	 *
	 * @param revision the new revision
	 */
	public void setRevision(int revision){this.revision = revision;}
	
	/**
	 * Gets the author.
	 *
	 * @return the author
	 */
	public User getAuthor(){return author;}
	
	/**
	 * Sets the author.
	 *
	 * @param author the new author
	 */
	public void setAuthor(User author){this.author = author;}
	
	/**
	 * Gets the date.
	 *
	 * @return the date
	 */
	public Calendar getDate(){return date;}
	
	/**
	 * Sets the date.
	 *
	 * @param date the new date
	 */
	public void setDate(Calendar date){this.date = date;}
	
	/**
	 * Gets the message.
	 *
	 * @return the message
	 */
	public String getMessage(){return message;}
	
	/**
	 * Sets the message.
	 *
	 * @param message the new message
	 */
	public void setMessage(String message){this.message = message;}
	
	/**
	 * Gets the code type.
	 *
	 * @return the code type
	 */
	public CodeType getCodeType(){return codeType;}
	
	/**
	 * Sets the code type.
	 *
	 * @param codeType the new code type
	 */
	public void setCodeType(CodeType codeType){this.codeType = codeType;}
	
}
