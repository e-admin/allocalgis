<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script src="js/util/comun/cambioMenus.js"></script>  
<div class="cuerpoprincipal">
	<div class="areamenu">
		<ul class="menusecundario">
			<li class="on">
				<a href="publicacionWeb.htm">
					<spring:message code="jsp.publicacion.publicacion.web"/>
				</a>
			</li>
			<li><a href="publicacionWeb.htm?accion=listaIndicadoresPendientes"><spring:message code="jsp.publicacion.lista.indicadores.pendientes"/></a></li>
		</ul>
	</div>
	<div class="areacentral">
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
        <c:if test="${resultadoOperacionError!=null}">
				<div class="controlAlertas">	
					<div class="ui-widget">
	            		<div class="ui-state-error ui-corner-all"> 
							<p>
								<span class="ui-icon ui-icon-alert fizq"></span>
					          	<spring:message code="${resultadoOperacionError}" />
				         	</p>
				  	 	</div>
	         		</div>
         		</div>
		</c:if>
		<c:if test="${resultadoOperacion!=null}">
				<div class="controlAlertas">	
					<div class="ui-widget">
	            		<div class="success ui-corner-all"> 
							<p>
								<span class="ui-icon ui-icon-alert fizq"></span>
					          	<spring:message code="${resultadoOperacion}" />
				         	</p>
				  	 	</div>
	         		</div>
         		</div>
		</c:if>
     	
     	<div class="linea">
			<c:if test="${!estadoPublicacion}">
			<a href="publicacionWeb.htm?accion=habilitarPublicacionWeb" class="boton fizq" style="margin-bottom:10px;width:350px;" id="btnHabilitar"><spring:message code="jsp.publicacion.habilitar.publicacion"/></a>
			</c:if>
			<c:if test="${estadoPublicacion}">
			<a href="publicacionWeb.htm?accion=deshabilitarPublicacionWeb" class="boton fizq" style="margin-bottom:10px;width:350px;" id="btnDeshabilitar"><spring:message code="jsp.publicacion.deshabilitar.publicacion"/></a>
			</c:if>
		</div>
		<br/>
		<div class="clear"></div>
		
		<h2><spring:message code="jsp.publicacion.lista.indicadores.publicados"/></h2>
     	<br/><br/>
		<div class="contenedorarbol ancho80">
			<table class="tabla_jerarquia" border="0" cellspacing="0" cellpadding="0">
				<thead>
					<tr>
						<th style="width: 10%;" id="tuplaCategoria0"><a href="#" class="enlaceTablaCategoria" idCategoria="0"><spring:message code="jsp.indicadores.indicadores.raiz"/></a></th>
						<th style="width: 18%;"></th>
						<th style="width: 18%;"></th>
						<th style="width: 18%;"></th>
						<th style="width: 18%;"></th>
						<th style="width: 18%;"></th>						
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${listaIndicadoresRaiz}" var="indicadorRaiz">
						<tr>
							<td></td>
							<td id="tuplaIndicador${indicadorRaiz.id}" class="no_cat"><a idIndicador="${indicadorRaiz.id}" class="enlaceTablaIndicador" href="#">${indicadorRaiz.nombre}</a></td>
							<td></td>
							<td></td>
							<td><a target="_blank" href="${indicadorRaiz.urlPublicacion}">${indicadorRaiz.urlPublicacionCorta}</a></td>
							<td>(${indicadorRaiz.numUsuariosPublicacion}) ${indicadorRaiz.loginUltimaPeticion}</td>
						</tr>
					</c:forEach>
					<c:forEach items="${listaCategoriasRaiz}" var="categoriaRaiz">
						<tr>
							<td></td>
							<td id="tuplaCategoria${categoriaRaiz.id}"><a href="#" class="enlaceTablaCategoria" idCategoria="${categoriaRaiz.id}" >${categoriaRaiz.nombre}</a></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
						</tr>
						<c:forEach items="${mapaIndicadoresSegundoNivel[categoriaRaiz.id]}" var="indicadorSL">
							<tr>
								<td></td>
								<td></td>
								<td id="tuplaIndicador${indicadorSL.id}" class="no_cat"><a idIndicador="${indicadorSL.id}" class="enlaceTablaIndicador" href="#">${indicadorSL.nombre}</a></td>
								<td></td>
								<td><a target="_blank" href="${indicadorSL.urlPublicacion}">${indicadorSL.urlPublicacionCorta}</a></td>
								<td>(${indicadorSL.numUsuariosPublicacion}) ${indicadorSL.loginUltimaPeticion}</td>
							</tr>
						</c:forEach>
						<c:forEach items="${mapaCategoriasSegundoNivel[categoriaRaiz.id]}" var="categoriaSL">
							<tr>
								<td></td>
								<td></td>
								<td id="tuplaCategoria${categoriaSL.id}" ><a href="#" class="enlaceTablaCategoria" idCategoria="${categoriaSL.id}">${categoriaSL.nombre}</a></td>
								<td></td>
								<td></td>
								<td></td>
							</tr>
							<c:forEach items="${mapaIndicadoresTercerNivel[categoriaSL.id]}" var="indicadorTL">
							<tr>
								<td></td>
								<td></td>
								<td></td>
								<td id="tuplaIndicador${indicadorTL.id}" class="no_cat"><a idIndicador="${indicadorTL.id}" class="enlaceTablaIndicador" href="#">${indicadorTL.nombre}</a></td>
								<td><a target="_blank" href="${indicadorTL.urlPublicacion}">${indicadorTL.urlPublicacionCorta}</a></td>
								<td>(${indicadorTL.numUsuariosPublicacion}) ${indicadorTL.loginUltimaPeticion}</td>
							</tr>
							</c:forEach>
						</c:forEach>
					</c:forEach>
				</tbody>
			
			</table>
		</div>
		
		<div class="operacionesarbol20 operacionesarbol">
			<ul>
				<li><a id="retirarIndicador" href="#"><spring:message code="jsp.publicacion.retirar.indicador"/></a></li>
			</ul>
		</div>
	
	</div>
	<div class="clear"></div>
</div>

<div id="dialogoModalSeleccionIndicador" style="display: none;background-color: white;">
     <div class="ui-widget">
         <div style="padding: 0.2em 0.7em;" class=""> 
             <p>
                 <span style="float: left; margin-right: 0.3em;" class=""></span>
                 <spring:message code="jsp.indicadores.indicadores.necesarioSeleccionarIndicador"/>
             </p>
         </div>
     </div>
 </div>
 
 <div id="dialogoModalSeleccionElemento" style="display: none;background-color: white;">
     <div class="ui-widget">
         <div style="padding: 0.2em 0.7em;" class=""> 
             <p>
                 <span style="float: left; margin-right: 0.3em;" class=""></span>
                 <spring:message code="jsp.indicadores.indicadores.necesarioSeleccionarElemento"/>
             </p>
         </div>
     </div>
 </div>

<script type="text/javascript">
	var idIndicador=null;
	$(".enlaceTablaIndicador").click(function(){
		if(idIndicador!=null){
			$("#tuplaIndicador"+idIndicador).removeClass("fondoGris");
		}
		
		idIndicador=$(this).attr("idIndicador");
		$("#tuplaIndicador"+idIndicador).addClass("fondoGris");
		return false;
	});
	
	$("#retirarIndicador").click(function(){  
		if(idIndicador==null){
			var dialogoModalSeleccionElemento=$( "#dialogoModalSeleccionElemento" ).dialog({
		        autoOpen: false,
		        show: "blind",
		        hide: "explode",                    
		        width: 400,
		        height: 200,
		        modal: true,
		        resizable: false,
		        buttons: {
		            "Aceptar": function() {
		                $( this ).dialog( "close" );
		                return false;
		                
		            }
		        }            
		    });  
			$(dialogoModalSeleccionElemento).dialog("open");
			return false;
		}
		document.location.href= "publicacionWeb.htm?accion=retirarIndicadorPublicacionWeb&id="+idIndicador;
		return false;
	});

$(document).ready(function() {
	$('td,th').mouseout(function() {
		$(this).removeClass("destacadoTablas");
	});
	$('td,th').mouseover(function() {
		if ( $(this).html().length>0 )
			$(this).addClass("destacadoTablas");
	});
});
</script>

