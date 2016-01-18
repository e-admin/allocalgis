package com.geopista.server.administradorCartografia;

import com.geopista.feature.Attribute;
import com.geopista.feature.Column;

import java.io.Serializable;


/** Datos de atributo para el interfaz con el Administrador de Cartografia */
public class ACAttribute implements Serializable{
    int id;
    String name;
    IACColumn column;
    int position;
    boolean editable;

    public int getId() {
        return id; 
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IACColumn getColumn() {
        return column;
    }

    public void setColumn(IACColumn column) {
        this.column = column;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    /** Obtiene un atributo de Jump */
    public Attribute convert(Column c){
        Attribute aRet=new Attribute();
        aRet.setName(this.name);
        if (!editable)
            aRet.setAccessType("R");
        return aRet;
    }
}
