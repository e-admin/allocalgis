package com.geopista.server.control.web;

import org.mortbay.jaas.JAASUserPrincipal;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.*;
import java.util.Vector;
import java.util.Enumeration;

import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.control.Sesion;
import com.geopista.security.*;
import com.geopista.protocol.control.Sesion;
import com.geopista.server.LoggerHttpServlet;
import com.geopista.server.database.COperacionesControl;
import admcarApp.PasarelaAdmcar;

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
public class GetPerfil extends LoggerHttpServlet
{
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(GetPerfil.class);

    public void doPost (HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
    	super.doPost(request);
        String sAclName = request.getParameter("mensajeXML");
        //System.out.println("sAclName: "+sAclName);
        logger.debug("ACL Solicitado:"+sAclName);

        JAASUserPrincipal userPrincipal = (JAASUserPrincipal)request.getUserPrincipal();
        String  sIdSesion= userPrincipal.getName();
        Sesion sSesion=PasarelaAdmcar.listaSesiones.getSesion(sIdSesion);

        CResultadoOperacion rResultado= getPerfil(sSesion, sAclName);
        response.setContentType ("text/html");

        Writer writer=response.getWriter();
        writer.write (rResultado.buildResponse());
        writer.flush();
        writer.close();

    }
    public CResultadoOperacion getPerfil(Sesion sSesion, String sAclName)
    {
        Vector vParametros=new Vector();
        vParametros.add(0,sAclName);
        CResultadoOperacion rQueryAcl=COperacionesControl.ejecutarQuery("Select IDACL FROM ACL WHERE NAME =?",vParametros);
        if (!rQueryAcl.getResultado())
            return rQueryAcl;
        if (rQueryAcl.getVector().size()<=0)
             return new CResultadoOperacion (false, "ACL no existe");
        //System.out.println("IDENTIFICADOR ACL: "+rQueryAcl.getVector().get(0));

        vParametros = new Vector();
        vParametros.add(0,sSesion.getIdUser());
        vParametros.add(1,rQueryAcl.getVector().get(0));
        vParametros.add(2,sSesion.getIdUser());
        vParametros.add(3,sSesion.getIdUser());
        vParametros.add(4,rQueryAcl.getVector().get(0));
        /*CResultadoOperacion rQueryPermisos=COperacionesControl.ejecutarQuery(
                "select usrgrouperm.def from r_usr_perm,usrgrouperm where r_usr_perm.idacl=? " +
                "and r_usr_perm.userid=? and r_usr_perm.idperm=usrgrouperm.idperm",vParametros);*/
        CResultadoOperacion rQueryPermisos=COperacionesControl.ejecutarQuery(
                "select def from usrgrouperm where idperm in (select idperm from r_group_perm,iusergroupuser " +
                " where r_group_perm.GROUPID = iusergroupuser.GROUPID and iusergroupuser.userid=? and r_group_perm.idacl=?) " +
                " and idperm not in (select idperm from r_usr_perm where r_usr_perm.userid=? and " +
                " r_usr_perm.aplica=0) or idperm in (select idperm from r_usr_perm where r_usr_perm.userid=? and r_usr_perm.idacl=? " +
                " and (r_usr_perm.aplica<>0 or r_usr_perm.aplica is null))",vParametros);
       return rQueryPermisos;
    }


}
