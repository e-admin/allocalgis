OpenLayers.Control.SalesManLocalgis  = 
  OpenLayers.Class(OpenLayers.Control, {
    /** @type OpenLayers.Control.TYPE_* */
    type: OpenLayers.Control.TYPE_BUTTON,
 
   
    
    /**
    * @constructor
    *
    * @param {String} urlPlaceNameInfoService Url del servicio de toponimos
    */
    initialize: function() {
        OpenLayers.Control.prototype.initialize.apply(this, arguments);
    },
    
    /**
     * Evento de click en el control
     */
    trigger: function() {
    	
    	if(arrayRoute.length >= 3) 	{
    		calcularRuta(0,tipoRuta,true,true);
		}
    },

    /** @final @type String */
    CLASS_NAME: "OpenLayers.Control.SalesManLocalgis"
});

