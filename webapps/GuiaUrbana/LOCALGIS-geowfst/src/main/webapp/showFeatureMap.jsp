<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page import="com.localgis.web.core.config.Configuration"%>
<html:html xhtml="true">
<head>
   	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
    <!-- <meta http-equiv="X-UA-Compatible" content="IE=9" />-->
    
    <title>LocalGIS GeoWFST</title>
    <html:base ref="site"/>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/favicon.ico" type="image/x-icon" />

	<link href="${pageContext.request.contextPath}/css/showFeatureMap/genericStyles.css" rel="stylesheet" type="text/css" />
	<link href="${pageContext.request.contextPath}/css/showFeatureMap/showFeatureMapStyles.css" rel="stylesheet" type="text/css" />
	<link href="${pageContext.request.contextPath}/css/showFeatureMap/defaultStyles.css" rel="stylesheet" type="text/css" />
    <link href="${pageContext.request.contextPath}/css/showFeatureMap/<bean:write name="procedure_id"/>Styles.css" rel="stylesheet" type="text/css" />
      
    <script type="text/javascript" src='${pageContext.request.contextPath}/${requestScope.configurationLocalgisWeb}/dwr/engine.js'></script>
    <script type="text/javascript" src='${pageContext.request.contextPath}/${requestScope.configurationLocalgisWeb}/dwr/util.js'></script>
    <script type="text/javascript" src='${pageContext.request.contextPath}/${requestScope.configurationLocalgisWeb}/dwr/interface/WFSGService.js'></script>
    <script type="text/javascript" src='${pageContext.request.contextPath}/${requestScope.configurationLocalgisWeb}/dwr/interface/MarkerService.js'></script>   
    <script type="text/javascript" src='${pageContext.request.contextPath}/${requestScope.configurationLocalgisWeb}/dwr/interface/Sigm.js'></script>
    <!--<script type="text/javascript" src='${pageContext.request.contextPath}/${requestScope.configurationLocalgisWeb}/dwr/interface/DWR_FeatureData.js'></script>-->
    <script type="text/javascript" src='${pageContext.request.contextPath}/${requestScope.configurationLocalgisWeb}/dwr/interface/GeoService.js'></script>
    
    <!--   <script type="text/javascript" src="${pageContext.request.contextPath}/js/showFeatureMap/wfs/GMapWFS-0.5.js"></script>-->
    <!-- <script type="text/javascript" src='${pageContext.request.contextPath}/js/showFeatureMap/openlayers-2.10-localgis/lib/OpenLayers.js'></script> -->
    <script type="text/javascript" src='${pageContext.request.contextPath}/js/showFeatureMap/openlayers-2.11-localgis/lib/OpenLayers.js'></script>  
	<script type="text/javascript" src='${pageContext.request.contextPath}/js/showFeatureMap/<bean:write name="procedure_id"/>.js'></script>
	
	<!-- Google Maps API Key -->
	<!-- <script src="http://maps.google.com/maps/api/js?v=3.6&amp;sensor=false&amp;key=AIzaSyA0RjG7VHfhAWwZ2CDYGVikl3IkRk74rlk"></script>
	-->
    <script type="text/javascript">
    	var googleMaps = false;
    
	    var map;
        var contextPathPrint = '${pageContext.request.contextPath}';
        var wpsRuta = '<bean:write name="urlWpsRutas" scope="request"/>';
        var originalSrid = <bean:write name="localgisMap" property="srid"/>;
        var originalProjectionStr = new OpenLayers.Projection("EPSG:"+srid);
        //var srid = <bean:write name="localgisMap" property="srid"/>;
		
        OpenLayers.Util.onImageLoadError = function() {
            //No mostramos el componente
            this.style.display = "none";            
        }; 
		
		function errh(msg, exc) {
			//alert("Error: servicio DWR no encontrado");
		}
		dwr.engine.setErrorHandler(errh);
		
        //---WFS-------------------------------------------------------------
        
	    OpenLayers.ProxyHost = "${pageContext.request.contextPath}/cgi-bin/proxy.cgi?url="; 
	    var geoserverUrl = '<bean:write name="geoserverUrl" scope="request"/>'; 
	    var procedureWSUrl = '<bean:write name="procedureWSUrl" scope="request"/>'; 
	  	var localgisWFSG = '<bean:write name="localgisWFSG" scope="request"/>';    
	  	  
	    var id_entidad = '<bean:write name="id_entidad" scope="request"/>';
	    var id_municipio = '<bean:write name="id_municipio" scope="request"/>';
	    var id_feature = '<bean:write name="id_feature" scope="request"/>';
	    var layer_name = '<bean:write name="layer_name" scope="request"/>';
	    var procedure_id = '<bean:write name="procedure_id" scope="request"/>';
	    var procedure_name = '<bean:write name="procedure_name" scope="request"/>';
	    var scheme = '<bean:write name="scheme" scope="request"/>';	 
		var sld_style = '<bean:write name="sld_style" scope="request"/>';
		var feature_toolbarName = '<bean:write name="feature_toolbarName" scope="request"/>';
		var wfsUrlLegend = "";

		var style_property = '<bean:write name="style_property" scope="request"/>';		
		var style_type = "";
		
		var address_property = '<bean:write name="address_property" scope="request"/>';	
			
		var srid = <bean:write name="localgisMap" property="srid"/>;
      	if(googleMaps==true){
    		var srid="4326"; 
      	}
        var projectionStr = new OpenLayers.Projection("EPSG:"+srid);
        var displayProjectionStr = new OpenLayers.Projection("EPSG:"+srid);
        var imgPath = OpenLayers._getScriptLocation() + 'theme/localgis/img';
        var themeStr = OpenLayers._getScriptLocation() + 'theme/localgis/style.css';

        var minx = <bean:write name="localgisMap" property="minx"/>;
        var miny = <bean:write name="localgisMap" property="miny"/>;
        var maxx = <bean:write name="localgisMap" property="maxx"/>;
        var maxy = <bean:write name="localgisMap" property="maxy"/>;
        var extentEntidad = new OpenLayers.Bounds(minx, miny, maxx, maxy);
        var minScale = <%= Configuration.MIN_SCALE_LAYERS %>;
        var numZoomLevels = <%= Configuration.ZOOM_LEVELS %>;
        var maxExtentSpain = new OpenLayers.Bounds(<bean:write name="boundingBoxSpain" property="minx"/>, <bean:write name="boundingBoxSpain" property="miny"/>, <bean:write name="boundingBoxSpain" property="maxx"/>, <bean:write name="boundingBoxSpain" property="maxy"/>);
		
		OpenLayers.LocalgisUtils.startStyleType();
			
		var layerWfst = new OpenLayers.Layer.Vector();
	    var layerWfs = new OpenLayers.Layer.Vector();
	    
	    var layerWfsStyleMap = new OpenLayers.StyleMap();
	    var layerWfstStyleMap = new OpenLayers.StyleMap();   
		OpenLayers.LocalgisUtils.replaceStyle();
	    
		var style_currentPropertyAndValue = new Array();
		style_currentPropertyAndValue.push(new OpenLayers.LocalgisUtils.ClassPropertyAndValue("Polygon","strokeWidth","3"));	
		style_currentPropertyAndValue.push(new OpenLayers.LocalgisUtils.ClassPropertyAndValue("Polygon","strokeColor","#ffffff"));
		
		var style_hoverPropertyAndValue = new Array();				
		style_hoverPropertyAndValue.push(new OpenLayers.LocalgisUtils.ClassPropertyAndValue("Polygon","fillOpacity","0.5"));	
		style_hoverPropertyAndValue.push(new OpenLayers.LocalgisUtils.ClassPropertyAndValue("Polygon","strokeWidth","1"));	
		style_hoverPropertyAndValue.push(new OpenLayers.LocalgisUtils.ClassPropertyAndValue("Polygon","strokeColor","#ffffff"));
				 
		var style_selectedPropertyAndValue = new Array();
		style_selectedPropertyAndValue.push(new OpenLayers.LocalgisUtils.ClassPropertyAndValue("Polygon","strokeWidth","3"));		
		style_selectedPropertyAndValue.push(new OpenLayers.LocalgisUtils.ClassPropertyAndValue("Polygon","strokeColor","#ffffff"));
				
		var style_current_selectedPropertyAndValue = new Array();
		style_current_selectedPropertyAndValue.push(new OpenLayers.LocalgisUtils.ClassPropertyAndValue("Polygon","strokeWidth","2"));	
		style_current_selectedPropertyAndValue.push(new OpenLayers.LocalgisUtils.ClassPropertyAndValue("Polygon","strokeColor","#000000"));
		
        var layerSwitcher = new OpenLayers.Control.LayerSwitcherLocalgis();        

        var featureInfo = new OpenLayers.Control.FeatureInfoLocalgis({
        	webServiceHost: procedureWSUrl,
        	idEntidad: id_entidad,
        	procedureName: procedure_name,
        	onFeatureUnselectText: '<bean:write name="onFeatureUnselectText" scope="request"/>',
        	onNotFeatureInfoText: '<bean:write name="onNotFeatureInfoText" scope="request"/>'
        });         
        var featureSearch = new OpenLayers.Control.FeatureSearchLocalgis({
        	webServiceHost: procedureWSUrl,
        	procedureName: procedure_name,
        	idEntidad: id_entidad,
        	feature_toolbarName: feature_toolbarName,
        	onNotFeatureSearchText: '<bean:write name="onNotFeatureSearchText" scope="request"/>'
        });
        var streetSearch = new OpenLayers.Control.StreetSearchLocalgis({
        	webServiceHost: localgisWFSG,
        	idEntidad: id_entidad,
        	srid: srid
        });	       
        
        //if(googleMaps==true)
        	//var srid="4326"; 
        //var srid="4326";
        //var srid="900913";
        
        
        
        //var projectionStr = new OpenLayers.Projection("EPSG:"+srid);
        //var displayProjectionStr = new OpenLayers.Projection("EPSG:4326");       
       
        //GoogleMaps
        if(googleMaps==true){
        	var projectionStr = new OpenLayers.Projection("EPSG:900913");
       	 	var displayProjectionStr = new OpenLayers.Projection("EPSG:4326");
       	 	var reproject = true;
        }
       //var displayProjectionStr = projectionStr;
       
        //var reproject = true;
        var imgPath = OpenLayers._getScriptLocation() + 'theme/localgis/img';
        var themeStr = OpenLayers._getScriptLocation() + 'theme/localgis/style.css';

        //var minx = <bean:write name="localgisMap" property="minx"/>;
        //var miny = <bean:write name="localgisMap" property="miny"/>;
        //var maxx = <bean:write name="localgisMap" property="maxx"/>;
        //var maxy = <bean:write name="localgisMap" property="maxy"/>;
        
        //var minx = -6.852663369215552;
		//var miny = 43.100385180289244;
		//var maxx = -6.5252171810725415;
		//var maxy = 43.37141724341885;
		
		//GoogleMaps
		if(googleMaps==true){
			var minx = -777306.79884551;
			var miny = 5332889.2524654;
			var maxx = -719507.71894591;
			var maxy = 5369013.6774026;
			var extentEntidad = new OpenLayers.Bounds(minx, miny, maxx, maxy); 
			var minScale = <%= Configuration.MIN_SCALE_LAYERS %>;
	        var numZoomLevels = <%= Configuration.ZOOM_LEVELS %>;  
		} 
        //extentEntidad = extentEntidad.transform(originalProjectionStr,displayProjectionStr);
             
		var units="m";
		//if (projectionStr=="EPSG:4326")
				//units="degrees";
        //var maxExtentSpain = new OpenLayers.Bounds(<bean:write name="boundingBoxSpain" property="minx"/>, <bean:write name="boundingBoxSpain" property="miny"/>, <bean:write name="boundingBoxSpain" property="maxx"/>, <bean:write name="boundingBoxSpain" property="maxy"/>);
       // maxExtentSpain = maxExtentSpain.transform(originalProjectionStr,displayProjectionStr);
       // alert(maxExtentSpain.toBBOX());
        // var maxExtentSpain = new OpenLayers.Bounds(-19.461230074334146, 
         //										24.82911466885096, 
         //										4.268864130274536, 
         //										45.32983612529813);
        //  var maxExtentSpain = new OpenLayers.Bounds(-22.28027, 30.25907, 12.78809, 47.15984).transform(projectionStr,displayProjectionStr);
         // var maxExtentSpain = new OpenLayers.Bounds(-22.94002, 30.75169, 12.08439, 47.51752);
        if(googleMaps==true)
        	var maxExtentSpain = new OpenLayers.Bounds(-2519371.1407685,3615208.9250292,1394204.7067315,6061193.8297167);
        
        var layer_base, gstr, gsat, ghib;
        
        //-------------------------------------------------------------------
        
        
        
        function createMap(){
			 // Creación y configuracion del mapa
			var map = new OpenLayers.MapLocalgis( 
                                    $('mapDetail') ,
                                    {controls: [/*new OpenLayers.Control.MousePosition()*/],
                                     minScale: minScale,
                                     maxExtent: maxExtentSpain,
                                     projection: projectionStr,
                                     maxResolution: "auto", 
                                     numZoomLevels: numZoomLevels, 
                                     theme: themeStr, 
                                     feature_toolbarName: feature_toolbarName,
                                     units: units, 
                                     extentEntidad: extentEntidad});
			if(googleMaps==true)
				map.controls.displayProjection = displayProjectionStr;
			
			return map;
		}
        
        
		function addLayersBase(map){
			
			
			//Para la version 3
            //var gphy = new OpenLayers.Layer.Google("Google Physical",{type: google.maps.MapTypeId.TERRAIN});
            //var gstr = new OpenLayers.Layer.Google("Google Streets", {numZoomLevels: 20});
            //var ghyb = new OpenLayers.Layer.Google("Google Hybrid",{type: google.maps.MapTypeId.HYBRID, numZoomLevels: 20});
            //var gsat = new OpenLayers.Layer.Google("Google Satellite",{type: google.maps.MapTypeId.SATELLITE, numZoomLevels: 22});
            //map.addLayers([gphy, gstr, ghyb, gsat]);*/
            
          
          	//Base
            layer_base = new OpenLayers.Layer.Image('BaseLayer', '${pageContext.request.contextPath}/img/blank.gif', maxExtentSpain, new OpenLayers.Size(1,1), 
            	{projection: projectionStr, 
                //displayProjection: displayProjectionStr,                
            	units: units/*,
                reproject:reproject*/});                                                            
            map.addLayer(layer_base);
            
			//Para la version 2
            //gstr = new OpenLayers.Layer.Google("Google Streets", {maxExtent: maxExtentSpain, numZoomLevels: 20});
            //gsat = new OpenLayers.Layer.Google("Google Satellite", {type: G_SATELLITE_MAP, maxExtent: maxExtentSpain, numZoomLevels: 22});

            
            //gstr = new OpenLayers.Layer.Google("Google Streets", {type: google.maps.MapTypeId.ROADMAP,/*maxExtent: maxExtentSpain,*/ numZoomLevels: 20});
            //gsat = new OpenLayers.Layer.Google("Google Satellite", {type: google.maps.MapTypeId.SATELLITE,/* maxExtent: maxExtentSpain,*/ numZoomLevels: 22});
           // gsat = new OpenLayers.Layer.Google("Google Satellite", {type: google.maps.MapTypeId.SATELLITE, maxExtent: maxExtentSpain, numZoomLevels: 22});
			
           //Google v3
           if(googleMaps==true){
           		gstr = new OpenLayers.Layer.Google("Google Streets", {type: google.maps.MapTypeId.ROADMAP, maxExtent: maxExtentSpain, numZoomLevels: 20});
           		gsat = new OpenLayers.Layer.Google("Google Satellite", {type: google.maps.MapTypeId.SATELLITE, maxExtent: maxExtentSpain, numZoomLevels: 20});
           		ghib = new OpenLayers.Layer.Google("Google Hybrid", {type: google.maps.MapTypeId.HYBRID, maxExtent: maxExtentSpain, numZoomLevels: 20});
           }
           
           //Google v2
		   //gstr = new OpenLayers.Layer.Google("Google Streets", {type: G_NORMAL_MAP, maxExtent: maxExtentSpain, numZoomLevels: 20});
           //gsat = new OpenLayers.Layer.Google("Google Satellite", {type: G_SATELLITE_MAP, maxExtent: maxExtentSpain, numZoomLevels: 20});
           //ghib = new OpenLayers.Layer.Google("Google Hybrid", {type: G_HYBRID_MAP, maxExtent: maxExtentSpain, numZoomLevels: 20});
            
			if(googleMaps==true)
            	map.addLayers([gstr, gsat, ghib]);

           // setMapBaseLayer(1);
		}
		
        function setMapBaseLayer(indice){        	
        	if (indice=='None')
        		map.setBaseLayer(layer_base);
           	else if (indice=='Street')
            	map.setBaseLayer(gstr);
            else if (indice=='Satellite')            	
                map.setBaseLayer(gsat);
            else if (indice=='Hybrid')
                map.setBaseLayer(ghib);        	
        	if(indice!='')
        		document.getElementById("GoogleSelectorBtn").className = ('googleSelectorBtn' + indice);
        	//alert(map.getExtent());
        }
        
        function initOpenLayers() {
          
            //OpenLayers.ProxyHost = "${pageContext.request.contextPath}/${requestScope.configurationLocalgisWeb}/getFeatureInfoProxy?language=<bean:write name="language"/>&urlWFSServer=";
            // Creación y configuracion del mapa
            map = createMap();


            // Layer switcher del mapa
            
            map.addControlWithIdDiv(layerSwitcher, null, 'layerSwitcher'); 
            
            var toolbarnav = new OpenLayers.Control.ToolbarNavLocalgis({feature_toolbarName: feature_toolbarName}); 			
        	map.addControlWithIdDiv(toolbarnav, null, 'toolbarNav');
        	
        	
    
	        // Barra de escala
	      /* 
	        var scaleBar = new OpenLayers.Control.ScaleBarLocalgis();
	        scaleBar.displaySystem = 'metric';
	        scaleBar.abbreviateLabel = true;
	        map.addControl(scaleBar);
	      */ 
	      
	      addLayersBase(map);

            var layer_provincias_name = '<bean:write name="provinciasLayer" property="name"/>';
            var layer_provincias_preName = '<img src="'+imgPath+'/layerProvincias.gif"/>&nbsp;';
       /*     
            var layer_provincias = new OpenLayers.Layer.WMSLocalgis(
                                           layer_provincias_name, 
                                           geoserverUrl + "wms", 
                                           {version:'1.1.1',
                                           layers: '<bean:write name="provinciasLayer" property="internalName"/>', 
                                           request: 'GetMap', 
                                           format: '<bean:write name="provinciasLayer" property="format"/>', 
                                           transparent: true, 
                                           EXCEPTIONS: 'application/vnd.ogc.se_blank'},
                                           {gutter: 0, 
                                           buffer: <bean:write name="buffer" scope="request"/>, 
                                           isBaseLayer: false, 
                                           urlLegend: '<bean:write name="provinciasLayer" property="urlGetLegendGraphicsRequests" filter="false"/>', 
                                           urlGetFeatureInfo: '<bean:write name="provinciasLayer" property="urlGetFeatureInfoRequests" filter="false"/>', 
                                           preName: layer_provincias_preName,
                                           singleTile: <bean:write name="singleTile" scope="request"/>,
                                           idLayer: '<bean:write name="provinciasLayer" property="idLayer"/>',
                                           isSystemLayer: true,
                                           projection: projectionStr,
                                           //displayProjection: projectionStr,
                                           units: units
                                           } );
       */

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
            
            if(googleMaps==true){
            	layer_provincias.displayProjection = displayProjectionStr;
            	layer_provincias.reproject = reproject;
            }                 
            map.addLayer(layer_provincias);
            layer_provincias.setVisibility(true);
                   
            
            var filter = new OpenLayers.Filter.Comparison({
				type: OpenLayers.Filter.Comparison.EQUAL_TO,
				property: 'id_municipio',
				value: id_municipio
			});	            
            var parser = new OpenLayers.Format.Filter.v1_1_0();
            var filterAsXml = parser.write(filter);
            var xml = new OpenLayers.Format.XML();
            var filterAsString = xml.write(filterAsXml);
            
            // Creacion y carga de capas dinamicas
            <logic:iterate id="layer" name="layers" indexId="layerIndex">
	if('<bean:write name="layer" property="internalName"/>'!=layer_name){           
                <logic:equal name="layer" property="externalLayer" value="false">
            var layer_<bean:write name="layerIndex"/>_name = '<bean:write name="layer" property="name"/>';
            var layer_<bean:write name="layerIndex"/>_preName = '';
                </logic:equal>
                <logic:equal name="layer" property="externalLayer" value="true">
            var layer_<bean:write name="layerIndex"/>_name = '<bean:write name="layer" property="name"/>';
            var layer_<bean:write name="layerIndex"/>_preName = '<img src="'+imgPath+'/layerWMS.gif"/>&nbsp;';
                </logic:equal>
                                
            var layer_<bean:write name="layerIndex"/> = new OpenLayers.Layer.WMSLocalgis(
            													layer_<bean:write name="layerIndex"/>_name, 
            													
            													//geoserverUrl + "wms", 
            													'<bean:write name="layer" property="urlGetMapRequests" filter="false"/>', 
            													
            													{version:'1.1.1', 
            													layers: '<bean:write name="layer" property="internalName"/>',
            													format: '<bean:write name="layer" property="format"/>', 
                                                                transparent:true},            												
                                                                {gutter: 0,
                                                                 buffer: <bean:write name="buffer" scope="request"/>, 
                                                                 minScale: minScale,
                                                                 maxExtent: extentEntidad,
                                                                 projection: map.displayProjection,
                                                                 //projection: projectionStr, 
                                                                 //projection: displayProjectionStr,
                                                                 //displayProjection: displayProjectionStr,
                                                                 //reproject: true,
                                                                 maxResolution: "auto", 
                                                                 isBaseLayer: false, 
                                                                 urlLegend: '<bean:write name="layer" property="urlGetLegendGraphicsRequests" filter="false"/>', 
                                                                 urlGetFeatureInfo: '<bean:write name="layer" property="urlGetFeatureInfoRequests" filter="false"/>', 
                                                                 preName: layer_<bean:write name="layerIndex"/>_preName,
                                                                 singleTile: <bean:write name="singleTile" scope="request"/>,                                                                 
                                                                 isExternal: <bean:write name="layer" property="externalLayer"/>,
                                                                 idLayer: '<bean:write name="layer" property="idLayer"/>',                                                              
                                                                 transitionEffect: true,
                                                                 units: units} );
                
            if(googleMaps==true){
            	layer_<bean:write name="layerIndex"/>.displayProjection = displayProjectionStr;
            	layer_<bean:write name="layerIndex"/>.reproject = reproject;
            }    
            layer_<bean:write name="layerIndex"/>.setOpacity(0.85);            
            map.addLayer(layer_<bean:write name="layerIndex"/>);
                
            layer_<bean:write name="layerIndex"/>.params["FILTER"] = filterAsString;
            layer_<bean:write name="layerIndex"/>.redraw();
            
            layer_<bean:write name="layerIndex"/>.setVisibility(<bean:write name="layer" property="visible"/>);
    }   
	else{ 
		wfsUrlLegend = '<bean:write name="layer" property="urlGetLegendGraphicsRequests" filter="false"/>';
	}    
		</logic:iterate>
            
            
            var markers = new OpenLayers.Layer.Markers("Marcas de posición",{layers: 'Pings', minScale: [51200]});
            markers.displayInLayerSwitcher = true;
            var markersAuxiliar = new OpenLayers.Layer.Markers("Markers",{layers: 'Markers'});
            markersAuxiliar.displayInLayerSwitcher = false;
            //map.addMarkersLayer(markers);
            map.addMarkersAuxiliarLayer(markersAuxiliar);
            
            //Añadimos las marcas de posicion
            /*
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
            */
            

            var minScaleProvincias = <%= Configuration.MIN_SCALE_PROVINCIAS %>;
     		/*     		
     	      var layer_overview_base = new OpenLayers.Layer.Image(
                    'OverviewBaseLayer', 
                    '${pageContext.request.contextPath}/img/blank.gif', 
                    maxExtentSpain, 
                    new OpenLayers.Size(1,1), 
                    {projection: projectionStr, 
                     maxExtent: maxExtentSpain, 
                     maxResolution: "auto", 
                     numZoomLevels: numZoomLevels,
                     minScale: minScaleProvincias, 
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
					                            projection: projectionStr, 
					                            maxResolution: "auto", 
					                            numZoomLevels: numZoomLevels, 
					                            minScale: minScaleProvincias,
					                            theme: themeStr, 
					                            units: units );
					var layersToOverview = [layer_overview_base, layer_overview];
				*/
			//--NUEVO--------------------------------------------------->            

					try{
		            	if(sld_style!='') OpenLayers.loadURL("index.jsp", null, OpenLayers.LocalgisUtils.completeLayerWfs, OpenLayers.LocalgisUtils.completeLayerWfs);	
					}
					catch(err){
						alert("ERROR: PARSEO ESTILOS (" + err + ")");
					}
					finally{  
						//MULTI MUNICIPIOS -> WHILE ID_MUNICIPIOS Y FILTRO AND
						var filterMunicipio = new OpenLayers.Filter.Comparison({
							type: OpenLayers.Filter.Comparison.EQUAL_TO,
							property: 'id_municipio',
							value: id_municipio
						});	
					  	
						var filterOtrosVados = new OpenLayers.Filter.Comparison({
			                type: OpenLayers.Filter.Comparison.NOT_EQUAL_TO,
			                property: 'id_feature',
			                value: id_feature
			            });
						
						var filterVadoTramite = new OpenLayers.Filter.Comparison({
			                type: OpenLayers.Filter.Comparison.EQUAL_TO,
			                property: 'id_feature',
			                value: id_feature
			            });				
						
						var layerWfsFilterAll = new OpenLayers.Filter.Logical({
							type: OpenLayers.Filter.Logical.AND,
							filters: [filterMunicipio,filterOtrosVados]
						});
						
						var layerWfstfilterAll = new OpenLayers.Filter.Logical({
							type: OpenLayers.Filter.Logical.AND,
							filters: [filterMunicipio,filterVadoTramite]
						});
																
						//-Creacion de capa de (geoserver - WFST)-//
						layerWfs = new OpenLayers.Layer.VectorLocalgis(
							"<bean:write name='feature_nameFirst'/> Anterior", 
							{
								styleMap: layerWfsStyleMap,
								filter: layerWfsFilterAll,
								strategies: [new OpenLayers.Strategy.BBOX(), new OpenLayers.Strategy.Save()/*, new OpenLayers.Strategy.Fixed()*/],
                               	projection: displayProjectionStr,
								protocol: new OpenLayers.Protocol.WFS({			
									version: "1.1.0",
									url:  geoserverUrl + "wfs",
									featureType: layer_name,
									srsName: "EPSG:" + srid,
									featureNS: "http://www.opengeospatial.net/cite",
									geometryName: "GEOMETRY",
									schema: geoserverUrl + "wfs?service=WFS&version=1.1.0&request=DescribeFeatureType&TypeName=cite:" + layer_name	
								}),
								urlLegend: wfsUrlLegend
							}					
						);
						map.addLayer(layerWfs);			
			           	/*		
						var readFormat = new OpenLayers.Format.GML({
							'internalProjection': new OpenLayers.Projection("EPSG:" + srid),
							'externalProjection': new OpenLayers.Projection("EPSG:900913")
							});
						
						var outFormat = new OpenLayers.Format.GML({
							'internalProjection': new OpenLayers.Projection("EPSG:4326"),
							'externalProjection':new OpenLayers.Projection("EPSG:" + srid)
							});
						*/
						//-Creacion de capa de (geoserver - WFST)-//
						layerWfst = new OpenLayers.Layer.VectorLocalgis(
							"<bean:write name='feature_nameFirst'/> en Tramite", 
							{
								styleMap: layerWfstStyleMap,
								filter: layerWfstfilterAll,
								strategies: [new OpenLayers.Strategy.BBOX(), new OpenLayers.Strategy.Save()/*, new OpenLayers.Strategy.Fixed()*/],
								projection: displayProjectionStr,
								protocol: new OpenLayers.Protocol.WFS({			
									version: "1.1.0",
									url:  geoserverUrl + "wfs",
									featureType: layer_name,
									srsName: "EPSG:" + srid,									
									featureNS: "http://www.opengeospatial.net/cite",
									geometryName: "GEOMETRY",
									schema: geoserverUrl + "wfs?service=WFS&version=1.1.0&request=DescribeFeatureType&TypeName=cite:" + layer_name	
									
								}),
								numFeaturesLimit: 1,
								urlLegend: wfsUrlLegend
							}					
						);						
						map.addLayer(layerWfst);
									
						
						//Añadimos las marcas de posicion al layerSwitcher y lo maximizamos
			            layerSwitcher.maximizeControl();  
				
			            var toolbarwfst = new OpenLayers.Control.<bean:write name='feature_toolbarName'/>(  
								{},
					            layerWfst, 
					            [layerWfst,layerWfs], 
					            {"featureadded": OpenLayers.LocalgisUtils.insert,
					            "afterfeaturemodified": OpenLayers.LocalgisUtils.afterModify},
								{"featureselected": OpenLayers.LocalgisUtils.selectAndModify,
					            "featureunselected": OpenLayers.LocalgisUtils.unselectAndModify}
					  	); 
					    map.addControlWithIdDiv(toolbarwfst, null, 'toolbarWfst');    
					      
						map.addControlWithIdDiv(featureInfo, null, 'featureInfo');
     	     		
			 	     	map.addControlWithIdDiv(featureSearch, null, 'featureSearch');
					 	featureSearch.setLayersSearch([layerWfst,layerWfs]);	 	     	
					   					 	
						map.addControlWithIdDiv(streetSearch, null, 'streetSearch'); 
								            
			    		map.zoomToExtent(extentEntidad);	
			    	
			    		OpenLayers.LocalgisUtils.waitForFunction("OpenLayers.LocalgisUtils.startFeatureSearch()",5000);
				
					}
		    
			//<----FIN-NUEVO------------------------------------------------------------------	     				 
				     		
    	}
        
        
//------------------------END FUNCTIONS------------------------------------------------------------        

	//window.confirm = null;
     //js/showFeatureMap/lcg_vados.js	 
	//if(typeof onBeforeExitFunction == 'function'){
	 	//window.onbeforeunload = onBeforeExitFunction;
		//window.onbeforeunload = null;
		//window.onbeforeunload = OpenLayers.LocalgisUtils.waitForFunction('alert("2")', 100000);
	//}
	//if(typeof onExitFunction == 'function'){
	 	//window.onunload = onExitFunction;
		//window.onunload = OpenLayers.LocalgisUtils.waitForFunction('alert("2")', 100000);
	//}
	
</script>
 
</head>
    <body onload="initOpenLayers();">
 
                    <!-- Contenido de la pagina -->        
                    <div id="map">        
                        <div id="mapDetail">
                        	<div id="mapTopShadow"></div>
                        	<div id="mapLeftShadow"></div>
                        	<div id="mapRightShadow"></div>
                        	<div id="mapBottomShadow"></div>
                        </div>                          
          				<div id="toolbarNavBox">         				    
                        	<div id="toolbarNav" class="toolbarNav_collapsed"></div> 
                        	<div id="toolbarNavBorderBottom">
                        		<div id="toolbarNavBorderBottomBtn" class="BorderBottomBtn_collapsed" onclick="OpenLayers.LocalgisUtils.borderBottomBtn_onClick(this,'toolbarNav');" title="Herramientas de navegación"></div>
                        	</div>
                        </div>
                        <div id="toolbarWfstBox" class="invisible">                       	
                        	<div id="toolbarWfst" class="toolbarWfst_collapsed"></div>
                        	<div id="toolbarWfstBorderBottom">
                        		<div id="toolbarWfstBorderBottomBtn" class="BorderBottomBtn_collapsed" onclick="OpenLayers.LocalgisUtils.borderBottomBtn_onClick(this,'toolbarWfst');" title="Herramientas de gestión de <bean:write name='feature_name'/>"></div>
                        	</div> 
                        </div>    
                        <div id="infoPanelBox">
			            	<div id="infoPanelConceleable" class="infoPanelConceleable_collapsed">                    		
				             	<div id="infoPanelBorderLeft"></div>
				             	<div id="infoPanelBorderRight"></div>
				                <div id="infoPanel">	                       		
				                    <div id="layerSwitcher" class="infoPanelChild_expanded">
				                  		<div class="SelectionBtn" onclick="OpenLayers.LocalgisUtils.infoPanel_onClick('layerSwitcher')">SELECTOR DE CAPAS</div> 		                     				
				                    </div>
				                    <div id="featureInfo" class="infoPanelChild_collapsed">
				                      	<div class="SelectionBtn" onclick="OpenLayers.LocalgisUtils.infoPanel_onClick('featureInfo')">INFORMACIÓN DE <bean:write name="feature_nameUpper"/></div>
				                    </div>
				                    <div id="featureSearch" class="infoPanelChild_collapsed">
				                       	<div class="SelectionBtn" onclick="OpenLayers.LocalgisUtils.infoPanel_onClick('featureSearch')">BÚSQUEDA DE <bean:write name="feature_nameUpper"/></div>
				                     </div>
				                    <div id="streetSearch" class="infoPanelChild_collapsed">
				                       	<div class="SelectionBtn" onclick="OpenLayers.LocalgisUtils.infoPanel_onClick('streetSearch')">BÚSQUEDA DE CALLES</div>
				                     </div>
				                </div> 
			              	</div> 
			               	<div id="infoPanelBorderBottom">
			                    <div id="infoPanelBorderBottomSemiBtn" class="BorderBottomSemiBtn_collapsed" onclick="OpenLayers.LocalgisUtils.borderBottomSemiBtn_onClick(this,'infoPanelConceleable');" title="Panel de Información"></div>
			                 </div>
			            </div>     
                        <div id="head">                        	
                        	<div id="headBox">                        	 	  
	                        	<div id="headConceleable" class="headConceleable_expanded">	                        			                      	                    		                                     
			                        <div id="headImgCenter">
			                        	<script type="text/javascript"> if(googleMaps == true){ 
			                        		document.write('<div id="GoogleSelectorBtn" class="googleSelectorBtnNone"></div>' +			                        
				                       		'<div id="headGoogleNoneBtn" class="googleNoneBtn" title="Sin Capa Base" onClick="setMapBaseLayer(\'None\')"></div>' +
				                        	'<div id="headGoogleStreetBtn" class="googleStreetBtn" title="Capa Base de Google - Mapa de Calles" onClick="setMapBaseLayer(\'Street\')"></div>' +			                        
				                        	'<div id="headGoogleSatelliteBtn" class="googleSatelliteBtn" title="Capa Base de Google - Satélite" onClick="setMapBaseLayer(\'Satellite\')"></div>' +			                        
				                       		'<div id="headGoogleHybridBtn" class="googleHybridBtn" title="Capa Base de Google - Híbrida" onClick="setMapBaseLayer(\'Hybrid\')"></div>');		                        
			                        	 } </script> 
			                        </div>	
			                        <div id="headImgLeftFrame">
			                        	<div id="headImgLeft"></div>
			                        </div>
			                        <div id="headTitleLeftFrame">
			                        	<div id="headTitleLeft">TRÁMITE DE <bean:write name="feature_nameUpper"/></div>
			                        </div> 			                        
			                         <!--  <div id="headTitle"></div>	   -->  								             
		                        </div>		                                                                     
		                        <div id="headBorderLeftSemiBtn" class="BorderLeftSemiBtn_expanded" onclick="OpenLayers.LocalgisUtils.borderLeftSemiBtn_onClick(this);"></div>  		           
                        	</div>
                        	<div id="headImgRightFrame"> 
	                        	<div id="headImgRight"></div>
	                        </div>       	
                        </div> 
                        <div id="bottom">
                        	<div id="bottomImgLeft"></div> 	
                        </div>
                        <div id="messageBox" class="invisible"><div id="message"></div></div>
                	</div>
                    <!--  Fin del contenido de la pagina -->
      
        
    </body>
    <script type="text/javascript">	
		if(typeof exitFunction == 'function'){	
			document.getElementById("headImgCenter").innerHTML='<div id="loadBtn" class="LoadBtn" onclick="exitFunction();" title="Carga de información en el trámite">CARGAR DATOS</div>';
		}	
	</script>	
</html:html>