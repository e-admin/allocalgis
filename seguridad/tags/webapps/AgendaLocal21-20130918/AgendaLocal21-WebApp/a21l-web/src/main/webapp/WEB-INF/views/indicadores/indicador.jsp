<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script src="js/util/comun/cambioMenu.js"></script>  
		<div class="cuerpoprincipal">
			<div class="areacompleta">
				<div class="areamenu fondo_blanco">
					<c:if test="${indicadorDto.id==0}">
						<h2 class="inf15"><spring:message code="jsp.indicadores.indicador.crearIndicador"/></h2>
					</c:if>
					<c:if test="${indicadorDto.id>0}">
						<h2 class="inf15"><spring:message code="jsp.indicadores.indicador.modificarIndicador"/></h2>
					</c:if>
					<div class="linea inf15 fizq">
						<label class="fizq" for="fuente"><spring:message code="jsp.indicador.seleccione.fuente"/></label>
						<select id="fuente_selec" name="fuente" class="ancho100">
							<option value="0">...</option>
							<c:forEach items="${listaFuentes}" var="fuente">
	                      		<option value="${fuente.id}||${fuente.tipo.id}">${fuente.nombre}</option>
	                       </c:forEach>
                    	</select>
                    	<span id="cargando_tablas" class="oculto"><img height="24px" src="images/ajax/cargando.gif"/></span>
					</div>
					<div class="linea">
						<label class="fizq" for="fuente_selec"><spring:message code="jsp.indicador.tablas.fuente"/></label>
						<select class="select_multiple" id="tabla_fuente_selec" name="tabla_fuente_selec" size="5">
						</select>
					</div>
					<span id="cargando_columnas" class="oculto"><img height="24px" src="images/ajax/cargando.gif"/></span>
					<a id="anhadir_tabla" class="boton fder" href="#"><spring:message code="jsp.indicador.anhadir.tabla"/></a>
					<div class="clear"></div>
					
					<c:if test="${tabla_ambito != null && columna_ambito != null }">
						<c:set var="mostrarAmbito" value=""/> 
					</c:if>
					<c:if test="${tabla_ambito == null && columna_ambito == null }">
						<c:set var="mostrarAmbito" value="oculto"/> 
					</c:if>
					
					<c:if test="${tabla_mapa != null && columna_mapa != null }">
						<c:set var="mostrarMapa" value=""/> 
					</c:if>
					<c:if test="${tabla_mapa == null && columna_mapa == null }">
						<c:set var="mostrarMapa" value="oculto"/> 
					</c:if>
					
					<h4 id="tit_ambito" class="${mostrarAmbito}"><spring:message code="jsp.indicador.campo.ambito"/></h4>
					<fieldset class="${mostrarAmbito} sup10" id="columna_marcada_ambito">
						<c:if test="${tabla_ambito != null && columna_ambito != null }">
							<div>
								<label><spring:message code="jsp.indicador.tabla"/>:</label>
								<span>${tabla_ambito}</span>
							</div>
							<div>
								<label><spring:message code="jsp.indicador.columna"/>:</label>
								<span><strong>${columna_ambito}</strong></span>
							</div>
						</c:if>
					</fieldset>
					<h4 id="tit_mapa" class="${mostrarMapa}"><spring:message code="jsp.indicador.campo.mapa"/></h4>
					<fieldset class="${mostrarMapa} sup10" id="columna_marcada_mapa">
						<c:if test="${tabla_mapa != null && columna_mapa != null }">
							<div>
								<label><spring:message code="jsp.indicador.tabla"/>:</label>
								<span>${tabla_mapa}</span>
							</div>
							<div>
								<label><spring:message code="jsp.indicador.columna"/>:</label>
								<span><strong>${columna_mapa}</strong></span>
							</div>
						</c:if>
					</fieldset>
				</div>
				
				<div class="areacentral">			
					<a id="boton_ayuda" href="#" class="boton interroga fder"><spring:message code="elementos.jsp.ayuda"/></a>
					<br/>
					
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
					<spring:hasBindErrors name="indicadorDto">
					<div class="controlAlertas">	
						<div class="ui-widget">
							<div class="ui-state-error ui-corner-all"> 
								<p><span class="ui-icon ui-icon-alert fizq"></span> 
								<strong><spring:message code="global.formulario.erros.titulo"/></strong> 
								</p>
								<ul>
									${empty error.globalError ? "" : "<li>" + error.defaultMessage + "</li>"}
									<c:forEach items="${errors.fieldErrors}" var="error">
							         	<li><form:errors path="${error.objectName}.${error.field}"/></li>
							        </c:forEach>
								</ul>
								<p></p>
							</div>
						</div>
					</div>
					<br/>
					</spring:hasBindErrors>
					<c:if test="${erroresValidacion}">
					<div class="controlAlertas">	
						<div class="ui-widget">
							<div class="ui-state-error ui-corner-all"> 
								<p><span class="ui-icon ui-icon-alert fizq"></span> 
								<strong><spring:message code="global.formulario.erros.titulo"/></strong> 
								</p>
								<ul>
									<c:forEach items="${listaErrores}" var="error">
							         	<li><spring:message code="${error}"/></li>
							        </c:forEach>
								</ul>
								<p></p>
							</div>
						</div>
					</div>
					<br/>
					</c:if>
					
					<c:if test="${indicadorDto.resultadoOperacion.resultado == 'exitoCrear' || indicadorDto.resultadoOperacion.resultado == 'exitoGuardar'}">
		            	<div class="controlAlertas">	
							<div class="ui-widget">
		            			<div class="success ui-corner-all"> 
									<p>
										<span class="ui-icon ui-icon-circle-check fizq"></span>
										<c:if test="${indicadorDto.resultadoOperacion.resultado == 'exitoCrear'}">
						          			<spring:message code="jsp.indicadores.indicador.exito.crear" />
						          		</c:if>
						          		<c:if test="${indicadorDto.resultadoOperacion.resultado == 'exitoGuardar'}">
						          			<spring:message code="jsp.indicadores.indicador.exito.guardar" />
						          		</c:if>
					         		</p>
					  	 		</div>
		         			</div>
		         		</div>
		         		<br/>     
		     		</c:if>
		     		<form:form  modelAttribute="indicadorDto" id="formularioIndicador" action="indicadores.htm">
						<input type="hidden" name="accion" value="guardaIndicador"/>
						<input type="hidden" name="id" value="${indicadorDto.id}"/>
						<input type="hidden" name="idCategoria" value="${indicadorDto.idCategoria}"/>
						<input type="hidden" name="pteAprobacionPublico" value="${indicadorDto.pteAprobacionPublico}" />
					<div class="clear alto40">
						<div class="linea fizq">
							<label class="alinea_inf"><spring:message code="jsp.indicadores.indicador.nombreIndicador"/></label>
							<form:input path="nombre" cssClass="input_pequeno" maxlength="255"/>
						</div>
						<div class="linea sup10">
							<label class="sup10 izq50 fizq"><spring:message code="jsp.indicadores.indicador.indicadorPublico"/></label>
							<form:checkbox cssClass="fizq" path="publico" id="publico"/>
						</div>
					</div>
					<div id="zona_tablas" <c:if test="${empty listaTablas}">class="oculto"</c:if> >
						<fieldset id="zona_columnas" >
							<!-- lista de tablas asociadas al indicador -->
							<c:set var="fuentestablas" value=""/>
							<c:set var="relacionestablas" value=""/>
							<c:set var="numRelaciones" value="0"/>
							<c:forEach items="${listaTablas}" var="tabla">
								<c:set var="fuentestablas" value="${fuentestablas}${tabla.fuente.id}|${tabla.nombre},"/>
								<div class="fizq">
								<table cellspacing="0" cellpadding="0" class="tabla_indicador izq10 inf15" id="tabla_${tabla.nombre}" data-idfuente="${tabla.fuente.id}">
									<tbody>
										<tr>
											<th data-tabla="tabla_${tabla.nombre}" class="cabeceraTabla" scope="col">${tabla.nombre}</th>
										</tr>
										<c:forEach items="${listaColumnasTabla[tabla.nombre]}" var="columna">
											<c:if test="${columna.tipoAtributo == 'VALORFDRELACION'}">
												<c:set var="relacionestablas" value="${relacionestablas}${tabla.fuente.id}|${tabla.nombre}|${columna.nombre}|${columna.strTipoDatoRelacion}|${columna.strFuenteRelacion}|${columna.strTablaRelacion}|${columna.strColumnaRelacion},"/>												
												<c:set var="numRelaciones" value="${numRelaciones+1}"/>
												<tr data-tabla="tabla_${tabla.nombre}" data-columna="${columna.nombre}" data-tipo-columna-relacionada="${columna.strTipoDatoRelacion}" data-tipo="${columna.tipoAtributo}" data-fuente-rel="${columna.strFuenteRelacion}" data-tabla-rel="${columna.strTablaRelacion}" data-col-rel="${columna.strColumnaRelacion}"  class="columnaTabla columnaTablaRelacion">
													<td>${columna.nombre}>${columna.strTablaRelacion}.${columna.strColumnaRelacion}</td>
												</tr>
											</c:if>
											<c:if test="${columna.tipoAtributo != 'VALORFDRELACION'}">
												<tr data-tabla="tabla_${tabla.nombre}" data-columna="${columna.nombre}" data-tipo="${columna.tipoAtributo}" class="columnaTabla">
													<td>${columna.nombre}</td>
												</tr>
											</c:if>
										</c:forEach>
									</tbody>
								</table>
								</div>
							</c:forEach>
						</fieldset>
						<a id="borrar_tabla" class="boton izq10 sup10 fder" href="#"><spring:message code="jsp.indicador.borrar.tabla"/></a>
						<a id="borrar_relacion" class="boton izq10 sup10 fder" href="#" onclick="return false;"><spring:message code="jsp.indicador.borrar.relacion"/></a>
						<a id="anhadir_relacion" class="boton izq10 sup10 fder" href="#"><spring:message code="jsp.indicador.anhadir.relacion"/></a>
						<c:if test="${mostrarMapa==''}">
							<c:set var="marcarMapa" value="oculto"/>
							<c:set var="desmarcarMapa" value=""/>
						</c:if>
						<c:if test="${mostrarMapa!=''}">
							<c:set var="marcarMapa" value=""/>
							<c:set var="desmarcarMapa" value="oculto"/>
						</c:if>
						
						<a id="desmarcar_mapa" class="${desmarcarMapa} boton izq10 sup10 fder" href="#"><spring:message code="jsp.indicador.desmarcar.mapa"/></a>
						<a id="marcar_mapa" class="${marcarMapa} boton izq10 sup10 fder" href="#"><spring:message code="jsp.indicador.marcar.mapa"/></a>
						<a id="marcar_ambito" class="boton sup10 fder" href="#"><spring:message code="jsp.indicador.marcar.ambito"/></a>
					</div>
					<div class="clear"></div>
					<!-- construccion campos envio formulario -->
					<div>
						<div id="campos_envio">
							<c:if test="${not empty listaColumnasIndicador}">
							<c:set var="contador" value="1"/>
							<c:forEach items="${listaColumnasIndicador}" var="col">
								<c:if test="${col.indicadorExpresion==null && col.relacion==null}">
								<input type="hidden" value="${col.columna.tabla.nombre}" id="tabla_campo_${contador}" name="tabla_campo_${contador}"/>
								<input type="hidden" value="${col.nombre}" id="columna_campo_${contador}" name="columna_campo_${contador}"/>
								<input type="hidden" value="${col.ordenVisualizacion}" id="orden_campo_${contador}" name="orden_campo_${contador}"/>
								<input type="hidden" id="mostrar_campo_${contador}" name="mostrar_campo_${contador}" value="${col.mostrar}"/>
								<input type="hidden" value="${col.columna.tabla.fuente.id}" id="idfuente_campo_${contador}" name="idfuente_campo_${contador}"/>
								<c:set var="contador" value="${contador+1}"/>
								</c:if>
							</c:forEach>
							</c:if>
						</div>
						<div id="formulas_envio">
							<c:if test="${not empty listaColumnasIndicador}">
							<c:set var="contador_formu" value="1"/>
							<c:forEach items="${listaColumnasIndicador}" var="col">
								<c:if test="${col.indicadorExpresion!=null && col.columna!=null}">
								<input type="hidden" value='${col.indicadorExpresion.expresionLiteral}' id="valor_formula_${contador_formu}" name="valor_formula_${contador_formu}"
									expresion="${col.indicadorExpresion.expresionLiteral}" />
								<input type="hidden" value="${col.nombre}" id="nombre_formula_${contador_formu}" name="nombre_formula_${contador_formu}"
									expresion="${col.indicadorExpresion.expresionLiteral}" />
								<input type="hidden" value="${col.ordenVisualizacion}" id="orden_formula_${contador_formu}" name="orden_formula_${contador_formu}"
									expresion="${col.indicadorExpresion.expresionLiteral}" />
								<input type="hidden" id="mostrar_formula_${contador_formu}" name="mostrar_formula_${contador_formu}" value="${col.mostrar}"
									expresion="${col.indicadorExpresion.expresionLiteral}" />
								<c:set var="contador_formu" value="${contador_formu+1}"/>
								</c:if>
							</c:forEach>
							</c:if>
						</div>
						<div id="relaciones_envio">
						<c:if test="${not empty listaRelaciones}">
							<c:set var="contador_relaciones" value="1"/>
							<c:forEach items="${listaRelaciones}" var="rel">
								<input id="tabla_relacion_${contador_relaciones}" type="hidden" value="${rel.tablaRelacion}" name="tabla_relacion_${contador_relaciones}">
								<input id="idfuente_relacion_${contador_relaciones}" type="hidden" value="${rel.idFuenteRelacion}" name="idfuente_relacion_${contador_relaciones}">
								<input id="columna_relacion_${contador_relaciones}" type="hidden" value="${rel.columnaRelacion}" name="columna_relacion_${contador_relaciones}">
								<input id="orden_relacion_${contador_relaciones}" type="hidden" value="${rel.ordenVisualizacion}" name="orden_relacion_${contador_relaciones}">
								<input id="mostrar_relacion_${contador_relaciones}" type="hidden" name="mostrar_relacion_${contador_relaciones}" value="${rel.mostrar}">
								<input id="tabla_relacionada_${contador_relaciones}" type="hidden" value="${rel.tablaRelacionada}" name="tabla_relacionada_${contador_relaciones}">
								<input id="columna_relacionada_${contador_relaciones}" type="hidden" value="${rel.columnaRelacionada}" name="columna_relacionada_${contador_relaciones}">
								<input id="idfuente_relacionada_${contador_relaciones}" type="hidden" value="${rel.idFuenteRelacionada}" name="idfuente_relacionada_${contador_relaciones}">
								<c:set var="contador_relaciones" value="${contador_relaciones+1}"/>
							</c:forEach>
						</c:if>
						</div>
						<div id="criterios_envio">
						<c:if test="${not empty listaCriterios}">
							<c:set var="contador_criterios" value="1"/>
							<c:forEach items="${listaColumnasIndicador}" var="col" varStatus="cont">
								<c:if test="${col.criterio!=null && col.criterio.cadenaCriterio!=''}">
									<!-- Campo Normal -->
									<c:if test="${col.criterio.atributo.columna != nulll}">
										<input id="tabla_criterio_${contador_criterios}" type="hidden" value="${col.criterio.atributo.columna.tabla.nombre}" name="tabla_criterio_${contador_criterios}">
										<input id="columna_criterio_${contador_criterios}" type="hidden" value="${col.criterio.atributo.columna.nombre}" name="columna_criterio_${contador_criterios}">
										<input id="valor_criterio_${contador_criterios}" type="hidden" value='${col.criterio.cadenaCriterio}' name="valor_criterio_${contador_criterios}">
										<c:set var="contador_criterios" value="${contador_criterios+1}"/>
									</c:if>
									<!-- Campo Fórmula -->
									<c:if test="${col.criterio.atributo.columna == null}">
										<input id="tabla_criterio_${contador_criterios}" type="hidden" value="${col.criterio.atributo.indicadorExpresion.expresionLiteral}" name="tabla_criterio_${contador_criterios}">
										<input id="columna_criterio_${contador_criterios}" type="hidden" value="${col.criterio.atributo.nombre}" name="columna_criterio_${contador_criterios}">
										<input id="valor_criterio_${contador_criterios}" type="hidden" value='${col.criterio.cadenaCriterio}' name="valor_criterio_${contador_criterios}">
										<c:set var="contador_criterios" value="${contador_criterios+1}"/>
									</c:if>
								</c:if>
							</c:forEach>
						</c:if>
						</div>
						<div id="campo_ambito">
							<c:if test="${tabla_ambito != null && columna_ambito != null }">
								<input type="hidden" value="${columna_ambito}" name="columna_campo_ambito" id="columna_campo_ambito"/>
								<input type="hidden" value="${tabla_ambito}" name="tabla_campo_ambito" id="tabla_campo_ambito"/>
							</c:if>
						</div>
						<div id="campo_mapa">
							<c:if test="${tabla_mapa != null && columna_mapa != null }">
								<input type="hidden" value="${columna_mapa}" name="columna_campo_mapa" id="columna_campo_mapa"/>
								<input type="hidden" value="${tabla_mapa}" name="tabla_campo_mapa" id="tabla_campo_mapa"/>
							</c:if>
						</div>
						<input type="hidden" name="num_relacionestablas_envio" id="num_relacionestablas_envio" value="${relacionestablas}"/>
						<input type="hidden" name="num_fuentes_tablas_envio" id="num_fuentes_tablas_envio" value="${fuentestablas}"/>
						<input type="hidden" name="num_campos_envio" id="num_campos_envio" value="${numCampos}"/>
						<input type="hidden" name="num_formulas_envio" id="num_formulas_envio" value="${numFormulas}"/>
						<input type="hidden" name="num_relaciones_envio" id="num_relaciones_envio" value="${numRelaciones}"/>
						<input type="hidden" name="num_criterios_envio" id="num_criterios_envio" value="${numCriterios}"/>
					</div>
				</form:form>
				</div>
				<!-- areacentral -->
				<p class="mayuscula sup40 fizq clear"><spring:message code="jsp.indicador.campos.a.mostrar"/>:</p>
				<div class="clear"></div>
				<div id="div_tabla_campos_mostrar" style="min-height: 50px; width: 889px; overflow-x:scroll;">
				<table id="tabla_campos_mostrar" class="tabla_campos_a_mostrar clear" cellpadding="0" cellspacing="0">
					<tr>
						<th class="tablaTitulo izquierda">&nbsp;</th>
						<th class="tablaTitulo izquierda"><spring:message code="jsp.indicador.tabla"/></th>
						<th class="tablaTitulo izquierda"><spring:message code="jsp.indicador.campo"/></th>						
						<th><spring:message code="jsp.indicador.orden"/></th>
						<th><spring:message code="jsp.indicador.mostrar"/></th>
						<th><spring:message code="jsp.indicador.criterios"/></th>							
					</tr>
					<c:set var="numCampos" value="1"/>
					<c:set var="numFormus" value="1"/>
					<c:set var="numCriterios" value="1"/>
					<c:forEach items="${listaColumnasIndicador}" var="col" varStatus="cont">
						<!-- es relacion -->
						<c:if test="${col.indicadorExpresion==null && col.relacion!=null}">
							<c:set var="claseFila" value="relacion"/>
						</c:if>
						<!-- es formula -->
						<c:if test="${col.indicadorExpresion!=null && col.relacion==null}">
							<c:set var="claseFila" value="formula"/>
						</c:if>
						<!-- campo -->
						<c:if test="${col.indicadorExpresion==null && col.columna!=null && col.relacion==null}">
							<c:set var="claseFila" value="campo"/>
						</c:if>
						<tr onmouseout="this.className=&quot;${claseFila}&quot;" onmouseover="this.className=&quot;destacadoTablas ${claseFila}&quot;" class="${claseFila}" id="${col.columna.tabla.nombre}||${col.nombre}">
							<!--  dependiendo del tipo de atributo, depende la class -->
							<!-- es formula -->
							<c:if test="${col.indicadorExpresion!=null && col.relacion==null}">
								<td><input type="checkbox" class="filasTablaFinal formula" name="marcar_formulas_${numFormus}"/></td>
							</c:if>
							<!-- es campo y es numerico-->
							<c:if test="${col.indicadorExpresion==null && col.columna!=null && col.relacion==null}">
								<td><input type="checkbox" class="filasTablaFinal campo" name="marcar_campos_${numCampos}"/></td>
							</c:if>
							<!-- es relacion o campo no numerico -->
							<c:if test="${(col.indicadorExpresion==null && col.relacion!=null)}">
								<td><input type="checkbox" class="filasTablaFinal relacion" name="marcar_relaciones_${numRelaciones}"/></td>
							</c:if>
							<td>${col.columna.tabla.nombre}</td>
							<td>${col.nombre}</td>
							<td class="centro orden">${col.ordenVisualizacion}</td>
							<!-- formula -->
							<c:if test="${col.indicadorExpresion!=null && col.relacion==null}">
								<td class="centro"><input type="checkbox" id="check_mostrar_formula_${numFormus}" class="check_mostrar" <c:if test="${col.mostrar==true}">checked</c:if>/></td>
								<c:set var="numFormus" value="${numFormus+1}"/>
							</c:if>
							<!-- campo -->
							<c:if test="${col.indicadorExpresion==null && col.columna!=null && col.relacion==null}">
								<td class="centro"><input type="checkbox" id="check_mostrar_campo_${numCampos}" class="check_mostrar" <c:if test="${col.mostrar==true}">checked</c:if>/></td>
								<c:set var="numCampos" value="${numCampos+1}"/>
							</c:if>
							<!--  relacion -->
							<c:if test="${col.indicadorExpresion==null && col.relacion!=null}">
								<td class="centro"><input type="checkbox" id="check_mostrar_relacion_${numRelaciones}" class="check_mostrar" <c:if test="${col.mostrar==true}">checked</c:if>/></td>
								<c:set var="numRelaciones" value="${numRelaciones+1}"/>
							</c:if>
							<!--  criterios -->
							<c:if test="${col.criterio!=null && col.criterio.cadenaCriterio!=''}">
								<td class="centro">
									<a data-columna="${col.criterio.atributo.columna.nombre}" data-tabla="${col.criterio.atributo.columna.tabla.nombre}" data-idfuente="${col.criterio.atributo.columna.tabla.fuente.id}" class="criterio fizq" href="#"><span class="ui-icon ui-icon-circle-plus"></span></a>
									<a title='${col.criterio.cadenaCriterio}' onclick="return false;" style="position:relative;top:-1px" class="criterio_anhadido fder" data-num-criterio="${numCriterios}" href="#"><span class="ui-icon ui-icon-check"></span></a>
									<c:set var="numCriterios" value="${numCriterios+1}"/>
								</td>
							</c:if>
							<c:if test="${col.criterio==null}">
								<!-- es formula -->
								<c:if test="${col.indicadorExpresion!=null && col.relacion==null}">
									<td class="centro"><a data-columna="${col.nombre}" data-tabla="${col.columna.tabla.nombre}" data-idfuente="${col.columna.tabla.fuente.id}" class="criterio fizq" href="#"><span class="ui-icon ui-icon-circle-plus"></span></a></td>
								</c:if>
								<!-- es campo y es numerico-->
								<c:if test="${col.indicadorExpresion==null && col.columna!=null && col.relacion==null}">
									<c:if test="${col.columna.tipoAtributo=='VALORFDNUMERICO' || col.columna.tipoAtributo=='VALORFDTEXTO'}">
										<td class="centro"><a data-columna="${col.columna.nombre}" data-tabla="${col.columna.tabla.nombre}" data-idfuente="${col.columna.tabla.fuente.id}" class="criterio fizq" href="#" onclick="return false;"><span class="ui-icon ui-icon-circle-plus"></span></a></td>
									</c:if>
								</c:if>
								<!-- es relacion o campo no numerico -->
								<c:if test="${(col.indicadorExpresion==null && col.relacion!=null) || (col.columna.tipoAtributo!='VALORFDNUMERICO' && col.indicadorExpresion==null)}">
									<c:if test="${col.columna.tipoAtributo!='VALORFDTEXTO'}">
										<td class="centro">---</td>
									</c:if>
								</c:if>
							</c:if>
						</tr>
					</c:forEach>
				</table>
				</div>
				<div class="clear"></div>
				<a id="quitar_campos" class="boton boton_grande izq10 sup10 fder" href="#"><spring:message code="jsp.indicador.quitar"/></a>
				<a id="anhadir_campo" class="boton boton_grande izq10 sup10 fder" href="#"><spring:message code="jsp.indicador.anhadir.campo"/></a>
				<a id="anhadir_formula" class="boton boton_grande sup10 fder" href="#"><spring:message code="jsp.indicador.anhadir.formula"/></a>
				<div class="clear"></div>
			</div>
		<!-- areacompleta -->
		<div class="clear"></div>
		<div class="botones_accion">
			<a href="indicadores.htm" class="boton izq10 sup10 fondo_blanco fder"><spring:message code="elementos.jsp.boton.cancelar"/></a>
			<a href="#" onclick="indicadores.actualizarFuentesTablas();indicadores.actualizarFilasRelacion();$('#formularioIndicador').submit();return false;" class="boton izq10 sup10 fondo_blanco fder"><spring:message code="elementos.jsp.boton.aceptar"/></a>
		</div>
	</div>	<!--  cuerpoprincipal -->
	
<!--  avisos -->	
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
<!-- añadir relacion -->
<div id="dialogoModalRelacion" style="display: none;background-color: white;">
    <div class="ui-widget">
        <div style="padding: 0.2em 0.7em;" class=""> 
            <div class="inf15">
            	<spring:message code="jsp.indicador.columna.relacionar"/>&nbsp;<span id="col_rel"></span>
            </div>
            <div class="fizq">
            	<label for="rel_tablas"><spring:message code="jsp.indicador.tablas"/></label>
            	<select id="rel_tablas">
            	</select>
            </div>
            <div class="fder">
            	<label for="rel_columnas"><spring:message code="jsp.indicador.columnas"/></label>
            	<select id="rel_columnas">
            		<option value="">...</option>
            	</select>
            </div>
        </div>
    </div>
</div>
<!-- ayuda -->
<div id="dialogoModalAyuda" style="display: none;background-color: white;">
    <div class="ui-widget">
        <div style="padding: 0.2em 0.7em;" class=""> 
            <p>
                <span style="float: left; margin-right: 0.3em;" class=""></span>
                <spring:message code="jsp.indicador.texto.ayuda"/>
            </p>
        </div>
    </div>
</div>		

<!-- añadir campo -->
<div id="dialogoModalNuevoCampo" style="display: none;background-color: white;">
    <div class="ui-widget">
        <div style="padding: 0.2em 0.7em;" class=""> 
            <div class="inf15">
            	<spring:message code="jsp.indicador.anhadir.campo.instrucciones"/>
            </div>
            <div class="linea fizq">
            	<label class="label fizq" for="tablas_campos"><spring:message code="jsp.indicador.tablas"/></label>
            	<select id="tablas_campos">
            	</select>
            </div>
            <div class="clear"></div>
            <div class="linea fizq">
            	<label class="label fizq" id="label_columnas_campos" for="columnas_campos"><spring:message code="jsp.indicador.columnas"/></label>
            	<select id="columnas_campos">
            		<option value="">...</option>
            	</select>
            </div>
            <div class="clear"></div>
            <div class="linea fizq">
            	<label class="label fizq" for="orden_campos"><spring:message code="jsp.indicador.orden"/></label>
            	<input type="text" id="orden_campos" />
            </div>
            <div class="clear"></div>
        </div>
    </div>
</div>

<!-- añadir campo con formula -->
<div id="dialogoModalNuevoCampoFormula" style="display: none;background-color: white;">
    <div class="ui-widget">
        <div style="padding: 0.2em 0.7em;" class=""> 
            <div class="inf15">
            	<spring:message code="jsp.indicador.anhadir.campo.instrucciones.formula"/>
            	<p class="texto_07"><spring:message code="jsp.indicador.anhadir.campo.instrucciones.formula.detalles"/></p>
            </div>
            <div class="fizq">
	            <div class="linea">
	            	<label class="label fizq" for="nombre_formula"><spring:message code="jsp.indicador.nombre"/></label>
	            	<input class="input_pequeno" type="text" id="nombre_formula" />
	            </div>
	            <div class="linea">
	            	<label class="label fizq" for="anhadir_campo_formula"><spring:message code="jsp.indicador.anhadir.campo"/></label>
	            	<select id="anhadir_campo_formula">
	            	</select>
	            	<a id="a_anhadir_campo" href="#"><spring:message code="jsp.indicador.anhadir.campo"/></a>
	            </div>
                <div class="linea">
	            	<label class="label fizq" for="valor_formula"><spring:message code="jsp.indicador.formula"/></label>
	            	<input class="input_med" type="text" id="valor_formula" />
	            </div>	            	            
	            <div class="linea">
	            	<label class="label fizq" for="orden_formula"><spring:message code="jsp.indicador.orden"/></label>
	            	<input class="input_pequeno" type="text" id="orden_formula" />
	            </div> 
            </div>           
        </div>
    </div>
</div>

<!-- añadir criterio a campo -->
<div id="dialogoModalCriterio" style="display: none;background-color: white;">
    <div class="ui-widget">
        <div style="padding: 0.2em 0.7em;" class=""> 
            <div class="inf15">
            	<spring:message code="jsp.indicador.anhadir.campo.instrucciones.criterio"/>
            	<p class="texto_07"><spring:message code="jsp.indicador.anhadir.campo.instrucciones.criterio.detalles"/></p>
            </div>
            <div class="clear"></div>
            <div class="inf15">
            	<spring:message code="jsp.indicador.anhadir.criterio.a"/>&nbsp;<span id="label_anhadir_criterio"></span>
            </div>
            <div class="fizq">
	            <div class="linea">
	            	<a id="a_anhadir_campo_criterio" href="#"><spring:message code="jsp.indicador.anhadir.campo"/></a>
	            	<input type="hidden" id="valor_campo_criterio" />
	            </div>
                <div class="linea">
	            	<label class="label fizq" for="valor_criterio"><spring:message code="jsp.indicador.criterio"/></label>
	            	<input class="input_pequeno" type="text" id="valor_criterio" />
	            </div>
            </div>           
        </div>
    </div>
</div>

<input type="hidden" id="error-ajax" value="<spring:message code="global.formulario.error.ajax"/>" />
<input type="hidden" id="error-caracteres" value="<spring:message code="global.formulario.error.caracteres"/>" />

<script type="text/javascript">
function focusCampo(id){
    var inputField = document.getElementById(id);
    if (inputField != null && inputField.value.length != 0){
        if (inputField.createTextRange){
            var FieldRange = inputField.createTextRange();
            FieldRange.moveStart('character',inputField.value.length);
            FieldRange.collapse();
            FieldRange.select();
        }else if (inputField.selectionStart || inputField.selectionStart == '0') {
            var elemLen = inputField.value.length;
            inputField.selectionStart = elemLen;
            inputField.selectionEnd = elemLen;
            inputField.focus();
        }
    }else{
        inputField.focus();
    }
}

var tablaSeleccionada = "";
var columnaSeleccionada = { tabla: "", columna : "", tipo : "" };
var columnaAmbito = { tabla: "", columna : "", tipo : "" };
var columnaMapa = { tabla: "", columna : "", tipo : "" };

$(document).ready(function() {
	// Se sitúa el foco en el textbox del nombre del indicador
	focusCampo("nombre");
	
	//Seleccionar FUENTE
	$('#fuente_selec').change(function() {indicadores.cargarTablasFuente($('#fuente_selec').val())});
	
	//Anhadir tabla
	$('#anhadir_tabla').click(function() {
		if ( $('#tabla_fuente_selec').val()<=0 ) {
			alert("<spring:message code="js.indicadores.seleccione.tabla"/>");
		} else {
			if ( indicadores.comprobarSiExisteTabla('tabla_'+$('#tabla_fuente_selec').val()))
				alert("<spring:message code="js.indicadores.la.tabla"/> "+$('#tabla_fuente_selec').val()+"<spring:message code="js.indicadores.esta.seleccionada"/>");
			else {
				indicadores.cargarColumnasFuente($('#fuente_selec').val(),$('#tabla_fuente_selec').val());
			}
		}
		return false;
	});
	
	//Seleccionar TABLA
	$('.cabeceraTabla').live("click",function() {
		//deseleccionar tabla
		$('.cabeceraTabla').removeClass("tabla_seleccionada");
		//Seleccionar esta tabla
		$(this).addClass("tabla_seleccionada");
		tablaSeleccionada = $(this).attr("data-tabla");
	});
	
	//Seleccionar COLUMNA
	$('.columnaTabla').live("click",function() {
		//deseleccionar otras columnas
		//var idTabla = $(this).parent().parent().attr("id");
		$(".columnaTabla").removeClass("columna_seleccionada");
		//Seleccionar esta columna
		$(this).addClass("columna_seleccionada");
		columnaSeleccionada = { tabla: "", columna : "", tipo : "" };
		columnaSeleccionada.tabla = $(this).attr("data-tabla");
		columnaSeleccionada.columna = $(this).attr("data-columna");
		columnaSeleccionada.tipo = $(this).attr("data-tipo");
	});
	
	//Seleccionar COLUMNA MAPA
	$('#marcar_mapa').click(function() {
		if ( columnaSeleccionada.tabla == "" && columnaSeleccionada.columna == "" && columnaSeleccionada.tipo == "") {
			alert("<spring:message code="js.indicadores.no.columna.mapa"/>");
			return false;
		}
		if ( columnaSeleccionada.tipo != 'VALORFDGEOGRAFICO') {
			alert("<spring:message code="js.indicadores.columna.mapa.debe.ser.geografico"/>");
			return false;
		}
		
		columnaMapa = columnaSeleccionada;
		
		$('#tit_mapa').fadeIn('slow',function() {$('#tit_mapa').removeClass("oculto");});
		$('#columna_marcada_mapa').html("<div><label>Tabla:</label><span>"+columnaMapa.tabla.substring("tabla_".length)+"</span></div>"+
				"<div><label>Columna:</label><span><strong>"+columnaMapa.columna+"</strong></span></div>");
		$('#columna_marcada_mapa').fadeIn('slow',function() {$('#columna_marcada_mapa').removeClass("oculto");});
		form = "<input type='hidden' id='columna_campo_mapa' name='columna_campo_mapa' value='"+columnaMapa.columna+"'/>";
		form += "<input type='hidden' id='tabla_campo_mapa' name='tabla_campo_mapa' value='"+columnaMapa.tabla.substring("tabla_".length)+"'/>";
		$('#campo_mapa').html("");
		$('#campo_mapa').html(form);
		$('#marcar_mapa').addClass("oculto");
		$('#desmarcar_mapa').removeClass("oculto");
		
		//Al marcar una columna como mapa se añade a la tabla de campos a mostrar
		indicadores.numCampos++;
		var html = "<tr id='"+columnaMapa.tabla.substring("tabla_".length)+"||"+columnaMapa.columna+"' class='' onmouseover='this.className=\"destacadoTablas\"' onmouseout='this.className=\"\"'>";
		var htmlMostrar = "";
		var form = "";
		html += "<td><input name='marcar_campos_"+indicadores.numCampos+"' type='checkbox' class='filasTablaFinal campo'/></td>";
		div = "campos_envio";
		html += "<td>"+columnaMapa.tabla.substring("tabla_".length)+"</td>";
		html += "<td>"+columnaMapa.columna+"</td>";
		htmlMostrar = "<td class='centro'><input class='check_mostrar' type='checkbox' id='check_mostrar_campo_"+indicadores.numCampos+"'/>";
		form = "<input type='hidden' name='tabla_campo_"+indicadores.numCampos+"' id='tabla_campo_"+indicadores.numCampos+"' value='"+columnaMapa.tabla.substring("tabla_".length)+"'/>";
		form += "<input type='hidden' name='columna_campo_"+indicadores.numCampos+"' id='columna_campo_"+indicadores.numCampos+"' value='"+columnaMapa.columna+"'/>";
		form += "<input type='hidden' name='orden_campo_"+indicadores.numCampos+"' id='orden_campo_"+indicadores.numCampos+"' value='2'/>";
		form += "<input type='hidden' name='mostrar_campo_"+indicadores.numCampos+"' id='mostrar_campo_"+indicadores.numCampos+"'/>";
		form += "<input type='hidden' name='idfuente_campo_"+indicadores.numCampos+"' id='idfuente_campo_"+indicadores.numCampos+"' value='"+$("[id='"+columnaMapa.tabla+"']").attr("data-idfuente")+"'/>";
		$('#num_campos_envio').val(indicadores.numCampos);
		
		html += "<td class='centro'>2</td>";
		html += htmlMostrar;
		html += "<td class='centro'>---</td></tr>";
		$('#tabla_campos_mostrar').append(html);
		//Componer formulario
		$('#'+div).append(form);
		
		return false;
	});	
	
	//DESeleccionar COLUMNA MAPA
	$('#desmarcar_mapa').click(function() {
		if ( columnaMapa.tabla == "" && columnaMapa.columna == "" && columnaMapa.tipo == "") {
			alert("<spring:message code="js.indicadores.no.desmarcar.mapa"/>");
			return false;
		}
		
		$('#marcar_mapa').removeClass("oculto");
		$('#desmarcar_mapa').addClass("oculto");
		$('#campo_mapa').html("");
		$('#columna_marcada_mapa').fadeOut();
		$('#tit_mapa').fadeOut();
		
		for (var i = 1; i <= indicadores.numCampos; i++) {
			if ( $('#tabla_campo_' + i).val() == columnaMapa.tabla.substring("tabla_".length) && $('#columna_campo_'+i).val() == columnaMapa.columna ) {
				$('#tabla_campo_' + i).remove();
				$('#columna_campo_' + i).remove();
				$('#orden_campo_' + i).remove();
				$('#mostrar_campo_' + i).remove();
				$('#idfuente_campo_' + i).remove();
				$("[id='"+columnaMapa.tabla.substring("tabla_".length) + "||" + columnaMapa.columna+"']").fadeOut('slow',function() { $(this).remove();});
				break;
			}
		}
		
		//Eliminar fila de los campos para mostrar
		$('#tabla_campos_mostrar tr').each(function() {
			if ($(this).attr("id") == columnaMapa.tabla.substring("tabla_".length) + "||" + columnaMapa.columna) {
				$(this).remove();
				// Se sale del .each
				return false;
			}
		});
		
		columnaMapa = { tabla: "", columna : "", tipo : "" };
		return false;
	});	
	
	//Seleccionar COLUMNA ÁMBITO
	$('#marcar_ambito').click(function() {
		if (columnaSeleccionada.tabla == "" && columnaSeleccionada.columna == "" && columnaSeleccionada.tipo == "") {
			alert("<spring:message code="js.indicadores.no.columna.ambito"/>");
			return false;
		}
		if (columnaSeleccionada.tipo == "VALORFDGEOGRAFICO" || columnaSeleccionada.tipo == "VALORFDRELACION") {
			alert("<spring:message code="js.indicadores.relaciones.geo.no.ambito"/>");
			return false;
		}
		// Si ya está marcada una columna como ámbito
		if (columnaAmbito != null && (columnaAmbito.tabla != "" || columnaAmbito.columna != "" || columnaAmbito.tipo != "")) {
			alert("<spring:message code="js.indicadores.ya.columna.ambito"/>");
			return false;
		}
		columnaAmbito = columnaSeleccionada;
		$('#columna_marcada_ambito').html("<div><label><spring:message code="js.indicadores.tabla"/>:</label><span>"+columnaAmbito.tabla.substring("tabla_".length)+"</span></div>"+
									"<div><label><spring:message code="js.indicadores.columna"/>:</label><span><strong>"+columnaAmbito.columna+"</strong></span></div>");
		$('#columna_marcada_ambito').fadeIn('slow',function() {$('#columna_marcada_ambito').removeClass("oculto");});
		$('#tit_ambito').fadeIn('slow',function() {$('#tit_ambito').removeClass("oculto");});
		form = "<input type='hidden' id='columna_campo_ambito' name='columna_campo_ambito' value='"+columnaAmbito.columna+"'/>";
		form += "<input type='hidden' id='tabla_campo_ambito' name='tabla_campo_ambito' value='"+columnaAmbito.tabla.substring("tabla_".length)+"'/>";
		$('#campo_ambito').html("");
		$('#campo_ambito').html(form);
		
		//Al marcar una columna como ámbito se añade a la tabla de campos a mostrar si es que no existe ya
		if ( !indicadores.existeCampoEnTablaFinal(columnaAmbito.tabla.substring("tabla_".length), columnaAmbito.columna)) {	
			indicadores.numCampos++;
			var html = "<tr id='"+columnaAmbito.tabla.substring("tabla_".length)+"||"+columnaAmbito.columna+"' class='' onmouseover='this.className=\"destacadoTablas\"' onmouseout='this.className=\"\"'>";
			var htmlMostrar = "";
			var form = "";
			html += "<td><input name='marcar_campos_"+indicadores.numCampos+"' type='checkbox' class='filasTablaFinal campo'/></td>";
			div = "campos_envio";
			html += "<td>"+columnaAmbito.tabla.substring("tabla_".length)+"</td>";
			html += "<td>"+columnaAmbito.columna+"</td>";
			htmlMostrar = "<td class='centro'><input class='check_mostrar' checked type='checkbox' id='check_mostrar_campo_"+indicadores.numCampos+"'/>";
			form = "<input type='hidden' name='tabla_campo_"+indicadores.numCampos+"' id='tabla_campo_"+indicadores.numCampos+"' value='"+columnaAmbito.tabla.substring("tabla_".length)+"'/>";
			form += "<input type='hidden' name='columna_campo_"+indicadores.numCampos+"' id='columna_campo_"+indicadores.numCampos+"' value='"+columnaAmbito.columna+"'/>";
			form += "<input type='hidden' name='orden_campo_"+indicadores.numCampos+"' id='orden_campo_"+indicadores.numCampos+"' value='1'/>";
			form += "<input type='hidden' name='mostrar_campo_"+indicadores.numCampos+"' id='mostrar_campo_"+indicadores.numCampos+"' value='true'/>";
			form += "<input type='hidden' name='idfuente_campo_"+indicadores.numCampos+"' id='idfuente_campo_"+indicadores.numCampos+"' value='"+$("[id='"+columnaAmbito.tabla+"']").attr("data-idfuente")+"'/>";
			$('#num_campos_envio').val(indicadores.numCampos);
			
			html += "<td class='centro orden'>1</td>";
			html += htmlMostrar;
			if ( columnaAmbito.tipo != "VALORFDNUMERICO" && columnaAmbito.tipo != "VALORFDTEXTO")
				html += "<td class='centro'>---</td></tr>";
			else
				html += "<td class='centro'><a href='#' onclick='return false;' class='criterio fizq' data-idfuente='"+$("[id='"+columnaAmbito.tabla+"']").attr("data-idfuente")+"' data-tabla='"+columnaAmbito.tabla.substring("tabla_".length)+"' data-columna='"+columnaAmbito.columna+"'><span class='ui-icon ui-icon-circle-plus'></span></a></td></tr>";
			$('#tabla_campos_mostrar').append(html);
			//Componer formulario
			$('#'+div).append(form);
		}
		
		return false;
	});	
	
	//Borrar tabla
	$('#borrar_tabla').click(function() {
		if ( tablaSeleccionada == "" )
			alert("<spring:message code="js.indicadores.tabla.no.seleccionada"/>");
		else {
			var nomTablaArreglado = indicadores.arreglarNombreTablaParaFormula(tablaSeleccionada.substring("tabla_".length));
			
			if ( confirm("<spring:message code="js.indicadores.eliminar.tabla.seleccionada"/>") ) {
				
				// Borra la tabla
				$("[id='"+tablaSeleccionada+"']").parent().fadeOut('slow', 
						function() { 
							$(this).remove();
							if ($('#zona_columnas').children().length == 0 && !$('#zona_tablas').hasClass("oculto")) {
							   $('#zona_tablas').addClass("oculto");
							}
						});
			
				// Borra las relaciones
				$("#zona_tablas table tr.columnaTablaRelacion[data-tabla-rel*='" + tablaSeleccionada.substring("tabla_".length) + "']").remove();
				
				// Borra los campos
				$('#tabla_campos_mostrar tr').each(function() {
					var idFila = $(this).attr("id");
					// Si el Id de la fila es nulo => se traba de la cabecera de la tabla
					if (idFila != null) {
						var tablaNombre = $(this).attr("id").split("||");
						
						//Borramos los inputs ocultos
						$('#campos_envio input').each(function(){
							if ( $(this).val() == tablaSeleccionada.substring("tabla_".length)) {
								var num = $(this).attr("name").substring("tabla_campo_".length);
								$('#tabla_campo_'+num).remove();
								$('#columna_campo_'+num).remove();
								$('#orden_campo_'+num).remove();
								$('#mostrar_campo_'+num).remove();							
								$('#idfuente_campo_'+num).remove();
							}
						});
						$("input[type='hidden'][expresion*='" + nomTablaArreglado + "']").remove();
						/*
						$('#formulas_envio input').each(function(){
							if ( $(this).val().indexOf(nomTablaArreglado)!=-1) {
								var num = $(this).attr("name").substring("nombre_formula_".length);
								$('#nombre_formula_'+num).remove();
								$('#valor_formula_'+num).remove();
								$('#orden_formula_'+num).remove();
								$('#mostrar_formula_'+num).remove();							
							}
						});
						*/
						$('#relaciones_envio input').each(function(){
							if ( $(this).val().indexOf(tablaSeleccionada.substring("tabla_".length))!=-1) {
								var num = $(this).attr("name").substring("tabla_relacion_".length);
								$('#tabla_relacion_'+num).remove();
								$('#columna_relacion_'+num).remove();
								$('#orden_relacion_'+num).remove();
								$('#mostrar_relacion_'+num).remove();
								$('#tabla_relacionada_'+num).remove();
								$('#columna_relacionada_'+num).remove();	
								$('#idfuente_relacionada_'+num).remove();
								$('#idfuente_relacion_'+num).remove();							
							}
						});
						
						$('#criterios_envio input').each(function(){
							if ( $(this).val() == tablaSeleccionada.substring("tabla_".length)) {
								var num = $(this).attr("name").substring("tabla_criterio_".length);
								$('#columna_criterio_'+num).remove();
								$('#tabla_criterio_'+num).remove();
								$('#valor_criterio_'+num).remove();
							}
						});
						
						//Eliminamos las filas
						var encontrado=false;
						for (i=1; i<tablaNombre.length;i++) {
								if (tablaNombre[i]==nomTablaArreglado) {
									encontrado=true;
								}
						}
						if ( tablaNombre[0] == tablaSeleccionada.substring("tabla_".length) || encontrado) {
							$(this).fadeOut('slow',function() {$(this).remove();});
							//Si ademas la columna ambito pertenece a esta tabla. Se elimina el ambito
							if (tablaNombre[0] == columnaAmbito.tabla.substring("tabla_".length) ) {
								$('#campo_ambito').html("");
								$('#columna_marcada_ambito').fadeOut();
								$('#tit_ambito').fadeOut();
								columnaAmbito = { tabla: "", columna : "", tipo : "" };
							}
							//Si ademas la columna mapa pertenece a esta tabla. Se elimina el mapa
							if (tablaNombre[0] == columnaMapa.tabla.substring("tabla_".length) ) {
								$('#marcar_mapa').removeClass("oculto");
								$('#desmarcar_mapa').addClass("oculto");
								$('#campo_mapa').html("");
								$('#columna_marcada_mapa').fadeOut();
								$('#tit_mapa').fadeOut();
								columnaMapa = { tabla: "", columna : "", tipo : "" };
							}				
						}
					}
				});
			}
		}
		tablaSeleccionada = "";
		return false;
	});
	
	//Borrar relacion
	$('#borrar_relacion').click(function() {
		if ( columnaSeleccionada.tabla == "" && columnaSeleccionada.columna == "" ) {
			alert("<spring:message code="js.indicadores.columna.no.seleccionada"/>");
			return false;
		}
		if ( $('.columna_seleccionada').attr("data-tipo")!="VALORFDRELACION" ) {
			alert("<spring:message code="js.indicadores.columna.seleccionada.no.relacion"/>");
			return false;
		}
		if ( confirm("<spring:message code="js.indicadores.eliminar.elemento"/>")) {
			$('.columna_seleccionada').fadeOut('slow',function() {
				$(this).remove();
			});
		}
	});
	
	//Añadir Relacion
	$('#anhadir_relacion').click(function() {	
		if ( columnaSeleccionada.tabla == "" && columnaSeleccionada.columna == "" ) {
			alert("<spring:message code="js.indicadores.columna.no.seleccionada"/>");
			return false;
		}
		if ( $('#zona_columnas table').length == 1) {
			alert("<spring:message code="js.indicadores.no.relacion.consigo"/>");
			return false;
		}
		if ( columnaSeleccionada.tipo == 'VALORFDRELACION') {
			alert("<spring:message code="js.indicadores.no.relacion.ya.relacionada"/>");
			return false;
		}
		
		$('#col_rel').html("<strong>"+columnaSeleccionada.columna+"</strong>");
		indicadores.rellenarTablasRelacion(columnaSeleccionada,"rel_tablas","rel_columnas");
		
		var dialogoModalRelacion= $("#dialogoModalRelacion").dialog({
		    autoOpen: false,
		    show: "blind",
		    hide: "explode",                    
		    width: 600,
		    height: 275,
		    modal: true,
		    resizable: false,
		    title: "<spring:message code='js.indicadores.anhadir.relacion'/>",
		    buttons: {
		        "<spring:message code="elementos.jsp.boton.aceptar"/>": function() {
		            tablaOriginal = columnaSeleccionada.tabla;
		            tabla = $('#rel_tablas').val();
		            col   = $('#rel_columnas').val();
		            fuente = $("[id='tabla_"+tabla+"']").attr("data-idfuente");
		            if (indicadores.existeFilaRelacion(tablaOriginal, tabla, columnaSeleccionada.columna, col)) {
	    				alert("<spring:message code="js.indicadores.no.relacion.ya.relacionada"/>");
	    				return false;
		            } else {
	    				$("[id='"+tablaOriginal+"']").append('<tr data-tabla="'+tablaOriginal+'"'+
	            									 'data-columna="'+columnaSeleccionada.columna+'" data-tipo-columna-relacionada="'+indicadores.obtenerTipoColumna(tabla,col)+'" data-tipo="VALORFDRELACION" data-fuente-rel="'+fuente+'" data-tabla-rel="'+tabla+'" data-col-rel="'+col+'"' + 
	            									 'class="columnaTabla columnaTablaRelacion"><td>'+columnaSeleccionada.columna+'>'+tabla+'.'+col+'</td></tr>');
	    				$(this).dialog( "close" );
		            	return false;
	    			}
		        },
		         "<spring:message code="elementos.jsp.boton.cancelar"/>": function() {
		            $(this).dialog( "close" );
		            return false;            
		        }
		    }            
		});
		$(dialogoModalRelacion).dialog("open");
		return false;
	});
	
	//Cambio de tabla en añadir relacion
	$('#rel_tablas').change(function() {
		$('#rel_columnas').html("");
		$("[id='tabla_"+$(this).val()+"'] tr td").each(function(){
			var nombreCol = $(this).html();
			var obj = new Option(nombreCol,nombreCol);
			$(obj).html(nombreCol);
			$(obj).attr("data-idfuente",$("[id='tabla_"+$('#rel_tablas').val()+"']").attr("data-idfuente"));
			$("#rel_columnas").append(obj);
		});
	});
	
	
	//Ayuda de la página de indicadores
	$('#boton_ayuda').click(function() {
		var dialogoModalAyuda = $("#dialogoModalAyuda").dialog({
		    autoOpen: false,
		    show: "blind",
		    hide: "explode",                    
		    width: 600,
		    height: 475,
		    modal: true,
		    resizable: false,
		    title: "<spring:message code='elementos.jsp.ayuda'/>",
		    buttons: {
		        "<spring:message code="elementos.jsp.boton.aceptar"/>": function() {
		            $(this).dialog( "close" );
		            return false;            
		        }
		    }            
		});
		$(dialogoModalAyuda).dialog("open");
		return false;
	});
	
	//Añadir CAMPO
	$('#anhadir_campo').click(function() {	
		if ( $('#zona_columnas table').length == 0) {
			alert("<spring:message code="js.indicadores.tabla.no.seleccionada"/>");
			return false;
		}
		
		$('#orden_campos').val(indicadores.obtenerSiguienteNumOrden());
		
		indicadores.rellenarTablas(columnaSeleccionada,"tablas_campos","columnas_campos");
		var dialogoModalNuevoCampo = $("#dialogoModalNuevoCampo").dialog({
		    autoOpen: false,
		    show: "blind",
		    hide: "explode",                    
		    width: 675,
		    height: 275,
		    modal: true,
		    resizable: false,
		    title: "<spring:message code='js.indicadores.anhadir.campo'/>",
		    buttons: {
		        "<spring:message code="elementos.jsp.boton.aceptar"/>": function() {
		            if ( indicadores.anhadirCampoTablaFinal())
		            	$(this).dialog( "close" );
		            return false;            
		           
		        },
		         "<spring:message code="elementos.jsp.boton.cancelar"/>": function() {
		            $(this).dialog( "close" );
		            return false;            
		        }
		    }            
		});
		$(dialogoModalNuevoCampo).dialog("open");
		return false;
	});
	
	//Cambio de tabla en añadir campo
	$('#tablas_campos').change(function() {
		$('#columnas_campos').removeClass("oculto");
		$('#label_columnas_campos').removeClass("oculto");
		
		$('#columnas_campos').html("");
		var nombreTabla = $(this).val();
		$("[id='tabla_"+$(this).val()+"'] tr td").each(function(){
			var nombreCol = indicadores.htmlDecode($(this).html());
			if (!indicadores.existeCampoEnTablaFinal(nombreTabla,nombreCol) ) {
				if ( ($(this).parent().attr("data-tipo")!="VALORFDRELACION") && ($(this).parent().attr("data-tipo")!="VALORFDGEOGRAFICO") ) {
					var obj = new Option(nombreCol,nombreCol);
					$(obj).html(nombreCol);
					$(obj).attr("data-tipo","VALORFDNORMAL");
					$("#columnas_campos").append(obj);
				}
			}
		});
	});
	
	//Añadir CAMPO CON FORMULA
	$('#anhadir_formula').click(function() {	
		if ( $('#zona_columnas table').length == 0) {
			alert("<spring:message code="js.indicadores.tabla.no.seleccionada"/>");
			return false;
		}
		
		indicadores.rellenarCamposParaFormula();
		$('#nombre_formula').val("");
		$('#valor_formula').val("");
		$('#orden_formula').val(indicadores.obtenerSiguienteNumOrden());
		
		var dialogoModalNuevoCampoFormula = $("#dialogoModalNuevoCampoFormula").dialog({
		    autoOpen: false,
		    show: "blind",
		    hide: "explode",                    
		    width: 675,
		    height: 375,
		    modal: true,
		    resizable: false,
		    title: "<spring:message code='js.indicadores.anhadir.formula'/>",
		    buttons: {
		        "<spring:message code="elementos.jsp.boton.aceptar"/>": function() {
		            if ( !indicadores.validarFormula() )
		            	return false;
		            else {
			            indicadores.anhadirFormulaTablaFinal();
		            	$(this).dialog( "close" );
			            return false;
		            }		           
		        },
		         "<spring:message code="elementos.jsp.boton.cancelar"/>": function() {
		            $(this).dialog( "close" );
		            return false;            
		        }
		    }            
		});
		$(dialogoModalNuevoCampoFormula).dialog("open");
		return false;
	});
	
	//Anhadir campo al valor de la formula
	$('#a_anhadir_campo').click(function() {
		$('#valor_formula').val($('#valor_formula').val()+$('#anhadir_campo_formula').val());
		
		// Se sitúa el foco al final del texto del TextBox con los datos de la fórmula
		focusCampo("valor_formula");

		return false;
	});
	
	//QUITAR CAMPOS TABLA
	$('#quitar_campos').click(function() {
		$('.filasTablaFinal:checked').each(function() {
			//Quitar los inputs ocultos del formulario
			var num;
			if ( $(this).hasClass("campo")) {
				num = $(this).attr("name").substring('marcar_campos_'.length);
				//Eliminar el ambito si era la columna de ambito
				if ($('#tabla_campo_'+num).val()==columnaAmbito.tabla.substring("tabla_".length) && $('#columna_campo_'+num).val()==columnaAmbito.columna ) {
					$('#campo_ambito').html("");
					$('#columna_marcada_ambito').fadeOut();
					$('#tit_ambito').fadeOut();
					columnaAmbito = { tabla: "", columna : "", tipo : "" };
				}
				
				//Eliminar el mapa si era la columna de mapa
				if ($('#tabla_campo_'+num).val() == columnaMapa.tabla.substring("tabla_".length) && $('#columna_campo_'+num).val() == columnaMapa.columna ) {
					$('#marcar_mapa').removeClass("oculto");
					$('#desmarcar_mapa').addClass("oculto");
					$('#campo_mapa').html("");
					$('#columna_marcada_mapa').fadeOut();
					$('#tit_mapa').fadeOut();
					columnaMapa = { tabla: "", columna : "", tipo : "" };
				}
				
				$('#tabla_campo_'+num).remove();
				$('#columna_campo_'+num).remove();
				$('#orden_campo_'+num).remove();
				$('#mostrar_campo_'+num).remove();
				$('#idfuente_campo_'+num).remove();
				//Retiro los criterios de este campo
				var numCriterio = 0;
				$(this).parent().parent().find(".criterio_anhadido").each(function() {
					numCriterio = $(this).attr("data-num-criterio");
				});
				$('#columna_criterio_'+numCriterio).remove();
				$('#tabla_criterio_'+numCriterio).remove();
				$('#valor_criterio_'+numCriterio).remove();				
				
			} else if ( $(this).hasClass("formula")) {
				num = $(this).attr("name").substring('marcar_formulas_'.length);
				$('#nombre_formula_'+num).remove();
				$('#valor_formula_'+num).remove();
				$('#orden_formula_'+num).remove();
				$('#mostrar_formula_'+num).remove();
				//Retiro los criterios de esta formula
				var numCriterio = 0;
				$(this).parent().parent().find(".criterio_anhadido").each(function() {
					numCriterio = $(this).attr("data-num-criterio");
				});
				$('#columna_criterio_'+numCriterio).remove();
				$('#tabla_criterio_'+numCriterio).remove();
				$('#valor_criterio_'+numCriterio).remove();
			} else if ( $(this).hasClass("relacion")) {
				num = $(this).attr("name").substring('marcar_relaciones_'.length);
				$('#tabla_relacion_'+num).remove();
				$('#columna_relacion_'+num).remove();
				$('#orden_relacion_'+num).remove();
				$('#mostrar_relacion_'+num).remove();
				$('#tabla_relacionada_'+num).remove();
				$('#columna_relacionada_'+num).remove();
				$('#idfuente_relacion_'+num).remove();
				$('#idfuente_relacionada_'+num).remove();
			}
			//eliminar la fila
			$(this).parent().parent().fadeOut('slow',function() {$(this).remove();});
		});
		if ( $('.filasTablaFinal:checked').length <= 0 )
			alert("<spring:message code="js.indicadores.marque.fila.quitar"/>");
		
		return false;
	});
	
	//Propagar cuando se pulsa en los checks de mostrar en la tabla de campos a mostrar
	$('.check_mostrar').live('click',function() {
		var num;
		var valor;
		if ($(this).is(":checked"))
			valor = "true";
		else
			valor = "false";
		
		if ( $(this).attr("id").indexOf("check_mostrar_campo_")!=-1) {
			num = $(this).attr("id").substring("check_mostrar_campo_".length);
			$('#mostrar_campo_'+num).val(valor);
		} else if ( $(this).attr("id").indexOf("check_mostrar_formula_")!=-1) {
			num = $(this).attr("id").substring("check_mostrar_formula_".length);
			$('#mostrar_formula_'+num).val(valor);
		} else if ( $(this).attr("id").indexOf("check_mostrar_relacion_")!=-1) {
			num = $(this).attr("id").substring("check_mostrar_relacion_".length);
			$('#mostrar_relacion_'+num).val(valor);
		}
	});
	
	//AÑADIR CRITERIOS
	$('.criterio').live("click",function() {
		var obj = $(this);
		$('#label_anhadir_criterio').html(obj.attr("data-columna"));
    	$('#valor_campo_criterio').val(obj.attr("data-columna"));
    	//Si tiene 2 'a' es q tiene el '+' para añadir criterios y el '?' con el criterio
    	if ( $(obj).parent().find("a").length == 2 ) {
    		$(obj).parent().find("a").each(function() {
    			if ( $(this).hasClass("criterio_anhadido")) {
    				$('#valor_criterio').val($(this).attr("title"));
    			}
    		})
    	} else {
    		$('#valor_criterio').val("");
    	}
    	    	
		var dialogoModalCriterio = $("#dialogoModalCriterio").dialog({
		    autoOpen: false,
		    show: "blind",
		    hide: "explode",                    
		    width: 575,
		    height: 300,
		    modal: true,
		    resizable: false,
		    title: obj.attr("data-columna"),
		    buttons: {
		        "<spring:message code="elementos.jsp.boton.aceptar"/>": function() {
		        	if ( $('#valor_criterio').val()==null || $('#valor_criterio').val()=="") {
		        		alert("<spring:message code="js.indicadores.criterio.no.vacio"/>");
		        		return false;
		        	}
		        	indicadores.anhadirCriterioTablaFinal(obj.attr("data-columna"),obj);
	            	$(this).dialog( "close" );
		            return false;            
		           
		        },
		         "<spring:message code="elementos.jsp.boton.cancelar"/>": function() {
		            $(this).dialog( "close" );
		            return false;            
		        }
		    }            
		});
		$(dialogoModalCriterio).dialog("open");
	});
	
	$('#a_anhadir_campo_criterio').live("click",function() {
		$('#valor_criterio').val($('#valor_criterio').val()+"[*]");
		focusCampo("valor_criterio");
		return false;
	});
});

var indicadores = {
	url 				: 'fuentes.htm',
	numCampos 			: 0,
	numFormulas			: 0,
	numRelaciones   	: 0,
	numCriterios    	: 0,
	fuentestablas		: '',
	relacionestablas	: '',
	
	/*
	 * CARGAR TABLAS FUENTES
	 */
	cargarTablasFuente: function(fuente) {
		fuente = fuente.split("||");
		if ( fuente[0] > 0 ) {
			$('#cargando_tablas').removeClass("oculto");
			$.ajax({
				   type: "POST",
				   url: indicadores.url,
				   data: "accion=listaTablas&fuente="+fuente[0]+"&tipo="+fuente[1],
				   success: function(msg){
					   $('#cargando_tablas').addClass("oculto");
					   
					   var res = msg.split("||");
					   if ( res[0] == 'correcto' ) {
						   var json = JSON.parse(res[1]);
						   indicadores.montarListaTablas(json,fuente[0]);
					   } else {
						   alert ($('#error-ajax').val()); 
					   }
					   
					}
			});
		}
	},
	
	/*
	 * CARGAR COLUMNAS FUENTE
	 */
	cargarColumnasFuente: function(fuente,tabla) {
		fuente = fuente.split("||");
		$('#cargando_columnas').removeClass("oculto");
		if ( fuente[0] > 0 ) {
			$.ajax({
				   type: "POST",
				   url: indicadores.url,
				   data: "accion=listaColumnas&fuente="+fuente[0]+"&tipo="+fuente[1]+"&tabla="+tabla,
				   success: function(msg){
					   $('#cargando_columnas').addClass("oculto");
					   var res = msg.split("||");
					   if( res[0] == 'error'){
					   		alert ($('#error-caracteres').val());
					   		var json = JSON.parse(res[1]);
						    indicadores.montarListaColumnasTabla(json,fuente[0],tabla);
						    if ($('#zona_tablas').hasClass("oculto")) {
								$('#zona_tablas').removeClass("oculto");
						    }
				   	   }else{
						   if ( res[0] == 'correcto' ) {
							   var json = JSON.parse(res[1]);
							   indicadores.montarListaColumnasTabla(json,fuente[0],tabla);
							   if ($('#zona_tablas').hasClass("oculto")) {
									$('#zona_tablas').removeClass("oculto");
							   }
						   } else {
							   
							   	alert ($('#error-ajax').val()); 
						   }
				   	   }
					}
			});
		}
	},
	
	/*
	 * MONTAR LISTA PARA COMBO DE TABLAS
	 */
	montarListaTablas: function(json,idFuente) {
		$("#tabla_fuente_selec").html("");
		$.each(json, function(key,value) { 
			if (value.esquema != null && value.esquema != ''){
				var obj = new Option(value.esquema+"."+value.nombre, value.esquema+"."+value.nombre);
			}
			else{
				var obj = new Option(value.nombre, value.nombre);
			}
			if (value.esquema != null && value.esquema != ''){
				$(obj).html(value.esquema+"."+value.nombre);
			}
			else{
				$(obj).html(value.nombre);
			}
			$("#tabla_fuente_selec").append(obj);
		});
	},
	
	/*
	 * MONTAR LISTA COLUMNAS TABLA PARA MOSTRAR
	 */
	montarListaColumnasTabla: function(json,idFuente,tabla) {
		//Montar tabla
		var htmlTabla = '<div class="fizq"><table data-idfuente="'+idFuente+'" id="tabla_'+tabla+'" class="tabla_indicador izq10 inf15" cellpadding="0" cellspacing="0">'+	
						'<tr>'+
						'<th scope="col" class="cabeceraTabla" data-tabla="tabla_'+tabla+'">'+tabla+'</th>'+
						'</tr>';
		$.each(json, function(key,value) {
			htmlTabla += '<tr class="columnaTabla" data-tipo="'+value.tipoAtributo+'" data-columna="'+value.nombre+'" data-tabla="tabla_'+tabla+'"><td>'+value.nombre+'</td></tr>';
		});
		indicadores.fuentestablas += idFuente + "|" + tabla + ",";
		htmlTabla += "</table></div>";
		//Añadir tabla
		$('#zona_columnas').prepend(htmlTabla);
		$('#num_fuentes_tablas_envio').val(indicadores.fuentestablas);
		
	},
	
	/*
	 * COMPROBAR SI UNA TABLA EXISTE YA EN LAS TABLAS UTILIZADAS
	 */
	comprobarSiExisteTabla: function(tabla) {
		var resp = false;
		$('#zona_columnas table').each(function(){
			if ($(this).attr("id") == tabla)
				resp = true;
		});
		return resp;
	},
	
	
	/*
	 * METODO QUE ACTUALIZA LA VARIABLE INDICADORES DESPUES DE ELIMINAR UNA TABLA.
	 * SE LLAMA ANTES DE HACER EL SUBMIT
	*/
	actualizarFuentesTablas: function() {
		indicadores.fuentestablas = '';
		$('#zona_columnas table').each(function() {
			var nombreTabla = $(this).attr("id").substring("tabla_".length);
			var idFuente = $(this).attr("data-idfuente");
			indicadores.fuentestablas += idFuente + "|" + nombreTabla + ",";
		});
		$('#num_fuentes_tablas_envio').val(indicadores.fuentestablas);
	},
	
	/*
	 * METODO QUE REVISA CUANTAS FILAS DE CADA TABLA SON RELACIÓN.
	 * SE LLAMA ANTES DE HACER EL SUBMIT
	*/
	actualizarFilasRelacion: function() {
		indicadores.relacionestablas = '';
		indicadores.numRelaciones = 0;
		$('#zona_columnas table').each(function() {
			var nombreTabla = $(this).attr("id").substring("tabla_".length);
			var idFuente = $(this).attr("data-idfuente");
			$("[id='tabla_"+nombreTabla+"'] tr td").each(function() {
				var tipo = $(this).parent().attr("data-tipo");
					if ( tipo == "VALORFDRELACION") {
						var columOrigen = $(this).parent().attr("data-columna");
						var tablaOrigen = $(this).parent().attr("data-tabla");
						var columRelacion = $(this).parent().attr("data-col-rel");
						var tablaRelacion = $(this).parent().attr("data-tabla-rel");
						var tipoDatoRelacion = $(this).parent().attr("data-tipo-columna-relacionada");
						var fuenteRelacion = $(this).parent().attr("data-fuente-rel");
						indicadores.numRelaciones++;
						indicadores.relacionestablas += idFuente + "|" + tablaOrigen + "|" + columOrigen + "|" + tipoDatoRelacion + "|" + fuenteRelacion + "|" + tablaRelacion + "|" + columRelacion + ",";
					}
			});
		});
		$('#num_relaciones_envio').val(indicadores.numRelaciones);
		$('#num_relacionestablas_envio').val(indicadores.relacionestablas);
	},
	
	
	/*
	 * RELLENAR TABLAS RELACION
	 */
	rellenarTablasRelacion: function(columna, idTablas, idColumnas) {
		//primero obtengo todas las tablas que no sean la q esta seleccionada
		var primero = true;
		$('#'+idTablas).html("");
		$('#'+idColumnas).html("");
		$('#zona_columnas table').each(function(){
			if ( $(this).attr("id") != columna.tabla) {
				var nombreTabla = $(this).attr("id").substring("tabla_".length);
				var obj = new Option(nombreTabla,nombreTabla);
				$(obj).html(nombreTabla);
				$(obj).attr("data-idfuente",$("[id='tabla_"+nombreTabla+"']").attr("data-idfuente"));
				$("#"+idTablas).append(obj);
				if ( primero ) {
					primero=false;
					$("[id='"+$(this).attr("id")+"'] tr td").each(function(){
						var nombreCol = $(this).html();
						var obj = new Option(nombreCol,nombreCol);
						$(obj).html(nombreCol);
						$(obj).attr("data-idfuente",$("[id='tabla_"+nombreTabla+"']").attr("data-idfuente"));
						$("#"+idColumnas).append(obj);
					});
				}
			}
		});
	},
	
	/*
	 * RELLENAR TABLAS PARA LISTA DE TABLAS
	 */
	rellenarTablas: function(columna, idTablas, idColumnas) {
		var primero = true;
		$('#'+idTablas).html("");
		$('#'+idColumnas).html("");
		$('#zona_columnas table').each(function(){
			var nombreTabla = $(this).attr("id").substring("tabla_".length);
			var obj = new Option(nombreTabla,nombreTabla);
			$(obj).html(nombreTabla);
			$("#"+idTablas).append(obj);
			if ( primero ) {
				primero=false;
				$("[id='"+$(this).attr("id")+"'] tr td").each(function(){
					var nombreCol = $(this).html();
					if ( !indicadores.existeCampoEnTablaFinal(nombreTabla,nombreCol)) {
						if ( ($(this).parent().attr("data-tipo")!="VALORFDRELACION") && ($(this).parent().attr("data-tipo")!="VALORFDGEOGRAFICO")) {
							var obj = new Option(nombreCol,nombreCol);
							$(obj).html(nombreCol);
							$(obj).attr("data-tipo","VALORFDNORMAL");
							$("#"+idColumnas).append(obj);
						}
					}
				});
			}
		});
	},
	
	/*
	 * ANHADIR CAMPO A LA TABLA FINAL
	 */
	anhadirCampoTablaFinal: function() {
		if ( $('#columnas_campos').find(":selected").val()==null || $('#columnas_campos').find(":selected").val()=="") {
			alert("<spring:message code="js.indicadores.no.columnas.disponibles"/>");
			return false;
		}
		if ( $('#orden_campos').val()==null || $('#orden_campos').val()=="" || !indicadores.esNumero($('#orden_campos').val()) || $('#orden_campos').val()<0 ) {
			alert("<spring:message code="js.indicadores.indique.orden"/>");
			return false;
		}
		
		if ( indicadores.existeOrdenTablaFinal($('#orden_campos').val())) {
			alert("<spring:message code="js.indicadores.indique.orden.no.uso"/>");
			return false;
		}
		
		var form = "";
		var div = "";
		var html = "<tr id='"+$('#tablas_campos').val()+"||"+$('#columnas_campos').val()+"' class='' onmouseover='this.className=\"destacadoTablas\"' onmouseout='this.className=\"\"'>";
		var htmlMostrar = "";
		if ( $('#columnas_campos').find(":selected").attr("data-tipo") == "VALORFDRELACION" ) {
			//Campos de relaciones
			indicadores.numRelaciones++;
			html += "<td><input name='marcar_relaciones_"+indicadores.numRelaciones+"' type='checkbox' class='filasTablaFinal relacion'/></td>";
			div = "relaciones_envio";
			html += "<td>"+$('#tablas_campos').val()+"</td>";
			html += "<td>"+$('#columnas_campos').val()+"</td>";
			htmlMostrar = "<td class='centro'><input class='check_mostrar' type='checkbox' id='check_mostrar_relacion_"+indicadores.numRelaciones+"'/>";
			form = "<input type='hidden' name='tabla_relacion_"+indicadores.numRelaciones+"' id='tabla_relacion_"+indicadores.numRelaciones+"' value='"+$('#columnas_campos').find(":selected").attr("data-tabla").substring("tabla_".length)+"'/>";
			form += "<input type='hidden' name='idfuente_relacion_"+indicadores.numRelaciones+"' id='idfuente_relacion_"+indicadores.numRelaciones+"' value='"+$('[id="tabla_'+$('#columnas_campos').find(":selected").attr("data-tabla").substring("tabla_".length)+'"]').attr("data-idfuente")+"'/>";
			form += "<input type='hidden' name='columna_relacion_"+indicadores.numRelaciones+"' id='columna_relacion_"+indicadores.numRelaciones+"' value='"+$('#columnas_campos').find(":selected").attr("data-col")+"'/>";
			form += "<input type='hidden' name='orden_relacion_"+indicadores.numRelaciones+"' id='orden_relacion_"+indicadores.numRelaciones+"' value='"+$('#orden_campos').val()+"'/>";
			form += "<input type='hidden' name='mostrar_relacion_"+indicadores.numRelaciones+"' id='mostrar_relacion_"+indicadores.numRelaciones+"'/>";
			form += "<input type='hidden' name='tabla_relacionada_"+indicadores.numRelaciones+"' id='tabla_relacionada_"+indicadores.numRelaciones+"' value='"+$('#columnas_campos').find(":selected").attr("data-tabla-rel")+"'/>";
			form += "<input type='hidden' name='columna_relacionada_"+indicadores.numRelaciones+"' id='columna_relacionada_"+indicadores.numRelaciones+"' value='"+$('#columnas_campos').find(":selected").attr("data-col-rel")+"'/>";
			form += "<input type='hidden' name='idfuente_relacionada_"+indicadores.numRelaciones+"' id='idfuente_relacionada_"+indicadores.numRelaciones+"' value='"+$('[id="tabla_'+$('#columnas_campos').find(":selected").attr("data-tabla-rel")+'"]').attr("data-idfuente")+"'/>";
			$('#num_relaciones_envio').val(indicadores.numRelaciones);
		} else {
			//Campos normales
			indicadores.numCampos++;
			html += "<td><input name='marcar_campos_"+indicadores.numCampos+"' type='checkbox' class='filasTablaFinal campo'/></td>";
			div = "campos_envio";
			html += "<td>"+$('#tablas_campos').val()+"</td>";
			html += "<td>"+$('#columnas_campos').val()+"</td>";
			htmlMostrar = "<td class='centro'><input class='check_mostrar' type='checkbox' checked='checked' id='check_mostrar_campo_"+indicadores.numCampos+"'/>";
			form = "<input type='hidden' name='tabla_campo_"+indicadores.numCampos+"' id='tabla_campo_"+indicadores.numCampos+"' value='"+$('#tablas_campos').val()+"'/>";
			form += "<input type='hidden' name='columna_campo_"+indicadores.numCampos+"' id='columna_campo_"+indicadores.numCampos+"' value='"+$('#columnas_campos').val()+"'/>";
			form += "<input type='hidden' name='orden_campo_"+indicadores.numCampos+"' id='orden_campo_"+indicadores.numCampos+"' value='"+$('#orden_campos').val()+"'/>";
			form += "<input type='hidden' name='mostrar_campo_"+indicadores.numCampos+"' id='mostrar_campo_"+indicadores.numCampos+"' value='true'/>";
			form += "<input type='hidden' name='idfuente_campo_"+indicadores.numCampos+"' id='idfuente_campo_"+indicadores.numCampos+"' value='"+$("[id='tabla_"+$('#tablas_campos').val()+"']").attr("data-idfuente")+"'/>";
			$('#num_campos_envio').val(indicadores.numCampos);
		}

		html += "<td class='centro orden'>"+$('#orden_campos').val()+"</td>";
		html += htmlMostrar;
		
		var criterios = false;
		var nombreTabla = "";
		//Averiguar el tipo de datos de la columna elegida.
		$('#zona_columnas table').each(function(){
			nombreTabla = $(this).attr("id").substring("tabla_".length);
			if ( nombreTabla == $('#tablas_campos').val()) {
				$("[id='tabla_"+nombreTabla+"'] tr td").each(function(){
					if ( $(this).html()==$('#columnas_campos').val()) {
						var tipo = $(this).parent().attr("data-tipo");
						if ( tipo == "VALORFDNUMERICO" || tipo == "VALORFDTEXTO")
							criterios = true;
					}
				});
			}
		});
		
		if ( !criterios )		
			html += "<td class='centro'>---</td></tr>";
		else
			html += "<td class='centro'><a href='#' onclick='return false;' class='criterio fizq' data-idfuente='"+$("[id='tabla_"+nombreTabla+"']").attr("data-idfuente")+"' data-tabla='"+$('#tablas_campos').val()+"' data-columna='"+$('#columnas_campos').val()+"'><span class='ui-icon ui-icon-circle-plus'></span></a></td></tr>";
	
		$('#tabla_campos_mostrar').append(html);
		//Componer formulario
		$('#'+div).append(form);
		return true;
	},
	
	/*
	 * ANHADIR FORMULA A LA TABLA FINAL
	 */
	anhadirFormulaTablaFinal: function() {
		indicadores.numFormulas++;
		var form = "";
		var div = "";
		
		var html = "<tr id='"+$('#valor_formula').val()+"||"+$('#orden_formula').val()+"' class='' onmouseover='this.className=\"destacadoTablas\"' onmouseout='this.className=\"\"'>";
		html += "<td><input type='checkbox' class='filasTablaFinal formula' name='marcar_formulas_"+indicadores.numFormulas+"'/></td>";
		html += "<td>"+indicadores.eliminarComillasSimples($('#valor_formula').val())+"</td>";
		div = "formulas_envio";
		html += "<td>"+$('#nombre_formula').val()+"</td>";
		html += "<td class='centro orden'>"+$('#orden_formula').val()+"</td>";
		html += "<td class='centro'><input class='check_mostrar' type='checkbox' checked='checked' id='check_mostrar_formula_"+indicadores.numFormulas+"'/>";
		html += "<td class='centro'><a href='#' onclick='return false;' class='criterio fizq' data-tabla='"+indicadores.eliminarComillasSimples($('#valor_formula').val())+"' data-columna='"+$('#nombre_formula').val()+"'><span class='ui-icon ui-icon-circle-plus'></span></a></td></tr>";
		$('#tabla_campos_mostrar').append(html);		

		//Componer formulas
		var valorFormula = indicadores.eliminarComillasSimples($('#valor_formula').val());
		form += "<input type='hidden' expresion='" + valorFormula + "' name='valor_formula_"+indicadores.numFormulas+"' id='valor_formula_"+indicadores.numFormulas+"' value='"+valorFormula+"'/>";
		form += "<input type='hidden' expresion='" + valorFormula + "' name='nombre_formula_"+indicadores.numFormulas+"' id='nombre_formula_"+indicadores.numFormulas+"' value='"+$('#nombre_formula').val()+"'/>";
		form += "<input type='hidden' expresion='" + valorFormula + "' name='orden_formula_"+indicadores.numFormulas+"' id='orden_formula_"+indicadores.numFormulas+"' value='"+$('#orden_formula').val()+"'/>";
		form += "<input type='hidden' expresion='" + valorFormula + "' name='mostrar_formula_"+indicadores.numFormulas+"' id='mostrar_formula_"+indicadores.numFormulas+"' value='true' />";
		$('#num_formulas_envio').val(indicadores.numFormulas);
		$('#'+div).append(form);
	},
	
	/*
	 * VALIDAR FORMULA
	 */
	validarFormula: function() {
		
		var noTieneMayor = "^[^>]+$";
		var noTieneMenor = "^[^<]+$";
		
		var noTieneIgual = "^[^=]+$";
		
		
		if ( ($('#nombre_formula').val()=="" || $('#nombre_formula').val().length<=0) ||
			 ($('#valor_formula').val()=="" || $('#valor_formula').val().length<=0)	) {
			alert("<spring:message code="js.indicadores.formula.error.validacion"/>");
			return false;
		}
		
		if ( $('#orden_formula').val()==null || $('#orden_formula').val()=="" || !indicadores.esNumero($('#orden_formula').val()) || $('#orden_campos').val()<0 ) {
			alert("<spring:message code="js.indicadores.indique.orden"/>");
			return false;
		}
		
		if ( indicadores.existeOrdenTablaFinal($('#orden_formula').val())) {
			alert("<spring:message code="js.indicadores.indique.orden.no.uso"/>");
			return false;
		}
		
		if ( (!($('#valor_formula').val()).match(noTieneMayor)) || (!($('#valor_formula').val()).match(noTieneMenor)) || (!($('#valor_formula').val()).match(noTieneIgual)) ) {
			alert("<spring:message code="js.indicadores.formula.caracteres.validacion"/>");
			return false;
		}  
		
		return true;
	},
	
	/*
	 * METODO QUE REVISA TODAS LAS RELACIONES EXISTENTES PARA COMPROBAR SI LA RELACIÓN
	 * QUE SE VA A INCORPORAR YA EXISTE EN LA TABLA
	*/
	existeFilaRelacion: function(strTablaOrigen, strTablaRelacion, strColumnaOrigen, strColumnaRelacion) {
		
		var existeRelacion = false;		
		$('#zona_columnas table').each(function() {
			var nombreTabla = $(this).attr("id").substring("tabla_".length);
			var idFuente = $(this).attr("data-idfuente");
			$("[id='tabla_"+nombreTabla+"'] tr td").each(function() {
				var tipo = $(this).parent().attr("data-tipo");
					if ( tipo == "VALORFDRELACION") {
						var columOrigen = $(this).parent().attr("data-columna");
						var tablaOrigen = $(this).parent().attr("data-tabla");
						var columRelacion = $(this).parent().attr("data-col-rel");
						var tablaRelacion = $(this).parent().attr("data-tabla-rel");
						var tipoDatoRelacion = $(this).parent().attr("data-tipo-columna-relacionada");
						var fuenteRelacion = $(this).parent().attr("data-fuente-rel");
						if ((tablaOrigen == strTablaOrigen) && (tablaRelacion == strTablaRelacion)) {
							if ((columOrigen == strColumnaOrigen) && (columRelacion == strColumnaRelacion))	 {
								existeRelacion = true;								
							}
						}
					}
			});
		});
		return existeRelacion;
	},
	
	
	/*
	 * RELLENAR SELECT CAMPOS PARA FORMULA 
	 */
	rellenarCamposParaFormula: function() {
		$("#anhadir_campo_formula").html("");
		$('#zona_columnas table').each(function(){
			var nombreTabla = $(this).attr("id").substring("tabla_".length);
			var idFuente = $(this).attr("data-idfuente");
			var html = "<optgroup label='"+nombreTabla+"'>";
			$("#anhadir_campo_formula").append(html);
			$("[id='"+$(this).attr("id")+"'] tr td").each(function(){
				var nombreCol = $(this).html();
				if ( $(this).parent().attr("data-tipo") == "VALORFDNUMERICO" ) {
					var htmlOption = "<option value='["+idFuente+"||"+indicadores.arreglarNombreTablaParaFormula(nombreTabla)+"||"+nombreCol+"]'>"+nombreCol+"</option>";
					$("#anhadir_campo_formula").append(htmlOption);
				}
			});
			$("#anhadir_campo_formula").append("</optgroup>");						
		});	
	},
	
	/*
	 * COMPROBAR SI EL CAMPO Y LA TABLA YA ESTAN ELEGIDOS EN LA TABLA FINAL 
	 */
	existeCampoEnTablaFinal : function (tabla, campo) {
		var res = false;
		$('#tabla_campos_mostrar tr').each(function() {
			if ($(this).attr("id") != null) {
				var tablaCampo = $(this).attr("id").split("||");
				if ( tablaCampo[0] == tabla && tablaCampo[1] == campo )
					res = true;
			}
		});
		return res;
	},
	
	/*
	 * COMPROBAR SI UNA TABLA CONTIENE UN CAMPO 
	 */
	existeColumnaTabla : function (tabla, columna) {
		var res = false;
		$('#zona_columnas table').each(function() {
			var nombreTabla = $(this).attr("id").substring("tabla_".length);
			if ( nombreTabla.toLowerCase() == tabla.toLowerCase() ) {
				$("[id='tabla_"+tabla+"'] tr").each(function() {
					if ( $(this).attr("data-columna") ) {
						if ( columna.toLowerCase() == $(this).attr("data-columna").toLowerCase()) {
							res = true;
							return true;
						}	
					}
				});
			}				
		});
		return res;
	},
	
	obtenerTipoColumna : function(tabla,columna) {
		var resultado = "";
		$('#zona_columnas table').each(function() {
			var nombreTabla = $(this).attr("id").substring("tabla_".length);
			if ( nombreTabla == tabla ) {
				$("[id='tabla_"+tabla+"'] tr").each(function() {
					if ( columna == $(this).attr("data-columna")) {
						resultado = $(this).attr("data-tipo");
					}
				});
			}				
		});
		return resultado;
	},
	
	/*
	 * ANHADIR CRITERIO
	 */
	anhadirCriterioTablaFinal : function(columna, obj) {
		var div = "criterios_envio";
		var form = "";
		
		var encontrado = false;
		$(obj).parent().find("a.criterio_anhadido").each(function() {
			encontrado = true;
		});
		if ( encontrado ) {
			$(obj).parent().find("a.criterio_anhadido").attr("title",indicadores.eliminarComillasSimples($('#valor_criterio').val()));
			var num = $(obj).parent().find("a.criterio_anhadido").attr("data-num-criterio");
			$('#valor_criterio_'+num).val(indicadores.eliminarComillasSimples($('#valor_criterio').val()));
		}
		else {
			$(obj).parent().append("<a href='#' data-num-criterio='"+indicadores.numCriterios+"' style='position:relative;top:-1px' class='criterio_anhadido fder' onclick='return false;' title='"+indicadores.eliminarComillasSimples($('#valor_criterio').val())+"'><span class='ui-icon ui-icon-check'></span></a>");
			//Componer criterios
			form += "<input type='hidden' name='columna_criterio_"+indicadores.numCriterios+"' id='columna_criterio_"+indicadores.numCriterios+"' value='"+$(obj).attr("data-columna")+"'/>";
			form += "<input type='hidden' name='tabla_criterio_"+indicadores.numCriterios+"' id='tabla_criterio_"+indicadores.numCriterios+"' value='"+$(obj).attr("data-tabla")+"'/>";
			form += "<input type='hidden' name='valor_criterio_"+indicadores.numCriterios+"' id='valor_criterio_"+indicadores.numCriterios+"'/>";
			$('#num_criterios_envio').val(indicadores.numCriterios);
			$('#'+div).append(form);
			$('#valor_criterio_'+indicadores.numCriterios).val(indicadores.eliminarComillasSimples($('#valor_criterio').val()));
		}
		indicadores.numCriterios++;
	},
	
	existeOrdenTablaFinal : function(orden) {
		if ( orden == 1 || orden == 2 ) {
			return true;
		}
		var encontrado = false;
		$('#tabla_campos_mostrar td.orden').each(function() {
			if ( $(this).html()==orden)
				encontrado = true;
		});
		return encontrado;
	},
	
	obtenerSiguienteNumOrden: function() {
		var siguienteOrden = 2;
		$('#tabla_campos_mostrar td.orden').each(function() {
			if ($(this).html() > siguienteOrden)
				siguienteOrden = Number($(this).html());
		});
		
		return Number(siguienteOrden + 1);
	},
	
	esNumero : function (n) {
	  return !isNaN(parseFloat(n)) && isFinite(n);
	},
	
	arreglarNombreTablaParaFormula : function(formula) {
		formula = indicadores.replaceAll(formula,"\\-","");
		formula = indicadores.replaceAll(formula,"\\+","");
		formula = indicadores.replaceAll(formula,"\\*","");
		formula = indicadores.replaceAll(formula,"\\/","");
		return formula;
	},
	
	eliminarComillasSimples : function(texto) {
		texto = indicadores.replaceAll(texto,"'","");
		return texto;
	},
	replaceAll : function(texto,cambia,por_esto) {
		return texto.replace(new RegExp(cambia, 'g'),por_esto);
	},
	htmlDecode : function (value){
		return $('<div/>').html(value).text();
	},
	htmlEncode : function (value){
		return $('<div/>').text(value).html();
	}		
};
indicadores.relacionestablas = "${relacionestablas}";
indicadores.fuentestablas = "${fuentestablas}";
indicadores.numCampos = ${numCampos};
indicadores.numFormulas = ${numFormulas};
indicadores.numRelaciones = ${numRelaciones};
indicadores.numCriterios = ${numCriterios};	

// Se inicializa el atributo que se correspondec con el campo Ámbito, si lo hay
<c:if test="${tabla_ambito != null && columna_ambito != null }">
	columnaAmbito.tabla = "tabla_${tabla_ambito}";
	columnaAmbito.columna = "${columna_ambito}";
	
	// Se obtiene el tipo de la columna
	<c:if test="${not empty listaColumnasIndicador}">
		<c:forEach items="${listaColumnasIndicador}" var="col">
			<c:if test="${col != null && col.columna.tabla.nombre == tabla_ambito && col.nombre == columna_ambito}">
			columnaAmbito.tipo = "${col.columna.tipoAtributo}";
			</c:if>
		</c:forEach>
	</c:if>
	
</c:if>

// Se inicializa el atributo que se correspondec con el campo Mapa, si lo hay
<c:if test="${tabla_mapa != null && columna_mapa != null }">
	columnaMapa.tabla = "tabla_${tabla_mapa}";
	columnaMapa.columna = "${columna_mapa}";
	columnaMapa.tipo = "VALORFDGEOGRAFICO";
</c:if>

</script>