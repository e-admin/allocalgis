/**
 * CalcRutaConfigFileReaderWriter.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * 
 */
package com.geopista.ui.plugin.routeenginetools.routeconfig;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import com.geopista.app.AppContext;


/**
 * @author javieraragon
 *
 */
public class CalcRutaConfigFileReaderWriter {  

	public static String REDPROPERTY = "redes";
	public static String VEHICLETYPE = "vehicletipe";
	public static String PAVEMENT_WIDTH = "anchuraAcera";
	public static String TRANSVERSAL_SLOPE = "pendienteTransversal";
	public static String LONGITUDINAL_SLOPE = "pendienteLongitudinal";
	public static String DISABILITY_TYPE = "disabilityType";
	
	private File filepath = new File (AppContext.getApplicationContext().getString("ruta.base.mapas"),"networks");
	private String fileName = "calculateroute.configuration";
	
	private Properties parameterList = new Properties();
	
	/**
	 * 
	 */
	public CalcRutaConfigFileReaderWriter() {
		// TODO Auto-generated constructor stub
		super();
		parameterList = readPropertiesFromConfigFile();
	}
	
	public String getCaclRoutePropertieByName(String propertyName){
		return parameterList.getProperty(propertyName);
	}
	
	public String[] getRedesNames(){
		String[] resultado = null;
		if (parameterList.getProperty(REDPROPERTY) != null){
			String redes = parameterList.getProperty(REDPROPERTY);
			redes.split(",");
			resultado =  redes.split(",");
		}
		return resultado;
	}
	
	public String getTipoVehiculo(){
		String resultado = null;
		if (parameterList.getProperty(VEHICLETYPE) != null){
			resultado = parameterList.getProperty(VEHICLETYPE);
		}
		return resultado;
	}

	public String getPavementWidth(){
		String resultado = null;
		if (parameterList.getProperty(PAVEMENT_WIDTH) != null){
			resultado = parameterList.getProperty(PAVEMENT_WIDTH);
		}
		return resultado;
	}

	public String getTransversalSlope(){
		String resultado = null;
		if (parameterList.getProperty(TRANSVERSAL_SLOPE) != null){
			resultado = parameterList.getProperty(TRANSVERSAL_SLOPE);
		}
		return resultado;
	}

	public String getLongitudinalSlope(){
		String resultado = null;
		if (parameterList.getProperty(LONGITUDINAL_SLOPE) != null){
			resultado = parameterList.getProperty(LONGITUDINAL_SLOPE);
		}
		return resultado;
	}

	public String getDisabilityType(){
		String resultado = null;
		if (parameterList.getProperty(DISABILITY_TYPE) != null){
			resultado = parameterList.getProperty(DISABILITY_TYPE);
		}
		return resultado;
	}
	
	public boolean writeParametersIntoConfigFile(String[] listaredes, String algorithm, String tipoVehiculo){
		
		ArrayList<String> lineasEscribir = getLinesFromParameterConfigFile();
				
		this.setParametersListValues(listaredes, algorithm, tipoVehiculo);
		
		if (listaredes == null){
			listaredes = new String[1];
		}

		if (tipoVehiculo == null){
			algorithm = " ";
		} if (tipoVehiculo == null){
			tipoVehiculo = " ";
		}
		
			
		if (lineasEscribir != null && lineasEscribir.size()> 0 && this.parameterList.get(REDPROPERTY) == null && this.parameterList.get(VEHICLETYPE) == null){
			for (int i = 0; i < lineasEscribir.size(); i++ ){
				if (lineasEscribir.get(i).startsWith(REDPROPERTY)){
					String redesParsed = " ";
					for (int m=0; m < listaredes.length; m++){
						if (m == 0){
							redesParsed = "" + listaredes[m];
						} else{
							redesParsed = redesParsed + "," + listaredes[m];
						}

					}
					lineasEscribir.set(i,
							lineasEscribir.get(i).replace(lineasEscribir.get(i).split("=")[1],redesParsed ));
				} else if (lineasEscribir.get(i).startsWith(VEHICLETYPE)){
					lineasEscribir.set(i,lineasEscribir.get(i).replace(lineasEscribir.get(i).split("=")[1], tipoVehiculo));
				}
			}
		}
		else{

			String redesParsed = "";
			for (int m=0; m < listaredes.length; m++){
				if (m == 0){
					redesParsed = redesParsed + listaredes[m];
				} else{
					redesParsed = redesParsed + "," + listaredes[m];
				}

			}
			
			if (lineasEscribir == null){
				lineasEscribir = new ArrayList<String>();
			}
			if (redesParsed == null || redesParsed.equals("")){
				redesParsed = " ";
			}
			lineasEscribir.add(REDPROPERTY + "=" + redesParsed );
			lineasEscribir.add(VEHICLETYPE + "=" + tipoVehiculo );
		}
		writeParametersIntoConfigFile(lineasEscribir);
		
		return true;
	}
	
	public boolean writeParametersIntoConfigFile(ArrayList lineasEscribir){
		try {
			FileWriter fileWriter = new FileWriter(new File(this.filepath, this.fileName));
			BufferedWriter bfWriter = new BufferedWriter(fileWriter);
			for (int i = 0; i < lineasEscribir.size(); i++ ){
				bfWriter.write(lineasEscribir.get(i) + "\n");
			}
			bfWriter.flush();
			bfWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
		
	}

	/**
	 * @param red
	 * @param subred
	 * @param algorithm
	 */
	private void setParametersListValues(String[] redes,
			String algorithm, String tipoVehiculo) {
		
		String redesParsed = " ";
		for (int i=0; i < redes.length; i++){
			redesParsed = redesParsed + "," + redes[i];
		}
		
		if (redes != null){
			this.parameterList.setProperty(REDPROPERTY, redesParsed);
		} else{
			this.parameterList.setProperty(REDPROPERTY, " ");
		}
		
		if (tipoVehiculo != null){
			this.parameterList.setProperty(VEHICLETYPE, tipoVehiculo);
		} else{
			this.parameterList.setProperty(VEHICLETYPE, "");
		}
	}
	
	public ArrayList<String> getLinesFromParameterConfigFile(){
		ArrayList<String>  result = null;
		String lineReader= "";
		BufferedReader bf = null;
		FileReader fr = null;
		

		try {
			
			fr = new FileReader(new File(this.filepath.getPath(),fileName));
								 
			
			bf = new BufferedReader(fr); 
			
			result = new ArrayList<String>();
			
			while ((lineReader = bf.readLine())!=null) {
				result.add(lineReader);
			} 
			
			bf.close();
			fr.close();
			
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try{
				bf.close();
			}catch(Exception e){e.printStackTrace();}
			try{
				fr.close();
			}catch(Exception e){e.printStackTrace();}
		}
		
		
		return result;
	}
	
	public void loadPropertiesFromConfigFile(){
		this.parameterList = readPropertiesFromConfigFile();
	}
	
	private Properties readPropertiesFromConfigFile(){
		Properties prop = new Properties();
		InputStream is = null;

		try {
			if (!filepath.exists()){
				filepath.mkdirs();
			}
			File fileToRead = new File(this.filepath.getPath(),this.fileName);
			if (!fileToRead.exists()){
				fileToRead.createNewFile();
			}
			is=new FileInputStream(new File(this.filepath.getPath(),this.fileName));
			prop.load(is);
		} catch(IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try{
				is.close();
			}catch(Exception e){
				
			}
		}
		
		return prop;
	}
	
	
}
