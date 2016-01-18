package com.geopista.app.patrimonio;
import com.geopista.app.AppContext;
import com.geopista.app.catastro.GeopistaInformesPostgresCon;

import java.sql.Connection;
import com.geopista.app.AppContext;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.ResourceBundle;

import jimm.datavision.Field;
import jimm.datavision.Formula;
import jimm.datavision.Report;
import jimm.datavision.layout.swing.SwingLE;

import org.xml.sax.SAXException;

public class GeopistaFichaLibroInventario 
{
  private int bien=0;
  private AppContext appcontext = (AppContext) AppContext.getApplicationContext(); 
          
  public GeopistaFichaLibroInventario(ArrayList bienes,String rutaMapa) throws Exception 
  {
    //Se recibe el valor del bien para su inventario, clásula Where
   try {
      Report report = new Report();
      SwingLE le = new SwingLE();

      String Where_c = "{bienes.fechabaja} is null";
      
      Iterator IdBienes = bienes.iterator();
      
      if(bienes.size()==0){
          Where_c= Where_c+" and ({bienes.id}=0";
      }
      else{
          Where_c=Where_c+" and (";
      }
          
      
      int n=0;
      while (IdBienes.hasNext()) 
      {
          n++;
          if (n>1){
              Where_c= Where_c+ " or";
          }
          Where_c= Where_c+ " {bienes.id}=" + IdBienes.next().toString() ;         
         
       }
      
      Where_c=Where_c+")";
      
      
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

         String unitName = appcontext.getUserPreference("unidad.nombre","",true);
         String unitEquivalence = appcontext.getUserPreference("unidad.equivalence","",true);

        InputStream in = null; 
        if(unitEquivalence==null||unitEquivalence.trim().equals(""))
        {
            in = new FileInputStream(appcontext.getPath("informe.patrimonio.inventario.fichero"));
        }
        else
        {
            in = new FileInputStream(appcontext.getPath("informe.patrimonio.inventario.fichero.equivalente"));
        }
        
        
        report.read(new InputStreamReader(in));
        if(unitEquivalence!=null&&!unitEquivalence.trim().equals(""))
        {
            Formula superficieEquivalente = report.findFormula("2");
            Formula nombreUnidadEquivalente = report.findFormula("6");
            
            superficieEquivalente.setExpression("['"+unitEquivalence+"']");
            nombreUnidadEquivalente.setExpression("['"+unitName+"']");
        }
        
       report.setTitle(appcontext.getI18nString("informe.patrimonio.inventario.titulo"));

       //Realizamos la traducción de los campos
       //Logo
        Field Logo = report.findField("5");
        Logo.setValue(appcontext.getPath("logo.informes"));
        
//      Realizamos la traducción de los campos
        //Logo
         Field ImagenPatrimonio = report.findField("231");
         ImagenPatrimonio.setValue(rutaMapa);
         
        
//       //Cargo
        Field cargo = report.findField("117");
        cargo.setValue(appcontext.getI18nString("informe.ficha.patrimonio.cargo"));
        
        //Sup. Solar
        Field supSolar = report.findField("119");
        supSolar.setValue(appcontext.getI18nString("informe.ficha.patrimonio.Sup.Solar"));
        
        // Sup. Construcción
        Field supConst = report.findField("120");
        supConst.setValue(appcontext.getI18nString("informe.ficha.patrimonio.Sup.Construccion"));
        
        //Sup. Real
        Field supReal = report.findField("121");
        supReal.setValue(appcontext.getI18nString("informe.ficha.patrimonio.Sup.Real"));
        
        //Domicilio Tributario
        Field domicilioTributario = report.findField("122");
        domicilioTributario.setValue(appcontext.getI18nString("informe.ficha.patrimonio.Domicilio.Tributario"));
        
        //Código Vía
        Field codVia = report.findField("123");
        codVia.setValue(appcontext.getI18nString("informe.ficha.patrimonio.cod.via"));
        
        //Código Postal
        Field codigoPostalDomicilio = report.findField("124");
        codigoPostalDomicilio.setValue(appcontext.getI18nString("informe.ficha.patrimonio.cod.postal"));

        //Destino 
        Field destino = report.findField("184");
        destino.setValue(appcontext.getI18nString("informe.ficha.patrimonio.destino"));
        //Coeficiente Propiedad
        Field coefPropiedad = report.findField("125");
        coefPropiedad.setValue(appcontext.getI18nString("informe.ficha.patrimonio.coef.propiedad"));
        
        //Valor Suelo
        Field valorSuelo = report.findField("127");
        valorSuelo.setValue(appcontext.getI18nString("informe.ficha.patrimonio.valor.suelo"));
        
        //Valor construcción
        Field valorConstruccion = report.findField("128");
        valorConstruccion.setValue(appcontext.getI18nString("informe.ficha.patrimonio.valor.construccion"));
        
        //Valor Catastral 
        Field valorCatastral = report.findField("129");
        valorCatastral.setValue(appcontext.getI18nString("informe.ficha.patrimonio.valor.catastral"));
        
        //Año Valor
        Field anhoValor = report.findField("130");
        anhoValor.setValue(appcontext.getI18nString("informe.ficha.patrimonio.anho.valor"));
        
        //Apellidos y Nombre Razon social
        Field apellidos = report.findField("132");
        apellidos.setValue(appcontext.getI18nString("informe.ficha.patrimonio.apellidos.nombre.razon.social"));
        
        //Nif
        Field Nif = report.findField("133");
        Nif.setValue(appcontext.getI18nString("informe.ficha.patrimonio.nif"));
        
        //Dirección social
        Field direccionSocial=report.findField("134");
        direccionSocial.setValue(appcontext.getI18nString("informe.ficha.patrimonio.Direccion.Social"));
        
        //Codigo Postal Sujeto Pasivo
        Field codigoPostalPasivo = report.findField("135");
        codigoPostalPasivo.setValue(appcontext.getI18nString("informe.ficha.patrimonio.cod.postal"));
        
        // Municipio Pasivo
        Field municipioPasivo = report.findField("136");
        municipioPasivo.setValue(appcontext.getI18nString("informe.ficha.patrimonio.municipio"));
        
        //Provincia Pasivo
        Field provinciaPasivo = report.findField("137");
        provinciaPasivo.setValue(appcontext.getI18nString("informe.ficha.patrimonio.provincia"));
        
		
		
        


    //CLASIFICACION CONTABLE
       //Field clacon2 = report.findField("111");
       //clacon2.setValue(appcontext.getI18nString("informe.patrimonio.inventario.clasificacion.contable2"));

       report.getDataSource().getQuery().setWhereClause(Where_c);
    
       
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
};
}