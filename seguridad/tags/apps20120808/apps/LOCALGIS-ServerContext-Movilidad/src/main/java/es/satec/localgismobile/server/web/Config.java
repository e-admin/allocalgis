package es.satec.localgismobile.server.web;

import es.satec.localgismobile.server.Global;

import javax.servlet.http.HttpServlet;
import javax.servlet.ServletException;


public class Config extends HttpServlet {

    private static final String cvsid = "$Id: Config.java,v 1.1 2011/09/19 13:57:27 satec Exp $";


    public void init() throws ServletException {
        super.init();
        System.out.println("[Config.init()] Iniciando sistema de logs...");
        Global.setLog4jConfig();
    }
}
