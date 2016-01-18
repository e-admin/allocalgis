<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script src="js/util/comun/cambioMenus.js"></script>
 
 	<div class="cuerpoprincipal">
 		<div class="areacompleta">
 			<h2 class="inf15"><spring:message code="jsp.indicadores.indicadoresBusquedaAvanzada.titulo"/></h2>
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
 			<form:form action="?" modelAttribute="indiBusqAvanzadaDto" id="formulario">
 				<input type="hidden" name="accion" value="realizaBusquedaAvanzada"/>
 				
 				<div class="contenedorarbol ancho80" onKeyPress="return buscar(event)">
 					<div class="linea">
 						<label class="label fizq"><spring:message code="jsp.indicadores.indicadoresBusquedaAvanzada.nombreIndicador"/></label>
 						<form:input path="nombre" size="80"/>
 					</div>
 					
 					<div class="linea">
 						<label class="label fizq"><spring:message code="jsp.indicadores.indicadoresBusquedaAvanzada.textoDescripcion"/></label>
 						<form:input path="descripcion" size="80"/>
 					</div>
 					
 					<div class="linea">
 						<label class="label fizq"><spring:message code="jsp.indicadores.indicadoresBusquedaAvanzada.categoriaContenedora"/></label>
 						<form:input path="categoriaContenedora" size="80"/>
 					</div>
 					
 					<c:if test="${usuarioAdministrador}">
	 					<div class="linea">
	 						<label class="label fizq"><spring:message code="jsp.indicadores.indicadoresBusquedaAvanzada.usuarioCreador"/></label>
	 						<form:input path="usuarioCreador" size="80"/>
	 					</div>
 					</c:if>
 					<div class="clear"></div>
 					
 					<div class="linea">
 						<label class="label fizq"><spring:message code="jsp.indicadores.indicadoresBusquedaAvanzada.anhadirFiltroMetadatos"/></label>
 						<form:select id="selectFiltro" path="" items="${listaAtributosNEM}" itemLabel="expresion" itemValue="id" cssStyle="width:55%;" />
 						<a class="boton fder" id="btnAnhadir" href=""><spring:message code="jsp.indicadores.indicadoresBusquedaAvanzada.anhadir"/></a>
 					</div>	
 				</div>
 				
 				<div class="operacionesarbol ancho17">
 					<ul class="operaciones">
 						<li><a href="indicadores.htm"><spring:message code="jsp.indicadores.indicadoresBusquedaAvanzada.volver"/></a></li>
 						<li><a href="" id="idBtnBuscar"><spring:message code="jsp.indicadores.indicadoresBusquedaAvanzada.buscar"/></a></li>
 					</ul>
 				</div>
 				
 				<div class="clear"></div>
 				
 				<h3 class="negrita sin_borde inf15"><spring:message code="jsp.indicadores.indicadoresBusquedaAvanzada.filtrosMetadatosAnhadidos"/></h3>
 				<div id="idListaFilrosMD" class="fizq">
 						<!--  implementar dinamicamente con js -->
 				</div>
 				
				<div  class="fder">
					<ul id="idListaBtnQuitar" class="operaciones">
						<!--  implementar dinamicamente con js -->
					</ul>
 				</div>
 				
 				<div class="clear"></div>
 				
 			</form:form>
 		</div>
 		<div class="clear"></div>
 	</div>
 	
<script type="text/javascript">
	$(document).ready(function() {
		$('#nombre').focus();
	});

	var numeroFiltros=0;
	var btnQuitar="<spring:message code="jsp.indicadores.indicadoresBusquedaAvanzada.btnQuitar"/>";
		
	$("#btnAnhadir").click(function() {
		numeroFiltros++;
		var filtroSelect= $("#selectFiltro option:selected");
		
		$("#idListaFilrosMD").append("<div class='linea' id='idFiltroMD"+numeroFiltros+"'><label class='label_grande fizq'>"+filtroSelect.text()+"</label><input class='input_pequeno' id='idValorFiltroMD"+numeroFiltros+"' type='text' value='...' name='valoresFiltroMD' onblur='evaluarValorVacio(\""+numeroFiltros+"\");return false' ><input type='hidden' name='idsFiltroMD' value='"+filtroSelect.val()+"'/></div>");
		$("#idListaBtnQuitar").append("<li id='idLineaBton"+numeroFiltros+"'><a href='#' onclick='borraFiltro(\""+numeroFiltros+"\");return false' class='btnQuitarFiltro' id='"+numeroFiltros+"' style='position: relative;top: 5px;'>"+btnQuitar+"</a></li>");
		
		return false;
	});
	
	function borraFiltro(idFiltro){
		$("#idFiltroMD"+idFiltro).remove();
		$("#idLineaBton"+idFiltro).remove();	
	}
	
	$("#idBtnBuscar").click(function(){
		$("#formulario").submit();
		return false;
	});
	
	
	function evaluarValorVacio(idFiltro){
		if(! $("#idValorFiltroMD"+idFiltro).val() ) {   
			$("#idValorFiltroMD"+idFiltro).val("...");
	    }
	}
	
	function buscar(e)
	{
		if (e && e.keyCode == 13){
			$("#formulario").submit();
				return false;
		}
	}
</script>
