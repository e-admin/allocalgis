/**
 * FxccAdditionalLayers.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.intercambio.edicion;

import java.util.HashSet;

import com.vividsolutions.jump.workbench.model.Layer;

public class FxccAdditionalLayers {

	public final static String LAYER_CATEGORY_TOPO_ISSUES = "Errores de Topología";
	
	public final static String ISSUES_LAYER_DANGLE = "Polígonos no cerrados";
	public final static String ISSUES_LAYER_CUT = "Bordes que no forman un polígono";
	public final static String ISSUES_LAYER_INVALID_RING = "Orden de vértices no válido";
	
	public static final String LAYER_CONSTRUCCIONES = "Construcciones";
	public static final String LAYER_CULTIVOS = "Cultivos";
	public static final String LAYER_PARCELAS = "Parcelas";
	public static final String LAYER_PARCELARIOS = "Parcelarios";
	
	public static HashSet<String> ignoredLayers;
	
	static {
		FxccAdditionalLayers.ignoredLayers = new HashSet<String>();
		FxccAdditionalLayers.ignoredLayers.add(LAYER_PARCELAS);
		FxccAdditionalLayers.ignoredLayers.add(LAYER_CULTIVOS);
		FxccAdditionalLayers.ignoredLayers.add(LAYER_CONSTRUCCIONES);
		FxccAdditionalLayers.ignoredLayers.add(LAYER_PARCELARIOS);
		FxccAdditionalLayers.ignoredLayers.add(ISSUES_LAYER_CUT);
		FxccAdditionalLayers.ignoredLayers.add(ISSUES_LAYER_DANGLE);
		FxccAdditionalLayers.ignoredLayers.add(ISSUES_LAYER_INVALID_RING);
	}
	
	public static boolean ignoreLayer(String layerName){
		return FxccAdditionalLayers.ignoredLayers.contains(layerName);
	}
	
	public static boolean ignoreLayer(Layer layer){
		return FxccAdditionalLayers.ignoredLayers.contains(layer.getName());
	}
	
}
