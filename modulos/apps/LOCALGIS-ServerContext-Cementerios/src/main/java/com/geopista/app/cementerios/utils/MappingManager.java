/**
 * MappingManager.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.cementerios.utils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.geopista.app.cementerios.Constantes;
import com.geopista.app.cementerios.business.dao.implementations.CementerioDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.CementerioFeatureDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.ConcesionDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.DatosFallecimientoDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.DifuntoDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.DocumentoDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.DocumentoTipoDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.ExhumacionDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.InhumacionDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.LocalizacionDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.PersonaDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.PlazaDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.RelTitularDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.TarifaDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.UnidadEnterramientoDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.elem_cementerioDAOImpl;
import com.geopista.app.cementerios.business.dao.interfaces.CementerioDAO;
import com.geopista.app.cementerios.business.dao.interfaces.CementerioFeatureDAO;
import com.geopista.app.cementerios.business.dao.interfaces.ConcesionDAO;
import com.geopista.app.cementerios.business.dao.interfaces.DatosFallecimientoDAO;
import com.geopista.app.cementerios.business.dao.interfaces.DifuntoDAO;
import com.geopista.app.cementerios.business.dao.interfaces.DocumentoDAO;
import com.geopista.app.cementerios.business.dao.interfaces.DocumentoTipoDAO;
import com.geopista.app.cementerios.business.dao.interfaces.ExhumacionDAO;
import com.geopista.app.cementerios.business.dao.interfaces.InhumacionDAO;
import com.geopista.app.cementerios.business.dao.interfaces.LocalizacionDAO;
import com.geopista.app.cementerios.business.dao.interfaces.PersonaDAO;
import com.geopista.app.cementerios.business.dao.interfaces.PlazaDAO;
import com.geopista.app.cementerios.business.dao.interfaces.RelTitularDAO;
import com.geopista.app.cementerios.business.dao.interfaces.TarifaDAO;
import com.geopista.app.cementerios.business.dao.interfaces.UnidadEnterramientoDAO;
import com.geopista.app.cementerios.business.dao.interfaces.elem_cementerioDAO;
import com.geopista.app.cementerios.business.vo.Bloque;
import com.geopista.app.cementerios.business.vo.Cementerio;
import com.geopista.app.cementerios.business.vo.CementerioFeatureKey;
import com.geopista.app.cementerios.business.vo.Concesion;
import com.geopista.app.cementerios.business.vo.DatosFallecimiento;
import com.geopista.app.cementerios.business.vo.Difunto;
import com.geopista.app.cementerios.business.vo.Documento;
import com.geopista.app.cementerios.business.vo.DocumentoTipo;
import com.geopista.app.cementerios.business.vo.DocumentoTipoExample;
import com.geopista.app.cementerios.business.vo.Exhumacion;
import com.geopista.app.cementerios.business.vo.HistoricoDifuntos;
import com.geopista.app.cementerios.business.vo.HistoricoPropiedad;
import com.geopista.app.cementerios.business.vo.Inhumacion;
import com.geopista.app.cementerios.business.vo.Intervencion;
import com.geopista.app.cementerios.business.vo.Localizacion;
import com.geopista.app.cementerios.business.vo.Persona;
import com.geopista.app.cementerios.business.vo.Plaza;
import com.geopista.app.cementerios.business.vo.PlazaExample;
import com.geopista.app.cementerios.business.vo.RelTitular;
import com.geopista.app.cementerios.business.vo.Tarifa;
import com.geopista.app.cementerios.business.vo.UnidadEnterramiento;
import com.geopista.app.cementerios.business.vo.elem_cementerio;
import com.geopista.app.cementerios.business.vo.elem_cementerioExample;
import com.geopista.protocol.cementerios.BloqueBean;
import com.geopista.protocol.cementerios.ConcesionBean;
import com.geopista.protocol.cementerios.Const;
import com.geopista.protocol.cementerios.DatosFallecimientoBean;
import com.geopista.protocol.cementerios.DifuntoBean;
import com.geopista.protocol.cementerios.ExhumacionBean;
import com.geopista.protocol.cementerios.HistoricoDifuntoBean;
import com.geopista.protocol.cementerios.HistoricoPropiedadBean;
import com.geopista.protocol.cementerios.InhumacionBean;
import com.geopista.protocol.cementerios.IntervencionBean;
import com.geopista.protocol.cementerios.LocalizacionBean;
import com.geopista.protocol.cementerios.PersonaBean;
import com.geopista.protocol.cementerios.PlazaBean;
import com.geopista.protocol.cementerios.TarifaBean;
import com.geopista.protocol.cementerios.UnidadEnterramientoBean;
import com.geopista.protocol.control.Sesion;
import com.geopista.protocol.document.DocumentBean;


/**
*
* Clase que contiene los métodos  que realizan el mapeo de Vo a Bean
*
*/
public class MappingManager {
	
    private CementerioDAO cementerioDAO;
    private UnidadEnterramientoDAO unidadEnterramientoDAO;
    private elem_cementerioDAO elemCementerioDAO;
    private LocalizacionDAO localizacionDAO;
    private CementerioFeatureDAO cementerioFeatureDAO;
    private PlazaDAO plazaDAO;
    private PersonaDAO personaDAO;
    private TarifaDAO tarifaDAO;
    private RelTitularDAO relTitularDAO;
    private DatosFallecimientoDAO datosFallecimientoDAO;
    private DocumentoDAO documentoDAO;
    private DocumentoTipoDAO documentoTipoDAO;
    private DifuntoDAO difuntoDAO;
    private InhumacionDAO inhumacionDAO;
    private ExhumacionDAO exhumacionDAO;
    private ConcesionDAO concesionDAO;
    
    private static MappingManager instance;
	
    public MappingManager(){
    	
        unidadEnterramientoDAO = new UnidadEnterramientoDAOImpl();
        elemCementerioDAO = new elem_cementerioDAOImpl();
        localizacionDAO = new LocalizacionDAOImpl();
        cementerioFeatureDAO = new CementerioFeatureDAOImpl();
        personaDAO = new PersonaDAOImpl();
        tarifaDAO = new TarifaDAOImpl();
        relTitularDAO = new RelTitularDAOImpl();
        plazaDAO = new PlazaDAOImpl();
        datosFallecimientoDAO = new DatosFallecimientoDAOImpl();
        cementerioDAO = new CementerioDAOImpl();
		documentoDAO = new DocumentoDAOImpl();
		documentoTipoDAO = new DocumentoTipoDAOImpl();
		difuntoDAO = new DifuntoDAOImpl();
		inhumacionDAO = new InhumacionDAOImpl();
		exhumacionDAO = new ExhumacionDAOImpl();
		concesionDAO = new ConcesionDAOImpl();
    }
    
    public static MappingManager getIntance(){
    	if(instance == null){
    		instance = new MappingManager();
    	}
    	return instance;
    }
    
    
 /*************************************************************** MAPEOS METHODS **************************************************************************/

	public BloqueBean mapBloqueVOToBean(Bloque bloque, Integer idCementerio) {
		
		BloqueBean alRet = null;
		try {
			elem_cementerioExample elemExample = new elem_cementerioExample();
			elemExample.createCriteria().andIdCementerioEqualTo(idCementerio)
					.andIdEqualTo(bloque.getIdElemcementerio());

			List elem_cementerioList = elemCementerioDAO
					.selectByExample(elemExample);

			if (elem_cementerioList.size() != 1) {
				return alRet;
			}
			// Se retorna una lista pero solo debe contener un elemento
			elem_cementerio elem = (elem_cementerio) elem_cementerioList.get(0);
			if (elem != null) {

				// Recuperamos el nombre del cementerio para hacer el set en la
				// unidad
				Cementerio cementerio = cementerioDAO.selectByPrimaryKey(idCementerio);

				BloqueBean bloqueBean = new BloqueBean();
				bloqueBean.setNombreCementerio(cementerio.getNombre());
				bloqueBean
						.setIdMunicipio(String.valueOf(elem.getIdMunicipio()));
				bloqueBean.setEntidad(elem.getEntidad());
				bloqueBean.setId(elem.getId());

				bloqueBean.setId_bloque(bloque.getId());

				bloqueBean.setDescripcion(bloque.getDescripcion());
				bloqueBean.setIdElemCementerio(bloque.getIdElemcementerio());
				bloqueBean.setId_feature(bloque.getIdFeature());
				bloqueBean.setNumColumnas(bloque.getNumColumnas());
				bloqueBean.setNumFilas(bloque.getNumFilas());
				bloqueBean.setTipo_unidades(bloque.getTipoBloque());

				alRet = bloqueBean;
			}

		} catch (Exception e) {

		}
		return alRet;
	}
	
    
  public UnidadEnterramientoBean mapUnidadEnterramientoVOToBean (UnidadEnterramiento unidadVO){

	UnidadEnterramientoBean unidadBean = new UnidadEnterramientoBean();

	try{
    	
    	List listaCementerioFeaturesKeys = new ArrayList<CementerioFeatureKey>();
    	
			unidadBean.setCodigo(unidadVO.getCodigo());
			unidadBean.setColumna(unidadVO.getColumna());
			unidadBean.setFila(unidadVO.getFila());
			unidadBean.setDescripcion(unidadVO.getDescripcion());
			unidadBean.setEstado(unidadVO.getEstado());
			unidadBean.setFecha_construccion(unidadVO.getFultConstruccion());
			unidadBean.setFecha_UltimaMod(unidadVO.getFultModificacion());
			unidadBean.setFecha_UltimaRef(unidadVO.getFreforma());
			unidadBean.setIdUe(unidadVO.getId());
			unidadBean.setTipo_unidad(unidadVO.getTipoUnidad());
			unidadBean.setNumMin_Plazas(unidadVO.getNumplazas());
 
			Localizacion localizacionVo;
				localizacionVo = localizacionDAO.selectByPrimaryKey(unidadVO.getIdLocalizacion());

				LocalizacionBean localizacion = new LocalizacionBean();
				localizacion.setId(unidadVO.getIdLocalizacion());
				localizacion.setDescripcion(localizacionVo.getDescripcion());
				localizacion.setValor(localizacionVo.getValor());
			unidadBean.setLocalizacion(localizacion);
			
	    	elem_cementerio elem = elemCementerioDAO.selectByPrimaryKey(unidadVO.getIdElemcementerio());
        	//Recuperamos el nombre del cementerio para hacer el set en la unidad
	    	cementerioDAO = new CementerioDAOImpl();
        	Cementerio cementerio = cementerioDAO.selectByPrimaryKey(elem.getIdCementerio());
	    	
	    	//Completamos los campos de cementerio que es el bean generico
	    	unidadBean.setIdMunicipio(String.valueOf(elem.getIdMunicipio()));
	    	unidadBean.setNombreCementerio(cementerio.getNombre());
	    	unidadBean.setEntidad(elem.getEntidad());
	    	unidadBean.setId(elem.getId());
	    	unidadBean.setTipo(elem.getTipo());
		    
    		//Plazas
			PlazaExample example = new PlazaExample();
			example.createCriteria().andIdUnidadenterramientoEqualTo(unidadVO.getId());
			List<Plaza> listaPlazasVO;
			plazaDAO = new PlazaDAOImpl();
			 listaPlazasVO = plazaDAO.selectByExample(example);
		    
    		ArrayList<PlazaBean> liataPlazasBean = new ArrayList<PlazaBean>();
    		Plaza plazaVO;
    		for (int j = 0; j < listaPlazasVO.size(); j++) {
				PlazaBean plazaBean = new PlazaBean();
				plazaVO = listaPlazasVO.get(j);
				
				if (plazaVO.getEstado() == Constantes.TRUE){
					plazaBean.setEstado(true);
				}else{
					plazaBean.setEstado(false);
				}
				plazaBean.setIdPlaza(plazaVO.getId());
				plazaBean.setIdUnidadEnterramiento((int)unidadBean.getIdUe());
				plazaBean.setModificado(plazaVO.getModicado());
				plazaBean.setDifunto(null);
				plazaBean.setSituacion(plazaVO.getSituacion());
				
				liataPlazasBean.add(plazaBean);
			}
    		unidadBean.setPlazas(liataPlazasBean);
    		
    		//getCementerio_feature where id unidad
    		cementerioFeatureDAO = new CementerioFeatureDAOImpl();
    		listaCementerioFeaturesKeys = cementerioFeatureDAO.selectByElem(unidadVO.getId());
    		List listafeatures = new ArrayList();
    		List listaLayers = new ArrayList();
    		for (int k = 0; k < listaCementerioFeaturesKeys.size(); k++) {
				CementerioFeatureKey keyElem = (CementerioFeatureKey) listaCementerioFeaturesKeys.get(k);
				listafeatures.add(keyElem.getIdFeature());
				listaLayers.add(keyElem.getIdLayer());
			}
    		
    		unidadBean.setIdFeatures(listafeatures);
    		unidadBean.setIdLayers(listaLayers);

    		
    		
	    }catch (Exception e) {
			
		}
	    return unidadBean;
    }
    
    
    /**
     * mapDifuntoVOToBean
     * @param difuntoVO
     * @return
     */
  public DifuntoBean mapDifuntoVOToBean (Difunto difuntoVO){
    
   DifuntoBean difunto = new DifuntoBean();	
    try{	
    	if (difuntoVO == null) return null;
    	
    
    	DatosFallecimiento datosFallecimientoVO = datosFallecimientoDAO.selectByPrimaryKey(difuntoVO.getIdDatosfallecimiento());
    	
    	if (datosFallecimientoVO != null){
    	
			DatosFallecimientoBean datosFallecimiento = new DatosFallecimientoBean();
			//Configuramos el VO
	
			datosFallecimiento.setCausa_fundamental(datosFallecimientoVO.getCausaFundamental());
	    	datosFallecimiento.setCausa_inmediata(datosFallecimientoVO.getCausaInmediata());
	   		datosFallecimiento.setFecha(datosFallecimientoVO.getFecha());
	   		datosFallecimiento.setLugar(datosFallecimientoVO.getLugar());
	   		datosFallecimiento.setMedico(datosFallecimientoVO.getMedico());
	   		datosFallecimiento.setNumColegiado(datosFallecimientoVO.getNumColegiado());
	   		datosFallecimiento.setPoblacion(datosFallecimientoVO.getPoblacion());
	   		datosFallecimiento.setId(datosFallecimientoVO.getId());
	   		datosFallecimiento.setReferencia_fallecimiento(datosFallecimientoVO.getReferencia());
	   		datosFallecimiento.setRegistro_civil(datosFallecimientoVO.getRegistroCivill());
	    		
	    	difunto.setDatosFallecimiento(datosFallecimiento);
	    	
    	}
    	//1.persona/ 
    	PersonaBean persona = new PersonaBean();

    	Persona personaVO = personaDAO.selectByPrimaryKey(difuntoVO.getDniPersona());

    	if (personaVO!= null ){
    		
        	persona.setApellido1(personaVO.getApellido1());
        	persona.setApellido2(personaVO.getApellido2());
        	persona.setDNI(personaVO.getDni());
        	persona.setDomicilio(personaVO.getDomicilio());
        	persona.setEstado_civil(personaVO.getEstadoCivil());
        	persona.setFecha_nacimiento(persona.getFecha_nacimiento());
        	persona.setNombre(personaVO.getNombre());
        	persona.setPoblacion(personaVO.getPoblacion());
        	persona.setSexo(personaVO.getSexo());
        	persona.setTelefono(personaVO.getTelefono());

        	difunto.setPersona(persona);
    	
    	}
      
    	difunto.setEdad(difuntoVO.getEdadDifunto());
    	difunto.setFecha_defuncion(difuntoVO.getFechaDefuncion());
    	difunto.setGrupo(difuntoVO.getGrupo());
    	difunto.setId_plaza(difuntoVO.getIdPlaza());
    	difunto.setId_difunto(difuntoVO.getId());
    	difunto.setCodigo(difuntoVO.getCodigo());

    }catch (Exception e) {
	} 	
	return difunto;
   }
 
  /**
   *   
   * @param concesionVO
   * @return
 * @throws SQLException 
   */
  public ConcesionBean mapConcesionVoToBean (Concesion concesionVO) throws SQLException{
	  
	  ConcesionBean concesionBean = new ConcesionBean();
	  
	  //1. Unidad de enterramiento a la que está asociada la concesión
	  UnidadEnterramiento unidadVO = unidadEnterramientoDAO.selectByPrimaryKey(concesionVO.getIdUnidad());
	  UnidadEnterramientoBean unidadBean = mapUnidadEnterramientoVOToBean(unidadVO);
	
	  //2.Mapeo de la persona/ titular principal  de la concesion -- tabla reltitular
	  
	  PersonaBean titular = new PersonaBean();
	  List cotitulares = new ArrayList();
	  
	  List listTitulares = relTitularDAO.selectByConcesion(concesionVO.getId());
	  RelTitular relTitularElem;
	  PersonaBean personaBean;
	  for (int i = 0; i < listTitulares.size(); i++) {
		  relTitularElem = (RelTitular) listTitulares.get(i);
		  Persona personaVO = personaDAO.selectByPrimaryKey(relTitularElem.getDniPersona());
		  personaBean = mapPersonaVoToBean(personaVO);
		  if (relTitularElem.getEsprincipal()){
			   titular = personaBean;
		  }else{
			  cotitulares.add(personaBean);
		  }
	  }
	  
	  TarifaBean tarifaBean = new TarifaBean();
	  Tarifa tarifaVo = tarifaDAO.selectByPrimaryKey(concesionVO.getIdTarifa());
	  tarifaBean = mapTarifaVoToBean(tarifaVo);
	  
	  concesionBean.setFecha_ini(concesionVO.getFechaIni());
	  concesionBean.setFecha_fin(concesionVO.getFechaFin());
	  concesionBean.setFecha_ultRenovacion(concesionVO.getUltimaRenova());
	  concesionBean.setId_concesion(concesionVO.getId());
	  concesionBean.setTipo_concesion(concesionVO.getTipoConcesion());
	  concesionBean.setEstado(concesionVO.getEstado());
	  concesionBean.setBonificacion(concesionVO.getBonificacion());
	  concesionBean.setPrecio_final(concesionVO.getPrecioFinal());
	  concesionBean.setDescripcion(concesionVO.getDescripcion());
		concesionBean.setLocalizacion(concesionVO.getLocalizado());
		concesionBean.setCodigo(concesionVO.getCodigo());
		concesionBean.setEstado(concesionVO.getEstado());
	  
	  concesionBean.setTarifa(tarifaBean);
	  concesionBean.setCotitulares(cotitulares);
	  concesionBean.setTitular(titular);
	  concesionBean.setUnidad(unidadBean);

	  //Se realiza el set de las propiedades comunes de elemcementerio
  	  elem_cementerio elem = elemCementerioDAO.selectByPrimaryKey(unidadVO.getIdElemcementerio());
  	  //Recuperamos el nombre del cementerio para hacer el set en la unidad
  	  cementerioDAO = new CementerioDAOImpl();
  	  Cementerio cementerio = cementerioDAO.selectByPrimaryKey(elem.getIdCementerio());
  	
  	  //Completamos los campos de cementerio que es el bean generico
  	  concesionBean.setIdMunicipio(String.valueOf(elem.getIdMunicipio()));
  	  concesionBean.setNombreCementerio(cementerio.getNombre());
  	  concesionBean.setEntidad(elem.getEntidad());
  	  concesionBean.setId(elem.getId());
  	  concesionBean.setTipo(elem.getTipo());	  
	  
	return concesionBean;
	  
  }
  
  /**
   * 
   * @param personaVo
   * @return
   */
  public PersonaBean mapPersonaVoToBean (Persona personaVo){
	  
	  PersonaBean personaBean = new PersonaBean();
	  
	  personaBean.setApellido1(personaVo.getApellido1());
	  personaBean.setApellido2(personaVo.getApellido2());
	  personaBean.setDNI(personaVo.getDni());
	  personaBean.setDomicilio(personaVo.getDomicilio());
	  personaBean.setEstado_civil(personaVo.getEstadoCivil());
	  personaBean.setFecha_nacimiento(personaVo.getFechaNacimiento());
	  personaBean.setNombre(personaVo.getNombre());
	  personaBean.setPoblacion(personaVo.getPoblacion());
	  personaBean.setSexo(personaVo.getSexo());
	  personaBean.setTelefono(personaVo.getTelefono());
	 
	  return  personaBean;
  }
  
  /**
   * 
   * @param tarifaVo
   * @return
   */
  public TarifaBean mapTarifaVoToBean (Tarifa tarifaVo){
	  
	    TarifaBean tarifaBean = new TarifaBean();
    	tarifaBean.setConcepto(tarifaVo.getConcepto());
    	tarifaBean.setTipo_tarifa(tarifaVo.getTipoTarifa());
    	tarifaBean.setPrecio(tarifaVo.getPrecio());
    	tarifaBean.setTipo_calculo(tarifaVo.getTipoCalculo());
    	tarifaBean.setTipo_unidad(tarifaVo.getCategoria());
    	tarifaBean.setId_tarifa(tarifaVo.getId());
	  return tarifaBean;
  }
  
  
	public String getMimeType(int tipo) throws Exception {
		
		String mime_type = null;
		DocumentoTipo tiposDocumento;

		tiposDocumento = documentoTipoDAO.selectByPrimaryKey((short)tipo);
		mime_type = tiposDocumento.getMimeType();
		
		return mime_type;
	}

	public String getExtensionFromMimeType(String mimetype) throws Exception {
		
		DocumentoTipoExample documentoTipoExample = new DocumentoTipoExample();
		documentoTipoExample.createCriteria().andMimeTypeEqualTo(mimetype);
		
		List listaDocumentosTipo = documentoTipoDAO.selectByExample(documentoTipoExample);

		String extension = ((DocumentoTipo)listaDocumentosTipo.get(0)).getExtension();
		
		return extension;
	}
  
	  public DocumentBean mapDocumentoVoToBean (Documento documentoVo) throws Exception{
		    
		  DocumentBean documentBean= null;
		  
		  documentBean= new DocumentBean();
		  
		  documentBean.setId(documentoVo.getIdDocumento().toString());
		  
		  documentBean.setFileName(documentoVo.getNombre());
		  documentBean.setSize(documentoVo.getTamanio());
		  documentBean.setTipo(getMimeType(((int)documentoVo.getTipo())));
		  documentBean.setFechaEntradaSistema(documentoVo.getFechaAlta());
		  documentBean.setFechaUltimaModificacion(documentoVo.getFechaModificacion());
		  documentBean.setComentario(documentoVo.getComentario());
		  documentBean.setPublico(documentoVo.getPublico());
		  documentBean.setOculto(documentoVo.getOculto());
		  documentBean.setIdMunicipio(String.valueOf(documentoVo.getIdMunicipio()));
		  documentBean.setIs_imgdoctext(documentoVo.getIsImgdoctext().charAt(0));
	      
		  documentBean.setThumbnail((byte[])documentoVo.getThumbnail());
	
		  return documentBean;
		  
	  }
  
	  public InhumacionBean mapInhumacionVOToBean (Inhumacion inhumacionVO) throws Exception{
		  
		  InhumacionBean inhumacion = new InhumacionBean();
		  
		  inhumacion.setBonificacion(inhumacionVO.getBonificacion());
		  inhumacion.setCodigo(inhumacionVO.getCodigo());
		  inhumacion.setFecha_inhumacion(inhumacionVO.getFechaInhumacion());
		  inhumacion.setInforme_inhumacion(inhumacionVO.getInforme());
		  inhumacion.setPrecio_final(inhumacionVO.getPrecioFinal());
		  inhumacion.setId_inhumacion(inhumacionVO.getId());
		  inhumacion.setTipo_contenedor(inhumacionVO.getContenedor());
		  inhumacion.setTipo_inhumacion(inhumacionVO.getTipoInhumacion());
		  
		  Tarifa tarifaVO = tarifaDAO.selectByPrimaryKey(inhumacionVO.getIdTarifa());
		  TarifaBean tarifa = mapTarifaVoToBean(tarifaVO);
		  inhumacion.setTarifa(tarifa);
		  
		return inhumacion;
		  
	  }

	  
	  public ExhumacionBean mapExhumacionVOToBean (Exhumacion exhumacionVO) throws Exception{
		  
		  ExhumacionBean exhumacion = new ExhumacionBean();
		  
		  exhumacion.setBonificacion(exhumacionVO.getBonificacion());
		  exhumacion.setCodigo(exhumacionVO.getCodigo());
		  exhumacion.setFecha_exhumacion(exhumacionVO.getFechaExhumacion());
		  exhumacion.setInforme_exhumacion(exhumacionVO.getInforme());
		  exhumacion.setPrecio_final(exhumacionVO.getPrecioFinal());
		  exhumacion.setId_exhumacion(exhumacionVO.getId());
		  exhumacion.setTipo_contenedor(exhumacionVO.getContenedor());
		  exhumacion.setTipo_exhumacion(exhumacionVO.getTipoExhumacion());
		  if (exhumacionVO.getRedRestos() == Constantes.TRUE){
			  exhumacion.setRed_restos(true);
		  }else{
			  exhumacion.setRed_restos(false);
		  }
		  
		  if (exhumacionVO.getTraslado() == Constantes.TRUE){
			  exhumacion.setTraslado(true);
		  }else{
			  exhumacion.setTraslado(false);
		  }
		  
		  Tarifa tarifaVO = tarifaDAO.selectByPrimaryKey(exhumacionVO.getIdTarifa());
		  TarifaBean tarifa = mapTarifaVoToBean(tarifaVO);
		  exhumacion.setTarifa(tarifa);
		  
		return exhumacion;
		  
	  }	  
	  
	  /**
	   * 
	   * @param historicoDifuntosVO
	   * @return
	   * @throws Exception
	   */
	  public HistoricoDifuntoBean mapHistoricoVOToBean (HistoricoDifuntos historicoDifuntosVO, Integer idCementerio) throws Exception{
		  
		  DifuntoBean difunto = null;
		  
		  HistoricoDifuntoBean historico = new HistoricoDifuntoBean();
		  historico.setComentario(historicoDifuntosVO.getComentario());
		  historico.setFechaOperacion(historicoDifuntosVO.getFechaOperacion());
		  historico.setId_elem(historicoDifuntosVO.getIdElem());
		  historico.setId_historico(historicoDifuntosVO.getId());
		  historico.setTipo(String.valueOf(historicoDifuntosVO.getTipo()));
		  
		  Difunto difuntoVO = difuntoDAO.selectByPrimaryKey(historicoDifuntosVO.getIdDifunto());
		  
			//obtengo la plaza donde esta el difunto para obtener la unidad y comprobar si esta o no en el cementerio
			Plaza plaza = plazaDAO.selectByPrimaryKey(difuntoVO.getIdPlaza());
			
			UnidadEnterramiento unidad = unidadEnterramientoDAO.selectByPrimaryKey(plaza.getIdUnidadenterramiento());
			UnidadEnterramientoBean unidadBean = mapUnidadEnterramientoVOToBean(unidad);
			
	    	elem_cementerioExample elemExample = new  elem_cementerioExample();
	    	elemExample.createCriteria().andIdCementerioEqualTo(idCementerio).andIdEqualTo(unidad.getIdElemcementerio());
	    	
	    	List elem_cementerioList =  elemCementerioDAO.selectByExample(elemExample);

			// Se retorna una lista pero solo debe contener un elemento
			elem_cementerio elem = (elem_cementerio) elem_cementerioList.get(0);
			if (elem != null) {
				// Recuperamos el nombre del cementerio para hacer el set en
				// la unidad
				Cementerio cementerio = cementerioDAO.selectByPrimaryKey(idCementerio);

				// si esta correcto me compongo el bean difunto y lo meto en la hash
				difunto = mapDifuntoVOToBean(difuntoVO);
				
				difunto.setIdMunicipio(String.valueOf(elem.getIdMunicipio()));
				difunto.setNombreCementerio(elem.getNombre());
				difunto.setEntidad(elem.getEntidad());
				difunto.setId(elem.getId());
				difunto.setTipo(elem.getTipo());
			
				difunto.setIdFeatures(unidadBean.getIdFeatures());
				difunto.setIdLayers(unidadBean.getIdLayers());
			}	

		  historico.setDifunto(difunto);

		  historico.setIdMunicipio(String.valueOf(difunto.getIdMunicipio()));
		  historico.setEntidad(difunto.getEntidad());
		  historico.setId((int)difunto.getId());
			
		  if (String.valueOf(historicoDifuntosVO.getTipo()).equalsIgnoreCase(Const.PATRON_INHUMACION)){
			  Inhumacion inhumacionVO = inhumacionDAO.selectByPrimaryKey(historicoDifuntosVO.getIdElem());
			  InhumacionBean inhumacion = mapInhumacionVOToBean(inhumacionVO);
			  historico.setElem(inhumacion);
			  historico.setTipoStr("Inhumación");
		  }else if (String.valueOf(historicoDifuntosVO.getTipo()).equalsIgnoreCase(Const.PATRON_EXHUMACION)){
			  Exhumacion exhumacionVO = exhumacionDAO.selectByPrimaryKey(historicoDifuntosVO.getIdElem());
			  ExhumacionBean exhumacion = mapExhumacionVOToBean(exhumacionVO);
			  historico.setElem(exhumacion);
			  historico.setTipoStr("Exhumación");
		  }
		  return historico;
	  }
	  
	  
		public Collection getConcesionesByTitular(String dni_persona,
				Sesion userSesion) throws Exception {

			List<Concesion> listaConcesiones;
			HashMap alRet = new HashMap();

			ArrayList<RelTitular> relTitularList = (ArrayList<RelTitular>) relTitularDAO.selectByTitular(dni_persona);
			
			for (int i = 0; i < relTitularList.size(); i++) {
				RelTitular elem = relTitularList.get(i);
				Concesion concesion = concesionDAO.selectByPrimaryKey(elem.getIdConcesion());
				if (concesion!= null){
					ConcesionBean concesionBean = mapConcesionVoToBean(concesion);
							alRet.put(concesionBean.getId_concesion(), concesionBean);
				}
			}
			return alRet.values();
		}
	  
	  /**
	   * 
	   * @param historicoDifuntosVO
	   * @return
	   * @throws Exception
	   */
	  public HistoricoPropiedadBean mapHistoricoPropVOToBean (HistoricoPropiedad historicoPropiedadVO, Integer idCementerio) throws Exception{
		  
		  HistoricoPropiedadBean historico = new HistoricoPropiedadBean();
		  historico.setComentario(historicoPropiedadVO.getComentario());
		  historico.setFechaOperacion(historicoPropiedadVO.getFechaOperacion());
		  historico.setId_elem(historicoPropiedadVO.getIdElem());
		  historico.setId_historico(historicoPropiedadVO.getId());
		  historico.setTipo(String.valueOf(historicoPropiedadVO.getTipo()));
		  
		  Collection colConcesiones = getConcesionesByTitular(historicoPropiedadVO.getDniTitular(), null);
		  Object[] arrayElems = colConcesiones.toArray();
		  int n = arrayElems.length;
		  ArrayList listaConcesiones = new ArrayList();
		  ConcesionBean concesion;
		    for (int k = 0; k < arrayElems.length; k++) {
		    	concesion = (ConcesionBean) arrayElems[k];
		    	
		    	//TODO
				/** Unidad de enterramiento**/
				UnidadEnterramiento unidadVO = unidadEnterramientoDAO.selectByPrimaryKey((int)concesion.getUnidad().getIdUe());
				UnidadEnterramientoBean unidadBean = mapUnidadEnterramientoVOToBean(unidadVO);
				concesion.setUnidad(unidadBean);

				// Completamos los campos de cementerio que es el bean generico
				elem_cementerioExample elemExample = new elem_cementerioExample();
				elemExample.createCriteria()
						.andIdCementerioEqualTo(idCementerio)
						.andIdEqualTo(unidadVO.getIdElemcementerio());

				List elem_cementerioList = elemCementerioDAO.selectByExample(elemExample);

				if (elem_cementerioList.size() != 1) {
					continue;
				}
				// Se retorna una lista pero solo debe contener un elemento
				elem_cementerio elem = (elem_cementerio) elem_cementerioList.get(0);
				if (elem != null) {
					// Recuperamos el nombre del cementerio para hacer el set en
					// la unidad
					Cementerio cementerio = cementerioDAO.selectByPrimaryKey(idCementerio);
					
					PersonaBean titular = concesion.getTitular();
					
					titular.setIdMunicipio(String.valueOf(elem.getIdMunicipio()));
					titular.setNombreCementerio(elem.getNombre());
					titular.setEntidad(elem.getEntidad());
					titular.setId(elem.getId());
					titular.setTipo(elem.getTipo());

					titular.setIdFeatures(unidadBean.getIdFeatures());
					titular.setIdLayers(unidadBean.getIdLayers());
					
					
		    	
				}
		    	
		    	//FIn TODO
				historico.setIdMunicipio(String.valueOf(concesion.getIdMunicipio()));
				historico.setEntidad(concesion.getEntidad());
				historico.setNombreCementerio(concesion.getNombreCementerio());
				historico.setId((int)concesion.getId());
				historico.setElem(concesion);
				historico.setTipoStr("Concesión");
		    }
		  return historico;
	  }
	  
	  /**
	   * 
	   * @param intervencionVO
	   * @return
	   * @throws Exception
	   */
	  public IntervencionBean mapIntervencionVOToBean (Intervencion intervencionVO) throws Exception{
		  
		  IntervencionBean  intervencion = new IntervencionBean();
		  intervencion.setCodigo(intervencionVO.getCodigo());
		  intervencion.setEstado(intervencionVO.getEstado());
		  intervencion.setFechaInicio(intervencionVO.getFechaInicio());
		  intervencion.setFechaFin(intervencionVO.getFechaFin());
		  intervencion.setInforme(intervencionVO.getInforme());
		  intervencion.setId_intervencion(intervencionVO.getId());
		  intervencion.setLocalizacion(intervencionVO.getLocalizacion());
		  
		  return intervencion;
	  }
	  
}
