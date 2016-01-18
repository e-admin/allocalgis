/**
 * GeopistaPostGreGestorEventos.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.planeamiento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.util.ApplicationContext;
import com.geopista.util.config.UserPreferenceStore;

/**
 * Esta clase contiene los métodos necesarios para trabajar con el gestor de
 * eventos de planeamiento
 */
public class GeopistaPostGreGestorEventos
{
    private static ApplicationContext app = AppContext.getApplicationContext();
    
    public String numero;
    
    public String nombre;
    
    public String idtipogest;
    
    public String ejecutado;
    
    public String autor;
    
    public String descripcion;
    
    public String periodicidad;
    
    public String mensaje;
    
    public String comentario;
    
    public String idHistGest;
    
    public String fechaHistEventos;
    
    public String fecha;
    
    public String oid;
    
    public Integer idMaxEvento;
    
    public Integer idMaxAviso;
    
    public String idMaxEventoText;
    
    public Connection con = null;
    
    public GeopistaPostGreGestorEventos()
    {
    }
    
    /**
     * Realiza la conexión con la base de datos
     * 
     * @return Devuelve la conexión establecida con la base de datos
     */
    public static Connection getDBConnection() throws SQLException
    {
        Connection conn = app.getConnection();
        return conn;
    }
    
    /**
     * Número de ámbito del ámbito de gestión seleccionado
     * 
     * @param numeroAmbito
     *            Identificador del Ambito seleccionado en el mapa
     * @param con
     *            Conexión con la Base de Datos
     * @return Devuelve en un String el Número de Ambito. Devuelve null cuando
     *         se han producido errores o el registro esta vacio.
     */
    public String getNumero(String numeroAmbito, Connection con) throws SQLException
    {
        String numero = "";
        ResultSet r = null;
        PreparedStatement ps = null;
        
        try
        {
            ps = con.prepareStatement("planeamientogestornumero");
            ps.setString(1, numeroAmbito);
            
            if (!ps.execute())
            {
            } else
            {
                r = ps.getResultSet();
                
                while (r.next())
                {
                    numero = r.getString("numero");
                    
                }
            }
        } finally
        {
            app.closeConnection(null, ps, null, r);
        }
        return numero;
        
    }
    
    /**
     * Obtiene el identificador del ámbito de gestión seleccionado en el mapa
     * 
     * @param numero
     *            Numero de Ambito
     * @param con
     *            Conexión con la Base de Datos
     * @return Devuelve en un String el Identificador del Ambito de Gestión.
     *         Devuelve null cuando se han producido errores o el registro esta
     *         vacio.
     */
    
    public String getIdAmbito(String numero, Connection con) throws SQLException
    {
        
        String id = "";
        ResultSet r = null;
        PreparedStatement ps = null;
        try
        {
            ps = con.prepareStatement("planeamientogestoridentificador");
            ps.setString(1, numero);
            
            if (!ps.execute())
            {
            } else
            {
                r = ps.getResultSet();
                
                while (r.next())
                {
                    id = r.getString("id");
                }
            }
        } finally
        {
            app.closeConnection(null, ps, null, r);
        }
        
        return id;
        
    }
    
    /**
     * Establece el nuevo número de Ambito
     * 
     * @param newnumero
     *            Nuevo Numero de Ambito
     * @param con
     *            Conexión con la Base de Datos
     */
    
    public void setNumero(String newNumero, Connection con)
    {
        numero = newNumero;
    }
    
    /**
     * Obtiene el nombre del ámbito de gestión seleccionado en el mapa
     * 
     * @param numeroAmbito
     *            Numero de Ambito
     * @param con
     *            Conexión con la Base de Datos
     * @return Devuelve en un String el nombre del Ambito de Gestión. Devuelve
     *         null cuando se han producido errores o el registro esta vacio.
     */
    
    public String getNombre(String numeroAmbito, Connection con) throws SQLException
    {
        ResultSet r = null;
        PreparedStatement ps = null;
        try
        {
            ps = con.prepareStatement("planeamientogestornombre");
            ps.setString(1, numeroAmbito);
            
            if (!ps.execute())
            {
            } else
            {
                r = ps.getResultSet();
                
                while (r.next())
                {
                    nombre = r.getString("nombre");
                }
            }
        } finally
        {
            app.closeConnection(null, ps, null, r);
        }
        
        return nombre;
        
    }
    
    /**
     * Establece el nuevo nombre de Ambito
     * 
     * @param newNombre
     *            Nuevo Numero de Ambito
     * @param con
     *            Conexión con la Base de Datos
     */
    
    public void setNombre(String newNombre, Connection con)
    {
        nombre = newNombre;
    }
    
    /**
     * Obtiene el identificador del tipo de gestión seleccionado en el mapa
     * 
     * @param numeroAmbito
     *            Numero de Ambito
     * @param con
     *            Conexión con la Base de Datos
     * @return Devuelve en un String el identificador del tipo de gestión.
     *         Devuelve null cuando se han producido errores o el registro esta
     *         vacio.
     */
    public String getIdtipogest(String numeroAmbito, Connection con) throws SQLException
    {
        ResultSet r = null;
        PreparedStatement ps = null;
        try
        {
            ps = con.prepareStatement("planeamientogestortipogest");
            ps.setString(1, numeroAmbito);
            
            if (!ps.execute())
            {
            } else
            {
                r = ps.getResultSet();
                
                while (r.next())
                {
                    idtipogest = r.getString("idtipget");
                    
                }
            }
        } finally
        {
            app.closeConnection(null, ps, null, r);
        }
        return idtipogest;
    }
    
    /**
     * Busca la descripción del Tipo de Gestión del Ambito seleccionado.
     * 
     * @param con
     *            Conexión con la Base de Datos
     * @param idTipo
     *            identificador del tipo de gestión
     * @return Devuelve en un String la descripción del tipo de gestión.
     *         Devuelve null cuando se han producido errores o el registro esta
     *         vacio.
     */
    
    public String getDescriTipoGest(String idTipo, int idDominio, Connection con)
    throws SQLException
    {
        ResultSet r = null;
        
        PreparedStatement ps = null;
        try
        {
            ps = con.prepareStatement("planeamientogestordescritipo");
            ps.setString(1, idTipo);
            ps.setInt(2, idDominio);
            ps.setString(3, UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOCALE_KEY, "es_ES", false));
            
            if (!ps.execute())
            {
            } else
            {
                r = ps.getResultSet();
                
                while (r.next())
                {
                    idtipogest = r.getString(1);
                    
                }
            }
            
        } finally
        {
            app.closeConnection(null, ps, null, r);
            
        }
        return idtipogest;
    }
    
    /**
     * Establece el identificador del tipo de gestión seleccionado en el mapa
     * 
     * @param newIdtipogest
     *            Nuevo Identificador
     * @param con
     *            Conexión con la Base de Datos
     */
    public void setIdtipogest(String newIdtipogest, Connection con) throws SQLException
    {
        idtipogest = newIdtipogest;
    }
    
    /**
     * Obtiene el valor del campo ejecutado
     * 
     * @param numeroAmbito
     *            Numero de Ambito
     * @param con
     *            Conexión con la Base de Datos
     * @return El valor del campo ejecutado
     */
    public String getEjecutado(String numeroAmbito, Connection con) throws SQLException
    {
        ResultSet r = null;
        
        PreparedStatement ps = null;
        try
        {
            ps = con.prepareStatement("planeamientogestorejecutado");
            ps.setString(1, numeroAmbito);
            
            if (!ps.execute())
            {
            } else
            {
                r = ps.getResultSet();
                
                while (r.next())
                {
                    ejecutado = r.getString("ejecutado");
                    
                }
            }
            
        } finally
        {
            app.closeConnection(null, ps, null, r);
        }
        return ejecutado;
    }
    
    /**
     * Listado de eventos del ámbito de gestión seleccionado en el mapa
     * 
     * @param numeroAmbito
     *            Número del Ambito seleccionado en el mapa
     * @param con
     *            Conexión con la Base de Datos
     * @return Devuelve en un ArrayList los identificadores de los eventos del
     *         ámbito de gestión. Devuelve null cuando no se han encontrado
     *         registros.
     */
    public ArrayList getListadoEventos(String numeroAmbito, Connection con) throws SQLException
    {
        ArrayList resultado = new ArrayList();
        ResultSet r = null;
        PreparedStatement ps = null;
        try
        {
            ps = con
            .prepareStatement("planeamientogestorlistadoeventos");
            ps.setString(1, numeroAmbito);
            
            if (!ps.execute())
            {
            } else
            {
                r = ps.getResultSet();
                
                while (r.next())
                {
                    resultado.add(r.getString("id"));
                }// del while
            }
        } finally
        {
            app.closeConnection(null, ps, null, r);
        }
        return resultado;
        
    }
    
    /**
     * Listado de avisos del ámbito de gestión seleccionado en el mapa
     * 
     * @param numeroAmbito
     *            Número del Ambito seleccionado en el mapa
     * @param con
     *            Conexión con la Base de Datos
     * @return Devuelve en un ArrayList los identificadores de los avisos del
     *         ámbito de gestión. Devuelve null cuando no se han encontrado
     *         registros.
     */
    
    public ArrayList getListadoAvisos(String numeroAmbito, Connection con) throws SQLException
    {
        ArrayList resultado = new ArrayList();
        ResultSet r = null;
        
        PreparedStatement ps = null;
        try
        {
            ps = con
            .prepareStatement("planeamientogestorlistadoavisos");
            ps.setString(1, numeroAmbito);
            
            if (!ps.execute())
            {
            } else
            {
                r = ps.getResultSet();
                while (r.next())
                {
                    resultado.add(r.getString("id"));
                }// del while
            }
        } finally
        {
            app.closeConnection(null, ps, null, r);
        }
        return resultado;
    }
    
    /**
     * Obtiene el autor del evento seleccionado
     * 
     * @param idEvento
     *            Identificador del evento seleccionado
     * @param con
     *            Conexión con la Base de Datos
     * @return el autor del evento seleccionado
     */
    
    public String getAutorHistorico(String idEvento, Connection con) throws SQLException
    {
        ResultSet r = null;
        PreparedStatement ps = null;
        try
        {
            ps = con
            .prepareStatement("planeamientogestorhistoricoautor");
            ps.setString(1, idEvento);
            
            if (!ps.execute())
            {
            } else
            {
                r = ps.getResultSet();
                while (r.next())
                {
                    autor = r.getString("autor");
                }// while
            }
        } finally
        {
            app.closeConnection(null, ps, null, r);
        }// catch
        return autor;
        
    }
    
    /**
     * Obtiene la descripción del evento seleccionado
     * 
     * @param idEvento
     *            Identificador del evento seleccionado
     * @param con
     *            Conexión con la Base de Datos
     * @return Descripción del evento seleccionado
     */
    public String getDescripcionHistorico(String idEvento, Connection con) throws SQLException
    {
        String numero = "";
        ResultSet r = null;
        PreparedStatement ps = null;
        try
        {
            ps = con
            .prepareStatement("planeamientogestorhistoricodescripcion");
            ps.setString(1, idEvento);
            if (!ps.execute())
            {
            } else
            {
                r = ps.getResultSet();
                while (r.next())
                {
                    descripcion = r.getString("descripcion");
                }// while*/
            }
        } finally
        {
            app.closeConnection(null, ps, null, r);
        }// catch
        return descripcion;
    }
    
    /**
     * Obtiene la periodicidad del aviso seleccionado
     * 
     * @param idAviso
     *            Identificador del aviso seleccionado
     * @param con
     *            Conexión con la Base de Datos
     * @return Periodicidad del aviso seleccionado
     */
    public String getPeriodicidadAviso(String idAviso, Connection con) throws SQLException
    {
        ResultSet r = null;
        PreparedStatement ps = null;
        try
        {
            ps = con
            .prepareStatement("planeamientogestoravisosperiodicidad");
            ps.setString(1, idAviso);
            
            if (!ps.execute())
            {
            } else
            {
                r = ps.getResultSet();
                while (r.next())
                {
                    periodicidad = r.getString("periodicidad");
                }// while*/
                
                
            }
            
        } finally
        {
            app.closeConnection(null, ps, null, r);
        }// catch
        
        return periodicidad;
    }
    
    /**
     * Obtiene el mensaje del aviso seleccionado
     * 
     * @param idAviso
     *            Identificador del aviso seleccionado
     * @param con
     *            Conexión con la Base de Datos
     * @return Mensaje del aviso seleccionado
     */
    
    public String getMensajeAviso(String idAviso, Connection con) throws SQLException
    {
        ResultSet r = null;
        PreparedStatement ps = null;
        try
        {
            ps = con
            .prepareStatement("planeamientogestoravisosmensaje");
            ps.setString(1, idAviso);
            
            if (!ps.execute())
            {
            } else
            {
                r = ps.getResultSet();
                
                while (r.next())
                {
                    mensaje = r.getString("mensaje");
                }// while
            }
        } finally
        {
            app.closeConnection(null, ps, null, r);
        }// catch
        return mensaje;
        
    }
    
    /**
     * Obtiene los comentarios del aviso seleccionado
     * 
     * @param idAviso
     *            Identificador del aviso seleccionado
     * @param con
     *            Conexión con la Base de Datos
     * @return Comentario del aviso seleccionado
     */
    public String getComentarioAviso(String idAviso, Connection con) throws SQLException
    {
        
        ResultSet r = null;
        
        PreparedStatement ps = null;
        try
        {
            ps = con
            .prepareStatement("planeamientogestoravisoscomentario");
            ps.setString(1, idAviso);
            
            if (!ps.execute())
            {
            } else
            {
                r = ps.getResultSet();
                while (r.next())
                {
                    comentario = r.getString("comentario");
                }
            }
        } finally
        {
            app.closeConnection(null, ps, null, r);
        }// catch
        return comentario;
    }
    
    /**
     * Modifica los datos de un evento
     * 
     * @param idEvento
     *            Identificador del evento seleccionado
     * @param fecha
     *            Fecha del evento
     * @param autor
     *            Autor del evento
     * @param descripcion
     *            Descripcion del evento
     * @param con
     *            Conexión con la Base de Datos
     */
    public void modificarHistorico(String idEvento, String fecha, String autor,
            String descripcion, Connection con) throws SQLException
            {
        PreparedStatement ps = null;
        try
        {
            ps = con
            .prepareStatement("planeamientogestorhistoricosmodificar");
            ps.setString(1, autor);
            ps.setString(2, descripcion);
            ps.setString(3, fecha);
            ps.setString(4, idEvento);
            ps.executeUpdate();
        } finally
        {
            app.closeConnection(null, ps, null, null);
        }// catch
        
            }
    
    /**
     * Obtiene la fecha del evento seleccionado
     * 
     * @param idHistGest
     *            Identificador del Evento
     * @param con
     *            Conexión con la Base de Datos
     * @return Fecha del Evento
     */
    public String getFechaEventos(String idHistGest, Connection con) throws SQLException
    {
        ResultSet r = null;
        PreparedStatement ps = null;
        
        try
        {
            ps = con
            .prepareStatement("planeamientogestorhistoricofecha");
            ps.setString(1, idHistGest);
            
            if (!ps.execute())
            {
            } else
            {
                r = ps.getResultSet();
                
                while (r.next())
                {
                    fecha = r.getString("fecha");
                }
            }
        } finally
        {
            app.closeConnection(null, ps, null, r);
        }// catch
        return fecha;
        
    }
    
    /**
     * Obtiene la fecha del aviso seleccionado
     * 
     * @param idAviso
     *            Identificador del aviso
     * @param con
     *            Conexión con la Base de Datos
     * @return Fecha del aviso
     */
    
    public String getFechaAviso(String idAviso, Connection con) throws SQLException
    {
        ResultSet r = null;
        PreparedStatement ps = null;
        try
        {
            ps = con.prepareStatement("planeamientogestoravisosfecha");
            ps.setString(1, idAviso);
            
            if (!ps.execute())
            {
            } else
            {
                r = ps.getResultSet();
                while (r.next())
                {
                    fecha = r.getString("fecha_aviso");
                }// while
                
                
            }
            
        } finally
        {
            app.closeConnection(null, ps, null, r);
        }// catch
        return fecha;
    }
    
    /**
     * Modifica los datos de un aviso
     * 
     * @param idAviso
     *            Identificador del aviso seleccionado
     * @param fechaAviso
     *            Fecha del aviso
     * @param periodicidadAviso
     *            Periodicidad del aviso
     * @param mensajeAviso
     *            Mensaje del aviso
     * @param comentarioAviso
     *            Comentario del aviso
     * @param con
     *            Conexión con la Base de Datos
     */
    public void modificarAviso(String idAviso, String fechaAviso,
            String periodicidadAviso, String comentarioAviso, String mensajeAviso,
            Connection con) throws SQLException
            {
        PreparedStatement ps = null;
        try
        {
            ps = con
            .prepareStatement("planeamientogestoravisosmodificar");
            ps.setString(1, fechaAviso);
            ps.setString(2, periodicidadAviso);
            ps.setString(3, comentarioAviso);
            ps.setString(4, mensajeAviso);
            ps.setString(5, idAviso);
            ps.executeUpdate();
            
            
        } finally
        {
            app.closeConnection(null, ps, null, null);
        }// catch
        
            }
    
    /**
     * Inserta los datos de un evento
     * 
     * @param idEvento
     *            Identificador del evento seleccionado
     * @param numeroAmbito
     *            Número de Ambito del evento
     * @param fecha
     *            Fecha del evento
     * @param autor
     *            Autor del evento
     * @param descripcion
     *            Descripcion del evento
     * @param con
     *            Conexión con la Base de Datos
     */
    public void insertarEvento(int idEvento, String numeroAmbito, String fecha,
            String autor, String descripcion, Connection con) throws SQLException
            {
        PreparedStatement ps = null;
        try
        {
            ps = con
            .prepareStatement("planeamientogestorhistoricoinsertar");
            ps.setInt(1, idEvento);
            ps.setString(2, numeroAmbito);
            ps.setString(3, fecha);
            ps.setString(4, autor);
            ps.setString(5, descripcion);
            ps.executeUpdate();
        } finally
        {
            app.closeConnection(null, ps, null, null);
        }// catch
        
            }
    
    /**
     * Inserta los datos de un aviso
     * 
     * @param idAviso
     *            Identificador del aviso seleccionado
     * @param numeroAmbito
     *            Número de Ambito del aviso
     * @param fecha
     *            Fecha del aviso
     * @param periodo
     *            Periodicidad del aviso
     * @param mensaje
     *            Mensaje del aviso
     * @param comentario
     *            Comentario del aviso
     * @param con
     *            Conexión con la Base de Datos
     */
    public void insertarAviso(int idAviso, String numeroAmbito, String fecha,
            String periodo, String mensaje, String comentario, Connection con) throws SQLException
            {
        PreparedStatement ps = null;
        try
        {
            
            ps = con
            .prepareStatement("planeamientogestoravisosinsertar");
            ps.setInt(1, idAviso);
            ps.setString(2, numeroAmbito);
            ps.setString(3, fecha);
            ps.setString(4, periodo);
            ps.setString(5, mensaje);
            ps.setString(6, comentario);
            
            ps.executeUpdate();
            
        } finally
        {
            app.closeConnection(null, ps, null, null);
        }// catch
        
            }
    
    /**
     * Obtiene el máximo identificador del evento registrado en la base de datos
     * 
     * @param con
     *            Conexión con la Base de Datos
     * @return Maximo Identificador del Evento a insertar
     */
    
    public int getMaxIdEvento(Connection con) throws SQLException
    {
        int idMaxEvento = 0;
        ResultSet r = null;
        
        PreparedStatement ps = null;
        
        try
        {
            ps = con
            .prepareStatement("planeamientogestorhistoricosmaximo");
            
            if (!ps.execute())
            {
            } else
            {
                r = ps.getResultSet();
                
                while (r.next())
                {
                    idMaxEvento = r.getInt(1);
                }// while
            }
            
        } finally
        {
            app.closeConnection(null, ps, null, r);
        }// catch
        return idMaxEvento;
    }
    
    /**
     * Obtiene el máximo identificador del aviso registrado en la base de datos
     * 
     * @param con
     *            Conexión con la Base de Datos
     * @return Maximo Identificador del Aviso a insertar
     */
    public int getMaxIdAviso(Connection con) throws SQLException
    {
        int idMaxAviso = 0;
        ResultSet r = null;
        PreparedStatement ps = null;
        
        try
        {
            ps = con.prepareStatement("planeamientogestoravisosmaximo");
            
            if (!ps.execute())
            {
            } else
            {
                r = ps.getResultSet();
                
                while (r.next())
                {
                    idMaxAviso = r.getInt(1);
                    // idMaxAviso = idMaxAviso + 1;
                }// while
                app.closeConnection(null, ps, null, r);
                
            }
        } finally
        {
            app.closeConnection(null, ps, null, r);
        }// catch
        return idMaxAviso;
    }
    
    /**
     * Elimina de la base de datos el evento seleccionado
     * 
     * @param idEvento
     *            Identificador del evento seleccionado
     * @param con
     *            Conexión con la Base de Datos
     */
    
    public void eliminarHistorico(String idEvento, Connection con) throws SQLException
    {
        PreparedStatement ps = null;
        try
        {
            ps = con
            .prepareStatement("planeamientogestorhistoricoeliminar");
            ps.setString(1, idEvento);
            
            ps.executeUpdate();
            
            
        } finally
        {
            app.closeConnection(null, ps, null, null);
        }// catch
        
    }
    
    /**
     * Elimina de la base de datos el aviso seleccionado
     * 
     * @param idAviso
     *            Identificador del aviso seleccionado
     * @param con
     *            Conexión con la Base de Datos
     */
    public void eliminarAviso(String idAviso, Connection con) throws SQLException
    {
        PreparedStatement ps = null;
        try
        {
            
            ps = con
            .prepareStatement("planeamientogestoravisoseliminar");
            ps.setString(1, idAviso);
            
            ps.executeUpdate();
            
            
        } finally
        {
            app.closeConnection(null, ps, null, null);
        }// catch
    }
    
}