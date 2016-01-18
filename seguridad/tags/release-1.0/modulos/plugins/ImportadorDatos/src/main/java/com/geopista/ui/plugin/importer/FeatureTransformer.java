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
