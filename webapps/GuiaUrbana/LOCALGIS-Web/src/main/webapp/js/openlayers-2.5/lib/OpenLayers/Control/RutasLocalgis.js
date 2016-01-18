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
	  	 urlPrintingRoute = urlPrinting;
	     this.height = height;
	     heightRoute = height;
	     this.width = width;
	     widthRoute = width;
    },
    
    /**
     * Evento de click en el control
     */
    trigger: function() {
    

//    	if(esRuta){
	    	var contentHTML;
	    	var checkVehicle = "rdb_0.gif";
	    	var checkPie = "rdb_0.gif";
	    	var checkPMR = "rdb_0.gif";
	    	

	    	if(tipoRuta == "VEHICLE")checkVehicle="rdb_1.gif";
	    	else if(tipoRuta == "WALKMAN")checkPie="rdb_1.gif";
	    	else if(tipoRuta == "PMR")checkPMR="rdb_1.gif";
//	    	contentHTML = '<form name="rutasForm">';
//	        contentHTML += '<table align="left" width="100%">';
//	      
//            contentHTML += '<tr><td align="left">Elige red</td>';
//            contentHTML += '<td><div style="position:relative;width: 130px">';
//            contentHTML += '<select id=\"red\" style="border-width:0px; width: 130px;" >';
//            contentHTML += '<option value="Callejero">Callejero</option><option value="CallejeroPMR">CallejeroPMR</option></select>';
//            contentHTML += '</div></td></tr>';
//	        contentHTML += '<tr><td colspan=2 align="left"><img id="rdb0" src="img/'+checkVehicle+'" onClick="OpenLayers.Control.RutasLocalgis.updateCheckIe(0)"/>Calcular la ruta en coche</td></tr>';
//	        contentHTML += '<tr><td colspan=2 align="left"><img id="rdb1" src="img/'+checkPie+'" onClick="OpenLayers.Control.RutasLocalgis.updateCheckIe(1)"/>Calcular la ruta a pie</td></tr>';
//	        contentHTML += '<tr><td align="left">¿Existe discapacidad?</td>';
//            contentHTML += '<td><div style="position:relative;width: 130px">';
//            contentHTML += '<select id=\"disability\" style="border-width:0px; width: 130px;" >';
//            contentHTML += '<option value="SI">SI</option><option value="NO">NO</option></select>';
//            contentHTML += '</div></td></tr>';
//            contentHTML += '<tr><td align="left">Anchura acera</td>';
//            contentHTML += '<td><div style="position:relative;width: 130px">';
//            contentHTML += '<input type="text" class="inputTextField" id=\"pavementWidth\" style="border-width:0px; width: 130px;" > ';
//            contentHTML += '</div></td></tr>';
//            contentHTML += '<tr><td align="left">Pendiente transversal(%)</td>';
//            contentHTML += '<td><div style="position:relative;width: 130px">';
//            contentHTML += '<input type="text" class="inputTextField" id=\"transversalSlope\" style="border-width:0px; width: 130px;" >';
//            contentHTML += '</div></td></tr>';
//            contentHTML += '<tr><td align="left">Pendiente longitudinal(%)</td>';
//            contentHTML += '<td><div style="position:relative;width: 130px">';
//            contentHTML += '<input type="text" class="inputTextField" id=\"longitudinalSlope\" style="border-width:0px; width: 130px;" >';
//            contentHTML += '</div></td></tr>';
//	        contentHTML += '<tr><td align="left" colspan=2><img id="rdb2" src="img/rdb_0.gif" onClick="OpenLayers.Control.RutasLocalgis.updateCheckIe(2)"/>Ver el detalle de la ruta</td></tr>';
//	        contentHTML += '<tr><td align="left" colspan=2><img id="rdb3" src="img/rdb_0.gif" onClick="OpenLayers.Control.RutasLocalgis.updateCheckIe(3)"/>Imprimir la ruta</td></tr>';
//	        contentHTML += "<tr><td align=\"center\" colspan=2><input type=\"hidden\" name=\"selRdb\" value=0><div id=\"divButtonRutas\" style=\"margin-top: 6px;\"><img class=\"imageButton\" src=\"img/btn_aceptar.gif\" alt=\"Aceptar\" onClick=\"OpenLayers.Control.RutasLocalgis.calcular('"+this.urlPrinting+"',"+this.height+","+this.width+");\"></div></td></tr>";
//	        //contentHTML += '<tr><td align="center" colspan=2><input type="hidden" name="selRdb" value=0><div id="divButtonRutas" style="margin-top: 6px;"><img class="imageButton" src="img/btn_aceptar.gif" alt="Aceptar" onclick="OpenLayers.Control.RutasLocalgis.calcular(\''+this.urlPrinting+'\','+this.height+','+this.width+');"/></div></td></tr>';
//	        contentHTML += '</table>';
//	        contentHTML += '</form>';
	        
	    	
	    	
	    	contentHTML = '<form name="rutasForm">';
	        contentHTML += '<table align="left" width="100%">';

	        contentHTML += '<tr><td colspan=2 align="left"><img id="rdb0" src="img/'+checkVehicle+'" onClick="OpenLayers.Control.RutasLocalgis.updateCheckIe(0)"/>Calcular la ruta en coche</td></tr>';
	        contentHTML += '<tr><td colspan=2 align="left"><img id="rdb1" src="img/'+checkPie+'" onClick="OpenLayers.Control.RutasLocalgis.updateCheckIe(1)"/>Calcular la ruta a pie</td></tr>';
	        contentHTML += '<tr><td colspan=2 align="left"><img id="rdb2" src="img/'+checkPie+'" onClick="OpenLayers.Control.RutasLocalgis.updateCheckIe(2)"/>Calcular la ruta PMR</td></tr>';
	                
	        contentHTML += '<tr><td><div id="disabilityZone" style="display:none">';
	        contentHTML += '<table>';
	        contentHTML += '<tr><td align="left">Anchura acera</td>';
            contentHTML += '<td><div style="position:relative;width: 130px">';
            contentHTML += '<input type="text" class="inputTextField" id=\"pavementWidth\" style="border-width:0px; width: 130px;" > ';
            contentHTML += '</div></td></tr>';
            contentHTML += '<tr><td align="left">Pendiente transversal(%)</td>';
            contentHTML += '<td><div style="position:relative;width: 130px">';
            contentHTML += '<input type="text" class="inputTextField" id=\"transversalSlope\" style="border-width:0px; width: 130px;" >';
            contentHTML += '</div></td></tr>';
            contentHTML += '<tr><td align="left">Pendiente longitudinal(%)</td>';
            contentHTML += '<td><div style="position:relative;width: 130px">';
            contentHTML += '<input type="text" class="inputTextField" id=\"longitudinalSlope\" style="border-width:0px; width: 130px;" >';
            contentHTML += '</table>';
	        contentHTML += '</div></td></tr>';
	        contentHTML += '<tr><td>---------------------------------------------------</td></tr>';
	        contentHTML += '<tr><td align="left" colspan=2><img id="rdbinfo0" src="img/rdb_0.gif" onClick="OpenLayers.Control.RutasLocalgis.updateCheckIeInfo(0)"/>Ver el detalle de la ruta</td></tr>';
	      //  contentHTML += '<tr><td align="left" colspan=2><img id="rdbinfo1" src="img/rdb_0.gif" onClick="OpenLayers.Control.RutasLocalgis.updateCheckIeInfo(1)"/>Imprimir la ruta</td></tr>';
	        contentHTML += '<tr><td align="left" colspan=2><img id="rdbinfo2" src="img/rdb_0.gif" onClick="OpenLayers.Control.RutasLocalgis.updateCheckIeInfo(2)"/>Deshabilitar información</td></tr>';

	        contentHTML += "<tr><td align=\"center\" colspan=2><input type=\"hidden\" name=\"selRdb\" value=0><input type=\"hidden\" name=\"selRdbInfo\" value=0><div id=\"divButtonRutas\" style=\"margin-top: 6px;\"><img class=\"imageButton\" src=\"img/btn_aceptar.gif\" alt=\"Aceptar\" onClick=\"OpenLayers.Control.RutasLocalgis.calcular('"+this.urlPrinting+"',"+this.height+","+this.width+");\"></div></td></tr>";
	        //
	        contentHTML += '</table></div>';
	        contentHTML += '</form>';
	    	   
	        OpenLayers.LocalgisUtils.showPopupSize(contentHTML, 300,400, namepopup);
	        
	        for(var x=0; x<3;x++)
			{
				document.getElementById("rdb"+x).src="img/rdb_0.gif";
			}
	        document.getElementById("rdb"+selRdb).src="img/rdb_1.gif";
	        document.forms.rutasForm.selRdb.value = selRdb;
			
			
			if(selRdb == 2){
				document.getElementById("disabilityZone").style.display = 'block';
				document.getElementById("pavementWidth").value = widthSideWalk;
				document.getElementById("transversalSlope").value = transversalSlope;
				document.getElementById("longitudinalSlope").value = longitudinalSlope;
			}
	      
	        for(var x=0; x<3;x++)
	    	{
	    		document.getElementById("rdbinfo"+x).src="img/rdb_0.gif";
	    	}
	    	document.getElementById("rdbinfo"+selRdbInfo).src="img/rdb_1.gif";
	    	document.forms.rutasForm.selRdbInfo.value = selRdbInfo;
//        }
        
        
        
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
		

		if(valor == 0 || valor == 1){
			document.getElementById("disabilityZone").style.display = 'none';
			if(document.getElementById("pavementWidth").value != "")
				widthSideWalk = document.getElementById("pavementWidth").value;
			if(document.getElementById("transversalSlope").value != "")
				transversalSlope = document.getElementById("transversalSlope").value;
			if(document.getElementById("longitudinalSlope").value != "")
				longitudinalSlope = document.getElementById("longitudinalSlope").value;
		}
		else{
			document.getElementById("disabilityZone").style.display = 'block';
			document.getElementById("pavementWidth").value = widthSideWalk;
			document.getElementById("transversalSlope").value = transversalSlope;
			document.getElementById("longitudinalSlope").value = longitudinalSlope;
			
		}
		
}

OpenLayers.Control.RutasLocalgis.updateCheckIeInfo = function(valor) {
	
	for(var x=0; x<3;x++)
	{
		document.getElementById("rdbinfo"+x).src="img/rdb_0.gif";
	}
	document.getElementById("rdbinfo"+valor).src="img/rdb_1.gif";
		
	document.forms.rutasForm.selRdbInfo.value = valor;	


}


OpenLayers.Control.RutasLocalgis.showDescriptionRoute= function() {
	var mensajeHtml = "<div id='descRuta'></div>";
	OpenLayers.LocalgisUtils.showPopup(mensajeHtml);
	mostrarDescripcion(); 
}




OpenLayers.Control.RutasLocalgis.printRoute = function() {
	
	var requestPrinting = urlPrintingRoute;
	var height = heightRoute;
	var width = widthRoute;
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


OpenLayers.Control.RutasLocalgis.calcular = function(urlPrinting,height,width) {
	
var divButtonRutas = document.getElementById('divButtonRutas');
try{
	
	var optionSel = document.forms.rutasForm.selRdb.value;
	var disabilityType = 0;
//	var pavementWidth = 200;
//	var transversalSlope = 20;
//	var longitudinalSlope = 20;
	var red = 0;
	var datosCorrectos = true;
	if(document.forms.rutasForm.selRdb.value == 2){
		disabilityType = 1;
		red = 1;
		if (document.forms.rutasForm.pavementWidth.value == '' || isNaN(document.forms.rutasForm.pavementWidth.value) == true){
			alert("Debe introducir un número válido para la anchura de la acera");
			datosCorrectos = false;
		}
		else{
			if (document.forms.rutasForm.transversalSlope.value == '' ||  isNaN(document.forms.rutasForm.transversalSlope.value) == true){
				alert("Debe introducir un número válido para la pendiente transversal");
				datosCorrectos = false;
			}
			else{
				if (document.forms.rutasForm.longitudinalSlope.value == '' ||  isNaN(document.forms.rutasForm.longitudinalSlope.value) == true){
					alert("Debe introducir un número válido para la pendiente longitudinal");
					datosCorrectos = false;
				}
			}
		}
		
	}
	else{
		disabilityType = 0;
		red = 0;
	}
	
	if(datosCorrectos){
		
		selRdb = document.forms.rutasForm.selRdb.value;
		selRdbInfo = document.forms.rutasForm.selRdbInfo.value;
		
		if(document.getElementById("pavementWidth").value != "")
			widthSideWalk = document.getElementById("pavementWidth").value;
		if(document.getElementById("transversalSlope").value != "")
			transversalSlope = document.getElementById("transversalSlope").value;
		if(document.getElementById("longitudinalSlope").value != "")
			longitudinalSlope = document.getElementById("longitudinalSlope").value;

		 map.destroyPopup(namepopup);
	}
		
	
//	
//	if (document.forms.rutasForm.disability.value=='SI')
//		disabilityType = 1;
//	else
//		disabilityType = 0;

//	if (document.forms.rutasForm.pavementWidth.value != '' && isNaN(document.forms.rutasForm.pavementWidth.value) == true)
//		alert("Debe introducir un número válido para la anchura de la acera");
//	else
//		pavementWidth = document.forms.rutasForm.pavementWidth.value;
	
//	if (document.forms.rutasForm.transversalSlope.value != '' && isNaN(document.forms.rutasForm.transversalSlope.value) == true)
//		alert("Debe introducir un número válido para la pendiente transversal");
//	else
//		transversalSlope = document.forms.rutasForm.transversalSlope.value;
//	
//	if (document.forms.rutasForm.longitudinalSlope.value != '' && isNaN(document.forms.rutasForm.longitudinalSlope.value) == true)
//		alert("Debe introducir un número válido para la pendiente longitudinal");
//	else
//		longitudinalSlope = document.forms.rutasForm.longitudinalSlope.value;
//	
//	var url = "red="+red+"&disabilityType="+disabilityType+"&pavementWidth="+pavementWidth+"&transversalSlope="+transversalSlope+"&longitudinalSlope="+longitudinalSlope;
//	OpenLayers.loadURL("modifyParametersRoute.do?"+url, null, this, this);
//	
//	if((optionSel == 0) || (optionSel == 1))
//	{
//		tipoRuta = "VEHICLE";
//		if(optionSel == 1)tipoRuta = "WALKMAN";
//		else if(optionSel == 2)tipoRuta = "PMR";
//		calcularRuta(0,tipoRuta,true,esRutaSalesMan);
//		
//		
//	}
//	
//	if(optionSel == 2)
//	{
//		var mensajeHtml = "<div id='descRuta'></div>";
//		OpenLayers.LocalgisUtils.showPopup(mensajeHtml);
//		mostrarDescripcion();
//		
//	}
//	if(optionSel == 3)
//	{
//		
//		var requestPrinting = urlPrinting;
//        //Añadimos los parametros a la peticion
//        if (!OpenLayers.String.contains(requestPrinting, "?")) {
//            requestPrinting += "?";
//        } else {
//            requestPrinting += "&";
//        }
//        var layers = map.layers;
//        var center = map.getCenter();
//        var zoom = map.getZoom();
//        requestPrinting += "x="+escape(center.lon);
//        requestPrinting += "&y="+escape(center.lat);
//        requestPrinting += "&zoom="+escape(zoom);
//        
//        var listadoLayers = "";
//        var layers = map.layers;
//        var i = 0;        
//        for(i = 0;i<layers.length;i++) {
//            if (layers[i].visibility && layers[i].inRange && layers[i].disabled != true) {
//            
//              if((!(layers[i] instanceof OpenLayers.Layer.Markers)) &&  (!(layers[i] instanceof OpenLayers.Layer.Vector))){
//            	  if(listadoLayers != "")listadoLayers+=",";
//            	  listadoLayers+= escape(layers[i].idLayer);
//              }
//            }
//        }
//        requestPrinting += "&layers="+listadoLayers;
//        if (map.markersLayer != null && map.markersLayer.visibility) {
//            requestPrinting += "&showMarkers=true";
//        } else {
//            requestPrinting += "&showMarkers=false";
//        }
//        
//        if(arrayRoute.length != 0) requestPrinting += "&markerRouteRequest="+arrayRoute;
//        if(layerVector.features.length != 0) requestPrinting += "&esRuta=true";
//        else requestPrinting += "&esRuta=false";
//        
//        if(layerPolygon.features.length != 0) requestPrinting += "&esArea=true";
//        else requestPrinting += "&esArea=false";
//        map.removeAllPopups();
//        
//        window.open(requestPrinting, "PrintMap", "height=" + height + ",width="+ width + ",scrollbars=yes");//,status=1,toolbar=1");
//	}
//	
//	divButtonRutas.innerHTML = '<img src="'+OpenLayers.Util.getImagesLocation()+'ajax-loader.gif" alt="Calculando"/>';
}
catch(e){
	alert(e);
	
	divButtonRutas.innerHTML = "<img class=\"imageButton\" src=\"img/btn_aceptar.gif\" alt=\"Aceptar\" onClick=\"OpenLayers.Control.RutasLocalgis.calcular('"+urlPrinting+"',"+height+","+width+");\">";
}
}
    
