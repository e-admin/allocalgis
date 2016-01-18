package com.geopista.app.inventario.sicalwin;



import java.util.ArrayList;

import com.geopista.app.inventario.sicalwin.dao.Entidad;
import com.geopista.app.inventario.sicalwin.dao.TerceroyCuenta;
import com.geopista.app.inventario.sicalwin.operaciones.Entidades;
import com.geopista.app.inventario.sicalwin.operaciones.OperacionGasto;
import com.geopista.app.inventario.sicalwin.operaciones.TercerosyCuentas;

public class IntegracionSicalwin {
	
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(IntegracionSicalwin.class);
	
	public IntegracionSicalwin() {
	}
		
	public static void main(String[] args) {
		
		/*TercerosyCuentas tercerosyCuentas=new TercerosyCuentas();
		ArrayList<TerceroyCuenta> listaTercerosyCuentas=tercerosyCuentas.getTercerosyCuentas();
		logger.info(listaTercerosyCuentas);

		Entidades entidades=new Entidades();
		ArrayList<Entidad> listaEntidades=entidades.getEntidades();
		logger.info(listaEntidades);*/
		
		OperacionGasto operacionGasto=new OperacionGasto();
		operacionGasto.generarOperacionGasto();		

	}

}
