package org.bellerophon.enums;

public enum VizSetType {

	CHIMERA2D("Chimera 2D"),
	CHIMERA3D("Chimera 3D");
	
	private String string;
	
	VizSetType(String string){
		this.string = string;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	public String toString(){return string;}
	
}
