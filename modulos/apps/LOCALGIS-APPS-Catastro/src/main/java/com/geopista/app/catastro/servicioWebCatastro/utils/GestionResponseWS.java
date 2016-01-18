/**
 * GestionResponseWS.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.servicioWebCatastro.utils;

import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import com.geopista.app.catastro.servicioWebCatastro.beans.DatosErrorWS;
import com.geopista.app.catastro.servicioWebCatastro.beans.DatosWSResponseBean;
import com.geopista.app.catastro.servicioWebCatastro.beans.ErrorWSBean;
import com.geopista.app.catastro.servicioWebCatastro.beans.UnidadErrorElementoWSBean;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.I18N;

public class GestionResponseWS {
	
	private static final String EXPTE = "EXPTE";
	private static final String FINCA = "FINCA";
	private static final String CONST = "CONST";
	private static final String UCONS = "UCONS";
	private static final String SUELO = "SUELO";
	private static final String CULTI = "CULTI";
	private static final String REPTO = "REPTO";
	private static final String BIENI = "BIENI";
	private static final String DRCHO = "DRCHO";
	private static final String REPRE = "REPRE";
	private static final String PRTNT = "PRTNT";
	private static final String FCARG = "FCARG";
	private static final String ENVIO = "ENVIO";
	
	
	public GestionResponseWS(){
		Locale loc=I18N.getLocaleAsObject();         
		ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.catastro.servicioWebCatastro.language.ComunicacionCatastroOVCi18n",loc,this.getClass().getClassLoader());
		I18N.plugInsResourceBundle.put("ComunicacionWSCatastro",bundle);
	}
	
	public static String gestionResponseExpediente(DatosWSResponseBean response, ApplicationContext aplicacion){
		
		StringBuffer mensaje = new StringBuffer();
		//MessageDialog messageDialog = new MessageDialog(aplicacion, "comunicacion.catastro.creacionExpediente.title");
		
		if(response.getRespuesta().getLstUnidadError() != null && 
				!response.getRespuesta().getLstUnidadError().isEmpty()){
			
			mensaje.append("<b>");
			mensaje.append(I18N.get("ComunicacionWSCatastro", "comunicacion.catastro.operacion.incidencia")+"<br><br>");
			mensaje.append("</b>");
			
			
			
			for(int i=0; i<response.getRespuesta().getLstUnidadError().size(); i++){
				UnidadErrorElementoWSBean unidadError = response.getRespuesta().getLstUnidadError().get(i);
				mensaje.append("<font color= \"red\">");
				mensaje.append("<b>" + I18N.get("ComunicacionWSCatastro", "comunicacion.catastro.tipo") +" </b>" +
							gestionTipoError(unidadError)+"<br>");
				
				mensaje.append("<b>" + I18N.get("ComunicacionWSCatastro", "comunicacion.catastro.origen")+"</b><br>");
				
				mensaje.append("<b>" + I18N.get("ComunicacionWSCatastro", "comunicacion.catastro.listaErrores")+"</b><br>");

				mensaje.append( gestionError(unidadError.getLstErrores()) + "<br><br>");
				mensaje.append("</font>");			
				
			}
		}
		else{
			mensaje.append("<font color= \"green\"><b>");
			mensaje.append(I18N.get("ComunicacionWSCatastro", "comunicacion.catastro.operacion.correcta")+"<br><br>");
			mensaje.append("</b></font>");
		}
		
		return mensaje.toString();
	}
	
	public static String gestionErrorComunicacion(DatosErrorWS datosError){
		
		StringBuffer mensaje = new StringBuffer();
	
		mensaje.append("<font color= \"red\"><b>");
     	mensaje.append(I18N.get("ComunicacionWSCatastro", "comunicacion.catastro.operacion.incidencia")+"<br><br>");
     	mensaje.append(datosError.getMensaje());
     	mensaje.append("</b></font>");

		return mensaje.toString();
	}
	
	private static String gestionTipoError( UnidadErrorElementoWSBean unidadError){
		String tipo = ""; 
		
		if(unidadError.getIdentificador().getTipo().equals(EXPTE)){
			tipo = I18N.get("ComunicacionWSCatastro", "comunicacion.catastro.tipo.elemento.expediente");
		}
		else if(unidadError.getIdentificador().getTipo().equals(FINCA)){
			tipo = I18N.get("ComunicacionWSCatastro", "comunicacion.catastro.tipo.elemento.finca");
		}
		else if(unidadError.getIdentificador().getTipo().equals(CONST)){
			tipo = I18N.get("ComunicacionWSCatastro", "comunicacion.catastro.tipo.elemento.construccion");	
		}
		else if(unidadError.getIdentificador().getTipo().equals(UCONS)){
			tipo = I18N.get("ComunicacionWSCatastro", "comunicacion.catastro.tipo.elemento.unidadConstructiva");
		}
		else if(unidadError.getIdentificador().getTipo().equals(SUELO)){
			tipo = I18N.get("ComunicacionWSCatastro", "comunicacion.catastro.tipo.elemento.suelo");
		}
		else if(unidadError.getIdentificador().getTipo().equals(CULTI)){
			tipo = I18N.get("ComunicacionWSCatastro", "comunicacion.catastro.tipo.elemento.cultivo");
		}
		else if(unidadError.getIdentificador().getTipo().equals(REPTO)){
			tipo = I18N.get("ComunicacionWSCatastro", "comunicacion.catastro.tipo.elemento.repto");
		}
		else if(unidadError.getIdentificador().getTipo().equals(BIENI)){
			tipo = I18N.get("ComunicacionWSCatastro", "comunicacion.catastro.tipo.elemento.bienInmueble");			
		}
		else if(unidadError.getIdentificador().getTipo().equals(DRCHO)){
			tipo = I18N.get("ComunicacionWSCatastro", "comunicacion.catastro.tipo.elemento.derecho");
		}
		else if(unidadError.getIdentificador().getTipo().equals(REPRE)){
			tipo = I18N.get("ComunicacionWSCatastro", "comunicacion.catastro.tipo.elemento.representante");
		}
		else if(unidadError.getIdentificador().getTipo().equals(PRTNT)){
			tipo = I18N.get("ComunicacionWSCatastro", "comunicacion.catastro.tipo.elemento.protocoloNotarial");				
		}
		else if(unidadError.getIdentificador().getTipo().equals(FCARG)){
			tipo = I18N.get("ComunicacionWSCatastro", "comunicacion.catastro.tipo.elemento.fcargo");
		}
		else if(unidadError.getIdentificador().getTipo().equals(ENVIO)){
			tipo = I18N.get("ComunicacionWSCatastro", "comunicacion.catastro.tipo.elemento.envio");
		}
		return tipo;
	}
	
	private static StringBuffer  gestionError( ArrayList <ErrorWSBean>lstErrores){
		
		StringBuffer errorStr = new StringBuffer();
		
		for(int i=0; i< lstErrores.size(); i++){
			ErrorWSBean error = lstErrores.get(i);
			
			String codigo = "&nbsp;&nbsp;&nbsp;&nbsp;<b>" + I18N.get("ComunicacionWSCatastro", "comunicacion.catastro.resultado.codigoError")+ " </b>" + error.getCodigo() + "<br>";
			String descripcion = "&nbsp;&nbsp;&nbsp;&nbsp;<b>" + I18N.get("ComunicacionWSCatastro", "comunicacion.catastro.resultado.decripcionError") + " </b>" + error.getDescripcion() + "<br>";
			String tipo = "&nbsp;&nbsp;&nbsp;&nbsp;<b>" + I18N.get("ComunicacionWSCatastro", "comunicacion.catastro.resultado.TipoError") + " </b>" + error.getTipo() + "<br>";
			errorStr.append("<br>");
			errorStr.append( codigo);
			errorStr.append(descripcion);
			errorStr.append(tipo);
			errorStr.append("<br>");

		}
		
		return errorStr;
	}
}
