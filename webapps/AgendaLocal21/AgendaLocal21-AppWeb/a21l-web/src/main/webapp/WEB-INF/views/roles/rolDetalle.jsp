<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<script src="js/util/comun/cambioMenu.js"></script>
<div class="cuerpoprincipal">
	<%@ include file="../usuarios/usuarioMenuIzq.jsp"%>
	<div class="areacentral">
		<h2><spring:message code="jsp.roles.rolModificar.modificarRol"/>&nbsp; ${rolDto.nombre}</h2>
		<br/>
		<c:if test="${rolDto.resultadoOperacion.resultado == 'exitoGuardar'}">
            <div class="controlAlertas">	
				<div class="ui-widget">
            		<div class="success ui-corner-all"> 
						<p>
							<span class="ui-icon ui-icon-circle-check fizq"></span>
				          	<spring:message code="jsp.roles.rol.exito.mofidicar" />
			         	</p>
			  	 	</div>
         		</div>
         	</div>
         	<br/>
          </c:if>
          
          <!--  mensajes de error -->
			<spring:hasBindErrors name="rolDto">
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
					</div>
				</div>
			</div>
	          <br/>
			</spring:hasBindErrors>
          
		<form:form id="formularioModificacionRol"  modelAttribute="rolDto" action="?">
			<input type="hidden" id="accion" name="accion" value="modificarRol" />
			<input type="hidden" name="id" value="${rolDto.id}" />
			
			<div>
				<div class="linea">
					<label class="label fizq"><spring:message code="jsp.roles.rol.nombre" /></label>
					<form:input path="nombre" size="50" maxlength="25" type="text" />
				</div>
				
				<div class="linea">
					<label class="label fizq"><spring:message code="jsp.roles.rol.descripcion" /></label>
					<form:textarea path="descripcion" size="50" maxlength="4000"  cssStyle="width:49%;" rows="5"/>
				</div>
			</div>
			
			<br/>
				<h2><spring:message code="jsp.roles.rol.indicadores" /></h2>
				<br/>
				<div class="contenedorarbol">
				<table class="tabla_jerarquia" border="0" cellspacing="0" cellpadding="0" id="tablaIndicadores">
					<thead>
						<tr>
							<th style="width: 95%;text-align: left;" id="tuplaCategoria0"><a href="#" class="enlaceTablaCategoria" idCategoria="0"><spring:message code="jsp.indicadores.indicadores.raiz"/></a></th>
							<th style="width: 5%;"></th>
						</tr>
					</thead>
					
				
				</table>
			</div>
	</form:form>
	</div>
	<div class="clear"></div>
	<div class="botones_accion">
		<a href="roles.htm" class="boton izq10 sup10 fondo_blanco fder"><spring:message code="elementos.jsp.boton.cancelar"/></a>
		<a href="#" onclick="$('#formularioModificacionRol').submit();return false;" class="boton izq10 sup10 fondo_blanco fder"><spring:message code="elementos.jsp.boton.aceptar"/></a>
	</div>	
	<div class="clear"></div>
</div>

<!-- DIALOGO MODAL -->   
<div id="dialogoModalConfirmacion" style="display:none; background-color: white;">
    <div class="ui-widget">
        <div style="padding: 0.2em 0.7em;" class=""> 
            <p>
                <span style="float: left; margin-right: 0.3em;" class=""></span>
                <spring:message code="elementos.jsp.dialogoConfirmacionCambios"/>
            </p>
        </div>
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
	$(document).ready(function() {
		$('#nombre').focus();
		InicializarTabla();
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
		<c:forEach items="${rolDto.eltosJerarquia}" var="item">
			if (idIndicador == '${item}'){
				marcado=true;
				
			}
		</c:forEach>
		if(marcado){
			cell2.innerHTML='<input type="checkbox" name="eltosSeleccionados" value="'+idIndicador+'" checked="checked" />';
		}else{
			cell2.innerHTML='<input type="checkbox" name="eltosSeleccionados" value="'+idIndicador+'"/>';
		}
		
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
		<c:forEach items="${rolDto.eltosJerarquia}" var="item">
			if (idCategoria == '${item}'){
				marcado=true;
				
			}
		</c:forEach>
		if(marcado){
			cell2.innerHTML='<input type="checkbox" name="eltosSeleccionados" value="'+idCategoria+'" checked="checked" />';
		}else{
			cell2.innerHTML='<input type="checkbox" name="eltosSeleccionados" value="'+idCategoria+'"/>';
		}
		
		
		
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