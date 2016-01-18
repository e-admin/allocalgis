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
			<th>Mapas disponibles por municipio</th>
		</tr>
	</thead>

<tbody>

<%
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
					
					String ids=p.getProperty("ids_municipios");
					
					if (ids==null) 
					{
						out.println("<h2>ERROR: la propiedad 'ids_municipios' en el archivo <i>portal.properties</i> no se encuentra</h2>");
						ids="";
					}
					
					String idsMunicipios[]=ids.split(",");				
	
					String filter=request.getParameter("filter");
					
					if (filter==null) filter="";
					
					filter=filter.toLowerCase();
		
	        LocalgisMapsConfigurationManager localgisMapsConfigurationManager = LocalgisManagerBuilderSingleton.getInstance().getLocalgisMapsConfigurationManager();
	        
	        List entidades = localgisMapsConfigurationManager.getEntidadesSupramunicipales();
			
	        for(int i=0; i<entidades.size();i++)
					{
						GeopistaEntidadSupramunicipal entidad=(GeopistaEntidadSupramunicipal)entidades.get(i);
						
						if (Arrays.asList(idsMunicipios).contains(""+entidad.getIdEntidad()))
						{
			        List mapas = localgisMapsConfigurationManager.getPublishedMaps(entidad.getIdEntidad(), new Boolean(true));
			
							if (mapas.size()>0)
							{
		    				out.println("<tr id='"+entidad.getIdEntidad()+"' style='display:none' onMouseOut='document.getElementById("+entidad.getIdEntidad()+").style.display='none';'>\n");
		
								out.println("<td class='municipio' id='"+entidad.getNombreoficial()+"'>"+entidad.getNombreoficial()+"</a></td>\n");
								out.println("<td>");
				
				        for(int j=0; j<mapas.size(); j++)
				        {
				        	LocalgisMap mapa=(LocalgisMap)mapas.get(j);
									
									if (filter.length()==0 || mapa.getName().toLowerCase().indexOf(filter)>=0)
									{
										out.println("<a href='/localgis-guiaurbana/public/showMap.do?idMap="+mapa.getMapid()+"' id='"+mapa.getName()+"' target='_blank'>"+mapa.getName()+"</a><br/>");
									}
								}	
		
								out.println("</td>");					
								out.println("</tr>\n");
							}
	
						}
	
					}
				}
%>
</tbody>
</table>


