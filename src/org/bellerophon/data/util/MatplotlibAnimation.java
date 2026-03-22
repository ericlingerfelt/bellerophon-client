/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: MatplotlibAnimation.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.data.util;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.TreeMap;

import org.bellerophon.enums.*;
import org.bellerophon.gui.format.Calendars;
import org.bellerophon.gui.viz.expl.comp.VizExplCompPanel;

/**
 * The Class Animation is the data structure for a matplotlib animation.
 *
 * @author Eric J. Lingerfelt
 */
public class MatplotlibAnimation extends Animation{

	private boolean smoothZones, displayDate, exportAllFramesForPython;
	private String zoom, range;
	private MatplotlibColormap colormap;
	private Scale scale;
	private MatplotlibModel model;
	private CustomFile pythonfile;
	
	/**
	 * The Constructor.
	 */
	public MatplotlibAnimation(){
		initialize();
	}
	
	/**
	 * Gets the web service com value.
	 *
	 * @return the web service com value
	 */
	public String getWebServiceComValue(){
		String s = "";
		s += index + ":";
		s += model.getIndex() + ":";
		s += colormap.getIndex() + ":";
		s += zoom + ":";
		s += range + ":";
		s += (smoothZones ? "1" : "0") + ":";
		s += animationId + ":";
		s += scale.name().toLowerCase() + ":";
		s += (displayDate ? "1" : "0");
		return s;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		String string = "";
		string += model;
		string += " : ";
		string += Math.abs(Integer.valueOf(zoom.split(",")[0])) + "km";
		if(smoothZones){
			string += " : smoothed";
		}
		if(!animationId.equals("")){
			string += " : " + animationId;
		}
		string += "";
		return string;
	}
	
	/**
	 * To string tar filename.
	 *
	 * @return the string
	 */
	public String toStringTarFilename(){
		String string = "";
		string += parentIndex + "_";
		string += index + "_";
		string += model.toString().replace(" ", "_") + "_";
		string += Math.abs(Integer.valueOf(zoom.split(",")[0])) + "km";
		if(smoothZones){
			string += "_smoothed";
		}
		if(!animationId.equals("")){
			string += "_" + animationId;
		}
		string += "_" + Calendars.getTimestampStringShort(Calendar.getInstance()) + ".tgz";
		return string;
	}
	
	/**
	 * To string movie filename.
	 *
	 * @return the string
	 */
	public String toStringMovieFilename(){
		String string = "";
		string += parentIndex + "_";
		string += index + "_";
		string += model + "_";
		string += Math.abs(Integer.valueOf(zoom.split(",")[0])) + "km";
		if(smoothZones){
			string += "_smoothed";
		}
		if(!animationId.equals("")){
			string += "_" + animationId;
		}
		string += ".mp4";
		return string;
	}
	
	/**
	 * To string frame filename.
	 *
	 * @param frame the frame
	 * @return the string
	 */
	public String toStringFrameFilename(int frame){
		String string = "";
		string += parentIndex + "_";
		string += index + "_";
		string += model + "_";
		string += Math.abs(Integer.valueOf(zoom.split(",")[0])) + "km";
		if(smoothZones){
			string += "_smoothed";
		}
		if(!animationId.equals("")){
			string += "_" + animationId;
		}
		string += "_" + new DecimalFormat("00000").format(frame) + ".png";
		return string;
	}
	
	public String toStringDataFilename(int frame){
		String string = "";
		string += parentIndex + "_";
		string += new DecimalFormat("00000").format(frame) + ".h5";
		return string;
	}
	
	public String toStringPythonFilename(int frame){
		String string = "";
		string += parentIndex + "_";
		string += index + "_";
		string += model + "_";
		string += Math.abs(Integer.valueOf(zoom.split(",")[0])) + "km";
		if(smoothZones){
			string += "_smoothed";
		}
		if(!animationId.equals("")){
			string += "_" + animationId;
		}
		string += "_" + new DecimalFormat("00000").format(frame) + ".py";
		return string;
	}
	
	public String toStringPythonFilename(){
		String string = "";
		string += parentIndex + "_";
		string += index + "_";
		string += model + "_";
		string += Math.abs(Integer.valueOf(zoom.split(",")[0])) + "km";
		if(smoothZones){
			string += "_smoothed";
		}
		if(!animationId.equals("")){
			string += "_" + animationId;
		}
		string += ".py";
		return string;
	}
	
	public String toStringPreviewPythonFilename(int frame){
		String string = "";
		string += parentIndex + "_";
		string += model + "_";
		string += Math.abs(Integer.valueOf(zoom.split(",")[0])) + "km";
		if(smoothZones){
			string += "_smoothed";
		}
		string += "_" + new DecimalFormat("00000").format(frame) + ".py";
		return string;
	}
	
	/**
	 * To string info.
	 *
	 * @param showFullReport the show full report
	 * @return the string
	 */
	public String toStringInfo(boolean showFullReport){
		String s = "";
		s += "\nAnimation Information\n";
		if(showFullReport){
			s += "Animation Index = " + index + "\n";
			if(!animationId.equals("")){
				s += "Animation Unique ID = " + animationId + "\n";
			}
			s += "Model = " + model + "\n";
			s += "Colortable = " + colormap + "\n";
			s += "Zoom [km] = " + zoom + "\n";
			if(!range.equals("")){
				s += "Colortable Range = " + range + "\n";
			}
			s += "Smooth Zones = " + (smoothZones ? "Yes" : "No") + "\n";
			s += "Scale = "            + scale + "\n";
			s += "Display Date = " + (displayDate ? "Yes" : "No") + "\n";
		}else{
			if(!animationId.equals("")){
				s += "Animation Unique ID = " + animationId + "\n";
			}
			s += "Model = " + model + "\n";
			s += "Zoom [km] = " + zoom + "\n";
			s += "Smooth Zones = " + (smoothZones ? "Yes" : "No") + "\n";
			s += "Scale = "            + scale + "\n";
		}
		return s;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public MatplotlibAnimation clone(){
		MatplotlibAnimation va = new MatplotlibAnimation();
		va.index = index;
		va.panelMap = panelMap;
		va.parentIndex = parentIndex;
		va.animationId = animationId;
		va.smoothZones = smoothZones;
		va.displayDate = displayDate;
		va.currentFrame = currentFrame;
		va.model = model;
		va.zoom = zoom;
		va.colormap = colormap;
		va.range = range;
		va.scale = scale;
		va.creationDate = (Calendar)creationDate.clone();
		va.modDate = (Calendar)modDate.clone();
		//va.frameMap = cloneFrameMap();
		va.panelMap = clonePanelMap();
		//va.moviefile = moviefile.clone();
		va.hot = hot;
		//va.framefile = framefile.clone();
		//va.tarfile = tarfile.clone();
		va.metadata = metadata;
		return va;
	}
	
	/**
	 * Clone panel map.
	 *
	 * @return the tree map
	 */
	private TreeMap<VizCompType, VizExplCompPanel> clonePanelMap(){
		TreeMap<VizCompType, VizExplCompPanel> map = new TreeMap<VizCompType, VizExplCompPanel>();
		Iterator<VizCompType> itr = panelMap.keySet().iterator();
		while(itr.hasNext()){
			VizCompType type = itr.next();
			map.put(type, panelMap.get(type));
		}
		return map;
	}
	
	/**
	 * Clone frame map.
	 *
	 * @return the tree map
	 */
	private TreeMap<Integer, CustomFile> cloneFrameMap(){
		TreeMap<Integer, CustomFile> map = new TreeMap<Integer, CustomFile>();
		Iterator<Integer> itr = frameMap.keySet().iterator();
		while(itr.hasNext()){
			Integer image = itr.next();
			map.put(image, frameMap.get(image).clone());
		}
		return map;
	}
	
	/* (non-Javadoc)
	 * @see org.bellerophon.data.Data#initialize()
	 */
	public void initialize(){
		super.initialize();
		model = null;
		zoom = "";
		colormap = null;
		smoothZones = false;
		displayDate = false;
		exportAllFramesForPython = false;
		range = "";
		scale = null;
		pythonfile = null;
		panelMap.put(VizCompType.BROWSE, null);
		panelMap.put(VizCompType.CUSTOM, null);
		panelMap.put(VizCompType.MOVIE, null);
	}
	
	public boolean getExportAllFramesForPython(){return exportAllFramesForPython;}
	public void setExportAllFramesForPython(boolean exportAllFramesForPython){this.exportAllFramesForPython = exportAllFramesForPython;}
	
	public CustomFile getPythonfile(){return pythonfile;}
	public void setPythonfile(CustomFile pythonfile){this.pythonfile = pythonfile;}
	
	/**
	 * Gets the model.
	 *
	 * @return the model
	 */
	public MatplotlibModel getMatplotlibModel(){return model;}
	
	/**
	 * Sets the model.
	 *
	 * @param model the new model
	 */
	public void setMatplotlibModel(MatplotlibModel model){this.model = model;}
	
	/**
	 * Gets the zoom.
	 *
	 * @return the zoom
	 */
	public String getZoom(){return zoom;}
	
	/**
	 * Sets the zoom.
	 *
	 * @param zoom the new zoom
	 */
	public void setZoom(String zoom){this.zoom = zoom;}
	
	/**
	 * Gets the color table.
	 *
	 * @return the color table
	 */
	public MatplotlibColormap getColormap(){return colormap;}
	
	/**
	 * Sets the color table.
	 *
	 * @param colorTable the new color table
	 */
	public void setColormap(MatplotlibColormap colormap){this.colormap = colormap;}
	
	/**
	 * Gets the range.
	 *
	 * @return the range
	 */
	public String getRange(){return range;}
	
	/**
	 * Sets the range.
	 *
	 * @param range the new range
	 */
	public void setRange(String range){this.range = range;}
	
	/**
	 * Gets the smooth zones.
	 *
	 * @return the smooth zones
	 */
	public boolean getSmoothZones(){return smoothZones;}
	
	/**
	 * Sets the smooth zones.
	 *
	 * @param smoothZones the new smooth zones
	 */
	public void setSmoothZones(boolean smoothZones){this.smoothZones = smoothZones;}
	
	/**
	 * Gets the scale.
	 *
	 * @return the scale
	 */
	public Scale getScale(){return scale;}
	
	/**
	 * Sets the scale.
	 *
	 * @param scale the new scale
	 */
	public void setScale(Scale scale){this.scale = scale;}
	
	/**
	 * Gets the display date.
	 *
	 * @return the display date
	 */
	public boolean getDisplayDate(){return displayDate;}
	
	/**
	 * Sets the display date.
	 *
	 * @param displayDate the new display date
	 */
	public void setDisplayDate(boolean displayDate){this.displayDate = displayDate;}
	
}
