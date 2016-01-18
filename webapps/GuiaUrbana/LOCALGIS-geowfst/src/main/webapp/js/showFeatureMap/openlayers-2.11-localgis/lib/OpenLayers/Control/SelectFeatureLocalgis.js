
OpenLayers.Control.SelectFeatureLocalgis = OpenLayers.Class(OpenLayers.Control.SelectFeature, {
	
	type: OpenLayers.Control.TYPE_TOOL,
	
	unselectAllFeatures: function(){
		for(var i=0,len=this.layers.length;i<len;i++){
			while (this.layers[i].selectedFeatures.length > 0) {
				this.unselect(this.layers[i].selectedFeatures[0]);
			}
		}		
	},
	
    CLASS_NAME: "OpenLayers.Control.SelectFeatureLocalgis"
});
