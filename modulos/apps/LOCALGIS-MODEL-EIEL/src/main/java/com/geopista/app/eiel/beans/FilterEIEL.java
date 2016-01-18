/**
 * FilterEIEL.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.beans;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;

import com.geopista.app.eiel.beans.filter.LCGCampoCapaTablaEIEL;
import com.geopista.app.eiel.panels.NoFilterException;
import com.geopista.feature.GeopistaFeature;

public class FilterEIEL implements IFilterEIEL,Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2186197279047776903L;

	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(FilterEIEL.class);
	
	ArrayList<LCGCampoCapaTablaEIEL> relacionFields;
	
	String tablaAlfanumericoUsos=null;
	
	
	@Override
	public String getFilterSQL() throws NoFilterException {		
		return getFilterSQL(relacionFields);
	}
	
	@Override
	public String getNombreTablaAlfanumerica() {		
		return getNombreTablaAlfanumerica(relacionFields);
	}
	
	@Override
	public String getNombreTablaAlfanumericaUsos() {		
		return tablaAlfanumericoUsos;
	}
	@Override
	public String getFilterSQLByFeature(Object feature) {
		return getFilterSQLByFeature(feature,relacionFields);
	}	

	
	public ArrayList<LCGCampoCapaTablaEIEL> getRelacionFields(){
		return relacionFields;		
	}

	/**
	 * Obtencion del metood para filtrar elementos
	 * @param relacionFields
	 * @return
	 */
	private String getFilterSQL(ArrayList<LCGCampoCapaTablaEIEL> relacionFields) throws NoFilterException{
		StringBuffer sql=new StringBuffer();
		try {
			if (relacionFields==null){
				logger.error("El elemento no dispone de datos alfanumericos para filtrar");
				throw new NoFilterException("NoFilterException");
			}
			Iterator<LCGCampoCapaTablaEIEL> it = relacionFields.iterator();			
			sql.append("(");
			int numElementos=1;
			while (it.hasNext()) {
				LCGCampoCapaTablaEIEL campoCapaTablaEIEL = it.next();
				String strMethod = campoCapaTablaEIEL.getMethod();
				String campoBD= campoCapaTablaEIEL.getCampoBD();
				String tabla= campoCapaTablaEIEL.getTabla();

				Method method = this.getClass().getMethod(strMethod,new Class[0]);				
				String valor;
				try {
					valor = (String) method.invoke(this);
					sql.append(tabla+"."+campoBD+"='"+valor+"'");
				} catch (Exception e) {
					if (method.invoke(this) instanceof java.math.BigDecimal){
						java.math.BigDecimal value = (java.math.BigDecimal) method.invoke(this);
						sql.append(tabla+"."+campoBD+"="+value+" ");
					}
					else if (method.invoke(this) instanceof Long){
						Long value = (Long) method.invoke(this);
						sql.append(tabla+"."+campoBD+"="+value+" ");
					}
				}	
				
				if (numElementos<relacionFields.size())
					sql.append(" and ");
				numElementos++;
			}
			sql.append(")");
		} catch (NoFilterException e) {			
			throw e;
		}catch (Exception e) {
			logger.error("Error al realizar el reflect del metodo:",e);
			//sql.delete(0,sql.length());
			return null;
		}
		return sql.toString();
	}
	
	/**
	 * Obtenemos el nombre de la tabla alfanumerica asociada. La primera que encontremos en la
	 * relacion  relationFields nos vale.
	 * @param relacionFields
	 * @return
	 */
	private String getNombreTablaAlfanumerica(ArrayList<LCGCampoCapaTablaEIEL> relacionFields) {
		
		String nombreTablaAlfanumerica=null;
		try {
			Iterator<LCGCampoCapaTablaEIEL> it = relacionFields.iterator();			
			int numElementos=1;
			while (it.hasNext()) {
				LCGCampoCapaTablaEIEL campoCapaTablaEIEL = it.next();
				nombreTablaAlfanumerica= campoCapaTablaEIEL.getTabla();
				break;
			}
		} catch (Exception e) {
			logger.error("Error al realizar la operacion:",e);
		}
		return nombreTablaAlfanumerica;
	}
	
	
	
	/**
	 * Obtenemos los atributos de la feature
	 * @param feature
	 * @param relacionFields
	 * @return
	 */
	public String getFilterSQLByFeature(Object feature,
			ArrayList<LCGCampoCapaTablaEIEL> relacionFields) {
		StringBuffer sql=new StringBuffer();
		String campoCapa=null;
		try {
			Iterator<LCGCampoCapaTablaEIEL> it = relacionFields.iterator();			
			sql.append("(");
			int numElementos=1;
			while (it.hasNext()) {
				LCGCampoCapaTablaEIEL campoCapaTablaEIEL = it.next();
				campoCapa= campoCapaTablaEIEL.getCampoCapa();
				String campoBD= campoCapaTablaEIEL.getCampoBD();
				String tabla= campoCapaTablaEIEL.getTabla();

				if (feature instanceof GeopistaFeature){
					GeopistaFeature geopistaFeature=(GeopistaFeature)feature;
					String valor=(String)geopistaFeature.getString(campoCapa);			
					sql.append(tabla+"."+campoBD+"='"+valor+"'");
					if (numElementos<relacionFields.size())
						sql.append(" and ");
					numElementos++;
				}
			}
			sql.append(")");
		} catch (Exception e) {
			logger.error("Excepcion al recuperar la informacion de features: Campo Capa:"+campoCapa,e);
			return null;
		}
		return sql.toString();
	}

}
