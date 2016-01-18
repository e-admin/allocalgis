<%@page contentType="text/html" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<script src="js/util/comun/cambioMenu.js"></script>
<script src="js/util/fuentes/fuente.js"></script>
<script type="text/javascript" src="js/util/comun/funcions.js"></script>
    <div class="cuerpoprincipal">
	    <div class="areamenu">
			<ul class="menusecundario">
				<li>
					<a href="#" onclick="cambioMenu('?accion=verCatalogoSistema'); return false;">
						<spring:message code="jsp.catalogo.titulo"/>
					</a>
				</li>
				<li lass="on">
					<a href="#" onclick="cambioMenu('fuentes.htm');return false;">
						<spring:message code="jsp.fuentes.fuentes"/>
					</a>
				</li>
			</ul>
		</div>
		<div class="areacentral">
		    <h2 class="inf15"><spring:message code="jsp.fuente.titulo"/></h2>
		    
			<c:if test="${errorTamanhoArchivo}">
				<div class="controlAlertas">	
					<div class="ui-widget">
	            		<div class="ui-state-error ui-corner-all"> 
							<p>
								<span class="ui-icon ui-icon-alert fizq"></span>
					          	<spring:message code="jsp.fuentes.fuente.error.tamanoArchivo" />
				         	</p>
				  	 	</div>
	         		</div>
         		</div>
         		<br/>     
			</c:if>
			<!--  mensajes de error -->
			<spring:hasBindErrors name="fuenteDto">
			<div class="controlAlertas">	
				<div class="ui-widget">
					<div class="ui-state-error ui-corner-all"> 
						<p><span class="ui-icon ui-icon-alert fizq"></span> 
						<strong><spring:message code="global.formulario.erros.titulo"/></strong> 
						</p>
						<ul>
							${empty error.globalError? "" : "<li>"+error.defaultMessage+"</li>"}
							<c:forEach items="${errors.fieldErrors}" var="error">
					         	<li><form:errors path="${error.objectName}.${error.field}"/></li>
					        </c:forEach>
						</ul>
						<p></p>
					</div>
				</div>
			</div>
			</spring:hasBindErrors>
			
			<div id="alertasJS" class="controlAlertas oculto">	
				<input id="error_nombre" type="hidden" value="<spring:message code="validacion.fuente.nombre.vacio"/>">
				<input id="error_tipo" type="hidden" value="<spring:message code="validacion.fuente.tipo.vacio"/>">
				<input id="error_conexion" type="hidden" value="<spring:message code="validacion.fuente.info.conexion.vacio"/>">
				<input id="error_conexion" type="hidden" value="<spring:message code="validacion.fuente.info.conexion.vacio"/>">
				<input id="error_ficheroSH" type="hidden" value="<spring:message code="validacion.fuentes.shapefile.ficheros.obligatorios"/>">
				<input id="error_ficheroCSV" type="hidden" value="<spring:message code="validacion.fuentes.fichero_obligatorio"/>">
				<div class="ui-widget">
					<div class="ui-state-error ui-corner-all"> 
						<p><span class="ui-icon ui-icon-alert fizq"></span> 
						<strong><spring:message code="global.formulario.erros.titulo"/></strong>
						</p>
						<div id="lista_alertas_JS" >
							
						</div>
					</div>
				</div>
			</div>
			
			<!--  mensajes de exito -->
			<c:if test="${resultado != null}">
            <div class="controlAlertas">	
				<div class="ui-widget">
                     <c:if test="${resultado == 'exitoCrear'}">
                         <div class="success ui-corner-all"> 
							<p><span class="ui-icon ui-icon-circle-check fizq"></span>
                            <spring:message code="jsp.fuente.nueva.alta" />
                            </p>
                         </div>
                     </c:if>
                     <c:if test="${resultado == 'exitoGuardar'}">
                         <div class="success ui-corner-all"> 
							<p><span class="ui-icon ui-icon-circle-check fizq"></span>
                            <spring:message code="jsp.fuente.modificacion" />
                            </p>
                         </div>
                     </c:if>
                     <c:if test="${resultado == 'exitoBorrar'}">
                         <div class="success ui-corner-all"> 
							<p><span class="ui-icon ui-icon-circle-check fizq"></span>
                            <spring:message code="jsp.fuente.borrado.correcto" />
                            </p>
                         </div>
                     </c:if>
                     <c:if test="${resultado == 'fileError'}">
                         <div class="ui-state-error ui-corner-all"> 
							<p><span class="ui-icon ui-icon-circle-check fizq"></span>
                            <spring:message code="${fileError}" />
                            </p>
                         </div>
                     </c:if>
                 </div>
            </div>
            </c:if>
            
            <c:if test="${passwordCambiadaExito != null}">
            <div class="controlAlertas">	
				<div class="ui-widget">
                     <div class="success ui-corner-all"> 
						<p><span class="ui-icon ui-icon-circle-check fizq"></span>
                        	<spring:message code="${passwordCambiadaExito}" />
                        </p>
                     </div>
                 </div>
            </div>
            </c:if>
            
            <c:if test="${errorFichero!=null}">
				<div class="controlAlertas">	
					<div class="ui-widget">
	            		<div class="ui-state-error ui-corner-all"> 
							<p>
								<span class="ui-icon ui-icon-alert fizq"></span>
					          	<spring:message code="${errorFichero}" />
				         	</p>
				  	 	</div>
	         		</div>
         		</div>
         		<br/>     
			</c:if>
            
            <div class="fizq">			
			<form:form id="formulario"  modelAttribute="fuenteDto" action="fuentes.htm" enctype="multipart/form-data" method="post">
				<input type="hidden" id="accion" name="accion" value="guarda" />
				<input type="hidden" id="idDto" name="id" value="${fuenteDto.id}" />
		
				<c:if test="${fuenteDto.id>0}">
				<div class="linea">
					<label for="nombre" class="label fizq"><spring:message code="jsp.fuentes.nombre"/></label>
					<span>${fuenteDto.nombre}</span>
					<form:input type="hidden" id="nombre" path="nombre"/>
				</div>
				</c:if>
				<c:if test="${fuenteDto.id<=0}">
				<div class="linea">
					<label for="nombre" class="label fizq"><spring:message code="jsp.fuentes.nombre"/></label>
					<form:input type="text" cssClass="input_med" id="nombre" path="nombre" maxlength="255"/>
				</div>
				</c:if>
				
				<div class="linea">
					<label for="descripcion" class="label fizq"><spring:message code="jsp.fuentes.descripcion"/></label>
					<form:textarea type="text" value="" id="descripcion" cssClass="input_med" path="descripcion" maxlength="255"/>
				</div>
				
				<div class="linea">
					<label for="tipo" class="label fizq"><spring:message code="jsp.fuentes.tipo"/></label>
					<select name="tipo" id="tipo" >
						<option value="" selected="selected">&nbsp;</option>
						<c:forEach items="${tiposFuente}" var="tipo">
                      	<option value="${tipo.id}" <c:if test="${fuenteDto.tipo.id eq tipo.id}">selected="selected"</c:if>><spring:message code="${tipo.internacionalizacion}"/></option>
                       </c:forEach>
                    </select>
				</div>
				<div class="clear"></div>
				
				
					<div id="shapefile" class="oculto conexion">
						<div class="linea">
							<label for="fich_shp_f" class="label fizq"><spring:message code="jsp.fuentes.fichero.shp"/></label>
							<input type="file" value="" id="fich_shp_f" class="input_med" name="fich_shp_f"/>
						</div>
						<c:if test="${fuenteDto.fich_shp!=null && fuenteDto.fich_shp!=''}">
							<div class="linea">
								<label class="label fizq"><spring:message code="elementos.jsp.fichero"/>:</label>
								<span>( ${fuenteDto.fich_shp} )</span>
							</div>
						</c:if>
						<div class="linea">
							<label for="fich_dbf_f" class="label fizq"><spring:message code="jsp.fuentes.fichero.dbf"/></label>
							<input type="file" value="" id="fich_dbf_f" class="input_med" name="fich_dbf_f"/>
						</div>
						<c:if test="${fuenteDto.fich_dbf!=null && fuenteDto.fich_dbf!=''}">
							<div class="linea">
								<label class="label fizq"><spring:message code="elementos.jsp.fichero"/>:</label>
								<span>( ${fuenteDto.fich_dbf} )</span>
							</div>
						</c:if>
						<div class="linea">
							<label for="fich_shx_f" class="label fizq"><spring:message code="jsp.fuentes.fichero.shx"/></label>
							<input type="file" value="" id="fich_shx_f" class="input_med" name="fich_shx_f"/>
						</div>
						<c:if test="${fuenteDto.fich_shx!=null && fuenteDto.fich_shx!=''}">
							<div class="linea">
								<label class="label fizq"><spring:message code="elementos.jsp.fichero"/>:</label>
								<span>( ${fuenteDto.fich_shx} )</span>
							</div>
						</c:if>
					</div>					
					
					<div id="txt_info_conexion" class="oculto conexion">
						<div class="linea" id="info_conexion">
							<label for="infoConexion" class="label fizq"><spring:message code="jsp.fuentes.info.conexion"/></label>
							<form:input type="text" id="infoConexion" cssClass="input_med" path="infoConexion" maxlength="255"/>
						</div>
					</div>
					
					<div id="csv_gml" class="oculto conexion">
						<div class="linea" id="fich_csv_gml_f_div">
							<label for="fich_csv_gml_f" class="label fizq"><spring:message code="jsp.fuentes.info.conexion"/></label>
							<input type="file" id="fich_csv_gml_f" name="fich_csv_gml_f"/>
						</div>
						<c:if test="${fuenteDto.fich_csv_gml!=null && fuenteDto.fich_csv_gml!=''}">
							<div class="linea">
								<label class="label fizq"><spring:message code="elementos.jsp.fichero"/>:</label>
								<span>( ${fuenteDto.fich_csv_gml} )</span>
							</div>
						</c:if>
					</div>
				<div id="user_pass">	
					<div class="linea">
						<label for="usuario" class="label fizq"><spring:message code="jsp.fuentes.usuario"/></label>
						<form:input type="text" id="usuario" cssClass="input_med" path="usuario" maxlength="255"/>
					</div>
					<div class="linea">
						<label for="password" class="label fizq"><spring:message code="jsp.fuentes.password"/></label>
						<form:input type="password" id="password" cssClass="input_med" path="password" maxlength="255"/>
					</div>
				</div>
			</form:form>
			</div>
			<%-- zona indicadores --%>
			<c:if test="${fuenteDto.id>0}">
				<div class="clear"><br/><br/></div>
				<div class="contenedorarbol">
					<h2><spring:message code="jsp.fuente.lista.indicadores.asociados"/></h2>
					<c:if test="${empty mapaCategorias && empty mapaIndicadores}">
						<div><spring:message code="jsp.indicadores.lista.vacia"/></div>
					</c:if>
					<c:if test="${!empty mapaCategorias || !empty mapaIndicadores}">
						<c:if test="${usuarioAdministrador}">
							<table id="tablaIndicadores" class="tabla_jerarquia" border="0" cellspacing="0" cellpadding="0">
								<thead>
									<tr>
										<td id="tuplaCategoria0" class="cat">
											<a href="#" onclick="mostrarOcultarArbore('idcategoriapadre-0_tr', 'idcategoria0_img',true, false, false);">
												<img id="idcategoria0_img" src="images/folder_open.png" style="float: left;" />
											</a>
											<a href="#" class="enlaceTablaCategoria" style="padding-left: 8px;" idCategoria="0">
												<spring:message code="jsp.indicadores.indicadores.raiz"/>
											</a>
										</td>
									</tr>
								</thead>
							</table>
						</c:if>
						<c:if test="${!usuarioAdministrador}">
							<table id="tablaIndicadores" class="tabla_jerarquia" border="0" cellspacing="0" cellpadding="0">
								<thead id="tbCabecera">
									<tr id="trCabecera">
										<td id="tuplaCategoria0" class="cat" idCategoria="0">
											<a href="#" onclick="mostrarOcultarArbore('idcategoriapadre-0_tr', 'idcategoria0_img',true, false, false);">
												<img id='idcategoria0_img' src="images/folder_open.png" style="float: left;" />
											</a>
											<a href="#" class="enlaceTablaCategoria" style="padding-left: 8px;" idCategoria="0">
												<spring:message code="jsp.indicadores.indicadores.raiz"/>
											</a>
										</td>
									</tr>
								</thead>
								<tbody id="tbCupero">
								</tbody>
							</table>
						</c:if>
					</c:if>
				</div>
			</c:if>
		</div>
		<div class="clear"></div>
		<div class="botones_accion">
			<a href="fuentes.htm" class="boton izq10 sup10 fondo_blanco fder"><spring:message code="elementos.jsp.boton.cancelar"/></a>
			<a href="#" onclick="enviarFormulario();return false;" class="boton izq10 sup10 fondo_blanco fder"><spring:message code="elementos.jsp.boton.aceptar"/></a>	
		</div>	
	</div>

<div id="dialogoModalAviso" style="display: none;background-color: white;">
    <div class="ui-widget">
        <div style="padding: 0.2em 0.7em;" class=""> 
            <p>
                <span style="float: left; margin-right: 0.3em;" class=""></span>
                <spring:message code="elementos.jsp.aviso.cambio"/>
            </p>
        </div>
    </div>
</div>

<script type="text/javascript">

var idCategoria=null;
var idIndicador=null;

$(document).ready(function() {
	<c:if test="${usuarioAdministrador}">
		InicializarTabla();
	</c:if>
	
	$('td,th').mouseout(function() {
		$(this).removeClass("destacadoTablas");
	});
	$('td,th').mouseover(function() {
		if ( $(this).html().length>0 )
			$(this).addClass("destacadoTablas");
	});
});

<c:if test="${usuarioAdministrador}">
	function InicializarTabla() {
		<c:forEach items="${mapaCategorias[0]}" var="categoria">
			dibujarFilaCategoria('tablaIndicadores', '0', '${categoria.id}', '${categoria.nombre}', 20);
		</c:forEach>
	
	 	<c:forEach items="${mapaIndicadores[0]}" var="indicador">
			dibujarFilaIndicador('tablaIndicadores', '0', '${indicador.id}', '${indicador.nombre}', 20);
		</c:forEach>
	}

	function dibujarFilaIndicador(idTabla, idCategoriaPadre, idIndicador, nombreIndicador, margenIzq) {
		var nombreClassFila = "idcategoriapadre-" + idCategoriaPadre + "_tr";
		var table = document.getElementById(idTabla);
	
		var rowCount = table.rows.length;
		var row = table.insertRow(rowCount);
		row.id ="idIndicador" + idIndicador;
		row.className = "idcategoriapadre-" + idCategoriaPadre + "_tr";
		
		var cell = row.insertCell(0);
		cell.id = "tuplaIndicador" + idIndicador;
		cell.className = "no_cat";
		cell.style.paddingLeft = margenIzq + "px";
		cell.innerHTML = "<a href='#' class='enlaceTablaIndicador' idindicador='" + idIndicador + "'><img id='idindicador" + idIndicador + "_img' src='images/file.png' alt='' style='float: left; padding-right: 8px;' /><span style='float: left; padding-left: 10px:'>" + nombreIndicador + "</span></a>";
	}

	function dibujarFilaCategoria(idTabla, idCategoriaPadre, idCategoria, nombreCategoria, margenIzq) {
		var nombreClassFila = "idcategoriapadre-" + idCategoriaPadre + "_tr";
		var funcionJs = "onclick=\"mostrarOcultarArbore('idcategoriapadre-" + idCategoria + "_tr', 'idcategoria" + idCategoria + "_img',true, false, false);\"";
		// Se dibuja la celda correspondiente a la categoría
		var table = document.getElementById(idTabla);
	
		var rowCount = table.rows.length;
		var row = table.insertRow(rowCount);
		row.id = "idCategoria" + idCategoria;
		row.className = nombreClassFila;
	
		var cell = row.insertCell(0);
		cell.id = "tuplaCategoria" + idCategoria;
		cell.className = "cat destacadotablas";
		cell.style.paddingLeft = margenIzq + "px";
		cell.innerHTML = "<a href='#' " + funcionJs +"><img id='idcategoria" + idCategoria + "_img' src='images/folder_open.png' style='float: left;' /></a><a href='#' class='enlaceTablaCategoria' style='padding-left: 8px;' idcategoria='" + idCategoria +"'>" + nombreCategoria + "</a>";
		
		// Se dibujan las filas correspondientes con sus Categorías hijo
		 <c:forEach items="${mapaCategorias}" var="entry">
		 	if (idCategoria == '${entry.key}') {
		 		<c:forEach items="${entry.value}" var="categoriaHija">
					dibujarFilaCategoria(idTabla, idCategoria, '${categoriaHija.id}', '${categoriaHija.nombre}', margenIzq + 20);
				</c:forEach>
			}
		</c:forEach>
		
		// Se dibujan las filas correspondientes con sus Indicadores hijo
		 <c:forEach items="${mapaIndicadores}" var="entry">
		 	if (idCategoria == '${entry.key}') {
		 		<c:forEach items="${entry.value}" var="indicadorHijo">
					dibujarFilaIndicador(idTabla, idCategoria, '${indicadorHijo.id}', '${indicadorHijo.nombre}', margenIzq + 20);
				</c:forEach>
			}
		</c:forEach>
	}
</c:if>

<c:if test="${!usuarioAdministrador}">
	
	var mapaIndicadores=${mapaIndicadores};
	var mapaCategorias=${mapaCategorias};

	function rellenarArbol(idCategoria,nivel) {
		
		if(mapaCategorias[idCategoria]!=null && (mapaCategorias[idCategoria]).length !=0) {
			//si no existe ese nivel en el arbol se crea
			if ($("#tpCabecera"+idCategoria).length){
				$("#trCabecera").append("<th class='fizq' id='tpCabecera+"+idCategoria+"'></th>");
			}
			
			var listaCategorias = mapaCategorias[idCategoria];
			
			for(var i=0; i < listaCategorias.length; i++) {
				
				var idCategoriaPadre = 0;
				if (listaCategorias[i].idCategoriaPadre != null) {
					idCategoriaPadre = listaCategorias[i].idCategoriaPadre;
				}
				
				var filaId = "idCategoria" + listaCategorias[i].id;
				var filaClass = "idcategoriapadre-" + idCategoriaPadre + "_tr";
				var funcionJs = "onclick=\"mostrarOcultarArbore('idcategoriapadre-" + listaCategorias[i].id + "_tr', 'idcategoria" + listaCategorias[i].id + "_img',true, false, false);\"";
				var celdaId = "tuplaCategoria" + listaCategorias[i].id;
				var celdaClass = "cat destacadotablas";
				
				var htmlTbCupero = "";
				// fila
				htmlTbCupero += "<tr id='"+ filaId + "' class='" + filaClass + "'>";
				// Celda
				htmlTbCupero += "<td id='" + celdaId + "' class='" + celdaClass + "' style='padding-left: " + 20 * nivel + "px;'>";
				htmlTbCupero += "<a href='#' " + funcionJs +"><img id='idcategoria" + listaCategorias[i].id + "_img' src='images/folder_open.png' style='float: left;' /></a><a href='#' class='enlaceTablaCategoria' style='padding-left: 8px;' idcategoria='" + listaCategorias[i].id +"'>" + listaCategorias[i].nombre + "</a>";
	
				htmlTbCupero += "</tr>";
				$('#tbCupero').append(htmlTbCupero);
				
				rellenarArbol(listaCategorias[i].id, nivel+1);
			}
		}	
		rellenaNodoIndicador(idCategoria, nivel);
	}

	function rellenaNodoIndicador(idCategoria,nivel) {
		
		if(mapaIndicadores[idCategoria]!=null && (mapaIndicadores[idCategoria]).length !=0) {
			//si no existe ese nivel en el arbol se crea
			if ($("#tpCabecera"+idCategoria).length) {
				$("#trCabecera").append("<th class='fizq' id='tpCabecera+"+idCategoria+"'></th>");
			}
			
			var indicador;
			var listaIndicadores=mapaIndicadores[idCategoria];
			
			for(var i=0; i<listaIndicadores.length; i++) {
				var idIndicador = listaIndicadores[i].id;
				var nombreIndicador = listaIndicadores[i].nombre;
				
				var trId = "idIndicador" + idIndicador;
				var trclass = "idcategoriapadre-" + idCategoria + "_tr";
				var tdId = "tuplaIndicador" + idIndicador;
				var tdClass = "no_cat";
				var htmlTbCupero = "";
				// Fila
				htmlTbCupero += "<tr id='" + trId + "' class='" + trclass + "'>";
				// Celda
				htmlTbCupero += "<td id='" + tdId + "' class='" + tdClass + "' style='padding-left: " + 20 * nivel + "px;'><a href='#' class='enlaceTablaIndicador' idindicador='" + idIndicador + "'><img id='idindicador" + idIndicador + "_img' src='images/file.png' alt='' style='float: left; padding-right: 8px;' /><span style='float: left; padding-left: 10px:'>" + nombreIndicador + "</span></a></td>";
	
				htmlTbCupero += "</tr>";
				$('#tbCupero').append(htmlTbCupero);
			}
		}
	}
	rellenarArbol(0, 1);
</c:if>



$(document).ready(function() {
    $(":input:visible:enabled").each(function() {
        if (($(this).attr('type') == 'text') && ($(this).is('input'))){
            $(this).focus();
            return false; 
        }
        if ($(this).is('textarea')){
            $(this).focus();
            return false; 
        } 
    });
});

var fich_csv_gml ="";
var fich_shp ="";
var fich_shx ="";
var fich_dbf ="";

$(document).ready(function() {
	<c:if test="${fuenteDto.fich_csv_gml!=null}">
		fich_csv_gml="${fuenteDto.fich_csv_gml}";
	</c:if>	
	<c:if test="${fuenteDto.fich_shp!=null}">
		fich_shp ="${fuenteDto.fich_shp}";
	</c:if>	
	<c:if test="${fuenteDto.fich_shx!=null}">
		fich_shx ="${fuenteDto.fich_shx}";
	</c:if>	
	<c:if test="${fuenteDto.fich_dbf!=null}">
		fich_dbf ="${fuenteDto.fich_dbf}";
	</c:if>	
	
});

	$(function(){
		$("#formulario").submit(function() {
			if ($('#accion').val() != "guarda")
				return true;
				
			var arrayErros = [];
			var nombre = $('#nombre').val();
			var tipo = $('#tipo').val();
			
			  
			$('#lista_alertas_JS').empty();
			  
			if ($.trim(nombre).length == 0) {
				arrayErros.push($('#error_nombre').val());
			}
			if (tipo.length == 0) {
				arrayErros.push($('#error_tipo').val());
			} else{ 
				if ((tipo == 2 || tipo == 5 || tipo == 6) && $('#infoConexion').val().length == 0) {
					arrayErros.push($('#error_conexion').val());
				}
					
					if($("#idDto").val()==0){
						if (tipo == 1 && (  $('#fich_shp_f').val().length == 0 || $('#fich_dbf_f').val().length == 0)) {
							arrayErros.push($('#error_ficheroSH').val());
						} else if ((tipo == 3 || tipo == 4) && $('#fich_csv_gml_f').val().length == 0) {
							arrayErros.push($('#error_ficheroCSV').val());
						} 
					}else{
						if (tipo == 1 && (  ($('#fich_shp_f').val().length == 0 &&  fich_shp.length==0) ||
								($('#fich_shx_f').val().length == 0 && fich_shx.length==0) ||
								($('#fich_dbf_f').val().length == 0 && fich_dbf.length==0) )) {
							arrayErros.push($('#error_ficheroSH').val());
						} else if ((tipo == 3 || tipo == 4) && $('#fich_csv_gml_f').val().length == 0 && fich_csv_gml.length==0) {
							arrayErros.push($('#error_ficheroCSV').val());
						} 
					}	
			}
			
			if (arrayErros.length > 0) {
				var ul = document.createElement("ul");
			
				$.each(arrayErros, function(i, el) {
					var li = document.createElement("li");
					li.innerHTML = el;
					ul.appendChild(li);						
				})
				
				$('#lista_alertas_JS').append(ul);
				$('#alertasJS').removeClass('oculto');
				
				return false;
			} else {
				$('#alertasJS').addClass('oculto');
			}
			     
			return true;
		});
		
		$('#formulario input').keydown(function(e) {
			 if (e.keyCode == 13) {
				 $('#formulario').submit();
			}
		 });
	});
</script>	

<script type="text/javascript">
function enviarFormulario() {
	if ($('.enlaceTablaIndicador').length>0) {
		var conf = confirm("<spring:message code="jsp.fuente.aviso.modificacion.indicadores"/>");
		if ( conf )
			$('#formulario').submit();
	} else 
		$('#formulario').submit();
}
</script>