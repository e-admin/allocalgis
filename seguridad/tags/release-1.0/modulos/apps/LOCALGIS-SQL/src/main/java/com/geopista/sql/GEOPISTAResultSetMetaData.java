package com.geopista.sql;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.lang.reflect.Array;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.Marshaller;
import java.io.OutputStreamWriter;

public class GEOPISTAResultSetMetaData implements ResultSetMetaData{
    private int iColumnCount=0;
    private int[] aiColumnDisplaySize=null;
    private int[] aiColumnType=null;
    private int[] aiPrecision=null;
    private int[] aiScale=null;
    private int[] aiNullable=null;
    private boolean[] abAutoIncrement=null;
    private boolean[] abCaseSensitive=null;
    private boolean[] abCurrency=null;

    private boolean[] abSearchable=null;
    private boolean[] abSigned=null;
    private String[] asCatalogName=null;
    private String[] asColumnClassName=null;
    private String[] asColumnLabel=null;
    private String[] asColumnName=null;
    private String[] asColumnTypeName=null;
    private String[] asSchemaName=null;
    private String[] asTableName=null;

    public GEOPISTAResultSetMetaData(){

    }
    
    public GEOPISTAResultSetMetaData(ResultSetMetaData rsmd) throws SQLException{
      if (rsmd!=null){
        iColumnCount=rsmd.getColumnCount();
        aiColumnDisplaySize=(int[])Array.newInstance(Integer.TYPE,iColumnCount);
        aiColumnType=(int[])Array.newInstance(Integer.TYPE,iColumnCount);
        aiPrecision=(int[])Array.newInstance(Integer.TYPE,iColumnCount);
        aiScale=(int[])Array.newInstance(Integer.TYPE,iColumnCount);
        aiNullable=(int[])Array.newInstance(Integer.TYPE,iColumnCount);
        abAutoIncrement=(boolean[])Array.newInstance(Boolean.TYPE,iColumnCount);
        abCaseSensitive=(boolean[])Array.newInstance(Boolean.TYPE,iColumnCount);
        abCurrency=(boolean[])Array.newInstance(Boolean.TYPE,iColumnCount);

        abSearchable=(boolean[])Array.newInstance(Boolean.TYPE,iColumnCount);
        abSigned=(boolean[])Array.newInstance(Boolean.TYPE,iColumnCount);
        asCatalogName=(String[])Array.newInstance(java.lang.String.class,iColumnCount);
        asColumnClassName=(String[])Array.newInstance(java.lang.String.class,iColumnCount);
        asColumnLabel=(String[])Array.newInstance(java.lang.String.class,iColumnCount);
        asColumnName=(String[])Array.newInstance(java.lang.String.class,iColumnCount);
        asColumnTypeName=(String[])Array.newInstance(java.lang.String.class,iColumnCount);
        asSchemaName=(String[])Array.newInstance(java.lang.String.class,iColumnCount);
        asTableName=(String[])Array.newInstance(java.lang.String.class,iColumnCount);

        for (int i=0;i<iColumnCount;i++){
          aiColumnDisplaySize[i]=rsmd.getColumnDisplaySize(i+1);
          aiColumnType[i]=rsmd.getColumnType(i+1);
          aiPrecision[i]=rsmd.getPrecision(i+1);
          aiScale[i]=rsmd.getScale(i+1);
          aiNullable[i]=rsmd.isNullable(i+1);
          abAutoIncrement[i]=rsmd.isAutoIncrement(i+1);
          abCaseSensitive[i]=rsmd.isCaseSensitive(i+1);
          abCurrency[i]=rsmd.isCurrency(i+1);

          abSearchable[i]=rsmd.isSearchable(i+1);
          abSigned[i]=rsmd.isSigned(i+1);
          asCatalogName[i]=rsmd.getCatalogName(i+1);
          asColumnClassName[i]=rsmd.getColumnClassName(i+1);
          asColumnLabel[i]=rsmd.getColumnLabel(i+1);
          asColumnName[i]=rsmd.getColumnName(i+1);
          asColumnTypeName[i]=rsmd.getColumnTypeName(i+1);
          asSchemaName[i]=rsmd.getSchemaName(i+1);
          asTableName[i]=rsmd.getTableName(i+1);
        }
      }
    }

    public int getColumnCount() throws SQLException {
        return this.iColumnCount;
    }

    public int getColumnDisplaySize(int i) throws SQLException {
        return aiColumnDisplaySize[i-1];
    }

    public int getColumnType(int i) throws SQLException {
        return aiColumnType[i-1];  
    }

    public int getPrecision(int i) throws SQLException {
        return aiPrecision[i-1]; 
    }

    public int getScale(int i) throws SQLException {
        return aiScale[i-1];
    }

    public int isNullable(int i) throws SQLException {
        return aiNullable[i-1];  
    }

    public boolean isAutoIncrement(int i) throws SQLException {
        return abAutoIncrement[i-1];  
    }

    public boolean isCaseSensitive(int i) throws SQLException {
        return abCaseSensitive[i-1];  
    }

    public boolean isCurrency(int i) throws SQLException {
        return abCurrency[i-1];  
    }

    public boolean isDefinitelyWritable(int i) throws SQLException {
        return false;  
    }

    public boolean isReadOnly(int i) throws SQLException {
        return true;  
    }

    public boolean isSearchable(int i) throws SQLException {
        return abSearchable[i-1];  
    }

    public boolean isSigned(int i) throws SQLException {
        return abSigned[i-1];  
    }

    public boolean isWritable(int i) throws SQLException {
        return false;  
    }

    public String getCatalogName(int i) throws SQLException {
        return asCatalogName[i-1];  
    }

    public String getColumnClassName(int i) throws SQLException {
        return asColumnClassName[i-1];  
    }

    public String getColumnLabel(int i) throws SQLException {
        return asColumnLabel[i-1];  
    }

    public String getColumnName(int i) throws SQLException {
        return asColumnName[i-1];  
    }

    public String getColumnTypeName(int i) throws SQLException {
        return asColumnTypeName[i-1];  
    }

    public String getSchemaName(int i) throws SQLException {
        return asSchemaName[i-1];  
    }

    public String getTableName(int i) throws SQLException {
        return asTableName[i-1];  
    }

    // Metodos para el marshall XML

    public void setColumnCount(int iColumnCount){
        this.iColumnCount=iColumnCount;
    }

    public int[] getColumnDisplaySize(){
      return this.aiColumnDisplaySize;
    }

    public void setColumnDisplaySize(int[] aiColumnDisplaySize){
      this.aiColumnDisplaySize=aiColumnDisplaySize;
    }

    public int[] getColumnType(){
      return this.aiColumnType;
    }

    public void setColumnType(int[] aiColumnType){
      this.aiColumnType=aiColumnType;
    }

    public int[] getPrecision(){
      return this.aiPrecision;
    }

    public void setPrecision(int[] aiPrecision){
      this.aiPrecision=aiPrecision;
    }

    public int[] getScale(){
      return this.aiScale;
    }

    public void setScale(int[] aiScale){
      this.aiScale=aiScale;
    }

    public int[] getNullable(){
      return this.aiNullable;
    }

    public void setNullable(int[] aiNullable){
      this.aiNullable=aiNullable;
    }


    public boolean[] getAutoIncrement(){
      return this.abAutoIncrement;
    }

    public void setAutoIncrement(boolean[] abAutoIncrement){
      this.abAutoIncrement=abAutoIncrement;
    }

    public boolean[] getCaseSensitive(){
      return this.abCaseSensitive;
    }

    public void setCaseSensitive(boolean[] abCaseSensitive){
      this.abCaseSensitive=abCaseSensitive;
    }

    public boolean[] getCurrency(){
      return this.abCurrency;
    }

    public void setCurrency(boolean[] abCurrency){
      this.abCurrency=abCurrency;
    }

    public boolean[] getSearchable(){
      return this.abSearchable;
    }

    public void setSearchable(boolean[] abSearchable){
      this.abSearchable=abSearchable;
    }

    public boolean[] getSigned(){
      return this.abSigned;
    }

    public void setSigned(boolean[] abSigned){
      this.abSigned=abSigned;
    }

    public String[] getCatalogName(){
      return this.asCatalogName;
    }

    public void setCatalogName(String[] asCatalogName){
      this.asCatalogName=asCatalogName;
    }


    public String[] getColumnClassName(){
      return this.asColumnClassName;
    }

    public void setColumnClassName(String[] asColumnClassName){
      this.asColumnClassName=asColumnClassName;
    }

    public String[] getColumnLabel(){
      return this.asColumnLabel;
    }

    public void setColumnLabel(String[] asColumnLabel){
      this.asColumnLabel=asColumnLabel;
    }

    public String[] getColumnName(){
      return this.asColumnName;
    }

    public void setColumnName(String[] asColumnName){
      this.asColumnName=asColumnName;
    }

    public String[] getColumnTypeName(){
      return this.asColumnTypeName;
    }

    public void setColumnTypeName(String[] asColumnTypeName){
      this.asColumnTypeName=asColumnTypeName;
    }

    public String[] getSchemaName(){
      return this.asSchemaName;
    }

    public void setSchemaName(String[] asSchemaName){
      this.asSchemaName=asSchemaName;
    }

    public String[] getTableName(){
      return this.asTableName;
    }

    public void setTableName(String[] asTableName){
      this.asTableName=asTableName;
    }

    public static void main(String args[])throws Exception{
        GEOPISTAResultSetMetaData  rsmd=new GEOPISTAResultSetMetaData(null);
        rsmd.iColumnCount=3;
        rsmd.aiColumnDisplaySize=new int[]{3,3,3};
        rsmd.aiColumnType=new int[]{3,3,3};
        rsmd.aiPrecision=new int[]{3,3,3};
        rsmd.aiScale=new int[]{3,3,3};
        rsmd.aiNullable=new int[]{0,0,0};
        rsmd.abAutoIncrement=new boolean[]{true,false,false};
        rsmd.abCaseSensitive=new boolean[]{true,false,false};
        rsmd.abCurrency=new boolean[]{true,false,false};

        rsmd.abSearchable=new boolean[]{true,false,false};
        rsmd.abSigned=new boolean[]{true,false,false};
        rsmd.asCatalogName=new String[]{"cat1","cat2","cat3"};
        rsmd.asColumnClassName=new String[]{"ccn1","ccn2","ccn3"};
        rsmd.asColumnLabel=new String[]{"cl1","cl2","cl3"};
        rsmd.asColumnName=new String[]{"cn1","cn2","cn3"};
        rsmd.asColumnTypeName=new String[]{"ctn1","ctn2","ctn3"};
        rsmd.asSchemaName=new String[]{"sn1","sn2","sn3"};
        rsmd.asTableName=new String[]{"tn1","tn2","tn3"};

       Marshaller.marshal(rsmd,new OutputStreamWriter(System.out));

    }

	public boolean isWrapperFor(Class arg0) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public Object unwrap(Class arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	

}
