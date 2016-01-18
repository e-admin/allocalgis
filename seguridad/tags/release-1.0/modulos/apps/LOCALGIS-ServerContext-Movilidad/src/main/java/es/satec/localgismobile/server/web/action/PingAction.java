package es.satec.localgismobile.server.web.action;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class PingAction extends Action {
	
	protected Logger log = Logger.getLogger(PingAction.class);
	
	public ActionForward execute(ActionMapping map, ActionForm form, HttpServletRequest req, HttpServletResponse res) {

		String ping = req.getParameter("ping");

		log.debug("Ping recibido en el servidor");
		if (ping != null) {
			try {
				res.getWriter().print("pong");
			} catch (IOException e) {
				log.error("Error al realizar la escribir la respuesta del servidor");
			}
		}
        return null;
    }
}
