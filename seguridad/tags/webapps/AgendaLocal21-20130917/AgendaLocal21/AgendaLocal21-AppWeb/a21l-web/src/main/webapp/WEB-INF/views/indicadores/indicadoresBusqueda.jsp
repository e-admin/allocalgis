<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
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
					<a href="indicadores.htm">
						<spring:message code="jsp.indicadores.indicadores.jerarquiaIndicadores"/>
					</a>
				</li>
				<c:if test="${!usuarioInvitado && usuarioAdministrador}">
					<li>
						<a href="indicadores.htm?accion=listaIndicadoresPublicos">
							<spring:message code="jsp.indicadores.indicadores.indicadoresPublicos"/>
						</a>
					</li>
					<li>
						<a href="publicacionWeb.htm?accion=listaIndicadoresPendientes">
							<spring:message code="jsp.indicadores.indicadores.indicadoresPendientesPublicos"/>
						</a>
					</li>
				</c:if>
			</ul>
		</div>
		<div class="areacentral">
			<c:if test="${exitoBorrar}">
	            <div class="controlAlertas">	
					<div class="ui-widget">
	            		<div class="success ui-corner-all"> 
							<p>
								<span class="ui-icon ui-icon-circle-check fizq"></span>
					          	<spring:message code="jsp.indicadores.indicadores.exito.borrar" />
				         	</p>
				  	 	</div>
	         		</div>
         		</div>
         		<br/>     
          	</c:if>
          	
          	<c:if test="${errorBorrarCategoria}">
	            <div class="controlAlertas">	
					<div class="ui-widget">
	            		<div class="ui-state-error ui-corner-all"> 
							<p>
								<span class="ui-icon ui-icon-circle-check fizq"></span>
					          	<spring:message code="jsp.indicadores.indicadores.error.borrar.categoria" />
				         	</p>
				  	 	</div>
	         		</div>
         		</div>
         		<br/>     
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
          	
          	<div class="linea">
				<span class="fizq" style="position: relative;top: 5px;"><spring:message code="jsp.indicadores.indicadores.busquedaDirecta"/>&nbsp;</span>
				<input class="fizq" type="text" id="campoBusqDirect" value="${criterio}" size="40" />&nbsp;
				<a href="#" class="boton fizq" style="position: relative;right: -10px;" id="btnBusquedaDirecta"><spring:message code="jsp.indicadores.indicadores.buscar"/></a>
				<a href="?accion=editaBusquedaAvanzada" style="position: relative;top: 5px;right: -10px;"><spring:message code="jsp.indicadores.indicadores.busquedaAvanzada"/></a>
			</div>
			<br/>
			<div class="clear"></div>
      
			<div class="contenedorarbol ancho70">
				<table id="tablaIndicadores" class="tabla_jerarquia" border="0" cellspacing="0" cellpadding="0">
					<thead id="tbCabecera">
						<tr id="trCabecera">
							<td id="tuplaCategoria0" class="cat" idCategoria="0">
								<a href="#" onclick="mostrarOcultarArbore('idcategoriapadre-0_tr', 'idcategoria0_img',true, false, false,false);">
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
			
			<div class="operacionesarbol">
				<ul>
					<li><a id="visualizarIndicador" href="#"><spring:message code="jsp.indicadores.indicadores.visualizar"/></a></li>
					<c:if test="${!usuarioInvitado}">
						<li><a href="#" id="btnEditarIndicador"><spring:message code="jsp.indicadores.indicadores.modificar"/></a></li>
						<li><a href="#" id="btnBorrar"><spring:message code="jsp.indicadores.indicadores.borrar"/></a></li>
						<li><a href="#"><spring:message code="jsp.indicadores.indicadores.publicarEnWeb"/></a></li>
						<li><a href="#" id="btnCrearIndicador"><spring:message code="jsp.indicadores.indicadores.crearIndicador"/></a></li>
						<li><a href="?accion=editaCategoria&id=0"><spring:message code="jsp.indicadores.indicadores.crearCategoria"/></a></li>
					</c:if>
				</ul>
			</div>
		
		</div>
		<div class="clear"></div>
	</div>
	
<div id="dialogoModalSeleccionCategoria" style="display: none;background-color: white;">
     <div class="ui-widget">
         <div style="padding: 0.2em 0.7em;" class=""> 
             <p>
                 <span style="float: left; margin-right: 0.3em;" class=""></span>
                 <spring:message code="jsp.indicadores.indicadores.necesarioSeleccionarCategoria"/>
             </p>
         </div>
     </div>
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
 
   <div id="dialogoModalBorrarRaiz" style="display: none; background-color: white;">
     <div class="ui-widget">
         <div style="padding: 0.2em 0.7em;" class=""> 
             <p>
                 <span style="float: left; margin-right: 0.3em;" class=""></span>
                 <spring:message code="jsp.indicadores.indicadores.error.borrar.raiz"/>
             </p>
         </div>
     </div>
 </div>
 
   <div id="dialogoModalModificarRaiz" style="display: none; background-color: white;">
     <div class="ui-widget">
         <div style="padding: 0.2em 0.7em;" class=""> 
             <p>
                 <span style="float: left; margin-right: 0.3em;" class=""></span>
                 <spring:message code="jsp.indicadores.indicadores.error.modificar.raiz"/>
             </p>
         </div>
     </div>
 </div>
 
 <div id="dialogoModalEliminarCategoria" style="display: none;background-color: white;">
     <div class="ui-widget">
         <div style="padding: 0.2em 0.7em;" class=""> 
             <p>
                 <span style="float: left; margin-right: 0.3em;" class=""></span>
                 <spring:message code="jsp.indicadores.indicadores.eliminar.categoria"/>
             </p>
         </div>
     </div>
 </div>
 
  <div id="dialogoModalEliminarIndicador" style="display: none;background-color: white;">
     <div class="ui-widget">
         <div style="padding: 0.2em 0.7em;" class=""> 
             <p>
                 <span style="float: left; margin-right: 0.3em;" class=""></span>
                 <spring:message code="jsp.indicadores.indicadores.eliminar.indicador"/>
             </p>
         </div>
     </div>
 </div>
 
<script type="text/javascript">
	$(document).ready(function() {
		$('#campoBusqDirect').focus();
	});

	var idCategoria=null;
	var idIndicador=null;
	
	var mapaIndicadores=${mapaIndicadores};
	var mapaCategorias=${mapaCategorias};

$(document).ready(function() {
	$(".enlaceTablaCategoria").live('click',function(){
		if(idCategoria!=null)
			$("#tuplaCategoria"+idCategoria).removeClass("fondoGris");
		if(idIndicador!=null){
			$("#tuplaIndicador"+idIndicador).removeClass("fondoGris");
		}
		
		idCategoria=$(this).attr("idcategoria");
		idIndicador = null;
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
		idCategoria = null;
		$("#tuplaIndicador"+idIndicador).addClass("fondoGris");
		return false;
	});
	
	$("#btnCrearIndicador").click(function(){
		if(idCategoria==null){
			var dialogoModalSeleccionCategoria=$( "#dialogoModalSeleccionCategoria" ).dialog({
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
			$(dialogoModalSeleccionCategoria).dialog("open");
			return false;
		}
		document.location.href="?accion=editaIndicador&id=0&idCategoria="+idCategoria;
		return false;
	});
	
	$("#btnBorrar").click(function(){
		if(idCategoria==null && idIndicador==null){
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
		if(idCategoria==null){
			var dialogoModalEliminarIndicador=$( "#dialogoModalEliminarIndicador" ).dialog({
		        autoOpen: false,
		        show: "blind",
		        hide: "explode",                    
		        width: 400,
		        height: 200,
		        modal: true,
		        resizable: false,
		        buttons: {
		            "Aceptar": function() {
		            	window.location="indicadores.htm?accion=borraIndicador&idIndicador="+idIndicador;
		                $( this ).dialog( "close" );
		                return false;
		                
		            },
					"Cancelar": function(){
		                $( this ).dialog( "close" );
		                return false;
					}
		        }            
		    });  
			$(dialogoModalEliminarIndicador).dialog("open");
			return false;
		}
		if(idCategoria == 0){
			var dialogoModalSeleccionElemento=$( "#dialogoModalBorrarRaiz" ).dialog({
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
		if(idIndicador==null){
			var dialogoModalEliminarCategoria=$( "#dialogoModalEliminarCategoria" ).dialog({
		        autoOpen: false,
		        show: "blind",
		        hide: "explode",                    
		        width: 400,
		        height: 200,
		        modal: true,
		        resizable: false,
		        buttons: {
		            "Aceptar": function() {
		            	window.location="indicadores.htm?accion=borraCategoria&idCategoria="+idCategoria;
		                $( this ).dialog( "close" );
		                return false;
		                
		            },
					"Cancelar": function(){
		                $( this ).dialog( "close" );
		                return false;
					}
		        }            
		    });  
			$(dialogoModalEliminarCategoria).dialog("open");
			return false;
		}
	});
	
	$("#btnEditarIndicador").click(function(){
		if(idIndicador==null && idCategoria==null){
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
		if ( idCategoria==null)
		{
			document.location.href="indicadores.htm?accion=editaIndicador&id="+idIndicador;
		}
		if(idCategoria == 0){
			var dialogoModalSeleccionElemento=$( "#dialogoModalModificarRaiz" ).dialog({
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
		if ( idIndicador==null)
			document.location.href="indicadores.htm?accion=editaCategoria&id="+idCategoria;
		return false;
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
			var funcionJs = "onclick=\"mostrarOcultarArbore('idcategoriapadre-" + listaCategorias[i].id + "_tr', 'idcategoria" + listaCategorias[i].id + "_img',true, false, false,false);\"";
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
			htmlTbCupero += "<td id='" + tdId + "' class='" + tdClass + "' style='padding-left: " + 20 * nivel + "px;'><a href='#' class='enlaceTablaIndicador' idindicador='" + idIndicador + "'><img id='idindicador" + idIndicador +"_img' src='images/file.png' alt='' style='float: left; padding-right: 8px;' /><span style='float: left; padding-left: 10px:'>" + nombreIndicador + "</span></a></td>";
			
			htmlTbCupero += "</tr>";
			$('#tbCupero').append(htmlTbCupero);
		}
	}
}

rellenarArbol(0, 1);
</script>
