/**
 * CatastroActualizarPostgre.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.model.beans.Derecho;
import com.geopista.app.catastro.model.beans.DireccionLocalizacion;
import com.geopista.app.catastro.model.beans.IdBienInmueble;
import com.geopista.app.catastro.model.beans.Titular;
import com.geopista.server.administradorCartografia.ACException;
import com.geopista.server.administradorCartografia.PermissionException;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;


public class CatastroActualizarPostgre
{
	
 public Connection conn = null;


 
 public CatastroActualizarPostgre()
  {
      try
      {
        conn = getDBConnection();
      }
      catch(Exception e)
      {
        e.printStackTrace();
      }
  }

 public static Connection getDBConnection () throws SQLException
  {
    AppContext app =(AppContext) AppContext.getApplicationContext();
    Connection con=  app.getConnection();
    con.setAutoCommit(false);
    return con;
  }  

public String ActualizarUC (int ID, ArrayList UC, ArrayList UCTipo)
{
  try
  {

    PreparedStatement s = null;
    s = conn.prepareStatement("catedactualizaruc");
    Iterator Datos = UC.iterator();
    Iterator TipoDatos = UCTipo.iterator();
    int n =0;
    while (TipoDatos.hasNext())
    {
        n=n+1;
        switch (Integer.parseInt(TipoDatos.next().toString())){
        case 0:
           s.setInt(n,Integer.parseInt(Datos.next().toString()));
           break;
        case 1:
            s.setString(n,Datos.next().toString());
            break;
       case 2:
            break;
        default:
          break;
      }    
    }
   s.setInt(n+1,ID);
   s.executeUpdate();  
   conn.commit();
   s.close();
   conn.close();
  return "Correcto";
  }catch (Exception ex)
  {
    
    ex.printStackTrace();
      return "Error";
  }//catch  
 }
 
 public String ActualizarSubparcela (int ID, ArrayList Subparcela, ArrayList SubparcelaTipo)
{
  try
  {
 
    PreparedStatement s = null;
    s = conn.prepareStatement("catedactualizarsubparcela");
    Iterator Datos = Subparcela.iterator();
    Iterator TipoDatos = SubparcelaTipo.iterator();
    int n =0;
    while (TipoDatos.hasNext())
    {
        n=n+1;
        switch (Integer.parseInt(TipoDatos.next().toString())){
        case 0:
           s.setInt(n,Integer.parseInt(Datos.next().toString()));
           break;
        case 1:
            s.setString(n,Datos.next().toString());
            break;
        default:
          break;
      }    
    }
    s.setInt(n+1,ID);
   s.executeUpdate();  
   conn.commit();
   s.close();
   conn.close();
  return "Correcto";
  }catch (Exception ex)
  {
    
    ex.printStackTrace();
      return "Error";
  }//catch  
 }

 public String ActualizarConstruccion (String ID, ArrayList UC, ArrayList UCTipo)
{
  try
  {

    PreparedStatement s = null;
    s = conn.prepareStatement("catedactualizarconstruccion");
    Iterator Datos = UC.iterator();
    Iterator TipoDatos = UCTipo.iterator();
    int n =0;
    while (TipoDatos.hasNext())
    {
        n=n+1;
        switch (Integer.parseInt(TipoDatos.next().toString())){
        case 0:
           s.setInt(n,Integer.parseInt(Datos.next().toString()));
           break;
        case 1:
            s.setString(n,Datos.next().toString());
            break;
       case 2:
            break;
        default:
          break;
      }    
    }
   s.setString(n+1,ID);
   s.executeUpdate();  
   conn.commit();
   s.close();
   conn.close();
  return "Correcto";
  }catch (Exception ex)
  {
    ex.printStackTrace();
      return "Error";
  }//catch  
 }



  public String ActualizarCargo (int ID, ArrayList Subparcela, ArrayList SubparcelaTipo)
{
  try
  {
 
    PreparedStatement s = null;
    s = conn.prepareStatement("catedactualizarcargo");
    Iterator Datos = Subparcela.iterator();
    Iterator TipoDatos = SubparcelaTipo.iterator();
    int n =0;
    while (TipoDatos.hasNext())
    {
        n=n+1;
        switch (Integer.parseInt(TipoDatos.next().toString())){
        case 0:
           s.setInt(n,Integer.parseInt(Datos.next().toString()));
           break;
        case 1:
            s.setString(n,Datos.next().toString());
            break;
       case 2:
            break;
        default:
          break;
      }    
    }
   s.setInt(n+1,ID);
   s.executeUpdate();  
   conn.commit();
   s.close();
   conn.close();
  return "Correcto";
  }catch (Exception ex)
  {
  
    ex.printStackTrace();
      return "Error";
  }//catch  
 }


  public String ActualizarTitular (String nif, String refCatastral, String numOrden, String digito1, String digito2, ArrayList DatosTitular, ArrayList DatosTitularTipo)
  {

	  AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	  Object aux = null;

	  try
	  {  	
		  PreparedStatement s = null;
		  s = conn.prepareStatement("catedactualizartitular");
		  Iterator Datos = DatosTitular.iterator();
		  Iterator TipoDatos = DatosTitularTipo.iterator();
		  int n =0;
		  while (TipoDatos.hasNext())
		  {
			  aux=Datos.next();
			  n=n+1;

			  switch (Integer.parseInt(TipoDatos.next().toString())){

			  case 0:
				  if (!aux.equals("")){
					  s.setInt(n,Integer.parseInt((String)aux));
				  }else
				  {
					  s.setNull(n, 0);
				  }
				  break;

			  case 1:
				  s.setString(n,(String)aux);
				  break;

			  case 2: 
				  if (!aux.equals("") && aux!=null){
					  s.setFloat(n,Float.parseFloat((String)aux));
				  }else
				  {
					  s.setNull(n, 2);
				  }
				  break;        

			  case 3: //Provincia

				  if (aux!=null){

					  if (!aux.equals("")){
						  s.setString(n,(String)aux);
					  }
					  else{
						  return "Error-1";
					  }
				  }
				  else{
					  s.setNull(n,0);
				  }

				  break;

			  case 4: //Municipio
				  if (aux != null){

					  if (!aux.equals("")){
						  s.setString(n,(String)aux);
					  }
					  else{
						  return "Error-2";
					  }

				  }
				  else{
					  s.setNull(n,0);
				  }

				  break; 

			  case 5:

				  if(aux!=null && (aux instanceof DireccionLocalizacion)){

					  PreparedStatement s2 = null;
					  ResultSet r2 = null;
					  String idVia = null;

					  s2 = null;
					  r2=null;	           
					  s2 = conn.prepareStatement("catedbuscaridvia");

					  s2.setString(1, ((DireccionLocalizacion)aux).getTipoVia());
					  s2.setString(2, ((DireccionLocalizacion)aux).getNombreVia()); //nombre oficial del municipio
					  r2 = s2.executeQuery();  

					  while (r2.next())
					  {
						  idVia= r2.getString(1);             
					  }				   	    

					  if(idVia!=null){
						  s.setString(n,idVia);
					  }
					  else
					  {
						  s.setNull(n, 0);
					  }

					  s2.close();
					  r2.close(); 
				  }
				  else{
					  s.setNull(n,0);
				  }

				  break;
			  case 6:
				  if (!aux.equals("")){	
					  Date fecha = new Date((String)aux);
					  s.setDate(n, (java.sql.Date)fecha);
				  }else
				  {
					  s.setNull(n, 0);
				  }
				  break;
			  default:
				  break;
			  }    
		  }
		  s.setString(n+1, nif);
		  s.setString(n+2, refCatastral);
		  s.setString(n+3, numOrden);
		  s.setString(n+4, digito1);
		  s.setString(n+5, digito2);
		  s.executeUpdate();  
		  conn.commit();
		  s.close();
		  conn.close();
		  return aplicacion.getI18nString("catastro.titulares.mensaje.actualizar.correcto");

	  }


	  catch (SQLException ex)
	  {
		  ex.printStackTrace();
		  ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("SQLError.Titulo"), aplicacion.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
		  return "Error";

	  }
	  catch (NumberFormatException ex)
	  {
		  ex.printStackTrace();
		  ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("FormatoNumeroError.Titulo"), aplicacion.getI18nString("FormatoNumeroError.Aviso"), StringUtil.stackTrace(ex));
		  return "Error";
	  }

	  catch (Exception ex)
	  {	   
		  ex.printStackTrace();
		  return "Error";
	  }//catch  
  }

  
  public String ActualizarSujetoPasivo (ArrayList DatosTitular, ArrayList DatosTitularTipo)
  {
      int ID_Provincia=0;
      String ID_Municipio="";
      String aux="";
      
      AppContext app =(AppContext) AppContext.getApplicationContext();
      Blackboard Identificadores = app.getBlackboard();
      String idSujetoPasivo = Identificadores.get("ID_SujetoPasivo").toString(); 
      
      try
      {  	
        PreparedStatement s = null;
        //query=update sujetopasivo set id=?, nif=?, identificacion=?, primera_letra=?,primer_numero=?, segunda_letra=?, segundo_numero=?, kilometro=?, bloque=?, direccion_no_estructurada=?, escalera=?, planta=?, puerta=?, codigo_postal=?, apartadocorreos=?, codigo_delegacion_meh=?, codigomunicipioine=?, nombrevia=?, codigovia=?, codigoentidadmenor=?, tipovia=?, identificacionconyuge=?, nifconyuge=?, claveinterna=?, derechoprevalente=?, numerotitulares=?, tipotiulares=?, complementotitularidad=? where id=?
	    s = conn.prepareStatement("catedactualizarsujetopasivo");
	    Iterator Datos = DatosTitular.iterator();
	    Iterator TipoDatos = DatosTitularTipo.iterator();
	    int n =0;
	    while (TipoDatos.hasNext())
	    {
	        aux=Datos.next().toString();
	        n=n+1;
	        
	        switch (Integer.parseInt(TipoDatos.next().toString())){
	        
	        case 0:
	           if (!aux.equals("") && aux!=null){
	               s.setInt(n,Integer.parseInt(aux));
	           }else
	           {
	               s.setNull(n, 0);
	           }
	               break;
	           
	        case 1:
	            s.setString(n,aux);
	            break;
	            
	        case 2: 
	            if (!aux.equals("") && aux!=null){
	 	              s.setFloat(n,Float.parseFloat(aux));
	 	          }else
		           {
		               s.setNull(n, 2);
		           }
		            break;
	            
	        
		            
	        case 3: //Provincia
		           
		           if (aux!=null && !aux.equals("")){
		           
				        PreparedStatement s2 = null;
				   	    ResultSet r2 = null;
				   	    s2 = conn.prepareStatement("buscarProvinciaPorNombre");	    	    
				   	    s2.setString(1, aux.toUpperCase()); //nombre oficial de la provincia
				           r2 = s2.executeQuery();  
				   	    while (r2.next())
				   	    {
				   	           ID_Provincia= Integer.parseInt(r2.getString(1));             
				   	    }
				   	    	    	    
				   	    if (ID_Provincia!=0){
				   	        s.setInt(n,ID_Provincia);
				   	    }
				   	    else{
				   	        return "Error-1";
				   	    }
		           }
		           else{
		               s.setNull(n,0);
		           }
		               
		   	    break;
		           
		       case 4: //Municipio
		           if (aux!=null && !aux.equals("")){
			           
				        PreparedStatement s2 = null;
				   	    ResultSet r2 = null;
		           
			            s2 = null;
			            r2=null;	           
				   	    s2 = conn.prepareStatement("buscarMunicipioPorNombre");
				   	    
				   	    NumberFormat formatter = new DecimalFormat("00");	            
				        s2.setString(1, formatter.format(ID_Provincia));
				        s2.setString(2, aux.toUpperCase()); //nombre oficial del municipio	            
				        r2 = s2.executeQuery();  
				           
				   	    while (r2.next())
				   	    {
				   	           ID_Municipio= r2.getString(1);             
				   	    }
				   	    
				   	    if (!ID_Municipio.equals("")){
				   	        s.setString(n,ID_Municipio);
				   	    }
				   	    else{
				   	        return "Error-2";
				   	    }
				           
				   	    s2.close();
				   		r2.close(); 
		           }
		           else{
		               s.setNull(n,0);
		           }
				   	    
		        break;        
		            
	        
	        default:
	          break;
	      }    
	    }
	    s.setInt(n+1,Integer.parseInt(idSujetoPasivo)); 
	    s.executeUpdate();  
	    conn.commit();
	    s.close();
	    conn.close();
	    return app.getI18nString("catastro.sujetopasivo.mensaje.actualizar.correcto") +" " +idSujetoPasivo;
	   
	  }
      catch (NumberFormatException ex)
      {
        ex.printStackTrace();
        ErrorDialog.show(app.getMainFrame(), app.getI18nString("FormatoNumeroError.Titulo"), app.getI18nString("FormatoNumeroError.Aviso"), StringUtil.stackTrace(ex));
        return "Error";
      }
      catch (SQLException ex)
      {
        ex.printStackTrace();
        ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
        return "Error";
      }
      
      catch (Exception ex)
	  {
	
	    ex.printStackTrace();
        return "Error";
	  }//catch  
 }
  
  
  
  
public ArrayList Subparcelas(int Referencia)
{
  ArrayList Datos = new ArrayList(); 
  try
  {
 
    PreparedStatement s = null;
    ResultSet r = null;
    s = conn.prepareStatement("catedsubparcelasnumeroorden");
    s.setInt(1, Referencia);
    r = s.executeQuery();
    while (r.next())
    {
        Datos.add(r.getString("numero_orden"));   

    }
    
   s.close();
   r.close(); 
   conn.close();
   return Datos;
    
  }catch (Exception ex)
  {
    ex.printStackTrace();
    return null;
  }//catch  
}

public ArrayList DatosSubparcela (int ID, int ID_Parcela)
{
  ArrayList Datos = new ArrayList(); 
  try
  {

    PreparedStatement s = null;
    ResultSet r = null;
    Connection conn = getDBConnection ();
    s = conn.prepareStatement("catedsubparcelas");
    s.setInt(1, ID);
    s.setInt(2, ID_Parcela);
    r = s.executeQuery();  
    while (r.next())
    {
        Datos.add(r.getString("id_subparcela")); 
        Datos.add(r.getString("numero_orden")); 
        Datos.add(r.getString("longitud_fachada"));   
        Datos.add(r.getString("tipo_fachada"));      
        Datos.add(r.getString("superficie_elemento_suelo")); 
        Datos.add(r.getString("fondo_elemento_suelo")); 
        Datos.add(r.getString("codigo_tipo_valor")); 
        Datos.add(r.getString("numero_fachadas")); 
        Datos.add(r.getString("corrector_longitud_fachada")); 
        Datos.add(r.getString("corrector_forma_irregular")); 
        Datos.add(r.getString("corrector_desmonte_excesivo")); 
        Datos.add(r.getString("corrector_profundidad_firme")); 
        Datos.add(r.getString("corrector_fondo_excesivo")); 
        Datos.add(r.getString("corrector_superficie_distinta")); 
        Datos.add(r.getString("corrector_depreciacion_funcional")); 
        Datos.add(r.getString("corrector_situaciones_especiales")); 
        Datos.add(r.getString("corrector_uso_no_lucrativo")); 
        Datos.add(r.getString("corrector_apreciacion")); 
        Datos.add(r.getString("corrector_cargas_singulares")); 
        Datos.add(r.getString("agua")); 
        Datos.add(r.getString("electricidad")); 
        Datos.add(r.getString("alumbrado")); 
        Datos.add(r.getString("desmonte")); 
        Datos.add(r.getString("pavimentacion")); 
        Datos.add(r.getString("alcantarillado")); 
        Datos.add(r.getString("tipomovimiento")); 
        Datos.add(r.getString("annoexpediente")); 
        Datos.add(r.getString("referenciaexpediente")); 
    }     
   s.close();
   r.close(); 
   conn.close();
   return Datos;
    
  }catch (Exception ex)
  {
    ex.printStackTrace();
    return null;
  }//catch 

  
}

 public String AltaSubparcela (int ID, ArrayList Subparcela, ArrayList SubparcelaTipo)
{
  try
  {

    PreparedStatement s = null;
    ResultSet r = null;
    s = conn.prepareStatement("catedidsubparcela");
    r = s.executeQuery();  
    int ID_Nuevo=0;
    while (r.next())
    {
        ID_Nuevo= Integer.parseInt(r.getString(1).toString());             
    }
 
    s = null;
    r = null;
 
    s = null;
    r = null;
    s = conn.prepareStatement("catednuevasubparcela");
    s.setInt(1, ID_Nuevo+1);
    Iterator Datos = Subparcela.iterator();
    Iterator TipoDatos = SubparcelaTipo.iterator();
    int n =1;
    TipoDatos.next();
    Datos.next();
    while (TipoDatos.hasNext())
    {
        n=n+1;
        String Dat= Datos.next().toString();
      
        switch (Integer.parseInt(TipoDatos.next().toString())){
        case 0:
           s.setInt(n,Integer.parseInt(Dat));
           break;
        case 1:
            s.setString(n,Dat);
            break;
       case 2:
            break;
        default:
          break;
      }    
              
    }
    n=n+1;
    Calendar cal=Calendar.getInstance();
    s.setDate(n,new java.sql.Date(cal.getTime().getTime()));
    s.executeUpdate();  
    conn.commit();
    s.close();
    conn.close();
    return "Correcto";
  }catch (Exception ex)
  {
    
    ex.printStackTrace();
      return "Error";
  }//catch  
 }


public ArrayList UCs(int Referencia)
{
  ArrayList Datos = new ArrayList(); 
  try
  {
 
    PreparedStatement s = null;
    ResultSet r = null;
    Connection conn = getDBConnection ();
    s = conn.prepareStatement("catedcodigouc");
    s.setInt(1, Referencia);
    r = s.executeQuery();
    while (r.next())
    {
        Datos.add(r.getString("codigo_unidad"));   

    }
    
   s.close();
   r.close(); 
   conn.close();
   return Datos;
    
  }catch (Exception ex)
  {
  
    ex.printStackTrace();
    return null;
  }//catch  
}

public ArrayList DatosUC (String Numero, int ID_Parcela)
{
  ArrayList Datos = new ArrayList(); 
  int ID_Via=0;
  int ID_TramoVia=0;
  try
  {
 
    PreparedStatement s = null;
    ResultSet r = null;
    s = conn.prepareStatement("cateducs");
    s.setString(1, Numero);
    s.setInt(2, ID_Parcela);
    r = s.executeQuery();  
    
    while (r.next())
    {
        Datos.add(r.getString("id_unidadconstructiva")); 
        Datos.add(r.getString("codigo_unidad")); 
        Datos.add(r.getString("codigo_entidad_menor"));   
        Datos.add(r.getString("primer_numero"));      
        Datos.add(r.getString("primera_letra")); 
        Datos.add(r.getString("segundo_numero")); 
        Datos.add(r.getString("segunda_letra")); 
        Datos.add(r.getString("kilometro")); 
        Datos.add(r.getString("bloque")); 
        Datos.add(r.getString("direccion_no_estructurada")); 
        Datos.add(r.getString("codigo_postal")); 
        Datos.add(r.getString("anio_construccion")); 
        Datos.add(r.getString("longitud_fachada")); 
        Datos.add(r.getString("numero_fachadas")); 
        Datos.add(r.getString("corrector_estado_conservacion")); 
        Datos.add(r.getString("corrector_cargas_singulares")); 
        Datos.add(r.getString("indicador_uso_agrario")); 
        Datos.add(r.getString("corrector_longitud_fachada")); 
        Datos.add(r.getString("corrector_depreciacion_funcional")); 
        Datos.add(r.getString("corrector_situaciones_especiales")); 
        Datos.add(r.getString("corrector_uso_no_lucrativo")); 
        Datos.add(r.getString("agua")); 
        Datos.add(r.getString("electricidad")); 
        Datos.add(r.getString("alumbrado")); 
        Datos.add(r.getString("desmonte")); 
        Datos.add(r.getString("pavimentacion")); 
        Datos.add(r.getString("alcantarillado")); 
        Datos.add(r.getString("exactitud_anio_construccion")); 
        Datos.add(r.getString("annoexpediente")); 
        Datos.add(r.getString("referenciaexpediente")); 
        ID_TramoVia=Integer.parseInt(r.getString("id_tramo_via").toString());

    }     

    s = null;
    r = null;
    s = conn.prepareStatement("catedgetidvia");
    s.setInt(1, ID_TramoVia);
    r = s.executeQuery();
    while (r.next())
    {
        ID_Via=Integer.parseInt(r.getString("id_via").toString());             
    }
 
    s = null;
    r = null;
    s = conn.prepareStatement("catconsvia");
    s.setInt(1,ID_Via);
    r = s.executeQuery();
    while (r.next())
    {
        Datos.add(r.getString(1));   
        Datos.add(r.getString(2));
        Datos.add(r.getString(3));
                
    }
 
   s.close();
   r.close(); 
   conn.close();
   return Datos;
    
  }catch (Exception ex)
  {
   
    ex.printStackTrace();
    return null;
  }//catch 

  
}

 public String AltaUC (int ID, int ID_Parcela, ArrayList Subparcela, ArrayList SubparcelaTipo)
{
  try
  {

    PreparedStatement s = null;
    ResultSet r = null;
  
    s = conn.prepareStatement("catediduc");
    r = s.executeQuery();  
    int ID_Nuevo=0;
    while (r.next())
    {
        ID_Nuevo= Integer.parseInt(r.getString(1).toString());             
    }
 
    s = null;
    r = null;
   
    conn = getDBConnection ();
    s = conn.prepareStatement("catednuevauc");
    s.setInt(1, ID_Nuevo+1);
    s.setInt(2, ID_Parcela);
    Iterator Datos = Subparcela.iterator();
    Iterator TipoDatos = SubparcelaTipo.iterator();
    int n =2;
    TipoDatos.next();
    Datos.next();
    while (TipoDatos.hasNext())
    {
        n=n+1;
        switch (Integer.parseInt(TipoDatos.next().toString())){
        case 0:
           s.setInt(n,Integer.parseInt(Datos.next().toString()));
           break;
        case 1:
            s.setString(n,Datos.next().toString());
            break;
       case 2:
            break;
        default:
          break;
      }    
    }
    n=n+1;
    Calendar cal=Calendar.getInstance();
    s.setDate(n,new java.sql.Date(cal.getTime().getTime()));
   s.executeUpdate();  
   conn.commit();
   s.close();
   conn.close();
  return "Correcto";
  }catch (Exception ex)
  {
   
    ex.printStackTrace();
      return "Error";
  }//catch  
 }






public ArrayList Cons(int ID_Parcela)
{
    AppContext app =(AppContext) AppContext.getApplicationContext();
    Blackboard Identificadores = app.getBlackboard();
    
  ArrayList Datos = new ArrayList(); 
  try
  {
    
    String referencia="";
    
    PreparedStatement s = null;
    ResultSet r = null;
    s = conn.prepareStatement("catedgetreferencia");
    s.setInt(1, ID_Parcela);
    r = s.executeQuery();
    while (r.next())
    {
        referencia= r.getString("referencia_catastral");  
        Identificadores.put("ReferenciaCatastral", referencia);
        Datos.add(referencia); 
    }
    

   s.close();
   r.close(); 
   conn.close();
   return Datos;
    
  }catch (Exception ex)
  {
    
    ex.printStackTrace();
    return null;
  }//catch  
}

public ArrayList DatosConstrucciones (String cargo, int ID_Parcela)
{
  ArrayList Datos = new ArrayList(); 
  try
  {
   
    String referencia="";

    PreparedStatement s = null;
    ResultSet r = null;
    s = conn.prepareStatement("catedgetreferencia");
    s.setInt(1, ID_Parcela);
    r = s.executeQuery();
    while (r.next())
    {
        referencia= r.getString(1);    
    }
    s = null;
    r = null;

    s = conn.prepareStatement("catedconstrucciones");
    referencia=referencia + "000" + cargo;
  
    s.setString(1, referencia);
    r = s.executeQuery();  
    while (r.next())
    {
        Datos.add(r.getString("id_construccion")); 
        Datos.add(r.getString("codigo_modalidad_reparto"));         
        Datos.add(r.getString("bloque")); 
        Datos.add(r.getString("escalera"));   
        Datos.add(r.getString("planta"));      
        Datos.add(r.getString("puerta")); 
        Datos.add(r.getString("codigo_destino_dgc")); 
        Datos.add(r.getString("indicador_tipo_reforma")); 
        Datos.add(r.getString("anio_reforma")); 
        Datos.add(r.getString("superficie_total_local")); 
        Datos.add(r.getString("superficie_terrazas_local")); 
        Datos.add(r.getString("superficie_imputable_local")); 
        Datos.add(r.getString("tipologia_constructiva")); 
        Datos.add(r.getString("codigo_uso_predominante")); 
        Datos.add(r.getString("codigo_categoria_predominante")); 
        Datos.add(r.getString("codigo_tipo_valor")); 
        Datos.add(r.getString("corrector_apreciacion_economica")); 
        Datos.add(r.getString("annoexpediente")); 
        Datos.add(r.getString("referenciaexpediente")); 
        Datos.add(r.getString("indicador_local_interior")); 
        Datos.add(r.getString("corrector_vivienda")); 
    }     
   s.close();
   r.close(); 
   conn.close();
   return Datos;
    
  }catch (Exception ex)
  {
  
    ex.printStackTrace();
    return null;
  }//catch 

  
}

 public String AltaConstruccion (int ID_Parcela, String cargo, int ID_Unidad, ArrayList Subparcela, ArrayList SubparcelaTipo)
{
  String ID_Nuevo="";
  try
  {
    
    PreparedStatement s = null;
    ResultSet r = null;
    s = conn.prepareStatement("catedgetreferencia");
    s.setInt(1, ID_Parcela);
    r = s.executeQuery();  
    while (r.next())
    {
        ID_Nuevo= r.getString(1).toString() + cargo;             
    }
    s = null;
    r = null;

    s = conn.prepareStatement("catednuevaconstruccion");
    s.setString(1, ID_Nuevo);
    s.setInt(2, ID_Unidad);
    Iterator Datos = Subparcela.iterator();
    Iterator TipoDatos = SubparcelaTipo.iterator();
    int n =2;
    TipoDatos.next();
    Datos.next();
    while (TipoDatos.hasNext())
    {
        n=n+1;
        switch (Integer.parseInt(TipoDatos.next().toString())){
        case 0:
           s.setInt(n,Integer.parseInt(Datos.next().toString()));
           break;
        case 1:
            s.setString(n,Datos.next().toString());
            break;
       case 2:
            break;
        default:
          break;
      }    
           
    }
    n=n+1;
    Calendar cal=Calendar.getInstance();
    s.setDate(n,new java.sql.Date(cal.getTime().getTime()));
   s.executeUpdate();  
   conn.commit();
   s.close();
   conn.close();
  return "Correcto";
  }catch (Exception ex)
  {
  
    ex.printStackTrace();
      return "Error";
  }//catch  
 }








public ArrayList Cargos (String ID_Construccion)
{
  ArrayList Datos = new ArrayList(); 
  try
  {

    PreparedStatement s = null;
    ResultSet r = null;
    s = conn.prepareStatement("catedgetnumerocargo");
    s.setString(1, ID_Construccion);
    r = s.executeQuery();
    while (r.next())
    {
        Datos.add(r.getString("numero_cargo"));   
              
    }
    
   s.close();
   r.close(); 
   conn.close();
   return Datos;
    
  }catch (Exception ex)
  {
  
    ex.printStackTrace();
    return null;
  }//catch  
}

public ArrayList DatosCargos (String cargo, String ID_Construccion)
{
  ArrayList Datos = new ArrayList(); 
  int ID_Via=0;
  try
  {
 
    PreparedStatement s = null;
    ResultSet r = null;
    s = conn.prepareStatement("catedcargos");
    s.setString(1, cargo);
    s.setString(2, ID_Construccion);
    r = s.executeQuery();  

    while (r.next())
    {
        Datos.add(r.getString("id_cargo")); 
        Datos.add(r.getString("id_titular")); 
        Datos.add(r.getString("numero_cargo")); 
        Datos.add(r.getString("primer_caracter_control"));   
        Datos.add(r.getString("segundo_caracter_control"));      
        Datos.add(r.getString("numero_fijo_inmueble")); 
        Datos.add(r.getString("coeficiente_participacion")); 
        Datos.add(r.getString("codigo_entidad_menor")); 
        Datos.add(r.getString("primer_numero"));      
        Datos.add(r.getString("primera_letra")); 
        Datos.add(r.getString("segundo_numero")); 
        Datos.add(r.getString("segunda_letra")); 
        Datos.add(r.getString("kilometro")); 
        Datos.add(r.getString("bloque")); 
        Datos.add(r.getString("direccion_no_estructurada")); 
        Datos.add(r.getString("codigo_postal")); 
        Datos.add(r.getString("escalera"));   
        Datos.add(r.getString("planta"));      
        Datos.add(r.getString("puerta")); 
        Datos.add(r.getString("anio_valor_catastral")); 
        Datos.add(r.getString("valor_catastral")); 
        Datos.add(r.getString("valor_catastral_suelo")); 
        Datos.add(r.getString("valor_catastral_construccion")); 
        Datos.add(r.getString("base_liquidable")); 
        Datos.add(r.getString("clave_uso_dgc")); 
        Datos.add(r.getString("anio_ultima_revision")); 
        Datos.add(r.getString("anio_ultima_notificacion")); 
        Datos.add(r.getString("numero_ultima_notificacion")); 
        Datos.add(r.getString("superficie_elementos_constructivos")); 
        Datos.add(r.getString("superficie_suelo")); 
        Datos.add(r.getString("coeficiente_propiedad")); 
        Datos.add(r.getString("anio_finalizacion_valoracion")); 
        Datos.add(r.getString("indicador_tipo_propiedad")); 
        Datos.add(r.getString("annoexpediente")); 
        Datos.add(r.getString("referenciaexpediente")); 
      ID_Via=Integer.parseInt(r.getString("id_via").toString());
    }     
if (ID_Via==0) return Datos; // TODO: revisar

    s = null;
    r = null;
    s = conn.prepareStatement("catconsvia");
    s.setInt(1,ID_Via);
    r = s.executeQuery();
    while (r.next())
    {
        Datos.add(r.getString(1));   
        Datos.add(r.getString(2));
        Datos.add(r.getString(3));
        
    }
 
   s.close();
   r.close(); 
   conn.close();
   return Datos;
    
  }catch (Exception ex)
  {
    ex.printStackTrace();
    return null;
  }//catch 

  
}

 public String AltaCargo (String ID_Construccion, ArrayList Subparcela, ArrayList SubparcelaTipo)
{
  try
  {
//    String query ="select max(id_cargo) from cargos";
    PreparedStatement s = null;
    ResultSet r = null;
    s = conn.prepareStatement("catedidcargo");
    r = s.executeQuery();  
    int ID_Nuevo=0;
    while (r.next())
    {
        ID_Nuevo= Integer.parseInt(r.getString(1).toString());             
    }
 
    s = null;
    r = null;

    s = conn.prepareStatement("catednuevocargo");
    s.setInt(1, ID_Nuevo+1);
    s.setString(2, ID_Construccion);
    Iterator Datos = Subparcela.iterator();
    Iterator TipoDatos = SubparcelaTipo.iterator();
    int n =2;
    TipoDatos.next();
    Datos.next();
    while (TipoDatos.hasNext())
    {
        n=n+1;
        switch (Integer.parseInt(TipoDatos.next().toString())){
        case 0:
           s.setInt(n,Integer.parseInt(Datos.next().toString()));
           break;
        case 1:
            s.setString(n,Datos.next().toString());
            break;
       case 2:
            break;
        default:
          break;
      }    
    }
    n=n+1;
    Calendar cal=Calendar.getInstance();
    s.setDate(n,new java.sql.Date(cal.getTime().getTime()));
   s.executeUpdate();  
   conn.commit();
   s.close();
   conn.close();
  return "Correcto";
  }catch (Exception ex)
  {
   
    ex.printStackTrace();
      return "Error";
  }//catch  
 }


 public ArrayList IdTitular (int ID_Parcela)
 {
   ArrayList Datos = new ArrayList(); 
   try
   {

     PreparedStatement s = null;
     ResultSet r = null;
   
     s = conn.prepareStatement("catidtitularparcela");
     s.setInt(1, ID_Parcela);
     r = s.executeQuery();  
     while (r.next())
     {
         Datos.add(r.getString(1));          
     }        
     
     s.close();
     r.close(); 
     conn.close();

     return Datos;
     
   }
   catch (SQLException ex)
   {
     ex.printStackTrace();
     
     AppContext app =(AppContext) AppContext.getApplicationContext();
     ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
     return null;
      
   }
   
   catch (Exception ex)
   {
    
     ex.printStackTrace();
     return null;
   }//catch 
	 
	   
 } 
 
	 public ArrayList IdsTitulares (int ID_Parcela) throws PermissionException
	 {
		 String refCatastral = "";
		 ArrayList Datos = new ArrayList();
		 PreparedStatement s = null;
		 ResultSet r = null;
		 
		 try
		 {    
			 s = conn.prepareStatement("catedgetreferencia");
			 s.setString(1, new Integer(ID_Parcela).toString());
			 r = s.executeQuery();  
			 while (r.next())
			 {
				 refCatastral= r.getString(1).toString();   
				 AppContext app =(AppContext) AppContext.getApplicationContext();
				 Blackboard Identificadores = app.getBlackboard();
				 Identificadores.put("ReferenciaCatastral", refCatastral);         
			 }
			 
			 s = null;
			 r = null;	
	
			 s = conn.prepareStatement("catedtodostitulares");
			 s.setString(1, refCatastral);
			 r = s.executeQuery();  
			 while (r.next())
			 {	         
				 Datos.add(r.getString("nif")); 
			 }    
			 
			 return Datos;	
		 }	
		 catch (SQLException ex)
		 {
			 //ex.printStackTrace();
	
			 if (ex.getCause() instanceof ACException 
					 && ex.getCause().getCause() instanceof PermissionException)
			 {
				 throw (PermissionException)ex.getCause().getCause();
			 }
			 else
			 {
				 AppContext app =(AppContext) AppContext.getApplicationContext();
				 ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
				 Datos = null;
			 }     
			 return Datos;   
	
		 }      
		 catch (Exception ex)
		 {    
			 ex.printStackTrace();
			 return null;
		 }
		 finally{
			 try {
				 s.close();
				 } catch (Exception e) {}
			 try {
				 r.close();
				 } catch (Exception e) {
				 //e.printStackTrace();
			 }
			 try {
				 conn.close();
				 } catch (Exception e) {
				 //e.printStackTrace();
			 }			 
		 }	
	
	 }
	 
	 public ArrayList IdsRazonSocialTitulares (int ID_Parcela) throws PermissionException
	 {
		 String refCatastral = "";
		 ArrayList Datos = new ArrayList();
		 PreparedStatement s = null;
		 ResultSet r = null;
		 
		 try
		 {    
			 s = conn.prepareStatement("catedgetreferencia");
			 s.setString(1, new Integer(ID_Parcela).toString());
			 r = s.executeQuery();  
			 while (r.next())
			 {
				 refCatastral= r.getString(1).toString();   
				 AppContext app =(AppContext) AppContext.getApplicationContext();
				 Blackboard Identificadores = app.getBlackboard();
				 Identificadores.put("ReferenciaCatastral", refCatastral);         
			 }
			 
			 s = null;
			 r = null;	
	
			 s = conn.prepareStatement("catedtodostitulares");
			 s.setString(1, refCatastral);
			 r = s.executeQuery();  
			 while (r.next())
			 {	         
				 Datos.add(r.getString("nif") + " - " +r.getString("razon_social") ); 
			 }    
			 
			 return Datos;	
		 }	
		 catch (SQLException ex)
		 {
			 //ex.printStackTrace();
	
			 if (ex.getCause() instanceof ACException 
					 && ex.getCause().getCause() instanceof PermissionException)
			 {
				 throw (PermissionException)ex.getCause().getCause();
			 }
			 else
			 {
				 AppContext app =(AppContext) AppContext.getApplicationContext();
				 ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
				 Datos = null;
			 }     
			 return Datos;   
	
		 }      
		 catch (Exception ex)
		 {    
			 ex.printStackTrace();
			 return null;
		 }
		 finally{
			 try {
				 s.close();
				 } catch (Exception e) {}
			 try {
				 r.close();
				 } catch (Exception e) {
				 //e.printStackTrace();
			 }
			 try {
				 conn.close();
				 } catch (Exception e) {
				 //e.printStackTrace();
			 }			 
		 }	
	
	 }

 public Titular DatosTitular (String nif,String refBien)
 {

	 Titular titular = new Titular();

	 try
	 {

		 String refCatastro = refBien.substring(0,14);
		 String numeroOrden = refBien.substring(14, 18);
		 String digito1 = refBien.substring(18, 19);
		 String digito2 = refBien.substring(19, 20);
		 
		 PreparedStatement s = null;
		 ResultSet r = null;

		 s = conn.prepareStatement("catedtitular");
		 s.setString(1, nif);
		 s.setString(2, refCatastro);
		 s.setString(3, numeroOrden);
		 s.setString(4, digito1);
		 s.setString(5, digito2);
		 
		 r = s.executeQuery();  

		 while (r.next())
		 {

			 titular.setNif(r.getString("nif"));
			 titular.setRazonSocial(r.getString("razon_social"));
			 titular.setNifConyuge(r.getString("nif_conyuge"));			 
			 DireccionLocalizacion direccion = new DireccionLocalizacion();
			 direccion.setPrimeraLetra(r.getString("primera_letra"));
			 direccion.setPrimerNumero(r.getInt("primer_numero"));
			 direccion.setSegundaLetra(r.getString("segunda_letra"));
			 direccion.setSegundoNumero(r.getInt("segundo_numero"));
			 direccion.setKilometro(r.getDouble("kilometro"));
			 direccion.setBloque(r.getString("bloque"));
			 direccion.setDireccionNoEstructurada(r.getString("direccion_no_estructurada"));
			 direccion.setEscalera(r.getString("escalera"));
			 direccion.setPlanta(r.getString("planta"));
			 direccion.setPuerta(r.getString("puerta"));

             /* JAVIER si codigo_postal es tipo int
             if(r.getString("codigo_postal")!=null){
				 if(!r.getString("codigo_postal").equals("")){
					 direccion.setCodigoPostal(new Integer(r.getString("codigo_postal")).intValue());
				 }
				 else{
					 direccion.setCodigoPostal(0);
				 }
			 }
			 else{
				 direccion.setCodigoPostal(0);
			 }
             */
             direccion.setCodigoPostal(r.getString("codigo_postal"));

             PreparedStatement s2 = null;
			 ResultSet r2 = null;
			 NumberFormat formatter = new DecimalFormat("00");

			 direccion.setProvinciaINE(r.getString("codigo_provincia_ine"));
			 direccion.setMunicipioINE(r.getString("codigo_municipio_ine"));

			 if (r.getString("codigo_provincia_ine")!=null){
				 s2 = conn.prepareStatement("buscarnombreprovincia");				 
				 s2.setString(1, formatter.format(r.getInt("codigo_provincia_ine")));
				 r2 = s2.executeQuery();  
				 while (r2.next())
				 {
					 direccion.setNombreProvincia(r2.getString("nombreoficial"));                
				 }

				 s2 = null;
				 r2 = null;

				 if (r.getString("codigo_municipio_ine")!=null){

					 s2 = conn.prepareStatement("buscarnombremunicipio"); //id de provincia
					 s2.setString(1, formatter.format(r.getInt("codigo_provincia_ine"))); //id de provincia

					 formatter = new DecimalFormat("000");
					 s2.setString(2, formatter.format(Integer.parseInt(r.getString("codigo_municipio_ine")))); //id de municipio

					 r2 = s2.executeQuery();  

					 while (r2.next())
					 {
						 direccion.setNombreMunicipio(r2.getString("nombreoficial"));
					 }

				 }
				 else
				 {                
					 direccion.setNombreMunicipio("");
				 }

			 }
			 else{
				 direccion.setNombreProvincia("");
				 direccion.setNombreMunicipio("");
			 }

			 titular.setDomicilio(direccion);

			 s2.close();
			 r2.close(); 

			 s2 = conn.prepareStatement("buscartipovia");
			 s2.setString(1, r.getString("id_via"));
			 r2 = s2.executeQuery();  

			 while (r2.next())
			 {
				 direccion.setTipoVia(r2.getString("tipovianormalizadocatastro"));
				 direccion.setNombreVia(r2.getString("nombrecatastro"));      
			 }
			 s2.close();
			 r2.close(); 
			 //conn.close();        
		 }     

		 s.close();
		 r.close(); 
		 //conn.close();
		 
		 s = conn.prepareStatement("catedbuscarderecho");
		 s.setString(1, nif);
		 s.setString(2, refBien);
		 r = s.executeQuery();  
		 
		 Derecho derecho = new Derecho();

		 while (r.next())
		 {
			 derecho.setPorcentajeDerecho(r.getFloat("porcentaje_derecho"));
			 derecho.setCodDerecho(r.getString("codigo_derecho"));      
		 }
		 s.close();
		 r.close(); 
		 conn.close();       
		 		 
		 IdBienInmueble idBienInmueble = new IdBienInmueble();
		 idBienInmueble.setParcelaCatastral(refCatastro);
		 idBienInmueble.setNumCargo(numeroOrden);
		 idBienInmueble.setDigControl1(digito1);
		 idBienInmueble.setDigControl2(digito2);
		 derecho.setIdBienInmueble(idBienInmueble);
		 
		 titular.setDerecho(derecho);
		 
		 
		 return titular;

	 }
	 catch (SQLException ex)
	 {
		 ex.printStackTrace();

		 AppContext app =(AppContext) AppContext.getApplicationContext();
		 ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
		 return null;

	 }
	 catch (Exception ex)
	 {

		 ex.printStackTrace();
		 return null;
	 }//catch 

 }

public String AltaDerecho (String nif, String idBien, String codigoDerecho, Float porcentajeDerecho)
{
	AppContext app =(AppContext) AppContext.getApplicationContext();
	
	try
	{

		PreparedStatement s = null;
		ResultSet r = null;

		s = conn.prepareStatement("catedbuscarderecho");
		s.setString(1, nif);
		s.setString(2, idBien);
		r = s.executeQuery();  

		while (r.next()){
			//Actualizar datos de titular
			String Result = ActualizarDerecho(nif, idBien, codigoDerecho, porcentajeDerecho);
			return Result;
		}

		//Insertar nuevo titular

		s = null;
		r = null;

		s = conn.prepareStatement("catednuevoderecho");		
		s.setString(1, nif);
		s.setString(2, idBien);
		s.setString(3, codigoDerecho);
		s.setFloat(4, porcentajeDerecho.floatValue());
		s.executeUpdate();  
		conn.commit();
		s.close();
		conn.close();

		return "";

	}

	catch (SQLException ex)
	{
		ex.printStackTrace();
		ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
		return "Error";

	}
	catch (NumberFormatException ex)
	{
		ex.printStackTrace();
		ErrorDialog.show(app.getMainFrame(), app.getI18nString("FormatoNumeroError.Titulo"), app.getI18nString("FormatoNumeroError.Aviso"), StringUtil.stackTrace(ex));
		return "Error";
	}
	catch (Exception ex)
	{
		ex.printStackTrace();
		return "Error";
	}//catch  
}

 
 public String AltaSujetoPasivo (ArrayList DatosTitular, ArrayList DatosTitularTipo)
 {
     AppContext app =(AppContext) AppContext.getApplicationContext();
	 Blackboard Identificadores = app.getBlackboard(); 
     int ID_Provincia=0;
     String ID_Municipio="";
     String aux="";
      
 	  try
 	  {
 	   
 	    PreparedStatement s = null;
 	    ResultSet r = null;
 	  
 	    s = conn.prepareStatement("catedidsujetopasivo");
 	    r = s.executeQuery();  
 	    int ID_UltimoSujetoPasivo=0;
 	    while (r.next())
 	    {
 	        ID_UltimoSujetoPasivo= Integer.parseInt(r.getString(1).toString());             
 	    }
 	    
 	    
 	    s = null;
 	    r = null;
 	    
 	    s = conn.prepareStatement("catednuevosujetopasivo");
 	    s.setInt(1, ++ID_UltimoSujetoPasivo);
 	    Iterator Datos = DatosTitular.iterator();
 	    Iterator TipoDatos = DatosTitularTipo.iterator();
 	    int n =1;
 	    TipoDatos.next();
 	    Datos.next(); 
 	    while (TipoDatos.hasNext())
 	    {
 	        n=n+1;
 	        aux= Datos.next().toString();
 	        switch (Integer.parseInt(TipoDatos.next().toString())){
 	        case 0:
 	           if (!aux.equals("") && aux!=null){
	               s.setInt(n,Integer.parseInt(aux));
	           }else
	           {
	               s.setNull(n, 0);
	           }
	           break;
 	           
 	           
 	        case 1:
 	            s.setString(n,aux);
 	            break;
 	       case 2:
 	          if (!aux.equals("") && aux!=null){
 	              s.setFloat(n,Float.parseFloat(aux));
 	          }else
	           {
	               s.setNull(n, 2);
	           }
	            break;
 	       
 	      case 3: //Provincia
	           
	           if (aux!=null && !aux.equals("")){
	           
			        PreparedStatement s2 = null;
			   	    ResultSet r2 = null;
			   	    s2 = conn.prepareStatement("buscarProvinciaPorNombre");	    	    
			   	    s2.setString(1, aux.toUpperCase()); //nombre oficial de la provincia
			           r2 = s2.executeQuery();  
			   	    while (r2.next())
			   	    {
			   	           ID_Provincia= Integer.parseInt(r2.getString(1));             
			   	    }
			   	    	    	    
			   	    if (ID_Provincia!=0){
			   	        s.setInt(n,ID_Provincia);
			   	    }
			   	    else{
			   	        return "Error-1";
			   	    }
	           }
	           else{
	               s.setNull(n,0);
	           }
	               
	   	    break;
	           
	       case 4: //Municipio
	           if (aux!=null && !aux.equals("")){
		           
			        PreparedStatement s2 = null;
			   	    ResultSet r2 = null;
	           
		            s2 = null;
		            r2=null;	           
			   	    s2 = conn.prepareStatement("buscarMunicipioPorNombre");
			   	    
			   	    NumberFormat formatter = new DecimalFormat("00");	            
			        s2.setString(1, formatter.format(ID_Provincia));
			        s2.setString(2, aux.toUpperCase()); //nombre oficial del municipio	            
			        r2 = s2.executeQuery();  
			           
			   	    while (r2.next())
			   	    {
			   	           ID_Municipio= r2.getString(1);             
			   	    }
			   	    
			   	    if (!ID_Municipio.equals("")){
			   	        s.setString(n,ID_Municipio);
			   	    }
			   	    else{
			   	        return "Error-2";
			   	    }
			           
			   	    s2.close();
			   		r2.close(); 
	           }
	           else{
	               s.setNull(n,0);
	           }
			   	    
	        break;     
	            
 	       default:
 	         break;
 	       
 	       
 	      }    
 	      	      
 	   }
 	   
 	  
 	   s.executeUpdate();  
 	   
 	   s=null;
 	   s = conn.prepareStatement("catedactparcelasujetopasivo");
 	   s.setInt(1,ID_UltimoSujetoPasivo); //id_sujetopasivo
 	   s.setInt(2,Integer.parseInt(Identificadores.get("ID_Parcela").toString())); //id de la parcela
 	   s.executeUpdate();  
 	   
 	   conn.commit();
 	   s.close();
 	   conn.close();
 	  
 	   return String.valueOf(ID_UltimoSujetoPasivo);
 	   
 	  }
 	catch (SQLException ex)
 	{
 	   ex.printStackTrace();
 	   ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
 	   return "Error";
 	    
 	}
 	catch (NumberFormatException ex)
    {
      ex.printStackTrace();
      ErrorDialog.show(app.getMainFrame(), app.getI18nString("FormatoNumeroError.Titulo"), app.getI18nString("FormatoNumeroError.Aviso"), StringUtil.stackTrace(ex));
      return "Error";
    }
 	  
 	  catch (Exception ex)
 	  {
 	    ex.printStackTrace();
 	    return "Error";
 	  }//catch  
  }

 
 
 public String BajaSubparcela (int ID)
{
  try
  {

    PreparedStatement s = null;

    s = conn.prepareStatement("catedbajasubparcela");
    s.setString(1,"B");
    Calendar cal=Calendar.getInstance();
    s.setDate(2,new java.sql.Date(cal.getTime().getTime()));
    s.setInt(3,ID);
   s.executeUpdate();  
   conn.commit();
   s.close();
   conn.close();
  return "Correcto";
  }catch (Exception ex)
  {
      ex.printStackTrace();
      return "Error";
  }//catch  
 }

 public String BajaUC (int ID)
{
  try
  {
 
    PreparedStatement s = null;

    s = conn.prepareStatement("catedbajauc");
    s.setString(1,"B");
    Calendar cal=Calendar.getInstance();
    s.setDate(2,new java.sql.Date(cal.getTime().getTime()));
    s.setInt(3,ID);
   s.executeUpdate();  
   conn.commit();
   s.close();
   conn.close();
  return "Correcto";
  }catch (Exception ex)
  {
      ex.printStackTrace();
      return "Error";
  }//catch  
 }

public String BajaConstruccion (String ID)
{
  try
  {
  	
  //  String query = "Update construcciones set tipomovimineto=?, fecha_baja=? where ID_Construccion='" + ID + "'";
    PreparedStatement s = null;
    
    s = conn.prepareStatement("catedbajaconstruccion");
    s.setString(1,"B");
    Calendar cal=Calendar.getInstance();
    s.setDate(2,new java.sql.Date(cal.getTime().getTime()));
    s.setString(3,ID);
    s.executeUpdate();  
    conn.commit();
    s.close();
    conn.close();
    return "Correcto";
  }catch (Exception ex)
  {
      ex.printStackTrace();
      return "Error";
  }//catch  
 }
 
public String BajaCargo (int ID)
{
  try
  {
  	
    PreparedStatement s = null;

    s = conn.prepareStatement("catedbajacargo");
    s.setString(1,"B");
    Calendar cal=Calendar.getInstance();
    s.setDate(2,new java.sql.Date(cal.getTime().getTime()));
    s.setInt(3,ID);
   s.executeUpdate();  
   conn.commit();
   s.close();
   conn.close();
  return "Correcto";
  }catch (Exception ex)
  {
      ex.printStackTrace();
      return "Error";
  }//catch  
 }

public String BajaTitular (int ID)
{
	try
	{

		PreparedStatement s = null;
		s = conn.prepareStatement("catedbajatitular");
		s.setInt(1,ID);
		s.executeUpdate();  
		conn.commit();
		s.close();
		conn.close();
		return "OK";

	}
	catch (SQLException ex)
	{
		ex.printStackTrace();

		AppContext app =(AppContext) AppContext.getApplicationContext();
		ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
		return "Error";

	}
	catch (Exception ex)
	{

		ex.printStackTrace();
		return "Error";
	}//catch  
}

public String BajaTitular (String nif, String parcelaCatastro, String numeroCargo, String digito1, String digito2)
{
  try
  {
   
    PreparedStatement s = null;
    s = conn.prepareStatement("catedbajatitular");
    s.setString(1,nif);
    s.setString(2,parcelaCatastro);
    s.setString(3,numeroCargo);
    s.setString(4,digito1);
    s.setString(5,digito2);
    s.executeUpdate();  
    conn.commit();
    s.close();
    conn.close();
        
    s = conn.prepareStatement("catedbajaderecho");
    s.setString(1,nif);
    s.setString(2,parcelaCatastro+numeroCargo+digito1+digito2);
    s.executeUpdate();  
    conn.commit();
    s.close();
    conn.close();
    
    return "OK";
    
  }
  catch (SQLException ex)
  {
    ex.printStackTrace();
    
    AppContext app =(AppContext) AppContext.getApplicationContext();
    ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
    return "Error";
     
  }
  catch (Exception ex)
  {
   
    ex.printStackTrace();
      return "Error";
  } 
 }


public String BajaSujetoPasivo (int ID_Parcela)
{
  try
  {  	
      AppContext app =(AppContext) AppContext.getApplicationContext();
      Blackboard Identificadores = app.getBlackboard();
      String idSujetoPasivo = Identificadores.get("ID_SujetoPasivo").toString();  
      
      PreparedStatement s = null;
      s = conn.prepareStatement("catedbajasujetopasivo");
      s.setInt(1,Integer.parseInt(idSujetoPasivo));
      s.executeUpdate();  
      
      s=null;
      
      s = conn.prepareStatement("catedactparcelasujetopasivo");     
      s.setNull(1, 0); //id_sujetopasivo=null
      s.setInt(2,ID_Parcela);
      s.executeUpdate();  
      
      conn.commit();
      s.close();
      conn.close();
      return "OK";
  
  }
  catch (SQLException ex)
  {
    ex.printStackTrace();
    
    AppContext app =(AppContext) AppContext.getApplicationContext();
    ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
    return "Error";
     
  }  
  catch (Exception ex)
  {
      ex.printStackTrace();
      return "Error";
  }//catch  
 }




public ArrayList DatosSujetoPasivo (int ID_Parcela)
{
  String idSujetoPasivo = "0";
  ArrayList Datos = new ArrayList();
     
  try
  {

      PreparedStatement s = null;
      ResultSet r = null;
      s = conn.prepareStatement("catedgetsujetopasivo");
      s.setInt(1, ID_Parcela);
      r = s.executeQuery();  
      while (r.next())
      {
          
          idSujetoPasivo= r.getString(1);  
          AppContext app =(AppContext) AppContext.getApplicationContext();
          Blackboard Identificadores = app.getBlackboard();
          Identificadores.put("ID_SujetoPasivo", idSujetoPasivo);  
          
      }
      
           
      if(idSujetoPasivo==null){
          
          s.close();
          r.close(); 
          //hay 27 campos
          for (int i=0; i<27; i++){
              Datos.add("");
          }
          conn.close();
          return Datos;
      }
      
      s = null;
      r = null;
      s = conn.prepareStatement("catedsujetopasivo");
      s.setInt(1, Integer.parseInt(idSujetoPasivo));
      r = s.executeQuery();  
      
      while (r.next())
      {

          Datos.add(r.getString("nif"));     
          Datos.add(r.getString("identificacion")); 
          Datos.add(r.getString("primera_letra")); 
          Datos.add(r.getString("primer_numero")); 
          Datos.add(r.getString("segunda_letra"));
          Datos.add(r.getString("segundo_numero"));
          Datos.add(r.getString("kilometro")); 
          Datos.add(r.getString("bloque")); 
          Datos.add(r.getString("direccion_no_estructurada")); 
          Datos.add(r.getString("escalera"));   
          Datos.add(r.getString("planta"));      
          Datos.add(r.getString("puerta")); 
          Datos.add(r.getString("codigo_postal")); 
          Datos.add(r.getString("apartadocorreos")); 
          
          PreparedStatement s2 = null;
          ResultSet r2 = null;
          NumberFormat formatter = new DecimalFormat("00");
          
          
          if (r.getString("codigo_delegacion_meh")!=null){
              s2 = conn.prepareStatement("buscarnombreprovincia");
              s2.setString(1, formatter.format(r.getInt("codigo_delegacion_meh")));
              r2 = s2.executeQuery();  
              while (r2.next())
              {
                  Datos.add(r2.getString("nombreoficial")); 
              }
                     
              s2 = null;
              r2 = null;
          
              if (r.getString("codigomunicipioine")!=null){
                  
                  s2 = conn.prepareStatement("buscarnombremunicipio");
                  s2.setString(1, formatter.format(r.getInt("codigo_delegacion_meh"))); //id de provincia
                  
                  formatter = new DecimalFormat("000");
                  s2.setString(2, formatter.format(Integer.parseInt(r.getString("codigomunicipioine")))); //id de municipio
                  
                  r2 = s2.executeQuery();  
                  
                  while (r2.next())
                  {
                      Datos.add(r2.getString("nombreoficial")); 
                  }
                  
              }
              else
              {
                  Datos.add(""); //municipio
              }
          
          }
          else{
              Datos.add(""); //provincia
              Datos.add(""); //municipio
          }
          
          s.close();
          r.close(); 
          conn.close();
          
          Datos.add(r.getString("nombrevia")); 
          Datos.add(r.getString("codigovia"));
          Datos.add(r.getString("codigoentidadmenor"));
          Datos.add(r.getString("tipovia"));
          
          Datos.add(r.getString("identificacionconyuge"));
          Datos.add(r.getString("nifconyuge"));
                    
          Datos.add(r.getString("claveinterna"));
          Datos.add(r.getString("derechoprevalente"));
          Datos.add(r.getString("numerotitulares"));
          Datos.add(r.getString("tipotitulares"));
          Datos.add(r.getString("complementotitularidad"));
      }     

      s.close();
      r.close(); 
      conn.close();
      return Datos;
      
    }catch (Exception ex) 
    {
      ex.printStackTrace();
      return null;
    }//catch    
}

public String obtenerRefCatastral(int parcela) {
	
	try
	  {
		String refCatastral = null;
		PreparedStatement s = null;
	    ResultSet r = null;
	    s = conn.prepareStatement("catedgetsujetopasivo");
	    s.setInt(1, parcela);
	    r = s.executeQuery();  
	    while (r.next())
	    {
	        refCatastral = r.getString(1);  
	    }
	    s.close();
	    r.close(); 
	    conn.close();
	      
	    return refCatastral; 
	      
	  }catch (Exception ex)
	  {
	      ex.printStackTrace();
	      return null; 
	  }
	
}

public String cargarDigitoControl1 (String parcelaCatastral, String numCargo)
{
        
    try
	  {
		String DC1 = null;
		PreparedStatement s = null;
	    ResultSet r = null;
	    s = conn.prepareStatement("catedbuscardigitocontrol1");
	    s.setString(1,parcelaCatastral);
	    s.setString(2, numCargo);
	    r = s.executeQuery();  
	    while (r.next())
	    {
	        DC1 = r.getString(1);  
	    }
	    s.close();
	    r.close(); 
	    conn.close();
	      
	    return DC1; 
	      
	  }catch (Exception ex)
	  {
	      ex.printStackTrace();
	      return null; 
	  }
	
    
}

public String cargarDigitoControl2 (String parcelaCatastral, String numCargo)
{
        
    try
	  {
		String DC2 = null;
		PreparedStatement s = null;
	    ResultSet r = null;
	    s = conn.prepareStatement("catedbuscardigitocontrol2");
	    s.setString(1,parcelaCatastral);
	    s.setString(2, numCargo);
	    r = s.executeQuery();  
	    while (r.next())
	    {
	        DC2 = r.getString(1);  
	    }
	    s.close();
	    r.close(); 
	    conn.close();
	      
	    return DC2; 
	      
	  }catch (Exception ex)
	  {
	      ex.printStackTrace();
	      return null; 
	  }
	
    
}

public String AltaTitular (String nif, String parcelaCatastral, String numOrden, String digito1, String digito2, ArrayList DatosTitular, ArrayList DatosTitularTipo)
{
	AppContext app =(AppContext) AppContext.getApplicationContext();
	Object aux="";

	try
	{

		PreparedStatement s = null;
		ResultSet r = null;

		s = conn.prepareStatement("catedbuscartitular");
		s.setString(1, nif);
		s.setString(2, parcelaCatastral);
		s.setString(3, numOrden);
		s.setString(4, digito1);
		s.setString(5, digito2);
		r = s.executeQuery();  

		while (r.next()){
			//Actualizar datos de titular
			String Result = ActualizarTitular(nif, parcelaCatastral, numOrden, digito1, digito2, DatosTitular, DatosTitularTipo);
			return Result;
		}

		//Insertar nuevo titular

		s = null;
		r = null;

		s = conn.prepareStatement("catednuevotitular");
		Iterator Datos = DatosTitular.iterator();
		Iterator TipoDatos = DatosTitularTipo.iterator();
		int n =0;
		while (TipoDatos.hasNext())
		{
			n=n+1;		        
			aux= Datos.next();
			switch (Integer.parseInt(TipoDatos.next().toString())){
			case 0:
				if(aux!=null){
					if (!aux.equals("")){
						s.setInt(n,Integer.parseInt((String)aux));
					}else
					{
						s.setNull(n, 0);
					}
				}else{
					s.setNull(n, 0);
				}
				break;

			case 1:
				s.setString(n,(String)aux);
				break;

			case 2: 
				if (!aux.equals("") && aux!=null){
					s.setFloat(n,Float.parseFloat((String)aux));
				}else
				{
					s.setNull(n, 2);
				}
				break;	                    

			case 3: //Provincia

				if (aux!=null){

					if (!aux.equals("")){
						s.setString(n,(String)aux);
					}
					else{
						return "Error-1";
					}
				}
				else{
					s.setNull(n,0);
				}

				break;

			case 4: //Municipio
				if (aux != null){

					if (!aux.equals("")){
						s.setString(n,(String)aux);
					}
					else{
						return "Error-2";
					}

				}
				else{
					s.setNull(n,0);
				}

				break; 

			case 5:

				if(aux!=null && (aux instanceof DireccionLocalizacion)){

					PreparedStatement s2 = null;
					ResultSet r2 = null;
					String idVia = null;

					s2 = null;
					r2=null;	           
					s2 = conn.prepareStatement("catedbuscaridvia");

					s2.setString(1, ((DireccionLocalizacion)aux).getTipoVia());
					s2.setString(2, ((DireccionLocalizacion)aux).getNombreVia()); //nombre oficial del municipio
					r2 = s2.executeQuery();  

					while (r2.next())
					{
						idVia= r2.getString(1);             
					}				   	    

					if(idVia!=null){
						s.setString(n,idVia);
					}
					else
					{
						s.setNull(n, 0);
					}

					s2.close();
					r2.close(); 
				}
				else{
					s.setNull(n,0);
				}

				break;
			case 6:
				if(aux!=null){
				  if (!aux.equals("")){	
					  
					  Date fecha = new Date((String)aux);
					  java.sql.Date date= new java.sql.Date(fecha.getTime());
					  s.setDate(n, (java.sql.Date)date);
				  }else
				  {
					  s.setNull(n, 0);
				  }
				}
				else{
					 s.setNull(n, 0);
				}
				  break;
			default:
				break;
			}

		}

		s.setString(n+1, parcelaCatastral);
		s.setString(n+2, numOrden);
		s.setString(n+3, digito1);
		s.setString(n+4, digito2);
		s.executeUpdate();  
		conn.commit();
		s.close();
		conn.close();

		return "";

	}

	catch (SQLException ex)
	{
		ex.printStackTrace();
		ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
		return "Error";

	}
	catch (NumberFormatException ex)
	{
		ex.printStackTrace();
		ErrorDialog.show(app.getMainFrame(), app.getI18nString("FormatoNumeroError.Titulo"), app.getI18nString("FormatoNumeroError.Aviso"), StringUtil.stackTrace(ex));
		return "Error";
	}
	catch (Exception ex)
	{
		ex.printStackTrace();
		return "Error";
	}//catch  
}

public String ActualizarDerecho (String nif, String idBien, String codigoDerecho, Float porcentajeDerecho)
{

	  AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	  
	  try
	  {  	
		  PreparedStatement s = null;
		  s = conn.prepareStatement("catedactualizarderecho");
		  
		  s.setString(1, codigoDerecho);
		  s.setFloat(2, porcentajeDerecho.floatValue());
		  s.setString(3, nif);
		  s.setString(4, idBien);
		  
		  s.executeUpdate();  
		  conn.commit();
		  s.close();
		  conn.close();
		  return aplicacion.getI18nString("catastro.titulares.mensaje.actualizar.correcto");

	  }


	  catch (SQLException ex)
	  {
		  ex.printStackTrace();
		  ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("SQLError.Titulo"), aplicacion.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
		  return "Error";

	  }
	  catch (NumberFormatException ex)
	  {
		  ex.printStackTrace();
		  ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("FormatoNumeroError.Titulo"), aplicacion.getI18nString("FormatoNumeroError.Aviso"), StringUtil.stackTrace(ex));
		  return "Error";
	  }

	  catch (Exception ex)
	  {	   
		  ex.printStackTrace();
		  return "Error";
	  }//catch  
}



}