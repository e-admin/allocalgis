/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.servicio.indicador.modelo.controladores;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.geotools.GML;
import org.geotools.GML.Version;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.FeatureCollections;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.WKTReader2;
import org.opengis.feature.simple.SimpleFeatureType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.xml.sax.SAXException;

import au.com.bytecode.opencsv.CSVWriter;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPolygon;

import es.dc.a21l.base.modelo.GestionErrores.EncapsuladorErroresSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorBooleanSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorFileSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorListSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorMapSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorPOSTSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorStringSW;
import es.dc.a21l.base.modelo.utils.UtilFecha;
import es.dc.a21l.base.utils.enumerados.TipoAtributoFD;
import es.dc.a21l.base.utils.enumerados.TipoModificacionEnum;
import es.dc.a21l.base.utils.enumerados.TiposFuente;
import es.dc.a21l.correo.cu.impl.GestorCUCorreoImpl;
import es.dc.a21l.criterio.cu.GestorCUCriterio;
import es.dc.a21l.elementoJerarquia.cu.CapaBaseDto;
import es.dc.a21l.elementoJerarquia.cu.ElementoJerarquiaDto;
import es.dc.a21l.elementoJerarquia.cu.GestorCUCapaBase;
import es.dc.a21l.elementoJerarquia.cu.GestorCUIndicador;
import es.dc.a21l.elementoJerarquia.cu.GestorCUIndicadorExpresion;
import es.dc.a21l.elementoJerarquia.cu.GestorCUIndicadorUsuarioModificacion;
import es.dc.a21l.elementoJerarquia.cu.GestorCURelacion;
import es.dc.a21l.elementoJerarquia.cu.IndicadorBusquedaAvanzadaDto;
import es.dc.a21l.elementoJerarquia.cu.IndicadorDto;
import es.dc.a21l.elementoJerarquia.cu.IndicadorUsuarioDto;
import es.dc.a21l.elementoJerarquia.cu.impl.GestorCUIndicadorUsuarioModificacionImpl;
import es.dc.a21l.fuente.cu.AtributoDto;
import es.dc.a21l.fuente.cu.AtributoFuenteDatosDto;
import es.dc.a21l.fuente.cu.AtributosMapDto;
import es.dc.a21l.fuente.cu.GestorCUAtributo;
import es.dc.a21l.fuente.cu.TablaFuenteDatosDto;
import es.dc.a21l.fuente.cu.ValorFDDto;
import es.dc.a21l.historico.cu.GestorCUHistorico;
import es.dc.a21l.metadatos.cu.GestorCUMetadatos;
import es.dc.a21l.metadatos.cu.MetadatosDto;
import es.dc.a21l.servicio.controladores.GenericAbstractController;
import es.dc.a21l.servicio.servicios.ServicioConfiguracionGeneral;
import es.dc.a21l.usuario.cu.GestorCUUsuario;
import es.dc.a21l.usuario.cu.GestorCUUsuarioPermiso;
import es.dc.a21l.usuario.cu.UsuarioDto;
import es.dc.a21l.visualizacion.cu.EstiloVisualizacionDiagramaBarrasDto;
import es.dc.a21l.visualizacion.cu.EstiloVisualizacionDiagramaSectoresDto;
import es.dc.a21l.visualizacion.cu.EstiloVisualizacionMapaDto;
import es.dc.a21l.visualizacion.cu.EstiloVisualizacionTablaDto;
import es.dc.a21l.visualizacion.cu.GestorCUEstiloVisualizacionDiagramaBarras;
import es.dc.a21l.visualizacion.cu.GestorCUEstiloVisualizacionDiagramaSectores;
import es.dc.a21l.visualizacion.cu.GestorCUEstiloVisualizacionMapa;
import es.dc.a21l.visualizacion.cu.GestorCUEstiloVisualizacionTabla;
import es.dc.a21l.visualizacion.cu.GestorCURangosVisualizacionMapa;
import es.dc.a21l.visualizacion.cu.RangosVisualizacionMapaDto;

@Controller
@RequestMapping(value = "/indicadores")
public class IndicadorControladorSW extends GenericAbstractController {

	private static final String DEFAULT_ENCODE = "ISO-8859-1";
	private GestorCUIndicador gestorCUIndicador;
	private GestorCUMetadatos gestorCUMetadatos;
	private GestorCUAtributo gestorCUAtributo;
	private GestorCUIndicadorExpresion gestorCUIndicadorExpresion;
	private GestorCUEstiloVisualizacionTabla gestorCUEstiloVisualizacionTabla;
	private GestorCUEstiloVisualizacionDiagramaBarras gestorCUEstiloVisualizacionDiagramaBarras;
	private GestorCUEstiloVisualizacionDiagramaSectores gestorCUEstiloVisualizacionDiagramaSectores;
	private GestorCUEstiloVisualizacionMapa gestorCUEstiloVisualizacionMapa;
	private GestorCURangosVisualizacionMapa gestorCURangosVisualizacionMapa;
	private GestorCUIndicadorUsuarioModificacion gestorCUIndicadorUsuarioModificacion;
	private GestorCUUsuario gestorCUUsuario;
	private GestorCUHistorico gestorCUHistorico;
	private GestorCURelacion gestorCURelacion;
	private GestorCUCriterio gestorCUCriterio;
	private GestorCUUsuarioPermiso gestorCUUsuarioPermiso;
	private GestorCUCapaBase gestorCUCapaBase;
	private final static String SPRING_MAIL_CONTEXT = "/spring/mail-context.xml";
	private static final String PREFIXO_NOMES_NUMERICOS = "_";
	private static final String ILLEGAL_CHARACTERS = "[:\\\\/*?|<>]";
	private static final String REPLACE_CHARACTER = "_";
	private static final String ERROR_NO_MAPA_SHAPEFILE = "_ERROR_NO_MAPA_SHAPEFILE_";
	private ServicioConfiguracionGeneral servicioConfiguracionGeneral;

	@Autowired
	public void setServicioConfiguracionGeneral(ServicioConfiguracionGeneral servicioConfiguracionGeneral) {
		this.servicioConfiguracionGeneral = servicioConfiguracionGeneral;
	}
	
	public ServicioConfiguracionGeneral getServicioConfiguracionGeneral() {
		return servicioConfiguracionGeneral;
	}
	
	@Autowired
	public void setGestorCUUsuario(GestorCUUsuario gestorCUUsuario) {
		this.gestorCUUsuario = gestorCUUsuario;
	}

	@Autowired
	public void setGestorCUAtributo(GestorCUAtributo gestorCUAtributo) {
		this.gestorCUAtributo = gestorCUAtributo;
	}

	@Autowired
	public void setGestorCUIndicadorExpresion(GestorCUIndicadorExpresion gestorCUIndicadorExpresion) {
		this.gestorCUIndicadorExpresion = gestorCUIndicadorExpresion;
	}

	@Autowired
	public void setGestorCUEstiloVisualizacionTabla(GestorCUEstiloVisualizacionTabla gestorCUEstiloVisualizacionTabla) {
		this.gestorCUEstiloVisualizacionTabla = gestorCUEstiloVisualizacionTabla;
	}

	@Autowired
	public void setGestorCUEstiloVisualizacionDiagramaBarras(
			GestorCUEstiloVisualizacionDiagramaBarras gestorCUEstiloVisualizacionDiagramaBarras) {
		this.gestorCUEstiloVisualizacionDiagramaBarras = gestorCUEstiloVisualizacionDiagramaBarras;
	}

	@Autowired
	public void setGestorCUEstiloVisualizacionDiagramaSectores(
			GestorCUEstiloVisualizacionDiagramaSectores gestorCUEstiloVisualizacionDiagramaSectores) {
		this.gestorCUEstiloVisualizacionDiagramaSectores = gestorCUEstiloVisualizacionDiagramaSectores;
	}
	
	@Autowired
	public void setGestorCUEstiloVisualizacionMapa(GestorCUEstiloVisualizacionMapa gestorCUEstiloVisualizacionMapa) {
		this.gestorCUEstiloVisualizacionMapa = gestorCUEstiloVisualizacionMapa;
	}
	
	@Autowired
	public void setGestorCURangosVisualizacionMapa(GestorCURangosVisualizacionMapa gestorCURangosVisualizacionMapa) {
		this.gestorCURangosVisualizacionMapa = gestorCURangosVisualizacionMapa;
	}

	@Autowired
	public void setGestorCUMetadatos(GestorCUMetadatos gestorCUMetadatos) {
		this.gestorCUMetadatos = gestorCUMetadatos;
	}

	@Autowired
	public void setGestorCUIndicador(GestorCUIndicador gestorCUIndicador) {
		this.gestorCUIndicador = gestorCUIndicador;
	}
	
	@Autowired
	public void setGestorCUHistorico(GestorCUHistorico gestorCUHistorico) {
		this.gestorCUHistorico = gestorCUHistorico;
	}
	
	@Autowired
	public void setGestorCURelacion(GestorCURelacion gestorCURelacion) {
		this.gestorCURelacion = gestorCURelacion;
	}
	
	@Autowired
	public void setGestorCUCriterio(GestorCUCriterio gestorCUCriterio) {
		this.gestorCUCriterio = gestorCUCriterio;
	}
	
	@Autowired
	public void setGestorCUIndicadorUsuarioModificacion(
			GestorCUIndicadorUsuarioModificacion gestorCUIndicadorUsuarioModificacion) {
		this.gestorCUIndicadorUsuarioModificacion = gestorCUIndicadorUsuarioModificacion;
	}
	
	@Autowired
	public void setGestorCUUsuarioPermiso(
			GestorCUUsuarioPermiso gestorCUUsuarioPermiso) {
		this.gestorCUUsuarioPermiso = gestorCUUsuarioPermiso;
	}
	
	@Autowired
	public void setGestorCUCapaBase(GestorCUCapaBase gestorCUCapaBase) {
		this.gestorCUCapaBase = gestorCUCapaBase;
	}




	private HttpHeaders responseHeaders = new HttpHeaders();
	{
		responseHeaders.add("Content-Type", "application/xml; charset=utf-8");
	}

	@RequestMapping(method = RequestMethod.GET, value = { "/cargaPorCategoria/{idCategoria}" })
	public ResponseEntity<EncapsuladorListSW<IndicadorDto>> cargaPorCategoria(@PathVariable("idCategoria") Long idCategoria) {
		return new ResponseEntity<EncapsuladorListSW<IndicadorDto>>(
				gestorCUIndicador.cargaIndicadoresPorCategoria(idCategoria), responseHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = { "/cargaPorCategoriaPorFuente/{idCategoria}/{idFuente}" })
	public ResponseEntity<EncapsuladorListSW<IndicadorDto>> cargaPorCategoriaPorFuente(@PathVariable("idCategoria") Long idCategoria, @PathVariable("idFuente") Long idFuente) {
		return new ResponseEntity<EncapsuladorListSW<IndicadorDto>>(gestorCUIndicador.cargaIndicadoresPorCategoriaPorFuente(idCategoria, idFuente), responseHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = { "/cargaPorCategoriaPendientesDePublicos/{idCategoria}" })
	public ResponseEntity<EncapsuladorListSW<IndicadorDto>> cargaPorCategoriaPendientesDePublicos(@PathVariable("idCategoria") Long idCategoria) {
		return new ResponseEntity<EncapsuladorListSW<IndicadorDto>>(gestorCUIndicador.cargaIndicadoresPorCategoriaPendientesDePublico(idCategoria), responseHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = { "/cargaPorCategoriaPendientesPublicacionWeb/{idCategoria}" })
	public ResponseEntity<EncapsuladorListSW<IndicadorDto>> cargaPorCategoriaPendientesPublicacionWeb(@PathVariable("idCategoria") Long idCategoria) {
		return new ResponseEntity<EncapsuladorListSW<IndicadorDto>>(gestorCUIndicador.cargaIndicadoresPorCategoriaPendientesPublicacionWeb(idCategoria), responseHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = { "/cargaPorCategoriaPublicos/{idCategoria}" })
	public ResponseEntity<EncapsuladorListSW<IndicadorDto>> cargaPorCategoriaPublicos(@PathVariable("idCategoria") Long idCategoria) {
		return new ResponseEntity<EncapsuladorListSW<IndicadorDto>>(
				gestorCUIndicador.cargaIndicadoresPorCategoriaPublicos(idCategoria), responseHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = { "/cargaPorCategoriaPublicados/{idCategoria}" })
	public ResponseEntity<EncapsuladorListSW<IndicadorDto>> cargaPorCategoriaPublicados(@PathVariable("idCategoria") Long idCategoria) {
		return new ResponseEntity<EncapsuladorListSW<IndicadorDto>>(gestorCUIndicador.cargaIndicadoresPorCategoriaPublicados(idCategoria), responseHeaders, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, value = { "/cargaSinCategoria" })
	public ResponseEntity<EncapsuladorListSW<IndicadorDto>> cargaSinCategoria() {
		return new ResponseEntity<EncapsuladorListSW<IndicadorDto>>(gestorCUIndicador.cargaIndicadoresSinCategoria(),responseHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = { "/cargaSinCategoriaPorFuente/{idFuente}" })
	public ResponseEntity<EncapsuladorListSW<IndicadorDto>> cargaSinCategoriaPorFuente(@PathVariable("idFuente") Long idFuente) {
		return new ResponseEntity<EncapsuladorListSW<IndicadorDto>>(gestorCUIndicador.cargaIndicadoresSinCategoriaPorFuente(idFuente),responseHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = { "/cargaSinCategoriaPendientesDePublicos" })
	public ResponseEntity<EncapsuladorListSW<IndicadorDto>> cargaSinCategoriaPendientesDePublicos() {
		return new ResponseEntity<EncapsuladorListSW<IndicadorDto>>(gestorCUIndicador.cargaIndicadoresSinCategoriaPendientesDePublico(),responseHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = { "/cargaSinCategoriaPendientesPublicacionWeb" })
	public ResponseEntity<EncapsuladorListSW<IndicadorDto>> cargaSinCategoriaPendientesPublicacionWeb() {
		return new ResponseEntity<EncapsuladorListSW<IndicadorDto>>(gestorCUIndicador.cargaIndicadoresSinCategoriaPendientesPublicacionWeb(),responseHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = { "/cargaSinCategoriaPublicos" })
	public ResponseEntity<EncapsuladorListSW<IndicadorDto>> cargaSinCategoriaPublicos() {
		return new ResponseEntity<EncapsuladorListSW<IndicadorDto>>(gestorCUIndicador.cargaIndicadoresSinCategoriaPublicos(),
				responseHeaders, HttpStatus.OK);
	}	
	
	@RequestMapping(method = RequestMethod.GET, value = { "/cargaSinCategoriaPublicados" })
	public ResponseEntity<EncapsuladorListSW<IndicadorDto>> cargaSinCategoriaPublicados() {
		return new ResponseEntity<EncapsuladorListSW<IndicadorDto>>(gestorCUIndicador.cargaIndicadoresSinCategoriaPublicados(),responseHeaders, HttpStatus.OK);
	}	

	@RequestMapping(method = RequestMethod.GET, value = { "/cargaPorId/{id}" })
	public ResponseEntity<IndicadorDto> cargaPorId(@PathVariable("id") Long id) {
		return new ResponseEntity<IndicadorDto>(gestorCUIndicador.cargaPorId(id), responseHeaders, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, value = { "/guardaIndicador/{idUsuario}" })
	public ResponseEntity<EncapsuladorPOSTSW<IndicadorDto>> guardaIndicador(@RequestBody IndicadorDto indicadorDto, @PathVariable("idUsuario")Long idUsuario) {
		EncapsuladorErroresSW errores = new EncapsuladorErroresSW();
		indicadorDto = gestorCUIndicador.guarda(indicadorDto, idUsuario, errores);
		return new ResponseEntity<EncapsuladorPOSTSW<IndicadorDto>>(new EncapsuladorPOSTSW<IndicadorDto>(indicadorDto,errores), responseHeaders, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, value = { "/cargaIndicadoresSinCategoriaPublicosOPorPermisoUsuario/{idUsuario}" })
	public ResponseEntity<EncapsuladorListSW<IndicadorDto>> cargaIndicadoresSinCategoriaPublicosOPorPermisoUsuario(@PathVariable("idUsuario") Long idUsuario) {
		return new ResponseEntity<EncapsuladorListSW<IndicadorDto>>(gestorCUIndicador.cargaIndicadoresSinCategoriaPublicosOPorPermisoUsuario(idUsuario), responseHeaders,HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, value = { "/cargaIndicadoresConCategoriaPublicosOPorPermisoUsuario/{idUsuario}/{idCategoria}" })
	public ResponseEntity<EncapsuladorListSW<IndicadorDto>> cargaIndicadoresConCategoriaPublicosOPorPermisoUsuario(
			@PathVariable("idCategoria") Long idCategoria, @PathVariable("idUsuario") Long idUsuario) {
		return new ResponseEntity<EncapsuladorListSW<IndicadorDto>>(
				gestorCUIndicador.cargaIndicadoresConCategoriaPublicosOPorPermisoUsuario(idCategoria, idUsuario),
				responseHeaders, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, value = { "/cargaIndicadoresConCategoriaNoPublicosPorPermisoUsuario/{idUsuario}/{idCategoria}" })
	public ResponseEntity<EncapsuladorListSW<IndicadorDto>> cargaIndicadoresConCategoriaNoPublicosPorPermisoUsuario(
			@PathVariable("idUsuario") Long idUsuario, @PathVariable("idCategoria") Long idCategoria) {
		return new ResponseEntity<EncapsuladorListSW<IndicadorDto>>(
				gestorCUIndicador.cargaIndicadoresConCategoriaNoPublicosPorPermisoUsuario(idUsuario, idCategoria),
				responseHeaders, HttpStatus.OK);

	}

	@RequestMapping(method = RequestMethod.GET, value = { "/cargaBusquedaDirecta/{idUsuario}/{criterio}" })
	public ResponseEntity<EncapsuladorMapSW<String, EncapsuladorMapSW<Long, EncapsuladorListSW<ElementoJerarquiaDto>>>> cargaBusquedaDirecta(@PathVariable("idUsuario") Long idUsuario, @PathVariable("criterio") String criterio) throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
		return new ResponseEntity<EncapsuladorMapSW<String, EncapsuladorMapSW<Long, EncapsuladorListSW<ElementoJerarquiaDto>>>>(gestorCUIndicador.cargaBusquedaDirecta(idUsuario, criterio, getServicioConfiguracionGeneral().getPathMetadatos()), responseHeaders, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/borraIndicador/{id}/{idUsuario}")
	public ResponseEntity<EncapsuladorBooleanSW> borrarIndicador(@PathVariable("id") Long id, @PathVariable("idUsuario")Long idUsuario, HttpServletRequest request) {
		EncapsuladorBooleanSW encapsulador = new EncapsuladorBooleanSW();
		try {
			EncapsuladorErroresSW errores = new EncapsuladorErroresSW();
			String realPath = getPathRealAplicacion(request, getServicioConfiguracionGeneral().getPathMetadatos());
			MetadatosDto metaDatos = gestorCUMetadatos.cargaPorIdIndicador(id);
			gestorCUMetadatos.borraPorIdIndicador(id, errores);
			gestorCUHistorico.borrarPorIdIndicador(id, errores);
			gestorCUCriterio.borraPorIdIndicador(id);
			gestorCUAtributo.borraPorIdIndicador(id);
			gestorCURelacion.borraPorIdIndicador(id);
			gestorCUIndicadorExpresion.borraPorIdIndicador(id);
			IndicadorUsuarioDto indicadorUsuario=new IndicadorUsuarioDto();
			indicadorUsuario.setIdIndicador(id);
			indicadorUsuario.setIdUsuario(idUsuario);
			indicadorUsuario.setAccion(TipoModificacionEnum.BORRADO.getTipoModificacion());
			gestorCUIndicadorUsuarioModificacion.guarda(indicadorUsuario, errores);
			gestorCUIndicador.borra(id, errores);
			gestorCUMetadatos.borrarFichero(metaDatos.getMetadatos(), id, realPath, errores);
			encapsulador.setValorLogico(true);
			ResponseEntity<EncapsuladorBooleanSW> respuesta = new ResponseEntity<EncapsuladorBooleanSW>(encapsulador,responseHeaders, HttpStatus.OK);
			return respuesta;
		} catch (Exception ex) {
			encapsulador.setValorLogico(false);
			return new ResponseEntity<EncapsuladorBooleanSW>(encapsulador, responseHeaders, HttpStatus.OK);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/permisosEdicionUsuarioIndicador/{id}/{idUsuario}")
	public ResponseEntity<EncapsuladorBooleanSW> esUsuarioPermisosEdicionIndicador(@PathVariable("id") Long id, @PathVariable("idUsuario") Long idUsuario, HttpServletRequest request){
		EncapsuladorBooleanSW encapsulador = new EncapsuladorBooleanSW();
		UsuarioDto usuarioDto = gestorCUUsuario.carga(idUsuario);
		if(gestorCUUsuarioPermiso.esUsuarioConPermisosSobreIndicador(id, usuarioDto)){
			encapsulador.setValorLogico(true);
		}else{
			encapsulador.setValorLogico(false);
		}
		return new ResponseEntity<EncapsuladorBooleanSW>(encapsulador, responseHeaders, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, value = { "/nuevoEstiloVisualizacionTabla/{idIndicador}/{idUsuario}" })
	public ResponseEntity<EncapsuladorPOSTSW<EstiloVisualizacionTablaDto>> nuevoEstiloVisualizacionTabla(@PathVariable("idIndicador") Long idIndicador, @PathVariable("idUsuario") Long idUsuario,@RequestBody EstiloVisualizacionTablaDto estiloDto) {
		EncapsuladorErroresSW errores = new EncapsuladorErroresSW();
		IndicadorDto indicadorDto = gestorCUIndicador.cargaPorId(idIndicador);
		UsuarioDto usuarioDto = gestorCUUsuario.carga(idUsuario);
		estiloDto.setIndicador(indicadorDto);
		estiloDto.setUsuario(usuarioDto);
		estiloDto = gestorCUEstiloVisualizacionTabla.guarda(estiloDto, errores);
		return new ResponseEntity<EncapsuladorPOSTSW<EstiloVisualizacionTablaDto>>(new EncapsuladorPOSTSW<EstiloVisualizacionTablaDto>(estiloDto, errores), responseHeaders, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, value = { "/nuevoEstiloVisualizacionDiagramaBarras/{idIndicador}/{idUsuario}" })
	public ResponseEntity<EncapsuladorPOSTSW<EstiloVisualizacionDiagramaBarrasDto>> nuevoEstiloVisualizacionDiagramaBarras(@PathVariable("idIndicador") Long idIndicador, @PathVariable("idUsuario") Long idUsuario, @RequestBody EstiloVisualizacionDiagramaBarrasDto estiloDto) {
		EncapsuladorErroresSW errores = new EncapsuladorErroresSW();
		IndicadorDto indicadorDto = gestorCUIndicador.cargaPorId(idIndicador);
		UsuarioDto usuarioDto = gestorCUUsuario.carga(idUsuario);
		estiloDto.setIndicador(indicadorDto);
		estiloDto.setUsuario(usuarioDto);
		estiloDto = gestorCUEstiloVisualizacionDiagramaBarras.guarda(estiloDto, errores);
		return new ResponseEntity<EncapsuladorPOSTSW<EstiloVisualizacionDiagramaBarrasDto>>(new EncapsuladorPOSTSW<EstiloVisualizacionDiagramaBarrasDto>(estiloDto, errores), responseHeaders,
				HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, value = { "/nuevoEstiloVisualizacionDiagramaSectores/{idIndicador}/{idUsuario}" })
	public ResponseEntity<EncapsuladorPOSTSW<EstiloVisualizacionDiagramaSectoresDto>> nuevoEstiloVisualizacionDiagramaSectores(@PathVariable("idIndicador") Long idIndicador, @PathVariable("idUsuario") Long idUsuario,@RequestBody EstiloVisualizacionDiagramaSectoresDto estiloDto) {
		EncapsuladorErroresSW errores = new EncapsuladorErroresSW();
		IndicadorDto indicadorDto = gestorCUIndicador.cargaPorId(idIndicador);
		UsuarioDto usuarioDto = gestorCUUsuario.carga(idUsuario);
		estiloDto.setIndicador(indicadorDto);
		estiloDto.setUsuario(usuarioDto);
		estiloDto = gestorCUEstiloVisualizacionDiagramaSectores.guarda(estiloDto, errores);
		return new ResponseEntity<EncapsuladorPOSTSW<EstiloVisualizacionDiagramaSectoresDto>>(new EncapsuladorPOSTSW<EstiloVisualizacionDiagramaSectoresDto>(estiloDto, errores), responseHeaders,HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = { "/nuevoEstiloVisualizacionMapa/{idIndicador}/{idUsuario}" })
	public ResponseEntity<EncapsuladorPOSTSW<EstiloVisualizacionMapaDto>> nuevoEstiloVisualizacionMapa(@PathVariable("idIndicador") Long idIndicador, @PathVariable("idUsuario") Long idUsuario,@RequestBody EstiloVisualizacionMapaDto estiloDto) {
		EncapsuladorErroresSW errores = new EncapsuladorErroresSW();
		IndicadorDto indicadorDto = gestorCUIndicador.cargaPorId(idIndicador);
		UsuarioDto usuarioDto = gestorCUUsuario.carga(idUsuario);
		estiloDto.setIndicador(indicadorDto);
		estiloDto.setUsuario(usuarioDto);
		
		estiloDto = gestorCUEstiloVisualizacionMapa.guarda(estiloDto, errores);		
			
		return new ResponseEntity<EncapsuladorPOSTSW<EstiloVisualizacionMapaDto>>(new EncapsuladorPOSTSW<EstiloVisualizacionMapaDto>(estiloDto, errores), responseHeaders,HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = { "/insertarRangosVisualizacionMapa/{idEstiloMapa}" })
	public ResponseEntity<EncapsuladorPOSTSW<RangosVisualizacionMapaDto>> insertarRangosVisualizacionMapa(@PathVariable("idEstiloMapa") Long idEstiloMapa,@RequestBody RangosVisualizacionMapaDto rangoDto) {
		EncapsuladorErroresSW errores = new EncapsuladorErroresSW();
		
		EstiloVisualizacionMapaDto estiloMapa = new EstiloVisualizacionMapaDto();
		estiloMapa.setId(idEstiloMapa);
		rangoDto.setEstiloMapa(estiloMapa);
				
		rangoDto = gestorCURangosVisualizacionMapa.guarda(rangoDto, errores);		
			
		return new ResponseEntity<EncapsuladorPOSTSW<RangosVisualizacionMapaDto>>(new EncapsuladorPOSTSW<RangosVisualizacionMapaDto>(rangoDto, errores), responseHeaders,HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, value = { "/cargaEstiloVisualizacionTabla/{idIndicador}/{idUsuario}" })
	public ResponseEntity<EstiloVisualizacionTablaDto> cargarEstiloVisualizacionTabla(@PathVariable("idIndicador") Long idIndicador, @PathVariable("idUsuario") Long idUsuario) {
		return new ResponseEntity<EstiloVisualizacionTablaDto>(gestorCUEstiloVisualizacionTabla.cargaPorIdUsuarioEIdIndicador(idUsuario, idIndicador),responseHeaders, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, value = { "/cargaEstiloVisualizacionDiagramaBarras/{idIndicador}/{idUsuario}" })
	public ResponseEntity<EstiloVisualizacionDiagramaBarrasDto> cargarEstiloVisualizacionDiagramaBarras(@PathVariable("idIndicador") Long idIndicador, @PathVariable("idUsuario") Long idUsuario) {
		return new ResponseEntity<EstiloVisualizacionDiagramaBarrasDto>(gestorCUEstiloVisualizacionDiagramaBarras.cargaPorIdUsuarioEIdIndicador(idUsuario, idIndicador),responseHeaders, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, value = { "/cargaEstiloVisualizacionDiagramaSectores/{idIndicador}/{idUsuario}" })
	public ResponseEntity<EstiloVisualizacionDiagramaSectoresDto> cargarEstiloVisualizacionDiagramaSectores(@PathVariable("idIndicador") Long idIndicador, @PathVariable("idUsuario") Long idUsuario) {
		return new ResponseEntity<EstiloVisualizacionDiagramaSectoresDto>(gestorCUEstiloVisualizacionDiagramaSectores.cargaPorIdUsuarioEIdIndicador(idUsuario, idIndicador),responseHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = { "/cargaEstiloVisualizacionMapa/{idIndicador}/{idUsuario}" })
	public ResponseEntity<EstiloVisualizacionMapaDto> cargarEstiloVisualizacionMapa(@PathVariable("idIndicador") Long idIndicador, @PathVariable("idUsuario") Long idUsuario) {
		return new ResponseEntity<EstiloVisualizacionMapaDto>(gestorCUEstiloVisualizacionMapa.cargaPorIdUsuarioEIdIndicador(idUsuario, idIndicador),responseHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = { "/cargaRangosVisualizacionMapa/{idEstiloMapa}" })
	public ResponseEntity<EncapsuladorListSW<RangosVisualizacionMapaDto>> cargarRangosVisualizacionMapa(@PathVariable("idEstiloMapa") Long idEstiloMapa) {
		return new ResponseEntity<EncapsuladorListSW<RangosVisualizacionMapaDto>>(gestorCURangosVisualizacionMapa.cargaPorIdEstiloMapa(idEstiloMapa),responseHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = { "/cargaCapasBaseMapa" })
	public ResponseEntity<EncapsuladorListSW<CapaBaseDto>> cargarCapasBaseMapa() {
		EncapsuladorListSW<CapaBaseDto> lista= gestorCUCapaBase.cargaTodas();
		return new ResponseEntity<EncapsuladorListSW<CapaBaseDto>>(lista,responseHeaders, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/exportarCSV/{id}")
	public ResponseEntity<EncapsuladorFileSW> exportarIndicadorCSV(@PathVariable("id") Long id, HttpServletRequest request) {
		String caracterSeparador = getServicioConfiguracionGeneral().getCaracterSeparadorCSV();
		EncapsuladorListSW<AtributoDto> listaColumnas = obterColumnas(id, request);
		String nomeIndicador=null;
		if(listaColumnas!=null){
			//Eliminamos campo mapa
			for(int i=0; i<listaColumnas.size();i++){
				if(listaColumnas.get(i)!=null&& listaColumnas.get(i).getEsMapa()){
					 listaColumnas.remove(i);
					 break;
				}
			}
			for(int i=0; i<listaColumnas.size();i++){
				if(listaColumnas.get(i)!=null&& listaColumnas.get(i).getColumna()!=null){
					 nomeIndicador= listaColumnas.get(i).getColumna().getTabla().getIndicador().getNombre();
					 break;
				}
			}
		}
		
		AtributosMapDto datos = gestorCUAtributo.cargarDatosIndicador(listaColumnas, caracterSeparador, "no", getServicioConfiguracionGeneral().getMapaPathTiposFuente());
		
		EncapsuladorFileSW ficheroCSV = new EncapsuladorFileSW();
		File archivoTmp = null;

		try {
			final LinkedHashMap<String, ValorFDDto> mapaDatos = datos.getContenido();
			
			if (mapaDatos != null && mapaDatos.keySet().size() > 0) {
				List<String[]> listaInvertida = new ArrayList<String[]>();
				List<String> valoresFilaCSV = null;

				listaInvertida.add(mapaDatos.keySet().toArray(new String[mapaDatos.keySet().size()]));

				int numeroDeElementosDaListaMaisGrande = obterIndiceMaximoDoMapa(mapaDatos);
				for (int i = 0; i < numeroDeElementosDaListaMaisGrande; i++) {
					valoresFilaCSV = new ArrayList<String>();
					for (int j = 0; j < mapaDatos.keySet().size(); j++) {
						if (i < mapaDatos.get((String)mapaDatos.keySet().toArray()[j]).getValores().size())
							valoresFilaCSV.add(mapaDatos.get((String)mapaDatos.keySet().toArray()[j]).getValores().get(i).getTexto());
						else 
							valoresFilaCSV.add("");
					}

					listaInvertida.add(valoresFilaCSV.toArray(new String[valoresFilaCSV.size()]));
				}
				FileInputStream inputStream = null;
				Writer writer = null;
				CSVWriter writerCSV = null;
				byte[] byteArrayCSV = null;

				try {
					archivoTmp = File.createTempFile("CSV_", ".csv");
					writer = new OutputStreamWriter(new FileOutputStream(archivoTmp), DEFAULT_ENCODE);

					writerCSV = new CSVWriter(writer, caracterSeparador.charAt(0));
					writerCSV.writeAll(listaInvertida);

					inputStream = new FileInputStream(archivoTmp);
				} catch (Exception e) {
					throw e;
				} finally {
					pecharRecursos(writer, writerCSV);
				}
				byteArrayCSV = IOUtils.toByteArray(inputStream);

				if (byteArrayCSV != null) {
					ficheroCSV.setFich(byteArrayCSV);
					ficheroCSV.setNombre(escaparNombreArchivo(nomeIndicador) + ".csv");
				}

				pecharRecursos(inputStream);
			}
		} catch (Exception e) {
			ResponseEntity<EncapsuladorFileSW> respuesta = new ResponseEntity<EncapsuladorFileSW>(new EncapsuladorFileSW(), HttpStatus.OK);
			return respuesta;
		} finally {
			borrarTmpSeguro(archivoTmp);
		}

		ResponseEntity<EncapsuladorFileSW> respuesta = new ResponseEntity<EncapsuladorFileSW>(ficheroCSV, HttpStatus.OK);
		return respuesta;
	}
	
	public static String quitarAcentos(String input) {
	    // Cadena de caracteres original a sustituir.
	    String []original = {"á","é","í","ó","ú","Á","É","Í","Ó","Ú","ñ","Ñ"};
	    // Cadena de caracteres ASCII que reemplazarán los originales.
	    String [] ascii = {"a","e;","i","o","u","A","E","I","O","U","n","N"};
	    String output = input;
	    for (int i=0; i<original.length; i++) {
	        // Reemplazamos los caracteres especiales.
	       output = output.replace(original[i], ascii[i]);
	    }//for i
	    return output;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/exportarBDEspacial/{id}")
	public ResponseEntity<EncapsuladorFileSW> exportarIndicadorBDEspacial(@PathVariable("id") Long id, HttpServletRequest request) {
		String caracterSeparador = getServicioConfiguracionGeneral().getCaracterSeparadorCSV();
		EncapsuladorListSW<AtributoDto> listaColumnas = obterColumnas(id, request);
		String nomeIndicador=null;
		if(listaColumnas!=null){
			for(int i=0; i<listaColumnas.size();i++){
				if(listaColumnas.get(i)!=null&& listaColumnas.get(i).getColumna()!=null){
					 nomeIndicador= listaColumnas.get(i).getColumna().getTabla().getIndicador().getNombre();
					 break;
				}
			}
		}
		if(nomeIndicador!=null){
			nomeIndicador=quitarAcentos(nomeIndicador);
		}
		AtributosMapDto datos = gestorCUAtributo.cargarDatosIndicador(listaColumnas, caracterSeparador, "no", getServicioConfiguracionGeneral().getMapaPathTiposFuente());

		EncapsuladorFileSW ficheroBD = new EncapsuladorFileSW();
		File archivoTmp = null;

		try {
			final LinkedHashMap<String, ValorFDDto> mapaDatos = datos.getContenido();

			if (mapaDatos != null && mapaDatos.keySet().size() > 0) {
				final HashMap<String, TipoAtributoFD> tiposDeAtributo = obterMapaDeTiposDeColumna(listaColumnas);
						
				FileInputStream inputStream = null;
				OutputStreamWriter writer = null;
				byte[] byteArraySQL = null;
				// Construccion del SQL
				try {
					String newLine = System.getProperty("line.separator");
					String cabeceiraSQL = crearCabeceiraSql(nomeIndicador, tiposDeAtributo, newLine);

					if (cabeceiraSQL != null && cabeceiraSQL.length() > 0) {
						archivoTmp = File.createTempFile("SQL_", ".sql");
						writer = new OutputStreamWriter(new FileOutputStream(archivoTmp), DEFAULT_ENCODE);

						//Cabeceira SQL
						writer.append(cabeceiraSQL);
						//Inserts
						int numeroDeElementosDaListaMaisGrande = obterIndiceMaximoDoMapa(mapaDatos);
						for (int i = 0; i < numeroDeElementosDaListaMaisGrande; i++) {
							List<String> valores = new ArrayList<String>();
							for (String key : mapaDatos.keySet()) {
								if (i < mapaDatos.get(key).getValores().size())
									valores.add(formatearValor(mapaDatos.get(key).getValores().get(i), 
											tiposDeAtributo.get(key)));
								else 
									valores.add("null");
							}
							writer.append(crearInsert(mapaDatos.keySet(),nomeIndicador, valores, newLine));
						}
						writer.append("COMMIT;");
						
						inputStream = new FileInputStream(archivoTmp);
					}
				} catch (Exception e) {
					throw e;
				} finally {
					pecharRecursos(writer);
				}
				byteArraySQL = IOUtils.toByteArray(inputStream);	
				
				if (byteArraySQL != null) {
					ficheroBD.setFich(byteArraySQL);
					ficheroBD.setNombre(escaparNombreArchivo(nomeIndicador) + ".sql");
				}
				pecharRecursos(inputStream);
			}
		} catch (Exception e) {
			ResponseEntity<EncapsuladorFileSW> respuesta = new ResponseEntity<EncapsuladorFileSW>(new EncapsuladorFileSW(), HttpStatus.OK);
			return respuesta;
		} finally {
			borrarTmpSeguro(archivoTmp);
		}
		
		ResponseEntity<EncapsuladorFileSW> respuesta = new ResponseEntity<EncapsuladorFileSW>(ficheroBD, HttpStatus.OK);
		return respuesta;
	}

	@SuppressWarnings("deprecation")
	@RequestMapping(method = RequestMethod.GET, value = "/exportarGML/{id}")
	public ResponseEntity<EncapsuladorFileSW> exportarIndicadorGML(@PathVariable("id") Long id,HttpServletRequest request) {
		String caracterSeparador = getServicioConfiguracionGeneral().getCaracterSeparadorCSV();
		EncapsuladorListSW<AtributoDto> listaColumnas = obterColumnas(id, request);
		String nomeIndicador=null;
		if(listaColumnas!=null){
			for(int i=0; i<listaColumnas.size();i++){
				if(listaColumnas.get(i)!=null&& listaColumnas.get(i).getColumna()!=null){
					 nomeIndicador= listaColumnas.get(i).getColumna().getTabla().getIndicador().getNombre();
					 break;
				}
			}
		}
		AtributosMapDto datos = gestorCUAtributo.cargarDatosIndicador(listaColumnas, caracterSeparador, "no", getServicioConfiguracionGeneral().getMapaPathTiposFuente());
		
		
		
		EncapsuladorFileSW ficheroGML = new EncapsuladorFileSW();
		File archivoTmp = null;
		
		try {
			final LinkedHashMap<String, ValorFDDto> mapaDatos = datos.getContenido();
			
			if (mapaDatos != null && mapaDatos.keySet().size() > 0) {
				final LinkedHashMap<String, TipoAtributoFD> tiposDeAtributo = obterMapaDeTiposDeColumna(listaColumnas);
				final String schemaGML = crearSchemaGML(tiposDeAtributo);
				final SimpleFeatureType tipo = DataUtilities.createType("a21l", schemaGML);
				WKTReader2 wkt = new WKTReader2();

				//Se crea la coleccion de features a partir de los datos
				SimpleFeatureCollection featureCollection = FeatureCollections.newCollection();
				List<Object> lineaValores;
				for (int i = 0; i < obterIndiceMaximoDoMapa(mapaDatos); i++) {
					lineaValores= new ArrayList<Object>();
					for (String key : mapaDatos.keySet()) {
						if (i < mapaDatos.get(key).getValores().size() && mapaDatos.get(key).getValores().get(i).getTexto()!=null && !("").equals(mapaDatos.get(key).getValores().get(i).getTexto().trim())) {
							if ( tiposDeAtributo.get(key)==TipoAtributoFD.VALORFDGEOGRAFICO ){
								lineaValores.add(wkt.read(mapaDatos.get(key).getValores().get(i).getTexto().trim()));
							}else{
								lineaValores.add(mapaDatos.get(key).getValores().get(i).getTexto().trim());
							}
						} else {
							lineaValores.add(null);
						}
					}
					featureCollection.add(SimpleFeatureBuilder.build(tipo, lineaValores.toArray(), generarIdFeature(i)));
				}
				
				archivoTmp = File.createTempFile("Location", ".xsd");

				URL locationURL = archivoTmp.toURI().toURL();
				URL baseURL = archivoTmp.getParentFile().toURI().toURL();

				FileOutputStream xsd = null;
				ByteArrayOutputStream out = null;

				try {
					xsd = new FileOutputStream(archivoTmp);
					out = new ByteArrayOutputStream();

					GML gml = new GML(Version.GML2);
					gml.setEncoding(Charset.forName(DEFAULT_ENCODE));
					gml.setBaseURL(baseURL);
					gml.setNamespace("a21l", locationURL.toExternalForm());
					gml.encode(xsd, tipo);

					GML gml2 = new GML(Version.WFS1_0);
					gml2.setEncoding(Charset.forName(DEFAULT_ENCODE));
					gml2.setBaseURL(baseURL);
					gml2.setNamespace("geotools", "http://geotools.org");
					gml2.encode(out, featureCollection);
				} catch (Exception e) {
					throw e;
				} finally { 
					pecharRecursos(xsd);
				}

				byte[] byteArrayGML = null;
				byteArrayGML = out.toByteArray();	

				if (byteArrayGML != null) {
					ficheroGML.setFich(byteArrayGML);
					ficheroGML.setNombre(escaparNombreArchivo(nomeIndicador) + ".gml");
				}

				pecharRecursos(out);
			}
		} catch (Exception e) {
			ResponseEntity<EncapsuladorFileSW> respuesta = new ResponseEntity<EncapsuladorFileSW>(new EncapsuladorFileSW(), HttpStatus.OK);
			return respuesta;
		} finally {
			borrarTmpSeguro(archivoTmp);
		}
		
		ResponseEntity<EncapsuladorFileSW> respuesta = new ResponseEntity<EncapsuladorFileSW>(ficheroGML, HttpStatus.OK);
		return respuesta;
	}
	
	@SuppressWarnings("deprecation")
	@RequestMapping(method = RequestMethod.GET, value = "/exportarShapefile/{id}")
	public ResponseEntity<EncapsuladorFileSW> exportarIndicadorShapeFile(@PathVariable("id") Long id,HttpServletRequest request) {
		String caracterSeparador = getServicioConfiguracionGeneral().getCaracterSeparadorCSV();
		EncapsuladorListSW<AtributoDto> listaColumnas = obterColumnas(id, request);
		String nomeIndicador=null;
		if(listaColumnas!=null){
			for(int i=0; i<listaColumnas.size();i++){
				if(listaColumnas.get(i)!=null&& listaColumnas.get(i).getColumna()!=null){
					 nomeIndicador= listaColumnas.get(i).getColumna().getTabla().getIndicador().getNombre();
					 break;
				}
			}
		}
		AtributosMapDto datos = gestorCUAtributo.cargarDatosIndicador(listaColumnas, caracterSeparador, "no", getServicioConfiguracionGeneral().getMapaPathTiposFuente());
		
		EncapsuladorFileSW ficheroSHP = new EncapsuladorFileSW();
		
		File archivoShpTemp = null;
		File archivoShxTemp = null;
		File archivoDbfTemp = null;
		
		//si no hay columna mapa no exporto a Shp y muestro un aviso
		boolean tieneMapa = false;
		for ( AtributoDto attr : listaColumnas ) {
			if ( attr.getEsMapa() ) {
				tieneMapa = true;
				break;
			}	
		}
		
		if ( !tieneMapa ) {
			EncapsuladorFileSW fich = new EncapsuladorFileSW();
			fich.setNombre(ERROR_NO_MAPA_SHAPEFILE);
			return new ResponseEntity<EncapsuladorFileSW>(fich, HttpStatus.OK);
		}
		
		try {
			final LinkedHashMap<String, ValorFDDto> mapaDatos = datos.getContenido();
			if (mapaDatos != null && mapaDatos.keySet().size() > 0) {
				final LinkedHashMap<String, TipoAtributoFD> tiposDeAtributo = obterMapaDeTiposDeColumna(listaColumnas);
				final String schemaGML = crearSchemaGMLOrdenado(tiposDeAtributo);
				SimpleFeatureType tipo = DataUtilities.createType("a21l", schemaGML);
				WKTReader2 wkt = new WKTReader2();

				//Se crea la coleccion de features a partir de los datos
				SimpleFeatureCollection featureCollection = FeatureCollections.newCollection();
				List<Object> lineaValores;
				for (int i = 0; i < obterIndiceMaximoDoMapa(mapaDatos); i++) {
					lineaValores= new ArrayList<Object>();
					for (String key : mapaDatos.keySet()) {
						if (i < mapaDatos.get(key).getValores().size() && mapaDatos.get(key).getValores().get(i).getTexto()!=null && !("").equals(mapaDatos.get(key).getValores().get(i).getTexto().trim())) {
							if ( tiposDeAtributo.get(key)==TipoAtributoFD.VALORFDGEOGRAFICO)	
								lineaValores.add(wkt.read(mapaDatos.get(key).getValores().get(i).getTexto()));
							else
								lineaValores.add(mapaDatos.get(key).getValores().get(i).getTexto());
						} else 
							lineaValores.add(null);
					}
					// A xeometria ten que estar na primeira posicion para crear o shape
					Collections.sort(lineaValores, new Comparator<Object>(){
						public int compare(Object o1, Object o2) {
							if (o1 instanceof Geometry) return -1;
							if (o2 instanceof Geometry) return 1;
							return 0;
						}
					});

					featureCollection.add(SimpleFeatureBuilder.build(tipo, lineaValores.toArray(), i + ""));
				}
				nomeIndicador = StringEscapeUtils.escapeJava(nomeIndicador);
				nomeIndicador = StringEscapeUtils.escapeSql(nomeIndicador).replace(" ", "_");
				archivoShpTemp = File.createTempFile(nomeIndicador, ".shp");

				ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();
				Map<String, Serializable> params = new HashMap<String, Serializable>();
				params.put("url", archivoShpTemp.toURI().toURL());
				params.put("create spatial index", Boolean.FALSE);

				ShapefileDataStore dataStore = (ShapefileDataStore) dataStoreFactory.createNewDataStore(params);
				dataStore.setStringCharset(Charset.forName(DEFAULT_ENCODE));
				dataStore.createSchema(tipo);

				String typeName = dataStore.getTypeNames()[0];
				SimpleFeatureStore featureStore = (SimpleFeatureStore) dataStore.getFeatureSource(typeName);

				Transaction transaction = new DefaultTransaction("create");
				featureStore.setTransaction(transaction);

				try {
					featureStore.addFeatures(featureCollection);
					transaction.commit();
				} catch (Exception e) {
					transaction.rollback();
					throw e;
				} finally {
					transaction.close();
				}
				archivoShxTemp = new File(archivoShpTemp.getAbsolutePath().replace(".shp", ".shx"));
				archivoDbfTemp = new File(archivoShpTemp.getAbsolutePath().replace(".shp", ".dbf"));
				
				byte[] byteArraySHPzip = super.compressMultiple(archivoShpTemp, archivoShxTemp, archivoDbfTemp);	

				if (byteArraySHPzip != null) {
					ficheroSHP.setFich(byteArraySHPzip);
					ficheroSHP.setNombre(escaparNombreArchivo(nomeIndicador) + ".zip");
				}
			}
		} catch (Exception e) {
			ResponseEntity<EncapsuladorFileSW> respuesta = new ResponseEntity<EncapsuladorFileSW>(new EncapsuladorFileSW(), HttpStatus.OK);
			return respuesta;
		} finally {
			borrarTmpSeguro(archivoShpTemp);
		}

		ResponseEntity<EncapsuladorFileSW> respuesta = new ResponseEntity<EncapsuladorFileSW>(ficheroSHP, HttpStatus.OK);
		return respuesta;
	}

	private String escaparNombreArchivo(String nombreArchivo) {
		try {
			nombreArchivo = URLEncoder.encode(nombreArchivo, "UTF-8").replace(".", "").replace("/", "").replace(":", "").replace("%", "");
			File f = File.createTempFile(nombreArchivo, ".tmp");
			f.getCanonicalPath();
		} catch (Exception e) {
			nombreArchivo = "File";
		}

		return nombreArchivo;
	}

	private String generarIdFeature(int idNumerico) {
		return new Formatter().format("%09d",idNumerico).toString();
	}
	
	/**
	 * Crea un esquema GML correspondente aos tipos de atributo pasados
	 * @param tiposDeAtributo Tipos de atributo
	 * @return Esquema GML correspondente
	 * @throws SchemaException
	 */
	private String crearSchemaGML(HashMap<String, TipoAtributoFD> tiposDeAtributo) throws SchemaException {
		List<String> defAtr = new ArrayList<String>();
		String tipoAtributo = "";
		
		for (String key : tiposDeAtributo.keySet()) {
			switch (tiposDeAtributo.get(key)) {
				case VALORFDNUMERICO:
					tipoAtributo = Double.class.getSimpleName();
					break;
				case VALORFDGEOGRAFICO:
					tipoAtributo = MultiPolygon.class.getSimpleName();
					break;
				case VALORFDFECHA:
					tipoAtributo = Date.class.getSimpleName();
					break;
				default:
					tipoAtributo = String.class.getSimpleName();
					break;
			}
			
			// Parseamos o nome da columna porque nos GML non se permiten columnas que comecen por caracter numerico
			defAtr.add(parsearNomeNumerico(key)+ ":" + tipoAtributo); 
		}
		
		return StringUtils.join(defAtr, ",");
	}
	
	/**
	 * Crea un esquema GML correspondente aos tipos de atributo pasados pero colocando a xeometria na primeira posicion
	 * @param tiposDeAtributo Tipos de atributo
	 * @return Esquema GML correspondente
	 * @throws SchemaException
	 */
	private String crearSchemaGMLOrdenado(HashMap<String, TipoAtributoFD> tiposDeAtributo) throws SchemaException {
		List<String> defAtr = new ArrayList<String>();
		List<String> defAtrOrdenado = new ArrayList<String>();
		
		for (String key : tiposDeAtributo.keySet()) {
			switch (tiposDeAtributo.get(key)) {
				case VALORFDNUMERICO:
					defAtr.add(key + ":" + Double.class.getSimpleName()); 
					break;
				case VALORFDGEOGRAFICO:
					//A xeometria na posicion 0 da lista ordeada
					defAtrOrdenado.add(key + ":" + MultiPolygon.class.getSimpleName());
					break;
				case VALORFDFECHA:
					defAtr.add(key + ":" + Date.class.getSimpleName()); 
					break;
				default:
					defAtr.add(key + ":" + String.class.getSimpleName()); 
					break;
			}
		}
		
		defAtrOrdenado.addAll(defAtr);
		
		return StringUtils.join(defAtrOrdenado, ",");
	}
	
	/**
	 * Obten as columnas do indicador
	 * @param id Id Indicador
	 * @param request Http Server Request
	 * @return Columnas do indicador
	 */
	private EncapsuladorListSW<AtributoDto> obterColumnas(Long id, HttpServletRequest request) {
		EncapsuladorListSW<AtributoDto> listaColumnas = gestorCUAtributo.cargaPorIndicador(id);
		EncapsuladorListSW<AtributoDto> listaColumnas2 = new EncapsuladorListSW<AtributoDto>();
		
		for (AtributoDto attr : listaColumnas) {
			AtributoFuenteDatosDto columna = attr.getColumna();
			TiposFuente tipo = null;
			if (columna==null || columna.getTabla()==null)
				tipo = TiposFuente.ODBC;
			else {
				TablaFuenteDatosDto tabla = columna.getTabla();
				tipo = tabla.getFuente().getTipo();				
			}
			String path;
			if (tipo.equals(TiposFuente.CSV))
				path = getServicioConfiguracionGeneral().getPathCsv();
			else if (tipo.equals(TiposFuente.GML))
				path = getServicioConfiguracionGeneral().getPathGml();
			else if (tipo.equals(TiposFuente.SHAPEFILE))
				path = getServicioConfiguracionGeneral().getPathShapefile();
			else
				path = null;
			
			String realPath = getPathRealAplicacion(request, path);
			AtributoDto attrCopia = new AtributoDto();
			attrCopia.setColumna(attr.getColumna());
			attrCopia.setIndicadorExpresion(attr.getIndicadorExpresion());
			attrCopia.setRelacion(attr.getRelacion());
			attrCopia.setEsAmbito(attr.getEsAmbito());
			attrCopia.setEsMapa(attr.getEsMapa());
			attrCopia.setId(attr.getId());
			attrCopia.setMostrar(attr.getMostrar());
			attrCopia.setNombre(attr.getNombre());
			attrCopia.setOrdenVisualizacion(attr.getOrdenVisualizacion());
			attrCopia.setPath(realPath);
			listaColumnas2.add(attrCopia);
		}
		return listaColumnas2;
	}

	/**Devuelve todos los datos asociados a un indicador para su visualización en los distintos formatos gráficos */
	@RequestMapping(method = RequestMethod.GET, value = "/obtenerDatos/{id}")
	public ResponseEntity<AtributosMapDto> obtenerDatosIndicador(@PathVariable("id") Long id,HttpServletRequest request) {		
		String caracterSeparador = getServicioConfiguracionGeneral().getCaracterSeparadorCSV();
		EncapsuladorListSW<AtributoDto> listaColumnas = gestorCUAtributo.cargaPorIndicador(id);
		
		EncapsuladorListSW<AtributoDto> listaColumnas2 = new EncapsuladorListSW<AtributoDto>(); 
		for ( AtributoDto attr : listaColumnas ) {
			AtributoFuenteDatosDto columna = attr.getColumna();
			String path;
			if (columna != null) {
				TablaFuenteDatosDto tabla = columna.getTabla();
				TiposFuente tipo = tabla.getFuente().getTipo();

				if (tipo.equals(TiposFuente.CSV))
					path = getServicioConfiguracionGeneral().getPathCsv();
				else if (tipo.equals(TiposFuente.GML))
					path = getServicioConfiguracionGeneral().getPathGml();
				else if (tipo.equals(TiposFuente.SHAPEFILE))
					path = getServicioConfiguracionGeneral().getPathShapefile();
				else
					path = null;
			} else{
				path = null;
			}
			
			String realPath = getPathRealAplicacion(request, path);
			
			AtributoDto attrCopia = new AtributoDto();
			attrCopia.setColumna(attr.getColumna());
			attrCopia.setIndicadorExpresion(attr.getIndicadorExpresion());
			attrCopia.setEsAmbito(attr.getEsAmbito());
			attrCopia.setEsMapa(attr.getEsMapa());
			attrCopia.setId(attr.getId());
			attrCopia.setMostrar(attr.getMostrar());
			attrCopia.setNombre(attr.getNombre());
			attrCopia.setOrdenVisualizacion(attr.getOrdenVisualizacion());
			attrCopia.setPath(realPath);
			attrCopia.setRelacion(attr.getRelacion());
			listaColumnas2.add(attrCopia);
		}
		
		//ResponseEntity<AtributosMapDto> resp = new ResponseEntity<AtributosMapDto>(gestorCUAtributo.cargarDatosIndicadorTodasColumnas(listaColumnas2, caracterSeparador, "no",getServicioConfiguracionGeneral().getMapaPathTiposFuente()), responseHeaders, HttpStatus.OK);
		ResponseEntity<AtributosMapDto> resp = new ResponseEntity<AtributosMapDto>(gestorCUAtributo.cargarDatosIndicador(listaColumnas2, caracterSeparador, "no", getServicioConfiguracionGeneral().getMapaPathTiposFuente()), responseHeaders, HttpStatus.OK);
		return resp;
	}
	
	/**Devuelve todos los datos tipo mapa asociados a un indicador para su visualización en formato geográfico */
	@RequestMapping(method = RequestMethod.GET, value = "/obtenerDatosMapa/{id}")
	public ResponseEntity<AtributosMapDto> obtenerDatosMapaIndicador(@PathVariable("id") Long id,HttpServletRequest request) {		
		String caracterSeparador = getServicioConfiguracionGeneral().getCaracterSeparadorCSV();
		EncapsuladorListSW<AtributoDto> listaColumnas = gestorCUAtributo.cargaPorIndicador(id);
		
		EncapsuladorListSW<AtributoDto> listaMapa = new EncapsuladorListSW<AtributoDto>(); 
		for ( AtributoDto attr : listaColumnas ) {
			if (attr.getEsMapa()){
				AtributoFuenteDatosDto columna = attr.getColumna();
	    		TablaFuenteDatosDto tabla = columna.getTabla();
				TiposFuente tipo = tabla.getFuente().getTipo();
				String path;
				if ( tipo.equals(TiposFuente.CSV))
					path = getServicioConfiguracionGeneral().getPathCsv();
				else if ( tipo.equals(TiposFuente.GML))
					path = getServicioConfiguracionGeneral().getPathGml();
				else if ( tipo.equals(TiposFuente.SHAPEFILE))
					path = getServicioConfiguracionGeneral().getPathShapefile();
				else
					path = null;
				
				String realPath = getPathRealAplicacion(request, path);
				
				AtributoDto attrCopia = new AtributoDto();
				attrCopia.setColumna(attr.getColumna());
				attrCopia.setIndicadorExpresion(attr.getIndicadorExpresion());
				attrCopia.setEsAmbito(attr.getEsAmbito());
				attrCopia.setEsMapa(attr.getEsMapa());
				attrCopia.setId(attr.getId());
				attrCopia.setMostrar(attr.getMostrar());
				attrCopia.setNombre(attr.getNombre());
				attrCopia.setOrdenVisualizacion(attr.getOrdenVisualizacion());
				attrCopia.setPath(realPath);
				listaMapa.add(attrCopia);
			}
		}
		
		//ResponseEntity<AtributosMapDto> resp = new ResponseEntity<AtributosMapDto>(gestorCUAtributo.cargarDatosIndicadorTodasColumnas(listaMapa, caracterSeparador, "no", getServicioConfiguracionGeneral().getMapaPathTiposFuente()), responseHeaders, HttpStatus.OK);
		ResponseEntity<AtributosMapDto> resp = new ResponseEntity<AtributosMapDto>(gestorCUAtributo.cargarDatosMapaIndicador(listaMapa, caracterSeparador, getServicioConfiguracionGeneral().getMapaPathTiposFuente()),  responseHeaders, HttpStatus.OK);
		return resp;
	}

	private LinkedHashMap<String, TipoAtributoFD> obterMapaDeTiposDeColumna(EncapsuladorListSW<AtributoDto> listaColumnas) {
		LinkedHashMap<String, TipoAtributoFD> tiposDeAtributo = new LinkedHashMap<String, TipoAtributoFD>();
		for ( AtributoDto col : listaColumnas ) {			
			if (col.getRelacion()!=null) continue;
			String nombreColumna=col.getNombre();
			Integer i = 1; 
    		while ( tiposDeAtributo.get(nombreColumna)!=null ) {
    			nombreColumna = nombreColumna+i.toString();
    			i++;
    		}
			nombreColumna=arreglaNombreColumna(nombreColumna);
			if ( col.getColumna()==null ) {
				tiposDeAtributo.put(nombreColumna,TipoAtributoFD.VALORFDNUMERICO);
			} else {
				tiposDeAtributo.put(nombreColumna, col.getColumna().getTipoAtributo());
			}
		}
		return tiposDeAtributo;
	}
	
	/**
	 * Formatea un valor segun o tipo de atributo que teña a columna correspondente
	 * @param valor Valor a formatear
	 * @param tipoAtributoFD Tipo de atributo
	 * @return Cadea de texto formateada
	 */
	private String formatearValor(EncapsuladorStringSW valor, TipoAtributoFD tipoAtributoFD) {
		try {
			if (tipoAtributoFD == TipoAtributoFD.VALORFDNUMERICO) 
				return valor.getTexto();
			else if(tipoAtributoFD == TipoAtributoFD.VALORFDGEOGRAFICO) 
				return "ST_GeomFromText('" + valor.getTexto() + "')";
			else if(tipoAtributoFD == TipoAtributoFD.VALORFDFECHA) 
				return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(UtilFecha.multiParse(valor.getTexto()));
			
			return (valor.getTexto() == null ? "null" : "'" + valor.getTexto() + "'");
		} catch (Exception e) {
			return null;
		}
	}

	
	private String crearCabeceiraSql(String nomeTaboa, HashMap<String, TipoAtributoFD> tiposDeAtributo, String caracterNewLine) {
		nomeTaboa = StringEscapeUtils.escapeJava(nomeTaboa);
		nomeTaboa = StringEscapeUtils.escapeSql(nomeTaboa).replace(" ", "_");

		String cabeceira = "";
		String columnas = "";
		String engadirXeometria = "";
		
		for (String key : tiposDeAtributo.keySet()) {
			switch (tiposDeAtributo.get(key)) {
				case VALORFDNUMERICO:
					columnas += "\"" + key + "\" " + "INT";
					columnas += ", " + caracterNewLine;
					break;
				case VALORFDGEOGRAFICO:
					//Engádese a parte
					String sistemaCoordenadas= getServicioConfiguracionGeneral().getCodigoCordenadas();
					if(sistemaCoordenadas!=null && ("").equals(sistemaCoordenadas.trim())){
						sistemaCoordenadas="-1";
					}
					engadirXeometria = "SELECT AddGeometryColumn('" + nomeTaboa + "','" + key + "','"+sistemaCoordenadas+"','MULTIPOLYGON',2);";
					break;
				case VALORFDFECHA:
					columnas += "\"" + key + "\" " + "DATETIME";
					columnas += ", " + caracterNewLine;
					break;
				default:
					columnas += "\"" + key + "\" " + "VARCHAR(255)";
					columnas += ", " + caracterNewLine;
					break;
			}
		}
		
		if (columnas.length() > 0 && nomeTaboa.length() > 0) {
			columnas = columnas.substring(0, columnas.length() - (caracterNewLine.length() + 2)); // Quitamos a última ', ' + newLine

			cabeceira += "SET CLIENT_ENCODING TO '" + DEFAULT_ENCODE + "';" + caracterNewLine;
			cabeceira += "SET STANDARD_CONFORMING_STRINGS TO ON;" + caracterNewLine;
			cabeceira += "BEGIN;" + caracterNewLine;
			cabeceira += "CREATE TABLE \""+ nomeTaboa + "\" (gid serial PRIMARY KEY, " + caracterNewLine;
			cabeceira += columnas + ");" + caracterNewLine;
			cabeceira += engadirXeometria + caracterNewLine;
		}
		
		return cabeceira;
	}
	
	/**
	 * Crea unha linea de insert para introducir nun script.sql
	 * @param columnas Set cos nomes das columnas
	 * @param nomeTaboa Nome da taboa onde se vai insertar
	 * @param valores Lista de valores que se van a insertar
	 * @param newLine String correspondente a unha nova linea
	 * @return Sentencia insert para os datos especificados
	 * @throws IOException
	 */
	private String crearInsert(Set<String> columnas, String nomeTaboa, List<String> valores, String newLine) throws IOException {
		nomeTaboa = StringEscapeUtils.escapeJava(nomeTaboa);
		nomeTaboa = StringEscapeUtils.escapeSql(nomeTaboa).replace(" ", "_");
		return "INSERT INTO \"" + nomeTaboa + "\"(\"" + StringUtils.join(columnas, "\", \"") + "\") " + 
				"VALUES (" + StringUtils.join(valores, ", ") + ");" + newLine;
	}
	
	/**
	 * Pecha todos los recursos pasados por parametro.
	 * @param closeables Recursos a pechar
	 * @return Devolve se todolos recursos foron pechados ou non
	 */
	private boolean pecharRecursos(Closeable... closeables) {
		Boolean todosPechados = true;
		for (Closeable rsc : closeables) {
			try {
				rsc.close();
			} catch (IOException e) {
				todosPechados = false;
			}
		}
		return todosPechados;
	}
	
	/**
	 * Elimina de forma segura os arquivos temporais
	 * @return Devolve se foron borrados correctamente todos os arquivos
	 */
	private boolean borrarTmpSeguro(File... tmpFiles) {
		Boolean todosEliminados = true;
		for (File tmp : tmpFiles) {
			try {
				tmp.delete();
			} catch (Exception e) {
				todosEliminados = false;
			}
		}
		return todosEliminados;
	}	
	
	/**
	 * Parsea o nome dun atributo no caso de ter formato numérico
	 * (É necesario parsear os nomes dos atributos, porque á hora de crear un GML
	 * non se admiten nomes que comecen por un caracter numérico)
	 * Ex:
	 * 12345 -> _12345
	 * 
	 * @param cadea
	 * @return Cadea parseada no caso de ser numérica ou a propia cadea no caso de non selo
	 */
	private String parsearNomeNumerico(String cadea){
		try {
			Integer.parseInt(cadea);
			return PREFIXO_NOMES_NUMERICOS + cadea;
		} catch (NumberFormatException nfe){
			return cadea;
		}
	}
	
	private int obterIndiceMaximoDoMapa(LinkedHashMap<String, ValorFDDto> mapaDatos) {
		int numeroDeElementosDaListaMaisGrande = 0;
		for (String key : mapaDatos.keySet()) {
			if (numeroDeElementosDaListaMaisGrande < mapaDatos.get(key).getValores().size()) 
				numeroDeElementosDaListaMaisGrande = mapaDatos.get(key).getValores().size();
		}
		return numeroDeElementosDaListaMaisGrande;
	}
	
	@RequestMapping(method=RequestMethod.POST,value="/cargaBusquedaAvanzada/{idUsuario}")
	public ResponseEntity<EncapsuladorMapSW<String, EncapsuladorMapSW<Long, EncapsuladorListSW<ElementoJerarquiaDto>>>> cargaBusquedaAvanzada(@RequestBody IndicadorBusquedaAvanzadaDto indicadorBusquedaAvanzadaDto,@PathVariable("idUsuario") Long idUsuario) throws XPathExpressionException, ParserConfigurationException, SAXException, IOException{
		return new ResponseEntity<EncapsuladorMapSW<String,EncapsuladorMapSW<Long,EncapsuladorListSW<ElementoJerarquiaDto>>>>(gestorCUIndicador.cargaBusquedaAvanzada(idUsuario, indicadorBusquedaAvanzadaDto, getServicioConfiguracionGeneral().getPathMetadatos()), responseHeaders, HttpStatus.OK);
	}
	
	private String arreglaNombreColumna(String nombre) {
		nombre = nombre.replaceAll("-", "_");
		nombre = nombre.replaceAll(">", "_");
		nombre = nombre.replaceAll(" ", "_");
		return nombre;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/autorizarIndicador/{id}/{asunto}/{texto}/{idUsuario}")
	public ResponseEntity<EncapsuladorBooleanSW> autorizarIndicador(@PathVariable("id") Long id, @PathVariable("asunto") String asunto, @PathVariable("texto") String texto, 
			@PathVariable("idUsuario")Long idUsuario, HttpServletRequest request) {
		EncapsuladorBooleanSW encapsulador = new EncapsuladorBooleanSW();
		try {
			EncapsuladorErroresSW errores = new EncapsuladorErroresSW();
			IndicadorDto indicadorDto = gestorCUIndicador.cargaPorId(id);
			
			//Enviar correo al usuario interesado
			UsuarioDto usuario = gestorCUUsuario.cargaUsuarioPorLogin(indicadorDto.getLoginCreador());
			if ( usuario != null && usuario.getId()>0 && usuario.getEmail()!=null && !usuario.getEmail().equals("") ) {
				ApplicationContext context = new ClassPathXmlApplicationContext(SPRING_MAIL_CONTEXT);
				GestorCUCorreoImpl gestorCUCorreo = (GestorCUCorreoImpl) context.getBean("gestorCUCorreo");
				gestorCUCorreo.enviarCorreo(usuario.getEmail(), asunto, texto);
			}
			
			indicadorDto.setPteAprobacionPublico(false);
			indicadorDto.setPublico(true);
			gestorCUIndicador.guarda(indicadorDto, idUsuario, errores);
			encapsulador.setValorLogico(true);
			ResponseEntity<EncapsuladorBooleanSW> respuesta = new ResponseEntity<EncapsuladorBooleanSW>(encapsulador,responseHeaders, HttpStatus.OK);
			
			return respuesta;
		} catch (Exception ex) {
			encapsulador.setValorLogico(false);
			return new ResponseEntity<EncapsuladorBooleanSW>(encapsulador, responseHeaders, HttpStatus.OK);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/descartarIndicador/{id}/{asunto}/{texto}/{idUsuario}")
	public ResponseEntity<EncapsuladorBooleanSW> descartarIndicador(@PathVariable("id") Long id, @PathVariable("asunto") String asunto, @PathVariable("texto") String texto, 
			@PathVariable("idUsuario")Long idUsuario, HttpServletRequest request) {
		EncapsuladorBooleanSW encapsulador = new EncapsuladorBooleanSW();
		try {
			EncapsuladorErroresSW errores = new EncapsuladorErroresSW();
			IndicadorDto indicadorDto = gestorCUIndicador.cargaPorId(id);
			
			//Enviar correo al usuario interesado
			UsuarioDto usuario = gestorCUUsuario.cargaUsuarioPorLogin(indicadorDto.getLoginCreador());
			if ( usuario != null && usuario.getId()>0 && usuario.getEmail()!=null && !usuario.getEmail().equals("") ) {
				ApplicationContext context = new ClassPathXmlApplicationContext(SPRING_MAIL_CONTEXT);
				GestorCUCorreoImpl gestorCUCorreo = (GestorCUCorreoImpl) context.getBean("gestorCUCorreo");
				gestorCUCorreo.enviarCorreo(usuario.getEmail(), asunto, texto);
			}
			
			indicadorDto.setPteAprobacionPublico(false);
			indicadorDto.setPublico(false);
			gestorCUIndicador.guarda(indicadorDto, idUsuario, errores);
			encapsulador.setValorLogico(true);
			ResponseEntity<EncapsuladorBooleanSW> respuesta = new ResponseEntity<EncapsuladorBooleanSW>(encapsulador,responseHeaders, HttpStatus.OK);
			
			return respuesta;
		} catch (Exception ex) {
			encapsulador.setValorLogico(false);
			return new ResponseEntity<EncapsuladorBooleanSW>(encapsulador, responseHeaders, HttpStatus.OK);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/retirarIndicador/{id}/{asunto}/{texto}/{idUsuario}")
	public ResponseEntity<EncapsuladorBooleanSW> retirarIndicador(@PathVariable("id") Long id, @PathVariable("asunto") String asunto, @PathVariable("texto") String texto, 
			@PathVariable("idUsuario")Long idUsuario, HttpServletRequest request) {
		EncapsuladorBooleanSW encapsulador = new EncapsuladorBooleanSW();
		try {
			EncapsuladorErroresSW errores = new EncapsuladorErroresSW();
			IndicadorDto indicadorDto = gestorCUIndicador.cargaPorId(id);
			
			//Enviar correo al usuario interesado
			UsuarioDto usuario = gestorCUUsuario.cargaUsuarioPorLogin(indicadorDto.getLoginCreador());
			if ( usuario != null && usuario.getId()>0 && usuario.getEmail()!=null && !usuario.getEmail().equals("") ) {
				ApplicationContext context = new ClassPathXmlApplicationContext(SPRING_MAIL_CONTEXT);
				GestorCUCorreoImpl gestorCUCorreo = (GestorCUCorreoImpl) context.getBean("gestorCUCorreo");
				gestorCUCorreo.enviarCorreo(usuario.getEmail(), asunto, texto);
			}
			
			indicadorDto.setPublico(false);
			gestorCUIndicador.guarda(indicadorDto, idUsuario, errores);
			encapsulador.setValorLogico(true);
			ResponseEntity<EncapsuladorBooleanSW> respuesta = new ResponseEntity<EncapsuladorBooleanSW>(encapsulador,responseHeaders, HttpStatus.OK);
			return respuesta;
		} catch (Exception ex) {
			encapsulador.setValorLogico(false);
			return new ResponseEntity<EncapsuladorBooleanSW>(encapsulador, responseHeaders, HttpStatus.OK);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/publicarEnWeb/{id}/{loginUltimaPeticion}/{idUsuario}")
	public ResponseEntity<EncapsuladorBooleanSW> publicarEnWeb(@PathVariable("id") Long id, @PathVariable("loginUltimaPeticion") String loginUltimaPeticion, 
			@PathVariable("idUsuario")Long idUsuario, HttpServletRequest request) {
		EncapsuladorBooleanSW encapsulador = new EncapsuladorBooleanSW();
		try {
			EncapsuladorErroresSW errores = new EncapsuladorErroresSW();
			IndicadorDto indicadorDto = gestorCUIndicador.cargaPorId(id);
			indicadorDto.setPublicadoEnWeb(false);
			indicadorDto.setPteAprobacion(true);
			Integer numUsuariosPublicacion = indicadorDto.getNumUsuariosPublicacion();
			if ( numUsuariosPublicacion == null ) 
				numUsuariosPublicacion = 0;
			indicadorDto.setNumUsuariosPublicacion(numUsuariosPublicacion+1);
			indicadorDto.setLoginUltimaPeticion(loginUltimaPeticion);
			gestorCUIndicador.guarda(indicadorDto, idUsuario, errores);
			encapsulador.setValorLogico(true);
			ResponseEntity<EncapsuladorBooleanSW> respuesta = new ResponseEntity<EncapsuladorBooleanSW>(encapsulador,responseHeaders, HttpStatus.OK);
			return respuesta;
		} catch (Exception ex) {
			encapsulador.setValorLogico(false);
			return new ResponseEntity<EncapsuladorBooleanSW>(encapsulador, responseHeaders, HttpStatus.OK);
		}
	}
	
	
	@RequestMapping(method = RequestMethod.GET, value = "/cargaTodasCategoriasYIndicadoresPorUsuarioVisualizar/{idUsuario}")
	public ResponseEntity<EncapsuladorMapSW<String, EncapsuladorMapSW<Long, EncapsuladorListSW<ElementoJerarquiaDto>>>> cargaTodasCategoriasYIndicadoresPorUsuarioVisualizar(@PathVariable("idUsuario") Long idUsuario) {
		return new ResponseEntity<EncapsuladorMapSW<String,EncapsuladorMapSW<Long,EncapsuladorListSW<ElementoJerarquiaDto>>>>(gestorCUIndicador.cargaTodasCategoriasYIndicadoresPorUsuarioVisualizar(idUsuario), responseHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/cargaTodasCategoriasYIndicadoresPorUsuarioVisualizarPorFuente/{idUsuario}/{idFuente}")
	public ResponseEntity<EncapsuladorMapSW<String, EncapsuladorMapSW<Long, EncapsuladorListSW<ElementoJerarquiaDto>>>> cargaTodasCategoriasYIndicadoresPorUsuarioVisualizarPorFuente(@PathVariable("idUsuario") Long idUsuario, @PathVariable("idFuente") Long idFuente) {
		return new ResponseEntity<EncapsuladorMapSW<String,EncapsuladorMapSW<Long,EncapsuladorListSW<ElementoJerarquiaDto>>>>(gestorCUIndicador.cargaTodasCategoriasYIndicadoresPorUsuarioVisualizarPorFuente(idUsuario, idFuente), responseHeaders, HttpStatus.OK);
	}	
	
	@RequestMapping(method = RequestMethod.GET, value = "/autorizarIndicadorPublicacionWeb/{id}/{asunto}/{texto}/{idUsuario}")
	public ResponseEntity<EncapsuladorBooleanSW> autorizarIndicadorPublicacionWeb(@PathVariable("id") Long id, @PathVariable("asunto") String asunto, @PathVariable("texto") String texto, 
			@PathVariable("idUsuario")Long idUsuario, HttpServletRequest request) {
		EncapsuladorBooleanSW encapsulador = new EncapsuladorBooleanSW();
		try {
			EncapsuladorErroresSW errores = new EncapsuladorErroresSW();
			IndicadorDto indicadorDto = gestorCUIndicador.cargaPorId(id);
			
			//Enviar correo al usuario interesado
			UsuarioDto usuario = gestorCUUsuario.cargaUsuarioPorLogin(indicadorDto.getLoginCreador());
			if ( usuario != null && usuario.getId()>0 && usuario.getEmail()!=null && !usuario.getEmail().equals("") ) {
				ApplicationContext context = new ClassPathXmlApplicationContext(SPRING_MAIL_CONTEXT);
				GestorCUCorreoImpl gestorCUCorreo = (GestorCUCorreoImpl) context.getBean("gestorCUCorreo");
				gestorCUCorreo.enviarCorreo(usuario.getEmail(), asunto, texto);
			}
			
			indicadorDto.setPteAprobacion(false);
			indicadorDto.setPublicadoEnWeb(true);
			gestorCUIndicador.guarda(indicadorDto, idUsuario, errores);
			encapsulador.setValorLogico(true);
			ResponseEntity<EncapsuladorBooleanSW> respuesta = new ResponseEntity<EncapsuladorBooleanSW>(encapsulador,responseHeaders, HttpStatus.OK);
			
			return respuesta;
		} catch (Exception ex) {
			encapsulador.setValorLogico(false);
			return new ResponseEntity<EncapsuladorBooleanSW>(encapsulador, responseHeaders, HttpStatus.OK);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/descartarIndicadorPublicacionWeb/{id}/{asunto}/{texto}/{idUsuario}")
	public ResponseEntity<EncapsuladorBooleanSW> descartarIndicadorPublicacionWeb(@PathVariable("id") Long id, @PathVariable("asunto") String asunto, @PathVariable("texto") String texto, 
			@PathVariable("idUsuario")Long idUsuario, HttpServletRequest request) {
		EncapsuladorBooleanSW encapsulador = new EncapsuladorBooleanSW();
		try {
			EncapsuladorErroresSW errores = new EncapsuladorErroresSW();
			IndicadorDto indicadorDto = gestorCUIndicador.cargaPorId(id);
			
			//Enviar correo al usuario interesado
			UsuarioDto usuario = gestorCUUsuario.cargaUsuarioPorLogin(indicadorDto.getLoginCreador());
			if ( usuario != null && usuario.getId()>0 && usuario.getEmail()!=null && !usuario.getEmail().equals("") ) {
				ApplicationContext context = new ClassPathXmlApplicationContext(SPRING_MAIL_CONTEXT);
				GestorCUCorreoImpl gestorCUCorreo = (GestorCUCorreoImpl) context.getBean("gestorCUCorreo");
				gestorCUCorreo.enviarCorreo(usuario.getEmail(), asunto, texto);
			}
			
			indicadorDto.setPteAprobacion(false);
			indicadorDto.setPublicadoEnWeb(false);
			gestorCUIndicador.guarda(indicadorDto, idUsuario, errores);
			encapsulador.setValorLogico(true);
			ResponseEntity<EncapsuladorBooleanSW> respuesta = new ResponseEntity<EncapsuladorBooleanSW>(encapsulador,responseHeaders, HttpStatus.OK);
			
			return respuesta;
		} catch (Exception ex) {
			encapsulador.setValorLogico(false);
			return new ResponseEntity<EncapsuladorBooleanSW>(encapsulador, responseHeaders, HttpStatus.OK);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/retirarIndicadorPublicacionWeb/{id}/{asunto}/{texto}/{idUsuario}")
	public ResponseEntity<EncapsuladorBooleanSW> retirarIndicadorPublicacionWeb(@PathVariable("id") Long id, @PathVariable("asunto") String asunto, @PathVariable("texto") String texto, 
			@PathVariable("idUsuario")Long idUsuario, HttpServletRequest request) {
		EncapsuladorBooleanSW encapsulador = new EncapsuladorBooleanSW();
		try {
			EncapsuladorErroresSW errores = new EncapsuladorErroresSW();
			IndicadorDto indicadorDto = gestorCUIndicador.cargaPorId(id);
			
			//Enviar correo al usuario interesado
			UsuarioDto usuario = gestorCUUsuario.cargaUsuarioPorLogin(indicadorDto.getLoginCreador());
			if ( usuario != null && usuario.getId()>0 && usuario.getEmail()!=null && !usuario.getEmail().equals("") ) {
				ApplicationContext context = new ClassPathXmlApplicationContext(SPRING_MAIL_CONTEXT);
				GestorCUCorreoImpl gestorCUCorreo = (GestorCUCorreoImpl) context.getBean("gestorCUCorreo");
				gestorCUCorreo.enviarCorreo(usuario.getEmail(), asunto, texto);
			}
			
			indicadorDto.setPublicadoEnWeb(false);
			indicadorDto.setPteAprobacion(false);
			gestorCUIndicador.guarda(indicadorDto, idUsuario, errores);
			encapsulador.setValorLogico(true);
			ResponseEntity<EncapsuladorBooleanSW> respuesta = new ResponseEntity<EncapsuladorBooleanSW>(encapsulador,responseHeaders, HttpStatus.OK);
			return respuesta;
		} catch (Exception ex) {
			encapsulador.setValorLogico(false);
			return new ResponseEntity<EncapsuladorBooleanSW>(encapsulador, responseHeaders, HttpStatus.OK);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/cabeceraExportarPDF/{id}")
	public ResponseEntity<EncapsuladorListSW<String>> cabeceraExportarPDF(@PathVariable("id") Long id,HttpServletRequest request) {
		String caracterSeparador = getServicioConfiguracionGeneral().getCaracterSeparadorCSV();
		EncapsuladorListSW<AtributoDto> listaColumnas = obterColumnas(id, request);
		AtributosMapDto datos = gestorCUAtributo.cargarDatosIndicador(listaColumnas, caracterSeparador, "no", getServicioConfiguracionGeneral().getMapaPathTiposFuente());
		List<String> listaInvertida = new ArrayList<String>();
		try {
			final LinkedHashMap<String, ValorFDDto> mapaDatos = datos.getContenido();
			if (mapaDatos != null && mapaDatos.keySet().size() > 0) {
				listaInvertida.addAll(mapaDatos.keySet());
			}
		} catch (Exception e) {
			ResponseEntity<EncapsuladorListSW<String>> respuesta = new ResponseEntity<EncapsuladorListSW<String>>(new EncapsuladorListSW<String>(), HttpStatus.OK);
			return respuesta;
		}
		EncapsuladorListSW<String> listaRespuesta = new EncapsuladorListSW<String>();
		listaRespuesta.setLista(listaInvertida);
		ResponseEntity<EncapsuladorListSW<String>> respuesta = new ResponseEntity<EncapsuladorListSW<String>>(listaRespuesta, HttpStatus.OK);
		return respuesta;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/datosExportarPDF/{id}")
	public ResponseEntity<EncapsuladorStringSW> datosExportarPDF(@PathVariable("id") Long id,HttpServletRequest request) {
		String caracterSeparador = getServicioConfiguracionGeneral().getCaracterSeparadorCSV();
		EncapsuladorListSW<AtributoDto> listaColumnas = obterColumnas(id, request);
		String nomeIndicador = listaColumnas.get(0).getColumna().getTabla().getIndicador().getNombre();
		AtributosMapDto datos = gestorCUAtributo.cargarDatosIndicador(listaColumnas, caracterSeparador, "no", getServicioConfiguracionGeneral().getMapaPathTiposFuente());
		
		EncapsuladorFileSW ficheroCSV = new EncapsuladorFileSW();
		File archivoTmp = null;
		String textoResultado = "";
		
		try {
			final LinkedHashMap<String, ValorFDDto> mapaDatos = datos.getContenido();
			
			if (mapaDatos != null && mapaDatos.keySet().size() > 0) {
				List<String[]> listaInvertida = new ArrayList<String[]>();
				List<String> valoresFilaCSV = null;

				listaInvertida.add(mapaDatos.keySet().toArray(new String[mapaDatos.keySet().size()]));

				int numeroDeElementosDaListaMaisGrande = obterIndiceMaximoDoMapa(mapaDatos);
				for (int i = 0; i < numeroDeElementosDaListaMaisGrande; i++) {
					valoresFilaCSV = new ArrayList<String>();
					for (int j = 0; j < mapaDatos.keySet().size(); j++) {
						if (i < mapaDatos.get((String)mapaDatos.keySet().toArray()[j]).getValores().size())
							valoresFilaCSV.add(mapaDatos.get((String)mapaDatos.keySet().toArray()[j]).getValores().get(i).getTexto());
						else 
							valoresFilaCSV.add("");
					}

					listaInvertida.add(valoresFilaCSV.toArray(new String[valoresFilaCSV.size()]));
				}
				FileInputStream inputStream = null;
				Writer writer = null;
				CSVWriter writerCSV = null;
				byte[] byteArrayCSV = null;

				try {
					archivoTmp = File.createTempFile("CSV_", ".csv");
					writer = new OutputStreamWriter(new FileOutputStream(archivoTmp), DEFAULT_ENCODE);
										
					writerCSV = new CSVWriter(writer, caracterSeparador.charAt(0));
					writerCSV.writeAll(listaInvertida);

					inputStream = new FileInputStream(archivoTmp);
				} catch (Exception e) {
					throw e;
				}
				
				StringWriter escritor = new StringWriter();
				IOUtils.copy(inputStream, escritor, DEFAULT_ENCODE);
				textoResultado = escritor.toString();
				pecharRecursos(inputStream);
			}
		} catch (Exception e) {
			ResponseEntity<EncapsuladorStringSW> respuesta = new ResponseEntity<EncapsuladorStringSW>(new EncapsuladorStringSW(), HttpStatus.OK);
			return respuesta;
		} finally {
			borrarTmpSeguro(archivoTmp);
		}
		EncapsuladorStringSW encap = new EncapsuladorStringSW();
		encap.setTexto(textoResultado);
		ResponseEntity<EncapsuladorStringSW> respuesta = new ResponseEntity<EncapsuladorStringSW>(encap, HttpStatus.OK);
		return respuesta;
	}
}
