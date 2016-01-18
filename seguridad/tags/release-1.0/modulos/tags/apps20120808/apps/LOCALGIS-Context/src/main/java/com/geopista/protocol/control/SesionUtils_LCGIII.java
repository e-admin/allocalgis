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
        vParametros.add(0,new Long(idAcl).toString());
        CResultadoOperacion rQueryAcl=COperacionesControl.ejecutarNewQuery("Select IDACL,NAME FROM ACL WHERE IDACL =?",vParametros,conn);
        if (!rQueryAcl.getResultado()||rQueryAcl.getVector().size()<=0)
        {
            logger.error("Error al buscar el acl "+ idAcl+ " o no existe"+rQueryAcl.getDescripcion());
            throw new java.security.acl.AclNotFoundException();

        }
        String sNombreAcl=(String)((Vector)rQueryAcl.getVector().get(0)).get(0);
        String sDescripcionAcl=(String)((Vector)rQueryAcl.getVector().get(0)).get(1);
        //String sDescripcionAcl=(String)rQueryAcl.getVector().get(1);
        vParametros = new Vector();
        vParametros.add(0,sesion.getIdUser());
        vParametros.add(1,new Long(idAcl).toString());
        vParametros.add(2,sesion.getIdUser());
        vParametros.add(3,new Long(idAcl).toString());
        vParametros.add(4,sesion.getIdUser());
        vParametros.add(5,new Long(idAcl).toString());
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
        vParametros.add(0,new Long(idAcl).toString());
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
        vParametros.add(0,sesion.getIdUser());
        vParametros.add(1,new Long(idAcl).toString());
        vParametros.add(2,sesion.getIdUser());
        vParametros.add(3,new Long(idAcl).toString());
        vParametros.add(4,sesion.getIdUser());
        vParametros.add(5,new Long(idAcl).toString());
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
