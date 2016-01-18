
OpenLayers.Layer.VectorLocalgis = OpenLayers.Class(OpenLayers.Layer.Vector, {
		
	saveStrategy: null,
    
    pkProperty: "id_feature",
    
    locationProperty: "id_municipio",
    
    styleProperty: "style_type",
    
    numFeaturesLimit: 0,
    
    urlLegend: null,
    
    preName: '',
	
	initialize: function(name, options) {
        OpenLayers.Layer.Vector.prototype.initialize.apply(this, arguments);   
        this.loadAfterSaveEvents();
    },
    
    loadAfterSaveEvents: function(){
    	this.setSaveStrategy();
    	if(this.saveStrategy!=null){
    		this.saveStrategy.events.register('success', null, this.saveSuccess);
    		this.saveStrategy.events.register('fail', null, this.saveFail);
    	}
	},
	
	saveSuccess: function(event) {
		alert('Datos Guardados Correctamente','','');
	},
		
	saveFail: function(event) {
		alert('Error al Guardar los Datos','','');				
	},	
	
	setSaveStrategy: function(){		
		for(var i=0,len=this.strategies.length;i<len;++i){
			strategy=this.strategies[i];
			if(strategy["CLASS_NAME"]=="OpenLayers.Strategy.Save"){
				this.saveStrategy=strategy;
				break;
			}
		}
	},
	
	saveAllChanges: function(){
		if(this.saveStrategy!=null) this.saveStrategy.save();
	},	
	
	getFeatureByAttribute: function(property, id){	        	
		var feature = null;
		for(var i=0; i<this.features.length; i++) {    	        	
		    if(this.features[i].attributes[property] == id) {
		    	feature = this.features[i];
		        break;
		    }
		}
		return feature;
	},	
	
    CLASS_NAME: "OpenLayers.Layer.VectorLocalgis"
});
