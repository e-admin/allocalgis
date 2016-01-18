/**
 * SvgXMLUpload.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package es.satec.localgismobile.server.projectsync.xml.beans;

import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;

import es.satec.localgismobile.server.projectsync.xml.ConstantsXMLUpload;

/**
 * 	<svg viewBox="0.0 0.0 641.70703 505.13672" despX="390375.27" despY="4716921.65" >
 * @author irodriguez
 *
 */
public class SvgXMLUpload {

	private List<GroupXMLUpload> groupList;
	private List<AttributeXMLUpload> attributeList;
	private double despX;
	private double despY;
	private Coordinate leftDownViewBox;
	private Coordinate rightUpViewBox;
	
	public SvgXMLUpload(List<GroupXMLUpload> groupList,
			List<AttributeXMLUpload> attributeList) {
		super();
		this.groupList = groupList;
		this.attributeList = attributeList;
		updateDesp();
	}
	public List<GroupXMLUpload> getGroupList() {
		return groupList;
	}

	public void setGroups(List<GroupXMLUpload> groupList) {
		this.groupList = groupList;
	}

	public List<AttributeXMLUpload> getAttributeList() {
		return attributeList;
	}

	public void setAttributeList(List<AttributeXMLUpload> attributeList) {
		this.attributeList = attributeList;
		updateDesp();
	}	
	public double getDespX() {
		return despX;
	}
	public double getDespY() {
		return despY;
	}
	public Coordinate getLeftDownViewBox() {
		return leftDownViewBox;
	}
	public Coordinate getRightUpViewBox() {
		return rightUpViewBox;
	}
	/**
	 * Actualiza los desplazamientos X e Y a los que ha sido sometido el SVG y también su viewbox(donde se encuadra el mapa)
	 * (atajo para atributos despX, despY)
	 */
	private void updateDesp(){
		AttributeXMLUpload attribute = null;
		for (int i = 0; i < attributeList.size(); i++) {
			attribute = attributeList.get(i);
			if(attribute.getKey().equals(ConstantsXMLUpload.ATT_DESPX)){
				despX = Double.parseDouble(attribute.getValue());
			} else if(attribute.getKey().equals(ConstantsXMLUpload.ATT_DESPY)){
				despY = Double.parseDouble(attribute.getValue());
			} else if(attribute.getKey().equals(ConstantsXMLUpload.ATT_VIEWBOX)){
				String fullViewBox = attribute.getValue();
				String[] splitViewBox = fullViewBox.split(" ");
				if(splitViewBox.length==4){
					leftDownViewBox = new Coordinate(Double.parseDouble(splitViewBox[0]),Double.parseDouble(splitViewBox[1]));
					rightUpViewBox = new Coordinate(Double.parseDouble(splitViewBox[2]),Double.parseDouble(splitViewBox[3]));
				}
			} 
		}
	}	
	
}
