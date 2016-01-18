
OpenLayers.Control.DeleteFeatureLocalgis = OpenLayers.Class(OpenLayers.Control, {
	
	type: OpenLayers.Control.TYPE_TOOL,
	
	initialize: function(layer, options) {
		OpenLayers.Control.prototype.initialize.apply(this, [options]);
		this.layer = layer;
		this.handler = new OpenLayers.Handler.Feature(
			this, layer, {click: this.clickFeature}
		);
	},
	
	clickFeature: function(feature) {	
		if(feature.fid == undefined) {
			this.layer.destroyFeatures([feature]);
		} else {		
			var idFeature = feature.attributes[this.layer.pkProperty];
			if(idFeature==undefined) idFeature = ""; 
			if(confirm("¿Esta Seguro de que desea borrar el elemento " + idFeature + "?")){
				feature.state = OpenLayers.State.DELETE;
				//feature.layer.protocol.commit([feature]);				
				feature.layer.saveAllChanges();	
			}
		}
	},
	setMap: function(map) {
		this.handler.setMap(map);
		OpenLayers.Control.prototype.setMap.apply(this, arguments);
	},
	
	CLASS_NAME: "OpenLayers.Control.DeleteFeatureLocalgis"
});