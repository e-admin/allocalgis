<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<script src="js/util/md5.js"></script>
<script src="js/util/comun/cambioMenu.js"></script>
<div class="cuerpoprincipal">
	<%@ include file="usuarioMenuIzq.jsp"%>
	<div class="areacentral">
		<h2><spring:message code="jsp.usuarios.usuario.crearUsuario"/></h2>
		<br/>
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
					</div>
				</div>
			</div>
			<br/>
			</spring:hasBindErrors>
		
		
		<c:if test="${usuarioDto.resultadoOperacion.resultado == 'exitoGuardar'}">
            <div class="controlAlertas">	
				<div class="ui-widget">
            		<div class="success ui-corner-all"> 
						<p>
							<span class="ui-icon ui-icon-circle-check fizq"></span>
				          	<spring:message code="jsp.usuarios.usuario.exito.guardar" />
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
						<p><span class="ui-icon ui-icon-circle-check fizq"></span>
                        	<spring:message code="${passwordCambiadaExito}" />
                        </p>
                     </div>
                 </div>
            </div>
            </c:if>
		
		<form:form id="formulario"  modelAttribute="usuarioDto" action="?">
			<input type="hidden" id="accion" name="accion" value="guardarUsuario" />
			<input type="hidden" name="id" value="${usuarioDto.id}" />
			<input type="hidden" name="activo" value="${usuarioDto.activo}" />
			<input type="hidden" name="fechaRegistro" value="<fmt:formatDate value="${usuarioDto.fechaRegistro}" pattern="dd/MM/yyyy"/>" />
			<input type="hidden" id="passwordH" name="password" value=""/>
			<input type="hidden" id="passwordConfirmH" name="passwordConfirm" value=""/>
	
			<div onKeyPress="return aceptar(event)">
				<div class="linea">
					<label class="label fizq"><spring:message code="jsp.usuarios.usuario.identificador" /></label>
					<form:input cssClass="grid-8" path="login" size="50" maxlength="255" type="text"/>
				</div>
				
				<div class="linea">
					<label class="label fizq"><spring:message code="jsp.usuarios.usuario.password" /></label>
					<input type="password" id="passwordF" size="50" maxlength="15" type="text"/>
				</div>
					
				<div class="linea">
					<label class="label fizq"><spring:message code="jsp.usuarios.usuario.password.confirmar" /></label>
					<input type="password" id="passwordConfirmF" size="50" maxlength="15" type="text"/>
				</div>
					
				<div class="linea">
					<label class="label fizq"><spring:message code="jsp.usuarios.usuario.usuario.administrador" /></label>
					<form:checkbox path="esAdmin" />
				</div>
				<div class="clear"></div>
				<div class="linea">
					<label class="label fizq"><spring:message code="jsp.usuarios.usuario.nombre" /></label>
					<form:input path="nombre" size="50" maxlength="255" type="text" />
				</div>
					
				<div class="linea">
					<label class="label fizq"><spring:message code="jsp.usuarios.usuario.descripcion" /></label>
					<form:textarea path="descripcion" size="50" maxlength="255"  cssStyle="width:49%;" rows="5"/>
				</div>
					
				<div class="linea">
					<label class="label fizq"><spring:message code="jsp.usuarios.usuario.email" /></label>
					<form:input path="email" size="50" maxlength="255" type="text"/>
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
	$('#login').focus();
});

$(function(){
	
	 $("#formulario").submit(function() {
	        var password; 
	        var md5;
	        password = $('#passwordF').val();
	        
	        if ( password.length<7 ) {
	        	alert('<spring:message code="jsp.usuarios.usuario.password.corta"/>');	
	        	return false;
	        }
	        	       
	        md5 = calcMD5(password);
	        $('#passwordH').val(md5);
	        
	        password= $('#passwordConfirmF').val();
	        md5 = calcMD5(password);
	        $('#passwordConfirmH').val(md5);
	        
	    	return true;
	    });
	 
	 $('#formulario input').keydown(function(e) {
		 if (e.keyCode == 13) {
			 $('#formulario').submit();
		}
	 });

});

</script>