/*
 * Created on 22-jul-2004
 *
 */
package com.geopista.style.sld.model.impl.actions;

import java.sql.Connection;
import java.util.List;

import es.enxenio.util.exceptions.InternalErrorException;

/**
 * @author enxenio s.l.
 *
 */
public class InsertUserStylesAction {

	private static String sldNS = "http://www.opengis.net/sld";

	private String _layerName;
	private List _userStyles;
	
	public InsertUserStylesAction(String layerName,List userStyles) {
		
		_layerName = layerName;
		_userStyles = userStyles;
	}

	/* (non-Javadoc)
	 * @see es.enxenio.util.sql.PlainAction#execute(java.sql.Connection)
	 */
	public Object execute(Connection connection) throws InternalErrorException {
		try {
			//TODO: This method will update the database to reflect the changes in the SLD style
			//TODO: It is not yet implemented!			
		} catch (Exception e) {
			throw new InternalErrorException(e);
		}
		return null;
	}
}
