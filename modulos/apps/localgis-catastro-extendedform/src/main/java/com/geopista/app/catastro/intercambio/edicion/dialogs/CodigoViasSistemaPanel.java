/**
 * CodigoViasSistemaPanel.java
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
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.geopista.app.catastro.intercambio.edicion.CodigoViasSistemaDialog;
import com.geopista.app.catastro.intercambio.edicion.utils.EdicionOperations;
import com.geopista.app.catastro.intercambio.edicion.utils.TableCodigoViasModel;
import com.geopista.app.utilidades.TableSorted;
import com.vividsolutions.jump.I18N;


public class CodigoViasSistemaPanel extends JPanel
{
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1L;
    private JPanel jPanelCodigoViasCatastro = null;  
    private JScrollPane jScrollPaneCodigoViasCatastro = null;
    private JTable jTableCodigoViasCatastro = null;
    private TableCodigoViasModel tablemodel;
    
    private String codigoVia;
    
    private ArrayList lstCodigoVias;
    
    private boolean okPressed = false;
    
    /**
     * This method initializes
     * 
     */
    public CodigoViasSistemaPanel()
    {
        super();
        this.codigoVia = "";
        initialize();
    }
    
    
    
    public ArrayList getLstCodigoVias() {
		return lstCodigoVias;
	}



	public void setLstCodigoVias(ArrayList lstCodigoVias) {
		this.lstCodigoVias = lstCodigoVias;
	}



	private void initialize()
    {     
        Locale loc=I18N.getLocaleAsObject();         
        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.catastro.intercambio.language.Expedientesi18n",
                loc,this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("Expedientes",bundle);
        
        this.setLayout(new GridBagLayout());
        this.setSize(CodigoViasSistemaDialog.DIM_X,CodigoViasSistemaDialog.DIM_Y);
        this.add(getJPanelCodigoViasCatastro(),  new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 5, 0, 5), 0, 150));
        
        
    }
    
    public CodigoViasSistemaPanel(GridBagLayout layout)
    {
        super(layout);
        this.codigoVia = "";
        initialize();
        
    }
    
    public String getCodigoVia()
    {
    	return codigoVia;
    }
    
    
    public void setCodigoVia(String codigoVia) {
		this.codigoVia = codigoVia;
	}



	public String validateInput()
    {
        return null;
    }
    
    
    public void okPressed()
    {
       okPressed = true;
    }
    
    /**
     * This method initializes jPanelViasCatastro	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanelCodigoViasCatastro()
    {
        if (jPanelCodigoViasCatastro == null)
        {
            jPanelCodigoViasCatastro = new JPanel(new GridBagLayout());
            jPanelCodigoViasCatastro.setBorder(BorderFactory.createTitledBorder(I18N.get("Expedientes","busqueda.codigovias.titulo")));
            jPanelCodigoViasCatastro.add(getJScrollPaneCodigoViasCatastro(), 
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 5, 0, 5), 0, 0));
        }
        return jPanelCodigoViasCatastro;
    }
    
    /**
     * This method initializes jScrollPaneViasCatastro	
     * 	
     * @return javax.swing.JScrollPane	
     */
    private JScrollPane getJScrollPaneCodigoViasCatastro()
    {
        if (jScrollPaneCodigoViasCatastro == null)
        {
        	jScrollPaneCodigoViasCatastro = new JScrollPane();
        	jScrollPaneCodigoViasCatastro.setViewportView(getJTableCodigoViasCatastro());
        }
        return jScrollPaneCodigoViasCatastro;
    }
    
    /**
     * This method initializes jTableViasCatastro	
     * 	
     * @return javax.swing.JTable	
     */
    private JTable getJTableCodigoViasCatastro()
    {
        if (jTableCodigoViasCatastro == null)
        {
        	jTableCodigoViasCatastro = new JTable();
            
            tablemodel = new TableCodigoViasModel();
            
            TableSorted tblSortedVias= new TableSorted(tablemodel);
            tblSortedVias.setTableHeader(jTableCodigoViasCatastro.getTableHeader());
            jTableCodigoViasCatastro.setModel(tblSortedVias);
            jTableCodigoViasCatastro.setLayout(new GridBagLayout());
            jTableCodigoViasCatastro.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            jTableCodigoViasCatastro.setCellSelectionEnabled(false);
            jTableCodigoViasCatastro.setColumnSelectionAllowed(false);
            jTableCodigoViasCatastro.setRowSelectionAllowed(true);
            jTableCodigoViasCatastro.getTableHeader().setReorderingAllowed(false);

            EdicionOperations oper = new EdicionOperations();
            lstCodigoVias = oper.obtenerCodigoVias();            	
            ((TableCodigoViasModel)((TableSorted)jTableCodigoViasCatastro.getModel()).getTableModel()).setData(lstCodigoVias);
                          
        }
        
          
        ListSelectionModel rowSM = jTableCodigoViasCatastro.getSelectionModel();
        rowSM.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                if (lsm.isSelectionEmpty()) {
                    //System.out.println("No rows are selected.");
                    
                } 
                else 
                {
                    int indexSelected = lsm.getLeadSelectionIndex();
                    String codigoVia = (String) getLstCodigoVias().get(indexSelected);
                    setCodigoVia(codigoVia);
                    
                }
            }
	
        });   
        
        
        
        return jTableCodigoViasCatastro;
    }
    
}
