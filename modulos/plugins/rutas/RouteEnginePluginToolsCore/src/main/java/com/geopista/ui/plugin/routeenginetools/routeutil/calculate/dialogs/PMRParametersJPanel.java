/**
 * PMRParametersJPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.routeenginetools.routeutil.calculate.dialogs;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;
import javax.swing.text.MaskFormatter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.localgis.route.weighter.PMRProperties;
import com.localgis.util.ConnectionUtilities;
import com.localgis.util.GeopistaRouteConnectionFactoryImpl;
import com.localgis.util.RouteConnectionFactory;
import com.vividsolutions.jump.I18N;

/**
 * Panel en el que se muestran los parámetros para rutas de movilidad reducida
 * @author miriamperez
 *
 */ 

public class PMRParametersJPanel extends JPanel implements PropertyChangeListener, ActionListener{

	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	private Map<String, PMRProperties> disabilityMap = new HashMap();
//	private PMRProperties pmrProperties;
	private PMRParametersJDialog dialog;
	private PMRProperties pmrProperties;
	private JLabel lblTiposMinusvalia = new JLabel(I18N.get("genred","tipos.minusvalia"));
	private JFormattedTextField jTextAnchuraMinAcera;
	private JLabel lblAnchuraMinAcera = new JLabel(I18N.get("genred","anchura.minima.acera"));
	private JFormattedTextField jTextMaxPendLongAcera;
	private JLabel lblMaxPendLongAcera = new JLabel(I18N.get("genred","max.pendiente.longitudinal.acera"));
	private JFormattedTextField jTextMaxPendTransAcera;
	private JLabel lblMaxPendTransAcera = new JLabel(I18N.get("genred","max.pendiente.transversal.acera"));
	private JLabel lblIrregularPaving = new JLabel(I18N.get("genred","considerar.pavimento.irregular"));
	private JLabel lblDifferentiatedPaving = new JLabel(I18N.get("genred","considerar.pavimento.irregular"));
	private JRadioButton rIrregularPavingYes = new JRadioButton("Sí", true);
	private JRadioButton rIrregularPavingNo = new JRadioButton("No", false);
	private JRadioButton rDifferentiatedPavingYes = new JRadioButton("Sí", true);
	private JRadioButton rDifferentiatedPavingNo = new JRadioButton("No", false);
	private NumberFormat amountFormat;
	private NumberFormat percentFormat;
	private static final Log logger	=LogFactory.getLog(PMRParametersJPanel.class);

	public PMRParametersJPanel(PMRParametersJDialog dialog){
		jbInit();
		this.dialog = dialog;
	}

	public PMRParametersJPanel(){
		jbInit();
	}

	private void jbInit(){
		JButton aceptarBtn = new JButton(I18N.get("genred","aceptar"));
		aceptarBtn.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
            	dialog.dispose();
            }
        });
		this.setBorder(BorderFactory.createTitledBorder
				(null, I18N.get("genred","caracteristicas.minusvalia"),
						TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12)));
		ButtonGroup buttonGroup1 = new ButtonGroup();
		buttonGroup1.add(rIrregularPavingYes);
		buttonGroup1.add(rIrregularPavingNo);
		ButtonGroup buttonGroup2 = new ButtonGroup();
		buttonGroup2.add(rDifferentiatedPavingYes);
		buttonGroup2.add(rDifferentiatedPavingNo);
		rDifferentiatedPavingNo.setSelected(true);

		String[] disabilityArray = this.obtenerTiposMinusvalia();
		comboTiposMinusvalia = new JComboBox(disabilityArray);
		comboTiposMinusvalia.addActionListener(this);
		try{
	        amountFormat = NumberFormat.getNumberInstance();
	        percentFormat = NumberFormat.getNumberInstance();
	        MaskFormatter mascara = new MaskFormatter("###.##");
			jTextAnchuraMinAcera = new JFormattedTextField(amountFormat);
			jTextAnchuraMinAcera.setColumns(10);
			jTextAnchuraMinAcera.setValue(new Double(100.0));
			jTextAnchuraMinAcera.addPropertyChangeListener("value", this);
			jTextMaxPendLongAcera = new JFormattedTextField(percentFormat);
			jTextMaxPendLongAcera.setColumns(10);
			jTextMaxPendLongAcera.setValue(new Double(6.0));
			jTextMaxPendLongAcera.addPropertyChangeListener("value", this);
			jTextMaxPendTransAcera = new JFormattedTextField(percentFormat);
			jTextMaxPendTransAcera.setColumns(10);
			jTextMaxPendTransAcera.setValue(new Double(2.0));
			jTextMaxPendTransAcera.addPropertyChangeListener("value", this);
		}catch(ParseException e){
			logger.debug(e);
		}

        this.setLayout(new java.awt.GridBagLayout());

        GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        this.add(lblTiposMinusvalia, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        this.add(comboTiposMinusvalia, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        this.add(lblAnchuraMinAcera, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        this.add(jTextAnchuraMinAcera, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        this.add(new JLabel("cm"), gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        this.add(lblMaxPendLongAcera, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        this.add(jTextMaxPendLongAcera, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        this.add(new JLabel("%"), gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        this.add(lblMaxPendTransAcera, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        this.add(jTextMaxPendTransAcera, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        this.add(new JLabel("%"), gridBagConstraints);

        /*        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        this.add(lblIrregularPaving, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        this.add(rIrregularPavingYes, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        this.add(rIrregularPavingNo, gridBagConstraints);*/

/*        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        this.add(lblDifferentiatedPaving, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        this.add(rDifferentiatedPavingYes, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        this.add(rDifferentiatedPavingNo, gridBagConstraints);*/

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        this.add(aceptarBtn, gridBagConstraints);

        /*        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        this.add(aceptarBtn, gridBagConstraints);*/
	}

/*	public void saveParameters(){
		pmrProperties = new PMRProperties();
		pmrProperties.setDisabilityType(((String)this.comboTiposMinusvalia.getSelectedItem()).charAt(0));
		pmrProperties.setConsiderateIrregularPaving(this.rIrregularPavingYes.isSelected());
		pmrProperties.setConsiderateNonDifferentiatedPaving(this.rDifferentiatedPavingYes.isSelected());
		pmrProperties.setMinWidth(Double.parseDouble(jTextAnchuraMinAcera.getText()));
		pmrProperties.setMaxLongitudinalSlope(Double.parseDouble(this.jTextMaxPendLongAcera.getText()));
		pmrProperties.setMaxTransversalSlope(Double.parseDouble(this.jTextMaxPendTransAcera.getText()));
		aplicacion.getBlackboard().put("PMRProperties", pmrProperties);
		this.dialog.dispose();
	}*/

	JComboBox comboTiposMinusvalia = new JComboBox();
	public JComboBox getComboTiposMinusvalia() {
		return comboTiposMinusvalia;
	}

	public void setComboTiposMinusvalia(JComboBox comboTiposMinusvalia) {
		this.comboTiposMinusvalia = comboTiposMinusvalia;
	}


	public JFormattedTextField getjTextAnchuraMinAcera() {
		return jTextAnchuraMinAcera;
	}

	public void setjTextAnchuraMinAcera(JFormattedTextField jTextAnchuraMinAcera) {
		this.jTextAnchuraMinAcera = jTextAnchuraMinAcera;
	}

	public JFormattedTextField getjTextMaxPendLongAcera() {
		return jTextMaxPendLongAcera;
	}

	public void setjTextMaxPendLongAcera(JFormattedTextField jTextMaxPendLongAcera) {
		this.jTextMaxPendLongAcera = jTextMaxPendLongAcera;
	}

	public JFormattedTextField getjTextMaxPendTransAcera() {
		return jTextMaxPendTransAcera;
	}

	public void setjTextMaxPendTransAcera(JFormattedTextField jTextMaxPendTransAcera) {
		this.jTextMaxPendTransAcera = jTextMaxPendTransAcera;
	}

	public JRadioButton getrIrregularPavingYes() {
		return rIrregularPavingYes;
	}

	public void setrIrregularPavingYes(JRadioButton rIrregularPavingYes) {
		this.rIrregularPavingYes = rIrregularPavingYes;
	}

	public JRadioButton getrIrregularPavingNo() {
		return rIrregularPavingNo;
	}

	public void setrIrregularPavingNo(JRadioButton rIrregularPavingNo) {
		this.rIrregularPavingNo = rIrregularPavingNo;
	}

	public JRadioButton getrDifferentiatedPaving() {
		return rDifferentiatedPavingYes;
	}

	public void setrDifferentiatedPaving(JRadioButton rDifferentiatedPavingYes) {
		this.rDifferentiatedPavingYes = rDifferentiatedPavingYes;
	}

	public JRadioButton getrDifferentiatedPavingNo() {
		return rDifferentiatedPavingNo;
	}

	public void setrDifferentiatedPavingNo(JRadioButton rDifferentiatedPavingNo) {
		this.rDifferentiatedPavingNo = rDifferentiatedPavingNo;
	}

	/** Called when a field's "value" property changes. */
	public void propertyChange(PropertyChangeEvent e) {
	    Object source = e.getSource();
	    if (source == this.jTextMaxPendLongAcera){
	    	if ((Double.parseDouble(this.jTextMaxPendLongAcera.getText())>100) || Double.parseDouble(this.jTextMaxPendLongAcera.getText())< 0){
	    		this.jTextMaxPendLongAcera.setValue(new Double(100));
	    	}
	    }else if (source == this.jTextMaxPendTransAcera){
	    	if ((Double.parseDouble(this.jTextMaxPendTransAcera.getText())>100) || Double.parseDouble(this.jTextMaxPendTransAcera.getText())< 0){
	    		this.jTextMaxPendTransAcera.setValue(new Double(100));
	    	}
	    }
	}


	/**
	 * Obtengo de la base de datos los distintos tipos de minusvalías y sus restricciones asociadas
	 * @return
	 */
	private String[] obtenerTiposMinusvalia(){
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs=null;
		Map<String,PMRProperties> disabilityMap = new HashMap<String,PMRProperties>();
		List<String> disabilityList = new ArrayList<String>();
		disabilityList.add("Ninguna");
		try{
			RouteConnectionFactory connectionFactory = new GeopistaRouteConnectionFactoryImpl();
			conn= connectionFactory.getConnection();
			String sQuery = "obtenerTiposMinusvalia";
			logger.debug(sQuery);
			PreparedStatement ps = conn.prepareStatement(sQuery);
			rs = ps.executeQuery();
			while (rs.next()){
				PMRProperties pmrProperties = new PMRProperties();
				pmrProperties.setDisabilityType(rs.getString("descripcion"));
				pmrProperties.setMaxLongitudinalSlope(rs.getDouble("max_pendiente_longitudinal"));
				pmrProperties.setMaxTransversalSlope(rs.getDouble("max_pendiente_transversal"));
				pmrProperties.setMinWidth(rs.getDouble("anchura_min_acera"));
				pmrProperties.setConsiderateIrregularPaving(Boolean.valueOf(rs.getInt("pavimento_irregular")==1));
				disabilityMap.put(rs.getString("descripcion"), pmrProperties);
				disabilityList.add(rs.getString("descripcion"));
			}
			this.disabilityMap = disabilityMap;
		}catch(Exception e){
			logger.error(e);
		}finally{
			ConnectionUtilities.closeConnection(conn, st, rs);
			return disabilityList.toArray(new String[disabilityList.size()]);
		}
	}

	/**
	 * Cuando se selecciona un tipo distinto de minusvalía se vuelven a cargar los datos
	 * @param e
	 */
	public void actionPerformed(ActionEvent e) {
        JComboBox cb = (JComboBox)e.getSource();
        String sName = (String)cb.getSelectedItem();
        PMRProperties pmrProperties = this.disabilityMap.get(sName);
        if (pmrProperties != null){
	        this.jTextAnchuraMinAcera.setText(String.valueOf(pmrProperties.getMinWidth()));
	        this.jTextMaxPendLongAcera.setText(String.valueOf(pmrProperties.getMaxLongitudinalSlope()));
	        this.jTextMaxPendTransAcera.setText(String.valueOf(pmrProperties.getMaxTransversalSlope()));
	        if (pmrProperties.isConsiderateIrregularPaving())
	        	this.rDifferentiatedPavingYes.setSelected(true);
	        else
	        	this.rDifferentiatedPavingNo.setSelected(true);
        }
    }
}
