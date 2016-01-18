/**
 * BienInmuebleExtendedInfoPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.intercambio.edicion.dialogs;


import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JButton;
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
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.catastro.intercambio.edicion.BienInmuebleExtendedInfoDialog;
import com.geopista.app.catastro.intercambio.edicion.CodigoRegistroDialog;
import com.geopista.app.catastro.intercambio.edicion.ParajesSistemaDialog;
import com.geopista.app.catastro.intercambio.edicion.ViasSistemaDialog;
import com.geopista.app.catastro.intercambio.edicion.utils.EdicionOperations;
import com.geopista.app.catastro.intercambio.edicion.utils.EdicionUtils;
import com.geopista.app.catastro.intercambio.edicion.utils.EstructuraDBListCellRenderer;
import com.geopista.app.catastro.intercambio.edicion.utils.UbicacionListCellRenderer;
import com.geopista.app.catastro.intercambio.images.IconLoader;
import com.geopista.app.catastro.model.beans.BienInmuebleCatastro;
import com.geopista.app.catastro.model.beans.BienInmuebleJuridico;
import com.geopista.app.catastro.model.beans.ConstantesRegExp;
import com.geopista.app.catastro.model.beans.DireccionLocalizacion;
import com.geopista.app.catastro.model.beans.EstructuraDB;
import com.geopista.app.catastro.model.beans.IdBienInmueble;
import com.geopista.app.catastro.model.beans.Municipio;
import com.geopista.app.catastro.model.beans.NumeroFincaRegistral;
import com.geopista.app.catastro.model.beans.Paraje;
import com.geopista.app.catastro.model.beans.Provincia;
import com.geopista.app.catastro.model.beans.TipoExpediente;
import com.geopista.app.catastro.registroExpedientes.utils.UtilRegistroExp;
import com.geopista.app.catastro.utils.ConstantesCatastro;
import com.geopista.app.utilidades.TextField;
import com.geopista.app.utilidades.comboBoxTipo.ComboBoxKeyTipoDestinoSelectionManager;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.geopista.protocol.administrador.dominios.DomainNode;
import com.geopista.util.GeopistaUtil;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.Blackboard;


public class BienInmuebleExtendedInfoPanel extends JPanel
{
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1L;
    
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    private Blackboard Identificadores = aplicacion.getBlackboard();
  
    
    private JPanel jPanelDatosIdentificacion = null;
    private JPanel jPanelDatosLocalizacion = null;
    private JPanel jPanelDatosEconomicos = null;
    private JLabel jLabelRefCatastral1 = null;
    private JLabel jLabelRefCatastral2 = null;
    private JTextField jTextFieldRefCatastral1 = null;
    private JTextField jTextFieldRefCatastral2 = null;
    private JPanel jPanelFincaRegistral = null;
    private JComboBox jComboCodProvincia = null;
    private JPanel jPanelUrbana = null;
    private JPanel jPanelRustica = null;
    private JLabel jLabelTipoVia = null;
    private JLabel jLabelCodigoVia = null;
    private JTextField jTextFieldCodigoVia = null;
    private JLabel jLabelDireccion = null;
    private ComboBoxEstructuras jComboBoxTipoVia = null;
    private JTextField jTextFieldDireccion = null;
    private JButton jButtonBuscarDireccion = null;
    private JLabel jLabelNumero = null;
    private JLabel jLabelLetra = null;
    private JLabel jLabelNumeroD = null;
    private JLabel jLabelLetraD = null;
    private JLabel jLabelBloque = null;
    private JLabel jLabelEscalera = null;
    private JLabel jLabelPuerta = null;
    private JTextField jTextFieldNumero = null;
    private JTextField jTextFieldLetra = null;
    private JTextField jTextFieldNumeroD = null;
    private JTextField jTextFieldLetraD = null;
    private JTextField jTextFieldBloque = null;
    private JLabel jLabelPoligono = null;
    private JLabel jLabelParcela = null;
    private JLabel jLabelParaje = null;
    private JLabel jLabelZona = null;
    private JLabel jLabelMunicipioAgregado = null;
    private JTextField jTextFieldPoligono = null;
    private JTextField jTextFieldParcela = null;
    private JTextField jTextFieldParaje = null;
    private JButton jButtonBuscarParaje = null;
    private JTextField jTextFieldZona = null;
    private JComboBox jComboBoxMunicipioAgregado = null;
    private JTextField jTextFieldSolar = null;
    private JTextField jTextFieldConstruidaTotal = null;
    private JTextField jTextFieldSobreRasante = null;
    private JTextField jTextFieldCubierta = null;
    private JTextField jTextFieldBajoRasante = null;
    private JLabel jLabelCodProvincia = null;
    private JLabel jLabelCargo = null;
    private JLabel jLabelDC1 = null;
    private JLabel jLabelDC2 = null;
    private JTextField jTextFieldCargo = null;
    private JTextField jTextFieldDC1 = null;
    private JTextField jTextFieldDC2 = null;
    private JLabel jLabelPlanta = null;
    private JLabel jLabelDirNoEstructurada = null;
    private JLabel jLabelKm = null;
    private JTextField jTextFieldDirNoEstructurada = null;
    private JTextField jTextFieldKm = null;
    private JLabel jLabelNumOrdenDH = null;
    private JLabel jLabelCoefParticipacion = null;
    private JLabel jLabelAnioFin = null;
    private JLabel jLabelPrecioAdmVenta = null;
    private JTextField jTextFieldNumOrdenDH = null;
    private JTextField jTextFieldCoefParticipacion = null;
    private JTextField jTextFieldAnioFin = null;
    private JTextField jTextFieldPrecioAdmVenta = null;
    private JLabel jLabelOrigenValorDeclarado = null;
    private JLabel jLabelPrecioDeclarado = null;
    private JTextField jTextFieldOrigenValorDeclarado = null;
    private JTextField jTextFieldPrecioDeclarado = null;
    private JPanel jPanelDatosBaseLiquidable = null;
    private JLabel jLabelOrigenValorBase = null;
    private ComboBoxEstructuras jComboBoxOrigenValorBase = null;
    private JLabel jLabelImporteValorBase = null;
    private JTextField jTextFieldImporteValorBase = null;
    private JTextField jTextFieldNumRegistro = null;
    private JTextField jTextFieldCodRegistro = null;
    private JLabel jLabelCodRegistro = null;
    private JLabel jLabelNumRegistro = null;
    private boolean okPressed = false;
	private JButton jButtonCodigoRegistro = null;
	private JLabel jLabelTipoPropiedad = null;
	private JLabel jLabelUso = null;
	private ComboBoxEstructuras jComboBoxTipoPropiedad = null;
	private JComboBox jComboBoxUso = null;
	private JComboBox jComboBoxEscalera = null;
	private JComboBox jComboBoxPlanta = null;
	private JComboBox jComboBoxPuerta = null;
	private JButton jButtonAnioFin = null;
	
	private Paraje paraje = null;

	private TipoExpediente tipoExpediente = null;

	private JLabel jLabelClase = null;

	private ComboBoxEstructuras jComboBoxClase = null;
    
    /**
     * This method initializes
     * 
     */
    public BienInmuebleExtendedInfoPanel()
    {
        super();
        initialize();
    }
    
    public BienInmuebleExtendedInfoPanel(BienInmuebleCatastro bi)
    {
        super();
        initialize();
        loadData (bi);
    }
    
    public class ListenerTextField implements  DocumentListener{
    	/*
    	 * Método que recibe evento cuando se escribe en alguno de los jTextField que tienen un escuchador DocumentListener
    	 * Según el campo origen del evento, cambia las etiquetas obligatorias.
    	 * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
    	 */
        public void insertUpdate(DocumentEvent e){
        	//Panel Base Liquidable
        	if(jTextFieldImporteValorBase.getDocument()==e.getDocument()){
        		jLabelImporteValorBase.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes","bieninmueble.extended.liquidable.importe")));         
                jLabelOrigenValorBase.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes","bieninmueble.extended.liquidable.origen")));    
        	}
        	//Panel rústica obligatoria
			if(jTextFieldParaje.getDocument()==e.getDocument() || jTextFieldParcela.getDocument()==e.getDocument() || jTextFieldZona.getDocument()==e.getDocument() || jTextFieldPoligono.getDocument()==e.getDocument()){
				jLabelParcela.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "bieninmueble.extended.rustica.parcela")));			            
			    jLabelPoligono.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "bieninmueble.extended.rustica.poligono")));
			    if(jTextFieldDireccion.getDocument().getLength()<=0 && jComboBoxTipoVia.getSelectedIndex()<=0 && jTextFieldNumero.getDocument().getLength()<=0 && 
			    		jTextFieldLetra.getDocument().getLength()<=0 && jTextFieldNumeroD.getDocument().getLength()<=0 && jTextFieldLetraD.getDocument().getLength()<=0 && 
			    		jTextFieldBloque.getDocument().getLength()<=0 && jTextFieldDirNoEstructurada.getDocument().getLength()<=0 && jTextFieldKm.getDocument().getLength()<=0 && 
			    		jComboBoxEscalera.getSelectedIndex()<=0 && jComboBoxPlanta.getSelectedIndex()<=0 && jComboBoxPuerta.getSelectedIndex()<=0 &&
			    		jTextFieldCodigoVia.getDocument().getLength()<=0)
		        	jLabelParaje.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "bieninmueble.extended.rustica.paraje")));
			    jPanelDatosLocalizacion.setBorder(BorderFactory.createTitledBorder
	                    (null, I18N.get("Expedientes", "bieninmueble.extended.localizacion.titulo"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
			}
			//Panel Finca Registral
			if(jTextFieldNumRegistro.getDocument()==e.getDocument() || jTextFieldCodRegistro.getDocument()==e.getDocument()){
				jLabelCodProvincia.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes","bieninmueble.extended.fincaregistral.codprovincia")));
				jLabelCodRegistro.setText(I18N.get("Expedientes","bieninmueble.extended.fincaregistral.codregistro"));
		        jLabelNumRegistro.setText(I18N.get("Expedientes","bieninmueble.extended.fincaregistral.numregistro"));
		    }
			//Panel Urbana obligatoria
			if(jTextFieldDireccion.getDocument()==e.getDocument() || jTextFieldNumero.getDocument()==e.getDocument() || jTextFieldLetra.getDocument()==e.getDocument() || 
					jTextFieldNumeroD.getDocument()==e.getDocument() || jTextFieldLetraD.getDocument()==e.getDocument() || jTextFieldBloque.getDocument()==e.getDocument() || 
					jTextFieldDirNoEstructurada.getDocument()==e.getDocument() || jTextFieldKm.getDocument()==e.getDocument() || jTextFieldCodigoVia.getDocument()==e.getDocument()){
				jLabelTipoVia.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "bieninmueble.extended.urbana.tipovia")));
                jLabelDireccion.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "bieninmueble.extended.urbana.direccion")));
                jLabelCodigoVia.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "bieninmueble.extended.urbana.codigovia")));
                jLabelParaje.setText(I18N.get("Expedientes", "bieninmueble.extended.rustica.paraje"));
                jPanelDatosLocalizacion.setBorder(BorderFactory.createTitledBorder
	                    (null, I18N.get("Expedientes", "bieninmueble.extended.localizacion.titulo"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
            }
        }
    	/*
    	 * Método que recibe evento cuando se borra caracteres en alguno de los jTextField que tienen un escuchador DocumentListener
    	 * Según el campo origen del evento, cambia las etiquetas obligatorias.
    	 * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
    	 */
        public void removeUpdate(DocumentEvent e){
        	//Panel Base Liquidable
        	if(jTextFieldImporteValorBase.getDocument().getLength()<=0 && jComboBoxOrigenValorBase.getSelectedIndex()<=0){
        		jLabelImporteValorBase.setText(I18N.get("Expedientes","bieninmueble.extended.liquidable.importe"));         
                jLabelOrigenValorBase.setText(I18N.get("Expedientes","bieninmueble.extended.liquidable.origen"));    
        	}
        	//Panel rústica obligatoria
        	if(jTextFieldParaje.getDocument().getLength()<=0 && jTextFieldParcela.getDocument().getLength()<=0 && jComboBoxMunicipioAgregado.getSelectedIndex()<=0 && jTextFieldPoligono.getDocument().getLength()<=0 && jTextFieldZona.getDocument().getLength()<=0){
        		jLabelParaje.setText(I18N.get("Expedientes", "bieninmueble.extended.rustica.paraje"));
                jLabelParcela.setText(I18N.get("Expedientes", "bieninmueble.extended.rustica.parcela"));                
	            jLabelPoligono.setText(I18N.get("Expedientes", "bieninmueble.extended.rustica.poligono"));	           
        	}
			//Panel Finca Registral
        	if(jComboCodProvincia.getSelectedIndex()<=0 && jTextFieldCodRegistro.getDocument().getLength()<=0 && jTextFieldNumRegistro.getDocument().getLength()<=0){
        		jLabelCodProvincia.setText(I18N.get("Expedientes","bieninmueble.extended.fincaregistral.codprovincia"));
				jLabelCodRegistro.setText(I18N.get("Expedientes","bieninmueble.extended.fincaregistral.codregistro"));
		        jLabelNumRegistro.setText(I18N.get("Expedientes","bieninmueble.extended.fincaregistral.numregistro"));
			}
			//Panel Urbana obligatoria
			if(jTextFieldDireccion.getDocument().getLength()<=0 && jComboBoxTipoVia.getSelectedIndex()<=0 && jTextFieldNumero.getDocument().getLength()<=0 && 
					jTextFieldLetra.getDocument().getLength()<=0 && jTextFieldNumeroD.getDocument().getLength()<=0 && jTextFieldLetraD.getDocument().getLength()<=0 && 
					jTextFieldBloque.getDocument().getLength()<=0 && jTextFieldDirNoEstructurada.getDocument().getLength()<=0 && jTextFieldKm.getDocument().getLength()<=0 && 
					jComboBoxEscalera.getSelectedIndex()<=0 && jComboBoxPlanta.getSelectedIndex()<=0 && jComboBoxPuerta.getSelectedIndex()<=0 && jTextFieldCodigoVia.getDocument().getLength()<=0){
				jLabelTipoVia.setText(I18N.get("Expedientes", "bieninmueble.extended.urbana.tipovia"));
                jLabelDireccion.setText(I18N.get("Expedientes", "bieninmueble.extended.urbana.direccion"));
                jLabelCodigoVia.setText(I18N.get("Expedientes", "bieninmueble.extended.urbana.codigovia"));
        	    if(jTextFieldParaje.getDocument().getLength()>0 || jTextFieldParcela.getDocument().getLength()>0 || jTextFieldZona.getDocument().getLength()>0 || jTextFieldPoligono.getDocument().getLength()>0 || jComboBoxMunicipioAgregado.getSelectedIndex()>0){
	    				jLabelParcela.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "bieninmueble.extended.rustica.parcela")));			            
	    			    jLabelPoligono.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "bieninmueble.extended.rustica.poligono")));	    			   
	    		        jLabelParaje.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "bieninmueble.extended.rustica.paraje")));
	    		}else{
	    			 jPanelDatosLocalizacion.setBorder(BorderFactory.createTitledBorder
     	                    (null, "* "+I18N.get("Expedientes", "bieninmueble.extended.localizacion.titulo"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
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
        	//Panel Base Liquidable
        	if(jComboBoxOrigenValorBase.getSelectedIndex()>0 || jTextFieldImporteValorBase.getDocument().getLength()>0){
                jLabelImporteValorBase.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes","bieninmueble.extended.liquidable.importe")));         
                jLabelOrigenValorBase.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes","bieninmueble.extended.liquidable.origen")));    
        	}else{
        		jLabelImporteValorBase.setText(I18N.get("Expedientes","bieninmueble.extended.liquidable.importe"));         
                jLabelOrigenValorBase.setText(I18N.get("Expedientes","bieninmueble.extended.liquidable.origen"));    
        	}
        	//Panel Rústica obligatoria
        	if(jComboBoxMunicipioAgregado.getSelectedIndex()>0 || jTextFieldPoligono.getDocument().getLength()>0 || jTextFieldParaje.getDocument().getLength()>0 || jTextFieldParcela.getDocument().getLength()>0 || jTextFieldZona.getDocument().getLength()>0){        		
	            jLabelParcela.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "bieninmueble.extended.rustica.parcela")));	            
	            jLabelPoligono.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "bieninmueble.extended.rustica.poligono")));
	            if(jTextFieldDireccion.getDocument().getLength()<=0 && jComboBoxTipoVia.getSelectedIndex()<=0 && 
	            		jTextFieldNumero.getDocument().getLength()<=0 && jTextFieldLetra.getDocument().getLength()<=0 && jTextFieldNumeroD.getDocument().getLength()<=0 && 
	            		jTextFieldLetraD.getDocument().getLength()<=0 && jTextFieldBloque.getDocument().getLength()<=0 && jTextFieldDirNoEstructurada.getDocument().getLength()<=0 && 
	            		jTextFieldKm.getDocument().getLength()<=0 && jComboBoxEscalera.getSelectedIndex()<=0 && jComboBoxPlanta.getSelectedIndex()<=0 && jComboBoxPuerta.getSelectedIndex()<=0 && jTextFieldCodigoVia.getDocument().getLength()<=0)
	            	jLabelParaje.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "bieninmueble.extended.rustica.paraje")));
	            jPanelDatosLocalizacion.setBorder(BorderFactory.createTitledBorder
	                    (null, I18N.get("Expedientes", "bieninmueble.extended.localizacion.titulo"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 

        	}else{
        		jLabelParaje.setText(I18N.get("Expedientes", "bieninmueble.extended.rustica.paraje"));
                jLabelParcela.setText(I18N.get("Expedientes", "bieninmueble.extended.rustica.parcela"));                 
	            jLabelPoligono.setText(I18N.get("Expedientes", "bieninmueble.extended.rustica.poligono"));
        	}
        	//Panel Finca Registral
        	if(jComboCodProvincia.getSelectedIndex()>0 || jTextFieldCodRegistro.getDocument().getLength()>0 || jTextFieldNumRegistro.getDocument().getLength()>0){
        		jLabelCodProvincia.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes","bieninmueble.extended.fincaregistral.codprovincia")));
				jLabelCodRegistro.setText(I18N.get("Expedientes","bieninmueble.extended.fincaregistral.codregistro"));
		        jLabelNumRegistro.setText(I18N.get("Expedientes","bieninmueble.extended.fincaregistral.numregistro"));
            }else{
            	jLabelCodProvincia.setText(I18N.get("Expedientes","bieninmueble.extended.fincaregistral.codprovincia"));
				jLabelCodRegistro.setText(I18N.get("Expedientes","bieninmueble.extended.fincaregistral.codregistro"));
		        jLabelNumRegistro.setText(I18N.get("Expedientes","bieninmueble.extended.fincaregistral.numregistro"));
            }
        	//Panel Urbana obligatoria
        	if(jComboBoxTipoVia.getSelectedIndex()>0 || jTextFieldDireccion.getDocument().getLength()>0 || jTextFieldNumero.getDocument().getLength()>0 || 
        			jTextFieldLetra.getDocument().getLength()>0 || jTextFieldLetraD.getDocument().getLength()>0 || jTextFieldNumeroD.getDocument().getLength()>0 ||
        			jTextFieldBloque.getDocument().getLength()>0 || jTextFieldDirNoEstructurada.getDocument().getLength()>0 || jTextFieldKm.getDocument().getLength()>0 || 
        			jComboBoxEscalera.getSelectedIndex()>0 || jComboBoxPlanta.getSelectedIndex()>0 || jComboBoxPuerta.getSelectedIndex()>0 ||
        			jTextFieldCodigoVia.getDocument().getLength()>0){
                jLabelTipoVia.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "bieninmueble.extended.urbana.tipovia")));
                jLabelDireccion.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "bieninmueble.extended.urbana.direccion")));
                jLabelCodigoVia.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "bieninmueble.extended.urbana.codigovia")));
        	    if(jTextFieldParaje.getDocument().getLength()>0 || jTextFieldParcela.getDocument().getLength()>0 || jTextFieldZona.getDocument().getLength()>0 || jTextFieldPoligono.getDocument().getLength()>0 || jComboBoxMunicipioAgregado.getSelectedIndex()>0)
        	    	jLabelParaje.setText(I18N.get("Expedientes", "bieninmueble.extended.rustica.paraje"));
        	    jPanelDatosLocalizacion.setBorder(BorderFactory.createTitledBorder
	                    (null, I18N.get("Expedientes", "bieninmueble.extended.localizacion.titulo"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
        	}else{
        		jLabelTipoVia.setText(I18N.get("Expedientes", "bieninmueble.extended.urbana.tipovia"));
                jLabelDireccion.setText(I18N.get("Expedientes", "bieninmueble.extended.urbana.direccion"));
                jLabelCodigoVia.setText(I18N.get("Expedientes", "bieninmueble.extended.urbana.codigovia"));
        	    if(jTextFieldParaje.getDocument().getLength()>0 || jTextFieldParcela.getDocument().getLength()>0 || jTextFieldZona.getDocument().getLength()>0 || jTextFieldPoligono.getDocument().getLength()>0 || jComboBoxMunicipioAgregado.getSelectedIndex()>0){
    				jLabelParcela.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "bieninmueble.extended.rustica.parcela")));			            
    			    jLabelPoligono.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "bieninmueble.extended.rustica.poligono")));	    			   
    		        jLabelParaje.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "bieninmueble.extended.rustica.paraje")));
        	    }else{
        	    	 jPanelDatosLocalizacion.setBorder(BorderFactory.createTitledBorder
     	                    (null, "* "+I18N.get("Expedientes", "bieninmueble.extended.localizacion.titulo"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
        	    }
        	}
        }
    }
    
    public void loadData(BienInmuebleCatastro bi)
    {
    	if (bi!=null){

    		//Datos identificación
    		if(bi.getIdBienInmueble().getParcelaCatastral()!=null){
    			jTextFieldRefCatastral1.setText(bi.getIdBienInmueble().getParcelaCatastral().getRefCatastral1()!=null?
    					bi.getIdBienInmueble().getParcelaCatastral().getRefCatastral1():"");
    			jTextFieldRefCatastral2.setText(bi.getIdBienInmueble().getParcelaCatastral().getRefCatastral2()!=null?
    					bi.getIdBienInmueble().getParcelaCatastral().getRefCatastral2():"");
    		}
    		else{
    			jTextFieldRefCatastral1.setText("");
    			jTextFieldRefCatastral2.setText("");
    		}
                    	
            jTextFieldCargo.setText(bi.getIdBienInmueble().getNumCargo()!=null?
            		bi.getIdBienInmueble().getNumCargo():"");
            
            jTextFieldDC1.setText(bi.getIdBienInmueble().getDigControl1()!=null?
            		bi.getIdBienInmueble().getDigControl1():"");
            
            jTextFieldDC2.setText(bi.getIdBienInmueble().getDigControl2()!=null?
            		bi.getIdBienInmueble().getDigControl2():"");
            
            jComboCodProvincia.addActionListener(new ListenerCombo());
            
            if (bi.getDomicilioTributario().getProvinciaINE()!=null){
                Provincia p = new Provincia();
                if (bi.getDomicilioTributario().getProvinciaINE().length()<2){
                    bi.getDomicilioTributario().setProvinciaINE(GeopistaUtil.completarConCeros(bi.getDomicilioTributario().getProvinciaINE(), 2));
                }
                p.setIdProvincia(bi.getDomicilioTributario().getProvinciaINE());
                p.setNombreOficial(bi.getDomicilioTributario().getNombreProvincia());
                jComboCodProvincia.setSelectedItem(p);
            }
            
            jComboBoxMunicipioAgregado.addActionListener(new ListenerCombo());
            if(bi.getDomicilioTributario().getCodMunOrigenAgregacion() != null){
                if(bi.getDomicilioTributario().getCodPoligono() != null && bi.getDomicilioTributario().getCodParcela() != null
                        && !bi.getDomicilioTributario().getCodPoligono().equals("") && !bi.getDomicilioTributario().getCodParcela().equals("") ){

                    Municipio m = new Municipio();
                    m.getProvincia().setIdProvincia(bi.getDomicilioTributario().getProvinciaINE());
                    m.setIdIne(bi.getDomicilioTributario().getMunicipioINE());
                    m.setIdCatastro(bi.getDomicilioTributario().getCodigoMunicipioDGC());
                    m.setNombreOficial(bi.getDomicilioTributario().getNombreMunicipio());

                    jComboBoxMunicipioAgregado.setSelectedItem(m);
                }
                else
                    jComboBoxMunicipioAgregado.setSelectedIndex(0);

            }
            jTextFieldNumRegistro.getDocument().addDocumentListener(new ListenerTextField());                      
            if(bi.getNumFincaRegistral()!=null){
            	
            	if(bi.getNumFincaRegistral().getSeccion()!=null 
            			&& bi.getNumFincaRegistral().getNumSubFinca()!=null 
            			&& bi.getNumFincaRegistral().getNumFinca()!=null){
            		if((bi.getNumFincaRegistral().getSeccion() + 
            				bi.getNumFincaRegistral().getNumSubFinca() + 
            				bi.getNumFincaRegistral().getNumFinca()).length()==14){
            			jTextFieldNumRegistro.setText(bi.getNumFincaRegistral().getSeccion() + 
            					bi.getNumFincaRegistral().getNumFinca() + 
            					bi.getNumFincaRegistral().getNumSubFinca());
            		}
            		else{

            			jTextFieldNumRegistro.setText("");
            		}
            	}
            	else{

            		jTextFieldNumRegistro.setText("");
            	}
            	jTextFieldCodRegistro.getDocument().addDocumentListener(new ListenerTextField()); 
            	if(bi.getNumFincaRegistral().getRegistroPropiedad()!=null){
            		if(bi.getNumFincaRegistral().getRegistroPropiedad().getCodigoRegistroPropiedad()!=null)
            			if(bi.getNumFincaRegistral().getRegistroPropiedad().getCodigoRegistroPropiedad().length()==3){
            				jTextFieldCodRegistro.setText(EdicionUtils.getStringValue(bi.getNumFincaRegistral().getRegistroPropiedad().getCodigoRegistroPropiedad()));
            			}
            			else{

            				jTextFieldCodRegistro.setText("");
            			}
            		}
	            	else{

	            		jTextFieldCodRegistro.setText("");
	            	}
            }
            else{

            	jTextFieldNumRegistro.setText("");
            	jTextFieldCodRegistro.setText("");
            }
            
            //Datos de Localización
            jComboBoxTipoVia.addActionListener(new ListenerCombo());
            if(bi.getDomicilioTributario().getTipoVia()!=null)
            	jComboBoxTipoVia.setSelectedPatron(bi.getDomicilioTributario().getTipoVia());
            else
            	jComboBoxTipoVia.setSelectedIndex(0);
            jTextFieldDireccion.getDocument().addDocumentListener(new ListenerTextField());
            if(bi.getDomicilioTributario().getCodigoVia() == -1){
            	jTextFieldCodigoVia.setText("");
            }
            else {
            	jTextFieldCodigoVia.setText(new Integer(bi.getDomicilioTributario().getCodigoVia()).toString());
            }
            jTextFieldDireccion.setText(bi.getDomicilioTributario().getNombreVia()!=null?
            		bi.getDomicilioTributario().getNombreVia():"");
            jTextFieldNumero.getDocument().addDocumentListener(new ListenerTextField());
            if (bi.getDomicilioTributario().getPrimerNumero() == -1){
            	jTextFieldNumero.setText("");
            }
            else{
            	jTextFieldNumero.setText(EdicionUtils.getStringValue(bi.getDomicilioTributario().getPrimerNumero()));
            }
            jTextFieldLetra.getDocument().addDocumentListener(new ListenerTextField());
            jTextFieldLetra.setText(bi.getDomicilioTributario().getPrimeraLetra()!=null?
            		bi.getDomicilioTributario().getPrimeraLetra():"");
            jTextFieldNumeroD.getDocument().addDocumentListener(new ListenerTextField()); 
            if (bi.getDomicilioTributario().getSegundoNumero() == -1){
            	jTextFieldNumeroD.setText("");
            }
            else{
            	jTextFieldNumeroD.setText(EdicionUtils.getStringValue(bi.getDomicilioTributario().getSegundoNumero()));
            }
            jTextFieldLetraD.getDocument().addDocumentListener(new ListenerTextField());
            jTextFieldLetraD.setText(bi.getDomicilioTributario().getSegundaLetra()!=null?
            		bi.getDomicilioTributario().getSegundaLetra():"");
            jTextFieldBloque.getDocument().addDocumentListener(new ListenerTextField());
            jTextFieldBloque.setText(bi.getDomicilioTributario().getBloque()!=null?
            		bi.getDomicilioTributario().getBloque():"");
            jComboBoxEscalera.addActionListener(new ListenerCombo());            
            EstructuraDB eEscalera = new EstructuraDB();
            if((jComboBoxEscalera.getItemCount() > 0) && (bi.getDomicilioTributario().getEscalera()!=null)){            	
            	eEscalera.setPatron(bi.getDomicilioTributario().getEscalera());
                jComboBoxEscalera.setSelectedItem(eEscalera);
            }
            else{
            	jComboBoxEscalera.setSelectedItem(eEscalera);
            }
            jComboBoxPlanta.addActionListener(new ListenerCombo());
            EstructuraDB ePlanta = new EstructuraDB();
            if((jComboBoxPlanta.getItemCount() > 0) && (bi.getDomicilioTributario().getPlanta()!=null)){            	
            	ePlanta.setPatron(bi.getDomicilioTributario().getPlanta());
                jComboBoxPlanta.setSelectedItem(ePlanta);
            }
            else{
            	jComboBoxPlanta.setSelectedItem(ePlanta);
            }
                          
            EstructuraDB ePuerta = new EstructuraDB();
            jComboBoxPuerta.addActionListener(new ListenerCombo());
            if((jComboBoxPuerta.getItemCount() > 0) && (bi.getDomicilioTributario().getPuerta()!=null)){
            	ePuerta.setPatron(bi.getDomicilioTributario().getPuerta());
            	jComboBoxPuerta.setSelectedItem(ePuerta);
            }
            else{
            	jComboBoxPuerta.setSelectedItem(ePuerta);
            }
            jTextFieldDirNoEstructurada.getDocument().addDocumentListener(new ListenerTextField());
            jTextFieldDirNoEstructurada.setText(bi.getDomicilioTributario().getDireccionNoEstructurada()!=null?
            		bi.getDomicilioTributario().getDireccionNoEstructurada():"");
            jTextFieldKm.getDocument().addDocumentListener(new ListenerTextField());
            if (bi.getDomicilioTributario().getKilometro() == -1){
            	jTextFieldKm.setText("");
            }
            else{
            	jTextFieldKm.setText(EdicionUtils.getStringValue(bi.getDomicilioTributario().getKilometro()));
            }
            
            /*jTextFieldParaje.setText(bi.getDomicilioTributario().getCodParaje()!=null?
            		bi.getDomicilioTributario().getCodParaje():"");*/
            jTextFieldParaje.getDocument().addDocumentListener(new ListenerTextField());
            jTextFieldParaje.setText(bi.getDomicilioTributario().getNombreParaje()!=null?
            		bi.getDomicilioTributario().getNombreParaje():"");
            jTextFieldPoligono.getDocument().addDocumentListener(new ListenerTextField());            
            jTextFieldPoligono.setText(bi.getDomicilioTributario().getCodPoligono()!=null?
            		bi.getDomicilioTributario().getCodPoligono():"");
            jTextFieldParcela.getDocument().addDocumentListener(new ListenerTextField());
            jTextFieldParcela.setText(bi.getDomicilioTributario().getCodParcela()!=null?
            		bi.getDomicilioTributario().getCodParcela():"");
            jTextFieldZona.getDocument().addDocumentListener(new ListenerTextField());
            jTextFieldZona.setText(bi.getDomicilioTributario().getCodZonaConcentracion()!=null?
            		bi.getDomicilioTributario().getCodZonaConcentracion():"");
                                 
            //Datos Económicos
            jTextFieldNumOrdenDH.setText(bi.getDatosEconomicosBien().getNumOrdenHorizontal()!=null?
            		bi.getDatosEconomicosBien().getNumOrdenHorizontal():"");
            
            jTextFieldCoefParticipacion.setText(EdicionUtils.getStringValue(bi.getDatosEconomicosBien().getCoefParticipacion()));
            
            jTextFieldAnioFin.setText(EdicionUtils.getStringValue(bi.getDatosEconomicosBien().getAnioFinValoracion()));
            
            
            jTextFieldPrecioAdmVenta.setText(bi.getDatosEconomicosBien().getPrecioVenta()!=null?
            		bi.getDatosEconomicosBien().getPrecioVenta().toString():"");
            
            jTextFieldOrigenValorDeclarado.setText(bi.getDatosEconomicosBien().getOrigenPrecioDeclarado()!=null?
            		bi.getDatosEconomicosBien().getOrigenPrecioDeclarado():"");
            
            jTextFieldPrecioDeclarado.setText(EdicionUtils.getStringValue(bi.getDatosEconomicosBien().getPrecioDeclarado()));
            
            //    Datos para el cálculo de la base liquidable
            jComboBoxOrigenValorBase.addActionListener(new ListenerCombo());
            if(bi.getDatosBaseLiquidable().getProcedenciaValorBase()!=null)
            	jComboBoxOrigenValorBase.setSelectedPatron(bi.getDatosBaseLiquidable().getProcedenciaValorBase());
            else
            	jComboBoxOrigenValorBase.setSelectedIndex(0);
            jTextFieldImporteValorBase.getDocument().addDocumentListener(new ListenerTextField());
            jTextFieldImporteValorBase.setText(EdicionUtils.getStringValue(bi.getDatosBaseLiquidable().getValorBase()));
                        
            EstructuraDB eUso = new EstructuraDB();
            if((jComboBoxUso.getItemCount() > 0) && (bi.getDatosEconomicosBien().getUso()!=null)){            	
            	eUso.setPatron(bi.getDatosEconomicosBien().getUso());
            	jComboBoxUso.setSelectedItem(eUso);
            }
            else{
            	jComboBoxUso.setSelectedItem(eUso);
            }
            
            if(bi.getDatosEconomicosBien().getIndTipoPropiedad()!=null)
            	jComboBoxTipoPropiedad.setSelectedPatron(bi.getDatosEconomicosBien().getIndTipoPropiedad());
            
            if(bi.getClaseBienInmueble()!=null)
            	jComboBoxClase.setSelectedPatron(EdicionUtils.getStringValue(bi.getClaseBienInmueble().trim()));

        }    
    }
    
    public void setBienModificado(BienInmuebleJuridico biJ){
    	
    	if (okPressed)
    	{
    		if(!(Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales")){
    			if(!biJ.getBienInmueble().getTipoMovimiento().equals((BienInmuebleCatastro.TIPO_MOVIMIENTO_ALTA))){
    				biJ.getBienInmueble().setTipoMovimiento(BienInmuebleCatastro.TIPO_MOVIMIENTO_MODIFICADO);
    			}
        	}
    	}
    	
    }
    
    public BienInmuebleJuridico getBienInmueble(BienInmuebleJuridico biJ){

    	BienInmuebleCatastro bi = null;
    	if (okPressed)
    	{
	    	if(biJ==null)
            {
                biJ = new BienInmuebleJuridico();
            }
            if(biJ.getBienInmueble()==null)
            {
                biJ.setBienInmueble(new BienInmuebleCatastro());
            }
            
            biJ.getBienInmueble().setElementoModificado(true);
            biJ.setElementoModificado(true);
            
            
            bi = biJ.getBienInmueble();
            //Datos identificación
	    	IdBienInmueble idBienInmueble = new IdBienInmueble();
	    	idBienInmueble.setParcelaCatastral(EdicionUtils.paddingString(jTextFieldRefCatastral1.getText(),EdicionUtils.TAM_REFCATASTRAL/2,'0', true)
	                + EdicionUtils.paddingString(jTextFieldRefCatastral2.getText(),EdicionUtils.TAM_REFCATASTRAL/2,'0', true));
	      
	        
	    	
	    	idBienInmueble.setNumCargo(GeopistaUtil.completarConCeros(jTextFieldCargo.getText(),4));	    	
	    	idBienInmueble.setDigControl1(jTextFieldDC1.getText());
	    	idBienInmueble.setDigControl2(jTextFieldDC2.getText());
	    	idBienInmueble.setIdBienInmueble(idBienInmueble.getParcelaCatastral().getRefCatastral1()+idBienInmueble.getParcelaCatastral().getRefCatastral2()+idBienInmueble.getNumCargo()+idBienInmueble.getDigControl1()+idBienInmueble.getDigControl2());
	        bi.setIdBienInmueble(idBienInmueble);

            Provincia provincia = (Provincia) jComboCodProvincia.getSelectedItem();
            // Se comenta esta linea porque el codigo Provincia tributario no depende de la provincia registral
            // bi.getDomicilioTributario().setProvinciaINE(provincia.getIdProvincia());

            NumeroFincaRegistral numFincaReg = null;
            if (jTextFieldNumRegistro.getText().length()>0 &&
                    jTextFieldCodRegistro.getText().length()>0 &&
                    provincia.getIdProvincia().length()>0){

            	String codigoProvincia = GeopistaUtil.completarConCeros(provincia.getIdProvincia(),2);
            	String numeroRegistro = GeopistaUtil.completarConCeros(jTextFieldNumRegistro.getText(), 3);
            	String numeroFincaRegistral = GeopistaUtil.completarConCeros(jTextFieldNumRegistro.getText(),14);
            	
                numFincaReg = new NumeroFincaRegistral(codigoProvincia + numeroRegistro + numeroFincaRegistral);
//                numFincaReg.getRegistroPropiedad().setCodigoRegistroPropiedad(jTextFieldCodRegistro.getText());
//                numFincaReg.getRegistroPropiedad().setCodigoProvincia(provincia.getIdProvincia());

                if (numFincaReg != null)
                    bi.setNumFincaRegistral(numFincaReg);
            }
            
            if (jComboCodProvincia.getSelectedItem()!=null){
            	if (jComboCodProvincia.getSelectedItem() instanceof Provincia){
            		bi.getDomicilioTributario().setProvinciaINE(((Provincia)jComboCodProvincia.getSelectedItem()).getIdProvincia());
            		bi.getDomicilioTributario().setNombreProvincia(((Provincia)jComboCodProvincia.getSelectedItem()).getNombreOficial());
            	}
            }

            //Datos de Localización
	        if (jComboBoxTipoVia.getSelectedPatron() != null)
	        	bi.getDomicilioTributario().setTipoVia(jComboBoxTipoVia.getSelectedPatron().toString());
	        else
	        	bi.getDomicilioTributario().setTipoVia("");
	        if(jTextFieldCodigoVia.getText().equals("")){
	        	bi.getDomicilioTributario().setCodigoVia(new Integer(-1));
	        }
	        else{
	        	bi.getDomicilioTributario().setCodigoVia(new Integer(jTextFieldCodigoVia.getText()).intValue());
	        }
            bi.getDomicilioTributario().setNombreVia(jTextFieldDireccion.getText());
	        if (jTextFieldNumero.getText().length()>0)
	        	bi.getDomicilioTributario().setPrimerNumero(Integer.parseInt(jTextFieldNumero.getText()));
	        else  if (jTextFieldNumero.getText().length() < 1)
	        	bi.getDomicilioTributario().setPrimerNumero(-1);
	        bi.getDomicilioTributario().setPrimeraLetra(jTextFieldLetra.getText());
	        if (jTextFieldNumeroD.getText().length()>0)
	        	bi.getDomicilioTributario().setSegundoNumero(Integer.parseInt(jTextFieldNumeroD.getText()));
	        else if (jTextFieldNumeroD.getText().length() < 1)
	        	bi.getDomicilioTributario().setSegundoNumero(-1);
	        bi.getDomicilioTributario().setSegundaLetra(jTextFieldLetraD.getText());
	        bi.getDomicilioTributario().setBloque(jTextFieldBloque.getText());
	        
	        if(jComboBoxEscalera.getSelectedItem()!=null)
	        	bi.getDomicilioTributario().setEscalera(((EstructuraDB)jComboBoxEscalera.getSelectedItem()).getPatron());
	        
	        if(jComboBoxPlanta.getSelectedItem()!=null)
	        	bi.getDomicilioTributario().setPlanta(((EstructuraDB)jComboBoxPlanta.getSelectedItem()).getPatron());
	        
	        if(jComboBoxPuerta.getSelectedItem()!=null)
	        	bi.getDomicilioTributario().setPuerta(((EstructuraDB)jComboBoxPuerta.getSelectedItem()).getPatron());
	        bi.getDomicilioTributario().setDireccionNoEstructurada(jTextFieldDirNoEstructurada.getText());
	        if (jTextFieldKm.getText().length()>0)
	        	bi.getDomicilioTributario().setKilometro(Double.parseDouble(jTextFieldKm.getText()));
	        else if (jTextFieldKm.getText().length() < 1)
	        	bi.getDomicilioTributario().setKilometro(-1);
	        bi.getDomicilioTributario().setCodPoligono(jTextFieldPoligono.getText());
	        bi.getDomicilioTributario().setCodParcela(jTextFieldParcela.getText());
            
	        if(paraje!=null && paraje.getCodigo()!=null)
	        	bi.getDomicilioTributario().setCodParaje(paraje.getCodigo());
	        
	        if(paraje!=null && paraje.getNombre()!=null)
	        	bi.getDomicilioTributario().setNombreParaje(paraje.getNombre());
            bi.getDomicilioTributario().setCodZonaConcentracion(jTextFieldZona.getText());

	         if (jComboBoxMunicipioAgregado.getSelectedItem()!=null){
	        	Municipio municipio = (Municipio) jComboBoxMunicipioAgregado.getSelectedItem();
	        	bi.getDomicilioTributario().setCodMunOrigenAgregacion(String.valueOf(municipio.getIdCatastro()));	        	
	        }
            

            //Datos Económicos
	        bi.getDatosEconomicosBien().setNumOrdenHorizontal(jTextFieldNumOrdenDH.getText());
	        if (jTextFieldCoefParticipacion.getText().length()>0)
	        	bi.getDatosEconomicosBien().setCoefParticipacion(Float.valueOf(jTextFieldCoefParticipacion.getText()));
	        else if (jTextFieldCoefParticipacion.getText().length() < 1)
	        	bi.getDatosEconomicosBien().setCoefParticipacion(null);

            	        
	        if(jTextFieldAnioFin.getText().length() == 4)
            {
                String fechaString = jTextFieldAnioFin.getText();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
                Date fecha=null;
                try
                {
                    fecha = formatter.parse(fechaString);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                Calendar aux= new GregorianCalendar();
                aux.setTime(fecha);
                int anno = aux.get(Calendar.YEAR);
                bi.getDatosEconomicosBien().setAnioFinValoracion(new Integer(anno));    
            }
            else 
	        	bi.getDatosEconomicosBien().setAnioFinValoracion(null);
	        
            if (jTextFieldPrecioAdmVenta.getText().length()>0)
            	bi.getDatosEconomicosBien().setPrecioVenta(Double.valueOf(jTextFieldPrecioAdmVenta.getText()));
            else if (jTextFieldPrecioAdmVenta.getText().length() < 1)
	        	bi.getDatosEconomicosBien().setPrecioVenta(null);            
            if (jTextFieldOrigenValorDeclarado.getText().length()>0)
            	bi.getDatosEconomicosBien().setOrigenPrecioDeclarado(jTextFieldOrigenValorDeclarado.getText());
	        if (jTextFieldPrecioDeclarado.getText().length()>0)
	        	bi.getDatosEconomicosBien().setPrecioDeclarado(Double.valueOf(jTextFieldPrecioDeclarado.getText()));
	        else if (jTextFieldPrecioDeclarado.getText().length() < 1)
	        	bi.getDatosEconomicosBien().setPrecioDeclarado(null);
	        
	        //    Datos para el cálculo de la base liquidable
            if (jComboBoxOrigenValorBase.getSelectedItem()!=null)
                bi.getDatosBaseLiquidable().setProcedenciaValorBase(jComboBoxOrigenValorBase.getSelectedPatron());
	        if (jTextFieldImporteValorBase.getText().length()>0)
	        	bi.getDatosBaseLiquidable().setValorBase(Double.valueOf(jTextFieldImporteValorBase.getText()));
	        else if (jTextFieldImporteValorBase.getText().length() < 1)
	        	bi.getDatosBaseLiquidable().setValorBase(null);
	        
	        if(jComboBoxUso.getSelectedItem()!=null)
	        	bi.getDatosEconomicosBien().setUso(((EstructuraDB)jComboBoxUso.getSelectedItem()).getPatron());
	        
	        if(jComboBoxTipoPropiedad.getSelectedItem()!=null)
	        	bi.getDatosEconomicosBien().setIndTipoPropiedad(((DomainNode)jComboBoxTipoPropiedad.getSelectedItem()).getPatron());  	        

	        if(jComboBoxClase.getSelectedItem()!=null)        	
                bi.setClaseBienInmueble(jComboBoxClase.getSelectedPatron());

            try
            {
            	if (bi.getDomicilioTributario().getTipoVia()!=null && !bi.getDomicilioTributario().getTipoVia().equals("") && bi.getDomicilioTributario().getNombreVia()!=null && !bi.getDomicilioTributario().getNombreVia().equals("")
            			&& (bi.getDomicilioTributario().getCodPoligono()==null || bi.getDomicilioTributario().getCodPoligono().equals("")) && (bi.getDomicilioTributario().getCodParaje()==null || bi.getDomicilioTributario().getCodParaje().equals(""))
            			&& (bi.getDomicilioTributario().getCodParcela()==null || bi.getDomicilioTributario().getCodParcela().equals(""))){
            		
                DireccionLocalizacion dir = ConstantesRegExp.clienteCatastro.getViaPorNombreYCodigo(bi.getDomicilioTributario().getNombreVia(), bi.getDomicilioTributario().getCodigoVia());
                if(dir!=null && dir.getNombreVia()!=null && !dir.getNombreVia().equalsIgnoreCase(""))
                {
                    if(!dir.getNombreVia().equalsIgnoreCase(bi.getDomicilioTributario().getNombreVia()))
                    {
                      bi.getDomicilioTributario().setNombreVia(dir.getNombreVia());
                    }
                    if (dir.getCodigoVia()==-1){
                    	bi.getDomicilioTributario().setCodigoVia(0);
                    }
                    else{
                    	bi.getDomicilioTributario().setCodigoVia(dir.getCodigoVia());
                    }
                    if (bi.getDomicilioTributario().getTipoVia()==null || bi.getDomicilioTributario().getTipoVia().equals("")){
                    	bi.getDomicilioTributario().setTipoVia(dir.getTipoVia());
                    }
                    
                }
            	}
            	else if ((bi.getDomicilioTributario().getTipoVia()==null || bi.getDomicilioTributario().getTipoVia().equals("")) && (bi.getDomicilioTributario().getNombreVia()==null || bi.getDomicilioTributario().getNombreVia().equals(""))
            			&& bi.getDomicilioTributario().getCodPoligono()!=null && !bi.getDomicilioTributario().getCodPoligono().equals("") && bi.getDomicilioTributario().getCodParaje()!=null && !bi.getDomicilioTributario().getCodParaje().equals("")
            			&& bi.getDomicilioTributario().getCodParcela()!=null && !bi.getDomicilioTributario().getCodParcela().equals("")){
            		bi.getDomicilioTributario().setCodigoVia(-1);
            	}
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            biJ= null;
        }
        return biJ;
    }
    
    public BienInmuebleJuridico getBienInmueble(BienInmuebleJuridico biJ, String tipoMovimiento){

    	BienInmuebleCatastro bi = null;
    	if (okPressed)
    	{
	    	if(biJ==null)
            {
                biJ = new BienInmuebleJuridico();
            }
            if(biJ.getBienInmueble()==null)
            {
                biJ.setBienInmueble(new BienInmuebleCatastro());
            }
            
            if(!(Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales")){
            	// expediente de variaciones
            	if(!biJ.getBienInmueble().getTipoMovimiento().equals(BienInmuebleCatastro.TIPO_MOVIMIENTO_ALTA)){
            		biJ.getBienInmueble().setTipoMovimiento(tipoMovimiento);
            	}
            	
            	if(!biJ.getTIPO_MOVIMIENTO().equals(BienInmuebleJuridico.TIPO_MOVIMIENTO_ALTA)){
            		biJ.setTIPO_MOVIMIENTO(tipoMovimiento);
            		biJ.getBienInmueble().setTipoMovimiento(tipoMovimiento);
            	}
    		}
            
            
            biJ.getBienInmueble().setElementoModificado(true);
            biJ.setElementoModificado(true);
            
            
            bi = biJ.getBienInmueble();
            //Datos identificación
	    	IdBienInmueble idBienInmueble = new IdBienInmueble();
	    	idBienInmueble.setParcelaCatastral(EdicionUtils.paddingString(jTextFieldRefCatastral1.getText(),EdicionUtils.TAM_REFCATASTRAL/2,'0', true)
	                + EdicionUtils.paddingString(jTextFieldRefCatastral2.getText(),EdicionUtils.TAM_REFCATASTRAL/2,'0', true));
	      
	        
	    	
	    	idBienInmueble.setNumCargo(GeopistaUtil.completarConCeros(jTextFieldCargo.getText(),4));	    	
	    	idBienInmueble.setDigControl1(jTextFieldDC1.getText());
	    	idBienInmueble.setDigControl2(jTextFieldDC2.getText());
	    	idBienInmueble.setIdBienInmueble(idBienInmueble.getParcelaCatastral().getRefCatastral1()+idBienInmueble.getParcelaCatastral().getRefCatastral2()+idBienInmueble.getNumCargo()+idBienInmueble.getDigControl1()+idBienInmueble.getDigControl2());
	        bi.setIdBienInmueble(idBienInmueble);

            Provincia provincia = (Provincia) jComboCodProvincia.getSelectedItem();
            // Se comenta esta linea porque el codigo Provincia tributario no depende de la provincia registral
            // bi.getDomicilioTributario().setProvinciaINE(provincia.getIdProvincia());

            NumeroFincaRegistral numFincaReg = null;
            if (jTextFieldNumRegistro.getText().length()>0 &&
                    jTextFieldCodRegistro.getText().length()>0 &&
                    provincia.getIdProvincia().length()>0){

            	String codigoProvincia = GeopistaUtil.completarConCeros(provincia.getIdProvincia(),2);
            	String numeroRegistro = GeopistaUtil.completarConCeros(jTextFieldNumRegistro.getText(), 3);
            	String numeroFincaRegistral = GeopistaUtil.completarConCeros(jTextFieldNumRegistro.getText(),14);
            	
                numFincaReg = new NumeroFincaRegistral(codigoProvincia + numeroRegistro + numeroFincaRegistral);
//                numFincaReg.getRegistroPropiedad().setCodigoRegistroPropiedad(jTextFieldCodRegistro.getText());
//                numFincaReg.getRegistroPropiedad().setCodigoProvincia(provincia.getIdProvincia());

                if (numFincaReg != null)
                    bi.setNumFincaRegistral(numFincaReg);
            }
            
            if (jComboCodProvincia.getSelectedItem()!=null){
            	if (jComboCodProvincia.getSelectedItem() instanceof Provincia){
            		bi.getDomicilioTributario().setProvinciaINE(((Provincia)jComboCodProvincia.getSelectedItem()).getIdProvincia());
            		bi.getDomicilioTributario().setNombreProvincia(((Provincia)jComboCodProvincia.getSelectedItem()).getNombreOficial());
            	}
            }

            //Datos de Localización
	        if (jComboBoxTipoVia.getSelectedPatron() != null)
	        	bi.getDomicilioTributario().setTipoVia(jComboBoxTipoVia.getSelectedPatron().toString());
	        else
	        	bi.getDomicilioTributario().setTipoVia("");
	                
	        if(jTextFieldCodigoVia.getText().equals("")){
	        	bi.getDomicilioTributario().setCodigoVia(new Integer(-1));
	        }
	        else{
	        	bi.getDomicilioTributario().setCodigoVia(new Integer(jTextFieldCodigoVia.getText()).intValue());
	        }
            bi.getDomicilioTributario().setNombreVia(jTextFieldDireccion.getText());
            if(jTextFieldCodigoVia.getText().equals("")){
            	 bi.getDomicilioTributario().setCodigoVia(Integer.valueOf(-1));
            }
            else{
            	 bi.getDomicilioTributario().setCodigoVia(Integer.valueOf(jTextFieldCodigoVia.getText()));
            }
           
	        if (jTextFieldNumero.getText().length()>0)
	        	bi.getDomicilioTributario().setPrimerNumero(Integer.parseInt(jTextFieldNumero.getText()));
	        else  if (jTextFieldNumero.getText().length() < 1)
	        	bi.getDomicilioTributario().setPrimerNumero(-1);
	        bi.getDomicilioTributario().setPrimeraLetra(jTextFieldLetra.getText());
	        if (jTextFieldNumeroD.getText().length()>0)
	        	bi.getDomicilioTributario().setSegundoNumero(Integer.parseInt(jTextFieldNumeroD.getText()));
	        else if (jTextFieldNumeroD.getText().length() < 1)
	        	bi.getDomicilioTributario().setSegundoNumero(-1);
	        bi.getDomicilioTributario().setSegundaLetra(jTextFieldLetraD.getText());
	        bi.getDomicilioTributario().setBloque(jTextFieldBloque.getText());
	        
	        if(jComboBoxEscalera.getSelectedItem()!=null)
	        	bi.getDomicilioTributario().setEscalera(((EstructuraDB)jComboBoxEscalera.getSelectedItem()).getPatron());
	        
	        if(jComboBoxPlanta.getSelectedItem()!=null)
	        	bi.getDomicilioTributario().setPlanta(((EstructuraDB)jComboBoxPlanta.getSelectedItem()).getPatron());
	        
	        if(jComboBoxPuerta.getSelectedItem()!=null)
	        	bi.getDomicilioTributario().setPuerta(((EstructuraDB)jComboBoxPuerta.getSelectedItem()).getPatron());
	        bi.getDomicilioTributario().setDireccionNoEstructurada(jTextFieldDirNoEstructurada.getText());
	        if (jTextFieldKm.getText().length()>0)
	        	bi.getDomicilioTributario().setKilometro(Double.parseDouble(jTextFieldKm.getText()));
	        else if (jTextFieldKm.getText().length() < 1)
	        	bi.getDomicilioTributario().setKilometro(-1);
	        bi.getDomicilioTributario().setCodPoligono(jTextFieldPoligono.getText());
	        bi.getDomicilioTributario().setCodParcela(jTextFieldParcela.getText());
            
	        if(paraje!=null && paraje.getCodigo()!=null)
	        	bi.getDomicilioTributario().setCodParaje(paraje.getCodigo());
	        
	        if(paraje!=null && paraje.getNombre()!=null)
	        	bi.getDomicilioTributario().setNombreParaje(paraje.getNombre());
            bi.getDomicilioTributario().setCodZonaConcentracion(jTextFieldZona.getText());

	         if (jComboBoxMunicipioAgregado.getSelectedItem()!=null){
	        	Municipio municipio = (Municipio) jComboBoxMunicipioAgregado.getSelectedItem();
	        	bi.getDomicilioTributario().setCodMunOrigenAgregacion(String.valueOf(municipio.getIdCatastro()));	        	
	        }
            

            //Datos Económicos
	        bi.getDatosEconomicosBien().setNumOrdenHorizontal(jTextFieldNumOrdenDH.getText());
	        if (jTextFieldCoefParticipacion.getText().length()>0)
	        	bi.getDatosEconomicosBien().setCoefParticipacion(Float.valueOf(jTextFieldCoefParticipacion.getText()));
	        else if (jTextFieldCoefParticipacion.getText().length() < 1)
	        	bi.getDatosEconomicosBien().setCoefParticipacion(null);

            	        
	        if(jTextFieldAnioFin.getText().length() == 4)
            {
                String fechaString = jTextFieldAnioFin.getText();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
                Date fecha=null;
                try
                {
                    fecha = formatter.parse(fechaString);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                Calendar aux= new GregorianCalendar();
                aux.setTime(fecha);
                int anno = aux.get(Calendar.YEAR);
                bi.getDatosEconomicosBien().setAnioFinValoracion(new Integer(anno));    
            }
            else 
	        	bi.getDatosEconomicosBien().setAnioFinValoracion(null);
	        
            if (jTextFieldPrecioAdmVenta.getText().length()>0)
            	bi.getDatosEconomicosBien().setPrecioVenta(Double.valueOf(jTextFieldPrecioAdmVenta.getText()));
            else if (jTextFieldPrecioAdmVenta.getText().length() < 1)
	        	bi.getDatosEconomicosBien().setPrecioVenta(null);            
            if (jTextFieldOrigenValorDeclarado.getText().length()>0)
            	bi.getDatosEconomicosBien().setOrigenPrecioDeclarado(jTextFieldOrigenValorDeclarado.getText());
	        if (jTextFieldPrecioDeclarado.getText().length()>0)
	        	bi.getDatosEconomicosBien().setPrecioDeclarado(Double.valueOf(jTextFieldPrecioDeclarado.getText()));
	        else if (jTextFieldPrecioDeclarado.getText().length() < 1)
	        	bi.getDatosEconomicosBien().setPrecioDeclarado(null);
	        
	        //    Datos para el cálculo de la base liquidable
            if (jComboBoxOrigenValorBase.getSelectedItem()!=null)
                bi.getDatosBaseLiquidable().setProcedenciaValorBase(jComboBoxOrigenValorBase.getSelectedPatron());
	        if (jTextFieldImporteValorBase.getText().length()>0)
	        	bi.getDatosBaseLiquidable().setValorBase(Double.valueOf(jTextFieldImporteValorBase.getText()));
	        else if (jTextFieldImporteValorBase.getText().length() < 1)
	        	bi.getDatosBaseLiquidable().setValorBase(null);
	        
	        if(jComboBoxUso.getSelectedItem()!=null)
	        	bi.getDatosEconomicosBien().setUso(((EstructuraDB)jComboBoxUso.getSelectedItem()).getPatron());
	        
	        if(jComboBoxTipoPropiedad.getSelectedItem()!=null)
	        	bi.getDatosEconomicosBien().setIndTipoPropiedad(((DomainNode)jComboBoxTipoPropiedad.getSelectedItem()).getPatron());  	        

	        if(jComboBoxClase.getSelectedItem()!=null)        	
                bi.setClaseBienInmueble(jComboBoxClase.getSelectedPatron());

            try
            {
            	if (bi.getDomicilioTributario().getTipoVia()!=null && !bi.getDomicilioTributario().getTipoVia().equals("") && bi.getDomicilioTributario().getNombreVia()!=null && !bi.getDomicilioTributario().getNombreVia().equals("")
            			&& (bi.getDomicilioTributario().getCodPoligono()==null || bi.getDomicilioTributario().getCodPoligono().equals("")) && (bi.getDomicilioTributario().getCodParaje()==null || bi.getDomicilioTributario().getCodParaje().equals(""))
            			&& (bi.getDomicilioTributario().getCodParcela()==null || bi.getDomicilioTributario().getCodParcela().equals(""))){
            		
                DireccionLocalizacion dir = ConstantesRegExp.clienteCatastro.getViaPorNombreYCodigo(bi.getDomicilioTributario().getNombreVia(), bi.getDomicilioTributario().getCodigoVia());
                if(dir!=null && dir.getNombreVia()!=null && !dir.getNombreVia().equalsIgnoreCase(""))
                {
                    if(!dir.getNombreVia().equalsIgnoreCase(bi.getDomicilioTributario().getNombreVia()))
                    {
                      bi.getDomicilioTributario().setNombreVia(dir.getNombreVia());
                    }
                    if (dir.getCodigoVia()==-1){
                    	bi.getDomicilioTributario().setCodigoVia(0);
                    }
                    else{
                    	bi.getDomicilioTributario().setCodigoVia(dir.getCodigoVia());
                    }
                    if (bi.getDomicilioTributario().getTipoVia()==null || bi.getDomicilioTributario().getTipoVia().equals("")){
                    	bi.getDomicilioTributario().setTipoVia(dir.getTipoVia());
                    }
                    
                }
            	}
            	else if ((bi.getDomicilioTributario().getTipoVia()==null || bi.getDomicilioTributario().getTipoVia().equals("")) && (bi.getDomicilioTributario().getNombreVia()==null || bi.getDomicilioTributario().getNombreVia().equals(""))
            			&& bi.getDomicilioTributario().getCodPoligono()!=null && !bi.getDomicilioTributario().getCodPoligono().equals("") && bi.getDomicilioTributario().getCodParaje()!=null && !bi.getDomicilioTributario().getCodParaje().equals("")
            			&& bi.getDomicilioTributario().getCodParcela()!=null && !bi.getDomicilioTributario().getCodParcela().equals("")){
            		bi.getDomicilioTributario().setCodigoVia(-1);
            	}
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            biJ= null;
        }
        return biJ;
    }

    private void initialize()
    {     
        Locale loc=I18N.getLocaleAsObject();         
        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.catastro.intercambio.language.Expedientesi18n",loc,this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("Expedientes",bundle);
                
        this.setLayout(new GridBagLayout());
        this.setSize(BienInmuebleExtendedInfoDialog.DIM_X,BienInmuebleExtendedInfoDialog.DIM_Y);
        this.add(getJPanelDatosIdentificacion(),  new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 0, 5), 0, 0));
        
        this.add(getJPanelDatosLocalizacion(), new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 0, 5), 0, 0));
        this.add(getJPanelDatosEconomicos(), new GridBagConstraints(0, 3, 1, 1, 0.1, 0.1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 0, 5), 0, 0));
        this.add(getJPanelDatosBaseLiquidable(), new GridBagConstraints(0, 4, 1, 1, 0.1, 0.1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 0, 5), 0, 0));
        
        //Inicializa los desplegables        
        if (Identificadores.get("ListaProvincias")==null)
        {
            EdicionOperations oper = new EdicionOperations();
            ArrayList lst = oper.obtenerProvincias();
            Identificadores.put("ListaProvincias", lst);
            EdicionUtils.cargarLista(getJComboCodProvincia(), lst);
        }
        else
        {
            EdicionUtils.cargarLista(getJComboCodProvincia(), 
                    (ArrayList)Identificadores.get("ListaProvincias"));
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
        
    }
    
    public BienInmuebleExtendedInfoPanel(GridBagLayout layout)
    {
        super(layout);
        initialize();
    }
    
    public BienInmuebleExtendedInfoPanel(GridBagLayout layout, TipoExpediente tipoExpediente)
    {
        super(layout);
        this.tipoExpediente  = tipoExpediente;
        initialize();
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
     * This method initializes jPanelDatosIdentificacion	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanelDatosIdentificacion()
    {
        if (jPanelDatosIdentificacion == null)
        {       
            jLabelDC2 = new JLabel(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes","bieninmueble.extended.identificacion.dc2")),
                    JLabel.CENTER);            
            jLabelDC1 = new JLabel(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes","bieninmueble.extended.identificacion.dc1")),
                    JLabel.CENTER);         
            jLabelCargo = new JLabel(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes","bieninmueble.extended.identificacion.cargo")),
                    JLabel.CENTER);
            jLabelRefCatastral2 = new JLabel(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes","bieninmueble.extended.identificacion.catastral2")),
                    JLabel.CENTER);           
            jLabelRefCatastral1 = new JLabel(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes","bieninmueble.extended.identificacion.catastral1")),
                    JLabel.CENTER);
            jLabelClase  = new JLabel(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes","bieninmueble.extended.identificacion.clase")),
                    JLabel.CENTER);
            
            
            jPanelDatosIdentificacion = new JPanel(new GridBagLayout());
            jPanelDatosIdentificacion.setBorder(BorderFactory.createTitledBorder
                    (null,I18N.get("Expedientes","bieninmueble.extended.identificacion.titulo"), 
                            TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12)));
            jPanelDatosIdentificacion.add(jLabelRefCatastral1, 
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosIdentificacion.add(jLabelRefCatastral2,
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
          
            jPanelDatosIdentificacion.add(jLabelCargo, 
                    new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosIdentificacion.add(jLabelDC1, 
                    new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
           jPanelDatosIdentificacion.add(jLabelDC2, 
                    new GridBagConstraints(4, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
           jPanelDatosIdentificacion.add(jLabelClase, 
                   new GridBagConstraints(5, 0, 1, 1, 0.1, 0.1,
                           GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                           new Insets(0, 5, 0, 5), 0, 0));
           
           
            jPanelDatosIdentificacion.add(getJTextFieldRefCatastral1(), 
                    new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosIdentificacion.add(getJTextFieldRefCatastral2(), 
                    new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            
            jPanelDatosIdentificacion.add(getJTextFieldCargo(), 
                    new GridBagConstraints(2, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosIdentificacion.add(getJTextFieldDC1(), 
                    new GridBagConstraints(3, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosIdentificacion.add(getJTextFieldDC2(), 
                    new GridBagConstraints(4, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosIdentificacion.add(getJComboBoxClase(), 
                    new GridBagConstraints(5, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
           
            jPanelDatosIdentificacion.add(getJPanelFincaRegistral(), 
                    new GridBagConstraints(0, 2, 6, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 300, 0));
          
        }
        return jPanelDatosIdentificacion;
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
            
            jPanelDatosLocalizacion = new JPanel(new GridBagLayout());
            jPanelDatosLocalizacion.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("Expedientes","bieninmueble.extended.localizacion.titulo"), 
                            TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12)));
            jPanelDatosLocalizacion.add(getJPanelUrbana(), 
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosLocalizacion.add(getJPanelRustica(),
                    new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            
        }
        return jPanelDatosLocalizacion;
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
            jLabelPrecioDeclarado = new JLabel(I18N.get("Expedientes","bieninmueble.extended.economicos.valordeclarado"),
                    JLabel.CENTER);
            jLabelOrigenValorDeclarado = new JLabel(I18N.get("Expedientes","bieninmueble.extended.economicos.origenvalordeclarado"),
                    JLabel.CENTER);
            jLabelPrecioAdmVenta = new JLabel(I18N.get("Expedientes","bieninmueble.extended.economicos.precioventa"),
                    JLabel.CENTER);
            jLabelAnioFin = new JLabel(I18N.get("Expedientes","bieninmueble.extended.economicos.aniofin"),
                    JLabel.CENTER);
            jLabelCoefParticipacion = new JLabel(I18N.get("Expedientes","bieninmueble.extended.economicos.coefparticipacion"),
                    JLabel.CENTER);
            jLabelNumOrdenDH = new JLabel(I18N.get("Expedientes","bieninmueble.extended.economicos.numordenDH"),
                    JLabel.CENTER);
            jLabelTipoPropiedad = new JLabel(I18N.get("Expedientes","bieninmueble.extended.economicos.tipopropiedad"),
                    JLabel.CENTER);
            jLabelUso = new JLabel(I18N.get("Expedientes","bieninmueble.extended.economicos.uso"),
                    JLabel.CENTER);
            
            jPanelDatosEconomicos = new JPanel(new GridBagLayout());
            jPanelDatosEconomicos.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("Expedientes","bieninmueble.extended.economicos.titulo"), 
                            TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12)));
            jPanelDatosEconomicos.add(jLabelNumOrdenDH, 
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosEconomicos.add(jLabelCoefParticipacion, 
                    new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosEconomicos.add(jLabelAnioFin, 
                    new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosEconomicos.add(jLabelPrecioAdmVenta, 
                    new GridBagConstraints(2, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosEconomicos.add(jLabelTipoPropiedad , 
                    new GridBagConstraints(4, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosEconomicos.add(getJTextFieldNumOrdenDH(), 
                    new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosEconomicos.add(getJTextFieldCoefParticipacion(), 
                    new GridBagConstraints(0, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosEconomicos.add(getJTextFieldAnioFin(), 
                    new GridBagConstraints(2, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
                               
            jPanelDatosEconomicos.add(getJTextFieldPrecioAdmVenta(), 
                    new GridBagConstraints(2, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosEconomicos.add(getJComboBoxTipoPropiedad(), 
                    new GridBagConstraints(4, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosEconomicos.add(jLabelOrigenValorDeclarado, 
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosEconomicos.add(jLabelPrecioDeclarado, 
                    new GridBagConstraints(1, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosEconomicos.add(jLabelUso , 
                    new GridBagConstraints(4, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosEconomicos.add(getJTextFieldOrigenValorDeclarado(), 
                    new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosEconomicos.add(getJTextFieldPrecioDeclarado(), 
                    new GridBagConstraints(1, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosEconomicos.add(getJComboBoxUso(), 
                    new GridBagConstraints(4, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
                       
        }
        return jPanelDatosEconomicos;
    }
    
    private JButton getJButtonAnioFin() {
    	
    	if(jButtonAnioFin ==null){
    		
    		jButtonAnioFin = new JButton();
    		jButtonAnioFin.setIcon(UtilRegistroExp.iconoZoom);
    		jButtonAnioFin.setToolTipText(I18N.get("RegistroExpedientes",
            "Catastro.RegistroExpedientes.PanelDatosCrearExp.fechaAltButton.hint"));
    		jButtonAnioFin.addActionListener(new ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    fechaAltButtonActionPerformed(evt);
                }
            });
    		jButtonAnioFin.setPreferredSize(new Dimension(20,20));
    	}
		return jButtonAnioFin;
	}
    
    /**
     * Metodo que trata el evento lanzado por el boton de la fecha de año fin y muestra el dialogo para selecionar
     * una fecha valida, recogiendola de la constante estatica calendarValue.
     *
     * @param evt Evento lanzado
     * */
    private void fechaAltButtonActionPerformed(ActionEvent evt)
    {
        UtilRegistroExp.showCalendarDialog(AppContext.getApplicationContext().getMainFrame());

		if ((ConstantesCatastro.calendarValue != null) && (!ConstantesCatastro.calendarValue.trim().equals("")))
        {
			jTextFieldAnioFin.setText(ConstantesCatastro.calendarValue);
		}
    }

	/**
     * This method initializes jTextFieldRefCatastral1	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getJTextFieldRefCatastral1()
    {
        if (jTextFieldRefCatastral1 == null)
        {
            jTextFieldRefCatastral1 = new TextField(7);
            if(this.tipoExpediente!=null && this.tipoExpediente.getCodigoTipoExpediente()!=null && this.tipoExpediente.getCodigoTipoExpediente().equals("901N")){
            	jTextFieldRefCatastral1.setEditable(true);
            }
            else{
            	jTextFieldRefCatastral1.setEditable(false);
            }
        }
        return jTextFieldRefCatastral1;
    }
    
    /**
     * This method initializes jTextFieldRefCatastral2	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getJTextFieldRefCatastral2()
    {
        if (jTextFieldRefCatastral2 == null)
        {
            jTextFieldRefCatastral2 = new TextField(7);
            if(this.tipoExpediente!=null && this.tipoExpediente.getCodigoTipoExpediente()!=null && this.tipoExpediente.getCodigoTipoExpediente().equals("901N")){
            	jTextFieldRefCatastral2.setEditable(true);
            }
            else{
            	jTextFieldRefCatastral2.setEditable(false);
            }
        }
        return jTextFieldRefCatastral2;
    }
    
    /**
     * This method initializes jPanelFincaRegistral	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanelFincaRegistral()
    {
        if (jPanelFincaRegistral == null)
        {
            jLabelCodProvincia = new JLabel(I18N.get("Expedientes","bieninmueble.extended.fincaregistral.codprovincia"),
                    JLabel.CENTER);
            jLabelCodRegistro = new JLabel(I18N.get("Expedientes","bieninmueble.extended.fincaregistral.codregistro"),
                    JLabel.CENTER);
            jLabelNumRegistro = new JLabel(I18N.get("Expedientes","bieninmueble.extended.fincaregistral.numregistro"),
                    JLabel.CENTER);
            jPanelFincaRegistral = new JPanel(new GridBagLayout());
            jPanelFincaRegistral.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("Expedientes","bieninmueble.extended.fincaregistral.titulo"),
                                TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12)));
            jPanelFincaRegistral.add(jLabelCodProvincia, 
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelFincaRegistral.add(new JPanel(), 
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelFincaRegistral.add(jLabelCodRegistro, 
                    new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelFincaRegistral.add(jLabelNumRegistro, 
                    new GridBagConstraints(4, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelFincaRegistral.add(getJComboCodProvincia(), 
                    new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelFincaRegistral.add(getJTextFieldCodRegistro(), 
                    new GridBagConstraints(2, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelFincaRegistral.add(getJButtonCodigoRegistro(), 
                    new GridBagConstraints(3, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.WEST, GridBagConstraints.NONE,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelFincaRegistral.add(getJTextFieldNumRegistro(), 
                    new GridBagConstraints(4, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
        }
        return jPanelFincaRegistral;
    }
    
    /**
     * This method initializes jComboCodProvincia	
     * 	
     * @return javax.swing.JComboBox	
     */
    private JComboBox getJComboCodProvincia()
    {
        if (jComboCodProvincia == null)
        {
            jComboCodProvincia = new JComboBox();
            jComboCodProvincia.setRenderer(new UbicacionListCellRenderer());
            jComboCodProvincia.setKeySelectionManager(new ComboBoxKeyTipoDestinoSelectionManager());
            jComboCodProvincia.addActionListener(new ActionListener()
                    {
                public void actionPerformed(ActionEvent e)
                {   
                    if (jComboCodProvincia.getSelectedIndex()==0)
                    {
                        jComboBoxMunicipioAgregado.removeAllItems();
                    }
                    else
                    {
                        EdicionOperations oper = new EdicionOperations();
                        EdicionUtils.cargarLista(getJComboBoxMunicipioAgregado(), 
                                oper.obtenerMunicipios((Provincia)jComboCodProvincia.getSelectedItem()));
                    }
                   
                }
                    });
            
        }
        jComboCodProvincia.addActionListener(new ListenerCombo());
        return jComboCodProvincia;
    }
    
    private JComboBox getJComboBoxClase()
    {
        //Indicador de exactitud del año de construccion
        if (jComboBoxClase  == null)
        {
        	Estructuras.cargarEstructura("Clase Bien Inmueble");
        	jComboBoxClase = new ComboBoxEstructuras(Estructuras.getListaTipos(),
    				null, AppContext.getApplicationContext().getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), true);
        	    		    		
        }
        return jComboBoxClase;
    }
    
    /**
     * This method initializes jTextFieldCodRegistro  
     *  
     * @return javax.swing.JTextField   
     */
    private JTextField getJTextFieldCodRegistro()
    {
        if (jTextFieldCodRegistro == null)
        {
            jTextFieldCodRegistro = new TextField(3);// JNumberTextField(JNumberTextField.NUMBER);//, new Integer(999));
            jTextFieldCodRegistro.addCaretListener(new CaretListener()
                    {
                public void caretUpdate(CaretEvent evt)
                {
                    EdicionUtils.chequeaLongYNumCampoEdit(jTextFieldCodRegistro,3, aplicacion.getMainFrame());
                }
                    });
        }
        jTextFieldCodRegistro.getDocument().addDocumentListener(new ListenerTextField()); 
        return jTextFieldCodRegistro;
    }
    
    /**
     * This method initializes jTextFieldNumRegistro  
     *  
     * @return javax.swing.JTextField   
     */
    private JTextField getJTextFieldNumRegistro()
    {
        if (jTextFieldNumRegistro == null)
        {
            jTextFieldNumRegistro = new TextField(14);            
        }
        jTextFieldNumRegistro.getDocument().addDocumentListener(new ListenerTextField()); 
        return jTextFieldNumRegistro;
    }
    
    /**
     * This method initializes jPanelUrbana	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanelUrbana()
    {
        if (jPanelUrbana == null)
        {            
           
            jLabelKm = new JLabel(I18N.get("Expedientes","bieninmueble.extended.urbana.km"), 
                    JLabel.CENTER);
           
           
            jLabelDirNoEstructurada = new JLabel(I18N.get("Expedientes","bieninmueble.extended.urbana.dirnoestruc"),
                    JLabel.CENTER);
            jLabelPuerta = new JLabel(I18N.get("Expedientes","bieninmueble.extended.urbana.puerta"),
                    JLabel.CENTER);
            jLabelPlanta  = new JLabel(I18N.get("Expedientes","bieninmueble.extended.urbana.planta"),
                    JLabel.CENTER);
            jLabelEscalera = new JLabel(I18N.get("Expedientes","bieninmueble.extended.urbana.escalera"),
                    JLabel.CENTER);
            jLabelBloque = new JLabel(I18N.get("Expedientes","bieninmueble.extended.urbana.bloque"),
                    JLabel.CENTER);
            jLabelLetraD = new JLabel(I18N.get("Expedientes","bieninmueble.extended.urbana.letraD"), 
                    JLabel.CENTER);
            jLabelNumeroD = new JLabel(I18N.get("Expedientes","bieninmueble.extended.urbana.numeroD"), 
                    JLabel.CENTER);   
            jLabelLetra = new JLabel(I18N.get("Expedientes","bieninmueble.extended.urbana.letra"), 
                    JLabel.CENTER);       
            jLabelNumero = new JLabel(I18N.get("Expedientes","bieninmueble.extended.urbana.numero"), 
                    JLabel.CENTER);     
            jLabelDireccion = new JLabel(I18N.get("Expedientes","bieninmueble.extended.urbana.direccion"), 
                    JLabel.CENTER);       
            jLabelTipoVia = new JLabel(I18N.get("Expedientes","bieninmueble.extended.urbana.tipovia"), 
                    JLabel.CENTER);
            jLabelCodigoVia= new JLabel(I18N.get("Expedientes","bieninmueble.extended.urbana.codigovia"), 
                    JLabel.CENTER);
            
            jPanelUrbana = new JPanel(new GridBagLayout());
            jPanelUrbana.setBorder(BorderFactory.createTitledBorder
                    (null,I18N.get("Expedientes","bieninmueble.extended.urbana.titulo"), 
                            TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12)));
            
            jPanelUrbana.add(jLabelTipoVia, 
                    new GridBagConstraints(0, 0, 2, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelUrbana.add(jLabelCodigoVia, 
                    new GridBagConstraints(2, 0, 2, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelUrbana.add(jLabelDireccion, 
                    new GridBagConstraints(3, 0, 8, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelUrbana.add(getJComboBoxTipoVia(), 
                    new GridBagConstraints(0, 1, 2, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelUrbana.add(getJTextFieldCodigoVia(), 
                    new GridBagConstraints(2, 1, 2, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelUrbana.add(getJTextFieldDireccion(), 
                    new GridBagConstraints(4, 1, 6, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelUrbana.add(getJButtonBuscarDireccion(), 
                    new GridBagConstraints(11, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelUrbana.add(jLabelNumero, 
                    new GridBagConstraints(0, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelUrbana.add(jLabelLetra, 
                    new GridBagConstraints(1, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelUrbana.add(jLabelNumeroD, 
                    new GridBagConstraints(2, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelUrbana.add(jLabelLetraD, 
                    new GridBagConstraints(3, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelUrbana.add(jLabelBloque, 
                    new GridBagConstraints(4, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelUrbana.add(jLabelEscalera, 
                    new GridBagConstraints(5, 3, 2, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelUrbana.add(jLabelPlanta, 
                    new GridBagConstraints(7, 3, 2, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelUrbana.add(jLabelPuerta, 
                    new GridBagConstraints(9, 3, 3, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelUrbana.add(getJTextFieldNumero(), 
                    new GridBagConstraints(0, 4, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelUrbana.add(getJTextFieldLetra(), 
                    new GridBagConstraints(1, 4, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelUrbana.add(getJTextFieldNumeroD(), 
                    new GridBagConstraints(2, 4, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelUrbana.add(getJTextFieldLetraD(), 
                    new GridBagConstraints(3, 4, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelUrbana.add(getJTextFieldBloque(), 
                    new GridBagConstraints(4, 4, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            
            jPanelUrbana.add(getJComboBoxEscalera(), 
                    new GridBagConstraints(5, 4, 2, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            
            jPanelUrbana.add(getJComboBoxPlanta(), 
                    new GridBagConstraints(7, 4, 2, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            
            jPanelUrbana.add(getJComboBoxPuerta(), 
                    new GridBagConstraints(9, 4, 3, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelUrbana.add(jLabelDirNoEstructurada, 
                    new GridBagConstraints(0, 5, 8, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelUrbana.add(jLabelKm, 
                    new GridBagConstraints(9, 5, 3, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelUrbana.add(getJTextFieldDirNoEstructurada(), 
                    new GridBagConstraints(0, 6, 8, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelUrbana.add(getJTextFieldKm(), 
                    new GridBagConstraints(9, 6, 3, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            
            EdicionUtils.crearMallaPanel(6, 12, jPanelUrbana, 0.1, 0.1, 
            		GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, 
            		new Insets(0, 5, 0, 5), 0, 0); 
        }
        return jPanelUrbana;
    }
    
    /**
     * This method initializes jPanelRustica	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanelRustica()
    {
        if (jPanelRustica == null)
        {            
            jLabelMunicipioAgregado = new JLabel(I18N.get("Expedientes","bieninmueble.extended.rustica.municipioagregado"), 
                    JLabel.CENTER);
            jLabelZona = new JLabel(I18N.get("Expedientes","bieninmueble.extended.rustica.zona"), 
                    JLabel.CENTER);            
            jLabelParaje = new JLabel(I18N.get("Expedientes","bieninmueble.extended.rustica.paraje"), 
                    JLabel.CENTER);           
            jLabelParcela = new JLabel(I18N.get("Expedientes","bieninmueble.extended.rustica.parcela"), 
                    JLabel.CENTER);            
            jLabelPoligono = new JLabel(I18N.get("Expedientes","bieninmueble.extended.rustica.poligono"), 
                    JLabel.CENTER);
            jPanelRustica = new JPanel(new GridBagLayout());
            jPanelRustica.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("Expedientes","bieninmueble.extended.rustica.titulo"), 
                            TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12)));
            jPanelRustica.add(jLabelPoligono, 
                    new GridBagConstraints(0, 0, 1, 1, 0, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelRustica.add(jLabelParcela, 
                    new GridBagConstraints(1, 0, 1, 1, 0, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelRustica.add(jLabelParaje, 
                    new GridBagConstraints(2, 0, 1, 1, 0, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 50, 0));
            jPanelRustica.add(jLabelZona, 
                    new GridBagConstraints(4, 0, 1, 1, 0, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelRustica.add(jLabelMunicipioAgregado, 
                    new GridBagConstraints(5, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelRustica.add(getJTextFieldPoligono(), 
                    new GridBagConstraints(0, 1, 1, 1, 0, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelRustica.add(getJTextFieldParcela(), 
                    new GridBagConstraints(1, 1, 1, 1, 0, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelRustica.add(getJTextFieldParaje(), 
                    new GridBagConstraints(2, 1, 1, 1, 0, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 180, 0));
            jPanelRustica.add(getJButtonBuscarParaje(), 
                    new GridBagConstraints(3, 1, 1, 1, 0, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelRustica.add(getJTextFieldZona(), 
                    new GridBagConstraints(4, 1, 1, 1, 0, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelRustica.add(getJComboBoxMunicipioAgregado(), 
                    new GridBagConstraints(5, 1, 1, 1, 0, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
        }
        return jPanelRustica;
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
            //Estructuras.cargarEstructura("Tipos de via normalizados del INE");
        	Estructuras.cargarEstructura("Tipo de vía");
            jComboBoxTipoVia = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), true);
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
            jTextFieldDireccion = new TextField(50);
        }
        jTextFieldDireccion.getDocument().addDocumentListener(new ListenerTextField());
        return jTextFieldDireccion;
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
     * This method initializes jButtonBuscarDireccion	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getJButtonBuscarDireccion()
    {
        if (jButtonBuscarDireccion == null)
        {
            jButtonBuscarDireccion = new JButton();
            jButtonBuscarDireccion.setIcon(IconLoader.icon(GestionExpedienteConst.ICONO_BUSCAR));
            jButtonBuscarDireccion.addActionListener(new java.awt.event.ActionListener()
                    {
                public void actionPerformed(java.awt.event.ActionEvent e)
                {
                	
                	String tipoVia = "";
                    if(jComboBoxTipoVia.getSelectedPatron()!=null){
                    	tipoVia = jComboBoxTipoVia.getSelectedPatron().toString();
                    }
                    int codigoVia = 0;
                    if (!jTextFieldCodigoVia.getText().equals("")){
                    	codigoVia = new Integer(jTextFieldCodigoVia.getText()).intValue();
                    }
                    ViasSistemaDialog dialog = new ViasSistemaDialog(jTextFieldDireccion.getText(),tipoVia,codigoVia);                    
                    dialog.setVisible(true);    
                    
                    String nombreVia = dialog.getVia();
                    jComboBoxTipoVia.setSelectedPatron(dialog.getTipoVia());
                    jTextFieldDireccion.setText(nombreVia);
                    if(dialog.getCodigoVia() == -1 ){
                    	jTextFieldCodigoVia.setText("");
                    }
                    else{
                    	jTextFieldCodigoVia.setText(new Integer(dialog.getCodigoVia()).toString());
                    }
                    
                }
                    });
            jButtonBuscarDireccion.setName("_buscarvia");
        }
        return jButtonBuscarDireccion;
    }
    private JButton getJButtonCodigoRegistro()
    {
        if (jButtonCodigoRegistro  == null)
        {
        	jButtonCodigoRegistro = new JButton();
        	jButtonCodigoRegistro.setIcon(IconLoader.icon(GestionExpedienteConst.ICONO_BUSCAR));
        	jButtonCodigoRegistro.addActionListener(new java.awt.event.ActionListener()
                    {
                public void actionPerformed(java.awt.event.ActionEvent e)
                {
                	CodigoRegistroDialog dialog;
                	if(jComboCodProvincia.getSelectedItem()!=null){
                		Provincia p = (Provincia) jComboCodProvincia.getSelectedItem();
                		if(!(p.getIdProvincia().equals("")))
                			dialog = new CodigoRegistroDialog(p.getIdProvincia());
                		else
                			dialog = new CodigoRegistroDialog();
                	}
                	else
                		dialog = new CodigoRegistroDialog();
                	
                    dialog.setVisible(true);     
                    String codigoRegistro = dialog.getCodigoRegistro();
                    jTextFieldCodRegistro.setText(codigoRegistro);
                    
                }
                    });
        	jButtonCodigoRegistro.setName("_buscarcodigoregistro");
        }
        return jButtonCodigoRegistro;
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
            jTextFieldNumero = new TextField(4);//(JNumberTextField.NUMBER);//, new Integer(9999));
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
     * This method initializes jTextFieldNumeroD	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getJTextFieldNumeroD()
    {
        if (jTextFieldNumeroD == null)
        {
            jTextFieldNumeroD = new TextField(4);//new JNumberTextField(JNumberTextField.NUMBER);//, new Integer(9999));
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
        jTextFieldBloque.getDocument().addDocumentListener(new ListenerTextField());
        return jTextFieldBloque;
    }
    
    /**
     * This method initializes jTextFieldEscalera	
     * 	
     * @return javax.swing.JTextField	
     */
   
    private JComboBox getJComboBoxEscalera()
    {
        if (jComboBoxEscalera  == null)
        {
        	jComboBoxEscalera = new JComboBox();
        	jComboBoxEscalera.setRenderer(new EstructuraDBListCellRenderer());
        	jComboBoxEscalera.setKeySelectionManager(new ComboBoxKeyTipoDestinoSelectionManager());
        }
        jComboBoxEscalera.addActionListener(new ListenerCombo());
        return jComboBoxEscalera;
    }
    /**
     * This method initializes jTextFieldPlanta   
     *  
     * @return javax.swing.JTextField   
     */
   
    private JComboBox getJComboBoxPlanta()
    {
        if (jComboBoxPlanta  == null)
        {
        	jComboBoxPlanta = new JComboBox();
        	jComboBoxPlanta.setRenderer(new EstructuraDBListCellRenderer());
        	jComboBoxPlanta.setKeySelectionManager(new ComboBoxKeyTipoDestinoSelectionManager());
        }
        jComboBoxPlanta.addActionListener(new ListenerCombo());
        return jComboBoxPlanta;
    }
    
    /**
     * This method initializes jTextFieldPuerta	
     * 	
     * @return javax.swing.JTextField	
     */
    
    private JComboBox getJComboBoxPuerta()
    {
        if (jComboBoxPuerta  == null)
        {
        	jComboBoxPuerta = new JComboBox();;
        	jComboBoxPuerta.setRenderer(new EstructuraDBListCellRenderer());
        	jComboBoxPuerta.setKeySelectionManager(new ComboBoxKeyTipoDestinoSelectionManager());
        }
        jComboBoxPuerta.addActionListener(new ListenerCombo());
        return jComboBoxPuerta;
    }
    
    /**
     * This method initializes jTextFieldPoligono	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getJTextFieldPoligono()
    {
        if (jTextFieldPoligono == null)
        {
            jTextFieldPoligono = new TextField(3);
        }
        jTextFieldPoligono.getDocument().addDocumentListener(new ListenerTextField());
        return jTextFieldPoligono;
    }
    
    /**
     * This method initializes jTextFieldParcela	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getJTextFieldParcela()
    {
        if (jTextFieldParcela == null)
        {
            jTextFieldParcela = new TextField(5);
        }
        jTextFieldParcela.getDocument().addDocumentListener(new ListenerTextField());
        return jTextFieldParcela;
    }
    
    /**
     * This method initializes jTextFieldParaje	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getJTextFieldParaje()
    {
        if (jTextFieldParaje == null)
        {
            jTextFieldParaje = new TextField(30);
            //jTextFieldParaje.setEditable(false);
        }
        jTextFieldParaje.getDocument().addDocumentListener(new ListenerTextField());
        return jTextFieldParaje;
    }
    
    /**
     * This method initializes jButtonBuscarParaje	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getJButtonBuscarParaje()
    {
        if (jButtonBuscarParaje == null)
        {
            jButtonBuscarParaje = new JButton();
            jButtonBuscarParaje.setIcon(IconLoader.icon(GestionExpedienteConst.ICONO_BUSCAR));
            jButtonBuscarParaje.addActionListener(new java.awt.event.ActionListener()
                    {
                public void actionPerformed(java.awt.event.ActionEvent e)
                {
                    ParajesSistemaDialog dialog = new ParajesSistemaDialog(jTextFieldParaje.getText());
                    dialog.setVisible(true);    
                    
                    paraje = dialog.getParaje();
                    if(paraje!=null && paraje.getNombre()!=null)
                    	jTextFieldParaje.setText(paraje.getNombre());
                }
                    });
            jButtonBuscarParaje.setName("_buscarparaje");
        }
        return jButtonBuscarParaje;
    }
    
    /**
     * This method initializes jTextFieldZona	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getJTextFieldZona()
    {
        if (jTextFieldZona == null)
        {
            jTextFieldZona = new TextField(2);
            jTextFieldZona.addCaretListener(new CaretListener()
                    {
                public void caretUpdate(CaretEvent evt)
                {
                    EdicionUtils.chequeaLongYNumCampoEdit(jTextFieldZona,2, aplicacion.getMainFrame());
                }
                    });
        }
        jTextFieldZona.getDocument().addDocumentListener(new ListenerTextField());
        return jTextFieldZona;
    }
    
    /**
     * This method initializes jComboBoxMunicipioAgregado	
     * 	
     * @return javax.swing.JComboBox	
     */
    private JComboBox getJComboBoxMunicipioAgregado()
    {
        if (jComboBoxMunicipioAgregado == null)
        {
            jComboBoxMunicipioAgregado = new JComboBox();
            jComboBoxMunicipioAgregado.setRenderer(new UbicacionListCellRenderer());
            jComboBoxMunicipioAgregado.setKeySelectionManager(new ComboBoxKeyTipoDestinoSelectionManager());
        }
        jComboBoxMunicipioAgregado.addActionListener(new ListenerCombo());
        return jComboBoxMunicipioAgregado;
    }
    
    private JComboBox getJComboBoxTipoPropiedad()
    {
        if (jComboBoxTipoPropiedad  == null)
        {
        	Estructuras.cargarEstructura("Tipo Propiedad");
        	jComboBoxTipoPropiedad = new ComboBoxEstructuras(Estructuras.getListaTipos(),
    				null, AppContext.getApplicationContext().getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), true);
    		    		
            
        }
        return jComboBoxTipoPropiedad;
    }
    
    private JComboBox getJComboBoxUso()
    {
        if (jComboBoxUso   == null)
        {
        	jComboBoxUso = new JComboBox();
        	jComboBoxUso.setRenderer(new EstructuraDBListCellRenderer());
        	jComboBoxUso.setKeySelectionManager(new ComboBoxKeyTipoDestinoSelectionManager());
            
        }
        return jComboBoxUso;
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
            EdicionUtils.chequeaLongYNumCampoEdit(jTextFieldCargo,4, aplicacion.getMainFrame());
        }
            });
        }
        return jTextFieldCargo;
    }

    /**
     * This method initializes jTextFieldDC1	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getJTextFieldDC1()
    {
        if (jTextFieldDC1 == null)
        {
            jTextFieldDC1 = new TextField(1);
            jTextFieldDC1.addCaretListener(new CaretListener()
            {
        public void caretUpdate(CaretEvent evt)
        {
        	
        	EdicionUtils.chequeaLongYCharCampoEditYGuion(jTextFieldDC1,1, aplicacion.getMainFrame());
        }
            });
        }
        return jTextFieldDC1;
    }

    /**
     * This method initializes jTextFieldDC2	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getJTextFieldDC2()
    {
        if (jTextFieldDC2 == null)
        {
            jTextFieldDC2 = new TextField(1);
            jTextFieldDC2.addCaretListener(new CaretListener()
            {
        public void caretUpdate(CaretEvent evt)
        {
        	EdicionUtils.chequeaLongYCharCampoEditYGuion(jTextFieldDC2,1, aplicacion.getMainFrame());
        }
            });
        }
        return jTextFieldDC2;
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
            jTextFieldKm = new JTextField();
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
     * This method initializes jTextFieldNumOrdenDH	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getJTextFieldNumOrdenDH()
    {
        if (jTextFieldNumOrdenDH == null)
        {
            jTextFieldNumOrdenDH = new TextField(4);//new JNumberTextField(JNumberTextField.NUMBER);//, new Integer(9999));
            jTextFieldNumOrdenDH.addCaretListener(new CaretListener()
                    {
                public void caretUpdate(CaretEvent evt)
                {
                    EdicionUtils.chequeaLongYNumCampoEdit(jTextFieldNumOrdenDH,4, aplicacion.getMainFrame());
                }
                    });
        }
        return jTextFieldNumOrdenDH;
    }

    /**
     * This method initializes jTextFieldCoefParticipacion	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getJTextFieldCoefParticipacion()
    {
        if (jTextFieldCoefParticipacion == null)
        {
            jTextFieldCoefParticipacion = new TextField(10);
            jTextFieldCoefParticipacion.addCaretListener(new CaretListener()
                    {
                public void caretUpdate(CaretEvent evt)
                {
                    EdicionUtils.chequeaLongYDecimalCampoEdit(jTextFieldCoefParticipacion, 10, aplicacion.getMainFrame());
                }
                    });
        }
        return jTextFieldCoefParticipacion;
    }

    /**
     * This method initializes jTextFieldAnioFin	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getJTextFieldAnioFin()
    {
        if (jTextFieldAnioFin  == null)
        {
            jTextFieldAnioFin = new TextField(4);//new JNumberTextField(JNumberTextField.NUMBER);//, new Integer(9999));
            jTextFieldAnioFin.setEnabled(false);
            jTextFieldAnioFin.setText(UtilRegistroExp.showToday());
            jTextFieldAnioFin.addCaretListener(new CaretListener()
            {
        public void caretUpdate(CaretEvent evt)
        {
            EdicionUtils.chequeaLongYNumCampoEdit(jTextFieldAnioFin,4, aplicacion.getMainFrame());
        }
            });
            
        }
        return jTextFieldAnioFin;
    }
    
   
    /**
     * This method initializes jTextFieldPrecioAdmVenta	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getJTextFieldPrecioAdmVenta()
    {
    	if (jTextFieldPrecioAdmVenta == null)
    	{
    		jTextFieldPrecioAdmVenta = new JTextField(13);

    		jTextFieldPrecioAdmVenta.addCaretListener(new CaretListener()
    		{
    			public void caretUpdate(CaretEvent evt)
    			{
    				EdicionUtils.chequeaLongYDecimalCampoEdit(jTextFieldPrecioAdmVenta,13, aplicacion.getMainFrame());
    			}
    		});

    	}
    	return jTextFieldPrecioAdmVenta;
    }

    /**
     * This method initializes jTextFieldOrigenValorDeclarado	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getJTextFieldOrigenValorDeclarado()
    {
        if (jTextFieldOrigenValorDeclarado == null)
        {
            jTextFieldOrigenValorDeclarado = new TextField(1);
        }
        return jTextFieldOrigenValorDeclarado;
    }

    /**
     * This method initializes jTextFieldPrecioDeclarado	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getJTextFieldPrecioDeclarado()
    {
        if (jTextFieldPrecioDeclarado == null)
        {
            jTextFieldPrecioDeclarado = new TextField(13);
            jTextFieldPrecioDeclarado.addCaretListener(new CaretListener()
                    {
                public void caretUpdate(CaretEvent evt)
                {
                    EdicionUtils.chequeaLongYDecimalCampoEdit(jTextFieldPrecioDeclarado,13, aplicacion.getMainFrame());
                }
                    });
        }
        return jTextFieldPrecioDeclarado;
    }
    /**
     * This method initializes jPanelDatosBaseLiquidable    
     *  
     * @return javax.swing.JPanel   
     */
    private JPanel getJPanelDatosBaseLiquidable()
    {
        if (jPanelDatosBaseLiquidable == null)
        {
            jLabelImporteValorBase = new JLabel(I18N.get("Expedientes","bieninmueble.extended.liquidable.importe"), 
                    JLabel.CENTER);         
            jLabelOrigenValorBase = new JLabel(I18N.get("Expedientes","bieninmueble.extended.liquidable.origen"), 
                    JLabel.CENTER);           
            jPanelDatosBaseLiquidable = new JPanel(new GridBagLayout());
            jPanelDatosBaseLiquidable.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("Expedientes","bieninmueble.extended.liquidable.titulo"), 
                            TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12)));
            jPanelDatosBaseLiquidable.add(jLabelOrigenValorBase, 
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosBaseLiquidable.add(getJComboBoxOrigenValorBase(), 
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosBaseLiquidable.add(jLabelImporteValorBase, 
                    new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosBaseLiquidable.add(getJTextFieldImporteValorBase(), 
                    new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 50, 0));
           
            
        }
        return jPanelDatosBaseLiquidable ;
    }

    /**
     * This method initializes jComboBoxOrigenValorBase	
     * 	
     * @return javax.swing.JComboBox	
     */
    private ComboBoxEstructuras getJComboBoxOrigenValorBase()
    {
        if (jComboBoxOrigenValorBase == null)
        {
            Estructuras.cargarEstructura("Código de valor base");
            jComboBoxOrigenValorBase = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), true);
        }
        jComboBoxOrigenValorBase.addActionListener(new ListenerCombo());
        return jComboBoxOrigenValorBase;
    }

    /**
     * This method initializes jTextFieldImporteValorBase	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getJTextFieldImporteValorBase()
    {
        if (jTextFieldImporteValorBase == null)
        {
            jTextFieldImporteValorBase = new TextField(13);
            jTextFieldImporteValorBase.addCaretListener(new CaretListener()
                    {
                public void caretUpdate(CaretEvent evt)
                {
                    EdicionUtils.chequeaLongYDecimalCampoEdit(jTextFieldImporteValorBase,13, aplicacion.getMainFrame());
                }
                    });
        }
        jTextFieldImporteValorBase.getDocument().addDocumentListener(new ListenerTextField());
        return jTextFieldImporteValorBase;
    }

    /*
     * Comprueba que los campos obligatorios están escritos.
     * @return boolean 
     */
    public boolean datosMinimosYCorrectos()
    {
    	//Panel Base Liquidable
    	boolean okBase=true;
    	if( (jComboBoxOrigenValorBase!=null && jComboBoxOrigenValorBase.getSelectedItem()!=null && jComboBoxOrigenValorBase.getSelectedIndex()>0)
        		|| (jTextFieldImporteValorBase.getText()!=null && !jTextFieldImporteValorBase.getText().equalsIgnoreCase("")) ){       		
        		okBase= (jComboBoxOrigenValorBase!=null && jComboBoxOrigenValorBase.getSelectedItem()!=null && jComboBoxOrigenValorBase.getSelectedIndex()>0) &&    				 
    				(jTextFieldImporteValorBase.getText()!=null && !jTextFieldImporteValorBase.getText().equalsIgnoreCase(""));
        }
    	//Panel Finca Registral
    	boolean okRegistral=true;
    	if( (jComboCodProvincia!=null && jComboCodProvincia.getSelectedItem()!=null && jComboCodProvincia.getSelectedIndex()>0)){
    		
    		okRegistral= (jComboCodProvincia!=null && jComboCodProvincia.getSelectedItem()!=null && jComboCodProvincia.getSelectedIndex()>0) ;
    	}
    	
    	boolean okLocal=false;
    	
    	boolean okUrb=true;
    	//Panel Localización Urbana obligatoria
    	if( (jComboBoxTipoVia!=null && jComboBoxTipoVia.getSelectedPatron()!=null && !(jComboBoxTipoVia.getSelectedPatron().equals("")))
    			|| (jComboBoxEscalera!=null && jComboBoxEscalera.getSelectedItem()!=null && jComboBoxEscalera.getSelectedIndex()>0)
    			|| (jComboBoxPlanta!=null && jComboBoxPlanta.getSelectedItem()!=null && jComboBoxPlanta.getSelectedIndex()>0)
    			|| (jComboBoxPuerta!=null && jComboBoxPuerta.getSelectedItem()!=null && jComboBoxPuerta.getSelectedIndex()>0)
    			|| (jTextFieldDireccion.getText()!=null && !jTextFieldDireccion.getText().equalsIgnoreCase(""))
    			|| (jTextFieldCodigoVia.getText()!=null && !jTextFieldCodigoVia.getText().equalsIgnoreCase(""))
    			|| (jTextFieldNumero.getText()!=null && !jTextFieldNumero.getText().equalsIgnoreCase(""))
    			|| (jTextFieldLetra.getText()!=null && !jTextFieldLetra.getText().equalsIgnoreCase(""))
    			|| (jTextFieldNumeroD.getText()!=null && !jTextFieldNumeroD.getText().equalsIgnoreCase(""))
    			|| (jTextFieldLetraD.getText()!=null && !jTextFieldLetraD.getText().equalsIgnoreCase(""))
    			|| (jTextFieldBloque.getText()!=null && !jTextFieldBloque.getText().equalsIgnoreCase(""))
    			|| (jTextFieldDirNoEstructurada.getText()!=null && !jTextFieldDirNoEstructurada.getText().equalsIgnoreCase(""))
    			|| (jTextFieldKm.getText()!=null && !jTextFieldKm.getText().equalsIgnoreCase("")) ){
    		okLocal=true;
    		if( (jTextFieldPoligono.getText()!=null && !jTextFieldPoligono.getText().equalsIgnoreCase(""))
        			|| (jTextFieldParcela.getText()!=null && !jTextFieldParcela.getText().equalsIgnoreCase(""))
        			|| (jTextFieldParaje.getText()!=null && !jTextFieldParaje.getText().equalsIgnoreCase(""))
        			|| (jTextFieldZona.getText()!=null && !jTextFieldZona.getText().equalsIgnoreCase(""))
        			|| (jComboBoxMunicipioAgregado!=null && jComboBoxMunicipioAgregado.getSelectedItem()!=null && jComboBoxMunicipioAgregado.getSelectedIndex()>0) ){
        		okUrb = (jComboBoxTipoVia!=null && jComboBoxTipoVia.getSelectedPatron()!=null && !(jComboBoxTipoVia.getSelectedPatron().equals(""))) &&
	        		(jTextFieldDireccion.getText()!=null && !jTextFieldDireccion.getText().equalsIgnoreCase("")) &&
	        		(jTextFieldCodigoVia.getText()!=null && !jTextFieldCodigoVia.getText().equalsIgnoreCase("")) &&	        		
	        		(jTextFieldPoligono.getText()!=null && !jTextFieldPoligono.getText().equalsIgnoreCase("")) &&
	        		(jTextFieldParcela.getText()!=null && !jTextFieldParcela.getText().equalsIgnoreCase(""));
    		}else{    		
	    		okUrb = (jComboBoxTipoVia!=null && jComboBoxTipoVia.getSelectedPatron()!=null && !(jComboBoxTipoVia.getSelectedPatron().equals(""))) &&
	    			(jTextFieldDireccion.getText()!=null && !jTextFieldDireccion.getText().equalsIgnoreCase("") &&
	    			(jTextFieldCodigoVia.getText()!=null && !jTextFieldCodigoVia.getText().equalsIgnoreCase("")));
    		}
    	}
    	
    	boolean okRust=true;
    	//Panel Localización rústica obligatoria
    	if( (jTextFieldPoligono.getText()!=null && !jTextFieldPoligono.getText().equalsIgnoreCase(""))
    			|| (jTextFieldParcela.getText()!=null && !jTextFieldParcela.getText().equalsIgnoreCase(""))
    			|| (jTextFieldParaje.getText()!=null && !jTextFieldParaje.getText().equalsIgnoreCase(""))
    			|| (jTextFieldZona.getText()!=null && !jTextFieldZona.getText().equalsIgnoreCase(""))
    			|| (jComboBoxMunicipioAgregado!=null && jComboBoxMunicipioAgregado.getSelectedItem()!=null && jComboBoxMunicipioAgregado.getSelectedIndex()>0) ){
    		okLocal=true;
    		if( (jComboBoxTipoVia!=null && jComboBoxTipoVia.getSelectedPatron()!=null && !(jComboBoxTipoVia.getSelectedPatron().equals("")))
    				|| (jComboBoxEscalera!=null && jComboBoxEscalera.getSelectedItem()!=null && jComboBoxEscalera.getSelectedIndex()>0)
        			|| (jComboBoxPlanta!=null && jComboBoxPlanta.getSelectedItem()!=null && jComboBoxPlanta.getSelectedIndex()>0)
        			|| (jComboBoxPuerta!=null && jComboBoxPuerta.getSelectedItem()!=null && jComboBoxPuerta.getSelectedIndex()>0)
        			|| (jTextFieldDireccion.getText()!=null && !jTextFieldDireccion.getText().equalsIgnoreCase(""))
        			|| (jTextFieldCodigoVia.getText()!=null && !jTextFieldCodigoVia.getText().equalsIgnoreCase(""))
	    			|| (jTextFieldNumero.getText()!=null && !jTextFieldNumero.getText().equalsIgnoreCase(""))
	    			|| (jTextFieldLetra.getText()!=null && !jTextFieldLetra.getText().equalsIgnoreCase(""))
	    			|| (jTextFieldNumeroD.getText()!=null && !jTextFieldNumeroD.getText().equalsIgnoreCase(""))
	    			|| (jTextFieldLetraD.getText()!=null && !jTextFieldLetraD.getText().equalsIgnoreCase(""))
	    			|| (jTextFieldBloque.getText()!=null && !jTextFieldBloque.getText().equalsIgnoreCase(""))
	    			|| (jTextFieldDirNoEstructurada.getText()!=null && !jTextFieldDirNoEstructurada.getText().equalsIgnoreCase(""))
	    			|| (jTextFieldKm.getText()!=null && !jTextFieldKm.getText().equalsIgnoreCase("")) ){
    			okRust=(jTextFieldPoligono.getText()!=null && !jTextFieldPoligono.getText().equalsIgnoreCase("")) &&
					(jTextFieldParcela.getText()!=null && !jTextFieldParcela.getText().equalsIgnoreCase("")) &&					
					(jComboBoxTipoVia!=null && jComboBoxTipoVia.getSelectedPatron()!=null && !(jComboBoxTipoVia.getSelectedPatron().equals(""))) &&
	    			(jTextFieldDireccion.getText()!=null && !jTextFieldDireccion.getText().equalsIgnoreCase("") &&
	    					(jTextFieldCodigoVia.getText()!=null && !jTextFieldCodigoVia.getText().equalsIgnoreCase("")));
    		}else{
	    		okRust=(jTextFieldPoligono.getText()!=null && !jTextFieldPoligono.getText().equalsIgnoreCase("")) &&
					(jTextFieldParcela.getText()!=null && !jTextFieldParcela.getText().equalsIgnoreCase("")) &&
					(jTextFieldParaje.getText()!=null && !jTextFieldParaje.getText().equalsIgnoreCase(""));
    		}   		
    	}
    	boolean anioFinPrecioVenta = true;
    	if((jTextFieldAnioFin.getText()!=null && jTextFieldAnioFin.getText().length()!=0 && jTextFieldAnioFin.getText().length()!=4)){
    		JOptionPane.showMessageDialog(this,I18N.get("Expedientes", "Error.J9"));
    		anioFinPrecioVenta=false;
    	}
    	
    	// se añade comprobación del panel de datos de identificación
    	return anioFinPrecioVenta && okBase && okRegistral && okLocal && okUrb && okRust && (jTextFieldRefCatastral1.getText()!=null && !jTextFieldRefCatastral1.getText().equalsIgnoreCase("")) 
 		&& (jTextFieldRefCatastral2.getText()!=null && !jTextFieldRefCatastral2.getText().equalsIgnoreCase(""))
 		&& (jTextFieldCargo.getText()!=null && !jTextFieldCargo.getText().equalsIgnoreCase(""))
 		&& (jTextFieldDC1.getText()!=null && jTextFieldDC1.getText().length()==1)
 		&& (jTextFieldDC2.getText()!=null && jTextFieldDC2.getText().length()==1)
 		&& (jComboBoxClase!=null && jComboBoxClase.getSelectedPatron()!=null && !(jComboBoxClase.getSelectedPatron().equals("")));
 		
    }  
}
