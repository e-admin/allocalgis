<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<script src="js/util/comun/cambioMenu.js"></script>
	<div class="cuerpoprincipal">
		
		<div class="areamenu">
			<ul class="menusecundario">
				<li>
					<a href="#" onclick="cambioMenu('indicadores.htm'); return false;">
						<spring:message code="jsp.indicadores.categoria.jerarquiaIndicadores"/>
					</a>
				</li>
			</ul>
		</div>
		
		<div class="areacentral">
			<c:if test="${categoriaDto.id==0}">
				<h2><spring:message code="jsp.indicadores.categoria.crearCategoria"/></h2>
			</c:if>
			<c:if test="${categoriaDto.id>0}">
				<h2><spring:message code="jsp.indicadores.categoria.modificarCategoria"/>
					<c:if test="${!mostrarNombreCategoriaOriginal}">
						&nbsp${categoriaDto.nombre}
					</c:if>
					<c:if test="${mostrarNombreCategoriaOriginal}">
						&nbsp${categoriaOrigenDto.nombre}
					</c:if>
				</h2>
			</c:if>
			
			<br/>
			<!--  mensajes de error -->
			<spring:hasBindErrors name="categoriaDto">
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
					</div>
				</div>
			</div>
			<br/>
			</spring:hasBindErrors>
			
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
			
			<form:form id="formularioCategoria" modelAttribute="categoriaDto" action="?">
				<input type="hidden" name="accion" value="guardaCategoria"/>
				<input type="hidden" name="id" value="${categoriaDto.id}"/>
				<input type="hidden" name="idCatSel" value="${idCategoriaSeleccionada}"/>
				
				<div class="linea"> 
					<label class="label fizq"><spring:message code="jsp.indicadores.categoria.categoriaPadre"/></label>
					<c:if test="${categoriaDto.id>0}">
					<c:set var="disabled" value="true"></c:set> 
					</c:if>
					<c:if test="${categoriaDto.id<=0}">
					<c:set var="disabled" value="false"></c:set>
					</c:if>
					<form:select id="cat_padre" path="idCategoriaPadre" cssStyle="width:60%;" disabled="${disabled}">
						<form:option value="0"><spring:message code="jsp.indicadores.categoria.raiz"/></form:option>
						<c:forEach items="${listaCategoriasPadre}" var="cat">
							<option <c:if test="${idCategoriaSeleccionada!=null && idCategoriaSeleccionada==cat.id}">selected="selected"</c:if>S value="${cat.id}">${cat.nombre}</option>
						</c:forEach>
					</form:select>
				</div>
				
				<div class="linea">
					<label class="label fizq"><spring:message code="jsp.indicadores.categoria.nombre"/></label>
					<form:input path="nombre" cssStyle="width:60%;" maxlength="100"/>
				</div>
				
				<div class="linea">
					<label class="label fizq"><spring:message code="jsp.indicadores.categoria.descripcion"/></label>
					<form:textarea path="descripcion" cssStyle="width:60%;" rows="5"/>
				</div>
			</form:form>			
		</div>
		<div class="clear"></div>
		<div class="botones_accion">
			<a href="indicadores.htm" class="boton izq10 sup10 fondo_blanco fder"><spring:message code="elementos.jsp.boton.cancelar"/></a>
			<a href="#" onclick="guardarCategoria();return false;" class="boton izq10 sup10 fondo_blanco fder"><spring:message code="elementos.jsp.boton.aceptar"/></a>
		</div>	
		<div class="clear"></div>	
	</div>
<div id="dialogoModalAviso" style="display: none;background-color: white;">
   <div class="ui-widget">
       <div style="padding: 0.2em 0.7em;" class=""> 
           <p>
               <span style="float: left; margin-right: 0.3em;" class=""></span>
               <spring:message code="elementos.jsp.aviso.cambio"/>
            </p>
        </div>
    </div>
</div>
<script type="text/javascript">
	$(document).ready(function() {
		$('#cat_padre').focus();
	});
	
	function guardarCategoria () {
		$('#cat_padre').attr("disabled",false);
		$('#formularioCategoria').submit();
	}
</script>