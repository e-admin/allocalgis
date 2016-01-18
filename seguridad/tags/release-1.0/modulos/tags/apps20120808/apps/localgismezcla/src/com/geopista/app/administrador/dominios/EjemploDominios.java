package com.geopista.app.administrador.dominios;

import com.geopista.protocol.metadatos.*;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.administrador.Municipio;
import com.geopista.protocol.administrador.OperacionesAdministrador;
import com.geopista.app.administrador.CMainAdministrador;


import javax.swing.*;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Locale;

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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
 USA.
 *
 * For more information, contact:
 *
 *
 * www.geopista.com
 *
 *
 */


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 06-oct-2004
 * Time: 10:16:37
 */
public class EjemploDominios extends javax.swing.JFrame{
    public EjemploDominios() {
        try
        {
            String idApp="Administracion";
            com.geopista.app.administrador.init.Constantes.idMunicipio=34083;
            com.geopista.app.administrador.init.Constantes.url="http://localhost:54321/administracion/";
            //com.geopista.security.SecurityManager.setHeartBeatTime(100000);
            com.geopista.security.SecurityManager.setHeartBeatTime(10000);
            com.geopista.security.SecurityManager.setsUrl(com.geopista.app.administrador.init.Constantes.url);
            com.geopista.security.SecurityManager.login("syssuperuser", "sysgeopass",
                                                   idApp);
            try
            {
                 Municipio municipio =
                         (new OperacionesAdministrador(com.geopista.app.administrador.init.Constantes.url)).getMunicipio(new Integer(com.geopista.app.administrador.init.Constantes.idMunicipio).toString());
                if (municipio!=null)
                {
                        com.geopista.app.administrador.init.Constantes.Municipio = municipio.getNombre();
                        com.geopista.app.administrador.init.Constantes.Provincia= municipio.getProvincia();
                }
                javax.swing.JDesktopPane desktopPane=  new javax.swing.JDesktopPane();
                getContentPane().add(desktopPane, java.awt.BorderLayout.CENTER);
                setSize(900,700);
                 show();
                JInternalFrame aux= new JInternalFrame();
                ResourceBundle messages = ResourceBundle.getBundle("config.administrador", new Locale("es_ES"));
                aux.getContentPane().add(new JPanelDominios(messages ,this ,"1"));
                desktopPane.add(aux);
                aux.setMaximum(true);
				aux.show();

            }catch (Exception e)
            {
                e.printStackTrace();
            }


        }catch (Exception e)
        {
            System.out.println(e);
        }

    }
    	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {

      new EjemploDominios();
	}
}
