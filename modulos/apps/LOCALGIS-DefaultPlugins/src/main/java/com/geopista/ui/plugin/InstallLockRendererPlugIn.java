/**
 * InstallLockRendererPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
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
