$(document).ready(function(){
	//ajustar los campos del formulario en funcion del tipo de fuente
	$('#tipo').change(function() {
		switch ( $(this).val() ) {
			case '1': fuentes.mostrarInputsFicheros('shapefile',0,0);					  
					  break;
			case '2': fuentes.mostrarInputsFicheros('txt_info_conexion',0,0);
					  fuentes.mostrarMensajeAyuda('msg_WFS');
					  break;
			case '3': fuentes.mostrarInputsFicheros('csv_gml',0,0);
					  fuentes.mostrarMensajeAyuda('msg_GML');
					  break;
			case '4': fuentes.mostrarInputsFicheros('csv_gml',0,1);
					  fuentes.mostrarMensajeAyuda('msg_CSV');
					  break;
			case '5': fuentes.mostrarInputsFicheros('txt_info_conexion',0,0);
					  fuentes.mostrarMensajeAyuda('msg_ODBC');
					  break;
			case '6': fuentes.mostrarInputsFicheros('txt_info_conexion',1,0);
					  fuentes.mostrarMensajeAyuda('msg_PostGis');
					  break;
			case '7': fuentes.mostrarInputsFicheros('txt_info_conexion',1,0);
					  fuentes.mostrarMensajeAyuda('msg_MySQL');
					  break;
			case '8': fuentes.mostrarInputsFicheros('txt_info_conexion',1,0);
					  fuentes.mostrarMensajeAyuda('msg_Oracle');
					  break;
		    default : fuentes.mostrarInputsFicheros('txt_info_conexion',1,0);
					  break;
		}
	});
	switch ( $('#tipo').val() ) {
		case '1': fuentes.mostrarInputsFicheros('shapefile',0,0);
				  break;
		case '2': fuentes.mostrarInputsFicheros('txt_info_conexion',0,0);
		  		  fuentes.mostrarMensajeAyuda('msg_WFS');
				  break;
		case '3': fuentes.mostrarInputsFicheros('csv_gml',0,0);
				  fuentes.mostrarMensajeAyuda('msg_GML');
				  break;
		case '4': fuentes.mostrarInputsFicheros('csv_gml',0,1);
		  		  fuentes.mostrarMensajeAyuda('msg_CSV');
				  break;
		case '5': fuentes.mostrarInputsFicheros('txt_info_conexion',0,0);
				  fuentes.mostrarMensajeAyuda('msg_ODBC');
				  break;
		case '6': fuentes.mostrarInputsFicheros('txt_info_conexion',1,0);
		  		  fuentes.mostrarMensajeAyuda('msg_PostGis');
				  break;
		case '7': fuentes.mostrarInputsFicheros('txt_info_conexion',1,0);
				  fuentes.mostrarMensajeAyuda('msg_MySQL');
				  break;
		case '8': fuentes.mostrarInputsFicheros('txt_info_conexion',1,0);
				  fuentes.mostrarMensajeAyuda('msg_Oracle');
				  break;
	    default : fuentes.mostrarInputsFicheros('txt_info_conexion',1,0);
				  break;
	}
});

var fuentes = {		
		mostrarInputsFicheros: function(id, mostrarUserPass,mostrarCSV) {
			//Mostrar el formulario adecuado para cada tipo de fuente
			$('.conexion').removeClass("no_oculto").addClass("oculto");
			$('#'+id).removeClass("oculto").addClass("no_oculto");
			if ( mostrarUserPass == 1) {
				$('#user_pass').removeClass("oculto").addClass("no_oculto");
			} else {
				$('#user_pass').removeClass("no_oculto").addClass("oculto");
			}
			if(mostrarCSV==1){
				$('#csv').removeClass("oculto").addClass("no_oculto");
			}else{
				$('#csv').removeClass("no_oculto").addClass("oculto");
			}
		},
		mostrarMensajeAyuda:function(mensaje){
			$('.mensaje_ayuda').removeClass("no_oculto").addClass("oculto");
			$('#'+mensaje).removeClass("oculto").addClass("no_oculto");
		}
	};