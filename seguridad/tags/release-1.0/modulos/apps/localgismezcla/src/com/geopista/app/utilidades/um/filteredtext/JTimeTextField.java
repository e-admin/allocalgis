/**
 * @(#) JTimeTextField.java	1.0	99/06/10
 *
 * This code is designed for JDK1.2
 * For running code in JDK1.1 some changes will have to be done.
 * Use tab spacing 4. Follow JavaDoc convention while coding.
 * Mail any suggestions or bugs to unicman@iname.com
 */
package	com.geopista.app.utilidades.um.filteredtext;

import	com.geopista.app.utilidades.um.filteredtext.time.*;
import	com.geopista.app.utilidades.um.event.TimeListener;
import	com.geopista.app.utilidades.um.util.TextConverter;

import	javax.swing.JTextField;		// For JDK1.1 change to swing package
import	java.awt.event.KeyEvent;
import	java.util.Calendar;
import	java.util.Date;

/**
 * This class accepts time input from the user. This class actually doesn't
 * contain any logic for filtering/validating time. All the functionality is
 * stored in the corresponding time adapters.
 * <p>
 * This class is extended from Swing (JFC) component, so if you don't want to
 * use swing (JFC), u can use the adapters separately by adding KeyListener,
 * FocusListener and InputMethodListener (JDK1.2 onwards) to the TextField
 * component.
 * <p>
 * The time can be specified in one of the specified ways -
 * <ul>
 * <li>ANY - Any type of time will be allowed
 * <li>BASE12 - Allows time in the format (HH:MM:SS.SSxx) 'xx' is AM/PM
 * <li>BASE24 - Allows time in the format (HH:MM:SS.SS)
 * </ul>
 * If invalid time is entered by the user, control can be gained by assigning
 * TimeListener to this component. Only one TimeListener can be attached to
 * the component. And once the TimeListener is set, it will be retained even
 * if adapter is changed using setFilter method. If u want to disassociate the
 * TimeListener, invoke clearTimeListener method.
 *
 * @version	1.0	06/10/99
 * @author	UnicMan
 */
public class JTimeTextField
	extends		JTextField
{
	public static final int NONE		= 0;
	public static final int BASE24		= 1;
	public static final int BASE12		= 2;
	public static final int ANY			= 3;
    /** charo */
    public static final int BASE_HM	    = 4;


    public static final String	INVALID	= "Invalid";
	private	int	miFilter					= NONE;
	private TimeAdapter	mtaCurrent			= null;


	/**
	 * Default constructor.
	 */
	public JTimeTextField()
	{
		this( ANY );
	}
	
	/**
	 * Constructor specifying restriction to apply on specification of date. 
	 *
	 * @param	iFilter	restriction ( ANY / BASE12 / BASE24 )
	 */
	public JTimeTextField( int iFilter )
	{
		setFilter( iFilter );
	}
	
	/**
	 * Constructor specifying Time to show. Initially the specified time field
	 * will be showed in the field. The filter will be used to determine how
	 * to show the time in the field.
	 *
	 * @param	dTime	time to show
	 * @param	iFilter	restriction and also specifies how to show the date.
	 *					In case of ANY, BASE12 will be used for showing date
	 */
	public JTimeTextField( int iFilter, Date dTime )
	{
		setFilter( iFilter );
		if( dTime != null )
		{
			Calendar	cTime = Calendar.getInstance();
			cTime.setTime(dTime);
			setTime( cTime );
		}
	}
	
	/**
	 * Constructor specifying Time to show. Initially the specified time field
	 * will be showed in the field. The filter will be used to determine how
	 * to show the time in the field.
	 *
	 * @param	cTime	time to show
	 * @param	iFilter	restriction and also specifies how to show the time.
	 *					In case of ANY, BASE12 will be used for showing time
	 */
	public JTimeTextField( int iFilter, Calendar cTime )
	{
		setFilter( iFilter );
		if( cTime != null )
			setTime( cTime );
	}

	/**
	 * Retrieves the current DateListener associated with the current adapter.
	 * If no listener is attached to the current adapter, null will be
	 * returned.
	 *
	 * @return	DateListener object
	 */
	public TimeListener getTimeListener()
	{
		return	mtaCurrent.getTimeListener();
	}

	/**
	 * Sets the TimeListener to the current adapter. Setting null TimeListener
	 * will disassociate the previous TimeListener.
	 * 
	 * @return	TimeListener	new TimeListener
	 */
	public void setTimeListener( TimeListener tlListener )
	{
		mtaCurrent.setTimeListener( tlListener );
	}

	/**
	 * Disassociates the TimeListener from the adapter. This method just sets
	 * null TimeListener as the new Listener, by which the previous Listener
	 * will be automatically disassociated.
	 */
	public void clearTimeListener()
	{
		mtaCurrent.setTimeListener( null );
	}

	/**
	 * Does all the initialization of the component.
	 */
	public void setFilter( int iFilter )
	{
		TimeListener	tlListener;
		
		if( mtaCurrent != null )
			tlListener = mtaCurrent.getTimeListener();
		else
			tlListener = null;

		removeKeyListener( mtaCurrent );
		removeFocusListener( mtaCurrent );
		removeInputMethodListener( mtaCurrent );	// Comment for JDK1.1
		switch( iFilter )
		{
		case BASE24:
			mtaCurrent = new Base24TimeAdapter();
			break;
		case BASE12:
			mtaCurrent = new Base12TimeAdapter();
			break;
        case BASE_HM:
            mtaCurrent = new BaseHMTimeAdapter(this);
            break;
		default:
			mtaCurrent = null;
		}

		miFilter = iFilter;
		if( mtaCurrent == null )
			return;

		addKeyListener( mtaCurrent );
		addFocusListener( mtaCurrent );
		addInputMethodListener( mtaCurrent );	// Comment for JDK1.1

		mtaCurrent.setTimeListener( tlListener );
	}

	/**
	 * Sets the value of this field to specified time. The time format for
	 * showing the time is determined by the specified filter.
	 *
	 * @param	cTime	time to set for this text field
	 */
	public void setTime( Calendar cTime )
	{
		switch( miFilter )
		{
		case BASE24:
			setText( TextConverter.toBase24String(cTime) );
			break;
		default:
			setText( TextConverter.toBase12String(cTime) );
			break;
		}
	}

	/**
	 * Retrieves the time entered in the text field. If invalid time is
	 * entered, this function returns null.
	 *
	 * @return	entered time
	 */
	public Calendar getTime()
	{
		String	szText	= getText();
		switch( miFilter )
		{
		case BASE24:
			return	TextConverter.getBase24Time(szText);
		case BASE12:
			return	TextConverter.getBase12Time(szText);
		case ANY:
			Calendar	cCal = TextConverter.getBase12Time(szText);
			if( cCal != null )
				return	cCal;
			else
				return	TextConverter.getBase24Time(szText);
		default:
			return	null;
		}
	}
}
