<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
   pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="es">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title></title>
</head>
<body>
<script type="text/javascript">
	<logic:notEmpty name="idEntidad" scope="session">
		document.location.href = "${pageContext.request.contextPath}/admin/menu.jsp";
	</logic:notEmpty>
	<logic:empty name="idEntidad" scope="session">
		document.location.href = "${pageContext.request.contextPath}/admin/seleccionEntidad.do";
	</logic:empty>		
</script>
</body>
</html>