/* Copyright (c) 2006 MetaCarta, Inc., published under a modified BSD license.
 * See http://svn.openlayers.org/trunk/openlayers/repository-license.txt 
 * for the full text of the license. */


/**
 * Move a feature with a drag.
 * 
 * @class
 * @requires OpenLayers/Control.js
 * @requires OpenLayers/Handler/Drag.js
 * @requires OpenLayers/Handler/Feature.js
 */
OpenLayers.Control.DragMarker = OpenLayers.Class.create();
OpenLayers.Control.DragMarker.prototype = 
  OpenLayers.Class.inherit(OpenLayers.Control.DragFeature, {

    /**
     * @param {OpenLayers.Layer.Vector} layer
     * @param {Object} options
     */
    initialize: function(layer, options) {
	 
        OpenLayers.Control.prototype.initialize.apply(this, [options]);
        this.layer = layer;
        this.dragCallbacks = OpenLayers.Util.extend({down: this.downFeature,
                                                     move: this.moveFeature,
                                                     up: this.upFeature,
                                                     out: this.cancel,
                                                     done: this.doneDragging
                                                    }, this.dragCallbacks);
        this.dragHandler = new OpenLayers.Handler.Drag(this, this.dragCallbacks);
        this.featureCallbacks = OpenLayers.Util.extend({over: this.overFeature,
                                                        out: this.outFeature
                                                       
                                                       }, this.featureCallbacks);
        var handlerOptions = {geometryTypes: this.geometryTypes};
        this.featureHandler = new OpenLayers.Handler.Marker(this, this.layer,
                                                        this.featureCallbacks,
                                                        handlerOptions);
                                                     
    },
    
    /**
     * Called when the drag handler detects a mouse-move.  Also calls the
     * optional onDrag method.
     * 
     * @param {OpenLayers.Pixel} pixel
     */
    moveFeature: function(pixel) {
    	
    	var px = this.feature.icon.px.add(pixel.x - this.lastPixel.x, pixel.y - this.lastPixel.y);;
        this.feature.moveTo(px);
        this.lastPixel = pixel;
        this.onDrag(this.feature, pixel);
    },
	
    /** @final @type String */
    CLASS_NAME: "OpenLayers.Control.DragMarker"
});
