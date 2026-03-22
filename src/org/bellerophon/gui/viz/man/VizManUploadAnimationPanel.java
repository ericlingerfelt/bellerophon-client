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
import org.bellerophon.gui.dialog.EditUploadedAnimationDialog;
import org.bellerophon.gui.dialog.CautionDialog;
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
public class VizManUploadAnimationPanel extends JPanel implements ActionListener, MouseListener{

	private Frame frame;
	private VizManPanel parent;
	private VizManData d;
	private JButton submitButton, addButton, removeButton
					, editButton, backButton, undoButton;
	private WordWrapLabel topLabel, indexLabel, vizSetLabel;
	private UploadAnimationTable table;
	private JPanel tablePanel;
	
	/**
	 * Instantiates a new viz create animation panel.
	 *
	 * @param frame the frame
	 * @param parent the parent
	 * @param d the d
	 */
	public VizManUploadAnimationPanel(Frame frame, VizManPanel parent, VizManData d){
		
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
		
		table = new UploadAnimationTable();
		table.addMouseListener(this);
		JScrollPane tablePane = new JScrollPane(table);
		tablePanel = new JPanel();
		double[] colTable = {5, TableLayoutConstants.FILL, 5};
		double[] rowTable = {5, TableLayoutConstants.FILL, 5};
		tablePanel.setLayout(new TableLayout(colTable, rowTable));
		tablePanel.add(tablePane, "1, 1, f, f");
		tablePanel.setBorder(Borders.getBorder("Uploaded Animations"));
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
			topLabel.setText("The visualization set you have selected does not contain any uploaded animations. " +
								"Click <i>Add New Animation</i> to begin.");
		}else{
			topLabel.setText("The visualization set you are creating does not contain any uploaded animations yet. " +
								"Click <i>Add New Animation</i> to begin.");
		}
		
		indexLabel.setText("Visualization Set Index = " + String.valueOf(d.getVizSet().getIndex()));
		vizSetLabel.setText("Visualization Set Unique ID = " + d.getVizSet().toString());
		
		removeAll();
		
		if(!table.isInitialized() || initialize){
			table.setCurrentState(d.getVizSet().getUploadedAnimationMap());
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
								, 10, TableLayoutConstants.FILL};
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
		String string = "You are about to undo all changes to this visualization set's uploaded animations and " +
						"revert to its original uploaded animations.<br><br><b>Are you sure?</b>";
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
		if(d.getVizSet().getIndex()==-1){
			Create3DVizSetWorker worker = new Create3DVizSetWorker(frame, parent, d.getVizSet());
			worker.execute();
		}else{
			Mod3DVizSetWorker worker = new Mod3DVizSetWorker(frame, parent, d.getVizSet());
			worker.execute();
		}
	}
	
	/**
	 * Edits the animation.
	 */
	private void editAnimation(){
		if(table.getSelectedRowCount()>0){
			int rowIndex = table.getSelectedRow();
			Vector<Object> animationRow = EditUploadedAnimationDialog.createEditUploadedAnimationDialog(frame
																				, table.getDataVector()
																				, rowIndex
																				, EditUploadedAnimationDialog.Type.EDIT
																				, d);
			if(animationRow!=null){
				table.replaceAnimation(animationRow, rowIndex);
			}
		}else{
			MessageDialog.createMessageDialog(frame, "Please select an uploaded animation from the table to edit.", "Attention!");
		}
	}
	
	/**
	 * Adds the animation.
	 */
	private void addAnimation(){
		Vector<Object> animationRow = EditUploadedAnimationDialog.createEditUploadedAnimationDialog(frame
				, table.getDataVector()
				, -1
				, EditUploadedAnimationDialog.Type.ADD
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
			String string = "You are about to delete the selected uploaded animation.<br><br><b>Are you sure?</b>";
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
			MessageDialog.createMessageDialog(frame, "Please select an uploaded animation from the table to remove.", "Attention!");
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
		TreeMap<Integer, UploadedAnimation> map = new TreeMap<Integer, UploadedAnimation>();
		int index = 0;
		while(itr.hasNext()){
			Vector<Object> v = itr.next();
			UploadedAnimation ua = new UploadedAnimation();
			ua.setIndex((Integer)v.get(0));
			ua.setAnimationId((String)v.get(1));
			ua.setBounceTime((Double)v.get(2));
			ua.setNumFrames((Integer)v.get(3));
			ua.setUploader((User)v.get(4));
			ua.setUploadDate((Calendar)v.get(5));
			ua.setDesc((String)v.get(6));
			ua.setLocalFilepath((String)v.get(7));
			map.put(index, ua);
			index++;
		}
		d.getVizSet().setUploadedAnimationMap(map);
	}

}

class UploadAnimationTable extends DefaultTable{
	
	private Vector<String> colNamesVector;
	private UploadAnimationTableModel model;
	private boolean initialized = false;
	private ArrayList<Integer> modifiedRowList = new ArrayList<Integer>();
	private Border outside = new MatteBorder(1, 0, 1, 0, Color.RED);
	private Border inside = new EmptyBorder(0, 1, 0, 1);
	private Border highlight = new CompoundBorder(outside, inside);

	public UploadAnimationTable(){
		
		colNamesVector = new Vector<String>();
		colNamesVector.add("Index");
		colNamesVector.add("Unique ID");
		colNamesVector.add("Bounce Time (ms)");
		colNamesVector.add("Number of Frames");
		colNamesVector.add("Uploaded By");
		colNamesVector.add("Upload Date");
		colNamesVector.add("Description");
		colNamesVector.add("Local Filepath");
		
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		model = new UploadAnimationTableModel();
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
	
	public UploadAnimationTableModel getModel(){
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
		modifiedRowList.add(rowIndex);	
		model.setValueAt(v.get(0), rowIndex, 0);
		model.setValueAt(v.get(1), rowIndex, 1);
		model.setValueAt(v.get(2), rowIndex, 2);
		model.setValueAt(v.get(3), rowIndex, 3);
		model.setValueAt(v.get(4), rowIndex, 4);
		model.setValueAt(v.get(5), rowIndex, 5);
		model.setValueAt(v.get(6), rowIndex, 6);
		model.setValueAt(v.get(7), rowIndex, 7);
	}
	
	public Component prepareRenderer(TableCellRenderer renderer, int row, int column){
        Component c = super.prepareRenderer(renderer, row, column);
        JComponent jc = (JComponent)c;
        if(modifiedRowList.contains(row)){
        	jc.setBorder(highlight);
        	jc.setForeground(Color.RED);
        	if(isRowSelected(row)){
        		jc.setBackground(new Color(Color.red.getRed(), Color.red.getGreen(), Color.red.getBlue(), 50));
        	}
        }
        return c;
    }
	
	public void setCurrentState(TreeMap<Integer, UploadedAnimation> uploadedAnimationMap){
		Vector<Vector<Object>> dataVector = new Vector<Vector<Object>>();
		if(uploadedAnimationMap!=null){
			ArrayList<Integer> keyList = new ArrayList<Integer>();
			Iterator<Integer> itr = uploadedAnimationMap.keySet().iterator();
			while(itr.hasNext()){
				keyList.add(itr.next());
			}
			Collections.sort(keyList, Collections.reverseOrder());
			
			for(int key: keyList){
				Vector<Object> v = new Vector<Object>();
				UploadedAnimation ua = uploadedAnimationMap.get(key);
				v.add(ua.getIndex());
				v.add(ua.getAnimationId());
				v.add(ua.getBounceTime());
				v.add(ua.getNumFrames());
				v.add(ua.getUploader());
				v.add(ua.getUploadDate());
				v.add(ua.getDesc());
				v.add(ua.getLocalFilepath());
				dataVector.add(v);
			}
		}
		model.setDataVector(dataVector, colNamesVector);
	}
	
}

class UploadAnimationTableModel extends DefaultTableModel{
	
	public Class<?> getColumnClass(int c){
		if(c==0){
			return Integer.class;
		}else if(c==1){
    		return String.class;
		}else if(c==2){
    		return Double.class;
    	}else if(c==3){
    		return Integer.class;
    	}else if(c==4){
    		return User.class;
    	}else if(c==5){
    		return Calendar.class;
    	}else if(c==6){
    		return String.class;
    	}else{
    		return String.class;
    	}
	}

    public boolean isCellEditable(int row, int col){
    	return false;
	}
	
}
