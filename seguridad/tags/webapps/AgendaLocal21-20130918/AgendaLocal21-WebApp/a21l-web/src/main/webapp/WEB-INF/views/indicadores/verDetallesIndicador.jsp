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
		    <h2 class="inf15"><spring:message code="jsp.indicador.indicador"/>: ${indicadorDto.nombre}</h2>
			
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
			
            <div class="fizq">			
			<form:form id="formulario"  modelAttribute="indicadorDto" action="?" method="post">
				<input type="hidden" name="id" value="${indicadorDto.id}" />
		
				<div class="linea">
					<label for="nombre" class="label fizq"><spring:message code="jsp.indicadores.indicador.nombreIndicador"/></label>
					<form:input readonly="true" type="text" value="" id="nombre" cssClass="input_med" path="nombre"/>
				</div>
				
				<div class="linea">
					<label for="descripcion" class="label fizq"><spring:message code="jsp.fuentes.descripcion"/></label>
					<form:textarea readonly="true" type="text" value="" id="descripcion" cssClass="input_med" path="descripcion"/>
				</div>
				<c:if test="${not ficheroMetadatos && ficheroMetadatos != '' }">
				<div class="linea">
					<div class="fder"><a href="indicadores.htm?accion=descargarMetadatos&id=${indicadorDto.id}"><spring:message code="jsp.indicador.descargar.metadatos"/></a></div>
				</div>
				<div class="clear"></div>
				</c:if>
				<div class="linea">
					<label for="metadatos" class="label fizq"><spring:message code="jsp.indicador.metadatos"/></label>
					<textarea wrap="off" readonly class="input_med texto_09" id="metadatos" name="metadatos" rows="20">${metadatos}</textarea>
				</div>
				<div class="clear"></div>
			</form:form>
			</div>
			<div class="fder ancho17">
				<ul class="operaciones">
					<li><a href="indicadores.htm?accion=visualizarIndicadorTabla&id=${indicadorDto.id}"><spring:message code="jsp.indicador.volver"/></a></li>
					<li><a href="#" onclick="return false;" id="exportar_indicador"><spring:message code="jsp.indicador.exportar"/></a></li>
					<%--<li><a href="#"><spring:message code="jsp.indicador.informe.pdf"/></a></li>--%>
					<c:if test="${!usuarioInvitado}">
						<li><a href="indicadores.htm?accion=modificarDetallesIndicador&id=${indicadorDto.id}"><spring:message code="jsp.indicador.modificar"/></a></li>
					</c:if>
				</ul>
			</div>
		</div>
		<div class="clear"></div>
	</div>
	
<!-- DIALOGO MODAL EXPORTAR -->
<div id="dialogoModalExportar" style="display: none;background-color: white;">
	<div class="ui-widget">
        <div style="padding: 0.2em 0.7em;" class=""> 
            <div class="inf15">
            	<spring:message code="jsp.exportar.indicador"/>
            </div>
            <div class="linea">
            	<label class="label fizq" for="tipo_exportacion"><spring:message code="jsp.exportar.tipo.exportacion"/></label>
            	<select name="tipo_exportacion" id="tipo_exportacion" >
					<option value="" selected="selected">&nbsp;</option>
					<option value="4"><spring:message code="jsp.exportar.csv"/></option>
					<option value="6"><spring:message code="jsp.exportar.bd.espacial"/></option>
					<option value="3"><spring:message code="jsp.exportar.gml"/></option>
					<option value="1"><spring:message code="jsp.exportar.shapefile"/></option>
                </select>
            </div>  	            
        </div>
    </div>
</div>

<script type="text/javascript">
$(document).ready(function() {
	$('#descripcion').focus();

	$('#exportar_indicador').click(function() {
		exportar.exportarIndicador();
	});
	
});

var exportar = {
		exportarIndicador : function () {
			var dialogoModalExportar = $("#dialogoModalExportar").dialog({
			    autoOpen: false,
			    show: "blind",
			    hide: "explode",                    
			    width: 475,
			    height: 200,
			    modal: true,
			    resizable: false,
			    title: "<spring:message code="jsp.indicador.exportar"/>",
			    buttons: {
			        "Aceptar": function() {
			        	var tipo = $('#tipo_exportacion').val();
			        	if ( tipo == "" ) {
			        		alert("Seleccione un tipo para la exportación");
			        		return false;
			        	}
			        	$(this).dialog( "close" );
			        	var url = "indicadores.htm?accion=exportar&tipoGrafico=1&tipo="+tipo+"&id=${indicadorDto.id}";
			        	document.location.href=url;
			            return false;            		           
			        },
			         "Cancelar": function() {
			            $(this).dialog( "close" );
			            return false;            
			        }
			    }            
			});
			$(dialogoModalExportar).dialog("open");	
		}	
	};
</script>