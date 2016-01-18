/* Copyright (c) 2006-2007 MetaCarta, Inc., published under the BSD license.
 * See http://svn.openlayers.org/trunk/openlayers/release-license.txt 
 * for the full text of the license. */

/** 
 * @requires OpenLayers/Control.js
 * 
 * Class: OpenLayers.Control.LayerSwitcherLocalgis
 *
 * Inherits from:
 *  - <OpenLayers.Control>
 */
OpenLayers.Control.LayerSwitcherLocalgis = 
  OpenLayers.Class(OpenLayers.Control.LayerSwitcher, {

    /**  
     * Property: activeColor
     * {String}
     */
    activeColor: "white",
    
    /**  
     * Property: activeLayerDiv
     * {DOMElement}
     */
    activeLayerDiv: null,

    legendMarkersDiv: null,
    
    positionMarkers: null,
    
    
    /**
     * Constructor: OpenLayers.Control.LayerSwitcher
     * 
     * Parameters:
     * options - {Object}
     */
    initialize: function(options) {
        OpenLayers.Control.LayerSwitcher.prototype.initialize.apply(this, arguments);
        this.positionMarkers = [];
    },
    
    /** 
     * Method: clearLayersArray
     * User specifies either "base" or "data". we then clear all the
     *     corresponding listeners, the div, and reinitialize a new array.
     * 
     * Parameters:
     * layersType - {String}  
     */
    clearLayersArray: function(layersType) {
        var layers = this[layersType + "Layers"];
        if (layers) {
            for(var i=0; i < layers.length; i++) {
                var layer = layers[i];
                OpenLayers.Event.stopObservingElement(layer.inputElem);
                OpenLayers.Event.stopObservingElement(layer.labelDiv);
                OpenLayers.Event.stopObservingElement(layer.collapseDiv);
            }
        }
        this[layersType + "LayersDiv"].innerHTML = "";
        this[layersType + "Layers"] = [];
    },

    /** 
     * Method: redraw
     * Goes through and takes the current state of the Map and rebuilds the
     *     control to display that state. Groups base layers into a 
     *     radio-button group and lists each data layer with a checkbox.
     *
     * Returns: 
     * {DOMElement} A reference to the DIV DOMElement containing the control
     */  
    redraw: function() {
        //if the state hasn't changed since last redraw, no need 
        // to do anything. Just return the existing div.
        if (!this.checkRedraw()) { 
            return this.div; 
        } 

        //clear out previous layers 
        this.clearLayersArray("base");
        this.clearLayersArray("data");
        
        var containsOverlays = false;
        var containsBaseLayers = false;
        
        // Save state -- for checking layer if the map state changed.
        // We save this before redrawing, because in the process of redrawing
        // we will trigger more visibility changes, and we want to not redraw
        // and enter an infinite loop.
        var oldLayerStates = this.layerStates;
        
        this.layerStates = new Array(this.map.layers.length);
        for (var i = 0; i < this.map.layers.length; i++) {
            var layer = this.map.layers[i];
            var oldCollapsedLegend = false;
            if (oldLayerStates != undefined && oldLayerStates [i] != undefined) {
                oldCollapsedLegend = oldLayerStates [i].collapsedLegend; 
            };
            this.layerStates[i] = {
                'name': layer.name, 
                'visibility': layer.visibility,
                'inRange': layer.inRange,
                'id': layer.id,
                'disabled': layer.disabled,
                'collapsedLegend': oldCollapsedLegend
            };
        }    

        var layers = this.map.layers.slice();
        layers = OpenLayers.LocalgisUtils.sortLayers(layers);
        if (!this.ascending) { layers.reverse(); }
        for( var i = 0; i < layers.length; i++) {
            var layer = layers[i];
            var baseLayer = layer.isBaseLayer;
            if (layer.displayInLayerSwitcher) {
                
                if (baseLayer) {
                    containsBaseLayers = true;
                } else {
                    containsOverlays = true;
                }    

                // only check a baselayer if it is *the* baselayer, check data
                //  layers if they are visible
                var checked = (baseLayer) ? (layer == this.map.baseLayer)
                                          : layer.getVisibility();
    
                // Creacion de un input (radio o checkbox para poner visible la capa o no)
                var inputElem = document.createElement("input");
                inputElem.id = "input_" + layer.name;
                inputElem.name = (baseLayer) ? "baseLayers" : layer.name;
                inputElem.type = (baseLayer) ? "radio" : "checkbox";
                inputElem.value = layer.name;
                inputElem.checked = checked;
                inputElem.defaultChecked = checked;

                if ((!baseLayer && !layer.inRange) || (layer instanceof OpenLayers.Layer.WMSLocalgis && layer.disabled)) {
                    inputElem.disabled = true;
                }
                        
                // Creacion de un div para el nombre de la capa
                var labelDiv = document.createElement("div");
                if ((!baseLayer && !layer.inRange) || (layer instanceof OpenLayers.Layer.WMSLocalgis && layer.disabled)) {
                    labelDiv.style.color = "gray";
                }


                labelDiv.className = 'olLayerSwitcherLocalgisLayerLabel';
                if (layer instanceof OpenLayers.Layer.WMSLocalgis) {
                    labelDiv.innerHTML = layer.preName + layer.name;
                } else {
                    labelDiv.innerHTML = layer.name;
                }
                labelDiv.style.verticalAlign = (baseLayer) ? "bottom" 
                                                            : "baseline";
                
                
                // Creacion de un div para la flecha de expandir/colapsar y para la etiqueta
                // Si es una capa WMSLocalgis solo mostramos la flecha si no tenemos layer.preName
                var collapseDiv = document.createElement("div");
                if (layer instanceof OpenLayers.Layer.WMSLocalgis || layer instanceof OpenLayers.Layer.VectorLocalgis) {
                    if (layer.preName == '') {
                        collapseDiv.innerHTML = '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';
                    } else {
                        collapseDiv.className = 'olLayerSwitcherLocalgisLayerWithoutLegend';
                    }
                } else if (layer instanceof OpenLayers.Layer.Markers) {
                        collapseDiv.innerHTML = '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';
                }
                collapseDiv.appendChild(labelDiv);
                
                //Creamos un div para la leyenda de la capa) 
                var legendDiv = document.createElement("div");
                if (this.layerStates[i].collapsedLegend) {
                    collapseDiv.className = 'olLayerSwitcherLocalgisLayerCollapsed';
                    legendDiv.className = 'olLayerSwitcherLocalgisLegendHidden';
                } else {
                    collapseDiv.className = 'olLayerSwitcherLocalgisLayerExpanded';
                    legendDiv.className = 'olLayerSwitcherLocalgisLegendVisible';
                }
                if (layer instanceof OpenLayers.Layer.Markers) {
                    legendDiv.style.marginTop = "5px";
                    this.legendMarkersDiv = legendDiv;
                    for (var j = 0; j < this.positionMarkers.length; j++) {
                        this.addPositionMarkerLocalgisToDiv(this.positionMarkers[j]);
                    } 
                }
                else if (layer.urlLegend != undefined && layer.urlLegend != '') {
                    legendDiv.innerHTML = '<img src="'+layer.urlLegend+'" alt="Leyenda capa '+layer.name+'"/>';
                }
                
                // Esto no se para que se hace
                var groupArray = (baseLayer) ? this.baseLayers
                                             : this.dataLayers;
                groupArray.push({
                    'layer': layer,
                    'inputElem': inputElem,
                    'labelDiv': labelDiv,
                    'collapseDiv': collapseDiv
                });
                
                // Creamos un div donde ira el input, el icono para expandir/contraer y el nombre de la capa
                var layerDiv = document.createElement("div");
                //Comprobamos si es la capa activa para poner el estilo en el caso de que estemos en una actualizacion del componente y para guardarla como atributo
                if (this.map.activeLayer == layer) {
                    layerDiv.className = "olLayerSwitcherLocalgisLayerActive";
                    this.activeLayerDiv = layerDiv;
                } else {
                    layerDiv.className = "olLayerSwitcherLocalgisLayerInactive";
                }
                layerDiv.appendChild(inputElem);
                if (collapseDiv != null) {
                    layerDiv.appendChild(collapseDiv);
                }

                // Contexto para los eventos
                var context = {
                    'inputElem': inputElem,
                    'layer': layer,
                    'legendDiv': legendDiv,
                    'collapseDiv': collapseDiv,
                    'layerDiv': layerDiv,
                    'labelDiv': labelDiv,
                    'layerSwitcher': this,
                    'layerStates': this.layerStates[i]};

                // Caputa del evento mouseup en el campo input (no se para que vale)
                OpenLayers.Event.observe(inputElem, "mouseup", 
                              this.onInputClick.bindAsEventListener(context));
                
                // Caputa del evento click en el campo input (para ver/ocultar una capa)
                OpenLayers.Event.observe(inputElem, "click", 
                              this.onInputClick.bindAsEventListener(context));

                // Caputa del evento mouseup en la flecha de expander/contraer la leyenda de una capa
                OpenLayers.Event.observe(collapseDiv, "click", 
                              this.onInputClickCollapseLayer.bindAsEventListener(context));

                // Caputa del evento mouseup en el campo input (no se para que vale)
                OpenLayers.Event.observe(labelDiv, "click", 
                              this.onInputClickActiveLayer.bindAsEventListener(context));

                var groupDiv = (baseLayer) ? this.baseLayersDiv
                                           : this.dataLayersDiv;

                groupDiv.appendChild(layerDiv);
                groupDiv.appendChild(legendDiv);

            }
        }

        // if no overlays, dont display the overlay label
        this.dataLbl.style.display = (containsOverlays) ? "" : "none";        
        
        // if no baselayers, dont display the baselayer label
        this.baseLbl.style.display = (containsBaseLayers) ? "" : "none";        

        return this.div;
    },

    /** 
     * Method:
     * A label has been clicked, check or uncheck its corresponding input
     * 
     * Parameters:
     * e - {Event} 
     *
     * Context:  
     *  - {DOMElement} inputElem
     *  - {<OpenLayers.Control.LayerSwitcher>} layerSwitcher
     *  - {<OpenLayers.Layer>} layer
     */
    onInputClick: function(e) {

        if (!this.inputElem.disabled) {
            if (this.inputElem.type == "radio") {
                this.inputElem.checked = true;
                this.layer.map.setBaseLayer(this.layer);
            } else {
                this.inputElem.checked = !this.inputElem.checked;
                // Si no esta chequeado y la capa esta activa la desactivamos
                if (!this.inputElem.checked && (this.layer.map.activeLayer == this.layer)) {
                    this.layerSwitcher.activeLayerDiv.className = 'olLayerSwitcherLocalgisLayerInactive';
                    this.layerSwitcher.activeLayerDiv = null;
                    this.layer.map.activeLayer = null;
                }
                
                this.layerSwitcher.updateMap();
            }
        }
        OpenLayers.Event.stop(e);
    },

    onInputClickCollapseLayer: function(e) {
        var classname = this.collapseDiv.className;
        if (classname == 'olLayerSwitcherLocalgisLayerExpanded') {
            this.collapseDiv.className = 'olLayerSwitcherLocalgisLayerCollapsed';
            this.legendDiv.className = 'olLayerSwitcherLocalgisLegendHidden';
            this.layerStates.collapsedLegend = true;
        } else {
            this.collapseDiv.className = 'olLayerSwitcherLocalgisLayerExpanded';
            this.legendDiv.className = 'olLayerSwitcherLocalgisLegendVisible';
            this.layerStates.collapsedLegend = false;
        }
        OpenLayers.Event.stop(e);
    },

    onInputClickActiveLayer: function(e) {
    	
        var classname = this.layerDiv.className;
        if (classname == 'olLayerSwitcherLocalgisLayerActive') {
            this.layerDiv.className = 'olLayerSwitcherLocalgisLayerInactive';
            this.layerSwitcher.activeLayerDiv = null;
            this.layer.map.activeLayer = null;
        } else {
            // Si previamente habia una capa activa se desactiva
            if (this.layerSwitcher.activeLayerDiv != null) {            
                this.layerSwitcher.activeLayerDiv.className = 'olLayerSwitcherLocalgisLayerInactive';
                this.layerSwitcher.activeLayerDiv = null;
            }

            this.layerDiv.className = 'olLayerSwitcherLocalgisLayerActive';
            this.layerSwitcher.activeLayerDiv = this.layerDiv;
            this.layer.map.activeLayer = this.layer;
            // Si la capa está no visible la ponemos visible
            if (!this.inputElem.disabled && (this.inputElem.type == "checkbox") && !this.inputElem.checked) {
                this.inputElem.checked = true;
                this.layerSwitcher.updateMap();
            }
        }
        OpenLayers.Event.stop(e);
    },

    /** 
     * Method: loadContents
     * Set up the labels and divs for the control
     */
    loadContents: function() {

        //configure main div
        this.div.style.position = "relative";
        this.div.style.fontFamily = "Verdana";
        this.div.style.fontWeight = "bold";
        this.div.style.fontSize = "12px";   
        this.div.style.color = "#212121";   
        this.div.style.backgroundColor = "transparent";
    
        OpenLayers.Event.observe(this.div, "mouseup", 
            OpenLayers.Function.bindAsEventListener(this.mouseUp, this));
        OpenLayers.Event.observe(this.div, "click",
                      this.ignoreEvent);
        OpenLayers.Event.observe(this.div, "mousedown",
            OpenLayers.Function.bindAsEventListener(this.mouseDown, this));
        OpenLayers.Event.observe(this.div, "dblclick", this.ignoreEvent);


        // layers list div        
        this.layersDiv = document.createElement("div");
        this.layersDiv.id = "layersDiv";
        this.layersDiv.style.paddingTop = "0px";
        this.layersDiv.style.paddingLeft = "0px";
        this.layersDiv.style.paddingBottom = "0px";
        this.layersDiv.style.paddingRight = "0px";
        //this.layersDiv.style.backgroundColor = this.activeColor;        

        // had to set width/height to get transparency in IE to work.
        // thanks -- http://jszen.blogspot.com/2005/04/ie6-opacity-filter-caveat.html
        //
        //this.layersDiv.style.width = "100%";
        //this.layersDiv.style.height = "100%";


        this.baseLbl = document.createElement("div");
        this.baseLbl.innerHTML = "<u>Base Layer</u>";
        this.baseLbl.style.marginTop = "3px";
        this.baseLbl.style.marginLeft = "3px";
        this.baseLbl.style.marginBottom = "3px";
        
        this.baseLayersDiv = document.createElement("div");
        this.baseLayersDiv.style.paddingLeft = "0px";
        /*OpenLayers.Event.observe(this.baseLayersDiv, "click", 
            OpenLayers.Function.bindAsEventListener(this.onLayerClick, this));
        */
                     

        this.dataLbl = document.createElement("div");
        this.dataLbl.innerHTML = "<u></u>";
        this.dataLbl.style.marginTop = "3px";
        this.dataLbl.style.marginLeft = "3px";
        this.dataLbl.style.marginBottom = "3px";
        
        this.dataLayersDiv = document.createElement("div");
        this.dataLayersDiv.style.paddingLeft = "0px";

        if (this.ascending) {
            this.layersDiv.appendChild(this.dataLbl);
            this.layersDiv.appendChild(this.dataLayersDiv);
        } else {
            this.layersDiv.appendChild(this.dataLbl);
            this.layersDiv.appendChild(this.dataLayersDiv);
        }    
 
        this.div.appendChild(this.layersDiv);

        // No redondeamos nada
        //OpenLayers.Rico.Corner.round(this.div, {corners: "tl bl",
        //                                bgColor: "transparent",
        //                                color: this.activeColor,
        //                                blend: false});

        OpenLayers.Rico.Corner.changeOpacity(this.layersDiv, 0.75);

        var imgLocation = OpenLayers.Util.getImagesLocation();
        var sz = new OpenLayers.Size(18,18);        

        // maximize button div
        var img = imgLocation + 'layer-switcher-maximize.png';
        this.maximizeDiv = OpenLayers.Util.createAlphaImageDiv(
                                    "OpenLayers_Control_MaximizeDiv", 
                                    null, 
                                    sz, 
                                    img, 
                                    "absolute");
        this.maximizeDiv.style.top = "5px";
        this.maximizeDiv.style.right = "0px";
        this.maximizeDiv.style.left = "";
        this.maximizeDiv.style.display = "none";
        OpenLayers.Event.observe(this.maximizeDiv, "click", 
            OpenLayers.Function.bindAsEventListener(this.maximizeControl, this)
        );
        
        // No ponemos el div de maximizar
        //this.div.appendChild(this.maximizeDiv);

        // No el evento de minimizar
        var img = imgLocation + 'layer-switcher-minimize.png';
        var sz = new OpenLayers.Size(18,18);        
        this.minimizeDiv = OpenLayers.Util.createAlphaImageDiv(
                                    "OpenLayers_Control_MinimizeDiv", 
                                    null, 
                                    sz, 
                                    img, 
                                    "absolute");
        this.minimizeDiv.style.top = "5px";
        this.minimizeDiv.style.right = "0px";
        this.minimizeDiv.style.left = "";
        this.minimizeDiv.style.display = "none";

        // No ponemos el div de minimizar
        //this.div.appendChild(this.minimizeDiv);
        
    },

    
    addPositionIncidenciaLocalgis: function (positionIncidenciaLocalgis) {       
       this.map.incidenciasLayer.addIncidencia(positionIncidenciaLocalgis);
    },

    addPositionMarkerLocalgis: function (positionMarkerLocalgis) {
    	this.map.markersLayer.addMarker(positionMarkerLocalgis); 

        this.addPositionMarkerLocalgisToDiv(positionMarkerLocalgis);
        
        this.map.markersLayer.addMarker(positionMarkerLocalgis); 
    },

    addPositionMarkerLocalgisToDiv: function(positionMarkerLocalgis) {
        var div = document.createElement("div");

        div.className = "olMarkerListLocalgisElement";
        
        var divLabel = document.createElement("div");
        divLabel.className = "olMarkerListLocalgisElementLabel";
        divLabel.innerHTML = unescape(positionMarkerLocalgis.name);
        
        div.appendChild(divLabel);
        
        // Contexto para los eventos
        var context = {'layerSwitcher': this, 'positionMarker': positionMarkerLocalgis, 'divPositionMarker': div};
        
        // Caputa del evento click en la imagen de crear una marca
        OpenLayers.Event.observe(div, "click", this.onGoToMark.bindAsEventListener(context));
        
        this.legendMarkersDiv.appendChild(div);
    },
    
    /*
     * Evento de ir a una marca. Contexto: 'layerSwitcher', 'positionMarker', 'divPositionMarker'
     */
    onGoToMark: function(evt) {
        this.layerSwitcher.map.removeAllPopups();
        var resolution = OpenLayers.Util.getResolutionFromScale(this.positionMarker.scale,this.layerSwitcher.map.baseLayer.units);
        var lonlat = new OpenLayers.LonLat(this.positionMarker.lonlat.lon,this.positionMarker.lonlat.lat);
        this.layerSwitcher.map.setCenter(lonlat, this.layerSwitcher.map.getZoomForResolution(resolution),false,false);
        var contentHTML;
        contentHTML = '<form name="updateMarker">';
        contentHTML += '<table style="margin-left:auto;margin-right:auto;text-align:center">';
        contentHTML += '<tr>';
        contentHTML += '<td align="left">Nombre:</td>';
        contentHTML += '<td><input type="text" class="inputTextField" name="nombre" value="'+unescape(this.positionMarker.name)+'" style="width: 240px;"></td>';
        contentHTML += '</tr>';
        contentHTML += '<tr>';
        contentHTML += '<td align="left">Descripción:</td>';
        contentHTML += '<td colspan="2" align="left"><textarea name="descripcion" style="width: 240px; height: 50px;">'+unescape(this.positionMarker.text)+'</textarea></td>';
        contentHTML += '</tr>';
        contentHTML += '<tr>';
        contentHTML += '<td align="center" colspan="2">';
        contentHTML += '<div id="divButtons">';
        contentHTML += '<table>';
        contentHTML += '<tr>';
        contentHTML += '<td><div id="divButtonModifiyMarker"><img id="buttonUpdateMarker" class="imageButton" src="img/btn_guardar.gif" alt="Modificar"/></div></td>';
        contentHTML += '<td><div id="divButtonRemoveMarker"><img id="buttonRemoveMarker" class="imageButton" src="img/btn_eliminar.gif" alt="Eliminar"/></div></td>';
        contentHTML += '</tr>';
        contentHTML += '</table>';
        contentHTML += '</div>';
        contentHTML += '</td>';
        contentHTML += '</tr>';
        contentHTML += '</table>';
        contentHTML += '</form>';

        OpenLayers.LocalgisUtils.showPopupPico(contentHTML);

        var updateImage = document.getElementById("buttonUpdateMarker");
        var removeImage = document.getElementById("buttonRemoveMarker");
        
        // Contexto para los eventos
        var context = {'divPositionMarker': this.divPositionMarker, 'positionMarker': this.positionMarker, 'layerSwitcher': this.layerSwitcher};
        
        // Caputa del evento click en la imagen de modificar
        OpenLayers.Event.observe(updateImage, "click", this.layerSwitcher.onUpdateMarker.bindAsEventListener(context));

        // Caputa del evento click en la imagen de eliminar
        OpenLayers.Event.observe(removeImage, "click", this.layerSwitcher.onRemoveMarker.bindAsEventListener(context));
    },
    
    /*
     * Metodo para eliminar una marca. Contexto: 'divPositionMarker', 'positionMarker', 'layerSwitcher'
     */
    onRemoveMarker: function(e) {
        var removeMarkerReplyServer = {
            callback: function(data) {
                var contentHTML;
                contentHTML = '<br>Marca de posición eliminada correctamente.<br><br>';
                removeMarkerReplyServer.layerSwitcher.legendMarkersDiv.removeChild(removeMarkerReplyServer.divPositionMarker);
                removeMarkerReplyServer.layerSwitcher.map.markersLayer.removeMarker(removeMarkerReplyServer.positionMarker);
                for (var i = 0; i < removeMarkerReplyServer.layerSwitcher.positionMarkers.length; i++) {
                    if (removeMarkerReplyServer.layerSwitcher.positionMarkers[i].id == removeMarkerReplyServer.positionMarker.id) {
                        removeMarkerReplyServer.layerSwitcher.positionMarkers.splice(i,1);
                        break;
                    }
                }
                OpenLayers.LocalgisUtils.showPopup(contentHTML);
            },
            timeout:10000,
            divPositionMarker: this.divPositionMarker,
            layerSwitcher: this.layerSwitcher,
            positionMarker: this.positionMarker,
            errorHandler:function(message) { 
                OpenLayers.LocalgisUtils.showError('La marca de posición no se ha podido eliminar.');
            }
        };

        var divButtons = document.getElementById('divButtons');
        divButtons.innerHTML = '<img src="'+OpenLayers.Util.getImagesLocation()+'ajax-loader.gif" alt="Modificando"/>';
        MarkerService.deleteMarker(this.positionMarker.idMap, this.positionMarker.id, removeMarkerReplyServer);

    },

    /*
     * Metodo para modificar una marca. Contexto: 'divPositionMarker', 'positionMarker', 'layerSwitcher'
     */
    onUpdateMarker: function(e) {
        var name = document.updateMarker.nombre.value;
        var text = document.updateMarker.descripcion.value;
        if (name == undefined || name.trim() == '') {
            alert("Debe introducir un nombre para la marca de posición.");
            return;
        }
        var updateMarkerReplyServer = {
            callback: function(data) {
                var contentHTML;
                contentHTML = '<br>Marca de posición modificada correctamente.<br><br>';
                updateMarkerReplyServer.positionMarker.setName(escape(updateMarkerReplyServer.name));
                updateMarkerReplyServer.positionMarker.setText(escape(updateMarkerReplyServer.text));
                updateMarkerReplyServer.divPositionMarker.firstChild.innerHTML = updateMarkerReplyServer.name;
                OpenLayers.LocalgisUtils.showPopup(contentHTML);
            },
            timeout:10000,
            name: name,
            text: text,
            positionMarker: this.positionMarker,
            divPositionMarker: this.divPositionMarker,
            errorHandler:function(message) { 
                OpenLayers.LocalgisUtils.showError('La marca de posición no se ha podido modificar.');
            }
        };
    
        var divButtons = document.getElementById('divButtons');
        divButtons.innerHTML = '<img src="'+OpenLayers.Util.getImagesLocation()+'ajax-loader.gif" alt="Modificando"/>';
        MarkerService.updateMarker(this.positionMarker.id, this.positionMarker.idMap, escape(name), escape(text), updateMarkerReplyServer);

    },
    
        /** 
     * Method: maximizeControl
     * Set up the labels and divs for the control
     * 
     * Parameters:
     * e - {Event} 
     */
    maximizeControl: function(e) {

        //HACK HACK HACK - find a way to auto-size this layerswitcher
        this.div.style.width = "";
        this.div.style.height = "";

        this.showControls(false);

        if (e != null) {
            OpenLayers.Event.stop(e);                                            
        }
    },
    
    /**
     * Method: checkRedraw
     * Checks if the layer state has changed since the last redraw() call.
     * 
     * Returns:
     * {Boolean} The layer state changed since the last redraw() call. 
     */
    checkRedraw: function() {
        var redraw = false;
        if ( !this.layerStates.length ||
             (this.map.layers.length != this.layerStates.length) ) {
            redraw = true;
        } else {
            for (var i=0; i < this.layerStates.length; i++) {
                var layerState = this.layerStates[i];
                var layer = this.map.layers[i];
                if ( (layerState.name != layer.name) || 
                     (layerState.inRange != layer.inRange) || 
                     (layerState.id != layer.id) || 
                     (layerState.visibility != layer.visibility) ||
                     (layerState.disabled != layer.disabled)) {
                    redraw = true;
                    break;
                }    
            }
        }    
        return redraw;
    },
    

    CLASS_NAME: "OpenLayers.Control.LayerSwitcherLocalgis"
});
