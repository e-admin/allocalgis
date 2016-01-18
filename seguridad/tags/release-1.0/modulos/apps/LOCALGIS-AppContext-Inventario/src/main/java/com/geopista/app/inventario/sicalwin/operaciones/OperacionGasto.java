package com.geopista.app.inventario.sicalwin.operaciones;

import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.geopista.app.inventario.sicalwin.dao.Entidad;
import com.geopista.app.inventario.sicalwin.util.ConfigSicalwin;

public class OperacionGasto extends OperacionSicalWin{

	
	/**
	 * GENERACIÓN DE OPERACIONES DE GASTO PREVIAS Y DEFINITIVAS.
	 */
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(OperacionGasto.class);
	public OperacionGasto(){
		
	}
	
	

	private void generarOperacionGasto(Element e,
			String codigo_organizacion, String codigo_entidad,
			String ejercicio_contable, String usuario, String password,
			HashMap mapPAR) {
		creaOPE(e, ConfigSicalwin.APL, ConfigSicalwin.TOBJ_GEN_OPE_GASTO,
				ConfigSicalwin.CMD_CRE, ConfigSicalwin.VER);
		creaSEC(e, ConfigSicalwin.CLI, codigo_organizacion, codigo_entidad,
				ejercicio_contable, usuario, password);
		//creaPAR(e, mapPAR);
		creaInfoGasto(e);
	}

	private void creaInfoGasto(Element e) {
		Element operacion = (Element) doc.createElement("operacion");
		e.appendChild(operacion);

		creaNodo(operacion, "idenTercero", "1");
		
		/*Element prevdef = (Element) doc.createElement("prevdef");
		par.appendChild(prevdef);

		Element tercero = (Element) doc.createElement("tercero");
		l_tercero.appendChild(tercero);

		creaNodo(tercero, "idenTercero", "1");*/
	}
	
	/**
	 * Obtencion de Operacion de Gasto
	 * @return
	 */
	public ArrayList generarOperacionGasto() {

		ArrayList operaciones=new ArrayList();
		try {
			
			initRequest();
			

			HashMap mapPAR = new HashMap();
			// En el mapPAR metemos los valores del <par></par>
			generarOperacionGasto(rootElement, CODIGO_ORG, CODIGO_ENTIDAD, "2011",USUARIO, CLAVE, null);

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
						logger.info("Valores:"+valores);
						//Entidad entidad=new Entidad();
						//entidad.load(valores);
						//entidades.add(entidad);
					}
				}
				else{
					logger.info("Peticion incorrecta");	
					String detalle=getInfo(document,"error");
					logger.error("Detalle Error:"+detalle);
				}
				
			}			
				

		} catch (TransformerException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return operaciones;
	}

	
}
