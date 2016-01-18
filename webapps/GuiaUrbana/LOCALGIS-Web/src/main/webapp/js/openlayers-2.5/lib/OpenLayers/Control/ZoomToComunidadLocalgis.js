/* Copyright (c) 2006-2007 MetaCarta, Inc., published under the BSD license.
 * See http://svn.openlayers.org/trunk/openlayers/release-license.txt 
 * for the full text of the license. */

/**
 * @requires OpenLayers/Control.js
 *
 * Class: OpenLayers.Control.ZoomToComunidadLocalgis
 * Imlements a very simple button control. Designed to be used with a 
 * <OpenLayers.Control.Panel>.
 * 
 * Inherits from:
 *  - <OpenLayers.Control>
 */
OpenLayers.Control.ZoomToComunidadLocalgis = OpenLayers.Class(OpenLayers.Control, {
    /**
     * Property: type
     * TYPE_BUTTON.
     */
    type: OpenLayers.Control.TYPE_BUTTON,
    
    /*
     * Method: trigger
     * Do the zoom.
     */
    trigger: function() {
        if (this.map && this.map.extentEntidad) {
            //Borramos todos los popus
            this.map.removeAllPopups();
            this.map.zoomToExtent(this.map.extentComunidad);
        }    
    },

    CLASS_NAME: "OpenLayers.Control.ZoomToComunidadLocalgis"
});
