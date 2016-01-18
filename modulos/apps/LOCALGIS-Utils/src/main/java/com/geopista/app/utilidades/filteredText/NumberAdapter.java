/**
 * NumberAdapter.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * @(#) NumberAdapter.java	1.0	99/06/17
 *
 * This code is designed for JDK1.2
 * For using this code in JDK1.1 some changes will have to be done.
 * Use tab spacing 4. Follow JavaDoc convention while coding.
 * Mail any suggestions or bugs to unicman@iname.com
 */
package	 com.geopista.app.utilidades.filteredText;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.geopista.app.utilidades.event.NumberEvent;
import com.geopista.app.utilidades.event.NumberListener;
// For JDK1.1 comment this line.
// For JDK1.1 comment this line.

/**
 * This class is base class for all the number adapters. This class is used to
 * store the instance of any type of number adapter, so there is no special code
 * in this class.
 *
 * This class can hold a NumberListener object which is used by all the classes
 * which are extended from this class. NumberListener is important when invalid
 * number is entered.
 *
 * @version	1.0	06/17/99
 * @author	UnicMan
 */
public class NumberAdapter
	implements	KeyListener,
				InputMethodListener,		// For JDK1.1 comment this line.
				FocusListener
{
	private NumberListener	mnlListener		= null;
	private boolean			mbSignAllowed	= true;
	private	UMNumberFormat	mnfFormat		= null;

	/**
	 * Checks if sign is allowed or not.
	 *
	 * @return	true - if sign will be allowed, false otherwise
	 */
	public boolean isSignAllowed()
	{
		return	mbSignAllowed;
	}

	/**
	 * Sets if the sign should be allowed or not.
	 *
	 * @param	bSignAllowed	true - sign allowed, false - not allowed
	 */
	public void setSignAllowed( boolean bSignAllowed )
	{
		mbSignAllowed	= bSignAllowed;
	}

	/**
	 * Returns the NumberListener associated with the adapter.
	 *
	 * @return	associated NumberListener
	 */
	public NumberListener getNumberListener()
	{
		return	mnlListener;
	}

	/**
	 * Sets new NumberListener for this adapter.
	 *
	 * @param	nlListener	NumberListener object
	 */
	public void setNumberListener( NumberListener nlListener )
	{
		mnlListener = nlListener;
	}

	/**
	 * Checks if any NumberListener is attached to the adapter.
	 *
	 * @return	true if attached, false otherwise
	 */
	public boolean isNumberListenerAttached()
	{
		return	(mnlListener != null);
	}

	/**
	 * Returns the NumberFormat associated with the adapter.
	 *
	 * @return	associated UMNumberFormat
	 */
	public UMNumberFormat getNumberFormat()
	{
		return	mnfFormat;
	}

	/**
	 * Sets new NumberFormat for this adapter.
	 *
	 * @param	nfFormat	UMNumberFormat object
	 */
	public void setNumberFormat( UMNumberFormat nfFormat )
	{
		mnfFormat	= nfFormat;
	}

	/**
	 * Invokes the gotInvalidNumber method of the associated listener. If no
	 * listener is associated, this method does nothing.
	 *
	 * @param	neEvt	date event to fire
	 */
	protected void fireInvalidNumberEvent( NumberEvent neEvt )
	{
		if( mnlListener == null )
			return;
		mnlListener.gotInvalidNumber( neEvt );
	}

	////
	// Below are all the interface function which should be overridden if
	// necessary...
	////

	public void keyPressed( KeyEvent keEvt )	{}
	public void keyReleased( KeyEvent keEvt )	{}
	public void keyTyped( KeyEvent keEvt )		{}

	public void focusGained( FocusEvent feEvt )	{}
	public void focusLost( FocusEvent feEvt )	{}

	// Comment out for JDK1.1 ...
	/**/
	public void caretPositionChanged( InputMethodEvent imeEvt )		{}
	public void inputMethodTextChanged( InputMethodEvent imeEvt )	{}
	/**/
}
