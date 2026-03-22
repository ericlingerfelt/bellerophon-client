/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: LogInDialog.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.dialog;

import info.clearthought.layout.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import org.bellerophon.data.MainData;
import org.bellerophon.data.util.User;
import java.net.*;


/**
 * The Class LogInDialog.
 *
 * @author Eric J. Lingerfelt
 */
public class LogInDialog extends JDialog implements ActionListener, KeyListener{

	private JTextField usernameField;
	private JButton submitButton, cancelButton;
	private JPasswordField passwordField;
	private PasswordAuthentication pwAuth;
	
	/**
	 * Instantiates a new log in dialog.
	 *
	 * @param owner the owner
	 */
	public LogInDialog(Frame owner){
		
		super(owner, "Bellerophon Log In", true);
		
		setSize(420, 160);
		setLocationRelativeTo(owner);
		
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we){
				System.exit(0);
			} 
		});
		
		double gap = 10;
		double[] col = {gap, TableLayoutConstants.FILL, gap};
		double[] row = {gap, TableLayoutConstants.FILL, 5, TableLayoutConstants.PREFERRED, gap};
		
		Container c = getContentPane();
		c.setLayout(new TableLayout(col, row));
		
		usernameField = new JTextField();
		usernameField.addKeyListener(this);
		passwordField = new JPasswordField();
		passwordField.addKeyListener(this);
		
		JLabel usernameLabel = new JLabel("Username");
		JLabel passwordLabel = new JLabel("Password");
		
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		
		submitButton = new JButton("Submit");
		submitButton.addActionListener(this);
		
		double[] colData = {TableLayoutConstants.PREFERRED
								, 10, TableLayoutConstants.FILL};
		double[] rowData = {TableLayoutConstants.PREFERRED
								, 10, TableLayoutConstants.PREFERRED};
		JPanel dataPanel = new JPanel();
		dataPanel.setLayout(new TableLayout(colData, rowData));
		dataPanel.add(usernameLabel, "0, 0, r, c");
		dataPanel.add(usernameField, "2, 0, f, c");
		dataPanel.add(passwordLabel, "0, 2, r, c");
		dataPanel.add(passwordField, "2, 2, f, c");
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(submitButton);
		buttonPanel.add(cancelButton);

		c.add(dataPanel, "1, 1, f, f");
		c.add(buttonPanel, "1, 3, c, c");
		
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	public void keyPressed(KeyEvent ke){
		if(ke.getKeyCode()==KeyEvent.VK_ENTER){
			submitValues();
		}
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
	public void keyReleased(KeyEvent ke){}
	
	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	public void keyTyped(KeyEvent ke){}
	
	/**
	 * Initialize.
	 */
	private void initialize(){
		MainData.setUser(null);
		usernameField.setText("");
		passwordField.setText("");
	}
	
	/**
	 * Submit values.
	 */
	private void submitValues(){
		if(usernameField.getText().trim().equals("")){
			ErrorDialog.createDialog(this, "Please enter a value for Username.");
			return;
		}
		if(String.valueOf(passwordField.getPassword()).trim().equals("")){
			ErrorDialog.createDialog(this, "Please enter a value for Password.");
			return;
		}
		String username = usernameField.getText().trim();
		String password = String.valueOf(passwordField.getPassword()).trim();
		User u = new User();
		u.setUsername(username);
		MainData.setUser(u);
		pwAuth = new PasswordAuthentication (username, password.toCharArray());
		setVisible(false);
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent ae){
		if(ae.getSource()==submitButton){
			submitValues();
		}else if(ae.getSource()==cancelButton){
			System.exit(0);
		}
	}
	
	/**
	 * Creates the log in dialog.
	 *
	 * @param owner the owner
	 * @return the password authentication
	 */
	public static PasswordAuthentication createLogInDialog(Frame owner){
		LogInDialog dialog = new LogInDialog(owner);
		dialog.initialize();
		dialog.setVisible(true);
		return dialog.pwAuth;
	}
	
}