/*
 * Created on 30.04.2005.2005 for Pirol
 *
 * CVS header information:
 * $RCSfile: ColorsChooserPanel.java,v $
 * $Revision: 1.1 $
 * $Date: 2009/07/03 12:31:46 $
 * $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/dialogs/ColorsChooserPanel.java,v $
 */
package pirolPlugIns.dialogs;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import pirolPlugIns.i18n.PirolPlugInMessages;
import pirolPlugIns.utilities.colors.ColorGenerator;
import pirolPlugIns.utilities.colors.ColoringChangedListener;


/**
 * Creates a panel which shows a number of colors (default 3). Each of it can be changed with a standard
 * java JColorChooser dialog. Helpful for the new "ColorGenerator".
 * Also shows a JSpinner for choosing the number of fading steps.
 * 
 * @author Stefan Ostermann
 * <br>
 * <br>FH Osnabr&uuml;ck - University of Applied Sciences Osnabr&uuml;ck,
 * <br>Project: PIROL (2005),
 * <br>Subproject: Daten- und Wissensmanagement
 * 
 * @version $Revision: 1.1 $
 */
public class ColorsChooserPanel extends JPanel implements ActionListener, ChangeListener{

    private static final long serialVersionUID = -3946459666373852401L;

    protected int 					numOfColors = 3;	
	protected Color 				colors[] = DEFAULTCOLORS;
	protected JPanel  				buttonPanel = null;
	protected JPanel				defButtonPanel = null;
	protected SpinnerPanel 			spinnerPanel = null;
	protected JButton 				buttons[] = null;
	protected JButton				defaultButton = null;
	
	protected JLabel  				label = null;
	protected JPanel				spinnerButtonPanel = null;
	protected String 				labelText = PirolPlugInMessages.getString("please-select-colors-and-numbers"); //$NON-NLS-1$
	
	public static final int		    MAXSTEPS = 512;
	public static final int		    MINSTEPS = 3;
	protected static final int		DEFAULTSTEPS = 32;
	protected static final Color 	DEFAULTCOLORS[] = {Color.BLUE, Color.YELLOW, Color.RED};
	protected static final Color 	DEFAULTCOLOR = Color.BLACK;
	protected static final String  	DEFAULTCOMMAND = PirolPlugInMessages.getString("default"); //$NON-NLS-1$
    
    protected ArrayList colorListener = new ArrayList();
    
    protected boolean allowChangingTheNumberOfSteps = true;

    protected boolean fireEvents = true;
	
	/** Standard constructor. Uses default values for three colors.
	 */
	public ColorsChooserPanel() {
		super();
		setupPanel();
	}
	
	
	/** Constructor.
	 *  @param colors array of default colors. colors.length represents the number of colors.
	 */
	public ColorsChooserPanel(Color colors[]) {
		super();
		this.colors = colors;
		this.numOfColors = colors.length;
		setupPanel();
	}
	
	/** Constructor.
     *  @param colors array of default colors. colors.length represents the number of colors to be generated
     *  @param numOfColors number of colors which can be greater than the number stored in 'colors' (Array has to be fill with nulls!)
	 */
	public ColorsChooserPanel(Color colors[], int numOfColors) {
		super();
		this.colors = colors;
		this.numOfColors = numOfColors;
		setupPanel();
	}
	
	/** Constructor.
	 *  @param colors array of default colors. colors.length represents the number of colors to be generated
	 *  @param numOfColors number of colors which can be greater than the number stored in 'colors' (Array has to be fill with nulls!)
	 *  @param labelText Text which will appear in top of the JPanel.
	 */
	public ColorsChooserPanel(Color colors[], int numOfColors, String labelText) {
		super();
		this.colors = colors;
		this.numOfColors = numOfColors;
		this.labelText = labelText;
		setupPanel();
	}
	
	
	protected void setupPanel() {
		buttonPanel = new JPanel();
		spinnerPanel = new SpinnerPanel(DEFAULTSTEPS,MINSTEPS,MAXSTEPS,1,PirolPlugInMessages.getString("num-of-fading-steps")); //$NON-NLS-1$
        spinnerPanel.spinner.addChangeListener(this);
		buttonPanel.setLayout(new GridLayout(1,this.numOfColors));

		defaultButton = new JButton(PirolPlugInMessages.getString("set-to-defaults")); //$NON-NLS-1$
		defaultButton.setActionCommand(ColorsChooserPanel.DEFAULTCOMMAND);
		defaultButton.addActionListener(this);
		defButtonPanel = new JPanel (new FlowLayout(FlowLayout.RIGHT));
		defButtonPanel.add(defaultButton);
		spinnerButtonPanel = new JPanel(new GridLayout(1,2));
		spinnerButtonPanel.add(spinnerPanel);
		spinnerButtonPanel.add(defButtonPanel);
		
		buttons = new JButton[this.numOfColors];
		for (int i=0;i<this.numOfColors;i++) {
			if (i==0)
				buttons[i] = new JButton(PirolPlugInMessages.getString("low")); //$NON-NLS-1$
			else if (i==this.numOfColors-1)
				buttons[i] = new JButton(PirolPlugInMessages.getString("high")); //$NON-NLS-1$
			else
				buttons[i] = new JButton(PirolPlugInMessages.getString("middle")+i); //$NON-NLS-1$
			
			buttons[i].setContentAreaFilled(false);
			buttons[i].setOpaque(true);
			if (this.colors[i] != null) 
				buttons[i].setBackground(this.colors[i]);
			else//color not yet set:
				buttons[i].setBackground(DEFAULTCOLOR);
			buttons[i].addActionListener(this);
			buttons[i].setActionCommand(new Integer(i).toString());
			setButtonForeground(buttons[i]);
			buttonPanel.add(buttons[i]);
		}
		

		this.label = new JLabel(this.labelText);
		this.setLayout(new GridLayout(3,1));
		this.add(label);
		this.add(spinnerButtonPanel);
		this.add(buttonPanel);

	}
	
	/** 
	 * Sets the default color steps.
	 * @param steps
	 */
	public void setSteps(int steps) {
		if (allowChangingTheNumberOfSteps && steps<=MAXSTEPS && steps >= MINSTEPS) {
			this.spinnerPanel.setIntValue(steps);
            this.fireColoringChangedEvent();
		}
	}
    
    /** 
     * Sets the default color steps.
     * @param steps
     */
    public void setStepsNoChangeEvent(int steps) {
        if (allowChangingTheNumberOfSteps && steps<=MAXSTEPS && steps >= MINSTEPS) {
            this.spinnerPanel.setIntValue(steps);
        }
    }
	
	protected void setButtonForeground(JButton button) {
		if (button.getBackground().getGreen()<100&&button.getBackground().getBlue()<100&&
				button.getBackground().getRed()<100)
			button.setForeground(Color.WHITE);
		else
			button.setForeground(Color.BLACK);
	}
	
	public void actionPerformed(ActionEvent ae) {
		
		if (ae.getActionCommand()==ColorsChooserPanel.DEFAULTCOMMAND) {
			this.spinnerPanel.setIntValue((ColorsChooserPanel.DEFAULTSTEPS));
			for (int i=0;i<buttons.length;i++) {
				if (i<colors.length)
					this.colors[i] = ColorsChooserPanel.DEFAULTCOLORS[i];
				else this.colors[i] = ColorsChooserPanel.DEFAULTCOLOR;
				buttons[i].setBackground(this.colors[i]);
				this.setButtonForeground(buttons[i]);
			}
		}else {
		
			Color bufferColor;
			int num = Integer.parseInt(ae.getActionCommand());
			if (colors.length>num) {
				if (colors[num]==null)
					colors[num] = DEFAULTCOLOR;
				bufferColor = colors[num];
				colors[num] = JColorChooser.showDialog(null,PirolPlugInMessages.getString("choose-new-color"),colors[num]); //$NON-NLS-1$
				if (colors[num]==null)
					colors[num] = bufferColor;
			}
			buttons[num].setBackground(colors[num]);
			setButtonForeground(buttons[num]);
		}
        this.fireColoringChangedEvent();
	}
	
	
	/**
	 * @return Returns the colors-array.
	 */
	public Color[] getColors() {
		return colors;
	}
	/**
	 * Returns a specific color, if the color is not yet specified,
	 * this method returns the DEFAULTCOLOR.
	 * @param num number of color to return.
	 * @return the choosen color.
	 */
	public Color getColor(int num) {
		if (colors.length<num)
			return colors[num];
		else if (num<this.numOfColors)
			return DEFAULTCOLOR;
		else
			return null;
	}
	/**
	 * 
	 * @return Number of colors. It is possible that not all colors have a proper value, yet.
	 */
	public int getNumOfColors() {
		return numOfColors;
	}
	
	/**
	 * 
	 * @return The int-value specified by the JSpinner.
	 */
	public int getNumOfFadingSteps() {
		if (spinnerPanel!=null)
			return spinnerPanel.getIntValue();
		return 0;
	}
    
    public ColorGenerator createColorGenerator(){
        return new ColorGenerator(this.getNumOfFadingSteps(), this.getColors());
    }
    
    protected void fireColoringChangedEvent(){
        if (this.colorListener.isEmpty() || !this.fireEvents ) return;
        
        ColoringChangedListener[] listener = (ColoringChangedListener[]) this.colorListener.toArray(new ColoringChangedListener[0]);
        
        ColorGenerator colGen = this.createColorGenerator(); 
        
        for (int i=0; i<listener.length; i++){
            listener[i].coloringChanged(colGen);
        }        
    }
    
    /**
     * Adds a listener that reacts on changing coloring attributes to this panel
     *@param listener
     */
    public void addColoringListener(ColoringChangedListener listener){
        this.colorListener.add(listener);        
    }
    
    /**
     * Removes a listener that reacts on changing coloring attributes to this panel
     *@param listener
     */
    public void removeColoringListener(ColoringChangedListener listener){
        this.colorListener.remove(listener);        
    }


    /**
     *@param arg0
     */
    public void stateChanged(ChangeEvent arg0) {
        if (this.allowChangingTheNumberOfSteps)
            this.fireColoringChangedEvent();
    }


    public boolean isAllowChangingTheNumberOfSteps() {
        return allowChangingTheNumberOfSteps;
    }


    public void setAllowChangingTheNumberOfSteps(boolean allowChangingTheNumberSteps) {
        this.allowChangingTheNumberOfSteps = allowChangingTheNumberSteps;
        this.spinnerPanel.spinner.setEnabled(allowChangingTheNumberSteps);
    }
    
    public void setColorGenerator(ColorGenerator colGen, boolean fireEvents){
        boolean wasFiringEvents = this.fireEvents;
        this.fireEvents = fireEvents;
        this.setSteps(colGen.getSteps());
        this.colors = colGen.getInputColorsAsArray();
        
        for (int i=0; i<this.buttons.length; i++){
            buttons[i].setBackground(colors[i]);
            setButtonForeground(buttons[i]);
        }
        
        this.fireEvents = wasFiringEvents;
    }
    
    
}
