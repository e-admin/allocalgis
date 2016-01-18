/**
 * EnviarCorreoAction.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.licencias.action;

import com.geopista.protocol.licencias.CExpedienteLicencia;
import com.geopista.protocol.licencias.CSolicitudLicencia;


public class EnviarCorreoAction extends DefaultAction{
	
	public EnviarCorreoAction() {
    	super();
    	
    }
    
    /**
     * Ejecuta la acción.
     * @param rctx Contexto de ejecución de la regla
     * @param attContext Atributos con información extra, utilizados dentro de 
     * la ejecución de la regla.
     * @return true si la ejecución termina correctamente, false en caso 
     * contrario.
     * @throws ActionException si ocurre algún error.
     */
    public boolean execute(String attContext,CExpedienteLicencia expedienteLicencia, int tipoLicencia, CSolicitudLicencia solicitud) 
    {
    	
      String correo = solicitud.getPropietario().getDatosNotificacion().getEmail();
      String destinatario=null;
      String asunto=null;
      String mensaje=null;
      String origen=null;
      
      	/*XMLFacade facade=new XMLFacade("<?xml version='1.0' encoding='ISO-8859-1'?><accion>"+attContext+"</accion>");
		NodeIterator it = facade.getNodeIterator("/accion");
		Node node = it.nextNode();
		NodeList mapa;
		Node accion;
		
		
		
      try {
    	  
    	  if(node.hasChildNodes())
  			{
  			mapa=node.getChildNodes();
  	
  			for(int i=0; i<mapa.getLength(); i++)
  			{
  				accion=mapa.item(i);
  				if(accion.getNodeName().equals("destinatario"))
  				{
  					destinatario = XMLFacade.getNodeValue(accion);
  					if(destinatario.equals("SOLICITANTE"))
  						destinatario = solicitud.getPropietario().getDatosNotificacion().getEmail();
  					
  					if(destinatario.equals("REPRESENTANTE"))
  						destinatario =  solicitud.getRepresentante().getDatosNotificacion().getEmail();
  					
  					if(destinatario.equals("PROMOTOR"))
  						destinatario = solicitud.getPromotor().getDatosNotificacion().getEmail();
  					
  				}
  				if(accion.getNodeName().equals("asunto"))
  				{
  					asunto=XMLFacade.getNodeValue(accion);
  				}
  				if(accion.getNodeName().equals("mensaje"))
  				{
  					mensaje= XMLFacade.getNodeValue(accion) + expedienteLicencia.getEstadoString() ;
  				}
  				if(accion.getNodeName().equals("origen"))
  				{
  					origen=XMLFacade.getNodeValue(accion);						
  				}
  			}
  		}
  		
		PostMail enviaCorreo = new PostMail(destinatario,asunto, mensaje, origen);
	} catch (MessagingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
      */
        
        return true;
    }
}
