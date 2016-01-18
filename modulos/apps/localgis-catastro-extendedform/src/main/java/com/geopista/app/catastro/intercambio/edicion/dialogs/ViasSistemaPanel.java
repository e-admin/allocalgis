/**
 * ViasSistemaPanel.java
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

import com.geopista.app.catastro.intercambio.edicion.ViasSistemaDialog;
import com.geopista.app.catastro.intercambio.edicion.utils.EdicionOperations;
import com.geopista.app.catastro.intercambio.edicion.utils.TableViasCatastroModel;
import com.geopista.app.catastro.intercambio.exception.DataExceptionCatastro;
import com.geopista.app.catastro.model.beans.Municipio;
import com.geopista.app.utilidades.TableSorted;
import com.geopista.protocol.catastro.Via;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;


public class ViasSistemaPanel extends JPanel
{
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1L;
    private JPanel jPanelViasCatastro = null;  
    private JScrollPane jScrollPaneViasCatastro = null;
    private JTable jTableViasCatastro = null;
    private TableViasCatastroModel tablemodel;
    
    private String nomVia;
    private String tipoVia;
    private String idMunicipio;
    private Municipio municipio;
  
	
	private int codigoVia;
    
    private ArrayList lstVias;
    
    private boolean okPressed = false;
    
    /**
     * This method initializes
     * 
     */
    public ViasSistemaPanel(String nomVia)
    {
        super();
        this.nomVia = nomVia;
        initialize();
    }
    
    
    
    public ArrayList getLstVias() {
		return lstVias;
	}



	public void setLstVias(ArrayList lstVias) {
		this.lstVias = lstVias;
	}



	private void initialize()
    {     
        Locale loc=I18N.getLocaleAsObject();         
        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.catastro.intercambio.language.Expedientesi18n",
                loc,this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("Expedientes",bundle);
        
        this.setLayout(new GridBagLayout());
        this.setSize(ViasSistemaDialog.DIM_X,ViasSistemaDialog.DIM_Y);
        this.add(getJPanelViasCatastro(),  new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 5, 0, 5), 0, 150));
        
        
    }
    
    public ViasSistemaPanel(GridBagLayout layout, String nomVia)
    {
        super(layout);
        this.nomVia = nomVia;
        initialize();
        
    }
    
    public ViasSistemaPanel(GridBagLayout layout, String nomVia, String tipoVia)
    {
        super(layout);
        this.nomVia = nomVia;
        this.tipoVia = tipoVia;
        initialize();
        
    }
    
    public ViasSistemaPanel(GridBagLayout layout, String nomVia, String tipoVia, String idMunicipio)
    {
        super(layout);
        this.nomVia = nomVia;
        this.tipoVia = tipoVia;
        this.idMunicipio = idMunicipio;
        initialize();
        
    }
    
    public ViasSistemaPanel(GridBagLayout layout, String nomVia, String tipoVia, int codigoVia)
    {
        super(layout);
        this.nomVia = nomVia;
        this.tipoVia = tipoVia;
        this.codigoVia = codigoVia;
        initialize();
        
    }
    
    public String getIdMunicipio(){
    	return idMunicipio;
    }
    
    public void setIdMunicipio(String idMunicipio){
    	this.idMunicipio = idMunicipio;
    }
    
    public String getNombreVia()
    {
    	return nomVia;
    }
    
    public int getCodigoVia() {
		return codigoVia;
	}



	public void setCodigoVia(int codigo) {
		this.codigoVia= codigo;
	}
    
    
    public void setNomVia(String nomVia) {
		this.nomVia = nomVia;
	}
    
    public Municipio getMunicipio() {
		return municipio;
	}



	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}


    public String getTipoVia() {
		return tipoVia;
	}



	public void setTipoVia(String tipoVia) {
		this.tipoVia = tipoVia;
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
    private JPanel getJPanelViasCatastro()
    {
        if (jPanelViasCatastro == null)
        {
            jPanelViasCatastro = new JPanel(new GridBagLayout());
            jPanelViasCatastro.setBorder(BorderFactory.createTitledBorder(I18N.get("Expedientes","busqueda.vias.titulo")));
            jPanelViasCatastro.add(getJScrollPaneViasCatastro(), 
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 5, 0, 5), 0, 0));
        }
        return jPanelViasCatastro;
    }
    
    /**
     * This method initializes jScrollPaneViasCatastro	
     * 	
     * @return javax.swing.JScrollPane	
     */
    private JScrollPane getJScrollPaneViasCatastro()
    {
        if (jScrollPaneViasCatastro == null)
        {
            jScrollPaneViasCatastro = new JScrollPane();
            jScrollPaneViasCatastro.setViewportView(getJTableViasCatastro());
        }
        return jScrollPaneViasCatastro;
    }
    
    /**
     * This method initializes jTableViasCatastro	
     * 	
     * @return javax.swing.JTable	
     */
    private JTable getJTableViasCatastro()
    {
        if (jTableViasCatastro == null)
        {
            jTableViasCatastro = new JTable();
            
            String[] columnNames = {I18N.get("Expedientes","busqueda.vias.columna.tipo"),
            		I18N.get("Expedientes","busqueda.vias.columna.nombre"), 
                    I18N.get("Expedientes","busqueda.codigovias.columna.codigovia"), 
                    I18N.get("Expedientes","busqueda.vias.columna.municipio")};
            tablemodel = new TableViasCatastroModel(columnNames);
            
            TableSorted tblSortedVias= new TableSorted(tablemodel);
            tblSortedVias.setTableHeader(jTableViasCatastro.getTableHeader());
            jTableViasCatastro.setModel(tblSortedVias);

            jTableViasCatastro.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            jTableViasCatastro.setCellSelectionEnabled(false);
            jTableViasCatastro.setColumnSelectionAllowed(false);
            jTableViasCatastro.setRowSelectionAllowed(true);
            jTableViasCatastro.getTableHeader().setReorderingAllowed(false);
            
            final JFrame desktop = new JFrame();
        	final TaskMonitorDialog progressDialog= new TaskMonitorDialog(desktop, null);
            
            progressDialog.setTitle("TaskMonitorDialog.Wait");
            progressDialog.report(I18N.get("Expedientes","busqueda.vias.dialog.titulo"));
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
					            try
					            {
					            	
					            	if (tipoVia != null && !tipoVia.equals("")){
					            		if (idMunicipio != null && !idMunicipio.equals("")){
					            			lstVias = oper.obtenerViasCatastro(nomVia, tipoVia, idMunicipio);
					            		}
					            		else if(codigoVia != 0){
					            			lstVias = oper.obtenerViasCatastroCodigoVia(nomVia, tipoVia, codigoVia);
					            		}
					            		else{
					            			lstVias = oper.obtenerViasCatastro(nomVia, tipoVia);
					            		}
					            	}
					            	else if(codigoVia != 0){
					            		lstVias = oper.obtenerViasCatastroCodigoVia(nomVia, tipoVia, codigoVia);
					            	}
					            	else if (idMunicipio != null && !idMunicipio.equals("")){
					            		lstVias = oper.obtenerViasCatastroMunic(nomVia, idMunicipio);
					            	}
					            	
					            	else{
					            		lstVias = oper.obtenerViasCatastro(nomVia);
					            	}
					            } catch (DataExceptionCatastro e1)
					            {                
					                e1.printStackTrace();
					            }
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
            
           ((TableViasCatastroModel)((TableSorted)jTableViasCatastro.getModel()).getTableModel()).setData(lstVias);
        }
        
          
        ListSelectionModel rowSM = jTableViasCatastro.getSelectionModel();
        rowSM.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                if (lsm.isSelectionEmpty()) {
                	
                } 
                else 
                {
                	
                	int indexSelected = ((TableSorted)jTableViasCatastro.getModel()).modelIndex(lsm.getMinSelectionIndex());
                	Via via =(Via)getLstVias().get(indexSelected);

                    setNomVia(via.getNombreCatastro());
                    setTipoVia(via.getTipoViaNormalizadoCatastro());

                    try{
                    	if(via.getCodigoCatastro() != null){
                    		if(new Integer(via.getCodigoCatastro()).intValue() == 0){
                    			setCodigoVia(-1);
                    		}
                    		else{
                    			setCodigoVia(new Integer(via.getCodigoCatastro()).intValue());
                    		}
                    	}
                    	else{
                    		setCodigoVia(0);
                    	}
                    	
                    } catch (NumberFormatException ex) {
                    	setCodigoVia(-1);
					}
                    

                    // se busca la provincia del muncipio a la que pertenece la via
                    EdicionOperations oper = new EdicionOperations();
                    try{
                    	municipio = oper.obtenerMunicipioPorId(via.getIdMunicipio());
                    	municipio.setId(via.getIdMunicipio().intValue());
                    
	                } catch (DataExceptionCatastro e1)
		            {                
		                e1.printStackTrace();
		            }
                }
            }
	
        });   

        return jTableViasCatastro;
    }
}
