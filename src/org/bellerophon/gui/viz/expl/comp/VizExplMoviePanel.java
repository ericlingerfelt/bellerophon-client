/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: VizExplMoviePanel.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.viz.expl.comp;

import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstants;

import java.awt.Color;
import java.awt.Frame;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;
import javax.swing.event.*;

import org.bellerophon.data.util.Animation;
import org.bellerophon.data.util.CustomFile;
import org.bellerophon.data.util.UploadedAnimation;
import org.bellerophon.data.util.MatplotlibAnimation;
import org.bellerophon.data.util.VizSet;
import org.bellerophon.enums.VizCompType;
import org.bellerophon.gui.dialog.AttentionDialog;
import org.bellerophon.gui.format.Buttons;
import org.bellerophon.gui.format.Colors;
import org.bellerophon.gui.format.Icons;
import org.bellerophon.gui.util.FramePanel;
import org.bellerophon.gui.viz.VizSetUpdateListener;
import org.bellerophon.gui.viz.expl.VizExplViewPanel;
import org.bellerophon.gui.viz.expl.listener.LoadMatplotlibAnimationFramesListener;
import org.bellerophon.gui.viz.expl.listener.LoadUploadedAnimationFramesListener;
import org.bellerophon.gui.viz.expl.listener.UpdateMatplotlibAnimationFramesListener;
import org.bellerophon.gui.viz.expl.listener.UpdateUploadedAnimationFramesListener;
import org.bellerophon.gui.viz.expl.worker.LoadUploadedAnimationFramesWorker;
import org.bellerophon.gui.viz.expl.worker.LoadMatplotlibAnimationFramesWorker;
import org.bellerophon.gui.viz.expl.worker.RecreateMatplotlibAnimationFrameWorker2;
import org.bellerophon.gui.viz.expl.worker.UpdateUploadedAnimationFramesWorker;
import org.bellerophon.gui.viz.expl.worker.UpdateMatplotlibAnimationFramesWorker;

/**
 * The Class VizExplMoviePanel.
 *
 * @author Eric J. Lingerfelt
 */
public class VizExplMoviePanel extends VizExplCompPanel implements ActionListener
															, MouseListener
															, ChangeListener
															, VizSetUpdateListener
															, LoadMatplotlibAnimationFramesListener
															, LoadUploadedAnimationFramesListener
															, UpdateMatplotlibAnimationFramesListener
															, UpdateUploadedAnimationFramesListener{

	private JButton playButton, gotoButton, recreateButton
					, seekForwardButton, seekBackwardButton
					, skipForwardButton, skipBackwardButton
					, loadingButton;
	private JCheckBox fitBox, loopBox;
	private JLabel maxFrameLabel, percentLabel, loadingFramesLabel;
	private AnimationTimer animationTimer;
	private JSlider slider;
	private FramePanel framePanel;
	private boolean isPlaying = false;
	private JTextField frameField;
	private boolean isLoading = false;
	private boolean isLoaded = false;
	private int lastFrame;
	private boolean isSliderSelected = false;
	private LoadMatplotlibAnimationFramesTimer loadMatplotlibAnimationFramesTimer;
	private LoadMatplotlibAnimationFramesWorker loadMatplotlibAnimationFramesWorker;
	
	/**
	 * Instantiates a new viz expl movie panel.
	 *
	 * @param parent the parent
	 * @param panel the panel
	 * @param vs the vs
	 * @param a the a
	 */
	public VizExplMoviePanel(Frame parent, VizExplViewPanel panel, VizSet vs, Animation a){
		
		super(parent, panel, vs, a, VizCompType.MOVIE);
		
		if(a instanceof UploadedAnimation){
			lastFrame = ((UploadedAnimation) a).getNumFrames();
		}else{
			lastFrame = vs.getLastFrame();
		}
		
		animationTimer = new AnimationTimer(50, this);
		loadMatplotlibAnimationFramesTimer = new LoadMatplotlibAnimationFramesTimer(this);
		
		frameField = new JTextField(5);
		frameField.setText(String.valueOf(lastFrame));
		
		frame = lastFrame;
		
		JLabel frameLabel = new JLabel("Current Frame Is");
		maxFrameLabel = new JLabel("Out Of " + lastFrame);
		
		percentLabel = new JLabel();
		loadingFramesLabel = new JLabel();
		
		JPanel loadingPanel = new JPanel();
		double[] colLoading = {TableLayoutConstants.PREFERRED};
		double[] rowLoading = {TableLayoutConstants.PREFERRED,
								10, TableLayoutConstants.PREFERRED};
		loadingPanel.setLayout(new TableLayout(colLoading, rowLoading));
		loadingPanel.add(percentLabel,     		"0, 0, l, c");
		loadingPanel.add(loadingFramesLabel,	"0, 2, l, c");
		
		slider = new JSlider(JSlider.HORIZONTAL);
		slider.setMaximum(lastFrame);
		slider.setValue(frame);
		slider.addChangeListener(this);
		slider.addMouseListener(this);
		
		gotoButton = Buttons.getIconButton("icons/edit-redo.png"
						, Colors.backColor
						, this
						, "Jump To Selected Frame");
		
		recreateButton = Buttons.getIconButton("icons/view-refresh.png"
						, Colors.backColor
						, this
						, "Regenerate Image");
		
		loadingButton = Buttons.getIconButton("icons/media-playback-pause.png"
						, Colors.backColor
						, this
						, "Pause Animation Loading");
		
		playButton = Buttons.getIconButton("icons/media-playback-start.png", Color.white, this, "Play");
		seekForwardButton = Buttons.getIconButton("icons/media-seek-forward.png", Color.white, null, "Seek Forward");
		seekForwardButton.addMouseListener(this);
		seekBackwardButton = Buttons.getIconButton("icons/media-seek-backward.png", Color.white, null, "Seek Backward");
		seekBackwardButton.addMouseListener(this);
		skipForwardButton = Buttons.getIconButton("icons/media-skip-forward.png", Color.white, this, "Skip Forward");
		skipBackwardButton = Buttons.getIconButton("icons/media-skip-backward.png", Color.white, this, "Skip Backward");
	
		fitBox = new JCheckBox("Fit to Window", true);
		fitBox.addActionListener(this);
		
		loopBox = new JCheckBox("Loop Playback", false);
		
		framePanel = new FramePanel();
		JScrollPane framePanelPane = new JScrollPane(framePanel);
		framePanel.setScrollPane(framePanelPane);
		framePanel.setFitToWindow(true);
		
		JPanel buttonPanel = new JPanel();
		double[] colButton = {TableLayoutConstants.PREFERRED
				, 10, TableLayoutConstants.PREFERRED
				, 10, TableLayoutConstants.PREFERRED
				, 10, TableLayoutConstants.PREFERRED
				, 10, TableLayoutConstants.PREFERRED};
		double[] rowButton = {TableLayoutConstants.PREFERRED};
		buttonPanel.setLayout(new TableLayout(colButton, rowButton));
		buttonPanel.add(skipBackwardButton,     "0, 0, c, c");
		buttonPanel.add(seekBackwardButton,     "2, 0, c, c");
		buttonPanel.add(playButton,             "4, 0, c, c");
		buttonPanel.add(seekForwardButton,      "6, 0, c, c");
		buttonPanel.add(skipForwardButton,      "8, 0, c, c");
		
		JPanel boxPanel = new JPanel();
		double[] colBox = {TableLayoutConstants.PREFERRED};
		double[] rowBox = {TableLayoutConstants.PREFERRED
							, 10, TableLayoutConstants.PREFERRED};
		boxPanel.setLayout(new TableLayout(colBox, rowBox));
		boxPanel.add(fitBox,     "0, 0, l, c");
		boxPanel.add(loopBox,    "0, 2, l, c");
		
		JPanel buttonPanel2 = new JPanel();
		
		if(a instanceof MatplotlibAnimation){

			double[] colButton2 = {TableLayoutConstants.PREFERRED
					, 10, TableLayoutConstants.PREFERRED
					, 10, TableLayoutConstants.PREFERRED};
			double[] rowButton2 = {TableLayoutConstants.PREFERRED};
			buttonPanel2.setLayout(new TableLayout(colButton2, rowButton2));
			buttonPanel2.add(gotoButton,     	"0, 0, c, c");
			buttonPanel2.add(recreateButton, 	"2, 0, c, c");
			buttonPanel2.add(loadingButton,  	"4, 0, c, c");
			
		}
		
		JPanel inputPanel = new JPanel();
		double[] colInput = {TableLayoutConstants.PREFERRED
								, 5, TableLayoutConstants.FILL
								, 5, TableLayoutConstants.PREFERRED};
		double[] rowInput = {TableLayoutConstants.PREFERRED
								, 5, TableLayoutConstants.PREFERRED};
		inputPanel.setLayout(new TableLayout(colInput, rowInput));
		inputPanel.add(frameLabel,       "0, 0, r, c");
		inputPanel.add(frameField,       "2, 0, c, c");
		inputPanel.add(maxFrameLabel,    "4, 0, l, c");
		inputPanel.add(slider,           "0, 2, 4, 2, f, c");
		
		double[] col = {5, TableLayoutConstants.FILL
						, 10, TableLayoutConstants.PREFERRED
						, 20, TableLayoutConstants.PREFERRED, 5};
		double[] row = {5, TableLayoutConstants.FILL
						, 5, TableLayoutConstants.PREFERRED
						, 5, TableLayoutConstants.PREFERRED, 5};
		setLayout(new TableLayout(col, row));
		add(framePanelPane, "1, 1, 5, 1, f, f");
		add(inputPanel,		"1, 3, c, c");
		add(buttonPanel, 	"1, 5, c, c");
		add(boxPanel,   	"3, 3, l, c");
		add(loadingPanel,   "5, 3, l, c");
		add(buttonPanel2,   "5, 5, l, c");

	}
	
	public void updateMaximumFrame(){
		
		int maxFrame = a.getFrameMap().lastKey();
		
		maxFrameLabel.setText("Out Of " + lastFrame);
		loadingFramesLabel.setText("Frame " + maxFrame + " Now Loading");
		int percentLoaded = (int)((double)maxFrame/(double)lastFrame*100.0);
		percentLabel.setText("Loading is " + percentLoaded + "% Complete");
		
		slider.removeChangeListener(this);
		slider.setMaximum(maxFrame);
		slider.addChangeListener(this);
		
		setCurrentFrame();
		
	}
	
	public void updateMinimumFrame(){
		
		int minFrame = a.getFrameMap().firstKey();
		
		int temp = lastFrame - minFrame + 1;
		loadingFramesLabel.setText("Frame " + minFrame + " Now Loading");
		int percentLoaded = (int)((double)temp/(double)lastFrame*100.0);
		percentLabel.setText("Loading is " + percentLoaded + "% Complete");
		
		slider.removeChangeListener(this);
		slider.setMinimum(minFrame);
		slider.addChangeListener(this);
		
		setCurrentFrame();
		
	}
	
	public void actionPerformed(ActionEvent ae){
		if(ae.getSource()==animationTimer){
			if(animationTimer.getDirection()==AnimationTimer.Direction.FORWARD){
				frame+=1;
				setCurrentFrame();
			}else{
				frame-=1;
				setCurrentFrame();
			}
		}else if(ae.getSource()==playButton){
			if(isPlaying){
				pauseMovie();
			}else{
				playMovie();
			}
		}else if(ae.getSource()==skipBackwardButton){
			pauseMovie();
			frame-=1;
			setCurrentFrame();
		}else if(ae.getSource()==skipForwardButton){
			pauseMovie();
			frame+=1;
			setCurrentFrame();
		}else if(ae.getSource()==fitBox){
			framePanel.setFitToWindow(fitBox.isSelected());
		}else if(ae.getSource()==gotoButton){
			if(goodData()){
				frame = Integer.valueOf(frameField.getText().trim());
				setCurrentFrame();	
			}else{
				frameField.setText(String.valueOf(frame));
			}
		}else if(ae.getSource()==recreateButton){
			pauseMovie();
			if(goodData()){
				frame = Integer.valueOf(frameField.getText().trim());
				slider.setValue(frame);
				a.setCurrentFrame(frame);
				setSelectedFrame();	
				RecreateMatplotlibAnimationFrameWorker2 worker = new RecreateMatplotlibAnimationFrameWorker2(parent, this, vs, (MatplotlibAnimation) a, a.getCurrentFrame());
				worker.execute();
			}
		}else if(ae.getSource()==loadingButton){
			pauseMovie();
			if(!isLoaded){
				if(isLoading){
					loadingButton.setIcon(Icons.createImageIcon("/resources/images/icons/media-playback-start.png"));
					loadingButton.setToolTipText("Restart Animation Loading");
					stopLoading();
				}else{
					loadingButton.setIcon(Icons.createImageIcon("/resources/images/icons/media-playback-pause.png"));
					loadingButton.setToolTipText("Pause Animation Loading");
					restartLoading();
				}
			}
		}
	}
	
	public void setCurrentFrame(){
		if(frame >= slider.getMinimum() 
				&& frame <= slider.getMaximum()){
			setSelectedFrame();
			frameField.setText(String.valueOf(frame));
			slider.removeChangeListener(this);
			slider.setValue(frame);
			slider.addChangeListener(this);
			frameFile = a.getFrameMap().get(frame);
			try {
				framePanel.setFile(frameFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(frame == slider.getMaximum()){
				if(!loopBox.isSelected()){
					pauseMovie();
				}else if(isPlaying && !isSliderSelected){
					frame = slider.getMinimum();
					setCurrentFrame();
				}
			}
		}
	}

	public void replaceFrameFile(CustomFile file, int frame) {
		a.getFrameMap().put(frame, file);
		setCurrentFrame();
	}
	
	public void updateState() {
		if(!isLoading){
			if(a instanceof UploadedAnimation){
				lastFrame = ((UploadedAnimation) a).getNumFrames();
			}else{
				lastFrame = vs.getLastFrame();
			}
			if(lastFrame!=slider.getMaximum()){
				if(a instanceof MatplotlibAnimation){
					UpdateMatplotlibAnimationFramesWorker worker = new UpdateMatplotlibAnimationFramesWorker(parent, vs, (MatplotlibAnimation) a, slider.getMaximum(), this);
					worker.execute();
				}else if(a instanceof UploadedAnimation){
					UpdateUploadedAnimationFramesWorker worker = new UpdateUploadedAnimationFramesWorker(parent, vs, (UploadedAnimation) a, slider.getMaximum(), this);
					worker.execute();
				}	
			}
		}
	}

	public void matplotlibAnimationFramesLoaded(){
		updateMinimumFrame();
		loadMatplotlibAnimationFramesTimer.stop();
		isLoading = false;
		isLoaded = true;
		loadingButton.setEnabled(false);
		loadingFramesLabel.setText("All Frames Are Loaded");
		percentLabel.setText("Loading is 100% Complete");
	}

	public void uploadedAnimationFramesLoaded(){
		loadMatplotlibAnimationFramesTimer.stop();
		isLoading = false;
		isLoaded = true;
		loadingButton.setEnabled(false);
		loadingFramesLabel.setText("All Frames Are Loaded");
		percentLabel.setText("Loading is 100% Complete");
	}
	
	public void uploadedAnimationFramesUpdated(){
		isLoading = false;
		isLoaded = true;
	}

	public void matplotlibAnimationFramesUpdated(){
		isLoading = false;
		isLoaded = true;
	}
	
	private void playMovie(){
		isPlaying = true;
		playButton.setIcon(Icons.createImageIcon("/resources/images/icons/media-playback-pause.png"));
		playButton.setToolTipText("Pause");
		animationTimer.stop();
		animationTimer.setDelay(50);
		animationTimer.setDirection(AnimationTimer.Direction.FORWARD);
		animationTimer.start();
	}
	
	public void pauseMovie(){
		isPlaying = false;
		playButton.setIcon(Icons.createImageIcon("/resources/images/icons/media-playback-start.png"));
		playButton.setToolTipText("Play");
		animationTimer.stop();
	}
	
	public void loadFrames(){
		if(!isLoaded){
			if(a.getFrameMap()==null && !isLoading){
				percentLabel.setText("Please wait");
				loadingFramesLabel.setText("while initializing.");
				startLoading();
			}else{
				restartLoading();
			}
		}
	}
	
	public void startLoading(){
		isLoading = true;
		loadingButton.setEnabled(true);
		if(a instanceof MatplotlibAnimation){
			loadMatplotlibAnimationFramesTimer.start();
			loadMatplotlibAnimationFramesWorker = new LoadMatplotlibAnimationFramesWorker(parent, vs, (MatplotlibAnimation) a, vs.getLastFrame(), this);
			loadMatplotlibAnimationFramesWorker.execute();
		}else if(a instanceof UploadedAnimation){
			LoadUploadedAnimationFramesWorker worker = new LoadUploadedAnimationFramesWorker(parent, vs, (UploadedAnimation) a, this);
			worker.execute();
		}	
	}
	
	public void restartLoading(){
		isLoading = true;
		loadMatplotlibAnimationFramesTimer.restart();
		loadMatplotlibAnimationFramesWorker = new LoadMatplotlibAnimationFramesWorker(parent, vs, (MatplotlibAnimation) a, a.getFrameMap().firstKey(), this);
		loadMatplotlibAnimationFramesWorker.execute();
	}
	
	public void stopLoading(){
		isLoading = false;
		loadMatplotlibAnimationFramesTimer.stop();
		loadMatplotlibAnimationFramesWorker.cancel();
		loadingFramesLabel.setText("Frame Loading Paused");
	}
	
	public void stateChanged(ChangeEvent ce){
		if(ce.getSource()==slider){
			frame = slider.getValue();
			setCurrentFrame();
		}
	}
	
	public void mouseClicked(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mousePressed(MouseEvent me){
		if(me.getSource()==seekBackwardButton){
			animationTimer.stop();
			animationTimer.setDelay(50);
			animationTimer.setDirection(AnimationTimer.Direction.BACKWARD);
			animationTimer.start();
		}else if(me.getSource()==seekForwardButton){
			animationTimer.stop();
			animationTimer.setDelay(50);
			animationTimer.setDirection(AnimationTimer.Direction.FORWARD);
			animationTimer.start();
		}else if(me.getSource()==slider){
			isSliderSelected = true;
		}
	}
	public void mouseReleased(MouseEvent me){
		if(me.getSource()==seekForwardButton
				|| me.getSource()==seekBackwardButton){
			animationTimer.stop();
			animationTimer.setDelay(50);
			animationTimer.setDirection(AnimationTimer.Direction.FORWARD);
			if(isPlaying){
				animationTimer.start();
			}
		}else if(me.getSource()==slider){
			isSliderSelected = false;
		}
	}
	
	private boolean goodData(){
		try{
			if(frameField.getText().trim().equals("")){
				AttentionDialog.createDialog(parent, "Please enter an integer value between 1 and " 
													+ lastFrame
													+ " for <i>Selected Frame</i>.");
				return false;
			}
			int frame = Integer.valueOf(frameField.getText().trim());
			if(frame<1 || frame>lastFrame){
				AttentionDialog.createDialog(parent, "Please enter an integer value between 1 and " 
													+ lastFrame
													+ " for <i>Selected Frame</i>.");
				return false;
			}
		}catch(NumberFormatException nfe){
			AttentionDialog.createDialog(parent, "Please enter an integer value between 1 and " 
												+ lastFrame
												+ " for <i>Selected Frame</i>.");
			return false;
		}
		return true;
	}
	
}

class LoadMatplotlibAnimationFramesTimer implements ActionListener{
	
	private Timer timer;
	private VizExplMoviePanel panel;
	
	public LoadMatplotlibAnimationFramesTimer(VizExplMoviePanel panel){
		
		this.panel = panel;
		timer = new Timer(0, this);
		timer.setInitialDelay(1000);
		timer.setDelay(100);
		
	}
	
	public void stop(){
		timer.stop();
	}
	
	public void start(){
		timer.start();
	}
	
	public void restart(){
		timer.setInitialDelay(100);
		timer.restart();
	}
	
	public void actionPerformed(ActionEvent ae){
		try{
			panel.updateMinimumFrame();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}

class AnimationTimer extends Timer{
	
	public enum Direction {FORWARD, BACKWARD}
	private Direction direction;
	
	public AnimationTimer(int delay, ActionListener al){
		super(delay, al);
	}
	
	public void setDirection(Direction direction){
		this.direction = direction;
	}
	
	public Direction getDirection(){
		return direction;
	}
	
}
