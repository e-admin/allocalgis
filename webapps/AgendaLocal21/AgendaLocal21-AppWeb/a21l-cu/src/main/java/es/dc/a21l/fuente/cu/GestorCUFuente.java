/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.fuente.cu;

import java.util.List;
import java.util.Map;

import es.dc.a21l.base.modelo.GestionErrores.EncapsuladorErroresSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorBooleanSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorFileSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorListSW;

/**
 *
 * @author Balidea Consulting & Programming
 */
public interface GestorCUFuente {
    public List<String> listarTablasCatalogoInterno();
    public Map<String,List<AtributoFuenteDatosDto>> obtenerDatosTablaCatalogoInterno();
    public Map<AtributoFuenteDatosDto,DescripcionAtributoDto> obtenerEsquemaTablaCatalogoInterno(String tabla);
    public Map<String,List<AtributoFuenteDatosDto>> obtenerMapaTablaCatalogoInterno(String tabla);
    public List<String> listarColumnasTablaCatalogoInterno(String tabla);
    public List<FuenteDto> listaFuentesExternas();
    public List<FuenteDto> listaFuentes();
    public EncapsuladorFileSW guardaFichero(EncapsuladorFileSW envioFich, String path, EncapsuladorErroresSW erros, boolean eliminarFicheros);
    
    public EncapsuladorListSW<TablaFuenteDatosDto> listarTablasFuenteExterna(long id,String path);
    public EncapsuladorListSW<AtributoFuenteDatosDto> listarColumnasTablaFuenteExterna(long id, String tabla, String path, String caracterSeparador);
    public EncapsuladorListSW<DescripcionAtributoDto> obtenerEsquemaTablaFuenteExterna(long id, String esquema, String tabla, String path, String caracterSeparador);
    public AtributosMapDto obtenerDatosTablaFuenteExterna(long id, String esquema, String tabla, String path, String caracterSeparador);
    public AtributosMapDto obtenerDatosTablaFuenteExternaCompletos(long id, String esquema, String tabla, String path, String caracterSeparador);
    public AtributosMapDto obtenerMapaTablaFuenteExterna(long id, String esquema, String tabla, String path);
        
	public EncapsuladorListSW<FuenteDto> cargaTodos();
	public EncapsuladorListSW<FuenteDto> listarFuentesExternas() ;
    public EncapsuladorListSW<FuenteDto> listarFuentesInternas() ;
    public FuenteDto carga(long id);
    public boolean existe(long id);
    public EncapsuladorBooleanSW probarFuente(long id, String path, String caracterSeparador);
    public FuenteDto garda(FuenteDto fuenteDto, Long idUsuarioCreador, EncapsuladorErroresSW erros,String sistemaCoordenadas);
    public FuenteDto borra(long id, String path, EncapsuladorErroresSW erros);
    public FuenteDto existeFuenteCatalogo(FuenteDto fuenteDto);
    public FuenteDto gardaFuenteCatalogo(FuenteDto fuenteDto, Long idUsuarioCreador);
}
