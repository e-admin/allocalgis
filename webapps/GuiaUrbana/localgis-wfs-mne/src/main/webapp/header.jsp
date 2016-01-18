<%-- $HeadURL: https://sushibar/svn/deegree/apps/services-template/trunk/header.jsp $ --%>
<%-- $Id: header.jsp,v 1.1 2007/10/11 15:36:34 miriamperez Exp $ --%>
<%@ page import="org.deegree.framework.version.*" %>

<table width="100%" border="0" cellpadding="0" cellspacing="0" >
	<tbody>
		<tr valign="top" bgColor="#de6339">
			<td align="left" colspan="2" noWrap>&nbsp;
				<font face="Arial,Helvetica,Geneva,Swiss,SunSans-Regular" size=2> 
					&nbsp;<b>deegree Version: <%=Version.getVersion()%></b>&nbsp;
				</font>&nbsp;
			</td>
			<td align="right" noWarp>
				&nbsp;
				<a name="MENU"></a><b>[<a href="index.jsp">Home</a>]</b>
				&nbsp;
			</td>
		</tr>
		<tr>
            <td align="left" width="25%">
            	<br />
            	<img src="./images/logo-latlon.png" alt="lat/lon GmbH"></img>
            	<br /><br />
            </td>
            <td align="center">
            	<img src="./images/logo-deegree.png" alt=""></img>
            </td>
            <td align="right" width="25%">
            	<img src="./images/logo-unibonn.png" alt="University of Bonn"></img>
            	<br /><br />
            </td>
        </tr>
        <tr>
            <%
	            String mesg = "deegree 2";
	            Object o = request.getParameter( "mesg" );
	            if ( o != null ) {
	                mesg = (String)o;
	            }
            %>
            <td colspan="3" align="center">
            	<h2 style="font-family: helvetica, Arial, Verdana"><%= mesg %></h2>
            </td>
        </tr>
	</tbody>
</table>
