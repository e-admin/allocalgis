<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<html:html xhtml="true" lang="es">

								
                    <form id="LoginForm" action="/localgis-guiaurbana/private_ajax/loginAjax.do" method="post">                    
                    <div id="contentPage<logic:equal name="dnie.authactive" value="true">DNIe</logic:equal>">
						<table id="centeredTableLoginWithMunicipio" border="0" style="margin: auto; padding-right: 30px;">
						
							<logic:equal name="dnie.authactive" value="false">
                                <tr>
                                    <td colspan="4" valign="middle" align="center" style="padding-right: 0px;">
                                       <img class="imageButton" src="${pageContext.request.contextPath}/img/dnie/btn_dnie.gif" alt="DNIe" style="vertical-align:bottom" onclick="document.location.href='../dnie/';" />
                                    </td>                                    
                                </tr>
                                <tr>
                                    <td colspan="4" valign="middle" align="center" style="padding-right: 0px;height:15px;"></td>                                    
                                </tr>
                             </logic:equal>                                
                                <tr>
                                    <td rowspan="2" valign="middle" style="padding-right: 0px;">
                                        <img src="${pageContext.request.contextPath}/img/user.gif" alt="User" />
                                    </td>
                                    <td align="center"  style="padding-left: 6px; font-weight:bold; font-size:14px;">
                                        Nombre de Usuario
                                    </td>
                                    <td rowspan="2" valign="middle" style="padding-right: 0px; ">
                                        <img src="${pageContext.request.contextPath}/img/key.gif" alt="Icono llave" />                                        
                                    </td>
                                    <td align="center" style="padding-left: 2px; font-weight:bold; font-size:14px;">
                                        Contraseña
                                    </td>
                                </tr>
                                <tr style="padding-top: 6px;">
                                    <td align="center" valign="middle" style="padding-left: 6px;">
                                        <input type="text" class="inputTextField" name="username"/>
                                    </td>
                                    <td align="center" valign="middle" style="padding-left: 2px;">
                                        <input type="password" class="inputTextField" name="password" onkeypress="checkReturn(event);" />
                                    </td>
                                </tr>                             
                                <tr>
                                    <td valign="middle" colspan="4" style="padding-top: 25px; text-align: center;">
                                        <img class="imageButton" src="${pageContext.request.contextPath}/img/btn_aceptar.gif" alt="Aceptar" style="vertical-align:bottom" onclick="login();" />
                                    </td>
                                </tr>
                                <bean:define id="error" value="${param.error}"/>
                                <logic:notEmpty name="error">
                                <tr>
                                    <td valign="middle" colspan="4" style="text-align:center; padding-top: 4px; color:red; font-weight:bold; font-size:14px;">
                                        <span class="errorMessage">Usuario o contraseña invalidos</span>
                                    </td>
                                </tr>    
                                </logic:notEmpty>
                            </table>                        
                    </div>
                    </form>

</html:html>