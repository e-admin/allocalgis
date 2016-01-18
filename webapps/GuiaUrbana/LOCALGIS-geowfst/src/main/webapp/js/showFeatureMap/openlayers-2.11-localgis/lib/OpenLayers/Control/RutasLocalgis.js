/* Copyright (c) 2006 ASO crea este botón para hacer el zoomin */

/**
 * @class
 *
 * Imlements a very simple button control.
 * @requires OpenLayers/Control.js
 */
 
OpenLayers.Control.RutasLocalgis  = 
  OpenLayers.Class(OpenLayers.Control, {
    /** @type OpenLayers.Control.TYPE_* */
    type: OpenLayers.Control.TYPE_BUTTON,
 
   
    
    /**
    * @constructor
    *
    * @param {String} urlPlaceNameInfoService Url del servicio de toponimos
    */
    initialize: function(urlPrinting,height,width) {
	  	OpenLayers.Control.prototype.initialize.apply(this, arguments);
	  	 this.urlPrinting = urlPrinting;
	     this.height = height;
	     this.width = width;
    },
    
    /**
     * Evento de click en el control
     */
    trigger: function() {
    

    	if(esRuta){
	    	var contentHTML;
	    	var checkVehicle = "rdb_0.gif";
	    	var checkPie = "rdb_0.gif";
	    	
	    	
	    	
	    	if(tipoRuta == "VEHICLE")checkVehicle="rdb_1.gif";
	    	else if(tipoRuta == "WALKMAN")checkPie="rdb_1.gif";
	    	contentHTML = '<form name="rutasForm">';
	        contentHTML += '<table align="left" width="100%">';
	      
	        contentHTML += '<tr><td align="left"><img id="rdb0" src="img/'+checkVehicle+'" onClick="OpenLayers.Control.RutasLocalgis.updateCheckIe(0)"/>Calcular la ruta en coche</td></tr>';
	        contentHTML += '<tr><td align="left"><img id="rdb1" src="img/'+checkPie+'" onClick="OpenLayers.Control.RutasLocalgis.updateCheckIe(1)"/>Calcular la ruta a pie</td></tr>';
	        contentHTML += '<tr><td align="left"><img id="rdb2" src="img/rdb_0.gif" onClick="OpenLayers.Control.RutasLocalgis.updateCheckIe(2)"/>Ver el detalle de la ruta</td></tr>';
	        contentHTML += '<tr><td align="left"><img id="rdb3" src="img/rdb_0.gif" onClick="OpenLayers.Control.RutasLocalgis.updateCheckIe(3)"/>Imprimir la ruta</td></tr>';
	        contentHTML += "<tr><td align=\"center\"><input type=\"hidden\" name=\"selRdb\" value=0><div id=\"divButtonRutas\" style=\"margin-top: 6px;\"><img class=\"imageButton\" src=\"img/btn_aceptar.gif\" alt=\"Aceptar\" onClick=\"OpenLayers.Control.RutasLocalgis.calcular('"+this.urlPrinting+"',"+this.height+","+this.width+");\"></div></td></tr>";
	        //contentHTML += '<tr><td align="center"><input type="hidden" name="selRdb" value=0><div id="divButtonRutas" style="margin-top: 6px;"><img class="imageButton" src="img/btn_aceptar.gif" alt="Aceptar" onclick="OpenLayers.Control.RutasLocalgis.calcular(\''+this.urlPrinting+'\','+this.height+','+this.width+');"/></div></td></tr>';
	        contentHTML += '</table>';
	        contentHTML += '</form>';
	        
	        OpenLayers.LocalgisUtils.showPopup(contentHTML);
	        
	       // for(var x = 0; x < 3 ; x++)
	        //{
	        	/*var r=document.createElement('input');
				r.setAttribute("type","radio");
				r.setAttribute("value",x+1);
				r.setAttribute("name","opcion"+x);
				r.onclick =function(){this.checked=true;this.selected=true;};*/
				
	        	
	        	/*var newRadio = document.createElement("input");
				newRadio.type = "radio";
				newRadio.id = "myRadio" + x;
				newRadio.name = "opcion";
				newRadio.value = x+1;
				newRadio.onclick=function(){this.checked=true;}*/
				/*var newLabel = document.createElement("label");
				var labelText;
				if(x==0)labelText="Calcular la ruta a pie";
				if(x==1)labelText="Calcular la ruta en coche";
				if(x==2)labelText="Ver el detalle de la ruta";
				newLabel.appendChild(document.createTextNode(labelText));
				
				document.getElementById("capa"+x).appendChild(r);
				document.getElementById("capa"+x).appendChild(newLabel);*/
	       // }
	       /* var newRadio = document.createElement("input");
			newRadio.type = "radio";
			newRadio.id = "myRadio";
			newRadio.name = "myRadio";
			newRadio.value = "someValue";
			
			document.getElementById("capa").appendChild(newRadio);*/
			
        }
        
        
        
    },

    /** @final @type String */
    CLASS_NAME: "OpenLayers.Control.RutasLocalgis"
});
OpenLayers.Control.RutasLocalgis.updateCheckIe = function(valor) {
	
		for(var x=0; x<3;x++)
		{
			document.getElementById("rdb"+x).src="img/rdb_0.gif";
		}
		document.getElementById("rdb"+valor).src="img/rdb_1.gif";
			
		document.forms.rutasForm.selRdb.value = valor;	
	
	
}
	 


OpenLayers.Control.RutasLocalgis.calcular = function(urlPrinting,height,width) {
	
var divButtonRutas = document.getElementById('divButtonRutas');
try{
	
	var optionSel = document.forms.rutasForm.selRdb.value;
	
	if((optionSel == 0) || (optionSel == 1))
	{
		tipoRuta = "VEHICLE";
		if(optionSel == 1)tipoRuta = "WALKMAN";
		calcularRuta(0,tipoRuta,true,esRutaSalesMan);
		
		
	}
	
	if(optionSel == 2)
	{
		var mensajeHtml = "<div id='descRuta'></div>";
		OpenLayers.LocalgisUtils.showPopup(mensajeHtml);
		mostrarDescripcion();
		
	}
	if(optionSel == 3)
	{
		
		var requestPrinting = urlPrinting;
        //Añadimos los parametros a la peticion
        if (!OpenLayers.String.contains(requestPrinting, "?")) {
            requestPrinting += "?";
        } else {
            requestPrinting += "&";
        }
        var layers = map.layers;
        var center = map.getCenter();
        var zoom = map.getZoom();
        requestPrinting += "x="+escape(center.lon);
        requestPrinting += "&y="+escape(center.lat);
        requestPrinting += "&zoom="+escape(zoom);
        
        var listadoLayers = "";
        var layers = map.layers;
        var i = 0;        
        for(i = 0;i<layers.length;i++) {
            if (layers[i].visibility && layers[i].inRange && layers[i].disabled != true) {
            
              if((!(layers[i] instanceof OpenLayers.Layer.Markers)) &&  (!(layers[i] instanceof OpenLayers.Layer.Vector))){
            	  if(listadoLayers != "")listadoLayers+=",";
            	  listadoLayers+= escape(layers[i].idLayer);
              }
            }
        }
        requestPrinting += "&layers="+listadoLayers;
        if (map.markersLayer != null && map.markersLayer.visibility) {
            requestPrinting += "&showMarkers=true";
        } else {
            requestPrinting += "&showMarkers=false";
        }
        
        if(arrayRoute.length != 0) requestPrinting += "&markerRouteRequest="+arrayRoute;
        if(layerVector.features.length != 0) requestPrinting += "&esRuta=true";
        else requestPrinting += "&esRuta=false";
        
        if(layerPolygon.features.length != 0) requestPrinting += "&esArea=true";
        else requestPrinting += "&esArea=false";
        map.removeAllPopups();
        
        window.open(requestPrinting, "PrintMap", "height=" + height + ",width="+ width + ",scrollbars=yes");//,status=1,toolbar=1");
	}
	
	divButtonRutas.innerHTML = '<img src="'+OpenLayers.Util.getImagesLocation()+'ajax-loader.gif" alt="Calculando"/>';
}
catch(e){
	alert(e);
	
	divButtonRutas.innerHTML = "<img class=\"imageButton\" src=\"img/btn_aceptar.gif\" alt=\"Aceptar\" onClick=\"OpenLayers.Control.RutasLocalgis.calcular('"+urlPrinting+"',"+height+","+width+");\">";
}
}
    
