/* Copyright (c) 2006 ASO crea este botón para hacer el zoomin */
//CLASE AÑADIDA ÁLVARO 
/**
* @class
*
* Imlements a very simple button control.
* @requires OpenLayers/Control.js
*/
OpenLayers.Control.MeasureAreaLocalgis =OpenLayers.Class(OpenLayers.Control, {
type: OpenLayers.Control.TYPE_TOOL,

initialize: function() {
	OpenLayers.Control.prototype.initialize.apply(this, arguments);
},

activate: function() {
	var mimenu = document.getElementById("menuR");
	//mimenu.style.visibility = "hidden";
		//Aqui esta el problema de que no pilla la clase para cambiar a activado
		document.getElementById("divMeasureArea").className="olControlMeasureAreaLocalgisItemActive";
		measureAreaControl = measureControls["polygon"];
		measureAreaControl.activate();
	},
	
deactivate: function() {
	try
	{
		measureAreaControl.deactivate();
		document.getElementById('resMeasure').style.display = "none";
		document.getElementById("divMeasureArea").className="olControlMeasureAreaLocalgisItemInactive";
	}
	catch(e)
	{
	}
},
/** @final @type String */
CLASS_NAME: "OpenLayers.Control.MeasureAreaLocalgis"
});
