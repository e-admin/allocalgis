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
 * Created on 25-ene-2005 by juacas
 *
 * 
 */
package com.geopista.ui.plugin;

import com.geopista.editor.TaskComponent;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.renderer.Renderer;
import com.vividsolutions.jump.workbench.ui.renderer.Renderer.Factory;

/**
 * TODO Documentación
 * @author juacas
 *
 */
public class InstallLockRendererPlugIn extends InstallRendererPlugIn
{

	/**
	 * @param contentID
	 * @param aboveLayerables
	 */
	public InstallLockRendererPlugIn()
	{
	super(LockedFeaturesRenderer.CONTENT_ID, true);
	}

	/* (non-Javadoc)
	 * @see com.geopista.ui.plugin.InstallRendererPlugIn#createFactory(com.geopista.editor.TaskComponent)
	 */
	protected Factory createFactory(final TaskComponent frame)
	{
	  return new Renderer.Factory() {
      public Renderer create() {
          return new LockedFeaturesRenderer((LayerViewPanel)frame.getLayerViewPanel());
      	}
	  };
	}

}
