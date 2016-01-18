<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<html:html xhtml="true" lang="es">


                    <html:form action="/${requestScope.configurationLocalgisWeb}/selectMapAjax.do">

						<table id="selector_entidad" style="margin:auto">
							
							
                                <tr>
                                    <td align="center" style="font-weight:bold; font-size:14px;">
                                        Entidad
                                    </td>
                                    <td align="center">
                                        &nbsp;
                                    </td>
                                </tr>
                                <tr>
                                    <td valign="middle" align="center">
                                        <html:select property="idEntidad">
                                            <logic:notEmpty name="entidades">
                                                <html:optionsCollection name="entidades" label="nombreoficial" value="idEntidad" />
                                            </logic:notEmpty>
                                        </html:select>
                                    </td>
                                    
                                    
                                      <logic:empty name="entidades">
                                      <td align="center" valign="middle">
                                        <html:img styleClass="imageButton"  src="${pageContext.request.contextPath}/img/btn_aceptar_deshabilitado.gif" alt="Ir a la Entidad" border="0" imageName="Image2" style="vertical-align:bottom" />    
                                    </td>
                                    </logic:empty>
                                    
                                    
                                     <logic:notEmpty name="entidades">
                                    <td valign="middle" align="center">
                                        <html:img styleClass="imageButton" src="${pageContext.request.contextPath}/img/btn_aceptar.gif" alt="Ir a la Entidad" border="0" imageName="Image1" style="vertical-align:bottom" onclick="seleccionarmapa();"/>
                                    </td>
                                    </logic:notEmpty>
                                </tr>
                                <tr>
                                    <td valign="middle" align="center">
                                        <span class="errorMessage"><html:errors property="idEntidad"/></span>
                                    </td>
                                    <td valign="middle" align="center">
                                        &nbsp;
                                    </td>
                                </tr>
                            </table>
                        </html:form>
						
					<logic:equal name="configurationLocalgisWeb" value="private">
					<br/><br/>
                       <img class="imageButton" style="display:block; margin:auto;" src="${pageContext.request.contextPath}/img/btn_cerrar_sesion.gif" alt="Cerrar Sesión" onclick="ajaxLoad('${pageContext.request.contextPath}/${requestScope.configurationLocalgisWeb}/logoffAjax.do');"/>
					   <br/><br/>
                    </logic:equal>
						
</html:html>