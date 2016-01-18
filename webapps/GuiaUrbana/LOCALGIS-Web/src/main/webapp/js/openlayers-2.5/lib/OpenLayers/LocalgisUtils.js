/**
 * Para poder usar los métodos de este fichero es necesario que se pueda acceder al mapa (de la clase MapLocalgis) 
 * que se está gestionando. Es decir, que el mapa sea global.
 */
/**
 * Namespace: OpenLayers.LocalgisUtils
 * {Object}
 */
OpenLayers.LocalgisUtils = function() {};
OpenLayers.LocalgisUtils.elementSelected = null;
OpenLayers.LocalgisUtils.posX = null;
OpenLayers.LocalgisUtils.posY = null;



/**
 * Método para mostrar un popup dentro del mapa con un contenido HTML determinado
 */
OpenLayers.LocalgisUtils.showPopupReport = function(contentHTML) {
	 
	map.destroyPopup('PopupReport');
   
    var popup = map.addPopupLocalgisReport('PopupReport', map.getLonLatFromViewPortPx(new OpenLayers.Pixel(400,30)));

    var content = '<div id="popupLocalgisTitle"><b><span style="color: #000000;">LOCAL</span><span style="color: #DAA74F;">GIS</span></b></div>';
    content += '<div id="popupLocalgisFirstLine"></div>';
    content += '<div id="popupLocalgisSecondLine"></div>';
    content += '<div id="popupLocalgisBodyReport">';
    content += contentHTML    
    content += '</div>';

    popup.setContentHTML(content);
    //Hack para poder hacer el copy en iexplorer de los datos de la popup
    void(document.onselectstart=null);

};

/**
 * Método para mostrar un popup dentro del mapa con un contenido HTML determinado
 */
OpenLayers.LocalgisUtils.showPopup = function(contentHTML) {

    map.removeAllPopups();

    var popup = map.addPopupLocalgis('Popup', map.getLonLatFromViewPortPx(new OpenLayers.Pixel(10,10)));

    var content = '<div id="popupLocalgisTitle"><b><span style="color: #000000;">LOCAL</span><span style="color: #DAA74F;">GIS</span></b></div>';
    content += '<div id="popupLocalgisFirstLine"></div>';
    content += '<div id="popupLocalgisSecondLine"></div>';
    content += '<div id="popupLocalgisBody">';
    content += contentHTML    
    content += '</div>';

    popup.setContentHTML(content);
    //Hack para poder hacer el copy en iexplorer de los datos de la popup
    void(document.onselectstart=null);

};

/**
 * Método para mostrar un popup dentro del mapa con un contenido HTML determinado
 */
OpenLayers.LocalgisUtils.showPopupSize = function(contentHTML, width, height, name) {

    map.removeAllPopups();

    var popup = map.addPopupLocalgisSize(name, map.getLonLatFromViewPortPx(new OpenLayers.Pixel(10,10)),width, height);

    var content = '<div id="popupLocalgisTitle"><b><span style="color: #000000;">LOCAL</span><span style="color: #DAA74F;">GIS</span></b></div>';
    content += '<div id="popupLocalgisFirstLine"></div>';
    content += '<div id="popupLocalgisSecondLine"></div>';
    content += '<div id="popupLocalgisBody">';
    content += contentHTML    
    content += '</div>';

    popup.setContentHTML(content);
    //Hack para poder hacer el copy en iexplorer de los datos de la popup
    void(document.onselectstart=null);

};
/**
 * Método para mostrar el pop de buscar coordenadas y viales
 * Esto lo hice para poder dar otro estilo al popup porque el otro eraç
 * muy grande
 */
OpenLayers.LocalgisUtils.showPopup2 = function(contentHTML) {

    map.removeAllPopups();

    var popup = map.addPopupLocalgis('Popup', map.getLonLatFromViewPortPx(new OpenLayers.Pixel(10,10)));

    var content = '<div id="popupLocalgisTitle"><b><span style="color: #000000;">LOCAL</span><span style="color: #DAA74F;">GIS</span></b></div>';
    content += '<div id="popupLocalgisFirstLine"></div>';
    content += '<div id="popupLocalgisSecondLine"></div>';
    content += '<div id="popupLocalgis2">';
    content += contentHTML    
    content += '</div>';

    popup.setContentHTML(content);
    //Hack para poder hacer el copy en iexplorer de los datos de la popup
    void(document.onselectstart=null);

};

OpenLayers.LocalgisUtils.showPopupBig = function(contentHTML) {

    map.removeAllPopups();

    var popup = map.addPopupLocalgisBig('Popup', map.getLonLatFromViewPortPx(new OpenLayers.Pixel(10,10)));

    var content = '<div id="popupLocalgisTitle"><b><span style="color: #000000;">LOCAL</span><span style="color: #DAA74F;">GIS</span></b></div>';
    content += '<div id="popupLocalgisFirstLine"></div>';
    content += '<div id="popupLocalgisSecondLine"></div>';
    content += '<div id="popupLocalgisBodyBig">';
    content += contentHTML    
    content += '</div>';

    popup.setContentHTML(content);
};
/**
 * Método para mostrar un popup dentro del mapa con un contenido HTML determinado y con un pico que llegue hasta el centro del mapa
 */
OpenLayers.LocalgisUtils.showPopupPico = function(contentHTML) {

    map.removeAllPopups();

    var popup = map.addPopupLocalgis('Popup', map.getLonLatFromViewPortPx(new OpenLayers.Pixel(10,10)));

    var content = '<div id="popupLocalgisTitle"><b><span style="color: #000000;">LOCAL</span><span style="color: #DAA74F;">GIS</span></b></div>';
    content += '<div id="popupLocalgisFirstLine"></div>';
    content += '<div id="popupLocalgisSecondLine"></div>';
    content += '<div id="popupLocalgisBody">';
    content += contentHTML    
    content += '</div>';

    popup.setContentHTML(content);

    var divPico = document.createElement("div");
    divPico.style.marginLeft = '253px';
    divPico.style.opacity = '0.95';
//    divPico.style.marginTop = '-92px';
    divPico.innerHTML = '<img src="img/pico.gif"/>'
    popup.div.appendChild(divPico);
};

/**
 * Método para mostrar un popup dentro del mapa que indica que se está cargando información
 */
OpenLayers.LocalgisUtils.showSearchingPopup = function() {

    var contentHTML = '<table style="margin-left:auto;margin-right:auto;text-align:center">';
    contentHTML += '<tr>';
    contentHTML += '<td align="center" colspan="2"><div id="divButtonSearchPlaceName"><img src="'+OpenLayers.Util.getImagesLocation()+'ajax-loader.gif" alt="Buscando"/></div></td>';
    contentHTML += '</tr>';
    contentHTML += '</table>';
    
    OpenLayers.LocalgisUtils.showPopup(contentHTML);

};

/**
 * Método para mostrar un popup dentro del mapa con un error determinado
 */
OpenLayers.LocalgisUtils.showError = function (message) {
    if (message == undefined || message == null) {
        message = 'No se ha podido realizar la operación.';
    }
    OpenLayers.LocalgisUtils.showPopup('<br>'+message+'<br><br>');
};

/**
 * Método para mostrar un mostrar una marca en una posición determinada en el mapa
 */
OpenLayers.LocalgisUtils.showMarker = function (lonlat) {
    var size = new OpenLayers.Size(12,20);
    var offset = new OpenLayers.Pixel(-(size.w/2), -size.h);
    var icon = new OpenLayers.Icon('img/point.gif',size,offset);
    var marker = new OpenLayers.Marker(lonlat,icon);
    map.markersAuxiliarLayer.clearMarkers();
    map.markersAuxiliarLayer.addMarker(new OpenLayers.Marker(lonlat,icon)); 
};


OpenLayers.LocalgisUtils.showIncidencia = function (lonlat) {
    var size = new OpenLayers.Size(12,20);
    var offset = new OpenLayers.Pixel(-(size.w/2), -size.h);
    var icon = new OpenLayers.Icon('img/rdb_1.gif',size,offset);
    var incidencia = new OpenLayers.Incidencia(lonlat,icon);
    map.incidenciasAuxiliarLayer.addIncidencia(incidencia); 
};

/**
 * Método para mostrar una marca especifica en una posición determinada en el mapa
 */
OpenLayers.LocalgisUtils.showCustomMarker = function (lonlat, imageName) {
    var size = new OpenLayers.Size(20,20);
    var offset = new OpenLayers.Pixel(-size.w/2, -size.h/2);
    var icon = new OpenLayers.Icon('img/' + imageName, size, offset);
    var marker = new OpenLayers.Marker(lonlat,icon);
    map.markersAuxiliarLayer.clearMarkers();
    map.markersAuxiliarLayer.addMarker(new OpenLayers.Marker(lonlat,icon)); 
};

/**
 * Método para seleccionar un resultado de una busqueda
 */
OpenLayers.LocalgisUtils.selectResult = function (element, posX, posY) {
    if (this.elementSelected != null) {
        this.elementSelected.className = "popupLocalgisListItem_0";
    }
    this.posX = posX;
    this.posY = posY;
    element.className = "popupLocalgisListItemSelected";
    this.elementSelected = element;
};
    

/**
 * Método para anadir un origen en el calculo de rutas cuando se busca por callejero
 */
OpenLayers.LocalgisUtils.addOrigen = function () {
	
	 if (OpenLayers.LocalgisUtils.elementSelected == null) {
	        alert("Debe seleccionar un elemento");
	    } else {
	        map.removeAllPopups();
	        OpenLayers.LocalgisUtils.elementSelected = null;
	        var lonlat = new OpenLayers.LonLat(this.posX,this.posY);
	        //OpenLayers.LocalgisUtils.showMarker(lonlat);
	        //map.setCenter(lonlat, zoomLevel, false, false);
	        setNodo(lonlat,0);
	    }
};

/**
 * Método para anadir un destino en el calculo de rutas cuando se busca por callejero
 */
OpenLayers.LocalgisUtils.addDestino = function () {
	
	 if (OpenLayers.LocalgisUtils.elementSelected == null) {
	        alert("Debe seleccionar un elemento");
	    } else {
	        map.removeAllPopups();
	        OpenLayers.LocalgisUtils.elementSelected = null;
	        var lonlat = new OpenLayers.LonLat(this.posX,this.posY);
	        //OpenLayers.LocalgisUtils.showMarker(lonlat);
	        //map.setCenter(lonlat, zoomLevel, false, false);
	        setNodo(lonlat,1);
	    }
};

/**
 * Método para anadir un nuevo destino en el calculo de rutas cuando se busca por callejero
 */
OpenLayers.LocalgisUtils.addAddDestino = function () {
	
	 if (OpenLayers.LocalgisUtils.elementSelected == null) {
	        alert("Debe seleccionar un elemento");
	    } else {
	        map.removeAllPopups();
	        OpenLayers.LocalgisUtils.elementSelected = null;
	        var lonlat = new OpenLayers.LonLat(this.posX,this.posY);
	        //OpenLayers.LocalgisUtils.showMarker(lonlat);
	        //map.setCenter(lonlat, zoomLevel, false, false);
	        setNodo(lonlat,-1);
	    }
};


/**
 * Método para ir a un resultado previamente seleccionado de una entidad
 */
OpenLayers.LocalgisUtils.goToResultEntidad = function () {
    OpenLayers.LocalgisUtils.goToResult(16)
};

/**
 * Método para ir a un resultado previamente seleccionado sin restriccion espacial
 */
OpenLayers.LocalgisUtils.goToResultWithoutSpacialRestriction = function () {
    OpenLayers.LocalgisUtils.goToResult(7)
};

/**
 * Método para ir a un resultado previamente seleccionado con un determinado nivel de zoom
 */
OpenLayers.LocalgisUtils.goToResult = function (zoomLevel) {
    if (OpenLayers.LocalgisUtils.elementSelected == null) {
        alert("Debe seleccionar un elemento");
    } else {
        map.removeAllPopups();
        OpenLayers.LocalgisUtils.elementSelected = null;
        var lonlat = new OpenLayers.LonLat(this.posX,this.posY);
        OpenLayers.LocalgisUtils.showMarker(lonlat);
        map.setCenter(lonlat, zoomLevel, false, false);
    }
};

/**
 * Método para ir a una posicion determinada previamente seleccionado con un determinado nivel de zoom
 */
OpenLayers.LocalgisUtils.goToPosition = function (lonlat) {
    map.removeAllPopups();
    OpenLayers.LocalgisUtils.showMarker(lonlat);
    map.setCenter(lonlat, 12, false, false);
};

/**
 * Método para obtener las capas de una URL (el valor del parametro LAYERS)
 */
OpenLayers.LocalgisUtils.getLayersFromURL = function (url) {
    var indexOfParams = url.indexOf("?");
    var indexOfParamLayers = url.indexOf("LAYERS=", indexOfParams);
    if (indexOfParamLayers == -1) {
            return null;
    }
    var indexOfEndParamLayers = url.indexOf("&", indexOfParamLayers);

    return unescape(url.substring(indexOfParamLayers + "LAYERS=".length, indexOfEndParamLayers));    
}

/**
 * Método para ordenar un vector de capas para conseguir el mismo comportamiento 
 * que en el editor de Geopista. Las capas vienen en el orden en que se deben 
 * dibujar, pero lo que se quiere es mostrarlas en otro orden: primero 
 * ortofotos, si la hay, luego provincias y despues el resto de capas empezando 
 * por la última
 */
OpenLayers.LocalgisUtils.sortLayers = function(layers) {
    var numberOfLayers = layers.length;
    var result = [];
    var i;
    var positionFirstWMSLocalgisNoSystem;
    var positionLastWMSLocalgis;
    /*
     * Vamos insertando en el resultado todas las capas que encontremos hasta 
     * que encontremos una capa WMSLocalgis que no sea del sistema
     */
    for (i = 0; i < numberOfLayers; i++) {
        var layer = layers[i];
        if (layer instanceof OpenLayers.Layer.WMSLocalgis) {
            if (layer.isSystemLayer) {
                result.push(layer);
            } else {
                // Encontrada una capa WMSLocalgis que no es del sistema, paramos
                break;
            }
        } else {
            result.push(layer);
        }
    }
    positionFirstWMSLocalgisNoSystem = i;
    /*
     * Saltamos todas las capas que no sean WMSLocalgis y nos guardamos el contador en k
     */
    for (i = numberOfLayers - 1; i >= positionFirstWMSLocalgisNoSystem; i--) {
        var layer = layers[i];
        if (layer instanceof OpenLayers.Layer.WMSLocalgis) {
            break;
        }
    }
    positionLastWMSLocalgis = i;
    for (i = positionLastWMSLocalgis; i >= positionFirstWMSLocalgisNoSystem; i--) {
        var layer = layers[i];
        result.push(layer);
    }
    for (i = positionLastWMSLocalgis + 1; i < numberOfLayers; i++) {
        var layer = layers[i];
        result.push(layer);
    }
    
    return result;
}
    
