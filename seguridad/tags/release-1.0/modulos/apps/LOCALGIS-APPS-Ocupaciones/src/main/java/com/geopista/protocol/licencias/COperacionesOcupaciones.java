package com.geopista.protocol.licencias;

import java.util.Vector;

import com.geopista.app.AppContext;
import com.geopista.global.ServletConstants;
import com.geopista.global.WebAppConstants;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.net.EnviarSeguro;

public class COperacionesOcupaciones extends COperacionesLicencias {

	public static CResultadoOperacion enviar(String text, Vector vAnexos) throws Exception{
		return EnviarSeguro.enviar(AppContext.getApplicationContext().getString(AppContext.HOST_ADMCAR) + "/" + WebAppConstants.OCUPACIONES_WEBAPP_NAME +
	            ServletConstants.CSERVLETOCUPACIONES_SERVLET_NAME, text, vAnexos);
	}
	
}
