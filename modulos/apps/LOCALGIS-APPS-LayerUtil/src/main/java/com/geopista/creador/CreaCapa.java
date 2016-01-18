/**
 * CreaCapa.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.creador;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import com.geopista.app.AppContext;
import com.geopista.app.layerutil.dbtable.ColumnDB;
import com.geopista.app.layerutil.dbtable.TablesDBOperations;
import com.geopista.app.layerutil.exception.DataException;
import com.geopista.app.layerutil.layer.ForeignCondition;
import com.geopista.app.layerutil.layer.LayerOperations;
import com.geopista.app.layerutil.layer.LayerTable;
import com.geopista.app.layerutil.layer.Query;
import com.geopista.app.layerutil.layer.TableFrom;
import com.geopista.app.layerutil.layerfamily.LayerFamilyOperations;
import com.geopista.app.layerutil.layerfamily.LayerFamilyTable;
import com.geopista.app.layerutil.schema.column.ColumnRow;
import com.geopista.editor.GeopistaEditor;
import com.geopista.feature.Attribute;
import com.geopista.feature.Column;
import com.geopista.feature.Domain;
import com.geopista.feature.Table;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.LayerFamily;
import com.geopista.protocol.administrador.Acl;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.workbench.model.Layer;

public class CreaCapa {
	/*
	 * Esta variable la utilizamos para rellenar las equivalencias entre los nombres de las columnas y los atributos:
	 * */
	public Hashtable columnasAtributos = new Hashtable();
		
    public static final int SELECT = 0;
    public static final int UPDATE = 1;
    public static final int INSERT = 2;
    public static final int DELETE = 3;  	
	
	public static final String IDIOMA_ES = "es_ES";
	
	public static final String DOMINIO_ID_NOMBRE = "Autonumérico incremental";
	public static final int DOMINIO_ID_ID = 10067;
	public static final String COLUMNA_ID_NOMBRE = "id";
	public static final int DOMINIO_ID_LEVEL = 0;
	
	public static final String DOMINIO_IDMUNICIPIO_NOMBRE = "Municipio obligatorio";
	public static final int DOMINIO_IDMUNICIPIO_ID = 10069;
	public static final String COLUMNA_IDMUNICIPIO_NOMBRE = "id_municipio";
	public static final int DOMINIO_IDMUNICIPIO_LEVEL = 0;
	
	public static final String COL_NUMERIC = "NUMERIC";
	public static final String COL_VARCHAR = "VARCHAR";
	public static final String COL_GEOMETRY = "GEOMETRY";
	
	public static final int TIPO_BBDD = 1;
	
	public static final String CODIGO_ACL = "34";
	
	/**
	 * En esta variable guardamos la ruta del fichero donde indicamos los caracteres que queremos cambiar por otros para no tener
	 * problemas a la hora de crear las columnas en la BBDD. Por ejemplo: á -> a   
	 *
	 */
	public static final String FICHERO_CONFIG = "config\\cambioCaracteres.txt";

	/**
	 * Constructor por defecto
	 * 
	 */	
	public CreaCapa() {

	}	

    /**
     * Hace todo lo necesario para crear la capa
     * @param newTable Table La tabla creada previamente
     * @param nombreCapa String El nombre de la capa que queremos crear
     * @param idLayerFamily int El identificador de la familia a la que va a pertenecer la capa
     * @param miliseg 
     */     
    public void crearCapa(AppContext aplicacion, Table newTable, String nombreCapa, int idLayerFamily, String digitosControl, long miliseg){
    	LayerOperations operacionesLayer = new LayerOperations();
    	Hashtable htNombres = new Hashtable();
    	
    	try {
    		//1. Guardamos en la tabla dictionary el nombre de la capa para los distintos idiomas:
    		htNombres.put(IDIOMA_ES, nombreCapa);
    		int idDictionary = operacionesLayer.actualizarDictionary (htNombres, 0);
    		
			//2. Guardamos la capa en la tabla layers:
    		Layer nuevaCapa = new Layer();
			if (idDictionary >0)
                nuevaCapa.setName(String.valueOf(idDictionary));
			
			nuevaCapa.setDescription(nombreCapa);
            LayerTable lt = new LayerTable(0, nuevaCapa);

            Acl acl = new Acl();
            
           // Asignamos el codigo ACL:
            acl.setId(CODIGO_ACL);
            
            lt.setAcl(acl);
            lt.setLayer(nuevaCapa);
            int idLayer = operacionesLayer.actualizarLayer (lt, null);
            
            // 3. Guardamos relacion con layerfamily en tabla layerfamiles_layers_relations:
            operacionesLayer.asociarLayerFamilyLayer(idLayerFamily, idLayer);
            
            // 4. Guardamos nombres de atributos en tabla dictionary y en la tabla Attributes:
            String tablaSecuencia = insertaAtributos(newTable, idLayer);

            // 5. Guardamos consultas SQL en la tabla queries:
            
            generarSQL(newTable, idLayer);
            
            // 6. Creamos una nueva secuencia SQL para las inserciones secuenciales de features en la nueva capa:
            if (digitosControl.equals("RECREATE_AND_INSERT_DATA")){
            	tablaSecuencia = tablaSecuencia.trim();
            	String nuevoNombreSecuencia = tablaSecuencia.toLowerCase()+"_"+miliseg;
            	operacionesLayer.actualizarSecuencia(nuevoNombreSecuencia, tablaSecuencia);
            	borrarSecuencia(aplicacion, tablaSecuencia);
            }
            operacionesLayer.crearSecuencia(tablaSecuencia);
            
		} catch (DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    private void borrarSecuencia(AppContext aplicacion, String tablaSecuencia) {
		ResultSet rs = null;
		Connection directConn;
		try {
			directConn = aplicacion.getJDBCConnection("geopista", "geopista");
			String query = "DROP SEQUENCE seq_"+tablaSecuencia+";";
			PreparedStatement ps = directConn.prepareStatement(query);
			ps.executeUpdate(query);
		    directConn.close(); 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  		
	}

	/**
     * Genera las consultas SQL y las inserta en la BBDD
     * @param newTable Table La tabla creada previamente
     * @param idLayer id El identificador de la capa
     */       
    private void generarSQL(Table newTable, int idLayer){
   
    	StringBuffer select = new StringBuffer();
    	StringBuffer update = new StringBuffer();
    	StringBuffer delete = new StringBuffer();
    	StringBuffer insert = new StringBuffer();
        LayerOperations operaciones = new LayerOperations();
        List columnas;
        
    	select.append("SELECT ");
    	
    	update.append("UPDATE \"")
    		.append(newTable.getName())
    		.append("\" SET ");
    	
    	delete.append("DELETE FROM \"")
    		.append(newTable.getName())
    		.append("\" WHERE \"id\"=?");
    	
    	insert.append("INSERT INTO \"")
    	    .append(newTable.getName())
    		.append("\" (");   	
    
		try {
			columnas = operaciones.obtenerListaColumnas(newTable);

	        Column columna;
	
	        int i = 0;
	        int id = 0;
	        String valoresInsert[] = new String[columnas.size()];
	        Iterator it = columnas.iterator();
	        
	        while (it.hasNext()){
	        	i = i+1;
	    		columna =(Column) it.next();    
	            select.append("\"")
	            	.append(newTable.getName())
	            	.append("\".\"")
	            	.append(columna.getName())
	            	.append("\"");
	            if (it.hasNext())
	            	select.append(",");
	            
	            update.append("\"");
            	if (!columna.getName().equals("GEOMETRY")){
            		update.append(columna.getName())
            			.append("\"=?")
            			.append(i);
            		if (columna.getName().equals("id"))
            			id = i;
            	}
            	else{
            		update.append(columna.getName())
            			.append("\"=GeometryFromText(?")
            			.append(i)
            			.append(",?S)");
            	}           	
	            if (it.hasNext())
	            	update.append(",");
	            
	            insert.append("\"")
            		.append(columna.getName())
            		.append("\"");
	            if (it.hasNext())
	            	insert.append(",");
	            
	            if (columna.getName().equals("GEOMETRY")){
	            	valoresInsert[i-1] = "GeometryFromText(?"+i+",?S)";
	            }
	            else{
	            	if (columna.getName().equals("id")){
	            		valoresInsert[i-1] = "?PK";
	            	}
	            	else{
	            		if (columna.getName().equals("id_municipio")){
		            		valoresInsert[i-1] = "?M";
		            	}
	            		else{
	            			valoresInsert[i-1] = "?"+i;
	            		}
	            	}
	            }
	            	
	        }

	        select.append(" FROM ")
	        	.append("\"")
	        	.append(newTable.getName())
	        	.append("\"");
	        
	        select.append(" WHERE ")
	        	.append("\"")
	        	.append(newTable.getName())
	        	.append("\".\"id_municipio\"=?M");
	        
	        update.append(" WHERE ")
	        	.append("\"id\"=?")
	        	.append(id);
	        
	        delete.append(id);
	        
        	insert.append(") VALUES(");
	        for (int j=1; j<=i; j++){
	        	insert.append(valoresInsert[j-1]);
	            if (j!=i)
	            	insert.append(",");
	        }
	        
	        insert.append(")");
	        Hashtable htQueries = new Hashtable();
	        
	        Query query = new Query();
	        query.setIdLayer(idLayer);
	        query.setDeleteQuery(delete.toString());
	        query.setInsertQuery(insert.toString());
	        query.setSelectQuery(select.toString());
	        query.setUpdateQuery(update.toString());
	        htQueries.put(TIPO_BBDD, query);
			operaciones.actualizarConsultas(idLayer, htQueries);
		} catch (DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * Crea la familia de la capa de nombre familia, si la familia ya existiese no la vuelve a crear
     * @param familia String El nombre de la familia que queremos crear
     * @return el identificador de la familia. 
     */    
    public String crearFamilia(String familia) {
    	LayerFamilyOperations operacionesFamilia = new LayerFamilyOperations();
    	LayerFamilyTable newLayerFamily = new LayerFamilyTable();
    	Boolean encontrado = Boolean.FALSE;

		String id = null;
    	int i = 0;

    	
    	try {    	
    		LayerFamily[] familias = operacionesFamilia.obtenerLayerFamilies();
        	int tamanio = familias.length; 	
	    	while ((i<tamanio) && !encontrado){
	    		i = i+1;
				if (familias[i-1].getDescription().equals(familia)){
					encontrado = Boolean.TRUE;
					id = familias[i-1].getSystemId();
				}
			}
	    	if (!encontrado){
	    	
		    	Hashtable htNombres = new Hashtable();
		    	htNombres.put(IDIOMA_ES, familia);    	
		    	newLayerFamily.setHtNombre(htNombres);
		    	
		    	Hashtable htDescripcion = new Hashtable();
		    	htDescripcion.put(IDIOMA_ES, familia);     	
		    	newLayerFamily.setHtDescripcion(htDescripcion);
	
				operacionesFamilia.crearLayerFamily(newLayerFamily);
				
				// Obtenemos el idLayerFamily para utilizarlo despues al crear la capa
				familias = operacionesFamilia.obtenerLayerFamilies();
				for (int j = 0; j < familias.length; j++){
					if (familias[j].getDescription().equals(familia))
						id = familias[j].getSystemId();
				}
	    	}

		} catch (DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return id;
	}
    
    
    public Integer devuelveGeometria(AppContext aplicacion, String sSHPFilePath){
    	Integer geometria = 0;
    	
		GeopistaLayer myOriginalLayer=null;
		GeopistaEditor geopistaEditor1 = new GeopistaEditor(aplicacion.getString("fichero.vacio"));

		
		try {
			myOriginalLayer = (GeopistaLayer) geopistaEditor1.loadData(sSHPFilePath, "");
			FeatureSchema fileSchema = myOriginalLayer.getFeatureCollectionWrapper().getFeatureSchema();
			List features = myOriginalLayer.getFeatureCollectionWrapper().getFeatures();
			Feature feature = (Feature) features.get(0);
			
			String nombreGeometria = feature.getAttribute("GEOMETRY").toString();
			

			if (nombreGeometria.contains("MULTIPOINT")){
				geometria = Table.MULTIPOINT;
			}
			else{
				if (nombreGeometria.contains("POINT")){
					geometria = Table.POINT;
				}				
				else{
/*					if (nombreGeometria.contains("MULTILINESTRING")){
						geometria = Table.MULTILINESTRING;
					}
					else{					
						if (nombreGeometria.contains("MULTIPOLYGON")){
							geometria = Table.MULTIPOLYGON;
						}
						else{
*/					
						// Se le asigna a geometria MULTILINESTRING en vez de LINESTRING por si el SHP tiene alguna MULTILINESTRING
						// para que no falle la carga del SHP. Como consecuencia, en la tabla de la BBDD se almacenan todas las
						// entidades de esa capa con geometria MULTILINESTRING (NO SABEMOS QUE REPERCUSIÓN PUEDE TENER ESTO). Con
						// LA GEOMETRIA DE POLYGON PASA LO MISMO.
							if (nombreGeometria.contains("LINESTRING")){
								geometria = Table.MULTILINESTRING;
							}
							else{						
								if (nombreGeometria.contains("POLYGON")){
									geometria = Table.MULTIPOLYGON;
								}							
								else{
									geometria = Table.GEOMETRY;
								}
							}
//						}
//					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		geopistaEditor1.destroy();
		return geometria;
    }
    
    /**
     * Crea la tabla newTable en la BBDD:
     * @param newTable Table La tabla que queremos insertar
     * @param tipoGeometry String El tipo de geometria.
     * @return TRUE si la tabla ya existe FALSE si la tabla no existe
     */
	public Boolean creaTabla(Table newTable){

		Boolean encontrado = Boolean.FALSE;
		try {			
			TablesDBOperations operaciones = new TablesDBOperations();

			List tablas = operaciones.obtenerListaTablasBD();
			Iterator it = tablas.iterator();
			
			// Comprobamos que no exista la tabla:
			while (it.hasNext() && !encontrado){
				Table tabla = (Table) it.next();
				if (tabla.getDescription().equals(newTable.getDescription())){
					encontrado = Boolean.TRUE;
				}	        
			}
			if ((!encontrado) && operaciones.crearTablaBD(newTable)){
				
				//Creamos por defecto la columna id_municipio, id y GEOMETRY
				
				creaIdColRow(newTable);
				creaIdMunicipioColRow(newTable);

				if (!(newTable.getGeometryType()<0)){
					creaIdGeometryColRow(newTable);
				} 
            }
		}
		catch (DataException e){
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return encontrado;
    }
    
    /**
     * Añade la columna 'id' en la tabla, esta es una columna que se tiene que crear obligatoriamente al crear la capa
     * @param newTable Table La tabla creada previamente
     */     
    private void creaIdColRow(Table newTable){
    	ColumnRow idColRow  = new ColumnRow();
		ColumnDB idDB = new ColumnDB();
		idDB.setName("id");
		idDB.setLength(8);
		idDB.setType(COL_NUMERIC);
		idDB.setDefaultValue("");
		idDB.setDescription("");
		idDB.setNotNull(true);
		idDB.setUnique(true);
		idColRow.setColumnaBD(idDB);
		Column idColSis = new Column("id", "id", null);
		idColRow.setColumnaSistema(idColSis);
		try {
			TablesDBOperations operaciones = new TablesDBOperations();
			operaciones.crearColumnaBD(newTable, idColRow);
			
			// Asociamos el dominio:
			asociarDominio(newTable, COLUMNA_ID_NOMBRE, DOMINIO_ID_ID, DOMINIO_ID_NOMBRE, DOMINIO_ID_LEVEL);
	
		} catch (DataException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Añade la columna 'id_municipio' en la tabla, esta es una columna que se tiene que crear obligatoriamente al crear la capa
     * @param newTable Table La tabla creada previamente
     */    
    private void creaIdMunicipioColRow(Table newTable){
		ColumnRow idMunicipioColRow  = new ColumnRow();
		ColumnDB idMunicipioDB = new ColumnDB();
		idMunicipioDB.setName("id_municipio");
		idMunicipioDB.setLength(5);
		idMunicipioDB.setType(COL_NUMERIC);
		idMunicipioDB.setDefaultValue("");
		idMunicipioDB.setNotNull(true);
		idMunicipioDB.setDescription("");
		idMunicipioColRow.setColumnaBD(idMunicipioDB);
		Column idMunicipioColSis = new Column("id_municipio", "id_municipio", null);
		idMunicipioColRow.setColumnaSistema(idMunicipioColSis);
		try {
			TablesDBOperations operaciones = new TablesDBOperations();
			operaciones.crearColumnaBD(newTable, idMunicipioColRow);
			
			// Asociamos el dominio:
			asociarDominio(newTable, COLUMNA_IDMUNICIPIO_NOMBRE, DOMINIO_IDMUNICIPIO_ID, DOMINIO_IDMUNICIPIO_NOMBRE, DOMINIO_IDMUNICIPIO_LEVEL);
			
		} catch (DataException e) {
			e.printStackTrace();
		}    	
    }
    
    /**
     * Añade la columna 'GEOMETRY' en la tabla, esta es una columna que se tiene que crear obligatoriamente al crear la capa
     * @param newTable Table La tabla creada previamente
     */   
    private void creaIdGeometryColRow(Table newTable){
		ColumnRow idGeometryColRow  = new ColumnRow();
		ColumnDB idGeometryDB = new ColumnDB();
		idGeometryDB.setName("GEOMETRY");
		idGeometryDB.setType(COL_GEOMETRY);
		idGeometryDB.setDefaultValue("");
		idGeometryDB.setDescription("");
		idGeometryColRow.setColumnaBD(idGeometryDB);
		Column idGeometryColSis = new Column("GEOMETRY", "GEOMETRY", null);
		idGeometryColRow.setColumnaSistema(idGeometryColSis);
		try {
			TablesDBOperations operaciones = new TablesDBOperations();
			operaciones.crearColumnaBD(newTable, idGeometryColRow);
		} catch (DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   	
    }
    
    /**
     * Hacemos la asociacion de las columnas con sus dominios
     * @param newTable Table La tabla creada previamente
     * @param nombreColumna String El nombre de la columna
     * @param idDominio int El identificador del dominio
     * @param nombreDominio String El nombre del dominio
     * @param level int Valor del atributo level de un dominio de tipo TreeDomain, por defecto ponemos 0
     */   
    private void asociarDominio(Table newTable, String nombreColumna, int idDominio, String nombreDominio, int level){
    	LayerOperations operacionesLayer = new LayerOperations();
    	TablesDBOperations operaciones = new TablesDBOperations();
    	
		try {
			HashMap hash = operaciones.obtenerListaColumnasSistema(newTable.getIdTabla());
			Column columna = new Column();
			columna = (Column) hash.get(nombreColumna);	
			
	    	Domain dominio= operacionesLayer.obtenerDominioTipo(idDominio, nombreDominio);
			operacionesLayer.actualizarDominioColumna(columna, dominio, level);
		} catch (DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
    
    /**
     * Sacamos los atributos de los SHP y los insertamos en la BBDD y también rellenamos el Hashtable columnasAtributos
     * @param newTable Tabla en la que insertamos las columnas 
     * @param sSHPFilePath Nombre y ruta del fichero shp 
     * 
     */
    public void insertaColumnas(Table newTable, String sSHPFilePath, AppContext aplicacion){   	
    	
		GeopistaLayer myOriginalLayer=null;
		GeopistaEditor geopistaEditor1 = new GeopistaEditor(aplicacion.getString("fichero.vacio"));
	        
		try{	
		
			myOriginalLayer = (GeopistaLayer) geopistaEditor1.loadData(sSHPFilePath, "");
	        
		} 
		catch (Exception e){

			e.printStackTrace();
		}
		
		FeatureSchema fileSchema = myOriginalLayer.getFeatureCollectionWrapper().getFeatureSchema();

		ColumnRow idGeometryColRow;
		ColumnDB idGeometryDB;
		Hashtable htCambio = devuelveconfigCar(FICHERO_CONFIG);
		
		columnasAtributos.put("id", "id");
		columnasAtributos.put("id_municipio", "id_municipio");
		columnasAtributos.put("GEOMETRY", "GEOMETRY");
		
		for (int j=0; j<fileSchema.getAttributeCount(); j++){
			idGeometryColRow = new ColumnRow();
			idGeometryDB = new ColumnDB();
			if (!fileSchema.getAttributeName(j).equals("GEOMETRY")){
				// Recorremos el nombre del atributo comparandolo con nuestro fichero de configuracion, por si tiene algun
				// caracter no valido, para que lo reemplace:
				String nombreColumna = fileSchema.getAttributeName(j);
				String nombreAtributo = nombreColumna;
				for (int i=0;i<nombreColumna.length();i++){
					Set set = htCambio.keySet();
					Iterator iter = set.iterator();
					while (iter.hasNext()) {
						String clave = (String) iter.next();
						String valor = (String) htCambio.get(clave);
						nombreAtributo = nombreAtributo.replaceAll(clave, valor);
					}				
				}

				columnasAtributos.put(nombreAtributo, nombreColumna);
				
				idGeometryDB.setName(nombreAtributo);
				AttributeType tipoTMP = fileSchema.getAttributeType(j);
				String tipo = devuelveTipoBD(tipoTMP);
				idGeometryDB.setType(tipo);
				idGeometryDB.setLength(fileSchema.getAttributeWidth(j));
				idGeometryDB.setPrecission(fileSchema.getAttributeDecimal(j));
				idGeometryDB.setDefaultValue("");
				idGeometryDB.setDescription("");
				
				idGeometryColRow.setColumnaBD(idGeometryDB);
				Column idGeometryColSis = new Column("GEOMETRY", "GEOMETRY", null);
				idGeometryColRow.setColumnaSistema(idGeometryColSis);
	
				try {
					TablesDBOperations operaciones = new TablesDBOperations();
					operaciones.crearColumnaBD(newTable, idGeometryColRow);
				} catch (DataException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}			
		}   	
	
    }
        
    /**
     * Inserta los atributos
     * @param idLayer int Identificador de la capa
     * @param newTable Table Tabla creada previamente
     * @return El nombre de la secuencia. 
     */  
    public String insertaAtributos(Table newTable, int idLayer){

        LayerOperations operaciones = new LayerOperations();
        String tablaSecuencia = null;        
        List columnas;
		try {
			columnas = operaciones.obtenerListaColumnas(newTable);

	        Column columna;

	        int i = 0;
	        Iterator it = columnas.iterator();
	        
	        while (it.hasNext()){
        		columna =(Column) it.next();
                Attribute att = new Attribute();
                att.setEditable(Boolean.TRUE);
                att.setIdLayer(idLayer);
                att.setColumn(columna);
                i = i+1;
                att.setPosition(i);
                Hashtable htTraducciones = new Hashtable();
                htTraducciones.put(IDIOMA_ES, columnasAtributos.get(columna.getName()));
                att.setHtTraducciones(htTraducciones);
                if (att.getColumn().getIdColumn()!=0 && !att.getColumn().getTable().getName().equalsIgnoreCase("SYSTEM")){ 

					int idDictionary = 0;
					idDictionary = operaciones.actualizarDictionary (att.getHtTraducciones(), 0);

                    if (idDictionary >0)
                        att.setIdAlias(idDictionary);                
                    
                    operaciones.actualizarAtributo(att);
                    
                    //Para averiguar el nombre de la tabla en la que está la geometria (esta tabla se utiliza
                    //para crear el nombre de la secuencia)
                    if(att.getColumn().getName().equals("GEOMETRY"))
                        tablaSecuencia = att.getColumn().getTable().getName();  
                }
	        }
		} catch (DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tablaSecuencia;   	
	
    }
    
    /**
     * Devuelve el tipo de dato obtenido del SHP en formato para la BBDD
     * @param tipoTMP AttributeType el tipo del atributo
     * @return El tipo de dato. 
     */  
    private String devuelveTipoBD(AttributeType tipoTMP) {
    	if (tipoTMP.toString().equalsIgnoreCase("STRING"))
    		return COL_VARCHAR;
    	else
    		if (tipoTMP.toString().equalsIgnoreCase("DOUBLE"))
    			return COL_NUMERIC;
    		else
        		if (tipoTMP.toString().equalsIgnoreCase("INTEGER"))
        			return COL_NUMERIC;
        		else    			
        			if (tipoTMP.toString().equalsIgnoreCase("GEOMETRY"))
        				return COL_GEOMETRY;
    	return null;
    			
	}

    // COPIADO DE LayersPanel (Se ha copiado porque en LayersPanel este metodo es privado):
    /**
     * Genera la cláusula FROM de la consulta SQL propuesta
     * @param claves Vector de claves suministradas por el usuario
     * @param tablas Conjunto de tablas incluidas en las claves
     * @return Cláusula FROM de la consulta
     */
    private String generateFromClause (Vector claves, HashSet tablas)
    {
        StringBuffer fromClause = new StringBuffer();
        
        //Si no se han suministrado foreign key, el from consiste en el listado de tablas participantes       
        if (claves.isEmpty())
        {
            for (Iterator it = tablas.iterator(); it.hasNext();)
            {               
                fromClause.append("\"").append(it.next().toString()).append("\",");                
            }
            
            if (fromClause.length()>0)
                return fromClause.toString().substring(0, fromClause.length()-1);            
            else
                return fromClause.toString();            
        }        
        
        //ForeignCondition previo = new ForeignCondition(); 
        //Vector clavesOrdenadas = new Vector(claves.size());
        
        //Método de ordenación: 
        //1. Se ordenan alfabéticamente las condiciones (dentro de cada condicion 
        //y entre condiciones)
        //2. Se recorre el vector resultante de claves y se colocan seguidas
        //las condiciones encadenadas (A.a=B.b; A.c=B.d; B.d=C.e; D.d=C.a;...)
        
        Comparator clavesComparator = new Comparator(){
            public int compare(Object o1, Object o2) {
                ForeignCondition fc1 = (ForeignCondition) o1;
                ForeignCondition fc2 = (ForeignCondition) o2;
                
                
                //Ordenacion dentro de cada condicion
                String condIzq1 = fc1.getCondicionIzquierda();
                String condIzq2 = fc2.getCondicionIzquierda();
                String condDcha1 = fc1.getCondicionDerecha();
                String condDcha2 = fc2.getCondicionDerecha();
                
                String condIzquierda = null;
                String condDerecha = null;
                
                if (condIzq1.compareToIgnoreCase(condDcha1)<0)
                {
                    condIzquierda = String.valueOf(condDcha1);
                    condDerecha = String.valueOf(condIzq1);
                    fc1.setCondicionIzquierda(condIzquierda);
                    fc1.setCondicionDerecha(condDerecha);
                }
                
                if (condIzq2.compareToIgnoreCase(condDcha2)<0)
                {
                    condIzquierda = String.valueOf(condDcha2);
                    condDerecha = String.valueOf(condIzq2);
                    fc2.setCondicionIzquierda(condIzquierda);
                    fc2.setCondicionDerecha(condDerecha);
                }                
                
                //Ordenacion entre condiciones
                condIzq1 = fc1.getCondicionIzquierda();
                condIzq2 = fc2.getCondicionIzquierda();
                
                return condIzq1.compareToIgnoreCase(condIzq2);                
            }
        };
        
        ForeignCondition[] fcArray = (ForeignCondition[])claves.toArray(new ForeignCondition[claves.size()]);
        Arrays.sort(fcArray, clavesComparator);
        
        //Se convierte el array en vector para poder eliminar elementos mas facilmente
        Vector fcVector = new Vector(Arrays.asList(fcArray));
        
        //Se crean los tablafrom con los nombres de las tablas y las condiciones
        TableFrom[] tf = new TableFrom[tablas.size()];
        int i=0;
        for (Iterator it = tablas.iterator(); it.hasNext();)
        {
            String nomTabla = it.next().toString();
            tf[i] = new TableFrom();
            tf[i].setNombre1(nomTabla);
            
            for (int j=0; j< fcVector.size(); j++)
            {
                ForeignCondition fc = (ForeignCondition)fcVector.get(j);
                
                if (fc.getTablaIzquierda().equalsIgnoreCase(nomTabla)
                        || fc.getTablaDerecha().equalsIgnoreCase(nomTabla))
                {                     
                    if (!fc.getTablaIzquierda().equalsIgnoreCase(nomTabla)
                            && (tf[i].getNombre2()==null 
                                    ||tf[i].getNombre2().equalsIgnoreCase(fc.getTablaIzquierda())))
                    {
                        tf[i].addCondicion(fc.getCondicion());
                        fcVector.remove(fc);
                        j--;
                        tf[i].setNombre2(fc.getTablaIzquierda());
                    }
                    else if(!fc.getTablaDerecha().equalsIgnoreCase(nomTabla)
                            && ( tf[i].getNombre2()==null 
                                    || tf[i].getNombre2().equalsIgnoreCase(fc.getTablaDerecha())))
                    {
                        tf[i].addCondicion(fc.getCondicion());
                        fcVector.remove(fc);
                        j--;
                        tf[i].setNombre2(fc.getTablaDerecha());
                    }
                }                
            }            
            i++;
        }        
        
        //Habra un numero de inner join igual a tablas.size()-1.
        //En la asociacion anterior, uno de los nombre2 habrá quedado sin rellenar
        //Ordeno las relaciones:
        Vector tablasOrdenadas = new Vector();
        String origen =null;  
        int maxCont = 0;
        while (tablasOrdenadas.size()< tf.length
                && maxCont <10)
        {
            //buscar el que no tiene joins            
            if (origen==null)
            {
                for (int k =0; k< tf.length; k++)
                {
                    if (tf[k].getNombre2()==null)
                    {
                        origen = tf[k].getNombre1();
                        tablasOrdenadas.add(tf[k]);
                    }                    
                }                
            }
            //para todos los demas, buscar el que coincide su nombre2 con el nombre1 anterior
            else
            {
                for (int k =0; k< tf.length; k++)
                {
                    if (tf[k].getNombre2()!=null && tf[k].getNombre2().equals(origen))
                    {
                        origen = tf[k].getNombre1();
                        tablasOrdenadas.add(tf[k]);
                    }                    
                }                
            }
            maxCont++;
        }
        
        for (i=0; i< tablasOrdenadas.size(); i++)
        {
            TableFrom tabfrom = (TableFrom)(tablasOrdenadas.get(i));
            if (i==0)
            {
                fromClause.append("\"").append(tabfrom.getNombre1()).append("\"");
            }
            else
            {
                
                if (i==1)
                    fromClause.insert(0," INNER JOIN ").insert(0, "\""+tabfrom.getNombre1()+"\"").append(" ON (");
                else
                    fromClause.insert(0," INNER JOIN (").insert(0, tabfrom.getNombre1()).append(") ON (");
                
                for (int k=0; k<tabfrom.getCondicionesOn().size(); k++)
                {
                    StringBuffer s = new StringBuffer("\"");
                    s.append(tabfrom.getCondicionesOn().get(k).toString()).append("\"");
                    String st = s.toString().replaceFirst("=", "\"=\"");
                    st = st.replaceAll("\\.", "\"\\.\"" );
                    
                    fromClause.append(st).append(" AND ");
                }
                fromClause = new StringBuffer (fromClause.substring(0, fromClause.length()-5)).append(") ");                
            }            
        }
        
        return fromClause.toString();        
    }
 
	public List<String> devuelveFilasFichero(String rutaFichero){
		
		List<String> filas = new ArrayList<String>();
		
		File archivo = null;
		FileReader fr = null;
		
		try {
        	archivo = new File(rutaFichero);
        	String linea;
        	fr = new FileReader (archivo);
        	BufferedReader br = new BufferedReader(fr);
        	linea=br.readLine();
        	while(linea!=null){
        		if (!linea.equals("")){
        			String primeraLetra = linea.substring(0, 1);
        			if (!primeraLetra.equals("#"))
        				filas.add(linea);
        		}
            	linea=br.readLine();
        	}
		}
		catch(IOException e){
			e.printStackTrace();
		}finally{
			try {                
				if( fr != null){  
					fr.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}     
		}
		return filas;
	}
	
    public Hashtable devuelveconfigCar(String ficheroConfig){
		List filas = devuelveFilasFichero(ficheroConfig);
		Hashtable htCambio = new Hashtable();
		Iterator it = filas.iterator();

		while (it.hasNext()){
			String fila = (String)it.next();
			String clave = fila.substring(0, fila.indexOf(";"));
			fila = fila.substring(fila.indexOf(";")+1);
			String valor = fila.substring(0, fila.indexOf(";"));
			htCambio.put(clave, valor);
		}
		return htCambio;
    }
    
    public Table renombraTabla(AppContext aplicacion, String nombreTabla, String nuevoNombreTabla){
    	Boolean exito = Boolean.FALSE;
    	TablesDBOperations operaciones = new TablesDBOperations();
    	Table tabla = null;
    	try {
    		
			List tablas = operaciones.obtenerListaTablasBD();
			Iterator it = tablas.iterator();
			
			while (it.hasNext() && !exito){
				tabla = (Table) it.next();
				if (tabla.getDescription().equals(nombreTabla)){
					Connection directConn = aplicacion.getJDBCConnection("geopista", "geopista");

					String consulta = "ALTER TABLE \""+nombreTabla+"\" RENAME TO \""+nuevoNombreTabla+"\";" +
							"UPDATE tables SET name=\'"+nuevoNombreTabla+"\' WHERE id_table="+tabla.getIdTabla()+";";
					
					PreparedStatement ps = directConn.prepareStatement(consulta);
		            ps.executeUpdate();
		            directConn.close();  					
					exito = Boolean.TRUE;
				}	        
			}			
  
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    	
		return tabla;  	
    }
    
    public void renombraCapa(AppContext aplicacion, Table tabla, String nuevoNombreCapa){
    	LayerOperations operaciones = new LayerOperations();
    	try {
    		List columnas = operaciones.obtenerListaColumnas(tabla);
    		Iterator it = columnas.iterator();

 
			if (it.hasNext()){
				Column col = (Column) it.next();
				int idColumna = col.getIdColumn();				
				
				ResultSet rs = null;
				Connection directConn = aplicacion.getJDBCConnection("geopista", "geopista");

				String consulta = "update layers set name='"+nuevoNombreCapa+"' where id_layer=(select id_layer from attributes where id_column="+idColumna+");";					
				PreparedStatement ps = directConn.prepareStatement(consulta);
			    ps.executeUpdate();
			    directConn.close();   				
			}		
  
		} catch (SQLException e) {
			e.printStackTrace();	
		} catch (DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    		
    }

    public Table obtenerTabla(AppContext aplicacion, String nombreTabla){
    	Boolean exito = Boolean.FALSE;
    	TablesDBOperations operaciones = new TablesDBOperations();
    	Table tabla = null;
    	try {
    		
			List tablas = operaciones.obtenerListaTablasBD();
			Iterator it = tablas.iterator();
			
			while (it.hasNext() && !exito){
				tabla = (Table) it.next();
				if (tabla.getDescription().equals(nombreTabla)){					
					exito = Boolean.TRUE;
				}	        
			}			
  		} catch (DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    	
		return tabla;  	
    }    

    public int obtenerIdLayer(AppContext aplicacion, Table tabla){
    	LayerOperations operaciones = new LayerOperations();
    	int idLayer = 0;
    	try {
    		List columnas = operaciones.obtenerListaColumnas(tabla);
    		Iterator it = columnas.iterator();

 
			if (it.hasNext()){
				Column col = (Column) it.next();
				int idColumna = col.getIdColumn();				
				
				ResultSet rs = null;
				Connection directConn = aplicacion.getJDBCConnection("geopista", "geopista");

				String consulta = "select id_layer from attributes where id_column="+idColumna+";";					
				PreparedStatement ps = directConn.prepareStatement(consulta);
			    rs = ps.executeQuery();
			    idLayer = rs.getInt("id_layer");
			    
			    directConn.close();   				
			}		
  
		} catch (SQLException e) {
			e.printStackTrace();	
		} catch (DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return idLayer;
    }       
    
	public void actualizaColumnasTabla(Table newTable, String sSHPFilePath, AppContext aplicacion) {
    	
		GeopistaLayer myOriginalLayer=null;
		GeopistaEditor geopistaEditor1 = new GeopistaEditor(aplicacion.getString("fichero.vacio"));
	        
		try{	
		
			myOriginalLayer = (GeopistaLayer) geopistaEditor1.loadData(sSHPFilePath, "");
	        
		} 
		catch (Exception e){

			e.printStackTrace();
		}
		
		FeatureSchema fileSchema = myOriginalLayer.getFeatureCollectionWrapper().getFeatureSchema();

		ColumnRow idGeometryColRow;
		ColumnDB idGeometryDB;
		Hashtable htCambio = devuelveconfigCar(FICHERO_CONFIG);
		
		columnasAtributos.put("id", "id");
		columnasAtributos.put("id_municipio", "id_municipio");
		columnasAtributos.put("GEOMETRY", "GEOMETRY");
		
		TablesDBOperations operaciones = new TablesDBOperations();
		LayerOperations operacionesCapa = new LayerOperations();
		
		try {
			
			Hashtable atributos = new Hashtable();
			
			for (int j=0; j<fileSchema.getAttributeCount(); j++){
				String nombreColumna = fileSchema.getAttributeName(j);
				String nombreAtributo = nombreColumna;
				
				for (int i=0;i<nombreColumna.length();i++){
					Set set = htCambio.keySet();
					Iterator iter = set.iterator();
					while (iter.hasNext()) {
						String clave = (String) iter.next();
						String valor = (String) htCambio.get(clave);
						nombreAtributo = nombreAtributo.replaceAll(clave, valor);
					}				
				}				
				atributos.put(j, nombreAtributo);
			}
			
			HashMap columnas = operaciones.obtenerListaColumnasBD(newTable.getName());
			Iterator it = columnas.entrySet().iterator();
			
			while (it.hasNext()) {
				Map.Entry elemento = (Map.Entry)it.next();
				ColumnDB col = (ColumnDB) elemento.getValue();

				idGeometryColRow = new ColumnRow();
				idGeometryDB = new ColumnDB();
				

				
				if (!atributos.containsValue(col.getName())){
					System.out.println("NOMBRE ATRIBUTO: "+col.getName());					
				}
				
				/*
				if (!fileSchema.getAttributeName(j).equals("GEOMETRY")){
					// Recorremos el nombre del atributo comparandolo con nuestro fichero de configuracion, por si tiene algun
					// caracter no valido, para que lo reemplace:
					
					
					
					System.out.println(e.getKey() + " " + e.getValue());
					}
					if (!columnas.containsValue(nombreAtributo))
						System.out.println("NOMBRE ATRIBUTO: "+nombreAtributo);
/*
					columnasAtributos.put(nombreAtributo, nombreColumna);

					idGeometryDB.setName(nombreAtributo);
					AttributeType tipoTMP = fileSchema.getAttributeType(j);
					String tipo = devuelveTipoBD(tipoTMP);
					idGeometryDB.setType(tipo);
					idGeometryDB.setLength(fileSchema.getAttributeWidth(j));
					idGeometryDB.setPrecission(fileSchema.getAttributeDecimal(j));
					idGeometryDB.setDefaultValue("");
					idGeometryDB.setDescription("");
					
					idGeometryColRow.setColumnaBD(idGeometryDB);
					Column idGeometryColSis = new Column("GEOMETRY", "GEOMETRY", null);
					idGeometryColRow.setColumnaSistema(idGeometryColSis);
		
					try {

						operaciones.crearColumnaBD(newTable, idGeometryColRow);
					} catch (DataException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} */
				}
		//	} 
			
		} catch (DataException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		  	
		
	}

	public void actualizaAtributosCapa(AppContext aplicacion, Table newTable,
			String nombreCapa) {
		// TODO Auto-generated method stub
		
	}
}
