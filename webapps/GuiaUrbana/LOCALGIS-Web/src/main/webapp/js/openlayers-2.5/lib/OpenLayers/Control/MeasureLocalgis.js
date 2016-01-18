/* Copyright (c) 2006 ASO crea este botón para hacer el zoomin */
/**
 * //CLASE AÑADIDA ÁLVARO 
* @class
*
* Imlements a very simple button control.
* @requires OpenLayers/Control.js
*/
OpenLayers.Control.MeasureLocalgis =OpenLayers.Class(OpenLayers.Control, {
type: OpenLayers.Control.TYPE_TOOL,

initialize: function() {
	OpenLayers.Control.prototype.initialize.apply(this, arguments);
	
},

activate: function() {
	var mimenu = document.getElementById("menuR");
	//mimenu.style.visibility = "hidden";
	try
	{
		document.getElementById("divMeasure").className="olControlMeasureLocalgisItemActive";

	}
	catch(e){alert(e);
	}
	measureControl = measureControls["line"];
	measureControl.activate();	
	
},
	
deactivate: function() {
	try
	{
		measureControl.deactivate();
		document.getElementById('resMeasure').style.display = "none";
		document.getElementById("divMeasure").className="olControlMeasureLocalgisItemInactive";
	}
	catch(e)
	{
	}
	},
/** @final @type String */
CLASS_NAME: "OpenLayers.Control.MeasureLocalgis"
}); 