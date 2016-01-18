<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" %><%@ page import="java.io.*" %><%@ page import="java.net.*" %><%@ page import="java.util.*" %>
<%

//Para evitar que de el error de que la salida standard ya ha sido utilizada.
out.clear();
URL myURL=application.getResource("/portal.properties");

if (myURL==null)
	{
		out.println("<h2>ERROR: el archivo <b>portal.properties</b> no se encuentra</h2>");
	}
	else
	{
		InputStream in=myURL.openStream();
		Properties p=new Properties();
		p.load(in);
				
		String almacen=p.getProperty("ruta_almacen_documentos");
					
		if (almacen==null) 
		{
			out.println("<h2>ERROR: la propiedad 'ruta_almacen_documentos' en el archivo <i>portal.properties</i> no se encuentra</h2>");
			return;
		}

	String idDocumento=request.getParameter("id");
	String folder=request.getParameter("folder");

	String filename=request.getParameter("nombre");

	String filepath=almacen+"/"+folder+"/"+idDocumento;
	
	System.out.println("GetDocumento:File Path:"+filepath);

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

%>