package org.bellerophon.data.util;

import org.bellerophon.data.Data;

public class Resolution implements Data, Comparable<Resolution>{

	private int rad, lat, lon;
	
	public Resolution clone(){
		return null;
	}
	
	public Resolution(){
		initialize();
	}
	
	@Override
	public void initialize(){
		this.rad = 0;
		this.lat = 0;
		this.lon = 0;
	}
	
	public int getRad(){return rad;}	
	public void setRad(int rad){this.rad = rad;}
	
	public int getLat(){return lat;}	
	public void setLat(int lat){this.lat = lat;}
	
	public int getLon(){return lon;}	
	public void setLon(int lon){this.lon = lon;}

	public int compareTo(Resolution r){
		if(rad!=r.rad){
			return rad-r.rad;
		}
		if(lat!=r.lat){
			return lat-r.lat;
		}
		return lon-r.lon;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object object){
		if(object instanceof Resolution){
			Resolution r = (Resolution)object;
			if(r.rad==rad && r.lat==lat && r.lon==lon){
				return true;
			}
			return false;
		}
		return false;
	}
	
	public String toString(){
		if(lon==0){
			return rad + " x " + lat;
		}
		return rad + " x " + lat + " x " + lon;
	}
	
}
