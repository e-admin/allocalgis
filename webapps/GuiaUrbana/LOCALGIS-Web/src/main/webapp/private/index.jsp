<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html:html xhtml="true" lang="es">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>LocalGis</title>
<link rel="shortcut icon" href="${pageContext.request.contextPath}/favicon.ico" type="image/x-icon" />
</head>

<body>
<script type="text/javascript">
	<logic:notEmpty name="idEntidad" scope="session">
		document.location.href = "${pageContext.request.contextPath}/private/selectMap.do?idEntidad=<bean:write name="idEntidad" scope="session"/>";
	</logic:notEmpty>
	<logic:empty name="idEntidad" scope="session">
		document.location.href = "${pageContext.request.contextPath}/private/selectEntidad.do";
	</logic:empty>
</script>
</body>
</html:html>