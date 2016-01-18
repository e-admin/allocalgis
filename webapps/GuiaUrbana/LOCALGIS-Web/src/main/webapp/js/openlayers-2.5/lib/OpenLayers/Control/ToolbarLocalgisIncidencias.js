/* Copyright (c) 2006 MetaCarta, Inc., published under the BSD license.
 * See http://svn.openlayers.org/trunk/openlayers/release-license.txt 
 * for the full text of the license. */

/**
 * @class
 * 
 * @requires OpenLayers/Control/Panel.js
 * @requires OpenLayers/Control/Navigation.js
 * @requires OpenLayers/Control/ZoomBox.js
 */
OpenLayers.Control.ToolbarLocalgisIncidencias  = 
  OpenLayers.Class(OpenLayers.Control.Panel, {

    incidencia: null,
    zoomIn: null,
    zoomOut: null,
    zoomBox: null,
    zoomToEntidad: null,
    zoomToComunidad: null,
    zoomToMaxExtent: null,
    featureInfo: null,
    navigation: null,
    marker: null,
    print: null,
    //search: null,
    save: null,
    gpx: null,
    rutas:null,
    areas:null,
    salesman:null,
    wmst:null,
    measure:null,
    measurearea: null,  
    zoomNucleo: null,
 
    
    /**
     * Add our two mousedefaults controls.
     */
    initialize: function(options,urlPrinting,urlSaveMapContext,urlExportGpx,idMap,targetCRSCode,idEntidad,
    						heightPrinting,widthPrinting,urlPrintingRuta,listaGravedad,listaTipo) {
        OpenLayers.Control.Panel.prototype.initialize.apply(this, arguments);
        
        this.incidencia = new OpenLayers.Control.IncidenciaLocalgis(null, idMap,idEntidad,listaGravedad,listaTipo);
        this.zoomIn = new OpenLayers.Control.ZoomInLocalgis();
        this.zoomOut = new OpenLayers.Control.ZoomOutLocalgis();
        this.zoomBox = new OpenLayers.Control.ZoomBoxLocalgis();
        this.zoomToEntidad = new OpenLayers.Control.ZoomToEntidadLocalgis();
        this.zoomToComunidad = new OpenLayers.Control.ZoomToComunidadLocalgis();
        this.zoomToMaxExtent = new OpenLayers.Control.ZoomToMaxExtentLocalgis();
        this.featureInfo = new OpenLayers.Control.GetFeatureInfoLocalgis(null);
        this.navigation = new OpenLayers.Control.NavigationLocalgis();
        this.marker = new OpenLayers.Control.MarkerLocalgis(idMap);
        this.print = new OpenLayers.Control.PrintLocalgis(urlPrinting,heightPrinting,widthPrinting);
//        this.search = new OpenLayers.Control.SearchLocalgis(targetCRSCode, idEntidad);
        this.save = new OpenLayers.Control.SaveLocalgis(urlSaveMapContext);
        this.gpx  = new OpenLayers.Control.GpxLocalgis( urlExportGpx);
        this.rutas = new OpenLayers.Control.RutasLocalgis(urlPrintingRuta,heightPrinting,widthPrinting);
        this.areas = new OpenLayers.Control.WithinCostLocalgis();
        this.salesman = new OpenLayers.Control.SalesManLocalgis();
        this.wmst = new OpenLayers.Control.WMSTime();
        this.measure = new OpenLayers.Control.MeasureLocalgis();
        this.measurearea = new OpenLayers.Control.MeasureAreaLocalgis();
        
        this.zoomNucleo = new OpenLayers.Control.ZoomNucleo();
       
        
    	this.addControls([
            this.incidencia,
            this.zoomIn,
            this.zoomOut,
            this.zoomBox,
            this.zoomToMaxExtent,
            this.zoomToEntidad,
            this.zoomToComunidad,
            this.featureInfo,
            this.navigation,
            this.marker,
            this.print,
            //this.search,
            this.save,
            this.gpx,
            this.rutas,
            this.areas,
            this.salesman,
            this.wmst,
            this.measure,
            this.measurearea,
            this.zoomNucleo         
        ]);
    },

    /**
     * calls the default draw, and then activates mouse defaults.
     */
    draw: function() {
        var div = OpenLayers.Control.Panel.prototype.draw.apply(this, arguments);
        this.activateControl(this.navigation);
        this.navigation.wheelHandler.activate();
        this.incidencia.panel_div.title = "Avisar de incidencia";
        this.incidencia.panel_div.id="divIncidencia";
        this.zoomIn.panel_div.title = "Acercar";
        this.zoomOut.panel_div.title = "Alejar";
        this.zoomBox.panel_div.title = "Zoom a recuadro";
        this.zoomToEntidad.panel_div.title = "Zoom a entidad";
        this.zoomToComunidad.panel_div.title = "Zoom a comunidad";
        this.zoomToMaxExtent.panel_div.title = "Zoom a España";
        this.featureInfo.panel_div.title = "Obtener información";
        this.navigation.panel_div.title = "Mover";
        this.marker.panel_div.title = "Crear marca de posición";
        this.print.panel_div.title = "Imprimir";
        //this.search.panel_div.title = "Buscar topónimos";
        this.save.panel_div.title = "Exportar WMC";
        this.gpx.panel_div.title = "Exportar GPX";
        this.rutas.panel_div.title="Rutas";
        this.rutas.panel_div.id="divRutasTool";
        this.areas.panel_div.title="Area de influencia";
        this.areas.panel_div.id="divAreasTool";
        this.salesman.panel_div.title="Ruta del Viajante";
        this.salesman.panel_div.id="divSalesManTool";
        this.wmst.panel_div.title="Histórico de datos";
        this.wmst.panel_div.id="wmstTool";
        this.measure.panel_div.title = "Medir distancias";
        this.measure.panel_div.id="divMeasure"; 
        this.measurearea.panel_div.title = "Calcular área";
        this.measurearea.panel_div.id="divMeasureArea"; 
        this.zoomNucleo.panel_div.title = "Zoom Núcleo";
        this.zoomNucleo.panel_div.id = "divZoomNucleo";
      
        
        return div;                
    },                            
                                  
    CLASS_NAME: "OpenLayers.Control.ToolbarLocalgisIncidencias"
});                               
                                  