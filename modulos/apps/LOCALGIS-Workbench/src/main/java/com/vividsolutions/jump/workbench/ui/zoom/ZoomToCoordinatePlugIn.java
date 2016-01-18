/**
 * ZoomToCoordinatePlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vividsolutions.jump.workbench.ui.zoom;

import java.awt.Component;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

import com.geopista.app.AppContext;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.geom.EnvelopeUtil;
import com.vividsolutions.jump.util.CoordinateArrays;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.LayerManager;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

public class ZoomToCoordinatePlugIn extends AbstractPlugIn {
	private Coordinate lastCoordinate = new Coordinate(0, 0);
	private  AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	public boolean execute(PlugInContext context) throws Exception {
		reportNothingToUndoYet(context);
		Coordinate coordinate = prompt(context);
		if (coordinate == null) {
			return false;
		}
        lastCoordinate = coordinate;
		context.getLayerViewPanel().getViewport()
				.zoom(toEnvelope(coordinate, (LayerManager)context.getLayerManager()));

		return true;
	}

	private Coordinate prompt(PlugInContext context) {
		while (true) {
			try {
				return toCoordinate(JOptionPane.showInputDialog((Component) context
						.getWorkbenchGuiComponent(),
						aplicacion.getI18nString("plugin.coordenadas.introducir"), lastCoordinate.x + ", "
								+ lastCoordinate.y));
			} catch (Exception e) {
				JOptionPane.showMessageDialog((Component) context.getWorkbenchGuiComponent(), e
						.getMessage(),
						context.getWorkbenchGuiComponent().getTitle(),
						JOptionPane.ERROR_MESSAGE);
			}
		}

	}

	private Envelope toEnvelope(Coordinate coordinate, LayerManager layerManager) {
		int segments = 0;
		int segmentSum = 0;
		outer : for (Iterator i = layerManager.iterator(); i.hasNext(); ) {
			Layer layer = (Layer) i.next();
			for (Iterator j = layer.getFeatureCollectionWrapper().iterator(); j
					.hasNext(); ) {
				Feature feature = (Feature) j.next();
                Collection coordinateArrays = CoordinateArrays.toCoordinateArrays(feature.getGeometry(), false);
                for (Iterator k = coordinateArrays.iterator(); k.hasNext(); ) {
                	Coordinate[] coordinates = (Coordinate[]) k.next();
                    for (int a = 1; a < coordinates.length; a++) {
                        segments++;
                    	segmentSum += coordinates[a].distance(coordinates[a-1]);
                        if (segments > 100) { break outer; }
                    }
                }
			}
		}
		Envelope envelope = new Envelope(coordinate);
        //Choose a reasonable magnification [Jon Aquino 10/22/2003]
		if (segmentSum > 0) {
			envelope = EnvelopeUtil.expand(envelope,
					segmentSum / (double) segments);
		} else {
			envelope = EnvelopeUtil.expand(envelope, 50);
		}
		return envelope;
	}

	private Coordinate toCoordinate(String s) throws Exception {
		if (s == null) {
			return null;
		}
		if (s.trim().length() == 0) {
			return null;
		}
		s = StringUtil.replaceAll(s, ",", " ");
		StringTokenizer tokenizer = new StringTokenizer(s);
		String x = tokenizer.nextToken();
		if (!StringUtil.isNumber(x)) {
			throw new Exception("Not a number: " + x);
		}
		String y = tokenizer.nextToken();
		if (!StringUtil.isNumber(y)) {
			throw new Exception("Not a number: " + y);
		}
		return new Coordinate(Double.parseDouble(x), Double.parseDouble(y));
	}

	public MultiEnableCheck createEnableCheck(
			final WorkbenchContext workbenchContext) {
		EnableCheckFactory checkFactory = new EnableCheckFactory(
				workbenchContext);

		return new MultiEnableCheck().add(checkFactory
				.createWindowWithLayerViewPanelMustBeActiveCheck());
	}
}