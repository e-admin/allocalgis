<!-- ***** INFORME DE TABLA ****************************************************************** -->

<div style="display: block;" id="informe-tabla-completo">
	<!-- DATOS -->
	<c:if test="${numFilas <= 0}">
		<p>
			<spring:message code="jsp.fuente.tabla.sin.valores"></spring:message>
		</p>
	</c:if>
	<c:if test="${fn:length(listaColumnasTabla) > 0}">
		<c:if test="${estilo==null}">
			<table id="tablaDatos" class="tablasTrabajo" cellpadding="0" cellspacing="0" style="padding-bottom:2px;">
		</c:if>
		<c:if test="${estilo!=null}">
			<table id="tablaDatos" class="tablasTrabajo" cellpadding="0" cellspacing="0" style="padding-bottom:2px;font-family:${estilo.tipoFuente} !important;font-size:${estilo.tamanhoFuente}px !important;">
		</c:if>
		<c:if test="${fn:length(listaColumnasTabla) > 0}">
			<p style="font-size: 16px; font-weight:bold; text-align:center">
				<spring:message code="jsp.indicador.tabla.valores"/>
			</p>
		</c:if>
		<thead>
			<tr>
				<c:forEach items="${listaColumnasTabla}" var="columna">
					<th class="tablaTitulo izquierda" scope="col">${columna.nombre}</th>
				</c:forEach>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="contador" varStatus="status" begin="1" end="${numFilas}" step="1">
				<tr onmouseout="this.className=''" onmouseover="this.className='destacadoTablas'" class="">
					<c:forEach items="${datos}" var="valoresFila">
						<c:if test="${valoresFila.value.valores[(status.count)-1].texto==null}">
							<td class="izquierda">&nbsp;</td>
						</c:if>
						<c:if test="${valoresFila.value.valores[(status.count)-1].texto!=null}">
							<c:forEach items="${listaColumnasTabla}" var="colum">
								<c:if test="${colum.nombre==valoresFila.key}">
									<c:if test="${(colum.columna==null && colum.indicadorExpresion!=null) || colum.columna.tipoAtributo=='VALORFDNUMERICO'}">
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
</div>

<script>

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
					<c:forEach items="${listaColumnasTabla}" var="colu" varStatus="cont">
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
						<c:if test="${cont.count!=fn:length(listaColumnasTabla) && (colu.columna.tipoAtributo!='VALORFDGEOGRAFICO' || (colu.columna.tipoAtributo=='VALORFDGEOGRAFICO' && colu.mostrar))}">
								,
						</c:if>
					</c:forEach>
					{
				        "mData": null,
				        "sDefaultContent": ""
				      }
    	            ],
       "bAutoWidth" : true,
       "sScrollX" : "",
       "sScrollY" : "",
       "sScrollXInner" : "",
       "bProcessing" : true,
       "bScrollCollapse" : true,
       "bPaginate": false,
       "sPaginationType": "full_numbers",
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