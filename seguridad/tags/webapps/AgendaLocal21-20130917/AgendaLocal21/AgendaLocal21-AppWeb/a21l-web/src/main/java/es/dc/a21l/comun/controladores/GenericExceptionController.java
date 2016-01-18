/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.comun.controladores;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NestedRuntimeException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import es.dc.a21l.base.utils.excepciones.PublicacionWebDeshabilitadaException;




@SuppressWarnings("unchecked")
@Controller
public class GenericExceptionController implements HandlerExceptionResolver {

	private static final Logger LOG = LoggerFactory.getLogger(GenericExceptionController.class);
	private static final String FormContent="Content-Disposition: form-data;";
	private static final String FileName="filename=\"";

	public ModelAndView resolveException(HttpServletRequest request,HttpServletResponse response, Object handler, Exception ex){ 
		LOG.error("Interceptando excepción "+ex);
		
		if (ex instanceof MaxUploadSizeExceededException) {
			// Se redirige a donde se viene con el parametro de error
			Map<String, String> mapaParametorosPost;
			try {
				mapaParametorosPost=recuperarParametrosPostExceptoFiles(request);
			} catch (IOException e) {
				e.printStackTrace();
				return new ModelAndView("errorTile");
			}
			request.getSession().setAttribute("mapaParametorosPost", mapaParametorosPost);
			return new ModelAndView("redirect:"+request.getHeader("referer")+"&errorTamanhoArchivo=true");
		}
		
		if (ex instanceof HttpMessageNotReadableException) {
			return new ModelAndView("noAutorizadoPublica");
		}
		
		if (ex instanceof PublicacionWebDeshabilitadaException){
			return new ModelAndView("PublicacionWebDeshabilitada");
		}
		
		if(ex instanceof NestedRuntimeException){
			return new ModelAndView("errorSwTile");
		}
		
		return new ModelAndView("errorTile");
	}
	
	@RequestMapping(method=RequestMethod.GET,value="/ir404.htm")
	public String ir404(HttpServletRequest request){
		return "404";
	}
	
	@RequestMapping(method=RequestMethod.GET,value="/ir500.htm")
	public String ir500(HttpServletRequest request){
		return "500";
	}
	
	@RequestMapping(method=RequestMethod.GET,value="/ir403.htm")
	public String ir403(HttpServletRequest request){
		return "403";
	}
	
	
	private Map<String, String> recuperarParametrosPostExceptoFiles(HttpServletRequest request) throws IOException{
		
		Map<String, String> result= new HashMap<String, String>();
		String line = null;
		String paternTrunk="";
		String name="";
		String value="";
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
		paternTrunk=reader.readLine();
		
		if(!StringUtils.isBlank(paternTrunk)){
			while((line = reader.readLine()) != null){
				name="";
				value="";
				if(line.startsWith(FormContent) && !line.contains(FileName)){
					name=recuperarNameValue(line);
					reader.readLine();
					while((line = reader.readLine()) != null){
						if(line.startsWith(paternTrunk))
							break;
						value+=line+"\n";
					}
					result.put(name, value);
				}
			}
		}
		return result;
	}
	
	private String recuperarNameValue(String cadena){
		String[] array=StringUtils.splitByWholeSeparator(cadena, "name=\"");
		if(array.length!=2)
			return "";
		
		array=StringUtils.splitByWholeSeparator(array[1],"\"");
		if(StringUtils.isBlank(array[0]))
			return "";
		return array[0];
	}
	
	
	
	
}
