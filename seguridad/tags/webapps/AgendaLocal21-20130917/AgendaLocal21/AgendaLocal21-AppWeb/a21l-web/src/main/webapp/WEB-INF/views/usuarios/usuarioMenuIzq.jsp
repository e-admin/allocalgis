<div class="areamenu">
	<ul class="menusecundario">
	<c:if test="${lateralUsuario}">
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
	</c:if>
	<c:if test="${!lateralUsuario}">
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
	</c:if>
	</ul>
</div>