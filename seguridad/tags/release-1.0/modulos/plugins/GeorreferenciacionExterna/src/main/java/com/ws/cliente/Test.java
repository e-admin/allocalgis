package com.ws.cliente;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.transport.http.HTTPConstants;

import com.geopista.ui.plugin.georreferenciacionExterna.FiltroData;
import com.localgis.ws.servidor.SQLExceptionException0;
import com.localgis.ws.servidor.ServicesStub;
import com.localgis.ws.servidor.ServicesStub.ConsultaDatosBean;
import com.localgis.ws.servidor.ServicesStub.RowBean;
 
public class Test extends javax.swing.JPanel{  
	
	public static boolean eliminarConsulta(int idConsulta) throws SQLExceptionException0{
		boolean eliminadaConsulta = false;
		
		ServicesStub customer = null;
		ServicesStub.BorrarConsuta request = null;
		ServicesStub.BorrarConsutaResponse response = null;
		try {
			// creamos el soporte
			customer = new ServicesStub();	
			// establecemos el parametro de la invocacion
			request = new ServicesStub.BorrarConsuta();
			
			request.setId(idConsulta);
			
			// invocamos al web service			
			response = customer.borrarConsuta(request);
			eliminadaConsulta = response.get_return();
			
		}
		catch (RemoteException excepcionDeInvocacion) {
			System.err.println(excepcionDeInvocacion.toString());
		}	
		return eliminadaConsulta;
	}
	
	public static void guardarConsulta(com.geopista.ui.plugin.georreferenciacionExterna.ConsultaDatosBean consultaDatosBean){
		
		ConsultaDatosBean[] wsConsultaDatosBean = new ConsultaDatosBean[1];
		wsConsultaDatosBean[0] = new ConsultaDatosBean();
		wsConsultaDatosBean[0].setNombreConsulta(consultaDatosBean.getNombreConsulta());
		wsConsultaDatosBean[0].setDescripcion(consultaDatosBean.getDescripcion());
		wsConsultaDatosBean[0].setUsuario(consultaDatosBean.getUsuario());
		wsConsultaDatosBean[0].setNombre_bbdd_ext(consultaDatosBean.getNombre_bbdd_ext());
		wsConsultaDatosBean[0].setNombre_tabla_ext(consultaDatosBean.getNombre_tabla_ext());
		wsConsultaDatosBean[0].setMetodo_georeferencia(consultaDatosBean.getMetodo_georeferencia());
		wsConsultaDatosBean[0].setTipo_geometria(consultaDatosBean.getTipo_geometria());
		wsConsultaDatosBean[0].setTabla_cruce(consultaDatosBean.getTabla_cruce());
		wsConsultaDatosBean[0].setCampo_georeferencia(consultaDatosBean.getCampo_georeferencia());
		wsConsultaDatosBean[0].setCampos_mostrar(consultaDatosBean.getCampos_mostrar());
		wsConsultaDatosBean[0].setCampo_etiqueta(consultaDatosBean.getCampo_etiqueta());
		wsConsultaDatosBean[0].setFiltro_operador(consultaDatosBean.getFiltro_operador());
		wsConsultaDatosBean[0].setFiltro_valor(consultaDatosBean.getFiltro_valor());
		if(consultaDatosBean.getPortal() == null){
			wsConsultaDatosBean[0].setPortal(null);
		}
		else{
			wsConsultaDatosBean[0].setPortal(consultaDatosBean.getPortal());
		}
		ServicesStub customer = null;
		ServicesStub.GuardarConsulta request = null;
		try {
			// creamos el soporte
			customer = new ServicesStub();	
			request = new ServicesStub.GuardarConsulta();
			
			// invocamos al web service			

			request.setConsultaDatosBean(wsConsultaDatosBean);
			customer.guardarConsulta(request);
			
		} catch (RemoteException excepcionDeInvocacion) {
			System.err.println(excepcionDeInvocacion.toString());
		} catch (SQLExceptionException0 e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	public static void actualizarConsulta(com.geopista.ui.plugin.georreferenciacionExterna.ConsultaDatosBean consultaDatosBean){
		
		ConsultaDatosBean[] wsConsultaDatosBean = new ConsultaDatosBean[1];
		wsConsultaDatosBean[0] = new ConsultaDatosBean();
		wsConsultaDatosBean[0].setId(consultaDatosBean.getId());
		wsConsultaDatosBean[0].setNombreConsulta(consultaDatosBean.getNombreConsulta());
		wsConsultaDatosBean[0].setDescripcion(consultaDatosBean.getDescripcion());
		wsConsultaDatosBean[0].setUsuario(consultaDatosBean.getUsuario());
		wsConsultaDatosBean[0].setNombre_bbdd_ext(consultaDatosBean.getNombre_bbdd_ext());
		wsConsultaDatosBean[0].setNombre_tabla_ext(consultaDatosBean.getNombre_tabla_ext());
		wsConsultaDatosBean[0].setMetodo_georeferencia(consultaDatosBean.getMetodo_georeferencia());
		wsConsultaDatosBean[0].setTipo_geometria(consultaDatosBean.getTipo_geometria());
		wsConsultaDatosBean[0].setTabla_cruce(consultaDatosBean.getTabla_cruce());
		wsConsultaDatosBean[0].setCampo_georeferencia(consultaDatosBean.getCampo_georeferencia());
		wsConsultaDatosBean[0].setCampos_mostrar(consultaDatosBean.getCampos_mostrar());
		wsConsultaDatosBean[0].setCampo_etiqueta(consultaDatosBean.getCampo_etiqueta());
		wsConsultaDatosBean[0].setFiltro_operador(consultaDatosBean.getFiltro_operador());
		wsConsultaDatosBean[0].setFiltro_valor(consultaDatosBean.getFiltro_valor());
		if(consultaDatosBean.getPortal() != null && consultaDatosBean.getPortal().toLowerCase().equals("null")){
			wsConsultaDatosBean[0].setPortal(null);
		}
		else{
			wsConsultaDatosBean[0].setPortal(consultaDatosBean.getPortal());
		}
		
		ServicesStub customer = null;
		ServicesStub.ActualizarConsulta request = null;
		try {
			// creamos el soporte
			customer = new ServicesStub();	
			request = new ServicesStub.ActualizarConsulta();
			
			// invocamos al web service			

			request.setConsultaDatosBean(wsConsultaDatosBean);
			customer.actualizarConsulta(request);
			
		} catch (RemoteException excepcionDeInvocacion) {
			System.err.println(excepcionDeInvocacion.toString());
		} catch (SQLExceptionException0 e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}


	public static ArrayList obtenerConsultas(String usuario) throws SQLExceptionException0{
		ArrayList array=new ArrayList();		
		
		/*
		 * Utilizamos el stub generado a partir del wsdl que logran establecer
		 * la conexion con el web service proveedor.
		 */
		
		ServicesStub customer = null;			
		ServicesStub.ObtenerConsultas request = null;	
		ServicesStub.ObtenerConsultasResponse response = null;

		try {
			// creamos el soporte
			customer = new ServicesStub();	
			
			// establecemos el parametro de la invocacion
			request = new ServicesStub.ObtenerConsultas();
			
			ServiceClient serviceClient=customer._getServiceClient();
			Options options = serviceClient.getOptions();			
			options.setProperty(HTTPConstants.SO_TIMEOUT, new Integer(360000));
			options.setProperty(HTTPConstants.CONNECTION_TIMEOUT, new Integer(360000));		
			
			
			request.setUsuario(usuario);
			
			// invocamos al web service			
			response = customer.obtenerConsultas(request);
			
			
			//Metemos el resultado en un ArrayList.
			
			ConsultaDatosBean[] str= response.get_return();

			if(str != null){
				for (int i=0; i<str.length; i++){
					if(str[i] != null){
						com.geopista.ui.plugin.georreferenciacionExterna.ConsultaDatosBean consultaDatosBean = new com.geopista.ui.plugin.georreferenciacionExterna.ConsultaDatosBean();
						consultaDatosBean.setId(((ConsultaDatosBean)str[i]).getId());
						consultaDatosBean.setNombreConsulta(((ConsultaDatosBean)str[i]).getNombreConsulta());
						consultaDatosBean.setDescripcion(((ConsultaDatosBean)str[i]).getDescripcion());
						consultaDatosBean.setUsuario(((ConsultaDatosBean)str[i]).getUsuario());
						consultaDatosBean.setNombre_bbdd_ext(((ConsultaDatosBean)str[i]).getNombre_bbdd_ext());
						consultaDatosBean.setNombre_tabla_ext(((ConsultaDatosBean)str[i]).getNombre_tabla_ext());
						consultaDatosBean.setMetodo_georeferencia(((ConsultaDatosBean)str[i]).getMetodo_georeferencia());
						consultaDatosBean.setTipo_geometria(((ConsultaDatosBean)str[i]).getTipo_geometria());
						consultaDatosBean.setTabla_cruce(((ConsultaDatosBean)str[i]).getTabla_cruce());
						consultaDatosBean.setCampo_georeferencia(((ConsultaDatosBean)str[i]).getCampo_georeferencia());
						consultaDatosBean.setCampos_mostrar(((ConsultaDatosBean)str[i]).getCampos_mostrar());
						consultaDatosBean.setCampo_etiqueta(((ConsultaDatosBean)str[i]).getCampo_etiqueta());
						consultaDatosBean.setFiltro_operador(((ConsultaDatosBean)str[i]).getFiltro_operador());
						consultaDatosBean.setFiltro_valor(((ConsultaDatosBean)str[i]).getFiltro_valor());
						if(((ConsultaDatosBean)str[i]).getFiltro_valor().toLowerCase().equals("null")){
							consultaDatosBean.setPortal(null);
						}
						else{
							consultaDatosBean.setPortal(consultaDatosBean.getPortal());
						}
						consultaDatosBean.setPortal(((ConsultaDatosBean)str[i]).getPortal());
						array.add(consultaDatosBean);
					}
				}
			}
			
		}
		catch (RemoteException excepcionDeInvocacion) {
			System.err.println(excepcionDeInvocacion.toString());
		}		
		return array;
				
	}
	
	public static ArrayList obtenerNombreBbdd(){
		
		ArrayList array=new ArrayList();		
		
		/*
		 * Utilizamos el stub generado a partir del wsdl que logran establecer
		 * la conexion con el web service proveedor.
		 */
		
		ServicesStub customer = null;			
		ServicesStub.NombresBbddResponse response = null;		

		try {
			// creamos el soporte
			customer = new ServicesStub();	
			
			// invocamos al web service			
			response = customer.nombresBbdd();					
			
			//Metemos el resultado en un ArrayList.
			
			String[] str= response.get_return();
			for (int h=0;h<str.length;h++){
				String s=str[h];
				if (s!=null){
					array.add(s);
				}				
			}		
		} catch (RemoteException excepcionDeInvocacion) {
			System.err.println(excepcionDeInvocacion.toString());
		}		
		return array;
	}
	
	public static Hashtable obtenerTablasBbdd(String tipoBbdd) throws SQLExceptionException0{
		
		Hashtable hash=new Hashtable();		
		
		/*
		 * Utilizamos el stub generado a partir del wsdl que logran establecer
		 * la conexion con el web service proveedor.
		 */
		
		ServicesStub customer = null;		
		ServicesStub.ObtenerTablas request = null;
		ServicesStub.ObtenerTablasResponse response = null;				

		try {
			// creamos el soporte
			customer = new ServicesStub();	
			request = new ServicesStub.ObtenerTablas();
			
			// establecemos el parametro de la invocacion

			request.setBbdd(tipoBbdd);
			
			// invocamos al web service			
			response = customer.obtenerTablas(request);
		
			//Metemos el resultado en un Hashtable.
			
			String[] strh=response.get_return();			
			for (int h=0;h<strh.length;h++){
				String s=strh[h];
				if (s!=null){
					hash.put(h,s);
				}				
			}						
		} catch (RemoteException excepcionDeInvocacion) {
			System.err.println(excepcionDeInvocacion.toString());
		}
		return hash;
	}
	
	public static Hashtable obtenerTablasBBDDLocalGis() throws SQLExceptionException0{
			
		Hashtable hash=new Hashtable();		
		
		/*
		 * Utilizamos el stub generado a partir del wsdl que logran establecer
		 * la conexion con el web service proveedor.
		 */
		
		ServicesStub customer = null;		
		//ServicesStub.ObtenerTablasBBDDLocalGisResponse request = null;
		ServicesStub.ObtenerTablasBBDDLocalGisResponse response = null;				

		try {
			// creamos el soporte
			customer = new ServicesStub();	
			//request = new ServicesStub.ObtenerTablas();
			
			// establecemos el parametro de la invocacion

			//request.setBbdd(tipoBbdd);
			
			// invocamos al web service			
			response = customer.obtenerTablasBBDDLocalGis();
		
			//Metemos el resultado en un Hashtable.
			
			String[] strh=response.get_return();			
			for (int h=0;h<strh.length;h++){
				String s=strh[h];
				if (s!=null){
					hash.put(h,s);
				}				
			}						
		} catch (RemoteException excepcionDeInvocacion) {
			System.err.println(excepcionDeInvocacion.toString());
		}
		return hash;
	}
	
	
	
	public static Hashtable obtenerColumnasBbdd(String tipoBbdd, String nombreTabla) throws SQLExceptionException0{
		
		Hashtable hash=new Hashtable();	
		
		/*
		 * Utilizamos el stub generado a partir del wsdl que logran establecer
		 * la conexion con el web service proveedor.
		 */
		
		ServicesStub customer = null;		
		ServicesStub.ObtenerColumnas request = null;		
		ServicesStub.ObtenerColumnasResponse response = null;				

		try {
			// creamos el soporte
			customer = new ServicesStub();	
			request = new ServicesStub.ObtenerColumnas();			
			
			// establecemos el parametro de la invocacion
			request.setBbdd(tipoBbdd);
			request.setTabla(nombreTabla);
			
			// invocamos al web service			
			response = customer.obtenerColumnas(request);
		
			//Metemos el resultado en un Hashtable.
			
			String[] strh=response.get_return();
			
			if(strh!=null){
				for (int h=0;h<=strh.length-2;h=h+2){		
					if(strh[h]!=null){
						hash.put(strh[h],strh[h+1]);
					}			
				}
			}						
		} catch (RemoteException excepcionDeInvocacion) {
			System.err.println(excepcionDeInvocacion.toString());
		}
		return hash;
	}

	public static int obtenerTotalFilasBbdd(String tipoBbdd, String nombreTabla, String camposRef, 
			Hashtable camposRef_eleg) throws SQLExceptionException0{
		int tamanioCampos = 0;
		if(camposRef_eleg.size() == 1){
			tamanioCampos = camposRef_eleg.size() +1;
		}
		else{
			tamanioCampos = camposRef_eleg.size();
		}
		String[] campos_elegidos=new String[tamanioCampos];		
		int valor=0;
				
		int cont=0;	
		
		//Sacamos los valores de los hashtables
		
		Set indices = camposRef_eleg.keySet();
		Iterator listaCamposEleg = indices.iterator();		
		while (listaCamposEleg.hasNext()){
			String key = listaCamposEleg.next().toString();
			campos_elegidos[cont]=key;
			cont++;
			if(indices.size() == 1){
				// se ha seleccionado el mismo campo para la x y la y
				campos_elegidos[cont]=key;
			}
		}	
		
		
		
		/*
		 * Utilizamos el stub generado a partir del wsdl que logran establecer
		 * la conexion con el web service proveedor.
		 */
		
		ServicesStub customer = null;
		ServicesStub.ObtenerTotalFilas request = null;		
		ServicesStub.ObtenerTotalFilasResponse response = null;
		
		try {
			// creamos el soporte
			customer = new ServicesStub();
			request = new ServicesStub.ObtenerTotalFilas();
			
			// establecemos el parametro de la invocacion
			request.setBbdd(tipoBbdd);
			request.setTabla(nombreTabla);
			request.setCampos_ref(camposRef);
			request.setCampo_eleg(campos_elegidos);	
						
			// invocamos al web service				
			
			response = customer.obtenerTotalFilas(request);
		
			//Metemos el resultado en un RowBean[].
			
			valor=response.get_return();			
					
			
		} catch (RemoteException excepcionDeInvocacion) {
			System.err.println(excepcionDeInvocacion.toString());
		}
		return valor;
	}
	
	public static RowBean[] obtenerDatosBbdd(String tipoBbdd, String nombreTabla, Hashtable campos, String camposRef, 
			Hashtable camposRef_eleg, int inicio, int fin, String tipoGeometria, String tablaCruceGeometria,
			int  iEPSGCode, ArrayList campoElegidosCoordeXY, ArrayList lstFiltros, String portal) throws SQLExceptionException0{
		
		String[] nombre_campo=new String[campos.size()-1];
		String[] tipo_campo=new String[campos.size()-1];
		String[] campos_elegidos=new String[camposRef_eleg.size()];		
		com.localgis.ws.servidor.ServicesStub.FiltroData[] filtros = new com.localgis.ws.servidor.ServicesStub.FiltroData[lstFiltros.size()];
		RowBean[] strh=null;
				
		int cont=0,cont1=0,cont2=0;	
		
		//Sacamos los valores de los hashtables
		Set valores = campos.keySet();
		Iterator listaCampos = valores.iterator();

		while (listaCampos.hasNext()){
			String key = listaCampos.next().toString();
			if(!key.equals("GEOMETRY")){
				nombre_campo[cont]=key;
				cont++;
			}		
		}
		
		
		
		Set valores1 = campos.keySet();
		Iterator listaCampos1 = valores1.iterator();
		while (listaCampos1.hasNext()){
			String key1 = listaCampos1.next().toString();
			if(!key1.equals("GEOMETRY")){
				tipo_campo[cont2]=campos.get(key1).toString();
				cont2++;
			}			
		}		
		
		if(camposRef.equals("Coordenadas X,Y")){
			for(int a=0; a<campoElegidosCoordeXY.size(); a++){
				Hashtable campoRefElegido = (Hashtable)campoElegidosCoordeXY.get(a);
				Set indices = campoRefElegido.keySet();
				Iterator listaCamposEleg = indices.iterator();		
				while (listaCamposEleg.hasNext()){
					String key = listaCamposEleg.next().toString();
					campos_elegidos[cont1]=key;
					cont1++;
				}	
				
			}
		}
		else{
			Set indices = camposRef_eleg.keySet();
			Iterator listaCamposEleg = indices.iterator();		
			while (listaCamposEleg.hasNext()){
				String key = listaCamposEleg.next().toString();
				campos_elegidos[cont1]=key;
				cont1++;
			}	
		}
		
		int indexFiltro = 0;
		if(!lstFiltros.isEmpty()){
			for(int j=0; j<lstFiltros.size();j++){
				FiltroData filtroDataLocal= (FiltroData)lstFiltros.get(j);
				com.localgis.ws.servidor.ServicesStub.FiltroData filtroData = new com.localgis.ws.servidor.ServicesStub.FiltroData();
				if(filtroDataLocal != null){
					filtroData.setCampo(filtroDataLocal.getCampo());
					filtroData.setOperador(filtroDataLocal.getOperador());
					filtroData.setTipoCampo(filtroDataLocal.getTipoCampo());
					filtroData.setValor(filtroDataLocal.getValor());
					filtros[indexFiltro] = filtroData;
					indexFiltro ++;
				}
				/*else{
					filtros[j] = null;
				}*/
				
			}
		}
		
		
		/*
		 * Utilizamos el stub generado a partir del wsdl que logran establecer
		 * la conexion con el web service proveedor.
		 */
		
		ServicesStub customer = null;
		ServicesStub.ObtenerDatos request = null;		
		ServicesStub.ObtenerDatosResponse response = null;
		
		try {
			// creamos el soporte
			customer = new ServicesStub();
			request = new ServicesStub.ObtenerDatos();
			
			ServiceClient serviceClient=customer._getServiceClient();
			Options options = serviceClient.getOptions();			
			options.setProperty(HTTPConstants.SO_TIMEOUT, new Integer(360000));
			options.setProperty(HTTPConstants.CONNECTION_TIMEOUT, new Integer(360000));		
			
			// establecemos el parametro de la invocacion
			request.setBbdd(tipoBbdd);
			request.setTabla(nombreTabla);
			request.setNombre_campos(nombre_campo);
			request.setTipo_campos(tipo_campo);
			request.setCampos_ref(camposRef);
			request.setCampo_eleg(campos_elegidos);
			request.setValorInicial(inicio);
			request.setValorFinal(fin);
			request.setTipoGeometria(tipoGeometria);
			request.setTablaCruceGeometria(tablaCruceGeometria);
			request.setIEPSGCode(iEPSGCode);	
			request.setLstFiltro(filtros);
			request.setPortal(portal);
			// invocamos al web service				
			
			response = customer.obtenerDatos(request);
		
			//Metemos el resultado en un RowBean[].
			
			strh=response.get_return();			
					
			
		} catch (RemoteException excepcionDeInvocacion) {
			System.err.println(excepcionDeInvocacion.toString());
		}
		return strh;
	}	
}
