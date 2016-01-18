<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<script src="js/util/comun/cambioMenus.js"></script>
<script src="js/util/datePicker/jquery.ui.datepicker-es.js"></script>
<script src="js/dataTables/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="js/graph/jquery.jqplot.min.js"></script>
<script type="text/javascript" src="js/graph/jqplot.pieRenderer.min.js"></script>
<script type="text/javascript" src="js/colorpicker/farbtastic.js"></script>
<!--[if IE]><script type="text/javascript" src="js/graph/excanvas.min.js"></script><![endif]-->
<link rel="stylesheet" type="text/css" href="js/graph/jquery.jqplot.min.css" />
<link type="text/css" rel="stylesheet" href="js/dataTables/css/jquery.dataTables.css" /> 
<link rel="stylesheet" href="js/colorpicker/farbtastic.css" type="text/css" />

<div class="cuerpoprincipal">
	<div class="areamenu">
		<ul class="menusecundario">
			<li>
				<a href="indicadorPublico.htm?accion=visualizarIndicadorTabla&id=${idIndicador}">
					<spring:message code="jsp.indicador.tabla.valores"/>
				</a>
			</li>
			<li>
				<a href="indicadorPublico.htm?accion=visualizarIndicadorDiagramaBarras&id=${idIndicador}">
					<spring:message code="jsp.indicador.diagrama.barras"/>
				</a>
			</li>
			<li class="on">
				<a href="indicadorPublico.htm?accion=visualizarIndicadorDiagramaSectores&id=${idIndicador}">
					<spring:message code="jsp.indicador.diagrama.sectores"/>
				</a>
			</li>
			<li>
				<a href="indicadorPublico.htm?accion=visualizarIndicadorMapa&id=${idIndicador}">
					<spring:message code="jsp.indicador.mapa.tematico"/>
				</a>
			</li>
		</ul>
	</div>
	<div class="areacentral">
	    <h2 class="inf15"><spring:message code="jsp.indicador.indicador"/>: ${indicadorDto.nombre}</h2>
	    <c:if test="${errorEstiloVisualizacion!=null && errorEstiloVisualizacion!=''}">
          	<div class="controlAlertas">	
				<div class="ui-widget">
					<div class="ui-state-error ui-corner-all"> 
						<p><span class="ui-icon ui-icon-alert fizq"></span> 
						<strong><spring:message code="global.formulario.erros.titulo"/></strong> 
						</p>
						<ul>
					         <li><spring:message code="${errorEstiloVisualizacion}"/></li>
						</ul>
					</div>
				</div>
			</div>
			<br/>
   		</c:if>
   		<c:if test="${exitoCrear!=null && exitoCrear!=''}">
          	<div class="controlAlertas">	
				<div class="ui-widget">
					<div class="success ui-corner-all"> 
						<p>
							<span class="ui-icon ui-icon-circle-check fizq"></span>
				          	<spring:message code="${exitoCrear}"/>
			         	</p>
			  	 	</div>
				</div>
			</div>
			<br/>
   		</c:if>
   		
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
   		
		<div class="fizq ancho80">
			<!-- DATOS -->
			<div class="linea">
				<label><spring:message code="jsp.indicador.parametro"/></label>
				<select name="parametros" id="parametros" onchange="cambiarParametro(${historico.id});" >
					<c:forEach items="${listaColumnas}" var="col">
	                	<option value="${col.nombre}" <c:if test="${col.nombre eq parametro}">selected="selected"</c:if>>${col.nombre}</option>
	                </c:forEach>
                </select>
			</div>
			<div id="div_diagramas_sectores" style="width: 545px; height: 550px;">
				<c:if test="${estilo!=null}">
				<table id="tabla_diagramas_sectores" cellspacing="0" cellpadding="0" style="width:545px;">
				</c:if>
				<c:if test="${estilo==null}">
				<table id="tabla_diagramas_sectores" style="width:545px" cellspacing="0" cellpadding="0">
				</c:if>
					<thead>
						<tr>
							<th><spring:message code="jsp.indicador.diagrama.sectores"/></th>
						</tr>
					</thead>
					<tbody>
						<c:if test="${estilo!=null}">
							<tr><td><div id="td_diagramas_sectores" style="width: ${estilo.diametro}px; height: ${estilo.diametro}px;font-family:${estilo.tipoFuente} !important;font-size:${estilo.tamanhoFuente}px !important;text-align:center;"></div></td></tr>
						</c:if>
						<c:if test="${estilo==null}">
							<tr><td><div id="td_diagramas_sectores" style="width: 550px; height: 500px;text-align:center;"></div></td></tr>
						</c:if>
					</tbody>
				</table>
			</div>
						<!-- HISTORICOS -->
			<div class="clear"></div>
			<br>
			<div><spring:message code="jsp.indicador.version.seleccion"/></div>
				<br/>
				<select name="listaHistoricos" id="listaHistoricos" >
					<c:forEach items="${historicos}" var="historicoDto" varStatus="orden">
						<c:if test="${historicoDto.id==historico.id}">
				        	<option value="${historicoDto.id}" selected="selected">
				        		<c:if test="${historicoDto.fecha != null}">
									<span id="fechaVersion_${historicoDto.id}">${historicoDto.strFecha}</span>
								</c:if>
								<c:if test="${historicoDto.fecha == null}">
									<span id="version_Actual"><spring:message code="elementos.jsp.boton.version.actual"/></span>
								</c:if>
			        		</option>
			        	</c:if>
			        	<c:if test="${historicoDto.id!=historico.id}">
			        		<option value="${historicoDto.id}">
			        			<c:if test="${historicoDto.fecha != null}">
									<span id="fechaVersion_${historicoDto.id}">${historicoDto.strFecha}</span>
								</c:if>
								<c:if test="${historicoDto.fecha == null}">
									<span id="version_Actual"><spring:message code="elementos.jsp.boton.version.actual"/></span>
								</c:if>
		        			</option>
			        	</c:if>
		        	</c:forEach>
			    </select>
				<div class="botones_accion">
					<a href="indicadorPublico.htm?accion=mostrarOpcionesEvolucion&id=${indicadorDto.id}" class="boton izq10 sup10 fder"><spring:message code="elementos.jsp.boton.version.mostrar"/></a>
					<c:if test="${propietario}">
						<a id="enlaceBorrar_version" href="#" class="boton izq10 sup10 fder"><spring:message code="elementos.jsp.boton.version.borrar"/></a>
						<a id="enlaceGuardar_version" href="#" class="boton izq10 sup10 fder"><spring:message code="elementos.jsp.boton.version.guardar"/></a>
					</c:if>
				</div>
		</div>
		<div class="fder ancho17">
			<ul class="operaciones">
				<li><a href="#" id="exportar_indicador"><spring:message code="jsp.indicador.exportar"/></a></li>
				<li><a href="indicadorPublico.htm?accion=exportarPDF&id=${indicadorDto.id}&tipo=tabla"><spring:message code="jsp.indicador.informe.pdf"/></a></li>
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
	$('#modificar_estilos').click(function() {
		estilos.modificarEstilos();
	});
	
    var select = $( "#listaHistoricos" );
	var slider = $( "<div id='slider'></div>" ).insertAfter( select ).slider({     	min: 1,
        max: ${fn:length(historicos)},
    	range: "min",
        value: select[ 0 ].selectedIndex + 1,
        slide: function( event, ui ) {
            select[ 0 ].selectedIndex = ui.value - 1;
            visualizarVersion();
        }
    });
	var ticks = $('<div id="ticks-slider"></div>').insertAfter(slider);
	var numeroPasos = ${fn:length(historicos)};
	var paso = 100 / (numeroPasos - 1);
	for (var i = 0; i < numeroPasos - 1; i++) {
		ticks.append('<div class="tick-slider" style="width:' + paso + '%"></div>');
	}
    $( "#listaHistoricos" ).change(function() {
        slider.slider( "value", this.selectedIndex + 1 );
        visualizarVersion();
    });
	
	
	$('#exportar_indicador').click(function() {
		exportar.exportarIndicador();
	});	                 
<c:if test="${estilo==null}">
var opciones = {
	seriesDefaults: {
		renderer: jQuery.jqplot.PieRenderer,
		rendererOptions: {
			showDataLabels: true
		}
	},
};
</c:if>
<c:if test="${estilo!=null}">
var colores = "${estilo.colores}";
var listaColores = colores.split("||");
var listaFinalColores = [];
for ( var i = 0 ; i < listaColores.length ; i++ ) {
	if ( listaColores[i]!="") {
		listaFinalColores.push(listaColores[i]);
	}
}

var opciones = {
	seriesColors: listaFinalColores,
	legend: { show:true, location: 'ne',placement:'outside'},
	seriesDefaults: {
		renderer: jQuery.jqplot.PieRenderer,
		rendererOptions: {
			showDataLabels: true
		}
	}
};
</c:if>
		
var data = [
	<c:forEach var="paginas" varStatus="pagina" begin="0" end="${numPaginasDiagramas}" step="1">
		<c:forEach var="contador" varStatus="status" begin="${pagina.count*elementosPagina}" end="${pagina.count*elementosPagina+elementosPagina-1}" step="1">
			<c:forEach items="${datos}" var="valoresFila" varStatus="cont">
				<c:if test="${not empty valoresFila.value.valores[(status.count)-1+((pagina.count-1)*elementosPagina)].texto && cont.count%2!=0}">
					<c:if test="${cont.count%2!=0}">
						["${fn:trim(valoresFila.value.valores[(status.count)-1+((pagina.count-1)*elementosPagina)].texto)}",
					</c:if>
				</c:if>
				<c:if test="${not empty valoresFila.value.valores[(status.count)-1+((pagina.count-1)*elementosPagina)].texto && cont.count%2==0}">
					<c:if test="${cont.count%2==0}">				
						<c:if test="${empty valoresFila.value.valores[(status.count)-1+((pagina.count-1)*elementosPagina)].texto}">
							0],
						</c:if>
						<c:if test="${not empty valoresFila.value.valores[(status.count)-1+((pagina.count-1)*elementosPagina)].texto}">
							${valoresFila.value.valores[(status.count)-1+((pagina.count-1)*elementosPagina)].texto}],
						</c:if>
					</c:if>
				</c:if>
			</c:forEach>
		</c:forEach>
	</c:forEach>
];

	var plot = $.jqplot('td_diagramas_sectores', [data], opciones);
	var legendTable = $($('.jqplot-table-legend')[0]);    
	legendTable.css('display','block');
	legendTable.css('z-index',100);
	legendTable.css('height','80%');
	legendTable.css('width','150px');
	legendTable.css('overflow-y','scroll');
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

	function visualizarVersion() {
		var idHistorico = $('#listaHistoricos').val();
		window.location="${urlBase}indicadorPublico.htm?accion=visualizarVersion&id="+idHistorico+"&idInd="+${indicadorDto.id}+"&tipoGrafico=3";
	}
	
	function cambiarParametro(idHistorico) {
		document.location.href="indicadorPublico.htm?accion=visualizarIndicadorDiagramaSectores&id=${idIndicador}&param="+$('#parametros').val()+"&idHistorico="+idHistorico;
	}
</script>