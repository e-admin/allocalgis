/**
 * GeopistaLanzarDisenadorInformes.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.reports;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import jimm.datavision.Report;
import jimm.datavision.gui.DesignWin;
import jimm.datavision.layout.swing.SwingLE;

import com.geopista.app.AppContext;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;

public class GeopistaLanzarDisenadorInformes extends JPanel implements WizardPanel
{


  AppContext aplicacion = (AppContext) AppContext.getApplicationContext();

  public SwingLE le = new SwingLE();
  private Blackboard blackboardInformes  = aplicacion.getBlackboard();

  public GeopistaLanzarDisenadorInformes()
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
    this.setLayout(null);
    this.setSize(new Dimension(614, 500));
    jPanel1.setBounds(new Rectangle(40, 40, 250, 185));
    jPanel1.setBounds(new Rectangle(125, 10, 280, 475));
    jPanel1.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
    jPanel1.setLayout(null);
    
      cmbAccion.addItem(aplicacion.getI18nString("generador.app.reports.lanzar.ejecutar"));
      cmbAccion.addItem(aplicacion.getI18nString("generador.app.reports.lanzar.disenar"));
     cmbAccion.addItem(aplicacion.getI18nString("generador.app.reports.lanzar.terminar"));


    cmbAccion.setBounds(new Rectangle(5, 70, 250, 20));
    cmbAccion.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          cmbAccion_actionPerformed(e);
        }
      });
    jLabel1.setText(aplicacion.getI18nString("generador.app.reports.lanzar.seleccion"));
    jLabel1.setBounds(new Rectangle(5, 30, 250, 20));
    jLabel2.setBounds(new Rectangle(10, 10, 250, 20));
    jLabel2.setFont(new Font("Dialog", 1, 11));
    lblImagen.setBounds(new Rectangle(5, 10, 110, 475));
    lblImagen.setIcon(IconLoader.icon((String)blackboardInformes.get("tipoBanner")));
    lblImagen.setBorder(BorderFactory.createLineBorder(Color.black, 1));
    lblImagen.setBorder(BorderFactory.createLineBorder(Color.black, 1));
    jPanel1.add(jLabel2, null);
    jPanel1.add(jLabel1, null);
    jPanel1.add(cmbAccion, null);
    this.add(jPanel1, null);
    this.add(lblImagen, null);
    this.add(jPanel1, null);
   
    
  }
 
  public void enteredFromLeft(Map dataMap)
  {
  
  }

    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {
      if (cmbAccion.getSelectedItem().equals(aplicacion.getI18nString("generador.app.reports.lanzar.terminar")))
      {
      }else{
        File informeFile = (File) blackboardInformes.get("fichero");
        String informe = informeFile.getAbsolutePath();
        
        if (cmbAccion.getSelectedItem().equals(aplicacion.getI18nString("generador.app.reports.lanzar.disenar"))){
           GeopistaGeneradorListadosConexionBD conectar = new GeopistaGeneradorListadosConexionBD();
           Connection cnn= conectar.abrirConexionPostgres();
            DesignWin win = new DesignWin(informe, null);
            win.getReport();
           //Cerrar la conexion y dejar la de Geopista
             Enumeration e = DriverManager.getDrivers();
              while (e.hasMoreElements())
              {
               DriverManager.deregisterDriver((Driver)e.nextElement());
              }
               DriverManager.registerDriver(new com.geopista.sql.GEOPISTADriver());           

        }else{
              
              Report report = new Report();
              GeopistaGeneradorListadosConexionBD conectar = new GeopistaGeneradorListadosConexionBD();
              
             Connection conn =conectar.abrirConexionPostgres();
             report.setDatabaseConnection(conn);
             //report.readFile(informe);
             InputStream in = new FileInputStream(informe);
             report.read(new InputStreamReader(in));
             //Cerrar la conexion y dejar la de Geopista
             Enumeration e = DriverManager.getDrivers();
              while (e.hasMoreElements())
              {
               DriverManager.deregisterDriver((Driver)e.nextElement());
              }
               DriverManager.registerDriver(new com.geopista.sql.GEOPISTADriver());           
            
             report.setLayoutEngine(le);  
             report.runReport();      
  
          
        }
      }        
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
      return " ";
    }

    public String getID()
    {
      return "7";
    }
  private WizardContext wizardContext;
  private JPanel jPanel1 = new JPanel();
  private JComboBox cmbAccion = new JComboBox();
  private JLabel jLabel1 = new JLabel();
  private JLabel jLabel2 = new JLabel();


     
 



  
 
    public void setWizardContext(WizardContext wd)
    {

      wizardContext = wd;
    }
    public String getInstructions()
    {
     return " ";
    }

    public boolean isInputValid()
    {

      return true;
    }

    private String nextID=null;
   private JLabel lblImagen = new JLabel();
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

  private void cmbAccion_actionPerformed(ActionEvent e)
  {
  }


/* (non-Javadoc)
 * @see com.geopista.ui.wizard.WizardPanel#exiting()
 */
public void exiting()
{
    // TODO Auto-generated method stub
    
}

}
