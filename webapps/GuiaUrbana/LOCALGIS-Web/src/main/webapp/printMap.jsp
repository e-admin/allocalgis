<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html:html xhtml="true" lang="es">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
    <title>AL LocalGIS Guia Urbana_ShowMap</title>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/favicon.ico" type="image/x-icon" />

    <link href="${pageContext.request.contextPath}/css/staticStyles.css" rel="stylesheet" type="text/css" />
    <logic:present name="customCSS" scope="request">
        <style type="text/css">
            <bean:write name="customCSS" scope="request"/>
        </style>
    </logic:present>
    <logic:notPresent name="customCSS" scope="request">
        <link href="${pageContext.request.contextPath}/css/printingStyles.css" rel="stylesheet" type="text/css" />
    </logic:notPresent>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/dhtmlxMenu/dhtmlxmenu.css"/>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/dhtmlxCalendar/dhtmlxcalendar.css"/>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/dhtmlx_layout/dhtmlxlayout.css"/>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/dhtmlx_layout/skins/dhtmlxlayout_dhx_blue.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/dhtmlx_Windows/skins/dhtmlxwindows_dhx_black.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/dhtmlx_combo/dhtmlxcombo.css">
	
	<!--  abh -->
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/dhtmlx_Windows/dhtmlxwindows.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/dhtmlx_Windows/skins/dhtmlxwindows_dhx_skyblue.css">
	<!-- abh -->
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/dhtmlxMenu/dhtmlxcommon.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/dhtmlxMenu/dhtmlxmenu.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/dhtmlxMenu/menu.js"></script>
    
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/dhtmlx_layout/dhtmlxcommon.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/dhtmlx_layout/dhtmlxlayout.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/dhtmlx_layout/dhtmlxcontainer.js"></script>
    
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/dhtmlx_Windows/dhtmlxwindows.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/dhtmlx_Windows/dhtmlxcontainer.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/dhtmlx_Windows/dhtmlxcommon.js"></script>
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/dhtmlxCalendar/dhtmlxcommon.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/dhtmlxCalendar/dhtmlxcalendar.js"></script>
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/dhtmlx_Windows/dhtmlxcommon.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/dhtmlx_Windows/dhtmlxcontainer.js"></script>
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/dhtmlx_combo/dhtmlxcommon.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/dhtmlx_combo/dhtmlxcombo.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/dhtmlx_combo/ext/dhtmlxcombo_extra.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/dhtmlx_combo/ext/dhtmlxcombo_group.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/dhtmlx_combo/ext/dhtmlxcombo_whp.js"></script>
    
    <script type="text/javascript" src='${pageContext.request.contextPath}/${requestScope.configurationLocalgisWeb}/dwr/engine.js'></script>
    <script type="text/javascript" src='${pageContext.request.contextPath}/${requestScope.configurationLocalgisWeb}/dwr/util.js'></script>
    <script type="text/javascript" src='${pageContext.request.contextPath}/${requestScope.configurationLocalgisWeb}/dwr/interface/WFSGService.js'></script>
    <script type="text/javascript" src='${pageContext.request.contextPath}/${requestScope.configurationLocalgisWeb}/dwr/interface/MarkerService.js'></script>
    <script type="text/javascript" src='${pageContext.request.contextPath}/${requestScope.configurationLocalgisWeb}/dwr/interface/IncidenciaService.js'></script>    
    <script type="text/javascript" src='${pageContext.request.contextPath}/js/openlayers-2.5/lib/OpenLayers.js'></script>
    
	<!-- this gmaps key generated for http://openlayers.org/dev/ -->
	<!-- Google Maps API key pamod-pre ABQIAAAAhxkUiBSZAcDmvS4JGP60OBQUI5KBusmYpig0pVVEUrJhxOje2xT0ZbzXygQ9-atC6cNgtaBbUvKMlQ -->
	<script type="text/javascript">
	  var hostname=window.location.hostname;
	  var googleKey='';
	  //alert('Mi host '+hostname);
	  if (hostname.indexOf('localhost') != -1) {
	     googleKey='ABQIAAAAjpkAC9ePGem0lIq5XcMiuhR_wWLPFku8Ix9i2SXYRVK3e45q1BQUd_beF8dtzKET_EteAjPdGDwqpQ';
	  }else   if (hostname.indexOf('pamod-pre') != -1){
	     googleKey='ABQIAAAAhxkUiBSZAcDmvS4JGP60OBSudBqu4HsAFxU698-e7JiJshvE9BTN30Oer-Sbl9catJIFyC-vd8Oa8A';
	  
      }else   if (hostname.indexOf('pamod-balanceada') != -1){
	     googleKey='ABQIAAAAhxkUiBSZAcDmvS4JGP60OBQA2jc6u-0iZywhyk6rbYzpY8qP8BR5tzjVx_6RSKRkocXQAZBRWVfq8g';
	  }			  
	  /*else {
	     alert('No se ha definido googleKey para el servidor '+ hostname +' en el servicio Google Maps');
	  }*/
	  
	  document.write("<script src='http://maps.google.com/maps?file=api&amp;v=2&amp;key=" + googleKey + "'></s"+"cript>");
		
	</script>  
  
     <!--jsp:include page="utils.jsp" /-->
  
    <script type="text/javascript">
    
    	var map;
    	
	    var srid = <bean:write name="localgisMap" property="srid"/>;
	    var projectionStr = "EPSG:"+srid;
	    var imgPath = OpenLayers._getScriptLocation() + 'theme/localgis/img';
	    var themeStr = OpenLayers._getScriptLocation() + 'theme/localgis/style.css';
    
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
        
        
        function escudoAyuntamiento(){
    		var escudoEntidad = "<bean:write name="escudoEntidad" scope="request"/>";
    		document.images["escudo"].src = escudoEntidad;
    	}
        
        function initOpenLayers() {
            // Constantes para el mapa
     
            var minx = <bean:write name="localgisMap" property="minx"/>;
            var miny = <bean:write name="localgisMap" property="miny"/>;
            var maxx = <bean:write name="localgisMap" property="maxx"/>;
            var maxy = <bean:write name="localgisMap" property="maxy"/>;

			var x = '<bean:write name="printMap" property="x"/>';
			var y = '<bean:write name="printMap" property="y"/>';
			var zoom = <bean:write name="printMap" property="zoom"/>;
            
            var extentEntidad = new OpenLayers.Bounds(minx, miny, maxx, maxy);
       	
        	
    		var units="m"
    		if (projectionStr=="EPSG:4326")
    			units="degrees";
    		

            var maxExtentSpain = new OpenLayers.Bounds(<bean:write name="boundingBoxSpain" property="minx"/>, <bean:write name="boundingBoxSpain" property="miny"/>, <bean:write name="boundingBoxSpain" property="maxx"/>, <bean:write name="boundingBoxSpain" property="maxy"/>);

            
           // utils_addLayout();
          
		<logic:empty name="desc" scope="request">
		 	dhxLayout = new dhtmlXLayoutObject("map", "2U"); 
        </logic:empty>
        <logic:notEmpty name="desc" scope="request">
			 dhxLayout = new dhtmlXLayoutObject("map", "3U");
			 dhxLayout.cells("c").setHeight(400);
			//dhxLayout.cells("c").setWidth(202);
			//dhxLayout.cells("c").fixSize(true, true);
			dhxLayout.cells("c").setText("Descripcion");
			dhxLayout.cells("c").attachObject("desc");
			dhxLayout.setAutoSize("b;c", "c");
        </logic:notEmpty> 
		dhxLayout.cells("a").setHeight(500);
		dhxLayout.cells("a").setWidth(200);
		dhxLayout.cells("a").fixSize(true, true);
		dhxLayout.cells("a").setText("Capas");
		dhxLayout.cells("b").setText(document.getElementById("nombreMunicipio").innerHTML);
		dhxLayout.cells("b").setWidth(800);
		dhxLayout.cells("a").attachObject("layerswitcher");
		dhxLayout.cells("b").attachObject("mapDetail");
		
		
          
            
            
            OpenLayers.ProxyHost = "${pageContext.request.contextPath}/${requestScope.configurationLocalgisWeb}/getFeatureInfoProxy.do?language=<bean:write name="language"/>&urlWFSServer=";

            // Creación y configuracion del mapa
            map = new OpenLayers.MapLocalgis( $('mapDetail') ,{controls: [], maxExtent: maxExtentSpain, minScale: 13107200, projection: projectionStr, maxResolution: "auto", numZoomLevels: 17, theme: themeStr, units: units, extentEntidad: extentEntidad});

            // Layer switcher del mapa
            var layerInfo = new OpenLayers.Control.LayerInfoLocalgis();
                        
            // Barra de escala
            utils_addScaleBar(map);
            
            var layer_base = new OpenLayers.Layer.Image('BaseLayer', '${pageContext.request.contextPath}/img/blank.gif', maxExtentSpain, new OpenLayers.Size(1,1), null);                                                            
            map.addLayer(layer_base);
            layerInfo.addLayerToDraw(layer_base);

            
            var layer_todo_name = '<bean:write name="layerToPrint" property="name"/>';
            var layer_todo_preName = '';
            var layer_todo = new OpenLayers.Layer.WMSLocalgis(layer_todo_name, '<bean:write name="layerToPrint" property="urlGetMapRequests" filter="false"/>', 
            											{layers: '<bean:write name="layerToPrint" property="internalName" filter="false"/>', 
            											request: 'GetMap', format: '<bean:write name="layerToPrint" property="format"/>',
            											transparent: false, EXCEPTIONS: 'application/vnd.ogc.se_blank'},
            											{gutter: 10, buffer: 0, isBaseLayer: false, 
            											urlLegend: '<bean:write name="layerToPrint" property="urlGetLegendGraphicsRequests" filter="false"/>', 
            											preName: layer_todo_preName, singleTile: <bean:write name="singleTile" scope="request"/>} );
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

            layerInfo.addLayerToDraw(layer_provincias);
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
            var layer_<bean:write name="layerIndex"/> = new OpenLayers.Layer.WMSLocalgis(layer_<bean:write name="layerIndex"/>_name, 
            								'<bean:write name="layer" property="urlGetMapRequests" filter="false"/>', 
            								{layers: '<bean:write name="layer" property="internalName"/>', 
            									request: 'GetMap', format: '<bean:write name="layer" property="format"/>', 
            									transparent:true, EXCEPTIONS: 'application/vnd.ogc.se_blank'}, 
            									{gutter: 10, buffer: 0, isBaseLayer: false, 
            									urlLegend: '<bean:write name="layer" property="urlGetLegendGraphicsRequests" filter="false"/>', 
            									preName: layer_<bean:write name="layerIndex"/>_preName} );
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
            
            map.addControlWithIdDiv(layerInfo, null, 'layerswitcher');
            layerInfo.maximizeControl();
            
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
            
            <logic:notEmpty name="overlay" scope="request">
            // Overlay vectorial
	            var overlay='<%=(String)request.getAttribute("overlay")%>';
	        	var overlayDoc = OpenLayers.parseXMLString(overlay);
	        	var fileGml = new OpenLayers.Format.GML();
				var routeFeaturesGml = fileGml.read(overlayDoc);
				if(routeFeaturesGml) 
					{
					if(routeFeaturesGml.constructor != Array) 
						{
							routeFeaturesGml = [routeFeaturesGml];
						}
						style_red = OpenLayers.Util.extend({}, OpenLayers.Feature.Vector.style['default']);
	    				style_red.strokeColor ="#6633CC"; 
	    				style_red.fillOpacity= 0.75;
	    				style_red.strokeWidth=8;
	   					style_red.strokeOpacity=0.75;
						layerVector = new OpenLayers.Layer.Vector("Ruta", {style: style_red});
						layerVector.displayInLayerSwitcher = false;
						map.addLayer(layerVector);
						layerVector.addFeatures(routeFeaturesGml);
					}            
            </logic:notEmpty>
           
  			map.setCenter(lonlat,zoom,false,false);  
        }
        
        function printMap() {
			var btnImp=document.getElementById("nuevoSearch");
			var padre=btnImp.parentNode;
			padre.removeChild(btnImp);
			
			var layer=document.getElementById("layersDiv");
			layer.style.opacity=1;
		
            window.print();
			window.close();
        }
        
    </script>
</head>


    <body onload="initOpenLayers();initMenu('mapDetail',1);escudoAyuntamiento();" onresize="waitToUpdate();">
	<div id="wrapShowMap">
		<!-- Contenido de la pagina -->

			<div id="bannerMapa">
				<div id="bannerMapaLeft">
    				<img src="../img/banner_left.gif" id="imgBannerLeft"/>
				</div>
				<div id="bannerMapaRight">
					<img src="../img/user.gif" name="escudo" id="imgBannerRight"/>
				</div>	
			</div>
			<div id="desplegables">
				<!--  Formulario de Busqueda -->
				<div id="nuevoSearch">
                   		<img src="${pageContext.request.contextPath}/img/btn_imprimir.gif" alt="Imprimir" onclick="javascript:printMap();"/>
					<!--<button onClick="Collapse()">Colapsar</button>-->
				</div>
				<!--  Formulario de Seleccion de Cartografia Base -->
			</div>
			<div id="toolbar"></div>
			
			
			<div id="map">
				<div id="nombreMunicipio">
					<logic:present name="nombreMapa" scope="request">
						<bean:write name="nombreMapa" scope="request"/>
					</logic:present>
				</div>				
				
				<div id="layerswitcher"></div>
				<div id="mapDetail">
					<div id="menuR">
						<div id="resMeasure"
							style="z-index: 1; background-color: rgb(234, 235, 236); display: none; left: 10px; top: 10px; position: absolute; font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 10pt;">
						</div>
					</div>
				</div>
			</div>
			<div id="desc">
				<logic:present name="desc" scope="request">
					<table>
						<%=(String)request.getAttribute("desc")%>
					</table>
				</logic:present>
			</div>
		</div>
	<!--  Fin del contenido de la pagina -->
	</div>
    </body>
<!-- 
    <body onload="initOpenLayers();" style="background-color: #FFFFFF;">
        <div id="wrapShowMap">
            <div id="top" class="top">
                <img src="${pageContext.request.contextPath}/img/top_left.gif" alt="Esquina superior izquierda" class="esquina_sup_izq" />
                <img src="${pageContext.request.contextPath}/img/top_right.gif" alt="Esquina superior derecha" class="esquina_sup_der" />
            </div>
            <div id="content" class="content">
                <div id="boxcontrol" class="boxcontrol">

                    <div id="bannerMapaContainer">
                        <div id="bannerMapa">
                            <div id="bannerMapaLeft">
                            <img name="escudo" src="${pageContext.request.contextPath}/img/banner_left.gif"/>
                            </div>
                                <div id="bannerMapaRight">
                                    <div id="searchDiv">
                                    </div>
                                    <div id="toolbar">
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                       
                    <div id="map">
                        <div id="nombreMunicipio">
                            <logic:present name="nombreMapa" scope="request">
                                <bean:write name="nombreMapa" scope="request"/>
                            </logic:present>
                        </div>
                        <div id="layerswitcher"></div>
                        <div id="mapDetail"></div>
                    </div>
                    
                </div>
            </div>
            <div id="bottom" class="bottom">
                <img src="${pageContext.request.contextPath}/img/btm_left.gif" alt="Esquina inferior izquierda" class="esquina_inf_izq" />
                <img src="${pageContext.request.contextPath}/img/btm_right.gif" alt="Esquina inferior derecha" class="esquina_inf_der" />
            </div>
        </div>
        <div id="print">
            <a href="javascript:printMap()">Imprimir</a>
        </div>
    </body>
    
    -->
</html:html>