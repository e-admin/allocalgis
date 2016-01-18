<%@page import="java.io.OutputStream"%>
<%@page import="com.localgis.web.core.model.LocalgisLegend"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ page language="java" contentType="image" %>
<%
    LocalgisLegend localgisLegend = (LocalgisLegend)request.getAttribute("localgisLegend");
    OutputStream outputStream = response.getOutputStream();
    outputStream.write(localgisLegend.getImg());
    outputStream.close();
    response.flushBuffer();
%>
