/*
 * Created on 25.10.2005 for PIROL
 *
 * CVS header information:
 *  $RCSfile: DateChooserPanel.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:46 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/dialogs/DateChooserPanel.java,v $
 */
package pirolPlugIns.dialogs;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import pirolPlugIns.dialogs.csun.DateChooser;
import pirolPlugIns.i18n.PirolPlugInMessages;

/**
 * Panel to choose a date with and to display the currently choosen date in a textfield.<br>
 * The main component is a date chooser created by Hovanes Gambaryan, Henry Demirchian, CSUN, CS 585, Professor Mike Barnes.
 *
 * @author Ole Rahn
 * <br>
 * <br>FH Osnabr&uuml;ck - University of Applied Sciences Osnabr&uuml;ck,
 * <br>Project: PIROL (2005),
 * <br>Subproject: Daten- und Wissensmanagement
 * 
 * @version $Revision: 1.1 $
 * 
 */
public class DateChooserPanel extends JPanel implements ActionListener {

    private static final long serialVersionUID = 1423874196859088272L;
    
    protected final static String ACTIONCOMMAND_CHOOSEDATE = "chooseDate";
    
    protected final static GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("Europe/Berlin"), Locale.GERMANY);
    protected DateChooser dateChooser = null;
    protected DateFormat dateFormat = DateFormat.getDateInstance();

    protected JButton dateButton = null;
    protected JTextField dateDisplayField = new JTextField();
    
    protected Dimension maxComponentDim = new Dimension(170, 20);
    
    protected Date choosenDate = null;
    
    protected int margin = 10;
    protected String text = null;
    
    protected ArrayList actionListener = new ArrayList();
    
    /**
     * 
     *@param parentFrame parent Frame of the Dialog
     *@param displayBorder a border around this panel will be displayed if true
     *@param dateTextResourceKey language key for the desired date text
     */
    public DateChooserPanel(JFrame parentFrame, boolean displayBorder, String dateTextResourceKey){
        super();

        dateChooser = new DateChooser(parentFrame, calendar);
        dateFormat.setCalendar(calendar);
        
        this.text = PirolPlugInMessages.getString(dateTextResourceKey);
        
        if (displayBorder)
            this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
     
        this.setPreferredSize(new Dimension( (int)(3*maxComponentDim.getWidth() + 2 * margin + 20), (int)(this.maxComponentDim.getHeight()*2 + 5)));
   
        this.setupGui();
    }
    
    /**
     * 
     *@param parentFrame parent Frame of the Dialog
     *@param displayBorder a border around this panel will be displayed if true
     */
    public DateChooserPanel(JFrame parentFrame, boolean displayBorder){
        this(parentFrame, displayBorder, "date");
    }
    
    /**
     * 
     *@param parentFrame parent Frame of the Dialog
     */
    public DateChooserPanel(JFrame parentFrame){
        this(parentFrame, true, "date");
    }
    
    /**
     * sets up the graphical elements of the panel
     *
     */
    protected void setupGui(){
        
        this.dateDisplayField.setEditable(false);
        this.dateDisplayField.setMaximumSize(this.maxComponentDim);
        this.dateDisplayField.setPreferredSize(this.maxComponentDim);
        
//        this.getDateButton().setMaximumSize(this.maxComponentDim);
        this.getDateButton().setPreferredSize(this.maxComponentDim);
        
        JLabel label = new JLabel(this.text+":");
//        label.setMaximumSize(this.maxComponentDim);
        label.setPreferredSize(this.maxComponentDim);
        
        Box vbox = Box.createVerticalBox();
        
        Box box = Box.createHorizontalBox();
        
        box.add(Box.createHorizontalStrut(margin));
        box.add(label);
        box.add(Box.createGlue());
        box.add(Box.createHorizontalStrut(margin));
        
        vbox.add(box);
        
        box = Box.createHorizontalBox();
        
        box.add(Box.createHorizontalStrut(margin));
        box.add(this.dateDisplayField);
        box.add(Box.createGlue());
        box.add(this.getDateButton());
        box.add(Box.createHorizontalStrut(margin));
        
        vbox.add(box);
        
        this.setLayout(new BorderLayout());
        this.add(vbox, BorderLayout.CENTER);
        
    }
    
    
    /**
     * This method initializes jButton  
     *  
     * @return javax.swing.JButton  
     */
    private JButton getDateButton() {
        if (dateButton == null) {
            dateButton = new JButton();
            dateButton.setText(PirolPlugInMessages.getString("choose-date")+"...");
            dateButton.addActionListener(this);
            dateButton.setActionCommand(ACTIONCOMMAND_CHOOSEDATE);
        }
        return dateButton;
    }


    /**
     *@inheritDoc
     */
    public void actionPerformed(ActionEvent event) {
        if ( ((JButton)event.getSource()).getActionCommand().equals(ACTIONCOMMAND_CHOOSEDATE) ){
            // choose date button was clicked
            if (this.dateChooser.showDateChooser() == DateChooser.OK_OPTION) {
                GregorianCalendar cal = dateChooser.getDate();
                this.dateDisplayField.setText(dateFormat.format(cal.getTime()));
                this.choosenDate = new Date(calendar.getTimeInMillis());
                
                this.fireActionEvent(new ActionEvent(this, AWTEvent.RESERVED_ID_MAX + 2, null));
                
            }
        }
    }

    public void fireActionEvent(ActionEvent event){
        for (int i=0; i<this.actionListener.size(); i++){
            ((ActionListener)this.actionListener.get(i)).actionPerformed(event);
        }
    }

    /**
     * 
     *@return the choosen date or null if none was choosen...
     */
    public Date getChoosenDate() {
        return choosenDate;
    }
    
    public void addActionListener(ActionListener al){
        this.actionListener.add(al);
    }

}
