
OpenLayers.Control.ModifyFeatureRotateLocalgis = OpenLayers.Class(OpenLayers.Control.ModifyFeature, {

	type: OpenLayers.Control.TYPE_TOOL,

	initialize: function(layer,options){
		OpenLayers.Control.ModifyFeature.prototype.initialize.apply(this, arguments);
		this.mode = OpenLayers.Control.ModifyFeature.ROTATE; 
	},
	
	CLASS_NAME: "OpenLayers.Control.ModifyFeatureRotateLocalgis"
});
