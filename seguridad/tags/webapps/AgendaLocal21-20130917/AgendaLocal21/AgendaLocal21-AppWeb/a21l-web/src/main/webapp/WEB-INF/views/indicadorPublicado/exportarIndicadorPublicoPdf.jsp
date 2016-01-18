<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="es.dc.a21l.fuente.cu.AtributoDto" %>
<%@ page import="es.dc.a21l.fuente.cu.AtributoFuenteDatosDto" %>
<%@ page import="es.dc.a21l.base.utils.enumerados.TipoAtributoFD" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script src="js/util/comun/cambioMenus.js"></script>

<!--  color picker -->
<script type="text/javascript" src="js/colorpicker/farbtastic.js"></script>
<link rel="stylesheet" href="js/colorpicker/farbtastic.css" type="text/css" />
 
<div class="cuerpoprincipal">
	<div class="areacompleta">
		<h2 class="inf15"><spring:message code="jsp.exportar.pdf.titulo"/> ${indicadorDto.nombre}</h2>
		
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
		
		<form id="formulario" action="indicadores.htm" method="post" name="formulario" target="_blank">
			<input type="hidden" name="accion" value="generarPdf"/>
			<input type="hidden" name="id" value="${indicadorDto.id}"/>
 			<input type="hidden" name="idHistorico" value="${idHistorico}"/>
 			<input type="hidden" name="origenExportacionPublico" value="${origenExportacionPublico}"/>
			<c:forEach items="${camposChkDiagrama}" var="elto" varStatus="cont">
				<input type="hidden" id="campo_${elto}" name="campo_${elto}" value="${elto}" />
			</c:forEach>
		<fieldset id="zona_columnas">
			<div>
				<span><spring:message code="jsp.exportar.pdf.subtitulo"/></span>
			</div>
			<!-- lista de tablas asociadas al indicador -->
			<div class="fizq">
			<table cellspacing="0" cellpadding="0" class="tabla_exportar_pdf izq10 inf15 fizq" id="tabla_${tabla.nombre}" data-idfuente="${tabla.fuente.id}">
				<tbody>
					<tr>
						<th class="cabeceraTabla" scope="col"><spring:message code="jsp.exportar.pdf.parametros.exportar"/></th>
						<th class="cabeceraTabla" scope="col"><spring:message code="jsp.exportar.pdf.tabla.valores"/></th>
						<th class="cabeceraTabla" scope="col"><spring:message code="jsp.exportar.pdf.diagrama.barras"/></th>
						<th class="cabeceraTabla" scope="col"><spring:message code="jsp.exportar.pdf.diagrama.sectores"/></th>
						<th class="cabeceraTabla" scope="col"><spring:message code="jsp.exportar.pdf.mapa.tematico"/></th>
					</tr>
					<c:forEach items="${listaColumnas}" var="columna"> 
					<%
						pageContext.setAttribute("esNumerico", false);
						AtributoDto columnaAUtilizar = (AtributoDto) pageContext.getAttribute("columna");
						if (columnaAUtilizar != null) {
							AtributoFuenteDatosDto atributo = columnaAUtilizar.getColumna();
							if (atributo != null) {
								boolean esNumerico = TipoAtributoFD.VALORFDNUMERICO.equals(atributo.getTipoAtributo());
								pageContext.setAttribute("esNumerico", esNumerico);
							}
						}
					%>
						<tr data-columna="${columna.nombre}" data-tipo="${columna.columna.tipoAtributo}" class="columnaTabla">
							<td class="exportar-parametros">${columna.nombre}</td>
							<td class="exportar-tabla"><input type="checkbox" name="tabla_${columna.nombre}" id="tabla_${columna.nombre}" <c:if test="${columna.esMapa}">disabled</c:if> /></td>
							<td class="exportar-barras"><input type="checkbox" name="barras_${columna.nombre}" id="barras_${columna.nombre}" <c:if test="${columna.esAmbito || columna.esMapa || !esNumerico}">disabled</c:if>  /></td>
							<td class="exportar-sectores"><input type="checkbox" name="sectores_${columna.nombre}" id="sectores_${columna.nombre}" <c:if test="${columna.esAmbito || columna.esMapa || !esNumerico}">disabled</c:if> /></td>
							<td class="exportar-mapa"><input type="checkbox" name="mapa_${columna.nombre}" id="mapa_${columna.nombre}" <c:if test="${columna.esAmbito || columna.esMapa || !esNumerico}">disabled</c:if> /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			</div>
		</fieldset>
		<div class="clear"></div>
		<br/><br/>
		<fieldset id="zona_estilos">
			<div>
				<span><spring:message code="jsp.exportar.pdf.seleccione.estilos"/></span>
			</div>
			<div class="fizq">
				<div class="linea">
					<label for="formato_impresion" class="label fizq"><spring:message code="jsp.exportar.pdf.tipo.formato.papel"/></label>
					<select id="formato_impresion" name="formato_impresion">
						<option value="a4"><spring:message code="jsp.exportar.pdf.a4"/></option>
						<option value="a4a"><spring:message code="jsp.exportar.pdf.a4.apaisado"/></option>
						<option value="a3"><spring:message code="jsp.exportar.pdf.a3"/></option>
						<option value="a3a"><spring:message code="jsp.exportar.pdf.a3.apaisado"/></option>
					</select>
				</div>
				<div style="color:red">
					<span>* &nbsp;<spring:message code="jsp.indicador.informe.pdf.formato"/></span>
				</div>
				<div class="linea">
					<label for="tipo_visualizacion" class="label fizq"><spring:message code="jsp.exportar.pdf.tipo.visualizacion"/></label>
					<select id="tipo_visualizacion">
						<option value="tabla"><spring:message code="jsp.exportar.pdf.tabla.valores"/></option>
						<option value="barras"><spring:message code="jsp.exportar.pdf.diagrama.barras"/></option>
						<option value="sectores"><spring:message code="jsp.exportar.pdf.diagrama.sectores"/></option>
						<option value="mapa"><spring:message code="jsp.exportar.pdf.mapa.tematico"/></option>						
					</select>
				</div>
			<!--  formulario estilo tabla -->
            <div class="fizq form_estilos" id="estilo_tabla">
	            <div class="linea">
	            	<label class="label fizq" for="tamanho_fuente"><spring:message code="jsp.visualizacion.tamanho.fuente"/></label>
	            	<select name="tamanho_fuente_tabla" id="tamanho_fuente_tabla" >
						<option value="">&nbsp;</option>
						<c:forEach items="${tamanhosFuente}" var="tam">
                      	<option value="${tam}">${tam}</option>
                       </c:forEach>
                    </select>
	            </div>
	            <div class="linea">
	            	<label class="label fizq" for="tipo_letra"><spring:message code="jsp.visualizacion.tipo.letra"/></label>
	            	<select name="tipo_letra_tabla" id="tipo_letra_tabla" >
						<option value="" selected="selected">&nbsp;</option>
						<c:forEach items="${tiposLetra}" var="tipo">
                      	<option value="${tipo.descripcion}">${tipo.descripcion}</option>
                       </c:forEach>
                    </select>
	            </div>
                <div class="linea">
	            	<label class="label fizq" for="decimales"><spring:message code="jsp.visualizacion.decimales"/></label>
	            	<select name="decimales_tabla" id="decimales_tabla" >
						<option value="" selected="selected">&nbsp;</option>
						<c:forEach items="${decimales}" var="decimal">
                      	<option value="${decimal}">${decimal}</option>
                       </c:forEach>
                    </select>
	            </div>
	            <div class="linea">
	            	<label class="label fizq" for="tipo_fecha"><spring:message code="jsp.visualizacion.tipo.fecha"/></label>
	            	<select name="tipo_fecha_tabla" id="tipo_fecha_tabla" >
						<option value="" selected="selected">&nbsp;</option>
						<c:forEach items="${tiposFecha}" var="tipo">
                      	<option value="${tipo.descripcion}">${tipo.descripcion}</option>
                       </c:forEach>
                    </select>
	            </div>	            	            
            </div> 
            
            <!-- formulario estilo diagrama de barras -->
            <div class="fizq form_estilos" id="estilo_barras" style="display:none">
	            <div class="linea">
	            	<label class="label fizq" for="tamanho_fuente"><spring:message code="jsp.visualizacion.tamanho.fuente"/></label>
	            	<select name="tamanho_fuente_barras" id="tamanho_fuente_barras" >
						<option value="">&nbsp;</option>
						<c:forEach items="${tamanhosFuente}" var="tam">
                      	<option value="${tam}">${tam}</option>
                       </c:forEach>
                    </select>
	            </div>
	            <div class="linea">
	            	<label class="label fizq" for="tipo_letra"><spring:message code="jsp.visualizacion.tipo.letra"/></label>
	            	<select name="tipo_letra_barras" id="tipo_letra_barras" >
						<option value="" selected="selected">&nbsp;</option>
						<c:forEach items="${tiposLetra}" var="tipo">
                      	<option value="${tipo.descripcion}">${tipo.descripcion}</option>
                       </c:forEach>
                    </select>
	            </div>
                <div class="linea">
	            	<label class="label fizq" for="tamanho"><spring:message code="jsp.visualizacion.tamanho"/></label>
	            	<select name="tamanho_barras" id="tamanho_barras" >
						<option value="" selected="selected">&nbsp;</option>
						<c:forEach items="${tamanhosColumna}" var="tamanho">
                      	<option value="${tamanho}">${tamanho}</option>
                       </c:forEach>
                    </select>
	            </div>
	            <div class="linea">
	            	<label class="label fizq" for="color"><spring:message code="jsp.visualizacion.color"/></label>
	            	<input type="text" id="color" name="color" value="#fff"/>
 					<div id="colorpicker"></div>
	            </div>	            	            
            </div>
            
            <!-- formulario estilo diagrama de sectores -->
            <div class="fizq form_estilos" id="estilo_sectores" style="display:none">
	            <div class="linea">
	            	<label class="label fizq" for="tamanho_fuente"><spring:message code="jsp.visualizacion.tamanho.fuente"/></label>
	            	<select name="tamanho_fuente_sectores" id="tamanho_fuente_sectores" >
						<option value="">&nbsp;</option>
						<c:forEach items="${tamanhosFuenteSectores}" var="tam" varStatus="cont">
                      	<option value="${tam}">${valoresTamanhosFuenteSectores[cont.count-1]}</option>
                       </c:forEach>
                    </select>
	            </div>
	            <div class="linea">
	            	<label class="label fizq" for="tipo_letra"><spring:message code="jsp.visualizacion.tipo.letra"/></label>
	            	<select name="tipo_letra_sectores" id="tipo_letra_sectores" >
						<option value="" selected="selected">&nbsp;</option>
						<c:forEach items="${tiposLetra}" var="tipo">
                      	<option value="${tipo.descripcion}">${tipo.descripcion}</option>
                       </c:forEach>
                    </select>
	            </div>
                <div class="linea">
	            	<label class="label fizq" for="tamanho"><spring:message code="jsp.visualizacion.diametro"/></label>
	            	<select name="diametro_sectores" id="diametro_sectores" >
						<option value="" selected="selected">&nbsp;</option>
						<c:forEach items="${diametros}" var="diametro">
                      	<option value="${diametro}">${diametro}</option>
                       </c:forEach>
                    </select>
	            </div>
	            <div class="linea">
	            	<label class="label fizq" for="color_sectores"><spring:message code="jsp.visualizacion.color"/></label>
	            	<input type="text" id="color_sectores" name="color_sectores" value="#FFF"/>&nbsp;<a href="#" id="anhadir_color"><spring:message code="jsp.visualizacion.anhadir.color"/></a>
	            	<input type="hidden" id="colores_sectores" name="colores_sectores" value=""/>
 					<div id="colorpicker_sectores"></div>
	            </div>
	            <div class="linea" style="width:100%;">
	            	<label class="label"><spring:message code="jsp.visualizacion.colores"/></label>
 					<div id="lista_colores_sectores"></div>
	            </div>	            	            
            </div> 
            
            <!-- formulario estilo mapa -->
            <div class="fizq form_estilos" id="estilo_mapa" style="display:none">
           		<div class="linea">
	            	<label class="label fizq" for="inicio"><spring:message code="jsp.visualizacion.inicio.rango"/></label>
	            	<input type="text" id="inicio_mapa" name="inicio_mapa" />
	            </div>
	            <div class="linea">
	            	<label class="label fizq" for="fin"><spring:message code="jsp.visualizacion.fin.rango"/></label>
	            	<input type="text" id="fin_mapa" name="fin_mapa" />
	            </div>
	            <div class="linea">
	            	<label class="label fizq" for="color"><spring:message code="jsp.visualizacion.color"/></label>
	            	<input type="text" id="color_mapa" name="color_mapa" value="#FFF"/>
 					<div id="colorpicker_mapa"></div>
	            </div>
	            <div class="linea">
	            	<a href="#" id="crear_rango" onclick="return false;"><spring:message code="jsp.visualizacion.crear.rango"/></a>
	            </div>
	            <hr/>
	            <c:if test="${empty rangos}">
					<div>
						<table class="tablasTrabajo" id="rangos" cellspacing="0" cellpadding="0">
							<thead>
								<tr>
									<th class="tablaTitulo"><spring:message code="jsp.visualizacion.inicio.rango"/></th>
									<th class="tablaTitulo"><spring:message code="jsp.visualizacion.fin.rango"/></th>
									<th class="tablaTitulo"><spring:message code="jsp.visualizacion.color"/></th>
									<th class="tablaTitulo"><spring:message code="elementos.jsp.boton.borrar"/></th>
								</tr>
							</thead>
							<tbody>
								<tr><td class="tablasDetalle">0</td><td class="tablasDetalle">99999999999999</td><td class="tablasDetalle" style="background-color:${color_defecto}">&nbsp;</td><td class="tablasDetalle borrar_rango" id="borrar_rango_1" style="cursor:pointer;"><spring:message code="elementos.jsp.boton.borrar"/></td></tr>	
							</tbody>	 							
						</table>
					
	 					<div id="inputs_rangos" style="display:none;">
							<input type="hidden" id="inicio_rango_1" name="inicio_rango_1" value="0"/>
							<input type="hidden" id="fin_rango_1" name="fin_rango_1" value="99999999999999"/>
							<input type="hidden" id="color_rango_1" name="color_rango_1" value="${color_defecto}"/>	
	 					</div>
	 					<input type="hidden" name="num_rangos" id="num_rangos" value="1"/>
		            </div>
				</c:if>
				<c:if test="${not empty rangos}">
		            <div class="linea" style="width:100%;">
		            	<label class="label"><spring:message code="jsp.visualizacion.rangos"/></label>
	 					<div>
	 						<table class="tablasTrabajo" id="rangos" cellspacing="0" cellpadding="0">
	 							<thead>
	 								<tr>
	 									<th class="tablaTitulo"><spring:message code="jsp.visualizacion.inicio.rango"/></th>
	 									<th class="tablaTitulo"><spring:message code="jsp.visualizacion.fin.rango"/></th>
	 									<th class="tablaTitulo"><spring:message code="jsp.visualizacion.color"/></th>
	 									<th class="tablaTitulo"><spring:message code="elementos.jsp.boton.borrar"/></th>
	 								</tr>
	 							</thead>
	 							<tbody>
	 								<c:forEach items="${rangos}" var="rango" varStatus="status">
	 									<tr><td class="tablasDetalle">${rango.inicio}</td><td class="tablasDetalle">${rango.fin}</td><td class="tablasDetalle" style="background-color:${rango.color}">&nbsp;</td><td class="tablasDetalle borrar_rango" id="borrar_rango_${status.count}" style="cursor:pointer;"><spring:message code="elementos.jsp.boton.borrar"/></td></tr>	
	 								</c:forEach>
	 							</tbody>	 							
	 						</table>
	 					</div>
	 					<div id="inputs_rangos" style="display:none;">
	 						<c:forEach items="${rangos}" var="range" varStatus="status">
								<input type="hidden" id="inicio_rango_${status.count}" name="inicio_rango_${status.count}" value="${range.inicio}"/>
								<input type="hidden" id="fin_rango_${status.count}" name="fin_rango_${status.count}" value="${range.fin}"/>
								<input type="hidden" id="color_rango_${status.count}" name="color_rango_${status.count}" value="${range.color}"/>	
							</c:forEach>
	 					</div>
	 					<input type="hidden" name="num_rangos" id="num_rangos" value="${numRangos}"/>
		            </div>	
			      </div>
		      </c:if>
		      
		      
            <script>
	    		<c:if test="${estiloTabla!=null}">
					$('#tamanho_fuente_tabla').val("${estiloTabla.tamanhoFuenteComoEntero}");
					$('#tipo_letra_tabla').val("${estiloTabla.tipoFuente}");
					$('#decimales_tabla').val("${estiloTabla.decimales}");
					$('#tipo_fecha_tabla').val("${estiloTabla.tipoFecha.descripcion}");
				</c:if>
				<c:if test="${estiloTabla==null}">
					$('#tamanho_fuente_tabla').val("${tamanho_fuente_defecto_tabla}");
					$('#tipo_letra_tabla').val("${fuente_defecto_tabla}");
					$('#decimales_tabla').val("0");
					$('#tipo_fecha_tabla').val("${tipo_fecha_defecto_tabla}");
				</c:if>
				
	    		<c:if test="${estiloBarras!=null}">
					$('#tamanho_fuente_barras').val("${estiloBarras.tamanhoFuenteComoEntero}");
					$('#tipo_letra_barras').val("${estiloBarras.tipoFuente}");
					$('#tamanho_barras').val("${estiloBarras.tamanho}");
					var estiloColor = "${estiloBarras.color}";
					if ( estiloColor != "" )
						$('#color').val("${estiloBarras.color}");
				</c:if>
				<c:if test="${estiloBarras==null}">
					$('#tamanho_fuente_barras').val("${tamanho_fuente_defecto_barras}");
					$('#tipo_letra_barras').val("${fuente_defecto_barras}");
					$('#tamanho_barras').val("${tamanho_defecto_barras}");
					$('#color').val("${color_defecto}");
				</c:if>
				
				<c:if test="${estiloSectores==null}">
					$('#tamanho_fuente_sectores').val("${tamanho_defecto_sectores}");
					$('#tipo_letra_sectores').val("${fuente_defecto_sectores}");
					$('#diametro_sectores').val("${diametro_defecto_sectores}");
					var estiloColor = "${colores_defecto_sectores}";
					if ( estiloColor != "" ) {
						$('#lista_colores_sectores').html("");
						$('#colores_sectores').val("${colores_defecto_sectores}");
						$('#color_sectores').val("#fff");
						var listaColores = "${colores_defecto_sectores}".split("||");
						for ( var i = 0 ; i < listaColores.length ; i++ ) {
							if ( listaColores[i]!="") {
								var nuevoColor = "<div class='fder' onclick='$(this).remove();' style='cursor:pointer;margin:2px;border:1px solid #000;width:50px;height:50px;background-color:"+listaColores[i]+"'>&nbsp;</div>";
								$('#lista_colores_sectores').append(nuevoColor);
							}
						}
					}
				</c:if>
				<c:if test="${estiloSectores!=null}">
					$('#tamanho_fuente_sectores').val("${estiloSectores.tamanhoFuente}");
					$('#tipo_letra_sectores').val("${estiloSectores.tipoFuente}");
					$('#diametro_sectores').val("${estiloSectores.diametro}");
					var estiloColor = "${estiloSectores.colores}";
					if ( estiloColor != "" ) {
						$('#lista_colores_sectores').html("");
						$('#colores_sectores').val("${estiloSectores.colores}");
						$('#color_sectores').val("#fff");
						var listaColores = "${estiloSectores.colores}".split("||");
						for ( var i = 0 ; i < listaColores.length ; i++ ) {
							if ( listaColores[i]!="") {
								var nuevoColor = "<div class='fder' onclick='$(this).remove();' style='cursor:pointer;margin:2px;border:1px solid #000;width:50px;height:50px;background-color:"+listaColores[i]+"'>&nbsp;</div>";
								$('#lista_colores_sectores').append(nuevoColor);
							}
						}
					}
				</c:if>
				
            </script>		      
		      
			</div>
		</fieldset>
		</form>
	</div>
	<div class="clear"></div>
		<div class="botones_accion">
			<a href="indicadorPublico.htm?accion=visualizarIndicadorTabla&id=${indicadorDto.id}" class="boton izq10 sup10 fondo_blanco fder"><spring:message code="elementos.jsp.boton.cancelar"/></a>
			<%--<a href="#" onclick="$('#formulario').submit();return false;" class="boton izq10 sup10 fondo_blanco fder"><spring:message code="elementos.jsp.boton.generar"/></a>--%>
			<a href="#" id="generar" class="boton izq10 sup10 fondo_blanco fder"><spring:message code="elementos.jsp.boton.generar"/></a>
		</div>
</div>

<script type="text/javascript">
var numRangos = 0;

$(document).ready(function() {
	$('#colorpicker').farbtastic('#color');
	$('#colorpicker_sectores').farbtastic('#color_sectores');
	$('#colorpicker_mapa').farbtastic('#color_mapa');
	
	$("#anhadir_color").click(function(){
		var color = $('#color_sectores').val();
		var nuevoColor = "<div class='fder' onclick='$(this).remove();' style='cursor:pointer;margin:2px;border:1px solid #000;width:50px;height:50px;background-color:"+color+"'>&nbsp;</div>";
		$('#lista_colores_sectores').append(nuevoColor);
		return false;
	});
	
	$('.parametros').click(function() {
		if ($(this).is(":checked")) {
			$('.tabla_visualizacion').css("display","none");
			var nombreColumna = $(this).attr("id").substring("check_".length);
			console.log(nombreColumna);
			$('#visualizacion_'+nombreColumna).fadeIn('slow');			
		} else {
			$('.tabla_visualizacion').css("display","none");
		}
	});
	$("#generar").click(function() {
		$('#colores_sectores').val("");
		$('#lista_colores_sectores div').each(function() {
			$('#colores_sectores').val($('#colores_sectores').val()+"||"+$(this).css("background-color"));
		});
		$('#formulario').submit();
	});
	$('#tipo_visualizacion').change(function() {
		switch ( $(this).val() ) {
			case 'tabla' : 	$('.form_estilos').css("display","none");
							$('#estilo_tabla').fadeIn('slow');
							break;
			case 'barras' : $('.form_estilos').css("display","none");
							$('#estilo_barras').fadeIn('slow');
							break;
			case 'sectores':$('.form_estilos').css("display","none");
							$('#estilo_sectores').fadeIn('slow');
							break;
			case 'mapa' : $('.form_estilos').css("display","none");
							$('#estilo_mapa').fadeIn('slow');
							break;
		}
	});
	
	$('#crear_rango').click(function() {
		estilos.crearRango();
	});
	
	$('.borrar_rango').live('click',function() {
		var numeroRango = $(this).attr("id").substring("borrar_rango_".length);
		$(this).parent().remove();
		$('#inicio_rango_'+numeroRango).remove();
		$('#fin_rango_'+numeroRango).remove();
		$('#color_rango_'+numeroRango).remove();		
	});
});



var estilos = {
		crearRango : function() {
			if ( $('#inicio_mapa').val()=="" || $('#fin_mapa').val()=="" || $('#color_mapa').val()=="") {
				alert("Los campos inicio, fin y color no pueden estar vacios");
				return false;
			}
			if ( parseInt($('#inicio_mapa').val())>parseInt($('#fin_mapa').val())) {
				alert("El campo inicio debe ser menor que el campo fin");
				return false;
			}
			numRangos = $('#num_rangos').val();
			numRangos++;
			var color = "<td class='tablasDetalle' style='background-color:"+$('#color_mapa').val()+"'>&nbsp;</td>";
			html = "<tr><td class='tablasDetalle'>"+$('#inicio_mapa').val()+"</td><td class='tablasDetalle'>"+$('#fin_mapa').val()+"</td>"+color+"<td class='tablasDetalle borrar_rango' id='borrar_rango_"+numRangos+"' style='cursor:pointer;'>Borrar</td></tr>";
			$('#rangos tbody').append(html);
			
			$('#inputs_rangos').append("<input type='hidden' id='inicio_rango_"+numRangos+"' name='inicio_rango_"+numRangos+"' value='"+$('#inicio_mapa').val()+"'/>");
			$('#inputs_rangos').append("<input type='hidden' id='fin_rango_"+numRangos+"' name='fin_rango_"+numRangos+"' value='"+$('#fin_mapa').val()+"'/>");
			$('#inputs_rangos').append("<input type='hidden' id='color_rango_"+numRangos+"' name='color_rango_"+numRangos+"' value='"+$('#color_mapa').val()+"'/>");
			$('#num_rangos').val(numRangos);
			
			//Limpiamos el formulario
			$('#inicio_mapa').val("");
			$('#fin_mapa').val("");	
		},
		modificarEstilos : function () {
			var dialogoModalModificarEstilos = $("#dialogoModalModificarEstilos").dialog({
			    autoOpen: false,
			    show: "blind",
			    hide: "explode",
			    width: 675,
			    height: 650,
			    modal: true,
			    resizable: false,
			    title: "<spring:message code="jsp.indicador.modificar.estilos"/>",
			    buttons: {
			        "Aceptar": function() {
			        	$('#formularioEstiloVisualizacion').submit();
			            return false;
			        },
			         "Cancelar": function() {
			            $(this).dialog( "close" );
			            return false;
			        }
			    }
			});
			$(dialogoModalModificarEstilos).dialog("open");
		}
	};
</script>