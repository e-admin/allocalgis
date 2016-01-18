/* Copyright (c) 2006-2007 MetaCarta, Inc., published under the BSD license.
 * See http://svn.openlayers.org/trunk/openlayers/release-license.txt 
 * for the full text of the license. */


/**
 * @requires OpenLayers/Layer/Grid.js
 * @requires OpenLayers/Tile/Image.js
 * 
 * Class: OpenLayers.Layer.WMS
 * Instances of OpenLayers.Layer.WMS are used to display data from OGC Web
 *     Mapping Services. Create a new WMS layer with the <OpenLayers.Layer.WMS>
 *     constructor.
 * 
 * Inherits from:
 *  - <OpenLayers.Layer.Grid>
 */
OpenLayers.Layer.WMSLocalgis = OpenLayers.Class(OpenLayers.Layer.WMS, {

    /*
     * Url para mostrar la leyenda. Se pasara como parametro en el constructor. 
     * Si no se define ninguna no se muestra nada
     */
    /** @type String */
    urlLegend: null,

    /*
     * Url para hacer una peticion getFeatureInfo. Se pasara como parametro en el constructor. 
     * Si no se define ninguna se utilizara la url que permite mostrar las imagenes
     */
    /** @type String */
    urlGetFeatureInfo: null,

    /*
     * Identificador interno de la capa
     */
    idLayer: null,
    
    /*
     * String a mostrar antes del nombre de la capa (podría ser codigo HTML)
     */
    /** @type String */
    preName: null,

    /*
    * 
    /*
     * Si la capa esta desactivada. Por defecto no lo esta
     */
    /** @type Boolean */
    disabled: null,

    /*
     * Si la capa es externa. Por defecto no lo es
     */
    /** @type String */
    isExternal: false,

    /*
     * Si la capa es del sistema (por ejemplo, ortofoto o provincias). Por defecto no lo es
     */
    /** @type String */
    isSystemLayer: false,
     
    initialize: function(name, url, params, options) {
        OpenLayers.Layer.WMS.prototype.initialize.apply(this, arguments);
        this.disabled = false;
    },
    
    setDisabled: function(disabled) {
        this.disabled = disabled;
    },

    getDisabled: function() {
        return this.disabled;
    },

    CLASS_NAME: "OpenLayers.Layer.WMSLocalgis"
});
