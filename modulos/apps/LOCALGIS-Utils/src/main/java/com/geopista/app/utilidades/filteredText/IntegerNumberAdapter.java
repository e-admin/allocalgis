/**
 * IntegerNumberAdapter.java
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

import com.geopista.app.utilidades.JNumberTextField;
import com.geopista.app.utilidades.event.NumberEvent;
import com.geopista.app.utilidades.event.NumberListener;

/**
 * This class acts as filter adapter for Integer numbers. This class
 * implements listeners necessary for restricting keys allowed for time. Also
 * it validates the entered time when the field looses focus.
 * <p>
 * The number format that are acceptable are NNNN (all numbers) or N,NNN,NNN
 * (numbers separated by comma).
 * <p>
 * If validation fails and if the NumberListener for this adapter is attached,
 * gotInvalidNumber method of the listener is invoked. So the user of this class
 * can have control if a invalid Number is pressed.
 *
 * @version	1.0	06/19/99
 * @author	UnicMan
 */
public class IntegerNumberAdapter
	extends		NumberAdapter
{
    private Number maxValue;
    private String _lastValue= "";
	/**
	 * Default constructor. If this constructor is used, signs will be allowed
	 * by default.
	 */
	public IntegerNumberAdapter()
	{
		setNumberFormat( new IntegerNumberFormat() );
	}

    /**
     * Default constructor. If this constructor is used, signs will be allowed
     * by default.
     */
    public IntegerNumberAdapter(Number nMaxValue)
    {
        maxValue=nMaxValue;
        setNumberFormat( new IntegerNumberFormat() );

    }

	/**
	 * Constructor specifying NumberListener. Assigns specified NumberListener
	 * at the construction time.
	 *
	 * @param	nlListener	number listener object
	 */
	public IntegerNumberAdapter( NumberListener nlListener )
	{
		setNumberListener( nlListener );
		setNumberFormat( new IntegerNumberFormat() );
	}

	/**
	 * Constructor which specifies whether sign is allowed or not.
	 *
	 * @param	bSignAllowed	flag specifying whether sign is allowed or not
	 */
	public IntegerNumberAdapter( boolean bSignAllowed )
	{
		setSignAllowed( bSignAllowed );
		setNumberFormat( new IntegerNumberFormat() );
	}

	/**
	 * Constructor specifying NumberListener and whether to allow sign or not.
	 *
	 * @param	nlListener		number listener object
	 * @param	bSignAllowed	flag specifying whether sign is allowed or not
	 */
	public IntegerNumberAdapter( NumberListener nlListener, boolean bSignAllowed )
	{
		setNumberListener( nlListener );
		setNumberFormat( new IntegerNumberFormat() );
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
        if (maxValue!=null)
        {
            Object	oText	= keKey.getSource();
            if( oText instanceof JTextField )
            {
                String szText = ((JTextField)oText).getText();

                try
                {
                    //int valor=getNumberFormat().parseNumber(szText).intValue();
                    long valor=new Long(szText).longValue();
                    if (valor>maxValue.intValue())
                    {
                        keKey.consume();
                        return;
                    }
                }catch(Exception e){}
            }
        }

		//if( Character.isDigit(cKey) || cKey == KeyEvent.VK_BACK_SPACE ||
		//	cKey == KeyEvent.VK_COMMA ||
		//	(isSignAllowed() && cKey == KeyEvent.VK_MINUS)
		// )
        if( Character.isDigit(cKey)
                 || cKey == KeyEvent.VK_BACK_SPACE
                  || cKey ==KeyEvent.VK_DELETE
                  || cKey ==KeyEvent.VK_ENTER
                   ||cKey==KeyEvent.VK_TAB
                ||(isSignAllowed() && cKey == KeyEvent.VK_MINUS))
        	return;
		keKey.consume();
	}


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
         try
        {
        //if (getNumberFormat().parseNumber(szText).intValue()>maxValue.intValue())
        if (new Long(szText).longValue()>maxValue.intValue())
        {
            if (oText instanceof JNumberTextField)
            {
                ((JNumberTextField)oText).setNumber((Integer)maxValue);
                szText=((JNumberTextField)oText).getText();
            }
        }
            }catch (Exception e)
        {
        }

		//if( szText.trim().length() == 0 )	// Replace with this cond for JDK1.1 application
		if( feEvt.isTemporary() || szText.trim().length() == 0 )
			return;

		Number			nNumber;
		String			szNumber;
		UMNumberFormat	nfFormat	= getNumberFormat();

		try
		{
			nNumber		= nfFormat.parseNumber( szText );
			szNumber	= nfFormat.formatNumber( nNumber );
		}
		catch( ParseException e )
		{
			fireInvalidNumberEvent( new NumberEvent( oText, szText ) );
			szNumber = "";
		}

		if( oText instanceof JTextField )
			((JTextField)oText).setText(szNumber);
		else if( oText instanceof TextField )
			((TextField)oText).setText(szNumber);
	}

    public void keyReleased(java.awt.event.KeyEvent evt) {

        Object	oText	= evt.getSource();

        try{
            if( oText instanceof JNumberTextField ){
                if (((JNumberTextField)oText).getDocument() != null){
                    long lText = ((JNumberTextField)oText).getNumber().longValue();

                    if (new Long(lText).longValue() >  maxValue.intValue()){
                        ((JNumberTextField)oText).setText(_lastValue);
                    }else if (new Long(lText).longValue() <=  maxValue.intValue()){
                        _lastValue= ((JNumberTextField)oText).getText();
                    }
                }
            }
        }catch (Exception e){}

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
				(isSignAllowed() && cChar == KeyEvent.VK_MINUS)
			)
				continue;
			imeEvt.consume();
			return;
		}
		return;
	}
	/**/

}
