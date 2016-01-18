<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<script src="js/util/comun/cambioMenus.js"></script>
<script src="js/util/datePicker/jquery.ui.datepicker-es.js"></script>
<script src="js/openlayers/OpenLayers.js"></script>

<script type="text/javascript" src="js/graph/jquery.jqplot.min.js"></script>
<script type="text/javascript" src="js/graph/jqplot.pieRenderer.min.js"></script>
<script type="text/javascript" src="js/graph/jqplot.barRenderer.min.js"></script>
<script type="text/javascript"
	src="js/graph/jqplot.categoryAxisRenderer.min.js"></script>
<script type="text/javascript" src="js/graph/jqplot.pointLabels.min.js"></script>
<script src="js/dataTables/js/jquery.dataTables.min.js"></script>

<link type="text/css" rel="stylesheet"
	href="js/dataTables/css/jquery.dataTables.css" />
<style>

/* ------ COMUN ----- */

body { margin: 0px !important; padding: 0px !important; }

.botonera { background-color: #00632E; border: 1px solid black; color: white; padding: 5%; width: 90%; }
.botonera a {
  color: white;
  font-weight: bold;
}
.botonera-inferior {
	margin-top: 50px;
} 
button {
  background-color: #96BD0D;
  border: 0 none;
  border-radius: 5px 5px 5px 5px;
  color: #00632E;
  cursor: pointer;
  display: block;
  float: right;
  font-size: 0.9em;
  padding: 6px 5px;
  text-align: center;
  text-decoration: none;
  text-transform: uppercase;
  width: 115px;
}

.numero-pagina { display: block; float: right; }
.pie-aviso-legal { display: block; float: left; }
.tabla-a4 > tbody > tr > td { vertical-align: top; }
.tabla-a4 { height: 100%; width: 100%; }
#cuerpo { 
	padding-top: 0px !important; 
}

<c:if test="${formato_impresion eq 'a4'}">
	/* --- A4 --- */
	.a4 { width: 21.0cm; height: 28cm; overflow: hidden; margin: 0px; }
	.informe-diagrama { width: 18cm; height: 18cm; margin-left: 1cm; border: 0px; margin-top: 1cm; }
	.informe-diagrama-sectores { width: 14cm; height: 14cm; margin-left: 1cm; border: 0px; margin-top: 1cm; }
	#cuerpo { width: 21.0cm; }
	.tabla-a4 > thead > tr > td { height: 2cm; }
	.tabla-a4 > tbody > tr > td { height: 24cm; }
	.tabla-a4 > tfoot > tr > td { height: 2cm; }
	<c:set var="filasPorPagina" value="30" />
	<c:if test="${estilo.tamanhoFuente ge 15.0}">
		<c:set var="filasPorPagina" value="25" />
	</c:if>
	<c:if test="${estilo.tamanhoFuente ge 19.0}">
		<c:set var="filasPorPagina" value="20" />
	</c:if>
	<c:if test="${estilo.tamanhoFuente ge 25.0}">
		<c:set var="filasPorPagina" value="15" />
	</c:if>
	<c:if test="${estilo.tamanhoFuente ge 33.0}">
		<c:set var="filasPorPagina" value="10" />
	</c:if>	
</c:if>

<c:if test="${formato_impresion eq 'a4a'}">
	/* --- A4 Apaisado--- */
	.a4 { width: 26cm; height: 17cm; overflow: hidden; margin: 0px; }
	.informe-diagrama { width: 18cm; height: 12cm; margin-top: 1cm; border: 0px; margin-left: 2cm; }
	.informe-diagrama-sectores { width: 16cm; height: 10cm; margin-top: 1cm; border: 0px; margin-left: 2cm; }
	#cuerpo { width: 26.0cm; }
	.tabla-a4 > thead > tr > td { height: 2cm; }
	.tabla-a4 > tbody > tr > td { height: 14cm; }
	.tabla-a4 > tfoot > tr > td { height: 1cm; }
	<c:set var="filasPorPagina" value="20" />
	<c:if test="${estilo.tamanhoFuente ge 12.0}">
		<c:set var="filasPorPagina" value="15" />
	</c:if>
	<c:if test="${estilo.tamanhoFuente ge 17.0}">
		<c:set var="filasPorPagina" value="10" />
	</c:if>
	<c:if test="${estilo.tamanhoFuente ge 27.0}">
		<c:set var="filasPorPagina" value="5" />
	</c:if>	
</c:if>

<c:if test="${formato_impresion eq 'a3'}">
	/* --- A3--- */
	.a4 { width: 29.7cm; height: 41cm; overflow: hidden; margin: 0px; }
	.informe-diagrama { width: 25cm; height: 29cm; margin-top: 2cm; border: 0px; margin-left: 1cm; }
	.informe-diagrama-sectores { width: 21cm; height: 25cm; margin-top: 2cm; border: 0px; margin-left: 1cm; }
	#cuerpo { width: 29.7cm; }
	.tabla-a4 > thead > tr > td { height: 2cm; }
	.tabla-a4 > tfoot > tr > td { height: 1cm; }
	.numero-pagina { margin-right: 80px; }
	<c:set var="filasPorPagina" value="65" />
	<c:if test="${estilo.tamanhoFuente ge 9.0}">
		<c:set var="filasPorPagina" value="60" />
	</c:if>
	<c:if test="${estilo.tamanhoFuente ge 11.0}">
		<c:set var="filasPorPagina" value="55" />
	</c:if>
	<c:if test="${estilo.tamanhoFuente ge 12.0}">
		<c:set var="filasPorPagina" value="50" />
	</c:if>
	<c:if test="${estilo.tamanhoFuente ge 14.0}">
		<c:set var="filasPorPagina" value="45" />
	</c:if>
	<c:if test="${estilo.tamanhoFuente ge 14.0}">
		<c:set var="filasPorPagina" value="40" />
	</c:if>
	<c:if test="${estilo.tamanhoFuente ge 18.0}">
		<c:set var="filasPorPagina" value="35" />
	</c:if>
	<c:if test="${estilo.tamanhoFuente ge 21.0}">
		<c:set var="filasPorPagina" value="30" />
	</c:if>	
	<c:if test="${estilo.tamanhoFuente ge 25.0}">
		<c:set var="filasPorPagina" value="25" />
	</c:if>
	<c:if test="${estilo.tamanhoFuente ge 31.0}">
		<c:set var="filasPorPagina" value="20" />
	</c:if>
</c:if>

<c:if test="${formato_impresion eq 'a3a'}">
	/* --- A3 Apaisado--- */
	.a4 { height: 28.5cm; width: 41cm; overflow: hidden; margin: 0px; }
	.informe-diagrama { height: 22cm; width: 27cm; margin-top: 1cm; border: 0px; margin-left: 3cm; }
	.informe-diagrama-sectores { height: 22cm; width: 27cm; margin-top: 1cm; border: 0px; margin-left: 3cm; }
	#cuerpo { width: 41cm; }
	.tabla-a4 > thead > tr > td { height: 2cm; }
	.tabla-a4 > tfoot > tr > td { height: 1cm; }
	.numero-pagina { margin-right: 80px; }
	<c:set var="filasPorPagina" value="40" />
	<c:if test="${estilo.tamanhoFuente ge 10.0}">
		<c:set var="filasPorPagina" value="35" />
	</c:if>
	<c:if test="${estilo.tamanhoFuente ge 12.0}">
		<c:set var="filasPorPagina" value="30" />
	</c:if>
	<c:if test="${estilo.tamanhoFuente ge 15.0}">
		<c:set var="filasPorPagina" value="25" />
	</c:if>	
	<c:if test="${estilo.tamanhoFuente ge 18.0}">
		<c:set var="filasPorPagina" value="20" />
	</c:if>
	<c:if test="${estilo.tamanhoFuente ge 25.0}">
		<c:set var="filasPorPagina" value="15" />
	</c:if>
	<c:if test="${estilo.tamanhoFuente ge 34.0}">
		<c:set var="filasPorPagina" value="10" />
	</c:if>
</c:if>

/* --- VISIONADO --- */
@media screen {
	.a4 {
	    background-color: white;
	    border: 1px solid black;
	    margin-top: 30px;
		-webkit-box-shadow: 8px 8px 0px rgba(0, 0, 0, 1);
		-moz-box-shadow:    8px 8px 0px rgba(0, 0, 0, 1);
		box-shadow:         8px 8px 0px rgba(0, 0, 0, 1);
	}
	body {
		background-color: #bbb;
	}
	.anexo {
		background-color: white;
		border: 1px solid black;
		box-shadow: 8px 8px 0 #000000;
		margin-top: 30px;
	}
}

/* --- IMPRESION --- */
@media print {
	.pagina { page-break-after: always; page-break-inside: avoid; }
	#tablaDatos_filter { display: none; }
	div.saltopagina { display: block; float: left; width: 100% height: 2px; page-break-after: always; }
	.botonera, .botonera-inferior { display: none; }
}

</style>

<div>

	<%@include file="informe-impresion.jsp" %>	
	
	<%@include file="informe-tabla.jsp" %>

<!-- 	
	<div>
		<div class="clear"></div><div class="saltopagina"></div>
	</div>
 -->
	
	
	<c:set var="paginasInformeTablaFloat" value="${numFilas / filasPorPagina}" />	
	<fmt:formatNumber value="${paginasInformeTablaFloat + ( 1 - (paginasInformeTablaFloat % 1)) % 1}" var="paginasInformeTabla" maxFractionDigits="0"/>	
	<c:set var="numero_pagina" value="${paginasInformeTabla}" />
	
	<c:set var="numero_paginas_total" value="${paginasInformeTabla}" />
	
	<c:forEach items="${datos_barras}" var="datos_informe_barras">
		<c:forEach var="paginas" varStatus="pagina" begin="0" end="${datos_informe_barras.numPaginasDiagramas}" step="1">
			<c:set var="numero_paginas_total" value="${numero_paginas_total + 1}" />
		</c:forEach>
	</c:forEach>	
	<c:set var="numero_paginas_total" value="${numero_paginas_total + numero_sectores}" />
	<c:set var="numero_paginas_total" value="${numero_paginas_total + numero_mapas}" />

	<%@include file="informe-tablas-con-cabeceras.jsp" %>
	
	<div class="clear"></div><div class="saltopagina"></div>
	
	<%@include file="informe-barras.jsp" %>
	
	<%@include file="informe-sectores.jsp" %>
	
	<%@include file="informe-mapas.jsp" %>
	
	<c:if test="${tiene_anexo}">
		<%@include file="informe_anexo.jsp" %>
	</c:if>
	
	<div class="botonera-inferior">
		<%@include file="informe-impresion.jsp" %>
	</div>
	
	<script type="text/javascript">
		if (${tiene_anexo}){
			<c:forEach items="${datos_sectores}" var="datos_sector">
				var legendTable = $('#div_diagramas_sectores_${datos_sector.parametro} .jqplot-table-legend').appendTo('#anexo_${datos_sector.parametro}');
				
			</c:forEach>
		}
	</script>
</div>