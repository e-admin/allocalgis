/**
 * @(#) JNumberTextField.java	1.0	99/06/20
 *
 * This code is designed for JDK1.2
 * For running code in JDK1.1 some changes will have to be done.
 * Use tab spacing 4. Follow JavaDoc convention while coding.
 * Mail any suggestions or bugs to unicman@iname.com
 */
package com.geopista.app.utilidades;

import	com.geopista.app.utilidades.filteredText.*;
import	com.geopista.app.utilidades.event.NumberListener;

import	javax.swing.JTextField;		// For JDK1.1 change to swing package
import	java.awt.event.KeyEvent;
import	java.text.ParseException;

import com.geopista.app.utilidades.filteredText.NumberAdapter;

/**
 * This class accepts number input from the user. This class actually doesn't
 * contain any logic for filtering/validating time. All the functionality is
 * stored in the corresponding number adapters.
 * <p>
 * This class is extended from Swing (JFC) component, so if you don't want to
 * use swing (JFC), u can use the adapters separately by adding KeyListener,
 * FocusListener and InputMethodListener (JDK1.2 onwards) to the TextField
 * component.
 * <p>
 * This class can be used for accepting following number inputs ...
 * <ul>
 * <li>NUMBER - Allows integer number in the format (NNNNN / N,NNN,NNN)
 * <li>REAL - Allows real number in the format (NNNN.NNN / N,NNN,NNN.NNN)
 * <li>SCIENTIFIC - Allows real number and also allows '3e-1' kind of
 * specifications (NNNN.NNN / N,NNN,NNN.NNN / NNNNeNNNN)
 * <li>CURRENCY - Allows currency input (NNNN.NN / N,NNN,NNN.NN)
 * </ul>
 * If invalid time is entered by the user, control can be gained by assigning
 * NumberListener to this component. Only one NumberListener can be attached to
 * the component. And once the NumberListener is set, it will be retained even
 * if adapter is changed using setFilter method. If u want to disassociate the
 * NumberListener, invoke clearNumberListener method.
 * <p>
 * Custom UMNumberFormat can be specified to the text-field for controling which
 * Number (string) is valid, and how to show the Number in the text-field. As
 * default formats for each adapter is different, if you change the adapter,
 * you will have to set the custom UMNumberFormat again.
 *
 * @version	1.0	06/20/99
 * @author	UnicMan
 */
public class JNumberTextField
	extends		JTextField
{
	public static final int NONE		= 0;
	public static final int NUMBER		= 1;
	public static final int REAL		= 2;
	public static final int SCIENTIFIC	= 3;
	public static final int CURRENCY	= 4;
	public static final int BASE_HM	    = 4;

	public static final String	INVALID	= "Invalid";

	private	int	miFilter					= NONE;
	private NumberAdapter	mnaCurrent		= null;
    private Number maxValue  = null;
    private int precision= 0;

	/**
	 * Default constructor.
	 */
	public JNumberTextField()
	{
		this( NUMBER );
	}

	/**
	 * Constructor specifying restriction to apply on specification of number.
	 *
	 * @param	iFilter	restriction ( NUMBER / REAL / SCIENTIFIC / CURRENCY )
	 */
	public JNumberTextField( int iFilter )
	{
		setFilter( iFilter );
		setHorizontalAlignment( JTextField.RIGHT );
	}

	public JNumberTextField( int iFilter, Number nMaxValue )
	{
        maxValue=nMaxValue;
		setFilter( iFilter );
		//setNumber( nNumber );
		setHorizontalAlignment( JTextField.RIGHT );
	}

    public JNumberTextField( int iFilter, Number nMaxValue, boolean left )
    {
        maxValue=nMaxValue;
        setFilter( iFilter );
        //setNumber( nNumber );
        if (left)
            setHorizontalAlignment( JTextField.LEFT);
        else
            setHorizontalAlignment( JTextField.RIGHT);
    }


    public JNumberTextField( int iFilter, Number nMaxValue, boolean left, int nPrecision )
    {
        maxValue=nMaxValue;
        precision= nPrecision;
        setFilter( iFilter );
        //setNumber( nNumber );
        if (left)
            setHorizontalAlignment( JTextField.LEFT);
        else
            setHorizontalAlignment( JTextField.RIGHT);
    }

	/**
	 * Retrieves the current NumberListener associated with the current adapter.
	 * If no listener is attached to the current adapter, null will be
	 * returned.
	 *
	 * @return	NumberListener object
	 */
	public NumberListener getNumberListener()
	{
		return	mnaCurrent.getNumberListener();
	}


	public void setNumberListener( NumberListener nlListener )
	{
		mnaCurrent.setNumberListener( nlListener );
	}

	/**
	 * Disassociates the NumberListener from the adapter. This method just sets
	 * null NumberListener as the new Listener, by which the previous Listener
	 * will be automatically disassociated.
	 */
	public void clearNumberListener()
	{
		mnaCurrent.setNumberListener( null );
	}

	/**
	 * Retrieves the current UMNumberFormat associated with the current adapter.
	 *
	 * @return	UMNumberFormat object
	 */
	public UMNumberFormat getNumberFormat()
	{
		return	mnaCurrent.getNumberFormat();
	}

	public void setNumberFormat( UMNumberFormat nfFormat )
	{
		mnaCurrent.setNumberFormat( nfFormat );
	}

	/**
	 * Does all the initialization of the component.
	 *
	 * @param	iFilter	filter for which initialization is to be carried out
	 */
	public void setFilter( int iFilter )
	{
		boolean			bSignAllowed;
		NumberListener	nlListener;

		if( mnaCurrent != null )
		{
			nlListener		= mnaCurrent.getNumberListener();
			bSignAllowed	= mnaCurrent.isSignAllowed();
		}
		else
		{
			nlListener		= null;
			bSignAllowed	= true;
		}

		removeKeyListener( mnaCurrent );
		removeFocusListener( mnaCurrent );
		removeInputMethodListener( mnaCurrent );	// Comment for JDK1.1
		switch( iFilter )
		{
		case NUMBER:
			mnaCurrent = new IntegerNumberAdapter(maxValue);
			break;
		case REAL:
            if (precision==0){
                mnaCurrent = new RealNumberAdapter(maxValue);
            }else{
                mnaCurrent = new RealNumberAdapter(maxValue, precision);
            }
			break;
		case SCIENTIFIC:
			mnaCurrent = new ScientificNumberAdapter();
			break;
		case CURRENCY:
			mnaCurrent = new CurrencyNumberAdapter(maxValue);
			break;
		default:
			mnaCurrent = null;
		}

		miFilter = iFilter;
		if( mnaCurrent == null )
			return;

		addKeyListener( mnaCurrent );
		addFocusListener( mnaCurrent );
		addInputMethodListener( mnaCurrent );	// Comment for JDK1.1


		mnaCurrent.setSignAllowed( bSignAllowed );
		mnaCurrent.setNumberListener( nlListener );
	}

	/**
	 * Sets the value of this field to specified number. The number format for
	 * showing the number is determined by the specified filter.
	 *
	 * @param	nNumber	number to set for this text field
	 */
	public void setNumber( Number nNumber )
	{
		setText( mnaCurrent.getNumberFormat().formatNumber(nNumber) );
	}


    public void setNumberWithoutFormat(Number nNumber)
    {
        setText(nNumber.toString());
    }

	/**
	 * Retrieves the number entered in the text field. If invalid number is
	 * entered, this function returns null.
	 *
	 * @return	entered number
	 */
	public Number getNumber()
		throws	ParseException
	{
		return	mnaCurrent.getNumberFormat().parseNumber( getText() );
	}

	/**
	 * Sets if the sign is allowed or not.
	 *
	 * @param	bSignAllowed	true - sign allowed, false - not allowed
	 */
	public void setSignAllowed( boolean bSignAllowed )
	{
		mnaCurrent.setSignAllowed( bSignAllowed );
	}

	/**
	 * Checks if this component allows sign.
	 *
	 * @return	true - sign allowed, false - not allowed
	 */
	public boolean isSignAllowed()
	{
		return	mnaCurrent.isSignAllowed();
	}
}
