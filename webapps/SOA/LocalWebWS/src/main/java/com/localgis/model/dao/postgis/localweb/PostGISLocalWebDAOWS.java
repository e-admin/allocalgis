/**
 * PostGISLocalWebDAOWS.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.model.dao.postgis.localweb;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.mortbay.util.Credential;
import com.localgis.exception.AclNoExistenteException;
import com.localgis.exception.NoPermisoException;
import com.localgis.exception.PasswordNoValidoException;
import com.localgis.exception.PoiExistenteException;
import com.localgis.exception.PoiNoExistenteException;
import com.localgis.exception.SubtipoNoExistenteException;
import com.localgis.exception.TipoNoExistenteException;
import com.localgis.exception.UsuarioNoExistenteException;
import com.localgis.model.dao.ILocalWebDAOWS;
import com.localgis.model.ot.CapaOT;
import com.localgis.model.ot.PoiOT;
import com.localgis.model.ot.SubtipoOT;
import com.localgis.util.ConnectionUtilities;
import com.localgis.util.DomainNode;
import com.localgis.util.EncriptarPassword;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

public class PostGISLocalWebDAOWS implements ILocalWebDAOWS
{
    
    public PostGISLocalWebDAOWS() throws NamingException
    {
        Context initCtx = new InitialContext( );
    }

    public Collection obtenerListaCapas(Connection connection, int idMunicipio) throws SQLException, ParseException {

    	ArrayList listaCapas = new ArrayList();
    	CapaOT capaoOT = null;
    	SubtipoOT subtipoOT = null;
    	String locale = System.getProperties().get("user.language") + "_" + System.getProperties().get("user.language").toString().toUpperCase();
    	
    	Hashtable lista = obtenerListaDominios(connection, "Pois", idMunicipio);
    	
    	for (Enumeration e=lista.elements();e.hasMoreElements();)
        {
            DomainNode auxDomainNode=(DomainNode) e.nextElement();
            for (Enumeration enumTipoDomain = auxDomainNode.getlHijos().gethDom().elements();enumTipoDomain.hasMoreElements();){
            	
            	DomainNode tipoDomainNode=(DomainNode) enumTipoDomain.nextElement();
            	capaoOT = new CapaOT();
            	capaoOT.setPatron(tipoDomainNode.getPatron());
            	
            	if (tipoDomainNode.gethDict().get(locale)!=null)
            		capaoOT.setNombreCapa((String)tipoDomainNode.gethDict().get(locale));
            	else
            		capaoOT.setNombreCapa((String)tipoDomainNode.gethDict().elements().nextElement());
            	
            	for (Enumeration enumSubtipoDomain = tipoDomainNode.getlHijos().gethDom().elements();enumSubtipoDomain.hasMoreElements();){            		
            		subtipoOT = new SubtipoOT();
            		DomainNode subtipoDomainNode=(DomainNode) enumSubtipoDomain.nextElement();
            		subtipoOT.setPatron(subtipoDomainNode.getPatron());
            		
            		if (subtipoDomainNode.gethDict().get(locale)!=null)
            			subtipoOT.setNombreSubtipo((String)subtipoDomainNode.gethDict().get(locale));
            		else
            			subtipoOT.setNombreSubtipo((String)subtipoDomainNode.gethDict().elements().nextElement());
            		
            		capaoOT.getSubtipos().add(subtipoOT);
            	}
            	listaCapas.add(capaoOT);            	
            }
        }
    	
    	return listaCapas;

    }

	
    public String bajaPOI(Connection connection, int idContenido) throws SQLException,
    		ParseException, PoiNoExistenteException{

    	if(!comprobarExistenciaPoi(connection, idContenido))
    		throw new PoiNoExistenteException("El poi con id "+ idContenido + " no existe");

    	ResultSet rs = null;
    	PreparedStatement preparedStatement = null;

    	try
    	{
    		//escribir query 
    		String senteciaSQL = "delete from pois where id_contenido = ?";
    		preparedStatement = connection.prepareStatement(senteciaSQL);  
    		preparedStatement.setInt(1, idContenido);

    		preparedStatement.executeUpdate();

    	}finally
    	{
    		ConnectionUtilities.closeConnection(null, preparedStatement, null);
    	}

    	String respuesta = "Se ha eliminado correctamente el poi con id " + idContenido;
    	return respuesta;
    }

	public String verPlano(Connection connection, String nombrePlano,
			double coordX, double coordY, double alturaPlano,
			double anchoPlano, double escala) throws SQLException,
			ParseException {
		ResultSet rs = null;
    	PreparedStatement preparedStatement = null;

    	//escribir query 
    	String senteciaSQL = "insert poi";

    	/*try
    	{

    		preparedStatement = connection.prepareStatement(senteciaSQL);  
    		rs = preparedStatement.executeQuery();

    		while(rs.next())
    		{
    			capaot = new CapaOT();
    			capaot.setIdCapa(rs.getInt("id_layer"));
    			capaot.setNombreCapa(rs.getString("name"));

    			listaCapas.add(capaot);


    		}
    	}finally
    	{
    		ConnectionUtilities.closeConnection(null, preparedStatement, rs);
    	}*/

    	String urlPlano = "http://geopista.grupotecopy.es:8080/guiaurbana/wms?" +
    			"VERSION=1.1.1&REQUEST=GetMap&LAYERS=Parcelas.34083,Tramos de calle.34083," +
    			"Calles.34083,Números de policía.34083&STYLES=default:parcelas,default:" +
    			"tramosvia,default:vias,default:numeros_policia&SRS=EPSG:23030" +
    			"&BBOX=383201.7602868919,4701117.68935343,402705.57137778815," +
    			"4723005.77322911&WIDTH=450&HEIGHT=450&FORMAT=image/png" +
    			"&EXCEPTIONS=application/vnd.ogc.se_inimage";
    	
    	return null;
	}
	
	public Hashtable obtenerListaDominios(Connection connection, String dominioPadre, int idMunicipio) throws SQLException
	{
		Hashtable listaDominios = new Hashtable();

			DomainNode dominio = obtenerDominioPadre(connection, dominioPadre, idMunicipio);

			if(dominio!=null && dominio.getIdNode()!=null){
				listaDominios.put(dominio.getIdNode(),dominio);    
			}

		return listaDominios;       
	}

	public DomainNode obtenerDominiosHijo(Connection connection, DomainNode dominioPadre, int idMunicipio) throws SQLException
	{		
		boolean hayDatos = false;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null; 

		try {

			preparedStatement = connection.prepareStatement("select domains.id as id_domain ,domainnodes.id as id_node, domainnodes.type as type," +
					"domainnodes.pattern as pattern, dictionary.locale as locale, dictionary.id_vocablo as id_descripcion," +
					"dictionary.traduccion as traduccion, domainnodes.parentdomain as parentdomain from domains,domainnodes,dictionary " +
					"where parentdomain=? and " +
					"domainnodes.id_domain=domains.id and domainnodes.id_description= dictionary.id_vocablo " +
			"and domainnodes.id_municipio=? order by domainnodes.id");
			preparedStatement.setString(1,dominioPadre.getIdNode());
			preparedStatement.setInt(2,idMunicipio);

			rs =preparedStatement.executeQuery();
			while (rs.next()){

				hayDatos = true;
				DomainNode childDomainNode=null;
				String newChildIdDomainNode=rs.getString("id_node");
				if ((childDomainNode==null) || (!childDomainNode.getIdNode().equals(newChildIdDomainNode)))
				{
					childDomainNode= new DomainNode(rs.getString("id_node"),rs.getString("id_descripcion"),
							rs.getInt("type"), new Integer(rs.getInt("parentdomain")).toString(),
							new Integer(idMunicipio).toString(),
							rs.getString("id_domain"),rs.getString("pattern"));
					if (rs.getString("id_descripcion")!=null)
						childDomainNode.addTerm(rs.getString("locale"), rs.getString("traduccion"));

					childDomainNode = obtenerDominiosHijo(connection, childDomainNode, idMunicipio);
					dominioPadre.addHijo(childDomainNode);
				}
				else
				{
					childDomainNode.addTerm(rs.getString("locale"), rs.getString("traduccion"));
				}

			}

			if(hayDatos)return dominioPadre;

			preparedStatement = connection.prepareStatement("select domains.id as id_domain ,domainnodes.id as id_node, domainnodes.type as type," +
					"domainnodes.pattern as pattern, dictionary.locale as locale, dictionary.id_vocablo as id_descripcion," +
					"dictionary.traduccion as traduccion, domainnodes.parentdomain as parentdomain from domains,domainnodes,dictionary " +
					"where parentdomain=? and " +
					"domainnodes.id_domain=domains.id and domainnodes.id_description= dictionary.id_vocablo " +
			"and domainnodes.id_municipio is null order by domainnodes.id");

			preparedStatement.setString(1,dominioPadre.getIdNode());
			rs =preparedStatement.executeQuery();
			while (rs.next()){

				DomainNode childDomainNode=null;
				String newChildIdDomainNode=rs.getString("id_node");
				if ((childDomainNode==null) || (!childDomainNode.getIdNode().equals(newChildIdDomainNode)))
				{
					childDomainNode= new DomainNode(rs.getString("id_node"),rs.getString("id_descripcion"),
							rs.getInt("type"), new Integer(rs.getInt("parentdomain")).toString(),
							new Integer(idMunicipio).toString(),
							rs.getString("id_domain"),rs.getString("pattern"));
					if (rs.getString("id_descripcion")!=null)
						childDomainNode.addTerm(rs.getString("locale"), rs.getString("traduccion"));

					childDomainNode = obtenerDominiosHijo(connection, childDomainNode, idMunicipio);
					dominioPadre.addHijo(childDomainNode);
				}
				else
				{
					childDomainNode.addTerm(rs.getString("locale"), rs.getString("traduccion"));
				}

			}
			return dominioPadre;

		} //catch (SQLException e) {
		//	e.printStackTrace();
		//}
		finally
		{
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
		}

		//return null;
	}
	
	public DomainNode obtenerDominioPadre(Connection connection, String dominio, int idMunicipio) throws SQLException{

		DomainNode oldDomainNode=null;
		PreparedStatement preparedStatement = null;
		ResultSet  rs = null;

		try {

			String senteciaSQL = "select domains.id as id_domain,domainnodes.id as " +
			"id_node, domainnodes.pattern as pattern,domainnodes.id_description " +
			"as id_descripcion, dictionary.locale as locale, dictionary.traduccion " +
			"as traduccion from domains,domainnodes,dictionary where upper(domains.name)=upper(?) " +
			"and domainnodes.type=? and domainnodes.id_domain=domains.id and " +
			"domainnodes.id_description= dictionary.id_vocablo and domainnodes.id_municipio =? " +
			"order by domainnodes.id";

			preparedStatement = connection.prepareStatement(senteciaSQL);
			preparedStatement.setString(1,dominio);
			//The Type of Tree Domain is 2
			preparedStatement.setInt(2,2);
			preparedStatement.setString(3, null);
			rs =preparedStatement.executeQuery();
			
			boolean hayDatos=true;
			if (!rs.next())
			{		
				preparedStatement = connection.prepareStatement("select domains.id as id_domain ,domainnodes.id as id_node, domainnodes.parentdomain as parentdomain," +
						"domainnodes.pattern as pattern, dictionary.locale as locale, dictionary.id_vocablo as id_descripcion, " +
						"dictionary.traduccion as traduccion from domains,domainnodes,dictionary " +
						"where upper(domains.name)=upper(?) and domainnodes.type=? and " +
						"domainnodes.id_domain=domains.id and domainnodes.id_description= dictionary.id_vocablo " +
						"and domainnodes.id_municipio is null order by domainnodes.id");
				
				preparedStatement.setString(1,dominio);
				//The Type of Tree Domain is 2
				preparedStatement.setInt(2,2);
				rs =preparedStatement.executeQuery();
				
				if (!rs.next())
				{
					hayDatos=false;					
				}
			}
			if (hayDatos)
			{ 
				do{
					String newIdDomainNode=rs.getString("id_node");
					if ((oldDomainNode==null) || (!oldDomainNode.getIdNode().equals(newIdDomainNode)))
					{
						oldDomainNode= new DomainNode(rs.getString("id_node"),rs.getString("id_descripcion"),
								//com.geopista.feature.Domain.CODEDENTRY, null,
								2, rs.getString("parentdomain"),
								new Integer(idMunicipio).toString(),
								rs.getString("id_domain"),rs.getString("pattern"));
						if (rs.getString("id_descripcion")!=null)
							oldDomainNode.addTerm(rs.getString("locale"), rs.getString("traduccion"));

						oldDomainNode = obtenerDominiosHijo(connection, oldDomainNode, idMunicipio);
					}
					else
					{
						oldDomainNode.addTerm(rs.getString("locale"), rs.getString("traduccion"));
					}
				}while (rs.next());
			}

		}// catch (Exception e) {
		//	StringWriter sw=new StringWriter();
		//	PrintWriter pw=new PrintWriter(sw);
		//	e.printStackTrace(pw);
			
		//}
		finally{
			
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
		}

		return oldDomainNode;
	}
	
	public String altaPOI(Connection connection, PoiOT poiOT)
			throws SQLException, ParseException, PoiExistenteException, TipoNoExistenteException, SubtipoNoExistenteException {
				
		Hashtable lista = obtenerListaDominios(connection, "Pois", poiOT.getIdMunicpio());
		if (!comprobarTipo(lista, poiOT.getTipo()))
			throw new TipoNoExistenteException("El nombre de capa " + poiOT.getTipo() + " no existe");
		
		if (!comprobarSubtipo(lista, poiOT.getSubtipo()))
			throw new SubtipoNoExistenteException("El subtipo " + poiOT.getSubtipo() + " no existe");
		
		if(comprobarExistenciaPoi(connection, poiOT.getIdContenido()))
			throw new PoiExistenteException("El poi con id "+ poiOT.getIdContenido() + " ya existe");
		
        PreparedStatement preparedStatement = null;

        String senteciaSQL = "insert into pois " +
        		" (id,tipo, subtipo, id_contenido, url_contenido, direccion, id_municipio, \"GEOMETRY\") " +
        		" values (nextval('seq_pois'),?,?,?,?,?,?,GeometryFromText('POINT(" + poiOT.getCoordX() + " " + poiOT.getCoordY() + ")',23030))";

        try
        {
            preparedStatement = connection.prepareStatement(senteciaSQL);
            preparedStatement.setString(1, poiOT.getTipo() );
            preparedStatement.setString(2, poiOT.getSubtipo());
            preparedStatement.setInt(3, poiOT.getIdContenido());
            preparedStatement.setString(4, poiOT.getUrlContenido());
            preparedStatement.setString(5, poiOT.getDireccion());
            preparedStatement.setInt(6, poiOT.getIdMunicpio());
    
            preparedStatement.executeUpdate();
            
        }finally
        {
            ConnectionUtilities.closeConnection(null, preparedStatement, null);
        }

		String response = "El punto de interés con idContenido " + poiOT.getIdContenido() + " se ha insertado con éxito";	
		
		return response;
	}

	private boolean comprobarSubtipo(Hashtable lista, String subtipo) {
		for (Enumeration e=lista.elements();e.hasMoreElements();)
        {
            DomainNode auxDomainNode=(DomainNode) e.nextElement();
            for (Enumeration enumTipoDomain = auxDomainNode.getlHijos().gethDom().elements();enumTipoDomain.hasMoreElements();){
            	
            	DomainNode tipoDomainNode=(DomainNode) enumTipoDomain.nextElement();
            	
            	for (Enumeration enumSubtipoDomain = tipoDomainNode.getlHijos().gethDom().elements();enumSubtipoDomain.hasMoreElements();){            		
            		
            		DomainNode subtipoDomainNode=(DomainNode) enumSubtipoDomain.nextElement();
            		if(subtipo.equals(subtipoDomainNode.getPatron())) return true;
            	}
            	     	
            }
        }
		return false;
	}

	private boolean comprobarTipo(Hashtable lista, String tipo) {

		for (Enumeration e=lista.elements();e.hasMoreElements();)
        {
            DomainNode auxDomainNode=(DomainNode) e.nextElement();
            for (Enumeration enumTipoDomain = auxDomainNode.getlHijos().gethDom().elements();enumTipoDomain.hasMoreElements();){
            	//Tenemos el Dominio con cada tipo
            	DomainNode tipoDomainNode=(DomainNode) enumTipoDomain.nextElement();            	
            	if(tipo.equals(tipoDomainNode.getPatron())) return true;         	
            }
        }
		return false;
	}

	public boolean comprobarExistenciaPoi(Connection connection, int id_contenido) throws SQLException
	{
		ResultSet rs = null;
		PreparedStatement preparedStatement = null;

        String senteciaSQL = "select id from pois where id_contenido = ?";

        try
        {
            preparedStatement = connection.prepareStatement(senteciaSQL);
            preparedStatement.setInt(1, id_contenido );
            
    
            rs = preparedStatement.executeQuery();
            
            if(rs.next()) return true;
            else return false;
            	
            
        }finally
        {
            ConnectionUtilities.closeConnection(null, preparedStatement, null);
        }
	}
	
	public String comprobarPermiso(Connection connection, Integer idUsuario, String constPermiso, String constAcl) throws SQLException, UsuarioNoExistenteException, PasswordNoValidoException, NumberFormatException, AclNoExistenteException, NoPermisoException {

		ArrayList listaPermisos = null;

		//int idUsuario = obtenerUsuario(connection, nombreUsuario, passwordUsuario, idMunicipio);

		if (idUsuario!=null){			
			listaPermisos = obtenerPermisosUsuario(connection, idUsuario, constAcl);
		}

		for (Iterator iterPermisos = listaPermisos.iterator(); iterPermisos.hasNext();){
			String permiso = (String)iterPermisos.next();
			if(constPermiso.equals(permiso))
				return new String("El usuario tiene el permiso " + constPermiso);
		}       

		throw new NoPermisoException("El usuario no tiene el permiso " + constPermiso);
	}
	
	public ArrayList obtenerPermisosUsuario(Connection connection, Integer idUsuario, String acl) throws SQLException, AclNoExistenteException {

		int idACL = 0; 
		ArrayList listaPermisos = new ArrayList();

		ResultSet rs = null;
		PreparedStatement preparedStatement = null;

		try{

			String senteciaSQL = "Select IDACL FROM ACL WHERE NAME =?";

			preparedStatement = connection.prepareStatement(senteciaSQL);
			preparedStatement.setString(1, acl );
			rs = preparedStatement.executeQuery();

			if(rs.next()){
				idACL = rs.getInt("idacl");
			}
			else
				throw new AclNoExistenteException("ACL " + acl + " no existe");

			senteciaSQL = "select def from usrgrouperm where idperm in (select idperm from r_group_perm,iusergroupuser " +
			" where r_group_perm.GROUPID = iusergroupuser.GROUPID and iusergroupuser.userid=? and r_group_perm.idacl=?) " +
			" and idperm not in (select idperm from r_usr_perm where r_usr_perm.userid=? and " +
			" r_usr_perm.aplica=0) or idperm in (select idperm from r_usr_perm where r_usr_perm.userid=? and r_usr_perm.idacl=? " +
			" and (r_usr_perm.aplica<>0 or r_usr_perm.aplica is null))";

			preparedStatement = connection.prepareStatement(senteciaSQL);
			preparedStatement.setInt(1, idUsuario.intValue());
			preparedStatement.setInt(2, idACL );
			preparedStatement.setInt(3, idUsuario.intValue());
			preparedStatement.setInt(4, idUsuario.intValue());
			preparedStatement.setInt(5, idACL );
			rs = preparedStatement.executeQuery();

			while (rs.next()) {
				String permiso = rs.getString("def");
				listaPermisos.add(permiso);
			}        

		}finally
		{
			ConnectionUtilities.closeConnection(null, preparedStatement, null);
		}

		return listaPermisos;
	}
	
	public Integer obtenerUsuario(Connection connection, String nombreUsuario, String passwordUsuario) throws PasswordNoValidoException, SQLException, UsuarioNoExistenteException{
		
		String password = null;
		Integer idUsuario = null;
		
		ResultSet rs = null;
		PreparedStatement preparedStatement = null;

        String senteciaSQL = "select password,id from IUSERUSERHDR where " +
        		//"upper(name)=? and (id_municipio=? or id_municipio is null)";
        	"upper(name)=upper(?)";

        try
        {
            preparedStatement = connection.prepareStatement(senteciaSQL);
            preparedStatement.setString(1, nombreUsuario );
            /*if(idMunicipio!=null)
            	preparedStatement.setInt(2, idMunicipio.intValue());
            else
            	preparedStatement.setInt(2, 0);*/
            
            rs = preparedStatement.executeQuery();
            
            if(rs.next()){
            	idUsuario = new Integer(rs.getInt("id"));
            	password = rs.getString("password");
            }
            else {
            	throw new UsuarioNoExistenteException("El usuario " + nombreUsuario + " no existe");
            }
            
            String passwordDesencriptado = null;
            try
            {
		    	passwordDesencriptado = (new EncriptarPassword()).undoEncrip(password);
            }catch(Exception e)
            {
                passwordDesencriptado = password;
            }
            
            Credential credential = Credential.getCredential(passwordDesencriptado);

			if(!credential.check(passwordUsuario))
				throw new PasswordNoValidoException("El password " + passwordUsuario + " no es válido");
			
        }finally
        {
            ConnectionUtilities.closeConnection(null, preparedStatement, null);
        }
		
		return idUsuario;
	}

}
