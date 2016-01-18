package com.geopista.ui.plugin.routeenginetools.routeutil;

import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.workbench.ui.plugin.AddNewLayerPlugIn;

public class EdgesFeatureCollections {

	public static FeatureCollection getEdgeFeatureCollection(){

		FeatureCollection edgesFeatureCol = AddNewLayerPlugIn
		.createBlankFeatureCollection();

		edgesFeatureCol.getFeatureSchema().addAttribute("edgeId",
				AttributeType.INTEGER);
		edgesFeatureCol.getFeatureSchema().addAttribute("cost",
				AttributeType.DOUBLE);
		edgesFeatureCol.getFeatureSchema().addAttribute("nodeAId",
				AttributeType.INTEGER);
		edgesFeatureCol.getFeatureSchema().addAttribute("nodeBId",
				AttributeType.INTEGER);

		return edgesFeatureCol;
	}

	public static FeatureCollection getDynamicEdgeFeatureCollection(){

		FeatureCollection dynamicEdgesFeatureCol = AddNewLayerPlugIn
		.createBlankFeatureCollection();

		dynamicEdgesFeatureCol.getFeatureSchema().addAttribute("edgeId",
				AttributeType.INTEGER);
		dynamicEdgesFeatureCol.getFeatureSchema().addAttribute("cost",
				AttributeType.DOUBLE);
		dynamicEdgesFeatureCol.getFeatureSchema().addAttribute("nodeAId",
				AttributeType.INTEGER);
		dynamicEdgesFeatureCol.getFeatureSchema().addAttribute("nodeBId",
				AttributeType.INTEGER);

		// if (edge instanceof DynamicEdge){
		dynamicEdgesFeatureCol.getFeatureSchema().addAttribute("impedanceAB",
				AttributeType.DOUBLE);
		dynamicEdgesFeatureCol.getFeatureSchema().addAttribute("impedanceBA",
				AttributeType.DOUBLE);
		dynamicEdgesFeatureCol.getFeatureSchema().addAttribute("costDynamicEdge",
				AttributeType.DOUBLE);

		return dynamicEdgesFeatureCol;
	}

	public static FeatureCollection getLocalGISDynamicEdgeFeatureCollection(){

		FeatureCollection ldDynamicEdgesFeatureCol = AddNewLayerPlugIn
		.createBlankFeatureCollection();

		ldDynamicEdgesFeatureCol.getFeatureSchema().addAttribute("edgeId",
				AttributeType.INTEGER);
		ldDynamicEdgesFeatureCol.getFeatureSchema().addAttribute("cost",
				AttributeType.DOUBLE);
		ldDynamicEdgesFeatureCol.getFeatureSchema().addAttribute("nodeAId",
				AttributeType.INTEGER);
		ldDynamicEdgesFeatureCol.getFeatureSchema().addAttribute("nodeBId",
				AttributeType.INTEGER);

		// if (edge instanceof DynamicEdge){
		ldDynamicEdgesFeatureCol.getFeatureSchema().addAttribute("impedanceAB",
				AttributeType.DOUBLE);
		ldDynamicEdgesFeatureCol.getFeatureSchema().addAttribute("impedanceBA",
				AttributeType.DOUBLE);
		ldDynamicEdgesFeatureCol.getFeatureSchema().addAttribute("costDynamicEdge",
				AttributeType.DOUBLE);


	//	if (edge instanceof ILocalGISEdge){
		ldDynamicEdgesFeatureCol.getFeatureSchema().addAttribute("edgeLength",
				AttributeType.DOUBLE);
		ldDynamicEdgesFeatureCol.getFeatureSchema().addAttribute("idFeature",
				AttributeType.INTEGER);
		ldDynamicEdgesFeatureCol.getFeatureSchema().addAttribute("idLayer",
				AttributeType.INTEGER);


		return ldDynamicEdgesFeatureCol;
	}

	public static FeatureCollection getLocalGISStreetDynamicEdgeFeatureCollection(){

		FeatureCollection streetEdgesFeatureCol = AddNewLayerPlugIn
		.createBlankFeatureCollection();

		streetEdgesFeatureCol.getFeatureSchema().addAttribute("edgeId",
				AttributeType.INTEGER);
		streetEdgesFeatureCol.getFeatureSchema().addAttribute("cost",
				AttributeType.DOUBLE);
		streetEdgesFeatureCol.getFeatureSchema().addAttribute("nodeAId",
				AttributeType.INTEGER);
		streetEdgesFeatureCol.getFeatureSchema().addAttribute("nodeBId",
				AttributeType.INTEGER);

		// if (edge instanceof DynamicEdge){
			streetEdgesFeatureCol.getFeatureSchema().addAttribute("impedanceAB",
					AttributeType.DOUBLE);
			streetEdgesFeatureCol.getFeatureSchema().addAttribute("impedanceBA",
					AttributeType.DOUBLE);
			streetEdgesFeatureCol.getFeatureSchema().addAttribute("costDynamicEdge",
					AttributeType.DOUBLE);


		//	if (edge instanceof ILocalGISEdge){
			streetEdgesFeatureCol.getFeatureSchema().addAttribute("edgeLength",
					AttributeType.DOUBLE);
			streetEdgesFeatureCol.getFeatureSchema().addAttribute("idFeature",
					AttributeType.INTEGER);
			streetEdgesFeatureCol.getFeatureSchema().addAttribute("idLayer",
					AttributeType.INTEGER);

		// if (edge instanceof LocalGISStreetDynamicEdge){
			streetEdgesFeatureCol.getFeatureSchema().addAttribute("trafficRegulation",
					AttributeType.STRING);
			streetEdgesFeatureCol.getFeatureSchema().addAttribute("nominalMaxSpeed",
					AttributeType.DOUBLE);
			streetEdgesFeatureCol.getFeatureSchema().addAttribute("TrafficRegulationPainted",
					AttributeType.INTEGER);


		return streetEdgesFeatureCol;
	}

	public static FeatureCollection getPMRLocalGISStreetDynamicEdgeFeatureCollection(FeatureCollection featureCollection){


		featureCollection.getFeatureSchema().addAttribute("pavementWidth",
				AttributeType.DOUBLE);
		System.out.println("Se ha annadido pavementWidth");
		featureCollection.getFeatureSchema().addAttribute("transversalSlope",
				AttributeType.DOUBLE);
		featureCollection.getFeatureSchema().addAttribute("longitudinalSlope",
				AttributeType.DOUBLE);
		featureCollection.getFeatureSchema().addAttribute("edgeRelatedToId",
				AttributeType.INTEGER);
		featureCollection.getFeatureSchema().addAttribute("edgeType",
				AttributeType.STRING);
		featureCollection.getFeatureSchema().addAttribute("obstacleHeight",
				AttributeType.DOUBLE);
		featureCollection.getFeatureSchema().addAttribute("zebraType",
				AttributeType.STRING);
		featureCollection.getFeatureSchema().addAttribute("calculatedSide",
				AttributeType.STRING);
		return featureCollection;
	}

}
