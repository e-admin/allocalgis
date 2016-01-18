/* Copyright (c) 2006-2007 MetaCarta, Inc., published under the BSD license.
 * See http://svn.openlayers.org/trunk/openlayers/release-license.txt 
 * for the full text of the license. */

/**
 * @requires OpenLayers/Control/ZoomBox.js
 * @requires OpenLayers/Control/DragPan.js
 * @requires OpenLayers/Handler/MouseWheel.js
 * 
 * Class: OpenLayers.Control.NavigationLocalgis
 * The navigation control handles map browsing with mouse events (dragging,
 *     double-clicking, and scrolling the wheel).  Create a new navigation 
 *     control with the <OpenLayers.Control.Navigation> control.  
 * 
 *     Note that this control is added to the map by default (if no controls 
 *     array is sent in the options object to the <OpenLayers.Map> 
 *     constructor).
 * 
 * Inherits:
 *  - <OpenLayers.Control>
 */
OpenLayers.Control.NavigationLocalgis = OpenLayers.Class(OpenLayers.Control.Navigation, {

    /**
     * Method: activate
     */
    activate: function() {
        this.dragPan.activate();
        this.wheelHandler.activate();
        this.zoomBox.activate();
        return OpenLayers.Control.prototype.activate.apply(this,arguments);
    },

    CLASS_NAME: "OpenLayers.Control.NavigationLocalgis"
});
