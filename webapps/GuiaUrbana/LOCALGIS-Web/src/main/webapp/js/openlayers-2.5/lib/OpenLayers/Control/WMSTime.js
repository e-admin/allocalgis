/* Copyright (c) 2006 ASO crea este botón para hacer el zoomin */

/**
 * @class
 *
 * Imlements a very simple button control.
 * @requires OpenLayers/Control.js
 */
 
OpenLayers.Control.WMSTime  = 
  OpenLayers.Class(OpenLayers.Control, {
    /** @type OpenLayers.Control.TYPE_* */
    type: OpenLayers.Control.TYPE_BUTTON,
 
    /** 
     * Property: targetCRSCode 
     * {String} 
     */
    
    
   
    initialize: function() {
        OpenLayers.Control.prototype.initialize.apply(this, arguments);
      
    },
    
    /**
     * Evento de click en el control
     */
    trigger: function() {
    	
		

    	 var contentHTML;
       
             contentHTML = '<form name="wmstForm">';
             contentHTML += '<table border="0" width="100%" style="margin-left:auto;margin-right:auto;text-align:center" cellpadding="3">';
             contentHTML += '<tr><td colspan=3 align="left">Seleccione la fecha en la cual quiere visualizar el histórico de datos:</td></tr>';
             contentHTML += '<tr>';
             contentHTML += '<td align="left">Seleccione una fecha:</td>';
             contentHTML += '<td align="left">';
             contentHTML += '<div style="position:relative;width: 130px">';
             contentHTML += '<input type="hidden" name="noFormat" id="noFormat"/><input type="text" class="inputTextField" id="calInput1" style="border-width:0px; width: 130px; readonly="true" disabled>';
             contentHTML += '</td><td align="left"><img style="cursor:pointer;" onClick="OpenLayers.Control.WMSTime.showCalendar()" src="js/dhtmlxCalendar/imgs/calendar.gif" align="top"><div id="calendar1" style="position:absolute;z-index:3000; right:10px; top:25px; display:none"></div></div>';

             contentHTML += '</td>';
             contentHTML += '</tr>';
             contentHTML += '<tr>';
             contentHTML += '<td align="left" colspan="3"><label><input type="checkbox" id="rstHour" name="rstHour" onclick="OpenLayers.Control.WMSTime.activateClock();"/> <span style="position: relative; top: -4px;">Con restricción horaria</span></label></td>';
             contentHTML += '</tr>';
             
             contentHTML += '<tr>';
             contentHTML += '<td colspan="3" align="left">';
             contentHTML += '<select name="hora" disabled>';
             for(var z=0; z<=23;z++)
             {
            	 var hora = z+"";
            	 if(hora.length == 1)hora = "0"+hora;
            	 contentHTML += '<option value="'+hora+'">'+hora+'</option>';
             }
             
             contentHTML += '</select>horas';
             contentHTML += '<select name="minutos" disabled>';
             for(var z=0; z<=59;z++)
             {
            	 var minuto = z+"";
            	 if(minuto.length == 1)minuto = "0"+minuto;
            	 contentHTML += '<option value="'+minuto+'">'+minuto+'</option>';
             }
             
             contentHTML += '</select>minutos';
             contentHTML += '<select name="segundos" disabled>';
             for(var z=0; z<=59;z++)
             {
            	 var segundos = z+"";
            	 if(segundos.length == 1)segundos = "0"+segundos;
            	 contentHTML += '<option value="'+segundos+'">'+segundos+'</option>';
             }
             
             contentHTML += '</select>segundos';
             contentHTML += '</td>';
             contentHTML += '</tr>';
             
             contentHTML += '<tr>';
             contentHTML += '<td align="center" colspan="2"><div id="divButtonSetTimeWMS" style="margin-top: 6px;"><img class="imageButton" src="img/btn_aceptar.gif" alt="Aceptar" onclick="OpenLayers.Control.WMSTime.sendRequest();"/></div>';
             contentHTML += '</tr>';
             contentHTML += '</table>';
             contentHTML += '</form>';
        
         OpenLayers.LocalgisUtils.showPopupBig(contentHTML);
         cal1 = new dhtmlxCalendarObject('calendar1');
 		cal1.setOnClickHandler(OpenLayers.Control.WMSTime.selectDate1);
    },

    /** @final @type String */
    CLASS_NAME: "OpenLayers.Control.WMSTime"
});
OpenLayers.Control.WMSTime.showCalendar = function() {
	
	document.getElementById('calendar1').style.display = 'block';
}
OpenLayers.Control.WMSTime.selectDate1 = function(date) {
	var dateformat = "%d-%m-%Y";
	
	document.getElementById('calInput1').value = cal1.getFormatedDate(dateformat,date);
	document.getElementById('noFormat').value = cal1.getFormatedDate(null,date);
	document.getElementById('calendar1').style.display = 'none';
	//dateFrom = new Date(date);
	//mCal.setSensitive(dateFrom, dateTo);
	return true;
}
OpenLayers.Control.WMSTime.activateClock = function() {
	
	if(document.getElementById("rstHour").checked==true)
	{
		document.forms.wmstForm.hora.disabled=false;
		document.forms.wmstForm.minutos.disabled=false;
		document.forms.wmstForm.segundos.disabled=false;

	}
	else
	{
		document.forms.wmstForm.hora.disabled=true;
		document.forms.wmstForm.minutos.disabled=true;
		document.forms.wmstForm.segundos.disabled=true;
	}
}

OpenLayers.Control.WMSTime.sendRequest= function() {
	
	
	if(document.getElementById("calInput1").value!="")
	{
		if(document.getElementById("noFormat").value != "")
		{
			var fecha = document.getElementById("noFormat").value;
			if (fecha >="2010-01-01"){
				if(document.getElementById("rstHour").checked==true)
				{
					fecha = fecha + "T"+document.forms.wmstForm.hora.value+":"+document.forms.wmstForm.minutos.value+":"+document.forms.wmstForm.segundos.value;
				}
				
				for(var x = 0; x < map.layers.length; x++)
				{
					if((map.layers[x].CLASS_NAME == "OpenLayers.Layer.WMSLocalgis")&&(!map.layers[x].isExternal))
					{
						map.layers[x].mergeNewParams({'time':fecha});
	
						
					}
					
				}
 			    map.removeAllPopups();
			 }else
				 alert("El versionado ha sido realizado en una fecha posterior.")
		}
		else
		{
			alert("No se ha podido realizar la petición. Seleccione otra fecha.")
		}
		
	}
	else
	{
		alert("Seleccione una fecha");
	}
	/*if(document.getElementById("rstHour").checked==true)
	{
	}*/
}

    
