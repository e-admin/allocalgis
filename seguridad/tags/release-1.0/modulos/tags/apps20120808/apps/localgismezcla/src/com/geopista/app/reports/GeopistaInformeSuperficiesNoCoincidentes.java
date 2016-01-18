package com.geopista.app.reports;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Map;

import javax.swing.JPanel;

import jimm.datavision.Field;
import jimm.datavision.Report;
import jimm.datavision.layout.swing.SwingLE;

import org.xml.sax.SAXException;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.GeopistaInformesPostgresCon;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;

/*public class GeopistaImportarPlaneamiento02Panel extends JPanel implements WizardPanel
{*/

public class GeopistaInformeSuperficiesNoCoincidentes extends JPanel implements WizardPanel
{


  AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
  public int parcela=0;
  public String rutaMapa="";
  public SwingLE le = new SwingLE();
  public GeopistaInformeSuperficiesNoCoincidentes(int parcela)
    
  {
      this.parcela=parcela;

    try
      {
        jbInit();
    }
        catch(Exception e)
      {
        e.printStackTrace();
      }   
  }

  private void jbInit() throws Exception
  {
      String Where_c="";
      Report report = new Report();
      if (parcela!=0){
       Where_c = "  {parcelas.id}="+ this.parcela ;
      }
      try {
    
      // Establecemos la conexion con los que vengan en la aplicación 
         GeopistaInformesPostgresCon conexion = new GeopistaInformesPostgresCon();
         Connection conn = this.abrirConexion();
         report.setDatabaseConnection(conn);

    //  Quitar Los drivers y registrar el de Geopista.
        Enumeration e = DriverManager.getDrivers();
          while (e.hasMoreElements())
            {
              DriverManager.deregisterDriver((Driver)e.nextElement());
            }
        DriverManager.registerDriver(new com.geopista.sql.GEOPISTADriver());

        // Leemos el report de disco

        InputStream in = new FileInputStream(aplicacion.getPath("report.patrimonio.superficie.distinta"));
        report.read(new InputStreamReader(in));

        report.setTitle(aplicacion.getI18nString("report.patrimonio.titulo.superficies.no.coincidentes"));
              //I18N de las etiquetas del informe

                //Titulo del Informe
                Field titulo = report.findField("2");
                titulo.setValue(aplicacion.getI18nString("informe.patrimonio.superficies.no.coincidentes.titulo.informe"));

              //Logo
                Field Logo = report.findField("1");
                Logo.setValue(aplicacion.getPath("logo.informes"));

              //Superficie Alfanumérica
                Field SupAlfa = report.findField("4");
                SupAlfa.setValue(aplicacion.getI18nString("informe.patrimonio.superficies.no.coincidentes.superficie.alfanumerica"));
                
              // Superficie Municipio
                Field municipio = report.findField("7");
                municipio.setValue(aplicacion.getI18nString("informe.patrimonio.superficies.no.coincidentes.municipio"));
                
              //Superficie Provincia
                Field provincia = report.findField("8");
                provincia.setValue(aplicacion.getI18nString("informe.patrimonio.superficies.no.coincidentes.provincia"));                

              //Superficie Grafica
                Field supGrafica = report.findField("14");
                supGrafica.setValue(aplicacion.getI18nString("informe.patrimonio.superficies.no.coincidentes.superficie.grafica"));                
 
              //Superficie Provincia
                Field refCat = report.findField("16");
                refCat.setValue(aplicacion.getI18nString("informe.patrimonio.superficies.no.coincidentes.referencia.catastral"));                

                
              // Pagina
                Field pagina = report.findField("10");
                pagina.setValue(aplicacion.getI18nString("informe.patrimonio.superficies.no.coincidentes.pagina"));                


                 // que no sean iguales las superficies

                 report.getDataSource().getQuery().setWhereClause(Where_c);

            
            report.setLayoutEngine(le);   // se lo asociamos al report
            report.runReport();           // procesamos la conversión
  

  } catch (SAXException e){
    e.printStackTrace();
  }
  


    
  } //del JbInit
     public void enteredFromLeft(Map dataMap){ }
     
    public void exitingToRight() throws Exception
    {
    }

    /**
     * Tip: Delegate to an InputChangedFirer.
     * @param listener a party to notify when the input changes (usually the
     * WizardDialog, which needs to know when to update the enabled state of
     * the buttons.
     */
    public void add(InputChangedListener listener)
    {
      
    }

    public void setWizardContext(WizardContext wd)
    {
      wd.inputChanged();
    }
    public void remove(InputChangedListener listener)
    {
      
    }

    public String getTitle()
    {
      return "";
    }

    public String getID()
    {
      return "2";
    }

    public String getInstructions()
    {
     return "";
    }

    public boolean isInputValid()
    {
      return true;
    }

    private String nextID=null;
    public void setNextID(String nextID)
    {
    	this.nextID=nextID;
    }
    
    /**
     * @return null to turn the Next button into a Finish button
     */
    public String getNextID()
    {

        return nextID;
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

    String url=aplicacion.getString("conexion.url");
    String user = aplicacion.getString("conexion.user");
    String pass =aplicacion.getUserPreference("conexion.pass","",false);
    String driver =aplicacion.getString("conexion.driver");
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

/* (non-Javadoc)
 * @see com.geopista.ui.wizard.WizardPanel#exiting()
 */
public void exiting()
{
    // TODO Auto-generated method stub
    
};

   
} //de la Clase
