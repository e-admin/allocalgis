package com.geopista.app.eiel.beans.filter;

import java.util.Date;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 04-oct-2006
 * Time: 16:41:53
 * To change this template use File | Settings | File Templates.
 */
public class CampoFiltroModel implements Serializable, Cloneable, Comparable{
    private static final long serialVersionUID= 3675121612249975115L;
    
    private String nombre;
    private String descripcion;
    private int tipo=-1;
    private int dominio=-1;
    private String tabla;

    private String operador;
    private String valorVarchar;
    private Date valorDate;
    private double valorDouble=-1;
    private long valorNumeric=-1;
    private String valorTerm;
    private boolean valorBoolean= false;
    private String valorCompuesto;

	private String nombreDominio;

    public static final int VARCHAR_CODE=0;
    public static final int NUMERIC_CODE=1;
    public static final int DATE_CODE=2;
    public static final int DOUBLE_CODE=3;
    public static final int DOMINIO_CODE=4;
    public static final int BOOLEAN_CODE=5;
    public static final int COMPUESTO_CODE=6;
    public static final int DOMINIO_CODE_INTEGER=7;

    public static final int CAMPO_CODIGOENTIDAD=-1;
    public static final int CAMPO_CODIGONUCLEO=-2;

    
    public String getTabla() {
        return tabla;
    } 

    public void setTabla(String tabla) {
        this.tabla = tabla;
    }

    public boolean getValorBoolean() {
        return valorBoolean;
    }

    public void setValorBoolean(boolean valorBoolean) {
        this.valorBoolean = valorBoolean;
    }
    
    public String getValorCompuesto() {
        return valorCompuesto;
    }

    public void setValorCompuesto(String valorCompuesto) {
        this.valorCompuesto= valorCompuesto;
    }

    public String getOperador() {
        return operador;
    }

    public void setOperador(String operador) {
        this.operador = operador;
    }

    public String getValorTerm() {
        return valorTerm;
    }

    public void setValorTerm(String valorTerm) {
        this.valorTerm = valorTerm;
    }

    public String getValorVarchar() {
        return valorVarchar;
    }

    public void setValorVarchar(String valorVarchar) {
        this.valorVarchar = valorVarchar;
    }

    public Date getValorDate() {
        return valorDate;
    }

    public void setValorDate(Date valorDate) {
        this.valorDate = valorDate;
    }

    public double getValorDouble() {
        return valorDouble;
    }

    public void setValorDouble(double valorDouble) {
        this.valorDouble = valorDouble;
    }

    public long getValorNumeric() {
        return valorNumeric;
    }

    public void setValorNumeric(long valorNumeric) {
        this.valorNumeric = valorNumeric;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public int getDominio() {
        return dominio;
    }

    public void setDominio(int dominio) {
        this.dominio = dominio;
    }

    public boolean isVarchar() {
        return tipo==VARCHAR_CODE;
    }

    public boolean isNumeric() {
        return tipo==NUMERIC_CODE;
    }

    public boolean isDate() {
        return tipo==DATE_CODE;
    }
    public boolean isDouble() {
        return tipo==DOUBLE_CODE;
    }
    public boolean isDominio() {
        return tipo==DOMINIO_CODE;
    }
    public boolean isDominioInteger() {
        return tipo==DOMINIO_CODE_INTEGER;
    }
    public boolean isBoolean() {
        return tipo==BOOLEAN_CODE;
    }

    public void setVarchar() {
        tipo=VARCHAR_CODE;
    }

    public void setNumeric() {
        tipo=NUMERIC_CODE;
    }

    public void setDate() {
        tipo=DATE_CODE;
    }
    public void setDouble() {
        tipo=DOUBLE_CODE;
    }
    public void setIsDominio() {
        tipo=DOMINIO_CODE;
    }
    public void setIsDominioInteger() {
        tipo=DOMINIO_CODE_INTEGER;
    }
    public void setBoolean() {
        tipo=BOOLEAN_CODE;
    }
    
    public boolean isCompuesto() {
    	return tipo==COMPUESTO_CODE;
    }
    public void setCompuesto() {
        tipo=COMPUESTO_CODE;
    }

    public Object clone() {
        CampoFiltroModel obj=null;
        try{
            obj=(CampoFiltroModel)super.clone();
         }catch(CloneNotSupportedException ex){}

        obj.nombre= nombre;
        obj.descripcion= descripcion;
        obj.tipo= tipo;
        obj.dominio= dominio;
        obj.tabla= tabla;
        obj.operador= operador;
        obj.valorVarchar= valorVarchar;
        obj.valorDate= valorDate;
        obj.valorDouble= valorDouble;
        obj.valorNumeric= valorNumeric;
        obj.valorTerm= valorTerm;
        obj.valorBoolean= valorBoolean;

        return obj;
    }

    public int compareTo(Object obj) {
        CampoFiltroModel campo= (CampoFiltroModel)obj;
        return descripcion!=null?descripcion.compareToIgnoreCase(campo.getDescripcion()!=null?campo.getDescripcion():campo.nombre):nombre.compareToIgnoreCase(campo.getDescripcion()!=null?campo.getDescripcion():campo.nombre);
    }

	public void setNombreDominio(String nombreDominio) {
		this.nombreDominio=nombreDominio;
		
	}
	
	public String getNombreDominio(){
		return nombreDominio;
	}




}
