package com.geopista.app.reports;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

import jimm.datavision.Field;
import jimm.datavision.Report;
import jimm.datavision.layout.swing.SwingLE;

import org.xml.sax.SAXException;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.GeopistaInformesPostgresCon;

public class GeopistaDiscrepanciasIBI 
{
  private int bien=0;
  private AppContext appcontext = (AppContext) AppContext.getApplicationContext();
          
  public GeopistaDiscrepanciasIBI() throws Exception 
  {
    //Se recibe el valor del bien para su inventario, clásula Where
   try {
      Report report = new Report();
      SwingLE le = new SwingLE();
    
      // Establecemos la conexion con los que vengan en la aplicación 
          Connection conn = abrirConexion();
          report.setDatabaseConnection(conn);

   //  Quitar Los drivers y registrar el de Geopista.
        Enumeration e = DriverManager.getDrivers();
          while (e.hasMoreElements())
            {
              DriverManager.deregisterDriver((Driver)e.nextElement());
            }
        DriverManager.registerDriver(new com.geopista.sql.GEOPISTADriver());


        
        // Leemos el report de disco
         GeopistaInformesPostgresCon conexion = new GeopistaInformesPostgresCon();

        
        InputStream in = new FileInputStream(appcontext.getPath("informe.ibi"));
        report.read(new InputStreamReader(in));
        
        report.setTitle(appcontext.getI18nString("informe.ibi.titulo"));

      //Titulo
       Field titulo = report.findField("130");
       titulo.setValue(appcontext.getI18nString("informe.ibi.titulo"));

       //Nif
        Field nif = report.findField("96");
        nif.setValue(appcontext.getI18nString("informe.ibi.nif"));

        //Nombre 
        Field nombre  = report.findField("103");
        nombre.setValue(appcontext.getI18nString("informe.ibi.nombre"));
        //Identidad
        Field identidad=report.findField("104");
        identidad.setValue(appcontext.getI18nString("informe.ibi.identidad"));

        //Dirección Ibi
        Field direccionIbi = report.findField("91");
        direccionIbi.setValue(appcontext.getI18nString("informe.ibi.direccion.ibi"));

        //Direccion Padron
        Field direccionPadron = report.findField("93");
        direccionPadron.setValue(appcontext.getI18nString("informe.ibi.direccion.padron"));

        // Referencia Catastral
        Field referencia = report.findField("94");
        referencia.setValue(appcontext.getI18nString("informe.ibi.referencia.catastral"));

        //toal discrepancias
        Field totalDiscrepancias = report.findField("108");
        totalDiscrepancias.setValue(appcontext.getI18nString("informe.ibi.total.discrepancias"));

        //Pagina
        Field pagina  = report.findField("135");
        pagina.setValue(appcontext.getI18nString("informe.ibi.numero.pagina"));

        //Logo
        Field Logo = report.findField("129");
        Logo.setValue(appcontext.getPath("logo.informes"));





     //  report.getDataSource().getQuery().setWhereClause(Where_c);
    
       
            report.setLayoutEngine(le);   // se lo asociamos al report
            report.runReport();           // procesamos la conversión


  } catch (SAXException e){
    e.printStackTrace();
  }
  
  }
/**
 * Método que crea la conexion a la base de datos
 * @param String username
 * @param Stirng password
 * @return Connection Conexion
 */
public static Connection getDBConnection (String username, String password,
  String thinConn, String driverClass) throws SQLException{
    Connection con =null;
    try
    {
      Class.forName(driverClass);
      con= DriverManager.getConnection(thinConn, username, password);
      con.setAutoCommit(false);
    }catch (Exception e){
        e.printStackTrace();
    }finally
      {
        return con;
      }
}

/**
 * Método que abre una conexion a org.postgres.Driver
 */

public Connection abrirConexion() throws Exception {

    String url=appcontext.getString("conexion.url");
    String user = appcontext.getString("conexion.user");
    String pass =appcontext.getUserPreference("conexion.pass","",false);
    String driver =appcontext.getString("conexion.driver");
    Connection conn = null;
  try {
      Enumeration e = DriverManager.getDrivers();
      while (e.hasMoreElements())
      {
       DriverManager.deregisterDriver((Driver)e.nextElement());
      }
      DriverManager.registerDriver(new org.postgresql.Driver());
     conn = getDBConnection (user,pass,url, driver);
  }catch (Exception e){e.printStackTrace();}
    return conn;
}
}


