<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page import="org.springframework.context.i18n.LocaleContextHolder" %>
<ul class="menuprincipal">
<c:if test="${paginaActiva eq 'indicadores'}">
	<li class="on">
</c:if>
<c:if test="${paginaActiva != 'indicadores'}">
	<li>
</c:if>
		<a href="#" onclick="cambioMenu('indicadores.htm'); return false;">
			<spring:message code="jsp.menu.indicadores"/>
		</a>
	</li>
<c:if test="${!usuarioInvitado}">
	<c:if test="${paginaActiva eq 'fuentes'}">
		<li class="on">
	</c:if>
	<c:if test="${paginaActiva != 'fuentes'}">
		<li>
	</c:if>
		<a href="#" onclick="cambioMenu('fuentes.htm'); return false;">
			<spring:message code="jsp.menu.fuentes"/>
		</a>
	</li>
	<c:if test="${usuarioAdministrador}">
		<c:if test="${paginaActiva eq 'publicacion'}">
	<li class="on">
		</c:if>
		<c:if test="${paginaActiva != 'publicacion'}">
	<li>
		</c:if>
		<a href="#" onclick="cambioMenu('publicacionWeb.htm'); return false;">
			<spring:message code="jsp.menu.publicacion"/>
		</a>
	</li>
		<c:if test="${paginaActiva eq 'usuarios'}">
		<li class="on">
		</c:if>
		<c:if test="${paginaActiva != 'usuarios'}">
		<li>
		</c:if>
		<a href="#" onclick="cambioMenu('usuarios.htm'); return false;">
			<spring:message code="jsp.menu.usuarios"/>
		</a>
	</li>
	</c:if>
</c:if>
</ul>
<ul class="ul_cuenta fder sup6">
	<li>
		<a>${loginUsuarioActual}</a>
		<ul>
			<c:if test="${!usuarioInvitado}">
				<li><a href="usuarios.htm?accion=cambiarContrasenha"><spring:message code="jsp.menu.cambiarContrasenha"/></a></li>
			</c:if>
			<li>
				<a id="idioma" href="#"><spring:message code="elementos.jsp.idiomas"/></a>
				<ul id="idiomas" style="display:none;position:absolute;left:-201px;top:26px;">
					<c:if test="${idioma=='es'}">
						<li class="idiomas">
							<a class="idioma_seleccionado" href="#" onclick="return false;">
								<spring:message code="jsp.menu.espanhol" />
							</a>
						</li>
						<li class="idiomas">
							<a class="idiomas" href="cambiarIdioma.htm?setUserLocaleTo=gl">
								<spring:message code="jsp.menu.gallego" />
							</a>
						</li>
						<li class="idiomas">
							<a class="idiomas" href="cambiarIdioma.htm?setUserLocaleTo=ca">
								<spring:message code="jsp.menu.catalan" />
							</a>
						</li>
						<li class="idiomas">
							<a class="idiomas" href="cambiarIdioma.htm?setUserLocaleTo=eu">
								<spring:message code="jsp.menu.vasco" />
							</a>
						</li>
						<li class="idiomas">
							<a class="idiomas" href="cambiarIdioma.htm?setUserLocaleTo=en">
								<spring:message code="jsp.menu.ingles" />
							</a>
						</li>
					</c:if>
					<c:if test="${idioma=='gl'}">
						<li class="idiomas">
							<a class="idiomas" style="border-top: 1px solid #999;" href="cambiarIdioma.htm?setUserLocaleTo=es">
								<spring:message code="jsp.menu.espanhol" />
							</a>
						</li>
						<li class="idiomas">
							<a class="idioma_seleccionado" href="#" onclick="return false;">
								<spring:message code="jsp.menu.gallego" />
							</a>
						</li>
						<li class="idiomas">
							<a class="idiomas" href="cambiarIdioma.htm?setUserLocaleTo=ca">
								<spring:message code="jsp.menu.catalan" />
							</a>
						</li>
						<li class="idiomas">
							<a class="idiomas" href="cambiarIdioma.htm?setUserLocaleTo=eu">
								<spring:message code="jsp.menu.vasco" />
							</a>
						</li>
						<li class="idiomas">
							<a class="idiomas" href="cambiarIdioma.htm?setUserLocaleTo=en">
								<spring:message code="jsp.menu.ingles" />
							</a>
						</li>
					</c:if>
					<c:if test="${idioma=='ca'}">
						<li class="idiomas">
							<a class="idiomas" style="border-top: 1px solid #999;" href="cambiarIdioma.htm?setUserLocaleTo=es">
								<spring:message code="jsp.menu.espanhol" />
							</a>
						</li>
						<li class="idiomas">
							<a class="idiomas" href="cambiarIdioma.htm?setUserLocaleTo=gl">
								<spring:message code="jsp.menu.gallego" />
							</a>
						</li>
						<li class="idiomas">
							<a class="idioma_seleccionado" href="#" onclick="return false;">
								<spring:message code="jsp.menu.catalan" />
							</a>
						</li>
						<li class="idiomas">
							<a class="idiomas" href="cambiarIdioma.htm?setUserLocaleTo=eu">
								<spring:message code="jsp.menu.vasco" />
							</a>
						</li>
						<li class="idiomas">
							<a class="idiomas" href="cambiarIdioma.htm?setUserLocaleTo=en">
								<spring:message code="jsp.menu.ingles" />
							</a>
						</li>
					</c:if>
					<c:if test="${idioma=='eu'}">
						<li class="idiomas">
							<a class="idiomas" style="border-top: 1px solid #999;" href="cambiarIdioma.htm?setUserLocaleTo=es">
								<spring:message code="jsp.menu.espanhol" />
							</a>
						</li>
						<li class="idiomas">
							<a class="idiomas" href="cambiarIdioma.htm?setUserLocaleTo=gl">
								<spring:message code="jsp.menu.gallego" />
							</a>
						</li>
						<li class="idiomas">
							<a class="idiomas" href="cambiarIdioma.htm?setUserLocaleTo=ca">
								<spring:message code="jsp.menu.catalan" />
							</a>
						</li>
						<li class="idiomas">
							<a class="idioma_seleccionado" href="#" onclick="return false;">
								<spring:message code="jsp.menu.vasco" />
							</a>
						</li>
						<li class="idiomas">
							<a class="idiomas" href="cambiarIdioma.htm?setUserLocaleTo=en">
								<spring:message code="jsp.menu.ingles" />
							</a>
						</li>
					</c:if>
					<c:if test="${idioma=='en'}">
						<li class="idiomas">
							<a class="idiomas" style="border-top: 1px solid #999;" href="cambiarIdioma.htm?setUserLocaleTo=es">
								<spring:message code="jsp.menu.espanhol" />
							</a>
						</li>
						<li class="idiomas">
							<a class="idiomas" href="cambiarIdioma.htm?setUserLocaleTo=gl">
								<spring:message code="jsp.menu.gallego" />
							</a>
						</li>
						<li class="idiomas">
							<a class="idiomas" href="cambiarIdioma.htm?setUserLocaleTo=ca">
								<spring:message code="jsp.menu.catalan" />
							</a>
						</li>
						<li class="idiomas">
							<a class="idiomas" href="cambiarIdioma.htm?setUserLocaleTo=eu">
								<spring:message code="jsp.menu.vasco" />
							</a>
						</li>
						<li class="idiomas">
							<a class="idioma_seleccionado" href="#" onclick="return false;">
								<spring:message code="jsp.menu.ingles" />
							</a>
						</li>
					</c:if>
				</ul>
			</li>
			<li><a href="<c:url value="/j_spring_security_logout" />" ><spring:message code="jsp.menu.cerrarSesion"/></a></li>
		</ul>
	</li>			
</ul>
<script type="text/javascript">
var sobreIdiomas = false;
$(document).ready(function() {
	$('#idioma').mouseenter(function() {
		$('#idiomas').slideDown();
	});
	$('#idioma').mouseleave(function() {
		setTimeout("comprobar()",100);	
	});
	$('.idiomas').mouseenter(function(){
		sobreIdiomas = true;
	});
	$('.idiomas').mouseleave(function(){
		sobreIdiomas = false;
	});
});
function comprobar() {
	if ( !sobreIdiomas ) $('#idiomas').slideUp();
}
</script>