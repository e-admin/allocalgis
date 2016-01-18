/**
 * ActionContext.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.database;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.Map;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeIterator;

public class ActionContext extends Context {
    /**
     * nombre del nodo raíz del contexto de atributos
     */
    private final static String ATT_CTX_ROOT = "acciones";
    /**
     * nombre de cada uno de los nodos que contendrán información de atributos
     */
    public final static String NAME_NODE = "accion";
    
    /**
     * en este mapa se almacenarán los nombres de atributos y los valores de los mismos, 
     */
    private Map acciones = null;
    /**
     * @param xml
     */
    public ActionContext(String xml) {
        super(xml, ATT_CTX_ROOT);
    }
    
    
    public String getAttribute(String attribute){
        getProperties();
        return (acciones == null) ? null : (String)acciones.get(attribute);
    }

    
	public Map getProperties(){
	    if (facade == null)
	        return new LinkedHashMap();
	    if (acciones == null){
		    acciones = new LinkedHashMap();
		    NodeIterator it = facade.getNodeIterator(root+"/"+NAME_NODE);
		    Node node = it.nextNode();
		    if (node != null)
			    do{
			        acciones.put(
			        		getNodeAttribute(node, "name"),
			        		XMLFacade.getNodeValue(node));
			        node = it.nextNode();
			    }while(node != null);
	    }

	    return acciones; 
	}    
    
	private static String getNodeAttribute(Node node, String attribute)
	{
		NamedNodeMap attrs = node.getAttributes();
		for (int i = 0; i < attrs.getLength(); i++)
		{
			Node attr = attrs.item(i);
			String name = attr.getNodeName();
			String value = attr.getNodeValue();
			if (name.equals(attribute))
			{
				return value;
			}
		}
		return null;
	}
	
    protected int getInt(String property) {
        // TODO Auto-generated method stub
        return 0;
    }

    protected Date getDate(String property) {
        // TODO Auto-generated method stub
        return null;
    }

    protected Timestamp getTimestamp(String property) {
        // TODO Auto-generated method stub
        return null;
    }

    protected String get(String property) {
        // TODO Auto-generated method stub
        return null;
    }	
}
