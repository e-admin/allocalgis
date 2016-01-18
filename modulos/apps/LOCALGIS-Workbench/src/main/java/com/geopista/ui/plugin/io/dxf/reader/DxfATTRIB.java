/**
 * DxfATTRIB.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 *
 * Este codigo se distribuye bajo licencia GPL
 * de GNU. Para obtener una cópia integra de esta
 * licencia acude a www.gnu.org.
 * 
 * Este software se distribuye "como es". AGIL
 * solo  pretende desarrollar herramientas para
 * la promoción del GIS Libre.
 * AGIL no se responsabiliza de las perdidas económicas o de 
 * información derivadas del uso de este software.
 */

package com.geopista.ui.plugin.io.dxf.reader;

/**Implementacion de la entidad Dxf ATTRIB no contemplada
 * por la version original del visor.
 *
 * Segun la especificacion geopistadxf, una entidad ATTRIB es un atributo visible
 * de un INSERT (Bloque insertado). Cuando una entidad INSERT tiene un grupo que especifica
 * que tiene atributos asociados, todas las entidades Dxf que encontremos hasta
 * la proxima entidad ENDSEQ seran entidades ATTRIB asociadas a ese INSERT.
 *
 */
import com.geopista.ui.plugin.io.dxf.math.Point3D;
public class DxfATTRIB extends DxfEntity {

	/**Punto de insercion del texto, definido
	por los grupos (#10,#20,#30)*/
	protected Point3D insertPoint = new Point3D();

	/**Altura del texto visible en pantalla, definida por los grupos #40*/
	protected double textHeight;

	/**Nombre del atributo, grupo #1*/
	protected String attribName;

	/**Valor del atributo, grupo #2*/
	protected String attribValue;


	public String getAttribName(){
		return attribName;
	}

	public String getAttribValue(){
		return attribValue;
	}

	/**Asigna valor para los grupos cuyo tipo de dato sea String:
	 * #2 (nombre del atributo) #1(valor)
	 */
	public boolean setGroup(short groupNumber,String value){

		switch(groupNumber){

			case 1:
				attribValue = value;
				return true;
			case 2:
				attribName = value;
				return true;
			default:
				return super.setGroup(groupNumber, value);
		}

	}

	/**Asigna valor para los grupos cuyo tipo de dato sea un float:
	 * De momento son #10,#20,#30 (coordenadas), #40 (altura del texto)
	 */
	public boolean setGroup(short groupNumber,double value){
		switch(groupNumber){

			case 10:
			case 20:
			case 30:
				setCoord(insertPoint, groupNumber/10, value);
				return true;
			case 40:
				textHeight=value;
				return true;
			 default:
				return super.setGroup(groupNumber, value);
		 }
	}

	public void convert(DxfConverter converter, DxfFile dxf, Object collector) {
		converter.convert(this, dxf, collector);
	}




}
