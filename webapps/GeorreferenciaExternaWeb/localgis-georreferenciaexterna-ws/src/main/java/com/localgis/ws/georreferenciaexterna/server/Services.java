/**
 * Services.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.ws.georreferenciaexterna.server;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.localgis.ws.georreferenciaexterna.server.beans.ColumnsBean;
import com.localgis.ws.georreferenciaexterna.server.beans.ConsultaDatosBean;
import com.localgis.ws.georreferenciaexterna.server.beans.FiltroData;
import com.localgis.ws.georreferenciaexterna.server.beans.RowBean;

public class Services {	
	
	//PostgresqlCuenca
	private static final String NOMBRE_BBDD_LOCALGIS = "PostgresqlLocalhost";
	static HashMap hash_idvia_codigoine = new HashMap();
	
	public static String[] nombresBbdd()
    { 
          Context envContext = null;
          Context initCtx;
          ArrayList array = new ArrayList(); 
          String str=""; 
          DataSource ds=null;          
          
	      try	
	      {	
	          initCtx = new InitialContext();	
	          envContext = (Context) initCtx.lookup("java:comp/env");
	          NamingEnumeration namingEnumeration = initCtx.list("java:comp/env");   //Me busca ese objeto en el contexto.
	          
	           	  while(namingEnumeration.hasMore())
		          {	
	           		    NameClassPair  a = (NameClassPair) namingEnumeration.next();   //Son parejas formadas por el objeto y la clase.       		    
		                
	           		    NamingEnumeration namingEnumeration1 = envContext.list("/"+a.getName());
	           		 
	           		    while(namingEnumeration1.hasMore())
	           		    {
	           		    	NameClassPair  b = (NameClassPair) namingEnumeration1.next();
	           		    	
	           		    	//sacar los nombres de los data sources
	           		    	str=b.getName().toString();	           		    
			                array.add(str);
	           		    }		                       		    
		          }	           	
	      } catch (NamingException e1)
	      {
	    	  e1.printStackTrace();
	
	      }     
	      
	      String [] nombres = new String[array.size()];
	      for (int i=0; i<array.size();i++){
	    	  nombres[i]=array.get(i).toString();
	      }
	      return nombres;
	}	
	
	
	public static Connection obtenerConexion(String bbdd) throws SQLException
    { 
		String bd;
		DataSource ds = null;
		Connection conn;
		Context envContext = null;
		Context initCtx;           
            		
	      try	
	      {	
	          initCtx = new InitialContext();	
	          envContext = (Context) initCtx.lookup("java:comp/env");
	          NamingEnumeration namingEnumeration = initCtx.list("java:comp/env");   //Me busca ese objeto en el contexto.
	          
	           	  while(namingEnumeration.hasMore())
		          {	
	           		    NameClassPair  a = (NameClassPair) namingEnumeration.next();   //Son parejas formadas por el objeto y la clase.       		    
		                
	           		    NamingEnumeration namingEnumeration1 = envContext.list("/"+a.getName());
	           		 
	           		    while(namingEnumeration1.hasMore())
	           		    {
	           		    	NameClassPair  b = (NameClassPair) namingEnumeration1.next();
	           		    	
	           		    	//sacar los data sources que se correspondan con la BD escogida.
	           		    	if(b.getName().equals(bbdd)){
	           		    		ds = (DataSource)envContext.lookup(a.getName()+"/"+b.getName());
	           		    	}
	           		    }		                       		    
		          }	           	
	      } catch (NamingException e1)
	      {
	    	  e1.printStackTrace();
	
	      }
	      return conn=ds.getConnection();
	}
	
	public static String[] obtenerTablas(String bbdd) throws SQLException
	{
		Connection connect;
		ArrayList array=new ArrayList();
		String tablaBD = "";
		String [] tablasBD=null;
		String nombreTablas = "%"; // Listamos todas las tablas
	    String tipos[] = new String[2]; // Listamos tablas y vistas
	    tipos[0] = "TABLE";
	    tipos[1] = "VIEW";
		  
	    connect=obtenerConexion(bbdd);
	    DatabaseMetaData dbmd = connect.getMetaData();
	    ResultSet tablas = dbmd.getTables( null, null,nombreTablas, tipos );		    
	    boolean seguir = tablas.next();	 
	    if (seguir==true){
	    	while( seguir ) {
		    	// Mostramos solo el nombre de las tablas, guardado
		    	// en la columna "TABLE_NAME"
		    	if(!dbmd.getDriverName().equals("MySQL-AB JDBC Driver")){
		    		tablaBD=tablas.getString(tablas.findColumn( "TABLE_SCHEM" ));
			    	tablaBD=tablaBD.concat("."+tablas.getString(tablas.findColumn( "TABLE_NAME" )));		    	
		    	}else{
		    		tablaBD=tablas.getString(tablas.findColumn( "TABLE_NAME" ));
		    	}
		    	array.add(tablaBD);  	    	
		        seguir = tablas.next();
		     };
		     
		     tablasBD = new String[array.size()];
		     for (int i=0; i<array.size();i++){
		    	 tablasBD[i]=array.get(i).toString();
		     }
	    }else{
	    	tablasBD = new String[1];
	    	tablasBD[0]="";
	    }	    
	    tablas.close();
	    connect.close();
	    return tablasBD;
	}
	
	public static String[] obtenerColumnas(String bbdd, String tabla) throws SQLException
	{
		Connection connect;
		Hashtable colTabla = new Hashtable();
		int cont=0;
		String nombreColumnas="%";
		ResultSet columns=null;
		
		connect=obtenerConexion(bbdd);
	    DatabaseMetaData dbmd = connect.getMetaData();
	    
	    if(!dbmd.getDriverName().equals("MySQL-AB JDBC Driver")){
	    	columns = dbmd.getColumns(null,tabla.substring(0, tabla.indexOf(".")), tabla.substring(tabla.indexOf(".")+1, tabla.length()), nombreColumnas);
	    }else{
	    	columns = dbmd.getColumns(null,null, tabla.substring(tabla.indexOf(".")+1, tabla.length()), nombreColumnas);
	    }
	    
	    boolean seguir = columns.next();
	    
	    while( seguir ) {
	    	// Mostramos solo el nombre de las tablas, guardado
	        // en la columna "TABLE_NAME"
	    	
	    	int tipo=columns.getInt(columns.findColumn( "DATA_TYPE" ));
	    	  
	    	  switch ( tipo ) {
	    	  case 1:
	        	  colTabla.put(columns.getString(columns.findColumn( "COLUMN_NAME" )),"CHAR");
	               break;
	          case 2:
	        	  colTabla.put(columns.getString(columns.findColumn( "COLUMN_NAME" )),"NUMERIC");
	               break;
	          case 3:
	        	  colTabla.put(columns.getString(columns.findColumn( "COLUMN_NAME" )),"DECIMAL");
	               break;
	          case 4:
	        	  colTabla.put(columns.getString(columns.findColumn( "COLUMN_NAME" )),"INTEGER");
	               break;
	          case 5:
	        	  colTabla.put(columns.getString(columns.findColumn( "COLUMN_NAME" )),"SMALLINT");
	               break;
	          case 6:
	        	  colTabla.put(columns.getString(columns.findColumn( "COLUMN_NAME" )),"FLOAT");
	               break;
	          case 7:
	        	  colTabla.put(columns.getString(columns.findColumn( "COLUMN_NAME" )),"REAL");
	               break;
	          case 8:
	        	  colTabla.put(columns.getString(columns.findColumn( "COLUMN_NAME" )),"DOUBLE");
	               break;
	          case 12:
	        	  colTabla.put(columns.getString(columns.findColumn( "COLUMN_NAME" )),"VARCHAR");
	               break;
	          case 16:
	        	  colTabla.put(columns.getString(columns.findColumn( "COLUMN_NAME" )),"BOOLEAN");
	               break;
	          case 91:
	        	  colTabla.put(columns.getString(columns.findColumn( "COLUMN_NAME" )),"DATE");
	               break;
	          case 92:
	        	  colTabla.put(columns.getString(columns.findColumn( "COLUMN_NAME" )),"TIME");
	               break;
	          case 93:
	        	  colTabla.put(columns.getString(columns.findColumn( "COLUMN_NAME" )),"TIMESTAMP");
	               break;
	          case 2004:
	        	  colTabla.put(columns.getString(columns.findColumn( "COLUMN_NAME" )),"BLOB");
	               break;	
	          case 2005:
	        	  colTabla.put(columns.getString(columns.findColumn( "COLUMN_NAME" )),"CLOB");
	               break;
	          case -1:
	        	  colTabla.put(columns.getString(columns.findColumn( "COLUMN_NAME" )),"LONGVARCHAR");
	               break;
	          case -5:
	        	  colTabla.put(columns.getString(columns.findColumn( "COLUMN_NAME" )),"BIGINT");
	               break;
	          default:
	        	  colTabla.put(columns.getString(columns.findColumn( "COLUMN_NAME" )),"OTHER");
	               break;
	          }
	        seguir = columns.next();
	      };
	      
	      String [] colBD = new String[colTabla.size()*2];
	      
	      Set valores = colTabla.keySet();
		  Iterator listaCampos = valores.iterator();

		  while (listaCampos.hasNext()){
			String key = listaCampos.next().toString();
			colBD[cont]=key;			
			cont++;
			colBD[cont]=colTabla.get(key).toString();
			cont++;
		  }
		  
	      columns.close();
	      connect.close();
	      return colBD;	 
	}
	
	public static int obtenerTotalFilas(String bbdd, String tabla, String campos_ref, String[] campo_eleg) throws SQLException
	{
		Connection connect;		
		connect=obtenerConexion(bbdd);
		
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		String sSQL="";
		int totalFilas=0;
		ArrayList filas=new ArrayList();
		RowBean[] fila=null;
		
		if(campos_ref.equals("Referencia Catastral")){
			sSQL="SELECT COUNT(*) AS total FROM " + tabla + " WHERE " + tabla +"." + campo_eleg[0]+ "!=' '";
		}else if(campos_ref.equals("Direcciï¿½n Postal")){
			sSQL="SELECT COUNT(*) AS total FROM " + tabla + " WHERE " + tabla + "." + campo_eleg[0] + ">0";
		}else{
			sSQL="SELECT COUNT(*) AS total FROM "+ tabla + " WHERE " + campo_eleg[0] + ">0 AND "+ campo_eleg[1] +">0";
		}

		ps=connect.prepareStatement(sSQL);
		rs=ps.executeQuery();
		
		while (rs.next()){
			totalFilas=rs.getInt("total");			
		}
		return totalFilas;
	}
	
	public static String[] obtenerTablasBBDDLocalGis() throws SQLException{
		Connection connect;
		ArrayList array=new ArrayList();
		String tablaBD = "";
		String [] tablasBD=null;
		String nombreTablas = "%"; // Listamos todas las tablas
	    String tipos[] = new String[2]; // Listamos tablas y vistas
	    tipos[0] = "TABLE";
	    tipos[1] = "VIEW";
		  
	    connect=obtenerConexion(NOMBRE_BBDD_LOCALGIS);
	    DatabaseMetaData dbmd = connect.getMetaData();
	    ResultSet tablas = dbmd.getTables( null, null,nombreTablas, tipos );		    
	    boolean seguir = tablas.next();	 
	    if (seguir==true){
	    	while( seguir ) {
		    	// Mostramos solo el nombre de las tablas, guardado
		    	// en la columna "TABLE_NAME"
		    	if(!dbmd.getDriverName().equals("MySQL-AB JDBC Driver")){
		    		tablaBD=tablas.getString(tablas.findColumn( "TABLE_SCHEM" ));
			    	tablaBD=tablaBD.concat("."+tablas.getString(tablas.findColumn( "TABLE_NAME" )));		    	
		    	}else{
		    		tablaBD=tablas.getString(tablas.findColumn( "TABLE_NAME" ));
		    	}
		    	array.add(tablaBD);  	    	
		        seguir = tablas.next();
		     };
		     
		     tablasBD = new String[array.size()];
		     for (int i=0; i<array.size();i++){
		    	 tablasBD[i]=array.get(i).toString();
		     }
	    }else{
	    	tablasBD = new String[1];
	    	tablasBD[0]="";
	    }	    
	    tablas.close();
	    connect.close();
	    return tablasBD;
	}
	
	private static HashMap obtenerDatosTablaExterna_RefCatas_DirPos(PreparedStatement ps, ResultSet rs, String bbddExterna, 
			String[] nombre_campos, String[] tipo_campos, String campos_ref, String tabla, StringBuffer campos, 
			String[] campo_eleg, int valorInicial, int valorFinal, FiltroData[] lstFiltro) throws SQLException{
		
		HashMap hashDatos = new HashMap();
		ArrayList almacenDeClavesParaConsulta = new ArrayList();
		String sSQL="";
		String sSQLFiltro="";
		int l=0;
		ArrayList filas=new ArrayList();
		ArrayList columns = new ArrayList();
		ColumnsBean colb=new ColumnsBean(); 
		
		Connection connect;		
		connect=obtenerConexion(bbddExterna);

		
		if(campos_ref.equals("Referencia Catastral")){				

			sSQL="SELECT "+ campos +" FROM " + tabla + " WHERE " + tabla +"." + campo_eleg[0]+ "!=' '";				
		}else{
			sSQL="SELECT "+ campos +" FROM " + tabla + " WHERE " + tabla + "." + campo_eleg[0] + ">0";
							
		}
		
		String sqlOperador = "";  
		if(lstFiltro != null && lstFiltro.length>0){
			
				for(int i=0; i<lstFiltro.length;i++){
					FiltroData filtro = (FiltroData)lstFiltro[i];
				if(filtro != null){
					sqlOperador += " AND "; 
					String typeCamp = filtro.getTipoCampo(); 
					
					if(filtro.getTipoCampo().equals("VARCHAR") || typeCamp.equals("CHAR") || typeCamp.equals("LONGVARCHAR")){
						// de tipo texto
						if(filtro.getOperador().equals(FiltroData.TEXTO_PATRON)){
							sqlOperador += " " + filtro.getCampo() +" LIKE '" +filtro.getValor() + "%'";
						}
						else if(filtro.getOperador().equals(FiltroData.TEXTO_COINCIDIR)){
							String [] stArrayValores = filtro.getValor().split(";"); 
							
							sqlOperador += " " + filtro.getCampo() + " IN (";
							for(int h=0; h<stArrayValores.length; h++){
								sqlOperador += "'" +stArrayValores[h]+"',";
							}
							// eliminamos la ultima coma denro del IN
							sqlOperador = sqlOperador.substring(0, sqlOperador.length()-1);
							sqlOperador += ")";
						}
						else{
							sqlOperador += " " + filtro.getCampo() + " " + filtro.getOperador()+ " '" + filtro.getValor()+ "'"; 
						}
					}
					else if (typeCamp.equals("INTEGER") || typeCamp.equals("NUMERIC") ||
		    				typeCamp.equals("DECIMAL") || typeCamp.equals("SMALLINT") ||
		    				typeCamp.equals("FLOAT") || typeCamp.equals("REAL") ||
		    				typeCamp.equals("DOUBLE") || typeCamp.equals("BIGINT")){
						
						if(filtro.getOperador().equals(FiltroData.NUMERICO_COINCIDIR)){
							String [] stArrayValores = filtro.getValor().split(";"); 
							
							sqlOperador += " " + filtro.getCampo() + " IN (";
							for(int h=0; h<stArrayValores.length; h++){
								sqlOperador += stArrayValores[h]+",";
							}
							// eliminamos la ultima coma denro del IN
							sqlOperador = sqlOperador.substring(0, sqlOperador.length()-1);
							sqlOperador += ")";
						} 
						else{
							sqlOperador += " " + filtro.getCampo() + " " + filtro.getOperador()+ " " + filtro.getValor();
						}
						
					}
					else if(typeCamp.equals("DATE")){
						sqlOperador += " " + filtro.getCampo() + " " + filtro.getOperador()+ " '" + filtro.getValor()+"'";
					}

				}

			}
		}
		
		
		sSQL += sqlOperador;

		ps=connect.prepareStatement(sSQL);
		rs=ps.executeQuery(); 
		
		int numeroRsActual = 0;			
		int inicio = valorInicial;//valorInicio
		int fin = valorFinal;//valorFinal
		while (rs.next() && numeroRsActual<fin){
			numeroRsActual++;

			RowBean row = new RowBean();
			if(numeroRsActual>= inicio && numeroRsActual<fin){										
				try{					
				for (l=0;l<nombre_campos.length;l++){
					
			  		if (!tipo_campos[l].equals("GEOMETRY")){
			  			if (tipo_campos[l].equals("INTEGER")){
				  			if(rs.getInt(nombre_campos[l])!=0){
				  				colb.setValue(String.valueOf(rs.getInt(nombre_campos[l])));		  				
				  			}else{
				  				colb.setValue(String.valueOf("0"));  					
				  			}
				  			colb.setName(nombre_campos[l]);					
							colb.setType(tipo_campos[l]);							
						}else if (tipo_campos[l].equals("STRING")){
							if (rs.getString(nombre_campos[l])!=null){
								colb.setValue(rs.getString(nombre_campos[l]));											  			
							}else{
								colb.setValue("");											  			
							}							
							colb.setName(nombre_campos[l]);							
							colb.setType(tipo_campos[l]);							
						}else if(tipo_campos[l].equals("LONG")){
							if(rs.getLong(nombre_campos[l])!=0){
								colb.setValue(String.valueOf(rs.getLong(nombre_campos[l])));							
							}else{
								colb.setValue(String.valueOf("0"));	 	  					
				  			}							
							colb.setName(nombre_campos[l]);								
							colb.setType(tipo_campos[l]);							
						}else if (tipo_campos[l].equals("DOUBLE")){
							if(rs.getDouble(nombre_campos[l])!=0 && !nombre_campos[l].equals(campo_eleg[0])){
								colb.setValue(String.valueOf(rs.getDouble(nombre_campos[l])));						
							}else if(rs.getDouble(nombre_campos[l])!=0 && nombre_campos[l].equals(campo_eleg[0])){
								colb.setValue(String.valueOf(rs.getInt(nombre_campos[l])));
							}else{
								colb.setValue(String.valueOf("0"));		  					
				  			}
							colb.setName(nombre_campos[l]);					
							colb.setType(tipo_campos[l]);							
						}else if (tipo_campos[l].equals("FLOAT")){
							if(rs.getFloat(nombre_campos[l])!=0){
								colb.setValue(String.valueOf(rs.getFloat(nombre_campos[l])));						
							}else{
								colb.setValue(String.valueOf("0"));		  					
				  			}
							colb.setName(nombre_campos[l]);					
							colb.setType(tipo_campos[l]);							
						}else if (tipo_campos[l].equals("BOOLEAN")){
							if (rs.getBoolean(nombre_campos[l])!=false){
								colb.setValue(String.valueOf(rs.getBoolean(nombre_campos[l])));											  			
							}else{
								colb.setValue("");											  			
							}
							colb.setName(nombre_campos[l]);					
							colb.setType(tipo_campos[l]);							
						}else if (tipo_campos[l].equals("TIME")){
							if (rs.getTime(nombre_campos[l])!=null){
								colb.setValue(rs.getTime(nombre_campos[l]).toString());											  			
							}else{
								colb.setValue("");											  			
							}
							colb.setName(nombre_campos[l]);					
							colb.setType(tipo_campos[l]);							
						}else if (tipo_campos[l].equals("TIMESTAMP")){
							if (rs.getTimestamp(nombre_campos[l])!=null){
								colb.setValue(String.valueOf(rs.getTimestamp(nombre_campos[l])));											  			
							}else{
								colb.setValue("");											  			
							}
							colb.setName(nombre_campos[l]);					
							colb.setType(tipo_campos[l]);							
						}else if (tipo_campos[l].equals("BLOB")){
							colb.setValue(String.valueOf(rs.getBlob(nombre_campos[l])));
							colb.setName(nombre_campos[l]);				
							colb.setType(tipo_campos[l]);							
						}else if (tipo_campos[l].equals("CLOB")){
							colb.setValue(String.valueOf(rs.getClob(nombre_campos[l])));
							colb.setName(nombre_campos[l]);				
							colb.setType(tipo_campos[l]);							
						}else if (tipo_campos[l].equals("DATE")){
							if(rs.getDate(nombre_campos[l])!=null){
								colb.setValue(String.valueOf(rs.getDate(nombre_campos[l])));						
							}else{
								colb.setValue("");						
							}	
							colb.setName(nombre_campos[l]);				
							colb.setType(tipo_campos[l]);							
						}else{
							colb.setName("");
							colb.setType("");
							colb.setValue("");							
							continue;
						}
						
			  			ColumnsBean column = new ColumnsBean();
				  		column.setName(colb.getName().toString());				  		
				  		column.setType(colb.getType().toString());				  		
				  		column.setValue(colb.getValue().toString());				  						  		
				  			
				  		if(column.getName().equals(campo_eleg[0]))
				  			row.setKey(column.getValue());
				  			
				  		columns.add(column);				  		
			  		}			  		
				}					
			  	row.setColumns((ColumnsBean[])columns.toArray(new ColumnsBean[columns.size()]));
			  		
			  	//Obtener las claves de las columns
			  		
			  	ColumnsBean[] colbean=row.getColumns();
			  		
			  	//Aquï¿½ almaceno las claves en un arraylist
			  		
			  	for (int j=0;j<colbean.length;j++){
			  		if(colbean[j].getName().contains(campo_eleg[0])){
			  			almacenDeClavesParaConsulta.add(colbean[j].getValue());			  			
			  		}
			  	}
			  	filas.add(row);			  	
			  	columns.clear();
				}catch(Exception e){					
					e.printStackTrace();
				}
			}
		}			
		
		hashDatos.put("CLAVES", almacenDeClavesParaConsulta);
		hashDatos.put("FILAS", filas);
		connect.close(); 
		return hashDatos;  
	}
	
	private static HashMap obtenerDatosTablaExterna_XY(PreparedStatement ps, ResultSet rs, String bbddExterna, 
			String[] nombre_campos, String tabla ,StringBuffer campos,
			String[] tipo_campos, String[] campo_eleg,
			int valorInicial, int valorFinal, String tipoGeometria , int iEPSGCode,
			FiltroData[] lstFiltro) throws SQLException{
		
		ArrayList almacenDeClavesParaConsulta = new ArrayList();
		HashMap hashDatos = new HashMap();
		
		ColumnsBean colb=new ColumnsBean();
		ArrayList columns = new ArrayList();
		ArrayList filas=new ArrayList();
		
		int l=0;	
		int numeroRsActual = 0;
		int inicio = valorInicial;//valorInicio
		int fin = valorFinal;//valorFinal
		
		Connection connect;		
		connect=obtenerConexion(bbddExterna);
		String sSQL="SELECT "+ campos +" FROM " + tabla + " WHERE " + campo_eleg[0] + ">0 AND "+ campo_eleg[1] +">0 ";
		
		
		String sqlOperador = "";  
		if(lstFiltro != null && lstFiltro.length>0){
			
				for(int i=0; i<lstFiltro.length;i++){
					FiltroData filtro = (FiltroData)lstFiltro[i];
				if(filtro != null){
					sqlOperador += " AND "; 
					String typeCamp = filtro.getTipoCampo(); 
	
					if(filtro.getTipoCampo().equals("VARCHAR") || typeCamp.equals("CHAR") || typeCamp.equals("LONGVARCHAR")){
						// de tipo texto
						if(filtro.getOperador().equals(FiltroData.TEXTO_PATRON)){
							sqlOperador += " " + filtro.getCampo() +" LIKE '" +filtro.getValor() + "%'";
						}
						else if(filtro.getOperador().equals(FiltroData.TEXTO_COINCIDIR)){
							String [] stArrayValores = filtro.getValor().split(";"); 
							
							sqlOperador += " " + filtro.getCampo() + " IN (";
							for(int h=0; h<stArrayValores.length; h++){
								sqlOperador += "'" +stArrayValores[h]+"',";
							}
							// eliminamos la ultima coma denro del IN
							sqlOperador = sqlOperador.substring(0, sqlOperador.length()-1);
							sqlOperador += ")";
						}
						else{
							sqlOperador += " " + filtro.getCampo() + " " + filtro.getOperador()+ " '" + filtro.getValor()+ "'"; 
						}
					}
					else if (typeCamp.equals("INTEGER") || typeCamp.equals("NUMERIC") ||
		    				typeCamp.equals("DECIMAL") || typeCamp.equals("SMALLINT") ||
		    				typeCamp.equals("FLOAT") || typeCamp.equals("REAL") ||
		    				typeCamp.equals("DOUBLE") || typeCamp.equals("BIGINT")){
						
						if(filtro.getOperador().equals(FiltroData.NUMERICO_COINCIDIR)){
							String [] stArrayValores = filtro.getValor().split(";"); 
							
							sqlOperador += " " + filtro.getCampo() + " IN (";
							for(int h=0; h<stArrayValores.length; h++){
								sqlOperador += stArrayValores[h]+",";
							}
							// eliminamos la ultima coma denro del IN
							sqlOperador = sqlOperador.substring(0, sqlOperador.length()-1);
							sqlOperador += ")";
						} 
						else{
							sqlOperador += " " + filtro.getCampo() + " " + filtro.getOperador()+ " " + filtro.getValor();
						}
						
					}
					else if(typeCamp.equals("DATE")){
						sqlOperador += " " + filtro.getCampo() + " " + filtro.getOperador()+ " '" + filtro.getValor()+"'";
					}

				}

			}
		}
		
		sSQL += sqlOperador;
		
		
		
		
		ps=connect.prepareStatement(sSQL);			
		rs=ps.executeQuery();	
		
		
		
		
		while (rs.next() && numeroRsActual<fin){
			RowBean row = new RowBean();
			
			numeroRsActual++;				
			if(numeroRsActual>= inicio && numeroRsActual<fin){	
				try{
			  	for (l=0;l<nombre_campos.length;l++){
				  		if (!tipo_campos[l].equals("GEOMETRY")){
				  			if (tipo_campos[l].equals("INTEGER")){
					 			if(rs.getInt(nombre_campos[l])!=0){
					  				colb.setValue(String.valueOf(rs.getInt(nombre_campos[l])));		  				
					  			}else{
					  				colb.setValue(String.valueOf("0"));  					
					  			}
					  			colb.setName(nombre_campos[l]);					
								colb.setType(tipo_campos[l]);				  			
							}else if (tipo_campos[l].equals("STRING")){
								if (rs.getString(nombre_campos[l])!=null){
									colb.setValue(rs.getString(nombre_campos[l]));												  			
								}else{
									colb.setValue("");											  			
								}
								colb.setName(nombre_campos[l]);					
								colb.setType(tipo_campos[l]);					
							}else if(tipo_campos[l].equals("LONG")){
								if(rs.getLong(nombre_campos[l])!=0){
									colb.setValue(String.valueOf(rs.getLong(nombre_campos[l])));							
								}else{
									colb.setValue(String.valueOf("0"));		  					
					  			}
								colb.setName(nombre_campos[l]);					
								colb.setType(tipo_campos[l]);
							}else if (tipo_campos[l].equals("DOUBLE")){
								if(rs.getDouble(nombre_campos[l])!=0){
									colb.setValue(String.valueOf(rs.getDouble(nombre_campos[l])));						
								}else{
									colb.setValue(String.valueOf("0"));		  					
					  			}
								colb.setName(nombre_campos[l]);					
								colb.setType(tipo_campos[l]);
							}else if (tipo_campos[l].equals("FLOAT")){
								if(rs.getFloat(nombre_campos[l])!=0){
									colb.setValue(String.valueOf(rs.getFloat(nombre_campos[l])));						
								}else{
									colb.setValue(String.valueOf("0"));		  					
					  			}
								colb.setName(nombre_campos[l]);					
								colb.setType(tipo_campos[l]);
							}else if (tipo_campos[l].equals("BOOLEAN")){
								if (rs.getBoolean(nombre_campos[l])!=false){
									colb.setValue(String.valueOf(rs.getBoolean(nombre_campos[l])));											  			
								}else{
									colb.setValue("");											  			
								}
								colb.setName(nombre_campos[l]);					
								colb.setType(tipo_campos[l]);
							}else if (tipo_campos[l].equals("TIME")){
								if (rs.getTime(nombre_campos[l])!=null){
									colb.setValue(rs.getTime(nombre_campos[l]).toString());											  			
								}else{
									colb.setValue("");											  			
								}
								colb.setName(nombre_campos[l]);					
								colb.setType(tipo_campos[l]);
							}else if (tipo_campos[l].equals("TIMESTAMP")){
								if (rs.getTimestamp(nombre_campos[l])!=null){
									colb.setValue(rs.getTimestamp(nombre_campos[l]).toString());											  			
								}else{
									colb.setValue("");											  			
								}
								colb.setName(nombre_campos[l]);					
								colb.setType(tipo_campos[l]);
							}else if (tipo_campos[l].equals("BLOB")){
								colb.setValue(String.valueOf(rs.getBlob(nombre_campos[l])));
								colb.setName(nombre_campos[l]);				
								colb.setType(tipo_campos[l]);									
							}else if (tipo_campos[l].equals("CLOB")){
								colb.setValue(String.valueOf(rs.getClob(nombre_campos[l])));
								colb.setName(nombre_campos[l]);				
								colb.setType(tipo_campos[l]);										
							}else if (tipo_campos[l].equals("DATE")){
								if(rs.getDate(nombre_campos[l])!=null){
									colb.setValue(String.valueOf(rs.getDate(nombre_campos[l])));						
								}else{
									colb.setValue("");						
								}	
								colb.setName(nombre_campos[l]);				
								colb.setType(tipo_campos[l]);
							}else{
								colb.setName("");
								colb.setType("");
								colb.setValue("");
								continue;
							}
				  		}
				  			
				  		ColumnsBean column = new ColumnsBean();
				  		column.setName(colb.getName().toString());
				  		column.setType(colb.getType().toString());
				  		column.setValue(colb.getValue().toString());
				  			
				  		columns.add(column);
					}	  		
				  		
				  						
				  	row.setColumns((ColumnsBean[])columns.toArray(new ColumnsBean[columns.size()]));
				  		
				  	//Obtener las claves de las columns
				  		
				  	ColumnsBean[] colbean=row.getColumns();
				  		
				  	//Aquï¿½ almaceno las claves en un arraylist
				  		
				  		
			  		String coordX="";
				  	String coordY="";
				  		
				  	for (int j=0;j<colbean.length;j++){
				  		if(colbean[j].getName().equals(campo_eleg[0])){
				  			coordX=colbean[j].getValue();	  				
				  		}
				  		if(colbean[j].getName().equals(campo_eleg[1])){
				  			coordY=colbean[j].getValue();
				  		}
				  	}

				  	row.setGeometryWkt("POINT("+coordX+" "+coordY+")");
				  	row.setSrid("4230");

				  	filas.add(row);			  	
				  	columns.clear(); 
				}catch(Exception e){					
					e.printStackTrace();
				}
			}
		}			
		
		hashDatos.put("FILAS", filas);
		connect.close(); 
		return hashDatos;  
	}
	

	private static String contruirSelectDatos( ArrayList almacenDeClavesParaConsulta, String campos_ref, boolean tipo_dato,
			String campoGeometria, String portal) throws SQLException{
		
		String sSQL1="";
		
		if(almacenDeClavesParaConsulta.size()<1000){
			StringBuffer valoresProcesados = getConcatenatedString(almacenDeClavesParaConsulta,tipo_dato);	
			
			//Hago la consulta para sacar la geometrï¿½a
			
			if(campos_ref.equals("Referencia Catastral")){
				sSQL1="SELECT " + campoGeometria + " AS geometria ,srid(\"GEOMETRY\") AS srid ,referencia_catastral FROM parcelas WHERE referencia_catastral IN ("+ valoresProcesados +") ";				

			}else{
				if(portal != null && !portal.equals("")){ 
					//se ha selecionado via mas portal

					String sSQL1_AUX="SELECT id, codigoine FROM vias WHERE codigoine IN ("+ valoresProcesados +") ";

					ArrayList lstIDVias = new ArrayList();
					hash_idvia_codigoine = new HashMap();
					
					
					PreparedStatement psGeom=null;
					ResultSet rsGeom=null;
					Connection connectGeom;				
					connectGeom=obtenerConexion(NOMBRE_BBDD_LOCALGIS);
					psGeom = connectGeom.prepareStatement(sSQL1_AUX);
					rsGeom = psGeom.executeQuery();
					
					while (rsGeom.next()){
						
						if((rsGeom.getString("id") != null && !rsGeom.getString("id").equals(""))){
							hash_idvia_codigoine.put(rsGeom.getString("codigoine"), rsGeom.getString("id"));
							lstIDVias.add(rsGeom.getString("id"));
						}
						
					}
					connectGeom.close();
					
					
					if(!lstIDVias.isEmpty()){
						String sqlAux = " IN (";
						for(int j= 0; j<lstIDVias.size(); j++){
							sqlAux += lstIDVias.get(j)+ ",";
							
						}
						sqlAux = sqlAux.substring(0, sqlAux.length()-1);
						sqlAux += ")";
						sSQL1="SELECT " + campoGeometria + " AS geometria ,srid(\"GEOMETRY\") AS srid ,id_via AS id FROM numeros_policia WHERE id_via "+ sqlAux +" AND rotulo = '"+portal+"' "; 
					}

					else{
						sSQL1 = "";
					}
					
					
				}
				else{
					//se ha seleccionados via
					sSQL1="SELECT " + campoGeometria + " AS geometria ,srid(\"GEOMETRY\") AS srid ,codigoine AS id FROM vias WHERE codigoine IN ("+ valoresProcesados +") ";
				}
			}				 
		}
		else{
			if(campos_ref.equals("Referencia Catastral")){
				sSQL1="SELECT " + campoGeometria + " AS geometria ,srid(\"GEOMETRY\") AS srid ,referencia_catastral FROM parcelas";				
			}else{ 
				
				if(portal != null && !portal.equals("")){ 
							 
					//se ha selecionado via mas portal
					String sSQL1_AUX="SELECT id, codigoine FROM vias";
	
					ArrayList lstIDVias = new ArrayList();
					hash_idvia_codigoine = new HashMap();
					
					
					PreparedStatement psGeom=null;
					ResultSet rsGeom=null;
					Connection connectGeom;				
					connectGeom=obtenerConexion(NOMBRE_BBDD_LOCALGIS);
					psGeom = connectGeom.prepareStatement(sSQL1_AUX);
					rsGeom = psGeom.executeQuery();
					
					while (rsGeom.next()){
						
						if((rsGeom.getString("id") != null && !rsGeom.getString("id").equals(""))){
							hash_idvia_codigoine.put(rsGeom.getString("codigoine"), rsGeom.getString("id"));
							lstIDVias.add(rsGeom.getString("id"));
						}
						
					}
					connectGeom.close();
					
					
					if(!lstIDVias.isEmpty()){
						String sqlAux = " IN (";
						for(int j= 0; j<lstIDVias.size(); j++){
							sqlAux += lstIDVias.get(j)+ ",";
							
						}
						sqlAux = sqlAux.substring(0, sqlAux.length()-1);
						sqlAux += ")";
						 
						sSQL1="SELECT " + campoGeometria + " AS geometria ,srid(\"GEOMETRY\") AS srid ,id_via AS id FROM numeros_policia WHERE id_via "+ sqlAux +" AND rotulo = '"+portal+"'";
					}
					else{
						sSQL1 = "";
					}
					
					
					//se ha selecionado via mas portal
					sSQL1="SELECT " + campoGeometria + " AS geometria ,srid(\"GEOMETRY\") AS srid ,id_via AD id FROM numeros_policia WHERE rotulo = '"+portal+"' ";
				}
				else{
					//se ha seleccionados via
					sSQL1="SELECT " + campoGeometria + " AS geometria ,srid(\"GEOMETRY\") AS srid ,codigoine AS id FROM vias ";
				}
								
			}
		}
		
		return sSQL1;
	}
	
	private static String contruirSelectDatosPointCruceTabla( ArrayList almacenDeClavesParaConsulta, String campos_ref, boolean tipo_dato,
			String campoGeometria){
		String sSQL1="";
		
		
		if(almacenDeClavesParaConsulta.size()<1000){
			StringBuffer valoresProcesados = getConcatenatedString(almacenDeClavesParaConsulta,tipo_dato);	
			
			//Hago la consulta para sacar la geometrï¿½a
			
			if(campos_ref.equals("Referencia Catastral")){
				sSQL1="SELECT " + campoGeometria + " ,srid(\"GEOMETRY\") AS srid ,referencia_catastral FROM parcelas WHERE referencia_catastral IN ("+ valoresProcesados +") AND \"GEOMETRY\" is not null";
				
			}else{
				sSQL1="SELECT " + campoGeometria + " ,srid(\"GEOMETRY\") AS srid ,id FROM vias WHERE id IN ("+ valoresProcesados +") AND \"GEOMETRY\" is not null";				
			}				 
		}else{
			if(campos_ref.equals("Referencia Catastral")){
				sSQL1="SELECT " + campoGeometria + ",srid(\"GEOMETRY\") AS srid ,referencia_catastral FROM parcelas WHERE \"GEOMETRY\" is not null";				
			}else{
				sSQL1="SELECT " + campoGeometria + ",srid(\"GEOMETRY\") AS srid ,id FROM vias WHERE \"GEOMETRY\" is not null";				
			}
		}
		return sSQL1;
	}
	
	
	private static HashMap obtenerGeometriaInterseccion(String referencia_catastral, String tablaCruceGeometria,
			String campos_ref ) throws SQLException{
		
		String tablaGeometria = "";
		String campo = "";
		String sSQL = "";
		
		PreparedStatement ps=null;
		ResultSet rs=null;
				
		PreparedStatement psGeom=null;
		ResultSet rsGeom=null;
		
		if(campos_ref.equals("Referencia Catastral")){
			tablaGeometria = "parcelas";
			sSQL = "SELECT asText(\"GEOMETRY\") AS geometria , srid(\"GEOMETRY\") AS srid from " + tablaCruceGeometria +" where intersects("+ tablaCruceGeometria +".\"GEOMETRY\",(SELECT CENTROID(\"GEOMETRY\") FROM "+tablaGeometria+" WHERE referencia_catastral = '"+ referencia_catastral +"' AND \"GEOMETRY\" is not null))";
		}
		else if(campos_ref.equals("Direcciï¿½n Postal")){
			tablaGeometria = "vias";
			
			sSQL = "SELECT asText(\"GEOMETRY\") AS geometria , srid(\"GEOMETRY\") AS srid from " + tablaCruceGeometria +" where intersects("+ tablaCruceGeometria +".\"GEOMETRY\",(SELECT CENTROID(\"GEOMETRY\") FROM "+tablaGeometria+" WHERE id = '"+ referencia_catastral +"' AND \"GEOMETRY\" is not null))";
		}
		
		
		Connection connectGeom;				
		connectGeom=obtenerConexion(NOMBRE_BBDD_LOCALGIS);
		
		psGeom=connectGeom.prepareStatement(sSQL);
		rsGeom=psGeom.executeQuery();
		
		HashMap geomIds = new HashMap();				
		while (rsGeom.next()){
			geomIds.put("geom", rsGeom.getString("geometria"));
			geomIds.put("srid", rsGeom.getString("srid"));
		}
		
		connectGeom.close();
		return geomIds;
		
	}
	
	private static ArrayList obtenerConsultasBBDD(String usuario) throws SQLException{
		ArrayList lstGeoRefData = new ArrayList();
		PreparedStatement ps=null;
		ResultSet rs=null;
		Connection connect;				
		connect=obtenerConexion(NOMBRE_BBDD_LOCALGIS);
				
		String sSQL = "SELECT * FROM consultasgeorreferenciaexterna WHERE usuario='"+usuario+"'";
		
		ps=connect.prepareStatement(sSQL);
		rs=ps.executeQuery();
		 
		while (rs.next()){
			ConsultaDatosBean consultaDatosBean = new ConsultaDatosBean();
			consultaDatosBean.setId(rs.getInt("id"));
			consultaDatosBean.setNombreConsulta(rs.getString("nombreconsulta"));
			consultaDatosBean.setDescripcion(rs.getString("descripcion"));
			consultaDatosBean.setUsuario(rs.getString("usuario"));
			consultaDatosBean.setNombre_bbdd_ext(rs.getString("nombre_bbdd_ext"));
			consultaDatosBean.setNombre_tabla_ext(rs.getString("nombre_tabla_ext"));
			consultaDatosBean.setMetodo_georeferencia(rs.getString("metodo_georeferencia"));
			consultaDatosBean.setTipo_geometria(rs.getString("tipo_geometria"));
			consultaDatosBean.setTabla_cruce(rs.getString("tabla_cruce"));
			consultaDatosBean.setCampo_georeferencia(rs.getString("campo_georeferencia"));
			consultaDatosBean.setCampos_mostrar(rs.getString("campos_mostrar"));
			consultaDatosBean.setCampo_etiqueta(rs.getString("campo_etiqueta"));
			consultaDatosBean.setFiltro_operador(rs.getString("filtro_operador"));
			consultaDatosBean.setFiltro_valor(rs.getString("filtro_valor"));
			consultaDatosBean.setPortal(rs.getString("portal"));
			
			
			lstGeoRefData.add(consultaDatosBean);
		}
		
		return lstGeoRefData;
	}
	
	public static ConsultaDatosBean[] obtenerConsultas(String usuario) throws SQLException{
		
		ArrayList lstGeoRefData = obtenerConsultasBBDD(usuario);
		ConsultaDatosBean[] consultaDatosBean = null;
		if(lstGeoRefData != null && !lstGeoRefData.isEmpty()){
			consultaDatosBean = new ConsultaDatosBean[lstGeoRefData.size()];
			for(int i=0; i<lstGeoRefData.size(); i++){
				consultaDatosBean[i] = (ConsultaDatosBean)lstGeoRefData.get(i);	
			}
		}
		else{
			consultaDatosBean = new ConsultaDatosBean[1];
			consultaDatosBean[0] = null;
		} 

		return consultaDatosBean;
	}
	
	public static boolean actualizarConsulta(ConsultaDatosBean[] consultaDatosBean) throws SQLException{
		
		PreparedStatement ps=null;
		ResultSet rs=null;
		Connection connect;				
		connect=obtenerConexion(NOMBRE_BBDD_LOCALGIS);
		
		String sSQL = "UPDATE consultasgeorreferenciaexterna SET " +
				"\"nombreconsulta\" = '" +consultaDatosBean[0].getNombreConsulta() + "'," +
				"\"descripcion\" = '" + consultaDatosBean[0].getDescripcion() + "'," +
				"\"usuario\" = '" + consultaDatosBean[0].getUsuario() + "'," +
				"\"nombre_bbdd_ext\" = '" + consultaDatosBean[0].getNombre_bbdd_ext() + "'," +
				"\"nombre_tabla_ext\" = '" + consultaDatosBean[0].getNombre_tabla_ext() + "'," +
				"\"metodo_georeferencia\" = '" + consultaDatosBean[0].getMetodo_georeferencia() + "'," +
				"\"tipo_geometria\" = '" + consultaDatosBean[0].getTipo_geometria() + "'," +
				"\"tabla_cruce\" = '" + consultaDatosBean[0].getTabla_cruce() + "'," +
				"\"campo_georeferencia\" = '" + consultaDatosBean[0].getCampo_georeferencia() + "'," +
				"\"campos_mostrar\" = '" + consultaDatosBean[0].getCampos_mostrar() + "'," +
				"\"campo_etiqueta\" = '" + consultaDatosBean[0].getCampo_etiqueta() + "'," +
				"\"filtro_operador\" = '" + consultaDatosBean[0].getFiltro_operador()  + "'," +
				"\"filtro_valor\" = '" + consultaDatosBean[0].getFiltro_valor() + "'," +
				"\"portal\" = '" + consultaDatosBean[0].getPortal() + "'" +
				" WHERE \"id\"="+ consultaDatosBean[0].getId();
 
		ps=connect.prepareStatement(sSQL);
		ps.executeUpdate();
		
		return true;
		
	}
	
	public static boolean guardarConsulta(ConsultaDatosBean[] consultaDatosBean) throws SQLException{
		 
		PreparedStatement ps=null;
		ResultSet rs=null;
		Connection connect;				
		connect=obtenerConexion(NOMBRE_BBDD_LOCALGIS);
		
		String sSQL = "INSERT INTO consultasgeorreferenciaexterna " +
				"(\"id\",\"nombreconsulta\",\"descripcion\",\"usuario\",\"nombre_bbdd_ext\",\"nombre_tabla_ext\",\"metodo_georeferencia\"," +
				"\"tipo_geometria\",\"tabla_cruce\",\"campo_georeferencia\",\"campos_mostrar\",\"campo_etiqueta\",\"filtro_operador\",\"filtro_valor\",\"portal\")" +
				" VALUES " +
				"("+
				"nextval('seq_consultasgeorreferenciaexterna'),"+
				"'" + consultaDatosBean[0].getNombreConsulta() + "',"+
				"'" + consultaDatosBean[0].getDescripcion() + "',"+
				"'" + consultaDatosBean[0].getUsuario() + "',"+
				"'" + consultaDatosBean[0].getNombre_bbdd_ext() + "',"+
				"'" + consultaDatosBean[0].getNombre_tabla_ext() + "',"+
				"'" + consultaDatosBean[0].getMetodo_georeferencia()+ "',"+
				"'" + consultaDatosBean[0].getTipo_geometria()+ "',"+
				"'" + consultaDatosBean[0].getTabla_cruce()+ "',"+
				"'" + consultaDatosBean[0].getCampo_georeferencia()+ "',"+
				"'" + consultaDatosBean[0].getCampos_mostrar()+ "',"+
				"'" + consultaDatosBean[0].getCampo_etiqueta()+ "',"+
				"'" + consultaDatosBean[0].getFiltro_operador()+ "',"+
				"'" + consultaDatosBean[0].getFiltro_valor()+ "'," +
				"'" + consultaDatosBean[0].getPortal()+ "'" +
				")";
				
		ps=connect.prepareStatement(sSQL);
		ps.executeUpdate();
		
		return true;
	}
	
	
	public static boolean borrarConsuta(int id) {
		boolean borrado = true;
		
		PreparedStatement ps=null;
		ResultSet rs=null;
		try{
			Connection connect;				
			connect=obtenerConexion(NOMBRE_BBDD_LOCALGIS);
					
			String sSQL = "DELETE FROM consultasGeorreferenciaExterna WHERE id="+id;
			
			ps=connect.prepareStatement(sSQL);
			ps.executeUpdate();

		}
		catch(SQLException sql){
			borrado = false;
		}
		
		return borrado;
	}


	public static RowBean[] obtenerDatos(String bbdd, String tabla, String[] nombre_campos, String[] tipo_campos, 
			String campos_ref, String[] campo_eleg, int valorInicial, int valorFinal, String tipoGeometria,
			String tablaCruceGeometria, 
			int iEPSGCode, FiltroData[] lstFiltro, String portal) throws SQLException
	{
		PreparedStatement ps=null;
		ResultSet rs=null;
				
		PreparedStatement psGeom=null;
		ResultSet rsGeom=null;
		
		ArrayList filas=new ArrayList();
		ArrayList nombresCampos=new ArrayList();
		
		int l=0;
		ColumnsBean colb=new ColumnsBean();
		RowBean[] fila=null;			
		
		String sSQL="";	
		String sSQL1="";				

		for(int valor=0;valor<nombre_campos.length;valor++){
			nombresCampos.add(nombre_campos[valor]);
		}
		
		StringBuffer campos=getConcatenatedString(nombresCampos,false);
		if(campos_ref.equals("Referencia Catastral") || campos_ref.equals("Direcciï¿½n Postal")){
			boolean tipo_dato=false;
			if(campos_ref.equals("Referencia Catastral")){				
				tipo_dato=true;
			}
			ArrayList almacenDeClavesParaConsulta = new ArrayList();			
			HashMap hashDatos = obtenerDatosTablaExterna_RefCatas_DirPos(ps, rs, bbdd,
					nombre_campos, tipo_campos, campos_ref, tabla, campos, campo_eleg,
					valorInicial, valorFinal, lstFiltro);
			almacenDeClavesParaConsulta = (ArrayList)hashDatos.get("CLAVES");
			filas = (ArrayList)hashDatos.get("FILAS");

			Hashtable geomIds = new Hashtable();
			Hashtable geomSrid =new Hashtable();
			if(!almacenDeClavesParaConsulta.isEmpty()){

				if(tipoGeometria.equals("Mostrar Puntos") || tipoGeometria.equals("Mostrar Geometrï¿½a Tabla") ){
					String campoGeometria = "asText(centroid(\"GEOMETRY\"))";
					sSQL1 = contruirSelectDatos(almacenDeClavesParaConsulta, campos_ref, tipo_dato, campoGeometria, portal);
				}
				else if(tipoGeometria.equals("Mostrar Geometrï¿½a Original")){
					String campoGeometria = "asText(\"GEOMETRY\")";
					sSQL1 = contruirSelectDatos(almacenDeClavesParaConsulta, campos_ref, tipo_dato, campoGeometria, portal);
				}

				Connection connectGeom;				
				connectGeom=obtenerConexion(NOMBRE_BBDD_LOCALGIS);
				psGeom=connectGeom.prepareStatement(sSQL1);
				rsGeom=psGeom.executeQuery();
				  	
				int index =0;
				while (rsGeom.next()){
					if(tipo_dato==true){
						if((rsGeom.getString("referencia_catastral") != null && !rsGeom.getString("referencia_catastral").equals(""))){
							if(tipoGeometria.equals("Mostrar Puntos") || tipoGeometria.equals("Mostrar Geometrï¿½a Original")){
								geomIds.put(rsGeom.getString("referencia_catastral"), buildWkt(rsGeom.getString("geometria"),String.valueOf(rsGeom.getInt("srid"))));
								geomSrid.put("srid_"+rsGeom.getString("referencia_catastral"),String.valueOf(rsGeom.getInt("srid")));
							}
							else if(tipoGeometria.equals("Mostrar Geometrï¿½a Tabla")){
								HashMap hashGeomInterseccion = obtenerGeometriaInterseccion(rsGeom.getString("referencia_catastral"), 
										tablaCruceGeometria,
										campos_ref);
								if(!hashGeomInterseccion.isEmpty()){
									geomIds.put(rsGeom.getString("referencia_catastral"), 
										buildWkt((String)hashGeomInterseccion.get("geom"),String.valueOf(rsGeom.getInt("srid"))));
									geomSrid.put("srid_"+rsGeom.getString("referencia_catastral"),String.valueOf(rsGeom.getInt("srid")));
								}
							}		
						}					
					}else{
						// direcion postal
						if((rsGeom.getString("id") != null && !rsGeom.getString("id").equals(""))){
							if(tipoGeometria.equals("Mostrar Puntos") || tipoGeometria.equals("Mostrar Geometrï¿½a Original")){
								if(portal !=null && !portal.equals("") &&!hash_idvia_codigoine.isEmpty()){
									Set set = hash_idvia_codigoine.keySet(); 
									Iterator iter = set.iterator();
									while (iter.hasNext()){
										String codigoine = (String)iter.next();
										String idVia = (String)hash_idvia_codigoine.get(codigoine);
									
										if(rsGeom.getString("id").equals(idVia)){
											geomIds.put(codigoine, buildWkt(rsGeom.getString("geometria"),String.valueOf(rsGeom.getInt("srid"))));
											geomSrid.put("srid_"+codigoine,String.valueOf(rsGeom.getInt("srid")));
										}
										
									}
									
								}
								else{
									geomIds.put(rsGeom.getString("id"), buildWkt(rsGeom.getString("geometria"),String.valueOf(rsGeom.getInt("srid"))));
									geomSrid.put("srid_"+rsGeom.getString("id"),String.valueOf(rsGeom.getInt("srid")));
								}
							
							}
							else if(tipoGeometria.equals("Mostrar Geometrï¿½a Tabla")){
								HashMap hashGeomInterseccion = obtenerGeometriaInterseccion(rsGeom.getString("id"), 
										tablaCruceGeometria,
										campos_ref);
								
								if(!hashGeomInterseccion.isEmpty()){
									geomIds.put(rsGeom.getString("id"), 
											buildWkt((String)hashGeomInterseccion.get("geom"),String.valueOf(rsGeom.getInt("srid"))));
									geomSrid.put("srid_"+rsGeom.getString("id"),String.valueOf(rsGeom.getInt("srid")));
								}	

							}
							
						}
					}		
					index ++;
				}		
				connectGeom.close();
			}
			Iterator filasIterator = filas.iterator();			  	
			ArrayList filasCompletas=new ArrayList();		
			while(filasIterator.hasNext()){
			  	RowBean actualRow = (RowBean)filasIterator.next();	
			  	if(actualRow.getKey()!=null){
			  		if(geomIds.get(actualRow.getKey())!= null)
				  		actualRow.setGeometryWkt((String)geomIds.get(actualRow.getKey()));
			  			actualRow.setSrid((String)geomSrid.get("srid_"+actualRow.getKey()));
			  	}			  				  	
			  	filasCompletas.add(actualRow);			  	
			}			  	
			//Convierto el arraylist en un rowbean para enviï¿½rselo a test.java
			  	
			fila = (RowBean[])filasCompletas.toArray(new RowBean[filasCompletas.size()]);			
			//connect.close();
			//connectGeom.close();			
		}
	
		else{			
			ArrayList columns = new ArrayList();
				 
			HashMap hashDatos = obtenerDatosTablaExterna_XY(ps, rs, bbdd, nombre_campos, tabla, campos, 
					tipo_campos, campo_eleg, valorInicial, valorFinal, tipoGeometria, iEPSGCode, lstFiltro);

			filas = (ArrayList)hashDatos.get("FILAS");
			
			if(tipoGeometria.equals("Mostrar Puntos") ){
				for(int h=0; h<filas.size();h++){
					
				}
					
				fila = (RowBean[])filas.toArray(new RowBean[filas.size()]);		
				
			}
			else if (tipoGeometria.equals("Mostrar Geometrï¿½a Tabla")){

				Connection connectGeom;				
				connectGeom=obtenerConexion(NOMBRE_BBDD_LOCALGIS);
				
				ArrayList lstActualRow = new ArrayList();
				
				for(int h=0; h<filas.size();h++){
					RowBean row = (RowBean)filas.get(h);
					ColumnsBean[] colbean=row.getColumns(); 
					String strPoint = row.getGeometryWkt();
					String sSql = "SELECT asText(TRANSFORM(\"GEOMETRY\","+iEPSGCode+")) AS geometria" +
							" FROM "+tablaCruceGeometria+" WHERE intersects("+tablaCruceGeometria+".\"GEOMETRY\",transform(GeometryFromText('"+strPoint+"',"+iEPSGCode+"),4230))";
					
					psGeom=connectGeom.prepareStatement(sSql);
					rsGeom=psGeom.executeQuery();
					 
					
					while (rsGeom.next()){
						
						String geometia = rsGeom.getString("geometria");
					
						RowBean actualRow = new RowBean();
						actualRow.setGeometryWkt(buildWkt(rsGeom.getString("geometria"),String.valueOf(iEPSGCode)));
						actualRow.setColumns(colbean);
						actualRow.setSrid(String.valueOf(iEPSGCode));
						lstActualRow.add(actualRow);
					}
					
				}
				
				fila = (RowBean[])lstActualRow.toArray(new RowBean[lstActualRow.size()]);		
				
				connectGeom.close();

			}
			
		}
	
		return fila;				
	}

	private static StringBuffer getConcatenatedString(ArrayList almacenDeClavesParaConsulta, boolean tipo_dato) {
		Iterator it = almacenDeClavesParaConsulta.iterator();
		StringBuffer result = new StringBuffer();
		while(it.hasNext()){
			if(tipo_dato==true){
				result.append("'"+it.next()+"'");
				if(it.hasNext())
					result.append(",");
			}else{
				result.append(it.next());
				if(it.hasNext())
					result.append(",");
			}			
		}
		return result;
	}

	private static String buildWkt(String string, String string2) {
		// TODO Completar metodo
		if(string == null || string.equals("null")){
			return "EMPTY";
		}
		else{
			return string+","+string2;
		}
	}
}