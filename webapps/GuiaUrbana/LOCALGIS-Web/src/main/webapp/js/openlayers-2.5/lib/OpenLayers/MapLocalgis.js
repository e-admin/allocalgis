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

    extentComunidad: null,

    boxLayer: null,

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
    
    destroyPopup: function (popupName) {
        var popups = this.popups;
        if (popups != 'undefined' && popups != null) {
            for (var i = 0; i < popups.length; i++) {
            	if (popups[i].id==popupName){
            		popups[i].destroy();
            	}
            }
        }        
    },
    
    /**
      * Metodo para crear un popup en el mapa
      */
    addPopupLocalgis: function(name, lonlat) {
        var popup = new OpenLayers.Popup.AnchoredBubbleLocalgis(name, lonlat, new OpenLayers.Size(350, 240), "", null, true);
        popup.setBackgroundColor("#eaebec");
        popup.setOpacity(0.95);
        
        this.addPopup(popup);
        return popup;
    },
    addPopupLocalgisSize: function(name, lonlat, width, height) {
        var popup = new OpenLayers.Popup.AnchoredBubbleLocalgis(name, lonlat, new OpenLayers.Size(width, height), "", null, true);
        popup.setBackgroundColor("#eaebec");
        popup.setOpacity(0.95);
        
        this.addPopup(popup);
        return popup;
    },
    addPopupLocalgisReport: function(name, lonlat) {
        var popup = new OpenLayers.Popup.AnchoredBubbleLocalgis(name, lonlat, new OpenLayers.Size(450, 240), "", null, true);
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
     * Método para añadir una capa de marcas auxiliares de posicion
     */
    addBoxLayer: function(layer) {
        this.boxLayer = layer;
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
    
    CLASS_NAME: "OpenLayers.MapLocalgis"
});
