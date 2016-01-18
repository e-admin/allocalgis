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
				<a href="indicadorPublico.htm?accion=visualizarIndicadorTabla&id=${idIndicador}">
					<spring:message code="jsp.indicador.tabla.valores"/>
				</a>
			</li>
			<li>
				<a href="indicadorPublico.htm?accion=visualizarIndicadorDiagramaBarras&id=${idIndicador}">
					<spring:message code="jsp.indicador.diagrama.barras"/>
				</a>
			</li>
			<li>
				<a href="indicadorPublico.htm?accion=visualizarIndicadorDiagramaSectores&id=${idIndicador}"><spring:message code="jsp.indicador.diagrama.sectores"/>
				</a>
			</li>
			<li class="on">
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
var numRangos = 0;
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
		        "Aceptar": function() {
		        	var tipo = $('#tipo_exportacion').val();
		        	if ( tipo == "" ) {
		        		alert("Seleccione un tipo para la exportación");
		        		return false;
		        	}
		        	$(this).dialog( "close" );
		        	var url = "indicadorPublico.htm?accion=exportar&tipoGrafico=1&tipo="+tipo+"&id=${indicadorDto.id}";
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

$(document).ready(function() {
	init();
	
	
	
	$('#exportar_indicador').click(function() {
		exportar.exportarIndicador();
	});
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
					<c:if test="${valores.value.valores[(status.count)-1].texto > valoresRango.inicio && valores.value.valores[(status.count)-1].texto <= valoresRango.fin}">
						<c:set var="color" value="${valoresRango.color}"/>
					</c:if>
					</c:if>
				</c:if>
			</c:forEach>
			var valorEstilo${status.count} =  new OpenLayers.StyleMap({
			    fillColor: <c:if test="${color==null || color==''}">'${color_defecto}'</c:if><c:if test="${color!=null}">'${color}'</c:if>,
		     	strokeColor: <c:if test="${color==null || color==''}">'${color_defecto}'</c:if><c:if test="${color!=null}">'${color}'</c:if>,
		     	strokeWidth: 1,
		     	fillOpacity: 0.9
		    });
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
				
				if ( element.length>0) {
					test = formats['in'][type].read(element);
					if ( test ) {
						features.push(test);
						feature.push(test);
						var vector = new OpenLayers.Layer.Vector("feature${status.count}", {
				            styleMap: valorEstilo${status.count}
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
		window.location="${urlBase}indicadorPublico.htm?accion=visualizarVersion&id="+idHistorico+"&idInd="+${indicadorDto.id}+"&tipoGrafico=4";
	}

	function cambiarParametro(idHistorico) {
		document.location.href="indicadorPublico.htm?accion=visualizarIndicadorMapa&id=${idIndicador}&param="+$('#parametros').val()+"&idHistorico="+idHistorico;
	}
</script>