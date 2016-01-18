/**
 * @class
 *
 * Imlements a very simple button control.
 * @requires OpenLayers/Control.js
 */
OpenLayers.Control.ReportLocalgis  = 
	  OpenLayers.Class(OpenLayers.Control, {

	    type: OpenLayers.Control.TYPE_TOOL,
	    
	    idLayer: null,
	    
	    idFeature: null,
	        
	    idEntidad: null,
	    
	    idMunicipio: null,
	    
	    locale: null,
	    
	    plantillas:null,
	    
	    document:null,

	    initialize: function() {
	  		OpenLayers.Control.prototype.initialize.apply(this, arguments);
	        //this.getFeatureInfoCallback = getFeatureInfoCallback;

	    },
	    
	    
	    showPopupReport: function(idLayer,idFeature,idEntidad,idMunicipio,locale,plantillas) {
	      	        	
	        	var contentHTML;
		        contentHTML = '<form name="showReport">';
		        contentHTML += '<table style="margin-left:auto;margin-right:auto;text-align:center">';               

               	this.idLayer=idLayer;
               	this.idFeature=idFeature;
               	this.idEntidad=idEntidad;
               	this.idMunicipio=idMunicipio;
               	this.locale=locale;
               	this.plantillas=plantillas;
               	
		        
		        contentHTML += '<td align="left">Nombre del Informe:</td>';
		        contentHTML +=	'<td align="left"><select name="idPlantilla" class="select">'; 
		        for(var i=0; i < this.plantillas.length; i++) {
			        contentHTML +=	'<option value="'+plantillas[i]+'">'+plantillas[i]+'</option>'; 	        	
		        }
		        contentHTML +=	'</select></td>'; 
		        contentHTML += '</tr>';
		        contentHTML += '<tr>';
		        contentHTML += '<td align="left">Exportar a:</td>';
		        contentHTML +=	'<td align="left"><select name="format" class="select">'; 
		        contentHTML +=	'<option value="PDF" selected="selected">PDF</option>'; 
		        contentHTML +=	'<option value="XML">XML</option>';
		        contentHTML +=	'</select></td>'; 		        
		        contentHTML += '</tr>';		        
		        contentHTML += '<tr>';
		        contentHTML += '<td align="center" colspan="2"><div id="divButtonCreateMarker"><img id="buttonShowReport" class="imageButton" src="img/btn_aceptar.gif" alt="Aceptar"/></div>';
		        contentHTML += '</tr>';
		        contentHTML += '</table>';
		        contentHTML += '</form>';
		        
	            var result;

	            OpenLayers.LocalgisUtils.showPopupReport(contentHTML);	            
	            
		        var imageElement = document.getElementById("buttonShowReport");
		        
		        this.document=document;
		        
		        // Contexto para los eventos
		        var context = {'reportLocalgis': this};
		        
		        // Captura del evento click en la imagen de obtener el report
		        OpenLayers.Event.observe(imageElement, "click", this.onShowReport.bindAsEventListener(context));
	    },
	    

	    /*
	     * Metodo para crear una marca. Contexto: incidenciaLocalgis --> IncidenciaLocalgis
	     */
	    onShowReport: function(e) {
	    	
	    	  var getReportReplyServer = {
	                  callback: function(data) {
	                      var contentHTML;
	                    
  	
	                    var divVentanaReport= document.getElementById('popupLocalgisBodyReport');
	          	        
	          	        divVentanaReport.innerHTML = "<br>Informe generado correctamente.<br><br>";
	          	        
	          	        document.location.href="public/downloadReport.do?reportName="+data;
	          	        
	          	        	          	       
	                  },
	                  timeout:60000,
	                  errorHandler:function(message) { 
	                      //OpenLayers.LocalgisUtils.showError();
	                	  		          	        
	                	  var divVentanaReport= document.getElementById('popupLocalgisBodyReport');
		          	        
		          	      divVentanaReport.innerHTML = "<br>No se ha podido generar el informe<br><br>";		          	      	

	                  },
	                  	                  
	              };
            
	        var divButtonCreateMarker = document.getElementById('divButtonCreateMarker');
	        divButtonCreateMarker.innerHTML = '<img src="'+OpenLayers.Util.getImagesLocation()+'ajax-loader.gif" alt="Creando"/>';
	        
	        var idPlantilla=this.reportLocalgis.document.showReport.idPlantilla.options[document.showReport.idPlantilla.selectedIndex].value
	        var format=this.reportLocalgis.document.showReport.format.options[document.showReport.format.selectedIndex].value
	        var retorno=ReportService.showReport(this.reportLocalgis.idLayer,this.reportLocalgis.idFeature,this.reportLocalgis.idEntidad,this.reportLocalgis.idMunicipio,idPlantilla,format,this.reportLocalgis.locale,getReportReplyServer);
    	        
	    },
	    
	    /**
	     * APIMethod: destroy 
	     */    
	    destroy: function() {
	    	OpenLayers.Control.prototype.destroy.apply(this, arguments);
	    	var imageElement = document.getElementById("buttonShowReport");
	        
	    	OpenLayers.Event.stopObservingElement(imageElement);

	    },
	    
	    

	    /** @final @type String */
	    CLASS_NAME: "OpenLayers.Control.ReportLocalgis"
	    	
	});

