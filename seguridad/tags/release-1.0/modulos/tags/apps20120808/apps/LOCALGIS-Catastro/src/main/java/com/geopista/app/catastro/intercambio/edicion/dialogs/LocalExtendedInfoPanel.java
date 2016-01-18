package com.geopista.app.catastro.intercambio.edicion.dialogs;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.intercambio.edicion.utils.EdicionOperations;
import com.geopista.app.catastro.intercambio.edicion.utils.EdicionUtils;
import com.geopista.app.catastro.intercambio.edicion.utils.EstructuraDBListCellRenderer;
import com.geopista.app.catastro.model.beans.ConstruccionCatastro;
import com.geopista.app.catastro.model.beans.DireccionLocalizacion;
import com.geopista.app.catastro.model.beans.EstructuraDB;
import com.geopista.app.catastro.model.beans.ReferenciaCatastral;
import com.geopista.app.catastro.model.datos.economicos.DatosEconomicosConstruccion;
import com.geopista.app.catastro.model.datos.fisicos.DatosFisicosConstruccion;
import com.geopista.app.catastro.registroExpedientes.utils.UtilRegistroExp;
import com.geopista.app.utilidades.TextField;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.geopista.util.GeopistaFunctionUtils;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.Blackboard;


public class LocalExtendedInfoPanel extends JPanel
{
    private JPanel jPanelDatosIdentificacion = null;
    private JPanel jPanelDatosFisicos = null;
    
    private JLabel jLabelCargo;
    private JLabel jLabelBloque;
    private JTextField jTextFieldCargo;
    private JLabel jLabelRefCatastral1;
    private JLabel jLabelRefCatastral2;
    
    AppContext app =(AppContext) AppContext.getApplicationContext();
    private Blackboard Identificadores = app.getBlackboard();  //  @jve:decl-index=0:
    
	private JLabel jLabelNumOrden = null;
	private JPanel jPanelDatosLocalizacion = null;
	private JPanel jPanelDatosEconomicos = null;
	
	private JTextField jTextFieldBloque = null;
	private JLabel jLabelEscalera = null;
	private JLabel jLabelPlanta = null;
	private JLabel jLabelPuerta = null;
	private JTextField jTextFieldRefCatastral1 = null;
	private JTextField jTextFieldNumOrden = null;
	private JTextField jTextFieldRefCatastral2 = null;
	private JLabel jLabelTipologia = null;
	private JComboBox jComboBoxTipologia = null;
	private JLabel jLabelCategoria = null;
	private ComboBoxEstructuras jComboBoxCategoria = null;
	private JLabel jLabelTipoValor = null;
	private ComboBoxEstructuras jComboBoxTipoValor = null;
	private JTextField jTextFieldBloqueLocal;
	private JLabel jLabelUnidadConst = null;
	private JTextField jTextFieldUnidadConst = null;
	private JLabel jLabelDestinoLocal = null;
	private JLabel jLabelTipoReforma = null;
	private JComboBox jComboBoxDestinoLocal = null;
	private ComboBoxEstructuras jComboBoxTipoReforma = null;
	private JLabel jLabelAnioReforma = null;
	private JTextField jTextFieldAnioReforma = null;
	private JLabel jLabelSuperficieTotal = null;
	private JTextField jTextFieldSuperficieTotal = null;
	private JLabel jLabelSuperficieTerrazas = null;
	private JTextField jTextFieldSuperficieTerrazas = null;
	private JLabel jLabelSupImpLocalOtrasPlantas = null;
	private JTextField jTextFieldSupImpLocalOtrasPlantas = null;
	private JCheckBox jCheckBoxLocalInterior = null;
	private boolean okPressed = false;
	private JLabel jLabelCoefAprecEcon = null;
	private JLabel jLabelUso = null;
	private JTextField jTextFieldCoefAprecEcon = null;
	private JComboBox jComboBoxEscalera = null;
	private JComboBox jComboBoxPlanta = null;
	private JComboBox jComboBoxPuerta = null;
	private JComboBox jComboBoxUso = null;

	
	public LocalExtendedInfoPanel()
    {
        super();
        initialize();       
        
    }
       
    public LocalExtendedInfoPanel(ConstruccionCatastro construc)
    {
        super();
        initialize();       
        
        loadData (construc);
        
    }
    

    public class ListenerTextField implements  DocumentListener{
    	/*
    	 * Método que recibe evento cuando se escribe en alguno de los jTextField que tienen un escuchador DocumentListener
    	 * Según el campo origen del evento, cambia las etiquetas obligatorias.
    	 * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
    	 */
        public void insertUpdate(DocumentEvent e){
        	//Panel Base Liquidable
        	if(jTextFieldAnioReforma.getDocument()==e.getDocument()){
                jLabelAnioReforma.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "local.panel.datosfisicos.anioreforma")));
                jLabelTipoReforma.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "local.panel.datosfisicos.tiporeforma")));             		
        	}
        }
    	/*
    	 * Método que recibe evento cuando se borra caracteres en alguno de los jTextField que tienen un escuchador DocumentListener
    	 * Según el campo origen del evento, cambia las etiquetas obligatorias.
    	 * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
    	 */
        public void removeUpdate(DocumentEvent e){
        	//Panel Base Liquidable
        	if(jTextFieldAnioReforma.getDocument().getLength()<=0 && jComboBoxTipoReforma.getSelectedIndex()<=0){
                jLabelAnioReforma.setText(I18N.get("Expedientes", "local.panel.datosfisicos.anioreforma"));
        		jLabelTipoReforma.setText(I18N.get("Expedientes", "local.panel.datosfisicos.tiporeforma"));
        	}
        }
        /*
         * Método declarado pero no implementado debido a que no se utiliza, pero su declaración ha de ser obligatoria.
         * @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.DocumentEvent)
         */
        public void changedUpdate(DocumentEvent e){        	
        }
    }
    
    public class ListenerCombo implements  ActionListener{
    	/*
    	 * Gestiona los eventos de los ComboBox y cambia adecuadamente las etiquetas de aquellos campos que son obligatorios
    	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
    	 */ 
        public void actionPerformed(ActionEvent e){  
        	//Panel Base Liquidable
        	if(jComboBoxTipoReforma.getSelectedIndex()>0 || jTextFieldAnioReforma.getDocument().getLength()>0){
                jLabelAnioReforma.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "local.panel.datosfisicos.anioreforma")));
                jLabelTipoReforma.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "local.panel.datosfisicos.tiporeforma")));
           	}else{
           		jLabelAnioReforma.setText(I18N.get("Expedientes", "local.panel.datosfisicos.anioreforma"));
           	 	jLabelTipoReforma.setText(I18N.get("Expedientes", "local.panel.datosfisicos.tiporeforma"));
        	}      	
        }
    }
    
    private void initialize()
    {
        Locale loc=I18N.getLocaleAsObject();         
        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.catastro.intercambio.language.Expedientesi18n",loc,this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("Expedientes",bundle);
                
        this.setLayout(new GridBagLayout());
        this.setSize(new Dimension(800, 757));
        this.add(getJPanelDatosIdentificacion(), 
                new GridBagConstraints(0, 0, 1, 1, 0.5, 0.5, GridBagConstraints.NORTH, 
                        GridBagConstraints.BOTH, new Insets(0, 5, 0, 5), 0, 0));
        this.add(getJPanelDatosLocalizacion(), 
                new GridBagConstraints(0,1,1,1,0.5, 0.5,GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0,5,0,5),0,0));
        this.add(getJPanelDatosFisicos(), new GridBagConstraints(0, 2, 1, 1, 0.5, 0.5, GridBagConstraints.NORTH, 
                GridBagConstraints.BOTH, new Insets(0, 5, 0, 5), 0, 0));
        this.add(getJPanelDatosEconomicos(), 
                new GridBagConstraints(0,3,1,1,0.5, 0.5,GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0,5,0,5),0,0));
    

        //Inicializa los desplegables        
        if (Identificadores.get("ListaTipoDestino")==null)
        {
            EdicionOperations oper = new EdicionOperations();
            ArrayList lst = oper.obtenerTiposDestino();
            Identificadores.put("ListaTipoDestino", lst);
            EdicionUtils.cargarLista(getJComboBoxDestinoLocal(), lst);
        }
        else
        {
            EdicionUtils.cargarLista(getJComboBoxDestinoLocal(), 
                    (ArrayList)Identificadores.get("ListaTipoDestino"));
        }   
        
        if (Identificadores.get("ListaUso")==null)
        {
            EdicionOperations oper = new EdicionOperations();
            ArrayList lst = oper.obtenerUso();
            Identificadores.put("ListaUso", lst);
            EdicionUtils.cargarLista(getJComboBoxUso(), lst);
        }
        else
        {
            EdicionUtils.cargarLista(getJComboBoxUso(), 
                    (ArrayList)Identificadores.get("ListaUso"));
        } 

        if (Identificadores.get("ListaTipologiaLocal")==null)
        {
            EdicionOperations oper = new EdicionOperations();
            ArrayList lst = oper.obtenerTipologiasLocales();
            Identificadores.put("ListaTipologiaLocal", lst);
            EdicionUtils.cargarLista(getJComboBoxTipologia(), lst);
        }
        else
        {
            EdicionUtils.cargarLista(getJComboBoxTipologia(), 
                    (ArrayList)Identificadores.get("ListaTipologiaLocal"));
        }           
        
        if (Identificadores.get("ListaEscalera")==null)
        {
            EdicionOperations oper = new EdicionOperations();
            ArrayList lst = oper.obtenerEscalera();
            Identificadores.put("ListaEscalera", lst);
            EdicionUtils.cargarLista(getJComboBoxEscalera(), lst);
        }
        else
        {
            EdicionUtils.cargarLista(getJComboBoxEscalera(), 
                    (ArrayList)Identificadores.get("ListaEscalera"));
        } 
        
        if (Identificadores.get("ListaPlanta")==null)
        {
            EdicionOperations oper = new EdicionOperations();
            ArrayList lst = oper.obtenerPlanta();
            Identificadores.put("ListaPlanta", lst);
            EdicionUtils.cargarLista(getJComboBoxPlanta(), lst);
        }
        else
        {
            EdicionUtils.cargarLista(getJComboBoxPlanta(), 
                    (ArrayList)Identificadores.get("ListaPlanta"));
        } 
        
        if (Identificadores.get("ListaPuerta")==null)
        {
            EdicionOperations oper = new EdicionOperations();
            ArrayList lst = oper.obtenerPuerta();
            Identificadores.put("ListaPuerta", lst);
            EdicionUtils.cargarLista(getJComboBoxPuerta(), lst);
        }
        else
        {
            EdicionUtils.cargarLista(getJComboBoxPuerta(), 
                    (ArrayList)Identificadores.get("ListaPuerta"));
        } 
    }
    
    
    public void loadData(ConstruccionCatastro construc)
    {
        if (construc !=null)
        {
            jTextFieldRefCatastral1.setText(((ConstruccionCatastro)construc).getRefParcela().getRefCatastral1());
            jTextFieldRefCatastral2.setText(((ConstruccionCatastro)construc).getRefParcela().getRefCatastral2());
            jTextFieldCargo.setText(((ConstruccionCatastro)construc).getNumOrdenBienInmueble());
            
            NumberFormat formatter = new DecimalFormat("0000");            
            jTextFieldNumOrden.setText(((ConstruccionCatastro)construc).getNumOrdenConstruccion()!=null?
            		formatter.format(new Integer(((ConstruccionCatastro)construc).getNumOrdenConstruccion())).toString():"");
            
            jTextFieldBloque.setText(((ConstruccionCatastro)construc).getDomicilioTributario().getBloque());
                   
            EstructuraDB eEscalera = new EstructuraDB();
            if((jComboBoxEscalera.getItemCount() > 0) && (((ConstruccionCatastro)construc).getDomicilioTributario().getEscalera()!=null)){            	
            	eEscalera.setPatron(((ConstruccionCatastro)construc).getDomicilioTributario().getEscalera());
                jComboBoxEscalera.setSelectedItem(eEscalera);
            }
            else{
            	jComboBoxEscalera.setSelectedItem(eEscalera);
            }
            
            EstructuraDB ePlanta = new EstructuraDB();
            if((jComboBoxPlanta.getItemCount() > 0) && (((ConstruccionCatastro)construc).getDomicilioTributario().getPlanta()!=null)){            	
            	ePlanta.setPatron(((ConstruccionCatastro)construc).getDomicilioTributario().getPlanta());
                jComboBoxPlanta.setSelectedItem(ePlanta);
            }
            else{
            	jComboBoxPlanta.setSelectedItem(ePlanta);
            }
                          
            EstructuraDB ePuerta = new EstructuraDB();
            if((jComboBoxPuerta.getItemCount() > 0) && (((ConstruccionCatastro)construc).getDomicilioTributario().getPuerta()!=null)){
            	ePuerta.setPatron(((ConstruccionCatastro)construc).getDomicilioTributario().getPuerta());
            	jComboBoxPuerta.setSelectedItem(ePuerta);
            }
            else{
            	jComboBoxPuerta.setSelectedItem(ePuerta);
            }
            
            jTextFieldUnidadConst.setText(((ConstruccionCatastro)construc).getDatosFisicos().getCodUnidadConstructiva());
            
            EstructuraDB e = new EstructuraDB();
            e.setPatron(((ConstruccionCatastro)construc).getDatosFisicos().getCodDestino());
            jComboBoxDestinoLocal.setSelectedItem(e);
            if (jComboBoxDestinoLocal.getSelectedItem()==null)
            {
                jComboBoxDestinoLocal.setSelectedIndex(0);                
            }
    		jComboBoxTipoReforma.addActionListener(new ListenerCombo());
            if(((ConstruccionCatastro)construc).getDatosFisicos().getTipoReforma()!=null)
            	jComboBoxTipoReforma.setSelectedPatron(((ConstruccionCatastro)construc).getDatosFisicos().getTipoReforma());
            jTextFieldAnioReforma.getDocument().addDocumentListener(new ListenerTextField()); 
            jTextFieldAnioReforma.setText(EdicionUtils.getStringValue(((ConstruccionCatastro)construc).getDatosFisicos().getAnioReforma()));
            jTextFieldSuperficieTotal.setText(EdicionUtils.getStringValue(((ConstruccionCatastro)construc).getDatosFisicos().getSupTotal()));
            jTextFieldSuperficieTerrazas.setText(EdicionUtils.getStringValue(((ConstruccionCatastro)construc).getDatosFisicos().getSupTerrazasLocal()));
            jTextFieldSupImpLocalOtrasPlantas.setText(EdicionUtils.getStringValue(((ConstruccionCatastro)construc).getDatosFisicos().getSupImputableLocal()));
            jCheckBoxLocalInterior.setSelected(((ConstruccionCatastro)construc).getDatosFisicos().isLocalInterior()!=null?
                    ((ConstruccionCatastro)construc).getDatosFisicos().isLocalInterior().booleanValue():false);
            
            e = new EstructuraDB();
            e.setPatron(((ConstruccionCatastro)construc).getDatosEconomicos().getTipoConstruccion());
            jComboBoxTipologia.setSelectedItem(e);
            if (jComboBoxTipologia.getSelectedItem()==null)
            {
                jComboBoxTipologia.setSelectedIndex(0);                
            }
            if(((ConstruccionCatastro)construc).getDatosEconomicos().getCorrectorApreciación()!=null)
            	jTextFieldCoefAprecEcon.setText(((ConstruccionCatastro)construc).getDatosEconomicos().getCorrectorApreciación().toString());
            if(((ConstruccionCatastro)construc).getDatosEconomicos().getCodCategoriaPredominante()!=null)
            	jComboBoxCategoria.setSelectedPatron(((ConstruccionCatastro)construc).getDatosEconomicos().getCodCategoriaPredominante());
            if(((ConstruccionCatastro)construc).getDatosEconomicos().getCodTipoValor()!=null)
            	jComboBoxTipoValor.setSelectedPatron(((ConstruccionCatastro)construc).getDatosEconomicos().getCodTipoValor());

            e = new EstructuraDB();
            if((jComboBoxUso.getItemCount() > 0) && (construc.getDatosEconomicos().getCodUsoPredominante()!=null)){            	
            	e.setPatron(construc.getDatosEconomicos().getCodUsoPredominante());
            	jComboBoxUso.setSelectedItem(e);
            }
            else{
            	jComboBoxUso.setSelectedItem(e);
            }
            
        }
    }
    
    /**
     * This method initializes jPanelDatosIdentificacion	
     * 			jPanelDatosIdentificacion.add(getJTextFieldNumOrden(), gridBagConstraints41);
			jPanelDatosIdentificacion.add(getJTextField(), gridBagConstraints32);
			jPanelDatosIdentificacion.add(getJTextFieldCatastral2(), gridBagConstraints22);
	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanelDatosIdentificacion()
    {
        if (jPanelDatosIdentificacion == null)
        {       
        	                    	
            jLabelNumOrden = new JLabel("", JLabel.CENTER);
            jLabelNumOrden.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "local.panel.datosidfentificacion.numorden")));
            jLabelCargo = new JLabel("", JLabel.CENTER);
            jLabelCargo.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "local.panel.datosidfentificacion.cargo")));
            jLabelRefCatastral1 = new JLabel("", JLabel.CENTER); 
            jLabelRefCatastral1.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "local.panel.datosidentificacion.catastral1"))); 
            jLabelRefCatastral2 = new JLabel("", JLabel.CENTER); 
            jLabelRefCatastral2.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "local.panel.datosidentificacion.catastral.2")));
            
            jPanelDatosIdentificacion = new JPanel(new GridBagLayout());
            //jPanelDatosIdentificacion.setPreferredSize(new Dimension(200, 100));
            jPanelDatosIdentificacion.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("Expedientes", "local.panel.datosidentificacion.titulo"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12)));           
            
            jPanelDatosIdentificacion.add(jLabelRefCatastral1, 
                    new GridBagConstraints(0,0,1,1,0.5, 0,GridBagConstraints.CENTER, 
                    		GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
            jPanelDatosIdentificacion.add(jLabelRefCatastral2, 
                    new GridBagConstraints(1,0,1,1,0.5, 0,GridBagConstraints.CENTER, 
                    		GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));            
            jPanelDatosIdentificacion.add(jLabelCargo, 
                    new GridBagConstraints(2,0,1,1,0.5, 0,GridBagConstraints.CENTER, 
                    		GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));            
            jPanelDatosIdentificacion.add(jLabelNumOrden, 
            		new GridBagConstraints(3,0,1,1,0.5, 0,GridBagConstraints.CENTER, 
            				GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
            jPanelDatosIdentificacion.add(getJTextFieldRefCatastral1(), 
					new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH,
		                    GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosIdentificacion.add(getJTextFieldNumOrden(), 
					new GridBagConstraints(3, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH,
		                    GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
			jPanelDatosIdentificacion.add(getJTextFieldRefCatastral2(), 
					new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH,
		                    GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
			jPanelDatosIdentificacion.add(getJTextFieldCargo(), 
					new GridBagConstraints(2,1,1,1,0.5, 0,GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
                   
            
        }
        return jPanelDatosIdentificacion;
    }
    
    /**
     * This method initializes jPanelDatosFisicos	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanelDatosFisicos()
    {
        if (jPanelDatosFisicos == null)
        {            
            
            jLabelSupImpLocalOtrasPlantas = new JLabel("", JLabel.CENTER);
            jLabelSupImpLocalOtrasPlantas.setText(I18N.get("Expedientes", "local.panel.datosfisicos.supimplocalotrasplantas"));
            
            jLabelSuperficieTerrazas = new JLabel("", JLabel.CENTER);
            jLabelSuperficieTerrazas.setText(I18N.get("Expedientes", "local.panel.datosfisicos.superficieterrazas"));
            
            jLabelSuperficieTotal = new JLabel("", JLabel.CENTER);
            jLabelSuperficieTotal.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "local.panel.datosfisicos.superficietotal")));
            
            jLabelAnioReforma = new JLabel("", JLabel.CENTER);
            jLabelAnioReforma.setText(I18N.get("Expedientes", "local.panel.datosfisicos.anioreforma"));
            
            jLabelTipoReforma = new JLabel("", JLabel.CENTER);
            jLabelTipoReforma.setText(I18N.get("Expedientes", "local.panel.datosfisicos.tiporeforma"));                        
            
            jLabelUnidadConst = new JLabel("", JLabel.CENTER);
            jLabelUnidadConst.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "local.panel.datosfisicos.unidadconst")));
            
            jPanelDatosFisicos = new JPanel(new GridBagLayout());
            jPanelDatosFisicos.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("Expedientes", "local.panel.datosfisicos.titulo"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12)));
            
            jPanelDatosFisicos.add(jLabelUnidadConst, 
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER,
		                    GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));            		
            jPanelDatosFisicos.add(getJTextFieldUnidadConst(), 
					new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER,
		                    GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));            
            jPanelDatosFisicos.add(jLabelTipoReforma,
					new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER,
		                    GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));            
            jPanelDatosFisicos.add(getJComboBoxTipoReforma(), 
					new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER,
		                    GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosFisicos.add(jLabelAnioReforma, 
					new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER,
		                    GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosFisicos.add(getJTextFieldAnioReforma(), 
					new GridBagConstraints(2, 1, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER,
		                    GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosFisicos.add(jLabelSuperficieTotal, 
					new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER,
		                    GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosFisicos.add(getJTextFieldSuperficieTotal(), 
					new GridBagConstraints(0, 3, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER,
		                    GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosFisicos.add(jLabelSuperficieTerrazas, 
					new GridBagConstraints(1, 2, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER,
		                    GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosFisicos.add(getJTextFieldSuperficieTerrazas(), 
					new GridBagConstraints(1, 3, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER,
		                    GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosFisicos.add(jLabelSupImpLocalOtrasPlantas, 
					new GridBagConstraints(2, 2, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER,
		                    GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosFisicos.add(getJTextFieldSupImpLocalOtrasPlantas(), 
					new GridBagConstraints(2, 3, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER,
		                    GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            
        }
        return jPanelDatosFisicos;
    }
    
       
    /**
     * This method initializes jTextFieldCargo 
     *  
     * @return javax.swing.JTextField   
     */
    private JTextField getJTextFieldCargo()
    {
        if (jTextFieldCargo == null)
        {
            jTextFieldCargo = new TextField(4);
            jTextFieldCargo.addCaretListener(new CaretListener()
            {
        public void caretUpdate(CaretEvent evt)
        {
            EdicionUtils.chequeaLongYNumCampoEdit(jTextFieldCargo,4, app.getMainFrame());
        }
            });
        }
        jTextFieldCargo.setName("numerocargo");
        return jTextFieldCargo;
    }
    
   
    /**
     * This method initializes jTextFieldBloqueLocal	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getJTextFieldBloqueLocal()
    {
        if (jTextFieldBloqueLocal == null)
        {
            jTextFieldBloqueLocal = new TextField(4);
        }
        return jTextFieldBloqueLocal;
    }

 
	private JPanel getJPanelDatosLocalizacion() {
		
		if (jPanelDatosLocalizacion == null) {
						
			jLabelPuerta = new JLabel("", JLabel.CENTER);
			jLabelPuerta.setText(I18N.get("Expedientes", "local.panel.datoslocalizacion.puerta"));			
			jLabelPlanta = new JLabel("", JLabel.CENTER);
			jLabelPlanta.setText(I18N.get("Expedientes", "local.panel.datoslocalizacion.planta"));			
			jLabelEscalera = new JLabel("", JLabel.CENTER);
			jLabelEscalera.setText(I18N.get("Expedientes", "local.panel.datoslocalizacion.escalera"));			
			jLabelBloque = new JLabel("", JLabel.CENTER);
			jLabelBloque.setText(I18N.get("Expedientes", "local.panel.datoslocalizacion.bloque"));
			
			jPanelDatosLocalizacion = new JPanel();
			jPanelDatosLocalizacion.setLayout(new GridBagLayout());
			jPanelDatosLocalizacion.setPreferredSize(new Dimension(200, 100));
			jPanelDatosLocalizacion.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("Expedientes", "local.panel.datoslocalizacion.titulo"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12)));
			
			jPanelDatosLocalizacion.add(jLabelBloque, 
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER,
		                    GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
			jPanelDatosLocalizacion.add(jLabelEscalera, 
					new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER,
		                    GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
			jPanelDatosLocalizacion.add(jLabelPlanta, 
					new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER,
		                    GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
			jPanelDatosLocalizacion.add(jLabelPuerta, 
					new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER,
		                    GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));						
			
			jPanelDatosLocalizacion.add(getJTextFieldBloque(),  
					new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH,
		                    GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
			
			jPanelDatosLocalizacion.add(getJComboBoxEscalera(),  
					new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH,
		                    GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
			
			jPanelDatosLocalizacion.add(getJComboBoxPlanta(),  
					new GridBagConstraints(2, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH,
		                    GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
			
			jPanelDatosLocalizacion.add(getJComboBoxPuerta(),  
					new GridBagConstraints(3, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH,
		                    GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
			
		}
		return jPanelDatosLocalizacion;
	}

	/**
	 * This method initializes jPanelDatosEconomicos	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelDatosEconomicos() {
		if (jPanelDatosEconomicos == null) {
						
			jLabelTipoValor = new JLabel("", JLabel.CENTER);
			jLabelTipoValor.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "local.panel.datoseconomicos.tipovalor")));			
			jLabelCategoria = new JLabel("", JLabel.CENTER);
			jLabelCategoria.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "local.panel.datoseconomicos.categoria")));			
			jLabelTipologia = new JLabel("", JLabel.CENTER);
			jLabelTipologia.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes","local.panel.datoseconomicos.tipologia")));
			jLabelDestinoLocal = new JLabel("", JLabel.CENTER);
            jLabelDestinoLocal.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "local.panel.datoseconomicos.destinolocal")));
            jLabelCoefAprecEcon  = new JLabel("", JLabel.CENTER);
            jLabelCoefAprecEcon.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "local.panel.datoseconomicos.coefaprececon")));
            jLabelUso  = new JLabel("", JLabel.CENTER);
            jLabelUso.setText(I18N.get("Expedientes", "local.panel.datoseconomicos.uso"));
			
			jPanelDatosEconomicos = new JPanel();
			jPanelDatosEconomicos.setLayout(new GridBagLayout());
			jPanelDatosEconomicos.setPreferredSize(new Dimension(200, 120));
			jPanelDatosEconomicos.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("Expedientes", "local.panel.datoseconomicos.titulo"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12)));
			
				
			jPanelDatosEconomicos.add(jLabelTipologia,
					new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER,
                    GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
			jPanelDatosEconomicos.add(getJComboBoxTipologia(), 
					new GridBagConstraints(0, 3, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER,
		                    GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
			jPanelDatosEconomicos.add(jLabelCategoria, 
					new GridBagConstraints(1, 2, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER,
		                    GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
			jPanelDatosEconomicos.add(getJComboBoxCategoria(), 
					new GridBagConstraints(1, 3, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER,
		                    GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
			jPanelDatosEconomicos.add(jLabelTipoValor, 
					new GridBagConstraints(2, 2, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER,
		                    GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
			jPanelDatosEconomicos.add(getJComboBoxTipoValor(), 
					new GridBagConstraints(2, 3, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER,
		                    GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
			jPanelDatosEconomicos.add(getJComboBoxDestinoLocal(), 
					new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER,
		                    GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));			
			jPanelDatosEconomicos.add(jLabelDestinoLocal, 
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER,
		                    GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
			jPanelDatosEconomicos.add(getJCheckBoxLocalInterior(), 
					new GridBagConstraints(2, 1, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER,
		                    GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
			jPanelDatosEconomicos.add(jLabelCoefAprecEcon, 
					new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER,
		                    GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
			jPanelDatosEconomicos.add(getJTextFieldCoefAprecEcon(), 
					new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER,
		                    GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
			jPanelDatosEconomicos.add(getJCheckBoxLocalInterior(), 
					new GridBagConstraints(2, 1, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER,
		                    GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
			jPanelDatosEconomicos.add(jLabelUso,
					new GridBagConstraints(0, 5, 2, 1, 0.1, 0.1, GridBagConstraints.CENTER,
                    GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
			jPanelDatosEconomicos.add(getJComboBoxUso(), 
					new GridBagConstraints(0, 6, 2, 1, 0.1, 0.1, GridBagConstraints.CENTER,
		                    GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
		}
		return jPanelDatosEconomicos;
	}

	/**
	 * This method initializes jTextFieldBloque	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldBloque() {
		if (jTextFieldBloque == null) {
			jTextFieldBloque = new TextField(4);
		}
		return jTextFieldBloque;
	}

	private JTextField getJTextFieldCoefAprecEcon() {
		if (jTextFieldCoefAprecEcon  == null) {
			jTextFieldCoefAprecEcon = new TextField();
			jTextFieldCoefAprecEcon.addCaretListener(new CaretListener()
            {
        public void caretUpdate(CaretEvent evt)
        {
            EdicionUtils.chequeaLongYDecimalCampoEdit(jTextFieldCoefAprecEcon,6, app.getMainFrame());
        }
            });
		}
		return jTextFieldCoefAprecEcon;
	}


	/**
	 * This method initializes jTextFieldEscalera	
	 * 	
	 * @return javax.swing.JTextField	
	 */
		
	private JComboBox getJComboBoxEscalera() {
		if (jComboBoxEscalera  == null) {
			jComboBoxEscalera = new JComboBox();
        	jComboBoxEscalera.setRenderer(new EstructuraDBListCellRenderer());
		}
		return jComboBoxEscalera;
	}

	/**
	 * This method initializes jTextFieldPlanta	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	
	private JComboBox getJComboBoxPlanta() {
		if (jComboBoxPlanta  == null) {
			jComboBoxPlanta = new JComboBox();
        	jComboBoxPlanta.setRenderer(new EstructuraDBListCellRenderer());
		}
		return jComboBoxPlanta;
	}

	/**
	 * This method initializes jTextFieldPuerta	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	
	private JComboBox getJComboBoxPuerta() {
		if (jComboBoxPuerta  == null) {
			jComboBoxPuerta = new JComboBox();
        	jComboBoxPuerta.setRenderer(new EstructuraDBListCellRenderer());
		}
		return jComboBoxPuerta;
	}

	/**
	 * This method initializes jTextFieldRefCatastral1	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldRefCatastral1() {
		if (jTextFieldRefCatastral1 == null) {
			jTextFieldRefCatastral1 = new TextField(7);
			jTextFieldRefCatastral1.setEditable(false);
		}
		return jTextFieldRefCatastral1;
	}

	/**
	 * This method initializes jTextFieldNumOrden	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldNumOrden() {
		if (jTextFieldNumOrden == null) {
			jTextFieldNumOrden = new TextField(4);
            jTextFieldNumOrden.addCaretListener(new CaretListener()
                    {
                public void caretUpdate(CaretEvent evt)
                {
                    EdicionUtils.chequeaLongYNumCampoEdit(jTextFieldNumOrden,4, app.getMainFrame());
                }
                    });
		}
		return jTextFieldNumOrden;
	}

	/**
	 * This method initializes jTextFieldRefCatastral2	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldRefCatastral2() {
		if (jTextFieldRefCatastral2 == null) {
			jTextFieldRefCatastral2 = new TextField(7);
			jTextFieldRefCatastral2.setEditable(false);
		}
		return jTextFieldRefCatastral2;
	}

	/**
	 * This method initializes jComboBoxTipologia	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getJComboBoxTipologia() {
		if (jComboBoxTipologia == null) {
			jComboBoxTipologia = new JComboBox();  
            jComboBoxTipologia.setRenderer(new EstructuraDBListCellRenderer());            
		}
		return jComboBoxTipologia;
	}

	/**
	 * This method initializes jComboBoxCategoria	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private ComboBoxEstructuras getJComboBoxCategoria() {
		if (jComboBoxCategoria == null) {			
            Estructuras.cargarEstructura("Categoría de construcción");
            jComboBoxCategoria = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, app.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), true);          
		}
		return jComboBoxCategoria;
	}

	/**
	 * This method initializes jComboBoxTipoValor	
	 * 	
	 * @return ComboBoxEstructuras	
	 */
	private ComboBoxEstructuras getJComboBoxTipoValor() {
		if (jComboBoxTipoValor == null) {	
            Estructuras.cargarEstructura("Codigo Tipo Valor");
            jComboBoxTipoValor = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, app.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), true);
		}
		jComboBoxTipoValor.setName("tipovalor");
		
		return jComboBoxTipoValor;
	}

	/**
	 * This method initializes jTextFieldUnidadConst	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldUnidadConst() {
	    if (jTextFieldUnidadConst == null) {
	        jTextFieldUnidadConst = new TextField(4);
	        jTextFieldUnidadConst.addCaretListener(new CaretListener()
	                {
	            public void caretUpdate(CaretEvent evt)
	            {
	                EdicionUtils.chequeaLongYNumCampoEdit(jTextFieldUnidadConst,4, app.getMainFrame());
	            }
	                });
	    }
	    return jTextFieldUnidadConst;
	}

    private JComboBox getJComboBoxUso()
    {
        if (jComboBoxUso   == null)
        {
        	jComboBoxUso = new JComboBox();
        	jComboBoxUso.setRenderer(new EstructuraDBListCellRenderer());
            
        }
        return jComboBoxUso;
    }

    /**
	 * This method initializes jComboBoxDestinoLocal	
	 * 	
	 * @return ComboBoxEstructuras	
	 */
	private JComboBox getJComboBoxDestinoLocal() {
		if (jComboBoxDestinoLocal == null) {
			jComboBoxDestinoLocal = new JComboBox();
            jComboBoxDestinoLocal.setRenderer(new EstructuraDBListCellRenderer());
		}
		return jComboBoxDestinoLocal;
	}

	/**
	 * This method initializes jComboBoxTipoReforma	
	 * 	
	 * @return ComboBoxEstructuras	
	 */
	private ComboBoxEstructuras getJComboBoxTipoReforma() {
		if (jComboBoxTipoReforma == null) {			
            Estructuras.cargarEstructura("Tipo Reforma");
            jComboBoxTipoReforma = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, app.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), true);
		}
		jComboBoxTipoReforma.addActionListener(new ListenerCombo());
		return jComboBoxTipoReforma;
	}

	/**
	 * This method initializes jTextFieldAnioReforma	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldAnioReforma() {
		if (jTextFieldAnioReforma == null) {
			jTextFieldAnioReforma = new TextField(4);
            jTextFieldAnioReforma.addCaretListener(new CaretListener()
                    {
                public void caretUpdate(CaretEvent evt)
                {
                    EdicionUtils.chequeaLongYNumCampoEdit(jTextFieldAnioReforma,4, app.getMainFrame());
                }
                    });
		}
        jTextFieldAnioReforma.getDocument().addDocumentListener(new ListenerTextField()); 
		return jTextFieldAnioReforma;
	}

	/**
	 * This method initializes jTextFieldSuperficieTotal	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldSuperficieTotal() {
	    if (jTextFieldSuperficieTotal == null) {
	        jTextFieldSuperficieTotal = new JTextField();
	        jTextFieldSuperficieTotal.addCaretListener(new CaretListener()
	                {
	            public void caretUpdate(CaretEvent evt)
	            {
	                EdicionUtils.chequeaLongYNumCampoEdit(jTextFieldSuperficieTotal,7, app.getMainFrame());
	            }
	                });
	    }
	    return jTextFieldSuperficieTotal;
	}

	/**
	 * This method initializes jTextFieldSuperficieTerrazas	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldSuperficieTerrazas() {
		if (jTextFieldSuperficieTerrazas == null) {
			jTextFieldSuperficieTerrazas = new JTextField();
            jTextFieldSuperficieTerrazas.addCaretListener(new CaretListener()
                    {
                public void caretUpdate(CaretEvent evt)
                {
                    EdicionUtils.chequeaLongYNumCampoEdit(jTextFieldSuperficieTerrazas,7, app.getMainFrame());
                }
                    });
		}
		return jTextFieldSuperficieTerrazas;
	}

	/**
	 * This method initializes jTextFieldSupImpLocalOtrasPlantas	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldSupImpLocalOtrasPlantas() {
	    if (jTextFieldSupImpLocalOtrasPlantas == null) {
	        jTextFieldSupImpLocalOtrasPlantas = new JTextField();
	        jTextFieldSupImpLocalOtrasPlantas.addCaretListener(new CaretListener()
	                {
	            public void caretUpdate(CaretEvent evt)
	            {
	                EdicionUtils.chequeaLongYNumCampoEdit(jTextFieldSupImpLocalOtrasPlantas,7, app.getMainFrame());
	            }
	                });
	    }
	    return jTextFieldSupImpLocalOtrasPlantas;
	}

	/**
	 * This method initializes jCheckBoxLocalInterior	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBoxLocalInterior() {
		if (jCheckBoxLocalInterior == null) {
			jCheckBoxLocalInterior = new JCheckBox();
			jCheckBoxLocalInterior.setText(I18N.get("Expedientes", "local.panel.datosfisicos.localinterior"));
		}
		return jCheckBoxLocalInterior;
	}
	
	
	public String validateInput()
    {
        return null; 
    }
	
	
	public ConstruccionCatastro getConstruccionCatastro(ConstruccionCatastro cons)
	{
        if (okPressed)
        {
        	if(cons==null)
            {
                cons = new ConstruccionCatastro();
            }
        	
            DireccionLocalizacion dir;
            if(cons.getDomicilioTributario()== null)
            {
                dir = new DireccionLocalizacion();
                cons.setDomicilioTributario(dir);
            }
            dir = cons.getDomicilioTributario();
            dir.setBloque(jTextFieldBloque.getText());
            if (jComboBoxEscalera.getSelectedItem() != null)
            	dir.setEscalera(((EstructuraDB)jComboBoxEscalera.getSelectedItem()).getPatron());            
            
            if (jComboBoxPlanta.getSelectedItem() != null)
            	dir.setPlanta(((EstructuraDB)jComboBoxPlanta.getSelectedItem()).getPatron());
            
            if (jComboBoxPuerta.getSelectedItem() != null)
            	dir.setPuerta(((EstructuraDB)jComboBoxPuerta.getSelectedItem()).getPatron());
            cons.setDomicilioTributario(dir);
            
            DatosEconomicosConstruccion datosEconomicosConstruccion;
            if(cons.getDatosEconomicos()==null)
            {
                datosEconomicosConstruccion =  new DatosEconomicosConstruccion();
                cons.setDatosEconomicos(datosEconomicosConstruccion);
            }
            datosEconomicosConstruccion = cons.getDatosEconomicos();

            datosEconomicosConstruccion.setCodCategoriaPredominante(jComboBoxCategoria.getSelectedPatron());
            datosEconomicosConstruccion.setCodTipoValor(jComboBoxTipoValor.getSelectedPatron());
            if (jComboBoxTipologia.getSelectedItem()!=null)
                datosEconomicosConstruccion.setTipoConstruccion(((EstructuraDB)jComboBoxTipologia.getSelectedItem()).getPatron());
            if(jTextFieldCoefAprecEcon.getText().length()>0)
            {
                datosEconomicosConstruccion.setCorrectorApreciación(new Float(jTextFieldCoefAprecEcon.getText()));
            }
	        if(jComboBoxUso.getSelectedItem()!=null)
	        	datosEconomicosConstruccion.setCodUsoPredominante(((EstructuraDB)jComboBoxUso.getSelectedItem()).getPatron());

	        cons.setDatosEconomicos(datosEconomicosConstruccion);
           
            DatosFisicosConstruccion datosFisicosConstruccion;
            if(cons.getDatosFisicos()==null)
            {
                datosFisicosConstruccion = new DatosFisicosConstruccion();
                cons.setDatosFisicos(datosFisicosConstruccion);
            }
            datosFisicosConstruccion = cons.getDatosFisicos();
            datosFisicosConstruccion.setCodUnidadConstructiva(jTextFieldUnidadConst.getText());

            if (jComboBoxDestinoLocal.getSelectedItem()!=null)
                datosFisicosConstruccion.setCodDestino(((EstructuraDB)jComboBoxDestinoLocal.getSelectedItem()).getPatron());
            if (jTextFieldAnioReforma.getText().length() > 0)
            	datosFisicosConstruccion.setAnioReforma(Integer.valueOf(jTextFieldAnioReforma.getText()));
            if (jComboBoxTipoReforma.getSelectedItem() != null)
            	datosFisicosConstruccion.setTipoReforma(jComboBoxTipoReforma.getSelectedPatron());
            datosFisicosConstruccion.setLocalInterior(Boolean.valueOf(jCheckBoxLocalInterior.getText()));
            if (jTextFieldSuperficieTotal.getText().length() > 0)
            	datosFisicosConstruccion.setSupTotal(Long.valueOf(jTextFieldSuperficieTotal.getText()));
            if (jTextFieldSuperficieTerrazas.getText().length() > 0)
            	datosFisicosConstruccion.setSupTerrazasLocal(Long.valueOf(jTextFieldSuperficieTerrazas.getText()));
            if (jTextFieldSupImpLocalOtrasPlantas.getText().length() > 0)
            	datosFisicosConstruccion.setSupImputableLocal(Long.valueOf(jTextFieldSupImpLocalOtrasPlantas.getText()));
            
            datosFisicosConstruccion.setLocalInterior(new Boolean(jCheckBoxLocalInterior.isSelected()));
            //Faltan los datos fisicos
            cons.setDatosFisicos(datosFisicosConstruccion);
            cons.setNumOrdenBienInmueble(GeopistaFunctionUtils.completarConCeros(jTextFieldCargo.getText(),4));
            cons.setNumOrdenConstruccion(jTextFieldNumOrden.getText());
            ReferenciaCatastral refFinca = new ReferenciaCatastral(jTextFieldRefCatastral1.getText()+jTextFieldRefCatastral2.getText());
            cons.setRefParcela(refFinca);
            cons.setIdConstruccion(cons.getNumOrdenBienInmueble());
        }
        else
        {
            cons = null;
        }
        return cons;
	}
	
    
    public void okPressed()
    {
        okPressed = true;    
    }

    public boolean datosMinimosYCorrectos()
    {
    	boolean okReforma=true;
    	if(jTextFieldAnioReforma.getDocument().getLength()>0 || jComboBoxTipoReforma.getSelectedIndex()>0){
    		okReforma= (jComboBoxTipoReforma!=null && jComboBoxTipoReforma.getSelectedItem()!=null && jComboBoxTipoReforma.getSelectedIndex()>0) &&
					   (jTextFieldAnioReforma.getText()!=null && jTextFieldAnioReforma.getText().length()==4);
        	if((jTextFieldAnioReforma.getText()!=null && jTextFieldAnioReforma.getText().length()!=0 && jTextFieldAnioReforma.getText().length()!=4))
        		JOptionPane.showMessageDialog(this,I18N.get("Expedientes", "Error.J8"));
    	}
    	
        return  okReforma && (jComboBoxDestinoLocal!=null && jComboBoxDestinoLocal.getSelectedItem()!=null && jComboBoxDestinoLocal.getSelectedIndex()>0) &&
        		(jComboBoxCategoria!=null && jComboBoxCategoria.getSelectedItem()!=null && jComboBoxCategoria.getSelectedIndex()>0) &&
        		(jComboBoxTipologia!=null && jComboBoxTipologia.getSelectedItem()!=null && jComboBoxTipologia.getSelectedIndex()>0) &&
        		(jTextFieldCoefAprecEcon.getText()!=null && !jTextFieldCoefAprecEcon.getText().equalsIgnoreCase("")) &&
        		(jTextFieldRefCatastral1.getText()!=null && !jTextFieldRefCatastral1.getText().equalsIgnoreCase("")) &&
        		(jTextFieldRefCatastral2.getText()!=null && !jTextFieldRefCatastral2.getText().equalsIgnoreCase("")) &&
        		(jTextFieldSuperficieTotal.getText()!=null && !jTextFieldSuperficieTotal.getText().equalsIgnoreCase("")) &&
        		(jTextFieldUnidadConst.getText()!=null && !jTextFieldUnidadConst.getText().equalsIgnoreCase("")) &&
        		(jTextFieldNumOrden.getText()!=null && !jTextFieldNumOrden.getText().equalsIgnoreCase("")) &&
                (jTextFieldCargo.getText()!=null && !jTextFieldCargo.getText().equalsIgnoreCase(""));
    }

    public boolean datosMinimosYCorrectosComun()
    {
    	boolean okReforma=true;
    	if(jTextFieldAnioReforma.getDocument().getLength()>0 || jComboBoxTipoReforma.getSelectedIndex()>0){
    		okReforma= (jComboBoxTipoReforma!=null && jComboBoxTipoReforma.getSelectedItem()!=null && jComboBoxTipoReforma.getSelectedIndex()>0) &&
					   (jTextFieldAnioReforma.getText()!=null && !jTextFieldAnioReforma.getText().equalsIgnoreCase(""));					
    	}    	
    	
        return  okReforma && (jComboBoxTipoValor.getSelectedPatron()!=null && !jComboBoxTipoValor.getSelectedPatron().equalsIgnoreCase("")) &&
                (jComboBoxDestinoLocal!=null && jComboBoxDestinoLocal.getSelectedItem()!=null && jComboBoxDestinoLocal.getSelectedIndex()>0) &&
        		(jComboBoxCategoria!=null && jComboBoxCategoria.getSelectedItem()!=null && jComboBoxCategoria.getSelectedIndex()>0) &&
        		(jComboBoxTipologia!=null && jComboBoxTipologia.getSelectedItem()!=null && jComboBoxTipologia.getSelectedIndex()>0) &&
        		(jTextFieldCoefAprecEcon.getText()!=null && !jTextFieldCoefAprecEcon.getText().equalsIgnoreCase("")) &&
        		(jTextFieldRefCatastral1.getText()!=null && !jTextFieldRefCatastral1.getText().equalsIgnoreCase("")) &&
        		(jTextFieldRefCatastral2.getText()!=null && !jTextFieldRefCatastral2.getText().equalsIgnoreCase("")) &&
        		(jTextFieldSuperficieTotal.getText()!=null && !jTextFieldSuperficieTotal.getText().equalsIgnoreCase("")) &&
        		(jTextFieldUnidadConst.getText()!=null && !jTextFieldUnidadConst.getText().equalsIgnoreCase("")) &&
        		(jTextFieldNumOrden.getText()!=null && !jTextFieldNumOrden.getText().equalsIgnoreCase(""));
    }

    public ConstruccionCatastro datosMinimosParaReparto(ConstruccionCatastro cons)
    {
        cons.setNumOrdenConstruccion(jTextFieldNumOrden.getText());
        return cons;
    }
}  //  @jve:decl-index=0:visual-constraint="10,10"
