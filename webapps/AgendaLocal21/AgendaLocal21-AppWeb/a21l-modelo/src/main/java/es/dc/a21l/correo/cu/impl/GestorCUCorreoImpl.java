/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.correo.cu.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import es.dc.a21l.correo.cu.GestorCUCorreo;
  
/** 
 * Servicio de envío de emails 
 */  
public class GestorCUCorreoImpl {  
  
    private static final Log log = LogFactory.getLog(GestorCUCorreoImpl.class);  
  
    private MailSender mailSender;
    private String from;
    
	public void setFrom(String from) {
		this.from = from;
	}
    public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}
 
	public void enviarCorreo(String to, String subject, String msg) {
 
		SimpleMailMessage message = new SimpleMailMessage();
 
		message.setFrom(from);
		message.setTo(to);
		message.setSubject(subject);
		message.setText(msg);
		mailSender.send(message);	
	}
}  