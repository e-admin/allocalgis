/**
 * GeopistaSupfNoCoincidentesPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.geopista.app.patrimonio;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import jimm.datavision.Report;
import jimm.datavision.layout.swing.SwingLE;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.reports.GeopistaGeneradorListadosConexionBD;
import com.geopista.app.reports.GeopistaInformeSuperficiesNoCoincidentes;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.config.UserPreferenceStore;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;

public class GeopistaSupfNoCoincidentesPanel  extends JPanel implements WizardPanel
{
  private JRadioButton rdbTodosBienes = new JRadioButton();
  private JRadioButton rdbSeleccionar = new JRadioButton();
  private JLabel lblTodos = new JLabel();
  private JTextField txtInventario = new JTextField();
  private JLabel lblSeleccionar = new JLabel();
  private AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
  public GeopistaSupfNoCoincidentesPanel()
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
    lblTodos.setText(aplicacion.getI18nString("sigap.todos.bienes"));
    lblTodos.setBounds(new Rectangle(35, 20, 120, 20));

    txtInventario.setBounds(new Rectangle(200, 50, 150, 20));

    lblSeleccionar.setText(aplicacion.getI18nString("sigap.numero.inventario"));
    lblSeleccionar.setBounds(new Rectangle(35, 50, 175, 15));


    
    ButtonGroup grupoOpciones = new ButtonGroup();
    txtInventario.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          txtInventario_actionPerformed(e);
        }
      });
    rdbTodosBienes.setBounds(new Rectangle(15, 20, 20, 20));
    rdbSeleccionar.setBounds(new Rectangle(15, 45, 20, 25));
    grupoOpciones.add(rdbTodosBienes);
    grupoOpciones.add(rdbSeleccionar);
    rdbTodosBienes.setSelected(true);

    this.setLayout(null);
    this.setSize(new Dimension(370, 88));
    this.add(lblSeleccionar, null);
    this.add(txtInventario, null);
    this.add(lblTodos, null);
    this.add(rdbSeleccionar, null);
    this.add(rdbTodosBienes, null);
  }
  
  private void btnSiguiente_actionPerformed(ActionEvent e)
  {
  }

  private void anteriorbtn_actionPerformed(ActionEvent e)
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

    private WizardContext wizardContext;
   public void setWizardContext(WizardContext wd)
    {
      wizardContext=wd;
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

 public void remove(InputChangedListener listener)
    {
      
    }

   public void add(InputChangedListener listener)
    {
      
    }

    public void enteredFromLeft(Map dataMap)
  {
    //Cargaremos en el combo los valores de los ambitos de Gestión.

    
  }

    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {
       if ((txtInventario.getText())!=null){
        //Lanzamos el informe Total
            Report report = new Report();
              SwingLE le = new SwingLE();
              GeopistaGeneradorListadosConexionBD conectar = new GeopistaGeneradorListadosConexionBD();
             
              String url=aplicacion.getString(UserPreferenceConstants.LOCALGIS_DATABASE_DIRECTCONNECTION_URL);
              String user =aplicacion.getString(UserPreferenceConstants.LOCALGIS_DATABASE_DIRECTCONNECTION_USER);
              String pass =aplicacion.getString(UserPreferenceConstants.LOCALGIS_DATABASE_DIRECTCONNECTION_PASS);
              //String pass =UserPreferenceStore.getUserPreference(UserPreferenceConstants.LOCALGIS_DATABASE_DIRECTCONNECTION_PASS,"",false);
              String driver =aplicacion.getString(UserPreferenceConstants.LOCALGIS_DATABASE_DIRECTCONNECTION_DRIVER);
              String informe = aplicacion.getPath("report.patrimonio.superficie.distinta");
              Connection conn = conectar.abrirConexionPostgres() ;conectar.getDBConnection(user,pass,url, driver);

              report.setDatabaseConnection(conn);
              //report.readFile(informe);
              
              if (rdbTodosBienes.isSelected()){
                  GeopistaInformeSuperficiesNoCoincidentes geo = new GeopistaInformeSuperficiesNoCoincidentes(0);
              } else{
                  int numero=0;
                  numero = Integer.parseInt(txtInventario.getText());
                  GeopistaInformeSuperficiesNoCoincidentes geo = new GeopistaInformeSuperficiesNoCoincidentes(numero);
              }              
              

       }else{
        //Debemos pasar un valor al report de selección de parcela.
        //Ejecutar el informe.
       }
    
     }

  private void txtInventario_actionPerformed(ActionEvent e)
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