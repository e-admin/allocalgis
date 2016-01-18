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
OpenLayers.Control.LayerInfoLocalgis = 
  OpenLayers.Class(OpenLayers.Control, {

    /**  
     * Property: activeColor
     * {String}
     */
    activeColor: "white",
    
    layersToDraw: null,

    /**  
     * Property: layerStates 
     * {Array(Object)} Basically a copy of the "state" of the map's layers 
     *     the last time the control was drawn. We have this in order to avoid
     *     unnecessarily redrawing the control.
     */
    layerStates: null,
    

  // DOM Elements
  
    /**
     * Property: layersDiv
     * {DOMElement} 
     */
    layersDiv: null,
    
    /** 
     * Property: baseLayersDiv
     * {DOMElement}
     */
    baseLayersDiv: null,

    /** 
     * Property: baseLayers
     * {Array(<OpenLayers.Layer>)}
     */
    baseLayers: null,
    
    
    /** 
     * Property: dataLbl
     * {DOMElement} 
     */
    dataLbl: null,
    dataExtLbl: null,
    
    /** 
     * Property: dataLayersDiv
     * {DOMElement} 
     */
    dataLayersDiv: null,

    /** 
     * Property: dataLayers
     * {Array(<OpenLayers.Layer>)} 
     */
    dataLayers: null,


    /** 
     * Property: minimizeDiv
     * {DOMElement} 
     */
    minimizeDiv: null,

    /** 
     * Property: maximizeDiv
     * {DOMElement} 
     */
    maximizeDiv: null,
    
    /**
     * APIProperty: ascending
     * {Boolean} 
     */
    ascending: true,
 
    legendMarkersDiv: null,
    
    positionMarkers: null,
 
    /**
     * Constructor: OpenLayers.Control.LayerSwitcher
     * 
     * Parameters:
     * options - {Object}
     */
    initialize: function(options) {
        OpenLayers.Control.prototype.initialize.apply(this, arguments);
        this.layerStates = [];
        this.layersToDraw = [];
        this.positionMarkers = [];
    },

    /**
     * APIMethod: destroy 
     */    
    destroy: function() {
        
        OpenLayers.Event.stopObservingElement(this.div);

        OpenLayers.Event.stopObservingElement(this.minimizeDiv);
        OpenLayers.Event.stopObservingElement(this.maximizeDiv);

        //clear out layers info and unregister their events 
        this.clearLayersArray("base");
        this.clearLayersArray("data");
        
        this.map.events.unregister("addlayer", this, this.redraw);
        this.map.events.unregister("changelayer", this, this.redraw);
        this.map.events.unregister("removelayer", this, this.redraw);
        this.map.events.unregister("changebaselayer", this, this.redraw);
        
        OpenLayers.Control.prototype.destroy.apply(this, arguments);
    },

    addLayerToDraw: function (layer) {
        for(var i=0; i < this.layersToDraw.length; i++) {
            if (this.layersToDraw[i] == layer) {
                var msg = "You tried to add the layer: " + layer.name + 
                          " to the map, but it has already been added";
                OpenLayers.Console.warn(msg);
                return false;
            }
        }    
        layer.visibility = true;
        this.layersToDraw.push(layer);
    },

    /** 
     * Method: setMap
     *
     * Properties:
     * map - {<OpenLayers.Map>} 
     */
    setMap: function(map) {
        OpenLayers.Control.prototype.setMap.apply(this, arguments);

        this.map.events.register("addlayer", this, this.redraw);
        this.map.events.register("changelayer", this, this.redraw);
        this.map.events.register("removelayer", this, this.redraw);
        this.map.events.register("changebaselayer", this, this.redraw);
    },

    /**
     * Method: draw
     *
     * Returns:
     * {DOMElement} A reference to the DIV DOMElement containing the 
     *     switcher tabs.
     */  
    draw: function() {
        OpenLayers.Control.prototype.draw.apply(this);

        // create layout divs
        this.loadContents();

        // set mode to minimize
        if(!this.outsideViewport) {
            this.minimizeControl();
        }

        // populate div with current info
        this.redraw();    

        return this.div;
    },

    /*
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
                OpenLayers.Event.stopObservingElement(layer.labelDiv);
            }
        }
        this[layersType + "LayersDiv"].innerHTML = "";
        this[layersType + "Layers"] = [];
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
                     (layerState.visibility != layer.visibility) ) {
                    redraw = true;
                    break;
                }    
            }
        }    
        return redraw;
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
        this.layerStates = new Array(this.layersToDraw.length);
        for (var i = 0; i < this.layersToDraw.length; i++) {
            var layer = this.layersToDraw[i];
            this.layerStates[i] = {
                'name': layer.name, 
                'visibility': layer.visibility,
                'inRange': layer.inRange,
                'id': layer.id
            };
        }    

        var layers = this.layersToDraw.slice();
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
    
                /*
                // Creacion de un input (radio o checkbox para poner visible la capa o no)
                var inputElem = document.createElement("input");
                inputElem.id = "input_" + layer.name;
                inputElem.name = (baseLayer) ? "baseLayers" : layer.name;
                inputElem.type = (baseLayer) ? "radio" : "checkbox";
                inputElem.value = layer.name;
                inputElem.checked = checked;
                inputElem.defaultChecked = checked;

                if (!baseLayer && !layer.inRange) {
                    inputElem.disabled = true;
                }
                */
                
                // Creacion de un div para el nombre de la capa
                var labelDiv = document.createElement("div");

                labelDiv.className = 'olLayerSwitcherLocalgisLayerLabel';
                if (layer instanceof OpenLayers.Layer.WMSLocalgis) {
                    labelDiv.innerHTML = layer.preName + layer.name;
                } else {
                    labelDiv.innerHTML = layer.name;
                }
                labelDiv.style.verticalAlign = (baseLayer) ? "bottom" 
                                                            : "baseline";                
                
                // Creacion de un div para la flecha de expandir/colapsar y para la etiqueta
               
                /*var collapseDiv = document.createElement("div");
                collapseDiv.className = 'olLayerSwitcherLocalgisLayerExpanded';
                collapseDiv.innerHTML = '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';
                collapseDiv.appendChild(labelDiv);
                */
                
                //Creamos un div para la leyenda de la capa) 
                var legendDiv = document.createElement("div");
                legendDiv.className = "olLayerSwitcherLocalgisLegendVisible";
                if (layer instanceof OpenLayers.Layer.Markers) {
                    legendDiv.style.marginTop = "5px";
                    this.legendMarkersDiv = legendDiv;
                    for (var j = 0; j < this.positionMarkers.length; j++) {
                        this.addPositionMarkerLocalgisToDiv(this.positionMarkers[j]);
                    } 
                }
                else if (layer.urlLegend != undefined && layer.urlLegend != '') {
                    legendDiv.innerHTML = '<img width=80 height=20 src="'+layer.urlLegend+'" alt="Leyenda capa '+layer.name+'"/>';
                }
                
                // Esto no se para que se hace
                var groupArray = (baseLayer) ? this.baseLayers
                                             : this.dataLayers;
                groupArray.push({
                    'layer': layer,
                    'labelDiv': labelDiv
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
                //layerDiv.appendChild(inputElem);
                layerDiv.appendChild(labelDiv);

                // Contexto para los eventos
                var context = {
                    'layer': layer,
                    'legendDiv': legendDiv,
                    'layerDiv': layerDiv,
                    'labelDiv': labelDiv,
                    'layerSwitcher': this
                }
                /*
                // Caputa del evento mouseup en el campo input (no se para que vale)
                OpenLayers.Event.observe(inputElem, "mouseup", 
                              this.onInputClick.bindAsEventListener(context));
                
                // Caputa del evento click en el campo input (para ver/ocultar una capa)
                OpenLayers.Event.observe(inputElem, "click", 
                              this.onInputClick.bindAsEventListener(context));

                // Caputa del evento mouseup en el campo input (no se para que vale)
                OpenLayers.Event.observe(collapseDiv, "click", 
                              this.onInputClickCollapseLayer.bindAsEventListener(context));

                // Caputa del evento mouseup en el campo input (no se para que vale)
                OpenLayers.Event.observe(labelDiv, "click", 
                              this.onInputClickActiveLayer.bindAsEventListener(context));

                */
                var groupDiv = (baseLayer) ? this.baseLayersDiv
                                           : this.dataLayersDiv;

                groupDiv.appendChild(layerDiv);
                groupDiv.appendChild(legendDiv);

            }
        }

        // if no overlays, dont display the overlay label
        this.dataLbl.style.display = (containsOverlays) ? "" : "none";
        
        // if no overlays, dont display the overlay label
        this.dataExtLbl.style.display = (containsOverlays) ? "" : "none"; 
        
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
                this.layerSwitcher.updateMap();
            }
        }
        OpenLayers.Event.stop(e);
    },
    
    /**
     * Method: onLayerClick
     * Need to update the map accordingly whenever user clicks in either of
     *     the layers.
     * 
     * Parameters: 
     * e - {Event} 
     */
    onLayerClick: function(e) {
        this.updateMap();
    },


    /** 
     * Method: updateMap
     * Cycles through the loaded data and base layer input arrays and makes
     *     the necessary calls to the Map object such that that the map's 
     *     visual state corresponds to what the user has selected in 
     *     the control.
     */
    updateMap: function() {

        // set the newly selected base layer        
        for(var i=0; i < this.baseLayers.length; i++) {
            var layerEntry = this.baseLayers[i];
            if (layerEntry.inputElem.checked) {
                this.map.setBaseLayer(layerEntry.layer, false);
            }
        }

        // set the correct visibilities for the overlays
        for(var i=0; i < this.dataLayers.length; i++) {
            var layerEntry = this.dataLayers[i];   
            layerEntry.layer.setVisibility(layerEntry.inputElem.checked);
        }

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
        this.div.style.width = "20em";
        this.div.style.height = "";

        this.showControls(false);

        if (e != null) {
            OpenLayers.Event.stop(e);                                            
        }
    },
    
    /** 
     * Method: minimizeControl
     * Hide all the contents of the control, shrink the size, 
     *     add the maximize icon
     *
     * Parameters:
     * e - {Event} 
     */
    minimizeControl: function(e) {

        this.div.style.width = "0px";
        this.div.style.height = "0px";

        this.showControls(true);

        if (e != null) {
            OpenLayers.Event.stop(e);                                            
        }
    },

    /**
     * Method: showControls
     * Hide/Show all LayerSwitcher controls depending on whether we are
     *     minimized or not
     * 
     * Parameters:
     * minimize - {Boolean}
     */
     
    showControls: function(minimize) {

        this.maximizeDiv.style.display = minimize ? "" : "none";
        this.minimizeDiv.style.display = minimize ? "none" : "";

        this.layersDiv.style.display = minimize ? "none" : "";
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
        
        /*
        OpenLayers.Event.observe(this.div, "mouseup", 
            OpenLayers.Function.bindAsEventListener(this.mouseUp, this));
        OpenLayers.Event.observe(this.div, "click",
                      this.ignoreEvent);
        OpenLayers.Event.observe(this.div, "mousedown",
            OpenLayers.Function.bindAsEventListener(this.mouseDown, this));
        OpenLayers.Event.observe(this.div, "dblclick", this.ignoreEvent);
        */

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
        this.layersDiv.style.width = "100%";
        this.layersDiv.style.height = "100%";


        this.baseLbl = document.createElement("div");
        this.baseLbl.innerHTML = "<u>Base Layer</u>";
        this.baseLbl.style.marginTop = "3px";
        this.baseLbl.style.marginLeft = "3px";
        this.baseLbl.style.marginBottom = "3px";
        
        this.baseLayersDiv = document.createElement("div");
        this.baseLayersDiv.style.paddingLeft = "10px";
        /*OpenLayers.Event.observe(this.baseLayersDiv, "click", 
            OpenLayers.Function.bindAsEventListener(this.onLayerClick, this));
        */
                     

        this.dataLbl = document.createElement("div");
        this.dataLbl.innerHTML = "<u>Capas</u>";
        this.dataLbl.style.marginTop = "3px";
        this.dataLbl.style.marginLeft = "3px";
        this.dataLbl.style.marginBottom = "3px";
        
        this.dataLayersDiv = document.createElement("div");
        this.dataLayersDiv.style.paddingLeft = "10px";

        if (this.ascending) {
            this.layersDiv.appendChild(this.dataLbl);
            this.layersDiv.appendChild(this.dataLayersDiv);
        } else {
            this.layersDiv.appendChild(this.dataLbl);
            this.layersDiv.appendChild(this.dataLayersDiv);
        }    
 
        this.dataExtLbl = document.createElement("div");
        this.dataExtLbl.innerHTML = "<u>Capas Externas</u>";
        this.dataExtLbl.style.marginTop = "3px";
        this.dataExtLbl.style.marginLeft = "3px";
        this.dataExtLbl.style.marginBottom = "3px";
        this.dataExtLbl.onclick= function() {
//          this.elementSelected = null;
//          var getPlaceNameServicesReplyServer = {
//              callback: function(data) {                  var contentHTML;
        	var aWMSServers=new Array(

					['IDEE','http://www.idee.es/wms/IDEE-Base/IDEE-Base'],
					['IGN CARTOCIUDAD','http://www.cartociudad.es/wms/CARTOCIUDAD/CARTOCIUDAD'],
					['Catastro','http://ovc.catastro.meh.es/Cartografia/WMS/ServidorWMS.aspx'],
					['PNOA','http://www.idee.es/wms/PNOA/PNOA']
					/*['Comunidad Valenciana','http://orto.cth.gva.es/wmsconnector/com.esri.wms.Esrimap/wms_senderos?']*/
					
					/*['Catalunya - ICC Web Map Service','http://shagrat.icc.es/lizardtech/iserv/ows?'],
					['Junta Andalucia','http://www.andaluciajunta.es/IDEAndalucia/IDEAwms/wms/MTA100v?'],
					['Aragon', 'http://sitar.aragon.es/AragonWMS?'],
					['Asturias','http://www.cartografia.asturias.es/wmsortofotos/request.asp?'],
					['Comunidad Valenciana - Conselleria de Territori i Habitatge, GVA - Servici WMS: wms_senderos','http://orto.cth.gva.es/wmsconnector/com.esri.wms.Esrimap/wms_senderos?'],
					['Euskadi (Pais Vasco)','http://www1.euskadi.net/servlet/com.esri.wms.Esrimap?ServiceName=GVasco']*/
					
				);
				
                    var contentHTML;
//                    if (data != undefined && data.length > 0) {
                        contentHTML = '<form name="serverform">';
                        contentHTML += '<table align="left" width="99%" cellspacing="3" cellpadding="3" border=0>';
                        contentHTML += '<tr>';
                        contentHTML += '<td colspan=2 id="tableServer">';
                        contentHTML += '<table align="left" width=100% border=0>';
                        contentHTML += '<tr>';
                        contentHTML += '<td colspan=2 align="left"><hr/> </td>';
                        contentHTML += '</tr>';
                        contentHTML += '<tr>';
                        contentHTML += '<td colspan=2 align="left"><strong>Datos del servidor de mapas que desea añadir: </strong></td>';
                        contentHTML += '</tr>';
                        contentHTML += '<tr>';
                        contentHTML += '<td><strong>Servidor:</strong></td><td><select name="wmsServerList" id="wmsServerList" align="left" style="width:125px;"/></td>';
                        contentHTML += '</tr>';
                        contentHTML += '<tr>';
                        contentHTML += '<td align="left"><strong>Url:</strong></td>';
                        contentHTML += '<td align="left"><input type="text" name="urlserver" id="urlserver" align="left" size="30" value=""/><input type="hidden" name="urlserverhidden" id="urlserverhidden" value=""/></td>';
                        contentHTML += '</tr>';
                        contentHTML += '<tr>';
                        contentHTML += '<td align="left"><strong>Version:</strong></td>';
                        contentHTML += '<td align="left"><input type="text" name="versionserver" id="versionserver" align="left" size="15" value=""/></td>';
                        contentHTML += '</tr>';
                        contentHTML += '<tr>';
                        contentHTML += '<td>&nbsp;</td>';
                        contentHTML += '<td align="left"><input type="button" name="AceptarServer" value="Aceptar"  onClick="wmsmanagerctrl.connect2server();"/></td>';
                        contentHTML += '</tr>';
                        contentHTML += '<tr>';
                        contentHTML += '<td colspan=2></td>';
                        contentHTML += '</tr>';
                        contentHTML += '<tr>';
                        contentHTML += '<td colspan=2 id="wmsManagerOutput"></td>';
                        contentHTML += '</tr>';
                        contentHTML += '<tr>';
                        contentHTML += '<td colspan=2 id="layerStuff"></td>';
                        contentHTML += '</tr>';
                        contentHTML += '</td>';
                        contentHTML += '</tr>';

                        contentHTML += '</table>';
                        contentHTML += '</form>';
//                    } else {
//                        contentHTML += 'No existe ningún servicio de búsqueda definido';
//                    }
        			
                    OpenLayers.LocalgisUtils.showPopupBig(contentHTML);
                    wmsmanagerctrl = new OpenLayers.Control.WMSManager(aWMSServers);
                    wmsmanagerctrl.loadContents();
                    wmsmanagerctrl.setMap(map);
//              },
//              timeout:30000,
//              errorHandler:function(message) { 
//                  OpenLayers.LocalgisUtils.showError();
//              },
//              searchLocalgis: this
//          };
//          
//          WFSGService.getPlaceNameServices(getPlaceNameServicesReplyServer);
//      
        		
		
        };
        
        this.dataLayersExtDiv = document.createElement("div");
        this.dataLayersExtDiv.style.paddingLeft = "10px";

        if (this.ascending) {
            this.layersDiv.appendChild(this.dataExtLbl);
            this.layersDiv.appendChild(this.dataLayersExtDiv);
        } else {
            this.layersDiv.appendChild(this.dataExtLbl);
            this.layersDiv.appendChild(this.dataLayersExtDiv);
        }    
        
        this.div.appendChild(this.layersDiv);

        // No redondeamos nada
        /*
        OpenLayers.Rico.Corner.round(this.div, {corners: "tl bl",
                                        bgColor: "transparent",
                                        color: this.activeColor,
                                        blend: false});

        */

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

        // No añadimos el evento de minimizar
        var sz = new OpenLayers.Size(18,18);        
        this.minimizeDiv = document.createElement("div");
        this.minimizeDiv.style.top = "5px";
        this.minimizeDiv.style.right = "0px";
        this.minimizeDiv.style.left = "";
        this.minimizeDiv.style.display = "none";

        // No ponemos el div de minimizar
        //this.div.appendChild(this.minimizeDiv);
    },    
    
    /** 
     * Method: ignoreEvent
     * 
     * Parameters:
     * evt - {Event} 
     */
    ignoreEvent: function(evt) {
        OpenLayers.Event.stop(evt);
    },

    /** 
     * Method: mouseDown
     * Register a local 'mouseDown' flag so that we'll know whether or not
     *     to ignore a mouseUp event
     * 
     * Parameters:
     * evt - {Event}
     */
    mouseDown: function(evt) {
        this.isMouseDown = true;
        this.ignoreEvent(evt);
    },

    /** 
     * Method: mouseUp
     * If the 'isMouseDown' flag has been set, that means that the drag was 
     *     started from within the LayerSwitcher control, and thus we can 
     *     ignore the mouseup. Otherwise, let the Event continue.
     *  
     * Parameters:
     * evt - {Event} 
     */
    mouseUp: function(evt) {
        if (this.isMouseDown) {
            this.isMouseDown = false;
            this.ignoreEvent(evt);
        }
    },

    addPositionMarkerLocalgis: function (positionMarkerLocalgis) {
        this.positionMarkers.push(positionMarkerLocalgis);

        this.addPositionMarkerLocalgisToDiv(positionMarkerLocalgis);
        
        this.map.markersLayer.addMarker(positionMarkerLocalgis); 
    },

    addPositionMarkerLocalgisToDiv: function(positionMarkerLocalgis) {
        var div = document.createElement("div");

        div.className = "olMarkerListLocalgisElement";
        div.style.cursor = "default";
        
        var divLabel = document.createElement("div");
        divLabel.className = "olMarkerListLocalgisElementLabel";
        divLabel.style.cursor = "default";
        divLabel.innerHTML = unescape(positionMarkerLocalgis.name);
        
        div.appendChild(divLabel);
        
        this.legendMarkersDiv.appendChild(div);
    },

    CLASS_NAME: "OpenLayers.Control.LayerInfoLocalgis"

});
