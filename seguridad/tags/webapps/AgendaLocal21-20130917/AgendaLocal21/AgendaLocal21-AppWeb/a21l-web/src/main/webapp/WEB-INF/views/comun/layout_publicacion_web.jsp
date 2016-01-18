<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="es">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
		<title><tiles:insertAttribute name="title" /></title>
		<link type="text/css" rel="stylesheet" href="css/ui/jquery-ui-1.8.23.custom.css" />
		<link type="text/css" rel="stylesheet" href="css/agendalocal21.css" />		
		<link rel="shortcut icon" href="favicon.ico" />
		<script src="js/jquery-1.5.min.js"></script>
		<script src="js/jquery-ui-1.8.9.custom.min.js"></script>
	</head>
	<body>
	
	<div id="cuerpo">
		<tiles:insertAttribute name="body"/>
	</div>	
	</body>
</html>