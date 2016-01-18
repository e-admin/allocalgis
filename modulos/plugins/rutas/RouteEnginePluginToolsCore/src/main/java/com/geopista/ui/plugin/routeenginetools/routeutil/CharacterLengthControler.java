/**
 * CharacterLengthControler.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.routeenginetools.routeutil;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class CharacterLengthControler extends PlainDocument{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7522763376474137520L;

	/**
	 * El editor del que estamos limitando caracteres.
	 */
	private JTextField editor;

	/**
	 * Nï¿½mero Maximo de caracteres que deseamos en el editor.
	 */
	private int numeroMaximoCaracteres;

	/**
	 * Crea una instancia de LimitadorCaracteres.
	 * 
	 * @param editor Editor en el que se quieren limitar los caracteres.
	 * @param numeroMaximoCaracteres Nï¿½mero Maximo de caracteres que queremos
	 * en el editor.
	 */
	public CharacterLengthControler(JTextField editor, int numeroMaximoCaracteres)
	{
		this.editor=editor;
		this.numeroMaximoCaracteres=numeroMaximoCaracteres;
	}

	/**
	 * Mï¿½todo al que llama el editor cada vez que se intenta insertar caracteres.
	 * El mï¿½todo comprueba que no se sobrepasa el lï¿½mite. Si es asï¿½, llama al
	 * mï¿½todo de la clase padre para que se inserten los caracteres. Si se 
	 * sobrepasa el lï¿½mite, retorna sin hacer nada.
	 */
	public void insertString(int arg0, String arg1, AttributeSet arg2) throws BadLocationException
	{
		if ((editor.getText().length()+arg1.length())>this.numeroMaximoCaracteres)
			return;
		super.insertString(arg0, arg1, arg2);
	}

}

