	 
//	var propertyAndValueArray = new Array();	
//	var streetNameNumber = new OpenLayers.LocalgisUtils.ClassPropertyAndValue("","property(VADO:UBICACION)","");
//	
//	onBeforeExitFunction = function(e){			
//	 	e.cancelBubble=true;	 	
//	 	if(e.stopPropagation){
//	 		e.preventDefault();	
//	 		e.stopPropagation();
//	 	}
//	 	else e.returnValue = false;	 		
//	 	exitFunction();
//		return false;    	  		
//    };      
//     
//    exitFunction = function(){
//    	if(window.opener!=null){ //&& !window.opener.close){
//	    	try{
//				if(layerWfst.features.length>0 && layerWfst.features[0].geometry.bounds.centerLonLat.lon!=null){
//					//peticion service calle mas cercana de un punto 
//					OpenLayers.LocalgisUtils.NearestStreetSearch(layerWfst.features[0].geometry.bounds.centerLonLat.lon,layerWfst.features[0].geometry.bounds.centerLonLat.lat,srid,id_municipio,streetNameNumber);
//				}
//				OpenLayers.LocalgisUtils.waitForFunction("openerFunction()",10000); 
//    		}
//			catch(ex){ 
//			alert(ex);
//			}		
//		} else alert("Error: Imposible conectar con el formulario tramitador"); 
//    };
//     
//    openerFunction = function(){
//		try{
//			if(streetNameNumber.value!=null) propertyAndValueArray.push(streetNameNumber);
//    	 
//			if(propertyAndValueArray.length>0){    	 		
//				if(typeof window.opener.updateFields=='function'){
//					window.opener.updateFields(propertyAndValueArray);
//				}
//			 }
//			 alert("Carga de datos finalizada");   	
//		}
//		catch(ex){ 
//			alert(ex);
//		}			
//    };
				
	/* 	 
	var propertyAndValueArray = new Array();	
	var streetNameNumber = new OpenLayers.LocalgisUtils.ClassPropertyAndValue("","property(VADO:UBICACION)","");
	
	onBeforeExitFunction = function(e){			
	 	e.cancelBubble=true;	 	
	 	if(e.stopPropagation){
	 		e.preventDefault();	
	 		e.stopPropagation();
	 	}
	 	else e.returnValue = false;	 		
	 	exitFunction();
		return false;    	  		
    };      
     
    exitFunction = function(){
    	if(window.opener!=null){ //&& !window.opener.close){
	    	try{
				if(layerWfst.features.length>0 && layerWfst.features[0].geometry.bounds.centerLonLat.lon!=null){
					//peticion service calle mas cercana de un punto 
					OpenLayers.LocalgisUtils.NearestStreetSearch(layerWfst.features[0].geometry.bounds.centerLonLat.lon,layerWfst.features[0].geometry.bounds.centerLonLat.lat,srid,id_municipio,streetNameNumber);
				}
				OpenLayers.LocalgisUtils.waitForFunction("openerFunction()",10000); 
    		}
			catch(ex){ 
			alert(ex);
			}		
		} else alert("Error: Imposible conectar con el formulario tramitador"); 
    };
     
    openerFunction = function(){
		try{
			if(streetNameNumber.value!=null) propertyAndValueArray.push(streetNameNumber);
    	 
			if(propertyAndValueArray.length>0){    	 		
				if(typeof window.opener.updateFields=='function'){
					window.opener.updateFields(propertyAndValueArray);
				}
			 }
			 alert("Carga de datos finalizada");   	
		}
		catch(ex){ 
			alert(ex);
		}			
    };    				
     			
		*/                    
		
