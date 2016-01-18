/**
 * TitledPopupMenuWMSLayerName.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.editor.beans;

import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import com.geopista.app.AppContext;
import com.vividsolutions.jump.workbench.model.Layerable;
import com.vividsolutions.jump.workbench.model.WMSLayer;
import com.vividsolutions.jump.workbench.ui.LayerNamePanel;
import com.vividsolutions.jump.workbench.ui.TitledPopupMenu;

public class TitledPopupMenuWMSLayerName extends TitledPopupMenu{
	private Listener lsnr;
	public TitledPopupMenuWMSLayerName(LayerNamePanel panel){
		lsnr = new Listener(panel);
        addPopupMenuListener(lsnr);
    }

	public void destroy(){
		lsnr = null;
		super.destroy();
	}

	class Listener implements PopupMenuListener {
		private LayerNamePanel panel;

		public Listener(LayerNamePanel panel){
			this.panel = panel;
		}
		public void destroy(){
			panel = null;
		}

		public void popupMenuWillBecomeVisible(PopupMenuEvent e)
        {
            setTitle((panel.selectedNodes(WMSLayer.class).size() != 1) ? ("(" //$NON-NLS-1$
                    + panel.selectedNodes(WMSLayer.class).size() + AppContext
                    .getApplicationContext().getI18nString(
                            "GeopistaWorkbenchFrame.WMS_layer_selected")) //$NON-NLS-1$
                    : ((Layerable) panel.selectedNodes(WMSLayer.class)
                            .iterator().next()).getName());
            System.out.println("Creando Popup Menu Listener");
        }

        public void popupMenuWillBecomeInvisible(PopupMenuEvent e)
        {
        }

        public void popupMenuCanceled(PopupMenuEvent e)
        {
        }
    }
}

