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
import com.geopista.app.catastro.intercambio.edicion.utils.TableParajesCatastroModel;
import com.geopista.app.catastro.intercambio.edicion.utils.TableViasCatastroModel;
import com.geopista.app.catastro.intercambio.exception.DataException;
import com.geopista.app.catastro.model.beans.Paraje;
import com.geopista.app.utilidades.TableSorted;
import com.geopista.protocol.catastro.Via;
import com.vividsolutions.jump.I18N;


public class ParajesSistemaPanel extends JPanel
{
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1L;
    private JPanel jPanelParajesCatastro = null;  
    private JScrollPane jScrollPaneParajesCatastro = null;
    private JTable jTableParajesCatastro = null;
    private TableParajesCatastroModel tablemodel;
    
    private ArrayList lstParajes;
    
    private String nomParaje;
    
    private boolean okPressed = false;
	private Integer codParaje;
	private Paraje paraje;
    
    /**
     * This method initializes
     * 
     */
    public ParajesSistemaPanel(String nomParaje)
    {
        super();
        this.nomParaje = nomParaje;
        initialize();
    }
    
    
    
    public String getNomParaje() {
		return nomParaje;
	}



	public void setNomParaje(String nomParaje) {
		this.nomParaje = nomParaje;
	}
	
	public void setParaje(Paraje paraje) {
		this.paraje = paraje;
	}

	private void initialize()
    {     
        Locale loc=I18N.getLocaleAsObject();         
        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.catastro.intercambio.language.Expedientesi18n",
                loc,this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("Expedientes",bundle);
        
        this.setLayout(new GridBagLayout());
        this.setSize(ViasSistemaDialog.DIM_X,ViasSistemaDialog.DIM_Y);
        this.add(getJPanelParajesCatastro(),  new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 5, 0, 5), 0, 150));
        
        
    }
    
    public ArrayList getLstParajes() {
		return lstParajes;
	}



	public void setLstParajes(ArrayList lstParajes) {
		this.lstParajes = lstParajes;
	}



	public ParajesSistemaPanel(GridBagLayout layout, String nomParaje)
    {
        super(layout);
        this.nomParaje = nomParaje;
        initialize();
        
    }
    
    /*public String getParaje()
    {
    	return nomParaje;
    }*/
	
	public Paraje getParaje()
    {
    	return paraje;
    }
    
    public Integer getCodParaje()
    {
    	return codParaje;
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
     * This method initializes jPanelParajesCatastro	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanelParajesCatastro()
    {
        if (jPanelParajesCatastro == null)
        {
            jPanelParajesCatastro = new JPanel(new GridBagLayout());
            jPanelParajesCatastro.setBorder(BorderFactory.
                    createTitledBorder(I18N.get("Expedientes","busqueda.parajes.titulo")));
            jPanelParajesCatastro.add(getJScrollPaneParajesCatastro(), 
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 5, 0, 5), 0, 0));
        }
        return jPanelParajesCatastro;
    }
    
    /**
     * This method initializes jScrollPaneParajesCatastro	
     * 	
     * @return javax.swing.JScrollPane	
     */
    private JScrollPane getJScrollPaneParajesCatastro()
    {
        if (jScrollPaneParajesCatastro == null)
        {
            jScrollPaneParajesCatastro = new JScrollPane();
            jScrollPaneParajesCatastro.setViewportView(getJTableParajesCatastro());
        }
        return jScrollPaneParajesCatastro;
    }
    
    /**
     * This method initializes jTableViasCatastro	
     * 	
     * @return javax.swing.JTable	
     */
    private JTable getJTableParajesCatastro()
    {
        if (jTableParajesCatastro == null)
        {
            jTableParajesCatastro = new JTable();
            
            String[] columnNames = {
                    I18N.get("Expedientes","busqueda.parajes.columna.nombre"),
                    I18N.get("Expedientes","busqueda.parajes.columna.codigo")};
            tablemodel = new TableParajesCatastroModel(columnNames);
            
            TableSorted tblSortedParajes= new TableSorted(tablemodel);
            tblSortedParajes.setTableHeader(jTableParajesCatastro.getTableHeader());
            jTableParajesCatastro.setModel(tblSortedParajes);
            jTableParajesCatastro.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            jTableParajesCatastro.setCellSelectionEnabled(false);
            jTableParajesCatastro.setColumnSelectionAllowed(false);
            jTableParajesCatastro.setRowSelectionAllowed(true);
            jTableParajesCatastro.getTableHeader().setReorderingAllowed(false);

            EdicionOperations oper = new EdicionOperations();
            try
            {
                lstParajes = oper.obtenerParajesCatastro(nomParaje);
                ((TableParajesCatastroModel)((TableSorted)jTableParajesCatastro.getModel()).getTableModel()).setData(lstParajes);
                
            } catch (DataException e1)
            {                
                e1.printStackTrace();
            }
        }
        
         
        ListSelectionModel rowSM = jTableParajesCatastro.getSelectionModel();
        rowSM.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                if (lsm.isSelectionEmpty()) {
                    //System.out.println("No rows are selected.");
                    
                } 
                else 
                {
                	int indexSelected = lsm.getLeadSelectionIndex();
                    //setNomParaje((String) getLstParajes().get(indexSelected));
                	//setNomParaje(((Paraje) getLstParajes().get(indexSelected)).getNombre());
                	setParaje((Paraje)getLstParajes().get(indexSelected));
                }
            }
        });   
        
        
        return jTableParajesCatastro;
    }
    
}
