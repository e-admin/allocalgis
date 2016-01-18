<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script src="js/util/comun/cambioMenus.js"></script>
<script src="js/util/datePicker/jquery.ui.datepicker-es.js"></script>
<script src="js/openlayers/OpenLayers.js"></script>

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
			<li class="on">
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
			<div class="linea">
				<label><spring:message code="jsp.indicador.parametro"/></label>
				<select name="parametros" id="parametros" onchange="cambiarParametro(${historico.id});">
					<c:forEach items="${listaColumnas}" var="col">
	                	<option value="${col.nombre}" <c:if test="${col.nombre eq parametro}">selected="selected"</c:if>>${col.nombre}</option>
	                </c:forEach>
                </select>
			</div>
			<!-- MAPA -->
			<div id="map" style="width: 550px; height: 550px;"></div>
			
			<c:if test="${estilo!=null}">
			<!-- leyenda de rangos -->
			<div class="clear"></div>
			<br/>			
			<table>
				<thead>
					<tr>
						<th class="tablaTitulo"><spring:message code="jsp.visualizacion.inicio.rango"/></th>
						<th class="tablaTitulo"><spring:message code="jsp.visualizacion.fin.rango"/></th>
						<th class="tablaTitulo"><spring:message code="jsp.visualizacion.color"/></th>
					</tr>
				</thead>				
				<tbody>
					<c:forEach items="${rangos}" var="rang" varStatus="status">
						<tr>
							<td>${rang.inicio}</td>
							<td>${rang.fin}</td>
							<td style="background-color:${rang.color}">&nbsp;</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			</c:if>
			
			<!-- HISTORICOS -->
			<div class="clear"></div>
			<br/>
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

<!-- añadir / modificar estilos -->
<div id="dialogoModalModificarEstilos" style="display: none;background-color: white;">
    <div class="ui-widget">
        <div style="padding: 0.2em 0.7em;" class=""> 
            <div class="inf15">
            	<spring:message code="jsp.visualizacion.mapa"/>
            	<p class="texto_07"><spring:message code="jsp.visualizacion.mapa.instrucciones.rangos"/></p>
            </div>
            <div>
            	<form id="formularioEstiloVisualizacion" action="indicadores.htm" method="post">
	                <input type="hidden" name="accion" value="nuevoEstiloVisualizacionMapa"/>
	            	<input type="hidden" name="id" value="${indicadorDto.id}"/>
	                <div class="linea">
		            	<label class="label fizq" for="inicio"><spring:message code="jsp.visualizacion.inicio.rango"/></label>
		            	<input type="text" id="inicio" name="inicio" />
		            </div>
		            <div class="linea">
		            	<label class="label fizq" for="fin"><spring:message code="jsp.visualizacion.fin.rango"/></label>
		            	<input type="text" id="fin" name="fin" />
		            </div>
		            <div class="linea">
		            	<label class="label fizq" for="color"><spring:message code="jsp.visualizacion.color"/></label>
		            	<input type="text" id="color" name="color" value="#FFF"/>
	 					<div id="colorpicker"></div>
		            </div>
		            <div class="linea">
		            	<a href="#" id="crear_rango" onclick="return false;"><spring:message code="jsp.visualizacion.crear.rango"/></a>
		            </div>
		            <hr/>
		            <div class="linea" style="width:100%;">
		            	<label class="label"><spring:message code="jsp.visualizacion.rangos"/></label>
	 					<c:if test="${empty rangos}">
		 					<div>
		 						<table class="tablasTrabajo" id="rangos" cellspacing="0" cellpadding="0">
		 							<thead>
		 								<tr>
		 									<th class="tablaTitulo"><spring:message code="jsp.visualizacion.inicio.rango"/></th>
		 									<th class="tablaTitulo"><spring:message code="jsp.visualizacion.fin.rango"/></th>
		 									<th class="tablaTitulo"><spring:message code="jsp.visualizacion.color"/></th>
		 									<th class="tablaTitulo"><spring:message code="elementos.jsp.boton.borrar"/></th>
		 								</tr>
		 							</thead>
		 							<tbody>
		 								<tr><td class="tablasDetalle">0</td><td class="tablasDetalle">99999999999999</td><td class="tablasDetalle" style="background-color:${color_defecto}">&nbsp;</td><td class="tablasDetalle borrar_rango" id="borrar_rango_1" style="cursor:pointer;"><spring:message code="elementos.jsp.boton.borrar"/></td></tr>	
		 							</tbody>	 							
		 						</table>
		 					
			 					<div id="inputs_rangos" style="display:none;">
									<input type="hidden" id="inicio_rango_1" name="inicio_rango_1" value="0"/>
									<input type="hidden" id="fin_rango_1" name="fin_rango_1" value="99999999999999"/>
									<input type="hidden" id="color_rango_1" name="color_rango_1" value="${color_defecto}"/>	
			 					</div>
			 					<input type="hidden" name="num_rangos" id="num_rangos" value="1"/>
				            </div>
	 					</c:if>
	 					<c:if test="${not empty rangos}">
	 					<div>
	 						<table class="tablasTrabajo" id="rangos" cellspacing="0" cellpadding="0">
	 							<thead>
	 								<tr>
	 									<th class="tablaTitulo"><spring:message code="jsp.visualizacion.inicio.rango"/></th>
	 									<th class="tablaTitulo"><spring:message code="jsp.visualizacion.fin.rango"/></th>
	 									<th class="tablaTitulo"><spring:message code="jsp.visualizacion.color"/></th>
	 									<th class="tablaTitulo"><spring:message code="elementos.jsp.boton.borrar"/></th>
	 								</tr>
	 							</thead>
	 							<tbody>
	 								<c:forEach items="${rangos}" var="rango" varStatus="status">
	 									<tr><td class="tablasDetalle">${rango.inicio}</td><td class="tablasDetalle">${rango.fin}</td><td class="tablasDetalle" style="background-color:${rango.color}">&nbsp;</td><td class="tablasDetalle borrar_rango" id="borrar_rango_${status.count}" style="cursor:pointer;"><spring:message code="elementos.jsp.boton.borrar"/></td></tr>	
	 								</c:forEach>
	 							</tbody>	 							
	 						</table>
	 					
		 					<div id="inputs_rangos" style="display:none;">
		 						<c:forEach items="${rangos}" var="range" varStatus="status">
									<input type="hidden" id="inicio_rango_${status.count}" name="inicio_rango_${status.count}" value="${range.inicio}"/>
									<input type="hidden" id="fin_rango_${status.count}" name="fin_rango_${status.count}" value="${range.fin}"/>
									<input type="hidden" id="color_rango_${status.count}" name="color_rango_${status.count}" value="${range.color}"/>	
								</c:forEach>
		 					</div>
		 					<input type="hidden" name="num_rangos" id="num_rangos" value="${numRangos}"/>
			            </div>	
			            </c:if>
			        </div>
		    	</form>                    	            
            </div> 
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
            	<input type="hidden" name="tipoGrafico" value="4" />
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
    $(":input:visible:enabled").each(function() {
        if (($(this).attr('type') == 'text') && ($(this).is('input'))){
            $(this).focus();
            return false; 
        }
        if ($(this).is('textarea')){
            $(this).focus();
            return false; 
        } 
        if (($(this).attr('type') == 'checkbox')){
            $(this).focus();
            return false; 
        }  
    });
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

var numRangos = 0;

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
		crearRango : function() {
			if ( $('#inicio').val()=="" || $('#fin').val()=="" || $('#color').val()=="") {
				alert("<spring:message code="jsp.visualizacion.mapa.rangos.vacios"/>");
				return false;
			}
			if ( parseInt($('#inicio').val())>parseInt($('#fin').val())) {
				alert("<spring:message code="jsp.visualizacion.mapa.rangos.incorrectos"/>");
				return false;
			}
			
			numRangos = $('#num_rangos').val();
			numRangos++;
			var color = "<td class='tablasDetalle' style='background-color:"+$('#color').val()+"'>&nbsp;</td>";
			html = "<tr><td class='tablasDetalle'>"+$('#inicio').val()+"</td><td class='tablasDetalle'>"+$('#fin').val()+"</td>"+color+"<td class='tablasDetalle borrar_rango' id='borrar_rango_"+numRangos+"' style='cursor:pointer;'>Borrar</td></tr>";
			$('#rangos tbody').append(html);
			
			$('#inputs_rangos').append("<input type='hidden' id='inicio_rango_"+numRangos+"' name='inicio_rango_"+numRangos+"' value='"+$('#inicio').val()+"'/>");
			$('#inputs_rangos').append("<input type='hidden' id='fin_rango_"+numRangos+"' name='fin_rango_"+numRangos+"' value='"+$('#fin').val()+"'/>");
			$('#inputs_rangos').append("<input type='hidden' id='color_rango_"+numRangos+"' name='color_rango_"+numRangos+"' value='"+$('#color').val()+"'/>");
			$('#num_rangos').val(numRangos);
			
			//Limpiamos el formulario
			$('#inicio').val("");
			$('#fin').val("");	
		},
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
			        	$('#formularioEstiloVisualizacion').submit();
			            return false;            
			           
			        },
			        "<spring:message code="elementos.jsp.boton.cancelar"/>": function() {
			            $(this).dialog( "close" );
			            return false;            
			        }
			    }            
			});
			$(dialogoModalModificarEstilos).dialog("open");	
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
	            	window.location="${urlBase}indicadores.htm?accion=eliminarVersion&id="+idHistorico+"&idInd="+${indicadorDto.id}+"&tipoGrafico=4";
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
	
	function cambiarParametro(idHistorico) {
		document.location.href="indicadores.htm?accion=visualizarIndicadorMapa&id=${indicadorDto.id}&param="+$('#parametros').val()+"&idHistorico="+idHistorico;
	}

$(document).ready(function() {
	init();
	$('#modificar_estilos').click(function() {
		estilos.modificarEstilos();
	});
	
	
	
	$('#enlaceGuardar_version').click(function() {
		version.guardarVersion();
	});
	
	$('#exportar_indicador').click(function() {
		exportar.exportarIndicador();
	});
	
	$('.borrar_rango').live('click',function() {
		var numeroRango = $(this).attr("id").substring("borrar_rango_".length);
		$(this).parent().remove();
		$('#inicio_rango_'+numeroRango).remove();
		$('#fin_rango_'+numeroRango).remove();
		$('#color_rango_'+numeroRango).remove();		
	});
	
	$('#crear_rango').click(function() {
		estilos.crearRango();
	});
	
	$('#colorpicker').farbtastic('#color');
});

</script>

<!--  MAPA para ShapeFile, WFS, GML y BD Espacial -->
<script type="text/javascript">
	
	var map, vectors, formats;
	var valorProyeccion= "EPSG:23029";
	
	var color;
	<c:forEach var="contador" varStatus="status" begin="0" end="${numFilas}" step="1">
		<c:forEach items="${datos}" var="valores" varStatus="cont">
			<c:forEach items="${rangos}" var="valoresRango">
				<c:if test="${cont.count%2==0}">
					<c:if test="${valores.value.valores[(status.count)-1].texto!='' && valores.value.valores[(status.count)-1].texto!=null}"> 	
					<c:if test="${valores.value.valores[(status.count)-1].texto >= valoresRango.inicio && valores.value.valores[(status.count)-1].texto < valoresRango.fin}">
						<c:set var="color" value="${valoresRango.color}"/>
					</c:if>
					</c:if>
				</c:if>
			</c:forEach>
			
			
			var valorEstilo${status.count} = OpenLayers.Util.extend(
				{}, 
				OpenLayers.Feature.Vector.style['default']
			);
			
			valorEstilo${status.count}.fillColor = <c:if test="${color==null || color==''}">'${color_defecto}'</c:if><c:if test="${color!=null}">'${color}'</c:if>;
			valorEstilo${status.count}.strokeColor = <c:if test="${color==null || color==''}">'${color_defecto}'</c:if><c:if test="${color!=null}">'${color}'</c:if>;
			valorEstilo${status.count}.fillOpacity = 0.7;
			valorEstilo${status.count}.strokeWidth= 1;
		</c:forEach>
	</c:forEach>
		
	
	function init() {
		
		var bounds = new OpenLayers.Bounds(
			475774.9406515219, 4629122,
			686857, 4849600
		);
		
		var options = {
			controls: [],
			maxExtent: bounds,
			maxResolution: 861.2421875,
			projection: valorProyeccion,
			units: 'm'
		};
		
		map = new OpenLayers.Map('map',options);
				
		var wms = new OpenLayers.Layer.WMS(
			"loured:municipios - Tiled", "http://dpac1102.probas.enxenio.net:80/geoserver/loured/wms",
			{
				LAYERS: 'loured:municipios',
				STYLES: '',
				format: 'image/png',
				tiled: true,
				tilesOrigin : map.maxExtent.left + ',' + map.maxExtent.bottom
			},
			{
				buffer: 0,
				displayOutsideMaxExtent: true,
				isBaseLayer: true,
				yx : {valorProyeccion : false}
			}
		); 
		vectors = new OpenLayers.Layer.Vector("Vector Layer");
		map.addLayers([ wms, vectors ]);
		
		var in_options = {
			'internalProjection' : map.baseLayer.projection,
			'externalProjection' : new OpenLayers.Projection(valorProyeccion)
		};
		var out_options = {
			'internalProjection' : map.baseLayer.projection,
			'externalProjection' : new OpenLayers.Projection(valorProyeccion)
		};
		formats = {
			'in' : {
				wkt : new OpenLayers.Format.WKT(in_options)
			},
			'out' : {
				wkt : new OpenLayers.Format.WKT(out_options)
			}
		};
		
		map.addControl(new OpenLayers.Control.PanZoomBar({
			position: new OpenLayers.Pixel(2, 15)
		}));
		map.addControl(new OpenLayers.Control.Navigation());
		
		options = {
			hover : false
		};
		var select = new OpenLayers.Control.SelectFeature(vectors, options);
		map.addControl(select);
		select.activate();			
		
		var element, test;
		var features = [];
		var feature = [];
		var type = "wkt";
		
		<c:forEach var="contador" varStatus="status" begin="0" end="${numFilas}" step="1">
			<c:forEach items="${mapa}" var="valoresFila">
				element = "${valoresFila.value.valores[(status.count)-1].texto}";
				
				if (element.length > 0) {
					test = formats['in'][type].read(element);
					if ( test ) {
						features.push(test);
						feature.push(test);
						var vector = new OpenLayers.Layer.Vector("feature${status.count}", {
							style: valorEstilo${status.count}
				        });
						vector.addFeatures(feature);
						map.addLayer(vector);
						feature = [];
					}
					else {
						$('#output').val("ERROR: "+element);
						return false;
					}	
				}
			</c:forEach>
		</c:forEach>
		
		var bounds;
		
		if (features.constructor != Array) {
			features = [ features ];
		}
		for ( var i = 0; i < features.length; ++i) {
			if (!bounds) {
				bounds = features[i].geometry.getBounds();
			} else {
				bounds.extend(features[i].geometry.getBounds());
			}
		}
		//vectors.addFeatures(features);
		map.zoomToExtent(bounds);
	}
	
	function visualizarVersion() {
		var idHistorico = $('#listaHistoricos').val();
		window.location="${urlBase}indicadores.htm?accion=visualizarVersion&id="+idHistorico+"&idInd="+${indicadorDto.id}+"&tipoGrafico=4";
	}
</script>