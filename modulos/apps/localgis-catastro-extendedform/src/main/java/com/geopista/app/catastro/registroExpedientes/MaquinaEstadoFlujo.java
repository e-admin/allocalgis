/**
 * MaquinaEstadoFlujo.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.registroExpedientes;

import java.util.ArrayList;

import com.geopista.app.catastro.model.beans.ConstantesRegExp;
import com.geopista.app.catastro.model.beans.DatosConfiguracion;
import com.geopista.app.catastro.model.beans.EstadoSiguiente;
import com.geopista.app.catastro.model.beans.Expediente;
import com.geopista.app.catastro.utils.ConstantesCatastro;

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
            codigoEstados = ConstantesRegExp.clienteCatastro.getCodigoEstados();
            estadoSiguiente = ConstantesRegExp.clienteCatastro.getEstadoSiguiente();
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
                    ConstantesCatastro.modoTrabajo.equalsIgnoreCase(((EstadoSiguiente)estadoSiguiente.get(i)).getModo()))
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
                    ConstantesCatastro.modoTrabajo.equalsIgnoreCase(((EstadoSiguiente)estadoSiguiente.get(i)).getModo()))
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
        int resultado = ConstantesCatastro.FRAME_ACCION_DEFAULT;
        if(exp!=null)
        {
            
            int idEstadoExp = (int)exp.getIdEstado();
            switch(idEstadoExp)
            {
                case ConstantesCatastro.ESTADO_REGISTRADO:
                {
                    switch(frameOrigenAccion)
                    {
                        case ConstantesCatastro.FRAME_ORIGEN_CREAR_EXP_ACCION_GUARDAR:
                        {
                           /* if(ConstantesCatastro.modoTrabajo.equalsIgnoreCase(DatosConfiguracion.MODO_TRABAJO_ACOPLADO))
                            {
                                exp.setIdEstado(estadoSiguiente(exp.getIdEstado()));
                            }*/
                            resultado = ConstantesCatastro.FRAME_DESTINO_GESTION_EXP_CREA_EXP;
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_CREAR_EXP_ACCION_SIGUIENTE:
                        {
                            /*if(ConstantesCatastro.modoTrabajo.equalsIgnoreCase(DatosConfiguracion.MODO_TRABAJO_ACOPLADO))
                            {
                                exp.setIdEstado(estadoSiguiente(exp.getIdEstado()));
                            }*/

                            if(ConstantesCatastro.tipoConvenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO))
                            {
                                if(exp.getTipoExpediente().getConvenio().equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO))
                                {
                                    resultado = ConstantesCatastro.FRAME_DESTINO_ASOCIAR_PARCELAS_CREAR_EXP;
                                }
                                else if(exp.getTipoExpediente().getConvenio().equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD))
                                {
                                    resultado = ConstantesCatastro.FRAME_DESTINO_ASOCIAR_BIEN_INMUEBLE_CREAR_EXP;
                                }
                            }
                            else if(ConstantesCatastro.tipoConvenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD))
                            {
                                resultado = ConstantesCatastro.FRAME_DESTINO_ASOCIAR_BIEN_INMUEBLE_CREAR_EXP;                                
                            }

                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_ASOCIAR_BIEN_INMUEBLE_ACCION_GUARDAR:
                        {
                            if(exp.getListaReferencias()!=null && exp.getListaReferencias().size()>0)
                            {
                                exp.setIdEstado(estadoSiguiente(exp.getIdEstado()));
                                resultado = ConstantesCatastro.FRAME_DESTINO_GESTION_EXP_UPDATE_EXP;
                            }
                            else
                            {
                                resultado = ConstantesCatastro.FRAME_DESTINO_GESTION_EXP;
                            }
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_ASOCIAR_PARCELA_ACCION_GUARDAR:
                        {
                            if(exp.getListaReferencias()!=null && exp.getListaReferencias().size()>0)
                            {
                                exp.setIdEstado(estadoSiguiente(exp.getIdEstado()));
                                resultado = ConstantesCatastro.FRAME_DESTINO_GESTION_EXP_UPDATE_EXP;
                            }
                            else
                            {
                                resultado = ConstantesCatastro.FRAME_DESTINO_GESTION_EXP;
                            }
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_GESTION_EXP_VER_ESTADO:
                        {
                            resultado = ConstantesCatastro.FRAME_DESTINO_VER_ESTADO;
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_GESTION_EXP_ASOCIAR_PARCELA_BIEN_INMUEBLE:
                        {

                            if(ConstantesCatastro.tipoConvenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO))
                            {
                                if(exp.getTipoExpediente().getConvenio().equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO))
                                {
                                    resultado = ConstantesCatastro.FRAME_DESTINO_ASOCIAR_PARCELAS;
                                }
                                else if(exp.getTipoExpediente().getConvenio().equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD))
                                {
                                    resultado = ConstantesCatastro.FRAME_DESTINO_ASOCIAR_BIEN_INMUEBLE;
                                }
                            }
                            else if(ConstantesCatastro.tipoConvenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD))
                            {
                                resultado = ConstantesCatastro.FRAME_DESTINO_ASOCIAR_BIEN_INMUEBLE;
                            }
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_BUSQUEDA_ACCION_ACEPTAR:
                        {
                            resultado = ConstantesCatastro.FRAME_DESTINO_GESTION_EXP;
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_ASOCIAR_PARCELA_ACCION_SALIR:
                        {
                            resultado = ConstantesCatastro.FRAME_DESTINO_GESTION_EXP;
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_ASOCIAR_BIEN_INMUEBLE_ACCION_SALIR:
                        {
                            resultado = ConstantesCatastro.FRAME_DESTINO_GESTION_EXP;
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_MODIFICAR_EXP_ACCION_GUARDAR:
                        {
                            resultado = ConstantesCatastro.FRAME_DESTINO_GESTION_EXP_UPDATE_EXP;
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_MODIFICAR_EXP_ACCION_SALIR:
                        {
                            resultado = ConstantesCatastro.FRAME_DESTINO_GESTION_EXP;
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_GESTION_EXP_ACCION_MODIFICAR_DATOS_EXP:
                        {
                            resultado = ConstantesCatastro.FRAME_DESTINO_MODIFICAR_DATOS_EXP;
                            break;
                        }     
                        case ConstantesCatastro.FRAME_ORIGEN_ASOCIADO_EXP:
                        {
                        	exp.setIdEstado(estadoSiguiente(exp.getIdEstado()));
                        	resultado = ConstantesCatastro.FRAME_DESTINO_GESTION_EXP_UPDATE_EXP;
                            break;
                        } 
                        case ConstantesCatastro.FRAME_ORIGEN_NO_ASOCIADO_EXP:
                        {
                        	resultado = ConstantesCatastro.FRAME_DESTINO_GESTION_EXP;
                            break;
                        } 
                    }
                    break;
                }
                case ConstantesCatastro.ESTADO_ASOCIADO:
                {
                    switch(frameOrigenAccion)
                    {
                        case ConstantesCatastro.FRAME_ORIGEN_ASOCIAR_PARCELA_ACCION_GUARDAR:
                        {
                            if(exp.getListaReferencias()!=null && exp.getListaReferencias().size()>0)
                            {
                                exp.setIdEstado(estadoSiguiente(exp.getIdEstado()));
                                resultado = ConstantesCatastro.FRAME_DESTINO_GESTION_EXP_UPDATE_EXP;
                            }
                            else
                            {
                                resultado = ConstantesCatastro.FRAME_DESTINO_GESTION_EXP;
                            }
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_ASOCIAR_BIEN_INMUEBLE_ACCION_GUARDAR:
                        {
                            if(exp.getListaReferencias()!=null && exp.getListaReferencias().size()>0)
                            {
                                exp.setIdEstado(estadoSiguiente(exp.getIdEstado()));
                                resultado = ConstantesCatastro.FRAME_DESTINO_GESTION_EXP_UPDATE_EXP;
                            }
                            else
                            {
                                resultado = ConstantesCatastro.FRAME_DESTINO_GESTION_EXP;
                            }
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_GESTION_EXP_ASOCIAR_PARCELA_BIEN_INMUEBLE:
                        {
                            if(ConstantesCatastro.tipoConvenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO))
                            {
                                if(exp.getTipoExpediente().getConvenio().equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO))
                                {
                                    resultado = ConstantesCatastro.FRAME_DESTINO_ASOCIAR_PARCELAS;
                                }
                                else if(exp.getTipoExpediente().getConvenio().equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD))
                                {
                                    resultado = ConstantesCatastro.FRAME_DESTINO_ASOCIAR_BIEN_INMUEBLE;
                                }
                            }
                            else if(ConstantesCatastro.tipoConvenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD))
                            {
                                resultado = ConstantesCatastro.FRAME_DESTINO_ASOCIAR_BIEN_INMUEBLE;
                            }
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_GESTION_EXP_VER_ESTADO:
                        {
                            resultado = ConstantesCatastro.FRAME_DESTINO_VER_ESTADO;
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_BUSQUEDA_ACCION_ACEPTAR:
                        {
                            resultado = ConstantesCatastro.FRAME_DESTINO_GESTION_EXP;
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_ASOCIAR_PARCELA_ACCION_SALIR:
                        {
                            resultado = ConstantesCatastro.FRAME_DESTINO_GESTION_EXP;
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_ASOCIAR_BIEN_INMUEBLE_ACCION_SALIR:
                        {
                            resultado = ConstantesCatastro.FRAME_DESTINO_GESTION_EXP;
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_MODIFICAR_EXP_ACCION_GUARDAR:
                        {
                            resultado = ConstantesCatastro.FRAME_DESTINO_GESTION_EXP_UPDATE_EXP;
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_MODIFICAR_EXP_ACCION_SALIR:
                        {
                            resultado = ConstantesCatastro.FRAME_DESTINO_GESTION_EXP;
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_GESTION_EXP_ACCION_MODIFICAR_DATOS_EXP:
                        {
                            resultado = ConstantesCatastro.FRAME_DESTINO_MODIFICAR_DATOS_EXP;
                            break;
                        }
                    }
                    break;
                }
                case ConstantesCatastro.ESTADO_RELLENADO:
                {
                    switch(frameOrigenAccion)
                    {
                        case ConstantesCatastro.FRAME_ORIGEN_GESTION_EXP_VER_ESTADO:
                        {
                            resultado = ConstantesCatastro.FRAME_DESTINO_VER_ESTADO;
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_GESTION_EXP_ASOCIAR_PARCELA_BIEN_INMUEBLE:
                        {
                            if(ConstantesCatastro.tipoConvenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO))
                            {
                                if(exp.getTipoExpediente().getConvenio().equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO))
                                {
                                    resultado = ConstantesCatastro.FRAME_DESTINO_ASOCIAR_PARCELAS;
                                }
                                else if(exp.getTipoExpediente().getConvenio().equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD))
                                {
                                    resultado = ConstantesCatastro.FRAME_DESTINO_ASOCIAR_BIEN_INMUEBLE;
                                }                                 
                            }
                            else if(ConstantesCatastro.tipoConvenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD))
                            {
                                resultado = ConstantesCatastro.FRAME_DESTINO_ASOCIAR_BIEN_INMUEBLE;
                            }
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_BUSQUEDA_ACCION_ACEPTAR:
                        {

                            resultado = ConstantesCatastro.FRAME_DESTINO_GESTION_EXP;
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_ASOCIAR_PARCELA_ACCION_GUARDAR:
                        {
                            resultado = ConstantesCatastro.FRAME_DESTINO_GESTION_EXP_UPDATE_EXP;
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_ASOCIAR_BIEN_INMUEBLE_ACCION_GUARDAR:
                        {
                            resultado = ConstantesCatastro.FRAME_DESTINO_GESTION_EXP_UPDATE_EXP;
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_MODIFICAR_EXP_ACCION_GUARDAR:
                        {
                            resultado = ConstantesCatastro.FRAME_DESTINO_GESTION_EXP_UPDATE_EXP;
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_MODIFICAR_EXP_ACCION_SALIR:
                        {
                            resultado = ConstantesCatastro.FRAME_DESTINO_GESTION_EXP;
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_GESTION_EXP_ACCION_MODIFICAR_DATOS_EXP:
                        {
                            resultado = ConstantesCatastro.FRAME_DESTINO_MODIFICAR_DATOS_EXP;
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_GESTION_EXP_ACCION_MODIFICAR_INF_CATASTRAL:
                        {
                            if(ConstantesCatastro.modoTrabajo.equalsIgnoreCase(DatosConfiguracion.MODO_TRABAJO_ACOPLADO))
                            {
                                resultado = ConstantesCatastro.FRAME_DESTINO_MODIFICAR_INF_CATASTRAL_SINCRONIZAR;
                            }
                            else
                            {
                                resultado = ConstantesCatastro.FRAME_DESTINO_MODIFICAR_INF_CATASTRAL_ESCRIBIR_CATASTRO_TEMPORAL;                                
                            }
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_GESTION_EXP_ACCION_ASOCIAR_DATOS_GRAFICOS:
                        {
                            if(ConstantesCatastro.modoTrabajo.equalsIgnoreCase(DatosConfiguracion.MODO_TRABAJO_ACOPLADO))
                            {
                                resultado = ConstantesCatastro.FRAME_DESTINO_ASOCIAR_DATOS_GRAFICOS_SINCRONIZAR;
                            }
                            else
                            {
                                resultado = ConstantesCatastro.FRAME_DESTINO_ASOCIAR_DATOS_GRAFICOS_ESCRIBIR_CATASTRO_TEMPORAL;
                            }
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_ASOCIAR_PARCELA_ACCION_SALIR:
                        {
                            resultado = ConstantesCatastro.FRAME_DESTINO_GESTION_EXP;
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_ASOCIAR_BIEN_INMUEBLE_ACCION_SALIR:
                        {
                            resultado = ConstantesCatastro.FRAME_DESTINO_GESTION_EXP;
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_OBTENER_INF_CATASTRO:
                        {
                        	 exp.setIdEstado(estadoSiguiente(exp.getIdEstado()));
                            resultado = ConstantesCatastro.FRAME_DESTINO_GESTION_EXP_CONSULTA_CATASTRO;
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_ACTUALIZAR_CATASTRO:
                        {
                            resultado = ConstantesCatastro.FRAME_DESTINO_GESTION_EXP;
                            break;
                        }
                    }
                    break;
                }
                case ConstantesCatastro.ESTADO_SINCRONIZADO:
                {
                    switch(frameOrigenAccion)
                    {
                        case ConstantesCatastro.FRAME_ORIGEN_GESTION_EXP_VER_ESTADO:
                        {
                            resultado = ConstantesCatastro.FRAME_DESTINO_VER_ESTADO;
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_BUSQUEDA_ACCION_ACEPTAR:
                        {
                            resultado = ConstantesCatastro.FRAME_DESTINO_GESTION_EXP;
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_GESTION_EXP_ASOCIAR_PARCELA_BIEN_INMUEBLE:
                        {
                            if(ConstantesCatastro.tipoConvenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO))
                            {
                                if(exp.getTipoExpediente().getConvenio().equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO))
                                {
                                    resultado = ConstantesCatastro.FRAME_DESTINO_ASOCIAR_PARCELAS;
                                }
                                else if(exp.getTipoExpediente().getConvenio().equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD))
                                {
                                    resultado = ConstantesCatastro.FRAME_DESTINO_ASOCIAR_BIEN_INMUEBLE;
                                }
                            }
                            else if(ConstantesCatastro.tipoConvenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD))
                            {
                                resultado = ConstantesCatastro.FRAME_DESTINO_ASOCIAR_BIEN_INMUEBLE;
                            }
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_ASOCIAR_PARCELA_ACCION_GUARDAR:
                        {
                            if(ConstantesCatastro.modoTrabajo.equalsIgnoreCase(DatosConfiguracion.MODO_TRABAJO_ACOPLADO))
                            {
                                exp.setIdEstado(estadoAnterior(exp.getIdEstado()));
                            }
                            resultado = ConstantesCatastro.FRAME_DESTINO_GESTION_EXP_UPDATE_EXP_ELIMINA_CATASTRO_TEMPORAL;
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_ASOCIAR_BIEN_INMUEBLE_ACCION_GUARDAR:
                        {
                            if(ConstantesCatastro.modoTrabajo.equalsIgnoreCase(DatosConfiguracion.MODO_TRABAJO_ACOPLADO))
                            {
                                exp.setIdEstado(estadoAnterior(exp.getIdEstado()));
                            }
                            resultado = ConstantesCatastro.FRAME_DESTINO_GESTION_EXP_UPDATE_EXP_ELIMINA_CATASTRO_TEMPORAL;
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_MODIFICAR_EXP_ACCION_GUARDAR:
                        {
                            resultado = ConstantesCatastro.FRAME_DESTINO_GESTION_EXP_UPDATE_EXP;
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_MODIFICAR_EXP_ACCION_SALIR:
                        {
                            resultado = ConstantesCatastro.FRAME_DESTINO_GESTION_EXP;
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_GESTION_EXP_ACCION_MODIFICAR_DATOS_EXP:
                        {
                            resultado = ConstantesCatastro.FRAME_DESTINO_MODIFICAR_DATOS_EXP;
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_GESTION_EXP_ACCION_MODIFICAR_INF_CATASTRAL:
                        {
                            resultado = ConstantesCatastro.FRAME_DESTINO_MODIFICAR_INF_CATASTRAL_ESCRIBIR_CATASTRO_TEMPORAL;
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_GESTION_EXP_ACCION_ASOCIAR_DATOS_GRAFICOS:
                        {
                            resultado = ConstantesCatastro.FRAME_DESTINO_ASOCIAR_DATOS_GRAFICOS_ESCRIBIR_CATASTRO_TEMPORAL;
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_ASOCIAR_PARCELA_ACCION_SALIR:
                        {
                            resultado = ConstantesCatastro.FRAME_DESTINO_GESTION_EXP;
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_ASOCIAR_BIEN_INMUEBLE_ACCION_SALIR:
                        {
                            resultado = ConstantesCatastro.FRAME_DESTINO_GESTION_EXP;
                            break;
                        }
                    }
                    break;
                }
                case ConstantesCatastro.ESTADO_MODIFICADO:
                {
                    switch(frameOrigenAccion)
                    {
                        case ConstantesCatastro.FRAME_ORIGEN_GESTION_EXP_VER_ESTADO:
                        {
                            resultado = ConstantesCatastro.FRAME_DESTINO_VER_ESTADO;
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_BUSQUEDA_ACCION_ACEPTAR:
                        {
                            resultado = ConstantesCatastro.FRAME_DESTINO_GESTION_EXP;
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_GESTION_EXP_ASOCIAR_PARCELA_BIEN_INMUEBLE:
                        {
                            if(ConstantesCatastro.tipoConvenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO))
                            {
                                if(exp.getTipoExpediente().getConvenio().equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO))
                                {
                                    resultado = ConstantesCatastro.FRAME_DESTINO_ASOCIAR_PARCELAS;
                                }
                                else if(exp.getTipoExpediente().getConvenio().equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD))
                                {
                                    resultado = ConstantesCatastro.FRAME_DESTINO_ASOCIAR_BIEN_INMUEBLE;
                                }
                            }
                            else if(ConstantesCatastro.tipoConvenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD))
                            {
                                resultado = ConstantesCatastro.FRAME_DESTINO_ASOCIAR_BIEN_INMUEBLE;
                            }
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_ASOCIAR_PARCELA_ACCION_GUARDAR:
                        {
                            exp.setIdEstado(estadoAnterior(exp.getIdEstado()));
                            resultado = ConstantesCatastro.FRAME_DESTINO_GESTION_EXP_UPDATE_EXP_ELIMINA_CATASTRO_TEMPORAL;
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_ASOCIAR_BIEN_INMUEBLE_ACCION_GUARDAR:
                        {
                            exp.setIdEstado(estadoAnterior(exp.getIdEstado()));                            
                            resultado = ConstantesCatastro.FRAME_DESTINO_GESTION_EXP_UPDATE_EXP_ELIMINA_CATASTRO_TEMPORAL;
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_MODIFICAR_EXP_ACCION_GUARDAR:
                        {
                            resultado = ConstantesCatastro.FRAME_DESTINO_GESTION_EXP_UPDATE_EXP;
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_MODIFICAR_EXP_ACCION_SALIR:
                        {
                            resultado = ConstantesCatastro.FRAME_DESTINO_GESTION_EXP;
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_GESTION_EXP_ACCION_MODIFICAR_DATOS_EXP:
                        {
                            resultado = ConstantesCatastro.FRAME_DESTINO_MODIFICAR_DATOS_EXP;
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_GESTION_EXP_ACCION_MODIFICAR_INF_CATASTRAL:
                        {
                            resultado = ConstantesCatastro.FRAME_DESTINO_MODIFICAR_INF_CATASTRAL;
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_GESTION_EXP_ACCION_ASOCIAR_DATOS_GRAFICOS:
                        {
                            resultado = ConstantesCatastro.FRAME_DESTINO_ASOCIAR_DATOS_GRAFICOS;
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_MODIFICAR_INF_CATASTRAL_ACCION_SALIR:
                        {
                            resultado = ConstantesCatastro.FRAME_DESTINO_GESTION_EXP;
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_ASOCIAR_PARCELA_ACCION_SALIR:
                        {
                            resultado = ConstantesCatastro.FRAME_DESTINO_GESTION_EXP;
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_ASOCIAR_BIEN_INMUEBLE_ACCION_SALIR:
                        {
                            resultado = ConstantesCatastro.FRAME_DESTINO_GESTION_EXP;
                            break;
                        }
                       
                    }
                    break;
                }
                case ConstantesCatastro.ESTADO_FINALIZADO:
                {
                    switch(frameOrigenAccion)
                    {
                        case ConstantesCatastro.FRAME_ORIGEN_GESTION_EXP_VER_ESTADO:
                        {
                            resultado = ConstantesCatastro.FRAME_DESTINO_VER_ESTADO;
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_BUSQUEDA_ACCION_ACEPTAR:
                        {
                            resultado = ConstantesCatastro.FRAME_DESTINO_GESTION_EXP;
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_GESTION_EXP_ACCION_EXPORTAR_FICHERO:
                        {
                            if(exp.getIdEstado()==ConstantesCatastro.ESTADO_FINALIZADO)
                            {
                                if(ConstantesCatastro.modoTrabajo.equalsIgnoreCase(DatosConfiguracion.MODO_TRABAJO_ACOPLADO))
                                {
                                    resultado = ConstantesCatastro.FRAME_DESTINO_GESTION_EXP_EXPORTAR;
                                }
                            }
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_MODIFICAR_INF_CATASTRAL_ACCION_SALIR:
                        {
                            resultado = ConstantesCatastro.FRAME_DESTINO_GESTION_EXP;
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_GESTION_EXP_ACCION_MODIFICAR_INF_CATASTRAL:
                        {
                            resultado = ConstantesCatastro.FRAME_DESTINO_MODIFICAR_INF_CATASTRAL;
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_GESTION_EXP_ACCION_MODIFICAR_DATOS_EXP:
                        {
                            resultado = ConstantesCatastro.FRAME_DESTINO_MODIFICAR_DATOS_EXP;
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_MODIFICAR_EXP_ACCION_SALIR:
                        {
                            resultado = ConstantesCatastro.FRAME_DESTINO_GESTION_EXP;
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_ACTUALIZAR_CATASTRO:
                        {
                        	exp.setIdEstado(estadoSiguiente(exp.getIdEstado()));
                            resultado = ConstantesCatastro.FRAME_DESTINO_GESTION_EXP_ACTUALIZA_CATASTRO;
                            break;
                        }
                    }
                    break;
                }
                case ConstantesCatastro.ESTADO_GENERADO:
                {
                    switch(frameOrigenAccion)
                    {
                        case ConstantesCatastro.FRAME_ORIGEN_GESTION_EXP_VER_ESTADO:
                        {
                            resultado = ConstantesCatastro.FRAME_DESTINO_VER_ESTADO;
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_BUSQUEDA_ACCION_ACEPTAR:
                        {
                            resultado = ConstantesCatastro.FRAME_DESTINO_GESTION_EXP;
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_GESTION_EXP_ACCION_MODIFICAR_INF_CATASTRAL:
                        {
                            resultado = ConstantesCatastro.FRAME_DESTINO_MODIFICAR_INF_CATASTRAL;
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_GESTION_EXP_ACCION_MODIFICAR_DATOS_EXP:
                        {
                            resultado = ConstantesCatastro.FRAME_DESTINO_MODIFICAR_DATOS_EXP;
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_MODIFICAR_INF_CATASTRAL_ACCION_SALIR:
                        {
                            resultado = ConstantesCatastro.FRAME_DESTINO_GESTION_EXP;
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_MODIFICAR_EXP_ACCION_SALIR:
                        {
                            resultado = ConstantesCatastro.FRAME_DESTINO_GESTION_EXP;
                            break;
                        }
                    }
                    break;
                }
                case ConstantesCatastro.ESTADO_ENVIADO:
                {
                    switch(frameOrigenAccion)
                    {
                        case ConstantesCatastro.FRAME_ORIGEN_GESTION_EXP_VER_ESTADO:
                        {
                            resultado = ConstantesCatastro.FRAME_DESTINO_VER_ESTADO;
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_BUSQUEDA_ACCION_ACEPTAR:
                        {
                            resultado = ConstantesCatastro.FRAME_DESTINO_GESTION_EXP;
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_GESTION_EXP_ACCION_MODIFICAR_INF_CATASTRAL:
                        {
                            resultado = ConstantesCatastro.FRAME_DESTINO_MODIFICAR_INF_CATASTRAL;
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_GESTION_EXP_ACCION_MODIFICAR_DATOS_EXP:
                        {
                            resultado = ConstantesCatastro.FRAME_DESTINO_MODIFICAR_DATOS_EXP;
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_MODIFICAR_INF_CATASTRAL_ACCION_SALIR:
                        {
                            resultado = ConstantesCatastro.FRAME_DESTINO_GESTION_EXP;
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_MODIFICAR_EXP_ACCION_SALIR:
                        {
                            resultado = ConstantesCatastro.FRAME_DESTINO_GESTION_EXP;
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_GESTION_EXP_CONSULTA_ESTADO:
                        {
                        	exp.setIdEstado(estadoSiguiente(exp.getIdEstado()));
                            resultado = ConstantesCatastro.FRAME_DESTINO_GESTION_EXP_CONSULTA_ESTADO_CATASTRO;
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_BUSQUEDA_CONSULTA_ESTADO:
                        {
                        	exp.setIdEstado(estadoSiguiente(exp.getIdEstado()));
                            resultado = ConstantesCatastro.FRAME_DESTINO_BUSQUEDA_CONSULTA_ESTADO;
                            break;
                        }
                        
                    }
                    break;
                }
                case ConstantesCatastro.ESTADO_CERRADO:
                {
                    switch(frameOrigenAccion)
                    {
                        case ConstantesCatastro.FRAME_ORIGEN_GESTION_EXP_VER_ESTADO:
                        {
                            resultado = ConstantesCatastro.FRAME_DESTINO_VER_ESTADO;
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_BUSQUEDA_ACCION_ACEPTAR:
                        {
                            resultado = ConstantesCatastro.FRAME_DESTINO_GESTION_EXP;
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_GESTION_EXP_ACCION_MODIFICAR_INF_CATASTRAL:
                        {
                            resultado = ConstantesCatastro.FRAME_DESTINO_MODIFICAR_INF_CATASTRAL;
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_GESTION_EXP_ACCION_MODIFICAR_DATOS_EXP:
                        {
                            resultado = ConstantesCatastro.FRAME_DESTINO_MODIFICAR_DATOS_EXP;
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_MODIFICAR_INF_CATASTRAL_ACCION_SALIR:
                        {
                            resultado = ConstantesCatastro.FRAME_DESTINO_GESTION_EXP;
                            break;
                        }
                        case ConstantesCatastro.FRAME_ORIGEN_MODIFICAR_EXP_ACCION_SALIR:
                        {
                            resultado = ConstantesCatastro.FRAME_DESTINO_GESTION_EXP;
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
                case ConstantesCatastro.FRAME_ORIGEN_BUSQUEDA_ACCION_NUEVO:
                {
                    resultado= ConstantesCatastro.FRAME_DESTINO_CREAR_EXP;
                    break;
                }
                case ConstantesCatastro.FRAME_ORIGEN_GESTION_EXP_ACCION_CERRAR:
                {
                    resultado= ConstantesCatastro.FRAME_DESTINO_BUSCAR_EXPEDIENTES;
                    break;
                }
            }
        }
        return resultado;
    }

    /**
     * Actualiza la variable botonesGestExp en ConstantesCatastro segun el estado del expediente. Esta variable
     * se usa para habilitar los botones en la pantalla gestion de expediente.
     */
    public void actualizaBotones(int estado)
    {
        switch(estado)
        {
            case ConstantesCatastro.ESTADO_REGISTRADO:
            {
                if(ConstantesCatastro.modoTrabajo.equalsIgnoreCase(DatosConfiguracion.MODO_TRABAJO_ACOPLADO))
                {
                    ConstantesCatastro.botonesGestExp[0] = true;
                    ConstantesCatastro.botonesGestExp[1] = false;
                    ConstantesCatastro.botonesGestExp[2] = false;
                    ConstantesCatastro.botonesGestExp[3] = false;
                    ConstantesCatastro.botonesGestExp[4] = false;
                    ConstantesCatastro.botonesGestExp[5] = true;
                    ConstantesCatastro.botonesGestExp[6] = true;
                    ConstantesCatastro.botonesGestExp[7] = false;
                    ConstantesCatastro.botonesGestExp[8] = false;
                }
                else
                {
                    ConstantesCatastro.botonesGestExp[0] = true;
                    ConstantesCatastro.botonesGestExp[1] = false;
                    ConstantesCatastro.botonesGestExp[2] = false;
                    ConstantesCatastro.botonesGestExp[3] = false;
                    ConstantesCatastro.botonesGestExp[4] = true;
                    ConstantesCatastro.botonesGestExp[5] = true;
                    ConstantesCatastro.botonesGestExp[6] = false;
                    ConstantesCatastro.botonesGestExp[7] = false;
                    ConstantesCatastro.botonesGestExp[8] = false;
                }
                break;
            }
            case ConstantesCatastro.ESTADO_ASOCIADO:
            {
                if(ConstantesCatastro.modoTrabajo.equalsIgnoreCase(DatosConfiguracion.MODO_TRABAJO_ACOPLADO))
                {
                    ConstantesCatastro.botonesGestExp[0] = true;
                    ConstantesCatastro.botonesGestExp[1] = false;
                    ConstantesCatastro.botonesGestExp[2] = false;
                    ConstantesCatastro.botonesGestExp[3] = false;
                    ConstantesCatastro.botonesGestExp[4] = true;
                    ConstantesCatastro.botonesGestExp[5] = true;
                    ConstantesCatastro.botonesGestExp[6] = false;
                    ConstantesCatastro.botonesGestExp[7] = false;
                    ConstantesCatastro.botonesGestExp[8] = false;
                }
                break;
            }
            case ConstantesCatastro.ESTADO_RELLENADO:
            {
                if(ConstantesCatastro.modoTrabajo.equalsIgnoreCase(DatosConfiguracion.MODO_TRABAJO_ACOPLADO))
                {
                    ConstantesCatastro.botonesGestExp[0] = true;
                    ConstantesCatastro.botonesGestExp[1] = false;
                    ConstantesCatastro.botonesGestExp[2] = false;
                    ConstantesCatastro.botonesGestExp[3] = false;
                    ConstantesCatastro.botonesGestExp[4] = true;
                    ConstantesCatastro.botonesGestExp[5] = true;
                    ConstantesCatastro.botonesGestExp[6] = true;
                    ConstantesCatastro.botonesGestExp[7] = false;
                    ConstantesCatastro.botonesGestExp[8] = false;
                }
                else
                {
                    ConstantesCatastro.botonesGestExp[0] = true;
                    ConstantesCatastro.botonesGestExp[1] = true;
                    ConstantesCatastro.botonesGestExp[2] = false;
                    ConstantesCatastro.botonesGestExp[3] = true;
                    ConstantesCatastro.botonesGestExp[4] = true;
                    ConstantesCatastro.botonesGestExp[5] = true;
                    ConstantesCatastro.botonesGestExp[6] = false;
                    ConstantesCatastro.botonesGestExp[7] = false;
                    ConstantesCatastro.botonesGestExp[8] = false;
                }
                break;
            }
            case ConstantesCatastro.ESTADO_SINCRONIZADO:
            {
                if(ConstantesCatastro.modoTrabajo.equalsIgnoreCase(DatosConfiguracion.MODO_TRABAJO_ACOPLADO))
                {
                    ConstantesCatastro.botonesGestExp[0] = true;
                    ConstantesCatastro.botonesGestExp[1] = true;
                    ConstantesCatastro.botonesGestExp[2] = false;
                    ConstantesCatastro.botonesGestExp[3] = true;
                    ConstantesCatastro.botonesGestExp[4] = true;
                    ConstantesCatastro.botonesGestExp[5] = true;
                    ConstantesCatastro.botonesGestExp[6] = false;
                    ConstantesCatastro.botonesGestExp[7] = false;
                    ConstantesCatastro.botonesGestExp[8] = false;
                }
                break;
            }
            case ConstantesCatastro.ESTADO_MODIFICADO:
            {
                if(ConstantesCatastro.modoTrabajo.equalsIgnoreCase(DatosConfiguracion.MODO_TRABAJO_ACOPLADO))
                {
                    ConstantesCatastro.botonesGestExp[0] = true;
                    ConstantesCatastro.botonesGestExp[1] = true;
                    ConstantesCatastro.botonesGestExp[2] = false;
                    ConstantesCatastro.botonesGestExp[3] = true;
                    ConstantesCatastro.botonesGestExp[4] = true;
                    ConstantesCatastro.botonesGestExp[5] = true;
                    ConstantesCatastro.botonesGestExp[6] = false;
                    ConstantesCatastro.botonesGestExp[7] = false;
                    ConstantesCatastro.botonesGestExp[8] = false;
                }
                else
                {
                    ConstantesCatastro.botonesGestExp[0] = true;
                    ConstantesCatastro.botonesGestExp[1] = true;
                    ConstantesCatastro.botonesGestExp[2] = false;
                    ConstantesCatastro.botonesGestExp[3] = true;
                    ConstantesCatastro.botonesGestExp[4] = true;
                    ConstantesCatastro.botonesGestExp[5] = true;
                    ConstantesCatastro.botonesGestExp[6] = false;
                    ConstantesCatastro.botonesGestExp[7] = false;
                    ConstantesCatastro.botonesGestExp[7] = false;
                    ConstantesCatastro.botonesGestExp[8] = false;
                }
                break;
            }
            case ConstantesCatastro.ESTADO_FINALIZADO:
            {
                if(ConstantesCatastro.modoTrabajo.equalsIgnoreCase(DatosConfiguracion.MODO_TRABAJO_ACOPLADO))
                {
                    ConstantesCatastro.botonesGestExp[0] = true;
                    ConstantesCatastro.botonesGestExp[1] = true;
                    ConstantesCatastro.botonesGestExp[2] = true;
                    ConstantesCatastro.botonesGestExp[3] = false;
                    ConstantesCatastro.botonesGestExp[4] = false;
                    ConstantesCatastro.botonesGestExp[5] = true;
                    ConstantesCatastro.botonesGestExp[6] = false;
                    ConstantesCatastro.botonesGestExp[7] = true;
                    ConstantesCatastro.botonesGestExp[8] = false;
                    
                }
                else
                {
                    ConstantesCatastro.botonesGestExp[0] = true;
                    ConstantesCatastro.botonesGestExp[1] = true;
                    ConstantesCatastro.botonesGestExp[2] = false;
                    ConstantesCatastro.botonesGestExp[3] = false;
                    ConstantesCatastro.botonesGestExp[4] = false;
                    ConstantesCatastro.botonesGestExp[5] = true;
                    ConstantesCatastro.botonesGestExp[6] = false;
                    ConstantesCatastro.botonesGestExp[7] = false;
                    ConstantesCatastro.botonesGestExp[8] = false;
                }
                break;
            }
            case ConstantesCatastro.ESTADO_GENERADO:
            {
                if(ConstantesCatastro.modoTrabajo.equalsIgnoreCase(DatosConfiguracion.MODO_TRABAJO_ACOPLADO))
                {
                    ConstantesCatastro.botonesGestExp[0] = true;
                    ConstantesCatastro.botonesGestExp[1] = true;
                    ConstantesCatastro.botonesGestExp[2] = false;
                    ConstantesCatastro.botonesGestExp[3] = false;
                    ConstantesCatastro.botonesGestExp[4] = false;
                    ConstantesCatastro.botonesGestExp[5] = true;
                    ConstantesCatastro.botonesGestExp[6] = false;
                    ConstantesCatastro.botonesGestExp[7] = false;
                    ConstantesCatastro.botonesGestExp[8] = true;
                }
                else
                {
                    ConstantesCatastro.botonesGestExp[0] = true;
                    ConstantesCatastro.botonesGestExp[1] = true;
                    ConstantesCatastro.botonesGestExp[2] = false;
                    ConstantesCatastro.botonesGestExp[3] = false;
                    ConstantesCatastro.botonesGestExp[4] = false;
                    ConstantesCatastro.botonesGestExp[5] = true;
                    ConstantesCatastro.botonesGestExp[6] = false;
                    ConstantesCatastro.botonesGestExp[7] = false;
                    ConstantesCatastro.botonesGestExp[7] = false;
                    ConstantesCatastro.botonesGestExp[8] = false;
                }
                break;
            }
            case ConstantesCatastro.ESTADO_ENVIADO:
            {
                if(ConstantesCatastro.modoTrabajo.equalsIgnoreCase(DatosConfiguracion.MODO_TRABAJO_ACOPLADO))
                {
                    ConstantesCatastro.botonesGestExp[0] = true;
                    ConstantesCatastro.botonesGestExp[1] = true;
                    ConstantesCatastro.botonesGestExp[2] = false;
                    ConstantesCatastro.botonesGestExp[3] = false;
                    ConstantesCatastro.botonesGestExp[4] = false;
                    ConstantesCatastro.botonesGestExp[5] = true;
                    ConstantesCatastro.botonesGestExp[6] = false;
                    ConstantesCatastro.botonesGestExp[7] = false;
                    ConstantesCatastro.botonesGestExp[8] = true;
                }
                else
                {
                    ConstantesCatastro.botonesGestExp[0] = true;
                    ConstantesCatastro.botonesGestExp[1] = true;
                    ConstantesCatastro.botonesGestExp[2] = false;
                    ConstantesCatastro.botonesGestExp[3] = false;
                    ConstantesCatastro.botonesGestExp[4] = false;
                    ConstantesCatastro.botonesGestExp[5] = true;
                    ConstantesCatastro.botonesGestExp[6] = false;
                    ConstantesCatastro.botonesGestExp[7] = false;
                    ConstantesCatastro.botonesGestExp[8] = false;
                }
                break;
            }
            case ConstantesCatastro.ESTADO_CERRADO:
            {
                if(ConstantesCatastro.modoTrabajo.equalsIgnoreCase(DatosConfiguracion.MODO_TRABAJO_ACOPLADO))
                {
                    ConstantesCatastro.botonesGestExp[0] = true;
                    ConstantesCatastro.botonesGestExp[1] = true;
                    ConstantesCatastro.botonesGestExp[2] = false;
                    ConstantesCatastro.botonesGestExp[3] = false;
                    ConstantesCatastro.botonesGestExp[4] = false;
                    ConstantesCatastro.botonesGestExp[5] = true;
                    ConstantesCatastro.botonesGestExp[6] = false;
                    ConstantesCatastro.botonesGestExp[7] = false;
                    ConstantesCatastro.botonesGestExp[8] = false;
                }
                else
                {
                    ConstantesCatastro.botonesGestExp[0] = true;
                    ConstantesCatastro.botonesGestExp[1] = true;
                    ConstantesCatastro.botonesGestExp[2] = false;
                    ConstantesCatastro.botonesGestExp[3] = false;
                    ConstantesCatastro.botonesGestExp[4] = false;
                    ConstantesCatastro.botonesGestExp[5] = true;
                    ConstantesCatastro.botonesGestExp[6] = false;
                    ConstantesCatastro.botonesGestExp[7] = false;
                    ConstantesCatastro.botonesGestExp[8] = false;
                }
                break;
            }
        }
    }
}
