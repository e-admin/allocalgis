/**
 * HistoricoPropiedadManager.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.cementerios.managers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.geopista.app.cementerios.business.dao.DAO;
import com.geopista.app.cementerios.business.dao.implementations.ConcesionDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.DatosFallecimientoDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.HistoricoPropiedadDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.PersonaDAOImpl;
import com.geopista.app.cementerios.business.dao.interfaces.ConcesionDAO;
import com.geopista.app.cementerios.business.dao.interfaces.DatosFallecimientoDAO;
import com.geopista.app.cementerios.business.dao.interfaces.HistoricoPropiedadDAO;
import com.geopista.app.cementerios.business.dao.interfaces.PersonaDAO;
import com.geopista.app.cementerios.business.vo.Concesion;
import com.geopista.app.cementerios.business.vo.ConcesionExample;
import com.geopista.app.cementerios.business.vo.HistoricoPropiedad;
import com.geopista.app.cementerios.business.vo.HistoricoPropiedadExample;
import com.geopista.app.cementerios.business.vo.Persona;
import com.geopista.app.cementerios.business.vo.PersonaExample;
import com.geopista.app.cementerios.utils.AddFilter;
import com.geopista.app.cementerios.utils.MappingManager;
import com.geopista.protocol.cementerios.CampoFiltro;
import com.geopista.protocol.cementerios.ConcesionBean;
import com.geopista.protocol.cementerios.HistoricoPropiedadBean;
import com.geopista.protocol.cementerios.PersonaBean;
import com.geopista.protocol.control.Sesion;

public class HistoricoPropiedadManager extends DAO {

	private org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(HistoricoPropiedadManager.class);
	
	private static HistoricoPropiedadManager instance;
	private HistoricoPropiedadDAO historicoDAO;
	private PersonaDAO personaDAO;
    private DatosFallecimientoDAO datosFallecimientoDAO;
    private ConcesionDAO concesionDAO;
    private MappingManager mappingManager;
    private ConcesionManager concesionManager;

    
    public HistoricoPropiedadManager(){
        
    	historicoDAO = new HistoricoPropiedadDAOImpl();
    	personaDAO = new PersonaDAOImpl();
   	    datosFallecimientoDAO = new DatosFallecimientoDAOImpl();
   	    concesionDAO = new ConcesionDAOImpl();
        mappingManager = MappingManager.getIntance();
        concesionManager = ConcesionManager.getInstance();
        
    }
    
    public static HistoricoPropiedadManager getInstance(){
    	if (instance == null){
    		instance = new HistoricoPropiedadManager();
    	}
    	return instance;
    }
		
	
	/**************************************************************** HistoricoDifuntoManager ****************************************************************************/
 
	public Collection getHistoricosByTitular(String superpatron, String patron,Object[] filtro, Object[] idLayers, Object[] idFeatures,
			Integer idCementerio,PersonaBean titular,  Sesion userSesion) throws Exception {

		HashMap alRet = new HashMap();
		
		List listaHistoricos = historicoDAO.selectByTitular(titular.getDNI());
		
		HistoricoPropiedad historicoPropiedadVO;

		for (int i = 0; i < listaHistoricos.size(); i++) {
			historicoPropiedadVO = (HistoricoPropiedad) listaHistoricos.get(i);
			
			if (historicoPropiedadVO!= null){
				
				  HistoricoPropiedadBean historico = new HistoricoPropiedadBean();
				  historico.setComentario(historicoPropiedadVO.getComentario());
				  historico.setFechaOperacion(historicoPropiedadVO.getFechaOperacion());
				  historico.setId_elem(historicoPropiedadVO.getIdElem());
				  historico.setId_historico(historicoPropiedadVO.getId());
				  historico.setTipo(String.valueOf(historicoPropiedadVO.getTipo()));
				
				  Collection colConcesiones = concesionManager.getConcesionesByTitular(titular.getDNI(), userSesion);
				  Object[] arrayElems = colConcesiones.toArray();
				  int n = arrayElems.length;
				  ArrayList listaConcesiones = new ArrayList();
				  ConcesionBean concesion;
				    for (int k = 0; k < arrayElems.length; k++) {
				    	concesion = (ConcesionBean) arrayElems[k];
						historico.setIdMunicipio(String.valueOf(concesion.getIdMunicipio()));
						historico.setEntidad(concesion.getEntidad());
						historico.setId((int)concesion.getId());
						historico.setElem(concesion);
						historico.setTipoStr("Concesión");
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
		}	
		
	return alRet.values();
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
//	public Collection getHistoricoByFilter(String superpatron,String patron, Object[] filtro, Integer idCementerio,
//			Sesion userSesion) throws Exception {
//
//		List<HistoricoPropiedad> listaHistoricos;
//		HashMap alRet = new HashMap();
//		AddFilter addFilter = AddFilter.getInstance();
//
//		HistoricoPropiedadExample historicoExample = new HistoricoPropiedadExample();
//		if (filtro != null) {
//			CampoFiltro campoFiltro;
//			com.geopista.app.cementerios.business.vo.HistoricoPropiedadExample.Criteria criteria = historicoExample.createCriteria();
//			
//			for (int i = 0; i < filtro.length; i++) {
//				campoFiltro = (CampoFiltro) filtro[i];
//				criteria = (com.geopista.app.cementerios.business.vo.HistoricoPropiedadExample.Criteria) 
//				addFilter.addHistoricoPropiedadFilter(criteria, campoFiltro, false);
//			}
//			historicoExample.or(criteria);
//			listaHistoricos = historicoDAO.selectByExample(historicoExample);
//
//			for (int j = 0; j < listaHistoricos.size(); j++) {
//				HistoricoPropiedad historico = (HistoricoPropiedad) listaHistoricos.get(j);
//				HistoricoPropiedadBean historicoBean = mappingManager.mapHistoricoPropVOToBean(historico);
//				alRet.put(historicoBean.getId_historico(), historicoBean);
//			}
//		}
//
//		return alRet.values();
//	}	
	
    private List joinConcesionesHistoricos(List listaConcesiones, List listaHistoricosTemp){
    	
    	List<HistoricoPropiedad> listaHistorico= new ArrayList<HistoricoPropiedad>();
    	for (int j = 0; j < listaHistoricosTemp.size(); j++) {
    		HistoricoPropiedad historicoElem = (HistoricoPropiedad) listaHistoricosTemp.get(j);
			for (int i = 0; i < listaConcesiones.size(); i++) {
				Concesion concesionElem = (Concesion) listaConcesiones.get(i);
				if (concesionElem.getId().intValue() == historicoElem.getIdElem().intValue()){
					listaHistorico.add(historicoElem);
					break;
				}
			}
		}
    	return listaHistorico;
    }
    
    private List joinPersonasHistoricos(List listaPersonas, List listaHistoricosTemp){
    	
    	List<HistoricoPropiedad> listaHistorico= new ArrayList<HistoricoPropiedad>();
    	for (int j = 0; j < listaHistoricosTemp.size(); j++) {
    		HistoricoPropiedad historicoElem = (HistoricoPropiedad) listaHistoricosTemp.get(j);
			for (int i = 0; i < listaPersonas.size(); i++) {
				Persona personaElem = (Persona) listaPersonas.get(i);
				if (personaElem.getDni().equalsIgnoreCase(historicoElem.getDniTitular())){
					listaHistorico.add(historicoElem);
					break;
				}
			}
    	}
    	return listaHistorico;
    }
	
    /**
     * 
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

		List<HistoricoPropiedad> listaHistoricos = null;
		List<HistoricoPropiedad> listaHistoricosAux = null;
		List<Persona> listaPersonas = null;
		List<HistoricoPropiedad> listaHistoricosTemp;
		
		List<Concesion> listaConcesiones = null;
		HashMap alRet = new HashMap();
		AddFilter addFilter = AddFilter.getInstance();

		HistoricoPropiedadExample historicoExample = new HistoricoPropiedadExample();
		if (filtro != null) {
			CampoFiltro campoFiltro;
			com.geopista.app.cementerios.business.vo.HistoricoPropiedadExample.Criteria criteria = historicoExample.createCriteria();
			
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
					else if (filter.equalsIgnoreCase("concesion")){
						ConcesionExample concesionExample = new ConcesionExample();
						com.geopista.app.cementerios.business.vo.ConcesionExample.Criteria concesionCriteria = concesionExample.createCriteria();
						concesionCriteria = addFilter.addConcesionFilter(concesionCriteria, campoFiltro, false);
						
						concesionExample.or(concesionCriteria);
						listaConcesiones = concesionDAO.selectByExample(concesionExample);
					}					
				}else{
					criteria = (com.geopista.app.cementerios.business.vo.HistoricoPropiedadExample.Criteria) 
					addFilter.addHistoricoPropiedadFilter(criteria, campoFiltro, false);
				}
			}
			historicoExample.or(criteria);
			listaHistoricosTemp = historicoDAO.selectByExample(historicoExample);

			/**se hace el join de las listas obtenidas de las diferentes tablas tras aplicar los filtros correspondientes**/
			
			if ((listaConcesiones!= null) || (listaPersonas!= null)){
				if (listaConcesiones != null){
					listaHistoricosAux = joinConcesionesHistoricos(listaConcesiones, listaHistoricosTemp);
				}
				if (listaPersonas != null){
					if (listaHistoricosAux!= null) listaHistoricosTemp= listaHistoricosAux;	
					listaHistoricosAux = joinPersonasHistoricos(listaPersonas, listaHistoricosTemp);
				}
				listaHistoricos = listaHistoricosAux; 
			}
			else{
				listaHistoricos = listaHistoricosTemp;
			}
			
			
			for (int j = 0; j < listaHistoricos.size(); j++) {
				HistoricoPropiedad historico = (HistoricoPropiedad) listaHistoricos.get(j);
				
				HistoricoPropiedadBean historicoBean = mappingManager.mapHistoricoPropVOToBean(historico,idCementerio);
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

		List<HistoricoPropiedad> listaHistoricos = null;
		HashMap alRet = new HashMap();
		AddFilter addFilter = AddFilter.getInstance();

		HistoricoPropiedadExample historicoExample = new HistoricoPropiedadExample();
		if (filtro != null) {
			CampoFiltro campoFiltro;
			com.geopista.app.cementerios.business.vo.HistoricoPropiedadExample.Criteria criteria = historicoExample.createCriteriaInternal();
			
			for (int i = 0; i < filtro.length; i++) {
				campoFiltro = (CampoFiltro) filtro[i];
				criteria = (com.geopista.app.cementerios.business.vo.HistoricoPropiedadExample.Criteria) 
				addFilter.addHistoricoPropiedadFilter(criteria, campoFiltro, false);
			
				if (criteria == null) continue;	
				historicoExample.or(criteria);
				listaHistoricos = historicoDAO.selectByExample(historicoExample);

				if ((listaHistoricos != null) && (listaHistoricos.size()>0)) break; 
				historicoExample.clear();

			}
			
			if (listaHistoricos!=null){
				for (int j = 0; j < listaHistoricos.size(); j++) {
					HistoricoPropiedad historico = (HistoricoPropiedad) listaHistoricos.get(j);
					HistoricoPropiedadBean historicoBean = mappingManager.mapHistoricoPropVOToBean(historico, idCementerio);
					alRet.put(historicoBean.getId_historico(), historicoBean);
				}
			}
		}

		return alRet.values();
	}
	
    
}