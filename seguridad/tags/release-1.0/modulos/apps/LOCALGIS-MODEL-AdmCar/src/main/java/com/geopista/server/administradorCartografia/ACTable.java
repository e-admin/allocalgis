package com.geopista.server.administradorCartografia;

import com.geopista.feature.Table;

import java.io.Serializable;

public class ACTable implements Serializable, IACTable{
    int id_table;
    String name;
    int geometryType=-1;

    public ACTable(){
    }

    public ACTable(int id, String sName, int iGeometryType){
        this.name=sName;
        this.id_table=id;
        this.geometryType=iGeometryType;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACTable#getId()
	 */
    @Override
	public int getId() {
        return id_table;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACTable#setId(int)
	 */
    @Override
	public void setId(int id_table) {
        this.id_table = id_table;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACTable#getName()
	 */
    @Override
	public String getName() {
        return name;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACTable#setName(java.lang.String)
	 */
    @Override
	public void setName(String sName) {
        this.name = sName;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACTable#getGeometryType()
	 */
    @Override
	public int getGeometryType() {
        return geometryType;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACTable#setGeometryType(int)
	 */
    @Override
	public void setGeometryType(int geometryType) {
        this.geometryType = geometryType;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACTable#convert()
	 */
    @Override
	public Table convert(){
        Table tRet=new Table(this.name,this.name);
        if (this.geometryType!=-1)
            tRet.setGeometryType(this.geometryType);
        return tRet;
    }
}
