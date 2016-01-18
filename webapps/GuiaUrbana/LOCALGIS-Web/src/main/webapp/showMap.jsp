<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%
response.setHeader("Cache-Control","no-cache"); 
response.setHeader("Pragma","no-cache"); 
response.setDateHeader ("Expires", -1); 
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page import="com.localgis.web.core.config.Configuration"%>
<html:html xhtml="true" lang="es">



<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />

<!-- 
<link href="${pageContext.request.contextPath}/css/overwrite.css" rel="stylesheet" type="text/css" />
-->



<title>AL LocalGIS Guia Urbana_ShowMap</title>
<html:base ref="site" />


<link rel="shortcut icon"
	href="${pageContext.request.contextPath}/favicon.ico"
	type="image/x-icon" />

<link href="${pageContext.request.contextPath}/css/staticStyles.css"
	rel="stylesheet" type="text/css" />


<logic:present name="customCSS" scope="request">
	<style type="text/css">
	<bean:write name="customCSS" scope="request"/>
	</style>
</logic:present>

<!-- Aqui -->
<link href="${pageContext.request.contextPath}/css/dynamicStyles.css"	rel="stylesheet" type="text/css" />


<link rel="stylesheet" type="text/css"	href="${pageContext.request.contextPath}/js/dhtmlxMenu/dhtmlxmenu.css" />
<link rel="stylesheet" type="text/css"	href="${pageContext.request.contextPath}/js/dhtmlxCalendar/dhtmlxcalendar.css" />
<link rel="stylesheet" type="text/css"	href="${pageContext.request.contextPath}/js/dhtmlx_layout/dhtmlxlayout.css" />
<link rel="stylesheet" type="text/css"	href="${pageContext.request.contextPath}/js/dhtmlx_layout/skins/dhtmlxlayout_dhx_blue.css">
<link rel="stylesheet" type="text/css"	href="${pageContext.request.contextPath}/js/dhtmlx_Windows/skins/dhtmlxwindows_dhx_black.css">
<link rel="stylesheet" type="text/css"	href="${pageContext.request.contextPath}/js/dhtmlx_combo/dhtmlxcombo.css">

<script type="text/javascript"	src="${pageContext.request.contextPath}/js/dhtmlxMenu/dhtmlxcommon.js"></script>
<script type="text/javascript"	src="${pageContext.request.contextPath}/js/dhtmlxMenu/dhtmlxmenu.js"></script>
<script type="text/javascript"	src="${pageContext.request.contextPath}/js/dhtmlxMenu/menu.js"></script>

<script type="text/javascript"	src="${pageContext.request.contextPath}/js/dhtmlx_layout/dhtmlxcommon.js"></script>
<script type="text/javascript"	src="${pageContext.request.contextPath}/js/dhtmlx_layout/dhtmlxlayout.js"></script>
<script type="text/javascript"	src="${pageContext.request.contextPath}/js/dhtmlx_layout/dhtmlxcontainer.js"></script>

<script type="text/javascript"	src="${pageContext.request.contextPath}/js/dhtmlx_Windows/dhtmlxwindows.js"></script>
<script type="text/javascript"	src="${pageContext.request.contextPath}/js/dhtmlx_Windows/dhtmlxcontainer.js"></script>
<script type="text/javascript"	src="${pageContext.request.contextPath}/js/dhtmlx_Windows/dhtmlxcommon.js"></script>

<script type="text/javascript"	src="${pageContext.request.contextPath}/js/dhtmlxCalendar/dhtmlxcommon.js"></script>
<script type="text/javascript"	src="${pageContext.request.contextPath}/js/dhtmlxCalendar/dhtmlxcalendar.js"></script>

<script type="text/javascript"	src="${pageContext.request.contextPath}/js/dhtmlx_Windows/dhtmlxcommon.js"></script>
<script type="text/javascript"	src="${pageContext.request.contextPath}/js/dhtmlx_Windows/dhtmlxcontainer.js"></script>

<script type="text/javascript"	src="${pageContext.request.contextPath}/js/dhtmlx_combo/dhtmlxcommon.js"></script>
<script type="text/javascript"	src="${pageContext.request.contextPath}/js/dhtmlx_combo/dhtmlxcombo.js"></script>
<script type="text/javascript"	src="${pageContext.request.contextPath}/js/dhtmlx_combo/ext/dhtmlxcombo_extra.js"></script>
<script type="text/javascript"	src="${pageContext.request.contextPath}/js/dhtmlx_combo/ext/dhtmlxcombo_group.js"></script>
<script type="text/javascript"	src="${pageContext.request.contextPath}/js/dhtmlx_combo/ext/dhtmlxcombo_whp.js"></script>

<script type="text/javascript"	src='${pageContext.request.contextPath}/${requestScope.configurationLocalgisWeb}/dwr/engine.js'></script>
<script type="text/javascript"	src='${pageContext.request.contextPath}/${requestScope.configurationLocalgisWeb}/dwr/util.js'></script>
<script type="text/javascript"	src='${pageContext.request.contextPath}/${requestScope.configurationLocalgisWeb}/dwr/interface/WFSGService.js'></script>
<script type="text/javascript"	src='${pageContext.request.contextPath}/${requestScope.configurationLocalgisWeb}/dwr/interface/MarkerService.js'></script>
<script type="text/javascript"	src='${pageContext.request.contextPath}/${requestScope.configurationLocalgisWeb}/dwr/interface/IncidenciaService.js'></script>
<script type="text/javascript"	src='${pageContext.request.contextPath}/${requestScope.configurationLocalgisWeb}/dwr/interface/SearchService.js'></script>
<!--  abh -->
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/dhtmlx_Windows/dhtmlxwindows.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/dhtmlx_Windows/skins/dhtmlxwindows_dhx_skyblue.css">
<!-- abh -->

<script type="text/javascript" src='${pageContext.request.contextPath}/js/jquery/jquery.js'></script>

<script type="text/javascript">
JQ = $;
$ = null;
</script>

<script type="text/javascript"	src='${pageContext.request.contextPath}/js/openlayers-2.5/lib/OpenLayers.js'></script>
<!-- script type="text/javascript" src='${pageContext.request.contextPath}/js/OpenLayers-2.11/lib/OpenLayers.js'></script>-->
<script type="text/javascript"	src='${pageContext.request.contextPath}/js/jquery/JQueryCollapse.js'></script>


<script type="text/javascript"	src='${pageContext.request.contextPath}/${requestScope.configurationLocalgisWeb}/dwr/interface/ReportService.js'></script>
<script type="text/javascript"	src='${pageContext.request.contextPath}/js/reports/report.js'></script>


<link rel="stylesheet" type="text/css"	href="${pageContext.request.contextPath}/css/jquery.css" />
						

<script type="text/javascript" src='${pageContext.request.contextPath}/${requestScope.configurationLocalgisWeb}/dwr/interface/SSOAuthCheckService.js'></script>
<script type="text/javascript" src='${pageContext.request.contextPath}/js/ssoAuth/ssoAuth.js'></script>
<script type="text/javascript">
if('${requestScope.configurationLocalgisWeb}'=='private')
	checkSSOAuth(<%= (Boolean)request.getSession().getAttribute("SSOActive") %>, '<%= (String)request.getSession().getAttribute("TokenAttribute") %>');
</script>


			<!-- this gmaps key generated for http://openlayers.org/dev/ -->
			<!-- Google Maps API key pamod-pre ABQIAAAAhxkUiBSZAcDmvS4JGP60OBQUI5KBusmYpig0pVVEUrJhxOje2xT0ZbzXygQ9-atC6cNgtaBbUvKMlQ -->
			<script type="text/javascript">
			  var hostname=window.location.hostname;
			  var googleKey='';
			  //alert('Mi host '+hostname);
			  if (hostname.indexOf('localhost') != -1) {
			     googleKey='ABQIAAAAjpkAC9ePGem0lIq5XcMiuhR_wWLPFku8Ix9i2SXYRVK3e45q1BQUd_beF8dtzKET_EteAjPdGDwqpQ';
			  }else   if (hostname.indexOf('localgis3.grupotecopy') != -1){
				     googleKey='AIzaSyB-b8JTZdB62OgpDIfFy4c2Fu7w8Bg76kc';
			  }else   if (hostname.indexOf('pamod-pre') != -1){
			     googleKey='ABQIAAAAhxkUiBSZAcDmvS4JGP60OBSudBqu4HsAFxU698-e7JiJshvE9BTN30Oer-Sbl9catJIFyC-vd8Oa8A';
			  
		      }else   if (hostname.indexOf('pamod-balanceada') != -1){
			     googleKey='ABQIAAAAhxkUiBSZAcDmvS4JGP60OBQA2jc6u-0iZywhyk6rbYzpY8qP8BR5tzjVx_6RSKRkocXQAZBRWVfq8g';
			  }			  
		      /*else   if (hostname.indexOf('localgis3.arrecife.es') != -1){
                  googleKey='AIzaSyDdHU13D1Ur8llm6wltBg4E29m-z1lFtdY';
               } 
		      else   if (hostname.indexOf('213.0.86.61') != -1){
                  googleKey='AIzaSyAKAE5EGCzxsEkMn0DFfzjDvgktSFkYNYU';
               } */
			  /*else {
			     alert('No se ha definido googleKey para el servidor '+ hostname +' en el servicio Google Maps');
			  }*/
			  
			  document.write("<script src='http://maps.google.com/maps?file=api&amp;v=2&amp;key=" + googleKey + "'></s"+"cript>");
				
			</script>	
			<!-- key API entorno localhost ABQIAAAAjpkAC9ePGem0lIq5XcMiuhR_wWLPFku8Ix9i2SXYRVK3e45q1BQUd_beF8dtzKET_EteAjPdGDwqpQ -->
			<!-- key API entorno PRE ABQIAAAAhxkUiBSZAcDmvS4JGP60OBSudBqu4HsAFxU698-e7JiJshvE9BTN30Oer-Sbl9catJIFyC-vd8Oa8A -->


			<!-- this gmaps key generated for http://openlayers.org/dev/ 
			<script
				src='http://maps.google.com/maps?file=api&amp;v=2&amp;key=ABQIAAAAjpkAC9ePGem0lIq5XcMiuhR_wWLPFku8Ix9i2SXYRVK3e45q1BQUd_beF8dtzKET_EteAjPdGDwqpQ'></script>
            -->
			<!-- script src="http://maps.google.com/maps/api/js?sensor=false"></script-->




			<!--[if lt IE 7]>
<script src="http://ie7-js.googlecode.com/svn/version/2.0(beta3)/IE8.js" type="text/javascript"></script>
<![endif]-->

			<jsp:include page="utils.jsp" />

			<script type="text/javascript">
    	var cont=-1;
        var map;
        
        //Capas Base
        var layer_base;                                                            
        var gmap;
        var gsat;
        
        var contextPathPrint = '${pageContext.request.contextPath}';
        var wpsRuta = '<bean:write name="urlWpsRutas" scope="request"/>';
        
        var reproject =true;
		
		//Variables globales
		var srid = <bean:write name="localgisMap" property="srid"/>;
		var originalSrid = '<bean:write name="localgisMap" property="originalSrid"/>';
		var mapid = '<bean:write name="localgisMap" property="mapid"/>';
		//srid = "4326"
		var projectionStr = "EPSG:" + srid;

		
		var displayProjectionStr = "EPSG:" + srid;

		var dhxWins,w1;
		var measureAreaControl,measureControl;
		
		var imgPath = OpenLayers._getScriptLocation()+ 'theme/localgis/img';
		var themeStr = OpenLayers._getScriptLocation()+ 'theme/localgis/style.css';

		
        OpenLayers.Util.onImageLoadError = function() {
            //No mostramos el componente
            this.style.display = "none";
            <%-- 
            /*
             * De momento no deshabilitamos la capa del layerswitcher porque 
             * errores puntuales podrían provocar que todas las capas estuvieran 
             * deshabilitadas
             */
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
            --%>
        } 
        
        var z;
        

		var minx = <bean:write name="localgisMap" property="minx"/>;
		var miny = <bean:write name="localgisMap" property="miny"/>;
		var maxx = <bean:write name="localgisMap" property="maxx"/>;
		var maxy = <bean:write name="localgisMap" property="maxy"/>;
		
		var extentEntidad = new OpenLayers.Bounds(minx, miny,maxx, maxy);
		
		
		var units="m"
		if (projectionStr=="EPSG:4326")
			units="degrees";
		
		
		var minScale = <%= Configuration.MIN_SCALE_LAYERS %>;
        var numZoomLevels = <%= Configuration.ZOOM_LEVELS %>;
        
        var maxExtentSpain = new OpenLayers.Bounds(<bean:write name="boundingBoxSpain" property="minx"/>, 
        											<bean:write name="boundingBoxSpain" property="miny"/>, 
        											<bean:write name="boundingBoxSpain" property="maxx"/>, 
        											<bean:write name="boundingBoxSpain" property="maxy"/>);

        var maxExtentComunidad = new OpenLayers.Bounds(<bean:write name="boundingBoxComunidad" property="minx"/>, 
				<bean:write name="boundingBoxComunidad" property="miny"/>, 
				<bean:write name="boundingBoxComunidad" property="maxx"/>, 
				<bean:write name="boundingBoxComunidad" property="maxy"/>);

        
        var layer_provincias_preName = '<img src="'+imgPath+'/layerProvincias.gif"/>&nbsp;';
        
		var medidas;
		var layerSwitcher;
		
		/**
			Inicializacion de OpenLayers
		*/
		function initOpenLayers() {
			

			utils_addLayout();
						
			//cont++;//Este contador se usa para ocultar las capas y mostrarlas
			
            OpenLayers.ProxyHost = "${pageContext.request.contextPath}/${requestScope.configurationLocalgisWeb}/getFeatureInfoProxy?language=<bean:write name="language"/>&urlWFSServer=";
                      
            map=createMap();
            
            //testGoogle();
            //return;

            // Layer switcher del mapa
            layerSwitcher = new OpenLayers.Control.LayerSwitcherLocalgis();
            map.addControlWithIdDiv(layerSwitcher, null, 'layerswitcher');
          
            //Toolbar de Localgis
           	utils_addToolBar(map);
            
			//Conectores de medidas            
            utils_addMedidas(map);
            
            // Barra de escala
            utils_addScaleBar(map);
        

            addLayersBase(map);
           
            map.zoomToMaxExtent();
                       
            //Capa de Ortofotos
            addOrtofotoLayer(map);
          
            //Capa de Provincias
			addProvinciasLayer(map);
            
			
			//addWMSLayer(map,"CARTO","http://www.cartociudad.es/wms/CARTOCIUDAD/CARTOCIUDAD","FondoUrbano");
			//addWMSLayer(map,"CATASTRO","http://ovc.catastro.meh.es/Cartografia/WMS/ServidorWMS.aspx","Catastro");
						
            //Capas Dinamicas
            addDynamicLayers(map);
            
            //Markers
            addMarkers(map);
            
            //Añadimos las marcas de posicion al layerSwitcher y lo maximizamos
            layerSwitcher.maximizeControl();
            
			//Realizamos el zoom            
            zoomToZone(map);
			
			//map.setCenter(new OpenLayers.LonLat(10.205188,48.857593), 5);
            //map.addControl( new OpenLayers.Control.LayerSwitcher() );
            //map.addControl( new OpenLayers.Control.PanZoomBar() );
            
            
            // Posicion del mouse
            var mousePosition = new OpenLayers.Control.MousePosition();
            map.addControl(mousePosition);			
            
            //OverViewMap
            addOverViewMap(map);
          
        }
		
		
		
		function testGoogle(){

	        //var satellite = new OpenLayers.Layer.Google( "Google Satellite" , {type: G_SATELLITE_MAP, 'maxZoomLevel':18} );
            //var satellite = new OpenLayers.Layer.Google( "Google Streets" , {type: G_SATELLITE_MAP, 'maxZoomLevel':18} );
            var satellite = new OpenLayers.Layer.Google( "Google Streets" , {'maxZoomLevel':18} );


            map.addLayers([satellite]);
            layer = new OpenLayers.Layer.WMS( "OpenLayers WMS", 
                    "http://labs.metacarta.com/wms/vmap0", {layers: 'basic', 'transparent':true}, 
                      {isBaseLayer: false} );
            layer.setVisibility(false);
            
        	var layercarto = new OpenLayers.Layer.WMS( "Cartociudad","http://www.cartociudad.es/wms/CARTOCIUDAD/CARTOCIUDAD",            				
    				{layers: 'FondoUrbano',transparent: 'TRUE'},{'reproject': true} );    

    			var layerprovincias = new OpenLayers.Layer.WMS( "Provincias","http://pamod-pre.c.ovd.interhost.com/mapserver/77/143/public/mapserv",            				
    				{layers: 'lcg_provincias',transparent: 'TRUE'},{'reproject': true} );    
    				
                map.addLayer(layercarto);
    			map.addLayer(layerprovincias);
            

            map.setCenter(new OpenLayers.LonLat(10.205188,48.857593), 5);
            map.addControl( new OpenLayers.Control.LayerSwitcher() );
            map.addControl( new OpenLayers.Control.PanZoomBar() );
                    
            return;	
		}
		
		
		
		/**
			Creacion del mapa
		*/
		function createMap(){
			 // Creación y configuracion del mapa
           var map = new OpenLayers.MapLocalgis( 
                                    $('mapDetail') ,
                                    {controls: [new OpenLayers.Control.MouseDefaults()],
                                     minScale: minScale,
                                     maxExtent: maxExtentSpain, 
                                     projection:projectionStr,
                                     displayProjection:displayProjectionStr,
                                     //projection: projectionStr, 
                                     //projection:"EPSG:900913",
                                     //displayProjection:projectionStr,
                                     tileSize: new OpenLayers.Size(512, 512),
                                    
                                     //displayProjection: "EPSG:4230",
                                     maxResolution: "auto", 
                                     numZoomLevels: numZoomLevels,                                                                      
                                     theme: themeStr, 
                                     units: units, 
                                     allOverlays: true,
                                     extentEntidad: extentEntidad,
                                     extentComunidad: maxExtentComunidad
                                     });
			 
			 /*
            map = new OpenLayers.Map( 'mapDetail' , 
                    { controls: [new OpenLayers.Control.MouseDefaults()] , 'numZoomLevels':20});*/

         
            
            return map;
		}
		
		/*************************************************/
		/*Capas Base		 	 */
		/*************************************************/
		function addLayersBase(map){
			
			
			//Para la version 3
            //var gphy = new OpenLayers.Layer.Google("Google Physical",{type: google.maps.MapTypeId.TERRAIN});
            //var gmap = new OpenLayers.Layer.Google("Google Streets", {numZoomLevels: 20});
            //var ghyb = new OpenLayers.Layer.Google("Google Hybrid",{type: google.maps.MapTypeId.HYBRID, numZoomLevels: 20});
            //var gsat = new OpenLayers.Layer.Google("Google Satellite",{type: google.maps.MapTypeId.SATELLITE, numZoomLevels: 22});
            //map.addLayers([gphy, gmap, ghyb, gsat]);*/
            
          
          	//Base
            layer_base = new OpenLayers.Layer.Image('BaseLayer', '${pageContext.request.contextPath}/img/blank.gif', maxExtentSpain, new OpenLayers.Size(1,1), null);                                                            
            map.addLayer(layer_base);
            
			//Para la version 2
            gmap = new OpenLayers.Layer.Google("Google Streets", {numZoomLevels: 20});
            gsat = new OpenLayers.Layer.Google("Google Satellite",{type: G_SATELLITE_MAP, numZoomLevels: 22});
            //gmap.displayInLayerSwitcher=true;
            //gsat.displayInLayerSwitcher=true;
            map.addLayers([ gmap, gsat]);
            
            
            //Open Street Map funciona en la 3            
            //var osmLayer = new OpenLayers.Layer.OSM("OpenStreetMap");
            //map.addLayer(osmLayer);
                    
		}
		

		
		
		
		
	
		
		/**
		  Añadimos la capa de ortofotos
		*/
		function addOrtofotoLayer(map){
            <logic:notEmpty name="ortofotoLayer" scope="request">
            var layer_ortofoto_name = '<bean:write name="ortofotoLayer" property="name"/>';
            var layer_ortofoto_preName = '<img src="'+imgPath+'/ortofoto.gif"/>&nbsp;';
            
            var layer_ortofoto = new OpenLayers.Layer.WMSLocalgis(
                                         layer_ortofoto_name, 
                                         '<bean:write name="ortofotoLayer" property="urlGetMapRequests" filter="false"/>', 
                                         {layers: '<bean:write name="ortofotoLayer" property="internalName"/>', 
                                          request: 'GetMap', 
                                          format: '<bean:write name="ortofotoLayer" property="format"/>', 
                                          transparent: false, 
                                          EXCEPTIONS: 'application/vnd.ogc.se_blank'}, 
                                         {gutter: 0, 
                                          buffer: <bean:write name="buffer" scope="request"/>, 
                                          isBaseLayer: false, 
                                          minScale: minScale,
                                          maxExtent: extentEntidad, 
                                          projection:projectionStr,
                                          displayProjection:displayProjectionStr,

                                          maxResolution: "auto", 
                                          numZoomLevels: numZoomLevels, 
                                          urlLegend: '<bean:write name="ortofotoLayer" property="urlGetLegendGraphicsRequests" filter="false"/>', 
                                          urlGetFeatureInfo: '<bean:write name="ortofotoLayer" property="urlGetFeatureInfoRequests" filter="false"/>', 
                                          preName: layer_ortofoto_preName,
                                          singleTile: <bean:write name="singleTile" scope="request"/>,
                                          idLayer: '<bean:write name="ortofotoLayer" property="idLayer"/>',
                                          isSystemLayer: true} );

            map.addLayer(layer_ortofoto);
            layer_ortofoto.setVisibility(false);
            </logic:notEmpty>
		}
		
		
		/**
		 Añadimos la capa de provincias
		*/
		function addProvinciasLayer(map){
			

			var layer_provincias_name = '<bean:write name="provinciasLayer" property="name"/>';
	        var layer_provincias_preName = '<img src="'+imgPath+'/layerProvincias.gif"/>&nbsp;';
	            
	        var layer_provincias = new OpenLayers.Layer.WMSLocalgis(
	                                           layer_provincias_name, 
	                                           '<bean:write name="provinciasLayer" property="urlGetMapRequests" filter="false"/>', 
	                                           {
	                                           layers: '<bean:write name="provinciasLayer" property="internalName"/>', 
	                                           request: 'GetMap', 
	                                           format: '<bean:write name="provinciasLayer" property="format"/>', 
	                                           transparent: true, 
	                                           //projection: projectionStr,
	                                           EXCEPTIONS: 'application/vnd.ogc.se_blank'},
	                                           {gutter: 0, 
	                                           buffer: <bean:write name="buffer" scope="request"/>, 
	                                           isBaseLayer: false, 
	                                           urlLegend: '<bean:write name="provinciasLayer" property="urlGetLegendGraphicsRequests" filter="false"/>', 
	                                           urlGetFeatureInfo: '<bean:write name="provinciasLayer" property="urlGetFeatureInfoRequests" filter="false"/>', 
	                                           preName: layer_provincias_preName,
	                                           singleTile: true,
	                                           idLayer: '<bean:write name="provinciasLayer" property="idLayer"/>',
	                                           //scales: [<%=Configuration.MIN_SCALE_LAYERS%>, <%=Configuration.MAX_SCALE_PROVINCIAS%>],
	                                           isSystemLayer: true,
	                                           reproject:reproject
	                                           });

	           map.addLayer(layer_provincias);
	           layer_provincias.setVisibility(true);
	            //layer_provincias.singleTile=true;
		}
		
		/**
		  Incluye un WMS en el mapa que se le pasa por parametros.
		*/
		function addWMSLayer(map,wms_name,url,wms_layer,baselayer){
			
	         var layer_wms = new OpenLayers.Layer.WMSLocalgis(
	                                           wms_name, 
	                                           url, 
	                                           {layers: wms_layer, 
	                                           request: 'GetMap', 
	                                           transparent: true, 
	                                           EXCEPTIONS: 'application/vnd.ogc.se_blank'},
	                                           {gutter: 0, 
	                                           isBaseLayer: baselayer, 
	                                           isSystemLayer: true,
	                                           reproject:reproject}
	                                           );

	           map.addLayer(layer_wms);
	           layer_wms.setVisibility(true);
		}
		
		/**
			Añade las capas dinamicas con las que estamos trabajando
		*/
		function addDynamicLayers(map,minscale){
			 // Creacion y carga de capas dinamicas
            <logic:iterate id="layer" name="layers" indexId="layerIndex">
                <logic:equal name="layer" property="externalLayer" value="false">
            var layer_<bean:write name="layerIndex"/>_name = getLayerName('<bean:write name="layer" property="name"/>');
            var layer_<bean:write name="layerIndex"/>_preName = '';
                </logic:equal>
                <logic:equal name="layer" property="externalLayer" value="true">
            var layer_<bean:write name="layerIndex"/>_name = '<bean:write name="layer" property="name"/>';
            var layer_<bean:write name="layerIndex"/>_preName = '<img src="'+imgPath+'/layerWMS.gif"/>&nbsp;';
                </logic:equal>
                
            var layer_<bean:write name="layerIndex"/> = new OpenLayers.Layer.WMSLocalgis(
                                                                layer_<bean:write name="layerIndex"/>_name, 
                                                                '<bean:write name="layer" property="urlGetMapRequests" filter="false"/>', 
                                                                {layers: '<bean:write name="layer" property="internalName"/>', 
                                                                 request: 'GetMap', 
                                                                 format: '<bean:write name="layer" property="format"/>', 
                                                                 transparent:true, 
                                                                 EXCEPTIONS: 'application/vnd.ogc.se_blank'},
                                                                {gutter: 0,
                                                                 buffer: <bean:write name="buffer" scope="request"/>, 
                                                                 minScale: minScale,
                                                                 maxExtent: extentEntidad, 
                                                                 projection:projectionStr,
                                                                 displayProjection:displayProjectionStr,

                                                                 maxResolution: "auto", 
                                                                 isBaseLayer: false, 
                                                                 reproject:reproject,
                                                                 urlLegend: '<bean:write name="layer" property="urlGetLegendGraphicsRequests" filter="false"/>', 
                                                                 urlGetFeatureInfo: '<bean:write name="layer" property="urlGetFeatureInfoRequests" filter="false"/>', 
                                                                 preName: layer_<bean:write name="layerIndex"/>_preName,
                                                                 //singleTile: <bean:write name="singleTile" scope="request"/>,
                                                                 singleTile: getSingleTile('<bean:write name="layer" property="name"/>'),
                                                                 //projection: '<bean:write name="layer" property="projection" filter="false"/>',
                                                                 isExternal: <bean:write name="layer" property="externalLayer"/>,
                                                                 idLayer: '<bean:write name="layer" property="idLayer"/>'
                                                                 } );
           
            <!-- Ponemos la capa del pnoa con menor transparencia-->
            <logic:notEqual name="layer" property="internalName" value="pnoa">
           		 layer_<bean:write name="layerIndex"/>.setOpacity(0.55);
	       </logic:notEqual>
	       
		  
    	   <logic:equal name="layer" property="internalName" value="xxx">
               <logic:notEqual name="layer" property="internalName" value="pnoa">
                   layer_<bean:write name="layerIndex"/>.singleTile=true;
           	</logic:notEqual>
       		</logic:equal>
       		
            map.addLayer(layer_<bean:write name="layerIndex"/>);
            layer_<bean:write name="layerIndex"/>.setVisibility(<bean:write name="layer" property="visible"/>);
            </logic:iterate>
		}
		
		/**
		  Añadimos las marcas
		*/
		function addMarkers(map){
			var markers = new OpenLayers.Layer.Markers("Marcas de posición",{layers: 'Pings', minScale: [51200]});
            markers.displayInLayerSwitcher = true;
            map.addMarkersLayer(markers);
            var markersAuxiliar = new OpenLayers.Layer.Markers("Markers",{layers: 'Markers'});
            markersAuxiliar.displayInLayerSwitcher = false;
            
            map.addMarkersAuxiliarLayer(markersAuxiliar);
            
            var boxes  = new OpenLayers.Layer.Boxes( "Boxes",{layers: 'Boxes'} );
            boxes.displayInLayerSwitcher = true;
            map.addBoxLayer(boxes);
            
 
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
                    layerSwitcher.addPositionMarkerLocalgis(positionMarkerLocalgis_<bean:write name="marker" property="markerid"/>);
                </logic:iterate>
            </logic:notEmpty>
            
		}
		
		/**
			Realizamos un zoom
		*/
		function zoomToZone(map){
			<logic:empty name="x" scope="request">
        	    // Visualizamos la entidad completo
    	        map.zoomToExtent(extentEntidad);
	        </logic:empty>
        
	        <logic:notEmpty name="x" scope="request">
	            // Situamos el mapa en el punto y en la escala solicitada
	            var lonlat = new OpenLayers.LonLat(<bean:write name="x" scope="request"/>, <bean:write name="y" scope="request"/>);
	            map.center = lonlat;
	            map.zoomTo(map.getZoomForResolution(<bean:write name="resolution" scope="request"/>));
	            map.setCenter(lonlat, map.getZoom(),false,false);
	            <logic:empty name="marker" scope="request">
	                OpenLayers.LocalgisUtils.showMarker(lonlat);
	            </logic:empty>
	            <logic:notEmpty name="marker" scope="request">
	                OpenLayers.LocalgisUtils.showCustomMarker(lonlat, '<bean:write name="marker" scope="request"/>');
	            </logic:notEmpty>
	        </logic:notEmpty>
		}
		
		/**
		 OverView Map
		*/
		function addOverViewMap(map){
			
			var layer_overview_base = new OpenLayers.Layer.Image(
                    'OverviewBaseLayer', 
                    '${pageContext.request.contextPath}/img/blank.gif', 
                    maxExtentSpain, 
                    new OpenLayers.Size(1,1), 
                    {
                    
                    projection:projectionStr,
                    displayProjection:displayProjectionStr,

                     maxExtent: maxExtentSpain, 
                     maxResolution: "auto", 
                     numZoomLevels: numZoomLevels, 
                     minScale: <%= Configuration.MIN_SCALE_PROVINCIAS %>, 
                     units: units});

			var layer_overview = new OpenLayers.Layer.WMSLocalgis(
                           'Overview', 
                           '<bean:write name="layerOverviewMap" property="urlGetMapRequests" filter="false"/>', 
                           {layers: '<bean:write name="layerOverviewMap" property="internalName"/>', 
                            request: 'GetMap', 
                            format: '<bean:write name="layerOverviewMap" property="format"/>', 
                            transparent:true, 
                            EXCEPTIONS: 'application/vnd.ogc.se_blank'},
                           {buffer: 0, 
                            isBaseLayer: false, 
                            urlLegend: '<bean:write name="layerOverviewMap" property="urlGetLegendGraphicsRequests" filter="false"/>', 
                            urlGetFeatureInfo: '<bean:write name="layerOverviewMap" property="urlGetFeatureInfoRequests" filter="false"/>', 
                            preName: layer_provincias_preName,
                            singleTile: <bean:write name="singleTile" scope="request"/>,
                            maxExtent: maxExtentSpain, 
                            projection:projectionStr,
                            displayProjection:displayProjectionStr,

                            maxResolution: "auto", 
                            numZoomLevels: numZoomLevels, 
                            minScale: <%= Configuration.MIN_SCALE_PROVINCIAS %>,
                            theme: themeStr, 
                            units: units} );
			var layersToOverview = [layer_overview_base, layer_overview];
			
  	
            
            // Overview map
            var overviewMap = new OpenLayers.Control.OverviewMapLocalgis({layers: layersToOverview});
            overviewMap.size = new OpenLayers.Size(180,133);
            map.addControl(overviewMap);
		}
		
        
       /*
        function defaultMouseDown(e){
        	if (!OpenLayers.Event.isLeftClick(e))
        	{
        		showMenu(e);
        		return false;
        	}
        	else
        	{
        		var mimenu = document.getElementById("menuR");
        	//mimenu.style.visibility = "hidden";
        	}
        }*/
          
        function handleMeasurements(event) {
        	var geometry = event.geometry;
        	var units = event.units;
        	var order = event.order;
        	var measure = event.measure;
        	var element = document.getElementById('resMeasure');
        	element.style.display = "block";
        	var out = "";
        	
        	if(order == 1) {
        		out += "Distancia: " + measure.toFixed(3) + " " + units;
        		
        	} else {
        		out += "Area: " + measure.toFixed(3) + " " + units + "<sup>2</" + "sup>";
        	}
        	//element.innerHTML = out;
        	statustemp.setText(out);
        	status = statustemp;
        //	document.getElementById(statustemp.id).innerHTML = out;
        } 
        
        function setPosition() {
            var xInput = document.getElementById('x');
            var xValue = xInput.value;
            var yInput = document.getElementById('y');
            var yValue = yInput.value;
            
            if (xValue!='' && yValue!='') {
                var lonlat = new OpenLayers.LonLat(xValue,yValue);        
                OpenLayers.LocalgisUtils.goToPosition(lonlat);
            }
            else {
                alert('Es necesario introducir ambos valores');
            }
        }
        
        function getStreetInformation() {
            var getStreetResultsReplyServer = {
                callback: function(data) {
                    var contentHTML;
                    if (data != undefined && data.length > 0) {
                        contentHTML = '<div id="popupLocalgisListResults">';
                        contentHTML += '<ul class="popupLocalgisList">';
                        for (var i = 0; i < data    .length; i++) {
                            if (data[i].exactResult) {
                                contentHTML += '<li class="popupLocalgisListItem" onclick="OpenLayers.LocalgisUtils.selectResult(this,'+data[i].posX+', '+data[i].posY+')">'+data[i].name+' - '+data[i].numero+'</li>';
                            } else {
                                contentHTML += '<li class="popupLocalgisListItem" onclick="OpenLayers.LocalgisUtils.selectResult(this,'+data[i].posX+', '+data[i].posY+')">'+data[i].name+'</li>';
                            }
                        }
                        contentHTML += '</ul>';
                        contentHTML += '</div><div align=center>';
                        contentHTML += '<table><tr><td>';
                        contentHTML += '<div id="popupLocalgisListGoResult" onclick="OpenLayers.LocalgisUtils.goToResultEntidad();"></div>';
                        contentHTML += '</td><td>';
                        contentHTML += '<div id="popupLocalgisListGoResultAddOrigen" onclick="OpenLayers.LocalgisUtils.addOrigen();"></div>';
                        contentHTML += '</td><td>';
                        if(esRuta)
        	    		{
                        	contentHTML += '<div id="popupLocalgisListGoResultAddAddDestino" onclick="OpenLayers.LocalgisUtils.addAddDestino();"></div>';
        				}else{
                        	contentHTML += '<div id="popupLocalgisListGoResultAddDestino" onclick="OpenLayers.LocalgisUtils.addDestino();"></div>';
                    	}
                        contentHTML += '</td></tr></table></div>';
                    } else {
                        contentHTML = 'No se ha encontrado ningún elemento.';
                    }
                    OpenLayers.LocalgisUtils.showPopup(contentHTML);
                },
                timeout:20000,
                errorHandler:function(message) { 
                    OpenLayers.LocalgisUtils.showError();
                }
            };
            var streetName = document.forms['search'].via.value;
            var streetNumber = parseInt(document.forms['search'].numero.value);
            if (streetName.trim() == '') {
                alert("Debe introducir un nombre de vía correcto");
                return;
            } else if (streetNumber != document.forms['search'].numero.value || streetNumber == 0) {
                alert("Debe introducir un número de vía correcto");
                return;
            } 

            OpenLayers.LocalgisUtils.showSearchingPopup();
            
            WFSGService.getStreetInformation('<bean:write name="streetInfoService" scope="request"/>', 
            								streetName, streetNumber, 
            								'<bean:write name="id_entidad" scope="request"/>',
            								'<bean:write name="localgisMap" property="srid" scope="request"/>',
            								getStreetResultsReplyServer);            
        }

        function getSearchInformation() {
            var getSearchResultsReplyServer = {
                callback: function(data) {
                    var contentHTML;
                    if (data != undefined && data.length > 0) {
                    	//contentHTML ='<div id = "ToolTip" >&nbsp;</div>';
                        contentHTML = '<div id="popupLocalgisListResults">';
                        
                        contentHTML += '<ul class="popupLocalgisList">';
                        for (var i = 0; i < data    .length; i++) {
                        	var sid=i.toString();
                        	contentHTML += '<li id="popupLocalgisListItem_'+sid+ '" class="popupLocalgisListItem"' 
                        	             +'onMouseOver="ShowToolTip(event,'+sid+",\'"+data[i].name+"\');\""
                        	             +'onMouseOut ="ShowToolTip(event,'+sid+",\'"+(data[i].name).substring(0,30)+"\');\""
                        			     
                        	             + ' onclick="OpenLayers.LocalgisUtils.selectResult(this,'+data[i].posX+', '+data[i].posY+')">'+(data[i].name).substring(0,30)+'</li>';
                        }
                        contentHTML += '</ul>';
                        contentHTML += '</div>';
                        contentHTML += '<div id="popupLocalgisListGoResult" onclick="OpenLayers.LocalgisUtils.goToResultEntidad();"></div>';
                    } else {
                        contentHTML = 'No se ha encontrado ningún elemento.';
                    }
                    OpenLayers.LocalgisUtils.showPopup(contentHTML);
                },
                timeout:20000,
                errorHandler:function(message) { 
                    OpenLayers.LocalgisUtils.showError();
                }
            };
            var searchText = document.forms['search'].termino.value;
        	if (this.map.activeLayer == null) {
                alert("Debe seleccionar una capa activa para obtener la información.");
                return;
        	} else if (searchText.trim() == '') {
                alert("Debe introducir un término a buscar");
                return;
            } 

            OpenLayers.LocalgisUtils.showSearchingPopup();
            
            //WFSGService.getStreetInformation('<bean:write name="streetInfoService" scope="request"/>', streetName, streetNumber, '<bean:write name="id_entidad" scope="request"/>','<bean:write name="localgisMap" property="srid" scope="request"/>', getStreetResultsReplyServer);
            SearchService.getSearchInformation(escape(this.map.activeLayer.idLayer), searchText.trim(), '<bean:write name="id_entidad" scope="request"/>', '<bean:write name="localgisMap" property="originalSrid" scope="request"/>', '<bean:write name="localgisMap" property="srid" scope="request"/>',getSearchResultsReplyServer);
        }
        
        function textHtmlHandler(){
            document.location.href = "${pageContext.request.contextPath}/${requestScope.configurationLocalgisWeb}";
        }
    
        function initDWR() {
            DWREngine.setTextHtmlHandler(textHtmlHandler);
        }    
        
        /*
        function waitToUpdate()
        { 
        	try{
			if (menu._isContextMenuVisible() && menu.contextAutoHide) { menu._hideContextMenu(); }
			}catch(e){} 
	    
		}*/
		 
	function submitEnterStreetInfo(e)
        {
                   var keycode;
                   if (window.event) keycode = window.event.keyCode;
                    else if (e) keycode = e.which;
                                           else return true;

                   if (keycode == 13)
                   {
                               getStreetInformation();
                               return false;
                   }
                   else
                   return true;
        }

        function submitSetPosition(e)
        {
                   var keycode;
                   if (window.event) keycode = window.event.keyCode;
                    else if (e) keycode = e.which;
                                           else return true;

                   if (keycode == 13)
                   {
                               setPosition();
                               return false;
                   }
                   else
                   return true;
        }
        
    	function submitEnterSearchInfo(e)
        {
                   var keycode;
                   if (window.event) keycode = window.event.keyCode;
                    else if (e) keycode = e.which;
                                           else return true;

                   if (keycode == 13)
                   {
                               getSearchInformation();
                               return false;
                   }
                   else
                   return true;
        }

        function seleccion(){
        	var indice = document.formul.combo.selectedIndex;
        	alert(indice);
        }
        
        //Esta funcion es la que maneja el combo para buscar, en funcion de que
        //hayamos seleccionado nos mostrará una interfaz u otra
        function Buscar() {

        	var indice = parseInt(document.formul.combo.selectedIndex);

            if(indice==1){
            	document.formul.combo.selectedIndex=0;
            	var formularioBusquedaViales='<div id="searchDiv" style="display:block;padding:8px;width:90%;"><form id="search" method="post" action="#"> <table width=95% cellpadding="3" cellspacing="3" border="0">  <tr>  <td colspan="3">Via</td><td><label><input class="inputTextField" type="text" name="via" id="via" onKeyPress="return submitEnterStreetInfo(event)"/></label><label>Nº <input class="inputTextField" name="numero" type="text" id="numero" style="width: 20px;" onKeyPress="return submitEnterStreetInfo(event)"/></label><a href="javascript:void(0);" onclick="getStreetInformation()">Buscar</a></td></tr></table></form></div>';
        		OpenLayers.LocalgisUtils.showPopup2(formularioBusquedaViales);
            }
            if(indice==2){
            	document.formul.combo.selectedIndex=0;
            	var formularioBusquedaCoordenadas='<div id="searchDiv" style="display:block;padding:8px;width:90%;"><form id="search" method="post" action="#"> <table width=95% cellpadding="3" cellspacing="3" border="0">  <tr>  <td colspan="3">X,Y</td><td><label><input class="inputTextField" name="x" type="text" id="x" size="15" style="width: 100px;" onKeyPress="return submitSetPosition(event)"/></label><label><input class="inputTextField" name="y" type="text" id="y" size="15" style="width: 100px;" onKeyPress="return submitSetPosition(event)"/></label><a href="javascript:void(0);" onclick="setPosition()" >Buscar</a></td></tr></table></form></div>';
        		OpenLayers.LocalgisUtils.showPopup2(formularioBusquedaCoordenadas);
            }
            if(indice==3){
                this.elementSelected = null;
                var getPlaceNameServicesReplyServer = {
                    callback: function(data) {
                        var contentHTML;
                        if (data != undefined && data.length > 0) {
                            contentHTML = '<form name="searchPlaceNameForm">';
                            contentHTML += '<table style="margin-left:auto;margin-right:auto;text-align:center" cellpadding="3">';
                            contentHTML += '<tr>';
                            contentHTML += '<td align="left">Servicio a Consultar:</td>';
                            contentHTML += '<td align="left">';
                            contentHTML += '<select name="serviceAndType">';
                            for (var i = 0; i < data.length; i++) {
                                contentHTML += '<option value="'+data[i].service+';'+data[i].type+';'+data[i].featureType+'">'+data[i].name+'</option>';
                            }
                            contentHTML += '</select>';
                            contentHTML += '</td>';
                            contentHTML += '</tr>';
                            contentHTML += '<tr><td align="left">Criterio de búsqueda:</td>';
                            contentHTML += '<td align="left"><input type="text" class="inputTextField" name="query" value="" size="20"/></td>';
                            contentHTML += '</tr>';
                            contentHTML += '</tr>';
                            contentHTML += '<td align="center" colspan="2"><label><input type="checkbox" name="withoutSpacialRestriction"/> <span style="position: relative; top: -4px;">Sin restricción espacial</span></label></td>';
                            contentHTML += '</tr>';
                            contentHTML += '<tr>';
                            contentHTML += '<td align="center" colspan="2"><div id="divButtonSearchPlaceName" style="margin-top: 6px;"><img class="imageButton" src="img/btn_aceptar.gif" alt="Aceptar" onclick="OpenLayers.Control.SearchLocalgis.searchInfo(document.searchPlaceNameForm.serviceAndType.options[document.searchPlaceNameForm.serviceAndType.selectedIndex].value, document.searchPlaceNameForm.query.value, \''+getPlaceNameServicesReplyServer.searchLocalgis.idEntidad+'\',\''+getPlaceNameServicesReplyServer.searchLocalgis.targetCRSCode+'\', document.searchPlaceNameForm.withoutSpacialRestriction.checked);"/></div>';
                            contentHTML += '</td>';
                            contentHTML += '</tr>';
                            contentHTML += '</table>';
                            contentHTML += '</form>';
                        } else {
                            contentHTML += 'No existe ningún servicio de búsqueda definido';
                        }
                        OpenLayers.LocalgisUtils.showPopup(contentHTML);
                    },
                    timeout:30000,
                    errorHandler:function(message) { 
                        OpenLayers.LocalgisUtils.showError();
                    },
                    searchLocalgis: this
                };
                
                WFSGService.getPlaceNameServices(getPlaceNameServicesReplyServer);
            }
            if(indice==4){
                document.formul.combo.selectedIndex=0;
            	if (this.map.activeLayer == null) {
                    alert("Debe seleccionar una capa activa para obtener la información.");
                    return;
            	}
            	
            	
            	var formularioBusquedaCapas='<div id="searchDiv" style="display:block;padding:8px;width:90%;"><form id="search" method="post" action="#"> <table width=95% cellpadding="3" cellspacing="3" border="0">  <tr>  <td colspan="3">Término</td><td><label><input class="inputTextField" type="text" name="termino" id="termino" onKeyPress="return submitEnterSearchInfo(event)"/></label><a href="javascript:void(0);" onclick="getSearchInformation()">Buscar</a></td></tr><tr></td>Puede utilizar el * como caracter comodin</td></tr></table></form></div>';
        		OpenLayers.LocalgisUtils.showPopup2(formularioBusquedaCapas);
            }
            if(indice==0){
            	alert("Debe elegir un criterio válido de búsqueda");
            }
        }
        
        
        function Collapse(){
        	var capas=document.getElementById("layerswitcher");
        	var mapDetail=document.getElementById("mapDetail");
        	var bannerR =document.getElementById("bannerMapaRight");
        	var imagen = document.getElementById("imagen");
        	
			if(cont==0){
        		capas.style.width="0";
        		mapDetail.style.width="100%";
        		mapDetail.style.left="0px";
        		bannerR.className="bannerMapaRight2";
        		imagen.src="js/openlayers-2.5/theme/localgis/img/expand-collapse2.GIF"
        		cont=1;
        	}
			else{
				capas.style.width="270px";
				mapDetail.style.width="79%";
				mapDetail.style.left="300px";
				bannerR.className="bannerMapaRight";
				imagen.src="js/openlayers-2.5/theme/localgis/img/expand-collapse.gif"
				cont=0;
			}
			
        }

        
        function cambiarCartografiaBase(){
        	
        	var indice = parseInt(document.forms.cartobase.combo.selectedIndex);
        	
        	if (indice==0)
        		map.setBaseLayer(layer_base);
           	else if (indice==1)
            	map.setBaseLayer(gmap);
            else if (indice==2)
                map.setBaseLayer(gsat);
        }
        
        function getLayerName(layername){
            if (layername.length > 50)
                    return layername.substring(0,50)+"....";
            else
                    return layername;
    	}
        
        function getLayerName2(layername){
        	if (layername.length > 35)
        		return layername.substring(0,35)+"<br>"+layername.substring(35,layername.length);
        	else
        		return layername;
        }
        
        function getSingleTile(layername){
        	//alert("singleTile "+layername);
        	if (layername="Concejos" || layername=="Municipios")
        		return true;
        	else
        		return false;
        }
        
        function escudoAyuntamiento(){
    		var escudoEntidad = "<bean:write name="escudoEntidad" scope="request"/>";
    		document.images["escudo"].src = escudoEntidad;
    	}

        </script>



			</script>

<script type="text/javascript">
function moveIt(obj, mvTop, mvLeft) {
	obj.style.position = "absolute";
	obj.style.top = mvTop;
	obj.style.left = mvLeft;
}
</script>

			<script language="javascript">
var X = 0;
var Y = 0;
var appType = navigator.appName;
 
 

//function ShowToolTip(event, text) {
//    if (appType == "Navigator") {
//        X = event.pageX;
//        Y = event.pageY;
//    } else {
//        X = event.clientX;
//        Y = event.clientY;
//    }
    
         
//    document.getElementById("ToolTip").innerHTML = text;
//    document.getElementById("ToolTip").style.display = '';
//    document.getElementById("ToolTip").style.top = Y + 10;
//    document.getElementById("ToolTip").style.left = X + 10;
//}

  function ShowToolTip(event, elemid, text){
	var item="popupLocalgisListItem_"+elemid;
    document.getElementById(item).innerHTML = text;

  }
 
function HideToolTip() {
    document.getElementById("ToolTip").style.display = 'none';
}
</script>
</head>
<!--  body onload="initOpenLayers();initDWR();"-->
<body
	onload="initOpenLayers();initDWR();initMenu('mapDetail',0);escudoAyuntamiento();"
	onresize="waitToUpdate();">
	<div id="wrapShowMap">
		<!-- Contenido de la pagina -->

			<div id="bannerMapa">
				<div id="bannerMapaLeft">
    				<img src="img/banner_left.gif" id="imgBannerLeft"/>
				</div>
				<div id="bannerMapaRight">
					<img src="img/user.gif" name="escudo" id="imgBannerRight"/>
				</div>	
			</div>
			<div id="desplegables">
				<!--  Formulario de Busqueda -->
				<div id="nuevoSearch">
					<form name="formul" class="buscar" id="combo_zone1">
					<!-- label for="combo">combo</label-->
						<select name="combo" id="combo" class="inputTextField_buscar"
							onChange="Buscar()">
							<option value="0">Buscar..</option>
							<option value="1">Viales</option>
							<option value="2">Coordenadas</option>
							<option value="3">Topónimos</option>
							<option value="4">Buscar en capas</option>
						</select>
					</form>
					<!--<button onClick="Collapse()">Colapsar</button>-->
				</div>
				<!--  Formulario de Seleccion de Cartografia Base -->
				<div id="cartobase">
					<form name="cartobase" id="cartobaseForm">
						<!-- label for="combo">combo</label-->
						<select name="combo" class="inputTextField_buscar"
							onChange="cambiarCartografiaBase()">
							<option value="0">Cartografia Base</option>
							<option value="1">Google</option>
							<option value="2">Google Earth</option>
						</select>
					</form>
					<!--<button onClick="Collapse()">Colapsar</button>-->
				</div>
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
		</div>
	<!--  Fin del contenido de la pagina -->
	</div>
</body>
</html:html>