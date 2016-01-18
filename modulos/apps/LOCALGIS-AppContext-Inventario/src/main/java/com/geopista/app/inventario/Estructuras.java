/**
 * Estructuras.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.inventario;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.global.WebAppConstants;
import com.geopista.protocol.ListaEstructuras;
import com.geopista.protocol.administrador.OperacionesAdministrador;
import com.geopista.protocol.administrador.dominios.ListaDomain;
import com.geopista.util.exception.CancelException;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 10-jul-2006
 * Time: 14:19:01
 * To change this template use File | Settings | File Templates.
 */
public class Estructuras {
	
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Estructuras.class);

    private static boolean cargada=false;
    private static boolean iniciada=false;
    private static boolean cancelada=false;
    private static ListaEstructuras listaClasificacionBienesPatrimonio= new ListaEstructuras("Clasificacion de los Bienes del Patrimonio");
    private static ListaEstructuras listaSubtipoBienesPatrimonio= new ListaEstructuras("Tipo de bien patrimonial");
    private static ListaEstructuras listaSubtipoLotes= new ListaEstructuras("Tipo lotes");
    private static ListaEstructuras listaSubtipoPatrimonioMunicipal= new ListaEstructuras("Tipo patrimonio municipal del suelo");
    private static ListaEstructuras listaUsoJuridico= new ListaEstructuras("Uso jurídico del bien");
    private static ListaEstructuras listaPropiedadPatrimonial= new ListaEstructuras("Tipo de propiedad patrimonial");
    private static ListaEstructuras listaFormaAdquisicion= new ListaEstructuras("Forma de adquisicion");
    private static ListaEstructuras listaAprovechamiento= new ListaEstructuras("Aprovechamiento");
    private static ListaEstructuras listaClasificacionContable= new ListaEstructuras("Clasificación contable");
    private static ListaEstructuras listaEstadoConservacion= new ListaEstructuras("Estado de conservación");
    private static ListaEstructuras listaTipoCarpinteria= new ListaEstructuras("Tipo de carpintería");
    private static ListaEstructuras listaTipoConstruccion= new ListaEstructuras("Tipo de construcción");
    private static ListaEstructuras listaTipoFachada= new ListaEstructuras("Tipo de fachada");
    private static ListaEstructuras listaTipoCubierta= new ListaEstructuras("Tipo de cubierta");
    private static ListaEstructuras listaTipoAmortizacion= new ListaEstructuras("Tipo de Amortizacion");
    private static ListaEstructuras listaUsosFuncionales= new ListaEstructuras("Uso Funcional");
    private static ListaEstructuras listaClasesValorMobiliario= new ListaEstructuras("Clase Valor Mobiliario");
    private static ListaEstructuras listaConceptosCreditosDerechos= new ListaEstructuras("Concepto Credito y Derecho");
    private static ListaEstructuras listaRazaSemoviente= new ListaEstructuras("Raza Semoviente");
    private static ListaEstructuras listaTiposViaINE= new ListaEstructuras("Tipos de via normalizados del INE");
    private static ListaEstructuras listaTiposVehiculo= new ListaEstructuras("Tipo Vehiculo");
    private static ListaEstructuras listaTraccion= new ListaEstructuras("Traccion");
    private static ListaEstructuras listaDocumentos= new ListaEstructuras("Tipo Documento");
    private static ListaEstructuras listaFormatoInforme= new ListaEstructuras("Formato Informe");
    private static ListaEstructuras listaTransmision= new ListaEstructuras("Transmision");
    private static ListaEstructuras listaClaseDerechosReales= new ListaEstructuras("Clase Derechos Reales");
    private static ListaEstructuras listaClaseCredito= new ListaEstructuras("Clase Credito");
    private static ListaEstructuras listaSubclaseCredito= new ListaEstructuras("Subclase Credito");
    private static ListaEstructuras listaClaseRustica= new ListaEstructuras("Clase Rustica");
    private static ListaEstructuras listaClaseUrbana= new ListaEstructuras("Clase Urbana");
    private static ListaEstructuras listaClaseMuebles= new ListaEstructuras("Clase Muebles");
    private static ListaEstructuras listaCrecDesarrollo= new ListaEstructuras("Etapas crecimiento y Desarrollo semoviente");
    private static ListaEstructuras listaClaseBienRevertible= new ListaEstructuras("Clase Bien Revertible");
    private static ListaEstructuras listaDiagnosis= new ListaEstructuras("Diagnosis");
    
    
    private static String CATEGORIA_DOMINIO_INVENTARIO="4";
	private static ListaDomain listaDomain;
	private static ListaDomain listaDomainParticular;
	
	private static TaskMonitorDialog progressDialog1;

    public static ListaEstructuras getListaFormatoInforme() {
        return listaFormatoInforme;
    }
    
    public static ListaEstructuras getListaTransmision() {
        return listaTransmision;
    }

    public static ListaEstructuras getListaDocumentos() {
        return listaDocumentos;
    }

    public static ListaEstructuras getListaTiposVehiculo() {
        return listaTiposVehiculo;
    }

    public static ListaEstructuras getListaTraccion() {
        return listaTraccion;
    }

    public static ListaEstructuras getListaTiposViaINE() {
        return listaTiposViaINE;
    }

    public static ListaEstructuras getListaRazaSemoviente() {
        return listaRazaSemoviente;
    }

    public static ListaEstructuras getListaConceptosCreditosDerechos() {
        return listaConceptosCreditosDerechos;
    }

    public static ListaEstructuras getListaClasesValorMobiliario() {
        return listaClasesValorMobiliario;
    }

    public static ListaEstructuras getListaUsosFuncionales() {
        return listaUsosFuncionales;
    }

    public static ListaEstructuras getListaClasificacionBienesPatrimonio() {
        return listaClasificacionBienesPatrimonio;
    }

    public static ListaEstructuras getListaSubtipoBienesPatrimonio() {
        return listaSubtipoBienesPatrimonio;
    }

    public static ListaEstructuras getListaSubtipoLotes() {
        return listaSubtipoLotes;
    }
    
    public static ListaEstructuras getListaSubtipoPatrimonioMunicipal() {
        return listaSubtipoPatrimonioMunicipal;
    }
    
    public static ListaEstructuras getListaUsoJuridico() {
        return listaUsoJuridico;
    }

    public static ListaEstructuras getListaPropiedadPatrimonial() {
        return listaPropiedadPatrimonial;
    }

    public static ListaEstructuras getListaFormaAdquisicion() {
        return listaFormaAdquisicion;
    }

    public static ListaEstructuras getListaAprovechamiento() {
        return listaAprovechamiento;
    }

    public static ListaEstructuras getListaClasificacionContable() {
        return listaClasificacionContable;
    }


    public static ListaEstructuras getListaEstadoConservacion() {
        return listaEstadoConservacion;
    }

    public static ListaEstructuras getListaTipoCarpinteria() {
        return listaTipoCarpinteria;
    }

    public static ListaEstructuras getListaTipoConstruccion() {
        return listaTipoConstruccion;
    }

    public static ListaEstructuras getListaTipoFachada() {
        return listaTipoFachada;
    }


    public static ListaEstructuras getListaTipoCubierta() {
        return listaTipoCubierta;
    }

    public static ListaEstructuras getListaTipoAmortizacion() {
        return listaTipoAmortizacion;
    }


    public static ListaEstructuras getListaClaseDerechosReales() {
		return listaClaseDerechosReales;
	}
    
    public static ListaEstructuras getListaClaseCredito() {
		return listaClaseCredito;
	}
    
    public static ListaEstructuras getListaSubclaseCredito() {
		return listaSubclaseCredito;
	}
    public static ListaEstructuras getListaClaseRustica() {
		return listaClaseRustica;
	}
    public static ListaEstructuras getListaClaseUrbana() {
		return listaClaseUrbana;
	}
    public static ListaEstructuras getListaClaseBienRevertible() {
		return listaClaseBienRevertible;
	}
    public static ListaEstructuras getListaClaseMuebles() {
		return listaClaseMuebles;
	}
    public static ListaEstructuras getListaCrecDesarrollo() {
		return listaCrecDesarrollo;
	}
    
	public static ListaEstructuras getListaDiagnosis() {
		return listaDiagnosis;
	}


	public static void cargarEstructuras() throws CancelException{
       if (iniciada || cargada) return;
       iniciada=true;
       final AppContext aplicacion = (AppContext) AppContext.getApplicationContext();

       final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion
                .getMainFrame(), null);
       
       
        progressDialog.setTitle(aplicacion.getI18nString("CargandoDatosIniciales"));
        progressDialog.report(aplicacion.getI18nString("CargandoDatosIniciales"));
        progressDialog.addComponentListener(new ComponentAdapter()
        {
                public void componentShown(ComponentEvent e)
                {

                    // Wait for the dialog to appear before starting the
                    // task. Otherwise
                    // the task might possibly finish before the dialog
                    // appeared and the
                    // dialog would never close. [Jon Aquino]
                    new Thread(new Runnable()
                        {
                            public void run()
                            {
						            try
						            {
						            	progressDialog1=progressDialog;
						            	//Cargamos la lista de dominios disponibles en el sistema
						               cargarListaDominios();						            	
                                       cargarLista(listaClasificacionBienesPatrimonio);
                                       cargarLista(listaSubtipoBienesPatrimonio);
                                       cargarLista(listaSubtipoPatrimonioMunicipal);
                                       cargarLista(listaSubtipoLotes);
                                       cargarLista(listaUsoJuridico);
                                       cargarLista(listaPropiedadPatrimonial);
                                       cargarLista(listaFormaAdquisicion);
                                       cargarLista(listaAprovechamiento);
                                       cargarLista(listaClasificacionContable);
                                       cargarLista(listaEstadoConservacion);
                                       cargarLista(listaTipoCarpinteria);
                                       cargarLista(listaTipoConstruccion);
                                       cargarLista(listaTipoFachada);
                                       cargarLista(listaTipoCubierta);
                                       cargarLista(listaTipoAmortizacion);
                                       cargarLista(listaUsosFuncionales);
                                       cargarLista(listaClasesValorMobiliario);
                                       cargarLista(listaConceptosCreditosDerechos);
                                       cargarLista(listaRazaSemoviente);
                                       cargarLista(listaTiposViaINE);
                                       cargarLista(listaTiposVehiculo);
                                       cargarLista(listaTraccion);
                                       cargarLista(listaDocumentos);
                                       cargarLista(listaFormatoInforme);
                                       cargarLista(listaTransmision);
                                       cargarLista(listaClaseDerechosReales);
                                       cargarLista(listaClaseCredito);
                                       cargarLista(listaSubclaseCredito);
                                       cargarLista(listaClaseRustica);
                                       cargarLista(listaClaseUrbana);
                                       cargarLista(listaClaseMuebles);
                                       cargarLista(listaCrecDesarrollo);
                                       cargarLista(listaClaseBienRevertible);
                                       cargarLista(listaDiagnosis);
                                       //Thread.sleep(30000);
                                       

                               }						       
						        catch(Exception e){
						        	if ((progressDialog!=null) && (progressDialog.isCancelRequested())){
						        		logger.warn("Carga de estructuras cancelada");		
		    							//JOptionPane.showMessageDialog(aplicacion.getMainFrame(),"Carga de estructuras cancelada");
		    							cancelada=true;
						        	}						        	
						        }
                                finally
								    {
								        progressDialog.setVisible(false);
                                        cargada=true;
								    }
								}

							
								}).start();
								}
								});
		GUIUtil.centreOnWindow(progressDialog);
		progressDialog.setVisible(true);

   }
	
		
   public static void cargarListaDominios(){
	   	    
	   
	   
		try {
			
		    String url= ((AppContext) AppContext.getApplicationContext()).getString(UserPreferenceConstants.LOCALGIS_SERVER_URL) + WebAppConstants.ADMINISTRACION_WEBAPP_NAME;

			listaDomain =new ListaDomain();
			listaDomainParticular =new ListaDomain();
			
			OperacionesAdministrador operacionAdministrador=new OperacionesAdministrador(url);
			operacionAdministrador.addTaskMonitor(progressDialog1);
			operacionAdministrador.getDominios(CATEGORIA_DOMINIO_INVENTARIO,listaDomain,listaDomainParticular);
			logger.info("Lista de dominios cargada");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

   }
   
   public static boolean existDomain(String domainname){
	   
	   //Si los dos son nulos dejamos seguir ya que vamos 
	   //a dejar insertar los elementos.
	   if ((listaDomain==null) && (listaDomainParticular==null)){
		   return true;
	   }
		   
	   if (listaDomainParticular!=null){
		   if (listaDomainParticular.getByName(domainname)!=null)
			   return true;
	   }
	   if (listaDomain!=null){
		   if (listaDomain.getByName(domainname)!=null)
			   return true;
		   
	   }
	   return false;
   }
	
	
   public static void cargarLista(ListaEstructuras lista) throws CancelException{
	   
	   if ((progressDialog1!=null) && (progressDialog1.isCancelRequested())){
   			throw new CancelException("Cancel");										
   		}
	   
      String url= ((AppContext) AppContext.getApplicationContext()).getString(UserPreferenceConstants.LOCALGIS_SERVER_URL) + WebAppConstants.INVENTARIO_WEBAPP_NAME;      
      lista.addTaskMonitor(progressDialog1);
      if (!lista.loadDB(url))
              lista.loadFile();
      lista.saveFile();
   }

   public static boolean isCargada() {
       return cargada;
   }
   public static boolean isCancelada() {
       return cancelada;
   }

   public static void setCargada(boolean cargada) {
       Estructuras.cargada = cargada;
   }

   public static boolean isIniciada() {
       return iniciada;
   }

   public static void setIniciada(boolean iniciada) {
       Estructuras.iniciada = iniciada;
   }


}
