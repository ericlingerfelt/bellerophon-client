package org.bellerophon.enums;

public enum PlotType {

	EXPLOSION_ENERGY("Explosion Energy"),
	RADII("Radii");
	
	private String string;
	
	/**
	 * Instantiates a new platform.
	 *
	 * @param string the string
	 */
	PlotType(String string){
		this.string = string;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	public String toString(){return string;}
	
}
