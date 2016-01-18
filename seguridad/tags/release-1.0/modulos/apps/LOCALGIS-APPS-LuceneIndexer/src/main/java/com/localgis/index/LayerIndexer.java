package com.localgis.index;


import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.ArrayList;
 
import java.util.List; 
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
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
	
	Connection geopistaConn;
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
		
	   Statement stmt=conn.createStatement( ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
	   ResultSet rs = stmt.executeQuery(sqlColumnDomains);
	    while (rs.next()) {
	    	String cd=rs.getString("layerColumnDomain");
	    	columnDomains.add(cd);
	    	numDominios++;
	    	//log.debug(id);
	    }
	    
	    stmt.close();
	    rs.close();
	    
		
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
		    
		    stmt.close();
		    rs.close();		   
		    log.info("Cacheados " + numDominios + " dominios con "+numTraducciones+" traducciones en total");
	  }catch(SQLException sqle){
		  log.error("Error recuperando dominios de columnas "+sqle.getMessage());
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
		try {
	
			//Primero obtenemos la lista de entidades supramunique tienen algún mapa publicado
    	    Statement stmt = conn.createStatement( ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
    	    ResultSet rs = stmt.executeQuery("SELECT id_municipio FROM entidades_municipios WHERE id_entidad="+idEntidad +" ORDER BY id_municipio LIMIT 1");
    	    while (rs.next()) {
    	    	String id=rs.getString("id_municipio");
    	    	municipios.add(id);
    	    	//log.debug(id);
    	    }
		}catch(SQLException sqle) {
			log.error("Error recuperando municipios entidad. "+idEntidad +" ERROR: "+sqle.getMessage());
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
		try {
			//Primero obtenemos la lista de entidades supramunique tienen algún mapa publicado
    	    Statement stmt = conn.createStatement( ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
    	    ResultSet rs = stmt.executeQuery("SELECT id_entidad FROM entidad_supramunicipal  ORDER BY id_entidad");
    	    while (rs.next()) {
    	    	String id=rs.getString("id_entidad");
    	    	entidades.add(id);
    	    	//log.debug(id);
    	    }
		}catch(SQLException sqle) {
			log.error("Error recuperando entidades. "+sqle.getMessage());
		}
		return entidades;
	}
	
	
	/** Obtiene la lista de capas publicadas para una entidad determinada
	 * 
	 * @param conn
	 * @param idEntidad Entidad de la que se buscarán las layers publicadas
	 * @return List<String> con los id_layer en geopista
	 */
	private List<String> getEntidadLayers(Connection conn, String idEntidad){
		List<String> layers = new ArrayList<String>();
		try {
    	    Statement stmt = conn.createStatement( ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
    	    ResultSet rs = stmt.executeQuery("SELECT DISTINCT l.layeridgeopista "+
                                             " FROM localgisguiaurbana.map m, localgisguiaurbana.maplayer ml, localgisguiaurbana.layer l"+
                                             " WHERE m.mapid=ml.mapid and ml.layerid=l.layerid and m.mapidentidad="+idEntidad);
    	    while (rs.next()) {
    	    	String id=rs.getString("layeridgeopista");
    	    	layers.add(id);
    	    	//log.debug(id);
    	    }
		}catch(SQLException sqle) {
			log.error("Error recuperando layers. "+sqle.getMessage());
		}
		return layers;		
	}
	
	private String getLayerQuery(Connection conn, String idLayer){
		String query = "";
		try {
    	    Statement stmt = conn.createStatement( ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
    	    ResultSet rs = stmt.executeQuery("SELECT q.selectquery "+
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
		}
		return query;		
	}	
	/** Traduce un idLayer de geopista en el idLayer en guiaUrbana
	 * 
	 */
	private String getGuiaUrbanaIdLayer(Connection conn, String geopistaIdLayer){
		String guiaUrbanaIdLayer = "";
		try {
    	    Statement stmt = conn.createStatement( ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
    	    ResultSet rs = stmt.executeQuery("SELECT l.layerid "+
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
		}
		return guiaUrbanaIdLayer;			
	}


	public void setParameters(Connection geopistaConn, String idEntidad){
		this.geopistaConn=geopistaConn;
		this.idEntidad=idEntidad;
	}
	
	public void indexLayer(Connection geopistaConn, String idEntidad, String idLayer) {
		log.info("**** Iniciando proceso de indexacion. Entidad="+idEntidad+" layer="+idLayer);
		
		if (idEntidad==null || idEntidad.length()==0)
			return;
		
		IndexWriter iwIndice = null;
		Directory indexDir = null;
        Analyzer an = null;

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
            
            Statement stm;
            ResultSetMetaData md;
            ResultSet rs;
            
            String projection=SimpleAppContext.getString("displayprojection", "4230");
            
            for (String id_municipio:municipios) {
                log.info("["+idEntidad+"]"+"Indexando identiad.municipio.layer "+idEntidad+"."+id_municipio+"."+idLayer);
            	//PreparedStatement stm = con.prepareStatement("SELECT id, referencia_catastral, id_municipio, direccion_no_estructurada, codigo_postal, x(centroid(transform(\"GEOMETRY\", 23029))) as x_centered, y(centroid(transform(\"GEOMETRY\", 23029))) as y_centered FROM parcelas");
            	selectQuery=query.replaceAll("\\?M", id_municipio);
            	selectQuery=selectQuery.replaceAll("\\?T", projection);
            	//Calcular cuál es el campo de geometría...
            	Pattern p=Pattern.compile("[\"A-Za-z_]+\\.\"GEOMETRY\"");
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
            	
            	stm.close();
            	rs.close();
            }       	
        	/*
        	
        	rsc.next();
        	long recordCount = rsc.getLong(1);
        	rsc.close();
        	
//        	long indexpoint = 0;
        	
        	long offset = 0;
        	while(offset <= recordCount) {
	        	
	        	ResultSet rs = stm.executeQuery("SELECT id, referencia_catastral, id_municipio, direccion_no_estructurada, codigo_postal, x(centroid(\"GEOMETRY\")) as x_centered, y(centroid(\"GEOMETRY\")) as y_centered FROM parcelas ORDER BY ID LIMIT " + LIMIT + " OFFSET " + offset);
	        	
	        	while(rs.next()){
	        		Document doc = new Document();
	        		
	        		String name = "";
	        		String searcheable = "";
	        		
	        		NumericField field = new NumericField("id", Field.Store.YES, false);
	        		field.setLongValue(rs.getLong("id"));
	        		doc.add(field);
	        		
	        		String aux = rs.getString("referencia_catastral");
	        		//doc.add(new Field("referencia_catastral", aux, Field.Store.NO, Field.Index.ANALYZED));
	        		name += aux;
	        		searcheable += aux;
	        		
	        		long laux = 0;
	        		//field = new NumericField("id_municipio", Field.Store.NO, true);
	        		laux = rs.getLong("id_municipio");
	        		//field.setLongValue(laux);
	        		//doc.add(field);
	        		name += "-" + laux;
	        		searcheable += " " + laux;
	        		
	        		aux = rs.getString("direccion_no_estructurada");
	        		if(aux!=null){
	        			//doc.add(new Field("direccion_no_estructurada", aux, Field.Store.NO, Field.Index.ANALYZED));
	        			searcheable += " " + aux;
	        		}
	        		
	        		laux = rs.getLong("codigo_postal");
	        		//field = new NumericField("codigo_postal", Field.Store.NO, true);
	        		//field.setLongValue(laux);
	        		//doc.add(field);
	        		searcheable += " " + laux;
	        		
	        		doc.add(new Field("searcheable", searcheable, Field.Store.NO, Field.Index.ANALYZED));
	
	        		double posx = rs.getDouble("x_centered");
	        		double posy = rs.getDouble("y_centered");
	        		
	        		field = new NumericField("pos_x", Field.Store.YES, false);
	        		field.setDoubleValue(posx);
	        		
	        		doc.add(field);
	        		field = new NumericField("pos_y", Field.Store.YES, false);
	        		field.setDoubleValue(posy);
	        		doc.add(field);
	        		        		        		
	        		doc.add(new Field("name", name, Field.Store.YES, Field.Index.NOT_ANALYZED));
	        		
//	        		doc.add(new Field("num", ""+indexpoint, Field.Store.YES, Field.Index.NOT_ANALYZED));
	        		
	        		iwIndice.addDocument(doc);
//	        		indexpoint++;
	        		//log.debug("Indexado elemento: " + name);
	        	}
	        	rs.close();
	        	offset+=LIMIT;
	        	log.debug("Elementos: " + recordCount + " Offset: " + offset);
	        }
	        */

        	
        	
        	iwIndice.close();
        	indexDir.close();
        }catch (SQLException sqle){
        	log.error("Error ejecutando consulta: "+selectQuery);
        }catch (Exception e) {
			log.error("["+idEntidad+"]"+"Error preparando indice: " + e.getMessage(), e);
			e.printStackTrace();
		}
        log.info("["+idEntidad+"]"+"**** Finalizado indexacion layer "+idLayer+"  Registros indexados: "+nregIndexados);
	}
	
	
	public  void indexEntidad(Connection conn,  String idEntidad) {
        log.info("["+idEntidad+"]"+"Indexando entidad "+idEntidad);
		List<String> layers=getEntidadLayers(conn, idEntidad);

		for (String idLayer: layers){
			if ( Util.included(layerIncludeList,idLayer) &&!Util.included(layerIgnoreList,idLayer) ) {
				String query=getLayerQuery(conn, idLayer);
				if (query==null) {
					log.error("["+idEntidad+"]"+"Query vacía para idLayer="+idLayer);
				}else {
					indexLayer(conn, idEntidad, idLayer);
				}
			}

		}		
	}	

	public void run() {

		if (this.geopistaConn!=null && this.idEntidad!=null && this.idEntidad.length()>0 ){
			log.info("["+idEntidad+"]"+"Iniciando proceso indexación entidad "+idEntidad);
			
			try {
				Connection conn=SimpleAppContext.getJDBCConnection();	
				cacheDomainNodes(conn);
				List<String> entidades;
				//Si no se ha especificado una entidad, se procesan todas
				if (idEntidad==null || idEntidad.length()==0) {
					entidades=getGeopistaEntidades(conn);
				}else {
					entidades=new ArrayList<String> ();
					entidades.add(idEntidad);

				}

				for (String idEntidad: entidades) {
					if (Util.included(entityIncludeList,idEntidad) &&!Util.included(entityIgnoreList,idEntidad)){
						setParameters(conn, idEntidad);
						indexEntidad(conn, idEntidad);

					}
				}
				conn.close();
			}catch (SQLException sqle) {
				log.error("Error: "+sqle.getMessage());
				sqle.printStackTrace();
			}
		}

	}

    
}
