/**
 * GeopistaPlaneamientoInformesPostgreCon.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.planeamiento;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;

public class GeopistaPlaneamientoInformesPostgreCon
{
private AppContext aplicacion = (AppContext) AppContext.getApplicationContext();

/**
 * Metodo para abrir la conexion
 */
public Connection abrirConexion() throws SQLException
{
    Connection conn = null;
try {
     //Quitamos los drivers

      Enumeration e = DriverManager.getDrivers();
      Class.forName("com.geopista.sql.GEOPISTADriver");
      String sConn = aplicacion.getString("geopista.conexion.url");
      conn = DriverManager.getConnection(sConn);     
      AppContext app = (AppContext) AppContext.getApplicationContext();
      conn= app.getConnection();
      conn.setAutoCommit(false);
  } catch (Exception e ) {return null;}
  
  return conn;
 
}
  

/**
 * Método para obtener el identificador de un Ambito
 * @param String numeroAmbito, Ambito de Gestión
 * @return String ObtenerIdentificador del ámbito
 */

public String ObtenerIdentificador(String numeroAmbito)
{

  PreparedStatement ps =null;
  ResultSet rs = null;
  Connection conn=null;
  try
  {
     
//    String query = "select idambgest from ambitos_gestion where numero=" + numeroAmbito;
    String codigoparcela ="";
    conn = this.abrirConexion();
    ps =conn.prepareStatement("informeUrbanisitcoObtenerIdentificador");
    ps.setString(1,numeroAmbito);
  
    if (!ps.execute()){
      // System.out.println("no ejecuta");
    }else{
      rs= ps.getResultSet();
    }
    while (rs.next()){
       codigoparcela = rs.getString(1);      
    }
    return codigoparcela;
  } catch (Exception ex)
  {
    ex.printStackTrace();
    return null;
  }//catch  
  finally{
      aplicacion.closeConnection(conn,ps,null,rs);
  }

}


/**
 * Método que almacenara el número de planeamiento y la fecha de aprobación
 * @param int parcela
 * @return HasMap planeamientoReferencia, datos anteriormente indicados
 **/
 
public HashMap planeamientoReferencia(int parcela)
{
    HashMap planeamiento = new HashMap();
    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement ps=null;
  try
  {
    conn = this.abrirConexion();
    ps = conn.prepareStatement("informeUrbanisticoPlaneamientoReferencia");
    ps.setInt(1,parcela);
    ps.setInt(2,Integer.parseInt(aplicacion.getString(UserPreferenceConstants.DEFAULT_MUNICIPALITY_ID)));

    if (!ps.execute()){
      // System.out.println("no ejecuta");
    }else{
      rs= ps.getResultSet();
    }
   
     while (rs.next()){
      //ponemos los valores al informe 
     // 1. Planeamiento General
         if (!rs.getString("numero").equals("")){
              planeamiento.put("numero",rs.getString("numero")); 
              }else{
                planeamiento.put("numero"," "); 
              };
              //Fecha de aprobacion
              if (rs.getString("fechaprobacion")!=null && !rs.getString("fechaprobacion").equals("")){
                 planeamiento.put("fechaprobacion",rs.getString("fechaprobacion")); 
              }else{
                planeamiento.put("fechaprobacion"," "); 
              };
              try
              {
                  planeamiento.put("areaParcela",new Double(rs.getDouble("superficiesuelo")));
              }catch(Exception e)
              {
                  planeamiento.put("areaParcela",new Double(0));
              }
              
            }  
      return planeamiento;
  } catch (Exception ex)
  {
    ex.printStackTrace();
    return null;
  }//catch  
  finally{
      aplicacion.closeConnection(conn,ps,null,rs);
  }

}

/**
 * Método que obtiene el ámbito de planeamiento especifico de una parcela
 * @param int parcela 
 * @return String ambitoPlaneamientoEspecífico de la parcela
 */
public String ambitoPlaneamientoEspecifico(int parcela)
{

   String planeamiento = "";
   Connection conn=null;
   ResultSet rs=null;
   PreparedStatement ps=null;
   
  try
  {
     conn =abrirConexion();
     ps=conn.prepareStatement("informeUrbanisiticoAmbitoPlaneamientoEspecifico");
     ps.setInt(1,parcela);
   
      if (!ps.execute()){
        // System.out.println("no ejecuta");
      }else{
        rs= ps.getResultSet();
      }

      while (rs.next()){
          //ponemos los valores al informe 
         // 1. Planeamiento General
              if (rs.getString("numero")!=null&&!rs.getString("numero").equals("")){
                 planeamiento = rs.getString("numero"); 
              }else{
                planeamiento = "     "; 
              };
             
            }
      return planeamiento;
           
  }catch (Exception e){
  e.printStackTrace();
  return null;
  }
  finally{
      aplicacion.closeConnection(conn,ps,null,rs);
  }
}


/**
 * Método para obtener el Ámbito de Gestión de una parcela
 * @param int parcela
 * @retutn String nombre ambitoGestion
 */
public String ambitosGestion(int parcela)
{

   String planeamiento = "";
   Connection conn=null;
   ResultSet rs=null;
   PreparedStatement ps=null;

  try
  {
     
    conn = abrirConexion();             
    ps=conn.prepareStatement("informeUrbanisticoAmbitosdeGestion");
    ps.setInt(1,parcela);

    if(!ps.execute()){
    }else{
      rs=ps.getResultSet();
    }
    
    while (rs.next()){
                
              //ponemos los valores al informe 
              // 1. Planeamiento General
              if (rs.getString("numero")!=null&&!rs.getString("numero").equals("")){
                 planeamiento = rs.getString("numero"); 
              }else{
                planeamiento = "      "; 
              };
             
            }  
           
 
   return planeamiento;
    
  }catch (Exception ex)
  {
    ex.printStackTrace();
    return null;
  }//catch 

  finally{
      aplicacion.closeConnection(conn,ps,null,rs);
  }

}

/**
 * Método para recuperar el tipo de clasificacion y la categoria de la clasificación del suelo
 * @param int parcela
 * @return HasMap clasificacionSuelo
 */
public HashMap clasificacionSuelo(int parcela)
{
  HashMap clasificacionSuelo = new HashMap();
  Connection conn = null;
  ResultSet rs = null;
  PreparedStatement ps=null;
 
  try
  {
  conn = abrirConexion();
  ps = conn.prepareStatement("informeUrbanisticoClasificacionSuelo");
  ps.setInt(1,parcela);

  if (ps.execute()){
      rs=ps.getResultSet();  
  }              
  
            while (rs.next()){
                
              //ponemos los valores al informe 
              // 1. Clasificacion Suelo
              if (rs.getString("idclasiftipo")!=null&&!rs.getString("idclasiftipo").equals("")){
                clasificacionSuelo.put("idclasiftipo",rs.getString("idclasiftipo")); 
              }else{
                clasificacionSuelo.put("idclasiftipo","  ");
              };
              //Categoria
              if (rs.getString("categoria")!=null&&!rs.getString("categoria").equals("")){
                clasificacionSuelo.put("categoria",rs.getString("categoria")); 
              }else{
                clasificacionSuelo.put("categoria","  ");
              };
              
            }  
        
    return clasificacionSuelo;
    
  }catch (Exception ex)
  {
  
    ex.printStackTrace();
    return null;
  }//catch 

  finally {
      aplicacion.closeConnection(conn,ps,null,rs);
  }

}

/**
 * Método que devuelve la calificación del suelo, (Ordenanza,Altura máxima,Edificación,Ocupación y plantas de perimetro)
 * @parm int parcela
 * @return HasMap calificacionSuelo
 */

public HashMap calificacionSuelo(int parcela)
{
    HashMap calificacionSuelo = new HashMap();
    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement ps =null;

  try
  {
    conn =abrirConexion();
    ps = conn.prepareStatement("informeUrbanisitcoCalificacionSuelo");
    ps.setInt(1,parcela);

    if(ps.execute()){
      rs=ps.getResultSet();
    }
             while (rs.next()){
                
              //ponemos los valores al informe 
              // 1. Ordenanza
              if (rs.getString("ordenanza")!=null&&!rs.getString("ordenanza").equals("")){
                calificacionSuelo.put("ordenanza",rs.getString("ordenanza")); 
              }else{
                calificacionSuelo.put("idclasiftipo","  ");
              };
              //Edificacion
              if (rs.getString("edif")!=null&&!rs.getString("edif").equals("")){
                calificacionSuelo.put("edif",rs.getString("edif")); 
              }else{
                calificacionSuelo.put("edif","  ");
              };
                //Edificacion
              if (rs.getString("ocupacion")!=null&&!rs.getString("ocupacion").equals("")){
                calificacionSuelo.put("ocupacion",rs.getString("ocupacion")); 
              }else{
                calificacionSuelo.put("ocupacion","  ");
              };
                //Edificacion
              if (rs.getString("alt_max")!=null&&!rs.getString("alt_max").equals("")){
                calificacionSuelo.put("alt_max",rs.getString("alt_max")); 
              }else{
                calificacionSuelo.put("alt_max","  ");
              };
                //Edificacion
              if (rs.getString("plantaspermit")!=null&&!rs.getString("plantaspermit").equals("")){
                calificacionSuelo.put("plantaspermit",rs.getString("plantaspermit")); 
              }else{
                calificacionSuelo.put("plantaspermit","  ");
              };
             
            }  
        
   return calificacionSuelo;
    
  }catch (Exception ex)
  {

    ex.printStackTrace();
    return null;
  }//catch 

  finally {
      aplicacion.closeConnection(conn,ps,null,rs);
  }

}

/**
 * Método que devuelve una lista con las parcelas afectadas por un ambito de gestión
 * @param String numeroAmbito
 * @return ArrayList parcelasAfectadasAmbitoGestion
 */
public ArrayList parcelasAfectadasAmbitoGestion(String numeroAmbito)
{
    ArrayList resultado =new ArrayList();
    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement ps =null;
    
  try
  {
     conn = abrirConexion();
     ps=conn.prepareStatement("informeUrbanisticoparcelasAfectadasAmbitoGestion");
     ps.setString(1,numeroAmbito);

     if (ps.execute()){
      rs = ps.getResultSet();
     }
              
            while (rs.next()){

              //ponemos los valores al informe 
              // 1. Referencia Catastral

              resultado.add(rs.getString("referencia_catastral"));
            }  

   return resultado;
    
  }catch (Exception ex)
  {
    ex.printStackTrace();
    return null;
  }//catch 

  finally {
      aplicacion.closeConnection(conn,ps,null,rs);
  }
}


/**
 * Devuelve los numeros de Ambito de la base de datos
 * @return ArrayList
 */
public ArrayList leerNumeroAmbitos() throws Exception
{
    ArrayList planeamiento = new ArrayList();
    
    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement ps =null;    

     
        try {
            conn = abrirConexion();
            ps = conn.prepareStatement("informeUrbanisticoLeerNumeroAmbitos");
            ps.setString(1,aplicacion.getString(UserPreferenceConstants.DEFAULT_MUNICIPALITY_ID));
                    
            if (ps.execute()){
               rs = ps.getResultSet();
             }

            while (rs.next()){
                planeamiento.add(rs.getString("numero"));
            }
        return planeamiento;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        finally{
                aplicacion.closeConnection(conn,ps,null,rs);        
        }      
            

    

}


/**
 * Método para buscar el id del bien del patrimonio
 * @param String refpar
 * @param String refpla
 * @return ArrayList con los identificadores de los inmuebles
 */
public ArrayList buscarIdPatrimonio(String refpla,String refpar)
{
   ArrayList IdInmuebles= new ArrayList();
   Connection conn=null;
   ResultSet rs=null;
   PreparedStatement ps=null;
   
  try
  {
     conn =abrirConexion();
     ps=conn.prepareStatement("buscarIdPatrimonio");
     ps.setString(1,refpla);
     ps.setString(2,refpar);
   
      if (!ps.execute()){
        // System.out.println("no ejecuta");
      }else{
        rs= ps.getResultSet();
      }

      while (rs.next()){
          //ponemos los valores al informe 
         // 1. Planeamiento General
             IdInmuebles.add(rs.getString("id").toString());
            }
      return IdInmuebles;
               
  }catch (Exception e){
      e.printStackTrace();  
      return IdInmuebles;
  
  }
  finally{
      
      aplicacion.closeConnection(conn,ps,null,rs);
      
  }
}

 
/**
 * Método la superficie almacenada en la base de datos
 * @param String referenciaCatastral
 * @return String superficieInmueble
 */
public String superficieInmueble(String referencia)
{

   String planeamiento = "";
   Connection conn=null;
   ResultSet rs=null;
   PreparedStatement ps=null;
   
  try
  {
     conn =abrirConexion();
     ps=conn.prepareStatement("superficieDistintaInmuebles");
     ps.setString(1,referencia);
   
      if (!ps.execute()){
        // System.out.println("no ejecuta");
      }else{
        rs= ps.getResultSet();
      }

      while (rs.next()){
          //ponemos los valores al informe 
         // 1. Planeamiento General
            planeamiento=rs.getString("area");
            }
      return planeamiento;
           
  }catch (Exception e){
  e.printStackTrace();
  return null;
  }
  finally{
      aplicacion.closeConnection(conn,ps,null,rs);
  }
}
 


/**
 * Método que almacenara el número de planeamiento y la fecha de aprobación
 * @param int parcela
 * @return HasMap planeamientoReferencia, datos anteriormente indicados
 **/
 
public InformacionCatastralBean informacionCatastral(int parcela)
{
	InformacionCatastralBean planeamiento = new InformacionCatastralBean();
    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement ps=null;
    PreparedStatement psInmuebles=null;
    ResultSet rsInmuebles=null;
    
    String refCat="";
    double valorCatastral=0;
    double valorConstruccion=0;
    double valorSuelo=0;
    double superficieCons=0;
    double superficieSuelo=0;
    int numeroInmuebles=0;
    
    
  try
  {
    conn = this.abrirConexion();
    ps = conn.prepareStatement("consultaInformesParcela");
    ps.setInt(1,parcela);
    

    if (!ps.execute()){
      // System.out.println("no ejecuta");
    }else{
      rs= ps.getResultSet();
    }
   
     while (rs.next()){
     //obtenemos los valores 
     	//Referencia Catastral
     	if (rs.getString("referencia_catastral")!=""){
     		refCat=rs.getString("referencia_catastral");
     	}
        
     	//Valor Catastral
     	if (rs.getString("valorcatastral")!=null){
     		valorCatastral=rs.getDouble("valorcatastral");
     	}
        
     	//Valor Suelo
     	if (rs.getString("valorcatastralconstruccion")!=null) {
     		valorConstruccion=rs.getDouble("valorcatastralconstruccion");
     	}
     	
     	if (rs.getString("valorcatastralsuelo")!=null) {
     		valorSuelo=rs.getDouble("valorcatastralsuelo");
     	}
     	      
     	       
     	//Para esa referencia catastral hay que buscar todos los bienes
     	//que tiene asociado así como sus superficies en suelo y construcción.
     	
     	psInmuebles=conn.prepareStatement("consultaInformesParcelaTotales");
     	psInmuebles.setString(1,refCat.substring(0,7));
     	psInmuebles.setString(2,refCat.substring(7,14));
     	
     	if (psInmuebles.execute()){
     		rsInmuebles=psInmuebles.getResultSet();
     		if (rsInmuebles.next()){
     			//Obtener todos los datos de la tabla Inmuebles
     			 if (rsInmuebles.getString("numInmuebles")!=null){
     			 	numeroInmuebles=rsInmuebles.getInt("numInmuebles");
     			 }
     			 //Superficie Elementos construidos
     			 if (rsInmuebles.getString("suelo")!=null){
     			 	 superficieSuelo=rsInmuebles.getDouble("suelo"); 	
     			 }
     			
     			 //Superficie elemntos Construcción
     			 if (rsInmuebles.getString("elemento")!=null){
     			 	superficieCons=rsInmuebles.getDouble("elemento");
     			 }
     		    
     		    
     			
     		}
     	}
     	
     	
     	
     	planeamiento.setValorCatastral(valorCatastral);
     	planeamiento.setValorConstruccion(valorConstruccion);
     	planeamiento.setValorSuelo(valorSuelo);
     	planeamiento.setPlanoCatastral(refCat);
     	planeamiento.setSupElementosConstruccion(superficieCons);
     	planeamiento.setSupElementosSuelo(superficieSuelo);
     	planeamiento.setNumeroInmuebles(numeroInmuebles);
     	
	}  
      return planeamiento;
  } catch (Exception ex)
  {
    ex.printStackTrace();
    return null;
  }//catch  
  finally{
      aplicacion.closeConnection(conn,ps,null,rs);
      aplicacion.closeConnection(conn,psInmuebles,null,rsInmuebles);
  }

}

}


           









