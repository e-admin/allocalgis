/**
 * SesionUtils_LCGIII.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.control;

import java.sql.Connection;
import java.util.Enumeration;
import java.util.Vector;

import com.geopista.protocol.CResultadoOperacion;
import com.geopista.security.GeopistaAcl;
import com.geopista.security.GeopistaAclEntry;
import com.geopista.security.GeopistaPermission;
import com.geopista.server.database.COperacionesControl;

public class SesionUtils_LCGIII {

	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(SesionUtils_LCGIII.class);
	
    public static GeopistaAcl getPerfil(ISesion sesion, long idAcl, Connection conn) throws java.lang.Exception
    {
        //Primerto miramos si existe ese ACL
        Vector vParametros=new Vector();
        //vParametros.add(0,new Long(idAcl).toString());
        vParametros.add(0,idAcl);

        CResultadoOperacion rQueryAcl=COperacionesControl.ejecutarNewQuery("Select IDACL,NAME FROM ACL WHERE IDACL =?",vParametros,conn);
        if (!rQueryAcl.getResultado()||rQueryAcl.getVector().size()<=0)
        {
            logger.error("Error al buscar el acl "+ idAcl+ " o no existe"+rQueryAcl.getDescripcion());
            throw new java.security.acl.AclNotFoundException();

        }
        String sNombreAcl=(String)((Vector)rQueryAcl.getVector().get(0)).get(0);
        String sDescripcionAcl=(String)((Vector)rQueryAcl.getVector().get(0)).get(1);
        //String sDescripcionAcl=(String)rQueryAcl.getVector().get(1);
        //CAST8.4
        vParametros = new Vector();
        //vParametros.add(0,getIdUser());
        //vParametros.add(1,new Long(idAcl).toString());
        vParametros.add(0,Integer.parseInt(sesion.getIdUser(),10));
        vParametros.add(1,idAcl);
        //vParametros.add(2,getIdUser());
        //vParametros.add(3,new Long(idAcl).toString());
        vParametros.add(2,Integer.parseInt(sesion.getIdUser(),10));
        vParametros.add(3,idAcl);
        //vParametros.add(4,getIdUser());
        //vParametros.add(5,new Long(idAcl).toString());
        vParametros.add(4,Integer.parseInt(sesion.getIdUser(),10));
        vParametros.add(5,idAcl);

        CResultadoOperacion rQueryPermisos=COperacionesControl.ejecutarNewQuery(
                " select def from usrgrouperm "+
                " where idperm in (select idperm from r_group_perm,iusergroupuser where r_group_perm.GROUPID = iusergroupuser.GROUPID "+
                " and iusergroupuser.userid=? and r_group_perm.idacl=?) and idperm not in (select idperm from r_usr_perm where r_usr_perm.userid=? and r_usr_perm.aplica=0 "+
                " and r_usr_perm.idAcl=? ) "+
                " or idperm in (select idperm from r_usr_perm where r_usr_perm.userid=? and r_usr_perm.idacl=? and (r_usr_perm.aplica<>0 or r_usr_perm.aplica is null)) ",
                vParametros,conn);
        GeopistaAcl geopistaAcl = new GeopistaAcl(sesion.getUserPrincipal(),sNombreAcl,sDescripcionAcl);
        GeopistaAclEntry entry= new GeopistaAclEntry(sesion.getUserPrincipal());

        if (rQueryPermisos.getVector()!=null&&rQueryPermisos.getVector().size()>0)
        {
            logger.debug("El usuario "+sesion.getUserPrincipal().getName()+ " tiene permisos "+rQueryPermisos.getVector().size()+" para el acl "+geopistaAcl.getAclName());
      	    for (Enumeration e = rQueryPermisos.getVector().elements(); e.hasMoreElements();) {
			    GeopistaPermission auxPermiso= new GeopistaPermission((String)e.nextElement());
                entry.addPermission(auxPermiso);
            }
        }
        else
            logger.debug("El usuario "+sesion.getUserPrincipal().getName()+ " NO tiene permisos para el acl "+geopistaAcl.getAclName()+" Descripcion:"+geopistaAcl.getAclDescripcion());
        geopistaAcl.addEntry(sesion.getUserPrincipal(),entry);
        return geopistaAcl;
	}
    
    public static GeopistaAcl getPerfil(ISesion sesion, long idAcl) throws java.lang.Exception
    {
        //Primerto miramos si existe ese ACL
        Vector vParametros=new Vector();
        vParametros.add(0,new Long(idAcl));
        CResultadoOperacion rQueryAcl=COperacionesControl.ejecutarQuery("Select IDACL,NAME FROM ACL WHERE IDACL =?",vParametros);
        if (!rQueryAcl.getResultado()||rQueryAcl.getVector().size()<=0)
        {
            logger.error("Error al buscar el acl "+ idAcl+ " o no existe"+rQueryAcl.getDescripcion());
            throw new java.security.acl.AclNotFoundException();

        }
        //String sNombreAcl=(String)rQueryAcl.getVector().get(0);
        //String sDescripcionAcl=(String)rQueryAcl.getVector().get(1);
        
        String sNombreAcl=(String)((Vector)rQueryAcl.getVector().get(0)).get(0);
        String sDescripcionAcl=(String)((Vector)rQueryAcl.getVector().get(0)).get(1);
        
        vParametros = new Vector();
        vParametros.add(0,new Long(sesion.getIdUser()));
        vParametros.add(1,new Long(idAcl));
        vParametros.add(2,new Long(sesion.getIdUser()));
        vParametros.add(3,new Long(idAcl));
        vParametros.add(4,new Long(sesion.getIdUser()));
        vParametros.add(5,new Long(idAcl));
        CResultadoOperacion rQueryPermisos=COperacionesControl.ejecutarQuery(
                " select def from usrgrouperm "+
                " where idperm in (select idperm from r_group_perm,iusergroupuser where r_group_perm.GROUPID = iusergroupuser.GROUPID "+
                " and iusergroupuser.userid=? and r_group_perm.idacl=?) and idperm not in (select idperm from r_usr_perm where r_usr_perm.userid=? and r_usr_perm.aplica=0 "+
                " and r_usr_perm.idAcl=? ) "+
                " or idperm in (select idperm from r_usr_perm where r_usr_perm.userid=? and r_usr_perm.idacl=? and (r_usr_perm.aplica<>0 or r_usr_perm.aplica is null)) ",
                vParametros);
        GeopistaAcl geopistaAcl = new GeopistaAcl(sesion.getUserPrincipal(),sNombreAcl,sDescripcionAcl);
        GeopistaAclEntry entry= new GeopistaAclEntry(sesion.getUserPrincipal());

        if (rQueryPermisos.getVector()!=null&&rQueryPermisos.getVector().size()>0)
        {
            logger.debug("El usuario "+sesion.getUserPrincipal().getName()+ " tiene permisos "+rQueryPermisos.getVector().size()+" para el acl "+geopistaAcl.getAclName());
      	    for (Enumeration e = rQueryPermisos.getVector().elements(); e.hasMoreElements();) {
			    GeopistaPermission auxPermiso= new GeopistaPermission((String)e.nextElement());
                entry.addPermission(auxPermiso);
            }
        }
        else
            logger.debug("El usuario "+sesion.getUserPrincipal().getName()+ " NO tiene permisos para el acl "+geopistaAcl.getAclName()+" Descripcion:"+geopistaAcl.getAclDescripcion());
        geopistaAcl.addEntry(sesion.getUserPrincipal(),entry);
        return geopistaAcl;
	}
	
}
