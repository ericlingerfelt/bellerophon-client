package org.bellerophon.data.util;

import org.bellerophon.data.Data;

public class MatplotlibModel implements Data{

	private int index;
	private String name, path;
	
	public void initialize() {
		index = -1;
		name = "";
		path = "";
	}

	public Data clone() {
		return null;
	}

	public String toString(){
		return path + "/" + name;
	}
	
	public int getIndex(){return index;}
	public void setIndex(int index){this.index = index;}
	
	public String getName(){return name;}
	public void setName(String name){this.name = name;}
	
	public String getPath(){return path;}
	public void setPath(String path){this.path = path;}
	
}
