/**
 * @(#) TimeAdapter.java	1.0	99/06/08
 *
 * This code is designed for JDK1.2
 * For using this code in JDK1.1 some changes will have to be done.
 * Use tab spacing 4. Follow JavaDoc convention while coding.
 * Mail any suggestions or bugs to unicman@iname.com
 */
package	com.geopista.app.utilidades.um.filteredtext.time;

import	com.geopista.app.utilidades.um.event.TimeEvent;
import	com.geopista.app.utilidades.um.event.TimeListener;

import	java.awt.event.KeyEvent;
import	java.awt.event.KeyListener;
import	java.awt.event.InputMethodEvent;	// For JDK1.1 comment this line.
import	java.awt.event.InputMethodListener;	// For JDK1.1 comment this line.
import	java.awt.event.FocusEvent;
import	java.awt.event.FocusListener;

/**
 * This class is base class for all the time adapters. This class is used to
 * store the instance of any type of time adapter, so there is no special code
 * in this class.
 *
 * This class can hold a TimeListener object which is used by all the classes
 * which are extended from this class. TimeListener is important when invalid
 * time is entered.
 *
 * @version	1.0	06/08/99
 * @author	UnicMan
 */
public class TimeAdapter
	implements	KeyListener,
				InputMethodListener,		// For JDK1.1 comment this line.
				FocusListener
{
	private TimeListener	mtlListener	= null;

	/**
	 * Default constructor. This constructor will not set any default
	 * TimeListener to the adapter. So the errors will be just ignored.
	 */
	public TimeAdapter()	{}
	
	/**
	 * Constructor with TimeListener. With this constructor any TimeListener
	 * can be set at the construction time only.
	 *
	 * @param	tlListener	TimeListener object
	 */
	public TimeAdapter( TimeListener tlListener )
	{
		mtlListener = tlListener;
	}

	/**
	 * Returns the TimeListener associated with the adapter.
	 * 
	 * @return	associated TimeListener
	 */
	public TimeListener getTimeListener()
	{
		return	mtlListener;
	}

	/**
	 * Sets new TimeListener for this adapter.
	 *
	 * @param	tlListener	TimeListener object
	 */
	public void setTimeListener( TimeListener tlListener )
	{
		mtlListener = tlListener;
	}

	/**
	 * Checks if any TimeListener is attached to the adapter.
	 *
	 * @return	true if attached, false otherwise
	 */
	public boolean isTimeListenerAttached()
	{
		return	(mtlListener != null);
	}
	
	/**
	 * Invokes the gotInvalidTime method of the associated listener. If no
	 * listener is associated, this method does nothing.
	 *
	 * @param	teEvt	time event to fire
	 */
	protected void fireInvalidTimeEvent( TimeEvent teEvt )
	{
		if( mtlListener == null )
			return;
		mtlListener.gotInvalidTime( teEvt );
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
