/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: TextSaver.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.export;

import java.awt.*;

import javax.swing.*;

import java.io.File;

import org.bellerophon.file.CustomFileFilter;
import org.bellerophon.file.FileType;
import org.bellerophon.gui.dialog.*;
import org.bellerophon.gui.util.PlainFileChooserFactory;
import org.bellerophon.io.IOUtilities;
import org.bellerophon.data.*;

import java.util.*;

/**
 * The Class TextSaver.
 *
 * @author Eric J. Lingerfelt
 */
public class TextSaver{

	/**
	 * Save plain text.
	 *
	 * @param string the string
	 * @param owner the owner
	 * @throws Exception the exception
	 */
	public static void savePlainText(String string, Frame owner) throws Exception{
		ArrayList<FileType> list = new ArrayList<FileType>();
		list.add(FileType.TXT);
		HashMap<FileType, String> map = new HashMap<FileType, String>();
		map.put(FileType.TXT, string);
		saveText(owner, list, map);
	}

	/**
	 * Save html text.
	 *
	 * @param string the string
	 * @param owner the owner
	 * @throws Exception the exception
	 */
	public static void saveHTMLText(String string, Frame owner) throws Exception{
		ArrayList<FileType> list = new ArrayList<FileType>();
		list.add(FileType.HTML);
		HashMap<FileType, String> map = new HashMap<FileType, String>();
		map.put(FileType.HTML, string);
		saveText(owner, list, map);
	}
	
	/**
	 * Save text.
	 *
	 * @param owner the owner
	 * @param text the text
	 * @param filename the filename
	 * @throws Exception the exception
	 */
	public static void saveText(Frame owner, String text, String filename) throws Exception{
		JFileChooser fileDialog = PlainFileChooserFactory.createPlainFileChooser();
		fileDialog.setAcceptAllFileFilterUsed(true);
		fileDialog.setSelectedFile(new File(filename));
		int returnVal = fileDialog.showSaveDialog(owner); 
		MainData.setAbsolutePath(fileDialog.getCurrentDirectory());
		if(returnVal==JFileChooser.APPROVE_OPTION){
			File file = fileDialog.getSelectedFile();
			String filepath = file.getAbsolutePath();
			if(new File(filepath).exists()){
				String msg = "The file " + file.getName() + " exists. Do you want to replace it?";
				int value = CautionDialog.createCautionDialog(owner, msg, "Attention!");
				if(value==CautionDialog.NO){
					saveText(owner, text, file.getName());
				}
			}
			IOUtilities.writeFile(filepath, text.getBytes());
		}
	}
	
	/**
	 * Save text.
	 *
	 * @param owner the owner
	 * @param list the list
	 * @param map the map
	 * @throws Exception the exception
	 */
	public static void saveText(Frame owner
								, ArrayList<FileType> list
								, HashMap<FileType, String> map) throws Exception{
		JFileChooser fileDialog = PlainFileChooserFactory.createPlainFileChooser();
		for(FileType type: list){
			fileDialog.addChoosableFileFilter(new CustomFileFilter(type));
		}
		fileDialog.setFileFilter(new CustomFileFilter(list.get(0)));
		fileDialog.setAcceptAllFileFilterUsed(false);
		int returnVal = fileDialog.showSaveDialog(owner); 
		CustomFileFilter selectedFilter = (CustomFileFilter)fileDialog.getFileFilter();
		MainData.setAbsolutePath(fileDialog.getCurrentDirectory());
		if(returnVal==JFileChooser.APPROVE_OPTION){
			File file = fileDialog.getSelectedFile();
			
			String filepath = file.getAbsolutePath();
        	if(selectedFilter.getFileExtension(file)==null){
        		filepath += "." + selectedFilter.getExtension();
        	}else if(!selectedFilter.getFileExtension(file).equals(selectedFilter.getExtension())){
        		filepath += "." + selectedFilter.getExtension();
        	}
        	
			if(new File(filepath).exists()){
				String msg = "The file " + file.getName() + " exists. Do you want to replace it?";
				int value = CautionDialog.createCautionDialog(owner, msg, "Attention!");
				if(value==CautionDialog.NO){
					saveText(owner, list, map);
				}
			}
       
        	IOUtilities.writeFile(filepath, map.get(selectedFilter.getFileType()).getBytes());

		}
	}
}