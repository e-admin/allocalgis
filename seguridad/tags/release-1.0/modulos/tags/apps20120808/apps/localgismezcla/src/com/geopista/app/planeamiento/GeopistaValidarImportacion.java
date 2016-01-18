package com.geopista.app.planeamiento;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.feature.AttributeType;
import com.geopista.app.AppContext;
import com.vividsolutions.jump.util.Blackboard;
import com.geopista.util.ApplicationContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;
public class GeopistaValidarImportacion 
{
  public boolean correcto;
  private int i=0;
  private static ApplicationContext app = AppContext.getApplicationContext();  
  public Connection con = null;
  public String nombre;
  public String desc;
  public String nombreTabla;
  public String idDomi;
  public String nombreCampoDomi;
  //private StringBuffer textoEditor = new StringBuffer();
  public GeopistaValidarImportacion()
  {
      
  }
  
 /**
    * Realiza la conexión con la base de datos
    * @return Devuelve la conexión establecida con la base de datos
  */ 
  public static Connection getDBConnection () throws SQLException
  {
      Connection conn= app.getConnection();
      return conn;
  }
  public ArrayList getIDPlan(int municipio,Connection con)
  {
    ArrayList resultado = new ArrayList();
    try
    {
      ResultSet r = null;
     
      PreparedStatement ps = con.prepareStatement("idPlaneamiento");
      //ps.setLong(1, municipio);
      ps.setInt(1, municipio);

       if(!ps.execute())
      {
      }
      else
      {
        r  = ps.getResultSet();
        
        while( r.next())
        {
            resultado.add(r.getString("id"));
        }//del while
        app.closeConnection(null, ps,null,r);
      
      }

    }catch (Exception ex)
    {
        System.out.println("Excepción: ");
      ex.printStackTrace();
    }
        return resultado;
     
  }
  public ArrayList getNombrePlan(int municipio,Connection con)
  {
    ArrayList resultado = new ArrayList();
    try
    {
      ResultSet r = null;
     
      PreparedStatement ps = con.prepareStatement("nombreplaneamiento");
      ps.setInt(1, municipio);
     
       if(!ps.execute())
      {
      }
      else
      {
        r  = ps.getResultSet();
        
        while( r.next())
        {
            resultado.add(r.getString("nombre"));
        }//del while
        app.closeConnection(null, ps,null,r);
      
      }

    }catch (Exception ex)
    {
      ex.printStackTrace();
    }
        return resultado;
     
  }

/**
 * Este método busca los campos y tipos de datos de los mismos para validar que el fichero que intenta importar es válido
 * @param esquemaTabla el esquema localizado en el shapefile o en el fichero jml
 * @return true cuando la validación del fichero es válido y false cuando no lo es
 */
  
   public boolean encontrarDominios(StringBuffer textoEditor, FeatureSchema esquemaTabla, int tipoFichero)
   {
    int cuenta=0;
    String nCampo;
    for(i=0;i<esquemaTabla.getAttributeCount();i++)
    {
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("IDTIPGET"))
       {
        correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
        cuenta = cuenta + 1;  
         if (correcto == false)
        {
             textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
          
        }
       }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("IDTRAMIT"))
       {
        correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
        cuenta = cuenta + 1;  
         if (correcto == false)
        {
             textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
          
        }
       }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("NUMERO"))
       {
        correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
        cuenta = cuenta + 1; 
       
         if (correcto == false)
        {
             textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
          
        }
       }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("EJECUTADO"))
       {
        correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
        cuenta = cuenta + 1;  
        
         if (correcto == false)
        {
             textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
          
        }
       }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("NOMBRE"))
       {
        correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
        cuenta = cuenta + 1;  
        
         if (correcto == false)
        {
             textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
          
        }
       }
       if (tipoFichero == 0)
       {
           nCampo = "DESCRIPCION";
       }
       else
       {
           nCampo = "DESCRIPCIO";  
       }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals(nCampo))
       {
        correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
        cuenta = cuenta + 1;
        System.out.println("descripcion");
         if (correcto == false)
        {
             textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
          
        }
       }
   
  }
    if(cuenta != 6)
    {
      correcto = false;
      textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("faltanCampos")+ "</b></font></p>");
    }
   
    return correcto;
   }

/**
 * Este método comprueba que un campo del shapefile o del jml es de tipo entero
 * @param esquemaCampo, el esquema localizado en el shapefile o en el fichero jml
 * @return true si el campo es de tipo entero y false si no lo es
 */
  private boolean compruebaINTEGER(FeatureSchema esquemaCampo, String nombreAtributo)
  {
   if(esquemaCampo.getAttributeType(nombreAtributo)==AttributeType.INTEGER)
   {
    return true;
   }
   else
   {
    return false;
   }
  }

/**
 * Este método comprueba que un campo del shapefile o del jml es de tipo String
 * @param esquemaCampo, el esquema localizado en el shapefile o en el fichero jml
 * @return true si el campo es de tipo String y false si no lo es
 */

 private boolean compruebaSTRING(FeatureSchema esquemaCampo, String nombreAtributo)
  {
   if(esquemaCampo.getAttributeType(nombreAtributo)==AttributeType.STRING)
   {
    return true;
   }
   else
   {
    return false;
   }
  }
/**
 * Este método comprueba que un campo del shapefile o del jml es de tipo Fecha
 * @param esquemaCampo, el esquema localizado en el shapefile o en el fichero jml
 * @return true si el campo es de tipo Fecha y false si no lo es
 */

 private boolean compruebaDATE(FeatureSchema esquemaCampo, String nombreAtributo)
  {
   if(esquemaCampo.getAttributeType(nombreAtributo)==AttributeType.DATE)
   {
    return true;
   }
   else
   {
    return false;
   }
  }
/**
 * Este método comprueba que un campo del shapefile o del jml es de tipo Doble
 * @param esquemaCampo, el esquema localizado en el shapefile o en el fichero jml
 * @return true si el campo es de tipo Doble y false si no lo es
 */
  private boolean compruebaDOUBLE(FeatureSchema esquemaCampo, String nombreAtributo)
  {
   if(esquemaCampo.getAttributeType(nombreAtributo)==AttributeType.DOUBLE)
   {
    return true;
   }
   else
   {
    return false;
   }
  }

/**
 * 
 */
public String nombreDominio(long idDominio,Connection con)
{
 
   try
  {
    ResultSet r = null;
   
    PreparedStatement ps = con.prepareStatement("nombreDominio");
    ps.setLong(1, idDominio);

    if(!ps.execute())
    {
    }
    else
    {
      r  = ps.getResultSet();
      
    while(r.next())
    {
          nombre = r.getString(1);   
        
    }
  }
    app.closeConnection(con, ps,null,r);
    return nombre;
 
  }catch (Exception ex)
  {
    ex.printStackTrace();
    return null;
  }//catch 
}
   
  /**
   * Busca los subdominios correspondientes al dominio padre que se busca.
   * @param idDomain identificador del dominio del que se van a obtener los subdominios
   * @param con Conexion a la base de datos
   * @return Devuelve en un array los valores encontrados para ese dominio
   */
   
  public ArrayList leerDominios(long idDomain,Connection con)
  {
    ArrayList resultado = new ArrayList();

    try
    {
      ResultSet r = null;
      PreparedStatement ps = con.prepareStatement("seleccionDominios");
      ps.setLong(1,idDomain);
      
      ps.setString(2,app.getString(AppContext.PREFERENCES_LOCALE_KEY));
      if(!ps.execute())
      {
      }
      else
      {
        r  = ps.getResultSet();

        while (r.next())
        {
            resultado.add(r.getString(1));
        }
        app.closeConnection(con, ps,null,r);
      }
    }catch(Exception exc)
     {
          exc.printStackTrace();
     }
     return resultado;
     
  }
/** 
 *  Obtiene los identificadores de los valores del dominio de ambitos de gestion 
 *  @param idDomain identificador del dominio del que se van a obtener los subdominios
 *  @param con Conexion a la base de datos
 *  @return Devuelve en un array los identificadores de los subdominios
 * */
 public ArrayList leerDominiosID(long idDomain,Connection con)
  {
    ArrayList resultado = new ArrayList();

    try
    {
      ResultSet r = null;
      PreparedStatement ps = con.prepareStatement("seleccionIDValorDominio");
      ps.setLong(1,idDomain);
      if(!ps.execute())
      {
      }
      else
      {
        r  = ps.getResultSet();

        while (r.next())
        {
            resultado.add(r.getString(1));
        }
        app.closeConnection(con, ps,null,r);
      }
    }catch(Exception exc)
     {
          exc.printStackTrace();
     }
     return resultado;
     
  }

/**
 * Busca la descripción de un subdominio
 * @param con Conexión con la Base de Datos
 * @param idTipo identificador del tipo de gestión
 * @param idDominio dominio padre al que pertenece
 * @return Devuelve en un String la descripción del tipo de gestión. Devuelve null cuando se han producido errores o el registro esta vacio.
 */

public String getDescriTipoDom(String idTipo, long idDominio,Connection con)
{
   try
  {
    ResultSet r = null;
   
    PreparedStatement ps = con.prepareStatement("planeamientogestordescritipo");
    ps.setString(1, idTipo);
    ps.setLong(2, idDominio);
    ps.setString(3, app.getUserPreference(AppContext.GEOPISTA_LOCALE_KEY, "es_ES", false));
    

    if(!ps.execute())
    {
    }
    else
    {
      r  = ps.getResultSet();
      
    while(r.next())
    {
          desc = r.getString(1);   
        
    }
  }
    app.closeConnection(con, ps,null,r);
    return desc;
 
  }catch (Exception ex)
  {
    ex.printStackTrace();
    return null;
  }//catch 
}

 /*public ArrayList encontrarTabla(FeatureSchema esquema, int tipoFichero)
  {
    int i=0;
    ArrayList resultado = new ArrayList();
    
    correcto = compruebaCalificacion(esquema, tipoFichero);
    if (correcto == false)
    {
    	correcto = compruebaAmbitos(esquema, tipoFichero);
    	if (correcto == false)
        {
    		correcto = compruebaTablaPlaneamiento(esquema, tipoFichero);
    		if (correcto == false)
            {
    	         correcto = compruebaClasificacion(esquema, tipoFichero);
    	         if (correcto == false)
    	         {
    	            correcto = compruebaSistemasGenerales(esquema, tipoFichero);
    	            if (correcto == false)
    	            {
    	                correcto = false; //No es un fichero de Planeamiento Válido
    	            }
    	            else
    	            {
    	                nombreTabla = "sistemas_generales";
    	            }
    	         }
    	         else
    	         {
    	         	nombreTabla = "clasificacion_suelo";
    	         }
            }
    		else
    		{
    			nombreTabla = "tabla_planeamiento";
    		}
        }
    	else
    	{
    		nombreTabla = "ambitos_planeamiento";
    	}
    }
    else
    {
    	nombreTabla = "calificacion_suelo";
    }
    
    
    resultado.add(nombreTabla); 
    resultado.add(new Boolean(correcto));    
    return resultado;
    
  }
  */
 public boolean compruebaSistemasGenerales(StringBuffer textoEditor, FeatureSchema esquemaTabla, int tipoFichero)
 {
     int cuenta=0;
 	 for(i=0;i<esquemaTabla.getAttributeCount();i++)
 	 {
 	       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("IDUSOGLO"))
 	       {
 	        correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
 	        cuenta = cuenta + 1; 

 	        if (correcto == false)
 	        {
 	           textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
 	        }
 	
 	       }
 	       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("NUMERO"))
 	       {
 	         correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
 	         cuenta = cuenta + 1;
 	         if (correcto == false)
 	         {
 	            textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
 	         }
 	
 	       }
 	       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("ADSCRIP"))
 	       {
 	        correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
 	        cuenta = cuenta + 1;
 	       
 	        if (correcto == false)
 	        {
 	           textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
 	        }
 	
 	       }
 	       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("EDIFICABIL"))
 	       {
 	         correcto = compruebaDOUBLE(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
 	         cuenta = cuenta + 1; 
 	         if (correcto == false)
 	         {
 	            textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDouble") + " " +"</b></font></p>");
 	         }
 	
 	       }
 	       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("NOMBRE"))
 	       {
 	         correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
 	         cuenta = cuenta + 1; 
 	         if (correcto == false)
 	         {
 	            textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
 	         }
 	
 	       }
 	      if(esquemaTabla.getAttributeName(i).toUpperCase().equals("NORMATIVA"))
 	      {
 	         correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
 	         cuenta = cuenta + 1; 
 	         if (correcto == false)
 	         { 
 	            textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
 	         }
 	
 	      }
 	      if(esquemaTabla.getAttributeName(i).toUpperCase().equals("DESCRIPCIO"))
 	      {
 	        correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
 	        cuenta = cuenta + 1; 
 	       
 	        if (correcto == false)
 	        {
 	           textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
 	        }
 	
 	      }
 	   }
     if(cuenta != 7)
     {
         textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("faltanCampos")+ "</b></font></p>");

       correcto = false;
     }
    return correcto; 

 }
 public boolean compruebaCalificacion(StringBuffer textoEditor, FeatureSchema esquemaTabla, int tipoFichero)
  {
  
    int cuenta=0;
    if (tipoFichero == 0)
    {
	    for(i=0;i<esquemaTabla.getAttributeCount();i++)
	    {
	       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("ORDENANZA"))
	       {
	        correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	        cuenta = cuenta + 1; 
	        if (correcto == false)
	        {
	            textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");   
	        }
	
	       }
	       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("EDIF"))
	       {
	         correcto = compruebaDOUBLE(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	         cuenta = cuenta + 1; 
	         if (correcto == false)
	         {
	             textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDouble") + " " +"</b></font></p>");
	         }
	
	       }
	       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("EDIF_M2"))
	       {
	        correcto = compruebaDOUBLE(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	        cuenta = cuenta + 1;  
	        if (correcto == false)
	        {
	            textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDouble") + " " +"</b></font></p>");
	        }
	
	       }
	       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("OCUPACION"))
	       {
	         correcto = compruebaDOUBLE(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	         cuenta = cuenta + 1; 
	         if (correcto == false)
	         {
	             textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDouble") + " " +"</b></font></p>");
	         }
	
	       }
	       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("ALT_MAX"))
	       {
	         correcto = compruebaDOUBLE(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	         cuenta = cuenta + 1; 
	         if (correcto == false)
	         {
	             textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDouble") + " " +"</b></font></p>");
	         }
	
	       }
	      if(esquemaTabla.getAttributeName(i).toUpperCase().equals("PLANTASPERMIT"))
	      {
	         correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	         cuenta = cuenta + 1; 
	         if (correcto == false)
	         { 
	             textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
	         }
	
	      }
	      if(esquemaTabla.getAttributeName(i).toUpperCase().equals("FONDOMAX"))
	      {
	        correcto = compruebaDOUBLE(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	        cuenta = cuenta + 1; 
	        if (correcto == false)
	        {
	            textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDouble") + " " +"</b></font></p>");
	        }
	
	      }
	      if(esquemaTabla.getAttributeName(i).toUpperCase().equals("RETRANQ"))
	      {
	        correcto = compruebaDOUBLE(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	        cuenta = cuenta + 1; 
	        if (correcto == false)
	        {
	            textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDouble") + " " +"</b></font></p>");
	        }
	
	      }
	      if(esquemaTabla.getAttributeName(i).toUpperCase().equals("PATIOINTERIOR"))
	      {
	        correcto = compruebaDOUBLE(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	        cuenta = cuenta + 1; 
	        if (correcto == false)
	        {
	            textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDouble") + " " +"</b></font></p>");
	        }
	
	      }
	      if(esquemaTabla.getAttributeName(i).toUpperCase().equals("ATICOPERMISIBLE"))
	      {
	        correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	        cuenta = cuenta + 1; 
	        if (correcto == false)
	        {
	            textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
	        }
	
	      }
	      if(esquemaTabla.getAttributeName(i).toUpperCase().equals("NORMATIVAGENERAL"))
	      {
	        correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	        cuenta = cuenta + 1;
	        if (correcto == false)
	        {
	            textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
	        }
	
	      }
	      if(esquemaTabla.getAttributeName(i).toUpperCase().equals("DESCRIPCION"))
	      {
	        correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	        cuenta = cuenta + 1; 
	        if (correcto == false)
	        {
	            textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
	        }
	
	      }
	    }
    }
    else
    {
    	for(i=0;i<esquemaTabla.getAttributeCount();i++)
 	    {
    		/*if(esquemaTabla.getAttributeName(i).toUpperCase().equals("IDPLAN"))
 	       	{
    			correcto = compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
    			cuenta = cuenta + 1; 
    			if (correcto == false)
    			{
    				break;
    			}
 	       	}*/
 	      
 	       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("IDORDENANZ"))
 	       {
 	       		correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
 	       		cuenta = cuenta + 1; 
 	       		if (correcto == false)
 	       		{
 	       		textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
 	       		}
 	       }
 	       
 	       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("EDIF"))
 	       {
 	       		correcto = compruebaDOUBLE(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
 	       		cuenta = cuenta + 1; 
 	       		if (correcto == false)
 	       		{
 	       		textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDouble") + " " +"</b></font></p>");	
 	       		}
 	       }
 	       
 	       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("EDIF_M2"))
 	       {
 	       		correcto = compruebaDOUBLE(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
 	       		cuenta = cuenta + 1;  
 	       		if (correcto == false)
 	       		{
 	       		    textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDouble") + " " +"</b></font></p>");	
 	       		}
 	       }
 	       
 	       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("OCUPACION"))
 	       {
 	       		correcto = compruebaDOUBLE(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
 	       		cuenta = cuenta + 1; 
 	       		if (correcto == false)
 	       		{
 	       		    textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDouble") + " " +"</b></font></p>");
 	       		}
 	       }
 	       
 	       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("ALT_MAX"))
 	       {
	 	        correcto = compruebaDOUBLE(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	 	        cuenta = cuenta + 1; 
	 	        if (correcto == false)
	 	        {
	 	           textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDouble") + " " +"</b></font></p>");
	 	        }
 	       }
 	       
 	      if(esquemaTabla.getAttributeName(i).toUpperCase().equals("PLANTASPER"))
 	      {
 	      		correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
 	      		cuenta = cuenta + 1; 
 	      		if (correcto == false)
 	      		{ 
 	      		 textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
 	      		}
 	      }
 	      
 	      if(esquemaTabla.getAttributeName(i).toUpperCase().equals("FONDOMAX"))
 	      {
 	      		correcto = compruebaDOUBLE(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
 	      		cuenta = cuenta + 1; 
 	      		if (correcto == false)
 	      		{
 	      		 textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDouble") + " " +"</b></font></p>");	
 	      		}
 	      }
 	      
 	      if(esquemaTabla.getAttributeName(i).toUpperCase().equals("RETRANQ"))
 	      {
 	      		correcto = compruebaDOUBLE(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
 	      		cuenta = cuenta + 1; 
 	      		if (correcto == false)
 	      		{
 	      		 textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDouble") + " " +"</b></font></p>");	
 	      		}
 	      }
 	      
 	      if(esquemaTabla.getAttributeName(i).toUpperCase().equals("PATIOINT"))
 	      {
 	      		correcto = compruebaDOUBLE(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
 	      		cuenta = cuenta + 1; 
 	      		if (correcto == false)
 	      		{
 	      		 textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDouble") + " " +"</b></font></p>");	
 	      		}
 	      }
 	      
 	      if(esquemaTabla.getAttributeName(i).toUpperCase().equals("ATICOPERMI"))
 	      {
 	      		correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
 	      		cuenta = cuenta + 1; 
 	      		if (correcto == false)
 	      		{
 	      		 textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");	
 	      		}
 	      }
 	      
 	      if(esquemaTabla.getAttributeName(i).toUpperCase().equals("NORMATIVA"))
 	      {
 	      		correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
 	      		cuenta = cuenta + 1;
 	      		if (correcto == false)
 	      		{
 	      		 textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");	
 	      		}
 	      }
 	      
 	      if(esquemaTabla.getAttributeName(i).toUpperCase().equals("DESCRIPCIO"))
 	      {
 	      		correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
 	      		cuenta = cuenta + 1; 
 	      		if (correcto == false)
 	      		{
 	      		 textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");	
 	      		}
 	      }
 	    }
    }
    if(cuenta != 12)
    {
        textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("faltanCampos")+ "</b></font></p>");

      correcto = false;
    }
   return correcto; 
  }
   public boolean compruebaAmbitos(StringBuffer textoEditor, FeatureSchema esquemaTabla, int tipoFichero)
  {
  int cuenta=0;
  if (tipoFichero == 0)
  {
  
    for(i=0;i<esquemaTabla.getAttributeCount();i++)
    {
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("IDTRAMIT"))
       {
        correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
        cuenta = cuenta + 1; 
        if (correcto == false)
        {
          textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
        }
       }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("IDAMBPLANETIPO"))
       {
        correcto = compruebaSTRING(esquemaTabla,esquemaTabla.getAttributeName(i).toString());
        cuenta = cuenta + 1; 
         if (correcto == false)
        {
            textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
        }
       }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("IDUSOGLO"))
       {
         correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
         cuenta = cuenta + 1; 
         if (correcto == false)
         {
              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
        
         }
       }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("IDCLASIF"))
       {
        correcto = compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
        cuenta = cuenta + 1;  
        
         if (correcto == false)
        {
             textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoInteger") + " " + "</b></font></p>");
          
        }
       }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("NUMERO"))
       {
         correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
         cuenta = cuenta + 1; 
          if (correcto == false)
         {
              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
          
        }
       }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("NOMBRE"))
       {
         correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
         cuenta = cuenta + 1; 
          if (correcto == false)
        {
              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
         
        }
       }
      if(esquemaTabla.getAttributeName(i).toUpperCase().equals("INDICE_EDIFICABILIDAD"))
      {
         correcto = compruebaDOUBLE(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
         cuenta = cuenta + 1; 
          if (correcto == false)
        {
              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDouble") + " " +"</b></font></p>");
        
        }
      }
      if(esquemaTabla.getAttributeName(i).toUpperCase().equals("EDIFICABILIDAD_M2"))
      {
        correcto = compruebaDOUBLE(esquemaTabla,esquemaTabla.getAttributeName(i).toString());
        cuenta = cuenta + 1; 
         if (correcto == false)
        {
             textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDouble") + " " +"</b></font></p>");
         
        }
      }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("ASUMIDO"))
      {
        correcto = compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
        cuenta = cuenta + 1; 
         if (correcto == false)
        {
             textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoInteger") + " " + "</b></font></p>");
          
        }
      }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("NORMATIVA"))
       {
         correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
         cuenta = cuenta + 1; 
          if (correcto == false)
        {
              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
          
        }
       }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("DESCRIPCION"))
       {
         correcto = compruebaSTRING(esquemaTabla,esquemaTabla.getAttributeName(i).toString());
         cuenta = cuenta + 1; 
          if (correcto == false)
        {
              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
          
        }
       }
    }
  }
  else
  {
  	
  	 for(i=0;i<esquemaTabla.getAttributeCount();i++)
     {
  	   
        if(esquemaTabla.getAttributeName(i).toUpperCase().equals("IDTRAMIT"))
        {
         correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
         cuenta = cuenta + 1; 
          if (correcto == false)
         {
              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
           
         }
         }
        if(esquemaTabla.getAttributeName(i).toUpperCase().equals("IDAMBPLAN"))
        {
         correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
         cuenta = cuenta + 1; 
          if (correcto == false)
         {
              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
           
         }
        }
        if(esquemaTabla.getAttributeName(i).toUpperCase().equals("IDUSOGLO"))
        {
          correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
          cuenta = cuenta + 1; 
           if (correcto == false)
         {
               textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
           
         }
        }
        if(esquemaTabla.getAttributeName(i).toUpperCase().equals("IDCLASIF"))
        {
         correcto = compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
         cuenta = cuenta + 1;  
          if (correcto == false)
         {
              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoInteger") + " " + "</b></font></p>");
           
         }
        }
        if(esquemaTabla.getAttributeName(i).toUpperCase().equals("NUMERO"))
        {
          correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
          cuenta = cuenta + 1; 
           if (correcto == false)
         {
               textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
           
         }
        }
        if(esquemaTabla.getAttributeName(i).toUpperCase().equals("NOMBRE"))
        {
          correcto = compruebaSTRING(esquemaTabla,esquemaTabla.getAttributeName(i).toString());
          cuenta = cuenta + 1; 
           if (correcto == false)
         {
               textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
           
         }
        }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("INDICEEDIF"))
       {
          correcto = compruebaDOUBLE(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
          cuenta = cuenta + 1; 
           if (correcto == false)
         {
               textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDouble") + " " +"</b></font></p>");
           
         }
       }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("EDIFICABIL"))
       {
         correcto = compruebaDOUBLE(esquemaTabla,esquemaTabla.getAttributeName(i).toString());
         cuenta = cuenta + 1; 
          if (correcto == false)
         {
              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDouble") + " " +"</b></font></p>");
           
         }
       }
        if(esquemaTabla.getAttributeName(i).toUpperCase().equals("ASUMID"))
       {
         correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
         cuenta = cuenta + 1; 
          if (correcto == false)
         {
              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
           
         }
       }
        if(esquemaTabla.getAttributeName(i).toUpperCase().equals("NORMATIVA"))
        {
          correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
          cuenta = cuenta + 1; 
           if (correcto == false)
         {
               textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
           
         }
        }
        if(esquemaTabla.getAttributeName(i).toUpperCase().equals("DESCRIPCIO"))
        {
          correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
          cuenta = cuenta + 1; 
           if (correcto == false)
         {
               textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
           
         }
        }
    
     }

  }
  	if (cuenta!=11)
  	{
        textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("faltanCampos")+ "</b></font></p>");
  		correcto = false;
  	}
  
   return correcto; 
  }
  
   public boolean compruebaTablaPlaneamiento(StringBuffer textoEditor, FeatureSchema esquemaTabla, int tipoFichero)
  {
  int cuenta=0;
  correcto = false;
  
  if (tipoFichero == 0)
  {
    for(i=0;i<esquemaTabla.getAttributeCount();i++)
    {
       
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("TIPOPLAN"))
       {
        correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
        cuenta = cuenta + 1; 
         if (correcto == false)
        {
             textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
        }
       }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("NOMBRE"))
       {
         correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
         cuenta = cuenta + 1; 
          if (correcto == false)
        {
              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
        }
       }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("NUMERO"))
       {
         correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
         cuenta = cuenta + 1; 
          if (correcto == false)
        {
              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
        }
       }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("FECHAAPROBACION"))
       {
         correcto = compruebaDATE(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
         cuenta = cuenta + 1; 
          if (correcto == false)
        {
              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDate") + " " +"</b></font></p>");
        }
       }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("DESCRIPCION"))
       {
         correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
         cuenta = cuenta + 1; 
          if (correcto == false)
        {
              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
        }
       }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("FECHAALTA"))
       {
         correcto = compruebaDATE(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
         cuenta = cuenta + 1; 
          if (correcto == false)
        {
              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDate") + " " +"</b></font></p>");
        }
       }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("FECHABAJA"))
       {
         correcto = compruebaDATE(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
         cuenta = cuenta + 1; 
          if (correcto == false)
        {
              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDate") + " " +"</b></font></p>");
        }
       }
    }
  }
  else
  {
  	for(i=0;i<esquemaTabla.getAttributeCount();i++)
    {
     
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("TIPOPLAN"))
       {
        correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
        cuenta = cuenta + 1; 
         if (correcto == false)
        {
             textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
        }
       }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("NOMBRE"))
       {
         correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
         cuenta = cuenta + 1; 
          if (correcto == false)
        {
              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
        }
       }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("NUMERO"))
       {
         correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
         cuenta = cuenta + 1; 
          if (correcto == false)
        {
              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
        }
       }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("FECHAAPROB"))
       {
         correcto = compruebaDATE(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
         cuenta = cuenta + 1; 
          if (correcto == false)
        {
              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDate") + " " +"</b></font></p>");
        }
       }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("DESCRIPCIO"))
       {
         correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
         cuenta = cuenta + 1; 
          if (correcto == false)
        {
              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
        }
       }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("FECHAALTA"))
       {
         correcto = compruebaDATE(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
         cuenta = cuenta + 1; 
          if (correcto == false)
        {
              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDate") + " " +"</b></font></p>");
        }
       }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("FECHABAJA"))
       {
         correcto = compruebaDATE(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
         cuenta = cuenta + 1; 
          if (correcto == false)
        {
              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDate") + " " +"</b></font></p>");
        }
       }
    }
    
  }

    if(cuenta != 7)
    {
        textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("faltanCampos")+ "</b></font></p>");

      correcto = false;
    }
   return correcto; 
  }
   public boolean compruebaClasificacion(StringBuffer textoEditor, FeatureSchema esquemaTabla, int tipoFichero)
  {
  int cuenta=0;
  if(tipoFichero == 0)
  {
    for(i=0;i<esquemaTabla.getAttributeCount();i++)
    {
     
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("IDUSOGLO"))
       {
        correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
        cuenta = cuenta + 1; 
         if (correcto == false)
        {
             textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
        }
       }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("IDCLASIFTIPO"))
       {
         correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
         cuenta = cuenta + 1; 
          if (correcto == false)
        {
              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
        }
       }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("CATEGORIA"))
       {
        correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
        cuenta = cuenta + 1; 
        if (correcto == false)
        {
            textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
        }
       }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("NUMERO"))
       {
        correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
        cuenta = cuenta + 1; 
        if (correcto == false)
        {
            textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
        }
       }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("NORMATIVA"))
       {
        correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
        cuenta = cuenta + 1;  
         if (correcto == false)
        {
             textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
        }
       }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("DESCRIPCIO"))
       {
         correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
         cuenta = cuenta + 1; 
          if (correcto == false)
        {
              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
        }
       }
    
    }
  }
  else
  {
    for(i=0;i<esquemaTabla.getAttributeCount();i++)
    {
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("IDUSOGLO"))
       {
        correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
        cuenta = cuenta + 1; 
         if (correcto == false)
        {
             textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");   
        }
       }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("IDCLASIF"))
       {
         correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
         cuenta = cuenta + 1; 
          if (correcto == false)
        {
              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
        }
       }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("CATEGORIA"))
       {
        correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
        cuenta = cuenta + 1; 
        if (correcto == false)
        {
            textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
        }
       }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("NUMERO"))
       {
        correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
        cuenta = cuenta + 1; 
        if (correcto == false)
        {
            textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
        }
       }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("NORMATIVA"))
       {
        correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
        cuenta = cuenta + 1;  
         if (correcto == false)
        {
             textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
        }
       }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("DESCRIPCIO"))
       {
       	correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
        cuenta = cuenta + 1; 
        if (correcto == false)
	    {
            textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
	    }
       }
    
    }

  }

    if(cuenta != 6)
    {
        textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("faltanCampos")+ "</b></font></p>");

      correcto = false;
    }
   return correcto; 
  }
  
  public boolean vincularDominio(String descripcion,long id, Connection con)
  {
     try
     {
        PreparedStatement ps = con.prepareStatement("vincularDominio");
        ps.setString(1, descripcion);
        ps.setLong(2,id);
        ps.executeUpdate();
        app.closeConnection(con, ps,null,null);
        return true;
      }
      catch(Exception exc)
      {
          exc.printStackTrace();
          return false;
      }
  
  }
  public ArrayList getCampo(String tabla, int tipoFichero)
  {
    ArrayList dominioArray = new ArrayList();
    
     if (tabla.equals("ambitos_planeamiento"))
      {
        idDomi = "20";
        if (tipoFichero==0)
        {
            nombreCampoDomi = "IDambplanetipo";
        }
        else
        {
            nombreCampoDomi = "IDAMBPLAN"; 
        }
      }
      else if(tabla.equals("tabla_planeamiento"))
      {
        idDomi = "83";
        if (tipoFichero==0)
        {
            nombreCampoDomi = "Tipoplan";
        }
        else
        {
            nombreCampoDomi = "TIPOPLAN"; 
        }
      }
      else if(tabla.equals("sistemas_generales"))
      {
        idDomi = "22";
        nombreCampoDomi = "IDUSOGLO";        
      }
      else if(tabla.equals("clasificacion_suelo"))
      {
        idDomi = "23";
        if (tipoFichero==0)
        {
            nombreCampoDomi = "IDClasifTipo";
        }
        else
        {
            nombreCampoDomi = "IDCLASIF";
        }
      }else if(tabla.equals("ambitos_gestion"))
      {
          idDomi = "24";
          if (tipoFichero==0)
          {
              nombreCampoDomi = "IDtipget";
          }
          else
          {
              nombreCampoDomi = "IDTIPGET";
          }
        }
      else
      {
        return null;
      }
    dominioArray.add(idDomi);
    dominioArray.add(nombreCampoDomi);
    
    return dominioArray;
  }

}