/**
 * SueloPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.infcatfisicoeconomico.dialogs;

import java.awt.Component;
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
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.catastro.intercambio.edicion.CodigoViasSistemaDialog;
import com.geopista.app.catastro.intercambio.edicion.ExpedientePanelTree;
import com.geopista.app.catastro.intercambio.edicion.dialogs.GestionExpedienteConst;
import com.geopista.app.catastro.intercambio.edicion.dialogs.GestionExpedientePanel;
import com.geopista.app.catastro.intercambio.edicion.utils.EdicionOperations;
import com.geopista.app.catastro.intercambio.edicion.utils.EdicionUtils;
import com.geopista.app.catastro.intercambio.edicion.utils.EstructuraDBListCellRenderer;
import com.geopista.app.catastro.intercambio.edicion.utils.HideableNode;
import com.geopista.app.catastro.intercambio.images.IconLoader;
import com.geopista.app.catastro.model.beans.ConstantesRegExp;
import com.geopista.app.catastro.model.beans.EstructuraDB;
import com.geopista.app.catastro.model.beans.FincaCatastro;
import com.geopista.app.catastro.model.beans.ReferenciaCatastral;
import com.geopista.app.catastro.model.beans.SueloCatastro;
import com.geopista.app.catastro.model.datos.ponencia.PonenciaTramos;
import com.geopista.app.catastro.model.datos.ponencia.PonenciaUrbanistica;
import com.geopista.app.catastro.model.datos.ponencia.PonenciaZonaValor;
import com.geopista.app.catastro.registroExpedientes.utils.ConstantesRegistro;
import com.geopista.app.catastro.registroExpedientes.utils.UtilRegistroExp;
import com.geopista.app.utilidades.TextField;
import com.geopista.app.utilidades.comboBoxTipo.ComboBoxKeyTipoDestinoSelectionManager;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.geopista.util.FeatureExtendedPanel;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.Blackboard;

public class SueloPanel extends JPanel implements FeatureExtendedPanel
{
    
    private JPanel jPanelDatosSuelo = null;
    private JPanel jPanelDatosFisicos = null;
    private JPanel jPanelDatosEconomicos = null;
    private JTextField jTextFieldSubparcela = null;
    private JLabel jLabelSubparcela = null;
    private JLabel jLabelSuperficie = null;
    private JLabel jLabelFondo = null;
    private JTextField jTextFieldSuperficie = null;
    private JTextField jTextFieldFondo = null;
    private JTextField jTextFieldLongitudFachada = null;
    private JPanel jPanelCCSuelo = null;
    private JPanel jPanelCCSueloYConstruccion = null;
    private JCheckBox jCheckBoxLongitudFachada;
    private JLabel jLabelValorCCApreciacion = null;
    private JTextField jTextFieldValorCCApreciacion = null;
    private JCheckBox jCheckBoxDepreciacion = null;
    private JCheckBox jCheckBoxSituacionesEspeciales = null;
    private JCheckBox jCheckBoxUsoNoLucrativo = null;  //  @jve:decl-index=0:visual-constraint="871,453"
    private JPanel jPanelFachada = null;
    private JPanel jPanelSuperficieFondo = null;
    private JCheckBox jCheckBoxDesmonteExcesivo = null;
    private JCheckBox jCheckBoxSupDistintaAMinima = null;
    private JCheckBox jCheckBoxInedificabilidadTemporal = null;
    private JCheckBox jCheckBoxReservadoVPO = null;
    private JTextField jTextFieldValorCCFincaAfectada;
    private JLabel jLabelValorCCFincaAfectada;
    private JPanel jPanelDatosPonencia;
    private JTextField jTextFieldCodigoVia;
    private ComboBoxEstructuras jComboBoxCodigoTipoValor;
    private JTextField jTextFieldPCatastral2;
    private JTextField jTextFieldPCatastral1;
    private JLabel jLabelPCatastral1;
    private JLabel jLabelPCatastral2;
    private JLabel jLabelZonaValor;
    private JLabel jLabelTramo;
    private JLabel jLabelCodigoVia;
    private JLabel jLabelZonaUrbanistica;
    private JLabel jLabelCodigoTipoValor;
    private SueloCatastro suelo;
	private ComboBoxEstructuras jComboBoxTipoFachada = null;
	private JCheckBox jCheckBoxFormaIrregular = null;
	private JPanel jPanelBotonera = null;
	private JButton jButtonNuevoSuelo = null;
	private JButton jButtonEliminarSuelo = null;
	private ComboBoxEstructuras jComboBoxNumeroFachadas = null;
	private JComboBox jComboBoxZonaValor = null;
	private JComboBox jComboBoxZonaUrbanistica = null;     
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    private Blackboard Identificadores = aplicacion.getBlackboard();
	private JComboBox jComboBoxTramo = null;
	private JButton jButtonBuscarCodigoVia = null;
	private JButton jButtonAniadirSuelo = null;
    private JTree arbol;
    private ExpedientePanelTree panelExpedientes;
    private FincaCatastro fincaActual;
    private GestionExpedientePanel gestionExpedientePanel = null;
    
    private ArrayList estructuraDBArray = new ArrayList();
    
    public SueloPanel()
    {
        super();
        initialize();
    }
    
    public SueloPanel(GestionExpedientePanel gestionExpedientePanel)
    {
        super();
        this.gestionExpedientePanel = gestionExpedientePanel;
        initialize();
    }
    
    public SueloPanel(SueloCatastro suelo)
    {
        super();
        initialize();        
        load (suelo);
    }
    
    public class ListenerTextField implements  DocumentListener{
    	/*
    	 * Método que recibe evento cuando se escribe en alguno de los jTextField que tienen un escuchador DocumentListener
    	 * Según el campo origen del evento, cambia las etiquetas obligatorias.
    	 * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
    	 */
        public void insertUpdate(DocumentEvent e){
        	//Panel Base Liquidable
        	if(jTextFieldCodigoVia.getDocument()==e.getDocument()){
        		jComboBoxZonaValor.setEnabled(false);
        		jLabelTramo.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "suelo.panel.datoseconomicos.datosponencia.tramo")));
        		jLabelCodigoVia.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "suelo.panel.datoseconomicos.datosponencia.codvia")));
        		jLabelZonaUrbanistica.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "suelo.panel.datoseconomicos.datosponencia.zonaurbanistica")));
        		jLabelCodigoTipoValor.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "suelo.panel.datoseconomicos.datosponencia.codtipovalor")));
        		jPanelDatosPonencia.setBorder(BorderFactory.createTitledBorder(I18N.get("Expedientes", "suelo.panel.datoseconomicos.datosponencia.titulo")));
        	}
        }
    	/*
    	 * Método que recibe evento cuando se borra caracteres en alguno de los jTextField que tienen un escuchador DocumentListener
    	 * Según el campo origen del evento, cambia las etiquetas obligatorias.
    	 * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
    	 */
        public void removeUpdate(DocumentEvent e){
        	//Panel Base Liquidable
        	if(jTextFieldCodigoVia.getDocument().getLength()<=0 && jComboBoxTramo.getSelectedIndex()<=0){
        		jLabelTramo.setText(I18N.get("Expedientes", "suelo.panel.datoseconomicos.datosponencia.tramo"));
        		jLabelCodigoVia.setText(I18N.get("Expedientes", "suelo.panel.datoseconomicos.datosponencia.codvia"));
        		jComboBoxZonaValor.setEnabled(true);
        		if(jComboBoxZonaValor.getSelectedIndex()<=0){
            		jTextFieldCodigoVia.setEnabled(false);
            		jTextFieldCodigoVia.setOpaque(true);
            		jButtonBuscarCodigoVia.setEnabled(false);
            		jComboBoxTramo.setEnabled(false);
        			jLabelZonaUrbanistica.setText(I18N.get("Expedientes", "suelo.panel.datoseconomicos.datosponencia.zonaurbanistica"));
            		jLabelCodigoTipoValor.setText(I18N.get("Expedientes", "suelo.panel.datoseconomicos.datosponencia.codtipovalor"));        
            		jPanelDatosPonencia.setBorder(BorderFactory.createTitledBorder("* "+I18N.get("Expedientes", "suelo.panel.datoseconomicos.datosponencia.titulo")));
        		}else{
            		jTextFieldCodigoVia.setEnabled(false);
            		jTextFieldCodigoVia.setOpaque(false);
            		jButtonBuscarCodigoVia.setEnabled(false);
            		jComboBoxTramo.setEnabled(false);
        			jLabelZonaUrbanistica.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "suelo.panel.datoseconomicos.datosponencia.zonaurbanistica")));
            		jLabelCodigoTipoValor.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "suelo.panel.datoseconomicos.datosponencia.codtipovalor")));        
            		jPanelDatosPonencia.setBorder(BorderFactory.createTitledBorder(I18N.get("Expedientes", "suelo.panel.datoseconomicos.datosponencia.titulo")));
        		}
        	}else{
        		jComboBoxZonaValor.setEnabled(false);
        		jTextFieldCodigoVia.setEnabled(false); 
        		jTextFieldCodigoVia.setOpaque(true);
        		jButtonBuscarCodigoVia.setEnabled(false);
        		jComboBoxTramo.setEnabled(false);
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
        	if(jComboBoxZonaValor.getSelectedIndex()>0 || jComboBoxTramo.getSelectedIndex()>0 || jTextFieldCodigoVia.getDocument().getLength()>0){
        		if(jComboBoxZonaValor.getSelectedIndex()>0){
        			jLabelZonaValor.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "suelo.panel.datoseconomicos.datosponencia.zonavalor")));
            		jTextFieldCodigoVia.setEnabled(false);            		
            		jTextFieldCodigoVia.setOpaque(false);
            		jButtonBuscarCodigoVia.setEnabled(false);
            		jComboBoxTramo.setEnabled(false);
            		jComboBoxZonaValor.setEnabled(true);
        		}else{
            		jTextFieldCodigoVia.setEnabled(false);
            		jTextFieldCodigoVia.setOpaque(true);
            		jButtonBuscarCodigoVia.setEnabled(false);
            		jComboBoxTramo.setEnabled(false);
            		jComboBoxZonaValor.setEnabled(false);
        			jLabelZonaValor.setText(I18N.get("Expedientes", "suelo.panel.datoseconomicos.datosponencia.zonavalor"));
        			jLabelTramo.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "suelo.panel.datoseconomicos.datosponencia.tramo")));
        			jLabelCodigoVia.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "suelo.panel.datoseconomicos.datosponencia.codvia")));
        		}
        		jLabelCodigoTipoValor.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "suelo.panel.datoseconomicos.datosponencia.codtipovalor")));        		
        		jLabelZonaUrbanistica.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "suelo.panel.datoseconomicos.datosponencia.zonaurbanistica")));
        		jPanelDatosPonencia.setBorder(BorderFactory.createTitledBorder(I18N.get("Expedientes", "suelo.panel.datoseconomicos.datosponencia.titulo")));
           	}else{     
        		jTextFieldCodigoVia.setEnabled(false);
        		jTextFieldCodigoVia.setOpaque(true);
        		jButtonBuscarCodigoVia.setEnabled(false);
        		jComboBoxTramo.setEnabled(false);
        		jComboBoxZonaValor.setEnabled(true);
           		jPanelDatosPonencia.setBorder(BorderFactory.createTitledBorder("* "+I18N.get("Expedientes", "suelo.panel.datoseconomicos.datosponencia.titulo")));
           		jLabelZonaValor.setText(I18N.get("Expedientes", "suelo.panel.datoseconomicos.datosponencia.zonavalor"));
           		jLabelTramo.setText(I18N.get("Expedientes", "suelo.panel.datoseconomicos.datosponencia.tramo"));
           		jLabelCodigoVia.setText(I18N.get("Expedientes", "suelo.panel.datoseconomicos.datosponencia.codvia"));
        		jLabelZonaUrbanistica.setText(I18N.get("Expedientes", "suelo.panel.datoseconomicos.datosponencia.zonaurbanistica"));
        		jLabelCodigoTipoValor.setText(I18N.get("Expedientes", "suelo.panel.datoseconomicos.datosponencia.codtipovalor"));
        	}  
        }
    }
    
    private void initialize()
    {
        Locale loc=I18N.getLocaleAsObject();         
        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.catastro.intercambio.language.Expedientesi18n",loc,this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("Expedientes",bundle);
        
        
        this.setLayout(new GridBagLayout());
        this.add(getJPanelDatosSuelo(), 
                new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH,
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
    
        
        if (Identificadores.get("ListaZonaValor")==null)
        {
            EdicionOperations oper = new EdicionOperations();
            ArrayList lst = oper.obtenerZonaValor();
            Identificadores.put("ListaZonaValor", lst);
            EdicionUtils.cargarLista(getJComboBoxZonaValor(), lst);
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

        
        if (Identificadores.get("ListaZonaUrbanistica")==null)
        {
            EdicionOperations oper = new EdicionOperations();
            ArrayList lst = oper.obtenerZonaUrbanistica();
            Identificadores.put("ListaZonaUrbanistica", lst);
            EdicionUtils.cargarLista(getJComboBoxZonaUrbanistica(), lst);
        }
        else
        {
            EdicionUtils.cargarLista(getJComboBoxZonaUrbanistica(), 
                    (ArrayList)Identificadores.get("ListaZonaUrbanistica"));
        } 
    }
    
    
    private void cargarListaTramo (JComboBox jComboBox, ArrayList lst)
    {
        jComboBox.removeAllItems();
        
        Iterator it = lst.iterator();
        while (it.hasNext()){
        	EstructuraDB estructuraDB = new EstructuraDB();
        	estructuraDB = (EstructuraDB)it.next();
        	
            jComboBox.addItem(estructuraDB.getPatron()+"- "+estructuraDB.getDescripcion());
            estructuraDBArray.add(estructuraDBArray);  
        }
    }
    
    public void load(SueloCatastro suelo)
    {        
        this.suelo = suelo;
        //Datos del suelo
        jTextFieldPCatastral1.setText(suelo.getRefParcela().getRefCatastral1());
        jTextFieldPCatastral2.setText(suelo.getRefParcela().getRefCatastral2());
        jTextFieldSubparcela.setText(suelo.getNumOrden());
        
        
        //Datos físicos
        jTextFieldSuperficie.setText(EdicionUtils.getStringValue(suelo.getDatosFisicos().getSupOcupada()));
        jTextFieldFondo.setText(EdicionUtils.getStringValue(suelo.getDatosFisicos().getFondo()));
        //  Fachada
        jTextFieldLongitudFachada.setText(EdicionUtils.getStringValue(suelo.getDatosFisicos().getLongFachada()));
        
        jComboBoxTipoFachada.setSelectedPatron(suelo.getDatosFisicos().getTipoFachada()!=null?suelo.getDatosFisicos().getTipoFachada():"");
        
        
        //Datos Económicos
        //   De la ponencia
        jComboBoxCodigoTipoValor.setSelectedPatron(suelo.getDatosEconomicos().getCodTipoValor());
               
        jComboBoxZonaValor.setSelectedItem(suelo.getDatosEconomicos().getZonaValor().getCodZonaValor()!=null?
        		suelo.getDatosEconomicos().getZonaValor().getCodZonaValor():"");
        
        EstructuraDB eUrb = new EstructuraDB();
        if((jComboBoxZonaUrbanistica.getItemCount() > 0) && (suelo.getDatosEconomicos().getZonaUrbanistica() != null && suelo.getDatosEconomicos().getZonaUrbanistica().getCodZona()!=null)){            	
        	eUrb.setPatron(suelo.getDatosEconomicos().getZonaUrbanistica().getCodZona());
        	eUrb.setDescripcion(suelo.getDatosEconomicos().getZonaUrbanistica().getDenominacion());
        	jComboBoxZonaUrbanistica.setSelectedItem(eUrb);
        }
        else{
        	jComboBoxZonaUrbanistica.setSelectedItem(eUrb);
        }
                
        jTextFieldCodigoVia.getDocument().addDocumentListener(new ListenerTextField());         
        jTextFieldCodigoVia.setText(suelo.getDatosEconomicos().getCodViaPonencia());
        
        EstructuraDB eTramo = new EstructuraDB();
        if((jComboBoxTramo.getItemCount() > 0) && (suelo.getDatosEconomicos().getTramos() != null && suelo.getDatosEconomicos().getTramos().getCodTramo()!=null)){            	
        	eTramo.setPatron(suelo.getDatosEconomicos().getTramos().getCodTramo());
        	eTramo.setDescripcion(suelo.getDatosEconomicos().getTramos().getDenominacion());
        	jComboBoxTramo.setSelectedItem(eTramo);
        }
        else{
        	jComboBoxTramo.setSelectedItem(eTramo);
        }
        //coeficientes correctores del valor del suelo
       
        jComboBoxNumeroFachadas.setSelectedPatron(suelo.getDatosEconomicos().getNumFachadas()!=null?
        		(EdicionUtils.getStringValue(suelo.getDatosEconomicos().getNumFachadas())):"");
        
        jCheckBoxFormaIrregular.setSelected(suelo.getDatosEconomicos().isCorrectorFormaIrregular()!=null?
                suelo.getDatosEconomicos().isCorrectorFormaIrregular().booleanValue():false);
        jCheckBoxLongitudFachada.setSelected(suelo.getDatosEconomicos().isCorrectorLongFachada()!=null?
                suelo.getDatosEconomicos().isCorrectorLongFachada().booleanValue():false);
        jCheckBoxDesmonteExcesivo.setSelected(suelo.getDatosEconomicos().isCorrectorDesmonte()!=null?
                suelo.getDatosEconomicos().isCorrectorDesmonte().booleanValue():false);
        jCheckBoxSupDistintaAMinima.setSelected(suelo.getDatosEconomicos().isCorrectorSupDistinta()!=null?
                suelo.getDatosEconomicos().isCorrectorSupDistinta().booleanValue():false);
        jCheckBoxInedificabilidadTemporal.setSelected(suelo.getDatosEconomicos().isCorrectorInedificabilidad()!=null?
                suelo.getDatosEconomicos().isCorrectorInedificabilidad().booleanValue():false);
        jCheckBoxReservadoVPO.setSelected(suelo.getDatosEconomicos().isCorrectorVPO()!=null?
                suelo.getDatosEconomicos().isCorrectorVPO().booleanValue():false);
        
        //Coeficientes correctores del valor del suelo y de las construcciones
        jTextFieldValorCCApreciacion.setText(EdicionUtils.getStringValue(suelo.getDatosEconomicos().getCorrectorAprecDeprec()));
        jTextFieldValorCCFincaAfectada.setText(EdicionUtils.getStringValue(suelo.getDatosEconomicos().getCorrectorCargasSingulares()));
        jCheckBoxDepreciacion.setSelected(suelo.getDatosEconomicos().isCorrectorVPO()!=null?
                suelo.getDatosEconomicos().isCorrectorDeprecFuncional().booleanValue():false);
        jCheckBoxSituacionesEspeciales.setSelected(suelo.getDatosEconomicos().isCorrectorSitEspeciales()!=null?
                suelo.getDatosEconomicos().isCorrectorSitEspeciales().booleanValue():false);
        jCheckBoxUsoNoLucrativo.setSelected(suelo.getDatosEconomicos().isCorrectorNoLucrativo()!=null?
                suelo.getDatosEconomicos().isCorrectorNoLucrativo().booleanValue():false);
    }
    
    public SueloCatastro getSuelo ()
    {
        if(suelo!=null)
        {
            //Datos del suelo
            ReferenciaCatastral refCat = new ReferenciaCatastral (jTextFieldPCatastral1.getText(), jTextFieldPCatastral2.getText());
            suelo.setRefParcela(refCat);
            suelo.setIdSuelo(EdicionUtils.paddingString(jTextFieldPCatastral1.getText(),EdicionUtils.TAM_REFCATASTRAL/2,'0', true)
                    + EdicionUtils.paddingString(jTextFieldPCatastral2.getText(),EdicionUtils.TAM_REFCATASTRAL/2,'0', true)
                    + EdicionUtils.paddingString(jTextFieldSubparcela.getText(),EdicionUtils.TAM_NUMORDEN_SUBPARCELA,'0', true));
            suelo.setNumOrden(EdicionUtils.paddingString(jTextFieldSubparcela.getText(),EdicionUtils.TAM_NUMORDEN_SUBPARCELA,'0', true));


            //Datos físicos
            if (jTextFieldSuperficie.getText().length() > 0)
                suelo.getDatosFisicos().setSupOcupada(Long.valueOf(jTextFieldSuperficie.getText()));
            if (jTextFieldFondo.getText().length()>0)
                suelo.getDatosFisicos().setFondo(Integer.valueOf(jTextFieldFondo.getText()));
            //  Fachada
            if (jTextFieldLongitudFachada.getText().length()>0)
            	suelo.getDatosFisicos().setLongFachada(Float.valueOf(jTextFieldLongitudFachada.getText().replaceAll(",",".")));

            suelo.getDatosFisicos().setTipoFachada(jComboBoxTipoFachada.getSelectedItem()!=null?jComboBoxTipoFachada.getSelectedItem().toString():"");

            //Datos Económicos
            //   De la ponencia
            suelo.getDatosEconomicos().setCodTipoValor(jComboBoxCodigoTipoValor.getSelectedPatron());

            suelo.getDatosEconomicos().getZonaValor().setCodZonaValor(jComboBoxZonaValor.getSelectedItem()!=null?
                    jComboBoxZonaValor.getSelectedItem().toString():"");

            suelo.getDatosEconomicos().setCodViaPonencia(jTextFieldCodigoVia.getText());
            
            if(jComboBoxTramo.getSelectedItem()!=null){ 
            	if (suelo.getDatosEconomicos().getTramos()== null){
            		suelo.getDatosEconomicos().setTramos(new PonenciaTramos());
            	}
            	suelo.getDatosEconomicos().getTramos().setCodTramo(((EstructuraDB)jComboBoxTramo.getSelectedItem()).getPatron());
            	suelo.getDatosEconomicos().getTramos().setDenominacion(((EstructuraDB)jComboBoxTramo.getSelectedItem()).getDescripcion());
            
            }
            
            if(jComboBoxZonaUrbanistica.getSelectedItem()!=null){ 
            	if (suelo.getDatosEconomicos().getZonaUrbanistica()== null){
            		suelo.getDatosEconomicos().setZonaUrbanistica(new PonenciaUrbanistica());
            	}
            	suelo.getDatosEconomicos().getZonaUrbanistica().setCodZona(((EstructuraDB)jComboBoxZonaUrbanistica.getSelectedItem()).getPatron());
            	suelo.getDatosEconomicos().getZonaUrbanistica().setDenominacion(((EstructuraDB)jComboBoxZonaUrbanistica.getSelectedItem()).getDescripcion());
            	
            }
            
            //coeficientes correctores del valor del suelo

            suelo.getDatosEconomicos().setNumFachadas(jComboBoxNumeroFachadas.getSelectedItem()!=null?
            	((ComboBoxEstructuras)jComboBoxNumeroFachadas).getSelectedPatron():"");
                  
            suelo.getDatosEconomicos().setCorrectorLongFachada(Boolean.valueOf(jCheckBoxLongitudFachada.isSelected()));
            suelo.getDatosEconomicos().setCorrectorFormaIrregular(Boolean.valueOf(jCheckBoxFormaIrregular.isSelected()));
            suelo.getDatosEconomicos().setCorrectorDesmonte(Boolean.valueOf(jCheckBoxDesmonteExcesivo.isSelected()));
            suelo.getDatosEconomicos().setCorrectorSupDistinta(Boolean.valueOf(jCheckBoxSupDistintaAMinima.isSelected()));
            suelo.getDatosEconomicos().setCorrectorInedificabilidad(Boolean.valueOf(jCheckBoxInedificabilidadTemporal.isSelected()));
            suelo.getDatosEconomicos().setCorrectorVPO(Boolean.valueOf(jCheckBoxReservadoVPO.isSelected()));

            //Coeficientes correctores del valor del suelo y de las construcciones
            if (jTextFieldValorCCApreciacion.getText().length()>0)
                suelo.getDatosEconomicos().setCorrectorAprecDeprec(Float.valueOf(jTextFieldValorCCApreciacion.getText()));
            if (jTextFieldValorCCFincaAfectada.getText().length()>0)
                suelo.getDatosEconomicos().setCorrectorCargasSingulares(Float.valueOf(jTextFieldValorCCFincaAfectada.getText()));
            suelo.getDatosEconomicos().setCorrectorDeprecFuncional(Boolean.valueOf(jCheckBoxDepreciacion.isSelected()));
            suelo.getDatosEconomicos().setCorrectorSitEspeciales(Boolean.valueOf(jCheckBoxSituacionesEspeciales.isSelected()));
            suelo.getDatosEconomicos().setCorrectorNoLucrativo(Boolean.valueOf(jCheckBoxUsoNoLucrativo.isSelected()));
            
            if (suelo.getCodDelegacion() == null || suelo.getCodDelegacion().equals("")){
            	suelo.setCodDelegacion(fincaActual.getCodDelegacionMEH());
            }
            if (suelo.getCodMunicipioDGC() == null || suelo.getCodMunicipioDGC().equals("")){
            	suelo.setCodMunicipioDGC(fincaActual.getCodMunicipioDGC());
            }
            
            if (suelo.getDatosEconomicos().getZonaValor().getCodZonaValor() != null &&
            		!suelo.getDatosEconomicos().getZonaValor().getCodZonaValor().equals("")){
            	
            	try {
            		
					PonenciaZonaValor ponenciaZonaValor = ConstantesRegExp.clienteCatastro.obtenerPonenciaZonaValor(suelo.getDatosEconomicos().getZonaValor().getCodZonaValor());
					suelo.getDatosEconomicos().setZonaValor(ponenciaZonaValor);
					
				} catch (Exception e) {					
					e.printStackTrace();
				}
            }
            
            
            if (suelo.getDatosEconomicos().getZonaUrbanistica().getCodZona() != null &&
            		!suelo.getDatosEconomicos().getZonaUrbanistica().getCodZona().equals("")){
            	
            	try {
            		
					PonenciaUrbanistica ponenciaUrbanistica = ConstantesRegExp.clienteCatastro.obtenerPonenciaUrbanistica(suelo.getDatosEconomicos().getZonaUrbanistica().getCodZona());
					suelo.getDatosEconomicos().setZonaUrbanistica(ponenciaUrbanistica);
					
				} catch (Exception e) {					
					e.printStackTrace();
				}
            }
            
            if (suelo.getDatosEconomicos().getTramos() != null && suelo.getDatosEconomicos().getTramos().getCodTramo() != null &&
            		!suelo.getDatosEconomicos().getTramos().getCodTramo().equals("")){
            	
            	try {
            		
					PonenciaTramos ponenciaTramos = ConstantesRegExp.clienteCatastro.obtenerPonenciaTramos(suelo.getDatosEconomicos().getTramos().getCodTramo());
					suelo.getDatosEconomicos().setTramos(ponenciaTramos);
					
				} catch (Exception e) {					
					e.printStackTrace();
				}
            }
            
        }
        return suelo;
    }
    
    
    /**
     * This method initializes jPanelDatosUC    
     *  
     * @return javax.swing.JPanel   
     */
    private JPanel getJPanelDatosSuelo()
    {
        if (jPanelDatosSuelo == null)
        {
            jLabelSubparcela = new JLabel(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "suelo.panel.datossuelo.subparcela")), JLabel.CENTER); 
            jLabelSubparcela.setPreferredSize(new Dimension(100, 20));
            
            jLabelPCatastral1 = new JLabel("", JLabel.CENTER); 
            jLabelPCatastral1.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "suelo.panel.datossuelo.catastral1"))); 
            jLabelPCatastral2 = new JLabel("", JLabel.CENTER); 
            jLabelPCatastral2.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "suelo.panel.datossuelo.catastral2"))); 
            
            GridBagLayout layout = new GridBagLayout();
            jPanelDatosSuelo = new JPanel(layout);
            
            jPanelDatosSuelo.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("Expedientes", "suelo.panel.datossuelo.titulo"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
            jPanelDatosSuelo.add(jLabelSubparcela, new GridBagConstraints(2, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
            
            jPanelDatosSuelo.add(jLabelPCatastral1, new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosSuelo.add(jLabelPCatastral2, new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosSuelo.add(getJTextFieldPCatastral1(),new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosSuelo.add(getJTextFieldPCatastral2(),new GridBagConstraints(1, 1, 1, 1, 0, 0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            
            
            jPanelDatosSuelo.add(getJTextFieldSubparcela(),new GridBagConstraints(2, 1, 1, 1, 0, 0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            
        }
        return jPanelDatosSuelo;
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
            jTextFieldPCatastral1 = new TextField(7);
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
            jTextFieldPCatastral2 = new TextField(7);
            jTextFieldPCatastral2.setEnabled(false);
            
        }
        return jTextFieldPCatastral2;
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
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.gridx = 2;
            gridBagConstraints1.gridy = 0;
            gridBagConstraints1.gridheight = 2;
           
            jPanelDatosFisicos = new JPanel(new GridBagLayout());
            jPanelDatosFisicos.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("Expedientes", "suelo.panel.datosfisicos.titulo"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
            
            jPanelDatosFisicos.add(getJPanelSuperficieFondo(), new GridBagConstraints(0, 1, 1, 1, 0.1, 0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosFisicos.add(getJPanelFachada(), new GridBagConstraints(3, 0, 1, 2, 0.1, 0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            
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
                    (null, I18N.get("Expedientes", "suelo.panel.datoseconomicos.titulo"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
            
            jPanelDatosEconomicos.add(getJPanelDatosPonencia(), 
                    new GridBagConstraints(0, 0, 2, 1, 0.1, 0.1, GridBagConstraints.NORTH,GridBagConstraints.BOTH, new Insets (0,5,0,5), 0,0));
            
            jPanelDatosEconomicos.add(getJPanelCCSuelo(), 
                    new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.WEST,GridBagConstraints.BOTH, new Insets (0,5,0,5), 0,0));
            jPanelDatosEconomicos.add(getJPanelCCSueloYConstruccion(), 
                    new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1, GridBagConstraints.EAST,GridBagConstraints.BOTH, new Insets (0,5,0,5), 0,0));
            
        }
        return jPanelDatosEconomicos;
    }
    
    /**
     * This method initializes jTextFieldSubparcela   
     *  
     * @return javax.swing.JTextField   
     */
    private JTextField getJTextFieldSubparcela()
    {
        if (jTextFieldSubparcela == null)
        {
            jTextFieldSubparcela = new TextField(4);
            jTextFieldSubparcela.setPreferredSize(new Dimension(200, 20));
            
        }
        return jTextFieldSubparcela;
    }
    
    /**
     * This method initializes jTextFieldSubparcela   
     *  
     * @return javax.swing.JTextField   
     */
    
    private JComboBox getJComboBoxTipodFachada()
    {
        if (jComboBoxTipoFachada  == null)
        {
        	Estructuras.cargarEstructura("Tipo Fachada");
        	jComboBoxTipoFachada = new ComboBoxEstructuras(Estructuras.getListaTipos(),
    				null, AppContext.getApplicationContext().getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), true);
        	    	
            
        }
        return jComboBoxTipoFachada;
    }
    
    
    /**
     * This method initializes jTextFieldSuperficie   
     *  
     * @return javax.swing.JTextField   
     */
    private JTextField getJTextFieldSuperficie()
    {
        if (jTextFieldSuperficie == null)
        {
            jTextFieldSuperficie = new TextField(7);
            jTextFieldSuperficie.addCaretListener(new CaretListener()
                    {
                public void caretUpdate(CaretEvent evt)
                {
                    EdicionUtils.chequeaLongYNumCampoEdit(jTextFieldSuperficie,7, aplicacion.getMainFrame());
                }
                    });
        }
        return jTextFieldSuperficie;
    }
    
    /**
     * This method initializes jTextFieldFondo  
     *  
     * @return javax.swing.JTextField   
     */
    private JTextField getJTextFieldFondo()
    {
        if (jTextFieldFondo == null)
        {
            jTextFieldFondo =  new TextField(3);//new JNumberTextField(JNumberTextField.NUMBER);//, new Integer(999));
            jTextFieldFondo.addCaretListener(new CaretListener()
                    {
                public void caretUpdate(CaretEvent evt)
                {
                    EdicionUtils.chequeaLongYNumCampoEdit(jTextFieldFondo,3, aplicacion.getMainFrame());
                }
                    });
        }
        return jTextFieldFondo;
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
            jTextFieldLongitudFachada = new TextField(5);//new JNumberTextField(JNumberTextField.NUMBER);//, new Integer(99999));
            jTextFieldLongitudFachada.addCaretListener(new CaretListener()
                    {
                public void caretUpdate(CaretEvent evt)
                {
                    EdicionUtils.chequeaLongYDecimalCampoEdit(jTextFieldLongitudFachada,5, aplicacion.getMainFrame());
                }
                    });
            jTextFieldLongitudFachada.setPreferredSize(new Dimension(100, 20));
        }
        return jTextFieldLongitudFachada;
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
            jPanelCCSuelo.setBorder(BorderFactory.createTitledBorder(I18N.get("Expedientes", "suelo.panel.datoseconomicos.coefcorrectores.titulo"))); 
            
            JLabel jLabelNumeroFachadas = new JLabel(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "suelo.panel.datoseconomicos.coefcorrectores.numfachadas"))); 
            jPanelCCSuelo.add(jLabelNumeroFachadas,
                    new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL, new Insets (0,25,0,5), 0,0));
           
            jPanelCCSuelo.add(getJComboBoxNumeroFachadas(),
                    new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelCCSuelo.add(getJCheckBoxLongitudFachada(),
                    new GridBagConstraints(0, 1, 2, 1, 0, 0, GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL, new Insets (5,5,0,5), 0,0));
            jPanelCCSuelo.add(getJCheckBoxFormaIrregular(),
                    new GridBagConstraints(0, 2, 2, 1, 0, 0, GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL, new Insets (5,5,0,5), 0,0));
            jPanelCCSuelo.add(getJCheckBoxDesmonteExcesivo(), 
                    new GridBagConstraints(0, 3, 2, 1, 0, 0, GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelCCSuelo.add(getJCheckBoxSupDistintaAMinima(), 
                    new GridBagConstraints(0, 4, 2, 1, 0, 0, GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelCCSuelo.add(getJCheckBoxInedificabilidadTemporal(), 
                    new GridBagConstraints(0, 5, 2, 1, 0, 0, GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelCCSuelo.add(getJCheckBoxReservadoVPO(), 
                    new GridBagConstraints(0, 6, 2, 1, 0, 0, GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            
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
        if (jComboBoxNumeroFachadas   == null)
        {
        	Estructuras.cargarEstructura("Numero Fachadas");
        	jComboBoxNumeroFachadas = new ComboBoxEstructuras(Estructuras.getListaTipos(),
    				null, AppContext.getApplicationContext().getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), true);
        	    		    		
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
            jCheckBoxLongitudFachada = new JCheckBox(I18N.get("Expedientes", "suelo.panel.datoseconomicos.coefcorrectores.longfachada")); 
        }
        return jCheckBoxLongitudFachada;
    }
    
    /**
     * This method initializes jCheckBoxFormaIrregular  
     *  
     * @return javax.swing.JTextField   
     */
    private JCheckBox getJCheckBoxFormaIrregular()
    {
        if (jCheckBoxFormaIrregular  == null)
        {
        	jCheckBoxFormaIrregular = new JCheckBox(I18N.get("Expedientes", "suelo.panel.datoseconomicos.coefcorrectores.formairregular")); 
        }
        return jCheckBoxFormaIrregular;
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
            jLabelValorCCApreciacion = new JLabel("", JLabel.LEFT); 
            jLabelValorCCApreciacion.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "suelo.panel.datoseconomicos.coefcorrectoresconstruc.valorccapreciacion"))); 
            jLabelValorCCFincaAfectada = new JLabel("", JLabel.LEFT); 
            jLabelValorCCFincaAfectada.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "suelo.panel.datoseconomicos.coefcorrectoresconstruc.valorccfinca"))); 
            jPanelCCSueloYConstruccion = new JPanel(new GridBagLayout());
            jPanelCCSueloYConstruccion.setBorder(BorderFactory.createTitledBorder(I18N.get("Expedientes", "suelo.panel.datoseconomicos.coefcorrectoresconstruc.titulo"))); 
            jPanelCCSueloYConstruccion.add(jLabelValorCCApreciacion, 
                    new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelCCSueloYConstruccion.add(getJTextFieldValorCCApreciacion(), 
                    new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelCCSueloYConstruccion.add(jLabelValorCCFincaAfectada, 
                    new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelCCSueloYConstruccion.add(getJTextFieldValorCCFincaAfectada(), 
                    new GridBagConstraints(1, 1, 1, 1, 0, 0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            
            jPanelCCSueloYConstruccion.add(getJCheckBoxDepreciacion(), 
                    new GridBagConstraints(0, 2, 2, 1, 0, 0, GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL, new Insets (5,5,0,5), 0,0));
            jPanelCCSueloYConstruccion.add(getJCheckBoxSituacionesEspeciales(),
                    new GridBagConstraints(0, 3, 2, 1, 0, 0, GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelCCSueloYConstruccion.add(getJCheckBoxUsoNoLucrativo(),
                    new GridBagConstraints(0, 4, 2, 1, 0, 0, GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
        }
        return jPanelCCSueloYConstruccion;
    }
    
    /**
     * This method initializes jTextFieldValorCCApreciacion    
     *  
     * @return javax.swing.JTextField   
     */
    private JTextField getJTextFieldValorCCApreciacion()
    {
        if (jTextFieldValorCCApreciacion == null)
        {
            jTextFieldValorCCApreciacion = new TextField(4);
            jTextFieldValorCCApreciacion.addCaretListener(new CaretListener()
                    {
                public void caretUpdate(CaretEvent evt)
                {
                    EdicionUtils.chequeaLongYDecimalCampoEdit(jTextFieldValorCCApreciacion,4, aplicacion.getMainFrame());
                }
                    });
        }
        return jTextFieldValorCCApreciacion;
    }
    /**
     * This method initializes jTextFieldValorCCFincaAfectada    
     *  
     * @return javax.swing.JTextField   
     */
    private JTextField getJTextFieldValorCCFincaAfectada()
    {
        if (jTextFieldValorCCFincaAfectada == null)
        {
            jTextFieldValorCCFincaAfectada = new JTextField(4);
            jTextFieldValorCCFincaAfectada.addCaretListener(new CaretListener()
                    {
                public void caretUpdate(CaretEvent evt)
                {
                    EdicionUtils.chequeaLongYDecimalCampoEdit(jTextFieldValorCCFincaAfectada,4, aplicacion.getMainFrame());
                }
                    });
        }
        return jTextFieldValorCCFincaAfectada;
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
            jCheckBoxDepreciacion.setText(I18N.get("Expedientes", "suelo.panel.datoseconomicos.coefcorrectoresconstruc.depreciacion")); 
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
            jCheckBoxSituacionesEspeciales.setText(I18N.get("Expedientes", "suelo.panel.datoseconomicos.coefcorrectoresconstruc.sitespeciales")); 
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
            jCheckBoxUsoNoLucrativo.setText(I18N.get("Expedientes", "suelo.panel.datoseconomicos.coefcorrectoresconstruc.nolucrativo")); 
        }
        return jCheckBoxUsoNoLucrativo;
    }
    
    /**
     * This method initializes jPanelFachada	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanelFachada()
    {
        if (jPanelFachada == null)
        {
            jPanelFachada = new JPanel(new GridBagLayout());
            jPanelFachada.setBorder(BorderFactory.createTitledBorder(I18N.get("Expedientes", "suelo.panel.datosfisicos.fachada.titulo"))); 
            
            JLabel jLabelLongitud = new JLabel (I18N.get("Expedientes", "suelo.panel.datosfisicos.fachada.longitud")); 
            JLabel jLabelTipo = new JLabel (UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "suelo.panel.datosfisicos.fachada.tipo"))); 
            jPanelFachada.add(jLabelLongitud, new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
            jPanelFachada.add(getJTextFieldLongitudFachada(),new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelFachada.add(jLabelTipo, new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
            
            jPanelFachada.add(getJComboBoxTipodFachada(),new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            
        }
        return jPanelFachada;
    }
    
    private JPanel getJPanelSuperficieFondo()
    {
    	 if (jPanelSuperficieFondo == null)
    	 {
    		 jPanelSuperficieFondo = new JPanel(new GridBagLayout());
    		     		 
    		 jLabelFondo = new JLabel("", JLabel.CENTER); 
             jLabelFondo.setText(I18N.get("Expedientes", "suelo.panel.datosfisicos.fondo")); 
             jLabelSuperficie = new JLabel("", JLabel.CENTER); 
             jLabelSuperficie.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "suelo.panel.datosfisicos.superficie")));
             
             jPanelSuperficieFondo.add(jLabelSuperficie, new GridBagConstraints(0, 0, 1, 1, 0.1, 0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
             jPanelSuperficieFondo.add(jLabelFondo, new GridBagConstraints(1, 0, 1, 1, 0.1, 0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
             jPanelSuperficieFondo.add(getJTextFieldSuperficie(), new GridBagConstraints(0, 1, 1, 1, 0.1, 0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
             jPanelSuperficieFondo.add(getJTextFieldFondo(), new GridBagConstraints(1, 1, 1, 1, 0.1, 0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
    	 }
    	 
    	 return jPanelSuperficieFondo;
    }
    
    /**
     * This method initializes jCheckBoxDesmonteExcesivo	
     * 	
     * @return javax.swing.JCheckBox	
     */
    private JCheckBox getJCheckBoxDesmonteExcesivo()
    {
        if (jCheckBoxDesmonteExcesivo == null)
        {
            jCheckBoxDesmonteExcesivo = new JCheckBox();
            jCheckBoxDesmonteExcesivo.setText(I18N.get("Expedientes", "suelo.panel.datoseconomicos.coefcorrectores.desmonte")); 
        }
        return jCheckBoxDesmonteExcesivo;
    }
    
    /**
     * This method initializes jCheckBoxSupDistintaAMinima	
     * 	
     * @return javax.swing.JCheckBox	
     */
    private JCheckBox getJCheckBoxSupDistintaAMinima()
    {
        if (jCheckBoxSupDistintaAMinima == null)
        {
            jCheckBoxSupDistintaAMinima = new JCheckBox();
            jCheckBoxSupDistintaAMinima.setText(I18N.get("Expedientes", "suelo.panel.datoseconomicos.coefcorrectores.supdistintaminima")); 
        }
        return jCheckBoxSupDistintaAMinima;
    }
    
    /**
     * This method initializes jCheckBoxInedificabilidadTemporal	
     * 	
     * @return javax.swing.JCheckBox	
     */
    private JCheckBox getJCheckBoxInedificabilidadTemporal()
    {
        if (jCheckBoxInedificabilidadTemporal == null)
        {
            jCheckBoxInedificabilidadTemporal = new JCheckBox();
            jCheckBoxInedificabilidadTemporal.setText(I18N.get("Expedientes", "suelo.panel.datoseconomicos.coefcorrectores.inedificabilidad")); 
        }
        return jCheckBoxInedificabilidadTemporal;
    }
    
    /**
     * This method initializes jCheckBoxReservadoVPO	
     * 	
     * @return javax.swing.JCheckBox	
     */
    private JCheckBox getJCheckBoxReservadoVPO()
    {
        if (jCheckBoxReservadoVPO == null)
        {
            jCheckBoxReservadoVPO = new JCheckBox();
            jCheckBoxReservadoVPO.setText(I18N.get("Expedientes", "suelo.panel.datoseconomicos.coefcorrectores.reservadoVPP")); 
        }
        return jCheckBoxReservadoVPO;
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
            jPanelDatosPonencia.setBorder(BorderFactory.createTitledBorder("* "+I18N.get("Expedientes", "suelo.panel.datoseconomicos.datosponencia.titulo"))); 
            
            jLabelCodigoTipoValor = new JLabel(I18N.get("Expedientes", "suelo.panel.datoseconomicos.datosponencia.codtipovalor"));             
            jPanelDatosPonencia.add(jLabelCodigoTipoValor,
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER,GridBagConstraints.BOTH, new Insets (0,5,0,5), 0,0));
            
            jPanelDatosPonencia.add(getJComboBoxCodigoTipoValor(),
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
                        
            jLabelZonaValor = new JLabel(I18N.get("Expedientes", "suelo.panel.datoseconomicos.datosponencia.zonavalor")); 
            jLabelZonaUrbanistica = new JLabel(I18N.get("Expedientes", "suelo.panel.datoseconomicos.datosponencia.zonaurbanistica"));
            jPanelDatosPonencia.add(jLabelZonaValor, 
                    new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosPonencia.add(jLabelZonaUrbanistica, 
                    new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1, GridBagConstraints.EAST,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosPonencia.add(getJComboBoxZonaValor(), 
                    new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1, GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosPonencia.add(getJComboBoxZonaUrbanistica(), 
                    new GridBagConstraints(1, 2, 1, 1, 0.1, 0.1, GridBagConstraints.EAST,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            
            jLabelCodigoVia = new JLabel(I18N.get("Expedientes", "suelo.panel.datoseconomicos.datosponencia.codvia")); 
            jLabelTramo = new JLabel(I18N.get("Expedientes", "suelo.panel.datoseconomicos.datosponencia.tramo")); 
            jPanelDatosPonencia.add(jLabelCodigoVia, 
                    new GridBagConstraints(2, 1, 1, 1, 0.1,0.1, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosPonencia.add(jLabelTramo, 
                    new GridBagConstraints(4, 1, 1, 1, 0.1,0.1, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            
            jPanelDatosPonencia.add(getJTextFieldCodigoVia(), 
                    new GridBagConstraints(2, 2, 1, 1, 0.1,0.1, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosPonencia.add(getJButtonBuscarCodigoVia(), 
                    new GridBagConstraints(3, 2, 1, 1, 0.1,0.1, GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets (0,5,0,5), 0,0));
            
            jPanelDatosPonencia.add(getJComboBoxTramo(), 
                    new GridBagConstraints(4, 2, 1, 1, 0.1,0.1, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
                                    
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
        if (jComboBoxZonaValor   == null)
        {
        	jComboBoxZonaValor = new JComboBox(); 
        	
        }

    	 jComboBoxZonaValor.addActionListener(new ListenerCombo());
        return jComboBoxZonaValor;        
    }
    
    /**
     * This method initializes jTextFieldZonaUrbanistica   
     *  
     * @return javax.swing.JTextField   
     */
    
    private JComboBox getJComboBoxZonaUrbanistica()
    { 
        if (jComboBoxZonaUrbanistica  == null)
        {
        	jComboBoxZonaUrbanistica = new JComboBox();  
        	jComboBoxZonaUrbanistica.setRenderer(new EstructuraDBListCellRenderer());
        	jComboBoxZonaUrbanistica.setKeySelectionManager(new ComboBoxKeyTipoDestinoSelectionManager());
        }
        return jComboBoxZonaUrbanistica;        
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
     * This method initializes jComboBoxCodigoTipoValor   
     *  
     * @return ComboBoxEstructuras   
     */
    private ComboBoxEstructuras getJComboBoxCodigoTipoValor()
    { 
        if (jComboBoxCodigoTipoValor == null)
        {
            Estructuras.cargarEstructura("Tipo de valor de suelo");
            jComboBoxCodigoTipoValor = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), true);
        
            jComboBoxCodigoTipoValor.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxCodigoTipoValor;        
    }
    /**
     * This method initializes jTextFieldTramo   
     *  
     * @return javax.swing.JTextField   
     */
   
    private JComboBox getJComboBoxTramo()
    { 
       if (jComboBoxTramo   == null)
        {
        	jComboBoxTramo = new JComboBox();   
        	jComboBoxTramo.setRenderer(new EstructuraDBListCellRenderer());
        	jComboBoxTramo.setKeySelectionManager(new ComboBoxKeyTipoDestinoSelectionManager());
        	
        }

        jComboBoxTramo.addActionListener(new ListenerCombo());
        return jComboBoxTramo;        
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
            
        	jPanelBotonera.add(getJButtonAniadirSuelo(), 
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 3, 0, 0), 0, 0));
            
        	jPanelBotonera.add(getJButtonNuevoSuelo(), 
                    new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 3, 0, 0), 0, 0));
            
        	jPanelBotonera.add(getJButtonEliminarSuelo(), 
                    new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 3, 0, 0), 0, 0));
            
        	jPanelBotonera.add(new JPanel(),
                    new GridBagConstraints(0, 0, 1, 1, 1, 1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 0, 5, 0), 0, 0));        
        }
        return jPanelBotonera;
    }
	
	private JButton getJButtonNuevoSuelo()
    {
        if (jButtonNuevoSuelo    == null)
        {
        	jButtonNuevoSuelo = new JButton();
        	jButtonNuevoSuelo.setText(I18N.get("Expedientes", "suelo.panel.boton.nuevosuelo"));
        	

        	jButtonNuevoSuelo.addActionListener(new java.awt.event.ActionListener()
            {
            	public void actionPerformed(java.awt.event.ActionEvent e)
            	{
                    getSuelo();
                    suelo = new SueloCatastro();
                    SueloPanel.this.getJTextFieldPCatastral1().setText(fincaActual.getRefFinca().getRefCatastral1());
            		SueloPanel.this.getJTextFieldPCatastral2().setText(fincaActual.getRefFinca().getRefCatastral2());
                    SueloPanel.this.getJTextFieldPCatastral1().setEnabled(false);
            		SueloPanel.this.getJTextFieldPCatastral2().setEnabled(false);
            		EdicionUtils.clearPanel(SueloPanel.this);     
            		SueloPanel.this.getJButtonAniadirSuelo().setEnabled(true);
            	}
            });
        }
        return jButtonNuevoSuelo;
        
        
    }
	
	private JButton getJButtonEliminarSuelo()
    {
        if (jButtonEliminarSuelo   == null)
        {
        	jButtonEliminarSuelo = new JButton();
        	jButtonEliminarSuelo.setText(I18N.get("Expedientes", "suelo.panel.boton.eliminarsuelo"));
        	
        	jButtonEliminarSuelo.addActionListener(new java.awt.event.ActionListener()
            {
            	public void actionPerformed(java.awt.event.ActionEvent e)
            	{
                    ArrayList lstSuelos = fincaActual.getLstSuelos();
                    if(lstSuelos!=null && lstSuelos.size()>0)
                    {
                        if (JOptionPane.showConfirmDialog(
                                (Component) SueloPanel.this,
                                I18N.get("Expedientes","suelo.panel.removesueloquestion"),
                                I18N.get("Expedientes","suelo.panel.removesuelo"),
                                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                        {
                            eliminaSuelo();
                        }
                    }
            	}
            });
        }
        return jButtonEliminarSuelo;
    }
    
	private JButton getJButtonBuscarCodigoVia()
    {
        if (jButtonBuscarCodigoVia   == null)
        {
        	jButtonBuscarCodigoVia = new JButton();
        	jButtonBuscarCodigoVia.setIcon(IconLoader.icon(GestionExpedienteConst.ICONO_BUSCAR));
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
        }
        return jButtonBuscarCodigoVia;
    }
	
	private JButton getJButtonAniadirSuelo()
    {
        if (jButtonAniadirSuelo     == null)
        {
        	jButtonAniadirSuelo = new JButton();
        	jButtonAniadirSuelo.setText(I18N.get("Expedientes", "suelo.panel.boton.aniadirsuelo"));
        	SueloPanel.this.getJButtonAniadirSuelo().setEnabled(false);
        	
        	jButtonAniadirSuelo.addActionListener(new java.awt.event.ActionListener()
            {
            	public void actionPerformed(java.awt.event.ActionEvent e)
            	{
                    annadeSuelo();
            	}
            });
        }
        return jButtonAniadirSuelo;
    }

    public void asignaArbol(ExpedientePanelTree panelExpedientes)
    {
        this.panelExpedientes = panelExpedientes;
        arbol= panelExpedientes.getTree();
    }
    
    public void asignaArbolPanel(Object panel)
    {
    	if (panel instanceof com.geopista.ui.plugin.infcatfisicoeconomico.paneles.InfoCatastralPanelTree) {
			arbol= ((com.geopista.ui.plugin.infcatfisicoeconomico.paneles.InfoCatastralPanelTree)panel).getTree();
		}
    	else if (panel instanceof com.geopista.ui.plugin.infcatfisicoeconomico.paneles.InfoCatastralPanelTree){
    		arbol= ((com.geopista.ui.plugin.infcatfisicoeconomico.paneles.InfoCatastralPanelTree)panel).getTree();
    	}
    }

    private void annadeSuelo()
    {
    	if(this.datosMinimosYCorrectosSuelo()){
	        getSuelo();
	        ArrayList lstSuelos = fincaActual.getLstSuelos();
	        boolean existeONoCorrecta = false;
	        if(lstSuelos==null)
	        {
	            lstSuelos = new ArrayList();
	            fincaActual.setLstSuelos(lstSuelos);
	        }
	        for(int i = 0; i< lstSuelos.size() && !existeONoCorrecta;i++)
	        {
	            SueloCatastro sue = (SueloCatastro)lstSuelos.get(i);
	            if((suelo.getNumOrden()!=null
	            &&suelo.getNumOrden().equalsIgnoreCase(sue.getNumOrden())))
	            {
	                existeONoCorrecta = true;
	            }
	        }
	        if(existeONoCorrecta || (suelo.getNumOrden()==null ||(suelo.getNumOrden()!=null
	            &&suelo.getNumOrden().equalsIgnoreCase(""))))
	        {
	            JOptionPane.showMessageDialog(this,I18N.get("Expedientes", "Error.J4"));
	            return;
	        }
	        lstSuelos.add(suelo);
	        DefaultMutableTreeNode node = (DefaultMutableTreeNode) arbol.getLastSelectedPathComponent();
	        Object nodeInfo = node.getUserObject();
	        if(nodeInfo instanceof SueloCatastro)
	        {
	            node = (DefaultMutableTreeNode)arbol.getSelectionPath().getParentPath().getLastPathComponent();
	        }
	        HideableNode nodo = panelExpedientes.addObject(node,suelo,true);
	        TreePath path = new TreePath(nodo.getPath());
	        arbol.setSelectionPath(path);
	        SueloPanel.this.getJButtonAniadirSuelo().setEnabled(false);
    	}else{
    		JOptionPane.showMessageDialog(this,I18N.get("Expedientes", "Catastro.Intercambio.Edicion.Dialogs.msgDatosMinimosYCorrectos"));
    	}
    }

    private void eliminaSuelo()
    {
        ArrayList lstSuelos = fincaActual.getLstSuelos();
        boolean continua = true;
        for(int i = 0; i< lstSuelos.size() && continua;i++)
        {
            SueloCatastro  sue = (SueloCatastro)lstSuelos.get(i);
            if(suelo.getNumOrden()!=null
            &&suelo.getNumOrden().equalsIgnoreCase(sue.getNumOrden()))
            {
                lstSuelos.remove(i);
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

    public void setSuelo(SueloCatastro suelo)
    {
        this.suelo = suelo;
    }

    public boolean datosMinimosYCorrectosSuelo()
    {
    	boolean okPonencia = false;   
    	if((jComboBoxTramo!=null && jComboBoxTramo.getSelectedItem()!=null && jComboBoxTramo.getSelectedIndex()>0)
				&& (jTextFieldCodigoVia.getText()!=null && !jTextFieldCodigoVia.getText().equalsIgnoreCase(""))){    		
        		okPonencia=true;
    	}else{
	    	if((jComboBoxTramo==null || jComboBoxTramo.getSelectedItem()==null || jComboBoxTramo.getSelectedIndex()<=0)
					&& (jTextFieldCodigoVia.getText()==null || jTextFieldCodigoVia.getText().equalsIgnoreCase(""))
					&& (jComboBoxZonaValor!=null && jComboBoxZonaValor.getSelectedItem()!=null && jComboBoxZonaValor.getSelectedIndex()>0)){
	    		okPonencia=true;
	    	}
    	}
        return  okPonencia && (jTextFieldPCatastral1.getText()!=null && !jTextFieldPCatastral1.getText().equalsIgnoreCase("")) &&
        		(jTextFieldPCatastral2.getText()!=null && !jTextFieldPCatastral2.getText().equalsIgnoreCase("")) &&
        		(jTextFieldSuperficie.getText()!=null && !jTextFieldSuperficie.getText().equalsIgnoreCase("")) &&
        		(jTextFieldSubparcela.getText()!=null && !jTextFieldSubparcela.getText().equalsIgnoreCase("")) &&
        		(jTextFieldValorCCApreciacion.getText()!=null && !jTextFieldValorCCApreciacion.getText().equalsIgnoreCase("")) &&
        		(jTextFieldValorCCFincaAfectada.getText()!=null && !jTextFieldValorCCFincaAfectada.getText().equalsIgnoreCase("")) &&
        		(jComboBoxZonaUrbanistica!=null && jComboBoxZonaUrbanistica.getSelectedItem()!=null && jComboBoxZonaUrbanistica.getSelectedIndex()>0) &&
        		(jComboBoxCodigoTipoValor!=null && jComboBoxCodigoTipoValor.getSelectedItem()!=null && jComboBoxCodigoTipoValor.getSelectedIndex()>0) &&
        		(jComboBoxNumeroFachadas!=null && jComboBoxNumeroFachadas.getSelectedItem()!=null && jComboBoxNumeroFachadas.getSelectedIndex()>0) &&
        		(jComboBoxTipoFachada!=null && jComboBoxTipoFachada.getSelectedItem()!=null && jComboBoxTipoFachada.getSelectedIndex()>0);
    }

	public void cleanup() {
		// TODO Auto-generated method stub
		if (jPanelDatosSuelo!=null){jPanelDatosSuelo.removeAll();}
		if (jPanelDatosPonencia!=null){jPanelDatosPonencia.removeAll();}
		if (jPanelDatosFisicos!=null){jPanelDatosFisicos.removeAll();}
		if (jPanelDatosEconomicos!=null){jPanelDatosEconomicos.removeAll();}
		if (jPanelCCSuelo!=null){jPanelCCSuelo.removeAll();}		
		if (jPanelCCSueloYConstruccion!=null){jPanelCCSueloYConstruccion.removeAll();}	
		if (jPanelFachada!=null){jPanelFachada.removeAll();}	
		if (jPanelSuperficieFondo!=null){jPanelSuperficieFondo.removeAll();}
		if (jPanelBotonera!=null){jPanelBotonera.removeAll();}

	    jTextFieldSubparcela = null;
	    jTextFieldSuperficie = null;
	    jTextFieldFondo = null;
	    jTextFieldLongitudFachada = null;
	    jTextFieldValorCCApreciacion = null;
	    jTextFieldValorCCFincaAfectada=null;
	    jTextFieldCodigoVia=null;
	    jTextFieldPCatastral2=null;
	    jTextFieldPCatastral1=null;

		if (jComboBoxTramo!=null){jComboBoxTramo.removeAll();jComboBoxTramo=null;}
		if (jComboBoxZonaUrbanistica!=null){jComboBoxZonaUrbanistica.removeAll();jComboBoxZonaUrbanistica=null;}
		if (jComboBoxZonaValor!=null){jComboBoxZonaValor.removeAll();jComboBoxZonaValor=null;}
		
	}
}  //  @jve:decl-index=0:visual-constraint="3,-86"
