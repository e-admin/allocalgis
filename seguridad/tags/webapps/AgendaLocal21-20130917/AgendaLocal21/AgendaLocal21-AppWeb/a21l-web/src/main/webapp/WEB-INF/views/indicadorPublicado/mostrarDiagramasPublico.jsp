<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script src="js/util/comun/cambioMenus.js"></script>
<script src="js/util/indicadores/diagramaBarras.js"></script>
<script src="js/util/datePicker/jquery.ui.datepicker-es.js"></script>
<script src="js/dataTables/js/jquery.dataTables.min.js"></script>
<link type="text/css" rel="stylesheet" href="js/dataTables/css/jquery.dataTables.css" /> 

<!--[if IE]><script type="text/javascript" src="js/graph/excanvas.min.js"></script><![endif]-->

<!--  color picker -->
<script type="text/javascript" src="js/colorpicker/farbtastic.js"></script>
<link rel="stylesheet" href="js/colorpicker/farbtastic.css" type="text/css" />

<form id="formularioCamposDiagrama" action="indicadorPublico.htm" method="post">
	<input type="hidden" name="accion" value="visualizarDiagramaPublico"/>
	<input type="hidden" name="id" value="${indicadorDto.id}"/>
	<input type="hidden" name="tipo" value="${tipoDiagrama}"/>

	<c:set var="num" value="0"></c:set>
	<c:forEach var="contador" varStatus="status" begin="0" end="${numFilasDiagrama}" step="1">
		<c:forEach items="${datosAmbitoDiagrama}" var="valoresFila">
			<c:forEach items="${camposChkDiagrama}" var="elto" varStatus="cont">
				<c:if test="${(status.count)-1==elto}">
					<input type="hidden" id="campo_${(status.count)-1}" name="campo_${(status.count)-1}" value="${(status.count)-1}" />
					<c:set var="num" value="${num+1}"></c:set>
				</c:if>
			</c:forEach>
        </c:forEach>
    </c:forEach>
    <input type="hidden" id="numCamposDiagrama" name="numCamposDiagrama" value="${num}" />

<div class="cuerpoprincipal">
	<div class="areacentral">
	    <h2 class="inf15"><spring:message code="jsp.indicador.indicador"/>: ${indicadorDto.nombre}</h2>
	    <h2 class="inf15">
	    	<c:if test="${tipoDiagrama!=null && tipoDiagrama=='barras'}" ><spring:message code="jsp.indicador.diagrama.barras"/></c:if>
			<c:if test="${tipoDiagrama!=null && tipoDiagrama=='sectores'}" ><spring:message code="jsp.indicador.diagrama.sectores"/></c:if>
	    </h2>
		<c:if test="${passwordCambiadaExito != null}">
			<div class="controlAlertas">
				<div class="ui-widget">
					<div class="success ui-corner-all">
						<p>
							<span class="ui-icon ui-icon-circle-check fizq"></span>
							<spring:message code="${passwordCambiadaExito}" />
						</p>
					</div>
				</div>
			</div>
		</c:if>
		<c:if test="${errorSeleccionarCampo!=null && errorSeleccionarCampo!='' || errorSeleccionarParametro!=null && errorSeleccionarParametro!=''}">
          	<div class="controlAlertas">	
				<div class="ui-widget">
					<div class="ui-state-error ui-corner-all"> 
						<p><span class="ui-icon ui-icon-alert fizq"></span> 
						 	<c:if test="${errorSeleccionarCampo!=null && errorSeleccionarCampo!=''}">
				         		<spring:message code="${errorSeleccionarCampo}"/>
				         	</c:if>
				         	<c:if test="${errorSeleccionarParametro!=null && errorSeleccionarParametro!=''}">
				         		<spring:message code="${errorSeleccionarParametro}"/>
				         	</c:if>
				         </p>
					</div>
				</div>
			</div>
			<br/>
   		</c:if>
		<div class="fizq ancho80">
			<div class="linea">
				<label><spring:message code="jsp.indicador.parametro"/></label>
				<select name="parametro" id="param">
					<c:forEach items="${listaColumnas}" var="col">
	                	<option value="${col.nombre}" <c:if test="${col.nombre eq parametro}">selected="selected"</c:if>>${col.nombre}</option>
	                </c:forEach>
                </select>
				<a id="enlaceMostrarCampos" href="#" class="boton boton_grande izq10 fder"><spring:message code="jsp.indicador.evolucion.seleccionar"/></a>
			</div>
		</div>
		<div class="fder ancho17">
			<ul class="operaciones">
				<li><a href="indicadorPublico.htm?accion=visualizarIndicadorTabla&id=${indicadorDto.id}"><spring:message code="jsp.indicador.volver"/></a></li>				
			</ul>
		</div>
		<div class="clear"></div>
		<br>
		<div id="evolucion" style="height:300px; width:560px;"></div>
	</div>
	<div class="clear"></div>
</div>

<!-- Seleccionar campos evolucion -->
	<div id="dialogoModalCamposDiagrama" style="display: none; background-color: white;">
	    <div class="ui-widget">
	        <div style="padding: 0.2em 0.7em;" class="">
	            <div class="fizq">
	            	<div>
	            		<c:if test="${seleccionado}">
	            			<input type="checkbox" class="chk_todos" id="chk_todos" name="chk_todos" checked="checked"/>&nbsp;
	            			<label><spring:message code="jsp.indicador.seleccionar.todos"/></label>
	            		</c:if>
	            		<c:if test="${!seleccionado}">
	            			<input type="checkbox" class="chk_todos" id="chk_todos" name="chk_todos" />&nbsp;
	            			<label><spring:message code="jsp.indicador.seleccionar.todos"/></label>
            			</c:if>
	            	</div>
		           	<div class="dos_columnas">
		           		<c:set var="chk" value="false"></c:set>
		           		<c:forEach var="contador" varStatus="status" begin="0" end="${numFilasDiagrama-1}" step="1">
		           			<c:forEach items="${datosAmbitoDiagrama}" var="valoresFila">
		           				<c:forEach items="${camposChkDiagrama}" var="elto" varStatus="cont">
		           					<c:if test="${(status.count)-1==elto}">
		           						<c:set var="chk" value="true"></c:set> 
		           					</c:if>
		           				</c:forEach>
      							<input type="checkbox" class="chk_campos" id="${(status.count)-1}" name="campo_${(status.count)-1}" <c:if test="${chk==true}">checked</c:if> />&nbsp;
   								<label>${valoresFila.value.valores[(status.count)-1].texto}</label><br>
   								<c:set var="chk" value="false"></c:set> 
		           				<c:if test="${numFilasDiagrama%2 == 0}">
		           					<c:if test="${status.count == (numFilasDiagrama div 2 + numFilasDiagrama%2)}">
		           						</div>	<div class="dos_columnas">
									</c:if>
								</c:if>
								<c:if test="${numFilasDiagrama%2 == 1}">
		           					<c:if test="${status.count == ((numFilasDiagrama-1) div 2 + numFilasDiagrama%2)}">
		           						</div>	<div class="dos_columnas">
									</c:if>
								</c:if>
		           			</c:forEach>
		           		</c:forEach>
		           	</div>
	            </div>
	        </div>
	    </div>
	</div>
</form>

<script type="text/javascript">

$(document).ready(function() {
	
	$('#enlaceMostrarCampos').click(function() {
		camposEvolucion.mostrar();
	});
	
	$('.chk_campos').click(function(){
		var cont = parseInt($('#numCamposDiagrama').val());
		 if ($(this).is(":checked")){
			 cont++;
			 $("#formularioCamposDiagrama").append("<input type='hidden' id='campo_"+$(this).attr("id")+"' name='campo_"+$(this).attr("id")+"' value="+$(this).attr("id")+" />");
			 $('#numCamposDiagrama').val(cont);
		 }
		 else{
			 cont--;
			 $("#campo_"+$(this).attr("id")).remove();
			 $('#numCamposDiagrama').val(cont);
		 }
	});	
	
	$('.chk_todos').click(function(){
		$("input:hidden[name*='campo_']").remove();
		cont=0;
		$('#numCamposDiagrama').val(cont);
		if ($(this).is(":checked")){
			$("input:checkbox").each(function(){
				cont++;
				$("#formularioCamposDiagrama").append("<input type='hidden' id='campo_"+$(this).attr("id")+"' name='campo_"+$(this).attr("id")+"' value="+$(this).attr("id")+" />");
				$('#numCamposDiagrama').val(cont);
				$(this).prop('checked', true);
			});
		}else{
			$("input:checkbox").prop('checked', false);	
		}
	});
});
	
var camposEvolucion = {
		mostrar : function () {
			var dialogoModalCamposDiagrama = $("#dialogoModalCamposDiagrama").dialog({
			    autoOpen: false,
			    show: "blind",
			    hide: "explode",                    
			    width: 675,
			    height: 385,
			    modal: true,
			    resizable: false,
			    title: "<spring:message code='jsp.indicador.evolucion.seleccionar'/>",
			    buttons: {
			        "Aceptar": function() {
			        	$('#formularioCamposDiagrama').submit();
			        	$(this).dialog( "close" );
			            return false;            
			           
			        },
			         "Cancelar": function() {
			        	$(this).dialog( "close" );
			            return false;            
			        }
			    }            
			});
			
			$(dialogoModalCamposDiagrama).dialog("open");	
		}	
};
</script>