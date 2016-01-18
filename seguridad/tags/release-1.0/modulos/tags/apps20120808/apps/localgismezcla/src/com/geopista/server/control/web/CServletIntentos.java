package com.geopista.server.control.web;

import java.io.IOException;
import java.io.Writer;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.administrador.SegPassword;
import com.geopista.protocol.net.EnviarSeguro;
import com.geopista.server.database.COperacionesAdministrador;


/**
 * Esta clase es la primera que se llama al entrar en una aplicación
 * su función consiste en crear la sesión del usuario dentro del sistema
 * Para ello regoge del user principal el nombre del usuario y obtiene
 * los datos de este de la base de datos.
 * También obtiene el listado de entidades y municipios.
 * 
 * @author angeles
 *
 */
public class CServletIntentos extends HttpServlet{
	
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(CServletIntentos.class);
    public CServletIntentos(){
    	
    }
    public void doPost (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        CResultadoOperacion rActualizar=null;

		String usuario = request.getParameter(EnviarSeguro.userIntentos);
		String mensaje = request.getParameter(EnviarSeguro.mensajeXML);
		CResultadoOperacion resultado = new CResultadoOperacion(true, usuario);
        Vector vector = new Vector();
		if (mensaje != null && (mensaje.equals("bloquea"))){
			try {
				COperacionesAdministrador.bloqueaUsuario(usuario);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		else{
			if (mensaje != null && (mensaje.equals("actualizaIntentos"))){
				try {
					COperacionesAdministrador.actualizaIntentos(usuario);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else{
				if (mensaje != null && (mensaje.equals("reiniciaIntentos"))){
					try {
						if (usuario!=null)
							COperacionesAdministrador.reiniciaIntentos(usuario);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else{
					SegPassword segPassword = COperacionesAdministrador.getIntentosUsuario(usuario);
					vector.addElement(segPassword.getIntentos());
					vector.addElement(segPassword.isBloqueado());
					vector.addElement(segPassword.getIntentos_reiterados());
					vector.addElement(segPassword.isUsuarioNoExiste());
				}
			}
		}
        
       
        resultado.setVector(vector);
        response.setContentType ("text/html");
        Writer writer = response.getWriter();
        writer.write (resultado.buildResponse());
        writer.flush();
        writer.close();
      } 

    public void doGet (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        doPost(request, response);
     }
}