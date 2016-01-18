/**
 * UtilidadesComponentes.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.cementerios;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.Icon;
import javax.swing.JFrame;

import org.apache.log4j.Logger;

import com.geopista.security.GeopistaAcl;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 26-jul-2006
 * Time: 11:42:04
 * To change this template use File | Settings | File Templates.
 */
public class UtilidadesComponentes {
    private static Logger logger = Logger.getLogger(UtilidadesComponentes.class);
    public static Icon iconoAdd;
    public static Icon iconoOK;
    public static Icon iconoBuscar;
    public static Icon iconoBorrar;
    public static Icon iconoEditar;
    public static Icon iconoClear;

    public static void inicializar(){
        try{
            ClassLoader cl= (new UtilidadesComponentes()).getClass().getClassLoader();
            iconoAdd= new javax.swing.ImageIcon(cl.getResource("img/add.gif"));
            iconoOK= new javax.swing.ImageIcon(cl.getResource("img/ok.gif"));
            iconoBuscar= new javax.swing.ImageIcon(cl.getResource("img/zoom.gif"));
            iconoBorrar= new javax.swing.ImageIcon(cl.getResource("img/remove.gif"));
            iconoEditar= new javax.swing.ImageIcon(cl.getResource("img/editar.gif"));
            iconoClear= new javax.swing.ImageIcon(cl.getResource("img/delete_parcela.gif"));
        }catch(Exception e){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            logger.error("Exception al inicializar las imagenes: " + sw.toString());
        }
    }


    public static void setEnabled(boolean b, JFrame desktop) {
        try {
            //desktop.getJMenuBar().getMenu(0).setEnabled(b);
            if (b){
                ((MainCementerios)desktop).resetSecurityPolicy();
                GeopistaAcl acl= com.geopista.security.SecurityManager.getPerfil("Inventario");
                ((MainCementerios)desktop).applySecurityPolicy(acl, com.geopista.security.SecurityManager.getPrincipal());
           }
        } catch (Exception e) {
            StringWriter sw= new StringWriter();
            PrintWriter pw= new PrintWriter(sw);
            e.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
        }
    }




}
