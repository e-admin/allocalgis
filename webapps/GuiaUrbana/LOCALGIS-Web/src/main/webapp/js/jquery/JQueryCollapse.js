//Esto no consigo que funcione con OpenLayers asignando el evento directamente, lo tengo que hacer con una funcion
//de javascript on lcik
/*JQ(function()
{

JQ("#mostrar").click(function(event) {
	event.preventDefault();
	JQ("#caja").slideToggle();
});

JQ("#caja a").click(function(event) {
	event.preventDefault();
	JQ("#caja").slideUp();
});
});*/

function slideToggle(){
	JQ("#ficha_datos_basicos_feature").slideToggle();
}

function slideToggleLayer(ficha_layer){
	
	JQ("#"+ficha_layer).slideToggle();
	
	
}
