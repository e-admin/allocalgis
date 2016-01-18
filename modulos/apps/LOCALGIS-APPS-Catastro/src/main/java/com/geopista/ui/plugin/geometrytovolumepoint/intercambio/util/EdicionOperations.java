/**
 * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * For more information, contact:
 *
 * 
 * www.geopista.com
 *
 */


package com.geopista.ui.plugin.geometrytovolumepoint.intercambio.util;


/**
 * Definición de métodos para la realización de operaciones sobre base de 
 * datos
 * 
 * @author cotesa
 *
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import com.geopista.app.AppConstants;
import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.catastro.intercambio.exception.DataException;
import com.geopista.ui.plugin.geometrytovolumepoint.model.beans.TipoSubparcela;
import com.geopista.util.config.UserPreferenceStore;

public class EdicionOperations
{
    /**
     * Conexión a base de datos
     */
    public Connection conn = null;
    /**
     * Conexión a base de datos sin pasar por el administrador de cartografía
     */
    public static Connection directConn = null;
    
    /**
     * Contexto de la aplicación
     */
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();

    /**
     * Locale que identifica el idioma del usuario
     */
    private String locale = UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOCALE_KEY, AppConstants.DEFAULT_LOCALE, false);
    public static Set familiasModificadas = new HashSet(); 

    
    /**
     * Constructor por defecto
     *
     */
    public EdicionOperations()
    {        
        try
        {
            conn = getDBConnection();
        }
        catch(Exception e)
        { 
            e.printStackTrace();
        }        
    }    
  
    
    /**
     * Obtiene una conexión a la base de datos
     * @return Conexión a la base de datos
     * @throws SQLException
     */
    private static Connection getDBConnection () throws SQLException
    {        
        Connection con=  aplicacion.getConnection();
        con.setAutoCommit(false);
        return con;
    }  
    
    /**
	 * Obtiene un arraylist de objetos de tipo TipoSubparcela con todos los tipos
	 * de destino del sistema
	 * 
	 * @return ArrayList de objetos TipoSubparcela
	 * @throws DataException Si se produce un error de acceso a datos
	 */
	public ArrayList obtenerTiposSubparcela() {
		ArrayList lstTiposSubparcela = new ArrayList();

		try {
			PreparedStatement s = null;
			ResultSet r = null;

			s = conn.prepareStatement("MCobtenerTipoSubparcela");
			r = s.executeQuery();
			TipoSubparcela tipoSubparcela = new TipoSubparcela();
//			lstTiposSubparcela.add(tipoSubparcela);

			while (r.next()) {
				tipoSubparcela = new TipoSubparcela();
				tipoSubparcela.setId(r.getInt(1));
				tipoSubparcela.setPatron(r.getString(2));
				tipoSubparcela.setDescripcion(r.getString(3));
				lstTiposSubparcela.add(tipoSubparcela);
			}

			if (s != null)
				s.close();
			if (r != null)
				r.close();
			conn.close();

			Comparator tiposComparator = new Comparator() {
				public int compare(Object o1, Object o2) {
					TipoSubparcela t1 = (TipoSubparcela) o1;
					TipoSubparcela t2 = (TipoSubparcela) o2;

					String desc1 = t1.getPatron();
					String desc2 = t2.getPatron();

					Collator myCollator = Collator.getInstance(new Locale(locale));
					myCollator.setStrength(Collator.PRIMARY);
					return myCollator.compare(desc1, desc2);
				}
			};

			Collections.sort(lstTiposSubparcela, tiposComparator);

		} catch (SQLException ex) {
			new DataException(ex);
		}

		return lstTiposSubparcela;
	}
    
}


