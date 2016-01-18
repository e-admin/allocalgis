/**
 * 
 */
package com.geopista.style.sld.ui.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JComboBox;

import com.geopista.feature.Domain;
import com.geopista.feature.GeopistaFeature;
import com.geopista.feature.GeopistaSchema;
import com.geopista.feature.TreeDomain;

/**
 * Editor y muestra de JComboBox como celda de JTable
 * @author migueldedios
 */
public class JComboEditorRenderTableDomain {
	
    /**
     * valores
     */
	HashMap valores;
    /**
     * Dominio en el que estan el key y el value
     */
	Domain parentDomain;
    /**
     * Objeto JCombo que se utiliza
     */
	JComboBox Jcombo;
	
	
	/**
     * Constructor
     * @param attName 
     * @param tdDomain 
     * @param schema 
     */
    public JComboEditorRenderTableDomain(String attName, TreeDomain tdDomain, GeopistaSchema schema)  
    {
        // Obtiene el dominio que se aplica como padre de este nivel
        // y para los valores de esta feature
    	
    	if (schema.getGeopistalayer().getFeatureCollectionWrapper().getFeatures().size()==0)
    		return;
    	GeopistaFeature feature = (GeopistaFeature)schema.getGeopistalayer().getFeatureCollectionWrapper().getFeatures().get(0);
    	
        parentDomain = tdDomain.getKeyDomainByColumn(schema.getColumnByAttribute(attName), feature);
        Vector combo = new Vector();
        valores = new HashMap();
        Domain selected = null;
        if (parentDomain != null)
        {
            combo.add(""); //Para que deje uno en blanco
            valores.put("","");
        	Iterator domainChildren = parentDomain.getChildren().iterator();

            /**
             * get current value of the attribute
             */
            String val = (feature).getString(schema.getAttributeIndex(attName));
            if (val == null)
                val = "";
            while (domainChildren.hasNext())
            {
                Domain domainChild = (Domain) domainChildren.next();
                String value = domainChild.getRepresentation();
                String code = domainChild.getPattern();
                valores.put(value, code);
                if (val.equals(code))
                    selected = domainChild;
                combo.add(domainChild);
            }
        }
        this.Jcombo = new JComboBox(combo);
    }
    
    /**
     * Devuelve el objeto combo
     */
    public JComboBox getCombo()
    {
    	return this.Jcombo;
    }
    
    /**
     * 
     * @return 
     */
    public HashMap getValues()
    {
    	return this.valores;
    }
    
}