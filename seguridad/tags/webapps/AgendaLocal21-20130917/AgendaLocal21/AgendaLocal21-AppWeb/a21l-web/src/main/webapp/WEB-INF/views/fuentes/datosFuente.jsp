<%@page contentType="text/html" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<script type="text/javascript" src="js/openlayers/OpenLayers.js"></script>
<script type="text/javascript" src="js/util/comun/cambioMenus.js"></script>
<script type="text/javascript" src="js/util/comun/funcions.js"></script><script src="js/dataTables/js/jquery.dataTables.min.js"></script>
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
	$('#tablaEsquema').dataTable( {
       "aoColumns": [ { "sType": "string"},{ "sType": "string"},{ "sType": "string"}],
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
					<c:forEach items="${datos}" var="colu" varStatus="cont">
						<c:forEach items="${mapaNumerico}" var="columna">
							<c:if test="${columna.key==colu.key }">
								<c:if test="${columna.value==true}">
									{ "sType": "numeric-comma" },
								</c:if>
								<c:if test="${columna.value==false}">
									{ "sType": "string"},
								</c:if>
							</c:if>
						</c:forEach>
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
<script type="text/javascript">
	$(document).ready(function() {
		$('#pestanhas').tabs();
	});
</script>
<div class="cuerpoprincipal">
	<div class="areamenu">
		<ul class="menusecundario">
			<c:if test="${( esinterna == '1')}">
				<li class="on"><a href="?accion=verCatalogoSistema"><spring:message code="jsp.catalogo.titulo" /></a></li>
				<li><a href="fuentes.htm"><spring:message code="jsp.fuentes.fuentes" /></a></li>
			</c:if>
			<c:if test="${(esinterna != '1' )}">
				<li><a href="?accion=verCatalogoSistema"><spring:message code="jsp.catalogo.titulo" /></a></li>
				<li class="on"><a href="fuentes.htm"><spring:message code="jsp.fuentes.fuentes" /></a></li>
			</c:if>
		</ul>
	</div>
	<div class="areacentral">
		<h2 class="inf15">
			<spring:message code="jsp.fuentes.nombre.fuentes" />&nbsp;${fuenteDto.nombre}
		</h2>
		
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
         <c:if test="${errorFichero!=null}">
			<div class="controlAlertas">	
				<div class="ui-widget">
            		<div class="ui-state-error ui-corner-all"> 
						<p>
							<span class="ui-icon ui-icon-alert fizq"></span>
				          	<spring:message code="${errorFichero}" />
			         	</p>
			  	 	</div>
         		</div>
        		</div>
        		<br/>     
		</c:if>
		 
		<div class="fizq ancho100">
			<c:if test="${empty listaTablas}">
				<div>
					<spring:message code="global.formulario.lista.vacia" />
				</div>
			</c:if>
			<div class="fizq" style="height:270px; width:23%; overflow-y:scroll;">
			<c:if test="${!empty listaTablas}">
				<table class="tablasTrabajo peque fizq" cellpadding="0" cellspacing="0">
					<tbody>
						<tr>
							<th class="tablaTitulo izquierda" scope="col"><spring:message code="jsp.fuentes.nombre.tabla" /></th>
						</tr>
						<c:forEach items="${listaTablas}" var="nomTabla">
							<c:if test="${esquemaTabla != null && esquemaTabla != 'null' && esquemaTabla != ''}">
								<c:if test="${esquemaTabla eq nomTabla.esquema && tabla eq nomTabla.nombre}">
									<tr class="destacadoTablas">
								</c:if>
							</c:if>
							<c:if test="${esquemaTabla == null || esquemaTabla == 'null' || esquemaTabla == ''}">
								<c:if test="${tabla eq nomTabla.nombre}">
									<tr class="destacadoTablas">
								</c:if>
							</c:if>
							<c:if test="${not tabla eq nomTabla.nombre}">
								<tr onmouseout="this.className=''" onmouseover="this.className='destacadoTablas'" class="">
							</c:if>
							<%-- <td class="izquierda"><a class="tablasTrabajo" href="fuentes.htm?accion=verDatos&id=${idfuente}&tabla=${nomTabla.nombre}&tipo=${tipofuente}" title="${nomTabla.nombre}">${fn:substring(nomTabla.nombre,0,20)}...</a></td> --%>
							<c:if test="${nomTabla.esquema != null && nomTabla.esquema != ''}">
								<td class="izquierda"><a class="tablasTrabajo" href="fuentes.htm?accion=verDatos&id=${idfuente}&esquema=${nomTabla.esquema}&tabla=${nomTabla.nombre}&tipo=${tipofuente}&cat=${esinterna}" title="${nomTabla.esquema}.${nomTabla.nombre}">
									${fn:substring(nomTabla.nombre,0,20)}
									<c:if test="${fn:length(nomTabla.nombre) > 20}" >
										...
									</c:if>
								</a></td>
							</c:if>
							<c:if test="${nomTabla.esquema == null || nomTabla.esquema == ''}">
								<td class="izquierda"><a class="tablasTrabajo" href="fuentes.htm?accion=verDatos&id=${idfuente}&esquema=${nomTabla.esquema}&tabla=${nomTabla.nombre}&tipo=${tipofuente}&cat=${esinterna}" title="${nomTabla.nombre}">
									${fn:substring(nomTabla.nombre,0,20)}
									<c:if test="${fn:length(nomTabla.nombre) > 20}" >
										...
									</c:if>
								</a></td>
							</c:if>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</c:if>
			</div>
			<c:if test="${tablaSeleccionada eq true}">
				<div id="pestanhas" class="fder" style="width: 75%;">
					<ul>
						<li><a href="#esquema"><spring:message code="jsp.fuente.esquema" /></a></li>
						<li><a href="#datos"><spring:message code="jsp.fuente.datos" /></a></li>
						<li><a id="mapaFuentes" href="#mapa"><spring:message code="jsp.fuente.mapa" /></a></li>
						<li><a id="detallesFuente" href="#detalles"><spring:message code="jsp.fuente.detalles" /></a></li>
					</ul>
					<div id="esquema" style="height: 425px;">
						<table id="tablaEsquema" class="tablasTrabajo" cellpadding="0" cellspacing="0" style="padding-bottom:2px;">
							<thead>
								<tr>
									<th class="tablaTitulo izquierda" scope="col"><spring:message code="jsp.fuente.atributo" /></th>
									<th class="tablaTitulo izquierda" scope="col"><spring:message code="jsp.fuente.tipo.dato" /></th>
									<th class="tablaTitulo izquierda" scope="col"><spring:message code="jsp.fuente.definicion" /></th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${esquema}" var="atributo">
									<tr onmouseout="this.className=''"
										onmouseover="this.className='destacadoTablas'" class="">
										<td class="izquierda">${atributo.nombreAcentos}</td>
										<td class="izquierda">${atributo.tipo.descripcion}</td>
										<c:if test="${atributo.definicion==null || atributo.definicion==''}">
											<td class="izquierda">&nbsp;</td>
										</c:if>
										<c:if test="${atributo.definicion!=null && atributo.definicion!=''}">
											<td class="izquierda">${atributo.definicion}</td>
										</c:if>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
					<div id="datos" style="height:425px;">
						<!-- DATOS -->
						<c:if test="${((numFilas==0 && tipofuente==3)|| numFilas<0)}">
							<p>
								<spring:message code="jsp.fuente.tabla.sin.valores"></spring:message>
							</p>
						</c:if>
						<c:if test="${numFilas>=0}">
							<table id="tablaDatos" class="tablasTrabajo" cellpadding="0" cellspacing="0" style="padding-bottom:2px;">
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
												<c:forEach items="${mapaNumerico}" var="columna">
													<c:if test="${columna.key==valoresFila.key }">
														<c:if test="${columna.value==true}">
															<td class="derecha">
														</c:if>
														<c:if test="${columna.value==false}">
															<td class="izquierda">
														</c:if>
													</c:if>
												</c:forEach>
												<c:if test="${valoresFila.value.valores[(status.count)-1].texto==null}">
													&nbsp;</td>
												</c:if>
												<c:if test="${valoresFila.value.valores[(status.count)-1].texto!=null}">
													${valoresFila.value.valores[(status.count)-1].texto}</td>
												</c:if>
											</c:forEach>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</c:if>
					</div>
					<div id="mapa" style="overflow: scroll">
						<c:if test="${empty mapa}">
							<p>
								<spring:message code="jsp.fuente.mapa.sin.valores"></spring:message>
							</p>
						</c:if>
						<c:if test="${not empty mapa}">
							<div id="map" style="width: 500px; height: 400px;"></div>
						</c:if>
					</div>
					<div id="detalles">
						<div class="contenedorarbol">
							<h2><spring:message code="jsp.fuente.lista.indicadores.asociados"/></h2>
							<c:if test="${empty mapaCategorias && empty mapaIndicadores}">
								<div><spring:message code="jsp.indicadores.lista.vacia"/></div>
							</c:if>
							<c:if test="${!empty mapaCategorias || !empty mapaIndicadores}">
								<c:if test="${usuarioAdministrador}">
									<table id="tablaIndicadores" class="tabla_jerarquia" border="0" cellspacing="0" cellpadding="0">
										<thead>
											<tr>
												<td id="tuplaCategoria0" class="cat">
													<a href="#" onclick="mostrarOcultarArbore('idcategoriapadre-0_tr', 'idcategoria0_img',true, false, false,false);">
														<img id="idcategoria0_img" src="images/folder_open.png" style="float: left;" />
													</a>
													<a href="#" class="enlaceTablaCategoria" style="padding-left: 8px;" idCategoria="0">
														<spring:message code="jsp.indicadores.indicadores.raiz"/>
													</a>
												</td>
											</tr>
										</thead>
									</table>
								</c:if>
								<c:if test="${!usuarioAdministrador}">
									<table id="tablaIndicadores" class="tabla_jerarquia" border="0" cellspacing="0" cellpadding="0">
										<thead id="tbCabecera">
											<tr id="trCabecera">
												<td id="tuplaCategoria0" class="cat" idCategoria="0">
													<a href="#" onclick="mostrarOcultarArbore('idcategoriapadre-0_tr', 'idcategoria0_img',true, false, false,false);">
														<img id='idcategoria0_img' src="images/folder_open.png" style="float: left;" />
													</a>
													<a href="#" class="enlaceTablaCategoria" style="padding-left: 8px;" idCategoria="0">
														<spring:message code="jsp.indicadores.indicadores.raiz"/>
													</a>
												</td>
											</tr>
										</thead>
										<tbody id="tbCupero">
										</tbody>
									</table>
								</c:if>
							</c:if>
						</div>
					</div>
				</div>
			</c:if>
		</div>
		<div class="clear"></div>
		<div id="mensaje_ayuda" class="linea" style="color:red;">
			<label id="msg"><spring:message  code="jsp.fuentes.ayuda"/></label>
		</div>
	</div>
	<div class="clear"></div>
</div>
<!--  MAPA para ShapeFile, WFS, GML y BD Espacial -->
<c:if test="${not empty mapa && (tipofuente == 1 || tipofuente == 2 || tipofuente == 3 || tipofuente == 6)}">
	<script type="text/javascript">
		$(document).ready(function() {
			$('#mapaFuentes').click(function() {
				$('#map').html("");
				init();
			});
		});
		
		var map, vectors, formats;
		var valorProyeccion= "EPSG:23029";
		
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
			};
			var select = new OpenLayers.Control.SelectFeature(vectors, options);
			map.addControl(select);
			select.activate();			
			
			var element, test;
			var features = [];
			var type = "wkt";
			
			<c:forEach var="contador" varStatus="status" begin="0" end="${numFilas}" step="1">
				<c:forEach items="${mapa}" var="valoresFila">
					element = "${valoresFila.value.valores[(status.count)-1].texto}";
					if ( element.length>0) {
						test = formats['in'][type].read(element);
						if ( test )
							features.push(test);
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
			vectors.addFeatures(features);
			map.zoomToExtent(bounds);
		}
			
	</script>
</c:if>
<script type="text/javascript">
var idCategoria=null;
var idIndicador=null;

$(document).ready(function() {
	<c:if test="${usuarioAdministrador}">
		InicializarTabla();
	</c:if>
	
	$('td,th').mouseout(function() {
		$(this).removeClass("destacadoTablas");
	});
	$('td,th').mouseover(function() {
		if ( $(this).html().length>0 )
			$(this).addClass("destacadoTablas");
	});
});

<c:if test="${usuarioAdministrador}">
	function InicializarTabla()
	{
		<c:forEach items="${mapaCategorias[0]}" var="categoria">
			dibujarFilaCategoria('tablaIndicadores', '0', '${categoria.id}', '${categoria.nombre}', 20);
		</c:forEach>
	
	 	<c:forEach items="${mapaIndicadores[0]}" var="indicador">
			dibujarFilaIndicador('tablaIndicadores', '0', '${indicador.id}', '${indicador.nombre}', 20);
		</c:forEach>
	}
	
	function dibujarFilaIndicador(idTabla, idCategoriaPadre, idIndicador, nombreIndicador, margenIzq)
	{
		var nombreClassFila = "idcategoriapadre-" + idCategoriaPadre + "_tr";
		var table = document.getElementById(idTabla);
	
		var rowCount = table.rows.length;
		var row = table.insertRow(rowCount);
		row.id ="idIndicador" + idIndicador;
		row.className = "idcategoriapadre-" + idCategoriaPadre + "_tr";
		
		var cell = row.insertCell(0);
		cell.id = "tuplaIndicador" + idIndicador;
		cell.className = "no_cat";
		cell.style.paddingLeft = margenIzq + "px";
		cell.innerHTML = "<a href='#' class='enlaceTablaIndicador' idindicador='" + idIndicador + "'><img id='idindicador" + idIndicador + "_img' src='images/file.png' alt='' style='float: left; padding-right: 8px;' /><span style='float: left; padding-left: 10px:'>" + nombreIndicador + "</span></a>";
	}
	
	function dibujarFilaCategoria(idTabla, idCategoriaPadre, idCategoria, nombreCategoria, margenIzq)
	{
		var nombreClassFila = "idcategoriapadre-" + idCategoriaPadre + "_tr";
		var funcionJs = "onclick=\"mostrarOcultarArbore('idcategoriapadre-" + idCategoria + "_tr', 'idcategoria" + idCategoria + "_img',true, false, false,false);\"";
		// Se dibuja la celda correspondiente a la categoría
		var table = document.getElementById(idTabla);
	
		var rowCount = table.rows.length;
		var row = table.insertRow(rowCount);
		row.id = "idCategoria" + idCategoria;
		row.className = nombreClassFila;
	
		var cell = row.insertCell(0);
		cell.id = "tuplaCategoria" + idCategoria;
		cell.className = "cat destacadotablas";
		cell.style.paddingLeft = margenIzq + "px";
		cell.innerHTML = "<a href='#' " + funcionJs +"><img id='idcategoria" + idCategoria + "_img' src='images/folder_open.png' style='float: left;' /></a><a href='#' class='enlaceTablaCategoria' style='padding-left: 8px;' idcategoria='" + idCategoria +"'>" + nombreCategoria + "</a>";
		
		// Se dibujan las filas correspondientes con sus Categorías hijo
		 <c:forEach items="${mapaCategorias}" var="entry">
		 	if (idCategoria == '${entry.key}')
		 	{
		 		<c:forEach items="${entry.value}" var="categoriaHija">
					dibujarFilaCategoria(idTabla, idCategoria, '${categoriaHija.id}', '${categoriaHija.nombre}', margenIzq + 20);
				</c:forEach>
			}
		</c:forEach>
		
		// Se dibujan las filas correspondientes con sus Indicadores hijo
		 <c:forEach items="${mapaIndicadores}" var="entry">
		 	if (idCategoria == '${entry.key}')
		 	{
		 		<c:forEach items="${entry.value}" var="indicadorHijo">
					dibujarFilaIndicador(idTabla, idCategoria, '${indicadorHijo.id}', '${indicadorHijo.nombre}', margenIzq + 20);
				</c:forEach>
			}
		</c:forEach>
	}
</c:if>

<c:if test="${!usuarioAdministrador}">
	var mapaIndicadores=${mapaIndicadores};
	var mapaCategorias=${mapaCategorias};

	function rellenarArbol(idCategoria,nivel){
		if(mapaCategorias[idCategoria]!=null && (mapaCategorias[idCategoria]).length !=0)
		{
			//si no existe ese nivel en el arbol se crea
			if ($("#tpCabecera"+idCategoria).length){
				$("#trCabecera").append("<th class='fizq' id='tpCabecera+"+idCategoria+"'></th>");
			}
			
			var listaCategorias = mapaCategorias[idCategoria];
			
			for(var i=0; i < listaCategorias.length; i++)
			{
				var idCategoriaPadre = 0;
				if (listaCategorias[i].idCategoriaPadre != null)
				{
					idCategoriaPadre = listaCategorias[i].idCategoriaPadre;
				}
				
				var filaId = "idCategoria" + listaCategorias[i].id;
				var filaClass = "idcategoriapadre-" + idCategoriaPadre + "_tr";
				var funcionJs = "onclick=\"mostrarOcultarArbore('idcategoriapadre-" + listaCategorias[i].id + "_tr', 'idcategoria" + listaCategorias[i].id + "_img',true, false, false,false);\"";
				var celdaId = "tuplaCategoria" + listaCategorias[i].id;
				var celdaClass = "cat destacadotablas";
				
				var htmlTbCupero = "";
				// fila
				htmlTbCupero += "<tr id='"+ filaId + "' class='" + filaClass + "'>";
				// Celda
				htmlTbCupero += "<td id='" + celdaId + "' class='" + celdaClass + "' style='padding-left: " + 20 * nivel + "px;'>";
				htmlTbCupero += "<a href='#' " + funcionJs +"><img id='idcategoria" + listaCategorias[i].id + "_img' src='images/folder_open.png' style='float: left;' /></a><a href='#' class='enlaceTablaCategoria' style='padding-left: 8px;' idcategoria='" + listaCategorias[i].id +"'>" + listaCategorias[i].nombre + "</a>";
	
				htmlTbCupero += "</tr>";
				$('#tbCupero').append(htmlTbCupero);
				
				rellenarArbol(listaCategorias[i].id, nivel+1);
			}
		}
		
		rellenaNodoIndicador(idCategoria, nivel);
	}
	
	function rellenaNodoIndicador(idCategoria,nivel){
		if(mapaIndicadores[idCategoria]!=null && (mapaIndicadores[idCategoria]).length !=0){
			//si no existe ese nivel en el arbol se crea
			if ($("#tpCabecera"+idCategoria).length)
			{
				$("#trCabecera").append("<th class='fizq' id='tpCabecera+"+idCategoria+"'></th>");
			}
			
			var indicador;
			var listaIndicadores=mapaIndicadores[idCategoria];
			
			for(var i=0; i<listaIndicadores.length; i++)
			{
				var idIndicador = listaIndicadores[i].id;
				var nombreIndicador = listaIndicadores[i].nombre;
				
				var trId = "idIndicador" + idIndicador;
				var trclass = "idcategoriapadre-" + idCategoria + "_tr";
				var tdId = "tuplaIndicador" + idIndicador;
				var tdClass = "no_cat";
				var htmlTbCupero = "";
				// Fila
				htmlTbCupero += "<tr id='" + trId + "' class='" + trclass + "'>";
				// Celda
				htmlTbCupero += "<td id='" + tdId + "' class='" + tdClass + "' style='padding-left: " + 20 * nivel + "px;'><a href='#' class='enlaceTablaIndicador' idindicador='" + idIndicador + "'><img id='idindicador" + idIndicador + "_img' src='images/file.png' alt='' style='float: left; padding-right: 8px;' /><span style='float: left; padding-left: 10px:'>" + nombreIndicador + "</span></a></td>";
	
				htmlTbCupero += "</tr>";
				$('#tbCupero').append(htmlTbCupero);
			}
		}
	}
	
	rellenarArbol(0, 1);
</c:if>
</script>