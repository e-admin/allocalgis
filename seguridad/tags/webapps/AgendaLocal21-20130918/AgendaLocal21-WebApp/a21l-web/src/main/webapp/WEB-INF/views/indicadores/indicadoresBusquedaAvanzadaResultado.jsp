<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script type="text/javascript" src="js/util/comun/cambioMenus.js"></script>
<script type="text/javascript" src="js/util/comun/funcions.js"></script>
	<div class="cuerpoprincipal">
		<div class="areacompleta">
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
          	
          	<div class="linea">
				<span class="fizq" style="position: relative;top: 5px;"><spring:message code="jsp.indicadores.indicadores.busquedaDirecta"/>&nbsp;</span>
				<input class="fizq" type="text" id="campoBusqDirect" value="" size="40" onKeyPress="return buscar(event)" />&nbsp;
				<a href="#" class="boton fizq" style="position: relative;right: -10px;" id="btnBusquedaDirecta"><spring:message code="jsp.indicadores.indicadores.buscar"/></a>
				<a href="?accion=editaBusquedaAvanzada" style="position: relative;top: 5px;right: -10px;"><spring:message code="jsp.indicadores.indicadores.busquedaAvanzada"/></a>
			</div>
			<br/>
			<div class="clear"></div>
			<br/>
			<h2><spring:message code="jsp.indicadores.indicadoresBusquedaAvanzadaResultado.parametrosEmpleados"/></h2>
      		<br/>
      		<div class="">
 					<div class="linea">
 						<label class="label fizq"><spring:message code="jsp.indicadores.indicadoresBusquedaAvanzada.nombreIndicador"/></label>
 						"<c:out value="${indicadorBusquedaAvanzadaDto.nombre}"></c:out>"
 					</div>
 					
 					<div class="linea">
 						<label class="label fizq"><spring:message code="jsp.indicadores.indicadoresBusquedaAvanzada.textoDescripcion"/></label>
 						"<c:out value="${indicadorBusquedaAvanzadaDto.descripcion}"></c:out>"
 					</div>
 					
 					<div class="linea">
 						<label class="label fizq"><spring:message code="jsp.indicadores.indicadoresBusquedaAvanzada.categoriaContenedora"/></label>
 						"<c:out value="${indicadorBusquedaAvanzadaDto.categoriaContenedora}"></c:out>"
 					</div>
 					
 					<div class="linea">
 						<label class="label fizq"><spring:message code="jsp.indicadores.indicadoresBusquedaAvanzada.usuarioCreador"/></label>
 						"<c:out value="${indicadorBusquedaAvanzadaDto.usuarioCreador}"></c:out>"
 					</div>
 					
 					<div class="clear"></div>
 					<br/>
 					<h2><spring:message code="jsp.indicadores.indicadoresBusquedaAvanzadaResultado.filtroMetadatos"/></h2>
 					<br/>
 					<c:forEach items="${indicadorBusquedaAvanzadaDto.valoresFiltroMD}" var="valor" varStatus="contador">
 						<div class="liena">
	 						<label class="">${listaNEMUtilizados[contador.count-1].expresion}</label>
	 						"<c:out value="${valor}"></c:out>"
 						</div>
 						<div class="clear"></div>
 					</c:forEach>
 			</div>
      		
			<div class="clear"></div>
      		
      		<br/>
      		<div class="clear"></div>
      		
      		<div>
      			<div class="fder" style="width: 17%;">
					<ul class="operaciones">
						<li><a href="indicadores.htm"><spring:message code="jsp.indicadores.indicadoresBusquedaAvanzadaResultado.volverIndicadores"/></a></li>
						<li><a id="visualizarIndicador" href="#"><spring:message code="jsp.indicadores.indicadoresBusquedaAvanzadaResultado.verIndicador"/></a></li>
						<li><a id="btnSenhalarIndicador" href="#"><spring:message code="jsp.indicadores.indicadoresBusquedaAvanzadaResultado.senhalarIndicador"/></a></li>
					</ul>
				</div>
	      		<fieldset style="width: 75%;">
					<div class="contenedorarbol">
						<table id="tablaIndicadores" class="tabla_jerarquia" border="0" cellspacing="0" cellpadding="0">
							<thead id="tbCabecera">
								<tr id="trCabecera">
									<td id="tuplaCategoria0" class="cat" idCategoria="0">
										<a href="#" onclick="mostrarOcultarArbore('idcategoriapadre-0_tr', 'idcategoria0_img',true, false, false);">
											<img id="idcategoria0_img" src="images/folder_open.png" style="float: left;" />
										</a>
										<a href="#" class="enlaceTablaCategoria" style="padding-left: 8px;" idCategoria="0">
											<spring:message code="jsp.indicadores.indicadores.raiz"/>
										</a>
									</td>
								</tr>
							</thead>
							<tbody id="tbCupero">
							</tbody>
						</table>
					</div>
				</fieldset>
			</div>
			<div class="clear"></div>
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
 
<script type="text/javascript">
	var idCategoria=null;
	var idIndicador=null;
	
	var mapaIndicadores=${mapaIndicadores};
	var mapaCategorias=${mapaCategorias};

$(document).ready(function() {
	$('#campoBusqDirect').focus();

	$(".enlaceTablaCategoria").live('click',function(){
		if(idCategoria!=null)
			$("#tuplaCategoria"+idCategoria).removeClass("fondoGris");
		if(idIndicador!=null){
			$("#tuplaIndicador"+idIndicador).removeClass("fondoGris");
		}
		
		idCategoria=$(this).attr("idcategoria");
		$("#tuplaCategoria"+idCategoria).addClass("fondoGris");
		return false;
	});
	
	$(".enlaceTablaIndicador").live('click',function(){
		if(idCategoria!=null)
			$("#tuplaCategoria"+idCategoria).removeClass("fondoGris");
		if(idIndicador!=null){
			$("#tuplaIndicador"+idIndicador).removeClass("fondoGris");
		}
		
		idIndicador=$(this).attr("idindicador");
		$("#tuplaIndicador"+idIndicador).addClass("fondoGris");
		return false;
	});
	
	$("#btnBusquedaDirecta").click(function(){
		window.location="?accion=busquedaDirecta&criterio="+$("#campoBusqDirect").val();
		return false;
	});
	
	$('td,th').mouseout(function() {
		$(this).removeClass("destacadoTablas");
	});
	$('td,th').mouseover(function() {
		if ( $(this).html().length>0 )
			$(this).addClass("destacadoTablas");
	});
});

$("#visualizarIndicador").click(function(){
	if(idIndicador==null){
		var dialogoModalSeleccionIndicador=$( "#dialogoModalSeleccionIndicador" ).dialog({
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
		$(dialogoModalSeleccionIndicador).dialog("open");
		return false;
	}
	document.location.href="?accion=visualizarIndicadorTabla&id="+idIndicador;
	return false;
});

$("#btnSenhalarIndicador").click(function(){
	if(idIndicador==null){
		var dialogoModalSeleccionIndicador=$( "#dialogoModalSeleccionIndicador" ).dialog({
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
		$(dialogoModalSeleccionIndicador).dialog("open");
		return false;
	}
	document.location.href="?idIndicadorSenhalado="+idIndicador;
	return false;
});

function buscar(e)
{
	if (e && e.keyCode == 13){
		window.location="?accion=busquedaDirecta&criterio="+$("#campoBusqDirect").val();
		return false;
	}
}

function rellenarArbol(idCategoria,nivel){
	if(mapaCategorias[idCategoria]!=null && (mapaCategorias[idCategoria]).length !=0)
	{
		//si no existe ese nivel en el arbol se crea
		if ($("#tpCabecera"+idCategoria).length){
			$("#trCabecera").append("<th class='fizq' id='tpCabecera+"+idCategoria+"'></th>");
		}
		
		var listaCategorias = mapaCategorias[idCategoria];
		
		for(var i=0; i < listaCategorias.length; i++)
		{
			var idCategoriaPadre = 0;
			if (listaCategorias[i].idCategoriaPadre != null)
			{
				idCategoriaPadre = listaCategorias[i].idCategoriaPadre;
			}
			
			var filaId = "idCategoria" + listaCategorias[i].id;
			var filaClass = "idcategoriapadre-" + idCategoriaPadre + "_tr";
			var funcionJs = "onclick=\"mostrarOcultarArbore('idcategoriapadre-" + listaCategorias[i].id + "_tr', 'idcategoria" + listaCategorias[i].id + "_img',true, false, false);\"";
			var celdaId = "tuplaCategoria" + listaCategorias[i].id;
			var celdaClass = "cat destacadotablas";
			
			var htmlTbCupero = "";
			// fila
			htmlTbCupero += "<tr id='"+ filaId + "' class='" + filaClass + "'>";
			// Celda
			htmlTbCupero += "<td id='" + celdaId + "' class='" + celdaClass + "' style='padding-left: " + 20 * nivel + "px;'>";
			htmlTbCupero += "<a href='#' " + funcionJs +"><img id='idcategoria" + listaCategorias[i].id + "_img' src='images/folder_open.png' style='float: left;' /></a><a href='#' class='enlaceTablaCategoria' style='padding-left: 8px;' idcategoria='" + listaCategorias[i].id +"'>" + listaCategorias[i].nombre + "</a>";

			htmlTbCupero += "</tr>";
			$('#tbCupero').append(htmlTbCupero);
			
			rellenarArbol(listaCategorias[i].id, nivel+1);
		}
	}
	
	rellenaNodoIndicador(idCategoria, nivel);
}

function rellenaNodoIndicador(idCategoria,nivel){
	if(mapaIndicadores[idCategoria]!=null && (mapaIndicadores[idCategoria]).length !=0){
		//si no existe ese nivel en el arbol se crea
		if ($("#tpCabecera"+idCategoria).length){
			$("#trCabecera").append("<th class='fizq' id='tpCabecera+"+idCategoria+"'></th>");
		}
		
		var indicador;
		var listaIndicadores=mapaIndicadores[idCategoria];
		
		for( var i=0;i<listaIndicadores.length;i++)
		{
			var idIndicador = listaIndicadores[i].id;
			var nombreIndicador = listaIndicadores[i].nombre;
			
			var trId = "idIndicador" + idIndicador;
			var trclass = "idcategoriapadre-" + idCategoria + "_tr";
			var tdId = "tuplaIndicador" + idIndicador;
			var tdClass = "no_cat";
			var htmlTbCupero = "";
			// Fila
			htmlTbCupero += "<tr id='" + trId + "' class='" + trclass + "'>";
			// Celda
			htmlTbCupero += "<td id='" + tdId + "' class='" + tdClass + "' style='padding-left: " + 20 * nivel + "px;'><a href='#' class='enlaceTablaIndicador' idindicador='" + idIndicador + "'><img id='idindicador" + idIndicador + "_img' src='images/file.png' alt='' style='float: left; padding-right: 8px;' /><span style='float: left; padding-left: 10px:'>" + nombreIndicador + "</span></a></td>";
			
			htmlTbCupero += "</tr>";
			$('#tbCupero').append(htmlTbCupero);
		}
	}
}

rellenarArbol(0, 1);
</script>