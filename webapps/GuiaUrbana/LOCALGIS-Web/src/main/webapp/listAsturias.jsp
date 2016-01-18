<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import = "java.util.*" %>
<%@ page import = "java.io.*" %>
<%@ page import = "java.net.*" %>
<%@ page import = "com.localgis.web.core.manager.*" %>
<%@ page import = "com.localgis.web.util.*" %>
<%@ page import = "com.localgis.web.core.model.*" %>
<%@ page import = "com.geopista.app.administrador.*" %>

<table cellpadding="0" cellspacing="0" border="0" class="display" id="tabla_mapas_asturias">

<thead>
		<tr>
			<th>Mapas disponibles</th>
		</tr>
	</thead>

<tbody>
<%				
				URL myURL=application.getResource("/portal.properties");

				if (myURL==null)
				{
					out.println("<h2>ERROR: el archivo <i>portal.properties</i> no se encuentra</h2>");
				}
				else
				{
					InputStream in=myURL.openStream();
					Properties p=new Properties();
					p.load(in);
					
					String ids=p.getProperty("id_asturias");
					
					if (ids==null) 
					{
						out.println("<h2>ERROR: la propiedad 'id_asturias' en el archivo <i>portal.properties</i> no se encuentra</h2>");
						ids="-1";
					}
					
					String idAsturias=ids;				

					String filter=request.getParameter("filter");
					
					if (filter==null) filter="";
					
					filter=filter.toLowerCase();
	
	
	        LocalgisMapsConfigurationManager localgisMapsConfigurationManager = LocalgisManagerBuilderSingleton.getInstance().getLocalgisMapsConfigurationManager();
	        
	        List entidades = localgisMapsConfigurationManager.getEntidadesSupramunicipales();
			
	        for(int i=0; i<entidades.size();i++)
					{
						GeopistaEntidadSupramunicipal entidad=(GeopistaEntidadSupramunicipal)entidades.get(i);
						
						// Los mapas de Asturias tiene el id 79					
						if (entidad.getIdEntidad()==Integer.parseInt(idAsturias))
						{
	
			        List mapas = localgisMapsConfigurationManager.getPublishedMaps(entidad.getIdEntidad(), new Boolean(true));
			
			        for(int j=0; j<mapas.size(); j++)
			        {
			        	LocalgisMap mapa=(LocalgisMap)mapas.get(j);
								
								if (filter.length()==0 || mapa.getName().toLowerCase().indexOf(filter)>=0)
								{
									out.println("<tr><td><a href='/localgis-guiaurbana/public/showMap.do?idMap="+mapa.getMapid()+"'>"+mapa.getName()+"</a></td></tr>");
								}
							}	
	
	
						}
	
					}
				}
%>
</tbody>
</table>


