package com.geopista.app.utilidades.um.filteredtext.time;

import com.geopista.app.utilidades.um.util.UMConstants;
import com.geopista.app.utilidades.um.util.TextConverter;
import com.geopista.app.utilidades.um.event.TimeEvent;
import com.geopista.app.utilidades.um.filteredtext.JTimeTextField;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.FocusEvent;
import java.awt.event.InputMethodEvent;
import java.awt.*;
import java.util.Calendar;
import java.text.AttributedCharacterIterator;
import java.text.CharacterIterator;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 08-nov-2004
 * Time: 10:49:55
 * To change this template use File | Settings | File Templates.
 */
/**
 * This class acts as filter adapter for time. This class implements listeners
 * necessary for restricting keys allowed for time. Also it validates the
 * entered time when the field looses focus.
 * <p>
 * The time format this adapter sticks to is HH:MM. Here the hours should
 * be 0 to 23, so AM/PM should not be specified.
 * <p>
 * If validation fails and if the TimeListener for this adapter is attached,
 * gotInvalidTime method of the listener is invoked. So the user of this class
 * can have control if a invalid Time is pressed.
 *
 * @version	1.0	06/08/99
 * @author	UnicMan
 */
public class BaseHMTimeAdapter
	extends		TimeAdapter
	implements	UMConstants
{

    private JTimeTextField component;

    public BaseHMTimeAdapter(JComponent object){
        component= (JTimeTextField)object;
    }

	/**
	 * Handles KEY_TYPED event. This handler checks for the valid keys and
	 * consumes the event if the key in not allowed.
	 *
	 * @param	keKey	event object
	 */
	public void keyTyped( KeyEvent keKey )
	{
		char	cKey	= keKey.getKeyChar();

        String valorAnterior= component.getText();

        if( Character.isDigit(cKey) || cKey == TIME_SEP ){
            String value= valorAnterior;

            /** Annadimos el caracter leido al valor del componente. De esta forma tenemos el
             * valor actualizado. */
            char[] keyChar= new char[1];
            keyChar[0]= cKey;
            value+= new String(keyChar);
            if (value.length() > 5){
                keKey.consume();
                component.setText(valorAnterior);
                return;
            }

            if (cKey == TIME_SEP){
                /** comprobamos si ya existe */
                if (valorAnterior.indexOf(TIME_SEP, 0) != -1){
                    keKey.consume();
                    return;
                }
                /** comprobamos que se hayan metido 2 digitos en la hora */
                int index= value.indexOf(TIME_SEP, 0);
                if (index != -1){
                    String hora= value.substring(0, index);
                    if (hora.length() != 2) keKey.consume();
                    return;
                }
            }else{
                int index= value.indexOf(TIME_SEP, 0);
                if (index != -1){
                    String hora= value.substring(0, index);
                    String min= "";
                    try{
                        min= value.substring(index+1, value.length());
                    }catch(Exception e){}

                    if (min.length() == 1){
                        /** comprobamos que el primer digito de los minutos no sea mayor que 5 */
                        int v= new Integer(min).intValue();
                        if (v > 5) keKey.consume();
                        return;
                    }
                    /** comprobamos que los minutos no supere el valor de 59 */
                    if (min.length() > 0){
                        int v= new Integer(min).intValue();
                        if (v > 59) keKey.consume();
                        return;
                    }
                }else{
                    if (value.length() == 1){
                        /** comprobamos que el primer digito no sea mayor que 1 */
                        if (new Integer(value).intValue() > 1) keKey.consume();
                        return;
                    }else if (value.length() > 2){
                        /** las horas no pueden superar los 2 digitos */
                        keKey.consume();
                        return;
                    }
                }
                return;
            }
        }

		keKey.consume();

	}

    public void keyPressed( KeyEvent keKey ){
        char	cKey	= keKey.getKeyChar();

        /** No es posible borrar un caracter, o de lo contrario
         * no podríamos hacer los chequeos, ya que para tener el valor actualizado tenemos
         * que annadir al valor del componente el carater tecleado. */
        if (cKey == KeyEvent.VK_BACK_SPACE){
            keKey.consume();
            return;
        }

    }

    public void keyReleased( KeyEvent keKey ){
    }


	/**
	 * Handles LOST_FOCUS event. When the component is about to loose the
	 * focus, this handler checks if the date entered is valid.
	 *
	 * @param	feKey	event object
	 */
	public void focusLost( FocusEvent feEvt )
	{
		Object	oText	= feEvt.getSource();
		String	szText;

		if( oText instanceof JTextField )
			szText = ((JTextField)oText).getText();
		else if( oText instanceof TextField )
			szText = ((TextField)oText).getText();
		else
			return;

		//if( szText.trim().length() == 0 )	// Replace with this cond for JDK1.1 application
		if( feEvt.isTemporary() || szText.trim().length() == 0 )
			return;

		Calendar	cCalendar;
		String		szTime;

		if( (cCalendar=TextConverter.getBaseHMTime(szText)) == null )
		{
			fireInvalidTimeEvent( new TimeEvent( oText, szText ) );
			szTime = "";
		}
		else
			szTime = TextConverter.toBaseHMString(cCalendar);

		if( oText instanceof JTextField )
			((JTextField)oText).setText(szTime);
		else if( oText instanceof TextField )
			((TextField)oText).setText(szTime);
	}

	/**
	 * Handlers INPUT_METHOD_TEXT_CHANGED event. This handler also checks if a
	 * invalid key is pressed. This handler is not necessary for JDK1.1, but
	 * in JDK1.2, 'keyTyped' event is never generated instead this event is
	 * generated so it has to be handled.
	 * <p>
	 * Note: In JDK1.1 this method should be commented.
	 *
	 * @param	imeEvt	event object
	 */
	public void inputMethodTextChanged( InputMethodEvent imeEvt )
	{
		AttributedCharacterIterator	aciIterator	= imeEvt.getText();

		for(char cChar = aciIterator.first();
			cChar != CharacterIterator.DONE;
			cChar = aciIterator.next()
		)
		{
			// If any one of the characters is not allowed,
			// consume the event ...
			if( Character.isDigit(cChar) || cChar == TIME_SEP ||
				cChar == KeyEvent.VK_PERIOD
			)
				continue;
			imeEvt.consume();
			return;
		}
		return;
	}
	/**/
}
