package com.geopista.app.catastro.registroExpedientes;

import java.util.ArrayList;

import com.geopista.app.catastro.model.beans.DatosConfiguracion;
import com.geopista.app.catastro.model.beans.EstadoSiguiente;
import com.geopista.app.catastro.model.beans.Expediente;
import com.geopista.app.catastro.registroExpedientes.utils.ConstantesRegistroExp;

/**
 * Clase que determina que accion es la siguiente a realizar y a que estado va a pasar el expediente, segun los
 * parametros que se pasan a la funcion controlDeFlujo. Cuando se inicializa carga los posibles estados de los
 * expediente y las posibles transiciones.
 */

public class MaquinaEstadoFlujo
{
    private ArrayList codigoEstados;
    private ArrayList estadoSiguiente;

    public MaquinaEstadoFlujo()
    {
        inicializaEstructurasBD();
    }

    /**
     * Funcion que accede a base de datos para coger los posibles estados de los expedientes y las posibles
     * transiciones entre los estados segun el modo en que se este ejecutando la aplicacion.
     */
    private void inicializaEstructurasBD()
    {
        try
        {
            codigoEstados = ConstantesRegistroExp.clienteCatastro.getCodigoEstados();
            estadoSiguiente = ConstantesRegistroExp.clienteCatastro.getEstadoSiguiente();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Calcula el estado siguiente segun el estado actual y el modo en que se este ejecutando la aplicacion.
     */
    public long estadoSiguiente(long estadoActual)
    {
        for(int i = 0; i< estadoSiguiente.size();i++)
        {
            if(estadoActual==((EstadoSiguiente)estadoSiguiente.get(i)).getEstadoActual()&&
                    ConstantesRegistroExp.modoTrabajo.equalsIgnoreCase(((EstadoSiguiente)estadoSiguiente.get(i)).getModo()))
            {
                long estSig = ((EstadoSiguiente)estadoSiguiente.get(i)).getEstadoSiguiente();
                if(estSig>estadoActual)
                {
                    return estSig;
                }
            }
        }
        return -1;
    }

    /**
     * Calcula el estado anterior segun el estado actual y el modo en que se este ejecutando la aplicacion.
     */
    public long estadoAnterior(long estadoActual)
    {
        for(int i = 0; i< estadoSiguiente.size();i++)
        {
            if(estadoActual==((EstadoSiguiente)estadoSiguiente.get(i)).getEstadoActual()&&
                    ConstantesRegistroExp.modoTrabajo.equalsIgnoreCase(((EstadoSiguiente)estadoSiguiente.get(i)).getModo()))
            {
                long estSig = ((EstadoSiguiente)estadoSiguiente.get(i)).getEstadoSiguiente();
                if(estSig<estadoActual)
                {
                    return estSig;
                }
            }
        }
        return -1;
    }    

    /**
     * Funcion que determina que accion es la siguiente a realizar en la aplicacion. Para ello requiere el expediente
     * y la accion y frame de la que proviene, definidas en constantes en ConstantesRegristroExp. Segun el estado del
     * expediente y de la ventana y accion de donde proviene decide el flujo de la aplicacion y si el expediente debe
     * cambiar de estado. Tambia actualiza los botones que se deben mostrar en la pantalla de Gestion de expedientes,
     * segun el  nuevo estado del expediente.
     */
    public int controlDeFlujo(Expediente exp, int frameOrigenAccion)
    {
        int resultado = ConstantesRegistroExp.FRAME_ACCION_DEFAULT;
        if(exp!=null)
        {
            
            int idEstadoExp = (int)exp.getIdEstado();
            switch(idEstadoExp)
            {
                case ConstantesRegistroExp.ESTADO_REGISTRADO:
                {
                    switch(frameOrigenAccion)
                    {
                        case ConstantesRegistroExp.FRAME_ORIGEN_CREAR_EXP_ACCION_GUARDAR:
                        {
                           /* if(ConstantesRegistroExp.modoTrabajo.equalsIgnoreCase(DatosConfiguracion.MODO_TRABAJO_ACOPLADO))
                            {
                                exp.setIdEstado(estadoSiguiente(exp.getIdEstado()));
                            }*/
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_GESTION_EXP_CREA_EXP;
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_CREAR_EXP_ACCION_SIGUIENTE:
                        {
                            /*if(ConstantesRegistroExp.modoTrabajo.equalsIgnoreCase(DatosConfiguracion.MODO_TRABAJO_ACOPLADO))
                            {
                                exp.setIdEstado(estadoSiguiente(exp.getIdEstado()));
                            }*/

                            if(ConstantesRegistroExp.tipoConvenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO))
                            {
                                if(exp.getTipoExpediente().getConvenio().equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO))
                                {
                                    resultado = ConstantesRegistroExp.FRAME_DESTINO_ASOCIAR_PARCELAS_CREAR_EXP;
                                }
                                else if(exp.getTipoExpediente().getConvenio().equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD))
                                {
                                    resultado = ConstantesRegistroExp.FRAME_DESTINO_ASOCIAR_BIEN_INMUEBLE_CREAR_EXP;
                                }
                            }
                            else if(ConstantesRegistroExp.tipoConvenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD))
                            {
                                resultado = ConstantesRegistroExp.FRAME_DESTINO_ASOCIAR_BIEN_INMUEBLE_CREAR_EXP;                                
                            }

                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_ASOCIAR_BIEN_INMUEBLE_ACCION_GUARDAR:
                        {
                            if(exp.getListaReferencias()!=null && exp.getListaReferencias().size()>0)
                            {
                                exp.setIdEstado(estadoSiguiente(exp.getIdEstado()));
                                resultado = ConstantesRegistroExp.FRAME_DESTINO_GESTION_EXP_UPDATE_EXP;
                            }
                            else
                            {
                                resultado = ConstantesRegistroExp.FRAME_DESTINO_GESTION_EXP;
                            }
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_ASOCIAR_PARCELA_ACCION_GUARDAR:
                        {
                            if(exp.getListaReferencias()!=null && exp.getListaReferencias().size()>0)
                            {
                                exp.setIdEstado(estadoSiguiente(exp.getIdEstado()));
                                resultado = ConstantesRegistroExp.FRAME_DESTINO_GESTION_EXP_UPDATE_EXP;
                            }
                            else
                            {
                                resultado = ConstantesRegistroExp.FRAME_DESTINO_GESTION_EXP;
                            }
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_GESTION_EXP_VER_ESTADO:
                        {
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_VER_ESTADO;
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_GESTION_EXP_ASOCIAR_PARCELA_BIEN_INMUEBLE:
                        {

                            if(ConstantesRegistroExp.tipoConvenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO))
                            {
                                if(exp.getTipoExpediente().getConvenio().equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO))
                                {
                                    resultado = ConstantesRegistroExp.FRAME_DESTINO_ASOCIAR_PARCELAS;
                                }
                                else if(exp.getTipoExpediente().getConvenio().equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD))
                                {
                                    resultado = ConstantesRegistroExp.FRAME_DESTINO_ASOCIAR_BIEN_INMUEBLE;
                                }
                            }
                            else if(ConstantesRegistroExp.tipoConvenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD))
                            {
                                resultado = ConstantesRegistroExp.FRAME_DESTINO_ASOCIAR_BIEN_INMUEBLE;
                            }
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_BUSQUEDA_ACCION_ACEPTAR:
                        {
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_GESTION_EXP;
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_ASOCIAR_PARCELA_ACCION_SALIR:
                        {
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_GESTION_EXP;
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_ASOCIAR_BIEN_INMUEBLE_ACCION_SALIR:
                        {
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_GESTION_EXP;
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_MODIFICAR_EXP_ACCION_GUARDAR:
                        {
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_GESTION_EXP_UPDATE_EXP;
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_MODIFICAR_EXP_ACCION_SALIR:
                        {
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_GESTION_EXP;
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_GESTION_EXP_ACCION_MODIFICAR_DATOS_EXP:
                        {
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_MODIFICAR_DATOS_EXP;
                            break;
                        }     
                        case ConstantesRegistroExp.FRAME_ORIGEN_ASOCIADO_EXP:
                        {
                        	exp.setIdEstado(estadoSiguiente(exp.getIdEstado()));
                        	resultado = ConstantesRegistroExp.FRAME_DESTINO_GESTION_EXP_UPDATE_EXP;
                            break;
                        } 
                        case ConstantesRegistroExp.FRAME_ORIGEN_NO_ASOCIADO_EXP:
                        {
                        	resultado = ConstantesRegistroExp.FRAME_DESTINO_GESTION_EXP;
                            break;
                        } 
                    }
                    break;
                }
                case ConstantesRegistroExp.ESTADO_ASOCIADO:
                {
                    switch(frameOrigenAccion)
                    {
                        case ConstantesRegistroExp.FRAME_ORIGEN_ASOCIAR_PARCELA_ACCION_GUARDAR:
                        {
                            if(exp.getListaReferencias()!=null && exp.getListaReferencias().size()>0)
                            {
                                exp.setIdEstado(estadoSiguiente(exp.getIdEstado()));
                                resultado = ConstantesRegistroExp.FRAME_DESTINO_GESTION_EXP_UPDATE_EXP;
                            }
                            else
                            {
                                resultado = ConstantesRegistroExp.FRAME_DESTINO_GESTION_EXP;
                            }
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_ASOCIAR_BIEN_INMUEBLE_ACCION_GUARDAR:
                        {
                            if(exp.getListaReferencias()!=null && exp.getListaReferencias().size()>0)
                            {
                                exp.setIdEstado(estadoSiguiente(exp.getIdEstado()));
                                resultado = ConstantesRegistroExp.FRAME_DESTINO_GESTION_EXP_UPDATE_EXP;
                            }
                            else
                            {
                                resultado = ConstantesRegistroExp.FRAME_DESTINO_GESTION_EXP;
                            }
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_GESTION_EXP_ASOCIAR_PARCELA_BIEN_INMUEBLE:
                        {
                            if(ConstantesRegistroExp.tipoConvenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO))
                            {
                                if(exp.getTipoExpediente().getConvenio().equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO))
                                {
                                    resultado = ConstantesRegistroExp.FRAME_DESTINO_ASOCIAR_PARCELAS;
                                }
                                else if(exp.getTipoExpediente().getConvenio().equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD))
                                {
                                    resultado = ConstantesRegistroExp.FRAME_DESTINO_ASOCIAR_BIEN_INMUEBLE;
                                }
                            }
                            else if(ConstantesRegistroExp.tipoConvenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD))
                            {
                                resultado = ConstantesRegistroExp.FRAME_DESTINO_ASOCIAR_BIEN_INMUEBLE;
                            }
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_GESTION_EXP_VER_ESTADO:
                        {
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_VER_ESTADO;
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_BUSQUEDA_ACCION_ACEPTAR:
                        {
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_GESTION_EXP;
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_ASOCIAR_PARCELA_ACCION_SALIR:
                        {
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_GESTION_EXP;
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_ASOCIAR_BIEN_INMUEBLE_ACCION_SALIR:
                        {
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_GESTION_EXP;
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_MODIFICAR_EXP_ACCION_GUARDAR:
                        {
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_GESTION_EXP_UPDATE_EXP;
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_MODIFICAR_EXP_ACCION_SALIR:
                        {
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_GESTION_EXP;
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_GESTION_EXP_ACCION_MODIFICAR_DATOS_EXP:
                        {
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_MODIFICAR_DATOS_EXP;
                            break;
                        }
                    }
                    break;
                }
                case ConstantesRegistroExp.ESTADO_RELLENADO:
                {
                    switch(frameOrigenAccion)
                    {
                        case ConstantesRegistroExp.FRAME_ORIGEN_GESTION_EXP_VER_ESTADO:
                        {
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_VER_ESTADO;
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_GESTION_EXP_ASOCIAR_PARCELA_BIEN_INMUEBLE:
                        {
                            if(ConstantesRegistroExp.tipoConvenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO))
                            {
                                if(exp.getTipoExpediente().getConvenio().equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO))
                                {
                                    resultado = ConstantesRegistroExp.FRAME_DESTINO_ASOCIAR_PARCELAS;
                                }
                                else if(exp.getTipoExpediente().getConvenio().equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD))
                                {
                                    resultado = ConstantesRegistroExp.FRAME_DESTINO_ASOCIAR_BIEN_INMUEBLE;
                                }                                 
                            }
                            else if(ConstantesRegistroExp.tipoConvenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD))
                            {
                                resultado = ConstantesRegistroExp.FRAME_DESTINO_ASOCIAR_BIEN_INMUEBLE;
                            }
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_BUSQUEDA_ACCION_ACEPTAR:
                        {

                            resultado = ConstantesRegistroExp.FRAME_DESTINO_GESTION_EXP;
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_ASOCIAR_PARCELA_ACCION_GUARDAR:
                        {
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_GESTION_EXP_UPDATE_EXP;
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_ASOCIAR_BIEN_INMUEBLE_ACCION_GUARDAR:
                        {
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_GESTION_EXP_UPDATE_EXP;
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_MODIFICAR_EXP_ACCION_GUARDAR:
                        {
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_GESTION_EXP_UPDATE_EXP;
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_MODIFICAR_EXP_ACCION_SALIR:
                        {
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_GESTION_EXP;
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_GESTION_EXP_ACCION_MODIFICAR_DATOS_EXP:
                        {
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_MODIFICAR_DATOS_EXP;
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_GESTION_EXP_ACCION_MODIFICAR_INF_CATASTRAL:
                        {
                            if(ConstantesRegistroExp.modoTrabajo.equalsIgnoreCase(DatosConfiguracion.MODO_TRABAJO_ACOPLADO))
                            {
                                resultado = ConstantesRegistroExp.FRAME_DESTINO_MODIFICAR_INF_CATASTRAL_SINCRONIZAR;
                            }
                            else
                            {
                                resultado = ConstantesRegistroExp.FRAME_DESTINO_MODIFICAR_INF_CATASTRAL_ESCRIBIR_CATASTRO_TEMPORAL;                                
                            }
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_GESTION_EXP_ACCION_ASOCIAR_DATOS_GRAFICOS:
                        {
                            if(ConstantesRegistroExp.modoTrabajo.equalsIgnoreCase(DatosConfiguracion.MODO_TRABAJO_ACOPLADO))
                            {
                                resultado = ConstantesRegistroExp.FRAME_DESTINO_ASOCIAR_DATOS_GRAFICOS_SINCRONIZAR;
                            }
                            else
                            {
                                resultado = ConstantesRegistroExp.FRAME_DESTINO_ASOCIAR_DATOS_GRAFICOS_ESCRIBIR_CATASTRO_TEMPORAL;
                            }
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_ASOCIAR_PARCELA_ACCION_SALIR:
                        {
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_GESTION_EXP;
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_ASOCIAR_BIEN_INMUEBLE_ACCION_SALIR:
                        {
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_GESTION_EXP;
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_OBTENER_INF_CATASTRO:
                        {
                        	 exp.setIdEstado(estadoSiguiente(exp.getIdEstado()));
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_GESTION_EXP_CONSULTA_CATASTRO;
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_ACTUALIZAR_CATASTRO:
                        {
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_GESTION_EXP;
                            break;
                        }
                    }
                    break;
                }
                case ConstantesRegistroExp.ESTADO_SINCRONIZADO:
                {
                    switch(frameOrigenAccion)
                    {
                        case ConstantesRegistroExp.FRAME_ORIGEN_GESTION_EXP_VER_ESTADO:
                        {
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_VER_ESTADO;
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_BUSQUEDA_ACCION_ACEPTAR:
                        {
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_GESTION_EXP;
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_GESTION_EXP_ASOCIAR_PARCELA_BIEN_INMUEBLE:
                        {
                            if(ConstantesRegistroExp.tipoConvenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO))
                            {
                                if(exp.getTipoExpediente().getConvenio().equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO))
                                {
                                    resultado = ConstantesRegistroExp.FRAME_DESTINO_ASOCIAR_PARCELAS;
                                }
                                else if(exp.getTipoExpediente().getConvenio().equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD))
                                {
                                    resultado = ConstantesRegistroExp.FRAME_DESTINO_ASOCIAR_BIEN_INMUEBLE;
                                }
                            }
                            else if(ConstantesRegistroExp.tipoConvenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD))
                            {
                                resultado = ConstantesRegistroExp.FRAME_DESTINO_ASOCIAR_BIEN_INMUEBLE;
                            }
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_ASOCIAR_PARCELA_ACCION_GUARDAR:
                        {
                            if(ConstantesRegistroExp.modoTrabajo.equalsIgnoreCase(DatosConfiguracion.MODO_TRABAJO_ACOPLADO))
                            {
                                exp.setIdEstado(estadoAnterior(exp.getIdEstado()));
                            }
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_GESTION_EXP_UPDATE_EXP_ELIMINA_CATASTRO_TEMPORAL;
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_ASOCIAR_BIEN_INMUEBLE_ACCION_GUARDAR:
                        {
                            if(ConstantesRegistroExp.modoTrabajo.equalsIgnoreCase(DatosConfiguracion.MODO_TRABAJO_ACOPLADO))
                            {
                                exp.setIdEstado(estadoAnterior(exp.getIdEstado()));
                            }
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_GESTION_EXP_UPDATE_EXP_ELIMINA_CATASTRO_TEMPORAL;
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_MODIFICAR_EXP_ACCION_GUARDAR:
                        {
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_GESTION_EXP_UPDATE_EXP;
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_MODIFICAR_EXP_ACCION_SALIR:
                        {
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_GESTION_EXP;
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_GESTION_EXP_ACCION_MODIFICAR_DATOS_EXP:
                        {
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_MODIFICAR_DATOS_EXP;
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_GESTION_EXP_ACCION_MODIFICAR_INF_CATASTRAL:
                        {
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_MODIFICAR_INF_CATASTRAL_ESCRIBIR_CATASTRO_TEMPORAL;
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_GESTION_EXP_ACCION_ASOCIAR_DATOS_GRAFICOS:
                        {
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_ASOCIAR_DATOS_GRAFICOS_ESCRIBIR_CATASTRO_TEMPORAL;
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_ASOCIAR_PARCELA_ACCION_SALIR:
                        {
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_GESTION_EXP;
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_ASOCIAR_BIEN_INMUEBLE_ACCION_SALIR:
                        {
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_GESTION_EXP;
                            break;
                        }
                    }
                    break;
                }
                case ConstantesRegistroExp.ESTADO_MODIFICADO:
                {
                    switch(frameOrigenAccion)
                    {
                        case ConstantesRegistroExp.FRAME_ORIGEN_GESTION_EXP_VER_ESTADO:
                        {
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_VER_ESTADO;
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_BUSQUEDA_ACCION_ACEPTAR:
                        {
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_GESTION_EXP;
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_GESTION_EXP_ASOCIAR_PARCELA_BIEN_INMUEBLE:
                        {
                            if(ConstantesRegistroExp.tipoConvenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO))
                            {
                                if(exp.getTipoExpediente().getConvenio().equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO))
                                {
                                    resultado = ConstantesRegistroExp.FRAME_DESTINO_ASOCIAR_PARCELAS;
                                }
                                else if(exp.getTipoExpediente().getConvenio().equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD))
                                {
                                    resultado = ConstantesRegistroExp.FRAME_DESTINO_ASOCIAR_BIEN_INMUEBLE;
                                }
                            }
                            else if(ConstantesRegistroExp.tipoConvenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD))
                            {
                                resultado = ConstantesRegistroExp.FRAME_DESTINO_ASOCIAR_BIEN_INMUEBLE;
                            }
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_ASOCIAR_PARCELA_ACCION_GUARDAR:
                        {
                            exp.setIdEstado(estadoAnterior(exp.getIdEstado()));
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_GESTION_EXP_UPDATE_EXP_ELIMINA_CATASTRO_TEMPORAL;
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_ASOCIAR_BIEN_INMUEBLE_ACCION_GUARDAR:
                        {
                            exp.setIdEstado(estadoAnterior(exp.getIdEstado()));                            
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_GESTION_EXP_UPDATE_EXP_ELIMINA_CATASTRO_TEMPORAL;
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_MODIFICAR_EXP_ACCION_GUARDAR:
                        {
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_GESTION_EXP_UPDATE_EXP;
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_MODIFICAR_EXP_ACCION_SALIR:
                        {
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_GESTION_EXP;
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_GESTION_EXP_ACCION_MODIFICAR_DATOS_EXP:
                        {
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_MODIFICAR_DATOS_EXP;
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_GESTION_EXP_ACCION_MODIFICAR_INF_CATASTRAL:
                        {
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_MODIFICAR_INF_CATASTRAL;
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_GESTION_EXP_ACCION_ASOCIAR_DATOS_GRAFICOS:
                        {
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_ASOCIAR_DATOS_GRAFICOS;
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_MODIFICAR_INF_CATASTRAL_ACCION_SALIR:
                        {
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_GESTION_EXP;
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_ASOCIAR_PARCELA_ACCION_SALIR:
                        {
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_GESTION_EXP;
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_ASOCIAR_BIEN_INMUEBLE_ACCION_SALIR:
                        {
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_GESTION_EXP;
                            break;
                        }
                       
                    }
                    break;
                }
                case ConstantesRegistroExp.ESTADO_FINALIZADO:
                {
                    switch(frameOrigenAccion)
                    {
                        case ConstantesRegistroExp.FRAME_ORIGEN_GESTION_EXP_VER_ESTADO:
                        {
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_VER_ESTADO;
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_BUSQUEDA_ACCION_ACEPTAR:
                        {
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_GESTION_EXP;
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_GESTION_EXP_ACCION_EXPORTAR_FICHERO:
                        {
                            if(exp.getIdEstado()==ConstantesRegistroExp.ESTADO_FINALIZADO)
                            {
                                if(ConstantesRegistroExp.modoTrabajo.equalsIgnoreCase(DatosConfiguracion.MODO_TRABAJO_ACOPLADO))
                                {
                                    resultado = ConstantesRegistroExp.FRAME_DESTINO_GESTION_EXP_EXPORTAR;
                                }
                            }
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_MODIFICAR_INF_CATASTRAL_ACCION_SALIR:
                        {
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_GESTION_EXP;
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_GESTION_EXP_ACCION_MODIFICAR_INF_CATASTRAL:
                        {
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_MODIFICAR_INF_CATASTRAL;
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_GESTION_EXP_ACCION_MODIFICAR_DATOS_EXP:
                        {
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_MODIFICAR_DATOS_EXP;
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_MODIFICAR_EXP_ACCION_SALIR:
                        {
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_GESTION_EXP;
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_ACTUALIZAR_CATASTRO:
                        {
                        	exp.setIdEstado(estadoSiguiente(exp.getIdEstado()));
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_GESTION_EXP_ACTUALIZA_CATASTRO;
                            break;
                        }
                    }
                    break;
                }
                case ConstantesRegistroExp.ESTADO_GENERADO:
                {
                    switch(frameOrigenAccion)
                    {
                        case ConstantesRegistroExp.FRAME_ORIGEN_GESTION_EXP_VER_ESTADO:
                        {
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_VER_ESTADO;
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_BUSQUEDA_ACCION_ACEPTAR:
                        {
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_GESTION_EXP;
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_GESTION_EXP_ACCION_MODIFICAR_INF_CATASTRAL:
                        {
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_MODIFICAR_INF_CATASTRAL;
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_GESTION_EXP_ACCION_MODIFICAR_DATOS_EXP:
                        {
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_MODIFICAR_DATOS_EXP;
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_MODIFICAR_INF_CATASTRAL_ACCION_SALIR:
                        {
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_GESTION_EXP;
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_MODIFICAR_EXP_ACCION_SALIR:
                        {
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_GESTION_EXP;
                            break;
                        }
                    }
                    break;
                }
                case ConstantesRegistroExp.ESTADO_ENVIADO:
                {
                    switch(frameOrigenAccion)
                    {
                        case ConstantesRegistroExp.FRAME_ORIGEN_GESTION_EXP_VER_ESTADO:
                        {
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_VER_ESTADO;
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_BUSQUEDA_ACCION_ACEPTAR:
                        {
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_GESTION_EXP;
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_GESTION_EXP_ACCION_MODIFICAR_INF_CATASTRAL:
                        {
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_MODIFICAR_INF_CATASTRAL;
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_GESTION_EXP_ACCION_MODIFICAR_DATOS_EXP:
                        {
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_MODIFICAR_DATOS_EXP;
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_MODIFICAR_INF_CATASTRAL_ACCION_SALIR:
                        {
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_GESTION_EXP;
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_MODIFICAR_EXP_ACCION_SALIR:
                        {
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_GESTION_EXP;
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_GESTION_EXP_CONSULTA_ESTADO:
                        {
                        	exp.setIdEstado(estadoSiguiente(exp.getIdEstado()));
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_GESTION_EXP_CONSULTA_ESTADO_CATASTRO;
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_BUSQUEDA_CONSULTA_ESTADO:
                        {
                        	exp.setIdEstado(estadoSiguiente(exp.getIdEstado()));
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_BUSQUEDA_CONSULTA_ESTADO;
                            break;
                        }
                        
                    }
                    break;
                }
                case ConstantesRegistroExp.ESTADO_CERRADO:
                {
                    switch(frameOrigenAccion)
                    {
                        case ConstantesRegistroExp.FRAME_ORIGEN_GESTION_EXP_VER_ESTADO:
                        {
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_VER_ESTADO;
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_BUSQUEDA_ACCION_ACEPTAR:
                        {
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_GESTION_EXP;
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_GESTION_EXP_ACCION_MODIFICAR_INF_CATASTRAL:
                        {
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_MODIFICAR_INF_CATASTRAL;
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_GESTION_EXP_ACCION_MODIFICAR_DATOS_EXP:
                        {
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_MODIFICAR_DATOS_EXP;
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_MODIFICAR_INF_CATASTRAL_ACCION_SALIR:
                        {
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_GESTION_EXP;
                            break;
                        }
                        case ConstantesRegistroExp.FRAME_ORIGEN_MODIFICAR_EXP_ACCION_SALIR:
                        {
                            resultado = ConstantesRegistroExp.FRAME_DESTINO_GESTION_EXP;
                            break;
                        }
                    }
                    break;
                }
            }
            actualizaBotones((int)exp.getIdEstado());            
        }
        else
        {
            switch(frameOrigenAccion)
            {
                case ConstantesRegistroExp.FRAME_ORIGEN_BUSQUEDA_ACCION_NUEVO:
                {
                    resultado= ConstantesRegistroExp.FRAME_DESTINO_CREAR_EXP;
                    break;
                }
                case ConstantesRegistroExp.FRAME_ORIGEN_GESTION_EXP_ACCION_CERRAR:
                {
                    resultado= ConstantesRegistroExp.FRAME_DESTINO_BUSCAR_EXPEDIENTES;
                    break;
                }
            }
        }
        return resultado;
    }

    /**
     * Actualiza la variable botonesGestExp en ConstantesRegistroExp segun el estado del expediente. Esta variable
     * se usa para habilitar los botones en la pantalla gestion de expediente.
     */
    public void actualizaBotones(int estado)
    {
        switch(estado)
        {
            case ConstantesRegistroExp.ESTADO_REGISTRADO:
            {
                if(ConstantesRegistroExp.modoTrabajo.equalsIgnoreCase(DatosConfiguracion.MODO_TRABAJO_ACOPLADO))
                {
                    ConstantesRegistroExp.botonesGestExp[0] = true;
                    ConstantesRegistroExp.botonesGestExp[1] = false;
                    ConstantesRegistroExp.botonesGestExp[2] = false;
                    ConstantesRegistroExp.botonesGestExp[3] = false;
                    ConstantesRegistroExp.botonesGestExp[4] = false;
                    ConstantesRegistroExp.botonesGestExp[5] = true;
                    ConstantesRegistroExp.botonesGestExp[6] = true;
                    ConstantesRegistroExp.botonesGestExp[7] = false;
                    ConstantesRegistroExp.botonesGestExp[8] = false;
                }
                else
                {
                    ConstantesRegistroExp.botonesGestExp[0] = true;
                    ConstantesRegistroExp.botonesGestExp[1] = false;
                    ConstantesRegistroExp.botonesGestExp[2] = false;
                    ConstantesRegistroExp.botonesGestExp[3] = false;
                    ConstantesRegistroExp.botonesGestExp[4] = true;
                    ConstantesRegistroExp.botonesGestExp[5] = true;
                    ConstantesRegistroExp.botonesGestExp[6] = false;
                    ConstantesRegistroExp.botonesGestExp[7] = false;
                    ConstantesRegistroExp.botonesGestExp[8] = false;
                }
                break;
            }
            case ConstantesRegistroExp.ESTADO_ASOCIADO:
            {
                if(ConstantesRegistroExp.modoTrabajo.equalsIgnoreCase(DatosConfiguracion.MODO_TRABAJO_ACOPLADO))
                {
                    ConstantesRegistroExp.botonesGestExp[0] = true;
                    ConstantesRegistroExp.botonesGestExp[1] = false;
                    ConstantesRegistroExp.botonesGestExp[2] = false;
                    ConstantesRegistroExp.botonesGestExp[3] = false;
                    ConstantesRegistroExp.botonesGestExp[4] = true;
                    ConstantesRegistroExp.botonesGestExp[5] = true;
                    ConstantesRegistroExp.botonesGestExp[6] = false;
                    ConstantesRegistroExp.botonesGestExp[7] = false;
                    ConstantesRegistroExp.botonesGestExp[8] = false;
                }
                break;
            }
            case ConstantesRegistroExp.ESTADO_RELLENADO:
            {
                if(ConstantesRegistroExp.modoTrabajo.equalsIgnoreCase(DatosConfiguracion.MODO_TRABAJO_ACOPLADO))
                {
                    ConstantesRegistroExp.botonesGestExp[0] = true;
                    ConstantesRegistroExp.botonesGestExp[1] = false;
                    ConstantesRegistroExp.botonesGestExp[2] = false;
                    ConstantesRegistroExp.botonesGestExp[3] = false;
                    ConstantesRegistroExp.botonesGestExp[4] = true;
                    ConstantesRegistroExp.botonesGestExp[5] = true;
                    ConstantesRegistroExp.botonesGestExp[6] = true;
                    ConstantesRegistroExp.botonesGestExp[7] = false;
                    ConstantesRegistroExp.botonesGestExp[8] = false;
                }
                else
                {
                    ConstantesRegistroExp.botonesGestExp[0] = true;
                    ConstantesRegistroExp.botonesGestExp[1] = true;
                    ConstantesRegistroExp.botonesGestExp[2] = false;
                    ConstantesRegistroExp.botonesGestExp[3] = true;
                    ConstantesRegistroExp.botonesGestExp[4] = true;
                    ConstantesRegistroExp.botonesGestExp[5] = true;
                    ConstantesRegistroExp.botonesGestExp[6] = false;
                    ConstantesRegistroExp.botonesGestExp[7] = false;
                    ConstantesRegistroExp.botonesGestExp[8] = false;
                }
                break;
            }
            case ConstantesRegistroExp.ESTADO_SINCRONIZADO:
            {
                if(ConstantesRegistroExp.modoTrabajo.equalsIgnoreCase(DatosConfiguracion.MODO_TRABAJO_ACOPLADO))
                {
                    ConstantesRegistroExp.botonesGestExp[0] = true;
                    ConstantesRegistroExp.botonesGestExp[1] = true;
                    ConstantesRegistroExp.botonesGestExp[2] = false;
                    ConstantesRegistroExp.botonesGestExp[3] = true;
                    ConstantesRegistroExp.botonesGestExp[4] = true;
                    ConstantesRegistroExp.botonesGestExp[5] = true;
                    ConstantesRegistroExp.botonesGestExp[6] = false;
                    ConstantesRegistroExp.botonesGestExp[7] = false;
                    ConstantesRegistroExp.botonesGestExp[8] = false;
                }
                break;
            }
            case ConstantesRegistroExp.ESTADO_MODIFICADO:
            {
                if(ConstantesRegistroExp.modoTrabajo.equalsIgnoreCase(DatosConfiguracion.MODO_TRABAJO_ACOPLADO))
                {
                    ConstantesRegistroExp.botonesGestExp[0] = true;
                    ConstantesRegistroExp.botonesGestExp[1] = true;
                    ConstantesRegistroExp.botonesGestExp[2] = false;
                    ConstantesRegistroExp.botonesGestExp[3] = true;
                    ConstantesRegistroExp.botonesGestExp[4] = true;
                    ConstantesRegistroExp.botonesGestExp[5] = true;
                    ConstantesRegistroExp.botonesGestExp[6] = false;
                    ConstantesRegistroExp.botonesGestExp[7] = false;
                    ConstantesRegistroExp.botonesGestExp[8] = false;
                }
                else
                {
                    ConstantesRegistroExp.botonesGestExp[0] = true;
                    ConstantesRegistroExp.botonesGestExp[1] = true;
                    ConstantesRegistroExp.botonesGestExp[2] = false;
                    ConstantesRegistroExp.botonesGestExp[3] = true;
                    ConstantesRegistroExp.botonesGestExp[4] = true;
                    ConstantesRegistroExp.botonesGestExp[5] = true;
                    ConstantesRegistroExp.botonesGestExp[6] = false;
                    ConstantesRegistroExp.botonesGestExp[7] = false;
                    ConstantesRegistroExp.botonesGestExp[7] = false;
                    ConstantesRegistroExp.botonesGestExp[8] = false;
                }
                break;
            }
            case ConstantesRegistroExp.ESTADO_FINALIZADO:
            {
                if(ConstantesRegistroExp.modoTrabajo.equalsIgnoreCase(DatosConfiguracion.MODO_TRABAJO_ACOPLADO))
                {
                    ConstantesRegistroExp.botonesGestExp[0] = true;
                    ConstantesRegistroExp.botonesGestExp[1] = true;
                    ConstantesRegistroExp.botonesGestExp[2] = true;
                    ConstantesRegistroExp.botonesGestExp[3] = false;
                    ConstantesRegistroExp.botonesGestExp[4] = false;
                    ConstantesRegistroExp.botonesGestExp[5] = true;
                    ConstantesRegistroExp.botonesGestExp[6] = false;
                    ConstantesRegistroExp.botonesGestExp[7] = true;
                    ConstantesRegistroExp.botonesGestExp[8] = false;
                    
                }
                else
                {
                    ConstantesRegistroExp.botonesGestExp[0] = true;
                    ConstantesRegistroExp.botonesGestExp[1] = true;
                    ConstantesRegistroExp.botonesGestExp[2] = false;
                    ConstantesRegistroExp.botonesGestExp[3] = false;
                    ConstantesRegistroExp.botonesGestExp[4] = false;
                    ConstantesRegistroExp.botonesGestExp[5] = true;
                    ConstantesRegistroExp.botonesGestExp[6] = false;
                    ConstantesRegistroExp.botonesGestExp[7] = false;
                    ConstantesRegistroExp.botonesGestExp[8] = false;
                }
                break;
            }
            case ConstantesRegistroExp.ESTADO_GENERADO:
            {
                if(ConstantesRegistroExp.modoTrabajo.equalsIgnoreCase(DatosConfiguracion.MODO_TRABAJO_ACOPLADO))
                {
                    ConstantesRegistroExp.botonesGestExp[0] = true;
                    ConstantesRegistroExp.botonesGestExp[1] = true;
                    ConstantesRegistroExp.botonesGestExp[2] = false;
                    ConstantesRegistroExp.botonesGestExp[3] = false;
                    ConstantesRegistroExp.botonesGestExp[4] = false;
                    ConstantesRegistroExp.botonesGestExp[5] = true;
                    ConstantesRegistroExp.botonesGestExp[6] = false;
                    ConstantesRegistroExp.botonesGestExp[7] = false;
                    ConstantesRegistroExp.botonesGestExp[8] = true;
                }
                else
                {
                    ConstantesRegistroExp.botonesGestExp[0] = true;
                    ConstantesRegistroExp.botonesGestExp[1] = true;
                    ConstantesRegistroExp.botonesGestExp[2] = false;
                    ConstantesRegistroExp.botonesGestExp[3] = false;
                    ConstantesRegistroExp.botonesGestExp[4] = false;
                    ConstantesRegistroExp.botonesGestExp[5] = true;
                    ConstantesRegistroExp.botonesGestExp[6] = false;
                    ConstantesRegistroExp.botonesGestExp[7] = false;
                    ConstantesRegistroExp.botonesGestExp[7] = false;
                    ConstantesRegistroExp.botonesGestExp[8] = false;
                }
                break;
            }
            case ConstantesRegistroExp.ESTADO_ENVIADO:
            {
                if(ConstantesRegistroExp.modoTrabajo.equalsIgnoreCase(DatosConfiguracion.MODO_TRABAJO_ACOPLADO))
                {
                    ConstantesRegistroExp.botonesGestExp[0] = true;
                    ConstantesRegistroExp.botonesGestExp[1] = true;
                    ConstantesRegistroExp.botonesGestExp[2] = false;
                    ConstantesRegistroExp.botonesGestExp[3] = false;
                    ConstantesRegistroExp.botonesGestExp[4] = false;
                    ConstantesRegistroExp.botonesGestExp[5] = true;
                    ConstantesRegistroExp.botonesGestExp[6] = false;
                    ConstantesRegistroExp.botonesGestExp[7] = false;
                    ConstantesRegistroExp.botonesGestExp[8] = true;
                }
                else
                {
                    ConstantesRegistroExp.botonesGestExp[0] = true;
                    ConstantesRegistroExp.botonesGestExp[1] = true;
                    ConstantesRegistroExp.botonesGestExp[2] = false;
                    ConstantesRegistroExp.botonesGestExp[3] = false;
                    ConstantesRegistroExp.botonesGestExp[4] = false;
                    ConstantesRegistroExp.botonesGestExp[5] = true;
                    ConstantesRegistroExp.botonesGestExp[6] = false;
                    ConstantesRegistroExp.botonesGestExp[7] = false;
                    ConstantesRegistroExp.botonesGestExp[8] = false;
                }
                break;
            }
            case ConstantesRegistroExp.ESTADO_CERRADO:
            {
                if(ConstantesRegistroExp.modoTrabajo.equalsIgnoreCase(DatosConfiguracion.MODO_TRABAJO_ACOPLADO))
                {
                    ConstantesRegistroExp.botonesGestExp[0] = true;
                    ConstantesRegistroExp.botonesGestExp[1] = true;
                    ConstantesRegistroExp.botonesGestExp[2] = false;
                    ConstantesRegistroExp.botonesGestExp[3] = false;
                    ConstantesRegistroExp.botonesGestExp[4] = false;
                    ConstantesRegistroExp.botonesGestExp[5] = true;
                    ConstantesRegistroExp.botonesGestExp[6] = false;
                    ConstantesRegistroExp.botonesGestExp[7] = false;
                    ConstantesRegistroExp.botonesGestExp[8] = false;
                }
                else
                {
                    ConstantesRegistroExp.botonesGestExp[0] = true;
                    ConstantesRegistroExp.botonesGestExp[1] = true;
                    ConstantesRegistroExp.botonesGestExp[2] = false;
                    ConstantesRegistroExp.botonesGestExp[3] = false;
                    ConstantesRegistroExp.botonesGestExp[4] = false;
                    ConstantesRegistroExp.botonesGestExp[5] = true;
                    ConstantesRegistroExp.botonesGestExp[6] = false;
                    ConstantesRegistroExp.botonesGestExp[7] = false;
                    ConstantesRegistroExp.botonesGestExp[8] = false;
                }
                break;
            }
        }
    }
}
