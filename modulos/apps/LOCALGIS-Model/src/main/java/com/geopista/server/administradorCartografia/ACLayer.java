/**
 * ACLayer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.administradorCartografia;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;


import com.geopista.feature.Column;
import com.geopista.feature.GeopistaSchema;
import com.geopista.feature.Table;
import com.geopista.model.IGeopistaLayer;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.workbench.model.ILayerManager;
import com.vividsolutions.jump.workbench.ui.renderer.style.LabelStyle;
import com.vividsolutions.jump.workbench.ui.renderer.style.SquareVertexStyle;



/** Datos de una capa para el interfaz con el administrador de cartografia */
public class ACLayer implements Serializable, IACLayer{
    /**
	 * Comment for <code>serialVersionUID</code>
	 */

//	public static final int TYPE_GEOMETRY = 1;
//	public static final int TYPE_NUMERIC = 2;
//	public static final int TYPE_STRING = 3;
//	public static final int TYPE_DATE = 4;
//	public static final int TYPE_BOOLEAN = 5;

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
	
	
	public ACLayer(ACLayerBase acLayerBase){
		this.lACL=acLayerBase.getACL();
		this.isActive=acLayerBase.isActive();
		this.attributes=acLayerBase.getAttributes();
		this.id_layer=acLayerBase.getId_layer();
		this.name=acLayerBase.getName();
		this.systemName=acLayerBase.getSystemName();
        this.selectQuery=acLayerBase.getSelectQuery();
        this.positionOnMap=acLayerBase.getPositionOnMap();
        this.isDinamica=acLayerBase.isDinamica();
        this.styleId=acLayerBase.getStyleId();
        this.isEditable=acLayerBase.isEditable();
        this.isVisible=acLayerBase.isVisible();
        this.styleXML=acLayerBase.getStyleXML();
        this.extendedForm=acLayerBase.getExtendedForm();
        this.isVersionable=acLayerBase.isVersionable();
        this.revisionActual=acLayerBase.getRevisionActual();
        this.ultimaRevision=acLayerBase.getUltimaRevision();
        this.styleName=acLayerBase.getStyleName();
        this.insertQuery=acLayerBase.getInsertQuery();
        this.updateQuery=acLayerBase.getUpdateQuery();
        this.deleteQuery=acLayerBase.getDeleteQuery();
        this.isLocal=acLayerBase.isStyleLocal();
        this.geometryAttribute=acLayerBase.getGeometryAttribute();
        this.idUsuario=acLayerBase.getIdUsuario();
        
	}
   
	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#getPositionOnMap()
	 */
	@Override
	public int getPositionOnMap() {
        return positionOnMap;
    }

      
	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#setPositionOnMap(int)
	 */
	@Override
	public void setPositionOnMap(int positionOnMap) {
        this.positionOnMap = positionOnMap;
    }

    
	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#isEditable()
	 */
	@Override
	public boolean isEditable() {
        return isEditable;
    }

   
    
	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#setEditable(boolean)
	 */
	@Override
	public void setEditable(boolean editable) {
        isEditable = editable;
    }

   
    
	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#isVisible()
	 */
	@Override
	public boolean isVisible() {
        return isVisible;
    }

    
    
	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible) {
    	isVisible = visible;
    }
    
    
	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#isActive()
	 */
	@Override
	public boolean isActive() {
        return isActive;
    }


    
	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#setActive(boolean)
	 */
	@Override
	public void setActive(boolean active) {
        isActive = active;
    }

    
	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#isStyleLocal()
	 */
	@Override
	public boolean isStyleLocal() {
        return isLocal;
    }


    
	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#setStyleLocal(boolean)
	 */
	@Override
	public void setStyleLocal(boolean isLocal) {
    	isLocal = isLocal;
    }


    
	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#getStyleId()
	 */
	@Override
	public String getStyleId() {
        return styleId;
    }


    
	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#setStyleId(java.lang.String)
	 */
	@Override
	public void setStyleId(String styleId) {
        this.styleId = styleId;
    }

 
    
	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#isDinamica()
	 */
	@Override
	public boolean isDinamica() {
        return isDinamica;
    }


    
	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#setDinamica(boolean)
	 */
	@Override
	public void setDinamica(boolean dinamica) {
    	isDinamica = dinamica;
    }


    
	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#getSelectQuery()
	 */
	@Override
	public String getSelectQuery() {
        return selectQuery;
    }

    
	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#setSelectQuery(java.lang.String)
	 */
	@Override
	public void setSelectQuery(String selectQuery) {
        this.selectQuery = selectQuery;
    }

    
	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#getACL()
	 */
	@Override
	public long getACL() {
        return lACL;
    }

 
    
	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#setACL(long)
	 */
	@Override
	public void setACL(long lACL) {
        this.lACL = lACL;
    }

  
    
	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#getSystemName()
	 */
	@Override
	public String getSystemName() {
        return systemName;
    }


    
	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#setSystemName(java.lang.String)
	 */
	@Override
	public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

 
    
	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#getStyleXML()
	 */
	@Override
	public String getStyleXML() {
        return styleXML;
    }


    
	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#setStyleXML(java.lang.String)
	 */
	@Override
	public void setStyleXML(String styleXML) {
        this.styleXML = styleXML;
    }


    
	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#getStyleName()
	 */
	@Override
	public String getStyleName() {
        return styleName;
    }

    
	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#setStyleName(java.lang.String)
	 */
	@Override
	public void setStyleName(String styleName) {
        this.styleName = styleName;
    }


    
	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#getGeometryAttribute()
	 */
	@Override
	public String getGeometryAttribute() {
        return geometryAttribute;
    }


    
	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#setGeometryAttribute(java.lang.String)
	 */
	@Override
	public void setGeometryAttribute(String geometryAttribute) {
        this.geometryAttribute = geometryAttribute;
    }

    public ACLayer(int id,String name,String systemName,String selectQuery){
        this.id_layer=id;
        this.name=name;
        this.systemName=systemName;
        attributes=new Hashtable();
        this.selectQuery=selectQuery;
    }


    
	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#getId_layer()
	 */
	@Override
	public int getId_layer() {
        return id_layer;
    }


    
	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#setId_layer(int)
	 */
	@Override
	public void setId_layer(int id_layer) {
        this.id_layer = id_layer;
    }


    
	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#getName()
	 */
	@Override
	public String getName() {
        return name;
    }

    
	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
        this.name = name;
    }

    
	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#getAttributes()
	 */
	@Override
	public Hashtable getAttributes() {
        return attributes;
    }


    
	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#setAttributes(java.util.Hashtable)
	 */
	@Override
	public void setAttributes(Hashtable attributes) {
        this.attributes = attributes;
    }


	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#addAttribute(com.geopista.server.administradorCartografia.ACAttribute)
	 */
	public void addAttribute(ACAttribute att){
        this.attributes.put(new Integer(att.getPosition()),att);
    }


    
	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#getAttribute(java.lang.String)
	 */
	public Object getAttribute(String sName){
        return this.attributes.get(sName);
    }


    
	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#getExtendedForm()
	 */
	@Override
	public String getExtendedForm() {
        return extendedForm;
    }


    
	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#setExtendedForm(java.lang.String)
	 */
	@Override
	public void setExtendedForm(String extendedForm) {
        this.extendedForm = extendedForm;
    }

    
	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#convert(com.vividsolutions.jump.workbench.model.LayerManager)
	 */
	/*public GeopistaLayer convert(ILayerManager layerManager){
        GeopistaLayer lRet=new GeopistaLayer();
        lRet.setId_LayerDataBase(this.id_layer);
        lRet.setSystemId(this.systemName);
        lRet.setName(this.name);
        lRet.setActiva(this.isActive);
        lRet.setVisible(this.isVisible);
        lRet.setEditable(this.isEditable);
        lRet.setFieldExtendedForm(this.extendedForm);
        lRet.setRevisionActual(this.revisionActual);
        lRet.setVersionable(this.isVersionable);
        lRet.setUltimaRevision(this.getUltimaRevision());
        if (layerManager!=null){
            lRet.setLayerManager(layerManager);
            if (styleXML!=null)
                applyStyle(lRet,layerManager,styleXML);
        }
        return lRet;
    }*/

    
	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#applyStyle(com.geopista.model.GeopistaLayer, com.vividsolutions.jump.workbench.model.LayerManager, java.lang.String)
	 */
	
	/*public void applyStyle(IGeopistaLayer gpLayer,ILayerManager layerManager,String sXML){
        boolean bFiringEvents=layerManager.isFiringEvents();
        try{
            layerManager.setFiringEvents(false);
            gpLayer.setLayerManager(layerManager);
            List lStyles=new ArrayList();
            SLDStyleImpl sld=new SLDStyleImpl(sXML,gpLayer.getSystemId());
            sld.setSystemId(this.styleId);
            String sStyleName=this.styleName;
            List lSLD = sld.getStyles();
            if (sStyleName==null && lSLD.size()>0)
                sStyleName=((UserStyle_Impl)(lSLD.get(0))).getName();
            sld.setCurrentStyleName(sStyleName);
            lStyles.add(sld);
            lStyles.add(new SquareVertexStyle());
            lStyles.add(new LabelStyle());
            gpLayer.setStyles(lStyles);
        }catch(Exception e){
            e.printStackTrace();
        }
        layerManager.setFiringEvents(bFiringEvents);
    }*/


    
	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#buildSchema(java.lang.String)
	 */
	@Override
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

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#findPrimaryTable()
	 */
    
	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#findPrimaryTable()
	 */
	@Override
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

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#findID()
	 */
	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#findID()
	 */
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

	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#getRevisionActual()
	 */
	
	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#getRevisionActual()
	 */
	@Override
	public long getRevisionActual() {
		return revisionActual;
	}

	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#setRevisionActual(long)
	 */
	
	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#setRevisionActual(long)
	 */
	@Override
	public void setRevisionActual(long revisionActual) {
		this.revisionActual = revisionActual;
	}

	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#getUpdateQuery()
	 */
	
	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#getUpdateQuery()
	 */
	@Override
	public String getUpdateQuery() {
		return updateQuery;
	}

	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#setUpdateQuery(java.lang.String)
	 */
	
	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#setUpdateQuery(java.lang.String)
	 */
	@Override
	public void setUpdateQuery(String updateQuery) {
		this.updateQuery = updateQuery;
	}

	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#getInsertQuery()
	 */
	
	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#getInsertQuery()
	 */
	@Override
	public String getInsertQuery() {
		return insertQuery;
	}

	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#setInsertQuery(java.lang.String)
	 */
	
	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#setInsertQuery(java.lang.String)
	 */
	@Override
	public void setInsertQuery(String insertQuery) {
		this.insertQuery = insertQuery;
	}

	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#isVersionable()
	 */
	
	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#isVersionable()
	 */
	@Override
	public boolean isVersionable() {
		return isVersionable;
	}

	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#setVersionable(boolean)
	 */
	
	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#setVersionable(boolean)
	 */
	@Override
	public void setVersionable(boolean isVersionable) {
		this.isVersionable = isVersionable;
	}

	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#getUltimaRevision()
	 */
	
	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#getUltimaRevision()
	 */
	@Override
	public long getUltimaRevision() {
		return ultimaRevision;
	}

	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#setUltimaRevision(long)
	 */
	
	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#setUltimaRevision(long)
	 */
	@Override
	public void setUltimaRevision(long ultimaRevision) {
		this.ultimaRevision = ultimaRevision;
	}

	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#getIdUsuario()
	 */
	
	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#getIdUsuario()
	 */
	@Override
	public int getIdUsuario() {
		return idUsuario;
	}

	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#setIdUsuario(int)
	 */
	
	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#setIdUsuario(int)
	 */
	@Override
	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#setDeleteQuery(java.lang.String)
	 */
	
	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#setDeleteQuery(java.lang.String)
	 */
	@Override
	public void setDeleteQuery(String deleteQuery) {
		this.deleteQuery=deleteQuery;		
	}
	
	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#getDeleteQuery()
	 */
	
	/* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACLayer#getDeleteQuery()
	 */
	@Override
	public String getDeleteQuery() {
		return deleteQuery;		
	}
}
