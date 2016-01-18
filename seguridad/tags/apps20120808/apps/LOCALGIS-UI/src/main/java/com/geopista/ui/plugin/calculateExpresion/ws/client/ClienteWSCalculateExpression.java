package com.geopista.ui.plugin.calculateExpresion.ws.client;

import java.rmi.RemoteException;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.ui.plugin.calculateExpresion.beans.TestCalculatorExpressionBean;
import com.geopista.ui.plugin.calculateExpresion.ws.server.ServicesStub;

public class ClienteWSCalculateExpression {

	private static final Log	logger	= LogFactory.getLog(ClienteWSCalculateExpression.class);
	private final long WSTIMEOUT = 86400000; //1 dia
	public ArrayList obtenerTablesNames(){
	
		ArrayList lstTables = new ArrayList();
		String[] str = null;
		 ServicesStub customer = null;
	
		ServicesStub.GetTablesNamesResponse response = null;
		
		try {
			customer = new ServicesStub();
			// Incrementamos el timeout a 2mins ya que la carga de datos puede tardarse
			customer._getServiceClient().getOptions().setTimeOutInMilliSeconds(WSTIMEOUT);
			// Invocamos al web service
			response = customer.getTablesNames();
			
			str = response.get_return();
			if (str != null) {
				
				for (int i = 0; i < str.length; i++) {
					lstTables.add(str[i]);
				}
			}
			
			
		} catch (RemoteException excepcionDeInvocacion) {
			System.err.println(excepcionDeInvocacion.toString());
			logger.error("Exception al obtener la lista de los nombres de tablas");
			logger.error(excepcionDeInvocacion.getMessage());
		}
		
		return lstTables;
	}
	
	
	public ArrayList obtenerColumnsNames(String nombreTabla){
		
		ArrayList lstColumnNames = new ArrayList();;
		
		String[] str = null;
		ServicesStub customer = null;
		ServicesStub.GetColumnNames request = null;
		ServicesStub.GetColumnNamesResponse response  = null;
		
		try {
			
			customer = new ServicesStub();
			// Incrementamos el timeout a 2mins ya que la carga de datos puede tardarse
			customer._getServiceClient().getOptions().setTimeOutInMilliSeconds(WSTIMEOUT);
			// establecemos el parametro de la invocacion
			
			request = new ServicesStub.GetColumnNames();
			request.setTableName(nombreTabla);
			
			// invocamos al web service
			response = customer.getColumnNames(request);
			
			str = response.get_return();

			if (str != null) {
				
				for (int i = 0; i < str.length; i++) {
					lstColumnNames.add(str[i]);
				}
			}
				
			
		} catch (RemoteException excepcionDeInvocacion) {
			System.err.println(excepcionDeInvocacion.toString());
			logger.error("Exception al obtener la lista las columnas de la tabla");
			logger.error(excepcionDeInvocacion.getMessage());
		}
		
		return lstColumnNames;
	}
	
	
	public String validateExpression(String destino, String expression, String from, String where){
		
		com.geopista.ui.plugin.calculateExpresion.ws.server.ServicesStub.TestCalculatorExpressionBean[] str = null;
		String message = null;
		ServicesStub customer = null;
		ServicesStub.ValidateExpresion request = null;
		ServicesStub.ValidateExpresionResponse response  = null;
		try {
			customer = new ServicesStub();
			// Incrementamos el timeout a 2mins ya que la carga de datos puede tardarse
			customer._getServiceClient().getOptions().setTimeOutInMilliSeconds(WSTIMEOUT);
			// establecemos el parametro de la invocacion
			request = new ServicesStub.ValidateExpresion();
			request.setDestino(destino);
			request.setExpression(expression);
			request.setFrom(from);
			request.setWhere(where);
			// invocamos al web service
			response = customer.validateExpresion(request);
			
			message = response.get_return();

		} catch (RemoteException excepcionDeInvocacion) {
			System.err.println(excepcionDeInvocacion.toString());
			logger.error("Exception al obtener la lista de resultados");
			logger.error(excepcionDeInvocacion.getMessage());
		}
		
		
		return message;
		
	}
	
	
	public ArrayList testDataExpression(String destino, String expression, String from, String where){
		
		com.geopista.ui.plugin.calculateExpresion.ws.server.ServicesStub.TestCalculatorExpressionBean[] str = null;
		ArrayList lstResultado = new ArrayList();
		ServicesStub customer = null;
		ServicesStub.TestDataExpresion request = null;
		ServicesStub.TestDataExpresionResponse response  = null;
		try {
			customer = new ServicesStub();
			// Incrementamos el timeout a 2mins ya que la carga de datos puede tardarse
			customer._getServiceClient().getOptions().setTimeOutInMilliSeconds(WSTIMEOUT);
			// establecemos el parametro de la invocacion
			request = new ServicesStub.TestDataExpresion();
			request.setDestino(destino);
			request.setExpression(expression);
			request.setFrom(from);
			request.setWhere(where);
			// invocamos al web service
			response = customer.testDataExpresion(request);
			
			str = response.get_return();

			if (str != null) {
				
				for (int i = 0; i < str.length; i++) {
					if(str[i] != null){
						TestCalculatorExpressionBean tceb = new TestCalculatorExpressionBean();
						tceb.setDestino(((com.geopista.ui.plugin.calculateExpresion.ws.server.ServicesStub.TestCalculatorExpressionBean)str[i]).getDestino());
						tceb.setResultado(((com.geopista.ui.plugin.calculateExpresion.ws.server.ServicesStub.TestCalculatorExpressionBean)str[i]).getResultado());
						lstResultado.add(tceb);
					}
				}
			}
			
		} catch (RemoteException excepcionDeInvocacion) {
			System.err.println(excepcionDeInvocacion.toString());
			logger.error("Exception al obtener la lista de resultados");
			logger.error(excepcionDeInvocacion.getMessage());
		}
		
		
		return lstResultado;
		
	}
	
	
	public ArrayList updateDataExpression(String destino, String expression, String from, String where){
		
		Boolean str = null;
		ArrayList lstResultado = new ArrayList();
		ServicesStub customer = null;
		ServicesStub.UpdateDataExpresion request = null;
		ServicesStub.UpdateDataExpresionResponse response  = null;
		try {
			customer = new ServicesStub();
			// Incrementamos el timeout a 2mins ya que la carga de datos puede tardarse
			customer._getServiceClient().getOptions().setTimeOutInMilliSeconds(WSTIMEOUT);
			// establecemos el parametro de la invocacion
			request = new ServicesStub.UpdateDataExpresion();
			request.setDestino(destino);
			request.setExpression(expression);
			request.setFrom(from);
			request.setWhere(where);
			// invocamos al web service
			response = customer.updateDataExpresion(request);
			
			str = response.get_return();

			
			
		} catch (RemoteException excepcionDeInvocacion) {
			System.err.println(excepcionDeInvocacion.toString());
			logger.error("Exception al obtener la lista de resultados");
			logger.error(excepcionDeInvocacion.getMessage());
		}
		
		
		return lstResultado;
		
	}
	
	
	public Boolean validarFicheroExpresion(String txtFichero){
		
		Boolean str = null;
		ArrayList lstResultado = new ArrayList();
		ServicesStub customer = null;
		ServicesStub.ValidarFicheroExpresion request = null;
		ServicesStub.ValidarFicheroExpresionResponse response  = null;
		try {
			customer = new ServicesStub();
			// Incrementamos el timeout a 2mins ya que la carga de datos puede tardarse
			customer._getServiceClient().getOptions().setTimeOutInMilliSeconds(WSTIMEOUT);
			// establecemos el parametro de la invocacion
			request = new ServicesStub.ValidarFicheroExpresion();
			request.setTxtFichero(txtFichero);
	
			// invocamos al web service
			response = customer.validarFicheroExpresion(request);
			
			str = response.get_return();

			
			
		} catch (RemoteException excepcionDeInvocacion) {
			System.err.println(excepcionDeInvocacion.toString());
			logger.error("Exception al validar el fichero xml");
			logger.error(excepcionDeInvocacion.getMessage());
		}
		
		
		return str;
		
	}
}


