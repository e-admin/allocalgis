/*
 * Created on 15.08.2005 for PIROL
 *
 * CVS header information:
 *  $RCSfile: GetRangePanel.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:46 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/dialogs/GetRangePanel.java,v $
 */
package pirolPlugIns.dialogs;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;

import pirolPlugIns.i18n.PirolPlugInMessages;

/**
 * Panel that offers controlls to ask Users for a range.
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
public class GetRangePanel extends JPanel implements ValueChecker {

    private static final long serialVersionUID = 8699695678972798419L;
    
    protected JButton fillInRangeButton = new JButton(PirolPlugInMessages.getString("use-this-range") + ": ");
    protected JFormattedTextField fromArea = null, toArea = null;

    /**
     * 
     *@param i18nKeyForText the resource key for the internationliazed text, e.g. <code>"use-this-range"</code>
     */
    public GetRangePanel(String i18nKeyForText){
        super(new GridLayout(5,1));
        
        this.fillInRangeButton = new JButton(PirolPlugInMessages.getString(i18nKeyForText) + ": ");
        
        this.add(new JLabel(PirolPlugInMessages.getString("fill-in-range-manually")));
        
        ((GridLayout)this.getLayout()).setHgap(5);
        ((GridLayout)this.getLayout()).setVgap(5);
        
        GridLayout grdl = new GridLayout(1,2);
        
        JPanel fromPanel = new JPanel(grdl);
        fromPanel.add(new JLabel(PirolPlugInMessages.getString("from") + ": "));
        this.fromArea = new JFormattedTextField();
        this.fromArea.setColumns(15);
        this.fromArea.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        fromPanel.add(this.fromArea);
        this.add(fromPanel);
        
        JPanel toPanel = new JPanel(grdl);
        toPanel.add(new JLabel(PirolPlugInMessages.getString("to") + ": "));
        this.toArea = new JFormattedTextField();
        this.toArea.setColumns(15);
        this.toArea.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        toPanel.add(this.toArea);
        this.add(toPanel);
        
        this.add(this.fillInRangeButton);
    }

    /**
     * Add action listener that reacts on submitted range values.
     *@param actionEvent
     */
    public void addActionListener(ActionListener actionEvent) {
        if (this.fillInRangeButton!=null)
            this.fillInRangeButton.addActionListener(actionEvent);
    }

    public String getFromText() {
        return fromArea.getText().replace(',','.');
    }

    public void setFromText(String arg0) {
        fromArea.setText(arg0);
    }
    
    public double getLowerRangeBorder(){
        return Double.parseDouble(this.getFromText());
    }
    
    public String getToText() {
        return toArea.getText().replace(',','.');
    }

    public void setToText(String arg0) {
        toArea.setText(arg0);
    }
    
    public double getUpperRangeBorder(){
        return Double.parseDouble(this.getToText());
    }
    
    public boolean wasSourceOfActionEvent(Object eventSource){
        return JButton.class.isInstance(eventSource) && this.fillInRangeButton.equals(eventSource);
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        this.fromArea.setEnabled(enabled);
        this.fromArea.setEditable(enabled);
        this.toArea.setEnabled(enabled);
        this.toArea.setEditable(enabled);
        this.fillInRangeButton.setEnabled(enabled);
        
        if (!enabled){
            this.fromArea.setBackground(this.getBackground());
            this.toArea.setBackground(this.getBackground());
        } else {
            JTextArea ta = new JTextArea();
            this.fromArea.setBackground(ta.getBackground());
            this.toArea.setBackground(ta.getBackground());
        }
    }

    /**
     *@inheritDoc
     */
    public boolean areValuesOk() {
        try {
            double min =  this.getLowerRangeBorder();
            double max =  this.getUpperRangeBorder();
            
            if (min <= max) {
                JTextArea ta = new JTextArea();
                this.fromArea.setBackground(ta.getBackground());
                this.toArea.setBackground(ta.getBackground());
                return true;
            }
                
        } catch (RuntimeException e) {
            
        }
        this.fromArea.setBackground(Color.red);
        this.toArea.setBackground(Color.red);
        
        return false;
    }
    
    
    
    
}
