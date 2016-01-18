/* Copyright (c) 2006 ASO crea este bot�n para hacer el zoomin */

/**
 * @class
 *
 * Imlements a very simple button control.
 * @requires OpenLayers/Control.js
 */
 OpenLayers.Control.SaveLocalgis  = 
 OpenLayers.Class(OpenLayers.Control, {
     /** @type OpenLayers.Control.TYPE_* */
     type: OpenLayers.Control.TYPE_BUTTON,
     
     /** @type String */
     urlSaveMapContext: null, 
     
     /**
     * @constructor
     *
     * @param {String} urlSaveMapContext Url para salvar el contexto del mapa
     */
     initialize: function(urlSaveMapContext) {
         OpenLayers.Control.prototype.initialize.apply(this, arguments);
         this.urlSaveMapContext = urlSaveMapContext;
     },
     
     trigger: function() {         
         var requestSaving = this.urlSaveMapContext;
         var layersVisibility = "&lv=";
         var layersNames = "";
         var layers = this.map.layers;
         var i = 0;
         for (i = 0; i < layers.length; i++){             
             if (layers[i] instanceof OpenLayers.Layer.WMSLocalgis){
                 layersNames += "&lid=" + escape(layers[i].idLayer);
                 if (layers[i].visibility){
                     layersVisibility += 1;
                 }
                 else {
                     layersVisibility += 0;
                 }
             }
         }
         
         var bounds = this.map.calculateBounds();
         var boundingbox = "&miny=" + escape(bounds.bottom);
         boundingbox += "&minx=" + escape(bounds.left);
         boundingbox += "&maxy=" + escape(bounds.top);
         boundingbox += "&maxx=" + escape(bounds.right);
         
         var viewWindow = "&ww=" + this.map.size.w;
         viewWindow += "&wh=" + this.map.size.h;
         
         requestSaving += layersVisibility + layersNames + boundingbox + viewWindow;
//         window.open(requestSaving, "SaveMapContext", "height=" + this.height + ",width="+ this.width + ",scrollbars=yes");
         window.location = requestSaving;
     },
     /** @final @type String */
     CLASS_NAME: "OpenLayers.Control.SaveLocalgis"
 });
 