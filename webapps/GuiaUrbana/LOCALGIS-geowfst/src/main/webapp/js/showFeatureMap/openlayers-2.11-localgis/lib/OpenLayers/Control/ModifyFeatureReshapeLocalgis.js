
OpenLayers.Control.ModifyFeatureReshapeLocalgis = OpenLayers.Class(OpenLayers.Control.ModifyFeature, {

	type: OpenLayers.Control.TYPE_TOOL,
	
	
	initialize: function(layer,options){
		OpenLayers.Control.ModifyFeature.prototype.initialize.apply(this, arguments);
		this.mode = OpenLayers.Control.ModifyFeature.RESHAPE; 
	},
	
    selectFeature: function(feature) {    	
        this.feature = feature;
        this.modified = false;
        this.resetVertices();
        this.dragControl.activate();
        this.onModificationStart(this.feature);
        
    },
	
	CLASS_NAME: "OpenLayers.Control.ModifyFeatureReshapeLocalgis"
});
