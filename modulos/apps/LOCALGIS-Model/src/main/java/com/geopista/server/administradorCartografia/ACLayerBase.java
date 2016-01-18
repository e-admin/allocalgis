/**
 * ACLayerBase.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.administradorCartografia;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Iterator;

import com.geopista.feature.Column;
import com.geopista.feature.GeopistaSchema;
import com.geopista.feature.Table;
import com.geopista.model.IGeopistaLayer;
import com.vividsolutions.jump.feature.AttributeType;



/** Datos de una capa para el interfaz con el administrador de cartografia */
public class ACLayerBase implements Serializable,IACLayer{
   

	private static final long	serialVersionUID	= 5351002207616871739L;
    protected int id_layer;
    protected String name=null;
    protected Hashtable attributes;
    protected String geometryAttribute;
    protected String styleXML;
    protected String styleName;
    protected String styleId;
    protected String systemName;
    protected String extendedForm;
    protected long lACL;
    protected transient String selectQuery;
    protected transient String updateQuery;
    protected transient String insertQuery;
    protected boolean isActive;
    protected boolean isVisible=true;
    protected boolean isEditable;
    protected boolean isDinamica;
    protected boolean isLocal=false;
    protected int positionOnMap=-1;
    protected long revisionActual = -1;
    protected long ultimaRevision = -1;
    protected boolean isVersionable=false;
    protected int idUsuario = -1;
	protected transient String deleteQuery;

   
   
    public ACLayerBase(int id,String name,String systemName,String selectQuery){
        this.id_layer=id;
        this.name=name;
        this.systemName=systemName;
        attributes=new Hashtable();
        this.selectQuery=selectQuery;
    }
	
	
   
	public int getPositionOnMap() {
        return positionOnMap;
    }

   
	public void setPositionOnMap(int positionOnMap) {
        this.positionOnMap = positionOnMap;
    }
	
	public boolean isEditable() {
        return isEditable;
    }

	public void setEditable(boolean editable) {
        isEditable = editable;
    }


	public boolean isVisible() {
        return isVisible;
    }


	public void setVisible(boolean visible) {
    	isVisible = visible;
    }
    
	public boolean isActive() {
        return isActive;
    }


	public void setActive(boolean active) {
        isActive = active;
    }
   

	public boolean isStyleLocal() {
        return isLocal;
    }


	public void setStyleLocal(boolean isLocal) {
    	isLocal = isLocal;
    }

	
	public String getStyleId() {
        return styleId;
    }


	public void setStyleId(String styleId) {
        this.styleId = styleId;
    }
    
	

	public boolean isDinamica() {
        return isDinamica;
    }
	

	public void setDinamica(boolean dinamica) {
    	isDinamica = dinamica;
    }
	
	
	public String getSelectQuery() {
        return selectQuery;
    }

	public void setSelectQuery(String selectQuery) {
        this.selectQuery = selectQuery;
    }

	
	public long getACL() {
        return lACL;
    }


	public void setACL(long lACL) {
        this.lACL = lACL;
    }

	
	public String getSystemName() {
        return systemName;
    }


	public void setSystemName(String systemName) {
        this.systemName = systemName;
    }


	public String getStyleXML() {
        return styleXML;
    }


	public void setStyleXML(String styleXML) {
        this.styleXML = styleXML;
    }


	public String getStyleName() {
        return styleName;
    }

	public void setStyleName(String styleName) {
        this.styleName = styleName;
    }
	
	public String getGeometryAttribute() {
        return geometryAttribute;
    }

	public void setGeometryAttribute(String geometryAttribute) {
        this.geometryAttribute = geometryAttribute;
    }

  	
	public int getId_layer() {
        return id_layer;
    }

	public void setId_layer(int id_layer) {
        this.id_layer = id_layer;
    }
	
	public String getName() {
        return name;
    }

	public void setName(String name) {
        this.name = name;
    }
	
	
	
	public Hashtable getAttributes() {
        return attributes;
    }

	public void setAttributes(Hashtable attributes) {
        this.attributes = attributes;
    }
	
	
	public void addAttribute(ACAttribute att){
        this.attributes.put(new Integer(att.getPosition()),att);
    }

	public Object getAttribute(String sName){
        return this.attributes.get(sName);
    }

	
	public String getExtendedForm() {
        return extendedForm;
    }

	public void setExtendedForm(String extendedForm) {
        this.extendedForm = extendedForm;
    }
	
	public GeopistaSchema buildSchema(String sLocale){
        GeopistaSchema gsRet=new GeopistaSchema();
        //gsRet.addAttribute("GEOMETRY",AttributeType.GEOMETRY);
        Hashtable htTables=new Hashtable();
        Hashtable htColumns=new Hashtable();
        Hashtable htDomains=new Hashtable();
        for (int i=1;i<=this.attributes.size();i++){
            // Obtenemos los objetos AC*
            ACAttribute acAtt=(ACAttribute)this.attributes.get(new Integer(i));
            //Cuando no esta bien definida una capa puede que no exita el atributo Integer(i)
            if (acAtt==null)
            {
                System.out.println("[ACLayer] Posible capa mal definida"+ i );
                continue;
            }
            IACColumn acCol=acAtt.getColumn();
            IACTable acTab=acCol.getTable();
            IACDomain acDom=acCol.getDomain();
            // Buscaremos si la tabla, la columna y el dominio estan
            // ya instanciados...
            Table tab=(Table)htTables.get(acTab.getName());
            if (tab==null){
                tab=acTab.convert();
                htTables.put(acTab.getName(),tab);
            }
            com.geopista.feature.Domain dom=null;
            if (acDom!=null){
                dom=(com.geopista.feature.Domain)htDomains.get(acDom.getName());
                if (dom==null){
                    dom=acDom.convert(sLocale);
                    htDomains.put(acDom.getName(),dom);
                }
            }
            Column col=(Column)htColumns.get(acCol.getName());
            if (col==null){
                col=acCol.convert(tab,dom);
                htColumns.put(acCol.getName(),col);
            }
            // Instanciamos por fin el atributo
            AttributeType attType=null;
            switch(acCol.getType()){
                case TYPE_GEOMETRY:
                    attType=AttributeType.GEOMETRY;
                    break;
                case TYPE_NUMERIC:
                    attType=acCol.getScale()==0?AttributeType.INTEGER:AttributeType.DOUBLE;
                    break;
                case TYPE_STRING:
                    attType=AttributeType.STRING;
                    break;
                case TYPE_DATE:
                    attType=AttributeType.DATE;
                    break;
                case TYPE_BOOLEAN:
                    attType=AttributeType.INTEGER;
                    //attType=AttributeType.BOOLEAN
                    break;
                default:
            }
            gsRet.addAttribute(acAtt.getName(),attType,col,acAtt.isEditable());
            //gsRet.setAttributeColumn(iAttributeIndex++,col);
        }
        return gsRet;
    }
	
	public String findPrimaryTable(){
        String sRet=null;
        for (Iterator it=getAttributes().values().iterator();it.hasNext();){
            ACAttribute att=(ACAttribute)it.next();
            IACColumn col=att.getColumn();
            if (col.getName().equals("GEOMETRY")){
                sRet=((IACTable)col.getTable()).getName();
                break;
            }
        }
        return sRet;
    }

    
	public ACAttribute findID(){
        ACAttribute attRet=null;
        for (int i=1;i<=attributes.size();i++){
            ACAttribute att=(ACAttribute)attributes.get(new Integer(i));
            IACColumn col=att.getColumn();
            if (col.getName().toUpperCase().equals("ID")){
                attRet=att;
                break;
            }
        }
        return attRet;
    }

    public static String findPrimaryTable(IGeopistaLayer layer){
        String sRet=null;
        try{
            GeopistaSchema schema=(GeopistaSchema)layer.getFeatureCollectionWrapper().getFeatureSchema();
            Column col=schema.getColumnByAttribute(schema.getGeometryIndex());
            sRet=col.getTable().getName();
        }catch(NullPointerException npe){
            System.err.println("GeopistaSchema Error");
        }
        return sRet;
    }
	

	public long getRevisionActual() {
		return revisionActual;
	}

	public void setRevisionActual(long revisionActual) {
		this.revisionActual = revisionActual;
	}

	
	public String getUpdateQuery() {
		return updateQuery;
	}

	public void setUpdateQuery(String updateQuery) {
		this.updateQuery = updateQuery;
	}

	public String getInsertQuery() {
		return insertQuery;
	}

	public void setInsertQuery(String insertQuery) {
		this.insertQuery = insertQuery;
	}
	
	public boolean isVersionable() {
		return isVersionable;
	}


	public void setVersionable(boolean isVersionable) {
		this.isVersionable = isVersionable;
	}

	
	public long getUltimaRevision() {
		return ultimaRevision;
	}


	public void setUltimaRevision(long ultimaRevision) {
		this.ultimaRevision = ultimaRevision;
	}
	
	public int getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

	
	public void setDeleteQuery(String deleteQuery) {
		this.deleteQuery=deleteQuery;		
	}
	

	public String getDeleteQuery() {
		return deleteQuery;		
	}



	
}
