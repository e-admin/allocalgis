/*
 * Created on 14.07.2005 for PIROL
 *
 * CVS header information:
 *  $RCSfile: ValueToColorTableModel.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:32:05 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/utilities/colors/ValueToColorTableModel.java,v $
 */
package pirolPlugIns.utilities.colors;

import java.awt.Color;

import pirolPlugIns.i18n.PirolPlugInMessages;
import pirolPlugIns.utilities.ObjectComparator;
import pirolPlugIns.utilities.StandardPirolTableModel;
import pirolPlugIns.utilities.colors.Value.ColoredRange;
import pirolPlugIns.utilities.colors.Value.ColoredValue;
import pirolPlugIns.utilities.debugOutput.DebugUserIds;
import pirolPlugIns.utilities.debugOutput.PersonalLogger;

/**
 * Table model for use with a ValueToColorMap. 
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
public class ValueToColorTableModel extends StandardPirolTableModel implements ValueToColorMapChangeListener {
    
    private static final long serialVersionUID = 6944868368034171963L;
    
    protected Class[] colClasses = null;
    
    public int[] colWidths = {25,50,50,15,15};
    public boolean[] editables = { true, true, true, true, true };
    
    protected ValueToColorMap val2color = null;
    
    protected PersonalLogger logger = new PersonalLogger(DebugUserIds.USER_Ole);
    
    protected boolean enabled = true;
    
    protected final static String[] colNamesNum = new String[]{PirolPlugInMessages.getString("color"), PirolPlugInMessages.getString("from"), PirolPlugInMessages.getString("to"), PirolPlugInMessages.getString("lower-border-included"), PirolPlugInMessages.getString("upper-border-included")};
    protected final static Class[]  colClassesNum = new Class[]{ Color.class, Double.class, Double.class, Boolean.class, Boolean.class };
    protected static int[] colWidthsNum = {25,50,50,15,15};
    protected final static boolean[] editablesNum = { true, true, true, true, true };
    
    protected final static String[] colNamesStr = new String[]{PirolPlugInMessages.getString("color"), PirolPlugInMessages.getString("value")};
    protected final static Class[]  colClassesStr = new Class[]{ Color.class, String.class };
    protected static int[] colWidthsStr = {25,120};
    protected final static boolean[] editablesStr = { true, false };

    public ValueToColorTableModel(ValueToColorMap val2color){
        super(colNamesNum);

        this.setVal2color(val2color);
    }
    
    
    
    /**
     *@inheritDoc
     */
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return (editables[columnIndex] && this.enabled) || (editables[columnIndex] && columnIndex==0);
    }
    
    

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        super.setValueAt(aValue, rowIndex, columnIndex);
        ColoredValue[] cvs = this.val2color.getKeysArray();
        
        switch (columnIndex){
            case (0):
                this.val2color.setColor(rowIndex, (Color)aValue);
                break;
            case (1):
                ((ColoredRange)cvs[rowIndex]).setMinValue(ObjectComparator.getDoubleValue(this.getValueAt(rowIndex, columnIndex)));
                break;
            case (2):
                ((ColoredRange)cvs[rowIndex]).setMaxValue(ObjectComparator.getDoubleValue(this.getValueAt(rowIndex, columnIndex)));
                break;
            case (3):
                ((ColoredRange)cvs[rowIndex]).setIncludingMin( ((Boolean)(this.getValueAt(rowIndex, columnIndex))).booleanValue() );
                break;
            case (4):
                ((ColoredRange)cvs[rowIndex]).setIncludingMax( ((Boolean)(this.getValueAt(rowIndex, columnIndex))).booleanValue() );
                break;
            default:
                break;                
        }
    }

    /**
     *@inheritDoc
     */
    public Class getColumnClass(int columnIndex) {
        if (this.colClasses.length > columnIndex)
            return this.colClasses[columnIndex];
        return null;
    }
    
    public void rebuildTable(){
        this.clearTable();
        
        ColoredValue[] cvs = this.val2color.getKeysArray();
        
        if (cvs==null){
            this.logger.printError("No val2Color keys!");
            return;
        }

        if (this.val2color.isNumericValues()){
            for (int i=0; i<cvs.length; i++){
                this.addRow(new Object[]{this.val2color.getColorFor(cvs[i]), new Double(((ColoredRange)cvs[i]).getMinValue()), new Double(((ColoredRange)cvs[i]).getMaxValue()), new Boolean(((ColoredRange)cvs[i]).isIncludingMin()), new Boolean(((ColoredRange)cvs[i]).isIncludingMax())});            
            }
        } else {
            for (int i=0; i<cvs.length; i++){
                this.addRow(new Object[]{this.val2color.getColorFor(cvs[i]), cvs[i].toString()});            
            }
        }
    }

    public ValueToColorMap getVal2color() {
        return val2color;
    }

    public void setVal2color(ValueToColorMap val2color) {
        this.val2color = val2color;
        this.clearTable();
        if (val2color.isNumericValues()){
            colClasses = colClassesNum;
            colNames = colNamesNum;
            colWidths = colWidthsNum;
            editables = editablesNum;
            this.setEnabled(true);
        } else {
            colClasses = colClassesStr;
            colNames = colNamesStr;
            colWidths = colWidthsStr;
            editables = editablesStr;
            this.setEnabled(false);
        }
        this.val2color.addValueToColorMapChangeListener(this);
        this.rebuildTable();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     *@param source
     */
    public void valueToColorMapChanged(ValueToColorMap source) {
        this.rebuildTable();
    }
}
