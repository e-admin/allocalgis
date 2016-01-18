<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script src="js/util/md5.js"></script>
<script src="js/util/comun/cambioMenus.js"></script>
<div class="cuerpoprincipal">
	<div class="areacompleta">
		<div style="margin-left:18%">
			<h2><spring:message code="jsp.usuarios.usuarioCambiarContrasenha.cambiarContrasenhaUsuario"/>&nbsp ${usuarioDto.nombre}</h2>
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
				
				<c:if test="${usuarioDto.resultadoOperacion.resultado == 'exitoGuardar'}">
		            	<div class="controlAlertas">	
							<div class="ui-widget">
		            			<div class="success ui-corner-all"> 
									<p>
										<span class="ui-icon ui-icon-circle-check fizq"></span>
						          		<spring:message code="jsp.usuarios.usuarioCambiarContrasenha.exito" />
					         		</p>
					  	 		</div>
		         			</div>
		         		</div>
		         		<br/>     
		     		</c:if>
				
				<form:form id="formulario"  modelAttribute="usuarioDto" action="?">
					<input type="hidden" id="accion" name="accion" value="guardarContrasenhaNueva" />
					<input type="hidden" name="id" value="${usuarioDto.id}" />
					<input type="hidden" id="passwordH" name="password" value=""/>
					<input type="hidden" id="passwordConfirmH" name="passwordConfirm" value=""/>
					<input type="hidden" id="passwordOldH" name="passwordOld" value=""/>
					<input type="hidden" id="referer" name="referer" value="${urlOrigen}"/>
					
					<div class="linea">
							<label class="label fizq"><spring:message code="jsp.usuarios.usuarioCambiarContrasenha.contrasenhaAntigua" /></label>
							<input type="password" id="passwordOldF" size="30" maxlength="15" type="text" />
					</div>
					
					<div class="linea">
							<label class="label fizq"><spring:message code="jsp.usuarios.usuarioRestablecerContrasenha.contrasenhaNueva" /></label>
							<input type="password" id="passwordF" size="30" maxlength="15" type="text" />
					</div>
					
					<div class="linea">
							<label class="label fizq"><spring:message code="jsp.usuarios.usuarioRestablecerContrasenha.ReintroducirContrasenhaNueva" /></label>
							<input type="password" id="passwordConfirmF" size="30" maxlength="15" type="text" />
					</div>
				</form:form>
			</div>
		</div>
		<div class="clear"></div>
		<div class="botones_accion">
			<a href="${urlOrigen}" class="boton izq10 sup10 fondo_blanco fder"><spring:message code="elementos.jsp.boton.cancelar"/></a>
			<a href="#" onclick="$('#formulario').submit();return false;" class="boton izq10 sup10 fondo_blanco fder"><spring:message code="elementos.jsp.boton.aceptar"/></a>
		</div>	
</div>
<script type="text/javascript">		
	$(document).ready(function() {
		$('#passwordOldF').focus();
	});
	
	$(function(){
		  $("#formulario").submit(function() {
		        var passwordOldF = $('#passwordOldF').val(); 
		        var md5OldF = calcMD5(passwordOldF);
		        
		        var passwordF = $('#passwordF').val();
		        var md5F = calcMD5(passwordF);
		        
		        var passwordConfirmF= $('#passwordConfirmF').val();
		        var md5ConfirmF = calcMD5(passwordConfirmF);
		        
		        if (passwordF.length < 7 || passwordConfirmF.length < 7)
	        	{
        		alert('<spring:message code="jsp.usuarios.usuario.password.corta"/>');	
        		return false;
	        	}
		        
		        $('#passwordOldH').val(md5OldF);
		        $('#passwordH').val(md5F);
		        $('#passwordConfirmH').val(md5ConfirmF);
		       
		    	return true;
		    });
		  
		  $('#formulario input').keydown(function(e) {
				 if (e.keyCode == 13) {
					 $('#formulario').submit();
				}
		  });
		
	});
</script>
 