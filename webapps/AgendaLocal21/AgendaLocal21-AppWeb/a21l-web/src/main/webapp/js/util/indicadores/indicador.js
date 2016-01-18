var tablaSeleccionada = "";
var columnaSeleccionada = { tabla: "", columna : "", tipo : "" };
var columnaAmbito = { tabla: "", columna : "", tipo : "" };
var columnaMapa = { tabla: "", columna : "", tipo : "" };

$(document).ready(function() {
	//Seleccionar FUENTE
	$('#fuente_selec').change(function() {indicadores.cargarTablasFuente($('#fuente_selec').val())});
	
	//Anhadir tabla
	$('#anhadir_tabla').click(function() {
		if ( $('#tabla_fuente_selec').val()<=0 ) {
			alert("Seleccione alguna tabla");
		} else {
			if ( indicadores.comprobarSiExisteTabla('tabla_'+$('#tabla_fuente_selec').val()))
				alert("La tabla: "+$('#tabla_fuente_selec').val()+" ya está seleccionada");
			else {
				indicadores.cargarColumnasFuente($('#fuente_selec').val(),$('#tabla_fuente_selec').val());
			}
		}
		return false;
	});
	
	//Seleccionar TABLA
	$('.cabeceraTabla').live("click",function() {
		//deseleccionar tabla
		$('.cabeceraTabla').removeClass("tabla_seleccionada");
		//Seleccionar esta tabla
		$(this).addClass("tabla_seleccionada");
		tablaSeleccionada = $(this).attr("data-tabla");
	});
	
	//Seleccionar COLUMNA
	$('.columnaTabla').live("click",function() {
		//deseleccionar otras columnas
		//var idTabla = $(this).parent().parent().attr("id");
		$(".columnaTabla").removeClass("columna_seleccionada");
		//Seleccionar esta columna
		$(this).addClass("columna_seleccionada");
		columnaSeleccionada = { tabla: "", columna : "", tipo : "" };
		columnaSeleccionada.tabla = $(this).attr("data-tabla");
		columnaSeleccionada.columna = $(this).attr("data-columna");
		columnaSeleccionada.tipo = $(this).attr("data-tipo");
	});
	
	//Seleccionar COLUMNA MAPA
	$('#marcar_mapa').click(function() {
		if ( columnaSeleccionada.tabla == "" && columnaSeleccionada.columna == "" && columnaSeleccionada.tipo == "") {
			alert("No ha seleccionado ninguna columna para marcarla como mapa");
			return false;
		}
		if ( columnaSeleccionada.tipo != 'VALORFDGEOGRAFICO') {
			alert("Para seleccionar una columna como mapa su tipo debe ser geográfico.");
			return false;
		}
		columnaMapa = columnaSeleccionada;
		$('#columna_marcada_mapa').html("<div><label>Tabla:</label><span>"+columnaMapa.tabla.substring("tabla_".length)+"</span></div>"+
									"<div><label>Columna:</label><span><strong>"+columnaMapa.columna+"</strong></span></div>");
		$('#columna_marcada_mapa').fadeIn('slow',function() {$('#columna_marcada_mapa').removeClass("oculto");});
		$('#marcar_mapa').addClass("oculto");
		$('#desmarcar_mapa').removeClass("oculto");
		$('#tit_mapa').fadeIn('slow',function() {$('#tit_mapa').removeClass("oculto");});
		form = "<input type='hidden' name='columna_campo_mapa' value='"+columnaMapa.columna+"'/>";
		form += "<input type='hidden' name='tabla_campo_mapa' value='"+columnaMapa.tabla.substring("tabla_".length)+"'/>";
		$('#campo_mapa').html(form);
		
		//Al marcar una columna como mapa se añade a la tabla de campos a mostrar
		indicadores.numCampos++;
		var html = "<tr id='"+columnaMapa.tabla.substring("tabla_".length)+"||"+columnaMapa.columna+"' class='' onmouseover='this.className=\"destacadoTablas\"' onmouseout='this.className=\"\"'>";
		var htmlMostrar = "";
		var form = "";
		html += "<td><input name='marcar_campos_"+indicadores.numCampos+"' type='checkbox' class='filasTablaFinal campo'/></td>";
		div = "campos_envio";
		html += "<td>"+columnaMapa.tabla.substring("tabla_".length)+"</td>";
		html += "<td>"+columnaMapa.columna+"</td>";
		htmlMostrar = "<td class='centro'><input class='check_mostrar' type='checkbox' id='check_mostrar_campo_"+indicadores.numCampos+"'/>";
		form = "<input type='hidden' name='tabla_campo_"+indicadores.numCampos+"' id='tabla_campo_"+indicadores.numCampos+"' value='"+columnaMapa.tabla.substring("tabla_".length)+"'/>";
		form += "<input type='hidden' name='columna_campo_"+indicadores.numCampos+"' id='columna_campo_"+indicadores.numCampos+"' value='"+columnaMapa.columna+"'/>";
		form += "<input type='hidden' name='orden_campo_"+indicadores.numCampos+"' id='orden_campo_"+indicadores.numCampos+"' value='0'/>";
		form += "<input type='hidden' name='mostrar_campo_"+indicadores.numCampos+"' id='mostrar_campo_"+indicadores.numCampos+"'/>";
		form += "<input type='hidden' name='idfuente_campo_"+indicadores.numCampos+"' id='idfuente_campo_"+indicadores.numCampos+"' value='"+$("[id='"+columnaMapa.tabla+"']").attr("data-idfuente")+"'/>";
		$('#num_campos_envio').val(indicadores.numCampos);
		
		html += "<td class='centro'>0</td>";
		html += htmlMostrar;
		html += "<td class='centro'>---</td></tr>";
		$('#tabla_campos_mostrar').append(html);
		//Componer formulario
		$('#'+div).append(form);
		
		return false;
	});	
	
	//DESeleccionar COLUMNA MAPA
	$('#desmarcar_mapa').click(function() {
		if ( columnaMapa.tabla == "" && columnaMapa.columna == "" && columnaMapa.tipo == "") {
			alert("No ha seleccionado ninguna columna como mapa, así que no es posible desmarcarla");
			return false;
		}
		$('#columna_marcada_mapa').html("");
		$('#columna_marcada_mapa').fadeOut('slow',function() {$('#columna_marcada_mapa').addClass("oculto");});
		$('#columna_marcada_mapa').addClass("oculto");
		$('#marcar_mapa').removeClass("oculto");
		$('#desmarcar_mapa').addClass("oculto");
		$('#tit_mapa').fadeOut('slow',function() {$('#tit_mapa').addClass("oculto");});
		$('#campo_mapa').html("");
		
		for ( var i=1;i<=indicadores.numCampos;i++) {
			if ( $('#tabla_campo_'+i).val()==columnaMapa.tabla.substring("tabla_".length) && $('#columna_campo_'+i).val()==columnaMapa.columna ) {
				$('#tabla_campo_'+i).remove();
				$('#columna_campo_'+i).remove();
				$('#orden_campo_'+i).remove();
				$('#mostrar_campo_'+i).remove();							
				$('#idfuente_campo_'+i).remove();
				$("[id='"+columnaMapa.tabla.substring("tabla_".length)+"||"+columnaMapa.columna+"']").fadeOut('slow',function() { $(this).remove();});
			}	
		}
		columnaMapa = { tabla: "", columna : "", tipo : "" };
		return false;
	});	
	
	//Seleccionar COLUMNA ÁMBITO
	$('#marcar_ambito').click(function() {
		if ( columnaSeleccionada.tabla == "" && columnaSeleccionada.columna == "" && columnaSeleccionada.tipo == "") {
			alert("No ha seleccionado ninguna columna para marcarla como ámbito");
			return false;
		}
		if ( columnaSeleccionada.tipo == "VALORFDGEOGRAFICO" || columnaSeleccionada.tipo == "VALORFDRELACION") {
			alert("Las relaciones y columnas geográficas no se puede utilizar como ámbito");
			return false;
		}
		columnaAmbito = columnaSeleccionada;
		$('#columna_marcada_ambito').html("<div><label>Tabla:</label><span>"+columnaAmbito.tabla.substring("tabla_".length)+"</span></div>"+
									"<div><label>Columna:</label><span><strong>"+columnaAmbito.columna+"</strong></span></div>");
		$('#columna_marcada_ambito').fadeIn('slow',function() {$('#columna_marcada_ambito').removeClass("oculto");});
		$('#tit_ambito').fadeIn('slow',function() {$('#tit_ambito').removeClass("oculto");});
		form = "<input type='hidden' id='columna_campo_ambito' name='columna_campo_ambito' value='"+columnaAmbito.columna+"'/>";
		form += "<input type='hidden' id='tabla_campo_ambito' name='tabla_campo_ambito' value='"+columnaAmbito.tabla.substring("tabla_".length)+"'/>";
		$('#campo_ambito').html("");
		$('#campo_ambito').html(form);
		
		//Al marcar una columna como ámbito se añade a la tabla de campos a mostrar si es que no existe ya
		if ( !indicadores.existeCampoEnTablaFinal(columnaAmbito.tabla.substring("tabla_".length), columnaAmbito.columna)) {	
			indicadores.numCampos++;
			var html = "<tr id='"+columnaAmbito.tabla.substring("tabla_".length)+"||"+columnaAmbito.columna+"' class='' onmouseover='this.className=\"destacadoTablas\"' onmouseout='this.className=\"\"'>";
			var htmlMostrar = "";
			var form = "";
			html += "<td><input name='marcar_campos_"+indicadores.numCampos+"' type='checkbox' class='filasTablaFinal campo'/></td>";
			div = "campos_envio";
			html += "<td>"+columnaAmbito.tabla.substring("tabla_".length)+"</td>";
			html += "<td>"+columnaAmbito.columna+"</td>";
			htmlMostrar = "<td class='centro'><input class='check_mostrar' checked type='checkbox' id='check_mostrar_campo_"+indicadores.numCampos+"'/>";
			form = "<input type='hidden' name='tabla_campo_"+indicadores.numCampos+"' id='tabla_campo_"+indicadores.numCampos+"' value='"+columnaAmbito.tabla.substring("tabla_".length)+"'/>";
			form += "<input type='hidden' name='columna_campo_"+indicadores.numCampos+"' id='columna_campo_"+indicadores.numCampos+"' value='"+columnaAmbito.columna+"'/>";
			form += "<input type='hidden' name='orden_campo_"+indicadores.numCampos+"' id='orden_campo_"+indicadores.numCampos+"' value='0'/>";
			form += "<input type='hidden' name='mostrar_campo_"+indicadores.numCampos+"' id='mostrar_campo_"+indicadores.numCampos+"' value='true'/>";
			form += "<input type='hidden' name='idfuente_campo_"+indicadores.numCampos+"' id='idfuente_campo_"+indicadores.numCampos+"' value='"+$("[id='"+columnaAmbito.tabla+"']").attr("data-idfuente")+"'/>";
			$('#num_campos_envio').val(indicadores.numCampos);
			
			html += "<td class='centro orden'>0</td>";
			html += htmlMostrar;
			if ( columnaAmbito.tipo != "VALORFDNUMERICO")
				html += "<td class='centro'>---</td></tr>";
			else
				html += "<td class='centro'><a href='#' onclick='return false;' class='criterio fizq' data-idfuente='"+$("[id='"+columnaAmbito.tabla+"']").attr("data-idfuente")+"' data-tabla='"+columnaAmbito.tabla.substring("tabla_".length)+"' data-columna='"+columnaAmbito.columna+"'><span class='ui-icon ui-icon-circle-plus'></span></a></td></tr>";
			$('#tabla_campos_mostrar').append(html);
			//Componer formulario
			$('#'+div).append(form);
		}
		
		return false;
	});	
	
	//Borrar tabla
	$('#borrar_tabla').click(function() {
		if ( tablaSeleccionada == "" )
			alert("No tiene ninguna tabla seleccionada");
		else {
			if ( confirm("Está seguro de eliminar la tabla seleccionada?") ) {
				$("[id='"+tablaSeleccionada+"']").parent().fadeOut('slow', 
						function() { 
							$(this).remove();
							if ($('#zona_columnas').children().length == 0 && !$('#zona_tablas').hasClass("oculto")) {
							   $('#zona_tablas').addClass("oculto");
							}
						});
				$('#tabla_campos_mostrar tr').each(function() {
					var tablaNombre = $(this).attr("id").split("||");
					
					//Borramos los inputs ocultos
					$('#campos_envio input').each(function(){
						if ( $(this).val() == tablaSeleccionada.substring("tabla_".length)) {
							var num = $(this).attr("name").substring("tabla_campo_".length);
							$('#tabla_campo_'+num).remove();
							$('#columna_campo_'+num).remove();
							$('#orden_campo_'+num).remove();
							$('#mostrar_campo_'+num).remove();							
							$('#idfuente_campo_'+num).remove();
						}
					});
					$('#formulas_envio input').each(function(){
						if ( $(this).val().indexOf(tablaSeleccionada.substring("tabla_".length))!=-1) {
							var num = $(this).attr("name").substring("nombre_formula_".length);
							$('#nombre_formula_'+num).remove();
							$('#valor_formula_'+num).remove();
							$('#orden_formula_'+num).remove();
							$('#mostrar_formula_'+num).remove();							
						}
					});
					$('#relaciones_envio input').each(function(){
						if ( $(this).val().indexOf(tablaSeleccionada.substring("tabla_".length))!=-1) {
							var num = $(this).attr("name").substring("tabla_relacion_".length);
							$('#tabla_relacion_'+num).remove();
							$('#columna_relacion_'+num).remove();
							$('#orden_relacion_'+num).remove();
							$('#mostrar_relacion_'+num).remove();
							$('#tabla_relacionada_'+num).remove();
							$('#columna_relacionada_'+num).remove();	
							$('#idfuente_relacionada_'+num).remove();
							$('#idfuente_relacion_'+num).remove();							
						}
					});
					
					$('#criterios_envio input').each(function(){
						if ( $(this).val() == tablaSeleccionada.substring("tabla_".length)) {
							var num = $(this).attr("name").substring("tabla_criterio_".length);
							console.log(num);
							$('#columna_criterio_'+num).remove();
							$('#tabla_criterio_'+num).remove();
							$('#valor_criterio_'+num).remove();
						}
					});
					
					//Eliminamos las filas
					if ( tablaNombre[0] == tablaSeleccionada.substring("tabla_".length))
						$(this).fadeOut('slow',function() {$(this).remove();});
						//Si ademas la columna ambito pertenece a esta tabla. Se elimina el ambito
						if ( tablaNombre[0] == columnaAmbito.tabla.substring("tabla_".length) ) {
							$('#campo_ambito').html("");
							$('#columna_marcada_ambito').fadeOut();
							$('#tit_ambito').fadeOut();
							columnaAmbito = { tabla: "", columna : "", tipo : "" };
						}
						//Si ademas la columna mapa pertenece a esta tabla. Se elimina el mapa
						if ( tablaNombre[0] == columnaMapa.tabla ) {
							$('#campo_mapa').html("");
							$('#columna_marcada_mapa').fadeOut();
							$('#tit_mapa').fadeOut();
							columnaMapa = { tabla: "", columna : "", tipo : "" };
						}
				});
			}
		}
		tablaSeleccionada = "";
		return false;
	});
	
	//Borrar relacion
	$('#borrar_relacion').click(function() {
		if ( columnaSeleccionada.tabla == "" && columnaSeleccionada.columna == "" ) {
			alert("No ha seleccionado ninguna columna");
			return false;
		}
		console.log($('.columna_seleccionada').attr("data-tipo"));
		if ( $('.columna_seleccionada').attr("data-tipo")!="VALORFDRELACION" ) {
			alert("La columna seleccionada no es una relación entre tablas");
			return false;
		}
		if ( confirm("Está seguro de borrar el elemento?")) {
			$('.columna_seleccionada').fadeOut('slow',function() {
				$(this).remove();
			});
		}
	});
	
	//Añadir Relacion
	$('#anhadir_relacion').click(function() {	
		if ( columnaSeleccionada.tabla == "" && columnaSeleccionada.columna == "" ) {
			alert("No ha seleccionado ninguna columna");
			return false;
		}
		if ( $('#zona_columnas table').length == 1) {
			alert("No puede relacionar una tabla consigo misma");
			return false;
		}
		if ( columnaSeleccionada.tipo == 'VALORFDRELACION') {
			alert("No puede relacionar un campo ya relacionado");
			return false;
		}
		
		$('#col_rel').html("<strong>"+columnaSeleccionada.columna+"</strong>");
		indicadores.rellenarTablasRelacion(columnaSeleccionada,"rel_tablas","rel_columnas");
		
		var dialogoModalRelacion= $("#dialogoModalRelacion").dialog({
		    autoOpen: false,
		    show: "blind",
		    hide: "explode",                    
		    width: 600,
		    height: 275,
		    modal: true,
		    resizable: false,
		    title: "Añadir Relación",
		    buttons: {
		        "Aceptar": function() {
		            tablaOriginal = columnaSeleccionada.tabla;
		            tabla = $('#rel_tablas').val();
		            col   = $('#rel_columnas').val();
		            if ( !indicadores.existeColumnaTabla(tabla,columnaSeleccionada.columna)) {
		    			alert("No se pueden relacionar estos campos ya que la tabla relacionada no contiene el campo: "+ columnaSeleccionada.columna);
		    		} else {
		    			$("[id='"+tablaOriginal+"']").append('<tr data-tabla="'+tablaOriginal+'"'+
		            									 'data-columna="'+columnaSeleccionada.columna+'" data-tipo-columna-relacionada="'+indicadores.obtenerTipoColumna(tabla,col)+'" data-tipo="VALORFDRELACION" data-tabla-rel="'+tabla+'" data-col-rel="'+col+'"' + 
		            									 'class="columnaTabla columnaTablaRelacion"><td>'+columnaSeleccionada.columna+'>'+tabla+'.'+col+'</td></tr>');
		    		}
		            $(this).dialog( "close" );
		            return false;            
		        },
		         "Cancelar": function() {
		            $(this).dialog( "close" );
		            return false;            
		        }
		    }            
		});
		$(dialogoModalRelacion).dialog("open");
		return false;
	});
	
	//Cambio de tabla en añadir relacion
	$('#rel_tablas').change(function() {
		$('#rel_columnas').html("");
		$("[id='tabla_"+$(this).val()+"'] tr td").each(function(){
			var nombreCol = $(this).html();
			var obj = new Option(nombreCol,nombreCol);
			$(obj).html(nombreCol);
			$("#rel_columnas").append(obj);
		});
	});
	
	
	//Ayuda de la página de indicadores
	$('#boton_ayuda').click(function() {
		var dialogoModalAyuda = $("#dialogoModalAyuda").dialog({
		    autoOpen: false,
		    show: "blind",
		    hide: "explode",                    
		    width: 600,
		    height: 475,
		    modal: true,
		    resizable: false,
		    title: "Ayuda",
		    buttons: {
		        "Aceptar": function() {
		            $(this).dialog( "close" );
		            return false;            
		        }
		    }            
		});
		$(dialogoModalAyuda).dialog("open");
		return false;
	});
	
	//Añadir CAMPO
	$('#anhadir_campo').click(function() {	
		if ( $('#zona_columnas table').length == 0) {
			alert("No ha seleccionado ninguna tabla");
			return false;
		}
		
		$('#orden_campos').val("");
		
		indicadores.rellenarTablas(columnaSeleccionada,"tablas_campos","columnas_campos");
		var dialogoModalNuevoCampo = $("#dialogoModalNuevoCampo").dialog({
		    autoOpen: false,
		    show: "blind",
		    hide: "explode",                    
		    width: 675,
		    height: 275,
		    modal: true,
		    resizable: false,
		    title: "Añadir campo",
		    buttons: {
		        "Aceptar": function() {
		            if ( indicadores.anhadirCampoTablaFinal())
		            	$(this).dialog( "close" );
		            return false;            
		           
		        },
		         "Cancelar": function() {
		            $(this).dialog( "close" );
		            return false;            
		        }
		    }            
		});
		$(dialogoModalNuevoCampo).dialog("open");
		return false;
	});
	
	//Cambio de tabla en añadir campo
	$('#tablas_campos').change(function() {
		$('#columnas_campos').removeClass("oculto");
		$('#label_columnas_campos').removeClass("oculto");
		
		$('#columnas_campos').html("");
		var nombreTabla = $(this).val();
		$("[id='tabla_"+$(this).val()+"'] tr td").each(function(){
			var nombreCol = $(this).html();
			if ( !indicadores.existeCampoEnTablaFinal(nombreTabla,nombreCol) ) {
				var obj = new Option(nombreCol,nombreCol);
				$(obj).html(nombreCol);
				if ( $(this).parent().attr("data-tipo")=="VALORFDRELACION") {
					$(obj).attr("data-tipo","VALORFDRELACION");
					$(obj).attr("data-col-rel",$(this).parent().attr("data-col-rel"));
					$(obj).attr("data-tabla-rel",$(this).parent().attr("data-tabla-rel"));
					$(obj).attr("data-col",$(this).parent().attr("data-columna"));
					$(obj).attr("data-tabla",$(this).parent().attr("data-tabla"));
				} else
					$(obj).attr("data-tipo","VALORFDNORMAL");
				$("#columnas_campos").append(obj);
			}
		});
	});
	
	//Añadir CAMPO CON FORMULA
	$('#anhadir_formula').click(function() {	
		if ( $('#zona_columnas table').length == 0) {
			alert("No ha seleccionado ninguna tabla");
			return false;
		}
		
		indicadores.rellenarCamposParaFormula();
		$('#nombre_formula').val("");
		$('#valor_formula').val("");
		$('#orden_formula').val("");
		
		var dialogoModalNuevoCampoFormula = $("#dialogoModalNuevoCampoFormula").dialog({
		    autoOpen: false,
		    show: "blind",
		    hide: "explode",                    
		    width: 675,
		    height: 375,
		    modal: true,
		    resizable: false,
		    title: "Añadir fórmula",
		    buttons: {
		        "Aceptar": function() {
		            if ( !indicadores.validarFormula() )
		            	return false;
		            else {
			            indicadores.anhadirFormulaTablaFinal();
		            	$(this).dialog( "close" );
			            return false;
		            }		           
		        },
		         "Cancelar": function() {
		            $(this).dialog( "close" );
		            return false;            
		        }
		    }            
		});
		$(dialogoModalNuevoCampoFormula).dialog("open");
		return false;
	});
	
	//Anhadir campo al valor de la formula
	$('#a_anhadir_campo').click(function() {
		$('#valor_formula').val($('#valor_formula').val()+$('#anhadir_campo_formula').val());
		return false;
	});
	
	//QUITAR CAMPOS TABLA
	$('#quitar_campos').click(function() {
		$('.filasTablaFinal:checked').each(function() {
			//Quitar los inputs ocultos del formulario
			var num;
			if ( $(this).hasClass("campo")) {
				num = $(this).attr("name").substring('marcar_campos_'.length);
				//Eliminar el ambito si era la columna de ambito
				if ( $('#tabla_campo_'+num).val()==columnaAmbito.tabla.substring("tabla_".length) && $('#columna_campo_'+num).val()==columnaAmbito.columna ) {
					$('#campo_ambito').html("");
					$('#columna_marcada_ambito').fadeOut();
					$('#tit_ambito').fadeOut();
					columnaAmbito = { tabla: "", columna : "", tipo : "" };
				}					
				$('#tabla_campo_'+num).remove();
				$('#columna_campo_'+num).remove();
				$('#orden_campo_'+num).remove();
				$('#mostrar_campo_'+num).remove();
				$('#idfuente_campo_'+num).remove();
				//Retiro los criterios de este campo
				var numCriterio = 0;
				$(this).parent().parent().find(".criterio_anhadido").each(function() {
					numCriterio = $(this).attr("data-num-criterio");
				});
				$('#columna_criterio_'+numCriterio).remove();
				$('#tabla_criterio_'+numCriterio).remove();
				$('#valor_criterio_'+numCriterio).remove();				
				
			} else if ( $(this).hasClass("formula")) {
				num = $(this).attr("name").substring('marcar_formulas_'.length);
				$('#nombre_formula_'+num).remove();
				$('#valor_formula_'+num).remove();
				$('#orden_formula_'+num).remove();
				$('#mostrar_formula_'+num).remove();
				//Retiro los criterios de esta formula
				var numCriterio = 0;
				$(this).parent().parent().find(".criterio_anhadido").each(function() {
					numCriterio = $(this).attr("data-num-criterio");
				});
				$('#columna_criterio_'+numCriterio).remove();
				$('#tabla_criterio_'+numCriterio).remove();
				$('#valor_criterio_'+numCriterio).remove();
			} else if ( $(this).hasClass("relacion")) {
				num = $(this).attr("name").substring('marcar_relaciones_'.length);
				$('#tabla_relacion_'+num).remove();
				$('#columna_relacion_'+num).remove();
				$('#orden_relacion_'+num).remove();
				$('#mostrar_relacion_'+num).remove();
				$('#tabla_relacionada_'+num).remove();
				$('#columna_relacionada_'+num).remove();
				$('#idfuente_relacion_'+num).remove();
				$('#idfuente_relacionada_'+num).remove();
			}
			//eliminar la fila
			$(this).parent().parent().fadeOut('slow',function() {$(this).remove();});
		});
		if ( $('.filasTablaFinal:checked').length <= 0 )
			alert("Marque alguna fila para quitar de la tabla de campos a mostrar");
		
		return false;
	});
	
	//Propagar cuando se pulsa en los checks de mostrar en la tabla de campos a mostrar
	$('.check_mostrar').live('click',function() {
		var num;
		var valor;
		if ($(this).is(":checked"))
			valor = "true";
		else
			valor = "false";
		
		if ( $(this).attr("id").indexOf("check_mostrar_campo_")!=-1) {
			num = $(this).attr("id").substring("check_mostrar_campo_".length);
			$('#mostrar_campo_'+num).val(valor);
		} else if ( $(this).attr("id").indexOf("check_mostrar_formula_")!=-1) {
			num = $(this).attr("id").substring("check_mostrar_formula_".length);
			$('#mostrar_formula_'+num).val(valor);
		} else if ( $(this).attr("id").indexOf("check_mostrar_relacion_")!=-1) {
			num = $(this).attr("id").substring("check_mostrar_relacion_".length);
			$('#mostrar_relacion_'+num).val(valor);
		}
	});
	
	//AÑADIR CRITERIOS
	$('.criterio').live("click",function() {
		var obj = $(this);
		$('#label_anhadir_criterio').html(obj.attr("data-columna"));
    	$('#valor_campo_criterio').val(obj.attr("data-columna"));
    	//Si tiene 2 'a' es q tiene el '+' para añadir criterios y el '?' con el criterio
    	if ( $(obj).parent().find("a").length == 2 ) {
    		$(obj).parent().find("a").each(function() {
    			if ( $(this).hasClass("criterio_anhadido")) {
    				$('#valor_criterio').val($(this).attr("title"));
    			}
    		})
    	} else 
    		$('#valor_criterio').val("");
    	
		var dialogoModalCriterio = $("#dialogoModalCriterio").dialog({
		    autoOpen: false,
		    show: "blind",
		    hide: "explode",                    
		    width: 575,
		    height: 300,
		    modal: true,
		    resizable: false,
		    title: obj.attr("data-columna"),
		    buttons: {
		        "Aceptar": function() {
		        	if ( $('#valor_criterio').val()==null || $('#valor_criterio').val()=="") {
		        		alert("El criterio no puede estar vacio");
		        		return false;
		        	}
		        	indicadores.anhadirCriterioTablaFinal(obj.attr("data-columna"),obj);
	            	$(this).dialog( "close" );
		            return false;            
		           
		        },
		         "Cancelar": function() {
		            $(this).dialog( "close" );
		            return false;            
		        }
		    }            
		});
		$(dialogoModalCriterio).dialog("open");
	});
	
	$('#a_anhadir_campo_criterio').live("click",function() {
		$('#valor_criterio').val($('#valor_criterio').val()+"[*]");
		return false;
	});
});

var indicadores = {
	url 			: 'fuentes.htm',
	numCampos 		: 0,
	numFormulas		: 0,
	numRelaciones   : 0,
	numCriterios    : 0,
	
	/*
	 * CARGAR TABLAS FUENTES
	 */
	cargarTablasFuente: function(fuente) {
		fuente = fuente.split("||");
		if ( fuente[0] > 0 ) {
			$('#cargando_tablas').removeClass("oculto");
			$.ajax({
				   type: "POST",
				   url: indicadores.url,
				   data: "accion=listaTablas&fuente="+fuente[0]+"&tipo="+fuente[1],
				   success: function(msg){
					   $('#cargando_tablas').addClass("oculto");
					   
					   var res = msg.split("||");
					   if ( res[0] == 'correcto' ) {
						   var json = JSON.parse(res[1]);
						   indicadores.montarListaTablas(json,fuente[0]);
					   } else {
						   alert ($('#error-ajax').val()); 
					   }
					   
					}
			});
		}
	},
	
	/*
	 * CARGAR COLUMNAS FUENTE
	 */
	cargarColumnasFuente: function(fuente,tabla) {
		fuente = fuente.split("||");
		$('#cargando_columnas').removeClass("oculto");
		if ( fuente[0] > 0 ) {
			$.ajax({
				   type: "POST",
				   url: indicadores.url,
				   data: "accion=listaColumnas&fuente="+fuente[0]+"&tipo="+fuente[1]+"&tabla="+tabla,
				   success: function(msg){
					   $('#cargando_columnas').addClass("oculto");
					   var res = msg.split("||");
					   if ( res[0] == 'correcto' ) {
						   var json = JSON.parse(res[1]);
						   indicadores.montarListaColumnasTabla(json,fuente[0],tabla);
						   if ($('#zona_tablas').hasClass("oculto")) {
								$('#zona_tablas').removeClass("oculto");
						   }
					   } else {
						   alert ($('#error-ajax').val()); 
					   }
					}
			});
		}
	},
	
	/*
	 * MONTAR LISTA PARA COMBO DE TABLAS
	 */
	montarListaTablas: function(json,idFuente) {
		$("#tabla_fuente_selec").html("");
		$.each(json, function(key,value) { 
			var obj = new Option(value.nombre, value.nombre);
			$(obj).html(value.nombre);
			$("#tabla_fuente_selec").append(obj);
		});
	},
	
	/*
	 * MONTAR LISTA COLUMNAS TABLA PARA MOSTRAR
	 */
	montarListaColumnasTabla: function(json,idFuente,tabla) {
		//Montar tabla
		var htmlTabla = '<div class="fizq"><table data-idfuente="'+idFuente+'" id="tabla_'+tabla+'" class="tabla_indicador izq10 inf15" cellpadding="0" cellspacing="0">'+	
						'<tr>'+
						'<th scope="col" class="cabeceraTabla" data-tabla="tabla_'+tabla+'">'+tabla+'</th>'+
						'</tr>';
		$.each(json, function(key,value) {
			htmlTabla += '<tr class="columnaTabla" data-tipo="'+value.tipoAtributo+'" data-columna="'+value.nombre+'" data-tabla="tabla_'+tabla+'"><td>'+value.nombre+'</td></tr>';
		});
		htmlTabla += "</table></div>";
		//Añadir tabla
		$('#zona_columnas').prepend(htmlTabla);
	},
	
	/*
	 * COMPROBAR SI UNA TABLA EXISTE YA EN LAS TABLAS UTILIZADAS
	 */
	comprobarSiExisteTabla: function(tabla) {
		var resp = false;
		$('#zona_columnas table').each(function(){
			if ( $(this).attr("id") == tabla )
			resp = true;
		});
		return resp;
	},
	
	/*
	 * RELLENAR TABLAS RELACION
	 */
	rellenarTablasRelacion: function(columna, idTablas, idColumnas) {
		//primero obtengo todas las tablas que no sean la q esta seleccionada
		var primero = true;
		$('#'+idTablas).html("");
		$('#'+idColumnas).html("");
		$('#zona_columnas table').each(function(){
			if ( $(this).attr("id") != columna.tabla) {
				var nombreTabla = $(this).attr("id").substring("tabla_".length);
				var obj = new Option(nombreTabla,nombreTabla);
				$(obj).html(nombreTabla);
				$("#"+idTablas).append(obj);
				if ( primero ) {
					primero=false;
					$("[id='"+$(this).attr("id")+"'] tr td").each(function(){
						
						var nombreCol = $(this).html();
						var obj = new Option(nombreCol,nombreCol);
						$(obj).html(nombreCol);
						$("#"+idColumnas).append(obj);
					});
				}
			}
		});
	},
	
	/*
	 * RELLENAR TABLAS PARA LISTA DE TABLAS
	 */
	rellenarTablas: function(columna, idTablas, idColumnas) {
		var primero = true;
		$('#'+idTablas).html("");
		$('#'+idColumnas).html("");
		$('#zona_columnas table').each(function(){
			var nombreTabla = $(this).attr("id").substring("tabla_".length);
			var obj = new Option(nombreTabla,nombreTabla);
			$(obj).html(nombreTabla);
			$("#"+idTablas).append(obj);
			if ( primero ) {
				primero=false;
				$("[id='"+$(this).attr("id")+"'] tr td").each(function(){
					var nombreCol = $(this).html();
					if ( !indicadores.existeCampoEnTablaFinal(nombreTabla,nombreCol)) {
						var obj = new Option(nombreCol,nombreCol);
						$(obj).html(nombreCol);
						if ( $(this).parent().attr("data-tipo")=="VALORFDRELACION") {
							$(obj).attr("data-tipo","VALORFDRELACION");
							$(obj).attr("data-col-rel",$(this).parent().attr("data-col-rel"));
							$(obj).attr("data-tabla-rel",$(this).parent().attr("data-tabla-rel"));
							$(obj).attr("data-col",$(this).parent().attr("data-columna"));
							$(obj).attr("data-tabla",$(this).parent().attr("data-tabla"));
						} else
							$(obj).attr("data-tipo","VALORFDNORMAL");
						$("#"+idColumnas).append(obj);
					}
				});
			}
		});
	},
	
	/*
	 * ANHADIR CAMPO A LA TABLA FINAL
	 */
	anhadirCampoTablaFinal: function() {
		if ( $('#columnas_campos').find(":selected").val()==null || $('#columnas_campos').find(":selected").val()=="") {
			alert("No hay más columnas disponibles en la tabla seleccionada");
			return false;
		}
		if ( $('#orden_campos').val()==null || $('#orden_campos').val()=="" || !indicadores.esNumero($('#orden_campos').val()) ) {
			alert("Indique un orden numérico para el campo");
			return false;
		}
		
		if ( indicadores.existeOrdenTablaFinal($('#orden_campos').val())) {
			alert("Indique un orden que no esté en uso");
			return false;
		}
		
		var form = "";
		var div = "";
		var html = "<tr id='"+$('#tablas_campos').val()+"||"+$('#columnas_campos').val()+"' class='' onmouseover='this.className=\"destacadoTablas\"' onmouseout='this.className=\"\"'>";
		var htmlMostrar = "";
		if ( $('#columnas_campos').find(":selected").attr("data-tipo") == "VALORFDRELACION" ) {
			//Campos de relaciones
			indicadores.numRelaciones++;
			html += "<td><input name='marcar_relaciones_"+indicadores.numRelaciones+"' type='checkbox' class='filasTablaFinal relacion'/></td>";
			div = "relaciones_envio";
			html += "<td>"+$('#tablas_campos').val()+"</td>";
			html += "<td>"+$('#columnas_campos').val()+"</td>";
			htmlMostrar = "<td class='centro'><input class='check_mostrar' type='checkbox' id='check_mostrar_relacion_"+indicadores.numRelaciones+"'/>";
			form = "<input type='hidden' name='tabla_relacion_"+indicadores.numRelaciones+"' id='tabla_relacion_"+indicadores.numRelaciones+"' value='"+$('#columnas_campos').find(":selected").attr("data-tabla").substring("tabla_".length)+"'/>";
			form += "<input type='hidden' name='idfuente_relacion_"+indicadores.numRelaciones+"' id='idfuente_relacion_"+indicadores.numRelaciones+"' value='"+$('[id="tabla_'+$('#columnas_campos').find(":selected").attr("data-tabla").substring("tabla_".length)+'"]').attr("data-idfuente")+"'/>";
			form += "<input type='hidden' name='columna_relacion_"+indicadores.numRelaciones+"' id='columna_relacion_"+indicadores.numRelaciones+"' value='"+$('#columnas_campos').find(":selected").attr("data-col")+"'/>";
			form += "<input type='hidden' name='orden_relacion_"+indicadores.numRelaciones+"' id='orden_relacion_"+indicadores.numRelaciones+"' value='"+$('#orden_campos').val()+"'/>";
			form += "<input type='hidden' name='mostrar_relacion_"+indicadores.numRelaciones+"' id='mostrar_relacion_"+indicadores.numRelaciones+"'/>";
			form += "<input type='hidden' name='tabla_relacionada_"+indicadores.numRelaciones+"' id='tabla_relacionada_"+indicadores.numRelaciones+"' value='"+$('#columnas_campos').find(":selected").attr("data-tabla-rel")+"'/>";
			form += "<input type='hidden' name='columna_relacionada_"+indicadores.numRelaciones+"' id='columna_relacionada_"+indicadores.numRelaciones+"' value='"+$('#columnas_campos').find(":selected").attr("data-col-rel")+"'/>";
			form += "<input type='hidden' name='idfuente_relacionada_"+indicadores.numRelaciones+"' id='idfuente_relacionada_"+indicadores.numRelaciones+"' value='"+$('[id="tabla_'+$('#columnas_campos').find(":selected").attr("data-tabla-rel")+'"]').attr("data-idfuente")+"'/>";
			$('#num_relaciones_envio').val(indicadores.numRelaciones);
		} else {
			//Campos normales
			indicadores.numCampos++;
			html += "<td><input name='marcar_campos_"+indicadores.numCampos+"' type='checkbox' class='filasTablaFinal campo'/></td>";
			div = "campos_envio";
			html += "<td>"+$('#tablas_campos').val()+"</td>";
			html += "<td>"+$('#columnas_campos').val()+"</td>";
			htmlMostrar = "<td class='centro'><input class='check_mostrar' type='checkbox' checked='checked' id='check_mostrar_campo_"+indicadores.numCampos+"'/>";
			form = "<input type='hidden' name='tabla_campo_"+indicadores.numCampos+"' id='tabla_campo_"+indicadores.numCampos+"' value='"+$('#tablas_campos').val()+"'/>";
			form += "<input type='hidden' name='columna_campo_"+indicadores.numCampos+"' id='columna_campo_"+indicadores.numCampos+"' value='"+$('#columnas_campos').val()+"'/>";
			form += "<input type='hidden' name='orden_campo_"+indicadores.numCampos+"' id='orden_campo_"+indicadores.numCampos+"' value='"+$('#orden_campos').val()+"'/>";
			form += "<input type='hidden' name='mostrar_campo_"+indicadores.numCampos+"' id='mostrar_campo_"+indicadores.numCampos+"' value='true'/>";
			form += "<input type='hidden' name='idfuente_campo_"+indicadores.numCampos+"' id='idfuente_campo_"+indicadores.numCampos+"' value='"+$("[id='tabla_"+$('#tablas_campos').val()+"']").attr("data-idfuente")+"'/>";
			$('#num_campos_envio').val(indicadores.numCampos);
		}

		html += "<td class='centro orden'>"+$('#orden_campos').val()+"</td>";
		html += htmlMostrar;
		
		var criterios = false;
		var nombreTabla = "";
		//Averiguar el tipo de datos de la columna elegida.
		$('#zona_columnas table').each(function(){
			nombreTabla = $(this).attr("id").substring("tabla_".length);
			if ( nombreTabla == $('#tablas_campos').val()) {
				$("[id='tabla_"+nombreTabla+"'] tr td").each(function(){
					if ( $(this).html()==$('#columnas_campos').val()) {
						var tipo = $(this).parent().attr("data-tipo");
						if ( tipo == "VALORFDNUMERICO")
							criterios = true;
					}
				});
			}
		});
		
		if ( !criterios )		
			html += "<td class='centro'>---</td></tr>";
		else
			html += "<td class='centro'><a href='#' onclick='return false;' class='criterio fizq' data-idfuente='"+$("[id='tabla_"+nombreTabla+"']").attr("data-idfuente")+"' data-tabla='"+$('#tablas_campos').val()+"' data-columna='"+$('#columnas_campos').val()+"'><span class='ui-icon ui-icon-circle-plus'></span></a></td></tr>";
	
		$('#tabla_campos_mostrar').append(html);
		//Componer formulario
		$('#'+div).append(form);
		return true;
	},
	
	/*
	 * ANHADIR FORMULA A LA TABLA FINAL
	 */
	anhadirFormulaTablaFinal: function() {
		indicadores.numFormulas++;
		var div = "formulas_envio";
		var form = "";
		var html = "<tr class='' onmouseover='this.className=\"destacadoTablas\"' onmouseout='this.className=\"\"'>";
		html += "<td><input type='checkbox' class='filasTablaFinal formula' name='marcar_formulas_"+indicadores.numFormulas+"'/></td>";
		html += "<td>"+$('#valor_formula').val()+"</td>";
		html += "<td>"+$('#nombre_formula').val()+"</td>";
		html += "<td class='centro orden'>"+$('#orden_formula').val()+"</td>";
		html += "<td class='centro'><input class='check_mostrar' type='checkbox' id='check_mostrar_formula_"+indicadores.numFormulas+"'/>";
		
		html += "<td class='centro'><a href='#' onclick='return false;' class='criterio fizq' data-columna='"+$('#nombre_formula').val()+"'><span class='ui-icon ui-icon-circle-plus'></span></a></td></tr>";
		
		$('#tabla_campos_mostrar').append(html);		
		
		//Componer formulas
		form += "<input type='hidden' name='nombre_formula_"+indicadores.numFormulas+"' id='nombre_formula_"+indicadores.numFormulas+"' value='"+$('#nombre_formula').val()+"'/>";
		form += "<input type='hidden' name='valor_formula_"+indicadores.numFormulas+"' id='valor_formula_"+indicadores.numFormulas+"' value='"+$('#valor_formula').val()+"'/>";
		form += "<input type='hidden' name='orden_formula_"+indicadores.numFormulas+"' id='orden_formula_"+indicadores.numFormulas+"' value='"+$('#orden_formula').val()+"'/>";
		form += "<input type='hidden' name='mostrar_formula_"+indicadores.numFormulas+"' id='mostrar_formula_"+indicadores.numFormulas+"'/>";
		$('#num_formulas_envio').val(indicadores.numFormulas);
		$('#'+div).append(form);
	},
	
	/*
	 * VALIDAR FORMULA
	 */
	validarFormula: function() {
		if ( ($('#nombre_formula').val()=="" || $('#nombre_formula').val().length<=0) ||
			 ($('#valor_formula').val()=="" || $('#valor_formula').val().length<=0)	) {
			alert("El nombre y la definición de la fórmula no puede estar vacios");
			return false;
		}
		
		if ( $('#orden_formula').val()==null || $('#orden_formula').val()=="" || !indicadores.esNumero($('#orden_formula').val())) {
			alert("Indique un orden numérico para el campo");
			return false;
		}
		
		if ( indicadores.existeOrdenTablaFinal($('#orden_formula').val())) {
			alert("Indique un orden que no esté en uso");
			return false;
		}
		return true;
	},
	
	/*
	 * RELLENAR SELECT CAMPOS PARA FORMULA 
	 */
	rellenarCamposParaFormula: function() {
		$("#anhadir_campo_formula").html("");
		$('#zona_columnas table').each(function(){
			var nombreTabla = $(this).attr("id").substring("tabla_".length);
			var idFuente = $(this).attr("data-idfuente");
			var html = "<optgroup label='"+nombreTabla+"'>";
			$("#anhadir_campo_formula").append(html);
			$("[id='"+$(this).attr("id")+"'] tr td").each(function(){
				var nombreCol = $(this).html();
				if ( $(this).parent().attr("data-tipo") == "VALORFDNUMERICO" ) {
					var htmlOption = "<option value='["+idFuente+"||"+nombreTabla+"||"+nombreCol+"]'>"+nombreCol+"</option>";
					$("#anhadir_campo_formula").append(htmlOption);
				}
			});
			$("#anhadir_campo_formula").append("</optgroup>");						
		});	
	},
	
	/*
	 * COMPROBAR SI EL CAMPO Y LA TABLA YA ESTAN ELEGIDOS EN LA TABLA FINAL 
	 */
	existeCampoEnTablaFinal : function (tabla, campo) {
		var res = false;
		$('#tabla_campos_mostrar tr').each(function() {
			var tablaCampo = $(this).attr("id").split("||");
			if ( tablaCampo[0] == tabla && tablaCampo[1] == campo )
				res = true;
		});
		return res;
	},
	
	/*
	 * COMPROBAR SI UNA TABLA CONTIENE UN CAMPO 
	 */
	existeColumnaTabla : function (tabla, columna) {
		console.log(tabla);
		console.log(columna);
		var res = false;
		$('#zona_columnas table').each(function() {
			var nombreTabla = $(this).attr("id").substring("tabla_".length);
			if ( nombreTabla == tabla ) {
				$("[id='tabla_"+tabla+"'] tr").each(function() {
					if ( columna == $(this).attr("data-columna")) {
						res = true;
						return true;
					}
				});
			}				
		});
		return res;
	},
	
	obtenerTipoColumna : function(tabla,columna) {
		var resultado = "";
		$('#zona_columnas table').each(function() {
			var nombreTabla = $(this).attr("id").substring("tabla_".length);
			if ( nombreTabla == tabla ) {
				$("[id='tabla_"+tabla+"'] tr").each(function() {
					if ( columna == $(this).attr("data-columna")) {
						resultado = $(this).attr("data-tipo");
					}
				});
			}				
		});
		return resultado;
	},
	
	/*
	 * ANHADIR CRITERIO
	 */
	anhadirCriterioTablaFinal : function(columna, obj) {
		var div = "criterios_envio";
		var form = "";
		
		var encontrado = false;
		$(obj).parent().find("a.criterio_anhadido").each(function() {
			encontrado = true;
		});
		if ( encontrado ) {
			$(obj).parent().find("a.criterio_anhadido").attr("title",$('#valor_criterio').val());
			var num = $(obj).parent().find("a.criterio_anhadido").attr("data-num-criterio");
			$('#valor_criterio_'+num).val($('#valor_criterio').val());
		}
		else {
			$(obj).parent().append("<a href='#' data-num-criterio='"+indicadores.numCriterios+"' style='position:relative;top:-1px' class='criterio_anhadido fder' onclick='return false;' title='"+$('#valor_criterio').val()+"'><span class='ui-icon ui-icon-check'></span></a>");
			//Componer criterios
			form += "<input type='hidden' name='columna_criterio_"+indicadores.numCriterios+"' id='columna_criterio_"+indicadores.numCriterios+"' value='"+$(obj).attr("data-columna")+"'/>";
			form += "<input type='hidden' name='tabla_criterio_"+indicadores.numCriterios+"' id='tabla_criterio_"+indicadores.numCriterios+"' value='"+$(obj).attr("data-tabla")+"'/>";
			form += "<input type='hidden' name='valor_criterio_"+indicadores.numCriterios+"' id='valor_criterio_"+indicadores.numCriterios+"'/>";
			$('#num_criterios_envio').val(indicadores.numCriterios);
			$('#'+div).append(form);
			$('#valor_criterio_'+indicadores.numCriterios).val($('#valor_criterio').val());
		}
		indicadores.numCriterios++;
	},
	
	existeOrdenTablaFinal : function(orden) {
		var encontrado = false;
		$('#tabla_campos_mostrar td.orden').each(function() {
			if ( $(this).html()==orden)
				encontrado = true;
		});
		return encontrado;
	},
	
	esNumero : function (n) {
	  return !isNaN(parseFloat(n)) && isFinite(n);
	}

};