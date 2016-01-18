<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script src="js/util/comun/cambioMenus.js"></script>
<script src="js/util/indicadores/diagramaBarras.js"></script>
<script src="js/util/datePicker/jquery.ui.datepicker-es.js"></script>
<script src="js/dataTables/js/jquery.dataTables.min.js"></script>

<script type="text/javascript" src="js/graph/jquery.jqplot.min.js"></script>
<script type="text/javascript" src="js/graph/jqplot.barRenderer.min.js"></script>
<script type="text/javascript" src="js/graph/jqplot.categoryAxisRenderer.min.js"></script>
<script type="text/javascript" src="js/graph/jqplot.pointLabels.min.js"></script>
<link rel="stylesheet" type="text/css" href="js/graph/jquery.jqplot.min.css" />
<link type="text/css" rel="stylesheet" href="js/dataTables/css/jquery.dataTables.css" /> 

<script type="text/javascript" src="js/graph/jqplot.highlighter.min.js"></script>
<script type="text/javascript" src="js/graph/jqplot.dateAxisRenderer.min.js"></script>

<!--[if IE]><script type="text/javascript" src="js/graph/excanvas.min.js"></script><![endif]-->

<!--  color picker -->
<script type="text/javascript" src="js/colorpicker/farbtastic.js"></script>
<link rel="stylesheet" href="js/colorpicker/farbtastic.css" type="text/css" />

<form id="formularioCamposEvolucion" action="indicadorPublico.htm" method="post">
	<input type="hidden" name="accion" value="visualizarEvolucion"/>
	<input type="hidden" name="id" value="${indicadorDto.id}"/>
	
	<c:set var="num" value="0"></c:set>
	<c:forEach var="contador" varStatus="status" begin="0" end="${numFilas}" step="1">
		<c:forEach items="${datosAmbito}" var="valoresFila">
			<c:forEach items="${camposChk}" var="elto" varStatus="cont">
				<c:if test="${(status.count)-1==elto}">
					<input type="hidden" id="campo_${(status.count)-1}" name="campo_${(status.count)-1}" value="${(status.count)-1}" />
					<c:set var="num" value="${num+1}"></c:set>
				</c:if>
			</c:forEach>
        </c:forEach>
    </c:forEach>
    <input type="hidden" id="numCampos" name="numCampos" value="${num}" />
    
	
<div class="cuerpoprincipal">
	<div class="areamenu">
		<ul class="menusecundario">
			<li>
				<a href="indicadorPublico.htm?accion=visualizarIndicadorTabla&id=${indicadorDto.id}">
					<spring:message code="jsp.indicador.tabla.valores"/>
				</a>
			</li>
			<li>
				<a href="indicadorPublico.htm?accion=visualizarIndicadorDiagramaBarras&id=${indicadorDto.id}">
					<spring:message code="jsp.indicador.diagrama.barras"/>
				</a>
			</li>
			<li>
				<a href="indicadorPublico.htm?accion=visualizarIndicadorDiagramaSectores&id=${indicadorDto.id}">
					<spring:message code="jsp.indicador.diagrama.sectores"/>
				</a>
			</li>
			<li>
				<a href="indicadorPublico.htm?accion=visualizarIndicadorMapa&id=${indicadorDto.id}">
					<spring:message code="jsp.indicador.mapa.tematico"/>
				</a>
			</li>
		</ul>
	</div>
	<div class="areacentral">
	    <h2 class="inf15"><spring:message code="jsp.indicador.indicador"/>: ${indicadorDto.nombre}</h2>
	    <h2 class="inf15"><spring:message code="jsp.indicador.evolucion"/></h2>
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
				<li><a href="#" id="exportar_indicador"><spring:message code="jsp.indicador.exportar"/></a></li>
				<li><a href="indicadorPublico.htm?accion=exportarPDF&id=${indicadorDto.id}&tipo=tabla"><spring:message code="jsp.indicador.informe.pdf"/></a></li>
			</ul>
		</div>
		<div class="clear"></div>
		<br>
		<div id="evolucion" style="height:300px; width:560px;"></div>
	</div>
	<div class="clear"></div>
</div>

<!-- Seleccionar campos evolucion -->
	<div id="dialogoModalCamposEvolucion" style="display: none; background-color: white;">
	    <div class="ui-widget">
	        <div style="padding: 0.2em 0.7em;" class="">
	            <div class="fizq">
		           	<div class="dos_columnas">
		           		<c:set var="chk" value="false"></c:set>
		           		<c:forEach var="contador" varStatus="status" begin="0" end="${numFilas}" step="1">
		           			<c:forEach items="${datosAmbito}" var="valoresFila">
		           				<c:forEach items="${camposChk}" var="elto" varStatus="cont">
		           					<c:if test="${(status.count)-1==elto}">
		           						<c:set var="chk" value="true"></c:set> 
		           					</c:if>
		           				</c:forEach>
      								<input type="checkbox" class="chk_campos" id="${(status.count)-1}" name="campo_${(status.count)-1}" <c:if test="${chk==true}">checked</c:if> />&nbsp;
   									<label>${valoresFila.value.valores[(status.count)-1].texto}</label><br>
   								<c:set var="chk" value="false"></c:set> 
		           				<c:if test="${numFilas%2 == 0}">
		           					<c:if test="${status.count == (numFilas div 2 + numFilas%2)}">
		           						</div>	<div class="dos_columnas">
									</c:if>
								</c:if>
								<c:if test="${numFilas%2 == 1}">
		           					<c:if test="${status.count == ((numFilas-1) div 2 + numFilas%2)}">
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
	$('#param').change(function() {
		$('#formularioCamposEvolucion').submit();
        return false;
	}); 
	
	$('#enlaceMostrarCampos').click(function() {
		camposEvolucion.mostrar();
	});
	
	$('#enlaceVerEvolucion').click(function() {
		$('#formularioCamposEvolucion').submit();
        return false;            
	});
	
	$('.chk_campos').click(function(){
		var cont = parseInt($('#numCampos').val());
		 if ($(this).is(":checked")){
			 cont++;
			 $("#formularioCamposEvolucion").append("<input type='hidden' id='campo_"+$(this).attr("id")+"' name='campo_"+$(this).attr("id")+"' value="+$(this).attr("id")+" />");
			 $('#numCampos').val(cont);
		 }
		 else{
			 cont--;
			 $("#campo_"+$(this).attr("id")).remove();
			 $('#numCampos').val(cont);
		 }
	});
	
	$('#exportar_indicador').click(function() {
		exportar.exportarIndicador();
	});
	
  <c:forEach items="${datos}" var="fila" varStatus="cont" >
  	var line${cont.count} = ${fila};
  </c:forEach>

  /* var line1=[['04/sep/2012', 1325.0],['05/sep/2012', 1335.0],['06/sep/2012', 1345.0]];
  var line2=[['04/sep/2012', 1147.0],['05/sep/2012', 1247.0],['06/sep/2012', 1347.0]]; 
  var plot1 = $.jqplot('evolucion', [line1,line2], { */
	  
 var plot1 = $.jqplot('evolucion', [
			<c:forEach items="${datos}" var="fila" varStatus="cont" >
				line${cont.count},
			</c:forEach>
                                ], {
      axes:{
        xaxis:{
          renderer:$.jqplot.DateAxisRenderer,
          /* <c:if test="${maxDia != null && not empty maxDia}">
          	max: ${maxDia},
          </c:if> */
          tickOptions:{
            formatString:'%Y &nbsp; %b&nbsp;%#d'
          }
        },
        yaxis:{
          tickOptions:{
            formatString:'%.2f'
            }
        }
      },
      highlighter: {
        show: true,
        sizeAdjust: 7.5
      },
      cursor: {
        show: false
      },
      legend: {show: true } , 
      //series:[{label:'Muxía'}, {label:'Camariñas'}]  
      series:[${leyenda}]
  });
});
	
var camposEvolucion = {
		mostrar : function () {
			var dialogoModalCamposEvolucion = $("#dialogoModalCamposEvolucion").dialog({
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
			        	$('#formularioCamposEvolucion').submit();
			        	$(this).dialog( "close" );
			            return false;            
			           
			        },
			         "Cancelar": function() {
			        	$(this).dialog( "close" );
			            return false;            
			        }
			    }            
			});
			
			$(dialogoModalCamposEvolucion).dialog("open");	
		}	
};


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
			    title: "<spring:message code='jsp.indicador.exportar'/>",
			    buttons: {
			    	"<spring:message code="elementos.jsp.boton.aceptar"/>": function() {
			        	var tipo = $('#tipo_exportacion').val();
			        	if ( tipo == "" ) {
			        		alert("<spring:message code="jsp.exportacion.seleccione.tipo"/>");
			        		return false;
			        	}
			        	$(this).dialog( "close" );
			        	var url = "indicadorPublico.htm?accion=exportar&tipoGrafico=1&tipo="+tipo+"&id=${indicadorDto.id}";
			        	document.location.href=url;
			            return false;            		           
			        },
			        "<spring:message code="elementos.jsp.boton.cancelar"/>": function() {
			            $(this).dialog( "close" );
			            return false;            
			        }
			    }            
			});
			$(dialogoModalExportar).dialog("open");	
		}	
	};
	
</script>