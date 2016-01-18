package com.geopista.server.administradorCartografia;

import com.geopista.feature.GeopistaFeature;
import com.geopista.feature.GeopistaSchema;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.io.WKTReader;
import java.io.Serializable;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;

/** Datos de un feature manejados por el interfaz del Administrador de Cartografia */
public class ACFeature implements Serializable, IACFeature{
    private HashMap attributes;
    private String geometry;
    
    private String error;
    
    private transient org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ACFeature.class);

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACFeature#getGeometry()
	 */
    @Override
	public String getGeometry() {
        return geometry;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACFeature#setGeometry(java.lang.String)
	 */
    @Override
	public void setGeometry(String geometry) {
        this.geometry = geometry;
    }

    public ACFeature(){
        this.attributes=new HashMap();
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACFeature#getAttributes()
	 */
    @Override
	public HashMap getAttributes(){
        return attributes;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACFeature#setAttributes(java.util.HashMap)
	 */
    @Override
	public void setAttributes(HashMap attributes){
        this.attributes = attributes;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACFeature#setAttribute(java.lang.String, java.io.Serializable)
	 */
    @Override
	public void setAttribute(String sKey, Serializable oValue){
        this.attributes.put(sKey,oValue);
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACFeature#getAttribute(java.lang.String)
	 */
    @Override
	public Object getAttribute(String sKey){
        return this.attributes.get(sKey);
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACFeature#findID(com.geopista.server.administradorCartografia.ACLayer)
	 */
	public int findID(ACLayer lSchema){
        int iRet=-1;
        if (this.attributes!=null){
            ACAttribute attID=lSchema.findID();
            if (attID!=null)
                iRet=((Number)this.attributes.get(attID.getName())).intValue();
        }
        return iRet;
    }


    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACFeature#convert(com.geopista.feature.GeopistaSchema)
	 */
    @Override
	public GeopistaFeature convert(GeopistaSchema schema) throws NoIDException{
        GeopistaFeature gfRet =null;
        com.vividsolutions.jts.geom.Geometry geom=null;
        WKTReader wktReader=new WKTReader();
        
        String atributoBusqueda=null;
        try{
        	if (getGeometry()!=null) {
        		StringReader stringReader = new StringReader(getGeometry());
        		FeatureCollection fc = null;
        		try {
        			fc=wktReader.read(stringReader);
        		}catch (Exception e) {
            		stringReader.close();
				}
        		Feature jumpFeature=(Feature)fc.iterator().next();
        		gfRet=new GeopistaFeature(schema);
        		gfRet.setLockedFeature(false);
        		gfRet.setFireDirtyEvents(false);
        		for (Iterator enumerationElement =attributes.keySet().iterator(); enumerationElement.hasNext(); ){
        			String sKey=(String)enumerationElement.next();
        			atributoBusqueda=sKey;
        			Object oValue=attributes.get(sKey);
        			gfRet.setAttribute(sKey,oValue);
        			if (schema.getColumnByAttribute(sKey).getName().equalsIgnoreCase("ID")){
        				if (oValue!=null)
        					gfRet.setSystemId(oValue.toString());
        			}
        		}

        		if (gfRet.getSystemId()==null)
        			throw new NoIDException();
        		
        		geom=jumpFeature.getGeometry();
        		gfRet.setGeometry(jumpFeature.getGeometry());
        		gfRet.setFireDirtyEvents(true);
        		gfRet.setLockedFeature(true);
        	}
        	else{
        		
        		gfRet=new GeopistaFeature(schema);
        		if (gfRet.setGeometryEmpty()){
        			gfRet.setLockedFeature(false);
        			gfRet.setFireDirtyEvents(false);
        			for (Iterator enumerationElement =attributes.keySet().iterator(); enumerationElement.hasNext(); ){
        				String sKey=(String)enumerationElement.next();
        				Object oValue=attributes.get(sKey);
        				gfRet.setAttribute(sKey,oValue);
        				if (schema.getColumnByAttribute(sKey).getName().equalsIgnoreCase("ID")){
        					if (oValue!=null)
        						gfRet.setSystemId(oValue.toString());
        				}
        			}

        			if (gfRet.getSystemId()==null)
        				throw new NoIDException();

        			gfRet.setFireDirtyEvents(true);
        			gfRet.setLockedFeature(true);
        		}
        		else{
        			return null;
        		}
        	}
        }catch (Exception e){
            e.printStackTrace();  
            if (logger!=null)
            	logger.error("Atributo busqueda:"+atributoBusqueda);
            
            error="Atributo busqueda:"+atributoBusqueda+" ";
            
            try {
				if (gfRet!=null)
					error+=gfRet.getErrorGeometria(geom);
			} catch (Exception e1) {}
            gfRet=null;
        }
        return gfRet;
    }
        
     /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACFeature#getError()
	 */
    @Override
	public String getError(){
    	 return error;
     }
    
}
