OpenLayers.Control.WithinCostLocalgis  = 
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
    	if((arrayRoute.length > 0)&&(arrayRoute[0].id==0))
    	{
	    	var contentHTML;
	    	contentHTML = '<form name="rutasForm">';
	        contentHTML += '<table align="left" width="100%">';
	      
	      /*  contentHTML += '<tr><td colspan=2 align="left"><input type="radio" value=1 name="opcion" checked>Calcular el área de influencia</td></tr>';
	        contentHTML += '<tr><td colspan=2 align="left"><input type="radio" value=2 name="opcion">Calcular los posibles destinos</td></tr>';*/
	        
	        contentHTML += '<tr><td colspan=2 align="left"><img id="rdb0" src="img/rdb_1.gif" onClick="OpenLayers.Control.WithinCostLocalgis.updateCheckIe(0)"/>Calcular el área de influencia</td></tr>';
	        contentHTML += '<tr><td colspan=2 align="left"><img id="rdb1" src="img/rdb_0.gif" onClick="OpenLayers.Control.WithinCostLocalgis.updateCheckIe(1)"/>Calcular los posibles destinos</td></tr>';
	        
	        contentHTML += '<tr><td colspan=2>&nbsp;</td></tr>';
	        contentHTML += '<tr><td align=right>Coste:</td><td align="left"><input type="text" value=100 size="10" class="inputTextField" name="textoCoste"/></td></tr>';
	        contentHTML += '<tr><td colspan=2 align="center"><input type="hidden" name="selRdb" value=0><div id="divButtonRutas" style="margin-top: 6px;"><img class="imageButton" src="img/btn_aceptar.gif" alt="Aceptar" onclick="OpenLayers.Control.WithinCostLocalgis.calcular();"/></div></td></tr>';
	        contentHTML += '</table>';
	        contentHTML += '</form>';
	        
	        OpenLayers.LocalgisUtils.showPopup(contentHTML);
        }
        
        
        
    },

    /** @final @type String */
    CLASS_NAME: "OpenLayers.Control.WithinCostLocalgis"
});

OpenLayers.Control.WithinCostLocalgis.updateCheckIe = function(valor) {
	
		for(var x=0; x<2;x++)
		{
			document.getElementById("rdb"+x).src="img/rdb_0.gif";
		}
		document.getElementById("rdb"+valor).src="img/rdb_1.gif";
			
		document.forms.rutasForm.selRdb.value = valor;	
	
	
}
OpenLayers.Control.WithinCostLocalgis.calcular = function() {
var divButtonRutas = document.getElementById('divButtonRutas');
try{
	/*var optionSel = "";
	var ctrl = document.forms.rutasForm;
	 for(i=0;i<ctrl.length;i++)
        if(ctrl[i].checked) optionSel = ctrl[i].value;
	
	*/
	var optionSel = document.forms.rutasForm.selRdb.value;
	
	
		try{
			var valorCoste = document.forms.rutasForm.textoCoste.value;
			if( !isNaN(valorCoste) ) {
				valorCoste = parseFloat(valorCoste);
				calcularArea(arrayRoute[0].lonlat,valorCoste,optionSel);
				divButtonRutas.innerHTML = '<img src="'+OpenLayers.Util.getImagesLocation()+'ajax-loader.gif" alt="Calculando"/>';
			}else alert("Introduzca un valor númerico en el campo coste");
		}catch(e){divButtonRutas.innerHTML = '<img class="imageButton" src="img/btn_aceptar.gif" alt="Aceptar" onclick="OpenLayers.Control.WithinCostLocalgis.calcular();"/>';}
		
	
	
	
	
}
catch(e){
	divButtonRutas.innerHTML = '<img class="imageButton" src="img/btn_aceptar.gif" alt="Aceptar" onclick="OpenLayers.Control.WithinCostLocalgis.calcular();"/>';
}
}