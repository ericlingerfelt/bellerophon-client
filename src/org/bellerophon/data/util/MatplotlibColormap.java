package org.bellerophon.data.util;

import org.bellerophon.data.Data;

public class MatplotlibColormap implements Data, Comparable<MatplotlibColormap>{

	private int index;
	private String name;
	
	public void initialize() {
		index = -1;
		name = "";
	}

	public Data clone() {
		return null;
	}

	public String toString(){
		return name;
	}
	
	public int compareTo(MatplotlibColormap m){
		return name.compareTo(m.getName());
	}
	
	public boolean equals(Object object){
		if(object instanceof MatplotlibColormap){
			MatplotlibColormap m = (MatplotlibColormap)object;
			if(m.index == index){
				return true;
			}
			return false;
		}
		return false;
	}
	
	public int getIndex(){return index;}
	public void setIndex(int index){this.index = index;}
	
	public String getName(){return name;}
	public void setName(String name){this.name = name;}
	
}
