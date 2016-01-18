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
	 * Número máximo de caracteres que deseamos en el editor.
	 */
	private int numeroMaximoCaracteres;

	/**
	 * Crea una instancia de LimitadorCaracteres.
	 * 
	 * @param editor Editor en el que se quieren limitar los caracteres.
	 * @param numeroMaximoCaracteres Número máximo de caracteres que queremos
	 * en el editor.
	 */
	public CharacterLengthControler(JTextField editor, int numeroMaximoCaracteres)
	{
		this.editor=editor;
		this.numeroMaximoCaracteres=numeroMaximoCaracteres;
	}

	/**
	 * Método al que llama el editor cada vez que se intenta insertar caracteres.
	 * El método comprueba que no se sobrepasa el límite. Si es así, llama al
	 * método de la clase padre para que se inserten los caracteres. Si se 
	 * sobrepasa el límite, retorna sin hacer nada.
	 */
	public void insertString(int arg0, String arg1, AttributeSet arg2) throws BadLocationException
	{
		if ((editor.getText().length()+arg1.length())>this.numeroMaximoCaracteres)
			return;
		super.insertString(arg0, arg1, arg2);
	}

}

