package es.satec.localgismobile.server.projectsync.xml;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import org.apache.log4j.Logger;
import org.exolab.castor.types.Date;

import es.satec.localgismobile.server.projectsync.xml.beans.ItemXMLUpload;


public class XMLUpload {

	private static Logger logger = Logger
	.getLogger(XMLUpload.class);
	
	/**
	 * Actualizamos los valores que nos vienen del servidor para rellenar el objeto correctamente.
	 * @param expOBien
	 * @param changedAttributes
	 * @throws Exception
	 */
	protected String reflectGetMethod(Object objeto, Hashtable changedAttributes) throws Exception  {
			String strMethod = null;
			Method method = null;
			String replaceString="";
						
			Enumeration attrs = changedAttributes.keys();
			while (attrs.hasMoreElements()) {
			  String atributo = (String)attrs.nextElement();
			  strMethod  = "get" + atributo;
			  method = objeto.getClass().getMethod(strMethod, new Class[]{});
			  if(method!=null){
				try {
					Object result=method.invoke(objeto, new Object[0]);
					String resultado=(String)result;
					if (resultado!=null){
						changedAttributes.put(atributo,resultado);
						replaceString+=atributo+"=\"" + resultado+"\" ";

					}
				} catch (Exception e) {
					logger.error("Error al acceder al metodo "+objeto.getClass()+"." + strMethod, e);
				}
			  }			
			}	
			return replaceString;
			
		}
	
	/**
	 * Realiza los setters de una lista de items
	 * 
	 * @param obj
	 * @param itemList
	 * @throws Exception
	 */
	protected Hashtable reflectItems(Object objectInvoke,
			List<ItemXMLUpload> itemList) throws Exception {
		if (objectInvoke == null) {
			return null;
		}
		ItemXMLUpload itemXMLUpload = null;
		String strMethod = null;
		Method method = null;
		Hashtable changedAtributes = new Hashtable();
		try {
			for (int j = 0; j < itemList.size(); j++) {
				itemXMLUpload = itemList.get(j);
				strMethod = "set" + itemXMLUpload.getReflectMethod();
				if (itemXMLUpload.getType().toLowerCase().equals(
						ConstantsXMLUpload.TAG_ITEM)) {
					method = objectInvoke.getClass().getMethod(strMethod,
							new Class[] { String.class });
					if (method != null) {
						try {
							method.invoke(objectInvoke, itemXMLUpload
									.getValue());
						} catch (Exception e) {
							logger.error(
									"Error al acceder al metodo "
											+ objectInvoke.getClass() + "."
											+ strMethod, e);
						}
					}
				} else if (itemXMLUpload.getType().toLowerCase().equals(
						ConstantsXMLUpload.TAG_ITEMLIST)) {
					method = objectInvoke.getClass().getMethod(strMethod,
							new Class[] { List.class });
					if (method != null) {
						try {
							method.invoke(objectInvoke, itemXMLUpload
									.getSubItems());
						} catch (Exception e) {
							logger.error(
									"Error al acceder al metodo "
											+ objectInvoke.getClass() + "."
											+ strMethod, e);
						}
					}
				}
				if (itemXMLUpload.getUpdatable().toLowerCase().equals("true")) {
					changedAtributes
							.put(itemXMLUpload.getReflectMethod(), "");
				}
			}

		} catch (Exception e) {
			logger.error("Error al acceder al metodo "
					+ objectInvoke.getClass() + "." + strMethod, e);
			throw new Exception("Error al acceder al metodo "
					+ objectInvoke.getClass() + "." + strMethod + ": "
					+ e.getMessage());
		}
		return changedAtributes;

	}
	
	public static Object getRelfectionValue(String value, String typeName){	  
	      try {	 
	    	if(typeName.equals("java.lang.Integer"))
	    		return Integer.parseInt(value);
	    	else if(typeName.equals("java.lang.Double"))
	    		return Double.parseDouble(value);
	    	else if(typeName.equals("java.lang.Float"))
	    		return Float.parseFloat(value);
	    	else if(typeName.equals("java.sql.Date"))
	    		return java.sql.Date.valueOf(value);
	    	else
	    		return value;
	      } catch (NumberFormatException e){
	    	  return 0;
		}	      
	}
	

	
}
