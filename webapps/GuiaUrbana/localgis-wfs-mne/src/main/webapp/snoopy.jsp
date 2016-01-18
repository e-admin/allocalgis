<%-- $HeadURL: https://sushibar/svn/deegree/apps/services-template/trunk/snoopy.jsp $ --%>
<%-- $Id: snoopy.jsp,v 1.1 2007/10/11 15:36:34 miriamperez Exp $ --%>
<%@ page
  	language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	isErrorPage="false" errorPage="error.jsp"
  	import="java.util.Enumeration"
%>

<html>
<head>
  <title>snoopy</title>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <meta http-equiv="expires" content="0">
  <!-- $Id: snoopy.jsp,v 1.1 2007/10/11 15:36:34 miriamperez Exp $ -->
</head>

<jsp:include page="header.jsp" flush="true"/>

<font face="Arial,Helvetica,Geneva,Swiss,SunSans-Regular">
  <H1 align="center">Status</H1>

  <table bgcolor="white" width="75%" cellpadding="5" cellspacing="5" height="" align="center" background="#CECECE">
    <tr>
      <td>
        <font size="4">
        <B>Request Information:</B><BR>
        JSP Request Method: <%= request.getMethod() %>
        <br>
        Request URI: <%= request.getRequestURI() %>
        <br>
        Request Protocol: <%= request.getProtocol() %>
        <br>
        Servlet path: <%= request.getServletPath() %>
        <br>
        <% String path = request.getServletPath(); %>
        Request Scheme: <%= request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path.substring(0,path.lastIndexOf('/') )+"/" %>
        <br>
        Path info: <%= request.getPathInfo() %>
        <br>
        Path translated: <%= request.getPathTranslated() %>
        <br>
        Query string: <%= request.getQueryString() %>
        <br>
        Content length: <%= request.getContentLength() %>
        <br>
        Content type: <%= request.getContentType() %>
        <br>
        Server name: <%= request.getServerName() %>
        <br>
        Server IP: <%=java.net.InetAddress.getByName( request.getServerName() ).getHostAddress() %>
        <br>
        Server port: <%= request.getServerPort() %>
        <br>
        Remote user: <%= request.getRemoteUser() %>
        <br>
        Remote address: <%= request.getRemoteAddr() %>
        <br>
        Remote host: <%= request.getRemoteHost() %>
        <br>
        Authorization scheme: <%= request.getAuthType() %>
        <br>
        Authenticated user: <%= request.getUserPrincipal() %>
        <br>
        <br><B>Request Header:</B><BR>
        <%
        Enumeration e = request.getHeaderNames();
        while (e.hasMoreElements() ) {
          String key = (String)e.nextElement();
          out.println( key + " = " +request.getHeader( key ) + "<BR>" );
        }

        out.println( "<BR><B>Request Parameter:</B><BR>");
        e = request.getParameterNames();
        while (e.hasMoreElements() ) {
          String key = (String)e.nextElement();
          out.println( key + " = " + request.getParameter( key ) + "<BR>");
        }

        out.println( "<BR><B>Request Attributes:</B><BR>");
        e = request.getAttributeNames();
        while (e.hasMoreElements() ) {
          String key = (String)e.nextElement();
          out.println( key + " = " + request.getAttribute( key ) + "<BR>");
        }
        %>

        <br>
        <B>Session Attributes:</B><BR>
        <%
        StringBuffer _buf = new StringBuffer("SessionID : " + session.getId() + "<BR>" );
        e = session.getAttributeNames();
        while ( e.hasMoreElements() ) {
              String key = (String) e.nextElement();
              if ( key != null ) {
                      Object value = session.getAttribute( key );
                      if ( value == null) value = new String( "null" );
                      _buf.append( key + " = " + value + "<BR>" );
              }
            }
        out.println( _buf.toString() );

        out.println( "<BR><B>Init Parameters:</B><BR>");
        _buf.delete( 0, _buf.length() );
        // show page attributes
            e = config.getInitParameterNames();
            while ( e.hasMoreElements() )
            {
              String key = (String) e.nextElement();
              _buf.append( key + " = " + config.getInitParameter( key ) + "<BR>" );
             }
        out.println( _buf.toString() );

  out.println( "<BR><BR><B>Application Context:</B><BR>");

  _buf.delete( 0, _buf.length() );
  for (e = application.getAttributeNames(); e.hasMoreElements(); ) {
       String key = e.nextElement().toString();
       String val = application.getAttribute( key ).toString();
       _buf.append( key + " = " + val + "<BR>" );
  }
  out.println( _buf.toString() );
        %>
        <BR><BR>
        <H1>System Properties:</H1>
        Server: <%=application.getServerInfo()%><BR>
        Total Memory: <%=Runtime.getRuntime().totalMemory()/1024%> Kilobyte<BR>
        Free Memory: <%=Runtime.getRuntime().freeMemory()/1024%> Kilobyte<BR>
        <%
          java.util.Properties sysprops = System.getProperties();
          _buf.delete( 0, _buf.length() );
          for (e = sysprops.keys(); e.hasMoreElements() ; ) {
            String key = e.nextElement().toString();
            String value = sysprops.getProperty( key );
            _buf.append( key + " : " + value + "<BR>" );
          }
          out.println( _buf.toString() );
        %>
        <hr>
        The browser you are using is <%= request.getHeader("User-Agent") %>
        <hr>
        </FONT>

      </td>
    </tr>
  </table>

</FONT>

<jsp:include page="footer.jsp" flush="true"/>


