<!-- ***** INFORME DE BARRAS ***************************************************************** -->
<div>
	<c:forEach items="${datos_barras}" var="datos_informe_barras">
		
		<div id="div_diagramas_barras_${datos_informe_barras.parametro}" >
			<c:forEach var="paginas" varStatus="pagina" begin="0" end="${datos_informe_barras.numPaginasDiagramas}" step="1">

				<%@include file="informe_inicio_pagina.jsp" %>
				
					<!-- Aplicacion de estilos -->
					<c:set var="estilo_a_aplicar" value="" />
					<c:if test="${estilo_barras!=null}">
						<c:set var="estilo_a_aplicar" value="font-family:${estilo_barras.tipoFuente} !important; font-size:${estilo_barras.tamanhoFuente}px !important;" />
					</c:if>
					
					<!-- Creacion de cada grafico del informe -->
					<table id="tabla_diagramas_barras_${datos_informe_barras.parametro}" cellspacing="0" cellpadding="0" class="informe-diagrama" style="${estilo_a_aplicar}" >
						<thead>
							<tr><th style="font-size:16px"><spring:message code="jsp.indicador.diagrama.barras"/>:&nbsp;${datos_informe_barras.parametro}</th></tr>
						</thead>
						<tbody>
							<tr><td><div id="td_diagramas_barras_${datos_informe_barras.parametro}_${pagina.count}" class="informe-diagrama"></div></td></tr>
						</tbody>
					</table>
					
				<%@include file="informe_fin_pagina.jsp" %>

			</c:forEach>
		</div>
		
	</c:forEach>
</div>
	
<!-- ***** FIN DE INFORME DE BARRAS ********************************************************** -->

<script>

//--- CREACION DEL DIAGRAMA DE BARRAS -----------------------------------------------------------

var ticks = [];

var opciones_barras = {
        seriesDefaults:{
            renderer:$.jqplot.BarRenderer,
            pointLabels: {show: true},
			rendererOptions:{
    			barWidth:25
			}
        },
        series:[
            <c:if test="${estilo_barras==null}">
            {label:$('#parametros').val(),color: '#00632E'}
            </c:if>
            <c:if test="${estilo_barras!=null}">
            {label:$('#parametros').val(),color: '${estilo_barras.color}'}
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

<c:forEach items="${datos_barras}" var="datos_diagrama_barras">

	<c:forEach var="paginas" varStatus="pagina" begin="0" end="${datos_diagrama_barras.numPaginasDiagramas}" step="1">
		var data_barras_${datos_diagrama_barras.parametro}_${pagina.count} = [
		<c:forEach var="contador" varStatus="status" begin="${pagina.count*datos_diagrama_barras.elementosPagina}" end="${pagina.count*datos_diagrama_barras.elementosPagina+datos_diagrama_barras.elementosPagina-1}" step="1">
			<c:forEach items="${datos_diagrama_barras.datos}" var="valoresFila" varStatus="cont">
				<c:if test="${not empty valoresFila.value.valores[(status.count)-1+((pagina.count-1)*datos_diagrama_barras.elementosPagina)].texto && cont.count%2!=0}">
					<c:if test="${cont.count%2!=0}">
						["${fn:trim(valoresFila.value.valores[(status.count)-1+((pagina.count-1)*datos_diagrama_barras.elementosPagina)].texto)}",
					</c:if>
				</c:if>
				<c:if test="${not empty valoresFila.value.valores[(status.count)-1+((pagina.count-1)*datos_diagrama_barras.elementosPagina)].texto && cont.count%2==0}">
					<c:if test="${cont.count%2==0}">				
						<c:if test="${empty valoresFila.value.valores[(status.count)-1+((pagina.count-1)*datos_diagrama_barras.elementosPagina)].texto || empty valoresFila.value.valores[(status.count)-1+((pagina.count-1)*datos_diagrama_barras.elementosPagina)].texto==null}">
							0],
						</c:if>
						<c:if test="${not empty valoresFila.value.valores[(status.count)-1+((pagina.count-1)*datos_diagrama_barras.elementosPagina)].texto}">
							${valoresFila.value.valores[(status.count)-1+((pagina.count-1)*datos_diagrama_barras.elementosPagina)].texto}],
						</c:if>
		    		</c:if>
		    	</c:if>
			</c:forEach>
		</c:forEach>
		];
		var plot${pagina.count} = $.jqplot('td_diagramas_barras_${datos_diagrama_barras.parametro}_${pagina.count}', [data_barras_${datos_diagrama_barras.parametro}_${pagina.count}], opciones_barras);
	
	</c:forEach>
</c:forEach>

</script>