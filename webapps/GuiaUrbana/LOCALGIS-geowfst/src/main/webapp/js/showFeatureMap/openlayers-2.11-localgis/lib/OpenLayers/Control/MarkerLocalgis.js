/* Copyright (c) 2006 ASO crea este botón para hacer el zoomin */

/**
 * @class
 *
 * Imlements a very simple button control.
 * @requires OpenLayers/Control.js
 */
OpenLayers.Control.MarkerLocalgis  = 
  OpenLayers.Class(OpenLayers.Control, {
    /** @type OpenLayers.Control.TYPE_* */
    type: OpenLayers.Control.TYPE_TOOL,
    
    /*
     * Posicion donde se produce el ultimo evento
     */
    lastEventXY: null,

    /*
     * Identificador del mapa asociado
     */
    idMap: null,

    initialize: function(idMap) {
        OpenLayers.Control.prototype.initialize.apply(this, arguments);
        this.idMap = idMap
    },
    

    draw: function() {
        this.handler = new OpenLayers.Handler.ClickLocalgis( this,
                            {done: this.click}, {keyMask: this.keyMask} );
    },

    getLayerSwitcherLocalgis : function() {
        for(i=0 ; i < this.map.controls.length;i++) {
            if (this.map.controls[i] instanceof OpenLayers.Control.LayerSwitcherLocalgis) {
                return this.map.controls[i];  
            }
        }
    },

    click: function(xy) {
        if (!this.map.markersLayer.visibility || !this.map.markersLayer.inRange || this.map.markersLayer.disabled) {
            alert("La capa \""+this.map.markersLayer.name+"\" no está activa en este nivel de zoom.");
            return false;
        }
        this.lastEventXY = xy;
        
        var contentHTML;
        contentHTML = '<form name="addMarker">';
        contentHTML += '<table style="margin-left:auto;margin-right:auto;text-align:center">';
        contentHTML += '<tr>';
        contentHTML += '<td align="left">Nombre:</td>';
        contentHTML += '<td><input type="text" class="inputTextField" name="nombre" value="" style="width: 240px;"></td>';
        contentHTML += '</tr>';
        contentHTML += '<tr>';
        contentHTML += '<td align="left">Descripción:</td>';
        contentHTML += '<td colspan="2" align="left"><textarea name="descripcion" style="width: 240px; height: 50px;"></textarea></td>';
        contentHTML += '</tr>';
        contentHTML += '<tr>';
        contentHTML += '<td align="center" colspan="2"><div id="divButtonCreateMarker"><img id="buttonCreateMarker" class="imageButton" src="img/btn_aceptar.gif" alt="Aceptar"/></div>';
        contentHTML += '</tr>';
        contentHTML += '</table>';
        contentHTML += '</form>';
        
        OpenLayers.LocalgisUtils.showPopup(contentHTML);
        
        var imageElement = document.getElementById("buttonCreateMarker");
        
        // Contexto para los eventos
        var context = {'markerLocalgis': this};
        
        // Caputa del evento click en la imagen de crear una marca
        OpenLayers.Event.observe(imageElement, "click", this.onCreateMarker.bindAsEventListener(context));
    },
    
    /*
     * Metodo para crear una marca. Contexto: markerLocalgis --> MarkerLocalgis
     */
    onCreateMarker: function(e) {
        var name = document.addMarker.nombre.value;
        var text = document.addMarker.descripcion.value;
        if (name == undefined || name.trim() == '') {
            alert("Debe introducir un nombre para la marca de posición.");
            return;
        }

        var lonlat = this.markerLocalgis.map.getLonLatFromPixel(this.markerLocalgis.lastEventXY);
        var scale = Math.floor(this.markerLocalgis.map.getScale());

        var size = new OpenLayers.Size(13,21);
        var offset = new OpenLayers.Pixel(0, -size.h);
        var icon = new OpenLayers.Icon('img/pin_inverted.gif',size,offset);
        var positionMarkerLocalgis = new OpenLayers.PositionMarkerLocalgis(lonlat,icon);
        positionMarkerLocalgis.setIdMap(this.markerLocalgis.idMap);
        positionMarkerLocalgis.setName(escape(name));
        positionMarkerLocalgis.setText(escape(text));
        positionMarkerLocalgis.setScale(scale);
       /* 
        var addMarkerReplyServer = {
            callback: function(data) {
                var contentHTML;
                contentHTML = '<br>Marca de posición creada correctamente.<br><br>';
                OpenLayers.LocalgisUtils.showPopup(contentHTML);
                var layerSwitcherLocalgis = addMarkerReplyServer.markerLocalgis.getLayerSwitcherLocalgis();
                addMarkerReplyServer.positionMarkerLocalgis.setId(data);
                layerSwitcherLocalgis.addPositionMarkerLocalgis(addMarkerReplyServer.positionMarkerLocalgis);
            },
            timeout:10000,
            errorHandler:function(message) { 
                OpenLayers.LocalgisUtils.showError();
            },
            markerLocalgis: this.markerLocalgis,
            positionMarkerLocalgis: positionMarkerLocalgis
        };
    */
        var divButtonCreateMarker = document.getElementById('divButtonCreateMarker');
        divButtonCreateMarker.innerHTML = '<img src="'+OpenLayers.Util.getImagesLocation()+'ajax-loader.gif" alt="Creando"/>';
        //MarkerService.addMarker(this.markerLocalgis.idMap, escape(name), escape(text), lonlat.lon, lonlat.lat, scale, addMarkerReplyServer);
        MarkerService.addMarker(this.markerLocalgis.idMap, escape(name), escape(text), lonlat.lon, lonlat.lat, scale, addMarkerReplyServer);
    },
    
    /** @final @type String */
    CLASS_NAME: "OpenLayers.Control.MarkerLocalgis"
});
