<%-- $HeadURL: https://sushibar/svn/deegree/apps/services-template/trunk/error.jsp $ --%>
<!-- $Id: error.jsp,v 1.1 2007/10/11 15:36:34 miriamperez Exp $ -->
<%@ page 
	language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
	isErrorPage="true" 
%>
<%
	if ( exception == null && request.getAttribute( "javax.servlet.jsp.jspException" ) != null ) {
        exception = (Exception) request.getAttribute( "javax.servlet.jsp.jspException" );
    }
    if ( exception == null ) {
        exception = new Exception( "Exception UNAVAILABLE: Tracing Stack..." );
    }
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 3.2//EN">
<html>
	<head>
		<title>Error - <%=exception.getMessage()%></title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta http-equiv="expires" content="0" />
	</head>
	<body bgcolor="#FFFFFF" text="#000000" leftmargin="10" rightmargin="10" topmargin="0" 
		marginwidth="0" marginheight="0" link="#2D2A63" vlink="#2D2A63" alink="#E31952" 
		style="font-family:Arial,Helvetica,Geneva,Swiss,SunSans-Regular">
		
	    <jsp:include page="header.jsp" flush="true" />
	
			<table align="center" width="75%" height="" cellpadding="5" cellspacing="5"  
				 bgcolor="white" background="#CECECE">
				<tr>
					<td>
			
					<table width="100%" border="0" cellspacing="0" cellpadding="0"
						height="80%">
						<tr>
							<td BGCOLOR=lightgrey ALIGN=CENTER>
								<h1 ALIGN=CENTER><FONT COLOR=RED>Error</FONT></H1>
							</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td>
								<b>The following error has occured: <%=exception.getMessage() %></b>
							</td>
						</tr>
						<tr>
							<td>
								<p>Sorry, your request cannot be completed. The server run into the 
								following error:</p>
								<h2>Stack Trace:</h2>
								<pre>
					                  <% exception.printStackTrace( ); %>
								</pre>
								<p>Please notify the administrator. Thank you.</p>
							</td>
						</tr>
					</table>
					
					</td>
				</tr>
			</table>
		
		<jsp:include page="footer.jsp" flush="true" />
		
	</body>
</html>
