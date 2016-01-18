/**
 * DatosPersonalesSistemaPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.intercambio.edicion.dialogs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.geopista.app.catastro.intercambio.edicion.DatosPersonalesSistemaDialog;
import com.geopista.app.catastro.intercambio.edicion.utils.EdicionOperations;
import com.geopista.app.catastro.intercambio.edicion.utils.TableDatosPersonalesModel;
import com.geopista.app.utilidades.TableSorted;
import com.geopista.protocol.datosPersonales.DatosPersonales;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

public class DatosPersonalesSistemaPanel extends JPanel{

	 /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1L;
    private JPanel jPanelTitulares = null;  
    private JScrollPane jScrollPaneTitulares = null;
    private JTable jTableTitulares = null; 
    private TableDatosPersonalesModel tablemodel;
    
    private String nif ="";
   	private String nombreApellidos = "";
   	private int parametro;
    
   	private DatosPersonales datosPersonales=null;
   
	private ArrayList lstTitulares;
	
	private boolean okPressed = false;
    
    public DatosPersonalesSistemaPanel(GridBagLayout layout, String nif, String nombreApellidos)
    {
    	super(layout);
        this.nif = nif;
        this.nombreApellidos = nombreApellidos;
        initialize();
    }
    
    public ArrayList getLstTitulares() {
		return lstTitulares;
	}

	public void setLstTitulares(ArrayList lstTitulares) {
		this.lstTitulares = lstTitulares;
	}
    
	private void initialize()
    {     
        Locale loc=I18N.getLocaleAsObject();         
        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.catastro.intercambio.language.Expedientesi18n",
                loc,this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("Expedientes",bundle);
        
        this.setLayout(new GridBagLayout());
        this.setSize(DatosPersonalesSistemaDialog.DIM_X,DatosPersonalesSistemaDialog.DIM_Y);
        this.add(getJPanelTitulares(),  new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 5, 0, 5), 0, 150));
        
        
    }
	
	 /**
     * This method initializes jPanelTitulares	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanelTitulares()
    {
        if (jPanelTitulares == null)
        {
        	jPanelTitulares = new JPanel(new GridBagLayout());
        	jPanelTitulares.setBorder(BorderFactory.createTitledBorder(I18N.get("Expedientes","busqueda.titulares.titulo")));
        	jPanelTitulares.add(getJScrollPaneTitulares(), 
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 5, 0, 5), 0, 0));
        }
        return jPanelTitulares;
    }
	
    /**
     * This method initializes jScrollPaneTitulares	
     * 	
     * @return javax.swing.JScrollPane	
     */
    private JScrollPane getJScrollPaneTitulares()
    {
        if (jScrollPaneTitulares == null)
        {
        	jScrollPaneTitulares = new JScrollPane();
        	jScrollPaneTitulares.setViewportView(getJTableDatosPersonales());
        }
        return jScrollPaneTitulares;
    }
    
    /**
     * This method initializes getJTableDatosPersonales	
     * 	
     * @return javax.swing.JTable	
     */
    private JTable getJTableDatosPersonales()
    {
        if (jTableTitulares == null)
        {
        	
        	final JFrame desktop = new JFrame();
        	final TaskMonitorDialog progressDialog= new TaskMonitorDialog(desktop, null);
            
            progressDialog.setTitle("TaskMonitorDialog.Wait");
            progressDialog.report(I18N.get("Expedientes","busqueda.titulares.dialog.titulo"));
            progressDialog.addComponentListener(new ComponentAdapter()
            {
                public void componentShown(ComponentEvent e)
                {
                    new Thread(new Runnable()
                    {
                        public void run()
                        {
                            try
                            {
                            	EdicionOperations oper = new EdicionOperations();
                            	lstTitulares = oper.obtenerTitulares(getNif(), getNombreApellidos());

                            }
                            catch(Exception e)
                            {

                                ErrorDialog.show(desktop, "ERROR", "ERROR", StringUtil.stackTrace(e));
                                return;
                            }
                            finally
                            {
                                progressDialog.setVisible(false);                                
                                progressDialog.dispose();
                            }
                        }
                  }).start();
              }
           });
           GUIUtil.centreOnScreen(progressDialog);
           progressDialog.setVisible(true);
           
        	jTableTitulares = new JTable();
            
            String[] columnNames = {I18N.get("Expedientes","busqueda.titulares.nif"),
                    I18N.get("Expedientes","busqueda.titulares.nombreApellidos")};
            tablemodel = new TableDatosPersonalesModel(columnNames);
            
            TableSorted tblSortedDatosPersonales= new TableSorted(tablemodel);
            tblSortedDatosPersonales.setTableHeader(jTableTitulares.getTableHeader());
            jTableTitulares.setModel(tblSortedDatosPersonales);
            jTableTitulares.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            jTableTitulares.setCellSelectionEnabled(false);
            jTableTitulares.setColumnSelectionAllowed(false);
            jTableTitulares.setRowSelectionAllowed(true);
            jTableTitulares.getTableHeader().setReorderingAllowed(false);
           
    
          ((TableDatosPersonalesModel)((TableSorted)jTableTitulares.getModel()).getTableModel()).setData(lstTitulares);
    
           
           ListSelectionModel rowSM = jTableTitulares.getSelectionModel();
           rowSM.addListSelectionListener(new ListSelectionListener() {
               public void valueChanged(ListSelectionEvent e) {
                   ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                   if (lsm.isSelectionEmpty()) {
                   	
                   } 
                   else 
                   {
                	   int indexSelected = ((TableSorted)jTableTitulares.getModel()).modelIndex(lsm.getMinSelectionIndex());
                       datosPersonales =(DatosPersonales) getLstTitulares().get(indexSelected);

                   }
               }
   	
           });   
        }
   
        return jTableTitulares;
    }
    
    public String getNif() {
		return nif;
	}

	public void setNif(String nif) {
		this.nif = nif;
	}

	public String getNombreApellidos() {
		return nombreApellidos;
	}

	public void setNombreApellidos(String nombreApellidos) {
		this.nombreApellidos = nombreApellidos;
	}
	
	public int getParametro() {
		return parametro;
	}

	public void setParametro(int parametro) {
		this.parametro = parametro;
	}
	
	public String validateInput()
    {
        return null;
    }
    
    
	public void okPressed()
    {
       okPressed = true;
    }
	
	
	public DatosPersonales getDatosPersonales() {
		return datosPersonales;
	}

	public void setDatosPersonales(DatosPersonales datosPersonales) {
		this.datosPersonales = datosPersonales;
	}


}
