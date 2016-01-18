/*
 * * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
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
 * For more information, contact:
 *
 * 
 * www.geopista.com
 * 
 * Created on 15-jun-2004 by juacas
 *
 * 
 */
package com.geopista.ui.renderer;

import java.awt.Graphics2D;
import java.util.Collection;
import java.util.Map;

import com.vividsolutions.jump.workbench.ui.LayerViewPanel;

/**
 * @author juacas
 *
 * Implementación vacía para evitar la protección de ciertos atributos a este paquete.
 */
public class SimpleFeatureCollectionRenderer
		extends
			com.vividsolutions.jump.workbench.ui.renderer.SimpleFeatureCollectionRenderer
{

	/* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.renderer.SimpleRenderer#paint(java.awt.Graphics2D)
	 */
	protected void paint(Graphics2D g) throws Exception
	{
		// TODO Auto-generated method stub
		super.paint(g);
	}
	/* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.renderer.SimpleFeatureCollectionRenderer#setLayerToFeaturesMap(java.util.Map)
	 */
	protected void setLayerToFeaturesMap(Map layerToFeaturesMap)
	{
		// TODO Auto-generated method stub
		super.setLayerToFeaturesMap(layerToFeaturesMap);
	}
	/* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.renderer.SimpleFeatureCollectionRenderer#setStyles(java.util.Collection)
	 */
	protected void setStyles(Collection styles)
	{
		// TODO Auto-generated method stub
		super.setStyles(styles);
	}
	/**
	 * @param contentID
	 * @param panel
	 */
	public SimpleFeatureCollectionRenderer(Object contentID,
			LayerViewPanel panel) {
		super(contentID, panel);
		// TODO Auto-generated constructor stub
	}

}
