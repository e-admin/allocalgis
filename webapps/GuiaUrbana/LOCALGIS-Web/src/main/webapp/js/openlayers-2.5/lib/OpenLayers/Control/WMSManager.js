/* Copyright (c) 2006-2007 MetaCarta, Inc., published under the BSD license.
 * See http://svn.openlayers.org/trunk/openlayers/release-license.txt 
 * for the full text of the license. */

/** 
 * @class
 *  
 * @author Lorenzo Becchi (ominiverdi.org)
 * @projectDescription ticket #687
 * 
 * @requires OpenLayers/Control.js
 */
OpenLayers.Control.WMSManager = OpenLayers.Class.create();
OpenLayers.Control.WMSManager.prototype = 
  OpenLayers.Class.inherit( OpenLayers.Control, {

   

  // DOM Elements
  
  	/** @type DOMElement */
    olLayerManager: null,
   
	
	/** @type DOMElement */
	olServerList: null,
	
	/** @type DOMElement */
	olLegend: null,
	
	/** @type DOMElement */
	olLayerManagerHeader: null,
	
	/** @type DOMElement */
	WMSManagerForm: null,
	
  
    /** @type DOMElement */
    layersDiv: null,
	
	/** @type DOMElement */
	layerStuff: null,
    
    /** @type DOMElement */
    baseLayersDiv: null,
	
	/** @type DOMElement */
    selectImageType: null,
	
	/** @type DOMElement */
	overlayButton:null,

    /** @type Array */
    baseLayerInputs: null,
    
    
    /** @type DOMElement */
    dataLbl: null,
    
    /** @type DOMElement */
    dataLayersDiv: null,

    /** @type Array */
    dataLayerInputs: null,


    /** @type DOMElement */
    minimizeDiv: null,

    /** @type DOMElement */
    maximizeDiv: null,
    
	//Arrays
    /** @type Array */
    aWMSServer: null,
	
	//WMS layers features
	/** @type Array
	 *  @param Name:Title:Abstract:BoundingBox:LegendURL:queryable
	 */ 
	aWMSLayers: [],
	
	//aLayersQueryable: [],
	//Image formats for WMS server
	/** @type Array
	 */ 
	aImageFormats: [],
	
    //others
   
    /** @type Boolean */
    ascending: true,
	
 	/** @type string */
    activePanel: 'olLegend',
	
	//WMS default params
	/** @type string */
    exceptionsValue: 'application/vnd.ogc.se_inimage',//OL default value
	resolutionsValue: null,//OL default value
	
 
    /**
    * @constructor
    */
    initialize: function(aWMSServer,targetObj,options) {
        OpenLayers.Control.prototype.initialize.apply(this, arguments);
		this.aWMSServer= aWMSServer;
		this.targetObj = targetObj;
		
    },

    /**
     *  
     */    
    destroy: function() {
        
        /*OpenLayers.Event.stopObservingElement(this.div);

        OpenLayers.Event.stopObservingElement(this.minimizeDiv);
        OpenLayers.Event.stopObservingElement(this.maximizeDiv);

        //clear out layers info and unregister their events 
        this.clearLayersArray("base");
        this.clearLayersArray("data");
        */
        this.map.events.unregister("addlayer", this, this.redraw);
        this.map.events.unregister("changelayer", this, this.redraw);
        this.map.events.unregister("removelayer", this, this.redraw);
        this.map.events.unregister("changebaselayer", this, this.redraw);
        
        OpenLayers.Control.prototype.destroy.apply(this, arguments);
    },

    /** 
     * @param {OpenLayers.Map} map
     */
    setMap: function(map) {
        OpenLayers.Control.prototype.setMap.apply(this, arguments);

        this.map.events.register("addlayer", this, this.redraw);
        this.map.events.register("changelayer", this, this.redraw);
        this.map.events.register("removelayer", this, this.redraw);
        this.map.events.register("changebaselayer", this, this.redraw);
		
		
    },

    /**
    * @returns A reference to the DIV DOMElement containing the switcher tabs
    * @type DOMElement
    */  
    draw: function() {
        OpenLayers.Control.prototype.draw.apply(this);
		
		
		
        // create layout divs
        this.loadContents();

        // set mode to minimize
        //this.minimizeControl();
        //this.showControls();
		
        // populate div with current info
        /*this.redraw();    
		
		this.switchPanel();
		this.maximizeControl();
        return this.div;*/
    },

    /** user specifies either "base" or "data". we then clear all the 
     *    corresponding listeners, the div, and reinitialize a new array.
     * 
     * @private
     * 
     * @param {String} layersType Ei
     */
    clearLayersArray: function(layersType) {
        var layers = this[layersType + "Layers"];
        if (layers) {
            for(var i=0; i < layers.length; i++) {
                var layer = layers[i];
                OpenLayers.Event.stopObservingElement(layer.inputElem);
                OpenLayers.Event.stopObservingElement(layer.labelSpan);
            }
        }
        this[layersType + "LayersDiv"].innerHTML = "";
        this[layersType + "Layers"] = [];
    },


    /** Goes through and takes the current state of the Map and rebuilds the
     *   control to display that state. Groups base layers into a radio-button
     *   group and lists each data layer with a checkbox.
     * 
     * @returns A reference to the DIV DOMElement containing the control
     * @type DOMElement
     */  
    redraw: function() {

        //clear out previous layers 
       /* this.baseLayersDiv.innerHTML = "";
        this.baseLayerInputs = [];
        
        this.dataLayersDiv.innerHTML = "";
        this.dataLayerInputs = [];
        
        var containsOverlays = false;
        
        var layers = this.map.layers.slice();
		if(layers.length == 0) 
				this.baseLayersDiv.innerHTML = "<p><span style='color:yellow'>no layers added</span><br> Please add some using 'WMS Servers' selector or using 'Catalogue' search.</p>";
		
        if (!this.ascending) { layers.reverse(); }
        for( var i = 0; i < layers.length; i++) {
            var layer = layers[i];
            var baseLayer = layer.isBaseLayer;

            if (baseLayer || layer.displayInLayerSwitcher) {
				
				//GET LAYER INFOS from WMS array
				// set gently layer Name	
				var layerName=  layer.name;
				
                if (!baseLayer) {
                    containsOverlays = true;
                }

                // only check a baselayer if it is *the* baselayer, check data
                //  layers if they are visible
                var checked = (baseLayer) ? (layer == this.map.baseLayer)
                                          : layer.getVisibility();
    
                // create input element
                var inputElem = document.createElement("input");
                inputElem.id = "input_" + layer.name;
                inputElem.name = (baseLayer) ? "baseLayers" : layer.name;
                inputElem.type = (baseLayer) ? "radio" : "checkbox";
                inputElem.value = layer.name;
                inputElem.checked = checked;
                inputElem.defaultChecked = checked;
				inputElem.layer = layer;
                inputElem.control = this;


                if (!baseLayer && !layer.inRange) {
                    inputElem.disabled = true;
                }
                

                OpenLayers.Event.observe(inputElem, "mouseup",  this.onInputClick.bindAsEventListener(inputElem));
                
                // create span
                /*var labelSpan = document.createElement("span");
                if (!baseLayer && !layer.inRange) {
                    labelSpan.style.color = "gray";
                }*/
				
				/*var layerUl = document.createElement("ul");
				
				var layerTitle = '';
				var aExtents = [];
				var aLayersName = layer.name.split(',');
				for(j=0;j<aLayersName.length;j++){
					var selectedLayer = aLayersName[j];
					try
					{
					for(y=0;y<layer.aWMSLayers.length;y++){
						var legendUrl = null;
						var listedLayer = layer.aWMSLayers[y];
						if(selectedLayer==listedLayer[0]) {
							//list of layer titles
							/*
								layerTitle +=(layerTitle.length>0)?
									    ', ' + '[' + listedLayer[0] + '] ' + listedLayer[1]
									  :   '[' + listedLayer[0] + '] ' + listedLayer[1];
								
								//show if queryable
								if(listedLayer[5]==1) layerTitle +=' (q)'; 
							*/
							
							/*var li = document.createElement('li');
							li.innerHTML =  '[' + listedLayer[0] + '] ' + listedLayer[1];
							if(listedLayer[5]==1) li.innerHTML +=' (q)'; 
							layerUl.appendChild(li);
							
							 
							
							//find layers extents
							if(listedLayer[3].length==4){
								aExtents.push(listedLayer[3]);//bounding boxes
							} else if(listedLayer[3].split){
								var bbox = listedLayer[3].split(',');
								aExtents.push(bbox);//bounding boxes
							} else alert('wrong bbox: '+listedLayer[3])
						}
					}
					}catch(e){}
				}

                //labelSpan.innerHTML = layerTitle;
                layerUl.style.verticalAlign = (baseLayer) ? "bottom" 
                                                            : "baseline";
                OpenLayers.Event.observe(layerUl, "click",   this.onInputClick.bindAsEventListener(inputElem));
                
				
				
				// create line break
                var br = document.createElement("br");
    			
				//create legend Tool and image
				var legendTools = document.createElement("div");
				legendTools.style.paddingLeft ='10px';
				legendTools.style.display ='none';
				
				//create opacity value
				var opacity = (layer.opacity)?layer.opacity:1;
    			var opSpan = document.createElement("span");
				opSpan.innerHTML = opacity;
				
				// create opacity Anchors
                var aUp = document.createElement("a");
				aUp.href="#";
				aUp.layer=layer;
				aUp.opSpan = opSpan;
				aUp.className="olLegendLayerOpacity";
				aUp.onclick = function(){
									var maxOpacity = 1;
  								    var minOpacity = 0.1;
									var opacity=(this.layer.opacity)?this.layer.opacity:1;
									var newOpacity = (parseFloat(opacity) + 0.1).toFixed(1);
          							newOpacity = Math.min(maxOpacity, Math.max(minOpacity, newOpacity));
									this.layer.setOpacity(newOpacity);
									this.opSpan.innerHTML = newOpacity;
								};
				aUp.innerHTML = '<span>&gt;</span>';
    			
				var aDown = document.createElement("a");
				aDown.href="#";
				aDown.layer=layer;
				aDown.opSpan = opSpan;
				aDown.className="olLegendLayerOpacity";
				aDown.onclick = function(){
									var maxOpacity = 1;
  								    var minOpacity = 0.1;
									var opacity=(this.layer.opacity)?this.layer.opacity:1;
									var newOpacity = (parseFloat(opacity) - 0.1).toFixed(1);
          							newOpacity = Math.min(maxOpacity, Math.max(minOpacity, newOpacity));
									this.layer.setOpacity(newOpacity);
									this.opSpan.innerHTML = newOpacity;
								};
				aDown.innerHTML = '<span>&lt;</span>';
				
				
				//Create DropLayer
                var aDrop = document.createElement("a");
				aDrop.innerHTML = '<span>X</span>';
				aDrop.className = 'olLegendLayerDrop';
				aDrop.layer = layer;
				aDrop.onclick = function(){
									this.layer.map.removeLayer(this.layer);
								};
								
				//Create SwitchLayer
                var aSwitchUp = document.createElement("a");
				aSwitchUp.innerHTML = '<span>&uarr;</span>';
				aSwitchUp.className = 'olLegendLayerSwitch';
				aSwitchUp.layer = layer;
				aSwitchUp.onclick = function(){
									var startZ = this.layer.map.getLayerIndex(this.layer);
									if(startZ==0)return alert("this is already the top");
									this.layer.map.raiseLayer(this.layer,-1);
								};
				var aSwitchDown = document.createElement("a");
				aSwitchDown.innerHTML = '<span>&darr;</span>';// + ' ' + layer.map.getLayerIndex(layer)
				aSwitchDown.className = 'olLegendLayerSwitch';
				aSwitchDown.layer = layer;
				aSwitchDown.onclick = function(){
									var startZ = this.layer.map.getLayerIndex(this.layer);
									var numL = this.layer.map.getNumLayers();
									//alert(startZ + ':' + numL);
									if(startZ==numL-1)return alert("this is already the bottom");
									this.layer.map.raiseLayer(this.layer,1);
									//NEED A PATCH IN this.layer.map.raiseLayer TO AVOID COUNTING BASE LAYERS
								};
				
				//expand legend icon				
				var aExpandIcon = document.createElement("a");
				aExpandIcon.innerHTML = '<span> + </span>';
				aExpandIcon.className = 'olLegendIconExpander';
				aExpandIcon.layer = layer;
				aExpandIcon.expanded = true;
				aExpandIcon.legendTools = legendTools;
				aExpandIcon.onclick = function(){
									if(this.expanded){
										this.innerHTML = '<span> - </span>';
										this.legendTools.style.display = "block";
										this.expanded = false;
									}else {
										this.innerHTML = '<span> + </span>';
										this.legendTools.style.display = "none";
										this.expanded = true;
									}
								};
				
				//zoom to layer extent icon	
				var bounds = null;
				for(u=0;u<aExtents.length;u++){
					var ext = aExtents[u];
					if(!bounds) {
						bounds = new OpenLayers.Bounds(ext[0], ext[1], ext[2], ext[3]); 
					} 
					else {
						var nBounds = new OpenLayers.Bounds(ext[0], ext[1], ext[2], ext[3]); 
						bounds.extend(nBounds);
						
					}
				}		
				var aZoomToIcon = document.createElement("a");
				aZoomToIcon.innerHTML = '<span> zoom </span>';
				aZoomToIcon.title = 'zoom to extent';
				aZoomToIcon.className = 'olLegendIconZoomer';
				aZoomToIcon.layer = layer;
				aZoomToIcon.bounds = bounds;
				aZoomToIcon.onclick = function(){
									//alert(this.bounds);
									
									this.layer.map.zoomToExtent(this.bounds);
								};
				
				
				if(!baseLayer){
					var layerTools = document.createElement("div");
					layerTools.className = 'olLegendLayerTools';
					layerTools.appendChild(aDown);
              	  	layerTools.appendChild(opSpan);
					layerTools.appendChild(aUp);
					layerTools.appendChild(aSwitchDown);
					layerTools.appendChild(aSwitchUp);
					legendTools.appendChild(layerTools);
				}
				
				
				var legendImages = document.createElement("div");
				legendImages.className = 'olLegendLegendImages';
				var aLayersName = layer.name.split(',');
				for(j=0;j<aLayersName.length;j++){
					var selectedLayer = aLayersName[j];
					try{
					for(y=0;y<layer.aWMSLayers.length;y++){
						var legendUrl = null;
						var listedLayer = layer.aWMSLayers[y];
						if(selectedLayer==listedLayer[0]) {
							
							//set legend url
							legendName = listedLayer[0];
							legendTitle = listedLayer[1];
							legendAbstract = listedLayer[2];
							legendQueriable = listedLayer[5];
							legendUrl = listedLayer[4];
						
						
							var legendSpan = document.createElement("span");
								var mess = '<table><tr><th>Name</th><td> '+ legendName + '</td></tr>' +
													'<tr><th>Title</th><td>'+legendTitle  +'</td></tr>' +
													'<tr><th>Abstract</th><td>'+ legendAbstract +'</td></tr>' +
													'<tr><th>Queryable</th><td>'+legendQueriable + '</td></tr>';
							
							if(legendUrl){
								mess += '<tr><th>Image</th><td><img src="'+legendUrl + '"></td></tr>';
								//'<h5>Legend Image:</h5><img src="'+legendUrl+'">';
								//var legendImg = document.createElement("img");
								//legendImg.src =legendUrl;
								//legendSpan.appendChild(legendImg);	
							}else {
								mess += '<tr><th>Image</th><td>none</td></tr>';
							}
							
							legendSpan.innerHTML = mess + '</tr></table>';
							legendImages.appendChild(legendSpan);
							legendImages.style.display = 'block';
						}
					}
					}catch(e){}
				
				}			
				
				legendTools.appendChild(legendImages);
				
				
				
				
                var groupArray = (baseLayer) ? this.baseLayerInputs
                                             : this.dataLayerInputs;
                groupArray.push(inputElem);
                                                     
    
                var groupDiv = (baseLayer) ? this.baseLayersDiv
                                           : this.dataLayersDiv;
                
				//create Layer div 
				var layerDiv = document.createElement("div");
				layerDiv.className = 'olLegendLayerDiv';
				layerDiv.appendChild(inputElem);
				layerDiv.appendChild(aDrop);
				layerDiv.appendChild(aExpandIcon);	
                layerDiv.appendChild(aZoomToIcon);	
                //layerDiv.appendChild(labelSpan);
				layerDiv.appendChild(layerUl);
                //layerDiv.appendChild(br);
                
				
				
				layerDiv.appendChild(legendTools);
				
				
				//append to group div
                groupDiv.appendChild(layerDiv);
				
            }
        }

        // if no overlays, dont display the overlay label
        this.dataLbl.style.display = (containsOverlays) ? "" : "none";        

        return this.div;*/
    },

    /** the overlay checbox has been clicked, check or uncheck its corresponding input
     * 
     * @private
     * 
     * @param {Event} e
     */

    onOverlayClick: function(e) {
        if (!this.disabled) {
            if (this.type == "radio") {
                this.checked = true;
                //this.layer.map.setBaseLayer(this.layer, true);
                //this.layer.map.events.triggerEvent("changebaselayer");
            } else {
                this.checked = !this.checked;
            }
        }
        OpenLayers.Event.stop(e);
    },
	
	/** the overlay checbox has been clicked, check or uncheck its corresponding input
     * 
     * @private
     * 
     * @param {Event} e
     */

    onServerSelectFocus: function(e) {
		
		//TODO DELETE THIS IF NOT NEEDED
		//alert('focus');
       /* if (!this.disabled) {
            if (this.type == "radio") {
                this.checked = true;
                //this.layer.map.setBaseLayer(this.layer, true);
                //this.layer.map.events.triggerEvent("changebaselayer");
            } else {
                this.checked = !this.checked;
            }
        }*/
        OpenLayers.Event.stop(e);
    },
	
	/** A label has been clicked, check or uncheck its corresponding input
     * 
     * @private
     * 
     * @param {Event} e
     */

    onInputClick: function(e) {
        if (!this.disabled) {
            if (this.type == "radio") {
                this.checked = true;
                this.layer.map.setBaseLayer(this.layer, true);
                this.layer.map.events.triggerEvent("changebaselayer");
            } else {
                this.checked = !this.checked;
                this.control.updateMap();
            }
        }
        OpenLayers.Event.stop(e);
    },
    
    /** Need to update the map accordingly whenever user clicks in either of
     *   the layers.
     * 
     * @private
     * 
     * @param {Event} e
     */
    onLayerClick: function(e) {
        this.updateMap();
    },


    /** Cycles through the loaded data and base layer input arrays and makes
     *   the necessary calls to the Map object such that that the map's 
     *   visual state corresponds to what the user has selected in the control
     * 
     * @private
     */
    updateMap: function() {

        // set the newly selected base layer        
        for(var i=0; i < this.baseLayerInputs.length; i++) {
            var input = this.baseLayerInputs[i];   
            if (input.checked) {
                this.map.setBaseLayer(input.layer, false);
            }
        }

        // set the correct visibilities for the overlays
        for(var i=0; i < this.dataLayerInputs.length; i++) {
            var input = this.dataLayerInputs[i];   
            input.layer.setVisibility(input.checked, true);
        }

    },

    /** Set up the labels and divs for the control
     * 
     * @param {Event} e
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
    
    /** Hide all the contents of the control, shrink the size, 
     *   add the maximize icon
     * 
     * @param {Event} e
     */
    minimizeControl: function(e) {

        this.div.style.width = "0px";
        this.div.style.height = "0px";

        this.showControls(true);

        if (e != null) {
            OpenLayers.Event.stop(e);                                            
        }
    },

    /** Hide/Show all LayerSwitcher controls depending on whether we are
     *   minimized or not
     * 
     * @private
     * 
     * @param {Boolean} minimize
     */
    showControls: function(minimize) {

        this.maximizeDiv.style.display = minimize ? "" : "none";
        this.minimizeDiv.style.display = minimize ? "none" : "";

        this.layersDiv.style.display = minimize ? "none" : "";
    },
    
    /** Set up the labels and divs for the control
     * 
     */
    loadContents: function() {

        //configure main div
        /*this.div.style.position = "absolute";
        this.div.style.top = "25px";
        this.div.style.right = "0px";
        this.div.style.left = "";
        this.div.style.fontFamily = "sans-serif";
        this.div.style.fontWeight = "bold";
        this.div.style.marginTop = "3px";
        this.div.style.marginLeft = "3px";
        this.div.style.marginBottom = "3px";
        this.div.style.fontSize = "smaller";   
        this.div.style.color = "white";   
        this.div.style.backgroundColor = "transparent";
    
        OpenLayers.Event.observe(this.div, "mouseup", 
                      this.mouseUp.bindAsEventListener(this));
        OpenLayers.Event.observe(this.div, "click",
                      this.ignoreEvent);
        OpenLayers.Event.observe(this.div, "mousedown",
                      this.mouseDown.bindAsEventListener(this));
        OpenLayers.Event.observe(this.div, "dblclick", this.ignoreEvent);

		//create olLayerManagerHeader div
		this.olLayerManagerHeader = document.createElement("div");
		this.olLayerManagerHeader.className = "olLayerManagerHeader";
		// add Legend button
		this.buttonLayers = document.createElement("a");
		this.buttonLayers.href="#";
		this.buttonLayers.LayerManager =this;
		this.buttonLayers.panel ='olLegend';
		this.buttonLayers.onclick =this.switchPanel;
		var spanL = document.createElement("span");
		spanL.innerHTML = "Legend";
		this.buttonLayers.appendChild(spanL);
		
		// add WMS Servers button
		this.buttonServers = document.createElement("a");
		this.buttonServers.href="#";
		this.buttonServers.LayerManager =this;
		this.buttonServers.panel = 'olServerList';
		this.buttonServers.onclick =this.switchPanel;
		var spanS = document.createElement("span");
		spanS.innerHTML = "WMS Servers";
		this.buttonServers.appendChild(spanS);
		
		// add Catalogue Server button
		this.catalogueServers = document.createElement("a");
		this.catalogueServers.href="#";
		this.catalogueServers.LayerManager =this;
		this.catalogueServers.panel = 'olCatalogue';
		this.catalogueServers.onclick =this.switchPanel;
		var spanS = document.createElement("span");
		spanS.innerHTML = "Catalogue";
		this.catalogueServers.appendChild(spanS);
		
		this.olLayerManagerHeader.appendChild(this.buttonLayers);
		this.olLayerManagerHeader.appendChild(this.buttonServers);
		this.olLayerManagerHeader.appendChild(this.catalogueServers);
		*/
		
		/*
		 * Catalogue panel 
		 */
		
		/*this.olCatalogue = document.createElement("div");
		this.olCatalogue.className = "olCatalogue";
		
		this.catalogueLog  = document.createElement('p');
		this.catalogueLog.className = 'CatalogueOutput';
		this.catalogueLog.innerHTML = 'Write a search string and press "search" button. Then wait for layer list to appear.';
		this.olCatalogue.appendChild(this.catalogueLog);
		
		//Catalogue FORM
		var form = document.createElement('form');
		form.name='CatalogueConnector';
		form.onsubmit = function() { return null; };
		form.action='#';
		
		this.catalogueInput = document.createElement('input');
		this.catalogueInput.className = 'CatalogueInput';
		this.catalogueInput.type = 'text';
		OpenLayers.Event.observe(this.catalogueInput, "click",
                      function(evt) { evt.target.focus();});
		form.appendChild(this.catalogueInput);
		
		this.catSubmit = document.createElement('input');
		this.catSubmit.type = 'button';
		this.catSubmit.value = 'search';
		this.catSubmit.WMSManager = this;
		OpenLayers.Event.observe(this.catSubmit, "click",
                      function(evt) { 
					  	var str = this.WMSManager.catalogueInput.value;
						if(!str) {
							return alert('you can\'t  search an empty string');	
						}
						console.log(str);
						this.WMSManager.searchCatalogue(this);
					});
		
		form.appendChild(this.catSubmit);
		
		this.olCatalogue.appendChild(form);
		
		
		*/

		/*
		 * WMS server form - panel
		 */
		
		//div to pack all layers stuff 
		
		/*this.layerStuff = document.createElement('div');
		this.layerStuff.className = 'wmsLayerLayerStuff';
		
		document.getElementById("layerStuff").appendChild(this.layerStuff);
		//server select
		this.olServerList = document.createElement("div");
		this.olServerList.className = "olServerList";
		var p = document.createElement('p');
		//p.innerHTML = 'Use the select box here below to choose a server. Wait for remote response.';
		this.olServerList.appendChild(p);
		var p = document.createElement('p');
		p.id = 'wmsManagerOutput';
		this.olServerList.appendChild(p);
		
		var form = document.createElement('form');
		form.name='WMSServerConnector';
		form.onsubmit = function() {return null;};
		form.action='#';
		this.olServerList.appendChild(form);
		
		//Make server select
		/*this.serverSelect = document.createElement('select');
		this.serverSelect.className = 'wmsServerList';
		this.serverSelect.id = 'wmsServerList';
		this.serverSelect.WMSManager = this;
		
		this.serverSelect.style.width = "220px";
		this.serverSelect.onchange = this.connect2server;*/
		
		/*OpenLayers.Event.observe(this.serverSelect, "mouseup",  this.onServerSelectFocus.bindAsEventListener(this.serverSelect));
		OpenLayers.Event.observe(this.serverSelect, "blur",  this.onServerSelectFocus.bindAsEventListener(this.serverSelect));
		OpenLayers.Event.observe(this.serverSelect, "focus",  this.onServerSelectFocus.bindAsEventListener(this.serverSelect));*/
		
		
		/*var j = 0;
		var opt = new Option( 'select a server', '', true, true );
		this.serverSelect[j++] = opt;
		
		form.appendChild(this.serverSelect);
		for(i=0;i<this.aWMSServer.length;i++) {
			this.serverSelect[j++] = new Option(this.aWMSServer[i][0],this.aWMSServer[i][1],false,false);
		}
		*/
		//form.appendChild(this.layerStuff);
		
		/*
		 * OL Layers Manager - panel
		 */
		
        // layers list div        
        /*this.layersDiv = document.createElement("div");
        this.layersDiv.id = "layersDiv";
        this.layersDiv.style.paddingTop = "5px";
        this.layersDiv.style.paddingLeft = "10px";
        this.layersDiv.style.paddingBottom = "5px";
        //this.layersDiv.style.paddingRight = "75px";
        this.layersDiv.style.backgroundColor = this.activeColor;        
		
        // had to set width/height to get transparency in IE to work.
        // thanks -- http://jszen.blogspot.com/2005/04/ie6-opacity-filter-caveat.html
        //
        this.layersDiv.style.width = "100%";
        this.layersDiv.style.height = "100%";
	
		

        var baseLbl = document.createElement("div");
        baseLbl.innerHTML = "<u>Base Layer</u>";
        baseLbl.style.marginTop = "3px";
        baseLbl.style.marginLeft = "3px";
        baseLbl.style.marginBottom = "3px";
        
        this.baseLayersDiv = document.createElement("div");
        this.baseLayersDiv.style.paddingLeft = "10px";*/
        /*OpenLayers.Event.observe(this.baseLayersDiv, "click", 
                      this.onLayerClick.bindAsEventListener(this));
        */
                     

        /*this.dataLbl = document.createElement("div");
        this.dataLbl.innerHTML = "<u>Overlays</u>";
        this.dataLbl.style.marginTop = "3px";
        this.dataLbl.style.marginLeft = "3px";
        this.dataLbl.style.marginBottom = "3px";
        
        this.dataLayersDiv = document.createElement("div");
        this.dataLayersDiv.style.paddingLeft = "10px";
		this.olLegend = document.createElement("div");
		this.olLegend.className = "olLegend";
		
		this.layersDiv.appendChild(this.olLayerManagerHeader);
        if (this.ascending) {
            this.olLegend.appendChild(baseLbl);
            this.olLegend.appendChild(this.baseLayersDiv);
            this.olLegend.appendChild(this.dataLbl);
            this.olLegend.appendChild(this.dataLayersDiv);
			
        } else {
            this.olLegend.appendChild(this.dataLbl);
            this.olLegend.appendChild(this.dataLayersDiv);
            this.olLegend.appendChild(baseLbl);
            this.olLegend.appendChild(this.baseLayersDiv);
        }    
		*/
        /*this.layersDiv.appendChild(this.olLegend);
 		this.layersDiv.appendChild(this.olServerList);
 		this.layersDiv.appendChild(this.olCatalogue);*/
		
		
		//document.getElementById("servidores").appendChild(this.olServerList);
        //this.div.appendChild(this.layersDiv);

        /*OpenLayers.Rico.Corner.round(this.div, {corners: "tl bl",
                                        bgColor: "transparent",
                                        color: this.activeColor,
                                        blend: false});

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
        OpenLayers.Event.observe(this.maximizeDiv, 
                      "click", 
                      this.maximizeControl.bindAsEventListener(this));
        
        this.div.appendChild(this.maximizeDiv);

        // minimize button div
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
        OpenLayers.Event.observe(this.minimizeDiv, 
                      "click", 
                      this.minimizeControl.bindAsEventListener(this));

        this.div.appendChild(this.minimizeDiv);*/
        
        
        //Make server select
        
		var form =  document.forms.serverform;
		this.serverSelect = document.getElementById("wmsServerList");
		this.serverSelect.WMSManager = this;
		this.serverSelect.onchange = this.selectserver;
		OpenLayers.Event.observe(this.serverSelect, "mouseup",  this.onServerSelectFocus.bindAsEventListener(this.serverSelect));
		OpenLayers.Event.observe(this.serverSelect, "blur",  this.onServerSelectFocus.bindAsEventListener(this.serverSelect));
		OpenLayers.Event.observe(this.serverSelect, "focus",  this.onServerSelectFocus.bindAsEventListener(this.serverSelect));
		var j = 0;
		var opt = new Option( '', '', true, true );
		this.serverSelect[j++] = opt;
		
		
		for(i=0;i<this.aWMSServer.length;i++) {
			var option = new Option(this.aWMSServer[i][0],this.aWMSServer[i][1],false,false);
			if(this.aWMSServer[i][2]){
				option.sldService = this.aWMSServer[i][2];
			}
			this.serverSelect[j++] = option;
		}
    },
    
	
	   /** 
     * @private
     *
     * @param {Event} evt
     */
     selectserver:function(evt)
     {
     	document.getElementById("wmsManagerOutput").innerHTML="";
		document.getElementById("layerStuff").innerHTML="";
		
     	 var select = this;
		 if((select)) {
		 	var url = select[select.selectedIndex].value;
			if(select[select.selectedIndex].sldService){
				this.WMSManager.sldService = select[select.selectedIndex].sldService;
			} else {
				this.WMSManager.sldService = '';
			}	 
			
		 } else {
		 	url ="";
		 }
		 if(url !="")
		 {
		  document.forms.serverform.urlserver.value = url;
		  document.forms.serverform.versionserver.value = "1.1.1";
		 }
		 else
		 {
		 	
		
		 	document.forms.serverform.urlserver.value = "";
		  document.forms.serverform.versionserver.value = "";
		 }
     },
    connect2server: function(evt) {
    	
         document.getElementById("layerStuff").innerHTML ='';
		 
		 var baseURL = "";//"http://www.idee.es/wms/IDEE-Base/IDEE-Base?&service=WMS&version=1.1.1&request=GetCapabilities";
		 document.forms.serverform.urlserverhidden.value = document.forms.serverform.urlserver.value;
		 
		 if(document.forms.serverform.urlserver.value !="")
		 {
		 	baseURL = document.forms.serverform.urlserver.value;
		 	
		 	if (baseURL.indexOf('?') == -1)
		    {
		        baseURL = baseURL + '?';
		    }
		    else
		    {
		        if (baseURL.charAt( baseURL.length - 1 ) == '&')
		            baseURL = baseURL.slice( 0, -1 );
		    }
		    
		 	baseURL = baseURL+ "&service=WMS";
		 
		 	if(document.forms.serverform.versionserver.value!="")
		 	{
	    		baseURL = baseURL +  "&version="+document.forms.serverform.versionserver.value;
//		 		baseURL = baseURL +  "&version=1.1.0";
	    	}
	    	else
	    	{
	    		baseURL = baseURL +  "&version=1.1.1";
	    	}
	    	baseURL = baseURL + "&request=GetCapabilities";
	    
		 	
		    
		    this.WMSManager = wmsmanagerctrl;
	    	document.getElementById("wmsManagerOutput").innerHTML = "Cargando listado de capas ...";
	    	OpenLayers.loadURLLocalgis(baseURL, null, this, this.WMSManager.parseGetCapabilities);
        
	    
		 }
		 else
		 {
		 	alert("Introduzca la url del servidor");
		 }
		 
    },
	
	
	
	/** 
     * @private 
     *
     * @param {XMLHttpRequest} ajaxRequest
     */
    parseGetCapabilities: function(ajaxRequest) {
   	
		var text = ajaxRequest.responseText;
		
		if(text == "error")
		{
			document.getElementById("wmsManagerOutput").innerHTML ="<font color='#FF0000'>Se ha producido un error al intentar conectar con el servidor</font>";
		}
				 
		text = text.replace(/\<!--.*?-->/g, ''); //Helped with ESA server
		text = text.replace(/\[.<!.*?>.\]/g, ''); //Helped with ESA server
		text = text.replace(/<GetTileService>.*?GetTileService>/g, '');//skip NASA DTD error
	
//		var xml = text; 
//		if (!xml || ajaxRequest.fileType!="XML") {
//			xml = ajaxRequest.responseText;
//		}
//		if (typeof xml == "string") { 
//			xml = OpenLayers.parseXMLString(text);
//		} 
		
		var xml = ajaxRequest.responseXML;
				
		if (!xml || ajaxRequest.fileType!="XML") {
			xml = ajaxRequest.responseText;
		
		}
		if (typeof xml == "string") { 
			
			xml = OpenLayers.parseXMLString(xml);
		} 
		    	   	
		//var xml = OpenLayers.parseXMLString(text);
		
		if(xml == null) return alert('incorrect content: check your WMS url');
		
		if(xml.childNodes.length==0) {
			try{
				if(OpenLayers.Ajax.getParseErrorText(xml) == OpenLayers.Ajax.PARSED_OK){
			      // The document was parsed/loaded just fine, go on
			      //alert(xml);
				  alert('No existen capas');
			    } 
			    else{
			      	
					/*var error = OpenLayers.Ajax.getParseErrorText(xml);
			      	
				  	var newwindow;
					newwindow=window.open('','','menubar=1,scrollbars=1,width=400,height=300,resizable=1') ;
					newwindow.document.writeln('<html> <head> <title>Error Parsing GetCapabilties<\/title> <\/head> <body><pre><code>');
					newwindow.document.writeln(OpenLayers.Ajax.escape(error));
					newwindow.document.writeln('<\/code><\/pre> <\/body> <\/html>');
					newwindow.document.close();*/
					
					document.getElementById("wmsManagerOutput").innerHTML ="<font color='#FF0000'>Se ha producido un error al intentar conectar con el servidor</font>";
			    };
				//return alert('the browser is not able to parse this GetCapabilities. Try with Firefox  ;-) ');
				

			} catch(e){
				alert(e.description);
			}
		}
		
		if(typeof xml=='object'){
			
			
			//Check exceptionsValue
			var exceptions = xml.getElementsByTagName('Exception');
			if(exceptions.length){
				var aFormats = exceptions[0].getElementsByTagName('Format');
				var exceptionsValue = this.WMSManager.exceptionsValue;
				var formatExists = null;
				var oFormats = [];
				for(i=0;i<aFormats.length;i++){
						//var format =(aFormats[i].textContent)? aFormats[i].textContent:aFormats[i].text;
						//var format = OpenLayers.Ajax.getText(aFormats[i]);
						try{
						var format = OpenLayers.Ajax.getText(aFormats[i]);
						} catch(e){alert(e)};
						if(format ==exceptionsValue) formatExists = true;
						else oFormats.push(format);
				}
				//if default format is not supported use the first usable
				if(!formatExists) this.WMSManager.exceptionsValue = oFormats[0];
			}
			
			
			
			//Check GetMap Formats
			//var GetMap = xml.getElementsByTagName('GetMap');
			
			var GetMap = xml.getElementsByTagName('GetMap');
			if(GetMap.length){
				var aFormats = GetMap[0].getElementsByTagName('Format');
				var oFormats = [];
				for(i=0;i<aFormats.length;i++){				
						//var format =(aFormats[i].textContent)? aFormats[i].textContent:aFormats[i].text;
						if(aFormats[i])var format = OpenLayers.Ajax.getText(aFormats[i]);
						//if(format == 'image/png' || format == 'image/jpeg' || format == 'image/gif')oFormats.push(format);
						if(format == 'image/png' || format == 'image/gif')oFormats.push(format);
				}
				
				this.WMSManager.aImageFormats = oFormats;
				
			}
			
			
			//Check resolutionsValue for TileCache
			var resolutions = xml.getElementsByTagName('Resolutions');
			if(resolutions.length){
				//var resolutionsS = resolutions[0].textContent;
				var resolutionsS = OpenLayers.Ajax.getText(resolutions[0]);
				if(resolutionsS.length){
					var aResolutions = resolutionsS.split(' ');
					//this.WMSManager.resolutionsValue = resolutionsS.replace(" ",",");
					this.WMSManager.resolutionsValue = aResolutions;
				} 
				
			}
		 	
			 //Print Layer List
			 var aLayer = xml.getElementsByTagName('Layer');
			 if(aLayer.length>0){
				 //this.WMSManager.drawLayersForm(xml);
				 this.WMSManager.drawLayersForm(xml);
			 } 
			 
	 } else {
		alert('connection error: response is not an object');
	 }
		
    },
	
	
	
	
	
	
    /** 
     * @private
     *
     * @param {Event} evt
     */
    searchCatalogue: function(obj) {
		var str = obj.WMSManager.catalogueInput.value;
		console.log(str);
         obj.WMSManager.catalogueLog.innerHTML ='';
		 
		 var url ="catalogue/fake_catalogue_response.xml";
		 
		 if(url.length>0) {
			 this.catalogueURL = url;
		 } else {
			 alert('no server selected. please select one.');
			 return;
		 } 
	    if (this.catalogueURL.indexOf('?') == -1)
	    {
	        this.catalogueURL = this.baseURL + '?';
	    }
	    else
	    {
	        if (this.catalogueURL.charAt( this.catalogueURL.length - 1 ) == '&')
	            this.catalogueURL = this.catalogueURL.slice( 0, -1 );
	    }
		/*
	    this.catalogueURL = this.catalogueURL+ "&service=WMS";
	    this.catalogueURL = this.catalogueURL +  "&version=1.1.1";
	    this.catalogueURL = this.catalogueURL + "&request=GetCapabilities";
	    */
		
		obj.WMSManager.catalogueLog.innerHTML = "Loading Layers list... please wait...";
        //$('wmsManagerOutput').innerHTML = this.baseURL;
		//alert(this.baseURL);
        //OpenLayers.loadURL("proxy.do?urlValor="+url, null, obj, obj.WMSManager.parseCatalogueResponse);
        
        

    },
	
	/** 
     * @private 
     *
     * @param {XMLHttpRequest} ajaxRequest
     */
    parseCatalogueResponse: function(ajaxRequest) {
		var text = ajaxRequest.responseText;
		
		//var oDomDoc = Sarissa.getDomDocument();
	    
		//myregexp = new RegExp('<!--[^(-->)]*-->');
		text = text.replace(/<!--.*?-->/g, ''); //Helped with ESA server
		text = text.replace(/\[.<!.*?>.\]/g, ''); //Helped with ESA server
		text = text.replace(/<GetTileService>.*?GetTileService>/g, '');//skip NASA DTD error
		
		var xml = OpenLayers.parseXMLString(text);
		console.log(xml);
		if(xml == null) return alert('incorrect content: check your Catalogue url');
		if(xml.childNodes.length==0) {
			try{
				if(OpenLayers.Ajax.getParseErrorText(xml) == OpenLayers.Ajax.PARSED_OK){
			      // The document was parsed/loaded just fine, go on
			      //alert(xml);
				  alert('no Layers available on this server');
			    } 
			    else{
			      	// The document was not loaded correctly! Inform the user:
					var error = OpenLayers.Ajax.getParseErrorText(xml);
			      	//alert(error);
				  	var newwindow;
					//Encode the xml inside the error message
					//encodedHtml = error;
			    	//encodedHtml = encodedHtml.replace(/</g,"&lt;");
			    	//encodedHtml = encodedHtml.replace(/>/g,"&gt;"); 
					//popup a message windows to let the use copy the error
					newwindow=window.open('','','menubar=1,scrollbars=1,width=400,height=300,resizable=1') ;
					//menubar=1,location=1,status=1,
					newwindow.document.writeln('<html> <head> <title>Error Parsing GetCapabilties<\/title> <\/head> <body><pre><code>');
					newwindow.document.writeln(OpenLayers.Ajax.escape(error));
					newwindow.document.writeln('<\/code><\/pre> <\/body> <\/html>');
					newwindow.document.close();
			    };
				//return alert('the browser is not able to parse this GetCapabilities. Try with Firefox  ;-) ');
				

			} catch(e){
				alert(e.description);
			}
		}
		//console.log('vicino');
		if(typeof xml=='object'){
			
			//console.dirxml(xml);
			//console.log('dentro');
			 //Print Layer List
			 var aLayer = xml.getElementsByTagName('Layer');
			 if(aLayer.length>0){
				 //this.WMSManager.drawLayersForm(xml);
				 this.WMSManager.drawCatalogueForm(xml);
				 //console.log('dentro layers');
			 } 
			 
	 } else {
		alert('connection error: response is not an object');
	 }
		
    },
	
	/** 
     * @private 
     *
     * @param {XMLHttpRequest} ajaxRequest
     */
    drawLayersForm: function(xml) {
		
		// DOM OBJECTS
		//this.olServerList = document.getElementById("servidores");
		//var formObj = this.olServerList;
		
		
		
		//div to show layer info
		var infoDiv = document.createElement('div');
		infoDiv.className = 'wmsLayerListOutput';
		
		infoDiv.innerHTML = '<p><i>Click en la capa para obtener más detalles</i></p>';
		
		//alert('1');
		//get Extent (to implent BoundingBox alternative) 
		//this is a generical catch for a bbox the refining comes when layers are parsed
		var bbox = xml.getElementsByTagName('LatLonBoundingBox')[0];
		if(!bbox) bbox = xml.getElementsByTagName('BoundingBox')[0];
		this.extent = [bbox.getAttribute('minx'),bbox.getAttribute('miny'),bbox.getAttribute('maxx'),bbox.getAttribute('maxy')];
		this.mapCenter = [((bbox.getAttribute('maxx') - bbox.getAttribute('minx'))/2),((bbox.getAttribute('maxy') - bbox.getAttribute('miny'))/2)];
		
		
		//getCapabilities
		var Capability = xml.getElementsByTagName('Capability')[0];
		var layerCont = this.findChildByName(Capability,'Layer');
		
		
		
		var aLayers = this.findChildrenByName(layerCont,'Layer');
		//var aLayers = layerCont.childNodes;
		
		//try to look for nested layers
		//ex: http://www.andaluciajunta.es/IDEAndalucia/IDEAwms/wms/MTA100v?
		var aLayersTemp = [];
		
		//alert(aLayers.length);
		for(i=0;i<aLayers.length;i++){
			try{
		
			//alert('i: '+i);
			aLayersTemp.push(aLayers[i]);
			var n = aLayers[i];
			var nameNode = this.findChildByName(n,'Name');
			//if(nameNode)var Name =(nameNode.textContent)? nameNode.textContent:nameNode.text;
			//else var Name = null;
			if(nameNode)var Name =OpenLayers.Ajax.getText(nameNode);
			else var Name = null;
			
			var titleNode = this.findChildByName(n,'Title');
			//if(titleNode)var Title =(titleNode.textContent)? titleNode.textContent:titleNode.text;
			//else var Title = null;
			if(titleNode)var Title = OpenLayers.Ajax.getText(titleNode);
			else var Title = null;
			
			
			var x  = n.firstChild;
			} catch(e){alert(e)}
			
		    do {
		    	if(x.nodeType==1 && x.nodeName=='Layer'){
					
					var nameNodeN = this.findChildByName(n,'Name');
					var titleNodeN = this.findChildByName(n,'Title');
					//alert(nameNodeN);
					if(!nameNodeN && titleNodeN) aLayersTemp.push(x);
				} 
				 
				x=x.nextSibling;
				
		    }while (x!=n.lastChild)
			
			
		}
		
		
		
		//aLayers.concat(aLayersTemp);
		aLayers = aLayersTemp;
		//alert(aLayers.length);
		
		
		var myselect = $('wmsLayerList');
		if( myselect)myselect.parentNode.removeChild(myselect);
		var select = document.createElement('select');
		select.name = 'wmsLayerList';
		//select.onchange = this.addWMSLayer;
		select.id = 'wmsLayerList';
		select.WMSManager = this;
		select.style.width = "200px";
		//select.multiple = 'false';//antes true
		select.size = 5;
		var j = 0;
		//var opt = new Option( 'select a Layer', '', true, true );
		//select[j++] = opt;
		for(i=0;i<aLayers.length;i++){
			try{
			var nameNode = this.findChildByName(aLayers[i],'Name');
			//if(nameNode)var Name =(nameNode.textContent)? nameNode.textContent:nameNode.text;
			//else var Name = null;
			if(nameNode)var Name =OpenLayers.Ajax.getText(nameNode);
			else var Name = null;
			//alert(nameNode.data);
			
			
			var titleNode = this.findChildByName(aLayers[i],'Title');
			//if(titleNode)var Title =(titleNode.textContent)? titleNode.textContent:titleNode.text;
			//else var Title = null;
			if(titleNode)var Title =OpenLayers.Ajax.getText(titleNode);
			else var Title = null;
			
			
			var abstractNode =  this.findChildByName(aLayers[i],'Abstract');
			//if(abstractNode) var Abstract =(abstractNode.textContent)? abstractNode.textContent:abstractNode.text;
			//else var Abstract = null;
			if(abstractNode)var Abstract =OpenLayers.Ajax.getText(abstractNode);
			else var Abstract = null;
			
			
			var queryable = aLayers[i].getAttribute('queryable');
			
			//this.aLayersQueryable[i] = queryable;
			var qTitle = Title + ' (q)';
			qTitle =(queryable==1)?qTitle :Title;
			select[j++] = new Option(qTitle,Name,false,false);
			select.options[i].Title = Title;
						
			//Legend sta dentro Style
				var LegendURL = null;
				
				var legendNode = aLayers[i].getElementsByTagName('LegendURL')[0];
				if(legendNode) {
					var OnlineResourceNode = this.findChildByName(legendNode,'OnlineResource');
					if(OnlineResourceNode){
						 LegendURL = OnlineResourceNode.getAttribute('xlink:href');
						//alert(LegendURL);
					}
					
				}
				
					
			//this should be changed with an <optgroup label="Title">
			if(!Name && Title) {
				select.options[i].disabled = true;
			} else {
				select.options[i].Name = Name;
				select.options[i].Abstract = Abstract;
				select.options[i].queryable = queryable;
				select.options[i].LegendURL = LegendURL;
				select.options[i].infoDiv = infoDiv;
				select.onchange = 	function(){
										var option = this[this.selectedIndex];
										var outObj = option.infoDiv;
										var queryable = (option.queryable>0)?'true -> (q)':'false';
										var mess = '<p>Detalles de la capa: </p><p><b>Nombre:</b><br>'+option.Name +'<br><br><b>Título:</b><br>'+option.Title; 
														//+'<br><br><b>Abstract:</b>';//<br>'+option.Abstract+'</p>';//<br><br><b>Queryable:</b><br>'+queryable;
										outObj.innerHTML = mess;
										
										//if(option.LegendURL)outObj.innerHTML += '<br><b>Legend Image:</b><br><img src="'+option.LegendURL+'" alt="Leyenda">';
//										if(option.LegendURL)
//										{
										
										
											//var src = "proxy.do?getImage=image&urlFinal="+option.LegendURL;
											
											/*outObj.innerHTML += '<br><b>Leyenda:</b><br><img src="'+src+'" alt="Leyenda">';
											outObj.style.display="none";
											outObj.style.display="block";
											
											pic1= new Image(100,25); 
  											pic1.src=src; 
  											
  											outObj.appendChild(pic1);*/
  											
  											
  											
  											
//											myUrl ="proxy.do?getImage=image&urlValor="+escape(option.LegendURL);
//											newimg = new Image();
//											newimg.src = myUrl;
											outObj.appendChild(newimg);
//											
											
//										}
//										else outObj.innerHTML += '<br><br><b>Leyenda:</b><br>(no disponible)';
//										outObj.innerHTML +="</p>";
									};
			}
			
			
			
			
			var bboxNode = this.findChildByName(aLayers[i],'BoundingBox');
			var latbboxNode = this.findChildByName(aLayers[i],'LatLonBoundingBox');
			
			if (latbboxNode){
				 var bbox = latbboxNode;
				 var BoundingBox = [bbox.getAttribute('minx'),bbox.getAttribute('miny'),bbox.getAttribute('maxx'),bbox.getAttribute('maxy')];
			} else if(bboxNode){
				var bbox = bboxNode;
				var BoundingBox = [bbox.getAttribute('minx'),bbox.getAttribute('miny'),bbox.getAttribute('maxx'),bbox.getAttribute('maxy')];
			} else if(this.extent){
				var BoundingBox = this.extent;
			}
			else  return alert('no bbox defined, check getCapabilities configuration');
			
			
			
			this.aWMSLayers.push([Name,Title,Abstract,BoundingBox,LegendURL,queryable]);
			}catch(e){alert(e);}
			
		}
		//formObj.appendChild(select);
		//this.layerStuff.appendChild(select);
		var textoType = document.createElement('p');
		textoType.innerHTML  ="Formato: &nbsp;";
		document.getElementById("wmsManagerOutput").innerHTML="<p style='padding-top:15px;'><strong>Seleccione las capas:</strong></p>";
		document.getElementById("layerStuff").appendChild(select);
		//alert('5');
		/*this.overlayButton = document.createElement('input');
		this.overlayButton.name = 'WMSradioB';
		this.overlayButton.className = 'wmsLayerButton';
		this.overlayButton.type = 'checkbox';
		this.overlayButton.value = 'overlay';
		OpenLayers.Event.observe(this.overlayButton, "mouseup",  this.onOverlayClick.bindAsEventListener(this.overlayButton));
		
		var radioSoverlay = document.createElement('span');
		radioSoverlay.innerHTML = 'overlay';
		radioSoverlay.id = 'wmsLayerButtonSpan';
		radioSoverlay.style.width = '5em';
		radioSoverlay.style.border = '1px solid black';
		radioSoverlay.button = this.overlayButton;
		radioSoverlay.WMSManager = this;
		
		OpenLayers.Event.observe(radioSoverlay, "mouseup",  this.onOverlayClick.bindAsEventListener(this.overlayButton));
		*/
		var br = document.createElement('p');
		br.innerHTML = "&nbsp;";
		var button = document.createElement('input');
		button.onclick = this.addWMSLayer;
		button.WMSManager = this;
		button.value = 'Añadir Capa';
		button.type = 'button';
		var espacio = document.createElement('span');
		espacio.innerHTML = "&nbsp;&nbsp;&nbsp;";
		var layerBox = document.createElement('div');
		layerBox.id = 'wmsLayerBox';
			
		//Image type chooser
		this.selectImageType = document.createElement('select');
		this.selectImageType.className = 'wmsLayerSelectImageType';
		//alert(this.aImageFormats.length);
		
		for(i=0;i<this.aImageFormats.length;i++){
				var format = this.aImageFormats[i];
				this.selectImageType[i]= new Option(format, format, false, false );
		
		}
		
	
		layerBox.appendChild(textoType);
		layerBox.appendChild(this.selectImageType);	
		layerBox.appendChild(espacio);
		
		layerBox.appendChild(button);
		layerBox.appendChild(infoDiv);	
		
		//this.layerStuff.appendChild(layerBox);
		document.getElementById("layerStuff").appendChild(layerBox);
		//formObj.appendChild();
		

		
		//$('wmsManagerOutput').innerHTML = '';
    },
	
	
	
	/** 
     * @private 
     *
     * @param {XMLHttpRequest} ajaxRequest
     */
     
    drawCatalogueForm: function(xml) {
		
		if(!this.catalogueDivForm ){
			this.catalogueDivForm = document.createElement('div');
			this.catalogueDivForm.className = 'catalogueListOutput';
			
		} else {
			this.catalogueDivForm.innerHTML = '';
		}
		
		
		
		//div to show layer info
		var infoDiv = document.createElement('div');
		infoDiv.className = 'catalogueInfoDiv';
		
		infoDiv.innerHTML = 'Click on a layer to have details';
		this.catalogueDivForm.appendChild(infoDiv);
		this.catalogueLog.innerHTML = '';
		
		
		var aLayers = xml.getElementsByTagName('Layer');
		//var aLayers = layerCont.childNodes;
		
		console.log(aLayers);
		//try to look for nested layers
		//ex: http://www.andaluciajunta.es/IDEAndalucia/IDEAwms/wms/MTA100v?
		var aLayersTemp = [];
		//alert(aLayers.length);
		for(i=0;i<aLayers.length;i++){
			//try{
		
			//alert('i: '+i);
			aLayersTemp.push(aLayers[i]);
			
			/*
			 var n = aLayers[i];
			var nameNode = this.findChildByName(n,'Name');
			//if(nameNode)var Name =(nameNode.textContent)? nameNode.textContent:nameNode.text;
			//else var Name = null;
			if(nameNode)var Name =OpenLayers.Ajax.getText(nameNode);
			else var Name = null;
			
			var titleNode = this.findChildByName(n,'Title');
			//if(titleNode)var Title =(titleNode.textContent)? titleNode.textContent:titleNode.text;
			//else var Title = null;
			if(titleNode)var Title = OpenLayers.Ajax.getText(titleNode);
			else var Title = null;
			
			 */
			
			/*var x  = n.firstChild;
			
			} catch(e){alert(e)}
			
		    do {
		    	if(x.nodeType==1 && x.nodeName=='Layer'){
					
					var nameNodeN = this.findChildByName(n,'Name');
					var titleNodeN = this.findChildByName(n,'Title');
					//alert(nameNodeN);
					if(!nameNodeN && titleNodeN) aLayersTemp.push(x);
				} 
				 
				x=x.nextSibling;
				
		    }while (x!=n.lastChild)
			*/
			
		}
		
		
		
		//aLayers.concat(aLayersTemp);
		aLayers = aLayersTemp;
		//alert(aLayers.length);
		
		
		var myselect = $('wmsLayerList');
		if( myselect)myselect.parentNode.removeChild(myselect);
		var select = document.createElement('select');
		select.name = 'wmsLayerList';
		//select.onchange = this.addWMSLayer;
		select.id = 'wmsLayerList';
		select.WMSManager = this;
		//select.style.width = "220px";
		//select.multiple = 'true';
		//select.size = 5;
		var j = 0;
		var opt = new Option( 'select a Layer', '', true, true );
		select[j++] = opt;
		for(i=1;i<=aLayers.length;i++){
			try{
			var layer= aLayers[i-1];
			
			var Name = layer.getAttribute('name');
			console.log(Name);
			//console.dirxml(aLayers[i]);
			
			
			var Title = layer.getAttribute('title');
			
			var HREF = layer.getAttribute('href');
			var Type = layer.getAttribute('type');
			
			var Abstract = layer.getAttribute('abstract');
			
			
			var queryable = layer.getAttribute('queryable');
			
			var qTitle = Title + ' (q)';
			qTitle =(queryable==1)?qTitle :Title;
			select[j++] = new Option(qTitle,Name,false,false);
			select.options[i].Title = Title;
						
			//Legend sta dentro Style
				var LegendURL = null;
				
				var legendNode = layer.getElementsByTagName('LegendURL')[0];
				if(legendNode) {
					var OnlineResourceNode = this.findChildByName(legendNode,'OnlineResource');
					if(OnlineResourceNode){
						 LegendURL = OnlineResourceNode.getAttribute('xlink:href');
						//alert(LegendURL);
					}
					
				}
				
			
			select.options[0].infoDiv = infoDiv;
			//this should be changed with an <optgroup label="Title">
			if(!Name && Title) {
				select.options[i].disabled = true;
			} else {
				select.options[i].Name = Name;
				select.options[i].Abstract = Abstract;
				select.options[i].queryable = queryable;
				select.options[i].LegendURL = LegendURL;
				select.options[i].HREF = HREF;
				select.options[i].Type = Type;
				select.options[i].infoDiv = infoDiv;
				select.onchange = 	function(){
										var option = this[this.selectedIndex];
										var outObj = option.infoDiv;
										var queryable = (option.queryable>0)?'true -> (q)':'false';
										if(option.Name){
										var mess = '<b>Layer Name:</b><br>'+option.Name +'<br><br><b>Layer Title:</b><br>'+option.Title 
														+'<br><br><b>Abstract:</b><br>'+option.Abstract
														+'<br><br><b>HREF:</b><br>'+option.HREF
														+'<br><br><b>Type:</b><br>'+option.Type
														+'<br><br><b>Queryable:</b><br>'+queryable;
												if(option.LegendURL)mess += '<br><br><b>Legend Image:</b><br><img src="'+option.LegendURL+'">';
												else mess += '<br><br><b>Legend Image:</b><br>none';
										} else {
											var mess = 'choose a valid layer from the list'
										}
										outObj.innerHTML = mess;
										
										
										
									};
			}
			
			
			
			
			/*var bboxNode = this.findChildByName(layer,'BoundingBox');
			var latbboxNode = this.findChildByName(layer,'LatLonBoundingBox');
			
			if (latbboxNode){
				 var bbox = latbboxNode;
				 var BoundingBox = [bbox.getAttribute('minx'),bbox.getAttribute('miny'),bbox.getAttribute('maxx'),bbox.getAttribute('maxy')];
			} else if(bboxNode){
				var bbox = bboxNode;
				var BoundingBox = [bbox.getAttribute('minx'),bbox.getAttribute('miny'),bbox.getAttribute('maxx'),bbox.getAttribute('maxy')];
			} else if(this.extent){
				var BoundingBox = this.extent;
			}
			else  return alert('no bbox defined, check getCapabilities configuration');
			*/
			BoundingBox = [-180,-90,180,90];
			
			//this.aWMSLayers.push([Name,Title,Abstract,BoundingBox,LegendURL,queryable]);
			this.aWMSLayers.push([Name,Title,Abstract,BoundingBox,HREF,queryable]);
			}catch(e){alert(e);}
			
		}
		//formObj.appendChild(select);
		this.catalogueSelect = select;
		this.catalogueDivForm.appendChild(select);
		
		//alert('5');
		/*this.overlayButton = document.createElement('input');
		this.overlayButton.name = 'WMSradioB';
		this.overlayButton.className = 'wmsLayerButton';
		this.overlayButton.type = 'checkbox';
		this.overlayButton.value = 'overlay';
		OpenLayers.Event.observe(this.overlayButton, "mouseup",  this.onOverlayClick.bindAsEventListener(this.overlayButton));
		
		var radioSoverlay = document.createElement('span');
		radioSoverlay.innerHTML = 'overlay';
		radioSoverlay.id = 'wmsLayerButtonSpan';
		radioSoverlay.style.width = '5em';
		radioSoverlay.style.border = '1px solid black';
		radioSoverlay.button = this.overlayButton;
		radioSoverlay.WMSManager = this;
		
		OpenLayers.Event.observe(radioSoverlay, "mouseup",  this.onOverlayClick.bindAsEventListener(this.overlayButton));
		*/
		var br = document.createElement('br');
		
		var button = document.createElement('input');
		button.onclick = this.addCatalogueLayer;
		button.WMSManager = this;
		button.value = 'add layer';
		button.type = 'button';
		
		
		var layerBox = document.createElement('div');
		layerBox.id = 'wmsLayerBox';
		
		//Buttons to switch layers up and down
		/*var aSwitchUp = document.createElement("a");
				aSwitchUp.innerHTML = '<span>&uarr;</span>';
				aSwitchUp.className = 'olWMSLayerSwitch';
				aSwitchUp.select = select;
				aSwitchUp.onclick = function(){
									var obj =  this.select;
									//alert(obj[obj.selectedIndex].value);
									var sel = new Array();
									for (var i=0; i<obj.length; i++) {
										if (obj[i].selected == true) {
											sel[sel.length] = i;
										}
									}
									for (i in sel) {
										if (sel[i] != 0 && !obj[sel[i]-1].selected) {
											var tmp = new Array((document.body.innerHTML ? obj[sel[i]-1].innerHTML : obj[sel[i]-1].text), obj[sel[i]-1].value, obj[sel[i]-1].style.color, obj[sel[i]-1].style.backgroundColor, obj[sel[i]-1].className, obj[sel[i]-1].id);
											if (document.body.innerHTML) obj[sel[i]-1].innerHTML = obj[sel[i]].innerHTML;
											else obj[sel[i]-1].text = obj[sel[i]].text;
											obj[sel[i]-1].value = obj[sel[i]].value;
											obj[sel[i]-1].style.color = obj[sel[i]].style.color;
											obj[sel[i]-1].style.backgroundColor = obj[sel[i]].style.backgroundColor;
											obj[sel[i]-1].className = obj[sel[i]].className;
											obj[sel[i]-1].id = obj[sel[i]].id;
											if (document.body.innerHTML) obj[sel[i]].innerHTML = tmp[0];
											else obj[sel[i]].text = tmp[0];
											obj[sel[i]].value = tmp[1];
											obj[sel[i]].style.color = tmp[2];
											obj[sel[i]].style.backgroundColor = tmp[3];
											obj[sel[i]].className = tmp[4];
											obj[sel[i]].id = tmp[5];
											obj[sel[i]-1].selected = true;
											obj[sel[i]].selected = false;
										}
									}
								};
		var aSwitchDown = document.createElement("a");
				aSwitchDown.innerHTML = '<span>&darr;</span>';
				aSwitchDown.className = 'olWMSLayerSwitch';
				aSwitchDown.select = select;
				aSwitchDown.onclick = function(){
									var obj =  this.select;
									//alert(obj[obj.selectedIndex].value);
									var sel = [];
									for (var i=obj.length-1; i>-1; i--) {
										if (obj[i].selected == true) {
											sel[sel.length] = i;
										}
									}
									for (i in sel) {
										if (sel[i] != obj.length-1 && !obj[sel[i]+1].selected) {
											var tmp = new Array((document.body.innerHTML ? obj[sel[i]+1].innerHTML : obj[sel[i]+1].text), obj[sel[i]+1].value, obj[sel[i]+1].style.color, obj[sel[i]+1].style.backgroundColor, obj[sel[i]+1].className, obj[sel[i]+1].id);
											if (document.body.innerHTML) obj[sel[i]+1].innerHTML = obj[sel[i]].innerHTML;
											else obj[sel[i]+1].text = obj[sel[i]].text;
											obj[sel[i]+1].value = obj[sel[i]].value;
											obj[sel[i]+1].style.color = obj[sel[i]].style.color;
											obj[sel[i]+1].style.backgroundColor = obj[sel[i]].style.backgroundColor;
											obj[sel[i]+1].className = obj[sel[i]].className;
											obj[sel[i]+1].id = obj[sel[i]].id;
											if (document.body.innerHTML) obj[sel[i]].innerHTML = tmp[0];
											else obj[sel[i]].text = tmp[0];
											obj[sel[i]].value = tmp[1];
											obj[sel[i]].style.color = tmp[2];
											obj[sel[i]].style.backgroundColor = tmp[3];
											obj[sel[i]].className = tmp[4];
											obj[sel[i]].id = tmp[5];
											obj[sel[i]+1].selected = true;
											obj[sel[i]].selected = false;
										}
									}
								};
								*/
		//Image type chooser
		this.selectImageType = document.createElement('select');
		this.selectImageType.className = 'wmsLayerSelectImageType';
		//alert(this.aImageFormats.length);
		
		
		//THIS IS BAD -> need request to getCapabilities to check.
		this.aImageFormats = ['image/png','img/gif'];
		
		for(i=0;i<this.aImageFormats.length;i++){
			var format = this.aImageFormats[i];
			this.selectImageType[i]= new Option(format, format, false, false );
		}
		
		//mount interface
		//this.catalogueDivForm.appendChild(aSwitchUp);
		//this.catalogueDivForm.appendChild(aSwitchDown);
		/*this.catalogueDivForm.appendChild(radioSoverlay);
		this.catalogueDivForm.appendChild(this.overlayButton);*/
		
		this.catalogueDivForm.appendChild(this.selectImageType);
		//this.catalogueDivForm.appendChild(br);	
		this.catalogueDivForm.appendChild(button);
		this.catalogueDivForm.appendChild(infoDiv);
		//this.catalogueDivForm.appendChild(br);	
		
		//this.layerStuff.appendChild(layerBox);
		//formObj.appendChild();
		
		this.olCatalogue.appendChild(this.catalogueDivForm);
		
		$('wmsManagerOutput').innerHTML = '';
    },
	
	
	
	/** 
     * @private
     *
     */
    findChildByName: function(n,name) {
		if(!n)return false;
		var x = n.firstChild;
		while (x)
	    {
	    	if(x.nodeName==name) return x;
			else if(x==n.lastChild)return null;
			else x=x.nextSibling;
	    }
	},
	
	/** 
     * @private
     *
     */
    findChildrenByName: function(n,name) {
		if(!n)return false;
		var nodes=n.childNodes;
		var aNode = [];
		for (i=0;i<nodes.length;i++) {
			var x=n.childNodes[i];
			if(x.nodeName==name) aNode.push(x) ;
		}
		
		return aNode;
	},
	
	/** 
     * @private
     *
     */
    addWMSLayer: function() {
        var select1 = $('wmsServerList');
		var select2 = $('wmsLayerList');

		 /*if(select1) var url = select1[select1.selectedIndex].value;
		 else url ="";*/
		 
		 var url =document.forms.serverform.urlserverhidden.value;
		 var layerA = [];
		 var titleA = [];
		 var queryableA = [];
		 for(i = 0; i < select2.options.length; i++) {
  			 if(select2.options[i].selected) {
  			 	//alert(this.aLayersQueryable[i]);
				layerA.push(select2.options[i].value);
				if(select2.options[i].textContent)titleA.push(select2.options[i].textContent);
				else titleA.push(select2.options[i].text);
				
				queryableA.push(this.WMSManager.aWMSLayers[i][5]);//el parámetro con indice 5 indica si la capa tiene info
				if((select2.options[i].LegendURL!=null)&&(select2.options[i].LegendURL!="")&&(select2.options[i].LegendURL!="none")&&(select2.options[i].LegendURL!="null"))
				{
					var div=document.createElement("div");
											div.id="legend_"+select2.options[i].Name;
											var p = document.createElement("p");
											var span2 = document.createElement("span");
											
											span2.innerHTML = "<strong>Capa " + select2.options[i].text+":</strong>";
											
											var pimg = document.createElement("p");
											var img = document.createElement("img");
											img.src = "proxy.do?getImage=image&urlValor="+escape(select2.options[i].LegendURL);
											
											p.appendChild(span2);
											pimg.appendChild(img);
											div.appendChild(p);
											div.appendChild(pimg);
											
											
											
//											document.getElementById("leyendaimg").appendChild(div);
				}
			}
		}
		 if(layerA.length) var layerName = layerA.join(',');
		 else layerName ="";
		 if(titleA.length) var title = titleA.join(', ');
		 else title = layer;
		 
		 if(queryableA.length) var queryableStr = queryableA.join(', ');
		 
		 if(url.length<1) {
			 alert('no server selected. please select one.');
			 return;
		 }
		 if(layerName.length<1) {
			 alert('no layer selected. please select one.');
			 return;
		 }
		  
	    if (url.indexOf('?') == -1)
	    {
	        url = url + '?';
	    }
	    else
	    {
	        if (url.charAt( url.length - 1 ) == '&')
	            url = url.slice( 0, -1 );
	    }
		
		
		//create layer add with OL
		/*
		 * need to check if PNG is supported by the server
		 */
		
		
		//get selected layers features
		var aExtents = [];
		var aLegendUrl = [];
		
		var aWMSLayers = [];
		for(i=0;i<layerA.length;i++){
			
			for(j=0;j<this.WMSManager.aWMSLayers.length;j++){
				var checkedLayer = layerA[i];
				var listedLayer = this.WMSManager.aWMSLayers[j];
				if(checkedLayer==listedLayer[0]) {
					aExtents.push(listedLayer[3]); 
					aLegendUrl.push(listedLayer[4]);
					aWMSLayers.push(listedLayer);
				}
			}
		}
		
//		alert(aLegendUrl);
		
		//set base or overlay
		//var overlayButton = this.WMSManager.overlayButton;
		
		
		//check for base layer
		var layers = this.WMSManager.map.layers.slice();
		var hasBaseLayer = false;
		for(i=0;i<layers.length;i++){
			layer = layers[i];
			
			if(layer.isBaseLayer) hasBaseLayer = true;
		}
		//if(overlayButton.checked) if(!hasBaseLayer)return alert('you need a base layer before setting an overlay') ;
		
		//manage exceptions
		if(this.WMSManager.exceptionsValue)
			var exception = this.WMSManager.exceptionsValue;
		 
		 
		 if(this.WMSManager.resolutionsValue){
		 		var aResolutions = this.WMSManager.resolutionsValue;
				var minRes = aResolutions[aResolutions.length -1];
		 		var options = {resolutions: aResolutions, maxresolution: aResolutions[0], minResolution: minRes};
		 		this.WMSManager.map.setOptions(options) ;
			}
			
		//SRS - OL default srs is EPSG:4326 
		/*var options = {srs: 'EPSG:4326'};
				this.WMSManager.map.setOptions(options) ;	
		*/
		
		//IMAGE TYPE
		var imageType = this.WMSManager.selectImageType[this.WMSManager.selectImageType.selectedIndex].value;
		
		
		/*if(overlayButton.checked){*/
			if(imageType=='image/jpeg') return alert('you cannot select JPEG format for overlays, please choose another format');
			var layer_wms_preName = '<img src="'+imgPath+'/layerWMS.gif"/>&nbsp;';
			var layer = new OpenLayers.Layer.WMS( title, 
                    url, {layers: layerName,format: imageType,TRANSPARENT: "true", EXCEPTIONS: 'application/vnd.ogc.se_blank'},{preName: layer_wms_preName});
			layer.isBaseLayer = false; 
			layer.aWMSLayers = aWMSLayers;
//			registerEvents(layer);
			
			
			var params = '';
			for (var i in layer.params) {
		        if(i){
					//alert(params[i]);
					params += '!!'+ i + '!'+layer.params[i];
				}
		    }
			layer.queryable=true;
			layer.WMSinfo = [layerName,url,params,aWMSLayers];//needed by Permalink/ArgParser

			map.addLayer(layer);
			
			layer.setOpacity(1);
			layer.setZIndexAddLayer(0);
			
			map.markersAuxiliarLayer.clearMarkers();
//			loadLayerCtrl.trigger();
		
    },
	
	
	
	

	
	/** 
     * @private
     *
     */
    switchPanel: function() {
        if(this.LayerManager)var LayerManager = this.LayerManager;
		else var LayerManager = this;
		
		var header = LayerManager.olLayerManagerHeader;
		if(this.panel)LayerManager.activePanel = this.panel;
		var aHref = header.getElementsByTagName('a');
		var activePanel = LayerManager.activePanel;
		
		for(var i=0;i<aHref.length;i++){
			//alert(aHref[i].innerHTML);
			if(aHref[i].panel == activePanel)
					aHref[i].className = 'active';
			else 
					aHref[i].className = '';
		}
		//if(this.panel) this.panel = 'olLegend';
		//LayerManager.olServerList.style.display = (activePanel==this.panel) ? "" : "none";
		LayerManager.olServerList.style.display = (activePanel=='olServerList') ? "" : "none";
        LayerManager.olLegend.style.display = (activePanel=='olLegend') ? "" : "none";
		LayerManager.olCatalogue.style.display = (activePanel=='olCatalogue') ? "" : "none";
		//alert(LayerManager.dataLayersDiv.innerHTML);
		//olCatalogueList
    },
	
    /** 
     * @private
     *
     * @param {Event} evt
     */
    ignoreEvent: function(evt) {
        OpenLayers.Event.stop(evt);
    },

    /** Register a local 'mouseDown' flag so that we'll know whether or not
     *   to ignore a mouseUp event
     * 
     * @private
     *
     * @param {Event} evt
     */
    mouseDown: function(evt) {
        this.mouseDown = true;
        this.ignoreEvent(evt);
    },

    /** If the 'mouseDown' flag has been set, that means that the drag was 
     *   started from within the LayerSwitcher control, and thus we can 
     *   ignore the mouseup. Otherwise, let the Event continue.
     *  
     * @private
     *
     * @param {Event} evt
     */
    mouseUp: function(evt) {
        if (this.mouseDown) {
            this.mouseDown = false;
            this.ignoreEvent(evt);
        }
    },
	
	

    /** @final @type String */
    CLASS_NAME: "OpenLayers.Control.WMSManager"
});


 /** Sarissa derived getText
     *  
     * @private
     *
     * @param {Node} XML node
     * @param {int} deep recursion  
     */
	OpenLayers.Ajax.getText = function(oNode, deep){
	    var s = "";
	    var nodes = oNode.childNodes;
	    for(var i=0; i < nodes.length; i++){
	        var node = nodes[i];
	        var nodeType = node.nodeType;
	        if(nodeType == Node.TEXT_NODE || nodeType == Node.CDATA_SECTION_NODE){
	            s += node.data;
	        } else if(deep == true
	                    && (nodeType == Node.ELEMENT_NODE
	                        || nodeType == Node.DOCUMENT_NODE
	                        || nodeType == Node.DOCUMENT_FRAGMENT_NODE)){
	            s += OpenLayers.Ajax.getText.getText(node, true);
	        };
	    };
	    return s;
	};
	
	/** Sarissa derived getParseErrorText
     *  
     * @private
     *
     * @param {Document} oDoc The target DOM document
     * @returns The parsing error description of the target Document in
     *          human readable form (preformated text)
     */
	OpenLayers.Ajax.PARSED_OK = "Document contains no parsing errors";
	OpenLayers.Ajax.PARSED_EMPTY = "Document is empty";
	OpenLayers.Ajax.PARSED_UNKNOWN_ERROR = "Not well-formed or other error";
	
	OpenLayers.Ajax.getParseErrorText = function(oDoc){
		//this is only the IE version from Sarissa	
	   var parseErrorText = OpenLayers.Ajax.PARSED_OK;
        if(oDoc && oDoc.parseError && oDoc.parseError.errorCode && oDoc.parseError.errorCode != 0){
            parseErrorText = "XML Parsing Error: " + oDoc.parseError.reason + 
                "\nLocation: " + oDoc.parseError.url + 
                "\nLine Number " + oDoc.parseError.line + ", Column " + 
                oDoc.parseError.linepos + 
                ":\n" + oDoc.parseError.srcText +
                "\n";
            for(var i = 0;  i < oDoc.parseError.linepos;i++){
                parseErrorText += "-";
            };
            parseErrorText +=  "^\n";
        }
        else if(oDoc.documentElement == null){
            parseErrorText = OpenLayers.Ajax.PARSED_EMPTY;
        };
        return parseErrorText;
	};
	
	/** 
	 * Sarissa derived Escape
	 * Escape the given string chacters that correspond to the five predefined XML entities
	 * @param sXml the string to escape
	 */
	OpenLayers.Ajax.escape = function(sXml){
	    return sXml.replace(/&/g, "&amp;")
	        .replace(/</g, "&lt;")
	        .replace(/>/g, "&gt;")
	        .replace(/"/g, "&quot;")
	        .replace(/'/g, "&apos;");
	};
	/** 
	 * Sarissa derived Unescape
	 * Unescape the given string. This turns the occurences of the predefined XML 
	 * entities to become the characters they represent correspond to the five predefined XML entities
	 * @param sXml the string to unescape
	 */
	OpenLayers.Ajax.unescape = function(sXml){
	    return sXml.replace(/&apos;/g,"'")
	        .replace(/&quot;/g,"\"")
	        .replace(/&gt;/g,">")
	        .replace(/&lt;/g,"<")
	        .replace(/&amp;/g,"&");
	};
	
	
	
	/** 
	 * Sarissa node values definition
	 * 
	 */
	if(!window.Node || !Node.ELEMENT_NODE){
	    Node = {ELEMENT_NODE: 1, ATTRIBUTE_NODE: 2, TEXT_NODE: 3, CDATA_SECTION_NODE: 4, ENTITY_REFERENCE_NODE: 5,  ENTITY_NODE: 6, PROCESSING_INSTRUCTION_NODE: 7, COMMENT_NODE: 8, DOCUMENT_NODE: 9, DOCUMENT_TYPE_NODE: 10, DOCUMENT_FRAGMENT_NODE: 11, NOTATION_NODE: 12};
	};
	



	
