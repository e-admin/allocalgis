<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<script src="js/util/comun/cambioMenu.js"></script>
<div class="cuerpoprincipal">
	<%@ include file="usuarioMenuIzq.jsp"%>
	<div class="areacentral">
		<h2><spring:message code="jsp.usuarios.usuarioModificar.modificarUsuario"/>&nbsp ${usuarioDto.nombre}</h2>
		<br/>
		
		<c:if test="${usuarioDto.resultadoOperacion.resultado == 'exitoGuardar'}">
            <div class="controlAlertas">	
				<div class="ui-widget">
            		<div class="success ui-corner-all"> 
						<p>
							<span class="ui-icon ui-icon-circle-check fizq"></span>
				          	<spring:message code="jsp.usuarios.usuario.exito.mofidicar" />
			         	</p>
			  	 	</div>
         		</div>
         	</div> 
         	<br/>    
          </c:if>
          
          <!--  mensajes de error -->
			<spring:hasBindErrors name="usuarioDto">
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
			          <br/>
			</spring:hasBindErrors>
			
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
          
		<form:form id="formulario"  modelAttribute="usuarioDto" action="?">
			<input type="hidden" id="accion" name="accion" value="guardarModificacionUsuario" />
			<input type="hidden" name="id" value="${usuarioDto.id}" />
			<input type="hidden" name="activo" value="${usuarioDto.activo}" />
			<input type="hidden" name="fechaRegistro" value="<fmt:formatDate value="${usuarioDto.fechaRegistro}" pattern="dd/MM/yyyy"/>" />
			
			<div>
				<div class="linea">
					<label class="label fizq"><spring:message code="jsp.usuarios.usuario.nombre" /></label>
					<form:input path="nombre" size="60" maxlength="255" type="text" />
				</div>
				
				<div class="linea">
					<label class="label fizq"><spring:message code="jsp.usuarios.usuario.descripcion" /></label>
					<form:textarea path="descripcion" size="60" maxlength="255"  cssStyle="width:57%;" rows="5"/>
				</div>
				
				<div class="linea">
					<label class="label fizq"><spring:message code="jsp.usuarios.usuario.email" /></label>
					<form:input path="email" size="60" maxlength="255" type="text"/>
				</div>
				
				<c:if test="${!(idUsuarioActual==usuarioDto.id)}">
					<div class="linea">
						<label class="label fizq"><spring:message code="jsp.usuarios.usuario.usuario.administrador" /></label>
						<form:checkbox path="esAdmin"/>
					</div>
				</c:if>
				<c:if test="${idUsuarioActual==usuarioDto.id}">
					<input type="hidden" name="esAdmin" value="${usuarioDto.esAdmin}">
				</c:if>
				
				<div class="linea">
					<label class="label fizq"><spring:message code="jsp.usuarios.usuario.usuario.fechaRegistro" /></label>
					<fmt:formatDate value="${usuarioDto.fechaRegistro}" pattern="dd/MM/yyyy"/>
				</div>
			</div>
	</form:form>
	</div>
	<div class="clear"></div>
	<div class="botones_accion">
		<a href="usuarios.htm" class="boton izq10 sup10 fondo_blanco fder"><spring:message code="elementos.jsp.boton.cancelar"/></a>
		<a href="#" onclick="$('#formulario').submit();return false;" class="boton izq10 sup10 fondo_blanco fder"><spring:message code="elementos.jsp.boton.aceptar"/></a>
	</div>	
	<div class="clear"></div>
</div>

<!-- DIALOGO MODAL -->   
<div id="dialogoModalConfirmacion" style="display: none;background-color: white;">
    <div class="ui-widget">
        <div style="padding: 0.2em 0.7em;" class=""> 
            <p>
                <span style="float: left; margin-right: 0.3em;" class=""></span>
                <spring:message code="jsp.usuarios.usuarios.dialogoConfirmacionCambios"/>
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
	});
	
	$(function(){
		 $('#formulario input').keydown(function(e) {
			 if (e.keyCode == 13) {
				 $('#formulario').submit();
			}
		 });
	});
</script>