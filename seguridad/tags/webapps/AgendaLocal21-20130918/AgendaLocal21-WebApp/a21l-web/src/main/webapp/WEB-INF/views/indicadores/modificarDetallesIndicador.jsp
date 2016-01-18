<%@page contentType="text/html" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<script src="js/util/fuentes/fuentes.js"></script>
<script src="js/util/comun/cambioMenu.js"></script>
    <div class="cuerpoprincipal">
	    <div class="areamenu">
			<ul class="menusecundario">
				<li>
					<a href="indicadores.htm?accion=visualizarIndicadorTabla&id=${indicadorDto.id}">
						<spring:message code="jsp.indicador.tabla.valores"/>
					</a>
				</li>
				<li>
					<a href="indicadores.htm?accion=visualizarIndicadorDiagramaBarras&id=${indicadorDto.id}">
						<spring:message code="jsp.indicador.diagrama.barras"/>
					</a>
				</li>
				<li>
					<a href="indicadores.htm?accion=visualizarIndicadorDiagramaSectores&id=${indicadorDto.id}">
						<spring:message code="jsp.indicador.diagrama.sectores"/>
					</a>
				</li>
				<li>
					<a href="indicadores.htm?accion=visualizarIndicadorMapa&id=${indicadorDto.id}">
						<spring:message code="jsp.indicador.mapa.tematico"/>
					</a>
				</li>
				<li class="on">
					<a href="indicadores.htm?accion=verDetallesIndicador&id=${indicadorDto.id}">
						<spring:message code="jsp.indicador.ver.detalles"/>
					</a>
				</li>
			</ul>
		</div>
		<div class="areacentral">			
		    <h2 class="inf15">
		    	<spring:message code="jsp.indicador.indicador"/>:
		    	<c:if test="${!mostrarNombreOriginal}">
		    		 ${indicadorDto.nombre}
		    	</c:if>
		    	<c:if test="${mostrarNombreOriginal}">
		    		 ${indicadorOriginalDto.nombre}
		    	</c:if>		    	
		    </h2>
			
			<!--  mensajes de error -->
			<spring:hasBindErrors name="indicadorDto">
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
			
			<!--  mensajes de exito -->
			<c:if test="${resultado != null}">
            <div class="controlAlertas">	
				<div class="ui-widget">
                     <c:if test="${resultado == 'exitoGuardar'}">
                         <div class="success ui-corner-all"> 
							<p><span class="ui-icon ui-icon-circle-check fizq"></span>
                            <spring:message code="jsp.indicador.guardar.detalles" />
                            </p>
                         </div>
                     </c:if>
                     <c:if test="${resultado == 'fileError'}">
                         <div class="ui-state-error ui-corner-all"> 
							<p><span class="ui-icon ui-icon-circle-check fizq"></span>
                            <spring:message code="${fileError}" />
                            </p>
                         </div>
                     </c:if>
                 </div>
            </div>
            </c:if>
			
            <div class="fizq">			
			<form:form id="formulario"  modelAttribute="indicadorDto" action="indicadores.htm" method="post" enctype="multipart/form-data">
				<input type="hidden" name="id" value="${indicadorDto.id}" />
				<input type="hidden" name="idCategoria" value="${indicadorDto.idCategoria}"/>
				<input type="hidden" name="pteAprobacionPublico" value="${indicadorDto.pteAprobacionPublico}" />
				<input type="hidden" id="haiFicheroMetadatos" name="haiFicheroMetadatos" value="${ficheroMetadatos}" />
				<input type="hidden" name="accion" value="modificarDetallesIndicador" />
		
				<div class="linea">
					<label for="nombre" class="label fizq"><spring:message code="jsp.indicador.nombre"/></label>
					<form:input type="text" value="" id="nombre" cssClass="input_med" path="nombre" maxlength="255"/>
				</div>
				
				<div class="linea">
					<label for="descripcion" class="label fizq"><spring:message code="jsp.fuentes.descripcion"/></label>
					<form:textarea type="text" value="" id="descripcion" cssClass="input_med" path="descripcion"/>
				</div>
				<c:if test="${ficheroMetadatos!=null && ficheroMetadatos != '' }">
					<div class="linea">
						<label class="label fizq"><spring:message code="jsp.indicador.archivo.metadatos.actual"/></label>
						<a href="indicadores.htm?accion=descargarMetadatos&id=${indicadorDto.id}">${ficheroMetadatos}</a>
					</div>
				</c:if>
				<div class="linea">
					<label for="metadatos" class="label fizq" id="fichero_anterior" name="fichero_anterior"><spring:message code="jsp.indicador.archivo.metadatos.nuevo"/></label>
					<input type="file" value="" id="fich_metadatos" class="input_med" name="fich_metadatos"/>
				</div>
				<div class="linea">
					<label for="sin_metadatos" class="label fizq">&nbsp;</label>
					<input class="fizq" type="checkbox" id="sin_metadatos" name="sin_metadatos"<c:if test="${ficheroMetadatos==null || ficheroMetadatos == '' }">checked="checked"</c:if>/>
					<spring:message code="jsp.indicador.sin.archivo.metadatos"/>					
				</div>
				<div class="clear"></div>
			</form:form>
			</div>
		</div>
		<div class="clear"></div>
		<div class="botones_accion">
			<a href="indicadores.htm?accion=verDetallesIndicador&id=${indicadorDto.id}" class="boton izq10 sup10 fondo_blanco fder"><spring:message code="elementos.jsp.boton.cancelar"/></a>
			<a href="#" onclick="$('#formulario').submit();return false;" class="boton izq10 sup10 fondo_blanco fder"><spring:message code="elementos.jsp.boton.aceptar"/></a>			
		</div>
	</div>
<script type="text/javascript">
	$(document).ready(function() {
		$('#nombre').focus();
	});
	
	$(function(){
		 $("#formulario").submit(function() {
			 if (!$('input[name=sin_metadatos]').is(':checked') && $('#fich_metadatos').val().length == 0 && ($('#haiFicheroMetadatos').val()== null || $('#haiFicheroMetadatos').val() == ''))
				 {
					 alert('<spring:message code="jsp.indicadores.modificarDetallesIndicador.necesarioSeleccionarMetadatos"/>');	
					 return false;
				 }
			 else
				 {
				 	return true;
				 }
		 });
	});
</script>