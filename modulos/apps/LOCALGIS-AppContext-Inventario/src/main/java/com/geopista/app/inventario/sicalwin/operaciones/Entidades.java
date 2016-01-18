/**
 * Entidades.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.inventario.sicalwin.operaciones;

import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.geopista.app.inventario.sicalwin.dao.Entidad;
import com.geopista.app.inventario.sicalwin.util.ConfigSicalwin;


public class Entidades extends OperacionSicalWin{

	/*
	(*) La estructura del dato devuelto en la etiqueta <detalle> es una cadena de longitud variable concatenando los
	valores de la operación en el orden indicado en la siguiente estructura:
	ENT_COD Código entidad
	ENT_NOM Nombre entidad
	*/
	
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Entidades.class);
	public Entidades(){
		
	}
	

	private void listaEntidades(Element e, String codigo_organizacion,
			String codigo_entidad, String ejercicio_contable, String usuario,
			String password, HashMap mapPAR) {
		creaOPE(e, ConfigSicalwin.APL, ConfigSicalwin.TOBJ_LISTA_ENTIDADES,
				ConfigSicalwin.CMD_LST, ConfigSicalwin.VER);
		creaSEC(e, ConfigSicalwin.CLI, codigo_organizacion, codigo_entidad,
				ejercicio_contable, usuario, password);
		//creaPAR(e, mapPAR);
		creaFixedPAR(e);
	}
	
	/**
	 * Obtencion de Terceros y Cuentas
	 * @return
	 */
	public ArrayList<Entidad> getEntidades() {
		ArrayList<Entidad> entidades=new ArrayList<Entidad>();
		try {
			initRequest();
			
			HashMap mapPAR = new HashMap();
			// En el mapPAR metemos los valores del <par></par>
			//mapPAR.put("l_tercero","<l_tercero><tercero><idenTercero>1</idenTercero></tercero></l_tercero>");
			listaEntidades(rootElement, CODIGO_ORG, CODIGO_ENTIDAD, "2011",USUARIO, CLAVE, null);
			
			String sml;
			sml = parseDocumentToXML(doc);			
			Document document=sendToServer(sml);
			
			if (document==null){
				logger.error("Error al obtener el documento xml en la peticion");
			}
			else{
				//El resultado es un XML en el que en el campo detalle viene esto				
				//ENT_COD Código entidad
				//ENT_NOM Nombre entidad
				if (isExito(document)){
					logger.info("Peticion correcta");
					String detalle=getInfo(document,"detalle");
					//logger.info("Detalle OK:"+detalle);
					if (detalle!=null){
						String [] valores=detalle.split(ConfigSicalwin.SEPARADOR);						
						Entidad entidad=new Entidad();
						entidad.load(valores);
						entidades.add(entidad);
					}
				}
				else{
					logger.info("Peticion incorrecta");	
					String detalle=getInfo(document,"desc");
					logger.error("Detalle Error:"+detalle);
				}
				
			}			
				
			
				
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return entidades;
	}
}
