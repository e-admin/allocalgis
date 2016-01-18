package com.geopista.app.catastro.intercambio.edicion.validacion;

import java.sql.Connection;
import com.geopista.app.catastro.intercambio.exception.DataException;
import com.geopista.app.catastro.model.beans.*;

public class ValidacionExpediente {
	
    private Expediente expediente;
    
    
    public ValidacionExpediente(Expediente expediente)
    {
    	this.expediente = expediente;
    }
    

    public String validacionesExpediente(Connection conn, Expediente expediente)throws DataException
    {
    	if (expediente.getFechaAlteracion() != null){
	        if(!expediente.getFechaAlteracion().before(expediente.getFechaRegistro()))
	        {
	        	return "Error.J20";
	        }
    	}
    	if (expediente.getFechaMovimiento() != null){
	        if(!expediente.getFechaMovimiento().after(expediente.getFechaRegistro()))
	        {
	        	return "Error.J20";
	        }
    	}
        return null;
    }

}
