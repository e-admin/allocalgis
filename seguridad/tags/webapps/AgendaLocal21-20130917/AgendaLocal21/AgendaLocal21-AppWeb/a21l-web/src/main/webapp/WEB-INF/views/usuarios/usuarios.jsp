<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>  
<c:url var="urlBase" value="/"/>
<script src="js/util/comun/cambioMenus.js"></script>
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
       "aoColumns": [ { "sType": "html"},{ "sType": "html"},{ "sType": "html"}],
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
			<li class="on">
				<a href="#" onclick="cambioMenu('usuarios.htm'); return false;">
					<spring:message code="jsp.usuarios.usuarios.gestionUsuarios"/>
				</a>
			</li>
			<c:if test="${usuarioAdmin}">
				<li>
					<a href="#" onclick="cambioMenu('roles.htm'); return false;">
						<spring:message code="jsp.usuarios.usuarios.gestionRoles"/>
					</a>
				</li>
			</c:if>
		</ul>
	</div>

	<div class="areacentral">
		<h2 class="inf15"><spring:message code="jsp.usuarios.usuarios.listaUsuarios"/></h2>
		
		<div class="fizq ancho80">
			<c:if test="${resultadoOperacion== 'exitoBorrar'}">
            	<div class="controlAlertas">	
					<div class="ui-widget">
            			<div class="success ui-corner-all"> 
							<p>
								<span class="ui-icon ui-icon-circle-check fizq"></span>
				          		<spring:message code="jsp.usuarios.usuario.exito.borrar" />
			         		</p>
			  	 		</div>
         			</div>
         		</div>
         		<br/>     
          	</c:if>
          	<c:if test="${resultadoOperacion== 'errorBorrar' || resultadoOperacion=='entidadUtilizada'}">
            	<div class="controlAlertas">	
					<div class="ui-widget">
            			<div class="ui-state-error ui-corner-all"> 
							<p>
								<span class="ui-icon ui-icon-circle-check fizq"></span>
								<c:if test="${resultadoOperacion== 'errorBorrar'}">
				          			<spring:message code="jsp.usuarios.usuario.unicoAdmin" />
				          		</c:if>
				          		<c:if test="${resultadoOperacion== 'entidadUtilizada'}">
				          			<spring:message code="jsp.usuarios.usuario.usuarioUtilizado" />
				          		</c:if>
			         		</p>
			  	 		</div>
         			</div>
         		</div>
         		<br/>     
          	</c:if>
          	<c:if test="${usuarioDto.resultadoOperacion.resultado == 'exitoGuardar' || exitoAsignarRoles || exitoAsignarPermisos}">
            <div class="controlAlertas">	
				<div class="ui-widget">
            		<div class="success ui-corner-all"> 
						<p>
							<span class="ui-icon ui-icon-circle-check fizq"></span>
							<c:if test="${usuarioDto.resultadoOperacion.resultado == 'exitoGuardar'}">
				          		<spring:message code="jsp.usuarios.usuario.exito.mofidicar" />
				          	</c:if>
				          	<c:if test="${exitoAsignarRoles}">
				          		<spring:message code="jsp.usuarios.usuarioAsignaRoles.exito.guardar" />
				          	</c:if>
				          	<c:if test="${exitoAsignarPermisos}">
				          		<spring:message code="jsp.usuarios.usuarioAsignarPermisos.exito.guardar" />
				          	</c:if>
			         	</p>
			  	 	</div>
         		</div>
         	</div> 
         	<br/>    
          </c:if>
          <c:if test="${usuarioDto.resultadoOperacion.resultado == 'exitoCrear'}">
            <div class="controlAlertas">	
				<div class="ui-widget">
            		<div class="success ui-corner-all"> 
						<p>
							<span class="ui-icon ui-icon-circle-check fizq"></span>
				          	<spring:message code="jsp.usuarios.usuario.exito.guardar" />
			         	</p>
			  	 	</div>
         		</div>
         	</div> 
         	<br/>    
          </c:if>
          	<c:if test="${msgRestPropiaPass}">
          		<div class="controlAlertas">	
					<div class="ui-widget">
	            		<div class="ui-state-error ui-corner-all"> 
							<p>
					          	<spring:message code="jsp.usuarios.usuarios.msgRestPropiaPass" />
				         	</p>
				  	 	</div>
	         		</div>
         		</div>
         		<br/>
          	</c:if>
          	<c:if test="${msgDelPropioUser}">
          		<div class="controlAlertas">	
					<div class="ui-widget">
	            		<div class="ui-state-error ui-corner-all"> 
							<p>
					          	<spring:message code="jsp.usuarios.usuarios.msgDelPropioUser" />
				         	</p>
				  	 	</div>
	         		</div>
         		</div> 
         		<br/>
          	</c:if>
          	<c:if test="${passRestablecida}">
            <div class="controlAlertas">	
				<div class="ui-widget">
            		<div class="success ui-corner-all"> 
						<p>
							<span class="ui-icon ui-icon-circle-check fizq"></span>
				          	<spring:message code="jsp.usuarios.usuario.pass.restablecida" />
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
          	
			<table id="tablaDatos"  class="tablasTrabajo" cellpadding="0" cellspacing="0">
	    		<thead>
		    		<tr>
		    			<th class="tablaTitulo izquierda"><spring:message code="jsp.usuarios.usuarios.usuario"/></th>
		    			<th class="tablaTitulo izquierda"><spring:message code="jsp.usuarios.usuarios.perfil"/></th>
		    			<th class="tablaTitulo izquierda"><spring:message code="jsp.usuarios.usuarios.roles"/></th>
		    		</tr>
	    		</thead>
	    		<tbody>
			    	<c:forEach items="${listaUsuarios}" var="usuarioDto">
				  		<tr onmouseout="this.className='tablasTrabajo'" onmouseover="this.className='destacadoTablas tablasTrabajo'" class="tablasTrabajo">
				   			<td class="tablasDetalle izquierda tupla${usuarioDto.id}"><a class="tablasTrabajo" idUsuario="${usuarioDto.id}" tieneFuentes="${usuarioDto.tieneFuentes}" href="#">${usuarioDto.login}</a></td>
				      		<td class="tablasDetalle izquierda tupla${usuarioDto.id}">
				      			<a class="tablasTrabajo" idUsuario="${usuarioDto.id}" tieneFuentes="${usuarioDto.tieneFuentes}" href="#">
				  					<c:if test="${!usuarioDto.esAdmin}"><spring:message code="jsp.usuarios.usuarios.usuario"/></c:if>
				      				<c:if test="${usuarioDto.esAdmin}"><spring:message code="jsp.usuarios.usuarios.administrador"/></c:if>
				      			</a>
				      		</td >
				     	 	<td class="tablasDetalle izquierda tupla${usuarioDto.id}"><a style="text-align: left;" class="tablasTrabajo"  idUsuario="${usuarioDto.id}" tieneFuentes="${usuarioDto.tieneFuentes}" href="#">${mapaRolesUsuario[usuarioDto.id]}</a></td>
				   		</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<div class="fder ancho17">
			<ul class="operaciones">
				<c:if test="${usuarioAdmin}">
					<li><a href="" id="enlaceRestablecarContrasenha"><spring:message code="jsp.usuarios.usuarios.restablecerContrasenha"/></a></li>
					<li><a href="" id="enlaceAsignarRoles"><spring:message code="jsp.usuarios.usuarios.asignarRoles"/></a></li>
					<li><a href="" id="enlaceGestionarPermisos"><spring:message code="jsp.usuarios.usuarios.gestionarPermisos"/></a></li>
					<li><a href="" id="enlaceModificar"><spring:message code="jsp.usuarios.usuarios.modificar"/></a></li>
					<li><a href="" id="enlaceEliminar"><spring:message code="jsp.usuarios.usuarios.eliminar"/></a></li>
				</c:if>
			</ul>
		</div>
	</div>
	<div class="clear"></div>
	<c:if test="${usuarioAdmin}">
		<div class="botones_accion">
			<a href="?accion=crearUsuario" class="boton izq10 sup10 fondo_blanco fder"><spring:message code="jsp.usuarios.usuarios.crearUsuario"/></a>			
		</div>
	</c:if>	
</div>

<!-- DIALOGO MODAL -->
  <div id="dialogoModalConfirmacion" style="display: none;background-color: white;">
      <div class="ui-widget">
          <div style="padding: 0.2em 0.7em;" class=""> 
              <p>
                  <span style="float: left; margin-right: 0.3em;" class=""></span>
                  <spring:message code="jsp.usuarios.usuarios.dialogoEliminar"/>
              </p>
          </div>
      </div>
  </div>
  
<!-- DIALOGO MODAL Fuentes -->
  <div id="dialogoModalFuentes" style="display: none;background-color: white;">
      <div class="ui-widget">
          <div style="padding: 0.2em 0.7em;" class=""> 
              <p>
                  <span style="float: left; margin-right: 0.3em;" class=""></span>
                  <spring:message code="jsp.usuarios.usuarios.dialogoEliminarFuentes"/>
              </p>
              <p>
                  <span style="float: left; margin-right: 0.3em;" class=""></span>
                  <spring:message code="jsp.usuarios.usuarios.dialogoEliminar"/>
              </p>
          </div>
      </div>
  </div>
  
<script type="text/javascript">
	var idUsuario=0;
	
	$(".tablasTrabajo").live('click',function(){
		if(idUsuario!=null && idUsuario!=0){
			$(".tupla"+idUsuario).removeClass("fondoGris");
		}
		idUsuario=$(this).attr('idUsuario');
		tieneFuentes=$(this).attr('tieneFuentes');
		$(".tupla"+idUsuario).addClass("fondoGris");
		return false;
	});
	
	$("#enlaceModificar").click(function(){
		if(idUsuario==null || idUsuario==0){
			alert("<spring:message code="jsp.usuarios.usuarios.seleccionarUsuario"/>");
			return false;
		}
		window.location="${urlBase}usuarios.htm?accion=modificarUsuario&id="+idUsuario;
		$(this).attr("disabled", true);
		return false;
	});
	
	$("#enlaceRestablecarContrasenha").click(function(){
		if(idUsuario==null || idUsuario==0){
			alert("<spring:message code="jsp.usuarios.usuarios.seleccionarUsuario"/>");
			return false;
		}
		window.location="${urlBase}usuarios.htm?accion=restablecerContrasenha&id="+idUsuario;
		$(this).attr("disabled", true);
		return false;
	});
	
	$("#enlaceAsignarRoles").click(function(){
		if(idUsuario==null || idUsuario==0){
			alert("<spring:message code="jsp.usuarios.usuarios.seleccionarUsuario"/>");
			return false;
		}
		window.location="?accion=asignaRoles&id="+idUsuario;
		$(this).attr("disabled", true);
		return false;
	});
	
	$("#enlaceGestionarPermisos").click(function(){
		if(idUsuario==null || idUsuario==0){
			alert("<spring:message code="jsp.usuarios.usuarios.seleccionarUsuario"/>");
			return false;
		}
		window.location="?accion=gestionarPermisos&id="+idUsuario;
		$(this).attr("disabled", true);
		return false;
	});
	
	$("#enlaceEliminar").click(function(){
		if(idUsuario==null || idUsuario==0){
			alert("<spring:message code="jsp.usuarios.usuarios.seleccionarUsuario"/>");
			return false;
		}
		var dialogoModalFuentes=$( "#dialogoModalFuentes" ).dialog({
	        autoOpen: false,
	        show: "blind",
	        hide: "explode",                    
	        width: 450,
	        height: 220,
	        modal: true,
	        resizable: false,
	        buttons: {
	            "Aceptar": function() {
	            	window.location="${urlBase}usuarios.htm?accion=eliminarUsuario&id="+idUsuario;
	                $( this ).dialog( "close" );
	                return false;
	                
	            },
	            "Cancelar": function() {
	                $( this ).dialog( "close" );
	                return false;
	            }
	        }            
	    });
		var dialogoModalConfirmacion=$( "#dialogoModalConfirmacion" ).dialog({
	        autoOpen: false,
	        show: "blind",
	        hide: "explode",                    
	        width: 400,
	        height: 200,
	        modal: true,
	        resizable: false,
	        buttons: {
	            "Aceptar": function() {
		            	window.location="${urlBase}usuarios.htm?accion=eliminarUsuario&id="+idUsuario;
		                $( this ).dialog( "close" );
		                return false;
	            	
	            },
	            "Cancelar": function() {
	                $( this ).dialog( "close" );
	                return false;
	            }
	        }            
	    });
		if(tieneFuentes!=null && tieneFuentes=='true'){
    		$(dialogoModalFuentes).dialog("open");
    		return false;
    	}else{
			$(dialogoModalConfirmacion).dialog("open");
			return false;
    	}
	});
</script>