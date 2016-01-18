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
import com.geopista.app.catastro.intercambio.edicion.CodigoRegistroDialog;
import com.geopista.app.catastro.intercambio.edicion.utils.EdicionOperations;
import com.geopista.app.catastro.intercambio.edicion.utils.TableCodigoRegistroModel;
import com.geopista.app.utilidades.TableSorted;
import com.vividsolutions.jump.I18N;


public class CodigoRegistroPanel extends JPanel
{
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1L;
    private JPanel jPanelCodigoRegistro = null;  
    private JScrollPane jScrollPaneCodigoRegistro = null;
    private JTable jTableCodigoRegistro = null;
    private TableCodigoRegistroModel tablemodel;
    
    private String codigoRegistro;
    private String codigoProvincia;
    
    private ArrayList lstCodigoRegistro;
    
    private boolean okPressed = false;
    
    /**
     * This method initializes
     * 
     */
    public CodigoRegistroPanel()
    {
        super();
        this.codigoRegistro = "";
        this.codigoProvincia = "";
        initialize();
    }
    
    
    
    public ArrayList getLstCodigoRegistro() {
		return lstCodigoRegistro;
	}



	public void setLstCodigoRegistro(ArrayList lstCodigoRegistro) {
		this.lstCodigoRegistro = lstCodigoRegistro;
	}



	private void initialize()
    {     
        Locale loc=I18N.getLocaleAsObject();         
        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.catastro.intercambio.language.Expedientesi18n",
                loc,this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("Expedientes",bundle);
        
        this.setLayout(new GridBagLayout());
        this.setSize(CodigoRegistroDialog.DIM_X,CodigoRegistroDialog.DIM_Y);
        this.add(getJPanelCodigoRegistro(),  new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 5, 0, 5), 0, 150));
        
        
    }
    
    public CodigoRegistroPanel(GridBagLayout layout)
    {
        super(layout);
        this.codigoRegistro = "";
        this.codigoProvincia = "";
        initialize();
        
    }
    
    public CodigoRegistroPanel(GridBagLayout layout, String codigoProvincia)
    {
        super(layout);
        this.codigoRegistro = "";
        this.codigoProvincia = codigoProvincia;
        initialize();
        
    }
    
    public String getCodigoRegistro()
    {
    	return codigoRegistro;
    }
    
    
    public void setCodigoVia(String codigoRegistro) {
		this.codigoRegistro = codigoRegistro;
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
    private JPanel getJPanelCodigoRegistro()
    {
        if (jPanelCodigoRegistro == null)
        {
        	jPanelCodigoRegistro = new JPanel(new GridBagLayout());
        	jPanelCodigoRegistro.setBorder(BorderFactory.createTitledBorder(I18N.get("Expedientes","busqueda.codigoregistro.titulo")));
        	jPanelCodigoRegistro.add(getJScrollPaneCodigoRegistro(), 
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 5, 0, 5), 0, 0));
        }
        return jPanelCodigoRegistro;
    }
    
    /**
     * This method initializes jScrollPaneViasCatastro	
     * 	
     * @return javax.swing.JScrollPane	
     */
    private JScrollPane getJScrollPaneCodigoRegistro()
    {
        if (jScrollPaneCodigoRegistro == null)
        {
        	jScrollPaneCodigoRegistro = new JScrollPane();
        	jScrollPaneCodigoRegistro.setViewportView(getJTableCodigoRegistro());
        }
        return jScrollPaneCodigoRegistro;
    }
    
    /**
     * This method initializes jTableViasCatastro	
     * 	
     * @return javax.swing.JTable	
     */
    private JTable getJTableCodigoRegistro()
    {
        if (jTableCodigoRegistro == null)
        {
        	jTableCodigoRegistro = new JTable();
            
            tablemodel = new TableCodigoRegistroModel();
            
            TableSorted tblSortedVias= new TableSorted(tablemodel);
            tblSortedVias.setTableHeader(jTableCodigoRegistro.getTableHeader());
            jTableCodigoRegistro.setModel(tblSortedVias);
            jTableCodigoRegistro.setLayout(new GridBagLayout());
            jTableCodigoRegistro.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            jTableCodigoRegistro.setCellSelectionEnabled(false);
            jTableCodigoRegistro.setColumnSelectionAllowed(false);
            jTableCodigoRegistro.setRowSelectionAllowed(true);
            jTableCodigoRegistro.getTableHeader().setReorderingAllowed(false);

            EdicionOperations oper = new EdicionOperations();
            lstCodigoRegistro = oper.obtenerCodigoRegistro(this.codigoProvincia);            	
            ((TableCodigoRegistroModel)((TableSorted)jTableCodigoRegistro.getModel()).getTableModel()).setData(lstCodigoRegistro);
                          
        }
        
          
        ListSelectionModel rowSM = jTableCodigoRegistro.getSelectionModel();
        rowSM.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                if (lsm.isSelectionEmpty()) {
                    
                } 
                else 
                {
                    int indexSelected = lsm.getLeadSelectionIndex();
                    String codigoVia = (String) getLstCodigoRegistro().get(indexSelected);
                    setCodigoVia(codigoVia);
                    
                }
            }
	
        });   
        
        
        
        return jTableCodigoRegistro;
    }
    
}
