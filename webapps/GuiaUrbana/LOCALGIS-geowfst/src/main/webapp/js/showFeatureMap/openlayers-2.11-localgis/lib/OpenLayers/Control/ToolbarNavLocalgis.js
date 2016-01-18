
OpenLayers.Control.ToolbarNavLocalgis  = 
  OpenLayers.Class(OpenLayers.Control.PanelLocalgis, {
	
    zoomIn: null,
    zoomOut: null,
    zoomBox: null,
    zoomToEntidad: null,
    zoomToMaxExtent: null,
    navigation: null,
	feature_toolbarName: null,
    
    /**
     * Add our two mousedefaults controls.
     */
    initialize: function(options) {
        OpenLayers.Control.PanelLocalgis.prototype.initialize.apply(this, arguments);
        this.zoomIn = new OpenLayers.Control.ZoomInLocalgis();
        this.zoomOut = new OpenLayers.Control.ZoomOutLocalgis();
        this.zoomBox = new OpenLayers.Control.ZoomBoxLocalgis();
        this.zoomToEntidad = new OpenLayers.Control.ZoomToEntidadLocalgis();
        this.zoomToMaxExtent = new OpenLayers.Control.ZoomToMaxExtentLocalgis();
        this.navigation = new OpenLayers.Control.NavigationLocalgis();
       
    	this.addControls([
            this.zoomIn,
            this.zoomOut,
            this.zoomBox,
            this.zoomToMaxExtent,
            this.zoomToEntidad,
            this.navigation
        ]);
    },

    /**
     * calls the default draw, and then activates mouse defaults.
     */
    draw: function() {
        var div = OpenLayers.Control.PanelLocalgis.prototype.draw.apply(this, arguments);
        this.zoomIn.panel_div.title = "Acercar";
        this.zoomOut.panel_div.title = "Alejar";
        this.zoomBox.panel_div.title = "Zoom a recuadro";
        this.zoomToEntidad.panel_div.title = "Zoom a entidad";
        this.zoomToMaxExtent.panel_div.title = "Zoom a España";
        this.navigation.panel_div.title = "Mover";
        
        this.activateControl(this.navigation);
        
        return div;               
    },    
    
    deactivateAll: function(){
    	this.deactivate();
    	this.activate();
    },
    
    activateControl: function(control){
    	 if(false && control.type=="3"){
    		this.map.deactivateControlsOf("OpenLayers.Control." + feature_toolbarName);
    	 }
    	 return OpenLayers.Control.PanelLocalgis.prototype.activateControl.apply(this, arguments);
    },
    
    CLASS_NAME: "OpenLayers.Control.ToolbarNavLocalgis"
});                               
                                  