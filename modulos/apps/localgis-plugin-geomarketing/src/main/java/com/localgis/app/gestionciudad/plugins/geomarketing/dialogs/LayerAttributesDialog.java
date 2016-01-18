/**
 * LayerAttributesDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * 
 */
package com.localgis.app.gestionciudad.plugins.geomarketing.dialogs;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.geopista.app.AppContext;
import com.localgis.app.gestionciudad.plugins.geomarketing.utils.GeoMarketingConstat;
import com.localgis.app.gestionciudad.plugins.geomarketing.utils.GeoMarketingUtils;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.LayerNameRenderer;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;

/**
 * @author javieraragon
 *
 */
public class LayerAttributesDialog extends JDialog{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -407859314987678839L;
	private PlugInContext context = null;
	
	private JComboBox layerNamesComboBox = null;
	private JComboBox attributesNamesComboBox = null;
	
	private JPanel rootPanel = null;
	private OKCancelPanel okCancelPanel= null;
	
	
	public LayerAttributesDialog(Frame parentFrame,String title, PlugInContext context){
		super(parentFrame, title, true);
		
		GeoMarketingUtils.inicializarIdiomaGeoMarketing();
		
		this.context = context;
		
		this.initialize();
		this.pack();
		this.setResizable(false);
		this.setLocationRelativeTo(AppContext.getApplicationContext().getMainFrame());
		
		this.setVisible(true);
	}
	
	
	
	private void initialize() {
		this.addComponentListener(new java.awt.event.ComponentAdapter() {
			public void componentShown(ComponentEvent e) {
				this_componentShown(e);
			}
		});
		
		this.setLayout(new GridBagLayout());
		
		this.add(getRootPanel(),
				new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
				GridBagConstraints.HORIZONTAL, 
				new Insets(0, 5, 0, 5), 0, 0));
		
		this.add(getOkCancelPanel(),
				new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
				GridBagConstraints.NONE, 
				new Insets(0, 5, 0, 5), 0, 0));
	}
	
	private JPanel getRootPanel(){
		if (rootPanel == null){
			rootPanel = new JPanel(new GridBagLayout());

			rootPanel.add(new JLabel(I18N.get("geomarketing","localgis.geomarketing.plugins.messages.layerattributesdialog.labels.layer")),
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(10, 5, 0, 5), 0, 0));
			rootPanel.add(getLayerNamesComboBox(),
					new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 30, 20, 30), 0, 2));
			
			rootPanel.add(new JLabel(I18N.get("geomarketing","localgis.geomarketing.plugins.messages.layerattributesdialog.labels.attribute")),
					new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));
			rootPanel.add(getAttributesNamesComboBox(),
					new GridBagConstraints(0, 3, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 30, 20, 30), 0, 2));
		}
		
		return rootPanel;
	}
	



	@SuppressWarnings("unchecked")
	private JComboBox getLayerNamesComboBox(){
		if (layerNamesComboBox == null){
			
			List<Layer> layerList = new ArrayList<Layer>();
			layerList.addAll(context.getLayerManager().getLayers());
			
			layerNamesComboBox = new JComboBox(new Vector<Layer>(layerList));
			
			layerNamesComboBox.setRenderer(new LayerNameRenderer());
			layerNamesComboBox.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					onLayerSelectedDo();
				}
			});
			
			layerNamesComboBox.setSelectedIndex(0);
		}
		return layerNamesComboBox;
	}
	
	
	private JComboBox getAttributesNamesComboBox(){
		if (attributesNamesComboBox == null){
			attributesNamesComboBox = new JComboBox(new Vector<String>(new ArrayList<String>()));
		}
		return attributesNamesComboBox;
	}
	
	private void onLayerSelectedDo() {
		if (getLayerNamesComboBox().getSelectedItem() != null){
			Layer layerSelected = (Layer) getLayerNamesComboBox().getSelectedItem();
			getAttributesNamesComboBox().removeAllItems();
			
			for (int i = 0; i < layerSelected.getFeatureCollectionWrapper().getFeatureSchema().getAttributeCount(); i++){
				getAttributesNamesComboBox().addItem(layerSelected.getFeatureCollectionWrapper().getFeatureSchema().getAttributeName(i));
			}
		}
	}


	
	
	
	
	private OKCancelPanel getOkCancelPanel(){
		if (this.okCancelPanel == null){ 
			this.okCancelPanel = new OKCancelPanel();

			this.okCancelPanel.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					okCancelPanel_actionPerformed(e);
				}
			});
		} 
		return this.okCancelPanel;
	}

	void okCancelPanel_actionPerformed(ActionEvent e) {
		if (!okCancelPanel.wasOKPressed() || isInputValid()) {
			setVisible(false);
			return;
		}
	}
	
	protected boolean isInputValid() {
		boolean selectedlayer = this.getLayerNamesComboBox()!=null && 
		this.getLayerNamesComboBox().getSelectedItem()!=null;
		if (!selectedlayer){
			JOptionPane.showMessageDialog(context.getWorkbenchFrame(), I18N.get("geomarketing","localgis.geomarketing.plugins.messages.layerattributesdialog.notvalidinfo.nolayerselected"));
			return false;
		}
		
		boolean selectedLayerHasAttributes = ((Layer) this.getLayerNamesComboBox().getSelectedItem()).getFeatureCollectionWrapper().getFeatureSchema().getAttributeCount() > 0;
		if (!selectedLayerHasAttributes){
			JOptionPane.showMessageDialog(context.getWorkbenchFrame(), I18N.get("geomarketing","localgis.geomarketing.plugins.messages.layerattributesdialog.layerwithnoattributes"));
			return false;
		}
		
		boolean selectedAttribute = this.getAttributesNamesComboBox()!=null && this.getAttributesNamesComboBox().getSelectedItem()!=null;
		if (!selectedAttribute){
			JOptionPane.showMessageDialog(context.getWorkbenchFrame(), I18N.get("geomarketing","localgis.geomarketing.plugins.messages.layerattributesdialog.notvalidinfo.noattributeselected"));
			return false;
		}
		Layer selectedLayer = context.getLayerManager().getLayer(this.getLayerNamesComboBox().getSelectedItem()+"");
		if (selectedLayer!=null && selectedLayer.getFeatureCollectionWrapper().getFeatures().size() > GeoMarketingConstat.NUM_MAX_FEATURES){
			JOptionPane.showMessageDialog(context.getWorkbenchFrame(), I18N.get("geomarketing","localgis.geomarketing.plugins.messages.error.maxnumfeatures"));
			return false;
		}
		return selectedlayer && selectedLayerHasAttributes && selectedAttribute; 
	}

	public boolean wasOKPressed() {
		return okCancelPanel.wasOKPressed();
	}

	void this_componentShown(ComponentEvent e) {
		okCancelPanel.setOKPressed(false);
	}
	
	public Layer getSelectedLayer(){
		return (Layer) this.getLayerNamesComboBox().getSelectedItem();
	}
	
	public String getSelectedAttribute(){
		return (String) this.getAttributesNamesComboBox().getSelectedItem();
	}
}
