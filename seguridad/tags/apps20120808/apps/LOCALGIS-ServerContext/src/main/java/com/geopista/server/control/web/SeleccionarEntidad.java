package com.geopista.server.control.web;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.plus.jaas.JAASUserPrincipal;

import admcarApp.PasarelaAdmcar;

import com.geopista.app.administrador.MunicipalityOperations;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.control.ListaSesiones;
import com.geopista.protocol.control.Sesion;
import com.geopista.protocol.net.EnviarSeguro;
import com.geopista.server.LoggerHttpServlet;
import com.geopista.server.database.COperacionesControl;
import com.localgis.server.SessionsContextShared;

import edu.emory.mathcs.backport.java.util.Collections;

/**
 * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
 USA.
 *
 * For more information, contact:
 *
 *
 * www.geopista.com
 *
 *
 */


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 28-may-2004
 * Time: 16:25:39
 */
public class SeleccionarEntidad extends LoggerHttpServlet
{
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(SeleccionarMunicipio.class);

    public void doPost (HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
    	super.doPost(request); 
        JAASUserPrincipal userPrincipal = (JAASUserPrincipal)request.getUserPrincipal();
        String  sIdSesion= userPrincipal.getName();
        PasarelaAdmcar.listaSesiones = (ListaSesiones) SessionsContextShared.getContextShared().getSharedAttribute(this.getServletContext(), "UserSessions");
        Sesion sSesion=PasarelaAdmcar.listaSesiones.getSesion(sIdSesion);
		String sIdEntidad = request.getParameter (EnviarSeguro.idEntidad);
		sSesion.setIdEntidad(sIdEntidad);

        List lista = getMunicipiosEntidad(sIdEntidad);
        MunicipalityOperations municipalityOperations = new MunicipalityOperations();
        List listMunicipios = municipalityOperations.construirListaMunicipios(lista);
        
      //*****************************
        //Ordenacion de los municipios
        //*****************************
        Collections.sort(listMunicipios, new java.util.Comparator(){    		 
            public int compare(Object o1, Object o2) {
            	com.geopista.app.catastro.model.beans.Municipio p1 = (com.geopista.app.catastro.model.beans.Municipio) o1;
            	com.geopista.app.catastro.model.beans.Municipio p2 = (com.geopista.app.catastro.model.beans.Municipio) o2;
               return p1.getNombreOficial().compareToIgnoreCase(p2.getNombreOficial());
            }
 
        });
        
        sSesion.setAlMunicipios(listMunicipios);

        CResultadoOperacion resultado = new CResultadoOperacion(true, sSesion.getIdSesion());

	    Vector vector = new Vector();
	    vector.addElement(sIdEntidad);
	    vector.addElement(lista);
	    resultado.setVector(vector);
        response.setContentType ("text/html");
        Writer writer = response.getWriter();
        writer.write (resultado.buildResponse());
        writer.flush();
	    writer.close();
    }
    
    public List getMunicipiosEntidad(String idEntidad) {
    	List alList = new ArrayList();
	    Vector vParametros=new Vector();
	    vParametros.add(0,idEntidad);
	    CResultadoOperacion rQuery=COperacionesControl.ejecutarQuery("select m.id,m.nombreoficial,astext(\"GEOMETRY\") as geom,m.srid from municipios m, entidades_municipios e where m.id = e.id_municipio and e.id_entidad=?",vParametros);
	    //CResultadoOperacion rQuery=COperacionesControl.ejecutarQuery("select m.id,m.nombreoficial,\"GEOMETRY\" as geom,m.srid from municipios m, entidades_municipios e where m.id = e.id_municipio and e.id_entidad=?",vParametros);
	    try
	    {
	    	if (rQuery.getVector().size()>0)
	    	{
	    		Enumeration enMunicipios = rQuery.getVector().elements();
	    		while (enMunicipios.hasMoreElements()){
	    			Vector vector = (Vector)enMunicipios.nextElement();
	    			Object[] datosMunicipio = vector.toArray();
	    			alList.add(datosMunicipio);
		        }
	      }
	    }catch(Exception e){e.getMessage();}
	    finally{
	    	return alList;
	    }
    }


}
