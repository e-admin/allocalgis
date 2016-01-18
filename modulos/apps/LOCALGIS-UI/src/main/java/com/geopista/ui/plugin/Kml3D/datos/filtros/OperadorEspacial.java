/**
 * OperadorEspacial.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.Kml3D.datos.filtros;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;

import com.geopista.ui.plugin.Kml3D.dialogs.filtro.FiltroDialogo;


public abstract class OperadorEspacial extends Operador {

	
	public enum OPERADOR_ESPACIAL { DWITHIN, BEYOND, BBOX };
	
	
	private Operando operando = null;	
	
	private OPERADOR_ESPACIAL operadorEspacial = null;
	
	private static List <JMenuItem> itemsMenu = null;
	
	private static FiltroDialogo dialogoActual = null;
	
	
	
	
	public OperadorEspacial ( OPERADOR_ESPACIAL operadorEspacial )
	{
		this.operadorEspacial = operadorEspacial;
	}
	
	
	
	/**
	 * Crea si hace falta y devuelve los items del menu espacial
	 * @return
	 */
	public static List <JMenuItem> dameItemsMenuEspacial ( FiltroDialogo dialogoActual )
	{
		if ( ( itemsMenu == null )  ||  ( OperadorEspacial.dialogoActual != dialogoActual) )
		{
			itemsMenu = new ArrayList <JMenuItem>();
			
			for ( OPERADOR_ESPACIAL operadorActual : OPERADOR_ESPACIAL.values() )			
			{				
				JMenuItem menuItem = new JMenuItem ( operadorActual.toString() );
				menuItem.addActionListener ( dialogoActual );
				itemsMenu.add ( menuItem );		
				
				OperadorEspacial operadorEspacial;
				if ( operadorActual.equals ( OPERADOR_ESPACIAL.BBOX ) )
				{
					operadorEspacial = new OperadorEspacialBbox( OPERADOR_ESPACIAL.BBOX );
				}
				else if ( operadorActual.equals ( OPERADOR_ESPACIAL.BEYOND ) )
				{
					operadorEspacial = new OperadorEspacialBeyond ( OPERADOR_ESPACIAL.BEYOND );
				}
				else
				{
					operadorEspacial = new OperadorEspacialDwithin ( OPERADOR_ESPACIAL.DWITHIN );
				}
				
				Elemento.agregaItem( menuItem, operadorEspacial);
			}
		}
		
		return itemsMenu;
	}
	
	
	

	
	
	
	public OPERADOR_ESPACIAL getOperadorEspacial() {
		return operadorEspacial;
	}

	
	
	public boolean masHijosPermitidos()
	{
		if ( this.getNumHijos() < 1 )
		{
			return true;
		}
		else
		{
			return false;
		}
	}



	public Operando getOperando() {
		return operando;
	}



	public void setOperando(Operando operando) {
		this.operando = operando;
	}
	
	
	
	
	
	
	
}
