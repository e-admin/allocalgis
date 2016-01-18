<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html:html xhtml="true" lang="es">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
    <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7">
    <title>AL LocalGIS Guia Urbana_Print</title>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/favicon.ico" type="image/x-icon" />

    <link href="${pageContext.request.contextPath}/css/staticStyles.css" rel="stylesheet" type="text/css" />
    <logic:present name="customCSS" scope="request">
        <style type="text/css">
            <bean:write name="customCSS" scope="request"/>
        </style>
    </logic:present>
    <logic:notPresent name="customCSS" scope="request">
        <link href="${pageContext.request.contextPath}/css/dynamicStyles.css" rel="stylesheet" type="text/css" />
    </logic:notPresent>

    <script type="text/javascript" src='${pageContext.request.contextPath}/${requestScope.configurationLocalgisWeb}/dwr/engine.js'></script>
    <script type="text/javascript" src='${pageContext.request.contextPath}/${requestScope.configurationLocalgisWeb}/dwr/util.js'></script>
    <script type="text/javascript" src='${pageContext.request.contextPath}/${requestScope.configurationLocalgisWeb}/dwr/interface/WFSGService.js'></script>
    <script type="text/javascript" src='${pageContext.request.contextPath}/${requestScope.configurationLocalgisWeb}/dwr/interface/MarkerService.js'></script>
    <script type="text/javascript" src='${pageContext.request.contextPath}/${requestScope.configurationLocalgisWeb}/dwr/interface/IncidenciaService.js'></script>        
    <script type="text/javascript" src='${pageContext.request.contextPath}/js/openlayers-2.5/lib/OpenLayers.js'></script>
    
    <style type="text/css" media="print">
    .noprint{
    	visibility:hidden;
    }
</style>
    <style type="text/css">
    #mapDetail{
    	/*float:left;*/
    	top:70px;
    	left:10px;
    	position:absolute;
    }
    #nombreMunicipio{
    	
    	float:left;
    	text-align:left;
    }
    #rutaDetalle{
    	
	   left:10px;
    	position:absolute;
	    font-family:Verdana, Arial, Helvetica, sans-serif;
    	font-size:10px;
    	display:table-cell;
    	top:540px;
    	/*float:left;
    	text-align:left;
    	margin-left:0px;
    	top:-10px;*/
    }
   
    </style>
    <script type="text/javascript">
    
        OpenLayers.Util.onImageLoadError = function() {
            //No mostramos el componente
            this.style.display = "none";
            var layerToDisable = OpenLayers.LocalgisUtils.getLayersFromURL(this.src);
            if (layerToDisable != null && layerToDisable != undefined) {
                //Deshabilitamos las capas del layerswitcher
                var layers = this.map.layers;
                for(var i = 0;i<layers.length;i++) {
                    if (layers[i] instanceof OpenLayers.Layer.WMSLocalgis) {
                        if (layers[i].params['LAYERS'] == layerToDisable) {
                            layers[i].setDisabled(true);
                            layers[i].setVisibility(false);
                        }
                    }
                }
            }
        } 
        
        function initOpenLayers() {
            // Constantes para el mapa
            var srid = <bean:write name="localgisMap" property="srid"/>;
            var projectionStr = "EPSG:"+srid;
            var imgPath = OpenLayers._getScriptLocation() + 'theme/localgis/img';
            var themeStr = OpenLayers._getScriptLocation() + 'theme/localgis/style.css';

            var minx = <bean:write name="localgisMap" property="minx"/>;
            var miny = <bean:write name="localgisMap" property="miny"/>;
            var maxx = <bean:write name="localgisMap" property="maxx"/>;
            var maxy = <bean:write name="localgisMap" property="maxy"/>;

			var x = '<bean:write name="printMap" property="x"/>';
			var y = '<bean:write name="printMap" property="y"/>';
			var zoom = <bean:write name="printMap" property="zoom"/>;
            
            var extentEntidad = new OpenLayers.Bounds(minx, miny, maxx, maxy);

            var maxExtentSpain = new OpenLayers.Bounds(<bean:write name="boundingBoxSpain" property="minx"/>, <bean:write name="boundingBoxSpain" property="miny"/>, <bean:write name="boundingBoxSpain" property="maxx"/>, <bean:write name="boundingBoxSpain" property="maxy"/>);

            OpenLayers.ProxyHost = "${pageContext.request.contextPath}/${requestScope.configurationLocalgisWeb}/getFeatureInfoProxy.do?language=<bean:write name="language"/>&urlWFSServer=";

            // Creación y configuracion del mapa
            var map = new OpenLayers.MapLocalgis( $('mapDetail') ,{controls: [], maxExtent: maxExtentSpain, minScale: 13107200, projection: projectionStr, maxResolution: "auto", numZoomLevels: 17, theme: themeStr, units: "m", extentEntidad: extentEntidad});

            // Layer switcher del mapa
            var layerInfo = new OpenLayers.Control.LayerInfoLocalgis();
            
            // Barra de escala
/*            var scaleBar = new OpenLayers.Control.ScaleBarLocalgis();
            scaleBar.displaySystem = 'metric';
            scaleBar.abbreviateLabel = true;
            map.addControl(scaleBar);*/

            var layer_base = new OpenLayers.Layer.Image('BaseLayer', '${pageContext.request.contextPath}/img/blank.gif', maxExtentSpain, new OpenLayers.Size(1,1), null);                                                            
            map.addLayer(layer_base);
            layerInfo.addLayerToDraw(layer_base);

            var layer_todo_name = '<bean:write name="layerToPrint" property="name"/>';
            var layer_todo_preName = '';
            var layer_todo = new OpenLayers.Layer.WMSLocalgis(layer_todo_name, '<bean:write name="layerToPrint" property="urlGetMapRequests" filter="false"/>', {layers: '<bean:write name="layerToPrint" property="internalName" filter="false"/>', request: 'GetMap', format: '<bean:write name="layerToPrint" property="format"/>', transparent: false, EXCEPTIONS: 'application/vnd.ogc.se_blank'}, {gutter: 10, buffer: 0, isBaseLayer: false, urlLegend: '<bean:write name="layerToPrint" property="urlGetLegendGraphicsRequests" filter="false"/>', preName: layer_todo_preName, singleTile: <bean:write name="singleTile" scope="request"/>} );
            layer_todo.setOpacity(0.85);
            map.addLayer(layer_todo);

            <logic:notEmpty name="ortofotoLayer">
            var layer_ortofoto_name = '<bean:write name="ortofotoLayer" property="name"/>';
            var layer_ortofoto_preName = '<img src="'+imgPath+'/ortofoto.gif"/>&nbsp;';
            var layer_ortofoto = new OpenLayers.Layer.WMSLocalgis(
                                         layer_ortofoto_name, 
                                         '<bean:write name="ortofotoLayer" property="urlGetMapRequests" filter="false"/>', 
                                         {layers: '<bean:write name="ortofotoLayer" property="internalName"/>', 
                                         request: 'GetMap', 
                                         format: '<bean:write name="ortofotoLayer" property="format"/>', 
                                         transparent:false, 
                                         EXCEPTIONS: 'application/vnd.ogc.se_blank'}, 
                                         {gutter: 0 ,
                                         buffer: 0, 
                                         isBaseLayer: false, 
                                         urlLegend: '<bean:write name="ortofotoLayer" property="urlGetLegendGraphicsRequests" filter="false"/>', 
                                         preName: layer_ortofoto_preName} );
            layerInfo.addLayerToDraw(layer_ortofoto);
            </logic:notEmpty>

            <logic:notEmpty name="provinciasLayer">
            var layer_provincias_name = 'Provincias';
            var layer_provincias_preName = '<img src="'+imgPath+'/layerProvincias.gif"/>&nbsp;';
            
            var layer_provincias = new OpenLayers.Layer.WMSLocalgis(
                                           layer_provincias_name, 
                                           '<bean:write name="provinciasLayer" property="urlGetMapRequests" filter="false"/>', 
                                           {layers: '<bean:write name="provinciasLayer" property="internalName"/>', 
                                           request: 'GetMap', 
                                           format: '<bean:write name="provinciasLayer" property="format"/>', 
                                           transparent:true, 
                                           EXCEPTIONS: 'application/vnd.ogc.se_blank'},
                                           {gutter: 0 ,
                                           buffer: 0, 
                                           isBaseLayer: false, 
                                           urlLegend: '<bean:write name="provinciasLayer" property="urlGetLegendGraphicsRequests" filter="false"/>', 
                                           preName: layer_provincias_preName} );

            //layerInfo.addLayerToDraw(layer_provincias);
            </logic:notEmpty>

            // Creacion y carga de capas dinamicas
            <logic:iterate id="layer" name="layers" indexId="layerIndex">
                <logic:equal name="layer" property="externalLayer" value="false">
            var layer_<bean:write name="layerIndex"/>_name = '<bean:write name="layer" property="name"/>';
            var layer_<bean:write name="layerIndex"/>_preName = '';
                </logic:equal>
                <logic:equal name="layer" property="externalLayer" value="true">
            var layer_<bean:write name="layerIndex"/>_name = '<bean:write name="layer" property="name"/>';
            var layer_<bean:write name="layerIndex"/>_preName = '<img src="'+imgPath+'/layerWMS.gif"/>&nbsp;';
                </logic:equal>
            var layer_<bean:write name="layerIndex"/> = new OpenLayers.Layer.WMSLocalgis(layer_<bean:write name="layerIndex"/>_name, '<bean:write name="layer" property="urlGetMapRequests" filter="false"/>', {layers: '<bean:write name="layer" property="internalName"/>', request: 'GetMap', format: '<bean:write name="layer" property="format"/>', transparent:true, EXCEPTIONS: 'application/vnd.ogc.se_blank'}, {gutter: 10, buffer: 0, isBaseLayer: false, urlLegend: '<bean:write name="layer" property="urlGetLegendGraphicsRequests" filter="false"/>', preName: layer_<bean:write name="layerIndex"/>_preName} );
            layer_<bean:write name="layerIndex"/>.setOpacity(0.7);
            layerInfo.addLayerToDraw(layer_<bean:write name="layerIndex"/>);
            </logic:iterate>

            // Visualizamos el mapa completo 
            var lonlat = new OpenLayers.LonLat(x,y);
            
            <logic:present name="markers" scope="request">
            var markers = new OpenLayers.Layer.Markers("Marcas de posición",{layers: 'Pings'});
            map.addMarkersLayer(markers);
            layerInfo.addLayerToDraw(markers);
            </logic:present>
            
            //map.addControlWithIdDiv(layerInfo, null, 'layerswitcher');
            //layerInfo.maximizeControl();
            
            //Añadimos las marcas de posicion
            <logic:notEmpty name="markers" scope="request">
                var size = new OpenLayers.Size(13,21);
                var offset = new OpenLayers.Pixel(0, -size.h);
                <logic:iterate name="markers" id="marker">
                    var icon_<bean:write name="marker" property="markerid"/> = new OpenLayers.Icon('img/pin_inverted.gif',size,offset);
                    var positionMarkerLocalgis_<bean:write name="marker" property="markerid"/> = new OpenLayers.PositionMarkerLocalgis(new OpenLayers.LonLat(<bean:write name="marker" property="x"/>, <bean:write name="marker" property="y"/>), icon_<bean:write name="marker" property="markerid"/>);
                    positionMarkerLocalgis_<bean:write name="marker" property="markerid"/>.setId(<bean:write name="marker" property="markerid"/>);
                    positionMarkerLocalgis_<bean:write name="marker" property="markerid"/>.setIdMap(<bean:write name="marker" property="mapid"/>);
                    positionMarkerLocalgis_<bean:write name="marker" property="markerid"/>.setName('<bean:write name="marker" property="markname"/>');
                    positionMarkerLocalgis_<bean:write name="marker" property="markerid"/>.setText('<bean:write name="marker" property="marktext"/>');
                    positionMarkerLocalgis_<bean:write name="marker" property="markerid"/>.setScale('<bean:write name="marker" property="scale"/>');
                    layerInfo.addPositionMarkerLocalgis(positionMarkerLocalgis_<bean:write name="marker" property="markerid"/>);
                </logic:iterate>
            </logic:notEmpty>
		
			var marcasRuta = window.opener.markerRoute;
			if(marcasRuta.markers.length != 0)
			{
				var markerRoutePrint = new OpenLayers.Layer.Markers("Markers_RUTA");//,{layers: 'Markers'});
				markerRoutePrint.displayInLayerSwitcher = false;
				map.addLayer(markerRoutePrint);
				for(var nm = 0; nm < marcasRuta.markers.length; nm++)
				{
					var urlIcon = "${pageContext.request.contextPath}/" + marcasRuta.markers[nm].icon.url;
					var offsetIcon = new OpenLayers.Pixel(marcasRuta.markers[nm].icon.offset.x, marcasRuta.markers[nm].icon.offset.y);
					var sizeIcon = new OpenLayers.Size(marcasRuta.markers[nm].icon.size.w, marcasRuta.markers[nm].icon.size.h);
					var iconTempMarker = new OpenLayers.Icon(urlIcon,sizeIcon,offsetIcon);
					var lonlatMarker = marcasRuta.markers[nm].lonlat;
					var marca = new OpenLayers.Marker(lonlatMarker, iconTempMarker);
					markerRoutePrint.addMarker(marca);
					
				}
				
			}
			try{
			<%
			if (request.getParameter("esRuta").equals("true"))
			{
				String textoRuta = session.getAttribute("gmlRutaPrint").toString().replaceAll("\"","\"");
			%>
				var style_red = OpenLayers.Util.extend({}, OpenLayers.Feature.Vector.style['default']);
			    style_red.strokeColor ="#6633CC"; 
			    style_red.fillOpacity= 0.75;
			    style_red.strokeWidth=8;
			    style_red.strokeOpacity=0.75;
			    
				var layerVectorRuta = new OpenLayers.Layer.Vector("Ruta", {style: style_red});
				layerVectorRuta.displayInLayerSwitcher = false;
				map.addLayer(layerVectorRuta);
				
				var textoGML = '<%=textoRuta%>';

				if (typeof textoGML == "string") { 
					textoGML = OpenLayers.parseXMLString(textoGML);
	        	}
				else textoGML = null;
			
				var fileGml = new OpenLayers.Format.GML();
				var featuresGml = fileGml.read(textoGML);
				var mensajeTexto = "";
				cadenaTextoRuta = "";
				if(featuresGml) 
				{
					if(featuresGml.constructor != Array) 
					{
						featuresGml = [featuresGml];
					}
					layerVectorRuta.addFeatures(featuresGml);
				}
			<%
			}
			%>

			<%
			if (request.getParameter("esArea").equals("true"))
			{
				String textoArea = session.getAttribute("gmlArea").toString().replaceAll("\"","\"");
			%>
				var style_red = OpenLayers.Util.extend({}, OpenLayers.Feature.Vector.style['default']);
			    style_red.strokeColor ="#6633CC"; 
			    style_red.fillOpacity= 0.75;
			    style_red.strokeWidth=8;
			    style_red.strokeOpacity=0.75;
			    
				var layerArea = new OpenLayers.Layer.Vector("Ruta", {style: style_red});
				layerArea.displayInLayerSwitcher = false;
				map.addLayer(layerArea);
				
				var textoGML = '<%=textoArea%>';

				if (typeof textoGML == "string") { 
					textoGML = OpenLayers.parseXMLString(textoGML);
	        	}
				else textoGML = null;
			
				var fileGml = new OpenLayers.Format.GML();
				var featuresGml = fileGml.read(textoGML);
				var mensajeTexto = "";
				cadenaTextoRuta = "";
				if(featuresGml) 
				{
					if(featuresGml.constructor != Array) 
					{
						featuresGml = [featuresGml];
					}
					layerArea.addFeatures(featuresGml);
				}
			<%
			}
			%>
			
			//map.addLayer(new OpenLayers.Layer.GML("GML", "${pageContext.request.contextPath}/prueba.gml"));
			
			var mensajeHtml = "<table width=100% style='text-align:left;'>";
			mensajeHtml += window.opener.cadenaTextoRuta; 
			mensajeHtml+="</table>";
			document.getElementById("rutaDetalle").innerHTML = "<table><tr><td><a class='noprint' onClick='javascript:printMap();' href='#'>Imprimir</a></td></tr></table>"+mensajeHtml;
			}
			catch(e){}
  			map.setCenter(lonlat,zoom,false,false);  
        }
        
        function printMap() {
            window.print();
        }
        
    </script>
    
    <% //out.print(session.getAttribute("resPotal"));%>
</head>

    <body onload="initOpenLayers();" style="background-color: #FFFFFF;">
        <div id="wrapShowMap">
            <div id="top" class="top">
                
            </div>
            <div >
                <div>

                    <!-- div id="bannerMapaContainer">
                        <div id="bannerMapa">
                            <div id="bannerMapaLeft">
                                <div id="bannerMapaRight">
                                    <div id="searchDiv">
                                    </div>
                                    <div id="toolbar">
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div> -->
                       
                    <div id="map">
                        <div id="nombreMunicipio">
                            <logic:present name="nombreMapa" scope="request">
                                <bean:write name="nombreMapa" scope="request"/>
                            </logic:present>
                        </div>
                        
                        <div id="mapDetail"></div>
                        
                        <div id="rutaDetalle"></div>
                       
                    </div>
                     
                    <!--  Fin del contenido de la pagina -->
                </div>
            </div>
           
        </div>
       
    </body>
</html:html>