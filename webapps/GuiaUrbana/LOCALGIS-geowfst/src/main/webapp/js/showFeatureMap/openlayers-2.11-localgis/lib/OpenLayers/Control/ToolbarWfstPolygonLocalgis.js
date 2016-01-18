
OpenLayers.Control.ToolbarWfstPolygonLocalgis  = 
  OpenLayers.Class(OpenLayers.Control.PanelLocalgis, {
	 
	selectFeature: null,
	drawFeature: null,	    
	modifyFeatureReshape: null,
	modifyFeatureResize: null,
	modifyFeatureRotate: null,
	modifyFeatureDrag: null,
	deleteFeature: null,
		
    /**
     * Add our two mousedefaults controls.
     */
    initialize: function(options,layerWfstModification,layersWfsSelection,layerWfstModificationEvents,layerWfstSelectionEvents) {
        OpenLayers.Control.PanelLocalgis.prototype.initialize.apply(this, arguments);      	
        this.selectFeature = new OpenLayers.Control.SelectFeatureLocalgis(layersWfsSelection, {clickout: true});
        this.drawFeature = new OpenLayers.Control.DrawFeaturePolygonLocalgis(layerWfstModification);
        this.modifyFeatureReshape = new OpenLayers.Control.ModifyFeatureReshapeLocalgis(layerWfstModification);
        this.modifyFeatureResize = new OpenLayers.Control.ModifyFeatureResizeLocalgis(layerWfstModification);
        this.modifyFeatureRotate = new OpenLayers.Control.ModifyFeatureRotateLocalgis(layerWfstModification);
        this.modifyFeatureDrag = new OpenLayers.Control.ModifyFeatureDragLocalgis(layerWfstModification);      
		this.deleteFeature = new OpenLayers.Control.DeleteFeatureLocalgis(layerWfstModification);


	    layerWfstModification.events.on(layerWfstModificationEvents);
        
        for(var i=0,len=layersWfsSelection.length;i<len;i++){
        	layersWfsSelection[i].events.on(layerWfstSelectionEvents);
        }
                	
    	this.addControls([
            this.selectFeature,
            this.drawFeature,
            this.modifyFeatureReshape,
            this.modifyFeatureResize,
            this.modifyFeatureRotate,
            this.modifyFeatureDrag,
    		this.deleteFeature    
        ]);
    	
    },

    /**
     * calls the default draw, and then activates mouse defaults.
     */
    draw: function() {
        var div = OpenLayers.Control.PanelLocalgis.prototype.draw.apply(this, arguments);
        this.selectFeature.panel_div.title = "Seleccionar";
        this.drawFeature.panel_div.title = "Añadir";
        this.modifyFeatureReshape.panel_div.title = "Reestructurar";
        this.modifyFeatureResize.panel_div.title = "Modificar tamaño";
        this.modifyFeatureRotate.panel_div.title = "Rotar";
        this.modifyFeatureDrag.panel_div.title = "Arrastrar";
		this.deleteFeature.panel_div.title = "Eliminar"; 
		
		document.getElementById("toolbarWfstBox").className="visible";
		this.map.addControl(new OpenLayers.Control.Navigation());    	
    	this.initializeHoverFeature(this.selectFeature.layers);
		
		this.activateControl(this.selectFeature);
		   
	    return div;               
    },       
    
    deactivateAll: function(){
    	this.deactivate();
    	this.activate();
    },
    
    initializeHoverFeature: function(layers){
    	for(var i=0,len=layers.length;i<len;i++){
        	var hoverFeature = new OpenLayers.Control.SelectFeature(layers[i], {
    			hover: true,
    			highlightOnly: true,		
    			renderIntent: "temporary"
    		});
        	this.map.addControl(hoverFeature);
        	hoverFeature.activate();
        }
    },
    
    /**
     * Method: onClick
     */
    onClick: function (ctrl, evt) {   
    	if(this.drawFeature.id==ctrl.id && ctrl.layer.numFeaturesLimit>0 && ctrl.layer.numFeaturesLimit==ctrl.layer.features.length){
    		alert("Error se ha superado el límite de elementos añadidos");
    	}
    	else{
    		OpenLayers.Event.stop(evt ? evt : window.event);
    		this.activateControl(ctrl);
    	}
    },	
    
    activateControl: function(control){
    	//if(control.type=="3"){
    		this.map.deactivateControlsOf("OpenLayers.Control.ToolbarNavLocalgis");
    	//}
    	return OpenLayers.Control.PanelLocalgis.prototype.activateControl.apply(this, arguments);
   },   
                                      
   CLASS_NAME: "OpenLayers.Control.ToolbarWfstPolygonLocalgis"
});                               
                                  