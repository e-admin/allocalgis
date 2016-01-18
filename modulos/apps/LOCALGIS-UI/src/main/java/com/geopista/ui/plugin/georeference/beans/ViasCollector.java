/**
 * ViasCollector.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.georeference.beans;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

import com.vividsolutions.jump.feature.AttributeType;

/**
 * @author rubengomez
 *
 *	Bean que almacena un ArrayList de Vias y un HashMap con los valores de los campos
 *	como clave y como valor el tipo general de todos los campos.
 * 
 */
public class ViasCollector {
	
	private ArrayList listaVias = null;
	private HashMap dataType = null;
	private boolean isLocal;
	
	public ViasCollector(boolean isLocal){
		listaVias = new ArrayList();
		this.isLocal = isLocal;
	}
	
	public void addVia(Via via){
		listaVias.add(via);
		if(isLocal)
			setDataType(via);
	}
	/**
	 * En caso de que se llame por primera vez (Constructor) rellena el 
	 * HashMap con los valores iniciales, de clave nombre del campo y
	 * de valor el tipo.
	 * En caso agregar nuevas vias, se llama a este método para comparar los valores 
	 * y sustituir por prioridad los tipos almacenados.

	 * @param via Tipo Via con la información del archivo
	 * 
	 */
	private void setDataType(Via via){
		if (dataType == null){
			dataType = new HashMap();
			Enumeration listaClaves = via.getKeys();
			while (listaClaves.hasMoreElements()){
				String key = (String) listaClaves.nextElement();
				AttributeType atributo = typeOfData((String)via.getData(key));
				dataType.put(key, atributo);
			}
		}
		else{
			Enumeration listaClaves = via.getKeys();
			while (listaClaves.hasMoreElements()){
				String key = (String) listaClaves.nextElement();
				AttributeType atributo = typeOfData((String)via.getData(key));
				if (dataType.get(key) != null && typeOfData((String)via.getData(key)) != null){// Ojo, creo que esto produce un error 
	                if(!dataType.get(key).equals(atributo)){
	    				if (((atributo.equals(AttributeType.DOUBLE) || atributo.equals(AttributeType.INTEGER)) &&
	    						dataType.get(key).equals(AttributeType.DATE) ) ||
	    					((dataType.get(key).equals(AttributeType.DOUBLE) || dataType.get(key).equals(AttributeType.INTEGER)) &&
	    							atributo.equals(AttributeType.DATE))){
	    						// Cambio a tipo String
	    					dataType.put(key, AttributeType.STRING);
	    				}else{
		                	if (
		                			dataType.get(key).equals(AttributeType.STRING) ||
		                		(atributo.equals(AttributeType.INTEGER) && dataType.get(key).equals(AttributeType.DOUBLE))
		                		){
		        					// Cambio al tipo attributeType
		                		dataType.put(key, atributo);
		                	}
	    				}
	                }			
				}
				else{
					/*if(dataType.get(key) == "")
						dataType.put(key, AttributeType.STRING);*/
				}
			}// Fin_while		
		}//fin_else
	}//fin_setDataType
	
	/**
	 * Método que indica el tipo de variable que posee
	 * 
	 * @param campo Texto a analizar 
	 * @return AttributeType Contiene el tipo de dato al que pertenece la variable campo
	 */
	private AttributeType typeOfData(String campo)
    {
        campo=campo.trim();
        AttributeType res = AttributeType.STRING;
        if (campo.length()==0)
            res = null;
        else {
            try{
                SimpleDateFormat formatoFecha=new SimpleDateFormat("dd/MM/yy");
                DateFormat d =formatoFecha.getDateInstance();
                formatoFecha.parse(campo);
                res = AttributeType.DATE;
            }catch(Exception e){
                try{
                    if (campo.indexOf('.')== -1 && campo.indexOf(',')== -1){
                        if (campo.length()< 10){
                            new Integer(campo);
                            res = AttributeType.INTEGER;
                        }else{
                            new Double(campo);
                            res = AttributeType.DOUBLE; 
                        }
                    }else{
                        new Double(campo);
                        res = AttributeType.DOUBLE;
                    }
                }catch(Exception f){
                    res = AttributeType.STRING;
                }
            }
            
        }
        return res; 
    }
// No sé si será necesario:
//	
//    public Date getDate(String val){
//        Date value=null;
//        try{
//            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
//            value=sdf.parse(val);
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//        return value;
//    }	
//	
	public HashMap getDataType() {
		return dataType;
	}
	public void setDataType(HashMap dataType) {
		this.dataType = dataType;
	}
	public ArrayList getListaVias() {
		return listaVias;
	}
	public void setListaVias(ArrayList listaVias) {
		this.listaVias = listaVias;
	}
}
