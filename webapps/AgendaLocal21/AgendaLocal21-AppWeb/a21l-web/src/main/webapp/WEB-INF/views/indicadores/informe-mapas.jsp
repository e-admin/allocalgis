<!-- ***** INFORME DE MAPAS ****************************************************************** -->
<div>

	<c:forEach items="${datos_mapas}" var="datos_mapa">
		
		<%@include file="informe_inicio_pagina.jsp" %>
		
			<table class="informe-diagrama">
				<thead>
					<tr><th>
						<div style="font-size: 16px; font-weight:bold; text-align:center">		
							<spring:message code="jsp.indicador.mapa.tematico"/>:&nbsp ${datos_mapa.parametro}
						</div>
					</th></tr>
				</thead>
				<tbody>
					<tr><td><div id="map_${datos_mapa.parametro}" class="informe-diagrama"></div></td></tr>
				</tbody>
			</table>
		
		<%@include file="informe_fin_pagina.jsp" %>
		
	</c:forEach>
</div>
<!-- ***** FIN DE INFORME DE MAPAS *********************************************************** -->

<script>

//--- CREACION DEL DIAGRAMA DE MAPAS -----------------------------------------------------------

<c:forEach items="${datos_mapas}" var="datos_mapa">

function generarMapa_${datos_mapa.parametro}() {

	var map, vectors, formats;
	var valorProyeccion= "EPSG:23029";
	
	var color;
	
	<c:forEach var="contador" varStatus="status" begin="0" end="${datos_mapa.numFilas}" step="1">
		<c:forEach items="${datos_mapa.datos}" var="valores" varStatus="cont">
			<c:forEach items="${datos_mapa.rangos}" var="valoresRango">
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
		
	
		var bounds = new OpenLayers.Bounds(
			475774.9406515219, 4629122,
			686857, 4849600
		);
		
		var opciones_mapa = {
			controls: [],
			maxExtent: bounds,
			maxResolution: 861.2421875,
			projection: valorProyeccion,
			units: 'm'
		};
		
		map = new OpenLayers.Map('map_${datos_mapa.parametro}',opciones_mapa);
				
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
		
		<c:forEach var="contador" varStatus="status" begin="0" end="${datos_mapa.numFilas}" step="1">
			<c:forEach items="${datos_mapa.mapa}" var="valoresFila">
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

$(document).ready(function() { generarMapa_${datos_mapa.parametro}(); });

</c:forEach>

</script>