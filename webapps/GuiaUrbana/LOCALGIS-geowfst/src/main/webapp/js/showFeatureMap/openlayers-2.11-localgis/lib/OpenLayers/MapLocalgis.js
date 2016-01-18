/* Copyright (c) 2006-2007 MetaCarta, Inc., published under the BSD license.
 * See http://svn.openlayers.org/trunk/openlayers/release-license.txt 
 * for the full text of the license. */


/**
 * @requires OpenLayers/Util.js
 * @requires OpenLayers/Events.js
 * 
 * Class: OpenLayers.MapLocalgis
 * Instances of OpenLayers.Map are interactive maps embedded in a web page.
 * Create a new map with the <OpenLayers.Map> constructor.
 * 
 * On their own maps do not provide much functionality.  To extend a map
 * it's necessary to add controls (<OpenLayers.Control>) and 
 * layers (<OpenLayers.Layer>) to the map. 
 */
OpenLayers.MapLocalgis = OpenLayers.Class(OpenLayers.Map, {
    
    /** Capa activa del mapa
     *
     * @type Array(OpenLayers.Layer)
     */
    activeLayer: null,
    
    markersLayer: null,
    
    markersAuxiliarLayer: null,
    
    incidenciasAuxiliarLayer: null,
    
    extentEntidad: null,
    
    featureSelected: null,
	
	feature_toolbarName: null,
    /**
     * APIMethod: addControlWithIdDiv
     * 
     * Parameters:
     * control - {<OpenLayers.Control>}
     * px - {<OpenLayers.Pixel>}
     * idDiv - {<String>}
     */    
    addControlWithIdDiv: function (control, px, idDiv) {
        this.controls.push(control);
        this.addControlToMapWithIdDiv(control, px, idDiv);
    },

    /**
     * Method: addControlToMap
     * 
     * Parameters:
     * 
     * control - {<OpenLayers.Control>}
     * px - {<OpenLayers.Pixel>}
     * idDiv - {<String>}
     */    
    addControlToMapWithIdDiv: function (control, px, idDiv) {
        // If a control doesn't have a div at this point, it belongs in the
        // viewport.
        control.outsideViewport = (control.div != null);
        control.setMap(this);
        var div = control.draw(px);
        if (div) {
            if(!control.outsideViewport) {
                div.style.zIndex = this.Z_INDEX_BASE['Control'] + 
                				this.controls.length;
                var parentDiv = document.getElementById(idDiv);
                parentDiv.appendChild( div );
            }
        }
       
    },

    /**
     * Método para borrar todos los popups de un mapa
     */
    removeAllPopups: function () {
        var popups = this.popups;
        if (popups != 'undefined' && popups != null) {
            for (var i = 0; i < popups.length; i++) {
                popups[i].destroy();
            }
        }
    },
    
    /**
      * Metodo para crear un popup en el mapa
      */
    addPopupLocalgis: function(name, lonlat) {
        var popup = new OpenLayers.Popup.AnchoredBubbleLocalgis(name, lonlat, new OpenLayers.Size(350, 200), "", null, true);
        popup.setBackgroundColor("#eaebec");
        popup.setOpacity(0.95);
        
        this.addPopup(popup);
        return popup;
    },
    addPopupLocalgisBig: function(name, lonlat) {
        var popup = new OpenLayers.Popup.AnchoredBubbleLocalgis(name, lonlat, new OpenLayers.Size(350, 300), "", null, true);
        popup.setBackgroundColor("#eaebec");
        popup.setOpacity(0.95);
        
        this.addPopup(popup);
        return popup;
    },
    /**
     * Método para añadir una capa de marcas de posicion
     */
    addMarkersLayer: function(layer) {
        this.markersLayer = layer;
        this.addLayer(layer);
    },

    /**
     * Método para añadir una capa de marcas auxiliares de posicion
     */
    addMarkersAuxiliarLayer: function(layer) {
        this.markersAuxiliarLayer = layer;
        this.addLayer(layer);
    },

    /**
     * Método para añadir una capa de marcas de posicion
     */
    addIncidenciasLayer: function(layer) {
        this.markersLayer = layer;
        this.addLayer(layer);
    },

    /**
     * Método para añadir una capa de marcas auxiliares de posicion
     */
    addIncidenciasAuxiliarLayer: function(layer) {
        this.incidenciasAuxiliarLayer = layer;
        this.addLayer(layer);
    },
    
    /**
     * Método para refrescar la capa indicada
     */
    cleanAll: function(layer) { 
    	this.startSelect();
    	//layer.redraw();
    	//this.layers[3].refresh({ force: true, params: { 'key': Math.random()} });
    	layer.redraw();
    }, 
    
    deactivateControlsOf: function(controlId){
    	 for (var i=0; i<this.getControlsByClass(controlId).length; i++){
	    	//alert(this.getControlsByClass(controlId)[i].id);
	    	//if(){
	    		this.getControlsByClass(controlId)[i].deactivateAll();
	    	//}
	     }
    },    
    /*
    startNavigation: function(){
    	this.getFirstControlByClass("OpenLayers.Control.ToolbarNavLocalgis").activateControl(this.getFirstControlByClass("OpenLayers.Control.ToolbarNavLocalgis").navigation);
    },
    */
    startSelect: function(){
    	this.getFirstControlByClass("OpenLayers.Control." + feature_toolbarName).activateControl(this.getFirstControlByClass("OpenLayers.Control." + feature_toolbarName).selectFeature);
    },  
    zoomOutBird: function(zoomOut) {
        this.zoomTo(this.getZoom() - zoomOut);
    },
    
    getFirstControlByClass: function(class_name){
    	var returnControl = null;
        for(var i=0, len=this.controls.length; i<len; i++) {
            var control = this.controls[i];
            if (control["CLASS_NAME"] == class_name) {
                returnControl = control;
                break;
            }
        }
        return returnControl;
    },
    
    setMapCenter: function(x,y){
    	 var lonlat = new OpenLayers.LonLat(x, y);
         map.center = lonlat;
        // map.zoomTo(map.getZoomForResolution(<bean:write name="resolution" scope="request"/>));
         map.setCenter(lonlat, map.getZoom(),false,false);
    },
    
    CLASS_NAME: "OpenLayers.MapLocalgis"
});
