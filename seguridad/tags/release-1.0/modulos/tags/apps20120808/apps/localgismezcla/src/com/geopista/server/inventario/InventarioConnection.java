package com.geopista.server.inventario;

import java.io.File;
import java.io.FilenameFilter;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeMap;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.postgis.PGgeometry;

import com.geopista.app.inventario.Constantes;
import com.geopista.app.inventario.ConstantesEIEL;
import com.geopista.app.inventario.panel.DatosAmortizacionJPanel;
import com.geopista.protocol.control.Sesion;
import com.geopista.protocol.inventario.BienBean;
import com.geopista.protocol.inventario.BienPreAltaBean;
import com.geopista.protocol.inventario.BienRevertible;
import com.geopista.protocol.inventario.CampoFiltro;
import com.geopista.protocol.inventario.CompanniaSeguros;
import com.geopista.protocol.inventario.ConfigParameters;
import com.geopista.protocol.inventario.Const;
import com.geopista.protocol.inventario.CreditoDerechoBean;
import com.geopista.protocol.inventario.CuentaAmortizacion;
import com.geopista.protocol.inventario.CuentaContable;
import com.geopista.protocol.inventario.DerechoRealBean;
import com.geopista.protocol.inventario.HistoricoAmortizacionesBean;
import com.geopista.protocol.inventario.InmuebleBean;
import com.geopista.protocol.inventario.InmuebleRusticoBean;
import com.geopista.protocol.inventario.InmuebleUrbanoBean;
import com.geopista.protocol.inventario.Inventario;
import com.geopista.protocol.inventario.InventarioEIELBean;
import com.geopista.protocol.inventario.ListaEIEL;
import com.geopista.protocol.inventario.ListaHistoricoAmortizaciones;
import com.geopista.protocol.inventario.Lote;
import com.geopista.protocol.inventario.Mejora;
import com.geopista.protocol.inventario.MuebleBean;
import com.geopista.protocol.inventario.Observacion;
import com.geopista.protocol.inventario.ReferenciaCatastral;
import com.geopista.protocol.inventario.Seguro;
import com.geopista.protocol.inventario.SemovienteBean;
import com.geopista.protocol.inventario.UsoFuncional;
import com.geopista.protocol.inventario.ValorMobiliarioBean;
import com.geopista.protocol.inventario.VehiculoBean;
import com.geopista.protocol.inventario.ViaBean;
import com.geopista.protocol.licencias.CReferenciaCatastral;
import com.geopista.security.GeopistaAcl;
import com.geopista.security.GeopistaPermission;
import com.geopista.server.administradorCartografia.ACException;
import com.geopista.server.administradorCartografia.AdministradorCartografiaClient;
import com.geopista.server.administradorCartografia.LockException;
import com.geopista.server.administradorCartografia.NewSRID;
import com.geopista.server.administradorCartografia.PermissionException;
import com.geopista.server.database.CPoolDatabase;
import com.geopista.server.document.DocumentConnection;
import com.geopista.server.document.DocumentoEnDisco;
import com.geopista.util.TypeUtil;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;


/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 14-jul-2006
 * Time: 10:18:17
 * To change this template use File | Settings | File Templates.
 */
public class InventarioConnection {
    private org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(InventarioConnection.class);
    //private SRID srid;
    private NewSRID srid;
    private static int AMORTIZACION= 1;
    private static int CONTABLE= 2;
    private Vector docsBorrados;
    private String municipio;
    private final String TABLA_OBSERVACION_INVENTARIO= "observaciones_inventario";
    private final String TABLA_OBSERVACION_BIEN_REVERTIBLE = "observaciones_bien_revertible";
    

    private static final int SQL_INMUEBLE= 0;
    private static final int SQL_INMUEBLE_URBANO= 1;
    private static final int SQL_INMUEBLE_RUSTICO= 2;
    private static final int SQL_MUEBLE= 3;
    private static final int SQL_DERECHOS_REALES= 4;
    private static final int SQL_VALOR_MOBILIARIO= 5;
    private static final int SQL_CREDITO_DERECHO= 6;
    private static final int SQL_SEMOVIENTE= 7;
    private static final int SQL_VIAS= 8;
    private static final int SQL_VEHICULOS= 9;
    private static String sSQLVersionado = "";
    private static String sMaxRevision = "";
    private static String sRevisionActual = "";
//    private static String sMaxRevision = "";
//    private static String sRevisionActual = "";
    private static String sRevisionExpiradaNula = "";
  
    
    private static final int TABLE_INVENTARIO_CREDITOS=12001;
    private static final int TABLE_INVENTARIO_DERECHOS=12002;
    private static final int TABLE_INVENTARIO_INMUEBLES=12003;
    private static final int TABLE_INVENTARIO_MUEBLES=12004;
    private static final int TABLE_INVENTARIO_SEMOVIENTES=12005;
    private static final int TABLE_INVENTARIO_VALORES=12006;
    private static final int TABLE_INVENTARIO_VEHICULOS=12007;
    private static final int TABLE_INVENTARIO_VIAS=12008;
    private static final int TABLE_INVENTARIO_REVERTIBLES=12008;
    
	public static final String POR_PORCENTAJE = "Por Porcentaje";
	public static final String POR_AÑOS = "Por Años";

    public InventarioConnection(String municipio, String fechaVersion){
		sSQLVersionado = "";
		sRevisionExpiradaNula = " and revision_expirada = 9999999999 ";
		if (!Const.fechaVersion.equals("")) {
			sSQLVersionado = " and ?.revision_actual <= (select coalesce(max(v.revision),0) from versiones v, tables_inventario t where v.fecha <=to_timestamp('"
					+ Const.fechaVersion
					+ "','yyyy-MM-dd HH24:MI:ss') and v.id_table_versionada=t.id_table and t.name='?') ";
			// sSQLVersionado +=
			// " and revision_expirada > (select coalesce(max(v.revision),0) from versiones v, tables_inventario t where v.fecha <=to_timestamp('"+Const.fechaVersion+"','yyyy-MM-dd HH24:MI:ss') and v.id_table_versionada=t.id_table and t.name=?) ";
		} else {
			sSQLVersionado = " and ?.revision_actual <= (select coalesce(max(v.revision),0) from versiones v, tables_inventario t where v.fecha <=localtimestamp and v.id_table_versionada=t.id_table and t.name='?') ";
			// sSQLVersionado += sRevisionExpiradaNula;
		}
		sMaxRevision = " (select nextval('\"seq_inventario_?\"')) ";
		sRevisionActual = " (select currval('\"seq_inventario_?\"')) ";
		this.municipio = municipio;
    }
   

    public InventarioConnection(NewSRID srid, String municipio, String fechaVersion){
    	this(municipio, fechaVersion);
    	this.srid=srid;
    }
    public InventarioConnection(String municipio){
    	this.municipio = municipio;
    }

    public void setSRID(NewSRID srid){
        this.srid=srid;
    }

    /**
     * Devuelve los inmuebles de un tipo urbano y rustico
     * @param oos parametro donde se deja el resultado de la operacion
     * @param patron tipo del inmueble
     * @param userSesion sesion del usuario
     */
    public void returnInmuebles(ObjectOutputStream oos, String superpatron, String patron, 
    		String cadena, Object[] filtro, Object[] idLayers, Object[] idFeatures,  Sesion userSesion,
    		ConfigParameters configParameters) throws Exception{
           try{
               for (Iterator it=getInmuebles(superpatron, patron, cadena, filtro, idLayers, idFeatures,null, 
            		   userSesion,false,configParameters).iterator();it.hasNext();){
                   oos.writeObject((InmuebleBean)it.next());
               }
           }catch(Exception e){
               logger.error("returnInmuebles: "+ e.getMessage());
               oos.writeObject(new ACException(e));
               throw e;
           }
    }
    
    /**
     * Devuelve el inmueble de un tipo urbano o rustico seleccionado
     * @param oos parametro donde se deja el resultado de la operacion
     * @param patron tipo del inmueble
     * @param userSesion sesion del usuario
     */
     /*public void returnInmueble(ObjectOutputStream oos, String patron, Long idInmueble, Sesion userSesion) throws Exception{
           try{
        	   
               InmuebleBean inmueble = getInmueble(idInmueble, patron, userSesion);               
               oos.writeObject(inmueble);
              
           }catch(Exception e){
               logger.error("returnInmuebles: "+ e.getMessage());
               oos.writeObject(new ACException(e));
               throw e;
           }
    }*/

    /**
     * Inserta en BD un bien, asociandolo a la lista de features
     * @param oos parametro donde se deja el resultado de la operacion
     * @param idLayers a los que se asocia el inmueble
     * @param idFeatures a los que se asocia el inmueble
     * @param obj bien a insertar en BD
     * @param userSesion sesion del usuario
     */
    public void returnInsertInventario(ObjectOutputStream oos, Object[] idLayers, Object[] idFeatures, Object obj, Sesion userSesion) throws Exception{
       try{
           if (obj instanceof InmuebleBean){
               InmuebleBean inm= insertInmueble(idLayers, idFeatures, (InmuebleBean)obj, userSesion);
               oos.writeObject(inm);
            }else if (obj instanceof MuebleBean){
                Collection<MuebleBean> listaMuebles= insertMueble(idLayers, idFeatures, (MuebleBean)obj, userSesion);
                oos.writeObject(listaMuebles);
            }else if (obj instanceof DerechoRealBean){
                DerechoRealBean derechoReal= insertDerechoReal(idLayers, idFeatures, (DerechoRealBean)obj, userSesion);
                oos.writeObject(derechoReal);
            }else if (obj instanceof ValorMobiliarioBean){
                ValorMobiliarioBean valorMobiliario= insertValorMobiliario(idLayers, idFeatures, (ValorMobiliarioBean)obj, userSesion);
                oos.writeObject(valorMobiliario);
            }else if (obj instanceof CreditoDerechoBean){
                CreditoDerechoBean creditoDerecho= insertCreditoDerecho(idLayers, idFeatures, (CreditoDerechoBean)obj, userSesion);
                oos.writeObject(creditoDerecho);
            }else if (obj instanceof SemovienteBean){
                SemovienteBean semoviente= insertSemoviente(idLayers, idFeatures, (SemovienteBean)obj, userSesion);
                oos.writeObject(semoviente);
            }else if (obj instanceof ViaBean){
                ViaBean via= insertVia(idLayers, idFeatures, (ViaBean)obj, userSesion);
                oos.writeObject(via);
            }else if (obj instanceof VehiculoBean){
                VehiculoBean vehiculo= insertVehiculo(idLayers, idFeatures, (VehiculoBean)obj, userSesion);
                oos.writeObject(vehiculo);
            }

       }catch(Exception e){
           logger.error("returnInsertInventario: Error al insertar el bien: "+((BienBean)obj).getNumInventario(),e);
           oos.writeObject(new ACException(e));
           throw e;
       }
    }

    /**
     * Actualiza en BD el inventario
     * @param oos parametro donde se deja el resultado de la operacion
     * @param obj bien a actualizar en BD
     * @param userSesion sesion del usuario
     */
    public void returnUpdateInventario(ObjectOutputStream oos, Object obj, Sesion userSesion) throws Exception{
       try{
           if (obj instanceof InmuebleBean){
               InmuebleBean inm= updateInmueble((InmuebleBean)obj, userSesion);
               oos.writeObject(inm);
           }else if (obj instanceof MuebleBean){
               MuebleBean mueble= updateMueble((MuebleBean)obj, userSesion);
               oos.writeObject(mueble);
           }else if (obj instanceof DerechoRealBean){
               DerechoRealBean derechoReal= updateDerechoReal((DerechoRealBean)obj, userSesion);
               oos.writeObject(derechoReal);
           }else if (obj instanceof ValorMobiliarioBean){
               ValorMobiliarioBean valorMobiliario= updateValorMobiliario((ValorMobiliarioBean)obj, userSesion);
               oos.writeObject(valorMobiliario);
           }else if (obj instanceof CreditoDerechoBean){
               CreditoDerechoBean creditoDerecho= updateCreditoDerecho((CreditoDerechoBean)obj, userSesion);
               oos.writeObject(creditoDerecho);
           }else if (obj instanceof SemovienteBean){
               SemovienteBean semoviente= updateSemoviente((SemovienteBean)obj, userSesion);
               oos.writeObject(semoviente);
           }else if (obj instanceof ViaBean){
               ViaBean via= updateVia((ViaBean)obj, userSesion);
               oos.writeObject(via);
           }else if (obj instanceof VehiculoBean){
               VehiculoBean vehiculo= updateVehiculo((VehiculoBean)obj, userSesion);
               oos.writeObject(vehiculo);
           }

       }catch(Exception e){
           logger.error("returnUpdateInventario: "+ e.getMessage());
           oos.writeObject(new ACException(e));
           throw e;
       }
    }
    /**
     * Actualiza en BD el bien revertible
     * @param oos parametro donde se deja el resultado de la operacion
     * @param obj bienRevertible a actualizar en BD
     * @param userSesion sesion del usuario
     */
    public void returnUpdateBienRevertible(ObjectOutputStream oos, 
    		BienRevertible bienRevertible, Sesion userSesion) throws Exception{
       try{
    	    //if (bienRevertible.getId()==null) 
    	        if (bienRevertible.getId()!=null)//Si se trata de una modificación añadimos la fecha
    	        	bienRevertible.setFechaUltimaModificacion(new Date());
    	    	bienRevertible=insertBienRevertible(bienRevertible, userSesion, null);
    	   // else 
    	   // 	bienRevertible=updateBienRevertible(bienRevertible);
    	    oos.writeObject(bienRevertible);
        }catch(Exception e){
           logger.error("returnUpdateBienRevertible: ", e);
           oos.writeObject(new ACException(e));
           throw e;
       }
    }
    /**
     * Actualiza en BD el bien revertible
     * @param oos parametro donde se deja el resultado de la operacion
     * @param obj bienRevertible a actualizar en BD
     * @param userSesion sesion del usuario
     */
    public void returnUpdateLote(ObjectOutputStream oos, Lote lote, Sesion userSesion) throws Exception{
       try{
    	    if (lote.getId_lote()==null)
                lote=insertarLote(lote,userSesion);
    	    else
    	    	lote=updateLote(lote, userSesion);
    	    oos.writeObject(lote);
        }catch(Exception e){
           logger.error("returnUpdateBienRevertible: ", e);
           oos.writeObject(new ACException(e));
           throw e;
       }
    }
    /**
     * Marca como Borrado un bien en la BD
     * @param oos parametro donde se deja el resultado de la operacion
     * @param obj
     * @param userSesion sesion del usuario
     */
    public void returnBorrarInventario(ObjectOutputStream oos, Object obj, Sesion userSesion) throws Exception{
       try{
    	   if (obj instanceof BienBean ){
    		   Object o= borrarInventario((BienBean)obj, userSesion);
    		   oos.writeObject(o);}
    	   else if(obj instanceof BienRevertible){
    		   Object o= borrarBienRevertible((BienRevertible)obj, userSesion);
    		   oos.writeObject(o);}
    	   
       }catch(Exception e){
           logger.error("returnBorrarInventario: "+ e.getMessage());
           oos.writeObject(new ACException(e));
           throw e;
       }
    }


    /**
     * Recupera un bien de la BD
     * @param oos parametro donde se deja el resultado de la operacion
     * @param obj
     * @param userSesion sesion del usuario
     */
    public void recuperarInventario(ObjectOutputStream oos, Object obj, Sesion userSesion) throws Exception{
       try{
    	   if (obj instanceof BienBean){
    		   Object o= recuperarInventario((BienBean)obj, userSesion);
    		   oos.writeObject(o);
    	   }
    	   if (obj instanceof BienRevertible){
    		   Object o= recuperarRevertible((BienRevertible)obj, userSesion);
    		   oos.writeObject(o);
    	   }
       }catch(Exception e){
           logger.error("returnBorrarInventario: "+ e.getMessage());
           oos.writeObject(new ACException(e));
           throw e;
       }
    }

    /**
     * Elimina el inventario de la BD
     * @param oos parametro donde se deja el resultado de la operacion
     * @param obj bien a eliminar de BD
     * @param userSesion sesion del usuario
     */
    public void returnEliminarInventario(ObjectOutputStream oos, Object obj, Object[] idLayers, 
    		Object[] idFeatures, Sesion userSesion) throws Exception{
       try{
           BienBean bien= eliminarInventario((BienBean)obj, idLayers, idFeatures, userSesion);
           oos.writeObject(bien);
       }catch(Exception e){
           logger.error("returnEliminarInventario: "+ e.getMessage());
           oos.writeObject(new ACException(e));
           throw e;
       }
    }

    /**
     * Elimina todo el inventario para un municipio determinado
     * @param obj objeto a eliminar o null si se quieren eliminar todos los elementos
     */
    public void returnEliminarTodoInventario(Connection conn,Object obj,ObjectOutputStream oos) throws Exception{
       try{
    	   if (obj==null){
    		   eliminarTodoInventarioBienRevertible(conn,obj);
    		   eliminarTodoInventarioLotes(conn,obj);
    		   eliminarTodoInventarioBienes(conn,obj);
    	   }
    	   else{
    		   if (obj instanceof BienRevertible)
        		   eliminarTodoInventarioBienRevertible(conn,obj);
        	   if (obj instanceof Lote)
        		   eliminarTodoInventarioLotes(conn,obj);
        	   if (obj instanceof BienBean)
        		   eliminarTodoInventarioBienes(conn,obj);
        	   if (oos!=null)
        		   oos.writeObject(new Boolean(true)); 
    	   }
    	   
       }catch(Exception e){
           logger.error("returnEliminarTodoInventario: "+ e.getMessage());
           if (oos!=null)
        	   oos.writeObject(new ACException(e));
           throw e;
       }
  
    }
    /**
     * Elimina el bien revertible de la base de daots
     * @param oos parametro donde se deja el resultado de la operacion
     * @param obj bien a eliminar de BD
     * @param userSesion sesion del usuario
     */
    public void returnEliminarBienRevertible(ObjectOutputStream oos, BienRevertible bienRevertible, Sesion userSesion) throws Exception{
       try{
           BienRevertible bien= eliminarBienRevertible(bienRevertible, userSesion);
           oos.writeObject(bien);
       }catch(Exception e){
           logger.error("returnEliminarInventario: "+ e.getMessage());
           oos.writeObject(new ACException(e));
           throw e;
       }
    }

    /**
     * Elimina el bien revertible de la base de daots
     * @param oos parametro donde se deja el resultado de la operacion
     * @param obj bien a eliminar de BD
     * @param userSesion sesion del usuario
     */
    public void returnEliminarLote(ObjectOutputStream oos, Lote lote, Sesion userSesion) throws Exception{
       try{
           lote= eliminarLote(lote, userSesion);
           oos.writeObject(lote);
       }catch(Exception e){
           logger.error("returnEliminarInventario: "+ e.getMessage());
           oos.writeObject(new ACException(e));
           throw e;
       }
    }
    /**
     * Devuelve las cuentas contables existentes en BD
     * @param oos parametro donde se deja el resultado de la operacion
     */
    public void returnCuentasContables(ObjectOutputStream oos) throws Exception{
           try{
               for (Iterator it=getCuentasContables().iterator();it.hasNext();){
                   oos.writeObject((CuentaContable)it.next());
               }
           }catch(Exception e){ 
               logger.error("returnCuentasContables: "+ e.getMessage());
               oos.writeObject(new ACException(e));
               throw e;
           }
    }


    /**
     * Devuelve las cuentas de amortizacion existentes en BD
     * @param oos parametro donde se deja el resultado de la operacion
     */
    public void returnCuentasAmortizacion(ObjectOutputStream oos) throws Exception{
           try{
               for (Iterator <CuentaAmortizacion>it=getCuentasAmortizacion().iterator();it.hasNext();){
                   oos.writeObject((CuentaAmortizacion)it.next());
               }
           }catch(Exception e){
               logger.error("returnCuentasAmortizacion: "+ e.getMessage());
               oos.writeObject(new ACException(e));
               throw e;
           }
    }

    /**
     * Devuelve la lista de compannias de seguros existentes en BD
     * @param oos parametro donde se deja el resultado de la operacion
     */
    public void returnCompanniasSeguros(ObjectOutputStream oos) throws Exception{
           try{
               for (Iterator it=getCompanniaSeguros().iterator();it.hasNext();){
                   oos.writeObject((CompanniaSeguros)it.next());
               }
           }catch(Exception e){
               logger.error("returnCompanniasSeguros: "+ e.getMessage());
               oos.writeObject(new ACException(e));
               throw e;
           }
    }

    /**
     * Devuelve los muebles de un tipo (historico_artisticos)
     * @param oos parametro donde se deja el resultado de la operacion
     * @param patron tipo del bien mueble
     * @param userSesion sesion del usuario
     * @param configParameters 
     */
    public void returnMuebles(ObjectOutputStream oos, String superpatron, String patron, String cadena, Object[] filtro, Object[] idLayers, 
    		Object[] idFeatures, Sesion userSesion, ConfigParameters configParameters) throws Exception{
           try{
               for (Iterator it=getMuebles(superpatron, patron, cadena, 
            		   filtro, idLayers, idFeatures,null, 
            		   userSesion, (idFeatures==null||idFeatures.length==0)?false:true,configParameters).iterator();it.hasNext();){
                   oos.writeObject((MuebleBean)it.next());
               }
           }catch(Exception e){
               logger.error("returnMuebles: "+ e.getMessage());
               oos.writeObject(new ACException(e));
               throw e;
           }
    }

    /**
     * Devuelve los derechos reales (historico_artisticos)
     * @param oos parametro donde se deja el resultado de la operacion
     * @param patron tipo del bien mueble
     * @param idLayers
     * @param idFeatures
     * @param userSesion sesion del usuario
     * @param configParameters 
     * @throws Exception
     */
    public void returnDerechosReales(ObjectOutputStream oos, String superpatron, String patron, String cadena, Object[] filtro, Object[] idLayers, Object[] idFeatures,
                                     Sesion userSesion, ConfigParameters configParameters) throws Exception{
           try{
               for (Iterator it=getDerechosReales(superpatron, patron, cadena, filtro, idLayers, idFeatures,null, 
            		   userSesion, false,configParameters).iterator();it.hasNext();){
                   oos.writeObject((DerechoRealBean)it.next());
               }
           }catch(Exception e){
               logger.error("returnMuebles: "+ e.getMessage());
               oos.writeObject(new ACException(e));
               throw e;
           }
    }

    /**
     * Retorna los bienes de inventario de tipo valor mobiliario
     * @param oos bufffer donde escribe el resultado de la operacion
     * @param patron tipo del bien de inventario
     * @param idLayers
     * @param idFeatures
     * @param userSesion sesion de usuario
     * @param configParameters 
     * @throws Exception
     */
    public void returnValoresMobiliarios(ObjectOutputStream oos, String superpatron, String patron, String cadena, Object[] filtro, Object[] idLayers, 
    		    Object[] idFeatures, Sesion userSesion, ConfigParameters configParameters) throws Exception{
           try{
               for (Iterator it=getValoresMobiliarios(superpatron, patron, cadena,
            		   filtro, idLayers, idFeatures, null,userSesion, false,configParameters).iterator();it.hasNext();){
                   oos.writeObject((ValorMobiliarioBean)it.next());
               }
           }catch(Exception e){
               logger.error("returnValoresMobiliarios: "+ e.getMessage());
               oos.writeObject(new ACException(e));
               throw e;
           }
    }

    /**
     * Retorna los bienes de inventario de tipo creditos y derechos
     * @param oos buffer donde escribe el resultado de la operacion
     * @param patron tipo del bien de inventario
     * @param idLayers
     * @param idFeatures
     * @param userSesion sesion de usuario
     * @param configParameters 
     * @throws Exception
     */
    public void returnCreditosDerechos(ObjectOutputStream oos, String superpatron, String patron, String cadena, Object[] filtro, Object[] idLayers, 
    		Object[] idFeatures, Sesion userSesion, ConfigParameters configParameters) throws Exception{
           try{
               for (Iterator it=getCreditosDerechos(superpatron, patron, 
            		   cadena, filtro, idLayers, idFeatures,null, 
            		   userSesion , false,configParameters).iterator();it.hasNext();){
                   oos.writeObject((CreditoDerechoBean)it.next());
               }
           }catch(Exception e){
               logger.error("returnCreditosDerechos: "+ e.getMessage());
               oos.writeObject(new ACException(e));
               throw e;
           }
    }

    /**
     * Retorna los bienes de inventario de tipo semoviente
     * @param oos buffer donde inserta el resultado de la operacion
     * @param patron tipo del bien de inventario
     * @param idLayers
     * @param idFeatures
     * @param userSesion sesion de usuario
     * @param configParameters 
     * @throws Exception
     */
    public void returnSemovientes(ObjectOutputStream oos, String superpatron, String patron, String cadena, Object[] filtro, Object[] idLayers, 
    		Object[] idFeatures, Sesion userSesion, ConfigParameters configParameters) throws Exception{
           try{
               for (Iterator it=getSemovientes(superpatron, patron, cadena, filtro, idLayers, idFeatures,null,
            		   userSesion,false,configParameters).iterator();it.hasNext();){
                   oos.writeObject((SemovienteBean)it.next());
               }
           }catch(Exception e){
               logger.error("returnSemovientes: "+ e.getMessage());
               oos.writeObject(new ACException(e));
               throw e;
           }
    }
    
    /**
     * Metodo generico para devolver un bien determinado
     * @param oos
     * @param superpatron
     * @param patron
     * @param idBien
     * @param userSesion
     * @param configParameters 
     * @throws Exception
     */
    public void returnGenericBien(ObjectOutputStream oos, String superpatron, String patron, Long idBien, Long idRevision,Long idRevisionExpirada,Sesion userSesion, ConfigParameters configParameters) throws Exception{
        try{
            Object[] idFeatures= new ArrayList<Object>().toArray();
            Object[] idLayers= new ArrayList<Object>().toArray();
           
            ArrayList<CampoFiltro> filtros=new ArrayList<CampoFiltro>();
            CampoFiltro campoFiltro=new CampoFiltro();
            campoFiltro.setNombre("id");
            campoFiltro.setTabla("bienes_inventario");
            campoFiltro.setTipo(CampoFiltro.NUMERIC_CODE);
            campoFiltro.setOperador("=");
            campoFiltro.setValorNumeric(idBien);
            filtros.add(campoFiltro);
            
            Object[] filtro= filtros.toArray();
            
            logger.info("Recuperando Generic Bien");
            if (idRevisionExpirada==Long.parseLong("9999999999"))
            	configParameters.setBusquedaIndividual(true);
            else
            	configParameters.setBusquedaIndividual(false);
                        
            if (patron.equals(Const.PATRON_SEMOVIENTES))
	            for (Iterator it=getSemovientes(superpatron, patron, null, filtro, idLayers, idFeatures,null, 
	            		userSesion, true,configParameters).iterator();it.hasNext();){
	                oos.writeObject((SemovienteBean)it.next());
	            }
            else if (patron.equals(Const.PATRON_VEHICULOS))
	            for (Iterator it=getVehiculos(superpatron, patron, null, filtro, idLayers, idFeatures,null, 
	            		userSesion, true,configParameters).iterator();it.hasNext();){
	                oos.writeObject((VehiculoBean)it.next());
	            }
            else if (patron.equals(Const.PATRON_DERECHOS_REALES))
	            for (Iterator it=getDerechosReales(superpatron, patron, null, filtro, idLayers, idFeatures,null, 
	            		userSesion, true,configParameters).iterator();it.hasNext();){
	                oos.writeObject((DerechoRealBean)it.next());
	            }            
            else if (patron.equals(Const.PATRON_CREDITOS_DERECHOS_PERSONALES))
	            for (Iterator it=getCreditosDerechos(superpatron, patron, null, filtro, idLayers, idFeatures,null, 
	            		userSesion, true,configParameters).iterator();it.hasNext();){
	                oos.writeObject((CreditoDerechoBean)it.next());
	            }
            else if (patron.equals(Const.PATRON_MUEBLES_HISTORICOART)|| patron.equals(Const.PATRON_BIENES_MUEBLES))
	            for (Iterator it=getMuebles(superpatron, patron, null, filtro, idLayers, idFeatures,null, 
	            		userSesion, true,configParameters).iterator();it.hasNext();){
	                oos.writeObject((MuebleBean)it.next());
	            }
            else if (patron.equals(Const.PATRON_VALOR_MOBILIARIO))
	            for (Iterator it=getValoresMobiliarios(superpatron, patron, null, filtro, idLayers, idFeatures,null, 
	            		userSesion, true,configParameters).iterator();it.hasNext();){
	                oos.writeObject((ValorMobiliarioBean)it.next());
	            }            
            else if (patron.equals(Const.PATRON_VIAS_PUBLICAS_RUSTICAS))
	            for (Iterator it=getVias(superpatron, patron, null, filtro, idLayers, idFeatures,null,
	            		userSesion, true,configParameters).iterator();it.hasNext();){
	                oos.writeObject((ViaBean)it.next());
	            }       
            else if (patron.equals(Const.PATRON_VIAS_PUBLICAS_URBANAS))
	            for (Iterator it=getVias(superpatron, patron, null, filtro, idLayers, idFeatures,null, 
	            		userSesion, true,configParameters).iterator();it.hasNext();){
	                oos.writeObject((ViaBean)it.next());
	            }  
            else if (patron.equals(Const.PATRON_INMUEBLES_RUSTICOS) || patron.equals(Const.PATRON_INMUEBLES_URBANOS))
	            for (Iterator it=getInmuebles(superpatron, patron, null, filtro, idLayers, idFeatures,null, 
	            		userSesion,true,configParameters).iterator();it.hasNext();){
	                oos.writeObject((InmuebleBean)it.next());
	            }    
            	
        }catch(Exception e){
            logger.error("returnGenericBean: "+ e.getMessage());
            oos.writeObject(new ACException(e));
            throw e;
        }
 }
    


    /**
     * Retorna los bienes de inventario tipo via (urbana y rustica)
     * @param oos
     * @param patron tipo de bien de inventario
     * @param idLayers
     * @param idFeatures
     * @param userSesion sesion del usuario
     * @param configParameters 
     * @throws Exception
     */
    public void returnVias(ObjectOutputStream oos, String superpatron, String patron, String cadena, Object[] filtro, Object[] idLayers, 
    		Object[] idFeatures, Sesion userSesion, ConfigParameters configParameters) throws Exception{
           try{
               for (Iterator it=getVias(superpatron, patron, cadena, filtro, idLayers, 
            		   idFeatures, null,userSesion, false,configParameters).iterator();it.hasNext();){
                   oos.writeObject((ViaBean)it.next());
               }
           }catch(Exception e){
               logger.error("returnVias: "+ e.getMessage());
               oos.writeObject(new ACException(e));
               throw e;
           }
    }

    /**
     * Retona los bienes de inventario que son vehiculos
     * @param oos buffer donde escribe el resulatdo de la operacion
     * @param patron tipo de bien
     * @param idLayers
     * @param idFeatures
     * @param userSesion sesion de usuario
     * @param configParameters 
     * @throws Exception
     */
    public void returnVehiculos(ObjectOutputStream oos, String superpatron, String patron, String cadena, 
    									Object[] filtro, Object[] idLayers, 
    									Object[] idFeatures, Sesion userSesion, ConfigParameters configParameters) throws Exception{
           try{
               for (Iterator it=getVehiculos(superpatron, patron, cadena, 
            		   filtro, idLayers, idFeatures,null,
            		    userSesion,false,configParameters).iterator();it.hasNext();){
            	   VehiculoBean vehiculoBean=(VehiculoBean)it.next();
                   oos.writeObject(vehiculoBean);
               }
           }catch(Exception e){
               logger.error("returnVehiculos: "+ e.getMessage());
               oos.writeObject(new ACException(e));
               throw e;
           }
    }


    private Collection getInmuebles(String superpatron, String patron, String cadena, Object[] filtro,
    		Object[] idLayers, Object[] idFeatures, Object[] numInventarios,Sesion userSesion, boolean withAllData,
    		ConfigParameters configParameters) throws Exception {
        HashMap alRet= new HashMap();
        Vector vNumInventarios=new Vector();
        String sSQL= "";

        if (cadena==null || cadena.trim().equalsIgnoreCase("")){
            sSQL= getSelectInmueble(superpatron, patron, filtro, idLayers, idFeatures, userSesion, "");
            sSQL+= getSelectNumInventario(patron,numInventarios,TABLE_INVENTARIO_INMUEBLES);
            sSQL+= " ORDER BY id ASC, inmuebles.revision_actual DESC";
        }else{
            sSQL= getSelectBusquedaInmueble(superpatron, patron, cadena, idLayers, idFeatures, userSesion);
            sSQL+= getSelectNumInventario(patron,numInventarios,TABLE_INVENTARIO_INMUEBLES);
            sSQL+= " ORDER BY id ASC, revision_actual DESC";
        }

        //Parametros de LIMIT y OFFSET.
        if (numInventarios==null){
	        if (configParameters.getLimit()!=-1)
	        	sSQL+=" LIMIT "+configParameters.getLimit();
	        if (configParameters.getOffset()!=-1)
	        	sSQL+=" OFFSET "+configParameters.getOffset();
        }
        
        logger.info("Sentencia getInmuebles:"+sSQL);
        Connection conn=null;
        PreparedStatement ps= null;
        ResultSet rs= null;
        try{
            conn= CPoolDatabase.getConnection();
            ps= conn.prepareStatement(sSQL);
            rs= ps.executeQuery();
            InmuebleBean inmueble= null;
            long anteriorId = -1;
            long revisionAnterior = -1;
            boolean soloMostrarVersionado=false;
            while (rs.next()){
            	
        		inmueble= new InmuebleBean();
				inmueble.setId(rs.getLong("ID"));
				inmueble.setNumInventario(rs.getString("NUMINVENTARIO"));
				inmueble.setOrganizacion(rs.getString("ORGANIZACION"));
				inmueble.setBorrado(rs.getString("BORRADO"));
				inmueble.setBloqueado(rs.getString("BLOQUEADO"));
				inmueble.setFechaAlta(rs.getTimestamp("FECHA_ALTA"));
                inmueble.setRevisionActual(rs.getLong("REVISION_ACTUAL"));
                inmueble.setRevisionExpirada(rs.getLong("REVISION_EXPIRADA"));
				inmueble.setFechaAprobacionPleno(rs.getTimestamp("FECHA_APROBACION_PLENO"));
    			inmueble.setFechaBaja(rs.getTimestamp("FECHA_BAJA"));
				inmueble.setFechaUltimaModificacion(rs.getTimestamp("FECHA_ULTIMA_MODIFICACION"));
				inmueble.setNombre(rs.getString("NOMBRE"));
				inmueble.setUso(rs.getString("USO"));
				inmueble.setDescripcion(rs.getString("DESCRIPCION"));
				inmueble.setTipo(rs.getString("TIPO"));
//                inmueble.setIdFeatures((getFeaturesInventario(inmueble.getId(), conn,rs.getString("REVISION_ACTUAL"))));
//                inmueble.setIdLayers((getLayersInventario(inmueble.getId(), conn,rs.getString("REVISION_ACTUAL"))));
				inmueble.setAdquisicion(rs.getString("ADQUISICION"));
				inmueble.setFechaAdquisicion(rs.getTimestamp("FECHA_ADQUISICION"));
				inmueble.setDestino(rs.getString("DESTINO"));
				inmueble.setDireccion(rs.getString("DIRECCION"));
				inmueble.setPropiedad(rs.getString("PROPIEDAD"));
				inmueble.setLinderoNorte(rs.getString("LINDERO_NORTE"));
				inmueble.setLinderoSur(rs.getString("LINDERO_SUR"));
				inmueble.setLinderoEste(rs.getString("LINDERO_ESTE"));
				inmueble.setLinderoOeste(rs.getString("LINDERO_OESTE"));
				inmueble.setPatrimonioMunicipalSuelo(rs.getString("PATRIMONIO_MUNICIPAL_SUELO"));
				inmueble.setIdMunicipio(rs.getString("ID_MUNICIPIO"));
				inmueble.setFechaUltimaModificacion(rs.getTimestamp("FECHA_ULTIMA_MODIFICACION"));
				inmueble.setRefCatastral(rs.getString("REF_CATASTRAL"));
				inmueble.setRegistroNotario(rs.getString("REGISTRO_NOTARIO"));
				inmueble.setRegistroProtocolo(rs.getString("REGISTRO_PROTOCOLO"));
				inmueble.setRegistroPropiedad(rs.getString("REGISTRO_PROPIEDAD"));
				inmueble.setRegistroTomo(rs.getString("REGISTRO_TOMO"));
				inmueble.setRegistroLibro(rs.getString("REGISTRO_LIBRO"));
				inmueble.setRegistroFolio(rs.getString("REGISTRO_FOLIO"));
				inmueble.setRegistroFinca(rs.getString("REGISTRO_FINCA"));
				inmueble.setRegistroInscripcion(rs.getString("REGISTRO_INSCRIPCION"));
				inmueble.setRegistroDesc(rs.getString("REGISTRO"));
				inmueble.setCalificacion(rs.getString("CALIFICACION"));
				inmueble.setFechaObra(rs.getTimestamp("FECHAOBRA"));
				inmueble.setNumeroOrden(rs.getString("numero_orden"));
				inmueble.setNumeroPropiedad(rs.getString("numero_propiedad"));
				inmueble.setAutor(rs.getString("NAME"));
				inmueble.setFechaVersion(rs.getTimestamp("FECHA"));
				
				try{inmueble.setEdificabilidad(Double.parseDouble(rs.getString("EDIFICABILIDAD")));}catch(Exception e){}
				
				inmueble.setNumPlantas(rs.getString("NUM_PLANTAS"));
				inmueble.setEstadoConservacion(rs.getString("ESTADOCONSERVACION"));
				inmueble.setEstadoConservacionDesc(rs.getString("ESTADOCONSERVACION_DESC"));
				inmueble.setTipoConstruccion(rs.getString("TIPOCONSTRUCCION"));
				inmueble.setTipoConstruccionDesc(rs.getString("TIPOCONSTRUCCION_DESC"));
				inmueble.setFachada(rs.getString("FACHADA"));
				inmueble.setFachadaDesc(rs.getString("FACHADA_DESC"));
				inmueble.setCubierta(rs.getString("CUBIERTA"));
				inmueble.setCubiertaDesc(rs.getString("CUBIERTA_DESC"));
				inmueble.setCarpinteria(rs.getString("CARPINTERIA"));
				inmueble.setCarpinteriaDesc(rs.getString("CARPINTERIA_DESC"));
				inmueble.setClase(rs.getString("clase"));
				try{inmueble.setSuperficieRegistralSuelo(Double.parseDouble(rs.getString("SUPERFICIE_REGISTRAL_SUELO")));}catch(Exception e){}
				try{inmueble.setSuperficieCatastralSuelo(Double.parseDouble(rs.getString("SUPERFICIE_CATASTRAL_SUELO")));}catch(Exception e){}
				try{inmueble.setSuperficieRealSuelo(Double.parseDouble(rs.getString("SUPERFICIE_REAL_SUELO")));}catch(Exception e){}
				try{inmueble.setFechaAdquisicionSuelo(rs.getTimestamp("FECHA_ADQUISICION_SUELO"));}catch(Exception e){}
				try{inmueble.setValorAdquisicionSuelo(Double.parseDouble(rs.getString("VALOR_ADQUISICION_SUELO")));}catch(Exception e){}
				try{inmueble.setValorCatastralSuelo(Double.parseDouble(rs.getString("VALOR_CATASTRAL_SUELO")));}catch(Exception e){}
				try{inmueble.setValorActualSuelo(Double.parseDouble(rs.getString("VALOR_ACTUAL_SUELO")));}catch(Exception e){}
				try{inmueble.setSuperficieOcupadaConstruccion(Double.parseDouble(rs.getString("SUPERFICIE_OCUPADA_CONSTRUCCION")));}catch(Exception e){}
				try{inmueble.setSuperficieEnPlantaConstruccion(Double.parseDouble(rs.getString("SUPERFICIE_ENPLANTA_CONSTRUCCION")));}catch(Exception e){}
				try{inmueble.setSuperficieConstruidaConstruccion(Double.parseDouble(rs.getString("SUPERFICIE_CONSTRUIDA_CONSTRUCCION")));}catch(Exception e){}
				try{inmueble.setValorAdquisicionConstruccion(Double.parseDouble(rs.getString("VALOR_ADQUISICION_CONSTRUCCION")));}catch(Exception e){}
				try{inmueble.setValorCatastralConstruccion(Double.parseDouble(rs.getString("VALOR_CATASTRAL_CONSTRUCCION")));}catch(Exception e){}
				try{inmueble.setValorActualConstruccion(Double.parseDouble(rs.getString("VALOR_ACTUAL_CONSTRUCCION")));}catch(Exception e){}
				try{inmueble.setValorAdquisicionInmueble(Double.parseDouble(rs.getString("VALOR_ADQUISICION_INMUEBLE")));}catch(Exception e){}
				try{inmueble.setValorActualInmueble(Double.parseDouble(rs.getString("VALOR_ACTUAL_INMUEBLE")));}catch(Exception e){}
				try{inmueble.setValorCatastralInmueble(Double.parseDouble(rs.getString("VALOR_CATASTRAL_INMUEBLE")));}catch(Exception e){}
				try{inmueble.setAnioValorCatastral(Integer.parseInt(rs.getString("ANIO_VALOR_CATASTRAL")));}catch(Exception e){}
				inmueble.setEdificabilidadDesc(rs.getString("EDIFICABILIDAD_DESCRIPCION"));
				try{inmueble.setFechaAdquisicionObra(rs.getTimestamp("FECHA_ADQUISICION_OBRA"));}catch(Exception e){}
				try{
					inmueble.setBic(rs.getBoolean("BIC"));
					inmueble.setCatalogado(rs.getBoolean("CATALOGADO"));
				}catch(Exception e){
					logger.error("Se ha producido un error en la lectura del inmueble");
					e.printStackTrace();
				}
				
				inmueble.setDerechosRealesFavor(rs.getString("DERECHOSREALESFAVOR"));
				inmueble.setDerechosRealesContra(rs.getString("DERECHOSREALESCONTRA"));
				inmueble.setDerechosPersonales(rs.getString("DERECHOSPERSONALES"));
				inmueble.setValorDerechosFavor(rs.getDouble("VALOR_DERECHOS_FAVOR"));
				inmueble.setValorDerechosContra(rs.getDouble("VALOR_DERECHOS_CONTRA"));
				inmueble.setFrutos(rs.getString("FRUTOS"));
				inmueble.setImporteFrutos(rs.getDouble("IMPORTE_FRUTOS"));
				
				if (patron.equalsIgnoreCase(Const.PATRON_INMUEBLES_URBANOS)){

					InmuebleUrbanoBean iu= new InmuebleUrbanoBean();
					iu.setManzana(rs.getString("MANZANA"));
					iu.setSueloAncho(rs.getDouble("ANCHOSUPERF"));
					iu.setSueloLong(rs.getDouble("LONGSUPERF"));
					iu.setSueloMetrosPav(rs.getDouble("METRPAVSUPERF"));
					iu.setSueloMetrosNoPav(rs.getDouble("METRNOPAVSUPERF"));
					inmueble.setInmuebleUrbano(iu);

				}else if (patron.equalsIgnoreCase(Const.PATRON_INMUEBLES_RUSTICOS)){

					InmuebleRusticoBean ir= new InmuebleRusticoBean();
					ir.setAprovechamiento(rs.getString("APROVECHAMIENTO"));
					ir.setParaje(rs.getString("PARAJE"));
					ir.setPoligono(rs.getString("POLIGONO"));
					ir.setSueloAncho(rs.getDouble("ANCHOSUPERF"));
					ir.setSueloLong(rs.getDouble("LONGSUPERF"));
					ir.setSueloMetrosPav(rs.getDouble("METRPAVSUPERF"));
					ir.setSueloMetrosNoPav(rs.getDouble("METRNOPAVSUPERF"));
					inmueble.setInmuebleRustico(ir);
				}
				inmueble.setIdFeatures((getFeaturesInventario(inmueble.getId(), conn, rs.getString("REVISION_ACTUAL"))));
	            inmueble.setIdLayers((getLayersInventario(inmueble.getId(), conn, rs.getString("REVISION_ACTUAL"))));
				if (withAllData){
					try{inmueble.setCuentaContable( getCuentaContable(rs.getInt("ID_CUENTA_CONTABLE")));}catch(Exception e){}
					try{inmueble.setCuentaAmortizacion(getCuentaAmortizacion(rs.getInt("ID_CUENTA_AMORTIZACION")));}catch(Exception e){}
					//inmueble.setIdFeatures((getFeaturesInventario(inmueble.getId(), conn, rs.getString("REVISION_ACTUAL"))));
		            //inmueble.setIdLayers((getLayersInventario(inmueble.getId(), conn, rs.getString("REVISION_ACTUAL"))));
					inmueble.setSeguro(getSeguro(rs.getObject("ID_SEGURO")));
					inmueble.setObservaciones(getObservaciones(inmueble.getId(), TABLA_OBSERVACION_INVENTARIO, conn,rs.getLong("REVISION_ACTUAL")));
					inmueble.setMejoras(getMejoras(inmueble.getId(), conn,rs.getLong("REVISION_ACTUAL")));
					inmueble.setReferenciasCatastrales(getRefCatastrales(inmueble.getId(), conn,rs.getLong("REVISION_ACTUAL")));
					inmueble.setUsosFuncionales(getUsosFuncionales(inmueble.getId(), conn,rs.getLong("REVISION_ACTUAL")));
					String[] filtroRevertibles ={" and bi.id='"+inmueble.getId()+"' and  bien_revertible.revision_expirada = 9999999999 "};
					inmueble.setBienesRevertibles(getBienesRevertibles(null, null, filtroRevertibles, null,userSesion, false,new ConfigParameters()));
				}
				 if (new Long(inmueble.getId()) == anteriorId){
	                	if(revisionAnterior != inmueble.getRevisionActual() && !soloMostrarVersionado){
	                		inmueble.setVersionado(true);
		                    alRet.put(inmueble.getId()+"_"+inmueble.getRevisionActual(), inmueble);
		                    revisionAnterior = inmueble.getRevisionActual();
	                	}
	             }else{
	            	 if(idFeatures.length>0){	
	            		 //filtra las versiones si se selecciona una version anterior.
	            		 if(rs.getString("feature_revision_actual")!=null && inmueble.getRevisionActual()<=rs.getLong("feature_revision_actual") ){
	            			 
		                		inmueble.setVersionado(false);
		                		alRet.put(inmueble.getId()+"_"+inmueble.getRevisionActual(), inmueble);
		                		anteriorId = new Long(inmueble.getId());
		                		revisionAnterior = inmueble.getRevisionActual();  
		                		if(rs.getLong("feature_revision_actual")==rs.getLong("revision"))
		                			soloMostrarVersionado=true;
		                }     
		            	 //-----NUEVO---->    
	            		 else{
	            			 	inmueble.setVersionado(false);
			                    alRet.put(inmueble.getId()+"_"+inmueble.getRevisionActual(), inmueble);
			                    anteriorId = new Long(inmueble.getId());
			                    revisionAnterior = inmueble.getRevisionActual(); 
	            		 }
		            	 //---FIN NUEVO-->
			         }else{
			            	inmueble.setVersionado(false);
		                    alRet.put(inmueble.getId()+"_"+inmueble.getRevisionActual(), inmueble);
		                    anteriorId = new Long(inmueble.getId());
		                    revisionAnterior = inmueble.getRevisionActual();
			         }
	                	
	            }
	                
                /*
                inmueble= new InmuebleBean();
                inmueble.setId(rs.getLong("ID"));
                inmueble.setNumInventario(rs.getString("NUMINVENTARIO"));
                inmueble.setBorrado(rs.getString("BORRADO"));
                inmueble.setFechaAlta(rs.getTimestamp("FECHA_ALTA"));
                inmueble.setFechaAprobacionPleno(rs.getTimestamp("FECHA_APROBACION_PLENO"));
                inmueble.setFechaUltimaModificacion(rs.getTimestamp("FECHA_ULTIMA_MODIFICACION"));
                inmueble.setNombre(rs.getString("NOMBRE"));
                inmueble.setUso(rs.getString("USO"));
                inmueble.setDescripcion(rs.getString("DESCRIPCION"));
                inmueble.setTipo(rs.getString("TIPO"));
                inmueble.setRefCatastral(rs.getString("REF_CATASTRAL"));
                inmueble.setNumeroOrden(rs.getString("NUMERO_ORDEN"));
                inmueble.setNumeroPropiedad(rs.getString("NUMERO_PROPIEDAD"));                
                inmueble.setIdFeatures((getFeaturesInventario(inmueble.getId(), conn)));
                inmueble.setIdLayers((getLayersInventario(inmueble.getId(), conn)));
                inmueble.setRevisionActual(rs.getLong("REVISION_ACTUAL"));
                inmueble.setAdquisicion(rs.getString("ADQUISICION"));
                inmueble.setDestino(rs.getString("DESTINO"));
                inmueble.setDireccion(rs.getString("DIRECCION"));
                inmueble.setPropiedad(rs.getString("PROPIEDAD"));
                inmueble.setLinderoNorte(rs.getString("LINDERO_NORTE"));
                inmueble.setLinderoSur(rs.getString("LINDERO_SUR"));
                inmueble.setLinderoEste(rs.getString("LINDERO_ESTE"));
                inmueble.setLinderoOeste(rs.getString("LINDERO_OESTE"));
                inmueble.setFechaAdquisicion(rs.getTimestamp("FECHA_ADQUISICION"));
                inmueble.setRevisionExpirada(rs.getLong("REVISION_EXPIRADA"));
                inmueble.setAutor(rs.getString("NAME"));
                inmueble.setFechaVersion(rs.getTimestamp("FECHA"));
                inmueble.setPatrimonioMunicipalSuelo(rs.getString("PATRIMONIO_MUNICIPAL_SUELO"));

                if (new Long(inmueble.getId()) == anteriorId){
                	if(revisionAnterior != inmueble.getRevisionActual()){
                		inmueble.setVersionado(true);
	                    alRet.put(inmueble.getId()+"_"+inmueble.getRevisionActual(), inmueble);
	                    revisionAnterior = inmueble.getRevisionActual();
                	}
                }else{
                	inmueble.setVersionado(false);
                    alRet.put(inmueble.getId()+"_"+inmueble.getRevisionActual(), inmueble);
                    anteriorId = new Long(inmueble.getId());
                    revisionAnterior = inmueble.getRevisionActual();
                }
                String[] filtroBienes ={" and bi.id='"+inmueble.getId()+"' "};
				inmueble.setBienesRevertibles(getBienesRevertibles(null, null, filtroBienes, userSesion, false));
				*/
//				 if (numInventarios==null)
//		                vNumInventarios.add(inmueble.getNumInventario());
//		             else
//		                inmueble.setVersionado(true);
				 if (numInventarios==null)
	                vNumInventarios.add(inmueble.getNumInventario());
				 //-----NUEVO----->
				 else if(inmueble.getRevisionExpirada()==9999999999L)
	            	 alRet.put(inmueble,inmueble);   
				 //---FIN NUEVO--->
	             else
	            	 inmueble.setVersionado(true);	 
	             
            }
        }catch(Exception ex){
        	logger.error("Error al ejecutar: "+sSQL);
        	throw ex;
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
        }
        
        //Añadimos los versionados al array Total y cambiamos la informacion de versionado
        if ((vNumInventarios.size()>0) && (!configParameters.isBusquedaIndividual())){
        	Collection elementosVersionados=getInmuebles(superpatron, patron, cadena,filtro, idLayers, idFeatures,vNumInventarios.toArray(),
        							userSesion,withAllData,configParameters);
        	Iterator it=elementosVersionados.iterator();
        	while (it.hasNext()){
        		InmuebleBean inmuebleBean=(InmuebleBean)it.next();
        		//String clave=vehiculo.getId()+"_"+vehiculo.getRevisionActual();
        		alRet.put(inmuebleBean,inmuebleBean);	
        	}        	
        }

        return alRet.values();
    }

    private CuentaContable insertarCuentaContable(CuentaContable cc, Connection conn) throws Exception{
        if (cc==null) return null;
        String sSQL= "INSERT INTO CONTABLE (ID_CLASIFICACION, CUENTA, DESCRIPCION) VALUES (?,?,?)";

        PreparedStatement ps= null;
        try {
            Object obj= comprobarCuenta(cc);
            if ((obj != null) && (((CuentaContable)obj).getId() != -1)) return (CuentaContable)obj;
            
            long idCuentaContable= CPoolDatabase.getNextValue("contable","id_clasificacion");
            ps= conn.prepareStatement(sSQL);
            ps.setLong(1, idCuentaContable);
            ps.setString(2, cc.getCuenta());
            ps.setString(3, cc.getDescripcion());
            ps.execute();
            /** actualizamos la cuenta contable */
            cc.setId(idCuentaContable);
            
        }catch (Exception e){
        	logger.error("Error al ejecutar: "+sSQL);
            throw e;
        }finally{
            try{ps.close();}catch(Exception e){};
        }

        return cc;
    }

    private CuentaAmortizacion actualizarCuentaAmortizacion(CuentaAmortizacion ca, Connection conn) throws Exception{
        if (ca == null) return null;
        Object obj= comprobarCuenta(ca);
        if ((obj != null) && (((CuentaAmortizacion)obj).getId() != -1)){
            return updateCuentaAmortizacion(ca, conn);
        }else return insertarCuentaAmortizacion(ca, conn);
    }

    private CuentaAmortizacion updateCuentaAmortizacion(CuentaAmortizacion ca, Connection conn) throws Exception{
        if (ca==null) return null;
        String sSQL= "UPDATE AMORTIZACION SET ANIOS=?, PORCENTAJE=?, TIPO_AMORTIZACION=?, TOTAL_AMORTIZADO=? WHERE ID=?";

        PreparedStatement ps= null;
		try {
            ps= conn.prepareStatement(sSQL);
            ps.setInt(1, ca.getAnnos());
            ps.setDouble(2, ca.getPorcentaje());
            ps.setString(3, ca.getTipoAmortizacion());
            ps.setDouble(4, ca.getTotalAmortizado());
            ps.setLong(5, ca.getId());
            ps.execute();
        }catch (Exception e){
            throw e;
        }finally{
            try{ps.close();}catch(Exception e){};
        }
        return ca;
    }


    private CuentaAmortizacion insertarCuentaAmortizacion(CuentaAmortizacion ca, Connection conn) throws Exception{
        if (ca==null) return null;
        String sSQL= "INSERT INTO AMORTIZACION(ID, CUENTA, DESCRIPCION, PORCENTAJE, ANIOS, TIPO_AMORTIZACION, TOTAL_AMORTIZADO) VALUES(?,?,?,?,?,?,?)";

        PreparedStatement ps= null;
      	try {
                long id= CPoolDatabase.getNextValue("amortizacion", "id");
                ps= conn.prepareStatement(sSQL);
                ps.setLong(1, id);
                ps.setString(2, ca.getCuenta());
                ps.setString(3, ca.getDescripcion());
                ps.setDouble(4, ca.getPorcentaje());
                ps.setInt(5, ca.getAnnos());
                ps.setString(6, ca.getTipoAmortizacion());
                ps.setDouble(7, ca.getTotalAmortizado());
                ps.execute();
                /** actualizamos la cuenta amortizacion */
                ca.setId(id);
        }catch (Exception e){
           	logger.error("Error al ejecutar: "+sSQL);
            throw e;
        }finally{
            try{ps.close();}catch(Exception e){};
        }

        return ca;
    }

    /**
     * Comprueba si un numero de cuenta existe en BD
     * @param cuenta
     * @return si existe una cuenta con el mismo numero de cuenta, esa cuenta; si no una cuenta nueva
     * insertada en BD
     * @throws Exception
     */
    private Object comprobarCuenta(Object cuenta) throws Exception{
        if (cuenta == null) return null;
        String sSQL1= "SELECT * from amortizacion WHERE cuenta=?";
        String sSQL2= "SELECT * from contable WHERE cuenta=?";
        Connection conn=null;
        PreparedStatement ps= null;
        ResultSet rs= null;
        try{
            conn= CPoolDatabase.getConnection();
            if (cuenta instanceof CuentaContable){
                ps= conn.prepareStatement(sSQL2);
                ps.setString(1, ((CuentaContable)cuenta).getCuenta());
                rs= ps.executeQuery();
                if (rs.next()){
                   ((CuentaContable)cuenta).setId(rs.getInt("ID_CLASIFICACION"));
                   ((CuentaContable)cuenta).setDescripcion(rs.getString("DESCRIPCION"));
                }
            }else if (cuenta instanceof CuentaAmortizacion){
                ps= conn.prepareStatement(sSQL1);
                ps.setString(1, ((CuentaAmortizacion)cuenta).getCuenta());
                rs= ps.executeQuery();
                if (rs.next()){
                    ((CuentaAmortizacion)cuenta).setId(rs.getInt("ID"));
                    ((CuentaAmortizacion)cuenta).setDescripcion(rs.getString("DESCRIPCION"));
                   
                }
            }
        }catch(Exception ex){
        	logger.error("Error al ejecutar: "+sSQL2);
        	throw ex;
        } finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
        }
        return cuenta;
    }
    
    /**
     * Devuelve la cuenta contable
     * @param id
     * @return
     * @throws Exception
     */
    private CuentaContable getCuentaContable(long id) throws Exception{
        String sSQL= "SELECT * from contable WHERE id_clasificacion=?";
        Connection conn=null;
        PreparedStatement ps= null;
        ResultSet rs= null;
        CuentaContable cc= null;
        try{
            if (id == -1) return null;
            conn= CPoolDatabase.getConnection();
            ps= conn.prepareStatement(sSQL);
            ps.setLong(1, id);
            rs= ps.executeQuery();
            if (rs.next()){
                   cc= new CuentaContable();
                   cc.setId(id);
                   cc.setDescripcion(rs.getString("DESCRIPCION"));
                   cc.setCuenta(rs.getString("CUENTA"));
            }
            return cc;
       }catch(Exception ex){
            logger.error("Error al ejecutar: "+sSQL);
        	throw ex;
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
        }
    }
    /**
     * Devuelve la cuenta de amortización
     * @param id
     * @return
     * @throws Exception
     */
    private CuentaAmortizacion getCuentaAmortizacion(long id) throws Exception{
        String sSQL= "SELECT * from amortizacion WHERE id=?";
        Connection conn=null;
        PreparedStatement ps= null;
        ResultSet rs= null;
        CuentaAmortizacion ca= null;
        try{
            if (id == -1) return null;
            conn= CPoolDatabase.getConnection();
            ps= conn.prepareStatement(sSQL);
            ps.setLong(1, id);
            rs= ps.executeQuery();
            if (rs.next()){
                    ca= new CuentaAmortizacion();
                    ca.setId(id);
                    ca.setDescripcion(rs.getString("DESCRIPCION"));
                    ca.setCuenta(rs.getString("CUENTA"));
                    ca.setPorcentaje(rs.getDouble("PORCENTAJE"));
                    ca.setAnnos(rs.getInt("ANIOS"));
                    ca.setTipoAmortizacion(rs.getString("TIPO_AMORTIZACION"));
                    ca.setTotalAmortizado(rs.getDouble("TOTAL_AMORTIZADO"));
            }
            return ca;
        }catch(Exception ex){
            logger.error("Error al ejecutar: "+sSQL);
        	throw ex;
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
        }
    }


    private void deleteCuenta(Object cuenta, Connection conn) throws Exception{
        String sSQL1= "DELETE from amortizacion WHERE id=?";
        String sSQL2= "DELETE from contable WHERE id_clasificacion=?";
        PreparedStatement ps= null;
        try{
            if (cuenta instanceof CuentaContable) if (((CuentaContable)cuenta).getId()==-1) return;
            if (cuenta instanceof CuentaAmortizacion) if (((CuentaAmortizacion)cuenta).getId()==-1) return;

            if (cuenta instanceof CuentaContable){
                ps= conn.prepareStatement(sSQL2);
                ps.setLong(1, ((CuentaContable)cuenta).getId());
                ps.execute();
            }else if (cuenta instanceof CuentaAmortizacion){
                ps= conn.prepareStatement(sSQL1);
                ps.setLong(1, ((CuentaAmortizacion)cuenta).getId());
                ps.execute();
            }
        }finally{
            try{ps.close();}catch(Exception e){};
        }
    }

    private Object getCuentaFromBien(BienBean bien, int tipo, Connection conn) throws Exception{
        String campo="";
        if (tipo==CONTABLE) campo="ID_CUENTA_CONTABLE";
        else if (tipo==AMORTIZACION) campo="ID_CUENTA_AMORTIZACION";

        String SQL= "SELECT "+campo+" FROM BIENES_INVENTARIO WHERE ID="+bien.getId();

        PreparedStatement ps= null;
        ResultSet rs= null;
        try{
            ps= conn.prepareStatement(SQL);
            rs= ps.executeQuery();
            if (rs.next()){
            	if (tipo==CONTABLE)
            		return getCuentaContable(rs.getInt(campo));
            	if (tipo==AMORTIZACION)
            		return getCuentaAmortizacion(rs.getInt(campo));
            	
            }
        }catch (Exception ex){
        	logger.error("Error al ejecutar: "+SQL);
        	throw ex;
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
        }
        return null;
    }


    private boolean cuentaAsociadaOtroBien(BienBean bien, Object cuenta, Connection conn) throws Exception{
        if (cuenta == null) return true;
        String SQL= "SELECT * FROM BIENES_INVENTARIO WHERE ID<>"+bien.getId();

        if (cuenta instanceof CuentaContable) SQL+=" AND ID_CUENTA_CONTABLE="+((CuentaContable)cuenta).getId();
        else if (cuenta instanceof CuentaAmortizacion) SQL+=" AND ID_CUENTA_AMORTIZACION="+((CuentaAmortizacion)cuenta).getId();

        boolean b= false;
        PreparedStatement ps= null;
        ResultSet rs= null;
        try{
            ps= conn.prepareStatement(SQL);
            rs= ps.executeQuery();
            if (rs.next()) b= true;
        }catch(Exception ex){
        	logger.error("Error al ejecutar: "+SQL);
        	throw ex;
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
        }
        return b;
    }



    private void insertInmueble(Connection conn, InmuebleBean inmueble, String revision, Sesion userSesion) throws PermissionException,LockException,Exception{

        String inmueblesSQL= "INSERT INTO INMUEBLES(ID, ADQUISICION, FECHA_ADQUISICION, " +
        "DESTINO, DIRECCION, PROPIEDAD, LINDERO_NORTE, LINDERO_SUR, LINDERO_ESTE, LINDERO_OESTE, PATRIMONIO_MUNICIPAL_SUELO, REF_CATASTRAL, " +
        "REGISTRO_NOTARIO, REGISTRO_PROTOCOLO, REGISTRO_PROPIEDAD, REGISTRO_TOMO, REGISTRO_LIBRO, REGISTRO_FOLIO, REGISTRO_FINCA, REGISTRO_INSCRIPCION, " +
        "CALIFICACION, FECHAOBRA, EDIFICABILIDAD, NUM_PLANTAS, ESTADOCONSERVACION, ESTADOCONSERVACION_DESC, TIPOCONSTRUCCION, TIPOCONSTRUCCION_DESC, FACHADA, FACHADA_DESC, CUBIERTA, CUBIERTA_DESC, CARPINTERIA, CARPINTERIA_DESC, " +
        "SUPERFICIE_REGISTRAL_SUELO, SUPERFICIE_CATASTRAL_SUELO, SUPERFICIE_REAL_SUELO, FECHA_ADQUISICION_SUELO, VALOR_ADQUISICION_SUELO, VALOR_CATASTRAL_SUELO, VALOR_ACTUAL_SUELO, " +
        "SUPERFICIE_OCUPADA_CONSTRUCCION, SUPERFICIE_ENPLANTA_CONSTRUCCION, SUPERFICIE_CONSTRUIDA_CONSTRUCCION, VALOR_ADQUISICION_CONSTRUCCION, VALOR_CATASTRAL_CONSTRUCCION, VALOR_ACTUAL_CONSTRUCCION, " +
        "VALOR_ADQUISICION_INMUEBLE, VALOR_ACTUAL_INMUEBLE, " +
        "DERECHOSREALESFAVOR, DERECHOSREALESCONTRA, DERECHOSPERSONALES, VALOR_DERECHOS_FAVOR, VALOR_DERECHOS_CONTRA, FRUTOS, IMPORTE_FRUTOS, NUMERO_ORDEN, NUMERO_PROPIEDAD, REVISION_ACTUAL," +
        "REGISTRO,valor_catastral_inmueble,anio_valor_catastral,edificabilidad_descripcion,fecha_adquisicion_obra, bic, catalogado, clase) " +
        "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"+revision+",?,?,?,?,?,?,?,?)";
        PreparedStatement ps= null;
       if (inmueble == null) return;
        ps= conn.prepareStatement(inmueblesSQL);
        ps.setLong(1, inmueble.getId());
        ps.setString(2, inmueble.getAdquisicion());
        if (inmueble.getFechaAdquisicion() == null)
            ps.setNull(3, java.sql.Types.TIMESTAMP);
        else ps.setTimestamp(3, new Timestamp(inmueble.getFechaAdquisicion().getTime()));
        ps.setString(4, inmueble.getDestino());
        ps.setString(5, inmueble.getDireccion());
        ps.setString(6, inmueble.getPropiedad());
        ps.setString(7, inmueble.getLinderoNorte());
        ps.setString(8, inmueble.getLinderoSur());
        ps.setString(9, inmueble.getLinderoEste());
        ps.setString(10, inmueble.getLinderoOeste());
        ps.setString(11, inmueble.getPatrimonioMunicipalSuelo()?"1":"0");
        ps.setString(12, inmueble.getRefCatastral());
        ps.setString(13, inmueble.getRegistroNotario());
        ps.setString(14, inmueble.getRegistroProtocolo());
        ps.setString(15, inmueble.getRegistroPropiedad());
        ps.setString(16, inmueble.getRegistroTomo());
        ps.setString(17, inmueble.getRegistroLibro());
        ps.setString(18, inmueble.getRegistroFolio());
        ps.setString(19, inmueble.getRegistroFinca());
        ps.setString(20, inmueble.getRegistroInscripcion());
        ps.setString(21, inmueble.getCalificacion());
        if (inmueble.getFechaObra() == null)
            ps.setNull(22, java.sql.Types.TIMESTAMP);
        else ps.setTimestamp(22, new Timestamp(inmueble.getFechaObra().getTime()));
        if (inmueble.getEdificabilidad()==null)
        	ps.setNull(23, java.sql.Types.DOUBLE);
        else
        	ps.setDouble(23, new Double(inmueble.getEdificabilidad()).doubleValue());
        ps.setString(24, inmueble.getNumPlantas());
        ps.setString(25, inmueble.getEstadoConservacion());
        ps.setString(26, inmueble.getEstadoConservacionDesc());
        ps.setString(27, inmueble.getTipoConstruccion());
        ps.setString(28, inmueble.getTipoConstruccionDesc());
        ps.setString(29, inmueble.getFachada());
        ps.setString(30, inmueble.getFachadaDesc());
        ps.setString(31, inmueble.getCubierta());
        ps.setString(32, inmueble.getCubiertaDesc());
        ps.setString(33, inmueble.getCarpinteria());
        ps.setString(34, inmueble.getCarpinteriaDesc());
        ps.setDouble(35, new Double(inmueble.getSuperficieRegistralSuelo()).doubleValue());
        ps.setDouble(36, new Double(inmueble.getSuperficieCatastralSuelo()).doubleValue());
        ps.setDouble(37, new Double(inmueble.getSuperficieRealSuelo()).doubleValue());
        if (inmueble.getFechaAdquisicionSuelo() == null)
        	ps.setNull(38, java.sql.Types.TIMESTAMP);
        else ps.setTimestamp(38, new Timestamp(inmueble.getFechaAdquisicionSuelo().getTime()));
        ps.setDouble(39, new Double(inmueble.getValorAdquisicionSuelo()).doubleValue());
        ps.setDouble(40, new Double(inmueble.getValorCatastralSuelo()).doubleValue());
        ps.setDouble(41, new Double(inmueble.getValorActualSuelo()).doubleValue());
        ps.setDouble(42, new Double(inmueble.getSuperficieOcupadaConstruccion()).doubleValue());
        ps.setDouble(43, new Double(inmueble.getSuperficieEnPlantaConstruccion()).doubleValue());
        ps.setDouble(44, new Double(inmueble.getSuperficieConstruidaConstruccion()).doubleValue());
        ps.setDouble(45, new Double(inmueble.getValorAdquisicionConstruccion()).doubleValue());
        ps.setDouble(46, new Double(inmueble.getValorCatastralConstruccion()).doubleValue());
        ps.setDouble(47, new Double(inmueble.getValorActualConstruccion()).doubleValue());
        ps.setDouble(48, new Double(inmueble.getValorAdquisicionInmueble()).doubleValue());
        ps.setDouble(49, new Double(inmueble.getValorActualInmueble()).doubleValue());
        ps.setString(50, inmueble.getDerechosRealesFavor());
        ps.setString(51, inmueble.getDerechosRealesContra());
        ps.setString(52, inmueble.getDerechosPersonales());
        ps.setDouble(53, new Double(inmueble.getValorDerechosFavor()).doubleValue());
        ps.setDouble(54, new Double(inmueble.getValorDerechosContra()).doubleValue());
        ps.setString(55, inmueble.getFrutos());
        if (inmueble.getImporteFrutos()==null)
        	ps.setNull(56,java.sql.Types.DOUBLE );
        else
        	ps.setDouble(56, inmueble.getImporteFrutos());
        ps.setString(57,inmueble.getNumeroOrden());
        ps.setString(58,inmueble.getNumeroPropiedad());
        ps.setString(59,inmueble.getRegistroDesc());
        ps.setDouble(60,new Double(inmueble.getValorCatastralInmueble()));
        if (inmueble.getAnioValorCatastral()==null)
        	ps.setObject(61,null);
        else
        	ps.setInt(61, inmueble.getAnioValorCatastral());
        ps.setString(62, inmueble.getEdificabilidadDesc());
        if (inmueble.getFechaAdquisicionObra()==null)
        	ps.setObject(63, null);
        else 
        	ps.setTimestamp(63, new java.sql.Timestamp(inmueble.getFechaAdquisicionObra().getTime()));
               
		 ps.setBoolean(64,inmueble.isBic());
		 ps.setBoolean(65,inmueble.isCatalogado());
         ps.setString(66, inmueble.getClase());
        ps.execute();
        ps.close();
  
        insertVersion(conn, "inmuebles", userSesion);
    }
    
    private InmuebleBean insertInmueble(Object[] idLayers, Object[] idFeatures, 
    		InmuebleBean inmueble, Sesion userSesion) throws PermissionException,LockException,Exception{

      
        String inmurbanoSQL= "INSERT INTO INMUEBLES_URBANOS(ID, MANZANA, REVISION_ACTUAL, ANCHOSUPERF, LONGSUPERF, METRPAVSUPERF, METRNOPAVSUPERF) VALUES(?,?,"+sRevisionActual.replaceAll("\\?", "inmuebles")+",?,?,?,?)";

        String inmrusticoSQL= "INSERT INTO INMUEBLES_RUSTICOS(ID, POLIGONO, APROVECHAMIENTO, PARAJE,REVISION_ACTUAL, ANCHOSUPERF, LONGSUPERF, METRPAVSUPERF, METRNOPAVSUPERF) VALUES(?,?,?,?,"+sRevisionActual.replaceAll("\\?", "inmuebles")+",?,?,?,?)";


        Connection conn=null;
        PreparedStatement ps= null;
        ResultSet rs= null;
		try {

			if (inmueble == null) return null;
            conn = CPoolDatabase.getConnection();
            conn.setAutoCommit(false);

            inmueble.setIdFeatures(idFeatures);
            inmueble.setIdLayers(idLayers);
            
            insertarBienInventario(inmueble, userSesion, conn, sMaxRevision.replaceAll("\\?", "inmuebles"),sRevisionActual.replaceAll("\\?", "inmuebles"));

            insertInmueble(conn, inmueble,sRevisionActual.replaceAll("\\?", "inmuebles"),userSesion);

            /** Insertamos las features asociadas al inmueble */
            //insertInventarioFeature(inmueble.getId(), idLayers, idFeatures, sRevisionActual.replaceAll("\\?", "inmuebles"), userSesion, conn);
            


            if (inmueble.isUrbano()){
                ps= conn.prepareStatement(inmurbanoSQL);
                ps.setLong(1, inmueble.getId());
//                ps.setString(2, inmueble.getInmuebleUrbano()!=null?inmueble.getInmuebleUrbano().getManzana():null);
//       		 	ps.setDouble(3,inmueble.getInmuebleUrbano()!=null?inmueble.getInmuebleUrbano().getSueloAncho():null);
//       		 	ps.setDouble(4,inmueble.getInmuebleUrbano()!=null?inmueble.getInmuebleUrbano().getSueloLong():null);
//       		 	ps.setDouble(5,inmueble.getInmuebleUrbano()!=null?inmueble.getInmuebleUrbano().getSueloMetrosPav():null);
//       		 	ps.setDouble(6,inmueble.getInmuebleUrbano()!=null?inmueble.getInmuebleUrbano().getSueloMetrosNoPav():null);
       		 	ps.setString(2, inmueble.getInmuebleUrbano()!=null && inmueble.getInmuebleUrbano().getManzana()!=null?inmueble.getInmuebleUrbano().getManzana():"");
    		 	ps.setDouble(3,inmueble.getInmuebleUrbano()!=null?inmueble.getInmuebleUrbano().getSueloAncho():0);
    		 	ps.setDouble(4,inmueble.getInmuebleUrbano()!=null?inmueble.getInmuebleUrbano().getSueloLong():0);
    		 	ps.setDouble(5,inmueble.getInmuebleUrbano()!=null?inmueble.getInmuebleUrbano().getSueloMetrosPav():0);
    		 	ps.setDouble(6,inmueble.getInmuebleUrbano()!=null?inmueble.getInmuebleUrbano().getSueloMetrosNoPav():0);
                ps.execute();
                ps.close();
            }else if (inmueble.isRustico()){
                ps= conn.prepareStatement(inmrusticoSQL);
                ps.setLong(1, inmueble.getId());
//                ps.setString(2, inmueble.getInmuebleRustico()!=null?inmueble.getInmuebleRustico().getPoligono():null);
//                ps.setString(3, inmueble.getInmuebleRustico()!=null?inmueble.getInmuebleRustico().getAprovechamiento():null);
//                ps.setString(4, inmueble.getInmuebleRustico()!=null?inmueble.getInmuebleRustico().getParaje():null);
//       		 	ps.setDouble(5,inmueble.getInmuebleRustico()!=null?inmueble.getInmuebleRustico().getSueloAncho():null);
//       		 	ps.setDouble(6,inmueble.getInmuebleRustico()!=null?inmueble.getInmuebleRustico().getSueloLong():null);
//       		 	ps.setDouble(7,inmueble.getInmuebleRustico()!=null?inmueble.getInmuebleRustico().getSueloMetrosPav():null);
//       		 	ps.setDouble(8,inmueble.getInmuebleRustico()!=null?inmueble.getInmuebleRustico().getSueloMetrosNoPav():null);
       		    ps.setString(2, inmueble.getInmuebleRustico()!=null?inmueble.getInmuebleRustico().getPoligono():"");
                ps.setString(3, inmueble.getInmuebleRustico()!=null?inmueble.getInmuebleRustico().getAprovechamiento():"");
                ps.setString(4, inmueble.getInmuebleRustico()!=null?inmueble.getInmuebleRustico().getParaje():"");
       		 	ps.setDouble(5,inmueble.getInmuebleRustico()!=null?inmueble.getInmuebleRustico().getSueloAncho():0);
       		 	ps.setDouble(6,inmueble.getInmuebleRustico()!=null?inmueble.getInmuebleRustico().getSueloLong():0);
       		 	ps.setDouble(7,inmueble.getInmuebleRustico()!=null?inmueble.getInmuebleRustico().getSueloMetrosPav():0);
       		 	ps.setDouble(8,inmueble.getInmuebleRustico()!=null?inmueble.getInmuebleRustico().getSueloMetrosNoPav():0);
       		 	logger.info("SQL: "+inmrusticoSQL);
				logger.info("Parametros:::" + inmueble.getId() + ","
						+ inmueble.getInmuebleRustico() != null ? inmueble
						.getInmuebleRustico().getPoligono() : null + ","
						+ inmueble.getInmuebleRustico() != null ? inmueble
						.getInmuebleRustico().getAprovechamiento() : null + ","
						+ inmueble.getInmuebleRustico() != null ? inmueble
						.getInmuebleRustico().getParaje() : null + ","
						+ inmueble.getInmuebleRustico() != null ? inmueble
						.getInmuebleRustico().getSueloAncho() : null + ","
						+ inmueble.getInmuebleRustico() != null ? inmueble
						.getInmuebleRustico().getSueloLong() : null + ","
						+ inmueble.getInmuebleRustico() != null ? inmueble
						.getInmuebleRustico().getSueloMetrosPav() : null + ", "
						+ inmueble.getInmuebleRustico() != null ? inmueble
						.getInmuebleRustico().getSueloMetrosNoPav() : null);
                ps.execute();
                ps.close();
            }

            /** Actualizamos los ficheros en disco (temporal --> destino) */
            DocumentoEnDisco.actualizarConFicherosDeTemporal(((BienBean)inmueble).getDocumentos());
            inmueble.setDocumentos(null);
            conn.commit();

        }catch (PermissionException e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }catch (LockException e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }catch (Exception e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }/** java.lang.OutOfMemoryError */catch (java.lang.Error e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){}
        }

        return inmueble;
    }

    private BienRevertible insertBienRevertible(BienRevertible bienRevertible, Sesion userSesion,Connection conn) throws PermissionException,LockException,Exception{

    	String insertBienRevertible="INSERT INTO bien_revertible("+
            "id, num_inventario, fecha_inicio, fecha_vencimiento, fecha_transmision,"+ 
            "importe, poseedor, titulo_posesion, condiciones_reversion,"+ 
            "detalles, cat_transmision, id_cuenta_amortizacion, id_municipio, revision_actual, fecha_alta, fecha_ultima_modificacion," +
            "nombre, organizacion, fecha_aprobacion_pleno, descripcion_bien, fecha_adquisicion," +
            "adquisicion, diagnosis, patrimonio_municipal_suelo, clase, id_seguro)"+
    		"VALUES (?, ?, ?, ?, ?,?, ?, ?, ?,?, ?,?,?, "+sMaxRevision.replaceAll("\\?", "bien_revertible")+", ?,?,?,?,?,?,?,?,?,?,?,?)";
    	String insertBienes= "INSERT INTO bien_revertible_bien("+
                "id_bien, id_bien_revertible, revision)  VALUES (?, ?, "+sRevisionActual.replaceAll("\\?", "bien_revertible")+")";
    	
    	String revisionExpirada= "UPDATE bien_revertible SET REVISION_EXPIRADA="+sRevisionActual.replaceAll("\\?", "bien_revertible" +
    			"")+
           " WHERE ID=? and REVISION_ACTUAL<>"+sRevisionActual.replaceAll("\\?", "bien_revertible" ) +
           sRevisionExpiradaNula;
    	
         boolean hacerCommit=conn==null;    	

    	
        PreparedStatement ps= null;
        try {
			if (conn==null){
				conn = CPoolDatabase.getConnection();
				conn.setAutoCommit(false);
			}
			/*insertamos primero el bien revertible*/  
			if (bienRevertible == null) return null;
			
			//Eliminamos la comprobación de que tiene que estar asignado a una lista de bienes
			//if (bienRevertible.getBienes()==null || bienRevertible.getBienes().size()==0)
			//	throw new ACException("Debe especificarse una lista de bienes a los que pertenece el bien revertible");
			
            
			 /* Insertamos la cuenta de amortizacion */
            if (bienRevertible.getCuentaAmortizacion()!=null)
            {
            	bienRevertible.setCuentaAmortizacion(actualizarCuentaAmortizacion(bienRevertible.getCuentaAmortizacion(), conn));
            }
            
            /** seguro */
            if (bienRevertible.getSeguro()!=null){
                if (bienRevertible.getSeguro().getId()==null || bienRevertible.getSeguro().getId()==-1) bienRevertible.setSeguro(insertarSeguro(bienRevertible.getSeguro(), conn));
                else updateSeguro(bienRevertible.getSeguro(), conn);
            }

            long idBienRevertible=0;
            String numInventario="";
            if (bienRevertible.getId()==null || bienRevertible.getId().longValue()<0){
            	idBienRevertible=CPoolDatabase.getNextValue("bien_revertible", "id");
            	bienRevertible.setId(idBienRevertible);

            	//Puede que en la carga de inventario venga con el numero de inventario
            	if (bienRevertible.getNumInventario()!=null && bienRevertible.getNumInventario().length()>0){
            		numInventario=bienRevertible.getNumInventario();
            		numInventario=getCodNumInventario(bienRevertible, numInventario);
            	}
            	else{
            		boolean existe = true;            	
            		while (existe){            			
		            	/** Formamos el numero de inventario */
		            	//numInventario= new Long(idBienRevertible).toString();
		            	// Generamos el numero de inventario especifico para bienes revertibles y municipio
						numInventario= new Long(CPoolDatabase.getNext("bienes_inventario","bienrevertible", conn, Integer.parseInt(municipio), true)).toString();
						numInventario=getCodNumInventario(bienRevertible, numInventario);
						if (!existeNumInventario(numInventario, Integer.parseInt(municipio)))
							existe = false;
	            	}
	            }
            }else{
            	logger.info("Actualizando Bien revertible:"+bienRevertible.getNumInventario());
            	idBienRevertible=bienRevertible.getId().longValue();
            	numInventario = bienRevertible.getNumInventario();
            }
                      
            ps= conn.prepareStatement(insertBienRevertible);
            ps.setLong(1, idBienRevertible);
           
            ps.setString(2, numInventario);
            if (bienRevertible.getFechaInicio()!=null)
            	ps.setDate(3,new java.sql.Date(bienRevertible.getFechaInicio().getTime()));
            else
            	ps.setObject(3, null);
            if (bienRevertible.getFechaVencimiento()!=null)
            	ps.setDate(4,new java.sql.Date(bienRevertible.getFechaVencimiento().getTime()));
            else
            	ps.setObject(4, null);
            if (bienRevertible.getFechaTransmision()!=null)
            	ps.setDate(5,new java.sql.Date(bienRevertible.getFechaTransmision().getTime()));
            else
            	ps.setObject(5, null);
            if (bienRevertible.getImporte()!=null)
            	ps.setDouble(6, bienRevertible.getImporte());
            else
            	ps.setObject(6,null);
            ps.setString(7, bienRevertible.getPoseedor());
            ps.setString(8, bienRevertible.getTituloPosesion());
            ps.setString(9, bienRevertible.getCondicionesReversion());
            ps.setString(10, bienRevertible.getDetalles());
            ps.setString(11, bienRevertible.getCatTransmision());
            if (bienRevertible.getCuentaAmortizacion()!=null && bienRevertible.getCuentaAmortizacion().getId()!=-1){
            	ps.setLong(12, bienRevertible.getCuentaAmortizacion().getId());
            }else{
            	ps.setObject(12, null);
            }
            ps.setInt(13,Integer.parseInt(municipio));
            //En caso de que venga de una carga o una modificacion
            java.util.Date dateAlta= new java.util.Date();
            if (bienRevertible.getFechaAlta()!=null)
            	dateAlta = bienRevertible.getFechaAlta();
            ps.setDate(14, new java.sql.Date(dateAlta.getTime()));
            if (bienRevertible.getFechaUltimaModificacion()!=null)
            	ps.setDate(15, new java.sql.Date(bienRevertible.getFechaUltimaModificacion().getTime()));
            else
              	ps.setObject(15, null);
            ps.setString(16, bienRevertible.getNombre());
            ps.setString(17,bienRevertible.getOrganizacion());
            if (bienRevertible.getFecha_aprobacion_pleno()!=null)
            	ps.setDate(18, new java.sql.Date(bienRevertible.getFecha_aprobacion_pleno().getTime()));
            else
              	ps.setObject(18, null);
            ps.setString(19,bienRevertible.getDescripcion_bien());
            if (bienRevertible.getFecha_adquisicion()!=null)
            	ps.setDate(20, new java.sql.Date(bienRevertible.getFecha_adquisicion().getTime()));
            else
              	ps.setObject(20, null);
            ps.setString(21,bienRevertible.getAdquisicion());
            ps.setString(22,bienRevertible.getDiagnosis());
            ps.setString(23,bienRevertible.isPatrimonioMunicipalSuelo()?"1":"0");
            ps.setString(24,bienRevertible.getClase());
            if (bienRevertible.getSeguro()!=null && bienRevertible.getSeguro().getId()!=null)
            	ps.setLong(25,bienRevertible.getSeguro().getId());
            else
            	ps.setObject(25,null);
            ps.execute();
            ps.close();
            /*insertamos ahora los bienes asociados*/
            if (bienRevertible.getBienes()!=null)
            for (Iterator<BienBean> it=bienRevertible.getBienes().iterator();it.hasNext();){
            	BienBean bien= (BienBean)it.next();
            	ps= conn.prepareStatement(insertBienes);
                ps.setLong(1,bien.getId());
            	ps.setLong(2,idBienRevertible);
                ps.execute();
                ps.close();
            }
            /* Insertamos las observaciones del bien */
            if (bienRevertible.getObservaciones() != null){
                for(Iterator<Observacion> it=bienRevertible.getObservaciones().iterator();it.hasNext();){
                   insertObservacion(bienRevertible.getId(), (Observacion)it.next(),"observaciones_bien_revertible", conn, sRevisionActual.replaceAll("\\?", "bien_revertible"));
               }
            }
            /* Actualizamos los documentos */
            /** Insertamos la documentacion en BD */
          	DocumentConnection docConn= new DocumentConnection(srid);
            if (bienRevertible.getDocumentos()!=null){
            	bienRevertible.setDocumentos(docConn.updateDocumentsInventario(bienRevertible, bienRevertible.getDocumentos().toArray(), userSesion, conn, municipio));
            	docsBorrados=docConn.getDocsBorrados();
            }
            
            /*actualizamos la revision expirada*/
            ps= conn.prepareStatement(revisionExpirada);
            ps.setLong(1, idBienRevertible);
            ps.execute();
            ps.close();
            insertVersion(conn, "bien_revertible", userSesion);
            if (hacerCommit) conn.commit();
		   	DocumentoEnDisco.actualizarConFicherosDeTemporal(bienRevertible.getDocumentos());
		   	docConn.borrarFicherosEnDisco(bienRevertible.getId(), docConn.getDocsBorrados());
		    bienRevertible.setFechaAlta(dateAlta);
		    bienRevertible.setDocumentos(null);
        }catch (PermissionException e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }catch (LockException e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }catch (Exception e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }/** java.lang.OutOfMemoryError */catch (java.lang.Error e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }finally{
            try{ps.close();}catch(Exception e){};
            if (hacerCommit)
            	try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){}
        }

        return bienRevertible;
    }
    
    /***
     * Actualiza los datos de un Bien Revertible
     * @param bienRevertible
     * @return
     * @throws PermissionException
     * @throws LockException
     * @throws Exception
     * @deprecated Al versionarse ya no se usa
     * 
     */
    private BienRevertible updateBienRevertible(BienRevertible bienRevertible) throws PermissionException,LockException,Exception{

    	String updateBienRevertible="update bien_revertible set "+
            "num_inventario =?, fecha_inicio=?, fecha_vencimiento=?," +
            " fecha_transmision=? , importe=? , poseedor=?, titulo_posesion =? "+
            ", condiciones_reversion=?  , detalles=? , cat_transmision=? , " +
            "id_cuenta_amortizacion=? where id =?";
    	
    	
    	String insertBienes= "INSERT INTO bien_revertible_bien("+
                "id_bien, id_bien_revertible)  VALUES (?, ?)";
    	
    	Connection conn=null;
        PreparedStatement ps= null;
        ResultSet rs= null;
		try {
			/*insertamos primero el bien revertible*/  
			if (bienRevertible == null) return null;
            conn = CPoolDatabase.getConnection();
            conn.setAutoCommit(false);
			 /* Insertamos la cuenta de amortizacion */
            if (bienRevertible.getCuentaAmortizacion()!=null)
            {
            	bienRevertible.setCuentaAmortizacion(actualizarCuentaAmortizacion(bienRevertible.getCuentaAmortizacion(), conn));
            }
           
            ps= conn.prepareStatement(updateBienRevertible);
            ps.setString(1, bienRevertible.getNumInventario());
            if (bienRevertible.getFechaInicio()!=null)
            	ps.setDate(2,new java.sql.Date(bienRevertible.getFechaInicio().getTime()));
            else
            	ps.setObject(2, null);
            if (bienRevertible.getFechaVencimiento()!=null)
            	ps.setDate(3,new java.sql.Date(bienRevertible.getFechaVencimiento().getTime()));
            else
            	ps.setObject(3, null);
            if (bienRevertible.getFechaTransmision()!=null)
            	ps.setDate(4,new java.sql.Date(bienRevertible.getFechaTransmision().getTime()));
            else
            	ps.setObject(4, null);
            if (bienRevertible.getImporte()!=null)
            	ps.setDouble(5, bienRevertible.getImporte());
            else
            	ps.setObject(5,null);
            ps.setString(6, bienRevertible.getPoseedor());
            ps.setString(7, bienRevertible.getTituloPosesion());
            ps.setString(8, bienRevertible.getCondicionesReversion());
            ps.setString(9, bienRevertible.getDetalles());
            ps.setString(10, bienRevertible.getCatTransmision());
            if (bienRevertible.getCuentaAmortizacion()!=null && bienRevertible.getCuentaAmortizacion().getId()!=-1){
            	ps.setLong(11, bienRevertible.getCuentaAmortizacion().getId());
            }else{
            	ps.setObject(11, null);
            }
            ps.setLong(12, bienRevertible.getId());
            ps.execute();
            ps.close();
            /*Elminamos los bienes*/
            deleteBienesFromBienesRevertible(bienRevertible, conn); 
            /*insertamos ahora los bienes asociados*/
            if (bienRevertible.getBienes()!=null)
            for (Iterator<BienBean> it=bienRevertible.getBienes().iterator();it.hasNext();){
            	BienBean bien= (BienBean)it.next();
            	ps= conn.prepareStatement(insertBienes);
                ps.setLong(1,bien.getId());
            	ps.setLong(2,bienRevertible.getId());
                ps.execute();
                ps.close();
            }
            actualizarObservaciones(bienRevertible.getId(), bienRevertible.getObservaciones(),
            		TABLA_OBSERVACION_BIEN_REVERTIBLE, conn);
            conn.commit();
        }catch (PermissionException e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }catch (LockException e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }catch (Exception e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }/** java.lang.OutOfMemoryError */catch (java.lang.Error e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){}
        }

        return bienRevertible;
    }
    private Lote insertarLote(Lote lote, Sesion userSesion) throws Exception {
    	Connection conn=null;
        PreparedStatement ps= null;
		try {
			/*insertamos lote*/  
			if (lote == null) return null;
            conn = CPoolDatabase.getConnection();
            conn.setAutoCommit(false);
		   	lote = insertarLote(lote,conn ,userSesion);
		   	if (lote.getBienes()!=null){
		   		for (Iterator it=lote.getBienes().iterator();it.hasNext();){
		   			MuebleBean mueble= new MuebleBean((BienBean)it.next());
		   			mueble.setLote(lote);
		   			insertaBienLote(mueble, conn);
		   		}
		   	}
		   	DocumentoEnDisco.actualizarConFicherosDeTemporal(lote.getDocumentos());
		   	lote.setDocumentos(null);
		   	conn.commit();
        }catch (Exception e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }/** java.lang.OutOfMemoryError */catch (java.lang.Error e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }finally{
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){}
        }

        return lote;

    }
    /***
     * Actualiza los datos de un Lote
     * @param Lote
     * @return
     * @throws PermissionException
     * @throws LockException
     * @throws Exception
     */
    private Lote updateLote(Lote lote, Sesion userSesion) throws PermissionException,LockException,Exception{

    	String updateLote="UPDATE lote "+
    		" SET nombre_lote=?, fecha_baja=?, fecha_ultima_modificacion=?, "+ 
    		" id_seguro=?, descripcion=?, destino=? WHERE id_lote =?";
    	Connection conn=null;
        PreparedStatement ps= null;
		try {
			/*insertamos lote*/  
			if (lote == null) return null;
			
			
            conn = CPoolDatabase.getConnection();
            
             
            conn.setAutoCommit(false);
		    ps= conn.prepareStatement(updateLote);
            ps.setString(1, lote.getNombre_lote());
            if (lote.getFecha_baja()!=null)
            	ps.setTimestamp(2,new java.sql.Timestamp(lote.getFecha_baja().getTime()));
            else
            	ps.setObject(2, null);
            lote.setFecha_ultima_modificacion(new Date());
            ps.setTimestamp(3,new java.sql.Timestamp(lote.getFecha_ultima_modificacion().getTime()));
            /** seguro */
            if (lote.getSeguro()!=null){
            	if (lote.getSeguro().getId()==null ||lote.getSeguro().getId()==-1) lote.setSeguro(insertarSeguro(lote.getSeguro(), conn));
            	else updateSeguro(lote.getSeguro(), conn);
            	ps.setLong(4,lote.getSeguro().getId());
            }else
            	ps.setObject(4, null);
          	ps.setString(5,lote.getDescripcion());
          	ps.setString(6,lote.getDestino());
          	ps.setLong(7,lote.getId_lote());
            ps.execute();
            ps.close();
           
            
           
            
            /** Insertamos la documentacion */
           
            DocumentConnection docConn= new DocumentConnection(srid);
            lote.setDocumentos(docConn.updateDocumentsInventario(lote,(lote.getDocumentos()==null?null:lote.getDocumentos().toArray()), userSesion, conn, municipio));
           

            /** Actualizamos los ficheros en disco  (temporal --> destino) */
            DocumentoEnDisco.actualizarConFicherosDeTemporal(lote.getDocumentos());
            docConn.borrarFicherosEnDisco(lote.getId_lote(), docConn.getDocsBorrados());

            conn.commit();
            lote.setDocumentos(null);
        }catch (Exception e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }/** java.lang.OutOfMemoryError */catch (java.lang.Error e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }finally{
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){}
        }

        return lote;
    }
    private void deleteBienesFromBienesRevertible(BienRevertible bienRevertible, Connection conn) throws Exception{
    	PreparedStatement ps = null;
    	try{
    		String deleteBienes= "delete from bien_revertible_bien where id_bien_revertible =?";
    		ps = conn.prepareStatement(deleteBienes);
    		ps.setLong(1,bienRevertible.getId());
    		ps.execute();
    	}
    	catch (Exception e){
    		throw e;
    	}finally{
    		try{ps.close();}catch(Exception e){};
    	}
    }
    /**
     * Devuelve la codificacion correcta del numero de inventario
     * @param bien
     * @param numInventario
     * @return
     */
    
    private String getCodNumInventario(BienBean bien, String numInventario){
    	//primero comprobamos que no tenga ya el patron correcto
    	String patron="[1-8]\\.[.0-9a-zA-Z]+|[1-8]\\.[1-2]\\.[.0-9a-zA-Z]+";
    	Pattern pattern = Pattern.compile(patron);
        Matcher matcher = pattern.matcher(numInventario);
        if(matcher.matches()) return numInventario;
    	//if (numInventario!=null && numInventario.length()>0) return numInventario;
    	if (bien instanceof InmuebleBean){
            if (((InmuebleBean)bien).isRustico()) numInventario= "1.2."+numInventario;
            else if (((InmuebleBean)bien).isUrbano()) numInventario= "1.1."+numInventario;
        }else if (bien instanceof MuebleBean){
            if (((MuebleBean)bien).getTipo().equalsIgnoreCase(Const.PATRON_MUEBLES_HISTORICOART))
                numInventario="3.1."+numInventario;
            if (((MuebleBean)bien).getTipo().equalsIgnoreCase(Const.PATRON_BIENES_MUEBLES))
                numInventario="3.2."+numInventario;
        }else if (bien instanceof DerechoRealBean){
            numInventario="2."+numInventario;
        }else if (bien instanceof ValorMobiliarioBean){
            numInventario="4."+numInventario;
        }else if (bien instanceof CreditoDerechoBean){
        	if (((CreditoDerechoBean)bien).isArrendamiento()) 
        		numInventario="5.1."+numInventario;
        	else
        		numInventario="5."+numInventario;
        }else if (bien instanceof SemovienteBean){
            numInventario="6."+numInventario;
        }else if (bien instanceof ViaBean){
            if (bien.getTipo().equalsIgnoreCase(Const.PATRON_VIAS_PUBLICAS_URBANAS))
                numInventario="7.1."+numInventario;
            if (bien.getTipo().equalsIgnoreCase(Const.PATRON_VIAS_PUBLICAS_RUSTICAS))
                numInventario="7.2."+numInventario;
        }else if (bien instanceof VehiculoBean){
            numInventario="8."+numInventario;
        }
        return numInventario;	
    }
    /**
     * Devuelve la codificacion correcta del numero de inventario
     * @param bienRevertible
     * @param numInventario
     * @return
     */
    
    private String getCodNumInventario(BienRevertible bienRevertible, String numInventario){
    	
    	//En los inventarios de serpa nos esta llegando este tipo de numeros ".1". 
    	//Si empieza por punto se lo quitamos
    	if (numInventario.startsWith(".")){
    		numInventario=numInventario.substring(1);
    	}
    	//primero comprobamos que no tenga ya el patron correcto
    	String patron="9_[.0-9a-zA-Z]+|9\\.[.0-9a-zA-Z]+";
    	Pattern pattern = Pattern.compile(patron);
        Matcher matcher = pattern.matcher(numInventario);
        if(matcher.matches()) return numInventario;
        return "9."+numInventario;
    }
    /**
     * Inserta los datos de un lote
     * @param lote
     * @param conn
     * @return
     */
    private Lote insertarLote(Lote lote, Connection conn, Sesion userSesion) throws Exception{
    	if (lote==null) return null;
    	String sSQL= "INSERT INTO lote("+
            " id_lote, nombre_lote, fecha_alta, fecha_ultima_modificacion, "+
            " id_seguro, descripcion, destino, id_municipio) "+
            " VALUES (?, ?, ?, ?, ?, ?, ?, '"+municipio+"')";
    	
    	
    	
    	PreparedStatement ps= null;
        try {

            /** seguro */
            if (lote.getSeguro()!=null){
                if (lote.getSeguro().getId()==null || lote.getSeguro().getId()==-1) lote.setSeguro(insertarSeguro(lote.getSeguro(), conn));
                else updateSeguro(lote.getSeguro(), conn);
            }
        	
        	
		    lote.setId_lote(CPoolDatabase.getNextValue("lote","id_lote"));
		    lote.setFecha_alta(new Date());
		    lote.setFecha_ultima_modificacion(new Date());
		    ps= conn.prepareStatement(sSQL);
            ps.setLong(1, lote.getId_lote());
            ps.setString(2, lote.getNombre_lote());
            ps.setTimestamp(3, new java.sql.Timestamp(lote.getFecha_alta().getTime()));
            ps.setTimestamp(4, new java.sql.Timestamp(lote.getFecha_ultima_modificacion().getTime()));
            
            if (lote.getSeguro()!=null && lote.getSeguro().getId()!=null)
            	ps.setLong(5,lote.getSeguro().getId());
            else
            	ps.setObject(5,null);
    
            ps.setString(6, lote.getDescripcion());
            ps.setString(7, lote.getDestino());
            ps.execute();
            ps.close();  
            
            

            
            /** Insertamos la documentacion en BD */
            if (lote.getDocumentos()!=null){
            	DocumentConnection docConn= new DocumentConnection(srid);
            	lote.setDocumentos(docConn.updateDocumentsInventario(lote, lote.getDocumentos().toArray(), userSesion, conn, municipio));
            }
			
		}catch(Exception ex){
        	logger.error("Error al ejecutar: "+sSQL);
        	throw ex;
        }finally{
           try{ps.close();}catch(Exception e){};
        } 
    	return lote;
    }
    /**
     * Inserta la relación entro lotes y muebles
     * @param mueble
     * @param conn
     * @return
     */
    private void insertaBienLote(MuebleBean mueble, Connection conn) throws Exception{
    	if (mueble==null || mueble.getId()==-1 ||  mueble.getLote()==null || mueble.getLote().getId_lote()==null) return;
    	String sSQL= "INSERT INTO lote_bien( id_bien, id_lote) VALUES (?, ?)";
    	PreparedStatement ps= null;
        try {
		    ps= conn.prepareStatement(sSQL);
            ps.setLong(1, mueble.getId());
            ps.setLong(2, mueble.getLote().getId_lote());
            ps.execute();
            ps.close();  
		}catch(Exception ex){
        	logger.error("Error al ejecutar: "+sSQL);
        	throw ex;
        }finally{
           try{ps.close();}catch(Exception e){};
        } 
    	return;
    }
    private synchronized BienBean insertarBienInventario(BienBean bien, Sesion userSesion, Connection conn, String nextValString, String currValString) throws Exception{
        if (bien == null) return null;

        
        
        
        
        String sSQL= "INSERT INTO BIENES_INVENTARIO (ID, NUMINVENTARIO, FECHA_ALTA, TIPO, NOMBRE, DESCRIPCION, USO, " +
                "ID_MUNICIPIO, ORGANIZACION, ID_CUENTA_CONTABLE, ID_CUENTA_AMORTIZACION, ID_SEGURO, FECHA_ULTIMA_MODIFICACION, " +
                "FECHA_APROBACION_PLENO, REVISION_ACTUAL) VALUES " +
                " (?,?,?,?,?,?,?,?,?,?,?,?,?,?,"+nextValString+")";

        PreparedStatement ps= null;
        try {
        	
        	logger.info("Insertando el bien:"+bien.getNumInventario());
            /** cuentas contable y amortizacion */
            bien.setCuentaContable(insertarCuentaContable(bien.getCuentaContable(), conn));
            bien.setCuentaAmortizacion(actualizarCuentaAmortizacion(bien.getCuentaAmortizacion(), conn));

            /** seguro */
            if (bien.getSeguro()!=null){
                if (bien.getSeguro().getId()==null || bien.getSeguro().getId()==-1) bien.setSeguro(insertarSeguro(bien.getSeguro(), conn));
                else updateSeguro(bien.getSeguro(), conn);
            }

            if (bien.getId()<0){
            	String numInventario= "";
            	if (bien.getNumInventario()!=null && bien.getNumInventario().length()>0){
            		numInventario=bien.getNumInventario();
            		numInventario=getCodNumInventario(bien, numInventario);
            	}
            	else{
            		boolean existe = true;            	
            		while (existe){
		            	// Se comprueba el tipo de epigrafe para generar el numero de inventario especifico de cada bien
		            	if (bien instanceof InmuebleBean){
		            		if (((InmuebleBean)bien).isRustico())
		            			numInventario= new Long(CPoolDatabase.getNext("bienes_inventario","inmueblerustico", conn, Integer.parseInt(municipio), true)).toString();
		                    else if (((InmuebleBean)bien).isUrbano())
		            			numInventario= new Long(CPoolDatabase.getNext("bienes_inventario","inmuebleurbano", conn, Integer.parseInt(municipio), true)).toString();
		                 }else if (bien instanceof MuebleBean){
		                	 if (((MuebleBean)bien).getTipo().equalsIgnoreCase(Const.PATRON_MUEBLES_HISTORICOART))
		                		 numInventario= new Long(CPoolDatabase.getNext("bienes_inventario","historico", conn, Integer.parseInt(municipio), true)).toString();
		                     if (((MuebleBean)bien).getTipo().equalsIgnoreCase(Const.PATRON_BIENES_MUEBLES))
		                    	 numInventario= new Long(CPoolDatabase.getNext("bienes_inventario","mueble", conn, Integer.parseInt(municipio), true)).toString();
		                 }else if (bien instanceof DerechoRealBean){
		                	 numInventario= new Long(CPoolDatabase.getNext("bienes_inventario","derechoreal", conn, Integer.parseInt(municipio), true)).toString();
		                 }else if (bien instanceof ValorMobiliarioBean){
		                	 numInventario= new Long(CPoolDatabase.getNext("bienes_inventario","valormobiliario", conn, Integer.parseInt(municipio), true)).toString();
		                 }else if (bien instanceof CreditoDerechoBean){
		                	 if (((CreditoDerechoBean)bien).isArrendamiento()) 
		                		 numInventario= new Long(CPoolDatabase.getNext("bienes_inventario","arrendamiento", conn, Integer.parseInt(municipio), true)).toString();
		                	 else
		                		 numInventario= new Long(CPoolDatabase.getNext("bienes_inventario","creditoderecho", conn, Integer.parseInt(municipio), true)).toString();
		                 }else if (bien instanceof SemovienteBean){
		                	 numInventario= new Long(CPoolDatabase.getNext("bienes_inventario","semoviente", conn, Integer.parseInt(municipio), true)).toString();
		                 }else if (bien instanceof ViaBean){
		                	 if (bien.getTipo().equalsIgnoreCase(Const.PATRON_VIAS_PUBLICAS_URBANAS))
		                    	 numInventario= new Long(CPoolDatabase.getNext("bienes_inventario","viaurbana", conn, Integer.parseInt(municipio), true)).toString();
		                     if (bien.getTipo().equalsIgnoreCase(Const.PATRON_VIAS_PUBLICAS_RUSTICAS))
		                    	 numInventario= new Long(CPoolDatabase.getNext("bienes_inventario","viarustica", conn, Integer.parseInt(municipio), true)).toString();
		                 }else if (bien instanceof VehiculoBean){
		                	 numInventario= new Long(CPoolDatabase.getNext("bienes_inventario","vehiculo", conn, Integer.parseInt(municipio), true)).toString();
		                 }
		            	numInventario=getCodNumInventario(bien, numInventario);
		            	if (!existeNumInventario(numInventario, Integer.parseInt(municipio)))
		            		existe = false;
            		}
            	}

            	long idBien = CPoolDatabase.getNextValue("bienes_inventario","id");
            	logger.debug("Insertando el bien:"+idBien);
          
            	//Puede que en la carga de inventario venga con el numero de inventario
            	
            	

            	bien.setId(idBien);
            	bien.setNumInventario(numInventario);
            } else
            	logger.debug("Actualizando el bien:"+bien.getId());
            
            	/*ASO: En el caso de que venga de una carga de inventario viene la fecha de alta y la de modificacion*/
            java.util.Date dateAlta= new java.util.Date();
            if (bien.getFechaAlta()!=null)
            	dateAlta = bien.getFechaAlta();
            
            logger.info("Preparando sentencia de actualizacion:"+bien.getId());
            ps= conn.prepareStatement(sSQL);
            ps.setLong(1, bien.getId());
            ps.setString(2, bien.getNumInventario());
            ps.setTimestamp(3, new Timestamp(dateAlta.getTime()));
            ps.setString(4, bien.getTipo());
            ps.setString(5, bien.getNombre());
            ps.setString(6, bien.getDescripcion());
            ps.setString(7, bien.getUso());
            ps.setInt(8, Integer.parseInt(municipio));
            ps.setString(9, bien.getOrganizacion());
            ps.setLong(10, bien.getCuentaContable()!=null?bien.getCuentaContable().getId():-1);
            ps.setLong(11, bien.getCuentaAmortizacion()!=null?bien.getCuentaAmortizacion().getId():-1);
            ps.setLong(12, bien.getSeguro()!=null?bien.getSeguro().getId():-1);
            if (bien.getFechaUltimaModificacion()==null)
            	ps.setObject(13, null);
            else
            	ps.setTimestamp(13, new Timestamp(bien.getFechaUltimaModificacion().getTime()));
            if (bien.getFechaAprobacionPleno()==null)
            	ps.setObject(14, null);
            else
            	ps.setTimestamp(14, new Timestamp(bien.getFechaAprobacionPleno().getTime()));
         
            ps.execute();
            ps.close();

            /** actualizamos el bien */
            bien.setIdMunicipio(municipio);
            bien.setFechaAlta(dateAlta);

	        logger.info("Insertando observaciones:"+bien.getId());

             /* Insertamos las observaciones del bien */
             if (bien.getObservaciones() != null){
                 for(Iterator it=bien.getObservaciones().iterator();it.hasNext();){
                    insertObservacion(bien.getId(), (Observacion)it.next(),"observaciones_inventario", conn, currValString);
                }
            }
             
             logger.info("Insertando mejoras:"+bien.getId());
            /** Insertamos las mejoras del bien (inmuebles, vias) */
            if (bien.getMejoras() != null){
                Object[] objs= bien.getMejoras().toArray();
                for(int i=0;i<objs.length;i++){
                    insertMejora(bien.getId(), (Mejora)objs[i], conn, currValString);
                }
            }
            
            if (bien instanceof ViaBean){
                /** Insertamos las refrencias catastrales de la via */
                if (((ViaBean)bien).getReferenciasCatastrales() != null){
                    Object[] objs= ((ViaBean)bien).getReferenciasCatastrales().toArray();
                    for(int i=0;i<objs.length;i++){
                        insertRefCatastral(((ViaBean)bien).getId(), (ReferenciaCatastral)objs[i], conn, currValString);
                    }
                }
            }
            else{
	            if (bien instanceof InmuebleBean){
	            	logger.info("Insertando referencias catastrales:"+bien.getId());
	                /** Insertamos las refrencias catastrales del inmueble */
	                if (((InmuebleBean)bien).getReferenciasCatastrales() != null){
	                    Object[] objs= ((InmuebleBean)bien).getReferenciasCatastrales().toArray();
	                    for(int i=0;i<objs.length;i++){
	                        insertRefCatastral(((InmuebleBean)bien).getId(), (ReferenciaCatastral)objs[i], conn, currValString);
	                    }
	                }
	                /** Insertamos los usos funcionales del inmueble */
	            	logger.info("Insertando usos funcionales:"+bien.getId());
	                if (((InmuebleBean)bien).getUsosFuncionales() != null){
	                    Object[] objs= ((InmuebleBean)bien).getUsosFuncionales().toArray();
	                    for(int i=0;i<objs.length;i++){
	                        insertUsoFuncional(((InmuebleBean)bien).getId(), (UsoFuncional)objs[i], conn, currValString);
	                    }
	                }
	            }
            }
            /** Insertamos la documentacion en BD solo si es una insercion 
             * Cuando se trate de una inserción por carga de inventario la fecha de modificación 
             * sera antigua
             */
            logger.info("Insertando documentacion:"+bien.getId());
            Date hoy = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            if (bien.getDocumentos()!=null && 
            		(bien.getFechaUltimaModificacion()==null || 
            		!sdf.format(bien.getFechaUltimaModificacion()).equals(sdf.format(hoy)))){
            	DocumentConnection docConn= new DocumentConnection(srid);
            	bien.setDocumentos(docConn.updateDocumentsInventario(bien, bien.getDocumentos().toArray(), userSesion, conn, municipio));
            }

            //Insertamos las features si previamente ya tenia elementos asociados.
            logger.info("Insertando features:"+bien.getId());
            if (bien.getIdFeatures()!=null && bien.getIdLayers()!=null){
            	//Actualizamos la revision de la tabla inventario_feature
                updateRevisionInventarioFeature(bien, conn, nextValString, currValString);   
                if (bien.getIdFeatures().length>0)
                	insertInventarioFeature(bien.getId(),bien.getIdLayers(),bien.getIdFeatures(),currValString,userSesion,conn);
                else{
                	
                }
            }

        }catch(Exception ex){
        	logger.error("Error al ejecutar: "+sSQL);
        	throw ex;
        }finally{
            try{ps.close();}catch(Exception e){};
        }

        return bien;
    }


	private boolean existeNumInventario(String numInventario, int id_muninicipio) {
		String sSQL= "SELECT * from bienes_inventario WHERE numinventario = '"+numInventario+"' and id_municipio = "+id_muninicipio;
        Connection conn=null;
        PreparedStatement ps= null;
        ResultSet rs= null;
        try{
            conn= CPoolDatabase.getConnection();
            ps= conn.prepareStatement(sSQL);
            rs= ps.executeQuery();
            if (rs.next()){
            	return true;
            }
            else{
            	return false;
            }
        }catch(Exception ex){
        	logger.error("Error al ejecutar: "+sSQL);
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
        }
		return false;
	}


	private InmuebleBean updateInmueble(InmuebleBean inmueble, Sesion userSesion) throws PermissionException,LockException,Exception{

           String inmueblesSQL= "UPDATE INMUEBLES SET REVISION_EXPIRADA="+sRevisionActual.replaceAll("\\?", "inmuebles")+
           " WHERE ID=? "+sRevisionExpiradaNula;
           
           String inmurbanoSQL= "INSERT INTO INMUEBLES_URBANOS(ID, MANZANA, REVISION_ACTUAL, ANCHOSUPERF, LONGSUPERF, METRPAVSUPERF, METRNOPAVSUPERF) VALUES(?,?,"+sRevisionActual.replaceAll("\\?", "inmuebles")+",?,?,?,?)";           

           String inmrusticoSQL= "INSERT INTO INMUEBLES_RUSTICOS(ID, POLIGONO, APROVECHAMIENTO, PARAJE,REVISION_ACTUAL, ANCHOSUPERF, LONGSUPERF, METRPAVSUPERF, METRNOPAVSUPERF) VALUES(?,?,?,?,"+sRevisionActual.replaceAll("\\?", "inmuebles")+", ?,?,?,?)";

           Connection conn=null;
           PreparedStatement ps= null;
           ResultSet rs= null;
           try {

               if (inmueble == null) return null;

               conn= CPoolDatabase.getConnection();
               conn.setAutoCommit(false);

               updateBienInventario(inmueble, userSesion, conn, sMaxRevision.replaceAll("\\?", "inmuebles"), sRevisionActual.replaceAll("\\?", "inmuebles"));               

               //Actualizamos la revision de la tabla inmueble rustico y urbano
               updateRevisionInmueble(inmueble, conn, sMaxRevision.replaceAll("\\?", "inmuebles"), sRevisionActual.replaceAll("\\?", "inmuebles"));               

               
               ps= conn.prepareStatement(inmueblesSQL);
               ps.setLong(1, inmueble.getId());
               ps.execute();
               ps.close();

               if (inmueble.isUrbano()){
                   ps= conn.prepareStatement(inmurbanoSQL);
                   ps.setLong(1, inmueble.getId());
                   ps.setString(2, inmueble.getInmuebleUrbano()!=null?inmueble.getInmuebleUrbano().getManzana():null);
   		       		ps.setDouble(3,inmueble.getInmuebleUrbano()!=null?inmueble.getInmuebleUrbano().getSueloAncho():null);
   		       		ps.setDouble(4,inmueble.getInmuebleUrbano()!=null?inmueble.getInmuebleUrbano().getSueloLong():null);
       		 		ps.setDouble(5,inmueble.getInmuebleUrbano()!=null?inmueble.getInmuebleUrbano().getSueloMetrosPav():null);
       		 		ps.setDouble(6,inmueble.getInmuebleUrbano()!=null?inmueble.getInmuebleUrbano().getSueloMetrosNoPav():null);                   
                   ps.execute();
                   ps.close();
               }else if (inmueble.isRustico()){
                   ps= conn.prepareStatement(inmrusticoSQL);
                   ps.setLong(1, inmueble.getId());
                   ps.setString(2, inmueble.getInmuebleRustico()!=null?inmueble.getInmuebleRustico().getPoligono():null);
                   ps.setString(3, inmueble.getInmuebleRustico()!=null?inmueble.getInmuebleRustico().getAprovechamiento():null);
                   ps.setString(4, inmueble.getInmuebleRustico()!=null?inmueble.getInmuebleRustico().getParaje():null);
   					ps.setDouble(5,inmueble.getInmuebleRustico()!=null?inmueble.getInmuebleRustico().getSueloAncho():null);
       		 		ps.setDouble(6,inmueble.getInmuebleRustico()!=null?inmueble.getInmuebleRustico().getSueloLong():null);
       		 		ps.setDouble(7,inmueble.getInmuebleRustico()!=null?inmueble.getInmuebleRustico().getSueloMetrosPav():null);
       		 		ps.setDouble(8,inmueble.getInmuebleRustico()!=null?inmueble.getInmuebleRustico().getSueloMetrosNoPav():null);
                   ps.execute();
                   ps.close();
               }

               this.insertInmueble(conn, inmueble, sRevisionActual.replaceAll("\\?", "inmuebles"), userSesion);

               /** Actualizamos los ficheros en disco  (temporal --> destino) */
               DocumentoEnDisco.actualizarConFicherosDeTemporal(((BienBean)inmueble).getDocumentos());
               inmueble.setDocumentos(null);
               conn.commit();

               /** Borramos de disco los ficheros del bien que no esten asociados a otro */
               DocumentConnection docConn= new DocumentConnection(srid);
               docConn.borrarFicherosEnDisco(inmueble.getId(), docsBorrados);

           }catch (Exception e){
               try{conn.rollback();}catch(Exception ex){};
               throw e;
           }/** java.lang.OutOfMemoryError */catch (java.lang.Error e){
               try{conn.rollback();}catch(Exception ex){};
               throw e;
           }finally{
               try{rs.close();}catch(Exception e){};
               try{ps.close();}catch(Exception e){};
               try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){}
           }

           return inmueble;
       }
    
    /**
     * Metodo para Insertar el Bien Rustico y Urbano
     * @param conn
     * @param inmueble
     * @param userSesion
     * @return
     * @throws PermissionException
     * @throws LockException
     * @throws Exception
     */
    private InmuebleBean insertInmuebleUrbanoRustico(Connection conn,InmuebleBean inmueble, String revision,Sesion userSesion) throws Exception{
        
        String inmurbanoSQL= "INSERT INTO INMUEBLES_URBANOS(ID, MANZANA, REVISION_ACTUAL, ANCHOSUPERF, LONGSUPERF, METRPAVSUPERF, METRNOPAVSUPERF) VALUES(?,?,"+sRevisionActual.replaceAll("\\?", "inmuebles")+",?,?,?,?)";

        String inmrusticoSQL= "INSERT INTO INMUEBLES_RUSTICOS(ID, POLIGONO, APROVECHAMIENTO, PARAJE,REVISION_ACTUAL, ANCHOSUPERF, LONGSUPERF, METRPAVSUPERF, METRNOPAVSUPERF) VALUES(?,?,?,?,"+sRevisionActual.replaceAll("\\?", "inmuebles")+",?,?,?,?)";
       
        PreparedStatement ps= null;

        if (inmueble == null) return null;

        if (inmueble.isUrbano()){
            ps= conn.prepareStatement(inmurbanoSQL);
            ps.setLong(1, inmueble.getId());
            ps.setString(2, inmueble.getInmuebleUrbano()!=null?inmueble.getInmuebleUrbano().getManzana():null);
	       	ps.setDouble(3,inmueble.getInmuebleUrbano()!=null?inmueble.getInmuebleUrbano().getSueloAncho():null);
   		 	ps.setDouble(4,inmueble.getInmuebleUrbano()!=null?inmueble.getInmuebleUrbano().getSueloLong():null);
   		 	ps.setDouble(5,inmueble.getInmuebleUrbano()!=null?inmueble.getInmuebleUrbano().getSueloMetrosPav():null);
   		 	ps.setDouble(6,inmueble.getInmuebleUrbano()!=null?inmueble.getInmuebleUrbano().getSueloMetrosNoPav():null);
            ps.execute();
            ps.close();
        }else if (inmueble.isRustico()){
            ps= conn.prepareStatement(inmrusticoSQL);
            ps.setLong(1, inmueble.getId());
            ps.setString(2, inmueble.getInmuebleRustico()!=null?inmueble.getInmuebleRustico().getPoligono():null);
            ps.setString(3, inmueble.getInmuebleRustico()!=null?inmueble.getInmuebleRustico().getAprovechamiento():null);
            ps.setString(4, inmueble.getInmuebleRustico()!=null?inmueble.getInmuebleRustico().getParaje():null);
			ps.setDouble(5,inmueble.getInmuebleRustico()!=null?inmueble.getInmuebleRustico().getSueloAncho():null);
   		 	ps.setDouble(6,inmueble.getInmuebleRustico()!=null?inmueble.getInmuebleRustico().getSueloLong():null);
   		 	ps.setDouble(7,inmueble.getInmuebleRustico()!=null?inmueble.getInmuebleRustico().getSueloMetrosPav():null);
   		 	ps.setDouble(8,inmueble.getInmuebleRustico()!=null?inmueble.getInmuebleRustico().getSueloMetrosNoPav():null);
            ps.execute();
            ps.close();
        }
        return inmueble;
    }
    
    private DerechoRealBean updateDerechoReal(DerechoRealBean derechoReal, Sesion userSesion) throws PermissionException,LockException,Exception{
           String derechosSQL= "UPDATE DERECHOS_REALES SET REVISION_EXPIRADA="+sRevisionActual.replaceAll("\\?", "derechos_reales")+
           " WHERE ID=? "+sRevisionExpiradaNula;

           Connection conn=null;
           PreparedStatement ps= null;
           ResultSet rs= null;
           try {

               if (derechoReal == null) return null;

               conn= CPoolDatabase.getConnection();
               conn.setAutoCommit(false);

               updateBienInventario(derechoReal, userSesion, conn,sMaxRevision.replaceAll("\\?", "derechos_reales"), sRevisionActual.replaceAll("\\?", "derechos_reales"));

               ps= conn.prepareStatement(derechosSQL);
               ps.setLong(1, derechoReal.getId());
               ps.execute();
               ps.close();

               insertDerechoReal(conn, derechoReal, sRevisionActual.replaceAll("\\?", "derechos_reales"), userSesion);

               /** Actualizamos los ficheros en disco  (temporal --> destino) */
               DocumentoEnDisco.actualizarConFicherosDeTemporal(((BienBean)derechoReal).getDocumentos());
               derechoReal.setDocumentos(null);
               conn.commit();

               /** Borramos de disco los ficheros del bien que no esten asociados a otro */
               DocumentConnection docConn= new DocumentConnection(srid);
               docConn.borrarFicherosEnDisco(derechoReal.getId(), docsBorrados);
           }catch (Exception e){
               try{conn.rollback();}catch(Exception ex){};
               throw e;
           }/** java.lang.OutOfMemoryError */catch (java.lang.Error e){
               try{conn.rollback();}catch(Exception ex){};
               throw e;
           }finally{
               try{rs.close();}catch(Exception e){};
               try{ps.close();}catch(Exception e){};
               try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){}
           }
           return derechoReal;
       }


    private BienBean updateBienInventario(BienBean bien, Sesion userSesion, Connection conn, String nextValString, String currValString)throws Exception{
        if (bien == null) return null;
        
        
        
//        this.insertarBienInventario(bien, userSesion, conn, nextValString,currValString);
        
        String sSQL= "UPDATE BIENES_INVENTARIO SET REVISION_EXPIRADA="+currValString+
        " WHERE ID=? "+sRevisionExpiradaNula+ " AND REVISION_ACTUAL<"+currValString;

        PreparedStatement ps= null;
        
         try{ 
        	logger.info("Actualizando bien:"+bien.getId());
			java.util.Date date= new java.util.Date();
	        bien.setFechaUltimaModificacion(date);
	        bien = insertarBienInventario(bien, userSesion, conn, nextValString,currValString);

	        logger.info("Bien Insertado. Actualizando resto informacion:"+bien.getId());
            /** Actualizamos cuenta contable y de amortizacion */
            gestionarCuentaContable(bien, conn);
            gestionarCuentaAmortizacion(bien, conn);

	        logger.info("Bien Insertado. Actualizando seguro:"+bien.getId());

            /** Seguro */
            gestionarSeguro(bien, conn);
            
            ps= conn.prepareStatement(sSQL);
            ps.setLong(1, bien.getId());
            ps.execute();

            /** actualizamos bien de inventario */
            bien.setFechaUltimaModificacion(date);


            /** actualizamos las observaciones del bien de inventario */
            //bien.setObservaciones(actualizarObservaciones(bien.getId(), bien.getObservaciones(),TABLA_OBSERVACION_INVENTARIO, conn));


            //if (bien instanceof InmuebleBean){
                /** actualizamos las mejoras del bien inmueble */
            //    bien.setMejoras(actualizarMejoras(bien.getId(), bien.getMejoras(), conn));

                /** actualizamos las ref. catastrales del inmueble */
            //    ((InmuebleBean)bien).setReferenciasCatastrales(actualizarRefCatastrales(((InmuebleBean)bien).getId(), ((InmuebleBean)bien).getReferenciasCatastrales(), conn));

                /** actualizamos los usos funcionales del inmueble */
            //    ((InmuebleBean)bien).setUsosFuncionales(actualizarUsosFuncionales(((InmuebleBean)bien).getId(), ((InmuebleBean)bien).getUsosFuncionales(), conn));
            //}//else if (bien instanceof ViaBean){
                /** actualizamos las mejoras del bien vias */
             //   bien.setMejoras(actualizarMejoras(bien.getId(), bien.getMejoras(), conn));
            //}

            logger.info("Bien Insertado. Actualizando documentacion:"+bien.getId());
            /** Insertamos la documentacion */
            DocumentConnection docConn= new DocumentConnection(srid);
            if (bien.getDocumentos()!=null)
            	bien.setDocumentos(docConn.updateDocumentsInventario(bien, bien.getDocumentos().toArray(), userSesion, conn, municipio));
            docsBorrados= docConn.getDocsBorrados();
            
        	/*logger.info("Actualizando revision para el bien:"+bien.getId());
            String sSQL= "UPDATE BIENES_INVENTARIO SET REVISION_EXPIRADA="+currValString+
            " WHERE ID=? "+sRevisionExpiradaNula+ " AND REVISION_ACTUAL<"+currValString;
            PreparedStatement ps= null;
    		try {
                ps= conn.prepareStatement(sSQL);
                ps.setLong(1, bien.getId());
                ps.execute();
               
    		 }catch (Exception e){
    	        logger.error("Error al ejecutar :"+sSQL); 	
    	        throw e;
    	     }finally{
    	    	 ps.close();
    	     }*/   


        }catch (Exception e){
        	e.printStackTrace();        	
            throw e;
        }

        return bien;
    }
    
    private BienBean updateRevisionInmueble(InmuebleBean bien, Connection conn, String nextValString, String currValString)throws Exception{
        if (bien == null) return null;
        
        String sSQL;
        if (bien.isUrbano()){
            sSQL= "UPDATE INMUEBLES_URBANOS SET REVISION_EXPIRADA="+currValString+
            " WHERE ID=? "+sRevisionExpiradaNula+ " AND REVISION_ACTUAL<"+currValString;
        	
        }
        else{
            sSQL= "UPDATE INMUEBLES_RUSTICOS SET REVISION_EXPIRADA="+currValString+
            " WHERE ID=? "+sRevisionExpiradaNula+ " AND REVISION_ACTUAL<"+currValString;        	
        }

        PreparedStatement ps= null;
        
         try{ 
        	logger.info("Actualizando versiones del inmueble rustico o urbano:"+bien.getId());
            
            ps= conn.prepareStatement(sSQL);
            ps.setLong(1, bien.getId());
            ps.execute();

        }catch (Exception e){
        	e.printStackTrace();        	
            throw e;
        }

        return bien;
    }
    
    private BienBean updateRevisionInventarioFeature(BienBean bien, Connection conn, String nextValString, String currValString)throws Exception{
        if (bien == null) return null;
        
        String sSQL;
        sSQL= "UPDATE INVENTARIO_FEATURE SET REVISION_EXPIRADA="+currValString+
        " WHERE ID_BIEN=? "+sRevisionExpiradaNula+ " AND REVISION_ACTUAL<"+currValString;

        PreparedStatement ps= null;
        
         try{ 
        	logger.info("Actualizando versiones de inventario feature:"+bien.getId());
            
            ps= conn.prepareStatement(sSQL);
            ps.setLong(1, bien.getId());
            ps.execute();

        }catch (Exception e){
        	e.printStackTrace();        	
            throw e;
        }

        return bien;
    }


    public Collection getCuentasContables() throws Exception{
        TreeMap alRet= new TreeMap();
        String sSQL= "SELECT * from contable WHERE cuenta is not null ORDER BY cuenta ASC";
        Connection conn=null;
        PreparedStatement ps= null;
        ResultSet rs= null;
        try{
            conn= CPoolDatabase.getConnection();
            ps= conn.prepareStatement(sSQL);
            rs= ps.executeQuery();
            CuentaContable cc= null;
            while (rs.next()){
                if (rs.getString("CUENTA")!=null && !rs.getString("CUENTA").trim().equalsIgnoreCase("")){
                    cc= new CuentaContable();
                    cc.setId(rs.getInt("ID_CLASIFICACION"));
                    cc.setCuenta(rs.getString("CUENTA"));
                    cc.setDescripcion(rs.getString("DESCRIPCION"));

                    alRet.put(new Long(cc.getId()), cc);
                }
            }
        }catch(Exception ex){
        	logger.error("Error al ejecutar: "+sSQL);
        	throw ex;
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
        }

        return alRet.values();
    }


    public Collection getCuentasAmortizacion() throws Exception{
    	TreeMap alRet= new TreeMap();
        String sSQL= "SELECT * from amortizacion WHERE cuenta is not null ORDER BY descripcion DESC";
        Connection conn=null;
        PreparedStatement ps= null;
        ResultSet rs= null;
        try{
            conn= CPoolDatabase.getConnection();
            ps= conn.prepareStatement(sSQL);
            rs= ps.executeQuery();
            CuentaAmortizacion ca= null;
            while (rs.next()){
                if (rs.getString("CUENTA")!=null && !rs.getString("CUENTA").trim().equalsIgnoreCase("")){
                    ca= new CuentaAmortizacion();
                    ca.setId(rs.getInt("ID"));
                    ca.setCuenta(rs.getString("CUENTA"));
                    ca.setDescripcion(rs.getString("DESCRIPCION"));
                    ca.setAnnos(rs.getInt("ANIOS"));
                    ca.setPorcentaje(rs.getDouble("PORCENTAJE"));
                    ca.setTipoAmortizacion(rs.getString("TIPO_AMORTIZACION"));
                    ca.setTotalAmortizado(rs.getDouble("TOTAL_AMORTIZADO"));

                    alRet.put(new Long(ca.getId()), ca);
                }
            }
        }catch(Exception ex){
        	logger.error("Error al ejecutar: "+sSQL);
        	throw ex;
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
        }

        return alRet.values();
    }

    public void checkPermissionLock(String idLayer, String idFeature, Sesion userSesion) throws Exception{


        String sSQL= "select l.name as name,l.acl as acl " +
                     "from layers l ";

        try{
            Integer.parseInt(idLayer);
            sSQL+= "where l.id_layer= ?";
        }catch(java.lang.NumberFormatException e){
            sSQL+= "where l.name= ?";
        }

        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        long aclLayer= -1;
        String nameLayer= "";
        try{
            conn=CPoolDatabase.getConnection();
            ps=conn.prepareStatement(sSQL);
            ps.setString(1,idLayer);
            rs=ps.executeQuery();
            if (rs.next()){
                aclLayer=rs.getLong("acl");
                nameLayer=rs.getString("name");
            }
            rs.close();
            ps.close();
            /** Permisos sobre el layer */
            if (!checkPermission(userSesion,aclLayer,com.geopista.server.administradorCartografia.Const.PERM_LAYER_WRITE))
                    throw new PermissionException(com.geopista.server.administradorCartografia.Const.PERM_LAYER_WRITE);

            /** Bloqueo sobre la feature */
            //int iLock=canLockFeature(Integer.parseInt(userSesion.getIdMunicipio()),idLayer,Integer.parseInt(idFeature),Integer.parseInt(userSesion.getIdUser()));
            int iLock=canLockFeature(Integer.parseInt(municipio),nameLayer,Integer.parseInt(idFeature),Integer.parseInt(userSesion.getIdUser()));
            if (!(iLock==AdministradorCartografiaClient.LOCK_FEATURE_OWN ||
                  iLock==AdministradorCartografiaClient.LOCK_LAYER_OWN   ||
                  iLock==AdministradorCartografiaClient.LOCK_LAYER_LOCKED)){
                String sMsg=null;
                switch (iLock){
                    case AdministradorCartografiaClient.LOCK_LAYER_OTHER:
                        sMsg="locked: layer "+idLayer;
                        break;
                    case AdministradorCartografiaClient.LOCK_FEATURE_OTHER:
                        sMsg="locked: feature";
                        break;
                    default:
                        sMsg="Lock error";
                }
                throw new LockException(sMsg+" ("+iLock+")");
            }
        }catch(Exception ex){
        	logger.error("Error al ejecutar: "+sSQL);
        	throw ex;
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
        }
    }


    public boolean checkPermission(Sesion sSesion,long lACL,String sPerm) throws Exception{
        GeopistaAcl acl= sSesion.getPerfil(lACL);
        if (acl==null) return false;
        return acl.checkPermission(new GeopistaPermission(sPerm));
    }

    private int canLockFeature(int iMunicipio, String sLayer, int iFeature, int iUser) throws Exception{
        int iRet=0;
        // Comparar ids de Feature y bloqueos de layer con la geometria de iFeature...
        int iFeatureLock=featureLocked(iMunicipio,sLayer,iFeature);
        if(iFeatureLock!=-1)
            return (iFeatureLock==iUser?AdministradorCartografiaClient.LOCK_FEATURE_OWN
                                       :AdministradorCartografiaClient.LOCK_FEATURE_OTHER);
        //Obtener la tabla donde esta la geometria...
        String sSQLTable="select t.name as table,l.id_layer from tables t,columns c,attributes a,layers l " +
                         "where a.id_layer=l.id_layer and l.name=? and c.id_table=t.id_table and c.name='GEOMETRY' and a.id_column=c.id";
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        String sSQLGeom="";
        try{
            conn=CPoolDatabase.getConnection();
            ps=conn.prepareStatement(sSQLTable);
            ps.setString(1,sLayer);
            rs=ps.executeQuery();
            String sTable=null;
            if (rs.next()){
                sTable=rs.getString("table");
            }
            rs.close();
            ps.close();
            if (CPoolDatabase.isPostgres(conn))
                sSQLGeom="select ll.user_id from locks_layer ll,"+sTable+" t " +
                            "where t.id=? and ll.municipio=? and ll.layer=? " +
                            " and t.\"GEOMETRY\" && setsrid(ll.\"GEOMETRY\","+srid.getSRID(iMunicipio)+");";
            else /** Oracle */
                sSQLGeom="select ll.user_id from locks_layer ll,"+sTable+" t " +
                           "where t.id=? and ll.municipio=? and ll.layer=? " +
                           " and sdo_relate(t.geometry,ll.geometry, 'mask=anyinteract querytype=window') = 'TRUE'";

            ps=conn.prepareStatement(sSQLGeom);
            ps.setInt(1,iFeature);
            ps.setInt(2,iMunicipio);
            ps.setString(3,sLayer);
            rs=ps.executeQuery();
            if (rs.next())
                iRet=(rs.getInt("user_id")!=iUser)? AdministradorCartografiaClient.LOCK_LAYER_OTHER
                                                  : AdministradorCartografiaClient.LOCK_LAYER_OWN;
        }catch(Exception ex){
        	logger.error("Error al ejecutar: "+sSQLGeom);
        	throw ex;
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
        }
        return iRet;
    }

    /** devuelve -1 si no bloqueado, id_usuario si hay bloqueo */
    public int featureLocked(int iMunicipio, String sLayer, int iFeature) throws Exception{
        int iRet=-1;
        String sSQL="select user_id,ts from locks_feature " +
                    "where " +
                        "municipio=? and " +
                        "layer=? and " +
                        "feature_id=?";
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try{
            conn=CPoolDatabase.getConnection();
            ps=conn.prepareStatement(sSQL);
            ps.setInt(1,iMunicipio);
            ps.setString(2,sLayer);
            ps.setInt(3,iFeature);
            rs=ps.executeQuery();
            if (rs.next()){
                iRet=rs.getInt("user_id");
            }
        }catch(Exception ex){
        	logger.error("Error al ejecutar: "+sSQL);
        	throw ex;
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
        }
        return iRet;
    }

    public int getIdLayer(String sLayer) throws Exception{
        int idLayer= -1;
        try{
            /** Se trata del identificador del layer */
            idLayer= Integer.parseInt(sLayer);
        }catch(NumberFormatException ex){
            /** Se trata del nombre del layer */
            String sSQL="select id_layer " +
                       "from layers " +
                       "where name= ?";

            Connection conn=null;
            PreparedStatement ps=null;
            ResultSet rs=null;
            try{
                conn=CPoolDatabase.getConnection();
                ps=conn.prepareStatement(sSQL);
                ps.setString(1,sLayer);
                rs=ps.executeQuery();
                if (rs.next()){
                    idLayer=rs.getInt("id_layer");
                }
            }catch(Exception ex2){
            	logger.error("Error al ejecutar: "+sSQL);
            	throw ex2;
            }finally{
                try{rs.close();}catch(Exception e){};
                try{ps.close();}catch(Exception e){};
                try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
            }
        }

        return idLayer;
    }


    public Object[] getIdLayers(Object[] sLayers) throws Exception{
        if ((sLayers==null) || (sLayers.length==0)) return null;
        Object[] idLayers= new Object[sLayers.length];
        for (int i=0; i<sLayers.length; i++){
            idLayers[i]=new Integer(getIdLayer((String)sLayers[i]));
        }
        return idLayers;
    }

    private Collection getCompanniaSeguros() throws Exception{
        java.util.Vector alRet= new java.util.Vector();
        String sSQL= "SELECT * from compannia_seguros ORDER BY nombre ASC";

        Connection conn=null;
        PreparedStatement ps= null;
        ResultSet rs= null;
        CompanniaSeguros compannia= null;
        try{
            conn= CPoolDatabase.getConnection();
            ps= conn.prepareStatement(sSQL);
            rs= ps.executeQuery();
            while (rs.next()){
                compannia= new CompanniaSeguros();
                compannia.setId(rs.getLong("ID"));
                compannia.setDescripcion(rs.getString("DESCRIPCION"));
                compannia.setNombre(rs.getString("NOMBRE"));

                alRet.add(compannia);
            }
        }catch(Exception ex){
        	logger.error("Error al ejecutar: "+sSQL);
        	throw ex;
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
        }
        return (Collection)alRet;
    }


    private Seguro getSeguro(Object objeto) throws Exception{
        String sSQL1= "SELECT s.*, cs.id as ID_CS, cs.nombre as nombre_CS, cs.descripcion as descripcion_CS " +
                      "FROM seguros_inventario s, compannia_seguros cs WHERE s.id_compannia=cs.id AND s.id=?";

        if (objeto==null ) return null;
        Long id=Long.parseLong(objeto.toString());
        if (id.longValue()==-1) return null;
        Connection conn=null;
        PreparedStatement ps= null;
        ResultSet rs= null;
        Seguro seguro= null;
        try{
            conn= CPoolDatabase.getConnection();
            ps= conn.prepareStatement(sSQL1);
            ps.setLong(1, id);
            rs= ps.executeQuery();
            if (rs.next()){
                seguro= new Seguro();
                seguro.setId(id);
                seguro.setDescripcion(rs.getString("DESCRIPCION"));
                Object aux=rs.getObject("POLIZA");
                if (aux!=null)
                	seguro.setPoliza(rs.getLong("POLIZA"));
                aux=rs.getObject("PRIMA");
                if (aux!=null)
                	seguro.setPrima(rs.getDouble("PRIMA"));
                aux=rs.getObject("FECHA_INICIO");
                if (aux!=null)
                    seguro.setFechaInicio(rs.getTimestamp("FECHA_INICIO"));
                aux=rs.getObject("FECHA_VENCIMIENTO");
                if (aux!=null)
                    seguro.setFechaVencimiento(rs.getTimestamp("FECHA_VENCIMIENTO"));
                CompanniaSeguros compannia= new CompanniaSeguros();
                compannia.setId(rs.getLong("ID_CS"));
                compannia.setNombre(rs.getString("NOMBRE_CS"));
                compannia.setDescripcion(rs.getString("DESCRIPCION_CS"));
                seguro.setCompannia(compannia);
            }
        }catch(Exception ex){
        	logger.error("Error al ejecutar: "+sSQL1);
        	throw ex;
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
        }
        return seguro;
    }

    private Long getIdSeguroFromBien(BienBean bien, Connection conn) throws Exception{

        String sSQL= "SELECT id_seguro FROM BIENES_INVENTARIO WHERE id="+bien.getId();

        PreparedStatement ps= null;
        ResultSet rs= null;
        try{
            ps= conn.prepareStatement(sSQL);
            rs= ps.executeQuery();
            if (rs.next()) return rs.getLong("id_seguro");
        }catch(Exception ex){
        	logger.error("Error al ejecutar: "+sSQL);
        	throw ex;
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
        }
        return null;
    }


    private CompanniaSeguros insertCompanniaSeguros(CompanniaSeguros compannia, Connection conn) throws Exception{
        if (compannia==null) return null;
        String sSQL= "INSERT INTO COMPANNIA_SEGUROS (ID, NOMBRE, DESCRIPCION) VALUES(?,?,?)";

        PreparedStatement ps= null;
     	try {
                long id= CPoolDatabase.getNextValue("compannia_seguros" ,"id");
                ps= conn.prepareStatement(sSQL);
                ps.setLong(1, id);
                if (compannia.getNombre()!=null)
                	ps.setString(2, compannia.getNombre());
                else
                	ps.setString(2, "No disponible");
                ps.setString(3, compannia.getDescripcion());
                ps.execute();
                /** actualizamos el seguro */
                compannia.setId(id);
        }catch(Exception ex){
        	logger.error("Error al ejecutar: "+sSQL);
        	throw ex;
        }finally{
            try{ps.close();}catch(Exception e){};
        }

        return compannia;

    }

    private CompanniaSeguros updateCompanniaSeguros(CompanniaSeguros compannia, Connection conn) throws Exception{
        if (compannia==null) return null;
        String sSQL= "UPDATE COMPANNIA_SEGUROS SET NOMBRE=?, DESCRIPCION=? WHERE ID=?";
        PreparedStatement ps= null;
		try {
            ps= conn.prepareStatement(sSQL);
            ps.setString(1, compannia.getNombre());
            ps.setString(2, compannia.getDescripcion());
            ps.setLong(3, compannia.getId());
            ps.execute();
        }catch (Exception e){
            throw e;
        }finally{
            try{ps.close();}catch(Exception e){};
        }
        return compannia;
    }


    private Seguro insertarSeguro(Seguro seguro, Connection conn) throws Exception{
        if (seguro==null) return null;
        String sSQL= "INSERT INTO SEGUROS_INVENTARIO (ID, DESCRIPCION, PRIMA, POLIZA, FECHA_INICIO, FECHA_VENCIMIENTO, ID_COMPANNIA) VALUES(?,?,?,?,?,?,?)";

        PreparedStatement ps= null;
      	try {
  				//logger.error("Fecha Inicio Seguro:"+ seguro.getFechaInicio());
  				//logger.error("Fecha Fin Seguro:"+ seguro.getFechaVencimiento());
      			//logger.error("Fecha Inicio Seguro:"+ new Timestamp(seguro.getFechaInicio().getTime()));
      			//logger.error("Fecha Fin Seguro:"+ new Timestamp(seguro.getFechaVencimiento().getTime()));
        	
                long id= CPoolDatabase.getNextValue("seguros_inventario", "id");
                ps= conn.prepareStatement(sSQL);
                ps.setLong(1, id);
                ps.setString(2, seguro.getDescripcion());
                if (seguro.getPrima()==null)
                	ps.setNull(3, java.sql.Types.DOUBLE);
                else
                	ps.setDouble(3, seguro.getPrima());
                if (seguro.getPoliza()==null)
                	ps.setNull(4,java.sql.Types.DOUBLE);
                else
                	ps.setDouble(4, seguro.getPoliza());
                if (seguro.getFechaInicio() == null)
                    ps.setNull(5, java.sql.Types.TIMESTAMP);
                else ps.setTimestamp(5, new Timestamp(seguro.getFechaInicio().getTime()));
                if (seguro.getFechaVencimiento() == null)
                    ps.setNull(6, java.sql.Types.TIMESTAMP);
                else ps.setTimestamp(6, new Timestamp(seguro.getFechaVencimiento().getTime()));
                /** Un seguro tiene que pertenecer OBLIGATORIAMENTE a una compannia */
                if (seguro.getCompannia().getId()==null || seguro.getCompannia().getId()==-1){
                    seguro.setCompannia(insertCompanniaSeguros(seguro.getCompannia(), conn));
                }else seguro.setCompannia(updateCompanniaSeguros(seguro.getCompannia(), conn));
                ps.setLong(7, seguro.getCompannia().getId());

                ps.execute();
                /** actualizamos el seguro */
                seguro.setId(id);
        }catch(Exception ex){
        	logger.error("Error al ejecutar: "+sSQL);
        	logger.error("Fecha Inicio Seguro:"+seguro.getFechaInicio());
        	logger.error("Fecha Fin Seguro:"+seguro.getFechaVencimiento());
        	throw ex;
        }finally{
            try{ps.close();}catch(Exception e){};
        }

        return seguro;
    }

    private Seguro updateSeguro(Seguro seguro, Connection conn) throws Exception{
        if (seguro==null) return null;
        String sSQL= "UPDATE SEGUROS_INVENTARIO SET DESCRIPCION=?, PRIMA=?, POLIZA=?, FECHA_INICIO=?, FECHA_VENCIMIENTO=?, ID_COMPANNIA=? WHERE ID=?";
        PreparedStatement ps= null;
		try {
            ps= conn.prepareStatement(sSQL);
            ps.setString(1, seguro.getDescripcion());
            if (seguro.getPrima()!=null)
            	ps.setDouble(2, seguro.getPrima());
            else
            	ps.setObject(2,null);
            if (seguro.getPoliza()!=null)
            	ps.setLong(3, seguro.getPoliza());
            else
            	ps.setObject(3, null);
            if (seguro.getFechaInicio() == null)
                ps.setNull(4, java.sql.Types.TIMESTAMP);
            else ps.setTimestamp(4, new Timestamp(seguro.getFechaInicio().getTime()));
            if (seguro.getFechaVencimiento() == null)
                ps.setNull(5, java.sql.Types.TIMESTAMP);
            else ps.setTimestamp(5, new Timestamp(seguro.getFechaVencimiento().getTime()));
            /** Un seguro tiene que pertenecer OBLIGATORIAMENTE a una compannia */
            if (seguro.getCompannia().getId()==-1){
                seguro.setCompannia(insertCompanniaSeguros(seguro.getCompannia(), conn));
            }else seguro.setCompannia(updateCompanniaSeguros(seguro.getCompannia(), conn));
            ps.setLong(6, seguro.getCompannia().getId());
            ps.setLong(7, seguro.getId());
            ps.execute();
        }catch (Exception e){
            throw e;
        }finally{
            try{ps.close();}catch(Exception e){};
        }
        return seguro;
    }

    private void deleteSeguro(long id, Connection conn) throws Exception{
        String sSQL= "DELETE FROM seguros_inventario WHERE ID=?";
        PreparedStatement ps= null;
		try {
            ps= conn.prepareStatement(sSQL);
            ps.setLong(1, id);
            ps.execute();
        }catch (Exception e){
            throw e;
        }finally{
            try{ps.close();}catch(Exception e){};
        }
    }

    private ListaEIEL getEIEL(long idBien, Connection conn) throws Exception{

    	PreparedStatement ps= null;
        ResultSet rs= null;
        ListaEIEL listaEIEL = new ListaEIEL();
    	String sSQL= "SELECT vista_eiel, union_clave_eiel FROM eiel_inventario WHERE id_inventario=?";

        try{
	    	ps= conn.prepareStatement(sSQL);
	    	ps.setLong(1, idBien);
	        rs= ps.executeQuery();
	        if (rs.next()){
	        	String vista_eiel = rs.getString("vista_eiel");
	        	String union_clave_eiel = rs.getString("union_clave_eiel");
	        	String tipo = "";
	        	
	        	sSQL= "select * from v_integ_eiel as v where v.union_clave_eiel = ?";
	        	
	        	tipo = obtenerTipo(vista_eiel);
	        	InventarioEIELBean eiel = null;
		        
	            ps= conn.prepareStatement(sSQL);
	            ps.setString(1, union_clave_eiel);
	            rs= ps.executeQuery();
	            while (rs.next()){
	            	eiel = new InventarioEIELBean();
	            	eiel.setUnionClaveEIEL(union_clave_eiel);
	            	eiel.setNombreEIEL(rs.getString("nombre"));
	            	eiel.setEstadoEIEL(rs.getString("estado"));
	            	eiel.setGestionEIEL(rs.getString("gestor"));
	            	eiel.setTipoEIEL(tipo);
	            	listaEIEL.add(eiel);

	            }
	        }

        }catch(Exception ex){
        	logger.error("Error al ejecutar: "+sSQL);
        	throw ex;
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
        }
        return listaEIEL;
    }    
    
    private Collection<Observacion> getObservaciones(long idBien, String tabla, Connection conn, long revision)throws Exception{
        java.util.Vector<Observacion> alRet= new java.util.Vector<Observacion>();

        String sSQL= "SELECT * from "+ tabla +"  WHERE id_bien=?  and revision_actual=?  ORDER BY fecha DESC";
        PreparedStatement ps= null;
        ResultSet rs= null;
        Observacion obs= null;
        try{
            ps= conn.prepareStatement(sSQL);
            ps.setLong(1, idBien);
            ps.setLong(2, revision);
            rs= ps.executeQuery();
            while (rs.next()){
                obs= new Observacion();
                obs.setId(rs.getLong("ID"));
                obs.setDescripcion(rs.getString("DESCRIPCION"));
                obs.setFecha(rs.getTimestamp("FECHA"));
                obs.setIdBien(idBien);
                obs.setRevisionActual(rs.getLong("revision_actual"));
                alRet.add(obs);
            }
        }catch(Exception ex){
        	logger.error("Error al ejecutar: "+sSQL);
        	throw ex;
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
        }
        return (Collection<Observacion>)alRet;
    }
    private Collection<Observacion> getObservaciones(long idBien, String tabla, Connection conn)throws Exception{
        java.util.Vector<Observacion> alRet= new java.util.Vector<Observacion>();

        String sSQL= "SELECT * from "+ tabla +"  WHERE id_bien=?    ORDER BY fecha DESC";
        PreparedStatement ps= null;
        ResultSet rs= null;
        Observacion obs= null;
        try{
            ps= conn.prepareStatement(sSQL);
            ps.setLong(1, idBien);
            rs= ps.executeQuery();
            while (rs.next()){
                obs= new Observacion();
                obs.setId(rs.getLong("ID"));
                obs.setDescripcion(rs.getString("DESCRIPCION"));
                obs.setFecha(rs.getTimestamp("FECHA"));
                obs.setIdBien(idBien);
                alRet.add(obs);
            }
        }catch(Exception ex){
        	logger.error("Error al ejecutar: "+sSQL);
        	throw ex;
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
        }
        return (Collection<Observacion>)alRet;
    }

    private Observacion insertObservacion(long idBien, Observacion obs, String nombreTabla, Connection conn, String revision) throws Exception{
        if (obs==null) return null;

        String sSQL= "INSERT INTO "+nombreTabla+" (ID, ID_BIEN, DESCRIPCION, FECHA, REVISION_ACTUAL) VALUES (?,?,?,?,"+revision+")";

        PreparedStatement ps= null;
        
       try {
                long id= CPoolDatabase.getNextValue(nombreTabla, "id");
                logger.info("Insertando la observacion:"+id+" con id_bien:"+idBien);
                java.util.Date date=new java.util.Date();
                if (obs.getFecha()!=null)
                	date=obs.getFecha();
                ps= conn.prepareStatement(sSQL);
                ps.setLong(1, id);
                ps.setLong(2, idBien);
                ps.setString(3, obs.getDescripcion());
                ps.setTimestamp(4, new Timestamp(date.getTime()));
                ps.execute();
                /** actualizamos la observacion */
                obs.setId(id);
                obs.setFecha(date);
                obs.setIdBien(idBien);
                
        }catch(Exception ex){
        	logger.error("Error al ejecutar: "+sSQL);
        	throw ex;
        }finally{
            try{ps.close();}catch(Exception e){};
        }

        return obs;

    }
    private Observacion insertObservacion(long idBien, Observacion obs, String nombreTabla, Connection conn) throws Exception{
        if (obs==null) return null;

        String sSQL= "INSERT INTO "+nombreTabla+" (ID, ID_BIEN, DESCRIPCION, FECHA) VALUES (?,?,?,?)";

        PreparedStatement ps= null;
       try {
            long id= CPoolDatabase.getNextValue(nombreTabla, "id");
                java.util.Date date=new java.util.Date();
                if (obs.getFecha()!=null)
                	date=obs.getFecha();
                ps= conn.prepareStatement(sSQL);
                ps.setLong(1, id);
                ps.setLong(2, idBien);
                ps.setString(3, obs.getDescripcion());
                ps.setTimestamp(4, new Timestamp(date.getTime()));
                ps.execute();
                /** actualizamos la observacion */
                obs.setId(id);
                obs.setFecha(date);
                obs.setIdBien(idBien);
                
        }catch(Exception ex){
        	logger.error("Error al ejecutar: "+sSQL);
        	throw ex;
        }finally{
            try{ps.close();}catch(Exception e){};
        }

        return obs;

    }

    private Observacion updateObservacion(Observacion obs, String tabla, Connection conn) throws Exception{
        if (obs==null) return null;
        String sSQL= "UPDATE "+ tabla+" SET DESCRIPCION=?, FECHA_ULTIMA_MODIFICACION=? WHERE ID=?";
        PreparedStatement ps= null;
		try {
            java.util.Date date= new java.util.Date();
            ps= conn.prepareStatement(sSQL);
            ps.setString(1, obs.getDescripcion());
            ps.setTimestamp(2, new Timestamp(date.getTime()));
            ps.setLong(3, obs.getId());
            ps.execute();

            /** actualizamos la observacion */
            obs.setFechaUltimaModificacion(date);

        }catch (Exception e){
            throw e;
        }finally{
            try{ps.close();}catch(Exception e){};
        }
        return obs;
    }

    private void deleteObservacion(Observacion obs, String tabla, Connection conn) throws Exception{
        if (obs==null) return;
        String sSQL= "DELETE FROM "+tabla+" WHERE ID=?";
        PreparedStatement ps= null;
		try {
            ps= conn.prepareStatement(sSQL);
            ps.setLong(1, obs.getId());
            ps.execute();
        }catch (Exception e){
            throw e;
        }finally{
            try{ps.close();}catch(Exception e){};
        }
    }


    private void deleteAllObservaciones(long idBien, String tabla, Connection conn) throws Exception{
        PreparedStatement ps= null;
        String sSQL= "DELETE FROM "+tabla+" WHERE ID_BIEN=?";
		try {
            ps= conn.prepareStatement(sSQL);
            ps.setLong(1, idBien);
            ps.execute();
        }catch (Exception e){
            throw e;
        }finally{
            try{ps.close();}catch(Exception e){};
        }
    }

    private Collection actualizarObservaciones(long idBien, Collection observaciones, String tabla, Connection conn, String revision)  throws Exception{
    	PreparedStatement ps = null;
    	ResultSet rs = null;
		long revisionActual = -1;
    	try{
    		conn.prepareStatement(revision);
    		rs = ps.executeQuery();
    		if (rs.next()){
    			revisionActual = rs.getLong(1);
    		}
    	}catch (Exception e){
            throw e;
    	}finally{ 		
    		rs.close();
    		ps.close();
    	}
    
        Object[] auxs= getObservaciones(idBien,tabla, conn, revisionActual).toArray();
        if (auxs.length==0) auxs= null;
        java.util.Hashtable hash= null;
        if (auxs!=null){
            hash= new java.util.Hashtable();
            for (int i=0;i<auxs.length;i++){
                hash.put(((Observacion)auxs[i]).getId()+"-"+((Observacion)auxs[i]).getRevisionActual(), (Observacion)auxs[i]);
                
            }
        }

        if ((observaciones==null) && (hash!=null)){
            /* borramos TODAS las observaciones */
            deleteAllObservaciones(idBien, tabla, conn);
        }
        if ((observaciones != null) &&(hash != null)){
            Object[] objs= observaciones.toArray();
            for (int i=0; i<objs.length;i++){
                Observacion obs= (Observacion)objs[i];
                if (hash.containsKey(obs.getId()+"-"+obs.getRevisionActual())){
                    updateObservacion(obs, tabla,conn);
                    hash.remove(obs.getId()+"-"+obs.getRevisionActual());
                }else insertObservacion(idBien, obs, tabla,conn, revision);
            }
            /** si quedan observaciones en la hash ==> Borramos */
            for (Enumeration e= hash.keys(); e.hasMoreElements();){
                deleteObservacion((Observacion)hash.get((String)e.nextElement()), tabla,conn);
            }
        }
        if ((observaciones != null) && (hash == null)){
            /* Insertamos TODAS las observaciones */
            Object[] objs= observaciones.toArray();
            for (int i=0; i<objs.length;i++){
                Observacion obs= (Observacion)objs[i];
                insertObservacion(idBien, obs, tabla, conn, revision);
            }
        }

        return observaciones;

    }

    private Collection actualizarObservaciones(long idBien, Collection observaciones, String tabla, Connection conn) throws Exception{
        Object[] auxs= getObservaciones(idBien,tabla, conn).toArray();
        if (auxs.length==0) auxs= null;
        java.util.Hashtable hash= null;
        if (auxs!=null){
            hash= new java.util.Hashtable();
            for (int i=0;i<auxs.length;i++){
                hash.put(new Long(((Observacion)auxs[i]).getId()), (Observacion)auxs[i]);
            }
        }

        if ((observaciones==null) && (hash!=null)){
            /* borramos TODAS las observaciones */
            deleteAllObservaciones(idBien, tabla, conn);
        }
        if ((observaciones != null) &&(hash != null)){
            Object[] objs= observaciones.toArray();
            for (int i=0; i<objs.length;i++){
                Observacion obs= (Observacion)objs[i];
                if (hash.containsKey(new Long(obs.getId()))){
                    updateObservacion(obs, tabla,conn);
                    hash.remove(new Long(obs.getId()));
                }else insertObservacion(idBien, obs, tabla,conn);
            }
            /** si quedan observaciones en la hash ==> Borramos */
            for (Enumeration e= hash.keys(); e.hasMoreElements();){
                deleteObservacion((Observacion)hash.get((Long)e.nextElement()),tabla, conn);
            }
        }
        if ((observaciones != null) && (hash == null)){
            /* Insertamos TODAS las observaciones */
            Object[] objs= observaciones.toArray();
            for (int i=0; i<objs.length;i++){
                Observacion obs= (Observacion)objs[i];
                insertObservacion(idBien, obs, tabla, conn);
            }
        }

        return observaciones;

    }

    private Collection<Mejora> getMejoras(long idBien, Connection conn,long revision) throws Exception{
        java.util.Vector<Mejora> alRet= new java.util.Vector<Mejora>();
        String sSQL= "SELECT * from mejoras_inventario WHERE id_bien=? and revision_actual=? ORDER BY fecha_ejecucion DESC";
        PreparedStatement ps= null;
        ResultSet rs= null;
        Mejora mejora= null;
        try{
            ps= conn.prepareStatement(sSQL);
            ps.setLong(1, idBien);
            ps.setLong(2, revision);
            rs= ps.executeQuery();
            while (rs.next()){
                mejora= new Mejora();
                mejora.setId(rs.getLong("ID"));
                mejora.setDescripcion(rs.getString("DESCRIPCION"));
                mejora.setFechaEntrada(rs.getTimestamp("FECHA"));
                mejora.setFechaEjecucion(rs.getTimestamp("FECHA_EJECUCION"));
                mejora.setFechaUltimaModificacion(rs.getTimestamp("FECHA_ULTIMA_MODIFICACION"));
                mejora.setImporte(rs.getDouble("IMPORTE"));
                mejora.setIdBien(idBien);
                mejora.setRevision(rs.getLong("REVISION_ACTUAL"));
                alRet.add(mejora);
            }
        }catch(Exception ex){
        	logger.error("Error al ejecutar: "+sSQL);
        	throw ex;
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
        }
        return (Collection<Mejora>)alRet;
    }


    private Mejora insertMejora(long idBien, Mejora mejora, Connection conn, String revision) throws Exception{
        if (mejora==null) return null;
        String sSQL= "INSERT INTO mejoras_inventario (ID, ID_BIEN, DESCRIPCION, FECHA, FECHA_EJECUCION, IMPORTE, REVISION_ACTUAL) VALUES (?,?,?,?,?,?,"+revision+")";

        PreparedStatement ps= null;
       try {
                java.util.Date date= new java.util.Date();
                if (mejora.getFechaEntrada()!=null)
                	date=mejora.getFechaEntrada();
                long id= CPoolDatabase.getNextValue("mejoras_inventario","id");
                ps= conn.prepareStatement(sSQL);
                ps.setLong(1, id);
                ps.setLong(2, idBien);
                ps.setString(3, mejora.getDescripcion());
                ps.setTimestamp(4, new Timestamp(date.getTime()));
                if (mejora.getFechaEjecucion() == null)
                    ps.setNull(5, java.sql.Types.TIMESTAMP);
                else ps.setTimestamp(5, new Timestamp(mejora.getFechaEjecucion().getTime()));
                ps.setDouble(6, new Double(mejora.getImporte()).doubleValue());
                ps.execute();
                /** actualizamos la mejora */
                mejora.setId(id);
                mejora.setFechaEntrada(date);
                mejora.setIdBien(idBien);
        }catch(Exception ex){
        	logger.error("Error al ejecutar: "+sSQL);
        	throw ex;
        }finally{
            try{ps.close();}catch(Exception e){};
        }

        return mejora;

    }


    private Mejora updateMejora(Mejora mejora, Connection conn) throws Exception{
        if (mejora==null) return null;
        String sSQL= "UPDATE mejoras_inventario SET DESCRIPCION=?, FECHA_ULTIMA_MODIFICACION=?, FECHA_EJECUCION=?, IMPORTE=? WHERE ID=?";
        PreparedStatement ps= null;
		try {
            java.util.Date date= new java.util.Date();
            ps= conn.prepareStatement(sSQL);
            ps.setString(1, mejora.getDescripcion());
            ps.setTimestamp(2, new Timestamp(date.getTime()));
            if (mejora.getFechaEjecucion() == null)
                ps.setNull(3, java.sql.Types.TIMESTAMP);
            else ps.setTimestamp(3, new Timestamp(mejora.getFechaEjecucion().getTime()));
            ps.setDouble(4, new Double(mejora.getImporte()).doubleValue());
            ps.setLong(5, mejora.getId());
            ps.execute();

            /** actualizamos la mejora */
            mejora.setFechaUltimaModificacion(date);

        }catch (Exception e){
            throw e;
        }finally{
            try{ps.close();}catch(Exception e){};
        }
        return mejora;
    }

    private void deleteMejora(Mejora mejora, Connection conn) throws Exception{
        if (mejora==null) return;
        String sSQL= "DELETE FROM mejoras_inventario WHERE ID=?";
        PreparedStatement ps= null;
		try {
            ps= conn.prepareStatement(sSQL);
            ps.setLong(1, mejora.getId());
            ps.execute();
        }catch (Exception e){
            throw e;
        }finally{
            try{ps.close();}catch(Exception e){};
        }
    }


    private void deleteAllMejoras(long idBien, Connection conn) throws Exception{
        String sSQL= "DELETE FROM mejoras_inventario WHERE ID_BIEN=?";
        PreparedStatement ps= null;
		try {
            ps= conn.prepareStatement(sSQL);
            ps.setLong(1, idBien);
            ps.execute();
        }catch (Exception e){
            throw e;
        }finally{
            try{ps.close();}catch(Exception e){};
        }
    }

    private Collection<Mejora> actualizarMejoras(long idBien, Collection<Mejora> mejoras, Connection conn,String revision) throws Exception{
    	PreparedStatement ps = null;
    	ResultSet rs = null;
    	long revisionActual = -1;
    	try{
    		ps = conn.prepareStatement(revision);
    		rs = ps.executeQuery();
    	
    		if (rs.next()){
    			revisionActual = rs.getLong(1);
    		}
    	}
    	catch(Exception e){
    		throw e;    		
    	}finally{
    		try{ps.close();}catch(Exception e){};
    		try{rs.close();}catch(Exception e){};
    	}
        Object[] auxs= getMejoras(idBien, conn, revisionActual).toArray();
        if (auxs.length==0) auxs= null;
        java.util.Hashtable hash= null;
        if (auxs!=null){
            hash= new java.util.Hashtable();
            for (int i=0;i<auxs.length;i++){
                hash.put(((Mejora)auxs[i]).getId()+"-"+((Mejora)auxs[i]).getRevision(), (Mejora)auxs[i]);
            }
        }
        if ((mejoras==null) && (hash!=null)){
            /* borramos TODAS las mejoras */
            deleteAllMejoras(idBien, conn);
        }
        if ((mejoras != null) &&(hash != null)){
            Object[] objs= mejoras.toArray();
            for (int i=0; i<objs.length;i++){
                Mejora mejora= (Mejora)objs[i];
                if (hash.containsKey(mejora.getId()+"-"+mejora.getRevision())){
                    updateMejora(mejora, conn);
                    hash.remove(mejora.getId()+"-"+mejora.getRevision());
                }else insertMejora(idBien, mejora, conn, revision);
            }
            /** si quedan mejoras en la hash ==> Borramos */
            for (Enumeration e= hash.keys(); e.hasMoreElements();){
                deleteMejora((Mejora)hash.get((String)e.nextElement()), conn);
            }
        }
        if ((mejoras != null) && (hash == null)){
            /* Insertamos TODAS las mejoras */
            Object[] objs= mejoras.toArray();
            for (int i=0; i<objs.length;i++){
                Mejora mejora= (Mejora)objs[i];
                insertMejora(idBien, mejora, conn, revision);
            }
        }

        return mejoras;

    }


    private Collection<ReferenciaCatastral> getRefCatastrales(long idBien, Connection conn, long revision)  throws Exception{
        java.util.Vector<ReferenciaCatastral> alRet= new java.util.Vector<ReferenciaCatastral>();
        String sSQL= "SELECT * from refcatastrales_inventario WHERE id_bien=? and revision_actual=? ORDER BY ref_catastral DESC";

        PreparedStatement ps= null;
        ResultSet rs= null;
        ReferenciaCatastral ref= null;
        try{
            ps= conn.prepareStatement(sSQL);
            ps.setLong(1, idBien);
            ps.setLong(2,revision);
            rs= ps.executeQuery();
            while (rs.next()){
                ref= new ReferenciaCatastral();
                ref.setId(rs.getLong("ID"));
                ref.setDescripcion(rs.getString("DESCRIPCION"));
                ref.setRefCatastral(rs.getString("REF_CATASTRAL"));
                ref.setIdBien(idBien);
                ref.setRevision(rs.getLong("REVISION_ACTUAL"));
                alRet.add(ref);
            }
        }catch(Exception ex){
        	logger.error("Error al ejecutar: "+sSQL);
        	throw ex;
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
        }
        return (Collection<ReferenciaCatastral>)alRet;
    }

    private ReferenciaCatastral insertRefCatastral(long idBien, ReferenciaCatastral ref, Connection conn, String revision)throws Exception{
        if (ref==null) return null;
        String sSQL= "INSERT INTO refcatastrales_inventario (ID, ID_BIEN, DESCRIPCION, REF_CATASTRAL, REVISION_ACTUAL) VALUES (?,?,?,?,"+revision+")";

        PreparedStatement ps= null;
        try {
                long id= CPoolDatabase.getNextValue("refcatastrales_inventario", "id");
                ps= conn.prepareStatement(sSQL);
                ps.setLong(1, id);
                ps.setLong(2, idBien);
                ps.setString(3, ref.getDescripcion());
                ps.setString(4, ref.getRefCatastral());
                ps.execute();
                /** actualizamos la mejora */
                ref.setId(id);
                ref.setIdBien(idBien);
        }catch(Exception ex){
        	logger.error("Error al ejecutar: "+sSQL);
        	throw ex;
        }finally{
            try{ps.close();}catch(Exception e){};
        }

        return ref;

    }


    private void deleteAllRefCatastrales(long idBien, Connection conn) throws Exception{
        String sSQL= "DELETE FROM refcatastrales_inventario WHERE ID_BIEN=?";

        PreparedStatement ps= null;
		try {
            ps= conn.prepareStatement(sSQL);
            ps.setLong(1, idBien);
            ps.execute();
        }catch (Exception e){
            throw e;
        }finally{
            try{ps.close();}catch(Exception e){};
        }
    }

    private Collection<ReferenciaCatastral> actualizarRefCatastrales(long idBien, Collection<ReferenciaCatastral> refs, Connection conn, String revision) throws Exception{
        /* borramos TODAS las referencias catastrales */
        //deleteAllRefCatastrales(idBien, conn);
        /** insertamos las referencias catastrales */
        if (refs != null){
            Object[] objs= refs.toArray();
            for (int i=0; i<objs.length;i++){
                insertRefCatastral(idBien, (ReferenciaCatastral)objs[i], conn, revision);
            }
        }
        return refs;

    }


private Collection<UsoFuncional> getUsosFuncionales(long idBien, Connection conn, long revision) throws Exception{
    java.util.Vector<UsoFuncional>  alRet= new java.util.Vector<UsoFuncional> ();
    String sSQL= "SELECT * from usos_funcionales_inventario WHERE id_bien=? and revision_actual=? ORDER BY fecha DESC";
    PreparedStatement ps= null;
    ResultSet rs= null;
    UsoFuncional uso= null;
    try{
        ps= conn.prepareStatement(sSQL);
        ps.setLong(1, idBien);
        ps.setLong(2, revision);
        rs= ps.executeQuery();
        while (rs.next()){
            uso= new UsoFuncional();
            uso.setId(rs.getLong("ID"));
            uso.setUso(rs.getString("USO"));
            uso.setFecha(rs.getTimestamp("FECHA"));
            uso.setSuperficie(rs.getDouble("SUPERFICIE"));
            uso.setIdBien(idBien);
            uso.setRevision(rs.getLong("REVISION_ACTUAL"));
            alRet.add(uso);
        }
    }catch(Exception ex){
    	logger.error("Error al ejecutar: "+sSQL);
    	throw ex;
    }finally{
        try{rs.close();}catch(Exception e){};
        try{ps.close();}catch(Exception e){};
    }
    return (Collection<UsoFuncional> )alRet;
}


private UsoFuncional insertUsoFuncional(long idBien, UsoFuncional uso, Connection conn, String revision)  throws Exception{
    if (uso==null) return null;
    String sSQL= "INSERT INTO usos_funcionales_inventario (ID, ID_BIEN, USO, FECHA, SUPERFICIE, REVISION_ACTUAL) VALUES (?,?,?,?,?,"+revision+")";

    PreparedStatement ps= null;
    try {
            java.util.Date date= new java.util.Date();
            if (uso.getFecha()!=null)
            	date=uso.getFecha();
            long id= CPoolDatabase.getNextValue("usos_funcionales_inventario", "id");
         
            ps= conn.prepareStatement(sSQL);
            ps.setLong(1, id);
            ps.setLong(2, idBien);
            ps.setString(3, uso.getUso());
            ps.setTimestamp(4, new Timestamp(date.getTime()));
            ps.setDouble(5, new Double(uso.getSuperficie()).doubleValue());
            ps.execute();
            /** actualizamos el uso */
            uso.setId(id);
            uso.setFecha(date);
            uso.setIdBien(idBien);
    }catch(Exception ex){
    	logger.error("Error al ejecutar: "+sSQL);
    	throw ex;
    }finally{
        try{ps.close();}catch(Exception e){};
    }

    return uso;

}


private void deleteAllUsosFuncionales(long idBien, Connection conn) throws Exception{
    String sSQL= "DELETE FROM usos_funcionales_inventario WHERE ID_BIEN=?";
    PreparedStatement ps= null;
    try {
        ps= conn.prepareStatement(sSQL);
        ps.setLong(1, idBien);
        ps.execute();
    }catch (Exception e){
        throw e;
    }finally{
        try{ps.close();}catch(Exception e){};
    }
}


private Collection<UsoFuncional> actualizarUsosFuncionales(long idBien, Collection<UsoFuncional> usos, Connection conn, String revision)  throws Exception{
    /* borramos TODOS los usos funcionales */
    //deleteAllUsosFuncionales(idBien, conn);
    /** insertamos los usos funcionales */
    if (usos != null){
        Object[] objs= usos.toArray();
        for (int i=0; i<objs.length;i++){
            insertUsoFuncional(idBien, (UsoFuncional)objs[i], conn, revision);
        }
    }
    return usos;

}


private BienBean recuperarInventario(BienBean bien, Sesion userSesion) throws Exception{
    PreparedStatement ps= null;
    Connection conn= null;

    try {
        conn= CPoolDatabase.getConnection();
        conn.setAutoCommit(false);

        if (bien instanceof InmuebleBean){
        	updateBienesInventario((InmuebleBean)bien, conn);
        	insertarBienInventario(bien, userSesion, conn, sMaxRevision.replaceAll("\\?", "inmuebles"), sRevisionActual.replaceAll("\\?", "inmuebles"));
            deleteInmueble((InmuebleBean)bien, conn);
            insertInmueble(conn, (InmuebleBean)bien, sRevisionActual.replaceAll("\\?", "inmuebles"), userSesion);
            insertInmuebleUrbanoRustico(conn, (InmuebleBean)bien, sRevisionActual.replaceAll("\\?", "inmuebles"), userSesion);
        }else if (bien instanceof MuebleBean){
        	updateBienesInventario((InmuebleBean)bien, conn);
            insertarBienInventario(bien, userSesion, conn, sMaxRevision.replaceAll("\\?", "muebles"), sRevisionActual.replaceAll("\\?", "muebles"));
            deleteMueble((MuebleBean)bien, conn);
            insertMueble(conn, (MuebleBean)bien, sRevisionActual.replaceAll("\\?", "muebles"), userSesion);
       }else if (bien instanceof DerechoRealBean){
    	   	 updateBienesInventario((InmuebleBean)bien, conn);
    	     insertarBienInventario(bien, userSesion, conn, sMaxRevision.replaceAll("\\?", "derechos_reales"), sRevisionActual.replaceAll("\\?", "derechos_reales"));
             deleteDerecho((DerechoRealBean)bien, conn);
             insertDerechoReal(conn, (DerechoRealBean)bien, sRevisionActual.replaceAll("\\?", "derechos_reales"), userSesion);
       }else if (bien instanceof ValorMobiliarioBean){
    	     updateBienesInventario((InmuebleBean)bien, conn);
    	     insertarBienInventario(bien, userSesion, conn, sMaxRevision.replaceAll("\\?", "valor_mobiliario"), sRevisionActual.replaceAll("\\?", "valor_mobiliario"));
             deleteValorMobiliario((ValorMobiliarioBean)bien, conn);
             insertValorMobiliario(conn, (ValorMobiliarioBean)bien, sRevisionActual.replaceAll("\\?", "valor_mobiliario"), userSesion);
       }else if (bien instanceof CreditoDerechoBean){
       		 updateBienesInventario((InmuebleBean)bien, conn);
    	     insertarBienInventario(bien, userSesion, conn, sMaxRevision.replaceAll("\\?", "credito_derecho"), sRevisionActual.replaceAll("\\?", "credito_derecho"));
             deleteCreditoDerecho((CreditoDerechoBean)bien, conn);
             insertCreditoDerecho(conn, (CreditoDerechoBean)bien, sRevisionActual.replaceAll("\\?", "credito_derecho"), userSesion);
         }else if (bien instanceof SemovienteBean){
         	 updateBienesInventario((InmuebleBean)bien, conn);
             insertarBienInventario(bien, userSesion, conn, sMaxRevision.replaceAll("\\?", "semoviente"), sRevisionActual.replaceAll("\\?", "semoviente"));
             deleteSemoviente((SemovienteBean)bien, conn);
             insertSemoviente(conn, (SemovienteBean)bien, sRevisionActual.replaceAll("\\?", "semoviente"), userSesion);
         }else if (bien instanceof ViaBean){
         	 updateBienesInventario((InmuebleBean)bien, conn);
             insertarBienInventario(bien, userSesion, conn, sMaxRevision.replaceAll("\\?", "vias_inventario"), sRevisionActual.replaceAll("\\?", "vias_inventario"));
             deleteVia((ViaBean)bien, conn);
             insertVia(conn, (ViaBean)bien, sRevisionActual.replaceAll("\\?", "vias_inventario"), userSesion);
         }else if (bien instanceof VehiculoBean){
         	 updateBienesInventario((InmuebleBean)bien, conn);
             insertarBienInventario(bien, userSesion, conn, sMaxRevision.replaceAll("\\?", "vehiculo"), sRevisionActual.replaceAll("\\?", "vehiculo"));
             deleteVehiculo((VehiculoBean)bien, conn);
             insertVehiculo(conn, (VehiculoBean)bien, sRevisionActual.replaceAll("\\?", "vehiculo"), userSesion);
         }
        conn.commit();
        
    }catch (Exception e){
        throw e;
    }finally{
        try{ps.close();}catch(Exception e){};
        try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){}
    }

    return bien;
}
private BienRevertible recuperarRevertible(BienRevertible bienRevertible, Sesion userSesion) throws Exception{
	 Connection conn= null;
    try {
    	 conn= CPoolDatabase.getConnection();
         conn.setAutoCommit(false);
    	 updateBienesRevertibleInventario(bienRevertible, conn);
    	 bienRevertible=insertBienRevertible(bienRevertible, userSesion, conn);
    	
    	conn.commit();
    }catch (Exception e){
        throw e;
    }finally{
        try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){}
    }

    return bienRevertible;
}


private BienBean borrarInventario(BienBean bien, Sesion userSesion) throws Exception{
    String sSQL= "UPDATE bienes_inventario SET BORRADO='1', FECHA_BAJA=? WHERE ID=? AND ID_MUNICIPIO=?";
    PreparedStatement ps= null;
    Connection conn= null;

    try {
        java.util.Date date= new java.util.Date();

        conn= CPoolDatabase.getConnection();
        conn.setAutoCommit(false);
        ps= conn.prepareStatement(sSQL);
        ps.setTimestamp(1, new Timestamp(date.getTime()));
        ps.setLong(2,bien.getId());
        ps.setString(3, municipio);
        ps.execute();
        conn.commit();
        bien.setBorrado("1");
        bien.setFechaBaja(date);

    }catch (Exception e){
        throw e;
    }finally{
        try{ps.close();}catch(Exception e){};
        try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){}
    }

    return bien;
}

private BienRevertible borrarBienRevertible(BienRevertible bienRevertible, Sesion userSesion) throws Exception{
    String sSQL= "UPDATE bien_revertible SET BORRADO='1', FECHA_BAJA=? WHERE ID=? AND ID_MUNICIPIO=?";
    PreparedStatement ps= null;
    Connection conn= null;

    try {
        java.util.Date date= new java.util.Date();

        conn= CPoolDatabase.getConnection();
        conn.setAutoCommit(false);
        ps= conn.prepareStatement(sSQL);
        ps.setTimestamp(1, new Timestamp(date.getTime()));
        ps.setLong(2,bienRevertible.getId());
        ps.setString(3, municipio);
        ps.execute();
        conn.commit();
        bienRevertible.setBorrado(true);
        bienRevertible.setFechaBaja(date);

    }catch (Exception e){
        throw e;
    }finally{
        try{ps.close();}catch(Exception e){};
        try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){}
    }

    return bienRevertible;
}


private void deleteInmueble(InmuebleBean inmueble, Object[] idLayers, Object[] idFeatures, Sesion userSesion, Connection conn) throws Exception{
    String inmurbanoSQL= "INSERT INTO INMUEBLES_URBANOS(ID, MANZANA, REVISION_ACTUAL, ANCHOSUPERF, LONGSUPERF, METRPAVSUPERF, METRNOPAVSUPERF) VALUES(?,?,"+sRevisionActual.replaceAll("\\?", "inmuebles")+",?,?,?,?)";

    String inmrusticoSQL= "INSERT INTO INMUEBLES_RUSTICOS(ID, POLIGONO, APROVECHAMIENTO, PARAJE,REVISION_ACTUAL, ANCHOSUPERF, LONGSUPERF, METRPAVSUPERF, METRNOPAVSUPERF) VALUES(?,?,?,?,"+sRevisionActual.replaceAll("\\?", "inmuebles")+",?,?,?,?)";

    PreparedStatement ps= null;
    try {

		if (inmueble == null) return;

        insertarBienInventario(inmueble, userSesion, conn, sMaxRevision.replaceAll("\\?", "inmuebles"),sRevisionActual.replaceAll("\\?", "inmuebles"));

        insertInmueble(conn, inmueble,sRevisionActual.replaceAll("\\?", "inmuebles"),userSesion);

        inmueble.setIdFeatures(idFeatures);
        inmueble.setIdLayers(idLayers);

        if (inmueble.isUrbano()){
            ps= conn.prepareStatement(inmurbanoSQL);
            ps.setLong(1, inmueble.getId());
            ps.setString(2, inmueble.getInmuebleUrbano()!=null?inmueble.getInmuebleUrbano().getManzana():null);
	       	ps.setDouble(3,inmueble.getInmuebleUrbano()!=null?inmueble.getInmuebleUrbano().getSueloAncho():null);
   		 	ps.setDouble(4,inmueble.getInmuebleUrbano()!=null?inmueble.getInmuebleUrbano().getSueloLong():null);
   		 	ps.setDouble(5,inmueble.getInmuebleUrbano()!=null?inmueble.getInmuebleUrbano().getSueloMetrosPav():null);
   		 	ps.setDouble(6,inmueble.getInmuebleUrbano()!=null?inmueble.getInmuebleUrbano().getSueloMetrosNoPav():null);
            ps.execute();
            ps.close();
        }else if (inmueble.isRustico()){
            ps= conn.prepareStatement(inmrusticoSQL);
            ps.setLong(1, inmueble.getId());
            ps.setString(2, inmueble.getInmuebleRustico()!=null?inmueble.getInmuebleRustico().getPoligono():null);
            ps.setString(3, inmueble.getInmuebleRustico()!=null?inmueble.getInmuebleRustico().getAprovechamiento():null);
            ps.setString(4, inmueble.getInmuebleRustico()!=null?inmueble.getInmuebleRustico().getParaje():null);
			ps.setDouble(5,inmueble.getInmuebleRustico()!=null?inmueble.getInmuebleRustico().getSueloAncho():null);
   		 	ps.setDouble(6,inmueble.getInmuebleRustico()!=null?inmueble.getInmuebleRustico().getSueloLong():null);
   		 	ps.setDouble(7,inmueble.getInmuebleRustico()!=null?inmueble.getInmuebleRustico().getSueloMetrosPav():null);
   		 	ps.setDouble(8,inmueble.getInmuebleRustico()!=null?inmueble.getInmuebleRustico().getSueloMetrosNoPav():null);
            ps.execute();
            ps.close();
        }

        /** Actualizamos los ficheros en disco (temporal --> destino) */
        DocumentoEnDisco.actualizarConFicherosDeTemporal(((BienBean)inmueble).getDocumentos());
        

        String sSQL= "UPDATE inmuebles SET REVISION_EXPIRADA="+sRevisionActual.replaceAll("\\?", "inmuebles");
	    sSQL +=	" WHERE ID=? "+sRevisionExpiradaNula;
	
	   
	
	    ps= conn.prepareStatement(sSQL);
	    ps.setLong(1, inmueble.getId());
	    ps.execute();
	    
	    //Actualizamos en inmuebles urbanos y rusticos para que la revision actual y la expirada
	    //sean la misma.
	    if (inmueble.isUrbano()){
	    	sSQL= "UPDATE inmuebles_urbanos SET REVISION_EXPIRADA="+sRevisionActual.replaceAll("\\?", "inmuebles");
		    sSQL +=	" WHERE ID=? "+sRevisionExpiradaNula;
			
	    }
	    else if (inmueble.isRustico()){
	    	sSQL= "UPDATE inmuebles_rusticos SET REVISION_EXPIRADA="+sRevisionActual.replaceAll("\\?", "inmuebles");
		    sSQL +=	" WHERE ID=? "+sRevisionExpiradaNula;
			
	    }
	    ps= conn.prepareStatement(sSQL);
	    ps.setLong(1, inmueble.getId());
	    ps.execute();
	
	}catch (Exception e){
	    throw e;
	}finally{
	    try{ps.close();}catch(Exception e){};
	}
}

/**
 *  Para recuperar un bien pasamos el campo borrado=0 donde antes era un 2 (Eliminado)
 * @param inmueble
 * @param conn
 * @throws Exception
 */
private void updateBienesInventario(InmuebleBean inmueble, Connection conn) throws Exception{
    String sSQL= "update bienes_inventario set borrado='0' where id=? and revision_actual=revision_expirada";
    
    PreparedStatement ps= null;
    try{
        ps= conn.prepareStatement(sSQL);
        ps.setLong(1, inmueble.getId());
        boolean resultado=ps.execute();
        ps.close();
    }catch (Exception e){
        throw e;
    }finally{
        try{ps.close();}catch(Exception e){};
    }
	 }

private void updateBienesRevertibleInventario(BienRevertible bien, Connection conn) throws Exception{
    String sSQL= "update bien_revertible set borrado='0' where id=? and revision_actual=revision_expirada";
    
    PreparedStatement ps= null;
    try{
        ps= conn.prepareStatement(sSQL);
        ps.setLong(1, bien.getId());
        boolean resultado=ps.execute();
        ps.close();
    }catch (Exception e){
        throw e;
    }finally{
        try{ps.close();}catch(Exception e){};
    }
	 }


private void deleteInmueble(InmuebleBean inmueble, Connection conn) throws Exception{
    String sSQL= "UPDATE inmuebles SET REVISION_EXPIRADA="+sRevisionActual.replaceAll("\\?", "inmuebles");
    sSQL +=	" WHERE ID=? "+sRevisionExpiradaNula;

    //Esto no me queda claro porque se hacia asi.
    /*String sSQLTipo= "";
    if (inmueble.isRustico()) sSQLTipo= "DELETE FROM inmuebles_rusticos WHERE ID=?";
    else if (inmueble.isUrbano()) sSQLTipo= "DELETE FROM inmuebles_urbanos WHERE ID=?";
     */
    PreparedStatement ps= null;
    try{
        ps= conn.prepareStatement(sSQL);
        ps.setLong(1, inmueble.getId());
        boolean resultado=ps.execute();
        ps.close();

        /*ps= conn.prepareStatement(sSQL);
        ps.setLong(1, inmueble.getId());
        ps.execute();*/

    }catch (Exception e){
        throw e;
    }finally{
        try{ps.close();}catch(Exception e){};
    }
	 }


private void insertInventarioFeature(long id, Object[] idLayers, Object[] idFeatures,String revision, Sesion userSesion, Connection conn) throws Exception{

    String sSQL= "INSERT INTO INVENTARIO_FEATURE (ID_BIEN, ID_LAYER, ID_FEATURE,REVISION_ACTUAL) VALUES (?,?,?,"+revision+")";
    PreparedStatement ps= null;

    try{
    	
        for (int j=0; j<idFeatures.length; j++){
            /** Para vincular un bien a una feature, es necesario que esta
             * no este bloqueada por otro usuario y tener permisos. */
            checkPermissionLock((String)idLayers[j], (String)idFeatures[j], userSesion);

            ps= conn.prepareStatement(sSQL);
            ps.setLong(1, id);
            ps.setInt(2, getIdLayer((String)idLayers[j]));
            ps.setInt(3, Integer.parseInt((String)idFeatures[j]));
            ps.execute();
            ps.close();
        }
    }catch (Exception e){
    	logger.error("El numero de revision es : "+revision);
    	e.printStackTrace();
        throw e;
    }finally{
        try{ps.close();}catch(Exception e){};
    }

}

 private Collection getLayersInventario(long id, Connection conn) throws Exception{
     java.util.Vector alRet= new java.util.Vector();
     //---NUEVO--->
     //String sSQL= "SELECT ID_LAYER from INVENTARIO_FEATURE WHERE id_bien=?";     
     String sSQL= "SELECT ID_LAYER from INVENTARIO_FEATURE WHERE id_bien=? AND revision_expirada=9999999";
     PreparedStatement ps= null;
     ResultSet rs= null;
     try{
         ps= conn.prepareStatement(sSQL);
         ps.setLong(1, id);
         rs= ps.executeQuery();
         while (rs.next()){
             alRet.add(rs.getString("ID_LAYER"));
         }
     }catch(Exception ex){
     	logger.error("Error al ejecutar: "+sSQL);
    	throw ex;
    }finally{
         try{rs.close();}catch(Exception e){};
         try{ps.close();}catch(Exception e){};
     }
     return (Collection)alRet;

 }

     
 
 private Collection getLayersInventario(long id, Connection conn,String revisionActual) throws Exception{
     java.util.Vector alRet= new java.util.Vector();    
     String sSQL= "SELECT ID_LAYER from INVENTARIO_FEATURE WHERE id_bien=? AND revision_actual = "+revisionActual;
     PreparedStatement ps= null;
     ResultSet rs= null;
     try{
         ps= conn.prepareStatement(sSQL);
         ps.setLong(1, id);
         rs= ps.executeQuery();
         if( rs.next()){
        	 alRet.add(rs.getString("ID_LAYER"));
             while (rs.next()){
                 alRet.add(rs.getString("ID_LAYER"));
             }        	 
         }
         else{
        	 sSQL= "SELECT ID_LAYER from INVENTARIO_FEATURE WHERE id_bien=?";
        	 ps= conn.prepareStatement(sSQL);
             ps.setLong(1, id);
             rs= ps.executeQuery();
             if( rs.next()){
            	 alRet.add(rs.getString("ID_LAYER"));
                 while (rs.next()){
                     alRet.add(rs.getString("ID_LAYER"));
                 }        	 
             }
         }
     }finally{
         try{rs.close();}catch(Exception e){};
         try{ps.close();}catch(Exception e){};
     }
     return alRet;

 }


 private Collection getFeaturesInventario(long id, Connection conn,String revisionActual) throws Exception{
     java.util.Vector alRet= new java.util.Vector();
     String sSQL= "SELECT ID_FEATURE from INVENTARIO_FEATURE WHERE id_bien=? and REVISION_ACTUAL = "+revisionActual;
     PreparedStatement ps= null;
     ResultSet rs= null;
     try{
         ps= conn.prepareStatement(sSQL);
         ps.setLong(1, id);
         rs= ps.executeQuery();  
         if (rs.next()){
        	 alRet.add(rs.getString("ID_FEATURE"));
             while (rs.next()){
                 alRet.add(rs.getString("ID_FEATURE"));
             }
        	 
         }
         else{
        	 //Temporalmente si no tiene asociada ninguna feature
        	 //No deberia de pasar por aqui. Pero hay features que tienen como revision_actual=0
        	 //logger.error("No hay una feature asociada para la revision especificada. Probando revision=0");
             sSQL= "SELECT ID_FEATURE from INVENTARIO_FEATURE WHERE id_bien=? and revision_expirada=9999999999";
        	
        	 ps= conn.prepareStatement(sSQL);
        	 ps.setLong(1, id);
             rs= ps.executeQuery(); 
             if (rs.next()){
            	 alRet.add(rs.getString("ID_FEATURE"));
                 while (rs.next()){
                     alRet.add(rs.getString("ID_FEATURE"));
                 }  
 
             }
        	 
         }
     }catch(Exception ex){
     	logger.error("Error al ejecutar: "+sSQL);
     	throw ex;         
     }finally{
         try{rs.close();}catch(Exception e){};
         try{ps.close();}catch(Exception e){};
     }
     return alRet;

 }

private void deleteInventarioFeature(long id, Object[]idLayers, Object[]idFeatures, Sesion userSesion,
		Connection conn) throws Exception{

    String sSQL= "DELETE FROM INVENTARIO_FEATURE WHERE ID_BIEN=? AND ID_LAYER=? AND ID_FEATURE=?";
    PreparedStatement ps= null;
    try{
        for (int j=0; j<idFeatures.length; j++){

            /** Para desvincular un bien a una feature, es necesario que esta
             * no este bloqueada por otro usuario y tener permisos. */
            checkPermissionLock((String)idLayers[j], (String)idFeatures[j], userSesion);

            ps= conn.prepareStatement(sSQL);
            ps.setLong(1, id);
            ps.setInt(2, getIdLayer((String)idLayers[j]));
            ps.setString(3, (String)idFeatures[j]);
            ps.execute();
            ps.close();

        }

    }catch(Exception ex){
    	logger.error("Error al ejecutar: "+sSQL);
    	throw ex;
    }finally{
        try{ps.close();}catch(Exception e){};
    }

}

private BienBean eliminarInventario(BienBean bien, Object[] idLayers, Object[] idFeatures, 
		Sesion userSesion) throws Exception{
    String sSQL= "DELETE FROM bienes_inventario WHERE ID=? AND ID_MUNICIPIO=?";
    PreparedStatement ps= null;
    Connection conn= null;

    try {
        conn= CPoolDatabase.getConnection();
        conn.setAutoCommit(false);

        /** borramos los enlaces del bien con features */
        //--NUEVO-->
       // deleteInventarioFeature(bien.getId(), idLayers, idFeatures, userSesion, conn);

      //  DocumentConnection docConn= new DocumentConnection(srid);

        /** borramos el bien */
        if (!asociadoAOtraFeature(bien.getId(), idLayers, idFeatures, conn)){

            /** TODOS los tipos de bien tienen observaciones */
//            deleteAllObservaciones(bien.getId(), conn);

            /** Si un bien tiene seguro, lo borramos */
/*            if (bien.getSeguro() != null)
                deleteSeguro(bien.getSeguro().getId(), conn);*/

            /** Si un bien tiene mejoras, las borramos */
/*            if (bien.getMejoras() != null)
                deleteAllMejoras(bien.getId(), conn);*/

            /** borramos las cuentas contables y las de amortizacion */
/*            if (bien.getCuentaContable()!=null)
                if (!cuentaAsociadaOtroBien(bien, bien.getCuentaContable(), conn)) deleteCuenta(bien.getCuentaContable(), conn);

            if (bien.getCuentaAmortizacion()!=null)
                if (!cuentaAsociadaOtroBien(bien, bien.getCuentaAmortizacion(), conn)) deleteCuenta(bien.getCuentaAmortizacion(), conn);
*/
            if (bien instanceof InmuebleBean){
                /*deleteAllRefCatastrales(bien.getId(), conn);
                deleteAllUsosFuncionales(bien.getId(), conn);
                insertInmueble(conn, (InmuebleBean)bien, sMaxRevision.replaceAll("\\?", "inmuebles"), userSesion);
                deleteInmueble((InmuebleBean)bien, conn);*/
                
                deleteInmueble((InmuebleBean)bien,idLayers, idFeatures,userSesion, conn);
                deleteBienInventario(bien,sRevisionActual.replaceAll("\\?", "inmuebles"),conn);
   
            }else if (bien instanceof MuebleBean){
            	  insertarBienInventario(bien, userSesion, conn, sMaxRevision.replaceAll("\\?", "muebles"), sRevisionActual.replaceAll("\\?", "muebles"));
                  deleteBienInventario(bien,sRevisionActual.replaceAll("\\?", "muebles"),conn);
                  insertMueble(conn, (MuebleBean)bien, sRevisionActual.replaceAll("\\?", "muebles"), userSesion);
                  deleteMueble((MuebleBean)bien, conn);
              }else if (bien instanceof DerechoRealBean){
                  insertarBienInventario(bien, userSesion, conn, sMaxRevision.replaceAll("\\?", "derechos_reales"), sRevisionActual.replaceAll("\\?", "derechos_reales"));
                  deleteBienInventario(bien,sRevisionActual.replaceAll("\\?", "derechos_reales"),conn);
                  insertDerechoReal(conn, (DerechoRealBean)bien, sRevisionActual.replaceAll("\\?", "derechos_reales"), userSesion);
                  deleteDerecho((DerechoRealBean)bien, conn);
              }else if (bien instanceof ValorMobiliarioBean){
                  insertarBienInventario(bien, userSesion, conn, sMaxRevision.replaceAll("\\?", "valor_mobiliario"), sRevisionActual.replaceAll("\\?", "valor_mobiliario"));
                  deleteBienInventario(bien,sRevisionActual.replaceAll("\\?", "valor_mobiliario"),conn);
                  insertValorMobiliario(conn, (ValorMobiliarioBean)bien, sRevisionActual.replaceAll("\\?", "valor_mobiliario"), userSesion);
                  deleteValorMobiliario((ValorMobiliarioBean)bien, conn);
              }else if (bien instanceof CreditoDerechoBean){
                  insertarBienInventario(bien, userSesion, conn, sMaxRevision.replaceAll("\\?", "credito_derecho"), sRevisionActual.replaceAll("\\?", "credito_derecho"));
                  deleteBienInventario(bien,sRevisionActual.replaceAll("\\?", "credito_derecho"),conn);
                  insertCreditoDerecho(conn, (CreditoDerechoBean)bien, sRevisionActual.replaceAll("\\?", "credito_derecho"), userSesion);
                  deleteCreditoDerecho((CreditoDerechoBean)bien, conn);
              }else if (bien instanceof SemovienteBean){
                  insertarBienInventario(bien, userSesion, conn, sMaxRevision.replaceAll("\\?", "semoviente"), sRevisionActual.replaceAll("\\?", "semoviente"));
                  deleteBienInventario(bien,sRevisionActual.replaceAll("\\?", "semoviente"),conn);
                  insertSemoviente(conn, (SemovienteBean)bien, sRevisionActual.replaceAll("\\?", "semoviente"), userSesion);
                  deleteSemoviente((SemovienteBean)bien, conn);
              }else if (bien instanceof ViaBean){
                  insertarBienInventario(bien, userSesion, conn, sMaxRevision.replaceAll("\\?", "vias_inventario"), sRevisionActual.replaceAll("\\?", "vias_inventario"));
                  deleteBienInventario(bien,sRevisionActual.replaceAll("\\?", "vias_inventario"),conn);
                  insertVia(conn, (ViaBean)bien, sRevisionActual.replaceAll("\\?", "vias_inventario"), userSesion);
                  deleteVia((ViaBean)bien, conn);
              }else if (bien instanceof VehiculoBean){
                  insertarBienInventario(bien, userSesion, conn, sMaxRevision.replaceAll("\\?", "vehiculo"), sRevisionActual.replaceAll("\\?", "vehiculo"));
                  deleteBienInventario(bien,sRevisionActual.replaceAll("\\?", "vehiculo"),conn);
                  insertVehiculo(conn, (VehiculoBean)bien, sRevisionActual.replaceAll("\\?", "vehiculo"), userSesion);
                  deleteVehiculo((VehiculoBean)bien, conn);
              }


            /** borramos los documentos de BD */
//            docConn.detachInventarioDocuments(bien.getId(), bien.getDocumentos(), conn, true);

            //ps= conn.prepareStatement(sSQL);
            //ps.setLong(1, bien.getId());
            //ps.setString(2, municipio);
//            ps.execute();

            conn.commit();

            /** borramos de disco los ficheros del bien que no esten asociados a otro */
//            docConn.borrarFicherosEnDisco(bien.getId(), bien.getDocumentos());

        }
        else{
        	logger.error("No se puede borrar el bien porque está asociado a otra feature: "+bien.getId());
        }
        conn.commit();

    }catch(Exception ex){
    	logger.error("Error al ejecutar: "+sSQL);
    	throw ex;
    }finally{
        try{ps.close();}catch(Exception e){};
        try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){}
    }

    return bien;
}
	private BienRevertible eliminarBienRevertible(BienRevertible bienRevertible, Sesion userSesion) throws Exception{
          //0pcion --> no elimina los datos fisicamente		
	      String sSQL= "UPDATE bien_revertible BORRADO='2',SET REVISION_EXPIRADA="+sRevisionActual.replaceAll("\\?", "bien_revertible")+
          " WHERE ID=? "+sRevisionExpiradaNula;

          PreparedStatement ps= null;
          Connection conn= null;
 
          try{
        	  conn= CPoolDatabase.getConnection();
  	          conn.setAutoCommit(false);
  	          bienRevertible=insertBienRevertible(bienRevertible, userSesion, conn);
              ps= conn.prepareStatement(sSQL);
              ps.setLong(1, bienRevertible.getId());
              ps.execute();
              conn.commit();
              bienRevertible.setRevisionExpirada(bienRevertible.getRevisionActual());
          }catch (Exception e){
        	  try{conn.rollback();}catch(Exception ex){}
              throw e;
          }finally{
              try{ps.close();}catch(Exception e){};
              try{conn.close();}catch(Exception e){};
          }
          return bienRevertible;
        	  
		//Opcion B --> Para eliminar fisicamente el registro hacer lo siguiente:
		/*
	    String sSQL= "DELETE FROM bien_revertible WHERE ID=?";
	    PreparedStatement ps= null;
	    Connection conn= null;

	    try {
	    	if (bienRevertible.getId()==null) return bienRevertible; 
	        conn= CPoolDatabase.getConnection();
	        conn.setAutoCommit(false);
	        
	        //borramos las observaciones
	        deleteAllObservaciones(bienRevertible.getId(), TABLA_OBSERVACION_BIEN_REVERTIBLE,
	        		conn);
            //borramao la asociacion de bienes, pero no los bienes
	        deleteBienesFromBienesRevertible(bienRevertible, conn);
	        
	        //borramos el bien revertible
	        ps= conn.prepareStatement(sSQL);
	        ps.setLong(1, bienRevertible.getId());
	        ps.execute();
	        conn.commit();
	    }catch(Exception ex){
	    	logger.error("Error al ejecutar: "+sSQL);
	    	throw ex;
	    }finally{
	        try{ps.close();}catch(Exception e){};
	        try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){}
	    }

	    return bienRevertible;*/
	}
	/**
	 * Elimina los datos del lote
	 * @param bienRevertible
	 * @param userSesion
	 * @return
	 * @throws Exception
	 */
	private Lote eliminarLote(Lote lote, Sesion userSesion) throws Exception{
		String sSQLLoteBien= "DELETE FROM lote_bien where id_lote=?";
	    String sSQLLote= "DELETE FROM lote WHERE id_lote=?";
	    PreparedStatement ps= null;
	    Connection conn= null;

	    try {
	    	if (lote==null || lote.getId_lote()==null) return lote;
	        conn= CPoolDatabase.getConnection();
	        conn.setAutoCommit(false);
	        
	        //borramos el bien asociados al lote
	        ps= conn.prepareStatement(sSQLLoteBien);
	        ps.setLong(1, lote.getId_lote());
	        ps.execute();
	        
	      //borramos el lote
	        ps= conn.prepareStatement(sSQLLote);
	        ps.setLong(1, lote.getId_lote());
	        ps.execute();
	        
	        conn.commit();
	    }catch(Exception ex){
	    	logger.error("Error al ejecutar: "+sSQLLote + " o "+ sSQLLoteBien);
	    	throw ex;
	    }finally{
	        try{ps.close();}catch(Exception e){};
	        try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){}
	    }

	    return lote;
	}

    


	
	   // delete from bien_revertible_bien;
	    //delete from observaciones_bien_revertible;
	    //delete from lote_bien;
	    //delete from bien_revertible;
	 /**
     * Se elimina toda la información de los Bienes Revertibles para un municipio
	 * @param obj 
     */
	 private Boolean eliminarTodoInventarioBienRevertible(Connection conn,Object obj) throws Exception{
	
		BienRevertible bien=null;
		logger.info("Eliminando todos los bienes revertibles");
		if (obj!=null)
			bien=(BienRevertible)obj;

		String sSQL;
		String sSQLBienesWithDocuments;
		String sSQLDeleteBienRevertible;
		//La primera query e principio daria un bien y la segunda daria todos los bienes
		//del municipio
		if (bien!=null)
			sSQL= "Select id from bien_revertible WHERE ID="+bien.getId()+" AND ID_MUNICIPIO='"+municipio+"' group by id";
		else
			sSQL= "Select id from bien_revertible WHERE ID_MUNICIPIO='"+municipio+"' group by id";
		
		
		if (bien!=null)
			sSQLBienesWithDocuments= "Select id FROM bien_revertible, anexo_bien_revertible WHERE bien_revertible.ID="+bien.getId()+" AND ID_MUNICIPIO='"+municipio+"' and bien_revertible.id = anexo_bien_revertible.id_bien_revertible group by id";
		else
			sSQLBienesWithDocuments= "Select id FROM bien_revertible, anexo_bien_revertible WHERE ID_MUNICIPIO='"+municipio+"' and bien_revertible.id = anexo_bien_revertible.id_bien_revertible group by id";

		String sSQLDeleteBienRevertibleBien ="delete from bien_revertible_bien where id_bien_revertible in ("+sSQL+")";
	    String sSQLDeleteObservaciones= "delete from observaciones_bien_revertible where id_bien in ("+sSQL+")";

		if (bien!=null)
			sSQLDeleteBienRevertible ="delete from bien_revertible where id="+bien.getId()+" and id_municipio='"+municipio+"'";
		else;
			sSQLDeleteBienRevertible ="delete from bien_revertible where id_municipio='"+municipio+"'";

		PreparedStatement ps= null;
	    ResultSet rs= null;
	    //Connection conn= null;
	    boolean closeConnection=false;
	    
	    try {
	    	boolean resultado;
	    	
	    	if (conn==null){
	    		conn= CPoolDatabase.getConnection();
	    		closeConnection=true;
	    	}
	        conn.setAutoCommit(false);
	        DocumentConnection docConn= new DocumentConnection(srid);
	        ps= conn.prepareStatement(sSQLBienesWithDocuments);
	        rs = ps.executeQuery();
	        while (rs.next()){
	        	BienRevertible bien1= new BienRevertible();
	        	bien1.setId(rs.getLong("id"));
	            docConn.detachInventarioDocuments(bien1, docConn.getAttachedInventarioDocuments(bien1), conn, false);
	  	    }
	        ps.close();
	    
	        
	        ps=conn.prepareStatement(sSQLDeleteBienRevertibleBien);
	        resultado=ps.execute();
	        ps.close();
	        ps=conn.prepareStatement(sSQLDeleteObservaciones);
	        resultado=ps.execute();
	        ps.close();
	        ps=conn.prepareStatement(sSQLDeleteBienRevertible);
	        resultado=ps.execute();
	        ps.close();
	        if (closeConnection)
	        	conn.commit();
	        logger.info("Bienes revertibles eliminados");
	    }catch(Exception ex){
	    	logger.error("Error al ejecutar: "+sSQL);
	    	throw ex;
	    }finally{
	        try{rs.close();}catch(Exception e){};
	        try{ps.close();}catch(Exception e){};
	        if (closeConnection)
	        	try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){}
	    }

	    return new Boolean(true);
	}


     /**
      * Se elimina toda la información de los lotes para un municipio
     * @param obj 
      */
	 private Boolean eliminarTodoInventarioLotes(Connection conn,Object obj) throws Exception{

		Lote bien=null;
		logger.info("Eliminando todos los lotes");
		if (obj!=null)
			bien=(Lote)obj;

		String sSQL;
		String sSQLLotesWithDocumentos;
		String sSQLDeleteLote;
		//La primera query e principio daria un bien y la segunda daria todos los bienes
		//del municipio
		if (bien!=null)		 
			sSQL= "Select id_lote FROM lote WHERE ID_LOTE="+bien.getId_lote()+" AND ID_MUNICIPIO='"+municipio+"' group by id_lote";
		else
			sSQL= "Select id_lote FROM lote WHERE ID_MUNICIPIO='"+municipio+"' group by id_lote";

		if (bien!=null)		 
			sSQLLotesWithDocumentos= "Select lote.id_lote FROM lote, anexo_lote WHERE lote.id_lote="+bien.getId_lote()+" AND lote.ID_MUNICIPIO='"+municipio+"' and lote.id_lote=anexo_lote.id_lote group by (lote.id_lote)";
		else
			sSQLLotesWithDocumentos= "Select lote.id_lote FROM lote, anexo_lote WHERE lote.ID_MUNICIPIO='"+municipio+"' and lote.id_lote=anexo_lote.id_lote group by (lote.id_lote)";

		String sSQLDeleteLoteBien ="delete from lote_bien where id_lote in ("+sSQL+")";
		
		if (bien!=null)		 
			sSQLDeleteLote ="delete from lote where id_lote="+bien.getId_lote()+" AND id_municipio='"+municipio+"'";
		else
			sSQLDeleteLote ="delete from lote where id_municipio='"+municipio+"'";

		PreparedStatement ps= null;
	    ResultSet rs= null;
	    //Connection conn= null;
	    boolean closeConnection=false;
	    try {
	    	boolean resultado;
	    	if (conn==null){
	    		conn= CPoolDatabase.getConnection();
	    		closeConnection=true;
	    	}
	        conn.setAutoCommit(false);
	        /****Eliminamos los documentos***/
	        DocumentConnection docConn= new DocumentConnection(srid);
	        ps= conn.prepareStatement(sSQLLotesWithDocumentos);
	        rs = ps.executeQuery();
	        while (rs.next()){
	        	Lote lote= new Lote();
	        	lote.setId_lote(rs.getLong("id_lote"));
	            docConn.detachInventarioDocuments(lote, docConn.getAttachedInventarioDocuments(lote), conn, false);
	  	    } 
	        ps.close();
	        ps=conn.prepareStatement(sSQLDeleteLoteBien);
	        resultado=ps.execute();
	        ps.close();
	        ps=conn.prepareStatement(sSQLDeleteLote);
		    resultado=ps.execute();
		    ps.close();
		    if (closeConnection)
		    	conn.commit();
	        logger.info("lotes eliminados");
	    }catch(Exception ex){
	    	logger.error("Error al ejecutar: "+sSQL);
	    	throw ex;
	    }finally{
	        try{rs.close();}catch(Exception e){};
	        try{ps.close();}catch(Exception e){};
	        if (closeConnection)
	        	try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){}
	    }

	    return new Boolean(true);
	}

    /***
     * Elimina toda la información de los bienes
     * @param obj 
     * @return
     * @throws Exception
     */
	private Boolean eliminarTodoInventarioBienes(Connection conn,Object obj) throws Exception{
		
		BienBean bien=null;
		logger.info("Eliminando todos los bienes");
		if (obj!=null)
			bien=(BienBean)obj;

		String sSQL;
		String sSQLBienesWithDocuments;
		String sSQLDelete;
		String sSQLVersiones;
		
		
		//La primera query e principio daria un bien y la segunda daria todos los bienes
		//del municipio
		if (bien!=null)
			sSQL= "Select id FROM bienes_inventario WHERE bienes_inventario.ID="+bien.getId()+" AND ID_MUNICIPIO='"+municipio+"' group by id";
		else
			sSQL= "Select id FROM bienes_inventario WHERE ID_MUNICIPIO='"+municipio+"' group by id";
	    
		if (bien!=null)
			sSQLBienesWithDocuments= "Select id FROM bienes_inventario, anexo_inventario WHERE bienes_inventario.ID="+bien.getId()+" AND ID_MUNICIPIO='"+municipio+"' and bienes_inventario.id = anexo_inventario.id_bien group by id";
		else
			sSQLBienesWithDocuments= "Select id FROM bienes_inventario, anexo_inventario WHERE ID_MUNICIPIO='"+municipio+"' and bienes_inventario.id = anexo_inventario.id_bien group by id";
	    
		String sSQLDeleteCreditoDerecho="delete from credito_derecho where id in ("+sSQL+")";
	    String sSQLDeleteVehiculo="delete from vehiculo where id in ("+sSQL+")";
	    String sSQLDeleteInmuebles="delete from inmuebles where id in ("+sSQL+")";
	    String sSQLDeleteInmueblesUrbanos="delete from inmuebles_urbanos where id in ("+sSQL+")";
	    String sSQLDeleteInmueblesRusticos="delete from inmuebles_rusticos where id in ("+sSQL+")";
	    String sSQLDeleteMuebles="delete from muebles where id in ("+sSQL+")";
	    String sSQLDeleteValorMobiliario="delete from valor_mobiliario where id in ("+sSQL+")";
	    String sSQLDeleteVias="delete from vias_inventario where id in ("+sSQL+")";
	    String sSQLDeleteSemoviente="delete from semoviente where id in ("+sSQL+")";
	    String sSQLDeleteObservaciones="delete from observaciones_inventario where id_bien in ("+sSQL+")";
	    String sSQLDeleteMejoras="delete from mejoras_inventario where id_bien in ("+sSQL+")";
	    String sSQLDeleteUsosFuncionales="delete from usos_funcionales_inventario where id_bien in ("+sSQL+")";
	    String sSQLDeleteRefCatastrales="delete from refcatastrales_inventario where id_bien in ("+sSQL+")";
	    String sSQLDeleteFeatures="delete from inventario_feature where id_bien in ("+sSQL+")";
	    
	    //Borrados especificos en lugar de por identificador de bien por identificador de feature
	    String sSQLDeleteFeaturesAmpliado1="delete from inventario_feature where id_feature in (select id from parcelas WHERE ID_MUNICIPIO='"+municipio+"' group by id)";
	    String sSQLDeleteFeaturesAmpliado2="delete from inventario_feature where id_feature in (select id from vias WHERE ID_MUNICIPIO='"+municipio+"' group by id)";

		if (bien!=null)
			sSQLDelete ="delete from bienes_inventario where id="+bien.getId()+" and id_Municipio ='"+municipio+"'";
		else
			sSQLDelete ="delete from bienes_inventario where id_Municipio ='"+municipio+"'";
	    
	    PreparedStatement ps= null;
	    ResultSet rs= null;
	    //Connection conn= null;
	    boolean closeConnection=false;
	    
	    boolean resultado;
	    try {
	    	if (conn==null){
	    		conn= CPoolDatabase.getConnection();
	    		closeConnection=true;
	    	}
	        conn.setAutoCommit(false);
	        /****Eliminamos los documentos***/
	        DocumentConnection docConn= new DocumentConnection(srid);
	        ps= conn.prepareStatement(sSQLBienesWithDocuments);
	        rs = ps.executeQuery();
	        while (rs.next()){
	        	BienBean bien1= new BienBean();
	        	bien1.setId(rs.getLong("id"));
	            docConn.detachInventarioDocuments(bien1, docConn.getAttachedInventarioDocuments(bien1), conn, false);
	  	    }
	        ps.close();
	        ps= conn.prepareStatement(sSQLDeleteCreditoDerecho);
	        resultado=ps.execute();
	        ps.close();
	        logger.info("borrado tabla Credito_Derecho:"+resultado);

	        ps= conn.prepareStatement(sSQLDeleteVehiculo);
		    resultado=ps.execute();
		    ps.close();
		    logger.info("borrado tabla Vehiculo:"+resultado);
		    
	        ps= conn.prepareStatement(sSQLDeleteInmuebles);
		    resultado=ps.execute();
		    ps.close();		    
		    logger.info("borrado tabla Inmueble:"+resultado);
		 
	        ps= conn.prepareStatement(sSQLDeleteInmueblesUrbanos);
		    resultado=ps.execute();
		    ps.close();		    
		    logger.info("borrado tabla Inmueble Urbano:"+resultado);
		    
	        ps= conn.prepareStatement(sSQLDeleteInmueblesRusticos);
		    resultado=ps.execute();
		    ps.close();		    
		    logger.info("borrado tabla Inmueble Rustico:"+resultado);
		    
	        ps= conn.prepareStatement(sSQLDeleteMuebles);
		    resultado=ps.execute();
		    ps.close();
		    logger.info("borrado tabla Muebles:"+resultado);

	        ps= conn.prepareStatement(sSQLDeleteValorMobiliario);
		    resultado=ps.execute();
		    ps.close();
		    logger.info("borrado tabla Valor_mobiliario:"+resultado);

	        ps= conn.prepareStatement(sSQLDeleteVias);
		    resultado=ps.execute();
		    ps.close();
		    logger.info("borrado tabla vias_inventario:"+resultado);

	        ps= conn.prepareStatement(sSQLDeleteSemoviente);
		    resultado=ps.execute();
		    ps.close();
		    logger.info("borrado tabla anexo_inventario:"+resultado);

		    ps= conn.prepareStatement(sSQLDeleteObservaciones);
		    resultado=ps.execute();
		    ps.close();
		    logger.info("borrado tabla semoviente:"+resultado);

	        ps= conn.prepareStatement(sSQLDeleteMejoras);
		    resultado=ps.execute();
		    ps.close();
		    logger.info("borrado tabla mejoras_inventario:"+resultado);

	        ps= conn.prepareStatement(sSQLDeleteUsosFuncionales);
		    resultado=ps.execute();
		    ps.close();
		    logger.info("borrado tabla usos_funcionales:"+resultado);

	        ps= conn.prepareStatement(sSQLDeleteRefCatastrales);
		    resultado=ps.execute();
		    ps.close();
		    logger.info("borrando tabla ref catastrales:"+resultado);

	        ps= conn.prepareStatement(sSQLDeleteFeatures);
		    resultado=ps.execute();
		    ps.close();
		    logger.info("borrado tabla inventario_feature:"+resultado);

		    //Esto solo lo realizamos si el bien es nulo
		    if (bien==null){
		        ps= conn.prepareStatement(sSQLDeleteFeaturesAmpliado1);
			    resultado=ps.execute();
			    ps.close();
			    logger.info("borrado tabla inventario_feature Ampliado1:"+resultado);
		     
			    ps= conn.prepareStatement(sSQLDeleteFeaturesAmpliado2);
			    resultado=ps.execute();
			    ps.close();
			    logger.info("borrado tabla inventario_feature Ampliado2:"+resultado);
		    }
		    
	        ps= conn.prepareStatement(sSQLDelete);
		    resultado=ps.execute();
		    ps.close();   
		    logger.info("borrando tabla bien_inventario:"+resultado);

		    if (closeConnection)
		    	conn.commit();
		    if (bien!=null)
		    	logger.info("Bienes eliminados para el bien:"+bien.getNumInventario());
		    else
		    	logger.info("Bienes eliminados");
	    }catch(Exception ex){
	    	logger.error("Error al ejecutar la sentencia SQL de borrado: ");
	    	throw ex;
	    }finally{
	        try{rs.close();}catch(Exception e){};
	        try{ps.close();}catch(Exception e){};
	        if (closeConnection)
	        	try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){}
	    }

	    return new Boolean(true);
	}

    private Collection getMuebles(String superpatron, String patron, String cadena, Object[] filtro, Object[] idLayers, 
    		Object[] idFeatures,Object[] numInventarios, Sesion userSesion, boolean withAllData,
    		ConfigParameters configParameters) throws Exception{
        HashMap alRet= new HashMap();
        Vector vNumInventarios=new Vector();
        String sSQL= "";

        if (cadena==null || cadena.trim().equalsIgnoreCase("")){
        	 sSQL= getSelectMueble(superpatron, patron, filtro, idLayers, idFeatures, userSesion, "");
        	 sSQL+= getSelectNumInventario(patron,numInventarios,TABLE_INVENTARIO_MUEBLES);
        	 sSQL+= " ORDER BY idBien ASC, muebles.revision_actual DESC";
        }
        else {
        	sSQL= addSelectsBusquedaMueble(superpatron, patron, cadena, idLayers, idFeatures, userSesion);
        	sSQL+= getSelectNumInventario(patron,numInventarios,TABLE_INVENTARIO_MUEBLES);
        	sSQL+= " ORDER BY idBien ASC,revision_actual DESC";
        }
        
        //Parametros de LIMIT y OFFSET.
        if (numInventarios==null){
	        if (configParameters.getLimit()!=-1)
	        	sSQL+=" LIMIT "+configParameters.getLimit();
	        if (configParameters.getOffset()!=-1)
	        	sSQL+=" OFFSET "+configParameters.getOffset();
        }

        logger.info("Sentencia getMuebles:"+sSQL);
        
        
        Connection conn=null;
        PreparedStatement ps= null;
        ResultSet rs= null;
        try{
            conn= CPoolDatabase.getConnection();
            ps= conn.prepareStatement(sSQL);
            rs= ps.executeQuery();
            MuebleBean mueble= null;
            long anteriorId = -1;
            long revisionAnterior = -1;
            boolean soloMostrarVersionado=false;	            
            while (rs.next()){
                mueble= new MuebleBean();
                mueble.setId(rs.getLong("IDBIEN"));
                mueble.setOrganizacion(rs.getString("ORGANIZACION"));
                mueble.setNumInventario(rs.getString("NUMINVENTARIO"));
                mueble.setBorrado(rs.getString("BORRADO"));
                mueble.setFechaAlta(rs.getTimestamp("FECHA_ALTA"));
                mueble.setFechaAprobacionPleno(rs.getTimestamp("FECHA_APROBACION_PLENO"));
                mueble.setFechaUltimaModificacion(rs.getTimestamp("FECHA_ULTIMA_MODIFICACION"));
                mueble.setNombre(rs.getString("NOMBRE"));
                mueble.setUso(rs.getString("USO"));
                mueble.setTipo(rs.getString("TIPO"));
                mueble.setDescripcion(rs.getString("DESCRIPCION"));
                mueble.setRevisionActual(rs.getLong("REVISION_ACTUAL"));
                mueble.setAdquisicion(rs.getString("ADQUISICION"));
                mueble.setFechaAdquisicion(rs.getTimestamp("FECHA_ADQUISICION"));
                mueble.setDestino(rs.getString("DESTINO"));
                mueble.setPropiedad(rs.getString("PROPIEDAD"));
                mueble.setMarca(rs.getString("MARCA"));
                mueble.setModelo(rs.getString("MODELO"));
                mueble.setEstadoConservacion(rs.getString("ESTADOCONSERVACION"));
                mueble.setCaracteristicas(rs.getString("CARACTERISTICAS"));
                mueble.setNumSerie(rs.getString("NUM_SERIE"));
                mueble.setCosteAdquisicion(rs.getDouble("COSTE_ADQUISICION"));
                mueble.setValorActual(rs.getDouble("VALOR_ACTUAL"));
                mueble.setFrutos(rs.getString("FRUTOS"));
                mueble.setImporteFrutos(rs.getDouble("IMPORTE_FRUTOS"));
                mueble.setFechaFinGarantia(rs.getTimestamp("FECHA_FIN_GARANTIA"));
                mueble.setDireccion(rs.getString("DIRECCION"));
                mueble.setTipo(patron);
                mueble.setRevisionExpirada(rs.getLong("REVISION_EXPIRADA"));
                mueble.setPatrimonioMunicipalSuelo(rs.getString("patrimonio_municipal_suelo"));
                mueble.setAutor(rs.getString("NAME"));
                mueble.setArtista(rs.getString("AUTOR"));
                mueble.setFechaVersion(rs.getTimestamp("FECHA"));
                mueble.setMaterial(rs.getString("MATERIAL"));
                mueble.setUbicacion(rs.getString("UBICACION"));                 
                mueble.setClase(rs.getString("clase"));	
               
                
                if (new Long(mueble.getId()) == anteriorId){
                	if(revisionAnterior != mueble.getRevisionActual() && !soloMostrarVersionado){
                		mueble.setVersionado(true);
	                    alRet.put(mueble.getId()+"_"+mueble.getRevisionActual(), mueble);
	                    revisionAnterior = mueble.getRevisionActual();
                	}
                }else{
                	if(idFeatures.length>0){
	                	//filtra las versiones si se selecciona una version anterior.
	                	if(rs.getString("feature_revision_actual")!=null && mueble.getRevisionActual()<=rs.getLong("feature_revision_actual") ){
	                		
	                		mueble.setVersionado(false);
	                		alRet.put(mueble.getId()+"_"+mueble.getRevisionActual(), mueble);
	                		anteriorId = new Long(mueble.getId());
	                		revisionAnterior = mueble.getRevisionActual();  
	                		if(rs.getLong("feature_revision_actual")==rs.getLong("revision"))
	                			soloMostrarVersionado=true;
	                	}                    
		            }else{
		            		mueble.setVersionado(false);
	                		alRet.put(mueble.getId()+"_"+mueble.getRevisionActual(), mueble);
	                		anteriorId = new Long(mueble.getId());
	                		revisionAnterior = mueble.getRevisionActual();  
		            }
                }
			    mueble.setIdFeatures(getFeaturesInventario(mueble.getId(), conn, rs.getString("REVISION_ACTUAL")));
                mueble.setIdLayers(getLayersInventario(mueble.getId(), conn, rs.getString("REVISION_ACTUAL")));
				if (withAllData)
				{
	               	mueble.setObservaciones(getObservaciones(mueble.getId(),TABLA_OBSERVACION_INVENTARIO, conn,rs.getLong("revision_actual")));
                    mueble.setSeguro(getSeguro(rs.getObject("ID_SEGURO")));
                	mueble.setCuentaContable(getCuentaContable(rs.getInt("ID_CUENTA_CONTABLE")));
                	
                	String[] filtroBien ={" and bi.id='"+mueble.getId()+"' and  bien_revertible.revision_expirada = 9999999999"};
    				mueble.setBienesRevertibles(getBienesRevertibles(null, null, filtroBien, null,userSesion, false,null));
 					Lote lote = getLoteByIdBien(mueble.getId(),conn);
 					if (lote!=null)
 						lote.setNumeroBienes(getNumeroBienesByLote(lote,conn));
 					mueble.setLote(lote);
				}
				
				if (numInventarios==null)
                	vNumInventarios.add(mueble.getNumInventario());
                else
                	mueble.setVersionado(true);
            }
        }catch(Exception ex){
        	logger.error("Error al ejecutar: "+sSQL);
        	throw ex;
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
        }

        //Añadimos los versionados al array Total y cambiamos la informacion de versionado
        if ((vNumInventarios.size()>0) && (!configParameters.isBusquedaIndividual())){
        	Collection elementosVersionados=getMuebles(superpatron, patron, cadena,filtro, idLayers, idFeatures,vNumInventarios.toArray(),
        							userSesion,withAllData,configParameters);
        	Iterator it=elementosVersionados.iterator();
        	while (it.hasNext()){
        		MuebleBean muebleBean=(MuebleBean)it.next();
        		//String clave=vehiculo.getId()+"_"+vehiculo.getRevisionActual();
        		alRet.put(muebleBean,muebleBean);	
        	}        	
        }

        return alRet.values();
    }

    private Collection getDerechosReales(String superpatron, String patron, String cadena, Object[] filtro, 
    		Object[] idLayers, Object[] idFeatures, Object[] numInventarios,
    		Sesion userSesion, boolean withAllData,ConfigParameters configParameters) throws Exception {

        HashMap alRet= new HashMap();
        Vector vNumInventarios=new Vector();
        String sSQL= "";

        if (cadena==null || cadena.trim().equalsIgnoreCase("")){
            sSQL= getSelectDerechosReales(superpatron, patron, filtro, idLayers, idFeatures, userSesion, "");
            sSQL+= getSelectNumInventario(patron,numInventarios,TABLE_INVENTARIO_DERECHOS);
            sSQL+= " ORDER BY id ASC, derechos_reales.revision_actual DESC";
        }else {
	       sSQL= addSelectsBusquedaDerechosReales(superpatron, patron, cadena, idLayers, idFeatures, userSesion);
	       sSQL+= getSelectNumInventario(patron,numInventarios,TABLE_INVENTARIO_DERECHOS);
           sSQL+= " ORDER BY id ASC,revision_actual DESC";
        }
        
        //Parametros de LIMIT y OFFSET.
        if (numInventarios==null){
	        if (configParameters.getLimit()!=-1)
	        	sSQL+=" LIMIT "+configParameters.getLimit();
	        if (configParameters.getOffset()!=-1)
	        	sSQL+=" OFFSET "+configParameters.getOffset();
        }
        
        Connection conn=null;
        PreparedStatement ps= null;
        ResultSet rs= null;

        try{
            conn= CPoolDatabase.getConnection();
            ps= conn.prepareStatement(sSQL);
            rs= ps.executeQuery();
            DerechoRealBean derechoReal= null;
            long anteriorId = -1;
            long revisionAnterior = -1;
            boolean soloMostrarVersionado=false;	            
            while (rs.next()){
                derechoReal= new DerechoRealBean();
                derechoReal.setId(rs.getLong("ID"));
                derechoReal.setNumInventario(rs.getString("NUMINVENTARIO"));
                derechoReal.setOrganizacion(rs.getString("ORGANIZACION"));
                derechoReal.setBorrado(rs.getString("BORRADO"));
                derechoReal.setFechaAlta(rs.getTimestamp("FECHA_ALTA"));
                derechoReal.setFechaAprobacionPleno(rs.getTimestamp("FECHA_APROBACION_PLENO"));
                derechoReal.setFechaUltimaModificacion(rs.getTimestamp("FECHA_ULTIMA_MODIFICACION"));
                derechoReal.setNombre(rs.getString("NOMBRE"));
                derechoReal.setUso(rs.getString("USO"));
                derechoReal.setTipo(rs.getString("TIPO"));
                derechoReal.setDescripcion(rs.getString("DESCRIPCION"));
                derechoReal.setDestino(rs.getString("DESTINO"));
                derechoReal.setFechaAdquisicion(rs.getTimestamp("FECHA_ADQUISICION"));
                derechoReal.setFormaAdquisicion(rs.getString("ADQUISICION"));
                derechoReal.setBien(rs.getString("BIEN"));
                derechoReal.setCosteAdquisicion(rs.getDouble("COSTE"));
                derechoReal.setValorActual(rs.getDouble("VALOR"));
                derechoReal.setFrutos(rs.getString("FRUTOS"));
                derechoReal.setImporteFrutos(rs.getDouble("IMPORTE_FRUTOS"));
                derechoReal.setRevisionActual(rs.getLong("REVISION_ACTUAL"));
                derechoReal.setRevisionExpirada(rs.getLong("REVISION_EXPIRADA"));
                derechoReal.setAutor(rs.getString("NAME"));
                derechoReal.setFechaVersion(rs.getTimestamp("FECHA"));
                derechoReal.setRegistroNotario(rs.getString("REGISTRO_NOTARIO"));
                derechoReal.setRegistroProtocolo(rs.getString("REGISTRO_PROTOCOLO"));
                derechoReal.setRegistroPropiedad(rs.getString("REGISTRO_PROPIEDAD"));
                derechoReal.setRegistroTomo(rs.getString("REGISTRO_TOMO"));
                derechoReal.setRegistroLibro(rs.getString("REGISTRO_LIBRO"));
                derechoReal.setRegistroFolio(rs.getString("REGISTRO_FOLIO"));
                derechoReal.setRegistroFinca(rs.getString("REGISTRO_FINCA"));
                derechoReal.setRegistroInscripcion(rs.getString("REGISTRO_INSCRIPCION"));
               derechoReal.setPatrimonioMunicipalSuelo(rs.getString("PATRIMONIO_MUNICIPAL_SUELO"));
               derechoReal.setClase(rs.getString("clase"));
				if (new Long(derechoReal.getId()) == anteriorId){
					if(revisionAnterior != derechoReal.getRevisionActual() && !soloMostrarVersionado){
                		derechoReal.setVersionado(true);
	                    alRet.put(derechoReal.getId()+"_"+derechoReal.getRevisionActual(), derechoReal);
	                    revisionAnterior = derechoReal.getRevisionActual();
                	}
                }else{
                	if(idFeatures.length>0){
	                	//filtra las versiones si se selecciona una version anterior.
	                	if(rs.getString("feature_revision_actual")!=null && derechoReal.getRevisionActual()<=rs.getLong("feature_revision_actual") ){
	                		
	                		derechoReal.setVersionado(false);
	                		alRet.put(derechoReal.getId()+"_"+derechoReal.getRevisionActual(), derechoReal);
	                		anteriorId = new Long(derechoReal.getId());
	                		revisionAnterior = derechoReal.getRevisionActual();  
	                		if(rs.getLong("feature_revision_actual")==rs.getLong("revision"))
	                			soloMostrarVersionado=true;
	                	}                    
		            }else{
		            	derechoReal.setVersionado(false);
	                		alRet.put(derechoReal.getId()+"_"+derechoReal.getRevisionActual(), derechoReal);
	                		anteriorId = new Long(derechoReal.getId());
	                		revisionAnterior = derechoReal.getRevisionActual();  
		            }
                	derechoReal.setVersionado(false);
                    alRet.put(derechoReal.getId()+"_"+derechoReal.getRevisionActual(), derechoReal);
                    anteriorId = new Long(derechoReal.getId());
                    revisionAnterior = derechoReal.getRevisionActual();
                }
				derechoReal.setIdFeatures((getFeaturesInventario(derechoReal.getId(), conn, rs.getString("REVISION_ACTUAL"))));
                derechoReal.setIdLayers((getLayersInventario(derechoReal.getId(), conn, rs.getString("REVISION_ACTUAL"))));

				if (withAllData){
					String[] filtroBien ={" and bi.id='"+derechoReal.getId()+"' and  bien_revertible.revision_expirada = 9999999999"};
					derechoReal.setBienesRevertibles(getBienesRevertibles(null, null, filtroBien, null,userSesion, false,null));
	                derechoReal.setObservaciones(getObservaciones(derechoReal.getId(),TABLA_OBSERVACION_INVENTARIO,conn,rs.getLong("REVISION_ACTUAL")));
					derechoReal.setCuentaContable(getCuentaContable(rs.getInt("ID_CUENTA_CONTABLE")));
				}
				if (numInventarios==null)
                	vNumInventarios.add(derechoReal.getNumInventario());
                else
                	derechoReal.setVersionado(true);

            }
        }catch(Exception ex){
        	logger.error("Error al ejecutar: "+sSQL);
        	throw ex;
        } finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
        }
        
        //Añadimos los versionados al array Total y cambiamos la informacion de versionado
        if ((vNumInventarios.size()>0) && (!configParameters.isBusquedaIndividual())){
        	Collection elementosVersionados=getDerechosReales(superpatron, patron, cadena,filtro, idLayers, idFeatures,vNumInventarios.toArray(),
        							userSesion,withAllData,configParameters);
        	Iterator it=elementosVersionados.iterator();
        	while (it.hasNext()){
        		DerechoRealBean derechoRealBean=(DerechoRealBean )it.next();
        		//String clave=vehiculo.getId()+"_"+vehiculo.getRevisionActual();
        		alRet.put(derechoRealBean,derechoRealBean);	
        	}        	
        }
        return alRet.values();
    }


    private void insertMueble(Connection conn, MuebleBean mueble, String revision, Sesion userSesion) throws PermissionException,LockException,Exception{

        String sSQL= "INSERT INTO MUEBLES(ID, ADQUISICION, FECHA_ADQUISICION, CARACTERISTICAS, " +
        "DESTINO, DIRECCION, UBICACION, MATERIAL, AUTOR, PROPIEDAD, PATRIMONIO_MUNICIPAL_SUELO, " +
        "ESTADOCONSERVACION,  COSTE_ADQUISICION, VALOR_ACTUAL, " +
        "FRUTOS, IMPORTE_FRUTOS, " +
        "MARCA, MODELO, NUM_SERIE, FECHA_FIN_GARANTIA,REVISION_ACTUAL, clase) " +
        "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"+revision+",?)";

        PreparedStatement ps= null;
        try{
	        if (mueble == null) return;
	
	        ps= conn.prepareStatement(sSQL);
	        ps.setLong(1, mueble.getId());
	        ps.setString(2, mueble.getAdquisicion());
	        if (mueble.getFechaAdquisicion() == null)
	            ps.setNull(3, java.sql.Types.TIMESTAMP);
	        else ps.setTimestamp(3, new Timestamp(mueble.getFechaAdquisicion().getTime()));
	        ps.setString(4, mueble.getCaracteristicas());
	        ps.setString(5, mueble.getDestino());
	        ps.setString(6, mueble.getDireccion());
	        ps.setString(7, mueble.getUbicacion());
	        ps.setString(8, mueble.getMaterial());
	        ps.setString(9, mueble.getArtista());
	        ps.setString(10, mueble.getPropiedad());
	        ps.setString(11, mueble.getPatrimonioMunicipalSuelo()?"1":"0");
	        ps.setString(12, mueble.getEstadoConservacion());
	        if (mueble.getCosteAdquisicion()==null)
	        	ps.setNull(13,java.sql.Types.DOUBLE );
	        else
	        	ps.setDouble(13, mueble.getCosteAdquisicion());
	   
	        if (mueble.getValorActual()==null)
	        	ps.setNull(14,java.sql.Types.DOUBLE );
	        else
	        	ps.setDouble(14, mueble.getValorActual());
	        
	        ps.setString(15, mueble.getFrutos());
	        if (mueble.getImporteFrutos()==null)
	        	ps.setNull(16,java.sql.Types.DOUBLE );
	        else
	        	ps.setDouble(16, mueble.getImporteFrutos());
	        ps.setString(17, mueble.getMarca());
	        ps.setString(18, mueble.getModelo());
	        ps.setString(19, mueble.getNumSerie());
	        if (mueble.getFechaFinGarantia() == null)
	            ps.setNull(20, java.sql.Types.TIMESTAMP);
	        else ps.setTimestamp(20, new Timestamp(mueble.getFechaFinGarantia().getTime()));
	        ps.setString(21, mueble.getClase());
	        ps.execute();
	        ps.close();
	
	        insertVersion(conn, "muebles", userSesion);
        }catch(Exception e){
        	throw e;
        }finally{
            try{ps.close();}catch(Exception e){};
        }
    }
    
    private Collection<MuebleBean> insertMueble(Object[] idLayers, Object[] idFeatures, MuebleBean mueble, Sesion userSesion) throws PermissionException,LockException,Exception{

        Connection conn=null;
        PreparedStatement ps= null;
        ResultSet rs= null;
        Vector listaMuebles= new Vector();
		try {

			if (mueble == null) return null;
            conn= CPoolDatabase.getConnection();
            conn.setAutoCommit(false);
            
            /*Miramos si pertenece a un lote  */
            
            if (mueble.getLote()!=null && mueble.getLote().getNumeroBienes()>1)
            {
            	mueble.setLote(insertarLote(mueble.getLote(),conn, userSesion));
            	for (int i=0;i<mueble.getLote().getNumeroBienes();i++){
            		mueble.setNumInventario(null);
            		mueble.setId(-1);
            		mueble= insertaUnMueble(idLayers, idFeatures, mueble, userSesion, conn);
            		MuebleBean newMueble=mueble.clone();
            		newMueble.setDocumentos(null);
            		listaMuebles.add(newMueble);
            	}
            	conn.commit();
                /** Actualizamos los ficheros en disco  (temporal --> destino) */
                DocumentoEnDisco.actualizarConFicherosDeTemporal(mueble.getLote().getDocumentos());
                mueble.getLote().setDocumentos(null);
            }else{
                mueble= insertaUnMueble(idLayers, idFeatures, mueble, userSesion, conn);
                mueble.setDocumentos(null);
                listaMuebles.add(mueble);
                conn.commit();
                
            }
            
             
         }catch (PermissionException e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }catch (LockException e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }catch (Exception e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }/** java.lang.OutOfMemoryError */catch (java.lang.Error e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){}
        }

        return listaMuebles;
    }
    /**
     * Inserta un bien de tipo mueble
     * @param mueble
     * @param userSesion
     * @param con
     * @return
     * @throws Exception
     */
    
    private MuebleBean insertaUnMueble(Object[] idLayers, Object[] idFeatures, MuebleBean mueble, Sesion userSesion, Connection conn) throws Exception{
    	mueble.setIdFeatures(idFeatures);
    	mueble.setIdLayers(idLayers);
    	
    	insertarBienInventario(mueble, userSesion, conn,sMaxRevision.replaceAll("\\?", "muebles"),sRevisionActual.replaceAll("\\?", "muebles"));
    	insertMueble(conn, mueble,sRevisionActual.replaceAll("\\?", "muebles"), userSesion);
    	if (mueble.getLote()!=null)
    		insertaBienLote(mueble, conn);

    	/** Insertamos las features asociadas al mueble */
//    	insertInventarioFeature(mueble.getId(), idLayers, idFeatures,sRevisionActual.replaceAll("\\?", "muebles"), userSesion, conn);


    	/** Actualizamos los ficheros en disco  (temporal --> destino) */
    	DocumentoEnDisco.actualizarConFicherosDeTemporal(((BienBean)mueble).getDocumentos());
    
        return mueble;
    }
    private void insertDerechoReal(Connection conn, DerechoRealBean derechoReal, String revision, Sesion userSesion) throws PermissionException,LockException,Exception{

        String sSQL= "INSERT INTO DERECHOS_REALES(ID, DESTINO," +
                "FECHA_ADQUISICION, ADQUISICION, BIEN, COSTE, VALOR, FRUTOS," +
                "IMPORTE_FRUTOS, " +
        "PATRIMONIO_MUNICIPAL_SUELO, REGISTRO_NOTARIO, REGISTRO_PROTOCOLO," +
                "REGISTRO_PROPIEDAD, REGISTRO_TOMO,REGISTRO_LIBRO, REGISTRO_FOLIO," +
                "REGISTRO_FINCA, REGISTRO_INSCRIPCION, REVISION_ACTUAL, clase ) " +
        "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"+revision+",?)";

        PreparedStatement ps= null;

        try{
	        if (derechoReal == null) return;
	
	        ps= conn.prepareStatement(sSQL);
	        ps.setLong(1, derechoReal.getId());
	        ps.setString(2,derechoReal.getDestino());
	        if (derechoReal.getFechaAdquisicion() == null)
	            ps.setNull(3, java.sql.Types.TIMESTAMP);
	        else ps.setTimestamp(3, new Timestamp(derechoReal.getFechaAdquisicion().getTime()));
	        ps.setString(4, derechoReal.getFormaAdquisicion());
	        ps.setString(5, derechoReal.getBien());
	        
	        if (derechoReal.getCosteAdquisicion()==null)
	        	ps.setNull(6,java.sql.Types.DOUBLE );
	        else
	        	ps.setDouble(6, derechoReal.getCosteAdquisicion());
	   
	        if (derechoReal.getValorActual()==null)
	        	ps.setNull(7,java.sql.Types.DOUBLE );
	        else
	        	ps.setDouble(7, derechoReal.getValorActual());
	   
	        ps.setString(8,derechoReal.getFrutos());
	       
	        if (derechoReal.getImporteFrutos()==null)
	        	ps.setNull(9,java.sql.Types.DOUBLE );
	        else
	        	ps.setDouble(9, derechoReal.getImporteFrutos());
	   
	        ps.setString(10, derechoReal.isPatrimonioMunicipal()?"1":"0");
	        ps.setString(11, derechoReal.getRegistroNotario());
	        ps.setString(12, derechoReal.getRegistroProtocolo());
	        ps.setString(13, derechoReal.getRegistroPropiedad());
	        ps.setString(14, derechoReal.getRegistroTomo());
	        ps.setString(15, derechoReal.getRegistroLibro());
	        ps.setString(16, derechoReal.getRegistroFolio());
	        ps.setString(17, derechoReal.getRegistroFinca());
	        ps.setString(18, derechoReal.getRegistroInscripcion());
	        ps.setString(19, derechoReal.getClase());
	
	        ps.execute();
	        ps.close();
	
	        insertVersion(conn, "derechos_reales", userSesion);
        }catch(Exception e){
        	throw e;
        }finally{
        	try{ps.close();}catch(Exception e){};
        }
    }

    private DerechoRealBean insertDerechoReal(Object[] idLayers, Object[] idFeatures, DerechoRealBean derechoReal,
    		Sesion userSesion) throws PermissionException,LockException,Exception{

            Connection conn=null;
            PreparedStatement ps= null;
            ResultSet rs= null;
            try {

                if (derechoReal == null) return null;
                conn= CPoolDatabase.getConnection();
                conn.setAutoCommit(false);

                derechoReal.setIdFeatures(idFeatures);
                derechoReal.setIdLayers(idLayers);
                
                insertarBienInventario(derechoReal, userSesion, conn, sMaxRevision.replaceAll("\\?", "derechos_reales"),sRevisionActual.replaceAll("\\?", "derechos_reales"));
                
                insertDerechoReal(conn, derechoReal,sRevisionActual.replaceAll("\\?", "derechos_reales"),userSesion);


                /** Insertamos las features asociadas al mueble */
//               insertInventarioFeature(derechoReal.getId(), idLayers, idFeatures, sRevisionActual.replaceAll("\\?", "derechos_reales"),userSesion, conn);


                /** Actualizamos los ficheros en disco  (temporal --> destino) */
                DocumentoEnDisco.actualizarConFicherosDeTemporal(((BienBean)derechoReal).getDocumentos());
                derechoReal.setDocumentos(null);  
                conn.commit();

            }catch (PermissionException e){
                try{conn.rollback();}catch(Exception ex){};
                throw e;
            }catch (LockException e){
                try{conn.rollback();}catch(Exception ex){};
                throw e;
            }catch (Exception e){
                try{conn.rollback();}catch(Exception ex){};
                throw e;
            }/** java.lang.OutOfMemoryError */catch (java.lang.Error e){
                try{conn.rollback();}catch(Exception ex){};
                throw e;
            }finally{
                try{rs.close();}catch(Exception e){};
                try{ps.close();}catch(Exception e){};
                try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){}
            }

            return derechoReal;
        }

    private MuebleBean updateMueble(MuebleBean mueble, Sesion userSesion) throws PermissionException,LockException,Exception{

        String sSQL= "UPDATE MUEBLES SET REVISION_EXPIRADA="+sRevisionActual.replaceAll("\\?", "muebles")+
        " WHERE ID=? "+sRevisionExpiradaNula;


        Connection conn=null;
        PreparedStatement ps= null;
        ResultSet rs= null;
		try {

			if (mueble == null) return null;

            conn= CPoolDatabase.getConnection();
            conn.setAutoCommit(false);

            updateBienInventario(mueble, userSesion, conn, sMaxRevision.replaceAll("\\?", "muebles"), sRevisionActual.replaceAll("\\?", "muebles"));

            ps= conn.prepareStatement(sSQL);
            ps.setLong(1, mueble.getId());
            ps.execute();
            ps.close();

            this.insertMueble(conn, mueble, sRevisionActual.replaceAll("\\?", "muebles"),userSesion);

            /** Actualizamos los ficheros en disco  (temporal --> destino) */
            DocumentoEnDisco.actualizarConFicherosDeTemporal(((BienBean)mueble).getDocumentos());
            mueble.setDocumentos(null);      
            conn.commit();

            /** Borramos de disco los ficheros del bien que no esten asociados a otro */
            DocumentConnection docConn= new DocumentConnection(srid);
            docConn.borrarFicherosEnDisco(mueble.getId(), docsBorrados);

        }catch (Exception e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }/** java.lang.OutOfMemoryError */catch (java.lang.Error e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){}
        }

        return mueble;
    }

    private void deleteMueble(MuebleBean mueble, Connection conn) throws Exception{
        String sSQL= "UPDATE muebles SET REVISION_EXPIRADA="+sRevisionActual.replaceAll("\\?", "muebles");
        sSQL +=	" WHERE ID=? "+sRevisionExpiradaNula;

        PreparedStatement ps = null;
        try{
            ps= conn.prepareStatement(sSQL);
            ps.setLong(1, mueble.getId());
            ps.execute();

        }catch (Exception e){
            throw e;
        }finally{
            try{ps.close();}catch(Exception e){};
        }

    }

      private void deleteDerecho(DerechoRealBean derecho, Connection conn) throws Exception{
        String sSQL= "UPDATE derechos_reales SET REVISION_EXPIRADA="+sRevisionActual.replaceAll("\\?", "derechos_reales");
        sSQL +=	" WHERE ID=? "+sRevisionExpiradaNula;

        PreparedStatement ps= null;
        try{
            ps= conn.prepareStatement(sSQL);
            ps.setLong(1, derecho.getId());
            ps.execute();

        }catch (Exception e){
            throw e;
        }finally{
            try{ps.close();}catch(Exception e){};
        }

    }
      private void deleteBienInventario(BienBean bien, String revision, Connection conn) throws Exception{
          String sSQL= "UPDATE bienes_inventario SET BORRADO='2',REVISION_EXPIRADA="+revision;
          sSQL +=	" WHERE ID=? "+sRevisionExpiradaNula;

          String sSQL2= "UPDATE inventario_feature SET REVISION_EXPIRADA="+revision;
          sSQL2 +=	" WHERE ID_BIEN=? "+sRevisionExpiradaNula;

          
          PreparedStatement ps= null;
          try{
              ps= conn.prepareStatement(sSQL);
              ps.setLong(1, bien.getId());
              ps.execute();
              
              ps= conn.prepareStatement(sSQL2);
              ps.setLong(1, bien.getId());
              ps.execute();
              
              

          }catch (Exception e){
              throw e;
          }finally{
              try{ps.close();}catch(Exception e){};
          }

      }
    public boolean asociadoAOtraFeature(long id, Object[]idLayers, Object[]idFeatures, Connection conn) throws Exception{
    	if (idFeatures==null || idLayers==null) return false;
        String sSQL= "SELECT * FROM INVENTARIO_FEATURE WHERE ID_BIEN="+id;
        for (int i=0; i<idFeatures.length; i++){
            sSQL+= " AND NOT (ID_LAYER="+getIdLayer((String)idLayers[i])+" AND ID_FEATURE="+(String)idFeatures[i]+")";
        }
        sSQL+= " AND REVISION_EXPIRADA=9999999999";

        boolean b= false;
        PreparedStatement ps= null;
        ResultSet rs= null;
        try{
            ps= conn.prepareStatement(sSQL);
            rs= ps.executeQuery();
            if (rs.next()) b= true;
        }catch(Exception ex){
        	logger.error("Error al ejecutar: "+sSQL);
        	throw ex;
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
        }
        return b;
    }

    /**
     * Bloquea/Desbloquea un bien de inventario
     * @param oos buffer donde escribe el resultado de la operacion
     * @param obj bien a bloquear/desbloquear
     * @param b true para bloquear, false para desbloquear
     * @throws Exception
     */
    public void returnBloquearInventario(ObjectOutputStream oos, Object obj, boolean b, Sesion userSesion) throws Exception{
        try{
            BienBean bien= bloquearInventario((BienBean)obj, b, userSesion);
            oos.writeObject(bien);
        }catch(Exception e){
            logger.error("returnBloquearInventario: "+ e.getMessage());
            oos.writeObject(new ACException(e));
            throw e;
        }
    }

    private BienBean bloquearInventario(BienBean bien, boolean b, Sesion userSesion) throws Exception{

        if (bien==null) return null;
        String sSQL= "UPDATE BIENES_INVENTARIO SET BLOQUEADO=? WHERE ID=?";

        PreparedStatement ps= null;
        Connection conn= null;
		try {
            conn= CPoolDatabase.getConnection();
            conn.setAutoCommit(false);
            ps= conn.prepareStatement(sSQL);
            if (b) ps.setString(1, userSesion.getUserPrincipal().getName());
            else ps.setNull(1, java.sql.Types.VARCHAR);
            ps.setLong(2, bien.getId());
            ps.execute();
            conn.commit();
            /** actualizamos el bien */
            bien.setBloqueado(b?userSesion.getUserPrincipal().getName():null);
        }catch (Exception e){
            throw e;
        }finally{
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){}
        }
        return bien;

    }

    /**
     * Retorna el usuario que bloquea el bien de inventario
     * @param oos buffer donde se escribe el resultado
     * @param obj
     * @throws Exception
     */
    public void returnBloqueado(ObjectOutputStream oos, Object obj, Sesion userSesion) throws Exception{
        try{
            String bloquea= bloqueado(((BienBean)obj).getId());
            oos.writeObject(bloquea);
        }catch(Exception e){
            logger.error("returnBloqueado: "+ e.getMessage());
            oos.writeObject(new ACException(e));
            throw e;
        }
    }

    /**
     * Usuario que bloquea el bien de inventario
     * @param id del bien de inventario
     * @return
     * @throws Exception
     */
    private String bloqueado(long id) throws Exception{

        String sSQL= "SELECT BLOQUEADO FROM BIENES_INVENTARIO WHERE ID="+id;

        PreparedStatement ps= null;
        ResultSet rs= null;
        Connection conn= null;
		try {
            conn= CPoolDatabase.getConnection();
            ps= conn.prepareStatement(sSQL);
            rs= ps.executeQuery();
            if (rs.next()) return rs.getString("BLOQUEADO");
            else return null;
        }catch(Exception ex){
        	logger.error("Error al ejecutar: "+sSQL);
        	throw ex;
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){}
        }
    }

    private Collection getValoresMobiliarios(String superpatron, String patron, 
    		String cadena, Object[] filtro, Object[] idLayers, Object[] idFeatures, 
    		Object[] numInventarios,Sesion userSesion, boolean withAllData,
    		ConfigParameters configParameters) throws Exception{
        HashMap alRet= new HashMap();
        Vector vNumInventarios=new Vector();
        String sSQL= "";
        if (cadena==null || cadena.trim().equalsIgnoreCase("")){
            sSQL= getSelectValorMobiliario(superpatron, patron, filtro, idLayers, idFeatures, userSesion, "");
            sSQL+= getSelectNumInventario(patron,numInventarios,TABLE_INVENTARIO_VALORES);
            sSQL+= " ORDER BY id ASC, valor_mobiliario.revision_actual DESC";
        }
        else {
        	sSQL= addSelectsBusquedaValorMobiliario(superpatron, patron, cadena, idLayers, idFeatures, userSesion);
        	sSQL+= getSelectNumInventario(patron,numInventarios,TABLE_INVENTARIO_VALORES);
        	 sSQL+= " ORDER BY id ASC,revision_actual DESC";
        }

      //Parametros de LIMIT y OFFSET.
        if (numInventarios==null){
	        if (configParameters.getLimit()!=-1)
	        	sSQL+=" LIMIT "+configParameters.getLimit();
	        if (configParameters.getOffset()!=-1)
	        	sSQL+=" OFFSET "+configParameters.getOffset();
        }
     
        Connection conn=null;
        PreparedStatement ps= null;
        ResultSet rs= null;
        try{
            conn= CPoolDatabase.getConnection();
            ps= conn.prepareStatement(sSQL);
            rs= ps.executeQuery();
            ValorMobiliarioBean valor= null;
            long anteriorId = -1;
            long revisionAnterior = -1;
            boolean soloMostrarVersionado=false;	            
            while (rs.next()){
                valor= new ValorMobiliarioBean();
                valor.setId(rs.getLong("ID"));
                valor.setNumInventario(rs.getString("NUMINVENTARIO"));
                valor.setBorrado(rs.getString("BORRADO"));
                valor.setFechaAlta(rs.getTimestamp("FECHA_ALTA"));
                valor.setFechaAprobacionPleno(rs.getTimestamp("FECHA_APROBACION_PLENO"));
                valor.setFechaUltimaModificacion(rs.getTimestamp("FECHA_ULTIMA_MODIFICACION"));
                valor.setNombre(rs.getString("NOMBRE"));
                valor.setUso(rs.getString("USO"));
                valor.setTipo(rs.getString("TIPO"));
                valor.setOrganizacion(rs.getString("ORGANIZACION"));
                valor.setDescripcion(rs.getString("DESCRIPCION"));
                valor.setRevisionActual(rs.getLong("REVISION_ACTUAL"));
                valor.setAdquisicion(rs.getString("ADQUISICION"));
                valor.setFechaAdquisicion(rs.getTimestamp("FECHA_ADQUISICION"));
                valor.setDestino(rs.getString("DESTINO"));
                valor.setClase(rs.getString("CLASE"));
                valor.setEmitidoPor(rs.getString("EMITIDO_POR"));
                valor.setDepositadoEn(rs.getString("DEPOSITADO_EN"));
                valor.setNumero(rs.getString("NUMERO"));
                valor.setSerie(rs.getString("SERIE"));
                valor.setCosteAdquisicion(rs.getDouble("COSTE_ADQUISICION"));
                valor.setValorActual(rs.getDouble("VALOR_ACTUAL"));
                valor.setFrutos(rs.getString("FRUTOS"));
                valor.setImporteFrutos(rs.getDouble("IMPORTE_FRUTOS"));
                valor.setPrecio(rs.getDouble("PRECIO"));
                valor.setCapital(rs.getDouble("CAPITAL"));
                valor.setNumTitulos(rs.getInt("NUM_TITULOS"));
                valor.setFechaAcuerdo(rs.getTimestamp("FECHA_ACUERDO"));
                valor.setRevisionExpirada(rs.getLong("REVISION_EXPIRADA"));
                valor.setAutor(rs.getString("NAME"));
                valor.setFechaVersion(rs.getTimestamp("FECHA"));
                valor.setSeguro(getSeguro(rs.getObject("ID_SEGURO")));
                valor.setPatrimonioMunicipalSuelo(rs.getString("PATRIMONIO_MUNICIPAL_SUELO"));

				
                if (new Long(valor.getId()) == anteriorId){
                	if(revisionAnterior != valor.getRevisionActual() && !soloMostrarVersionado){
	                	valor.setVersionado(true);
	                    alRet.put(valor.getId()+"_"+valor.getRevisionActual(), valor);
	                    revisionAnterior = valor.getRevisionActual();
                	}
                }else{
                	if(idFeatures.length>0){
	                	//filtra las versiones si se selecciona una version anterior.
	                	if(rs.getString("feature_revision_actual")!=null && valor.getRevisionActual()<=rs.getLong("feature_revision_actual") ){
	                		
	                		valor.setVersionado(false);
	                		alRet.put(valor.getId()+"_"+valor.getRevisionActual(), valor);
	                		anteriorId = new Long(valor.getId());
	                		revisionAnterior =valor.getRevisionActual();  
	                		if(rs.getLong("feature_revision_actual")==rs.getLong("revision"))
	                			soloMostrarVersionado=true;
	                	}                    
		            }else{
		            		valor.setVersionado(false);
	                		alRet.put(valor.getId()+"_"+valor.getRevisionActual(),valor);
	                		anteriorId = new Long(valor.getId());
	                		revisionAnterior = valor.getRevisionActual();  
		            }
                }
                valor.setIdFeatures(getFeaturesInventario(valor.getId(), conn, rs.getString("REVISION_ACTUAL")));
                valor.setIdLayers(getLayersInventario(valor.getId(), conn, rs.getString("REVISION_ACTUAL")));
                if (withAllData){
                	String[] filtroBien ={" and bi.id='"+valor.getId()+"' and  bien_revertible.revision_expirada = 9999999999"};
                	valor.setBienesRevertibles(getBienesRevertibles(null, null, filtroBien, null,userSesion, false,null));
                    valor.setObservaciones(getObservaciones(valor.getId(),TABLA_OBSERVACION_INVENTARIO,conn,rs.getLong("REVISION_ACTUAL")));
                    valor.setCuentaContable(getCuentaContable(rs.getInt("ID_CUENTA_CONTABLE")));
                }
                
                if (numInventarios==null)
                	vNumInventarios.add(valor.getNumInventario());
                else
                	valor.setVersionado(true);
            }
        }catch(Exception ex){
        	logger.error("Error al ejecutar: "+sSQL);
        	throw ex;
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
        }
        
        //Añadimos los versionados al array Total y cambiamos la informacion de versionado
        if ((vNumInventarios.size()>0) && (!configParameters.isBusquedaIndividual())){
        	Collection elementosVersionados=getValoresMobiliarios(superpatron, patron, cadena,filtro, idLayers, idFeatures,vNumInventarios.toArray(),
        							userSesion,withAllData,configParameters);
        	Iterator it=elementosVersionados.iterator();
        	while (it.hasNext()){
        		ValorMobiliarioBean valorMobiliarioBean=(ValorMobiliarioBean)it.next();
        		//String clave=vehiculo.getId()+"_"+vehiculo.getRevisionActual();
        		alRet.put(valorMobiliarioBean,valorMobiliarioBean);	
        	}        	
        }

        return alRet.values();
    }

    private void insertValorMobiliario(Connection conn, ValorMobiliarioBean valor, String revision, Sesion userSesion) throws PermissionException,LockException,Exception{

        String sSQL= "INSERT INTO VALOR_MOBILIARIO (ID, ADQUISICION, FECHA_ADQUISICION, " +
        "DESTINO, DEPOSITADO_EN, EMITIDO_POR, CLASE, PATRIMONIO_MUNICIPAL_SUELO, " +
        "COSTE_ADQUISICION, VALOR_ACTUAL, PRECIO, CAPITAL, " +
        "FRUTOS, IMPORTE_FRUTOS, " +
        "SERIE, NUM_TITULOS, FECHA_ACUERDO, NUMERO,REVISION_ACTUAL) " +
        "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"+revision+")";

        PreparedStatement ps= null;
        try{
			if (valor == null) return;
	
	        ps= conn.prepareStatement(sSQL);
	        ps.setLong(1, valor.getId());
	        ps.setString(2, valor.getAdquisicion());
	        if (valor.getFechaAdquisicion() == null)
	            ps.setNull(3, java.sql.Types.TIMESTAMP);
	        else ps.setTimestamp(3, new Timestamp(valor.getFechaAdquisicion().getTime()));
	        ps.setString(4, valor.getDestino());
	        ps.setString(5, valor.getDepositadoEn());
	        ps.setString(6, valor.getEmitidoPor());
	        ps.setString(7, valor.getClase());
	        ps.setString(8, valor.getPatrimonioMunicipalSuelo()?"1":"0");
	        if (valor.getCosteAdquisicion()==null)
	        	ps.setNull(9,java.sql.Types.DOUBLE );
	        else
	        	ps.setDouble(9, valor.getCosteAdquisicion());
	       
	        if (valor.getValorActual()==null)
	        	ps.setNull(10,java.sql.Types.DOUBLE );
	        else
	        	ps.setDouble(10, valor.getValorActual());
	        
	        if (valor.getPrecio()==null)
	        	ps.setNull(11,java.sql.Types.DOUBLE );
	        else
	             ps.setDouble(11, valor.getPrecio());
	        
	        if (valor.getCapital()==null)
	        	ps.setNull(12,java.sql.Types.DOUBLE );
	        else
	             ps.setDouble(12, valor.getCapital());
	        
	        ps.setString(13, valor.getFrutos());
	        
	        if (valor.getImporteFrutos()==null)
	        	ps.setNull(14,java.sql.Types.DOUBLE );
	        else
	        	ps.setDouble(14, valor.getImporteFrutos());
	        ps.setString(15, valor.getSerie());
	        
	        if (valor.getNumTitulos()==null)
	        	ps.setNull(16,java.sql.Types.INTEGER );
	        else
	            ps.setInt(16, valor.getNumTitulos());
	        
	        if (valor.getFechaAcuerdo() == null)
	            ps.setNull(17, java.sql.Types.TIMESTAMP);
	        else ps.setTimestamp(17, new Timestamp(valor.getFechaAcuerdo().getTime()));
	        ps.setString(18, valor.getNumero());
	        ps.execute();
	        ps.close();
	        
	        insertVersion(conn, "valor_mobiliario", userSesion);
        }
        catch(Exception e){
        	throw e;
        }finally{
        	try{ps.close();}catch(Exception e){};
        }
    }
    
    private ValorMobiliarioBean insertValorMobiliario(Object[] idLayers, Object[] idFeatures, ValorMobiliarioBean valor, Sesion userSesion) throws PermissionException,LockException,Exception{

        Connection conn=null;
        PreparedStatement ps= null;
        ResultSet rs= null;
		try {

			if (valor == null) return null;
            conn= CPoolDatabase.getConnection();
            conn.setAutoCommit(false);
            
            valor.setIdFeatures(idFeatures);
            valor.setIdLayers(idLayers);
            
            insertarBienInventario(valor, userSesion, conn, sMaxRevision.replaceAll("\\?", "valor_mobiliario"),sRevisionActual.replaceAll("\\?", "valor_mobiliario"));
            
            insertValorMobiliario(conn, valor, sRevisionActual.replaceAll("\\?", "valor_mobiliario"),userSesion);

            /** Insertamos las features asociadas al valor mobiliario */
//            insertInventarioFeature(valor.getId(), idLayers, idFeatures,sRevisionActual.replaceAll("\\?", "valor_mobiliario"), userSesion, conn);


            /** Actualizamos los ficheros en disco  (temporal --> destino) */
            DocumentoEnDisco.actualizarConFicherosDeTemporal(((BienBean)valor).getDocumentos());
            valor.setDocumentos(null);
            conn.commit();

        }catch (PermissionException e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }catch (LockException e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }catch (Exception e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }/** java.lang.OutOfMemoryError */catch (java.lang.Error e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){}
        }

        return valor;
    }

    private ValorMobiliarioBean updateValorMobiliario(ValorMobiliarioBean valor, Sesion userSesion) throws PermissionException,LockException,Exception{

        String sSQL= "UPDATE VALOR_MOBILIARIO SET REVISION_EXPIRADA="+sRevisionActual.replaceAll("\\?", "valor_mobiliario")+
        " WHERE ID=? "+sRevisionExpiradaNula;

        Connection conn=null;
        PreparedStatement ps= null;
        ResultSet rs= null;
		try {

			if (valor == null) return null;

            conn= CPoolDatabase.getConnection();
            conn.setAutoCommit(false);

            updateBienInventario(valor, userSesion, conn, sMaxRevision.replaceAll("\\?", "valor_mobiliario"), sRevisionActual.replaceAll("\\?", "valor_mobiliario"));

            ps= conn.prepareStatement(sSQL);
            ps.setLong(1, valor.getId());
            ps.execute();
            ps.close();

            this.insertValorMobiliario(conn, valor, sRevisionActual.replaceAll("\\?", "valor_mobiliario"), userSesion);

            /** Actualizamos los ficheros en disco  (temporal --> destino) */
            DocumentoEnDisco.actualizarConFicherosDeTemporal(((BienBean)valor).getDocumentos());
            valor.setDocumentos(null);
            conn.commit();

            /** Borramos de disco los ficheros del bien que no esten asociados a otro */
            DocumentConnection docConn= new DocumentConnection(srid);
            docConn.borrarFicherosEnDisco(valor.getId(), docsBorrados);

        }catch (Exception e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }/** java.lang.OutOfMemoryError */catch (java.lang.Error e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){}
        }

        return valor;
    }

    private void deleteValorMobiliario(ValorMobiliarioBean valor, Connection conn) throws Exception{
        String sSQL= "UPDATE valor_mobiliario SET REVISION_EXPIRADA="+sRevisionActual.replaceAll("\\?", "valor_mobiliario");
        sSQL +=	" WHERE ID=? "+sRevisionExpiradaNula;

        PreparedStatement ps= null;
        try{
            ps= conn.prepareStatement(sSQL);
            ps.setLong(1, valor.getId());
            ps.execute();

        }catch (Exception e){
            throw e;
        }finally{
            try{ps.close();}catch(Exception e){};
        }

    }


    private Collection getCreditosDerechos(String superpatron, String patron, 
    		String cadena, Object[] filtro, Object[] idLayers, Object[] idFeatures, 
    		Object[] numInventarios,Sesion userSesion, boolean withAllData,
    		ConfigParameters configParameters) throws Exception{
        HashMap alRet= new HashMap();
        Vector vNumInventarios=new Vector();
        String sSQL= "";
        if (cadena==null || cadena.trim().equalsIgnoreCase("")){
            sSQL= getSelectCreditosDerechos(superpatron, patron, filtro, idLayers, idFeatures, userSesion, "");
            sSQL+= getSelectNumInventario(patron,numInventarios,TABLE_INVENTARIO_CREDITOS);
            sSQL+= " ORDER BY id ASC,  credito_derecho.revision_actual DESC";
        }
        else {
        	sSQL= addSelectsBusquedaCreditosDerechos(superpatron, patron, cadena, idLayers, idFeatures, userSesion);
        	sSQL+= getSelectNumInventario(patron,numInventarios,TABLE_INVENTARIO_CREDITOS);
        	sSQL+= " ORDER BY id ASC,revision_actual DESC";
        }
        
        //Parametros de LIMIT y OFFSET.
        if (numInventarios==null){
	        if (configParameters.getLimit()!=-1)
	        	sSQL+=" LIMIT "+configParameters.getLimit();
	        if (configParameters.getOffset()!=-1)
	        	sSQL+=" OFFSET "+configParameters.getOffset();
        }
        
        Connection conn=null;
        PreparedStatement ps= null;
        ResultSet rs= null;
        try{
            conn= CPoolDatabase.getConnection();
            ps= conn.prepareStatement(sSQL);
            rs= ps.executeQuery();
            CreditoDerechoBean credito= null;
            long anteriorId = -1;
            long revisionAnterior = -1;
            boolean soloMostrarVersionado=false;	            
            while (rs.next()){
                credito= new CreditoDerechoBean();
                credito.setId(rs.getLong("ID"));
                credito.setNumInventario(rs.getString("NUMINVENTARIO"));
                credito.setBorrado(rs.getString("BORRADO"));
                credito.setFechaAlta(rs.getTimestamp("FECHA_ALTA"));
                credito.setFechaAprobacionPleno(rs.getTimestamp("FECHA_APROBACION_PLENO"));
                credito.setFechaUltimaModificacion(rs.getTimestamp("FECHA_ULTIMA_MODIFICACION"));
                credito.setNombre(rs.getString("NOMBRE"));
                credito.setUso(rs.getString("USO"));
                credito.setTipo(rs.getString("TIPO"));
                credito.setOrganizacion(rs.getString("ORGANIZACION"));
                credito.setDescripcion(rs.getString("BIEN_DESCRIPCION"));
                credito.setAdquisicion(rs.getString("ADQUISICION"));
                credito.setFechaAdquisicion(rs.getTimestamp("FECHA_ADQUISICION"));
                credito.setConcepto(rs.getString("CONCEPTO"));
                credito.setConceptoDesc(rs.getString("CONCEPTO_DESC"));
                credito.setFechaVencimiento(rs.getTimestamp("FECHA_VENCIMIENTO"));
                credito.setDestino(rs.getString("DESTINO"));
                credito.setDeudor(rs.getString("DEUDOR"));
                credito.setCaracteristicas(rs.getString("DESCRIPCION"));
                credito.setImporte(rs.getDouble("IMPORTE"));
                credito.setRevisionExpirada(rs.getLong("REVISION_EXPIRADA"));
                credito.setAutor(rs.getString("NAME"));
                credito.setFechaVersion(rs.getTimestamp("FECHA"));
                credito.setRevisionActual(rs.getLong("REVISION_ACTUAL"));
                credito.setPatrimonioMunicipalSuelo(rs.getString("PATRIMONIO_MUNICIPAL_SUELO"));
                credito.setArrendamiento(rs.getInt("ARRENDAMIENTO")==1);
                credito.setClase(rs.getString("clase"));
                credito.setSubClase(rs.getString("subclase"));
             	
                if (new Long(credito.getId()) == anteriorId){
                	if(revisionAnterior != credito.getRevisionActual() && !soloMostrarVersionado){
                		credito.setVersionado(true);
	                    alRet.put(credito.getId()+"_"+credito.getRevisionActual(), credito);
	                    revisionAnterior = credito.getRevisionActual();
                	}
                }else{
                	if(idFeatures.length>0){
	                	//filtra las versiones si se selecciona una version anterior.
	                	if(rs.getString("feature_revision_actual")!=null && credito.getRevisionActual()<=rs.getLong("feature_revision_actual") ){
	                		
	                		credito.setVersionado(false);
	                		alRet.put(credito.getId()+"_"+credito.getRevisionActual(), credito);
	                		anteriorId = new Long(credito.getId());
	                		revisionAnterior = credito.getRevisionActual();  
	                		if(rs.getLong("feature_revision_actual")==rs.getLong("revision"))
	                			soloMostrarVersionado=true;
	                	}                    
		            }else{
		            		credito.setVersionado(false);
	            			alRet.put(credito.getId()+"_"+credito.getRevisionActual(), credito);
	                		anteriorId = new Long(credito.getId());
	                		revisionAnterior = credito.getRevisionActual();  
		            }
                }
    			credito.setIdFeatures(getFeaturesInventario(credito.getId(), conn, rs.getString("REVISION_ACTUAL")));
                credito.setIdLayers(getLayersInventario(credito.getId(), conn, rs.getString("REVISION_ACTUAL")));
        		if (withAllData) {
                   
        			String[] filtroBien ={" and bi.id='"+credito.getId()+"' and  bien_revertible.revision_expirada = 9999999999"};
            		credito.setObservaciones(getObservaciones(credito.getId(),TABLA_OBSERVACION_INVENTARIO,conn,rs.getLong("REVISION_ACTUAL")));
        			credito.setCuentaContable(getCuentaContable(rs.getInt("ID_CUENTA_CONTABLE")));
        			credito.setBienesRevertibles(getBienesRevertibles(null, null, filtroBien, null,userSesion,false,null));
        		}
        		
        		 if (numInventarios==null)
                 	vNumInventarios.add(credito.getNumInventario());
                 else
                	 credito.setVersionado(true);
            }
        }catch(Exception ex){
        	logger.error("Error al ejecutar: "+sSQL);
        	throw ex;
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
        }

        //Añadimos los versionados al array Total y cambiamos la informacion de versionado
        if ((vNumInventarios.size()>0) && (!configParameters.isBusquedaIndividual())){
        	Collection elementosVersionados=getCreditosDerechos(superpatron, patron, cadena,filtro, idLayers, idFeatures,vNumInventarios.toArray(),
        							userSesion,withAllData,configParameters);
        	Iterator it=elementosVersionados.iterator();
        	while (it.hasNext()){
        		CreditoDerechoBean creditoDerechoBean=(CreditoDerechoBean)it.next();
        		//String clave=vehiculo.getId()+"_"+vehiculo.getRevisionActual();
        		alRet.put(creditoDerechoBean,creditoDerechoBean);	
        	}        	
        }
                
        return alRet.values();
    }

    private CreditoDerechoBean insertCreditoDerecho(Connection conn, CreditoDerechoBean credito, String revision, Sesion userSesion) throws PermissionException,LockException,Exception{
        String sSQL= "INSERT INTO CREDITO_DERECHO (ID, ADQUISICION, FECHA_ADQUISICION, " +
        "DESTINO, CONCEPTO, CONCEPTO_DESC, DEUDOR, DESCRIPCION, PATRIMONIO_MUNICIPAL_SUELO, " +
        "IMPORTE, FECHA_VENCIMIENTO,REVISION_ACTUAL, ARRENDAMIENTO, clase, subclase) " +
        "VALUES(?,?,?,?,?,?,?,?,?,?,?,"+revision+",?, ?, ?)";


        PreparedStatement ps= null;

        try{
			if (credito == null) return null;
	
	        ps= conn.prepareStatement(sSQL);
	        ps.setLong(1, credito.getId());
	        ps.setString(2, credito.getAdquisicion());
	        if (credito.getFechaAdquisicion() == null)
	            ps.setNull(3, java.sql.Types.TIMESTAMP);
	        else ps.setTimestamp(3, new Timestamp(credito.getFechaAdquisicion().getTime()));
	        ps.setString(4, credito.getDestino());
	        ps.setString(5, credito.getConcepto());
	        ps.setString(6, credito.getConceptoDesc());
	        ps.setString(7, credito.getDeudor());
	        ps.setString(8, credito.getCaracteristicas());
	        ps.setString(9, credito.getPatrimonioMunicipalSuelo()?"1":"0");
	        if (credito.getImporte()==null)
	        	ps.setNull(10,java.sql.Types.DOUBLE);
	        else
	        	ps.setDouble(10, credito.getImporte());
	        if (credito.getFechaVencimiento() == null)
	            ps.setNull(11, java.sql.Types.TIMESTAMP);
	        else ps.setTimestamp(11, new Timestamp(credito.getFechaVencimiento().getTime()));
	        ps.setInt(12, credito.isArrendamiento()?1:0);
	        ps.setString(13, credito.getClase());
	        ps.setString(14, credito.getSubClase());
	        ps.execute();
	        ps.close();
	
	        insertVersion(conn, "credito_derecho", userSesion);
        }catch(Exception e){
        	throw e;
        }finally{
        	try{ps.close();}catch(Exception e){};
        }
        return credito;
    }
    
    private synchronized void insertVersion(Connection conn, String table, Sesion userSesion) throws PermissionException,LockException,Exception{
    	PreparedStatement ps= null;
    	try{
    		StringBuffer sbSQL = new StringBuffer("insert into versiones (revision,id_autor,fecha,id_table_versionada) values (");
    		sbSQL = sbSQL.append(this.sRevisionActual.replaceAll("\\?", table));
    		sbSQL = sbSQL.append(",");
    		sbSQL = sbSQL.append(userSesion.getIdUser());
    		sbSQL = sbSQL.append(",(select date_trunc('second', localtimestamp)),(select id_table from tables_inventario where name='");
    		sbSQL = sbSQL.append(table);
    		sbSQL = sbSQL.append("'));");    		
    		ps = conn.prepareStatement(sbSQL.toString());
    		ps.executeUpdate();
    		ps.close();
    	}catch(Exception ex){
    		logger.error("Error al introducir la version para la tabla:"+table);
    		throw ex;
    	}finally{
        	try{ps.close();}catch(Exception e){};
        }
    }

    private CreditoDerechoBean insertCreditoDerecho(Object[] idLayers, Object[] idFeatures, CreditoDerechoBean credito, Sesion userSesion) throws PermissionException,LockException,Exception{

        Connection conn=null;
        PreparedStatement ps= null;
        ResultSet rs= null;
		try {

			if (credito == null) return null;
            conn= CPoolDatabase.getConnection();
            conn.setAutoCommit(false);
            
            credito.setIdFeatures(idFeatures);
            credito.setIdLayers(idLayers);
            
            insertarBienInventario(credito,userSesion, conn, sMaxRevision.replaceAll("\\?", "credito_derecho"),sRevisionActual.replaceAll("\\?", "credito_derecho"));
            credito = insertCreditoDerecho(conn,credito,sRevisionActual.replaceAll("\\?", "credito_derecho"),userSesion);
            
            /** Insertamos las features asociadas al bien */
//            insertInventarioFeature(credito.getId(), idLayers, idFeatures, sRevisionActual.replaceAll("\\?", "credito_derecho"),userSesion, conn);


            /** Actualizamos los ficheros en disco  (temporal --> destino) */
            DocumentoEnDisco.actualizarConFicherosDeTemporal(((BienBean)credito).getDocumentos());
            credito.setDocumentos(null);
            conn.commit();

        }catch (PermissionException e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }catch (LockException e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }catch (Exception e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }/** java.lang.OutOfMemoryError */catch (java.lang.Error e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){}
        }

        return credito;
    }

    private CreditoDerechoBean updateCreditoDerecho(CreditoDerechoBean credito, Sesion userSesion) throws PermissionException,LockException,Exception{

        String sSQL= "UPDATE CREDITO_DERECHO SET REVISION_EXPIRADA="+sRevisionActual.replaceAll("\\?", "credito_derecho")+
        " WHERE ID=? "+sRevisionExpiradaNula;

        Connection conn=null;
        PreparedStatement ps= null;
        ResultSet rs= null;
		try {

			if (credito == null) return null;

            conn= CPoolDatabase.getConnection();
            conn.setAutoCommit(false);

            updateBienInventario(credito, userSesion, conn, sMaxRevision.replaceAll("\\?", "credito_derecho"), sRevisionActual.replaceAll("\\?", "credito_derecho"));

            ps= conn.prepareStatement(sSQL);
            ps.setLong(1, credito.getId());
            ps.execute();
            ps.close();

            this.insertCreditoDerecho(conn, credito, sRevisionActual.replaceAll("\\?", "credito_derecho"),userSesion);
            
            /** Actualizamos los ficheros en disco  (temporal --> destino) */
            DocumentoEnDisco.actualizarConFicherosDeTemporal(((BienBean)credito).getDocumentos());
            credito.setDocumentos(null);
            conn.commit();

            /** Borramos de disco los ficheros del bien que no esten asociados a otro */
            DocumentConnection docConn= new DocumentConnection(srid);
            docConn.borrarFicherosEnDisco(credito.getId(), docsBorrados);
            
        }catch (Exception e){
        	logger.error("Error al ejecutar: "+sSQL);
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }/** java.lang.OutOfMemoryError */catch (java.lang.Error e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){}
        }

        return credito;
    }

    private void deleteCreditoDerecho(CreditoDerechoBean credito, Connection conn) throws Exception{
        String sSQL= "UPDATE credito_derecho SET REVISION_EXPIRADA="+sRevisionActual.replaceAll("\\?", "credito_derecho");
        sSQL +=	" WHERE ID=? "+sRevisionExpiradaNula;

        PreparedStatement ps= null;
        try{
            ps= conn.prepareStatement(sSQL);
            ps.setLong(1, credito.getId());
            ps.execute();

        }catch (Exception e){
            throw e;
        }finally{
            try{ps.close();}catch(Exception e){};
        }

    }


    private Collection getSemovientes(String superpatron, String patron, String cadena, Object[] filtro, Object[] idLayers, Object[] idFeatures, 
    		Object[] numInventarios,Sesion userSesion, boolean withAllData,
    		ConfigParameters configParameters) throws Exception{
        HashMap alRet= new HashMap();
        Vector vNumInventarios=new Vector();
        String sSQL= "";

        if (cadena==null || cadena.trim().equalsIgnoreCase("")){
            sSQL= getSelectSemoviente(superpatron, patron, filtro, idLayers, idFeatures, userSesion, "");
            sSQL+= getSelectNumInventario(patron,numInventarios,TABLE_INVENTARIO_SEMOVIENTES);
           	sSQL+= " ORDER BY id ASC,  semoviente.revision_actual DESC";
         }
        else {
        	sSQL= addSelectsBusquedaSemoviente(superpatron, patron, cadena, idLayers, idFeatures, userSesion);
        	sSQL+= getSelectNumInventario(patron,numInventarios,TABLE_INVENTARIO_SEMOVIENTES);
        	sSQL+= " ORDER BY id ASC, revision_actual DESC";
        }
        
        //Parametros de LIMIT y OFFSET.
        if (numInventarios==null){
	        if (configParameters.getLimit()!=-1)
	        	sSQL+=" LIMIT "+configParameters.getLimit();
	        if (configParameters.getOffset()!=-1)
	        	sSQL+=" OFFSET "+configParameters.getOffset();
        }

        
        Connection conn=null;
        PreparedStatement ps= null;
        ResultSet rs= null;
        try{
            conn= CPoolDatabase.getConnection();
            ps= conn.prepareStatement(sSQL);
            rs= ps.executeQuery();
            SemovienteBean semoviente= null;
			long anteriorId = -1;
			long revisionAnterior = -1;
			boolean soloMostrarVersionado=false;	            
            while (rs.next()){
                semoviente= new SemovienteBean();
                semoviente.setId(rs.getLong("ID"));
                semoviente.setNumInventario(rs.getString("NUMINVENTARIO"));
                semoviente.setBorrado(rs.getString("BORRADO"));
                semoviente.setFechaAlta(rs.getTimestamp("FECHA_ALTA"));
                semoviente.setFechaAprobacionPleno(rs.getTimestamp("FECHA_APROBACION_PLENO"));
                semoviente.setFechaUltimaModificacion(rs.getTimestamp("FECHA_ULTIMA_MODIFICACION"));
                semoviente.setNombre(rs.getString("NOMBRE"));
                semoviente.setUso(rs.getString("USO"));
                semoviente.setTipo(rs.getString("TIPO"));
                semoviente.setOrganizacion(rs.getString("ORGANIZACION"));
                semoviente.setDescripcion(rs.getString("BIEN_DESCRIPCION"));
                semoviente.setRevisionActual(rs.getLong("REVISION_ACTUAL"));
                semoviente.setAdquisicion(rs.getString("ADQUISICION"));
                semoviente.setFechaAdquisicion(rs.getTimestamp("FECHA_ADQUISICION"));
                semoviente.setDestino(rs.getString("DESTINO"));
                semoviente.setRaza(rs.getString("RAZA"));
                semoviente.setCaracteristicas(rs.getString("DESCRIPCION"));
                semoviente.setEspecie(rs.getString("ESPECIE"));
                semoviente.setIdentificacion(rs.getString("IDENTIFICACION"));
                semoviente.setPropiedad(rs.getString("PROPIEDAD"));
                semoviente.setConservacion(rs.getString("CONSERVACION"));
                semoviente.setCosteAdquisicion(rs.getDouble("COSTE_ADQUISICION"));
                semoviente.setValorActual(rs.getDouble("VALOR_ACTUAL"));
                semoviente.setFrutos(rs.getString("FRUTOS"));
                semoviente.setImporteFrutos(rs.getDouble("IMPORTE_FRUTOS"));
                semoviente.setCantidad(rs.getLong("CANTIDAD"));
                semoviente.setFechaNacimiento(rs.getTimestamp("FECHA_NACIMIENTO"));
                semoviente.setRevisionExpirada(rs.getLong("REVISION_EXPIRADA"));
                semoviente.setAutor(rs.getString("NAME"));
                semoviente.setFechaVersion(rs.getTimestamp("FECHA"));
                semoviente.setPatrimonioMunicipalSuelo(rs.getString("PATRIMONIO_MUNICIPAL_SUELO"));
				if (new Long(semoviente.getId()) == anteriorId){
					if(revisionAnterior !=semoviente.getRevisionActual() && !soloMostrarVersionado){
	                	semoviente.setVersionado(true);
	                    alRet.put(semoviente.getId()+"_"+semoviente.getRevisionActual(), semoviente);
	                    revisionAnterior = semoviente.getRevisionActual();
                	}
                }else{
                	if(idFeatures.length>0){
	                	//filtra las versiones si se selecciona una version anterior.
	                	if(rs.getString("feature_revision_actual")!=null && semoviente.getRevisionActual()<=rs.getLong("feature_revision_actual") ){
	                		
	                		semoviente.setVersionado(false);
	                		alRet.put(semoviente.getId()+"_"+semoviente.getRevisionActual(), semoviente);
	                		anteriorId = new Long(semoviente.getId());
	                		revisionAnterior = semoviente.getRevisionActual();  
	                		if(rs.getLong("feature_revision_actual")==rs.getLong("revision"))
	                			soloMostrarVersionado=true;
	                	}                    
		            }else{
		            	semoviente.setVersionado(false);
	                		alRet.put(semoviente.getId()+"_"+semoviente.getRevisionActual(), semoviente);
	                		anteriorId = new Long(semoviente.getId());
	                		revisionAnterior = semoviente.getRevisionActual();  
		            }
                }
			    semoviente.setIdFeatures(getFeaturesInventario(semoviente.getId(), conn, rs.getString("REVISION_ACTUAL")));
                semoviente.setIdLayers(getLayersInventario(semoviente.getId(), conn, rs.getString("REVISION_ACTUAL")));
				if (withAllData){
					String[] filtroBien ={" and bi.id='"+semoviente.getId()+"' and  bien_revertible.revision_expirada = 9999999999"};
					semoviente.setBienesRevertibles(getBienesRevertibles(null, null, filtroBien, null,userSesion, false,null));
					semoviente.setObservaciones(getObservaciones(semoviente.getId(),TABLA_OBSERVACION_INVENTARIO,conn,rs.getLong("REVISION_ACTUAL")));
					semoviente.setCuentaContable(getCuentaContable(rs.getInt("ID_CUENTA_CONTABLE")));
	                semoviente.setSeguro(getSeguro(rs.getObject("ID_SEGURO")));
	            }
				
				if (numInventarios==null)
                	vNumInventarios.add(semoviente.getNumInventario());
                else
                	semoviente.setVersionado(true);
            }
        }catch(Exception ex){
        	logger.error("Error al ejecutar: "+sSQL);
        	throw ex;
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
        }

        //Añadimos los versionados al array Total y cambiamos la informacion de versionado
        if ((vNumInventarios.size()>0) && (!configParameters.isBusquedaIndividual())){
        	Collection elementosVersionados=getSemovientes(superpatron, patron, cadena,filtro, idLayers, idFeatures,vNumInventarios.toArray(),
        							userSesion,withAllData,configParameters);
        	Iterator it=elementosVersionados.iterator();
        	while (it.hasNext()){
        		SemovienteBean semovienteBean=(SemovienteBean)it.next();
        		//String clave=vehiculo.getId()+"_"+vehiculo.getRevisionActual();
        		alRet.put(semovienteBean,semovienteBean);	
        	}        	
        }
     		   
        return alRet.values();
    }
    /***
     * Devuelve un lote de un bien
     * @param idBien
     * @param conn
     * @return
     */
    private Lote getLoteByIdBien(long idBien, Connection conn) throws Exception {
    	 String sSQL= " SELECT lote.id_lote, lote.nombre_lote, lote.fecha_alta, lote.fecha_baja, lote.fecha_ultima_modificacion, "+ 
    	 			  "	lote.descripcion, lote.destino,lote.id_seguro "+
    	 			  "	FROM lote, lote_bien where lote_bien.id_lote= lote.id_lote and lote_bien.id_bien=? ";
    	
         PreparedStatement ps= null;
         ResultSet rs= null;
         Lote lote = null;
         try{
             ps= conn.prepareStatement(sSQL);
             ps.setLong(1, idBien);
             rs= ps.executeQuery();
             if (rs.next()){
                 lote= new Lote();
                 lote.setId_lote(rs.getLong(1));
                 lote.setNombre_lote(rs.getString(2));
                 lote.setFecha_alta(rs.getTimestamp(3));
                 lote.setFecha_baja(rs.getTimestamp(4));
                 lote.setFecha_ultima_modificacion(rs.getTimestamp(5));
                 //lote.setSeguro(rs.getString(6));
                 lote.setSeguro(getSeguro(rs.getObject("id_seguro")));
                 
                 lote.setDescripcion(rs.getString(6));
                 lote.setDestino(rs.getString(7));
             }
        }catch(Exception ex){
             	logger.error("Error al ejecutar: "+sSQL);
             	throw ex;
             }finally{
                 try{rs.close();}catch(Exception e){};
                 try{ps.close();}catch(Exception e){};
             }
         return lote;    
    }

    /***
     * Devuelve el numero de bienes de un lote
     * @param idBien
     * @param conn
     * @return
     */
    private int getNumeroBienesByLote(Lote lote, Connection conn) throws Exception {
    	 String sSQL= " SELECT * FROM lote_bien where lote_bien.id_lote= ?";
         PreparedStatement ps= null;
         ResultSet rs= null;
         int total =0;
         try{
             ps= conn.prepareStatement(sSQL);
             ps.setLong(1, lote.getId_lote());
             rs= ps.executeQuery();
             while (rs.next()){
            	 total++;
             }
             
        }catch(Exception ex){
             	logger.error("Error al ejecutar: "+sSQL);
             	throw ex;
             }finally{
                 try{rs.close();}catch(Exception e){};
                 try{ps.close();}catch(Exception e){};
             }
         return total;    
    }
    
    /***
     * Devuelve la lista de bienes revertibles
     * @param superpatron
     * @param patron
     * @param cadena
     * @param filtro
     * @param idLayers
     * @param idFeatures
     * @param userSesion
     * @return
     * @throws Exception
     */
    private Collection <BienRevertible>getBienesRevertibles(String patron, String cadena, Object[] filtro, 
    		Object[] numInventarios,Sesion userSesion, boolean loadBienes,ConfigParameters configParameters) throws Exception{
        Vector alRet= new Vector();
        Vector vNumInventarios=new Vector();
        String sSQL= "";

        if (cadena==null || cadena.trim().equalsIgnoreCase("")){
            sSQL= getSelectBienesRevertibles(patron, filtro, userSesion, "");
            sSQL+= getSelectNumInventarioRevertible(numInventarios);

        }
        else{
            sSQL= getSelectBusquedaBienesRevertibles(patron, cadena, userSesion, "");
            sSQL+= getSelectNumInventarioRevertible(numInventarios);
        }
        
    	/** añadimos la ordenacion**/
    	sSQL+=" order by bien_revertible.id ASC,bien_revertible.revision_actual DESC, bi.id, bi.revision_actual DESC";
    

        //Parametros de LIMIT y OFFSET.
        if (numInventarios==null){
        	if (configParameters!=null){
		        if (configParameters.getLimit()!=-1)
		        	sSQL+=" LIMIT "+configParameters.getLimit();
		        if (configParameters.getOffset()!=-1)
		        	sSQL+=" OFFSET "+configParameters.getOffset();
        	}
        }
        	
        logger.info("Sentencia getRevertibles:"+sSQL);
        Connection conn=null;
        PreparedStatement ps= null;
        ResultSet rs= null;
        try{
            conn= CPoolDatabase.getConnection();
            ps= conn.prepareStatement(sSQL);
            rs= ps.executeQuery();
            BienRevertible brAnterior=null;
            BienRevertible br=null;
            BienBean bienAnterior=null;
           while (rs.next()){
            	if (brAnterior==null || brAnterior.getId()!=rs.getLong("br_id") || 
            			brAnterior.getRevisionActual()!= rs.getLong("br_revision_actual")){
            		br= new BienRevertible();
            		br.setId(rs.getLong("br_id"));
            		br.setNumInventario(rs.getString("br_num_inventario"));
            		Object aux=rs.getObject("br_fecha_inicio");
            		if (aux!=null)
            			br.setFechaInicio(rs.getTimestamp("br_fecha_inicio"));
            		aux=rs.getObject("br_fecha_vencimiento");
            		if (aux!=null)
            			br.setFechaVencimiento(rs.getTimestamp("br_fecha_vencimiento"));
            		aux=rs.getObject("br_fecha_transmision");
            		if (aux!=null)
            			br.setFechaTransmision(rs.getTimestamp("br_fecha_transmision"));
            		aux=rs.getObject("br_fecha_baja");
            		if (aux!=null)
            			br.setFechaBaja(rs.getTimestamp("br_fecha_baja"));
            		br.setBorrado(rs.getString("br_borrado"));
            		br.setImporte(rs.getDouble("br_importe"));
            		br.setPoseedor(rs.getString("br_poseedor"));
            		br.setTituloPosesion(rs.getString("br_titulo_posesion"));
            		br.setCondicionesReversion(rs.getString("br_condiciones_reversion"));
            		br.setDetalles(rs.getString("br_detalles"));
            		br.setCatTransmision(rs.getString("br_cat_transmision"));
            		alRet.add(br);
            		br.setObservaciones(getObservaciones(br.getId(),TABLA_OBSERVACION_BIEN_REVERTIBLE,conn,rs.getLong("br_revision_actual")));
            		br.setCuentaAmortizacion(getCuentaAmortizacion(rs.getLong("br_id_cuenta_amortizacion")));
            		br.setSeguro(getSeguro(rs.getObject("br_id_seguro")));
            		br.setRevisionActual(rs.getLong("br_revision_actual"));
            		br.setRevisionExpirada(rs.getLong("br_revision_expirada"));
            		br.setAutor(rs.getString("NAME"));
            		br.setFechaVersion(rs.getTimestamp("FECHA"));
            		aux=rs.getObject("br_fecha_alta");
            		if (aux!=null)
            			br.setFechaAlta(rs.getTimestamp("br_fecha_alta"));
            		aux=rs.getObject("br_fecha_ultima_modificacion");
            		if (aux!=null)
            			br.setFechaUltimaModificacion(rs.getTimestamp("br_fecha_ultima_modificacion"));
                    br.setNombre(rs.getString("br_nombre"));
                    br.setOrganizacion(rs.getString("br_organizacion"));
                    aux=rs.getObject("br_fecha_aprobacion_pleno");
            		if (aux!=null)
            			br.setFecha_aprobacion_pleno(rs.getTimestamp("br_fecha_aprobacion_pleno"));
            		br.setDescripcion_bien(rs.getString("br_descripcion_bien"));
            		aux=rs.getObject("br_fecha_adquisicion");
            		if (aux!=null)
            			br.setFecha_adquisicion(rs.getTimestamp("br_fecha_adquisicion"));
            	    br.setAdquisicion(rs.getString("br_adquisicion"));
            	    br.setDiagnosis(rs.getString("br_diagnosis"));
            	    br.setPatrimonioMunicipalSuelo(rs.getString("br_patrimonio_municipal_suelo"));
                    br.setClase(rs.getString("br_clase"));
            		if (brAnterior!=null){
            			if (br.getId().longValue()==brAnterior.getId().longValue())
            				br.setVersionado(true);
            			else
            				br.setVersionado(false);
            		}
            	    
            		brAnterior=br;
            		bienAnterior=null;
            	}
            	if (loadBienes){
            		if (rs.getObject("bi_id")!=null){
            			if (bienAnterior ==null || bienAnterior.getId()!=rs.getLong("bi_id")){
            				BienBean bien= new BienBean();
            				bien.setId(rs.getLong("bi_id"));
            				bien.setNumInventario(rs.getString("bi_numinventario"));
            				bien.setTipo(rs.getString("bi_tipo"));
            				bien.setDescripcion(rs.getString("bi_descripcion"));
            				bien.setNombre(rs.getString("bi_nombre"));
            				try{bien.setFechaAlta(rs.getDate("bi_fecha_alta"));}catch(Exception ex){}
            				try{bien.setFechaAprobacionPleno(rs.getDate("bi_fecha_aprobacion_pleno"));}catch(Exception ex){}
            				try{bien.setFechaBaja(rs.getDate("bi_fecha_baja"));}catch(Exception ex){}
            				try{bien.setFechaUltimaModificacion(rs.getDate("bi_fecha_ultima_modificacion"));}catch(Exception ex){}
            				bien.setUso(rs.getString("bi_uso"));
            				bien.setBloqueado(rs.getString("bi_bloqueado"));
            				bien.setRevisionExpirada(rs.getLong("bi_revision_expirada"));
            				br.addBien(bien);
            				bienAnterior=bien;
            			}
            		}
            	}
            	 if (numInventarios==null)
 	                vNumInventarios.add(br.getNumInventario());
 	             else
 	                br.setVersionado(true);
            }
        }catch(Exception ex){
        	logger.error("Error al ejecutar: "+sSQL);
        	throw ex;
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
        }
        
        
        //Añadimos los versionados al array Total y cambiamos la informacion de versionado
        if ((vNumInventarios.size()>0) && (!configParameters.isBusquedaIndividual())){
        	Collection elementosVersionados=getBienesRevertibles(patron, cadena,filtro,vNumInventarios.toArray(),
        							userSesion,loadBienes,configParameters);
        	Iterator it=elementosVersionados.iterator();
        	while (it.hasNext()){
        		BienRevertible bienRevertibleBean=(BienRevertible)it.next();
        		//String clave=vehiculo.getId()+"_"+vehiculo.getRevisionActual();
        		alRet.add(bienRevertibleBean);	
        	}        	
        }

        return (Collection)alRet;
    }
    
    /***
     * Devuelve la lista de bienes revertibles
     * @param superpatron
     * @param patron
     * @param cadena
     * @param filtro
     * @param idLayers
     * @param idFeatures
     * @param userSesion
     * @return
     * @throws Exception
     */
    private Collection <BienBean>getBienes(String superpatron,String cadena, Object[] filtro,
    					Object[] idLayers, Object[] idFeatures) throws Exception{
        Vector alRet= new Vector();
        String sSQL= "";

        if (cadena==null || cadena.trim().equalsIgnoreCase(""))
            sSQL= getSelectBienes(superpatron,filtro,idLayers,idFeatures);
      
        Connection conn=null;
        PreparedStatement ps= null;
        ResultSet rs= null;
        try{
            conn= CPoolDatabase.getConnection();
            ps= conn.prepareStatement(sSQL);
            rs= ps.executeQuery();
            long anteriorId = -1;
            long revisionAnterior = -1;
           while (rs.next()){
                		BienBean bien= new BienBean();
	            		bien.setId(rs.getLong("id"));
	            		bien.setNumInventario(rs.getString("numinventario"));
		                bien.setTipo(rs.getString("tipo"));
		                bien.setDescripcion(rs.getString("descripcion"));
		                bien.setNombre(rs.getString("nombre"));
		                
		                bien.setRevisionActual(rs.getLong("REVISION_ACTUAL"));
		                bien.setRevisionExpirada(rs.getLong("REVISION_EXPIRADA"));
		                
		                try{bien.setFechaAlta(rs.getDate("fecha_alta"));}catch(Exception ex){}
		                try{bien.setFechaAprobacionPleno(rs.getDate("fecha_aprobacion_pleno"));}catch(Exception ex){}
			            try{bien.setFechaBaja(rs.getDate("fecha_baja"));}catch(Exception ex){}
		                try{bien.setFechaUltimaModificacion(rs.getDate("fecha_ultima_modificacion"));}catch(Exception ex){}
		                bien.setUso(rs.getString("uso"));
		                bien.setBloqueado(rs.getString("bloqueado"));
		                
		                
		                if (new Long(bien.getId()) == anteriorId){
		                	if(revisionAnterior != bien.getRevisionActual()){
		                		bien.setVersionado(true);
			                    //alRet.put(inmueble.getId()+"_"+inmueble.getRevisionActual(), inmueble);
			                    revisionAnterior = bien.getRevisionActual();
		                	}
		                }else{
		                	bien.setVersionado(false);
		                    //alRet.put(inmueble.getId()+"_"+inmueble.getRevisionActual(), inmueble);
		                    anteriorId = new Long(bien.getId());
		                    revisionAnterior = bien.getRevisionActual();
		                }
		                
		                alRet.add(bien);
		    }
        }catch(Exception ex){
        	logger.error("Error al ejecutar: "+sSQL);
        	throw ex;
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
        }

        return (Collection)alRet;
    }
    
    /***
     * Devuelve la lista de Lotes
     * @param superpatron
     * @param patron
     * @param cadena
     * @param filtro
     * @param idLayers
     * @param idFeatures
     * @param userSesion
     * @return
     * @throws Exception
     */
    private Collection <Lote>getLotes(String patron, String cadena, Object[] filtro, 
    		Object[] numInventarios,Sesion userSesion,ConfigParameters configParameters) throws Exception{
        Vector alRet= new Vector();
        String sSQL= "";

        if (cadena==null || cadena.trim().equalsIgnoreCase("")){
            sSQL= getSelectLotes(patron, filtro, userSesion, "");
            sSQL+= getSelectNumInventarioLotes(patron,numInventarios,TABLE_INVENTARIO_INMUEBLES);
        }
        else{
        	sSQL= getSelectBusquedaLotes(patron, cadena, userSesion, "");
            sSQL+= getSelectNumInventarioLotes(patron,numInventarios,TABLE_INVENTARIO_INMUEBLES);
        }
      
        /** añadimos la ordenacion**/
    	sSQL+=" order by lote.id_lote , bi.id";
      
    	

        //Parametros de LIMIT y OFFSET.
        if (numInventarios==null){
        	if (configParameters!=null){
		        if (configParameters.getLimit()!=-1)
		        	sSQL+=" LIMIT "+configParameters.getLimit();
		        if (configParameters.getOffset()!=-1)
		        	sSQL+=" OFFSET "+configParameters.getOffset();
        	}
        }
        
    	Connection conn=null;
        PreparedStatement ps= null;
        ResultSet rs= null;
        try{
            conn= CPoolDatabase.getConnection();
            ps= conn.prepareStatement(sSQL);
            rs= ps.executeQuery();
            Lote loteAnterior=null;
            Lote lote=null;
           while (rs.next()){
            	if (loteAnterior==null || loteAnterior.getId_lote()!=rs.getLong(1) ){
            		lote= new Lote();
            		lote.setId_lote(rs.getLong(1));
                     lote.setNombre_lote(rs.getString(2));
                     lote.setFecha_alta(rs.getTimestamp(3));
                     lote.setFecha_baja(rs.getTimestamp(4));
                     lote.setFecha_ultima_modificacion(rs.getTimestamp(5));
                     //lote.setSeguro(rs.getString(6));
                     lote.setSeguro(getSeguro(rs.getObject("id_seguro")));
                     lote.setDescripcion(rs.getString(6));
                     lote.setDestino(rs.getString(7));
                     alRet.add(lote);
                 	 loteAnterior=lote;
            	}	
                     
                MuebleBean bien= new MuebleBean();
                bien.setId(rs.getLong("bi_id"));
                bien.setNumInventario(rs.getString("bi_numinventario"));
                bien.setTipo(rs.getString("bi_tipo"));
                bien.setDescripcion(rs.getString("bi_descripcion"));
                bien.setNombre(rs.getString("bi_nombre"));
                try{bien.setFechaAlta(rs.getDate("bi_fecha_alta"));}catch(Exception ex){}
                try{bien.setFechaBaja(rs.getDate("bi_fecha_baja"));}catch(Exception ex){}
                try{bien.setFechaUltimaModificacion(rs.getDate("bi_fecha_ultima_modificacion"));}catch(Exception ex){}
                bien.setUso(rs.getString("bi_uso"));
                bien.setBloqueado(rs.getString("bi_bloqueado"));
                bien.setRevisionActual(rs.getLong("bi_revision_actual"));
                bien.setRevisionExpirada(new Long("9999999999"));
                lote.addBien(bien);
             
              
            }
        }catch(Exception ex){
        	logger.error("Error al ejecutar: "+sSQL);
        	throw ex;
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
        }

        return (Collection)alRet;
    }
    
 
    /**
     * @param conn
     * @param semoviente
     * @param revision
     * @param userSesion
     * @throws PermissionException
     * @throws LockException
     * @throws Exception
     */
    private void insertSemoviente(Connection conn, SemovienteBean semoviente, String revision, Sesion userSesion) throws PermissionException,LockException,Exception{

        String sSQL= "INSERT INTO SEMOVIENTE (ID, ADQUISICION, FECHA_ADQUISICION, " +
        "DESTINO, ESPECIE, RAZA, DESCRIPCION, PROPIEDAD, CONSERVACION, PATRIMONIO_MUNICIPAL_SUELO, " +
        "CANTIDAD, FECHA_NACIMIENTO, IDENTIFICACION, COSTE_ADQUISICION, VALOR_ACTUAL, " +
        "FRUTOS, IMPORTE_FRUTOS, REVISION_ACTUAL) " +
        "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"+revision+")";

        PreparedStatement ps= null;

        try{
	        if (semoviente == null) return;
	
	        ps= conn.prepareStatement(sSQL);
	        ps.setLong(1, semoviente.getId());
	        ps.setString(2, semoviente.getAdquisicion());
	        if (semoviente.getFechaAdquisicion() == null)
	            ps.setNull(3, java.sql.Types.TIMESTAMP);
	        else ps.setTimestamp(3, new Timestamp(semoviente.getFechaAdquisicion().getTime()));
	        ps.setString(4, semoviente.getDestino());
	        ps.setString(5, semoviente.getEspecie());
	        ps.setString(6, semoviente.getRaza());
	        ps.setString(7, semoviente.getCaracteristicas());
	        ps.setString(8, semoviente.getPropiedad());
	        ps.setString(9, semoviente.getConservacion());
	        ps.setString(10, semoviente.getPatrimonioMunicipalSuelo()?"1":"0");
	        if (semoviente.getCosteAdquisicion()==null ||semoviente.getCantidad()==null)
	        	ps.setNull(11,java.sql.Types.NUMERIC );
	        else 
	        	ps.setLong(11, semoviente.getCantidad());
	        if (semoviente.getFechaNacimiento() == null)
	            ps.setNull(12, java.sql.Types.TIMESTAMP);
	        else ps.setTimestamp(12, new Timestamp(semoviente.getFechaNacimiento().getTime()));
	        ps.setString(13, semoviente.getIdentificacion());
	        if (semoviente.getCosteAdquisicion()==null)
	        	ps.setNull(14,java.sql.Types.DOUBLE );
	        else
	        	ps.setDouble(14, semoviente.getCosteAdquisicion());
	    
	        if (semoviente.getValorActual()==null)
	        	ps.setNull(15,java.sql.Types.DOUBLE );
	        else
	        	ps.setDouble(15, semoviente.getValorActual());
	    
	        ps.setString(16, semoviente.getFrutos());
	        
	        if (semoviente.getImporteFrutos()==null)
	        	ps.setNull(17,java.sql.Types.DOUBLE );
	        else
	        	ps.setDouble(17, semoviente.getImporteFrutos());
	    
	        ps.execute();
	
	        insertVersion(conn, "semoviente", userSesion);
        }catch(Exception e){
        	throw e;
        }finally{
        	try{ps.close();}catch(Exception e){};
        }
    }
        
    private SemovienteBean insertSemoviente(Object[] idLayers, Object[] idFeatures, SemovienteBean semoviente, Sesion userSesion) throws PermissionException,LockException,Exception{

        Connection conn=null;
        PreparedStatement ps= null;
        ResultSet rs= null;
		try {

			if (semoviente == null) return null;
            conn= CPoolDatabase.getConnection();
            conn.setAutoCommit(false);

            semoviente.setIdFeatures(idFeatures);
            semoviente.setIdLayers(idLayers);
            
            insertarBienInventario(semoviente, userSesion, conn, sMaxRevision.replaceAll("\\?", "semoviente"),sRevisionActual.replaceAll("\\?", "semoviente"));
            
            insertSemoviente(conn, semoviente,sRevisionActual.replaceAll("\\?", "semoviente"),userSesion);

            /** Insertamos las features asociadas al bien */
//            insertInventarioFeature(semoviente.getId(), idLayers, idFeatures, sRevisionActual.replaceAll("\\?", "semoviente"), userSesion, conn);


            /** Actualizamos los ficheros en disco  (temporal --> destino) */
            DocumentoEnDisco.actualizarConFicherosDeTemporal(((BienBean)semoviente).getDocumentos());
            semoviente.setDocumentos(null);
            conn.commit();

        }catch (PermissionException e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }catch (LockException e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }catch (Exception e){
        	try{conn.rollback();}catch(Exception ex){};
            throw e;
        }/** java.lang.OutOfMemoryError */catch (java.lang.Error e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){}
        }

        return semoviente;
    }


    private SemovienteBean updateSemoviente(SemovienteBean semoviente, Sesion userSesion) throws PermissionException,LockException,Exception{

        String sSQL= "UPDATE SEMOVIENTE SET REVISION_EXPIRADA="+sRevisionActual.replaceAll("\\?", "semoviente")+
        " WHERE ID=? "+sRevisionExpiradaNula;

        Connection conn=null;
        PreparedStatement ps= null;
        ResultSet rs= null;
		try {

			if (semoviente == null) return null;

            conn= CPoolDatabase.getConnection();
            conn.setAutoCommit(false);

            updateBienInventario(semoviente, userSesion, conn, sMaxRevision.replaceAll("\\?", "semoviente"),sRevisionActual.replaceAll("\\?", "semoviente"));

            ps= conn.prepareStatement(sSQL);
            ps.setLong(1, semoviente.getId());
            ps.execute();
            ps.close();

            this.insertSemoviente(conn, semoviente, sRevisionActual.replaceAll("\\?", "semoviente"), userSesion);

            /** Actualizamos los ficheros en disco  (temporal --> destino) */
            DocumentoEnDisco.actualizarConFicherosDeTemporal(((BienBean)semoviente).getDocumentos());
            semoviente.setDocumentos(null);
            conn.commit();

            /** Borramos de disco los ficheros del bien que no esten asociados a otro */
            DocumentConnection docConn= new DocumentConnection(srid);
            docConn.borrarFicherosEnDisco(semoviente.getId(), docsBorrados);

        }catch (Exception e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }/** java.lang.OutOfMemoryError */catch (java.lang.Error e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){}
        }

        return semoviente;
    }

    private void deleteSemoviente(SemovienteBean semoviente, Connection conn) throws Exception{
        String sSQL= "UPDATE semoviente SET REVISION_EXPIRADA="+sRevisionActual.replaceAll("\\?", "semoviente");
        sSQL +=	" WHERE ID=? "+sRevisionExpiradaNula;

        PreparedStatement ps= null;
        try{
            ps= conn.prepareStatement(sSQL);
            ps.setLong(1, semoviente.getId());
            ps.execute();

        }catch (Exception e){
            throw e;
        }finally{
            try{ps.close();}catch(Exception e){};
        }

    }


    private Collection getVias(String superpatron, String patron, String cadena, Object[] filtro, 
    		Object[] idLayers, Object[] idFeatures,Object[] numInventarios, 
    		Sesion userSesion, boolean withAllData,ConfigParameters configParameters) throws Exception{
        HashMap alRet= new HashMap();
        Vector vNumInventarios=new Vector();
        String sSQL= "";

        if (cadena==null || cadena.trim().equalsIgnoreCase("")){
            sSQL= getSelectVias(superpatron, patron, filtro, idLayers, idFeatures, userSesion, "");
            sSQL+= getSelectNumInventario(patron,numInventarios,TABLE_INVENTARIO_VIAS);
            sSQL+= " ORDER BY id ASC, vias_inventario.revision_actual DESC";
        }
        else {
        	sSQL= addSelectsBusquedaVias(superpatron, patron, cadena, idLayers, idFeatures, userSesion);
        	sSQL+= getSelectNumInventario(patron,numInventarios,TABLE_INVENTARIO_VIAS);
        	sSQL+= " ORDER BY id ASC, revision_actual DESC";

        }
        
        //Parametros de LIMIT y OFFSET.
        if (numInventarios==null){
	        if (configParameters.getLimit()!=-1)
	        	sSQL+=" LIMIT "+configParameters.getLimit();
	        if (configParameters.getOffset()!=-1)
	        	sSQL+=" OFFSET "+configParameters.getOffset();
        }

        logger.info("Sentencia getVias:"+sSQL);
        Connection conn=null;
        PreparedStatement ps= null;
        ResultSet rs= null;
        try{
            conn= CPoolDatabase.getConnection();
            ps= conn.prepareStatement(sSQL);
            rs= ps.executeQuery();
            ViaBean via= null;
            long anteriorId = -1;
            long revisionAnterior = -1;
            boolean soloMostrarVersionado=false;	            
            while (rs.next()){
                via= new ViaBean();
                via.setId(rs.getLong("ID"));
                via.setNumInventario(rs.getString("NUMINVENTARIO"));
                via.setBorrado(rs.getString("BORRADO"));
                via.setFechaAlta(rs.getTimestamp("FECHA_ALTA"));
                via.setFechaAprobacionPleno(rs.getTimestamp("FECHA_APROBACION_PLENO"));
                via.setFechaUltimaModificacion(rs.getTimestamp("FECHA_ULTIMA_MODIFICACION"));
                via.setNombre(rs.getString("BIEN_NOMBRE"));
                via.setUso(rs.getString("USO"));
                via.setTipo(rs.getString("TIPO"));
                via.setOrganizacion(rs.getString("ORGANIZACION"));
                via.setDescripcion(rs.getString("DESCRIPCION"));
                via.setRevisionActual(rs.getLong("REVISION_ACTUAL"));
                via.setAdquisicion(rs.getString("ADQUISICION"));
                via.setFechaAdquisicion(rs.getTimestamp("FECHA_ADQUISICION"));
                via.setDestino(rs.getString("DESTINO"));
                via.setCodigo(rs.getString("CODIGO"));
                via.setCategoria(rs.getString("CATEGORIA"));
                via.setNombreVia(rs.getString("NOMBRE"));
                via.setInicioVia(rs.getString("INICIO"));
                via.setFinVia(rs.getString("FIN"));
                via.setNumApliques(rs.getLong("NUM_APLIQUES"));
                via.setNumBancos(rs.getLong("NUM_BANCOS"));
                via.setNumPapeleras(rs.getLong("NUM_PAPELERAS"));
                via.setValorActual(rs.getDouble("VALOR_ACTUAL"));
                via.setMetrosPavimentados(rs.getDouble("METROS_PAVIMENTADOS"));
                via.setMetrosNoPavimentados(rs.getDouble("METROS_NO_PAVIMENTADOS"));
                via.setZonasVerdes(rs.getDouble("ZONAS_VERDES"));
                via.setLongitud(rs.getDouble("LONGITUD"));
                via.setAncho(rs.getDouble("METROS_PAVIMENTADOS"));
                via.setRevisionExpirada(rs.getLong("REVISION_EXPIRADA"));
                via.setAutor(rs.getString("NAME"));
                via.setFechaVersion(rs.getTimestamp("FECHA"));
                via.setPatrimonioMunicipalSuelo(rs.getString("PATRIMONIO_MUNICIPAL_SUELO"));
				via.setCuentaContable(getCuentaContable(rs.getInt("ID_CUENTA_CONTABLE")));
				via.setClase(rs.getString("clase"));
                if (new Long(via.getId()) == anteriorId){
                	if(revisionAnterior != via.getRevisionActual() && !soloMostrarVersionado){
                		via.setVersionado(true);
	                    alRet.put(via.getId()+"_"+via.getRevisionActual(), via);
	                    revisionAnterior = via.getRevisionActual();
                	}
                }else{
                	if(idFeatures.length>0){
	                	//filtra las versiones si se selecciona una version anterior.
	                	if(rs.getString("feature_revision_actual")!=null && via.getRevisionActual()<=rs.getLong("feature_revision_actual") ){
	                		
	                		via.setVersionado(false);
	                		alRet.put(via.getId()+"_"+via.getRevisionActual(), via);
	                		anteriorId = new Long(via.getId());
	                		revisionAnterior = via.getRevisionActual();  
	                		if(rs.getLong("feature_revision_actual")==rs.getLong("revision"))
	                			soloMostrarVersionado=true;
	                	}                    
		            }else{
		            		via.setVersionado(false);
	                		alRet.put(via.getId()+"_"+via.getRevisionActual(),via);
	                		anteriorId = new Long(via.getId());
	                		revisionAnterior = via.getRevisionActual();  
		            }
                }
                via.setIdFeatures(getFeaturesInventario(via.getId(), conn, rs.getString("REVISION_ACTUAL")));
                via.setIdLayers(getLayersInventario(via.getId(), conn, rs.getString("REVISION_ACTUAL")));
                if (withAllData){
                	String[] filtroBien ={" and bi.id='"+via.getId()+"' and  bien_revertible.revision_expirada = 9999999999"};
                	via.setBienesRevertibles(getBienesRevertibles(null, null, filtroBien, null,userSesion,false,null));
                	via.setObservaciones(getObservaciones(via.getId(),TABLA_OBSERVACION_INVENTARIO,conn,rs.getLong("REVISION_ACTUAL")));
                    via.setSeguro(getSeguro(rs.getObject("ID_SEGURO")));
                    via.setMejoras(getMejoras(via.getId(), conn,rs.getLong("REVISION_ACTUAL")));
                    via.setReferenciasCatastrales(getRefCatastrales(via.getId(), conn,rs.getLong("REVISION_ACTUAL")));
                }

                if (numInventarios==null)
                	vNumInventarios.add(via.getNumInventario());
                else
                	via.setVersionado(true);
            }
        }catch(Exception ex){
        	logger.error("Error al ejecutar: "+sSQL);
        	throw ex;
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
        }


        //Añadimos los versionados al array Total y cambiamos la informacion de versionado
        if ((vNumInventarios.size()>0) && (!configParameters.isBusquedaIndividual())){
        	Collection elementosVersionados=getVias(superpatron, patron, cadena,filtro, idLayers, idFeatures,vNumInventarios.toArray(),
        							userSesion,withAllData,configParameters);
        	Iterator it=elementosVersionados.iterator();
        	while (it.hasNext()){
        		ViaBean viaBean=(ViaBean)it.next();
        		//String clave=vehiculo.getId()+"_"+vehiculo.getRevisionActual();
        		alRet.put(viaBean,viaBean);	
        	}        	
        }
        return alRet.values();
    }

    private void insertVia(Connection conn, ViaBean via, String revision, Sesion userSesion) throws PermissionException,LockException,Exception{

        String sSQL= "INSERT INTO VIAS_INVENTARIO (ID, ADQUISICION, FECHA_ADQUISICION, " +
        "DESTINO, CATEGORIA, CODIGO, NOMBRE, NUM_APLIQUES, NUM_BANCOS, NUM_PAPELERAS, PATRIMONIO_MUNICIPAL_SUELO, " +
        "METROS_PAVIMENTADOS, METROS_NO_PAVIMENTADOS, VALOR_ACTUAL, " +
        "INICIO, FIN, ZONAS_VERDES, LONGITUD, ANCHO, REVISION_ACTUAL, clase) " +
        "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"+revision+", ?)";

        PreparedStatement ps= null;

        try{
			if (via == null) return;
	
	        ps= conn.prepareStatement(sSQL);
	        ps.setLong(1, via.getId());
	        ps.setString(2, via.getAdquisicion());
	        if (via.getFechaAdquisicion() == null)
	            ps.setNull(3, java.sql.Types.TIMESTAMP);
	        else ps.setTimestamp(3, new Timestamp(via.getFechaAdquisicion().getTime()));
	        ps.setString(4, via.getDestino());
	        ps.setString(5, via.getCategoria());
	        ps.setString(6, via.getCodigo());
	        ps.setString(7, via.getNombreVia());
	        if (via.getNumApliques()==null)
	        	ps.setNull(8, java.sql.Types.DOUBLE);
	        else
	        	ps.setDouble(8, via.getNumApliques().longValue());
	        if (via.getNumBancos()==null)
	        	ps.setNull(9, java.sql.Types.DOUBLE);
	        else
	        	ps.setDouble(9, via.getNumBancos().longValue());
	        if (via.getNumPapeleras()==null)
	        	ps.setNull(10, java.sql.Types.DOUBLE);
	        else
	        	ps.setDouble(10, via.getNumPapeleras().longValue());
	        ps.setString(11, via.getPatrimonioMunicipalSuelo()?"1":"0");
	        if (via.getMetrosPavimentados()==null)
	        	ps.setNull(12, java.sql.Types.DOUBLE);
	        else
	            ps.setDouble(12, via.getMetrosPavimentados());
	        if (via.getMetrosNoPavimentados()==null)
	        	ps.setNull(13, java.sql.Types.DOUBLE);
	        else
	            ps.setDouble(13, new Double(via.getMetrosNoPavimentados()).doubleValue());
	        if (via.getValorActual()==null)
	        	ps.setNull(14, java.sql.Types.DOUBLE);
	        else
	            ps.setDouble(14, new Double(via.getValorActual()).doubleValue());
	        ps.setString(15, via.getInicioVia());
	        ps.setString(16, via.getFinVia());
	        if (via.getZonasVerdes()==null)
	        	ps.setNull(17, java.sql.Types.DOUBLE);
	        else
	            ps.setDouble(17, via.getZonasVerdes());
	        if (via.getLongitud()==null)
	        	ps.setNull(18, java.sql.Types.DOUBLE);
	        else
	            ps.setDouble(18, via.getLongitud());
	        if (via.getAncho()==null)
	        	ps.setNull(19, java.sql.Types.DOUBLE);
	        else
	            ps.setDouble(19, via.getAncho());
	
	        ps.setString(20, via.getClase());
	        ps.execute();
	        ps.close();
	
	        insertVersion(conn, "vias_inventario", userSesion);
        }catch(Exception e){
        	throw e;
        }finally{
        	try{ps.close();}catch(Exception e){};
        }
    }
    
    private ViaBean insertVia(Object[] idLayers, Object[] idFeatures, ViaBean via, Sesion userSesion) throws PermissionException,LockException,Exception{

        Connection conn=null;
        PreparedStatement ps= null;
        ResultSet rs= null;
		try {

			if (via == null) return null;
            conn= CPoolDatabase.getConnection();
            conn.setAutoCommit(false);

            via.setIdFeatures(idFeatures);
            via.setIdLayers(idLayers);
            
            insertarBienInventario(via, userSesion, conn, sMaxRevision.replaceAll("\\?", "vias_inventario"),sRevisionActual.replaceAll("\\?", "vias_inventario"));
            
            insertVia(conn,via,sRevisionActual.replaceAll("\\?", "vias_inventario"),userSesion);

            /** Insertamos las features asociadas al bien */
//            insertInventarioFeature(via.getId(), idLayers, idFeatures, sRevisionActual.replaceAll("\\?", "vias_inventario"),userSesion, conn);


            /** Actualizamos los ficheros en disco  (temporal --> destino) */
            DocumentoEnDisco.actualizarConFicherosDeTemporal(((BienBean)via).getDocumentos());
            via.setDocumentos(null);
            conn.commit();

        }catch (PermissionException e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }catch (LockException e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }catch (Exception e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }/** java.lang.OutOfMemoryError */catch (java.lang.Error e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){}
        }

        return via;
    }

    private ViaBean updateVia(ViaBean via, Sesion userSesion) throws PermissionException,LockException,Exception{

        String sSQL= "UPDATE VIAS_INVENTARIO SET REVISION_EXPIRADA="+sRevisionActual.replaceAll("\\?", "vias_inventario")+
                " WHERE ID=? "+sRevisionExpiradaNula;

        Connection conn=null;
        PreparedStatement ps= null;
        ResultSet rs= null;
		try {

			if (via == null) return null;

            conn= CPoolDatabase.getConnection();
            conn.setAutoCommit(false);

            updateBienInventario(via, userSesion, conn, sMaxRevision.replaceAll("\\?", "vias_inventario"), sRevisionActual.replaceAll("\\?", "vias_inventario"));

            
            
            
            ps= conn.prepareStatement(sSQL);
            ps.setLong(1, via.getId());
            ps.execute();
            ps.close();

            this.insertVia(conn, via, sRevisionActual.replaceAll("\\?", "vias_inventario"), userSesion);

            /** Actualizamos los ficheros en disco  (temporal --> destino) */
            DocumentoEnDisco.actualizarConFicherosDeTemporal(((BienBean)via).getDocumentos());
            via.setDocumentos(null);
            conn.commit();

            /** Borramos de disco los ficheros del bien que no esten asociados a otro */
            DocumentConnection docConn= new DocumentConnection(srid);
            docConn.borrarFicherosEnDisco(via.getId(), docsBorrados);

        }catch (Exception e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }/** java.lang.OutOfMemoryError */catch (java.lang.Error e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){}
        }

        return via;
    }

    private void deleteVia(ViaBean via, Connection conn) throws Exception{
        String sSQL= "UPDATE vias_inventario SET REVISION_EXPIRADA="+sRevisionActual.replaceAll("\\?", "vias_inventario");
        sSQL +=	" WHERE ID=? "+sRevisionExpiradaNula;

        PreparedStatement ps= null;
        try{
            ps= conn.prepareStatement(sSQL);
            ps.setLong(1, via.getId());
            ps.execute();

        }catch (Exception e){
            throw e;
        }finally{
            try{ps.close();}catch(Exception e){};
        }

    }

    private Collection getVehiculos(String superpatron, String patron, String cadena, Object[] filtro, Object[] idLayers,
    		Object[] idFeatures, Object[] numInventarios,Sesion userSesion, boolean withAllData, 
    		ConfigParameters configParameters) throws Exception{
        HashMap alRet= new HashMap();
        
        Vector vNumInventarios=new Vector();
        String sSQL= "";

        //Inicialmente recuperamos los bienes sin version y luego para los que recuperemos obtenemos
        //las versiones correspondientes. Esto sirve para que solo recuperemos un numero determinado
        //de bienes definido por el valor "LIMIT" y por el valor "OFFSET".
        
        if (cadena==null || cadena.trim().equalsIgnoreCase("")){
        	 sSQL= getSelectVehiculo(superpatron, patron, filtro, idLayers, idFeatures, userSesion, "");
        	 sSQL+= getSelectNumInventario(patron,numInventarios,TABLE_INVENTARIO_VEHICULOS);
        	 sSQL+= " ORDER BY id ASC, vehiculo.revision_actual DESC";
        }
        else {
        	sSQL= addSelectsBusquedaVehiculo(superpatron, patron, cadena, idLayers, idFeatures, userSesion);
       	 	sSQL+= getSelectNumInventario(patron,numInventarios,TABLE_INVENTARIO_VEHICULOS);
        	sSQL+= " ORDER BY id ASC, revision_actual DESC";
        }
        
        //Parametros de LIMIT y OFFSET.
        if (numInventarios==null){
	        if (configParameters.getLimit()!=-1)
	        	sSQL+=" LIMIT "+configParameters.getLimit();
	        if (configParameters.getOffset()!=-1)
	        	sSQL+=" OFFSET "+configParameters.getOffset();
        }

        
        Connection conn=null;
        PreparedStatement ps= null;
        ResultSet rs= null;
        try{
            conn= CPoolDatabase.getConnection();
            ps= conn.prepareStatement(sSQL);
            rs= ps.executeQuery();
            VehiculoBean vehiculo= null;
            long anteriorId = -1;
            long revisionAnterior = -1;
            boolean soloMostrarVersionado=false;
            while (rs.next()){
                vehiculo= new VehiculoBean();
                vehiculo.setId(rs.getLong("ID"));
                vehiculo.setNumInventario(rs.getString("NUMINVENTARIO"));
                vehiculo.setBorrado(rs.getString("BORRADO"));
                vehiculo.setFechaAlta(rs.getTimestamp("FECHA_ALTA"));
                vehiculo.setFechaAprobacionPleno(rs.getTimestamp("FECHA_APROBACION_PLENO"));
                vehiculo.setFechaUltimaModificacion(rs.getTimestamp("FECHA_ULTIMA_MODIFICACION"));
                vehiculo.setAdquisicion(rs.getString("ADQUISICION"));
				vehiculo.setFechaAdquisicion(rs.getTimestamp("FECHA_ADQUISICION"));
				vehiculo.setNombre(rs.getString("NOMBRE"));
                vehiculo.setUso(rs.getString("USO"));
                vehiculo.setTipo(rs.getString("TIPO_BIEN"));
                vehiculo.setOrganizacion(rs.getString("ORGANIZACION"));
                vehiculo.setDescripcion(rs.getString("DESCRIPCION"));
                vehiculo.setRevisionActual(rs.getLong("REVISION_ACTUAL"));
                vehiculo.setNumBastidor(rs.getString("NUM_BASTIDOR"));
                vehiculo.setDestino(rs.getString("DESTINO"));
                vehiculo.setTipoVehiculo(rs.getString("TIPO_VEHICULO"));
                vehiculo.setTraccion(rs.getString("TRACCION"));
                vehiculo.setEstadoConservacion(rs.getString("ESTADO_CONSERVACION"));
                vehiculo.setPropiedad(rs.getString("PROPIEDAD"));
                vehiculo.setPatrimonioMunicipalSuelo(rs.getString("PATRIMONIO_MUNICIPAL_SUELO"));
                vehiculo.setMatriculaVieja(rs.getString("MATRICULA_VIEJA"));
                vehiculo.setMatriculaNueva(rs.getString("MATRICULA_NUEVA"));
                vehiculo.setMarca(rs.getString("MARCA"));
                vehiculo.setMotor(rs.getString("MOTOR"));
                vehiculo.setFuerza(rs.getString("FUERZA"));
                vehiculo.setServicio(rs.getString("SERVICIO"));
                vehiculo.setCosteAdquisicion(rs.getDouble("COSTE_ADQUISICION"));
                vehiculo.setValorActual(rs.getDouble("VALOR_ACTUAL"));
                vehiculo.setFrutos(rs.getString("FRUTOS"));
                vehiculo.setImporteFrutos(rs.getDouble("IMPORTE_FRUTOS"));
                vehiculo.setRevisionExpirada(rs.getLong("REVISION_EXPIRADA"));
                vehiculo.setAutor(rs.getString("NAME"));
                vehiculo.setFechaVersion(rs.getTimestamp("FECHA"));
                vehiculo.setSeguro(getSeguro(rs.getObject("ID_SEGURO")));
                
                if (new Long(vehiculo.getId()) == anteriorId){
                	if(revisionAnterior != vehiculo.getRevisionActual() && !soloMostrarVersionado){
                		vehiculo.setVersionado(true);
	                    alRet.put(vehiculo.getId()+"_"+vehiculo.getRevisionActual(), vehiculo);
	                    revisionAnterior = vehiculo.getRevisionActual();
                	}
                }else{
                	if(idFeatures.length>0){
	                	//filtra las versiones si se selecciona una version anterior.
	                	if(rs.getString("feature_revision_actual")!=null && vehiculo.getRevisionActual()<=rs.getLong("feature_revision_actual") ){
	                		
	                		vehiculo.setVersionado(false);
	                		alRet.put(vehiculo.getId()+"_"+vehiculo.getRevisionActual(), vehiculo);
	                		anteriorId = new Long(vehiculo.getId());
	                		revisionAnterior = vehiculo.getRevisionActual();  
	                		if(rs.getLong("feature_revision_actual")==rs.getLong("revision"))
	                			soloMostrarVersionado=true;
	                	}                    
		            }else{
		                	vehiculo.setVersionado(false);
	                		alRet.put(vehiculo.getId()+"_"+vehiculo.getRevisionActual(), vehiculo);
	                		anteriorId = new Long(vehiculo.getId());
	                		revisionAnterior = vehiculo.getRevisionActual();  
		            }
                	
                }
				
            	vehiculo.setIdFeatures(getFeaturesInventario(vehiculo.getId(), conn, rs.getString("REVISION_ACTUAL")));
                vehiculo.setIdLayers(getLayersInventario(vehiculo.getId(), conn,rs.getString("REVISION_ACTUAL")));
                if (withAllData){
                    
                	vehiculo.setObservaciones(getObservaciones(vehiculo.getId(),TABLA_OBSERVACION_INVENTARIO,conn,rs.getLong("REVISION_ACTUAL")));
                	vehiculo.setCuentaContable(getCuentaContable(rs.getInt("ID_CUENTA_CONTABLE")));
                	vehiculo.setCuentaAmortizacion(getCuentaAmortizacion(rs.getInt("ID_CUENTA_AMORTIZACION")));
                	String[] filtroBien ={" and bi.id='"+vehiculo.getId()+"' "};
                	vehiculo.setBienesRevertibles(getBienesRevertibles(null, null, filtroBien,null, userSesion,false,null));
                }
                if (numInventarios==null)
                	vNumInventarios.add(vehiculo.getNumInventario());
                else
                	vehiculo.setVersionado(true);

            }
        }catch(Exception ex){
        	logger.error("Error al ejecutar: "+sSQL);
        	throw ex;
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
        }

        //Añadimos los versionados al array Total y cambiamos la informacion de versionado
        if ((vNumInventarios.size()>0) && (!configParameters.isBusquedaIndividual())){
        	Collection elementosVersionados=getVehiculos(superpatron, patron, cadena,filtro, idLayers, idFeatures,vNumInventarios.toArray(),
        							userSesion,withAllData,configParameters);
        	Iterator it=elementosVersionados.iterator();
        	while (it.hasNext()){
        		VehiculoBean vehiculoBean=(VehiculoBean)it.next();
        		//String clave=vehiculo.getId()+"_"+vehiculo.getRevisionActual();
        		alRet.put(vehiculoBean,vehiculoBean);	
        	}        	
        }
     		    
        return alRet.values();
    }

    private void insertVehiculo(Connection conn, VehiculoBean vehiculo, String revision, Sesion userSesion) throws PermissionException,LockException,Exception{

        String sSQL= "INSERT INTO VEHICULO (ID, ADQUISICION, FECHA_ADQUISICION, " +
        "DESTINO, MATRICULA_VIEJA, MATRICULA_NUEVA, NUM_BASTIDOR, MARCA, MOTOR, PATRIMONIO_MUNICIPAL_SUELO, " +
        "FUERZA, SERVICIO, TIPO, ESTADO_CONSERVACION, TRACCION, PROPIEDAD, COSTE_ADQUISICION, VALOR_ACTUAL, " +
        "FRUTOS, IMPORTE_FRUTOS, REVISION_ACTUAL) " +
        "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"+revision+")";

        PreparedStatement ps= null;

        try{
			if (vehiculo == null) return;
	
	        ps= conn.prepareStatement(sSQL);
	        ps.setLong(1, vehiculo.getId());
	        ps.setString(2, vehiculo.getAdquisicion());
	        if (vehiculo.getFechaAdquisicion() == null)
	            ps.setNull(3, java.sql.Types.TIMESTAMP);
	        else ps.setTimestamp(3, new Timestamp(vehiculo.getFechaAdquisicion().getTime()));
	        ps.setString(4, vehiculo.getDestino());
	        ps.setString(5, vehiculo.getMatriculaVieja());
	        ps.setString(6, vehiculo.getMatriculaNueva());
	        ps.setString(7, vehiculo.getNumBastidor());
	        ps.setString(8, vehiculo.getMarca());
	        ps.setString(9, vehiculo.getMotor());
	        ps.setString(10, vehiculo.getPatrimonioMunicipalSuelo()?"1":"0");
	        ps.setString(11, vehiculo.getFuerza());
	        ps.setString(12, vehiculo.getServicio());
	        ps.setString(13, vehiculo.getTipoVehiculo());
	        ps.setString(14, vehiculo.getEstadoConservacion());
	        ps.setString(15, vehiculo.getTraccion());
	        ps.setString(16, vehiculo.getPropiedad());
	         if (vehiculo.getCosteAdquisicion()==null)
	        	ps.setNull(17,java.sql.Types.DOUBLE );
	        else
	        	ps.setDouble(17, vehiculo.getCosteAdquisicion());
	   
	        if (vehiculo.getValorActual()==null)
	        	ps.setNull(18,java.sql.Types.DOUBLE );
	        else
	        	ps.setDouble(18, vehiculo.getValorActual());
	   
	        ps.setString(19, vehiculo.getFrutos());
	       
	        if (vehiculo.getImporteFrutos()==null)
	        	ps.setNull(20,java.sql.Types.DOUBLE );
	        else
	        	ps.setDouble(20, vehiculo.getImporteFrutos());
	   
	        ps.execute();
	        ps.close();
	
	        insertVersion(conn, "vehiculo", userSesion);
        }catch(Exception e){
        	throw e;
        }finally{
        	try{ps.close();}catch(Exception e){};
        }
    }

    private VehiculoBean insertVehiculo(Object[] idLayers, Object[] idFeatures, VehiculoBean vehiculo, Sesion userSesion) throws PermissionException,LockException,Exception{
        Connection conn=null;
        PreparedStatement ps= null;
        ResultSet rs= null;
		try {

			if (vehiculo == null) return null;
            conn= CPoolDatabase.getConnection();
            conn.setAutoCommit(false);

            vehiculo.setIdFeatures(idFeatures);
            vehiculo.setIdLayers(idLayers);
            
            insertarBienInventario(vehiculo, userSesion, conn, sMaxRevision.replaceAll("\\?", "vehiculo"),sRevisionActual.replaceAll("\\?", "vehiculo"));

            insertVehiculo(conn, vehiculo, sRevisionActual.replaceAll("\\?", "vehiculo"), userSesion);

            /** Insertamos las features asociadas al bien */
//            insertInventarioFeature(vehiculo.getId(), idLayers, idFeatures, sRevisionActual.replaceAll("\\?", "vehiculo"),userSesion, conn);


            /** Actualizamos los ficheros en disco  (temporal --> destino) */
            DocumentoEnDisco.actualizarConFicherosDeTemporal(((BienBean)vehiculo).getDocumentos());
            vehiculo.setDocumentos(null);
            conn.commit();

        }catch (PermissionException e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }catch (LockException e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }catch (Exception e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }/** java.lang.OutOfMemoryError */catch (java.lang.Error e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){}
        }

        return vehiculo;
    }

    private VehiculoBean updateVehiculo(VehiculoBean vehiculo, Sesion userSesion) throws PermissionException,LockException,Exception{

        Connection conn=null;
        PreparedStatement ps= null;
        ResultSet rs= null;
		try {

			if (vehiculo == null) return null;

            conn= CPoolDatabase.getConnection();
            conn.setAutoCommit(false);

            updateBienInventario(vehiculo, userSesion, conn, sMaxRevision.replaceAll("\\?", "vehiculo"), sRevisionActual.replaceAll("\\?", "vehiculo"));
            
            String sSQL= "UPDATE VEHICULO SET REVISION_EXPIRADA="+sRevisionActual.replaceAll("\\?", "vehiculo")+
            " WHERE ID=? "+sRevisionExpiradaNula;

            ps= conn.prepareStatement(sSQL);
            ps.setLong(1, vehiculo.getId());
            ps.execute();
            ps.close();

            this.insertVehiculo(conn, vehiculo, sRevisionActual.replaceAll("\\?", "vehiculo"), userSesion);

            /** Actualizamos los ficheros en disco  (temporal --> destino) */
            DocumentoEnDisco.actualizarConFicherosDeTemporal(((BienBean)vehiculo).getDocumentos());
            vehiculo.setDocumentos(null);
            conn.commit();

            /** Borramos de disco los ficheros del bien que no esten asociados a otro */
            DocumentConnection docConn= new DocumentConnection(srid);
            docConn.borrarFicherosEnDisco(vehiculo.getId(), docsBorrados);
      

        }catch (Exception e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }/** java.lang.OutOfMemoryError */catch (java.lang.Error e){
            try{conn.rollback();}catch(Exception ex){};
            throw e;
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){}
        }

        return vehiculo;
    }

    private void deleteVehiculo(VehiculoBean vehiculo, Connection conn) throws Exception{
        String sSQL= "UPDATE vehiculo SET REVISION_EXPIRADA="+sRevisionActual.replaceAll("\\?", "vehiculo");
        sSQL +=	" WHERE ID=? "+sRevisionExpiradaNula;

        PreparedStatement ps= null;
        try{
            ps= conn.prepareStatement(sSQL);
            ps.setLong(1, vehiculo.getId());
            ps.execute();

        }catch (Exception e){
            throw e;
        }finally{
            try{ps.close();}catch(Exception e){};
        }

    }

    private void gestionarCuentaContable(BienBean bien, Connection conn) throws Exception{
        if (bien.getCuentaContable() != null){
            bien.setCuentaContable(insertarCuentaContable(bien.getCuentaContable(), conn));
        }else{
            Object cuenta= getCuentaFromBien(bien, CONTABLE, conn);
            if (cuenta !=null){
                if (!cuentaAsociadaOtroBien(bien, cuenta, conn))
                    deleteCuenta(cuenta, conn);
            }
        }
    }

    private void gestionarCuentaAmortizacion(BienBean bien, Connection conn) throws Exception{
        if (bien.getCuentaAmortizacion()!=null){
            bien.setCuentaAmortizacion(actualizarCuentaAmortizacion(bien.getCuentaAmortizacion(), conn));
        }else{
            Object cuenta= getCuentaFromBien(bien, AMORTIZACION, conn);
            if (cuenta!=null){
                if (!cuentaAsociadaOtroBien(bien, cuenta, conn))
                    deleteCuenta(cuenta, conn);
            }
        }

    }

    private void gestionarSeguro(BienBean bien, Connection conn) throws Exception{
        if (bien.getSeguro()!=null){
            if (bien.getSeguro().getId()==null||bien.getSeguro().getId()==-1) bien.setSeguro(insertarSeguro(bien.getSeguro(), conn));
            else updateSeguro(bien.getSeguro(), conn);
        }else{
            /** borrar seguro */
            deleteSeguro(getIdSeguroFromBien(bien, conn), conn);
        }
    }


    private String getSelectBusquedaInmueble(String superpatron, String patron, String cadena, Object[] idLayers, Object[] idFeatures, Sesion userSesion) throws Exception{
        if (cadena==null || cadena.trim().equalsIgnoreCase("")) return "";

        String sql= addSQLInmueble(superpatron, patron, cadena, idLayers, idFeatures, userSesion);
        sql+= " UNION ";
        sql+= addSQLSegurosInventario(SQL_INMUEBLE, superpatron, patron, cadena, idLayers, idFeatures, userSesion);
        sql+= " UNION ";
        sql+= addSQLContable(SQL_INMUEBLE, superpatron, patron, cadena, idLayers, idFeatures, userSesion);
        sql+= " UNION ";
        sql+= addSQLAmortizacion(SQL_INMUEBLE, superpatron, patron, cadena, idLayers, idFeatures, userSesion);
        sql+= " UNION ";
        sql+= addSQLMejoras(SQL_INMUEBLE, superpatron, patron, cadena, idLayers, idFeatures, userSesion);
        //sql+= " UNION ";
        //sql+= addSQLUsosFuncionales(SQL_INMUEBLE, superpatron, patron, cadena, idLayers, idFeatures, userSesion);
        sql+= " UNION ";
        sql+= addSQLObservaciones(SQL_INMUEBLE, superpatron, patron, cadena, idLayers, idFeatures, userSesion);
        sql+= " UNION ";
        sql+= addSQLRefCatastrales(SQL_INMUEBLE, superpatron, patron, cadena, idLayers, idFeatures, userSesion);
        sql+= " UNION ";
        sql+= addSQLDocumentacion(SQL_INMUEBLE, superpatron, patron, cadena, idLayers, idFeatures, userSesion);

        return sql;
    }


    private String getSelectInmueble(String superpatron, String patron, Object[] filtro, Object[] idLayers, Object[] idFeatures, Sesion userSesion, String addToFrom) throws Exception {

    	String sSQL="";

    	sSQL= "select bienes_inventario.numinventario, bienes_inventario.nombre, bienes_inventario.descripcion, " +
    	"bienes_inventario.uso, bienes_inventario.tipo,bienes_inventario.fecha_alta, bienes_inventario.fecha_aprobacion_pleno, " +
    	"bienes_inventario.fecha_ultima_modificacion, " +
    	"bienes_inventario.borrado, bienes_inventario.tipo,bienes_inventario.organizacion, bienes_inventario.borrado, bienes_inventario.bloqueado," +
    	"bienes_inventario.fecha_baja,bienes_inventario.id_municipio,bienes_inventario.id_seguro,  inmuebles.* "+
    	",versiones.*,iuseruserhdr.name,tables_inventario.name ";
    	if (patron.equalsIgnoreCase(Const.PATRON_INMUEBLES_URBANOS)){
    		sSQL += ", inmuebles_urbanos.manzana, inmuebles_urbanos.ANCHOSUPERF,inmuebles_urbanos.LONGSUPERF, inmuebles_urbanos.METRPAVSUPERF, inmuebles_urbanos.METRNOPAVSUPERF ";
    	}
    	else if (patron.equalsIgnoreCase(Const.PATRON_INMUEBLES_RUSTICOS)){
    		sSQL += ", inmuebles_rusticos.aprovechamiento, inmuebles_rusticos.paraje, inmuebles_rusticos.poligono, inmuebles_rusticos.ANCHOSUPERF,inmuebles_rusticos.LONGSUPERF, inmuebles_rusticos.METRPAVSUPERF, inmuebles_rusticos.METRNOPAVSUPERF ";
    	}
    	
    	if (idFeatures.length > 0)
    		sSQL+= ", inventario_feature.revision_actual AS feature_revision_actual ";
    	
    	
    	sSQL += " from " + addToFrom + "inmuebles inner join bienes_inventario " +
    	"on bienes_inventario.id=inmuebles.id and bienes_inventario.revision_actual=inmuebles.revision_actual ";
		sSQL+= " inner join versiones on versiones.revision=inmuebles.revision_actual ";
		sSQL+= " inner join iuseruserhdr on versiones.id_autor=iuseruserhdr.id ";
		sSQL+= " inner join tables_inventario on versiones.id_table_versionada=tables_inventario.id_table ";
		//sSQL+= " inner join (SELECT ID, CASE WHEN MAX(REVISION_EXPIRADA) = 9999999999 THEN TRUE ELSE FALSE END AS ACTIVO FROM BIENES_INVENTARIO GROUP BY ID) tiene_ultima_revision on tiene_ultima_revision.id = bienes_inventario.id ";

    	if (patron.equalsIgnoreCase(Const.PATRON_INMUEBLES_URBANOS)){
    		sSQL+= "inner join inmuebles_urbanos on inmuebles.id=inmuebles_urbanos.id  and inmuebles.revision_actual=inmuebles_urbanos.revision_actual ";
    	}
    	else if (patron.equalsIgnoreCase(Const.PATRON_INMUEBLES_RUSTICOS)){
    		sSQL+= "inner join inmuebles_rusticos on inmuebles.id=inmuebles_rusticos.id and inmuebles.revision_actual=inmuebles_rusticos.revision_actual ";
    	}

    	if (idFeatures.length > 0){
    		sSQL+= " inner join inventario_feature on bienes_inventario.id=inventario_feature.id_bien ";
    	}
    	
    	for(int i=0; i<idFeatures.length; i++){
    		
    		if (i==0) 
    			sSQL+= "AND inventario_feature.id_feature="+(String)idFeatures[i]+" AND " +
    			"inventario_feature.id_layer="+getIdLayer((String)idLayers[i]);
    		else 
    			sSQL+=" OR (inventario_feature.id_bien=bienes_inventario.id AND " +
    			"inventario_feature.id_feature="+(String)idFeatures[i]+" AND " +
    			"inventario_feature.id_layer="+getIdLayer((String)idLayers[i])+") ";
    	}

    	sSQL+=" WHERE bienes_inventario.id_municipio='"+municipio+"' AND bienes_inventario.tipo='"+patron+"' ";
    	/** en funcion del superpatron */
    	if (superpatron!=null){
	    	if (superpatron.equalsIgnoreCase(Const.SUPERPATRON_REVERTIBLES))
	    		sSQL+="AND inmuebles.propiedad='"+Const.PATRON_CEDIDO+"' ";
	    	else if (superpatron.equalsIgnoreCase(Const.SUPERPATRON_PATRIMONIO_MUNICIPAL_SUELO))
	    		sSQL+="AND inmuebles.patrimonio_municipal_suelo='1' ";
    	}
    	sSQL += sSQLVersionado.replaceAll("\\?", "inmuebles");
    	sSQL += " AND tables_inventario.name='inmuebles'";
    	if ((filtro==null) || (filtro.length==0)) return sSQL;

    	return addFiltro(sSQL, patron.equalsIgnoreCase(Const.PATRON_INMUEBLES_URBANOS)?SQL_INMUEBLE_URBANO:SQL_INMUEBLE_RUSTICO, superpatron, patron, filtro, idLayers, idFeatures, userSesion);

    }

    private String getSelectInmueble(long idInmueble, String patron, Sesion userSesion) throws Exception {

    	String sSQL="";

    	sSQL= "select bienes_inventario.numinventario, bienes_inventario.nombre, bienes_inventario.descripcion, " +
    	"bienes_inventario.uso, bienes_inventario.tipo, bienes_inventario.fecha_alta, bienes_inventario.fecha_aprobacion_pleno, " +
    	"bienes_inventario.fecha_baja, bienes_inventario.fecha_ultima_modificacion, " +
    	"bienes_inventario.id_municipio, bienes_inventario.borrado, bienes_inventario.bloqueado, " +
    	"bienes_inventario.organizacion, bienes_inventario.id_cuenta_contable, " +
    	"bienes_inventario.id_cuenta_amortizacion, bienes_inventario.id_seguro, inmuebles.*, ";
    	
    	if (patron.equalsIgnoreCase(Const.PATRON_INMUEBLES_URBANOS)){
    		sSQL += "inmuebles_urbanos.manzana, inmuebles_urbanos.ANCHOSUPERF,inmuebles_urbanos.LONGSUPERF, inmuebles_urbanos.METRPAVSUPERF, inmuebles_urbanos.METRNOPAVSUPERF ";
    	}
    	else if (patron.equalsIgnoreCase(Const.PATRON_INMUEBLES_RUSTICOS)){
    		sSQL += "inmuebles_rusticos.aprovechamiento, inmuebles_rusticos.paraje, inmuebles_rusticos.poligono, inmuebles_rusticos.ANCHOSUPERF,inmuebles_rusticos.LONGSUPERF, inmuebles_rusticos.METRPAVSUPERF, inmuebles_rusticos.METRNOPAVSUPERF ";
    	}
    	
    	sSQL += "from inmuebles inner join bienes_inventario " +
    	"on bienes_inventario.id=inmuebles.id and bienes_inventario.revision_actual=inmuebles.revision_actual ";
    	
    	if (patron.equalsIgnoreCase(Const.PATRON_INMUEBLES_URBANOS)){
    		sSQL+= "inner join inmuebles_urbanos on inmuebles.id=inmuebles_urbanos.id ";
    	}
    	else if (patron.equalsIgnoreCase(Const.PATRON_INMUEBLES_RUSTICOS)){
    		sSQL+= "inner join inmuebles_rusticos on inmuebles.id=inmuebles_rusticos.id ";
    	}
    	
    	sSQL+=" WHERE bienes_inventario.id_municipio='"+municipio+"' AND bienes_inventario.tipo='"+patron+"' ";
    	
    	sSQL += "and inmuebles.id=" + idInmueble;

    	sSQL += sSQLVersionado.replaceAll("\\?", "inmuebles");
    	
    	return sSQL;
    }
    
    private String getSelectCreditoDerecho(long idInmueble, String patron, Sesion userSesion) throws Exception {

    	String sSQL="";

    	sSQL= "select bienes_inventario.numinventario, bienes_inventario.nombre, bienes_inventario.descripcion as " +
    			"BIEN_DESCRIPCION, bienes_inventario.uso, bienes_inventario.tipo, bienes_inventario.fecha_alta, " +
    			"bienes_inventario.fecha_baja, bienes_inventario.fecha_ultima_modificacion, bienes_inventario.id_municipio, " +
    			"bienes_inventario.borrado, bienes_inventario.organizacion, bienes_inventario.id_cuenta_contable, " +
    			"bienes_inventario.id_cuenta_amortizacion, bienes_inventario.id_seguro, bienes_inventario.bloqueado, " +
    			"credito_derecho.* from credito_derecho inner join bienes_inventario on bienes_inventario.id=credito_derecho.id "+
    			"and bienes_inventario.revision_actual=credito_derecho.revision_actual ";
    	
    	sSQL+=" WHERE bienes_inventario.id_municipio='"+municipio+"' AND bienes_inventario.tipo='"+patron+"' ";
    	
    	sSQL += "and credito_derecho.id=" + idInmueble;
    	sSQL += sSQLVersionado.replaceAll("\\?", "credito_derecho");

    	return sSQL;
    }
    
    private String getSelectDerechoReal(long idInmueble, String patron, Sesion userSesion) throws Exception {

    	String sSQL="";

    	sSQL= "select bienes_inventario.id as ID_BIEN, bienes_inventario.numinventario, bienes_inventario.nombre, " +
    			"bienes_inventario.descripcion, bienes_inventario.uso, bienes_inventario.tipo, bienes_inventario.fecha_alta, " +
    			"bienes_inventario.fecha_baja, bienes_inventario.fecha_ultima_modificacion, bienes_inventario.id_municipio, " +
    			"bienes_inventario.borrado, bienes_inventario.organizacion, bienes_inventario.id_cuenta_contable, " +
    			"bienes_inventario.id_cuenta_amortizacion, bienes_inventario.id_seguro, bienes_inventario.bloqueado, " +
    			"derechos_reales.* from derechos_reales inner join bienes_inventario on" +
    			" bienes_inventario.id=derechos_reales.id and bienes_inventario.revision_actual=derechos_reales.revision_actual ";
    	
    	sSQL+=" WHERE bienes_inventario.id_municipio='"+municipio+"' AND bienes_inventario.tipo='"+patron+"' ";
    	
    	sSQL += "and derechos_reales.id=" + idInmueble;
    	sSQL += sSQLVersionado.replaceAll("\\?", "derecho_reales");
    	
    	return sSQL;
    }

    private String getSelectMueble(long idInmueble, String patron, Sesion userSesion) throws Exception {

    	String sSQL="";

    	sSQL= "select bienes_inventario.numinventario, bienes_inventario.nombre, bienes_inventario.descripcion, " +
    			"bienes_inventario.uso, bienes_inventario.tipo, bienes_inventario.fecha_alta, bienes_inventario.fecha_baja, " +
    			"bienes_inventario.fecha_ultima_modificacion, bienes_inventario.id_municipio, bienes_inventario.borrado, " +
    			"bienes_inventario.organizacion, bienes_inventario.id_cuenta_contable, " +
    			"bienes_inventario.id_cuenta_amortizacion, bienes_inventario.id_seguro, bienes_inventario.bloqueado, " +
    			"muebles.* from muebles inner join bienes_inventario on " +
    			"bienes_inventario.id=muebles.id and bienes_inventario.revision_actual=muebles.revision_actual  ";
    	
    	sSQL+=" WHERE bienes_inventario.id_municipio='"+municipio+"' AND bienes_inventario.tipo='"+patron+"' ";
    	
    	sSQL += "and muebles.id=" + idInmueble;
    	sSQL += sSQLVersionado.replaceAll("\\?", "muebles");

    	return sSQL;
    }
    
    private String getSelectSemoviente(long idInmueble, String patron, Sesion userSesion) throws Exception {

    	String sSQL="";

    	sSQL= "select bienes_inventario.numinventario, bienes_inventario.nombre, " +
    			"bienes_inventario.descripcion as BIEN_DESCRIPCION, bienes_inventario.uso, bienes_inventario.tipo, " +
    			"bienes_inventario.fecha_alta, bienes_inventario.fecha_baja, bienes_inventario.fecha_ultima_modificacion, " +
    			"bienes_inventario.id_municipio, bienes_inventario.borrado, bienes_inventario.organizacion, " +
    			"bienes_inventario.id_cuenta_contable, bienes_inventario.id_cuenta_amortizacion, bienes_inventario.id_seguro, " +
    			"bienes_inventario.bloqueado, semoviente.* " +
    			"from semoviente inner join bienes_inventario on " +
    			"bienes_inventario.id=semoviente.id and bienes_inventario.revision_actual=semoviente.revision_actual ";
    	    	
    	sSQL+=" WHERE bienes_inventario.id_municipio='"+municipio+"' AND bienes_inventario.tipo='"+patron+"' ";
    	
    	sSQL += "and semoviente.id=" + idInmueble;
    	sSQL += sSQLVersionado.replaceAll("\\?", "semoviente");

    	return sSQL;
    }
    
    private String getSelectValorMobiliario(long idInmueble, String patron, Sesion userSesion) throws Exception {

    	String sSQL="";

    	sSQL= "select bienes_inventario.numinventario, bienes_inventario.nombre, bienes_inventario.descripcion, " +
    			"bienes_inventario.uso, bienes_inventario.tipo, bienes_inventario.fecha_alta, bienes_inventario.fecha_baja, " +
    			"bienes_inventario.fecha_ultima_modificacion, bienes_inventario.id_municipio, bienes_inventario.borrado, " +
    			"bienes_inventario.organizacion, bienes_inventario.id_cuenta_contable, " +
    			"bienes_inventario.id_cuenta_amortizacion, bienes_inventario.id_seguro, bienes_inventario.bloqueado, " +
    			"valor_mobiliario.* from valor_mobiliario inner join bienes_inventario on " +
    			"bienes_inventario.id=valor_mobiliario.id and bienes_inventario.revision_actual=valor_mobiliario.revision_actual ";
    	    	    	
    	sSQL+=" WHERE bienes_inventario.id_municipio='"+municipio+"' AND bienes_inventario.tipo='"+patron+"' ";
    	
    	sSQL += "and valor_mobiliario.id=" + idInmueble;
    	sSQL += sSQLVersionado.replaceAll("\\?", "valor_mobiliario");

    	return sSQL;
    }
    
    private String getSelectVehiculo(long idInmueble, String patron, Sesion userSesion) throws Exception {

    	String sSQL="";

    	sSQL= "select bienes_inventario.numinventario, bienes_inventario.nombre, bienes_inventario.descripcion, " +
    			"bienes_inventario.uso, bienes_inventario.tipo as BIEN_TIPO, bienes_inventario.fecha_alta, " +
    			"bienes_inventario.fecha_baja, bienes_inventario.fecha_ultima_modificacion, bienes_inventario.id_municipio, " +
    			"bienes_inventario.borrado, bienes_inventario.organizacion, bienes_inventario.id_cuenta_contable, " +
    			"bienes_inventario.id_cuenta_amortizacion, bienes_inventario.id_seguro, bienes_inventario.bloqueado, " +
    			"vehiculo.* from vehiculo inner join bienes_inventario on bienes_inventario.id=vehiculo.id " +
    			" and bienes_inventario.revision_actual=vehiculo.revision_actual ";
    	
    	sSQL+=" WHERE bienes_inventario.id_municipio='"+municipio+"' AND bienes_inventario.tipo='"+patron+"' ";
    	
    	sSQL += "and vehiculo.id=" + idInmueble;
    	sSQL += sSQLVersionado.replaceAll("\\?", "vehiculo");;

    	return sSQL;
    }
    
    private String getSelectVias(long idInmueble, String patron, Sesion userSesion) throws Exception {

    	String sSQL="";

    	sSQL= "select bienes_inventario.numinventario, bienes_inventario.nombre as BIEN_NOMBRE, bienes_inventario.descripcion, " +
    			"bienes_inventario.uso, bienes_inventario.tipo, bienes_inventario.fecha_alta, bienes_inventario.fecha_baja, " +
    			"bienes_inventario.fecha_ultima_modificacion, bienes_inventario.id_municipio, bienes_inventario.borrado, " +
    			"bienes_inventario.organizacion, bienes_inventario.id_cuenta_contable, " +
    			"bienes_inventario.id_cuenta_amortizacion, bienes_inventario.id_seguro, bienes_inventario.bloqueado, " +
    			"vias_inventario.* from vias_inventario inner join bienes_inventario on " +
    			"bienes_inventario.id=vias_inventario.id and bienes_inventario.revision_actual=vias_inventario.revision_actual ";
    	
    	sSQL+=" WHERE bienes_inventario.id_municipio='"+municipio+"' AND bienes_inventario.tipo='"+patron+"' ";
    	
    	sSQL += "and vias_inventario.id=" + idInmueble;
    	sSQL += sSQLVersionado.replaceAll("\\?", "vias_inventario");;

    	return sSQL;
    }

    private String addSQLInmueble(String superpatron, String patron, String cadena, Object[] idLayers, Object[] idFeatures, Sesion userSesion) throws Exception{
        String sql= getSelectInmueble(superpatron, patron, null, idLayers, idFeatures, userSesion, "") + " AND ";
        sql+= "(UPPER(bienes_inventario.numinventario) like UPPER('%"+cadena+"%') OR " +
            //  "UPPER(bienes_inventario.fecha_alta) like UPPER('%"+cadena+"%') OR " +
            //  "UPPER(bienes_inventario.fecha_baja) like UPPER('%"+cadena+"%') OR " +
            //  "UPPER(bienes_inventario.fecha_ultima_modificacion) like UPPER('%"+cadena+"%') OR "+
              "UPPER(bienes_inventario.descripcion) like UPPER('%"+cadena+"%') OR "+
              "UPPER(bienes_inventario.nombre) like UPPER('%"+cadena+"%') OR "+
              "UPPER(bienes_inventario.organizacion) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(inmuebles.direccion) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(inmuebles.lindero_norte) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(inmuebles.lindero_sur) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(inmuebles.lindero_este) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(inmuebles.lindero_oeste) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(inmuebles.registro_folio) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(inmuebles.registro_libro) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(inmuebles.registro_inscripcion) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(inmuebles.registro_protocolo) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(inmuebles.registro_notario) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(inmuebles.registro) like UPPER('%"+cadena+"%') OR ";
       // sql+= "UPPER(inmuebles.superficie_registral_suelo) like UPPER('%"+cadena+"%') OR ";
        //sql+= "UPPER(inmuebles.superficie_catastral_suelo) like UPPER('%"+cadena+"%') OR ";
       // sql+= "UPPER(inmuebles.superficie_real_suelo) like UPPER('%"+cadena+"%') OR ";
       // sql+= "UPPER(inmuebles.superficie_registral_construccion) like UPPER('%"+cadena+"%') OR ";
       // sql+= "UPPER(inmuebles.superficie_catastral_construccion) like UPPER('%"+cadena+"%') OR ";
     //   sql+= "UPPER(inmuebles.superficie_real_construccion) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(inmuebles.derechosrealesfavor) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(inmuebles.derechosrealescontra) like UPPER('%"+cadena+"%') OR ";
        //sql+= "UPPER(inmuebles.valor_derechos_favor) like UPPER('%"+cadena+"%') OR ";
        //sql+= "UPPER(inmuebles.valor_derechos_contra) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(inmuebles.derechospersonales) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(inmuebles.refpla) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(inmuebles.refcat) like UPPER('%"+cadena+"%') OR ";
       // sql+= "UPPER(inmuebles.fechaobra) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(inmuebles.destino) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(inmuebles.ref_catastral) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(inmuebles.registro_propiedad) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(inmuebles.num_plantas) like UPPER('%"+cadena+"%') OR ";
      //  sql+= "UPPER(inmuebles.fecha_adquisicion) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(inmuebles.registro_tomo) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(inmuebles.registro_finca) like UPPER('%"+cadena+"%') OR ";
      //  sql+= "UPPER(inmuebles.edificabilidad) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(inmuebles.fachada_desc) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(inmuebles.cubierta_desc) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(inmuebles.carpinteria_desc) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(inmuebles.tipoconstruccion_desc) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(inmuebles.estadoconservacion_desc) like UPPER('%"+cadena+"%') OR ";
        //sql+= "UPPER(inmuebles.fecha_adquisicion_suelo) like UPPER('%"+cadena+"%') OR ";
        //sql+= "UPPER(inmuebles.superficie_ocupada_construccion) like UPPER('%"+cadena+"%') OR ";
       // sql+= "UPPER(inmuebles.superficie_construida_construccion) like UPPER('%"+cadena+"%') OR ";
       // sql+= "UPPER(inmuebles.superficie_enplanta_construccion) like UPPER('%"+cadena+"%') OR ";
       // sql+= "UPPER(inmuebles.valor_adquisicion_suelo) like UPPER('%"+cadena+"%') OR ";
      //  sql+= "UPPER(inmuebles.valor_catastral_suelo) like UPPER('%"+cadena+"%') OR ";
      //  sql+= "UPPER(inmuebles.valor_actual_suelo) like UPPER('%"+cadena+"%') OR ";
      //  sql+= "UPPER(inmuebles.valor_adquisicion_construccion) like UPPER('%"+cadena+"%') OR ";
       // sql+= "UPPER(inmuebles.valor_catastral_construccion) like UPPER('%"+cadena+"%') OR ";
       // sql+= "UPPER(inmuebles.valor_actual_construccion) like UPPER('%"+cadena+"%') OR ";
       // sql+= "UPPER(inmuebles.valor_adquisicion_inmueble) like UPPER('%"+cadena+"%') OR ";
      //  sql+= "UPPER(inmuebles.valor_actual_inmueble) like UPPER('%"+cadena+"%') OR ";
       sql+= "UPPER(inmuebles.frutos) like UPPER('%"+cadena+"%') OR ";
      //  sql+= "UPPER(inmuebles.importe_frutos) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(inmuebles.numero_orden) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(inmuebles.numero_propiedad) like UPPER('%"+cadena+"%')";
        if (patron.equalsIgnoreCase(Const.PATRON_INMUEBLES_URBANOS)){
            sql+= " OR UPPER(inmuebles_urbanos.parcela) like UPPER('%"+cadena+"%') OR ";
            sql+= "UPPER(inmuebles_urbanos.manzana) like UPPER('%"+cadena+"%')";
        }else if (patron.equalsIgnoreCase(Const.PATRON_INMUEBLES_RUSTICOS)){
            sql+= " OR UPPER(inmuebles_rusticos.poligono) like UPPER('%"+cadena+"%') OR ";
            sql+= "UPPER(inmuebles_rusticos.paraje) like UPPER('%"+cadena+"%')";
        }
        sql+=")";
        return sql;
    }

    private String addSQLSegurosInventario(int tiposql, String superpatron, String patron, String cadena, Object[] idLayers, Object[] idFeatures, Sesion userSesion) throws Exception{
        return addSQLSegurosInventario(tiposql, superpatron, patron, cadena, null, idLayers, idFeatures, userSesion);
    }

    private String addSQLSegurosInventario(int tiposql, String superpatron, String patron, Object[] filtro, Object[] idLayers, Object[] idFeatures, Sesion userSesion) throws Exception{
        return addSQLSegurosInventario(tiposql, superpatron, patron, null, filtro, idLayers, idFeatures, userSesion);
    }

    private String addSQLSegurosInventario(int tiposql, String superpatron, String patron, String cadena, Object[] filtro, Object[] idLayers, Object[] idFeatures, Sesion userSesion) throws Exception{
        String sql= "";
        boolean addFiltro= false;

        switch (tiposql){
            case SQL_INMUEBLE:
                sql= getSelectInmueble(superpatron, patron, null, idLayers, idFeatures, userSesion, " seguros_inventario , compannia_seguros, ") + " AND ";
                addFiltro= true;
                break;
            case SQL_MUEBLE:
                sql= getSelectMueble(superpatron, patron, null, idLayers, idFeatures, userSesion, " seguros_inventario , compannia_seguros, ") + " AND ";
                addFiltro= true;
                break;
            case SQL_VALOR_MOBILIARIO:
                sql= getSelectValorMobiliario(superpatron, patron, null, idLayers, idFeatures, userSesion, " seguros_inventario , compannia_seguros, ") + " AND ";
                addFiltro= true;
                break;
            case SQL_SEMOVIENTE:
                sql= getSelectSemoviente(superpatron, patron, null, idLayers, idFeatures, userSesion, " seguros_inventario , compannia_seguros, ") + " AND ";
                addFiltro= true;
                break;
            case SQL_VIAS:
                sql= getSelectVias(superpatron, patron, null, idLayers, idFeatures, userSesion, " seguros_inventario , compannia_seguros, ") + " AND ";
                addFiltro= true;
                break;
            case SQL_VEHICULOS:
                sql= getSelectVehiculo(superpatron, patron, null, idLayers, idFeatures, userSesion, " seguros_inventario , compannia_seguros, ") + " AND ";
                addFiltro= true;
                break;

        }

        if (cadena != null){
            sql+= "bienes_inventario.id_seguro=seguros_inventario.id AND seguros_inventario.id_compannia=compannia_seguros.id AND " +
                    "(UPPER(compannia_seguros.nombre) like UPPER('%"+cadena+"%') OR UPPER(compannia_seguros.descripcion) like UPPER('%"+cadena+"%') OR " +
                    "UPPER(seguros_inventario.descripcion) like UPPER('%"+cadena+"%')) " ;
                    		//"OR UPPER(seguros_inventario.prima) like UPPER('%"+cadena+"%') " +
                  //  " OR UPPER(seguros_inventario.poliza) like UPPER('%"+cadena+"%') " ;
                    		//+ "OR UPPER(seguros_inventario.fecha_inicio) like UPPER('%"+cadena+"%') "
                 //   		" OR " +
                 //   "UPPER(seguros_inventario.fecha_vencimiento) like UPPER('%"+cadena+"%'))";
        }else if ((addFiltro) && (filtro!=null)){
            sql+= "bienes_inventario.id_seguro=seguros_inventario.id AND seguros_inventario.id_compannia=compannia_seguros.id ";
            for (int i=0; i<filtro.length; i++){
                CampoFiltro cf= (CampoFiltro)filtro[i];
                sql+= addPartOfWhere(cf);
            }
        }
        return sql;
    }

    private String addSQLContable(int tiposql, String superpatron, String patron, String cadena, Object[] idLayers, 
    		Object[] idFeatures, Sesion userSesion) throws Exception{
        return addSQLContable(tiposql, superpatron, patron, cadena, null, idLayers, idFeatures, userSesion);
    }

    private String addSQLContable(int tiposql, String superpatron, String patron, Object[] filtro, Object[] idLayers, 
    		Object[] idFeatures, Sesion userSesion) throws Exception{
        return addSQLContable(tiposql, superpatron, patron, null, filtro, idLayers, idFeatures, userSesion);
    }

    private String addSQLContable(int tiposql, String superpatron, String patron, String cadena, 
    		Object[] filtro, Object[] idLayers, Object[] idFeatures, Sesion userSesion) throws Exception{
        String sql= "";
        boolean addFiltro= false;
        switch (tiposql){
            case SQL_INMUEBLE:
                sql= getSelectInmueble(superpatron, patron, null, idLayers, idFeatures, userSesion, " contable, ") + " AND ";
                addFiltro= true;
                break;
            case SQL_MUEBLE:
                sql= getSelectMueble(superpatron, patron, null, idLayers, idFeatures, userSesion, " contable, ") + " AND ";
                addFiltro= true;
                break;
            case SQL_DERECHOS_REALES:
                sql= getSelectDerechosReales(superpatron, patron, null, idLayers, idFeatures, userSesion, " contable, ") + " AND ";
                addFiltro= true;
                break;
            case SQL_CREDITO_DERECHO:
                sql= getSelectCreditosDerechos(superpatron, patron, null, idLayers, idFeatures, userSesion, " contable, ") + " AND ";
                addFiltro= true;
                break;
            case SQL_VALOR_MOBILIARIO:
                sql= getSelectValorMobiliario(superpatron, patron, null, idLayers, idFeatures, userSesion, " contable, ") + " AND ";
                addFiltro= true;
                break;
            case SQL_SEMOVIENTE:
                sql= getSelectSemoviente(superpatron, patron, null, idLayers, idFeatures, userSesion, " contable, ") + " AND ";
                addFiltro= true;
                break;
            case SQL_VIAS:
                sql= getSelectVias(superpatron, patron, null, idLayers, idFeatures, userSesion, " contable, ") + " AND ";
                addFiltro= true;
                break;
            case SQL_VEHICULOS:
                sql= getSelectVehiculo(superpatron, patron, null, idLayers, idFeatures, userSesion, " contable, ") + " AND ";
                addFiltro= true;
                break;

        }

        if (cadena != null){
            sql+= "bienes_inventario.id_cuenta_contable=contable.id_clasificacion AND " +
                    "(UPPER(contable.descripcion) like UPPER('%"+cadena+"%') OR UPPER(contable.cuenta) like UPPER('%"+cadena+"%'))";
        }else if ((addFiltro) && (filtro!=null)){
            sql+= "bienes_inventario.id_cuenta_contable=contable.id_clasificacion ";
            for (int i=0; i<filtro.length; i++){
                CampoFiltro cf= (CampoFiltro)filtro[i];
                sql+= addPartOfWhere(cf);
            }
        }

        return sql;
    }

    private String addSQLAmortizacion(int tiposql, String superpatron, String patron, String cadena, Object[] idLayers, Object[] idFeatures, Sesion userSesion) throws Exception{
        return addSQLAmortizacion(tiposql, superpatron, patron, cadena, null, idLayers, idFeatures, userSesion);
    }

    private String addSQLAmortizacion(int tiposql, String superpatron, String patron, Object[] filtro, Object[] idLayers, Object[] idFeatures, Sesion userSesion) throws Exception{
        return addSQLAmortizacion(tiposql, superpatron, patron, null, filtro, idLayers, idFeatures, userSesion);
    }

    private String addSQLAmortizacion(int tiposql, String superpatron, String patron, String cadena, 
    		Object[] filtro, Object[] idLayers, Object[] idFeatures, Sesion userSesion) throws Exception{
        String sql= "";
        boolean addFiltro= false;
        switch (tiposql){
            case SQL_INMUEBLE:
                sql= getSelectInmueble(superpatron, patron, null, idLayers, idFeatures, userSesion, " amortizacion, ") + " AND ";
                addFiltro= true;
                break;
            case SQL_MUEBLE:
                sql= getSelectMueble(superpatron, patron, null, idLayers, idFeatures, userSesion, " amortizacion, ") + " AND ";
                addFiltro= true;
                break;
            case SQL_VEHICULOS:
                sql= getSelectVehiculo(superpatron, patron, null, idLayers, idFeatures, userSesion, " amortizacion, ") + " AND ";
                addFiltro= true;
                break;

        }

        if (cadena != null){
            sql+= "bienes_inventario.id_cuenta_amortizacion=amortizacion.id AND (UPPER(amortizacion.descripcion) like UPPER('%"+cadena+"%') OR " +
              "UPPER(amortizacion.cuenta) like UPPER('%"+cadena+"%') " +
           //   		"OR UPPER(amortizacion.anios) like UPPER('%"+cadena+"%') " +
            //  "OR UPPER(amortizacion.porcentaje) like UPPER('%"+cadena+"%') " +
            //  		"OR UPPER(amortizacion.total_amortizado) like UPPER('%"+cadena+"%')" +
              				")";
        }else if ((addFiltro) && (filtro!=null)){
            sql+= "bienes_inventario.id_cuenta_amortizacion=amortizacion.id ";
            for (int i=0; i<filtro.length; i++){
                CampoFiltro cf= (CampoFiltro)filtro[i];
                sql+= addPartOfWhere(cf);
            }
        }


        return sql;
    }

    private String addSQLMejoras(int tiposql, String superpatron, String patron, String cadena, Object[] idLayers, Object[] idFeatures, Sesion userSesion) throws Exception{
        return addSQLMejoras(tiposql, superpatron, patron, cadena, null, idLayers, idFeatures, userSesion);
    }

    private String addSQLMejoras(int tiposql, String superpatron, String patron, Object[] filtro, Object[] idLayers,
    		Object[] idFeatures, Sesion userSesion) throws Exception{
        return addSQLMejoras(tiposql, superpatron, patron, null, filtro, idLayers, idFeatures, userSesion);
    }
    private String addSQLMejoras(int tiposql, String superpatron, String patron, String cadena, Object[] filtro, Object[] idLayers, Object[] idFeatures,
    		Sesion userSesion) throws Exception{
        String sql= "";
        boolean addFiltro= false;

        switch (tiposql){
            case SQL_INMUEBLE:
                sql= getSelectInmueble(superpatron, patron, null, idLayers, idFeatures, userSesion, " mejoras_inventario, ") + " AND ";
                addFiltro= true;
                break;
            case SQL_VIAS:
                sql= getSelectVias(superpatron, patron, null, idLayers, idFeatures, userSesion, " mejoras_inventario, ") + " AND ";
                addFiltro= true;
                break;

        }

        if (cadena != null){
            sql+= "bienes_inventario.id=mejoras_inventario.id_bien AND (UPPER(mejoras_inventario.descripcion) like UPPER('%"+cadena+"%')) " ;
           // 		"OR ";
           // sql+= "UPPER(mejoras_inventario.fecha) like UPPER('%"+cadena+"%') OR ";
           //sql+= "UPPER(mejoras_inventario.fecha_ejecucion) like UPPER('%"+cadena+"%') OR ";
          //  sql+= "UPPER(mejoras_inventario.fecha_ultima_modificacion) like UPPER('%"+cadena+"%') OR ";
          //  sql+= "UPPER(mejoras_inventario.importe) like UPPER('%"+cadena+"%'))";
        }else if ((addFiltro) && (filtro!=null)){
            sql+= "bienes_inventario.id=mejoras_inventario.id_bien ";
            for (int i=0; i<filtro.length; i++){
                CampoFiltro cf= (CampoFiltro)filtro[i];
                sql+= addPartOfWhere(cf);
            }
        }

        return sql;
    }

    private String addSQLUsosFuncionales(int tiposql, String superpatron, String patron, String cadena, Object[] idLayers, Object[] idFeatures, Sesion userSesion) throws Exception{
        return addSQLUsosFuncionales(tiposql, superpatron, patron, cadena, null, idLayers, idFeatures, userSesion);
    }

    private String addSQLUsosFuncionales(int tiposql, String superpatron, String patron, Object[] filtro, Object[] idLayers, 
    		Object[] idFeatures, Sesion userSesion) throws Exception{
        return addSQLUsosFuncionales(tiposql, superpatron, patron, null, filtro, idLayers, idFeatures, userSesion);
    }
    private String addSQLUsosFuncionales(int tiposql, String superpatron, String patron, String cadena, Object[] filtro, Object[] idLayers, 
    		Object[] idFeatures, Sesion userSesion) throws Exception{
        String sql= "";
        boolean addFiltro= false;
        switch (tiposql){
            case SQL_INMUEBLE:
                sql= getSelectInmueble(superpatron, patron, null, idLayers, idFeatures, userSesion, " usos_funcionales_inventario, ") + " AND ";
                addFiltro= true;
                break;
        }

        if (cadena != null){
            sql+= "bienes_inventario.id=usos_funcionales_inventario.id_bien " ;
            //		" AND UPPER(usos_funcionales_inventario.superficie) like UPPER('%"+cadena+"%')";
            //+ " OR ";
            //sql+= "UPPER(usos_funcionales_inventario.fecha) like UPPER('%"+cadena+"%'))";
        }else if ((addFiltro) && (filtro!=null)){
            sql+= "bienes_inventario.id=usos_funcionales_inventario.id_bien ";
            for (int i=0; i<filtro.length; i++){
                CampoFiltro cf= (CampoFiltro)filtro[i];
                sql+= addPartOfWhere(cf);
            }
        }

        return sql;
    }

    private String addSQLObservaciones(int tiposql, String superpatron, String patron, String cadena, 
    		Object[] idLayers, Object[] idFeatures, Sesion userSesion) throws Exception{
        return addSQLObservaciones(tiposql, superpatron, patron, cadena, null, idLayers, idFeatures, userSesion);

    }
    private String addSQLObservaciones(int tiposql, String superpatron, String patron,
    		Object[] filtro, Object[] idLayers, Object[] idFeatures, Sesion userSesion) throws Exception{
        return addSQLObservaciones(tiposql, superpatron, patron, null, filtro, idLayers, idFeatures, userSesion);
    }

    private String addSQLObservaciones(int tiposql, String superpatron, String patron, String cadena, 
    		Object[] filtro, Object[] idLayers, Object[] idFeatures, Sesion userSesion) throws Exception{
        String sql= "";
        boolean addFiltro= false;

        switch (tiposql){
            case SQL_INMUEBLE:
                sql= getSelectInmueble(superpatron, patron, null, idLayers, idFeatures, userSesion, " observaciones_inventario, ") + " AND ";
                addFiltro= true;
                break;
            case SQL_MUEBLE:
                sql= getSelectMueble(superpatron, patron, null, idLayers, idFeatures, userSesion, " observaciones_inventario, ") + " AND ";
                addFiltro= true;
                break;
            case SQL_DERECHOS_REALES:
                sql= getSelectDerechosReales(superpatron, patron, null, idLayers, idFeatures, userSesion, " observaciones_inventario, ") + " AND ";
                addFiltro= true;
                break;
            case SQL_CREDITO_DERECHO:
                sql= getSelectCreditosDerechos(superpatron, patron, null, idLayers, idFeatures, userSesion, " observaciones_inventario, ") + " AND ";
                addFiltro= true;
                break;
            case SQL_VALOR_MOBILIARIO:
                sql= getSelectValorMobiliario(superpatron, patron, null, idLayers, idFeatures, userSesion, " observaciones_inventario, ") + " AND ";
                addFiltro= true;
                break;
            case SQL_SEMOVIENTE:
                sql= getSelectSemoviente(superpatron, patron, null, idLayers, idFeatures, userSesion, " observaciones_inventario, ") + " AND ";
                addFiltro= true;
                break;
            case SQL_VIAS:
                sql= getSelectVias(superpatron, patron, null, idLayers, idFeatures, userSesion, " observaciones_inventario, ") + " AND ";
                addFiltro= true;
                break;
            case SQL_VEHICULOS:
                sql= getSelectVehiculo(superpatron, patron, null, idLayers, idFeatures, userSesion, " observaciones_inventario, ") + " AND ";
                addFiltro= true;
                break;

        }

        if (cadena != null){
            sql+= "bienes_inventario.id=observaciones_inventario.id_bien AND (UPPER(observaciones_inventario.descripcion) like UPPER('%"+cadena+"%')" ;
            //		" OR ";
           // sql+= "UPPER(observaciones_inventario.fecha) like UPPER('%"+cadena+"%') OR ";
           // sql+= "UPPER(observaciones_inventario.fecha_ultima_modificacion) like UPPER('%"+cadena+"%')";
            sql+= ")";
        }else if ((addFiltro) && (filtro!=null)){
            sql+= "bienes_inventario.id=observaciones_inventario.id_bien ";
            for (int i=0; i<filtro.length; i++){
                CampoFiltro cf= (CampoFiltro)filtro[i];
                sql+= addPartOfWhere(cf);
            }
        }

        return sql;
    }

    private String addSQLRefCatastrales(int tiposql, String superpatron, String patron, String cadena, 
    		Object[] idLayers, Object[] idFeatures, Sesion userSesion) throws Exception{
        return addSQLRefCatastrales(tiposql, superpatron, patron, cadena, null, idLayers, 
        		idFeatures, userSesion);
    }

    private String addSQLRefCatastrales(int tiposql, String superpatron, String patron, Object[] filtro, Object[] idLayers,
    		Object[] idFeatures, Sesion userSesion) throws Exception{
        return addSQLRefCatastrales(tiposql, superpatron, patron, null, filtro, idLayers, idFeatures, userSesion);
    }

    private String addSQLRefCatastrales(int tiposql, String superpatron, String patron, String cadena,
    		Object[] filtro, Object[] idLayers, Object[] idFeatures, Sesion userSesion) throws Exception{
        String sql= "";
        boolean addFiltro= false;

        switch (tiposql){
            case SQL_INMUEBLE:
                sql= getSelectInmueble(superpatron, patron, null, idLayers, idFeatures,
                		userSesion, " refcatastrales_inventario, ") + " AND ";
                addFiltro= true;
                break;
        }

        if (cadena != null){
            sql+= "bienes_inventario.id=refcatastrales_inventario.id_bien AND (UPPER(refcatastrales_inventario.descripcion) like UPPER('%"+cadena+"%') OR ";
            sql+= "UPPER(refcatastrales_inventario.ref_catastral) like UPPER('%"+cadena+"%'))";
        }else if ((addFiltro) && (filtro!=null)){
            sql+= "bienes_inventario.id=refcatastrales_inventario.id_bien ";
            for (int i=0; i<filtro.length; i++){
                CampoFiltro cf= (CampoFiltro)filtro[i];
                sql+= addPartOfWhere(cf);
            }
        }

        return sql;
    }

    private String addSQLDocumentacion(int tiposql, String superpatron, String patron, String cadena, Object[] idLayers, 
    		Object[] idFeatures, Sesion userSesion) throws Exception{
        return addSQLDocumentacion(tiposql, superpatron, patron, cadena, null, idLayers, idFeatures, userSesion);
    }

    private String addSQLDocumentacion(int tiposql, String superpatron, String patron, 
    		Object[] filtro, Object[] idLayers, Object[] idFeatures, Sesion userSesion) throws Exception{
        return addSQLDocumentacion(tiposql, superpatron, patron, null, filtro, idLayers, idFeatures, userSesion);
    }
    private String addSQLDocumentacion(int tiposql, String superpatron, String patron, String cadena, 
    		Object[] filtro, Object[] idLayers, Object[] idFeatures, Sesion userSesion) throws Exception{
        String sql= "";
        boolean addFiltro= false;

        switch (tiposql){
            case SQL_INMUEBLE:
                sql= getSelectInmueble(superpatron, patron, null, idLayers, idFeatures, userSesion, " anexo_inventario , documento, ") + " AND ";
                addFiltro= true;
                break;
            case SQL_MUEBLE:
                sql= getSelectMueble(superpatron, patron, null, idLayers, idFeatures, userSesion, " anexo_inventario , documento, ") + " AND ";
                addFiltro= true;
                break;
            case SQL_DERECHOS_REALES:
                sql= getSelectDerechosReales(superpatron, patron, null, idLayers, idFeatures, userSesion, " anexo_inventario , documento, ") + " AND ";
                addFiltro= true;
                break;
            case SQL_CREDITO_DERECHO:
                sql= getSelectCreditosDerechos(superpatron, patron, null, idLayers, idFeatures, userSesion, " anexo_inventario , documento, ") + " AND ";
                addFiltro= true;
                break;
            case SQL_VALOR_MOBILIARIO:
                sql= getSelectValorMobiliario(superpatron, patron, null, idLayers, idFeatures, userSesion, " anexo_inventario , documento, ") + " AND ";
                addFiltro= true;
                break;
            case SQL_SEMOVIENTE:
                sql= getSelectSemoviente(superpatron, patron, null, idLayers, idFeatures, userSesion, " anexo_inventario , documento,  ") + " AND ";
                addFiltro= true;
                break;
            case SQL_VIAS:
                sql= getSelectVias(superpatron, patron, null, idLayers, idFeatures, userSesion, " anexo_inventario , documento, ") + " AND ";
                addFiltro= true;
                break;
            case SQL_VEHICULOS:
                sql= getSelectVehiculo(superpatron, patron, null, idLayers, idFeatures, userSesion, " anexo_inventario , documento, ") + " AND ";
                addFiltro= true;
                break;

        }

        if (cadena != null){
            sql+= "bienes_inventario.id=anexo_inventario.id_bien AND anexo_inventario.id_documento=documento.id_documento AND " +
                    "(UPPER(documento.nombre) like UPPER('%"+cadena+"%')" +
             //       		" OR UPPER(documento.fecha_alta) like UPPER('%"+cadena+"%') OR " +
              //      "UPPER(documento.fecha_modificacion) like UPPER('%"+cadena+"%')" +
                    		" OR UPPER(documento.comentario) like UPPER('%"+cadena+"%'))";
        }else if ((addFiltro) && (filtro!=null)){
            sql+= "bienes_inventario.id=anexo_inventario.id_bien AND anexo_inventario.id_documento=documento.id_documento ";
            for (int i=0; i<filtro.length; i++){
                CampoFiltro cf= (CampoFiltro)filtro[i];
                sql+= addPartOfWhere(cf);
            }
        }
        return sql;

    }


    private String addSelectsBusquedaMueble(String superpatron, String patron, String cadena, 
    		Object[] idLayers, Object[] idFeatures, Sesion userSesion) throws Exception{
        if (cadena==null || cadena.trim().equalsIgnoreCase("")) return "";

        String sql= addSQLMueble(superpatron, patron, cadena, idLayers, idFeatures, userSesion);
        sql+= " UNION ";
        sql+= addSQLSegurosInventario(SQL_MUEBLE, superpatron, patron, cadena, idLayers, idFeatures, userSesion);
        sql+= " UNION ";
        sql+= addSQLContable(SQL_MUEBLE, superpatron, patron, cadena, idLayers, idFeatures, userSesion);
        sql+= " UNION ";
        sql+= addSQLAmortizacion(SQL_MUEBLE, superpatron, patron, cadena, idLayers, idFeatures, userSesion);
        sql+= " UNION ";
        sql+= addSQLObservaciones(SQL_MUEBLE, superpatron, patron, cadena, idLayers, idFeatures, userSesion);
        sql+= " UNION ";
        sql+= addSQLDocumentacion(SQL_MUEBLE, superpatron, patron, cadena, idLayers, idFeatures, userSesion);

        return sql;
    }

    private String getSelectMueble(String superpatron, String patron, Object[] filtro, Object[] idLayers,
    		Object[] idFeatures, Sesion userSesion, String addToFrom) throws Exception {
        
    	String sSQL="";

    	sSQL= "select bienes_inventario.id as idBien,bienes_inventario.numinventario, bienes_inventario.organizacion, bienes_inventario.nombre, bienes_inventario.descripcion, " +
    			"bienes_inventario.uso, bienes_inventario.tipo,bienes_inventario.fecha_alta, bienes_inventario.fecha_aprobacion_pleno, bienes_inventario.fecha_ultima_modificacion, " +
    			"bienes_inventario.borrado, bienes_inventario.id_cuenta_contable,bienes_inventario.id_seguro,muebles.* " +
    			",versiones.*,iuseruserhdr.name,tables_inventario.name ";
    	if (idFeatures.length > 0){		
    		sSQL+=" ,inventario_feature.revision_actual AS feature_revision_actual ";
    	}
    	
    	sSQL+= 	" from " + addToFrom + "muebles inner join bienes_inventario on " +
    			" bienes_inventario.id=muebles.id and bienes_inventario.revision_actual=muebles.revision_actual ";
		sSQL+= " inner join versiones on versiones.revision=muebles.revision_actual ";
		sSQL+= " inner join iuseruserhdr on versiones.id_autor=iuseruserhdr.id ";
		sSQL+= " inner join tables_inventario on versiones.id_table_versionada=tables_inventario.id_table ";
    	
    	if (idFeatures.length > 0){
    		sSQL+= "inner join inventario_feature on bienes_inventario.id=inventario_feature.id_bien ";
    	}
    	
    	for(int i=0; i<idFeatures.length; i++){
    		
    		if (i==0) 
    			sSQL+= "AND inventario_feature.id_feature="+(String)idFeatures[i]+" AND " +
    			"inventario_feature.id_layer="+getIdLayer((String)idLayers[i]);
    		else 
    			sSQL+=" OR (inventario_feature.id_bien=bienes_inventario.id AND " +
    			"inventario_feature.id_feature="+(String)idFeatures[i]+" AND " +
    			"inventario_feature.id_layer="+getIdLayer((String)idLayers[i])+") ";
    	}

    	sSQL+=" WHERE bienes_inventario.id_municipio='"+municipio+"' AND bienes_inventario.tipo='"+patron+"' ";
    	/** en funcion del superpatron */
    	   
    	if (superpatron!=null){
	    	if (superpatron.equalsIgnoreCase(Const.SUPERPATRON_REVERTIBLES))
	            sSQL+="AND muebles.propiedad='"+Const.PATRON_CEDIDO+"' ";
	        else if (superpatron.equalsIgnoreCase(Const.SUPERPATRON_PATRIMONIO_MUNICIPAL_SUELO))
	            sSQL+="AND muebles.patrimonio_municipal_suelo='1' ";
    	}
    	sSQL += sSQLVersionado.replaceAll("\\?", "muebles");;
    	sSQL += " AND tables_inventario.name='muebles'";
    	if ((filtro==null) || (filtro.length==0)) return sSQL;

    	return addFiltro(sSQL, SQL_MUEBLE, superpatron, patron, filtro, idLayers, idFeatures, userSesion);

    }

    private String addSQLMueble(String superpatron, String patron, String cadena, Object[] idLayers, Object[] idFeatures, Sesion userSesion) throws Exception{

        String sql= getSelectMueble(superpatron, patron, null, idLayers, idFeatures, userSesion, "") + " AND ";
        sql+= "(UPPER(bienes_inventario.numinventario) like UPPER('%"+cadena+"%') OR " +
           //   "UPPER(bienes_inventario.fecha_alta) like UPPER('%"+cadena+"%') OR " +
           //   "UPPER(bienes_inventario.fecha_baja) like UPPER('%"+cadena+"%') OR " +
           //   "UPPER(bienes_inventario.fecha_ultima_modificacion) like UPPER('%"+cadena+"%') OR "+
              "UPPER(bienes_inventario.descripcion) like UPPER('%"+cadena+"%') OR "+
              "UPPER(bienes_inventario.nombre) like UPPER('%"+cadena+"%') OR "+
              "UPPER(bienes_inventario.organizacion) like UPPER('%"+cadena+"%') OR ";
        //sql+= "UPPER(muebles.fecha_adquisicion) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(muebles.direccion) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(muebles.ubicacion) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(muebles.caracteristicas) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(muebles.material) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(muebles.autor) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(muebles.destino) like UPPER('%"+cadena+"%') OR ";
        //sql+= "UPPER(muebles.coste_adquisicion) like UPPER('%"+cadena+"%') OR ";
        //sql+= "UPPER(muebles.valor_actual) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(muebles.frutos) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(muebles.num_serie) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(muebles.marca) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(muebles.modelo) like UPPER('%"+cadena+"%') ";
        //		"OR ";
       // sql+= "UPPER(muebles.fecha_fin_garantia) like UPPER('%"+cadena+"%')";
        sql+=")";
        return sql;
    }

    private String getSelectDerechosReales(String superpatron, String patron, Object[] filtro, Object[] idLayers,
    		Object[] idFeatures, Sesion userSesion, String addToFrom) throws Exception {
        
    	String sSQL="";

    	sSQL= "select bienes_inventario.numinventario, bienes_inventario.organizacion,bienes_inventario.nombre, " +
    			"bienes_inventario.descripcion, bienes_inventario.tipo,bienes_inventario.uso, bienes_inventario.fecha_alta, bienes_inventario.fecha_aprobacion_pleno, bienes_inventario.id_cuenta_contable," +
    			"bienes_inventario.fecha_ultima_modificacion, bienes_inventario.borrado, derechos_reales.* " +
    			",versiones.*,iuseruserhdr.name,tables_inventario.name ";
    	if (idFeatures.length > 0){		
    		sSQL+=" ,inventario_feature.revision_actual AS feature_revision_actual ";
    	}
    	sSQL+=" from " + addToFrom + "derechos_reales inner join bienes_inventario on bienes_inventario.id=derechos_reales.id ";
    	sSQL+= " and bienes_inventario.revision_actual=derechos_reales.revision_actual ";
		sSQL+= " inner join versiones on versiones.revision=derechos_reales.revision_actual ";
		sSQL+= " inner join iuseruserhdr on versiones.id_autor=iuseruserhdr.id ";
		sSQL+= " inner join tables_inventario on versiones.id_table_versionada=tables_inventario.id_table ";
    	
    	if (idFeatures.length > 0){
    		sSQL+= "inner join inventario_feature on bienes_inventario.id=inventario_feature.id_bien ";
    	}
    	
    	for(int i=0; i<idFeatures.length; i++){
    		
    		if (i==0) 
    			sSQL+= "AND inventario_feature.id_feature="+(String)idFeatures[i]+" AND " +
    			"inventario_feature.id_layer="+getIdLayer((String)idLayers[i]);
    		else 
    			sSQL+=" OR (inventario_feature.id_bien=bienes_inventario.id AND " +
    			"inventario_feature.id_feature="+(String)idFeatures[i]+" AND " +
    			"inventario_feature.id_layer="+getIdLayer((String)idLayers[i])+") ";
    	}

    	sSQL+=" WHERE bienes_inventario.id_municipio='"+municipio+"' AND bienes_inventario.tipo='"+patron+"' ";
    	/** en funcion del superpatron */  	
    	
    	if (superpatron!=null && superpatron.equalsIgnoreCase(Const.SUPERPATRON_PATRIMONIO_MUNICIPAL_SUELO))
            sSQL+="AND derechos_reales.patrimonio_municipal_suelo='1' ";

    	sSQL += sSQLVersionado.replaceAll("\\?", "derechos_reales");;
    	sSQL += " AND tables_inventario.name='derechos_reales'";

    	if ((filtro==null) || (filtro.length==0)) return sSQL;
    	
    	return addFiltro(sSQL, SQL_DERECHOS_REALES, superpatron, patron, filtro, idLayers, idFeatures, userSesion);
    	
    }

    private String addSQLDerechosReales(String superpatron, String patron, String cadena, Object[] idLayers, Object[] idFeatures,
    		Sesion userSesion) throws Exception{

        String sql= getSelectDerechosReales(superpatron, patron, null, idLayers, idFeatures, userSesion, "") + " AND ";
        sql+= "(UPPER(bienes_inventario.numinventario) like UPPER('%"+cadena+"%') OR " +
             // "UPPER(bienes_inventario.fecha_alta) like UPPER('%"+cadena+"%') OR " +
             // "UPPER(bienes_inventario.fecha_baja) like UPPER('%"+cadena+"%') OR " +
             // "UPPER(bienes_inventario.fecha_ultima_modificacion) like UPPER('%"+cadena+"%') OR "+
              "UPPER(bienes_inventario.descripcion) like UPPER('%"+cadena+"%') OR "+
              "UPPER(bienes_inventario.nombre) like UPPER('%"+cadena+"%') OR "+
              "UPPER(bienes_inventario.organizacion) like UPPER('%"+cadena+"%') OR ";

       // sql+= "UPPER(derechos_reales.fecha_adquisicion) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(derechos_reales.bien) like UPPER('%"+cadena+"%') OR ";
        //sql+= "UPPER(derechos_reales.coste) like UPPER('%"+cadena+"%') OR ";
       // sql+= "UPPER(derechos_reales.valor) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(derechos_reales.registro_notario) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(derechos_reales.registro_protocolo) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(derechos_reales.destino) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(derechos_reales.registro_propiedad) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(derechos_reales.registro_tomo) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(derechos_reales.registro_libro) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(derechos_reales.registro_folio) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(derechos_reales.registro_finca) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(derechos_reales.registro_inscripcion) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(derechos_reales.frutos) like UPPER('%"+cadena+"%') ";//OR ";
       // sql+= "UPPER(derechos_reales.importe_frutos) like UPPER('%"+cadena+"%')";
        sql+=")";
        return sql;
    }

    private String addSelectsBusquedaDerechosReales(String superpatron, String patron, String cadena, 
    		Object[] idLayers, Object[] idFeatures, Sesion userSesion) throws Exception{
        if (cadena==null || cadena.trim().equalsIgnoreCase("")) return "";

        String sql= addSQLDerechosReales(superpatron, patron, cadena, idLayers, idFeatures, userSesion);
        sql+= " UNION ";
        sql+= addSQLObservaciones(SQL_DERECHOS_REALES, superpatron, patron, cadena, idLayers, idFeatures, userSesion);
        sql+= " UNION ";
        sql+= addSQLDocumentacion(SQL_DERECHOS_REALES, superpatron, patron, cadena, idLayers, idFeatures, userSesion);
        sql+= " UNION ";
        sql+= addSQLContable(SQL_DERECHOS_REALES, superpatron, patron, cadena, idLayers, idFeatures, userSesion);

        return sql;
    }


    private String getSelectCreditosDerechos(String superpatron, String patron, Object[] filtro, Object[] idLayers, 
    		Object[] idFeatures, Sesion userSesion, String addToFrom) throws Exception {
        
        String sSQL="";

    	sSQL= "select bienes_inventario.numinventario, bienes_inventario.nombre, bienes_inventario.descripcion as " +
    			"BIEN_DESCRIPCION, bienes_inventario.tipo,bienes_inventario.organizacion,bienes_inventario.uso, bienes_inventario.fecha_alta, bienes_inventario.fecha_aprobacion_pleno , " +
    			"bienes_inventario.fecha_ultima_modificacion, bienes_inventario.id_cuenta_contable," +
    			"bienes_inventario.borrado, credito_derecho.* ,versiones.*,iuseruserhdr.name,tables_inventario.name ";
    	if (idFeatures.length > 0){		
    		sSQL+=" ,inventario_feature.revision_actual AS feature_revision_actual ";
    	}
    	sSQL+=	" from " + addToFrom + "credito_derecho inner join bienes_inventario on bienes_inventario.id=credito_derecho.id ";
    	sSQL+= " and bienes_inventario.revision_actual=credito_derecho.revision_actual ";
		sSQL+= " inner join versiones on versiones.revision=credito_derecho.revision_actual ";
		sSQL+= " inner join iuseruserhdr on versiones.id_autor=iuseruserhdr.id ";
		sSQL+= " inner join tables_inventario on versiones.id_table_versionada=tables_inventario.id_table ";
    	
    	if (idFeatures.length > 0){
    		sSQL+= "inner join inventario_feature on bienes_inventario.id=inventario_feature.id_bien ";
    	}
    	
    	for(int i=0; i<idFeatures.length; i++){
    		
    		if (i==0) 
    			sSQL+= "AND inventario_feature.id_feature="+(String)idFeatures[i]+" AND " +
    			"inventario_feature.id_layer="+getIdLayer((String)idLayers[i]);
    		else 
    			sSQL+=" OR (inventario_feature.id_bien=bienes_inventario.id AND " +
    			"inventario_feature.id_feature="+(String)idFeatures[i]+" AND " +
    			"inventario_feature.id_layer="+getIdLayer((String)idLayers[i])+") ";
    	}

    	sSQL+=" WHERE bienes_inventario.id_municipio='"+municipio+"' AND bienes_inventario.tipo='"+patron+"' ";
    	/** en funcion del superpatron */
    	
    	if (superpatron!=null && superpatron.equalsIgnoreCase(Const.SUPERPATRON_PATRIMONIO_MUNICIPAL_SUELO))
            sSQL+="AND credito_derecho.patrimonio_municipal_suelo='1' ";

    	sSQL += sSQLVersionado.replaceAll("\\?", "credito_derecho");
    	sSQL += " AND tables_inventario.name='credito_derecho'";
   	
    	if ((filtro==null) || (filtro.length==0)) return sSQL;

        return addFiltro(sSQL, SQL_CREDITO_DERECHO, superpatron, patron, filtro, idLayers, idFeatures, userSesion);

    }

    private String addSQLCreditosDerechos(String superpatron, String patron, String cadena, 
    		Object[] idLayers, Object[] idFeatures, Sesion userSesion) throws Exception{

        String sql= getSelectCreditosDerechos(superpatron, patron, null, idLayers, idFeatures, userSesion, "") + " AND ";
        sql+= "(UPPER(bienes_inventario.numinventario) like UPPER('%"+cadena+"%') OR " +
              //"UPPER(bienes_inventario.fecha_alta) like UPPER('%"+cadena+"%') OR " +
              //"UPPER(bienes_inventario.fecha_baja) like UPPER('%"+cadena+"%') OR " +
              //"UPPER(bienes_inventario.fecha_ultima_modificacion) like UPPER('%"+cadena+"%') OR "+
              "UPPER(bienes_inventario.descripcion) like UPPER('%"+cadena+"%') OR "+
              "UPPER(bienes_inventario.nombre) like UPPER('%"+cadena+"%') OR "+
              "UPPER(bienes_inventario.organizacion) like UPPER('%"+cadena+"%') OR ";

        //sql+= "UPPER(credito_derecho.fecha_adquisicion) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(credito_derecho.deudor) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(credito_derecho.descripcion) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(credito_derecho.destino) like UPPER('%"+cadena+"%') OR ";
       // sql+= "UPPER(credito_derecho.importe) like UPPER('%"+cadena+"%') OR ";
        //sql+= "UPPER(credito_derecho.fecha_vencimiento) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(credito_derecho.concepto_desc) like UPPER('%"+cadena+"%')";
        sql+=")";
        return sql;
    }

    private String addSelectsBusquedaCreditosDerechos(String superpatron, String patron, String cadena, 
    		Object[] idLayers, Object[] idFeatures, Sesion userSesion) throws Exception{
        if (cadena==null || cadena.trim().equalsIgnoreCase("")) return "";

        String sql= addSQLCreditosDerechos(superpatron, patron, cadena, idLayers, idFeatures, userSesion);
        sql+= " UNION ";
        sql+= addSQLObservaciones(SQL_CREDITO_DERECHO, superpatron, patron, cadena, idLayers, idFeatures, userSesion);
        sql+= " UNION ";
        sql+= addSQLDocumentacion(SQL_CREDITO_DERECHO, superpatron, patron, cadena, idLayers, idFeatures, userSesion);
        sql+= " UNION ";
        sql+= addSQLContable(SQL_CREDITO_DERECHO, superpatron, patron, cadena, idLayers, idFeatures, userSesion);

        return sql;
    }


    private String getSelectValorMobiliario(String superpatron, String patron, Object[] filtro, Object[] idLayers, Object[] idFeatures,
    		Sesion userSesion, String addToFrom) throws Exception {
            	
    	String sSQL="";

    	sSQL= "select bienes_inventario.numinventario, bienes_inventario.nombre, bienes_inventario.descripcion, bienes_inventario.id_cuenta_contable," +
    			"bienes_inventario.uso,bienes_inventario.tipo, bienes_inventario.organizacion,bienes_inventario.fecha_alta, bienes_inventario.fecha_ultima_modificacion, " +
    			"bienes_inventario.fecha_aprobacion_pleno, bienes_inventario.id_cuenta_contable,bienes_inventario.id_seguro," +
    			"bienes_inventario.borrado, valor_mobiliario.* ,versiones.*,iuseruserhdr.name,tables_inventario.name ";
    	if (idFeatures.length > 0){		
    		sSQL+=" ,inventario_feature.revision_actual AS feature_revision_actual ";
    	}
    	sSQL+=	" from " + addToFrom + "valor_mobiliario inner join bienes_inventario on bienes_inventario.id=valor_mobiliario.id ";
    	sSQL+= " and bienes_inventario.revision_actual=valor_mobiliario.revision_actual ";
		sSQL+= " inner join versiones on versiones.revision=valor_mobiliario.revision_actual ";
		sSQL+= " inner join iuseruserhdr on versiones.id_autor=iuseruserhdr.id ";
		sSQL+= " inner join tables_inventario on versiones.id_table_versionada=tables_inventario.id_table ";
    	
    	if (idFeatures.length > 0){
    		sSQL+= "inner join inventario_feature on bienes_inventario.id=inventario_feature.id_bien ";
    	}
    	
    	for(int i=0; i<idFeatures.length; i++){
    		
    		if (i==0) 
    			sSQL+= "AND inventario_feature.id_feature="+(String)idFeatures[i]+" AND " +
    			"inventario_feature.id_layer="+getIdLayer((String)idLayers[i]);
    		else 
    			sSQL+=" OR (inventario_feature.id_bien=bienes_inventario.id AND " +
    			"inventario_feature.id_feature="+(String)idFeatures[i]+" AND " +
    			"inventario_feature.id_layer="+getIdLayer((String)idLayers[i])+") ";
    	}

    	sSQL+=" WHERE bienes_inventario.id_municipio='"+municipio+"' AND bienes_inventario.tipo='"+patron+"' ";
    	/** en funcion del superpatron */  	
    	
    	if (superpatron!=null && superpatron.equalsIgnoreCase(Const.SUPERPATRON_PATRIMONIO_MUNICIPAL_SUELO))
            sSQL+="AND valor_mobiliario.patrimonio_municipal_suelo='1' ";

    	sSQL += sSQLVersionado.replaceAll("\\?", "valor_mobiliario");;
    	sSQL += " AND tables_inventario.name='valor_mobiliario'";

    	if ((filtro==null) || (filtro.length==0)) return sSQL;

    	return addFiltro(sSQL, SQL_VALOR_MOBILIARIO, superpatron, patron, filtro, idLayers, idFeatures, userSesion);
    	
    }

    private String addSQLValorMobiliario(String superpatron, String patron, String cadena, 
    		Object[] idLayers, Object[] idFeatures, Sesion userSesion) throws Exception{

        String sql= getSelectValorMobiliario(superpatron, patron, null, idLayers, idFeatures, userSesion, "") + " AND ";
        sql+= "(UPPER(bienes_inventario.numinventario) like UPPER('%"+cadena+"%') OR " +
            //  "UPPER(bienes_inventario.fecha_alta) like UPPER('%"+cadena+"%') OR " +
            //  "UPPER(bienes_inventario.fecha_baja) like UPPER('%"+cadena+"%') OR " +
            //  "UPPER(bienes_inventario.fecha_ultima_modificacion) like UPPER('%"+cadena+"%') OR "+
              "UPPER(bienes_inventario.descripcion) like UPPER('%"+cadena+"%') OR "+
              "UPPER(bienes_inventario.nombre) like UPPER('%"+cadena+"%') OR "+
              "UPPER(bienes_inventario.organizacion) like UPPER('%"+cadena+"%') OR ";

       // sql+= "UPPER(valor_mobiliario.fecha_adquisicion) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(valor_mobiliario.depositado_en) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(valor_mobiliario.emitido_por) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(valor_mobiliario.numero) like UPPER('%"+cadena+"%') OR ";
       // sql+= "UPPER(valor_mobiliario.serie) like UPPER('%"+cadena+"%') OR ";
       // sql+= "UPPER(valor_mobiliario.num_titulos) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(valor_mobiliario.destino) like UPPER('%"+cadena+"%') OR ";
       // sql+= "UPPER(valor_mobiliario.coste_adquisicion) like UPPER('%"+cadena+"%') OR ";
       // sql+= "UPPER(valor_mobiliario.valor_actual) like UPPER('%"+cadena+"%') OR ";
       // sql+= "UPPER(valor_mobiliario.precio) like UPPER('%"+cadena+"%') OR ";
       // sql+= "UPPER(valor_mobiliario.capital) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(valor_mobiliario.frutos) like UPPER('%"+cadena+"%') ";//OR ";
       // sql+= "UPPER(valor_mobiliario.importe_frutos) like UPPER('%"+cadena+"%') " ;
        //		"OR ";
       // sql+= "UPPER(valor_mobiliario.fecha_acuerdo) like UPPER('%"+cadena+"%')";
        sql+=")";
        return sql;
    }

    private String addSelectsBusquedaValorMobiliario(String superpatron, String patron, String cadena, 
    		Object[] idLayers, Object[] idFeatures, Sesion userSesion) throws Exception{
        if (cadena==null || cadena.trim().equalsIgnoreCase("")) return "";

        String sql= addSQLValorMobiliario(superpatron, patron, cadena, idLayers, idFeatures, userSesion);
        sql+= " UNION ";
        sql+= addSQLObservaciones(SQL_VALOR_MOBILIARIO, superpatron, patron, cadena, idLayers, idFeatures, userSesion);
        sql+= " UNION ";
        sql+= addSQLDocumentacion(SQL_VALOR_MOBILIARIO, superpatron, patron, cadena, idLayers, idFeatures, userSesion);
        sql+= " UNION ";
        sql+= addSQLContable(SQL_VALOR_MOBILIARIO, superpatron, patron, cadena, idLayers, idFeatures, userSesion);
        sql+= " UNION ";
        sql+= addSQLSegurosInventario(SQL_VALOR_MOBILIARIO, superpatron, patron, cadena, idLayers, idFeatures, userSesion);

        return sql;
    }

    private String getSelectSemoviente(String superpatron, String patron, Object[] filtro, Object[] idLayers,
    		Object[] idFeatures, Sesion userSesion, String addToFrom) throws Exception {
            	
    	String sSQL="";

    	sSQL= "select bienes_inventario.numinventario, bienes_inventario.nombre, bienes_inventario.descripcion as " +
    			"BIEN_DESCRIPCION, bienes_inventario.tipo,bienes_inventario.organizacion,bienes_inventario.uso, bienes_inventario.fecha_alta," +
    			"bienes_inventario.fecha_aprobacion_pleno, bienes_inventario.id_cuenta_contable," +
    			"bienes_inventario.fecha_ultima_modificacion, bienes_inventario.borrado, bienes_inventario.id_seguro, semoviente.* " +
    			",versiones.*,iuseruserhdr.name,tables_inventario.name ";
    	if (idFeatures.length > 0){		
    		sSQL+=" ,inventario_feature.revision_actual AS feature_revision_actual ";
    	}
    	sSQL+=	" from " + addToFrom + "semoviente inner join bienes_inventario on bienes_inventario.id=semoviente.id ";
    	sSQL+= " and bienes_inventario.revision_actual=semoviente.revision_actual ";
		sSQL+= " inner join versiones on versiones.revision=semoviente.revision_actual ";
		sSQL+= " inner join iuseruserhdr on versiones.id_autor=iuseruserhdr.id ";
		sSQL+= " inner join tables_inventario on versiones.id_table_versionada=tables_inventario.id_table ";
    	
    	if (idFeatures.length > 0){
    		sSQL+= "inner join inventario_feature on bienes_inventario.id=inventario_feature.id_bien ";
    	}
    	
    	for(int i=0; i<idFeatures.length; i++){
    		
    		if (i==0) 
    			sSQL+= "AND inventario_feature.id_feature="+(String)idFeatures[i]+" AND " +
    			"inventario_feature.id_layer="+getIdLayer((String)idLayers[i]);
    		else 
    			sSQL+=" OR (inventario_feature.id_bien=bienes_inventario.id AND " +
    			"inventario_feature.id_feature="+(String)idFeatures[i]+" AND " +
    			"inventario_feature.id_layer="+getIdLayer((String)idLayers[i])+") ";
    	}

    	sSQL+=" WHERE bienes_inventario.id_municipio='"+municipio+"' AND bienes_inventario.tipo='"+patron+"' ";
    	/** en funcion del superpatron */  	
    	
    	if (superpatron!=null){
    		if (superpatron.equalsIgnoreCase(Const.SUPERPATRON_REVERTIBLES))
    			sSQL+="AND semoviente.propiedad='"+Const.PATRON_CEDIDO+"' ";
    		else if (superpatron.equalsIgnoreCase(Const.SUPERPATRON_PATRIMONIO_MUNICIPAL_SUELO))
    			sSQL+="AND semoviente.patrimonio_municipal_suelo='1' ";
    	}

    	sSQL += sSQLVersionado.replaceAll("\\?", "semoviente");;
    	sSQL += " AND tables_inventario.name='semoviente'";
    	if ((filtro==null) || (filtro.length==0)) return sSQL;

    	return addFiltro(sSQL, SQL_SEMOVIENTE, superpatron, patron, filtro, idLayers, idFeatures, userSesion);
    	
    }

    private String getSelectBienesRevertibles( String patron, Object[] filtro,  Sesion userSesion, 
    		String addToFrom) throws Exception {
    	
    	String sSQL="";

    	sSQL= "select bien_revertible.id as br_id, bien_revertible.num_inventario as br_num_inventario, bien_revertible.fecha_inicio as br_fecha_inicio, " +
    		  "bien_revertible.fecha_vencimiento as br_fecha_vencimiento,bien_revertible.fecha_transmision as br_fecha_transmision, bien_revertible.importe as br_importe, bien_revertible.poseedor as br_poseedor," +
    		  " bien_revertible.titulo_posesion as br_titulo_posesion , bien_revertible.borrado as br_borrado, bien_revertible.fecha_baja as br_fecha_baja,bien_revertible.condiciones_reversion as br_condiciones_reversion, bien_revertible.detalles as br_detalles," +
    		  " bien_revertible.cat_transmision as br_cat_transmision, bien_revertible.id_cuenta_amortizacion as br_id_cuenta_amortizacion , bien_revertible.revision_actual as br_revision_actual, bien_revertible.revision_expirada as br_revision_expirada, " +
    		  " bien_revertible.fecha_alta as br_fecha_alta, bien_revertible.fecha_ultima_modificacion as br_fecha_ultima_modificacion, bien_revertible.nombre as br_nombre, bien_revertible.organizacion as br_organizacion, bien_revertible.fecha_aprobacion_pleno as br_fecha_aprobacion_pleno, "+
    		  " bien_revertible.descripcion_bien as br_descripcion_bien, bien_revertible.fecha_adquisicion as br_fecha_adquisicion, bien_revertible.adquisicion as br_adquisicion, "+
    		  " bien_revertible.diagnosis as br_diagnosis, bien_revertible.patrimonio_municipal_suelo as br_patrimonio_municipal_suelo, bien_revertible.clase as br_clase, "+
    		  " bien_revertible.id_seguro as br_id_seguro, "+
    		  "bi.id as bi_id, bi.numinventario as bi_numinventario , bi.tipo as bi_tipo, " +
    		  "bi.descripcion as bi_descripcion, bi.nombre as bi_nombre, bi.fecha_alta as bi_fecha_alta, " +
    		  "bi.fecha_baja as bi_fecha_baja ,bi.fecha_aprobacion_pleno as bi_fecha_aprobacion_pleno, bi.fecha_ultima_modificacion as bi_fecha_ultima_modificacion, "+
    		  "bi.bloqueado as bi_bloqueado, bi.uso as bi_uso, bi.revision_expirada as bi_revision_expirada, versiones.* ,iuseruserhdr.name "+
    	      " from bien_revertible as bien_revertible " +
    	      "left outer join bien_revertible_bien as brb on bien_revertible.id=brb.id_bien_revertible  and bien_revertible.revision_actual=brb.revision " +
    	      "left outer join bienes_inventario as bi on brb.id_bien = bi.id  ";
    	sSQL+= " inner join versiones on versiones.revision=bien_revertible.revision_actual ";
    	sSQL+= " inner join iuseruserhdr on versiones.id_autor=iuseruserhdr.id ";
    	sSQL+= " inner join tables_inventario on versiones.id_table_versionada=tables_inventario.id_table ";
        
		sSQL+=" WHERE bien_revertible.id_municipio='"+municipio+"' " ;
    	sSQL += sSQLVersionado.replaceAll("\\?", "bien_revertible");
    	//sSQL+=" and (bi.revision_expirada = 9999999999 or bi.revision_expirada is null)";;    	
    	if (patron!=null)
    			sSQL+=" AND bi.tipo='"+patron+"' ";
    	/** en funcion del superpatron */  	
    	sSQL += " AND tables_inventario.name='bien_revertible'";
       	
    	if (filtro!=null){
    		for (int i=0;i<filtro.length;i++){
    			if (filtro[i] instanceof String)
    				sSQL+=filtro[i];
    		    if (filtro[i] instanceof CampoFiltro){
    		    	CampoFiltro cf = (CampoFiltro)filtro[i];
    		    	if (cf.getTabla().equalsIgnoreCase("bien_revertible")){
    		    				sSQL+=addPartOfWhere(cf);
    		    	}
    		    } 
    		}
    	}
    	
    	
    	// TODO return addFiltro(sSQL, SQL_SEMOVIENTE, superpatron, patron, filtro, idLayers, idFeatures, userSesion);
    	
    	return sSQL;
    }
    /**
     * Busqueda de bienes revertibles
     * @param patron
     * @param filtro
     * @param userSesion
     * @param addToFrom
     * @return
     * @throws Exception
     */
    private String getSelectBusquedaBienesRevertibles( String patron, 
    		                             String cadena,  Sesion userSesion, 
    		String addToFrom) throws Exception {
    	
    	String sSQL= getSelectBienesRevertibles( patron, 
                    null,  userSesion,  addToFrom); 
        sSQL+=" and (bien_revertible.num_inventario like '%"+cadena+"%' ";
        sSQL+=" or upper(bien_revertible.poseedor) like  upper('%"+cadena+"%') ";
        sSQL+=" or upper(bien_revertible.titulo_posesion) like  upper('%"+cadena+"%') ";
        sSQL+=" or upper(bien_revertible.condiciones_reversion) like  upper('%"+cadena+"%') ";
        sSQL+=" or upper(bien_revertible.detalles) like  upper('%"+cadena+"%') ";
        sSQL+=" or bi.numinventario like  '%"+cadena+"%' ";
        sSQL+=" or upper(bi.descripcion) like  upper('%"+cadena+"%') ";
        sSQL+=" or upper(bi.nombre) like  upper('%"+cadena+"%') ";
        //buscamos en las observaciones
        sSQL+=" or bien_revertible.id in (select id_bien from observaciones_bien_revertible" +
        		" where upper(descripcion) like upper('%"+cadena+"%')))";     
       
     	
    	return sSQL;
    }
    private String getSelectBienes (String superpatron,Object[] filtro,Object[] idLayers, Object[] idFeatures) throws Exception {
    	
    	String sSQL= "select * from bienes_inventario ";
    		
    	
    	//MEJORA_001. Seleccion de bienes independiente del epigrafe
    	if ((idFeatures!=null) && (idFeatures.length > 0)){
    		sSQL+= "inner join inventario_feature on bienes_inventario.id=inventario_feature.id_bien AND bienes_inventario.revision_actual=inventario_feature.revision_actual ";
    	}
    	else{
    		
    	}
    	if (idFeatures!=null){
	    	for(int i=0; i<idFeatures.length; i++){
	    		
	    		if (i==0) 
	    			sSQL+= "AND inventario_feature.id_feature="+(String)idFeatures[i]+" AND " +
	    			"inventario_feature.id_layer="+getIdLayer((String)idLayers[i]);
	    		else 
	    			sSQL+=" OR (inventario_feature.id_bien=bienes_inventario.id AND " +
	    			"inventario_feature.id_feature="+(String)idFeatures[i]+" AND " +
	    			"inventario_feature.id_layer="+getIdLayer((String)idLayers[i])+") ";
	    	}
    	}
	    	

    	sSQL+=" WHERE id_municipio='"+municipio+"' " ;
    	
    	/** en funcion del superpatron */  	
    	if (filtro!=null){
    		for (int i=0;i<filtro.length;i++)
    			if (filtro[i] instanceof String)
    				sSQL+=filtro[i];
    	}
    	
    	if ((idFeatures==null) || (idFeatures.length==0)){
	    	if ((superpatron!=null) &&
	    			(superpatron.equalsIgnoreCase(Const.SUPERPATRON_PATRIMONIO_MUNICIPAL_SUELO))){
	    		sSQL+="and id in (select id from vehiculo where patrimonio_municipal_suelo='1' ";
	    		sSQL+="union select id from inmuebles where patrimonio_municipal_suelo='1' "; 
	    		sSQL+="union select id from vias_inventario where patrimonio_municipal_suelo='1' ";
	    		sSQL+="union select id from derechos_reales where patrimonio_municipal_suelo='1' ";
	    		sSQL+="union select id from muebles where patrimonio_municipal_suelo='1' ";
	    		sSQL+="union select id from valor_mobiliario where patrimonio_municipal_suelo='1' ";
	    		sSQL+="union select id from credito_derecho where patrimonio_municipal_suelo='1' ";
	    		sSQL+="union select id from semoviente where patrimonio_municipal_suelo='1' ";
	    		sSQL+=") ";
	    	}
    	}
    	
    	
    	/** añadimos la ordenacion**/
    	//sSQL+=" order by id";    	
    	sSQL+="ORDER BY id ASC, bienes_inventario.revision_actual DESC";
    	return sSQL;
    }

    /**
     * Devuelve el select de lotes
     * @param patron
     * @param filtro
     * @param userSesion
     * @param addToFrom
     * @return
     * @throws Exception
     */
    private String getSelectLotes( String patron, Object[] filtro,  Sesion userSesion, 
    		String addToFrom) throws Exception {
    	
    	String sSQL="SELECT lote.id_lote, lote.nombre_lote, lote.fecha_alta, lote.fecha_baja, lote.fecha_ultima_modificacion, "+ 
		  "	lote.descripcion, lote.destino, lote.id_seguro,bi.id as bi_id, bi.numinventario as bi_numinventario , bi.tipo as bi_tipo, " +
    		  "bi.descripcion as bi_descripcion, bi.nombre as bi_nombre, bi.fecha_alta as bi_fecha_alta, " +
    		  "bi.fecha_baja as bi_fecha_baja , bi.fecha_ultima_modificacion as bi_fecha_ultima_modificacion, "+
    		  "bi.bloqueado as bi_bloqueado, bi.uso as bi_uso, bi.revision_actual as bi_revision_actual "+
    		  " FROM lote left outer join lote_bien  on lote.id_lote=lote_bien.id_lote left outer join "+ 
    		  "	bienes_inventario as bi on lote_bien.id_bien= bi.id ";
		sSQL+=" where lote.id_municipio='"+municipio+"' " ;
		sSQL+=sRevisionExpiradaNula;
		
		//Cuando el lote no puede localizar los elementos porque no existen no aparecen
		//porque la revision expirada del bien no existe;
		//ASO 11-01-2011 --> la revision_expirada nunca es null (esto lo que hace
		//es machacarme la condición posterior) lo comento. 
		//sSQL+=" or revision_expirada is null ";
		
    	
    	if (patron!=null)
    			sSQL+=" AND bi.tipo='"+patron+"' ";
    	/** en funcion del superpatron */  	
    	
    	if (filtro!=null){
    		for (int i=0;i<filtro.length;i++){
    			if (filtro[i] instanceof String){
    				sSQL+=filtro[i];
    			}else if (filtro[i] instanceof CampoFiltro){
    				CampoFiltro cf = (CampoFiltro)filtro[i];
    				if (cf.getTabla().equalsIgnoreCase("lote")){
    					sSQL+=addPartOfWhere(cf);
    				}
    			}
    		}
    	}
    
    	return sSQL;
    }
    
    /**
     * Devuelve el select de lotes
     * @param patron
     * @param filtro
     * @param userSesion
     * @param addToFrom
     * @return
     * @throws Exception
     */
    private String getSelectBusquedaLotes( String patron, String cadena,  Sesion userSesion, 
    		String addToFrom) throws Exception {
    	String sSQL= getSelectLotes(patron, null, userSesion, addToFrom);
    	
    	sSQL+= " and ( upper(lote.nombre_lote) like upper('%"+cadena+"%')";
    	sSQL+= " or upper(lote.seguro) like upper('%"+cadena+"%')";
    	sSQL+= " or upper(lote.descripcion) like upper('%"+cadena+"%')";
    	sSQL+= " or upper(lote.destino) like upper('%"+cadena+"%')";
    	sSQL+= " or upper(bi.numinventario) like upper('%"+cadena+"%')";
    	sSQL+= " or upper(bi.descripcion) like upper('%"+cadena+"%')";
    	sSQL+= " or upper(bi.nombre) like upper('%"+cadena+"%') )";
     	return sSQL;
    }


    private String addSQLSemoviente(String superpatron, String patron, String cadena, 
    		Object[] idLayers, Object[] idFeatures, Sesion userSesion) throws Exception{

        String sql= getSelectSemoviente(superpatron, patron, null, idLayers, idFeatures, userSesion, "") + " AND ";
        sql+= "(UPPER(bienes_inventario.numinventario) like UPPER('%"+cadena+"%') OR " +
             //"UPPER(bienes_inventario.fecha_alta) like UPPER('%"+cadena+"%') OR " +
             // "UPPER(bienes_inventario.fecha_baja) like UPPER('%"+cadena+"%') OR " +
             // "UPPER(bienes_inventario.fecha_ultima_modificacion) like UPPER('%"+cadena+"%') OR "+
              "UPPER(bienes_inventario.descripcion) like UPPER('%"+cadena+"%') OR "+
              "UPPER(bienes_inventario.nombre) like UPPER('%"+cadena+"%') OR "+
              "UPPER(bienes_inventario.organizacion) like UPPER('%"+cadena+"%') OR ";

//        sql+= "UPPER(semoviente.fecha_adquisicion) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(semoviente.identificacion) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(semoviente.descripcion) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(semoviente.destino) like UPPER('%"+cadena+"%') OR ";
       // sql+= "UPPER(semoviente.cantidad) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(semoviente.frutos) like UPPER('%"+cadena+"%') ";
        //OR ";
      //  sql+= "UPPER(semoviente.importe_frutos) like UPPER('%"+cadena+"%') OR ";
       // sql+= "UPPER(semoviente.coste_adquisicion) like UPPER('%"+cadena+"%') OR ";
      //  sql+= "UPPER(semoviente.valor_actual) like UPPER('%"+cadena+"%') " ;
        //		"OR ";
  //      sql+= "UPPER(semoviente.fecha_nacimiento) like UPPER('%"+cadena+"%')";
        sql+=")";
        return sql;
    }

    private String addSelectsBusquedaSemoviente(String superpatron, String patron, String cadena, 
    		Object[] idLayers, Object[] idFeatures, Sesion userSesion) throws Exception{
        if (cadena==null || cadena.trim().equalsIgnoreCase("")) return "";

        String sql= addSQLSemoviente(superpatron, patron, cadena, idLayers, idFeatures, userSesion);
        sql+= " UNION ";
        sql+= addSQLObservaciones(SQL_SEMOVIENTE, superpatron, patron, cadena, idLayers, idFeatures, userSesion);
        sql+= " UNION ";
        sql+= addSQLDocumentacion(SQL_SEMOVIENTE, superpatron, patron, cadena, idLayers, idFeatures, userSesion);
        sql+= " UNION ";
        sql+= addSQLContable(SQL_SEMOVIENTE, superpatron, patron, cadena, idLayers, idFeatures, userSesion);
        sql+= " UNION ";
        sql+= addSQLSegurosInventario(SQL_SEMOVIENTE, superpatron, patron, cadena, idLayers, idFeatures, userSesion);

        return sql;
    }

    private String getSelectVias(String superpatron, String patron, Object filtro[], Object[] idLayers,
Object[] idFeatures, Sesion userSesion, String addToFrom) throws Exception {
        
    	String sSQL="";

    	sSQL= "select bienes_inventario.numinventario, bienes_inventario.nombre as BIEN_NOMBRE, bienes_inventario.descripcion, " +
    			"bienes_inventario.uso, bienes_inventario.tipo,bienes_inventario.organizacion,bienes_inventario.fecha_alta, " +
    			"bienes_inventario.fecha_aprobacion_pleno, bienes_inventario.fecha_ultima_modificacion, bienes_inventario.id_cuenta_contable," +
    			"bienes_inventario.borrado,bienes_inventario.id_seguro, vias_inventario.* ,versiones.*,iuseruserhdr.name,tables_inventario.name ";
    	if (idFeatures.length > 0){		
    		sSQL+=" ,inventario_feature.revision_actual AS feature_revision_actual ";
    	}
    			
    	sSQL+= " from " + addToFrom + "vias_inventario inner join bienes_inventario on bienes_inventario.id=vias_inventario.id " +
    			" and bienes_inventario.revision_actual=vias_inventario.revision_actual ";
    	sSQL+= " inner join versiones on versiones.revision=vias_inventario.revision_actual ";
		sSQL+= " inner join iuseruserhdr on versiones.id_autor=iuseruserhdr.id ";
		sSQL+= " inner join tables_inventario on versiones.id_table_versionada=tables_inventario.id_table ";
    	
    	if (idFeatures.length > 0){
    		sSQL+= "inner join inventario_feature on bienes_inventario.id=inventario_feature.id_bien ";
    	}
    	
    	for(int i=0; i<idFeatures.length; i++){
    		
    		if (i==0) 
    			sSQL+= "AND inventario_feature.id_feature="+(String)idFeatures[i]+" AND " +
    			"inventario_feature.id_layer="+getIdLayer((String)idLayers[i]);
    		else 
    			sSQL+=" OR (inventario_feature.id_bien=bienes_inventario.id AND " +
    			"inventario_feature.id_feature="+(String)idFeatures[i]+" AND " +
    			"inventario_feature.id_layer="+getIdLayer((String)idLayers[i])+") ";
    	}

    	sSQL+=" WHERE bienes_inventario.id_municipio='"+municipio+"' AND bienes_inventario.tipo='"+patron+"' ";
    	/** en funcion del superpatron */  	
    	
    	if (superpatron!=null && superpatron.equalsIgnoreCase(Const.SUPERPATRON_PATRIMONIO_MUNICIPAL_SUELO))
            sSQL+="AND vias_inventario.patrimonio_municipal_suelo='1' ";
    	sSQL += sSQLVersionado.replaceAll("\\?", "vias_inventario");;
    	sSQL += " AND tables_inventario.name='vias_inventario'";

    	if ((filtro==null) || (filtro.length==0)) return sSQL;

    	return addFiltro(sSQL, SQL_VIAS, superpatron, patron, filtro, idLayers, idFeatures, userSesion);
    	
    }

    private String addSQLVias(String superpatron, String patron, String cadena, 
    		Object[] idLayers, Object[] idFeatures, Sesion userSesion) throws Exception{

        String sql= getSelectVias(superpatron, patron, null, idLayers, idFeatures, userSesion, "") + " AND ";
        sql+= "(UPPER(bienes_inventario.numinventario) like UPPER('%"+cadena+"%') OR " +
           //  "UPPER(bienes_inventario.fecha_alta) like UPPER('%"+cadena+"%') OR " +
           //   "UPPER(bienes_inventario.fecha_baja) like UPPER('%"+cadena+"%') OR " +
           //   "UPPER(bienes_inventario.fecha_ultima_modificacion) like UPPER('%"+cadena+"%') OR "+
              "UPPER(bienes_inventario.descripcion) like UPPER('%"+cadena+"%') OR "+
              "UPPER(bienes_inventario.nombre) like UPPER('%"+cadena+"%') OR "+
              "UPPER(bienes_inventario.organizacion) like UPPER('%"+cadena+"%') OR ";

        //sql+= "UPPER(vias_inventario.fecha_adquisicion) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(vias_inventario.categoria) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(vias_inventario.nombre) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(vias_inventario.inicio) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(vias_inventario.fin) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(vias_inventario.destino) like UPPER('%"+cadena+"%') " ;
        	//	"OR ";
      //  sql+= "UPPER(vias_inventario.num_apliques) like UPPER('%"+cadena+"%') OR ";
      //  sql+= "UPPER(vias_inventario.num_bancos) like UPPER('%"+cadena+"%') OR ";
      //  sql+= "UPPER(vias_inventario.num_papeleras) like UPPER('%"+cadena+"%') OR ";
      //  sql+= "UPPER(vias_inventario.metros_pavimentados) like UPPER('%"+cadena+"%') OR ";
      //  sql+= "UPPER(vias_inventario.metros_no_pavimentados) like UPPER('%"+cadena+"%') OR ";
      //  sql+= "UPPER(vias_inventario.zonas_verdes) like UPPER('%"+cadena+"%') OR ";
      //  sql+= "UPPER(vias_inventario.longitud) like UPPER('%"+cadena+"%') OR ";
      //  sql+= "UPPER(vias_inventario.ancho) like UPPER('%"+cadena+"%') OR ";
      //  sql+= "UPPER(vias_inventario.valor_actual) like UPPER('%"+cadena+"%')";
        sql+=")";
        return sql;
    }

    private String addSelectsBusquedaVias(String superpatron, String patron, String cadena, 
    		Object[] idLayers, Object[] idFeatures, Sesion userSesion) throws Exception{
        if (cadena==null || cadena.trim().equalsIgnoreCase("")) return "";

        String sql= addSQLVias(superpatron, patron, cadena, idLayers, idFeatures, userSesion);
        sql+= " UNION ";
        sql+= addSQLObservaciones(SQL_VIAS, superpatron, patron, cadena, idLayers, idFeatures, userSesion);
        sql+= " UNION ";
        sql+= addSQLDocumentacion(SQL_VIAS, superpatron, patron, cadena, idLayers, idFeatures, userSesion);
        sql+= " UNION ";
        sql+= addSQLContable(SQL_VIAS, superpatron, patron, cadena, idLayers, idFeatures, userSesion);
        sql+= " UNION ";
        sql+= addSQLSegurosInventario(SQL_VIAS, superpatron, patron, cadena, idLayers, idFeatures, userSesion);
        sql+= " UNION ";
        sql+= addSQLMejoras(SQL_VIAS, superpatron, patron, cadena, idLayers, idFeatures, userSesion);

        return sql;
    }

    private String getSelectVehiculo(String superpatron, String patron, Object[] filtro, Object[] idLayers, 
    		Object[] idFeatures, Sesion userSesion, String addToFrom) throws Exception {
            	
    	String sSQL="";

    	sSQL= "select bienes_inventario.numinventario, bienes_inventario.nombre, bienes_inventario.descripcion, " +
    			"bienes_inventario.uso, bienes_inventario.tipo AS tipo_bien,bienes_inventario.organizacion,bienes_inventario.fecha_alta," +
    			"bienes_inventario.fecha_aprobacion_pleno, bienes_inventario.fecha_ultima_modificacion, " +
    			"bienes_inventario.id_cuenta_contable,bienes_inventario.id_cuenta_amortizacion, bienes_inventario.id_seguro," +
    			"bienes_inventario.borrado,vehiculo.tipo as tipo_vehiculo, vehiculo.*,versiones.*,iuseruserhdr.name,tables_inventario.name ";
    	if (idFeatures.length > 0){		
    		sSQL+=" ,inventario_feature.revision_actual AS feature_revision_actual ";
    	}
    	sSQL+= " from " + addToFrom + "vehiculo inner join bienes_inventario on bienes_inventario.id=vehiculo.id ";
    	sSQL+= " and bienes_inventario.revision_actual=vehiculo.revision_actual ";
		sSQL+= " inner join versiones on versiones.revision=vehiculo.revision_actual ";
		sSQL+= " inner join iuseruserhdr on versiones.id_autor=iuseruserhdr.id ";
		sSQL+= " inner join tables_inventario on versiones.id_table_versionada=tables_inventario.id_table ";
    	
    	if (idFeatures.length > 0){
    		sSQL+= "inner join inventario_feature on bienes_inventario.id=inventario_feature.id_bien ";
    	}
    	
    	for(int i=0; i<idFeatures.length; i++){
    		
    		if (i==0) 
    			sSQL+= " AND inventario_feature.id_feature="+(String)idFeatures[i]+" AND " +
    			"inventario_feature.id_layer="+getIdLayer((String)idLayers[i]);
    		else 
    			sSQL+=" OR (inventario_feature.id_bien=bienes_inventario.id AND " +
    			"inventario_feature.id_feature="+(String)idFeatures[i]+" AND " +
    			"inventario_feature.id_layer="+getIdLayer((String)idLayers[i])+") ";
    	}


    	sSQL+=" WHERE bienes_inventario.id_municipio='"+municipio+"' AND bienes_inventario.tipo='"+patron+"' ";
    	/** en funcion del superpatron */  	
    	
    	if (superpatron!=null && superpatron.equalsIgnoreCase(Const.SUPERPATRON_REVERTIBLES))
            sSQL+="AND vehiculo.propiedad='"+Const.PATRON_CEDIDO+"' ";
        else if (superpatron!= null && superpatron.equalsIgnoreCase(Const.SUPERPATRON_PATRIMONIO_MUNICIPAL_SUELO))
            sSQL+="AND vehiculo.patrimonio_municipal_suelo='1' ";

    	sSQL += sSQLVersionado.replaceAll("\\?", "vehiculo");
    	sSQL += " AND tables_inventario.name='vehiculo'";
    	if ((filtro==null) || (filtro.length==0)) return sSQL;

    	return addFiltro(sSQL, SQL_VEHICULOS, superpatron, patron, filtro, idLayers, idFeatures, userSesion);
    	
    }

    private String addSQLVehiculo(String superpatron, String patron, String cadena, 
    		Object[] idLayers, Object[] idFeatures, Sesion userSesion) throws Exception{

        String sql= getSelectVehiculo(superpatron, patron, null, idLayers, idFeatures, userSesion, "") + " AND ";
        sql+= "(UPPER(bienes_inventario.numinventario) like UPPER('%"+cadena+"%') OR " +
            //  "UPPER(bienes_inventario.fecha_alta) like UPPER('%"+cadena+"%') OR " +
            //  "UPPER(bienes_inventario.fecha_baja) like UPPER('%"+cadena+"%') OR " +
            //  "UPPER(bienes_inventario.fecha_ultima_modificacion) like UPPER('%"+cadena+"%') OR "+
              "UPPER(bienes_inventario.descripcion) like UPPER('%"+cadena+"%') OR "+
              "UPPER(bienes_inventario.nombre) like UPPER('%"+cadena+"%') OR "+
              "UPPER(bienes_inventario.organizacion) like UPPER('%"+cadena+"%') OR ";

//        sql+= "UPPER(vehiculo.fecha_adquisicion) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(vehiculo.matricula_vieja) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(vehiculo.matricula_nueva) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(vehiculo.num_bastidor) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(vehiculo.marca) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(vehiculo.motor) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(vehiculo.fuerza) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(vehiculo.servicio) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(vehiculo.destino) like UPPER('%"+cadena+"%') OR ";
        sql+= "UPPER(vehiculo.frutos) like UPPER('%"+cadena+"%') "; //"OR ";
        //sql+= "UPPER(vehiculo.importe_frutos) like UPPER('%"+cadena+"%') OR ";
       // sql+= "UPPER(vehiculo.coste_adquisicion) like UPPER('%"+cadena+"%') OR ";
       // sql+= "UPPER(vehiculo.valor_actual) like UPPER('%"+cadena+"%')";
        sql+=")";
        return sql;
    }

    private String addSelectsBusquedaVehiculo(String superpatron, String patron, String cadena, 
    		Object[] idLayers, Object[] idFeatures, Sesion userSesion) throws Exception{
        if (cadena==null || cadena.trim().equalsIgnoreCase("")) return "";

        String sql= addSQLVehiculo(superpatron, patron, cadena, idLayers, idFeatures, userSesion);
        sql+= " UNION ";
        sql+= addSQLObservaciones(SQL_VEHICULOS, superpatron, patron, cadena, idLayers, idFeatures, userSesion);
        sql+= " UNION ";
        sql+= addSQLDocumentacion(SQL_VEHICULOS, superpatron, patron, cadena, idLayers, idFeatures, userSesion);
        sql+= " UNION ";
        sql+= addSQLContable(SQL_VEHICULOS, superpatron, patron, cadena, idLayers, idFeatures, userSesion);
        sql+= " UNION ";
        sql+= addSQLSegurosInventario(SQL_VEHICULOS, superpatron, patron, cadena, idLayers, idFeatures, userSesion);
        sql+= " UNION ";
        sql+= addSQLAmortizacion(SQL_VEHICULOS, superpatron, patron, cadena, idLayers, idFeatures, userSesion);


        return sql;
    }


    private String addFiltro(String sSQL, int tipoSQL, String superpatron, String patron, 
    		Object[] filtro, Object[] idLayers, Object[] idFeatures, Sesion userSesion) throws Exception{
        if (filtro==null || filtro.length==0) return sSQL;

        ArrayList filtroSeguros= new ArrayList();
        ArrayList filtroMejoras= new ArrayList();
        ArrayList filtroObservaciones= new ArrayList();
        ArrayList filtroCContable= new ArrayList();
        ArrayList filtroCAmortizacion= new ArrayList();
        ArrayList filtroUsosFuncionales= new ArrayList();
        ArrayList filtroRefCatastrales= new ArrayList();
        ArrayList filtroDocumentacion= new ArrayList();

        for (int i=0; i<filtro.length; i++){
            CampoFiltro cf= (CampoFiltro)filtro[i];
            if (cf.getTabla().equalsIgnoreCase("bienes_inventario") ||
                ((tipoSQL==SQL_INMUEBLE_URBANO || tipoSQL==SQL_INMUEBLE_RUSTICO) && cf.getTabla().equalsIgnoreCase("inmuebles")) ||
                (tipoSQL==SQL_INMUEBLE_URBANO && cf.getTabla().equalsIgnoreCase("inmuebles_urbanos")) ||
                (tipoSQL==SQL_INMUEBLE_RUSTICO && cf.getTabla().equalsIgnoreCase("inmuebles_rusticos")) ||
                (tipoSQL==SQL_MUEBLE && cf.getTabla().equalsIgnoreCase("muebles")) ||
                (tipoSQL==SQL_DERECHOS_REALES && cf.getTabla().equalsIgnoreCase("derechos_reales")) ||
                (tipoSQL==SQL_CREDITO_DERECHO && cf.getTabla().equalsIgnoreCase("credito_derecho")) ||
                (tipoSQL==SQL_VALOR_MOBILIARIO && cf.getTabla().equalsIgnoreCase("valor_mobiliario")) ||
                (tipoSQL==SQL_VEHICULOS && cf.getTabla().equalsIgnoreCase("vehiculo")) ||
                (tipoSQL==SQL_VIAS && cf.getTabla().equalsIgnoreCase("vias_inventario")) ||
                (tipoSQL==SQL_SEMOVIENTE && cf.getTabla().equalsIgnoreCase("semoviente"))){
                sSQL+= addPartOfWhere(cf);
            }else if (cf.getTabla().equalsIgnoreCase("seguros_inventario") || cf.getTabla().equalsIgnoreCase("compannia_seguros")){
                filtroSeguros.add(cf);
            }else if (cf.getTabla().equalsIgnoreCase("contable")){
                filtroCContable.add(cf);
            }else if (cf.getTabla().equalsIgnoreCase("amortizacion")){
                filtroCAmortizacion.add(cf);
            }else if (cf.getTabla().equalsIgnoreCase("mejoras_inventario")){
                filtroMejoras.add(cf);
            }else if (cf.getTabla().equalsIgnoreCase("usos_funcionales_inventario")){
                filtroUsosFuncionales.add(cf);
            }else if (cf.getTabla().equalsIgnoreCase("observaciones_inventario")){
                filtroObservaciones.add(cf);
            }else if (cf.getTabla().equalsIgnoreCase("refcatastrales_inventario")){
                filtroRefCatastrales.add(cf);
            }else if (cf.getTabla().equalsIgnoreCase("documento")){
                filtroDocumentacion.add(cf);
            }

        }

        if (filtroSeguros.size()>0){
            sSQL+= addIntersecSQL(addSQLSegurosInventario((tipoSQL==SQL_INMUEBLE_URBANO || tipoSQL==SQL_INMUEBLE_RUSTICO)?SQL_INMUEBLE:tipoSQL, superpatron, patron, filtroSeguros.toArray(), idLayers, idFeatures, userSesion));
        }
        if (filtroCContable.size()>0){
            sSQL+= addIntersecSQL(addSQLContable((tipoSQL==SQL_INMUEBLE_URBANO || tipoSQL==SQL_INMUEBLE_RUSTICO)?SQL_INMUEBLE:tipoSQL, superpatron, patron, filtroCContable.toArray(), idLayers, idFeatures, userSesion));
        }
        if (filtroCAmortizacion.size()>0){
            sSQL+= addIntersecSQL(addSQLAmortizacion((tipoSQL==SQL_INMUEBLE_URBANO || tipoSQL==SQL_INMUEBLE_RUSTICO)?SQL_INMUEBLE:tipoSQL, superpatron, patron, filtroCAmortizacion.toArray(), idLayers, idFeatures, userSesion));
        }
        if (filtroMejoras.size()>0){
            sSQL+= addIntersecSQL(addSQLMejoras((tipoSQL==SQL_INMUEBLE_URBANO || tipoSQL==SQL_INMUEBLE_RUSTICO)?SQL_INMUEBLE:tipoSQL, superpatron, patron, filtroMejoras.toArray(), idLayers, idFeatures, userSesion));
        }
        if (filtroUsosFuncionales.size()>0){
            sSQL+= addIntersecSQL(addSQLUsosFuncionales((tipoSQL==SQL_INMUEBLE_URBANO || tipoSQL==SQL_INMUEBLE_RUSTICO)?SQL_INMUEBLE:tipoSQL, superpatron, patron, filtroUsosFuncionales.toArray(), idLayers, idFeatures, userSesion));
        }
        if (filtroObservaciones.size()>0){
            sSQL+= addIntersecSQL(addSQLObservaciones((tipoSQL==SQL_INMUEBLE_URBANO || tipoSQL==SQL_INMUEBLE_RUSTICO)?SQL_INMUEBLE:tipoSQL, superpatron, patron, filtroObservaciones.toArray(), idLayers, idFeatures, userSesion));
        }
        if (filtroRefCatastrales.size()>0){
            sSQL+= addIntersecSQL(addSQLRefCatastrales((tipoSQL==SQL_INMUEBLE_URBANO || tipoSQL==SQL_INMUEBLE_RUSTICO)?SQL_INMUEBLE:tipoSQL, superpatron, patron, filtroRefCatastrales.toArray(), idLayers, idFeatures, userSesion));
        }
        if (filtroDocumentacion.size()>0){
            sSQL+= addIntersecSQL(addSQLDocumentacion((tipoSQL==SQL_INMUEBLE_URBANO || tipoSQL==SQL_INMUEBLE_RUSTICO)?SQL_INMUEBLE:tipoSQL, superpatron, patron, filtroDocumentacion.toArray(), idLayers, idFeatures, userSesion));
        }

        return sSQL;
    }

    private String addIntersecSQL(String sql) throws Exception{
        if (sql!=null && !sql.equalsIgnoreCase("")){
            return " INTERSECT "+sql;
        }else return "";
    }

    private String addPartOfWhere(CampoFiltro cf) throws Exception {
        if (cf.isVarchar() || cf.isDominio()){
        	try {
        		Long aux= new Long(cf.getValorVarchar());
          		return " AND "+getAliasTabla(cf.getTabla())+"."+cf.getNombre()+" "+cf.getOperador()+" '"+cf.getValorVarchar() +"'";
          	  
        	}catch (Exception ex){
        		return " AND UPPER("+getAliasTabla(cf.getTabla())+"."+cf.getNombre()+") "+cf.getOperador()+" UPPER('"+cf.getValorVarchar() +"')";
        	}
        }else if (cf.isBoolean()){
            return " AND "+getAliasTabla(cf.getTabla())+"."+cf.getNombre()+cf.getOperador()+"'"+(cf.getValorBoolean()?1:0)+"'";
        }else if (cf.isDate()){
            /** El formato de la fecha del campo de la tabla tiene que ser yyyy-MM-dd o de lo contrario no funciona */
           // return " AND to_date("+getAliasTabla(cf.getTabla())+"."+cf.getNombre()+", 'yyyy-MM-dd') "+cf.getOperador()+" to_date('"+cf.getValorVarchar()+"', '"+Const.pattern+"')";
        	return " AND "+getAliasTabla(cf.getTabla())+"."+cf.getNombre()+" "+cf.getOperador()+" to_date('"+cf.getValorVarchar()+"', '"+Const.pattern+"')";
        }else if (cf.isNumeric()){
            return " AND "+getAliasTabla(cf.getTabla())+"."+cf.getNombre()+cf.getOperador()+cf.getValorNumeric();
        }else if (cf.isDouble()){
    		return " AND "+getAliasTabla(cf.getTabla())+"."+cf.getNombre()+cf.getOperador()+cf.getValorDouble();
        
    	}else if (cf.isCompuesto()){
            return " AND "+cf.getValorCompuesto();
    	}
        return "";
    }

    private String getAliasTabla(String tabla){
        if (tabla.equalsIgnoreCase("bienes_inventario")) return "bienes_inventario";
        if (tabla.equalsIgnoreCase("inmuebles")) return "inmuebles";
        if (tabla.equalsIgnoreCase("inmuebles_urbanos")) return "inmuebles_urbanos";
        if (tabla.equalsIgnoreCase("inmuebles_rusticos")) return "inmuebles_rusticos";
        if (tabla.equalsIgnoreCase("seguros_inventario")) return "seguros_inventario";
        if (tabla.equalsIgnoreCase("compannia_seguros")) return "compannia_seguros";
        if (tabla.equalsIgnoreCase("contable")) return "contable";
        if (tabla.equalsIgnoreCase("amortizacion")) return "amortizacion";
        if (tabla.equalsIgnoreCase("mejoras_inventario")) return "mejoras_inventario";
        if (tabla.equalsIgnoreCase("usos_funcionales_inventario")) return "usos_funcionales_inventario";
        if (tabla.equalsIgnoreCase("observaciones_inventario")) return "observaciones_inventario";
        if (tabla.equalsIgnoreCase("refcatastrales_inventario")) return "refcatastrales_inventario";
        if (tabla.equalsIgnoreCase("anexo_inventario")) return "anexo_inventario";
        if (tabla.equalsIgnoreCase("documento")) return "documento";
        if (tabla.equalsIgnoreCase("credito_derecho")) return "credito_derecho";
        if (tabla.equalsIgnoreCase("vehiculo")) return "vehiculo";
        if (tabla.equalsIgnoreCase("derechos_reales")) return "derechos_reales";
        if (tabla.equalsIgnoreCase("semoviente")) return "semoviente";
        if (tabla.equalsIgnoreCase("muebles")) return "muebles";
        if (tabla.equalsIgnoreCase("vias_inventario")) return "vias_inventario";
        if (tabla.equalsIgnoreCase("valor_mobiliario")) return "valor_mobiliario";

        return tabla;

    }

    /**
     * Retorna las plantillas para la generacion de informes
     * @param oos
     * @param path
     * @throws Exception
     */
    public void returnPlantillas(ObjectOutputStream oos, String path) throws Exception{
           try{
               for (Iterator it=getPlantillas(path).iterator();it.hasNext();){
                   oos.writeObject(it.next());
               }
           }catch(Exception e){
               logger.error("returnPlantillas: "+ e.getMessage());
               oos.writeObject(new ACException(e));
               throw e;
           }
    }

    private Collection getPlantillas(String path) throws Exception{

        ArrayList aList= new ArrayList();
        ArrayList plantillas= new ArrayList();
        Object[] plantilla;
        ArrayList imagenes= new ArrayList();
        Object[] imagen;

        LinkedList imgDirectories = new LinkedList();
        /** Leemos las imagenes. TODAS las imagenes se encuentran en el mismo directorio */
        File dir = new File(Constantes.PATH_PLANTILLAS_INVENTARIO + File.separator + "img");
        imgDirectories.add(dir);
        
        while(!imgDirectories.isEmpty()){
        	File tempdir = (File) imgDirectories.poll();
        	File[] children = tempdir.listFiles();
            if (children != null) {
            	for (int i = 0; i < children.length; i++) {
	                File file= children[i];
	                
	                if (file.isDirectory()){
	                	imgDirectories.addLast(file);
	                    continue;
	                }
	                
	                imagen= new Object[2];
	                imagen[0]= file.getName();
	                imagen[1]= DocumentoEnDisco.getContenido(file);
	                imagenes.add(imagen);
            	}
            }
        }
        /*old way 
        if (dir.isDirectory()) {
            File[] children = dir.listFiles();
            if (children == null) {
                // Either dir does not exist or is not a directory
            } else {
                for (int i = 0; i < children.length; i++) {
                    // Get filename of file or directory
                    File file= children[i];
                    if (file.isDirectory()){
                    	imgDirectories.add(file);
                    	continue;
                    }
                    imagen= new Object[2];
                    imagen[0]= file.getName();
                    imagen[1]= DocumentoEnDisco.getContenido(file);
                    imagenes.add(imagen);
                }
            }
        }*/
        /** El primer elemento del array correspode a las imagenes, el segundo a las plantillas .jrxml */
        aList.add(imagenes);

        /** Leemos las plantillas */
        /** filtramos por ficheros con extension .jrxml */
        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".jrxml");
            }
        };
        /** Plantillas Globales (para todos los tipos) excepto para lotes y bienes revertibles */
        if (path.toUpperCase().indexOf("LOTES")<0 && path.toUpperCase().indexOf("REVERTIBLES")<0){
	        dir = new File(Constantes.PATH_PLANTILLAS_INVENTARIO);
	        if (dir.isDirectory()) {
	            File[] children = dir.listFiles(filter);
	            if (children == null) {
	                // Either dir does not exist or is not a directory
	            } else {
	                for (int i = 0; i < children.length; i++) {
	                    // Get filename of file or directory
	                    File file= children[i];
	                    plantilla= new Object[2];
	                    plantilla[0]= file.getName();
	                    plantilla[1]= DocumentoEnDisco.getContenido(file);
	                    plantillas.add(plantilla);
	                }
	            }
	        }
         }   
        /** Plantillas del tipo seleccionado por el usuario */
        dir = new File(path);
        if (dir.isDirectory()) {
            File[] children = dir.listFiles(filter);
            if (children == null) {
                // Either dir does not exist or is not a directory
            } else {
                for (int i = 0; i < children.length; i++) {
                    // Get filename of file or directory
                    File file= children[i];
                    plantilla= new Object[2];
                    plantilla[0]= file.getName();
                    plantilla[1]= DocumentoEnDisco.getContenido(file);
                    plantillas.add(plantilla);
                }
            }
        }
        /** Insertamos las plantillas en el segundo elemento del array */
        aList.add(plantillas);

        aList.add(getSubReports());

        return aList;
    }

    private Collection getSubReports() throws Exception{

        ArrayList subreports= new ArrayList();
        Object[] subreport;

        /** filtramos por ficheros con extension .jrxml */
        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".jrxml");
            }
        };

        /** Leemos los subreports. TODOS los subreports se encuentran en el mismo directorio */
        File dir = new File(Constantes.PATH_PLANTILLAS_INVENTARIO + File.separator + "subreports");
        if (dir.isDirectory()) {
            File[] children = dir.listFiles(filter);
            if (children == null) {
                // Either dir does not exist or is not a directory
            } else {
                for (int i = 0; i < children.length; i++) {
                    // Get filename of file or directory
                    File file= children[i];
                    subreport= new Object[2];

                    subreport[0]= file.getName();
                    subreport[1]= DocumentoEnDisco.getContenido(file);
                    subreports.add(subreport);
                }
            }
        }

        return subreports;
    }


    public Collection returnReferanciasCatastrales(String patron) throws Exception
    {
        ArrayList referenciasCatastrales= new ArrayList();
        
        String sSQL= "select parcelas.referencia_catastral, parcelas.primer_numero, parcelas.primera_letra, parcelas.codigopoligono," +
                " parcelas.codigoparcela, parcelas.id_via, parcelas.bloque, parcelas.codigo_postal from parcelas where" +
                " parcelas.referencia_catastral IS NOT NULL and parcelas.id_municipio= '"+ municipio+"' "
                +" and ((parcelas.referencia_catastral like upper('%" + patron + "%')) or ( parcelas.referencia_catastral is null)) "
                + " order by parcelas.referencia_catastral asc";
        PreparedStatement ps= null;
        Connection conn= null;
        ResultSet rs= null;
        try
        {
            conn= CPoolDatabase.getConnection();
            ps= conn.prepareStatement(sSQL);
            rs= ps.executeQuery();
            while(rs.next())
            {
            	CReferenciaCatastral referenciaCatastral = new CReferenciaCatastral();
				            	
				referenciaCatastral.setReferenciaCatastral(rs.getString("referencia_catastral"));
				referenciaCatastral.setBloque(rs.getString("bloque"));
				referenciaCatastral.setPrimeraLetra(rs.getString("primera_letra"));
				referenciaCatastral.setPrimerNumero(rs.getString("primer_numero"));
				referenciaCatastral.setCPostal(rs.getInt("codigo_postal")!=0?new Integer(rs.getInt("codigo_postal")).toString():"");
                
                
                int idVia = -1;
                idVia = TypeUtil.getSimpleInteger(rs,"id_via");
                ResultSet rsVia= null;                
                try
                {
                	if(idVia!=-1){
	                    sSQL = "select vias.tipovianormalizadocatastro, vias.nombrecatastro from vias where codigocatastro=" + idVia
	                            + " and id_municipio="+Integer.parseInt(municipio);
	                    ps= conn.prepareStatement(sSQL);
	                    rsVia= ps.executeQuery();
	                    if(rsVia.next())
	                    {
	                    	referenciaCatastral.setNombreVia(rsVia.getString("nombrecatastro"));
	                        referenciaCatastral.setTipoVia(rsVia.getString("tipovianormalizadocatastro"));
	                    }
                	}
                }catch(Exception ex){
                	logger.error("Error al ejecutar: "+sSQL);
                	throw ex;
                }finally
                {
                    try{rsVia.close();}catch(Exception e){};
                }
                
                referenciasCatastrales.add(referenciaCatastral);
            }
        }
        catch (Exception e)
        {
            throw e;
        }
        finally
        {
            try{ps.close();}catch(Exception e){};
            try{rs.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
        }
        return referenciasCatastrales;
    }

    public void returnReferanciasCatastrales(ObjectOutputStream oos,  String patron) throws Exception
    {
       try
       {
           for(Iterator it= returnReferanciasCatastrales(patron).iterator();it.hasNext();)
           {
                oos.writeObject((CReferenciaCatastral)it.next());
           }
       }
       catch(Exception e)
       {
           logger.error("getDireccionRefCatastralBuscadas: "+ e.getMessage());
           oos.writeObject(new ACException(e));
           throw e;
       }
    }
    
    /**
     * Compruebo si el bien está dado de baja o no
     */
    private boolean estaActivoBien(long revisionExpirada){
    	return revisionExpirada == Long.parseLong("9999999999");
    }

    /**
     * Retorna los bienes revertivle del inventario
     * @param oos buffer donde inserta el resultado de la operacion
     * @param patron tipo del bien de inventario
     * @param idLayers
     * @param idFeatures
     * @param userSesion sesion de usuario
     * @throws Exception
     */
    public void returnBienesRevertibles(ObjectOutputStream oos, String patron, String cadena, 
    		Object[] filtro, Object[] idLayers, Object[] idFeatures, Sesion userSesion,ConfigParameters configParameters) throws Exception{
           try{
               for (Iterator it=getBienesRevertibles(patron, cadena, filtro,  null,userSesion, true,configParameters).iterator();it.hasNext();){
                   oos.writeObject((BienRevertible)it.next());
               }
           }catch(Exception e){
               logger.error("returnBienesRevertibles: "+ e.getMessage());
               oos.writeObject(new ACException(e));
               throw e;
           }
    }
    
    /**
     * Retorna una lista generica de bienes del inventario
     * @param oos buffer donde inserta el resultado de la operacion
     * @param patron tipo del bien de inventario
     * @param idLayers
     * @param idFeatures
     * @param userSesion sesion de usuario
     * @throws Exception
     */
    public void returnBienes(ObjectOutputStream oos, String superpatron,String cadena, 
    		Object[] filtro,Object[] idLayers, Object[] idFeatures) throws Exception{
           try{
              for (Iterator it=getBienes(superpatron,cadena, filtro,idLayers,idFeatures).iterator();it.hasNext();){
                   oos.writeObject((BienBean)it.next());
               }
           }catch(Exception e){
               logger.error("returnBienes: "+ e.getMessage());
               oos.writeObject(new ACException(e));
               throw e;
           }
    }
    /**
     * Devuelve los lotes
     * @param oos
     * @param patron
     * @param cadena
     * @param filtro
     * @param idLayers
     * @param idFeatures
     * @param userSesion
     * @throws Exception
     */
    public void returnLotes(ObjectOutputStream oos, String patron, String cadena, 
    		Object[] filtro, Object[] idLayers, Object[] idFeatures, Sesion userSesion,ConfigParameters configParameters) throws Exception{
           try{
               for (Iterator it=getLotes(patron, cadena, filtro,  null,userSesion,configParameters).iterator();it.hasNext();){
                   oos.writeObject((Lote)it.next());
               }
           }catch(Exception e){
               logger.error("returnLotes: "+ e.getMessage());
               oos.writeObject(new ACException(e));
               throw e;
           }
    }
    
    private String getSelectNumInventario(String patron, Object[] numInventarios,int id_table_versionada) throws Exception {
            	
    	String sSQL="";
    	
    	//Si el numero de inventarios esta vacio simplemente obtenemos el inventario
    	//actual (el ultimo) sin recuperar las versiones
    	if ((numInventarios==null) || (numInventarios.length==0)){
    	//if ((numInventarios==null) || (numInventarios.length<=1)){
        	sSQL+=" and (bienes_inventario.revision_expirada=9999999999 ";
        	
        	//Con esta query tambien sacamos los eliminados completamente.
        	sSQL+=" or (bienes_inventario.borrado='2' and bienes_inventario.revision_actual=bienes_inventario.revision_expirada))";

        	sSQL+=" and versiones.id_table_versionada="+id_table_versionada;
        	//Para los inmuebles (tables_inventario=12003) incluimos una condicion mas
        	if (id_table_versionada==12003){
        		if (patron.equalsIgnoreCase(Const.PATRON_INMUEBLES_URBANOS)){
                	sSQL+="  and inmuebles.revision_actual=inmuebles_urbanos.revision_actual";
            	}
            	else if (patron.equalsIgnoreCase(Const.PATRON_INMUEBLES_RUSTICOS)){
                	sSQL+="  and inmuebles.revision_actual=inmuebles_rusticos.revision_actual";
            	}
        	}
    		return sSQL;
    	}
    	
    	//sSQL+=" and bienes_inventario.revision_expirada!=9999999999 ";
    	sSQL+=" and bienes_inventario.revision_expirada!=9999999999 ";
    	sSQL+=" and versiones.id_table_versionada="+id_table_versionada;
    	if (id_table_versionada==12003){
    		if (patron.equalsIgnoreCase(Const.PATRON_INMUEBLES_URBANOS)){
            	sSQL+="  and inmuebles.revision_actual=inmuebles_urbanos.revision_actual";
        	}
        	else if (patron.equalsIgnoreCase(Const.PATRON_INMUEBLES_RUSTICOS)){
            	sSQL+="  and inmuebles.revision_actual=inmuebles_rusticos.revision_actual";
        	}
    	}
    	sSQL+=" and bienes_inventario.numinventario in ( ";
    	for(int i=0; i<numInventarios.length; i++){
    		if (i==0) 	
    			sSQL+= "'"+(String)numInventarios[i]+"'";
    		else
    			sSQL+= ","+"'"+(String)numInventarios[i]+"'";
    	}
    	sSQL+=")";
    	
    	return sSQL;
    }
    
    private String getSelectNumInventarioRevertible(Object[] numInventarios) throws Exception {
    	
    	String sSQL="";
    	
    	//Si el numero de inventarios esta vacio simplemente obtenemos el inventario
    	//actual (el ultimo) sin recuperar las versiones
    	if ((numInventarios==null) || (numInventarios.length==0)){
        	sSQL+=" and (bien_revertible.revision_expirada=9999999999 ";
        	
        	//Con esta query tambien sacamos los eliminados completamente.
        	sSQL+=" or (bien_revertible.borrado='2' and bien_revertible.revision_actual=bien_revertible.revision_expirada))";

        	/*sSQL+=" and versiones.id_table_versionada="+id_table_versionada;
        	//Para los inmuebles (tables_inventario=12003) incluimos una condicion mas
        	if (id_table_versionada==12003){
        		if (patron.equalsIgnoreCase(Const.PATRON_INMUEBLES_URBANOS)){
                	sSQL+="  and inmuebles.revision_actual=inmuebles_urbanos.revision_actual";
            	}
            	else if (patron.equalsIgnoreCase(Const.PATRON_INMUEBLES_RUSTICOS)){
                	sSQL+="  and inmuebles.revision_actual=inmuebles_rusticos.revision_actual";
            	}
        	}*/
    		return sSQL;
    	}
    	
    	sSQL+=" and bien_revertible.revision_expirada!=9999999999 ";
    	/*sSQL+=" and versiones.id_table_versionada="+id_table_versionada;
    	if (id_table_versionada==12003){
    		if (patron.equalsIgnoreCase(Const.PATRON_INMUEBLES_URBANOS)){
            	sSQL+="  and inmuebles.revision_actual=inmuebles_urbanos.revision_actual";
        	}
        	else if (patron.equalsIgnoreCase(Const.PATRON_INMUEBLES_RUSTICOS)){
            	sSQL+="  and inmuebles.revision_actual=inmuebles_rusticos.revision_actual";
        	}
    	}
    	*/
    	sSQL+=" and bien_revertible.num_inventario in ( ";
    	for(int i=0; i<numInventarios.length; i++){
    		if (i==0) 	
    			sSQL+= "'"+(String)numInventarios[i]+"'";
    		else
    			sSQL+= ","+"'"+(String)numInventarios[i]+"'";
    	}
    	sSQL+=")";
    	
    	return sSQL;
    }
    
    private String getSelectNumInventarioLotes(String patron, Object[] numInventarios,int id_table_versionada) throws Exception {
    	
    	String sSQL="";
    	
    	//Si el numero de inventarios esta vacio simplemente obtenemos el inventario
    	//actual (el ultimo) sin recuperar las versiones
    	/*if ((numInventarios==null) || (numInventarios.length==0)){
        	sSQL+=" and (bienes_inventario.revision_expirada=9999999999 ";
        	
        	//Con esta query tambien sacamos los eliminados completamente.
        	sSQL+=" or (bienes_inventario.borrado=2 and bienes_inventario.revision_actual=bienes_inventario.revision_expirada))";

        	sSQL+=" and versiones.id_table_versionada="+id_table_versionada;
        	//Para los inmuebles (tables_inventario=12003) incluimos una condicion mas
        	if (id_table_versionada==12003){
        		if (patron.equalsIgnoreCase(Const.PATRON_INMUEBLES_URBANOS)){
                	sSQL+="  and inmuebles.revision_actual=inmuebles_urbanos.revision_actual";
            	}
            	else if (patron.equalsIgnoreCase(Const.PATRON_INMUEBLES_RUSTICOS)){
                	sSQL+="  and inmuebles.revision_actual=inmuebles_rusticos.revision_actual";
            	}
        	}
    		return sSQL;
    	}
    	
    	//sSQL+=" and bienes_inventario.revision_expirada!=9999999999 ";
    	sSQL+=" and bienes_inventario.revision_expirada!=9999999999 ";
    	sSQL+=" and versiones.id_table_versionada="+id_table_versionada;
    	if (id_table_versionada==12003){
    		if (patron.equalsIgnoreCase(Const.PATRON_INMUEBLES_URBANOS)){
            	sSQL+="  and inmuebles.revision_actual=inmuebles_urbanos.revision_actual";
        	}
        	else if (patron.equalsIgnoreCase(Const.PATRON_INMUEBLES_RUSTICOS)){
            	sSQL+="  and inmuebles.revision_actual=inmuebles_rusticos.revision_actual";
        	}
    	}
    	sSQL+=" and bienes_inventario.numinventario in ( ";
    	for(int i=0; i<numInventarios.length; i++){
    		if (i==0) 	
    			sSQL+= "'"+(String)numInventarios[i]+"'";
    		else
    			sSQL+= ","+"'"+(String)numInventarios[i]+"'";
    	}
    	sSQL+=")";
    	*/
    	return sSQL;
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
    	String sSQL ="";
   
    	try{
    		conn = CPoolDatabase.getConnection();
    		sSQL = "SELECT TO_CHAR(localtimestamp, 'HH24:MI:ss')";
    		ps = conn.prepareStatement(sSQL);
    		rs = ps.executeQuery();
    		if (rs.next())
    			hora = rs.getString(1);
    	
    	}catch(Exception ex){
        	logger.error("Error al ejecutar: "+sSQL);
        	throw ex;
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
            oos.writeObject(hora);
	    }
    }
    public static void main(String[] arg){
    	BienBean bien= new MuebleBean();
    	bien.setNumInventario("43432");
    	bien.setTipo(Const.PATRON_MUEBLES_HISTORICOART);
    	System.out.println("Pepe: "+new InventarioConnection("gffg", "fds").getCodNumInventario(bien,"1.4.666a"));
    	System.out.println("Pepe: "+new InventarioConnection("gffg", "fds").getCodNumInventario(bien,"2222"));

    }


	public void returnUpdateBienFeatureInventario(ObjectOutputStream oos,
			Object obj, Sesion userSesion) throws Exception{
   	   	insertLayerFeature((BienBean)obj,userSesion);    
		
	}

    private BienBean insertLayerFeature(BienBean bien, Sesion userSesion) throws Exception{
		if (bien == null) return null;
        updateBien(bien, userSesion);
        return bien;
	}
    
    private void updateBien(BienBean bien,Sesion userSesion) throws PermissionException, LockException, Exception{
    	if(bien instanceof DerechoRealBean){
    		updateDerechoReal((DerechoRealBean) bien, userSesion);
    	}
    	if(bien instanceof CreditoDerechoBean){
    		updateCreditoDerecho((CreditoDerechoBean) bien, userSesion);
    	}
    	if (bien instanceof InmuebleBean){
    		updateInmueble((InmuebleBean) bien, userSesion);
    	}
    	if(bien instanceof MuebleBean){
    		updateMueble((MuebleBean) bien, userSesion);
    	}if(bien instanceof SemovienteBean){
    		updateSemoviente((SemovienteBean)bien, userSesion);
    	}if(bien instanceof VehiculoBean){
    		updateVehiculo((VehiculoBean)bien, userSesion);
    	}if(bien instanceof ViaBean){
    		updateVia((ViaBean)bien, userSesion);
    	}
    }


    /**
     * Devuelve la hora de base de datos
     * @return
     */
    public Date getDate(ObjectOutputStream oos) throws Exception{
    	Connection conn = null;
    	PreparedStatement ps = null;
    	ResultSet rs = null;
    	Timestamp time = null;
    	Date date = null;
    	try{
    		conn = CPoolDatabase.getConnection();
    		String sSQL = "SELECT localtimestamp";
    		logger.debug("Getting actula version date");
    		ps = conn.prepareStatement(sSQL);
    		rs = ps.executeQuery();
    		if (rs.next())
    			time = rs.getTimestamp(1);
    		date = new Date(time.getTime());
    	}catch(Exception e){
            throw e;    		
	    }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();}catch(Exception e){};
            if(oos!=null)
            	oos.writeObject(date);
	    }
	    return date;
    }

	public void returnNumerosInventario(ObjectOutputStream oos, Integer epigInventario) throws Exception{
		
    	Connection conn= CPoolDatabase.getConnection();
    	
    	if (conn==null)
    		throw new Exception("No se ha conseguido establecer la conexion");
    	
        try{
            for (Iterator it=getNumerosInventario(conn, epigInventario).iterator();it.hasNext();){
                oos.writeObject((Inventario)it.next());
            }
        }catch(Exception e){ 
            logger.error("returnNumerosInventario: "+ e.getMessage());
            oos.writeObject(new ACException(e));
            throw e;
        }finally{
			try {
				conn.close();
				CPoolDatabase.releaseConexion();
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
		
	}
	
    public ArrayList getNumerosInventario(Connection conn, Integer epigInventario) throws Exception{
    	
		String sSQL = "SELECT numinventario, id FROM bienes_inventario WHERE revision_expirada = 9999999999 AND tipo = ? AND id_municipio = ? ORDER BY numinventario";
    	ArrayList lstNumInventario = new ArrayList();
        PreparedStatement ps= null;
        ResultSet rs= null;
        com.geopista.protocol.inventario.Inventario eielInventario = new com.geopista.protocol.inventario.Inventario();
		eielInventario.setId(0);
		eielInventario.setNumInventario("");
		String numInventario = new String();
		int id = 0;
        try{
            ps= conn.prepareStatement(sSQL);
            ps.setInt(1, epigInventario);
            ps.setInt(2,Integer.valueOf(municipio));
            rs= ps.executeQuery();
            InventarioEIELBean eiel= null;
            while (rs.next()){
            	numInventario = rs.getString("numinventario");
            	id = rs.getInt("id");
			
				eielInventario = new com.geopista.protocol.inventario.Inventario();
				eielInventario.setId(id);
				eielInventario.setNumInventario(numInventario);
				lstNumInventario.add(eielInventario); 
            }
        }catch(Exception ex){
        	logger.error("Error al ejecutar: "+sSQL);
        	throw ex;
        }finally{
            try{rs.close();}catch(Exception e){e.printStackTrace();};
            try{ps.close();}catch(Exception e){e.printStackTrace();};
        }

        return lstNumInventario;
    }    
    
	public void returnComprobarIntegEIELInventario(ObjectOutputStream oos, BienBean bien) throws Exception{
		
    	Connection conn= CPoolDatabase.getConnection();
    	
    	if (conn==null)
    		throw new Exception("No se ha conseguido establecer la conexion");
    	
        try{
            for (Iterator it=getComprobarIntegEIELInventario(conn, bien).iterator();it.hasNext();){
                oos.writeObject((Vector)it.next());
            }
        }catch(Exception e){ 
            logger.error("returnDatosEIELBien: "+ e.getMessage());
            oos.writeObject(new ACException(e));
            throw e;
        }finally{
			try {
				conn.close();
				CPoolDatabase.releaseConexion();
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
		
	}
	
	public void returnElementosComprobarIntegEIELInventario(ObjectOutputStream oos, long idBien) throws Exception{
		
    	Connection conn= CPoolDatabase.getConnection();
    	
    	if (conn==null)
    		throw new Exception("No se ha conseguido establecer la conexion");
    	
        try{
            for (Iterator it=getFeaturesLayersEIEL(conn, idBien).iterator();it.hasNext();){
                oos.writeObject((Vector)it.next());
            }
        }catch(Exception e){ 
            logger.error("returnDatosEIELBien: "+ e.getMessage());
            oos.writeObject(new ACException(e));
            throw e;
        }finally{
			try {
				conn.close();
				CPoolDatabase.releaseConexion();
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
		
	}
		
    public Collection getComprobarIntegEIELInventario(Connection conn, BienBean bien) throws Exception{
    	String sSQL= null;
        PreparedStatement ps= null;
        ResultSet rs= null;
    	Object[] features;
    	Object[] layers;
    	HashMap hComp = new HashMap();
    	Vector featuresTablasEIEL = getFeaturesLayersEIEL(conn, bien.getId());
    	  	
    	Vector tablasEIEL = (Vector) featuresTablasEIEL.get(0);
    	Vector featuresEIEL = (Vector) featuresTablasEIEL.get(1);
    	Vector unionClaveEIEL = (Vector) featuresTablasEIEL.get(2);
    	features = bien.getIdFeatures();
    	layers = bien.getIdLayers();
    	if (features != null){
    		for (int i=0;i<layers.length;i++){
    			String layer = (String)layers[i];

    			if (layer.equals(Constantes.INVENTARIO_PARCELAS)){
    				sSQL= "SELECT * from parcelas where id = ?";
    			}
    			else{
    				if (layer.equals(Constantes.INVENTARIO_VIAS)){
    					sSQL= "SELECT * from vias where id = ?";
        			}
    			}
    			ps= conn.prepareStatement(sSQL);
    			String feature = (String)features[i];
    			if (feature != null){ 					
    				ps.setLong(1, Long.valueOf(feature));
                	rs= ps.executeQuery();
                	if (rs.next()){
                		PGgeometry pgGeometry=(PGgeometry)rs.getObject("GEOMETRY");
                		if (pgGeometry != null){
                			Geometry geometry = convertGeometry(pgGeometry);
	                		for (int j=0;j<featuresEIEL.size();j++){
	                			if (featuresEIEL.get(j) != null){
	                				sSQL= "SELECT * from "+tablasEIEL.get(j).toString()+" where id = ?";
	                				ps= conn.prepareStatement(sSQL);
	                				String featureEIEL = (String)featuresEIEL.get(j);
	                				ps.setLong(1, Long.valueOf(featureEIEL));
	                            	rs= ps.executeQuery();
	                            	if (rs.next()){
	                            		PGgeometry pgGeometryEIEL=(PGgeometry)rs.getObject("GEOMETRY");
	                            		if (pgGeometryEIEL != null){
		                            		Geometry geometryEIEL = convertGeometry(pgGeometryEIEL);
		                            		if (geometry.contains(geometryEIEL)){
		                            			logger.info("LA GEOMETRIA DEL ELEMENTO EIEL ESTA DENTRO DE LA GEOMETRIA DEL BIEN.");
		                            		}
		                            		else{
		                            			logger.info("LA GEOMETRIA DEL ELEMENTO EIEL NO ESTA DENTRO DE LA GEOMETRIA DEL BIEN.");
		                            	    	Vector featuresTablasEIELTMP = new Vector();
		                            			featuresTablasEIELTMP.add(unionClaveEIEL.get(j).toString());
		                            			featuresTablasEIELTMP.add(featureEIEL);
		                            			featuresTablasEIELTMP.add(tablasEIEL.get(j).toString());
		                            			hComp.put(j, featuresTablasEIELTMP);
		                            		}
	                            		}
	                            		else{
	                            			logger.info("EL ELEMENTO EIEL NO TIENE GEOMETRIA.");
	                            		}
	                            	}
	                        	}
	                        	else{
	                        		logger.info("EL ELEMENTO EIEL NO TIENE INFORMACION GRAFICA.");
	                        	}
	                		}
	            		}
                		else{
                			logger.info("EL BIEN DE INVENTARIO NO TIENE GEOMETRIA.");
                		}
                	}
    			}
    		}
    	}
		return hComp.values();
    }
	
	public void returnDatosEIELSinAsociar(ObjectOutputStream oos) throws Exception{
		
    	Connection conn= CPoolDatabase.getConnection();
    	
    	if (conn==null)
    		throw new Exception("No se ha conseguido establecer la conexion");
    	
        try{
            for (Iterator it=getDatosEIELSinAsociar(conn).iterator();it.hasNext();){
                oos.writeObject((InventarioEIELBean)it.next());
            }
        }catch(Exception e){ 
            logger.error("returnDatosEIELSinAsociar: "+ e.getMessage());
            oos.writeObject(new ACException(e));
            throw e;
        }finally{
			try {
				conn.close();
				CPoolDatabase.releaseConexion();
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
		
	}
	
	public void returnDatosEIELBien(ObjectOutputStream oos, long idBien) throws Exception{
		
    	Connection conn= CPoolDatabase.getConnection();
    	
    	if (conn==null)
    		throw new Exception("No se ha conseguido establecer la conexion");
    	
        try{
            for (Iterator it=getDatosEIELBien(conn, idBien).iterator();it.hasNext();){
                oos.writeObject((InventarioEIELBean)it.next());
            }
        }catch(Exception e){ 
            logger.error("returnDatosEIELBien: "+ e.getMessage());
            oos.writeObject(new ACException(e));
            throw e;
        }finally{
			try {
				conn.close();
				CPoolDatabase.releaseConexion();
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
		
	}
	
    public Vector getFeaturesLayersEIEL(Connection conn, long idBien) throws Exception{
    	java.util.Vector vTablas= new java.util.Vector();
    	java.util.Vector vFeatures= new java.util.Vector();
    	java.util.Vector vUnionClaveEIEL= new java.util.Vector();
    	java.util.Vector vTablasFeatures= new java.util.Vector();
    	// String sSQL= "SELECT v.tabla_c, v.id_c, v.union_clave_eiel FROM eiel_inventario e, v_integ_eiel v  WHERE e.id_inventario = ? AND e.union_clave_eiel = v.union_clave_eiel";
    	// Se ha separado en dos para reducir el tiempo de ejecución
    	String sSQL= "SELECT union_clave_eiel FROM EIEL_INVENTARIO where id_inventario = ?";
        PreparedStatement ps= null;
        ResultSet rs= null;
        try{
            ps= conn.prepareStatement(sSQL);
            ps.setLong(1, idBien);
            String union_clave_eiel = "";
            rs= ps.executeQuery();
            while (rs.next()){
            	if (!rs.isLast()){
            		union_clave_eiel = union_clave_eiel +"'"+rs.getString("union_clave_eiel")+"', ";            	            	
            	}
            	else{
            		union_clave_eiel = union_clave_eiel +"'"+rs.getString("union_clave_eiel")+"'";
            	}
            	
            }
            if (!union_clave_eiel.equals("")){
	            ps= null;
	            rs= null;
	            sSQL = "SELECT v.tabla_c, v.id_c, v.union_clave_eiel FROM v_integ_eiel v where v.union_clave_eiel IN ("+union_clave_eiel+")";
	            ps= conn.prepareStatement(sSQL);
	
	            rs= ps.executeQuery();
	            InventarioEIELBean eiel= null;
	            while (rs.next()){
	            	vTablas.add(rs.getString("tabla_c"));
	            	vFeatures.add(rs.getString("id_c"));
	            	vUnionClaveEIEL.add(rs.getString("union_clave_eiel"));
	            }
	            vTablasFeatures.add(vTablas);
	            vTablasFeatures.add(vFeatures);
	            vTablasFeatures.add(vUnionClaveEIEL);
            }   
        }catch(Exception ex){
        	logger.error("Error al ejecutar: "+sSQL);
        	throw ex;
        }finally{
            try{rs.close();}catch(Exception e){e.printStackTrace();};
            try{ps.close();}catch(Exception e){e.printStackTrace();};
        }

        return vTablasFeatures;
    }
    
    public Collection getDatosEIELBien(Connection conn, long idBien) throws Exception{
    	// String sSQL= "SELECT v.vista_eiel as vista_eiel, v.nombre, v.union_clave_eiel, v.estado, v.gestor FROM eiel_inventario e, v_integ_eiel v  WHERE e.id_inventario = ? AND e.union_clave_eiel = v.union_clave_eiel and e.id_municipio = ?";
    	// Se ha separado en dos para reducir el tiempo de ejecución
    	String sSQL= "SELECT union_clave_eiel FROM EIEL_INVENTARIO where id_inventario = ?";
        PreparedStatement ps= null;
        ResultSet rs= null;
        HashMap hEIEL = new HashMap();
        try{
            ps= conn.prepareStatement(sSQL);
            ps.setLong(1, idBien);
            String union_clave_eiel = "";
            rs= ps.executeQuery();
            while (rs.next()){
            	if (!rs.isLast()){
            		union_clave_eiel = union_clave_eiel +"'"+rs.getString("union_clave_eiel")+"', ";            	            	
            	}
            	else{
            		union_clave_eiel = union_clave_eiel +"'"+rs.getString("union_clave_eiel")+"'";
            	}
            	
            }
            if (!union_clave_eiel.equals("")){
            	ps= null;
	            rs= null;
	            sSQL = "SELECT v.vista as vista_eiel, v.nombre, v.union_clave_eiel, v.estado, v.gestor FROM v_integ_eiel v  WHERE v.union_clave_eiel IN ("+union_clave_eiel+")";
	            ps= conn.prepareStatement(sSQL);
	            rs= ps.executeQuery();
	            InventarioEIELBean eiel= null;
	            while (rs.next()){
	            	eiel = new InventarioEIELBean();
	            	eiel.setUnionClaveEIEL(rs.getString("union_clave_eiel"));
	            	String vista_eiel = rs.getString("vista_eiel");
	            	String tipo = "";
	            	tipo = obtenerTipo(vista_eiel);
	
	            	eiel.setTipoEIEL(tipo);
	            	eiel.setNombreEIEL(rs.getString("nombre"));
	            	eiel.setEstadoEIEL(rs.getString("estado"));
	            	eiel.setGestionEIEL(rs.getString("gestor"));
	                hEIEL.put(eiel.getUnionClaveEIEL(), eiel);
	            }
            }
        }catch(Exception ex){
        	logger.error("Error al ejecutar: "+sSQL);
        	throw ex;
        }finally{
            try{rs.close();}catch(Exception e){e.printStackTrace();};
            try{ps.close();}catch(Exception e){e.printStackTrace();};
        }

        return hEIEL.values();
    }
	
	public Collection getDatosEIELSinAsociar(Connection conn) throws Exception{
    	// String sSQL= "SELECT e.vista_eiel, v.nombre, v.union_clave_eiel, v.estado, v.gestor FROM eiel_inventario e, v_integ_eiel v  WHERE e.id_inventario = 0 AND e.union_clave_eiel = v.union_clave_eiel and e.titularidad_municipal= ? and e.id_municipio = ?";
    	// Se ha separado en dos para reducir el tiempo de ejecución
		String sSQL= "SELECT union_clave_eiel FROM EIEL_INVENTARIO where id_inventario = 0 and titularidad_municipal= ? and id_municipio = ?";
        PreparedStatement ps= null;
        ResultSet rs= null;
        HashMap hEIEL = new HashMap();
        try{
            ps= conn.prepareStatement(sSQL);
            ps.setString(1, ConstantesEIEL.TITULARIDAD_MUNICIPAL_SI);
            ps.setInt(2,Integer.valueOf(municipio));
            String union_clave_eiel = "";
            rs= ps.executeQuery();
            while (rs.next()){
            	if (!rs.isLast()){
            		union_clave_eiel = union_clave_eiel +"'"+rs.getString("union_clave_eiel")+"', ";            	            	
            	}
            	else{
            		union_clave_eiel = union_clave_eiel +"'"+rs.getString("union_clave_eiel")+"'";
            	}
            	
            }
            if (!union_clave_eiel.equals("")){
            	ps= null;
	            rs= null;
	            sSQL= "SELECT v.vista as vista_eiel, v.nombre, v.union_clave_eiel, v.estado, v.gestor FROM v_integ_eiel v  WHERE v.union_clave_eiel IN ("+union_clave_eiel+")";
	            ps= conn.prepareStatement(sSQL);
	
	            rs= ps.executeQuery();
	            InventarioEIELBean eiel= null;
	            while (rs.next()){
	            	eiel = new InventarioEIELBean();
	            	eiel.setUnionClaveEIEL(rs.getString("union_clave_eiel"));
	            	String vista_eiel = rs.getString("vista_eiel");
	            	String tipo = obtenerTipo(vista_eiel);
	            	eiel.setTipoEIEL(tipo);
	            	eiel.setNombreEIEL(rs.getString("nombre"));
	            	eiel.setEstadoEIEL(rs.getString("estado"));
	            	eiel.setGestionEIEL(rs.getString("gestor"));
	                hEIEL.put(eiel.getUnionClaveEIEL(), eiel);
	            }
            }
        }catch(Exception ex){
        	logger.error("Error al ejecutar: "+sSQL);
        	throw ex;
        }finally{
            try{rs.close();}catch(Exception e){e.printStackTrace();};
            try{ps.close();}catch(Exception e){e.printStackTrace();};
        }

        return hEIEL.values();
    }

	public void returnBienesPreAlata(ObjectOutputStream oos) throws Exception{
		
    	Connection conn= CPoolDatabase.getConnection();
    	
    	if (conn==null)
    		throw new Exception("No se ha conseguido establecer la conexion");
    	
        try{
            for (Iterator it=getBienesPA(conn).iterator();it.hasNext();){
                oos.writeObject((BienPreAltaBean)it.next());
            }
        }catch(Exception e){ 
            logger.error("returnCuentasContables: "+ e.getMessage());
            oos.writeObject(new ACException(e));
            throw e;
        }finally{
			try {
				conn.close();
				CPoolDatabase.releaseConexion();
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
		
	}
	

	
    public Collection getBienesPA(Connection conn) throws Exception{
    	
    	String sSQL= "SELECT * FROM  bien_PreAlta WHERE nombre is not null and esperaAlta=true ORDER BY id DESC";
        HashMap alRet= new HashMap();
        PreparedStatement ps= null;
        ResultSet rs= null;
        try{
            ps= conn.prepareStatement(sSQL);
            rs= ps.executeQuery();
            BienPreAltaBean bpa= null;
            while (rs.next()){
                if (rs.getString("NOMBRE")!=null && !rs.getString("NOMBRE").trim().equalsIgnoreCase("")){
                    bpa= new BienPreAltaBean();
                    bpa.setId(rs.getLong("ID"));
                    bpa.setNombre(rs.getString("NOMBRE"));
                    bpa.setDescripcion(rs.getString("DESCRIPCION"));
                    bpa.setFechaAdquisicion(rs.getDate("FECHA_ADQUISICION"));
                    bpa.setCosteAdquisicion(rs.getDouble("COSTE_ADQUISICION"));
                    bpa.setTipo(rs.getLong("TIPO_BIEN"));
                    bpa.setIdMunicipio(rs.getLong("ID_MUNICIPIO"));
                    alRet.put(new Long(bpa.getId()), bpa);
                }
            }
        }catch(Exception ex){
        	logger.error("Error al ejecutar: "+sSQL);
        	throw ex;
        }finally{
            try{rs.close();}catch(Exception e){e.printStackTrace();};
            try{ps.close();}catch(Exception e){e.printStackTrace();};
        }

        return alRet.values();
    }


	public void returnInsertBienPreALta(ObjectOutputStream oos,
			Object[] idlayers, Object[] idfeatures, Object obj,
			Object bienPA, Sesion userSesion) throws Exception {
		Connection conn = CPoolDatabase.getConnection();

		if (conn == null)
			throw new Exception("No se ha conseguido establecer la conexion");
		try {
			//TODO: Habira que usar una sola conexion para realizar todas las operaciones!!!
			returnInsertInventario(oos, idlayers, idfeatures, obj, userSesion);
			deleteBienesPA((BienPreAltaBean)bienPA, conn);
			conn.commit();
		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace();
			logger.error("NO se han podido insertar un bien en prealta");
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				logger.error("No se ha podido cerrar la conexion");
			}
		}
	}

	private void deleteBienesPA(BienPreAltaBean bienPA, Connection conn)
			throws Exception {
		String sSQL = "update bien_PreAlta set esperaAlta=FALSE WHERE id = ?";
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sSQL);
			ps.setLong(1, bienPA.getId());
			if (logger.isDebugEnabled()){
				logger.debug("La consulta es: " + sSQL);
				logger.debug("id= "+bienPA.getId());
			}
			ps.execute();
			conn.commit();


		} catch (Exception ex) {
			logger.error("No se ha podido borrar el bien en Prealta: "
					+ bienPA.getId());
			ex.printStackTrace();
			throw ex;
		} finally {
			try {
				if (ps != null)
					ps.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void returnInsertIntegEIELInventario(ObjectOutputStream oos, InventarioEIELBean object) throws Exception {
		Connection conn = CPoolDatabase.getConnection();

		if (conn == null)
			throw new Exception("No se ha conseguido establecer la conexion");
		try {
			//TODO: Habira que usar una sola conexion para realizar todas las operaciones!!!
			insertIntegEIELInventario(conn, object);
			conn.commit();
		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace();
			logger.error("NO se han podido Insertar la Integracion de un bien de Inventario con la EIEL.");
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				logger.error("No se ha podido cerrar la conexion");
			}
		}
	}

    private void insertIntegEIELInventario(Connection conn, InventarioEIELBean object){
    	/* Antes de insertar comprobamos si ya existe una fila para ese elemento:
    	 * 
    	 * - Si existe:
    	 *  + Si titularidadmunicipal == "": Borramos la fila.
    	 *  + Si titularidadmunicipal != "": Actualizamos por si ha cambiado algo.
    	 * - Si no existe: Insertamos. 
    	 */
    	if (object.getTipoEIEL()!= null){
    		object.setTipoEIEL(obtenerVista(object.getTipoEIEL()));
    	}	
    	PreparedStatement ps = null;
    	ResultSet rs = null;
		String sSQL = "select * from eiel_inventario where union_clave_eiel = ? and vista_eiel = ? and id_municipio = ?";		
				
		try {
			ps = conn.prepareStatement(sSQL);

			ps.setString(1, object.getUnionClaveEIEL());
			ps.setString(2, object.getTipoEIEL());
			ps.setInt(3, Integer.valueOf(municipio));
		
			rs = ps.executeQuery();
			// Existe una fila en eiel_inventario con esa clave:
			if (rs.next()) {

		    	ps = null;
				sSQL = "update eiel_inventario set id_inventario=?, epig_inventario=? where union_clave_eiel = ? and vista_eiel = ? and id_municipio = ?";
		
				ps = conn.prepareStatement(sSQL);
				ps.setLong(1, object.getIdBien());
				ps.setInt(2, object.getEpigInventario());
				ps.setString(3, object.getUnionClaveEIEL());
				ps.setString(4, object.getTipoEIEL());
				ps.setInt(5, Integer.valueOf(municipio));
			
				ps.execute();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

    }
	

	public void updateDatosValoracion(ObjectOutputStream oos, Double valorUrbano, Double valorRustico, Integer idEntidad) throws Exception {
		String sSQL = "update valor_datos_valoracion set valor_urbano=?, valor_rustico=? ,id_entidad=? WHERE id_entidad = ?";
		PreparedStatement ps = null;
		Connection conn = CPoolDatabase.getConnection();

		if (conn == null)
			throw new Exception("No se ha conseguido establecer la conexion");
		try {
			ps = conn.prepareStatement(sSQL);
			ps.setDouble(1,  valorUrbano);
			ps.setDouble(2,  valorRustico);
			ps.setInt(3,  idEntidad);
			ps.setInt(4,  idEntidad);
			if (logger.isDebugEnabled()){
				logger.debug("La consulta es: " + sSQL);
				logger.debug("id_entidad= "+idEntidad);
			}
			int row=ps.executeUpdate();
			if(row ==0){
				sSQL = "INSERT INTO valor_datos_valoracion (valor_urbano, valor_rustico,id_entidad) VALUES(?,?,?)";
				ps = conn.prepareStatement(sSQL);
				ps.setDouble(1,  valorUrbano);
				ps.setDouble(2,  valorRustico);
				ps.setInt(3,  idEntidad);
				if (logger.isDebugEnabled()){
					logger.debug("La consulta es: " + sSQL);
					logger.debug("idEntidad= "+idEntidad);
				}
				ps.execute();
			}


		} catch (Exception ex) {
			logger.error("No se ha podido actualizar los valores para los datos de valoración "+ idEntidad);
			ex.printStackTrace();
			throw ex;
		} finally {
			try {
				if (ps != null)
					ps.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}


	public Double returnValorMetro(ObjectOutputStream oos, String keyPatron, Integer idEntidad) throws Exception {
		String sSQL = "SELECT valor_urbano, valor_rustico FROM valor_datos_valoracion WHERE id_entidad=?";
		PreparedStatement ps = null;
		Connection conn = CPoolDatabase.getConnection();
		ResultSet rs=null;
		Double valor_metro=0.0;
		if (conn == null)
			throw new Exception("No se ha conseguido establecer la conexion");
		try {
			ps = conn.prepareStatement(sSQL);
			ps.setDouble(1, idEntidad);
			if (logger.isDebugEnabled()){
				logger.debug("La consulta es: " + sSQL);
				logger.debug("id_entidad= "+idEntidad);
			}
			rs = ps.executeQuery();
			while (rs.next()){
				 if(keyPatron.equals(Const.PATRON_INMUEBLES_URBANOS))
					 valor_metro=rs.getDouble("valor_urbano");
			     else if (keyPatron.equals(Const.PATRON_INMUEBLES_RUSTICOS))
			    	 valor_metro=rs.getDouble("valor_rustico");
			}
			
	         oos.writeObject(valor_metro);
	         
		} catch (Exception ex) {
			logger.error("No se ha podido actualizar los valores para los datos de valoración "+ idEntidad);
			ex.printStackTrace();
			throw ex;
		} finally {
			try {
				if (ps != null)
					ps.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return valor_metro;

	}
	
    private String obtenerTipo(String vista_eiel) {
    	
		if (vista_eiel.equals(ConstantesEIEL.VISTA_DEPOSITOS))
    		return ConstantesEIEL.TIPO_DEPOSITOS;
		else
    		if (vista_eiel.equals(ConstantesEIEL.VISTA_CENTRO_ASISTENCIAL))
        		return ConstantesEIEL.TIPO_CENTRO_ASISTENCIAL;
    		else
        		if (vista_eiel.equals(ConstantesEIEL.VISTA_CASA_CONSISTORIAL))
            		return ConstantesEIEL.TIPO_CASA_CONSISTORIAL;
        		else
            		if (vista_eiel.equals(ConstantesEIEL.VISTA_CEMENTERIO))
                		return ConstantesEIEL.TIPO_CEMENTERIO;
            		else
                		if (vista_eiel.equals(ConstantesEIEL.VISTA_CENTRO_CULTURAL))
                    		return ConstantesEIEL.TIPO_CENTRO_CULTURAL;
                		else
                    		if (vista_eiel.equals(ConstantesEIEL.VISTA_CENTRO_ENSENIANZA))
                        		return ConstantesEIEL.TIPO_CENTRO_ENSENIANZA;
                    		else
                        		if (vista_eiel.equals(ConstantesEIEL.VISTA_INSTALACION_DEPORTIVA))
                            		return ConstantesEIEL.TIPO_INSTALACION_DEPORTIVA;
                        		else
                            		if (vista_eiel.equals(ConstantesEIEL.VISTA_INCENDIOS_PROTECCION))
                                		return ConstantesEIEL.TIPO_INCENDIOS_PROTECCION;
                            		else
                                		if (vista_eiel.equals(ConstantesEIEL.VISTA_LONJAS_MERCADOS))
                                    		return ConstantesEIEL.TIPO_LONJAS_MERCADOS;
                                		else
                                    		if (vista_eiel.equals(ConstantesEIEL.VISTA_MATADEROS))
                                        		return ConstantesEIEL.TIPO_MATADEROS;
                                    		else
                                        		if (vista_eiel.equals(ConstantesEIEL.VISTA_PARQUES_JARDINES))
                                            		return ConstantesEIEL.TIPO_PARQUES_JARDINES;
                                        		else
                                            		if (vista_eiel.equals(ConstantesEIEL.VISTA_CENTRO_SANITARIO))
                                                		return ConstantesEIEL.TIPO_CENTRO_SANITARIO;
                                            		else
                                                		if (vista_eiel.equals(ConstantesEIEL.VISTA_EDIFICIO_SIN_USO))
                                                    		return ConstantesEIEL.TIPO_EDIFICIO_SIN_USO;
                                                		else
                                                    		if (vista_eiel.equals(ConstantesEIEL.VISTA_TANATORIO))
                                                        		return ConstantesEIEL.TIPO_TANATORIO;
                                                    		else
                                                        		if (vista_eiel.equals(ConstantesEIEL.VISTA_CARRETERAS))
                                                            		return ConstantesEIEL.TIPO_CARRETERAS;
                                                        		else
                                                            		if (vista_eiel.equals(ConstantesEIEL.VISTA_ALUMBRADOS))
                                                                		return ConstantesEIEL.TIPO_ALUMBRADOS;
                                                            		else
                                                                		if (vista_eiel.equals(ConstantesEIEL.VISTA_DEPURADORAS))
                                                                    		return ConstantesEIEL.TIPO_DEPURADORAS;
                                                                   		else return null;
	}
   	
    
    private String obtenerVista(String tipo_eiel) {
    	

		if (tipo_eiel.equals(ConstantesEIEL.TIPO_DEPOSITOS))
    		return ConstantesEIEL.VISTA_DEPOSITOS;
		else
    		if (tipo_eiel.equals(ConstantesEIEL.TIPO_CENTRO_ASISTENCIAL))
        		return ConstantesEIEL.VISTA_CENTRO_ASISTENCIAL;
    		else
        		if (tipo_eiel.equals(ConstantesEIEL.TIPO_CASA_CONSISTORIAL))
            		return ConstantesEIEL.VISTA_CASA_CONSISTORIAL;
        		else
            		if (tipo_eiel.equals(ConstantesEIEL.TIPO_CEMENTERIO))
                		return ConstantesEIEL.VISTA_CEMENTERIO;
            		else
                		if (tipo_eiel.equals(ConstantesEIEL.TIPO_CENTRO_CULTURAL))
                    		return ConstantesEIEL.VISTA_CENTRO_CULTURAL;
                		else
                    		if (tipo_eiel.equals(ConstantesEIEL.TIPO_CENTRO_ENSENIANZA))
                        		return ConstantesEIEL.VISTA_CENTRO_ENSENIANZA;
                    		else
                        		if (tipo_eiel.equals(ConstantesEIEL.TIPO_INSTALACION_DEPORTIVA))
                            		return ConstantesEIEL.VISTA_INSTALACION_DEPORTIVA;
                        		else
                            		if (tipo_eiel.equals(ConstantesEIEL.TIPO_INCENDIOS_PROTECCION))
                                		return ConstantesEIEL.VISTA_INCENDIOS_PROTECCION;
                            		else
                                		if (tipo_eiel.equals(ConstantesEIEL.TIPO_LONJAS_MERCADOS))
                                    		return ConstantesEIEL.VISTA_LONJAS_MERCADOS;
                                		else
                                    		if (tipo_eiel.equals(ConstantesEIEL.TIPO_MATADEROS))
                                        		return ConstantesEIEL.VISTA_MATADEROS;
                                    		else
                                        		if (tipo_eiel.equals(ConstantesEIEL.TIPO_PARQUES_JARDINES))
                                            		return ConstantesEIEL.VISTA_PARQUES_JARDINES;
                                        		else
                                            		if (tipo_eiel.equals(ConstantesEIEL.TIPO_CENTRO_SANITARIO))
                                                		return ConstantesEIEL.VISTA_CENTRO_SANITARIO;
                                            		else
                                                		if (tipo_eiel.equals(ConstantesEIEL.TIPO_EDIFICIO_SIN_USO))
                                                    		return ConstantesEIEL.VISTA_EDIFICIO_SIN_USO;
                                                		else
                                                    		if (tipo_eiel.equals(ConstantesEIEL.TIPO_TANATORIO))
                                                        		return ConstantesEIEL.VISTA_TANATORIO;
                                                    		else
                                                        		if (tipo_eiel.equals(ConstantesEIEL.TIPO_CARRETERAS))
                                                            		return ConstantesEIEL.VISTA_CARRETERAS;
                                                        		else
                                                            		if (tipo_eiel.equals(ConstantesEIEL.TIPO_ALUMBRADOS))
                                                                		return ConstantesEIEL.VISTA_ALUMBRADOS;
                                                            		else
                                                                		if (tipo_eiel.equals(ConstantesEIEL.TIPO_DEPURADORAS))
                                                                    		return ConstantesEIEL.VISTA_DEPURADORAS;
                                                                   		else return null;
}


    public Geometry convertGeometry(PGgeometry pg) throws SQLException, ParseException{

    	String geometryString= pg.getGeometry().toString();
        int srid = pg.getGeometry().getSrid();
    	GeometryFactory fact= new GeometryFactory(new PrecisionModel(1E10), srid);
        WKTReader r= new WKTReader( fact );

    	Geometry geom;
    	
        if (geometryString.indexOf(';') != -1){
        	String []temp= PGgeometry.splitSRID(geometryString);       	
            geom= (Geometry) r.read(temp[1]);
            geom.setSRID(srid);
        }
        else
        	geom = (Geometry) r.read(geometryString);
        
        return geom;

    }
    
    public PGgeometry GeometryToPGgeometry(Geometry geom) throws SQLException, ParseException{

    	String PGgeometryStr = "SRID=" + geom.getSRID() + ";" + geom.toString();
    	PGgeometry  pg = new PGgeometry(PGgeometry.geomFromString(PGgeometryStr));
    	return pg;

}

	
	 public Collection<HistoricoAmortizacionesBean> getBienesAmortizables(String superpatron, String patron, String cadena, Object[] filtro, Sesion userSesion,Integer anio) throws Exception{
		String tipoBien=null;
		String tipoEspecificoBien=null;
		String coste=null;
		String table=null;
		String sSQL= "";
		  
		  if (patron.equalsIgnoreCase(Const.PATRON_INMUEBLES_URBANOS)){
			  tipoBien="inmuebles";
			  tipoEspecificoBien=Const.INMUEBLES_URBANOS;
				coste="valor_adquisicion_inmueble";
				table="12003";
		  }
	    	else if (patron.equalsIgnoreCase(Const.PATRON_INMUEBLES_RUSTICOS)){
	    		tipoBien="inmuebles";
				tipoEspecificoBien=Const.INMUEBLES_RUSTICOS;
				coste="valor_adquisicion_inmueble";
				table="12003";
			
	      }
	    	else if (patron.equalsIgnoreCase(Const.PATRON_MUEBLES_HISTORICOART)){
	    		tipoBien=Const.MUEBLES;
	    		coste="coste_adquisicion";
	    		table="12004";
	    	}
	    	else if (patron.equalsIgnoreCase(Const.PATRON_VEHICULOS)){
	    		tipoBien=Const.VEHICULOS;
	    		coste="coste_adquisicion";
	    		table="12007";
	    	}
	   sSQL="select bienes_inventario.numinventario, bienes_inventario.nombre, bienes_inventario.descripcion AS B_DESCRIPCION, bienes_inventario.tipo," +
	    			tipoBien+".fecha_adquisicion, " +
	    			tipoBien+"."+coste+" AS COSTE_ADQUISICION,"+tipoBien+".id, " +
	    			"bienes_inventario.id_cuenta_contable,cuentas_contables.cuenta_contable, cuentas_contables.descripcion AS CC_DESCRIPCION, " +
	    			"cuentas_contables.cuenta AS CC_CUENTA,bienes_inventario.id_cuenta_amortizacion,cuentas_amortizacion.cuenta AS CA_CUENTA, " +
	    			"cuentas_amortizacion.descripcion AS CA_DESCRIPCION,cuentas_amortizacion.anios, cuentas_amortizacion.porcentaje, " +
	    			"cuentas_amortizacion.total_amortizado,cuentas_amortizacion.tipo_amortizacion, " +
	    			" historico_amortizacion.id_cuenta_amortizacion AS ID_CA ,historico_amortizacion.total_amortizado_porcentaje,historico_amortizacion.total_amortizado_anio "+
	    			"from "+ tipoBien+
	    			" inner join bienes_inventario on bienes_inventario.id="+tipoBien+".id and bienes_inventario.revision_actual="+tipoBien+".revision_actual AND bienes_inventario.id_cuenta_contable >0 AND bienes_inventario.id_cuenta_amortizacion > 0 "+
	    			"inner join versiones on versiones.revision="+tipoBien+".revision_actual " +
	    			"inner join iuseruserhdr on versiones.id_autor=iuseruserhdr.id " +
	    			"inner join tables_inventario on versiones.id_table_versionada=tables_inventario.id_table ";
	   				if(tipoEspecificoBien!=null)
	   					sSQL+=" inner join "+tipoEspecificoBien+" on "+tipoBien+".id="+tipoEspecificoBien+".id  and "+tipoBien+".revision_actual="+tipoEspecificoBien+".revision_actual ";
	   				
	   				sSQL+=" left join contable cuentas_contables on  bienes_inventario.id_cuenta_contable=cuentas_contables.id_clasificacion AND bienes_inventario.id_cuenta_contable IS NOT NULL  " +
	    			" left join amortizacion cuentas_amortizacion on  bienes_inventario.id_cuenta_amortizacion=cuentas_amortizacion.id AND bienes_inventario.id_cuenta_amortizacion IS NOT NULL " +
	    			" left join historico_amortizacion on historico_amortizacion.id_bien=bienes_inventario.id AND historico_amortizacion.anio=? "+
	    			"WHERE bienes_inventario.id_municipio=? " +
	    				"AND bienes_inventario.tipo=? " +
	    				"and "+tipoBien+".revision_actual <= (select coalesce(max(v.revision),0) from versiones v, tables_inventario t where v.fecha <=localtimestamp " +
	    				"and v.id_table_versionada=t.id_table and t.name='"+tipoBien+"')  " +
	    				"AND tables_inventario.name='"+tipoBien+"' "+
	    				"AND bienes_inventario.borrado = '0' and (bienes_inventario.revision_expirada=9999999999 " +
	    				"or (bienes_inventario.borrado='2' and bienes_inventario.revision_actual=bienes_inventario.revision_expirada)) " +
	    				"and versiones.id_table_versionada="+table;
	   				if(tipoEspecificoBien!=null)
	   					sSQL+=" and "+tipoBien+".revision_actual="+tipoEspecificoBien+".revision_actual ";
	   				sSQL+=" AND "+ tipoBien+".fecha_adquisicion IS NOT NULL AND " +tipoBien+".fecha_adquisicion BETWEEN "+tipoBien+".fecha_adquisicion AND CAST (? AS DATE)"+
	   						" AND "+tipoBien+"."+coste+">0 "; 
	   				sSQL+=" ORDER BY "+tipoBien+".id ASC, "+tipoBien+".revision_actual DESC OFFSET 0";
	   
	  
       
       logger.info("Sentencia getBienesAmortizables:"+sSQL);
       Connection conn=null;
       HashMap alRet= new HashMap();
       PreparedStatement ps= null;
       ResultSet rs= null;
	        try{
	    		conn= CPoolDatabase.getConnection();
	    		if (conn==null)
	        		throw new Exception("No se ha conseguido establecer la conexion");
	        	
	            ps= conn.prepareStatement(sSQL);
	            ps.setInt(1, anio);
	            ps.setString(2, this.municipio);
	            ps.setString(3,patron);
	            ps.setString(4,"'12-31-"+anio+"'");//limita al año introducido
	            rs= ps.executeQuery();
	            HistoricoAmortizacionesBean hab= null;
	            SimpleDateFormat formatoDeFecha = new SimpleDateFormat("dd/MM/yyyy");	          
	            
	            while (rs.next()){
//	                if (rs.getString("NOMBRE")!=null && !rs.getString("NOMBRE").trim().equalsIgnoreCase("")){
	                    hab= new HistoricoAmortizacionesBean();
	                    hab.setIdBien(rs.getLong("ID"));
	                    hab.setNumInventario(rs.getString("NUMINVENTARIO"));
	                    hab.setNombre(rs.getString("NOMBRE"));
	                    hab.setFechaAdquisicion(rs.getDate("FECHA_ADQUISICION"));
	                    hab.setCoste(rs.getDouble("COSTE_ADQUISICION"));
	                    CuentaAmortizacion cuenta =new CuentaAmortizacion();
	                    cuenta.setAnnos(rs.getInt("anios"));
	                    cuenta.setCuenta(rs.getString("CA_CUENTA"));
	                    cuenta.setDescripcion(rs.getString("CA_DESCRIPCION"));
	                    cuenta.setId(rs.getLong("id_cuenta_amortizacion"));	                   
	                    cuenta.setPorcentaje(rs.getDouble("porcentaje"));
	                    cuenta.setTipoAmortizacion(rs.getString("tipo_amortizacion"));
	                    cuenta.setTotalAmortizado(rs.getDouble("total_amortizado"));
	                    hab.setCuentaAmortizacion(cuenta);
	                    CuentaContable cuentaContable=new CuentaContable();
	                    cuentaContable.setCuenta(rs.getString("CC_CUENTA"));
	                    cuentaContable.setDescripcion(rs.getString("CC_DESCRIPCION"));
	                    cuentaContable.setId(rs.getLong("id_cuenta_contable"));
	                    hab.setCuentaContable(cuentaContable);
//	                    hab.setValorAmor(rs.getDouble("total_amortizado"));
	                    if(rs.getLong("ID_CA")!=0 && rs.getLong("id_cuenta_amortizacion")!= rs.getLong("ID_CA")){
	                    	  hab.setValorAmorAnio(rs.getDouble("total_amortizado_anio")+calcularTotalAmortizadoPorAnio(hab.getFechaAdquisicion(),anio, hab.getCoste(), hab.getCuentaAmortizacion(), DatosAmortizacionJPanel.POR_AÑOS));
			                   hab.setValorAmorPorc(rs.getDouble("total_amortizado_porcentaje")+calcularTotalAmortizadoPorAnio(hab.getFechaAdquisicion(),anio, hab.getCoste(), hab.getCuentaAmortizacion(), DatosAmortizacionJPanel.POR_PORCENTAJE));
			                   cuenta.setId(rs.getLong("id_cuenta_amortizacion"));	                   
	                    }
	                    else{ 
	                    	 hab.setValorAmorAnio(calcularTotalAmortizado(formatoDeFecha.parse("31/12/"+Integer.toString(anio)),hab.getFechaAdquisicion(), hab.getCoste(), hab.getCuentaAmortizacion(), DatosAmortizacionJPanel.POR_AÑOS));
			                   hab.setValorAmorPorc(calcularTotalAmortizado(formatoDeFecha.parse("31/12/"+Integer.toString(anio)),hab.getFechaAdquisicion(), hab.getCoste(), hab.getCuentaAmortizacion(), DatosAmortizacionJPanel.POR_PORCENTAJE));
		             	  }
	                    if(tipoEspecificoBien!=null)
	                    	hab.setTipo(tipoEspecificoBien);
	                    else
	                    	hab.setTipo(tipoBien);
	                    alRet.put(new Long(hab.getIdBien()), hab);
//	                }
	            }
	        }catch(Exception ex){
	        	logger.error("Error al ejecutar: "+sSQL);
	        	throw ex;
			} finally {
				
					try{rs.close();}catch(Exception e){e.printStackTrace();};
			        try{ps.close();}catch(Exception e){e.printStackTrace();};
			        try {
						if (conn != null)
							conn.close();
						} catch (Exception e) {
							logger.error("No se ha podido cerrar la conexion");
					}
	        }

	        return alRet.values();
	    }
	 
	    /**
	     * Inserta un bien amortizable en el historico
	     * @param oos parametro donde se deja el resultado de la operacion
	     * @param obj Lista de Bienes
	     * @param userSesion sesion del usuario
	     */
	    public void insertBienesAmortizados(ObjectOutputStream oos, Integer anio,Sesion userSesion) throws Exception{
	    	Connection conn=null;
	    	try{
	    		conn = CPoolDatabase.getConnection();
	    			   
	    	   ListaHistoricoAmortizaciones listaHA=getListaHistoricoAmortizables(anio,userSesion);
	    	   deleteAllBienesAmortizadosByAnio(conn,anio);
	    	   for (HistoricoAmortizacionesBean hab : listaHA.getHistoricoAmortizaciones().values()) {
//	    		   if(hab.getValorAmorAnio()!=hab.getCoste() || hab.getValorAmorPorc()!=hab.getCoste()){
		    		   hab.setAnio(anio);
			    	   insertBienAmortizado(hab, userSesion, null,conn);
//	    		   }
	    	   }

	    	    oos.writeObject(listaHA);
	        }catch(Exception e){
	           logger.error("insertBienAmortizado: ", e);
	           oos.writeObject(new ACException(e));
	           throw e;
		} finally {
			try {
				if (conn != null)
					conn.close();
				} catch (Exception e) {
					logger.error("No se ha podido cerrar la conexion");
				}
		}
	  }


		private void deleteAllBienesAmortizadosByAnio(Connection conn,
				Integer anio) throws Exception{
			  if (anio==null) return;
		        
			  String sSQL= "DELETE FROM historico_amortizacion WHERE anio=?";
			      
		        PreparedStatement ps= null;
		     	try {
		                ps= conn.prepareStatement(sSQL);
		                ps.setLong(1, anio);
		                ps.execute();

		        }catch(Exception ex){
		        	logger.error("Error al ejecutar: "+sSQL);
		        	throw ex;
		        }finally{
		            try{ps.close();}catch(Exception e){};
		        }
			
		}


		private void insertBienAmortizado(HistoricoAmortizacionesBean hab,
				Sesion userSesion, Object object, Connection conn) throws Exception {
			  if (hab==null) return;
		        
			  String sSQL= "INSERT INTO historico_amortizacion (id_bien, anio,total_amortizado_anio,id_cuenta_amortizacion,id_cuenta_contable,total_amortizado_porcentaje)" +
			  		" VALUES(?,?,?,?,?,?)";
			      
		        PreparedStatement ps= null;
		     	try {
		                ps= conn.prepareStatement(sSQL);
		                ps.setLong(1, hab.getIdBien());
		                ps.setInt(2, hab.getAnio());
		                ps.setDouble(3, hab.getValorAmorAnio());
		                ps.setLong(4, hab.getCuentaAmortizacion().getId());
		                ps.setLong(5, hab.getCuentaContable().getId()); 
		                ps.setDouble(6, hab.getValorAmorPorc());
		                ps.execute();

		        }catch(Exception ex){
		        	logger.error("Error al ejecutar: "+sSQL);
		        	throw ex;
		        }finally{
		            try{ps.close();}catch(Exception e){};
		        }
			
		}
	    private ListaHistoricoAmortizaciones getListaHistoricoAmortizables(Integer anio,Sesion userSesion) {
	    	ListaHistoricoAmortizaciones listaHA=new ListaHistoricoAmortizaciones();
	    	Collection<HistoricoAmortizacionesBean> listaBienesAmort = null;
			
			try {
				listaBienesAmort = getBienesAmortizables(Const.SUPERPATRON_BIENES,Const.PATRON_INMUEBLES_URBANOS, null, null,userSesion,anio);
			   	listaHA.addAll(listaBienesAmort);
			   	listaHA.setTotalInmueblesUrbanos(getCuentaBienes(Const.PATRON_INMUEBLES_URBANOS));
				listaBienesAmort = getBienesAmortizables(Const.SUPERPATRON_BIENES,Const.PATRON_INMUEBLES_RUSTICOS, null, null,userSesion,anio);
			   	listaHA.addAll(listaBienesAmort);
			   	listaHA.setTotalInmueblesRusticos(getCuentaBienes(Const.PATRON_INMUEBLES_RUSTICOS));
			   	listaBienesAmort = getBienesAmortizables(Const.SUPERPATRON_BIENES,Const.PATRON_MUEBLES_HISTORICOART, null, null,userSesion,anio);
			   	listaHA.addAll(listaBienesAmort);
			   	listaHA.setTotalMuebles(getCuentaBienes(Const.PATRON_MUEBLES_HISTORICOART));
			   	listaBienesAmort = getBienesAmortizables(Const.SUPERPATRON_BIENES,Const.PATRON_VEHICULOS, null, null,userSesion,anio);
			   	listaHA.addAll(listaBienesAmort);
			   	listaHA.setTotalVehiculos(getCuentaBienes(Const.PATRON_VEHICULOS));

			} catch (Exception e) {
		           logger.error("getListaHistoricoAmortizables: ", e);
//				logger.error(messages.getString("inventario.getinventario.error"));
//				ErrorDialog.show(this.getParent(), "ERROR",
//						messages.getString("inventario.getinventario.error"),
//						StringUtil.stackTrace(e));
			}
	 
			return listaHA;
		}
	    
	   


		public void returnHistoricoBienAmortizado(ObjectOutputStream oos, Long idBien,String patron) throws Exception{
			
	    	Connection conn= CPoolDatabase.getConnection();
	    	
	    	if (conn==null)
	    		throw new Exception("No se ha conseguido establecer la conexion");
	    	
	    	 try{
		    	   ListaHistoricoAmortizaciones listaHA=getListaHistoricoAmortizables(conn,idBien,patron);

		    	    oos.writeObject(listaHA);
		        }catch(Exception e){
		           logger.error("returnHistoricoBienAmortizado: ", e);
		           oos.writeObject(new ACException(e));
		           throw e;
				} finally {
					try {
						if (conn != null)
							conn.close();
						} catch (Exception e) {
							logger.error("No se ha podido cerrar la conexion");
						}
				}
		        
		        
			
		}


		private ListaHistoricoAmortizaciones getListaHistoricoAmortizables(
				Connection conn, Long idBien,String patron) throws Exception {
				ListaHistoricoAmortizaciones listaHBA= new ListaHistoricoAmortizaciones();
				String tipoBien=null;
				String tipoEspecificoBien=null;
				String coste=null;
				String table=null;
				  
				  if (patron.equalsIgnoreCase(Const.PATRON_INMUEBLES_URBANOS)){
					  tipoBien="inmuebles";
					  tipoEspecificoBien=Const.INMUEBLES_URBANOS;
						coste="valor_adquisicion_inmueble";
						table="12003";
				  }
			    	else if (patron.equalsIgnoreCase(Const.PATRON_INMUEBLES_RUSTICOS)){
			    		tipoBien="inmuebles";
						tipoEspecificoBien=Const.INMUEBLES_RUSTICOS;
						coste="valor_adquisicion_inmueble";
						table="12003";
					
			      }
			    	else if (patron.equalsIgnoreCase(Const.PATRON_MUEBLES_HISTORICOART)){
			    		tipoBien=Const.MUEBLES;
			    		coste="coste_adquisicion";
			    		table="12004";
			    	}
			    	else if (patron.equalsIgnoreCase(Const.PATRON_VEHICULOS)){
			    		tipoBien=Const.VEHICULOS;
			    		coste="coste_adquisicion";
			    		table="12007";
			    	}
				  
			  String sSQL="select "+ tipoBien+"."+coste+" AS COSTE_ADQUISICION ,historico_amortizacion.anio,historico_amortizacion.total_amortizado_anio," +
			  		" historico_amortizacion.total_amortizado_porcentaje,historico_amortizacion.id_cuenta_contable,cuentas_contables.cuenta_contable," +
			  		" cuentas_contables.descripcion AS CC_DESCRIPCION,cuentas_contables.cuenta AS CC_CUENTA,historico_amortizacion.id_cuenta_amortizacion,cuentas_amortizacion.cuenta AS CA_CUENTA," +
			  		" cuentas_amortizacion.total_amortizado,cuentas_amortizacion.descripcion AS CA_DESCRIPCION,cuentas_amortizacion.anios, cuentas_amortizacion.porcentaje," +
			  		" cuentas_amortizacion.tipo_amortizacion" +
			  		" from historico_amortizacion" +
			  		" left join "+tipoBien+" on "+tipoBien+".id=historico_amortizacion.id_bien" +
			  		" inner join bienes_inventario on bienes_inventario.id="+tipoBien+".id and bienes_inventario.revision_actual="+tipoBien+".revision_actual" +
			  		" AND bienes_inventario.id_municipio= ? AND bienes_inventario.tipo= ? " +
			  		" AND bienes_inventario.borrado = '0' AND (bienes_inventario.revision_expirada=9999999999 or (bienes_inventario.borrado='2'" +
			  		" and bienes_inventario.revision_actual=bienes_inventario.revision_expirada))" +
			  		" inner join versiones on versiones.revision="+tipoBien+".revision_actual" +
			  		" inner join iuseruserhdr on versiones.id_autor=iuseruserhdr.id" +
			  		" inner join tables_inventario on versiones.id_table_versionada=tables_inventario.id_table " ;
			  		if(tipoEspecificoBien!=null)
	   					sSQL+=" inner join "+tipoEspecificoBien+" on "+tipoBien+".id="+tipoEspecificoBien+".id  and "+tipoBien+".revision_actual="+tipoEspecificoBien+".revision_actual ";
	   				
			  		sSQL+=" AND "+ tipoBien+".revision_actual <= (select coalesce(max(v.revision),0) from versiones v, tables_inventario t where v.fecha <=localtimestamp" +
			  		" and v.id_table_versionada=t.id_table and t.name='"+tipoBien+"') " +
			  		" AND tables_inventario.name='"+tipoBien+"' " +
			  		" AND versiones.id_table_versionada="+table;
			  		if(tipoEspecificoBien!=null)
	   					sSQL+=" and "+tipoBien+".revision_actual="+tipoEspecificoBien+".revision_actual ";
			  		sSQL+=" AND "+tipoBien+".fecha_adquisicion IS NOT NULL AND "+tipoBien+"."+coste+">0" +
			  		" left join contable cuentas_contables on  historico_amortizacion.id_cuenta_contable=cuentas_contables.id_clasificacion" +
			  		" left join amortizacion cuentas_amortizacion on historico_amortizacion.id_cuenta_amortizacion IS NOT NULL" +
			  		" AND  historico_amortizacion.id_cuenta_amortizacion=cuentas_amortizacion.id" +
			  		" WHERE historico_amortizacion.id_bien= ? " +
			  		" ORDER BY historico_amortizacion.anio";

			   PreparedStatement ps=null;
			   ResultSet rs=null;
	           HistoricoAmortizacionesBean hab= null;
			   try{
			       conn=CPoolDatabase.getConnection();
		    		if (conn==null)
		        		throw new Exception("No se ha conseguido establecer la conexion");
		        	
			       ps=conn.prepareStatement(sSQL);
			       ps.setString(1, this.municipio);
			       ps.setString(2,patron);
			       ps.setLong(3,idBien);
			       rs=ps.executeQuery();                  
                   while (rs.next()){
                	   hab= new HistoricoAmortizacionesBean();
	                   hab.setIdBien(idBien);
	                   hab.setAnio(rs.getInt("anio"));
	                   hab.setCoste(rs.getDouble("COSTE_ADQUISICION"));
	                   CuentaAmortizacion cuenta =new CuentaAmortizacion();
	                   cuenta.setAnnos(rs.getInt("anios"));
	                   cuenta.setCuenta(rs.getString("CA_CUENTA"));
	                   cuenta.setDescripcion(rs.getString("CA_DESCRIPCION"));
	                   cuenta.setId(rs.getLong("id_cuenta_amortizacion"));
	                   cuenta.setPorcentaje(rs.getDouble("porcentaje"));
	                   cuenta.setTipoAmortizacion(rs.getString("tipo_amortizacion"));
	                   cuenta.setTotalAmortizado(rs.getDouble("total_amortizado"));
	                   hab.setCuentaAmortizacion(cuenta);
	                   CuentaContable cuentaContable=new CuentaContable();
	                   cuentaContable.setCuenta(rs.getString("CC_CUENTA"));
	                   cuentaContable.setDescripcion(rs.getString("CC_DESCRIPCION"));
	                   cuentaContable.setId(rs.getLong("id_cuenta_contable"));
	                   hab.setCuentaContable(cuentaContable);
	                   hab.setValorAmorAnio(rs.getDouble("total_amortizado_anio"));
		               hab.setValorAmorPorc(rs.getDouble("total_amortizado_porcentaje"));
		               listaHBA.add(hab);

                   }
        }catch(Exception e){
           logger.error("returnHistoricoBienAmortizado: ", e);
           throw e;
		} finally {
			try {
				if (conn != null)
					conn.close();
				} catch (Exception e) {
					logger.error("No se ha podido cerrar la conexion");
				}
		}
        return listaHBA;
	}
		
		 private int getCuentaBienes(String patron) throws Exception {
				String tipoBien=null;
				String tipoEspecificoBien=null;
				int cuenta=-1;
				String table=null;
				String sSQL= "";
				  
				  if (patron.equalsIgnoreCase(Const.PATRON_INMUEBLES_URBANOS)){
					  tipoBien="inmuebles";
					  tipoEspecificoBien=Const.INMUEBLES_URBANOS;
						table="12003";
				  }
			    	else if (patron.equalsIgnoreCase(Const.PATRON_INMUEBLES_RUSTICOS)){
			    		tipoBien="inmuebles";
						tipoEspecificoBien=Const.INMUEBLES_RUSTICOS;
						table="12003";
					
			      }
			    	else if (patron.equalsIgnoreCase(Const.PATRON_MUEBLES_HISTORICOART)){
			    		tipoBien=Const.MUEBLES;
			    		table="12004";
			    	}
			    	else if (patron.equalsIgnoreCase(Const.PATRON_VEHICULOS)){
			    		tipoBien=Const.VEHICULOS;
			    		table="12007";
			    	}
			   sSQL="select count(*) " +
			   		"from "+ tipoBien+
			    	" inner join bienes_inventario on bienes_inventario.id="+tipoBien+".id and bienes_inventario.revision_actual="+tipoBien+".revision_actual "+
			    	" inner join versiones on versiones.revision="+tipoBien+".revision_actual " +
			    	" inner join iuseruserhdr on versiones.id_autor=iuseruserhdr.id " +
			    	" inner join tables_inventario on versiones.id_table_versionada=tables_inventario.id_table ";
			   		if(tipoEspecificoBien!=null)
			   			sSQL+=" inner join "+tipoEspecificoBien+" on "+tipoBien+".id="+tipoEspecificoBien+".id  and "+tipoBien+".revision_actual="+tipoEspecificoBien+".revision_actual ";
			   		sSQL+=" WHERE bienes_inventario.id_municipio=? AND bienes_inventario.tipo=? " +
			    				"and "+tipoBien+".revision_actual <= (select coalesce(max(v.revision),0) from versiones v, tables_inventario t where v.fecha <=localtimestamp " +
			    				"and v.id_table_versionada=t.id_table and t.name='"+tipoBien+"')  " +
			    				"AND tables_inventario.name='"+tipoBien+"' "+
			    				"AND bienes_inventario.borrado = '0' and (bienes_inventario.revision_expirada=9999999999 " +
			    				"or (bienes_inventario.borrado='2' and bienes_inventario.revision_actual=bienes_inventario.revision_expirada)) " +
			    				"and versiones.id_table_versionada="+table;
			   				if(tipoEspecificoBien!=null)
			   					sSQL+=" and "+tipoBien+".revision_actual="+tipoEspecificoBien+".revision_actual ";
			   
			  
		       
		       logger.info("Sentencia getCuentaBienes:"+sSQL);
		       Connection conn=null;
		       HashMap alRet= new HashMap();
		       PreparedStatement ps= null;
		       ResultSet rs= null;
			        try{
			    		conn= CPoolDatabase.getConnection();
			    		if (conn==null)
			        		throw new Exception("No se ha conseguido establecer la conexion");
			        	
			            ps= conn.prepareStatement(sSQL);
			            ps.setString(1, this.municipio);
			            ps.setString(2,patron);
			            rs= ps.executeQuery();
			            rs.next();
		                cuenta= rs.getInt("count");

			        }catch(Exception ex){
			        	logger.error("Error al ejecutar: "+sSQL);
			        	throw ex;
					} finally {
						
							try{rs.close();}catch(Exception e){e.printStackTrace();};
					        try{ps.close();}catch(Exception e){e.printStackTrace();};
					        try {
								if (conn != null)
									conn.close();
								} catch (Exception e) {
									logger.error("No se ha podido cerrar la conexion");
							}
			        }

			        return cuenta;
			}
		   /**
		     * TODO: Metodo que obtiene el valor de la amortizacion desde la fecha de adquisicion hasta la fecha de amortizacion
		     * @throws Exception 
		     */
		    	private double calcularTotalAmortizado(Date fechaAmortizado, Date fechaAdq, double costeAdq,CuentaAmortizacion cuentaAmort,String tipoAmort) throws Exception {
		    		logger.info("Calculando Amortizado");
		    		if (tipoAmort == null
		    				|| tipoAmort == null
		    				|| tipoAmort.equalsIgnoreCase("")
		    				|| fechaAdq==null) {
		    			return 0;
		    		}
		    		DecimalFormat formateador8= new DecimalFormat("########.##");	
		    		DecimalFormatSymbols dfs = formateador8.getDecimalFormatSymbols();
		    		dfs.setDecimalSeparator('.');	
		    		formateador8.setDecimalFormatSymbols(dfs);
		    		
		    		double totalAmor = 0;
		    		// if (tipoAmortizacionEJCBox.getSelectedItem())
		    		if (cuentaAmort != null)
		    			cuentaAmort.setTipoAmortizacion(tipoAmort);
		    		// Se obtiene los dias que han pasado desde la fecha e adquisicin a la
		    		// fecha actual
		    		Date fechaActual=null;
		    		if (fechaAmortizado==null)
		    			fechaActual =  (Date) getDate(null);
		    		else
		    			fechaActual = fechaAmortizado;

		    		int ndias = 0;
		    		try {
		    			ndias = (int) Math.floor((fechaActual.getTime() - fechaAdq
		    					.getTime()) / (1000 * 3600 * 24));
		    		} catch (Exception e) {
		    			e.printStackTrace();
		    			logger.error("No se puede calcular la amortización con la fecha de amortización nula");
		    		}
		    		if(ndias>0){
			    		if (tipoAmort.equalsIgnoreCase(POR_AÑOS)) {
			    				
			    			// totalAmor =
			    			// (costeAdquisicion/(cuentaAmortizacion.getAnnos()*365))*ndias;
			    			// Se obtiene lo que se amortiza cada año
			    			totalAmor = costeAdq / cuentaAmort.getAnnos();
			    			// Se obtiene lo que se amortiza cada dia
			    			totalAmor = totalAmor / (365);
			    			// Se obtiene lo que se ha amortizado ya
			    			totalAmor = totalAmor * ndias;
			
			    		} else if (tipoAmort.equalsIgnoreCase(POR_PORCENTAJE)) {
			    			// totalAmor =
			    			// (costeAdquisicion/(cuentaAmortizacion.getPorcentaje()*365))*ndias;
			    			// Se obtiene lo que se amortiza cada año
			    			totalAmor = costeAdq / cuentaAmort.getPorcentaje();
			    			// Se obtiene lo que se amortiza cada dia
			    			totalAmor = totalAmor / 365;
			    			// Se obtiene lo que se ha amortizado ya
			    			totalAmor = totalAmor * ndias;
			    		} else {
			    			totalAmor = 0;
			    		}
						totalAmor =Double.parseDouble(formateador8.format(totalAmor));
						if(totalAmor>=costeAdq)
							totalAmor=costeAdq;
			    		cuentaAmort.setTotalAmortizado(totalAmor);
		    		}else{
		    			logger.error("No se puede calcular la amortización para una fecha de adquisición posterior a la fecha de amoritzación");
		    			throw new RuntimeException("No se puede calcular la amortización para una fecha de adquisición posterior a la fecha de amoritzación");

		    		}
		    		return (cuentaAmort.getTotalAmortizado());

		    	}
		    	/**
		    	 * Calcula el valor amortizado por año
		    	 * @param anio
		    	 * @param fechaAdq
		    	 * @param costeAdq
		    	 * @param cuentaAmort
		    	 * @param tipoAmort
		    	 * @return
		    	 * @throws Exception
		    	 */
		   	private double calcularTotalAmortizadoPorAnio(Date fechaAdq,Integer anio,double costeAdq,CuentaAmortizacion cuentaAmort,String tipoAmort) throws Exception {
		   		SimpleDateFormat sdf= new SimpleDateFormat("dd-MM-yyyy"); 
		   		String fechaAmortizadoString="31-12-"+anio;
		   		Date fecha=fechaAdq;
		   		Date fechaAmortizado=sdf.parse(fechaAmortizadoString); 
		   		if(fechaAdq.getYear()+1900<anio){
		   			String fechaAdqString="01-12-"+anio;
		   			fecha=sdf.parse(fechaAdqString);
		   		}
		   		return calcularTotalAmortizado(fechaAmortizado, fecha, costeAdq, cuentaAmort, tipoAmort);
		   		
		   	}
}
