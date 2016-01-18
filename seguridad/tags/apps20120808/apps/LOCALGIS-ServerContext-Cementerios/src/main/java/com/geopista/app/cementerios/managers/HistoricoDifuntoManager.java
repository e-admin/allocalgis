package com.geopista.app.cementerios.managers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.geopista.app.cementerios.business.dao.DAO;
import com.geopista.app.cementerios.business.dao.implementations.DatosFallecimientoDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.DifuntoDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.ElemFeatureDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.HistoricoDifuntosDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.PersonaDAOImpl;
import com.geopista.app.cementerios.business.dao.interfaces.DatosFallecimientoDAO;
import com.geopista.app.cementerios.business.dao.interfaces.DifuntoDAO;
import com.geopista.app.cementerios.business.dao.interfaces.ElemFeatureDAO;
import com.geopista.app.cementerios.business.dao.interfaces.HistoricoDifuntosDAO;
import com.geopista.app.cementerios.business.dao.interfaces.PersonaDAO;
import com.geopista.app.cementerios.business.vo.Cementerio;
import com.geopista.app.cementerios.business.vo.DatosFallecimiento;
import com.geopista.app.cementerios.business.vo.DatosFallecimientoExample;
import com.geopista.app.cementerios.business.vo.Difunto;
import com.geopista.app.cementerios.business.vo.DifuntoExample;
import com.geopista.app.cementerios.business.vo.HistoricoDifuntos;
import com.geopista.app.cementerios.business.vo.HistoricoDifuntosExample;
import com.geopista.app.cementerios.business.vo.Inhumacion;
import com.geopista.app.cementerios.business.vo.InhumacionExample;
import com.geopista.app.cementerios.business.vo.Persona;
import com.geopista.app.cementerios.business.vo.PersonaExample;
import com.geopista.app.cementerios.business.vo.Plaza;
import com.geopista.app.cementerios.business.vo.UnidadEnterramiento;
import com.geopista.app.cementerios.business.vo.elem_cementerio;
import com.geopista.app.cementerios.business.vo.elem_cementerioExample;
import com.geopista.app.cementerios.utils.AddFilter;
import com.geopista.app.cementerios.utils.MappingManager;
import com.geopista.protocol.cementerios.CampoFiltro;
import com.geopista.protocol.cementerios.DifuntoBean;
import com.geopista.protocol.cementerios.HistoricoDifuntoBean;
import com.geopista.protocol.cementerios.InhumacionBean;
import com.geopista.protocol.cementerios.UnidadEnterramientoBean;
import com.geopista.protocol.control.Sesion;

public class HistoricoDifuntoManager extends DAO {

	private org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ExhumacionManager.class);
	
	private static HistoricoDifuntoManager instance;
	private HistoricoDifuntosDAO historicoDAO;
	private PersonaDAO personaDAO;
	private DifuntoDAO difuntoDAO;
    private DatosFallecimientoDAO datosFallecimientoDAO;
    private MappingManager mappingManager;
    

    
    public HistoricoDifuntoManager(){
        
    	historicoDAO = new HistoricoDifuntosDAOImpl();
    	personaDAO = new PersonaDAOImpl();
    	difuntoDAO = new DifuntoDAOImpl();
   	    datosFallecimientoDAO = new DatosFallecimientoDAOImpl();
        mappingManager = MappingManager.getIntance();
        
    }
    
    public static HistoricoDifuntoManager getInstance(){
    	if (instance == null){
    		instance = new HistoricoDifuntoManager();
    	}
    	return instance;
    }
		
	
	/**************************************************************** HistoricoDifuntoManager ****************************************************************************/
 
	public Collection getHistoricosByDifunto(String superpatron, String patron,Object[] filtro, Object[] idLayers, Object[] idFeatures,
			Integer idCementerio,DifuntoBean difunto,  Sesion userSesion) throws Exception {

		HashMap alRet = new HashMap();
		
		List listaHistoricos = historicoDAO.selectByDifunto(difunto.getId_difunto());
		
		HistoricoDifuntos historicoDifuntoVO;

		for (int i = 0; i < listaHistoricos.size(); i++) {
			historicoDifuntoVO = (HistoricoDifuntos) listaHistoricos.get(i);
			
			if (historicoDifuntoVO!= null){
				HistoricoDifuntoBean historico = mappingManager.mapHistoricoVOToBean(historicoDifuntoVO, idCementerio);
				
				Collection c = new ArrayList(); 
				for (int j = 0; j < idFeatures.length; j++) {
					c.add(idFeatures[j]);
				}
				historico.setIdFeatures(c);
				c.clear();
				for (int j = 0; j < idLayers.length; j++) {
					c.add(idLayers[j]);
				}
				historico.setIdLayers(c);
				alRet.put(historico.getId_historico(), historico);
			}
		}
		
	return alRet.values();
	}
	
	
    private boolean perteneceDifuntoListapersonas (Difunto difunto, List listaPersonas){
    	boolean pertenece = false;
		for (int i = 0; i < listaPersonas.size(); i++) {
			Persona personaElem = (Persona) listaPersonas.get(i);
			if (difunto.getDniPersona().equalsIgnoreCase(personaElem.getDni())){
				pertenece = true;
				break;
			}
		}
		return pertenece;	
    }
    
    private boolean perteneceDifuntoListaDatosFallecimiento (Difunto difunto, List listaDatosFallecimiento){
    	boolean pertenece = false;
		for (int i = 0; i < listaDatosFallecimiento.size(); i++) {
			DatosFallecimiento datosFallecimientoElem = (DatosFallecimiento) listaDatosFallecimiento.get(i);
			if (difunto.getIdDatosfallecimiento().intValue() == difunto.getIdDatosfallecimiento().intValue()){ 
				pertenece = true;
				break;
			}
		}
		return pertenece;	
    	
    }

    
    private boolean perteneceDifuntoListaDifuntos (Difunto difunto, List listaDifuntos){
    	boolean pertenece = false;
		for (int i = 0; i < listaDifuntos.size(); i++) {
			Difunto difuntoElem = (Difunto) listaDifuntos.get(i);
			if (difunto.getDniPersona().equalsIgnoreCase(difuntoElem.getDniPersona())){ 
				pertenece = true;
				break;
			}
		}
		return pertenece;	
    	
    }
	
	/**
	 * getHistoricoByFilter
	 * @param superpatron
	 * @param patron
	 * @param filtro
	 * @param idCementerio
	 * @param userSesion
	 * @return
	 * @throws Exception
	 */
	public Collection getHistoricoByFilter(String superpatron,String patron, Object[] filtro, Integer idCementerio,
			Sesion userSesion) throws Exception {

		List<HistoricoDifuntos> listaHistoricos;
		List<Persona> listaPersonas = null;
		List<DatosFallecimiento> listaDatosFallecimiento = null;
		List<Difunto> listaDifuntos = null;
		HashMap alRet = new HashMap();
		AddFilter addFilter = AddFilter.getInstance();

		HistoricoDifuntosExample historicoExample = new HistoricoDifuntosExample();
		if (filtro != null) {
			CampoFiltro campoFiltro;
			com.geopista.app.cementerios.business.vo.HistoricoDifuntosExample.Criteria criteria = historicoExample.createCriteria();
			
			for (int i = 0; i < filtro.length; i++) {
				campoFiltro = (CampoFiltro) filtro[i];
				
				if (campoFiltro.getValorId() != null){
					String filter = campoFiltro.getValorId();
					if (filter.equalsIgnoreCase("persona")){
						PersonaExample personaExample = new PersonaExample();
						com.geopista.app.cementerios.business.vo.PersonaExample.Criteria personaCriteria = personaExample.createCriteria();
						personaCriteria = addFilter.addPersonaFilter(personaCriteria, campoFiltro, false);
						
						personaExample.or(personaCriteria);
						listaPersonas = personaDAO.selectByExample(personaExample);
					}
					else if (filter.equalsIgnoreCase("datosfallecimiento")){
						DatosFallecimientoExample datosFallecimientoExample = new DatosFallecimientoExample();
						com.geopista.app.cementerios.business.vo.DatosFallecimientoExample.Criteria datosFallecimientoCriteria =
							datosFallecimientoExample.createCriteria();
						datosFallecimientoCriteria = addFilter.addDatosFallecimientoFilter(datosFallecimientoCriteria, campoFiltro, false);
						datosFallecimientoExample.or(datosFallecimientoCriteria);
						listaDatosFallecimiento = datosFallecimientoDAO.selectByExample(datosFallecimientoExample);
					}					
					else if (filter.equalsIgnoreCase("difunto")){
						DifuntoExample difuntoExample = new DifuntoExample();
						com.geopista.app.cementerios.business.vo.DifuntoExample.Criteria difuntoCriteria = difuntoExample.createCriteria();
						difuntoCriteria = addFilter.addDifuntoFilter(difuntoCriteria, campoFiltro, false);
						
						difuntoExample.or(difuntoCriteria);
						listaDifuntos = difuntoDAO.selectByExample(difuntoExample);
					}					
				}else{
					criteria = (com.geopista.app.cementerios.business.vo.HistoricoDifuntosExample.Criteria) 
					addFilter.addHistoricoDifuntoFilter(criteria, campoFiltro, false);
				}					
			}
			historicoExample.or(criteria);
			listaHistoricos = historicoDAO.selectByExample(historicoExample);

			for (int j = 0; j < listaHistoricos.size(); j++) {
				HistoricoDifuntos historico = (HistoricoDifuntos) listaHistoricos.get(j);
				
				Difunto difuntoVO = difuntoDAO.selectByPrimaryKey(historico.getIdDifunto());
				if (listaPersonas != null){
					if (!perteneceDifuntoListapersonas(difuntoVO, listaPersonas)){
						continue;
					}
				}
				if (listaDatosFallecimiento != null){
					if (!perteneceDifuntoListaDatosFallecimiento(difuntoVO, listaDatosFallecimiento)){
						continue;
					}
				}
				if (listaDifuntos != null){
					if (!perteneceDifuntoListaDifuntos(difuntoVO, listaDifuntos)){
						continue;
					}
				}
				
				HistoricoDifuntoBean historicoBean = mappingManager.mapHistoricoVOToBean(historico, idCementerio);
				
				alRet.put(historicoBean.getId_historico(), historicoBean);
			}
		}

		return alRet.values();
	}	
	
	/**
	 * findHistorico
	 * @param superpatron
	 * @param patron
	 * @param filtro
	 * @param idCementerio
	 * @param userSesion
	 * @return
	 * @throws Exception
	 */
	public Collection findHistorico(String superpatron,String patron, Object[] filtro, Integer idCementerio,
			Sesion userSesion) throws Exception {

		List<HistoricoDifuntos> listaHistoricos = null;
		HashMap alRet = new HashMap();
		AddFilter addFilter = AddFilter.getInstance();

		HistoricoDifuntosExample historicoExample = new HistoricoDifuntosExample();
		if (filtro != null) {
			CampoFiltro campoFiltro;
			com.geopista.app.cementerios.business.vo.HistoricoDifuntosExample.Criteria criteria = historicoExample.createCriteria();
			
			for (int i = 0; i < filtro.length; i++) {
				campoFiltro = (CampoFiltro) filtro[i];
				criteria = (com.geopista.app.cementerios.business.vo.HistoricoDifuntosExample.Criteria) 
				addFilter.addHistoricoDifuntoFilter(criteria, campoFiltro, false);
			
				historicoExample.or(criteria);
				listaHistoricos = historicoDAO.selectByExample(historicoExample);

				if ((listaHistoricos != null) && (listaHistoricos.size()>0)) break; 
				historicoExample.clear();

			}
			for (int j = 0; j < listaHistoricos.size(); j++) {
				HistoricoDifuntos historico = (HistoricoDifuntos) listaHistoricos.get(j);
				HistoricoDifuntoBean historicoBean = mappingManager.mapHistoricoVOToBean(historico, idCementerio);
				alRet.put(historicoBean.getId_historico(), historicoBean);
			}
		}

		return alRet.values();
	}
	
    
}