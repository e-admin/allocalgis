/**
 * GeopistaInformeCatastral.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.reports;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;

import javax.swing.JPanel;

import jimm.datavision.Field;
import jimm.datavision.Formula;
import jimm.datavision.Report;
import jimm.datavision.layout.swing.SwingLE;

import org.xml.sax.SAXException;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.catastro.GeopistaInformesPostgresCon;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.config.UserPreferenceStore;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;

/**
 * Clase para lanzar el informe de Catastro
 */
public class GeopistaInformeCatastral extends JPanel implements WizardPanel
{

 //private JPanel pnlVentana = new JPanel();
  
  public int parcela=0;
  public String rutaMapa="";
  public SwingLE le = new SwingLE();
  AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
  public GeopistaInformeCatastral(int parcela,String rutaMapa)

  {
      this.parcela=parcela;
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
      String Where_c = "{parcelas.id}="+ this.parcela + " and  {municipios.id="+ aplicacion.getString(UserPreferenceConstants.DEFAULT_MUNICIPALITY_ID) +"}";
      try {
    
      // Establecemos la conexion con los que vengan en la aplicación 

         //Connection conn = conexion.getDBConnection("postgres","postgres","jdbc:postgresql://212.22.38.19:5432/geopista","org.postgresql.Driver");

           Connection conn = abrirConexion();
           report.setDatabaseConnection(conn);

      Enumeration e = DriverManager.getDrivers();
      while (e.hasMoreElements())
      {
       DriverManager.deregisterDriver((Driver)e.nextElement());
      }
       DriverManager.registerDriver(new com.geopista.sql.GEOPISTADriver());

           
          // Format the current time.
        Class.forName(aplicacion.getString(UserPreferenceConstants.LOCALGIS_DATABASE_DRIVER));
        GeopistaInformesPostgresCon conexion = new GeopistaInformesPostgresCon();
        SimpleDateFormat formatter = new SimpleDateFormat ("dd 'de'  MMMMM  'de' yyyy");
        Date currentTime_1 = new Date();
        String dateString = formatter.format(currentTime_1);

        // Leemos el report de disco

        InputStream in = new FileInputStream(aplicacion.getPath("report.catastro.parcelas"));
        report.read(new InputStreamReader(in));

       report.setTitle(aplicacion.getI18nString("titulo.informe.catastro.parcelas"));
          
            //Se cambia la fecha con un campo fórmula 
             Formula a  = report.findFormula("2");
             String expresion ="['" + dateString.toString() +"']";
             a.setExpression(expresion);

             //Cambiamos el distrito Censal del informe 
              // 1. Distrito Censal
                Formula a1  = report.findFormula("1");
                String expresion1 ="[' " + conexion.distritoCensal(this.parcela) +" ']";
                a1.setExpression(expresion1);

              // El tipo de Via se pone como formula
              // 3. Codigo via
                Formula a3  = report.findFormula("3");
                String expresion3 ="['" + conexion.codigoVia(this.parcela) +" ']";
                a3.setExpression(expresion3);

                // El tipo de Via se pone como formula
              // 3. Codigo via
                Formula a4  = report.findFormula("4");
                String expresion4 ="['" + conexion.nombreVia(this.parcela) +" ']";
                a4.setExpression(expresion4);
              
                Field campoMapa = report.findField("4");
                campoMapa.setValue(rutaMapa);
                
                String unitName = UserPreferenceStore.getUserPreference("unidad.nombre","",true);
                String unitEquivalence = UserPreferenceStore.getUserPreference("unidad.equivalence","",true);
                
                Formula a5  = report.findFormula("5");
                String expresion5 = "";
                try
                {
	                //Para la conversion a unidades de medida equivalente
                    int tempInt = Integer.parseInt(unitEquivalence);
                    unitEquivalence = String.valueOf(tempInt);
	                expresion5 ="['"+unitEquivalence+"']";
	                
                }catch(Exception e1)
                {
                    unitEquivalence = "";
                }
                a5.setExpression(expresion5);
                
                Formula a11 = report.findFormula("11");
                String expresion11 = "";
                if(!unitEquivalence.equals("")) expresion11="['"+unitName+"']";
                a11.setExpression(expresion11);
              
                

              //I18N de las etiquetas del informe
                //Titulo del Informe
                Field titulo = report.findField("117");
                titulo.setValue(aplicacion.getI18nString("informe.catastral.titulo.informe"));


                //Referencia catastral del a parcela
                Field refCat = report.findField("10");
                refCat.setValue(aplicacion.getI18nString("informe.catastral.referencia.catastral"));


                //Referencia catastral del a parcela
                Field identificacion = report.findField("13");
                identificacion.setValue(aplicacion.getI18nString("informe.catastral.identificacion.parcela"));


                //Municipio
                Field municipio = report.findField("14");
                municipio.setValue(aplicacion.getI18nString("informe.catastral.municipio"));


                //Provincia
                Field provincia = report.findField("15");
                 provincia.setValue(aplicacion.getI18nString("informe.catastral.provincia"));
                 
              //Domicilio Tributario
                Field domicilioTributario = report.findField("16");
                domicilioTributario.setValue(aplicacion.getI18nString("informe.catastral.domicilio.tributario"));
 

               //Código Postal
                Field cPostal = report.findField("17");
                cPostal.setValue(aplicacion.getI18nString("informe.catastral.codigo.postal"));

                //Distrito Censal
                Field dCensal = report.findField("18");
                dCensal.setValue(aplicacion.getI18nString("informe.catastral.distrito.censal"));

                
               //Datos Físicos
                Field dFisicos = report.findField("19");
                dFisicos.setValue(aplicacion.getI18nString("informe.catastral.datos.fisicos"));
                

               //Superficie Solar
                Field sSolar = report.findField("20");
                sSolar.setValue(aplicacion.getI18nString("informe.catastral.superficie.solar"));
                

               //Superficie Total Construida
               // Field sTotal = report.findField("21");
               //  sTotal.setValue(aplicacion.getI18nString("informe.catastral.superficie.total"));
                

               //Superficie Sobre Rasante
               // Field sBrasante = report.findField("22");
               // sBrasante.setValue(aplicacion.getI18nString("informe.catastral.superficie.sobre.rasante"));
          

               //Superficie Bajo Rasante
               // Field sBjrasante = report.findField("23");
               // sBjrasante.setValue(aplicacion.getI18nString("informe.catastral.superficie.bajo.rasante"));


               //Superficie Cubierta
               // Field sCubierta = report.findField("24");
               // sCubierta.setValue(aplicacion.getI18nString("informe.catastral.superficie.cubierta"));
                
            //Logo
                Field Logo = report.findField("116");
                Logo.setValue(aplicacion.getPath("logo.informes"));


                 report.getDataSource().getQuery().setWhereClause(Where_c);
              

//    Quitamos los drivers y dejamos GeopistaDriver       

            report.setLayoutEngine(le);   // se lo asociamos al report
            report.runReport();           // procesamos la conversión

  

  } catch (SAXException e){
    e.printStackTrace();
  }
  


    
  } //del JbInit
     public void enteredFromLeft(Map dataMap){ }
     
    public void exitingToRight() throws Exception
    {
        Connection cnn  = aplicacion.getConnection();
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
