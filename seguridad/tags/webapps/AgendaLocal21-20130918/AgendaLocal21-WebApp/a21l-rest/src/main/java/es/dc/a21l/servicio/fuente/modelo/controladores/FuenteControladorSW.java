/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.servicio.fuente.modelo.controladores;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import es.dc.a21l.base.modelo.GestionErrores.EncapsuladorErroresSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorBooleanSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorFileSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorListSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorPOSTSW;
import es.dc.a21l.base.utils.enumerados.TiposFuente;
import es.dc.a21l.base.modelo.utils.UtilFecha;

import es.dc.a21l.fuente.cu.AtributoFuenteDatosDto;
import es.dc.a21l.fuente.cu.AtributosMapDto;
import es.dc.a21l.fuente.cu.DescripcionAtributoDto;
import es.dc.a21l.fuente.cu.FuenteDto;
import es.dc.a21l.fuente.cu.GestorCUFuente;
import es.dc.a21l.fuente.cu.TablaFuenteDatosDto;
import es.dc.a21l.servicio.controladores.GenericAbstractController;

/*
 * SERVICIO WEB REST
 */

@Controller
@RequestMapping(value = "/fuentes")
public class FuenteControladorSW extends GenericAbstractController {
	private static final Logger log = LoggerFactory.getLogger(FuenteControladorSW.class);
	private static final String ERROR_PARAM_CONFIG = "__ERROR__PARAM__CONFIG__";
	
	private GestorCUFuente gestorCUFuente;
	
	@Autowired
	public void setGestorCUFuente(GestorCUFuente gestorCUFUEnte) {
		this.gestorCUFuente = gestorCUFUEnte;
	}
	
	private HttpHeaders responseHeaders = new HttpHeaders();
	{responseHeaders.add("Content-Type", "application/xml; charset=utf-8");}
		
	@RequestMapping(method = RequestMethod.GET, value = "/listaFuentes")
	public ResponseEntity<EncapsuladorListSW<FuenteDto>> cargaTodos() {

		return new ResponseEntity<EncapsuladorListSW<FuenteDto>>(gestorCUFuente.cargaTodos(), responseHeaders, HttpStatus.OK);
	}	
	
	
	@RequestMapping(method = RequestMethod.GET, value = "/cargaFuenteInterna/{idUsuarioCreador}")
	public ResponseEntity<FuenteDto> CargaCatalogo(@PathVariable("idUsuarioCreador") Long idUsuarioCreador) {

		FuenteDto fuenteCatalogo;
		FuenteDto fuenteDto = new FuenteDto();		
		fuenteDto.setEsCatalogoInterno((short) 1);
		Date data;
		String strData;
		try {
			
			strData = getServicioConfiguracionGeneral().getCatalogoData();
			data = UtilFecha.multiParse(strData);
			fuenteDto.setFechaRegistro(data);
			fuenteDto.setNombre(getServicioConfiguracionGeneral().getCatalogoNome());
			fuenteDto.setInfoConexion(getServicioConfiguracionGeneral().getCatalogoUrl());
			fuenteDto.setPassword(getServicioConfiguracionGeneral().getCatalogoPass());
			fuenteDto.setUsuario(getServicioConfiguracionGeneral().getCatalogoUser());
			fuenteDto.setId(0);
		} 
		catch (Exception e) {
			log.error("Error excetion ERROR_PARAM_CONFIG ");
			fuenteDto.setNombre(ERROR_PARAM_CONFIG);
			return new ResponseEntity<FuenteDto>(fuenteDto, responseHeaders,HttpStatus.OK);
		}
		
	    try {	
	    
			if (fuenteDto.getNombre().isEmpty() || fuenteDto.getInfoConexion().isEmpty() || data == null ) {
				log.error("Error parametros vacions "+ERROR_PARAM_CONFIG +strData);
				fuenteDto.setNombre(ERROR_PARAM_CONFIG);
				return new ResponseEntity<FuenteDto>(fuenteDto, responseHeaders,HttpStatus.OK);
			} 
			else 
			{
				// comprobamos si existe la fuente con los datos del fichero de
				// configuración despliegue.properties en BD
				 fuenteCatalogo = gestorCUFuente.existeFuenteCatalogo(fuenteDto);

				// sino existe la fuente la insertamos en la DB
				if (fuenteCatalogo == null || fuenteCatalogo.getId() == 0) 
				{
					fuenteCatalogo = gestorCUFuente.gardaFuenteCatalogo(fuenteDto,idUsuarioCreador);				
				}				
			}
		} 
		catch (Exception e) {
			 log.error("Error segunda exception fuente vacia" );
			return new ResponseEntity<FuenteDto>(new FuenteDto(),responseHeaders, HttpStatus.OK);
		}
	    log.debug("EXITO al cargar la fuente: "+ fuenteCatalogo.getNombre());
		return new ResponseEntity<FuenteDto>(fuenteCatalogo, responseHeaders,HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/listaFuentesInt")
	public ResponseEntity<EncapsuladorListSW<FuenteDto>> listarFuentesInternas() {		

		return new ResponseEntity<EncapsuladorListSW<FuenteDto>>(gestorCUFuente.listarFuentesInternas(), responseHeaders,HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/listaFuentesExt")
	public ResponseEntity<EncapsuladorListSW<FuenteDto>> listarFuentesExternas() {
		return new ResponseEntity<EncapsuladorListSW<FuenteDto>>(gestorCUFuente.listarFuentesExternas(), responseHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/fuente/{id}")
	public ResponseEntity<FuenteDto> cargaPorId(@PathVariable("id") Long id) {
		return new ResponseEntity<FuenteDto>(gestorCUFuente.carga(id), responseHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/fuente/{idUsuarioCreador}")
	public ResponseEntity<EncapsuladorPOSTSW> guardar(@PathVariable("idUsuarioCreador") Long idUsuarioCreador, @RequestBody FuenteDto fuenteDto,HttpServletRequest request) {
		EncapsuladorErroresSW errores= new EncapsuladorErroresSW();
		String sistemaCoordenadas= getServicioConfiguracionGeneral().getCodigoCordenadas();
		fuenteDto=gestorCUFuente.garda(fuenteDto, idUsuarioCreador, errores,sistemaCoordenadas);
		EncapsuladorPOSTSW<FuenteDto> encapsulador=new EncapsuladorPOSTSW<FuenteDto>(fuenteDto, errores);
		ResponseEntity<EncapsuladorPOSTSW> respuesta= new ResponseEntity<EncapsuladorPOSTSW>(encapsulador, HttpStatus.OK);
		return respuesta;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/tablasFuente/{id}/{tipo}")
	public ResponseEntity<EncapsuladorListSW<TablaFuenteDatosDto>> listaTablas(@PathVariable("id") Long id,@PathVariable("tipo") String tipo, HttpServletRequest request) {
		String path;
		
		if ( tipo.equals(TiposFuente.CSV.getId()))
			path = getServicioConfiguracionGeneral().getPathCsv();
		else if ( tipo.equals(TiposFuente.GML.getId()))
			path = getServicioConfiguracionGeneral().getPathGml();
		else if ( tipo.equals(TiposFuente.SHAPEFILE.getId()))
			path = getServicioConfiguracionGeneral().getPathShapefile();
		else
			path = null;
		String realPath = getPathRealAplicacion(request, path);
		return new ResponseEntity<EncapsuladorListSW<TablaFuenteDatosDto>>(gestorCUFuente.listarTablasFuenteExterna(id,realPath), responseHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/columnasFuente/{tabla}/{id}/{tipo}")
	public ResponseEntity<EncapsuladorListSW<AtributoFuenteDatosDto>> listaColumnas(@PathVariable("tabla") String tabla,@PathVariable("id") Long id, @PathVariable("tipo") String tipo, HttpServletRequest request) {
		String path;
		
		if ( tipo.equals(TiposFuente.CSV.getId()))
			path = getServicioConfiguracionGeneral().getPathCsv();
		else if ( tipo.equals(TiposFuente.GML.getId()))
			path = getServicioConfiguracionGeneral().getPathGml();
		else if ( tipo.equals(TiposFuente.SHAPEFILE.getId()))
			path = getServicioConfiguracionGeneral().getPathShapefile();
		else
			path = null;
		
		String realPath = getPathRealAplicacion(request, path);
		String caracterSeparador = getServicioConfiguracionGeneral().getCaracterSeparadorCSV();
		
		return new ResponseEntity<EncapsuladorListSW<AtributoFuenteDatosDto>>(gestorCUFuente.listarColumnasTablaFuenteExterna(id, tabla, realPath, caracterSeparador), responseHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/datosTablaFuente/{esquema}/{tabla}/{id}/{tipo}")
	public ResponseEntity<AtributosMapDto> datosTablaFuente(@PathVariable("esquema") String esquema,@PathVariable("tabla") String tabla,@PathVariable("id") Long id, 
			@PathVariable("tipo") String tipo, HttpServletRequest request) {
		String path;
		
		if ( tipo.equals(TiposFuente.CSV.getId()))
			path = getServicioConfiguracionGeneral().getPathCsv();
		else if ( tipo.equals(TiposFuente.GML.getId()))
			path = getServicioConfiguracionGeneral().getPathGml();
		else if ( tipo.equals(TiposFuente.SHAPEFILE.getId()))
			path = getServicioConfiguracionGeneral().getPathShapefile();
		else
			path = null;
		
		String caracterSeparador = getServicioConfiguracionGeneral().getCaracterSeparadorCSV();
		String realPath = getPathRealAplicacion(request, path);
		ResponseEntity<AtributosMapDto> respuesta= new ResponseEntity<AtributosMapDto>(gestorCUFuente.obtenerDatosTablaFuenteExterna(id, esquema, tabla, realPath, caracterSeparador), responseHeaders, HttpStatus.OK);
		
		return respuesta;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/esquemaTablaFuente/{esquema}/{tabla}/{id}/{tipo}")
	public ResponseEntity<EncapsuladorListSW<DescripcionAtributoDto>> esquemaTablaFuente(@PathVariable("esquema") String esquema,@PathVariable("tabla") String tabla,@PathVariable("id") Long id, @PathVariable("tipo") String tipo, HttpServletRequest request) {
		String path;
		
		if ( tipo.equals(TiposFuente.CSV.getId()))
			path = getServicioConfiguracionGeneral().getPathCsv();
		else if ( tipo.equals(TiposFuente.GML.getId()))
			path = getServicioConfiguracionGeneral().getPathGml();
		else if ( tipo.equals(TiposFuente.SHAPEFILE.getId()))
			path = getServicioConfiguracionGeneral().getPathShapefile();
		else
			path = null;
		
		String caracterSeparador = getServicioConfiguracionGeneral().getCaracterSeparadorCSV();
		String realPath = getPathRealAplicacion(request, path);
		ResponseEntity<EncapsuladorListSW<DescripcionAtributoDto>> respuesta= new ResponseEntity<EncapsuladorListSW<DescripcionAtributoDto>>(gestorCUFuente.obtenerEsquemaTablaFuenteExterna(id, esquema, tabla, realPath, caracterSeparador), responseHeaders, HttpStatus.OK);
		return respuesta;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/mapaTablaFuente/{esquema}/{tabla}/{id}/{tipo}")
	public ResponseEntity<AtributosMapDto> mapaTablaFuente(@PathVariable("esquema") String esquema,@PathVariable("tabla") String tabla,@PathVariable("id") Long id, @PathVariable("tipo") String tipo, HttpServletRequest request) {
		String path;
		
		if ( tipo.equals(TiposFuente.CSV.getId()))
			path = getServicioConfiguracionGeneral().getPathCsv();
		else if ( tipo.equals(TiposFuente.GML.getId()))
			path = getServicioConfiguracionGeneral().getPathGml();
		else if ( tipo.equals(TiposFuente.SHAPEFILE.getId()))
			path = getServicioConfiguracionGeneral().getPathShapefile();
		else
			path = null;
		
		String caracterSeparador = getServicioConfiguracionGeneral().getCaracterSeparadorCSV();
		String realPath = getPathRealAplicacion(request, path);
		ResponseEntity<AtributosMapDto> respuesta= new ResponseEntity<AtributosMapDto>(gestorCUFuente.obtenerMapaTablaFuenteExterna(id, esquema, tabla, realPath), responseHeaders, HttpStatus.OK);
		
		return respuesta;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/borraFuente/{id}/{tipo}")
	public ResponseEntity<EncapsuladorPOSTSW> borrar(@PathVariable("id") Long id, @PathVariable("tipo") String tipo,HttpServletRequest request) {
		String path;
		
		if ( tipo.equals(TiposFuente.CSV.getId()))
			path = getServicioConfiguracionGeneral().getPathCsv();
		else if ( tipo.equals(TiposFuente.GML.getId()))
			path = getServicioConfiguracionGeneral().getPathGml();
		else if ( tipo.equals(TiposFuente.SHAPEFILE.getId()))
			path = getServicioConfiguracionGeneral().getPathShapefile();
		else
			path = null;
		
		String realPath = getPathRealAplicacion(request, path);
		EncapsuladorErroresSW errores= new EncapsuladorErroresSW();
		EncapsuladorPOSTSW<FuenteDto> encapsulador=new EncapsuladorPOSTSW<FuenteDto>(gestorCUFuente.borra(id,realPath,errores), errores);
		ResponseEntity<EncapsuladorPOSTSW> respuesta= new ResponseEntity<EncapsuladorPOSTSW>(encapsulador, HttpStatus.OK);
		return respuesta;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/fuenteCatalogo")
	public ResponseEntity<FuenteDto> cargaFuenteCatalogo() {
		FuenteDto fuenteCatalogo;
		FuenteDto fuenteDto = new FuenteDto();		
		fuenteDto.setEsCatalogoInterno((short) 1);
		Date data;
		String strData;
		try {
			
			strData = getServicioConfiguracionGeneral().getCatalogoData();
			data = UtilFecha.multiParse(strData);
			fuenteDto.setFechaRegistro(data);
			fuenteDto.setNombre(getServicioConfiguracionGeneral().getCatalogoNome());
			fuenteDto.setInfoConexion(getServicioConfiguracionGeneral().getCatalogoUrl());
			fuenteDto.setPassword(getServicioConfiguracionGeneral().getCatalogoPass());
			fuenteDto.setUsuario(getServicioConfiguracionGeneral().getCatalogoUser());
			fuenteDto.setId(0);
		} 
		catch (Exception e) {
			log.error("Error excetion ERROR_PARAM_CONFIG ");
			fuenteDto.setNombre(ERROR_PARAM_CONFIG);
			
		}
		return new ResponseEntity<FuenteDto>(fuenteDto, responseHeaders,HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/probarFuente/{id}/{tipo}")
	public ResponseEntity<EncapsuladorBooleanSW> probarFuente(@PathVariable("id") Long id, @PathVariable("tipo") String tipo, HttpServletRequest request) {
		String path;
		String caracterSeparador = getServicioConfiguracionGeneral().getCaracterSeparadorCSV();
		
		if ( tipo.equals(TiposFuente.CSV.getId()))
			path = getServicioConfiguracionGeneral().getPathCsv();
		else if ( tipo.equals(TiposFuente.GML.getId()))
			path = getServicioConfiguracionGeneral().getPathGml();
		else if ( tipo.equals(TiposFuente.SHAPEFILE.getId()))
			path = getServicioConfiguracionGeneral().getPathShapefile();
		else
			path = null;
		
		String realPath = getPathRealAplicacion(request, path);
		return new ResponseEntity<EncapsuladorBooleanSW>(gestorCUFuente.probarFuente(id,realPath,caracterSeparador), responseHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/obtenerFichero/{id}/{tipo}")
	public ResponseEntity<EncapsuladorFileSW> obtenerFichero(@PathVariable("id") Long id, @PathVariable("tipo") String tipo, HttpServletRequest request) {
		String path;
		String caracterSeparador = getServicioConfiguracionGeneral().getCaracterSeparadorCSV();
		FuenteDto fuenteDto = gestorCUFuente.carga(id);
		String fich = "";
		if ( tipo.equals(TiposFuente.CSV.getId())) {
			path = getServicioConfiguracionGeneral().getPathCsv();
			fich = fuenteDto.getFich_csv_gml();
		} else if ( tipo.equals(TiposFuente.GML.getId())) {
			path = getServicioConfiguracionGeneral().getPathGml();
			fich = fuenteDto.getFich_csv_gml();
		} else if ( tipo.equals(TiposFuente.SHAPEFILE.getId())) {
			path = getServicioConfiguracionGeneral().getPathShapefile();
			fich = fuenteDto.getFich_shp();
		} else {
			path = null;
			fich = "";
		}
		
		String realPath = getPathRealAplicacion(request, path);
		
		RandomAccessFile f = null;
		try {		
			File file = new File(realPath+fuenteDto.getId()+"/"+fich);
			
			f = new RandomAccessFile(file, "r");
			byte[] contenido = new byte[(int)f.length()];
			f.read(contenido);
	
			EncapsuladorFileSW encapsuladorFich = new EncapsuladorFileSW();
			encapsuladorFich.setFich(contenido);
			encapsuladorFich.setNombre(fich);
			ResponseEntity<EncapsuladorFileSW> respuesta= new ResponseEntity<EncapsuladorFileSW>(encapsuladorFich, HttpStatus.OK);
			return respuesta;
		} catch (FileNotFoundException e) {
			log.error("Fichero: "+fich+" no encontrado para descarga");
			return new ResponseEntity<EncapsuladorFileSW>(new EncapsuladorFileSW(), HttpStatus.OK);
		} catch (Exception e) {
			log.error("Se ha producido un error al acceder al fichero: "+fich);
			return new ResponseEntity<EncapsuladorFileSW>(new EncapsuladorFileSW(), HttpStatus.OK);
		} finally {
			try {
				if ( f!=null)
					f.close();
			} catch (IOException e) {
				log.error("No se pudo cerrar la conexion con el fichero");
			}
		}
	}
}