<!-- ***** INFORME DE SECTORES *************************************************************** -->
<div id="anexo" >
		<c:forEach items="${datos_sectores}" var="datos_sector">
			<div class="anexo">
				<h1 align="center"><spring:message code="jsp.indicador.informe.anexo"/>&nbsp;${datos_sector.parametro}</h1>
				<br/>
				<div id="anexo_${datos_sector.parametro}" style="padding-left:10%">
				</div>
				<div class="saltopagina"></div>
			</div>
		</c:forEach>
</div>