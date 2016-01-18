<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
   pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="es">
<head>
<title><bean:message key="welcome.title"/></title>
<link href="../css/styles.css" rel="stylesheet" type="text/css" />
<link rel="shortcut icon" href="../favicon.ico" type="image/x-icon" />
<script type='text/javascript' src='../dwr/interface/JMap.js'></script>
<script type='text/javascript' src='../dwr/engine.js'></script>
<script type='text/javascript' src='../dwr/util.js'></script>

<script type="text/javascript" src='../dwr/interface/SSOAuthCheckService.js'></script>
<script type="text/javascript" src='../js/ssoAuth/ssoAuth.js'></script>
<script type="text/javascript">
checkSSOAuth(<%= (Boolean)request.getSession().getAttribute("SSOActive") %>, '<%= (String)request.getSession().getAttribute("TokenAttribute") %>');
</script>
<script type="text/javascript">
<!--
		function MM_preloadImages() { //v3.0
		  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
		    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
		    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
		}
		
		function MM_jumpMenu(targ,selObj,restore){ //v3.0
		  eval(targ+".location='"+selObj.options[selObj.selectedIndex].value+"'");
		  if (restore) selObj.selectedIndex=0;
		}
		function MM_swapImgRestore() { //v3.0
		  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
		}
		
		function MM_findObj(n, d) { //v4.01
		  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
		    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
		  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
		  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
		  if(!x && d.getElementById) x=d.getElementById(n); return x;
		}
		
		function MM_swapImage() { //v3.0
		  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
		   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
		}
//-->
</script>
<script type="text/javascript">
<!--
       var disabledButtonPublish = false;
       var disabledButtonRepublish = false;
       var initialAvailableMapsOptions ;
       var initialPublishedMapsOptions ;
       
       function previous() {
               var optionsTarget = document.forms[0].available.options;
               var optionsSource = document.forms[0].published.options;
               for (i = 0;i<optionsSource.length;i++) {
                    if (optionsSource[i].text.indexOf('(Default)') >=0 && optionsSource[i].selected) {
                        var index = optionsSource[i].text.indexOf('(Default)');
                        optionsSource[i].text = optionsSource[i].text.substr(0,index);
                    }                
                }
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
                            if (optionsTarget[i].className == 'mapaPendienteCambios') {
                                optionsTarget[i].className = '';
                            } else {
                                optionsTarget[i].className = 'mapaPendienteCambios';
                            }

                    }catch (e) {

                    }

               }
       }
	        
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
       	 				toPublish = toPublish + initialAvailableMapsOptions[i].value + ';' + initialAvailableMapsOptions[i].text;
       	 			}
       	 			else {

       	 				toPublish = toPublish + ',' +  initialAvailableMapsOptions[i].value + ';' + initialAvailableMapsOptions[i].text;
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
       	 				toRemove = toRemove + initialPublishedMapsOptions[i].value + ';' + initialPublishedMapsOptions[i].text;
       	 			}
       	 			else {

       	 				toRemove = toRemove + ',' + initialPublishedMapsOptions[i].value + ';' + initialPublishedMapsOptions[i].text;
       	 			}
       	 			break;
       	 		}       	 		
       	 	}
       	 }
       	 document.forms[0].mapsToPublish.value = toPublish; 
       	 document.forms[0].mapsToRemove.value = toRemove;
       }
       
       function getMaps() {
      		var type = document.forms[0].type.value;
       		JMap.getMaps(type,getMapsReplyServer);
       } 
               
       function publishMaps() {
            if (disabledButtonPublish) {
                return false;
            }
            disableButtonRepublish();
       		calculateChanges();
       		var mapsToPublish = document.forms[0].mapsToPublish.value;
       		var mapsToRemove = document.forms[0].mapsToRemove.value;
       		var type = document.forms[0].type.value;
       		var defaultMap = getDefault();
       		if (mapsToPublish != '') {
	       		if (defaultMap != -1) {
	       		   JMap.publishMaps(mapsToPublish,mapsToRemove,type,defaultMap,publishMapsReplyServer);       		
	       		   var image = document.getElementById('image');
	       		   image.src = '../img/ajax-bar.gif';
	       		} else {
	       		    alert("Debe seleccionar un mapa por defecto.");
                    enableButtonRepublish();
	       		}
	        }
	        else {
	           JMap.publishMaps(mapsToPublish,mapsToRemove,type,defaultMap,publishMapsReplyServer);            
               var image = document.getElementById('image');
               image.src = '../img/ajax-bar.gif';
	        }
       }
       
       
       var getMapsReplyServer = {
  			callback:function(data) { 
  						if (data != null && typeof data == 'object') {
					     	var i = 0;
					     	var availableMaps = data['availableMaps'];
					     	var publihedMaps = data['publishedMaps'];
					     	var availableOptions = new Array();
					     	var publishedOptions = new Array();
					     	var i = 0;
					     	for(i = 0;i<availableMaps.length;i++) {
					     		availableOptions[i] = {name:availableMaps[i].idMap,value:availableMaps[i].name};
								
					     				     	
					     	}
					     	for (i = 0;i<publihedMaps.length;i++) {
					     			publishedOptions[i] = {name:publihedMaps[i].mapid + ';' + publihedMaps[i].mapidgeopista ,value:publihedMaps[i].mapid +' - ' + publihedMaps[i].name};
					     	        if (publihedMaps[i].mapdefault == 1) {
					     	             publishedOptions[i].value = publishedOptions[i].value + ' (Default)';
					     	        }
					     	}
				     	   dwr.util.removeAllOptions("available");
				     	   dwr.util.removeAllOptions("published");
				      	   dwr.util.addOptions("available",availableOptions,'name','value');
				     	   dwr.util.addOptions("published",publishedOptions,'name','value');
				        }
						chargeInitialOptions();
						var image = document.getElementById('image');
       					image.src = '../img/btn_aceptar.gif';
  					},
  			timeout:120000,
  			errorHandler:function(message, ex) { 
  						//problema de acceso a datos
			  			if(ex.javaClassName=="com.localgis.web.core.exceptions.LocalgisDBException"){
				  			texto="Se ha producido un error, contacte con el administrador del sistema.";
				  			alert(texto);
			  				}
			  			//error de configuración	
			  			else if(ex.javaClassName=="com.localgis.web.core.exceptions.LocalgisConfigurationException"){
			  				texto="Se ha producido un error de configuración";
				  			if(message!='')
				  			texto+="\r\n Descripción: "+message;
				  			alert(texto);
			  				}
			  			//un error de inicialización	
			  			else if(ex.javaClassName=="com.localgis.web.core.exceptions.LocalgisInitiationExceptionn"){
			  				texto="Se ha producido un error de inicialización";
				  			if(message!='')
				  			texto+="\r\n Descripción: "+message;
				  			alert(texto);
			  				}
			  			//parámetro inválido
			  			else if(ex.javaClassName=="com.localgis.web.core.exceptions.LocalgisInvalidParameterException"){
			  				texto="Se ha recibido un parámetro inválido";
				  			if(message!='')
				  			texto+="\r\n Descripción: "+message;
				  			alert(texto);
			  				}
			  			//mapa no encontrado
			  			else if(ex.javaClassName=="com.localgis.web.core.exceptions.LocalgisMapNotFoundException"){
			  				texto="Se ha producido un error al no encontrar el mapa";
				  			if(message!='')
				  			texto+="\r\n Descripción: "+message;
				  			alert(texto);
			  				}
			  			//operación no permitida
			  			else if(ex.javaClassName=="com.localgis.web.core.exceptions.LocalgisWMSException"){
			  				texto="Error realizando operación con el servidor de mapas de la guía urbana";
				  			if(message!='')
				  			texto+="\r\n Descripción: "+message;
				  			alert(texto);
			  				}
			  			//timeout
			  			else if(message=="Timeout"){
			  				texto="No ha sido posible realizar la operación, debido a que se ha sobrepasado el tiempo máximo para la misma";
				  			alert(texto);
			  			}
			  			//otros errores
			  			else{ 		
			  				texto="Se ha producido un error";
				  			if(message!='')
				  			texto+="\r\n Descripción: "+message;
				  			alert(texto);
			  				}	
			  			var image = document.getElementById('image');
  						image.src = '../img/btn_aceptar.gif';
  					 }
		};   
       
       var publishMapsReplyServer = {
  			callback:function(data) { 
  						getMaps();
						var image = document.getElementById('image');
       					image.src = '../img/btn_aceptar.gif';
                        enableButtonRepublish();
  					},
  			timeout:120000,
  			errorHandler:function(message, ex) { 
  						//problema de acceso a datos
			  			if(ex.javaClassName=="com.localgis.web.core.exceptions.LocalgisDBException"){
			  				if(ex.message=="El mapa que desea publicar ya ha sido previamente publicado por otro usuario")
			  					texto=message;
			  				else
				  				texto="Se ha producido un error, contacte con el administrador del sistema.";
				  			alert(texto);
			  				}
			  			//error de configuración	
			  			else if(ex.javaClassName=="com.localgis.web.core.exceptions.LocalgisConfigurationException"){
			  				texto="Se ha producido un error de configuración";
				  			if(message!='')
				  			texto+="\r\n Descripción: "+message;
				  			alert(texto);
			  				}
			  			//un error de inicialización	
			  			else if(ex.javaClassName=="com.localgis.web.core.exceptions.LocalgisInitiationExceptionn"){
			  				texto="Se ha producido un error de inicialización";
				  			if(message!='')
				  			texto+="\r\n Descripción: "+message;
				  			alert(texto);
			  				}
			  			//parámetro inválido
			  			else if(ex.javaClassName=="com.localgis.web.core.exceptions.LocalgisInvalidParameterException"){
			  				texto="Se ha recibido un parámetro inválido";
				  			if(message!='')
				  			texto+="\r\n Descripción: "+message;
				  			alert(texto);
			  				}
			  			//mapa no encontrado
			  			else if(ex.javaClassName=="com.localgis.web.core.exceptions.LocalgisMapNotFoundException"){
			  				texto="Se ha producido un error al no encontrar el mapa";
				  			if(message!='')
				  			texto+="\r\n Descripción: "+message;
				  			alert(texto);
			  				}
			  			//operación no permitida
			  			else if(ex.javaClassName=="com.localgis.web.core.exceptions.LocalgisWMSException"){
			  				texto="Error realizando operación con el servidor de mapas de la guía urbana";
				  			if(message!='')
				  			texto+="\r\n Descripción: "+message;
				  			alert(texto);
			  				}
			  			//timeout
			  			else if(message=="Timeout"){
			  				texto="No ha sido posible realizar la operación, debido a que se ha sobrepasado el tiempo máximo para la misma.";
				  			alert(texto);
			  			}
			  			//otros errores
			  			else{ 		
			  				texto="Se ha producido un error";
				  			if(message!='')
				  			texto+="\r\n Descripción: "+message;
				  			alert(texto);
			  				}
                        enableButtonRepublish();
                        var image = document.getElementById('image');
  						image.src = '../img/btn_aceptar.gif';
  					 }
		};        
       
       function textHtmlHandler(){
    		document.location.href= 'index.jsp';
   		}
   	
    	function init() {
			DWREngine.setTextHtmlHandler(textHtmlHandler);
    	}    

        function setDefault() {
            var publishedSelect = document.getElementById('published');
            var indexSelected = publishedSelect.options.selectedIndex;
            if (indexSelected != -1) {
                var i;
                var options = publishedSelect.options;
                for (i = 0;i<options.length;i++) {
                    if (options[i].text.indexOf('(Default)') >=0 ) {
                        var index = options[i].text.indexOf('(Default)');
                        options[i].text = options[i].text.substr(0,index);
                    }                
                }
                publishedSelect.options[indexSelected].text = publishedSelect.options[indexSelected].text + ' (Default)';
            }
            else alert("Debe tener un mapa a publicar seleccionado");
        }
        
        function getDefault () {
            var publishedSelect = document.getElementById('published');
            var options = publishedSelect.options;
            for(i = 0;i<options.length;i++) {
               if (options[i].text.indexOf('(Default)') >=0 ) {
                  var index = options[i].value.lastIndexOf(";");
                  return options[i].value.substr(index + 1);
               }                
            }
            return -1;
       } 
       
       function disableButtonRepublish() {
           disabledButtonRepublish = true;
           var image = document.getElementById('republishImage');
           image.className = '';
       }
       
       function disableButtonPublish() {
           disabledButtonPublish = true;
           var image = document.getElementById('image');
           image.className = '';
       }
       
       function enableButtonRepublish() {
           disabledButtonRepublish = false;
           var image = document.getElementById('republishImage');
           image.className = 'imageButton';
       }
       
       function enableButtonPublish() {
           disabledButtonPublish = false;
           var image = document.getElementById('image');
           image.className = 'imageButton';
       }

       
       function republishMaps(publicarTodasEntidades,borrar) {
           if (disabledButtonRepublish) {
               return false;
           }
           disableButtonPublish();
           var options = document.forms[0].published.options;
           var mapsToRepublish = '';
           var type = document.forms[0].type.value;
           for (var i=0; i<options.length; i++) {
               if (options[i].selected) {
                   var published = false;
                   for (var j=0; j<initialPublishedMapsOptions.length; j++) {
                       if (initialPublishedMapsOptions[j].value == options[i].value) {
                           published = true;
                           if (mapsToRepublish != '') {
                               mapsToRepublish += ',';
                           }
                           mapsToRepublish += mapsToRepublish + initialPublishedMapsOptions[j].value + ';' + initialPublishedMapsOptions[j].text;
                           break;
                       }
                   }
                   if (!published) {
                       alert("Ha seleccionado mapas que aún no están publicados.");
                       enableButtonPublish();
                       return;
                   }
               }
           }
           if (mapsToRepublish == '') {
               alert("Debe seleccionar los mapas a republicar.");
               enableButtonPublish();
           } else {
               JMap.republishMaps(mapsToRepublish,type,publicarTodasEntidades,borrar,republishMapsReplyServer);             
               var image = document.getElementById('republishImage');
               image.src = '../img/ajax-loader.gif';
           }
       }
       
       var republishMapsReplyServer = {
            callback:function(data) { 
                        var image = document.getElementById('republishImage');
                        image.src = '../img/btn_republicar.gif';
                        enableButtonPublish();
                    },
            timeout:120000,
            errorHandler:function(message, ex) { 
                       //problema de acceso a datos
			  			if(ex.javaClassName=="com.localgis.web.core.exceptions.LocalgisDBException"){
				  			texto="Se ha producido un error, contacte con el administrador del sistema.";
				  			alert(texto);
			  				}
			  			//error de configuración	
			  			else if(ex.javaClassName=="com.localgis.web.core.exceptions.LocalgisConfigurationException"){
			  				texto="Se ha producido un error de configuración";
				  			if(message!='')
				  			texto+="\r\n Descripción: "+message;
				  			alert(texto);
			  				}
			  			//un error de inicialización	
			  			else if(ex.javaClassName=="com.localgis.web.core.exceptions.LocalgisInitiationExceptionn"){
			  				texto="Se ha producido un error de inicialización";
				  			if(message!='')
				  			texto+="\r\n Descripción: "+message;
				  			alert(texto);
			  				}
			  			//parámetro inválido
			  			else if(ex.javaClassName=="com.localgis.web.core.exceptions.LocalgisInvalidParameterException"){
			  				texto="Se ha recibido un parámetro inválido";
				  			if(message!='')
				  			texto+="\r\n Descripción: "+message;
				  			alert(texto);
			  				}
			  			//mapa no encontrado
			  			else if(ex.javaClassName=="com.localgis.web.core.exceptions.LocalgisMapNotFoundException"){
			  				texto="Se ha producido un error al no encontrar el mapa";
				  			if(message!='')
				  			texto+="\r\n Descripción: "+message;
				  			alert(texto);
			  				}
			  			//operación no permitida
			  			else if(ex.javaClassName=="com.localgis.web.core.exceptions.LocalgisWMSException"){
			  				texto="Error realizando operación con el servidor de mapas de la guía urbana";
				  			if(message!='')
				  			texto+="\r\n Descripción: "+message;
				  			alert(texto);
				  			}
			  			//timeout
			  			else if(message=="Timeout"){
			  				texto="No ha sido posible realizar la operación, debido a que se ha sobrepasado el tiempo máximo para la misma.";
				  			alert(texto);
			  			}
			  			//publicacion global
			  			else if(ex.javaClassName=="com.localgis.web.wm.exceptions.LocalgisWaitException"){
			  				texto="Publicacion Global";
				  			if(message!='')
				  			texto+="\r\n Descripción: "+message;
				  			alert(texto);
			  				}
			  			//otros errores
			  			else{ 		
			  				texto="Se ha producido un error";
				  			if(message!='')
				  			texto+="\r\n Descripción: "+message;
				  			alert(texto);
			  				}
                        enableButtonPublish();
                        var image = document.getElementById('republishImage');
                        image.src = '../img/btn_republicar.gif';
                     }
        };        
		function goHome(){
			document.location.href="${pageContext.request.contextPath}";
		}
		
		
		
		function infoMapa() {          
	           var mapsToGetInfo= '';
	           var type = document.forms[0].type.value;
	           var options = document.forms[0].published.options;
	           for (var i=0; i<options.length; i++) {
	               if (options[i].selected) {
	                   var published = false;
	                   for (var j=0; j<initialPublishedMapsOptions.length; j++) {
	                       if (initialPublishedMapsOptions[j].value == options[i].value) {
	                           published = true;
	                           if (mapsToGetInfo != '') {
	                        	   mapsToGetInfo += ',';
	                           }
	                           mapsToGetInfo += mapsToGetInfo + initialPublishedMapsOptions[j].value + ';' + initialPublishedMapsOptions[j].text;
	                           break;
	                       }
	                   }
	                   if (!published) {
	                       alert("Ha seleccionado mapas que aún no están publicados.");
	                       return;
	                   }
	               }
	           }
	           if (mapsToGetInfo == '') {
	               alert("Debe seleccionar el mapa a consultar.");
	               enableButtonPublish();
	           } else {
	               JMap.getInfoMap(mapsToGetInfo,type,getInfoMapReplyServer);             
	               var image = document.getElementById('republishImage');
	               image.src = '../img/ajax-loader.gif';
	           }
	       }
		
		 var getInfoMapReplyServer = {
		            callback:function(data) { 
		                        var image = document.getElementById('republishImage');
		                        image.src = '../img/btn_republicar.gif';
		                        alert("Informacion del mapa");
		                    },
		            timeout:120000,
		            errorHandler:function(message, ex) { 
		                       //problema de acceso a datos
					  			if(ex.javaClassName=="com.localgis.web.core.exceptions.LocalgisDBException"){
						  			texto="Se ha producido un error, contacte con el administrador del sistema.";
						  			alert(texto);
					  				}
					  			//error de configuración	
					  			else if(ex.javaClassName=="com.localgis.web.core.exceptions.LocalgisConfigurationException"){
					  				texto="Se ha producido un error de configuración";
						  			if(message!='')
						  			texto+="\r\n Descripción: "+message;
						  			alert(texto);
					  				}
					  			//un error de inicialización	
					  			else if(ex.javaClassName=="com.localgis.web.core.exceptions.LocalgisInitiationExceptionn"){
					  				texto="Se ha producido un error de inicialización";
						  			if(message!='')
						  			texto+="\r\n Descripción: "+message;
						  			alert(texto);
					  				}
					  			//parámetro inválido
					  			else if(ex.javaClassName=="com.localgis.web.core.exceptions.LocalgisInvalidParameterException"){
					  				texto="Se ha recibido un parámetro inválido";
						  			if(message!='')
						  			texto+="\r\n Descripción: "+message;
						  			alert(texto);
					  				}
					  			//mapa no encontrado
					  			else if(ex.javaClassName=="com.localgis.web.core.exceptions.LocalgisMapNotFoundException"){
					  				texto="Se ha producido un error al no encontrar el mapa";
						  			if(message!='')
						  			texto+="\r\n Descripción: "+message;
						  			alert(texto);
					  				}
					  			//operación no permitida
					  			else if(ex.javaClassName=="com.localgis.web.core.exceptions.LocalgisWMSException"){
					  				texto="Error realizando operación con el servidor de mapas de la guía urbana";
						  			if(message!='')
						  			texto+="\r\n Descripción: "+message;
						  			alert(texto);
						  			}
					  			//publicacion global
					  			else if(ex.javaClassName=="com.localgis.web.wm.exceptions.LocalgisInfoException"){
					  				texto="Informacion del Mapa";
						  			if(message!='')
						  			texto+="\r\n Descripción: "+message;
						  			alert(texto);
					  				}
					  			//timeout
					  			else if(message=="Timeout"){
					  				texto="No ha sido posible realizar la operación, debido a que se ha sobrepasado el tiempo máximo para la misma.";
						  			alert(texto);
					  			}
		                       
					  			//otros errores
					  			else{ 		
					  				texto="Se ha producido un error";
						  			if(message!='')
						  			texto+="\r\n Descripción: "+message;
						  			alert(texto);
					  				}
		                        enableButtonPublish();
		                        var image = document.getElementById('republishImage');
		                        image.src = '../img/btn_republicar.gif';
		                     }
		        };        
		
//-->
</script>
<bean:define id="nombreEntidad" name="nombreEntidad" scope="session"/>
<bean:define id="tipoMapas" name="tipoMapas" scope="session"/>

 </head>
<body onload="MM_preloadImages();chargeInitialOptions();init();">
<div id="wrap">
<div id="top" class="top">
<img src="../img/top_left.gif" alt="" class="esquina_sup_izq" />
<img src="../img/top_right.gif" alt="" class="esquina_sup_der" />




</div>
<div id="content" class="content">
<div id="boxcontrol" class="boxcontrol">
<div id="bannerSelect" onclick="goHome()">


</div>
	
	


    <logic:present parameter="type">
    <bean:parameter id="type" name="type"/>
		<logic:present name="availableMaps">
			<logic:present name ="publishedMaps">
			
			
			 <table>
		<tr>
			<td><h1>Seleccione los mapas a publicar (<bean:write name="nombreEntidad"/> - <bean:write name="tipoMapas"/>)</h1></td>
			<td align="right" style="width:29em"></td>
			<td><html:img styleClass="imageButton" alt="menu" onclick="document.location.href='./menu.do'"   src="../img/btn_menu.gif"/></td>
			<td><html:img styleClass="imageButton" alt="salir" onclick="document.location.href='./logoff.do'" src="../img/btn_cerrar_sesion.gif" /></td>
		</tr>		
	</table>
			
			<form action="" method="post">
				<table style="margin-left:auto;margin-right: auto;text-align:center; width:85%;height:55%;">
					<tr>
						<td style="height:3em;"></td>
					</tr>
					<tr>
						<td><h1>Mapas Disponibles</h1></td>
						<td></td>
						<td><h1>Mapas Publicados</h1></td>					
					</tr>					
					<tr>											
						<td>
						  	 <!-- label for="available">available</label-->
						        <select size="6" id="available" name="available" multiple="multiple" style="background-color:#F7F7F7;width:235px;">
						            <logic:iterate  id="item" name="availableMaps" scope="request">
						            	<option value="${item.idMap};${item.idMap}">
											<bean:write name="item" property="name" />
										</option>
						            </logic:iterate>
						        </select>
						</td>

						<td>
								<html:img styleClass="imageButton" src="../img/btn_remove.gif" alt="Flecha izquierda" width="23" height="16" onclick="previous();"/>
								<html:img styleClass="imageButton" src="../img/btn_add.gif" alt="Flecha derecha" width="23" height="16" onclick="next();"/>
						</td>
						<td>       
							  <!-- label for="published">published</label-->
						       <select size="6" id="published" name="published" multiple="multiple" style="background-color:#F7F7F7;width:235px;">
						            <logic:iterate  id="item" name="publishedMaps"  scope="request">
						                <option value="${item.mapid};${item.mapidgeopista}">
						                	<bean:write name="item" property="mapid"/> - <bean:write name="item" property="name"/>
						                    <logic:equal name="item" property="mapdefault" value="1"> (Default)</logic:equal>
						                    
						                </option>
						            </logic:iterate>
						        </select>	        
						        <input type="hidden" id="type" value="${type}"/>
                                <input type="hidden" id="mapsToPublish"/>
						        <input type="hidden" id="mapsToRemove"/>						
						</td>
					</tr>
					<tr>
					   <td colspan="2">
					       &nbsp;
					   </td>
					   <td>
					       <table border="0" cellpadding="0" cellspacing="0" style="margin-left:auto;margin-right: auto;text-align:center;width: 235px">
					           <tr>
					               <td align="left">
					                   <html:img styleClass="imageButton" alt="publicar" styleId="republishImage" border="0" src="../img/btn_republicar.gif"  onclick="republishMaps(false,false);" />
					               </td>
					               <td align="right">
					                   <html:img styleClass="imageButton" alt="default" styleId="default" border="0" src="../img/default.gif"  onclick="setDefault();" />
					               </td>
					                  <td align="right">
					                   <html:img styleClass="imageButton" alt="republicar" styleId="republishImage" border="0" src="../img/btn_info.gif"  onclick="infoMapa();" />
					               </td>
					           </tr>
					           
					           <logic:present name="publicadorglobal">
					           <tr>
					            <td colspan="2" align="center">
					                   &nbsp;
					               </td>
					           </tr>
					           	<tr>
					               <td align="left">
					                   <html:img styleClass="imageButton" styleId="republishImage" border="0" src="../img/btn_publicar_entidades.gif" alt="Republicar Todos"  onclick="republishMaps(true,false);" />
					               </td>
					               <td align="right">
					                   <html:img styleClass="imageButton" styleId="republishImage" border="0" src="../img/btn_despublicar_entidades.gif" alt="DesPpublicar Todos"  onclick="republishMaps(true,true);" />
					               </td>
					           </tr>
					           </logic:present>
					           
					       </table>
					   </td>
					</tr>						
					<tr>
						<td colspan="3" align="center">
							<br/>
							<br/>
							<html:img styleClass="imageButton" styleId="image" border="0" src="../img/btn_aceptar.gif" alt="Aceptar"  onclick="publishMaps();" />
						</td>
					</tr>
				</table>
			</form>
			</logic:present>	
		</logic:present> 
	</logic:present>
 
<logic:notPresent name="availableMaps">
    <table>
    
		<tr>
			<td><h1>No hay mapas disponibles</h1></td>
			<td align="right" style="width:29em"></td>
			<td><html:img styleClass="imageButton" alt="menu" onclick="document.location.href='./menu.do'"   src="../img/btn_menu.gif"/></td>
			<td><html:img styleClass="imageButton" alt="salir" onclick="document.location.href='./logoff.do'" src="../img/btn_cerrar_sesion.gif"  /></td>
		</tr>		
	</table>
</logic:notPresent >

<logic:notPresent name="publishedMaps">
    <table>
    
		<tr>
			<td><h1>No hay mapas disponibles</h1></td>
			<td align="right" style="width:29em"></td>
			<td><html:img styleClass="imageButton" alt="menu" onclick="document.location.href='./menu.do'"   src="../img/btn_menu.gif"/></td>
			<td><html:img styleClass="imageButton" alt="salir" onclick="document.location.href='./logoff.do'" src="../img/btn_cerrar_sesion.gif" /></td>
		</tr>		
	</table>
</logic:notPresent >

</div>
</div>


<div id="bottom" class="bottom">
<img src="../img/btm_left.gif" alt="" class="esquina_inf_izq" />
<img src="../img/btm_right.gif" alt="" class="esquina_inf_der" />
</div>

</div>


  <!-- Recuadro inferior de la pagina -->
        <div id="wrap">
            <div id="top" class="top">
                <img src="../img/top_left.gif" alt="" class="esquina_sup_izq" />
                <img src="../img/top_right.gif" alt="" class="esquina_sup_der" />
            </div>
            <div id="content" class="content">
                <div id="boxcontrol" class="boxcontrol">
                    <!-- Contenido del recuadro -->
                    <div id="footer" class="footer"></div>
                    <!--  Fin del contenido del recuadro inferior -->
                </div>
            </div>
            <div id="bottom" class="bottom">
                <img src="../img/btm_left.gif" alt="" class="esquina_inf_izq" />
                <img src="../img/btm_right.gif" alt="" class="esquina_inf_der" />
            </div>
        </div>
        <!-- Fin del recuadro inferior de la pagina -->

</body>
</html>
