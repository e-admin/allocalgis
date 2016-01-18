<!-- ***** INFORME DE SECTORES *************************************************************** -->
<div>
		
	<c:forEach items="${datos_sectores}" var="datos_sector">
	
		<%@include file="informe_inicio_pagina.jsp" %>
		
		<!-- Aplicacion de estilos -->
		<c:set var="estilo_a_aplicar" value="" />
		<c:if test="${estilo_sectores!=null}">
			<!-- <c:set var="estilo_a_aplicar" value="width: ${estilo_sectores.diametro}px; height: ${estilo_sectores.diametro}px;font-family:${estilo_sectores.tipoFuente} !important;font-size:${estilo_sectores.tamanhoFuente}px !important;" /> -->
			<c:set var="estilo_a_aplicar" value="font-family:${estilo_sectores.tipoFuente} !important;font-size:${estilo_sectores.tamanhoFuente}px !important;" />
		</c:if>

		<!-- Creacion de cada grafico del informe -->
		<div id="div_diagramas_sectores_${datos_sector.parametro}">
			
			<table id="tabla_diagramas_sectores_${datos_sector.parametro}" class="informe-diagrama-sectores">
				<thead>
					<tr><th style="font-size:16px"><spring:message code="jsp.indicador.diagrama.sectores"/>:&nbsp ${datos_sector.parametro}</th></tr>
				</thead>
				<tbody>
					<tr><td><div id="td_diagramas_sectores_${datos_sector.parametro}" style="text-align:center;${estilo_a_aplicar}" class="informe-diagrama-sectores"><div id="tdAnexo"></div></div></td></tr>
				</tbody>
			</table>
			<c:if test="${tiene_anexo}">
				<p style="color:red;padding-left: 10%;">*&nbsp;<spring:message code="jsp.indicador.mensaje.anexo"/></p>
			</c:if>
			
		</div>
			
		<%@include file="informe_fin_pagina.jsp" %>
		
	</c:forEach>
</div>


<script>

// --- CREACION DEL DIAGRAMA DE SECTORES -----------------------------------------------------------


<c:if test="${estilo_sectores==null}">
var colores = "${colores_defecto}";
</c:if>
<c:if test="${estilo_sectores!=null}">
var colores = "${estilo_sectores.colores}";
</c:if>
var listaColores = colores.split("||");
var listaFinalColores = [];
for ( var i = 0 ; i < listaColores.length ; i++ ) {
	if ( listaColores[i]!="") {
		listaFinalColores.push(listaColores[i]);
	}
}

var opciones_sectores = {
	seriesColors: listaFinalColores,
	legend: { show:true, location: 'ne',placement:'outside'  },
	seriesDefaults: {
		renderer: jQuery.jqplot.PieRenderer,
		rendererOptions: {
			showDataLabels: true
		}
	}
};

<c:forEach items="${datos_sectores}" var="datos_sector">
var dataSectores_${datos_sector.parametro} = [
	<c:set var="izquierda" value="false"/>
	<c:forEach var="paginas" varStatus="pagina" begin="0" end="${datos_sector.numPaginasDiagramas}" step="1">
		<c:forEach var="contador" varStatus="status" begin="${pagina.count*elementosPagina}" end="${pagina.count*elementosPagina+elementosPagina-1}" step="1">
			<c:forEach items="${datos_sector.datos}" var="valoresFila" varStatus="cont">
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

var plot = $.jqplot('td_diagramas_sectores_${datos_sector.parametro}', [dataSectores_${datos_sector.parametro}], opciones_sectores);
if (${tiene_anexo}){
	var legendTable = $($('#div_diagramas_sectores_${datos_sector.parametro} .jqplot-table-legend')[0]);
}else{
	var legendTable = $($('#div_diagramas_sectores_${datos_sector.parametro} .jqplot-table-legend')[0]);    
	legendTable.css('display','block');
	legendTable.css('z-index',100);
	legendTable.css('height','80%');
	legendTable.css('width','150px');
	legendTable.css('overflow-y','scroll');
}

</c:forEach>

</script>