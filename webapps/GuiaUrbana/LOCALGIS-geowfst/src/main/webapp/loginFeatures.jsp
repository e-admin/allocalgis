<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@page import="javax.servlet.http.HttpServletRequest" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html:html xhtml="true">
<script type="text/javascript">
<!--
function checkReturn(e){
    var evt = e || window.event;
    if(!evt) 
        return;
    var key = 0;
    
    if (evt.keyCode) { key = evt.keyCode; } 
    else if (typeof(evt.which)!= 'undefined') { key = evt.which; } 
    if( key == 13 )
    {
        document.forms[0].submit();
    }
}

//-->
</script>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>LocalGis</title>
<link rel="shortcut icon" href="${pageContext.request.contextPath}/favicon.ico" type="image/x-icon" />
<link href="${pageContext.request.contextPath}/css/showFeatureMap/genericStyles.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/showFeatureMap/loginFeaturesStyles.css" rel="stylesheet" type="text/css" />
</head>
    <body onload="document.forms[0].username.focus();">
         <!-- Recuadro superior de la pagina -->
                   <bean:define id="error" value="${param.error}"/>
                                <logic:notEmpty name="error">
                                <tr>
                                    <td colspan="2" align="center">
                                        <span class="errorMessage">Usuario o contraseña invalidos</span>
                                    </td>
                                </tr> 
                                     </logic:notEmpty> 
        <div id="box">                             
	        <div id="boxTopShadow"></div>
	        <div id="boxLeftShadow"></div>
	        <div id="boxRightShadow"></div>
	        <div id="boxBottomShadow"></div>
	        <div id="loginBox">         			
                    <div id="login">
                    	<div id="loginPanelBorderTop"/>
                    	<div class="loginHead">ACCESO</div> 
                        <div id="loginPanelCenter">
                        <form action="j_security_check" method="post">    
                        <input type="hidden" name="requesturi" value="<%=request.getAttribute("fullRequestURL")%>" /> 
                        <table border="0" width="250px">
                            	<tr><td><div height="15px"></div></td></tr>
                                <tr>                                   
                                    <td align="right" width="110px">
                                        Nombre&nbsp;de&nbsp;Usuario
                                    </td>
                                    <td align="center" valign="middle" width="110px">
                                        <input type="text" name="username" class="loginTextBox"/>
                                    </td>
                                </tr>
                                <tr><td><div height="10px"></div></td></tr>
                                <tr>
                               		<td align="right" width="110px">
                                        Contraseña
                                    </td>                                   
                                    <td align="center" valign="middle" width="110px">
                                        <input type="password" name="password" class="loginTextBox" onkeypress="checkReturn(event);" />
                                    </td>
                                </tr>  
                                <tr><td><div height="10px"></div></td></tr>
                                <tr>
                                	<td valign="middle" align="center" colspan="2">
                                       <div id="acceptBtn" onclick="document.forms[0].submit();" class="ActionBtn">ACEPTAR</div>                                         
                                    </td>
                                </tr>             
                              	<tr><td><div height="15px"></div></td></tr>                             
                            </table>                            
                        </form>
                        </div>
                        <div class="loginBottom"></div>
                        <div id="loginPanelBorderLeft"></div>
                    	<div id="loginPanelBorderRight"></div>
                    	<logic:equal name="dnie.authactive" value="true">
                    		<div id="loginCertificateBtn" class="loginCertificateBtn" title="Certificado Electrónico" onclick="document.location.href='../dnie/';"></div>                    	
                        </logic:equal>  
                        <div id="loginPanelBorderBottom"/>
                    </div>                    
                    <!--  Fin del contenido de la pagina -->
	        </div> 
	        
		</div>
		<div id="bottom">
	        <div id="bottomImgLeft"></div> 	
		</div>
        <!-- Fin del recuadro superior de la pagina -->
    </body>
</html:html>