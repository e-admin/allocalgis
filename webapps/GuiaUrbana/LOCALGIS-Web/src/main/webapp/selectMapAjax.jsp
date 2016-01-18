<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import = "java.util.*" %>
<%@ page import = "java.io.*" %>
<%@ page import = "com.localgis.web.core.manager.*" %>
<%@ page import = "com.localgis.web.util.*" %>
<%@ page import = "com.localgis.web.core.*" %>
<%@ page import = "com.localgis.web.core.model.*" %>
<%@ page import = "com.geopista.app.administrador.*" %>
<%@ page import = "com.geopista.app.reports.localgiscoreutils.*" %>

<html:html xhtml="true">

 <p style="padding-left: 15px; color: #333333; font-family: tahoma,arial,sans-serif; font-size: 12px; line-height: 1.5; text-align: justify;">La entidad seleccionada es 
 
	<b><%  

		LocalgisEntidadSupramunicipalManager localgisEntidadSupramunicipalManager=LocalgisManagerBuilderSingleton.getInstance().getLocalgisEntidadSupramunicipalManager();
		GeopistaEntidadSupramunicipal entidad=localgisEntidadSupramunicipalManager.getEntidadSupramunicipal(Integer.parseInt(request.getParameter("idEntidad")));

		out.println(entidad.getNombreoficial());
	
	%></b>.
	
	 <a href='pages/privado.html'>Pulse aquí para volver atrás.</a></p><br/>
	
                        <html:form action="/${requestScope.configurationLocalgisWeb}/showMap.do">
                            <input type="hidden" name="idEntidad" value="<bean:write name="idEntidad"/>"/>
                            <table id="selector_entidad" style="margin:auto">
                                <tr>
                                    <td align="center" style="font-weight:bold; font-size:14px;">
                                        Mapa
                                    </td>
                                    <td align="center" style="font-weight:bold; font-size:14px;">
                                        Idioma
                                    </td>
                                    <td align="center">
                                        &nbsp;
                                    </td>
                                </tr>
                                <tr>
                                    <td align="center" valign="middle">
                                        <html:select property="idMap">
                                            <logic:notEmpty name="maps">
                                                <logic:iterate id="map" name="maps">
                                                    <logic:notEmpty name="map" property="name">
                                                        <option value="<bean:write name="map" property="mapid"/>"><bean:write name="map" property="name"/></option>
                                                    </logic:notEmpty>
                                                    <logic:empty name="map" property="name">
                                                        <option value="<bean:write name="map" property="mapid"/>">Nombre Desconocido ID <bean:write name="map" property="mapid"/></option>
                                                    </logic:empty>
                                                </logic:iterate>
                                            </logic:notEmpty>
                                        </html:select>
                                    </td>
                                    <td align="center" valign="middle">
                                    	<label for="language">language</label>
                                        <select name="language" id="language">
                                            <option value="es_ES">Castellano</option>
                                            <option value="ca_ES">Catalán</option>
                                            <option value="gl_ES">Gallego</option>
                                            <option value="eu_ES">Euskera</option>
                                        </select>
                                    </td>
                                    <logic:empty name="maps">
                                      <td align="center" valign="middle">
                                        <html:img styleClass="imageButton"  src="${pageContext.request.contextPath}/img/btn_aceptar_deshabilitado.gif" alt="Ir al Municipio" border="0" imageName="Image2" style="vertical-align:bottom" />    
                                    </td>
                                    </logic:empty>
                                    <logic:notEmpty name="maps">
                                    <td align="center" valign="middle">
                                        <html:img styleClass="imageButton" src="${pageContext.request.contextPath}/img/btn_aceptar.gif" alt="Ir al Municipio" border="0" imageName="Image1" style="vertical-align:bottom" onclick="document.getElementById('ShowMapForm').submit();"/>    
                                    </td>
                                    </logic:notEmpty>
                                </tr>
                                <tr>
                                    <td align="center" valign="middle">
                                        <span class="errorMessage"><html:errors property="idMap" /></span>
                                    </td>
                                    <td align="center" valign="middle">
                                        <span class="errorMessage"><html:errors property="language" /></span>
                                    </td>
                                    <td align="center" valign="middle">
                                        &nbsp;
                                    </td>
                                </tr>
                            </table>
                        </html:form>
</html:html>