/**
 * Depuradoras2Panel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.catastro.model.beans.Municipio;
import com.geopista.app.catastro.model.beans.Provincia;
import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.app.eiel.beans.Depuradora2EIEL;
import com.geopista.protocol.inventario.Inventario;
import com.geopista.app.eiel.beans.MunicipioEIEL;
import com.geopista.app.eiel.dialogs.Depuradoras2Dialog;
import com.geopista.app.eiel.utils.EdicionOperations;
import com.geopista.app.eiel.utils.EdicionUtils;
import com.geopista.app.eiel.utils.UbicacionListCellRenderer;
import com.geopista.app.eiel.utils.UtilRegistroExp;
import com.geopista.app.inventario.ConstantesEIEL;
import com.geopista.app.utilidades.TextField;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.geopista.feature.GeopistaSchema;
import com.geopista.ui.components.DateField;
import com.geopista.ui.dialogs.FeatureDialog;
import com.geopista.util.FeatureExtendedPanel;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.util.Blackboard;

public class Depuradoras2Panel extends InventarioPanel implements FeatureExtendedPanel
{

	private static final long serialVersionUID = 1L;

	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	private Blackboard Identificadores = aplicacion.getBlackboard();

	private JPanel jPanelIdentificacion = null; 
	private JPanel jPanelInformacion = null;
	private JPanel jPanelRevision = null;

	public boolean okPressed = false;	

	private JComboBox jComboBoxProvincia = null;
	private JComboBox jComboBoxMunicipio = null;

	private JLabel jLabelCodProv = null;
	private JLabel jLabelClave = null;
	private JLabel jLabelCodMunic = null;
	private JLabel jLabelOrden = null;

	private JTextField jTextFieldClave = null;
	private JTextField jTextFieldOrden = null;

	private JLabel jLabelTitular = null;
	private JLabel jLabelGestor = null;
	private JLabel jLabelCapacidad = null;
	private JLabel jLabelProblem1 = null;
	private JLabel jLabelProblem2 = null;
	private JLabel jLabelProblem3 = null;
	private JLabel jLabelGestionLodos = null;
	private JLabel jLabelLodoVertidos = null;
	private JLabel jLabelLodoInci = null;
	private JLabel jLabelLodoConAgri = null;
	private JLabel jLabelLodoSinAgri = null;
	private JLabel jLabelLodoOt = null;
	private JLabel jLabelFechaInst = null;
	private JLabel jLabelObserv = null;

	private ComboBoxEstructuras jComboBoxTitular = null;
	private ComboBoxEstructuras jComboBoxGestor = null;
	private JTextField jTextFieldCapacidad = null;
	private ComboBoxEstructuras jComboBoxProblem1 = null;
	private ComboBoxEstructuras jComboBoxProblem2 = null;
	private ComboBoxEstructuras jComboBoxProblem3 = null;
	private ComboBoxEstructuras jComboBoxGestionLodos = null;
	private JTextField jTextFieldLodoVertidos = null;
	private JTextField jTextFieldLodoInci = null;
	private JTextField jTextFieldLodoConAgri = null;
	private JTextField jTextFieldLodoSinAgri = null;
	private JTextField jTextFieldLodoOt = null;
	private DateField jTextFieldfechaInst = null;
	private JTextField jTextFieldObserv = null;

	private JLabel jLabelFecha = null;
	private JLabel jLabelEstado = null;

	private JTextField jTextFieldFecha = null;
	private ComboBoxEstructuras jComboBoxEstado = null;

	/**
	 * This method initializes
	 * 
	 */
	public Depuradoras2Panel()
	{
		super();
		initialize();
	}

	public Depuradoras2Panel(Depuradora2EIEL elemento)
	{
		super();
		initialize();
		loadData (elemento);
	}

	public void loadData(Depuradora2EIEL elemento)
	{
		if (elemento!=null)
		{
			//Datos identificacion
			if (elemento.getClave() != null){
				jTextFieldClave.setText(elemento.getClave());
			}
			else{
				jTextFieldClave.setText("");
			}

			if (elemento.getCodINEProvincia() != null) {
				jComboBoxProvincia
						.setSelectedIndex(provinciaIndexSeleccionar(elemento
								.getCodINEProvincia()));
			} else {
				jComboBoxProvincia.setSelectedIndex(0);
			}

			if (elemento.getCodINEMunicipio() != null) {
				jComboBoxMunicipio
						.setSelectedIndex(municipioIndexSeleccionar(elemento
								.getCodINEMunicipio()));
			} else {
				jComboBoxMunicipio.setSelectedIndex(0);
			}
			if (elemento.getCodOrden() != null){
				jTextFieldOrden.setText(elemento.getCodOrden());
			}
			else{
				jTextFieldOrden.setText("");
			}

			if (elemento.getTitular() != null){
				jComboBoxTitular.setSelectedPatron(elemento.getTitular());
			}
			else{
				jComboBoxTitular.setSelectedIndex(0);
			}

			if (elemento.getGestor() != null){
				jComboBoxGestor.setSelectedPatron(elemento.getGestor());
			}
			else{
				jComboBoxGestor.setSelectedIndex(0);
			}

			if (elemento.getCapacidad() != null){
				jTextFieldCapacidad.setText(elemento.getCapacidad().toString());
			}
			else{
				jTextFieldCapacidad.setText("");
			}

			if (elemento.getProblemas1() != null){
				jComboBoxProblem1.setSelectedPatron(elemento.getProblemas1());
			}
			else{
				jComboBoxProblem1.setSelectedIndex(0);
			}
			if (elemento.getProblemas2() != null){
				jComboBoxProblem2.setSelectedPatron(elemento.getProblemas2());
			}
			else{
				jComboBoxProblem2.setSelectedIndex(0);
			}
			if (elemento.getProblemas3() != null){
				jComboBoxProblem3.setSelectedPatron(elemento.getProblemas3());
			}
			else{
				jComboBoxProblem3.setSelectedIndex(0);
			}

			if (elemento.getGestionLodos() != null){
				jComboBoxGestionLodos.setSelectedPatron(elemento.getGestionLodos());
			}
			else{
				jComboBoxGestionLodos.setSelectedIndex(0);
			}

			if (elemento.getLodosVertedero() != null){
				jTextFieldLodoVertidos.setText(elemento.getLodosVertedero().toString());
			}
			else{
				jTextFieldLodoVertidos.setText("");
			}

			if (elemento.getLodosIncineracion() != null){
				jTextFieldLodoInci.setText(elemento.getLodosIncineracion().toString());
			}
			else{
				jTextFieldLodoInci.setText("");
			}

			if (elemento.getLodosAgrConCompostaje() != null){
				jTextFieldLodoConAgri.setText(elemento.getLodosAgrConCompostaje().toString());
			}
			else{
				jTextFieldLodoConAgri.setText("");
			}

			if (elemento.getLodosAgrSinCompostaje() != null){
				jTextFieldLodoSinAgri.setText(elemento.getLodosAgrSinCompostaje().toString());
			}
			else{
				jTextFieldLodoSinAgri.setText("");
			}

			if (elemento.getLodosOtroFinal() != null){
				jTextFieldLodoOt.setText(elemento.getLodosOtroFinal().toString());
			}
			else{
				jTextFieldLodoOt.setText("100");
			}

			if (elemento.getFechaInstalacion() != null){
				jTextFieldfechaInst.setDate(elemento.getFechaInstalacion());
			}
			else{
				jTextFieldfechaInst.setDate(null);
			}

			if (elemento.getObservaciones() != null){
				jTextFieldObserv.setText(elemento.getObservaciones());
			}
			else{
				jTextFieldObserv.setText("");
			}

			if (elemento.getFechaRevision() != null && elemento.getFechaRevision().equals( new java.util.Date()) ){
				jTextFieldFecha.setText(elemento.getFechaRevision().toString());
			}
			else{
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				java.util.Date date = new java.util.Date();
				String datetime = dateFormat.format(date);

				jTextFieldFecha.setText(datetime);
			}

			if (elemento.getEstadoRevision() != null){
				jComboBoxEstado.setSelectedPatron(elemento.getEstadoRevision().toString());
			}
			else{
				jComboBoxEstado.setSelectedIndex(0);
			}

			if (elemento.getTitularidadMunicipal() != null){
            	jComboBoxTitularidadMunicipal.setSelectedPatron(elemento.getTitularidadMunicipal());
        	}
        	else{
        		jComboBoxTitularidadMunicipal.setSelectedIndex(0);
        	}
            
            if (elemento.getEpigInventario() != null){
            	jComboBoxEpigInventario.setSelectedPatron(elemento.getEpigInventario().toString());
        	}
        	else{
        		if (jComboBoxEpigInventario.isEnabled())
        			jComboBoxEpigInventario.setSelectedIndex(0);
        	}
            
            if (elemento.getIdBien() != null && elemento.getIdBien() != 0){
            	jComboBoxNumInventario.setEnabled(true);
				EdicionOperations oper = new EdicionOperations();          			
				EdicionUtils.cargarLista(getJComboBoxNumInventario(), oper.obtenerNumInventario(Integer.parseInt(jComboBoxEpigInventario.getSelectedPatron())));
				Inventario inventario = new Inventario();
				inventario.setId(elemento.getIdBien());
            	jComboBoxNumInventario.setSelectedItem(inventario);
            }
            else{
            	if (jComboBoxNumInventario.isEnabled())
            		jComboBoxNumInventario.setSelectedIndex(0);
            }
            
		}else {
			// elemento a cargar es null....
			// se carga por defecto la clave, el codigo de provincia y el codigo de municipio

			jTextFieldClave.setText(ConstantesLocalGISEIEL.DEPURADORAS2_CLAVE);

			if (ConstantesLocalGISEIEL.idProvincia!=null){
				jComboBoxProvincia
					.setSelectedIndex(provinciaIndexSeleccionar(ConstantesLocalGISEIEL.idProvincia));
			}

			if (ConstantesLocalGISEIEL.idMunicipio!=null){
				 jComboBoxMunicipio.setSelectedIndex(municipioIndexSeleccionar(ConstantesLocalGISEIEL.idMunicipio));
			}

			jComboBoxEpigInventario.setEnabled(false);
		}
	}

	public void loadData()
	{
		Object object = AppContext.getApplicationContext().getBlackboard().get("depuradora2_panel");    	
		if (object != null && object instanceof Depuradora2EIEL){    		
			Depuradora2EIEL elemento = (Depuradora2EIEL)object;
			//Datos identificacion
			if (elemento.getClave() != null){
				jTextFieldClave.setText(elemento.getClave());
			}
			else{
				jTextFieldClave.setText("");
			}

			if (elemento.getCodINEProvincia() != null) {
				jComboBoxProvincia
						.setSelectedIndex(provinciaIndexSeleccionar(elemento
								.getCodINEProvincia()));
			} else {
				jComboBoxProvincia.setSelectedIndex(0);
			}

			if (elemento.getCodINEMunicipio() != null) {
				jComboBoxMunicipio
						.setSelectedIndex(municipioIndexSeleccionar(elemento
								.getCodINEMunicipio()));
			} else {
				jComboBoxMunicipio.setSelectedIndex(0);
			}
			if (elemento.getCodOrden() != null){
				jTextFieldOrden.setText(elemento.getCodOrden());
			}
			else{
				jTextFieldOrden.setText("");
			}

			if (elemento.getTitular() != null){
				jComboBoxTitular.setSelectedPatron(elemento.getTitular());
			}
			else{
				jComboBoxTitular.setSelectedIndex(0);
			}

			if (elemento.getGestor() != null){
				jComboBoxGestor.setSelectedPatron(elemento.getGestor());
			}
			else{
				jComboBoxGestor.setSelectedIndex(0);
			}

			if (elemento.getCapacidad() != null){
				jTextFieldCapacidad.setText(elemento.getCapacidad().toString());
			}
			else{
				jTextFieldCapacidad.setText("");
			}

			if (elemento.getProblemas1() != null){
				jComboBoxProblem1.setSelectedPatron(elemento.getProblemas1());
			}
			else{
				jComboBoxProblem1.setSelectedIndex(0);
			}
			if (elemento.getProblemas2() != null){
				jComboBoxProblem2.setSelectedPatron(elemento.getProblemas2());
			}
			else{
				jComboBoxProblem2.setSelectedIndex(0);
			}
			if (elemento.getProblemas3() != null){
				jComboBoxProblem3.setSelectedPatron(elemento.getProblemas3());
			}
			else{
				jComboBoxProblem3.setSelectedIndex(0);
			}

			if (elemento.getGestionLodos() != null){
				jComboBoxGestionLodos.setSelectedPatron(elemento.getGestionLodos());
			}
			else{
				jComboBoxGestionLodos.setSelectedIndex(-1);
			}

			if (elemento.getLodosVertedero() != null){
				jTextFieldLodoVertidos.setText(elemento.getLodosVertedero().toString());
			}
			else{
				jTextFieldLodoVertidos.setText("");
			}

			if (elemento.getLodosIncineracion() != null){
				jTextFieldLodoInci.setText(elemento.getLodosIncineracion().toString());
			}
			else{
				jTextFieldLodoInci.setText("");
			}

			if (elemento.getLodosAgrConCompostaje() != null){
				jTextFieldLodoConAgri.setText(elemento.getLodosAgrConCompostaje().toString());
			}
			else{
				jTextFieldLodoConAgri.setText("");
			}

			if (elemento.getLodosAgrSinCompostaje() != null){
				jTextFieldLodoSinAgri.setText(elemento.getLodosAgrSinCompostaje().toString());
			}
			else{
				jTextFieldLodoSinAgri.setText("");
			}

			if (elemento.getLodosOtroFinal() != null){
				jTextFieldLodoOt.setText(elemento.getLodosOtroFinal().toString());
			}
			else{
				jTextFieldLodoOt.setText("100");
			}

			if (elemento.getFechaInstalacion() != null){
				jTextFieldfechaInst.setDate(elemento.getFechaInstalacion());
			}
			else{
				jTextFieldfechaInst.setDate(null);
			}

			if (elemento.getObservaciones() != null){
				jTextFieldObserv.setText(elemento.getObservaciones());
			}
			else{
				jTextFieldObserv.setText("");
			}

			if (elemento.getFechaRevision() != null && elemento.getFechaRevision().equals( new java.util.Date()) ){
				jTextFieldFecha.setText(elemento.getFechaRevision().toString());
			}
			else{
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				java.util.Date date = new java.util.Date();
				String datetime = dateFormat.format(date);

				jTextFieldFecha.setText(datetime);
			}

			if (elemento.getEstadoRevision() != null){
				jComboBoxEstado.setSelectedPatron(elemento.getEstadoRevision().toString());
			}
			else{
				jComboBoxEstado.setSelectedIndex(0);
			}

			if (elemento.getEpigInventario() != null){
            	jComboBoxEpigInventario.setSelectedPatron(elemento.getEpigInventario().toString());
        	}
        	else{
        		jComboBoxEpigInventario.setSelectedIndex(0);
        	}

            if (elemento.getIdBien() != null && elemento.getIdBien() != 0)
            	jComboBoxNumInventario.setSelectedItem(elemento.getIdBien());
        	else{
        		jComboBoxNumInventario.setSelectedIndex(0);
        	}
            
            if (elemento.getTitularidadMunicipal() != null){
            	jComboBoxTitularidadMunicipal.setSelectedPatron(elemento.getTitularidadMunicipal());
        	}
        	else{
        		jComboBoxTitularidadMunicipal.setSelectedIndex(0);
        	}
		}
	}
	
	
	public Depuradora2EIEL getDepuradora2Data ()
	{

		Depuradora2EIEL	elemento = new Depuradora2EIEL();

		elemento.setCodINEProvincia(((Provincia) jComboBoxProvincia
				.getSelectedItem()).getIdProvincia());
		elemento.setCodINEMunicipio(((Municipio) jComboBoxMunicipio
				.getSelectedItem()).getIdIne());
		elemento.setIdMunicipio(Integer.parseInt(ConstantesLocalGISEIEL.idMunicipio));

		if (jTextFieldClave.getText()!=null){
			elemento.setClave(jTextFieldClave.getText());
		}

		if (jTextFieldOrden.getText()!=null){
			elemento.setCodOrden(jTextFieldOrden.getText());
		}

		if (jComboBoxTitular.getSelectedPatron()!=null)
			elemento.setTitular((String) jComboBoxTitular.getSelectedPatron());
		else elemento.setTitular("");
		if (jComboBoxGestor.getSelectedPatron()!=null)
			elemento.setGestor((String) jComboBoxGestor.getSelectedPatron());
		else elemento.setGestor("");

		if (jTextFieldCapacidad.getText()!=null && !jTextFieldCapacidad.getText().equals("")){
			elemento.setCapacidad(new Integer(jTextFieldCapacidad.getText()));
		}else{
			elemento.setCapacidad(null);
		}

		if (jComboBoxProblem1.getSelectedPatron()!=null)
			elemento.setProblemas1((String) jComboBoxProblem1.getSelectedPatron());
		else elemento.setProblemas1("");
		if (jComboBoxProblem2.getSelectedPatron()!=null)
			elemento.setProblemas2((String) jComboBoxProblem2.getSelectedPatron());
		else elemento.setProblemas2("");
		if (jComboBoxProblem3.getSelectedPatron()!=null)
			elemento.setProblemas3((String) jComboBoxProblem3.getSelectedPatron());
		else elemento.setProblemas3("");

		if (jComboBoxGestionLodos.getSelectedPatron()!=null){
			elemento.setGestionLodos(jComboBoxGestionLodos.getSelectedPatron());
		} else{
			elemento.setGestionLodos("");
		}

		if (jTextFieldLodoVertidos.getText()!=null && !jTextFieldLodoVertidos.getText().equals("")){
			elemento.setLodosVertedero(new Integer(jTextFieldLodoVertidos.getText()));
		}else{
			elemento.setLodosVertedero(null);
		}

		if (jTextFieldLodoInci.getText()!=null && !jTextFieldLodoInci.getText().equals("")){
			elemento.setLodosIncineracion( new Integer(jTextFieldLodoInci.getText()));
		}else{
			elemento.setLodosIncineracion(null);
		}

		if (jTextFieldLodoConAgri.getText()!=null && !jTextFieldLodoConAgri.getText().equals("")){
			elemento.setLodosAgrConCompostaje( new Integer(jTextFieldLodoConAgri.getText()));
		}else{
			elemento.setLodosAgrConCompostaje(null);
		}

		if (jTextFieldLodoSinAgri.getText()!=null && !jTextFieldLodoSinAgri.getText().equals("")){
			elemento.setLodosAgrSinCompostaje( new Integer(jTextFieldLodoSinAgri.getText()));
		}else{
			elemento.setLodosAgrSinCompostaje(null);
		}

		if (jTextFieldLodoOt.getText()!=null && !jTextFieldLodoOt.getText().equals("")){
			elemento.setLodosOtroFinal( new Integer(jTextFieldLodoOt.getText()));
		}else{
			elemento.setLodosOtroFinal(null);
		}   

		if (jTextFieldfechaInst.getDate()!=null && !jTextFieldfechaInst.getDate().toString().equals("")){
			if (getJTextFieldFechaInst().getDate() != null ){
				java.sql.Date sqlDate = new java.sql.Date(
						getJTextFieldFechaInst().getDate().getYear(),
						getJTextFieldFechaInst().getDate().getMonth(),
						getJTextFieldFechaInst().getDate().getDate()
				);
				elemento.setFechaInstalacion(sqlDate);
			}
		}  
		else{
			elemento.setFechaInstalacion(null);
		}

		if (jTextFieldObserv.getText()!=null){
			elemento.setObservaciones(jTextFieldObserv.getText());
		} 

		if (jTextFieldFecha.getText()!=null && !jTextFieldFecha.getText().equals("")){
			String fechas=jTextFieldFecha.getText();
			String anio=fechas.substring(0,4);
			String mes=fechas.substring(5,7);
			String dia=fechas.substring(8,10);

			java.util.Date fecha = new java.util.Date(Integer.parseInt(anio)-1900,Integer.parseInt(mes)-1,Integer.parseInt(dia));            	
			elemento.setFechaRevision(new Date(fecha.getYear(),fecha.getMonth(), fecha.getDate())); 
		}  
		else{
			elemento.setFechaRevision(null);
		}

		if (jComboBoxEstado.getSelectedPatron()!=null)
			elemento.setEstadoRevision(Integer.parseInt(jComboBoxEstado.getSelectedPatron()));

		if (jComboBoxTitularidadMunicipal.getSelectedPatron() != null && jComboBoxTitularidadMunicipal.getSelectedPatron().equals(ConstantesEIEL.TITULARIDAD_MUNICIPAL_SI)){
			elemento.setTitularidadMunicipal(jComboBoxTitularidadMunicipal.getSelectedPatron());
			if (jComboBoxEpigInventario.getSelectedPatron()!=null){
				elemento.setEpigInventario(Integer.parseInt(jComboBoxEpigInventario.getSelectedPatron()));
				elemento.setIdBien(((Inventario) jComboBoxNumInventario.getSelectedItem()).getId());
			}
			else{ 
				elemento.setEpigInventario(0);
				elemento.setIdBien(0);
			}
		}
		else{
			// Si la titularidad Municipal está en blanco significa que es un bien no inventariable
			elemento.setTitularidadMunicipal(jComboBoxTitularidadMunicipal.getSelectedPatron());
			elemento.setEpigInventario(0);
			elemento.setIdBien(0);
		}
		
		return elemento;
	}
	
	

	public Depuradora2EIEL getDepuradora2 (Depuradora2EIEL elemento)
	{

		if (okPressed)
		{
			if(elemento==null)
			{
				elemento = new Depuradora2EIEL();
			}
			elemento.setCodINEProvincia(((Provincia) jComboBoxProvincia
					.getSelectedItem()).getIdProvincia());
			elemento.setCodINEMunicipio(((Municipio) jComboBoxMunicipio
					.getSelectedItem()).getIdIne());
			elemento.setIdMunicipio(Integer.parseInt(ConstantesLocalGISEIEL.idMunicipio));
			if (jTextFieldClave.getText()!=null){
				elemento.setClave(jTextFieldClave.getText());
			}

			if (jTextFieldOrden.getText()!=null){
				elemento.setCodOrden(jTextFieldOrden.getText());
			}

			if (jComboBoxTitular.getSelectedPatron()!=null)
				elemento.setTitular((String) jComboBoxTitular.getSelectedPatron());
			else elemento.setTitular("");
			if (jComboBoxGestor.getSelectedPatron()!=null)
				elemento.setGestor((String) jComboBoxGestor.getSelectedPatron());
			else elemento.setGestor("");

			if (jTextFieldCapacidad.getText()!=null && !jTextFieldCapacidad.getText().equals("")){
				elemento.setCapacidad(new Integer(jTextFieldCapacidad.getText()));
			}else{
				elemento.setCapacidad(null);
			}

			if (jComboBoxProblem1.getSelectedPatron()!=null)
				elemento.setProblemas1((String) jComboBoxProblem1.getSelectedPatron());
			else elemento.setProblemas1("");
			if (jComboBoxProblem2.getSelectedPatron()!=null)
				elemento.setProblemas2((String) jComboBoxProblem2.getSelectedPatron());
			else elemento.setProblemas2("");
			if (jComboBoxProblem3.getSelectedPatron()!=null)
				elemento.setProblemas3((String) jComboBoxProblem3.getSelectedPatron());
			else elemento.setProblemas3("");

			if (jComboBoxGestionLodos.getSelectedPatron()!=null){
				elemento.setGestionLodos(jComboBoxGestionLodos.getSelectedPatron());
			} else{
				elemento.setGestionLodos("");
			}

			if (jTextFieldLodoVertidos.getText()!=null && !jTextFieldLodoVertidos.getText().equals("")){
				elemento.setLodosVertedero(new Integer(jTextFieldLodoVertidos.getText()));
			}else{
				elemento.setLodosVertedero(null);
			}

			if (jTextFieldLodoInci.getText()!=null && !jTextFieldLodoInci.getText().equals("")){
				elemento.setLodosIncineracion( new Integer(jTextFieldLodoInci.getText()));
			}else{
				elemento.setLodosIncineracion(null);
			}

			if (jTextFieldLodoConAgri.getText()!=null && !jTextFieldLodoConAgri.getText().equals("")){
				elemento.setLodosAgrConCompostaje( new Integer(jTextFieldLodoConAgri.getText()));
			}else{
				elemento.setLodosAgrConCompostaje(null);
			}

			if (jTextFieldLodoSinAgri.getText()!=null && !jTextFieldLodoSinAgri.getText().equals("")){
				elemento.setLodosAgrSinCompostaje( new Integer(jTextFieldLodoSinAgri.getText()));
			}else{
				elemento.setLodosAgrSinCompostaje(null);
			}

			if (jTextFieldLodoOt.getText()!=null && !jTextFieldLodoOt.getText().equals("")){
				elemento.setLodosOtroFinal( new Integer(jTextFieldLodoOt.getText()));
			}else{
				elemento.setLodosOtroFinal(null);
			}   

			if (jTextFieldfechaInst.getDate()!=null && !jTextFieldfechaInst.getDate().toString().equals("")){
				if (getJTextFieldFechaInst().getDate() != null ){
					java.sql.Date sqlDate = new java.sql.Date(
							getJTextFieldFechaInst().getDate().getYear(),
							getJTextFieldFechaInst().getDate().getMonth(),
							getJTextFieldFechaInst().getDate().getDate()
					);
					elemento.setFechaInstalacion(sqlDate);
				}
			}  
			else{
				elemento.setFechaInstalacion(null);
			}

			if (jTextFieldObserv.getText()!=null){
				elemento.setObservaciones(jTextFieldObserv.getText());
			} 

			if (jTextFieldFecha.getText()!=null && !jTextFieldFecha.getText().equals("")){
				String fechas=jTextFieldFecha.getText();
				String anio=fechas.substring(0,4);
				String mes=fechas.substring(5,7);
				String dia=fechas.substring(8,10);

				java.util.Date fecha = new java.util.Date(Integer.parseInt(anio)-1900,Integer.parseInt(mes)-1,Integer.parseInt(dia));            	
				elemento.setFechaRevision(new Date(fecha.getYear(),fecha.getMonth(), fecha.getDate())); 
			}  
			else{
				elemento.setFechaRevision(null);
			}

			if (jComboBoxEstado.getSelectedPatron()!=null)
				elemento.setEstadoRevision(Integer.parseInt(jComboBoxEstado.getSelectedPatron()));

			if (jComboBoxTitularidadMunicipal.getSelectedPatron() != null && jComboBoxTitularidadMunicipal.getSelectedPatron().equals(ConstantesEIEL.TITULARIDAD_MUNICIPAL_SI)){
    			elemento.setTitularidadMunicipal(jComboBoxTitularidadMunicipal.getSelectedPatron());
    			if (jComboBoxEpigInventario.getSelectedPatron()!=null){
    				elemento.setEpigInventario(Integer.parseInt(jComboBoxEpigInventario.getSelectedPatron()));
    				elemento.setIdBien(((Inventario) jComboBoxNumInventario.getSelectedItem()).getId());
    			}
    			else{ 
    				elemento.setEpigInventario(0);
    				elemento.setIdBien(0);
    			}
    		}
    		else{
    			// Si la titularidad Municipal está en blanco significa que es un bien no inventariable
    			elemento.setTitularidadMunicipal(jComboBoxTitularidadMunicipal.getSelectedPatron());
    			elemento.setEpigInventario(0);
    			elemento.setIdBien(0);
    		}
		}

		return elemento;
	}

	private void initialize()
	{      
		Locale loc=I18N.getLocaleAsObject();         
		ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.eiel.language.LocalGISEIELi18n",loc,this.getClass().getClassLoader());
		I18N.plugInsResourceBundle.put("LocalGISEIEL",bundle);

		this.setName(I18N.get("LocalGISEIEL","localgiseiel.depuradoras2.panel.title"));

		this.setLayout(new GridBagLayout());
		this.setSize(Depuradoras2Dialog.DIM_X,Depuradoras2Dialog.DIM_Y);

		this.add(getJPanelDatosIdentificacion(),  new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 5, 0, 5), 0, 0));

		this.add(getJPanelDatosInformacion(), new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 5, 0, 5), 0, 0));

        this.add(getJPanelDatosInventario(),  new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 0, 5), 0, 0));
        
		this.add(getJPanelDatosRevision(), new GridBagConstraints(0, 3, 1, 1, 0.1, 0.1,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 5, 0, 5), 0, 0));

		EdicionOperations oper = new EdicionOperations();

		// Inicializa los desplegables
		if (Identificadores.get("ListaProvinciasGenerales") == null) {
			ArrayList lst = oper.obtenerProvinciasConNombre(true);
			Identificadores.put("ListaProvinciasGenerales", lst);
			EdicionUtils.cargarLista(getJComboBoxProvincia(),
					lst);
			Provincia p = new Provincia();
			p.setIdProvincia(ConstantesLocalGISEIEL.idProvincia);
			p.setNombreOficial(ConstantesLocalGISEIEL.Provincia);
			getJComboBoxProvincia().setSelectedItem(p);
		} else {
			EdicionUtils.cargarLista(getJComboBoxProvincia(),
					(ArrayList) Identificadores.get("ListaProvinciasGenerales"));
			 /*EdicionUtils.cargarLista(getJComboBoxProvincia(),
			 oper.obtenerProvinciasConNombre(true));*/
		}
		loadData();
	}

	public JTextField getJTextFieldClave()
	{
		if (jTextFieldClave  == null)
		{
			jTextFieldClave = new TextField(2);
		}
		return jTextFieldClave;
	}

	private ComboBoxEstructuras getJComboBoxEstado()
	{ 
		if (jComboBoxEstado == null)
		{
			Estructuras.cargarEstructura("eiel_Estado de revisión");
			jComboBoxEstado = new ComboBoxEstructuras(Estructuras.getListaTipos(),
					null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), true);

			jComboBoxEstado.setPreferredSize(new Dimension(100, 20));
		}
		return jComboBoxEstado;        
	}

	/**
	 * This method initializes jComboBoxProvincia	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	public JComboBox getJComboBoxProvincia()
	{
		if (jComboBoxProvincia == null) {
			EdicionOperations oper = new EdicionOperations();
			ArrayList<Provincia> listaProvincias = oper
					.obtenerProvinciasConNombre();
			jComboBoxProvincia = new JComboBox(listaProvincias.toArray());
			jComboBoxProvincia
					.setSelectedIndex(this
							.provinciaIndexSeleccionar(ConstantesLocalGISEIEL.idProvincia));
			jComboBoxProvincia.setRenderer(new UbicacionListCellRenderer());
			jComboBoxProvincia.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (getJComboBoxMunicipio() != null) {
						if (jComboBoxProvincia.getSelectedIndex() == 0) {
							jComboBoxMunicipio.removeAllItems();
							Municipio municipio = new Municipio();
							municipio.setIdIne("");
							municipio.setNombreOficial("");
							jComboBoxMunicipio.addItem(municipio);
						} else {
							EdicionOperations oper = new EdicionOperations();
							if (jComboBoxProvincia.getSelectedIndex()==-1)
							jComboBoxProvincia
									.setSelectedIndex(provinciaIndexSeleccionar(ConstantesLocalGISEIEL.idProvincia));

							if (jComboBoxProvincia.getSelectedItem() != null) {
								EdicionUtils.cargarLista(
										getJComboBoxMunicipio(),
										oper.obtenerTodosMunicipios(((Provincia) jComboBoxProvincia
												.getSelectedItem())
												.getIdProvincia()));
								jComboBoxMunicipio
										.setSelectedIndex(municipioIndexSeleccionar(ConstantesLocalGISEIEL.idMunicipio));
							}
						}
					}

				}
			});

		}

		return jComboBoxProvincia;
	}

	/**
	 * This method initializes jComboBoxMunicipio	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	public JComboBox getJComboBoxMunicipio()
	{
		if (jComboBoxMunicipio == null) {
			jComboBoxMunicipio = new JComboBox();
			jComboBoxMunicipio.setRenderer(new UbicacionListCellRenderer());
			jComboBoxMunicipio.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					if (jComboBoxMunicipio.getSelectedIndex() != 0) {
						MunicipioEIEL municipio = new MunicipioEIEL();
						if (jComboBoxProvincia.getSelectedItem() != null) {
							municipio
									.setCodProvincia(((Provincia) jComboBoxProvincia
											.getSelectedItem())
											.getIdProvincia());
						}
						if (jComboBoxMunicipio.getSelectedItem() != null) {
							municipio
									.setCodMunicipio(((Municipio) jComboBoxMunicipio
											.getSelectedItem()).getIdIne());
						}

					}
				}
			});
		}
		return jComboBoxMunicipio;
	}

	/**
	 * This method initializes jTextFieldPlanta   
	 *  
	 * @return javax.swing.JTextField   
	 */

	private JTextField getJTextFieldOrden()
	{
		if (jTextFieldOrden   == null)
		{
			jTextFieldOrden = new TextField(3);
		}
		return jTextFieldOrden;
	}

	private ComboBoxEstructuras getJComboBoxTitular()
	{ 
		if (jComboBoxTitular == null)
		{
			Estructuras.cargarEstructura("eiel_Titularidad");
			jComboBoxTitular = new ComboBoxEstructuras(Estructuras.getListaTipos(),
					null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"),false);

			jComboBoxTitular.setPreferredSize(new Dimension(100, 20));
		}
		return jComboBoxTitular;        
	}

	private ComboBoxEstructuras getJComboBoxGestor()
	{ 
		if (jComboBoxGestor == null)
		{
			Estructuras.cargarEstructura("eiel_Gestión");
			jComboBoxGestor = new ComboBoxEstructuras(Estructuras.getListaTipos(),
					null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), false);

			jComboBoxGestor.setPreferredSize(new Dimension(100, 20));
		}
		return jComboBoxGestor;        
	}

	private JTextField getJTextFieldCapacidad()
	{
		if (jTextFieldCapacidad  == null)
		{
			jTextFieldCapacidad  = new TextField(10);

		}
		return jTextFieldCapacidad;
	}

	private ComboBoxEstructuras getJComboBoxProblem1()
	{ 
		if (jComboBoxProblem1 == null)
		{
			Estructuras.cargarEstructura("eiel_EDAR problemas existentes");
			jComboBoxProblem1 = new ComboBoxEstructuras(Estructuras.getListaTipos(),
					null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"),false);

			jComboBoxProblem1.setPreferredSize(new Dimension(100, 20));
		}
		return jComboBoxProblem1;        
	}

	private ComboBoxEstructuras getJComboBoxProblem2()
	{ 
		if (jComboBoxProblem2 == null)
		{
			Estructuras.cargarEstructura("eiel_EDAR problemas existentes");
			jComboBoxProblem2 = new ComboBoxEstructuras(Estructuras.getListaTipos(),
					null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), false);

			jComboBoxProblem2.setPreferredSize(new Dimension(100, 20));
		}
		return jComboBoxProblem2;        
	}

	private ComboBoxEstructuras getJComboBoxProblem3()
	{ 
		if (jComboBoxProblem3 == null)
		{
			Estructuras.cargarEstructura("eiel_EDAR problemas existentes");
			jComboBoxProblem3 = new ComboBoxEstructuras(Estructuras.getListaTipos(),
					null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"),false);

			jComboBoxProblem3.setPreferredSize(new Dimension(100, 20));
		}
		return jComboBoxProblem3;        
	}
	
	
	private ComboBoxEstructuras getJComboBoxGestionLodos()
	{
		if (jComboBoxGestionLodos  == null)
		{
			Estructuras.cargarEstructura("eiel_EDAR Gestión de lodos");
			jComboBoxGestionLodos = new ComboBoxEstructuras(Estructuras.getListaTipos(),
					null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), false);

			jComboBoxGestionLodos.setPreferredSize(new Dimension(100, 20));
			

		}
		return jComboBoxGestionLodos;
	}

	private JTextField getJTextLodoVertidos()
	{
		if (jTextFieldLodoVertidos  == null)
		{
			jTextFieldLodoVertidos  = new TextField(10);
			jTextFieldLodoVertidos.addCaretListener(new CaretListener()
			{
				public void caretUpdate(CaretEvent evt)
				{
					EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldLodoVertidos, 10, aplicacion.getMainFrame());
				}
			});

			jTextFieldLodoVertidos.addFocusListener(new FocusAdapter() {
				public void focusLost(FocusEvent e) {
					recalcularLodosConOtroDestino();
				}


			});

			jTextFieldLodoVertidos.getDocument().addDocumentListener(new DocumentListener() {
				public void changedUpdate(DocumentEvent e) {

				}
				public void insertUpdate(DocumentEvent e) {
					recalcularLodosConOtroDestino();
				}
				public void removeUpdate(DocumentEvent e) {
					recalcularLodosConOtroDestino();
				}

			});

			jTextFieldLodoVertidos.setText(Integer.toString(0));


		}
		return jTextFieldLodoVertidos;
	}

	private JTextField getJTextFieldLodoInci()
	{
		if (jTextFieldLodoInci  == null)
		{
			jTextFieldLodoInci  = new TextField(10);
			jTextFieldLodoInci.addCaretListener(new CaretListener()
			{
				public void caretUpdate(CaretEvent evt)
				{
					EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldLodoInci, 10, aplicacion.getMainFrame());
				}
			});

			jTextFieldLodoInci.addFocusListener(new FocusAdapter() {
				public void focusLost(FocusEvent e) {
					recalcularLodosConOtroDestino();
				}


			});

			jTextFieldLodoInci.getDocument().addDocumentListener(new DocumentListener() {
				public void changedUpdate(DocumentEvent e) {
				}
				public void insertUpdate(DocumentEvent e) {
					recalcularLodosConOtroDestino();
				}
				public void removeUpdate(DocumentEvent e) {
					recalcularLodosConOtroDestino();
				}

			});

			jTextFieldLodoInci.setText(Integer.toString(0));
		}
		return jTextFieldLodoInci;
	}

	private JTextField getJTextFieldlocoConAgri()
	{
		if (jTextFieldLodoConAgri  == null)
		{
			jTextFieldLodoConAgri  = new TextField(10);
			jTextFieldLodoConAgri.addCaretListener(new CaretListener()
			{
				public void caretUpdate(CaretEvent evt)
				{
					EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldLodoConAgri, 10, aplicacion.getMainFrame());
				}
			});

			jTextFieldLodoConAgri.addFocusListener(new FocusAdapter() {
				public void focusLost(FocusEvent e) {
					recalcularLodosConOtroDestino();
				}


			});

			jTextFieldLodoConAgri.getDocument().addDocumentListener(new DocumentListener() {
				public void changedUpdate(DocumentEvent e) {

				}
				public void insertUpdate(DocumentEvent e) {
					recalcularLodosConOtroDestino();
				}
				public void removeUpdate(DocumentEvent e) {
					recalcularLodosConOtroDestino();
				}

			});

			jTextFieldLodoConAgri.setText(Integer.toString(0));
		}
		return jTextFieldLodoConAgri;
	}

	private JTextField getJTextFieldLodoSinAgri()
	{
		if (jTextFieldLodoSinAgri  == null)
		{
			jTextFieldLodoSinAgri  = new TextField(10);
			jTextFieldLodoSinAgri.addCaretListener(new CaretListener()
			{
				public void caretUpdate(CaretEvent evt)
				{
					EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldLodoSinAgri, 10, aplicacion.getMainFrame());
				}
			});

			jTextFieldLodoSinAgri.addFocusListener(new FocusAdapter() {
				public void focusLost(FocusEvent e) {
					recalcularLodosConOtroDestino();
				}


			});

			jTextFieldLodoSinAgri.getDocument().addDocumentListener(new DocumentListener() {
				public void changedUpdate(DocumentEvent e) {
				}
				public void insertUpdate(DocumentEvent e) {
					recalcularLodosConOtroDestino();
				}
				public void removeUpdate(DocumentEvent e) {
					recalcularLodosConOtroDestino();
				}

			});

			jTextFieldLodoSinAgri.setText(Integer.toString(0));
		}
		return jTextFieldLodoSinAgri;
	}

	private JTextField getJTextFieldLodoOt()
	{
		if (jTextFieldLodoOt  == null)
		{
			jTextFieldLodoOt  = new TextField(10);
			jTextFieldLodoOt.addCaretListener(new CaretListener()
			{
				public void caretUpdate(CaretEvent evt)
				{
					EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldLodoOt, 10, aplicacion.getMainFrame());

				}
			});

			recalcularLodosConOtroDestino();



		}
		return jTextFieldLodoOt;
	}

	private DateField getJTextFieldFechaInst()
	{
		if (jTextFieldfechaInst  == null)
		{
			jTextFieldfechaInst = new DateField( (java.util.Date) null, 0);
			jTextFieldfechaInst.setDateFormatString("yyyy-MM-dd");
			jTextFieldfechaInst.setEditable(true);
		}
		return jTextFieldfechaInst;
	}

	private JTextField getJTextFieldObserv()
	{
		if (jTextFieldObserv  == null)
		{
			jTextFieldObserv  = new TextField(50);
			jTextFieldObserv.addCaretListener(new CaretListener()
			{
				public void caretUpdate(CaretEvent evt)
				{
					EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldObserv, 50, aplicacion.getMainFrame());
				}
			});
		}
		return jTextFieldObserv;
	}


	private JTextField getJTextFieldFecha()
	{
		if (jTextFieldFecha  == null)
		{
			jTextFieldFecha  = new TextField();
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date date = new java.util.Date();
			String datetime = dateFormat.format(date);            
			jTextFieldFecha.setText(datetime);
		}
		return jTextFieldFecha;
	}

	public Depuradoras2Panel(GridBagLayout layout)
	{
		super(layout);
		initialize();
	}



	public String validateInput()
	{
		return null; 
	}


	/**
	 * This method initializes jPanelDatosIdentificacion	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	public JPanel getJPanelDatosIdentificacion()
	{
		if (jPanelIdentificacion == null)
		{   
			jPanelIdentificacion = new JPanel(new GridBagLayout());
			jPanelIdentificacion.setBorder(BorderFactory.createTitledBorder
					(null, I18N.get("LocalGISEIEL", "localgiseiel.panels.identity"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 


			jLabelClave = new JLabel("", JLabel.CENTER); 
			jLabelClave.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.clave"))); 

			jLabelCodProv = new JLabel("", JLabel.CENTER); 
			jLabelCodProv.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.codprov"))); 

			jLabelCodMunic = new JLabel("", JLabel.CENTER); 
			jLabelCodMunic.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.codmunic"))); 

			jLabelOrden  = new JLabel("", JLabel.CENTER);
			jLabelOrden.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.orden")));

			jPanelIdentificacion.add(jLabelClave,
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
							new Insets(5, 5, 5, 5), 0, 0));

			jPanelIdentificacion.add(getJTextFieldClave(), 
					new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
							new Insets(5, 5, 5, 5), 0, 0));

			jPanelIdentificacion.add(jLabelCodProv, 
					new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
							new Insets(5, 5, 5, 5), 0, 0));            

			jPanelIdentificacion.add(getJComboBoxProvincia(), 
					new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
							new Insets(5, 5, 5, 5), 0, 0));

			jPanelIdentificacion.add(jLabelCodMunic, 
					new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
							new Insets(5, 5, 5, 5), 0, 0));


			jPanelIdentificacion.add(getJComboBoxMunicipio(), 
					new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
							new Insets(5, 5, 5, 5), 0, 0));

			jPanelIdentificacion.add(jLabelOrden, 
					new GridBagConstraints(2, 1, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
							new Insets(5, 5, 5, 5), 0, 0));


			jPanelIdentificacion.add(getJTextFieldOrden(), 
					new GridBagConstraints(3, 1, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
							new Insets(5, 5, 5, 5), 0, 0));

			for (int i=0; i < jPanelIdentificacion.getComponentCount(); i++){
				jPanelIdentificacion.getComponent(i).setEnabled(false);
			}


		}
		return jPanelIdentificacion;
	}

	/**
	 * This method initializes jPanelDatosIdentificacion	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelDatosInformacion()
	{
		if (jPanelInformacion == null)
		{   
			jPanelInformacion  = new JPanel(new GridBagLayout());
			jPanelInformacion.setBorder(BorderFactory.createTitledBorder
					(null, I18N.get("LocalGISEIEL", "localgiseiel.panels.informacion"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 

			jLabelTitular = new JLabel("", JLabel.CENTER); 
			jLabelTitular.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.titular")); 

			jLabelGestor = new JLabel("", JLabel.CENTER); 
			jLabelGestor.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.gestor")); 

			jLabelCapacidad = new JLabel("", JLabel.CENTER);
			jLabelCapacidad.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.capacidad"));

			jLabelProblem1 = new JLabel("", JLabel.CENTER);
			jLabelProblem1.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.problem1"));

			jLabelProblem2 = new JLabel("", JLabel.CENTER);
			jLabelProblem2.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.problem2"));

			jLabelProblem3 = new JLabel("", JLabel.CENTER);
			jLabelProblem3.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.problem3"));

			jLabelGestionLodos = new JLabel("", JLabel.CENTER);
			jLabelGestionLodos.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.gestionlodos"));

			jLabelLodoVertidos = new JLabel("", JLabel.CENTER);
			jLabelLodoVertidos.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.lodovertidos"));

			jLabelLodoInci = new JLabel("", JLabel.CENTER);
			jLabelLodoInci.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.lodoinci"));

			jLabelLodoConAgri = new JLabel("", JLabel.CENTER);
			jLabelLodoConAgri.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.lodoconagri"));

			jLabelLodoSinAgri = new JLabel("", JLabel.CENTER);
			jLabelLodoSinAgri.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.lodosinagri"));

			jLabelLodoOt = new JLabel("", JLabel.CENTER);
			jLabelLodoOt.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.lodoot"));

			jLabelFechaInst = new JLabel("", JLabel.CENTER);
			jLabelFechaInst.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.fechainst"));

			jLabelObserv = new JLabel("", JLabel.CENTER);
			jLabelObserv.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.observ"));
			

			JPanel primeraLineaPanel = new JPanel(new GridBagLayout());
			
			primeraLineaPanel.add(jLabelTitular,
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
							GridBagConstraints.FIRST_LINE_END, GridBagConstraints.NONE,
							new Insets(5, 5, 5, 5),1, 0));

			primeraLineaPanel.add(getJComboBoxTitular(), 
					new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
							new Insets(5, 5, 5, 5), 100, 0));
			

			primeraLineaPanel.add(jLabelFechaInst,
					new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
							GridBagConstraints.FIRST_LINE_END, GridBagConstraints.NONE,
							new Insets(5, 5, 5, 5), 1, 0));

			primeraLineaPanel.add(getJTextFieldFechaInst(), 
					new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
							new Insets(5, 5, 5, 5), 100, 0));

			primeraLineaPanel.add(jLabelGestor,
					new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
							GridBagConstraints.FIRST_LINE_END, GridBagConstraints.NONE,
							new Insets(5, 5, 5, 5), 1, 0));

			primeraLineaPanel.add(getJComboBoxGestor(), 
					new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
							new Insets(5, 5, 5, 5), 100, 0));
			
			primeraLineaPanel.add(jLabelGestionLodos,
					new GridBagConstraints(2, 1, 1, 1, 0.1, 0.1,
							GridBagConstraints.FIRST_LINE_END, GridBagConstraints.NONE,
							new Insets(5, 5, 5, 5), 1, 0));

			primeraLineaPanel.add(getJComboBoxGestionLodos(), 
					new GridBagConstraints(3, 1, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
							new Insets(5, 5, 5, 5), 40, 0));

			primeraLineaPanel.add(jLabelCapacidad,
					new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1,
							GridBagConstraints.FIRST_LINE_END, GridBagConstraints.NONE,
							new Insets(5, 5, 5, 5), 1, 0));

			primeraLineaPanel.add(getJTextFieldCapacidad(), 
					new GridBagConstraints(1, 2, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
							new Insets(5, 5, 5, 5), 100, 0));

			primeraLineaPanel.add(jLabelProblem1,
					new GridBagConstraints(2, 2, 1, 1, 0.1, 0.1,
							GridBagConstraints.FIRST_LINE_END, GridBagConstraints.NONE,
							new Insets(5, 5, 5, 5), 1, 0));

			primeraLineaPanel.add(getJComboBoxProblem1(), 
					new GridBagConstraints(3, 2, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
							new Insets(5, 5, 5, 5), 100, 0));

			primeraLineaPanel.add(jLabelProblem2,
					new GridBagConstraints(0, 3, 1, 1, 0.1, 0.1,
							GridBagConstraints.FIRST_LINE_END, GridBagConstraints.NONE,
							new Insets(5, 5, 5, 5), 1, 0));

			primeraLineaPanel.add(getJComboBoxProblem2(), 
					new GridBagConstraints(1, 3, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
							new Insets(5, 5, 5, 5), 100, 0));

			primeraLineaPanel.add(jLabelProblem3,
					new GridBagConstraints(2, 3, 1, 1, 0.1, 0.1,
							GridBagConstraints.FIRST_LINE_END, GridBagConstraints.NONE,
							new Insets(5, 5, 5, 5), 1, 0));

			primeraLineaPanel.add(getJComboBoxProblem3(), 
					new GridBagConstraints(3, 3, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
							new Insets(5, 5, 5, 5), 100, 0));
			
			JPanel terceraLineaPanel = new JPanel(new GridBagLayout());

			terceraLineaPanel.add(jLabelLodoVertidos,
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
							GridBagConstraints.FIRST_LINE_END, GridBagConstraints.NONE,
							new Insets(5, 5, 5, 5), 1, 0));

			terceraLineaPanel.add(getJTextLodoVertidos(), 
					new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
							new Insets(5, 5, 5, 5), 40, 0));

			terceraLineaPanel.add(jLabelLodoInci,
					new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
							GridBagConstraints.FIRST_LINE_END, GridBagConstraints.NONE,
							new Insets(5, 5, 5, 5), 1, 0));

			terceraLineaPanel.add(getJTextFieldLodoInci(), 
					new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
							new Insets(5, 5, 5, 5), 40, 0));

			terceraLineaPanel.add(jLabelLodoConAgri,
					new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
							GridBagConstraints.FIRST_LINE_END, GridBagConstraints.NONE,
							new Insets(5, 5, 5, 5), 1, 0));

			terceraLineaPanel.add(getJTextFieldlocoConAgri(), 
					new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
							new Insets(5, 5, 5, 5), 40, 0));

			terceraLineaPanel.add(jLabelLodoSinAgri,
					new GridBagConstraints(2, 1, 1, 1, 0.1, 0.1,
							GridBagConstraints.FIRST_LINE_END, GridBagConstraints.NONE,
							new Insets(5, 5, 5, 5), 1, 0));

			terceraLineaPanel.add(getJTextFieldLodoSinAgri(), 
					new GridBagConstraints(3, 1, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
							new Insets(5, 5, 5, 5), 40, 0));

			terceraLineaPanel.add(jLabelLodoOt,
					new GridBagConstraints(2, 2, 1, 1, 0.1, 0.1,
							GridBagConstraints.FIRST_LINE_END, GridBagConstraints.NONE,
							new Insets(5, 5, 5, 5), 1, 0));

			terceraLineaPanel.add(getJTextFieldLodoOt(), 
					new GridBagConstraints(3, 2, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
							new Insets(5, 5, 5, 5), 40, 0));

			JPanel segundaLineaPanel = new JPanel(new GridBagLayout());
			
			segundaLineaPanel.add(jLabelObserv,
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
							GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
							new Insets(5, 0, 5, 0), 0, 0));

			segundaLineaPanel.add(getJTextFieldObserv(), 
					new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
							GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
							new Insets(5, 5, 5, 5), 250, 0));
			
			segundaLineaPanel.add(new JLabel(), 
					new GridBagConstraints(2, 0, 2, 1, 0.1, 0.1,
							GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
							new Insets(5, 5, 5, 5), 0, 0));
			segundaLineaPanel.add(new JLabel(), 
					new GridBagConstraints(3, 0,2, 1, 0.1, 0.1,
							GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
							new Insets(5, 5, 5, 5),0, 0));

			jPanelInformacion.add(primeraLineaPanel,
					new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
							new Insets(5, 5, 5, 5), 0, 0));
			jPanelInformacion.add(segundaLineaPanel,
					new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
							new Insets(5, 5, 5, 5), 0, 0));
			jPanelInformacion.add(terceraLineaPanel,
					new GridBagConstraints(0, 3, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
							new Insets(5, 5, 5, 5), 0, 0));

		}
		return jPanelInformacion;
	}

	private JPanel getJPanelDatosRevision()
	{
		if (jPanelRevision == null)
		{   
			jPanelRevision = new JPanel(new GridBagLayout());
			jPanelRevision.setBorder(BorderFactory.createTitledBorder
					(null, I18N.get("LocalGISEIEL", "localgiseiel.panels.revision"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 

			jLabelFecha  = new JLabel("", JLabel.CENTER); 
			jLabelFecha.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.fecha")); 

			jLabelEstado = new JLabel("", JLabel.CENTER); 
			jLabelEstado.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.estado")); 

			jPanelRevision.add(jLabelFecha,
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
							new Insets(5, 5, 5, 5), 0, 0));

			jPanelRevision.add(getJTextFieldFecha(), 
					new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
							new Insets(5, 5, 5, 5), 0, 0));

			jPanelRevision.add(jLabelEstado, 
					new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
							new Insets(5, 5, 5, 5), 0, 0));            

			jPanelRevision.add(getJComboBoxEstado(), 
					new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
							new Insets(5, 5, 5, 5), 130, 0));

		}
		return jPanelRevision;
	}


	public void okPressed()
	{
		okPressed = true;
	}

	public boolean getOkPressed()
	{
		return okPressed;
	}


	public boolean datosMinimosYCorrectos()
	{

		return  ((jTextFieldClave.getText()!=null && !jTextFieldClave.getText().equalsIgnoreCase("")) &&
				(jTextFieldOrden.getText()!=null && !jTextFieldOrden.getText().equalsIgnoreCase("")) &&
				(jComboBoxProvincia!=null && jComboBoxProvincia.getSelectedItem()!=null && jComboBoxProvincia.getSelectedIndex()>0) &&
				(jComboBoxMunicipio!=null && jComboBoxMunicipio.getSelectedItem()!=null && jComboBoxMunicipio.getSelectedIndex()>0)); 

	}

	public void enter() {
		loadData();
		loadDataIdentificacion();
	}

	public void exit() {

	}


	public void loadDataIdentificacion(){

		Object obj = AppContext.getApplicationContext().getBlackboard().get("featureDialog");
		if (obj != null && obj instanceof FeatureDialog){
			FeatureDialog featureDialog = (FeatureDialog) obj;
			Feature feature = featureDialog.get_fieldPanel().getModifiedFeature();

			GeopistaSchema esquema = (GeopistaSchema)feature.getSchema();
			feature.getAttribute(esquema.getAttributeByColumn("id"));

			String clave = null;
			if (feature.getAttribute(esquema.getAttributeByColumn("clave"))!=null){
				clave=(feature.getAttribute(esquema.getAttributeByColumn("clave"))).toString();
			}

			String codprov = null;
			if (feature.getAttribute(esquema.getAttributeByColumn("codprov"))!=null){
				codprov=(feature.getAttribute(esquema.getAttributeByColumn("codprov"))).toString();
			}

			String codmunic = null;
			if (feature.getAttribute(esquema.getAttributeByColumn("codmunic"))!=null){
				codmunic=(feature.getAttribute(esquema.getAttributeByColumn("codmunic"))).toString();
			}

			String orden_ed = null;
			if (feature.getAttribute(esquema.getAttributeByColumn("orden_ed"))!=null){
				orden_ed=(feature.getAttribute(esquema.getAttributeByColumn("orden_ed"))).toString();
			}

			EdicionOperations operations = new EdicionOperations();
			loadData(operations.getPanelDepuradora2EIEL(clave, codprov, codmunic, orden_ed));

			loadDataIdentificacion(clave, codprov, codmunic, orden_ed);       	
		}
	}



	public void loadDataIdentificacion(String clave, String codprov,
			String codmunic, String orden_ed) {

		//Datos identificacion
		if (clave != null){
			jTextFieldClave.setText(clave);
		}
		else{
			jTextFieldClave.setText("");
		}

		if (codprov != null) {
			// jComboBoxProvincia.setSelectedItem(codprov);
			jComboBoxProvincia
					.setSelectedIndex(provinciaIndexSeleccionar(codprov));
		} else {
			jComboBoxProvincia.setSelectedIndex(-1);
		}

		if (codmunic != null) {
			jComboBoxMunicipio
					.setSelectedIndex(municipioIndexSeleccionar(codmunic));
		} else {
			jComboBoxMunicipio.setSelectedIndex(-1);
		}

		if (orden_ed != null){
			jTextFieldOrden.setText(orden_ed);
		}
		else{
			jTextFieldOrden.setText("");
		}

	}


	/**
	 * Calculate 
	 */
	private static Border defaultBorder = new JTextField().getBorder();
	
	private void recalcularLodosConOtroDestino() {

		try{
			int sumaLodos = 0;

			if (jTextFieldLodoConAgri != null && jTextFieldLodoConAgri.getText() != null && !jTextFieldLodoConAgri.getText().equals("")){
				try{
					sumaLodos = sumaLodos + Integer.valueOf(jTextFieldLodoConAgri.getText());
					jTextFieldLodoConAgri.setBorder(defaultBorder);
				} catch (NumberFormatException e) {
					if (jTextFieldLodoConAgri.getBorder() instanceof TitledBorder) {

						TitledBorder bor = (TitledBorder)jTextFieldLodoConAgri.getBorder();
						if (bor != null)
							jTextFieldLodoConAgri.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.RED,2),""));

					} else{
						jTextFieldLodoConAgri.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
					}
				}
			}
			if (jTextFieldLodoSinAgri != null && jTextFieldLodoSinAgri.getText() != null && !jTextFieldLodoSinAgri.getText().equals("")){
				try{
					sumaLodos = sumaLodos + Integer.valueOf(jTextFieldLodoSinAgri.getText());
					jTextFieldLodoSinAgri.setBorder(defaultBorder);
				} catch (NumberFormatException e) {
					if (jTextFieldLodoSinAgri.getBorder() instanceof TitledBorder) {
						TitledBorder bor = (TitledBorder)jTextFieldLodoSinAgri.getBorder();
						if (bor != null)
							jTextFieldLodoSinAgri.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.RED,2),""));
					} else{
						jTextFieldLodoSinAgri.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
					}
				}			
			}
			if (jTextFieldLodoInci != null && jTextFieldLodoInci.getText() != null && !jTextFieldLodoInci.getText().equals("")){
				try{
					sumaLodos = sumaLodos + Integer.valueOf(jTextFieldLodoInci.getText());
					jTextFieldLodoInci.setBorder(defaultBorder);
				} catch (NumberFormatException e) {
					if (jTextFieldLodoInci.getBorder() instanceof TitledBorder) {

						TitledBorder bor = (TitledBorder)jTextFieldLodoInci.getBorder();
						if (bor != null)
							jTextFieldLodoInci.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.RED,2),""));
					} else{
						jTextFieldLodoInci.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
					}
				}			
			}
			if (jTextFieldLodoVertidos != null && jTextFieldLodoVertidos.getText() != null && !jTextFieldLodoVertidos.getText().equals("")){
				try{
					sumaLodos = sumaLodos + Integer.valueOf(jTextFieldLodoVertidos.getText());
					jTextFieldLodoVertidos.setBorder(defaultBorder);
				} catch (NumberFormatException e) {
					if (jTextFieldLodoVertidos.getBorder() instanceof TitledBorder) {

						TitledBorder bor = (TitledBorder)jTextFieldLodoVertidos.getBorder();
						if (bor != null)
							jTextFieldLodoVertidos.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.RED,2),""));
					} else{
						jTextFieldLodoVertidos.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
					}
				}			
			}


			if (jTextFieldLodoOt != null){
				if (sumaLodos <= 100) {
					jTextFieldLodoOt.setText(Integer.toString((100 - sumaLodos)));
					jTextFieldLodoOt.setBorder(defaultBorder);
				} else{
					jTextFieldLodoOt.setText(Long.toString((long)(100 - sumaLodos)));
					if ( jTextFieldLodoOt.getBorder() instanceof TitledBorder) {

						TitledBorder bor = (TitledBorder) jTextFieldLodoOt.getBorder();
						if (bor != null)
							jTextFieldLodoOt.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.RED,2),""));

					} else{
						jTextFieldLodoOt.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
					}

				}
			}


		} catch (NumberFormatException e){
			e.printStackTrace();
			jTextFieldLodoOt.setText(Integer.toString(-1));
		}
	}
	
	public int provinciaIndexSeleccionar(String provinciaId) {
		for (int i = 0; i < jComboBoxProvincia.getItemCount(); i++) {
			if ((!jComboBoxProvincia.getItemAt(i).equals(""))
					&& (jComboBoxProvincia.getItemAt(i) instanceof Provincia)
					&& ((Provincia) jComboBoxProvincia.getItemAt(i))
							.getIdProvincia().equals(provinciaId)) {
				return i;
			}
		}

		return -1;
	}

	public int municipioIndexSeleccionar(String municipioId) {
		if (!municipioId.equals("")) {
			for (int i = 0; i < jComboBoxMunicipio.getItemCount(); i++) {
				try {
					if (((Municipio) jComboBoxMunicipio.getItemAt(i))
							.getIdIne().equals(municipioId.substring(2, 5))) {
						// jComboBoxMunicipioNucleo.setEnabled(false);
						return i;
					}
				} catch (StringIndexOutOfBoundsException e) {
					if (((Municipio) jComboBoxMunicipio.getItemAt(i))
							.getIdIne().equals(municipioId)) {
						// jComboBoxMunicipio.setEnabled(false);
						return i;
					}
				}
			}
		}

		return -1;
	}

}
