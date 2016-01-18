<%@page contentType="text/html" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script type="text/javascript" src="js/util/comun/cambioMenus.js"></script>
<script type="text/javascript" src="js/util/comun/funcions.js"></script>
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
       "aoColumns": [ { "sType": "string"},{ "sType": "string"},{ "sType": "date-eu"},{ "sType": "string"}],
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
				<li>
					<a href="?accion=verCatalogoSistema">
						<spring:message code="jsp.catalogo.titulo"/>
					</a>
				</li>
				<li class="on">
					<a href="fuentes.htm">
						<spring:message code="jsp.fuentes.fuentes"/>
					</a>
				</li>
			</ul>
		</div>
		<div class="areacentral">
		    <h2 class="inf15"><spring:message code="jsp.fuentes.listado.fuentes"/></h2>
		    
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
			
			<c:if test="${errorColumnas!=null}">
				<div class="controlAlertas">	
					<div class="ui-widget">
	            		<div class="ui-state-error ui-corner-all"> 
							<p>
								<span class="ui-icon ui-icon-alert fizq"></span>
					          	<spring:message code="${errorColumnas}" />
				         	</p>
				  	 	</div>
	         		</div>
         		</div>
         		<br/>     
			</c:if>
		    <!--  mensajes de exito -->
			<c:if test="${resultado != null}">
            <div class="controlAlertas">	
				<div class="ui-widget">
                     <c:if test="${resultado == 'exitoCrear'}">
                         <div class="success ui-corner-all"> 
							<p><span class="ui-icon ui-icon-circle-check fizq"></span>
                            <spring:message code="jsp.fuente.nueva.alta" />
                            </p>
                         </div>
                     </c:if>
                     <c:if test="${resultado == 'exitoGuardar'}">
                         <div class="success ui-corner-all"> 
							<p><span class="ui-icon ui-icon-circle-check fizq"></span>
                            <spring:message code="jsp.fuente.modificacion" />
                            </p>
                         </div>
                     </c:if>
                     <c:if test="${resultado == 'exitoBorrar'}">
                         <div class="success ui-corner-all"> 
							<p><span class="ui-icon ui-icon-circle-check fizq"></span>
                            <spring:message code="jsp.fuente.borrado.correcto" />
                            </p>
                         </div>
                     </c:if>
                     <c:if test="${resultado == 'fileError'}">
                         <div class="ui-state-error ui-corner-all"> 
							<p><span class="ui-icon ui-icon-circle-check fizq"></span>
                            <spring:message code="${fileError}" />
                            </p>
                         </div>
                     </c:if>
                 </div>
            </div>
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
            <!--  mensajes de error -->
			
			<spring:hasBindErrors name="fuenteDto">
			<div class="controlAlertas">	
				<div class="ui-widget">
					<div class="ui-state-error ui-corner-all"> 
						<p><span class="ui-icon ui-icon-alert fizq"></span> 
						<strong><spring:message code="global.formulario.erros.titulo"/></strong> 
						</p>
						<ul>
							${empty error.globalError? "" : "<li>"+error.defaultMessage+"</li>"}
							<c:forEach items="${errors.fieldErrors}" var="error">
					         	<li><form:errors path="${error.objectName}.${error.field}"/></li>
					        </c:forEach>
						</ul>
						<p></p>
					</div>
				</div>
			</div>
			</spring:hasBindErrors>
		    
			<div class="fizq ancho80" style="margin-top: 2px;">
			<c:if test="${empty listaFuentes}">
				<div><spring:message code="global.formulario.lista.vacia"></spring:message></div>
			</c:if>
			<c:if test="${!empty listaFuentes}">
				<form:form id="formulario" action="fuentes.htm">
					<table id="tablaDatos"  class="tablasTrabajo" cellpadding="0" cellspacing="0" style="padding-bottom:2px;">
				    	<thead>
					    	<tr>
					    		<th class="tablaTitulo izquierda" scope="col" ><spring:message code="jsp.fuentes.nombre"/></th>
					    		<th class="tablaTitulo izquierda" scope="col" ><spring:message code="jsp.fuentes.usuario"/></th>
					    		<th class="tablaTitulo izquierda" scope="col" ><spring:message code="jsp.fuentes.fecha.registro"/></th>
					    		<th class="tablaTitulo izquierda" scope="col" ><spring:message code="jsp.fuentes.info.conexion"/></th>
					    	</tr>
				    	</thead>
				    	<tbody>
						    <c:forEach items="${listaFuentes}" var="fuente">
							   <tr onmouseout="this.className='filaFuente'" onmouseover="this.className='destacadoTablas filaFuente'" class="filaFuente" data-idfuente="${fuente.id}" data-tipo="${fuente.tipo.id}"> 
							      <td class="izquierda" >${fuente.nombre}</td>
							      <td class="izquierda">${fuente.registradaPor.nombre}&nbsp;</td>
							      <td class="izquierda"><c:if test="${fuente.fechaRegistro eq null}">&nbsp;</c:if><c:if test="${fuente.fechaRegistro != null}"><fmt:formatDate value="${fuente.fechaRegistro}" pattern="dd/MM/yyyy"/></c:if></td>
							      <td class="izquierda">${fuente.infoConexion}&nbsp;</td>
							   </tr>
							</c:forEach>
						</tbody>
					</table>	
				</form:form>
			</c:if>
			</div>
			<div class="fder ancho17">
				<ul class="operaciones">
					<li><a href="#" onclick="fuentes.verTablasFuente();return false;"><spring:message code="elementos.jsp.boton.ver.datos"/></a></li>
					<li><a href="#" onclick="fuentes.modificar();return false;" ><spring:message code="elementos.jsp.boton.modificar"/></a></li>
					<li><a href="#" onclick="fuentes.borrarElemento();return false;" ><spring:message code="elementos.jsp.boton.borrar"/></a></li>
				</ul>
			</div>
		</div>
		
		<div class="clear"></div>
		<div class="botones_accion">
			<a href="fuentes.htm?accion=edita&id=0" class="boton izq10 sup10 fondo_blanco fder"><spring:message code="elementos.jsp.boton.registrar"/></a>			
		</div>	
	</div>	

<div id="dialogoModalEliminar" style="display: none;background-color: white;">
    <div class="ui-widget">
        <div style="padding: 0.2em 0.7em;" class=""> 
            <p>
                <span style="float: left; margin-right: 0.3em;" class=""></span>
                <spring:message code="elementos.jsp.confirmacion.borrar"/>
            </p>
        </div>
    </div>
</div>
<script type="text/javascript">
var idFuente;
var idTipoFuente;
$(document).ready(function(){
	$('.filaFuente').live('click',function() {
		$('.filaFuente td').removeClass("fondoGris");
		idFuente = $(this).attr("data-idfuente");
		idTipoFuente = $(this).attr("data-tipo");
		$(this).find("td").addClass("fondoGris");
	});
});

var fuentes = {
		borrarElemento: function() {
			if ( idFuente == null) {
				alert("<spring:message code="jsp.fuentes.seleccionar.fuente"/>");
				return false;
			} else {
				var dialogoModalEliminar = $("#dialogoModalEliminar").dialog({
				    autoOpen: false,
				    show: "blind",
				    hide: "explode",                    
				    width: 400,
				    height: 200,
				    modal: true,
				    resizable: false,
				    buttons: {
				        "Aceptar": function() {
				        	document.location.href="fuentes.htm?accion=borrar&id="+idFuente+"&tipo="+idTipoFuente;
				        	return true;
				        },
				        "Cancelar": function() {
				            $(this).dialog( "close" );
				            return false;
				        }
				    }            
				});
				$(dialogoModalEliminar).dialog("open");
			}
		},

		verTablasFuente: function() {
			if ( idFuente == null) {
				alert("<spring:message code="jsp.fuentes.seleccionar.fuente"/>");
				return false;
			} else {
				document.location.href="fuentes.htm?accion=verTablas&id="+idFuente+"&tipo="+idTipoFuente+"&cat='0'";;
			}
		},
		modificar : function()  {
			if ( idFuente == null) {
				alert("<spring:message code="jsp.fuentes.seleccionar.fuente"/>");
				return false;
			} else {
				document.location.href="fuentes.htm?accion=edita&id="+idFuente;
				return true;
			}
		}
	};
</script>