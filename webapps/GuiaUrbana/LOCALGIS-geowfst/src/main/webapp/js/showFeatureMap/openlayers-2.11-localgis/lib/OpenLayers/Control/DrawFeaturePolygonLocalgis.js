
OpenLayers.Control.DrawFeaturePolygonLocalgis = OpenLayers.Class(OpenLayers.Control.DrawFeature, {
    
	type: OpenLayers.Control.TYPE_TOOL,

	initialize: function(layer,options){
		OpenLayers.Control.DrawFeature.prototype.initialize.apply(this, [layer,OpenLayers.Handler.Polygon,arguments]);
	},
	
	drawFeature: function(geometry) {
	    var feature = new OpenLayers.Feature.Vector(geometry);
	    this.layer.addFeatures([feature]);
	    this.featureAdded(feature);
	},	

    CLASS_NAME: "OpenLayers.Control.DrawFeaturePolygonLocalgis"
});
