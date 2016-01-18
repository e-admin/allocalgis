/**
 * LoadAutoCADMapRenderer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 */
package com.geopista.ui.plugin.loadautocadmap;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import com.geopista.app.AppContext;
import com.geopista.model.GeopistaMap;

public class LoadAutoCADMapRenderer extends JPanel implements ListCellRenderer
{
    JLabel lblThumb = new JLabel();
    
    JLabel lblDescripcion = new JLabel();
    
    JLabel lblMunicipio = new JLabel();

    private static AppContext aplicacion = (AppContext) AppContext
            .getApplicationContext();

    /**
     * This method initializes jPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel()
    {
    	if (jPanel == null)
    	{

    		jPanel = new JPanel();
    		jPanel.setLayout(new GridBagLayout());
    		jPanel.setOpaque(false);

    		jPanel.add(lblDescripcion,
    				new GridBagConstraints(0,0,1,1, 1.0, 1.0,GridBagConstraints.WEST,
    						GridBagConstraints.NONE, new Insets(5,0,0,0),0,0));

    		jPanel.add(lblMunicipio,
    				new GridBagConstraints(0,1,1,1, 1.0, 1.0,GridBagConstraints.WEST,
    						GridBagConstraints.NONE, new Insets(5,0,0,0),0,0));


    	}
    	return jPanel;
    }

    /**
     * This is the default constructor
     */
    public LoadAutoCADMapRenderer()
    {
    	this.setLayout(new GridBagLayout());
    }

    private String filePath = null;

    private JPanel jPanel = null;

    public String getFilePath()
    {
        return this.filePath;
    }

    public void setFilePath(String filePath)
    {
        this.filePath = filePath;
    }

    public Component getListCellRendererComponent(JList list, Object value, int index,
            boolean isSelected, boolean cellHasFocus)
    {

    	GeopistaMap mapGeopista = (GeopistaMap) value;

        setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        lblThumb.setIcon(new ImageIcon(mapGeopista.getThumbnail()));

        // CREAMOS LAS ETIQUETAS
        // THUMBNAIL
        lblThumb.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        lblThumb.setOpaque(false);
        lblThumb.setPreferredSize(new java.awt.Dimension(100,80));
        
        lblDescripcion.setText(aplicacion.getI18nString("Mapa.Titulo") + " "
                + mapGeopista.getDescription());
        lblDescripcion.setOpaque(false);

        lblMunicipio.setText("Identificador de municipio:"+" " + mapGeopista.getIdMunicipio());
               
        this.add(lblThumb, 
        		new GridBagConstraints(0,0,1,1, 0.1, 0.1,GridBagConstraints.WEST,
						GridBagConstraints.NONE, new Insets(5,5,5,5),0,0));
        		        
        this.add(getJPanel(),
				new GridBagConstraints(1,0,1,1, 1.0, 1.0,GridBagConstraints.CENTER,
						GridBagConstraints.BOTH, new Insets(5,5,5,5),0,0));

        this.setBackground(isSelected ? Color.lightGray : Color.white);
        this.setForeground(isSelected ? Color.white : Color.black);
        return this;
    }
} 
