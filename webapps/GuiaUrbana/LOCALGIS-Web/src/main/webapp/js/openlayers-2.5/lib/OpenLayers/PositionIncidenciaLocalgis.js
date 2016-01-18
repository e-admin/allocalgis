/* Copyright (c) 2006-2007 MetaCarta, Inc., published under the BSD license.
 * See http://svn.openlayers.org/trunk/openlayers/release-license.txt 
 * for the full text of the license. */


/**
 * @requires OpenLayers/Events.js
 * @requires OpenLayers/Icon.js
 * 
 * Class: OpenLayers.Marker
 * Instances of OpenLayers.Marker are a combination of a 
 * <OpenLayers.LonLat> and an <OpenLayers.Icon>.  
 *
 * Markers are generally added to a special layer called
 * <OpenLayers.Layer.Markers>.
 *
 * Example:
 * (code)
 * var markers = new OpenLayers.Layer.Markers( "Markers" );
 * map.addLayer(markers);
 *
 * var size = new OpenLayers.Size(10,17);
 * var offset = new OpenLayers.Pixel(-(size.w/2), -size.h);
 * var icon = new OpenLayers.Icon('http://boston.openguides.org/markers/AQUA.png',size,offset);
 * markers.addMarker(new OpenLayers.Marker(new OpenLayers.LonLat(0,0),icon));
 * markers.addMarker(new OpenLayers.Marker(new OpenLayers.LonLat(0,0),icon.clone()));
 *
 * (end)
 *
 * Note that if you pass an icon into the Marker constructor, it will take
 * that icon and use it. This means that you should not share icons between
 * markers -- you use them once, but you should clone() for any additional
 * markers using that same icon.
 */
OpenLayers.PositionIncidenciaLocalgis = OpenLayers.Class(OpenLayers.Marker, {

    id: null,

    identificador: null,
    
    idMap: null,
    
    gravedadIncidencia: null,
    
    tipoIncidencia: null,
    
    emailContacto: null,
    
    descripcion: null,
    
    layerName: null,
    
    idFeature: null,
    
    idMunicipio: null,
    
    /** 
     * Constructor: OpenLayers.PositionIncidenciaLocalgis
     * Paraemeters:
     * icon - {<OpenLayers.Icon>}  the icon for this marker
     * lonlat - {<OpenLayers.LonLat>} the position of this marker
     */
    initialize: function(lonlat, icon) {
        OpenLayers.Marker.prototype.initialize.apply(this, arguments);
    },

    setIdentificador: function(identificador){
    	this.identificador = identificador;
    },
    
    setId: function(id) {
        this.id = id;
    },

    setIdMap: function(idMap) {
        this.idMap = idMap;
    },

    setTipoIncidencia: function(tipoIncidencia) {
        this.tipoIncidencia = tipoIncidencia;
    },
    
    setGravedadIncidencia: function(gravedadIncidencia) {
        this.gravedadIncidencia = gravedadIncidencia;
    },
    
    setEmailContacto: function(emailContacto) {
        this.emailContacto = emailContacto;
    },
    
    setDescripcion: function(descripcion) {
        this.descripcion = descripcion;
    },
    
    setIdFeature: function(idFeature) {
        this.idFeature = idFeature;
    },
    
    setLayerName: function(layerName) {
        this.layerName = layerName;
    },
    
    setIdMunicipio: function(idMunicipio) {
        this.idMunicipio = idMunicipio;
    },
    CLASS_NAME: "OpenLayers.PositionIncidenciaLocalgis"
});
