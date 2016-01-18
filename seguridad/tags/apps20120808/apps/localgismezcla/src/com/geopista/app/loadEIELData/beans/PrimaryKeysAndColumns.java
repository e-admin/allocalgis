package com.geopista.app.loadEIELData.beans;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import com.geopista.app.loadEIELData.Utils;


public class PrimaryKeysAndColumns {

	Hashtable<String,String> primaryKeys;
	Hashtable<String,String> primaryKeysOriginal;
	Hashtable<String,String> valueKeys;
	ArrayList<ColumnInfo> columns;	
	
	public PrimaryKeysAndColumns() {
		super();
	}
	
	public PrimaryKeysAndColumns(Hashtable<String, String> primaryKeys,
			ArrayList<ColumnInfo> columns) {
		super();
		this.primaryKeys = primaryKeys;
		this.columns = columns;
	}
	
	public Hashtable<String,String> getPrimaryKeys() {
		return primaryKeys;
	}
	public void setPrimaryKeys(Hashtable<String,String> primaryKeys) {
		this.primaryKeys = primaryKeys;
	}
	
	public Hashtable<String,String> getPrimaryKeysOriginal() {
		return primaryKeysOriginal;
	}
	public void setPrimaryKeysOriginal(Hashtable<String,String> primaryKeysOriginal) {
		this.primaryKeysOriginal = primaryKeysOriginal;
	}
	
	public Hashtable<String,String> getValueKeys() {
		return valueKeys;
	}
	public void setValueKeys(Hashtable<String,String> valueKeys) {
		this.valueKeys = valueKeys;
	}
	
	public ArrayList<ColumnInfo> getColumns() {
		return columns;
	}
	public void setColumns(ArrayList<ColumnInfo> columns) {
		this.columns = columns;
	}
	
	private String quitarPuntoDecimal(String cadena){
		return cadena.replaceAll("\\.",",");
	}
	
	private String ponerPuntoDecimal(String cadena){
		return cadena.replaceAll("\\,",".");
	}

	/*public static void main(String args[]){
		String cadena="0.9.";
		cadena=PrimaryKeysAndColumns.quitarPuntoDecimal(cadena);
		System.out.println("CAD:"+cadena);
	}*/
	
	/*
	 * Comprueba que dos instancias de PrimaryKeysAndColumns tengan las mismas claves primarias con los mismos valores.
	 * 
	 * */
	@Override
	public boolean equals(Object obj) {
		return equals(obj,false,false);
	}
	
	public boolean isDouble(String valor){
		try {
            Double.parseDouble(valor);        
        } catch (NumberFormatException ex) {
            return false;
        }
		return true;
		
	}
	
	public boolean ambosNumericos(String valor1, String valor2){
		try {
            Double.parseDouble(ponerPuntoDecimal(valor1));
            Double.parseDouble(ponerPuntoDecimal(valor2));    
        } catch (NumberFormatException ex) {
            return false;
        }
		return true;
	}
	

	public boolean equals(Object obj,boolean matchContains,boolean reverse) {
		
		StringBuffer sb=new StringBuffer();
		if(!(obj instanceof PrimaryKeysAndColumns)) return false;
		PrimaryKeysAndColumns primaryKeysExt = (PrimaryKeysAndColumns) obj;
		Enumeration<String> pKeys = this.getPrimaryKeys().keys();
		if(!pKeys.hasMoreElements()) return false;
		String cadena="##\n";
		Utils.infoMatchTemp.delete(0, Utils.infoMatchTemp.length());
		Utils.infoMatchTemp.append(cadena);
		sb.append(cadena);
		Utils.numClavesCoincidentesActual=0;
		Utils.numClavesCoincidentesSinMatchContainsActual=0;
		while(pKeys.hasMoreElements()){
			String pKey = pKeys.nextElement();
			String valor="";
			try{
				valor=this.getValueKeys().get(pKey);
			}
			catch(Exception e){e.printStackTrace();}
			//System.out.println("Casando:"+this.getPrimaryKeys().get(pKey)+" con:"+primaryKeysExt.getPrimaryKeys().get(pKey));
			cadena="Match. "+valor+" (SHP):"+this.getPrimaryKeysOriginal().get(pKey)+"->"+quitarPuntoDecimal(this.getPrimaryKeys().get(pKey))+" con BBDD: "+primaryKeysExt.getPrimaryKeys().get(pKey)+"\n";
			Utils.infoMatchTemp.append(cadena);
			sb.append(cadena);
			
			if(!primaryKeysExt.getPrimaryKeys().containsKey(pKey)) return false;
			String clave=null;
			String claveExt=null;
			try{
				
				clave=quitarPuntoDecimal(this.getPrimaryKeys().get(pKey));
				claveExt=primaryKeysExt.getPrimaryKeys().get(pKey);

				if (clave.contains(",")){
					clave=clave.substring(0,claveExt.length());			
				}
				else if (claveExt.contains(",")){
					claveExt=claveExt.substring(0,clave.length());
				}
				
			}
			catch (Exception e){
				//Si da una excepcion verificamos si estamos trabajando con un numerico.
				//e.printStackTrace();
				if (!isDouble(ponerPuntoDecimal(clave))){					
					e.printStackTrace();
					System.exit(1);
				}
			}
			
			boolean compararElemento=true;
			//Vamos a realizar un pequeño apaño para el caso de travesias en
			//la red viaria ya que tiene el problema de la clave de denominacion
			//solo es importante cuando el tipo de infraestructura es una travesia
			//en caso contrario la clave no es importante. Como parametrizar esto es imposible
			//lo hacemos desde codigo.
			//Este patron pk5 pk7 es el de carreteras
			if (primaryKeysExt.getValueKeys()!=null){
				if ((primaryKeysExt.getValueKeys().get("pk5")!=null) &&	
						((primaryKeysExt.getValueKeys().get("pk5").equalsIgnoreCase("tipo_infr")) || 
								(primaryKeysExt.getValueKeys().get("pk5").equalsIgnoreCase("TIPO_INFR"))) &&
						(primaryKeysExt.getValueKeys().get("pk7")!=null) &&	
						((primaryKeysExt.getValueKeys().get("pk7").equalsIgnoreCase("denominaci")) ||
								(primaryKeysExt.getValueKeys().get("pk7").equalsIgnoreCase("DENOMINAC1")))){
					
					
					if (pKey.equals("pk7")){
						//Si el elemento es una travesia y lo que se esta comparando es la denominacion
						//no hacemos nada.
						if ((primaryKeysExt.getPrimaryKeys().get("pk5")!=null) &&
								(primaryKeysExt.getPrimaryKeys().get("pk5").equals("TR")))
								compararElemento=true;
							else						
								compararElemento=false;				
					}
				}
			}
			
			
			if (compararElemento){			
				if(!clave.equals(claveExt)){
					if (matchContains){
						
						if (!reverse){
							if (ambosNumericos(this.getPrimaryKeys().get(pKey),primaryKeysExt.getPrimaryKeys().get(pKey))){
								if(!quitarPuntoDecimal(this.getPrimaryKeys().get(pKey)).equals(primaryKeysExt.getPrimaryKeys().get(pKey))){
									return false;
								}
								else{
									System.out.println("Matching Exacto de numericos");
								}
								
							}
							else{
								if(!quitarPuntoDecimal(this.getPrimaryKeys().get(pKey)).contains(primaryKeysExt.getPrimaryKeys().get(pKey))){
				
									/*if (quitarPuntoDecimal(this.getPrimaryKeys().get(pKey)).contains(",")){
										Utils.infoMatchSpecial.append(sb);
									}*/
									return false;			
								}
	
							}
						}
						else{
							if (ambosNumericos(this.getPrimaryKeys().get(pKey),primaryKeysExt.getPrimaryKeys().get(pKey))){
								if(!quitarPuntoDecimal(this.getPrimaryKeys().get(pKey)).equals(primaryKeysExt.getPrimaryKeys().get(pKey))){
									return false;
								}
							}
							else if(!quitarPuntoDecimal(primaryKeysExt.getPrimaryKeys().get(pKey)).contains(this.getPrimaryKeys().get(pKey))){
								
								/*if (quitarPuntoDecimal(this.getPrimaryKeys().get(pKey)).contains(",")){
									Utils.infoMatchSpecial.append(sb);
								}*/
								return false;			
							}
						}
						
					}
					else
						return false;
				}
				else{
					Utils.numClavesCoincidentesSinMatchContainsActual++;
				}
				Utils.numClavesCoincidentesActual++;
			}
		}
		
		return true;
	}

	@Override
	public String toString() {
		return (primaryKeys.toString() + "\n" + columns.toString() + "\n\n");
	}
	
}
