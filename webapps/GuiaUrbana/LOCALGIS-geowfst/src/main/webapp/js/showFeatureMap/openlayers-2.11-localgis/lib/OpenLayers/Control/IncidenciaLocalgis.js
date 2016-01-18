/**
 * @class
 *
 * Imlements a very simple button control.
 * @requires OpenLayers/Control.js
 */
OpenLayers.Control.IncidenciaLocalgis  = 
	  OpenLayers.Class(OpenLayers.Control, {
	    /** @type OpenLayers.Control.TYPE_* */
	    type: OpenLayers.Control.TYPE_TOOL,
	    
	    /*
	     * Posicion donde se produce el ultimo evento
	     */
	    lastEventXY: null,

	    /*
	     * Identificador del mapa asociado
	     */
	    idMap: null,
	    
	    /*
	     * Identificador de la capa asociada
	     */
	    layerName: null,
	        
	    nombreElemento: null,
	    
	    /*
	     * 
	     * Identificador de la entidad asociada
	     */
	    idFeature: null,
	    
	    idMunicipio: null,
	    
	    /** @type String */
	    /** Funcion JS a la que se llamara cuando se obtenga una feature con respuesta GML asociada */
	    getFeatureInfoCallback: null,

	    initialize: function(getFeatureInfoCallback, idMap) {
	  		OpenLayers.Control.prototype.initialize.apply(this, arguments);
	        this.getFeatureInfoCallback = getFeatureInfoCallback;
	        this.idMap = idMap;
	        this.idMunicipio = 30026;

	    },
	    

	    draw: function() {
	        this.handler = new OpenLayers.Handler.ClickLocalgis( this,
	                            {done: this.click}, {keyMask: this.keyMask} );
	    },

	    click: function(xy) {
	    	
	    	this.lastEventXY = xy;
	        var x = xy.x;
	        var y = xy.y;
	        var url;
	    	// Comprobamos que haya capa activa
	        if (this.map.activeLayer == null) {
	        	this.showFeatureInfo2(); 
	            return false;
	        } else if (!(this.map.activeLayer instanceof OpenLayers.Layer.WMSLocalgis) || this.map.activeLayer.urlGetFeatureInfo == null || this.map.activeLayer.urlGetFeatureInfo == '') {
	        	this.showFeatureInfo2(); 
	            return false;
	        }
	        
	        OpenLayers.LocalgisUtils.showSearchingPopup();
	        
		        if (this.map.activeLayer.urlGetFeatureInfo != null) {
		            url = this.map.activeLayer.urlGetFeatureInfo + "&";
		            url += OpenLayers.Util.getParameterString(
		                        {BBOX: this.map.getExtent().toBBOX(),
		                         X: x,
		                         Y: y,
		                         WIDTH: this.map.size.w,
		                         SLD: "",
		                         HEIGHT: this.map.size.h});
		        } else {
		            url =  this.map.activeLayer.getFullRequestString({
		                         REQUEST: "GetFeatureInfo",
		                         EXCEPTIONS: "application/vnd.ogc.se_xml",
		                         BBOX: this.map.getExtent().toBBOX(),
		                         X: x,
		                         Y: y,
		                         INFO_FORMAT: "gml",
		                         QUERY_LAYERS: this.map.activeLayer.params.LAYERS,
		                         WIDTH: this.map.size.w,
		                         SLD: "",
		                         HEIGHT: this.map.size.h});
		        }
	        
	        OpenLayers.loadURL(url, 'accion=incidencia', this, this.showFeatureInfo, this.showErrorFeatureInfo); 
	    },
	    
	    showFeatureInfo: function(xmlHTTPRequest) {

	        if (xmlHTTPRequest.status == 200) {
	        	
	        	var contentHTML;
		        contentHTML = '<form name="addIncidencia">';
		        contentHTML += '<table style="margin-left:auto;margin-right:auto;text-align:center">';
	        	contentHTML += '<tr>';
	        	var xmlDoc = xmlHTTPRequest.responseXML;
	        	if (xmlDoc != null){
	        		if (xmlDoc.getElementsByTagName('id')[0] != null)
	        			this.idFeature = xmlDoc.getElementsByTagName('id')[0].childNodes[0].nodeValue.replace(/\n/gi,"");
	        		if (xmlDoc.getElementsByTagName('layer_name')[0] != null)
	        			this.layerName = xmlDoc.getElementsByTagName('layer_name')[0].childNodes[0].nodeValue.replace(/\n/gi,"");
	        		if (xmlDoc.getElementsByTagName('id_municipio')[0] != null)
	        			this.idMunicipio = xmlDoc.getElementsByTagName('id_municipio')[0].childNodes[0].nodeValue.replace(/\n/gi,"");
	        		if (xmlDoc.getElementsByTagName('nombre')[0] != null){
	        			this.nombreElemento = xmlDoc.getElementsByTagName('nombre')[0].childNodes[0].nodeValue.replace(/\n/gi,"");
	        			
	    		        contentHTML += '<td align="left" colspan="2">La incidencia se asociará al elemento: '+this.nombreElemento+'</td>';
	    		        contentHTML += '</tr>';
	    		        contentHTML += '<tr>';
	        		}
	        	}
		        
		        contentHTML += '<td align="left">Tipo:</td>';
		        contentHTML +=	'<td align="left"><select name="tipoIncidencia" class="select">'; 
		        contentHTML +=	'<option value="mu">Mobiliario Urbano</option>'; 
		        contentHTML +=	'<option value="vp">Vía Pública</option>'; 
		        contentHTML +=	'</select></td>'; 
		        contentHTML += '</tr>';
		        contentHTML += '<tr>';
		        contentHTML += '<td align="left">Gravedad:</td>';
		        contentHTML +=	'<td align="left"><select name="gravedadIncidencia" class="select">'; 
		        contentHTML +=	'<option value="b">Baja</option>'; 
		        contentHTML +=	'<option value="m">Media</option>';
		        contentHTML +=	'<option value="a">Alta</option>'; 
		        contentHTML +=	'</select></td>'; 		        
		        contentHTML += '</tr>';
		        contentHTML += '<tr>';
		        contentHTML += '<td align="left">e-mail:</td>';
		        contentHTML += '<td><input type="text" class="inputTextField" name="emailContacto" value="" style="width: 240px;"></td>';
		        contentHTML += '</tr>';	 	        
		        contentHTML += '<tr>';
		        contentHTML += '<td align="left">Descripción:</td>';
		        contentHTML += '<td colspan="2" align="left"><textarea name="descripcion" style="width: 240px; height: 35px;"></textarea></td>';
		        contentHTML += '</tr>';
		        contentHTML += '<tr>';
		        contentHTML += '<td align="center" colspan="2"><div id="divButtonCreateMarker"><img id="buttonCreateMarker" class="imageButton" src="img/btn_aceptar.gif" alt="Aceptar"/></div>';
		        contentHTML += '</tr>';
		        contentHTML += '</table>';
		        contentHTML += '</form>';
		        
	            var result;

	            OpenLayers.LocalgisUtils.showPopup(contentHTML);	            
	            
		        var imageElement = document.getElementById("buttonCreateMarker");
		        
		        // Contexto para los eventos
		        var context = {'incidenciaLocalgis': this};
		        
		        // Caputa del evento click en la imagen de crear una marca
		        OpenLayers.Event.observe(imageElement, "click", this.onCreateIncidencia.bindAsEventListener(context));
	        }
	    },

	    getLayerSwitcherLocalgis : function() {
	        for(i=0 ; i < this.map.controls.length;i++) {
	            if (this.map.controls[i] instanceof OpenLayers.Control.LayerSwitcherLocalgis) {
	                return this.map.controls[i];  
	            }
	        }
	    },
	    
	    showErrorFeatureInfo: function(xmlHTTPRequest) {

	        var contentHTML;;
	        contentHTML = "<br>No se ha encontrado ningún elemento.<br><br>";

	        OpenLayers.LocalgisUtils.showPopup(contentHTML);
	    },
	    
	    /*
	     * Metodo para crear una marca. Contexto: incidenciaLocalgis --> IncidenciaLocalgis
	     */
	    onCreateIncidencia: function(e) {
	    	var tipoIncidencia = document.addIncidencia.tipoIncidencia.value;
	        if (tipoIncidencia == undefined || tipoIncidencia.trim() == '') {
	            alert("Debe introducir el tipo de incidencia.");
	            return;
	        }
	    	var gravedadIncidencia = document.addIncidencia.gravedadIncidencia.value;	        
	        if (gravedadIncidencia == undefined || gravedadIncidencia.trim() == '') {
	            alert("Debe introducir la gravedad de la incidencia.");
	            return;
	        }
	        var emailContacto = document.addIncidencia.emailContacto.value;
	        if (emailContacto == undefined || emailContacto.trim() == '') {
	            alert("Debe introducir un correo electrónico.");
	            return;
	        }
	        var descripcion = document.addIncidencia.descripcion.value;
	        if (descripcion == undefined || descripcion.trim() == '') {
	            alert("Debe introducir una descripción.");
	            return;
	        }
	        var lonlat = this.incidenciaLocalgis.map.getLonLatFromPixel(this.incidenciaLocalgis.lastEventXY);
	        
            OpenLayers.Control.IncidenciaLocalgis.showIncidencia(lonlat);
            
	        var divButtonCreateMarker = document.getElementById('divButtonCreateMarker');
	        divButtonCreateMarker.innerHTML = '<img src="'+OpenLayers.Util.getImagesLocation()+'ajax-loader.gif" alt="Creando"/>';
	        
	        IncidenciaService.addIncidencia(this.incidenciaLocalgis.idMap, this.incidenciaLocalgis.layerName, this.incidenciaLocalgis.idFeature, tipoIncidencia, gravedadIncidencia, emailContacto, descripcion, lonlat.lon, lonlat.lat, this.incidenciaLocalgis.idMunicipio);
	        
	        var divVentanaIncidencia = document.getElementById('popupLocalgisBody');
	        divVentanaIncidencia.innerHTML = "<br>Incidencia añadida correctamente.<br><br>";
            
            
	        	        
	    },
	    
	    
	    showFeatureInfo2: function(xmlHTTPRequest) {
       	
	        	var contentHTML;
		        contentHTML = '<form name="addIncidencia">';
		        contentHTML += '<table style="margin-left:auto;margin-right:auto;text-align:center">';
		        contentHTML += '<tr>';
		        contentHTML += '<td align="left">Tipo:</td>';
		        contentHTML +=	'<td align="left"><select name="tipoIncidencia" class="select">'; 
		        contentHTML +=	'<option value="mu">Mobiliario Urbano</option>'; 
		        contentHTML +=	'<option value="vp">Vía Pública</option>'; 
		        contentHTML +=	'</select></td>'; 
		        contentHTML += '</tr>';
		        contentHTML += '<tr>';
		        contentHTML += '<td align="left">Gravedad:</td>';
		        contentHTML +=	'<td align="left"><select name="gravedadIncidencia" class="select">'; 
		        contentHTML +=	'<option value="b">Baja</option>'; 
		        contentHTML +=	'<option value="m">Media</option>';
		        contentHTML +=	'<option value="a">Alta</option>'; 
		        contentHTML +=	'</select></td>'; 		        
		        contentHTML += '</tr>';
		        contentHTML += '<tr>';
		        contentHTML += '<td align="left">e-mail:</td>';
		        contentHTML += '<td><input type="text" class="inputTextField" name="emailContacto" value="" style="width: 240px;"></td>';
		        contentHTML += '</tr>';	 	        
		        contentHTML += '<tr>';
		        contentHTML += '<td align="left">Descripción:</td>';
		        contentHTML += '<td colspan="2" align="left"><textarea name="descripcion" style="width: 240px; height: 45px;"></textarea></td>';
		        contentHTML += '</tr>';
		        contentHTML += '<tr>';
		        contentHTML += '<td align="center" colspan="2"><div id="divButtonCreateMarker"><img id="buttonCreateMarker" class="imageButton" src="img/btn_aceptar.gif" alt="Aceptar"/></div>';
		        contentHTML += '</tr>';
		        contentHTML += '</table>';
		        contentHTML += '</form>';
		        
	            OpenLayers.LocalgisUtils.showPopup(contentHTML);

		        var imageElement = document.getElementById("buttonCreateMarker");
		        
		        // Contexto para los eventos
		        var context = {'incidenciaLocalgis': this};
		        
		        // Caputa del evento click en la imagen de crear una marca
		        OpenLayers.Event.observe(imageElement, "click", this.onCreateIncidencia.bindAsEventListener(context));
	   //     }
	    },

	     
	    /** @final @type String */
	    CLASS_NAME: "OpenLayers.Control.IncidenciaLocalgis"
	    	
	});

OpenLayers.Control.IncidenciaLocalgis.showIncidencia = function (lonlat) {

    var incidencias = new OpenLayers.Layer.Markers("Incidencia");
    incidencias.displayInLayerSwitcher = false;
    map.addLayer(incidencias);

    var size = new OpenLayers.Size(18,17);
    var offset = new OpenLayers.Pixel(-(size.w/2), -size.h);
    var icon = new OpenLayers.Icon('js/openlayers-2.5/theme/localgis/img/incidencia_on.png',size,offset);
    incidencias.addMarker(new OpenLayers.Marker(lonlat,icon));
    incidencias.addMarker(new OpenLayers.Marker(lonlat,icon.clone()));
};
