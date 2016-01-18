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
        var keys=new Array();
        var values=new Array();
        var layers = this.map.layers;
        var center = this.map.getCenter();
        var zoom = this.map.getZoom();
        requestPrinting += "x="+escape(center.lon);
        keys.push("x");values.push(escape(center.lon));
        
        requestPrinting += "&y="+escape(center.lat);
        keys.push("y");values.push(escape(center.lat));
        requestPrinting += "&zoom="+escape(zoom);
        keys.push("zoom");values.push(escape(zoom));

        var layers = this.map.layers;
        
        var i = 0;  
        
        for(i = 0;i<layers.length;i++) {
            if (layers[i].visibility && layers[i].inRange && layers[i].disabled != true) {
              if (!(layers[i] instanceof OpenLayers.Layer.Markers) ) {
            	  if (layers[i].idLayer!=undefined)
            		  requestPrinting += "&layers=" + escape(layers[i].idLayer);
                  	keys.push("layers");values.push(escape(layers[i].idLayer));
              }
            }
        }
        if (this.map.markersLayer != null && this.map.markersLayer.visibility) {
            requestPrinting += "&showMarkers=true";
          	keys.push("showMarkers");values.push("true");

        } else {
            requestPrinting += "&showMarkers=false";
          	keys.push("showMarkers");values.push("false");

        }
        if (typeof markerArray != "undefined")
        	{
        	
        	}
     // Geometria en GML de la ultima ruta
//        var routeGml;
        // Descripcion de la ultima ruta calculada
//        var cadenaTextoRuta = "";
        if (typeof cadenaTextoRuta !="undefined" && cadenaTextoRuta != "")
        	{
        	var texto=cadenaTextoRuta;//encodeURIComponent(cadenaTextoRuta);
        	texto.replace(/\"/g, '\\"');
        	requestPrinting += "&desc="+texto;
          	keys.push("desc");values.push(texto);

        	}
        if (typeof routeGml !="undefined" && routeGml != "")
    	{
        	var gml=encodeURIComponent(routeGml);
        	requestPrinting += "&overlay="+gml;
          	keys.push("overlay");values.push(gml);

    	}
        //window.open(requestPrinting, "PrintMap", "height=" + this.height + ",width="+ this.width + ",scrollbars=yes,status=yes,toolbar=yes");
        
       
        var newWindow = window.open("", "PrintMap", "height=" + this.height + ",width="+ this.width + ",scrollbars=yes,status=yes,toolbar=yes");
        if (!newWindow) return false;
        var html = "";
        html += "<html><head></head><body><form id='formid' method='post' action='" + this.urlPrinting + "'>";
        if (keys && values && (keys.length == values.length))
        for (var i=0; i < keys.length; i++)
        html += '<input type="hidden" name="' + keys[i] + '" value="' + values[i] + '"/>';
        html += "</form><script type='text/javascript'>document.getElementById(\"formid\").submit()</script></body></html>";
        newWindow.document.write(html);
    },
    /** @final @type String */
    CLASS_NAME: "OpenLayers.Control.PrintLocalgis"
});
