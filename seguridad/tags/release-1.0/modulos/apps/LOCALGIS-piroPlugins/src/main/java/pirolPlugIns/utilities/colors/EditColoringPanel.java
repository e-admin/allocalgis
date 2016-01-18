/*
 * Created on 14.07.2005 for PIROL
 *
 * CVS header information:
 *  $RCSfile: EditColoringPanel.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:32:05 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/utilities/colors/EditColoringPanel.java,v $
 */
package pirolPlugIns.utilities.colors;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;
import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableColumn;

import pirolPlugIns.dialogs.ColorsChooserPanel;
import pirolPlugIns.i18n.PirolPlugInMessages;
import pirolPlugIns.utilities.ColorRenderer;
import pirolPlugIns.utilities.DoubleEditor;
import pirolPlugIns.utilities.FeatureCollectionTools;
import pirolPlugIns.utilities.Properties.PropertiesHandler;
import pirolPlugIns.utilities.debugOutput.DebugUserIds;
import pirolPlugIns.utilities.debugOutput.PersonalLogger;

import com.vividsolutions.jump.feature.FeatureCollection;

/**
 * Panel that shows a coloring sheme in a table and also supports changes.
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
public class EditColoringPanel extends JPanel implements ColoringChangedListener, ValueToColorMapChangeListener, ActionListener {

    private static final long serialVersionUID = 1544365553325347290L;
    
    private FeatureCollection featureCollection = null;
    private String attribute = null;
    
    protected JTextField saveAs = new JTextField();
    protected JButton saveAsButton = new JButton(PirolPlugInMessages.getString("save-coloring-as")), loadButton = new JButton(PirolPlugInMessages.getString("load"));
    
    protected String propertiesFileEnding = PropertiesHandler.propertiesFileEnding;
    
    protected ColorsChooserPanel colorChooser = null;
    private JTable table;
    private ValueToColorTableModel tableModel = null;
    protected ValueToColorMap val2color = null;
    
    protected JComboBox storedColorMaps = null;
    
    protected static PersonalLogger logger = new PersonalLogger(DebugUserIds.USER_Ole);

    private JScrollPane scrollPane;
    
    protected static final int defaultColoringSteps = 15;
    
    protected static DecimalFormat numFormat = null;
    
    public EditColoringPanel( FeatureCollection featureCollection, String attribute ){
        this.featureCollection = featureCollection;
        this.attribute  = attribute;
        
        this.setupNumberFormat();
        
        Color[] colors = new Color[3];
        colors[0] = Color.GREEN;
        colors[1] = Color.YELLOW;
        colors[2] = Color.RED;
        
        this.colorChooser = new ColorsChooserPanel( colors, colors.length, PirolPlugInMessages.getString("please-choose-color-range") + ":");
        this.colorChooser.setSteps(defaultColoringSteps);
        
        this.addColoringListner(this);
        
        this.setupGui(0,0);
    }
    
    public EditColoringPanel( double min, double max ){
        
        this.setupNumberFormat();
        
        Color[] colors = new Color[3];
        colors[0] = Color.GREEN;
        colors[1] = Color.YELLOW;
        colors[2] = Color.RED;
        
        this.colorChooser = new ColorsChooserPanel( colors, colors.length, PirolPlugInMessages.getString("please-choose-color-range") + ":");
        this.colorChooser.setSteps(defaultColoringSteps);
        
        this.addColoringListner(this);
        
        this.setupGui( min,  max);
    }
    
    protected void setupNumberFormat(){
        if (numFormat == null){
            numFormat = (DecimalFormat)DecimalFormat.getInstance(Locale.getDefault());
        }
    }
    
    protected void createValue2ColorMap(){
        if (FeatureCollectionTools.isAttributeTypeNumeric(this.featureCollection.getFeatureSchema().getAttributeType(this.attribute))){
            this.setEditingEnabled(true);
            double[] minMax = FeatureCollectionTools.getMinMaxAttributeValue(this.featureCollection, this.attribute);
            this.val2color = new ValueToColorMap(defaultColoringSteps, minMax[0], minMax[1]);
            this.val2color.rebuildColors(false);
            this.colorChooser.setStepsNoChangeEvent(this.val2color.getNumberOfColors());
        } else {
            this.setEditingEnabled(true);
            Set values = FeatureCollectionTools.getSetOfDifferentAttributeValues(FeatureCollectionTools.FeatureCollection2FeatureArray(this.featureCollection), this.attribute);
            this.val2color = new ValueToColorMap(values.toArray());
            this.val2color.rebuildColors(false);
            this.colorChooser.setStepsNoChangeEvent(this.val2color.getNumberOfColors());
            this.setEditingEnabled(false);
        }
        this.val2color.setColorGenerator(this.colorChooser.createColorGenerator());
    }
    
    protected void setupGui(double min, double max){
        this.setLayout(new BorderLayout());
        
        this.add(this.colorChooser, BorderLayout.NORTH);
        
        if (this.featureCollection!=null){
            this.createValue2ColorMap();
        } else {
            this.val2color = new ValueToColorMap(this.colorChooser.getNumOfColors(), min, max);
        }
        
        this.val2color.setColorGenerator(this.colorChooser.createColorGenerator());
        
        this.tableModel = new ValueToColorTableModel(this.val2color);
        
        this.table = this.createNewTable();
        scrollPane = new JScrollPane(this.table);
        scrollPane.setPreferredSize(new Dimension(this.getWidth() - 50, 150));
        this.add( scrollPane, BorderLayout.CENTER );
        
        // -----------------------------------
        JPanel loadSaveColorings = new JPanel(new BorderLayout());
        JPanel loadStoredColorings = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel SaveColorings = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        loadStoredColorings.add(new JLabel(PirolPlugInMessages.getString("stored-coloring")));
        this.storedColorMaps = new JComboBox();
        
        this.fillLoadDropDown();
        
        this.storedColorMaps.setPreferredSize(new Dimension(160, 20));
        loadStoredColorings.add(this.storedColorMaps);
        loadStoredColorings.add(this.loadButton);
        this.loadButton.addActionListener(this);
        
        this.saveAsButton.addActionListener(this);
        this.saveAsButton.setActionCommand(this.saveAsButton.getText());
        SaveColorings.add(this.saveAsButton);
        this.saveAs.setPreferredSize(new Dimension(160, 20));
        SaveColorings.add(this.saveAs);
        
        loadSaveColorings.add(loadStoredColorings, BorderLayout.NORTH);
        loadSaveColorings.add(SaveColorings, BorderLayout.SOUTH);
        
        // -----------------------------------
        
        this.add(loadSaveColorings, BorderLayout.SOUTH);
        
        this.val2color.addValueToColorMapChangeListener(this);
    }
    
    protected void fillLoadDropDown(){
        File coloringDir = ValueToColorMap.getColoringDir();
        File[] storedColorings = coloringDir.listFiles(new FileFilter(){

            public boolean accept(File file) {
                return file.getName().toLowerCase().endsWith(".properties");
            }
                
            }
        );
        
        this.storedColorMaps.removeAllItems();
        
        if (storedColorings!=null && storedColorings.length > 0) {
            String[] fileNames = new String[storedColorings.length];
            
            for (int i=0; i<storedColorings.length; i++){
                fileNames[i] = storedColorings[i].getName().endsWith(this.propertiesFileEnding)?storedColorings[i].getName().substring(0,storedColorings[i].getName().lastIndexOf(this.propertiesFileEnding)):storedColorings[i].getName();
                this.storedColorMaps.addItem(fileNames[i]);
            }
            this.storedColorMaps.setEnabled(true);
            this.loadButton.setEnabled(true);
        } else {
            this.storedColorMaps.setEnabled(false);
            this.loadButton.setEnabled(false);
        }
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        
        if (attribute==null){
            EditColoringPanel.logger.printError("attribute name is null!");
            return;
        }
        
        this.attribute = attribute;
        this.saveAs.setText(this.attribute);
        this.createValue2ColorMap();
        

        this.setVal2color(this.val2color);
        
    }

    public ValueToColorMap getValueToColorMap() {
        return this.val2color;
    }
    
    protected JTable createNewTable(){
        JTable table = new JTable(this.tableModel );
        //table.getDefaultRenderer(Double.class).getTableCellRendererComponent()
        table.setDefaultRenderer(Color.class, new ColorRenderer(true));
        table.setDefaultEditor(Color.class, new ColorEditor(this));
        
        table.setDefaultEditor(Double.class, new DoubleEditor() );
        
        TableColumn column = null;
        for (int i = 0; i < this.tableModel.getColumnCount(); i++) {
            column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth(this.tableModel.colWidths[i]);
        }
        return table;
    }

    public void setVal2color(ValueToColorMap val2color) {
        this.val2color = val2color;
        this.val2color.addValueToColorMapChangeListener(this);
        this.tableModel.setVal2color(this.val2color);
        this.colorChooser.setColorGenerator(this.val2color.getColorGenerator(), false);
        this.tableModel.rebuildTable();
        
        this.table = this.createNewTable();
        
        this.scrollPane.setViewportView(this.table);
    }

    /**
     *@see ColorsChooserPanel#addColoringListener(ColoringChangedListener)
     */
    public void addColoringListner(ColoringChangedListener listener) {
        colorChooser.addColoringListener(listener);
    }

    /**
     *@see ColorsChooserPanel#removeColoringListener(ColoringChangedListener)
     */
    public void removeColoringListner(ColoringChangedListener listener) {
        colorChooser.removeColoringListener(listener);
    }

    /**
     *@inheritDoc
     */
    public void coloringChanged(ColorGenerator colorGen) {
        this.val2color.setColorGenerator(colorGen);
        this.tableModel.rebuildTable();
    }

    public int getNumberOfColors() {
        return val2color.getNumberOfColors();
    }

    public void setValueRange(double min, double max) {
        val2color.setValueRange(min, max);
        this.tableModel.rebuildTable();
    }

    public void setNumberOfColors(int numberOfColors) {
        val2color.setNumberOfColors(numberOfColors);
        this.colorChooser.setSteps(numberOfColors);
    }

    public FeatureCollection getFeatureCollection() {
        return featureCollection;
    }

    public void setFeatureCollection(FeatureCollection featureCollection) {
        this.featureCollection = featureCollection;
    }

    public boolean isEditingEnabled() {
        return tableModel.isEnabled() && this.colorChooser.isAllowChangingTheNumberOfSteps();
    }

    public void setEditingEnabled(boolean enabled) {
        if (this.tableModel!=null)
            this.tableModel.setEnabled(enabled);
        this.colorChooser.setAllowChangingTheNumberOfSteps(enabled);
    }

    public void addValueToColorMapChangeListener(ValueToColorMapChangeListener listener) {
        val2color.addValueToColorMapChangeListener(listener);
    }

    public void removeValueToColorMapChangeListener(ValueToColorMapChangeListener listener) {
        val2color.removeValueToColorMapChangeListener(listener);
    }

    /**
     *@param source
     */
    public void valueToColorMapChanged(ValueToColorMap source) {
        this.colorChooser.removeColoringListener(this);
        this.colorChooser.setSteps(source.getNumberOfColors());
        this.colorChooser.addColoringListener(this);
    }
    
    protected boolean saveColorSchema(ValueToColorMap map, String fileName){
        if (!fileName.toLowerCase().endsWith(propertiesFileEnding)){
            fileName = fileName + propertiesFileEnding;
        }
        PropertiesHandler propHandler = new PropertiesHandler(ValueToColorMap.storedColoringDirectory + fileName);
        
        boolean retVal = map.storeToFile(propHandler);
        
        this.fillLoadDropDown();
        
        return retVal;
    }
    
    protected boolean loadColorSchema(String fileName){
        if (!fileName.toLowerCase().endsWith(propertiesFileEnding)){
            fileName = fileName + propertiesFileEnding;
        }
        PropertiesHandler propHandler = new PropertiesHandler(ValueToColorMap.storedColoringDirectory + fileName);
        
        this.setVal2color(new ValueToColorMap(propHandler));
        return true;
    }

    /**
     *@inheritDoc
     */
    public void actionPerformed(ActionEvent event) {
        JComponent source = (JComponent)event.getSource();
        
        if (source == this.saveAsButton){
            boolean saved = this.saveColorSchema(this.getValueToColorMap(), this.saveAs.getText());
            if (saved){
                JOptionPane.showMessageDialog(this, PirolPlugInMessages.getString("color-schema-saved"));
            }
        } else if (source == this.loadButton) {
            this.loadColorSchema(this.storedColorMaps.getSelectedItem().toString());
            this.saveAs.setText(this.storedColorMaps.getSelectedItem().toString());
        }
    }
}
