package com.geopista.app.inventario.sicalwin.operaciones;

import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.geopista.app.inventario.sicalwin.dao.TerceroyCuenta;
import com.geopista.app.inventario.sicalwin.util.ConfigSicalwin;

public class TercerosyCuentas extends OperacionSicalWin{

	/*
	 (*) La estructura del dato devuelto en la etiqueta <detter> es una cadena de longitud variable concatenando los
	valores de la operación en el orden indicado en la siguiente estructura:
	
	TER_ITE Identificador de Tercero
	TER_DOC Número de Documento
	TER_TDC Tipo de Documento
	TER_ALI Alias
	TER_NOM Nombre del Tercero
	TER_DOM Domicilio
	TER_POB Población
	TER_CPO Código Postal
	TER_PRO Provincia
	TER_TLF Teléfono
	TER_FAX FAX
	TER_TIP Tipo de Tercero
	TER_REL Relación con la Entidad
	TER_SEC Sector Industrial
	TER_ACT Actividad Económica
	TER_FPG Forma de Pago por Defecto
	TER_COM Comprobar Compensaciones
	TER_GAS Gastos de Trasferencias
	TER_OBS Observaciones
	TER_EMB Embargado
	TER_DCE Dirección de Correo Electrónico
	TER_NOMBRE Nombre del tercero (cuando se mantienen nombre y apellidos separados)
	TER_APELLIDO1 Apellido 1 del tercero (cuando se mantienen nombre y apellidos separados)
	TER_APELLIDO2 Apellido 2 del tercero (cuando se mantienen nombre y apellidos separados)*/
	
	/*(**) La estructura del dato devuelto en la etiqueta <detbco> es una cadena de longitud variable concatenando los
	valores de la operación en el orden indicado en la siguiente estructura:
	TDB_ITE Identif Tercero
	TDB_ORD Cod Ordinal
	TDB_BCO Banco
	TDB_SUC Sucursal
	TDB_DIG Dígito Control
	TDB_CTA Cuenta
	TDB_TPG Tipo pago
	TDB_SIT Situación
	TDB_BIC BIC
	TDB_PAI País
	TDB_OBS Observaciones
	TDB_FCA Fecha caducidad
	TDB_TIP Tipo de cuenta*/
	
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(TercerosyCuentas.class);
	
	public TercerosyCuentas(){
		
	}
	
	
	private void listarTercerosyCuentas(Element e,
			String codigo_organizacion, String codigo_entidad,
			String ejercicio_contable, String usuario, String password,
			HashMap mapPAR) {
		creaOPE(e, ConfigSicalwin.APL, ConfigSicalwin.TOBJ_TERCEROS_Y_CUENTAS,
				ConfigSicalwin.CMD_LST, ConfigSicalwin.VER);
		creaSEC(e, ConfigSicalwin.CLI, codigo_organizacion, codigo_entidad,
				ejercicio_contable, usuario, password);
		creaFixedPAR(e);
		// creaPAR(e, mapPAR);
	}
	
	/**
	 * Obtencion de Terceros y Cuentas
	 * @return
	 */
	public ArrayList<TerceroyCuenta> getTercerosyCuentas() {
		ArrayList<TerceroyCuenta> cuentas=new ArrayList<TerceroyCuenta>();
		
		try {
			initRequest();
			
			HashMap mapPAR = new HashMap();
			// En el mapPAR metemos los valores del <par></par>
			//mapPAR.put("l_tercero","<l_tercero><tercero><idenTercero>1</idenTercero></tercero></l_tercero>");
			listarTercerosyCuentas(rootElement, CODIGO_ORG, CODIGO_ENTIDAD, "2011",USUARIO, CLAVE, null);
			
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
					String detter=getInfo(document,"detter");
					//logger.info("Detalle OK:"+detalle);
					if (detter!=null){
						String [] valores=detter.split(ConfigSicalwin.SEPARADOR);
						TerceroyCuenta terceroyCuenta=new TerceroyCuenta();
						terceroyCuenta.load(valores);
						cuentas.add(terceroyCuenta);
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
		return cuentas;
	}
}
