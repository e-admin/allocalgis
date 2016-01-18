
OpenLayers.Control.FeatureSearchLocalgis = 
  OpenLayers.Class(OpenLayers.Control, {
    
    layersSearch: null,
    
	webServiceHost: '',  
	
	onNotFeatureSearchText: "",
	
	procedureName: null,
	
	idEntidad: null,
	
	scheme: null,
	
	feature_toolbarName: null,
    /**
     * Constructor: OpenLayers.Control.LayerSwitcher
     * 
     * Parameters:
     * options - {Object}
     */
    initialize: function(options) {
        OpenLayers.Control.prototype.initialize.apply(this, arguments);     
    },
       
    draw: function() {
        OpenLayers.Control.prototype.draw.apply(this);      
        this.redraw();    
        return this.div;
    },
    
    setMap: function(map) {
        OpenLayers.Control.prototype.setMap.apply(this, arguments);
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
    	this.div.innerHTML="";
    	this.div.id="featureSearchLocalgis";
    	this.div.className="";
    	this.div.style.position="relative";     	
    	this.getPropertyAndName();
    	//else this.div.innerHTML="Ruta al servicio de búsqueda no configurada";
    	return this.div;
    },
    
    getPropertyAndName: function(){
	    var getPropertyAndNameResultsReplyServer = {
			callback: function(data) {
				if(data != null && data.length > 0){
				    document.getElementById("featureSearchLocalgis").innerHTML="";
				    for(var i=0,len=data.length;i<len;i++){
					   	if(data[i].searchActive==true)
					   		document.getElementById("featureSearchLocalgis").innerHTML+=data[i].name + ": " + "<input type=\"text\" id=\"featureSearch_" + data[i].property + "\" name=\"" + data[i].property + "\" style=\"width:178px\"><br>"; 		
					}
				    var searchInputs = document.getElementById("featureSearchLocalgis").getElementsByTagName("input");
				    if(searchInputs!=undefined && searchInputs.length>0){
					    document.getElementById("featureSearchLocalgis").innerHTML+="<br><div id=\"featureSearchBtn\" onclick=\"map.getFirstControlByClass('OpenLayers.Control.FeatureSearchLocalgis').searchFeaturesAll();\" class=\"ActionBtn\" style=\"left:12px;\">BUSCAR</div> "
					    + "<div id=\"featureCancelBtn\" onclick=\"map.getFirstControlByClass('OpenLayers.Control.FeatureSearchLocalgis').cancelSearch();\" class=\"ActionBtn\" style=\"left:28px;\">CANCELAR</div><br><br>"
					   	+ "<div id=\"featureSearchResult\"></div>";  
					}else document.getElementById("featureSearchLocalgis").innerHTML="No han sido asociados parametros de busqueda";
				}
				else document.getElementById("featureSearchLocalgis").innerHTML="No han sido asociados parámetros de búsqueda";
				},
			timeout:20000
		};		
	    if(this.procedureName!=null){ 	
	    	Sigm.getPropertyAndName(this.procedureName, getPropertyAndNameResultsReplyServer);
		}
	},

    searchFeaturesAll: function(){
    	var features = new Array();
		for(var i=0,len=document.getElementById("featureSearchLocalgis").getElementsByTagName("input").length;i<len;++i){
			var property = document.getElementById("featureSearchLocalgis").getElementsByTagName("input")[i].name;
			var value = document.getElementById("featureSearchLocalgis").getElementsByTagName("input")[i].value;
			if(value!=""){
				features.push(new OpenLayers.LocalgisUtils.ClassPropertyAndValue("",property,value));
			}  			
		}	
		this.searchFeaturesBy(features);
	},	
	
    searchFeaturesBy: function(propertyAndValueArray){
		if(propertyAndValueArray.length>0){
			var layersSearch = this.layersSearch;
			var pkProperty = layersSearch[0].pkProperty;	
			var onNotFeatureSearchText = this.onNotFeatureSearchText;
			var getSearchAllResultsReplyServer = {
				callback: function(data){
					if(data != null && data.length > 0){
						var contentHTML = "";
				    	for(var i=0,ilen=data.length;i<ilen;i++){
							for(var j=0,jlen=layersSearch.length;j<jlen;j++){							
								var layerFeature = layersSearch[j].getFeatureByAttribute(pkProperty,data[i]);
								if(layerFeature!=null){									
									contentHTML+="<div class=\"ActionText\""
									+ "onclick=\"map.getFirstControlByClass('OpenLayers.Control.FeatureSearchLocalgis').selectFeatureFromSearch('" + data[i] + "');"
									+ "map.zoomToExtent(new OpenLayers.Bounds(" + layerFeature.geometry.bounds.toBBOX() + "));map.zoomOutBird(3);\">" 
									+ data[i] + "</div>";									
								}
							}
						}			    		
						document.getElementById("featureSearchResult").innerHTML = contentHTML;
				   	}
				   	else document.getElementById("featureSearchResult").innerHTML = onNotFeatureSearchText;				
				},
				timeout:20000
			};			
			if(this.webServiceHost!='')
				Sigm.getSearchAll(this.webServiceHost, this.idEntidad, this.procedureName, propertyAndValueArray, getSearchAllResultsReplyServer);
		   	else document.getElementById("featureSearchLocalgis").innerHTML="Ruta al servicio de búsqueda no encontrada";				
		}else document.getElementById("featureSearchResult").innerHTML = "Introduzca datos antes de pulsar BUSCAR";
    },	
	
	cancelSearch: function(){
		var inputs = this.div.getElementsByTagName('input');
		for(var i=0,len=inputs.length; i<len; i++ ){ 
		    inputs[i].value="";  
		}
		document.getElementById("featureSearchResult").innerHTML="";
	},
	
	selectFeatureFromSearch: function(id){
			var feature = this.getFeatureFromLayers(id);	
			if(feature!=null){
				this.map.getFirstControlByClass("OpenLayers.Control." + feature_toolbarName).activateControl(this.map.getFirstControlByClass("OpenLayers.Control." + feature_toolbarName).selectFeature);
				this.map.getFirstControlByClass("OpenLayers.Control.SelectFeatureLocalgis").unselectAllFeatures();
				this.map.getFirstControlByClass("OpenLayers.Control.SelectFeatureLocalgis").select(feature);
			}
	},
	
	getFeatureFromLayers: function(id){
		var feature = null;
		var pkProperty = this.layersSearch[0].pkProperty;
		for(var j=0,jlen=this.layersSearch.length;j<jlen;j++){							
			feature = this.layersSearch[j].getFeatureByAttribute(pkProperty,id);
			if(feature!=null){
				break;
			}
		}	
		return feature;
	},

	setLayersSearch: function(layersSearch){
		this.layersSearch=layersSearch;
		//this.redraw();
	},

    CLASS_NAME: "OpenLayers.Control.FeatureSearchLocalgis"
});
