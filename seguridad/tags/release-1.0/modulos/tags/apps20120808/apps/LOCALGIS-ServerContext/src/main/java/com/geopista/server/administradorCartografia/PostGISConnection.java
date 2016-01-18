package com.geopista.server.administradorCartografia;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Pattern;


import org.agil.vectorialserver.SDEException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.postgis.PGgeometry;

import com.geopista.app.catastro.model.beans.Municipio;
import com.geopista.model.LayerFamily;
import com.geopista.model.LayerStyleData;
import com.geopista.protocol.AbstractValidator;
import com.geopista.protocol.Version;
import com.geopista.protocol.VersionValidator;
import com.geopista.protocol.administrador.EncriptarPassword;
import com.geopista.protocol.administrador.ListaUsuarios;
import com.geopista.protocol.administrador.Permiso;
import com.geopista.protocol.administrador.Usuario;
import com.geopista.protocol.control.ISesion;
import com.geopista.protocol.control.SesionUtils_LCGIII;
import com.geopista.security.GeopistaAcl;
import com.geopista.security.GeopistaPermission;
import com.geopista.server.database.CPoolDatabase;
import com.geopista.ui.dialogs.beans.ComboItemGraticuleListener;
import com.geopista.ui.dialogs.beans.ExtractionProject;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jump.coordsys.CoordinateSystem;
import com.vividsolutions.jump.coordsys.CoordinateSystemRegistry;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.util.Blackboard;
import java.lang.instrument.Instrumentation;

public class PostGISConnection implements GeopistaConnection{
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(PostGISConnection.class);
	private static final int DATABASETYPE=1;
	private String sUrl;
	private String sUser;
	private String sPass;
	private Integer positionIdMunicipio = new Integer(-1);
//	private SRID srid;
	private NewSRID srid;
	private String consultaTable = " (select c.id_table from layers l, attributes a,columns c where l.id_layer = a.id_layer and a.id_column = c.id and a.position = 1 and l.name = ?) ";


	static HashMap<Long,GeopistaAcl> cacheACL=new HashMap<Long,GeopistaAcl>();

	
	static{
		try{
			Class.forName("org.postgresql.Driver");
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public PostGISConnection(String sHost,String sPort,
			String sDB, String sUser, String sPass, NewSRID srid){
		this.sUrl="jdbc:postgresql://"+sHost+":"+sPort+"/"+sDB;
		this.sUser=sUser;
		this.sPass=sPass;
		this.srid=srid;
	}

	public PostGISConnection(NewSRID srid){
		this.srid=srid;

	}

	public PostGISConnection(){

	}

	public void setSRID(NewSRID srid){
		this.srid=srid;
	}


	private Connection openConnectionManual()throws SQLException{
		Connection conn=DriverManager.getConnection(this.sUrl,this.sUser,this.sPass);
		((org.postgresql.PGConnection)conn).addDataType("geometry","org.postgis.PGgeometry");
		return conn;
	}

	public Connection openConnection()throws SQLException{
		return openConnectionDS();
	}

	private Connection openConnectionDS() throws SQLException{
		Connection conn=CPoolDatabase.getConnection();

		((org.postgresql.PGConnection)((org.postgresql.jdbc3.Jdbc3Connection)conn)).addDataType("geometry","org.postgis.PGgeometry");
		((org.postgresql.PGConnection)((org.postgresql.jdbc3.Jdbc3Connection)conn)).addDataType("box3d","org.postgis.PGbox3d");
		conn.setAutoCommit(true);
		return conn;
	}
	
	public String getSRIDDefecto(boolean defecto, int idEntidad) throws FileNotFoundException,IOException,ACException,SQLException{
		SRIDDefecto sridDefecto = null;
		Connection conn = null;
		if (defecto){
			sridDefecto = new SRIDDefecto(Const.SRID_DEFECTO);
			return String.valueOf(sridDefecto.getSRID());
		}else{
			sridDefecto = new SRIDDefecto();
			conn = this.openConnection();
			String srid = String.valueOf(sridDefecto.getSRID(conn, idEntidad));
			try{conn.close();CPoolDatabase.releaseConexion();}catch(SQLException e){throw e;}
			return srid;
		}
	}

	public int getSRIDDefecto(boolean defecto, Connection conn, String idEntidad) throws FileNotFoundException,IOException,ACException{
		SRIDDefecto sridDefecto = null;
		if (defecto){
			sridDefecto = new SRIDDefecto(Const.SRID_DEFECTO);
			return sridDefecto.getSRID();
		}else{
			sridDefecto = new SRIDDefecto();
			return sridDefecto.getSRID(conn, Integer.parseInt(idEntidad));
		}
		
	}

	public void returnLayer(int iMunicipio,String sLayer,String sLocale, Geometry geom,FilterNode fn,ObjectOutputStream oos,ISesion sesion,List lMunicipalities)throws IOException,SQLException, Exception{
		returnLayer(iMunicipio,sLayer,sLocale,geom,fn,oos,sesion,true,lMunicipalities);
	}


    public void returnLayer(int iMunicipio, String sLayer, String sLocale, Geometry geom,
            FilterNode fn, ObjectOutputStream oos, ISesion sesion, boolean bValidateData,
            List lMunicipalities) throws IOException, SQLException, Exception {
        returnLayer(iMunicipio,sLayer,sLocale,geom,fn,oos,sesion,true,lMunicipalities, null);
    }
    
    /**
     * Carga un conjunto de features (Se utiliza para la carga dinamica y para aplicaciones como gestion de la ciudad
     */
	public void loadFeatures(int iEntidad,String sLayer,String sLocale, Geometry geom,FilterNode fn,ObjectOutputStream oos,ISesion sesion,boolean bValidateData,List lMunicipalities, Integer srid_destino)throws IOException,SQLException, Exception{

		ACLayer lyRet=null;

		/** Incidencia[365] El administrador de cartografia no funciona para otros idiomas  */
		String sSQL="select q.id,q.selectquery,q.updatequery,q.insertquery,q.deletequery,"
			+"l.name,l.acl,l.id_layer,d.traduccion,l.id_styles,l.extended_form,s.xml,d.locale,l.modificable,l.versionable "
			+"from queries q,layers l, dictionary d, styles s "
			+"where q.databasetype="+DATABASETYPE+" and l.name=? and "
			+"q.id_layer=l.id_layer and  "
			+"l.id_name=d.id_vocablo and "
			+"l.id_styles=s.id_style ";

		
		Connection conn=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		int numFeatures=0;
		try{
			conn=openConnection();
			ps=conn.prepareStatement(sSQL);
			ps.setString(1,sLayer);
			rs=ps.executeQuery();

			if (rs.next()){
				lyRet=new ACLayer(rs.getInt("id_layer"),rs.getString("traduccion"),rs.getString("name"),replaceSRID(rs.getString("selectquery"), Integer.parseInt(sesion.getIdMunicipio())));
				String selectQuery=rs.getString("selectquery");
			
				Iterator itMunicipios = lMunicipalities.iterator();
				String sMunicipios = "";
				while (itMunicipios.hasNext()){
					Municipio municipio = (Municipio)itMunicipios.next();
					if (!sMunicipios.equals(""))
						sMunicipios += ",";
					sMunicipios += String.valueOf(municipio.getId());
				}
				int iRet=-1;

				GeopistaAcl acl=getPermission(sesion,rs.getLong("acl"),conn);
				checkReadPermLayer(sesion,acl,sLayer);

				readNewSchema(lyRet,sLocale, conn, sesion,acl);
				
				rs.close();
				ps.close();

				//Miro si la geometría está en el srid correspondiente
				sSQL = selectQuery;
				ps=new SQLParserPostGIS(conn,srid).newSelect(sesion.getIdEntidad(), sMunicipios,sSQL,null,geom,fn, srid_destino,null);
				rs=ps.executeQuery();
				ACFeature acFeature=null;
				while (rs.next()){
					numFeatures++;
					acFeature=new ACFeature();
					ACAttribute att=null;
					Object oAttValue=null;
					for (Iterator it=lyRet.getAttributes().values().iterator();it.hasNext();){
						att=(ACAttribute)it.next();
						switch(att.getColumn().getType()){
						case ACLayer.TYPE_GEOMETRY:
							PGgeometry pgGeometry=(PGgeometry)rs.getObject("GEOMETRY");
							if (pgGeometry==null) continue;
							StringBuffer noSRIDgeometry = new StringBuffer();
							pgGeometry.getGeometry().outerWKT(noSRIDgeometry);
							acFeature.setGeometry(noSRIDgeometry.toString());
							break;
						case ACLayer.TYPE_NUMERIC:
						case ACLayer.TYPE_STRING:
						case ACLayer.TYPE_DATE:
						case ACLayer.TYPE_BOOLEAN:
						default:
							try
						{
								oAttValue=rs.getObject(att.getColumn().getName());
								acFeature.setAttribute(att.getName(),(Serializable)oAttValue);
						}catch(Exception ex)
						{
						}
						}
					}
					oos.writeObject(acFeature);
				
				}
			} else
				throw new ObjectNotFoundException("No se encuentra layer: "+sLayer+ " ("+sLocale+")");

            oos.reset();
            logger.info("Fin de cargar el layer: "+sLayer+" Features:"+numFeatures);
		}catch(Exception e){
			oos.writeObject(new ACException(e));
			//Modificado para reestablecer el BufferedOutputStream
			oos.reset();
		
			throw e;
		}finally{
			try{rs.close();}catch(Exception e){};
			try{ps.close();}catch(Exception e){};
			try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
		}
	}

	public void returnLayer(int iEntidad,String sLayer,String sLocale, Geometry geom,FilterNode fn,ObjectOutputStream oos,ISesion sesion,boolean bValidateData,List lMunicipalities, Integer srid)throws IOException,SQLException, Exception{

		ACLayer lyRet=null;

		/** Incidencia[365] El administrador de cartografia no funciona para otros idiomas  */
		String sSQL="select q.id,q.selectquery,q.updatequery,q.insertquery,q.deletequery,"
			+"l.name,l.acl,l.id_layer,l.id_entidad,d.traduccion,l.id_styles,l.extended_form,s.xml,d.locale,l.modificable,l.versionable "
			+"from queries q,layers l, dictionary d, styles s "
			+"where q.databasetype="+DATABASETYPE+" and l.name=? and "
			+"q.id_layer=l.id_layer and  "
			+"l.id_name=d.id_vocablo and "
			+"l.id_styles=s.id_style order by CASE locale WHEN '"+sLocale+"' THEN 1 WHEN 'es_ES' THEN 2 ELSE 3 END";

		String sSQL2="select coalesce(ls.revision,-1) as revision, to_char(v.fecha,'yyyy-MM-dd HH24:MI:ss') as time_fin "
			+"from layers_styles ls, versiones v, attributes a, columns c "
			+"where coalesce(ls.revision,0)=v.revision and ls.id_layer=? and (ls.id_entidad=? or ls.id_entidad = 0) "
			+"and v.id_table_versionada = c.id_table and ls.id_layer = a.id_layer and a.id_column = c.id "
			+"and a.position = 1 "
			+"order by ls.id_entidad desc";
		
		//String sSQL3="select transform(\"GEOMETRY\" , ?) as \"GEOMETRY\" from entidad_supramunicipal where id_entidad=?";
		String sSQL3="select \"GEOMETRY\",municipiodefault from entidad_supramunicipal where id_entidad=?";
		
		Connection conn=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		try{
			conn=openConnection();
			ps=conn.prepareStatement(sSQL);
			ps.setString(1,sLayer);
			logger.info("SQL Ejecutando query Get Queries: "+((org.postgresql.PGStatement)ps).toString());
			rs=ps.executeQuery();

			if (rs.next()){
				//aso para evitar que el resultSet no se posicione si el idioma es el último
				String selectQuery=rs.getString("selectquery");

				/** Incidencia[365] El administrador de cartografia no funciona para otros idiomas  */
				int iCurrentLayerLocale= -1;
				/** Entra en el bucle */
				int iLastLayerLocale= 4;
 //				String time_inicio = obtieneFechaRevision(conn, sLayer, 0);

				do{
					iCurrentLayerLocale= getOpcionLocale(rs.getString("locale"), sLocale);
					/** Priorizamos los idiomas: preferencia de usuario, es_ES, resto de idiomas */
					if (iCurrentLayerLocale < iLastLayerLocale){
						PreparedStatement ps2=conn.prepareStatement(sSQL2);
						
						logger.info("Obteniendo revision de la capa:"+sLayer);
						ps2.setInt(1,rs.getInt("id_layer"));
						ps2.setInt(2,iEntidad);
		    			logger.info("SQL Ejecutando query Filled: "+((org.postgresql.PGStatement)ps).toString());
						ResultSet rs2=ps2.executeQuery();
						long revision = -1;
						String time = "";
						if (rs2.next()){
							revision = rs2.getLong("revision");
							Version version = sesion.getVersion();
							if (version != null){
								if (version.getFecha().equals(""))
									revision = version.getRevisionActual();
								else
									revision = this.obtieneRevision(conn,sLayer,version.getFecha());
							}else if (revision == -1){
									time = this.obtieneFechaRevision(conn, rs.getString("name"),revision);
							}else
								time = rs2.getString("time_fin").replace(' ', 'T');
						}
						rs2.close();
						ps2.close();
						
						int nuevoiEntidad = rs.getInt("id_entidad");
						//Compruebo si la capa es dinámica o no 
						String url = getUrlDynamicLayer(conn, rs.getInt("id_layer"),nuevoiEntidad);
						if (url.equals("")){ //capa no dinámica
							lyRet=new ACLayer(rs.getInt("id_layer"),rs.getString("traduccion"),rs.getString("name"),replaceSRID(rs.getString("selectquery"), Integer.parseInt(sesion.getIdMunicipio())));
							lyRet.setDinamica(false);
						}else{
							if (rs.getBoolean("versionable")){
								Version version = sesion.getVersion();
								if (version != null){
									if (version.getFecha().equals(""))
										time = this.obtieneFechaRevision(conn, rs.getString("name"),version.getRevisionActual());
									else{
										time = version.getFecha().replace(' ', 'T');
									}
								}
							}
							lyRet=new ACDynamicLayer(rs.getInt("id_layer"),rs.getString("traduccion"),rs.getString("name"),replaceSRID(rs.getString("selectquery"),Integer.parseInt(sesion.getIdMunicipio())),url,time);
							lyRet.setDinamica(true);
						}
						lyRet.setACL(rs.getLong("acl"));
						lyRet.setStyleId(String.valueOf(rs.getInt("id_styles")));
						lyRet.setStyleXML(rs.getString("xml"));
						lyRet.setExtendedForm(rs.getString("extended_form"));
						lyRet.setActive(true);
						lyRet.setVisible(true); // ¿Por qué es true? ¿No debiera consultarse layers_styles?
						lyRet.setEditable(rs.getBoolean("modificable"));
						lyRet.setVersionable(rs.getBoolean("versionable"));
						logger.info("La capa:"+sLayer+" esta en la revision:"+revision);
						lyRet.setRevisionActual(revision);
						iLastLayerLocale= iCurrentLayerLocale;
						if (iCurrentLayerLocale == 1) break;
					}
				}while ((rs.next()) );

				
				//CUANDO CARGAMOS LA CAPA EL ACL YA PODRIA VENIR PORQUE LO HEMOS PEDIDO PARA EL MAPA.
				//SE PIDE CUANDO SE CARGA EL MAPA Y CUANDO SE CARGA LA CAPA. LO REDUCIMOS A UNO
				//Posible uso de cacheACL
				GeopistaAcl acl=cacheACL.get(lyRet.getACL());
				if (acl==null){
					acl=getPermission(sesion,lyRet.getACL(),conn);						
				}					
				//GeopistaAcl acl=getPermission(sesion,lyRet.getACL(),conn);
				
				checkReadPermLayer(sesion,acl,sLayer);

				logger.info("Cargando schema de la capa:"+sLayer);
				readNewSchema(lyRet,sLocale, conn, sesion,acl);
				if (lyRet.isVersionable()){
					lyRet.setUltimaRevision (this.obtieneUltimaRevision(conn, lyRet.getSystemName()));
					if (lyRet.getRevisionActual() != -1) 
						if (lyRet.getUltimaRevision() != lyRet.getRevisionActual())
							lyRet.setEditable(false);
						else
							lyRet.setRevisionActual(-1);
				}
				oos.writeObject(lyRet);
				
				//************************
				//Verificamos si la entidad de la que solicita informacion tiene una geometria asociada.
				//en cuyo caso queremos solo sacar la informacion geometrica de la entidad.
				//************************
				org.postgis.Geometry geometryEntidad=null;
				int idMunicipioDefault=-1;
				try {					
					ps=conn.prepareStatement(sSQL3);
					//ps.setInt(2,srid);
					ps.setInt(1,iEntidad);
					logger.info("SQL Ejecutando query Get Geometry: "+((org.postgresql.PGStatement)ps).toString());
					rs=ps.executeQuery();

					if (rs.next()){
						
						PGgeometry pgGeometry=(PGgeometry)rs.getObject("GEOMETRY");
						idMunicipioDefault=rs.getInt("municipiodefault");
						if (pgGeometry!=null){
							logger.info("La entidad dispone de geometria");
							geometryEntidad=pgGeometry.getGeometry();
							StringBuffer noSRIDgeometry=new StringBuffer();
							pgGeometry.getGeometry().outerWKT(noSRIDgeometry);
						}
					}
					//else
					//	logger.info("La entidad no dispone de geometria");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//Si la capa es dinámica sólo hay que pedir información si se ha pedido cargar como vectorial una parte de las features.
				if (!lyRet.isDinamica || geom != null){
					Iterator itMunicipios = lMunicipalities.iterator();
					String sMunicipios = "";
					StringBuffer sb=new StringBuffer();
					while (itMunicipios.hasNext()){
						Municipio municipio = (Municipio)itMunicipios.next();
						//if (!sMunicipios.equals("")){
						if (sb.length()>0){
							//sMunicipios += ",";
							sb.append(",");
						}
						sb.append(municipio.getId());
						//sMunicipios += String.valueOf(municipio.getId());
					}					
					sMunicipios=sb.toString();
					if(sMunicipios.length()==0)
						sMunicipios = sesion.getIdMunicipio();
					
					
					//if (idMunicipioDefault!=-1)
					//	sMunicipios=String.valueOf(idMunicipioDefault);
					if (sesion.getLoadFeatureLayer()==1)
						returnFeatures(sMunicipios,selectQuery,lyRet,geom,fn,oos,conn,sesion,acl,sLocale,bValidateData, srid,geometryEntidad);
				}
			} else
				throw new ObjectNotFoundException("No se encuentra layer: "+sLayer+ " ("+sLocale+")");

            oos.reset();
            logger.info("Fin de cargar el layer: "+sLayer);
		}catch(Exception e){
			oos.writeObject(new ACException(e));			
			//Modificado para reestablecer el BufferedOutputStream
			oos.reset();
			
			throw e;
		}finally{
			try{rs.close();}catch(Exception e){};
			try{ps.close();}catch(Exception e){};
			try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
		}
	}


	public Collection loadMaps(String sLocale, ISesion sesion) throws SQLException,Exception{
		LinkedHashMap alRet=new LinkedHashMap();

		String sSQL="select * from maps m, dictionary d where (id_entidad = 0  or id_entidad='"+sesion.getIdEntidad()+"') "+
		"and m.id_name=d.id_vocablo "+
		"order by fecha_mod is not null desc,fecha_mod desc,id_entidad, id_map";

		Connection conn=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		try{
			conn=openConnection();
			ps=conn.prepareStatement(sSQL);
			rs=ps.executeQuery();
			ACMap map=null;
			/** Incidencia [365] */
			/** Locale del mapa */
			int iCurrentMapLocale= -1;
			int iLastMapLocale= -1;
			/** Indentificador del mapa */
			int iCurrentMapId= -1;
			int iLastMapId= -1;
			/** municipio del mapa */
			int iLastEntidad= -1;
			int iCurrentEntidad= -1;
			/**/
			while (rs.next()){
				map=new ACMap();
				map.setName(rs.getString("traduccion"));
				// TODO: Esto sera un blob...
				map.setImage((byte[])rs.getObject("image"));
				map.settimeStamp(rs.getTimestamp("fecha_mod"));
				map.setId(String.valueOf(rs.getInt("id_map")));
				map.setLocale(rs.getString("locale"));
				/* Cometado por Incidencia [365]*/
				//alRet.put(map.getId(),map); // La query ordena los resultados con los idmuni NULL primero por lo que en el hashtable sobreviven los no NULL en caso de duplicidad.

				/* Incidencia [365]: El administrador de cartografia no funciona para otros idiomas.
                   Si no existe para sLocale, se mostrara es_ES, si no, en ultima instancia se mostrará en el idioma en el que exista. */
				iCurrentMapId= rs.getInt("id_map");
				iCurrentEntidad= rs.getInt("id_entidad");
				/** La query ordena los resultados con los idmuni NULL primero por lo que en el hashtable sobreviven los no NULL en caso de duplicidad. */
				iCurrentMapLocale= getOpcionLocale(rs.getString("locale"), sLocale);
				
				
				ACMap currentStoredMap=(ACMap)alRet.get(map.getId());
				if (currentStoredMap!=null){
					
					//Si el mapa almacenado es de la entidad 0 lo sobreescribimos 
					//con el nuevo siempre y cuando sea de otra entidad
					if (currentStoredMap.getIdEntidad()==0){
						if (iCurrentEntidad!=0){
							map.setIdEntidad(iCurrentEntidad);
							alRet.put(map.getId(),map);
						}
						else{
							//Verificamos el idioma del mapa para ver cual se adapta mas 
							//a las necesidades del usuario
							int iCurrentStoreMapLocale=getOpcionLocale(currentStoredMap.getLocale(), sLocale);
							if (iCurrentStoreMapLocale>iCurrentMapLocale){
								map.setIdEntidad(iCurrentEntidad);
								alRet.put(map.getId(),map);
							}
						}
					}
					else{
						//Verificamos el idioma del mapa para ver cual se adapta mas 
						//a las necesidades del usuario
						int iCurrentStoreMapLocale=getOpcionLocale(currentStoredMap.getLocale(), sLocale);
						if (iCurrentStoreMapLocale>iCurrentMapLocale){
							map.setIdEntidad(iCurrentEntidad);
							alRet.put(map.getId(),map);
						}
					}
				}
				else{
					map.setIdEntidad(iCurrentEntidad);
					alRet.put(map.getId(),map);
					
				}
				
				
				/*
				
				//Si un mapa esta para una entidad y para la 0, tenemos que coger tambien el de la cero.
				//En el caso de los idiomas tenemos que coger aquellos mapas que esten en nuestro idioma
				if (iCurrentEntidad != iLastEntidad){
					//Si el mapa es el mismo no hacemos nada con ello
					if ((iCurrentEntidad==0) && (iCurrentMapId == iLastMapId)){
						//No hacemos nada
					}
					else{
							map.setIdEntidad(iCurrentEntidad);
							alRet.put(map.getId(),map);
							iLastEntidad= iCurrentEntidad;
							iLastMapLocale= iCurrentMapLocale;
							iLastMapId= iCurrentMapId;						
					}
				}else{
					if ( ((iCurrentEntidad == iLastEntidad) && (iCurrentMapId != iLastMapId)) ||
					     ((iCurrentEntidad == iLastEntidad) && (iCurrentMapId == iLastMapId) && (iCurrentMapLocale < iLastMapLocale)) ){
						map.setIdEntidad(iCurrentEntidad);
						alRet.put(map.getId(),map);
						iLastEntidad= iCurrentEntidad;
						iLastMapLocale= iCurrentMapLocale;
						iLastMapId= iCurrentMapId;
					}
				}
				*/
				/*if ((iCurrentEntidad != iLastEntidad) ||
						((iCurrentEntidad == iLastEntidad) && (iCurrentMapId != iLastMapId)) ||
						((iCurrentEntidad == iLastEntidad) && (iCurrentMapId == iLastMapId) && (iCurrentMapLocale < iLastMapLocale))){
					map.setIdEntidad(iCurrentEntidad);
					alRet.put(map.getId(),map);
					iLastEntidad= iCurrentEntidad;
					iLastMapLocale= iCurrentMapLocale;
					iLastMapId= iCurrentMapId;
				}*/
				/**/
			}
		}finally{
			try{rs.close();}catch(Exception e){};
			try{ps.close();}catch(Exception e){};
			try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
		}
		return alRet.values();
	}

	public void returnMaps(String sLocale,ISesion sesion, ObjectOutputStream oos)throws IOException,SQLException,Exception{
		try{
			for (Iterator it=loadMaps(sLocale, sesion).iterator();it.hasNext();){
				oos.writeObject((ACMap)it.next());
				oos.reset();	
			}
		}catch(Exception e){
			oos.writeObject(new ACException(e));
			//Modificado para reestablecer el BufferedOutputStream
			oos.reset();
			throw e;
		}
	}

	public ACMap loadMap(int iID, String sLocale, ISesion sesion, boolean bLoadSchema,Connection connection) throws Exception{
		ACMap acMap=null;
		int realIdEntidad = 0;

		//Limpiamos la cache de ACL
		cacheACL.clear();
		
		/* Incidencia [365] */
		String sSQLMap="select m.id_map,m.image,m.xml,d.traduccion, m.id_entidad, d.locale " +
		"from maps m, dictionary d " +
		"where m.id_map=? and m.id_name=d.id_vocablo "+
		"and (id_entidad = 0  or id_entidad=?) order by id_entidad";

//		String sSQLLayers="select mlr.position as mlr_position, " +
//		"d1.traduccion as nombre_categoria, d2.traduccion as nombre_layer,lf.id_layerfamily as lf_id_layerfamily," +
//		"l.id_layer as l_id_layer," +
//		"l.name as l_name," +
//		"l.acl as l_acl," +
//		"l.versionable as l_versionable," +
//		"llr.position as llr_position," +
//		"ls.stylename as ls_stylename," +
//		"ls.position as ls_position," +
//		"ls.isactive as ls_isactive, "+
//		"ls.isvisible as ls_isvisible, "+
//		"ls.iseditable as ls_iseditable, "+
//		"ls.id_entidad as ls_id_entidad, "+
//		"s.xml as s_xml," +
//		"s.id_style as s_id_style," +
//		"d1.locale as lf_locale, d2.locale as l_locale," +
//		"q.selectquery as q_selectquery " +
//		"from maps m inner join maps_layerfamilies_relations mlr on m.id_map=mlr.id_map " +
//		"inner join layerfamilies lf on mlr.id_layerfamily=lf.id_layerfamily " +
//		"inner join layerfamilies_layers_relations llr on lf.id_layerfamily=llr.id_layerfamily " +
//		"inner join layers l on llr.id_layer=l.id_layer " +
//		"inner join queries q on q.id_layer=l.id_layer " +
//		"left join layers_styles ls on m.id_map=ls.id_map and lf.id_layerfamily=ls.id_layerfamily and ls.id_layer=l.id_layer " +
//		"left join styles s on s.id_style=ls.id_style " +
//		"inner join dictionary d1 on lf.id_name=d1.id_vocablo " +
//		"inner join dictionary d2 on l.id_name=d2.id_vocablo " +
//		"where q.databasetype="+DATABASETYPE+" and m.id_map=? and d1.locale=d2.locale"+
//		" and s.id_style is not null and m.id_entidad=mlr.id_entidad and mlr.id_entidad=ls.id_entidad"+
//		" and (ls.id_entidad =? or ls.id_entidad = 0)  " + //Añadido por ASO
//		" order by mlr.position,llr.position,l.id_layer,d2.locale";
		
		//NUEVO
		String sSQLLayers="select mlr.position as mlr_position, " +
		"d1.traduccion as nombre_categoria, d2.traduccion as nombre_layer,lf.id_layerfamily as lf_id_layerfamily," +
		"l.id_layer as l_id_layer," +
		"l.name as l_name," +
		"l.acl as l_acl," +
		"l.versionable as l_versionable," +
		"llr.position as llr_position," +
		"ls.stylename as ls_stylename," +
		"ls.position as ls_position," +
		"ls.isactive as ls_isactive, "+
		"ls.isvisible as ls_isvisible, "+
		"ls.iseditable as ls_iseditable, "+
		"ls.id_entidad as ls_id_entidad, "+
		"s.xml as s_xml," +
		"s.id_style as s_id_style," +
		"d1.locale as lf_locale, d2.locale as l_locale," +
		"q.selectquery as q_selectquery " +
		"from maps m inner join maps_layerfamilies_relations mlr on m.id_map=mlr.id_map " +
		"inner join layerfamilies lf on mlr.id_layerfamily=lf.id_layerfamily " +
		"inner join layerfamilies_layers_relations llr on lf.id_layerfamily=llr.id_layerfamily " +
		"inner join layers l on llr.id_layer=l.id_layer " +
		"inner join queries q on q.id_layer=l.id_layer " +
		"left join layers_styles ls on m.id_map=ls.id_map and lf.id_layerfamily=ls.id_layerfamily and ls.id_layer=l.id_layer " +
		"left join styles s on s.id_style=ls.id_style " +
		"inner join dictionary d1 on lf.id_name=d1.id_vocablo " +
		"inner join dictionary d2 on l.id_name=d2.id_vocablo " +
		"where q.databasetype="+DATABASETYPE+" and m.id_map=? and d1.locale=d2.locale"+
		" and s.id_style is not null and m.id_entidad=mlr.id_entidad and mlr.id_entidad=ls.id_entidad"+
		" and (ls.id_entidad =? or ls.id_entidad = 0)  " + //Añadido por ASO
		" order by mlr.position,llr.position,l.id_layer,d2.locale";
		//NUEVO

		Connection conn=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		boolean bCerrarConexion=true;
		try{

			if (connection!=null)
			{
				conn=connection;
				bCerrarConexion=false;
			}
			else
			{
				conn=openConnection();
				bCerrarConexion=true;
			}
			ps=conn.prepareStatement(sSQLMap);
			ps.setInt(1,iID);
			ps.setInt(2,Integer.parseInt(sesion.getIdEntidad()));
			rs=ps.executeQuery();
			acMap=new ACMap();

			int iCurrentEntidad= -1;
			if (rs.next()){
				/** Comentado por incidencia [365] */

				/* Incidencia [365]: El administrador de cartografia no funciona para otros idiomas.
                   Si no existe para sLocale, se mostrará en el idioma en el que exista. */
				int iLastEntidad= -1;
				int iCurrentMapLocale= -1;
				int iLastMapLocale= -1;

				do{
					// Priorizamos los idiomas: preferencia de usuario, es_ES, resto de idiomas
					iCurrentEntidad= rs.getInt("id_entidad");
					iCurrentMapLocale= getOpcionLocale(rs.getString("locale"), sLocale);
					/** ordenado por id_municipio: los registros con id_municicpio=0 los primeros */
					if ((iCurrentEntidad != iLastEntidad) ||
							((iCurrentEntidad == iLastEntidad) && (iCurrentMapLocale < iLastMapLocale))){
						acMap.setName(rs.getString("traduccion"));
						acMap.setImage((byte[])rs.getObject("image"));
						acMap.setId(String.valueOf(rs.getInt("id_map")));
						acMap.setXml(updateProjection(rs.getAsciiStream("xml"),srid.getSRID(Integer.parseInt(sesion.getIdMunicipio())), sesion)); 
						realIdEntidad = rs.getInt("id_entidad");
						acMap.setIdEntidad(realIdEntidad);

						iLastEntidad= iCurrentEntidad;
						iLastMapLocale= iCurrentMapLocale;
					}
				}while (rs.next());
				/**/
			} else
				throw new ObjectNotFoundException("ObjectNotFoundException: map "+iID + " ("+sLocale+")");

			rs.close();
			ps.close();
			ps=conn.prepareStatement(sSQLLayers);
			ps.setInt(1,iID);
			ps.setInt(2,realIdEntidad);
			rs=ps.executeQuery();
			Hashtable htFamilies=new Hashtable();
			Hashtable htLayers=null;
			acMap.setLayerFamilies(htFamilies);
			int iCurrentCategoryPosition=0;
			int iLastCategoryPosition=-1;
			String sCategory=null;
			ACLayerFamily acFamily=null;
			// Lo de abajo deja de ser cierto fallaba cuando el nombre de la categoria y el layer eran
			// los mismos, además no se podía concretar que el orden fuera correcto
			// Los resultados salen en filas de 2 en 2, la primera con el nombre de la categoria
			// y la segunda con el nombre del layer

			/** Incidencia [365] */
			int iLastLayerPosition= -1;
			int iCurrentLayerPosition= -1;
			/** opcion idioma layer */
			int iLastLayerLocale= -1;
			int iCurrentLayerLocale= -1;
			/** opcion idioma categoria */
			int iLastCategoriaLocale= -1;
			int iCurrentCategoriaLocale= -1;
			/**/
			int i=0;
			
			List mapServerLayers = new ArrayList();
			htLayers=new Hashtable();
			while (rs.next()){   
				if (rs.getInt("ls_id_entidad")  == iCurrentEntidad){
						/** Opcion de idioma encontrada en la categoria */
						iCurrentCategoriaLocale= getOpcionLocale(rs.getString("lf_locale"), sLocale);
						/**/
						iCurrentCategoryPosition=rs.getInt("mlr_position");
						if (iCurrentCategoryPosition!=iLastCategoryPosition){ // Nueva categoria?
							htLayers = new Hashtable();
							sCategory=rs.getString("nombre_categoria");
							acFamily=new ACLayerFamily();
							acFamily.setName(sCategory);
							acFamily.setId(rs.getInt("lf_id_layerfamily"));
							acFamily.setLayers(htLayers);
							htFamilies.put(new Integer(iCurrentCategoryPosition),acFamily);
							iLastCategoryPosition=iCurrentCategoryPosition;
		
							/** Añadido por Incidencia [365] */
							iLastLayerPosition= -1;
							iLastCategoriaLocale= iCurrentCategoriaLocale;
						}else if (iCurrentCategoriaLocale < iLastCategoriaLocale){
							/** Locale actual mejor que el anterior */
							if (htFamilies.get(new Integer(iCurrentCategoryPosition)) != null){
								acFamily= (ACLayerFamily)htFamilies.get(new Integer(iCurrentCategoryPosition));
								acFamily.setName(rs.getString("nombre_categoria"));
							}
							iLastCategoriaLocale= iCurrentCategoriaLocale;
						}
						/**/
					
						String layerName=rs.getString("nombre_layer");
						ACLayer acLayer;
						//Compruebo si la capa es dinámica o no 
						String url = getUrlDynamicLayer(conn, rs.getInt("l_id_layer"),iCurrentEntidad);
						if (url.equals("")) //capa no dinámica
							acLayer=new ACLayer(rs.getInt("l_id_layer"),rs.getString("nombre_layer"),rs.getString("l_name"),replaceSRID(rs.getString("q_selectquery"),Integer.parseInt(sesion.getIdMunicipio())));
						else{
							acLayer=new ACDynamicLayer(rs.getInt("l_id_layer"),rs.getString("nombre_layer"),rs.getString("l_name"),replaceSRID(rs.getString("q_selectquery"),Integer.parseInt(sesion.getIdMunicipio())),url);
						}
						String sStyleName=rs.getString("ls_stylename");
						String sStyleXML=rs.getString("s_xml");
						String sStyleId=String.valueOf(rs.getInt("s_id_style"));
						if (rs.getString("ls_position")!=null)
						{
							int iPosOnMap=rs.getInt("ls_position");
							acLayer.setPositionOnMap(iPosOnMap);
						}
						if (sStyleName!=null && sStyleXML!=null){
							acLayer.setStyleName(sStyleName);
							acLayer.setStyleXML(sStyleXML);
							acLayer.setStyleId(sStyleId);
						}
						//ASO AÑADE
						i++;
						acLayer.setActive(rs.getBoolean("ls_isactive"));//checkPermission(sesion,rs.getLong("l_acl"),Const.PERM_LAYER_READ));
						acLayer.setVisible(rs.getBoolean("ls_isvisible"));
						acLayer.setVersionable(rs.getBoolean("l_versionable"));
						boolean bedit = rs.getBoolean("ls_iseditable");
						long acl_long=rs.getLong("l_acl");
								
						GeopistaAcl acl=cacheACL.get(acl_long);
						if (acl==null){
							acl=getPermission(sesion,rs.getLong("l_acl"),conn);						
							cacheACL.put(acl_long,acl);
						}
						
						checkReadPerm(sesion,acl,layerName);
						// David: Valor_Editable AND Valor_Permiso 
						acLayer.setEditable(bedit);
						
						//Se comenta para realizar la conexion contra la Base de Datos solo una vez.
		
						/** Incidencia [365] . Leemos la opcion de idioma.*/
						iCurrentLayerLocale= getOpcionLocale(rs.getString("l_locale"), sLocale);
						iCurrentLayerPosition= rs.getInt("llr_position");
						if ((iCurrentLayerPosition != iLastLayerPosition) ||
								((iCurrentLayerPosition == iLastLayerPosition) && (iCurrentLayerLocale < iLastLayerLocale))){
							/** La opcion locale actual es mejor que la anterior */
							htLayers.put(new Integer(rs.getInt("llr_position")),acLayer);
							//TODO CAMBIAR
							if (bLoadSchema)
								readNewSchema(acLayer,sLocale, conn, sesion,acl);
							iLastLayerLocale= iCurrentLayerLocale;
							iLastLayerPosition= iCurrentLayerPosition;
						}
						/**/
				/**/
				/**/
				}
			}

			mapServerLayers.addAll(loadMapServerLayers(iID, sesion,conn));
			acMap.setLayerFamilies(htFamilies);
			
			acMap.setMapServerLayers(mapServerLayers);
		}
		catch(Exception e){
			e.printStackTrace();
			throw e;
		}finally{
			try{rs.close();}catch(Exception e){};
			try{ps.close();}catch(Exception e){};
			try{if (bCerrarConexion){conn.close();CPoolDatabase.releaseConexion();}}catch(Exception e1){};

		}
		return acMap;
	}

	/**
	 * Comprobamos si tiene permiso de lectura.
	 * @param sesion
	 * @param acl
	 * @throws Exception
	 */
	private void checkReadPerm(ISesion sesion, GeopistaAcl acl,String layerName) throws Exception{
		boolean permisoLectura=acl.checkPermission(new GeopistaPermission(Const.PERM_LAYER_READ));
		if (!permisoLectura){
			throw new PermissionException("Usuario "+sesion.getUserPrincipal().getName()+ " no tiene" +
			" permisos para ver las capas. Layer:"+layerName+" Acl:"+acl.getAclDescripcion());                	
		}

	}
	private void checkReadPermLayer(ISesion sesion, GeopistaAcl acl,String layerName) throws Exception{
		boolean permisoLectura=acl.checkPermission(new GeopistaPermission(Const.PERM_LAYER_READ));
		if (!permisoLectura){
			//throw new PermissionException("PermissionException: " + Const.PERM_LAYER_READ);
			throw new PermissionException("PermissionException:"+Const.PERM_LAYER_READ+ "para leer la capa. Layer:"+layerName+" Acl:"+acl.getAclDescripcion());                	
		}    	
	}   

	private boolean checkPerm(ISesion sesion, GeopistaAcl acl,String perm) throws Exception{
		boolean permiso=acl.checkPermission(new GeopistaPermission(perm));
		if (!permiso){
			return false;
		}
		return true;
	}     

	public ArrayList loadMapServerLayers(int idMap, ISesion sesion,Connection connection){
		ArrayList wmsLayers = new ArrayList();
		ACWMSLayer layer = null;
		boolean bCerrarConexion=true;

		String query = "select MSL.id, MSL.service, MSL.url, MSL.params, " +
		"MSL.srs, MSL.format, MSL.version, MSL.activa, MSL.visible, MSL.styles, MSL.name, R.position " +
		"from map_server_layers MSL, maps_wms_relations R " +
		//"where MSL.id = R.id_mapserver_layer and R.id_map = ? and R.id_entidad = ?";
		"where MSL.id = R.id_mapserver_layer and R.id_map = ? and (R.id_entidad = ? or R.id_entidad=0)";

		Connection conn=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		try{
			//conn=openConnection();
			if (connection!=null)
			{
				conn=connection;
				bCerrarConexion=false;
			}
			else
			{
				conn=openConnection();
				bCerrarConexion=true;
			}            
			ps=conn.prepareStatement(query);
			ps.setInt(1,idMap);
			ps.setInt(2,Integer.parseInt(sesion.getIdEntidad()));
			rs=ps.executeQuery();

			while (rs.next()){
				int idWMSLayer = rs.getInt("id");
				String service = rs.getString("service");
				String url = rs.getString("url");
				String params = rs.getString("params");
				String srs = rs.getString("srs");
				String format = rs.getString("format");
				String version = rs.getString("version");
				boolean activa = rs.getInt("activa") == 1 ? true : false;
				boolean visible = rs.getInt("visible") == 1 ? true : false;
				int position = rs.getInt("position");
				String styles=rs.getString("styles");
				String name=rs.getString("name");

				layer = new ACWMSLayer(idWMSLayer, service, url, params, srs, format, version, activa, visible, position,styles, name);
				wmsLayers.add(layer);
			}

		}
		catch(Exception e){
		}
		finally{
			try{rs.close();}catch(Exception e){};
			try{ps.close();}catch(Exception e){};
			try{if (bCerrarConexion){conn.close();CPoolDatabase.releaseConexion();}}catch(Exception e){};
		}

		return wmsLayers;
	}

	public void returnMap(int iEntidad,int iID, String sLocale, ObjectOutputStream oos,ISesion sesion)throws IOException,SQLException,Exception{
		try{
			oos.writeObject(loadMap(iID,sLocale,sesion,false,null));
			oos.reset();	
			
		}catch(PermissionException pe)
		{
			throw pe;
		}catch(Exception e){
			oos.writeObject(new ACException(e));
			//Modificado para reestablecer el BufferedOutputStream
			oos.reset();
			
			throw e;
		}
	}

	private int returnFeatures (int iMunicipio,String sSQL, ACLayer layer, Geometry geom,FilterNode fn, ObjectOutputStream oos, Connection connection,ISesion sesion, GeopistaAcl acl)throws Exception{
		return returnFeatures(iMunicipio,sSQL,layer,geom,fn,oos,connection,sesion,acl,null,true);
	}

	private int returnFeatures (int iMunicipio,String sSQL, ACLayer layer, Geometry geom,FilterNode fn, ObjectOutputStream oos, Connection connection,ISesion sesion, GeopistaAcl acl,String sLocale)throws Exception{
		return returnFeatures(iMunicipio,sSQL,layer,geom,fn,oos,connection,sesion,acl,sLocale,true);
	}
	
	private int returnFeatures (int iMunicipio,String sSQL, ACLayer layer, Geometry geom,FilterNode fn, ObjectOutputStream oos, Connection connection,ISesion sesion, GeopistaAcl acl, String sLocale, boolean bValidateData)throws Exception{
	    return returnFeatures(iMunicipio,sSQL,layer,geom,fn,oos,connection,sesion,acl,sLocale,true, null);
	}

	/** Devuelve el ID del ultimo Feature leido 
	 * @param acl */
	private int returnFeatures (int iMunicipio,String sSQL, ACLayer layer, Geometry geom,FilterNode fn, ObjectOutputStream oos, Connection connection,ISesion sesion, GeopistaAcl acl, String sLocale, boolean bValidateData, Integer srid_destino)throws Exception{
		int iRet=-1;
		Connection conn=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		boolean bCerrarConexion=true;
		try{

			if (connection!=null)
			{
				conn=connection;
				bCerrarConexion=false;
			}
			else
			{
				conn=openConnection();
				bCerrarConexion=true;
			}
			if (acl==null){
				if (!checkPermission(sesion,layer.getACL(),Const.PERM_LAYER_READ,conn))
					throw new PermissionException(Const.PERM_LAYER_READ);
			}
			else{
				boolean permisoLectura=acl.checkPermission(new GeopistaPermission(Const.PERM_LAYER_READ));
				if (!permisoLectura)
					throw new PermissionException(Const.PERM_LAYER_READ);                
			}

			ArrayList lstValidateFeatures = null;
			if (bValidateData)
			{
				lstValidateFeatures = getValidateFeatures(layer,sLocale,conn);
			}

			layer.setSelectQuery(sSQL);
			if (lstValidateFeatures != null){
				for (Iterator iterValidateFeatures = lstValidateFeatures.iterator();iterValidateFeatures.hasNext();){
					AbstractValidator validateFeatures = (AbstractValidator)iterValidateFeatures.next();
					if (validateFeatures instanceof VersionValidator){
						Version version = sesion.getVersion();
						if (version == null)
							version = new Version();
						version.setIdTable(this.obtenerIdTabla(conn, layer));
						version.setRevisionActual(layer.getRevisionActual());
						validateFeatures.setVersion(version);
						if (version != null && !version.isFeaturesActivas()){
							if (!checkPermission(sesion,layer.getACL(),Const.PERM_LAYER_READ,conn))
								throw new PermissionException(Const.PERM_LAYER_READ);
						}
					}
					validateFeatures.beforeRead(layer);
				}
			}
			sSQL = layer.getSelectQuery();
			
			//REFACTORIZACION ORACLE ps=new SQLParser(conn,srid).newSelect(iMunicipio,sSQL,layer,geom,fn);
			ps=new SQLParserPostGIS(conn,srid).newSelect(sesion.getIdEntidad(), String.valueOf(iMunicipio),sSQL,layer,geom,fn, srid_destino,null);
			rs=ps.executeQuery();
			ACFeature acFeature=null;
			while (rs.next()){
				acFeature=new ACFeature();
				ACAttribute att=null;
				Object oAttValue=null;
				for (Iterator it=layer.getAttributes().values().iterator();it.hasNext();){
					att=(ACAttribute)it.next();
					switch(att.getColumn().getType()){
					case ACLayer.TYPE_GEOMETRY:
						PGgeometry pgGeometry=(PGgeometry)rs.getObject("GEOMETRY");
						if (pgGeometry==null) continue;
						StringBuffer noSRIDgeometry = new StringBuffer();
						pgGeometry.getGeometry().outerWKT(noSRIDgeometry);
						acFeature.setGeometry(noSRIDgeometry.toString());
						break;
					case ACLayer.TYPE_NUMERIC:
					case ACLayer.TYPE_STRING:
					case ACLayer.TYPE_DATE:
					case ACLayer.TYPE_BOOLEAN:
					default:
						try
					{
							oAttValue=rs.getObject(att.getColumn().getName());
							acFeature.setAttribute(att.getName(),(Serializable)oAttValue);
					}catch(Exception ex)
					{
					}
					}
				}
				oos.writeObject(acFeature);
				oos.reset();	
			
			}
			if (acFeature!=null)
			{
				iRet=acFeature.findID(layer);

				if (lstValidateFeatures != null){
					for (Iterator iterValidateFeatures = lstValidateFeatures.iterator();iterValidateFeatures.hasNext();){
						AbstractValidator validateFeatures = (AbstractValidator)iterValidateFeatures.next();
						validateFeatures.afterRead(acFeature, layer);
					}
				}
			}
		}catch (Exception e){
			oos.writeObject(new ACException(e));
			//Modificado para reestablecer el BufferedOutputStream
			oos.reset();
			throw e;
		}finally{
			try{rs.close();}catch(Exception e){};
			try{ps.close();}catch(Exception e){};
			try{if (bCerrarConexion){conn.close();CPoolDatabase.releaseConexion();}}catch(Exception e){};
		}
		return iRet;
	}


	private int returnFeatures (String iMunicipio,String sSQL, ACLayer layer, Geometry geom,FilterNode fn, ObjectOutputStream oos, Connection connection,ISesion sesion, GeopistaAcl acl, String sLocale, boolean bValidateData, Integer srid_destino, org.postgis.Geometry geometryEntidad)throws Exception{
		int iRet=-1;
		Connection conn=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		boolean bCerrarConexion=true;
		int numFeatures=0;
		try{

			if (connection!=null)
			{
				conn=connection;
				bCerrarConexion=false;
			}
			else
			{
				conn=openConnection();
				bCerrarConexion=true;
			}
			if (acl==null){
				if (!checkPermission(sesion,layer.getACL(),Const.PERM_LAYER_READ,conn))
					throw new PermissionException(Const.PERM_LAYER_READ);
			}
			else{
				boolean permisoLectura=acl.checkPermission(new GeopistaPermission(Const.PERM_LAYER_READ));
				if (!permisoLectura)
					throw new PermissionException(Const.PERM_LAYER_READ);                
			}

			ArrayList lstValidateFeatures = null;
			if (bValidateData)
			{
				lstValidateFeatures = getValidateFeatures(layer,sLocale,conn);
			}

			if (lstValidateFeatures != null){
				for (Iterator iterValidateFeatures = lstValidateFeatures.iterator();iterValidateFeatures.hasNext();){
					AbstractValidator validateFeatures = (AbstractValidator)iterValidateFeatures.next();
					if (validateFeatures instanceof VersionValidator){
						Version version = sesion.getVersion();
						if (version == null){
							version = new Version();
							if (layer.getRevisionActual() != -1){
								version.setRevisionActual(layer.getRevisionActual());
							}
						}
						version.setIdTable(this.obtenerIdTabla(conn, layer));
						validateFeatures.setVersion(version);
						if (version != null && !version.isFeaturesActivas()){
							if (!checkPermission(sesion,layer.getACL(),Const.PERM_LAYER_READ,conn))
								throw new PermissionException(Const.PERM_LAYER_READ);
						}
					}
					validateFeatures.beforeRead(layer);
				}
			}
			
			//OJO. Puede devolver elementos temporales y publicables
			//¿Los filtramos para que no vengan?

			//REFACTORIZACION ORACLE ps=new SQLParser(conn,srid).newSelect(iMunicipio,sSQL,layer,geom,fn);
			//Miro si la geometría está en el srid correspondiente
			sSQL = layer.getSelectQuery();
			long startMils=Calendar.getInstance().getTimeInMillis();
			logger.info("Start Time:"+layer.getName()+" IdMunicipio:"+sesion.getIdMunicipio()+" iMunicipio:"+iMunicipio);
			ps=new SQLParserPostGIS(conn,srid).newSelect(sesion.getIdEntidad(), iMunicipio,sSQL,layer,geom,fn, srid_destino,geometryEntidad);
			//if (true)throw new Exception();
			rs=ps.executeQuery();
			long endMils=Calendar.getInstance().getTimeInMillis();
			logger.info("End Time:"+layer.getName()+" :"+(endMils-startMils)/1000+" seg"+" IdMunicipio:"+sesion.getIdMunicipio());
			ACFeature acFeature=null;
			
			
			startMils=Calendar.getInstance().getTimeInMillis();
			logger.info("Devolviendo features de la capa:"+layer.getName());
			long featuresSize=0;
			//configuration steps
			ACUtil.initSizeOf();
			
			
			int numElementos=0;
			while (rs.next()){
				numFeatures++;
				acFeature=new ACFeature();
				ACAttribute att=null;
				Object oAttValue=null;
				for (Iterator it=layer.getAttributes().values().iterator();it.hasNext();){
					att=(ACAttribute)it.next();
					switch(att.getColumn().getType()){
					case ACLayer.TYPE_GEOMETRY:
						PGgeometry pgGeometry=(PGgeometry)rs.getObject("GEOMETRY");
						if (pgGeometry==null) continue;
						StringBuffer noSRIDgeometry = new StringBuffer();
						pgGeometry.getGeometry().outerWKT(noSRIDgeometry);
						acFeature.setGeometry(noSRIDgeometry.toString());
						break;
					case ACLayer.TYPE_NUMERIC:
					case ACLayer.TYPE_STRING:
					case ACLayer.TYPE_DATE:
					case ACLayer.TYPE_BOOLEAN:
					default:
						try{
							oAttValue=rs.getObject(att.getColumn().getName());
							acFeature.setAttribute(att.getName(),(Serializable)oAttValue);
					}catch(Exception ex)
					{
					}
					}
				}
				oos.writeObject(acFeature);
				oos.reset();
				//calculate object size
				
				featuresSize+=ACUtil.deepSizeOf(acFeature);			
				numElementos++;
				if (numElementos % 10000 ==0){
					logger.info("Cargando elementos de la capa:"+layer.getName()+" Elementos:"+numElementos);
					//oos.flush();
				}
			}
			logger.info("Fin de cargar el layer: "+layer.getName()+" Features:"+numFeatures);
			endMils=Calendar.getInstance().getTimeInMillis();
			logger.info("End Time Get features:"+layer.getName()+" :"+(endMils-startMils)/1000+" seg."+ " Size:("+featuresSize+") "+featuresSize/(1024*1000)+" Mb"+" IdMunicipio:"+sesion.getIdMunicipio());

			if (acFeature!=null)
			{
				iRet=acFeature.findID(layer);

				if (lstValidateFeatures != null){
					for (Iterator iterValidateFeatures = lstValidateFeatures.iterator();iterValidateFeatures.hasNext();){
						AbstractValidator validateFeatures = (AbstractValidator)iterValidateFeatures.next();
						validateFeatures.afterRead(acFeature, layer);
					}
				}
			}
		}catch (Exception e){
			oos.writeObject(new ACException(e));
			//Modificado para reestablecer el BufferedOutputStream
			oos.reset();
		
			throw e;
		}finally{
			try{rs.close();}catch(Exception e){};
			try{ps.close();}catch(Exception e){};
			try{if (bCerrarConexion){conn.close();CPoolDatabase.releaseConexion();}}catch(Exception e){};
		}
		return iRet;
	}

	/** Devuelve todos los Features con los IDs en featureIDs[]
	 * @param acl */
	private int returnAllFeatures (String sTable, ACLayer layer, int[] featureIDs, ObjectOutputStream oos, Connection connection,ISesion sesion, GeopistaAcl acl, Integer srid_destino, boolean bImportaciones)throws Exception{
		int iRet=-1;
		Connection conn=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		boolean bCerrarConexion=true;
		try{

			if (connection!=null)
			{
				conn=connection;
				bCerrarConexion=false;
			}
			else
			{
				conn=openConnection();
				bCerrarConexion=true;
			}
			if (acl==null){
				if (!checkPermission(sesion,layer.getACL(),Const.PERM_LAYER_READ,conn))
					throw new PermissionException(Const.PERM_LAYER_READ);
			}
			else{
				boolean permisoLectura=acl.checkPermission(new GeopistaPermission(Const.PERM_LAYER_READ));
				if (!permisoLectura)
					throw new PermissionException(Const.PERM_LAYER_READ);                
			}

			//REFACTORIZACION ORACLE ps=new SQLParser(conn,srid).newSelect(iMunicipio,sSQL,layer,geom,fn);
			
			boolean bFirstValueEntered = false;
			StringBuffer sbFeatures = new StringBuffer("");
			for (int i=0; i<featureIDs.length; i++){
				int iFeatureID = featureIDs[i];
				if (iFeatureID!=-1){
					if (!bFirstValueEntered){
						sbFeatures.append("\""+sTable+"\".ID=");
						sbFeatures.append(iFeatureID);
						bFirstValueEntered = true;
					}else{
						sbFeatures.append(" OR \""+sTable+"\".ID=");
						sbFeatures.append(iFeatureID);
					}
				}
			}


			sbFeatures.append(");");
			if (!bImportaciones){
				//Saco los municipios implicados
				Iterator itMunicipios = sesion.getAlMunicipios().iterator();
				String sMunicipios = "";
				while (itMunicipios.hasNext()){
					Municipio municipio = (Municipio)itMunicipios.next();
					if (!sMunicipios.equals(""))
						sMunicipios += ",";
					sMunicipios += String.valueOf(municipio.getId());
				}
				String sSQL = layer.getSelectQuery()+" and ("+sbFeatures.toString();
				ps=new SQLParserPostGIS(conn,srid).newSelect(sesion.getIdEntidad(), sMunicipios,sSQL,layer,null,null, srid_destino,null);
			}else{
				
				SRIDDefecto sridDefecto = new SRIDDefecto(Const.SRID_DEFECTO);
				Integer defaultSRID=sridDefecto.getSRID();
				
				StringBuffer sbSQL = new StringBuffer(0);
				sbSQL.append("select ");
				sbSQL.append("transform("+sTable+".\"GEOMETRY\","+srid_destino+") as geom,");
				sbSQL.append("\""+sTable);
				sbSQL.append("\".* from \"");
				sbSQL.append(sTable);
				sbSQL.append("\" where (");
				sbSQL.append(sbFeatures.toString());
				ps=conn.prepareStatement(sbSQL.toString());
    			logger.info("SQL Ejecutando query Filled: "+((org.postgresql.PGStatement)ps).toString());

			}

			rs=ps.executeQuery();
			ACFeature acFeature=null;
			while (rs.next()){
				acFeature=new ACFeature();
				ACAttribute att=null;
				Object oAttValue=null;
				for (Iterator it=layer.getAttributes().values().iterator();it.hasNext();){
					att=(ACAttribute)it.next();
					switch(att.getColumn().getType()){
					case ACLayer.TYPE_GEOMETRY:
						PGgeometry pgGeometry=null;
						if (!bImportaciones)							
							pgGeometry=(PGgeometry)rs.getObject("GEOMETRY");
						else
							pgGeometry=(PGgeometry)rs.getObject("geom");
						if (pgGeometry==null) continue;
						StringBuffer noSRIDgeometry = new StringBuffer();
						pgGeometry.getGeometry().outerWKT(noSRIDgeometry);
						acFeature.setGeometry(noSRIDgeometry.toString());
						break;
					case ACLayer.TYPE_NUMERIC:
					case ACLayer.TYPE_STRING:
					case ACLayer.TYPE_DATE:
					case ACLayer.TYPE_BOOLEAN:
					default:
						try
					{
							oAttValue=rs.getObject(att.getColumn().getName());
							acFeature.setAttribute(att.getName(),(Serializable)oAttValue);
					}catch(Exception ex)
					{
					}
					}
				}
				oos.writeObject(acFeature);
                oos.reset();
			}

		}catch (Exception e){
			oos.writeObject(new ACException(e));
            //oos.flush();
			//Modificado para reestablecer el BufferedOutputStream
			oos.reset();
			throw e;
		}finally{
			try{rs.close();}catch(Exception e){};
			try{ps.close();}catch(Exception e){};
			try{if (bCerrarConexion){conn.close();CPoolDatabase.releaseConexion();}}catch(Exception e){};
		}
		return iRet;
	}

	public void loadDomains() throws SQLException{
		String sSQL="select dom.id as id_dom, dom.name as name_dom,"
			+      " dn.id as id_dn, dn.parentdomain, dn.type, dn.id_municipio, dn.pattern, dn.id_description,"
			+      " dic.locale, dic.traduccion "
			+"from domainnodes dn left join domains dom on dn.id_domain=dom.id "
			+                    "left join dictionary dic on dn.id_description=dic.id_vocablo "
			+"order by id_municipio asc,id_dom asc,id_dn";
		Connection conn=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		try{
			conn=openConnection();
			ps=conn.prepareStatement(sSQL);
			rs=ps.executeQuery();
			ListaDomain lDomains=null;
			ListaDomainNode lNodes=null;
			DomainNode node=null;
			int iLastMunicipio=-1;
			int iMunicipio=-1;
			int iLastNode=-1;
			int iLastDomain=-1;
			int iNode=0;
			GeopistaDomains.clear();
			while (rs.next()){
				try{
					iMunicipio=rs.getInt("id_municipio");
					if (iMunicipio!=iLastMunicipio){
						if (lNodes!=null){
							lNodes.restructurar();
							GeopistaDomains.addDomains(iLastMunicipio,lDomains);
						}
						iLastMunicipio=iMunicipio;
						lDomains=new ListaDomain();
						lNodes=new ListaDomainNode();
					}
					int idDomain=rs.getInt("id_dom");
					String sParent=rs.getString("parentdomain");
					if (iLastDomain!=idDomain){
						lDomains.add(new Domain(new Integer(idDomain).toString(),rs.getString("name_dom")));
						iLastDomain=idDomain;
					}
					String sIdDescription=rs.getString("id_description");
					iNode=rs.getInt("id_dn");
					if (!rs.wasNull()){
						// Crear Node
						if (iNode!=iLastNode){
							iLastNode=iNode;
							node= new DomainNode(String.valueOf(iNode),
									sIdDescription,
									rs.getInt("type"),
									sParent,
									String.valueOf(iMunicipio),
									new Integer(idDomain).toString(),
									rs.getString("pattern"));
							lNodes.add(node);
							if (sParent==null)
								lDomains.get(new Integer(idDomain).toString()).addNode(node);
						}
						if (sIdDescription!=null)
							node.addTerm(rs.getString("locale"), rs.getString("traduccion"));
					}
				}catch (SQLException sqle){
					throw sqle;
				}catch (Exception e){
				}
			}
			lNodes.restructurar();
			GeopistaDomains.addDomains(iLastMunicipio,lDomains);
		}finally{
			try{rs.close();}catch(Exception e){};
			try{ps.close();}catch(Exception e){};
			try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
		}
	}

	public Geometry municipioGeometry(int iMunicipio){
		Geometry gRet=null;
		String sSQL="select \"GEOMETRY\" from municipios where id=?";
		Connection conn=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		try{
			conn=openConnection();
			ps=conn.prepareStatement(sSQL);
			ps.setInt(1,iMunicipio);
			rs=ps.executeQuery();
			if (rs.next()){
				PGgeometry pgGeometry=(PGgeometry)rs.getObject("GEOMETRY");
				StringBuffer sbGeometry = new StringBuffer();
				if (pgGeometry!=null){
					pgGeometry.getGeometry().outerWKT(sbGeometry);
					try{
	
						Feature f=(Feature)new com.vividsolutions.jump.io.WKTReader().read(new StringReader(sbGeometry.toString())).getFeatures().get(0);
						gRet=f.getGeometry();
					}catch (Exception e){
					}
				}
			}
		}catch(SQLException se){
		}finally{
			try{rs.close();}catch(Exception e){};
			try{ps.close();}catch(Exception e){};
			try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
		}
		return gRet;
	}

	public int lockLayer(int iMunicipio, String sLayer, int iUser, Geometry geom) throws Exception{
		// TODO: Geometry del Municipio si geom==null 
		int iRet=AdministradorCartografiaClient.LOCK_LAYER_LOCKED;
		String sSQL="insert into locks_layer (municipio,layer,\"GEOMETRY\",user_id,ts) "
			+"values (?,?,?,?,?)";
		Connection conn=null;
		PreparedStatement ps=null;
		try{
			iRet=canLockLayer(iMunicipio,sLayer,geom,iUser);
			if (iRet==0){
				conn=openConnection();
				ps=conn.prepareStatement(sSQL);
				ps.setInt(1,iMunicipio);
				ps.setString(2,sLayer);
				ps.setObject(3,new PGgeometry(geom.toText()));
				ps.setInt(4,iUser);
				ps.setTimestamp(5,new Timestamp(System.currentTimeMillis()));
				ps.executeUpdate();
			}
		}catch(SQLException se){
			iRet=AdministradorCartografiaClient.LOCK_LAYER_ERROR;
		}finally{
			try{ps.close();}catch(Exception e){};
			try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
		}
		return iRet;
	}

	private String replaceSRID(String sQuery,int iMunicipio) throws ACException{
		String sRet=sQuery;
		if (sQuery!=null){
			Pattern pattern=Pattern.compile("\\?S");
			sRet= pattern.matcher(sQuery).replaceAll(String.valueOf(srid.getSRID(iMunicipio)));
		}
		return sRet;
	}

	public int unlockLayer(int iMunicipio, String sLayer, int iUser){
		int iRet=AdministradorCartografiaClient.UNLOCK_LAYER_UNLOCKED;
		String sSQL="delete from locks_layer where " +
		"municipio=? and " +
		"layer=? and " +
		"user_id=?";
		Connection conn=null;
		PreparedStatement ps=null;
		try{            
			conn=openConnection();
			ps=conn.prepareStatement(sSQL);
			ps.setInt(1,iMunicipio);
			ps.setString(2,sLayer);
			ps.setInt(3,iUser);
			iRet=ps.executeUpdate();

		}catch(SQLException se){
			iRet=AdministradorCartografiaClient.UNLOCK_LAYER_ERROR;
		}finally{
			try{ps.close();}catch(Exception e){};
			try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
		}
		return iRet;
	}


	public int canLockLayer(int iMunicipio, String sLayer, Geometry geom, int iUser) throws SQLException, ACException{
		// Comparar bloqueos de layer y geometrias de features...
		int iRet=0;
		// Comparar ids de Feature y bloqueos de layer con la geometria de iFeature...
		int iLayerLock=layerLocked(iMunicipio,sLayer,geom);
		if(iLayerLock!=-1)
			return (iLayerLock==iUser?AdministradorCartografiaClient.LOCK_LAYER_OWN
					:AdministradorCartografiaClient.LOCK_LAYER_OTHER);
		//Obtener la tabla donde esta la geometria...
		String sSQLTable="select t.name as table,l.id_layer from tables t,columns c,attributes a,layers l " +
		"where a.id_layer=l.id_layer and l.name=? and c.id_table=t.id_table and c.name='GEOMETRY' and a.id_column=c.id";
		Connection conn=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		try{
			conn=openConnection();
			ps=conn.prepareStatement(sSQLTable);
			ps.setString(1,sLayer);
			rs=ps.executeQuery();
			String sTable=null;
			if (rs.next()){
				sTable=rs.getString("table");
			}
			rs.close();
			ps.close();
			String sSQLGeom="select ll.user_id from locks_feature ll,\""+sTable+"\" t " +
			"where ll.municipio=? and ll.layer=? and t.id=ll.feature_id " +
			" and t.id_municipio=? and t.\"GEOMETRY\" && geometryfromtext(?,"+srid.getSRID(iMunicipio)+")";
			ps=conn.prepareStatement(sSQLGeom);
			ps.setInt(1,iMunicipio);
			ps.setString(2,sLayer);
			ps.setInt(3,iMunicipio);
			ps.setString(4,geom.toText());
			rs=ps.executeQuery();
			if (rs.next() && rs.getInt("user_id")!=iUser)
				iRet=AdministradorCartografiaClient.LOCK_FEATURE_OTHER;
		}finally{
			try{rs.close();}catch(Exception e){};
			try{ps.close();}catch(Exception e){};
			try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
		}
		return iRet;
	}

	public int canLockFeature(int iMunicipio, String sLayer, int iFeature, int iUser,Connection connection) throws SQLException, ACException{
		int iRet=0;
		boolean bCerrarConexion=true;
		// Comparar ids de Feature y bloqueos de layer con la geometria de iFeature...
		int iFeatureLock=featureLocked(iMunicipio,sLayer,iFeature,connection);
		if(iFeatureLock!=-1)
			return (iFeatureLock==iUser?AdministradorCartografiaClient.LOCK_FEATURE_OWN
					:AdministradorCartografiaClient.LOCK_FEATURE_OTHER);
		//Obtener la tabla donde esta la geometria...
		String sSQLTable="select t.name as table,l.id_layer from tables t,columns c,attributes a,layers l " +
		"where a.id_layer=l.id_layer and l.name=? and c.id_table=t.id_table and c.name='GEOMETRY' and a.id_column=c.id";

		Connection conn=null;

		PreparedStatement ps=null;
		ResultSet rs=null;
		try{
			if (connection!=null)
			{
				conn=connection;
				bCerrarConexion=false;
			}
			else
			{
				conn=openConnection();
				bCerrarConexion=true;
			}

			ps=conn.prepareStatement(sSQLTable);
			ps.setString(1,sLayer);
			rs=ps.executeQuery();
			String sTable=null;
			if (rs.next()){
				sTable=rs.getString("table");
			}
			rs.close();
			ps.close();
			String sSQLGeom="select ll.user_id from locks_layer ll,\""+sTable+"\" t " +
			"where t.id=? and ll.municipio=? and ll.layer=? " +
			" and t.\"GEOMETRY\" && setsrid(ll.\"GEOMETRY\","+srid.getSRID(iMunicipio)+");";
			ps=conn.prepareStatement(sSQLGeom);
			ps.setInt(1,iFeature);
			ps.setInt(2,iMunicipio);
			ps.setString(3,sLayer);
			rs=ps.executeQuery();
			if (rs.next())
				iRet=(rs.getInt("user_id")!=iUser)? AdministradorCartografiaClient.LOCK_LAYER_OTHER
						: AdministradorCartografiaClient.LOCK_LAYER_OWN;
		}finally{
			try{rs.close();}catch(Exception e){};
			try{ps.close();}catch(Exception e){};
			try{if (bCerrarConexion){conn.close();CPoolDatabase.releaseConexion();}}catch(Exception e1){};
		}
		return iRet;
	}

	/** Implementación de canLockFeature para inserciones */
	public int canLockFeature(Connection conn, int iMunicipio, String sLayer, String sGeometry, int iUser) throws SQLException, ACException{
		int iRet=0;
		int iSRID=srid.getSRID(iMunicipio);
		String sSQL="select ll.user_id from locks_layer ll " +
		"where ll.municipio=? and ll.layer=? " +
		" and geometryFromText(?,"+iSRID+") && setsrid(locks_layer.\"GEOMETRY\","+iSRID+")";

		//Connection conn=null;

		PreparedStatement ps=null;
		ResultSet rs=null;
		try{
			conn=openConnection();
			ps=conn.prepareStatement(sSQL);
			ps.setInt(1,iMunicipio);
			ps.setString(2,sLayer);
			ps.setString(3,sGeometry);
			rs=ps.executeQuery();
			if (rs.next())
				iRet=(rs.getInt("user_id")!=iUser)? AdministradorCartografiaClient.LOCK_LAYER_OTHER
						: AdministradorCartografiaClient.LOCK_LAYER_OWN;
		}finally{
			try{rs.close();}catch(Exception e){};
			try{ps.close();}catch(Exception e){};
		}
		return iRet;
	}

	/** devuelve -1 si no bloqueado, id_usuario si hay bloqueo */
	private int layerLocked(int iMunicipio, String sLayer, Geometry geom) throws SQLException{
		int iRet=-1;
		String sSQL="select user_id,ts from locks_layer " +
		"where " +
		"municipio=? and " +
		"layer=? " +
		(geom!=null? "and \"GEOMETRY\" && ?"
				: "");
		Connection conn=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		try{
			conn=openConnection();
			ps=conn.prepareStatement(sSQL);
			ps.setInt(1,iMunicipio);
			ps.setString(2,sLayer);
			if (geom!=null)
				ps.setObject(3,new PGgeometry(geom.toText()));
			rs=ps.executeQuery();
			if (rs.next()){
				iRet=rs.getInt("user_id");
			}
		}finally{
			try{rs.close();}catch(Exception e){};
			try{ps.close();}catch(Exception e){};
			try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
		}
		return iRet;
	}

	public FeatureLockResult lockFeature(int iMunicipio, List layers, List featuresIds, int iUserId) throws Exception{    	
		FeatureLocker featureLocker = new FeatureLocker();

		return featureLocker.lock(iMunicipio, layers, featuresIds, iUserId);    	
	}

	public List unlockFeature(int iMunicipio, List layers, List featuresIds, int iUser){
		ArrayList unlockResultList = new ArrayList();

		int unlockResult = AdministradorCartografiaClient.UNLOCK_FEATURE_UNLOCKED;

		String sqlDeleteFeatureLock="delete from locks_feature where " +
		"municipio=? and " +
		"layer=? and " +
		"user_id=? and " +
		"feature_id=?";

		//Comprobacion de si el feature esta cerrado
		String sqlCheckFeatureLock="select user_id,ts from locks_feature " +
		"where " +
		"municipio=? and " +
		"layer=? and " +
		"feature_id=?";

		PreparedStatement psDeleteFeatureLock = null;
		PreparedStatement psCheckFeatureLock = null;
		ResultSet rs = null;
		Connection conn=null;

		try{
			conn = openConnection();        	    
			psDeleteFeatureLock = conn.prepareStatement(sqlDeleteFeatureLock);
			psCheckFeatureLock = conn.prepareStatement(sqlCheckFeatureLock);

			for (int i = 0; i < featuresIds.size(); i++){
				String layer = (String) layers.get(i);
				int feature = ((Integer) featuresIds.get(i)).intValue();

				int lockingUserId = -1;

				psCheckFeatureLock.setInt(1,iMunicipio);
				psCheckFeatureLock.setString(2,layer);
				psCheckFeatureLock.setInt(3,feature);

				rs = psCheckFeatureLock.executeQuery();
				if (rs.next()){
					lockingUserId=rs.getInt("user_id");
				}
				rs.close();

				if (lockingUserId==-1){
					unlockResult = AdministradorCartografiaClient.UNLOCK_FEATURE_NOTLOCKED;
				}else{        				        				
					psDeleteFeatureLock.setInt(1,iMunicipio);
					psDeleteFeatureLock.setString(2,layer);
					psDeleteFeatureLock.setInt(3,iUser);
					psDeleteFeatureLock.setInt(4,feature);

					unlockResult = psDeleteFeatureLock.executeUpdate();
				}
				unlockResultList.add(new Integer(unlockResult ));
			}
		} catch(SQLException se){
			unlockResult = AdministradorCartografiaClient.UNLOCK_FEATURE_ERROR;
			unlockResultList.add(new Integer(unlockResult ));  
		} finally {
			try{psDeleteFeatureLock.close();}catch(Exception e){};
			try{psCheckFeatureLock.close();}catch(Exception e){};
			try{rs.close();}catch(Exception e){};
			try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
		}

		return unlockResultList;
	}

	/** devuelve -1 si no bloqueado, id_usuario si hay bloqueo */
	private int featureLocked(int iMunicipio, String sLayer, int iFeature,Connection connection) throws SQLException{
		int iRet=-1;
		boolean bCerrarConexion=true;
		String sSQL="select user_id,ts from locks_feature " +
		"where " +
		"municipio=? and " +
		"layer=? and " +
		"feature_id=?";
		Connection conn=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		try{
			if (connection!=null)
			{
				conn=connection;
				bCerrarConexion=false;
			}
			else
			{
				conn=openConnection();
				bCerrarConexion=true;
			}
			ps=conn.prepareStatement(sSQL);
			ps.setInt(1,iMunicipio);
			ps.setString(2,sLayer);
			ps.setInt(3,iFeature);
			rs=ps.executeQuery();
			if (rs.next()){
				iRet=rs.getInt("user_id");
			}
		}finally{
			try{rs.close();}catch(Exception e){};
			try{ps.close();}catch(Exception e){};            
			try{if (bCerrarConexion){conn.close();CPoolDatabase.releaseConexion();}}catch(Exception e1){};
		}
		return iRet;
	}

	/** Carga el esquema de una capa en el objeto ACLayer 
	 * @param acl */
	private void readSchema(ACLayer layer, String sLocale, Connection connection, ISesion sesion)throws Exception
	{
		boolean bCerrarConexion=true;
		int iMunicipio=Integer.parseInt(sesion.getIdMunicipio());

		/** Incidencia [365] El administrador de cartografia no funciona para otros idiomas */
		String sSQL="select t.id_table,c.id as id_column,c.name as c_name,c.\"Length\" as c_length,c.\"Precision\" as c_precision,c.\"Scale\" as c_scale,c.\"Type\" as c_type,cd.id_domain as cd_id_domain, cd.level as cd_level,t.name as t_name,a.position,a.editable,d.traduccion,t.geometrytype,d.locale "+
		"from attributes a inner join columns c on (a.id_column=c.id) "+
		"left join columns_domains cd on (c.id=cd.id_column) "+
		"inner join tables t on (c.id_table=t.id_table) "+
		"inner join dictionary d on (a.id_alias=d.id_vocablo) "+
		"where a.id_layer=? "+                       
		"order by a.position,t.id_table,c.id";

		Connection conn=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		try
		{
			if (connection!=null)
			{
				conn=connection;
				bCerrarConexion=false;
			}
			else
			{
				conn=openConnection();
				bCerrarConexion=true;
			}
			boolean bEditable=true;
			bEditable=checkPermission(sesion,layer.getACL(),Const.PERM_LAYER_WRITE);

			ps=conn.prepareStatement(sSQL);
			ps.setInt(1,layer.getId_layer());
			rs=ps.executeQuery();
			int iCurrentTable=-1;
			int iCurrentColumn=-1;
			int iTable=0;
			int iColumn=0;
			ACTable table=null;
			ACColumn column=null;
			ACAttribute attribute=null;

			/** Incidencia [365] El administrador de cartografia no funciona para otros idiomas */
			int iCurrentAttributePosition= -1;
			int iLastAttributePosition= -1;
			int iCurrentAttributeLocale= -1;
			int iLastAttributeLocale= -1;
			/**/

			while (rs.next()){
				iTable=rs.getInt("id_table");
				iColumn=rs.getInt("id_column");
				if (iTable!=iCurrentTable){
					int iGeomType=rs.getInt("geometrytype");
					if (rs.wasNull())
						iGeomType=-1;
					table=new ACTable(rs.getInt("id_table"),rs.getString("t_name"),iGeomType);
					iCurrentTable=iTable;
				}
				if (iColumn!=iCurrentColumn){
					column=new ACColumn(iColumn,rs.getString("c_name"),table,null, rs.getInt("cd_level"),
							rs.getInt("c_length"),rs.getInt("c_precision"),rs.getInt("c_scale"),rs.getInt("c_type"));
					iCurrentTable=iTable;
				}

				attribute=new ACAttribute();
				attribute.setColumn(column);
				attribute.setName(rs.getString("traduccion"));
				attribute.setPosition(rs.getInt("position"));
				attribute.setEditable(rs.getBoolean("editable")&&bEditable);
				ACDomain domain=GeopistaDomains.getDomain(iMunicipio,rs.getInt("cd_id_domain"));
				column.setDomain(domain);
				/** Comentado por incidencia [365] */

				/** Incidencia [365] El atributo en sLocale, si no es_ES, si no el idioma que exista. */
				iCurrentAttributePosition= rs.getInt("position");
				/** Opcion de idioma encontrada en el atributo */
				iCurrentAttributeLocale= getOpcionLocale(rs.getString("locale"), sLocale);
				/** Confirmado por JPablo: un schema no puede tener 2 atributos distintos en la misma posicion */
				if ((iCurrentAttributePosition != iLastAttributePosition) ||
						((iCurrentAttributePosition == iLastAttributePosition) && (iCurrentAttributeLocale < iLastAttributeLocale))){
					layer.addAttribute(attribute);
					if (column.getName().equals("GEOMETRY"))
						layer.setGeometryAttribute(attribute.getName());

					iLastAttributeLocale= iCurrentAttributeLocale;
					iLastAttributePosition= iCurrentAttributePosition;
				}
			}
		}finally{
			try{rs.close();}catch(Exception e){};
			try{ps.close();}catch(Exception e){};
			try{if (bCerrarConexion){conn.close();CPoolDatabase.releaseConexion();}}catch(Exception e){};
		}
	}

	/** Carga el esquema de una capa en el objeto ACLayer 
	 * @param acl */
	
	private void readNewSchema(ACLayer layer, String sLocale, Connection connection, ISesion sesion, GeopistaAcl acl)throws Exception
	{
		
		boolean bCerrarConexion=true;
		int iEntidad=Integer.parseInt(sesion.getIdEntidad());

		/** Incidencia [365] El administrador de cartografia no funciona para otros idiomas */
		String sSQL="select t.id_table,c.id as id_column,c.name as c_name,c.\"Length\" as c_length,c.\"Precision\" as c_precision,c.\"Scale\" as c_scale,c.\"Type\" as c_type,cd.id_domain as cd_id_domain, cd.level as cd_level,t.name as t_name,a.position,a.editable,d.traduccion,t.geometrytype,d.locale "+
		"from attributes a inner join columns c on (a.id_column=c.id) "+
		"left join columns_domains cd on (c.id=cd.id_column) "+
		"inner join tables t on (c.id_table=t.id_table) "+
		"inner join dictionary d on (a.id_alias=d.id_vocablo) "+
		"where a.id_layer=? "+                        
		"order by a.position,t.id_table,c.id";

		Connection conn=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		try
		{
			if (connection!=null)
			{
				conn=connection;
				bCerrarConexion=false;
			}
			else
			{
				conn=openConnection();
				bCerrarConexion=true;
			}
			boolean bEditable=true;
			if (acl==null)
				bEditable=checkPermission(sesion,layer.getACL(),Const.PERM_LAYER_WRITE,conn);
			else
				bEditable=acl.checkPermission(new GeopistaPermission(Const.PERM_LAYER_WRITE));

			ps=conn.prepareStatement(sSQL);
			ps.setInt(1,layer.getId_layer());
			rs=ps.executeQuery();
			int iCurrentTable=-1;
			int iCurrentColumn=-1;
			int iTable=0;
			int iColumn=0;
			ACTable table=null;
			ACColumn column=null;
			ACAttribute attribute=null;

			/** Incidencia [365] El administrador de cartografia no funciona para otros idiomas */
			int iCurrentAttributePosition= -1;
			int iLastAttributePosition= -1;
			int iCurrentAttributeLocale= -1;
			int iLastAttributeLocale= -1;
			/**/
			
			boolean tieneRevisionActual=false;
			int iTableRevisionActual=0;
			String nombreTableRevisionActual=null;
			int posicionRevisionActual=0;

			while (rs.next()){
				iTable=rs.getInt("id_table");
				iColumn=rs.getInt("id_column");
				if (iTable!=iCurrentTable){
					int iGeomType=rs.getInt("geometrytype");
					if (rs.wasNull())
						iGeomType=-1;
					table=new ACTable(rs.getInt("id_table"),rs.getString("t_name"),iGeomType);
					iCurrentTable=iTable;
				}
				if (iColumn!=iCurrentColumn){
					column=new ACColumn(iColumn,rs.getString("c_name"),table,null, rs.getInt("cd_level"),
							rs.getInt("c_length"),rs.getInt("c_precision"),rs.getInt("c_scale"),rs.getInt("c_type"));
					iCurrentTable=iTable;
				}
				
				//Si el schema tiene revision actual lo marcamos
				if (rs.getString("c_name").equals("revision_actual")){
					tieneRevisionActual=true;
					iTableRevisionActual=iTable;
					nombreTableRevisionActual=rs.getString("t_name");
					posicionRevisionActual=rs.getInt("position")+1;
				}

				attribute=new ACAttribute();
				attribute.setColumn(column);
				String name = rs.getString("traduccion");
				if (name != null && name.equals("id_municipio"))
					positionIdMunicipio = new Integer(rs.getInt("position"));
				attribute.setName(name);
				attribute.setPosition(rs.getInt("position"));
				attribute.setEditable(rs.getBoolean("editable")&&bEditable);
				ACDomain domain=GeopistaDomains.getDomain(iEntidad,rs.getInt("cd_id_domain"));
				column.setDomain(domain);
				/** Comentado por incidencia [365] */

				/** Incidencia [365] El atributo en sLocale, si no es_ES, si no el idioma que exista. */
				iCurrentAttributePosition= rs.getInt("position");
				/** Opcion de idioma encontrada en el atributo */
				iCurrentAttributeLocale= getOpcionLocale(rs.getString("locale"), sLocale);
				/** Confirmado por JPablo: un schema no puede tener 2 atributos distintos en la misma posicion */
				if ((iCurrentAttributePosition != iLastAttributePosition) ||
						((iCurrentAttributePosition == iLastAttributePosition) && (iCurrentAttributeLocale < iLastAttributeLocale))){
					layer.addAttribute(attribute);
					layer.setEditable(bEditable && layer.isEditable());
					if (column.getName().equals("GEOMETRY"))
						layer.setGeometryAttribute(attribute.getName());

					iLastAttributeLocale= iCurrentAttributeLocale;
					iLastAttributePosition= iCurrentAttributePosition;
				}
			}
			
			/*ACAttribute fakeAttribute=new ACAttribute();
			ACTable faketable=new ACTable(1,"rutas_trekking",3);
			ACColumn fakecolumn=new ACColumn(1,"Test",faketable,null, 0,0,0,0,2);
			fakeAttribute.setColumn(fakecolumn);
			fakeAttribute.setName("Test");
			fakeAttribute.setPosition(3);
			fakeAttribute.setEditable(false);
			layer.addAttribute(fakeAttribute);
			//ACDomain domain=GeopistaDomains.getDomain(iEntidad,rs.getInt("cd_id_domain"));
			//column.setDomain(domain);*/
		}finally{
			try{rs.close();}catch(Exception e){};
			try{ps.close();}catch(Exception e){};
			try{if (bCerrarConexion){conn.close();CPoolDatabase.releaseConexion();}}catch(Exception e){};
		}
	}

	public int insertFeature(int iMunicipio,String sLayer,String sLocale,ACFeatureUpload upload, ObjectOutputStream oos,ISesion sesion) throws Exception{
		return insertFeature(iMunicipio,sLayer,sLocale,upload,oos,sesion,true);
	}

	public int insertFeature(int iMunicipio,String sLayer,String sLocale,ACFeatureUpload upload, ObjectOutputStream oos,ISesion sesion,boolean bValidateData) throws Exception{
	    return insertFeature(iMunicipio,sLayer,sLocale,upload,oos,sesion,bValidateData, null);
	}
	
	public int insertFeature(int iMunicipio,String sLayer,String sLocale,ACFeatureUpload upload, ObjectOutputStream oos,ISesion sesion,boolean bValidateData, Integer srid_destino) throws Exception{
		int iRet=-1;
		ACLayer acLayer=null;

		/** Incidencia [365] El administrador de cartografia no funciona para otros idiomas */
		String sSQL="select q.id,q.selectquery,q.updatequery,q.insertquery,q.deletequery,"
			+"l.name,l.acl,l.id_layer,d.traduccion,d.locale "
			+"from queries q,layers l, dictionary d "
			+"where q.databasetype="+DATABASETYPE+" and l.name=? and "
			+"q.id_layer=l.id_layer and  "
			+"l.id_name=d.id_vocablo ";

		Connection conn=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		try{
			conn=openConnection();
			ps=conn.prepareStatement(sSQL);
			ps.setString(1,sLayer);
			rs=ps.executeQuery();

			if (rs.next()){
				/** Comentado por Incidencia [365] */

				/** Incidencia [365] */
				String sInsertSQL= "";
				String sSelectSQL= "";
				int iCurrentLayerLocale= -1;
				/** aseguramos que entra la primera vez */
				int iLastLayerLocale=4;
				boolean mejorOpcion= false;
				do{
					iCurrentLayerLocale= getOpcionLocale(rs.getString("locale"), sLocale);
					/** Priorizamos los idiomas: preferencia de usuario, es_ES, resto de idiomas */
					if (iCurrentLayerLocale < iLastLayerLocale){
						acLayer=new ACLayer(rs.getInt("id_layer"),rs.getString("traduccion"),rs.getString("name"),replaceSRID(rs.getString("selectquery"),iMunicipio));
						sInsertSQL=rs.getString("insertquery");
						sSelectSQL=rs.getString("selectquery");
						acLayer.setACL(rs.getLong("acl"));

						iLastLayerLocale= iCurrentLayerLocale;
						if (iCurrentLayerLocale == 1) mejorOpcion= true;
					}
				}while ((rs.next()) && (!mejorOpcion));
				/**/
				//Comprobamos si tiene permiso para poder realizar la operacion
				GeopistaAcl acl=getPermission(sesion,acLayer.getACL(),conn);

				readNewSchema(acLayer,sLocale, conn, sesion,acl);
				rs.close();
				ps.close();

				//Comprobamos si tiene permiso para poder realizar la operacion
				if (!checkPerm(sesion,acl,Const.PERM_LAYER_ADD))
					throw new PermissionException(Const.PERM_LAYER_ADD);

				ArrayList lstValidateFeatures = null;
				if (upload!= null && bValidateData)
				{
					lstValidateFeatures = getValidateFeatures(acLayer,sLocale,conn);
				}

				acLayer.setInsertQuery(sInsertSQL);
				
				if (lstValidateFeatures != null){
					for (Iterator iterValidateFeatures = lstValidateFeatures.iterator();iterValidateFeatures.hasNext();){
						AbstractValidator validateFeatures = (AbstractValidator)iterValidateFeatures.next();
						if (validateFeatures instanceof VersionValidator){
							Version version = sesion.getVersion();
							if (version == null)
								version = new Version();
							version.setIdTable(this.obtenerIdTabla(conn, acLayer));
							validateFeatures.setVersion(version);
							acLayer.setIdUsuario(Integer.parseInt(sesion.getIdUser()));
						}
						
						if (sesion.getEstadoValidacion()==Const.ESTADO_VALIDO)
							validateFeatures.beforeInsert(upload,acLayer);
						else
							validateFeatures.beforeInsertTemporal(upload,acLayer,sesion.getEstadoValidacion());
						
					}
				}

				sInsertSQL = acLayer.getInsertQuery();
				
				//REFACTORIZACION ORACLE SQLParser sqlParser=new SQLParser(conn,srid);
				SQLParserPostGIS sqlParser=new SQLParserPostGIS(conn,srid);
				conn.setAutoCommit(false);

				String sTable=acLayer.findPrimaryTable();
				//Si la feature que me llega tiene el SistemID se lo pongo como clave primaria sino
				//le pongo nextval
				String sSelectSQLId = " and \""+sTable+"\".ID=currval('SEQ_"+sTable.toUpperCase() +"')";
				if (sInsertSQL.indexOf("?PK")>=0)
				{
					try
					{
						new Long(upload.getId());
						sInsertSQL=SQLParser.replaceString(sInsertSQL,"?PK"," '"+upload.getId()+"' ");
						sSelectSQLId = " and \""+sTable+"\".ID="+upload.getId();
					}catch (Exception e)
					{
						sInsertSQL=SQLParser.replaceString(sInsertSQL,"?PK"," nextval('SEQ_"+sTable.toUpperCase() +"') ");
					}
				}
				
				int sridDefecto = this.getSRIDDefecto(true, conn, sesion.getIdEntidad());
				int sridInicial = this.getSRIDDefecto(false, conn, sesion.getIdEntidad());
				PreparedStatement[] apsUpdate=sqlParser.newUpdate(sesion.getAlMunicipios(),sInsertSQL,acLayer,upload, srid_destino,iMunicipio,sridDefecto,sridInicial);
				
				/*if (true)
					throw new Exception();*/
			
				for(int i=0;i<apsUpdate.length;i++){
					try{
						logger.info("Trying to execute: "+((org.postgresql.PGStatement)apsUpdate[i]).toString());				      
					}
					catch (Exception e){}
					apsUpdate[i].executeUpdate();
					apsUpdate[i].close();
				}
				conn.commit();

				sesion.setVersion(null);
				iRet=returnFeatures(Integer.parseInt(sqlParser.getIdMunicipio(upload, acLayer.getAttributes(),iMunicipio)),sSelectSQL
						+ sSelectSQLId,acLayer,null,null,oos,conn,sesion,acl, null, true, srid_destino);

				if (lstValidateFeatures != null){
					for (Iterator iterValidateFeatures = lstValidateFeatures.iterator();iterValidateFeatures.hasNext();){
						AbstractValidator validateFeatures = (AbstractValidator)iterValidateFeatures.next();
						validateFeatures.afterInsert(upload,acLayer);
					}
				}

			} else
				throw new ObjectNotFoundException("layer: "+sLayer+" ("+sLocale+")");
		}catch(Exception e){
			try{
				conn.rollback();
			}catch(Exception ex){
				e.getMessage();
			};

			oos.writeObject(new ACException(e));
			//Modificado para reestablecer el BufferedOutputStream
			oos.reset();
			
			throw e;
		}finally{
			try{rs.close();}catch(Exception e){};
			try{ps.close();}catch(Exception e){};
			try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
		}
		return iRet;
	}

	public int updateFeature(int iMunicipio,String sLayer,String sLocale,ACFeatureUpload upload, boolean bDelete, ObjectOutputStream oos, ISesion sesion)throws Exception{
		return updateFeature(iMunicipio,sLayer,sLocale,upload,bDelete,oos,sesion,true);
	}
	
	public int updateFeature(int iMunicipio,String sLayer,String sLocale,ACFeatureUpload upload, boolean bDelete, ObjectOutputStream oos, ISesion sesion,boolean bValidateData)throws Exception{
	    return updateFeature(iMunicipio,sLayer,sLocale,upload,bDelete,oos,sesion,true,null);
	}

	public int updateFeature(int iMunicipio,String sLayer,String sLocale,ACFeatureUpload upload, boolean bDelete, ObjectOutputStream oos, ISesion sesion,boolean bValidateData, Integer srid_destino)throws Exception{
		int iRet=-1;
		ACLayer acLayer=null;
		
		

		/** Incidencia [365] El administrador de cartografia no funciona para otros idiomas */
		String sSQL="select q.id,q.selectquery,q.updatequery,q.insertquery,q.deletequery,"
			+"l.id_layer,l.name,l.acl,d.traduccion,d.locale "
			+"from queries q,layers l, dictionary d "
			+"where q.databasetype="+DATABASETYPE+" and l.name=? and "
			+"q.id_layer=l.id_layer and  "
			+"l.id_name=d.id_vocablo ";

		Connection conn=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sInsertSQL = null;
		String sSelectSQL = null;
		String sUpdateSQL = null;
		String sDeleteSQL = null;
		try{
			conn=openConnection();
			ps=conn.prepareStatement(sSQL);
			ps.setString(1,sLayer);
			rs=ps.executeQuery();

			if (rs.next()){
				/** Comentado por Incidencia [365] */

				/** Incidencia [365] */
				int iCurrentLayerLocale= -1;
				/** aseguramos que entra la primera vez */
				int iLastLayerLocale=4;
				boolean mejorOpcion= false;

				do{
					iCurrentLayerLocale= getOpcionLocale(rs.getString("locale"), sLocale);
					/** Priorizamos los idiomas: preferencia de usuario, es_ES, resto de idiomas */
					if (iCurrentLayerLocale < iLastLayerLocale){
						acLayer=new ACLayer(rs.getInt("id_layer"),rs.getString("traduccion"),rs.getString("name"),replaceSRID(rs.getString("selectquery"),iMunicipio));
						acLayer.setACL(rs.getLong("acl"));
						sSQL=rs.getString(bDelete?"deletequery":"updatequery");
						sInsertSQL=rs.getString("insertquery");
						sSelectSQL=rs.getString("selectquery");
						sUpdateSQL=rs.getString("updatequery");
						sDeleteSQL=rs.getString("deletequery");
						iLastLayerLocale= iCurrentLayerLocale;
						if (iCurrentLayerLocale == 1) mejorOpcion= true;
					}
				}while ((rs.next()) && (!mejorOpcion));
				/**/
				rs.close();
				ps.close();
				GeopistaAcl acl=getPermission(sesion,acLayer.getACL(),conn);

				readNewSchema(acLayer,sLocale, conn, sesion,acl);

				//Comprobamos si tiene permiso para poder realizar la operacion
				if (!checkPerm(sesion,acl,Const.PERM_LAYER_WRITE))
					throw new PermissionException(Const.PERM_LAYER_WRITE);

				ArrayList lstValidateFeatures = null;
				if (upload!= null && bValidateData)
				{
					lstValidateFeatures = getValidateFeatures(acLayer,sLocale,conn);
				}

				if (lstValidateFeatures != null){
					for (Iterator iterValidateFeatures = lstValidateFeatures.iterator();iterValidateFeatures.hasNext();){
						AbstractValidator validateFeatures = (AbstractValidator)iterValidateFeatures.next();
						if (validateFeatures instanceof VersionValidator){
							Version version = sesion.getVersion();
							if (version == null)
								version = new Version();
							version.setIdTable(this.obtenerIdTabla(conn,acLayer));
							validateFeatures.setVersion(version);
							if (bDelete){
								acLayer.setUpdateQuery(sUpdateSQL);
								acLayer.setInsertQuery(sInsertSQL);
								acLayer.setDeleteQuery(sDeleteSQL);
								acLayer.setIdUsuario(Integer.parseInt(sesion.getIdUser()));							
								
								if (sesion.getEstadoValidacion()==Const.ESTADO_VALIDO){
									validateFeatures.beforeDelete(upload, acLayer);
									sSQL = acLayer.getUpdateQuery()+";"+acLayer.getInsertQuery();
								}
								else{
									validateFeatures.beforeDeleteTemporal(upload,acLayer);
									sSQL = acLayer.getUpdateQuery();
								}
								
							}
							else{
								acLayer.setUpdateQuery(sUpdateSQL);
								acLayer.setInsertQuery(sInsertSQL);
								acLayer.setDeleteQuery(sDeleteSQL);
								acLayer.setIdUsuario(Integer.parseInt(sesion.getIdUser()));
								
								if (sesion.getEstadoValidacion()==Const.ESTADO_VALIDO)
									validateFeatures.beforeUpdate(upload, acLayer);
								else if (sesion.getEstadoValidacion()==Const.ESTADO_PUBLICABLE)
									validateFeatures.beforeUpdatePublicable(upload, acLayer,sesion.getEstadoValidacion());
								else if (sesion.getEstadoValidacion()==Const.ESTADO_PUBLICABLE_MOVILIDAD)
									validateFeatures.beforeUpdateAutoPublicable(upload, acLayer,sesion.getEstadoValidacion());
								else	
									validateFeatures.beforeUpdateTemporal(upload,acLayer,sesion.getEstadoValidacion());
								
								sSQL = acLayer.getUpdateQuery()+";"+acLayer.getInsertQuery();
							}
						}
					}
				}
				
				int iLock=canLockFeature(iMunicipio,sLayer,Integer.parseInt(upload.getId()),Integer.parseInt(sesion.getIdUser()),conn);
				if (!(iLock==AdministradorCartografiaClient.LOCK_FEATURE_OWN ||
						iLock==AdministradorCartografiaClient.LOCK_LAYER_OWN   ||
						iLock==AdministradorCartografiaClient.LOCK_LAYER_LOCKED)){
					String sMsg=null;
					switch (iLock){
					case AdministradorCartografiaClient.LOCK_LAYER_OTHER:
						sMsg="locked: layer "+sLayer;
						break;
					case AdministradorCartografiaClient.LOCK_FEATURE_OTHER:
						sMsg="locked: feature";
						break;
					default:
						sMsg="Lock error";
					}
					throw new LockException(sMsg+" ("+iLock+")");
				}
				conn.setAutoCommit(false);
				int sridDefecto = this.getSRIDDefecto(true, conn, sesion.getIdEntidad());
				int sridInicial = this.getSRIDDefecto(false, conn, sesion.getIdEntidad());
				//REFACTORIZACION ORACLE PreparedStatement[] apsUpdate=new SQLParser(conn,srid).newUpdate(iMunicipio,sSQL,acLayer,upload);
				PreparedStatement[] apsUpdate=new SQLParserPostGIS(conn,srid).newUpdate(sesion.getAlMunicipios(),sSQL,acLayer,upload, srid_destino, iMunicipio, sridDefecto, sridInicial);

				/*if (true)
					throw new Exception(); */
				for(int i=0;i<apsUpdate.length;i++){
					try{
						logger.info("Trying to execute: "+((org.postgresql.PGStatement)apsUpdate[i]).toString());				      
					}
					catch (Exception e){}
					int rows=apsUpdate[i].executeUpdate();
					apsUpdate[i].close();
				}
				conn.commit();
				String sTable=acLayer.findPrimaryTable();
				if (!bDelete){
					String sql = null;
					StringBuffer sbSQL=new StringBuffer(sSelectSQL);
					int iOffset=sbSQL.toString().toUpperCase().indexOf(" GROUP BY");
					if (iOffset==-1){
						sql = sbSQL.toString() + " and \"" + sTable + "\".ID=" + upload.getAttValues()[acLayer.findID().getPosition()-1];

						//Devolvemos el elemento insertado de forma temporal
						if (sesion.getEstadoValidacion()==Const.ESTADO_PUBLICABLE)
							//sql = sql + " and \"" + sTable + "\".revision_expirada=" + Const.REVISION_PUBLICABLE;
							sql = sql + " and \"" + sTable + "\".revision_expirada=" + Const.REVISION_VALIDA;
						else if (sesion.getEstadoValidacion()==Const.ESTADO_TEMPORAL)
							sql = sql + " and \"" + sTable + "\".revision_expirada=" + Const.REVISION_TEMPORAL;
						else if (sesion.getEstadoValidacion()==Const.ESTADO_A_PUBLICAR)
							sql = sql + " and \"" + sTable + "\".revision_expirada=" + Const.REVISION_PUBLICABLE;
					}
					else if (sTable!= null && upload.getAttValues()[acLayer.findID().getPosition()-1]!=null){
						
						String insert1=" and \"" + sTable + "\".ID=" + upload.getAttValues()[acLayer.findID().getPosition()-1];

						if (sesion.getEstadoValidacion()==Const.ESTADO_PUBLICABLE)
							//insert1 = insert1 + " and \"" + sTable + "\".revision_expirada=" + Const.REVISION_PUBLICABLE;
							insert1 = insert1 + " and \"" + sTable + "\".revision_expirada=" + Const.REVISION_VALIDA;	
						else if (sesion.getEstadoValidacion()==Const.ESTADO_TEMPORAL)
							insert1 = insert1 + " and \"" + sTable + "\".revision_expirada=" + Const.REVISION_TEMPORAL;
						else if (sesion.getEstadoValidacion()==Const.ESTADO_A_PUBLICAR)
							insert1 = insert1 + " and \"" + sTable + "\".revision_expirada=" + Const.REVISION_PUBLICABLE;
						
						sbSQL.insert(iOffset,insert1);
					}
					sesion.setVersion(null);
					iRet=returnFeatures(iMunicipio,iOffset==-1?sql:sbSQL.toString(),acLayer,null,null,oos,conn,sesion,null, null, true, srid_destino);
					if (iRet==-1){
				      	logger.info("La query de obtencion de features no devuelve ningun elemento");				       
					}
				}
				if (lstValidateFeatures != null){
					for (Iterator iterValidateFeatures = lstValidateFeatures.iterator();iterValidateFeatures.hasNext();){
						AbstractValidator validateFeatures = (AbstractValidator)iterValidateFeatures.next();
						if (bDelete){
							validateFeatures.afterDelete(upload,acLayer);
						}
						else{
							validateFeatures.afterUpdate(upload,acLayer);
						}
					}
				}

			} else
				throw new ObjectNotFoundException("layer: "+sLayer+" ("+sLocale+")");
			if(upload.getAttValues()[acLayer.findID().getPosition()-1] instanceof String)
			{
				String iRetString =  (String) upload.getAttValues()[acLayer.findID().getPosition()-1];
				iRet = Integer.parseInt(iRetString);
			}
			else
			{
				iRet=((Number)upload.getAttValues()[acLayer.findID().getPosition()-1]).intValue();
			}
		}catch(Exception e){
			try{conn.rollback();}catch(Exception ex){};

			try{
				oos.writeObject(new ACException(e));
				//Modificado para reestablecer el BufferedOutputStream
				oos.reset();
				
			}catch(Exception ex){};
			throw e;
		}finally{
			try{rs.close();}catch(Exception e){};
			try{ps.close();}catch(Exception e){};
			try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
		}
		return iRet;
	}

	public int procesarLoteActualizacion(int iMunicipio,String sLayer,String sLocale,ACFeatureUpload[] aUpload, ObjectOutputStream oos, ISesion sesion, int iUserID, boolean bLoadData)throws Exception{
		return procesarLoteActualizacion(iMunicipio,sLayer,sLocale,aUpload,oos,sesion,iUserID,bLoadData,true);
	}
	
	public int procesarLoteActualizacion(int iMunicipio,String sLayer,String sLocale,ACFeatureUpload[] aUpload, ObjectOutputStream oos, ISesion sesion, int iUserID, boolean bLoadData,boolean bValidateData)throws Exception {
	    return procesarLoteActualizacion(iMunicipio,sLayer,sLocale,aUpload,oos,sesion,iUserID,bLoadData,true, null);
	}

	public int procesarLoteActualizacion(int iMunicipio,String sLayer,String sLocale,ACFeatureUpload[] aUpload, ObjectOutputStream oos, ISesion sesion, int iUserID, boolean bLoadData,boolean bValidateData, Integer srid_destino)throws Exception{
		return procesarLoteActualizacion(iMunicipio,sLayer,sLocale,aUpload, oos, sesion, iUserID, bLoadData, bValidateData,  srid_destino,null);

	}
	public int procesarLoteActualizacion(int iMunicipio,String sLayer,String sLocale,ACFeatureUpload[] aUpload, ObjectOutputStream oos, ISesion sesion, int iUserID, boolean bLoadData,boolean bValidateData, Integer srid_destino, Hashtable params)throws Exception{

		int iRet=-1;

		ACLayer acLayer=null;

		//Primero obtenemos las querys de la base de datos

		String sSQL="select q.id,q.selectquery,q.updatequery,q.insertquery,q.deletequery,"
			+"l.id_layer,l.name,l.acl,d.traduccion,d.locale "
			+"from queries q,layers l, dictionary d "
			+"where q.databasetype="+DATABASETYPE+" and l.name=? and "
			+"q.id_layer=l.id_layer and  "
			+"l.id_name=d.id_vocablo ";

		Connection conn=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		try{
			conn=openConnection();
			ps=conn.prepareStatement(sSQL);
			ps.setString(1,sLayer);
			rs=ps.executeQuery();

			if (rs.next()){
				/** Comentado por Incidencia [365] */

				/** Incidencia [365] El administrador de cartografia no funciona para otros idiomas */
				String sSQLDelete= "";
				String sSQLUpdate= "";
				String sSQLInsert= "";


				int iCurrentLayerLocale= -1;
				/** aseguramos que entra la primera vez */
				int iLastLayerLocale=4;
				do{
					iCurrentLayerLocale= getOpcionLocale(rs.getString("locale"), sLocale);
					/** Priorizamos los idiomas: preferencia de usuario, es_ES, resto de idiomas */
					if (iCurrentLayerLocale < iLastLayerLocale){
						acLayer=new ACLayer(rs.getInt("id_layer"),rs.getString("traduccion"),rs.getString("name"),replaceSRID(rs.getString("selectquery"),iMunicipio));
						acLayer.setACL(rs.getLong("acl"));
						sSQLDelete=rs.getString("deletequery");
						sSQLUpdate=rs.getString("updatequery");
						sSQLInsert=rs.getString("insertquery");

						iLastLayerLocale= iCurrentLayerLocale;
						if (iCurrentLayerLocale == 1) break;
					}
				}while (rs.next());
				/**/
				rs.close();
				ps.close();
				GeopistaAcl acl=SesionUtils_LCGIII.getPerfil(sesion,acLayer.getACL(),conn);
				
				readNewSchema(acLayer,sLocale, conn, sesion,acl);
				int[] arlFeatureIDs = new int[aUpload.length];

				ArrayList lstValidateFeatures = null;

				if (bValidateData)
				{
					lstValidateFeatures = getValidateFeatures(acLayer,sLocale,conn);
				}

				String sSQLInsertAux = sSQLInsert;
				for (int i=0;i<aUpload.length;i++){
					//Por si se ha cambiado se resetea el valor de la sentencia INSERT
					sSQLInsert = sSQLInsertAux;
					if (aUpload[i].isNew())
					{
						if (!checkPermission(sesion,acl,Const.PERM_LAYER_ADD))
							throw new PermissionException(Const.PERM_LAYER_ADD);
					}else
					{
						if (!checkPermission(sesion,acl,Const.PERM_LAYER_WRITE))
							throw new PermissionException(Const.PERM_LAYER_WRITE);
						int iLock=canLockFeature(iMunicipio,sLayer,Integer.parseInt(aUpload[i].getId()),Integer.parseInt(sesion.getIdUser()),conn);
						if (!(iLock==AdministradorCartografiaClient.LOCK_FEATURE_OWN ||
								iLock==AdministradorCartografiaClient.LOCK_LAYER_OWN   ||
								iLock==AdministradorCartografiaClient.LOCK_LAYER_LOCKED)){
							String sMsg=null;
							switch (iLock){
							case AdministradorCartografiaClient.LOCK_LAYER_OTHER:
								sMsg="locked: layer "+sLayer;
								break;
							case AdministradorCartografiaClient.LOCK_FEATURE_OTHER:
								sMsg="locked: feature";
								break;
							default:
								sMsg="Lock error";
							}
							throw new LockException(sMsg+" ("+iLock+")");
						}
					}

					if (lstValidateFeatures != null){
						for (Iterator iterValidateFeatures = lstValidateFeatures.iterator();iterValidateFeatures.hasNext();){
							AbstractValidator validateFeatures = (AbstractValidator)iterValidateFeatures.next();
							if (validateFeatures instanceof VersionValidator){
								Version version = new Version();
								version.setIdTable(this.obtenerIdTabla(conn, acLayer));
								version.setCrearVersionado(true);
								validateFeatures.setVersion(version);
								acLayer.setInsertQuery(sSQLInsert);
								acLayer.setIdUsuario(Integer.parseInt(sesion.getIdUser()));
								if (aUpload[i].isNew()){
									validateFeatures.beforeInsert(aUpload[i],acLayer);
									sSQLInsert = acLayer.getInsertQuery();
								}
							}
							validateFeatures.beforeUpdate(aUpload[i],acLayer);
						}
					}

					conn.setAutoCommit(false);
					sSQL=(aUpload[i].isNew()?sSQLInsert:aUpload[i].isDeleted()?sSQLDelete:sSQLUpdate);
					String sTable=acLayer.findPrimaryTable();
					String sSelectSQLId = " and \""+sTable+"\".ID=currval('SEQ_"+sTable.toUpperCase() +"')";
					boolean featureWithId=true;
					if (sSQL.indexOf("?PK")>=0)
					{
						try
						{
							//Esta primera sentencia si el shp no tiene identificador provoca un
							//error en el Exception se coge el valor de nextval
							new Long(aUpload[i].getId());
							sSQL=SQLParser.replaceString(sSQL,"?PK"," '"+aUpload[i].getId()+"' ");
							sSelectSQLId = " and \""+sTable+"\".ID="+aUpload[i].getId();
						}catch (Exception e)
						{
							sSQL=SQLParser.replaceString(sSQL,"?PK"," nextval('SEQ_"+sTable.toUpperCase() +"') ");
							featureWithId=false;
						}
					}
					if (srid_destino == null || srid_destino == -1){
						srid_destino = this.getSRIDDefecto(true, conn, sesion.getIdEntidad());
					}
					//REFACTORIZACION ORACLE 
					int sridDefecto = this.getSRIDDefecto(true, conn, sesion.getIdEntidad());
					int sridInicial = this.getSRIDDefecto(false, conn, sesion.getIdEntidad());
					
					logger.info("SRID Defecto:"+sridDefecto+"-SRID Inicial:"+sridInicial);
					if (params != null && params.get(Const.INITIAL_SRID) != null)
						sridInicial = ((Integer)params.get(Const.INITIAL_SRID)).intValue();
					boolean bImportaciones = false;
					if (params != null){
						bImportaciones = ((Boolean)params.get(Const.KEY_IMPORTACIONES)).booleanValue();
					}
					PreparedStatement[] apsUpdate=null;
					if (bImportaciones)
						apsUpdate = new SQLParserPostGIS(conn,srid).newUpdate(iMunicipio,sSQL,acLayer,aUpload[i], sridInicial, sridDefecto, srid_destino);
					else{
						if (positionIdMunicipio != -1){
							 if (aUpload[i].getAttValues()[positionIdMunicipio-1] != null)
								 if (aUpload[i].getAttValues()[positionIdMunicipio-1] instanceof String)
									 iMunicipio = Integer.parseInt(aUpload[i].getAttValues()[positionIdMunicipio-1].toString());
								 else 
									 if (aUpload[i].getAttValues()[positionIdMunicipio-1] instanceof Integer)
										 iMunicipio = (Integer) aUpload[i].getAttValues()[positionIdMunicipio-1];
									 else 
										 if (aUpload[i].getAttValues()[positionIdMunicipio-1] instanceof Double)
											 iMunicipio = ((Double) aUpload[i].getAttValues()[positionIdMunicipio-1]).intValue();
						}
						apsUpdate = new SQLParserPostGIS(conn,srid).newUpdate(iMunicipio,sSQL,acLayer,aUpload[i], srid_destino, sridDefecto, sridInicial);
					}
					//PreparedStatement[] apsUpdate=new SQLParserPostGIS(conn,srid).newUpdate(sesion.getAlMunicipios(),sSQL,acLayer,aUpload[i]);
					//PreparedStatement[] apsUpdate=new SQLParserPostGIS(conn,srid).newUpdate(sesion.getAlMunicipios(),sSQL,acLayer,upload, srid_destino, iMunicipio, sridDefecto, sridInicial);

					for(int j=0;j<apsUpdate.length;j++){
						apsUpdate[j].executeUpdate();
						apsUpdate[j].close();
					}
					conn.commit();

					if (aUpload[i].isNew()&& bLoadData)
					{
						//Si el shp trae identificador el nextval no se coge por lo que no podemos
						//utilizar el valor de currval en la sentencia.
						if (!featureWithId){
							sSQL = "SELECT currval('SEQ_" + sTable.toUpperCase() + "') as ID";
							PreparedStatement psCurrVal=conn.prepareStatement(sSQL);
							ResultSet rsCurrVal= psCurrVal.executeQuery();
	
							if (rsCurrVal.next()){               	
								iRet = ((Number)rsCurrVal.getObject("ID")).intValue();
							}
						}
						else{
							iRet=Integer.parseInt(aUpload[i].getId());
						}
						arlFeatureIDs[i] = iRet;
						
						if (i>=(aUpload.length-1)){
							iRet = returnAllFeatures(sTable, acLayer, arlFeatureIDs, oos,conn,sesion,null,srid_destino,bImportaciones);
						}
					}
					else if (!aUpload[i].isDeleted() && bLoadData)
					{
						iRet=returnFeatures(iMunicipio,acLayer.getSelectQuery()
								+ " and \""+sTable+"\".ID="+aUpload[i].getAttValues()[acLayer.findID().getPosition()-1],acLayer,null,null,oos,conn,sesion,null,null,true,srid_destino);
						iRet=new Integer(aUpload[i].getAttValues()[acLayer.findID().getPosition()-1].toString()).intValue();
					}
					if (iRet!=-1)
						log(iMunicipio,sLayer,iUserID,iRet,aUpload[i].isNew()?Const.HISTORY_ACTION_INSERT:aUpload[i].isDeleted()?Const.HISTORY_ACTION_DELETE:Const.HISTORY_ACTION_UPDATE,conn);

					if (lstValidateFeatures != null){
						for (Iterator iterValidateFeatures = lstValidateFeatures.iterator();iterValidateFeatures.hasNext();){
							AbstractValidator validateFeatures = (AbstractValidator)iterValidateFeatures.next();
							validateFeatures.afterUpdate(aUpload[i],acLayer);
						}
					}
				}

			} else
				throw new ObjectNotFoundException("layer: "+sLayer+" ("+sLocale+")");

		}catch(Exception e){
			try{conn.rollback();}catch(Exception ex){};

			try{
				oos.writeObject(new ACException(e));
                //oos.flush();
				//Modificado para reestablecer el BufferedOutputStream
				oos.reset();
	
			}catch(Exception ex){};
			throw e;
		}finally{
			try{rs.close();}catch(Exception e){};
			try{ps.close();}catch(Exception e){};
			try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
		}
		return iRet;
	}



	public void updateStyle(String sLayer, String sXML, ObjectOutputStream oos, ISesion sesion) throws Exception{
		String sSQL="select ls.id_style , l.acl "
			+ "from layers_styles ls, layers l  "
			+ "where l.name=? and ls.id_layer= l.id_layer";
		Connection conn=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		try{
			conn=openConnection();
			ps=conn.prepareStatement(sSQL);
			ps.setString(1,sLayer);
			rs=ps.executeQuery();
			if (rs.next()){
				int iStyleID=rs.getInt("id_style");
				long lACL=rs.getLong("acl");
				rs.close();
				ps.close();
				if (!checkPermission(sesion,lACL,Const.PERM_WRITE_SLD,conn))
					throw new PermissionException(Const.PERM_WRITE_SLD);
				ps=conn.prepareStatement("update styles set xml=? where id_style=?");
				ps.setString(1,sXML);
				ps.setInt(2,iStyleID);
				ps.executeUpdate();
			}  else
				throw new ObjectNotFoundException("layer: "+sLayer);
		}catch(SQLException e){
			try{
				oos.writeObject(new ACException(e));
				//Modificado para reestablecer el BufferedOutputStream
				oos.reset();

			}catch(Exception ex){};
			throw e;
		}finally{
			try{rs.close();}catch(Exception e){};
			try{ps.close();}catch(Exception e){};
			try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
		}
	}

	public void returnModifiedFeatureIDs(int iMunicipio,String sLayer, long lDate, String sLocale, ObjectOutputStream oos)throws IOException,SQLException,Exception{
		String sSQL="select feature from history " +
		"where municipio=? and " +
		"layer=? and " +
		"ts>=?";
		Connection conn=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		try{
			conn=openConnection();
			ps=conn.prepareStatement(sSQL);
			ps.setInt(1,iMunicipio);
			ps.setString(2,sLayer);
			ps.setTimestamp(3,new Timestamp(lDate));
			rs=ps.executeQuery();
			ArrayList alFeatures=new ArrayList();
			for (;rs.next();)
				alFeatures.add(new Integer(rs.getInt("feature")));
			int[] aiFeatures=new int[alFeatures.size()];
			for (int i=0;i<aiFeatures.length;i++)
				aiFeatures[i]=((Integer)alFeatures.get(i)).intValue();
			oos.writeObject(aiFeatures);
			oos.reset();
		}catch(Exception e){
			oos.writeObject(new ACException(e));
			//Modificado para reestablecer el BufferedOutputStream
			oos.reset();

			throw e;
		}finally{
			try{rs.close();}catch(Exception e){};
			try{ps.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
	}

	public void returnLayerFamilyIDsByMap(int iMap, ObjectOutputStream oos)throws IOException,SQLException,Exception{
		String sSQL="select id_layerfamily from maps_layerfamilies_relations where id_map=? order by position";
		Connection conn=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		try{
			conn=openConnection();
			ps=conn.prepareStatement(sSQL);
			ps.setInt(1,iMap);
			rs=ps.executeQuery();
			ArrayList alIDs=new ArrayList();
			for (;rs.next();)
				alIDs.add(new Integer(rs.getInt("id_layerfamily")));
			int[] aiIDs=new int[alIDs.size()];
			for (int i=0;i<aiIDs.length;i++)
				aiIDs[i]=((Integer)alIDs.get(i)).intValue();
			oos.writeObject(aiIDs);
			oos.reset();
		}catch(Exception e){
			oos.writeObject(new ACException(e));
			//Modificado para reestablecer el BufferedOutputStream
			oos.reset();

			throw e;
		}finally{
			try{rs.close();}catch(Exception e){};
			try{ps.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
	}

	public void returnLayerFamilyIDs(ObjectOutputStream oos)throws IOException,SQLException,Exception{
		String sSQL="select id_layerfamily from layerfamilies";
		Connection conn=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		try{
			conn=openConnection();
			ps=conn.prepareStatement(sSQL);
			rs=ps.executeQuery();
			ArrayList alIDs=new ArrayList();
			for (;rs.next();)
				alIDs.add(new Integer(rs.getInt("id_layerfamily")));
			int[] aiIDs=new int[alIDs.size()];
			for (int i=0;i<aiIDs.length;i++)
				aiIDs[i]=((Integer)alIDs.get(i)).intValue();
			oos.writeObject(aiIDs);
			oos.reset();
		}catch(Exception e){
			oos.writeObject(new ACException(e));
			//Modificado para reestablecer el BufferedOutputStream
			oos.reset();

			throw e;
		}finally{
			try{rs.close();}catch(Exception e){};
			try{ps.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
	}

	/** Comentado Por Incidencia [365] */    
	public void returnLayerFamilies(ObjectOutputStream oos, String sLocale)throws IOException,SQLException,Exception{
		/** Comentado por Incidencia [365] */

		String sSQL="select id_layerfamily,traduccion,locale from layerfamilies, dictionary where id_name=id_vocablo "+
		" order by id_layerfamily";

		Connection conn=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		try{
			conn=openConnection();
			ps=conn.prepareStatement(sSQL);
			rs=ps.executeQuery();
			/** Incidencia [365] */
			int iCurrentLayerFamilyID= -1;
			int iLastLayerFamilyID= -1;
			int iCurrentLayerFamilyLocale= -1;
			int iLastLayerFamilyLocale= -1;
			Hashtable ht_acLayerFamily= new Hashtable();
			/**/
			for (;rs.next();){
				ACLayerFamily acFamily=new ACLayerFamily();
				acFamily.setId(rs.getInt("id_layerfamily"));
				acFamily.setName(rs.getString("traduccion"));

				/** Incidencia [365] */
				iCurrentLayerFamilyID= rs.getInt("id_layerfamily");
				/** Comprobamos el locale encontrado */
				iCurrentLayerFamilyLocale= getOpcionLocale(rs.getString("locale"), sLocale);
				if ((iCurrentLayerFamilyID != iLastLayerFamilyID) ||
						((iCurrentLayerFamilyID == iLastLayerFamilyID) && (iCurrentLayerFamilyLocale < iLastLayerFamilyLocale))){
					ht_acLayerFamily.put(new Integer(iCurrentLayerFamilyID), acFamily);
					iLastLayerFamilyLocale= iCurrentLayerFamilyLocale;
					iLastLayerFamilyID= iCurrentLayerFamilyID;
				}
				/**/
			}

			/** Comentado por Incidencia [365] */

			/** Inidencia [365] */
			ACLayerFamily[] aFamilies=new ACLayerFamily[ht_acLayerFamily.size()];
			Enumeration e= ht_acLayerFamily.elements();
			for (int i=0; e.hasMoreElements(); i++){
				aFamilies[i]= (ACLayerFamily)e.nextElement();
			}
			/**/

			oos.writeObject(aFamilies);
			oos.reset();
		}catch(Exception e){
			oos.writeObject(new ACException(e));
			//Modificado para reestablecer el BufferedOutputStream
			oos.reset();

			throw e;
		}finally{
			try{rs.close();}catch(Exception e){};
			try{ps.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
	}

	public void returnGeoRef(ObjectOutputStream oos, String tipoVia, String via, String numPoli, String sLocale,ISesion sesion) throws Exception {
		returnGeoRef(oos,tipoVia,via,numPoli,sLocale,sesion,true);
	}

	public void returnGeoRef(ObjectOutputStream oos, String tipoVia, String via, String numPoli, String sLocale,ISesion sesion,boolean bValidateData) throws Exception {
		String sSQL="select numeros_policia.ID, numeros_policia.\"GEOMETRY\"" +
		" from vias , numeros_policia where vias.id_municipio=? and numeros_policia.id_municipio=? and " +
		" (UPPER(vias.tipoviaine) like ? or UPPER(vias.tipovianormalizadocatastro) like ?) and " +
		" (UPPER(vias.nombreviaine) like ? or UPPER(vias.nombrecatastro) like ?) and " +
		" UPPER(numeros_policia.rotulo) like ? and numeros_policia.id_via=vias.id;";
		Connection conn=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		Geometry geo=null;
		try{
			conn=openConnection();
			ps=conn.prepareStatement(sSQL);
			ps.setString(1,sesion.getIdMunicipio());
			ps.setString(2,sesion.getIdMunicipio());
			ps.setString(3,(tipoVia!=null&&tipoVia.length()>0?tipoVia.toUpperCase():"%"));
			ps.setString(4,(tipoVia!=null&&tipoVia.length()>0?tipoVia.toUpperCase():"%"));
			if (via!=null) via=replace(via);
			ps.setString(5,(via!=null&&via.length()>0?via.toUpperCase():"%"));
			ps.setString(6,(via!=null&&via.length()>0?via.toUpperCase():"%"));
			ps.setString(7,(numPoli!=null&&numPoli.length()>0?numPoli.toUpperCase():"%"));
			rs=ps.executeQuery();
			if (rs.next())
			{

				FilterNode fn=  FilterLeaf.equal("numeros_policia.ID",rs.getString("ID"));
				while(rs.next())
				{
					FilterNode fnAux=  FilterLeaf.equal("numeros_policia.ID",rs.getString("ID"));
					FilterNode fnDoble= FilterOpBinary.or(fn,fnAux);
					fn=fnDoble;
				};
				returnLayer(Integer.parseInt(sesion.getIdMunicipio()), "numeros_policia",sLocale,null,fn,oos,sesion,bValidateData,null);

			}
			else
				throw new ObjectNotFoundException("Georeferenciación no encontrada");

		}catch(Exception e){
			if (!(e instanceof ObjectNotFoundException))
			{
			}
			oos.writeObject(new ACException(e));
			//Modificado para reestablecer el BufferedOutputStream
			oos.reset();

			throw e;
		}finally{
			try{rs.close();}catch(Exception e){};
			try{ps.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}

	}

	private String replace(String valor)
	{
		String valorFinal=valor.replace(' ','%');
		valorFinal=valorFinal.replace('(','%');
		valorFinal=valorFinal.replace(')','%');
		valorFinal=valorFinal.replace(',','%');
		String[] remplazables = {"A","DE","LOS","EL","LA","EN"};
		for (int i=0; i<remplazables.length;i++)
		{
			valorFinal=replace(valorFinal,remplazables[i],'%');
		}
		return valorFinal;
	}
	private String replace(String valor, String remplazable, char espacio)
	{
		String origen=valor.toUpperCase();
		remplazable=remplazable.toUpperCase();
		String remplazado=valor;
		if (origen.indexOf(espacio+remplazable+espacio)>=0)
		{
			remplazado=origen.substring(0,origen.indexOf(espacio+remplazable+espacio))+"%"+
			origen.substring(origen.indexOf(espacio+remplazable+espacio)+(espacio+remplazable+espacio).length());

			origen=remplazado;
		}
		if (origen.indexOf(remplazable+espacio)==0)
		{
			remplazado="%"+origen.substring((remplazable+espacio).length());
			origen=remplazado;
		}
		if (origen.lastIndexOf(espacio+remplazable)>=0)
		{
			if (origen.lastIndexOf(espacio+remplazable)==origen.length()-(espacio+remplazable).length())
			{
				remplazado=origen.substring(0,origen.length()-(espacio+remplazable).length())+"%";
			}
		}
		return remplazado;
	}
	public void returnLayerIDsByFamily(int iLayerFamily,ObjectOutputStream oos,ISesion sesion) throws IOException,SQLException,Exception{
		String sSQL="select l.name,l.acl from layerfamilies_layers_relations llr, layers l " +
		"where " +
		"llr.id_layer=l.id_layer and " +
		"llr.id_layerfamily=? and (l.id_entidad=0 or l.id_entidad=?) " +
		"order by llr.position";
		Connection conn=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		try{
			conn=openConnection();
			ps=conn.prepareStatement(sSQL);
			ps.setInt(1,iLayerFamily);
			ps.setString(2,sesion.getIdEntidad());
			rs=ps.executeQuery();
			ArrayList alIDs=new ArrayList();
			for (;rs.next();)
				if (checkPermission(sesion,rs.getLong("acl"),Const.PERM_LAYER_READ,conn,String.valueOf(iLayerFamily))){
					String sName=rs.getString("name");
					if (sName!=null)
						alIDs.add(sName);
				}
			String[] asIDs=new String[alIDs.size()];
			for (int i=0;i<asIDs.length;i++)
				asIDs[i]=((String)alIDs.get(i));
			oos.writeObject(asIDs);
			oos.reset();
		}catch(Exception e){
			oos.writeObject(new ACException(e));
			//Modificado para reestablecer el BufferedOutputStream
			oos.reset();

			throw e;
		}finally{
			try{rs.close();}catch(Exception e){};
			try{ps.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
	}


	public void returnLayerIDsByMap(int iMap,int iPosition,ObjectOutputStream oos,ISesion sesion)throws IOException,SQLException,Exception{
		String sSQL="select l.name,l.acl from maps m, maps_layerfamilies_relations mlr, layerfamilies_layers_relations llr, layers l " +
		"where " +
		"m.id_map=mlr.id_map and " +
		"mlr.id_layerfamily=llr.id_layerfamily and " +
		"llr.id_layer=l.id_layer and " +
		"m.id_map=? and " +
		"mlr.position=? " +
		"order by llr.position";
		Connection conn=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		try{
			conn=openConnection();
			ps=conn.prepareStatement(sSQL);
			ps.setInt(1,iMap);
			ps.setInt(2,iPosition);
			rs=ps.executeQuery();
			ArrayList alIDs=new ArrayList();
			for (;rs.next();)
				if (checkPermission(sesion,rs.getLong("acl"),Const.PERM_LAYER_READ)){
					String sName=rs.getString("name");
					if (sName!=null)
						alIDs.add(sName);
				}
			String[] asIDs=new String[alIDs.size()];
			for (int i=0;i<asIDs.length;i++)
				asIDs[i]=((String)alIDs.get(i));
			oos.writeObject(asIDs);
			oos.reset();
		}catch(Exception e){
			oos.writeObject(new ACException(e));
			//Modificado para reestablecer el BufferedOutputStream
			oos.reset();

			throw e;
		}finally{
			try{rs.close();}catch(Exception e){};
			try{ps.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
	}


	public void insertMap (ISesion sesion, String sIdEntidad, ACMap acMap,String sLocale,ObjectOutputStream oos,int id_map,Connection connection,GeopistaAcl acl) throws Exception{

		boolean bCerrarConexion=true;

		String sSQLDict="insert into dictionary (id_vocablo,locale,traduccion) values (?,?,?)";
		String sSQLMaps="insert into maps (id_map,id_name,xml,image,id_entidad,fecha_ins,fecha_mod) values (?,?,?,?,?,?,?)";

		Connection conn=null;
		PreparedStatement ps=null;
		Integer ID_entidad=Integer.parseInt(sIdEntidad);
		try{
			if (connection!=null)
			{
				conn=connection;
				bCerrarConexion=false;
			}
			else
			{
				conn=openConnection();
				bCerrarConexion=true;
			}

			conn.setAutoCommit(false);

			if (acl!=null){
				if (!checkPerm(sesion,acl,Const.PERM_MAP_CREATE)){
					throw new PermissionException("PermissionException: " + Const.PERM_MAP_CREATE);
				}
			}
			else{
				if (!checkPermission(sesion,Const.ACL_MAP,Const.PERM_MAP_CREATE))
					throw new PermissionException("PermissionException: " + Const.PERM_MAP_CREATE);
			}
			long idDictionary = CPoolDatabase.getNextValue("dictionary","id_vocablo","SEQ_DICTIONARY",conn);
			String sIdDictionary = String.valueOf(idDictionary);
			ps=conn.prepareStatement(sSQLDict);
			ps.setLong(1,new Long(idDictionary));
			ps.setString(2,sLocale);
			ps.setString(3,acMap.getName());
			ps.executeUpdate();
			ps.close();

			//Solucion provisional para el almacenamiento de mapas en idiomas!= es_ES
			if (!sLocale.equals("es_ES"))
			{
				ps=conn.prepareStatement(sSQLDict);                
				ps.setLong(1,new Long(idDictionary));
				ps.setString(2,"es_ES");
				ps.setString(3,acMap.getName());
				ps.executeUpdate();
				ps.close();
			}
			//

			ps=conn.prepareStatement(sSQLMaps);
			ps.setInt(1,id_map);
			ps.setLong(2,idDictionary);
			ps.setString(3,acMap.getXml());
			ps.setObject(4,acMap.getImage());
			ps.setInt(5,ID_entidad);
			Timestamp t =new java.sql.Timestamp(new java.util.Date().getTime());
			ps.setTimestamp(6,t);
			ps.setTimestamp(7,t);
			ps.executeUpdate();
			//ps.executeUpdate();
			String sFamiliesSQL="insert into maps_layerfamilies_relations (id_map,id_layerfamily,position,id_entidad) values (?,?,?,?)";
			Integer iPosition=null;
			for (Enumeration e=acMap.getLayerFamilies().keys(); e.hasMoreElements();){
				iPosition=(Integer)e.nextElement();
				ACLayerFamily lf=(ACLayerFamily)acMap.getLayerFamilies().get(iPosition);
				ps=null;
				ps=conn.prepareStatement(sFamiliesSQL);
				ps.setInt(1,id_map);
				ps.setInt(2,lf.getId());
				ps.setInt(3,iPosition.intValue());
				ps.setInt(4,ID_entidad);
				ps.executeUpdate();
				ps.close();
			}
			String sIdStyle;
			String sStylesSQL="insert into layers_styles (id_map,id_layerfamily,id_layer,id_style,stylename,position,id_entidad,isactive,isvisible,iseditable) values (?,?,(select id_layer from layers where name=?),?,?,?,?,?,?,?)";
			for (Iterator it=acMap.getLayerStyles().iterator(); it.hasNext();){
				LayerStyleData lsd=(LayerStyleData)it.next();
				sIdStyle=lsd.getIdStyle();
				String sPos;
				if (sIdStyle!=null){
					ps=null;
					ACLayer layer = obtieneLayerAsociado(conn, acMap, lsd);
					// NOTA: Se comenta hasta que se solucione, para que se puedan guardar mapas y no tener problemas con las versiones	
		/*			if (layer!= null && layer.isVersionable())
						sStylesSQL="insert into layers_styles (id_map,id_layerfamily,id_layer,id_style,stylename,position,id_entidad,isactive,isvisible,iseditable,revision) values (?,?,(select id_layer from layers where name=?),?,?,?,?,?,?,?,?)";
					else
						sStylesSQL="insert into layers_styles (id_map,id_layerfamily,id_layer,id_style,stylename,position,id_entidad,isactive,isvisible,iseditable) values (?,?,(select id_layer from layers where name=?),?,?,?,?,?,?,?)";
			*/		
					ps=conn.prepareStatement(sStylesSQL);
					ps.setInt(1,id_map);
					ps.setInt(2,Integer.parseInt(lsd.getIdLayerFamily()));
					ps.setString(3,lsd.getIdLayer());
					sIdStyle=lsd.getIdStyle();
					if (sIdStyle!=null)
						ps.setInt(4,Integer.parseInt(sIdStyle));
					else
						ps.setNull(4,java.sql.Types.INTEGER);
					ps.setString(5,lsd.getStyleName());

					sPos=lsd.getLayerPosition();
					if (sPos!=null)
						try {ps.setInt(6, Integer.parseInt(sPos));} 
					catch (Exception e5) {e5.printStackTrace();}
					else ps.setNull(6,java.sql.Types.INTEGER);
					ps.setInt(7,(Integer)ID_entidad);
					ps.setBoolean(8,lsd.isActive());
					ps.setBoolean(9,lsd.isVisible());
					ps.setBoolean(10,lsd.isEditable());
					// NOTA: Se comenta hasta que se solucione, para que se puedan guardar mapas y no tener problemas con las versiones	
		/*			if (layer != null && layer.isVersionable()){
						ps.setLong(11, layer.getRevisionActual());
					}*/
					ps.executeUpdate();
					ps.close();
				}
			}

			acMap.setId(Integer.toString(id_map));
			updateMapServerLayer(acMap, sIdEntidad, conn);

			oos.writeObject(new Integer(id_map));
			oos.reset();
			conn.commit();
		}catch(SQLException e){
			try{conn.rollback();}catch(Exception ex){};

			
			throw e;
		}finally{            
			try{ps.close();}catch(Exception e){};
			try{if (bCerrarConexion){conn.close();CPoolDatabase.releaseConexion();}}catch(Exception e1){};

		}
	}

	public void insertMap(ISesion sesion, String sIdEntidad, ACMap acMap,String sLocale,ObjectOutputStream oos) throws Exception{

		Connection conn=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		try{

			conn=openConnection();

			//Comprobamos si tiene permiso para poder realizar la operacion
			GeopistaAcl acl=getPermission(sesion,Const.ACL_MAP,conn);
			if (!checkPerm(sesion,acl,Const.PERM_MAP_CREATE))
				throw new PermissionException("PermissionException: " + Const.PERM_MAP_CREATE);

			long iId=CPoolDatabase.getNextValue("MAPS","id_map","SEQ_MAPS",conn);

			insertMap(sesion,sIdEntidad,acMap,sLocale,oos,new Long(iId).intValue(),conn,acl);

		}catch(SQLException e){
			try{conn.rollback();}catch(Exception ex){};

			
			throw e;
		}finally{
			try{rs.close();}catch(Exception e){};
			try{ps.close();}catch(Exception e){};
			try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
		}
	}

	public void updateMap(ISesion sesion, String sIdEntidad, ACMap acMap,String sLocale,ObjectOutputStream oos) throws Exception{

		try
		{

			String sSQLfecha="select fecha_ins from maps where id_entidad=? and id_map=?";
			String sSQL="update maps set xml=?,image=?, fecha_mod=? where id_entidad=? and id_map=?";
			Connection conn=null;

			PreparedStatement ps=null;
			PreparedStatement ps1=null;
			ResultSet rs1=null;
			try{
				conn=openConnection();
				conn.setAutoCommit(false);
				//Comprobamos si tiene permiso para poder realizar la operacion
				GeopistaAcl acl=getPermission(sesion,Const.ACL_MAP,conn);
				if (!checkPerm(sesion,acl,Const.PERM_MAP_EDIT))
					throw new PermissionException("PermissionException: " + Const.PERM_MAP_EDIT);

				ACMap tempMap = loadMap(Integer.parseInt(acMap.getId()),sLocale,sesion,false,conn);

				if(tempMap.getIdEntidad()==0)
					
				{
					if (!sIdEntidad.equals("0")){
						insertMap(sesion,sIdEntidad,acMap,sLocale,oos,Integer.parseInt(tempMap.getId()),conn,acl);					
						return;
					}
				}
				else{
					
					//Un mapa que hemos definido para una entidad lo hacemos global.
					if (sIdEntidad.equals("0")){
						String sSQLSelect="select id_map from maps where id_entidad=? and id_map=?";
						conn.setAutoCommit(false);
						ps=conn.prepareStatement(sSQLSelect);
						ps.setString(1,sIdEntidad);
						ps.setInt(2,Integer.parseInt(acMap.getId()));
						rs1 = ps.executeQuery();
						if(!rs1.next()){
							insertMap(sesion,sIdEntidad,acMap,sLocale,oos,Integer.parseInt(tempMap.getId()),conn,acl);							
							rs1.close();
							return;
						}
						rs1.close();
						ps.close();

						String[] asSql=new String[]{"delete from dictionary where id_vocablo=(select id_name from maps where id_map=? and id_entidad = ?)"
								,"delete from maps_layerfamilies_relations where id_map=? and id_entidad = ?"
								,"delete from maps_wms_relations where id_map=? and id_entidad = ?"
								,"delete from layers_styles where id_map=? and id_entidad = ?"
								,"delete from maps where id_map=? and id_entidad = ?"};
						for (int i=0;i<asSql.length;i++){
							ps=conn.prepareStatement(asSql[i]);
							ps.setInt(1,Integer.parseInt(acMap.getId()));
							ps.setInt(2,tempMap.getIdEntidad());
							ps.executeUpdate();
							ps.close();
						}
						
					}
					
				}
                
				ps=conn.prepareStatement(sSQL);
				ps.setString(1,acMap.getXml());
				ps.setObject(2,acMap.getImage());
				Timestamp t =new java.sql.Timestamp(new java.util.Date().getTime());
				ps.setTimestamp(3, t);
				ps.setString(4,sIdEntidad);
				ps.setInt(5,Integer.parseInt(acMap.getId()));
				ps.executeUpdate();
				String[] asSql=new String[]{"delete from maps_layerfamilies_relations where id_map=? and id_entidad = ?"
						,"delete from layers_styles where id_map=? and id_entidad = ?"
								,"delete from maps_wms_relations where id_map=? and id_entidad=?"};
				int iId=Integer.parseInt(acMap.getId());
				for (int i=0;i<asSql.length;i++){
					ps=conn.prepareStatement(asSql[i]);
					ps.setInt(1,iId);
					ps.setInt(2,Integer.parseInt(sIdEntidad));
					ps.executeUpdate();
					ps.close();
				}
				String updateDictionary = "Update dictionary set traduccion = ? where dictionary.id_vocablo = (select maps.id_name from maps where maps.id_map = ? and maps.id_entidad = ?) and dictionary.locale = ?";
				ps=conn.prepareStatement(updateDictionary);
				ps.setString(1,acMap.getName());
				ps.setInt(2,iId);
				ps.setInt(3,Integer.parseInt(sIdEntidad));
				ps.setString(4,sLocale);
				int rows= ps.executeUpdate(); // Incidencia [365] - annadimos rows afectadas en la actualizacion
				ps.close();

				/** Si no se actualiza para locale sLocale, es que no existe la traduccion en el dictionary para ese locale -> hacemos la insercion */
				if (rows == 0){
					/** No existen traducciones para sLocale - Hacemos la insercion de la traduccion para sLocale. */
					String insertDictionary="insert into dictionary (traduccion, id_vocablo, locale) values (?,(select maps.id_name from maps where maps.id_map=? and maps.id_entidad=?),?)";
					ps=conn.prepareStatement(insertDictionary);
					ps.setString(1,acMap.getName());
					ps.setInt(2,iId);
					
					ps.setInt(3,Integer.parseInt(sIdEntidad));
					ps.setString(4,sLocale);
					ps.executeUpdate();
					ps.close();
				}

				boolean [] activos = null;
				String sFamiliesSQL="insert into maps_layerfamilies_relations (id_map,id_layerfamily,position,id_entidad) values (?,?,?,?)";
				Integer iPosition=null;
				for (Enumeration e=acMap.getLayerFamilies().keys(); e.hasMoreElements();){
					iPosition=(Integer)e.nextElement();
					ACLayerFamily lf=(ACLayerFamily)acMap.getLayerFamilies().get(iPosition);
					ps=conn.prepareStatement(sFamiliesSQL);
					ps.setInt(1,iId);
					ps.setInt(2,lf.getId());
					ps.setInt(3,iPosition.intValue());
					ps.setInt(4,Integer.parseInt(sIdEntidad));
					ps.executeUpdate();
					ps.close();

				}


				String sStylesSQL="";
				for (Iterator it=acMap.getLayerStyles().iterator(); it.hasNext();){
					LayerStyleData lsd=(LayerStyleData)it.next();
					if (lsd.getIdStyle()!=null){
						ACLayer layer = obtieneLayerAsociado(conn, acMap, lsd);
					// NOTA: Se comenta hasta que se solucione, para que se puedan guardar mapas y no tener problemas con las versiones		
					//	if (layer!= null && layer.isVersionable())
					//		sStylesSQL="insert into layers_styles (id_map,id_layerfamily,id_layer,id_style,stylename,position,id_entidad,isactive,isvisible,iseditable,revision) values (?,?,(select id_layer from layers where name=?),?,?,?,?,?,?,?,?)";
					//	else
							sStylesSQL="insert into layers_styles (id_map,id_layerfamily,id_layer,id_style,stylename,position,id_entidad,isactive,isvisible,iseditable) values (?,?,(select id_layer from layers where name=?),?,?,?,?,?,?,?)";
						
						ps=conn.prepareStatement(sStylesSQL);
						ps.setInt(1,iId);
						ps.setInt(2,Integer.parseInt(lsd.getIdLayerFamily()));
						ps.setString(3,lsd.getIdLayer());
						String sIdStyle=lsd.getIdStyle();
						if (sIdStyle!=null)
							ps.setInt(4,Integer.parseInt(sIdStyle));
						else
							ps.setNull(4,java.sql.Types.INTEGER);
						ps.setString(5,lsd.getStyleName());
						String sPos=lsd.getLayerPosition();
						if (sPos!=null)
							ps.setInt(6,Integer.parseInt(sPos));
						else
							ps.setNull(6,java.sql.Types.INTEGER);
						ps.setInt(7,Integer.parseInt(sIdEntidad));

						ps.setBoolean(8,lsd.isActive());
						ps.setBoolean(9,lsd.isVisible());
						ps.setBoolean(10,lsd.isEditable());
					// NOTA: Se comenta hasta que se solucione, para que se puedan guardar mapas y no tener problemas con las versiones	
					//	if (layer != null && layer.isVersionable()){
					//		ps.setLong(11, layer.getRevisionActual());
					//	}
						ps.executeUpdate();
						ps.close();


					}
				}

				updateMapServerLayer(acMap, sIdEntidad, conn);

				conn.commit();
			}catch(SQLException e){
				try{conn.rollback();}catch(Exception ex){};
				
				throw e;
			}finally{
				try{ps.close();}catch(Exception e){};
				try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
			}
		}catch(ObjectNotFoundException e)
		{
			insertMap(sesion,sIdEntidad,acMap,sLocale,oos);
			return;
		}
	}

	private void updateMapServerLayer(ACMap acMap, String sIdEntidad, Connection conn) throws Exception{
		PreparedStatement ps=null;
		try{
			if(acMap.getMapServerLayers() != null && !acMap.getMapServerLayers().isEmpty()){

				String sWMSLayersSQL="insert into maps_wms_relations (id_map,id_mapserver_layer,position,id_entidad) values (?,?,?,?)";
				for (Iterator it=acMap.getMapServerLayers().iterator(); it.hasNext();){

					ACWMSLayer layer = (ACWMSLayer)it.next();
					if(layer.getId() == null){//si la capa no existe la creamos
						layer.setId(new Integer(insertMapServerLayer(layer, conn)));
					}
					else{//en caso contrario la actualizamos
						String updateWMSLayer="update map_server_layers set params=?, srs=?, format=?, activa=?, visible=?, styles=?, name=? where id=?";
						ps=conn.prepareStatement(updateWMSLayer);
						ps.setString(1,layer.getCommaSeparatedLayerNamesList());
						ps.setString(2,layer.getSrs());
						ps.setString(3,layer.getFormat());
						ps.setInt(4, layer.isActiva()? 1 : 0);
						ps.setInt(5, layer.isVisible()? 1 : 0);
						ps.setString(6, layer.getCommaSeparatedStylesList());
						ps.setString(7, layer.getName());
						ps.setInt(8,layer.getId());
						ps.executeUpdate();
						ps.close();
					}

					int iId=Integer.parseInt(acMap.getId());

					ps=conn.prepareStatement(sWMSLayersSQL);
					ps.setInt(1,iId);
					ps.setInt(2,layer.getId().intValue());
					ps.setInt(3,layer.getPosition());
					ps.setInt(4,Integer.parseInt(sIdEntidad));
					ps.executeUpdate();
					ps.close();
				}
			}
		}catch(SQLException e){
			throw e;
		}finally{
			try{ps.close();}catch(Exception e){};
		}
	}

	private int insertMapServerLayer(ACWMSLayer layer, Connection conn) throws Exception{
		PreparedStatement ps=null;
		Statement st = null;
		ResultSet rs = null;
		int id = 0;

		try{

			String seq = "select nextval('seq_map_server_layers') as id";
			st = conn.createStatement();
			rs = st.executeQuery(seq);

			if(!rs.next())
				throw new Exception("No se pudo obtener el ID de la secuencia de Map_Server_Layers");

			id = rs.getInt("id");

			st.close();
			rs.close();

			String sWMSLayerSQL="insert into map_server_layers (id,service,url,"+
			"params,srs,format,version,activa,visible,styles,name) values (?,?,?,?,?,?,?,?,?,?,?)";

			ps=conn.prepareStatement(sWMSLayerSQL);
			ps.setInt(1,id);
			ps.setString(2,layer.getService());
			ps.setString(3,layer.getUrl());
			ps.setString(4,layer.getCommaSeparatedLayerNamesList());
			ps.setString(5,layer.getSrs());
			ps.setString(6,layer.getFormat());
			ps.setString(7,layer.getVersion());
			ps.setInt(8, layer.isActiva()? 1 : 0);
			ps.setInt(9, layer.isVisible()? 1 : 0);
			ps.setString(10, layer.getCommaSeparatedStylesList());
			ps.setString(11, layer.getName());

			ps.executeUpdate();
			ps.close();

		}
		catch(SQLException e){
			throw e;
		}
		finally{
			try{rs.close();}catch(Exception e){};
			try{st.close();}catch(Exception e){};
			try{ps.close();}catch(Exception e){};
		}

		return id;
	}

	public void deleteMap(ISesion sesion, int iId,ObjectOutputStream oos, String sLocale) throws Exception{

		Connection conn=null;
		PreparedStatement ps=null;
		try{
			conn=openConnection();

			//Comprobamos si tiene permiso para poder realizar la operacion
			GeopistaAcl acl=getPermission(sesion,Const.ACL_MAP,conn);
			if (!checkPerm(sesion,acl,Const.PERM_MAP_EDIT))
				throw new PermissionException("PermissionException: " + Const.PERM_MAP_EDIT);

			ACMap tempMap = loadMap(iId,sLocale,sesion,false,conn);
			
			GeopistaAcl acl2=getPermission(sesion,Const.ACL_GENERAL,conn);
			if (acl2!=null){
				if (!checkPerm(sesion,acl2,Const.PERM_MAP_BORRAR_MAPAS_GLOBALES))
					if(tempMap.getIdEntidad()==0) throw new SystemMapException("SystemMap");
			}
			else{
				if(tempMap.getIdEntidad()==0) throw new SystemMapException("SystemMap");
			}

			if (isPublishedMap(iId,tempMap.getIdEntidad(),conn))
				throw new PublishedMapException();

			conn.setAutoCommit(false);
			String[] asSql=new String[]{"delete from dictionary where id_vocablo=(select id_name from maps where id_map=? and id_entidad = ?)"
					,"delete from maps_layerfamilies_relations where id_map=? and id_entidad = ?"
					,"delete from maps_wms_relations where id_map=? and id_entidad = ?"
					,"delete from layers_styles where id_map=? and id_entidad = ?"
					,"delete from maps where id_map=? and id_entidad = ?"};
			for (int i=0;i<asSql.length;i++){
				ps=conn.prepareStatement(asSql[i]);
				ps.setInt(1,iId);
				ps.setInt(2,tempMap.getIdEntidad());
				ps.executeUpdate();
				ps.close();
			}
			conn.commit();
		}catch(SQLException e){
			try{conn.rollback();}catch(Exception ex){};
			oos.writeObject(new ACException(e));
			//Modificado para reestablecer el BufferedOutputStream
			oos.reset();

			throw e;
		}finally{
			try{ps.close();}catch(Exception e){};
			try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
		}
	}

	/**
	 * Comprueba si un mapa esta publicado en la guia urbana antigua o en la nueva.
	 * @param id
	 * @return
	 * @throws SQLException 
	 */
	private boolean isPublishedMap(int id,int idEntidad,Connection connection) throws SQLException{

		String sec1 = "select mapid from localgisguiaurbana.map where mapidgeopista=? and mapidentidad=?";
		Connection conn=null;
		PreparedStatement preparedStmt=null;
		ResultSet rSet=null;
		boolean bCerrarConexion=true;
		try {
			if (connection!=null)
			{
				conn=connection;
				bCerrarConexion=false;
			}
			else
			{
				conn=openConnection();
				bCerrarConexion=true;
			}
			preparedStmt=conn.prepareStatement(sec1);
			preparedStmt.setInt(1,id);
			preparedStmt.setInt(2,idEntidad);
			rSet = preparedStmt.executeQuery();
			if (rSet.next())
				return true;
		} catch (Exception e) {
		} finally {
			try{rSet.close();}catch(Exception e){};
			try{preparedStmt.close();}catch(Exception e){};
			try{if (bCerrarConexion){conn.close();CPoolDatabase.releaseConexion();}}catch(Exception e1){};
		}      
		return false;
	}


	public void log(int iMunicipio, String sLayer, int iUserID, int iFeature, int iAction)throws SQLException{

		log(iMunicipio,sLayer,iUserID,iFeature,iAction,null);
	}

	public void log(int iMunicipio, String sLayer, int iUserID, int iFeature, int iAction,Connection connection)throws SQLException{
		String sSQL="insert into history (municipio,layer,feature,user_id,ts,action) " +
		"values (?,?,?,?,?,?)";
		Connection conn=null;
		PreparedStatement ps=null;
		boolean bCerrarConexion=true;
		try{
			if (connection!=null)
			{
				conn=connection;
				bCerrarConexion=false;
			}
			else
			{
				conn=openConnection();
				bCerrarConexion=true;
			}
			ps=conn.prepareStatement(sSQL);
			ps.setInt(1,iMunicipio);
			ps.setString(2,sLayer);
			ps.setInt(3,iFeature);
			ps.setInt(4,iUserID);
			ps.setTimestamp(5,new Timestamp(System.currentTimeMillis()));
			ps.setInt(6,iAction);
			ps.executeUpdate();
		}catch(SQLException e){
			throw e;
		}finally{
			try{ps.close();}catch(Exception e){};       
			try{if (bCerrarConexion){conn.close();CPoolDatabase.releaseConexion();}}catch(Exception e1){};
		}
	}    

	private boolean checkPermission(ISesion sSesion,long lACL,String sPerm) throws Exception{
		GeopistaAcl acl=SesionUtils_LCGIII.getPerfil(sSesion,lACL,null);
		if (acl==null) return false;
		return acl.checkPermission(new GeopistaPermission(sPerm));
	}
	private boolean checkPermission(ISesion sSesion,long lACL,String sPerm,Connection con) throws Exception{
		GeopistaAcl acl=SesionUtils_LCGIII.getPerfil(sSesion,lACL,con);
		if (acl==null) return false;
		return acl.checkPermission(new GeopistaPermission(sPerm));
	}

	private boolean checkPermission(ISesion sSesion,long lACL,String sPerm,Connection con,String layerName) throws Exception{
		GeopistaAcl acl=SesionUtils_LCGIII.getPerfil(sSesion,lACL,con);
		if (acl==null) return false;
		boolean permiso=acl.checkPermission(new GeopistaPermission(sPerm));
        if (!permiso){
			throw new PermissionException("Usuario  no tiene" +
			" permisos para ver la capa. Layer:"+layerName+" Acl:"+acl.getAclDescripcion());                	
		}
        return permiso;
	}
	
	private boolean checkPermission(ISesion sSesion,GeopistaAcl acl,String sPerm) throws Exception{
		return acl.checkPermission(new GeopistaPermission(sPerm));
	}

	/**
	 * Este metdo sustitute al checkPermission para obtener la informacion de la Base
	 * de datos solo una vez.
	 * @param sSesion
	 * @param lACL
	 * @param conn
	 * @return
	 * @throws Exception
	 */
	private GeopistaAcl getPermission(ISesion sSesion,long lACL,Connection conn) throws Exception{
		GeopistaAcl acl=SesionUtils_LCGIII.getPerfil(sSesion,lACL,conn);
		if (acl==null){
			throw new PermissionException("El usuario "+sSesion.getUserPrincipal().getName()+ " no tiene" +
			" permisos para ver todas las capas del mapa.");                	
		}
		return acl;
	}    

	private void testThumbnails(String sFile){
		Connection conn=null;
		PreparedStatement ps=null;
		try{
			ByteArrayOutputStream baos=new ByteArrayOutputStream();
			FileInputStream fis=new FileInputStream(sFile);
			for(;;){
				int b=fis.read();
				if (b!=-1)
					baos.write(b);
				else
					break;
			}
			fis.close();
			conn=openConnectionManual();
			String sSQL="update maps set image=?";
			ps=conn.prepareStatement(sSQL);
			ps.setObject(1,baos.toByteArray());
			ps.executeUpdate();
		}catch(Exception e){
		}finally{
			try{ps.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
	}

	/** Incidencia [365] */
	/** Retorna 1 - si la traduccion encontrada corresponde a la preferencia de idioma del usuario
	 *          2 - si la traduccion encontrada corresponde al locale es_ES (segunda mejor opcion)
	 *          3 - si es cualquier otro idioma (x_ES)
	 */
	public static int getOpcionLocale(String locale, String sLocale){
		if (locale.equals(sLocale)){
			/** mejor opcion, sLocale */
			return 1;
		}else if (locale.equals("es_ES")){
			/** segunda mejor opcion, es_ES */
			return 2;
		}else{
			/** peor opcion, x_ES */
			return 3;
		}
	}


	public static void main(String sArgs[])throws Exception{
//		PostGISConnection pgc=new PostGISConnection("localhost","5432","pista","jramirez",null, new SRID("config\\srid.properties"));
	    PostGISConnection pgc=new PostGISConnection("localhost","5432","pista","jramirez",null, new NewSRID());
		pgc.municipioGeometry(34083);

	}

	/**
	 * Inserta una nueva coverage layer en la tabla de coverage layers
	 * 
	 */
	public void insertCoverageLayer(int idMunicipio, int idName, String desc_path, String srs, String extension) 
	throws Exception {

		String sSQLInsertCoverageLayer = "insert into coverage_layers (id_municipio, id_name, desc_path, srs, extension) values (?,?,?,?,?)";
		String sSQLDeleteCoverageLayer = "delete from coverage_layers where id_municipio=?";
		Connection conn = null;
		conn=openConnection();
		PreparedStatement preparedStmt=null;
		ResultSet rSet=null;

		try {
			preparedStmt=conn.prepareStatement(sSQLDeleteCoverageLayer);
			preparedStmt.setInt(1, idMunicipio);
			preparedStmt.executeUpdate();
			preparedStmt = null;

			preparedStmt=conn.prepareStatement(sSQLInsertCoverageLayer);
			preparedStmt.setInt(1, idMunicipio);
			preparedStmt.setInt(2, idName);
			preparedStmt.setString(3, desc_path);
			preparedStmt.setString(4, srs);
			preparedStmt.setString(5, extension);
			preparedStmt.executeUpdate();
			conn.commit();
		} catch (Exception e) {
			throw e;
		} finally {
			try{rSet.close();}catch(Exception e){};
			try{preparedStmt.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
	}

	public int getNextDictionaryId() throws Exception {

		String sec = "select nextval('seq_dictionary') as next";
		Connection conn = null;
		conn=openConnection();
		PreparedStatement preparedStmt=null;
		ResultSet rSet=null;
		try {
			preparedStmt=conn.prepareStatement(sec);
			rSet = preparedStmt.executeQuery();
			rSet.next();
			int res = rSet.getInt("next");

			return res;
		} catch (Exception e) {
			throw e;
		} finally {
			try{rSet.close();}catch(Exception e){};
			try{preparedStmt.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
	}

	public void insertVocablo(int idVocablo, String locale, String traduccion) 
	throws Exception {

		String sSQLInsertVocablo = "insert into dictionary values (?,?,?)";
		Connection conn = null;
		conn=openConnection();
		PreparedStatement preparedStmt=null;
		ResultSet rSet=null;

		try {
			preparedStmt=conn.prepareStatement(sSQLInsertVocablo);
			preparedStmt.setInt(1, idVocablo);
			preparedStmt.setString(2, locale);
			preparedStmt.setString(3, traduccion);
			preparedStmt.executeUpdate();
			conn.commit();
		} catch (Exception e) {
			throw e;
		} finally {
			try{rSet.close();}catch(Exception e){};
			try{preparedStmt.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
	}
	/**
	 * Se coge de la base de datos el campo maps.xml. Si el nodo mapProjection es "Unspecified",
	 * se sustituye éste por el sistema de coordenadas correspondiente
	 * @param sArgs
	 * @throws Exception
	 */
	private String updateProjection(InputStream xml, int idSrid, ISesion sesion) throws Exception{
		SAXBuilder builder = new SAXBuilder(false);
		Document docNew = builder.build(xml);
		Element rootElement = docNew.getRootElement();
		Element elemento = (Element)rootElement.getChild("mapProjection");
		if (elemento.getName().equals("mapProjection") && (elemento.getText().equals("")||elemento.getText().equals("Unspecified"))){
		    
			//logger.info("Actualización de proyección SRID1:"+idSrid+" SRID2:"+srid.getSRIDEntidad(Integer.parseInt(sesion.getIdEntidad())));
		    //CoordinateSystem cS = CoordinateSystemRegistry.instance(new Blackboard()).get(idSrid);
		//DESCOMENTAR
			//    CoordinateSystem cS = CoordinateSystemRegistry.instance(new Blackboard()).get(srid.getSRIDEntidad(Integer.parseInt(sesion.getIdEntidad())));
		    CoordinateSystem cS = CoordinateSystemRegistry.instance(new Blackboard()).get(srid.getSRIDEntidad(Integer.parseInt(sesion.getIdMunicipio())));
		    elemento.setText(cS.getName());		    
//			if (idSrid == 23030)
//				elemento.setText("UTM 30N ED50");
//			else if (idSrid==23029)
//				elemento.setText("UTM 29N ED50");
//			else if (idSrid ==23031)
//				elemento.setText("UTM 31N ED50");
		} 
		XMLOutputter outp = new XMLOutputter();
		return outp.outputString(docNew);
	}

	// Esta es una clase auxiliar. No es thread safe. Para utilizar, instanciar un objeto
	// y realizar el lock
	private class FeatureLocker {

		// Numero de threads utilizados para insertar
		private int numberOfThreads = 4;

		// Insercion de un lock para la feature
		private String sqlInsertFeatureLock = "insert into locks_feature (municipio,layer,feature_id,user_id,ts) "
			+"values (?,?,?,?,?)";        
		//Obtener la tabla donde esta la geometria...
		private String sqlFindGeometryTable = "select t.name as table,l.id_layer from tables t,columns c,attributes a,layers l " +
		"where a.id_layer=l.id_layer and l.name=? and c.id_table=t.id_table and c.name='GEOMETRY' and a.id_column=c.id";

		//Comprobacion de si el feature esta cerrado
		private  String sqlCheckFeatureLock = "select user_id,ts from locks_feature " +
		"where municipio=? and layer=? and feature_id=?";

		int lockingUserId = -1;

		Connection conn;

		Thread[] insertLockThreads;

		FeatureLockResult featureLockResult;    	

		public FeatureLockResult lock(int iMunicipio, List layers, List featuresIds, int iUserId) {
			ArrayList featuresToLock = new ArrayList();
			int lockStatus = AdministradorCartografiaClient.LOCK_FEATURE_LOCKED;  

			featureLockResult = null;
			PreparedStatement psFindGeometryTable = null;
			PreparedStatement psInsertFeatureLock = null;
			PreparedStatement psCheckFeatureLock = null;

			try {
				try{
					conn = openConnection();
					psFindGeometryTable = conn.prepareStatement(sqlFindGeometryTable);    
					psInsertFeatureLock = conn.prepareStatement(sqlInsertFeatureLock);
					psCheckFeatureLock = conn.prepareStatement(sqlCheckFeatureLock);

					for(int i = 0; i < featuresIds.size(); i++){
						String layer = (String) layers.get(i);
						int featureId = ((Integer) featuresIds.get(i)).intValue();

						lockStatus = checkFeatureLockStatus(psFindGeometryTable,psCheckFeatureLock, 
								iMunicipio,layer,featureId,iUserId);

						if (lockStatus == 0){  
							featuresToLock.add(new Integer(i));
						}

						if (lockStatus != AdministradorCartografiaClient.LOCK_FEATURE_LOCKED
								&& lockStatus != AdministradorCartografiaClient.LOCK_FEATURE_OWN
								&& lockStatus != AdministradorCartografiaClient.LOCK_LAYER_OWN) {

							featureLockResult = new FeatureLockResult(lockStatus, layer, featureId);

							if (lockStatus == AdministradorCartografiaClient.LOCK_FEATURE_OTHER){
								addLockOwnerUserInfo(featureLockResult, lockingUserId);
							}

							return featureLockResult;
						}
					}
				} catch (ACException e) {             		
					featureLockResult = new FeatureLockResult(AdministradorCartografiaClient.LOCK_FEATURE_ERROR);
					return featureLockResult;
				} catch(SQLException se) {

					featureLockResult = new FeatureLockResult(AdministradorCartografiaClient.LOCK_FEATURE_ERROR);
					return featureLockResult;            		
				} finally{        	
					try{psFindGeometryTable.close();}catch(Exception e){};
					try{psInsertFeatureLock.close();}catch(Exception e){};
					try{psCheckFeatureLock.close();}catch(Exception e){};            	
				}

				try {
					int numberOfFeaturesPerThread = featuresToLock.size()/ numberOfThreads;

					conn.setAutoCommit(false);

					insertLockThreads = new InsertLockThread[numberOfThreads];        	

					for (int i = 0; i < insertLockThreads.length; i++){
						int first = 0 + i*(numberOfFeaturesPerThread);
						int last = i*(numberOfFeaturesPerThread) + (numberOfFeaturesPerThread);

						if (i == insertLockThreads.length - 1){
							last = featuresToLock.size();
						}

						insertLockThreads[i] = new InsertLockThread(conn, featuresToLock, featuresIds,
								layers, iMunicipio,	iUserId, first, last);

						insertLockThreads[i].start();
					}

					// Espero a que finalicen los hilos
					for (int i = 0; i < insertLockThreads.length; i++){
						insertLockThreads[i].join();
					}

					if (featureLockResult == null){
						conn.commit();
						return new FeatureLockResult(AdministradorCartografiaClient.LOCK_FEATURE_LOCKED);
					}
					else {
						// Retornamos el resultado de error devuelto por uno de los hilos
						try{conn.rollback();} catch (SQLException e){}
						return featureLockResult;
					}
				} catch (SQLException e){
					try{conn.rollback();} catch (SQLException se){}

					featureLockResult = new FeatureLockResult(AdministradorCartografiaClient.LOCK_FEATURE_ERROR);
					return featureLockResult;
				} catch (InterruptedException e) {
					try{conn.rollback();} catch (SQLException se){}

					featureLockResult = new FeatureLockResult(AdministradorCartografiaClient.LOCK_FEATURE_ERROR);
					return featureLockResult;
				}            	
			} finally {
				try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
			}           
		}

		private int checkFeatureLockStatus(PreparedStatement psFindGeometryTable,
				PreparedStatement psCheckFeatureLock, 
				int iMunicipio, String layer, int feature, int userId)
		throws SQLException, ACException{

			int lockStatus=0;
			PreparedStatement psGeom = null;
			ResultSet rs=null;
			try{
				// Comparar ids de Feature y bloqueos de layer con la geometria de iFeature...
				lockingUserId = -1;

				psCheckFeatureLock.setInt(1,iMunicipio);
				psCheckFeatureLock.setString(2,layer);
				psCheckFeatureLock.setInt(3,feature);

				rs = psCheckFeatureLock.executeQuery();
				if (rs.next()){
					lockingUserId=rs.getInt("user_id");
				}
				rs.close();

				if(lockingUserId != -1){
					return (lockingUserId==userId?AdministradorCartografiaClient.LOCK_FEATURE_OWN
							:AdministradorCartografiaClient.LOCK_FEATURE_OTHER);
				}

				psFindGeometryTable.setString(1,layer);
				rs = psFindGeometryTable.executeQuery();
				String geometryTable = null;

				if (rs.next()){
					geometryTable = rs.getString("table");
				}

				rs.close();

				String sqlGeom = "select ll.user_id from locks_layer ll,\""+geometryTable+"\" t " +
				"where t.id=? and ll.municipio=? and ll.layer=? " +
				" and t.\"GEOMETRY\" && setsrid(ll.\"GEOMETRY\","+srid.getSRID(iMunicipio)+");";

				psGeom=conn.prepareStatement(sqlGeom);
				psGeom.setInt(1,feature);
				psGeom.setInt(2,iMunicipio);
				psGeom.setString(3,layer);

				rs = psGeom.executeQuery();

				if (rs.next()){
					lockStatus=(rs.getInt("user_id")!=userId)? AdministradorCartografiaClient.LOCK_LAYER_OTHER
							: AdministradorCartografiaClient.LOCK_LAYER_OWN;
				}

				rs.close();
			}finally{
				try{rs.close();}catch(Exception e){};
				try{psGeom.close();}catch(Exception e){};            
			}
			return lockStatus;
		}

		private class InsertLockThread extends Thread{        		
			List featuresIds = null;
			List layers = null;
			List featuresToLock = null;
			int municipio;
			int userId;
			int first;
			int last;        	

			public InsertLockThread(Connection conn, List featuresToLock, List featuresIds,
					List layers, int municipio, int userId, int first, int last){
				super();
				this.featuresIds = featuresIds;
				this.layers = layers;
				this.featuresToLock = featuresToLock;
				this.municipio = municipio;
				this.userId = userId;
				this.first = first;
				this.last = last;        					
			}

			public void run() {    					
				PreparedStatement psInsertFeatureLock = null;
				String layer = null;
				int featureId = -1;

				try {    						
					String sqlInsertFeatureLock="insert into locks_feature (municipio,layer,feature_id,user_id,ts) "
					+"values (?,?,?,?,?)";

					psInsertFeatureLock = 
						conn.prepareStatement(sqlInsertFeatureLock);

					for (int i = first; i < last; i++){
						int index = ((Integer) featuresToLock.get(i)).intValue();
						layer = (String) layers.get(index);
						featureId = ((Integer) featuresIds.get(index)).intValue();

						psInsertFeatureLock.setInt(1,municipio);
						psInsertFeatureLock.setString(2,layer);
						psInsertFeatureLock.setInt(3,featureId);
						psInsertFeatureLock.setInt(4,userId);
						psInsertFeatureLock.setTimestamp(5,new Timestamp(System.currentTimeMillis()));
						psInsertFeatureLock.executeUpdate();

						if (Thread.interrupted()){
							return;
						}
					}

				} catch (Exception e){
					int errorCode = AdministradorCartografiaClient.LOCK_FEATURE_ERROR;
					featureLockErrorListener(this, errorCode, layer, featureId);
				} finally {
					try{psInsertFeatureLock.close();}catch(Exception e){};
				}
			}
		}

		private synchronized void featureLockErrorListener(Thread failingThread,
				int errorCode, String layer, int featureId){
			// Si otro thread tambien obtuvo un error, solo se considerara
			// el error del primer thread que invoque a esta funcion
			if (Thread.interrupted()){
				return;
			}

			for (int i = 0; i < insertLockThreads.length; i++){
				if (insertLockThreads[i] != failingThread){
					insertLockThreads[i].interrupt();
				}
			}

			featureLockResult = new FeatureLockResult(errorCode, layer, featureId); 

			return;
		}

		private void addLockOwnerUserInfo(FeatureLockResult lockResult, int lockOwnerUserId){
			String sqlFindUserName = "SELECT u.name FROM iuseruserhdr u WHERE u.id=?";
			PreparedStatement psFindUserName = null;
			ResultSet rs = null;
			try {

				psFindUserName = conn.prepareStatement(sqlFindUserName);
				psFindUserName.setInt(1, lockOwnerUserId);
				rs = psFindUserName.executeQuery();

				String lockOwnerUserName = null;
				if (rs.next()){
					lockOwnerUserName = rs.getString("name");
				}

				lockResult.setLockOwnerUserName(lockOwnerUserName);
				lockResult.setLockOwnerUserId(new Integer(lockOwnerUserId));

			} catch (SQLException e) {
				e.printStackTrace();
			}
			finally {
				try{rs.close();}catch(Exception e){};
				try{psFindUserName.close();}catch(Exception e){};        
			}
		}
	}
	private ArrayList getValidateFeatures(ACLayer acLayer, String sLocale) throws Exception {
		return getValidateFeatures(acLayer,sLocale,null);
	}
	private ArrayList getValidateFeatures(ACLayer acLayer, String sLocale,Connection connection) throws Exception {

		ArrayList lstValidates = null;

		String sSQL = "select l.validator, d.locale "
			+ "from layers l, dictionary d "
			+ "where l.name=? and l.id_name=d.id_vocablo ";

		Connection conn=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		boolean bCerrarConexion=true;
		try{
			if (connection!=null)
			{
				conn=connection;
				bCerrarConexion=false;
			}
			else
			{
				conn=openConnection();
				bCerrarConexion=true;
			}
			ps=conn.prepareStatement(sSQL);
			ps.setString(1,acLayer.getSystemName());
			rs=ps.executeQuery();

			if (rs.next()){

				int iCurrentLayerLocale= -1;
				int iLastLayerLocale=4;
				boolean mejorOpcion= false;

				AbstractValidator abstractValidator = null;
				String sValidator = null;

				do{
					iCurrentLayerLocale= getOpcionLocale(rs.getString("locale"), sLocale);
					/** Priorizamos los idiomas: preferencia de usuario, es_ES, resto de idiomas */
					if (iCurrentLayerLocale < iLastLayerLocale){

						sValidator = rs.getString("validator");

						iLastLayerLocale= iCurrentLayerLocale;
						if (iCurrentLayerLocale == 1) mejorOpcion= true;
					}
				}while ((rs.next()) && (!mejorOpcion));

				String sValidates[] = null;

				if (sValidator != null && !sValidator.equals("")){
					lstValidates = new ArrayList();
					sValidates = sValidator.split(";");
					for(int i=0;i<sValidates.length;i++){
						abstractValidator = (AbstractValidator) Class
						.forName(sValidates[i]).newInstance();
						lstValidates.add(abstractValidator);
					}
				}  
			}
			return lstValidates;

		}catch(Exception e){
			try{conn.rollback();}catch(Exception ex){};
			throw e;
		}finally{
			try{rs.close();}catch(Exception e){};
			try{ps.close();}catch(Exception e){};
			try{if (bCerrarConexion){conn.close();CPoolDatabase.releaseConexion();}}catch(Exception e1){};

		}
	}
    /**
     * Devuelve una lista con todos los usuarios de la entidad
     */
	public ListaUsuarios getUsuarios(int idEntidad)throws SQLException{
    	
    	 Connection conn=null;
         PreparedStatement ps=null;
         ResultSet rs=null;
         ListaUsuarios auxListaUsuarios =new ListaUsuarios();
         try{
             conn=openConnection();
             //ps=conn.prepareStatement(sSQL);

                // Primero buscamos los roles
                //Select id, name, nombrecompleto, password, remarks,mail, deptid FROM IUSERUSERHDR where borrado!=1 and id_municipio=?
                ps = conn.prepareStatement("SELECT * FROM IUSERUSERHDR where borrado!=1 and (id_entidad=? or id_entidad=0 or id_entidad is NULL)");
                ps.setInt(1,idEntidad);
                rs =ps.executeQuery();
                EncriptarPassword  ep=new EncriptarPassword();
                while (rs.next())
                {
                	String id=rs.getString("id");
                	String name=rs.getString("name");
                	String password = "";
					try {
						password = ep.undoEncrip(rs.getString("password"));
					} catch (Exception e) {
						 logger.error("Error al desencriptar el password");
					}
                	String deptid=rs.getString("deptid");
                	String email=rs.getString("mail");
                	String nif=rs.getString("nif");
                	String remarks=rs.getString("remarks");
                	String nombreCompleto= rs.getString("nombrecompleto");
                	//SATEC corregir id entidad
                	//Usuario usuario=new Usuario(id,name,password,deptid,email,remarks,nombreCompleto,nif);
                	Usuario usuario=new Usuario(id,name,password,deptid,email,remarks,nombreCompleto,nif, String.valueOf(idEntidad));
                	System.out.println("Añadiendo usuario: "+name);
                    auxListaUsuarios.add(usuario);
                }
                //System.out.println("Finalizamos añadiendo usuarios");
                rs.close();
			    ps.close();
                // en vez de la sentencia SQL.
                //select r_usr_perm.userid userid , r_usr_perm.idperm idperm, r_usr_perm.idacl idacl, r_usr_perm.aplica aplica from r_usr_perm order by userid
                ps = conn.prepareStatement("SELECT r_usr_perm.userid AS userid , r_usr_perm.idperm AS idperm, r_usr_perm.idacl AS idacl, r_usr_perm.aplica AS aplica from r_usr_perm order by userid");
                rs =ps.executeQuery();
                while (rs.next())
                {
                    Permiso auxPermiso=new Permiso(rs.getString("idPerm"),rs.getString("idAcl"),!(rs.getString("aplica")!=null&&rs.getString("aplica").equals("0")));
                    String sIdUsuario=rs.getString("userid");
                    auxListaUsuarios.addPermiso(sIdUsuario,auxPermiso);
                }
                rs.close();
			    ps.close();
                //allUsuariosroles
                //select iusergroupuser.userid userid , iusergroupuser.groupid groupid  from iusergroupuser order by userid
                ps = conn.prepareStatement("SELECT iusergroupuser.userid AS userid , iusergroupuser.groupid AS groupid  from iusergroupuser order by userid");
                rs =ps.executeQuery();
                while (rs.next())
                {
                     auxListaUsuarios.addGrupo(rs.getString("userid"),rs.getString("groupid"));
                }
                
       } catch (SQLException e) {
            java.io.StringWriter sw=new java.io.StringWriter();
		    java.io.PrintWriter pw=new java.io.PrintWriter(sw);
	    	e.printStackTrace(pw);
		    logger.error("ERROR al coger los usuarios :"+sw.toString());
            throw e;

        } finally {
        	try{
            rs.close();
            ps.close();
		    conn.close();
        	}catch (Exception e) {}
        }
        
        return auxListaUsuarios;
    }
    
    /**
     * Devuelve una lista con todos los usuarios con permiso de lectura sobre las capas indicadas
     * @param idEntidad
     * @param listaCapas
     * @return
     * @throws SQLException
     */
    public ListaUsuarios getUsuariosPermisosCapas(List<Integer> listaCapas, int idEntidad)throws SQLException{
    	if(listaCapas==null || listaCapas.size()==0){return null;}
   	 	Connection conn=null;
        Statement st=null;
        ResultSet rs=null;
        //ANTES
        //final String permLectura = "871";
        //String whereLayers = "where id_layer=";
        //Integer idCapa = null;
        //for (int i = 0; i < listaCapas.size(); i++) {
        //	idCapa = listaCapas.get(i);
        //	if(i>0){
        //		whereLayers+=" or id_layer=";
        //	}
        //	whereLayers+=idCapa;
		//}
        String whereLayers = "where id_layer IN (";
        Integer idCapa = null;
        for (int i = 0; i < listaCapas.size(); i++) {
        	idCapa = listaCapas.get(i);
        	if(i>0){
        		whereLayers+=",";
        	}
        	whereLayers+=idCapa;
		}
        whereLayers += ")";

        ListaUsuarios auxListaUsuarios =new ListaUsuarios();
        
        try{
            conn=openConnection();
//            String consulta = "select * from iuseruserhdr where " +
//		   	"borrado!=1 and (id_municipio=? or id_municipio=0 or id_municipio is NULL) and id in (" + 
//		   	"select userid from r_usr_perm where idperm="+permLectura+" and " +
//		   	"(r_usr_perm.aplica<>0 or r_usr_perm.aplica is null) and idacl in ("+
//		   	"select acl from layers "+whereLayers+") "+
//		   	"union select b.userid from r_group_perm a, iusergroupuser b "+ 
//		   	"where a.idperm="+permLectura+" and a.groupid=b.groupid and idacl in ( "+
//		   	"select acl from layers "+whereLayers+"))";
//	ANTES
//            String consulta = "select * from iuseruserhdr where " +
//            "borrado!=1 and (id_entidad="+idEntidad+" or id_entidad=0 or id_entidad is NULL) and id in (" +
//            "select userid from r_usr_perm where idperm="+permLectura+" and " +
//            "(r_usr_perm.aplica<>0 or r_usr_perm.aplica is null) and idacl in (" +
//            "select acl from layers "+whereLayers+") " +
//            "union select b.userid from r_group_perm a, iusergroupuser b " +
//            "where a.idperm=871 and a.groupid=b.groupid and b.groupid in (" +
//            "select groupid from r_group_perm where idperm="+permLectura+" and idacl in (" +
//            "select acl from layers "+whereLayers+") " +
//            "group by groupid having count(idacl)=(select count(acl) from layers "+whereLayers+")))";
//   DESPUES    
          String consulta = "select * from iuseruserhdr where " +
          "borrado!=1 and (id_entidad="+idEntidad+" or id_entidad=0 or id_entidad is NULL) and id in (" +
          "select userid from r_usr_perm where " +
          "(r_usr_perm.aplica<>0 or r_usr_perm.aplica is null) and idperm in (" +
          "select idperm from r_usr_perm where idacl in (" +
          "select acl from layers "+whereLayers+")) " +
          "union select b.userid from r_group_perm a, iusergroupuser b " +
          "where a.idperm=871 and a.groupid=b.groupid and b.groupid in (" +
          "select groupid from r_group_perm where idacl in (" +
          "select acl from layers "+whereLayers+") " +
          "group by groupid having count(idacl)=(select count(acl) from layers "+whereLayers+")))";
            
            logger.info(">> "+consulta+" <<");
            
               st = conn.createStatement();
               rs =st.executeQuery(consulta);
               
               EncriptarPassword  ep=new EncriptarPassword();
               while (rs.next())
               {
               	String id=rs.getString("id");
               	String name=rs.getString("name");
               	String password = "";
					try {
						password = ep.undoEncrip(rs.getString("password"));
					} catch (Exception e) {
						 logger.error("Error al desencriptar el password");
					}
               	String deptid=rs.getString("deptid");
               	String email=rs.getString("mail");
               	String nif=rs.getString("nif");
               	String remarks=rs.getString("remarks");
               	String nombreCompleto= rs.getString("nombrecompleto");
               	//SATEC corregir id entidad
               	//Usuario usuario=new Usuario(id,name,password,deptid,email,remarks,nombreCompleto,nif);
               	Usuario usuario=new Usuario(id,name,password,deptid,email,remarks,nombreCompleto,nif,String.valueOf(idEntidad));
                
               	System.out.println("Añadiendo usuario: "+name);
                   auxListaUsuarios.add(usuario);
               }
              // System.out.println("Finalizamos añadiendo usuarios");

      } catch (SQLException e) {
           java.io.StringWriter sw=new java.io.StringWriter();
		    java.io.PrintWriter pw=new java.io.PrintWriter(sw);
	    	e.printStackTrace(pw);
		    logger.error("ERROR al coger los usuarios :"+sw.toString());
           throw e;

       }finally {
           try {rs.close();
		    st.close();
		    conn.close();
           }catch (Exception e) {}
       }
       
       return auxListaUsuarios;
   } 
    
    /**
     * Crea un nuevo proyecto de extracción en BBDD
     * @param eProject
     * @throws SQLException
     */
    public void crearProyectoExtraccion(ExtractionProject eProject, int idEntidad)throws SQLException{
        String sSQLProjects="insert into proyectos_extraccion (id_proyecto,fecha_extraccion,nombre_proyecto,pos_esquina_x," +
        		"pos_esquina_y,ancho_celdas,alto_celdas,celdas_x,celdas_y,id_map,id_entidad) values (?,?,?,?,?,?,?,?,?,?,?)";
        
        String sSQLLayers="insert into capas_extraccion (id_proyecto,id_layer_extract) values (?,?)";
        
        logger.info(">> "+sSQLProjects+" <<");
        logger.info(">> "+sSQLLayers+" <<");
        
        Connection conn=null;
        PreparedStatement psProj=null;
        PreparedStatement psLayers=null;
        try{
        	conn=openConnection();
        	conn.setAutoCommit(false); //inicio de la transacción
        	psProj=conn.prepareStatement(sSQLProjects);
        	psLayers=conn.prepareStatement(sSQLLayers);
        	
        	//insercción de proyecto de extracción
        	psProj.setString(1,eProject.getIdProyecto());
        	psProj.setTimestamp(2,new Timestamp(eProject.getFechaExtraccion().getTime()));
        	psProj.setString(3,eProject.getNombreProyecto());
        	psProj.setDouble(4,eProject.getPosEsquinaX());
        	psProj.setDouble(5,eProject.getPosEsquinaY());
        	psProj.setDouble(6,eProject.getAnchoCeldas());
        	psProj.setDouble(7,eProject.getAltoCeldas());
        	psProj.setInt(8, eProject.getCeldasX());
        	psProj.setInt(9, eProject.getCeldasY());
        	psProj.setString(10, eProject.getIdMapa());
        	psProj.setInt(11, idEntidad);
        	psProj.executeUpdate();
        	//inserción de capas extraídas
            List<Integer> idExtractLayersList = eProject.getIdExtractLayersList();
            Integer idLayer = null;
            for (int i = 0; i < idExtractLayersList.size(); i++) {
            	idLayer = idExtractLayersList.get(i);
                psLayers.setString(1,eProject.getIdProyecto());
                psLayers.setInt(2,idLayer);
                psLayers.executeUpdate();
			}
            conn.commit(); //fin de la transacción
        }catch(SQLException se){
        	conn.rollback();
            logger.error("crearProyectoExtraccion " + se.getMessage(), se);
        }finally{
        	conn.setAutoCommit(true);
            try{psProj.close();}catch(Exception e){};
            try{psLayers.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
        }
    }
    
    /**
     * Borrar un proyecto de extraccion
     */
    public void deleteProyectoExtraccion(ExtractionProject eProject, int idEntidad)throws SQLException{
        String sSQLProjects="delete from proyectos_extraccion where id_proyecto=?";
        
        String sSQLLayers="delete from capas_extraccion where id_proyecto=?";
        String sSQLCeldas="delete from celdas_extraccion where id_proyecto=?";
               
        
        Connection conn=null;
        PreparedStatement psProj=null;
        PreparedStatement psLayers=null;
        PreparedStatement psCeldas=null;
        try{
        	conn=openConnection();
        	conn.setAutoCommit(false); //inicio de la transacción
        	psProj=conn.prepareStatement(sSQLProjects);
        	psLayers=conn.prepareStatement(sSQLLayers);
        	psCeldas=conn.prepareStatement(sSQLCeldas);
        	
        	
        	//borrado de las celdas del proyecto.
        	psCeldas.setString(1,eProject.getIdProyecto());
        	psCeldas.executeUpdate();
        	
        	//borrado de las capas del proyecto.
        	psLayers.setString(1,eProject.getIdProyecto());
        	psLayers.executeUpdate();
        	
        	psProj.setString(1,eProject.getIdProyecto());        	
        	psProj.executeUpdate();
            conn.commit(); //fin de la transacción
        }catch(SQLException se){
        	conn.rollback();
            logger.error("deleteProyectoExtraccion " + se.getMessage(), se);
        }finally{
        	conn.setAutoCommit(true);
            try{psProj.close();}catch(Exception e){};
            try{psLayers.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
        }
    }
    
    /**
     * Devuelve una lista con los proyectos de desconexión asociados a un mapa
     */
    public List<ExtractionProject> getProyectosExtraccion(int idMapa, int idEntidad)throws SQLException{
   	 	Connection conn=null;
        PreparedStatement psProj=null;
        PreparedStatement psLayers=null;
        ResultSet rsProj=null;
        ResultSet rsLayers=null;
        
        List<ExtractionProject> proyectosExtraidos = new ArrayList<ExtractionProject>();
        ExtractionProject currProj = null;
        try{
            conn=openConnection();

            String sqlProjects = "select * from proyectos_extraccion where id_map=? and id_entidad=?";
            String sqlLayers = "select * from capas_extraccion where id_proyecto=?";
            logger.info(">> "+sqlProjects+" <<");
            logger.info(">> "+sqlLayers+" <<");
            
            psProj = conn.prepareStatement(sqlProjects);
           	psLayers = conn.prepareStatement(sqlLayers);
           	
            psProj.setInt(1, idMapa);
            psProj.setInt(2, idEntidad);
            rsProj = psProj.executeQuery();
            
            if(rsProj==null){ 
            	logger.warn("No hay proyectos de extraccion para el mapa " + idMapa + " idEntidad " + idEntidad);
            	return proyectosExtraidos;
            }
            	   
               while (rsProj.next())
               {       	
            	    //info del proyecto de extracción
	               	String id_proyecto=rsProj.getString("id_proyecto");
	               	java.util.Date fecha_extraccion=rsProj.getTimestamp("fecha_extraccion");
	               	String nombre_proyecto=rsProj.getString("nombre_proyecto");
	               	Double pos_esquina_x=rsProj.getDouble("pos_esquina_x");
	               	Double pos_esquina_y=rsProj.getDouble("pos_esquina_y");
	               	Double ancho_celdas=rsProj.getDouble("ancho_celdas");
	               	Double alto_celdas= rsProj.getDouble("alto_celdas");
	               	Integer celdas_x= rsProj.getInt("celdas_x");
	               	Integer celdas_y= rsProj.getInt("celdas_y");
	               	String id_map = rsProj.getString("id_map");
	               	Integer id_entidad = rsProj.getInt("id_entidad");
	               	
	               	//capas extraídas del proyecto
	               	psLayers.setString(1, id_proyecto);
	                rsLayers = psLayers.executeQuery();
	                
	                if(rsLayers==null){ 
	                	logger.warn("No hay capas de extraccion para el mapa " + idMapa + " idEntidad " + idEntidad + " proyecto " + id_proyecto);
	                	continue;
	                }
	                
	                List<Integer> idExtractLayersList = new ArrayList<Integer>();
	                while (rsLayers.next())
	                {   
	                	idExtractLayersList.add(rsLayers.getInt("id_layer_extract"));
	                }
	               	
	                //creamos el proyecto
	               	currProj=new ExtractionProject(id_proyecto, fecha_extraccion, nombre_proyecto,
	               			pos_esquina_x, pos_esquina_y, ancho_celdas, alto_celdas, 
	               			celdas_x, celdas_y, id_map, id_entidad, idExtractLayersList);
	               	
	               	logger.debug("Obteniendo proyecto de extraccion: "+id_proyecto + " -> date: " + fecha_extraccion);
	               	proyectosExtraidos.add(currProj);
               }
               
           		logger.debug("Finalizamos obteniendo proyectos de extraccion");
      } catch (SQLException e) {
		    logger.error("ERROR al obtener proyectos :"+e, e);
           throw e;
       } finally {
    	   try {
    	   	rsProj.close();
    	   	rsLayers.close();
    	   	psProj.close();
    	   	psLayers.close();
		    conn.close();
    	   }catch (Exception e) {}
       }
       return proyectosExtraidos;
   }

    /**
     * Asigna los usuarios a las celdas del proyecto de extracción
     */
	public void asignarCeldasProyectoExtraccion(String idProyectoExtract, HashMap<String, String> celdasUsuarios) throws SQLException {
		String sqlSelect = "select * from celdas_extraccion where id_proyecto=? and num_celda=?";
		String sqlInsert="insert into celdas_extraccion (id_proyecto,num_celda,id_usuario_asign) values (?,?,?)";
		String sqlUpdate="update celdas_extraccion set id_usuario_asign=? where id_proyecto=? and num_celda=?";
		String sqlDelete="delete from celdas_extraccion where id_proyecto=? and num_celda=?";
	   
	   	logger.info(">> "+sqlSelect+" <<");
	   	logger.info(">> "+sqlInsert+" <<");
	   	logger.info(">> "+sqlUpdate+" <<");
	   	logger.info(">> "+sqlDelete+" <<");
		
		   Connection conn=null;
		   PreparedStatement psSel=null;
		   PreparedStatement psIns=null;
		   PreparedStatement psUpd=null;
		   PreparedStatement psDel=null;
		   try{
		   	conn=openConnection();
		   	psSel=conn.prepareStatement(sqlSelect);
		   	psIns=conn.prepareStatement(sqlInsert);
		   	psUpd=conn.prepareStatement(sqlUpdate);
		   	psDel=conn.prepareStatement(sqlDelete);
		   	//insercción o actualización de las celdas asignadas a los usuarios
		   	Iterator<String> keyIterator = celdasUsuarios.keySet().iterator();
		   	String idCeldaStr = null;
		   	Integer idCelda = null;
		   	String idUsuarioStr = null;
			Integer idUsuario = null;
			Integer id_usuario_asign = null;
		   	ResultSet rs = null;
		   	boolean cuadriculaSinAsignar = true;
		    while (keyIterator.hasNext()) {
		    	idCeldaStr = (String) keyIterator.next();
				idCelda = Integer.parseInt(idCeldaStr);
				idUsuarioStr = celdasUsuarios.get(idCeldaStr);
				cuadriculaSinAsignar = idUsuarioStr.equals(ComboItemGraticuleListener.SIN_ASIGNAR);
				if(!cuadriculaSinAsignar){
					idUsuario =  Integer.parseInt(idUsuarioStr);
				} else {idUsuario = 0;}
				
		    	//hacemos un select para ver si hay valores
				psSel.setString(1,idProyectoExtract);
				psSel.setInt(2,idCelda);
				rs = psSel.executeQuery();
				//si hay resultados hacemos un update o delete
				if(rs.next()){
					id_usuario_asign=rs.getInt("id_usuario_asign");
					//si no hay cuadricula asignada borraremos la entrada
					if(cuadriculaSinAsignar){
						psDel.setString(1,idProyectoExtract);
						psDel.setInt(2,idCelda);
						psDel.executeUpdate();	
					}
					//sino actualizamos en caso de que haya que hacerlo
					else if(id_usuario_asign!=null && !id_usuario_asign.equals(idUsuario)){
						psUpd.setInt(1,idUsuario);
						psUpd.setString(2,idProyectoExtract);
						psUpd.setInt(3,idCelda);
						psUpd.executeUpdate();	
					}
				}
				//sino un insert si están asignados
				else if(!cuadriculaSinAsignar){
					psIns.setString(1,idProyectoExtract);
					psIns.setInt(2,idCelda);
					psIns.setInt(3,idUsuario);
					psIns.executeUpdate();	
		    	}
				rs.close();
		    }
		   }catch(SQLException se){
		       logger.error("asignarCeldasProyectoExtraccion " + se.getMessage(), se);
		   }finally{
		       try{psSel.close();}catch(Exception e){};
		       try{psIns.close();}catch(Exception e){};
		       try{psUpd.close();}catch(Exception e){};
		       try{psDel.close();}catch(Exception e){};
		       try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
		   }
	} 
	
	/**
	 * Obtiene las celdas asignadas de un proyecto de extracción
	 */
	public HashMap<String, String> obtenerCeldasProyectoExtraccion(String idProyectoExtract)throws SQLException {
		HashMap<String, String> celdasUsuarios = new HashMap<String, String>();
 	 	Connection conn=null;
        Statement st=null;
        ResultSet rs=null;
		
	       try{
	            conn=openConnection();

	            String consulta = "select * from celdas_extraccion where id_proyecto = '" + idProyectoExtract + "'";
	            
	            logger.info(">> "+consulta+" <<");
	            
	               st = conn.createStatement();
	               rs =st.executeQuery(consulta);
	               while (rs.next())
	               {
	               	//String id_proyecto=rs.getString("id_proyecto");
	               	Integer num_celda=rs.getInt("num_celda");
	               	Integer id_usuario_asign=rs.getInt("id_usuario_asign");
	               	celdasUsuarios.put(String.valueOf(num_celda), String.valueOf(id_usuario_asign));	               	
	               }

	      } catch (SQLException e) {
			    logger.error("ERROR al obtener celdas y sus usuarios asignados :"+e,e);
	           throw e;

	       }finally {
	           try {rs.close();
			    st.close();
			    conn.close();
	           }catch (Exception e) {}
	       }
	       
	       return celdasUsuarios;
	}
	
	/**
	 * Busca por el valor de un atributo dentro de las features de una capa
	 */
    public void searchByAttribute(String idEntidad, int iMunicipio, String sLayer, String sLocale, FilterNode fn,
            ObjectOutputStream oos, ISesion sesion, String attributeName, String attributeValue,
            boolean bValidateData, Integer srid_destino) throws IOException, SQLException,
            Exception {
        
        int iRet = -1;
        ACLayer acLayer=null;
        
        String sSQL = "select q.id,q.selectquery,q.updatequery,q.insertquery,q.deletequery,"
                + "l.name,l.acl,l.id_layer,d.traduccion,l.id_styles,l.extended_form,s.xml,d.locale,l.modificable, "
                + "from queries q,layers l, dictionary d, styles s " + "where q.databasetype="
                + DATABASETYPE + " and l.name=? and " + "q.id_layer=l.id_layer and  "
                + "l.id_name=d.id_vocablo and " + "l.id_styles=s.id_style ";
	    
	    
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = openConnection();
            ps = conn.prepareStatement(sSQL);
            ps.setString(1, sLayer);
            rs = ps.executeQuery();
            
            String selectQuery= null;
            if (rs.next()){
                
                selectQuery = rs.getString("selectquery");

                /** Incidencia [365] */
                int iCurrentLayerLocale= -1;
                /** aseguramos que entra la primera vez */
                int iLastLayerLocale=4;
                boolean mejorOpcion= false;

                do{
                    iCurrentLayerLocale= getOpcionLocale(rs.getString("locale"), sLocale);
                    /** Priorizamos los idiomas: preferencia de usuario, es_ES, resto de idiomas */
                    if (iCurrentLayerLocale < iLastLayerLocale) {                        
                        
						//Compruebo si la capa es dinámica o no 
						String url = getUrlDynamicLayer(conn, rs.getInt("id_layer"),Integer.parseInt(idEntidad));
                        if (url.equals("")){ // capa no dinámica
                            acLayer=new ACLayer(rs.getInt("id_layer"),rs.getString("traduccion"),rs.getString("name"),replaceSRID(rs.getString("selectquery"), Integer.parseInt(sesion.getIdMunicipio())));
                        }else{
                            acLayer=new ACDynamicLayer(rs.getInt("id_layer"),rs.getString("traduccion"),rs.getString("name"),replaceSRID(rs.getString("selectquery"),Integer.parseInt(sesion.getIdMunicipio())),url);
                        }
                        acLayer.setACL(rs.getLong("acl"));
                        acLayer.setStyleId(String.valueOf(rs.getInt("id_styles")));
                        acLayer.setStyleXML(rs.getString("xml"));
                        acLayer.setExtendedForm(rs.getString("extended_form"));
                        acLayer.setActive(true);
                        acLayer.setVisible(true); 
                        acLayer.setEditable(rs.getBoolean("modificable"));
                                                                        
                        iLastLayerLocale = iCurrentLayerLocale;
                        if (iCurrentLayerLocale == 1)
                            mejorOpcion = true;
                    }
                } while ((rs.next()) && (!mejorOpcion));
            }
            
            rs.close();
            ps.close();
                        
            GeopistaAcl acl=getPermission(sesion,acLayer.getACL(),conn);
            checkReadPermLayer(sesion,acl,sLayer);

            readNewSchema(acLayer,sLocale, conn, sesion,acl);
            oos.writeObject(acLayer);
			oos.reset();	

            ArrayList lstValidateFeatures = null;
            if (bValidateData) {
                lstValidateFeatures = getValidateFeatures(acLayer, sLocale, conn);
            }

            if (lstValidateFeatures != null){
                for (Iterator iterValidateFeatures = lstValidateFeatures.iterator();iterValidateFeatures.hasNext();){
                    AbstractValidator validateFeatures = (AbstractValidator)iterValidateFeatures.next();
					if (validateFeatures instanceof VersionValidator){
						Version version = sesion.getVersion();
						if (version == null)
							version = new Version();
						version.setIdTable(this.obtenerIdTabla(conn, acLayer));
						validateFeatures.setVersion(version);
						if (version != null && !version.isFeaturesActivas()){
							if (!checkPermission(sesion,acLayer.getACL(),Const.PERM_LAYER_READ,conn))
								throw new PermissionException(Const.PERM_LAYER_READ);
						}
					}
                    validateFeatures.beforeRead(acLayer);
                }
            }

            conn.setAutoCommit(false);
            
            StringBuffer sbSQL = new StringBuffer(selectQuery);
            
            
            int iOffset = selectQuery.toUpperCase().indexOf(" GROUP BY");
            if (iOffset == -1)
                iOffset = sbSQL.length();
            sbSQL.insert(iOffset, " and " + attributeName + " = '" + attributeValue + "'");
            
            //REFACTORIZACION ORACLE PreparedStatement[] apsUpdate=new SQLParser(conn,srid).newUpdate(iMunicipio,sSQL,acLayer,upload);
            PreparedStatement apsSelect = new SQLParserPostGIS(conn, srid).newSelect(idEntidad,
                    String.valueOf(iMunicipio), sbSQL.toString(), acLayer, null, fn, srid_destino,null);
            rs = apsSelect.executeQuery();
            ACFeature acFeature=null;
            while (rs.next()) {
                acFeature = new ACFeature();
                ACAttribute att = null;
                Object oAttValue = null;
                for (Iterator it = acLayer.getAttributes().values().iterator(); it.hasNext();) {
                    att = (ACAttribute) it.next();
                    switch (att.getColumn().getType()) {
                    case ACLayer.TYPE_GEOMETRY:
                        PGgeometry pgGeometry = (PGgeometry) rs.getObject("GEOMETRY");
                        if (pgGeometry == null)
                            continue;
                        StringBuffer noSRIDgeometry = new StringBuffer();
                        pgGeometry.getGeometry().outerWKT(noSRIDgeometry);
                        acFeature.setGeometry(noSRIDgeometry.toString());
                        break;
                    case ACLayer.TYPE_NUMERIC:
                    case ACLayer.TYPE_STRING:
                    case ACLayer.TYPE_DATE:
                    case ACLayer.TYPE_BOOLEAN:
                    default:
                        try {
                            oAttValue = rs.getObject(att.getColumn().getName());
                            acFeature.setAttribute(att.getName(), (Serializable) oAttValue);
                        } catch (Exception ex) {
                        }
                    }
                }
                oos.writeObject(acFeature);
    			oos.reset();	

            }
            if (acFeature != null) {
                iRet = acFeature.findID(acLayer);

                if (lstValidateFeatures != null) {
                    for (Iterator iterValidateFeatures = lstValidateFeatures.iterator(); iterValidateFeatures
                            .hasNext();) {
                        AbstractValidator validateFeatures = (AbstractValidator) iterValidateFeatures
                                .next();
                        validateFeatures.afterRead(acFeature, acLayer);
                    }
                }
            }
            
        } catch (Exception e) {
            oos.writeObject(new ACException(e));
			//Modificado para reestablecer el BufferedOutputStream
			oos.reset();

            throw e;
        } finally {
            try {
                rs.close();
            } catch (Exception e) {
            }
            try {
                ps.close();
            } catch (Exception e) {
            }
            try {
                conn.close();
                CPoolDatabase.releaseConexion();
            } catch (Exception e) {
            }
        }
        
	}
    
    /**
     * Dada una revisión, obtiene la fecha en que se creó
     * @param conn
     * @param layerName
     * @param revision
     * @return
     * @throws SQLException
     */
    private String obtieneFechaRevision(Connection conn, String layerName, long revision) throws SQLException{
    	StringBuffer stQuery = new StringBuffer("select to_char(v.fecha,'yyyy-MM-dd HH24:MI:ss') from versiones v where v.id_table_versionada= ");
    	stQuery = stQuery.append(consultaTable);
    	if (revision != -1)
    		stQuery = stQuery.append(" and v.revision=? ");
    	stQuery = stQuery.append(" order by 1 desc");
    	PreparedStatement ps = conn.prepareStatement(stQuery.toString());
    	ps.setString(1, layerName);
    	if (revision != -1)
    		ps.setLong(2, revision);
    	ResultSet rs = ps.executeQuery();
    	String fecha = "";
    	if (rs.next()){
    		fecha = rs.getString(1);
    		fecha = fecha.replace(' ', 'T');
    	}
    	rs.close();
    	ps.close();
    	return fecha;
    }

    
    /**
     * Obtiene la última revisión asociada a una capa 
     * @param conn
     * @param layerName
     * @return
     * @throws SQLException
     */
    private long obtieneUltimaRevision(Connection conn, String layerName) throws SQLException{
    	PreparedStatement ps = conn.prepareStatement("select coalesce(max(v.revision),0) from versiones v where v.id_table_versionada="+consultaTable);
    	ps.setString(1, layerName);
    	ResultSet rs = ps.executeQuery();
    	long revision = 0;
    	if (rs.next()){
    		revision = rs.getLong(1);
    	}
    	rs.close();
    	ps.close();
    	return revision;
    }
    

    /**
     * Obtiene la revisión correspondiente a la capa dada una fecha 
     * @param conn
     * @param layerName
     * @param fecha
     * @return
     * @throws SQLException
     */
    private long obtieneRevision(Connection conn, String layerName, String fecha) throws SQLException{
    	try{
	    	StringBuffer sb = new StringBuffer("select coalesce(max(v.revision),0) from versiones v ");
	    	sb = sb.append("where v.id_table_versionada= ");
	    	sb = sb.append(consultaTable);
	    	sb = sb.append(" and fecha <= to_timestamp(?,'yyyy-MM-dd HH24:MI:ss')");
	    	PreparedStatement ps = conn.prepareStatement(sb.toString());
	    	ps.setString(1, layerName);
	    	ps.setString(2, fecha);
	    	ResultSet rs = ps.executeQuery();
	    	long revision = 0;
	    	if (rs.next()){
	    		revision = rs.getLong(1);
	    	}
	    	rs.close();
	    	ps.close();
	    	return revision;
    	}catch(SQLException e){
    		throw e;
    	}
    }

    /**
     * Obtiene datos sobre la capa
     * @return
     */
    private ACLayer obtieneLayerAsociado(Connection conn, ACMap acMap, LayerStyleData lsd) throws SQLException{
		Enumeration enumLF = acMap.getLayerFamilies().elements();
		ACLayerFamily layerFamily = null;
		while (enumLF.hasMoreElements()){
			layerFamily = (ACLayerFamily)enumLF.nextElement();
			if (layerFamily.getId() == Integer.parseInt(lsd.getIdLayerFamily()))
				break;
		}
		ACLayer layer = null;
		if (layerFamily.getLayers() != null){
			Enumeration enumLayers = layerFamily.getLayers().elements();
			while (enumLayers.hasMoreElements()){
				layer = (ACLayer)enumLayers.nextElement();
				if (layer.getSystemName().equals(lsd.getIdLayer()))
					break;
			}
		}
		if (layer.getRevisionActual() == -1)
			layer.setRevisionActual(this.obtieneUltimaRevision(conn, layer.getSystemName()));
		return layer;
    }
    
    /**
     * Obtiene el id de la tabla asociada a una layer
     * @param conn
     * @param acLayer
     * @return
     */
	private long obtenerIdTabla(Connection conn, ACLayer acLayer) throws SQLException{
		try{
			long idTabla = -1;
			StringBuffer consultaTable = new StringBuffer("select c.id_table from layers l, attributes a,columns c ");
			consultaTable = consultaTable.append("where l.id_layer = a.id_layer and a.id_column = c.id ");
			consultaTable = consultaTable.append("and a.position = 1 and l.name = ?");
		    PreparedStatement st = conn.prepareStatement(consultaTable.toString());
		    st.setString(1, acLayer.getSystemName());
		    ResultSet rs = st.executeQuery();
		    if (rs.next()){
		        idTabla = rs.getLong("id_table");
		    }
		    rs.close();
		    st.close();
			return idTabla;
		}catch(SQLException e){
			throw e;
		}
	}

    /**
     * Obtiene el url de publicacion para una capa en el caso de que ésta sea dinámica
     * @param conn
     * @param idLayer
     * @param idEntidad
     * @return
     */
	private String getUrlDynamicLayer(Connection conn, int idLayer, int idEntidad) throws SQLException{
		try{
			String url = "";
			String sSQL="select * from dynamiclayers where id=? and id_entidad=?";
		    PreparedStatement st = conn.prepareStatement(sSQL);
		    st.setInt(1, idLayer);
		    st.setInt(2, idEntidad);
		    ResultSet rs = st.executeQuery();
		    if (rs.next()){
		        url = rs.getString("url");
		        if (url == null)
		        	url = "";
		    }
		    rs.close();
		    st.close();
			return url;
		}catch(SQLException e){
			throw e;
		}
	}

	 
}