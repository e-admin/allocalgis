/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.servicio.fuente.modelo.controladores;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

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
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorPOSTSW;
import es.dc.a21l.elementoJerarquia.cu.IndicadorDto;
import es.dc.a21l.fuente.cu.GestorCUFuente;
import es.dc.a21l.metadatos.cu.GestorCUMetadatos;
import es.dc.a21l.metadatos.cu.MetadatosDto;
import es.dc.a21l.servicio.controladores.GenericAbstractController;

/*
 * SERVICIO WEB REST
 */

@Controller
@RequestMapping(value = "/ficheros")
public class FicheroControladorSW extends GenericAbstractController {
	private static final Logger log = LoggerFactory.getLogger(FicheroControladorSW.class);
	
	private GestorCUFuente gestorCUFuente;
	private GestorCUMetadatos gestorCUMetadatos;
	
	@Autowired
	public void setGestorCUFuente(GestorCUFuente gestorCUFUEnte) {
		this.gestorCUFuente = gestorCUFUEnte;
	}
	
	@Autowired
	public void setGestorCUMetadatos(GestorCUMetadatos gestorCUMetadatos) {
		this.gestorCUMetadatos = gestorCUMetadatos;
	}
	
	private HttpHeaders responseHeaders = new HttpHeaders();
	{responseHeaders.add("Content-Type", "application/xml; charset=utf-8");}
	
	@RequestMapping(method = RequestMethod.POST, value = "/ficheroCSV")
	public ResponseEntity<EncapsuladorPOSTSW> guardarFicheroCSV(@RequestBody EncapsuladorFileSW fich,HttpServletRequest request) {
		String path = getPathRealAplicacion(request, getServicioConfiguracionGeneral().getPathCsv());
		EncapsuladorErroresSW errores= new EncapsuladorErroresSW();
		gestorCUFuente.guardaFichero(fich, path,  errores, true);
		EncapsuladorPOSTSW<EncapsuladorFileSW> encapsulador=new EncapsuladorPOSTSW<EncapsuladorFileSW>(fich, errores);
		ResponseEntity<EncapsuladorPOSTSW> respuesta= new ResponseEntity<EncapsuladorPOSTSW>(encapsulador, HttpStatus.OK);
		return respuesta;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/ficheroGML")
	public ResponseEntity<EncapsuladorPOSTSW> guardarFicheroGML(@RequestBody EncapsuladorFileSW fich,HttpServletRequest request) {
		String path = getPathRealAplicacion(request, getServicioConfiguracionGeneral().getPathGml());
		EncapsuladorErroresSW errores= new EncapsuladorErroresSW();
		gestorCUFuente.guardaFichero(fich, path,  errores, true);
		EncapsuladorPOSTSW<EncapsuladorFileSW> encapsulador=new EncapsuladorPOSTSW<EncapsuladorFileSW>(fich, errores);
		ResponseEntity<EncapsuladorPOSTSW> respuesta= new ResponseEntity<EncapsuladorPOSTSW>(encapsulador, HttpStatus.OK);
		return respuesta;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/ficheroSHP")
	public ResponseEntity<EncapsuladorPOSTSW> guardarFicheroSHP(@RequestBody EncapsuladorFileSW fich,HttpServletRequest request) {
		String path = getPathRealAplicacion(request, getServicioConfiguracionGeneral().getPathShapefile());
		EncapsuladorErroresSW errores= new EncapsuladorErroresSW();
		gestorCUFuente.guardaFichero(fich, path,  errores, true);
		EncapsuladorPOSTSW<EncapsuladorFileSW> encapsulador=new EncapsuladorPOSTSW<EncapsuladorFileSW>(fich, errores);
		ResponseEntity<EncapsuladorPOSTSW> respuesta= new ResponseEntity<EncapsuladorPOSTSW>(encapsulador, HttpStatus.OK);
		return respuesta;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/ficheroDBF")
	public ResponseEntity<EncapsuladorPOSTSW> guardarFicheroDBF(@RequestBody EncapsuladorFileSW fich,HttpServletRequest request) {
		String path = getPathRealAplicacion(request, getServicioConfiguracionGeneral().getPathShapefile());
		EncapsuladorErroresSW errores= new EncapsuladorErroresSW();
		gestorCUFuente.guardaFichero(fich, path,  errores, false);
		EncapsuladorPOSTSW<EncapsuladorFileSW> encapsulador=new EncapsuladorPOSTSW<EncapsuladorFileSW>(fich, errores);
		ResponseEntity<EncapsuladorPOSTSW> respuesta= new ResponseEntity<EncapsuladorPOSTSW>(encapsulador, HttpStatus.OK);
		return respuesta;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/ficheroSHX")
	public ResponseEntity<EncapsuladorPOSTSW> guardarFicheroSHX(@RequestBody EncapsuladorFileSW fich,HttpServletRequest request) {
		String path = getPathRealAplicacion(request, getServicioConfiguracionGeneral().getPathShapefile());
		EncapsuladorErroresSW errores= new EncapsuladorErroresSW();
		gestorCUFuente.guardaFichero(fich, path,  errores, false);
		EncapsuladorPOSTSW<EncapsuladorFileSW> encapsulador=new EncapsuladorPOSTSW<EncapsuladorFileSW>(fich, errores);
		ResponseEntity<EncapsuladorPOSTSW> respuesta= new ResponseEntity<EncapsuladorPOSTSW>(encapsulador, HttpStatus.OK);
		return respuesta;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/ficheroMetadatos")
	public ResponseEntity<EncapsuladorPOSTSW> guardarFicheroMetadatos(@RequestBody EncapsuladorFileSW fich,HttpServletRequest request) {
		String path = getPathRealAplicacion(request, getServicioConfiguracionGeneral().getPathMetadatos());
		EncapsuladorErroresSW errores= new EncapsuladorErroresSW();
		gestorCUMetadatos.guardaFichero(fich, path,  errores);
		EncapsuladorPOSTSW<EncapsuladorFileSW> encapsulador=new EncapsuladorPOSTSW<EncapsuladorFileSW>(fich, errores);
		ResponseEntity<EncapsuladorPOSTSW> respuesta= new ResponseEntity<EncapsuladorPOSTSW>(encapsulador, HttpStatus.OK);
		return respuesta;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/borrarFicheroMetadatos")
	public ResponseEntity<EncapsuladorPOSTSW> borrarFicheroMetadatos(@RequestBody IndicadorDto indicadorDto,HttpServletRequest request) {
		String path = getPathRealAplicacion(request, getServicioConfiguracionGeneral().getPathMetadatos());
		EncapsuladorErroresSW errores= new EncapsuladorErroresSW();
		MetadatosDto metaDatos = gestorCUMetadatos.cargaPorIdIndicador(indicadorDto.getId());
		boolean res = gestorCUMetadatos.borrarFichero(metaDatos.getMetadatos(),indicadorDto.getId(), path,  errores);
		EncapsuladorBooleanSW encapBoolean = new EncapsuladorBooleanSW();
		encapBoolean.setValorLogico(res);
		EncapsuladorPOSTSW<EncapsuladorBooleanSW> encapsulador=new EncapsuladorPOSTSW<EncapsuladorBooleanSW>(encapBoolean, errores);
		ResponseEntity<EncapsuladorPOSTSW> respuesta= new ResponseEntity<EncapsuladorPOSTSW>(encapsulador, HttpStatus.OK);
		return respuesta;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/descargarMetadatos/{id}")
	public ResponseEntity<EncapsuladorFileSW> descargarMetadatos(@PathVariable("id") Long id,HttpServletRequest request) {		
		MetadatosDto metadatos = gestorCUMetadatos.cargaPorIdIndicador(id);
		RandomAccessFile f = null;
		try {
			String path = getPathRealAplicacion(request, getServicioConfiguracionGeneral().getPathMetadatos());
			File file = new File(path+id+"/"+metadatos.getMetadatos());
			
			f = new RandomAccessFile(file, "r");
			byte[] contenido = new byte[(int)f.length()];
			f.read(contenido);

			EncapsuladorFileSW encapsuladorFich = new EncapsuladorFileSW();
			encapsuladorFich.setFich(contenido);
			encapsuladorFich.setNombre(metadatos.getMetadatos());
			encapsuladorFich.setIdIndicador(id);
			ResponseEntity<EncapsuladorFileSW> respuesta= new ResponseEntity<EncapsuladorFileSW>(encapsuladorFich, HttpStatus.OK);
			f.close();
			return respuesta;
		} catch (FileNotFoundException e) {
			log.debug("Fichero: "+metadatos.getMetadatos()+" no encontrado para descarga");
			return new ResponseEntity<EncapsuladorFileSW>(new EncapsuladorFileSW(), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			log.debug("Se ha producido un error al acceder al fichero: "+metadatos.getMetadatos());
			return new ResponseEntity<EncapsuladorFileSW>(new EncapsuladorFileSW(), HttpStatus.OK);
		} finally {
			try {
				if ( f!=null )
					f.close();
			} catch (Exception e) {
				log.debug("no se pudo cerrar la conexion con el fichero de metadatos");
			}
		}
	}
}