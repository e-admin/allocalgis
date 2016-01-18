/**
 * GeopistaParcelasAfectadas.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.reports;

//clase que lanzará el informe Urbanístico de una parcela seleccionada
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import jimm.datavision.Field;
import jimm.datavision.Formula;
import jimm.datavision.Report;
import jimm.datavision.layout.swing.SwingLE;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.type.WhenNoDataTypeEnum;
import net.sf.jasperreports.engine.util.JRSaver;
import net.sf.jasperreports.view.JasperViewer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.planeamiento.GeopistaPlaneamientoInformesPostgreCon;
import com.geopista.app.reports.exceptions.ReportNotFoundException;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.config.UserPreferenceStore;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;

/*public class GeopistaImportarPlaneamiento02Panel extends JPanel implements WizardPanel
{*/

public class GeopistaParcelasAfectadas  extends JPanel implements WizardPanel
{

	private static final Log logger = LogFactory.getLog(GeopistaParcelasAfectadas.class);
  
  public String numeroAmbitoGestion="";
  public AppContext aplicacion =(AppContext) AppContext.getApplicationContext();
  public String rutaMapa="";
  public SwingLE le = new SwingLE();

  private static final String COMPILED_REPORT_DIR_NAME = "compiled";
  
  private static final String[] reportFiles = new String[] {
		"parcelasAfectadas.jrxml"
	};

  public GeopistaParcelasAfectadas(String numeroAmbitoGestion,String rutaMapa)

  {
      this.numeroAmbitoGestion=numeroAmbitoGestion;
      this.rutaMapa=rutaMapa;
    try
      {
    	//jbInit es la implementacion con Datavision
        //jbInit();
    	jbInit_ireport();
    }
        catch(Exception e)
      {
        e.printStackTrace();
      }   
  }
  
  private void jbInit_ireport(){
	  try {

			// Establecemos la conexion con los que vengan en la aplicación 
			Connection conn = abrirConexion();
			
			JasperPrint report = fillReport(conn);
			
			if (report!=null)
				JasperViewer.viewReport(report);
			else
				JOptionPane.showMessageDialog(this,"Error al generar el informe");
			
		} catch (Exception e){
			logger.error("Excepcion mientras se procesaba el informe", e);
		}
  }
  
  private JasperPrint fillReport(Connection dbConnection) throws ReportNotFoundException{
		JasperReport compiledReport = getCompiledReport();		
		
		JasperPrint jasperPrint = null;
		
		String localPath = UserPreferenceStore.getUserPreference(UserPreferenceConstants.PREFERENCES_DATA_PATH_KEY,UserPreferenceConstants.DEFAULT_DATA_PATH, true);
		String compiledReportPath = localPath + File.separator +
			COMPILED_REPORT_DIR_NAME + File.separator;
		
		HashMap parametersMap = new HashMap();
		parametersMap.put("SUBREPORT_DIR", compiledReportPath);
		parametersMap.put("ID_ENTIDAD", String.valueOf(aplicacion.getIdEntidad()));
		parametersMap.put("id_ambito", this.numeroAmbitoGestion);
		parametersMap.put("idMunicipio",
				new Integer(aplicacion.getString(UserPreferenceConstants.DEFAULT_MUNICIPALITY_ID)));
		
		try {
			jasperPrint = JasperFillManager.fillReport(compiledReport, parametersMap, 
				dbConnection);
		} catch (JRException e) {
			logger.error("Se ha producido un error al rellenar el informe ",e);			
			return jasperPrint;
		}
		
		return jasperPrint;
	}
	
	private JasperReport getCompiledReport() throws ReportNotFoundException{
		JasperReport  mainReport = null;
		
		for (int i = 0; i < reportFiles.length; i++){
			InputStream reportFileStream;
			String localPath = UserPreferenceStore.getUserPreference(UserPreferenceConstants.PREFERENCES_DATA_PATH_KEY,UserPreferenceConstants.DEFAULT_DATA_PATH, true);

			String reportPath = localPath + File.separator +
				UserPreferenceConstants.REPORT_DIR_NAME + File.separator + reportFiles[i];
			
			try {
				reportFileStream = new FileInputStream(reportPath);
			} catch (FileNotFoundException e1) {
				logger.error("No se ha encontrado el fichero con el informe");
				throw new ReportNotFoundException();
			}
			
			JasperReport report = null;
			
			try {
				report = JasperCompileManager.compileReport(reportFileStream);
			} catch (JRException e) {
				logger.error("Error al compilar el informe", e);
				return null;
			}
			
			//report.setWhenNoDataType(JRReport.WHEN_NO_DATA_TYPE_NO_DATA_SECTION);
			//JasperReport 4 y 5
			report.setWhenNoDataType(WhenNoDataTypeEnum.NO_DATA_SECTION);

			if (i != 0){
				String compiledReportName = reportFiles[i].substring(0,
						reportFiles[i].indexOf(".jrxml")) + ".jasper";
				

				String compiledReportDir = localPath + File.separator +
					COMPILED_REPORT_DIR_NAME;
				
				File compiledReportDirFile = new File(compiledReportDir);
				if (!compiledReportDirFile.exists()){
					compiledReportDirFile.mkdirs();
				}
				
				String compiledReportPath = localPath + File.separator +
					COMPILED_REPORT_DIR_NAME + File.separator + compiledReportName;

				try {
					JRSaver.saveObject(report, compiledReportPath);
				} catch (JRException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			else {
				mainReport = report;
			}
		}
		
		return mainReport;
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

         String unitName = UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_UNIT_NAME,"",true);
         String unitEquivalence = UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_UNIT_EQUIVALENCE,"",true);

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

  } catch (Throwable e){
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

    String url=aplicacion.getString(UserPreferenceConstants.LOCALGIS_DATABASE_DIRECTCONNECTION_URL);
    String user = aplicacion.getString(UserPreferenceConstants.LOCALGIS_DATABASE_DIRECTCONNECTION_USER);
    String pass =aplicacion.getString(UserPreferenceConstants.LOCALGIS_DATABASE_DIRECTCONNECTION_PASS);
    //String pass =UserPreferenceStore.getUserPreference(UserPreferenceConstants.LOCALGIS_DATABASE_DIRECTCONNECTION_PASS,"",false);
    String driver =aplicacion.getString(UserPreferenceConstants.LOCALGIS_DATABASE_DIRECTCONNECTION_DRIVER);
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


