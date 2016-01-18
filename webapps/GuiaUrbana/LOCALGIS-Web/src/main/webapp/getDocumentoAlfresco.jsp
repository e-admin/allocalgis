<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" %><%@ page import="java.io.*" %><%@ page import="java.net.*" %><%@ page import="java.util.*" %><%@ page import="com.geopista.utils.alfresco.manager.implementations.AlfrescoManagerImpl" %><%@ page import="com.geopista.utils.alfresco.manager.beans.*" %>
<%

out.clear();

	String idDocumento=request.getParameter("id");
	String folder=request.getParameter("folder");

	String filename=request.getParameter("nombre");

	String filepath=System.getProperty("java.io.tmpdir")+idDocumento;
	
	if(AlfrescoManagerImpl.getInstance().downloadFile(new AlfrescoKey(idDocumento, AlfrescoKey.KEY_UUID), filepath)){	
	
		BufferedInputStream buf=null;
		ServletOutputStream myOut=null;
	
		try
			{
	
			myOut = response.getOutputStream( );
			File myfile = new File(filepath);
			 
			//set response headers
			response.setContentType("application/octet-stream; charset=utf-8");
			 
			
			response.addHeader("Content-Disposition","attachment; filename=\""+filename+"\"");
	
			response.setContentLength( (int) myfile.length( ) );
			 
			FileInputStream input = new FileInputStream(myfile);
			buf = new BufferedInputStream(input);
			int readBytes = 0;
	
			//read from the file; write to the ServletOutputStream
			while((readBytes = buf.read( )) != -1)
			   myOut.write(readBytes);
	
			}
		catch (IOException ioe)
			{     
			throw new ServletException(ioe.getMessage( ));         
			} 
		finally 
			{
			if (myOut != null) myOut.close( );
			if (buf != null) buf.close( );
		
			}
		}
	else{
		out.println("<h2>ERROR: No se ha encontrado el documento</h2>");
		return;
	}	

%>