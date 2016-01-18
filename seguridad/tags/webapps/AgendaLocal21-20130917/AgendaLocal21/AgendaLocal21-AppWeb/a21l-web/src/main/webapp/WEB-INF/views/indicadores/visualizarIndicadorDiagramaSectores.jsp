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

<form id="formularioCamposAmbitos" action="indicadores.htm" method="post">

	<input type="hidden" name="accion" value="visualizarDiagrama"/>
	<input type="hidden" name="id" value="${indicadorDto.id}"/>
	<input type="hidden" name="tipo" value="sectores"/>
	<input type="hidden" name="idHistorico" value="${historico.id}"/>
	<input type="hidden" name="tipoGrafico" value="3"/>
	<input type="hidden" name="param" value="${parametro}"/>
	
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
		<div class="areamenu">
			<ul class="menusecundario">
				<li>
					<a href="indicadores.htm?accion=visualizarIndicadorTabla&id=${indicadorDto.id}">
						<spring:message code="jsp.indicador.tabla.valores"/>
					</a>
				</li>
				<li>
					<a href="indicadores.htm?accion=mostrarOpcionesDiagramas&id=${indicadorDto.id}&tipo=barras">
						<spring:message code="jsp.indicador.diagrama.barras"/>
					</a>
				</li>
				<li class="on">
					<a href="indicadores.htm?accion=mostrarOpcionesDiagramas&id=${indicadorDto.id}&tipo=sectores">
						<spring:message code="jsp.indicador.diagrama.sectores"/>
					</a>
				</li>
				<li>
					<a href="indicadores.htm?accion=visualizarIndicadorMapa&id=${indicadorDto.id}">
						<spring:message code="jsp.indicador.mapa.tematico"/>
					</a>
				</li>
				<li>
					<a href="indicadores.htm?accion=verDetallesIndicador&id=${indicadorDto.id}">
						<spring:message code="jsp.indicador.ver.detalles"/>
					</a>
				</li>
			</ul>
		</div>
		<div class="areacentral">
		    <h2 class="inf15"><spring:message code="jsp.indicador.indicador"/>: ${indicadorDto.nombre}</h2>
		    <c:if test="${errorEstiloVisualizacion!=null && errorEstiloVisualizacion!=''|| errorGuardarVersion!=null && errorGuardarVersion!='' }">
	          	<div class="controlAlertas">	
					<div class="ui-widget">
						<div class="ui-state-error ui-corner-all"> 
							<p><span class="ui-icon ui-icon-alert fizq"></span> 
							<strong><spring:message code="global.formulario.erros.titulo"/></strong> 
							</p>
							<ul>
								<c:if test="${errorEstiloVisualizacion!=null && errorEstiloVisualizacion!=''}">
						         	<li><spring:message code="${errorEstiloVisualizacion}"/></li>
						        </c:if>
						        <c:if test="${errorGuardarVersion!=null && errorGuardarVersion!=''}">
						         	<li><spring:message code="${errorGuardarVersion}"/></li>
						        </c:if>
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
					<select name="parametro" id="parametro">
<%-- 					onchange="cambiarParametro(${historico.id});"> --%>
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
						<a href="indicadores.htm?accion=mostrarOpcionesEvolucion&id=${indicadorDto.id}" class="boton izq10 sup10 fder"><spring:message code="elementos.jsp.boton.version.mostrar"/></a>
						<c:if test="${propietario}">
							<a id="enlaceBorrar_version" href="#" onclick="borrarhistorico(${historico.id})" class="boton izq10 sup10 fder"><spring:message code="elementos.jsp.boton.version.borrar"/></a>
							<a id="enlaceGuardar_version" href="#" class="boton izq10 sup10 fder"><spring:message code="elementos.jsp.boton.version.guardar"/></a>
						</c:if>
					</div>
				
			</div>
			<div class="fder ancho17">
				<ul class="operaciones">
					<li><a href="indicadores.htm"><spring:message code="jsp.indicador.volver"/></a></li>
					<li><a href="#" id="exportar_indicador"><spring:message code="jsp.indicador.exportar"/></a></li>
					<li><a href="indicadores.htm?accion=exportarPDF&id=${indicadorDto.id}&idHistorico=${historico.id}&tipo=tabla&desde=1"><spring:message code="jsp.indicador.informe.pdf"/></a></li>
					<c:if test="${!usuarioInvitado && (usuarioAdministrador || tienePermisos)}">
						<li><a href="indicadores.htm?accion=editaIndicador&id=${indicadorDto.id}"><spring:message code="jsp.indicadores.indicador.modificarIndicador"/></a></li>
						<li><a id="modificar_estilos" href="#"><spring:message code="jsp.indicador.modificar.estilos"/></a></li>
					</c:if>
					<li><a id="enlaceMostrarCampos" href="#"><spring:message code="jsp.indicador.evolucion.seleccionar"/></a></li>
				</ul>
			</div>
		</div>
		<div class="clear"></div>
	</div>
<!-- Seleccionar campos diagramas -->
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
<!-- añadir / modifcar estilos -->
<div id="dialogoModalModificarEstilos" style="display: none;background-color: white;">
    <div class="ui-widget">
        <div style="padding: 0.2em 0.7em;" class=""> 
            <form id="formularioEstiloVisualizacion" action="indicadores.htm" method="post">
            	<input type="hidden" name="accion" value="nuevoEstiloVisualizacionDiagramaSectores"/>
            	<input type="hidden" name="id" value="${indicadorDto.id}"/>
            <div class="inf15">
            	<spring:message code="jsp.visualizacion.sectores"/>
            </div>
            <div class="fizq">
	            <div class="linea">
	            	<label class="label fizq" for="tamanho_fuente"><spring:message code="jsp.visualizacion.tamanho.fuente"/></label>
	            	<select name="tamanho_fuente" id="tamanho_fuente" >
						<option value="">&nbsp;</option>
						<c:forEach items="${tamanhosFuente}" var="tam" varStatus="cont">
                      	<option value="${tam}">${valoresTamanhosFuente[cont.count-1]}</option>
                       </c:forEach>
                    </select>
	            </div>
	            <div class="linea">
	            	<label class="label fizq" for="tipo_letra"><spring:message code="jsp.visualizacion.tipo.letra"/></label>
	            	<select name="tipo_letra" id="tipo_letra" >
						<option value="" selected="selected">&nbsp;</option>
						<c:forEach items="${tiposLetra}" var="tipo">
                      	<option value="${tipo.descripcion}">${tipo.descripcion}</option>
                       </c:forEach>
                    </select>
	            </div>
                <div class="linea">
	            	<label class="label fizq" for="tamanho"><spring:message code="jsp.visualizacion.diametro"/></label>
	            	<select name="diametro" id="diametro" >
						<option value="" selected="selected">&nbsp;</option>
						<c:forEach items="${diametros}" var="diametro">
                      	<option value="${diametro}">${diametro}</option>
                       </c:forEach>
                    </select>
	            </div>
	            <div class="linea">
	            	<label class="label fizq" for="color"><spring:message code="jsp.visualizacion.color"/></label>
	            	<input type="text" id="color" name="color" value="#FFF"/>&nbsp;<a href="#" id="anhadir_color"><spring:message code="jsp.visualizacion.anhadir.color"/></a>
	            	<input type="hidden" id="colores" name="colores" value=""/>
 					<div id="colorpicker"></div>
	            </div>
	            <div class="linea" style="width:100%;">
	            	<label class="label"><spring:message code="jsp.visualizacion.colores"/></label>
 					<div id="lista_colores"></div>
	            </div>	            	            
            </div> 
        	</form>          
        </div>
    </div>
</div>

<!-- Guardar / modificar version -->
<div id="dialogoModalGuardarVersion" style="display: none; background-color: white;">
    <div class="ui-widget">
        <div style="padding: 0.2em 0.7em;" class=""> 
            <form id="formularioGuardarVersion" action="indicadores.htm" method="post">
            	<input type="hidden" name="accion" value="guardarVersion"/>
            	<input type="hidden" name="id" value="${indicadorDto.id}"/>
            	<input type="hidden" id="historicoId" name="historicoId" value=""/>
            	<input type="hidden" name="tipoGrafico" value="3" />
            <div class="fizq">
	            <div class="linea">
	            	<label class="label fizq" for="historico_fecha"><spring:message code="jsp.indicador.version.fecha"/></label>
	            </div>
	            <div class="linea">
	            	<input type="text" id="historico_fecha" name="historico_fecha" value="../../...." size="10" maxlength="10" /><div id="datepicker"></div>
	            </div>
            </div> 
        	</form>          
        </div>
    </div>
</div>

<!-- DIALOGO MODAL -->
<div id="dialogoModalConfirmacion" style="display: none;background-color: white;">
	<div class="ui-widget">
   		<div style="padding: 0.2em 0.7em;" class=""> 
       		<p>
           		<span style="float: left; margin-right: 0.3em;" class=""></span>
           		<spring:message code="jsp.indicador.version.dialogoEliminar"/>
       		</p>
   		</div>
	</div>
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
	$('#parametro').focus();
});

$(document).ready(function() {
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
        $('#formularioCamposAmbitos').submit();
        return false;
        //visualizarVersion();
    });
});

var estilos = {
		modificarEstilos : function () {
			var dialogoModalModificarEstilos = $("#dialogoModalModificarEstilos").dialog({
			    autoOpen: false,
			    show: "blind",
			    hide: "explode",                    
			    width: 675,
			    height: 650,
			    modal: true,
			    resizable: false,
			    title: "<spring:message code='jsp.indicador.modificar.estilos'/>",
			    buttons: {
			    	"<spring:message code="elementos.jsp.boton.aceptar"/>": function() {
			        	$('#colores').val("");
			        	$('#lista_colores div').each(function() {
			        		$('#colores').val($('#colores').val()+"||"+$(this).css("background-color"));
			        	});
			        	$('#formularioEstiloVisualizacion').submit();
			            return false;            
			           
			        },
			        "<spring:message code="elementos.jsp.boton.cancelar"/>": function() {
			            $(this).dialog( "close" );
			            return false;            
			        }
			    }            
			});
			<c:if test="${estilo==null}">
				$('#tamanho_fuente').val("${tamanho_defecto}");
				$('#tipo_letra').val("${fuente_defecto}");
				$('#diametro').val("${diametro_defecto}");
				var estiloColor = "${colores_defecto}";
				if ( estiloColor != "" ) {
					$('#lista_colores').html("");
					$('#colores').val("${colores._defecto}");
					$('#color').val("#fff");
					var listaColores = "${colores_defecto}".split("||");
					for ( var i = 0 ; i < listaColores.length ; i++ ) {
						if ( listaColores[i]!="") {
							var nuevoColor = "<div class='fder' onclick='$(this).remove();' style='cursor:pointer;margin:2px;border:1px solid #000;width:50px;height:50px;background-color:"+listaColores[i]+"'>&nbsp;</div>";
							$('#lista_colores').append(nuevoColor);
						}
					}
				}
			</c:if>
			<c:if test="${estilo!=null}">
				$('#tamanho_fuente').val("${estilo.tamanhoFuente}");
				$('#tipo_letra').val("${estilo.tipoFuente}");
				$('#diametro').val("${estilo.diametro}");
				var estiloColor = "${estilo.colores}";
				if ( estiloColor != "" ) {
					$('#lista_colores').html("");
					$('#colores').val("${estilo.colores}");
					$('#color').val("#fff");
					var listaColores = "${estilo.colores}".split("||");
					for ( var i = 0 ; i < listaColores.length ; i++ ) {
						if ( listaColores[i]!="") {
							var nuevoColor = "<div class='fder' onclick='$(this).remove();' style='cursor:pointer;margin:2px;border:1px solid #000;width:50px;height:50px;background-color:"+listaColores[i]+"'>&nbsp;</div>";
							$('#lista_colores').append(nuevoColor);
						}
					}
				}
			</c:if>
			
			$(dialogoModalModificarEstilos).dialog("open");	
		}	
	};

$(document).ready(function() {
	
	$('#enlaceMostrarCampos').click(function() {
		camposDiagrama.mostrar();
	});
		
	$('.chk_campos').click(function(){
		var cont = parseInt($('#numCamposDiagrama').val());
		 if ($(this).is(":checked")){
			 cont++;
			 $("#formularioCamposAmbitos").append("<input type='hidden' id='campo_"+$(this).attr("id")+"' name='campo_"+$(this).attr("id")+"' value="+$(this).attr("id")+" />");
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
				$("#formularioCamposAmbitos").append("<input type='hidden' id='campo_"+$(this).attr("id")+"' name='campo_"+$(this).attr("id")+"' value="+$(this).attr("id")+" />");
				$('#numCamposDiagrama').val(cont);
				$(this).prop('checked', true);
			});
		}else{
			$("input:checkbox").prop('checked', false);	
		}
	});
	
	$('#parametro').change(function() {
		$('#formularioCamposAmbitos').submit();
        return false;
	}); 
	
	
	$('#modificar_estilos').click(function() {
		estilos.modificarEstilos();
	});
	
	$('#enlaceGuardar_version').click(function() {
		version.guardarVersion();
	});
		
	
	$('#exportar_indicador').click(function() {
		exportar.exportarIndicador();
	});
	
	$("#anhadir_color").click(function(){
		var color = $('#color').val();
		var nuevoColor = "<div class='fder' onclick='$(this).remove();' style='cursor:pointer;margin:2px;border:1px solid #000;width:50px;height:50px;background-color:"+color+"'>&nbsp;</div>";
		$('#lista_colores').append(nuevoColor);
		return false;
	});
	                 
<c:if test="${estilo==null}">
var colores = "${colores_defecto}";
var listaColores = colores.split("||");
var listaFinalColores = [];
for ( var i = 0 ; i < listaColores.length ; i++ ) {
	if ( listaColores[i]!="") {
		listaFinalColores.push(listaColores[i]);
	}
}

var opciones = {
		seriesColors: listaFinalColores,
		legend: { show:true, location: 'ne',placement:'outside' },
		seriesDefaults: {
		renderer: jQuery.jqplot.PieRenderer,
		rendererOptions: {
			showDataLabels: true
		}
	}
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
	legend: { show:true, location: 'ne',placement:'outside'  },
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
	
	$('#td_diagramas_sectores').bind('jqplotDataHighlight', 
	    function(ev, seriesIndex, pointIndex, data) {
        var $this = $(this);                

        $this.attr('title', data[0] + ": " + data[1]);                               
    }); 
	   
	  // Bind a function to the unhighlight event to clean up after highlighting.
	  $('#td_diagramas_sectores').bind('jqplotDataUnhighlight', 
	      function (ev, seriesIndex, pointIndex, data) {
	          $('#tooltip1b').empty();
	          $('#tooltip1b').hide();
	          $('#legend1b tr').css('background-color', '#ffffff');
	      });
	
	var legendTable = $($('.jqplot-table-legend')[0]);    
	legendTable.css('display','block');
	legendTable.css('z-index',100);
	legendTable.css('height','52%');
	legendTable.css('width','150px');
	legendTable.css('margin-top','38%');
	legendTable.css('overflow-y','scroll');
    
    $('#colorpicker').farbtastic('#color');
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
			        	var url = "indicadores.htm?accion=exportar&tipoGrafico=1&tipo="+tipo+"&id=${indicadorDto.id}";
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

var camposDiagrama = {
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
			        	$('#formularioCamposAmbitos').submit();
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

var version = {
		guardarVersion : function () {
			var dialogoModalGuardarVersion = $("#dialogoModalGuardarVersion").dialog({
			    autoOpen: false,
			    show: "blind",
			    hide: "explode",                    
			    width: 675,
			    height: 385,
			    modal: true,
			    resizable: false,
			    title: "<spring:message code='jsp.indicador.version.guardar'/>",
			    buttons: {
			    	"<spring:message code="elementos.jsp.boton.aceptar"/>": function() {
			            $('#formularioGuardarVersion').submit();
			            return false;            
			           
			        },
			        "<spring:message code="elementos.jsp.boton.cancelar"/>": function() {
			            $(this).dialog( "close" );
			            return false;            
			        }
			    }            
			});
			
			
			$(dialogoModalGuardarVersion).dialog("open");	
		}	
	};

	$(function() {
		$( "#datepicker" ).datepicker({altField: 'input#historico_fecha',changeYear: true});
		
		$('#formularioEstiloVisualizacion select').keydown(function(e) {
			 if (e.keyCode == 13) {
				 $('#formularioEstiloVisualizacion').submit();
			}
		 });
		
		$('#formularioGuardarVersion input').keydown(function(e) {
			 if (e.keyCode == 13) {
				 $('#formularioGuardarVersion').submit();
			}
		 });
	});

	function borrarhistorico(){
		var idHistorico = $('#listaHistoricos').val();
		if(idHistorico == null || idHistorico == 0){
			alert("<spring:message code="jsp.indicador.version.seleccionarVersion"/>");
			return false;
		}
		var dialogoModalConfirmacion=$( "#dialogoModalConfirmacion" ).dialog({
	        autoOpen: false,
	        show: "blind",
	        hide: "explode",                    
	        width: 400,
	        height: 200,
	        modal: true,
	        resizable: false,
	        buttons: {
	        	"<spring:message code="elementos.jsp.boton.aceptar"/>": function() {
	            	window.location="${urlBase}indicadores.htm?accion=eliminarVersion&id="+idHistorico+"&idInd="+${indicadorDto.id}+"&tipoGrafico=3";
	                $( this ).dialog( "close" );
	                return false;
	                
	            },
	            "<spring:message code="elementos.jsp.boton.cancelar"/>": function() {
	                $( this ).dialog( "close" );
	                return false;
	            }
	        }            
	    });  
		$(dialogoModalConfirmacion).dialog("open");
		return false;
	}

	function visualizarVersion() {
		var idHistorico = $('#listaHistoricos').val();
		window.location="${urlBase}indicadores.htm?accion=visualizarVersion&id="+idHistorico+"&idInd="+${indicadorDto.id}+"&tipoGrafico=3";
	}
	function cambiarParametro(idHistorico) {
		document.location.href="indicadores.htm?accion=visualizarIndicadorDiagramaSectores&id=${idindicador}&param="+$('#parametros').val()+"&idHistorico="+idHistorico
	}
	
</script>