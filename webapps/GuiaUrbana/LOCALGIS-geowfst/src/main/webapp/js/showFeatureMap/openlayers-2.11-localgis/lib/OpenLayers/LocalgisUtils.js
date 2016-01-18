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
OpenLayers.LocalgisUtils.showPopup = function(contentHTML) {

    map.removeAllPopups();

    var popup = map.addPopupLocalgis('Popup', map.getLonLatFromViewPortPx(new OpenLayers.Pixel(10,10)));

    var content = '<div id="popupLocalgisTitle"><b><span style="color: #000000;">LOCAL</span><span style="color: #DAA74F;">GIS</span></b></div>';
    content += '<div id="popupLocalgisFirstLine"></div>';
    content += '<div id="popupLocalgisSecondLine"></div>';
    content += '<div id="popupLocalgisBody">';
    content += contentHTML;    
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
    content += contentHTML;    
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
    content += contentHTML;    
    content += '</div>';

    popup.setContentHTML(content);

    var divPico = document.createElement("div");
    divPico.style.marginLeft = '253px';
    divPico.style.opacity = '0.95';
//    divPico.style.marginTop = '-92px';
    divPico.innerHTML = '<img src="img/pico.gif"/>';
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
OpenLayers.LocalgisUtils.selectResult= function (element, posX, posY) {
    this.posX = posX;
    this.posY = posY;
    this.elementSelected = element;
};
    
/**
 * Método para ir a un resultado previamente seleccionado de una entidad
 */
OpenLayers.LocalgisUtils.goToResultEntidad = function () {
    OpenLayers.LocalgisUtils.goToResult(12);
};

/**
 * Método para ir a un resultado previamente seleccionado sin restriccion espacial
 */
OpenLayers.LocalgisUtils.goToResultWithoutSpacialRestriction = function () {
    OpenLayers.LocalgisUtils.goToResult(7);
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
};

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
};

//------NUEVO-------------------------------------------->

//Función que limpia el marcador de búsquedade calle
OpenLayers.LocalgisUtils.removeMarkers = function(){ 
	OpenLayers.LocalgisUtils.elementSelected = null;
	OpenLayers.LocalgisUtils.posX = null;
	OpenLayers.LocalgisUtils.posY = null;
    map.markersAuxiliarLayer.clearMarkers();
};

//Clase que permite representar propiedades con sus respectivos valores
OpenLayers.LocalgisUtils.ClassPropertyAndValue = function(groupTitle,property,value){   
	this.groupTitle=groupTitle;
	this.property=property;
	this.value=value;
};
    
//Función que permite el uso de las flechas de despliegue de herramientas y del Panel de Información
OpenLayers.LocalgisUtils.borderBottomBtn_onClick = function(elementBorderBottomBtn,elementName){
  	if(elementBorderBottomBtn.className == "BorderBottomBtn_expanded"){
  		elementBorderBottomBtn.className = "BorderBottomBtn_collapsed";
  		document.getElementById(elementName).className = elementName + "_collapsed";
  	}
  	else{
  		elementBorderBottomBtn.className = "BorderBottomBtn_expanded";       
  		document.getElementById(elementName).className = elementName + "_expanded";
  	}
};

OpenLayers.LocalgisUtils.borderLeftSemiBtn_onClick = function(elementBorderLeftSemiBtn){
  	if(elementBorderLeftSemiBtn.className == "BorderLeftSemiBtn_expanded"){
  		/*Forzar el collapsed del Info Panel al cerrar el head*/
  		document.getElementById("infoPanelBorderBottomSemiBtn").className = "BorderBottomSemiBtn_collapsed";
  		document.getElementById("infoPanelConceleable").className = "infoPanelConceleable_collapsed";
  		  		
  	    setTimeout(function(){  	    	
	  		document.getElementById("infoPanelBox").style.visibility='hidden';  
	  		
	  		elementBorderLeftSemiBtn.className = "BorderLeftSemiBtn_collapsed";
	  		document.getElementById("headConceleable").className = "headConceleable_collapsed";
	  		
	  		document.getElementById("head").style.width='190px';
	  		document.getElementById("head").style.left='610px';  
  	    }, 200);
  	}
  	else{
  		document.getElementById("infoPanelBox").style.visibility='visible';
  		
  		elementBorderLeftSemiBtn.className = "BorderLeftSemiBtn_expanded";       
  		document.getElementById("headConceleable").className = "headConceleable_expanded";
  		
  		document.getElementById("head").style.width='800px';
  		document.getElementById("head").style.left='0px';  
  		
  	    /*setTimeout(function(){  	    	  
  	    	document.getElementById("infoPanelBorderBottomSemiBtn").className = "BorderBottomSemiBtn_expanded";
  	  		document.getElementById("infoPanelConceleable").className = "infoPanelConceleable_expanded";
  	    }, 200);*/
  	}
};

OpenLayers.LocalgisUtils.borderBottomSemiBtn_onClick = function(elementBorderBottomSemiBtn,elementName){
  	if(elementBorderBottomSemiBtn.className == "BorderBottomSemiBtn_expanded"){
  		elementBorderBottomSemiBtn.className = "BorderBottomSemiBtn_collapsed";
  		document.getElementById(elementName).className = elementName + "_collapsed";
  	}
  	else{
  		elementBorderBottomSemiBtn.className = "BorderBottomSemiBtn_expanded";       
  		document.getElementById(elementName).className = elementName + "_expanded";
  	}
};

//Función que permite la uso del Panel de Información
OpenLayers.LocalgisUtils.infoPanel_onClick = function(elementName){
  	if(document.getElementById(elementName).className == "infoPanelChild_collapsed"){        		
  		document.getElementById("layerSwitcher").className = "infoPanelChild_collapsed";
  		document.getElementById("featureInfo").className = "infoPanelChild_collapsed";
  		document.getElementById("featureSearch").className = "infoPanelChild_collapsed";  
  		document.getElementById("streetSearch").className = "infoPanelChild_collapsed"; 
  		document.getElementById(elementName).className = "infoPanelChild_expanded";
  	}
};    

//Función que realiza la búsqueda de una calle (al inicio)
OpenLayers.LocalgisUtils.startStreetSearch = function(){
	if(procedureWSUrl!=''){
		var streetName = "";
		var streetNumber = null;
		Sigm.getInfoByPrimaryKey(
			procedureWSUrl,
			id_municipio,
			procedure_name,
			id_feature,
			address_property,
			getInfoByPrimaryKeyReplyServer = {
				callback: function(data){
					if(data!=null) streetName = data;			  		  						
			  		if (streetName.trim() == "") return;
			  		streetNumber = streetName.match(/(\d+)/g);
			  		//if (streetNumber==null || streetNumber.length==0) return;
			  		if (streetNumber==null || streetNumber.length==0) streetNumber="1";
			  		else{
				  		for(var i=0,len=streetNumber.length;i<len;i++){
				  			streetName = streetName.replace(streetNumber[i],"");
					  	}
			  		}
			  		streetNumber = streetNumber[0];
			  		streetName = streetName.trim();
			  		document.getElementById("streetSearch_StreetName").value=streetName;
			  		document.getElementById("streetSearch_StreetNumber").value=streetNumber;
		      		streetSearch.searchFeature(streetName,streetNumber);
		      		OpenLayers.LocalgisUtils.infoPanel_onClick('streetSearch');
		      		OpenLayers.LocalgisUtils.borderBottomSemiBtn_onClick(document.getElementById("infoPanelBorderBottomSemiBtn"),'infoPanelConceleable');		
				},
				timeout:20000
			}
		);	
	}
};

//Función que realiza una busqueda de un elemento (al inicio)
OpenLayers.LocalgisUtils.startFeatureSearch = function(){ 	
	if(layerWfst.features.length>0){   
		//OpenLayers.LocalgisUtils.updateStyle(layerWfst.features[0]);
    	map.getFirstControlByClass('OpenLayers.Control.FeatureSearchLocalgis').selectFeatureFromSearch(layerWfst.features[0].attributes[layerWfst.pkProperty]);
    	map.zoomToExtent(layerWfst.features[0].geometry.bounds);
    	map.zoomOutBird(3);	
    	OpenLayers.LocalgisUtils.infoPanel_onClick('featureInfo');
      	OpenLayers.LocalgisUtils.borderBottomSemiBtn_onClick(document.getElementById("infoPanelBorderBottomSemiBtn"),'infoPanelConceleable');		
    }
    else{	 	  		      		
    	OpenLayers.LocalgisUtils.startStreetSearch();
  	}
};

//Función que lanza una función dada esperando un tiempo determinado
OpenLayers.LocalgisUtils.waitForFunction = function(functionString,functionWait){	
	setTimeout("clearInterval(" + setInterval(functionString,functionWait) + ")",(functionWait+(functionWait/2)));
};

//Función que busca y reemplaza caracteres en una cadena
OpenLayers.LocalgisUtils.replaceAll = function( text, busca, reemplaza ){
	  while (text.toString().indexOf(busca) != -1)
	      text = text.toString().replace(busca,reemplaza);
	  return text;
};

//Función que transforma el SLD a UTF-8
OpenLayers.LocalgisUtils.replaceStyle = function(){
	sld_style=OpenLayers.LocalgisUtils.replaceAll(sld_style,"&lt;","<");
	sld_style=OpenLayers.LocalgisUtils.replaceAll(sld_style,"&gt;",">");
	sld_style=OpenLayers.LocalgisUtils.replaceAll(sld_style,"&quot;","\"");
	sld_style=OpenLayers.LocalgisUtils.replaceAll(sld_style,"&#39;","'");
};
/*
OpenLayers.LocalgisUtils.updateStyle = function(feature){		
	if(feature.attributes[layerWfst.styleProperty]!=undefined){
		feature.attributes[layerWfst.styleProperty]=style_type; 
		layerWfst.saveAllChanges();
	}
};
*/

//Función de obtención del estilo SLD por defecto de la capa	
OpenLayers.LocalgisUtils.startStyleType = function(){
	if(procedureWSUrl!=''){
		Sigm.getInfoByPrimaryKey(
			procedureWSUrl,
			id_entidad,
			procedure_name,
			id_feature,			
			style_property,
			getInfoByPrimaryKeyReplyServer = {
				callback: function(data){
							if(data!=null) style_type = data;						
				},
				timeout:20000
			}
		);
	}
};

//Función de parseo de SLD y asignación de estilo a capa (elemento normal)	
OpenLayers.LocalgisUtils.completeLayerWfs = function(req) {
	try{
		var formatWfs = new OpenLayers.Format.SLD();
	    var formatWfst = new OpenLayers.Format.SLD();
		
		sld = formatWfs.read(sld_style);    						
		layerWfstStyleMap.styles["default"] = OpenLayers.LocalgisUtils.getStyleModified(OpenLayers.LocalgisUtils.getDefaultStyle(sld, layer_name),style_currentPropertyAndValue);
			
		sld2 = formatWfst.read(sld_style);  
		layerWfsStyleMap.styles["default"] = OpenLayers.LocalgisUtils.getDefaultStyle(sld2, layer_name);
	}
	catch(err){
		alert("ERROR: function completeLayerWfs (" + err + ")");
	}	
	finally{			
		OpenLayers.LocalgisUtils.completeLayerWfs_hover();
		layerWfst.redraw();	
		layerWfs.redraw();	
	}	
};
	
//Función de parseo de SLD y asignación de estilo a capa (sobre el elemento)		
OpenLayers.LocalgisUtils.completeLayerWfs_hover = function(req) {							
	try{				
		var formatWfs = new OpenLayers.Format.SLD();
	    var formatWfst = new OpenLayers.Format.SLD();
	    
		sld = formatWfs.read(sld_style);
		layerWfstStyleMap.styles["temporary"] = OpenLayers.LocalgisUtils.getStyleModified(OpenLayers.LocalgisUtils.getStyleModified(OpenLayers.LocalgisUtils.getDefaultStyle(sld, layer_name),style_currentPropertyAndValue),style_hoverPropertyAndValue);
		//OpenLayers.Util.extend(layerWfstStyleMap.styles["temporary"], {cursor: 'crosshair'});
			
		sld2 = formatWfst.read(sld_style);
		layerWfsStyleMap.styles["temporary"] = OpenLayers.LocalgisUtils.getStyleModified(OpenLayers.LocalgisUtils.getDefaultStyle(sld2, layer_name),style_hoverPropertyAndValue);
		//OpenLayers.Util.extend(layerWfstStyleMap2.styles["temporary"], {cursor: 'crosshair'});
	}
	catch(err){
		alert("ERROR: function completeLayerWfst_hover (" + err + ")");
	}	
	finally{	
		OpenLayers.LocalgisUtils.completeLayerWfs_selected();
		layerWfst.redraw();		
		layerWfs.redraw();	
	}				
};
	
//Función de parseo de SLD y asignación de estilo a capa (elemento seleccionado)	
OpenLayers.LocalgisUtils.completeLayerWfs_selected = function(req) {					
	try{
	    var formatWfs = new OpenLayers.Format.SLD();
	    var formatWfst = new OpenLayers.Format.SLD();
		
		sld = formatWfs.read(sld_style);
		layerWfstStyleMap.styles["select"] = OpenLayers.LocalgisUtils.getStyleModified(OpenLayers.LocalgisUtils.getStyleModified(OpenLayers.LocalgisUtils.getDefaultStyle(sld, layer_name),style_currentPropertyAndValue),style_current_selectedPropertyAndValue);  		
			
		sld2 = formatWfst.read(sld_style);
		layerWfsStyleMap.styles["select"] = OpenLayers.LocalgisUtils.getStyleModified(OpenLayers.LocalgisUtils.getDefaultStyle(sld2, layer_name),style_selectedPropertyAndValue); 					
	}
	catch(err){
		alert("ERROR: function completeLayerWfst_selected (" + err + ")");
	}			
	finally{
		layerWfst.redraw();
		layerWfs.redraw();	
	}				
};

//Función de modificación de estilo OpenLayers
OpenLayers.LocalgisUtils.getStyleModified = function(style,stylePropertyAndValue){
	var styleModified = style;
	for(var i=0; i<styleModified.rules.length; i++) {
		for(var j=0; j<stylePropertyAndValue.length;j++){
			styleModified.rules[i].symbolizer[stylePropertyAndValue[j].groupTitle][stylePropertyAndValue[j].property]=stylePropertyAndValue[j].value;
		}
	}  		
	return styleModified;
};

//Función de traducción de estilo SLD a estilo OpenLayers
OpenLayers.LocalgisUtils.getDefaultStyle = function(sld, layerName) {
	var styles = sld.namedLayers[layerName].userStyles;
	var style;
	for(var i=0; i<styles.length; ++i) {
		style = styles[i];
		if(style.isDefault) {
			break;
		}
	}
	return style;
};

//Función de guardado de cambios
OpenLayers.LocalgisUtils.saveChanges = function(feature){
	var centerLonLat = feature.geometry.getBounds().getCenterLonLat();
	var geometry = new String(new OpenLayers.Geometry.Point(centerLonLat.lon, centerLonLat.lat));
	var getMunicipioByGeometryReplyServer = {
			callback: function(data){
				if(data!=null) id_municipio = data;					
			},
			timeout:20000
		};	
	GeoService.getMunicipioByGeometry(srid, geometry, id_municipio, getMunicipioByGeometryReplyServer);
	feature.attributes[layerWfst.pkProperty]=id_feature;
	feature.attributes[layerWfst.locationProperty]=id_municipio;
	feature.attributes[layerWfst.styleProperty]=style_type;				
	OpenLayers.LocalgisUtils.saveAndClean();
};

//Función guardado y limpieza
OpenLayers.LocalgisUtils.saveAndClean = function(){
	layerWfst.saveAllChanges();
	map.cleanAll(layerWfst);
};

//Función disparada al seleccionar elemento
OpenLayers.LocalgisUtils.selectAndModify = function(event){
	featureInfo.onFeatureSelected(layerWfst.pkProperty,event.feature.attributes[layerWfst.pkProperty]);	
};

//Función disparada al deseleccionar un elemento
OpenLayers.LocalgisUtils.unselectAndModify = function(event){
	featureInfo.cleanAll();   
	map.featureSelected=null;
};

//Función disparada después de modificar un elemento
OpenLayers.LocalgisUtils.afterModify = function(event){
	OpenLayers.LocalgisUtils.saveAndClean();
	event.feature.state = OpenLayers.State.UPDATE;	
	event.feature.attributes[layerWfst.styleProperty]=style_type; 
};

//Función disparada tras la inserción de un elemento
OpenLayers.LocalgisUtils.insert = function(event){
	if(event.feature.fid == undefined){					
    	event.feature.state = OpenLayers.State.INSERT;
    	event.feature.attributes[layerWfst.pkProperty]=id_feature;	
    	event.feature.attributes[layerWfst.locationProperty]=id_municipio; 
    	event.feature.attributes[layerWfst.styleProperty]=style_type; 
		layerWfst.redraw();	  					
		OpenLayers.LocalgisUtils.saveChanges(event.feature);	
	}  	  		      	
};	

//Función que devuelve el nombre y número de la calle más cercana a un punto (lat,lon)
OpenLayers.LocalgisUtils.NearestStreetSearch = function(featureCenterLon,featureCenterLat,srid,id_municipio,streetNameNumber){	
	GeoService.getNearestStreet(
		featureCenterLon,
		featureCenterLat,
		srid,
		id_municipio,
		getNearestStreetReplyServer = {
			callback: function(data){
				if(data!=null) streetNameNumber.value = data;					
			},
			timeout:20000		
		}
	);	
};

//Asigna los eventos de cursores del mapa
OpenLayers.LocalgisUtils.setMapCursorEvents = function(name, value){
	var mapDetail = document.getElementById("mapDetail");
	if(name=="onmousedown"){
		mapDetail.onmousedown = function() {
			document.getElementById("mapDetail").className = value;
		};
	}
	else if(name=="onmouseup"){
		mapDetail.onmouseup = function() {
			document.getElementById("mapDetail").className = value;
		};
	}
	else if(name=="default"){
		mapDetail.className = value;
		if(value=="")			
			mapDetail.className = "cursorDefault";
	}
};

