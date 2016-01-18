
OpenLayers.Control.StreetSearchLocalgis = 
  OpenLayers.Class(OpenLayers.Control, {
    
	webServiceHost: null,  
	
	idEntidad: null,
	
	srid: null,
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
    	this.div.id="streetSearchLocalgis";
    	this.div.className="";
    	this.div.style.position="relative";
    	this.div.innerHTML+="Nombre de la Calle: <input type=\"text\" id=\"streetSearch_StreetName\" style=\"width:200px\"><br>"; 		
    	this.div.innerHTML+="Número de la Calle: <input type=\"text\" id=\"streetSearch_StreetNumber\" style=\"width:200px\"><br>";     
    	this.div.innerHTML+="<br><div id=\"streetSearchBtn\" onclick=\"map.getFirstControlByClass('OpenLayers.Control.StreetSearchLocalgis').searchFeatureAll();\" class=\"ActionBtn\" style=\"left:23px;\">BUSCAR</div> "
	    + "<div id=\"streetCancelBtn\" onclick=\"map.getFirstControlByClass('OpenLayers.Control.StreetSearchLocalgis').cancelSearch();OpenLayers.LocalgisUtils.removeMarkers();\" class=\"ActionBtn\" style=\"left:37px;\">CANCELAR</div><br><br>"
	    + "<div id=\"streetSearchResult\"></div>";  
        return this.div;
    },

    searchFeature: function(streetName,streetNumber){
    	 var getStreetResultsReplyServer = {
    		callback: function(data) {
	            var contentHTML="";
	            if (data != undefined && data.length > 0) {
	                for (var i = 0; i < data.length; i++) {
	                    if (data[i].exactResult) {
	                        contentHTML += '<div class="ActionText" name="streetSearch_Element" onclick="OpenLayers.LocalgisUtils.selectResult(this,'+data[i].posX+', '+data[i].posY+');OpenLayers.LocalgisUtils.goToResultEntidad();">'+data[i].name+', '+data[i].numero+'</div>';
	                    } else {
	                        contentHTML += '<div class="ActionText" name="streetSearch_Element" onclick="OpenLayers.LocalgisUtils.selectResult(this,'+data[i].posX+', '+data[i].posY+');OpenLayers.LocalgisUtils.goToResultEntidad();">'+data[i].name+'</div>';
	                    }
	                }
	            } else {
	                contentHTML = 'No se ha encontrado ninguna calle con estas características.';
	            }
	            //OpenLayers.LocalgisUtils.showPopup(contentHTML);
	            document.getElementById("streetSearchResult").innerHTML=contentHTML;
		    },
	        timeout:20000//,
	        /*errorHandler:function(message) { 
	            OpenLayers.LocalgisUtils.showError();
	        }*/
	    };
	    
	    if(this.webServiceHost!=null){    
	    	WFSGService.getStreetInformation(this.webServiceHost, streetName, streetNumber, this.idEntidad,this.srid, getStreetResultsReplyServer);            
	    }else alert("Error : StreetSearchLocalgis : Dirección de servicio web mal introducida");	    	
	},	
	
	searchFeatureAll: function(){
		var streetName = document.getElementById("streetSearch_StreetName").value;
	    var streetNumber = document.getElementById("streetSearch_StreetNumber").value;//parseInt(document.forms['search'].numero.value);
	    if (streetName.trim() == '') {
	        alert("Debe introducir un nombre de calle correcto");
	        return;	        
	    } 
	    //else if (streetNumber != document.getElementById("streetSearch_StreetNumber").value || streetNumber == 0) {  
	    	//alert("Debe introducir un número de calle correcto");
	        //return;
	    //} 	    
	    else if (streetNumber != document.getElementById("streetSearch_StreetNumber").value || streetNumber == 0) {  
	     	streetNumber="1";
	    	document.getElementById("streetSearch_StreetNumber").value = streetNumber;	   
	    } 	
	    this.searchFeature(streetName,streetNumber);
	},
	
	cancelSearch: function(){
		var inputs = this.div.getElementsByTagName('input');
		for(var i=0,len=inputs.length; i<len; i++ ){ 
		    inputs[i].value="";  
		}
		document.getElementById("streetSearchResult").innerHTML="";
	},	

    CLASS_NAME: "OpenLayers.Control.StreetSearchLocalgis"
});
