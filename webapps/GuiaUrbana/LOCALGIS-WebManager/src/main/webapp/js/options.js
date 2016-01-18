       function previous() {
               var optionsTarget = document.forms[0].available.options;
               var optionsSource = document.forms[0].published.options;
               moveOptions(optionsSource,optionsTarget);
       }
       
       function next() {
			   var optionsSource = document.forms[0].available.options;
               var optionsTarget = document.forms[0].published.options;
               moveOptions(optionsSource,optionsTarget);
       }       
       function moveOptions(optionsSource,optionsTarget) {
			
               var length = optionsSource.length;
               var i = 0;
               var j = 0;
               var optionsToMove = new Array();
               while (i< optionsSource.length) {
               		if (optionsSource[i].selected) {
               			
               			optionsSource[i].selected = false;
                        optionsToMove[j++] = optionsSource[i];
                        try {
                            optionsSource[i--] = null;
                        } catch (e) {

                        }
                    }
                     i++;   
               }
               j = 0;
               for (i=optionsTarget.length ;i < optionsTarget.length + optionsToMove.length ;i++) {
                   try {
                            optionsTarget[i] = optionsToMove[j++];

                    }catch (e) {

                    }

               }
       }
  	   var initialAvailableMapsOptions ;
	   var initialPublishedMapsOptions ;
	        
       function chargeInitialOptions() {
         initialAvailableMapsOptions = new Array();
         initialPublishedMapsOptions = new Array();
         var i = 0;
         for (i=0;i<document.forms[0].available.options.length;i++) {
         	initialAvailableMapsOptions[i] = document.forms[0].available.options[i];
         }
         for (i=0;i<document.forms[0].published.options.length;i++) {
         	initialPublishedMapsOptions[i] = document.forms[0].published.options[i];
         }
       }
       
       function calculateChanges() {
       	 var i = 0;
       	 var j = 0;
       	 var founded = false;
       	 var toPublish = '';
       	 var toRemove = '';
       	 var options = document.forms[0].published.options;
       	 for (i=0;i<initialAvailableMapsOptions.length;i++) {       	 	
       	 	for (j=0;j<options.length;j++) {
       	 		if (initialAvailableMapsOptions[i].value == options[j].value) {
       	 			if (toPublish == '') {
       	 				toPublish = toPublish + initialAvailableMapsOptions[i].value;
       	 			}
       	 			else {

       	 				toPublish = toPublish + ',' +  initialAvailableMapsOptions[i].value;
       	 			}
       	 			break;
       	 		}       	 		
       	 	}
       	 }
       	 options = document.forms[0].available.options;
       	 for (i=0;i<initialPublishedMapsOptions.length;i++) {       	 	
       	 	for (j=0;j<options.length;j++) {
       	 		if (initialPublishedMapsOptions[i].value == options[j].value) {
       	 			if (toRemove == '') {
       	 				toRemove = toRemove + initialPublishedMapsOptions[i].value;
       	 			}
       	 			else {

       	 				toRemove = toRemove + ',' + initialPublishedMapsOptions[i].value;
       	 			}
       	 			break;
       	 		}       	 		
       	 	}
       	 }
       	 document.forms[0].mapsToPublish.value = toPublish; 
       	 document.forms[0].mapsToRemove.value = toRemove;
       }
  }