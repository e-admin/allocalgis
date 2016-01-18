/* Copyright (c) 2006-2007 MetaCarta, Inc., published under the BSD license.
 * See http://svn.openlayers.org/trunk/openlayers/release-license.txt 
 * for the full text of the license. */

/**
 * @requires OpenLayers/Control.js
 *
 * Class: OpenLayers.Control.ZoomToNucleoLocalgis
 * Imlements a very simple button control. Designed to be used with a 
 * <OpenLayers.Control.Panel>.
 * 
 * Inherits from:
 *  - <OpenLayers.Control>
 */
OpenLayers.Control.ZoomToNucleoLocalgis = OpenLayers.Class(OpenLayers.Control, {
    /**
     * Property: type
     * TYPE_BUTTON.
     */
   
    zoomToNucleoLocalgis: function (left, bottom, right,top) {
      
         var extentNucleo = new OpenLayers.Bounds(left,bottom,right, top)
    	 this.map.zoomToExtent(this.map.extentNucleo);
    },
    /*
     * Method: trigger
     * Do the zoom.
     */
    trigger: function() {
        if (this.map && this.map.extentNucleo) {
       
            this.map.zoomToExtent(this.map.extentNucleo);
        }    
    },

    
    CLASS_NAME: "OpenLayers.Control.ZoomToNucleoLocalgis"
});
