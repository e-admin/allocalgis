/**
 * OperadorComparacion.java
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


public class OperadorComparacion extends Operador {


	public static final String [] OPERADORES_COMPARACION = { "=", "<", "<=", ">", ">=", "LIKE", "IS NULL", "BETWEEN" };
	
	
	private List <Operando> operandosHijos = new ArrayList <Operando>();
	
	
	private String operadorComparacion = "";
	private static List <JMenuItem> itemsMenu = null;
	
	private static FiltroDialogo dialogoActual = null;
	
	
	
	public OperadorComparacion ( String operadorComparacion )
	{
		this.operadorComparacion = operadorComparacion;
	}
	
	
	
	/**
	 * Crea si hace falta y devuelve los items del menu comparacion
	 * @return
	 */
	public static List <JMenuItem> dameItemsMenuComparacion ( FiltroDialogo dialogoActual )
	{
		if ( ( itemsMenu == null )  ||  ( OperadorComparacion.dialogoActual != dialogoActual) )
		{
			itemsMenu = new ArrayList <JMenuItem>();
			
			for ( String operadorActual : OPERADORES_COMPARACION )
			{
				JMenuItem menuItem = new JMenuItem ( operadorActual );
				menuItem.addActionListener ( dialogoActual );
				itemsMenu.add ( menuItem );		
				Elemento.agregaItem( menuItem, new OperadorComparacion ( operadorActual ) );
			}
		}
		
		return itemsMenu;
	}
	
	
	
	
	
	public String getTexto ()
	{
		String texto = this.operadorComparacion;
		if ( texto.equals ( "is null" ) )
		{
			texto = "null";
		}
		
		return texto.toUpperCase();
	}
	
	
	
	public Elemento clone()
	{
		return new OperadorComparacion ( this.operadorComparacion );
	}
	
	
	
	/**
	 * Indica si el operador admite más hijos
	 */
	public boolean masHijosPermitidos()
	{		
		if ( this.operadorComparacion.equals ( "IS NULL" ) )
		{
			if ( this.getNumHijos() < 1 )
			{
				return true;
			}
		}
		else
		{
			if ( this.getNumHijos() < 2)
			{
				return true;
			}
		}
		
		return false;
	}
	
	
	
	
	public void addOperandoComparacion ( Operando operando )
	{
		this.operandosHijos.add ( operando );
	}
	
	
	
	public List<Operando> getHijosOperandos ()
	{
		return this.operandosHijos;
	}
	
	
	
	public String creaFiltro()
	{
		String operando = "";
		if ( this.operadorComparacion.equals ( "IS NULL" ) )
		{
			operando = "IsNull";
		}
		else if ( this.operadorComparacion.equals ( "BETWEEN" ) )
		{
			operando = "between";
		}
		else if ( this.operadorComparacion.equals ( "LIKE" ) )
		{
			operando = "IsLike";
		}
		else
		{
			operando = this.operadorComparacion;
		}
		
			
		String texto = "";
		
		if ( this.operadorComparacion.equals( "IS NULL" ) )
		{
			texto = operando + " ";
			if ( this.operandosHijos.size() > 0 )
			{
				texto += this.operandosHijos.get ( 0 ).creaFiltro() + " ";			
			}
		}
		else
		{
			if ( this.operandosHijos.size() > 0 ) 
			{
				texto = this.operandosHijos.get ( 0 ).creaFiltro() + " " + operando + " ";
				
				if ( this.operandosHijos.size() > 1 )
				{
					texto += this.operandosHijos.get ( 1 ).creaFiltro();
				}
			}
			else
			{
				texto = operando + " ";
			}
		}
		
		return texto;
	}
	
	
	
	
}
