/**
 * Validation.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.cementerios.utils;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.geopista.protocol.cementerios.PlazaBean;
import com.geopista.protocol.cementerios.UnidadEnterramientoBean;



public class Validation {
	
	
	private static Validation instance;
	
	public static  Validation getInstance(){
		if (instance == null) 
			instance = new Validation();
		return instance;
	}

	public boolean validateDNI(JFrame desktop, Object value) {
	
		boolean validationOk= true;;
	    	try {
				String cadena = "";
				String letras = "TRWAGMYFPDXBNJZSQVHLCKET";
				int NumDNI = 0;
				int Indice = 0;
	
				String nif1 = ((String) value).toUpperCase();
	
				if ( Character.isLetter(nif1.charAt(0)) ){ 
					if(nif1.charAt(0) != 'X'){
				    	String message = "El NIE introducido es incorrecto.";
	              		JOptionPane.showMessageDialog(desktop,message, "Información validación" ,JOptionPane.WARNING_MESSAGE);
	              		validationOk= false;
					}
						
					else
						cadena += nif1.substring(1, nif1.length());
				}
				else {
					cadena += nif1;
				}
				
				char correcto = cadena.charAt(cadena.length() - 1);
				NumDNI = Integer.valueOf(cadena.substring(0,cadena.length()-1));
				Indice = NumDNI - (23 * (NumDNI/23));
				if ((Indice >= 0) && (Indice < 24)) {
					if (letras.charAt(Indice) != correcto) {
				    	String message = "La letra del NIF/NIE introducido es incorrecta.";
	              		JOptionPane.showMessageDialog(desktop,message, "Información validación" ,JOptionPane.WARNING_MESSAGE);
	              		validationOk= false;

					}
				}
			} catch (NumberFormatException e) {
		    	String message = " - El NIF/NIE introducido es incorrecto";
          		JOptionPane.showMessageDialog(desktop,message, "Información validación" ,JOptionPane.WARNING_MESSAGE);
          		validationOk= false;
				
			}
			return validationOk;
		}
	
	
	public boolean todasPlazasLibres (UnidadEnterramientoBean unidad){
		boolean todasLibres = true;
		
		ArrayList<PlazaBean> listaPlazas;
		listaPlazas = unidad.getPlazas();
		PlazaBean elem;
		for (int i = 0; i < listaPlazas.size(); i++) {
			elem = (PlazaBean) listaPlazas.get(i);
			if (elem.isEstado()){ //plaza asignada
					todasLibres = false;
					break;
			}
		}
		return todasLibres;
	}

}
