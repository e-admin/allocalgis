/**
 * BuscadorCallejeroPanelBusqueda.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.buscadorcallejero.paneles;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.editor.GeopistaEditor;
import com.geopista.editor.TaskComponent;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.feature.Attribute;
import com.geopista.feature.GeopistaFeature;
import com.geopista.feature.GeopistaSchema;
import com.geopista.model.GeopistaLayer;
import com.geopista.ui.GeopistaOneLayerAttributeTab;
import com.geopista.util.ApplicationContext;
import com.geopista.util.GeopistaUtil;
import com.geopista.util.config.UserPreferenceStore;
import com.vividsolutions.jts.util.Assert;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.coordsys.CoordinateSystemRegistry;
import com.vividsolutions.jump.workbench.model.CategoryEvent;
import com.vividsolutions.jump.workbench.model.FeatureEvent;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.LayerEvent;
import com.vividsolutions.jump.workbench.model.LayerListener;
import com.vividsolutions.jump.workbench.model.LayerManager;
import com.vividsolutions.jump.workbench.model.LayerManagerProxy;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.CloneableInternalFrame;
import com.vividsolutions.jump.workbench.ui.ILayerViewPanel;
import com.vividsolutions.jump.workbench.ui.LayerNamePanel;
import com.vividsolutions.jump.workbench.ui.LayerNamePanelProxy;
import com.vividsolutions.jump.workbench.ui.LayerViewPanelProxy;
import com.vividsolutions.jump.workbench.ui.SelectionManager;
import com.vividsolutions.jump.workbench.ui.SelectionManagerProxy;
import com.vividsolutions.jump.workbench.ui.TaskFrameProxy;

public class BuscadorCallejeroPanelBusqueda extends JPanel{

	private Collection attributeCollection=new ArrayList();
	 
	ApplicationContext appContext=AppContext.getApplicationContext();
	
	private String user = "";
	private boolean okPressed = false;
	private PlugInContext context = null;
	private static final Log    logger  = LogFactory.getLog(GeopistaUtil.class);
	
	private JPanel panelBotonera = null;
	private JPanel panelGeocodificar = null;
	private JPanel panelOpcionesAvanzadas = null;
	
	private JTextField nombreviaTextField = null;
	private JTextField numeroTextField = null;
	
	private JCheckBox chkLiteraltext = null;
	private JCheckBox chkOpcionesAvanzadas = null;
	private JButton btnCancelar = null;
	private JButton btnAceptar = null;
	private JLabel introduzcaDatosLabel = null;
	private JLabel opcionesAvanzadasConfigLabel = null;
	
	
	
	private JPanel panelBuscadorNombreVia= null;
	private JLabel valorNombreViaLabel = null;
	private JLabel capaNombreViaLabel = null;
	private JLabel atributoNombreViaLabel = null;
	private JComboBox atributosNombreViaJCBox = null;
	private JLabel relacionNumeroLabel = null;
	private JLabel relacion2NumeroLabel = null;
	private JComboBox capaNombreViaJCBox = null;
	private JComboBox relacionNumeroJCBox = null;
	
	
	private JPanel panelBuscadorNumero= null;	
	private JLabel valorNumeroLabel = null;
	private JLabel capaNumeroLabel = null;
	private JLabel atributoNumeroLabel = null;
	private JComboBox atributosNumeroJCBox = null;
	private JLabel relacionNombreViaLabel = null;
	private JLabel relacion2NombreViaLabel = null;
	private JComboBox capaNumeroJCBox = null;
	private JComboBox relacionNombreViaJCBox = null;


	public BuscadorCallejeroPanelBusqueda(PlugInContext context, String user)
	{
		Locale loc=I18N.getLocaleAsObject();      
    	ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.ui.plugin.buscadorcallejero.languages.BuscadorCallejeroi18n",loc,this.getClass().getClassLoader());
    	I18N.plugInsResourceBundle.put("BuscadorCallejero",bundle);   
    	
		Object[] projections = CoordinateSystemRegistry
		  .instance(context.getWorkbenchContext()
		          .getBlackboard()).getCoordinateSystems().toArray();
			this.user = user;
		    this.context=context;
		    try
		    {
		      jbInit();
		    }
		    catch(Exception e)
		    {
		      e.printStackTrace();
		    }
	
	}
	
	private void jbInit() throws Exception
	{
		this.setLayout(new GridBagLayout());

		
		this.add(getPanelGeocodificar(), 
				new GridBagConstraints(0,0,1,1, 1, 0.8,GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
		
		this.add(getPanelOpcionesAvanzadas(), 
				new GridBagConstraints(0,1,1,1, 1, 0.8,GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
		
		this.add(getPanelBotonera(), 
				new GridBagConstraints(0,3,2,1, 1, 0.2,GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
		
		inicializarOpcionesAvanzadas();
		cargarBusqueda();
		
	}
	
	
	private void inicializarOpcionesAvanzadas(){
		
		if(chkOpcionesAvanzadas.isSelected()){
			
			opcionesAvanzadasConfigLabel.setVisible(false);
			panelBuscadorNombreVia.setVisible(true);
			panelBuscadorNumero.setVisible(true);
		}
		else{
			opcionesAvanzadasConfigLabel.setVisible(true);
			panelBuscadorNombreVia.setVisible(false);
			panelBuscadorNumero.setVisible(false);
		}
		
	}
	
	public JPanel getPanelGeocodificar() {
		if(panelGeocodificar == null){
			panelGeocodificar = new JPanel();
			panelGeocodificar.setLayout(new GridBagLayout());
			
			panelGeocodificar.setBorder(BorderFactory.createTitledBorder
					(null,I18N.get("BuscadorCallejero","buscadorCallejero.popup.geocodificacion"), 
							TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12),Color.BLUE));
			
			introduzcaDatosLabel = new JLabel();
			introduzcaDatosLabel.setFont(new Font("Dialog", 1, 11));
			introduzcaDatosLabel.setText(I18N.get("BuscadorCallejero", "buscadorCallejero.popup.introduzcaDatos"));
			
			valorNombreViaLabel = new JLabel();
			valorNombreViaLabel .setText(I18N.get("BuscadorCallejero", "buscadorCallejero.popup.nombreViaBusqueda"));
			
			valorNumeroLabel = new JLabel();
			valorNumeroLabel.setText(I18N.get("BuscadorCallejero", "buscadorCallejero.popup.numeroBusqueda"));
			
			
			panelGeocodificar.add(introduzcaDatosLabel, 
					new GridBagConstraints(0,0,4,1, 1, 1,GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(20,20,0,5),0,0));
			
			panelGeocodificar.add(valorNombreViaLabel, 
					new GridBagConstraints(0,1,1,1, 1, 1,GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(20,20,0,0),0,0));
			
			panelGeocodificar.add(getNombreviaTextField(), 
					new GridBagConstraints(1,1,1,1, 1, 1,GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(20,5,0,0),0,0));
			
			panelGeocodificar.add(valorNumeroLabel, 
					new GridBagConstraints(2,1,1,1, 1, 1,GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(20,10,0,0),0,0));
			
			panelGeocodificar.add(getNumeroTextField(), 
					new GridBagConstraints(3,1,1,1, 1, 1,GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(20,5,0,0),0,0));
			
			panelGeocodificar.add(getChkLiteraltext(), 
					new GridBagConstraints(0,2,1,1, 1, 1,GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(20,20,0,0),0,0));
			
			panelGeocodificar.add(getChkOpcionesAvanzadas(), 
					new GridBagConstraints(2,2,1,1, 1, 1,GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(20,0,0,0),0,0));
			
		}
		return panelGeocodificar;
	}

	
	public void setPanelGeocodificar(JPanel panelGeocodificar) {
		this.panelGeocodificar = panelGeocodificar;
	}

	
	public JCheckBox getChkLiteraltext() {
		if(chkLiteraltext == null){
			chkLiteraltext = new JCheckBox();
			chkLiteraltext.setText(I18N.get("BuscadorCallejero", "buscadorCallejero.popup.textoLiteral"));
		}
		return chkLiteraltext;
	}

	public void setChkLiteraltext(JCheckBox chkLiteraltext) {
		this.chkLiteraltext = chkLiteraltext;
	}

	public JCheckBox getChkOpcionesAvanzadas() {
		if(chkOpcionesAvanzadas == null){
			chkOpcionesAvanzadas = new JCheckBox();
			chkOpcionesAvanzadas.setText(I18N.get("BuscadorCallejero", "buscadorCallejero.popup.opcionesAvanzadas"));
			
			chkOpcionesAvanzadas.setSelected(false);
			
			chkOpcionesAvanzadas.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent arg0) {
					if(chkOpcionesAvanzadas.isSelected()){
						
						opcionesAvanzadasConfigLabel.setVisible(false);
						panelBuscadorNombreVia.setVisible(true);
						panelBuscadorNumero.setVisible(true);
					}
					else{
						opcionesAvanzadasConfigLabel.setVisible(true);
						panelBuscadorNombreVia.setVisible(false);
						panelBuscadorNumero.setVisible(false);
					}
				}    		
			});
			
			
		}
		return chkOpcionesAvanzadas;
	}

	public void setChkOpcionesAvanzadas(JCheckBox chkOpcionesAvanzadas) {
		this.chkOpcionesAvanzadas = chkOpcionesAvanzadas;
	}

	
	public JPanel getPanelOpcionesAvanzadas() {
		if(panelOpcionesAvanzadas == null){
			panelOpcionesAvanzadas = new JPanel();
			panelOpcionesAvanzadas.setLayout(new GridBagLayout());
	
			panelOpcionesAvanzadas.setBorder(BorderFactory.createTitledBorder
					(null,I18N.get("BuscadorCallejero","buscadorCallejero.popup.opcionesAvanzadas"), 
							TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12),Color.BLUE));

			opcionesAvanzadasConfigLabel = new JLabel();
			opcionesAvanzadasConfigLabel.setText(I18N.get("BuscadorCallejero","buscadorCallejero.popup.seleccioneOpcionesAvanzadas"));
			
			panelOpcionesAvanzadas.add(opcionesAvanzadasConfigLabel, 
					new GridBagConstraints(0,0,1,1, 1, 1,GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(80,140,150,5),0,0));
			
			panelOpcionesAvanzadas.add(getPanelBuscadorNombreVia(), 
					new GridBagConstraints(0,1,1,1, 1, 0.8,GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
			
			panelOpcionesAvanzadas.add(getPanelBuscadorNumero(), 
					new GridBagConstraints(1,1,1,1, 1, 0.8,GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
				
		}
		return panelOpcionesAvanzadas;
	}

	public void setPanelOpcionesAvanzadas(JPanel panelOpcionesAvanzadas) {
		this.panelOpcionesAvanzadas = panelOpcionesAvanzadas;
	}

	public JTextField getNombreviaTextField() {
		if(nombreviaTextField == null){
			
			nombreviaTextField = new JTextField();	
		}
		
		return nombreviaTextField;
	}

	
	public void setNombreviaTextField(JTextField nombreviaTextField) {
		this.nombreviaTextField = nombreviaTextField;
	}

	public JTextField getNumeroTextField() {
		if(numeroTextField == null){
			numeroTextField = new JTextField();
		
		}
		return numeroTextField;
	}

	public void setNumeroTextField(JTextField numeroTextField) {
		this.numeroTextField = numeroTextField;
	}

	
	public JPanel getPanelBuscadorNombreVia() {
		
		if(panelBuscadorNombreVia == null){
			panelBuscadorNombreVia = new JPanel();
			panelBuscadorNombreVia.setLayout(new GridBagLayout());
			panelBuscadorNombreVia.setVisible(false);
			
			capaNombreViaLabel = new JLabel();
			capaNombreViaLabel.setText(I18N.get("BuscadorCallejero", "buscadorCallejero.popup.seleccioneCapa"));
			
			atributoNombreViaLabel = new JLabel();
			atributoNombreViaLabel.setText(I18N.get("BuscadorCallejero", "buscadorCallejero.popup.seleccioneAtributo"));

			
			relacionNumeroLabel = new JLabel();
			relacionNumeroLabel.setText(I18N.get("BuscadorCallejero", "buscadorCallejero.popup.relacion"));
			
			
			relacion2NumeroLabel = new JLabel();
			relacion2NumeroLabel.setText(I18N.get("BuscadorCallejero", "buscadorCallejero.popup.relacionNumero"));
		
			
			panelBuscadorNombreVia.setBorder(BorderFactory.createTitledBorder
					(null,I18N.get("BuscadorCallejero","buscadorCallejero.popup.nombreViaBusqueda"), 
							TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12),Color.BLUE));
			
			panelBuscadorNombreVia.add(capaNombreViaLabel, 
						new GridBagConstraints(0,0,1,1, 1, 1,GridBagConstraints.CENTER,
								GridBagConstraints.BOTH, new Insets(20,20,0,5),0,0));
			
			panelBuscadorNombreVia.add(getCapaNombreViaJCBox(), 
					new GridBagConstraints(0,1,1,1, 1, 1,GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(10,30,0,10),0,0));
			
			panelBuscadorNombreVia.add(atributoNombreViaLabel, 
					new GridBagConstraints(0,2,1,1, 1, 1,GridBagConstraints.CENTER,
							GridBagConstraints.BOTH, new Insets(20,20,0,5),0,0));
			
			panelBuscadorNombreVia.add(getAtributosNombreViaJCBox(), 
					new GridBagConstraints(0,3,1,1, 1, 1,GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(10,30,0,10),0,0));
			
			panelBuscadorNombreVia.add(relacionNumeroLabel, 
					new GridBagConstraints(0,6,1,1, 1, 1,GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(20,20,0,5),0,0));
			
			panelBuscadorNombreVia.add(relacion2NumeroLabel, 
					new GridBagConstraints(0,7,1,1, 1, 1,GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(5,20,0,5),0,0));
			
			panelBuscadorNombreVia.add(getRelacionNumeroJCBox(), 
					new GridBagConstraints(0,8,1,1, 1, 1,GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(10,30,10,10),0,0));
		
			
		}
		return panelBuscadorNombreVia;
	}

	public void setPanelBuscadorNombreVia(JPanel panelBuscadorNombreVia) {
		this.panelBuscadorNombreVia = panelBuscadorNombreVia;
	}

	public JComboBox getCapaNombreViaJCBox() {
		
		if(capaNombreViaJCBox == null) {
			capaNombreViaJCBox = new JComboBox();
			capaNombreViaJCBox.setEnabled(true);
			capaNombreViaJCBox.setEditable(true);
			
			capaNombreViaJCBox.addItem("");
			Iterator iter =  context.getLayerManager().getLayers().iterator();
			while(iter.hasNext()){
				GeopistaLayer layer = (GeopistaLayer)iter.next();
				capaNombreViaJCBox.addItem(layer.getSystemId());

			}
			
			capaNombreViaJCBox.addActionListener(new ActionListener(){    		
				public void actionPerformed(ActionEvent arg0) {
					atributosNombreViaJCBox.removeAllItems();
					relacionNumeroJCBox.removeAllItems();
					atributosNombreViaJCBox.addItem("");
					relacionNumeroJCBox.addItem("");
					
					if(!((String)capaNombreViaJCBox.getSelectedItem()).equals("")){
						ArrayList lstAtributos = obtenerAtributosCapa((String)capaNombreViaJCBox.getSelectedItem());
						
						relacionNumeroJCBox.addItem("");
						for (int i=0; i< lstAtributos.size(); i++){
							atributosNombreViaJCBox.addItem((String)lstAtributos.get(i));
							relacionNumeroJCBox.addItem((String)lstAtributos.get(i));
						}
						atributosNombreViaJCBox.setEnabled(true);
						relacionNumeroJCBox.setEnabled(true);
						
					}
					else{
						atributosNombreViaJCBox.setEnabled(false);
						relacionNumeroJCBox.setEnabled(false);
					}

				}
    			
	        });
			
		}
		return capaNombreViaJCBox;
		
	}

	public void setNombreViaJCBox(JComboBox capaJCBox) {
		this.capaNombreViaJCBox = capaJCBox;
	}
	
	public JComboBox getAtributosNombreViaJCBox() {
		if(atributosNombreViaJCBox == null){
			atributosNombreViaJCBox = new JComboBox();
			atributosNombreViaJCBox.removeAllItems();

			if(!((String)capaNombreViaJCBox.getSelectedItem()).equals("")){
				ArrayList lstAtributos = obtenerAtributosCapa((String)capaNombreViaJCBox.getSelectedItem());
				
				for (int i=0; i< lstAtributos.size(); i++){
					atributosNombreViaJCBox.addItem((String)lstAtributos.get(i));
					
				}
			}
			
		}
		return atributosNombreViaJCBox;
	}

	public void setAtributosNombreViaJCBox(JComboBox atributosJCBox) {
		this.atributosNombreViaJCBox = atributosJCBox;
	}

	public JPanel getPanelBotonera() {
		if(panelBotonera == null){
			panelBotonera = new JPanel();		
			
			
			panelBotonera.add(getBtnAceptar(), 
					new GridBagConstraints(0,0,1,1, 1, 1,GridBagConstraints.CENTER,
							GridBagConstraints.BOTH, new Insets(0,100,0,5),0,0));
			
			panelBotonera.add(getBtnCancelar(), 
					new GridBagConstraints(1,0,1,1, 1, 1,GridBagConstraints.CENTER,
							GridBagConstraints.BOTH, new Insets(0,5,0,5),0,0));
			
		}
		return panelBotonera;
	}

	public void setPanelBotonera(JPanel panelBotonera) {
		this.panelBotonera = panelBotonera;
	}

	public JPanel getPanelBuscadorNumero() {
		
		if(panelBuscadorNumero == null){
			panelBuscadorNumero = new JPanel();
			panelBuscadorNumero.setLayout(new GridBagLayout());
			panelBuscadorNumero.setVisible(false);

			capaNumeroLabel = new JLabel();
			capaNumeroLabel.setText(I18N.get("BuscadorCallejero", "buscadorCallejero.popup.seleccioneCapa"));
			
			atributoNumeroLabel = new JLabel();
			atributoNumeroLabel.setText(I18N.get("BuscadorCallejero", "buscadorCallejero.popup.seleccioneAtributo"));

			
			relacionNombreViaLabel = new JLabel();
			relacionNombreViaLabel.setText(I18N.get("BuscadorCallejero", "buscadorCallejero.popup.relacion"));
			
			relacion2NombreViaLabel = new JLabel();
			relacion2NombreViaLabel.setText(I18N.get("BuscadorCallejero", "buscadorCallejero.popup.relacionNombre"));
			
			
			panelBuscadorNumero.setBorder(BorderFactory.createTitledBorder
					(null,I18N.get("BuscadorCallejero","buscadorCallejero.popup.numeroBusqueda"), 
							TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12),Color.BLUE));
			
			panelBuscadorNumero.add(capaNumeroLabel, 
					new GridBagConstraints(0,0,1,1, 1, 1,GridBagConstraints.CENTER,
							GridBagConstraints.BOTH, new Insets(20,20,0,5),0,0));
			
			panelBuscadorNumero.add(getCapaNumeroJCBox(), 
				new GridBagConstraints(0,1,1,1, 1, 1,GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, new Insets(10,30,0,10),0,0));
		
			panelBuscadorNumero.add(atributoNumeroLabel, 
				new GridBagConstraints(0,2,1,1, 1, 1,GridBagConstraints.CENTER,
						GridBagConstraints.BOTH, new Insets(20,20,0,5),0,0));
		
			panelBuscadorNumero.add(getAtributosNumeroJCBox(), 
				new GridBagConstraints(0,3,1,1, 1, 1,GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, new Insets(10,30,0,10),0,0));

			panelBuscadorNumero.add(relacionNombreViaLabel, 
					new GridBagConstraints(0,6,1,1, 1, 1,GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(20,20,0,5),0,0));
			
			panelBuscadorNumero.add(relacion2NombreViaLabel, 
					new GridBagConstraints(0,7,1,1, 1, 1,GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(5,20,0,5),0,0));
			
			panelBuscadorNumero.add(getRelacionNombreViaJCBox(), 
					new GridBagConstraints(0,8,1,1, 1, 1,GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(10,30,10,10),0,0));
			
		}
		
		return panelBuscadorNumero;
		
	}

	public void setPanelBuscadorNumero(JPanel panelBuscadorNumero) {
		this.panelBuscadorNumero = panelBuscadorNumero;
	}

	
	public JComboBox getCapaNumeroJCBox() {
			
		if(capaNumeroJCBox == null) {
			capaNumeroJCBox = new JComboBox();
			capaNumeroJCBox.setEnabled(true);
			capaNumeroJCBox.setEditable(true);
			
			capaNumeroJCBox.addItem("");
			Iterator iter =  context.getLayerManager().getLayers().iterator();
			while(iter.hasNext()){
				GeopistaLayer layer = (GeopistaLayer)iter.next();
				capaNumeroJCBox.addItem(layer.getSystemId());
				
			}
			
			capaNumeroJCBox.addActionListener(new ActionListener(){    		
				public void actionPerformed(ActionEvent arg0) {
					atributosNumeroJCBox.removeAllItems();
					relacionNombreViaJCBox.removeAllItems();
					atributosNumeroJCBox.addItem("");
					relacionNombreViaJCBox.addItem("");
					
					
					if(!((String)capaNumeroJCBox.getSelectedItem()).equals("")){
						ArrayList lstAtributos = obtenerAtributosCapa((String)capaNumeroJCBox.getSelectedItem());
						
						for (int i=0; i< lstAtributos.size(); i++){
							atributosNumeroJCBox.addItem((String)lstAtributos.get(i));
							relacionNombreViaJCBox.addItem((String)lstAtributos.get(i));
							
						}
						atributosNumeroJCBox.setEnabled(true);
						relacionNombreViaJCBox.setEnabled(true);
					}
					else{
						atributosNumeroJCBox.setEnabled(false);
						relacionNombreViaJCBox.setEnabled(false);
					}
				}
    			
	        });

		}
	
		return capaNumeroJCBox;
	}

	public void setCapaNumeroJCBox(JComboBox capaNumeroJCBox) {
		this.capaNumeroJCBox = capaNumeroJCBox;
	}
	
	public JComboBox getAtributosNumeroJCBox() {
		
		if(atributosNumeroJCBox == null){
			atributosNumeroJCBox = new JComboBox();
			atributosNumeroJCBox.removeAllItems();

			if(!((String)capaNumeroJCBox.getSelectedItem()).equals("")){
				ArrayList lstAtributos = obtenerAtributosCapa((String)capaNumeroJCBox.getSelectedItem());
				
				for (int i=0; i< lstAtributos.size(); i++){
					atributosNumeroJCBox.addItem((String)lstAtributos.get(i));
					
				}
			}
		}
		
		return atributosNumeroJCBox;
	}

	private ArrayList obtenerAtributosCapa(String layerName){
		
		ArrayList lstAtributos = new ArrayList();
		GeopistaSchema schema = (GeopistaSchema) (context.getLayerManager().getLayer(layerName).getFeatureCollectionWrapper().getFeatureSchema());
		Iterator iterAtributes = schema.getAttributes().iterator();
		while(iterAtributes.hasNext()){
			Attribute atributo = (Attribute)iterAtributes.next();
			lstAtributos.add(atributo.getName());
		}
		
		return lstAtributos;
	}
	
	
	public void setAtributosNumeroJCBox(JComboBox atributosNumeroJCBox) {
		this.atributosNumeroJCBox = atributosNumeroJCBox;
	}

	public JComboBox getRelacionNumeroJCBox() {
		
		if(relacionNumeroJCBox == null){
			relacionNumeroJCBox = new JComboBox();

			if(!((String)capaNombreViaJCBox.getSelectedItem()).equals("")){
				ArrayList lstAtributos = obtenerAtributosCapa((String)capaNombreViaJCBox.getSelectedItem());
				
				for (int i=0; i< lstAtributos.size(); i++){
					relacionNumeroJCBox.addItem((String)lstAtributos.get(i));
					
				}
			}
		}
		return relacionNumeroJCBox;
	}

	public void setRelacionNumeroJCBox(JComboBox relacionNumeroJCBox) {
		this.relacionNumeroJCBox = relacionNumeroJCBox;
	}

	public JComboBox getRelacionNombreViaJCBox() {
		if(relacionNombreViaJCBox == null){
			relacionNombreViaJCBox = new JComboBox();
			
			if(!((String)capaNumeroJCBox.getSelectedItem()).equals("")){
				ArrayList lstAtributos = obtenerAtributosCapa((String)capaNumeroJCBox.getSelectedItem());
				
				for (int i=0; i< lstAtributos.size(); i++){
					relacionNombreViaJCBox.addItem((String)lstAtributos.get(i));
					
				}
			}
		}
		return relacionNombreViaJCBox;
	}

	public void setRelacionNombreViaJCBox(JComboBox relacionNombreViaJCBox) {
		this.relacionNombreViaJCBox = relacionNombreViaJCBox;
	}
	 
	public boolean wasOKPressed() {
	      return okPressed;
	}

	public void setOKPressed(boolean okPressed) {
	     this.okPressed = okPressed;
	 }
		
	public JButton getBtnCancelar() {
		
		if(btnCancelar == null){
			btnCancelar= new JButton();
			btnCancelar.setText(I18N.get("BuscadorCallejero", "buscadorCallejero.popup.btnCancelar"));
		    btnCancelar.addActionListener(new ActionListener()
		      {
		        public void actionPerformed(ActionEvent e)
		        {
		          btnCancelar_actionPerformed(e);
		        }
		      });
		}
		return btnCancelar;
	}

	public void setBtnCancelar(JButton btnCancelar) {
		this.btnCancelar = btnCancelar;
	}

	public JButton getBtnAceptar() {
		
		if(btnAceptar == null){
			btnAceptar = new JButton();
		    btnAceptar.setText(I18N.get("BuscadorCallejero", "buscadorCallejero.popup.btnAceptar"));
		    btnAceptar.addActionListener(new ActionListener()
		      {
		        public void actionPerformed(ActionEvent e)
		        {
		          btnAceptar_actionPerformed(e);
		        }
		      });
		}
		return btnAceptar;
	}
	
	public void setBtnAceptar(JButton btnAceptar) {
		this.btnAceptar = btnAceptar;
	}
	
	private void btnCancelar_actionPerformed(ActionEvent e)
	  {
	    this.setVisible(false);
	    JDialog Dialog = (JDialog) SwingUtilities.getWindowAncestor(this );
	    Dialog.setVisible(false);
	  }
	
	private void guardarBusqueda(){
		String opcionesAvanzadas = "";
		if(chkOpcionesAvanzadas.isSelected()){
			opcionesAvanzadas = "1";
		}
		else{
			opcionesAvanzadas = "0";
		}
		
		String busquedaLiteralText = "";
		if(chkLiteraltext.isSelected()){
			busquedaLiteralText = "1";
		}
		else{
			busquedaLiteralText = "0";
		}
		
		if(appContext.getString(UserPreferenceConstants.BUSCADOR_CALLEJERO_AVANZADO) == null){
			UserPreferenceStore.getUserPreference(UserPreferenceConstants.BUSCADOR_CALLEJERO_AVANZADO,opcionesAvanzadas, true);
		}
		else{
			UserPreferenceStore.setUserPreference(UserPreferenceConstants.BUSCADOR_CALLEJERO_AVANZADO, opcionesAvanzadas);
		}
		if(appContext.getString(UserPreferenceConstants.BUSCADOR_CALLEJERO_LITERAL) == null){
			UserPreferenceStore.getUserPreference(UserPreferenceConstants.BUSCADOR_CALLEJERO_LITERAL,busquedaLiteralText, true);
		}
		else{
			UserPreferenceStore.setUserPreference(UserPreferenceConstants.BUSCADOR_CALLEJERO_LITERAL,busquedaLiteralText);
		}
		
		if(appContext.getString(UserPreferenceConstants.BUSCADOR_CALLEJERO_NOMBREVIA) == null){
			UserPreferenceStore.getUserPreference(UserPreferenceConstants.BUSCADOR_CALLEJERO_NOMBREVIA,nombreviaTextField.getText(), true);
		}
		else{
			UserPreferenceStore.setUserPreference(UserPreferenceConstants.BUSCADOR_CALLEJERO_NOMBREVIA,nombreviaTextField.getText());
		}
		
		if(appContext.getString(UserPreferenceConstants.BUSCADOR_CALLEJERO_NUMERO) == null){
			UserPreferenceStore.getUserPreference(UserPreferenceConstants.BUSCADOR_CALLEJERO_NUMERO,numeroTextField.getText(), true);
		}
		else{
			UserPreferenceStore.setUserPreference(UserPreferenceConstants.BUSCADOR_CALLEJERO_NUMERO,numeroTextField.getText());
		}
		
		if(appContext.getString(UserPreferenceConstants.BUSCADOR_CALLEJERO_CAPA_NOMBREVIA) == null){
			UserPreferenceStore.getUserPreference(UserPreferenceConstants.BUSCADOR_CALLEJERO_CAPA_NOMBREVIA,(String)capaNombreViaJCBox.getSelectedItem(), true);
		}
		else{
			UserPreferenceStore.setUserPreference(UserPreferenceConstants.BUSCADOR_CALLEJERO_CAPA_NOMBREVIA,(String)capaNombreViaJCBox.getSelectedItem());
		}
		
		if(appContext.getString(UserPreferenceConstants.BUSCADOR_CALLEJERO_CAPA_NUMERO) == null){
			UserPreferenceStore.getUserPreference(UserPreferenceConstants.BUSCADOR_CALLEJERO_CAPA_NUMERO,(String)capaNumeroJCBox.getSelectedItem(), true);
		}
		else{
			UserPreferenceStore.setUserPreference(UserPreferenceConstants.BUSCADOR_CALLEJERO_CAPA_NUMERO, (String)capaNumeroJCBox.getSelectedItem());
		}
		
		if(appContext.getString(UserPreferenceConstants.BUSCADOR_CALLEJERO_ATRIBUTO_NOMBREVIA) == null){
			UserPreferenceStore.getUserPreference(UserPreferenceConstants.BUSCADOR_CALLEJERO_ATRIBUTO_NOMBREVIA,(String)atributosNombreViaJCBox.getSelectedItem(), true);
		}
		else{
			UserPreferenceStore.setUserPreference(UserPreferenceConstants.BUSCADOR_CALLEJERO_ATRIBUTO_NOMBREVIA,(String)atributosNombreViaJCBox.getSelectedItem());
		}
		
		if(appContext.getString(UserPreferenceConstants.BUSCADOR_CALLEJERO_ATRIBUTO_NUMERO) == null){
			UserPreferenceStore.getUserPreference(UserPreferenceConstants.BUSCADOR_CALLEJERO_ATRIBUTO_NUMERO,(String)atributosNumeroJCBox.getSelectedItem(), true);
		}
		else{
			UserPreferenceStore.setUserPreference(UserPreferenceConstants.BUSCADOR_CALLEJERO_ATRIBUTO_NUMERO,(String)atributosNumeroJCBox.getSelectedItem());
		}
		
		if(appContext.getString(UserPreferenceConstants.BUSCADOR_CALLEJERO_RELACION_NUMERO) == null){
			UserPreferenceStore.getUserPreference(UserPreferenceConstants.BUSCADOR_CALLEJERO_RELACION_NUMERO,(String)relacionNumeroJCBox.getSelectedItem(), true);
		}
		else{
			UserPreferenceStore.setUserPreference(UserPreferenceConstants.BUSCADOR_CALLEJERO_RELACION_NUMERO,(String)relacionNumeroJCBox.getSelectedItem());
		}
		
		if(appContext.getString(UserPreferenceConstants.BUSCADOR_CALLEJERO_RELACION_NOMBREVIA) == null){
			UserPreferenceStore.getUserPreference(UserPreferenceConstants.BUSCADOR_CALLEJERO_RELACION_NOMBREVIA,(String)relacionNombreViaJCBox.getSelectedItem(), true);
		}
		else{
			UserPreferenceStore.setUserPreference(UserPreferenceConstants.BUSCADOR_CALLEJERO_RELACION_NOMBREVIA,(String)relacionNombreViaJCBox.getSelectedItem());
		}
		
	
	}
	
	private void cargarBusqueda(){
		
		String opcionesAvanzadas = appContext.getString(UserPreferenceConstants.BUSCADOR_CALLEJERO_AVANZADO);
		String busquedaLiteralText = appContext.getString(UserPreferenceConstants.BUSCADOR_CALLEJERO_LITERAL);
		String nombrevia = appContext.getString(UserPreferenceConstants.BUSCADOR_CALLEJERO_NOMBREVIA);
		String numero = appContext.getString(UserPreferenceConstants.BUSCADOR_CALLEJERO_NUMERO);
		
		String capaNombreVia = appContext.getString(UserPreferenceConstants.BUSCADOR_CALLEJERO_CAPA_NOMBREVIA);
		String capaNumero = appContext.getString(UserPreferenceConstants.BUSCADOR_CALLEJERO_CAPA_NUMERO);
		String atributoNombreVia = appContext.getString(UserPreferenceConstants.BUSCADOR_CALLEJERO_ATRIBUTO_NOMBREVIA);
		String atributoNumero = appContext.getString(UserPreferenceConstants.BUSCADOR_CALLEJERO_ATRIBUTO_NUMERO);
		String relacionNumero = appContext.getString(UserPreferenceConstants.BUSCADOR_CALLEJERO_RELACION_NUMERO);
		String relacionNombrevia = appContext.getString(UserPreferenceConstants.BUSCADOR_CALLEJERO_RELACION_NOMBREVIA);
		

		if(opcionesAvanzadas == null){
			// no esta cargada ninguna busqueda en el registro
			// se hace la búsqueda por la layer ve Vias y Numeros policia
			// si hay una consulta almacenada, siempre debería existir la key BUSCADOR_CALLEJERO_AVANZADO
			// en el registro
			String layerName=appContext.getString("lyrVias");
			String layerName2=appContext.getString("lyrNumerosPolicia");
			
			if(context.getLayerManager().getLayer(layerName) == null ||
					context.getLayerManager().getLayer(layerName2) == null){
				JOptionPane.showMessageDialog(this,I18N.get("BuscadorCallejero", "buscadorCallejero.popup.noCargadaCapas.vias.numeropolicia"));
			}
		}
		else{
			//existe una búsqueda almacenada en el registro
			if(opcionesAvanzadas.equals("0")){
				
				String layerName=appContext.getString("lyrVias");
				String layerName2=appContext.getString("lyrNumerosPolicia");
				
				if(context.getLayerManager().getLayer(layerName) == null ||
						context.getLayerManager().getLayer(layerName2) == null){

					JOptionPane.showMessageDialog(this,I18N.get("BuscadorCallejero", "buscadorCallejero.popup.noCargadaCapas.vias.numeropolicia"));
				}
			}
			else if(opcionesAvanzadas.equals("1")){
				
				if(context.getLayerManager().getLayer(capaNombreVia) == null ||
						context.getLayerManager().getLayer(capaNumero) == null){

					JOptionPane.showMessageDialog(this,I18N.get("BuscadorCallejero", "buscadorCallejero.popup.noCargadaCapas.almacenadas"));
				}
			}
			
		}	
		
		if(nombrevia != null){
			nombreviaTextField.setText(nombrevia);
		}
		
		if(numero != null){
			numeroTextField.setText(numero);
		}
		
		
		if(opcionesAvanzadas == null || opcionesAvanzadas.equals("0") || opcionesAvanzadas.equals("")){
			chkOpcionesAvanzadas.setSelected(false);
			opcionesAvanzadasConfigLabel.setVisible(true);
			panelBuscadorNombreVia.setVisible(false);
			panelBuscadorNumero.setVisible(false);
		}
		else{
			chkOpcionesAvanzadas.setSelected(true);
			opcionesAvanzadasConfigLabel.setVisible(false);
			panelBuscadorNombreVia.setVisible(true);
			panelBuscadorNumero.setVisible(true);
		}
		
		if(busquedaLiteralText == null || busquedaLiteralText.equals("0") || busquedaLiteralText.equals("")){	
			chkLiteraltext.setSelected(false);
		}
		else{
			chkLiteraltext.setSelected(true);
		}
		

		if(context.getLayerManager().getLayer(capaNombreVia) != null){
			capaNombreViaJCBox.setSelectedItem(capaNombreVia);
			atributosNombreViaJCBox.setEnabled(true);
			atributosNombreViaJCBox.setSelectedItem(atributoNombreVia);
			relacionNumeroJCBox.setEnabled(true);
			relacionNumeroJCBox.setSelectedItem(relacionNumero);
		}
		else{
			capaNombreViaJCBox.setSelectedIndex(0);
			atributosNombreViaJCBox.setSelectedIndex(0);
			atributosNombreViaJCBox.setEnabled(false);
			relacionNumeroJCBox.setSelectedIndex(0);
			relacionNumeroJCBox.setEnabled(false);
		}
		
		if(context.getLayerManager().getLayer(capaNumero) != null){
			capaNumeroJCBox.setSelectedItem(capaNumero);
			atributosNumeroJCBox.setSelectedItem(atributoNumero);
			atributosNumeroJCBox.setEnabled(true);
			relacionNombreViaJCBox.setSelectedItem(relacionNombrevia);
			relacionNombreViaJCBox.setEnabled(true);
		}
		else{
			capaNumeroJCBox.setSelectedIndex(0);
			atributosNumeroJCBox.setSelectedIndex(0);
			atributosNumeroJCBox.setEnabled(false);
			relacionNombreViaJCBox.setSelectedIndex(0);
			relacionNombreViaJCBox.setEnabled(false);
		}
		

	}
	
	private void btnAceptar_actionPerformed(ActionEvent e)
	{
		 String layerName = null;
		 
		 ArrayList attributeNames = new ArrayList();
		 ArrayList values = new ArrayList();
		 ArrayList attributeNamesNumPolice = new ArrayList();
		 ArrayList valuesNumPolice = new ArrayList();
		 
		 String value2=null;
		 String snumPolice=null;
	     String snumPoliceAttri=null;
		 boolean tipoBusqueda = false;
	 
		 if(nombreviaTextField.getText().equals("")){
			 // no se ha introducido un valor en el nombre de via
			 JOptionPane.showMessageDialog(this,I18N.get("BuscadorCallejero", "buscadorCallejero.popup.introducirNombreVia")); 
		 }
		 else{
			 // se ha introducido un valor en el nombre de via
			 if(chkLiteraltext.isSelected()){
				 // el campo literal esta seleccionado, por lo tanto la busqueda del nombre de la
				 //calle tiene que coincidir
		          tipoBusqueda=true;
			 }
			 
			 if(chkOpcionesAvanzadas.isSelected()){
				 if(((String)capaNombreViaJCBox.getSelectedItem()).equals("") ||
						 ((String)atributosNombreViaJCBox.getSelectedItem()).equals("") ||
						 ((String)relacionNumeroJCBox.getSelectedItem()).equals("")){
					 
					 JOptionPane.showMessageDialog(this, I18N.get("BuscadorCallejero", "buscadorCallejero.popup.configurarDatosNombreVia"));
				 }
				 else{
					 // se ha seleccionado la configuracion de opciones avanzadas
					 layerName = (String)capaNombreViaJCBox.getSelectedItem();
					
				     attributeNames.add((String)atributosNombreViaJCBox.getSelectedItem());
				     
				     
				     String value1 = (String)this.nombreviaTextField.getText();
					 values.add(value1);
					 
					 this.attributeCollection = GeopistaUtil.searchByAttributes(context.getLayerManager(),
							 layerName, attributeNames, values, value2, null, tipoBusqueda);
				     if (attributeCollection.size()==0)
				     {
				    	 JOptionPane.showMessageDialog(this,I18N.get("BuscadorCallejero", "buscadorCallejero.popup.NoLocation"));
				    	 guardarBusqueda();
				    	 return;
				     }
				     else{
				    	
				    	  String layerName2 = (String)capaNumeroJCBox.getSelectedItem();
				    	  Iterator attributeCollectionIter = attributeCollection.iterator();
				    	  while (attributeCollectionIter.hasNext())
				          {
				    		  GeopistaFeature fviasGeopistaFeature = (GeopistaFeature) attributeCollectionIter.next();
				    		  
				    		  valuesNumPolice.add(fviasGeopistaFeature.getString((String)relacionNumeroJCBox.getSelectedItem()));
				              attributeNamesNumPolice.add((String)relacionNombreViaJCBox.getSelectedItem());
				          }
				    	  
				    	  String value3 = (String)numeroTextField.getText();
				    	  if(value3.equals("")){
				              value3=null;
				          } 
					    	   
				    	  
				    	  if(value3 != null){
				              snumPolice=value3;
				              snumPoliceAttri=(String)atributosNumeroJCBox.getSelectedItem();
				          }
				    	  
				    	  this.attributeCollection.clear();
				          this.attributeCollection = GeopistaUtil.searchByAttributes(context.getLayerManager(),layerName2,
				        		  attributeNamesNumPolice, valuesNumPolice, snumPolice,snumPoliceAttri,tipoBusqueda);
				          if (attributeCollection.size()==0)
				          {
				            if(value3 == null){
				                JOptionPane.showMessageDialog(this,I18N.get("BuscadorCallejero", "buscadorCallejero.popup.numesInexis"));
				                guardarBusqueda();
				                return;
				            }else{
				                JOptionPane.showMessageDialog(this,I18N.get("BuscadorCallejero", "buscadorCallejero.popup.numInexis"));
				                
				            }
				          }
				          
	
				          LayerManager layerManager = context.getLayerManager();
				          Layer localLayer = layerManager.getLayer(layerName2);
				    	  
				          //primero desseleccionamos los objetos ateriormente seleccionados
				          context.getLayerViewPanel().getSelectionManager().getFeatureSelection().unselectItems();
				          context.getLayerViewPanel().getSelectionManager().getFeatureSelection().selectItems(localLayer,attributeCollection);
				          //el el siguiente bloque try...catch es donde realizamos el zoom a la zona seleccionada
				         
				          try{
				              GeopistaEditor zoomAction=new GeopistaEditor();
				              zoomAction.zoom(context.getLayerViewPanel().getSelectionManager().getSelectedItems() ,context.getLayerViewPanel());
				              
				          }catch (Exception NoninvertibleTransformException)
				          {
				              logger.error("[GeopistaNumerosPoliciaPanel]Error realizando zoom",NoninvertibleTransformException);
				              guardarBusqueda();
				              return;
				          }
	
				    	 final ViewAttributesFrame frame = new ViewAttributesFrame(context,this.attributeCollection,layerName2);
						 	
					      // REVISAR: Cast to WorkbenchFrame for the MDI model
					      ((WorkbenchGuiComponent)context.getWorkbenchGuiComponent()).addInternalFrame( frame);
				     }
				 }
			 }
			 else{
				 //no se han configurado las opciones avanzadas
				 String snombreCatastro="nombrecatastro";
			     String stipoVia="tipovianormalizadocatastro";
			     String scodigoViaNumPolice="id_via";
			     String scodigoVia="id";
			     String srotuloNumPolice="rotulo";
			     
				 String value1 = (String)this.nombreviaTextField.getText();
				 values.add(value1);
				 
				 layerName=appContext.getString("lyrVias");
				 
				 if(context.getLayerManager().getLayer(layerName) == null){
					 JOptionPane.showMessageDialog(this,I18N.get("BuscadorCallejero", "buscadorCallejero.popup.noCargadaCapaVias"));
				 }
				 else{
					 		 
					 //recogemos el schema para una capa en concreto para poder buscar los atributos con el nombnre de la columna
				     GeopistaSchema schema = (GeopistaSchema) (context.getLayerManager().getLayer(layerName).getFeatureCollectionWrapper().getFeatureSchema());
				     String snombreCatastroAttri = schema.getAttributeByColumn(snombreCatastro);
				     String stipoViaAttri = schema.getAttributeByColumn(stipoVia);
				     String scodigoViaAttri = schema.getAttributeByColumn(scodigoVia);
				     
				     attributeNames.add(snombreCatastroAttri);
				     
				     
				     this.attributeCollection = GeopistaUtil.searchByAttributes(context.getLayerManager(),layerName,attributeNames,values,value2,stipoViaAttri,tipoBusqueda);
				     if (attributeCollection.size()==0)
				     {
				      	JOptionPane.showMessageDialog(this,I18N.get("BuscadorCallejero", "buscadorCallejero.popup.NoLocation"));
				      	guardarBusqueda();
				      	return;
				     }
				     else{
				    	 layerName=appContext.getString("lyrNumerosPolicia");
				    	 
				    	 if(context.getLayerManager().getLayer(layerName) != null){
					 
							 String value3 = (String)this.numeroTextField.getText();
							 if(value3.equals("")){
						          value3=null;
						      }

					          Iterator attributeCollectionIter = attributeCollection.iterator();
					          GeopistaSchema schemaNumpolice = (GeopistaSchema) (context.getLayerManager().getLayer(layerName).getFeatureCollectionWrapper().getFeatureSchema());
					          String scodigoViaNumPoliceAttri= schemaNumpolice.getAttributeByColumn(scodigoViaNumPolice);
					          String srotuloNumPoliceAttri= schemaNumpolice.getAttributeByColumn(srotuloNumPolice);
					          while (attributeCollectionIter.hasNext())
					          {
					              GeopistaFeature fviasGeopistaFeature = (GeopistaFeature) attributeCollectionIter.next();

					              valuesNumPolice.add(fviasGeopistaFeature.getString(scodigoViaAttri));
					              attributeNamesNumPolice.add(scodigoViaNumPoliceAttri);
			
					          }
					          if(value3 != null){
					              snumPolice=value3;
					              snumPoliceAttri=srotuloNumPoliceAttri;
					          }
				              
					          this.attributeCollection.clear();
					          this.attributeCollection = GeopistaUtil.searchByAttributes(context.getLayerManager(),layerName,attributeNamesNumPolice,valuesNumPolice,snumPolice,snumPoliceAttri,tipoBusqueda);
					          if (attributeCollection.size()==0)
					          {
					            if(value3 == null){
					                JOptionPane.showMessageDialog(this,I18N.get("BuscadorCallejero", "buscadorCallejero.popup.numesInexis"));
					                guardarBusqueda();
					                return;
					            }else{
					                JOptionPane.showMessageDialog(this,I18N.get("BuscadorCallejero", "buscadorCallejero.popup.numInexis"));
					                
					            }
					          }
					          LayerManager layerManager = context.getLayerManager();
					          Layer localLayer = layerManager.getLayer(layerName);
					          //primero desseleccionamos los objetos ateriormente seleccionados
					          context.getLayerViewPanel().getSelectionManager().getFeatureSelection().unselectItems();
					          context.getLayerViewPanel().getSelectionManager().getFeatureSelection().selectItems(localLayer,attributeCollection);
					          //el el siguiente bloque try...catch es donde realizamos el zoom a la zona seleccionada
					          try{
					              GeopistaEditor zoomAction=new GeopistaEditor();
					              zoomAction.zoom(context.getLayerViewPanel().getSelectionManager().getSelectedItems() ,context.getLayerViewPanel());
					              
					          }catch (Exception NoninvertibleTransformException)
					          {
					              logger.error("[GeopistaNumerosPoliciaPanel]Error realizando zoom",NoninvertibleTransformException);
					              guardarBusqueda();
					              return;
					          }
					          
					          final ViewAttributesFrame frame = new ViewAttributesFrame(context,this.attributeCollection,layerName);
							 	
						      // REVISAR: Cast to WorkbenchFrame for the MDI model
						      ((WorkbenchGuiComponent)context.getWorkbenchGuiComponent()).addInternalFrame( frame);
				    	 }
				    	 else{
				    		 JOptionPane.showMessageDialog(this,I18N.get("BuscadorCallejero", "buscadorCallejero.popup.noCargadaCapaNumeroPolicia"));
				    	 }
					 }

				 }
			 }
			 
		     btnCancelar_actionPerformed(null); // cierra el diálogo.
		 }
		 guardarBusqueda();
	 }

 
	 private class ViewAttributesFrame extends JInternalFrame
	 		implements LayerManagerProxy, SelectionManagerProxy,
         LayerNamePanelProxy, TaskFrameProxy, LayerViewPanelProxy {
	     private LayerManager layerManager;
	     private GeopistaOneLayerAttributeTab attributeTab;
	
	     public ViewAttributesFrame(PlugInContext context, Collection attributeCollection, String layerName) {
	         this.layerManager = context.getLayerManager();
	         addInternalFrameListener(new InternalFrameAdapter() {
	                 public void internalFrameClosed(InternalFrameEvent e) {
	                     //Assume that there are no other views on the model [Jon Aquino]
	                     attributeTab.getModel().dispose();
	                 }
	             });
	         setResizable(true);
	         setClosable(true);
	         setMaximizable(true);
	         setIconifiable(true);
	         getContentPane().setLayout(new BorderLayout());
	         //attributeTab = new OneLayerAttributeTab(context.getWorkbenchContext(),
	         //        (TaskComponent) context.getActiveInternalFrame(), this).setLayer(context.getSelectedLayer(
	         //            0));
	
	         attributeTab = new GeopistaOneLayerAttributeTab(context.getWorkbenchContext(),
	                 (TaskComponent) context.getActiveInternalFrame(), this).setLayer(attributeCollection,context.getLayerManager().getLayer(layerName));
	
	         
	         addInternalFrameListener(new InternalFrameAdapter() {
	                 public void internalFrameOpened(InternalFrameEvent e) {
	                     attributeTab.getToolBar().updateEnabledState();
	                 }
	             });
	         getContentPane().add(attributeTab, BorderLayout.CENTER);
	         setSize(500, 300);
	         
	         updateTitle(attributeTab.getLayer());
	         context.getLayerManager().addLayerListener(new LayerListener() {
	                 public void layerChanged(LayerEvent e) {
	                     if (attributeTab.getLayer() != null) {
	                         updateTitle(attributeTab.getLayer());
	                     }
	                 }
	
	                 public void categoryChanged(CategoryEvent e) {
	                 }
	
	                 public void featuresChanged(FeatureEvent e) {
	                 }
	             });
	         Assert.isTrue(!(this instanceof CloneableInternalFrame),
	             "There can be no other views on the InfoModel");
	     }
	
	     public ILayerViewPanel getLayerViewPanel() {
	         return getTaskComponent().getLayerViewPanel();
	     }
	
	     public LayerManager getLayerManager() {
	         return layerManager;
	     }
	
	     private void updateTitle(Layer layer) {
	         setTitle((layer.isEditable() ? "Edit" : "View") + " Attributes: " +
	             layer.getName());
	     }
	
	     public TaskComponent getTaskComponent() {
	         return attributeTab.getTaskFrame();
	     }
	
	     public SelectionManager getSelectionManager() {
	         return attributeTab.getPanel().getSelectionManager();
	     }
	
	     public LayerNamePanel getLayerNamePanel() {
	         return attributeTab;
	     }
	  
	 }
 
}
