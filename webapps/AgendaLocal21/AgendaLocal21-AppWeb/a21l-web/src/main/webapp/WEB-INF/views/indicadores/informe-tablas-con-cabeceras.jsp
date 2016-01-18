<div id="informe-tabla-generado" style="display: block;"></div>

<script>

	function getEncabezado(numeroPaginaEncabezado) {
		var resultado =
			'<div class="pagina a4">'
				+ '<table class="tabla-a4" cellpadding="0" cellspacing="0">'
					+ '<thead>' 
						+ '<tr>'
							+ '<td>'
								+ '<div id="cabecera">'
									+ '<div class="conjuntocabecerapdf">'
										+ '<div class="logoscomplementos"> <img src="images/loured.png" alt="Loured" width="114" height="33" /> <img src="images/planavanza.png" alt="Plan Avanza2" width="169" height="33" /> </div>'
										+ '<h1><a href="indicadores.htm"><img src="images/logo.png" alt="Agenda Local 21" width="232" height="80" /></a></h1>'
									+ '</div>'
								+ '</div>'
							+ '</td>'
						+ '</tr>'
					+ '</thead>'
					+ '<tfoot>'
						+ '<tr>'
							+ '<td>'
								+ '<div class="pie-aviso-legal">Aviso Legal &copy; Agenda Local 21. Información mantenida y publicada por Proyecto LOURED.</div>'
								+ '<div class="numero-pagina">P&aacute;gina ' + numeroPaginaEncabezado + ' / ${numero_paginas_total}</div>'
							+ '</td>'
						+ '</tr>'
					+ '</tfoot>'
					+ '<tbody>'
						+ '<tr>'
						+ '<td>';
		return resultado;
	}

	<c:if test="${estilo==null}">
		var cabeceraTabla = '<table class="tablasTrabajo" cellpadding="0" cellspacing="0" style="padding-bottom:2px;">' + $("#tablaDatos > thead").html();
	</c:if>
	<c:if test="${estilo!=null}">
		var cabeceraTabla = '<table class="tablasTrabajo" cellpadding="0" cellspacing="0" style="padding-bottom:2px; font-family:${estilo.tipoFuente} !important; font-size:${estilo.tamanhoFuente}px !important;">' + $("#tablaDatos > thead").html();
	</c:if>

	var numeroPaginas = ${paginasInformeTabla};
	var filasPorPagina = ${filasPorPagina};

	var filas = $("#tablaDatos>tbody").children();
	var salida = $("#informe-tabla-generado");
	var i = 0;
	var htmlGenerado = "";
	
	var npagina = 1;
	var pie =
						'</td>'
					+ '</tr>'
				+ '</tbody>'
			+ '</table>'
		+ '</div>';
	
	filas.each(function () {
		if (i == 0) {
			htmlGenerado += getEncabezado(npagina++) + cabeceraTabla;
		}
		var hijo = $(this);
		htmlGenerado += "<tr>" + hijo.html() + "</tr>";
		i++;
		if (i == filasPorPagina) {
			htmlGenerado += "</table>" + pie;
			i = 0;
		}
	});
	if (i != 0) {
		htmlGenerado += "</table>" + pie;
	}
	salida.html(htmlGenerado);
	$("#informe-tabla-completo").hide();
</script>