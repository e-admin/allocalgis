<%@page contentType="text/html" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script src="js/util/comun/cambioMenus.js"></script>
<div class="cuerpoprincipal">
	 <div class="areamenu">
			<ul class="menusecundario">				
				<li class="on">
				<a href="fuentes.htm?accion=verCatalogoSistema"><spring:message code="jsp.catalogo.titulo"/></a></li>
				<li><a href="fuentes.htm"><spring:message code="jsp.fuentes.fuentes"/></a></li>
			</ul>
		</div>
	
	<div class="areacentral">		
	 <h2 class="inf15"><spring:message code="jsp.fuentes.catalogoSistema.titulo"/></h2>
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
	 	
			<c:if test="${resultado != null}">
				<div class="controlAlertas">	
					<div class="ui-widget">
						<c:if test="${resultado == 'exitoBorrar'}">
	                         <div class="success ui-corner-all"> 
								<p><span class="ui-icon ui-icon-circle-check fizq"></span>
	                            <spring:message code="jsp.fuente.borrado.correcto" />
	                            </p>
	                         </div>
	                     </c:if>
	                     <c:if test="${resultado == 'fuenteCatalogo'}">
	                         <div class="ui-state-error ui-corner-all"> 
								<p><span class="ui-icon ui-icon-circle-check fizq"></span>
	                            <spring:message code="${fuenteCatalogo}" />
	                            </p>
	                         </div>
	                     </c:if>
					</div>
				</div>
			</c:if>
	 
           <!--  error Carga del catalogo -->
		    <c:if test="${errorCarga!=null}">
				<div class="controlAlertas">	
					<div class="ui-widget">
	            		<div class="ui-state-error ui-corner-all"> 
							<p>
							  <span class="ui-icon ui-icon-alert fizq"></span>
					          <spring:message code="jsp.fuentes.catalogoSistema.catalogoNoInstalado"/>
				         	</p>
				  	 	</div>
	         		</div>
         		</div>
         		<br/>     
			</c:if>
			
		    <!--  mensajes de error parametros incorrectors -->
		<c:if test="${errorParamCatalogo != null}">
			<div class="controlAlertas">
				<div class="ui-widget">
					<div class="ui-state-error ui-corner-all">
						<p>
							<span class="ui-icon ui-icon-circle-check fizq"></span>
							<spring:message
								code="jsp.fuentes.catalogoSistema.parametrosIncorrectos" />
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
		    
		       <!--  rejilla -->			
			<div class="fizq ancho80">
			<c:if test="${empty listaFuentesInt}">
				<div><spring:message code="global.formulario.lista.vacia"></spring:message></div>
			</c:if>
			<c:if test="${!empty listaFuentesInt}">
				<form:form id="formulario" action="?">
					<table class="tablasTrabajo" cellpadding="0" cellspacing="0">
						<tbody>
							<tr>
								<th class="tablaTitulo izquierda" scope="col"><spring:message
										code="jsp.fuentes.nombre" /></th>
								<th class="tablaTitulo" scope="col"><spring:message
										code="jsp.fuentes.usuario" /></th>
								<th class="tablaTitulo" scope="col"><spring:message
										code="jsp.fuentes.fecha.registro" /></th>
								<th class="tablaTitulo" scope="col"><spring:message
										code="jsp.fuentes.info.conexion" /></th>
							</tr>
							<c:forEach items="${listaFuentesInt}" var="fuente">
								<tr onmouseout="this.className='filaFuente'"
									onmouseover="this.className='destacadoTablas filaFuente'"
									class="filaFuente" data-idfuente="${fuente.id}"
									data-tipo="${fuente.tipo.id}">
									<td class="izquierda">${fuente.nombre}</td>
									<td class="tablasDetalle">${fuente.registradaPor.nombre}</td>
									<td class="tablasDetalle"><c:if
											test="${fuente.fechaRegistro eq null}">&nbsp;</c:if>
										<c:if test="${fuente.fechaRegistro != null}">
											<fmt:formatDate value="${fuente.fechaRegistro}"
												pattern="dd/MM/yyyy" />
										</c:if></td>
									<td class="tablasDetalleFuentes">${fuente.infoConexion}</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</form:form>
			</c:if>
			</div>
			
			     <!--  operaciones -->	
			<div class="fder ancho17">
				<c:if test="${!empty listaFuentesInt}">
				<ul class="operaciones">
					<li><a href="#" onclick="fuentes.verTablasFuente();return false;"><spring:message code="elementos.jsp.boton.ver.datos"/></a></li>
					<li><a href="#" onclick="fuentes.borrarElemento();return false;" ><spring:message code="elementos.jsp.boton.borrar"/></a></li>
				</ul>
				</c:if>
			</div>
		</div>
		
		<div class="clear"></div>
		<div class="botones_accion">
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
	$('.filaFuente').click(function() {
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
				        	document.location.href="fuentes.htm?accion=borrarInterno&id="+idFuente+"&tipo="+idTipoFuente;
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
				document.location.href="fuentes.htm?accion=verTablas&id="+idFuente+"&tipo="+idTipoFuente+"&cat='1'";
			}
		}
};
</script>


