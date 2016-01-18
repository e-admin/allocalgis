var cambio = false;
$(document).ready(function(){
	$("input,select,textarea").change(function() {cambio = true;});
});
function cambioMenu(url) {
	console.log(url);
	console.log(cambio);
	if ( cambio ) {
		var dialogoModalAviso = $("#dialogoModalAviso").dialog({
		    autoOpen: false,
		    show: "blind",
		    hide: "explode",                    
		    width: 400,
		    height: 200,
		    modal: true,
		    resizable: false,
		    buttons: {
		        "Aceptar": function() {
		            $(this).dialog( "close" );
		            return false;            
		        }
		    }            
		});
		$(dialogoModalAviso).dialog("open");
		return false;
	} else
		document.location.href=url;
}