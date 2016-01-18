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

import com.geopista.app.catastro.intercambio.edicion.ViasSistemaDialog;
import com.geopista.app.catastro.intercambio.edicion.utils.EdicionOperations;
import com.geopista.app.catastro.intercambio.edicion.utils.TableViasCatastroModel;
import com.geopista.app.catastro.intercambio.exception.DataException;
import com.geopista.protocol.catastro.Via;
import com.vividsolutions.jump.I18N;


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
                    I18N.get("Expedientes","busqueda.vias.columna.nombre"), I18N.get("Expedientes","busqueda.vias.columna.municipio")};
            tablemodel = new TableViasCatastroModel(columnNames);
            
           /* TableSorted tblSortedVias= new TableSorted(tablemodel);
            tblSortedVias.setTableHeader(jTableViasCatastro.getTableHeader());*/
            jTableViasCatastro.setModel(tablemodel);
            jTableViasCatastro.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            jTableViasCatastro.setCellSelectionEnabled(false);
            jTableViasCatastro.setColumnSelectionAllowed(false);
            jTableViasCatastro.setRowSelectionAllowed(true);
            jTableViasCatastro.getTableHeader().setReorderingAllowed(false);
            EdicionOperations oper = new EdicionOperations();
            try
            {
            	
            	if (tipoVia != null && !tipoVia.equals("")){
            		if (idMunicipio != null && !idMunicipio.equals("")){
            			lstVias = oper.obtenerViasCatastro(nomVia, tipoVia, idMunicipio);
            		}
            		else{
            			lstVias = oper.obtenerViasCatastro(nomVia, tipoVia);
            		}
            	}
            	else if (idMunicipio != null && !idMunicipio.equals("")){
            		lstVias = oper.obtenerViasCatastroMunic(nomVia, idMunicipio);
            	}
            	else{
            		lstVias = oper.obtenerViasCatastro(nomVia);
            	}
            	
//            	if((tipoVia == null || tipoVia.equals("")) && (idMunicipio == null || idMunicipio.equals(""))){
//            		
//            		//lstVias = oper.obtenerViasINE(nomVia);
//            		lstVias = oper.obtenerViasCatastro(nomVia);
//            	}            	
//            	else if (idMunicipio == null || idMunicipio.equals("")){
//            		//lstVias = oper.obtenerViasINE(nomVia, tipoVia);
//            		lstVias = oper.obtenerViasCatastro(nomVia, tipoVia);
//            	}
//            	else{
//            		lstVias = oper.obtenerViasCatastro(nomVia, tipoVia, idMunicipio);
//            	}
                //((TableViasCatastroModel)((TableSorted)jTableViasCatastro.getModel()).getTableModel()).setData(lstVias);
            	((TableViasCatastroModel)jTableViasCatastro.getModel()).setData(lstVias);
                
            } catch (DataException e1)
            {                
                e1.printStackTrace();
            }
        }
        
          
        ListSelectionModel rowSM = jTableViasCatastro.getSelectionModel();
        rowSM.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                if (lsm.isSelectionEmpty()) {
                    //System.out.println("No rows are selected.");
                    
                } 
                else 
                {
                    int indexSelected = lsm.getLeadSelectionIndex();
                    Via via = (Via) getLstVias().get(indexSelected);
                    setNomVia(via.getNombreCatastro());
                    setTipoVia(via.getTipoViaNormalizadoCatastro());
                    try{
                    	setCodigoVia(Integer.parseInt(via.getCodigoCatastro()));
                    } catch (NumberFormatException ex) {
						// TODO: handle exception
                    	setCodigoVia(0);
					}
                }
            }
	
        });   
        
        
        
        return jTableViasCatastro;
    }
    
}
