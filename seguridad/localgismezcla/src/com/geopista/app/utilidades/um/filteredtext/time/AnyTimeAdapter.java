/**
 * @(#) AnyTimeAdapter.java	1.0	99/06/11
 *
 * This code is designed for JDK1.2
 * For running code in JDK1.1 some changes will have to be done.
 * Use tab spacing 4. Follow JavaDoc convention while coding.
 * Mail any suggestions or bugs to unicman@iname.com
 */
package	com.geopista.app.utilidades.um.filteredtext.time;

import	com.geopista.app.utilidades.um.event.TimeEvent;
import	com.geopista.app.utilidades.um.util.TextConverter;
import	com.geopista.app.utilidades.um.util.UMConstants;

import	javax.swing.JTextField;					// For JDK1.1 give swing package
import	java.awt.TextField;
import	java.awt.event.KeyEvent;
import	java.awt.event.FocusEvent;
import	java.awt.event.InputMethodEvent;		// For JDK1.1 comment this line
import	java.text.AttributedCharacterIterator;	// For JDK1.1 comment this line
import	java.text.CharacterIterator;			// For JDK1.1 comment this line
import	java.util.Calendar;

/**
 * This class acts as filter adapter for time. This class implements listeners
 * necessary for restricting keys allowed for time. Also it validates the
 * entered time when the field looses focus.
 * <p>
 * The time format this adapter sticks to is HH:MM:SSxx (where 'xx' is AM/PM).
 * Here the hours can be 12 based or 24 based. If no AM/PM is specified, 24
 * based time is assumed.
 * <p>
 * If validation fails and if the TimeListener for this adapter is attached,
 * gotInvalidTime method of the listener is invoked. So the user of this class
 * can have control if a invalid Time is pressed.
 *
 * @version	1.0	06/11/99
 * @author	UnicMan
 */
public class AnyTimeAdapter
	extends		com.geopista.app.utilidades.um.filteredtext.time.TimeAdapter
	implements	UMConstants
{
	/**
	 * Handles KEY_TYPED event. This handler checks for the valid keys and
	 * consumes the event if the key in not allowed.
	 *
	 * @param	keKey	event object
	 */
	public void keyTyped( KeyEvent keKey )
	{
		char	cKey	= keKey.getKeyChar();

		if( Character.isDigit(cKey) || cKey == TIME_SEP ||
			cKey == KeyEvent.VK_PERIOD || cKey == KeyEvent.VK_BACK_SPACE
		)
			return;

		cKey	= Character.toUpperCase(cKey);
		
		if(	cKey == KeyEvent.VK_A || cKey == KeyEvent.VK_P ||
			cKey == KeyEvent.VK_M
		 )
			return;
		keKey.consume();
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

		if( (cCalendar=TextConverter.getBase12Time(szText)) != null ||
			(cCalendar=TextConverter.getBase24Time(szText)) != null
		)
			szTime = TextConverter.toBase12String(cCalendar);
		else
		{
			fireInvalidTimeEvent( new TimeEvent( oText, szText ) );
			szTime = "";
		}

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
			
			cChar	= Character.toUpperCase( cChar );
				
			if(	cChar == KeyEvent.VK_A || cChar == KeyEvent.VK_P ||
				cChar == KeyEvent.VK_M
			)
				continue;
			imeEvt.consume();
			return;
		}
		return;
	}
	/**/
}
