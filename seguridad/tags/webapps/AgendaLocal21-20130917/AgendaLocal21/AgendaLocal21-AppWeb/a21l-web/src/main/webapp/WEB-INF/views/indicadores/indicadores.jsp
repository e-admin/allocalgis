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
				<li class="on">
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
						<a href="indicadores.htm?accion=listaIndicadoresPendientes">
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
          	<c:if test="${errorBorrarIndicador}">
	            <div class="controlAlertas">	
					<div class="ui-widget">
	            		<div class="ui-state-error ui-corner-all"> 
							<p>
								<span class="ui-icon ui-icon-circle-check fizq"></span>
					          	<spring:message code="jsp.indicadores.indicadores.error.borrar.indicador" />
				         	</p>
				  	 	</div>
	         		</div>
         		</div>
         		<br/>     
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
            
           <c:if test="${borrarSinPermisos != null}">
	            <div class="controlAlertas">	
					<div class="ui-widget">
	            		<div class="ui-state-error ui-corner-all"> 
							<p>
								<span class="ui-icon ui-icon-circle-check fizq"></span>
					          	<spring:message code="jsp.indicadores.indicadores.borrar.sinpermisos" />
				         	</p>
				  	 	</div>
	         		</div>
         		</div>
         		<br/>    
         	</c:if>
            <c:if test="${editarSinPermisos != null}">
	            <div class="controlAlertas">	
					<div class="ui-widget">
	            		<div class="ui-state-error ui-corner-all"> 
							<p>
								<span class="ui-icon ui-icon-circle-check fizq"></span>
					          	<spring:message code="jsp.indicadores.indicadores.editar.sinpermisos" />
				         	</p>
				  	 	</div>
	         		</div>
         		</div>
         		<br/>    
         	</c:if>
            <c:if test="${categoriaDto.resultadoOperacion.resultado == 'exitoCrear' || categoriaDto.resultadoOperacion.resultado == 'exitoGuardar'}">
            	<div class="controlAlertas">	
					<div class="ui-widget">
            			<div class="success ui-corner-all"> 
							<p>
								<span class="ui-icon ui-icon-circle-check fizq"></span>
								<c:if test="${categoriaDto.resultadoOperacion.resultado == 'exitoCrear'}">
				          			<spring:message code="jsp.indicadores.categoria.exito.crear" />
				          		</c:if>
				          		<c:if test="${categoriaDto.resultadoOperacion.resultado == 'exitoGuardar'}">
				          			<spring:message code="jsp.indicadores.categoria.exito.guardar" />
				          		</c:if>
			         		</p>
			  	 		</div>
         			</div>
         		</div>
         		<br/>     
     		</c:if>
     		
      		<div class="linea">
				<span class="fizq" style="position: relative;top: 5px;"><spring:message code="jsp.indicadores.indicadores.busquedaDirecta"/>&nbsp;</span>
				<input class="fizq" type="text" id="campoBusqDirect" value="" size="40" onKeyPress="return buscar(event)" />&nbsp;
				<a href="" class="boton fizq" style="position: relative;right: -10px;" id="btnBusquedaDirecta"><spring:message code="jsp.indicadores.indicadores.buscar"/></a>
				<a href="?accion=editaBusquedaAvanzada" class="claseLink" style="position: relative;top: 5px;right: -10px; color:black;"><spring:message code="jsp.indicadores.indicadores.busquedaAvanzada"/></a>
			</div>
			<br/>
			<div class="clear"></div>
			<a onclick="mostrarOcultarArbore('idcategoriapadre-0_tr', 'idcategoria0_img',false, true, false,true);"><img alt="mas" src="images/mas.png"/></a>
			<a onclick="mostrarOcultarArbore('idcategoriapadre-0_tr', 'idcategoria0_img',false, false, true,false);"><img alt="mas" src="images/menos.png"/></a>
			<div class="contenedorarbol ancho70" style="overflow:auto;">
				<table id="tablaIndicadores" class="tabla_jerarquia" border="0" cellspacing="0" cellpadding="0">
					<thead>
						<tr>
							<td id="tuplaCategoria0" class="cat">
								<a onclick="mostrarOcultarArbore('idcategoriapadre-0_tr', 'idcategoria0_img',true, false, false,false);">
									<img id="idcategoria0_img" src="images/folder_open.png" style="float: left;" />
								</a>
								<a href="#" class="enlaceTablaCategoria" style="padding-left: 8px;" idCategoria="0">
									<spring:message code="jsp.indicadores.indicadores.raiz"/>
								</a>
							</td>
						</tr>
					</thead>
				</table>
			</div>
			
			<div class="operacionesarbol">
				<ul>
					<li><a id="visualizarIndicador" href="#"><spring:message code="jsp.indicadores.indicadores.visualizar"/></a></li>
					<c:if test="${!usuarioInvitado}">
						<li><a id="btnEditarIndicador" href="#"><spring:message code="jsp.indicadores.indicadores.modificar"/></a></li>
						<li><a href="#" id="btnBorrar" onclick="return false;"><spring:message code="jsp.indicadores.indicadores.borrar"/></a></li>
						<li><a href="#" id="btnPublicarWeb" onclick="return false;"><spring:message code="jsp.indicadores.indicadores.publicarEnWeb"/></a></li>
						<li><a href="#" id="btnCrearIndicador" onclick="return false;"><spring:message code="jsp.indicadores.indicadores.crearIndicador"/></a></li>
						<li><a href="#" onclick="return false;" id="btnCrearCategoria"><spring:message code="jsp.indicadores.indicadores.crearCategoria"/></a></li>
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
 
 <div id="dialogoModalEliminarCategoriaSubcategorias" style="display: none;background-color: white;">
     <div class="ui-widget">
         <div style="padding: 0.2em 0.7em;" class=""> 
             <p>
                 <span style="float: left; margin-right: 0.3em;" class=""></span>
                 <spring:message code="jsp.indicadores.indicadores.eliminar.categoria.subcategoria"/>
             </p>
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
		InicializarTabla();
	});
	
	function InicializarTabla()
	{
		<c:forEach items="${mapaCategoriasTabla[0]}" var="categoria">
			dibujarFilaCategoria('tablaIndicadores', '0', '${categoria.id}', '${categoria.nombre}', 20);
		</c:forEach>

	 	<c:forEach items="${mapaIndicadoresTabla[0]}" var="indicador">
			dibujarFilaIndicador('tablaIndicadores', '0', '${indicador.id}', '${indicador.nombre}', 20,'${indicador.publico}', '${indicador.pteAprobacionPublico}');
		</c:forEach>
	}

	var idCategoria=null;
	var idIndicador=null;
	
	function buscar(e)
	{
		if (e && e.keyCode == 13){
			window.location="?accion=busquedaDirecta&criterio="+$("#campoBusqDirect").val();
			return false;
		}
	}
	
	$(".enlaceTablaCategoria").live("click",function(){
		if(idCategoria!=null)
			$("#tuplaCategoria"+idCategoria).removeClass("fondoGris");
		if(idIndicador!=null){
			$("#tuplaIndicador"+idIndicador).removeClass("fondoGris");
		}
		
		idCategoria=$(this).attr("idCategoria");
		idIndicador = null;
		$("#tuplaCategoria"+idCategoria).addClass("fondoGris");
		return false;
	});
	
	$(".enlaceTablaIndicador").live("click",function(){
		if(idCategoria!=null)
			$("#tuplaCategoria"+idCategoria).removeClass("fondoGris");
		if(idIndicador!=null){
			$("#tuplaIndicador"+idIndicador).removeClass("fondoGris");
		}
		
		idIndicador=$(this).attr("idIndicador");
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
		if (idCategoria==null) {
			document.location.href="indicadores.htm?accion=editaIndicador&id="+idIndicador;
			
			return false;
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
		
		if (idIndicador == null) {
			document.location.href="indicadores.htm?accion=editaCategoria&id="+idCategoria;
			
			return false;
		}
			
		return false;
	});
	
	$("#btnBorrar").click(function(){
		if(idCategoria == null && idIndicador == null){
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
		
		if(idCategoria == null){
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
			var num=$(".idCategoriaPadre_"+idCategoria).length;
			if(num!=null && num>0){
				var dialogoModalEliminarCategoriaSubcategorias=$( "#dialogoModalEliminarCategoriaSubcategorias" ).dialog({
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
				$(dialogoModalEliminarCategoriaSubcategorias).dialog("open");
				return false;
			}else{
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
		}
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
	
	$("#btnPublicarWeb").click(function(){
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
		document.location.href="indicadores.htm?accion=publicarEnWeb&id="+idIndicador;
		return false;
	});
	
	$('#btnCrearCategoria').click(function() {
		if ( idCategoria!=null ) {
			document.location.href="?accion=editaCategoria&id=0&idCatSel="+idCategoria;
		} else {
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
	});
	
$(document).ready(function() {
	$('td,th').mouseout(function() {
		$(this).removeClass("destacadoTablas");
	});
	
	$('td,th').mouseover(function() {
		if ( $(this).html().length>0 )
			$(this).addClass("destacadoTablas");
	});
	
	 <c:if test="${idIndicadorSenhalado!=null }">
		idIndicador=${idIndicadorSenhalado};
		$("#tuplaIndicador"+idIndicador).addClass("fondoGris");
		return false;
	 </c:if>
});


function dibujarFilaIndicador(idTabla, idCategoriaPadre, idIndicador, nombreIndicador, margenIzq, publico, pteAprobacionPublico)
{
	var nombreClassFila = "idcategoriapadre-" + idCategoriaPadre + "_tr";
	var table = document.getElementById(idTabla);

	var rowCount = table.rows.length;
	var row = table.insertRow(rowCount);
	row.id ="idIndicador" + idIndicador;
	row.className = "idcategoriapadre-" + idCategoriaPadre + "_tr";
	
	var cell = row.insertCell(0);
	cell.id = "tuplaIndicador" + idIndicador;
	cell.className = "no_cat";
	cell.style.paddingLeft = margenIzq + "px";
	if(publico=="true" && pteAprobacionPublico=="false"){
		cell.innerHTML = "<a href='#' class='enlaceTablaIndicador' idindicador='" + idIndicador + "'  title='<spring:message code='jsp.indicadores.indicadores.indicadorPublico'/>'><img id='idindicador" + idIndicador + "_img' src='images/file_publi.png' alt='' style='float: left; padding-right: 8px;' /><span style='float: left; padding-left: 10px:'>" + nombreIndicador + "</span></a>";	
	}else{
		cell.innerHTML = "<a href='#' class='enlaceTablaIndicador' idindicador='" + idIndicador + "'><img id='idindicador" + idIndicador + "_img' src='images/file.png' alt='' style='float: left; padding-right: 8px;' /><span style='float: left; padding-left: 10px:'>" + nombreIndicador + "</span></a>";
	}
}

function dibujarFilaCategoria(idTabla, idCategoriaPadre, idCategoria, nombreCategoria, margenIzq)
{
	var nombreClassFila = "idcategoriapadre-" + idCategoriaPadre + "_tr";

	// Se dibuja la celda correspondiente a la categoría
	var table = document.getElementById(idTabla);

	var rowCount = table.rows.length;
	var row = table.insertRow(rowCount);
	row.id = "idCategoria" + idCategoria;
	row.className = nombreClassFila;

	var funcionJs = "onclick=\"mostrarOcultarArbore('idcategoriapadre-" + idCategoria + "_tr', 'idcategoria" + idCategoria + "_img',true, false, false,false);\"";
	
	var cell = row.insertCell(0);
	cell.id = "tuplaCategoria" + idCategoria;
	cell.className = "cat destacadotablas";
	cell.style.paddingLeft = margenIzq + "px";
	cell.innerHTML = "<a " + funcionJs +"><img id='idcategoria" + idCategoria + "_img' src='images/folder_open.png' style='float: left;' /></a><a href='#' class='enlaceTablaCategoria idCategoriaPadre_"+idCategoriaPadre+"' style='padding-left: 8px;' idcategoria='" + idCategoria +"' idCategoriaPadre='"+idCategoriaPadre+"'>" + nombreCategoria + "</a>";
	
	// Se dibujan las filas correspondientes con sus Categorías hijo
	 <c:forEach items="${mapaCategoriasTabla}" var="entry">
	 	if (idCategoria == '${entry.key}')
	 	{
	 		<c:forEach items="${entry.value}" var="categoriaHija">
				dibujarFilaCategoria(idTabla, idCategoria, '${categoriaHija.id}', '${categoriaHija.nombre}', margenIzq + 20);
			</c:forEach>
		}
	</c:forEach>
	
	// Se dibujan las filas correspondientes con sus Indicadores hijo
	 <c:forEach items="${mapaIndicadoresTabla}" var="entry">
	 	if (idCategoria == '${entry.key}')
	 	{
	 		<c:forEach items="${entry.value}" var="indicadorHijo">
				dibujarFilaIndicador(idTabla, idCategoria, '${indicadorHijo.id}', '${indicadorHijo.nombre}', margenIzq + 20, '${indicadorHijo.publico}', '${indicadorHijo.pteAprobacionPublico}') ;
			</c:forEach>
		}
	</c:forEach>
}
</script>
