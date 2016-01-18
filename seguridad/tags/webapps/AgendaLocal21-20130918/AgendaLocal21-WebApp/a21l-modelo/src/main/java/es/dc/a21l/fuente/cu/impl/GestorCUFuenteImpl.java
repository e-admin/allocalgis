/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.fuente.cu.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import es.dc.a21l.base.modelo.GestionErrores.EncapsuladorErroresSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorBooleanSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorFileSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorListSW;
import es.dc.a21l.base.modelo.validadores.Validador;
import es.dc.a21l.base.utils.enumerados.TiposFuente;
import es.dc.a21l.fuente.cu.AtributoFuenteDatosDto;
import es.dc.a21l.fuente.cu.AtributosMapDto;
import es.dc.a21l.fuente.cu.DescripcionAtributoDto;
import es.dc.a21l.fuente.cu.FuenteDto;
import es.dc.a21l.fuente.cu.GestorCUFuente;
import es.dc.a21l.fuente.cu.TablaFuenteDatosDto;
import es.dc.a21l.fuente.cu.ValorFDDto;
import es.dc.a21l.fuente.modelo.Fuente;
import es.dc.a21l.fuente.modelo.FuenteRepositorio;
import es.dc.a21l.fuente.modelo.OrigenCSVRepositorio;
import es.dc.a21l.fuente.modelo.OrigenGMLRepositorio;
import es.dc.a21l.fuente.modelo.OrigenMysqlRepositorio;
import es.dc.a21l.fuente.modelo.OrigenODBCRepositorio;
import es.dc.a21l.fuente.modelo.OrigenOracleRepositorio;
import es.dc.a21l.fuente.modelo.OrigenPostGisRepositorio;
import es.dc.a21l.fuente.modelo.OrigenShapeFileRepositorio;
import es.dc.a21l.fuente.modelo.OrigenWFSRepositorio;
import es.dc.a21l.usuario.modelo.Usuario;
import es.dc.a21l.usuario.modelo.UsuarioRepositorio;

/**
 *
 * @author Balidea Consulting & Programming
 */
public class GestorCUFuenteImpl implements GestorCUFuente{
	private static final Logger log = LoggerFactory.getLogger(GestorCUFuenteImpl.class);
	private Mapper mapper;
    private FuenteRepositorio fuenteRepositorio;
    private OrigenODBCRepositorio origenODBCRepositorio;
    private OrigenMysqlRepositorio origenMysqlRepositorio;
    private OrigenOracleRepositorio origenOracleRepositorio;
    private OrigenCSVRepositorio origenCSVRepositorio;
    private OrigenShapeFileRepositorio origenShapeFileRepositorio;
    private OrigenPostGisRepositorio origenPostGisRepositorio;
    private OrigenGMLRepositorio origenGMLRepositorio;
    private OrigenWFSRepositorio origenWFSRepositorio;
    
    private UsuarioRepositorio usuarioRepositorio;
    
    private static final String ERROR_PARSEO_FICHERO = "__ERROR__PARSEO__FICHERO__";
    
  
    @Autowired
    public void setMapper(Mapper mapper) {
        this.mapper = mapper;
    }
    @Autowired
    public void setFuenteRepositorio(FuenteRepositorio fuenteRepositorio) {
        this.fuenteRepositorio = fuenteRepositorio;
    }
    
    @Autowired
    public void setOrigenODBCRepositorio(OrigenODBCRepositorio origenODBCRepositorio) {
        this.origenODBCRepositorio = origenODBCRepositorio;
    }
    
    @Autowired
    public void setOrigenCSVRepositorio(OrigenCSVRepositorio origenCSVRepositorio) {
        this.origenCSVRepositorio = origenCSVRepositorio;
    }
    
    @Autowired
    public void setOrigenShapeFileRepositorio(OrigenShapeFileRepositorio origenShapeFileRepositorio) {
        this.origenShapeFileRepositorio = origenShapeFileRepositorio;
    }
    
    @Autowired
    public void setOrigenPostGisRepositorio(OrigenPostGisRepositorio origenPostGisRepositorio) {
    	this.origenPostGisRepositorio = origenPostGisRepositorio;
    }
    
    @Autowired
    public void setOrigenGMLRepositorio(OrigenGMLRepositorio origenGMLRepositorio) {
    	this.origenGMLRepositorio = origenGMLRepositorio;
    }
    
    @Autowired
    public void setOrigenWFSRepositorio(OrigenWFSRepositorio origenWFSRepositorio) {
    	this.origenWFSRepositorio = origenWFSRepositorio;
    }
    
    @Autowired
    public void setUsuarioRepositorio(UsuarioRepositorio usuarioRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
    }
    
    @Autowired
    public void setOrigenMysqlRepositorio(
			OrigenMysqlRepositorio origenMysqlRepositorio) {
		this.origenMysqlRepositorio = origenMysqlRepositorio;
	}
    
    @Autowired
	public void setOrigenOracleRepositorio(
			OrigenOracleRepositorio origenOracleRepositorio) {
		this.origenOracleRepositorio = origenOracleRepositorio;
	}
    
	public EncapsuladorListSW<FuenteDto> cargaTodos() {
        List<Fuente> fuentes = fuenteRepositorio.cargaTodos();
        EncapsuladorListSW<FuenteDto> result = new EncapsuladorListSW<FuenteDto>();
        Fuente2FuenteDtoTransformer transformer = new Fuente2FuenteDtoTransformer(mapper);
        for (Fuente fuente: fuentes)
        { 
        	result.add(transformer.transform(fuente));
        }
        return result;
    }

    public EncapsuladorListSW<FuenteDto> listarFuentesExternas() {
        List<Fuente> fuentes = fuenteRepositorio.cargaTodos();
        EncapsuladorListSW<FuenteDto> result = new EncapsuladorListSW<FuenteDto>();
        Fuente2FuenteDtoTransformer transformer = new Fuente2FuenteDtoTransformer(mapper);
        for (Fuente fuente: fuentes)
        {  if(fuente.getEsCatalogoInterno() == 0)
            {
        	   result.add(transformer.transform(fuente));
            }
        }
        return result;
    }
    
    public EncapsuladorListSW<FuenteDto> listarFuentesInternas() {
        List<Fuente> fuentes = fuenteRepositorio.cargaTodos();
        EncapsuladorListSW<FuenteDto> result = new EncapsuladorListSW<FuenteDto>();
        Fuente2FuenteDtoTransformer transformer = new Fuente2FuenteDtoTransformer(mapper);
        for (Fuente fuente: fuentes)
        {  if(fuente.getEsCatalogoInterno() == 1)
            {
        	   result.add(transformer.transform(fuente));
            }
        }
        return result;
    }
    
    public FuenteDto borra(long id, String path, EncapsuladorErroresSW erros) {
        FuenteDto fuenteDto = carga(id);
      
        //Comprobamos si la fuente tiene algun indicador asociado. En ese caso, no la podemos eliminar.
        if ( cargaIndicadoresAsociadosFuente(fuenteDto.getId()) ) {
        	log.debug("La fuente " + fuenteDto.getNombre() + " no se ha podido borrar porque tiene indicadores asociados");
    		erros.addError(FuenteFormErrorsEmun.ERROR_ELIMINAR_FUENTE_INDICADORES_ASOCIADOS);
    		return fuenteDto;
        }
        
        //Solo se borran los ficheros si es que los hay
        if ( path != null ) {
	        String directorio = path+id+"/";
	        if (!borrarConHijos(directorio,true)) {
	    		log.debug("El fichero/carpeta " + directorio + " no se ha podido borrar o vaciar");
	    		erros.addError(FuenteFormErrorsEmun.ERROR_ELIMINAR_FICHERO);
	    		return fuenteDto;
	    	}
        }
        
        fuenteRepositorio.borra(id);
        return fuenteDto;
    }

    public FuenteDto carga(long id) {
        return new Fuente2FuenteDtoTransformer(mapper).transform(fuenteRepositorio.carga(id));
    }

    public FuenteDto garda(FuenteDto fuenteDto, Long idUsuarioCreador, EncapsuladorErroresSW erros,String sistemaCoordenadas) {
    	if(fuenteDto.getNombre()!=null && (cargaExisteFuenteConNombre(fuenteDto.getNombre()) && fuenteDto.getId()<=0)){
    		erros.addError(FuenteFormErrorsEmun.NOMBRE_EN_USO);
    	}
    	
    	Validador<FuenteDto> fuenteDtoValidador = new FuenteDtoValidador();
        //Tipo de Fuente
        fuenteDtoValidador.valida(fuenteDto, erros);
        if (erros.getHashErrors()) return null;

        Fuente fuente = new FuenteDto2FuenteTransformer(mapper, fuenteRepositorio).transform(fuenteDto);
        
       
    	
        //La fecha de registro se actualiza cada vez q se guarda
        fuente.setFechaRegistro(new Date());
        Usuario user = usuarioRepositorio.carga(idUsuarioCreador);
        fuente.setRegistradaPor(user);        
        fuente.setEsCatalogoInterno((short) 0);
        
        Fuente fuente2 = fuenteRepositorio.guarda(fuente);
        FuenteDto resultado= new Fuente2FuenteDtoTransformer(mapper).transform(fuente2);
        //Si es bd tipo postgis compruebo que sus columnas geométricas tengan el sistema de coordenadas especificado en el fichero de configuración.
        if(fuenteDto.getTipo()!=null && fuenteDto.getTipo()==TiposFuente.BDESPACIAL){
    		if(!origenPostGisRepositorio.comprobarSridColumnasGeometricas(fuente, sistemaCoordenadas)){
    			resultado.setColumnasGeometricasErroneas(true);
    		}else{
    			resultado.setColumnasGeometricasErroneas(false);
    		}
    	}
        return resultado;
    }

    public boolean existe(long id) {        
        return fuenteRepositorio.existe(id);
    }

    public FuenteDto existeFuenteCatalogo(FuenteDto fuenteDto){
        log.debug("existeFuenteCatalogo: fuentes que se va a comprobar: "+fuenteDto.getNombre() );
    	Fuente f=fuenteRepositorio.existeFuenteInterna(fuenteDto.getNombre(),fuenteDto.getInfoConexion(),fuenteDto.getUsuario(),fuenteDto.getPassword(),fuenteDto.getFechaRegistro());
    	return new Fuente2FuenteDtoTransformer(mapper).transform(f);
    
    }

	public FuenteDto gardaFuenteCatalogo(FuenteDto fuenteDto,Long idUsuarioCreador) {

		EncapsuladorErroresSW er = new EncapsuladorErroresSW();

		fuenteDto.setEsCatalogoInterno((short) 1);
		fuenteDto.setTipo(TiposFuente.BDESPACIAL);     
		Validador<FuenteDto> fuenteDtoValidador = new FuenteDtoValidador();
		fuenteDtoValidador.valida(fuenteDto, er);

		if (er.getHashErrors()) {			
			 log.error("gardaFuenteCatalogo: ERROR VALIDACION DE FUENTE");	
			return new FuenteDto();

		}
		Fuente fuente = new FuenteDto2FuenteTransformer(mapper,	fuenteRepositorio).transform(fuenteDto);
		Usuario user = usuarioRepositorio.carga(idUsuarioCreador);
		fuente.setRegistradaPor(user);

		Fuente fuente2 = fuenteRepositorio.guarda(fuente);

		return new Fuente2FuenteDtoTransformer(mapper).transform(fuente2);
	}
    
	public List<String> listarTablasCatalogoInterno() {
		return null;
	}
    public Map<String,List<AtributoFuenteDatosDto>> obtenerDatosTablaCatalogoInterno() {
    	return null;
    }
    public Map<AtributoFuenteDatosDto,DescripcionAtributoDto> obtenerEsquemaTablaCatalogoInterno(String tabla) {
    	return null;
    }
    public Map<String,List<AtributoFuenteDatosDto>> obtenerMapaTablaCatalogoInterno(String tabla) { 
    	return null;
    }
    public List<String> listarColumnasTablaCatalogoInterno(String tabla) {
    	return null;
    }
    public List<FuenteDto> listaFuentesExternas() {
    	return null;
    }
    public List<FuenteDto> listaFuentes() {
    	return null;
    } 
    
    public EncapsuladorListSW<TablaFuenteDatosDto> listarTablasFuenteExterna(long id, String path) {
    	FuenteDto fuenteDto = carga(id);
    	TiposFuente tipoFuente = fuenteDto.getTipo();
    	    	
    	EncapsuladorListSW<TablaFuenteDatosDto> listaTablas = new EncapsuladorListSW<TablaFuenteDatosDto>();
    	FuenteDto2FuenteTransformer transformer = new FuenteDto2FuenteTransformer(mapper, fuenteRepositorio);
    	Fuente fuente = transformer.transform(fuenteDto);
    	
    	if ( tipoFuente == TiposFuente.ODBC ) {
    		listaTablas = origenODBCRepositorio.listarTablasFuenteExterna(fuente);
    	}else if(tipoFuente==TiposFuente.MYSQL){
    		listaTablas=origenMysqlRepositorio.listarTablasFuenteExterna(fuente);
    	}else if(tipoFuente==TiposFuente.ORACLE){
    		listaTablas=origenOracleRepositorio.listarTablasFuenteExterna(fuente);
    	}else if ( tipoFuente == TiposFuente.CSV ) {
    		listaTablas = origenCSVRepositorio.listarTablasFuenteExterna(fuente,path);
    	} else if ( tipoFuente == TiposFuente.SHAPEFILE ) {
    		listaTablas = origenShapeFileRepositorio.listarTablasFuenteExterna(fuente,path);
    	} else if ( tipoFuente == TiposFuente.BDESPACIAL ) {
    		listaTablas = origenPostGisRepositorio.listarTablasFuenteExterna(fuente);
    	} else if ( tipoFuente == TiposFuente.GML ) {
			listaTablas = origenGMLRepositorio.listarTablasFuenteExterna(fuente,path);
		} else if ( tipoFuente == TiposFuente.WFS ) {
			listaTablas = origenWFSRepositorio.listarTablasFuenteExterna(fuente,path);
		}
    	
    	if ( listaTablas == null || listaTablas.getLista()==null ) {
    		EncapsuladorListSW<TablaFuenteDatosDto> lista = new EncapsuladorListSW<TablaFuenteDatosDto>();
    		TablaFuenteDatosDto tabla = new TablaFuenteDatosDto();
    		tabla.setNombre(ERROR_PARSEO_FICHERO);
    		lista.add(tabla);
    		return lista;
    	} 
		return listaTablas;
    	
    }
    
    
    public EncapsuladorListSW<AtributoFuenteDatosDto> listarColumnasTablaFuenteExterna(long id, String tabla, String path, String caracterSeparador) {
    	//Cargar la fuente por id para sacar la cadena de conexion y conectarse y consultar.
    	FuenteDto fuenteDto = carga(id);
    	FuenteDto2FuenteTransformer transformer = new FuenteDto2FuenteTransformer(mapper, fuenteRepositorio);
    	Fuente fuente = transformer.transform(fuenteDto);
    	EncapsuladorListSW<AtributoFuenteDatosDto> listaColumnas = new EncapsuladorListSW<AtributoFuenteDatosDto>();
    	TiposFuente tipoFuente = fuente.getTipo();
    	
    	if ( tipoFuente == TiposFuente.ODBC ) {
    		listaColumnas = origenODBCRepositorio.listarColumnasTablaFuenteExterna(fuente, tabla);
    	}else if(tipoFuente==TiposFuente.MYSQL) {
    		listaColumnas=origenMysqlRepositorio.listarColumnasTablaFuenteExterna(fuente, tabla);
    	}else if(tipoFuente==tipoFuente.ORACLE){
    		listaColumnas=origenOracleRepositorio.listarColumnasTablaFuenteExterna(fuente, tabla);
    	}else if ( tipoFuente == TiposFuente.CSV ) {
    		listaColumnas = origenCSVRepositorio.listarColumnasTablaFuenteExterna(fuente, tabla, path, caracterSeparador);
	    } else if ( tipoFuente == TiposFuente.SHAPEFILE ) {
	    	listaColumnas = origenShapeFileRepositorio.listarColumnasTablaFuenteExterna(fuente, tabla, path, true);
		} else if ( tipoFuente == TiposFuente.BDESPACIAL ) {
			String esquema = tabla.substring(0, tabla.indexOf("."));
			tabla = tabla.substring(esquema.length()+1, tabla.length());
			listaColumnas = origenPostGisRepositorio.listarColumnasTablaFuenteExterna(fuente, esquema, tabla);
		} else if ( tipoFuente == TiposFuente.GML ) {
			listaColumnas = origenGMLRepositorio.listarColumnasTablaFuenteExterna(fuente, tabla, path);
		} else if ( tipoFuente == TiposFuente.WFS ) {
			listaColumnas = origenWFSRepositorio.listarColumnasTablaFuenteExterna(fuente, tabla, path);
		}
    	return listaColumnas;
    }
    
   
    
    public EncapsuladorListSW<DescripcionAtributoDto> obtenerEsquemaTablaFuenteExterna(long id, String esquema, String tabla, String path, String caracterSeparador) {
    	//Cargar la fuente por id para sacar la cadena de conexion y conectarse y consultar.
    	FuenteDto fuenteDto = carga(id);
    	FuenteDto2FuenteTransformer transformer = new FuenteDto2FuenteTransformer(mapper, fuenteRepositorio);
    	Fuente fuente = transformer.transform(fuenteDto);
    	TiposFuente tipoFuente = fuente.getTipo();
    	Connection con = null;
    	EncapsuladorListSW<DescripcionAtributoDto> listaEsquema = new EncapsuladorListSW<DescripcionAtributoDto>();
    	if ( tipoFuente == TiposFuente.ODBC ) {
    		listaEsquema = origenODBCRepositorio.obtenerEsquemaTablaFuenteExterna(fuente, tabla);
    	}else if(tipoFuente==TiposFuente.MYSQL){
    		listaEsquema=origenMysqlRepositorio.obtenerEsquemaTablaFuenteExterna(fuente, tabla);
    	}else if(tipoFuente==TiposFuente.ORACLE){
    		listaEsquema=origenOracleRepositorio.obtenerEsquemaTablaFuenteExterna(fuente, tabla);
    	}else if ( tipoFuente == TiposFuente.CSV ) {
    		listaEsquema = origenCSVRepositorio.obtenerEsquemaTablaFuenteExterna(fuente, tabla, path, caracterSeparador);
    	} else if ( tipoFuente == TiposFuente.SHAPEFILE ) {
    		listaEsquema = origenShapeFileRepositorio.obtenerEsquemaTablaFuenteExterna(fuente, tabla, path);
		} else if ( tipoFuente == TiposFuente.BDESPACIAL ) {
			if (esquema == null || esquema.isEmpty()){
				esquema = tabla.substring(0, tabla.indexOf("."));
				tabla = tabla.substring(esquema.length()+1, tabla.length());
			}
			listaEsquema = origenPostGisRepositorio.obtenerEsquemaTablaFuenteExterna(fuente, esquema, tabla);
		} else if ( tipoFuente == TiposFuente.GML) {
			listaEsquema = origenGMLRepositorio.obtenerEsquemaTablaFuenteExterna(fuente, tabla, path);
		} else if ( tipoFuente == TiposFuente.WFS) {
			listaEsquema = origenWFSRepositorio.obtenerEsquemaTablaFuenteExterna(fuente, tabla, path);
		}
    	
    	return listaEsquema;
    }
    
    public AtributosMapDto obtenerDatosTablaFuenteExterna(long id, String esquema, String tabla, String path, String caracterSeparador) {
    	//Cargar la fuente por id para sacar la cadena de conexion y conectarse y consultar.
    	FuenteDto fuenteDto = carga(id);
    	FuenteDto2FuenteTransformer transformer = new FuenteDto2FuenteTransformer(mapper, fuenteRepositorio);
    	Fuente fuente = transformer.transform(fuenteDto);
    	TiposFuente tipoFuente = fuente.getTipo();
    	
    	AtributosMapDto mapaValores = new AtributosMapDto();
    	
    	if ( tipoFuente == TiposFuente.ODBC ) {
    		mapaValores = origenODBCRepositorio.obtenerDatosTablaFuenteExterna(fuente, tabla, false);
    	} else if(tipoFuente==TiposFuente.MYSQL){
    		mapaValores=origenMysqlRepositorio.obtenerDatosTablaFuenteExterna(fuente, tabla, false);
    	}else if(tipoFuente==TiposFuente.ORACLE){
    		mapaValores=origenOracleRepositorio.obtenerDatosTablaFuenteExterna(fuente, tabla, false);
    	}else if ( tipoFuente == TiposFuente.CSV ) {
    		mapaValores = origenCSVRepositorio.obtenerDatosTablaFuenteExterna(fuente, tabla, path, caracterSeparador, false);
    	} else if ( tipoFuente == TiposFuente.SHAPEFILE ) {
    		mapaValores = origenShapeFileRepositorio.obtenerDatosTablaFuenteExterna(fuente, tabla, path, false);
		} else if ( tipoFuente == TiposFuente.BDESPACIAL ) {
			if (esquema == null || esquema.isEmpty()){
				esquema = tabla.substring(0, tabla.indexOf("."));
				tabla = tabla.substring(esquema.length()+1, tabla.length());
			}
			mapaValores = origenPostGisRepositorio.obtenerDatosTablaFuenteExterna(fuente, esquema, tabla, false);
		} else if ( tipoFuente == TiposFuente.GML ) {
			mapaValores = origenGMLRepositorio.obtenerDatosTablaFuenteExterna(fuente, tabla, path, false);
		} else if ( tipoFuente == TiposFuente.WFS ) {
			mapaValores = origenWFSRepositorio.obtenerDatosTablaFuenteExterna(fuente, tabla, path, false);
		}
    	
    	if ( mapaValores == null || mapaValores.getContenido()==null ) {
    		AtributosMapDto mapa = new AtributosMapDto();
    		ValorFDDto valores = new ValorFDDto();
    		mapa.setValor(ERROR_PARSEO_FICHERO, valores);
    		return mapa;
    	} else	
    		return mapaValores;
    }
    
    public AtributosMapDto obtenerDatosTablaFuenteExternaCompletos(long id, String esquema, String tabla, String path, String caracterSeparador) {
    	//Cargar la fuente por id para sacar la cadena de conexion y conectarse y consultar.
    	FuenteDto fuenteDto = carga(id);
    	FuenteDto2FuenteTransformer transformer = new FuenteDto2FuenteTransformer(mapper, fuenteRepositorio);
    	Fuente fuente = transformer.transform(fuenteDto);
    	TiposFuente tipoFuente = fuente.getTipo();
    	
    	AtributosMapDto mapaValores = new AtributosMapDto();
    	
    	if ( tipoFuente == TiposFuente.ODBC ) {
    		mapaValores = origenODBCRepositorio.obtenerDatosTablaFuenteExterna(fuente, tabla, true);
    	}else if(tipoFuente==TiposFuente.MYSQL){
    		mapaValores=origenMysqlRepositorio.obtenerDatosTablaFuenteExterna(fuente, tabla, true);
    	}else if(tipoFuente==TiposFuente.ORACLE){
    		mapaValores=origenOracleRepositorio.obtenerDatosTablaFuenteExterna(fuente, tabla, true);
    	}else if ( tipoFuente == TiposFuente.CSV ) {
    		mapaValores = origenCSVRepositorio.obtenerDatosTablaFuenteExterna(fuente, tabla, path, caracterSeparador, true);
    	} else if ( tipoFuente == TiposFuente.SHAPEFILE ) {
    		mapaValores = origenShapeFileRepositorio.obtenerDatosTablaFuenteExterna(fuente, tabla, path, true);
		} else if ( tipoFuente == TiposFuente.BDESPACIAL ) {
			if (esquema == null || esquema.isEmpty()){
				esquema = tabla.substring(0, tabla.indexOf("."));
				tabla = tabla.substring(esquema.length()+1, tabla.length());
			}
			mapaValores = origenPostGisRepositorio.obtenerDatosTablaFuenteExterna(fuente, esquema, tabla, true);
		} else if ( tipoFuente == TiposFuente.GML ) {
			mapaValores = origenGMLRepositorio.obtenerDatosTablaFuenteExterna(fuente, tabla, path, true);
		} else if ( tipoFuente == TiposFuente.WFS ) {
			mapaValores = origenWFSRepositorio.obtenerDatosTablaFuenteExterna(fuente, tabla, path, true);
		}
    	
    	return mapaValores;
    }
    
    public AtributosMapDto obtenerMapaTablaFuenteExterna(long id, String esquema, String tabla, String path) {
    	//Cargar la fuente por id para sacar la cadena de conexion y conectarse y consultar.
    	FuenteDto fuenteDto = carga(id);
    	FuenteDto2FuenteTransformer transformer = new FuenteDto2FuenteTransformer(mapper, fuenteRepositorio);
    	Fuente fuente = transformer.transform(fuenteDto);
    	TiposFuente tipoFuente = fuente.getTipo();
    	
    	AtributosMapDto mapaValores = new AtributosMapDto();
    	
    	if ( tipoFuente == TiposFuente.SHAPEFILE ) {
    		mapaValores = origenShapeFileRepositorio.obtenerMapaTablaFuenteExterna(fuente, tabla, path);
		} else if ( tipoFuente == TiposFuente.BDESPACIAL ) {
			if (esquema == null || esquema.isEmpty()){
				esquema = tabla.substring(0, tabla.indexOf("."));
				tabla = tabla.substring(esquema.length()+1, tabla.length());
			}
			mapaValores = origenPostGisRepositorio.obtenerMapaTablaFuenteExterna(fuente, esquema, tabla);
		} else if ( tipoFuente == TiposFuente.GML ) {
			mapaValores = origenGMLRepositorio.obtenerMapaTablaFuenteExterna(fuente, tabla, path);
		} else if ( tipoFuente == TiposFuente.WFS ) {
			mapaValores = origenWFSRepositorio.obtenerMapaTablaFuenteExterna(fuente, tabla, path);
		}
    	
    	return mapaValores;
    }
    
    public EncapsuladorFileSW guardaFichero(EncapsuladorFileSW fich, String path, EncapsuladorErroresSW erros, boolean eliminarFicheros) {
    	//Primero borro los ficheros que pueda haber en la carpeta
    	String directorio = path+fich.getIdFuente();
    	if (eliminarFicheros) {
	    	if (!borrarConHijos(directorio,false)) {
	    		log.debug("El fichero/carpeta " + directorio + " no se ha podido borrar o vaciar");
	    		erros.addError(FuenteFormErrorsEmun.ERROR_ELIMINAR_FICHERO);
	    		return fich;
	    	}
    	}
    	
    	log.debug("Path = " + path + fich.getNombre());
        File f = new File(path+fich.getIdFuente()+"/"+fich.getNombre());
        f.getParentFile().mkdirs();
        
        InputStream inputStream = null;
        OutputStream out = null;
        
        try {
            inputStream = new ByteArrayInputStream(fich.getFich());
            out = new FileOutputStream(f);
            byte buf[] = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            inputStream.close();
        } catch (Exception ex) {
            log.error("Error al crear archivo en temporales", ex);
            erros.addError(FuenteFormErrorsEmun.ERROR_ALMACENAR_FICHERO);
            try {
				out.close();
				inputStream.close();
			} catch (Exception e) {
				log.debug("Error al cerrar el fichero");
				e.printStackTrace();
			}
            return fich;
        }
    	return fich;
    }
    
    private boolean borrarConHijos(String path, boolean borrarCarpeta) {  
        File file = new File(path);  
        if (!file.exists()) {  
            return true;  
        }  
        if (!file.isDirectory()) {  
            return file.delete();  
        }  
        file.setWritable(true);
        if ( borrarCarpeta ) return borrarHijos(file) && file.delete();
        else return borrarHijos(file);  
    }  
      
    private boolean borrarHijos(File dir) {  
        File[] hijos = dir.listFiles();  
        boolean hijosBorrados = true;  
        for (int i = 0; hijos != null && i < hijos.length; i++) {  
            File child = hijos[i];  
            if (child.isDirectory()) {  
                hijosBorrados = borrarHijos(child) && hijosBorrados;  
            }  
            if (child.exists()) {  
            	hijosBorrados = child.delete() && hijosBorrados;  
            }  
        }  
        return hijosBorrados;  
    }  
    
    public EncapsuladorBooleanSW probarFuente(long id, String path, String caracterSeparador) {
    	EncapsuladorBooleanSW res = new EncapsuladorBooleanSW();
    	boolean resBool = false;
    	
    	
    	FuenteDto fuenteDto = carga(id);
    	FuenteDto2FuenteTransformer transformer = new FuenteDto2FuenteTransformer(mapper, fuenteRepositorio);
    	Fuente fuente = transformer.transform(fuenteDto);
    	EncapsuladorListSW<AtributoFuenteDatosDto> listaColumnas = new EncapsuladorListSW<AtributoFuenteDatosDto>();
    	TiposFuente tipoFuente = fuente.getTipo();
    	
    	if ( tipoFuente == TiposFuente.ODBC ) {
    		resBool = origenODBCRepositorio.probarFuente(fuente);
    	}else if(tipoFuente==TiposFuente.MYSQL){
    		resBool=origenMysqlRepositorio.probarFuente(fuente);
    	}else if(tipoFuente==TiposFuente.ORACLE){
    		resBool=origenOracleRepositorio.probarFuente(fuente);
    	}else if ( tipoFuente == TiposFuente.CSV ) {
    		resBool = origenCSVRepositorio.probarFuente(fuente, path, caracterSeparador);
	    } else if ( tipoFuente == TiposFuente.SHAPEFILE ) {
	    	resBool = origenShapeFileRepositorio.probarFuente(fuente, path);
		} else if ( tipoFuente == TiposFuente.BDESPACIAL ) {
			resBool = origenPostGisRepositorio.probarFuente(fuente);
		} else if ( tipoFuente == TiposFuente.GML ) {
			resBool = origenGMLRepositorio.probarFuente(fuente, path);
		} else if ( tipoFuente == TiposFuente.WFS ) {
			resBool = origenWFSRepositorio.probarFuente(fuente, path);
		}
    	res.setValorLogico(resBool);
    	return res;
    }
    
    public Boolean cargaExisteFuenteConNombre(String nombre){
		 return fuenteRepositorio.cargaFuentePorNombre(nombre)!=null;
	}
    
    private Boolean cargaIndicadoresAsociadosFuente(Long idFuente){
		 return (fuenteRepositorio.cargaIndicadoresAsociadosFuente(idFuente)>0)?true:false;
	}
}