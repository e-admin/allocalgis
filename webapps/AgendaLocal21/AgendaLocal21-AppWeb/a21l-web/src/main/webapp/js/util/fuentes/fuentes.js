var idFuente;
var idTipoFuente;
$(document).ready(function(){
	$('.filaFuente').click(function() {
		$('.filaFuente td').removeClass("fondoGris");
		idFuente = $(this).attr("data-idfuente");
		idTipoFuente = $(this).attr("data-tipo");
		$(this).find("td").addClass("fondoGris");
	});
});

var fuentes = {
		borrarElemento: function() {
			if ( idFuente == null) {
				alert("Tiene que seleccionar una fuente");
				return false;
			} else {
				dialogoModalEliminar = $("#dialogoModalEliminar").dialog({
				    autoOpen: false,
				    show: "blind",
				    hide: "explode",                    
				    width: 400,
				    height: 200,
				    modal: true,
				    resizable: false,
				    buttons: {
				        "Aceptar": function() {
				        	document.location.href="fuentes.htm?accion=borrar&id="+idFuente+"&tipo="+idTipoFuente;
				        	return true;
				        },
				        "Cancelar": function() {
				            $(this).dialog( "close" );
				            return false;
				        }
				    }            
				});
				$(dialogoModalEliminar).dialog("open");
			}
		},

		verTablasFuente: function() {
			if ( idFuente == null) {
				alert("Tiene que seleccionar una fuente");
				return false;
			} else {
				document.location.href="fuentes.htm?accion=verTablas&id="+idFuente+"&tipo="+idTipoFuente+"&cat=0";
			}
		},
		modificar : function()  {
			if ( idFuente == null) {
				alert("Tiene que seleccionar una fuente");
				return false;
			} else {
				document.location.href="fuentes.htm?accion=edita&id="+idFuente;
				return true;
			}
		}
	};