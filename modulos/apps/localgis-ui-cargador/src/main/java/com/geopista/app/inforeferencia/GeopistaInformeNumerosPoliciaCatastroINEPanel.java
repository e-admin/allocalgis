/**
 * GeopistaInformeNumerosPoliciaCatastroINEPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.inforeferencia;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.geopista.app.AppContext;
import com.geopista.security.GeopistaPermission;
import com.geopista.sql.GeopistaSQL;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;

/**
 * GeopistaInformeNumerosPoliciaCatastroINEPanel
 * Panel que procesa relaciona semi-automáticamente los registros de tramosviaine y vias
 * ofreciendo la posibilidad al usuario de realizar asociaciones no contempladas por el 
 * algoritmo de enlazado
 */
 
public class GeopistaInformeNumerosPoliciaCatastroINEPanel extends JPanel implements ListSelectionListener, WizardPanel 
{

  private JScrollPane pnlCoincidentes = new JScrollPane();
  private JTable tblCoincidentes = null;
  public ApplicationContext appContext=AppContext.getApplicationContext();
  private JLabel lblInformeNumerosPoliciaCatastroINE = new JLabel();
  private JScrollPane pnlNoCoincidentes = new JScrollPane();
  private JTable tblNoCoincidentes = null;
  private JLabel lblInformeNumerosPoliciaCatastroINE_no = new JLabel();

  public boolean acceso;
  private JOptionPane OpCuadroDialogo;
  
  public GeopistaInformeNumerosPoliciaCatastroINEPanel()
  {
    try
    {
      GeopistaPermission geopistaPerm = new GeopistaPermission("Geopista.InfReferencia.GeopistaAsociarCatastroINEVias");
      acceso = appContext.checkPermission(geopistaPerm,"Informacion de Referencia");
      if (acceso){
        jbInit();
      }else
      {
           OpCuadroDialogo.showMessageDialog(null,appContext.getI18nString("SinAcceso"));
      }
      
    }catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception
  {
    this.setSize(new Dimension(800, 600));
    this.setLayout(null);
    pnlCoincidentes.setBounds(new Rectangle(25, 75, 755, 175));
    Vector columnas = rellenarColumnas("inforeferencianumerospoliciaselectcoincidentes");
    Vector filas = rellenarFilas(columnas,"inforeferencianumerospoliciaselectcoincidentes");
    tblCoincidentes = new JTable(filas, columnas);
    tblCoincidentes.setPreferredScrollableViewportSize(new Dimension(500, 70));

    columnas = rellenarColumnas("inforeferencianumerospoliciaselectnocoincidentes");
    filas = rellenarFilas(columnas,"inforeferencianumerospoliciaselectnocoincidentes");
    tblNoCoincidentes = new JTable(filas, columnas);
    tblNoCoincidentes.setPreferredScrollableViewportSize(new Dimension(500, 70));
    
    lblInformeNumerosPoliciaCatastroINE.setText(appContext.getI18nString("lblInformeNumerosPoliciaCatastroINE"));
    lblInformeNumerosPoliciaCatastroINE.setBounds(new Rectangle(25, 45, 755, 20));
    pnlNoCoincidentes.setBounds(new Rectangle(25, 315, 755, 165));
    lblInformeNumerosPoliciaCatastroINE_no.setText(appContext.getI18nString("lblInformeNumerosPoliciaCatastroINE_no"));
    lblInformeNumerosPoliciaCatastroINE_no.setBounds(new Rectangle(25, 275, 560, 25));
    pnlCoincidentes.getViewport().add(tblCoincidentes, null);
    pnlNoCoincidentes.getViewport().add(tblNoCoincidentes, null);
    this.add(lblInformeNumerosPoliciaCatastroINE_no, null);
    this.add(pnlNoCoincidentes, null);
    this.add(lblInformeNumerosPoliciaCatastroINE, null);
    this.add(pnlCoincidentes, null);
    
  }

  /**
   * rellenarColumnas(String queryName)
   * rellena los nombres de las columnas de un JTable
   * 
   * @param queryName :Consulta que se ejecuta
   * @return Devuelve un objeto de tipo Vector con los nombres de las columnas
   */
  private Vector rellenarColumnas(String queryName)
  {
    
    String [] columnNames = null;
    try
    {
      Connection conn = appContext.getConnection();
      columnNames = GeopistaSQL.Columns(queryName,"",conn);
    }catch(SQLException esql)
    {
      esql.printStackTrace();
    }
    Vector columnNamesV = new Vector(Arrays.asList(columnNames));
    return columnNamesV;
  }


  /**
   * rellenarFilas(Vector columns, String queryName)
   * rellena las filas de un JTable con los datos procedentes de una consulta a la
   * Base de Datos
   * 
   * @param queryName :Consulta que se ejecuta
   * @return Devuelve un objeto de tipo Vector con los datos de las filas
   */

   
  private Vector rellenarFilas(Vector columns, String queryName)
  {
    ResultSet filas=null;
    Vector rowData = null;
    try
    {
      rowData = new Vector();
      Connection conn = appContext.getConnection();
      filas = GeopistaSQL.Query(queryName,"",conn);
      
      while(filas.next())
      {
        //
        String temp = "";
        int numColumnas = columns.size();
       
        for(int i=1;i<=numColumnas;i++)
        {
          
          if(filas.getMetaData().getColumnTypeName(i).equals("numeric"))
          {
            temp += String.valueOf(filas.getInt(i))+";";
          }
          if(filas.getMetaData().getColumnTypeName(i).equals("varchar"))
          {
            temp += filas.getString(i)+";";
          }
          if(filas.getMetaData().getColumnTypeName(i).equals("date"))
          {
            if(filas.getDate(i)==null)
            {
              temp += ";";
            }else
            {
              temp += filas.getDate(i).toString()+";";
            }
            
          }
        }
        String[] temp2 = null;
        temp2 = temp.split("\\;");
        Vector colData = new Vector(Arrays.asList(temp2));
        rowData.add(colData);
      }
    }catch(Exception ex)
    {
      ex.printStackTrace();
    }finally
    {
      return rowData;
    }
  
    
    
    
    
    
  }


  /************************Interface**********************/
  //This method is required by ListSelectionListener.
  public void valueChanged(ListSelectionEvent e) {
        
  }
  public void enteredFromLeft(Map dataMap)
  {

  }

    /**
     * Called when the user presses Next on this panel
     */
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
    
      public void setWizardContext(WizardContext wd)
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

