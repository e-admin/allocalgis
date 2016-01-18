/**
 * Elemento.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.Kml3D.datos.filtros;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.JMenuItem;
import javax.swing.tree.TreeNode;

import com.geopista.ui.plugin.Kml3D.datos.filtros.OperadorLogico.OPERADOR_LOGICO;

public abstract class Elemento implements Cloneable{

	private static Hashtable <String, Elemento> asociacionItems = new Hashtable <String, Elemento>();
	
	
	
	public static void agregaItem ( JMenuItem menuItem, Elemento elemento )
	{
		asociacionItems.put ( menuItem.getText(), elemento );		
	}
	
	
	
	public static Elemento dameElemento ( JMenuItem menuItem )
	{
		return asociacionItems.get ( menuItem.getText() );
	}


	
	/**
	 * Crea el elemento del item cuyo texto nos pasan
	 * 
	 * @param textoItem
	 * @return
	 */
	public static Elemento dameElemento ( String textoItem )
	{
		Elemento elemento = asociacionItems.get ( textoItem );
		
		if ( elemento != null )
		{
			return elemento;
		}
		else if ( textoItem.startsWith ( "Literal" ) )
		{
			String [] trozos = textoItem.split ( " " );
			OperandoLiteral literal = new OperandoLiteral ( trozos[1] );
			return literal;
		}
		else if ( textoItem.startsWith ( "PropertyName" ) )
		{
			String [] trozos = textoItem.split ( " " );
			OperandoPropiedad propiedad = new OperandoPropiedad( trozos[1] );
			return propiedad;
		}
		else if ( textoItem.startsWith ( "Bbox" )  ||  textoItem.startsWith ( "BBOX" ) )
		{
			OperadorEspacialBbox operadorEspacialBbox = OperadorEspacialBbox.creaOperadorDesdeTexto ( textoItem );								
			return operadorEspacialBbox;
		}
		else if ( textoItem.startsWith ( "Beyond" )  ||  textoItem.startsWith ( "BEYOND" ) )
		{
			OperadorEspacialBeyond operadorEspacialBeyond = OperadorEspacialBeyond.creaOperadorDesdeTexto ( textoItem );								
			return operadorEspacialBeyond;
		}
		else if ( textoItem.startsWith ( "Dwithin" )  ||  textoItem.startsWith ( "DWITHIN" ) )
		{
			OperadorEspacialDwithin operadorEspacialDwithin = OperadorEspacialDwithin.creaOperadorDesdeTexto ( textoItem );								
			return operadorEspacialDwithin;
		}
		else
		{
			return null;
		}
	}
	
	
	
	/**
	 * Crea el elemento y sus hijos a partir del nodo
	 * 
	 * @param nodo
	 * @return
	 */
	public static Elemento dameElemento ( TreeNode nodo )
	{	
		String textoItem = nodo.toString();
		
		if ( textoItem.startsWith ( "Literal" ) )
		{
			String [] trozos = textoItem.split ( " " );
			OperandoLiteral literal = new OperandoLiteral ( trozos[1] );
			return literal;
		}
		else if ( textoItem.startsWith ( "PropertyName" ) )
		{
			String [] trozos = textoItem.split ( " " );
			OperandoPropiedad propiedad = new OperandoPropiedad( trozos[1] );
			return propiedad;
		}
		else if ( textoItem.startsWith ( "Bbox" )  ||  textoItem.startsWith ( "BBOX" ) )
		{
			OperadorEspacialBbox operadorEspacialBbox = OperadorEspacialBbox.creaOperadorDesdeTexto ( textoItem );
			
			if ( nodo.getChildCount() > 0 )
			{
				Enumeration<TreeNode> hijos = nodo.children();
				if ( hijos.hasMoreElements() )
				{
					TreeNode hijoActual = hijos.nextElement();
					operadorEspacialBbox.setOperando ( (Operando) Elemento.dameElemento( hijoActual ) );
				}
			}
			
			return operadorEspacialBbox;
		}
		else if ( textoItem.startsWith ( "Beyond" )  ||  textoItem.startsWith ( "BEYOND" ) )
		{
			OperadorEspacialBeyond operadorEspacialBeyond = OperadorEspacialBeyond.creaOperadorDesdeTexto ( textoItem );
			
			if ( nodo.getChildCount() > 0 )
			{
				Enumeration<TreeNode> hijos = nodo.children();
				if ( hijos.hasMoreElements() )
				{
					TreeNode hijoActual = hijos.nextElement();
					operadorEspacialBeyond.setOperando ( (Operando) Elemento.dameElemento( hijoActual ) );
				}
			}
			
			return operadorEspacialBeyond;
		}
		else if ( textoItem.startsWith ( "Dwithin" )  ||  textoItem.startsWith ( "DWITHIN" ) )
		{
			OperadorEspacialDwithin operadorEspacialDwithin = OperadorEspacialDwithin.creaOperadorDesdeTexto ( textoItem );
			
			if ( nodo.getChildCount() > 0 )
			{
				Enumeration<TreeNode> hijos = nodo.children();
				if ( hijos.hasMoreElements() )
				{
					TreeNode hijoActual = hijos.nextElement();
					operadorEspacialDwithin.setOperando ( (Operando) Elemento.dameElemento( hijoActual ) );
				}
			}
			
			return operadorEspacialDwithin;
		}
		else if ( textoItem.startsWith ( "AND" )  ||  textoItem.startsWith ( "OR" )  ||  textoItem.startsWith ( "NOT" ) )
		{
			OperadorLogico operadorLogico = new OperadorLogico ( OPERADOR_LOGICO.valueOf( textoItem ) );
			
			if ( nodo.getChildCount() > 0 )
			{
				Enumeration<TreeNode> hijos = nodo.children();
				while ( hijos.hasMoreElements() )
				{
					TreeNode hijoActual = hijos.nextElement();
					operadorLogico.addOperadorLogico( ( Operador ) Elemento.dameElemento( hijoActual ) );
				}
			}		
			
			return operadorLogico;
		}
		else
		{
			OperadorComparacion operadorComparacion = new OperadorComparacion ( textoItem );
			
			if ( nodo.getChildCount() > 0 )
			{
				Enumeration<TreeNode> hijos = nodo.children();
				while ( hijos.hasMoreElements() )
				{
					TreeNode hijoActual = hijos.nextElement();
					operadorComparacion.addOperandoComparacion( ( Operando ) Elemento.dameElemento( hijoActual ) );
				}
			}		
			
			return operadorComparacion;
		}
	}
	
	
	
	
	
	
	
	public abstract String getTexto();
	public abstract Elemento clone();	
	
}
