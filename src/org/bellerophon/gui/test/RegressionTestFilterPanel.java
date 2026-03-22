/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: RegressionTestFilterPanel.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.test;

import info.clearthought.layout.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;
import org.bellerophon.data.util.*;
import org.bellerophon.enums.*;
import org.bellerophon.gui.format.Borders;
import org.bellerophon.gui.format.Buttons;
import org.bellerophon.gui.format.Colors;
import org.bellerophon.gui.format.Icons;
import org.bellerophon.gui.test.expl.TestExplSelectPanel;
import org.jdesktop.swingx.*;

/**
 * The Class RegressionTestFilterPanel.
 *
 * @author Eric J. Lingerfelt
 */
public class RegressionTestFilterPanel extends JPanel implements ActionListener{
	
	private JComboBox revBox, indexBox, resultBox, platformBox, codeTypeBox;
	private JXDatePicker checkoutDateBox;
	private JButton filterButton;
	private boolean filter;
	private TestExplSelectPanel parent;
	
	/**
	 * Instantiates a new regression test filter panel.
	 *
	 * @param parent the parent
	 */
	public RegressionTestFilterPanel(TestExplSelectPanel parent){
		
		this.parent = parent;
		
		JLabel revLabel = new JLabel("Revision");
		JLabel indexLabel = new JLabel("Index");
		JLabel codeTypeLabel = new JLabel("Code Type");
		JLabel resultLabel = new JLabel("Result");
		JLabel platformLabel = new JLabel("Platform");
		JLabel checkoutDateLabel = new JLabel("Start Date");
		
		double[] col = {5, TableLayoutConstants.FILL, 5};
		double[] row = {5, TableLayoutConstants.PREFERRED
						, 20, TableLayoutConstants.PREFERRED, 5};
		setLayout(new TableLayout(col, row));
		
		double[] colBox = {TableLayoutConstants.FILL};
		double[] rowBox = {TableLayoutConstants.PREFERRED
							, 5, TableLayoutConstants.FILL
							, 10, TableLayoutConstants.PREFERRED
							, 5, TableLayoutConstants.FILL
							, 10, TableLayoutConstants.PREFERRED
							, 5, TableLayoutConstants.FILL
							, 10, TableLayoutConstants.PREFERRED
							, 5, TableLayoutConstants.FILL
							, 10, TableLayoutConstants.PREFERRED
							, 5, TableLayoutConstants.FILL
							, 10, TableLayoutConstants.PREFERRED
							, 5, TableLayoutConstants.FILL};
		JPanel boxPanel = new JPanel();
		boxPanel.setLayout(new TableLayout(colBox, rowBox));
		
		revBox = new JComboBox();
		indexBox = new JComboBox();
		codeTypeBox = new JComboBox();
		resultBox = new JComboBox();
		platformBox = new JComboBox();
		checkoutDateBox = new JXDatePicker();
		checkoutDateBox.setFormats(new SimpleDateFormat("MM/dd/yy"));
		checkoutDateBox.addActionListener(this);

		resultBox.addItem("-");
		for(RegressionTestResult result: RegressionTestResult.values()){
			resultBox.addItem(result);
		}
		resultBox.addActionListener(this);
		
		codeTypeBox.addItem("-");
		for(CodeType codeType: CodeType.values()){
			codeTypeBox.addItem(codeType);
		}
		codeTypeBox.addActionListener(this);
		
		filterButton = Buttons.getIconButton("Search Filter OFF"
												, "icons/process-stop.png"
												, Buttons.IconPosition.RIGHT
												, Colors.RED
												, this
												, new Dimension(100, 50));	
		
		boxPanel.add(indexLabel,     	"0, 0, l, c");
		boxPanel.add(indexBox,        	"0, 2, f, c");
		boxPanel.add(revLabel,        	"0, 4, l, c");
		boxPanel.add(revBox,          	"0, 6, f, c");
		boxPanel.add(codeTypeLabel,   	"0, 8, l, c");
		boxPanel.add(codeTypeBox,     	"0, 10, f, c");
		boxPanel.add(checkoutDateLabel,	"0, 12, l, c");
		boxPanel.add(checkoutDateBox,   "0, 14, f, c");
		boxPanel.add(platformLabel,   	"0, 16, l, c");
		boxPanel.add(platformBox,     	"0, 18, f, c");
		boxPanel.add(resultLabel,     	"0, 20, l, c");
		boxPanel.add(resultBox,       	"0, 22, f, c");
		
		add(boxPanel, "1, 1, f, c");
		add(filterButton, "1, 3, f, f");
		
		setBorder(Borders.getBorder("Search Filter"));
	}
	
	/**
	 * Sets the current state.
	 *
	 * @param map the map
	 */
	public void setCurrentState(TreeMap<Integer, RegressionTest> map){
		
		revBox.removeActionListener(this);
		indexBox.removeActionListener(this);
		platformBox.removeActionListener(this);
		
		revBox.removeAllItems();
		indexBox.removeAllItems();
		platformBox.removeAllItems();
		
		Iterator<RegressionTest> itr = map.values().iterator();
		
		ArrayList<Integer> revList = new ArrayList<Integer>();
		ArrayList<Integer> indexList = new ArrayList<Integer>();
		ArrayList<Platform> platformList = new ArrayList<Platform>();
		
		while(itr.hasNext()){
			RegressionTest rt = itr.next();
			if(!revList.contains(rt.getRevision())){
				revList.add(rt.getRevision());
			}
			if(!indexList.contains(rt.getIndex())){
				indexList.add(rt.getIndex());
			}
			if(!platformList.contains(rt.getPlatform())){
				platformList.add(rt.getPlatform());
			}
		}
		
		Collections.sort(revList);
		Collections.sort(indexList);
		Collections.sort(platformList);
		
		revBox.addItem("-");
		for(Integer i: revList){
			revBox.addItem(i);
		}
		
		indexBox.addItem("-");
		for(Integer i: indexList){
			indexBox.addItem(i);
		}
		
		platformBox.addItem("-");
		for(Platform p: platformList){
			platformBox.addItem(p);
		}
		
		revBox.addActionListener(this);
		indexBox.addActionListener(this);
		platformBox.addActionListener(this);
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent ae){
		if(ae.getSource()==filterButton){
			if(!filter){
				filterButton.setText("Search Filter ON");
				filterButton.setIcon(Icons.createImageIcon("/resources/images/icons/system-search.png"));
				filterButton.setForeground(Colors.BLUE);
				filter = true;
				applyFilter();
			}else{
				parent.setFilter(new RegressionTestFilter());
				filterButton.setText("Search Filter OFF");
				filterButton.setIcon(Icons.createImageIcon("/resources/images/icons/process-stop.png"));
				filterButton.setForeground(Colors.RED);
				filter = false;
				parent.setFilter(new RegressionTestFilter());
			}
		}else if(ae.getSource()==revBox 
					|| ae.getSource()==indexBox
					|| ae.getSource()==codeTypeBox
					|| ae.getSource()==resultBox
					|| ae.getSource()==platformBox
					|| ae.getSource()==checkoutDateBox){
			if(filter){
				applyFilter();
			}
		}
	}
	
	/**
	 * Apply filter.
	 */
	private void applyFilter(){
		Integer rev = revBox.getSelectedItem().toString().equals("-") ? -1 : (Integer)revBox.getSelectedItem();
		Integer index = indexBox.getSelectedItem().toString().equals("-") ? -1 : (Integer)indexBox.getSelectedItem();
		CodeType codeType = codeTypeBox.getSelectedItem().toString().equals("-") ? null : (CodeType)codeTypeBox.getSelectedItem();
		Platform platform = platformBox.getSelectedItem().toString().equals("-") ? null : (Platform)platformBox.getSelectedItem();
		Calendar checkoutDate = null;
		if(checkoutDateBox.getDate()!=null){
			Calendar c = Calendar.getInstance();
			c.clear();
			c.setTime(checkoutDateBox.getDate());
			checkoutDate = c;
		}
		RegressionTestResult result = resultBox.getSelectedItem().toString().equals("-") ? null : (RegressionTestResult)resultBox.getSelectedItem();
		RegressionTestFilter rtf = new RegressionTestFilter();
		rtf.setRevision(rev);
		rtf.setIndex(index);
		rtf.setCodeType(codeType);
		rtf.setResult(result);
		rtf.setPlatform(platform);
		rtf.setCheckoutDate(checkoutDate);
		parent.setFilter(rtf);
	}
	
}
