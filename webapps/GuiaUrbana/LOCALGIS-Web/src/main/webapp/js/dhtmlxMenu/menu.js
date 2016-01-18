var menu;
var menuNode = new Array();
var markerRoute;
var markerInter;
var arrayRoute = new Array();
var arrayInter = new Array();
var ultimoInter = "";
var arrayABC = new Array("A.gif","B.gif","C.gif","D.gif","E.gif","F.gif","G.gif","H.gif","I.gif","J.gif");
var sizeIconRoute;
var offsetIconRoute;
var divPrimaria = "";
var esRuta = false;
var dragMarker,dragPoint;
var wpsProfileReq;
var wpsProfileReq2;
var wpsProfileReqPrint;

var detailRoute = false;
var	isCenterNow = false;
var posicion = -1;
var onDrag = false;
var layerVector;
var layerPoint;
var style_red = null;
var modify = null;
var featureSelect = null;
var style_point = null;
var puntoIntermedio = false;
//var precedence = ["node", "vertex", "edge"];
var precedence = ["vertex", "edge", "node"];
var arrayFeatures = new Array();
var arrayDescription = new Array();
var arrayTempDescripcion = new Array();
var iconSnap = null;
var iconTemp = null;
var lastFeatureSelect = null;
var markerTemporal = null;
var iconInter = null;
var tipoRuta = "VEHICLE";
var PMRType = "0"; //0-Red no PMR, 1 red PMR
var esRutaSalesMan = false;
var layerPolygon=null;
var tipoArea = 1;
var moveInter = false;
var ultimoNodo = null;
//var markerPoint = null;
var positionMouse =null;
var positionNode = null;
var sizeIcon = null;
// features de la capa de la ultima ruta
var routeFeaturesGml;
// Geometria en GML de la ultima ruta
var routeGml;
// Descripcion de la ultima ruta calculada
var cadenaTextoRuta = "";

var widthSideWalk = '1';
var transversalSlope = '2';
var longitudinalSlope = '6'; 	
var selRdb = 0;
var selRdbInfo = 2;
var urlPrintingRoute = null;
var heightRoute = -1;
var widthRoute = -1;
var namepopup = 'PopupRutas';
	
function initMenu(divMap,type) {
	
	for (var numctrl = 0; numctrl < map.controls.length; numctrl++)
	{
	 	if(map.controls[numctrl].CLASS_NAME=="OpenLayers.Control.MousePosition")
     	{
     		positionMouse = map.controls[numctrl];
        	break;
     	}
    }
	
	style_red = OpenLayers.Util.extend({}, OpenLayers.Feature.Vector.style['default']);
    style_red.strokeColor ="#6633CC"; 
    style_red.fillOpacity= 0.75;
    style_red.strokeWidth=8;
    style_red.strokeOpacity=0.75;
    
   
	layerPolygon = new OpenLayers.Layer.Vector("Poligono", {style: style_red});
	layerPolygon.displayInLayerSwitcher = false;
	map.addLayer(layerPolygon);
	
	
	layerVector = new OpenLayers.Layer.Vector("Ruta", {style: style_red});
	layerVector.displayInLayerSwitcher = false;
	
	map.addLayer(layerVector);
	
	
	map.events.register('mousemove', layerVector, handleFeature);
	
	
	
	
	var drawRutaControls = {
		        hover: new OpenLayers.Control.SelectFeature(layerVector,
                {hover: true,
                'outFeature':function(feature){
                lastFeatureSelect=feature;featureSelect=null;/*this.map.div.style.cursor = "default";*/}, 'overFeature': function(feature) {
                featureSelect=feature;/*this.map.div.style.cursor = "pointer";*//*showVertex(feature);*/}})
	};
	map.addControl(drawRutaControls["hover"]); 
    drawRutaControls["hover"].activate();    	
	
	sizeIcon = new OpenLayers.Size(14,14);
	
	offsetIcon = new OpenLayers.Pixel(-(sizeIcon.w/2), -sizeIcon.h);
	
	iconSnap = new OpenLayers.Icon("img/point3.png",sizeIcon,offsetIcon);
	
	iconTemp = new OpenLayers.Icon("img/point.png",sizeIcon,offsetIcon);
	
	iconInter = new OpenLayers.Icon("img/point.png",sizeIcon,offsetIcon);
	
	markerRoute = new OpenLayers.Layer.Markers("Markers");//,{layers: 'Markers'});
	markerRoute.displayInLayerSwitcher = false;
	map.addLayer(markerRoute);
	
	
	layerPoint = new OpenLayers.Layer.Markers("layerPoint");//, {style: style_point});
	layerPoint.displayInLayerSwitcher = false;
	map.addLayer(layerPoint);
	layerPoint.events.register("mousedown", layerPoint, function(evt){
		
						var px = map.getPixelFromLonLat(new OpenLayers.LonLat(0,0));
						layerPoint.markers[0].moveTo(px);
						if(featureSelect == null)featureSelect=lastFeatureSelect;
						if(featureSelect!=null)
						{
							for(var za = 0; za < layerVector.features.length; za++)
							{
								if((layerVector.features[za].id == featureSelect.id)&&(!onDrag))
								{
									
									var Pixel = new OpenLayers.Pixel(evt.xy.x,evt.xy.y);
									var lonlat =  map.getLonLatFromPixel(Pixel);
									//var sizeIcon_ = new OpenLayers.Size(14,14);
									offsetIcon_ = new OpenLayers.Pixel(-(sizeIcon.w/2), -sizeIcon.h);
									var iconTempMarker = new OpenLayers.Icon("img/point.png",sizeIcon,offsetIcon_);
									var marca = new OpenLayers.Marker(lonlat, iconTempMarker);
									markerRoute.addMarker(marca);
									moveInter = true;
									var idDivNode = marca.icon.imageDiv.id;
									ultimoNodo = idDivNode;
									menu.addContextZone(idDivNode);
									
									featureSelect = null;
									lastFeatureSelect = null;
									
									/*if(!_isIE)
									{*/
										arrayRoute.splice(za+1,0,new Object);
										arrayRoute[za+1].id = za+1;
										arrayRoute[za+1].idContext = idDivNode;
										arrayRoute[za+1].lonlat =lonlat;
										arrayRoute[za+1].inter = true;
																			
										for(var y = za+2; y < arrayRoute.length; y++)//el nº de nodos que quedan detrás
										{
											arrayRoute[y].id = parseInt(arrayRoute[y].id) + 1;
										}
									/*}
									else positionNode = za+1;*/
									break;
								}
									
							}
							
						}
						
					}
				);
	
	markerTemporal = new OpenLayers.Layer.Markers("markerTemp");//, {style: style_point});
	markerTemporal.displayInLayerSwitcher = false;
	map.addLayer(markerTemporal);
	
	
	
    divPrimaria = divMap;
	
	menu = new dhtmlXMenuObject();
	menu.setImagePath("imgs/");
	menu.renderAsContextMenu(true);

	if (type==0)
		menu.loadXML("js/dhtmlxMenu/dhtmlxmenu.xml?e="+new Date().getTime());
	else
		menu.loadXML("../js/dhtmlxMenu/dhtmlxmenu.xml?e="+new Date().getTime());
	menu.addContextZone(divMap);
	
	
	
	if (document.addEventListener){ 
		document.getElementById(divMap).addEventListener('click',defaultMouseDown,false);
	//	document.getElementById(divMap).addEventListener('mousemove',defaultMouseDown,false);
	}
	else{
		document.getElementById(divMap).attachEvent('onclick',defaultMouseDown);
	} 
		
	sizeIconRoute = new OpenLayers.Size(24,38);
	
	offsetIconRoute = new OpenLayers.Pixel(-(sizeIconRoute.w/2), -sizeIconRoute.h);
	
	
	
	
	if(!dragMarker)
	{
		dragMarker = new OpenLayers.Control.DragMarker(markerRoute);
		map.addControl(dragMarker);
		dragMarker.activate();
		
		
	}
	
	if(!dragPoint)
	{
		dragPoint = new OpenLayers.Control.DragMarker(markerTemporal);
		map.addControl(dragPoint);
		dragPoint.activate();
		
	}
	
	/*if ((_isIE)&&(/MSIE (\d+\.\d+);/.test(navigator.userAgent)))
	{
		 var ieversion=new Number(RegExp.$1)
		 if (ieversion < 8)
		 {*/
			
			layerPoint.div.style.zIndex = "700";
			markerRoute.div.style.zIndex = "710";
				
		 /*}
	}*/
	
				
	menu.attachEvent("onClick", function(id, zoneId)
	{
		
		//if(!dragMarker.active)dragMarker.activate();
		
        var lonlat = map.getLonLatFromPixel(positionMouse.lastXy);
        
       
		switch(id)
	 	{
	 		
	 		case "origen":
	 			setNodo(lonlat,0);//el valor 0 para el origen
	 			break;
	 		case "destino":
	 			setNodo(lonlat,1);//el valor 1 para el destino
	 			break;
	 		case "adddestino":
	 			setNodo(lonlat,-1);//el valor -1 para un nuevo destino
	 			break;
	 		case "delete":
	 			deleteNode(zoneId);
	 			break;
	 		case "delroute":
	 			deleteRoute();
	 			break;
	 		case "zoomin":
	 			map.zoomIn();
	 			break;
	 		case "zoomout":
	 			map.zoomOut();
	 			break;
	 		case "centrar":
	 			map.setCenter(lonlat, map.getZoom(),false,true);
	 			break;
	 		case "area":
	 			layerPolygon.destroyFeatures();
	 			break;
	 		default:
	 		
	 	}
    });
    
    menu.attachEvent("onBeforeContextMenu", function(zoneId){
    	
    	//if(document.getElementById("Popup"))return false;
    	
	    	if(zoneId != divPrimaria)
	    	{
	    		menu.showItem("delete");
	    		menu.hideItem("origen");
				menu.hideItem("destino");
				menu.hideItem("adddestino");
				menu.hideItem("delroute");
	    	}
	    	else
	    	{
	    		if(esRuta)
	    		{
	    			menu.hideItem("delete");
					menu.hideItem("origen");
					menu.hideItem("destino");
					menu.showItem("adddestino");
					menu.showItem("delroute");
					
				}
				else
				{
					menu.hideItem("delete");
					menu.showItem("origen");
					menu.showItem("destino");
					menu.hideItem("adddestino");
					menu.hideItem("delroute");
				}
				
				if(layerPolygon.features.length != 0)menu.showItem("area");
				else menu.hideItem("area");
				
	    	}
	    	
    	return true;
    	
    });
    
   
}

function defaultMouseDown(e){
	
	if (menu._isContextMenuVisible() && menu.contextAutoHide) { menu._hideContextMenu(); }
	
}
function deleteRoute()
{
	try{
	//	document.getElementById("divRutasTool").className="olControlRutasLocalgisItemInactive";
		arrayRoute = new Array();
		esRuta = false;
		layerVector.destroyFeatures();
		delete routeFeaturesGml;
		delete routeGml;
		markerRoute.clearMarkers();
		arrayFeatures = new Array();
		arrayDescription = new Array();
		arrayInter = new Array();
		layerPolygon.destroyFeatures();
		markerTemporal.clearMarkers();
		layerPoint.events.unregister("mousedown", layerPoint, function(evt){});
		layerPoint.clearMarkers();
		document.getElementById("divAreasTool").className="olControlWithinCostLocalgisItemInactive";
	}catch(e){}
}


function setNodo(lonlat,pos)
{
		if(pos == 0)
		{
			layerPolygon.destroyFeatures();
		}
		try
		{
	

			if(selRdb == 0 ){
				// en la configuracion de las rutas se ha seleccionado ruta en coche o a pie
				PMRType = 0;
			}
			else{
				// en la configuracion de las rutas se ha seleccionado ruta PMR
				PMRType = 1;
			}
			var postParams = "<?xml version='1.0' encoding='UTF-8' standalone='yes'?>";
			postParams += "<wps:Execute service='WPS' version='1.0.0' xmlns:wps='http://www.opengis.net/wps/1.0.0' xmlns:ows='http://www.opengis.net/ows/1.1' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:gml='http://www.opengis.net/gml' xsi:schemaLocation='http://www.opengis.net/wps/1.0.0 ../wpsExecute_request.xsd'>";
			postParams += "<ows:Identifier>com.localgis.wps.algorithm.ClosestMultipleNodeFinder</ows:Identifier>";
			postParams += "<wps:DataInputs><wps:Input><ows:Identifier>data</ows:Identifier><ows:Title>Nodes</ows:Title><ows:Abstract>Ordered Nodes</ows:Abstract><wps:Data><wps:ComplexData>";	
			postParams += "<nodes><node><gml:Point srsName='http://www.opengis.net/gml/srs/epsg.xml#"+srid+"'><gml:coord><gml:X>"+lonlat.lon+"</gml:X><gml:Y>"+lonlat.lat+"</gml:Y></gml:coord></gml:Point></node></nodes>";
		    postParams += "</wps:ComplexData></wps:Data></wps:Input><wps:Input><ows:Identifier>tolerance</ows:Identifier><ows:Title>tolerance</ows:Title><wps:Data> <wps:LiteralData>100</wps:LiteralData></wps:Data></wps:Input>";
		    
		    postParams += "<wps:Input> <ows:Identifier>PMRType</ows:Identifier><ows:Title>PMRType</ows:Title>  <wps:Data><wps:LiteralData>"+PMRType+"</wps:LiteralData> </wps:Data>  </wps:Input>";
		    
	        postParams += "</wps:DataInputs> <wps:ResponseForm> <wps:ResponseDocument storeExecuteResponse='false'>";
	        postParams += "<wps:Output asReference='false'><ows:Identifier>nodes</ows:Identifier></wps:Output></wps:ResponseDocument></wps:ResponseForm></wps:Execute>";	
		  	postParams="urlValor="+ wpsRuta+"&request="+escape(postParams);
		  	
		  	
		  	doWPSProfileQuery(wpsRuta, postParams,false,true,true,pos);//calcularuta,detalleruta,recentrar,posicion de la marca
		 }
		 catch(e){
			 alert("No se ha podido establecer la ruta");
		 }
	
}


function existeDestino(pos)
{
	for(var x = 0; x < arrayRoute.length; x++)
	{
		if(arrayRoute[x].id==pos)
		{
			return true;
			break;
		}
	}
	return false;
}
function doWPSProfileQuery(urlPath, postParams,ruta,detail,center,pos) {
	
	if((wpsProfileReq) && (onDrag))
	{
		timeoutId2= setTimeout( function() { if ( callInProgress(wpsProfileReq) ) {wpsProfileReq.abort(); return false;} }); 
	}
	if(wpsProfileReq) wpsProfileReq.abort();
	
	esRuta = ruta;
//	if(esRuta)	document.getElementById("divRutasTool").className="olControlRutasLocalgisItemActive";
//	else document.getElementById("divRutasTool").className="olControlRutasLocalgisItemInactive";
 	detailRoute = detail;
 	posicion = pos;
			
    if (window.XMLHttpRequest) {
		wpsProfileReq = new XMLHttpRequest();
		wpsProfileReq.onreadystatechange = handlerWhenWPSProfileLoaded;
		wpsProfileReq.open("POST","proxy.do", true);
		wpsProfileReq.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
		wpsProfileReq.send(postParams);
        
    } else if (window.ActiveXObject) {
		wpsProfileReq = new ActiveXObject("Microsoft.XMLHTTP");
		if (wpsProfileReq) {
			wpsProfileReq.onreadystatechange = handlerWhenWPSProfileLoaded;
			wpsProfileReq.open("POST","proxy.do", true);
			wpsProfileReq.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
			wpsProfileReq.send(postParams);
		}
	}
       
	
}


function handlerWhenWPSProfileLoaded() 
{
	
	try 
	{
		if (wpsProfileReq.readyState==4) 
		{
			var idNode = null;
			var networkname = null;
			var pointNet = null;
			var coordX = null;
			var coordY = null
			var pointList = [];
			var cadenaXML = "";
			
			profileURL = wpsProfileReq.responseXML; 
			
			if((arrayRoute.length > 0)&&(arrayRoute[0].id==0))document.getElementById("divAreasTool").className="olControlWithinCostLocalgisItemActive";
			else document.getElementById("divAreasTool").className="olControlWithinCostLocalgisItemInactive";
			
			
			if(arrayRoute.length >= 3)document.getElementById("divSalesManTool").className="olControlSalesManLocalgisItemActive";
			else document.getElementById("divSalesManTool").className="olControlSalesManLocalgisItemInactive";
			
			if (!profileURL || wpsProfileReq.fileType!="XML") {
		    	profileURL = wpsProfileReq.responseText;
		    }
        	if (typeof profileURL == "string") { 
           		profileURL = OpenLayers.parseXMLString(profileURL);
        	}
        	
        	if(esRuta){
        		try {
            		// XMLSerializer exists in current Mozilla browsers
            		serializer = new XMLSerializer();
            		routeGml = serializer.serializeToString(profileURL);
            	} 
            	catch (e) {
            		// Internet Explorer has a different approach to serializing XML
            		serialized = profileURL.xml;
            	}
        		layerVector.destroyFeatures();
                
	        	var fileGml = new OpenLayers.Format.GML();
				routeFeaturesGml = fileGml.read(profileURL);
				var mensajeTexto = "";
				cadenaTextoRuta = "";
				if(routeFeaturesGml) 
				{
					if(routeFeaturesGml.constructor != Array) 
					{
						routeFeaturesGml = [routeFeaturesGml];
					}
					layerVector.addFeatures(routeFeaturesGml);
				}
				
				var root =  profileURL.childNodes[0];
				if (root.childNodes.length == 0){
                                	alert("No se ha encontrado la ruta");
				}
				for(var tpm = 0; tpm < root.childNodes.length;tpm++)
	        	{
					if(root.childNodes[tpm].nodeName=="descripcion")
					{
						var nodoDesc = root.childNodes[tpm];
						for(var dcs = 0; dcs < nodoDesc.childNodes.length;dcs++)
			        	{
							for(var attr_nodo = 0; attr_nodo < nodoDesc.childNodes[dcs].attributes.length; attr_nodo++)
							{
								if(nodoDesc.childNodes[dcs].attributes[attr_nodo].nodeName == "id")
								{
									var icono_attr = parseInt(nodoDesc.childNodes[dcs].attributes[attr_nodo].nodeValue);
									
									for(var mkr = 0; mkr < markerRoute.markers.length; mkr++)
									{
				        				if(markerRoute.markers[mkr].icon.imageDiv.id == arrayRoute[icono_attr].idContext)
										{
				        					var icon_url = markerRoute.markers[mkr].icon.url;
				        					mensajeTexto = "<img src='"+contextPathPrint+"/"+icon_url+"'/>"
				        					break;
										}
									}
									//mensajeTexto = "<img src='img/ruta/"+arrayABC[icono_attr]+"'/>"
								}
							}
							for(var asx = 0; asx < nodoDesc.childNodes[dcs].childNodes.length;asx++)
				        	{
								cadenaTextoRuta += "<tr>"
								if(asx == 0)
								{
									cadenaTextoRuta += "<td>" + mensajeTexto + "</td>";
								}
								else if((asx == nodoDesc.childNodes[dcs].childNodes.length - 1)&&(dcs == nodoDesc.childNodes.length - 1)){
									var idlast_route = arrayRoute[arrayRoute.length-1].idContext;
									var icon_find = false;
									for(var mkr = 0; mkr < markerRoute.markers.length; mkr++)
									{
				        				if(markerRoute.markers[mkr].icon.imageDiv.id == idlast_route)
										{
				        					var icon_url = markerRoute.markers[mkr].icon.url;
				        					icon_find = true;
				        					cadenaTextoRuta += "<td><img src='"+contextPathPrint+"/"+icon_url+"'/></td>";
				        					break;
										}
									}
									if(!icon_find)cadenaTextoRuta += "<td> </td>";
									//cadenaTextoRuta += "<td><img src='img/ruta/"+arrayABC[icono_attr+1]+"'/></td>";
								}
								else cadenaTextoRuta += "<td> </td>";
								cadenaTextoRuta += "<td>" +nodoDesc.childNodes[dcs].childNodes[asx].firstChild.nodeValue + "</td>";
								cadenaTextoRuta += "</tr>";
				        	}
							
			        	}
					}
					
	        	}
				map.removeAllPopups();
				
				if(esRuta){
					if(selRdbInfo == 0 ){
						//si esta activado en el configurador de rutas -ver la detalle de la ruta se muestra
						// en un popup la descripcion 
						OpenLayers.Control.RutasLocalgis.showDescriptionRoute();
		    		}
					else if(selRdbInfo == 1){
						//si esta activado en el configurador de rutas -imprimir ruta
						OpenLayers.Control.RutasLocalgis.printRoute();
	
		        	}
        		}
        	}	
        	else
        	{
        	
        		for(var tpm = 0; tpm < profileURL.childNodes.length;tpm++)
	        	{
        			
	        		if(profileURL.childNodes[tpm].nodeName == "ns:ExceptionReport")
	        		{
	        			
	        			if((posicion != -1) && (posicion <= arrayRoute.length -1) && (!onDrag))
	        			{ 
		        			for(var mkr = 0; mkr < markerRoute.markers.length; mkr++)
							{
		        				if(markerRoute.markers[mkr].icon.imageDiv.id == arrayRoute[posicion].idContext)
								{
		        					if((arrayRoute[posicion].lonlat)&&(arrayRoute[posicion].lonlat!="undefined"))
								  	{
		        						var lonlat = arrayRoute[posicion].lonlat;
									  	var px = map.getPixelFromLonLat(lonlat);
									  	markerRoute.markers[mkr].moveTo(px);	
									}
									else 
									{
										arrayRoute.splice(posicion,1);
									  	markerRoute.removeMarker(markerRoute.markers[mkr]);
									}
									break;
								  }
							}
						}
						if(arrayRoute.length >= 2)esRuta=true;
	        				
	        		}
	        		if(profileURL.childNodes[tpm].nodeName == "ns:ExecuteResponse")
	        		{
	        			var featureResponse = profileURL.childNodes[tpm].childNodes;
	        			for(var y = 0; y < featureResponse.length; y++)
	        			{
	        			
	        				if(featureResponse[y].nodeName == "ns:ProcessOutputs")
	        				{
	        					
	        					for(var po = 0; po < featureResponse[y].childNodes.length; po++)
	        					{
	        						var nodoRaiz = featureResponse[y].childNodes[po];
	        						
	        						if(nodoRaiz.nodeName == "ns:Output")
		        					{
		        						
		        						var nodoOutput = nodoRaiz.childNodes;
		        						for(var xy = 0; xy<nodoOutput.length;xy++)
		        						{
		        							if(nodoOutput[xy].nodeName == "ns:Data")
		        							{
		        								var nodoData = nodoOutput[xy].childNodes;;
		        								for(var xz = 0; xz<nodoData.length;xz++)
		        								{
		        									if(nodoData[xz].nodeName == "ns:ComplexData")
		        									{
		        										var nodoComplexData = nodoData[xz];
		        										
		        										if(nodoComplexData.childNodes.length > 0)
														{
															var nodes = nodoComplexData.childNodes;
	        											
		        											for(var xa = 0; xa<nodes.length;xa++)
		        											{
		        												var node = nodes[xa].childNodes;
		        												for(var xb = 0; xb < node.length; xb++)
		        												{
		        													var node_xb = node[xb].childNodes;
		        													cadenaXML += "<node>";
		        													for(var xc = 0; xc < node_xb.length; xc++)
		        													{
		        														if(node_xb[xc].nodeName == "id"){
		        															idNode = node_xb[xc].childNodes[0].nodeValue;
		        															cadenaXML += "<id>"+idNode+"</id>";
		        														}
		        														if(node_xb[xc].nodeName == "networkName"){
		        															networkname = node_xb[xc].childNodes[0].nodeValue;
		        															cadenaXML += "<networkName>"+networkname+"</networkName>";
		        														}
		        														if(node_xb[xc].nodeName == "gml:Point")
		        														{
		        															cadenaXML += "<gml:Point";
		        															var attrPoint = node_xb[xc].attributes;
		        															for(var at = 0; at < attrPoint.length; at++)
		        															{
		        																cadenaXML += " " + attrPoint[at].nodeName+"='"+attrPoint[at].nodeValue+"'";
		        															}
		        															cadenaXML += ">";
		        															var gmlPoint = node_xb[xc].childNodes;
		        															for(var xd = 0;xd < gmlPoint.length; xd++)
		        															{
		        																if(gmlPoint[xd].nodeName == "gml:coord")
		        																{
		        																	if(gmlPoint[xd].childNodes.length == 2)
		        																	{
		        																		coordX =  gmlPoint[xd].childNodes[0].childNodes[0].nodeValue;
		        																		coordY =  gmlPoint[xd].childNodes[1].childNodes[0].nodeValue;
		        																		
		        																		cadenaXML +="<gml:coord><gml:X>"+coordX+"</gml:X><gml:Y>"+coordY+"</gml:Y></gml:coord>";
		        																		cadenaXML +="</gml:Point>";
		        																		var lonlat = new OpenLayers.LonLat(coordX,coordY);
														        						
														        						var existePosition = false;
														        						//if(ultimoInter == posicion)alert(ultimoInter);
																						if(posicion == -1)posicion = arrayRoute.length;
																							
																						if(posicion < arrayABC.length)
																						{	
																							var indiceArray = posicion;//posicion - 1;
																							if(posicion == 0)indiceArray=0;
																							if((arrayRoute.length == 1)&&(posicion == 1))indiceArray=0;
																							if(posicion != -1)existePosition = existeDestino(posicion);//si está establecido ya el origen
																							if(!existePosition){
																								var intermedios = new Array();
																								var newPos = posicion;
																								//arrayInter = new Array();
																								for (i=arrayRoute.length-1;i>=0;i--) {
																									if(arrayRoute[i].inter)
																									{	//arrayInter.push(arrayRoute[i]);
																										newPos = newPos - 1;
																									}
																									
																								}
																								
																								var nombreIcono = arrayABC[newPos];
																								var icon = new OpenLayers.Icon("img/ruta/"+nombreIcono,sizeIconRoute,offsetIconRoute);
																								var marca = new OpenLayers.Marker(lonlat, icon);
																								markerRoute.addMarker(marca);
																								var idDivNode = marca.icon.imageDiv.id;
																								menu.addContextZone(idDivNode);
																								
																								if((posicion == 0) && (arrayRoute.length != 0)){arrayRoute.splice(0,0,new Object());indiceArray=0;}
																								else{
																									
																									arrayRoute.push(new Object());
																									indiceArray = arrayRoute.length - 1;
																								}
																								
																								
																							}
																							else {
																							try{
																								idDivNode=arrayRoute[indiceArray].idContext;
																								
																								for(var z = 0; z < markerRoute.markers.length; z++)
																								{
																									if(markerRoute.markers[z].icon.imageDiv.id == idDivNode)
																									{
																										markerRoute.markers[z].lonlat = lonlat;
																										var px = map.getPixelFromLonLat(lonlat);
																										markerRoute.markers[z].moveTo(px);
																										
																										break;
																									}
																								}
																								
																							}catch(e){alert(e);}
																								
																							}
																							
																							arrayRoute[indiceArray].id = posicion;
																							arrayRoute[indiceArray].idContext = idDivNode;
																							arrayRoute[indiceArray].lonlat = lonlat;
																							
																							
																						  	if(posicion == 0)
																						  	{
																						  		document.getElementById("divAreasTool").className="olControlWithinCostLocalgisItemActive";
																						  	}
																						  	
																						}
																						else alert("No se pueden añadir más destinos a la ruta");
		        																	}
		        																}
		        															}
		        														}
		        													}
		        													cadenaXML += "</node>";
		        												}
		        												
		        												
		        											}
		        											if (typeof indiceArray == "undefined")
	        											    {
		        												alert("No se ha encontrado un vial cerca de este punto");
		        												return;
	        											    } 
	        												arrayRoute[indiceArray].xml = cadenaXML;
	        												
	        												if(arrayRoute.length >= 2)
	        												{
	        													esRuta=true;
	        													if(selRdb == 0){
	        														tipoRuta = 'VEHICLE';
	        														
	        													}
	        													else{
	        														tipoRuta = 'WALKMAN';
	        													}
	        													calcularRuta(posicion,tipoRuta,false,false);
	        												}
		        										  
		        											
	        											}
	        										
		        									}//ns:complexdata
		        										
		        								}//for ns:data
		        									
		        							}//ns:data
		        						}//for nodooutput
		        						
		        						
		        					}//if ns:ouotput
		        					
		        				}//for process
	        					
	        				}//if process
	        			}//featureresponse
	        			
	        		}//executeresponse
	        	
	        	}//for childnodes	
	        }//else
        }//readystate*/
		
        	
		
	}	catch(e){alert(e);map.removeAllPopups();}
}



function calcularRuta(posicion,tipoRuta,recalcular,viajante)
{
	for(var x = 0; x < arrayRoute.length; x++)
	{
		if((x == posicion - 1)||(x == posicion)||(x==posicion + 1))
		{
			if(!arrayRoute[x].xml){
				
				deleteNode(arrayRoute[x].idContext);
				break;
			}
		}
	}
	
	
	if((tipoRuta == null)||(tipoRuta == ""))
	{
		tipoRuta = "VEHICLE";
	}
	if(recalcular){
		var arrayTempRoute = arrayRoute;
		deleteRoute();
		arrayRoute = arrayTempRoute;
		
	}
	try{
		/*if(document.getElementById("descRuta")){
			 var contentHTML = '<table style="margin-left:auto;margin-right:auto;text-align:center">';
		    contentHTML += '<tr>';
		    contentHTML += '<td align="center" colspan="2"><div id="divButtonSearchPlaceName"><img src="'+OpenLayers.Util.getImagesLocation()+'ajax-loader.gif" alt="Buscando"/></div></td>';
		    contentHTML += '</tr>';
		    contentHTML += '</table>';
			document.getElementById("descRuta").innerHTML = contentHTML;
			}
		else*/ 
		OpenLayers.LocalgisUtils.showSearchingPopup();
		
		if((arrayRoute.length > 0)&&(arrayRoute[0].id==0))document.getElementById("divAreasTool").className="olControlWithinCostLocalgisItemActive";
		//Solo se calcula la ruta entre un punto de origen y otro de destino
		var postParams = "<?xml version='1.0' encoding='UTF-8' standalone='yes'?>";
		postParams += "<wps:Execute service='WPS' version='1.0.0' xmlns:wps='http://www.opengis.net/wps/1.0.0' xmlns:ows='http://www.opengis.net/ows/1.1' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:gml='http://www.opengis.net/gml' xsi:schemaLocation='http://www.opengis.net/wps/1.0.0 ../wpsExecute_request.xsd'>";
		if(viajante){
			esRutaSalesMan = true;
			postParams += "<ows:Identifier>com.localgis.wps.algorithm.TravelerSalesmanRouteFinder</ows:Identifier>";
		}
		else{
			esRutaSalesMan = false;
			postParams += "<ows:Identifier>com.localgis.wps.algorithm.AStarRouteFinder</ows:Identifier>";
		}
			
		postParams += "<wps:DataInputs><wps:Input><ows:Identifier>data</ows:Identifier><ows:Title>Nodes</ows:Title><ows:Abstract>Ordered Nodes</ows:Abstract><wps:Data><wps:ComplexData>";	
		postParams += "<nodes>";	
		
		for(var x = 0; x < arrayRoute.length; x++)
		{
			if(recalcular)
			{
				var nombreIcono = arrayABC[x];
				var icon = new OpenLayers.Icon("img/ruta/"+nombreIcono,sizeIconRoute,offsetIconRoute);
				var marca = new OpenLayers.Marker(arrayRoute[x].lonlat, icon);
				markerRoute.addMarker(marca);
				var idDivNode = marca.icon.imageDiv.id;
				arrayRoute[x].idContext=idDivNode;
				menu.addContextZone(idDivNode);
				postParams +=arrayRoute[x].xml;
			}
			else
			{
				/*if((arrayRoute[x].id == posicion -1)||(arrayRoute[x].id == posicion)||(arrayRoute[x].id == posicion + 1))
				{*/
					if((arrayRoute[x].xml)&&(arrayRoute[x].xml!=""))
					postParams +=arrayRoute[x].xml;
				//}
			}
		}
		
		if(selRdb == 0 ){
			// en la configuracion de las rutas se ha seleccionado ruta en coche o a pie
			PMRType = 0;
		}
		else{
			// en la configuracion de las rutas se ha seleccionado ruta PMR
			PMRType = 1;
		}
	
		postParams += "</nodes>";
		postParams += "</wps:ComplexData></wps:Data></wps:Input><wps:Input><ows:Identifier>tolerance</ows:Identifier><ows:Title>tolerance</ows:Title>";
	    postParams += "<wps:Data> <wps:LiteralData>100</wps:LiteralData></wps:Data></wps:Input><wps:Input> <ows:Identifier>pathType</ows:Identifier><ows:Title>pathType</ows:Title>  <wps:Data><wps:LiteralData>"+tipoRuta+"</wps:LiteralData> </wps:Data>  </wps:Input>";
	    
	    postParams += "<wps:Input> <ows:Identifier>PMRType</ows:Identifier><ows:Title>PMRType</ows:Title>  <wps:Data><wps:LiteralData>"+PMRType+"</wps:LiteralData> </wps:Data>  </wps:Input>";
	    postParams += "<wps:Input> <ows:Identifier>pavementWidth</ows:Identifier><ows:Title>pavementWidth</ows:Title>  <wps:Data><wps:LiteralData>"+widthSideWalk+"</wps:LiteralData> </wps:Data>  </wps:Input>";
	    postParams += "<wps:Input> <ows:Identifier>transversalSlope</ows:Identifier><ows:Title>transversalSlope</ows:Title>  <wps:Data><wps:LiteralData>"+transversalSlope+"</wps:LiteralData> </wps:Data>  </wps:Input>";
	    postParams += "<wps:Input> <ows:Identifier>longitudinalSlope</ows:Identifier><ows:Title>longitudinalSlope</ows:Title>  <wps:Data><wps:LiteralData>"+longitudinalSlope+"</wps:LiteralData> </wps:Data>  </wps:Input>";
	    
	    if(viajante)postParams += "<wps:Input> <ows:Identifier>ciclico</ows:Identifier><ows:Title>ciclico</ows:Title>  <wps:Data><wps:LiteralData>false</wps:LiteralData> </wps:Data>  </wps:Input>";
	    postParams += "</wps:DataInputs>";
	    postParams += "<wps:ResponseForm> <wps:ResponseDocument storeExecuteResponse='false'>";
	    postParams += "<wps:Output asReference='false'><ows:Identifier>result</ows:Identifier></wps:Output><wps:Output asReference='false'><ows:Identifier>description</ows:Identifier></wps:Output><wps:Output asReference='false'><ows:Identifier>nodes</ows:Identifier></wps:Output></wps:ResponseDocument></wps:ResponseForm></wps:Execute>";	
		postParams="urlValor="+ wpsRuta+"&request="+escape(postParams)+"&get_ruta=1";
		
		doWPSProfileQuery(wpsRuta, postParams,true,true,true,posicion);//calcularuta,detalleruta,recentrar,posicion de la marca
		

		
	}catch(e){map.removeAllPopups();}
	  	
}	
function callInProgress(xmlhttp) {
    switch ( xmlhttp.readyState ) {
        case 1, 2, 3:
            return true;
        break;

        // Case 4 and 0
        default:
            return false;
        break;
    }
}

 function showVertex(feature)
 {
 	
 //	alert("feature: " + feature + " evt: " + evt);
 	
 	
 }

 function handleFeature(evt)
 {
	//if(moveInter)alert("ha creado un punto: " + arrayRoute.length);
	
	if(moveInter)setTimeout("ieNode('"+ultimoNodo+"')",1000)
		
	/*if((!_isIE)&&(moveInter))
	{
		deleteNode(ultimoNodo);
	}else {
		if(moveInter)setTimeout("ieNode('"+ultimoNodo+"')",1500)
		moveInter = false;
	}*/
 	if((featureSelect!=null)&&(!onDrag)&&(!moveInter))
 	{
 		
 		var lonlat = map.getLonLatFromViewPortPx(evt.xy);
 		var point = new OpenLayers.Geometry.Point(lonlat.lon, lonlat.lat);
 		
      	considerSnapping(point, point);
      
      	//if(!moveInter)markerTemporal.clearMarkers();
    	    
 	}
 	else
 	{
 		if(!onDrag){markerTemporal.clearMarkers();}
 		if(layerPoint.markers.length != 0)
 		{
 			
 			var px = map.getPixelFromLonLat(new OpenLayers.LonLat(0,0));
			layerPoint.markers[0].moveTo(px);
		
		}
 	}
 		
 	/*if(moveInter)
	{
		
		deleteNode(ultimoNodo);
		
	}*/
		/*document.getElementById("divDrag").style.left =  "0px";
		document.getElementById("divDrag").style.top = "0px";*/
		
		//}
 		//layerPoint.clearMarkers();
 	//}
 	
 	
 }
 
 function considerSnapping(point, loc) {
 
        var best = {
            rank: Number.POSITIVE_INFINITY,
            dist: Number.POSITIVE_INFINITY,
            x: null, y: null
        };
        var snapped = false;
       
        var result, target;
        
           target = layerVector;
            result = testTarget( loc);
            if(result) {
            	var greedy = true;
                if(greedy) {
                    best = result;
                    best.target = target; 
                    snapped = true;
                    
                } else {
                
                    if((result.rank < best.rank) ||
                       (result.rank === best.rank && result.dist < best.dist)) {
                      
                        best = result;
                        best.target = target;
                        snapped = true;
                    }
                }
            }
      
        if(snapped) {
        
            var proceed = {
                point: point, x: best.x, y: best.y, distance: best.dist,
                layer: best.target.layer, snapType: precedence[best.rank]
            };
          
            if(proceed !== false) {
                point.x = best.x;
                point.y = best.y;
            	
                /*map.events.register("snap", {
                    point: point,
                    snapType: precedence[best.rank],
                    layer: best.target.layer,
                    distance: best.dist
                });*/
            } else {
                snapped = false;
            }
        }
        if(point && !snapped) {
            point.x = loc.x;
            point.y = loc.y;
           
            /*var lonlat = new OpenLayers.LonLat(point.x,point.y)
            var px = map.getPixelFromLonLat(lonlat);*/
             
            /*document.getElementById("divDrag").style.left = px.x + "px";// - 5;
			document.getElementById("divDrag").style.top = px.y + "px";*/ 
            //var lonLat = new OpenLayers.LonLat(point.x,point.y);
            /*if(layerPolygon.features.length == 0)
            {
            	var point = new OpenLayers.Geometry.Point(lonLat.lon, lonLat.lat);
            	var pointFeature = new OpenLayers.Feature.Vector(point);
            	layerPolygon.addFeatures(pointFeature);
            	layerPolygon.features[0].createMarker();
            }
            else
            {
            	var px = map.getPixelFromLonLat(lonLat);
            	layerPolygon.features[0].geometry.x = px.x;
            	layerPolygon.features[0].geometry.y = px.y;
            }*/
           
            
            if(layerPoint.markers.length == 0)
            {
            	var marcaPunto = new OpenLayers.Marker(new OpenLayers.LonLat(point.x,point.y),iconSnap);
				layerPoint.addMarker(marcaPunto);
				
				
				
			}
			else
			{
				
				var px = map.getPixelFromLonLat(new OpenLayers.LonLat(point.x,point.y));
				layerPoint.markers[0].moveTo(px);
				//if(!layerPoint.getVisibility())layerPoint.setVisibility(true);
			}
			
			
			
			/*var lonLat = new OpenLayers.LonLat(point.x,point.y);
			var point = new OpenLayers.Geometry.Point(lonLat.lon, lonLat.lat);
            var pointFeature = new OpenLayers.Feature.Vector(point);//,null,style_blue);
			
			
			layerVectorTemp.addFeatures(pointFeature);*/
			
			/*document.getElementById("dragCursor").style.left = px.x + "px";
			document.getElementById("dragCursor").style.top = px.y + "px";*/
		
			
			
																										
			//map.events.register("onclick",marca,function(e){alert("click en marca");});
			//if(!dragPoint.active)dragPoint.activate();
        }
    }
    
    function testTarget(loc) {
        var tolerance = {
            node: getGeoTolerance(1),
            vertex:getGeoTolerance(1),
            edge: getGeoTolerance(1)
        };
        // this could be cached if we don't support setting tolerance values directly
        var maxTolerance = Math.max(
            tolerance.node, tolerance.vertex, tolerance.edge
        );
      
        var result = {
            rank: Number.POSITIVE_INFINITY, dist: Number.POSITIVE_INFINITY
        };
        var eligible = false;
        var features = layerVector.features;
        var feature, type, vertices, vertex, closest, dist, found;
        var numTypes = precedence.length;
        var ll = new OpenLayers.LonLat(loc.x, loc.y);
        for(var i=0, len=features.length; i<len; ++i) {
            feature = features[i];
            if(feature !== this.feature && !feature._sketch &&
               feature.state !== OpenLayers.State.DELETE ){/*&&
               (!target.filter || target.filter.evaluate(feature.attributes))) {*/
                if(feature.atPoint(ll, maxTolerance, maxTolerance)) {
                	
                    for(var j=0, stop=Math.min(result.rank+1, numTypes); j<stop; ++j) {
                        type = precedence[j];
                        //var typeTarget = "";
                        if(type) {
                            if(type === "edge") {
                                closest = distanceTo(feature.geometry,loc, {details: true});
                                dist = closest.distance;
                                if(dist <= tolerance[type] && dist < result.dist) {
                                    result = {
                                        rank: j, dist: dist,
                                        x: closest.x0, y: closest.y0 // closest coords on feature
                                    };
                                    eligible = true;
                                    // don't look for lower precedence types for this feature
                                    break;
                                }
                            } else {
                                // look for nodes or vertices
                                vertices = getVertices(type === "node",feature.geometry);
                                found = false;
                                for(var k=0, klen=vertices.length; k<klen; ++k) {
                                    vertex = vertices[k];
                                    dist = vertex.distanceTo(loc);
                                    if(dist <= tolerance[type] &&
                                       (j < result.rank || (j === result.rank && dist < result.dist))) {
                                        result = {
                                            rank: j, dist: dist,
                                            x: vertex.x, y: vertex.y
                                        };
                                        eligible = true;
                                        found = true;
                                    }
                                }
                                if(found) {
                                    // don't look for lower precedence types for this feature
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        return eligible ? result : null;
    }
    
    /**
     * Method: getGeoTolerance
     * Calculate a tolerance in map units given a tolerance in pixels.  This
     *     takes advantage of the <geoToleranceCache> when the map resolution
     *     has not changed.
     *     
     * Parameters:
     * tolerance - {Number} A tolerance value in pixels.
     *
     * Returns:
     * {Number} A tolerance value in map units.
     */
    function getGeoTolerance(tolerance) {
        var resolution = layerVector.map.getResolution();
        
        if(resolution !== this.resolution) {
            this.resolution = resolution;
            this.geoToleranceCache = {};
        }
        var geoTolerance = this.geoToleranceCache[tolerance];
        if(geoTolerance === undefined) {
            geoTolerance = tolerance * resolution;
            this.geoToleranceCache[tolerance] = geoTolerance;
        }
       
        return geoTolerance;
    }
    
    function getVertices (nodes,geom) {
        return (nodes === true) ? [] : geom.components.slice(0, geom.components.length-1);
    }
    
    function distanceTo(feature,geometry, options) {
    	
        var edge = !(options && options.edge === false);
        var details = edge && options && options.details;
        var result, best;
        var min = Number.POSITIVE_INFINITY;
        for(var i=0, len=feature.components.length; i<len; ++i) {
            result = distanceToPoint(feature.components[i],geometry, options);
            distance = details ? result.distance : result;
            if(distance < min) {
                min = distance;
                best = result;
                if(min == 0) {
                    break;
                }
            }
        }
        
        return best;
    }
    
    function distanceToPoint(geometry,oldgeom,options) {
        var edge = !(options && options.edge === false);
        var details = edge && options && options.details;
        var distance, x0, y0, x1, y1, result;
        if(geometry instanceof OpenLayers.Geometry.Point) {
            x0 = oldgeom.x;
            y0 = oldgeom.y;
            x1 = geometry.x;
            y1 = geometry.y;
            distance = Math.sqrt(Math.pow(x0 - x1, 2) + Math.pow(y0 - y1, 2));
            result = !details ?
                distance : {x0: x0, y0: y0, x1: x1, y1: y1, distance: distance};
        } else {
            result = geometry.distanceTo(this, options);
            if(details) {
                // switch coord order since this geom is target
                result = {
                    x0: result.x1, y0: result.y1,
                    x1: result.x0, y1: result.y0,
                    distance: result.distance
                };
            }
        }
      
        return result;
    }
    
    function mostrarDescripcion()
    {
    	if(document.getElementById("descRuta"))
		{
			var mensajeHtml = "<div id='descRuta'><table width=100% style='text-align:left;'>";
			mensajeHtml += cadenaTextoRuta; 
			/*for(var aD = 0; aD < arrayDescription.length; aD++)
			{
				textoPuntoRuta = "Llegada";
				if(aD==0)textoPuntoRuta = "Salida";
				mensajeHtml +="<tr><td align='left'><img src='img/ruta/"+arrayABC[aD]+"' width='12' height='19'/>"+textoPuntoRuta+"</td></tr>";
				for(var aT = 0; aT < arrayDescription[aD].length; aT++)
				{
					mensajeHtml+="<tr><td align='left'><ul><li>"+arrayDescription[aD][aT].orientation + " <strong>" +arrayDescription[aD][aT].calle + "</strong> " + arrayDescription[aD][aT].longitud+"</li></ul></td></tr>";
				}
				
				if(aD == arrayDescription.length - 1)
				{
					mensajeHtml +="<tr><td align='left'><img src='img/ruta/"+arrayABC[aD+1]+"' width='12' height='19'/>Llegada</td></tr>";
				}	
			}*/
			mensajeHtml+="</table></div>";
			document.getElementById("descRuta").innerHTML = mensajeHtml;
		}
    }
    
    function calcularArea(lonlat,valorcoste,tipo)
    {
    	
    	try
		{
    		
    		var tipoCalcularArea = 2;
    		if(tipo == 1)tipoCalcularArea=1;
    		
			var postParams = "<?xml version='1.0' encoding='UTF-8' standalone='yes'?>";
			postParams += "<wps:Execute service='WPS' version='1.0.0' xmlns:wps='http://www.opengis.net/wps/1.0.0' xmlns:ows='http://www.opengis.net/ows/1.1' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:gml='http://www.opengis.net/gml' xsi:schemaLocation='http://www.opengis.net/wps/1.0.0 ../wpsExecute_request.xsd'>";
			postParams += "<ows:Identifier>com.localgis.wps.algorithm.WithinCostRouteFinder</ows:Identifier>";
			postParams += "<wps:DataInputs><wps:Input><ows:Identifier>data</ows:Identifier><ows:Title>Nodes</ows:Title><ows:Abstract>Ordered Nodes</ows:Abstract><wps:Data><wps:ComplexData>";	
			postParams += "<node><id></id><gml:Point srsName='http://www.opengis.net/gml/srs/epsg.xml#"+srid+"'><gml:coord><gml:X>"+lonlat.lon+"</gml:X><gml:Y>"+lonlat.lat+"</gml:Y></gml:coord></gml:Point></node>";
		    postParams += "</wps:ComplexData></wps:Data></wps:Input><wps:Input><ows:Identifier>tolerance</ows:Identifier><ows:Title>tolerance</ows:Title>";
	        postParams += "<wps:Data> <wps:LiteralData>100</wps:LiteralData></wps:Data></wps:Input><wps:Input><ows:Identifier>cost</ows:Identifier><ows:Title>cost</ows:Title>";
	        postParams += "<wps:Data> <wps:LiteralData>"+valorcoste+"</wps:LiteralData></wps:Data></wps:Input></wps:DataInputs> <wps:ResponseForm> <wps:ResponseDocument storeExecuteResponse='false'>";
	        postParams += "<wps:Output asReference='false'><ows:Identifier>result</ows:Identifier></wps:Output><wps:Output asReference='false'><ows:Identifier>nodes</ows:Identifier></wps:Output>";
	        if(tipo == 1)postParams += "<wps:Output asReference='false'><ows:Identifier>routes</ows:Identifier></wps:Output>";
	        postParams += "</wps:ResponseDocument></wps:ResponseForm></wps:Execute>";	
		  	postParams="urlValor="+ wpsRuta+"&request="+escape(postParams)+"&get_ruta="+tipoCalcularArea;
		  	doWPSWithinCost(wpsRuta,postParams,tipo);
		  	
		 }
		 catch(e){map.removeAllPopups();}
    }
    
  function doWPSWithinCost(urlPath, postParams,tipo) {
   
   	tipoArea = tipo;
   	if(tipoArea == 1){
//   		if(dragMarker.active)dragMarker.deactivate();
   		nodoOrigen = arrayRoute[0].lonlat;
   		deleteRoute();
   		
   		
   	}
   	if(wpsProfileReq2)
	{
		timeoutId2= setTimeout( function() { if ( callInProgress(wpsProfileReq2) ) {wpsProfileReq2.abort(); return false;} }); 
	}
	if(wpsProfileReq2) wpsProfileReq2.abort();
	
	if (window.XMLHttpRequest) {
		wpsProfileReq2 = new XMLHttpRequest();
		wpsProfileReq2.onreadystatechange = handlerWhenWPSLoaded;
		wpsProfileReq2.open("POST","proxy.do", true);
		wpsProfileReq2.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
		wpsProfileReq2.send(postParams);
        
    } else if (window.ActiveXObject) {
		wpsProfileReq2 = new ActiveXObject("Microsoft.XMLHTTP");
		if (wpsProfileReq2) {
			wpsProfileReq2.onreadystatechange = handlerWhenWPSLoaded;
			wpsProfileReq2.open("POST","proxy.do", true);
			wpsProfileReq2.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
			wpsProfileReq2.send(postParams);
		}
	}
   }
function handlerWhenWPSLoaded()
{
	try{	
		if (wpsProfileReq2.readyState==4) 
		{
			
			layerPolygon.destroyFeatures();
			profileURL = wpsProfileReq2.responseXML; 
			
			var boundsPolygon = null;
			if (!profileURL || wpsProfileReq2.fileType!="XML") 
			{
		    	profileURL = wpsProfileReq2.responseText;
		    }
	       	if (typeof profileURL == "string") 
	       	{ 
	          		profileURL = OpenLayers.parseXMLString(profileURL);
	       	}
	       	var fileGml = new OpenLayers.Format.GML();
			var featuresGml = fileGml.read(profileURL);
			if(featuresGml) 
			{
				if(featuresGml.constructor != Array) 
				{
					featuresGml = [featuresGml];
				}
				layerPolygon.addFeatures(featuresGml);
			}
			
			map.removeAllPopups();
			if(layerPolygon.features.length != 0){
				map.zoomToExtent(layerPolygon.getDataExtent());
				map.zoomTo(map.getZoom() - 1);
			}
			else{alert("Se ha producido un error al calcular el área de influencia");}
		}
	}catch(e){
                alert("No se ha podido calcular la ruta");	
		alert(e);
		map.removeAllPopups();
	}
}
	

   
function ieNode(ultimoNodo)
  {
  	//moveInter = false;
  	if(moveInter)
  	{
	  	for(var x = 0; x < markerRoute.markers.length; x++)
		{
			if(markerRoute.markers[x].icon.imageDiv.id == ultimoNodo)
			{
				nodo = markerRoute.markers[x];
				var nombreNodo = markerRoute.markers[x].icon.imageDiv.id;
				markerRoute.removeMarker(nodo);
				moveInter = false;
				break;
			}
		}
	}
  	
  }
  
  function deleteNode(zoneId)
  {
	 
  	arrayInter = new Array();
  	posicionNodo = posicion_nodo(zoneId);
  	if(posicionNodo!=null)
  	{
  		
  		for(var mkr = 0; mkr < markerRoute.markers.length; mkr++)
		{
			if(markerRoute.markers[mkr].icon.imageDiv.id == zoneId)
		  	{
		  		var nodo = markerRoute.markers[mkr];
				markerRoute.removeMarker(nodo);
				break;
		  	}
		}
		  		
  		if(arrayRoute.length == 1){arrayRoute = new Array();esRuta = false;layerPolygon.destroyFeatures();document.getElementById("divAreasTool").className="olControlWithinCostLocalgisItemInactive";}
				
		/* Si existen 2 elementos en el array hay que eliminar la ruta */
		else if(arrayRoute.length == 2)
		{
			//funcion para eliminar la ruta
			//reasignación de nodos
			if(arrayRoute[0].idContext == zoneId)
			{
				//está eliminando el origen, es necesario ahora reestablecer el destino como origen y eliminar el primer elemento del array	
				arrayRoute.shift();
				layerPolygon.destroyFeatures();
				document.getElementById("divAreasTool").className="olControlWithinCostLocalgisItemInactive";
				arrayRoute[0].id = 1;
				
			} 
			else arrayRoute.pop();//está eliminando el destino, es necesario eliminar el último elemento del array
			
			esRuta = false;
			layerVector.destroyFeatures();
			arrayDescription = new Array();
			
		}
		/* Si existen más de 2 elementos en el array hay que calcular una nueva ruta con los nodos que quedan */
		else
		{    
			if(posicionNodo == 0){layerPolygon.destroyFeatures();}
			//comprobar que no hay nodos intermedios antes o después del nodo a eliminar
			
			//1 El nodo seleccionado no es un nodo intermedio:
			
			var esNodoInter = false;
			if(!arrayRoute[posicionNodo].inter)
	  		{
				var nodoAnterior = -1;
				var nodoSiguiente = -1;
				
				for(var x = 0; x < arrayRoute.length; x++)
				{
					if((arrayRoute[x].id < posicionNodo) && (!arrayRoute[x].inter))
					{
						if(nodoAnterior < arrayRoute[x].id)nodoAnterior = arrayRoute[x].id;
						
					}
					if((arrayRoute[x].id > posicionNodo) && (!arrayRoute[x].inter) && (nodoSiguiente == -1))nodoSiguiente = arrayRoute[x].id;
				}
				if(nodoAnterior == -1)nodoAnterior = posicionNodo;
				if(nodoSiguiente == -1)nodoSiguiente = posicionNodo;
				//se eliminan los nodos intermedios que haya en este espacio:
				
				for(var x = nodoAnterior; x < nodoSiguiente; x++)//se buscan los nodos intermedios siguientes
		  		{
		  			if(arrayRoute[x].inter){
		  				for(var mkr = 0; mkr < markerRoute.markers.length; mkr++)
						{
							if(markerRoute.markers[mkr].icon.imageDiv.id == arrayRoute[x].idContext)
						  	{
						  		nodo = markerRoute.markers[mkr];
								markerRoute.removeMarker(nodo);
								break;
						  	}
						}
		  				arrayInter.push(x);
		  			}
		  		}
				if(arrayInter.length > 0)
		  		{
					
		  			arrayInter = arrayInter.sort();
		  			var pPos = arrayInter[0];
		  			if(posicionNodo == 0)pPos = 0;
		  			arrayRoute.splice(pPos,arrayInter.length + 1)//+ 1 por el nodo no intermedio que eliminará;
					arrayDescription.splice(pPos,arrayInter.length + 1);
		  		}
				else arrayRoute.splice(posicionNodo,1);
	  		}
			else{arrayRoute.splice(posicionNodo,1); esNodoInter = true;}
			/*else
			{
				arrayRoute.splice(posicionNodo,1);
			}*/
			
			if(arrayRoute.length < 2)deleteRoute();
			else{
			
				//se renombran las marcas y los identificadores
				for(var x = 0; x < arrayRoute.length; x++)
			  	{
		  			if(arrayRoute[x].id > posicionNodo)
			  		{
		  				arrayRoute[x].id = arrayRoute[x].id - arrayInter.length - 1;
		  				if(!arrayRoute[x].inter)//asignar icono
		  				{
		  					var preInter = 0;
			  				for (var h=x-1;h>=0;h--)
					  		{
					  			if(arrayRoute[h].inter)preInter+=1;
					  			else break;
					  		}
			  				if(!esNodoInter)
			  				{
			  					var idImagen = arrayRoute[x].id - preInter;
			  					var RutaImg = "img/ruta/"+arrayABC[idImagen];
			  					for(var mkr = 0; mkr < markerRoute.markers.length; mkr++)
			  					{
			  						
			  						if(markerRoute.markers[mkr].icon.imageDiv.id == arrayRoute[x].idContext)
			  						{
			  							markerRoute.markers[mkr].icon.url = RutaImg;
										OpenLayers.Util.modifyAlphaImageDiv(markerRoute.markers[mkr].icon.imageDiv, 
			                        		markerRoute.markers[mkr].icon.imageDiv.id, 
			                       			null, 
			                            	sizeIconRoute, 
			                        		RutaImg, 
			                            	"absolute");
			                            break;
			                        }
			                        
			                    }  
			  				}
		  				}
			  		}
			  	}
				
				calcularRuta(0,tipoRuta,false,false);
			}
		}	
  	}
		    /*arrayFeatures = new Array();
		    var nuevaPos;
			//comprobar los nodos intermedios antes de esta posicion y después de esta posición
			if(!arrayRoute[posicionNodo].inter)
	  		{
	  			if(posicionNodo == 0){layerPolygon.destroyFeatures();layerVector.features[0].layer.removeFeatures(layerVector.features[0]);}
	  			
		  		for(var x = posicionNodo + 1; x < arrayRoute.length; x++)
		  		{
		  			if(arrayRoute[x].inter){
		  				for(var mkr = 0; mkr < markerRoute.markers.length; mkr++)
						{
							if(markerRoute.markers[mkr].icon.imageDiv.id == arrayRoute[x].idContext)
						  	{
						  		nodo = markerRoute.markers[mkr];
								markerRoute.removeMarker(nodo);
								break;
						  	}
						}
		  				arrayInter.push(x);
		  			}
		  			else break;
		  		}
		  		
		  		if(posicionNodo != 0)
		  		{
		  			var idNextN = posicionNodo - 1;
			  		for (var x=idNextN;x>=0;x--)
			  		{
			  			if(arrayRoute[x].inter){
			  				for(var mkr = 0; mkr < markerRoute.markers.length; mkr++)
							{
								if(markerRoute.markers[mkr].icon.imageDiv.id == arrayRoute[x].idContext)
							  	{
							  		nodo = markerRoute.markers[mkr];
									markerRoute.removeMarker(nodo);
									break;
							  	}
							}
			  				arrayInter.push(x);
			  				
			  			}
			  			else break;
			  		}
			  	}
		  		
		  		
		  		
		  		if(arrayInter.length > 0)
		  		{
		  			arrayInter = arrayInter.sort();
		  			var pPos = arrayInter[0];
		  			if(posicionNodo == 0)pPos = 0;
		  			arrayRoute.splice(pPos,arrayInter.length + 1)//+ 1 por el nodo no intermedio que eliminará;
					arrayDescription.splice(pPos,arrayInter.length + 1);
		  			layerVector.removeFeatures(layerVector.features[x]);
					
		  		}
		  		else
		  		{
		  			arrayRoute.splice(posicionNodo,1)
					arrayDescription.splice(posicionNodo,1);
		  			layerVector.removeFeatures(layerVector.features[x]);
		  		}
		  		
		  		
		  		for(var x = 0; x < arrayRoute.length; x++)
			  	{
		  			if(arrayRoute[x].id > posicionNodo)
			  		{
		  				arrayRoute[x].id = arrayRoute[x].id - arrayInter.length - 1;
			  			//redibujar
			  				
			  			if(!arrayRoute[x].inter)
			  			{
			  				if((!nuevaPos)&&(posicionNodo!=0))nuevaPos = arrayRoute[x].id;
			  				var preInter = 0;
			  				for (var h=x-1;h>=0;h--)
					  		{
					  			if(arrayRoute[h].inter)preInter+=1;
					  			else break;
					  		}
					  			var idImagen = arrayRoute[x].id - preInter;
			  					var RutaImg = "img/ruta/"+arrayABC[idImagen];
			  					for(var mkr = 0; mkr < markerRoute.markers.length; mkr++)
			  					{
			  						
			  						if(markerRoute.markers[mkr].icon.imageDiv.id == arrayRoute[x].idContext)
			  						{
			  							markerRoute.markers[mkr].icon.url = RutaImg;
										OpenLayers.Util.modifyAlphaImageDiv(markerRoute.markers[mkr].icon.imageDiv, 
			                        		markerRoute.markers[mkr].icon.imageDiv.id, 
			                       			null, 
			                            	sizeIconRoute, 
			                        		RutaImg, 
			                            	"absolute");
			                            break;
			                        }
			                        
			                    }                 
			  				}
			  			}
			  			
			  		}
			  	/*}*/
		  		
		  /*	}	
		  	else //elimina un nodo intermedio
		  	{
		  		//arrayFeatures.push(layerVector.features[posicionNodo-1]);
		  		arrayRoute.splice(posicionNodo,1)//+ 1 por el nodo no intermedio que eliminará;
				arrayDescription.splice(posicionNodo,1);
				nuevaPos =  posicionNodo;
				
				for(var x = 0; x < arrayRoute.length; x++)
		  		{
		  			if(arrayRoute[x].id > posicionNodo)
		  			{
		  				arrayRoute[x].id = arrayRoute[x].id - 1;
		  			}
		  		}
		  	}
		}//si hay más de 2 elementos
  		                    	
  	}
  	//layerVector.removeFeatures(arrayFeatures);
  	if((!moveInter)&&(nuevaPos)){calcularRuta(nuevaPos,tipoRuta,false,false);}
	else moveInter = false;*/
  }
  function posicion_nodo(zoneId)
  {
  	for(var x = 0; x < arrayRoute.length; x++)
  	{
  		if((arrayRoute[x].idContext)&&(arrayRoute[x].idContext == zoneId))
  		{
  			return x;
  		}
  		
  	}
  	return null;
  }
  
  function imprimirRuta(identidad,idmap)
  {
	  var urlWmsLocalgis = "";
	  var listadoCapas = "";
	  
	  for(var x = 0; x < map.layers.length; x ++)
	  {
		  if(map.layers[x].CLASS_NAME == "OpenLayers.Layer.WMSLocalgis")
		  {
			  if(urlWmsLocalgis == "") urlWmsLocalgis = map.layers[x].url;
			  if(map.layers[x].visible){
				  listadoCapas += map.layers[x].name;
			  }  
		  }
	  }
	  /*if(wpsProfileReqPrint) 
		{
			timeoutId2= setTimeout( function() { if ( callInProgress(wpsProfileReqPrint) ) {wpsProfileReqPrint.abort(); return false;} }); 
		}
		if(wpsProfileReqPrint) wpsProfileReqPrint.abort();
		
		if (window.XMLHttpRequest)
		  {// code for IE7+, Firefox, Chrome, Opera, Safari
			wpsProfileReqPrint=new XMLHttpRequest();
			wpsProfileReqPrint.open("GET","proxy.do?entidad="+identidad+"&map="+idmap, true);
			wpsProfileReqPrint.send(null);
		  }
		else
		  {// code for IE6, IE5
			wpsProfileReqPrint=new ActiveXObject("Microsoft.XMLHTTP");
			wpsProfileReqPrint.open("GET","proxy.do?entidad="+identidad+"&map="+idmap, true);
		  
			wpsProfileReqPrint.send();
		  }
*/
		
  }
  