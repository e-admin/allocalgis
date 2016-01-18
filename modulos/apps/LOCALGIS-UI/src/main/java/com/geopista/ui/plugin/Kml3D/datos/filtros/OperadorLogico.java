/**
 * OperadorLogico.java
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

public class OperadorLogico extends Operador {

	public enum OPERADOR_LOGICO { AND, OR, NOT };
	
	private List <Operador> operadoresHijos = new ArrayList <Operador>();
	
	private OPERADOR_LOGICO operadorLogico = null;
	
	private static List <JMenuItem> itemsMenu = null;
	
	private static FiltroDialogo dialogoActual = null;
	
	
	
	public OperadorLogico ( OPERADOR_LOGICO operadorLogico )
	{
		this.operadorLogico = operadorLogico;
	}
	
	
	
	/**
	 * Crea si hace falta y devuelve los items del menu logico
	 * @return
	 */
	public static List <JMenuItem> dameItemsMenuLogico ( FiltroDialogo dialogoActual )
	{
		if ( ( itemsMenu == null )  ||  ( OperadorLogico.dialogoActual != dialogoActual) )
		{
			itemsMenu = new ArrayList <JMenuItem>();			
			for ( OPERADOR_LOGICO operadorActual : OPERADOR_LOGICO.values() )			
			{								
				JMenuItem menuItem = new JMenuItem ( operadorActual.toString() );
				menuItem.addActionListener ( dialogoActual );
				itemsMenu.add ( menuItem );	
				Elemento.agregaItem( menuItem, new OperadorLogico ( operadorActual ) );
			}
		}
		
		return itemsMenu;
	}
	
	
	
	public String getTexto ()
	{
		return this.operadorLogico.toString();
	}
	
	
	
	
	public Elemento clone()
	{
		return new OperadorLogico ( this.operadorLogico );
	}
	
	
	
	/**
	 * Indica si el operador admite más hijos
	 */
	public boolean masHijosPermitidos()
	{
		int numHijos = this.getNumHijos();
		
		if ( this.operadorLogico.equals ( OPERADOR_LOGICO.NOT ) )
		{
			if ( numHijos < 1 )
			{
				return true;
			}
		}
		else 
		{
			return true;
		}
		
		return false;
	}
	
	
	
	public void addOperadorLogico ( Operador operador )
	{
		this.operadoresHijos.add( operador );
	}
	
		
	public List<Operador> getOperadoresHijos ()
	{
		return this.operadoresHijos;
	}
	
	
	
	public String creaFiltro()
	{
		String texto = "";
		if ( this.operadoresHijos.size() > 0 )
		{
			if ( this.operadorLogico.equals ( OPERADOR_LOGICO.NOT ) )
			{
				Operador operadorHijo = this.operadoresHijos.get ( 0 );
				texto = OPERADOR_LOGICO.NOT.toString() + " (" + operadorHijo.creaFiltro() + ") ";
			}
			else
			{
				int numHijos = 0;
				for ( Operador operador : this.operadoresHijos )
				{
					if ( numHijos == 0 )
					{
						texto = "(" + operador.creaFiltro() + ") " + this.operadorLogico.toString() + " ";  
					}
					else if ( numHijos == 1 )
					{
						texto += "(" + operador.creaFiltro() + ") ";
					}
					else
					{
						texto += this.operadorLogico.toString() + " (" + operador.creaFiltro() + ") ";
					}
					
					numHijos ++;
				}
			}
		}
		else
		{
			texto = this.operadorLogico.toString() + " ";
		}
		
		return texto;
	}
	
	
}
