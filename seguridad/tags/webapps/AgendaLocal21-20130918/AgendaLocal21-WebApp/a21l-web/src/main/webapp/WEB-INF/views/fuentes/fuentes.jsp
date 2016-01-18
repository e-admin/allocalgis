<%@page contentType="text/html" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script type="text/javascript" src="js/util/comun/cambioMenus.js"></script>
<script type="text/javascript" src="js/util/comun/funcions.js"></script>
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
		    
			<div class="fizq ancho80">
			<c:if test="${empty listaFuentes}">
				<div><spring:message code="global.formulario.lista.vacia"></spring:message></div>
			</c:if>
			<c:if test="${!empty listaFuentes}">
				<form:form id="formulario" action="fuentes.htm">
					<table class="tablasTrabajo" cellpadding="0" cellspacing="0">
				    	<thead>
					    	<tr style="display:block;">
					    		<th class="tablaTitulo izquierda" scope="col" style="width:20%;"><spring:message code="jsp.fuentes.nombre"/></th>
					    		<th class="tablaTitulo" scope="col" style="width:20%;"><spring:message code="jsp.fuentes.usuario"/></th>
					    		<th class="tablaTitulo" scope="col" style="width:20%;"><spring:message code="jsp.fuentes.fecha.registro"/></th>
					    		<th class="tablaTitulo" scope="col" style="width:40%;"><spring:message code="jsp.fuentes.info.conexion"/></th>
					    	</tr>
				    	</thead>
				    	<tbody style="height: 500px; overflow: scroll;display:block">
						    <c:forEach items="${listaFuentes}" var="fuente">
							   <tr onmouseout="this.className='filaFuente'" onmouseover="this.className='destacadoTablas filaFuente'" class="filaFuente" data-idfuente="${fuente.id}" data-tipo="${fuente.tipo.id}"> 
							      <td class="izquierda" style="width:20%;">${fuente.nombre}</td>
							      <td class="tablasDetalle" style="width:20%;">${fuente.registradaPor.nombre}</td>
							      <td class="tablasDetalle" style="width:20%;"><c:if test="${fuente.fechaRegistro eq null}">&nbsp;</c:if><c:if test="${fuente.fechaRegistro != null}"><fmt:formatDate value="${fuente.fechaRegistro}" pattern="dd/MM/yyyy"/></c:if></td>
							      <td class="tablasDetalleFuentes" style="width:40%;">${fuente.infoConexion}</td>
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