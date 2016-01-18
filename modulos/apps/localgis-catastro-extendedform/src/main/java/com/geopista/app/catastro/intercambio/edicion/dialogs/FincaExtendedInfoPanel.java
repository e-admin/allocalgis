/**
 * FincaExtendedInfoPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.intercambio.edicion.dialogs;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.catastro.intercambio.edicion.FincaExtendedInfoDialog;
import com.geopista.app.catastro.intercambio.edicion.ParajesSistemaDialog;
import com.geopista.app.catastro.intercambio.edicion.ViasSistemaDialog;
import com.geopista.app.catastro.intercambio.edicion.utils.EdicionOperations;
import com.geopista.app.catastro.intercambio.edicion.utils.EdicionUtils;
import com.geopista.app.catastro.intercambio.edicion.utils.UbicacionListCellRenderer;
import com.geopista.app.catastro.intercambio.images.IconLoader;
import com.geopista.app.catastro.model.beans.ConstantesRegExp;
import com.geopista.app.catastro.model.beans.DireccionLocalizacion;
import com.geopista.app.catastro.model.beans.FincaCatastro;
import com.geopista.app.catastro.model.beans.Municipio;
import com.geopista.app.catastro.model.beans.Paraje;
import com.geopista.app.catastro.model.beans.ReferenciaCatastral;
import com.geopista.app.catastro.registroExpedientes.utils.UtilRegistroExp;
import com.geopista.app.utilidades.TextField;
import com.geopista.app.utilidades.comboBoxTipo.ComboBoxKeyTipoDestinoSelectionManager;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.Blackboard;


public class FincaExtendedInfoPanel extends JPanel
{
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    private Blackboard Identificadores = aplicacion.getBlackboard();  //  @jve:decl-index=0:
  
    
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1L;
    private JPanel jPanelDatosIdentificacion = null;
    private JPanel jPanelDatosLocalizacion = null;
    private JPanel jPanelDatosFisicos = null;
    private JPanel jPanelDatosEconomicos = null;
    private JLabel jLabelRefCatastral1 = null;
    private JLabel jLabelRefCatastral2 = null;
    private JTextField jTextFieldRefCatastral1 = null;
    private JTextField jTextFieldRefCatastral2 = null;
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
    private JLabel jLabelDirNoEstructurada = null;
    private JLabel jLabelKm = null;
    private JTextField jTextFieldNumero = null;
    private JTextField jTextFieldLetra = null;
    private JTextField jTextFieldNumeroD = null;
    private JTextField jTextFieldLetraD = null;
    private JTextField jTextFieldBloque = null;
    private JTextField jTextFieldDirNoEstructurada = null;
    private JTextField jTextFieldKm = null;
    private JLabel jLabelPoligono = null;
    private JLabel jLabelParcela = null;
    private JLabel jLabelParaje = null;
    private JLabel jLabelZona = null;
    private JLabel jLabelMunicipioAgregado = null;
    private JTextField jTextFieldParcela = null;
    private JTextField jTextFieldParaje = null;
    private JButton jButtonBuscarParaje = null;
    private JTextField jTextFieldZona = null;
    private JComboBox jComboBoxMunicipioAgregado = null;
    private JLabel jLabelSolar = null;
    private JLabel jLabelConstruidaTotal = null;
    private JLabel jLabelSobreRasante = null;
    private JLabel jLabelCubierta = null;
    private JLabel jLabelBajoRasante = null;
    private JTextField jTextFieldSolar = null;
    private JTextField jTextFieldConstruidaTotal = null;
    private JTextField jTextFieldSobreRasante = null;
    private JTextField jTextFieldCubierta = null;
    private JTextField jTextFieldBajoRasante = null;
    private JLabel jLabelAnioPonencia = null;
    private JLabel jLabelFormaCalculo = null;
    private JLabel jLabelPoligonoValoracion = null;
    private JComboBox jComboBoxFormaCalculo = null;
    private JTextField jTextFieldPoligonoValoracion = null;
    private boolean okPressed = false;
	private JLabel jLabelFechaAlteracion = null;
	private JTextField jTextFieldFechaAlteracion = null;
	private JComboBox jComboBoxPoligono = null;
	private JLabel jLabelVuelo = null;
	private JLabel jLabelInfraedificada = null;
	private JTextField jTextFieldInfraedificada = null;
	private JComboBox jComboBoxAnioPonencia = null;
	private JComboBox jComboBoxPoligonoValoracion = null;
	private ComboBoxEstructuras jComboBoxVuelo = null;
	private Paraje paraje = null;
    /**
     * This method initializes
     * 
     */
    public FincaExtendedInfoPanel()
    {
        super();
        initialize();
    }
    public FincaExtendedInfoPanel(FincaCatastro finca)
    {
        super();
        initialize();
        loadData (finca);
    }

    public class ListenerTextField implements  DocumentListener{
    	/*
    	 * Método que recibe evento cuando se escribe en alguno de los jTextField que tienen un escuchador DocumentListener
    	 * Según el campo origen del evento, cambia las etiquetas obligatorias.
    	 * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
    	 */
        public void insertUpdate(DocumentEvent e){
        	//Panel rústica obligatoria
			if(jTextFieldParaje.getDocument()==e.getDocument() || jTextFieldParcela.getDocument()==e.getDocument() || jTextFieldZona.getDocument()==e.getDocument()){
				jLabelParcela.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "finca.extended.rustica.parcela")));			            
			    jLabelPoligono.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "finca.extended.rustica.poligono")));
			    if(jTextFieldDireccion.getDocument().getLength()<=0 && jComboBoxTipoVia.getSelectedIndex()<=0 && jTextFieldNumero.getDocument().getLength()<=0 && 
			    		jTextFieldLetra.getDocument().getLength()<=0 && jTextFieldNumeroD.getDocument().getLength()<=0 && jTextFieldLetraD.getDocument().getLength()<=0 && 
			    		jTextFieldBloque.getDocument().getLength()<=0 && jTextFieldDirNoEstructurada.getDocument().getLength()<=0 && jTextFieldKm.getDocument().getLength()<=0 &&
			    		jTextFieldCodigoVia.getDocument().getLength()<=0)
		        	jLabelParaje.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "finca.extended.rustica.paraje")));
			    jPanelDatosLocalizacion.setBorder(BorderFactory.createTitledBorder
	                    (null, I18N.get("Expedientes", "finca.extended.localizacion.titulo"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
			}
			//Panel Datos Económicos
			if(jTextFieldInfraedificada.getDocument()==e.getDocument()){
				jLabelFormaCalculo.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "finca.extended.economicos.formacalculo")));
            	jLabelVuelo.setText(I18N.get("Expedientes", "finca.extended.economicos.vuelo"));
            	jLabelPoligonoValoracion.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "finca.extended.economicos.poligonovaloracion"))); 
                jLabelAnioPonencia.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "finca.extended.economicos.anioponencia")));                
			}
			//Panel Urbana obligatoria
			if(jTextFieldDireccion.getDocument()==e.getDocument() || jTextFieldNumero.getDocument()==e.getDocument() || 
					jTextFieldLetra.getDocument()==e.getDocument() || jTextFieldNumeroD.getDocument()==e.getDocument() || 
					jTextFieldLetraD.getDocument()==e.getDocument() || jTextFieldBloque.getDocument()==e.getDocument() || 
					jTextFieldDirNoEstructurada.getDocument()==e.getDocument() || jTextFieldKm.getDocument()==e.getDocument() ||
					jTextFieldCodigoVia.getDocument()==e.getDocument()){		
				jLabelCodigoVia.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "finca.extended.urbana.codigovia")));
				jLabelTipoVia.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "finca.extended.urbana.tipovia")));
                jLabelDireccion.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "finca.extended.urbana.direccion")));
                jLabelParaje.setText(I18N.get("Expedientes", "finca.extended.rustica.paraje"));
                jPanelDatosLocalizacion.setBorder(BorderFactory.createTitledBorder
	                    (null, I18N.get("Expedientes", "finca.extended.localizacion.titulo"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
            }
        }
    	/*
    	 * Método que recibe evento cuando se borra caracteres en alguno de los jTextField que tienen un escuchador DocumentListener
    	 * Según el campo origen del evento, cambia las etiquetas obligatorias.
    	 * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
    	 */
        public void removeUpdate(DocumentEvent e){
        	//Panel rústica obligatoria
        	if(jTextFieldParaje.getDocument().getLength()<=0 && jTextFieldParcela.getDocument().getLength()<=0 && jComboBoxMunicipioAgregado.getSelectedIndex()<=0 && jComboBoxPoligono.getSelectedIndex()<=0 &&jTextFieldZona.getDocument().getLength()<=0){
        		jLabelParaje.setText(I18N.get("Expedientes", "finca.extended.rustica.paraje"));
                jLabelParcela.setText(I18N.get("Expedientes", "finca.extended.rustica.parcela"));                
	            jLabelPoligono.setText(I18N.get("Expedientes", "finca.extended.rustica.poligono"));	           
        	}
			//Panel Datos Económicos
        	if(jTextFieldInfraedificada.getDocument().getLength()<=0 && jComboBoxFormaCalculo.getSelectedIndex()==-1 && jComboBoxVuelo.getSelectedIndex()<=0 && jComboBoxAnioPonencia.getSelectedIndex()<=0 && jComboBoxPoligonoValoracion.getSelectedIndex()<=0 && jTextFieldInfraedificada.getDocument().getLength()<=0){
				jLabelFormaCalculo.setText(I18N.get("Expedientes", "finca.extended.economicos.formacalculo"));
            	jLabelVuelo.setText(I18N.get("Expedientes", "finca.extended.economicos.vuelo"));
            	jLabelPoligonoValoracion.setText(I18N.get("Expedientes", "finca.extended.economicos.poligonovaloracion")); 
                jLabelAnioPonencia.setText(I18N.get("Expedientes", "finca.extended.economicos.anioponencia"));
			}
			//Panel Urbana obligatoria
			if(jTextFieldDireccion.getDocument().getLength()<=0 && jComboBoxTipoVia.getSelectedIndex()<=0 && jTextFieldNumero.getDocument().getLength()<=0 && 
					jTextFieldLetra.getDocument().getLength()<=0 && jTextFieldNumeroD.getDocument().getLength()<=0 && 
					jTextFieldLetraD.getDocument().getLength()<=0 && jTextFieldBloque.getDocument().getLength()<=0 && 
					jTextFieldDirNoEstructurada.getDocument().getLength()<=0 && jTextFieldKm.getDocument().getLength()<=0 &&
					jTextFieldCodigoVia.getDocument().getLength()<=0){
				jLabelCodigoVia.setText(I18N.get("Expedientes", "finca.extended.urbana.codigovia"));
				jLabelTipoVia.setText(I18N.get("Expedientes", "finca.extended.urbana.tipovia"));
                jLabelDireccion.setText(I18N.get("Expedientes", "finca.extended.urbana.direccion"));
        	    if(jTextFieldParaje.getDocument().getLength()>0 || jTextFieldParcela.getDocument().getLength()>0 || jTextFieldZona.getDocument().getLength()>0 || jComboBoxPoligono.getSelectedIndex()>0 || jComboBoxMunicipioAgregado.getSelectedIndex()>0){
	    				jLabelParcela.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "finca.extended.rustica.parcela")));			            
	    			    jLabelPoligono.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "finca.extended.rustica.poligono")));	    			   
	    		        jLabelParaje.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "finca.extended.rustica.paraje")));
	    		}else{
	    			 jPanelDatosLocalizacion.setBorder(BorderFactory.createTitledBorder
     	                    (null, "* "+I18N.get("Expedientes", "finca.extended.localizacion.titulo"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
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
        	//Panel Rústica obligatoria
        	if(jComboBoxMunicipioAgregado.getSelectedIndex()>0 || jComboBoxPoligono.getSelectedIndex()>0 || jTextFieldParaje.getDocument().getLength()>0 || jTextFieldParcela.getDocument().getLength()>0 || jTextFieldZona.getDocument().getLength()>0){        		
	            jLabelParcela.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "finca.extended.rustica.parcela")));	            
	            jLabelPoligono.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "finca.extended.rustica.poligono")));
	            if(jTextFieldDireccion.getDocument().getLength()<=0 && jComboBoxTipoVia.getSelectedIndex()<=0 && jTextFieldNumero.getDocument().getLength()<=0 && jTextFieldLetra.getDocument().getLength()<=0 &&
	            		jTextFieldNumeroD.getDocument().getLength()<=0 && jTextFieldLetraD.getDocument().getLength()<=0 && 
	            		jTextFieldBloque.getDocument().getLength()<=0 && jTextFieldDirNoEstructurada.getDocument().getLength()<=0 && 
	            		jTextFieldKm.getDocument().getLength()<=0 && jTextFieldCodigoVia.getDocument().getLength()<=0)
	            	jLabelParaje.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "finca.extended.rustica.paraje")));
	            jPanelDatosLocalizacion.setBorder(BorderFactory.createTitledBorder
	                    (null, I18N.get("Expedientes", "finca.extended.localizacion.titulo"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 

        	}else{
        		jLabelParaje.setText(I18N.get("Expedientes", "finca.extended.rustica.paraje"));
                jLabelParcela.setText(I18N.get("Expedientes", "finca.extended.rustica.parcela"));                 
	            jLabelPoligono.setText(I18N.get("Expedientes", "finca.extended.rustica.poligono"));
        	}
        	//Panel Datos Económicos
        	if(jComboBoxFormaCalculo.getSelectedIndex()!=-1 || jComboBoxVuelo.getSelectedIndex()>0 || jComboBoxAnioPonencia.getSelectedIndex()>0 || jComboBoxPoligonoValoracion.getSelectedIndex()>0 || jTextFieldInfraedificada.getDocument().getLength()>0){
            	jLabelFormaCalculo.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "finca.extended.economicos.formacalculo")));
            	jLabelVuelo.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "finca.extended.economicos.vuelo")));
            	jLabelPoligonoValoracion.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "finca.extended.economicos.poligonovaloracion"))); 
                jLabelAnioPonencia.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "finca.extended.economicos.anioponencia")));                
            }else{
            	jLabelFormaCalculo.setText(I18N.get("Expedientes", "finca.extended.economicos.formacalculo"));
            	jLabelVuelo.setText(I18N.get("Expedientes", "finca.extended.economicos.vuelo"));
            	jLabelPoligonoValoracion.setText(I18N.get("Expedientes", "finca.extended.economicos.poligonovaloracion")); 
                jLabelAnioPonencia.setText(I18N.get("Expedientes", "finca.extended.economicos.anioponencia"));                
            }
        	//Panel Urbana obligatoria
        	if(jComboBoxTipoVia.getSelectedIndex()>0 || jTextFieldDireccion.getDocument().getLength()>0 || 
        			jTextFieldNumero.getDocument().getLength()>0 || jTextFieldLetra.getDocument().getLength()>0 || 
        			jTextFieldLetraD.getDocument().getLength()>0 || jTextFieldNumeroD.getDocument().getLength()>0 ||
        			jTextFieldBloque.getDocument().getLength()>0 || jTextFieldDirNoEstructurada.getDocument().getLength()>0 || 
        			jTextFieldKm.getDocument().getLength()>0 || jTextFieldCodigoVia.getDocument().getLength()>0){
        		jLabelCodigoVia.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "finca.extended.urbana.codigovia")));
                jLabelTipoVia.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "finca.extended.urbana.tipovia")));
                jLabelDireccion.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "finca.extended.urbana.direccion")));  
        	    if(jTextFieldParaje.getDocument().getLength()>0 || jTextFieldParcela.getDocument().getLength()>0 || jTextFieldZona.getDocument().getLength()>0 || jComboBoxPoligono.getSelectedIndex()>0 || jComboBoxMunicipioAgregado.getSelectedIndex()>0)
        	    	jLabelParaje.setText(I18N.get("Expedientes", "finca.extended.rustica.paraje"));
        	    jPanelDatosLocalizacion.setBorder(BorderFactory.createTitledBorder
	                    (null, I18N.get("Expedientes", "finca.extended.localizacion.titulo"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
        	}else{
        		jLabelCodigoVia.setText(I18N.get("Expedientes", "finca.extended.urbana.codigovia"));
        		jLabelTipoVia.setText(I18N.get("Expedientes", "finca.extended.urbana.tipovia"));
                jLabelDireccion.setText(I18N.get("Expedientes", "finca.extended.urbana.direccion")); 
        	    if(jTextFieldParaje.getDocument().getLength()>0 || jTextFieldParcela.getDocument().getLength()>0 || jTextFieldZona.getDocument().getLength()>0 || jComboBoxPoligono.getSelectedIndex()>0 || jComboBoxMunicipioAgregado.getSelectedIndex()>0){
    				jLabelParcela.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "finca.extended.rustica.parcela")));			            
    			    jLabelPoligono.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "finca.extended.rustica.poligono")));	    			   
    		        jLabelParaje.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "finca.extended.rustica.paraje")));
        	    }else{
        	    	 jPanelDatosLocalizacion.setBorder(BorderFactory.createTitledBorder
     	                    (null, "* "+I18N.get("Expedientes", "finca.extended.localizacion.titulo"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
        	    }
        	}
        }
    }

    
    public void loadData(FincaCatastro finca)
    {
        if (finca!=null)
        {
            //Datos de identificación
        	
        	if(finca.getDatosExpediente().getFechaAlteracion()!=null){
        		if(finca.getDatosExpediente().getFechaAlteracion().getTime()!=0){
        			DateFormat df=new SimpleDateFormat("dd/MM/yyyy");
        			String fecha = df.format(finca.getDatosExpediente().getFechaAlteracion());
        			jTextFieldFechaAlteracion.setText(fecha);
        		}
        		else{
        			jTextFieldFechaAlteracion.setText("");
        		}
        	}
        	else{
        		jTextFieldFechaAlteracion.setText("");
        	}
            jTextFieldRefCatastral1.setText(finca.getRefFinca().getRefCatastral1());
            jTextFieldRefCatastral2.setText(finca.getRefFinca().getRefCatastral2());
          
            //Datos de localización
            jComboBoxTipoVia.addActionListener(new ListenerCombo());
            jComboBoxTipoVia.setSelectedPatron(finca.getDirParcela().getTipoVia());
            
            jTextFieldCodigoVia.getDocument().addDocumentListener(new ListenerTextField()); 
            if(finca.getDirParcela().getCodigoVia() == -1){
            	 jTextFieldCodigoVia.setText("");
            }
            else{
            	jTextFieldCodigoVia.setText(new Integer(finca.getDirParcela().getCodigoVia()).toString());
            }
            
            jTextFieldDireccion.getDocument().addDocumentListener(new ListenerTextField()); 
            jTextFieldDireccion.setText(finca.getDirParcela().getNombreVia());
            jTextFieldNumero.getDocument().addDocumentListener(new ListenerTextField());
            if (finca.getDirParcela().getPrimerNumero() == -1){
            	jTextFieldNumero.setText("");
            }
            else{
            	jTextFieldNumero.setText(EdicionUtils.getStringValue(finca.getDirParcela().getPrimerNumero()));
            }
            jTextFieldLetra.getDocument().addDocumentListener(new ListenerTextField());
            jTextFieldLetra.setText(finca.getDirParcela().getPrimeraLetra());
            jTextFieldNumeroD.getDocument().addDocumentListener(new ListenerTextField()); 
            if (finca.getDirParcela().getSegundoNumero() == -1){
            	jTextFieldNumeroD.setText("");
            }
            else{
            	jTextFieldNumeroD.setText(EdicionUtils.getStringValue(finca.getDirParcela().getSegundoNumero()));
            }
            jTextFieldLetraD.getDocument().addDocumentListener(new ListenerTextField());
            jTextFieldLetraD.setText(finca.getDirParcela().getSegundaLetra());
            jTextFieldBloque.getDocument().addDocumentListener(new ListenerTextField());
            jTextFieldBloque.setText(finca.getDirParcela().getBloque());
            jTextFieldDirNoEstructurada.getDocument().addDocumentListener(new ListenerTextField());
            jTextFieldDirNoEstructurada.setText(finca.getDirParcela().getDireccionNoEstructurada());
            jTextFieldKm.getDocument().addDocumentListener(new ListenerTextField());
            if (finca.getDirParcela().getKilometro() == -1){
            	jTextFieldKm.setText("");
            }
            else{
            	jTextFieldKm.setText(EdicionUtils.getStringValue(finca.getDirParcela().getKilometro()));
            }
            
            jComboBoxPoligono.setSelectedItem(finca.getDirParcela().getCodPoligono());
            jTextFieldParcela.getDocument().addDocumentListener(new ListenerTextField()); 
            jTextFieldParcela.setText(finca.getDirParcela().getCodParcela());          
            jTextFieldParaje.getDocument().addDocumentListener(new ListenerTextField());
            jTextFieldParaje.setText(finca.getDirParcela().getNombreParaje());
            
            jTextFieldZona.getDocument().addDocumentListener(new ListenerTextField()); 
            jTextFieldZona.setText(finca.getDirParcela().getCodZonaConcentracion());
            jComboBoxMunicipioAgregado.addActionListener(new ListenerCombo());
            if(finca.getDirParcela().getCodMunOrigenAgregacion() != null){

                if(finca.getDirParcela().getCodPoligono() != null && finca.getDirParcela().getCodParcela() != null
                        && !finca.getDirParcela().getCodPoligono().equals("") && !finca.getDirParcela().getCodParcela().equals("")){

                    Municipio m = new Municipio();
                    m.getProvincia().setIdProvincia(finca.getDirParcela().getProvinciaINE());
                    m.setIdIne(finca.getDirParcela().getMunicipioINE());
                    m.setIdCatastro(finca.getDirParcela().getCodigoMunicipioDGC());
                    m.setNombreOficial(finca.getDirParcela().getNombreMunicipio());

                    jComboBoxMunicipioAgregado.setSelectedItem(m);
                }
                else
                    jComboBoxMunicipioAgregado.setSelectedIndex(0);
            }
            
            //Datos Físicos de superficie
            jTextFieldSolar.setText(EdicionUtils.getStringValue(finca.getDatosFisicos().getSupFinca()));
            jTextFieldConstruidaTotal.setText(EdicionUtils.getStringValue(finca.getDatosFisicos().getSupTotal()));
            jTextFieldSobreRasante.setText(EdicionUtils.getStringValue(finca.getDatosFisicos().getSupSobreRasante()));
            jTextFieldCubierta.setText(EdicionUtils.getStringValue(finca.getDatosFisicos().getSupCubierta()));
            jTextFieldBajoRasante.setText(EdicionUtils.getStringValue(finca.getDatosFisicos().getSupBajoRasante()));
                        
            //Datos Económicos
            jComboBoxAnioPonencia.addActionListener(new ListenerCombo());
            jComboBoxAnioPonencia.setSelectedItem((EdicionUtils.getStringValue(finca.getDatosEconomicos().getAnioAprobacion())));
            
            jComboBoxFormaCalculo.addActionListener(new ListenerCombo());
            if (finca.getDatosEconomicos().getCodigoCalculoValor()!=null){
            	jComboBoxFormaCalculo.setSelectedIndex(finca.getDatosEconomicos().getCodigoCalculoValor().intValue()+1);
            }
            else{
            	jComboBoxFormaCalculo.setSelectedIndex(0);
            }
            jComboBoxPoligonoValoracion.addActionListener(new ListenerCombo());
            jComboBoxPoligonoValoracion.setSelectedItem(finca.getDatosEconomicos().getPoligonoCatastralValor().getCodPoligono());
            jTextFieldInfraedificada.getDocument().addDocumentListener(new ListenerTextField());
            jTextFieldInfraedificada.setText(finca.getDatosEconomicos().getIndInfraedificabilidad());
        	jComboBoxVuelo.addActionListener(new ListenerCombo());
            if(finca.getDatosEconomicos().getIndModalidadReparto()!=null){
            	jComboBoxVuelo.setSelectedPatron(finca.getDatosEconomicos().getIndModalidadReparto());
            }
           
        }        
    }
    
    public FincaCatastro getFincaCatastro(FincaCatastro finca)
    {
       
    	if (okPressed)
    	{
            if(finca==null)
            {
                finca = new FincaCatastro();
                if(!(Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales")){
            		finca.setTIPO_MOVIMIENTO(finca.TIPO_MOVIMIENTO_ALTA);
            	}
            }else{
            	if(!(Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales")){
            		if(!finca.getTIPO_MOVIMIENTO().equals(finca.TIPO_MOVIMIENTO_ALTA)){
            			finca.setTIPO_MOVIMIENTO(finca.TIPO_MOVIMIENTO_MODIF);
            		}
            	}
            }
            
            finca.setElementoModificado(true);
        	
            //Datos de identificación
            ReferenciaCatastral refCat = new ReferenciaCatastral(jTextFieldRefCatastral1.getText(), jTextFieldRefCatastral2.getText());
            finca.setRefFinca(refCat);
            
            //Datos de localización
            if(jComboBoxTipoVia.getSelectedPatron()!=null)
                finca.getDirParcela().setTipoVia(jComboBoxTipoVia.getSelectedPatron());
            else
            	finca.getDirParcela().setTipoVia("");
            
            if(jTextFieldCodigoVia.getText().equals(""))
                finca.getDirParcela().setCodigoVia(-1);
            else
            	finca.getDirParcela().setCodigoVia(new Integer(jTextFieldCodigoVia.getText()).intValue());
            

            
            finca.getDirParcela().setNombreVia(jTextFieldDireccion.getText());
            if (jTextFieldNumero.getText().length() > 0)
            	finca.getDirParcela().setPrimerNumero(Integer.parseInt(jTextFieldNumero.getText()));
            else if (jTextFieldNumero.getText().length() < 1)
            	finca.getDirParcela().setPrimerNumero(-1);
            finca.getDirParcela().setPrimeraLetra(jTextFieldLetra.getText());
            if (jTextFieldNumeroD.getText().length() > 0)
            	finca.getDirParcela().setSegundoNumero(Integer.parseInt(jTextFieldNumeroD.getText()));
            else if (jTextFieldNumeroD.getText().length() < 1)
            	finca.getDirParcela().setSegundoNumero(-1);
            finca.getDirParcela().setSegundaLetra(jTextFieldLetraD.getText());
            finca.getDirParcela().setBloque(jTextFieldBloque.getText());
            finca.getDirParcela().setDireccionNoEstructurada(jTextFieldDirNoEstructurada.getText());
            if (jTextFieldKm.getText().length() > 0)
            	finca.getDirParcela().setKilometro(Double.parseDouble(jTextFieldKm.getText()));
            else if (jTextFieldKm.getText().length() < 1)
            	finca.getDirParcela().setKilometro(-1);
            
            finca.getDirParcela().setCodPoligono(jComboBoxPoligono.getSelectedItem()!=null?
            		jComboBoxPoligono.getSelectedItem().toString():"");
            finca.getDirParcela().setCodParcela(jTextFieldParcela.getText());
            if(paraje!=null && paraje.getNombre()!=null)
            	finca.getDirParcela().setNombreParaje(paraje.getNombre());
            if(paraje!=null && paraje.getNombre()!=null)
            	finca.getDirParcela().setCodParaje(paraje.getCodigo());
            finca.getDirParcela().setCodZonaConcentracion(jTextFieldZona.getText());

            if (jComboBoxMunicipioAgregado.getSelectedItem()!=null && jComboBoxMunicipioAgregado.getSelectedIndex() != 0)
                finca.getDirParcela().setCodMunOrigenAgregacion(new Integer(((Municipio)jComboBoxMunicipioAgregado.getSelectedItem()).getIdCatastro()).toString());

            //Datos Físicos de superficie
            if (jTextFieldSolar.getText().length()>0)
            	finca.getDatosFisicos().setSupFinca(Long.valueOf(jTextFieldSolar.getText()));
            else if (jTextFieldSolar.getText().length() < 1)
            	finca.getDatosFisicos().setSupFinca(new Long(0));
            if (jTextFieldConstruidaTotal.getText().length()>0)
            	finca.getDatosFisicos().setSupTotal(Long.valueOf(jTextFieldConstruidaTotal.getText()));
            else if (jTextFieldConstruidaTotal.getText().length() < 1)
            	finca.getDatosFisicos().setSupTotal(new Long(0));
            if (jTextFieldSobreRasante.getText().length()>0)
            	finca.getDatosFisicos().setSupSobreRasante(Long.valueOf(jTextFieldSobreRasante.getText()));
            else if (jTextFieldSobreRasante.getText().length() < 1)
            	finca.getDatosFisicos().setSupSobreRasante(new Long(0));
            if (jTextFieldCubierta.getText().length()>0)
            	finca.getDatosFisicos().setSupCubierta(Long.valueOf(jTextFieldCubierta.getText()));
            else if (jTextFieldCubierta.getText().length() < 1)
            	finca.getDatosFisicos().setSupCubierta(new Long(0));
            if (jTextFieldBajoRasante.getText().length()>0)
            	finca.getDatosFisicos().setSupBajoRasante(Long.valueOf(jTextFieldBajoRasante.getText()));
            else if (jTextFieldBajoRasante.getText().length() < 1)
            	finca.getDatosFisicos().setSupBajoRasante(new Long(0));
            
            //Datos Económicos
            
            if (jComboBoxAnioPonencia.getSelectedItem()!=null && !(jComboBoxAnioPonencia.getSelectedItem().toString().equals(""))){
            	int anio = Integer.parseInt(jComboBoxAnioPonencia.getSelectedItem().toString());
                if(anio != 0)
                    finca.getDatosEconomicos().setAnioAprobacion(new Integer(anio));
            }
            
            if (jComboBoxFormaCalculo.getSelectedItem()!=null)
            {
            	if (jComboBoxFormaCalculo.getSelectedItem().toString().length() > 0)
            		finca.getDatosEconomicos().setCodigoCalculoValor(Integer.valueOf(jComboBoxFormaCalculo.getSelectedItem().toString()));
            }
            if(jComboBoxPoligonoValoracion.getSelectedItem()!=null)           
            	finca.getDatosEconomicos().getPoligonoCatastralValor().setCodPoligono(jComboBoxPoligonoValoracion.getSelectedItem().toString());
            
            if (jTextFieldFechaAlteracion.getText()!=null){
        		if (jTextFieldFechaAlteracion.getText().length() < 10)
        			finca.getDatosExpediente().setFechaAlteracion(new Date(0));
        		else if (jTextFieldFechaAlteracion.getText().length() == 10){        			
        			finca.getDatosExpediente().setFechaAlteracion(new Date(jTextFieldFechaAlteracion.getText()));
        		}
        	}
        	else{
        		finca.getDatosExpediente().setFechaAlteracion(new Date(0));
        	}
            if(jTextFieldInfraedificada.getText()!=null){
            	finca.getDatosEconomicos().setIndInfraedificabilidad(jTextFieldInfraedificada.getText());
            }
            
            if(jComboBoxVuelo.getSelectedPatron()!=null)
            	finca.getDatosEconomicos().setIndModalidadReparto(jComboBoxVuelo.getSelectedPatron());

            try
            {
            	if (finca.getDirParcela().getTipoVia()!=null && !finca.getDirParcela().getTipoVia().equals("") && finca.getDirParcela().getNombreVia()!=null && !finca.getDirParcela().getNombreVia().equals("")
            			&& (finca.getDirParcela().getCodPoligono()==null || finca.getDirParcela().getCodPoligono().equals("")) && (finca.getDirParcela().getCodParaje()==null || finca.getDirParcela().getCodParaje().equals(""))
            			&& (finca.getDirParcela().getCodParcela()==null || finca.getDirParcela().getCodParcela().equals(""))){
            		
            		DireccionLocalizacion dir = ConstantesRegExp.clienteCatastro.getViaPorNombreYCodigo(finca.getDirParcela().getNombreVia(), finca.getDirParcela().getCodigoVia());
            		if(dir!=null && dir.getNombreVia()!=null && !dir.getNombreVia().equalsIgnoreCase("")){

            			
            			if(!dir.getNombreVia().equalsIgnoreCase(finca.getDirParcela().getNombreVia()))
                        {
            				finca.getDirParcela().setNombreVia(dir.getNombreVia());
                        }
                        if (dir.getCodigoVia()==-1){
                        	finca.getDirParcela().setCodigoVia(0);
                        }
                        else{
                        	finca.getDirParcela().setCodigoVia(dir.getCodigoVia());
                        }
                        if (finca.getDirParcela().getTipoVia()==null || finca.getDirParcela().getTipoVia().equals("")){
                        	finca.getDirParcela().setTipoVia(dir.getTipoVia());
                        }
            			
//            			if(!dir.getNombreVia().equalsIgnoreCase(finca.getDirParcela().getNombreVia()))
//            				finca.getDirParcela().setNombreVia(dir.getNombreVia());
//            			
//            			/*if (dir.getCodigoVia() == -1){
//            				finca.getDirParcela().setCodigoVia(0);
//            			}
//            			else{
//            				finca.getDirParcela().setCodigoVia(dir.getCodigoVia());
//            			}*/
//            			finca.getDirParcela().setTipoVia(dir.getTipoVia());
            		}
            	}
            	else if ((finca.getDirParcela().getTipoVia()==null || finca.getDirParcela().getTipoVia().equals("")) && (finca.getDirParcela().getNombreVia()==null || finca.getDirParcela().getNombreVia().equals(""))
            			&& finca.getDirParcela().getCodPoligono()!=null && !finca.getDirParcela().getCodPoligono().equals("") && finca.getDirParcela().getCodParaje()!=null && !finca.getDirParcela().getCodParaje().equals("")
            			&& finca.getDirParcela().getCodParcela()!=null && !finca.getDirParcela().getCodParcela().equals("")){
            		finca.getDirParcela().setCodigoVia(-1);
            	}
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

        }
        return finca;
    }
    
    
    private void initialize()
    {      
        Locale loc=I18N.getLocaleAsObject();         
        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.catastro.intercambio.language.Expedientesi18n",loc,this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("Expedientes",bundle);
        
        this.setLayout(new GridBagLayout());
        this.setSize(FincaExtendedInfoDialog.DIM_X, FincaExtendedInfoDialog.DIM_Y);
        this.add(getJPanelDatosIdentificacion(),  new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 0, 5), 0, 0));
        
        this.add(getJPanelDatosLocalizacion(), new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 0, 5), 0, 0));
        this.add(getJPanelDatosFisicos(), new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 0, 5), 0, 0));
        this.add(getJPanelDatosEconomicos(), new GridBagConstraints(0, 3, 1, 1, 0.1, 0.1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 0, 5), 0, 0));
        
        //Inicializa los desplegables        
        if (Identificadores.get("ListaPoligonos")==null)
        {
            EdicionOperations oper = new EdicionOperations();
            ArrayList lst = oper.obtenerPoligonos();
            Identificadores.put("ListaPoligonos", lst);
            EdicionUtils.cargarLista(getJComboBoxPoligono(), lst);
        }
        else
        {
            EdicionUtils.cargarLista(getJComboBoxPoligono(), 
                    (ArrayList)Identificadores.get("ListaPoligonos"));
        }     
        if (Identificadores.get("ListaAnioPonencia")==null)
        {
            EdicionOperations oper = new EdicionOperations();
            ArrayList lst = oper.obtenerAnioPonencia();
            Identificadores.put("ListaAnioPonencia", lst);
            EdicionUtils.cargarLista(getJComboBoxAnioPonencia(), lst);
        }
        else
        {
            EdicionUtils.cargarLista(getJComboBoxAnioPonencia(), 
                    (ArrayList)Identificadores.get("ListaAnioPonencia"));
        }    
        if (Identificadores.get("ListaPoligonoValoracion")==null)
        {
            EdicionOperations oper = new EdicionOperations();
            ArrayList lst = oper.obtenerPoligonoValoracion();
            Identificadores.put("ListaPoligonoValoracion", lst);
            EdicionUtils.cargarLista(getJComboBoxPoligonoValoracion(), lst);
        }
        else
        {
            EdicionUtils.cargarLista(getJComboBoxPoligonoValoracion(), 
                    (ArrayList)Identificadores.get("ListaPoligonoValoracion"));
        }    
        if (Identificadores.get("ListaMunicipios")==null)
        {
            EdicionOperations oper = new EdicionOperations();
            ArrayList lst = oper.obtenerMunicipios();
            Identificadores.put("ListaMunicipios", lst);
            EdicionUtils.cargarLista(getJComboBoxMunicipioAgregado(), lst);
        }
        else
        {
            EdicionUtils.cargarLista(getJComboBoxMunicipioAgregado(), 
                    (ArrayList)Identificadores.get("ListaMunicipios"));
        } 
        getJComboBoxMunicipioAgregado().setSelectedIndex(0);
    }
    
    public FincaExtendedInfoPanel(GridBagLayout layout)
    {
        super(layout);
        initialize();
    }
    
    public FincaExtendedInfoPanel(GridBagLayout layout, boolean isNew)
    {
    	this(layout);
    	getJTextFieldRefCatastral1().setEditable(isNew);
    	getJTextFieldRefCatastral2().setEditable(isNew);
    }
    
       
    public String validateInput()
    {
    	return null;
    }
    
    public void okPressed(boolean ok)
    {
          okPressed = ok;
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
            jLabelRefCatastral2 = new JLabel("", JLabel.CENTER); 
            jLabelRefCatastral2.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "finca.extended.identificacion.catastral2"))); 
            
            jLabelRefCatastral1 = new JLabel("", JLabel.CENTER); 
            jLabelRefCatastral1.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "finca.extended.identificacion.catastral1")));
            
            jLabelFechaAlteracion  = new JLabel("", JLabel.CENTER); 
            jLabelFechaAlteracion.setText(I18N.get("Expedientes", "finca.extended.identificacion.fechaalteracion"));
            
            jPanelDatosIdentificacion = new JPanel(new GridBagLayout());
            jPanelDatosIdentificacion.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("Expedientes", "finca.extended.identificacion.titulo"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
            jPanelDatosIdentificacion.add(jLabelRefCatastral1, 
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosIdentificacion.add(jLabelRefCatastral2,
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosIdentificacion.add(jLabelFechaAlteracion,
                    new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
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
            jPanelDatosIdentificacion.add(getJTextFieldFechaAlteracion(), 
                    new GridBagConstraints(2, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            
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
                    (null, I18N.get("Expedientes", "finca.extended.localizacion.titulo"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
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
     * This method initializes jPanelDatosFisicos	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanelDatosFisicos()
    {
        if (jPanelDatosFisicos == null)
        {
           
            jLabelBajoRasante = new JLabel("", JLabel.CENTER);             
            jLabelBajoRasante.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "finca.extended.fisicos.bajorasante")));
           
            jLabelCubierta = new JLabel("", JLabel.CENTER); 
            jLabelCubierta.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "finca.extended.fisicos.cubierta")));
           
            jLabelSobreRasante = new JLabel("", JLabel.CENTER);
            jLabelSobreRasante.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "finca.extended.fisicos.sobrerasante")));
           
            jLabelConstruidaTotal = new JLabel("", JLabel.CENTER); 
            jLabelConstruidaTotal.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "finca.extended.fisicos.construidatotal")));
            
            jLabelSolar = new JLabel("", JLabel.CENTER);
            jLabelSolar.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes", "finca.extended.fisicos.solar")));
            jPanelDatosFisicos = new JPanel(new GridBagLayout());
            jPanelDatosFisicos.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("Expedientes", "finca.extended.fisicos.titulo"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
            jPanelDatosFisicos.add(jLabelSolar, 
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosFisicos.add(jLabelConstruidaTotal, 
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosFisicos.add(jLabelSobreRasante, 
                    new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosFisicos.add(jLabelCubierta, 
                    new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosFisicos.add(jLabelBajoRasante, 
                    new GridBagConstraints(4, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosFisicos.add(getJTextFieldSolar(), 
                    new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosFisicos.add(getJTextFieldConstruidaTotal(), 
                    new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosFisicos.add(getJTextFieldSobreRasante(), 
                    new GridBagConstraints(2, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosFisicos.add(getJTextFieldCubierta(), 
                    new GridBagConstraints(3, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosFisicos.add(getJTextFieldBajoRasante(), 
                    new GridBagConstraints(4, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            
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
            
            jLabelPoligonoValoracion = new JLabel("", JLabel.CENTER); 
            jLabelPoligonoValoracion.setText(I18N.get("Expedientes", "finca.extended.economicos.poligonovaloracion")); 
           
            jLabelFormaCalculo = new JLabel("", JLabel.CENTER); 
            jLabelFormaCalculo.setText(I18N.get("Expedientes", "finca.extended.economicos.formacalculo")); 
          
            jLabelAnioPonencia = new JLabel("", JLabel.CENTER); 
            jLabelAnioPonencia.setText(I18N.get("Expedientes", "finca.extended.economicos.anioponencia"));
            
            jLabelVuelo  = new JLabel("", JLabel.CENTER); 
            jLabelVuelo.setText(I18N.get("Expedientes", "finca.extended.economicos.vuelo"));
            
            jLabelInfraedificada   = new JLabel("", JLabel.CENTER); 
            jLabelInfraedificada.setText(I18N.get("Expedientes", "finca.extended.economicos.infraedificada"));
            
            jPanelDatosEconomicos = new JPanel(new GridBagLayout());
            jPanelDatosEconomicos.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("Expedientes", "finca.extended.economicos.titulo"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
            jPanelDatosEconomicos.add(jLabelAnioPonencia, 
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosEconomicos.add(jLabelFormaCalculo, 
                    new GridBagConstraints(1, 0, 2, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosEconomicos.add(jLabelPoligonoValoracion, 
                    new GridBagConstraints(3, 0, 2, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosEconomicos.add(jLabelVuelo, 
                    new GridBagConstraints(5, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosEconomicos.add(jLabelInfraedificada, 
                    new GridBagConstraints(6, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            
            jPanelDatosEconomicos.add(getJComboBoxAnioPonencia(), 
                    new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosEconomicos.add(getJComboBoxFormaCalculo(), 
                    new GridBagConstraints(1, 1, 2, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            
            jPanelDatosEconomicos.add(getJComboBoxPoligonoValoracion(), 
                    new GridBagConstraints(3, 1, 2, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            
            jPanelDatosEconomicos.add(getJComboBoxVuelo(), 
                    new GridBagConstraints(5, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosEconomicos.add(getJTextFieldInfraedificada(), 
                    new GridBagConstraints(6, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            
            EdicionUtils.crearMallaPanel(2, 7, jPanelDatosEconomicos, 0.1, 0.1, GridBagConstraints.CENTER, 
            		GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0);
                        
        }
        return jPanelDatosEconomicos;
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
        }
        return jTextFieldRefCatastral2;
    }
    
    private JTextField getJTextFieldFechaAlteracion()
    {
        if (jTextFieldFechaAlteracion  == null)
        {
        	jTextFieldFechaAlteracion = new TextField(10);
        }
        return jTextFieldFechaAlteracion;
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
            jLabelKm = new JLabel("", JLabel.CENTER); 
            jLabelKm.setText(I18N.get("Expedientes", "finca.extended.urbana.km")); 
            jLabelDirNoEstructurada = new JLabel("", JLabel.CENTER); 
            jLabelDirNoEstructurada.setText(I18N.get("Expedientes", "finca.extended.urbana.dirnoestructurada")); 
            jLabelBloque = new JLabel("", JLabel.CENTER); 
            jLabelBloque.setText(I18N.get("Expedientes", "finca.extended.urbana.bloque")); 
            jLabelLetraD = new JLabel("", JLabel.CENTER); 
            jLabelLetraD.setText(I18N.get("Expedientes", "finca.extended.urbana.letraD")); 
            jLabelNumeroD = new JLabel("", JLabel.CENTER); 
            jLabelNumeroD.setText(I18N.get("Expedientes", "finca.extended.urbana.numeroD")); 
            jLabelLetra = new JLabel("", JLabel.CENTER); 
            jLabelLetra.setText(I18N.get("Expedientes", "finca.extended.urbana.letra")); 
            jLabelNumero = new JLabel("", JLabel.CENTER); 
            jLabelNumero.setText(I18N.get("Expedientes", "finca.extended.urbana.numero")); 
            jLabelDireccion = new JLabel("", JLabel.CENTER); 
            jLabelDireccion.setText(I18N.get("Expedientes", "finca.extended.urbana.direccion")); 
            jLabelTipoVia = new JLabel("", JLabel.CENTER); 
            jLabelTipoVia.setText(I18N.get("Expedientes", "finca.extended.urbana.tipovia")); 
            jLabelCodigoVia = new JLabel("", JLabel.CENTER); 
            jLabelCodigoVia.setText(I18N.get("Expedientes", "finca.extended.urbana.codigovia")); 
            jPanelUrbana = new JPanel(new GridBagLayout());
            jPanelUrbana.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("Expedientes", "finca.extended.urbana.titulo"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
            jPanelUrbana.add(jLabelTipoVia, 
                    new GridBagConstraints(0, 0, 2, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelUrbana.add(jLabelCodigoVia, 
                    new GridBagConstraints(2, 0, 2, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelUrbana.add(jLabelDireccion, 
                    new GridBagConstraints(4, 0, 6, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelUrbana.add(getJComboBoxTipoVia(), 
                    new GridBagConstraints(0, 1, 2, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelUrbana.add(getJTextFieldCodigoVia(), 
                    new GridBagConstraints(2, 1, 2, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelUrbana.add(getJTextFieldDireccion(), 
                    new GridBagConstraints(4, 1, 5, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelUrbana.add(getJButtonBuscarDireccion(), 
                    new GridBagConstraints(9, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
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
            jPanelUrbana.add(jLabelDirNoEstructurada, 
                    new GridBagConstraints(5, 3, 4, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelUrbana.add(jLabelKm, 
                    new GridBagConstraints(9, 3, 1, 1, 0.1, 0.1,
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
            jPanelUrbana.add(getJTextFieldDirNoEstructurada(), 
                    new GridBagConstraints(5, 4, 4, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 300, 0));
            jPanelUrbana.add(getJTextFieldKm(), 
                    new GridBagConstraints(9, 4, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
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
            jLabelMunicipioAgregado = new JLabel("", JLabel.CENTER); 
            jLabelMunicipioAgregado.setText(I18N.get("Expedientes", "finca.extended.rustica.municipioagregado")); 
           
            jLabelZona = new JLabel("", JLabel.CENTER); 
            jLabelZona.setText(I18N.get("Expedientes", "finca.extended.rustica.zona")); 
           
            jLabelParaje = new JLabel("", JLabel.CENTER); 
            jLabelParaje.setText(I18N.get("Expedientes", "finca.extended.rustica.paraje")); 
          
            jLabelParcela = new JLabel("", JLabel.CENTER); 
            jLabelParcela.setText(I18N.get("Expedientes", "finca.extended.rustica.parcela")); 
          
            jLabelPoligono = new JLabel("", JLabel.CENTER); 
            jLabelPoligono.setText(I18N.get("Expedientes", "finca.extended.rustica.poligono")); 
            jPanelRustica = new JPanel(new GridBagLayout());
            jPanelRustica.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("Expedientes", "finca.extended.rustica.titulo"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
            jPanelRustica.add(jLabelPoligono, 
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelRustica.add(jLabelParcela, 
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelRustica.add(jLabelParaje, 
                    new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 50, 0));
            jPanelRustica.add(jLabelZona, 
                    new GridBagConstraints(4, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelRustica.add(jLabelMunicipioAgregado, 
                    new GridBagConstraints(5, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelRustica.add(getJComboBoxPoligono(), 
                    new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelRustica.add(getJTextFieldParcela(), 
                    new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelRustica.add(getJTextFieldParaje(), 
                    new GridBagConstraints(2, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 180, 0));
            jPanelRustica.add(getJButtonBuscarParaje(), 
                    new GridBagConstraints(3, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelRustica.add(getJTextFieldZona(), 
                    new GridBagConstraints(4, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelRustica.add(getJComboBoxMunicipioAgregado(), 
                    new GridBagConstraints(5, 1, 1, 1, 0.1, 0.1,
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
            jTextFieldDireccion = new TextField(25);
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
        	jTextFieldCodigoVia = new TextField(25);
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
                    
                    jTextFieldDireccion.setText(nombreVia);
                    if(dialog.getCodigoVia() == -1){
                    	jTextFieldCodigoVia.setText("");
                    }
                    else{
                    	jTextFieldCodigoVia.setText(new Integer(dialog.getCodigoVia()).toString());
                    }
                    jComboBoxTipoVia.setSelectedPatron(dialog.getTipoVia());
                }
                    });
            jButtonBuscarDireccion.setName("_buscarvia");
        }
        return jButtonBuscarDireccion;
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
            jTextFieldNumero = new JTextField();//new JNumberTextField(JNumberTextField.NUMBER);//, new Integer(9999));
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
            jTextFieldNumeroD = new JTextField();//new JNumberTextField(JNumberTextField.NUMBER);//, new Integer(9999));
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
     * This method initializes jTextFieldDirNoEstruc	
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
                    EdicionUtils.chequeaLongYDecimalCampoEdit(jTextFieldKm, 6, aplicacion.getMainFrame());
                }
                    });
        }
        jTextFieldKm.getDocument().addDocumentListener(new ListenerTextField()); 
        return jTextFieldKm;
    }
    
    /**
     * This method initializes jTextFieldPoligono	
     * 	
     * @return javax.swing.JTextField	
     */
        
    private JComboBox getJComboBoxPoligono()
    {
        if (jComboBoxPoligono  == null)
        {
        	jComboBoxPoligono = new JComboBox();
        }
        jComboBoxPoligono.addActionListener(new ListenerCombo());
        return jComboBoxPoligono;
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
            jTextFieldZona = new TextField(2);//new JNumberTextField(JNumberTextField.NUMBER);//, new Integer(99));
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
    
    /**
     * This method initializes jTextFieldSolar	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getJTextFieldSolar()
    {	
        if (jTextFieldSolar == null)
        {
            jTextFieldSolar = new JTextField();
            jTextFieldSolar.addCaretListener(new CaretListener()
                    {
                public void caretUpdate(CaretEvent evt)
                {
                    EdicionUtils.chequeaLongYNumCampoEdit(jTextFieldSolar, 7, aplicacion.getMainFrame());
                }
                    });
        }
        return jTextFieldSolar;
    }
    
    /**
     * This method initializes jTextFieldConstruidaTotal	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getJTextFieldConstruidaTotal()
    {
        if (jTextFieldConstruidaTotal == null)
        {
            jTextFieldConstruidaTotal = new JTextField(7);
            jTextFieldConstruidaTotal.addCaretListener(new CaretListener()
                    {
                public void caretUpdate(CaretEvent evt)
                {
                    EdicionUtils.chequeaLongYNumCampoEdit(jTextFieldConstruidaTotal,7, aplicacion.getMainFrame());
                    
                }
                    });
        }
        return jTextFieldConstruidaTotal;
    }
    
    /**
     * This method initializes jTextFieldSobreRasante	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getJTextFieldSobreRasante()
    {
        if (jTextFieldSobreRasante == null)
        {
            jTextFieldSobreRasante = new JTextField();
            jTextFieldSobreRasante.addCaretListener(new CaretListener()
                    {
                public void caretUpdate(CaretEvent evt)
                {
                    EdicionUtils.chequeaLongYNumCampoEdit(jTextFieldSobreRasante,7, aplicacion.getMainFrame());
                    
                    calcularSuperficieBajoRasante();
                }
                    });
        }
        return jTextFieldSobreRasante;
    }
    
    /**
     * This method initializes jTextFieldCubierta	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getJTextFieldCubierta()
    {
        if (jTextFieldCubierta == null)
        {
            jTextFieldCubierta = new JTextField();
            jTextFieldCubierta.addCaretListener(new CaretListener()
                    {
                public void caretUpdate(CaretEvent evt)
                {
                    EdicionUtils.chequeaLongYNumCampoEdit(jTextFieldCubierta,7, aplicacion.getMainFrame());
                }
                    });
        }
        return jTextFieldCubierta;
    }
    
    /**
     * This method initializes jTextFieldBajoRasante	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getJTextFieldBajoRasante()
    {
        if (jTextFieldBajoRasante == null)
        {
            jTextFieldBajoRasante = new JTextField(7);
            jTextFieldBajoRasante.addCaretListener(new CaretListener()
                    {
                public void caretUpdate(CaretEvent evt)
                {
                    EdicionUtils.chequeaLongYNumCampoEdit(jTextFieldBajoRasante,7, aplicacion.getMainFrame());
                    
                    calcularSuperficieConstruidaTotal();

                }
                    });
        }
        return jTextFieldBajoRasante;
    }
    
        
    /**
     * This method initializes jComboBoxFormaCalculo	
     * 	
     * @return javax.swing.JComboBox	
     */
    private JComboBox getJComboBoxFormaCalculo()
    {
        if (jComboBoxFormaCalculo == null)
        {
            jComboBoxFormaCalculo = new JComboBox();
            jComboBoxFormaCalculo.removeAllItems();
            
            //escribe de 00 a 99 
            int i=0;
            NumberFormat formatter = new DecimalFormat("00");
            jComboBoxFormaCalculo.addItem(null);
            while (i<100)            
                jComboBoxFormaCalculo.addItem(formatter.format(new Integer(i++)));
           
        }
        jComboBoxFormaCalculo.addActionListener(new ListenerCombo());
        return jComboBoxFormaCalculo;
    }
    
    private JComboBox getJComboBoxAnioPonencia()
    {
        if (jComboBoxAnioPonencia  == null)
        {
        	jComboBoxAnioPonencia = new JComboBox();
        	           
        }
        jComboBoxAnioPonencia.addActionListener(new ListenerCombo());
        return jComboBoxAnioPonencia;
    }
    
    private JComboBox getJComboBoxPoligonoValoracion()
    {
        if (jComboBoxPoligonoValoracion   == null)
        {
        	jComboBoxPoligonoValoracion = new JComboBox();
        	           
        }
        jComboBoxPoligonoValoracion.addActionListener(new ListenerCombo());
        return jComboBoxPoligonoValoracion;
    }
    
    /**
     * This method initializes jTextFieldPoligonoValoracion	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getJTextFieldPoligonoValoracion()
    {
        if (jTextFieldPoligonoValoracion == null)
        {
            jTextFieldPoligonoValoracion = new JTextField();//new JNumberTextField(JNumberTextField.NUMBER);//, new Integer(999));
            jTextFieldPoligonoValoracion.addCaretListener(new CaretListener()
                    {
                public void caretUpdate(CaretEvent evt)
                {
                    EdicionUtils.chequeaLongYNumCampoEdit(jTextFieldPoligonoValoracion,3, aplicacion.getMainFrame());
                }
                    });
        }
        return jTextFieldPoligonoValoracion;
    }
    
        
    private JComboBox getJComboBoxVuelo()
    {
    	if (jComboBoxVuelo  == null)
    	{   
    		Estructuras.cargarEstructura("Vuelo");
    		jComboBoxVuelo = new ComboBoxEstructuras(Estructuras.getListaTipos(),
    				null, AppContext.getApplicationContext().getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), true);
    		    		
    	}
    	jComboBoxVuelo.addActionListener(new ListenerCombo());
    	return jComboBoxVuelo;
    }
    
    private JTextField getJTextFieldInfraedificada()
    {
        if (jTextFieldInfraedificada   == null)
        {
        	jTextFieldInfraedificada = new JTextField();
        }
        jTextFieldInfraedificada.getDocument().addDocumentListener(new ListenerTextField());
        return jTextFieldInfraedificada;
    }

 
    
    /*
     * Comprueba que los campos obligatorios están escritos.
     * @return boolean 
     */
    public boolean datosMinimosYCorrectos()
    {
    	//Panel Datos económicos
    	boolean okValoracion=true;
    	if( (jComboBoxFormaCalculo!=null && jComboBoxFormaCalculo.getSelectedItem()!=null && !(jComboBoxFormaCalculo.getSelectedItem().toString().equals("")))
    			|| (jComboBoxAnioPonencia!=null && jComboBoxAnioPonencia.getSelectedItem()!=null && !(jComboBoxAnioPonencia.getSelectedItem().toString().equals("")))
        		|| (jComboBoxPoligonoValoracion!=null && jComboBoxPoligonoValoracion.getSelectedItem()!=null && !(jComboBoxPoligonoValoracion.getSelectedItem().toString().equals("")))
        		|| (jTextFieldInfraedificada.getText()!=null && !jTextFieldInfraedificada.getText().equalsIgnoreCase("")) ){
    		okValoracion= (jComboBoxFormaCalculo!=null && jComboBoxFormaCalculo.getSelectedItem()!=null && !(jComboBoxFormaCalculo.getSelectedItem().toString().equals(""))) &&
    		(jComboBoxAnioPonencia!=null && jComboBoxAnioPonencia.getSelectedItem()!=null && !(jComboBoxAnioPonencia.getSelectedItem().toString().equals(""))) &&
    		(jComboBoxPoligonoValoracion!=null && jComboBoxPoligonoValoracion.getSelectedItem()!=null && !(jComboBoxPoligonoValoracion.getSelectedItem().toString().equals("")));
    	}
    	
    	boolean okLocal=false;
    	
    	boolean okUrb=true;
    	//Panel Localización Urbana obligatoria
    	if( (jComboBoxTipoVia!=null && jComboBoxTipoVia.getSelectedPatron()!=null && !(jComboBoxTipoVia.getSelectedPatron().equals("")))
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
    		if( (jComboBoxPoligono!=null && jComboBoxPoligono.getSelectedItem()!=null && !(jComboBoxPoligono.getSelectedItem().toString().equals("")))
        			|| (jTextFieldParcela.getText()!=null && !jTextFieldParcela.getText().equalsIgnoreCase(""))
        			|| (jTextFieldParaje.getText()!=null && !jTextFieldParaje.getText().equalsIgnoreCase(""))
        			|| (jTextFieldZona.getText()!=null && !jTextFieldZona.getText().equalsIgnoreCase(""))
        			|| (jComboBoxMunicipioAgregado!=null && jComboBoxMunicipioAgregado.getSelectedItem()!=null && jComboBoxMunicipioAgregado.getSelectedIndex()>0) ){
        		okUrb = (jComboBoxTipoVia!=null && jComboBoxTipoVia.getSelectedPatron()!=null && !(jComboBoxTipoVia.getSelectedPatron().equals(""))) &&
	        		(jTextFieldDireccion.getText()!=null && !jTextFieldDireccion.getText().equalsIgnoreCase("")) &&
	        		(jComboBoxPoligono!=null && jComboBoxPoligono.getSelectedItem()!=null && !(jComboBoxPoligono.getSelectedItem().toString().equals(""))) &&
	        		(jTextFieldParcela.getText()!=null && !jTextFieldParcela.getText().equalsIgnoreCase(""));
    		}else{    		
	    		okUrb = (jComboBoxTipoVia!=null && jComboBoxTipoVia.getSelectedPatron()!=null && !(jComboBoxTipoVia.getSelectedPatron().equals(""))) &&
	    			(jTextFieldDireccion.getText()!=null && !jTextFieldDireccion.getText().equalsIgnoreCase(""));
    		}
    	}
    	
    	boolean okRust=true;
    	//Panel Localización rústica obligatoria
    	if( (jComboBoxPoligono!=null && jComboBoxPoligono.getSelectedItem()!=null && !(jComboBoxPoligono.getSelectedItem().toString().equals("")))
    			|| (jTextFieldParcela.getText()!=null && !jTextFieldParcela.getText().equalsIgnoreCase(""))
    			|| (jTextFieldParaje.getText()!=null && !jTextFieldParaje.getText().equalsIgnoreCase(""))
    			|| (jTextFieldZona.getText()!=null && !jTextFieldZona.getText().equalsIgnoreCase(""))
    			|| (jComboBoxMunicipioAgregado!=null && jComboBoxMunicipioAgregado.getSelectedItem()!=null && jComboBoxMunicipioAgregado.getSelectedIndex()>0) ){
    		okLocal=true;
    		if( (jComboBoxTipoVia!=null && jComboBoxTipoVia.getSelectedPatron()!=null && !(jComboBoxTipoVia.getSelectedPatron().equals("")))
        			|| (jTextFieldDireccion.getText()!=null && !jTextFieldDireccion.getText().equalsIgnoreCase(""))
        			|| (jTextFieldCodigoVia.getText()!=null && !jTextFieldCodigoVia.getText().equalsIgnoreCase(""))
	    			|| (jTextFieldNumero.getText()!=null && !jTextFieldNumero.getText().equalsIgnoreCase(""))
	    			|| (jTextFieldLetra.getText()!=null && !jTextFieldLetra.getText().equalsIgnoreCase(""))
	    			|| (jTextFieldNumeroD.getText()!=null && !jTextFieldNumeroD.getText().equalsIgnoreCase(""))
	    			|| (jTextFieldLetraD.getText()!=null && !jTextFieldLetraD.getText().equalsIgnoreCase(""))
	    			|| (jTextFieldBloque.getText()!=null && !jTextFieldBloque.getText().equalsIgnoreCase(""))
	    			|| (jTextFieldDirNoEstructurada.getText()!=null && !jTextFieldDirNoEstructurada.getText().equalsIgnoreCase(""))
	    			|| (jTextFieldKm.getText()!=null && !jTextFieldKm.getText().equalsIgnoreCase("")) ){
    			okRust=(jComboBoxPoligono!=null && jComboBoxPoligono.getSelectedItem()!=null && !(jComboBoxPoligono.getSelectedItem().toString().equals(""))) &&
					(jTextFieldParcela.getText()!=null && !jTextFieldParcela.getText().equalsIgnoreCase("")) &&
					(jComboBoxTipoVia!=null && jComboBoxTipoVia.getSelectedPatron()!=null && !(jComboBoxTipoVia.getSelectedPatron().equals(""))) &&
	    			(jTextFieldDireccion.getText()!=null && !jTextFieldDireccion.getText().equalsIgnoreCase(""))&&
	    			(jTextFieldCodigoVia.getText()!=null && !jTextFieldCodigoVia.getText().equalsIgnoreCase(""));
    		}else{
	    		okRust=(jComboBoxPoligono!=null && jComboBoxPoligono.getSelectedItem()!=null && !(jComboBoxPoligono.getSelectedItem().toString().equals(""))) &&
					(jTextFieldParcela.getText()!=null && !jTextFieldParcela.getText().equalsIgnoreCase("")) &&
					(jTextFieldParaje.getText()!=null && !jTextFieldParaje.getText().equalsIgnoreCase(""));
    		}   		
    	}
    	// se añade comprobación del panel de datos de identificación y datos físicos de superficie
        return okValoracion && okUrb && okRust && okLocal && (jTextFieldBajoRasante.getText()!=null && !jTextFieldBajoRasante.getText().equalsIgnoreCase("")) &&
	        (jTextFieldCubierta.getText()!=null && !jTextFieldCubierta.getText().equalsIgnoreCase("")) &&
	        (jTextFieldSobreRasante.getText()!=null && !jTextFieldSobreRasante.getText().equalsIgnoreCase("")) &&
	        (jTextFieldConstruidaTotal.getText()!=null && !jTextFieldConstruidaTotal.getText().equalsIgnoreCase("")) &&
	        (jTextFieldSolar.getText()!=null && !jTextFieldSolar.getText().equalsIgnoreCase("")) &&       
	        (jTextFieldRefCatastral1.getText()!=null &&!jTextFieldRefCatastral1.getText().equalsIgnoreCase("")) &&
	        (jTextFieldRefCatastral2.getText()!=null &&!jTextFieldRefCatastral2.getText().equalsIgnoreCase("")) ;
    }   
    
    
    private void calcularSuperficieConstruidaTotal(){
    	int sobreRasante= 0;
    	int bajoRasante=0;
    	int construidaTotal = 0;
    	
    	if(!jTextFieldSobreRasante.getText().equals("")){
    		sobreRasante = new Integer(jTextFieldSobreRasante.getText()).intValue();
    	}
    	if(!jTextFieldBajoRasante.getText().equals("")){
    		bajoRasante = new Integer(jTextFieldBajoRasante.getText()).intValue();
    	}
    	construidaTotal = sobreRasante + bajoRasante;
    	jTextFieldConstruidaTotal.setText(new Integer(construidaTotal).toString());
    	
    }
    
    private void calcularSuperficieBajoRasante(){
    	int construidaTotal = 0;
    	int sobreRasante= 0;
    	int bajoRasante=0;
    	   	
    	if(!jTextFieldConstruidaTotal.getText().equals("")){
    		construidaTotal = new Integer(jTextFieldConstruidaTotal.getText()).intValue();
    	}
    	if(!jTextFieldSobreRasante.getText().equals("")){
    		sobreRasante = new Integer(jTextFieldSobreRasante.getText()).intValue();
    	}
    	bajoRasante = construidaTotal - sobreRasante;
    	jTextFieldBajoRasante.setText(new Integer(bajoRasante).toString());
    	
    }
    
}
