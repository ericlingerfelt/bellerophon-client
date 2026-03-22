package org.bellerophon.gui.util;

import java.awt.Frame;

import javax.swing.JFileChooser;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.bellerophon.data.MainData;
import org.bellerophon.exception.CaughtExceptionHandler;

public class PlainFileChooserFactory{
	public static JFileChooser createPlainFileChooser(){
		LookAndFeel previousLF = UIManager.getLookAndFeel();
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			JFileChooser fileDialog = new JFileChooser(MainData.getAbsolutePath()); 
			UIManager.setLookAndFeel(previousLF);
			return fileDialog;
		} catch (ClassNotFoundException 
				| InstantiationException
				| IllegalAccessException  
				| UnsupportedLookAndFeelException e){
			e.printStackTrace();
			CaughtExceptionHandler.handleException(e, new Frame());
		}
		return null;
	}
}
