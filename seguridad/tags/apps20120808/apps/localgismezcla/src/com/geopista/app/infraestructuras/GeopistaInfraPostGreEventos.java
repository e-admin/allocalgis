package com.geopista.app.infraestructuras;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.geopista.app.AppContext;
import com.geopista.util.ApplicationContext;
/**
 * Esta clase contiene los métodos necesarios para trabajar con 
 * el gestor de eventos de infraestructuras
 */
public class GeopistaInfraPostGreEventos
{
  private static ApplicationContext app = AppContext.getApplicationContext();
  public String fecha;
  public Connection con = null;
  public String tipoActuacion;
  public String tipoAveria;
  public String tipoSolucion;
  public int activo=0;
  public int idMaxMto=0;
  public int idMaxAv=0;
  public int idMaxAvisoB=0;
  public long idLocalizacion=0;
  public String observaciones;
  public String gravedad;
  public String motivo;
  public GeopistaInfraPostGreEventos()
  {
      /*try
      {
        con = getDBConnection();
      }
      catch(Exception e)
      {
        e.printStackTrace();
      }*/
  }
  /**
    * Realiza la conexión con la base de datos
    * @return Devuelve la conexión establecida con la base de datos
  */
  public static Connection getDBConnection () throws SQLException
  {
    Connection conn=  app.getConnection();
    return conn;
  }

/**
 * Listado de datos de Mantenimiento de la infraestructura seleccionada en el mapa
 * @param entidad Identificador de la entidad seleccionada en el mapa
 * @param con Conexión con la Base de Datos
 * @return Devuelve en un ArrayList con los identificadores de los datos de mantenimiento de la entidad. Devuelve null cuando no se han encontrado registros.
 */
public ArrayList getListadoMantenimiento(int entidad, int idElemento, Connection con)
{
  ArrayList resultado = new ArrayList();
  try
  {
    ResultSet r = null;

    PreparedStatement ps = con.prepareStatement("infraestructurasgestorlistadomantenimiento");
    ps.setInt(1, entidad);
    ps.setInt(2, idElemento);

    if(!ps.execute())
    {
    }
    else
    {
      r  = ps.getResultSet();
      while( r.next())
      {
          resultado.add(new Integer(r.getInt("id")));
      }
      app.closeConnection(null, ps,null,r);
    }

  }catch (Exception ex)
  {
      ex.printStackTrace();    
  }
      return resultado;

}

/**
 * Listado de Avisos sobre la infraestructura seleccionada en el mapa
 * @param entidad Identificador de la entidad seleccionada en el mapa
 * @param con Conexión con la Base de Datos
 * @return Devuelve en un ArrayList los identificadores de los avisos de la entidad. Devuelve null cuando no se han encontrado registros.
 */

public ArrayList getListadoAvisos(int entidad,int idElemento, Connection con)
{
  ArrayList resultadoAvisos = new ArrayList();
  try
  {
    ResultSet r = null;
    PreparedStatement ps = con.prepareStatement("infraestructurasgestorlistadoavisos");
    ps.setInt(1, entidad);
    ps.setInt(2, idElemento);

    if(!ps.execute())
    {
        
    }
    else
    {
      r  = ps.getResultSet();
      while( r.next())
      {
        resultadoAvisos.add(new Integer(r.getInt("id")));
      }
        app.closeConnection(null, ps,null,r);


    }
  }catch (Exception ex)
  {
    ex.printStackTrace();
  }
  return resultadoAvisos;
}

/**
 * Listado de Averias sobre la infraestructura seleccionada en el mapa
 * @param entidad Identificador de la entidad seleccionada en el mapa
 * @param con Conexión con la Base de Datos
 * @return Devuelve en un ArrayList los identificadores de las averias de la entidad. Devuelve null cuando no se han encontrado registros.
*/
public ArrayList getListadoAverias(int entidad, int idElemento, Connection con)
{
  ArrayList resultado = new ArrayList();
  try
  {
    ResultSet r = null;

    PreparedStatement ps = con.prepareStatement("infraestructurasgestorlistadoaverias");
    ps.setInt(1, entidad);
    ps.setInt(2, idElemento);
    if(!ps.execute())
    {
        
    }
    else
    {
      r  = ps.getResultSet();
      while( r.next())
      {
        resultado.add(new Integer(r.getInt("id")));
      }
      app.closeConnection(null, ps,null,r);
    }
  }catch (Exception ex)
  {
    ex.printStackTrace();
  }
  return resultado;
}
/** 
 * Obtiene la fecha de mantenimiento del dato seleccionado
 * @param idMantenimiento Identificador del mantenimiento seleccionado
 * @param con Conexión con la Base de Datos
 * @return Devuelve la fecha del mantenimiento. Devuelve null cuando se ha producido un error o no encuentra la fecha 
 */ 
public String getFechaMantenimiento(int idMantenimiento, Connection con)
  {
     try
    {
      ResultSet r = null;

      PreparedStatement ps = con.prepareStatement("infraestructurasgestorfechamantenimiento");
      ps.setInt(1,idMantenimiento);//    r = GeopistaPlaneamientoSQL.Query("planeamientogestornumero",params);

      if(!ps.execute())
      {
          
      }
      else
      {
        r  = ps.getResultSet();

      while(r.next())
      {
           fecha = r.getString("fechaactuacion");
      }
    }
    app.closeConnection(null, ps,null,r);

    return fecha;

  }catch (Exception ex)
  {
     ex.printStackTrace();
    return null;
  }

}

/** 
 * Obtiene la fecha de aviso del dato seleccionado
 * @param idAviso Identificador del aviso seleccionado
 * @param con Conexión con la Base de Datos
 * @return Devuelve la fecha del aviso. Devuelve null si se producen errores.
 */ 

public String getFechaAviso(int idAviso, Connection con)
{
 try
  {
    ResultSet r = null;

    PreparedStatement ps = con.prepareStatement("infraestructurasgestorfechaaviso");
    ps.setInt(1,idAviso);

    try
    {
      if(!ps.execute())
      {
          
      }
      else
      {
        r  = ps.getResultSet();
        while (r.next())
        {
          fecha = r.getString("fechaaviso");
        }
        app.closeConnection(null, ps,null,r);
        }
    }catch (Exception ex)
    {
      ex.printStackTrace();
      return null;
    }
  
  }catch (Exception ex)
  {
    ex.printStackTrace();
    return null;
  }
  return fecha;
}

/** 
 * Obtiene la fecha de averia del dato seleccionado
 * @param idAveria Identificador de la averia seleccionada
 * @param con Conexión con la Base de Datos
 * @return Devuelve la fecha de la averia. Devuelve null si se han producido errores.
 */ 
public String getFechaAveria(int idAveria, Connection con)
{
  try
  {
    ResultSet r = null;

    PreparedStatement ps = con.prepareStatement("infraestructurasgestorfechaaveria");
    ps.setInt(1,idAveria);

    if(!ps.execute())
    {
        
    }
    else
    {
      r  = ps.getResultSet();
      while (r.next())
      {
        fecha = r.getString("fechainstalacion");
      }
     app.closeConnection(null, ps,null,r);
    }

  }catch (Exception ex)
  {
    ex.printStackTrace();
    return null;
  }
  return fecha;
}

/** 
 * Obtiene el tipo de Actuación del dato seleccionado
 * @param idAveria Identificador de la averia seleccionada
 * @param con Conexión con la Base de Datos
 * @return Devuelve el identificador del dominio del Tipo de Actuación correspondiente a ese dato de Mantenimiento. Devuelve 0 si se han producido errores
 */ 
 
  public String getTipoActuacion(int idMantenimiento, Connection con)
  {
  try
  {

    ResultSet r = null;
   
    PreparedStatement ps = con.prepareStatement("infraestructurasgestortipoactuacion");
    ps.setInt(1,idMantenimiento);

    if(!ps.execute())
    {
    }
    else
    {
        r  = ps.getResultSet();
        while (r.next())
        {
            tipoActuacion = r.getString("tipoactuacion");
        }
      app.closeConnection(null, ps,null,r);
    }
    
  }catch (Exception ex)
  {
    ex.printStackTrace();
    return null;
  } 
  
   return tipoActuacion; 
  }

/** 
 * Obtiene las observaciones del dato seleccionado
 * @param idMantenimiento Identificador del mantenimiento seleccionado
 * @param con Conexión con la Base de Datos
 * @return Devuelve las observaciones correspondientes a ese dato de Mantenimiento. Devuelve null cuando se han producido errores o no existen observaciones
 */ 
public String getObservacionesMantenimiento(int idMantenimiento, Connection con)
{

  try
  {
    ResultSet r = null;

    PreparedStatement ps = con.prepareStatement("infraestructurasgestorobservacionesmantenimiento");
    ps.setInt(1,idMantenimiento);

    if(!ps.execute())
    {
        
    }
    else
    {
      r  = ps.getResultSet();
      while (r.next())
      {
        observaciones = r.getString("observaciones");
      }
      app.closeConnection(null, ps,null,r);
 }

  }catch (Exception ex)
  {
    ex.printStackTrace();
    return null;
  }
  return observaciones;
}

/** 
 * Obtiene la fecha de reparación del dato seleccionado
 * @param idAveria Identificador de la averia seleccionada
 * @param con Conexión con la Base de Datos
 * @return Devuelve la fecha correspondiente a la Avería. Devuelve null si se han producido errores
 */ 
public String getFechaReparacion(int idAveria, Connection con)
{

  try
  {
    ResultSet r = null;

    PreparedStatement ps = con.prepareStatement("infraestructurasgestorfechareparacion");
    ps.setInt(1,idAveria);

    if(!ps.execute())
    {
        
    }
    else
    {
      r  = ps.getResultSet();
      while (r.next())
      {
        fecha = r.getString("fechareparacion");
      }
      app.closeConnection(null, ps,null,r);
    }

  }catch (Exception ex)
  {
    ex.printStackTrace();
    return null;
  }
  return fecha;
}

/** 
 * Obtiene el tipo de avería del dato seleccionado
 * @param idAveria Identificador de la averia seleccionada
 * @param con Conexión con la Base de Datos
 * @return Devuelve el identificador del dominio que corresponde a la Avería. Devuelve 0 cuando se han producido errores
 */ 

public String getTipoAveria(int idAveria, Connection con)
{
  try
  {
    ResultSet r = null;
   
    PreparedStatement ps = con.prepareStatement("infraestructurasgestortipoaveria");
    ps.setInt(1,idAveria);

    if(!ps.execute())
    {
    }
    else
    {
        r  = ps.getResultSet();
        while (r.next())
        {
            tipoAveria = r.getString("tipoaveria");
        }
      app.closeConnection(null, ps,null,r);
    }
  }catch (Exception ex)
  {
    ex.printStackTrace();
    return null;
  } 
  
   return tipoAveria; 
  }

/** 
 * Obtiene la gravedad del dato seleccionado
 * @param idAveria Identificador de la averia seleccionada
 * @param con Conexión con la Base de Datos
 * @return Devuelve la gravedad de la Avería. Devuelve null cuando se han producido errores.
 */ 
public String getGravedad(int idAveria, Connection con)
{
  try
  {
    ResultSet r = null;

    PreparedStatement ps = con.prepareStatement("infraestructurasgestoraveriagravedad");
    ps.setInt(1,idAveria);

    if(!ps.execute())
    {
    }
    else
    {
      r  = ps.getResultSet();
      while (r.next())
      {
        gravedad = r.getString("gravedad");
      }
       app.closeConnection(null, ps,null,r);
    }

  }catch (Exception ex)
  {
    ex.printStackTrace();
    return null;
  }
  return gravedad;
}
/** 
 * Obtiene el tipo de solución del dato seleccionado
 * @param idAveria Identificador de la averia seleccionada
 * @param con Conexión con la Base de Datos
 * @return Devuelve el identificador del dominio que corresponde a la Avería. Devuelve 0 cuando se han producido errores
 */ 
public String getTipoSolucion(int idAveria, Connection con)
{
  try
  {

    ResultSet r = null;
   
    PreparedStatement ps = con.prepareStatement("infraestructurasgestortiposolucion");
    ps.setInt(1,idAveria);

    if(!ps.execute())
    {
    }
    else
    {
        r  = ps.getResultSet();
        while (r.next())
        {
            tipoSolucion = r.getString("tiposolucion");
        }
        app.closeConnection(null, ps,null,r);
    }
  }catch (Exception ex)
  {
    ex.printStackTrace();
    return null;
  } 
  
   return tipoSolucion; 
  }

/** 
 * Obtiene si el aviso esta activado o no.
 * @param idAviso Identificador del aviso seleccionado
 * @param con Conexión con la Base de Datos
 * @return Devuelve 1 si el aviso esta activado. Devuelve 0 cuando no esta activo o se han producido errores.
 */ 
public int getActivo(int idAviso, Connection con)
  {

  try
  {

    ResultSet r = null;
   
    PreparedStatement ps = con.prepareStatement("infraestructurasgestoravisosactivo");
    ps.setInt(1,idAviso);

    if(!ps.execute())
    {
    }
    else
    {
        r  = ps.getResultSet();
        while (r.next())
        {
            activo = r.getInt("activo");
        }
        app.closeConnection(null, ps,null,r);
    }
    
  }catch (Exception ex)
  {
    ex.printStackTrace();
    return 0;
  } 
  
   return activo; 
}
/** 
 * Obtiene el motivo del aviso 
 * @param idAviso Identificador del aviso seleccionado
 * @param con Conexión con la Base de Datos
 * @return Devuelve un String con el motivo del aviso; devuelve null si se producen errores
 */ 
public String getMotivoAviso(int idAviso, Connection con)
  {

  try
  {

    ResultSet r = null;
   
    PreparedStatement ps = con.prepareStatement("infraestructurasgestormotivoaviso");
    ps.setInt(1,idAviso);

    if(!ps.execute())
    {
    }
    else
    {
        r  = ps.getResultSet();
        while (r.next())
        {
            motivo = r.getString("motivo");
        }
        app.closeConnection(null, ps,null,r);      
    }
    
  }catch (Exception ex)
  {
    ex.printStackTrace();
    return null;
  } 
  
   return motivo; 
}

/** 
 * Se llama a este método para obtener el identificador del mantenimiento que tiene que insertarse en la base de datos
 * @param con Conexión con la Base de Datos
 * @return Devuelve el identificador del mantenimiento
 */ 
 
  public int getMaxIdMantenimiento(Connection con)
  {
    try
    {

    ResultSet r = null;

   
    PreparedStatement ps = con.prepareStatement("infraestructurasgestormaxmantenimiento");

    if(!ps.execute())
    {
    }
    else
    {
      r  = ps.getResultSet();
      while (r.next())
      {
        idMaxMto = r.getInt(1);
//        idMaxMto = idMaxMto + 1;
      }
      app.closeConnection(null, ps,null,r);    
    }
    
  }catch (Exception ex)
  {
    ex.printStackTrace();
    return 0;
  } 
     return idMaxMto;     
  }
/** 
 * Inserta en la tabla historicomantenimiento los valores insertados por el usuario en el formulario del gestor de eventos de infraestructuras.
 * @param id Identificador del mantenimiento
 * @param entidad Identificador de la entidad
 * @param elemento Elemento en el que ha seleccionado el usuario
 * @param tipo Tipo de mantenimiento
 * @param fecha Fecha de mantenimiento
 * @param observaciones Observaciones del mantenimiento
 * @param con Conexión con la Base de Datos
 */ 
public void insertarMantenimiento(int id, int entidad, int elemento, int tipo, String fecha, String observaciones, Connection con)
{
    try
    {

      PreparedStatement ps = con.prepareStatement("infraestructurasinsertarmantenimiento");
      ps.setInt(1, id);
      ps.setInt(2, entidad);
      ps.setInt(3, elemento);
      ps.setInt(4, tipo);
      ps.setString(5, fecha);
      ps.setString(6, observaciones);
      ps.executeUpdate();
      app.closeConnection(null, ps,null,null);    
    }catch (Exception ex)
    {
     ex.printStackTrace();
    } 
  
}

/** 
 * Se llama a este método para obtener el identificador de la avería que tiene que insertarse en la base de datos
 * @param con Conexión con la Base de Datos
 * @return Devuelve el identificador de la avería
 */ 
  public int getMaxIdAveria(Connection con)
  {
    try
    {

    ResultSet r = null;

   
    PreparedStatement ps = con.prepareStatement("infraestructurasgestormaxaveria");

    if(!ps.execute())
    {
    }
    else
    {
      r  = ps.getResultSet();
      
      while (r.next())
      {
        idMaxAv = r.getInt(1);
//        idMaxAv = idMaxAv + 1;
      }
     app.closeConnection(null, ps,null,r);
     
    }
    
  }catch (Exception ex)
  {
    ex.printStackTrace();
    return 0;
  } 
     return idMaxAv;     
  }

/** 
 * Inserta en la tabla averias los valores insertados por el usuario en el formulario del gestor de eventos de infraestructuras.
 * @param id Identificador de la averia
 * @param entidad Identificador de la entidad
 * @param elemento Elemento en el que ha seleccionado el usuario
 * @param gravedad Gravedad de la avería
 * @param tipo Tipo de Avería
 * @param fecha Fecha de Notificación de la Avería
 * @param localizacion Localización de la Avería
 * @param solucion Tipo de Solución
 * @param reparacion Fecha de Reparación
 * @param observaciones Observaciones sobre la averia
 * @param con Conexión con la Base de Datos
 */ 
public void insertarAveria(int id, int entidad, int elemento, int gravedad, int tipo, String fecha, int localizacion, int solucion, String reparacion, String observaciones, Connection con)
{
    try
    {

      PreparedStatement ps = con.prepareStatement("infraestructurasgestorinsertaraveria");
      
      ps.setInt(1, id);
      ps.setInt(2, entidad);
      ps.setInt(3, elemento);
      ps.setInt(4, gravedad);
      ps.setInt(5, tipo);
      ps.setString(6, fecha);
      ps.setInt(7, localizacion);
      ps.setInt(8, solucion);
      
      if(reparacion!=null&&reparacion.trim().equals(""))
          reparacion=null;
      
      ps.setString(9, reparacion);
      ps.setString(10, observaciones);
      ps.executeUpdate();
      app.closeConnection(null, ps,null,null);    
    }catch (Exception ex)
    {
       ex.printStackTrace();
    } 
  
}
/** 
 * Inserta en la tabla avisos los valores insertados por el usuario en el formulario del gestor de eventos de infraestructuras.
 * @param id Identificador del aviso
 * @param entidad Identificador de la entidad
 * @param elemento Elemento en el que ha seleccionado el usuario
 * @param fecha Fecha del Aviso
 * @param motivo Motivo del aviso
 * @param activo Aviso activado Sí/No
 * @param con Conexión con la Base de Datos
 */ 
public void insertarAviso(int id, int entidad, int elemento,String fecha, String motivo, int activo, Connection con)
{
    try
    {

      PreparedStatement ps = con.prepareStatement("infraestructurasgestorinsertaraviso");
      ps.setInt(1, id);
      ps.setInt(2, entidad);
      ps.setInt(3, elemento);
      ps.setString(4, fecha);
      ps.setString(5, motivo);
      ps.setInt(6, activo);
      ps.executeUpdate();
      app.closeConnection(null, ps,null,null);    

    }catch (Exception ex)
    {
     ex.printStackTrace();
    } 
  
}
/** 
 * Se llama a este método para obtener el identificador del aviso que tiene que insertarse en la base de datos
 * @param con Conexión con la Base de Datos
 * @return Devuelve el identificador del aviso
 */ 
  public int getMaxIdAviso(Connection con)
  {
    try
    {

    ResultSet r = null;
    PreparedStatement ps = con.prepareStatement("infraestructurasgestormaxaviso");

    if(!ps.execute())
    {
    }
    else
    {
      r  = ps.getResultSet();
      
      while (r.next())
      {
        idMaxAvisoB = r.getInt(1);
        idMaxAvisoB = idMaxAvisoB + 1;
      }
      app.closeConnection(null, ps,null,r);     
    }
    
  }catch (Exception ex)
  {
    ex.printStackTrace();
    return 0;
  } 
     return idMaxAvisoB;     
  }

/** 
 * Modifica los datos de la tabla historicomantenimiento con los valores introducidos por el usuario en el formulario del gestor de eventos de infraestructuras.
 * @param id Identificador del mantenimiento
 * @param entidad Identificador de la entidad
 * @param elemento Elemento en el que ha seleccionado el usuario
 * @param tipo Tipo de mantenimiento
 * @param fecha Fecha de mantenimiento
 * @param observaciones Observaciones del mantenimiento
 * @param con Conexión con la Base de Datos
 */ 

public void modificarMantenimiento(int id, int entidad, int elemento, int tipo, String fecha, String observaciones, Connection con)
{
    try
    {

      PreparedStatement ps = con.prepareStatement("infraestructurasgestormantenimientomodificar");
     
      ps.setInt(1, entidad);
      ps.setInt(2, elemento);
      ps.setInt(3, tipo);
      ps.setString(4, fecha);
      ps.setString(5, observaciones);
      ps.setInt(6, id);
      ps.executeUpdate();
      app.closeConnection(null, ps,null,null);    

    }catch (Exception ex)
    {
      ex.printStackTrace();
    } 
  
}
/** 
 * Elimina de la tabla historicomantenimiento el dato de mantenimiento seleccionado por el usuario
 * @param id Identificador del mantenimiento
 * @param con Conexión con la Base de Datos
 */
public void eliminarMantenimiento(int idMant, Connection con)
{
    try
    {
      PreparedStatement ps = con.prepareStatement("infraestructurasgestoreliminarmantenimiento");
      ps.setInt(1, idMant);
    
      ps.executeUpdate();
      app.closeConnection(null, ps,null,null);    
     }
    catch (Exception ex)
    {
      ex.printStackTrace();
    } 
    
}

/** 
 * Modifica en la tabla averias los valores introducidos por el usuario en el formulario del gestor de eventos de infraestructuras.
 * @param id Identificador de la averia
 * @param entidad Identificador de la entidad
 * @param elemento Elemento en el que ha seleccionado el usuario
 * @param gravedad Gravedad de la avería
 * @param tipo Tipo de Avería
 * @param fecha Fecha de Notificación de la Avería
 * @param localizacion Localización de la Avería
 * @param solucion Tipo de Solución
 * @param reparacion Fecha de Reparación
 * @param observaciones Observaciones sobre la averia
 * @param con Conexión con la Base de Datos
 */ 
public void modificarAveria(int id, int entidad, int elemento, int gravedad, int tipoAveria, String fechaNotificacion, int localizacion, int tipoSolucion, String fechaReparacion, String observaciones, Connection con)
{
    try
    {

      PreparedStatement ps = con.prepareStatement("infraestructurasgestormodificaraverias");
     
      ps.setInt(1, entidad);
      ps.setInt(2, elemento);
      ps.setInt(3, gravedad);
      ps.setInt(4, tipoAveria);
      ps.setString(5, fechaNotificacion);
      ps.setInt(6, localizacion);
      ps.setInt(7, tipoSolucion);
      ps.setString(8, fechaReparacion);
      ps.setString(9, observaciones);
      ps.setInt(10, id);

/*      String consulta;
      consulta = "update averias set id_entidad="+entidad;
      consulta = consulta + "," + "id_elemento="+elemento;
      consulta = consulta + "," + "gravedad="+gravedad;
      consulta = consulta + "," + "tipoaveria="+tipoAveria;
      consulta = consulta + "," + "fechainstalacion="+fechaNotificacion;
      consulta = consulta + "," + "id_localizacion="+localizacion;
      consulta = consulta + "," + "tiposolucion="+tipoSolucion;
      consulta = consulta + "," + "fechareparacion="+fechaReparacion;
      consulta = consulta + "," + "observaciones="+observaciones;
      consulta = consulta + " where id="+id;
      System.out.println("Consulta: " +consulta);
      */
      ps.executeUpdate();
      app.closeConnection(null, ps,null,null);    

    }catch (Exception ex)
    {
    } 
  
}
/** 
 * Modifica los datos de la tabla avisos 
 * @param id Identificador del aviso
 * @param entidad Identificador de la entidad
 * @param elemento Elemento en el que ha seleccionado el usuario
 * @param fecha Fecha del Aviso
 * @param motivo Motivo del aviso
 * @param activo Aviso activado Sí/No
 * @param con Conexión con la Base de Datos
 * */ 
public void modificarAviso(int id, int entidad, int elemento, String fecha, String motivo, int activo, Connection con)
{
    try
    {

      PreparedStatement ps = con.prepareStatement("infraestructurasgestormodificaravisos");
     
      ps.setInt(1, entidad);
      ps.setInt(2, elemento);
      ps.setString(3, fecha);
      ps.setString(4, motivo);
      ps.setInt(5, activo);
      ps.setInt(6, id);
      ps.executeUpdate();
      app.closeConnection(null, ps,null,null);    

    }catch (Exception ex)
    {
      ex.printStackTrace();
    } 
  
}

/** 
 * Elimina de la tabla avisos el aviso seleccionado por el usuario
 * @param idAviso Identificador del aviso
 * @param con Conexión con la Base de Datos
 */
public void eliminarAviso(int idAviso, Connection con)
{
    try
    {
      PreparedStatement ps = con.prepareStatement("infraestructurasgestoreliminaravisos");
      ps.setInt(1, idAviso);
    
      ps.executeUpdate();
      app.closeConnection(null, ps,null,null);    
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    } 
    
}
/** 
 * Elimina de la tabla averias la averia seleccionada por el usuario
 * @param idAveria Identificador de la averia
 * @param con Conexión con la Base de Datos
 */
public void eliminarAveria(int idAveria, Connection con)
{
    try
    {
      PreparedStatement ps = con.prepareStatement("infraestructurasgestoreliminaraverias");
      ps.setInt(1, idAveria);
    
      ps.executeUpdate();
      app.closeConnection(null, ps,null,null);    
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    } 
    
}

/** 
 * Obtiene las observaciones de la Averia seleccionada
 * @param idAveria Identificadorde la Averia seleccionada
 * @param con Conexión con la Base de Datos
 * @return Devuelve un String con las observaciones; devuelve null si se producen errores
 */ 
public String getObservacionesAveria(int idAveria, Connection con)
{

  try
  {
    ResultSet r = null;

    PreparedStatement ps = con.prepareStatement("infraestructurasgestorobservacionesaveria");
    ps.setInt(1,idAveria);

    if(!ps.execute())
    {
    }
    else
    {
      r  = ps.getResultSet();
      while (r.next())
      {
        observaciones = r.getString("observaciones");
      }
      app.closeConnection(null, ps,null,r);
    }

  }catch (Exception ex)
  {
    return null;
  }
  return observaciones;
}

public long getIdLoc(int idAveria, Connection con)
  {

  try
  {

    ResultSet r = null;
   
    PreparedStatement ps = con.prepareStatement("infraestructurasgestoraveriaslocalizacion");
    ps.setInt(1,idAveria);

    if(!ps.execute())
    {
    }
    else
    {
        r  = ps.getResultSet();
        while (r.next())
        {
            idLocalizacion = r.getLong("id_localizacion");
        }
        app.closeConnection(null, ps,null,r);
    }
    
  }catch (Exception ex)
  {
    ex.printStackTrace();
    return 0;
  } 
  
   return idLocalizacion; 
}


}//llave del constructor