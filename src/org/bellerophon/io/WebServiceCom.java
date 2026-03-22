/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: WebServiceCom.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.io;

import java.awt.Frame;
import java.io.*;
import java.net.*;
import java.text.DecimalFormat;
import java.util.*;

import javax.net.ssl.HttpsURLConnection;

import org.bellerophon.data.*;
import org.bellerophon.data.feature.*;
import org.bellerophon.data.util.*;
import org.bellerophon.enums.*;
import org.bellerophon.gui.format.Calendars;


/**
 * The Class WebServiceCom.
 *
 * @author Eric J. Lingerfelt
 */
public class WebServiceCom {

	private String action, username, password, id, stack_trace
					, regression_test_index, rev_min, rev_max, path
					, date_min, date_max, revision, code_type
					, progenitor, notes, rad_resolution, lat_resolution, long_resolution
					, matplotlib_animations, viz_set_id, viz_set_index
					, viz_set_indices, matplotlib_animation_index, matplotlib_animation
					, current_frame, animation_metadata
					, uploaded_animation_index, uploaded_animations
					, get_all_data, new_last_frame, export_all_frames_for_python;

	private final String actionString = "ACTION";
	private final String usernameString = "USERNAME";
	private final String passwordString = "PASSWORD";
	private final String idString = "ID";
	private final String stack_traceString = "STACK_TRACE";
	private final String regression_test_indexString = "REGRESSION_TEST_INDEX";
	private final String rev_minString = "REV_MIN";
	private final String rev_maxString = "REV_MAX";
	private final String date_minString = "DATE_MIN";
	private final String date_maxString = "DATE_MAX";
	private final String pathString = "PATH";
	private final String revisionString = "REVISION";
	private final String code_typeString = "CODE_TYPE";
	private final String progenitorString = "PROGENITOR";
	private final String rad_resolutionString = "RAD_RESOLUTION";
	private final String lat_resolutionString = "LAT_RESOLUTION";
	private final String long_resolutionString = "LONG_RESOLUTION";
	private final String notesString = "NOTES";
	private final String matplotlib_animationsString = "MATPLOTLIB_ANIMATIONS";
	private final String viz_set_idString = "VIZ_SET_ID";
	private final String viz_set_indexString = "VIZ_SET_INDEX";
	private final String matplotlib_animation_indexString = "MATPLOTLIB_ANIMATION_INDEX";
	private final String uploaded_animation_indexString = "UPLOADED_ANIMATION_INDEX";
	private final String uploaded_animationsString = "UPLOADED_ANIMATIONS";
	private final String viz_set_indicesString = "VIZ_SET_INDICES";
	private final String matplotlib_animationString = "MATPLOTLIB_ANIMATION";
	private final String current_frameString = "CURRENT_FRAME";
	private final String animation_metadataString = "ANIMATION_METADATA";
	private final String get_all_dataString = "GET_ALL_DATA";
	private final String new_last_frameString = "NEW_LAST_FRAME";
	private final String export_all_frames_for_pythonString = "EXPORT_ALL_FRAMES_FOR_PYTHON";
	private int exceptionCounter, totalBytesRead, totalBytesWritten;
	private WebServiceComParser parser = new WebServiceComParser();
	public final static String NO_ERROR = "NO_ERROR";
	
	/**
	 * Gets the single instance of WebServiceCom.
	 *
	 * @return single instance of WebServiceCom
	 */
	public static WebServiceCom getInstance(){
		return new WebServiceCom();
	}
	
	/**
	 * Initialize.
	 */
	private void initialize(){
		action = "";
		username = "";
		password = "";
		id = "";
		stack_trace = "";
		regression_test_index = "";
		rev_min = "";
		rev_max = "";
		date_min = "";
		date_max = "";
		path = "";
		revision = "";
		code_type = "";
		progenitor = "";
		rad_resolution = "";
		lat_resolution = "";
		long_resolution = "";
		notes = "";
		matplotlib_animations = "";
		viz_set_id = "";
		viz_set_index = "";
		matplotlib_animation_index = "";
		uploaded_animation_index = "";
		uploaded_animations = "";
		viz_set_indices = "";
		matplotlib_animation = "";
		current_frame = "";
		animation_metadata = "";
		get_all_data = "";
		new_last_frame = "";
		export_all_frames_for_python = "";
		exceptionCounter = 0;
		totalBytesRead = 0;
		totalBytesWritten = 0;
	}
	
	/**
	 * Gets the total bytes read.
	 *
	 * @return the total bytes read
	 */
	public int getTotalBytesRead(){
		return totalBytesRead;
	}
	
	/**
	 * Gets the total bytes written.
	 *
	 * @return the total bytes written
	 */
	public int getTotalBytesWritten(){
		return totalBytesWritten;
	}
	
	/**
	 * Do web service com call.
	 *
	 * @param d the d
	 * @param action the action
	 * @return the error result
	 */
	public ErrorResult doWebServiceComCall(Data d, Action action){
		return doWebServiceComCall(d, action, "", null);
	}
	
	/**
	 * Do web service com call.
	 *
	 * @param d the d
	 * @param action the action
	 * @param filepath the filepath
	 * @return the error result
	 */
	public ErrorResult doWebServiceComCall(Data d, Action action, String filepath){
		return doWebServiceComCall(d, action, filepath, null);
	}
	
	/**
	 * Do web service com call.
	 *
	 * @param d the d
	 * @param action the action
	 * @param frame the frame
	 * @return the error result
	 */
	public ErrorResult doWebServiceComCall(Data d, Action action, Frame frame){
		return doWebServiceComCall(d, action, "", frame);
	}
	
	/**
	 * Do web service com call.
	 *
	 * @param d the d
	 * @param action the action
	 * @param filepath the filepath
	 * @param frame the frame
	 * @return the error result
	 */
	public ErrorResult doWebServiceComCall(Data d, Action action, String filepath, Frame frame){
		initialize();
		HashMap<String, String> map = getWebServiceComSubmitPropertyMap(action, d);
		String outputString = getOutputString(map);
		String inputString = transmitWebServiceComString(outputString, filepath);
		if(MainData.isDebug()){
			printExchange(outputString, inputString);
		}
		ErrorResult result = new ErrorResult();
		ArrayList<WebServiceComInputProperty> inputList = getInputList(inputString);
		if(inputList.size()==0){
			return result;
		}
		if(inputList.get(0).getProperty().equals("ERROR")){
			result.setError(true);
			result.setString(inputList.get(0).getValue());
			return result;
		}
		parser.parse(action, d, inputList);
		return result;
	}
	
	/**
	 * Prints the exchange.
	 *
	 * @param out the out
	 * @param in the in
	 */
	private void printExchange(String out, String in){
		try{
			System.out.println(URLDecoder.decode(out.trim(), "UTF-8"));
			if(!in.trim().equals("")){
				System.out.println(in.trim());
			}
			System.out.println("");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the file header.
	 *
	 * @param filename the filename
	 * @return the file header
	 */
	private String getFileHeader(String filename)
    {
        return "--" 
                + id
                + "\r\nContent-Disposition: form-data; name=\"userfile\"; filename=\"" 
                + filename
                + "\"\r\nContent-type: binary\r\n\r\n";
    }
	
	/**
	 * Write file.
	 *
	 * @param os the os
	 * @param filepath the filepath
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void writeFile(OutputStream os, String filepath) throws IOException
    {
        int buffer = 1024 * 10;
        FileInputStream is = new FileInputStream(filepath);
        byte[] data = new byte[buffer];
        int bytes;
        while ((bytes = is.read(data, 0, buffer)) > 0)
        {
        	os.write(data, 0, bytes);
        	totalBytesWritten += bytes;
        }
        is.close();
    }
	
	/**
	 * Transmit web service com string.
	 *
	 * @param inputString the input string
	 * @param filepath the filepath
	 * @return the string
	 */
	private String transmitWebServiceComString(String inputString, String filepath){	
		
		String outputString = "";
		try{
			
			URL url = new URL(MainData.PHP_URL);
			HttpsURLConnection urlConnection = (HttpsURLConnection)url.openConnection();
			urlConnection.setRequestProperty("Content-type", "multipart/form-data; boundary=" + id);
			urlConnection.setDoOutput(true);
			
			String trailer = "\r\n--" + id + "--\r\n";
			OutputStream os = urlConnection.getOutputStream();
			os.write(inputString.getBytes());
			if (!filepath.equals("")){
				totalBytesWritten = 0;
				String header = getFileHeader(new File(filepath).getName());
				os.write(header.getBytes());
				writeFile(os, filepath);
			}
			os.write(trailer.getBytes());
			os.close();
			
			InputStream inputStream = urlConnection.getInputStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			IOUtilities.readStream(inputStream, baos);
			outputString = new String(baos.toByteArray());
			baos.close();
			inputStream.close();
		}catch(Exception e){
			e.printStackTrace();
			if(exceptionCounter==0){
				exceptionCounter++;
				return transmitWebServiceComString(inputString, filepath);
			}
			return "ERROR=An error has occurred connecting to our web server. "
					+ "Please check your internet connection and restart this software.";
		}
		return outputString;
	}
	
	/**
	 * Gets the web service com submit property map.
	 *
	 * @param webServiceComAction the web service com action
	 * @param d the d
	 * @return the web service com submit property map
	 */
	private HashMap<String, String> getWebServiceComSubmitPropertyMap(Action webServiceComAction, Data d){
		
		HashMap<String, String> map = new HashMap<String, String>();

		action = webServiceComAction.toString();
		id = MainData.getID();
		if(d!=null){
			if(d instanceof User){
				User u = (User)d;
				username = u.getUsername();
				password = u.getPassword();
			}
			if(d instanceof UncaughtException){
				UncaughtException ued = (UncaughtException)d;
				stack_trace = ued.getStackTrace();
			}
			if(d instanceof RegressionTest){
				RegressionTest rt = (RegressionTest)d;
				regression_test_index = String.valueOf(rt.getIndex());
			}
			if(d instanceof MatplotlibAnimation){
				MatplotlibAnimation va = (MatplotlibAnimation)d;
				matplotlib_animation_index = String.valueOf(va.getIndex());
				viz_set_index = String.valueOf(va.getParentIndex());
				current_frame = String.valueOf(va.getCurrentFrame());
				animation_metadata = va.getMetadata();
				matplotlib_animation = va.getWebServiceComValue();
				export_all_frames_for_python = va.getExportAllFramesForPython() ? "EXPORT_ALL_FRAMES_FOR_PYTHON" : "";
			}
			if(d instanceof UploadedAnimation){
				UploadedAnimation ua = (UploadedAnimation)d;
				uploaded_animation_index = String.valueOf(ua.getIndex());
				viz_set_index = String.valueOf(ua.getParentIndex());
				current_frame = String.valueOf(ua.getCurrentFrame());
				animation_metadata = ua.getMetadata();
			}
			if(d instanceof StatData){
				StatData sd = (StatData)d;
				rev_min = String.valueOf(sd.getRevMin());
				rev_max = String.valueOf(sd.getRevMax());
				if(sd.getDateMin()!=null){
					date_min = Calendars.getFormattedDateString(sd.getDateMin());
				}else{
					date_min = "-1";
				}
				if(sd.getDateMax()!=null){
					date_max = Calendars.getFormattedDateString(sd.getDateMax());
				}else{
					date_max = "-1";
				}
				if(sd.getCodeType()!=null){
					code_type = sd.getCodeType().name();
				}
			}
			if(d instanceof CustomFile){
				CustomFile cf = (CustomFile)d;
				path = String.valueOf(cf.getPath());
			}
			if(d instanceof RevisionLogEntry){
				RevisionLogEntry rle = (RevisionLogEntry)d;
				revision = String.valueOf(rle.getRevision());
				if(rle.getCodeType()!=null){
					code_type = rle.getCodeType().name();
				}
			}
			if(d instanceof VizExplData){
				VizExplData ved = (VizExplData)d;
				if(ved.getVizSetMap()!=null){
					Iterator<Integer> itr = ved.getVizSetMap().keySet().iterator();
					while(itr.hasNext()){
						viz_set_indices += itr.next();
						if(itr.hasNext()){
							viz_set_indices += ",";
						}
					}
				}
				get_all_data = ved.getGetAllData() ? "GET_ALL_DATA" : "";
			}
			if(d instanceof VizManData){
				VizManData vmd = (VizManData)d;
				viz_set_id = vmd.getVizSetId();
				if(vmd.getVizSetMap()!=null){
					Iterator<Integer> itr = vmd.getVizSetMap().keySet().iterator();
					while(itr.hasNext()){
						viz_set_indices += itr.next();
						if(itr.hasNext()){
							viz_set_indices += ",";
						}
					}
				}
				get_all_data = vmd.getGetAllData() ? "GET_ALL_DATA" : "";
			}
			if(d instanceof TestExplData){
				TestExplData ted = (TestExplData)d;
				if(ted.getTestMap()!=null){
					regression_test_index = String.valueOf(ted.getTestMap().lastKey());
				}
			}
			if(d instanceof VizSet){
				VizSet vs = (VizSet)d;
				get_all_data = "GET_ALL_DATA";
				viz_set_index = String.valueOf(vs.getIndex());
				viz_set_indices = String.valueOf(vs.getIndex());
				progenitor = vs.getProgenitor();
				if(vs.getResolution()!=null) {
					rad_resolution = String.valueOf(vs.getResolution().getRad());
					lat_resolution = String.valueOf(vs.getResolution().getLat());
					long_resolution = String.valueOf(vs.getResolution().getLon());
				}
				notes = vs.getNotes();
				viz_set_id = vs.getVizSetId();
				new_last_frame = String.valueOf(vs.getNewLastFrame());
				if(vs.getMatplotlibAnimationMap()!=null){
					matplotlib_animations = getMatplotlibAnimations(vs.getMatplotlibAnimationMap());
				}
				if(vs.getUploadedAnimationMap()!=null){
					uploaded_animations = getUploadedAnimations(vs.getUploadedAnimationMap());
				}
			}
		}
		
		map.put(actionString, action);
		if(webServiceComAction!=Action.GET_ID){
			map.put(idString, id);
		}
		
		switch(webServiceComAction){
		
			case GET_ID:
				map.put(usernameString, username);
				map.put(passwordString, password);
				break;
				
			case GET_USER_DATA:
				map.put(usernameString, username);
				break;
				
			case LOG_JAVA_EXCEPTION:
				map.put(stack_traceString, stack_trace);
				break;
				
			case CREATE_SVN_STATS:
				map.put(rev_minString, rev_min);
				map.put(rev_maxString, rev_max);
				map.put(date_minString, date_min);
				map.put(date_maxString, date_max);
				map.put(code_typeString, code_type);
				break;
				
			case GET_DIR_LISTING:
				map.put(pathString, path);
				break;
				
			case CREATE_REGRESSION_TEST_TARFILE:
				map.put(regression_test_indexString, regression_test_index);
				break;
				
			case CREATE_HDF_DUMP:
				map.put(pathString, path);
				break;
				
			case GET_DATA_FOR_REVISION:
				map.put(code_typeString, code_type);
				map.put(revisionString, revision);
				break;
			
			case GET_REGRESSION_TESTS:
				map.put(regression_test_indexString, regression_test_index);
				break;
				
			case CREATE_2D_VIZ_SET:
				map.put(progenitorString, progenitor);
				map.put(viz_set_idString, viz_set_id);
				map.put(notesString, notes);
				map.put(matplotlib_animationsString, matplotlib_animations);
				break;
				
			case CREATE_3D_VIZ_SET:
				map.put(progenitorString, progenitor);
				map.put(rad_resolutionString, rad_resolution);
				map.put(lat_resolutionString, lat_resolution);
				map.put(long_resolutionString, long_resolution);
				map.put(viz_set_idString, viz_set_id);
				map.put(notesString, notes);
				map.put(uploaded_animationsString, uploaded_animations);
				break;
				
			case MOD_2D_VIZ_SET:
				map.put(viz_set_indexString, viz_set_index);
				map.put(progenitorString, progenitor);
				map.put(viz_set_idString, viz_set_id);
				map.put(notesString, notes);
				map.put(matplotlib_animationsString, matplotlib_animations);
				break;
				
			case MOD_3D_VIZ_SET:
				map.put(viz_set_indexString, viz_set_index);
				map.put(progenitorString, progenitor);
				map.put(rad_resolutionString, rad_resolution);
				map.put(lat_resolutionString, lat_resolution);
				map.put(long_resolutionString, long_resolution);
				map.put(viz_set_idString, viz_set_id);
				map.put(notesString, notes);
				map.put(uploaded_animationsString, uploaded_animations);
				break;
				
			case DELETE_VIZ_SET:
				map.put(viz_set_indexString, viz_set_index);
				break;
				
			case VIZ_SET_ID_EXISTS:
				map.put(viz_set_idString, viz_set_id);
				break;
				
			case GET_VIZ_SETS:
				map.put(get_all_dataString, get_all_data);
				break;
				
			case GET_MATPLOTLIB_ANIMATIONS:
				map.put(viz_set_indicesString, viz_set_indices);
				map.put(get_all_dataString, get_all_data);
				break;	
				
			case GET_UPLOADED_ANIMATIONS:
				map.put(viz_set_indicesString, viz_set_indices);
				break;
				
			case GET_VIZ_JOBS:
				map.put(viz_set_indicesString, viz_set_indices);
				map.put(get_all_dataString, get_all_data);
				break;
				
			case CREATE_MATPLOTLIB_ANIMATION_MOVIEFILE:
				map.put(matplotlib_animation_indexString, matplotlib_animation_index);
				break;
			
			case CREATE_UPLOADED_ANIMATION_MOVIEFILE:
				map.put(uploaded_animation_indexString, uploaded_animation_index);
				break;
				
			case SET_HOT_MATPLOTLIB_ANIMATION:
				map.put(matplotlib_animation_indexString, matplotlib_animation_index);
				break;
				
			case SET_HOT_UPLOADED_ANIMATION:
				map.put(uploaded_animation_indexString, uploaded_animation_index);
				break;
				
			case GENERATE_MATPLOTLIB_ANIMATION_PREVIEW:
				map.put(viz_set_indexString, viz_set_index);
				map.put(matplotlib_animationString, matplotlib_animation);
				map.put(current_frameString, current_frame);
				break;

			case CREATE_MATPLOTLIB_ANIMATION_TARFILE:
				map.put(matplotlib_animation_indexString, matplotlib_animation_index);
				map.put(animation_metadataString, animation_metadata);
				break;
				
			case CREATE_UPLOADED_ANIMATION_TARFILE:
				map.put(uploaded_animation_indexString, uploaded_animation_index);
				map.put(animation_metadataString, animation_metadata);
				break;
				
			case GET_MATPLOTLIB_ANIMATION_DATAFILE_SIZE:
				map.put(matplotlib_animation_indexString, matplotlib_animation_index);
				map.put(current_frameString, current_frame);
				break;
				
			case CREATE_MATPLOTLIB_ANIMATION_PYTHONFILE:
				map.put(matplotlib_animation_indexString, matplotlib_animation_index);
				map.put(matplotlib_animationString, matplotlib_animation);
				map.put(current_frameString, current_frame);
				map.put(viz_set_indexString, viz_set_index);
				map.put(export_all_frames_for_pythonString, export_all_frames_for_python);
				break;
				
			case REWIND_VIZ_SET:
				map.put(viz_set_indexString, viz_set_index);
				map.put(new_last_frameString, new_last_frame);
				break;
				
			case RECREATE_MATPLOTLIB_ANIMATION_FRAME:
				map.put(matplotlib_animation_indexString, matplotlib_animation_index);
				map.put(current_frameString, current_frame);
				break;
				
			case RECREATE_MATPLOTLIB_ANIMATION:
				map.put(matplotlib_animation_indexString, matplotlib_animation_index);
				break;
				
			case UPLOAD_ANIMATION_FRAME:
				map.put(uploaded_animation_indexString, uploaded_animation_index);
				map.put(current_frameString, current_frame);
				break;
		
		}
		
		return map;
		
	}
	
	/**
	 * Gets the animations.
	 *
	 * @param map the map
	 * @return the animations
	 */
	private String getMatplotlibAnimations(TreeMap<Integer, MatplotlibAnimation> map){
		String s = "";
		Iterator<MatplotlibAnimation> itr = map.values().iterator();
		while(itr.hasNext()){
			MatplotlibAnimation va = itr.next();
			s += va.getWebServiceComValue();
			if(itr.hasNext()){
				s += ";";
			}
		}
		return s;
	}
	
	private String getUploadedAnimations(TreeMap<Integer, UploadedAnimation> map){
		String s = "";
		Iterator<UploadedAnimation> itr = map.values().iterator();
		while(itr.hasNext()){
			UploadedAnimation ua = itr.next();
			s += ua.getWebServiceComValue();
			if(itr.hasNext()){
				s += ";";
			}
		}
		return s;
	}
	
	/**
	 * Gets the output string.
	 *
	 * @param map the map
	 * @return the output string
	 */
	private String getOutputString(HashMap<String, String> map){
		String string = "";
		Iterator<String> itr = map.keySet().iterator();
		while(itr.hasNext()){
			String key = itr.next();
			string += "--" 
                + id
                + "\r\n" 
                + "Content-Disposition: form-data; name=" 
                + "\""
                + key
                + "\"\r\n\r\n" 
                + map.get(key)
                + "\r\n";
		}
		return string;
	}
	
	/**
	 * Gets the input list.
	 *
	 * @param string the string
	 * @return the input list
	 */
	private ArrayList<WebServiceComInputProperty> getInputList(String string){
		ArrayList<WebServiceComInputProperty> list = new ArrayList<WebServiceComInputProperty>();
		if(string.indexOf("\n")!=-1){
			String[] array = string.split("\n");
			for(String substring: array){
				if(substring.indexOf("=")!=-1){
					String[] subarray = substring.split("=", 2);
					String property = subarray[0].trim();
					String value = "";
					if(subarray.length==2){
						value = subarray[1].trim();
					}
					if(!addToInputList(list, property, value)){
						return list;
					}
				}
			}
		}else{
			if(string.indexOf("=")!=-1){
				String[] array = string.split("=");
				String property = array[0].trim();
				String value = array[1].trim();
				if(!addToInputList(list, property, value)){
					return list;
				}
			}
		}
		return list;
	}
	
	/**
	 * Adds the to input list.
	 *
	 * @param list the list
	 * @param property the property
	 * @param value the value
	 * @return true, if successful
	 */
	private boolean addToInputList(ArrayList<WebServiceComInputProperty> list, String property, String value){
		if(!property.equals("ERROR")){
			if(!value.equals("")){
				WebServiceComInputProperty prop = new WebServiceComInputProperty(property, value);
				list.add(prop);
			}
			return true;
		}
		list.clear();
		WebServiceComInputProperty prop = new WebServiceComInputProperty(property, value);
		list.add(prop);
		return false;
	}
	
}

class WebServiceComInputProperty{
	private String property;
	private String value;
	
	public WebServiceComInputProperty(String property, String value){
		this.property = property;
		this.value = value;
	}
	
	public String getProperty(){return property;}
	public String getValue(){return value;}
}

class WebServiceComParser{
	
	public void parse(Action action
							, Data d
							, ArrayList<WebServiceComInputProperty> inputList){
		
		switch(action){
			case GET_ID:
				parseGET_ID(inputList);
				break;
			case GET_USER_DATA:
				parseGET_USER_DATA(inputList, (User)d);
				break;
			case GET_REGRESSION_TESTS:
				parseGET_REGRESSION_TESTS(inputList, (TestExplData)d);
				break;
			case CREATE_SVN_STATS:
				parseCREATE_SVN_STATS(inputList, (StatData)d);
				break;
			case GET_DIR_LISTING:
				parseGET_DIR_LISTING(inputList, (CustomFile)d);
				break;
			case CREATE_REGRESSION_TEST_TARFILE:
				parseCREATE_REGRESSION_TEST_TARFILE(inputList, (RegressionTest)d);
				break;
			case CREATE_HDF_DUMP:
				parseCREATE_HDF_DUMP(inputList, (CustomFile)d);
				break;
			case GET_REVISIONS:
				parseGET_REVISIONS(inputList, (StatData)d);
				break;
			case GET_DATA_FOR_REVISION:
				parseGET_DATA_FOR_REVISION(inputList, (RevisionLogEntry)d);
				break;
			case CREATE_2D_VIZ_SET:
				parseCREATE_2D_VIZ_SET(inputList, (VizSet)d);
				break;
			case CREATE_3D_VIZ_SET:
				parseCREATE_3D_VIZ_SET(inputList, (VizSet)d);
				break;
			case MOD_3D_VIZ_SET:
				parseMOD_3D_VIZ_SET(inputList, (VizSet)d);
				break;
			case GET_VIZ_SETS:
				parseGET_VIZ_SETS(inputList, (VizData)d);
				break;
			case VIZ_SET_ID_EXISTS:
				parseVIZ_SET_ID_EXISTS(inputList, (VizManData)d);
				break;
			case GET_VIZ_JOBS:
				parseGET_VIZ_JOBS(inputList, (VizData)d);
				break;
			case GET_MATPLOTLIB_ANIMATIONS:
				if(d instanceof VizSet){
					parseGET_MATPLOTLIB_ANIMATIONS(inputList, (VizSet)d);
				}else if(d instanceof VizExplData){
					parseGET_MATPLOTLIB_ANIMATIONS(inputList, (VizExplData)d);
				}
				break;
			case GET_DEFAULT_MATPLOTLIB_ANIMATIONS:
				parseGET_DEFAULT_MATPLOTLIB_ANIMATIONS(inputList, (VizSet)d);
				break;
			case GET_UPLOADED_ANIMATIONS:
				if(d instanceof VizSet){
					parseGET_UPLOADED_ANIMATIONS(inputList, (VizSet)d);
				}else if(d instanceof VizExplData){
					parseGET_UPLOADED_ANIMATIONS(inputList, (VizExplData)d);
				}
				break;
			case GENERATE_MATPLOTLIB_ANIMATION_PREVIEW:
				parseGENERATE_MATPLOTLIB_ANIMATION_PREVIEW(inputList, (MatplotlibAnimation)d);
				break;
			case CREATE_MATPLOTLIB_ANIMATION_TARFILE:
				parseCREATE_MATPLOTLIB_ANIMATION_TARFILE(inputList, (MatplotlibAnimation)d);
				break;
			case CREATE_UPLOADED_ANIMATION_TARFILE:
				parseCREATE_UPLOADED_ANIMATION_TARFILE(inputList, (UploadedAnimation)d);
				break;
			case GET_MATPLOTLIB_ANIMATION_DATAFILE_SIZE:
				parseGET_MATPLOTLIB_ANIMATION_DATAFILE_SIZE(inputList, (MatplotlibAnimation)d);
				break;
			case CREATE_MATPLOTLIB_ANIMATION_MOVIEFILE:
				parseCREATE_MATPLOTLIB_ANIMATION_MOVIEFILE(inputList, (MatplotlibAnimation)d);
				break;
			case CREATE_UPLOADED_ANIMATION_MOVIEFILE:
				parseCREATE_UPLOADED_ANIMATION_MOVIEFILE(inputList, (UploadedAnimation)d);
				break;
			case REWIND_VIZ_SET:
				parseREWIND_VIZ_SET(inputList, (VizSet)d);
				break;
			case GET_MATPLOTLIB_COLORMAPS:
				parseGET_MATPLOTLIB_COLORMAPS(inputList);
				break;
			case GET_MATPLOTLIB_MODELS:
				parseGET_MATPLOTLIB_MODELS(inputList);
				break;
			case GET_PROGENITORS:
				parseGET_PROGENITORS(inputList);
				break;
		}
	}
	
	private void parseGET_PROGENITORS(ArrayList<WebServiceComInputProperty> inputList){
		ArrayList<String> list = new ArrayList<String>();
		for(WebServiceComInputProperty prop: inputList){
			String value = prop.getValue();
			if(prop.getProperty().equals("PROGENITOR")){
				list.add(value);
			}
		}
		Collections.sort(list);
		MainData.setProgenitorList(list);
	}
	
	private void parseGET_MATPLOTLIB_COLORMAPS(ArrayList<WebServiceComInputProperty> inputList){
		MatplotlibColormap map = null;
		for(WebServiceComInputProperty prop: inputList){
			String value = prop.getValue();
			if(prop.getProperty().equals("MATPLOTLIB_COLORMAP_INDEX")){
				map = new MatplotlibColormap();
				map.setIndex(Integer.valueOf(value));
				MainData.addMatplotlibColormap(map);
			}else if(prop.getProperty().equals("NAME")){
				map.setName(value);
			}
		}
	}
	
	private void parseGET_MATPLOTLIB_MODELS(ArrayList<WebServiceComInputProperty> inputList){
		MatplotlibModel model = null;
		for(WebServiceComInputProperty prop: inputList){
			String value = prop.getValue();
			if(prop.getProperty().equals("MATPLOTLIB_MODEL_INDEX")){
				model = new MatplotlibModel();
				model.setIndex(Integer.valueOf(value));
			}else if(prop.getProperty().equals("NAME")){
				model.setName(value);
				MainData.addMatplotlibModel(model);
			}else if(prop.getProperty().equals("PATH")){
				model.setPath(value);
			}
		}
	}
	
	private void parseGET_ID(ArrayList<WebServiceComInputProperty> inputList){
		for(WebServiceComInputProperty prop: inputList){
			String value = prop.getValue();
			if(prop.getProperty().equals("ID")){
				MainData.setID(value);
			}
		}
	}
	
	private void parseGET_USER_DATA(ArrayList<WebServiceComInputProperty> inputList, User u){
		for(WebServiceComInputProperty prop: inputList){
			String value = prop.getValue();
			if(prop.getProperty().equals("EMAIL")){
				u.setEmail(value);
			}else if(prop.getProperty().equals("FIRST_NAME")){
				u.setFirstName(value);
			}else if(prop.getProperty().equals("LAST_NAME")){
				u.setLastName(value);
			}
		}
	}
	
	private void parseGET_REGRESSION_TESTS(ArrayList<WebServiceComInputProperty> inputList, TestExplData ted){
		TreeMap<Integer, RegressionTest> map = new TreeMap<Integer, RegressionTest>();
		RegressionTest rt = null;
		for(WebServiceComInputProperty prop: inputList){
			String value = prop.getValue();
			if(prop.getProperty().equals("REGRESSION_TEST_INDEX")){
				rt = new RegressionTest();
				rt.setIndex(Integer.parseInt(value));
				CustomFile dir = new CustomFile();
				dir.setPath(MainData.TEST_URL + "/" + String.valueOf(rt.getIndex()));
				dir.setName(String.valueOf(rt.getIndex()));
				dir.setDir(true);
				rt.setDir(dir);
				map.put(rt.getIndex(), rt);
			}else if(prop.getProperty().equals("PLATFORM")){
				rt.setPlatform(Platform.valueOf(value.toUpperCase()));
			}else if(prop.getProperty().equals("CODE_TYPE")){
				if(value.equals("unknown")){
					rt.setCodeType(CodeType.valueOf(value.toUpperCase()));
				}else{
					rt.setCodeType(CodeType.valueOf("CHIMERA" + value.toUpperCase()));
				}
			}else if(prop.getProperty().equals("REVISION")){
				rt.setRevision(Integer.parseInt(value));
			}else if(prop.getProperty().equals("CHECKOUT_DATE")){
				rt.setCheckoutDate(Calendars.getCalendar(value));
				//rt.setDate(Calendars.getDateFromCalendar(Calendars.getCalendar(value)));
			}else if(prop.getProperty().equals("COMP_DATE")){
				rt.setCompDate(Calendars.getCalendar(value));
			}else if(prop.getProperty().equals("COMP_STATUS")){
				rt.setCompStatus(Integer.parseInt(value));
			}else if(prop.getProperty().equals("EXEC_DATE")){
				rt.setExecDate(Calendars.getCalendar(value));
			}else if(prop.getProperty().equals("EXEC_STATUS")){
				rt.setExecStatus(Integer.parseInt(value));
			}else if(prop.getProperty().equals("END_DATE")){
				rt.setEndDate(Calendars.getCalendar(value));
			}
			
		}
		Iterator<RegressionTest> itr = map.values().iterator();
		while(itr.hasNext()){
			itr.next().calcResult();
		}
		if(ted.getTestMap()==null){
			ted.setTestMap(map);
		}else{
			ted.getTestMap().putAll(map);
		}
	}
	
	private void parseCREATE_SVN_STATS(ArrayList<WebServiceComInputProperty> inputList, StatData sd){
		ArrayList<String> list = new ArrayList<String>();
		sd.setFileList(list);
		for(WebServiceComInputProperty prop: inputList){
			String value = prop.getValue();
			if(prop.getProperty().equals("FILE")){
				list.add(value);
			}
		}
	}
	
	private void parseGET_REVISIONS(ArrayList<WebServiceComInputProperty> inputList, StatData sd){
		EnumMap<CodeType, TreeMap<Integer, RevisionLogEntry>> map = new EnumMap<CodeType, TreeMap<Integer, RevisionLogEntry>>(CodeType.class);
		sd.setLogMap(map);
		CodeType codeType = null;
		for(WebServiceComInputProperty prop: inputList){
			String value = prop.getValue();
			if(prop.getProperty().equals("CODE_TYPE")){
				codeType = CodeType.valueOf("CHIMERA_" + value.toUpperCase());
				map.put(codeType, new TreeMap<Integer, RevisionLogEntry>());
			}else if(prop.getProperty().equals("REVISION")){
				int revision = Integer.parseInt(value);
				map.get(codeType).put(revision, new RevisionLogEntry());
				map.get(codeType).get(revision).setRevision(revision);
				map.get(codeType).get(revision).setCodeType(codeType);
			}
		}
	}
	
	private void parseGET_DATA_FOR_REVISION(ArrayList<WebServiceComInputProperty> inputList, RevisionLogEntry rle){
		User u = null;
		for(WebServiceComInputProperty prop: inputList){
			String value = prop.getValue();
			if(prop.getProperty().equals("AUTHOR_USERNAME")){
				u = new User();
				u.setUsername(value);
				rle.setAuthor(u);
			}else if(prop.getProperty().equals("AUTHOR_FIRST_NAME")){
				u.setFirstName(value);
			}else if(prop.getProperty().equals("AUTHOR_LAST_NAME")){
				u.setLastName(value);
			}else if(prop.getProperty().equals("DATE")){
				rle.setDate(Calendars.getCalendar(value));
			}else if(prop.getProperty().equals("MESSAGE_LINE")){
				if(value.equals("BLANK_STRING")){
					value = "";
				}
				rle.setMessage(rle.getMessage() + value + "\n");
			}
		}
	}
	
	private void parseGET_DIR_LISTING(ArrayList<WebServiceComInputProperty> inputList, CustomFile cf){
		TreeMap<String, CustomFile> map = new TreeMap<String, CustomFile>();
		cf.setFileMap(map);
		CustomFile f = null;
		for(WebServiceComInputProperty prop: inputList){
			String value = prop.getValue();
			if(prop.getProperty().equals("FILE")){
				f = new CustomFile();
				f.setName(value);
				f.setPath(cf.getPath() + "/" + value);
				if(value.length()>1 && value.substring(value.length()-2).equals("h5")){
					f.setHdf(true);
				}
				map.put(value, f);
			}else if(prop.getProperty().equals("IS_DIR")){
				f.setDir(Boolean.parseBoolean(value));
			}else if(prop.getProperty().equals("IS_POP")){
				f.setPop(Boolean.parseBoolean(value));
			}else if(prop.getProperty().equals("SIZE")){
				f.setSize(Integer.parseInt(value));
			}
		}
	}
	
	private void parseCREATE_REGRESSION_TEST_TARFILE(ArrayList<WebServiceComInputProperty> inputList, RegressionTest rt){
		for(WebServiceComInputProperty prop: inputList){
			String value = prop.getValue();
			if(prop.getProperty().equals("SIZE")){
				rt.getTarfile().setSize(Integer.parseInt(value));
			}
		}
	}
	
	private void parseCREATE_MATPLOTLIB_ANIMATION_MOVIEFILE(ArrayList<WebServiceComInputProperty> inputList, MatplotlibAnimation va){
		for(WebServiceComInputProperty prop: inputList){
			String value = prop.getValue();
			if(prop.getProperty().equals("MOVIE_CURRENT")){
				va.setMovieCurrent(value.equals("1") ? true : false);
			}else if(prop.getProperty().equals("MOVIEFILE_SIZE")){
				CustomFile moviefile = new CustomFile();
				moviefile.setName(va.getIndex() + ".mp4");
				moviefile.setPath(MainData.MEDIA_URL 
									+ "/viz_sets/" 
									+ va.getParentIndex()
									+ "/movies/matplotlib/"
									+ moviefile.getName());
				moviefile.setSize(Integer.parseInt(value));
				va.setMoviefile(moviefile);
			}
		}
	}
	
	private void parseCREATE_UPLOADED_ANIMATION_MOVIEFILE(ArrayList<WebServiceComInputProperty> inputList, UploadedAnimation ua){
		for(WebServiceComInputProperty prop: inputList){
			String value = prop.getValue();
			if(prop.getProperty().equals("MOVIE_CURRENT")){
				ua.setMovieCurrent(value.equals("1") ? true : false);
			}else if(prop.getProperty().equals("MOVIEFILE_SIZE")){
				CustomFile moviefile = new CustomFile();
				moviefile.setName(ua.getIndex() + ".mp4");
				moviefile.setPath(MainData.MEDIA_URL 
									+ "/viz_sets/" 
									+ ua.getParentIndex()
									+ "/movies/uploaded/"
									+ moviefile.getName());
				moviefile.setSize(Integer.parseInt(value));
				ua.setMoviefile(moviefile);
			}
		}
	}
	
	private void parseCREATE_MATPLOTLIB_ANIMATION_TARFILE(ArrayList<WebServiceComInputProperty> inputList, MatplotlibAnimation va){
		for(WebServiceComInputProperty prop: inputList){
			String value = prop.getValue();
			if(prop.getProperty().equals("SIZE")){
				va.getTarfile().setSize(Integer.parseInt(value));
			}
		}
	}
	
	private void parseCREATE_UPLOADED_ANIMATION_TARFILE(ArrayList<WebServiceComInputProperty> inputList, UploadedAnimation ua){
		for(WebServiceComInputProperty prop: inputList){
			String value = prop.getValue();
			if(prop.getProperty().equals("SIZE")){
				ua.getTarfile().setSize(Integer.parseInt(value));
			}
		}
	}
	
	private void parseCREATE_HDF_DUMP(ArrayList<WebServiceComInputProperty> inputList, CustomFile cf){
		for(WebServiceComInputProperty prop: inputList){
			String value = prop.getValue();
			if(prop.getProperty().equals("SIZE")){
				cf.setSize(Integer.parseInt(value));
			}
		}
	}
	
	private void parseVIZ_SET_ID_EXISTS(ArrayList<WebServiceComInputProperty> inputList, VizManData vcd){
		for(WebServiceComInputProperty prop: inputList){
			String value = prop.getValue();
			if(prop.getProperty().equals("VIZ_SET_ID_EXISTS")){
				vcd.setVizSetIdExists(Boolean.valueOf(value));
			}
		}
	}
	
	private void parseCREATE_2D_VIZ_SET(ArrayList<WebServiceComInputProperty> inputList, VizSet vs){
		for(WebServiceComInputProperty prop: inputList){
			String value = prop.getValue();
			if(prop.getProperty().equals("VIZ_SET_INDEX")){
				vs.setIndex(Integer.parseInt(value));
			}
		}
	}
	
	private void parseCREATE_3D_VIZ_SET(ArrayList<WebServiceComInputProperty> inputList, VizSet vs){
		int counter = 0;
		for(WebServiceComInputProperty prop: inputList){
			String value = prop.getValue();
			if(prop.getProperty().equals("VIZ_SET_INDEX")){
				vs.setIndex(Integer.parseInt(value));
			}else if(prop.getProperty().equals("ANIMATION_INDEX")){
				int animationIndex = Integer.valueOf(value);
				UploadedAnimation ua = vs.getUploadedAnimationMap().get(counter);
				vs.getUploadedAnimationMap().put(animationIndex, ua);
				vs.getUploadedAnimationMap().remove(counter);
				ua.setIndex(animationIndex);
				counter++;
			}
		}
	}
	
	private void parseMOD_3D_VIZ_SET(ArrayList<WebServiceComInputProperty> inputList, VizSet vs){
		int lastArrayPosition = 0;
		ArrayList<Integer> badPositionList = new ArrayList<Integer>();
		for(WebServiceComInputProperty prop: inputList){
			String value = prop.getValue();
			if(prop.getProperty().equals("ANIMATION_INDEX")){
				int animationIndex = Integer.valueOf(value);
				UploadedAnimation ua = vs.getUploadedAnimationMap().get(lastArrayPosition);
				while(ua.getIndex()!=-1){
					lastArrayPosition++;
					ua = vs.getUploadedAnimationMap().get(lastArrayPosition);
				}
				badPositionList.add(lastArrayPosition);
				ua.setIndex(animationIndex);
			}
		}
		for(Integer i: badPositionList){
			UploadedAnimation ua = vs.getUploadedAnimationMap().get(i);
			vs.getUploadedAnimationMap().put(ua.getIndex(), ua);
			vs.getUploadedAnimationMap().remove(i);
		}
	}
	
	private void parseGET_VIZ_SETS(ArrayList<WebServiceComInputProperty> inputList, VizData vd){
		
		TreeMap<Integer, VizSet> map = vd.getVizSetMap();
		if(map==null){
			map = new TreeMap<Integer, VizSet>();
			vd.setVizSetMap(map);
		}
		
		VizSet vs = null;
		User u = null;
		Resolution r = null;
		for(WebServiceComInputProperty prop: inputList){
			String value = prop.getValue();
			if(prop.getProperty().equals("VIZ_SET_INDEX")){
				int index = Integer.parseInt(value);
				if(!map.containsKey(index)){
					vs = new VizSet();
					vs.setIndex(index);
					map.put(index, vs);
				}
				vs = map.get(index);
			}else if(prop.getProperty().equals("VIZ_SET_ID")){
				vs.setVizSetId(value);
			}else if(prop.getProperty().equals("VIZ_SET_TYPE")){
				vs.setVizSetType(VizSetType.valueOf(value.toUpperCase()));
			}else if(prop.getProperty().equals("CREATOR_USERNAME")){
				u = new User();
				u.setUsername(value);
				vs.setCreator(u);
			}else if(prop.getProperty().equals("CREATOR_FIRST_NAME")){
				u.setFirstName(value);
			}else if(prop.getProperty().equals("CREATOR_LAST_NAME")){
				u.setLastName(value);
			}else if(prop.getProperty().equals("CREATION_DATE")){
				vs.setCreationDate(Calendars.getCalendar(value));
			}else if(prop.getProperty().equals("MOD_DATE")){
				vs.setModDate(Calendars.getCalendar(value));
			}else if(prop.getProperty().equals("PROGENITOR")){
				vs.setProgenitor(value);
			}else if(prop.getProperty().equals("RAD_RESOLUTION")){
				r = new Resolution();
				r.setRad(Integer.parseInt(value));
				vs.setResolution(r);
			}else if(prop.getProperty().equals("LAT_RESOLUTION")){
				vs.getResolution().setLat(Integer.parseInt(value));
			}else if(prop.getProperty().equals("LONG_RESOLUTION")){
				vs.getResolution().setLon(Integer.parseInt(value));
			}else if(prop.getProperty().equals("NOTES")){
				vs.setNotes(value.replaceAll("NEW_LINE", "\n"));
			}else if(prop.getProperty().equals("BOUNCE_TIME")){
				vs.setBounceTime(Double.parseDouble(value));
			}else if(prop.getProperty().equals("ELAPSED_TIME")){
				vs.setElapsedTime(Double.parseDouble(value));
			}else if(prop.getProperty().equals("TIME_STAMP")){
				vs.setTimeStampDate(Calendars.getCalendar(value));
			}else if(prop.getProperty().equals("LAST_FRAME")){
				vs.setLastFrame(Integer.parseInt(value));
			}
		}
	}
	
	private void parseREWIND_VIZ_SET(ArrayList<WebServiceComInputProperty> inputList, VizSet vs){
		for(WebServiceComInputProperty prop: inputList){
			String value = prop.getValue();
			if(prop.getProperty().equals("BOUNCE_TIME")){
				vs.setBounceTime(Double.parseDouble(value));
			}else if(prop.getProperty().equals("ELAPSED_TIME")){
				vs.setElapsedTime(Double.parseDouble(value));
			}
		}
	}
	
	
	private void parseGET_MATPLOTLIB_ANIMATIONS(ArrayList<WebServiceComInputProperty> inputList, VizExplData ved){
		TreeMap<Integer, MatplotlibAnimation> map = null;
		MatplotlibAnimation va = null;
		VizSet vs = null;
		for(WebServiceComInputProperty prop: inputList){
			String value = prop.getValue();
			if(prop.getProperty().equals("VIZ_SET_INDEX")){
				vs = ved.getVizSetMap().get(Integer.valueOf(value));
				if(vs.getMatplotlibAnimationMap()==null){
					vs.setMatplotlibAnimationMap(new TreeMap<Integer, MatplotlibAnimation>());
				}
				map = vs.getMatplotlibAnimationMap();
			}else if(prop.getProperty().equals("MATPLOTLIB_ANIMATION_INDEX")){
				int index = Integer.parseInt(value);
				if(!map.containsKey(index)){
					va = new MatplotlibAnimation();
					va.setIndex(index);
					va.setParentIndex(vs.getIndex());
					map.put(va.getIndex(), va);
				}
				va = map.get(index);
			}else if(prop.getProperty().equals("CREATION_DATE")){
				va.setCreationDate(Calendars.getCalendar(value));
			}else if(prop.getProperty().equals("MOD_DATE")){
				va.setModDate(Calendars.getCalendar(value));
			}else if(prop.getProperty().equals("MATPLOTLIB_MODEL_INDEX")){
				va.setMatplotlibModel(MainData.getMatplotlibModel(Integer.valueOf(value)));
			}else if(prop.getProperty().equals("ANIMATION_ID")){
				va.setAnimationId(value);
			}else if(prop.getProperty().equals("HOT")){
				va.setHot(value.equals("1") ? true : false);
			}else if(prop.getProperty().equals("RANGE")){
				va.setRange(value);
			}else if(prop.getProperty().equals("SCALE")){
				va.setScale(Scale.valueOf(value.toUpperCase()));
			}else if(prop.getProperty().equals("MATPLOTLIB_COLORMAP_INDEX")){
				va.setColormap(MainData.getMatplotlibColormap(Integer.valueOf(value)));
			}else if(prop.getProperty().equals("ZOOM")){
				va.setZoom(value);
			}else if(prop.getProperty().equals("SMOOTH_ZONES")){
				va.setSmoothZones(value.equals("1") ? true : false);
			}else if(prop.getProperty().equals("DISPLAY_DATE")){
				va.setDisplayDate(value.equals("1") ? true : false);
			}
		}
	}
	
	private void parseGET_UPLOADED_ANIMATIONS(ArrayList<WebServiceComInputProperty> inputList, VizExplData ved){
		TreeMap<Integer, UploadedAnimation> map = null;
		UploadedAnimation ua = null;
		VizSet vs = null;
		User u = null;
		for(WebServiceComInputProperty prop: inputList){
			String value = prop.getValue();
			if(prop.getProperty().equals("VIZ_SET_INDEX")){
				vs = ved.getVizSetMap().get(Integer.valueOf(value));
				if(vs.getUploadedAnimationMap()==null){
					vs.setUploadedAnimationMap(new TreeMap<Integer, UploadedAnimation>());
				}
				map = vs.getUploadedAnimationMap();
			}else if(prop.getProperty().equals("UPLOADED_ANIMATION_INDEX")){
				int index = Integer.parseInt(value);
				if(!map.containsKey(index)){
					ua = new UploadedAnimation();
					ua.setIndex(index);
					ua.setParentIndex(vs.getIndex());
					map.put(ua.getIndex(), ua);
				}
				ua = map.get(index);
			}else if(prop.getProperty().equals("UPLOAD_DATE")){
				ua.setUploadDate(Calendars.getCalendar(value));
			}else if(prop.getProperty().equals("ANIMATION_ID")){
				ua.setAnimationId(value);
			}else if(prop.getProperty().equals("HOT")){
				ua.setHot(value.equals("1") ? true : false);
			}else if(prop.getProperty().equals("DESC")){
				ua.setDesc(value);
			}else if(prop.getProperty().equals("BOUNCE_TIME")){
				ua.setBounceTime(Double.parseDouble(value));
			}else if(prop.getProperty().equals("NUM_FRAMES")){
				ua.setNumFrames(Integer.parseInt(value));
			}else if(prop.getProperty().equals("UPLOADER_USERNAME")){
				u = new User();
				u.setUsername(value);
				ua.setUploader(u);
			}else if(prop.getProperty().equals("UPLOADER_FIRST_NAME")){
				u.setFirstName(value);
			}else if(prop.getProperty().equals("UPLOADER_LAST_NAME")){
				u.setLastName(value);
			}
		}
	}
	
	private void parseGET_DEFAULT_MATPLOTLIB_ANIMATIONS(ArrayList<WebServiceComInputProperty> inputList, VizSet vs){
		TreeMap<Integer, MatplotlibAnimation> map = new TreeMap<Integer, MatplotlibAnimation>();
		vs.setMatplotlibAnimationMap(map);
		MatplotlibAnimation va = null;
		for(WebServiceComInputProperty prop: inputList){
			String value = prop.getValue();
			if(prop.getProperty().equals("MATPLOTLIB_ANIMATION_DEFAULT_INDEX")){
				va = new MatplotlibAnimation();
				va.setIndex(-1);
				map.put(Integer.parseInt(value), va);
			}else if(prop.getProperty().equals("MATPLOTLIB_MODEL_INDEX")){
				va.setMatplotlibModel(MainData.getMatplotlibModel(Integer.valueOf(value)));
			}else if(prop.getProperty().equals("ANIMATION_ID")){
				va.setAnimationId(value);
			}else if(prop.getProperty().equals("MATPLOTLIB_COLORMAP_INDEX")){
				va.setColormap(MainData.getMatplotlibColormap(Integer.valueOf(value)));
			}else if(prop.getProperty().equals("ZOOM")){
				va.setZoom(value);
			}else if(prop.getProperty().equals("SMOOTH_ZONES")){
				va.setSmoothZones(value.equals("1") ? true : false);
			}else if(prop.getProperty().equals("DISPLAY_DATE")){
				va.setDisplayDate(value.equals("1") ? true : false);
			}else if(prop.getProperty().equals("RANGE")){
				va.setRange(value);
			}else if(prop.getProperty().equals("SCALE")){
				va.setScale(Scale.valueOf(value.toUpperCase()));
			}
		}
	}
	
	private void parseGET_MATPLOTLIB_ANIMATIONS(ArrayList<WebServiceComInputProperty> inputList, VizSet vs){
		TreeMap<Integer, MatplotlibAnimation> map = new TreeMap<Integer, MatplotlibAnimation>();
		vs.setMatplotlibAnimationMap(map);
		MatplotlibAnimation va = null;
		for(WebServiceComInputProperty prop: inputList){
			String value = prop.getValue();
			if(prop.getProperty().equals("MATPLOTLIB_ANIMATION_INDEX")){
				va = new MatplotlibAnimation();
				va.setIndex(Integer.parseInt(value));
				map.put(va.getIndex(), va);
			}else if(prop.getProperty().equals("CREATION_DATE")){
				va.setCreationDate(Calendars.getCalendar(value));
			}else if(prop.getProperty().equals("MOD_DATE")){
				va.setModDate(Calendars.getCalendar(value));
			}else if(prop.getProperty().equals("MATPLOTLIB_MODEL_INDEX")){
				va.setMatplotlibModel(MainData.getMatplotlibModel(Integer.valueOf(value)));
			}else if(prop.getProperty().equals("ANIMATION_ID")){
				va.setAnimationId(value);
			}else if(prop.getProperty().equals("HOT")){
				va.setHot(value.equals("1") ? true : false);
			}else if(prop.getProperty().equals("MATPLOTLIB_COLORMAP_INDEX")){
				va.setColormap(MainData.getMatplotlibColormap(Integer.valueOf(value)));
			}else if(prop.getProperty().equals("ZOOM")){
				va.setZoom(value);
			}else if(prop.getProperty().equals("SMOOTH_ZONES")){
				va.setSmoothZones(value.equals("1") ? true : false);
			}else if(prop.getProperty().equals("DISPLAY_DATE")){
				va.setDisplayDate(value.equals("1") ? true : false);
			}else if(prop.getProperty().equals("RANGE")){
				va.setRange(value);
			}else if(prop.getProperty().equals("SCALE")){
				va.setScale(Scale.valueOf(value.toUpperCase()));
			}
		}
	}

	private void parseGET_UPLOADED_ANIMATIONS(ArrayList<WebServiceComInputProperty> inputList, VizSet vs){
		TreeMap<Integer, UploadedAnimation> map = new TreeMap<Integer, UploadedAnimation>();
		vs.setUploadedAnimationMap(map);
		UploadedAnimation ua = null;
		User u = null;
		for(WebServiceComInputProperty prop: inputList){
			String value = prop.getValue();
			if(prop.getProperty().equals("UPLOADED_ANIMATION_INDEX")){
				ua = new UploadedAnimation();
				ua.setIndex(Integer.parseInt(value));
				map.put(ua.getIndex(), ua);
			}else if(prop.getProperty().equals("UPLOAD_DATE")){
				ua.setUploadDate(Calendars.getCalendar(value));
			}else if(prop.getProperty().equals("ANIMATION_ID")){
				ua.setAnimationId(value);
			}else if(prop.getProperty().equals("HOT")){
				ua.setHot(value.equals("1") ? true : false);
			}else if(prop.getProperty().equals("DESC")){
				ua.setDesc(value);
			}else if(prop.getProperty().equals("BOUNCE_TIME")){
				ua.setBounceTime(Double.parseDouble(value));
			}else if(prop.getProperty().equals("NUM_FRAMES")){
				ua.setNumFrames(Integer.parseInt(value));
			}else if(prop.getProperty().equals("UPLOADER_USERNAME")){
				u = new User();
				u.setUsername(value);
				ua.setUploader(u);
			}else if(prop.getProperty().equals("UPLOADER_FIRST_NAME")){
				u.setFirstName(value);
			}else if(prop.getProperty().equals("UPLOADER_LAST_NAME")){
				u.setLastName(value);
			}
		}
	}
	
	private void parseGET_MATPLOTLIB_ANIMATION_DATAFILE_SIZE(ArrayList<WebServiceComInputProperty> inputList, MatplotlibAnimation va){
		for(WebServiceComInputProperty prop: inputList){
			String value = prop.getValue();
			if(prop.getProperty().equals("DATAFILE_SIZE")){
				CustomFile datafile = new CustomFile();
				datafile.setName(new DecimalFormat("00000").format(va.getCurrentFrame()) + ".h5");
				datafile.setPath(MainData.DATA_URL 
						+ "/viz_sets/" 
						+ va.getParentIndex()
						+ "/hdf5/"
						+ datafile.getName());
				datafile.setSize(Integer.parseInt(value));
				va.setDatafile(datafile);
			}
		}
	}
	
	private void parseGET_VIZ_JOBS(ArrayList<WebServiceComInputProperty> inputList, VizData d){
		TreeMap<Integer, VizJob> map = null;
		VizJob vj = null;
		VizSet vs = null;
		User u = null;
		for(WebServiceComInputProperty prop: inputList){
			String value = prop.getValue();
			if(prop.getProperty().equals("VIZ_SET_INDEX")){
				vs = d.getVizSetMap().get(Integer.valueOf(value));
				map = vs.getVizJobMap();
				if(map==null){
					map = new TreeMap<Integer, VizJob>();
					vs.setVizJobMap(map);
				}
			}else if(prop.getProperty().equals("VIZ_JOB_INDEX")){
				int index = Integer.parseInt(value);
				if(!map.containsKey(index)){
					vj = new VizJob();
					vj.setIndex(index);
					map.put(vj.getIndex(), vj);
				}
				vj = map.get(index);
			}else if(prop.getProperty().equals("CREATOR_USERNAME")){
				u = new User();
				u.setUsername(value);
				vj.setCreator(u);
			}else if(prop.getProperty().equals("CREATOR_FIRST_NAME")){
				u.setFirstName(value);
			}else if(prop.getProperty().equals("CREATOR_LAST_NAME")){
				u.setLastName(value);
			}else if(prop.getProperty().equals("START_DATE")){
				vj.setStartDate(Calendars.getCalendar(value));
			}else if(prop.getProperty().equals("END_DATE")){
				vj.setEndDate(Calendars.getCalendar(value));
			}else if(prop.getProperty().equals("JOB_ID")){
				vj.setJobId(Integer.valueOf(value));
			}else if(prop.getProperty().equals("MODELS_DATA_PATH")){
				vj.setModelsDataPath(value);
			}else if(prop.getProperty().equals("INITIAL_DATA_PATH")){
				vj.setInitialDataPath(value);
			}else if(prop.getProperty().equals("PLATFORM")){
				vj.setPlatform(Platform.valueOf(value.toUpperCase()));
			}else if(prop.getProperty().equals("START_FRAME")){
				vj.setStartFrame(Integer.parseInt(value));
			}else if(prop.getProperty().equals("END_FRAME")){
				vj.setEndFrame(Integer.parseInt(value));
				vs.setLastFrame(vs.getVizJobMap().lastEntry().getValue().getEndFrame());
			}
		}
	}
	
	private void parseGENERATE_MATPLOTLIB_ANIMATION_PREVIEW(ArrayList<WebServiceComInputProperty> inputList, MatplotlibAnimation va){
		for(WebServiceComInputProperty prop: inputList){
			String value = prop.getValue();
			if(prop.getProperty().equals("PREVIEW_FILENAME")){
				CustomFile framefile = new CustomFile();
				framefile.setName(value);
				framefile.setImg(true);
				framefile.setPath(MainData.MEDIA_URL 
									+ "/viz_sets/tmp/images/matplotlib/"
									+ framefile.getName());
				try{
					framefile.setContents(IOUtilities.readURL(framefile.getPath()));
				}catch(Exception e){
					e.printStackTrace();
				}
				va.setFramefile(framefile);
			}
		}
	}
}
