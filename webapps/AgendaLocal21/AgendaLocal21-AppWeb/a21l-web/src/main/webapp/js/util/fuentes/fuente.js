$(document).ready(function(){
	//ajustar los campos del formulario en funcion del tipo de fuente
	$('#tipo').change(function() {
		switch ( $(this).val() ) {
			case '1': fuentes.mostrarInputsFicheros('shapefile',0);
					  break;
			case '2': fuentes.mostrarInputsFicheros('txt_info_conexion',0);
					  break;
			case '3': fuentes.mostrarInputsFicheros('csv_gml',0);
					  break;
			case '4': fuentes.mostrarInputsFicheros('csv_gml',0);
					  break;
			case '5': fuentes.mostrarInputsFicheros('txt_info_conexion',0);
					  break;
			case '6': fuentes.mostrarInputsFicheros('txt_info_conexion',1);
					  break;
		    default : fuentes.mostrarInputsFicheros('txt_info_conexion',1);
					  break;
		}
	});
	switch ( $('#tipo').val() ) {
		case '1': fuentes.mostrarInputsFicheros('shapefile',0);
				  break;
		case '2': fuentes.mostrarInputsFicheros('txt_info_conexion',0);
				  break;
		case '3': fuentes.mostrarInputsFicheros('csv_gml',0);
				  break;
		case '4': fuentes.mostrarInputsFicheros('csv_gml',0);
				  break;
		case '5': fuentes.mostrarInputsFicheros('txt_info_conexion',0);
				  break;
		case '6': fuentes.mostrarInputsFicheros('txt_info_conexion',1);
				  break;
	    default : fuentes.mostrarInputsFicheros('txt_info_conexion',1);
				  break;
	}
});

var fuentes = {		
		mostrarInputsFicheros: function(id, mostrarUserPass) {
			//Mostrar el formulario adecuado para cada tipo de fuente
			$('.conexion').removeClass("no_oculto").addClass("oculto");
			$('#'+id).removeClass("oculto").addClass("no_oculto");
			if ( mostrarUserPass == 1) {
				$('#user_pass').removeClass("oculto").addClass("no_oculto");
			} else {
				$('#user_pass').removeClass("no_oculto").addClass("oculto");
			}
		}
	};