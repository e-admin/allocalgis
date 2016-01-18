/**
 * DatosGeneralesComunesJPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.inventario.panel;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;

import com.geopista.app.AppContext;
import com.geopista.app.inventario.Estructuras;
import com.geopista.app.inventario.component.InventarioLabel;
import com.geopista.app.inventario.component.InventarioTextField;
import com.geopista.app.inventario.component.InventarioTextPane;
import com.geopista.app.utilidades.CalendarButton;
import com.geopista.app.utilidades.TextPane;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.protocol.inventario.BienBean;


/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 17-jul-2006
 * Time: 10:16:38
 * To change this template use File | Settings | File Templates.
 */
public class DatosGeneralesComunesJPanel extends javax.swing.JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private AppContext aplicacion;

	private String locale;

	/**
	 * Método que genera el panel de los datos Generales comunes a todos los bienes
	 * @param locale
	 */
	public DatosGeneralesComunesJPanel(String locale) {
		aplicacion = (AppContext) AppContext.getApplicationContext();
		this.locale = locale;
		initComponents();
		renombrarComponentes();
	}

	private void initComponents() {
		organizacionJLabel = new InventarioLabel();
		tipoBienJLabel = new InventarioLabel();
		numeroInventarioJLabel = new InventarioLabel();
		nombreJLabel = new InventarioLabel();
		usoJLabel = new InventarioLabel();
		descripcionJLabel = new InventarioLabel();
		organizacionJTField = new InventarioTextField();
		tipoBienJTField = new InventarioTextField();
		numInventarioJTField = new InventarioTextField();
		nombreJTField = new InventarioTextField(254);
		usoEJCBox = new ComboBoxEstructuras(Estructuras.getListaUsoJuridico(),
				null, locale, false);
		descripcionJScrollPane = new javax.swing.JScrollPane();
		descripcionJTArea = new InventarioTextPane(254);
		fAprobacionJLabel = new InventarioLabel();
	    fAprobacionJTField = new JFormattedTextField(com.geopista.app.inventario.Constantes.df);
	    fAprobacionJButton= new CalendarButton(fAprobacionJTField);
	    

		setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
		setBorder(BorderFactory.createEtchedBorder());

		add(organizacionJLabel,	new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 5, 105,20));
		add(tipoBienJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 25, 110, 20));
		add(numeroInventarioJLabel,	new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 45, 110,20));
		add(nombreJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10,	65, 110, 20));
		add(usoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10,85, 110, 20));
		add(descripcionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 105, 110,20));
		add(organizacionJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 5, 305,	-1));

		add(tipoBienJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(	130, 25, 305, -1));

		add(numInventarioJTField,new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 45, 160,-1));
		add(nombreJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 65, 305, -1));
		add(usoEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(130,85, 305, -1));

		descripcionJScrollPane.setViewportView(descripcionJTArea);

		add(descripcionJScrollPane,	new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 105, 305,45));
		add(fAprobacionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 155, -1, -1));
		add(fAprobacionJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 155, 140, -1));
	    add(fAprobacionJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 155, 20, 20));
	     
		setPreferredSize(new Dimension(200, 185));
		setMinimumSize(new Dimension(200, 185));

		//System.out.println("Cargando Ayuda Online....");
			

	}

	/**
	 * Método que actualiza los datos generales comunes para un bien
	 * @param bien a actualizar los datos generales
	 */
	public void actualizarDatosGeneralesComunes(BienBean bien) {
		if (bien == null)
			return;
		bien.setNombre(nombreJTField.getText().trim());
		bien.setUso(usoEJCBox.getSelectedPatron());
		bien.setDescripcion(descripcionJTArea.getText().trim());
		bien.setNumInventario(numInventarioJTField.getText().trim());
	    try{bien.setFechaAprobacionPleno(com.geopista.app.inventario.Constantes.df.parse(fAprobacionJTField.getText().trim()));}catch(java.text.ParseException e){}
	    
	}

	/**
	 * Método que carga en el panel los datos generales del bien
	 * @param bien a cargar en el panel
	 */
	public void load(BienBean bien) {
		if (bien == null)
			return;
		organizacionJTField.setText(bien.getOrganizacion() != null ? bien
				.getOrganizacion() : "");
		numInventarioJTField.setText(bien.getNumInventario() != null ? bien
				.getNumInventario() : "");
		try {
			tipoBienJTField.setText(bien.getTipo() != null ? Estructuras
					.getListaSubtipoBienesPatrimonio().getDomainNode(
							bien.getTipo()).getTerm(locale) : "");
		} catch (Exception e) {
			tipoBienJTField.setText("");
		}
		nombreJTField.setText(bien.getNombre() != null ? bien.getNombre() : "");
		try {
			usoEJCBox.setSelectedPatron(bien.getUso() != null ? bien.getUso()
					: "");
		} catch (Exception e) {
		}
		descripcionJTArea.setText(bien.getDescripcion() != null ? bien
				.getDescripcion() : "");
		try{fAprobacionJTField.setText(com.geopista.app.inventario.Constantes.df.format(bien.getFechaAprobacionPleno()));}catch(Exception e){}
	      

	}

	public void clear() {
		organizacionJTField.setText("");
		tipoBienJTField.setText("");
		nombreJTField.setText("");
		usoEJCBox.setSelectedPatron(null);
		descripcionJTArea.setText("");
		fAprobacionJTField.setText("");
	}

	public void setEnabled(boolean b) {
		descripcionJTArea.setEnabled(b);
		nombreJTField.setEnabled(b);
		numInventarioJTField.setEnabled(b);
		organizacionJTField.setEnabled(false);
		tipoBienJTField.setEnabled(false);
		usoEJCBox.setEnabled(b);
		fAprobacionJTField.setEnabled(false);
	    fAprobacionJButton.setEnabled(b);
	}

	public void numeroInventarioJLabelSetEnabled(boolean b) {
		//numeroInventarioJLabel.setEnabled(b);
	}

	private void renombrarComponentes() {
		try {organizacionJLabel.setText(aplicacion.getI18nString("inventario.datosGenerales.tag2"));} catch (Exception e) {}
		try {tipoBienJLabel.setText(aplicacion.getI18nString("inventario.datosGenerales.tag3"));} catch (Exception e) {	}
		try {numeroInventarioJLabel.setText(aplicacion.getI18nString("inventario.datosGenerales.tag4"));} catch (Exception e) {	}
		try {nombreJLabel.setText(aplicacion.getI18nString("inventario.datosGenerales.tag6"));} catch (Exception e) {}
		try {usoJLabel.setText(aplicacion.getI18nString("inventario.datosGenerales.tag7"));} catch (Exception e) {		}
		try {descripcionJLabel.setText(aplicacion.getI18nString("inventario.datosGenerales.tag8"));} catch (Exception e) {	}
		try{fAprobacionJLabel.setText(aplicacion.getI18nString("inventario.datosGenerales.tag25"));}catch(Exception e){}
		
	}

	private InventarioLabel descripcionJLabel;

	private javax.swing.JScrollPane descripcionJScrollPane;

	private TextPane descripcionJTArea;

	private InventarioLabel nombreJLabel;

	private com.geopista.app.utilidades.TextField nombreJTField;

	private InventarioTextField numInventarioJTField;

	private InventarioLabel numeroInventarioJLabel;

	private InventarioLabel organizacionJLabel;

	private InventarioTextField organizacionJTField;

	private InventarioLabel tipoBienJLabel;

	private InventarioTextField tipoBienJTField;

	private InventarioLabel usoJLabel;

	private ComboBoxEstructuras usoEJCBox;

	private InventarioLabel fAprobacionJLabel;
    
	private JFormattedTextField fAprobacionJTField;
	
    private CalendarButton fAprobacionJButton;
    
}
