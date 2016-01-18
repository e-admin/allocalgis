<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<script src="js/util/comun/cambioMenu.js"></script>

<div class="cuerpoprincipal">
	<div class="areamenu">
		<ul class="menusecundario">
			<li class="on">
				<a href="#" onclick="cambioMenu('usuarios.htm'); return false;">
					<spring:message code="jsp.usuarios.usuarios.gestionUsuarios"/>
				</a>
			</li>
			<c:if test="${usuarioAdmin}">
				<li>
					<a href="#" onclick="cambioMenu('roles.htm'); return false;">
						<spring:message code="jsp.usuarios.usuarios.gestionRoles"/>
					</a>
				</li>
			</c:if>
		</ul>
	</div>
	<div class="areacentral">
		<h2><spring:message code="jsp.usuarios.usuarioAsignarPermisos.titulo"/>&nbsp; ${usuarioDto.nombre}</h2>
		<br/>
		
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
		
		<form:form id="formularioAsignarPermisos" modelAttribute="usuarioDto" action="?">
			<input type="hidden" id="accion" name="accion" value="guardarPermisos" />
			<input type="hidden" name="id" value="${usuarioDto.id}" />

			<div class="contenedorarbol">
			<table class="tabla_jerarquia" border="0" cellspacing="0" cellpadding="0" id="tablaIndicadores">
				<thead>
					<tr>
						<th style="width: 90%;text-align: left;" id="tuplaCategoria0"><a href="#" class="enlaceTablaCategoria" idCategoria="0"><spring:message code="jsp.indicadores.indicadores.raiz"/></a></th>
						<th style="width: 5%;"></th>
						<th style="width: 5%;"></th>
					</tr>
				</thead>
				
			</table>
		</div>
		</form:form>
	</div>
	<div class="clear"></div>
	<div class="botones_accion">
		<a href="?" class="boton izq10 sup10 fondo_blanco fder"><spring:message code="elementos.jsp.boton.cancelar"/></a>
		<a href="#" onclick="$('#formularioAsignarPermisos').submit();return false;" class="boton izq10 sup10 fondo_blanco fder"><spring:message code="elementos.jsp.boton.aceptar"/></a>
	</div>
</div>

<!-- DIALOGO MODAL -->
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
	        if (($(this).attr('type') == 'checkbox')){
	            $(this).focus();
	            return false; 
	        }  
	    });
	    InicializarTabla();
	});

	$(function(){
		 $('#formularioAsignarPermisos input').keydown(function(e) {
			 if (e.keyCode == 13) {
				 $('#formularioAsignarPermisos').submit();
			}
		 });
	});
	function InicializarTabla()
	{
		<c:forEach items="${mapaCategoriasTabla[0]}" var="categoria">
			dibujarFilaCategoriaPub('tablaIndicadores', '0', '${categoria.id}', '${categoria.nombre}', 20);
		</c:forEach>

	 	<c:forEach items="${mapaIndicadoresTabla[0]}" var="indicador">
			dibujarFilaIndicadorPub('tablaIndicadores', '0', '${indicador.id}', '${indicador.nombre}','${indicador.urlPublicacion}', '${indicador.urlPublicacionCorta}', '${indicador.numUsuariosPublicacion}', '${indicador.loginUltimaPeticion}', 20);
		</c:forEach>
	}

	function dibujarFilaIndicadorPub(idTabla, idCategoriaPadre, idIndicador, nombreIndicador, urlPublicacion, urlPublicacionCorta, numUsuarios, loginUsuario, margenIzq)
	{
		var table = document.getElementById(idTabla);

		var rowCount = table.rows.length;
		var row = table.insertRow(rowCount);
		row.id ="idIndicador" + idIndicador;
		row.className = "idcategoriapadre-" + idCategoriaPadre + "_tr";
		
		
		// [1] Nombre del indicador
		var cell = row.insertCell(0);
		cell.id = "tuplaIndicador" + idIndicador;
		cell.className = "no_cat";
		cell.style.paddingLeft = margenIzq + "px";
		cell.innerHTML=nombreIndicador;
		
		
// 		// [2] Enlace a la página de consulta
		var cell2 = row.insertCell(1);
		var marcado=false;
		<c:forEach items="${usuarioDto.eltosJerarquia}" var="item">
			if (idIndicador == '${item}'){
				marcado=true;
				
			}
		</c:forEach>
		if(marcado){
			cell2.innerHTML='<input type="checkbox" name="eltosSeleccionados" value="'+idIndicador+'" checked="checked" />';
		}else{
			cell2.innerHTML='<input type="checkbox" name="eltosSeleccionados" value="'+idIndicador+'"/>';
		}
		
// 		// [3] Usuario(s) que solicitaron la publicación
		var cell3 = row.insertCell(2);
		<c:forEach items="${mapaEltosRol}" var="item">
			if(idIndicador=='${item.key}'){
				cell3.innerHTML="<img alt='<spring:message code='jsp.usuarios.usuarioAsignarPermisos.elemento.deshabilitado'/><c:forEach items='${mapaEltosRol[item.key]}' var='nombreRol'>${nombreRol}</c:forEach>"+ 
				"src='images/interrogacion.png' title='<spring:message code='jsp.usuarios.usuarioAsignarPermisos.elemento.deshabilitado'/><c:forEach items='${mapaEltosRol[item.key]}' var='nombreRol'> ${nombreRol}</c:forEach>' src='images/interrogacion.png'>";
			}
		</c:forEach>
	}

	function dibujarFilaCategoriaPub(idTabla, idCategoriaPadre, idCategoria, nombreCategoria, margenIzq)
	{
		var nombreClassFila = "idcategoriapadre-" + idCategoriaPadre + "_tr";

		// Se dibuja la celda correspondiente a la categoría
		var table = document.getElementById(idTabla);

		var rowCount = table.rows.length;
		var row = table.insertRow(rowCount);
		row.id = "idCategoria" + idCategoria;
		row.className = nombreClassFila;
	
		// [0] Nombre de la categoría
		var cell = row.insertCell(0);
		cell.id = "tuplaCategoria" + idCategoria;
		cell.className = "cat destacadotablas";
		cell.style.paddingLeft = margenIzq + "px";
		cell.innerHTML="<a href='#' class='enlaceTablaCategoria' id='idCategoria"+idCategoria+"'>"+nombreCategoria+"</a>";
		
		
		// [1] Celda check
		var cell2 = row.insertCell(1);
		var marcado=false;
		<c:forEach items="${usuarioDto.eltosJerarquia}" var="item">
			if (idCategoria == '${item}'){
				marcado=true;
				
			}
		</c:forEach>
		if(marcado){
			cell2.innerHTML='<input type="checkbox" name="eltosSeleccionados" value="'+idCategoria+'" checked="checked" />';
		}else{
			cell2.innerHTML='<input type="checkbox" name="eltosSeleccionados" value="'+idCategoria+'"/>';
		}
		
		// [2] Celda en blanco
		var cell3 = row.insertCell(2);
		<c:forEach items="${mapaEltosRol}" var="item">
			if(idCategoria=='${item.key}'){
				cell3.innerHTML="<img alt='<spring:message code='jsp.usuarios.usuarioAsignarPermisos.elemento.deshabilitado'/><c:forEach items='${mapaEltosRol[item.key]}' var='nombreRol'>${nombreRol}</c:forEach>"+ 
				"src='images/interrogacion.png' title='<spring:message code='jsp.usuarios.usuarioAsignarPermisos.elemento.deshabilitado'/><c:forEach items='${mapaEltosRol[item.key]}' var='nombreRol'> ${nombreRol}</c:forEach>' src='images/interrogacion.png'>";
			}
		</c:forEach>
		
		// Se dibujan las filas correspondientes con sus Categorías hijo
		 <c:forEach items="${mapaCategoriasTabla}" var="entry">
		 	if (idCategoria == '${entry.key}')
		 	{
		 		<c:forEach items="${entry.value}" var="categoriaHija">
					dibujarFilaCategoriaPub(idTabla, idCategoria, '${categoriaHija.id}', '${categoriaHija.nombre}', margenIzq + 20);
				</c:forEach>
			}
		</c:forEach>
		
		// Se dibujan las filas correspondientes con sus Indicadores hijo
		 <c:forEach items="${mapaIndicadoresTabla}" var="entry">
		 	if (idCategoria == '${entry.key}')
		 	{
		 		<c:forEach items="${entry.value}" var="indicadorHijo">
					dibujarFilaIndicadorPub(idTabla, idCategoria, '${indicadorHijo.id}', '${indicadorHijo.nombre}','${indicadorHijo.urlPublicacion}', '${indicadorHijo.urlPublicacionCorta}', '${indicadorHijo.numUsuariosPublicacion}', '${indicadorHijo.loginUltimaPeticion}', margenIzq + 20);
				</c:forEach>
			}
		</c:forEach>
	}
</script>