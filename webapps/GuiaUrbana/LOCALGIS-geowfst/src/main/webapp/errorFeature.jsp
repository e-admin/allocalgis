<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html:html xhtml="true">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>LocalGis</title>
<link rel="shortcut icon" href="${pageContext.request.contextPath}/favicon.ico" type="image/x-icon" />

</head>

<body>
        
                    <div id="contentPage">
                        <table id="centeredTable">
                            <tr>
                                <td align="center">
                                    <logic:empty name="errorMessageKey">
                                        <span class="errorMessage">Ha sucedido un error interno en la aplicación.</span>
                                    </logic:empty>
                                    <logic:notEmpty name="errorMessageKey">
                                        <span class="errorMessage"><bean:write name="errorMessageKey"/></span>
                                    </logic:notEmpty>
                                </td>
                            </tr>
                        </table>
                    </div>   

    </body>
</html:html>
