/**
 * AddPMRParametersJPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.routeenginetools.networkfactorydialogs;

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
import org.apache.log4j.Logger;

import com.geopista.app.AppContext;
import com.localgis.route.weighter.PMRProperties;
import com.localgis.util.ConnectionUtilities;
import com.localgis.util.GeopistaRouteConnectionFactoryImpl;
import com.localgis.util.RouteConnectionFactory;
import com.vividsolutions.jump.I18N;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Panel en el que se muestran los parametros para rutas de movilidad reducida
 * @author miriamperez
 *
 */

public class AddPMRParametersJPanel extends JPanel implements PropertyChangeListener {

	private static Logger LOGGER = Logger.getLogger(AddPMRParametersJPanel.class);
	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
//	private PMRProperties pmrProperties;
	private AddPMRParametersDialog dialog;
	private PMRProperties pmrProperties;
	private JFormattedTextField jTextAnchuraAcera;
	private JLabel lblAnchuraAcera = new JLabel(I18N.get("genred","anchura.acera"));
	private JFormattedTextField jTextPendLongAcera;
	private JLabel lbPendLongAcera = new JLabel(I18N.get("genred","pendiente.longitudinal.acera"));
	private JFormattedTextField jTextPendTransAcera;
	private JFormattedTextField jTextObstacleHeight;
	private JLabel lblPendTransAcera = new JLabel(I18N.get("genred","pendiente.transversal.acera"));
	private JLabel lblIrregularPaving = new JLabel(I18N.get("genred","considerar.pavimento.irregular"));
	private JLabel lbObstacleHeight = new JLabel(I18N.get("genred","alturaObstaculo"));
	private JRadioButton rIrregularPavingYes = new JRadioButton("Si", true);
	private JRadioButton rIrregularPavingNo = new JRadioButton("No", false);
	private NumberFormat amountFormat;
	private NumberFormat percentFormat;
	private static final Log logger	=LogFactory.getLog(AddPMRParametersJPanel.class);

	public AddPMRParametersJPanel(AddPMRParametersDialog dialog){
		this.dialog = dialog;
		jbInit();
	}


	private void jbInit(){
		JButton aceptarBtn = new JButton(I18N.get("genred","aceptar"));
		aceptarBtn.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                 saveParameters();
            }
        });
		this.setBorder(BorderFactory.createTitledBorder
				(null, I18N.get("genred","caracteristicas.minusvalia"),
						TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12)));
/*		ButtonGroup buttonGroup1 = new ButtonGroup();
		buttonGroup1.add(rIrregularPavingYes);
		buttonGroup1.add(rIrregularPavingNo);
		rIrregularPavingNo.setSelected(true);*/
		try{
	        amountFormat = NumberFormat.getNumberInstance();
	        percentFormat = NumberFormat.getNumberInstance();
	        MaskFormatter mascara = new MaskFormatter("###.##");
			jTextAnchuraAcera = new JFormattedTextField(amountFormat);
			if (dialog.getPmrProperties() != null)
				jTextAnchuraAcera.setValue(new Double(dialog.getPmrProperties().getMinWidth()));
			else
				jTextAnchuraAcera.setValue(new Double(100.0));
			jTextAnchuraAcera.setColumns(10);
			jTextAnchuraAcera.addPropertyChangeListener("value", this);
			jTextPendLongAcera = new JFormattedTextField(percentFormat);
			if (dialog.getPmrProperties() != null)
				jTextPendLongAcera.setValue(new Double(dialog.getPmrProperties().getMaxLongitudinalSlope()));
			else
				jTextPendLongAcera.setValue(new Double(6.0));
			jTextPendLongAcera.setColumns(10);
			jTextPendLongAcera.addPropertyChangeListener("value", this);
			jTextPendTransAcera = new JFormattedTextField(percentFormat);
			if (dialog.getPmrProperties() != null)
				jTextPendTransAcera.setValue(new Double(dialog.getPmrProperties().getMaxTransversalSlope()));
			else
				jTextPendTransAcera.setValue(new Double(2.0));
			jTextPendTransAcera.setColumns(10);
			jTextPendTransAcera.addPropertyChangeListener("value", this);
			jTextObstacleHeight = new JFormattedTextField(percentFormat);
			if (dialog.getPmrProperties() != null)
				jTextObstacleHeight.setValue(new Double(dialog.getPmrProperties().getObstacleHeight()));
			else
				jTextObstacleHeight.setValue(new Double(0));
			jTextObstacleHeight.setColumns(10);
			jTextObstacleHeight.addPropertyChangeListener("value", this);
		}catch(ParseException e){
			LOGGER.debug(e);
		}

        this.setLayout(new java.awt.GridBagLayout());

        GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        this.add(lblAnchuraAcera, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        this.add(jTextAnchuraAcera, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        this.add(new JLabel("m"), gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        this.add(lbPendLongAcera, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        this.add(jTextPendLongAcera, gridBagConstraints);

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
        this.add(lblPendTransAcera, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        this.add(jTextPendTransAcera, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        this.add(new JLabel("%"), gridBagConstraints);


        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        this.add(lbObstacleHeight, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        this.add(jTextObstacleHeight, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        this.add(new JLabel("m"), gridBagConstraints);

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

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        this.add(aceptarBtn, gridBagConstraints);
	}

	public void saveParameters(){
		pmrProperties = new PMRProperties();
		pmrProperties.setConsiderateIrregularPaving(this.rIrregularPavingYes.isSelected());
		pmrProperties.setMinWidth(Double.parseDouble(jTextAnchuraAcera.getText().replace(",", ".")));
		pmrProperties.setMaxLongitudinalSlope(Double.parseDouble(this.jTextPendLongAcera.getText()));
		pmrProperties.setMaxTransversalSlope(Double.parseDouble(this.jTextPendTransAcera.getText()));
		pmrProperties.setObstacleHeight(Double.parseDouble(this.jTextObstacleHeight.getText().replace(",", ".")));
		dialog.setPmrProperties(pmrProperties);
		this.dialog.dispose();
	}

	public JFormattedTextField getjTextAnchuraAcera() {
		return jTextAnchuraAcera;
	}

	public void setjTextAnchuraAcera(JFormattedTextField jTextAnchuraMinAcera) {
		this.jTextAnchuraAcera = jTextAnchuraMinAcera;
	}

	public JFormattedTextField getjTextPendLongAcera() {
		return jTextPendLongAcera;
	}

	public void setjTextPendLongAcera(JFormattedTextField jTextMaxPendLongAcera) {
		this.jTextPendLongAcera = jTextMaxPendLongAcera;
	}

	public JFormattedTextField getjTextPendTransAcera() {
		return jTextPendTransAcera;
	}

	public JFormattedTextField jTextObstacleHeight() {
		return jTextObstacleHeight;
	}

	public void setjTextPendTransAcera(JFormattedTextField jTextMaxPendTransAcera) {
		this.jTextPendTransAcera = jTextMaxPendTransAcera;
	}

	public void setjTextObstacleHeight(JFormattedTextField jTextObstacleHeight) {
		this.jTextObstacleHeight = jTextObstacleHeight;
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

	/** Called when a field's "value" property changes. */
	public void propertyChange(PropertyChangeEvent e) {
	    Object source = e.getSource();
	    if (source == this.jTextPendLongAcera){
	    	if ((Double.parseDouble(this.jTextPendLongAcera.getText())>100) || Double.parseDouble(this.jTextPendLongAcera.getText())< 0){
	    		this.jTextPendLongAcera.setValue(new Double(0));
	    	}
	    }else if (source == this.jTextPendTransAcera){
	    	if ((Double.parseDouble(this.jTextPendTransAcera.getText())>100) || Double.parseDouble(this.jTextPendTransAcera.getText())< 0){
	    		this.jTextPendTransAcera.setValue(new Double(0));
	    	}
	    }else if (source == this.jTextObstacleHeight){
	    	if ((Double.parseDouble(this.jTextObstacleHeight.getText())>100) || Double.parseDouble(this.jTextObstacleHeight.getText())< 0){
	    		this.jTextObstacleHeight.setValue(new Double(0));
	    	}
	    }else if (source == this.jTextAnchuraAcera){
	    	if (Double.parseDouble(this.jTextAnchuraAcera.getText())< 0){
	    		this.jTextAnchuraAcera.setValue(new Double(100));
	    	}
	    }
	}
}
