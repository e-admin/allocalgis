<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script src="js/util/comun/cambioMenus.js"></script>
<script src="js/util/md5.js"></script>
<div class="cuerpoprincipal">
		<%@ include file="usuarioMenuIzq.jsp"%>
	<div class="areacentral">
		<h2><spring:message code="jsp.usuarios.usuarioRestablecerContrasenha.cambiarContrasenhaUsuario"/>&nbsp ${usuarioDto.nombre}</h2>
		
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
				<input type="hidden" id="accion" name="accion" value="guardarRestContrasenhaUsuario" />
				<input type="hidden" name="id" value="${usuarioDto.id}" />
				<input type="hidden" id="passwordH" name="password" value=""/>
				<input type="hidden" id="passwordConfirmH" name="passwordConfirm" value=""/>
				
				<div class="linea">
						<label class="label fizq"><spring:message code="jsp.usuarios.usuarioRestablecerContrasenha.contrasenhaNueva" /></label>
						<input type="password" id="passwordF" size="60" maxlength="15" type="text" value="" />
				</div>
				
				<div class="linea">
						<label class="label fizq"><spring:message code="jsp.usuarios.usuarioRestablecerContrasenha.ReintroducirContrasenhaNueva" /></label>
						<input type="password"  id="passwordConfirmF" size="60" maxlength="15" type="text" />
				</div>
			</form:form>
			</div>
		<div class="clear"></div>
		<div class="botones_accion">
			<a href="usuarios.htm" class="boton izq10 sup10 fondo_blanco fder"><spring:message code="elementos.jsp.boton.cancelar"/></a>
			<a href="#" onclick="$('#formulario').submit();return false;" class="boton izq10 sup10 fondo_blanco fder"><spring:message code="elementos.jsp.boton.aceptar"/></a>
		</div>	
</div>
<script type="text/javascript">
	$(document).ready(function() {
		$('#passwordF').focus();
	});

	$(function(){
		  $("#formulario").submit(function() {
		        var password = $('#passwordF').val();
		        var md5 = calcMD5(password);
		        var passwordC = $('#passwordConfirmF').val();
		        var md5C = calcMD5(password);
		        password = $('#passwordF').val();
		        
		        if ( password.length<7 ) {
		        	alert('<spring:message code="jsp.usuarios.usuario.password.corta"/>');	
		        	return false;
		        }
		        
		        md5 = calcMD5(password);
		        $('#passwordH').val(md5);
		        $('#passwordConfirmH').val(md5C);
		        
		    	return true;
		    });
		  
		  $('#formulario input').keydown(function(e) {
			    if (e.keyCode == 13) {
			        $('#formulario').submit();
			    }
			});
		
	});
</script>		