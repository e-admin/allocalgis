<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<script src="js/util/comun/cambioMenus.js"></script> 
<script src="js/dataTables/js/jquery.dataTables.min.js"></script>
<script src="js/util/datePicker/jquery.ui.datepicker-es.js"></script>
<link type="text/css" rel="stylesheet" href="js/dataTables/css/jquery.dataTables.css" /> 
<script type="text/javascript">
$(document).ready(function() {
	jQuery.extend( jQuery.fn.dataTableExt.oSort, {
	    
		"numeric-comma-pre": function ( a ) {
			var y = (a == "-") ? 0 : a.replace( /\./, "" );
	        var x = (a == "-") ? 0 : y.replace( /,/, "." );
	        return parseFloat( x );
	    },
	 
	    "numeric-comma-asc": function ( a, b ) {
	        return ((a < b) ? -1 : ((a > b) ? 1 : 0));
	    },
	 
	    "numeric-comma-desc": function ( a, b ) {
	        return ((a < b) ? 1 : ((a > b) ? -1 : 0));
	    },
		
		"date-eu-pre": function ( date ) {
	        var date = date.replace(" ", "");
	          
	        if (date.indexOf('.') > 0) {
	            /*date a, format dd.mn.(yyyy) ; (year is optional)*/
	            var eu_date = date.split('.');
	        } else {
	            /*date a, format dd/mn/(yyyy) ; (year is optional)*/
	            var eu_date = date.split('/');
	        }
	          
	        /*year (optional)*/
	        if (eu_date[2]) {
	            var year = eu_date[2];
	        } else {
	            var year = 0;
	        }
	          
	        /*month*/
	        var month = eu_date[1];
	        if (month.length == 1) {
	            month = 0+month;
	        }
	          
	        /*day*/
	        var day = eu_date[0];
	        if (day.length == 1) {
	            day = 0+day;
	        }
	          
	        return (year + month + day) * 1;
	    },
	 
	    "date-eu-asc": function ( a, b ) {
	        return ((a < b) ? -1 : ((a > b) ? 1 : 0));
	    },
	 
	    "date-eu-desc": function ( a, b ) {
	        return ((a < b) ? 1 : ((a > b) ? -1 : 0));
	    }
	} );
	$('#tablaDatos').dataTable( {
    	"aoColumns": [
						<c:forEach items="${listaColumnas}" var="colu" varStatus="cont">
						<c:if test="${colu.columna.tipoAtributo=='VALORFDNUMERICO' || (colu.columna==null && colu.indicadorExpresion!=null)}">
							{ "sType": "numeric-comma" }
						</c:if>
						<c:if test="${colu.columna.tipoAtributo=='VALORFDTEXTO' || colu.columna.tipoAtributo=='VALORFDRELACION' || (colu.columna.tipoAtributo == 'VALORFDGEOGRAFICO' && colu.mostrar)}">
							{ "sType": "string" }
						</c:if>
						<c:if test="${colu.columna.tipoAtributo=='VALORFDFECHA'}">
							{ "sType": "date-eu" }
						</c:if>
						<c:if test="${colu.columna==null && colu.indicadorExpresion==null}">
							{ "sType": "string" }
						</c:if>
						<c:if test="${cont.count!=fn:length(listaColumnas) && (colu.columna.tipoAtributo!='VALORFDGEOGRAFICO' || (colu.columna.tipoAtributo=='VALORFDGEOGRAFICO' && colu.mostrar))}">
								,
						</c:if>
						</c:forEach>
    	            ],
       "bAutoWidth" : true,
       "sScrollX" : "100%",
       "sScrollXInner" : "",
       "bProcessing" : true,
       "bScrollCollapse" : true,
       "sPaginationType": "full_numbers",
       "aLengthMenu": [[10, 25, 50, 100 , -1], [10, 25, 50, 100, "<spring:message code='jsp.tablas.js.menu.todas'/>"]],
        "oLanguage": {
        	"sProcessing": "<spring:message code="jsp.tablas.js.procesando"/>",
            "sLengthMenu": "<spring:message code="jsp.tablas.js.longitud.menu"/>",
            "sZeroRecords": "<spring:message code="jsp.tablas.js.cero.elementos"/>",
            "sInfo": "<spring:message code="jsp.tablas.js.info"/>",
            "sInfoEmpty": "<spring:message code="jsp.tablas.js.info.vacia"/>",
            "sInfoFiltered": "<spring:message code="jsp.tablas.js.info.filtrado"/>",
            "sInfoPostFix": "",
            "sSearch": "<spring:message code="jsp.tablas.js.busqueda"/>",
            "sUrl": "",
            "oPaginate": {
	    		"sFirst":    "<spring:message code="jsp.tablas.js.primero"/>",
	    		"sPrevious": "<spring:message code="jsp.tablas.js.anterior"/>",
	    		"sNext":     "<spring:message code="jsp.tablas.js.siguiente"/>",
	    		"sLast":     "<spring:message code="jsp.tablas.js.ultimo"/>"
	    	}
        }
    });
});
</script>
<div class="cuerpoprincipal">
	<div class="areamenu">
		<ul class="menusecundario">
			<li class="on">
				<a href="indicadorPublico.htm?accion=visualizarIndicadorTabla&id=${indicadorDto.id}">
					<spring:message code="jsp.indicador.tabla.valores"/>
				</a>
			</li>
			<li>
				<a href="indicadorPublico.htm?accion=mostrarOpcionesDiagramasPublicos&id=${indicadorDto.id}&tipo=barras">
					<spring:message code="jsp.indicador.diagrama.barras"/>
				</a>
			</li>
			<li>
				<a href="indicadorPublico.htm?accion=mostrarOpcionesDiagramasPublicos&id=${indicadorDto.id}&tipo=sectores">
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
	    	    <c:if test="${errorEstiloVisualizacion!=null && errorEstiloVisualizacion!='' || errorGuardarVersion!=null && errorGuardarVersion!=''}">
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
					         <c:if test="${errorFichero!=null && errorFichero!=''}">
					         	<li><spring:message code="${errorFichero}"/></li>
					         </c:if>
					         <c:if test="${errorColumnas!=null && errorColumnas!=''}">
					         	<li><spring:message code="${errorColumnas}"/></li>
					         </c:if>
						</ul>
					</div>
				</div>
			</div>
			<br/>
   		</c:if>
   		<c:if test="${errorFichero!=null && errorFichero!=''|| errorColumnas!=null && errorColumnas!=''}">
          	<div class="controlAlertas">	
				<div class="ui-widget">
					<div class="ui-state-error ui-corner-all"> 
						<ul>
							<c:if test="${errorColumnas!=null && errorColumnas!=''}">
					         	<li><spring:message code="${errorColumnas}"/></li>
					        </c:if>
				         	<c:if test="${errorFichero!=null && errorFichero!=''}">
				         		<li><spring:message code="${errorFichero}"/></li>
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
			<c:if test="${numFilas <= 0}">
				<p>
					<spring:message code="jsp.fuente.tabla.sin.valores"></spring:message>
				</p>
			</c:if>
			<c:if test="${numFilas > 0}">
				<c:if test="${estilo==null}">
					<table id="tablaDatos" class="tablasTrabajo" cellpadding="0" cellspacing="0" style="padding-bottom:2px;">
				</c:if>
				<c:if test="${estilo!=null}">
					<table id="tablaDatos" class="tablasTrabajo" cellpadding="0" cellspacing="0" style="padding-bottom:2px;font-family:${estilo.tipoFuente} !important;font-size:${estilo.tamanhoFuente}px !important;">
				</c:if>
				<thead>
					<tr>
						<c:forEach items="${datos}" var="columna">
							<th class="tablaTitulo izquierda" scope="col">${columna.key}</th>
						</c:forEach>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="contador" varStatus="status" begin="0" end="${numFilas}" step="1">
						<tr onmouseout="this.className=''" onmouseover="this.className='destacadoTablas'" class="">
							<c:forEach items="${datos}" var="valoresFila">
								<c:if test="${valoresFila.value.valores[(status.count)-1].texto==null}">
									<td class="izquierda">&nbsp;</td>
								</c:if>
								<c:if test="${valoresFila.value.valores[(status.count)-1].texto!=null}">
									<c:forEach items="${listaColumnas}" var="colum">
										<c:if test="${colum.nombre==valoresFila.key}">
											<c:if test="${(colum.columna==null && colum.indicadorExpresion!=null) || colum.columna.tipoAtributo=='VALORFDNUMERICO'}">
												<!-- PONER LOS DECIMALEs -->
												<td class="derecha">
													<fmt:formatNumber type="number" minFractionDigits="${estilo.decimales}" maxFractionDigits="${estilo.decimales}" value="${valoresFila.value.valores[(status.count)-1].texto}" />
												</td>
											</c:if>
											<c:if test="${not ((colum.columna==null && colum.indicadorExpresion!=null) || colum.columna.tipoAtributo=='VALORFDNUMERICO')}">
												<!-- no tocar -->
												<td class="izquierda">${valoresFila.value.valores[(status.count)-1].texto}</td>
											</c:if>
										</c:if>
									</c:forEach>
								</c:if>
							</c:forEach>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			</c:if>
			<!--  HISTÓRICOS -->
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
				<li><a href="indicadorPublico.htm?accion=exportarPDF&id=${indicadorDto.id}&idHistorico=${historico.id}&tipo=tabla&desde=0"><spring:message code="jsp.indicador.informe.pdf"/></a></li>
			</ul>
		</div>
	</div>
	<div class="clear"></div>


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
	if (($(this).attr('type') == 'text') && ($(this).is('input'))){
		$(this).focus();
		return false; 
	}
	if ($(this).is('textarea')){
		$(this).focus();
		return false; 
	} 
	if ($(this).is('select')){
		$(this).focus();
		return false; 
	} 
	$('#exportar_indicador').click(function() {
		exportar.exportarIndicador();
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
	window.location="${urlBase}indicadorPublico.htm?accion=visualizarVersion&id="+idHistorico+"&idInd="+${indicadorDto.id}+"&tipoGrafico=1";
}
</script>