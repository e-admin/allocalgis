
OpenLayers.Control.FeatureInfoLocalgis = 
  OpenLayers.Class(OpenLayers.Control, {
	
	webServiceHost: '',  
	
	onFeatureUnselectText: "",
	
	onNotFeatureInfoText: "",
		
	idEntidad: null,
	
	procedureName: null,	
	
	scheme: null,
	
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
        this.div.innerHTML=this.onFeatureUnselectText;  
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
       	this.div.id="featureInfoLocalgis";
    	this.div.className="";
    	this.div.style.position="relative";    	  	
        return this.div;
    },
    
    onFeatureSelected: function(property,id){  
    	this.getInfo(id);    	
	},
		
	getInfo: function(id){		
		if(this.webServiceHost!=''){
			var webServiceHost = this.webServiceHost;	
			var idEntidad = this.idEntidad;
			var procedureName = this.procedureName;
			var onNotFeatureInfoText = this.onNotFeatureInfoText;
			var propertyAndNameDiv = "";
			var getInfoAllResultsReplyServer = {
		    	callback: function(data) {
					if(data != null && data.length > 0){
						document.getElementById("featureInfoLocalgis").innerHTML = propertyAndNameDiv;
						for(var i=0,len=data.length;i<len;i++){ 							
							if(document.getElementById("featureInfo_" + data[i].property) != undefined && data[i].value != null){		
								document.getElementById("featureInfo_" + data[i].property).innerHTML=data[i].value;
							}     						
						}
					}
					else document.getElementById("featureInfoLocalgis").innerHTML=onNotFeatureInfoText + "(" + id + ")";	
				},
			    timeout:20000
			};	 		
			var getPropertyAndNameResultsReplyServer = {
					callback: function(data) {
			    	if(data != null && data.length > 0){
			    		document.getElementById("featureInfoLocalgis").innerHTML="";	    			
			    		for(var i=0;i<data.length;i++){
			    			if(data[i].active==true){
				    			propertyAndNameDiv+="  "
				    			+ data[i].name + ": " + "<br><div class=\"propertyAndName\" id=\"featureInfo_" 
				    			+ data[i].property + "\"><br></div>";
			    			}
			    		}
			    		if(webServiceHost!=''){ 			
			    			Sigm.getInfoAll(webServiceHost, idEntidad, procedureName, id, getInfoAllResultsReplyServer);	    	  
			    		}else alert("Error : FeatureInfoLocalgis : Configuración introducida incorrectamente");
			    	}
			    	else document.getElementById("featureInfoLocalgis").innerHTML=onNotFeatureInfoText + " (" + id + ")";
			    },
				timeout:20000
			};		
			if(procedureName!=null){ 							    
				Sigm.getPropertyAndName(procedureName, getPropertyAndNameResultsReplyServer);  				
			}else alert("Error : Configuración introducida incorrectamente");
		}else document.getElementById("featureInfoLocalgis").innerHTML="Ruta al servicio de información no configurada";
	},
	
	divContent: function(text){
		this.div.innerHTML=text;		
	},
	
	cleanAll: function(){
		this.divContent(this.onFeatureUnselectText);
	},
	
    CLASS_NAME: "OpenLayers.Control.FeatureInfoLocalgis"
});
