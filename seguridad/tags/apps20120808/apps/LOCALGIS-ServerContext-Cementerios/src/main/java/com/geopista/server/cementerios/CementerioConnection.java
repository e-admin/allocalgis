package com.geopista.server.cementerios;

import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import com.geopista.app.cementerios.Constantes;
import com.geopista.app.cementerios.business.dao.DAO;
import com.geopista.app.cementerios.business.dao.implementations.CementerioDAOImpl;
import com.geopista.app.cementerios.business.dao.implementations.UnidadEnterramientoDAOImpl;
import com.geopista.app.cementerios.business.dao.interfaces.CementerioDAO;
import com.geopista.app.cementerios.business.dao.interfaces.UnidadEnterramientoDAO;
import com.geopista.app.cementerios.business.vo.Cementerio;
import com.geopista.app.cementerios.business.vo.UnidadEnterramiento;
import com.geopista.app.cementerios.managers.BloqueManager;
import com.geopista.app.cementerios.managers.ComunManager;
import com.geopista.app.cementerios.managers.ConcesionManager;
import com.geopista.app.cementerios.managers.DifuntoManager;
import com.geopista.app.cementerios.managers.ExhumacionManager;
import com.geopista.app.cementerios.managers.HistoricoDifuntoManager;
import com.geopista.app.cementerios.managers.HistoricoPropiedadManager;
import com.geopista.app.cementerios.managers.InhumacionManager;
import com.geopista.app.cementerios.managers.IntervencionManager;
import com.geopista.app.cementerios.managers.PlazaManager;
import com.geopista.app.cementerios.managers.TarifaManager;
import com.geopista.app.cementerios.managers.TitularManager;
import com.geopista.app.cementerios.managers.UnidadEnterramientoManager;
import com.geopista.protocol.cementerios.BloqueBean;
import com.geopista.protocol.cementerios.CampoFiltro;
import com.geopista.protocol.cementerios.CementerioBean;
import com.geopista.protocol.cementerios.ConcesionBean;
import com.geopista.protocol.cementerios.Const;
import com.geopista.protocol.cementerios.DifuntoBean;
import com.geopista.protocol.cementerios.ErrorBean;
import com.geopista.protocol.cementerios.ExhumacionBean;
import com.geopista.protocol.cementerios.HistoricoDifuntoBean;
import com.geopista.protocol.cementerios.HistoricoPropiedadBean;
import com.geopista.protocol.cementerios.InhumacionBean;
import com.geopista.protocol.cementerios.IntervencionBean;
import com.geopista.protocol.cementerios.PersonaBean;
import com.geopista.protocol.cementerios.PlazaBean;
import com.geopista.protocol.cementerios.TarifaBean;
import com.geopista.protocol.cementerios.UnidadEnterramientoBean;
import com.geopista.protocol.control.Sesion;
import com.geopista.server.administradorCartografia.ACException;
import com.geopista.server.administradorCartografia.NewSRID;
import com.geopista.server.database.CPoolDatabase;

public class CementerioConnection extends DAO{
	
    private org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(CementerioConnection.class);
    private NewSRID srid;
    private String municipio;
    private Vector docsBorrados;

    //YR:cementerio
    private CementerioDAO cementerioDAO;
    private UnidadEnterramientoDAO unidadEnterramientoDAO;

    private UnidadEnterramientoManager unidadManager;
    private BloqueManager bloqueManager;
    private PlazaManager plazaManager;
    private ConcesionManager concesionManager;
    private DifuntoManager difuntoManager;
    private IntervencionManager intervencionManager;
    private TitularManager titularManager;
    private TarifaManager tarifaManager;
    private ComunManager comunManager;
    private InhumacionManager inhumacionManager;
    private ExhumacionManager exhumacionManager;
    private HistoricoDifuntoManager historicoDifuntoMnager;
    private HistoricoPropiedadManager historicoPropiedadManager;
    
    public CementerioConnection(String municipio){
    	this.municipio=municipio;
    }

    public CementerioConnection(NewSRID srid, String municipio){
    	this(municipio);
        this.srid=srid;
        
       unidadEnterramientoDAO = new UnidadEnterramientoDAOImpl();
       cementerioDAO = new CementerioDAOImpl();
       
	   unidadManager = UnidadEnterramientoManager.getInstance();
	   bloqueManager = BloqueManager.getInstance();
	   plazaManager = PlazaManager.getInstance();
	   concesionManager = ConcesionManager.getInstance();
	   difuntoManager = DifuntoManager.getInstance();
	   intervencionManager = IntervencionManager.getInstance();
	   titularManager = TitularManager.getInstance();
	   comunManager = ComunManager.getInstance();
	   tarifaManager = TarifaManager.getInstance();
	   inhumacionManager = InhumacionManager.getInstance();
	   exhumacionManager = ExhumacionManager.getInstance();
	   historicoDifuntoMnager = HistoricoDifuntoManager.getInstance();
	   historicoPropiedadManager = HistoricoPropiedadManager.getInstance();
    }

	public void setSRID(NewSRID srid){
        this.srid=srid;
    }



    /**
     * Devuelve la hora de base de datos
     * @return
     */
    public void getHora(ObjectOutputStream oos) throws Exception{
    	Connection conn = null;
    	PreparedStatement ps = null;
    	ResultSet rs = null;
    	String hora = "";
    	try{
    		conn = CPoolDatabase.getConnection();
    		String sSQL = "SELECT TO_CHAR(localtimestamp, 'HH24:MI:ss')";
    		ps = conn.prepareStatement(sSQL);
    		rs = ps.executeQuery();
    		if (rs.next())
    			hora = rs.getString(1);
    	}catch(Exception e){
            throw e;    		
	    }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();}catch(Exception e){};
            oos.writeObject(hora);
	    }
    }


   /***********************************************************************************************************************/
    
    public void returnAllElemsByType(ObjectOutputStream oos, String superpatron, String patron, String cadena, Integer idCementerio, Sesion userSesion) throws Exception{
    	
    	try{
            
    		if (superpatron.equalsIgnoreCase(Const.SUPERPATRON_GELEMENTOS)){
    			if (patron.equalsIgnoreCase(Const.PATRON_UENTERRAMIENTO)){
    				for (Iterator it=unidadManager.getAllUnidadesEnterramientoByCementerio(idCementerio).iterator();it.hasNext();){;
    					oos.writeObject((UnidadEnterramientoBean)it.next());
    				}
    			}else
    				for (Iterator it=bloqueManager.getAllBloquesByCementerio(idCementerio).iterator();it.hasNext();){;
					oos.writeObject((BloqueBean)it.next());
				}
    		}
    		if (superpatron.equalsIgnoreCase(Const.SUPERPATRON_GPROPIEDAD)){
    			if (patron.equalsIgnoreCase(Const.PATRON_CONCESION)){
    				for (Iterator it=concesionManager.getAllConcesionesByCementerio(idCementerio).iterator();it.hasNext();){;
    					oos.writeObject((ConcesionBean)it.next());
    				}
    			}else if (patron.equalsIgnoreCase(Const.PATRON_TITULAR)){
    				for (Iterator it=titularManager.getAllTitularesByCementerio(idCementerio).iterator();it.hasNext();){;
					oos.writeObject((PersonaBean)it.next());}
    			}
    			else if (patron.equalsIgnoreCase(Const.PATRON_TARIFASPROP)){
    				for (Iterator it=tarifaManager.getAllTarifasByCementerioAndType(idCementerio, Constantes.TARIFA_GPROPIEDAD).iterator();it.hasNext();){;
					oos.writeObject((TarifaBean)it.next());}
    			}
    		}
    		if (superpatron.equalsIgnoreCase(Const.SUPERPATRON_GDIFUNTOS)){
    			if (patron.equalsIgnoreCase(Const.PATRON_DEFUNCION)){
    				for (Iterator it=difuntoManager.getAllDifuntosByCemetery(idCementerio).iterator();it.hasNext();){;
    					oos.writeObject((DifuntoBean)it.next());
    				}
    			}
    			else if (patron.equalsIgnoreCase(Const.PATRON_TARIFASDEF)){
    				for (Iterator it=tarifaManager.getAllTarifasByCementerioAndType(idCementerio, Constantes.TARIFA_GDIFUNTOS).iterator();it.hasNext();){;
					oos.writeObject((TarifaBean)it.next());}
    			}
    			else if (patron.equalsIgnoreCase(Const.PATRON_INHUMACION)){
    				for (Iterator it=inhumacionManager.getAllInhumacionesByCemetery(idCementerio).iterator();it.hasNext();){;
					oos.writeObject((InhumacionBean)it.next());}
    			}
    			else if (patron.equalsIgnoreCase(Const.PATRON_EXHUMACION)){
    				for (Iterator it=exhumacionManager.getAllExhumacionesByCemetery(idCementerio).iterator();it.hasNext();){;
					oos.writeObject((ExhumacionBean)it.next());}
    			}
    		}
    		if (superpatron.equalsIgnoreCase(Const.SUPERPATRON_GHISTORICOS)){
    			if (patron.equalsIgnoreCase(Const.PATRON_HISTORICO_DIFUNTO)){
    				for (Iterator it=difuntoManager.getAllDifuntosByCemetery(idCementerio).iterator();it.hasNext();){;
					oos.writeObject((DifuntoBean)it.next());}
    			}
    			else if (patron.equalsIgnoreCase(Const.PATRON_HISTORICO_PROPIEDAD)){
    				for (Iterator it=titularManager.getAllTitularesByCementerio(idCementerio).iterator();it.hasNext();){;
					oos.writeObject((PersonaBean)it.next());}
    			}
    		}
    		if (superpatron.equalsIgnoreCase(Const.SUPERPATRON_GINTERVENCIONES)){
    			if (patron.equalsIgnoreCase(Const.PATRON_INTERVENCION)){
    				for (Iterator it=intervencionManager.getAllIntervencionesByCementerio(idCementerio).iterator();it.hasNext();){;
					oos.writeObject((IntervencionBean)it.next());}
    			}
    		}
    		
    		
    	}catch(Exception e){
            logger.error("returnUnidades: "+ e.getMessage());
            oos.writeObject(new ACException(e));
            throw e;
        }
    }
    
    /**
     * returnElemByType
     * @param oos
     * @param superpatron
     * @param patron
     * @param cadena
     * @param filtro
     * @param idLayers
     * @param idFeatures
     * @param userSesion
     * @throws Exception
     */
    public void returnElemByType(ObjectOutputStream oos, String superpatron, String patron, String cadena, Object[] filtro, Object[] idLayers, Object[] idFeatures,Integer idCementerio, Sesion userSesion) throws Exception{
    	
    	try{
            
    		if (superpatron.equalsIgnoreCase(Const.SUPERPATRON_GELEMENTOS)){
    			if (patron.equalsIgnoreCase(Const.PATRON_UENTERRAMIENTO)){
    	            UnidadEnterramientoBean unidad = unidadManager.getUnidad(superpatron, patron,  filtro, idLayers, idFeatures, idCementerio,userSesion);
                    oos.writeObject(unidad);
    			}else{
    	            BloqueBean bloque = bloqueManager.getBloque(superpatron, patron,  filtro, idLayers, idFeatures,idCementerio, userSesion);
                    oos.writeObject(bloque);
    			}
    		}
    		if (superpatron.equalsIgnoreCase(Const.SUPERPATRON_GPROPIEDAD)){
    			if (patron.equalsIgnoreCase(Const.PATRON_TARIFASDEF)){
    				//TODO hacer tarifas
    			}else{
    				 ConcesionBean concesion = concesionManager.getConcesion(superpatron, patron,  filtro, idLayers, idFeatures, idCementerio,  userSesion);
    				 if (patron.equalsIgnoreCase(Const.PATRON_CONCESION)){
    	                oos.writeObject(concesion);
    				 }else if (patron.equalsIgnoreCase(Const.PATRON_TITULAR)){
    					 if(concesion!= null){
	    					 PersonaBean titular = (PersonaBean) titularManager.getTitularPrincipalByConcesion((int)concesion.getId_concesion());
	    					 oos.writeObject(titular);
	    					 }
    					 }
    				}
    		}
    	}catch(Exception e){
            logger.error("returnUnidades: "+ e.getMessage());
            oos.writeObject(new ACException(e));
            throw e;
        }
    }

    public void returnElemsByTypeAndFeature(ObjectOutputStream oos, String superpatron, String patron, String cadena, Object[] filtro, Object[] idLayers, Object[] idFeatures,Integer idCementerio, Sesion userSesion) throws Exception{
    	
    	try{

    		if (superpatron.equalsIgnoreCase(Const.SUPERPATRON_GELEMENTOS)){
    			if (patron.equalsIgnoreCase(Const.PATRON_UENTERRAMIENTO)){
    				for (Iterator it=unidadManager.getUnidadesByFeature(superpatron, patron,  filtro, idLayers, idFeatures, idCementerio,userSesion).iterator();it.hasNext();){;
					oos.writeObject((UnidadEnterramientoBean)it.next());
    				}
    			}else{
    	            BloqueBean bloque = bloqueManager.getBloque(superpatron, patron,  filtro, idLayers, idFeatures,idCementerio, userSesion);
                    oos.writeObject(bloque);
    			}
    		}
    		if (superpatron.equalsIgnoreCase(Const.SUPERPATRON_GPROPIEDAD)){
    			if (patron.equalsIgnoreCase(Const.PATRON_TARIFASPROP)){
    				for (Iterator it=tarifaManager.getAllTarifasByCementerioAndType(idCementerio,Constantes.TARIFA_GPROPIEDAD).iterator();it.hasNext();){;
					oos.writeObject((TarifaBean)it.next());}
    			}else{
    				ConcesionBean concesion = null;
    				for (Iterator it=concesionManager.getConcesionesByFeature(superpatron, patron,  filtro, idLayers, idFeatures, idCementerio,  userSesion).iterator();it.hasNext();){;
    					concesion =(ConcesionBean)it.next(); 
	   				 	if (patron.equalsIgnoreCase(Const.PATRON_CONCESION)){
	   				 		oos.writeObject(concesion);
	   				 	}else if (patron.equalsIgnoreCase(Const.PATRON_TITULAR)){
	   				 		if(concesion!= null){
	   				 			PersonaBean titular = (PersonaBean) titularManager.getTitularPrincipalByConcesion((int)concesion.getId_concesion());
	   				 			oos.writeObject(titular);
	    					 }
						 }
    				   }
    				}
    			}
    		if (superpatron.equalsIgnoreCase(Const.SUPERPATRON_GDIFUNTOS)){
    			if (patron.equalsIgnoreCase(Const.PATRON_DEFUNCION)){
    				for (Iterator it=difuntoManager.getDifuntosByFeature(superpatron, patron,  filtro, idLayers, idFeatures, idCementerio,  userSesion).iterator();it.hasNext();){;
    					oos.writeObject((DifuntoBean)it.next());
    				}
    			}else if (patron.equalsIgnoreCase(Const.PATRON_TARIFASDEF)){
    				for (Iterator it=tarifaManager.getAllTarifasByCementerioAndType(idCementerio,Constantes.TARIFA_GDIFUNTOS).iterator();it.hasNext();){;
					oos.writeObject((TarifaBean)it.next());}
    			}
    			else if (patron.equalsIgnoreCase(Const.PATRON_INHUMACION)){
					for (Iterator it=inhumacionManager.getInhumacionesByFeature(superpatron, patron, filtro, idLayers, idFeatures, idCementerio, userSesion).iterator();it.hasNext();){;
					oos.writeObject((InhumacionBean)it.next());}
    			}
    			else if (patron.equalsIgnoreCase(Const.PATRON_EXHUMACION)){
					for (Iterator it=exhumacionManager.getExhumacionesByFeature(superpatron, patron, filtro, idLayers, idFeatures, idCementerio, userSesion).iterator();it.hasNext();){;
					oos.writeObject((ExhumacionBean)it.next());}
    			}
    		}
    		if (superpatron.equalsIgnoreCase(Const.SUPERPATRON_GHISTORICOS)){
    			if (patron.equalsIgnoreCase(Const.PATRON_HISTORICO_DIFUNTO)){
    				for (Iterator it=difuntoManager.getDifuntosByFeature(superpatron, patron,  filtro, idLayers, idFeatures, idCementerio,  userSesion).iterator();it.hasNext();){;
    					oos.writeObject((DifuntoBean)it.next());}
    			}
    			else if (patron.equalsIgnoreCase(Const.PATRON_HISTORICO_PROPIEDAD)){
    				ConcesionBean concesion = null;
    				for (Iterator it=concesionManager.getConcesionesByFeature(superpatron, patron,  filtro, idLayers, idFeatures, idCementerio,  userSesion).iterator();it.hasNext();){;
    					concesion =(ConcesionBean)it.next(); 
	   				 		if(concesion!= null){
	   				 			PersonaBean titular = (PersonaBean) titularManager.getTitularPrincipalByConcesion((int)concesion.getId_concesion());
	   				 			oos.writeObject(titular);
	    					 }
						 }
    				}
    			}
    		if (superpatron.equalsIgnoreCase(Const.SUPERPATRON_GINTERVENCIONES)){
    			if (patron.equalsIgnoreCase(Const.PATRON_INTERVENCION)){
    				for (Iterator it=intervencionManager.getIntervencionesByFeature(superpatron, patron,  filtro, idLayers, idFeatures, idCementerio,  userSesion).iterator();it.hasNext();){;
    					oos.writeObject((IntervencionBean)it.next());}
    			}
    		}
    		
    				
    	}catch(Exception e){
            logger.error("returnUnidades: "+ e.getMessage());
            oos.writeObject(new ACException(e));
            throw e;
        }
    }
    
    
    public void returnFilterElem(ObjectOutputStream oos, String superpatron, String patron, String cadena, Object[] filtro, Object[] idLayers,
    		Object[] idFeatures,Integer idCementerio, Sesion userSesion) throws Exception{
    	
    	try{
    		
    		if (superpatron.equalsIgnoreCase(Const.SUPERPATRON_GELEMENTOS)){
    			if (patron.equalsIgnoreCase(Const.PATRON_UENTERRAMIENTO)){
    				for (Iterator it=unidadManager.getUnidadesByFilter(superpatron, patron,  filtro, idCementerio,userSesion).iterator();it.hasNext();){;
					oos.writeObject((UnidadEnterramientoBean)it.next());
				}
    			}else{
    				for (Iterator it=bloqueManager.getBloquesByFilter(superpatron, patron,  filtro, idCementerio,userSesion).iterator();it.hasNext();){;
					oos.writeObject((BloqueBean)it.next());}
    			}
    		}
    		if (superpatron.equalsIgnoreCase(Const.SUPERPATRON_GPROPIEDAD)){
    			if (patron.equalsIgnoreCase(Const.PATRON_CONCESION)){
    				for (Iterator it=concesionManager.getConcesionesByFilter(superpatron, patron,  filtro, idCementerio,userSesion).iterator();it.hasNext();){;
					oos.writeObject((ConcesionBean)it.next());}
    			}else if (patron.equalsIgnoreCase(Const.PATRON_TITULAR)){
    				for (Iterator it=titularManager.getTitularesByFilter(superpatron, patron,  filtro, idCementerio,userSesion).iterator();it.hasNext();){;
					oos.writeObject((PersonaBean)it.next());}
    			}
    			else if (patron.equalsIgnoreCase(Const.PATRON_TARIFASPROP)){
    				for (Iterator it=tarifaManager.getTarifasPropiedadByFilter(superpatron, patron,  filtro, idCementerio, Constantes.TARIFA_GPROPIEDAD, userSesion).iterator();it.hasNext();){;
						oos.writeObject((TarifaBean)it.next());}
				}   
    		}
    		if (superpatron.equalsIgnoreCase(Const.SUPERPATRON_GDIFUNTOS)){
    			if (patron.equalsIgnoreCase(Const.PATRON_DEFUNCION)){
    				for (Iterator it=difuntoManager.getDifuntosByFilter(superpatron, patron,  filtro, idCementerio,  userSesion).iterator();it.hasNext();){;
    					oos.writeObject((DifuntoBean)it.next());}
    			}
    			else if (patron.equalsIgnoreCase(Const.PATRON_INHUMACION)){
    				for (Iterator it=inhumacionManager.getInhumacionesByFilter(superpatron, patron,  filtro, idCementerio,  userSesion).iterator();it.hasNext();){;
						oos.writeObject((InhumacionBean)it.next());}
				}
    			else if (patron.equalsIgnoreCase(Const.PATRON_EXHUMACION)){
    				for (Iterator it=exhumacionManager.getExhumacionesByFilter(superpatron, patron,  filtro, idCementerio,  userSesion).iterator();it.hasNext();){;
						oos.writeObject((ExhumacionBean)it.next());}
				}
    			else if (patron.equalsIgnoreCase(Const.PATRON_TARIFASDEF)){
    				for (Iterator it=tarifaManager.getTarifasDifuntoByFilter(superpatron, patron,  filtro, idCementerio,Constantes.TARIFA_GDIFUNTOS, userSesion).iterator();it.hasNext();){;
						oos.writeObject((TarifaBean)it.next());}
				}    
    		}
    		if (superpatron.equalsIgnoreCase(Const.SUPERPATRON_GHISTORICOS)){
    			if (patron.equalsIgnoreCase(Const.PATRON_HISTORICO_DIFUNTO)){
    				for (Iterator it=historicoDifuntoMnager.getHistoricoByFilter(superpatron, patron,  filtro, idCementerio,  userSesion).iterator();it.hasNext();){;
					oos.writeObject((HistoricoDifuntoBean)it.next());}
    			}
    			else if (patron.equalsIgnoreCase(Const.PATRON_HISTORICO_PROPIEDAD)){
    				for (Iterator it=historicoPropiedadManager.getHistoricoByFilter(superpatron, patron,  filtro, idCementerio,  userSesion).iterator();it.hasNext();){;
					oos.writeObject((HistoricoPropiedadBean)it.next());}
    			}
    			
			}
    		if (superpatron.equalsIgnoreCase(Const.SUPERPATRON_GINTERVENCIONES)){
    			if (patron.equalsIgnoreCase(Const.PATRON_INTERVENCION)){
    				for (Iterator it=intervencionManager.getIntervencionesByFilter(superpatron, patron,  filtro, idCementerio,  userSesion).iterator();it.hasNext();){;
					oos.writeObject((IntervencionBean)it.next());}
    			}
			}
    		
    		
    	}catch(Exception e){
            logger.error("returnUnidades: "+ e.getMessage());
            oos.writeObject(new ACException(e));
            throw e;
        }
    }
    
    public void returnFindElem(ObjectOutputStream oos, String superpatron, String patron, String cadena, Object[] filtro, Object[] idLayers,
    		Object[] idFeatures,Integer idCementerio, Sesion userSesion) throws Exception{
    	
    	try{
    		
    		if (superpatron.equalsIgnoreCase(Const.SUPERPATRON_GELEMENTOS)){
    			if (patron.equalsIgnoreCase(Const.PATRON_UENTERRAMIENTO)){
    				for (Iterator it=unidadManager.findUnidades(superpatron, patron,  filtro, idCementerio,userSesion).iterator();it.hasNext();){;
					oos.writeObject((UnidadEnterramientoBean)it.next());
				}
    			}else{
    				for (Iterator it=bloqueManager.findBloques(superpatron, patron,  filtro, idCementerio,userSesion).iterator();it.hasNext();){;
					oos.writeObject((BloqueBean)it.next());}
    			}
    		}
    		if (superpatron.equalsIgnoreCase(Const.SUPERPATRON_GPROPIEDAD)){
    			if (patron.equalsIgnoreCase(Const.PATRON_CONCESION)){
    				for (Iterator it=concesionManager.findConcesiones(superpatron, patron,  filtro, idCementerio,userSesion).iterator();it.hasNext();){;
					oos.writeObject((ConcesionBean)it.next());}
    			}else if (patron.equalsIgnoreCase(Const.PATRON_TITULAR)){
    				for (Iterator it=titularManager.findTitulares(superpatron, patron,  filtro, idCementerio,userSesion).iterator();it.hasNext();){;
					oos.writeObject((PersonaBean)it.next());}
    			}
    			else if (patron.equalsIgnoreCase(Const.PATRON_TARIFASPROP)){
    				for (Iterator it=tarifaManager.findTarifasPropiedad(superpatron, patron,  filtro, idCementerio, Constantes.TARIFA_GPROPIEDAD, userSesion).iterator();it.hasNext();){;
						oos.writeObject((TarifaBean)it.next());}
				}   
    		}
    		if (superpatron.equalsIgnoreCase(Const.SUPERPATRON_GDIFUNTOS)){
    			if (patron.equalsIgnoreCase(Const.PATRON_DEFUNCION)){
    				for (Iterator it=difuntoManager.findDifuntos(superpatron, patron,  filtro, idCementerio,  userSesion).iterator();it.hasNext();){;
					oos.writeObject((DifuntoBean)it.next());}
				}
    			else if (patron.equalsIgnoreCase(Const.PATRON_TARIFASDEF)){
    				for (Iterator it=tarifaManager.findTarifasDifunto(superpatron, patron,  filtro, idCementerio, Constantes.TARIFA_GPROPIEDAD, userSesion).iterator();it.hasNext();){;
						oos.writeObject((TarifaBean)it.next());}
				}
    			else if (patron.equalsIgnoreCase(Const.PATRON_INHUMACION)){
    				for (Iterator it=inhumacionManager.findInhumaciones(superpatron, patron,  filtro, idCementerio,  userSesion).iterator();it.hasNext();){;
						oos.writeObject((InhumacionBean)it.next());}
				}
    			else if (patron.equalsIgnoreCase(Const.PATRON_EXHUMACION)){
    				for (Iterator it=exhumacionManager.findExhumaciones(superpatron, patron,  filtro, idCementerio,  userSesion).iterator();it.hasNext();){;
						oos.writeObject((ExhumacionBean)it.next());}
				}
    		}
    		if (superpatron.equalsIgnoreCase(Const.SUPERPATRON_GHISTORICOS)){
    			if (patron.equalsIgnoreCase(Const.PATRON_HISTORICO_DIFUNTO)){
    				for (Iterator it=historicoDifuntoMnager.findHistorico(superpatron, patron,  filtro, idCementerio,  userSesion).iterator();it.hasNext();){;
					oos.writeObject((HistoricoDifuntoBean)it.next());}
    			}
			}
    		if (superpatron.equalsIgnoreCase(Const.SUPERPATRON_GINTERVENCIONES)){
    			if (patron.equalsIgnoreCase(Const.PATRON_INTERVENCION)){
    				for (Iterator it=intervencionManager.findIntervenciones(superpatron, patron,  filtro, idCementerio,  userSesion).iterator();it.hasNext();){;
					oos.writeObject((IntervencionBean)it.next());}
    			}
			}
    		
    		}catch(Exception e){
            logger.error("returnUnidades: "+ e.getMessage());
            oos.writeObject(new ACException(e));
            throw e;
        }
    }
    
    public void returnPlaza(ObjectOutputStream oos, Long id,  Sesion userSesion) throws Exception{
        try{
           PlazaBean plaza = plazaManager.getPlaza(id, userSesion);
                oos.writeObject(plaza);
        }catch(Exception e){
            logger.error("returnplaza: "+ e.getMessage());
            oos.writeObject(new ACException(e));
            throw e;
        }
    }
    
    public void returnCementerio(ObjectOutputStream oos, Integer id,  Sesion userSesion) throws Exception{
        try{
           CementerioBean cementerio = getCementerio(id);
                oos.writeObject(cementerio);
        }catch(Exception e){
            logger.error("returnplaza: "+ e.getMessage());
            oos.writeObject(new ACException(e));
            throw e;
        }
    }
    
    public void returnAllCamposElems(ObjectOutputStream oos, String superpatron, String patron, Sesion userSesion) throws Exception{
    	
    	try{
            
    		if (superpatron.equalsIgnoreCase(Const.SUPERPATRON_GELEMENTOS)){
    			if (patron.equalsIgnoreCase(Const.PATRON_UENTERRAMIENTO)){
    				for (Iterator it=comunManager.getCamposUnidadEnterramiento().iterator();it.hasNext();){;
    					oos.writeObject((CampoFiltro)it.next());
    				}
    			}else{
    				for (Iterator it=comunManager.getCamposBloque().iterator();it.hasNext();){;
						oos.writeObject((CampoFiltro)it.next());
    				}
    			}
    		}else if (superpatron.equalsIgnoreCase(Const.SUPERPATRON_GPROPIEDAD)){
    			if (patron.equalsIgnoreCase(Const.PATRON_CONCESION)){
    				for (Iterator it=comunManager.getCamposConcesion().iterator();it.hasNext();){;
						oos.writeObject((CampoFiltro)it.next());
    				}
    			}else if (patron.equalsIgnoreCase(Const.PATRON_TITULAR)){
    				for (Iterator it=comunManager.getCamposTitular().iterator();it.hasNext();){;
    					oos.writeObject((CampoFiltro)it.next());
    				}    				
    			}else if (patron.equalsIgnoreCase(Const.PATRON_TARIFASPROP)){
    				for (Iterator it=comunManager.getCamposTarifasPropiedad().iterator();it.hasNext();){;
						oos.writeObject((CampoFiltro)it.next());
    				}    				
    			}
    		}else if (superpatron.equalsIgnoreCase(Const.SUPERPATRON_GDIFUNTOS)){
    			if (patron.equalsIgnoreCase(Const.PATRON_DEFUNCION)){
    				for (Iterator it=comunManager.getCamposDifunto().iterator();it.hasNext();){;
    					oos.writeObject((CampoFiltro)it.next());
    				}    				
    			}else if (patron.equalsIgnoreCase(Const.PATRON_INHUMACION)){
    				for (Iterator it=comunManager.getCamposInhumacion().iterator();it.hasNext();){;
						oos.writeObject((CampoFiltro)it.next());
    				}
    			}else if (patron.equalsIgnoreCase(Const.PATRON_EXHUMACION)){
    				for (Iterator it=comunManager.getCamposExhumacion().iterator();it.hasNext();){;
						oos.writeObject((CampoFiltro)it.next());
    				}
    			}else if (patron.equalsIgnoreCase(Const.PATRON_TARIFASDEF)){
    				for (Iterator it=comunManager.getCamposTarifasDifuntos().iterator();it.hasNext();){;
					oos.writeObject((CampoFiltro)it.next());
    				}
    			}
    		}else if (superpatron.equalsIgnoreCase(Const.SUPERPATRON_GINTERVENCIONES)){
    			if(patron.equalsIgnoreCase(Const.PATRON_INTERVENCION)){
    				for (Iterator it=comunManager.getCamposIntervenciones().iterator();it.hasNext();){;
						oos.writeObject((CampoFiltro)it.next());
    				}
    			}
    		}
    			else if (superpatron.equalsIgnoreCase(Const.SUPERPATRON_GHISTORICOS)){
    			if(patron.equalsIgnoreCase(Const.PATRON_HISTORICO_DIFUNTO)){
    				for (Iterator it=comunManager.getCamposHistoricoDifuntos().iterator();it.hasNext();){;
						oos.writeObject((CampoFiltro)it.next());
    				}
    			}
    			else if(patron.equalsIgnoreCase(Const.PATRON_HISTORICO_PROPIEDAD)){
    				for (Iterator it=comunManager.getCamposHistoricoPropiedad().iterator();it.hasNext();){;
						oos.writeObject((CampoFiltro)it.next());
    				}
    			}
    		}
    		
    	}catch(Exception e){
            logger.error("returnUnidades: "+ e.getMessage());
            oos.writeObject(new ACException(e));
            throw e;
        }
    }
    
    
    public void returnHistorico(ObjectOutputStream oos, String superpatron, String patron, String cadena, Object[] filtro, Object[] idLayers,
    		Object[] idFeatures,Integer idCementerio,Object obj, Sesion userSesion) throws Exception{
        try{
    		if (superpatron.equalsIgnoreCase(Const.SUPERPATRON_GHISTORICOS)){
    			if (patron.equalsIgnoreCase(Const.PATRON_HISTORICO_DIFUNTO)){
    				for (Iterator it=historicoDifuntoMnager.getHistoricosByDifunto(superpatron, patron, filtro, idLayers, idFeatures, idCementerio, (DifuntoBean)obj, userSesion).iterator();it.hasNext();){;
    				oos.writeObject((HistoricoDifuntoBean)it.next());}
    			}else  {
    				for (Iterator it=historicoPropiedadManager.getHistoricosByTitular(superpatron, patron, filtro, idLayers, idFeatures, idCementerio, (PersonaBean)obj, userSesion).iterator();it.hasNext();){;
    				oos.writeObject((HistoricoPropiedadBean)it.next());}
    			}
    		}
           
        }catch(Exception e){
            logger.error("returnUnidades: "+ e.getMessage());
            oos.writeObject(new ACException(e));
            throw e;
        }
 }
    
    
    public void returnUnidadEnterramiento(ObjectOutputStream oos, String superpatron, String patron, String cadena, Object[] filtro, Object[] idLayers,
    		Object[] idFeatures,Integer idCementerio, Sesion userSesion) throws Exception{
        try{
            UnidadEnterramientoBean unidad = unidadManager.getUnidad(superpatron, patron,  filtro, idLayers, idFeatures, idCementerio, userSesion);
                oos.writeObject(unidad);
        }catch(Exception e){
            logger.error("returnUnidades: "+ e.getMessage());
            oos.writeObject(new ACException(e));
            throw e;
        }
 }
    
    public void returnConcesion(ObjectOutputStream oos, String superpatron, String patron, String cadena, Object[] filtro, Object[] idLayers, Object[] idFeatures, 
    		Integer idCementerio, Sesion userSesion) throws Exception{
        try{
           ConcesionBean concesion = concesionManager.getConcesion(superpatron, patron,  filtro, idLayers, idFeatures,idCementerio, userSesion);
                oos.writeObject(concesion);
        }catch(Exception e){
            logger.error("returnConcesiones: "+ e.getMessage());
            oos.writeObject(new ACException(e));
            throw e;
        }
 }
    
    
    public void returnUnidadEnterramiento(ObjectOutputStream oos, String patron, long idUnidadEnterramiento, Sesion userSesion) throws Exception{
        try{

            UnidadEnterramiento unidadEnterramiento = unidadEnterramientoDAO.selectByPrimaryKey((int)idUnidadEnterramiento); 
            oos.writeObject(unidadEnterramiento);
           
        }catch(Exception e){
            logger.error("returnUnidadEnterramiento: "+ e.getMessage());
            oos.writeObject(new ACException(e));
            throw e;
        }
    }
    
    
    /*************************************************************** CEMENTERIO **********************************************************************/

    public CementerioBean getCementerio(Integer idCementerio){

    	Cementerio cementerio = null;
    	CementerioBean cementerioBean = null;
    	try{
    		cementerio = cementerioDAO.selectByPrimaryKey(idCementerio);
    		cementerioBean = new CementerioBean();
    		cementerioBean.setId(cementerio.getId());
    		cementerioBean.setNombre(cementerio.getNombre());
    		
    	}catch (Exception e) {
			logger.error("Error obteniendo el cementerio" + e);
		}

		return cementerioBean;
    }
    

    
    /**
     * Inserta en BD un elemento del cementerio, asociandolo a la lista de features
     * @param oos parametro donde se deja el resultado de la operacion
     * @param idLayers a los que se asocia el inmueble
     * @param idFeatures a los que se asocia el inmueble
     * @param obj bien a insertar en BD
     * @param userSesion sesion del usuario
     */
    public void returnInsertElemCementerio(ObjectOutputStream oos, Object[] idLayers, Object[] idFeatures, Object obj, Integer idCementerio, Sesion userSesion) throws Exception{
       try{
           if (obj instanceof BloqueBean){
        	   BloqueBean elem= bloqueManager.insertBloque(idLayers, idFeatures, (BloqueBean)obj, idCementerio, userSesion);
               oos.writeObject(elem);
            }
           if (obj instanceof UnidadEnterramientoBean){
               UnidadEnterramientoBean elem= unidadManager.insertUnidadEnterramiento(idLayers, idFeatures, (UnidadEnterramientoBean)obj, idCementerio,  userSesion);
               oos.writeObject(elem);
            }
           if (obj instanceof TarifaBean){
        	   TarifaBean elem= tarifaManager.insertTarifa(idLayers, idFeatures, (TarifaBean)obj, idCementerio,  userSesion);
               oos.writeObject(elem);
            }
           else{
        	   Object[] cadenaBusqueda = null;
        	   UnidadEnterramientoBean unidad = unidadManager.getUnidad(Const.SUPERPATRON_GELEMENTOS, Const.PATRON_UENTERRAMIENTO,  cadenaBusqueda, idLayers, idFeatures,idCementerio, userSesion);
	           if (obj instanceof ConcesionBean){
	        	   //((ConcesionBean) obj).setUnidad(unidad);
	               ConcesionBean elem= concesionManager.insertConcesion(idLayers, idFeatures, (ConcesionBean)obj, idCementerio, userSesion);
	               oos.writeObject(elem);
	            }
	           if (obj instanceof DifuntoBean){
	        	   DifuntoBean elem = difuntoManager.insertDifunto (idLayers, idFeatures, (DifuntoBean)obj, userSesion);
	        	   oos.writeObject(elem);
	           }
	           if (obj instanceof IntervencionBean){
	               IntervencionBean elem= intervencionManager.insertIntervencion(idLayers, idFeatures, (IntervencionBean)obj, idCementerio, userSesion);
	               oos.writeObject(elem);
	            }
	           if (obj instanceof PlazaBean){
	        	    ((PlazaBean) obj).setIdUnidadEnterramiento((int) unidad.getIdUe());
	               PlazaBean elem= plazaManager.insertPlaza(idLayers, idFeatures, (PlazaBean)obj, userSesion);
	               oos.writeObject(elem);
	            }
	           if (obj instanceof InhumacionBean){
	               InhumacionBean elem= inhumacionManager.insertInhumacion(idLayers, idFeatures, (InhumacionBean)obj,idCementerio, userSesion);
	               oos.writeObject(elem);
	            }
	           if (obj instanceof ExhumacionBean){
	        	   ExhumacionBean elem= exhumacionManager.insertExhumacion(idLayers, idFeatures, (ExhumacionBean)obj,idCementerio, userSesion);
	               oos.writeObject(elem);
	            }	           
	           
           }
       }catch(Exception e){
           logger.error("returnInsertInventario: "+ e.getMessage());
           oos.writeObject(new ACException(e));
           throw e;
       }
    }
    

    public void returnUpdateElemCementerio(ObjectOutputStream oos, Object obj, Sesion userSesion) throws Exception{
        try{
            if (obj instanceof UnidadEnterramientoBean){
            	UnidadEnterramientoBean unidad= unidadManager.updateUnidadEnterramiento((UnidadEnterramientoBean)obj, userSesion);
                oos.writeObject(unidad);
            }
            if (obj instanceof ConcesionBean){
            	ConcesionBean elem = concesionManager.updateConcesion ((ConcesionBean)obj, userSesion);
            	oos.writeObject(elem);
            }
            if (obj instanceof BloqueBean){
            	BloqueBean elem = bloqueManager.updateBloque((BloqueBean)obj, userSesion) ;
            	oos.writeObject(elem);
            }
            if (obj instanceof TarifaBean){
         	   TarifaBean elem= tarifaManager.updateTarifa((TarifaBean)obj, userSesion);
                oos.writeObject(elem);
             }
           if (obj instanceof InhumacionBean){
               InhumacionBean elem= inhumacionManager.updateInhumacion((InhumacionBean)obj, userSesion);
               oos.writeObject(elem);
            }
           if (obj instanceof ExhumacionBean){
               ExhumacionBean elem= exhumacionManager.updateExhumacion((ExhumacionBean)obj, userSesion);
               oos.writeObject(elem);
            }
           if (obj instanceof PersonaBean){
               PersonaBean elem= titularManager.updateTitular((PersonaBean)obj, userSesion);
               oos.writeObject(elem);
            }
           if (obj instanceof IntervencionBean){
               IntervencionBean elem= intervencionManager.updateIntervencion((IntervencionBean)obj, userSesion);
               oos.writeObject(elem);
            }           
            
        }catch(Exception e){
            logger.error("returnUpdateInventario: "+ e.getMessage());
            oos.writeObject(new ACException(e));
            throw e;
        }
     }

    
    public void returnEliminarElemCementerio(ObjectOutputStream oos, Object[] idLayers, Object[] idFeatures, Object obj,Integer idCementerio, Sesion userSesion) throws Exception{
    	try{
         	if (obj instanceof ConcesionBean){
            	ConcesionBean concesion = concesionManager.deleteConcesion((ConcesionBean)obj, userSesion);
            	oos.writeObject(concesion);
            }
         	else{
             	Object[] cadenaBusqueda = null;
             	ConcesionBean concesion = concesionManager.getConcesion(Const.SUPERPATRON_GPROPIEDAD, 
             				Const.PATRON_CONCESION,  cadenaBusqueda, idLayers, idFeatures, idCementerio, userSesion);
             	
             	if (obj instanceof UnidadEnterramientoBean){
             		if (((((UnidadEnterramientoBean)obj).getPlazas() != null) && (((UnidadEnterramientoBean)obj).getPlazas().size() == 0 )) || 
             				((((UnidadEnterramientoBean)obj).getPlazas()!= null) && (unidadManager.todasPlazasLibres(((UnidadEnterramientoBean)obj))))){
	             		if (concesion!= null){
	             			ErrorBean error = new ErrorBean();
	             			error.setSuperPatron(Const.SUPERPATRON_GELEMENTOS);
	             			error.setPatron(Const.PATRON_UENTERRAMIENTO);
	             			error.setMessage("No es posible eliminar! \n La unidad de enterrramiento tiene una concesión asociada");
	             			error.setTitle(" Intentando borrar..");
	             			oos.writeObject(error);
	             		}else{
		                    UnidadEnterramientoBean unidad= unidadManager.deleteUnidadEnterramiento(idLayers, idFeatures,(UnidadEnterramientoBean)obj, userSesion);
		                    oos.writeObject(unidad);
	             		}
             		
             		}else{
             			ErrorBean error = new ErrorBean();
             			error.setSuperPatron(Const.SUPERPATRON_GELEMENTOS);
             			error.setPatron(Const.PATRON_UENTERRAMIENTO);
             			error.setMessage("No es posible eliminar! \n La unidad de enterrramiento tiene plazas asignadas");
             			error.setTitle(" Intentando borrar..");
             			oos.writeObject(error);
             		}
             } if (obj instanceof TarifaBean){
            	 if (!concesionManager.estaEnUso(((TarifaBean)obj).getId_tarifa())){
            		 TarifaBean tarifa = tarifaManager.deleteTarifa(idLayers, idFeatures, (TarifaBean)obj, userSesion);
            		 oos.writeObject(tarifa);
            	 }else{
          			ErrorBean error = new ErrorBean();
         			error.setSuperPatron(Const.SUPERPATRON_GELEMENTOS);
         			error.setPatron(Const.PATRON_TARIFASPROP);
         			error.setMessage("No es posible eliminar! \n La tarifa está en uso");
         			error.setTitle(" Intentando borrar..");
         			oos.writeObject(error);
            	 }
          }
             if (obj instanceof PersonaBean){
            	 boolean estitular = false;
            	 Collection c;
            	 Object[] arrayElems;
            	 c = concesionManager.getConcesionesByTitular(((PersonaBean)obj).getDNI(), userSesion);
            	 arrayElems = c.toArray();
            	 if (arrayElems.length > 0){
            		estitular=true;
 					ErrorBean error = new ErrorBean();
              		error.setSuperPatron(Const.SUPERPATRON_GPROPIEDAD);
              		error.setPatron(Const.PATRON_TITULAR);
              		error.setMessage("No es posible eliminar! \n es titular de una concesión");
              		error.setTitle(" Intentando borrar..");
              		oos.writeObject(error);
 				}
            	if (!estitular){ 
                	PersonaBean titular = titularManager.deleteTitular((PersonaBean)obj, userSesion);
                	oos.writeObject(titular);
            	}
             }
            if (obj instanceof BloqueBean){
            	BloqueBean bloque = bloqueManager.deleteBloque((BloqueBean)obj, userSesion);
            	  oos.writeObject(bloque);
            }
            if (obj instanceof DifuntoBean){
            	DifuntoBean difunto = difuntoManager.deleteDifunto(idLayers, idFeatures, (DifuntoBean)obj, userSesion);
            	  oos.writeObject(difunto);
            }
           if (obj instanceof InhumacionBean){
               InhumacionBean elem= inhumacionManager.deleteInhumacion(idLayers, idFeatures, (InhumacionBean)obj,idCementerio, userSesion);
               oos.writeObject(elem);
            }
           if (obj instanceof ExhumacionBean){
               ExhumacionBean elem= exhumacionManager.deleteExhumacion(idLayers, idFeatures, (ExhumacionBean)obj,idCementerio, userSesion);
               oos.writeObject(elem);
            }
           if (obj instanceof IntervencionBean){
               IntervencionBean elem= intervencionManager.deleteIntervencion(idLayers, idFeatures, (IntervencionBean)obj,idCementerio, userSesion);
               oos.writeObject(elem);
            }   
         }
        }catch(Exception e){
            logger.error("returnUpdateInventario: "+ e.getMessage());
            oos.writeObject(new ACException(e));
            throw e;
        }
     }
    
    
}
