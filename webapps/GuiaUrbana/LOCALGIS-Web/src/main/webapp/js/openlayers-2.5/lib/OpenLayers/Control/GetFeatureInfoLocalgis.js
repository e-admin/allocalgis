/* Copyright (c) 2006 ASO crea este botón para hacer el zoomin */

/**
 * @class
 *
 * Imlements a very simple button control.
 * @requires OpenLayers/Control.js
 */
OpenLayers.Control.GetFeatureInfoLocalgis  = 
  OpenLayers.Class(OpenLayers.Control, {

    /** @type OpenLayers.Control.TYPE_* */
    type: OpenLayers.Control.TYPE_TOOL,

    lastEventXY: null,
    
    /** @type String */
    /** Funcion JS a la que se llamara cuando se obtenga una feature con respuesta GML asociada */
    getFeatureInfoCallback: null,
    
    /**
    * @constructor
    *
    * @param {String} getFeatureInfoCallback Callback al que llamar al obtener una feature
    */
    initialize: function(getFeatureInfoCallback) {
        OpenLayers.Control.prototype.initialize.apply(this, arguments);
        this.getFeatureInfoCallback = getFeatureInfoCallback;
    },
    
    /**
     * 
     */    
    draw: function() {
        this.handler = new OpenLayers.Handler.ClickLocalgis( this,
                            {done: this.getFeatureInfo}, {keyMask: this.keyMask} );
    },

    getFeatureInfo: function (xy) {
    
    	var capaSeleccionada=true;
    	var urlGetFeatureInfo=null;
    	//Cambiado para permitir que no sea necesario cambiar ninguna capa.
        // Comprobamos que haya capa activa
        if (this.map.activeLayer == null) {
            //alert("Debe seleccionar una capa activa para obtener la información.");
            //return false;
        	capaSeleccionada=false;
        	
        } else if (!(this.map.activeLayer instanceof OpenLayers.Layer.WMSLocalgis) || this.map.activeLayer.urlGetFeatureInfo == null || this.map.activeLayer.urlGetFeatureInfo == '') {
            alert("La capa seleccionada no se puede consultar.");
            return false;
        }
        else{
        	urlGetFeatureInfo=this.map.activeLayer.urlGetFeatureInfo;
        }
        

    	
        
        var strAllActiveLayers="";
        if (!capaSeleccionada){
	        var layers = this.map.layers;
	        for(i = 0;i<layers.length;i++) {
	            if (layers[i].visibility) {
	              if (!(layers[i] instanceof OpenLayers.Layer.Markers) ) {
	            	  if (layers[i].idLayer!=undefined){
	            		  strAllActiveLayers += escape(layers[i].idLayer)+",";
	            		  if ((layers[i].urlGetFeatureInfo!=null) && (layers[i].urlGetFeatureInfo!=""))
	            			  urlGetFeatureInfo=layers[i].urlGetFeatureInfo;
	            	  }
	              }
	            }
	        }
        }
        else{
        	strAllActiveLayers=this.map.activeLayer.idLayer;
        }
        
        OpenLayers.LocalgisUtils.showSearchingPopup();
    
        this.lastEventXY = xy;
        var x = xy.x;
        var y = xy.y;
        var url;
        if ((urlGetFeatureInfo!=null) || (this.map.activeLayer.urlGetFeatureInfo != null)) {
            //url = this.map.activeLayer.urlGetFeatureInfo + "&";
            url = urlGetFeatureInfo + "&";
            url += OpenLayers.Util.getParameterString(
                        {BBOX: this.map.getExtent().toBBOX(),
                         capasactivas:	strAllActiveLayers,
                         X: x,
                         Y: y,
                         WIDTH: this.map.size.w,
                         SLD: "",
                         HEIGHT: this.map.size.h});
        } else {
            url =  this.map.activeLayer.getFullRequestString({
                         REQUEST: "GetFeatureInfo",
                         EXCEPTIONS: "application/vnd.ogc.se_xml",
                         BBOX: this.map.getExtent().toBBOX(),
                         capasactivas:	strAllActiveLayers,
                         X: x,
                         Y: y,
                         INFO_FORMAT: "gml",
                         QUERY_LAYERS: this.map.activeLayer.params.LAYERS,
                         WIDTH: this.map.size.w,
                         SLD: "",
                         HEIGHT: this.map.size.h});
        }
        
        
        //Extraemos solo un cuadro alrederdor del punto seleccionado, para luego buscar cuantas capas caen
        //dentro de ese cuadro y solo buscar por esas capas. Este algoritmo no esta demasiado fino
        var lonlat = this.map.getLonLatFromPixel(this.lastEventXY);
        var resolution = this.map.getResolution();
        
        //Bounds (left botton, right top
        var porcentaje=15;
        //var bounds = (lonlat.lon - (resolution * porcentaje))+","+(lonlat.lat - (resolution * porcentaje))+","+(lonlat.lon + (resolution * porcentaje))+","+(lonlat.lat + (resolution * porcentaje));        

        map.boxLayer.clearMarkers();
        var bounds = new OpenLayers.Bounds((lonlat.lon - (resolution * porcentaje)), (lonlat.lat - (resolution * porcentaje)),(lonlat.lon + (resolution * porcentaje)), (lonlat.lat + (resolution * porcentaje)));
        var box = new OpenLayers.Marker.Box(bounds);
        
        var boundForInfo=bounds.left+","+bounds.bottom+","+bounds.right+","+bounds.top;
        
        //Esto sirve para que se dibuje el cuadrado que abarca el area de busqueda.
        //map.boxLayer.addMarker(box);

        //var geom = bounds.toGeometry()
        
        //Si la capa viene seleccionada dejamos que se comporte como hasta ahora.
        var extraParameters=null;
        if (capaSeleccionada){
        	if (this.map.activeLayer.isExternal)
        		extraParameters="&capasActivas=&bbox="+bounds+"&x="+lonlat.lon+"&y="+lonlat.lat;
        	else
        		extraParameters="&capasActivas="+strAllActiveLayers+"&bbox="+boundForInfo+"&x="+lonlat.lon+"&y="+lonlat.lat;
    	}
        else
        	extraParameters="&capasActivas="+strAllActiveLayers+"&bbox="+boundForInfo+"&x="+lonlat.lon+"&y="+lonlat.lat;
        
        OpenLayers.loadURL(url, extraParameters, this, this.showFeatureInfo, this.showErrorFeatureInfo);
    },
    
    showFeatureInfo: function(xmlHTTPRequest) {

        if (xmlHTTPRequest.status == 200) {

            var contentHTML;;
            var result;

            if (xmlHTTPRequest.responseText != '') {
                result = true;
                contentHTML = xmlHTTPRequest.responseText;
                if (this.map.activeLayer!=null && this.map.activeLayer.isExternal) {
                    contentHTML += "<p class=\"notaPopup\"><b>Nota</b>: Los enlaces mostrados pueden provocar la p&eacute;rdida de la p&aacute;gina actual. Se recomienda abrirlos en una nueva ventana.</p>";
                }
            } else {
                result = false;
                contentHTML = "<br>No se ha encontrado ningún elemento.<br><br>";
            }
        
            OpenLayers.LocalgisUtils.showPopup(contentHTML);

            if (result) {
                var lonlat = this.map.getLonLatFromPixel(this.lastEventXY);
                OpenLayers.Control.GetFeatureInfoLocalgis.showMarker(lonlat);
                // Si la capa no es externa se busca si viene el gml asociado a la feature consultada. Si viene y existe el callbackJS se invoca
            	if (this.map.activeLayer!=null){
	                if (!this.map.activeLayer.isExternal && this.getFeatureInfoCallback != null) {
	                    var gmlResponse = document.getElementById("gmlResponse").innerHTML;
	                    eval(this.getFeatureInfoCallback+'(\"'+gmlResponse+'\")');
	                }
            	}
            	else{
            		if (this.getFeatureInfoCallback != null) {
	                    var gmlResponse = document.getElementById("gmlResponse").innerHTML;
	                    eval(this.getFeatureInfoCallback+'(\"'+gmlResponse+'\")');
	                }
            	}
            }
        }
    },

    showErrorFeatureInfo: function(xmlHTTPRequest) {

        var contentHTML;;
        contentHTML = "<br>No se ha encontrado ningún elemento.<br><br>";

        OpenLayers.LocalgisUtils.showPopup(contentHTML);
    },

    /** @final @type String */
    CLASS_NAME: "OpenLayers.Control.GetFeatureInfoLocalgis"
});

OpenLayers.Control.GetFeatureInfoLocalgis.showMarker = function (lonlat) {
    var size = new OpenLayers.Size(18,17);
    var offset = new OpenLayers.Pixel(-(size.w/2), -(size.h/2));
    var icon = new OpenLayers.Icon(''+OpenLayers.Util.getImagesLocation()+'inf.gif',size,offset);
    var marker = new OpenLayers.Marker(lonlat,icon);
    map.markersAuxiliarLayer.clearMarkers();
    map.markersAuxiliarLayer.addMarker(new OpenLayers.Marker(lonlat,icon)); 
};
