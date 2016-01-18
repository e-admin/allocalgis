package com.geopista.app.catastro.intercambio.edicion.dialogs;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.intercambio.edicion.utils.TableRepartoModel;
import com.geopista.app.catastro.intercambio.edicion.utils.TableRowReparto;
import com.geopista.app.catastro.model.beans.BienInmuebleJuridico;
import com.geopista.app.catastro.model.beans.ConstruccionCatastro;
import com.geopista.app.catastro.model.beans.Cultivo;
import com.geopista.app.catastro.model.beans.ElementoReparto;
import com.geopista.app.catastro.model.beans.FincaCatastro;
import com.geopista.app.catastro.model.beans.ReferenciaCatastral;
import com.geopista.app.catastro.model.beans.RepartoCatastro;
import com.geopista.app.catastro.registroExpedientes.utils.UtilRegistroExp;
import com.geopista.app.utilidades.TableSorted;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.geopista.util.GeopistaFunctionUtils;
import com.vividsolutions.jump.I18N;

public class RepartoPanel extends JPanel
{
    
    private JPanel jPanelDatosReparto = null;
    private JPanel jPanelReparto = null;
    private JPanel jPanelModalidadReparto;
    private ComboBoxEstructuras jComboBoxPorcentajeReparto;
    private JLabel jLabelLocalesCultivosComunes = null;
    private JTextField jTextFieldNumOrden = null;
    private JLabel jLabelPorcentajeReparto = null;
    private ComboBoxEstructuras jComboBoxFormaReparto = null;
    private JScrollPane jScrollPaneReparto = null;
    private JTable jTableReparto = null;
    private TableRepartoModel jTableRepartoModelTemp = null;
    private JTextField jTextFieldPCatastral2;
    private JTextField jTextFieldPCatastral1;
    private JLabel jLabelPCatastral2;
    private JLabel jLabelPCatastral1;
    private FincaCatastro finca;
    AppContext app =(AppContext) AppContext.getApplicationContext();
    private Object comun = null;
    private TableRepartoModel tablerepartomodel = new TableRepartoModel();
    
    private boolean okPressed = false;
    
    public RepartoPanel()
    {
        super();
        initialize();
    }
    public RepartoPanel(Object comun)
    {
        super();
        initialize();
        this.comun = comun;
        load (comun);
    }
    
    public void load(Object comun)
    {
        this.comun = comun;
        this.finca = (FincaCatastro)app.getBlackboard().get("finca");
        
        RepartoCatastro reparto = obtenerReparto(comun);
        
        /*if (reparto!=null){
        	
        	jTextFieldPCatastral1.setText(reparto.getIdOrigen().getRefCatastral1());
        	jTextFieldPCatastral2.setText(reparto.getIdOrigen().getRefCatastral2());
        	jTextFieldNumOrden.setText(reparto.getIdReparto());

        	jComboBoxFormaReparto.setSelectedPatron(reparto.getTipoReparto());
        	getTablaReparto(reparto);
        	jComboBoxPorcentajeReparto.setSelectedPatron(new Integer(Math.round(reparto.getPorcentajeReparto())).toString());
        }
        else{
        	jTextFieldPCatastral1.setText(finca.getRefFinca().getRefCatastral1());
            jTextFieldPCatastral2.setText(finca.getRefFinca().getRefCatastral2());            
        }*/
        
        if (comun instanceof ConstruccionCatastro)
        {   
            jTextFieldPCatastral1.setText(((ConstruccionCatastro)comun).getRefParcela().getRefCatastral1()!=null?
            		((ConstruccionCatastro)comun).getRefParcela().getRefCatastral1():"");
            jTextFieldPCatastral2.setText(((ConstruccionCatastro)comun).getRefParcela().getRefCatastral2()!=null?
            		((ConstruccionCatastro)comun).getRefParcela().getRefCatastral2():"");
            jTextFieldNumOrden.setText(((ConstruccionCatastro)comun).getNumOrdenConstruccion()!=null?
            		((ConstruccionCatastro)comun).getNumOrdenConstruccion():"");
            //jTextFieldNumOrden.setText(((RepartoCatastro)((ConstruccionCatastro)comun).getLstRepartos().iterator().next()).getIdReparto());
                        
            if(((ConstruccionCatastro)comun).getDatosEconomicos().getCodModalidadReparto()!=null){
            	if(((ConstruccionCatastro)comun).getDatosEconomicos().getCodModalidadReparto().length()==3){
            		jComboBoxFormaReparto.setSelectedPatron(((ConstruccionCatastro)comun).getDatosEconomicos().
            				getCodModalidadReparto().substring(0, 2));
            		jComboBoxPorcentajeReparto.setSelectedPatron(((ConstruccionCatastro)comun).getDatosEconomicos().
            				getCodModalidadReparto().substring(2,3));
            	}
            }       
        }
        else if (comun instanceof Cultivo)
        {
            jTextFieldPCatastral1.setText(((Cultivo)comun).getIdCultivo().getParcelaCatastral().getRefCatastral1()!=null?
            		((Cultivo)comun).getIdCultivo().getParcelaCatastral().getRefCatastral1():"");
            jTextFieldPCatastral2.setText(((Cultivo)comun).getIdCultivo().getParcelaCatastral().getRefCatastral2()!=null?
            		((Cultivo)comun).getIdCultivo().getParcelaCatastral().getRefCatastral2():"");
            
            String numOrden = "";
            if(((Cultivo)comun).getCodSubparcela()!=null)
            	numOrden = numOrden + (((Cultivo)comun).getCodSubparcela());
            if(((Cultivo)comun).getIdCultivo().getCalifCultivo()!=null)
            	numOrden = numOrden + (((Cultivo)comun).getIdCultivo().getCalifCultivo());

            jTextFieldNumOrden.setText(numOrden);
            
            if(((Cultivo)comun).getCodModalidadReparto()!=null){
            	if(((Cultivo)comun).getCodModalidadReparto().length()==3){
            		jComboBoxFormaReparto.setSelectedPatron(((Cultivo)comun).getCodModalidadReparto().substring(0, 2));
            		jComboBoxPorcentajeReparto.setSelectedPatron((((Cultivo)comun).getCodModalidadReparto().substring(2,3)));
            	}
            }
        }     
        
        if (reparto!=null){        	
        	getTablaReparto(reparto);        	
        }
        

    }
        
    public Object getComun ()
    {
        return comun;
    }
    
    
    private void initialize()
    {    
        Locale loc=I18N.getLocaleAsObject();         
        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.catastro.intercambio.language.Expedientesi18n",loc,this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("Expedientes",bundle);
        
        this.setLayout(new GridBagLayout());
        this.add(getJPanelDatosReparto(), 
                new GridBagConstraints(0, 1, 1, 1, 1, 0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(5, 5, 0, 5), 0, 0));
        this.add(getJPanelModalidadReparto(), 
                new GridBagConstraints(0, 2, 3, 1, 1, 0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(5, 5, 0, 5), 0, 0));
        this.add(getJPanelReparto(), 
                new GridBagConstraints(0, 3, 3, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 5, 0, 5), 0, 0));
       
    }
    
    /**
     * This method initializes jPanelDatosReparto    
     *  
     * @return javax.swing.JPanel   
     */
    private JPanel getJPanelDatosReparto()
    {
        if (jPanelDatosReparto == null)
        {  
            
            jLabelLocalesCultivosComunes = new JLabel("", JLabel.CENTER); 
            jLabelLocalesCultivosComunes.setText(I18N.get("Expedientes", "reparto.panel.datosreparto.numorden")); 
            
            jLabelPCatastral1 = new JLabel("", JLabel.CENTER); 
            jLabelPCatastral1.setText(I18N.get("Expedientes", "reparto.panel.datosreparto.catastral1")); 
            jLabelPCatastral2 = new JLabel("", JLabel.CENTER); 
            jLabelPCatastral2.setText(I18N.get("Expedientes", "reparto.panel.datosreparto.catastral2")); 
            
            jPanelDatosReparto = new JPanel(new GridBagLayout());
            jPanelDatosReparto.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("Expedientes", "reparto.panel.datosreparto.titulo"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
            
            jPanelDatosReparto.add(jLabelPCatastral1, 
                    new GridBagConstraints(0, 0, 1, 1, 0, 1, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,25,0,5), 0,0));
            jPanelDatosReparto.add(jLabelPCatastral2, 
                    new GridBagConstraints(1, 0, 1, 1, 0, 1, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosReparto.add(getJTextFieldPCatastral1(),
                    new GridBagConstraints(0, 1, 1, 1, 0, 1, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,25,0,5), 0,0));
            jPanelDatosReparto.add(getJTextFieldPCatastral2(),
                    new GridBagConstraints(1, 1, 1, 1, 0, 1, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            
            jPanelDatosReparto.add(jLabelLocalesCultivosComunes, 
                    new GridBagConstraints(2, 0, 1, 1, 0, 1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosReparto.add(getJTextFieldNumOrden(), 
                    new GridBagConstraints(2, 1, 1, 1, 0, 1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
            
        }
        return jPanelDatosReparto;
    }
    
    
    /**
     * This method initializes jTextFieldPCatastral1   
     *  
     * @return javax.swing.JTextField   
     */
    private JTextField getJTextFieldPCatastral1()
    {
        if (jTextFieldPCatastral1 == null)
        {
            jTextFieldPCatastral1 = new JTextField(7);
            jTextFieldPCatastral1.setEditable(false);
        }
        return jTextFieldPCatastral1;
    }
    
    /**
     * This method initializes jTextFieldPCatastral2   
     *  
     * @return javax.swing.JTextField   
     */
    private JTextField getJTextFieldPCatastral2()
    {
        if (jTextFieldPCatastral2 == null)
        {
            jTextFieldPCatastral2 = new JTextField(7);
            jTextFieldPCatastral2.setEditable(false);
            
        }
        return jTextFieldPCatastral2;
    }
    
    
    /**
     * This method initializes jPanelCCSuelo    
     *  
     * @return javax.swing.JPanel   
     */
    private JPanel getJPanelReparto()
    {
        if (jPanelReparto == null)
        {
            GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
            gridBagConstraints8.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints8.weighty = 1.0;
            gridBagConstraints8.gridy = 0;
            gridBagConstraints8.weightx = 1.0;
            gridBagConstraints8.gridx = 0;
            jPanelReparto = new JPanel(new GridBagLayout());
            jPanelReparto.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("Expedientes", "reparto.panel.datosreparto.entre"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
            
            jPanelReparto.add(getJScrollPaneReparto(), gridBagConstraints8);
            
            
        }
        return jPanelReparto;
    }
    
    
    /**
     * This method initializes jPanelModalidadReparto  
     *  
     * @return javax.swing.JPanel   
     */
    private JPanel getJPanelModalidadReparto()
    {
        if (jPanelModalidadReparto == null)
        {   
            
            jLabelPorcentajeReparto = new JLabel("", JLabel.CENTER); 
            jLabelPorcentajeReparto.setText(I18N.get("Expedientes", "reparto.panel.datosreparto.porcentaje")); 
            jPanelModalidadReparto = new JPanel(new GridBagLayout());
            jPanelModalidadReparto.setPreferredSize(new Dimension(200,75));
            jPanelModalidadReparto.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("Expedientes", "reparto.panel.datosreparto.modalidad"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
            
            JLabel jLabelFormaReparto = new JLabel("", JLabel.CENTER);             
            jLabelFormaReparto.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "reparto.panel.datosreparto.forma"))); 
            jPanelModalidadReparto.add(jLabelFormaReparto,
                    new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelModalidadReparto.add(getJComboBoxPorcentajeReparto(), 
                    new GridBagConstraints(1, 1, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5), 0, 0));
            
            
            jPanelModalidadReparto.add(jLabelPorcentajeReparto, 
                    new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
            jPanelModalidadReparto.add(getJComboBoxFormaReparto(), 
                    new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
            
            
        }
        return jPanelModalidadReparto;
    }
    
    /**
     * This method initializes jComboBoxPorcentajeReparto   
     *  
     * @return ComboBoxEstructuras   
     */
    private ComboBoxEstructuras getJComboBoxPorcentajeReparto()
    { 
        if (jComboBoxPorcentajeReparto == null)
        {
            Estructuras.cargarEstructura("Proporción de reparto");
            jComboBoxPorcentajeReparto = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, app.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), true);
        }
        return jComboBoxPorcentajeReparto;        
    }
    /**
     * This method initializes jTextFieldNumOrden	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getJTextFieldNumOrden()
    {
        if (jTextFieldNumOrden == null)
        {
            jTextFieldNumOrden = new JTextField(7);
            jTextFieldNumOrden.setEditable(false);
        }
        return jTextFieldNumOrden;
    }
    
    /**
     * This method initializes jComboBoxFormaReparto	
     * 	
     * @return ComboBoxEstructuras	
     */
    private JComboBox getJComboBoxFormaReparto()
    {
    	if (jComboBoxFormaReparto == null)
    	{   
    		Estructuras.cargarEstructura("Forma de reparto");
    		jComboBoxFormaReparto = new ComboBoxEstructuras(Estructuras.getListaTipos(),
    				null, app.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), true);
    		
    		jComboBoxFormaReparto.addActionListener(new ActionListener()
    		{
    			public void actionPerformed(ActionEvent e)
    			{                    
    				//Llamar al metodo correspondiente
    				formaReparto_actionPerformed(e);
    			}
    		});
    	}
    	return jComboBoxFormaReparto;
    }
    
    protected void formaReparto_actionPerformed(ActionEvent e) {
		
    	ArrayList lst = new ArrayList();
    	    	
    	((TableRepartoModel)((TableSorted)jTableReparto.getModel()).getTableModel()).removeElements();
    	
    	if (jComboBoxFormaReparto.getSelectedIndex()>0){
    		String patron = jComboBoxFormaReparto.getSelectedPatron();
    		if(patron!=null){
    			TableRowReparto data = null;
    			if(patron.equals("TC")){
    				
    				for(Iterator bienes = finca.getLstBienesInmuebles().iterator();bienes.hasNext();){
    					BienInmuebleJuridico bi = (BienInmuebleJuridico)bienes.next();
    					data = new TableRowReparto(bi.getBienInmueble().getIdBienInmueble().getParcelaCatastral().getRefCatastral1(),
    							bi.getBienInmueble().getIdBienInmueble().getParcelaCatastral().getRefCatastral2(),
                                bi.getBienInmueble().getIdBienInmueble().getNumCargo(),
    							new Boolean(true));    					
    					
    					lst.add(data);
    				}
    				
    			}
    			else if(patron.equals("TL")){
    				if(comun instanceof ConstruccionCatastro){
    					for(Iterator bienes = finca.getLstConstrucciones().iterator();bienes.hasNext();){
    						ConstruccionCatastro cc = (ConstruccionCatastro)bienes.next();
    						if (!(cc.getDatosEconomicos().getCodTipoValor()!=null
    								&& !cc.getDatosEconomicos().getCodTipoValor().equals("")
    								&& cc.getDatosEconomicos().getCodModalidadReparto()!=null)){
    							
    							data = new TableRowReparto(cc.getRefParcela().getRefCatastral1(),
    									cc.getRefParcela().getRefCatastral2(),cc.getNumOrdenConstruccion(),
    									new Boolean(true));
    							lst.add(data);
    						}
    					}
    				}    				
    			}
    			else if(patron.equals("AC")){
    				for(Iterator bienes = finca.getLstBienesInmuebles().iterator();bienes.hasNext();){
    					BienInmuebleJuridico bi = (BienInmuebleJuridico)bienes.next();
    					
    					data = new TableRowReparto(bi.getBienInmueble().getIdBienInmueble().getParcelaCatastral().getRefCatastral1(),
    							bi.getBienInmueble().getIdBienInmueble().getParcelaCatastral().getRefCatastral2(),
                                bi.getBienInmueble().getIdBienInmueble().getNumCargo(),
    							new Boolean(false));
    					lst.add(data);
    				}    				
    				
    			}
    			else if(patron.equals("AL")){
    				if(comun instanceof ConstruccionCatastro){
    					for(Iterator bienes = finca.getLstConstrucciones().iterator();bienes.hasNext();){
    						ConstruccionCatastro cc = (ConstruccionCatastro)bienes.next();
    						if (!(cc.getDatosEconomicos().getCodTipoValor()!=null
    								&& !cc.getDatosEconomicos().getCodTipoValor().equals("")
    								&& cc.getDatosEconomicos().getCodModalidadReparto()!=null)){
    							
    							data = new TableRowReparto(cc.getRefParcela().getRefCatastral1(),
    									cc.getRefParcela().getRefCatastral2(),cc.getNumOrdenConstruccion(),
    									new Boolean(false));
    							lst.add(data);
    						}
    					}
    				}
    			}
    		}
    	}
    	
		((TableRepartoModel)((TableSorted)jTableReparto.getModel()).getTableModel()).setData(lst);
		
    	jPanelReparto.remove(jScrollPaneReparto);
		 
    	GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
        gridBagConstraints8.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints8.weighty = 1.0;
        gridBagConstraints8.gridy = 0;
        gridBagConstraints8.weightx = 1.0;
        gridBagConstraints8.gridx = 0;
    	jPanelReparto.add(getJScrollPaneReparto(),gridBagConstraints8);
    	jPanelReparto.updateUI();
	}
	/**
     * This method initializes jScrollPaneReparto	
     * 	
     * @return javax.swing.JScrollPane	
     */
    private JScrollPane getJScrollPaneReparto()
    {
        if (jScrollPaneReparto == null)
        {
            jScrollPaneReparto = new JScrollPane();
            jScrollPaneReparto.setViewportView(getJTableReparto());
            
            
        }
        return jScrollPaneReparto;
    }
    
    /**
     * This method initializes jTableReparto	
     * 	
     * @return javax.swing.JTable	
     */
    private JTable getJTableReparto()
    {
        if (jTableReparto == null)
        {
            jTableReparto = new JTable();
            tablerepartomodel = new TableRepartoModel();
            
            TableSorted tblSorted= new TableSorted(tablerepartomodel);
            tblSorted.setTableHeader(jTableReparto.getTableHeader());
            jTableReparto.setModel(tblSorted);
            jTableReparto.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            jTableReparto.setCellSelectionEnabled(true);
            jTableReparto.setColumnSelectionAllowed(true);
            jTableReparto.setRowSelectionAllowed(true);
            jTableReparto.getTableHeader().setReorderingAllowed(false);
                        
        }
        return jTableReparto;
    }
    public String validateInput()
    {
        return null; 
    }
    public void okPressed()
    {
       okPressed = true;
        
    }
    
    public RepartoCatastro getReparto()
    {
    	RepartoCatastro reparto = null;
    	
    	if (okPressed)
    	{
    		reparto = new RepartoCatastro();
    		
    		reparto.setIdOrigen(new ReferenciaCatastral(jTextFieldPCatastral1.getText() + jTextFieldPCatastral2.getText()));
    		//reparto.setIdReparto(jTextFieldNumOrden.getText());
    		reparto.setTipoReparto(jComboBoxFormaReparto.getSelectedPatron());
    		
    		ArrayList lstElemRepartidos = new ArrayList();
    		ArrayList lstElementos = ((TableRepartoModel)((TableSorted)jTableReparto.getModel()).getTableModel()).getData();
    		if (lstElementos != null)
    		{
    			for (int i=0;i<lstElementos.size();i++)
    			{
    				TableRowReparto elem = (TableRowReparto) lstElementos.get(i);
    				if (elem.getEditable().booleanValue())
    					lstElemRepartidos.add(elem.getIdentificador());
    			}
    		}
    		
    		if(reparto.getTipoReparto()!=null){
    			if (reparto.getTipoReparto().equalsIgnoreCase("AC") || reparto.getTipoReparto().equalsIgnoreCase("TC"))
    				reparto.setLstBienes(lstElemRepartidos);
    			else if (reparto.getTipoReparto().equalsIgnoreCase("AL") || reparto.getTipoReparto().equalsIgnoreCase("TL"))
    				reparto.setLstLocales(lstElemRepartidos);
    		}
    		
            reparto.setPorcentajeReparto(Float.parseFloat(jComboBoxPorcentajeReparto.getSelectedPatron()));
           
    	}
    	
    	return reparto;
    }

    
    public RepartoCatastro getReparto(Object obj)
    {
    	RepartoCatastro reparto = null;
    	
    	if (okPressed)
    	{
    		reparto = new RepartoCatastro();
    		
    		reparto.setIdOrigen(new ReferenciaCatastral(jTextFieldPCatastral1.getText() + jTextFieldPCatastral2.getText()));
    		//reparto.setIdReparto(jTextFieldNumOrden.getText());
    		reparto.setTipoReparto(jComboBoxFormaReparto.getSelectedPatron());
    		if(jComboBoxPorcentajeReparto.getSelectedPatron()!=null)
    			reparto.setPorcentajeReparto(Float.parseFloat(jComboBoxPorcentajeReparto.getSelectedPatron()));

            ArrayList lstElemRepartidos = new ArrayList();
    		ArrayList lstElementos = ((TableRepartoModel)((TableSorted)jTableReparto.getModel()).getTableModel()).getData();
    		if (lstElementos != null)
    		{
    			for (int i=0;i<lstElementos.size();i++)
    			{
    				TableRowReparto elem = (TableRowReparto) lstElementos.get(i);
    				if (elem.getEditable().booleanValue())
                    {
                        ElementoReparto elemRep = new ElementoReparto();
                        elemRep.setId(elem.getIdentificador());
                        elemRep.setNumCargo(elem.getCargo());
                        if(reparto.getPorcentajeReparto()>0)
                        {
                            elemRep.setPorcentajeReparto(reparto.getPorcentajeReparto());
                        }
                        if(reparto.getTipoReparto()!=null)
                        {
                            elemRep.setEsConstruccion((reparto.getTipoReparto().equalsIgnoreCase("AL") || reparto.getTipoReparto().equalsIgnoreCase("TL")
                            &&!(reparto.getTipoReparto().equalsIgnoreCase("AC") || reparto.getTipoReparto().equalsIgnoreCase("TC"))));
                        }
                        lstElemRepartidos.add(elemRep);
                    }
                }
    		}
    		
    		if(reparto.getTipoReparto()!=null){
    			if (reparto.getTipoReparto().equalsIgnoreCase("AC") || reparto.getTipoReparto().equalsIgnoreCase("TC"))
    				reparto.setLstBienes(lstElemRepartidos);
    			else if (reparto.getTipoReparto().equalsIgnoreCase("AL") || reparto.getTipoReparto().equalsIgnoreCase("TL"))
    				reparto.setLstLocales(lstElemRepartidos);
    		}

            if(obj!=null){
            	
            	if (obj instanceof ConstruccionCatastro){
            		
            		ConstruccionCatastro construccion = (ConstruccionCatastro)obj;
            		reparto.setNumOrdenConsRepartir(construccion.getNumOrdenConstruccion()!=null?
            				construccion.getNumOrdenConstruccion():"");
            		reparto.setCodDelegacion(new Integer(construccion.getCodDelegacionMEH()).toString()!=null?
            				new Integer(construccion.getCodDelegacionMEH()).toString():"");
            		reparto.setCodMunicipio(new Integer(construccion.getCodMunicipio()).toString()!=null?
            				new Integer(construccion.getCodMunicipio()).toString():"");
            		reparto.setTIPO_MOVIMIENTO(construccion.getTIPO_MOVIMIENTO()!=null?
            				construccion.getTIPO_MOVIMIENTO():"");

            	}
            	else if (obj instanceof Cultivo){

            		Cultivo cultivo = (Cultivo)obj;
            		reparto.setCodSubparcelaElementoRepartir(cultivo.getCodSubparcela()!=null?
            				cultivo.getCodSubparcela():"");
            		if(cultivo.getIdCultivo()!=null && cultivo.getIdCultivo().getCalifCultivo()!=null)
            			reparto.setCalifCatastralElementoRepartir(cultivo.getIdCultivo().getCalifCultivo());
            		else
            			reparto.setCalifCatastralElementoRepartir("");
            		if(cultivo.getCodDelegacionMEH()!=null)
            			reparto.setCodDelegacion(cultivo.getCodDelegacionMEH().toString()!=null?
            					cultivo.getCodDelegacionMEH().toString():"");
            		if(cultivo.getCodMunicipioDGC()!=null)
            			reparto.setCodMunicipio(cultivo.getCodMunicipioDGC().toString()!=null?
            					cultivo.getCodMunicipioDGC().toString():"");
            		reparto.setTIPO_MOVIMIENTO(cultivo.getTIPO_MOVIMIENTO()!=null?
            				cultivo.getTIPO_MOVIMIENTO():"");
            	}
            }
    	}
    	
    	return reparto;
    }

    private RepartoCatastro obtenerReparto(Object obj){
    	
    	if(finca!=null && finca.getLstReparto()!=null && finca.getLstReparto().size()>0){
    		
    		if (obj instanceof ConstruccionCatastro){
    			
    			ConstruccionCatastro construccion = (ConstruccionCatastro)obj;
    			
    			for (Iterator repartos = finca.getLstReparto().iterator();repartos.hasNext();){
    				
    				RepartoCatastro reparto = (RepartoCatastro)repartos.next();
    				
    				if(construccion.getRefParcela()!=null && reparto.getIdOrigen()!=null && construccion.getNumOrdenConstruccion()!=null){
    					if (construccion.getRefParcela().getRefCatastral().equals(reparto.getIdOrigen().getRefCatastral()) 
    							&& construccion.getNumOrdenConstruccion().equals(reparto.getNumOrdenConsRepartir())){
    						return reparto;
    					}
    				}
    			}
    		}
    		else if (obj instanceof Cultivo){
    			
    			Cultivo cultivo = (Cultivo)obj;
    			
    			for (Iterator repartos = finca.getLstReparto().iterator();repartos.hasNext();){
    				
    				RepartoCatastro reparto = (RepartoCatastro)repartos.next();
    				
    				if(cultivo.getIdCultivo()!=null && cultivo.getIdCultivo().getParcelaCatastral()!=null && reparto.getIdOrigen()!=null && cultivo.getCodSubparcela()!=null){
    					if (cultivo.getIdCultivo().getParcelaCatastral().getRefCatastral().equals(reparto.getIdOrigen().getRefCatastral()) 
    							&& cultivo.getCodSubparcela().equals(reparto.getCodSubparcelaElementoRepartir())){
    						return reparto;
    					}
    				}
    			}
    		}
    	}
    	return null;
    }
    
    protected void getTablaReparto(RepartoCatastro reparto) {
		
    	ArrayList lst = new ArrayList();
    	    	
    	((TableRepartoModel)((TableSorted)jTableReparto.getModel()).getTableModel()).removeElements();
    	
    	
    	/*String patron = reparto.getTipoReparto();
    		
    		if(patron!=null){
    			
    			TableRowReparto data = null;
    			
    			if((patron.equals("TC"))||(patron.equals("AC"))){
    				
    				for(Iterator bienes = finca.getLstBienesInmuebles().iterator();bienes.hasNext();){
    					
    					BienInmuebleJuridico bi = (BienInmuebleJuridico)bienes.next();
    					
    					boolean activa = false;
    					for (Iterator iteratorBienes = reparto.getLstBienes().iterator();iteratorBienes.hasNext();){
    						
    						if ((bi.getBienInmueble().getIdBienInmueble().getParcelaCatastral().getRefCatastral() + bi.getBienInmueble().getIdBienInmueble().getNumCargo()).equals(iteratorBienes.next().toString())){
    							activa = true;
    						}
    					}
    					data = new TableRowReparto(bi.getBienInmueble().getIdBienInmueble().getParcelaCatastral().getRefCatastral1(),
    							bi.getBienInmueble().getIdBienInmueble().getParcelaCatastral().getRefCatastral2(),
                                bi.getBienInmueble().getIdBienInmueble().getNumCargo(),
    							new Boolean(activa));    					
    					
    					lst.add(data);
    				}
    				
    			}
    			else if((patron.equals("TL"))||(patron.equals("AL"))){
    				    				
    					for(Iterator bienes = finca.getLstConstrucciones().iterator();bienes.hasNext();){
    						
    						ConstruccionCatastro cc = (ConstruccionCatastro)bienes.next();
    						
    						if (!(cc.getDatosEconomicos().getCodTipoValor()!=null
    								&& !cc.getDatosEconomicos().getCodTipoValor().equals("")
    								&& cc.getDatosEconomicos().getCodModalidadReparto()!=null)){
    							
    							boolean activa = false;
    	    					for (Iterator iteratorConstrucciones = reparto.getLstLocales().iterator();iteratorConstrucciones.hasNext();){
    	    						
    	    						if ((cc.getRefParcela().getRefCatastral() + cc.getNumOrdenConstruccion()).equals(iteratorConstrucciones.next().toString())){
    	    							activa = true;
    	    						}
    	    					}
    	    					
    							data = new TableRowReparto(cc.getRefParcela().getRefCatastral1(),
    									cc.getRefParcela().getRefCatastral2(),cc.getNumOrdenConstruccion(),
    									new Boolean(activa));
    							lst.add(data);
    						}
    					}
    				 				
    			}
    			
    			
    		}*/
    		
    		if((reparto.getLstBienes()!=null)&&(reparto.getLstBienes().size()>0)){

    			TableRowReparto data = null;

    			for(Iterator bienes = finca.getLstBienesInmuebles().iterator();bienes.hasNext();){

    				BienInmuebleJuridico bi = (BienInmuebleJuridico)bienes.next();

    				boolean activa = false;
    				for (Iterator iteratorBienes = reparto.getLstBienes().iterator();iteratorBienes.hasNext();){

    					if ((bi.getBienInmueble().getIdBienInmueble().getParcelaCatastral().getRefCatastral() + bi.getBienInmueble().getIdBienInmueble().getNumCargo()).equals(((ElementoReparto)iteratorBienes.next()).getId())){
    						activa = true;
    					}
    				}
    				data = new TableRowReparto(bi.getBienInmueble().getIdBienInmueble().getParcelaCatastral().getRefCatastral1(),
    						bi.getBienInmueble().getIdBienInmueble().getParcelaCatastral().getRefCatastral2(),
    						bi.getBienInmueble().getIdBienInmueble().getNumCargo(),
    						new Boolean(activa));    					

    				lst.add(data);
    			}
    		}
    		else if ((reparto.getLstLocales()!=null)&&(reparto.getLstLocales().size()>0)){

    			TableRowReparto data = null;    			

    			for(Iterator bienes = finca.getLstConstrucciones().iterator();bienes.hasNext();){

    				ConstruccionCatastro cc = (ConstruccionCatastro)bienes.next();

    				if (!(cc.getDatosEconomicos().getCodTipoValor()!=null
    						&& !cc.getDatosEconomicos().getCodTipoValor().equals("")
    						&& cc.getDatosEconomicos().getCodModalidadReparto()!=null)){

    					boolean activa = false;
    					for (Iterator iteratorConstrucciones = reparto.getLstLocales().iterator();iteratorConstrucciones.hasNext();){
    						
    						if (cc.getRefParcela().getRefCatastral()!=null && cc.getNumOrdenConstruccion()!=null){
    							if (cc.getRefParcela().getRefCatastral().length()<14){
    								cc.getRefParcela().setRefCatastral(GeopistaFunctionUtils.completarConCeros(cc.getRefParcela().getRefCatastral(),14));
    							}
    							if (cc.getNumOrdenConstruccion().length()<4){
    								cc.setNumOrdenConstruccion(GeopistaFunctionUtils.completarConCeros(cc.getNumOrdenConstruccion(), 4));
    							}

    							if ((cc.getRefParcela().getRefCatastral() + cc.getNumOrdenConstruccion()).equals(((ElementoReparto)iteratorConstrucciones.next()).getId())){
    								activa = true;
    							}
    						}
    					}

    					data = new TableRowReparto(cc.getRefParcela().getRefCatastral1(),
    							cc.getRefParcela().getRefCatastral2(),cc.getNumOrdenConstruccion(),
    							new Boolean(activa));
    					lst.add(data);
    				}
    			}
    		}

		((TableRepartoModel)((TableSorted)jTableReparto.getModel()).getTableModel()).setData(lst);
		
    	jPanelReparto.remove(jScrollPaneReparto);
		 
    	GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
        gridBagConstraints8.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints8.weighty = 1.0;
        gridBagConstraints8.gridy = 0;
        gridBagConstraints8.weightx = 1.0;
        gridBagConstraints8.gridx = 0;
    	jPanelReparto.add(getJScrollPaneReparto(),gridBagConstraints8);
    	jPanelReparto.updateUI();
	}

    public boolean datosMinimosYCorrectos()
    {
        //Todo no se si el porcentaje es obligatorio, lo pongo por ahora.
        return (jComboBoxFormaReparto.getSelectedPatron()!=null && !jComboBoxFormaReparto.
                getSelectedPatron().equalsIgnoreCase(""));
    }
	public void cleanup() {
		// TODO Auto-generated method stub
		
	}    
}  //  @jve:decl-index=0:visual-constraint="3,-86"
