package org.bellerophon.gui.util;

import java.awt.Frame;
import java.io.File;

import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.bellerophon.data.MainData;
import org.bellerophon.exception.CaughtExceptionHandler;

import com.jtechlabs.ui.widget.directorychooser.DirectoryChooserDefaults;
import com.jtechlabs.ui.widget.directorychooser.JDirectoryChooser;

public class PlainDirectoryChooserFactory{
	public static File createPlainFileChooser(Frame frame){
		LookAndFeel previousLF = UIManager.getLookAndFeel();
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			DirectoryChooserDefaults.putOption(DirectoryChooserDefaults.PROP_ICONS_THEME, new Integer(JDirectoryChooser.ICONS_NATIVE));
			File dir = JDirectoryChooser.showDialog(frame, MainData.getAbsolutePath(), "Select Directory", "", JDirectoryChooser.ACCESS_NEW);
			UIManager.setLookAndFeel(previousLF);
			return dir;
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
