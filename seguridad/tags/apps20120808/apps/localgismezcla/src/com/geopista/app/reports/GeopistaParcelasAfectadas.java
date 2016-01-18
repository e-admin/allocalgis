package com.geopista.app.reports;

//clase que lanzará el informe Urbanístico de una parcela seleccionada
import com.geopista.app.AppContext;
import com.geopista.app.planeamiento.GeopistaPlaneamientoInformesPostgreCon;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;

import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import java.util.Enumeration;
import java.sql.Driver;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.util.Map;

import javax.swing.JPanel;

import jimm.datavision.Field;
import jimm.datavision.Formula;
import jimm.datavision.Report;
import jimm.datavision.layout.swing.SwingLE;

import org.xml.sax.SAXException;

/*public class GeopistaImportarPlaneamiento02Panel extends JPanel implements WizardPanel
{*/

public class GeopistaParcelasAfectadas  extends JPanel implements WizardPanel
{


  
  public String numeroAmbitoGestion="";
  public AppContext aplicacion =(AppContext) AppContext.getApplicationContext();
  public String rutaMapa="";
  public SwingLE le = new SwingLE();
  public GeopistaParcelasAfectadas(String numeroAmbitoGestion,String rutaMapa)

  {
      this.numeroAmbitoGestion=numeroAmbitoGestion;
      this.rutaMapa=rutaMapa;
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

      Report report = new Report();
      String Where_c = " area(intersection(parcelas.\"GEOMETRY\", ambitos_gestion.\"GEOMETRY\"))>0 AND {ambitos_gestion.numero}='"+ this.numeroAmbitoGestion + "'";
      try {
    
      // Establecemos la conexion con los que vengan en la aplicación 

         //Connection conn = conexion.getDBConnection("postgres","postgres","jdbc:postgresql://212.22.38.19:5432/geopista","org.postgresql.Driver");
          Connection conn = this.abrirConexion();
          report.setDatabaseConnection(conn);

      Enumeration e = DriverManager.getDrivers();
      while (e.hasMoreElements())
      {
       DriverManager.deregisterDriver((Driver)e.nextElement());
      }
       DriverManager.registerDriver(new com.geopista.sql.GEOPISTADriver());

          
         GeopistaPlaneamientoInformesPostgreCon conexion = new GeopistaPlaneamientoInformesPostgreCon();          

        // Leemos el report de disco

         String unitName = aplicacion.getUserPreference("unidad.nombre","",true);
         String unitEquivalence = aplicacion.getUserPreference("unidad.equivalence","",true);

        InputStream in = null; 
        if(unitEquivalence==null||unitEquivalence.trim().equals(""))
        {
    
            in = new FileInputStream(aplicacion.getPath("report.parcelas.afectadas.ambitos.gestion"));
        }
        else
        {
            in = new FileInputStream(aplicacion.getPath("report.parcelas.afectadas.ambitos.gestion.equivalente"));
        }
        
        report.read(new InputStreamReader(in));

        if(unitEquivalence!=null&&!unitEquivalence.trim().equals(""))
        {
            Formula superficieEquivalente = report.findFormula("17");
            Formula nombreUnidadEquivalente = report.findFormula("16");
            
            superficieEquivalente.setExpression("['"+unitEquivalence+"']");
            nombreUnidadEquivalente.setExpression("['("+unitName+")']");
            Field superficieAfectada = report.findField("93");
            superficieAfectada.setValue(aplicacion.getI18nString("informe.parcelas.afectadas.superficie.afectada.unidadequivalente"));
            
//          Superficie Afectada
            Field fTotalSuperficie = report.findField("98");
            fTotalSuperficie.setValue(aplicacion.getI18nString("informe.parcelas.afectadas.superficie.afectada.total.sinUnidad"));


        }
        else
        {
//          Superficie Afectada
            Field fTotalSuperficie = report.findField("87");
            fTotalSuperficie.setValue(aplicacion.getI18nString("informe.parcelas.afectadas.superficie.afectada.total"));

        }
        
       report.setTitle("PARCELAS AFECTADAS POR AMBITO DE GESTION");
        
        //Datos Etiquetas
       //tÍTULO
       Field ftitulo = report.findField("52");
       ftitulo.setValue(aplicacion.getI18nString("informe.parcelas.afectadas.titulo"));

       //Descripción del Ambito de Gestion
       Field fDescripcion = report.findField("74");
       fDescripcion.setValue(aplicacion.getI18nString("informe.parcelas.afectadas.ambito.gestion"));

       //nÚMERO DEL ÁMBITO
       Field fNumero = report.findField("62");
       fNumero.setValue(aplicacion.getI18nString("informe.parcelas.afectadas.ambito.numero"));

       //Nombre del ámbito
       Field fNombre = report.findField("63");
       fNombre.setValue(aplicacion.getI18nString("informe.parcelas.afectadas.ambito.nombre"));

      //Plano de Referencia del ámbito
       Field fPlano = report.findField("61");
       fPlano.setValue(aplicacion.getI18nString("informe.parcelas.afectadas.ambito.plano"));

       //Listado de parcelas afectadas
       Field fListado = report.findField("21");
       fListado.setValue(aplicacion.getI18nString("informe.parcelas.afectadas.ambito.listado"));

       //Municipio
       Field fMunicipio = report.findField("8");
       fMunicipio.setValue(aplicacion.getI18nString("informe.parcelas.afectadas.ambito.municipio"));

       //Referencia Catastral
       Field fReferencia = report.findField("9");
       fReferencia.setValue(aplicacion.getI18nString("informe.parcelas.afectadas.ambito.referencia.catastral"));
               
       //Tipo Parcela
       //Field fTipo = report.findField("64");
       //fTipo.setValue(aplicacion.getI18nString("informe.parcelas.afectadas.ambito.tipo.parcela"));

       //Superficie Afectada
       Field fSuperficie = report.findField("10");
       fSuperficie.setValue(aplicacion.getI18nString("informe.parcelas.afectadas.superficie.afectada"));

       
       //Página
       Field fPagina = report.findField("79");
       fPagina.setValue(aplicacion.getI18nString("informe.parcelas.afectadas.pagina"));

            //Logo
                Field Logo = report.findField("51");
                Logo.setValue(aplicacion.getPath("logo.informes"));
        
        Field campoMapa = report.findField("75");
        campoMapa.setValue(rutaMapa);
        
        report.getDataSource().getQuery().setWhereClause(Where_c);
        report.setLayoutEngine(le);   // se lo asociamos al report
        report.runReport();           // procesamos la conversión







                 


    

   

  } catch (SAXException e){
    e.printStackTrace();
  }
  


    
  } //del JbInit

   public void setWizardContext(WizardContext wd)
    {
    }
    
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
         e = DriverManager.getDrivers();
      while (e.hasMoreElements())
      {
       DriverManager.deregisterDriver((Driver)e.nextElement());
      }
      
       DriverManager.registerDriver(new com.geopista.sql.GEOPISTADriver());
  }catch (Exception e){e.printStackTrace();}
    return conn;
};

public  Connection getDBConnection (String username, String password,
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

/* (non-Javadoc)
 * @see com.geopista.ui.wizard.WizardPanel#exiting()
 */
public void exiting()
{
    // TODO Auto-generated method stub
    
}
} //de la Clase


