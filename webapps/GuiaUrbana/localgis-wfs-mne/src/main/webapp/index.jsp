<%-- $HeadURL: https://sushibar/svn/deegree/apps/services-template/trunk/index.jsp $ --%>
<%-- $Id: index.jsp,v 1.1 2007/10/11 15:36:34 miriamperez Exp $ --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="false" errorPage="error.jsp" import="org.deegree.framework.version.*"%>
<% 
	String serviceList = (String) application.getAttribute( "deegree_ogc_services" );
	String title = application.getServletContextName();
	if ( title == null ){
	    title = "deegree 2";
	}

	// it may be difficult to find this out dynamically
	String servicesName = "services";	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 3.2//EN">
<html>
	<head>
		<title><%=title %></title>
		<link rel="stylesheet" href="./css/deegree.css" />
	</head>
	<body leftmargin="10" rightmargin="10" topmargin="0" marginwidth="0" marginheight="0" 
		  link="#2D2A63" vlink="#2D2A63" alink="#E31952">
		
		<jsp:include page="header.jsp" flush="true" />
		
		<p align="center">
			<font size="-1"><%=Version.getVersion()%></font>
		</p>
		
		<table align="center" width="75%" cellpadding="5" cellspacing="5" background="#CECECE">
			<tr>
				<td>
					<b>Installed OGC Web Services: <%=serviceList%></b>
					<br />
					<ul>
<% if (serviceList.contains("WMS") ) { %>
            <li>WMS 1.1.1 - <a href="<%=servicesName %>?REQUEST=GetCapabilities&version=1.1.1&service=WMS">Get 1.1.1 Capabilities</a></li>
            <!-- <li>WMS 1.3.0 - <a href="<%=servicesName %>?REQUEST=GetCapabilities&version=1.3.0&service=WMS">Get 1.3.0 Capabilities</a></li>-->
            <li>WMS 1.1.1 - <a href="wms.jsp">Test WMS</a></li>
<% } if (serviceList.contains( "WFS" ) ) { %>
            <li>WFS - <a href="<%=servicesName %>?REQUEST=GetCapabilities&version=1.1.0&service=WFS">Get Capabilities</a></li>
            <li>WFS 1.1.0 - <a href="wfs.jsp">Test WFS</a></li>
<% } if (serviceList.contains("CSW") ) { %>
            <li>CSW - <a href="<%=servicesName %>?SERVICE=CSW&VERSION=2.0.0&REQUEST=GetCapabilities">Get Capabilities</a></li>
<% } if (serviceList.contains("WCS") ) { %>
            <li>WCS - <a href="<%=servicesName %>?REQUEST=GetCapabilities&version=1.0.0&service=WCS">Get Capabilities</a></li>
            <li>WCS 1.0.0 - <a href="wcs.jsp">Test WCS</a></li>
<% } if (serviceList.contains( "WSS" ) ) { %>
            <li>WSS - <a href="<%=servicesName %>?REQUEST=GetCapabilities&version=1.0&service=WSS">Get Capabilities</a></li>
<% } if (serviceList.contains( "WAS" ) ) { %>
            <li>WAS - <a href="<%=servicesName %>?REQUEST=GetCapabilities&version=1.0&service=WAS">Get Capabilities</a></li>
<% } if (serviceList.contains( "WPVS" ) ) { %>
            <li>WPVS - <a href="<%=servicesName %>?REQUEST=GetCapabilities&version=1.0&service=WPVS">Get Capabilities</a></li>
<% } %>
          </ul>
				</td>
			</tr>
			<tr>
				<td>
					<b>Send some requests:</b>
					<br />
					<ul>
            <li><a href="client/client.html">Generic OGC WebService Client</a></li>
					</ul>
				</td>
			</tr>			
			<tr>
				<td>
					<b>System information:</b>
					<br />
					<ul>
						<li><a href="snoopy.jsp">Status and System Information</a></li>
					</ul>
				</td>
			</tr>
		</table>
		
		<table align="center" width="75%" border="0" cellspacing="0" cellpadding="0">
			<tr align="center">
				<td bgcolor="#FFFF00">
					<font face="Arial, Helvetica, sans-serif" size="1" color="#000000">
						<b> $Revision: 1.1 $ $Workfile: index.jsp $ - Version <%=Version.getVersion() %></b>
					</font>
				</td>
			</tr>
		</table>
	
		<jsp:include page="footer.jsp" flush="true" />
		
	</body>
</html>
		