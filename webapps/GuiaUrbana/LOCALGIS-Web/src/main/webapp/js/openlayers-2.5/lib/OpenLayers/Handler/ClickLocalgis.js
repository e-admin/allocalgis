/* Copyright (c) 2006 MetaCarta, Inc., published under the BSD license.
 * See http://svn.openlayers.org/trunk/openlayers/release-license.txt 
 * for the full text of the license. */


/**
 * Handler to respond to click mouse event.
 * Callback will be called for click (corresponding
 * to the equivalent mouse events).
 * 
 * @class
 * @requires OpenLayers/Handler.js
 */
OpenLayers.Handler.ClickLocalgis = OpenLayers.Class(OpenLayers.Handler, {
    
    /**
     * @constructor
     *
     * @param {OpenLayers.Control} control
     * @param {Object} callback An object containing a single function to be
     *                          called when the click operation is called.
     * @param {Object} options
     */
    initialize: function(control, callback, options) {
        OpenLayers.Handler.prototype.initialize.apply(this, arguments);
    },

    /**
     * Handle mouse click.
     * 
     * @param {Event} evt
     */
    click: function(evt) {
        this.callback("done", [evt.xy]);
        return false;
    },
    
    mousedown: function (evt) {
        return false;
    },
    
    mouseup: function (evt) {
        return false;
    },
    
    activate: function () {
        if (OpenLayers.Handler.prototype.activate.apply(this, arguments)) {
            return true;
        } else {
            return false;
        }
    },

    deactivate: function () {
        if (OpenLayers.Handler.prototype.deactivate.apply(this, arguments)) {
            return true;
        } else {
            return false;
        }
    },

    /** @final @type String */
    CLASS_NAME: "OpenLayers.Handler.ClickLocalgis"
});
