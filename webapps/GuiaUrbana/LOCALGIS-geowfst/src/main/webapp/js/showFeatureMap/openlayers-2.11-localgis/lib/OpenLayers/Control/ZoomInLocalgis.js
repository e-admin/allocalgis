/* Copyright (c) 2006 ASO crea este botón para hacer el zoomin */

/**
 * @class
 *
 * Imlements a very simple button control.
 * @requires OpenLayers/Control.js
 */
OpenLayers.Control.ZoomInLocalgis  = 
  OpenLayers.Class(OpenLayers.Control, {
    /** @type OpenLayers.Control.TYPE_* */
    type: OpenLayers.Control.TYPE_BUTTON,

    trigger: function() {
        if (this.map) {
            //Borramos todos los popus
            this.map.removeAllPopups();
            this.map.zoomIn();
        }
    },
    /** @final @type String */
    CLASS_NAME: "OpenLayers.Control.ZoomInLocalgis"
});
