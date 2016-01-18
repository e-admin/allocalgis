/**
 * MapPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.importer.panels;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.JPanel;

import com.geopista.feature.Attribute;
import com.geopista.feature.CodeBookDomain;
import com.geopista.feature.Column;
import com.geopista.feature.GeopistaSchema;
import com.geopista.feature.TreeDomain;
import com.geopista.ui.plugin.importer.FeatureTransformer;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

public class MapPanel extends JPanel implements ActionListener, PropertyChangeListener{

	private static final long serialVersionUID = 1L;
	private MapFieldPanel mapFieldPanel1 = null;
	private FeatureTransformer featureTransformer;  
	private PlugInContext context = null;
	
	/**
	 * This is the default constructor
	 */
	public MapPanel(PlugInContext context) {
		super();
		setContext(context);
		initialize();
	}

	public MapPanel(FeatureTransformer transformer, PlugInContext context) {
		super();
		setContext(context);
		initialize();		
		configure(transformer);
	}
	
	public void setContext (PlugInContext context)
	{
		this.context = context;
	}

	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
		this.setSize(400, 100);
		this.setMinimumSize(new Dimension(400,100));
		this.setLayout(new GridBagLayout());
		//this.setBorder(BorderFactory.createLineBorder(Color.red, 5));  // Generated
		this.add(getMapFieldPanel(), new GridBagConstraints(0, 1, 1, 1, 1, 0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5),0,0));  // Generated
		Attribute att=new Attribute();
		att.setName("testfield");
	}

	/**
	 * This method initializes mapFieldPanel1
	 * @param featureTransformer2
	 *
	 * @return com.geopista.importer.panels.MapFieldPanel
	 */
	public MapFieldPanel getMapFieldPanel() {
		if (mapFieldPanel1 == null) {
			mapFieldPanel1 = new MapFieldPanel(context);
			mapFieldPanel1.setContext(context);

		}
		return mapFieldPanel1;
	}

	@SuppressWarnings("unchecked")
	public void configure(FeatureTransformer transformer) {
		//clean contents
		this.featureTransformer=transformer;
		this.removeAll();
		if (featureTransformer.getTargetSchema() instanceof GeopistaSchema)
		{
			List<Attribute> atts=((GeopistaSchema)featureTransformer.getTargetSchema()).getAttributes();
			int posy=0;
			for (Attribute att : atts) {
				
				if (! 
						(att.getType().equals(AttributeType.GEOMETRY.toString()) || 
								att.getColumn().getName().equalsIgnoreCase("id") || 
								att.getColumn().getName().equalsIgnoreCase("id_municipio") || 
								att.getColumn().getName().equalsIgnoreCase("idmunicipio")
								)
				)						
				{
					
					MapFieldPanel field=new MapFieldPanel(att, context, checkMandatory(att.getColumn()));
					field.addActionListener(this);
					field.addPropertyChangeListener(this);
					//field.setBorder(BorderFactory.createLineBorder(Color.red, 5));
					this.add(field, new GridBagConstraints(0, posy++, 1, 1, 1, 0, GridBagConstraints.NORTHWEST,
							GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0),0,0));

				}
				
			}
		}		
	}
	
	private boolean checkMandatory(Column column) {
		boolean isMandatory = false;
		
		if (column.getDomain()!=null)
		{
			if (column.getDomain() instanceof TreeDomain || 
					column.getDomain() instanceof CodeBookDomain)
				isMandatory = true;
			else if (!column.getDomain().getPattern().startsWith("?"))
				isMandatory = true;				
		}	
		
		return isMandatory;
	}

	/**
	 * Activa Action events
	 */
	ActionEventSupport adapter=new ActionEventSupport();
	   		
		public void actionPerformed(ActionEvent e) {
			adapter.forwardActionEvent(e);			
		}

		public void addActionListener(ActionListener listener) {
			adapter.addActionListener(listener);
		}

		public void removeActionListener(ActionListener listener) {
			adapter.removeActionListener(listener);
		}

		public void propertyChange(PropertyChangeEvent evt) {
			firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());			
		}
}
