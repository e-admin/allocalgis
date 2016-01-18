<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import = "java.util.*" %>
<%@ page import = "java.io.*" %>
<%@ page import = "java.net.*" %>
<%@ page import = "com.localgis.web.core.manager.*" %>
<%@ page import = "com.localgis.web.util.*" %>
<%@ page import = "com.localgis.web.core.model.*" %>
<%@ page import = "com.geopista.app.administrador.*" %>

<table cellpadding="0" cellspacing="0" border="0" class="display" id="tabla_mapas_municipios">

<thead>
		<tr>
			<th>Municipio</th>
			<th>Id</th>
		</tr>
	</thead>

<tbody>

<%	
					String filter=request.getParameter("filter");
					
					if (filter==null) filter="";
					
					filter=filter.toLowerCase();
		
	        LocalgisMapsConfigurationManager localgisMapsConfigurationManager = LocalgisManagerBuilderSingleton.getInstance().getLocalgisMapsConfigurationManager();
	        
	        List entidades = localgisMapsConfigurationManager.getEntidadesSupramunicipales();
			
	        for(int i=0; i<entidades.size();i++)
					{
						GeopistaEntidadSupramunicipal entidad=(GeopistaEntidadSupramunicipal)entidades.get(i);
						
			        List mapas = localgisMapsConfigurationManager.getPublishedMaps(entidad.getIdEntidad(), new Boolean(true));
			
							if (mapas.size()>0)
							{
		    				out.println("<tr>\n");
		
								out.println("<td class='municipio'>"+entidad.getNombreoficial()+"</td>\n");
								out.println("<td>"+entidad.getIdEntidad()+"</td>\n");
				
								out.println("</tr>\n");
							}
	
					}
%>
</tbody>
</table>


