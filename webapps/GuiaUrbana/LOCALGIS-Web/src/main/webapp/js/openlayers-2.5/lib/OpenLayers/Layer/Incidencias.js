/* Copyright (c) 2006-2007 MetaCarta, Inc., published under the BSD license.
 * See http://svn.openlayers.org/trunk/openlayers/release-license.txt 
 * for the full text of the license. */


/**
 * @requires OpenLayers/Layer.js
 * 
 * Class: OpenLayers.Layer.Incidencias
 * 
 * Inherits from:
 *  - <OpenLayers.Layer> 
 */
OpenLayers.Layer.Incidencias = OpenLayers.Class(OpenLayers.Layer, {
    
    /** 
     * APIProperty: isBaseLayer 
     * {Boolean} Incidencias layer is never a base layer.  
     */
    isBaseLayer: false,
    
    /** 
     * Property: incidencias 
     * Array({<OpenLayers.Incidencia>}) internal incidencia list 
     */
    incidencias: null,


    /** 
     * Property: drawn 
     * {Boolean} internal state of drawing. This is a workaround for the fact
     * that the map does not call moveTo with a zoomChanged when the map is
     * first starting up. This lets us catch the case where we have *never*
     * drawn the layer, and draw it even if the zoom hasn't changed.
     */
    drawn: false,
    
    /**
     * Constructor: OpenLayers.Layer.Incidencias 
     * Create a Incidencias layer.
     *
     * Parameters:
     * name - {String} 
     * options - {Object} Hashtable of extra options to tag onto the layer
     */
    initialize: function(name, options) {
        OpenLayers.Layer.prototype.initialize.apply(this, arguments);
        this.incidencias = [];
    },
    
    /**
     * APIMethod: destroy 
     */
    destroy: function() {
        this.clearIncidencias();
        this.incidencias = null;
        OpenLayers.Layer.prototype.destroy.apply(this, arguments);
    },

    
    /** 
     * Method: moveTo
     *
     * Parameters:
     * bounds - {<OpenLayers.Bounds>} 
     * zoomChanged - {Boolean} 
     * dragging - {Boolean} 
     */
    moveTo:function(bounds, zoomChanged, dragging) {
        OpenLayers.Layer.prototype.moveTo.apply(this, arguments);

        if (zoomChanged || !this.drawn) {
            for(i=0; i < this.incidencias.length; i++) {
                this.drawIncidencia(this.incidencias[i]);
            }
            this.drawn = true;
        }
    },

    /**
     * APIMethod: addIncidencia
     *
     * Parameters:
     * incidencia - {<OpenLayers.Incidencia>} 
     */
    addIncidencia: function(incidencia) {
        this.incidencias.push(incidencia);
        if (this.map && this.map.getExtent()) {
            incidencia.map = this.map;
            this.drawIncidencia(incidencia);
        }
    },

    /**
     * APIMethod: removeIncidencia
     *
     * Parameters:
     * incidencia - {<OpenLayers.Incidencia>} 
     */
    removeIncidencia: function(incidencia) {
        OpenLayers.Util.removeItem(this.incidencias, incidencia);
        if ((incidencia.icon != null) && (incidencia.icon.imageDiv != null) &&
            (incidencia.icon.imageDiv.parentNode == this.div) ) {
            this.div.removeChild(incidencia.icon.imageDiv);    
            incidencia.drawn = false;
        }
    },

    /**
     * Method: clearIncidencias
     * This method removes all incidencias from a layer. The incidencias are not
     * destroyed by this function, but are removed from the list of incidencias.
     */
    clearIncidencias: function() {
        if (this.incidencias != null) {
            while(this.incidencias.length > 0) {
                this.removeIncidencia(this.incidencias[0]);
            }
        }
    },

    /** 
     * Method: drawIncidencia
     * Calculate the pixel location for the incidencia, create it, and 
     *    add it to the layer's div
     *
     * Parameters:
     * incidencia - {<OpenLayers.Incidencia>} 
     */
    drawIncidencia: function(incidencia) {
        var px = this.map.getLayerPxFromLonLat(incidencia.lonlat);
        if (px == null) {
            incidencia.display(false);
        } else {
            var incidenciaImg = incidencia.draw(px);
            if (!incidencia.drawn) {
                this.div.appendChild(incidenciaImg);
                incidencia.drawn = true;
            }
        }
    },
    
    /** 
     * APIMethod: getDataExtent
     * Calculates the max extent which includes all of the incidencias.
     * 
     * Returns:
     * {<OpenLayers.Bounds>}
     */
    getDataExtent: function () {
        var maxExtent = null;
        
        if ( this.incidencias && (this.incidencias.length > 0)) {
            var maxExtent = new OpenLayers.Bounds();
            for(var i=0; i < this.incidencias.length; i++) {
                var incidencia = this.incidencias[i];
                maxExtent.extend(incidencia.lonlat);
            }
        }

        return maxExtent;
    },

    CLASS_NAME: "OpenLayers.Layer.Incidencias"
});
