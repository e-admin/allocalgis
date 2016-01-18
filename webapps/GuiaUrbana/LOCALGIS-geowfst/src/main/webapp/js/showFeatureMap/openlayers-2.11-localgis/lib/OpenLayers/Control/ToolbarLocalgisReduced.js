/* Copyright (c) 2006 MetaCarta, Inc., published under the BSD license.
 * See http://svn.openlayers.org/trunk/openlayers/release-license.txt 
 * for the full text of the license. */

/**
 * @class
 * 
 * @requires OpenLayers/Control/Panel.js
 * @requires OpenLayers/Control/Navigation.js
 * @requires OpenLayers/Control/ZoomBox.js
 */
OpenLayers.Control.ToolbarLocalgisReduced  = 
  OpenLayers.Class(OpenLayers.Control.Panel, {

    zoomIn: null,
    zoomOut: null,
    zoomBox: null,
    zoomToEntidad: null,
    featureInfo: null,
    navigation: null,
    
    /**
     * Add our two mousedefaults controls.
     */
    initialize: function(options, callbackGetFeatureInfo) {
        OpenLayers.Control.Panel.prototype.initialize.apply(this, arguments);
        this.zoomIn = new OpenLayers.Control.ZoomInLocalgis();
        this.zoomOut = new OpenLayers.Control.ZoomOutLocalgis();
        this.zoomBox = new OpenLayers.Control.ZoomBoxLocalgis();
        this.zoomToEntidad = new OpenLayers.Control.ZoomToEntidadLocalgis();
        this.featureInfo = new OpenLayers.Control.GetFeatureInfoLocalgis(callbackGetFeatureInfo);
        this.navigation = new OpenLayers.Control.NavigationLocalgis();

        this.addControls([
            this.zoomIn,
            this.zoomOut,
            this.zoomBox,
            this.zoomToEntidad,
            this.featureInfo,
            this.navigation
        ]);
        
    },

    /**
     * calls the default draw, and then activates mouse defaults.
     */
    draw: function() {
        var div = OpenLayers.Control.Panel.prototype.draw.apply(this, arguments);
        this.activateControl(this.navigation);
        this.navigation.wheelHandler.activate();
        this.zoomIn.panel_div.title = "Acercar";
        this.zoomOut.panel_div.title = "Alejar";
        this.zoomBox.panel_div.title = "Zoom a recuadro";
        this.zoomToEntidad.panel_div.title = "Zoom a entidad";
        this.featureInfo.panel_div.title = "Obtener información";
        this.navigation.panel_div.title = "Mover";
        return div;               
    },                            
                                  
    CLASS_NAME: "OpenLayers.Control.ToolbarLocalgisReduced"
});                               
                                  