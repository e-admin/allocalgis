/* Copyright (c) 2006 ASO crea este botón para hacer el zoomin */

/**
 * @class
 *
 * Imlements a very simple button control.
 * @requires OpenLayers/Control.js
 */
OpenLayers.Control.PrintLocalgis  = 
  OpenLayers.Class(OpenLayers.Control, {
    /** @type OpenLayers.Control.TYPE_* */
    type: OpenLayers.Control.TYPE_BUTTON,

    /** @type String */
    urlPrinting: null, 
    
    /** @type Integer */
    height: 600 ,

    /** @type Integer */
    width: 800 ,
    
    /**
    * @constructor
    *
    * @param {String} urlPrint Url para imprimir
    */
    initialize: function(urlPrinting,height,width) {
        OpenLayers.Control.prototype.initialize.apply(this, arguments);
        this.urlPrinting = urlPrinting;
        this.height = height;
        this.width = width;
    },

    trigger: function() {
        var requestPrinting = this.urlPrinting;
        //Añadimos los parametros a la peticion
        if (!OpenLayers.String.contains(requestPrinting, "?")) {
            requestPrinting += "?";
        } else {
            requestPrinting += "&";
        }
        var layers = this.map.layers;
        var center = this.map.getCenter();
        var zoom = this.map.getZoom();
        requestPrinting += "x="+escape(center.lon);
        requestPrinting += "&y="+escape(center.lat);
        requestPrinting += "&zoom="+escape(zoom);
        var layers = this.map.layers;
        var i = 0;        
        for(i = 0;i<layers.length;i++) {
            if (layers[i].visibility && layers[i].inRange && layers[i].disabled != true) {
              if (!(layers[i] instanceof OpenLayers.Layer.Markers) ) {
                requestPrinting += "&layers=" + escape(layers[i].idLayer);    
              }
            }
        }
        if (this.map.markersLayer != null && this.map.markersLayer.visibility) {
            requestPrinting += "&showMarkers=true";
        } else {
            requestPrinting += "&showMarkers=false";
        }
        window.open(requestPrinting, "PrintMap", "height=" + this.height + ",width="+ this.width + ",scrollbars=yes");
        
    },
    /** @final @type String */
    CLASS_NAME: "OpenLayers.Control.PrintLocalgis"
});
