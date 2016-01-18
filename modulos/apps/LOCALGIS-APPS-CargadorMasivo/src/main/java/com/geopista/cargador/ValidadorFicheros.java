/**
 * ValidadorFicheros.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.cargador;

import com.geopista.app.AppContext;
import com.geopista.editor.GeopistaEditor;
import com.vividsolutions.jump.util.Blackboard;

public class ValidadorFicheros {

    private String FICHERO_VIAS_INE = "ficheroViaIne"; 
    
	private Blackboard blackboard;
	private AppContext aplicacion;
	private GeopistaEditor geopistaEditor1;



	public ValidadorFicheros(Blackboard blackboard, AppContext aplicacion, GeopistaEditor geopistaEditor1){
		this.blackboard=blackboard;
		this.aplicacion=aplicacion;
		this.geopistaEditor1=geopistaEditor1;
	}
	
	/**
     * Método para determinar si el fichero numeros de policia es correcto
     * 
     * @param String
     *            fich, ruta del fichero a comprobar que sea correcto
     * @return boolean true si es correcto, false en caso contrario
     */
    public boolean validateNumPolicia(String fich)
    {
    	boolean ok=false;
    	
        /*try{
            String[] texto = new String[1];
            ok = GeopistaImportarCallejeroPanel.validateNumPolicia(geopistaEditor1, fich, aplicacion, texto);
            System.out.println(CargadorUtil.filterText(texto[0]));
            if(ok){
                blackboard.put("ficheroNumerosPolicia",fich);
                System.out.println(CargadorUtil.filterText(aplicacion.getI18nString("importar.informe.numeros.policia.fichero.correcto")));
            }
        } catch (Exception e){  
        	return false;
        }*/
        return ok;
    }
    
    /**
     * Método para determinar si el fichero tramos de via Ine es correcto
     * 
     * @param String
     *            fich, ruta del fichero a comprobar que sea correcto
     * @return boolean true si es correcto, false en caso contrario
     */
	public boolean validateTramosViaINE(String fich){		
		boolean ok=false;
		try{
			// Validar el fichero La verificación que todos las lineas sean de tamaño 273
			//if (GeopistaImportarCallejeroPanel.ficheroTextoIne(fich)){
				blackboard.put("ficheroTextoTramosViaIne",fich);
				System.out.println(aplicacion.getI18nString("importar.informacion.referencia.tramos.via"));				
				System.out.println("El proceso de validación de los tramos de via ine han finalizado correctamente.");
				ok=true;
			/*} else{
				System.out.println("El fichero no es correcto. No se podrán importar estos datos.");
			}*/
		} catch (Exception e){
		}
	 return ok;
	}


    /**
     * Método para determinar si el fichero tramos de via es correcto
     * 
     * @param String
     *            fichSHP, ruta del fichero a comprobar que sea correcto
     * @param String
     *            fichDBF, ruta del fichero a comprobar que sea correcto         
     * @return boolean true si es correcto, false en caso contrario
     */
    public boolean validateTramosVia(String fichSHP, String fichDBF)
    {
    	boolean ok=false;
        try{
        	blackboard.put("rutaFicheroEjes",fichSHP);
            blackboard.put("rutaFicheroCarVia",fichDBF);
            String cadenaTexto = "";
            cadenaTexto = "<font face=SansSerif size=3>"+ aplicacion.getI18nString("ImportacionComenzar")+ "<b>" + " "+ fichSHP + "</b>";
            cadenaTexto = cadenaTexto+ "<p>"+ aplicacion.getI18nString("OperacionMinutos")+ " ...</p></font>";
            System.out.println(CargadorUtil.filterText(cadenaTexto));
            String[] texto = new String[1];
            //TODO REVISAR
            //ok=GeopistaImportarCallejeroPanel.validateTramosVia(geopistaEditor1, aplicacion, fichSHP, fichDBF, texto);
            cadenaTexto=texto[0];
            System.out.println(CargadorUtil.filterText(cadenaTexto));
        } catch (Exception e){
        } 
        return ok;
    }
    
 
    
    /**
     * Método para determinar si el fichero de vias INE es correcto
     * 
     * @param String
     *            fich, ruta del fichero a comprobar que sea correcto
     * @return boolean true si es correcto, false en caso contrario
     */
    public boolean validateViasINE(String fich)
    {
    	boolean ok=false;
        try{
        	//TODO REVISAR
        	/*
            if(GeopistaImportarCallejeroPanel.validarFicheroViasINE(fich)){
                blackboard.put(FICHERO_VIAS_INE, fich);
                System.out.println(CargadorUtil.filterText(aplicacion.getI18nString("importar.informe.vias.ine.fichero.correcto")));
                ok=true;
            } else{
                System.out.println(CargadorUtil.filterText(aplicacion.getI18nString("importar.informacion.ficheros.no.correctos")));
            }*/
        } catch (Exception e){
        	ok=false;
        }
        return ok;
    }        
}
