package com.geopista.protocol.alp;

import java.io.Serializable;
import java.util.Iterator;

import com.geopista.protocol.AbstractValidator;
import com.geopista.server.administradorCartografia.ACAttribute;
import com.geopista.server.administradorCartografia.ACFeature;
import com.geopista.server.administradorCartografia.ACFeatureUpload;
import com.geopista.server.administradorCartografia.ACLayer;

public class NumerosPoliciaValidator extends AbstractValidator{

	public NumerosPoliciaValidator(){
		
	}
	
	public void afterInsert(ACFeatureUpload uploadFeature, ACLayer acLayer){
		System.out.println("Posterior a la insercción");
		//Situar aquí la llamada al cliente de los servicios web de ALP
		ACFeature acFeature=new ACFeature();
        ACAttribute att=null;
        Object oAttValue=null;
        for (Iterator it=acLayer.getAttributes().values().iterator();it.hasNext();){
            att=(ACAttribute)it.next();
            try
            {
//               oAttValue=rs.getObject(att.getColumn().getName());
            	int index = att.getPosition() - 1;
               acFeature.setAttribute(att.getName(),(Serializable) uploadFeature.getAttValues()[index]);
            }catch(Exception ex)
            {
//                logger.error("No se puede cargar la columna: "+att.getColumn().getName(),ex);
            }
        }
        System.out.println("Posterior a la insercción");
	}

}
