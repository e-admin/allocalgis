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





  <script type="text/javascript">
  
	function waitToUpdate()
	 { 
		try{
			if (menu._isContextMenuVisible() && menu.contextAutoHide) { menu._hideContextMenu(); }
		}catch(e){} 
	  	}
  	var status;
  	var statustemp;
  	/*************************************************/
	/*Incluimos el layout de visualizacion		 	 */
  	/*************************************************/
	function utils_addLayout(){
		//Creamos las ventanas movibles para albergar el mapa y las capas
		dhxLayout = new dhtmlXLayoutObject("map", "2U");
		dhxLayout.cells("a").setHeight(2000);
		dhxLayout.cells("a").setWidth(272);
		dhxLayout.cells("a").fixSize(true, true);
		dhxLayout.cells("a").setText("Capas");
		dhxLayout.cells("b").setText(document.getElementById("nombreMunicipio").innerHTML);
		dhxLayout.cells("a").attachObject("layerswitcher");
		dhxLayout.cells("b").attachObject("mapDetail");

		var item = dhxLayout.cells("b");
		//var id = dhxLayout.items[1].getId();

		//La barra de herramientas va sobre este componente de menu
		 var dhxMenu = dhxLayout.cells("b").attachMenu();



		//resultado de las medidas en el status bar
		//En Chrome si no lo cargas como variable temporal no funciona.
		statustemp = item.attachStatusBar();
		
  		//Creamos las ventanas movibles para albergar el mapa y las capas
		
		statustemp.setText("Medidas");
		
		status=statustemp;
	
		//dhxLayout.item.expand();
		//item.setEffect("collapse", false);
	
		//item.hideHeader();
		//var dhxLayout,dhxWins,z,status;
		var dhxLayout,dhxWins;
		//alert("INICIO");

		dhxLayout.attachEvent("onPanelResizeFinish", function() {
	
			map.updateSize();
		});
		dhxLayout.attachEvent("onResizeFinish", function() {
			map.updateSize();
		});
		dhxLayout.attachEvent("onExpand", function() {
	
			// map.zoomToExtent(map.getExtent()); 
			map.updateSize();
		});
	
		dhxLayout.attachEvent("onCollapse", function() {
	
			map.updateSize();
	
		});
					
	}
  	
  	/*************************************************/
	/*Toolbar de Localgis		 	 */
  	/*************************************************/
	function utils_addToolBar(map){
		var toolbar = new OpenLayers.Control.ToolbarLocalgisIncidencias(
	            {}, 
	            '${pageContext.request.contextPath}/${requestScope.configurationLocalgisWeb}/printMap.do?idMap=<bean:write name="localgisMap" property="mapid"/>&language=<bean:write name="language"/>&x=0&y=0&zoom=10&layers=<bean:write name="provinciasLayer" property="name"/>&showMarkers=false&esRuta=false&esArea=false',
	            '${pageContext.request.contextPath}/${requestScope.configurationLocalgisWeb}/saveMapContext.do?idMap=<bean:write name="localgisMap" property="mapid"/>&language=<bean:write name="language"/>',
	            '${pageContext.request.contextPath}/${requestScope.configurationLocalgisWeb}/exportGpx.do?idMap=<bean:write name="localgisMap" property="mapid"/>&language=<bean:write name="language"/>',
	            <bean:write name="localgisMap" property="mapid"/>,
	            srid,
	            '<bean:write name="localgisMap" property="mapidentidad"/>', 
	            768, 
	            1024,
	            '${pageContext.request.contextPath}/${requestScope.configurationLocalgisWeb}/printRutas.do?idMap=<bean:write name="localgisMap" property="mapid"/>&language=<bean:write name="language"/>',
	            '<bean:write name="gravedadIncidencia"/>',
	            '<bean:write name="tipoIncidencia"/>');            
	    map.addControlWithIdDiv(toolbar, null, 'toolbar');
	}
  	
  	
	/*************************************************/
	/*Barra de Medidas                  		 	 */
  	/*************************************************/
	function utils_addMedidas(map){
		
	     
      var measureControl=null;
		var measureAreaControl=null;
      
      sketchSymbolizers = {
      	"Point": {
      		pointRadius: 4,
      		graphicName: "circle",
      		fillColor: "white",
      		fillOpacity: 1,
      		strokeWidth: 1,
      		strokeOpacity: 1,
      		strokeColor: "#8C2080"
      	},
      	"Line": {
      		strokeWidth: 3,
      		strokeOpacity: 1,
      		strokeColor: "#8C2080",
      		strokeDashstyle: "dot"
      	},
      	"Polygon": {
      		strokeWidth: 3,
      		strokeOpacity: 1,
      		strokeColor: "#8C2080",
      		fillColor: "#8C2080",
      		fillOpacity: 0.3,
      		strokeDashstyle: "dot"
      	}
      };
      
      var style = new OpenLayers.Style();
      
      style.addRules([new OpenLayers.Rule({symbolizer: this.sketchSymbolizers})]);
      
      var styleMap = new OpenLayers.StyleMap({"default": style});
      
      var options = {
      	handlerOptions: {
      	style: "default",
      	layerOptions: {styleMap: styleMap},
      	persist: true
      	}
      };
      
      measureControls = {
			line: new OpenLayers.Control.Measure(
      		OpenLayers.Handler.Path, options
      	),
      	polygon: new OpenLayers.Control.Measure(
      		OpenLayers.Handler.Polygon, options
      	)
      };
      
      var control = new OpenLayers.Control();
      
      for(var key in measureControls) {
      	control = measureControls[key];
      	control.events.on({
      		"measure": handleMeasurements,
      		"measurepartial": handleMeasurements
      	});
      	map.addControl(control);
      }
	}
	
	
	/*************************************************/
	/*Barra de escala		 	 */
  	/*************************************************/
	function utils_addScaleBar(map){
	    
		 /*var scaleBar = new OpenLayers.Control.ScaleBar({
             div: document.getElementById("scalebar"),
             minWidth: 75,
             maxWidth: 175
         });*/
		 
		
	    //var scaleBar = new OpenLayers.Control.ScaleBar();
	    var scaleBar = new OpenLayers.Control.ScaleBarLocalgis();
	    scaleBar.displaySystem = 'metric';
	    scaleBar.abbreviateLabel = true;
	    map.addControl(scaleBar);
	
	}
  </script>