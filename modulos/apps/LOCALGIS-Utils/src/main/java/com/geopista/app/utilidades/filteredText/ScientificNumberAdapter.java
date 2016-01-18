/**
 * ScientificNumberAdapter.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.utilidades.filteredText;

import java.awt.TextField;
import java.awt.event.FocusEvent;
import java.awt.event.InputMethodEvent;
import java.awt.event.KeyEvent;
import java.text.AttributedCharacterIterator;
import java.text.CharacterIterator;
import java.text.ParseException;

import javax.swing.JTextField;

import com.geopista.app.utilidades.event.NumberEvent;
import com.geopista.app.utilidades.event.NumberListener;

/**
 * @(#) ScientificNumberAdapter.java	1.0	99/07/27
 *
 * This code is designed for JDK1.2
 * For running code in JDK1.1 some changes will have to be done.
 * Use tab spacing 4. Follow JavaDoc convention while coding.
 * Mail any suggestions or bugs to unicman@iname.com
 */
/**
 * This class acts as filter adapter for scientific numbers. This class
 * implements listeners necessary for restricting keys allowed for time. Also
 * it validates the entered time when the field looses focus.
 * <p>
 * The number format that are acceptable are NNNN.NN (all numbers) or
 * N,NNN,NNN.NN (numbers separated by comma) or NNNN.NNeNN (with exponent).
 * <p>
 * If validation fails and if the NumberListener for this adapter is attached,
 * gotInvalidNumber method of the listener is invoked. So the user of this class
 * can have control if a invalid Number is pressed.
 *
 * @version	1.0	07/27/99
 * @author	UnicMan
 */
public class ScientificNumberAdapter
	extends		NumberAdapter
{
	/**
	 * Default constructor. If this constructor is used, signs will be allowed
	 * by default.
	 */
	public ScientificNumberAdapter()
	{
		setNumberFormat( new ScientificNumberFormat() );
	}

	/**
	 * Constructor specifying NumberListener. Assigns specified NumberListener
	 * at the construction time.
	 *
	 * @param	nlListener	number listener object
	 */
	public ScientificNumberAdapter( NumberListener nlListener )
	{
		setNumberFormat( new ScientificNumberFormat() );
		setNumberListener( nlListener );
	}

	/**
	 * Constructor which specifies whether sign is allowed or not.
	 *
	 * @param	bSignAllowed	flag specifying whether sign is allowed or not
	 */
	public ScientificNumberAdapter( boolean bSignAllowed )
	{
		setNumberFormat( new ScientificNumberFormat() );
		setSignAllowed( bSignAllowed );
	}

	/**
	 * Constructor specifying NumberListener and whether to allow sign or not.
	 *
	 * @param	nlListener		number listener object
	 * @param	bSignAllowed	flag specifying whether sign is allowed or not
	 */
	public ScientificNumberAdapter( NumberListener nlListener, boolean bSignAllowed )
	{
		setNumberFormat( new ScientificNumberFormat() );
		setNumberListener( nlListener );
		setSignAllowed( bSignAllowed );
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

		if( Character.isDigit(cKey) || cKey == KeyEvent.VK_BACK_SPACE ||
			cKey == KeyEvent.VK_PERIOD || cKey == KeyEvent.VK_COMMA ||
			cKey == KeyEvent.VK_MINUS || Character.toUpperCase(cKey) == KeyEvent.VK_E
		 )
			return;
		keKey.consume();
	}

	/**
	 * Handles LOST_FOCUS event. When the component is about to loose the
	 * focus, this handler checks if the date entered is valid.
	 *
	 * @param	feEvt
     * eKey	event object
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

		Number			nNumber;
		String			szNumber	= "";
		UMNumberFormat	nfFormat	= getNumberFormat();

		try
		{
			nNumber		= nfFormat.parseNumber( szText );

			if( !isSignAllowed() && nNumber.longValue() < 0 )
				fireInvalidNumberEvent( new NumberEvent( oText, szText ) );
			else
				szNumber	= nfFormat.formatNumber( nNumber );
		}
		catch( ParseException e )
		{
			fireInvalidNumberEvent( new NumberEvent( oText, szText ) );
		}

		if( oText instanceof JTextField )
			((JTextField)oText).setText(szNumber);
		else if( oText instanceof TextField )
			((TextField)oText).setText(szNumber);
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
			if( Character.isDigit(cChar) || cChar == KeyEvent.VK_COMMA ||
				cChar == KeyEvent.VK_PERIOD || cChar == KeyEvent.VK_MINUS ||
				Character.toUpperCase(cChar) == KeyEvent.VK_E
			)
				continue;
			imeEvt.consume();
			return;
		}
		return;
	}
	/**/
}
