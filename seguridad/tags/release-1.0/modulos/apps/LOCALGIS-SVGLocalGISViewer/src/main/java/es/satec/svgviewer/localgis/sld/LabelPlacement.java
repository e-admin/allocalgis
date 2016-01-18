package es.satec.svgviewer.localgis.sld;


public class LabelPlacement {

    private float anchorPointX = 0.f;
    private float anchorPointY = 0.f;
    private int displacementX = 0;
    private int displacementY = 0;
	/**
	 * @param anchorPointX
	 * @param anchorPointY
	 * @param displacementX
	 * @param displacementY
	 */
	public LabelPlacement(float anchorPointX, float anchorPointY, int displacementX, int displacementY) {
		super();
		this.anchorPointX = anchorPointX;
		this.anchorPointY = anchorPointY;
		this.displacementX = displacementX;
		this.displacementY = displacementY;
	}
	
	public float getAnchorPointX() {
		return anchorPointX;
	}
	public void setAnchorPointX(float anchorPointX) {
		this.anchorPointX = anchorPointX;
	}
	public float getAnchorPointY() {
		return anchorPointY;
	}
	public void setAnchorPointY(float anchorPointY) {
		this.anchorPointY = anchorPointY;
	}
	public int getDisplacementX() {
		return displacementX;
	}
	public void setDisplacementX(int displacementX) {
		this.displacementX = displacementX;
	}
	public int getDisplacementY() {
		return displacementY;
	}
	public void setDisplacementY(int displacementY) {
		this.displacementY = displacementY;
	}
    
}