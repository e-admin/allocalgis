package com.geopista.app.eiel;

import org.apache.log4j.Logger;

import javax.swing.*;
import java.io.PrintWriter;
import java.io.StringWriter;

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
//                ((IMainLocalGISEIEL)desktop).resetSecurityPolicy();
                GeopistaAcl acl= com.geopista.security.SecurityManager.getPerfil("EIEL");
                ((IMainLocalGISEIEL)desktop).applySecurityPolicy(acl, com.geopista.security.SecurityManager.getPrincipal());
           }
        } catch (Exception e) {
            StringWriter sw= new StringWriter();
            PrintWriter pw= new PrintWriter(sw);
            e.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
        }
    }




}
