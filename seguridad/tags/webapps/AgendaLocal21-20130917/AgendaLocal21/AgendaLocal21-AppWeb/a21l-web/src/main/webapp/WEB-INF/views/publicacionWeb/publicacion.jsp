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
				<a href="publicacionWeb.htm">
					<spring:message code="jsp.publicacion.publicacion.web"/>
				</a>
			</li>
			<li>
				<a href="publicacionWeb.htm?accion=listaIndicadoresPendientes">
					<spring:message code="jsp.publicacion.lista.indicadores.pendientes"/>
				</a>
			</li>
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
			<c:if test="${empty mapaCategoriasTabla && empty mapaIndicadoresTabla}">
				<div><spring:message code="jsp.indicadores.lista.vacia"/></div>
			</c:if>
			<c:if test="${!empty mapaCategoriasTabla || !empty mapaIndicadoresTabla}">
				<table id="tablaIndicadores" class="tabla_jerarquia" border="0" cellspacing="0" cellpadding="0">
					<thead>
						<tr>
							<td id="tuplaCategoria0" class="cat" style="width: 57%;">
								<a href="#" onclick="mostrarOcultarArbore('idcategoriapadre-0_tr', 'idcategoria0_img',true, false, false,false);">
									<img id="idcategoria0_img" src="images/folder_open.png" style="float: left;" />
								</a>
								<a href="#" class="enlaceTablaCategoria" style="padding-left: 8px;" idCategoria="0">
									<spring:message code="jsp.indicadores.indicadores.raiz"/>
								</a>
							</td>
							<td style="width: 20%"></td>
							<td style="width: 23%"></td>
						</tr>
					</thead>
				</table>
			</c:if>
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
	
	$(".enlaceTablaIndicador").live("click",function()
	{
		if(idIndicador!=null)
		{
			$("#tuplaIndicador"+idIndicador).removeClass("fondoGris");
		}
		
		idIndicador=$(this).attr("idIndicador");
		$("#tuplaIndicador"+idIndicador).addClass("fondoGris");
		
		return false;
	});
	
	$("#retirarIndicador").click(function()
	{ 
		if(idIndicador==null)
		{
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
		
		document.location.href="publicacionWeb.htm?accion=retirarIndicadorPublicacionWeb&id="+idIndicador;
		
		return false;
	});

$(document).ready(function() {
	InicializarTabla();
	
	$('td,th').mouseout(function() 
	{
		$(this).removeClass("destacadoTablas");
	});
	
	$('td,th').mouseover(function() 
	{
		if ( $(this).html().length>0 )
			$(this).addClass("destacadoTablas");
	});
});

function InicializarTabla()
{
	<c:forEach items="${mapaCategoriasTabla[0]}" var="categoria">
		dibujarFilaCategoriaPub('tablaIndicadores', '0', '${categoria.id}', '${categoria.nombre}', 20);
	</c:forEach>

 	<c:forEach items="${mapaIndicadoresTabla[0]}" var="indicador">
		dibujarFilaIndicadorPub('tablaIndicadores', '0', '${indicador.id}', '${indicador.nombre}','${indicador.urlPublicacion}', '${indicador.urlPublicacionCorta}', '${indicador.numUsuariosPublicacion}', '${indicador.loginUltimaPeticion}', 20);
	</c:forEach>
}

function dibujarFilaIndicadorPub(idTabla, idCategoriaPadre, idIndicador, nombreIndicador, urlPublicacion, urlPublicacionCorta, numUsuarios, loginUsuario, margenIzq)
{
	var table = document.getElementById(idTabla);

	var rowCount = table.rows.length;
	var row = table.insertRow(rowCount);
	row.id ="idIndicador" + idIndicador;
	row.className = "idcategoriapadre-" + idCategoriaPadre + "_tr";
	
	// [1] Nombre del indicador
	var cell = row.insertCell(0);
	cell.id = "tuplaIndicador" + idIndicador;
	cell.className = "no_cat";
	cell.style.paddingLeft = margenIzq + "px";
	cell.innerHTML = "<a href='#' class='enlaceTablaIndicador' idIndicador='" + idIndicador + "'><img id='idindicador" + idIndicador + "_img' src='images/file.png' alt='' style='float: left; padding-right: 8px;' /><span style='float: left; padding-left: 10px:'>" + nombreIndicador + "</span></a>";
	
	// [2] Enlace a la página de consulta
	var cell2 = row.insertCell(1);
	cell2.innerHTML = "<a target='_blank' href='" + urlPublicacion + "'>" + urlPublicacionCorta + "</a>";
	
	// [3] Usuario(s) que solicitaron la publicación
	var cell3 = row.insertCell(2);
	cell3.innerHTML = "(" + numUsuarios + ") " + loginUsuario;
}

function dibujarFilaCategoriaPub(idTabla, idCategoriaPadre, idCategoria, nombreCategoria, margenIzq)
{
	var nombreClassFila = "idcategoriapadre-" + idCategoriaPadre + "_tr";

	// Se dibuja la celda correspondiente a la categoría
	var table = document.getElementById(idTabla);

	var rowCount = table.rows.length;
	var row = table.insertRow(rowCount);
	row.id = "idCategoria" + idCategoria;
	row.className = nombreClassFila;

	var funcionJs = "onclick=\"mostrarOcultarArbore('idcategoriapadre-" + idCategoria + "_tr', 'idcategoria" + idCategoria + "_img',true, false, false,false);\"";
	
	// [0] Nombre de la categoría
	var cell = row.insertCell(0);
	cell.id = "tuplaCategoria" + idCategoria;
	cell.className = "cat destacadotablas";
	cell.style.paddingLeft = margenIzq + "px";
	cell.innerHTML = "<a href='#' " + funcionJs +"><img id='idcategoria" + idCategoria + "_img' src='images/folder_open.png' style='float: left;' /></a><a href='#' class='enlaceTablaCategoria' style='padding-left: 8px;' idcategoria='" + idCategoria +"'>" + nombreCategoria + "</a>";
	
	// [1] Celda en blanco
	var cell2 = row.insertCell(1);
	
	// [2] Celda en blanco
	var cell3 = row.insertCell(2);
	
	// Se dibujan las filas correspondientes con sus Categorías hijo
	 <c:forEach items="${mapaCategoriasTabla}" var="entry">
	 	if (idCategoria == '${entry.key}')
	 	{
	 		<c:forEach items="${entry.value}" var="categoriaHija">
				dibujarFilaCategoriaPub(idTabla, idCategoria, '${categoriaHija.id}', '${categoriaHija.nombre}', margenIzq + 20);
			</c:forEach>
		}
	</c:forEach>
	
	// Se dibujan las filas correspondientes con sus Indicadores hijo
	 <c:forEach items="${mapaIndicadoresTabla}" var="entry">
	 	if (idCategoria == '${entry.key}')
	 	{
	 		<c:forEach items="${entry.value}" var="indicadorHijo">
				dibujarFilaIndicadorPub(idTabla, idCategoria, '${indicadorHijo.id}', '${indicadorHijo.nombre}','${indicadorHijo.urlPublicacion}', '${indicadorHijo.urlPublicacionCorta}', '${indicadorHijo.numUsuariosPublicacion}', '${indicadorHijo.loginUltimaPeticion}', margenIzq + 20);
			</c:forEach>
		}
	</c:forEach>
}
</script>

