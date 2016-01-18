/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.fuente.controladores;

import java.io.IOException;
import java.net.IDN;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorBooleanSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorFileSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorListSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorMapSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorPOSTSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorStringSW;
import es.dc.a21l.base.utils.enumerados.TiposFuente;
import es.dc.a21l.base.utils.enumerados.TiposFuenteEnumConverter;
import es.dc.a21l.comun.controladores.GenericAbstractController;
import es.dc.a21l.comun.utils.UrlConstructorSW;
import es.dc.a21l.comun.utils.impl.UrlConstructorSWImpl;
import es.dc.a21l.elementoJerarquia.cu.CategoriaDto;
import es.dc.a21l.elementoJerarquia.cu.ElementoJerarquiaDto;
import es.dc.a21l.elementoJerarquia.cu.IndicadorDto;
import es.dc.a21l.elementoJerarquia.cu.impl.IndicadorDtoFormErrorsEmun;
import es.dc.a21l.fuente.cu.AtributoFuenteDatosDto;
import es.dc.a21l.fuente.cu.AtributosMapDto;
import es.dc.a21l.fuente.cu.DescripcionAtributoDto;
import es.dc.a21l.fuente.cu.FuenteDto;
import es.dc.a21l.fuente.cu.TablaFuenteDatosDto;
import es.dc.a21l.fuente.cu.ValorFDDto;

/**
 * 
 * @author Balidea Consulting & Programming
 */

@Controller
@RequestMapping("/fuentes.htm")
public class FuentesControlador extends GenericAbstractController {
	private static final Logger log = LoggerFactory.getLogger(FuentesControlador.class);
	private static final String ERROR_PARSEO_FICHERO = "__ERROR__PARSEO__FICHERO__";
	private static final String ERROR_CARGA_CATOLOGO = "__ERROR__CARGA__CATOLOGO__ ";
	private static final String ERROR_PARAM_CONFIG = "__ERROR__PARAM__CONFIG__";

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
		binder.registerCustomEditor(TiposFuente.class,new TiposFuenteEnumConverter());
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET)
	public String cargaTodos(Model model, HttpServletRequest request) {
		List<FuenteDto> listaFuentesExterna =  new ArrayList<FuenteDto>();
		UrlConstructorSW url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		url.setParametroCadena("fuentes");
		url.setParametroCadena("listaFuentesExt");

		listaFuentesExterna.addAll( (getRestTemplate().getForEntity(url.getUrl(), EncapsuladorListSW.class)).getBody() );
	
		model.addAttribute("listaFuentes", listaFuentesExterna);
		return "fuentesTile";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET, params={"accion=verCatalogoSistema"})
	public String listarFuentesInternas(Model model, HttpServletRequest request) {
		List<FuenteDto> listaFuentesSistema = new ArrayList<FuenteDto>();
		
		log.debug("CATALOGO  ENTRA COMPRUEBA SI EXISTE");						
		UrlConstructorSW url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW()).setParametroCadena("fuentes").setParametroCadena("cargaFuenteInterna").setParametroCadena(getServicioSeguridad().getUserDetails().getId());
       
		FuenteDto f  = getRestTemplate().getForEntity(url.getUrl(), FuenteDto.class).getBody();	
		
		log.debug("ID: "+ f.getId() + "  nombre: "+ f.getNombre());	
		log.debug("Obtiene fuentes internas de BD");	
		UrlConstructorSW url2 = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW()).setParametroCadena("fuentes")
					.setParametroCadena("listaFuentesInt");

		listaFuentesSistema.addAll((getRestTemplate().getForEntity(url2.getUrl(), EncapsuladorListSW.class)).getBody());
		
		model.addAttribute("listaFuentesInt", listaFuentesSistema);
		
		
		if(f != null && f.getNombre().equals(ERROR_PARAM_CONFIG))	
		{
		   log.error("error de cargaCatalogoSistema " +  f.getNombre());
		   model.addAttribute("errorParamCatalogo", this.ERROR_PARAM_CONFIG);
		}
		else if (f==null || f.getId()<1)
		{    log.error("error de cargaCatalogoSistema "+this.ERROR_CARGA_CATOLOGO);
			 model.addAttribute("errorCarga", this.ERROR_CARGA_CATOLOGO);
		}
				
		return "catalogoSistemaTile";
						
						
	}

	@RequestMapping(method = RequestMethod.POST, params = { "accion=listaTablas" })
	public @ResponseBody String ajaxTablasFuente(Model model, HttpServletRequest request,@RequestParam(value = "fuente") Long id,@RequestParam(value = "tipo") String tipo) {
		UrlConstructorSW url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		url.setParametroCadena("fuentes");
		url.setParametroCadena("tablasFuente");
		url.setParametroCadena(id);
		url.setParametroCadena(tipo);
		
		ResponseEntity<EncapsuladorListSW> lista = getRestTemplate().getForEntity(url.getUrl(), EncapsuladorListSW.class);
		EncapsuladorListSW<TablaFuenteDatosDto> listaTablas =lista.getBody();
		for(TablaFuenteDatosDto tabla : listaTablas.getLista()){
			tabla.setNombre(quitarAcentos(tabla.getNombre()));
		}
		return "correcto||"+writeJson(listaTablas);
	}
	
	public static String quitarAcentos(String input) {
	    // Cadena de caracteres original a sustituir.
	    String []original = {"á","é","í","ó","ú","Á","É","Í","Ó","Ú","ñ","Ñ"};
	    // Cadena de caracteres ASCII que reemplazarán los originales.
	    String [] ascii = {"&aacute;","&eacute;","&iacute;","&oacute;","&uacute;","&Aacute;","&Eacute;","&Iacute;","&Oacute;","&Uacute;","&ntilde;","&Ntilde;"};
	    String output = input;
	    for (int i=0; i<original.length; i++) {
	        // Reemplazamos los caracteres especiales.
	       output = output.replace(original[i], ascii[i]);
	    }//for i
	    return output;
	}

	@RequestMapping(method = RequestMethod.POST, params = { "accion=listaColumnas" })
	public @ResponseBody String ajaxColumnasTablaFuente(Model model,HttpServletRequest request,@RequestParam(value = "fuente") Long id,@RequestParam(value = "tabla") String tabla,@RequestParam(value = "tipo") String tipo) {
		UrlConstructorSW url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		url.setParametroCadena("fuentes");
		url.setParametroCadena("columnasFuente");
		url.setParametroCadena(tabla);
		url.setParametroCadena(id);
		url.setParametroCadena(tipo);

		ResponseEntity<EncapsuladorListSW> lista = getRestTemplate().getForEntity(url.getUrl(), EncapsuladorListSW.class);
		EncapsuladorListSW<AtributoFuenteDatosDto> listaColumnas=(EncapsuladorListSW<AtributoFuenteDatosDto>)lista.getBody();
		boolean error=false;
		if(listaColumnas!=null && listaColumnas.getLista()!=null && listaColumnas.getLista().size()>0){
    		for(AtributoFuenteDatosDto columna: listaColumnas.getLista()){
    			if(tieneCaracteresEspeciales(columna.getNombre())){
    				error=true;
    				break;
    			}
    		}
    	}
		if(error){
			model.addAttribute("erroresCaracteres", "true");
			return "error||"+writeJson(lista.getBody());
		}
		return "correcto||"+writeJson(lista.getBody());
	}
	
	
	
	 private boolean tieneCaracteresEspeciales(String cadena){
    	boolean sinCaracteresEspeciales=Pattern.matches("[a-zA-Z_0-9\\p{Blank}]+", cadena);
    	if(sinCaracteresEspeciales){
    		return false;
    	}else{
    		return true;
    	}
    }
	
	@RequestMapping(method = RequestMethod.GET, params = { "accion=edita" })
	public String edita(@RequestParam(value = "id") long id, Model model,HttpServletRequest request,@RequestParam(value="errorTamanhoArchivo",required=false)Boolean errorTamanhoArchivo) {
		UrlConstructorSW url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		url.setParametroCadena("fuentes");
		url.setParametroCadena("fuente");
		url.setParametroCadena(id);

		FuenteDto fuenteDto = id < 1 ? new FuenteDto() : getRestTemplate().getForEntity(url.getUrl(), FuenteDto.class).getBody();

		if(errorTamanhoArchivo!=null && errorTamanhoArchivo)
			fuenteDto=rellenarParametrosPost(fuenteDto, request);
		
		model.addAttribute("fuenteDto", fuenteDto);
		
		model = obtenerListaIndicadores(model, fuenteDto);
		
		return "fuenteTile";
	}

	@RequestMapping(method = RequestMethod.POST, params = { "accion=guarda" })
	public String guarda(Model model,@ModelAttribute FuenteDto fuenteDto,BindingResult result, HttpServletRequest request,
			@RequestParam(value = "fich_csv_gml_f", required = false) MultipartFile fich_f,
			@RequestParam(value = "fich_dbf_f", required = false) MultipartFile fich_dbf_f,
			@RequestParam(value = "fich_shp_f", required = false) MultipartFile fich_shp_f,
			@RequestParam(value = "fich_shx_f", required = false) MultipartFile fich_shx_f) {
		
		UrlConstructorSW url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		url.setParametroCadena("fuentes");
		url.setParametroCadena("fuente");
		url.setParametroCadena(getServicioSeguridad().getUserDetails().getId());

		fuenteDto.setFich_csv_gml(null);
		fuenteDto.setFich_dbf(null);
		fuenteDto.setFich_shp(null);
		fuenteDto.setFich_shx(null);

		String ficheroCsvGml = null;
		String ficheroShp = null;
		String ficheroDbf = null;
		String ficheroShx = null;
		
		EncapsuladorFileSW fichero = null;
		FuenteDto fuenteDtoOriginal = new FuenteDto();
		
		if ( (fich_f.getSize()<=0 && fuenteDto.getTipo()==TiposFuente.CSV || fich_f.getSize()<=0 && fuenteDto.getTipo()==TiposFuente.GML )) {
			if ( fuenteDto.getId()<=0) {
				log.error("Error. Fichero de formato incorrecto (csv, gml)");
				model.addAttribute("resultado", "fileError");
				model.addAttribute("fileError","validacion.fuentes.fichero_obligatorio");
				return "fuenteTile";
			}
		}
		
		//Si es una edicion de fuente. Me quedo con el fichero anterior por si el nuevo falla.
		if ( fuenteDto.getId()>0) {
			UrlConstructorSW urlTmp = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
			urlTmp.setParametroCadena("fuentes");
			urlTmp.setParametroCadena("obtenerFichero");
			urlTmp.setParametroCadena(fuenteDto.getId());
			urlTmp.setParametroCadena(fuenteDto.getTipo().getId());
			
			fichero = getRestTemplate().getForEntity(urlTmp.getUrl(), EncapsuladorFileSW.class).getBody();
			
			urlTmp = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
			urlTmp.setParametroCadena("fuentes");
			urlTmp.setParametroCadena("fuente");
			urlTmp.setParametroCadena(fuenteDto.getId());

			fuenteDtoOriginal = getRestTemplate().getForEntity(urlTmp.getUrl(), FuenteDto.class).getBody();			
		}
		
		boolean envioFichero = false;
		if (fich_f != null && fich_f.getSize() > 0) {
			envioFichero = true;
			String nombreOriginalCsvGml = fich_f.getOriginalFilename();
			
			if (!(nombreOriginalCsvGml.toLowerCase().trim().endsWith(".csv") && fuenteDto.getTipo() == TiposFuente.CSV) && !(nombreOriginalCsvGml.toLowerCase().trim().endsWith(".gml") && fuenteDto.getTipo() == TiposFuente.GML) ) {
				log.error("Error. Fichero de formato incorrecto (csv, gml)");
				model.addAttribute("resultado", "fileError");
				model.addAttribute("fileError","validacion.fuentes.fichero_tipo_incorrecto");
				return "fuenteTile";
			}
			
			ficheroCsvGml = fich_f.getOriginalFilename();
			fuenteDto.setFich_csv_gml(ficheroCsvGml);
		}
		
		if (fich_dbf_f != null && fich_dbf_f.getSize() > 0 && fich_shp_f != null && fich_shp_f.getSize() > 0 ) {
			envioFichero = true;
			String nombreOriginalShp = fich_shp_f.getOriginalFilename();
			String nombreOriginalDbf = fich_dbf_f.getOriginalFilename();
			String nombreOriginalShx = null;
			if ( fich_shx_f != null && fich_shx_f.getSize()>0)
				nombreOriginalShx = fich_shx_f.getOriginalFilename();
			
			boolean errorTipo = true;
			if ( (nombreOriginalShp.toLowerCase().trim().endsWith(".shp") && fuenteDto.getTipo() == TiposFuente.SHAPEFILE) &&
				 (nombreOriginalDbf.toLowerCase().trim().endsWith(".dbf") && fuenteDto.getTipo() == TiposFuente.SHAPEFILE) &&
				 (nombreOriginalShx==null || nombreOriginalShx.toLowerCase().trim().endsWith(".shx") && fuenteDto.getTipo() == TiposFuente.SHAPEFILE)){
				errorTipo=false;
			}
			if (errorTipo) {
				log.error("Error. Fichero de formato incorrecto");
				model.addAttribute("resultado", "fileError");
				model.addAttribute("fileError","validacion.fuentes.fichero_tipo_incorrecto.shp");
				return "fuenteTile";
			}
			errorTipo=true;
			String nombreFichShp=nombreOriginalShp.substring(0, nombreOriginalShp.length()-4);
			String nombreFichDbf=nombreOriginalDbf.substring(0,nombreOriginalDbf.length()-4);
			if(nombreOriginalShx!=null){
				String nombreFichShx=nombreOriginalShx.substring(0,nombreOriginalShx.length()-4);
				if(nombreFichDbf.equals(nombreFichShp)&&nombreFichDbf.equals(nombreFichShx)){
					errorTipo = false;
				}
			}else{
				if(nombreFichDbf.equals(nombreFichShp)){
					errorTipo = false;
				}
			}
			if (errorTipo) {
				log.error("Error. Fichero de nombre incorrecto");
				model.addAttribute("resultado", "fileError");
				model.addAttribute("fileError","validacion.fuentes.fichero_nombre_incorrecto.shp");
				return "fuenteTile";
			}
			ficheroShp = nombreOriginalShp;
			ficheroDbf = nombreOriginalDbf;
			ficheroShx = nombreOriginalShx;
			fuenteDto.setFich_dbf(ficheroDbf);
			fuenteDto.setFich_shp(ficheroShp);
			fuenteDto.setFich_shx(ficheroShx);
		}else{
			if ((fich_dbf_f != null && fich_dbf_f.getSize() > 0 ) ||(fich_shp_f != null && fich_shp_f.getSize() > 0)||(fich_shx_f != null && fich_shx_f.getSize() > 0) ) {
				log.error("Error. Ficheros obligatorios no incluidos");
				model.addAttribute("resultado", "fileError");
				model.addAttribute("fileError","validacion.fuentes.fichero_obligatorio_error.shp");
				return "fuenteTile";
			}
		}
		
		if ( !envioFichero && fuenteDto.getId()>0 ) {
			fuenteDto.setFich_csv_gml(fuenteDtoOriginal.getFich_csv_gml());
			fuenteDto.setFich_dbf(fuenteDtoOriginal.getFich_dbf());
			fuenteDto.setFich_shp(fuenteDtoOriginal.getFich_shp());
			fuenteDto.setFich_shx(fuenteDtoOriginal.getFich_shx());
		}
			

		HttpEntity<FuenteDto> entity = new HttpEntity<FuenteDto>(fuenteDto,getHeaders());
		ResponseEntity<EncapsuladorPOSTSW> respuesta = getRestTemplate().postForEntity(url.getUrl(), entity, EncapsuladorPOSTSW.class);

		// Se evaluan errores en el servicio web
		if (hasErrorsSW(respuesta))
			return irPaginaErrorSW();

		// Se evaluan errores en el formulario
		if (respuesta.getBody().hashErrors()) {
			fuenteDto.setFich_csv_gml("");
			fuenteDto.setFich_shp("");
			fuenteDto.setFich_dbf("");
			fuenteDto.setFich_shx("");
			escribirErrores(result, respuesta.getBody());
			return "fuenteTile";
		}

		FuenteDto fuenteDto2 = (FuenteDto) respuesta.getBody().getObjetoEncapsulado();
		model.addAttribute("fuenteDto", fuenteDto2);

		ResponseEntity<EncapsuladorPOSTSW> respuestaFichero = null;
		//Enviamos el fichero al servidor SOLO si hay fichero
		if ( envioFichero ) {
			envioFichero = true;
			if ( fuenteDto2.getTipo()==TiposFuente.CSV )
				respuestaFichero = guardarFicheroCSV(fich_f,fuenteDto2.getId());
			else if ( fuenteDto2.getTipo()==TiposFuente.GML )
				respuestaFichero = guardarFicheroGML(fich_f,fuenteDto2.getId());
			else if ( fuenteDto2.getTipo()==TiposFuente.SHAPEFILE ) {
				if ( fich_shp_f!=null && fich_shp_f.getSize()>0) {
					respuestaFichero = guardarFicheroShape(fich_shp_f,fuenteDto2.getId(),"shp");
					if (respuestaFichero != null) {
						if (hasErrorsSW(respuestaFichero))
							return irPaginaErrorSW();
		
						if (respuestaFichero.getBody().hashErrors()) {
							escribirErrores(result, respuestaFichero.getBody());
							model.addAttribute(getPathPaqueteValidacion() + "fuenteDto",result);
							return "fuenteTile";
						}
					}
				}
				if ( fich_dbf_f!=null && fich_dbf_f.getSize()>0) {
					respuestaFichero = guardarFicheroShape(fich_dbf_f,fuenteDto2.getId(),"dbf");
					if (respuestaFichero != null) {
						if (hasErrorsSW(respuestaFichero))
							return irPaginaErrorSW();
		
						if (respuestaFichero.getBody().hashErrors()) {
							escribirErrores(result, respuestaFichero.getBody());
							model.addAttribute(getPathPaqueteValidacion() + "fuenteDto",result);
							return "fuenteTile";
						}
					}
				}
				if ( fich_shx_f!=null && fich_shx_f.getSize()>0) {
					respuestaFichero = guardarFicheroShape(fich_shx_f,fuenteDto2.getId(),"shx");
					if (respuestaFichero != null) {
						if (hasErrorsSW(respuestaFichero))
							return irPaginaErrorSW();
		
						if (respuestaFichero.getBody().hashErrors()) {
							escribirErrores(result, respuestaFichero.getBody());
							model.addAttribute(getPathPaqueteValidacion() + "fuenteDto",result);
							return "fuenteTile";
						}
					}
				}
			}
	
			if (respuestaFichero != null) {
				// Despues de guardar el fichero
				// Se evaluan errores en el servicio web
				if (hasErrorsSW(respuestaFichero))
					return irPaginaErrorSW();
	
				// Se evaluan errores en el formulario
				if (respuestaFichero.getBody().hashErrors()) {
					escribirErrores(result, respuestaFichero.getBody());
					model.addAttribute(getPathPaqueteValidacion() + "fuenteDto",result);
					return "fuenteTile";
				}
			} else if ( fuenteDto2.getTipo().getId().equals(TiposFuente.CSV.getId()) || fuenteDto2.getTipo().getId().equals(TiposFuente.GML.getId()) || fuenteDto2.getTipo().getId().equals(TiposFuente.SHAPEFILE.getId())) {
				log.error("Error. Error al leer el fichero");
				model.addAttribute("resultado", "fileError");
				model.addAttribute("fileError","validacion.fuente.fichero.error");
				return "fuenteTile";
			}
		}

		boolean errorParseo = false;
		String nombreTabla = "";
		String esquema = "null";
		if ( fuenteDto.getTipo()==TiposFuente.BDESPACIAL || fuenteDto.getTipo()==TiposFuente.ODBC || fuenteDto.getTipo()==TiposFuente.MYSQL || fuenteDto.getTipo()==TiposFuente.ORACLE) {
			url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
			url.setParametroCadena("fuentes");
			url.setParametroCadena("tablasFuente");
			url.setParametroCadena(fuenteDto2.getId());
			url.setParametroCadena(fuenteDto2.getTipo().getId());
			
			EncapsuladorListSW<TablaFuenteDatosDto> tablas = getRestTemplate().getForEntity(url.getUrl(),EncapsuladorListSW.class).getBody();
			if(tablas!=null && tablas.size()>0){
				nombreTabla = tablas.get(0).getNombre();
				esquema = tablas.get(0).getEsquema();
			}
		} else {
			nombreTabla = fuenteDto.getNombre();
		}
		//if ( envioFichero ) {
		AtributosMapDto datos = null;
		if ( !nombreTabla.equals(ERROR_PARSEO_FICHERO)&& !("").equals(nombreTabla.trim())) {
			//La fuente ya se ha guardado. Ahora vamos a obtener los datos para comprobar si el fichero es parseable / correcto
			// Datos tabla
			url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
			url.setParametroCadena("fuentes");
			url.setParametroCadena("datosTablaFuente");
			if (esquema==null || esquema.equals("")){
				esquema = "null";
			}
			url.setParametroCadena(esquema);
			url.setParametroCadena(nombreTabla);
			url.setParametroCadena(fuenteDto2.getId());
			url.setParametroCadena(fuenteDto2.getTipo().getId());
	
			datos = getRestTemplate().getForEntity(url.getUrl(),AtributosMapDto.class).getBody();
		} else {
			AtributosMapDto mapa = new AtributosMapDto();
    		ValorFDDto valores = new ValorFDDto();
    		mapa.setValor(ERROR_PARSEO_FICHERO, valores);
    		datos = mapa;
		}
		if ( datos.getContenido().containsKey(ERROR_PARSEO_FICHERO)) {
			errorParseo = true;
			if ( fuenteDto.getTipo()==TiposFuente.BDESPACIAL || fuenteDto.getTipo()==TiposFuente.ODBC || fuenteDto.getTipo()==TiposFuente.MYSQL || fuenteDto.getTipo()==TiposFuente.ORACLE) {
				model.addAttribute("errorFichero","jsp.fuente.error.parseo.dataBase");
			}else{
				model.addAttribute("errorFichero","jsp.fuente.error.parseo.fichero");
			}
			if ( fuenteDto.getId()<=0 ) {
				url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
				url.setParametroCadena("fuentes");
				url.setParametroCadena("borraFuente");
				url.setParametroCadena(fuenteDto2.getId());
				url.setParametroCadena(fuenteDto2.getTipo().getId());
				entity = new HttpEntity<FuenteDto>(fuenteDto,getHeaders());
				respuesta = getRestTemplate().postForEntity(url.getUrl(), entity, EncapsuladorPOSTSW.class);
				fuenteDto2.setFich_csv_gml(null);
				fuenteDto2.setFich_dbf(null);
				fuenteDto2.setFich_shp(null);
				fuenteDto2.setFich_shx(null);
				fuenteDto2.setId(0);
			} else {
				//Volvemos a guardar el fichero anterior ya q el nuevo dio error
				if ( fuenteDto2.getTipo()==TiposFuente.CSV )
					respuestaFichero = guardarFicheroCSVBytes(fichero,fuenteDto2.getId());
				else if ( fuenteDto2.getTipo()==TiposFuente.GML )
					respuestaFichero = guardarFicheroGMLBytes(fichero,fuenteDto2.getId());
				else if ( fuenteDto2.getTipo()==TiposFuente.SHAPEFILE ) {
					if ( fich_shp_f!=null && fich_shp_f.getSize()>0) {
						respuestaFichero = guardarFicheroShapeBytes(fichero,fuenteDto2.getId(),"shp");
						if (respuestaFichero != null) {
							if (hasErrorsSW(respuestaFichero))
								return irPaginaErrorSW();
			
							if (respuestaFichero.getBody().hashErrors()) {
								escribirErrores(result, respuestaFichero.getBody());
								model.addAttribute(getPathPaqueteValidacion() + "fuenteDto",result);
								return "fuenteTile";
							}
						}
					}
				}
				//Dejamos la fuente como estaba
				UrlConstructorSW url2 = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
				url2.setParametroCadena("fuentes");
				url2.setParametroCadena("fuente");
				url2.setParametroCadena(getServicioSeguridad().getUserDetails().getId());
				
				entity = new HttpEntity<FuenteDto>(fuenteDtoOriginal,getHeaders());
				respuesta = getRestTemplate().postForEntity(url2.getUrl(), entity, EncapsuladorPOSTSW.class);
				fuenteDto2 = (FuenteDto)respuesta.getBody().getObjetoEncapsulado();
			}
			model.addAttribute("fuenteDto", fuenteDto2);
		} else {
			if (fuenteDto.getId() <= 0) {
				model.addAttribute("resultado", "exitoCrear");
			} else {
				model.addAttribute("resultado", "exitoGuardar");
			}
		}
		if(fuenteDto2.isColumnasGeometricasErroneas()){
			model.addAttribute("errorColumnas","validacion.fuentes.columnas.geometricas");
		}
		if ( errorParseo )
			return "fuenteTile";
		else
			return cargaTodos(model, request);
	}
	
	@RequestMapping(method = RequestMethod.GET, params = { "accion=borrarInterno" })
	public String borraInterno(@RequestParam(value = "id") long id, @RequestParam(value = "tipo") String tipo, @ModelAttribute FuenteDto fuentesDto, BindingResult result, Model model, HttpServletRequest request) {
		UrlConstructorSW url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		url.setParametroCadena("fuentes");
		url.setParametroCadena("fuente");
		url.setParametroCadena(id);
		FuenteDto fuenteDto = getRestTemplate().getForEntity(url.getUrl(), FuenteDto.class).getBody();
		
		url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		url.setParametroCadena("fuentes");
		url.setParametroCadena("fuenteCatalogo");
		FuenteDto fuenteCatalogo = getRestTemplate().getForEntity(url.getUrl(), FuenteDto.class).getBody();
		
		if(fuenteDto!=null && fuenteCatalogo!=null && fuenteDto.getNombre()!=null && fuenteDto.getNombre().equals(fuenteCatalogo.getNombre())&& 
				fuenteDto.getInfoConexion()!=null &&fuenteDto.getInfoConexion().equals(fuenteCatalogo.getInfoConexion())&& fuenteDto.getUsuario()!=null && fuenteDto.getUsuario().equals(fuenteCatalogo.getUsuario())
				&& fuenteDto.getPassword()!=null && fuenteDto.getPassword().equals(fuenteCatalogo.getPassword())){
			log.error("Error. Fuente en catalogo");
			model.addAttribute("resultado", "fuenteCatalogo");
			model.addAttribute("fuenteCatalogo","validacion.fuente.eliminar.fuente.catalogoInterno");
			return listarFuentesInternas(model, request);
		}
		
		url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		url.setParametroCadena("fuentes");
		url.setParametroCadena("borraFuente");
		url.setParametroCadena(fuenteDto.getId());
		url.setParametroCadena(fuenteDto.getTipo().getId());
		
		HttpEntity<FuenteDto> entity = new HttpEntity<FuenteDto>(fuenteDto,getHeaders());
		ResponseEntity<EncapsuladorPOSTSW> respuesta = getRestTemplate().postForEntity(url.getUrl(), entity, EncapsuladorPOSTSW.class);
		
		// Se evaluan errores en el servicio web
		if (hasErrorsSW(respuesta))
			return irPaginaErrorSW();

		// Se evaluan errores en el formulario
		if (respuesta.getBody().hashErrors()) {
			fuenteDto.setFich_csv_gml(((FuenteDto)respuesta.getBody().getObjetoEncapsulado()).getFich_csv_gml());
			fuenteDto.setFich_dbf(((FuenteDto)respuesta.getBody().getObjetoEncapsulado()).getFich_dbf());
			fuenteDto.setFich_shp(((FuenteDto)respuesta.getBody().getObjetoEncapsulado()).getFich_shp());
			fuenteDto.setFich_shx(((FuenteDto)respuesta.getBody().getObjetoEncapsulado()).getFich_shx());
			
			escribirErrores(result, respuesta.getBody());
			model = obtenerListaIndicadores(model, (FuenteDto)respuesta.getBody().getObjetoEncapsulado());
			return listarFuentesInternas(model, request);
			//return "fuenteTile";
		}

		model.addAttribute("fuenteDto", new FuenteDto());
		model.addAttribute("resultado", "exitoBorrar");
		
		return listarFuentesInternas(model, request);
	}

	@RequestMapping(method = RequestMethod.GET, params = { "accion=borrar" })
	public String borra(@RequestParam(value = "id") long id, @RequestParam(value = "tipo") String tipo, @ModelAttribute FuenteDto fuentesDto, BindingResult result, Model model, HttpServletRequest request) {
		UrlConstructorSW url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		url.setParametroCadena("fuentes");
		url.setParametroCadena("fuente");
		url.setParametroCadena(id);
		FuenteDto fuenteDto = getRestTemplate().getForEntity(url.getUrl(), FuenteDto.class).getBody();
		
		url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		url.setParametroCadena("fuentes");
		url.setParametroCadena("borraFuente");
		url.setParametroCadena(fuenteDto.getId());
		url.setParametroCadena(fuenteDto.getTipo().getId());
		
		HttpEntity<FuenteDto> entity = new HttpEntity<FuenteDto>(fuenteDto,getHeaders());
		ResponseEntity<EncapsuladorPOSTSW> respuesta = getRestTemplate().postForEntity(url.getUrl(), entity, EncapsuladorPOSTSW.class);
		
		// Se evaluan errores en el servicio web
		if (hasErrorsSW(respuesta))
			return irPaginaErrorSW();

		// Se evaluan errores en el formulario
		if (respuesta.getBody().hashErrors()) {
			fuenteDto.setFich_csv_gml(((FuenteDto)respuesta.getBody().getObjetoEncapsulado()).getFich_csv_gml());
			fuenteDto.setFich_dbf(((FuenteDto)respuesta.getBody().getObjetoEncapsulado()).getFich_dbf());
			fuenteDto.setFich_shp(((FuenteDto)respuesta.getBody().getObjetoEncapsulado()).getFich_shp());
			fuenteDto.setFich_shx(((FuenteDto)respuesta.getBody().getObjetoEncapsulado()).getFich_shx());
			
			escribirErrores(result, respuesta.getBody());
			model = obtenerListaIndicadores(model, (FuenteDto)respuesta.getBody().getObjetoEncapsulado());
			return cargaTodos(model, request);
			//return "fuenteTile";
		}

		model.addAttribute("fuenteDto", new FuenteDto());
		model.addAttribute("resultado", "exitoBorrar");
		
		return cargaTodos(model, request);
		//return "fuenteTile";
	}

	
	@RequestMapping(method = RequestMethod.GET, params = { "accion=verTablas" })
	public String listarTablasFuente(@RequestParam(value = "id") long id, @RequestParam(value = "tipo") String tipo,@RequestParam(value = "cat") String cat, Model model, HttpServletRequest request) {
		UrlConstructorSW url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		
		url.setParametroCadena("fuentes");
		url.setParametroCadena("fuente");
		url.setParametroCadena(id);

		FuenteDto fuenteDto = getRestTemplate().getForEntity(url.getUrl(), FuenteDto.class).getBody();
		// Se evaluan errores en el servicio web
		model.addAttribute("fuenteDto", fuenteDto);
		
		url=new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		url.setParametroCadena("fuentes");
		url.setParametroCadena("tablasFuente");
		url.setParametroCadena(id);
		url.setParametroCadena(tipo);
		
		//listaFuentesExterna.addAll( (getRestTemplate().getForEntity(url.getUrl(), EncapsuladorListSW.class)).getBody() );

		ResponseEntity<EncapsuladorListSW> lista = getRestTemplate().getForEntity(url.getUrl(), EncapsuladorListSW.class);

		// Se evaluan errores en el servicio web
		if (hasErrorsSW(lista))
			return irPaginaErrorSW();

		model.addAttribute("listaTablas", lista.getBody());
		model.addAttribute("idfuente", id);
		model.addAttribute("tipofuente", tipo);
		if(cat.contains("1"))
		{   
			log.debug("retorna 1  fte interna");
			model.addAttribute("esinterna", "1");
		}
		else
		{   
			log.debug("retorna 0 fte externa");
			model.addAttribute("esinterna", "0");
		}
		
		return "datosFuenteTile";
	}
	
	private Boolean esCoumnaNumerica(ValorFDDto listaDatos){
		for(EncapsuladorStringSW dato:listaDatos.getValores()){
			if(dato.getTexto()!=null && dato.getTexto().trim().length()>0){
				try{
					Float.parseFloat(dato.getTexto());
				}catch (Exception e) {
					return false;
				}
			}
		}
		return true;
	}
	
	@RequestMapping(method = RequestMethod.GET, params = { "accion=verDatos" })
	public String verDatosTablaFuente(@RequestParam(value = "id") long id,@RequestParam(value = "esquema") String esquema, @RequestParam(value = "tabla") String tabla, 
			@RequestParam(value = "tipo") String tipo,@RequestParam(value = "cat") String cat, Model model,HttpServletRequest request) {

		if (esquema.equals(""))
			esquema = "null";
		
		// Lista de tablas
		UrlConstructorSW url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		
		url.setParametroCadena("fuentes");
		url.setParametroCadena("fuente");
		url.setParametroCadena(id);

		FuenteDto fuente = getRestTemplate().getForEntity(url.getUrl(), FuenteDto.class).getBody();
		// Se evaluan errores en el servicio web
		model.addAttribute("fuenteDto", fuente);
		
		url=new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		
		url.setParametroCadena("fuentes");
		url.setParametroCadena("tablasFuente");
		url.setParametroCadena(id);
		url.setParametroCadena(tipo);

		ResponseEntity<EncapsuladorListSW> lista = getRestTemplate().getForEntity(url.getUrl(), EncapsuladorListSW.class);

		// Se evaluan errores en el servicio web
		if (hasErrorsSW(lista))
			return irPaginaErrorSW();

		model.addAttribute("listaTablas", lista.getBody());

		// Datos tabla
		url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		url.setParametroCadena("fuentes");
		url.setParametroCadena("datosTablaFuente");
		url.setParametroCadena(esquema);
		url.setParametroCadena(tabla);
		url.setParametroCadena(id);
		url.setParametroCadena(tipo);

		AtributosMapDto datos = getRestTemplate().getForEntity(url.getUrl(),AtributosMapDto.class).getBody();

		
		// Se evaluan errores en el servicio web
		if (hasErrorsSW(lista))
			return irPaginaErrorSW();

		// Esquema tabla
		url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		url.setParametroCadena("fuentes");
		url.setParametroCadena("esquemaTablaFuente");
		url.setParametroCadena(esquema);
		url.setParametroCadena(tabla);
		url.setParametroCadena(id);
		url.setParametroCadena(tipo);

		EncapsuladorListSW<DescripcionAtributoDto> esquemaTabla = getRestTemplate().getForEntity(url.getUrl(), EncapsuladorListSW.class).getBody();

		// Se evaluan errores en el servicio web
		if (hasErrorsSW(lista))
			return irPaginaErrorSW();

		// Cuantas filas de datos hay?
		Set<String> claves = datos.getContenido().keySet();
		String primeraClave = "";
		int numFilas = 0;
		if (claves.iterator().hasNext()) {
			primeraClave = claves.iterator().next();
			if ( tipo.equals(TiposFuente.GML.getId()))
				numFilas = datos.getContenido().get(primeraClave).getValores().size();
			else
				numFilas = datos.getContenido().get(primeraClave).getValores().size() - 1;
		} else {
			numFilas = -1;
		}
		//necesario para alinear columnas numéricas á dereita
		HashMap<String, Boolean> mapaNumericos= new HashMap<String, Boolean>();
		for(String clave:claves){
			mapaNumericos.put(clave, esCoumnaNumerica(datos.getContenido().get(clave)));
		}
		
		AtributosMapDto mapa = null;
		//Datos geograficos
		if ( tipo.equals(TiposFuente.BDESPACIAL.getId()) || tipo.equals(TiposFuente.GML.getId()) || tipo.equals(TiposFuente.SHAPEFILE.getId()) || tipo.equals(TiposFuente.WFS.getId())) {
			// Datos mapa
			url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
			url.setParametroCadena("fuentes");
			url.setParametroCadena("mapaTablaFuente");
			url.setParametroCadena(esquema);
			url.setParametroCadena(tabla);
			url.setParametroCadena(id);
			url.setParametroCadena(tipo);

			mapa = getRestTemplate().getForEntity(url.getUrl(), AtributosMapDto.class).getBody();

			// Se evaluan errores en el servicio web
			if (hasErrorsSW(lista))
				return irPaginaErrorSW();
		}

		model.addAttribute("tabla", tabla);
		model.addAttribute("esquemaTabla", esquema);
		model.addAttribute("datos", datos.getContenido().entrySet());
		model.addAttribute("mapaNumerico", mapaNumericos);
		if ( mapa != null )
			model.addAttribute("mapa",mapa.getContenido().entrySet());
		else
			model.addAttribute("mapa",mapa);
		model.addAttribute("esquema", esquemaTabla);
		model.addAttribute("numFilas", numFilas);
		model.addAttribute("idfuente", id);
		model.addAttribute("tipofuente", tipo);
		model.addAttribute("tablaSeleccionada", true);
		model.addAttribute("esinterna", cat);
		log.debug("ESINTERNA"+cat);
		FuenteDto fuenteDto = new FuenteDto();
		fuenteDto.setId(id);
		model = obtenerListaIndicadores(model, fuenteDto);

		return "datosFuenteTile";
	}

	@ModelAttribute(value = "tiposFuente")
	public List<TiposFuente> getTiposFuente() {
		List<TiposFuente> items = new ArrayList<TiposFuente>();
		for (TiposFuente tipo : TiposFuente.values())
			items.add(tipo);
		return items;
	}
	
	private ResponseEntity<EncapsuladorPOSTSW> guardarFicheroCSV(MultipartFile file, long id) {
		ResponseEntity<EncapsuladorPOSTSW> respuestaFichero = null;
		if (file != null && file.getSize() > 0) {
			UrlConstructorSW url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
			url.setParametroCadena("ficheros");
			url.setParametroCadena("ficheroCSV");
			
			EncapsuladorFileSW envioFichero = null;
			try {
				envioFichero = new EncapsuladorFileSW(file.getOriginalFilename(), file.getBytes(), id);
			} catch (IOException ex) {
				return null;
			}
			HttpEntity<EncapsuladorFileSW> fichero = new HttpEntity<EncapsuladorFileSW>(envioFichero, getHeaders());
			respuestaFichero = getRestTemplate().postForEntity(url.getUrl(),fichero, EncapsuladorPOSTSW.class);
		} else
			return null;
		return respuestaFichero;
	}
	
	private ResponseEntity<EncapsuladorPOSTSW> guardarFicheroGML(MultipartFile file, long id) {
		ResponseEntity<EncapsuladorPOSTSW> respuestaFichero = null;
		if (file != null && file.getSize() > 0) {
			UrlConstructorSW url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
			url.setParametroCadena("ficheros");
			url.setParametroCadena("ficheroGML");
			
			EncapsuladorFileSW envioFichero = null;
			try {
				envioFichero = new EncapsuladorFileSW(file.getOriginalFilename(), file.getBytes(), id);
			} catch (IOException ex) {
				return null;
			}
			HttpEntity<EncapsuladorFileSW> fichero = new HttpEntity<EncapsuladorFileSW>(envioFichero, getHeaders());
			respuestaFichero = getRestTemplate().postForEntity(url.getUrl(),fichero, EncapsuladorPOSTSW.class);
		} else
			return null;
		return respuestaFichero;
	}
	
	private ResponseEntity<EncapsuladorPOSTSW> guardarFicheroShape(MultipartFile fileShp, long id, String tipo) {
		ResponseEntity<EncapsuladorPOSTSW> respuestaFichero = null;
		if (fileShp != null && fileShp.getSize() > 0) {
			UrlConstructorSW url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
			url.setParametroCadena("ficheros");
			if ( tipo == "shp" )
				url.setParametroCadena("ficheroSHP");
			else if ( tipo == "dbf" )
				url.setParametroCadena("ficheroDBF");
			else if ( tipo == "shx" )
				url.setParametroCadena("ficheroSHX");
			
			EncapsuladorFileSW envioFichero = null;
			try {
				envioFichero = new EncapsuladorFileSW(fileShp.getOriginalFilename(), fileShp.getBytes(),id);
			} catch (IOException ex) {
				return null;
			}
			HttpEntity<EncapsuladorFileSW> fichero = new HttpEntity<EncapsuladorFileSW>(envioFichero, getHeaders());
			respuestaFichero = getRestTemplate().postForEntity(url.getUrl(),fichero, EncapsuladorPOSTSW.class);
		} else
			return null;
		return respuestaFichero;
	}
	
	private FuenteDto rellenarParametrosPost(FuenteDto fuenteDto,HttpServletRequest request){
		Map<String, String> mapaParametorosPost= (Map<String, String>) cargaValorDeSesion(request, "mapaParametorosPost");
		if(mapaParametorosPost!=null){
			String cadena="";
			for(String temp:mapaParametorosPost.keySet()){
				cadena=mapaParametorosPost.get(temp).trim();
				if(temp.equals("nombre")){
					fuenteDto.setNombre(cadena);
				}
				if(temp.equals("id") & !StringUtils.isBlank(cadena)){
					fuenteDto.setId(Long.valueOf(cadena));
				}
				if(temp.equals("infoConexion")){
					fuenteDto.setInfoConexion(cadena);
				}
				if(temp.equals("usuario")){
					fuenteDto.setUsuario(cadena);
				}
				if(temp.equals("tipo") & !StringUtils.isBlank(cadena)){
					fuenteDto.setTipo(TiposFuente.recuperarFuentePorId(Integer.valueOf(cadena)));
				}
				if(temp.equals("descripcion")){
					fuenteDto.setDescripcion(cadena);
				}
				
				if(temp.equals("password")){
					fuenteDto.setPassword(cadena);
				}
			}
		}
		borraEnSesion(request, "mapaParametorosPost");
		return fuenteDto;
	}
	
	@RequestMapping(method = RequestMethod.GET, params = { "accion=probar" })
	public String probarFuente(@RequestParam(value = "id") long id, Model model,HttpServletRequest request) {
		UrlConstructorSW url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		url.setParametroCadena("fuentes");
		url.setParametroCadena("fuente");
		url.setParametroCadena(id);

		FuenteDto fuenteDto = id < 1 ? new FuenteDto() : getRestTemplate().getForEntity(url.getUrl(), FuenteDto.class).getBody();
		
		model.addAttribute("fuenteDto", fuenteDto);
		
		url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		url.setParametroCadena("fuentes");
		url.setParametroCadena("probarFuente");
		url.setParametroCadena(id);
		url.setParametroCadena(fuenteDto.getTipo().getId());

		EncapsuladorBooleanSW res = getRestTemplate().getForEntity(url.getUrl(), EncapsuladorBooleanSW.class).getBody();
		if ( res.getValorLogico() ) {
			model.addAttribute("exitoProbar","jsp.fuente.exito.probar" );
		} else {
			model.addAttribute("errorProbar","jsp.fuente.error.probar" );
		}
		
		model = obtenerListaIndicadores(model, fuenteDto);
		
		return "fuenteTile";
	}
	
	private ResponseEntity<EncapsuladorPOSTSW> guardarFicheroCSVBytes(EncapsuladorFileSW file, long id) {
		ResponseEntity<EncapsuladorPOSTSW> respuestaFichero = null;
		
		UrlConstructorSW url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		url.setParametroCadena("ficheros");
		url.setParametroCadena("ficheroCSV");
		
		EncapsuladorFileSW envioFichero = null;
		envioFichero = new EncapsuladorFileSW(file.getNombre(), file.getFich(), id);
		
		HttpEntity<EncapsuladorFileSW> fichero = new HttpEntity<EncapsuladorFileSW>(envioFichero, getHeaders());
		respuestaFichero = getRestTemplate().postForEntity(url.getUrl(),fichero, EncapsuladorPOSTSW.class);
		
		return respuestaFichero;
	}
	
	private ResponseEntity<EncapsuladorPOSTSW> guardarFicheroGMLBytes(EncapsuladorFileSW file, long id) {
		ResponseEntity<EncapsuladorPOSTSW> respuestaFichero = null;
	
		UrlConstructorSW url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		url.setParametroCadena("ficheros");
		url.setParametroCadena("ficheroGML");
		
		EncapsuladorFileSW envioFichero = null;
		envioFichero = new EncapsuladorFileSW(file.getNombre(), file.getFich(), id);
		
		HttpEntity<EncapsuladorFileSW> fichero = new HttpEntity<EncapsuladorFileSW>(envioFichero, getHeaders());
		respuestaFichero = getRestTemplate().postForEntity(url.getUrl(),fichero, EncapsuladorPOSTSW.class);
		
		return respuestaFichero;
	}
	
	private ResponseEntity<EncapsuladorPOSTSW> guardarFicheroShapeBytes(EncapsuladorFileSW file, long id, String tipo) {
		ResponseEntity<EncapsuladorPOSTSW> respuestaFichero = null;
		UrlConstructorSW url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW());
		url.setParametroCadena("ficheros");
		url.setParametroCadena("ficheroSHP");
		
		EncapsuladorFileSW envioFichero = null;
		envioFichero = new EncapsuladorFileSW(file.getNombre(), file.getFich(),id);
		HttpEntity<EncapsuladorFileSW> fichero = new HttpEntity<EncapsuladorFileSW>(envioFichero, getHeaders());
		respuestaFichero = getRestTemplate().postForEntity(url.getUrl(),fichero, EncapsuladorPOSTSW.class);
		
		return respuestaFichero;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Model obtenerListaIndicadores(Model model, FuenteDto fuenteDto) 
	{
		Map<Long, List<CategoriaDto>> mapaCategorias = new HashMap<Long, List<CategoriaDto>>();
		Map<Long, List<IndicadorDto>> mapaIndicadores = new HashMap<Long, List<IndicadorDto>>();

		if (getServicioSeguridad().getUserDetails().getEsAdmin()) {
			// Se recuperan las categorías e indicadores raíz
			UrlConstructorSW urlCat = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW()).setParametroCadena("categorias").setParametroCadena("cargaCategoriasPadreParaFuentes");
			urlCat.setParametroCadena(fuenteDto.getId());
			UrlConstructorSW urlInd = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW()).setParametroCadena("indicadores").setParametroCadena("cargaSinCategoriaPorFuente");
			urlInd.setParametroCadena(fuenteDto.getId());
			
			List<CategoriaDto> listaCategorias = getRestTemplate().getForEntity(urlCat.getUrl(), EncapsuladorListSW.class).getBody();
			List<Long> categoriasPadre = new ArrayList<Long>();
			
			if (listaCategorias != null && listaCategorias.size() > 0)
			{
				mapaCategorias.put(0L, listaCategorias);
			}
			
			List<IndicadorDto> listaIndicadores = getRestTemplate().getForEntity(urlInd.getUrl(), EncapsuladorListSW.class).getBody();
			
			if (listaIndicadores != null && listaIndicadores.size() > 0)
			{
				mapaIndicadores.put(0L, listaIndicadores);
			}

			urlCat.borraUltimoParametroCadena().borraUltimoParametroCadena().setParametroCadena("cargaCategoriasPorPadreParaFuentes");
			urlInd.borraUltimoParametroCadena().borraUltimoParametroCadena().setParametroCadena("cargaPorCategoriaPorFuente");
		
			int indiceMapaCategorias = 0;
			// Se recorren todas las categorías, para buscar las categorías/indicadores que de ellas dependen
			while (indiceMapaCategorias < mapaCategorias.size())
			{
				Iterator<Entry<Long, List<CategoriaDto>>> it = mapaCategorias.entrySet().iterator();
				
				// Se recorre el mapa buscando el índice cuyo valor coincida con el id de la categoría padre
				while (it.hasNext()) 
				{
					Map.Entry elementoActual = (Map.Entry)it.next();
					
					// Si las categorías son Hijo de la Categoría Padre buscada
					if (elementoActual != null && elementoActual.getKey() != null && !categoriasPadre.contains(((Long)elementoActual.getKey()).longValue()))
					{
						List<CategoriaDto> categorias = (List<CategoriaDto>)elementoActual.getValue();
						
						for(CategoriaDto categoriaMadre : categorias)
						{
							// Se buscan categorías hijo
							urlCat.setParametroCadena(categoriaMadre.getId());
							urlCat.setParametroCadena(fuenteDto.getId());
							List<CategoriaDto> listaCategoriasHijo = getRestTemplate().getForEntity(urlCat.getUrl(), EncapsuladorListSW.class).getBody();
							// Si se obtienen resultados
							if (listaCategoriasHijo != null && listaCategoriasHijo.size() > 0)
							{
								mapaCategorias.put(categoriaMadre.getId(), listaCategoriasHijo);
							}
							
							// Se buscan indicadores que cuelguen de esta categoría
							urlInd.setParametroCadena(categoriaMadre.getId());
							urlInd.setParametroCadena(fuenteDto.getId());
							List<IndicadorDto> listaIndicadoresHijo = getRestTemplate().getForEntity(urlInd.getUrl(), EncapsuladorListSW.class).getBody();
							// Si se obtienen resultados
							if (listaIndicadoresHijo != null && listaIndicadoresHijo.size() > 0)
							{
								mapaIndicadores.put(categoriaMadre.getId(), listaIndicadoresHijo);
							}
			
							// Se resetean los parámetros de los servicios Web
							urlCat.borraUltimoParametroCadena().borraUltimoParametroCadena();
							urlInd.borraUltimoParametroCadena().borraUltimoParametroCadena();
						}
						
						// Se actualiza el índice en el mapa de categorías
						indiceMapaCategorias++;
						// Se añade el IdCategoriaPadre actual a la lista de revisados
						categoriasPadre.add(((Long)elementoActual.getKey()).longValue());
						
						break;
					}
				}
			}
			
			model.addAttribute("mapaCategorias", mapaCategorias);
			model.addAttribute("mapaIndicadores", mapaIndicadores);
		} 
		else 
		{
			UrlConstructorSW url = new UrlConstructorSWImpl(getServicioConfiguracionGeneral().getUrlBaseSW()).setParametroCadena("indicadores").setParametroCadena("cargaTodasCategoriasYIndicadoresPorUsuarioVisualizarPorFuente").setParametroCadena(getServicioSeguridad().getUserDetails().getId()).setParametroCadena(fuenteDto.getId());
			EncapsuladorMapSW<String, EncapsuladorMapSW<Long, EncapsuladorListSW<ElementoJerarquiaDto>>> resultado= getRestTemplate().getForEntity(url.getUrl(),EncapsuladorMapSW.class).getBody();
			
			model.addAttribute("mapaIndicadores", writeJson(resultado.get("Indicadores")));
			model.addAttribute("mapaCategorias", writeJson(resultado.get("Categorias")));
		}

		return model;
	}	
	
}