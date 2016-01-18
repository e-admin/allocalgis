/**
 * LabelPlacement.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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