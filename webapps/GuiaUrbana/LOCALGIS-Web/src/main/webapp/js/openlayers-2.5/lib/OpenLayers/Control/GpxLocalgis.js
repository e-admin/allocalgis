/* Copyright (c) 2006 ASO crea este botón para hacer el zoomin */

/**
 * @class
 *
 * Imlements a very simple button control.
 * @requires OpenLayers/Control.js
 */
OpenLayers.Control.GpxLocalgis  = 
  OpenLayers.Class(OpenLayers.Control, {
	     /** @type OpenLayers.Control.TYPE_* */
	     type: OpenLayers.Control.TYPE_BUTTON,
	     
	     /** @type String */
	     urlGpx: null, 
	     
	     /**
	     * @constructor
	     *
	     * @param {String} urlGpx Url para salvar el contexto del mapa
	     */
	     initialize: function(urlGpx) {
	         OpenLayers.Control.prototype.initialize.apply(this, arguments);
	         this.urlGpx = urlGpx;
	     },
	     
	     trigger: function() {       
	    	 
	         // Comprobamos que haya capa activa
	         if (this.map.activeLayer == null) {
	             alert("Debe seleccionar una capa activa para obtener la información.");
	             return false;
	         } else if (!(this.map.activeLayer instanceof OpenLayers.Layer.WMSLocalgis) || this.map.activeLayer.urlGetFeatureInfo == null || this.map.activeLayer.urlGetFeatureInfo == '') {
	             alert("La capa seleccionada no se puede consultar.");
	             return false;
	         }	   
	        
	         //alert(this.map);
	         
	         var requestSaving = this.urlGpx;
	         
	         requestSaving += "&idlayer="+this.map.activeLayer.idLayer;
	         window.location = requestSaving;
	     },
    CLASS_NAME: "OpenLayers.Control.GpxLocalgis"
});

OpenLayers.Control.GpxLocalgis.showMarker = function (lonlat) {
    var size = new OpenLayers.Size(18,17);
    var offset = new OpenLayers.Pixel(-(size.w/2), -(size.h/2));
    var icon = new OpenLayers.Icon(''+OpenLayers.Util.getImagesLocation()+'inf.gif',size,offset);
    var marker = new OpenLayers.Marker(lonlat,icon);
    map.markersAuxiliarLayer.clearMarkers();
    map.markersAuxiliarLayer.addMarker(new OpenLayers.Marker(lonlat,icon)); 
};
