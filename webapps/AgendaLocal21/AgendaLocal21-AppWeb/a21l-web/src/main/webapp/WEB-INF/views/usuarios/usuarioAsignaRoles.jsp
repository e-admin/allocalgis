<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>  
<script src="js/util/md5.js"></script>
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
		<h2><spring:message code="jsp.usuarios.usuarioAsignaRoles.titulo"/>&nbsp ${usuarioDto.nombre}</h2>
		
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
		
		<form:form action="?" modelAttribute="usuarioDto" id="formulario">
			<input type="hidden" name="accion" value="guardaRoles"/>
			<input type="hidden" name="id" value="${usuarioDto.id}"/>
			
			<div class="fizq" style="width: 35%;">
				<b><spring:message code="jsp.usuarios.usuarioAsignaRoles.rolesDisponibles"/></b>
				<select  id="rolesDisponibles" size="15" style="width: 100%;" multiple="multiple">
					<c:forEach items="${listaRolesNoUsuario}" var="rol">
						<option value="${rol.id}">${rol.nombre}</option>
					</c:forEach>
				</select>
			</div>
			
			<div class="fizq" style="width: 25%;">
					<ul class="sinpuntos">
						<li  style="position: relative;right: -10%;"><a href="" id="btnAnadirRoles"><img alt="" src="images/botonDobleFlechaDrch.png"></a></li>
						<li style="position: relative;top: 20px;right: -10%;"><a href="" id="btnQuitarRoles"><img alt="" src="images/botonDobleFlechaIzq.png"></a></li>
					</ul>	
			</div>
			<div class="fizq" style="width: 35%;">
				<b><spring:message code="jsp.usuarios.usuarioAsignaRoles.rolesAsignados"/></b>
				<select id="rolesAsignados" size="15" style="width: 100%;" multiple="multiple">
					<c:forEach items="${listaRolesUsuario}" var="rol">
						<option value="${rol.id}">${rol.nombre}</option>
					</c:forEach>
				</select>
			</div>

		</form:form>
	
	</div>
	<div class="clear"></div>
	<div class="botones_accion">
		<a  href="?" class="boton izq10 sup10 fondo_blanco fder"><spring:message code="elementos.jsp.boton.cancelar"/></a>
		<a href="#" onclick="$('#formulario').submit();return false;" class="boton izq10 sup10 fondo_blanco fder"><spring:message code="elementos.jsp.boton.aceptar"/></a>
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
</div>

<script type="text/javascript">
	$(document).ready(function() {
		$('#rolesDisponibles').focus();
	});

	$(function(){
		incializarListaRoles();
		
		$("#btnAnadirRoles").click(function(){
			andirRoles();
			return false;
		});
		
		$("#btnQuitarRoles").click(function(){
			quitarRoles();
			return false;
		});
		
		$('#formulario select, div').keydown(function(e) {
			 if (e.keyCode == 13) {
				 $('#formulario').submit();
			}
		 });
		
	});
	
	function andirRoles(){
		$("#rolesDisponibles option:selected").each(function (){
			$("#rolesAsignados").append("<option value="+$(this).val()+">"+$(this).text()+"</option>");
			$("#formulario").append("<input type='hidden' id='roles"+$(this).val()+"' name='roles' value="+$(this).val()+" />");
			$(this).remove();
		});
	};
	
	
	function quitarRoles(){
		$("#rolesAsignados option:selected").each(function (){
			$("#rolesDisponibles").append("<option value="+$(this).val()+">"+$(this).text()+"</option>");
			$("#roles"+$(this).val()).remove();
			$(this).remove();
		});
	}
	
	function incializarListaRoles(){
		$("#rolesAsignados option").each(function (){
			$("#formulario").append("<input type='hidden' id='roles"+$(this).val()+"' name='roles' value="+$(this).val()+" />");
		});
		return false;
	}
</script>