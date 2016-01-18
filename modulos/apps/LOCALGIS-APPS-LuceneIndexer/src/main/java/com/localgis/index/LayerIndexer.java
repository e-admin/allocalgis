/**
 * LayerIndexer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.index;


import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.util.Version;

import com.geopista.app.SimpleAppContext;
import com.geopista.server.database.CPoolDatabase;

public class LayerIndexer  implements Runnable {

	private static Logger log = Logger.getLogger(LayerIndexer.class);
	
	private  String entityIgnoreList="";
	private  String entityIncludeList="";
	
	private  String layerIgnoreList="";
	private  String layerIncludeList="";
	
	public String indexerIdentifier="";
	
	public LayerIndexer(String entityIgnoreList, String entityIncludeList, String layerIgnoreList, String layerIncludeList  ){
		super();
		this.entityIgnoreList=entityIgnoreList;
		this.entityIncludeList=entityIncludeList;
		this.layerIgnoreList=layerIgnoreList;
		this.layerIncludeList=layerIncludeList;
			
	}
	
	String idEntidad;
	String idLayer;
	String query;
	
	/**
	 * Cache de columnas que tienen dominios asociados.
	 * Almacenados en la forma "idLayer.columnName"
	 * Puede usarse para comprobar previamente si una columna tiene dominio
	 */
	private static List<String> columnDomains;
	
	/**
	 * Cache con la traducción de los valores de las columnas con dominio
	 */
	private static Dictionary<String,String> columnDomainsValues;
	
	/**
	 * Traduce el valor almacenado en una tabla de geopista en una columna con dominio asociado
	 * @param column Columna en formato idlayer.columnName de geopista
	 * @param value Valor a traduccir
	 * @return
	 */
	private synchronized static String getDomainValue(String column, String value){
		//System.out.println("Buscando traducción para "+column+","+value);
		if (value==null)
			return "";
		String traduccion=value;
		if (columnDomains.contains(column)) {

			String t=columnDomainsValues.get(column+"."+value);
			if (t!=null && !t.equals("null")) {
				traduccion=t;
			    log.debug("Traducción encontrada para "+column+"."+value+"="+traduccion);
			}
		}

		return traduccion;
	}
	
	/**
	 * Obtiene la traducción de las columnas con dominio y lo almacena
	 * en los atributos columnDomains y columnDomainsValues.
	 * Las caches son compartidas por los indexadores concurrentes, así que si ya han sido inicializadas 
	 * por el primero, no se vuelve a inicializar
	 */
	private static void cacheDomainNodes(Connection conn){
		if (columnDomains!=null )
			return;
		log.info("Cacheando dominios");
		columnDomains=new ArrayList<String>(800);
		columnDomainsValues=new Hashtable<String, String>();
		Statement stmt=null;
		ResultSet rs = null;
	  try {
		  int numDominios=0;
		  int numTraducciones=0;
		//Primero se recupera la lista de columnas que tienen dominio asociado
		String sqlColumnDomains="SELECT DISTINCT layers.id_layer,layers.name as layerName,columns.name as columnName , layers.id_layer || '.' || columns.name as layerColumnDomain "
			                   +" FROM layers JOIN attributes ON (attributes.id_layer=layers.id_layer)"
			                   +"      JOIN columns ON (attributes.id_column=columns.id)"
			                   +"      JOIN domainnodes ON (columns.id_domain=domainnodes.id_domain)"
			                   +"       LEFT OUTER JOIN dictionary ON (domainnodes.id_description=dictionary.id_vocablo) "
			                   +" WHERE (domainnodes.id_municipio is null) and (dictionary.locale='es_ES' or dictionary.locale is null) and length(domainnodes.pattern)>0"
			                   +" order by layers.id_layer;";
		
	   stmt=conn.createStatement( ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
	   rs = stmt.executeQuery(sqlColumnDomains);
	    while (rs.next()) {
	    	String cd=rs.getString("layerColumnDomain");
	    	columnDomains.add(cd);
	    	numDominios++;
	    	//log.debug(id);
	    }
		
		//Ahora recuperamos los valores asociados
		String sqlColumnDomainsValues="SELECT DISTINCT layers.id_layer,layers.name as layerName,columns.name as columnName , layers.id_layer || '.' || columns.name as layerColumnDomain "
			+" , layers.id_layer || '.' || columns.name || '.' || domainnodes.pattern as layerColumnDomainPattern"
			+" ,domainnodes.pattern as pattern,dictionary.traduccion as layerColumnDomainPatternTraduccion "
            +" FROM layers JOIN attributes ON (attributes.id_layer=layers.id_layer)"
            +"      JOIN columns ON (attributes.id_column=columns.id)"
            +"      JOIN domainnodes ON (columns.id_domain=domainnodes.id_domain)"
            +"       LEFT OUTER JOIN dictionary ON (domainnodes.id_description=dictionary.id_vocablo) "
            +" WHERE (domainnodes.id_municipio is null) and (dictionary.locale='es_ES' or dictionary.locale is null) and length(domainnodes.pattern)>0"
            +" order by layers.id_layer;";
		
		   stmt=conn.createStatement( ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
		    rs = stmt.executeQuery(sqlColumnDomainsValues);
		    
		    while (rs.next()) {
		    	String pattern=rs.getString("layerColumnDomainPattern");
		    	String value=rs.getString("layerColumnDomainPatternTraduccion");
		    	if (pattern!=null && value!=null){
		    		columnDomainsValues.put(pattern,value);
		    		numTraducciones++;
		    	}
		    	//log.debug(id);
		    }
 
		    log.info("Cacheados " + numDominios + " dominios con "+numTraducciones+" traducciones en total");
	  }catch(SQLException sqle){
		  log.error("Error recuperando dominios de columnas "+sqle.getMessage());
		} finally {
			safeClose(rs, stmt, null);
		}
		
		
	}
	
	/**
	 * Retorna todos los id_municipio de una entidad supramunicipal dada
	 * @param conn
	 * @param idEntidad
	 * @return Lista con los id de las entidades en geopista
	 */
	private List<String> getGeopistaMunicipios(Connection conn, String idEntidad){
		List<String> municipios = new ArrayList<String>();
		Statement stmt=null;
		ResultSet rs = null;
		try {
	
			//Primero obtenemos la lista de entidades supramunique tienen algún mapa publicado
    	    stmt = conn.createStatement( ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
    	    rs = stmt.executeQuery("SELECT id_municipio FROM entidades_municipios WHERE id_entidad="+idEntidad +" ORDER BY id_municipio LIMIT 1");
    	    while (rs.next()) {
    	    	String id=rs.getString("id_municipio");
    	    	municipios.add(id);
    	    	//log.debug(id);
    	    }
		}catch(SQLException sqle) {
			log.error("Error recuperando municipios entidad. "+idEntidad +" ERROR: "+sqle.getMessage());
		} finally {
			safeClose(rs, stmt, null);
		}
		return municipios;
	}	
	
	/**
	 * Retorna todas las entidades supramunicipales en geopista
	 * @param conn
	 * @return Lista con los id de las entidades en geopista
	 */
	public static List<String> getGeopistaEntidades(Connection conn){
		List<String> entidades = new ArrayList<String>();
		Statement stmt=null;
		ResultSet rs = null;		
		try {
			//Primero obtenemos la lista de entidades supramunique tienen algún mapa publicado
    	    stmt = conn.createStatement( ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
    	    rs = stmt.executeQuery("SELECT id_entidad FROM entidad_supramunicipal  ORDER BY id_entidad");
    	    while (rs.next()) {
    	    	String id=rs.getString("id_entidad");
    	    	entidades.add(id);
    	    	//log.debug(id);
    	    }
		}catch(SQLException sqle) {
			log.error("Error recuperando entidades. "+sqle.getMessage());
		} finally {
			safeClose(rs, stmt, null);
		}
		return entidades;
	}
	
	
	/** Obtiene la lista de capas publicadas para una entidad determinada
	 * 
	 * @param conn
	 * @param idEntidad Entidad de la que se buscarán las layers publicadas
	 * @return List<String> con los id_layer en geopista
	 */
	private Hashtable<String, Integer> getEntidadLayers(Connection conn, String idEntidad){
		Hashtable<String, Integer> layers = new Hashtable<String, Integer>();
		Statement stmt=null;
		ResultSet rs = null;			
		try {
    	    stmt = conn.createStatement( ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
    	    rs = stmt.executeQuery("SELECT DISTINCT l.layeridgeopista, la.versionable  "+
                                             " FROM localgisguiaurbana.map m, localgisguiaurbana.maplayer ml, localgisguiaurbana.layer l, public.layers la "+
                                             " WHERE m.mapid=ml.mapid and ml.layerid=l.layerid and la.id_layer=l.layeridgeopista  and m.mapidentidad="+idEntidad);
    	    while (rs.next()) {
    	    	String id=rs.getString("layeridgeopista");
    	    	int versionable = rs.getInt("versionable");
    	    	layers.put(id, versionable);
    	    	//log.debug(id);
    	    }
		}catch(SQLException sqle) {
			log.error("Error recuperando layers. "+sqle.getMessage());
		} finally {
			safeClose(rs, stmt, null);
		}
		return layers;		
	}
	
	private String getLayerQuery(Connection conn, String idLayer){
		String query = "";
		Statement stmt=null;
		ResultSet rs = null;	
		try {
    	    stmt = conn.createStatement( ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
    	   rs = stmt.executeQuery("SELECT q.selectquery "+
                                             " FROM queries q"+
                                             " WHERE q.id_layer="+idLayer+
                                             " LIMIT 1");
    	    while (rs.next()) {
    	    	String sql=rs.getString("selectquery");
    	    	query=sql;
    	    	//log.debug(sql);
    	    }
		}catch(SQLException sqle) {
			log.error("Error recuperando queries. "+sqle.getMessage());
		} finally {
			safeClose(rs, stmt, null);
		}
		return query;		
	}	
	/** Traduce un idLayer de geopista en el idLayer en guiaUrbana
	 * 
	 */
	private String getGuiaUrbanaIdLayer(Connection conn, String geopistaIdLayer){
		String guiaUrbanaIdLayer = "";
		Statement stmt=null;
		ResultSet rs = null;
		try {
    	    stmt = conn.createStatement( ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
    	    rs = stmt.executeQuery("SELECT l.layerid "+
                                             " FROM localgisguiaurbana.layer l"+
                                             " WHERE l.layeridgeopista="+geopistaIdLayer+
                                             " LIMIT 1");
    	    while (rs.next()) {
    	    	String sql=rs.getString("layerid");
    	    	guiaUrbanaIdLayer=sql;
    	    	//log.debug(sql);
    	    }
		}catch(SQLException sqle) {
			log.error("Error recuperando id_layer en guiaUrbana. "+sqle.getMessage());
		} finally {
			safeClose(rs, stmt, null);
		}
		return guiaUrbanaIdLayer;			
	}


	public void setParameters(String idEntidad){
		this.idEntidad=idEntidad;
	}
	
	public void indexLayer(Connection geopistaConn, String idEntidad, String idLayer, int versionable) {
		log.info("**** Iniciando proceso de indexacion. Entidad="+idEntidad+" layer="+idLayer);
		
		if (idEntidad==null || idEntidad.length()==0)
			return;
		
		IndexWriter iwIndice = null;
		Directory indexDir = null;
        Analyzer an = null;
        Statement stm = null;
        ResultSet rs = null;
        String selectQuery="";
        int nregIndexados=0;
        try {

        	query=getLayerQuery(geopistaConn, idLayer);
        	//Esto se podría mejorar generando distintos indices por tabla e id_entidad
        	//de esta manera cada uno se lanzaría en un hilo, o teniendo un pool de hilos
        	String guiaUrbanaLayerId=getGuiaUrbanaIdLayer(geopistaConn, idLayer);

        	indexDir = new NIOFSDirectory(new File(SimpleAppContext.getString("index.rootDir")+"/"+idEntidad+"/"+idLayer));
        	
        	log.info("["+idEntidad+"]"+"Creando indice en "+SimpleAppContext.getString("index.rootDir")+"/"+idEntidad+"/"+idLayer);
			log.debug("SQL Original: "+query);
        	an = new StandardAnalyzer(Version.LUCENE_35);
        	
        	//an = new SimpleAnalyzer(Version.LUCENE_35);
        	
        	IndexWriterConfig iwc=new IndexWriterConfig(Version.LUCENE_35, an);
        	iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        	
            iwIndice = new IndexWriter(indexDir,iwc);
            
            List<String> municipios=getGeopistaMunicipios(geopistaConn, idEntidad);
                       
            ResultSetMetaData md;
            
            String projection=SimpleAppContext.getString("displayprojection", "4258");
            
            for (String id_municipio:municipios) {
                log.info("["+idEntidad+"]"+"Indexando identidad.municipio.layer "+idEntidad+"."+id_municipio+"."+idLayer);
            	//PreparedStatement stm = con.prepareStatement("SELECT id, referencia_catastral, id_municipio, direccion_no_estructurada, codigo_postal, x(centroid(transform(\"GEOMETRY\", 23029))) as x_centered, y(centroid(transform(\"GEOMETRY\", 23029))) as y_centered FROM parcelas");
            	selectQuery=query.replaceAll("\\?M", id_municipio);
            	selectQuery=selectQuery.replaceAll("\\?T", projection);
            	//Calcular cuál es el campo de geometría...
            	Pattern p=Pattern.compile("[\"A-Za-z_]+\\.\"GEOMETRY\"");
            	if (versionable == 1)
            		selectQuery += " AND revision_expirada = 9999999999";
            	Matcher m=p.matcher(selectQuery);
            	String patron="";
            	//..para añadir los campos calculados de los centroides x y
            	if (m.find()){
            		patron=selectQuery.substring(m.start(), m.end());               	
            	}
            	else patron="\"GEOMETRY\"";
            	//projection="4326";
            	selectQuery=selectQuery.replaceAll("FROM",", x(centroid(transform("+patron+","+ projection +"))) as x_centered, y(centroid(transform("+patron+","+projection+"))) as y_centered FROM ");
            	//String addT="\\' +proj=utm +ellps=intl +zone=30 +towgs84=-92.9,-104.2,-120.0,0,0,0,0 +units=m +no_defs\', \' +proj=longlat +ellps=WGS84 +datum=WGS84 +no_defs\'";
            	
            	//selectQuery=selectQuery.replaceAll("FROM",", x(centroid(transform_geometry("+patron+","+addT+","+ projection +"))) as x_centered, y(centroid(transform_geometry("+patron+","+addT+","+projection+"))) as y_centered FROM ");
            	 
            	

            	 stm = geopistaConn.createStatement(
            			ResultSet.TYPE_FORWARD_ONLY,
            			ResultSet.CONCUR_READ_ONLY
            	);

            	stm.setFetchSize(10);
            	log.debug("Select query: "+selectQuery);
            	rs = stm.executeQuery(selectQuery);
            	md = rs.getMetaData() ;

            	// Print the column labels
            	int nAtributos=md.getColumnCount();

            	//for( int i = 1; i <=nAtributos ; i++ )
            	//   log.debug( md.getColumnLabel(i) + " " ) ;

            	while (rs.next()){
            		Document doc = new Document();

            		String name = "";
            		String searcheable = "";
            		String value;
            		String column="";
            		//Puesto que las búsquedas se realizan siempre sobre el campo searcheable, 
            		//solo vamos a crear los campos name y searcheable
            		//Field field;
            		for( int i = 1; i <= nAtributos; i++ ) {
            			column=md.getColumnLabel(i);
            			//if (!column.equals("x_centered") && !column.equals("y_centered") && !column.equals("id") && !column.equals("codprov") && !column.equals("codmunic")&& !column.equals("codentidad")&& !column.equals("codpoblamiento")	){
            			if (!column.equals("codprov") && !column.equals("codmunic")&& !column.equals("codentidad")&& !column.equals("codpoblamiento")	){
            				value=rs.getString(i);

            				if (!rs.wasNull() && !column.equalsIgnoreCase("GEOMETRY")){
            					//System.out.println("column,value"+column+","+value);
            					if (value!=null){
            						//Buscamos la traducción por si es una columna con dominio
            						value=LayerIndexer.getDomainValue(idLayer+"."+column,value);
            						// field = new Field(column, value,Field.Store.YES, Field.Index.ANALYZED);
            					}
            					//else field = new Field(column, value,Field.Store.YES, Field.Index.ANALYZED);

            					//SUGERENCIA: Contemplar Dominios de columnas
            					//Idea cargar dominios en hashmap
            					searcheable=searcheable+" "+column.substring(0, Math.min(7,md.getColumnLabel(i).length()))+" "+value.toUpperCase()+" ";
            					//doc.add(field);
            				}
            			}
            		}
            		         		
            		
                   searcheable=searcheable.replaceAll(",", "");
                   searcheable=searcheable.replaceAll(";", "");
            		
            		doc.add(new Field("searcheable", searcheable, Field.Store.YES, Field.Index.ANALYZED));
            		
            		NumericField fieldx= new NumericField("pos_x", Field.Store.YES, false);
	        		fieldx.setDoubleValue(rs.getDouble("x_centered"));       		
	        		doc.add(fieldx);
	        		
	        		NumericField fieldy = new NumericField("pos_y", Field.Store.YES, false);
	        		fieldy.setDoubleValue(rs.getDouble("y_centered"));
	        		doc.add(fieldy);
	        		
            		//El "nombre" del elemento es complejo de calcular sin proporcionar información de configuración..
            		//Así que utilizaremos los primeros caracteres del registro	        		
            		//name=searcheable.substring(0, Math.min(60,searcheable.length()));
            		
            		
	        		
            		doc.add(new Field("name", name, Field.Store.YES, Field.Index.NOT_ANALYZED));	        		
            		
        			iwIndice.addDocument(doc);       			
        			nregIndexados++;
            		

            	}

            }       	      	
        	
        	iwIndice.close();
        	indexDir.close();
        }catch (SQLException sqle){
        	log.error("Error ejecutando consulta: "+selectQuery);
        }catch (Exception e) {
			log.error("["+idEntidad+"]"+"Error preparando indice: " + e.getMessage(), e);
			e.printStackTrace();
		} finally {
			safeClose(rs, stm, null);
		}
        log.info("["+idEntidad+"]"+"**** Finalizado indexacion layer "+idLayer+"  Registros indexados: "+nregIndexados);
	}
	
	
	public  void indexEntidad(Connection conn,  String idEntidad) {
        log.info("["+idEntidad+"]"+"Indexando entidad "+idEntidad);
		Hashtable<String, Integer> layers=getEntidadLayers(conn, idEntidad);
		Enumeration<String> e = layers.keys();
		while(e.hasMoreElements()){
			String idLayer = e.nextElement();
			int versionable = 0;
			if (layers.get(idLayer) != null)
				versionable = layers.get(idLayer);
				
			if ( Util.included(layerIncludeList,idLayer) &&!Util.included(layerIgnoreList,idLayer) ) {
				String query=getLayerQuery(conn, idLayer);
				if (query==null) {
					log.error("["+idEntidad+"]"+"Query vacía para idLayer="+idLayer);
				}else {
					indexLayer(conn, idEntidad, idLayer, versionable);
				}
			}

		}		
	}	

	public void run() {
		Connection conn=null;
		int typeOfConnection = 0;
			
		try {
			//conn=SimpleAppContext.getJDBCConnection();	
			
			try {
				conn = CPoolDatabase.getSimpleConnection();
			} catch (Exception e) {
			}
			if (conn == null) {
				conn = SimpleAppContext.getJDBCConnection();
				typeOfConnection = 1;
			}
			
			cacheDomainNodes(conn);
			List<String> entidades;
			//Si no se ha especificado una entidad, se procesan todas
			if (idEntidad==null || idEntidad.length()==0) {
				entidades=getGeopistaEntidades(conn);
			}else {
				entidades=new ArrayList<String> ();
				entidades.add(idEntidad);

			}
			log.info("["+idEntidad+"]"+"Iniciando proceso indexación entidad:"+idEntidad);

			for (String idEntidad: entidades) {
				if (Util.included(entityIncludeList,idEntidad) &&!Util.included(entityIgnoreList,idEntidad)){
					setParameters(idEntidad);
					indexEntidad(conn, idEntidad);

				}
			}
			conn.close();
		}catch (SQLException sqle) {
			log.error("Error: "+sqle.getMessage());
			sqle.printStackTrace();
		} finally {
			safeClose(null, null, conn);
			if (typeOfConnection==0)
				CPoolDatabase.releaseConexion();
		}


	}

	public static boolean safeClose(ResultSet rs, Statement statement,
			Connection connection) {

//		try {
//			connection.commit();
//		} catch (Exception ex2) {
//		}
		try {
			if (rs != null)
				rs.close();
		} catch (Exception ex2) {
		}
		try {
			if (statement != null)
				statement.close();
		} catch (Exception ex2) {
		}
		try {
			connection.close();

		} catch (Exception ex2) {
		}

		return true;
	}
}
