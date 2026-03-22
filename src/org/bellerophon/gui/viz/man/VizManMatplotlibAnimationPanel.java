/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: VizManAnimationPanel.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.viz.man;

import info.clearthought.layout.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.*;
import org.bellerophon.data.feature.VizManData;
import org.bellerophon.data.util.*;
import org.bellerophon.enums.Scale;
import org.bellerophon.gui.dialog.EditMatplotlibAnimationDialog;
import org.bellerophon.gui.dialog.CautionDialog;
import org.bellerophon.gui.dialog.DelayDialog;
import org.bellerophon.gui.dialog.MessageDialog;
import org.bellerophon.gui.format.*;
import org.bellerophon.gui.table.DefaultTable;
import org.bellerophon.gui.util.WordWrapLabel;
import org.bellerophon.gui.viz.man.VizManPanel.VizManMode;
import org.bellerophon.gui.viz.man.worker.*;

/**
 * The Class VizManAnimationPanel.
 *
 * @author Eric J. Lingerfelt
 */
public class VizManMatplotlibAnimationPanel extends JPanel implements ActionListener, MouseListener{

	private Frame frame;
	private VizManPanel parent;
	private VizManData d;
	private JButton submitButton, addButton, removeButton
					, editButton, backButton, undoButton;
	private WordWrapLabel topLabel, indexLabel, vizSetLabel;
	private MatplotlibAnimationTable table;
	private JPanel tablePanel;
	
	/**
	 * Instantiates a new viz create animation panel.
	 *
	 * @param frame the frame
	 * @param parent the parent
	 * @param d the d
	 */
	public VizManMatplotlibAnimationPanel(Frame frame, VizManPanel parent, VizManData d){
		
		this.frame = frame;
		this.parent = parent;
		this.d = d;
		
		submitButton = Buttons.getIconButton("Submit Viz Set"
												, "icons/go-next.png"
												, Buttons.IconPosition.RIGHT
												, Colors.GREEN
												, this
												, new Dimension(250, 50));
		
		addButton = Buttons.getIconButton("Add New Animation"
												, "icons/list-add.png"
												, Buttons.IconPosition.RIGHT
												, Colors.BLUE
												, this
												, new Dimension(225, 50));
		
		removeButton = Buttons.getIconButton("Remove Selected Animation"
												, "icons/list-remove.png"
												, Buttons.IconPosition.RIGHT
												, Colors.BLUE
												, this
												, new Dimension(225, 50));
		
		editButton = Buttons.getIconButton("Edit Selected Animation"
												, "icons/applications-accessories.png"
												, Buttons.IconPosition.RIGHT
												, Colors.RED
												, this
												, new Dimension(225, 50));
		
		backButton = Buttons.getIconButton("Edit Visualization Set"
												, "icons/go-previous.png"
												, Buttons.IconPosition.LEFT
												, Colors.GREEN
												, this
												, new Dimension(250, 50));
		
		undoButton = Buttons.getIconButton("Undo All Changes"
												, "icons/edit-undo.png"
												, Buttons.IconPosition.RIGHT
												, Colors.GREEN
												, this
												, new Dimension(225, 50));
		
		indexLabel = new WordWrapLabel(true);
		indexLabel.setFont(Borders.getBorderFont());
		
		vizSetLabel = new WordWrapLabel(true);
		vizSetLabel.setFont(Borders.getBorderFont());
		
		table = new MatplotlibAnimationTable();
		table.addMouseListener(this);
		JScrollPane tablePane = new JScrollPane(table);
		tablePanel = new JPanel();
		double[] colTable = {5, TableLayoutConstants.FILL, 5};
		double[] rowTable = {5, TableLayoutConstants.FILL, 5};
		tablePanel.setLayout(new TableLayout(colTable, rowTable));
		tablePanel.add(tablePane, "1, 1, f, f");
		tablePanel.setBorder(Borders.getBorder("Animations"));
		topLabel = new WordWrapLabel();
		
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent me){
		if(me.getClickCount()==2){
			editAnimation();
		}
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent me){}
	
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent me){}
	
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent me){}
	
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent me){}
	
	/**
	 * Sets the current state.
	 *
	 * @param initialize the new current state
	 */
	public void setCurrentState(boolean initialize){
		
		if(d.getState()==VizManData.VizCreateState.MOD){
			topLabel.setText("The visualization set you have selected does not contain any animations. " +
								"Click <i>Add New Animation</i> to begin.");
		}else{
			topLabel.setText("The visualization set you are creating does not contain any animations yet. " +
								"Click <i>Add New Animation</i> to begin.");
		}
		
		indexLabel.setText("Visualization Set Index = " + String.valueOf(d.getVizSet().getIndex()));
		vizSetLabel.setText("Visualization Set Unique ID = " + d.getVizSet().toString());
		
		removeAll();
		
		if(!table.isInitialized() || initialize){
			table.setCurrentState(d.getVizSet().getMatplotlibAnimationMap());
			table.setInitialized(true);
		}
		
		if(table.getDataVector().size()==0){
			
			JPanel centerPanel = new JPanel();
			double[] colCenter = {TableLayoutConstants.FILL};
			double[] rowCenter = {TableLayoutConstants.PREFERRED
									, 20, TableLayoutConstants.PREFERRED};
			centerPanel.setLayout(new TableLayout(colCenter, rowCenter));
			centerPanel.add(topLabel,     "0, 0, c, c");
			centerPanel.add(addButton,    "0, 2, c, c");
			
			double[] col = {TableLayoutConstants.FILL};
			double[] row = {TableLayoutConstants.PREFERRED
								, 10, TableLayoutConstants.PREFERRED
								, 10, TableLayoutConstants.FILL
								, 10, TableLayoutConstants.FILL};
			setLayout(new TableLayout(col, row));
			add(indexLabel,   	"0, 0, c, c");
			add(vizSetLabel,  	"0, 2, c, c");
			add(centerPanel,    "0, 4, c, c");
			add(backButton,    	"0, 6, l, b");
			
		}else{
			
			JPanel buttonPanel = new JPanel();
			
			if(d.getState()==VizManData.VizCreateState.MOD){
			
				double[] colButton = {TableLayoutConstants.PREFERRED
										, 20, TableLayoutConstants.PREFERRED
										, 20, TableLayoutConstants.PREFERRED
										, 20, TableLayoutConstants.PREFERRED};
				double[] rowButton = {TableLayoutConstants.PREFERRED};
				buttonPanel.setLayout(new TableLayout(colButton, rowButton));
				buttonPanel.add(addButton,    	"0, 0, c, c");
				buttonPanel.add(removeButton,   "2, 0, c, c");
				buttonPanel.add(editButton, 	"4, 0, c, c");
				buttonPanel.add(undoButton, 	"6, 0, c, c");
			
			}else{
				
				double[] colButton = {TableLayoutConstants.PREFERRED
										, 20, TableLayoutConstants.PREFERRED
										, 20, TableLayoutConstants.PREFERRED};
				double[] rowButton = {TableLayoutConstants.PREFERRED};
				buttonPanel.setLayout(new TableLayout(colButton, rowButton));
				buttonPanel.add(addButton,    	"0, 0, c, c");
				buttonPanel.add(removeButton,   "2, 0, c, c");
				buttonPanel.add(editButton, 	"4, 0, c, c");
				
			}
			
			double[] col = {TableLayoutConstants.FILL
								, 10,  TableLayoutConstants.FILL};
			double[] row = {TableLayoutConstants.PREFERRED
								, 10, TableLayoutConstants.PREFERRED
								, 10, TableLayoutConstants.FILL
								, 10, TableLayoutConstants.PREFERRED
								, 50, TableLayoutConstants.PREFERRED};
			setLayout(new TableLayout(col, row));
			
			add(indexLabel,   "0, 0, 2, 0, c, c");
			add(vizSetLabel,  "0, 2, 2, 2, c, c");
			add(tablePanel,   "0, 4, 2, 4, f, f");
			add(buttonPanel,  "0, 6, 2, 6, c, c");
			add(backButton,   "0, 8, l, b");
			add(submitButton, "2, 8, r, b");
			
		}
		
		validate();
		repaint();
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent ae){
		if(ae.getSource()==addButton){
			addAnimation();
		}else if(ae.getSource()==removeButton){
			removeAnimation();
		}else if(ae.getSource()==submitButton){
			submitVizSet();
		}else if(ae.getSource()==editButton){
			editAnimation();
		}else if(ae.getSource()==undoButton){
			undoChanges();
		}else if(ae.getSource()==backButton){
			getCurrentState();
			parent.setCurrentState(VizManMode.VIZ_SET);
		}
	}
	
	/**
	 * Undo changes.
	 */
	private void undoChanges(){
		String string = "You are about to undo all changes to this visualization set's animations and " +
						"revert to its original animations.<br><br><b>Are you sure?</b>";
		int returnValue = CautionDialog.createCautionDialog(frame, string, "Attention!");
		if(returnValue==CautionDialog.YES){
			setCurrentState(true);
		}
	}
	
	/**
	 * Submit viz set.
	 */
	private void submitVizSet(){
		getCurrentState();
		String string = "Please wait while the visualization set is submitted.";
		DelayDialog dialog = new DelayDialog(frame, string, "Please wait...");
		dialog.open();
		if(d.getVizSet().getIndex()==-1){
			Create2DVizSetWorker worker = new Create2DVizSetWorker(frame, parent, d.getVizSet(), dialog);
			worker.execute();
		}else{
			Mod2DVizSetWorker worker = new Mod2DVizSetWorker(frame, parent, d.getVizSet(), dialog);
			worker.execute();
		}
	}
	
	/**
	 * Edits the animation.
	 */
	private void editAnimation(){
		if(table.getSelectedRowCount()>0){
			int rowIndex = table.getSelectedRow();
			Vector<Object> animationRow = EditMatplotlibAnimationDialog.createEditMatplotlibAnimationDialog(frame
																				, table.getDataVector()
																				, rowIndex
																				, EditMatplotlibAnimationDialog.Type.EDIT
																				, d);
			if(animationRow!=null){
				if(animationRow.size()==10){
					animationRow.remove(9);
					animationRow.setElementAt(-1, 0);
					table.addAnimation(animationRow);
					setCurrentState(false);
				}else{
					table.replaceAnimation(animationRow, rowIndex);
				}
			}
		}else{
			MessageDialog.createMessageDialog(frame, "Please select an animation from the table to edit.", "Attention!");
		}
	}
	
	/**
	 * Adds the animation.
	 */
	private void addAnimation(){
		Vector<Object> animationRow = EditMatplotlibAnimationDialog.createEditMatplotlibAnimationDialog(frame
				, table.getDataVector()
				, -1
				, EditMatplotlibAnimationDialog.Type.ADD
				, d);
		if(animationRow!=null){
			table.addAnimation(animationRow);
			setCurrentState(false);
		}
	}
	
	/**
	 * Removes the animation.
	 */
	private void removeAnimation(){
		if(table.getSelectedRowCount()>0){
			String string = "You are about to delete the selected animation.<br><br><b>Are you sure?</b>";
			int returnValue = CautionDialog.createCautionDialog(frame, string, "Attention!");
			if(returnValue==CautionDialog.YES){
				table.removeAnimation();
				if(table.getRowCount()>0){
					table.setRowSelectionInterval(0, 0);
				}else{
					setCurrentState(false);
				}
			}
		}else{
			MessageDialog.createMessageDialog(frame, "Please select an animation from the table to remove.", "Attention!");
		}
	}
	
	/**
	 * Gets the current state.
	 *
	 * @return the current state
	 */
	public void getCurrentState(){
		Vector<Vector<Object>> vv = table.getDataVector();
		Iterator<Vector<Object>> itr = vv.iterator();
		TreeMap<Integer, MatplotlibAnimation> map = new TreeMap<Integer, MatplotlibAnimation>();
		int index = 0;
		while(itr.hasNext()){
			Vector<Object> v = itr.next();
			MatplotlibAnimation va = new MatplotlibAnimation();
			va.setIndex((Integer)v.get(0));
			va.setAnimationId((String)v.get(1));
			va.setMatplotlibModel((MatplotlibModel)v.get(2));
			va.setColormap((MatplotlibColormap)v.get(3));
			va.setZoom((String)v.get(4));
			va.setRange((String)v.get(5));
			va.setSmoothZones((Boolean)v.get(6));
			va.setDisplayDate((Boolean)v.get(7));
			va.setScale((Scale)v.get(8));
			map.put(index, va);
			index++;
		}
		d.getVizSet().setMatplotlibAnimationMap(map);
	}

}

class MatplotlibAnimationTable extends DefaultTable{
	
	private Vector<String> colNamesVector;
	private MatplotlibAnimationTableModel model;
	private boolean initialized = false;
	private ArrayList<Integer> modifiedRowList = new ArrayList<Integer>();
	private Border outside = new MatteBorder(1, 0, 1, 0, Color.RED);
	private Border inside = new EmptyBorder(0, 1, 0, 1);
	private Border highlight = new CompoundBorder(outside, inside);
	private TreeMap<Integer, MatplotlibAnimation> matplotlibAnimationMap;
	
	public MatplotlibAnimationTable(){
		
		colNamesVector = new Vector<String>();
		colNamesVector.add("Index");
		colNamesVector.add("Unique ID");
		colNamesVector.add("Model");
		colNamesVector.add("Colortable");
		colNamesVector.add("Zoom [km]");
		colNamesVector.add("Colortable Range");
		colNamesVector.add("Smooth Zones?");
		colNamesVector.add("Display Date?");
		colNamesVector.add("Scale");

		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		model = new MatplotlibAnimationTableModel();
		setModel(model);
		setRowSorter(null);
	}
	
	public boolean isInitialized(){
		return initialized;
	}
	
	public void setInitialized(boolean initialized){
		this.initialized = initialized;
		modifiedRowList = new ArrayList<Integer>();
	}
	
	public MatplotlibAnimationTableModel getModel(){
		return model;
	}
	
	public Vector<Vector<Object>> getDataVector(){
		if(isEditing()){
			for(int i=0; i<getRowCount(); i++){
				for(int j=0; j<getColumnCount(); j++){
					if(isCellEditable(i, j)){
						getCellEditor(i, j).stopCellEditing();
					}
				}
			}
		}
		return model.getDataVector();
	} 
	
	public void addAnimation(Vector<Object> v){
		model.insertRow(model.getRowCount(), v);
		modifiedRowList.add(model.getRowCount()-1);
	}
	
	public void removeAnimation(){
		model.removeRow(getSelectedRow());
	}
	
	public void replaceAnimation(Vector<Object> v, int rowIndex){
		
		if(!isModifiedAnimation(v)){
			modifiedRowList.remove((Integer)rowIndex);
		}else{
			modifiedRowList.add(rowIndex);
		}
		
		model.setValueAt(v.get(0), rowIndex, 0);
		model.setValueAt(v.get(1), rowIndex, 1);
		model.setValueAt(v.get(2), rowIndex, 2);
		model.setValueAt(v.get(3), rowIndex, 3);
		model.setValueAt(v.get(4), rowIndex, 4);
		model.setValueAt(v.get(5), rowIndex, 5);
		model.setValueAt(v.get(6), rowIndex, 6);
		model.setValueAt(v.get(7), rowIndex, 7);
		model.setValueAt(v.get(8), rowIndex, 8);

	}
	
	private boolean isModifiedAnimation(Vector<Object> v){
		MatplotlibAnimation va  = matplotlibAnimationMap.get(v.get(0));
		MatplotlibModel matplotlibModel = (MatplotlibModel)v.get(2);
		
		if(va==null){
			return true;
		}
		
		if(va.getMatplotlibModel()!=matplotlibModel){
			return true;
		}
		
		if(((String)v.get(1)).equals(va.getAnimationId())
					&& ((MatplotlibColormap)v.get(3))==va.getColormap()
					&& ((String)v.get(4)).equals(va.getZoom())
					&& ((String)v.get(5)).equals(va.getRange())
					&& ((Boolean)v.get(6))==va.getSmoothZones()
					&& ((Boolean)v.get(7))==va.getDisplayDate()
					&& ((Scale)v.get(8))==va.getScale()){
			return false;
		}
		return true;
	}
	
	public Component prepareRenderer(TableCellRenderer renderer, int row, int column){
        Component c = super.prepareRenderer(renderer, row, column);
        JComponent jc = (JComponent)c;
        if(modifiedRowList.contains(row)){
        	jc.setBorder(highlight);
        	jc.setForeground(Color.RED);
        	if(this.isRowSelected(row)){
        		jc.setBackground(new Color(Color.red.getRed(), Color.red.getGreen(), Color.red.getBlue(), 50));
        	}
        }
        return c;
    }
	
	public void setCurrentState(TreeMap<Integer, MatplotlibAnimation> matplotlibAnimationMap){
		this.matplotlibAnimationMap = matplotlibAnimationMap;
		Vector<Vector<Object>> dataVector = new Vector<Vector<Object>>();
		if(matplotlibAnimationMap!=null){
			ArrayList<Integer> keyList = new ArrayList<Integer>();
			Iterator<Integer> itr = matplotlibAnimationMap.keySet().iterator();
			while(itr.hasNext()){
				keyList.add(itr.next());
			}
			//Collections.sort(keyList, Collections.reverseOrder());
			
			for(int key: keyList){
				Vector<Object> v = new Vector<Object>();
				MatplotlibAnimation va = matplotlibAnimationMap.get(key);
				v.add(va.getIndex());
				v.add(va.getAnimationId());
				v.add(va.getMatplotlibModel());
				v.add(va.getColormap());
				v.add(va.getZoom());
				v.add(va.getRange());
				v.add(va.getSmoothZones());
				v.add(va.getDisplayDate());
				v.add(va.getScale());
				dataVector.add(v);
			}
		}
		model.setDataVector(dataVector, colNamesVector);
	}
	
}

class MatplotlibAnimationTableModel extends DefaultTableModel{
	
	public Class<?> getColumnClass(int c){
		if(c==0){
			return Integer.class;
		}else if(c==1){
    			return String.class;
		}else if(c==2){
    			return MatplotlibModel.class;
	    	}else if(c==3){
	    		return MatplotlibColormap.class;
	    	}else if(c==4){
	    		return String.class;
	    	}else if(c==5){
	    		return String.class;
	    	}else if(c==6){
	    		return Boolean.class;
	    	}else if(c==7){
	    		return Boolean.class;
	    	}
	    	return Scale.class;
	}

    public boolean isCellEditable(int row, int col){
    	return false;
	}
	
}
