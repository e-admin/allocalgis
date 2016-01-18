package com.geopista.ui.plugin.infcattitularidad.dialogs;


import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.intercambio.edicion.CodigoViasSistemaDialog;
import com.geopista.app.catastro.intercambio.edicion.ExpedientePanelTree;
import com.geopista.app.catastro.intercambio.edicion.ViasSistemaDialog;
import com.geopista.app.catastro.intercambio.edicion.dialogs.GestionExpedientePanel;
import com.geopista.app.catastro.intercambio.edicion.utils.EdicionOperations;
import com.geopista.app.catastro.intercambio.edicion.utils.EdicionUtils;
import com.geopista.app.catastro.intercambio.edicion.utils.EstructuraDBListCellRenderer;
import com.geopista.app.catastro.intercambio.edicion.utils.HideableNode;
import com.geopista.app.catastro.intercambio.images.IconLoader;
import com.geopista.app.catastro.model.beans.DireccionLocalizacion;
import com.geopista.app.catastro.model.beans.EstructuraDB;
import com.geopista.app.catastro.model.beans.FincaCatastro;
import com.geopista.app.catastro.model.beans.ReferenciaCatastral;
import com.geopista.app.catastro.model.beans.UnidadConstructivaCatastro;
import com.geopista.app.catastro.model.datos.ponencia.PonenciaTramos;
import com.geopista.app.catastro.model.datos.ponencia.PonenciaZonaValor;
import com.geopista.app.catastro.registroExpedientes.utils.ConstantesRegistro;
import com.geopista.app.catastro.registroExpedientes.utils.ConstantesRegistroExp;
import com.geopista.app.catastro.registroExpedientes.utils.UtilRegistroExp;
import com.geopista.app.utilidades.TextField;
import com.geopista.app.utilidades.comboBoxTipo.ComboBoxKeyTipoDestinoSelectionManager;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.geopista.util.FeatureExtendedPanel;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.Blackboard;

public class UnidadConstructivaPanel extends JPanel implements FeatureExtendedPanel
{    
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    private Blackboard Identificadores = aplicacion.getBlackboard();
      
    private JPanel jPanelDatosUC = null;
    private JPanel jPanelDatosLocalizacion = null;
    private JPanel jPanelDatosFisicos = null;
    private JPanel jPanelDatosEconomicos = null;
    private JTextField jTextFieldCodigoUC = null;
    private JLabel jLabelCodigoUC = null;
    private JLabel jLabelClaseUC = null;
    private ComboBoxEstructuras jComboBoxClaseUC = null;
    private JLabel jLabelVia = null;
    private JLabel jLabelDireccion = null;
    private JButton jButtonBuscar = null;
    private ComboBoxEstructuras jComboBoxTipoVia = null;
    private JTextField jTextFieldDireccion = null;
    private JLabel jLabelNumero = null;
    private JLabel jLabelLetra = null;
    private JLabel jLabelNumeroD = null;
    private JLabel jLabelLetraD = null;
    private JLabel jLabelBloque = null;
    private JLabel jLabelDireccionNoEstructurada = null;
    private JLabel jLabelKm = null;
    private JTextField jTextFieldNumero = null;
    private JTextField jTextFieldLetra = null;
    private JTextField jTextFieldNumeroD = null;
    private JTextField jTextFieldLetraD = null;
    private JTextField jTextFieldBloque = null;
    private JTextField jTextFieldDirNoEstructurada = null;
    private JTextField jTextFieldKm = null;
    private JLabel jLabelAnioConstruccion = null;
    private JLabel jLabelAnioI = null;
    private JLabel jLabelSuperficieSuelo = null;
    private JLabel jLabelLongitudFachada = null;
    private JLabel jLabelZonaValor = null;
    private JLabel jLabelTramo = null;
    private JLabel jLabelCodigoVia = null;
    private JTextField jTextFieldAnioConstruccion = null;
    private JTextField jTextFieldSuperficieSuelo = null;
    private JTextField jTextFieldLongitudFachada = null;
    private JPanel jPanelDatosPonencia = null;
    private JPanel jPanelCCSuelo = null;
    private JPanel jPanelCCConstruccion = null;
    private JPanel jPanelCCSueloYConstruccion = null;
    private JTextField jTextFieldCodigoVia;
    private JCheckBox jCheckBoxLongitudFachada;
    private JLabel jLabelCodEstadoConservacion = null;
    private JTextField jTextFieldEstadoConservacion = null;
    private JLabel jLabelValorCCCargasSingulares = null;
    private JTextField jTextFieldValorCCCargasSingulares = null;
    private JCheckBox jCheckBoxDepreciacion = null;
    private JCheckBox jCheckBoxSituacionesEspeciales = null;
    private JCheckBox jCheckBoxUsoNoLucrativo = null;  //  @jve:decl-index=0:visual-constraint="871,453"
    private JLabel jLabelPCatastral2;
    private JLabel jLabelPCatastral1;
    private JTextField jTextFieldPCatastral2;
    private JTextField jTextFieldPCatastral1;
	private ComboBoxEstructuras jComboBoxAnioI = null;
	private ComboBoxEstructuras jComboBoxNumeroFachadas = null;
	private UnidadConstructivaCatastro unidadCons;
	private JComboBox jComboBoxZonaValor = null;
	private JComboBox jComboBoxTramo = null;
	private JButton jButtonBuscarCodigoVia = null;
	private JPanel jPanelBotonera = null;
	private JButton jButtonEliminarUC = null;
	private JButton jButtonNuevaUC = null;
	
	private JButton jButtonAniadirUC = null;

    private JTree arbol;
    private ExpedientePanelTree panelExpedientes;
    private FincaCatastro fincaActual;
    private GestionExpedientePanel gestionExpedientePanel = null;

    public UnidadConstructivaPanel()
    {
        super();
        initialize();
    }
    
    public UnidadConstructivaPanel(GestionExpedientePanel gestionExpedientePanel)
    {
        super();
        this.gestionExpedientePanel = gestionExpedientePanel;
        initialize();
    }
    
    public UnidadConstructivaPanel(UnidadConstructivaCatastro uc)
    {
        super();
        initialize();
        load (uc);
    }
    
    public class ListenerTextField implements  DocumentListener{
    	/*
    	 * Método que recibe evento cuando se escribe en alguno de los jTextField que tienen un escuchador DocumentListener
    	 * Según el campo origen del evento, cambia las etiquetas obligatorias.
    	 * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
    	 */
        public void insertUpdate(DocumentEvent e){
        	//Panel Base Liquidable
        	if(jTextFieldDireccion.getDocument()==e.getDocument() || jTextFieldNumero.getDocument()==e.getDocument()|| jTextFieldNumeroD.getDocument()==e.getDocument() || jTextFieldLetra.getDocument()==e.getDocument() || jTextFieldLetraD.getDocument()==e.getDocument() || jTextFieldDirNoEstructurada.getDocument()==e.getDocument() || jTextFieldKm.getDocument()==e.getDocument()){
        		jLabelVia.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "unidadconstructiva.panel.datoslocalizacion.via")));
        		jLabelDireccion.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "unidadconstructiva.panel.datoslocalizacion.direccion")));
           	}
        	if(jTextFieldCodigoVia.getDocument()==e.getDocument()){
        		jTextFieldCodigoVia.setEnabled(true);
        		jTextFieldCodigoVia.setOpaque(true);
        		jButtonBuscarCodigoVia.setEnabled(true);
        		jComboBoxTramo.setEnabled(true);
        		jComboBoxZonaValor.setEnabled(false);
        		jLabelTramo.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "unidadconstructiva.panel.datoseconomicos.datosponencia.tramo")));
        		jLabelCodigoVia.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "unidadconstructiva.panel.datoseconomicos.datosponencia.codigovia")));        		
        	}
        }
    	/*
    	 * Método que recibe evento cuando se borra caracteres en alguno de los jTextField que tienen un escuchador DocumentListener
    	 * Según el campo origen del evento, cambia las etiquetas obligatorias.
    	 * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
    	 */
        public void removeUpdate(DocumentEvent e){
        	//Panel Base Liquidable
        	if(jTextFieldDireccion.getDocument().getLength()<=0 && jComboBoxTipoVia.getSelectedIndex()<=0 && jTextFieldDireccion.getDocument().getLength()<=0 && jTextFieldNumero.getDocument().getLength()<=0 && jTextFieldNumeroD.getDocument().getLength()<=0 && jTextFieldLetra.getDocument().getLength()<=0 && jTextFieldLetraD.getDocument().getLength()<=0 && jTextFieldDirNoEstructurada.getDocument().getLength()<=0 && jTextFieldKm.getDocument().getLength()<=0){
        		jLabelVia.setText(I18N.get("Expedientes", "unidadconstructiva.panel.datoslocalizacion.via"));
        		jLabelDireccion.setText(I18N.get("Expedientes", "unidadconstructiva.panel.datoslocalizacion.direccion"));
        	}else{
        		jLabelVia.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "unidadconstructiva.panel.datoslocalizacion.via"))); 
            	jLabelDireccion.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "unidadconstructiva.panel.datoslocalizacion.direccion"))); 		
        	}    
        	if(jTextFieldCodigoVia.getDocument().getLength()<=0 && jComboBoxTramo.getSelectedIndex()<=0){
        		jLabelTramo.setText(I18N.get("Expedientes", "unidadconstructiva.panel.datoseconomicos.datosponencia.tramo"));
        		jLabelCodigoVia.setText(I18N.get("Expedientes", "unidadconstructiva.panel.datoseconomicos.datosponencia.codigovia"));
        		if(jComboBoxZonaValor.getSelectedIndex()<=0){
            		jTextFieldCodigoVia.setEnabled(true);
            		jTextFieldCodigoVia.setOpaque(true);
            		jButtonBuscarCodigoVia.setEnabled(true);
            		jComboBoxTramo.setEnabled(true);
            		jComboBoxZonaValor.setEnabled(true);
        			jLabelZonaValor.setText(I18N.get("Expedientes", "unidadconstructiva.panel.datoseconomicos.datosponencia.zonavalor"));        			
        		}
        	}else{
        		if(jComboBoxTramo.getSelectedIndex()<=0){
	        		jTextFieldCodigoVia.setEnabled(false);
	        		jTextFieldCodigoVia.setOpaque(false);
	        		jButtonBuscarCodigoVia.setEnabled(false);
	        		jComboBoxTramo.setEnabled(false);
	        		jComboBoxZonaValor.setEnabled(true);
        		}else{
	        		jTextFieldCodigoVia.setEnabled(true);
	        		jTextFieldCodigoVia.setOpaque(true);
	        		jButtonBuscarCodigoVia.setEnabled(true);
	        		jComboBoxTramo.setEnabled(true);
	        		jComboBoxZonaValor.setEnabled(false);
        		}
        		if(jComboBoxZonaValor.getSelectedIndex()<=0){
        			jLabelZonaValor.setText(I18N.get("Expedientes", "unidadconstructiva.panel.datoseconomicos.datosponencia.zonavalor"));        			
        		}     		
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
        	if(jComboBoxTipoVia.getSelectedIndex()>0 || jTextFieldDireccion.getDocument().getLength()>0 || jTextFieldDireccion.getDocument().getLength()>0 || jTextFieldNumero.getDocument().getLength()>0 || jTextFieldNumeroD.getDocument().getLength()>0 || jTextFieldLetra.getDocument().getLength()>0 || jTextFieldLetraD.getDocument().getLength()>0 || jTextFieldDirNoEstructurada.getDocument().getLength()>0 || jTextFieldKm.getDocument().getLength()>0){
        		jLabelVia.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "unidadconstructiva.panel.datoslocalizacion.via")));
        		jLabelDireccion.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "unidadconstructiva.panel.datoslocalizacion.direccion")));
        	}else{ 
        		jLabelVia.setText(I18N.get("Expedientes", "unidadconstructiva.panel.datoslocalizacion.via")); 
        		jLabelDireccion.setText(I18N.get("Expedientes", "unidadconstructiva.panel.datoslocalizacion.direccion"));
           	} 

        	if(jComboBoxZonaValor.getSelectedIndex()>0 || jComboBoxTramo.getSelectedIndex()>0 || jTextFieldCodigoVia.getDocument().getLength()>0){
        		if(jComboBoxZonaValor.getSelectedIndex()>0){
            		jTextFieldCodigoVia.setEnabled(false);
            		jTextFieldCodigoVia.setOpaque(false);
            		jButtonBuscarCodigoVia.setEnabled(false);
            		jComboBoxTramo.setEnabled(false);
            		jComboBoxZonaValor.setEnabled(true);
        			jLabelZonaValor.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "unidadconstructiva.panel.datoseconomicos.datosponencia.zonavalor")));
        			if(jComboBoxTramo.getSelectedIndex()>0 || jTextFieldCodigoVia.getDocument().getLength()>0){
        				jLabelTramo.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "unidadconstructiva.panel.datoseconomicos.datosponencia.tramo")));
            			jLabelCodigoVia.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "unidadconstructiva.panel.datoseconomicos.datosponencia.codigovia")));
        			}else{
        				jLabelTramo.setText(I18N.get("Expedientes", "unidadconstructiva.panel.datoseconomicos.datosponencia.tramo"));
                   		jLabelCodigoVia.setText(I18N.get("Expedientes", "unidadconstructiva.panel.datoseconomicos.datosponencia.codigovia"));
        			}
        		}else{
            		jTextFieldCodigoVia.setEnabled(true);
            		jTextFieldCodigoVia.setOpaque(true);
            		jButtonBuscarCodigoVia.setEnabled(true);
            		jComboBoxTramo.setEnabled(true);
            		jComboBoxZonaValor.setEnabled(false);
        			jLabelZonaValor.setText(I18N.get("Expedientes", "unidadconstructiva.panel.datoseconomicos.datosponencia.zonavalor"));
        			jLabelTramo.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "unidadconstructiva.panel.datoseconomicos.datosponencia.tramo")));
        			jLabelCodigoVia.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "unidadconstructiva.panel.datoseconomicos.datosponencia.codigovia")));
        		}                            		        		
           	}else{  
        		jTextFieldCodigoVia.setEnabled(true);
        		jTextFieldCodigoVia.setOpaque(true);
        		jButtonBuscarCodigoVia.setEnabled(true);
        		jComboBoxTramo.setEnabled(true);
        		jComboBoxZonaValor.setEnabled(true);
    			jLabelZonaValor.setText(I18N.get("Expedientes", "unidadconstructiva.panel.datoseconomicos.datosponencia.zonavalor"));
           		jLabelTramo.setText(I18N.get("Expedientes", "unidadconstructiva.panel.datoseconomicos.datosponencia.tramo"));
           		jLabelCodigoVia.setText(I18N.get("Expedientes", "unidadconstructiva.panel.datoseconomicos.datosponencia.codigovia"));
           	}  
        }
    }
    
    private void initialize()
    {       
        Locale loc=I18N.getLocaleAsObject();         
        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.catastro.intercambio.language.Expedientesi18n",loc,this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("Expedientes",bundle);
        
        this.setLayout(new GridBagLayout());
        this.setSize(new java.awt.Dimension(800,575));
        this.add(getJPanelDatosUC(), 
                new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH,
                        GridBagConstraints.BOTH, new Insets (5,5,0,0), 0,0));
        this.add(getJPanelDatosLocalizacion(), 
                new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH,
                        GridBagConstraints.BOTH, new Insets (5,5,0,0), 0,0));
        this.add(getJPanelDatosFisicos(), 
                new GridBagConstraints(0, 3, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH,
                GridBagConstraints.BOTH, new Insets (5,5,0,0), 0,0));
        this.add(getJPanelDatosEconomicos(), 
                new GridBagConstraints(0, 4, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH,
                        GridBagConstraints.BOTH, new Insets (5,5,0,0), 0,0));
        this.add(getJPanelBotonera(), 
                new GridBagConstraints(0, 5, 1, 1, 0.1, 0.1, GridBagConstraints.EAST,
                        GridBagConstraints.HORIZONTAL, new Insets (5,5,0,0), 0,0));
    
        
        //Inicializa los desplegables        
       
        if (Identificadores.get("ListaZonaValor")==null)
        {
            EdicionOperations oper = new EdicionOperations();
            ArrayList lst = oper.obtenerZonaValor();
            Identificadores.put("ListaZonaValor", lst);
            //EdicionUtils.cargarLista(getJComboBoxZonaValor(), lst);
        }
        else
        {
            EdicionUtils.cargarLista(getJComboBoxZonaValor(), 
                    (ArrayList)Identificadores.get("ListaZonaValor"));
        } 
        
        if (Identificadores.get("ListaCodigoTramo")==null)
        {
            EdicionOperations oper = new EdicionOperations();
            ArrayList lst = oper.obtenerCodigoTramo();
            Identificadores.put("ListaCodigoTramo", lst);
            EdicionUtils.cargarLista(getJComboBoxTramo(), lst);
        }
        else
        {
            EdicionUtils.cargarLista(getJComboBoxTramo(), 
                    (ArrayList)Identificadores.get("ListaCodigoTramo"));
        } 

    }

    
    public void load (UnidadConstructivaCatastro uc)
    {
        
        unidadCons = uc;
        //datos localización
        jComboBoxTipoVia.addActionListener(new ListenerCombo());
        jComboBoxTipoVia.setSelectedPatron(uc.getDirUnidadConstructiva().getTipoVia());
        jTextFieldDireccion.getDocument().addDocumentListener(new ListenerTextField());  
        jTextFieldDireccion.setText(uc.getDirUnidadConstructiva().getNombreVia());
        jTextFieldNumero.getDocument().addDocumentListener(new ListenerTextField());  
        if (uc.getDirUnidadConstructiva().getPrimerNumero() == -1){
        	jTextFieldNumero.setText("");
        }
        else{
        	jTextFieldNumero.setText(EdicionUtils.getStringValue(uc.getDirUnidadConstructiva().getPrimerNumero()));
        }
        jTextFieldLetra.getDocument().addDocumentListener(new ListenerTextField()); 
        jTextFieldLetra.setText(uc.getDirUnidadConstructiva().getPrimeraLetra());
        jTextFieldNumeroD.getDocument().addDocumentListener(new ListenerTextField()); 
        if (uc.getDirUnidadConstructiva().getSegundoNumero() == -1){
        	jTextFieldNumeroD.setText("");
        }
        else{
        	jTextFieldNumeroD.setText(EdicionUtils.getStringValue(uc.getDirUnidadConstructiva().getSegundoNumero()));
        }
        jTextFieldLetraD.getDocument().addDocumentListener(new ListenerTextField()); 
        jTextFieldLetraD.setText(uc.getDirUnidadConstructiva().getSegundaLetra());
        jTextFieldBloque.setText(uc.getDirUnidadConstructiva().getBloque());
        jTextFieldDirNoEstructurada.getDocument().addDocumentListener(new ListenerTextField());  
        jTextFieldDirNoEstructurada.setText(uc.getDirUnidadConstructiva().getDireccionNoEstructurada());
        jTextFieldKm.getDocument().addDocumentListener(new ListenerTextField());  
        if (uc.getDirUnidadConstructiva().getKilometro() == -1){
        	jTextFieldKm.setText("");
        }
        else{
        	jTextFieldKm.setText(EdicionUtils.getStringValue(uc.getDirUnidadConstructiva().getKilometro()));
        }
        
        
        //datos físicos  
        if (uc.getDatosFisicos().getAnioConstruccion()!= null){
        	jTextFieldAnioConstruccion.setText(EdicionUtils.getStringValue(uc.getDatosFisicos().getAnioConstruccion().intValue()));
        }
        else{
        	jTextFieldAnioConstruccion.setText("");
        }
        
        if(uc.getDatosFisicos().getIndExactitud()!=null)
        	jComboBoxAnioI.setSelectedPatron(EdicionUtils.getStringValue(uc.getDatosFisicos().getIndExactitud()));
        jTextFieldSuperficieSuelo.setText(EdicionUtils.getStringValue(uc.getDatosFisicos().getSupOcupada()));
        jTextFieldLongitudFachada.setText(uc.getDatosFisicos().getLongFachada()!=null?
        		EdicionUtils.getStringValue(uc.getDatosFisicos().getLongFachada().intValue()):"");
        
        
        //datos económicos        
        jComboBoxZonaValor.addActionListener(new ListenerCombo());
        if(uc.getDatosEconomicos().getZonaValor()!=null){
        	if(uc.getDatosEconomicos().getZonaValor().getCodZonaValor()!=null){
        		jComboBoxZonaValor.setSelectedItem(uc.getDatosEconomicos().getZonaValor().getCodZonaValor());
        	}
        	else{
        		jComboBoxZonaValor.setSelectedIndex(0);
        	}
        }
        else{
        	jComboBoxZonaValor.setSelectedIndex(0);
        }
        jTextFieldCodigoVia.getDocument().addDocumentListener(new ListenerTextField()); 
        jTextFieldCodigoVia.setText(uc.getDatosEconomicos().getCodViaPonencia());
        jComboBoxTramo.addActionListener(new ListenerCombo());
        
//        if(uc.getDatosEconomicos().getTramoPonencia()!=null){
//        	if(uc.getDatosEconomicos().getTramoPonencia().getCodTramo()!=null){
//        		jComboBoxTramo.setSelectedItem(uc.getDatosEconomicos().getTramoPonencia().getCodTramo());
//        		jComboBoxTramo.getComponents();
//        		jComboBoxTramo.getSelectedItem();
//        	}
//        	else{
//        		jComboBoxTramo.setSelectedIndex(0);
//        	}
//        }
//        else{
//        	jComboBoxTramo.setSelectedIndex(0);
//        }
        
        EstructuraDB eTramo = new EstructuraDB();
        if((jComboBoxTramo.getItemCount() > 0) && uc.getDatosEconomicos().getTramoPonencia()!= null && (uc.getDatosEconomicos().getTramoPonencia().getCodTramo()!=null)){            	
        	eTramo.setPatron(uc.getDatosEconomicos().getTramoPonencia().getCodTramo());
        	eTramo.setDescripcion(uc.getDatosEconomicos().getTramoPonencia().getDenominacion());
        	jComboBoxTramo.setSelectedItem(eTramo);
        }
        else{
        	jComboBoxTramo.setSelectedItem(eTramo);
        }
       
        //coeficientes correctores del valor del suelo
        if(uc.getDatosEconomicos().getNumFachadas()!=null)
        	jComboBoxNumeroFachadas.setSelectedPatron(EdicionUtils.getStringValue(uc.getDatosEconomicos().getNumFachadas()));
        
        jCheckBoxLongitudFachada.setSelected(uc.getDatosEconomicos().isCorrectorLongFachada()!=null?
                uc.getDatosEconomicos().isCorrectorLongFachada().booleanValue():false);
        
        //coeficientes correctores del valor de la construccion
        jTextFieldEstadoConservacion.setText(EdicionUtils.getStringValue(uc.getDatosEconomicos().getCorrectorConservacion()));
        
        //coeficientes correctores del valor del suelo y de las construcciones
        jTextFieldValorCCCargasSingulares.setText(EdicionUtils.getStringValue(uc.getDatosEconomicos().getCoefCargasSingulares()));
        jCheckBoxDepreciacion.setSelected(uc.getDatosEconomicos().isCorrectorDepreciacion()!=null?
                uc.getDatosEconomicos().isCorrectorDepreciacion().booleanValue():false);
        jCheckBoxSituacionesEspeciales.setSelected(uc.getDatosEconomicos().isCorrectorSitEspeciales()!=null?
                uc.getDatosEconomicos().isCorrectorSitEspeciales().booleanValue():false);
        jCheckBoxUsoNoLucrativo.setSelected(uc.getDatosEconomicos().isCorrectorNoLucrativo()!=null?
                uc.getDatosEconomicos().isCorrectorNoLucrativo().booleanValue():false);
        
        
        //Datos de la unidad constructiva
        jTextFieldPCatastral1.setText(uc.getRefParcela().getRefCatastral1());
        jTextFieldPCatastral2.setText(uc.getRefParcela().getRefCatastral2());
        jTextFieldCodigoUC.setText(uc.getCodUnidadConstructiva());
        jComboBoxClaseUC.setSelectedPatron(uc.getTipoUnidad());
        
    }
    
    public UnidadConstructivaCatastro getUnidadConstructiva ()
    {
        if(unidadCons!=null)
        {
            ReferenciaCatastral refParcela = new ReferenciaCatastral(jTextFieldPCatastral1.getText(), jTextFieldPCatastral2.getText());
            unidadCons.setRefParcela(refParcela);

            //datos localización

            if (jComboBoxTipoVia.getSelectedPatron() != null)
                unidadCons.getDirUnidadConstructiva().setTipoVia(jComboBoxTipoVia.getSelectedPatron());
            unidadCons.getDirUnidadConstructiva().setNombreVia(jTextFieldDireccion.getText());
            if (jTextFieldNumero.getText().length()>0)
                unidadCons.getDirUnidadConstructiva().setPrimerNumero(Integer.parseInt(jTextFieldNumero.getText()));
            else if (jTextFieldNumero.getText().length()<1)
            	unidadCons.getDirUnidadConstructiva().setPrimerNumero(-1);
            unidadCons.getDirUnidadConstructiva().setPrimeraLetra(jTextFieldLetra.getText());
            if (jTextFieldNumeroD.getText().length()>0)
                unidadCons.getDirUnidadConstructiva().setSegundoNumero(Integer.parseInt(jTextFieldNumeroD.getText()));
            else if (jTextFieldNumeroD.getText().length()<1)
            	unidadCons.getDirUnidadConstructiva().setSegundoNumero(-1);
            unidadCons.getDirUnidadConstructiva().setSegundaLetra(jTextFieldLetraD.getText());
            unidadCons.getDirUnidadConstructiva().setBloque(jTextFieldBloque.getText());
            unidadCons.getDirUnidadConstructiva().setDireccionNoEstructurada(jTextFieldDirNoEstructurada.getText());
            if (jTextFieldKm.getText().length()>0)
                unidadCons.getDirUnidadConstructiva().setKilometro(Double.parseDouble(jTextFieldKm.getText()));
            else if (jTextFieldKm.getText().length()<1)
            	unidadCons.getDirUnidadConstructiva().setKilometro(-1);

            try
            {
                DireccionLocalizacion dir = ConstantesRegistroExp.clienteCatastro.getViaPorNombre(unidadCons.getDirUnidadConstructiva().getNombreVia());
                if(dir!=null && dir.getNombreVia()!=null && !dir.getNombreVia().equalsIgnoreCase(""))
                {
                    if(!dir.getNombreVia().equalsIgnoreCase(unidadCons.getDirUnidadConstructiva().getNombreVia()))
                    {
                      unidadCons.getDirUnidadConstructiva().setNombreVia(dir.getNombreVia());
                    }
                    if (dir.getCodigoVia()==-1){
                    	unidadCons.getDirUnidadConstructiva().setCodigoVia(0);
                    }
                    else{
                    	unidadCons.getDirUnidadConstructiva().setCodigoVia(dir.getCodigoVia());
                    }
                    unidadCons.getDirUnidadConstructiva().setTipoVia(dir.getTipoVia());
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }


            //datos físicos
            if (jTextFieldAnioConstruccion.getText().length()>0)
                unidadCons.getDatosFisicos().setAnioConstruccion(Integer.valueOf(jTextFieldAnioConstruccion.getText()));

            if(jComboBoxAnioI.getSelectedItem()!=null)
                unidadCons.getDatosFisicos().setIndExactitud(jComboBoxAnioI.getSelectedItem().toString());

            if (jTextFieldSuperficieSuelo.getText().length()>0)
                unidadCons.getDatosFisicos().setSupOcupada(Long.valueOf(jTextFieldSuperficieSuelo.getText()));
            if (jTextFieldLongitudFachada.getText().length()>0)
                unidadCons.getDatosFisicos().setLongFachada(Float.valueOf(jTextFieldLongitudFachada.getText()));


            //datos económicos
            PonenciaZonaValor zonaValor = unidadCons.getDatosEconomicos().getZonaValor();
            if(zonaValor==null)
            {
                zonaValor = new PonenciaZonaValor();
            }
            zonaValor.setCodZonaValor(jComboBoxZonaValor.getSelectedItem().toString()!=null?
                    jComboBoxZonaValor.getSelectedItem().toString():"");

            unidadCons.getDatosEconomicos().setZonaValor(zonaValor);
            unidadCons.getDatosEconomicos().setCodViaPonencia(jTextFieldCodigoVia.getText());
            
            PonenciaTramos ponenciaTramos = unidadCons.getDatosEconomicos().getTramoPonencia();
            if(ponenciaTramos==null)
            {
                ponenciaTramos = new PonenciaTramos();
            }
            
            if(jComboBoxTramo.getSelectedItem()!=null){
            	ponenciaTramos.setCodTramo(((EstructuraDB)jComboBoxTramo.getSelectedItem()).getPatron());
            	ponenciaTramos.setDenominacion(((EstructuraDB)jComboBoxTramo.getSelectedItem()).getDescripcion());
            }
	        
//            ponenciaTramos.setCodTramo(jComboBoxTramo.getSelectedItem()!=null?
//                    jComboBoxTramo.getSelectedItem().toString():"");
            unidadCons.getDatosEconomicos().setTramoPonencia(ponenciaTramos);


            //coeficientes correctores del valor del suelo

            if(jComboBoxNumeroFachadas.getSelectedItem()!=null)
            	unidadCons.getDatosEconomicos().setNumFachadas(((ComboBoxEstructuras)jComboBoxNumeroFachadas).getSelectedPatron());
                //unidadCons.getDatosEconomicos().setNumFachadas(jComboBoxNumeroFachadas.getSelectedItem().toString());
            unidadCons.getDatosEconomicos().setCorrectorLongFachada(Boolean.valueOf(jCheckBoxLongitudFachada.isSelected()));

            //coeficientes correctores del valor de la construccion
            unidadCons.getDatosEconomicos().setCorrectorConservacion(jTextFieldEstadoConservacion.getText());

            //coeficientes correctores del valor del suelo y de las construcciones
            if (jTextFieldValorCCCargasSingulares.getText().length()>0)
                unidadCons.getDatosEconomicos().setCoefCargasSingulares(Float.valueOf(jTextFieldValorCCCargasSingulares.getText()));
            unidadCons.getDatosEconomicos().setCorrectorDepreciacion(Boolean.valueOf(jCheckBoxDepreciacion.isSelected()));
            unidadCons.getDatosEconomicos().setCorrectorSitEspeciales(Boolean.valueOf(jCheckBoxSituacionesEspeciales.isSelected()));
            unidadCons.getDatosEconomicos().setCorrectorNoLucrativo(Boolean.valueOf(jCheckBoxUsoNoLucrativo.isSelected()));


            //Datos de la unidad constructiva
            unidadCons.setIdUnidadConstructiva(EdicionUtils.paddingString(jTextFieldPCatastral1.getText(),EdicionUtils.TAM_REFCATASTRAL/2,'0', true)
                    + EdicionUtils.paddingString(jTextFieldPCatastral2.getText(),EdicionUtils.TAM_REFCATASTRAL/2,'0', true)
                    +jTextFieldCodigoUC.getText());

            unidadCons.setCodUnidadConstructiva(jTextFieldCodigoUC.getText());
            if (jComboBoxClaseUC.getSelectedItem() != null)
                unidadCons.setTipoUnidad(jComboBoxClaseUC.getSelectedPatron());
            
            if (unidadCons.getCodDelegacionMEH() == null || unidadCons.getCodDelegacionMEH().equals("")){
            	unidadCons.setCodDelegacionMEH(fincaActual.getCodDelegacionMEH());
            }
            if (unidadCons.getCodMunicipioDGC() == null || unidadCons.getCodMunicipioDGC().equals("")){
            	unidadCons.setCodMunicipioDGC(fincaActual.getCodMunicipioDGC());
            }
            
            if (unidadCons.getDatosEconomicos().getZonaValor().getCodZonaValor() != null &&
            		!unidadCons.getDatosEconomicos().getZonaValor().getCodZonaValor().equals("")){
            	
            	try {
            		
					PonenciaZonaValor ponenciaZonaValor = ConstantesRegistroExp.clienteCatastro.obtenerPonenciaZonaValor(unidadCons.getDatosEconomicos().getZonaValor().getCodZonaValor());
					unidadCons.getDatosEconomicos().setZonaValor(ponenciaZonaValor);
					
				} catch (Exception e) {					
					e.printStackTrace();
				}
            }
            
            if (unidadCons.getDatosEconomicos().getTramoPonencia().getCodTramo() != null &&
            		!unidadCons.getDatosEconomicos().getTramoPonencia().getCodTramo().equals("")){
            	
            	try {
            		
					PonenciaTramos tramos = ConstantesRegistroExp.clienteCatastro.obtenerPonenciaTramos(unidadCons.getDatosEconomicos().getTramoPonencia().getCodTramo());
					unidadCons.getDatosEconomicos().setTramoPonencia(tramos);
					
				} catch (Exception e) {					
					e.printStackTrace();
				}
            }
            
        }
        return unidadCons;
    }
    
    
    
    /**
     * This method initializes jPanelDatosUC	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanelDatosUC()
    {
        if (jPanelDatosUC == null)
        {
            jLabelClaseUC = new JLabel("", JLabel.CENTER); 
            jLabelClaseUC.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "unidadconstructiva.panel.datosuc.claseuc"))); 
            jLabelCodigoUC = new JLabel("", JLabel.CENTER); 
            jLabelCodigoUC.setText(
            		UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "unidadconstructiva.panel.datosuc.codigouc"))); 
            
            
            jLabelPCatastral1 = new JLabel("", JLabel.CENTER); 
            jLabelPCatastral1.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "unidadconstructiva.panel.datosuc.catastral1"))); 
            jLabelPCatastral2 = new JLabel("", JLabel.CENTER); 
            jLabelPCatastral2.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "unidadconstructiva.panel.datosuc.catastral2"))); 
            
            GridBagLayout layout = new GridBagLayout();
            jPanelDatosUC = new JPanel(layout);
            
            jPanelDatosUC.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("Expedientes", "unidadconstructiva.panel.datosuc.titulo"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
            
            jPanelDatosUC.add(jLabelPCatastral1, new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosUC.add(jLabelPCatastral2, new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosUC.add(getJTextFieldPCatastral1(),new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosUC.add(getJTextFieldPCatastral2(),new GridBagConstraints(1, 1, 1, 1, 0, 0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            
            
            jPanelDatosUC.add(jLabelCodigoUC, new GridBagConstraints(2, 0, 1, 1, 0, 0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosUC.add(jLabelClaseUC,new GridBagConstraints(3, 0, 1, 1, 0, 0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosUC.add(getJTextFieldCodigoUC(),new GridBagConstraints(2, 1, 1, 1, 0, 0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosUC.add(getJComboBoxClaseUC(),new GridBagConstraints(3, 1, 1, 1, 0, 0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            
        }
        return jPanelDatosUC;
    }
    
    /**
     * This method initializes jPanelDatosLocalizacion	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanelDatosLocalizacion()
    {
        if (jPanelDatosLocalizacion == null)
        {
           
            jLabelKm = new JLabel("", JLabel.CENTER); 
            jLabelKm.setText(I18N.get("Expedientes", "unidadconstructiva.panel.datoslocalizacion.km"));           
            jLabelDireccionNoEstructurada = new JLabel("", JLabel.CENTER); 
            jLabelDireccionNoEstructurada.setText(I18N.get("Expedientes", "unidadconstructiva.panel.datoslocalizacion.dirnoestructurada"));             
            jLabelBloque = new JLabel("", JLabel.CENTER); 
            jLabelBloque.setText(I18N.get("Expedientes", "unidadconstructiva.panel.datoslocalizacion.bloque"));
            jLabelLetraD = new JLabel("", JLabel.CENTER); 
            jLabelLetraD.setText(I18N.get("Expedientes", "unidadconstructiva.panel.datoslocalizacion.letraD"));           
            jLabelNumeroD = new JLabel("", JLabel.CENTER); 
            jLabelNumeroD.setText(I18N.get("Expedientes", "unidadconstructiva.panel.datoslocalizacion.numeroD"));            
            jLabelLetra = new JLabel("", JLabel.CENTER); 
            jLabelLetra.setText(I18N.get("Expedientes", "unidadconstructiva.panel.datoslocalizacion.letra"));            
            jLabelNumero = new JLabel("", JLabel.CENTER); 
            jLabelNumero.setText(I18N.get("Expedientes", "unidadconstructiva.panel.datoslocalizacion.numero")); 
            jLabelDireccion = new JLabel("", JLabel.CENTER); 
            jLabelDireccion.setText(I18N.get("Expedientes", "unidadconstructiva.panel.datoslocalizacion.direccion"));             
            jLabelVia = new JLabel("", JLabel.CENTER); 
            jLabelVia.setText(I18N.get("Expedientes", "unidadconstructiva.panel.datoslocalizacion.via")); 
            
            jPanelDatosLocalizacion = new JPanel(new GridBagLayout());
            jPanelDatosLocalizacion.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("Expedientes", "unidadconstructiva.panel.datoslocalizacion.titulo"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
           
            jPanelDatosLocalizacion.add(jLabelVia, new GridBagConstraints(0, 2, 2, 1, 0.1,0.1, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosLocalizacion.add(jLabelDireccion, new GridBagConstraints(2, 2, 7, 1, 0.1,0.1, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosLocalizacion.add(getJButtonBuscarDireccion(), new GridBagConstraints(9, 3, 1, 1, 0.1,0.10, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosLocalizacion.add(getJComboBoxTipoVia(), new GridBagConstraints(0, 3, 2, 1, 0.1,0.1, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosLocalizacion.add(getJTextFieldDireccion(), new GridBagConstraints(2, 3, 7, 1, 0.1,0.1, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosLocalizacion.add(jLabelNumero, new GridBagConstraints(0, 4, 1, 1, 0.1,0.1, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosLocalizacion.add(jLabelLetra, new GridBagConstraints(1, 4, 1, 1, 0.1,0.1, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosLocalizacion.add(jLabelNumeroD, new GridBagConstraints(2, 4, 1, 1,0.1,0.1, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosLocalizacion.add(jLabelLetraD, new GridBagConstraints(3, 4, 1, 1, 0.1,0.1, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosLocalizacion.add(jLabelBloque, new GridBagConstraints(4, 4, 1, 1, 0.1,0.1, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosLocalizacion.add(jLabelDireccionNoEstructurada, new GridBagConstraints(5, 4, 4, 1, 0.1,0.1, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosLocalizacion.add(jLabelKm, new GridBagConstraints(9, 4, 1, 1, 0.1,0.1, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosLocalizacion.add(getJTextFieldNumero(), new GridBagConstraints(0, 5, 1, 1, 0.1,0.1, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosLocalizacion.add(getJTextFieldLetra(), new GridBagConstraints(1, 5, 1, 1, 0.1,0.1, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosLocalizacion.add(getJTextFieldNumeroD(), new GridBagConstraints(2, 5, 1, 1,0.1,0.1, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosLocalizacion.add(getJTextFieldLetraD(), new GridBagConstraints(3, 5, 1, 1, 0.1,0.1, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosLocalizacion.add(getJTextFieldBloque(), new GridBagConstraints(4, 5, 1, 1, 0.1,0.1, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosLocalizacion.add(getJTextFieldDirNoEstructurada(), new GridBagConstraints(5, 5, 4, 1, 0.1,0.1, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 250,0));
            jPanelDatosLocalizacion.add(getJTextFieldKm(), new GridBagConstraints(9, 5, 1, 1, 0.1,0.1, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            
            jLabelBloque.setVisible(false);
            jTextFieldBloque.setVisible(false);
            
        }
        return jPanelDatosLocalizacion;
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
            jLabelLongitudFachada = new JLabel("", JLabel.CENTER); 
            jLabelLongitudFachada.setText(I18N.get("Expedientes", "unidadconstructiva.panel.datosfisicos.longfachada")); 
            jLabelSuperficieSuelo = new JLabel("", JLabel.CENTER); 
            jLabelSuperficieSuelo.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "unidadconstructiva.panel.datosfisicos.supsuelo"))); 
            jLabelAnioI = new JLabel("", JLabel.CENTER); 
            jLabelAnioI.setText(I18N.get("Expedientes", "unidadconstructiva.panel.datosfisicos.anioc")); 
            jLabelAnioConstruccion = new JLabel("", JLabel.CENTER); 
            jLabelAnioConstruccion.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "unidadconstructiva.panel.datosfisicos.anioconstruccion"))); 
            jPanelDatosFisicos = new JPanel(new GridBagLayout());
            jPanelDatosFisicos.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("Expedientes", "unidadconstructiva.panel.datosfisicos.titulo"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
            jPanelDatosFisicos.add(jLabelAnioConstruccion, new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosFisicos.add(jLabelAnioI, new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosFisicos.add(jLabelSuperficieSuelo, new GridBagConstraints(2, 0, 1, 1, 0, 0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosFisicos.add(jLabelLongitudFachada, new GridBagConstraints(3, 0, 1, 1, 0, 0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosFisicos.add(getJTextFieldAnioConstruccion(), new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            //jPanelDatosFisicos.add(getJTextFieldAnioI(), new GridBagConstraints(1, 1, 1, 1, 0, 0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosFisicos.add(getJComboBoxAnioI(), new GridBagConstraints(1, 1, 1, 1, 0, 0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosFisicos.add(getJTextFieldSuperficieSuelo(), new GridBagConstraints(2, 1, 1, 1, 0, 0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosFisicos.add(getJTextFieldLongitudFachada(), new GridBagConstraints(3, 1, 1, 1, 0, 0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            
        }
        return jPanelDatosFisicos;
    }
    
    /**
     * This method initializes jPanelDatosEconomicos	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanelDatosEconomicos()
    {
        if (jPanelDatosEconomicos == null)
        {
            jPanelDatosEconomicos = new JPanel(new GridBagLayout());
            jPanelDatosEconomicos.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("Expedientes", "unidadconstructiva.panel.datoseconomicos.titulo"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
            jPanelDatosEconomicos.add(getJPanelDatosPonencia(), 
                    new GridBagConstraints(0, 0, 2, 1, 0.1, 0.1, GridBagConstraints.WEST,GridBagConstraints.BOTH, new Insets (0,5,0,5), 0,0));
            jPanelDatosEconomicos.add(getJPanelCCSuelo(), 
                    new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.WEST,GridBagConstraints.BOTH, new Insets (0,5,0,5), 0,0));
            jPanelDatosEconomicos.add(getJPanelCCConstruccion(), 
                    new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1, GridBagConstraints.WEST,GridBagConstraints.BOTH, new Insets (0,5,0,5), 0,0));
            jPanelDatosEconomicos.add(getJPanelCCSueloYConstruccion(), 
                    new GridBagConstraints(1, 1, 1, 2, 0.1, 0.1, GridBagConstraints.EAST,GridBagConstraints.BOTH, new Insets (0,5,0,5), 0,0));
            
        }
        return jPanelDatosEconomicos;
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
            jTextFieldPCatastral1.setEnabled(false);
            
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
            jTextFieldPCatastral2.setEnabled(false);
                        
        }
        return jTextFieldPCatastral2;
    }
    
    /**
     * This method initializes jTextFieldCodigoUC	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getJTextFieldCodigoUC()
    {
        if (jTextFieldCodigoUC == null)
        {
            jTextFieldCodigoUC = new TextField(4);
            jTextFieldCodigoUC.addCaretListener(new CaretListener()
                    {
                public void caretUpdate(CaretEvent evt)
                {
                    EdicionUtils.chequeaLongYNumCampoEdit(jTextFieldCodigoUC,4, aplicacion.getMainFrame());
                }
                    });
                       
        }
        return jTextFieldCodigoUC;
    }
        
    /**
     * This method initializes jComboBoxClaseUC	
     * 	
     * @return javax.swing.JComboBox	
     */
    private ComboBoxEstructuras getJComboBoxClaseUC()
    {
        if (jComboBoxClaseUC == null)
        {
            Estructuras.cargarEstructura("Clase de Unidad Constructiva");
            jComboBoxClaseUC = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), true);
            
        }
        return jComboBoxClaseUC;
    }
    
    
    /**
     * This method initializes jButtonBuscar	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getJButtonBuscarDireccion()
    {
        if (jButtonBuscar == null)
        {
            jButtonBuscar = new JButton();
            jButtonBuscar.setIcon(IconLoader.icon(GestionExpedientePanel.ICONO_BUSCAR));
            jButtonBuscar.addActionListener(new java.awt.event.ActionListener()
                    {
                public void actionPerformed(java.awt.event.ActionEvent e)
                {
                	String tipoVia = "";
                    if(jComboBoxTipoVia.getSelectedPatron()!=null){
                    	tipoVia = jComboBoxTipoVia.getSelectedPatron().toString();
                    }                	
                    ViasSistemaDialog dialog = new ViasSistemaDialog(jTextFieldDireccion.getText(),tipoVia);
                    //ViasSistemaDialog dialog = new ViasSistemaDialog(jTextFieldDireccion.getText());
                    dialog.setVisible(true);        
                    
                    String nombreVia = dialog.getVia();
                    
                    jTextFieldDireccion.setText(nombreVia);
                }
                    });
            jButtonBuscar.setName("_buscarvia");
        }
        return jButtonBuscar;
    }
    
    private JButton getJButtonBuscarCodigoVia()
    {
        if (jButtonBuscarCodigoVia  == null)
        {
        	jButtonBuscarCodigoVia = new JButton();
        	jButtonBuscarCodigoVia.setIcon(IconLoader.icon(GestionExpedientePanel.ICONO_BUSCAR));
        	jButtonBuscarCodigoVia.addActionListener(new java.awt.event.ActionListener()
                    {
                public void actionPerformed(java.awt.event.ActionEvent e)
                {                	          	
                    CodigoViasSistemaDialog dialog = new CodigoViasSistemaDialog();                    
                    dialog.setVisible(true);        
                    
                    String codigoVia = dialog.getCodigoVia();                    
                    jTextFieldCodigoVia.setText(codigoVia);
                }
                    });
        	jButtonBuscar.setName("_buscarcodigovia");
        }
        return jButtonBuscarCodigoVia;
    }
    
    /**
     * This method initializes jComboBoxTipoVia	
     * 	
     * @return javax.swing.JComboBox	
     */
    private ComboBoxEstructuras getJComboBoxTipoVia()
    {
        if (jComboBoxTipoVia == null)
        {
            Estructuras.cargarEstructura("Tipo de vía");
            jComboBoxTipoVia = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), true);
            
        }
        jComboBoxTipoVia.addActionListener(new ListenerCombo());
        return jComboBoxTipoVia;
    }
    
    /**
     * This method initializes jTextFieldDireccion	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getJTextFieldDireccion()
    {
        if (jTextFieldDireccion == null)
        {
            jTextFieldDireccion = new TextField(25);
            
        }
        jTextFieldDireccion.getDocument().addDocumentListener(new ListenerTextField());  
        return jTextFieldDireccion;
    }
    
    /**
     * This method initializes jTextFieldNumero	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getJTextFieldNumero()
    {
        if (jTextFieldNumero == null)
        {
            jTextFieldNumero = new TextField(4);
            jTextFieldNumero.addCaretListener(new CaretListener()
                    {
                public void caretUpdate(CaretEvent evt)
                {
                    EdicionUtils.chequeaLongYNumCampoEdit(jTextFieldNumero,4, aplicacion.getMainFrame());
                }
                    });
            
        }
        jTextFieldNumero.getDocument().addDocumentListener(new ListenerTextField());  
        return jTextFieldNumero;
    }
    
    /**
     * This method initializes jTextFieldLetra	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getJTextFieldLetra()
    {
        if (jTextFieldLetra == null)
        {
            jTextFieldLetra = new TextField(1);
                        
        }
        jTextFieldLetra.getDocument().addDocumentListener(new ListenerTextField()); 
        return jTextFieldLetra;
    }
    
    /**
     * This method initializes jTextFieldNumerod	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getJTextFieldNumeroD()
    {
        if (jTextFieldNumeroD == null)
        {
            jTextFieldNumeroD = new TextField(4);
            jTextFieldNumeroD.addCaretListener(new CaretListener()
                    {
                public void caretUpdate(CaretEvent evt)
                {
                    EdicionUtils.chequeaLongYNumCampoEdit(jTextFieldNumeroD,4, aplicacion.getMainFrame());
                }
                    });
                       
        }
        jTextFieldNumeroD.getDocument().addDocumentListener(new ListenerTextField());  
        return jTextFieldNumeroD;
    }
    
    /**
     * This method initializes jTextFieldLetraD	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getJTextFieldLetraD()
    {
        if (jTextFieldLetraD == null)
        {
            jTextFieldLetraD = new TextField(1);
            
        }
        jTextFieldLetraD.getDocument().addDocumentListener(new ListenerTextField()); 
        return jTextFieldLetraD;
    }
    
    /**
     * This method initializes jTextFieldBloque	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getJTextFieldBloque()
    {
        if (jTextFieldBloque == null)
        {
            jTextFieldBloque = new TextField(4);
            
        }
        return jTextFieldBloque;
    }
    
    /**
     * This method initializes jTextFieldDirNoEstructurada	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getJTextFieldDirNoEstructurada()
    {
        if (jTextFieldDirNoEstructurada == null)
        {
            jTextFieldDirNoEstructurada = new TextField(25);
            
        }
        jTextFieldDirNoEstructurada.getDocument().addDocumentListener(new ListenerTextField());  
        return jTextFieldDirNoEstructurada;
    }
    
    /**
     * This method initializes jTextFieldKm	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getJTextFieldKm()
    {
        if (jTextFieldKm == null)
        {
            jTextFieldKm = new TextField(6);
            jTextFieldKm.addCaretListener(new CaretListener()
                    {
                public void caretUpdate(CaretEvent evt)
                {
                    EdicionUtils.chequeaLongYDecimalCampoEdit(jTextFieldKm,6, aplicacion.getMainFrame());
                }
                    });
                       
        }
        jTextFieldKm.getDocument().addDocumentListener(new ListenerTextField());  
        return jTextFieldKm;
    }
    
    /**
     * This method initializes jTextFieldAnioConstruccion	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getJTextFieldAnioConstruccion()
    {
        if (jTextFieldAnioConstruccion == null)
        {
            jTextFieldAnioConstruccion = new TextField(4);
            jTextFieldAnioConstruccion.addCaretListener(new CaretListener()
                    {
                public void caretUpdate(CaretEvent evt)
                {
                    EdicionUtils.chequeaLongYNumCampoEdit(jTextFieldAnioConstruccion,4, aplicacion.getMainFrame());
                }
                    });
                        
        }
        return jTextFieldAnioConstruccion;
    }
    
    /**
     * This method initializes jTextFieldAnioI	
     * 	
     * @return javax.swing.JTextField	
     */
    
    private JComboBox getJComboBoxAnioI()
    {
        //Indicador de exactitud del año de construccion
        if (jComboBoxAnioI  == null)
        {
        	Estructuras.cargarEstructura("Indicador Año Construcción");
        	jComboBoxAnioI = new ComboBoxEstructuras(Estructuras.getListaTipos(),
    				null, AppContext.getApplicationContext().getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), true);
        	    		    		
        }
        return jComboBoxAnioI;
    }
    
    /**
     * This method initializes jTextFieldSuperficieSuelo	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getJTextFieldSuperficieSuelo()
    {
        if (jTextFieldSuperficieSuelo == null)
        {
            jTextFieldSuperficieSuelo = new TextField(7);
            jTextFieldSuperficieSuelo.addCaretListener(new CaretListener()
                    {
                public void caretUpdate(CaretEvent evt)
                {
                    EdicionUtils.chequeaLongYNumCampoEdit(jTextFieldSuperficieSuelo,7, aplicacion.getMainFrame());
                }
                    });
            
        }
        return jTextFieldSuperficieSuelo;
    }
    
    /**
     * This method initializes jTextFieldLongitudFachada	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getJTextFieldLongitudFachada()
    {
        if (jTextFieldLongitudFachada == null)
        {
            jTextFieldLongitudFachada = new TextField(5);
            jTextFieldLongitudFachada.addCaretListener(new CaretListener()
                    {
                public void caretUpdate(CaretEvent evt)
                {
                    EdicionUtils.chequeaLongYNumCampoEdit(jTextFieldLongitudFachada,5, aplicacion.getMainFrame());
                }
                    });
           
        }
        return jTextFieldLongitudFachada;
    }
    
    /**
     * This method initializes jPanelDatosPonencia	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanelDatosPonencia()
    {
        if (jPanelDatosPonencia == null)
        {   
            jPanelDatosPonencia = new JPanel(new GridBagLayout());
            jPanelDatosPonencia.setBorder(BorderFactory.createTitledBorder(
                    I18N.get("Expedientes", "unidadconstructiva.panel.datoseconomicos.datosponencia.titulo"))); 
                        
            jLabelZonaValor = new JLabel(
                    I18N.get("Expedientes", "unidadconstructiva.panel.datoseconomicos.datosponencia.zonavalor"), JLabel.CENTER); 
            jPanelDatosPonencia.add(jLabelZonaValor, 
                    new GridBagConstraints(0, 0, 2, 1, 0.1,0.1, GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL, new Insets (0,25,0,5), 0,0));
            
            jPanelDatosPonencia.add(getJComboBoxZonaValor(), 
                    new GridBagConstraints(0, 1, 2, 1, 0.1,0.1, GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL, new Insets (0,25,0,5), 0,0));
            jLabelCodigoVia = new JLabel(
                    I18N.get("Expedientes", "unidadconstructiva.panel.datoseconomicos.datosponencia.codigovia"), JLabel.CENTER); 
            jLabelTramo = new JLabel(
                    I18N.get("Expedientes", "unidadconstructiva.panel.datoseconomicos.datosponencia.tramo"), JLabel.CENTER); 
            jPanelDatosPonencia.add(jLabelCodigoVia, 
                    new GridBagConstraints(4, 0, 2, 1, 0.1,0.1, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,25,0,5), 0,0));
            jPanelDatosPonencia.add(jLabelTramo, 
                    new GridBagConstraints(9, 0, 2, 1, 0.1,0.1, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            
            jPanelDatosPonencia.add(getJTextFieldCodigoVia(), 
                    new GridBagConstraints(4, 1, 2, 1, 0.1,0.1, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,25,0,5), 0,0));
            jPanelDatosPonencia.add(getJButtonBuscarCodigoVia(), 
                    new GridBagConstraints(6, 1, 1, 1, 0.1,0.1, GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets (0,5,0,5), 0,0));
            jPanelDatosPonencia.add(getJComboBoxTramo(), 
                    new GridBagConstraints(9, 1, 2, 1, 0.1,0.1, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            
            EdicionUtils.crearMallaPanel(2, 11, jPanelDatosPonencia, 0.1, 0.1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0);
             
        }
        return jPanelDatosPonencia;
    }
      
    /**
     * This method initializes jTextFieldZonaValor   
     *  
     * @return javax.swing.JTextField   
     */
    
    private JComboBox getJComboBoxZonaValor()
    { 
        if (jComboBoxZonaValor  == null)
        {
        	jComboBoxZonaValor = new JComboBox(); 
        	
        }
        
        jComboBoxZonaValor.addActionListener(new ListenerCombo());
        return jComboBoxZonaValor;        
    }
           
    /**
     * This method initializes jTextFieldCodigoVia   
     *  
     * @return javax.swing.JTextField   
     */
    private JTextField getJTextFieldCodigoVia()
    { 
        if (jTextFieldCodigoVia == null)
        {
            jTextFieldCodigoVia = new TextField(5);   
            
        }
        jTextFieldCodigoVia.getDocument().addDocumentListener(new ListenerTextField()); 
        return jTextFieldCodigoVia;        
    }
    /**
     * This method initializes jTextFieldTramo   
     *  
     * @return javax.swing.JTextField   
     */
   
    private JComboBox getJComboBoxTramo()
    { 
        if (jComboBoxTramo  == null)
        {
        	jComboBoxTramo = new JComboBox();   
        	jComboBoxTramo.setRenderer(new EstructuraDBListCellRenderer());
        	jComboBoxTramo.setKeySelectionManager(new ComboBoxKeyTipoDestinoSelectionManager());
        	
        }
        
        jComboBoxTramo.addActionListener(new ListenerCombo());
        return jComboBoxTramo;        
    }
    
    /**
     * This method initializes jPanelCCSuelo	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanelCCSuelo()
    {
        if (jPanelCCSuelo == null)
        {   
            jPanelCCSuelo = new JPanel(new GridBagLayout());
            jPanelCCSuelo.setBorder(BorderFactory.createTitledBorder(
                    I18N.get("Expedientes", "unidadconstructiva.panel.datoseconomicos.coefcorrectores.titulo"))); 
            
            JLabel jLabelNumeroFachadas = new JLabel(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "unidadconstructiva.panel.datoseconomicos.coefcorrectores.numfachadas")), JLabel.CENTER); 
            jPanelCCSuelo.add(jLabelNumeroFachadas,
                    new GridBagConstraints(0, 0, 1, 1, 0.1,0.1, GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL, new Insets (3,5,0,5), 0,0));
           
            jPanelCCSuelo.add(getJComboBoxNumeroFachadas(),
                    new GridBagConstraints(1, 0, 1, 1, 0.1,0.1, GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL, new Insets (3,5,0,5), 0,0));
            jPanelCCSuelo.add(getJCheckBoxLongitudFachada(),
                    new GridBagConstraints(0, 1, 2, 1, 0.1,0.1, GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            
        }
        return jPanelCCSuelo;
    }
    
    
    /**
     * This method initializes getJTextNumeroFachadas  
     *  
     * @return javax.swing.JTextField   
     */
   
    private JComboBox getJComboBoxNumeroFachadas()
    {
        if (jComboBoxNumeroFachadas  == null)
        {
        	Estructuras.cargarEstructura("Numero Fachadas");
        	jComboBoxNumeroFachadas = new ComboBoxEstructuras(Estructuras.getListaTipos(),
    				null, AppContext.getApplicationContext().getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), true);
        	    		    		
        }
        return jComboBoxNumeroFachadas;
    }
    
    /**
     * This method initializes jCheckBoxLongitudFachada  
     *  
     * @return javax.swing.JTextField   
     */
    private JCheckBox getJCheckBoxLongitudFachada()
    {
        if (jCheckBoxLongitudFachada == null)
        {
            jCheckBoxLongitudFachada = new JCheckBox(
                    I18N.get("Expedientes", "unidadconstructiva.panel.datoseconomicos.coefcorrectores.longfachada"));            
        }
        return jCheckBoxLongitudFachada;
    }
    /**
     * This method initializes jPanelCCConstruccion	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanelCCConstruccion()
    {
        if (jPanelCCConstruccion == null)
        {
            jLabelCodEstadoConservacion = new JLabel("", JLabel.CENTER); 
            jLabelCodEstadoConservacion.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "unidadconstructiva.panel.datoseconomicos.estadoconservacion.titulo"))); 
            jPanelCCConstruccion = new JPanel(new GridBagLayout());
            jPanelCCConstruccion.setBorder(BorderFactory.createTitledBorder(I18N.get("Expedientes", "unidadconstructiva.panel.datoseconomicos.estadoconservacion.valorconstruccion"))); 
            jPanelCCConstruccion.add(jLabelCodEstadoConservacion, 
                    new GridBagConstraints(0, 0, 1, 1, 0.1,0.1, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelCCConstruccion.add(getJTextFieldEstadoConservacion(), 
                    new GridBagConstraints(1, 0, 1, 1, 0.5,0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 30,0));
        }
        return jPanelCCConstruccion;
    }
    
    /**
     * This method initializes jPanelCCSueloYConstruccion	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanelCCSueloYConstruccion()
    {
        if (jPanelCCSueloYConstruccion == null)
        {
            jLabelValorCCCargasSingulares = new JLabel("", JLabel.CENTER); 
            jLabelValorCCCargasSingulares.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "unidadconstructiva.panel.datoseconomicos.sueloyconstrucciones.valorcc"))); 
            jPanelCCSueloYConstruccion = new JPanel(new GridBagLayout());
            jPanelCCSueloYConstruccion.setBorder(BorderFactory.createTitledBorder(I18N.get("Expedientes", "unidadconstructiva.panel.datoseconomicos.sueloyconstrucciones.titulo"))); 
            jPanelCCSueloYConstruccion.add(jLabelValorCCCargasSingulares, 
                    new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelCCSueloYConstruccion.add(getJTextFieldValorCCCargasSingulares(), 
                    new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelCCSueloYConstruccion.add(getJCheckBoxDepreciacion(), 
                    new GridBagConstraints(0, 1, 2, 1, 0, 0, GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelCCSueloYConstruccion.add(getJCheckBoxSituacionesEspeciales(),
                    new GridBagConstraints(0, 2, 2, 1, 0, 0, GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelCCSueloYConstruccion.add(getJCheckBoxUsoNoLucrativo(),
                    new GridBagConstraints(0, 3, 2, 1, 0, 0, GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
        }
        return jPanelCCSueloYConstruccion;
    }
    
    
    /**
     * This method initializes jTextFieldEstadoConservacion	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getJTextFieldEstadoConservacion()
    {
        if (jTextFieldEstadoConservacion == null)
        {
            jTextFieldEstadoConservacion = new TextField(4);
            jTextFieldEstadoConservacion.setPreferredSize(new Dimension(60,20));
/*            jTextFieldEstadoConservacion.addCaretListener(new CaretListener()
//            {
//        public void caretUpdate(CaretEvent evt)
//        {
//            EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldEstadoConservacion,1, aplicacion.getMainFrame());
//        }
//            });
//            
*/        }
        return jTextFieldEstadoConservacion;
    }
    
    /**
     * This method initializes jTextFieldValorCCCargasSingulares	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getJTextFieldValorCCCargasSingulares()
    {
        if (jTextFieldValorCCCargasSingulares == null)
        {
            //0.50-1.80
            jTextFieldValorCCCargasSingulares = new TextField(4);
            jTextFieldValorCCCargasSingulares.addCaretListener(new CaretListener()
                    {
                public void caretUpdate(CaretEvent evt)
                {
                    EdicionUtils.chequeaLongYDecimalCampoEdit(jTextFieldValorCCCargasSingulares,4, aplicacion.getMainFrame());
                }
                    });
                       
        }
        return jTextFieldValorCCCargasSingulares;
    }
    
    /**
     * This method initializes jCheckBoxDepreciacion	
     * 	
     * @return javax.swing.JCheckBox	
     */
    private JCheckBox getJCheckBoxDepreciacion()
    {
        if (jCheckBoxDepreciacion == null)
        {
            jCheckBoxDepreciacion = new JCheckBox();
            jCheckBoxDepreciacion.setText(I18N.get("Expedientes", "unidadconstructiva.panel.datoseconomicos.sueloyconstrucciones.depreciacion"));
            
        }
        return jCheckBoxDepreciacion;
    }
    
    /**
     * This method initializes jCheckBoxSituacionesEspeciales	
     * 	
     * @return javax.swing.JCheckBox	
     */
    private JCheckBox getJCheckBoxSituacionesEspeciales()
    {
        if (jCheckBoxSituacionesEspeciales == null)
        {
            jCheckBoxSituacionesEspeciales = new JCheckBox();
            jCheckBoxSituacionesEspeciales.setText(
                    I18N.get("Expedientes", "unidadconstructiva.panel.datoseconomicos.sueloyconstrucciones.caracterextrinseco"));
                        
        }
        return jCheckBoxSituacionesEspeciales;
    }
    
    /**
     * This method initializes jCheckBoxUsoNoLucrativo	
     * 	
     * @return javax.swing.JCheckBox	
     */
    private JCheckBox getJCheckBoxUsoNoLucrativo()
    {
        if (jCheckBoxUsoNoLucrativo == null)
        {
            jCheckBoxUsoNoLucrativo = new JCheckBox();
            jCheckBoxUsoNoLucrativo.setText(
                    I18N.get("Expedientes", "unidadconstructiva.panel.datoseconomicos.sueloyconstrucciones.nolucrativo"));
            
        }
        return jCheckBoxUsoNoLucrativo;
    }

	public void enter() {
		
		if (this.gestionExpedientePanel != null){
			this.gestionExpedientePanel.getJButtonValidar().setEnabled(true);
			
			if(this.gestionExpedientePanel.getExpediente().getIdEstado() >= ConstantesRegistro.ESTADO_FINALIZADO)
            {
                EdicionUtils.enablePanel(this.gestionExpedientePanel.getJPanelBotones(), false);   
                this.gestionExpedientePanel.getJButtonEstadoModificado().setEnabled(true);
            }
    		else{
    			EdicionUtils.enablePanel(this.gestionExpedientePanel.getJPanelBotones(), true);
    			this.gestionExpedientePanel.getJButtonEstadoModificado().setEnabled(false);
    		}
		}
		
	}

	public void exit() {
		// TODO Auto-generated method stub
		
	}
	
	private JPanel getJPanelBotonera()
    {
        if (jPanelBotonera  == null)
        {
        	jPanelBotonera = new JPanel(new GridBagLayout());
                    	
        	jPanelBotonera.add(getJButtonAniadirUC(), 
                    new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 3, 0, 0), 0, 0));
            
        	jPanelBotonera.add(getJButtonNuevaUC(), 
                    new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 3, 0, 0), 0, 0));
            
        	jPanelBotonera.add(getJButtonEliminarUC(), 
                    new GridBagConstraints(4, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 3, 0, 0), 0, 0));
            
        	jPanelBotonera.add(new JPanel(),
                    new GridBagConstraints(0, 0, 1, 1, 1, 1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 0, 5, 0), 0, 0));        
        }
        return jPanelBotonera;
    }
    
	private JButton getJButtonEliminarUC()
    {
        if (jButtonEliminarUC  == null)
        {
        	jButtonEliminarUC = new JButton();
        	jButtonEliminarUC.setText(I18N.get("Expedientes", "unidadconstructiva.panel.boton.eliminarunidadconstructiva"));
        	
        	jButtonEliminarUC.addActionListener(new java.awt.event.ActionListener()
            {
            	public void actionPerformed(java.awt.event.ActionEvent e)
            	{
                    ArrayList lstUC = fincaActual.getLstUnidadesConstructivas();
                    if(lstUC!=null && lstUC.size()>0)
                    {
                        if (JOptionPane.showConfirmDialog(
                                (Component) UnidadConstructivaPanel.this,
                                I18N.get("Expedientes","unidadconstructiva.panel.removeucquestion"),
                                I18N.get("Expedientes","unidadconstructiva.panel.removeuc"),
                                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                        {
                            eliminaUC();
                        }
                    }
                }
            });
        }
        return jButtonEliminarUC;
    }
	
	private JButton getJButtonNuevaUC()
    {
        if (jButtonNuevaUC   == null)
        {
        	jButtonNuevaUC = new JButton();
        	jButtonNuevaUC.setText(I18N.get("Expedientes", "unidadconstructiva.panel.boton.nuevaunidadconstructiva"));
        	
        	jButtonNuevaUC.addActionListener(new java.awt.event.ActionListener()
            {
            	public void actionPerformed(java.awt.event.ActionEvent e)
            	{
                    EdicionUtils.clearPanel(UnidadConstructivaPanel.this);

                    getUnidadConstructiva();
                    unidadCons = new UnidadConstructivaCatastro();
                    DireccionLocalizacion dirUC = new DireccionLocalizacion();
                    dirUC.setNombreMunicipio(fincaActual.getDirParcela().getNombreMunicipio());
                    dirUC.setNombreProvincia(fincaActual.getDirParcela().getNombreProvincia());
                    dirUC.setCodigoMunicipioDGC(fincaActual.getDirParcela().getCodigoMunicipioDGC());
                    dirUC.setMunicipioINE(fincaActual.getDirParcela().getMunicipioINE());
                    dirUC.setProvinciaINE(fincaActual.getDirParcela().getProvinciaINE());
                    unidadCons.setDirUnidadConstructiva(dirUC);
                  
                    unidadCons.setCodDelegacionMEH(fincaActual.getCodDelegacionMEH());
                    unidadCons.setCodMunicipioDGC(fincaActual.getCodMunicipioDGC());
                    
                    UnidadConstructivaPanel.this.getJTextFieldPCatastral1().setText(fincaActual.getRefFinca().getRefCatastral1());
            		UnidadConstructivaPanel.this.getJTextFieldPCatastral2().setText(fincaActual.getRefFinca().getRefCatastral2());
                    UnidadConstructivaPanel.this.getJTextFieldPCatastral1().setEnabled(false);
            		UnidadConstructivaPanel.this.getJTextFieldPCatastral2().setEnabled(false);
                 
                    UnidadConstructivaPanel.this.getJTextFieldDireccion().setText(fincaActual.getDirParcela().getNombreVia());
                    UnidadConstructivaPanel.this.getJTextFieldDirNoEstructurada().setText(fincaActual.getDirParcela().getDireccionNoEstructurada());
                    UnidadConstructivaPanel.this.getJComboBoxTipoVia().setSelectedPatron(fincaActual.getDirParcela().getTipoVia());
                    if(fincaActual.getDirParcela().getKilometro() > 0)
                        UnidadConstructivaPanel.this.getJTextFieldKm().setText(String.valueOf(fincaActual.getDirParcela().getKilometro()));

                    UnidadConstructivaPanel.this.getJTextFieldLetra().setText(fincaActual.getDirParcela().getPrimeraLetra());
                    UnidadConstructivaPanel.this.getJTextFieldLetraD().setText(fincaActual.getDirParcela().getSegundaLetra());
                    if(fincaActual.getDirParcela().getPrimerNumero() > 0)
                        UnidadConstructivaPanel.this.getJTextFieldNumero().setText(String.valueOf(fincaActual.getDirParcela().getPrimerNumero()));

                    if(fincaActual.getDirParcela().getSegundoNumero() > 0)
                        UnidadConstructivaPanel.this.getJTextFieldNumeroD().setText(String.valueOf(fincaActual.getDirParcela().getSegundoNumero()));

            		UnidadConstructivaPanel.this.getJButtonAniadirUC().setEnabled(true);
            	}
            });
        }
        return jButtonNuevaUC;
    }
	
	private JButton getJButtonAniadirUC()
    {
        if (jButtonAniadirUC    == null)
        {
        	jButtonAniadirUC = new JButton();
        	jButtonAniadirUC.setText(I18N.get("Expedientes", "unidadconstructiva.panel.boton.aniadirunidadconstructiva"));
        	UnidadConstructivaPanel.this.getJButtonAniadirUC().setEnabled(false);
        	
        	jButtonAniadirUC.addActionListener(new java.awt.event.ActionListener()
            {
            	public void actionPerformed(java.awt.event.ActionEvent e)
            	{         
            		//Añadir Unidad Conatructiva
                    annadeUC();
            	}
            });
        }
        return jButtonAniadirUC;
    }

    public void asignaArbol(ExpedientePanelTree panelExpedientes)
    {
        this.panelExpedientes = panelExpedientes;
        arbol= panelExpedientes.getTree();
    }

    public void asignaArbolPanel(Object panel)
    {
    	if (panel instanceof com.geopista.ui.plugin.infcattitularidad.paneles.InfoCatastralPanelTree) {
			arbol= ((com.geopista.ui.plugin.infcattitularidad.paneles.InfoCatastralPanelTree)panel).getTree();
		}
    	else if (panel instanceof com.geopista.ui.plugin.infcattitularidad.paneles.InfoCatastralPanelTree){
    		arbol= ((com.geopista.ui.plugin.infcattitularidad.paneles.InfoCatastralPanelTree)panel).getTree();
    	}
    }
    
    private void annadeUC()
    {
    	if(this.datosMinimosYCorrectosUC()){
	        getUnidadConstructiva();
	        ArrayList lstUC = fincaActual.getLstUnidadesConstructivas();
	        boolean existeONoCorrecta = false;
	        if(lstUC==null)
	        {
	            lstUC = new ArrayList();
	            fincaActual.setLstUnidadesConstructivas(lstUC);            
	        }
	        for(int i = 0; i< lstUC.size() && !existeONoCorrecta;i++)
	        {
	            UnidadConstructivaCatastro  uc = (UnidadConstructivaCatastro)lstUC.get(i);
	            if((unidadCons.getCodUnidadConstructiva()!=null
	            &&unidadCons.getCodUnidadConstructiva().equalsIgnoreCase(uc.getCodUnidadConstructiva())))
	            {
	                existeONoCorrecta = true;
	            }
	        }
	        if(existeONoCorrecta || (unidadCons.getCodUnidadConstructiva()==null ||(unidadCons.getCodUnidadConstructiva()!=null
	            &&unidadCons.getCodUnidadConstructiva().equalsIgnoreCase(""))))
	        {
	            JOptionPane.showMessageDialog(this,I18N.get("Expedientes", "Error.J3"));
	            return;
	        }
	        lstUC.add(unidadCons);
	        DefaultMutableTreeNode node = (DefaultMutableTreeNode) arbol.getLastSelectedPathComponent();
	        Object nodeInfo = node.getUserObject();
	        if(nodeInfo instanceof UnidadConstructivaCatastro)
	        {
	            node = (DefaultMutableTreeNode)arbol.getSelectionPath().getParentPath().getLastPathComponent();
	        }
	        HideableNode nodo = panelExpedientes.addObject(node,unidadCons,true);
	        TreePath path = new TreePath(nodo.getPath());
	        arbol.setSelectionPath(path);
            UnidadConstructivaPanel.this.getJButtonAniadirUC().setEnabled(false);
    	}else{
            JOptionPane.showMessageDialog(this,I18N.get("Expedientes", "Catastro.Intercambio.Edicion.Dialogs.msgDatosMinimosYCorrectos"));
        }
    }

    private void eliminaUC()
    {
        ArrayList lstUC = fincaActual.getLstUnidadesConstructivas();
        boolean continua = true;
        for(int i = 0; i< lstUC.size() && continua;i++)
        {
            UnidadConstructivaCatastro  uc = (UnidadConstructivaCatastro)lstUC.get(i);
            if(unidadCons.getCodUnidadConstructiva()!=null
            &&unidadCons.getCodUnidadConstructiva().equalsIgnoreCase(uc.getCodUnidadConstructiva()))
            {
                lstUC.remove(i);
                continua = false;
            }
        }
        EdicionUtils.clearPanel(this);
        TreePath path = new TreePath(arbol.getSelectionPath().getParentPath().getLastPathComponent());
        panelExpedientes.removeObject(arbol.getSelectionPath().getLastPathComponent());        
        arbol.setSelectionPath(path);
    }

    public void setFincaActual(FincaCatastro fincaActual)
    {
        this.fincaActual = fincaActual;
    }
    
    public void setUnidadCons(UnidadConstructivaCatastro unidadCons)
    {
        this.unidadCons = unidadCons;
    }

    public boolean datosMinimosYCorrectosUC()
    {
    	boolean okLoc=true;
    	//Panel Localización Urbana obligatoria
    	if( (jComboBoxTipoVia!=null && jComboBoxTipoVia.getSelectedPatron()!=null && !(jComboBoxTipoVia.getSelectedPatron().equals("")))
    			|| (jTextFieldDireccion.getText()!=null && !jTextFieldDireccion.getText().equalsIgnoreCase(""))
    			|| (jTextFieldNumero.getText()!=null && !jTextFieldNumero.getText().equalsIgnoreCase(""))
    			|| (jTextFieldLetra.getText()!=null && !jTextFieldLetra.getText().equalsIgnoreCase(""))
    			|| (jTextFieldNumeroD.getText()!=null && !jTextFieldNumeroD.getText().equalsIgnoreCase(""))
    			|| (jTextFieldLetraD.getText()!=null && !jTextFieldLetraD.getText().equalsIgnoreCase(""))
    			|| (jTextFieldDirNoEstructurada.getText()!=null && !jTextFieldDirNoEstructurada.getText().equalsIgnoreCase(""))
    			|| (jTextFieldKm.getText()!=null && !jTextFieldKm.getText().equalsIgnoreCase("")) ){

    	        okLoc = (jComboBoxTipoVia!=null && jComboBoxTipoVia.getSelectedPatron()!=null && !(jComboBoxTipoVia.getSelectedPatron().equals(""))) &&
	        		(jTextFieldDireccion.getText()!=null && !jTextFieldDireccion.getText().equalsIgnoreCase(""));
    	}
    	
    	boolean okPonencia = true;
    	if((jComboBoxTramo!=null && jComboBoxTramo.getSelectedItem()!=null && jComboBoxTramo.getSelectedIndex()>0)
				|| (jTextFieldCodigoVia.getText()!=null && !jTextFieldCodigoVia.getText().equalsIgnoreCase(""))){    		
        		okPonencia=(jComboBoxTramo!=null && jComboBoxTramo.getSelectedItem()!=null && jComboBoxTramo.getSelectedIndex()>0)
        					&& (jTextFieldCodigoVia.getText()!=null && !jTextFieldCodigoVia.getText().equalsIgnoreCase(""));
    	}    	
    	
    	if((jTextFieldAnioConstruccion.getText()!=null && jTextFieldAnioConstruccion.getText().length()!=0 && jTextFieldAnioConstruccion.getText().length()!=4))
    		JOptionPane.showMessageDialog(this,I18N.get("Expedientes", "Error.J7"));

        return  okLoc && okPonencia && (jTextFieldSuperficieSuelo.getText()!=null && !jTextFieldSuperficieSuelo.getText().equalsIgnoreCase("")) &&
        		(jTextFieldAnioConstruccion.getText()!=null && jTextFieldAnioConstruccion.getText().length()==4) &&
        		(jTextFieldPCatastral1.getText()!=null && !jTextFieldPCatastral1.getText().equalsIgnoreCase("")) &&
        		(jTextFieldPCatastral2.getText()!=null && !jTextFieldPCatastral2.getText().equalsIgnoreCase("")) &&
        		(jTextFieldCodigoUC.getText()!=null && !jTextFieldCodigoUC.getText().equalsIgnoreCase("")) &&
        		(jTextFieldEstadoConservacion.getText()!=null && !jTextFieldEstadoConservacion.getText().equalsIgnoreCase("")) &&
        		(jTextFieldValorCCCargasSingulares.getText()!=null && !jTextFieldValorCCCargasSingulares.getText().equalsIgnoreCase("")) &&
        		(jComboBoxNumeroFachadas!=null && jComboBoxNumeroFachadas.getSelectedItem()!=null && jComboBoxNumeroFachadas.getSelectedIndex()>0) &&
        		(jComboBoxClaseUC!=null && jComboBoxClaseUC.getSelectedItem()!=null && jComboBoxClaseUC.getSelectedIndex()>0);
    }

	public void cleanup() {
		// TODO Auto-generated method stub
		if (jPanelDatosEconomicos!=null){jPanelDatosEconomicos.removeAll();}
		if (jPanelDatosUC!=null){jPanelDatosUC.removeAll();}
		if (jPanelDatosLocalizacion!=null){jPanelDatosLocalizacion.removeAll();}
		if (jPanelDatosFisicos!=null){jPanelDatosFisicos.removeAll();	}
		if (jPanelDatosEconomicos!=null){jPanelDatosEconomicos.removeAll();}
		if (jPanelDatosPonencia!=null){jPanelDatosPonencia.removeAll();	}		
		if (jPanelCCSuelo!=null){jPanelCCSuelo.removeAll();	}		
		if (jPanelCCSuelo!=null){jPanelCCSuelo.removeAll();	}		
		if (jPanelCCConstruccion!=null){jPanelCCConstruccion.removeAll();	}		
		if (jPanelCCSueloYConstruccion!=null){jPanelCCSueloYConstruccion.removeAll();	}		
		if (jPanelBotonera!=null){jPanelBotonera.removeAll();	}		
		
	    jTextFieldCodigoUC = null;
	    jTextFieldDireccion = null;
	    jTextFieldNumero = null;
	    jTextFieldLetra = null;
	    jTextFieldNumeroD = null;
	    jTextFieldLetraD = null;
	    jTextFieldBloque = null;
	    jTextFieldDirNoEstructurada = null;
	    jTextFieldKm = null;
	    jTextFieldAnioConstruccion = null;
	    jTextFieldSuperficieSuelo = null;
	    jTextFieldLongitudFachada = null;
	    jTextFieldCodigoVia=null;
	    jTextFieldEstadoConservacion = null;
	    jTextFieldValorCCCargasSingulares = null;
	    jTextFieldPCatastral2=null;
	    jTextFieldPCatastral1=null;

		
		if (jComboBoxTramo!=null){jComboBoxTramo.removeAll();jComboBoxTramo=null;}
		if (jComboBoxZonaValor!=null){jComboBoxZonaValor.removeAll();jComboBoxZonaValor=null;}
		
	}
}  //  @jve:decl-index=0:visual-constraint="3,-86"
