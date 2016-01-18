package com.geopista.app.reports;
//clase que lanzará el informe Urbanístico de una parcela seleccionada
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.util.Map;

import javax.swing.JPanel;

import jimm.datavision.Field;
import jimm.datavision.Report;
import jimm.datavision.layout.swing.SwingLE;

import org.xml.sax.SAXException;

import com.geopista.app.AppContext;
import com.geopista.sql.GeopistaInformesPostgresCon;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.wizard.WizardPanel;

/*public class GeopistaImportarPlaneamiento02Panel extends JPanel implements WizardPanel
{*/

public class GeopistaInformacionReferenciaVias extends JPanel implements WizardPanel
{

 //private JPanel pnlVentana = new JPanel();
  AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
  public SwingLE le = new SwingLE();
  public GeopistaInformacionReferenciaVias()

  {
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

      try {
    
      // Establecemos la conexion con los que vengan en la aplicación 
         GeopistaInformesPostgresCon conexion = new GeopistaInformesPostgresCon();
         //Connection conn = conexion.getDBConnection("postgres","postgres","jdbc:postgresql://212.22.38.19:5432/geopista","org.postgresql.Driver");
          Connection conn = conexion.abrirConexion();
           report.setDatabaseConnection(conn);

         
        InputStream in = new FileInputStream(aplicacion.getPath("report.informacionReferencia.vias"));
        report.read(new InputStreamReader(in));

       report.setTitle(aplicacion.getI18nString("titulo.informacion.referencia"));

                //Logo
                Field Logo = report.findField("140");
                Logo.setValue(aplicacion.getPath("logo.informes"));

                //Titulo del Informe
                Field sSolar = report.findField("141");
                sSolar.setValue(aplicacion.getI18nString("informe.vias.titulo.informe"));
                
               //Código
                Field fCodigo = report.findField("131");
                fCodigo.setValue(aplicacion.getI18nString("informe.vias.codigo"));

               //Nombre
                Field fNombre = report.findField("132");
                fNombre.setValue(aplicacion.getI18nString("informe.vias.nombre"));

               //Longitud Via
                Field fLongitud = report.findField("133");
                fLongitud.setValue(aplicacion.getI18nString("informe.vias.longitud"));

               //Subtramo
                Field fSubtramo = report.findField("134");
                fSubtramo.setValue(aplicacion.getI18nString("informe.vias.subtramo"));

               //Fecha Alta
                Field fAlta = report.findField("135");
                fAlta.setValue(aplicacion.getI18nString("informe.vias.fecha.alta"));
            
               //Longitud subtramo
                Field fLongitudSubtramo = report.findField("136");
                fLongitudSubtramo.setValue(aplicacion.getI18nString("informe.vias.longitud.subtramo"));




            report.setLayoutEngine(le);   // se lo asociamos al report
            report.runReport();           // procesamos la conversión


                       


    

   

  } catch (SAXException e){
    e.printStackTrace();
  }
  


    
  } //del JbInit
     public void enteredFromLeft(Map dataMap){ }
     
    public void exitingToRight() throws Exception
    {
/*        //  String nombreParcela = txtPoligonoUrbano.getText().toString() + txtParcelaUrbano.getText().toString();
          int n = JOptionPane.showConfirmDialog(this,"¿Desea expedir el informe urbanístico de la parcela  nombreParcela ?",	"Expedir Informe Urbanístico",	JOptionPane.YES_NO_OPTION);

          if (n == JOptionPane.YES_OPTION)
          {
           this.jbInit();
          }
          else if (n == JOptionPane.NO_OPTION)
          {
           
          }
          else
          {
           
          }*/

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

    /**
     * @return null to turn the Next button into a Finish button
     */
    public String getNextID()
    {

        return null;
    }





    
  } //de la Clase


