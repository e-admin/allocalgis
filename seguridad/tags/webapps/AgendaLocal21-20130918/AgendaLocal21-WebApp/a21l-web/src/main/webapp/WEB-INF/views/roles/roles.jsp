<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<c:url var="urlBase" value="/" />
<script src="js/util/comun/cambioMenus.js"></script>
<div class="cuerpoprincipal">
	<div class="areamenu">
		<ul class="menusecundario">
			<li>
				<a href="#" onclick="cambioMenu('usuarios.htm'); return false;">
					<spring:message code="jsp.usuarios.usuarios.gestionUsuarios"/>
				</a>
			</li>
			<c:if test="${usuarioAdmin}">
			<li class="on">
				<a href="#" onclick="cambioMenu('roles.htm'); return false;">
					<spring:message code="jsp.usuarios.usuarios.gestionRoles"/>
				</a>
			</li>
			</c:if>
		</ul>
	</div>
	
	<div class="areacentral">
		<h2 class="inf15">
			<spring:message code="jsp.roles.roles.listaRoles"/>
		</h2>
		
		<div class="fizq ancho80">
			<c:if test="${rolDto.resultadoOperacion.resultado == 'exitoCrear'}">
				<div class="controlAlertas">
					<div class="ui-widget">
						<div class="success ui-corner-all">
							<p>
								<span class="ui-icon ui-icon-circle-check fizq"></span>
								<spring:message code="jsp.roles.rol.exito.crear" />
							</p>
						</div>
					</div>
				</div>
				<br/>
			</c:if>
			
			<c:if test="${rolDto.resultadoOperacion.resultado == 'exitoGuardar'}">
				<div class="controlAlertas">
					<div class="ui-widget">
						<div class="success ui-corner-all">
							<p>
								<span class="ui-icon ui-icon-circle-check fizq"></span>
								<spring:message code="jsp.roles.rol.exito.guardar" />
							</p>
						</div>
					</div>
				</div>
				<br/>
			</c:if>
	
			<c:if test="${resultadoOperacion == 'exitoBorrar'}">
				<div class="controlAlertas">
					<div class="ui-widget">
						<div class="success ui-corner-all">
							<p>
								<span class="ui-icon ui-icon-circle-check fizq"></span>
								<spring:message code="jsp.roles.rol.exito.borrar" />
							</p>
						</div>
					</div>
				</div>
				<br/>
			</c:if>
			
			<c:if test="${resultadoOperacion == 'errorBorrar'}">
				<div class="controlAlertas">
					<div class="ui-widget">
						<div class="ui-state-error ui-corner-all">
							<p>
								<span class="ui-icon ui-icon-circle-check fizq"></span>
								<spring:message code="jsp.roles.rol.error.borrar" />
							</p>
						</div>
					</div>
				</div>
				<br/>
			</c:if>
			
			<c:if test="${resultadoOperacion == 'entidadUtilizada'}">
				<div class="controlAlertas">
					<div class="ui-widget">
						<div class="ui-state-error ui-corner-all">
							<p>
								<span class="ui-icon ui-icon-circle-check fizq"></span>
								<spring:message code="jsp.roles.rol.entidad.utilizada" />
							</p>
						</div>
					</div>
				</div>
				<br/>
			</c:if>
			
			<c:if test="${passwordCambiadaExito != null}">
			<div class="controlAlertas">	
				<div class="ui-widget">
					<div class="success ui-corner-all"> 
						<p>
							<span class="ui-icon ui-icon-circle-check fizq"></span>
							<spring:message code="${passwordCambiadaExito}" />
						</p>
					</div>
				</div>
			</div>
			</c:if>
			
			<table class="tablasTrabajo" cellpadding="0" cellspacing="0">
				<tbody>
					<tr>
						<th class="tablaTitulo izquierda" style="width: 25%;"><spring:message code="jsp.roles.roles.rol"/></th>
					</tr>
					<c:forEach items="${listaRoles}" var="rolDto">
						<tr onmouseout="this.className=''" onmouseover="this.className='destacadoTablas'">
							<td class="tablasDetalle izquierda tupla${rolDto.id}"><a class="tablasTrabajo" idRol="${rolDto.id}" href="#">${rolDto.nombre}</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<div class="fder ancho17">
			<ul class="operaciones">
				<c:if test="${usuarioAdmin}">
					<li><a href="" id="enlaceModificar"><spring:message code="elementos.jsp.boton.modificar"/></a></li>
					<li><a href="" id="enlaceEliminar"><spring:message code="elementos.jsp.boton.eliminar"/></a></li>
				</c:if>
			</ul>
		</div>
	</div>
	<div class="clear"></div>
	<c:if test="${usuarioAdmin}">
		<div class="botones_accion">
			<a href="?accion=crearRol" class="boton izq10 sup10 fondo_blanco fder"><spring:message code="jsp.roles.rol.crearRol"/></a>			
		</div>
	</c:if>
</div>

<!-- DIALOGO MODAL -->
<div id="dialogoModalConfirmacion" style="display: none;background-color: white;">
	<div class="ui-widget">
 		<div style="padding: 0.2em 0.7em;" class=""> 
			<p>
				<span style="float: left; margin-right: 0.3em;" class=""></span>
				<spring:message code="jsp.roles.rol.dialogoEliminar"/>
			</p>
 		</div>
	</div>
</div>		
<script type="text/javascript">
	var idRol=0;

	$("a.tablasTrabajo").click(function(){
		if(idRol != null && idRol!= 0){
			$(".tupla"+idRol).removeClass("fondoGris");
		}
		
		idRol = $(this).attr('idRol');
		$(".tupla"+idRol).addClass("fondoGris");
		
		return false;
	});
	
	$("#enlaceModificar").click(function(){
		if(idRol == null || idRol == 0){
			alert("<spring:message code="jsp.roles.roles.seleccionarRol"/>");
			return false;
		}
		
		window.location="${urlBase}roles.htm?accion=mostrarDetallesRol&id="+idRol;
		$(this).attr("disabled", true);
		
		return false;
	});
	
	$("#enlaceEliminar").click(function(){
		if(idRol == null || idRol == 0){
			alert("<spring:message code="jsp.roles.roles.seleccionarRol"/>");
			
			return false;
		}
		
		var dialogoModalConfirmacion=$( "#dialogoModalConfirmacion" ).dialog({
			 autoOpen: false,
			 show: "blind",
			 hide: "explode", 
			 width: 400,
			 height: 200,
			 modal: true,
			 resizable: false,
			 buttons: {
				 "Aceptar": function() {
					window.location="${urlBase}roles.htm?accion=eliminarRol&id="+idRol;
					$( this ).dialog( "close" );
					
					return false;
				 },
				 "Cancelar": function() {
					 $( this ).dialog( "close" );
					 
					 return false;
				 }
			 }
		}); 
		
		$(dialogoModalConfirmacion).dialog("open");
		return false;
	});
</script>