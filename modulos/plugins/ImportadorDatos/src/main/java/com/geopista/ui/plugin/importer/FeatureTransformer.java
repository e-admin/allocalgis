/**
 * FeatureTransformer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.importer;

import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.feature.FeatureSchema;

public class FeatureTransformer {

	private FeatureSchema sourceSchema;
	private FeatureSchema targetSchema;
	private FeatureCollection	sourceFeatureCollection;
	private FeatureCollection targetFeatureCollection;
	
	public void setSourceSchema(FeatureSchema sch) {
		this.sourceSchema=sch;
		
	}

	public FeatureSchema getSourceSchema() {
		return sourceSchema;
	}

	public FeatureSchema getTargetSchema() {
		return targetSchema;
	}

	public void setTargetSchema(FeatureSchema targetSchema) {
		this.targetSchema = targetSchema;
		
	}

	public FeatureCollection getSourceFeatureCollection() {
		return sourceFeatureCollection;
	}

	public void setSourceDataSet(FeatureCollection sourceDataSet) {
		this.sourceFeatureCollection = sourceDataSet;
		setSourceSchema(sourceDataSet.getFeatureSchema());
	}

	public FeatureCollection getTargetFeatureCollection() {
		return targetFeatureCollection;
	}

	public void setTargetFeatureCollection(FeatureCollection targetCollection) {
		this.targetFeatureCollection = targetCollection;
		setTargetSchema(targetFeatureCollection.getFeatureSchema());
	}

}
