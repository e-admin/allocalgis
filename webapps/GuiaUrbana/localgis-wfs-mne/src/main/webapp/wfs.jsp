<%-- $Id: wfs.jsp,v 1.1 2007/10/11 15:36:34 miriamperez Exp $ --%>
<%@ page language="java" contentType="text/html"
    pageEncoding="UTF-8"
    language="java"
    isErrorPage="false" errorPage="error.jsp"
    import="org.deegree.framework.version.*"
    import="org.deegree.ogcwebservices.wfs.capabilities.*"
    import="java.net.*"
    %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>deegree 2 WFS</title>
        <link rel="stylesheet" href="./css/deegree.css"/>
    </head>
    <body>
        <jsp:include page="header.jsp?mesg=WFS Demo" flush="true"/>
        
        <blockquote>
            <b>Capabilities</b>
            <p>The WFS Capabilites: <a href="services?service=WFS&version=1.1.0&request=GetCapabilities">Get Capabilities</a></p>
            <br/>
            
            <b>FeatureTypes</b>
            <p>The WFS Feature Type: <a href="services?service=WFS&version=1.1.0&request=DescribeFeatureType">Describe Feature Type</a></p>
            <blockquote>
                <%
                WFSCapabilitiesDocument capsDoc = new WFSCapabilitiesDocument();
                String u = "http://" + request.getServerName() + ":" + request.getServerPort() +
                            "/" + request.getContextPath() + 
                            "/services?service=WFS&version=1.1.0&request=GetCapabilities";
                
                capsDoc.load( new URL( u ) );
                WFSCapabilities caps =  (WFSCapabilities)capsDoc.parseCapabilities();
                WFSFeatureType[] types = caps.getFeatureTypeList().getFeatureTypes();
                
                for ( int i = 0; i < types.length; i++ ) {
                    org.deegree.datatypes.QualifiedName qn = types[i].getName();
                    out.println( "<a href=\"" );
                    out.println( "services?service=WFS&version=1.1.0&request=DescribeFeatureType&typename=" );
                    out.println( qn.getPrefix() + ":" + qn.getLocalName() + 
                                 "&namespace=xmlns(" + qn.getPrefix() + "=" + qn.getNamespace() + 
                                 ")&outputformat=text%2Fxml%3B+subtype%3Dgml%2F3.1.1\">" );
                    out.println( qn.getLocalName() + "</a><br/>" );
                }
                %>
            </blockquote>
            <br/>
            
            <b>Clients</b>
            <p>Generic OGC WebService <a href="client/client.html">Client</a></p>
            <br/>
        </blockquote>
        
        <jsp:include page="footer.jsp" flush="true"/>
    </body>
</html>