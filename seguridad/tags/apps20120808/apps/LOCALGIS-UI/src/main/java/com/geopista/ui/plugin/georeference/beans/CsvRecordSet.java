package com.geopista.ui.plugin.georeference.beans;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import reso.jump.joinTable.JoinTableDataSourceCSV;

/**
 * Esta clase almacena listas con los atributos introducidos por teclado
 * @author jvaca
 * 
 *
 */
public class CsvRecordSet
{

    private List fieldValue =new ArrayList();   
    
    //Este objeto almacenara una lista con los campos asi como una lista con los tipos de datos
    private JoinTableDataSourceCSV objetDataSource=null;
    /**
     * constructor
     */
    public void CsvRecordSet(){}
    /**
     * 
     * @param int indice del valor que se quiere recuperar 
     */
    public String getString(int i){
        String value=(String)fieldValue.get(i);
        return value;
    }
    /**
     * 
     * @param String con el nombre del atributo para el que queremos recuperar el valor 
     */
    public String getString(String attrib){
        List fieldNames=objetDataSource.getFieldNames();
        String value=null;
        for(int i=0;i<fieldNames.size();i++){
            if(fieldNames.get(i).equals(attrib)){
                value=getString(i);
                break;
            }
        }
        return value;
    }
    /**
     * 
     * @param int indice del valor que se quiere recuperar 
     */
    public Integer getInteger(int i){
        Integer value=new Integer((String) fieldValue.get(i));
        return value;
        
    }
    /**
     * 
     * @param int indice del valor que se quiere recuperar 
     */
    public Integer getInteger(String attrib){
        List fieldNames=objetDataSource.getFieldNames();
        Integer value=null;
        for(int i=0;i<fieldNames.size();i++){
            if(fieldNames.get(i).equals(attrib)){
                value=getInteger(i);
                break;
            }
        }
        return value;
    }
    /**
     * 
     * @param int indice del valor que se quiere recuperar 
     */
    public Double getDouble(int i){
        Double value=new Double((String)fieldValue.get(i));
        return value;
    }
    /**
     * 
     * @param int indice del valor que se quiere recuperar 
     */
    public Double getDouble(String attrib){
        List fieldNames=objetDataSource.getFieldNames();
        Double value=null;
        for(int i=0;i<fieldNames.size();i++){
            if(fieldNames.get(i).equals(attrib)){
                value=getDouble(i);
                break;
            }
        }
        return value;
    }
    /**
     * 
     * @param int indice del valor que se quiere recuperar 
     */
    public Date getDate(int i){
        Date value=null;
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
            value=sdf.parse((String)fieldValue.get(i));
        //Date value=new Date(sdf.format(fieldValue.get(i)));
        }catch(Exception e){
            e.printStackTrace();
        }
        return value;
    }
    /**
     * 
     * @param int indice del valor que se quiere recuperar 
     */
    public Date getDate(String attrib){
        List fieldNames=objetDataSource.getFieldNames();
        Date value=null;
        for(int i=0;i<fieldNames.size();i++){
            if(fieldNames.get(i).equals(attrib)){
                value=getDate(i);
                break;
            }
        }
        return value;
    }
    

    /**
     * @return Returns the objetDataSource.
     */
    public JoinTableDataSourceCSV getObjetDataSource()
    {
        return objetDataSource;
    }
    /**
     * @param objetDataSource The objetDataSource to set.
     */
    public void setObjetDataSource(JoinTableDataSourceCSV objetDataSource)
    {
        this.objetDataSource = objetDataSource;
    }

    
    /**
     * @return Returns the fieldValue.
     */
    public List getFieldValue()
    {
        return fieldValue;
    }
    /**
     * @param fieldValue The fieldValue to set.
     */
    public void setFieldValue(List fieldValue)
    {
        this.fieldValue = fieldValue;
    }
    
}
