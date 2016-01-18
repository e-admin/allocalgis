<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<script src="js/util/comun/cambioMenus.js"></script>
<script src="js/util/indicadores/diagramaBarras.js"></script>
<script src="js/util/datePicker/jquery.ui.datepicker-es.js"></script>

<script type="text/javascript" src="js/graph/jquery.jqplot.min.js"></script>
<script type="text/javascript" src="js/graph/jqplot.barRenderer.min.js"></script>
<script type="text/javascript" src="js/graph/jqplot.categoryAxisRenderer.min.js"></script>
<script type="text/javascript" src="js/graph/jqplot.pointLabels.min.js"></script>
<!--[if IE]><script type="text/javascript" src="js/graph/excanvas.min.js"></script><![endif]-->

<link rel="stylesheet" type="text/css" href="js/graph/jquery.jqplot.min.css" />

<!--  color picker -->
<script type="text/javascript" src="js/colorpicker/farbtastic.js"></script>
<link rel="stylesheet" href="js/colorpicker/farbtastic.css" type="text/css" />

<div class="cuerpoprincipal">
	<div class="areamenu">
		<ul class="menusecundario">
			<li>
				<a href="indicadores.htm?accion=visualizarIndicadorTabla&id=${indicadorDto.id}">
					<spring:message code="jsp.indicador.tabla.valores"/>
				</a>
			</li>
			<li class="on">
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
				<select name="parametros" id="parametros" onchange="cambiarParametro(${historico.id});">
					<c:forEach items="${listaColumnas}" var="col">
	                	<option value="${col.nombre}" <c:if test="${col.nombre eq parametro}">selected="selected"</c:if>>${col.nombre}</option>
	                </c:forEach>
                </select>
			</div>
			<div id="div_diagramas_barras" style="width: 545px; height: 500px;">
				<c:if test="${estilo==null}">
				<table id="tabla_diagramas_barras" style="width:545px" cellspacing="0" cellpadding="0">
				</c:if>
				<c:if test="${estilo!=null}">
				<table id="tabla_diagramas_barras" cellspacing="0" cellpadding="0" style="width:545px;font-family:${estilo.tipoFuente} !important;font-size:${estilo.tamanhoFuente}px !important;">
				</c:if>
					<thead>
						<tr>
							<th><spring:message code="jsp.indicador.diagrama.barras"/></th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="paginas" varStatus="pagina" begin="0" end="${numPaginasDiagramas}" step="1">
						    <tr><td><div id="td_diagramas_barras${pagina.count}" style="width: 550px; height: 500px;"></div></td></tr>
						</c:forEach>
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
				<li><a href="indicadores.htm?accion=exportarPDF&id=${indicadorDto.id}&tipo=tabla"><spring:message code="jsp.indicador.informe.pdf"/></a></li>
				<c:if test="${!usuarioInvitado && (usuarioAdministrador || tienePermisos)}">
					<li><a href="indicadores.htm?accion=editaIndicador&id=${indicadorDto.id}"><spring:message code="jsp.indicadores.indicador.modificarIndicador"/></a></li>
					<li><a id="modificar_estilos" href="#"><spring:message code="jsp.indicador.modificar.estilos"/></a></li>
				</c:if>
			</ul>
		</div>
	</div>
	<div class="clear"></div>
</div>

<!-- añadir / modifcar estilos -->
<div id="dialogoModalModificarEstilos" style="display: none;background-color: white;">
    <div class="ui-widget">
        <div style="padding: 0.2em 0.7em;" class=""> 
            <form id="formularioEstiloVisualizacion" action="indicadores.htm" method="post">
            	<input type="hidden" name="accion" value="nuevoEstiloVisualizacionDiagramaBarras"/>
            	<input type="hidden" name="id" value="${indicadorDto.id}"/>
            <div class="inf15">
            	<spring:message code="jsp.visualizacion.barras"/>
            </div>
            <div class="fizq">
	            <div class="linea">
	            	<label class="label fizq" for="tamanho_fuente"><spring:message code="jsp.visualizacion.tamanho.fuente"/></label>
	            	<select name="tamanho_fuente" id="tamanho_fuente" >
						<option value="" selected="selected">&nbsp;</option>
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
	            	<label class="label fizq" for="tamanho"><spring:message code="jsp.visualizacion.tamanho"/></label>
	            	<select name="tamanho" id="tamanho" >
						<option value="" selected="selected">&nbsp;</option>
						<c:forEach items="${tamanhosColumna}" var="tamanho">
                      	<option value="${tamanho}">${tamanho}</option>
                       </c:forEach>
                    </select>
	            </div>
	            <div class="linea">
	            	<label class="label fizq" for="color"><spring:message code="jsp.visualizacion.color"/></label>
	            	<input type="text" id="color" name="color" value="#fff"/>
 					<div id="colorpicker"></div>
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
            	<input type="hidden" name="tipoGrafico" value="2" />
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
	$('#parametros').focus();
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
        visualizarVersion();
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
	
var estilos = {
	modificarEstilos : function () {
		var dialogoModalModificarEstilos = $("#dialogoModalModificarEstilos").dialog({
		    autoOpen: false,
		    show: "blind",
		    hide: "explode",                    
		    width: 675,
		    height: 500,
		    modal: true,
		    resizable: false,
		    title: "<spring:message code='jsp.indicador.modificar.estilos'/>",
		    buttons: {
		    	"<spring:message code="elementos.jsp.boton.aceptar"/>": function() {
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
			$('#tamanho').val("${tamanho_columna_defecto}");
			$('#color').val("${color_defecto}");
		</c:if>
		<c:if test="${estilo!=null}">
			$('#tamanho_fuente').val("${estilo.tamanhoFuente}");
			$('#tipo_letra').val("${estilo.tipoFuente}");
			$('#tamanho').val("${estilo.tamanho}");
			var estiloColor = "${estilo.color}";
			if ( estiloColor != "" )
				$('#color').val("${estilo.color}");
		</c:if>
		
		$(dialogoModalModificarEstilos).dialog("open");	
	}	
};

$(document).ready(function() {
	$('#modificar_estilos').click(function() {
		estilos.modificarEstilos();
	});
	
	$('#enlaceGuardar_version').click(function() {
		version.guardarVersion();
	});
	
	
	
	$('#exportar_indicador').click(function() {
		exportar.exportarIndicador();
	});
	
	var opciones = {
	        seriesDefaults:{
	            renderer:$.jqplot.BarRenderer,
	            pointLabels: {show: true},
				rendererOptions:{
        			barWidth:25
				}
	        },
	        series:[	            
	            <c:if test="${estilo==null}">
	            {label:$('#parametros').val(),color: '#00632E'}	           
	            </c:if>
	            <c:if test="${estilo!=null}">
	            {label:$('#parametros').val(),color: '${estilo.color}'}
	            </c:if>
	        ],
	        legend: {
		      show: true,
		      location: 'nw',
		      placement: 'insideGrid'
		    },
	        axes: {
	            xaxis: {
	                renderer: $.jqplot.CategoryAxisRenderer,
	                ticks: ticks
	            },
	            yaxis: {
	                pad: 1.05	                
	            }
	        }
	    };
	
	var ticks = [];
	
	<c:forEach var="paginas" varStatus="pagina" begin="0" end="${numPaginasDiagramas}" step="1">
	
		var data${pagina.count} = [
		<c:forEach var="contador" varStatus="status" begin="${pagina.count*elementosPagina}" end="${pagina.count*elementosPagina+elementosPagina-1}" step="1">
			<c:forEach items="${datos}" var="valoresFila" varStatus="cont">
				<c:if test="${not empty valoresFila.value.valores[(status.count)-1+((pagina.count-1)*elementosPagina)].texto && cont.count%2!=0}">
					<c:if test="${cont.count%2!=0}">
						["${fn:trim(valoresFila.value.valores[(status.count)-1+((pagina.count-1)*elementosPagina)].texto)}",
					</c:if>
				</c:if>
				<c:if test="${not empty valoresFila.value.valores[(status.count)-1+((pagina.count-1)*elementosPagina)].texto && cont.count%2==0}">
					<c:if test="${cont.count%2==0}">				
						<c:if test="${empty valoresFila.value.valores[(status.count)-1+((pagina.count-1)*elementosPagina)].texto || empty valoresFila.value.valores[(status.count)-1+((pagina.count-1)*elementosPagina)].texto==null}">
							0],
						</c:if>
						<c:if test="${not empty valoresFila.value.valores[(status.count)-1+((pagina.count-1)*elementosPagina)].texto}">
							${valoresFila.value.valores[(status.count)-1+((pagina.count-1)*elementosPagina)].texto}],
						</c:if>
		    		</c:if>
		    	</c:if>
			</c:forEach>
		</c:forEach>
	    ];
	    var plot${pagina.count} = null;
	    if (data${pagina.count}.length != 0) {
		    plot${pagina.count} = $.jqplot('td_diagramas_barras${pagina.count}', [data${pagina.count}], opciones);
	    } else {
	    	var el_td = $("#td_diagramas_barras${pagina.count}").parent();
	    	var el_tr = el_td.parent();
	    	el_tr.remove();
	    }
	</c:forEach>
	
	
    $('#tabla_diagramas_barras').dataTable( {
    	"bFilter" : false,
    	"iDisplayLength": 1,
    	"bLengthChange": false,
    	"bInfo": false,
    	"aLengthMenu": [[1], [1]],
        "sPaginationType": "full_numbers",
        "oLanguage": {
        	"sProcessing": "<spring:message code="jsp.tablas.js.procesando"/>",
            "sZeroRecords": "<spring:message code="jsp.tablas.js.cero.elementos"/>",
            "sInfoEmpty": "<spring:message code="jsp.tablas.js.info.vacia"/>",
            "sInfoFiltered": "<spring:message code="jsp.tablas.js.info.filtrado"/>",
            "sInfoPostFix": "",
            "sSearch": "",
            "sUrl": "",
            "oPaginate": {
	    		"sFirst":    "<spring:message code="jsp.tablas.js.primero"/>",
	    		"sPrevious": "<spring:message code="jsp.tablas.js.anterior"/>",
	    		"sNext":     "<spring:message code="jsp.tablas.js.siguiente"/>",
	    		"sLast":     "<spring:message code="jsp.tablas.js.ultimo"/>"
	    	}
        }
    });
    
    $('#colorpicker').farbtastic('#color');
});
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
	            	window.location="${urlBase}indicadores.htm?accion=eliminarVersion&id="+idHistorico+"&idInd="+${indicadorDto.id}+"&tipoGrafico=2";
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
		window.location="${urlBase}indicadores.htm?accion=visualizarVersion&id="+idHistorico+"&idInd="+${indicadorDto.id}+"&tipoGrafico=2";
	}
	
	function cambiarParametro(idHistorico) {
		document.location.href="indicadores.htm?accion=visualizarIndicadorDiagramaBarras&id=${idindicador}&param="+$('#parametros').val()+"&idHistorico="+idHistorico;
	}
	
</script>
<script src="js/dataTables/js/jquery.dataTables.min.js"></script>
<link type="text/css" rel="stylesheet" href="js/dataTables/css/jquery.dataTables.css" /> 