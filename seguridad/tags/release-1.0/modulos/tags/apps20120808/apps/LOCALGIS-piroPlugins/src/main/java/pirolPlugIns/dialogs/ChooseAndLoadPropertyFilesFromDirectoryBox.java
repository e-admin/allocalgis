/*
 * Created on 06.09.2005 for PIROL
 *
 * CVS header information:
 *  $RCSfile: ChooseAndLoadPropertyFilesFromDirectoryBox.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:46 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/dialogs/ChooseAndLoadPropertyFilesFromDirectoryBox.java,v $
 */
package pirolPlugIns.dialogs;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.border.EtchedBorder;

import pirolPlugIns.i18n.PirolPlugInMessages;
import pirolPlugIns.utilities.Properties.PropertiesHandler;

/**
 * a Panel with a dropDown box and a "load"-button to load a certain properties file from a directory. 
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
public class ChooseAndLoadPropertyFilesFromDirectoryBox extends Box implements ActionListener {

    private static final long serialVersionUID = 5426619366626106449L;
    
    protected final static String emptyString = "";
    
    protected String labelText = null;
    protected JComboBox availFiles = null;
    protected JButton loadButton = null;
    
    protected PropertiesHandler ph = null;
    protected String propertiesFileEnding = PropertiesHandler.propertiesFileEnding;
    
    protected File dirToLookIn = null;
    
    protected List loadPropertiesListener = new ArrayList();
    
    protected boolean insertEmptyItem = true;

    public ChooseAndLoadPropertyFilesFromDirectoryBox(String labelTextResourceKey, File dirToLookIn) {
        super(BoxLayout.X_AXIS);
        
        this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        
        this.dirToLookIn = dirToLookIn;
        
        if (!this.dirToLookIn.exists()){
            try {
                this.dirToLookIn.mkdirs();
            } catch (RuntimeException e) { }
        }
        
        this.labelText = PirolPlugInMessages.getString(labelTextResourceKey);
        this.availFiles = new JComboBox();
        this.loadButton = new JButton(PirolPlugInMessages.getString("load"));
        this.loadButton.addActionListener(this);
        
        this.add(Box.createRigidArea(new Dimension(10,0)));
        this.add(new JLabel(this.labelText));
        this.add(Box.createGlue());
        this.add(this.availFiles);
        this.add(Box.createGlue());
        this.add(this.loadButton);
        this.add(Box.createRigidArea(new Dimension(10,0)));
        
        this.fillLoadDropDown();
        
    }
    
    protected void fillLoadDropDown(){
        File[] filesFoundInDir = this.dirToLookIn.listFiles(new FileFilter(){

            public boolean accept(File file) {
                return file.getName().toLowerCase().endsWith(".properties");
            }
                
            }
        );
        
        if (this.availFiles.getItemCount()!=0)
            this.availFiles.removeAllItems();
        
        if (this.insertEmptyItem )
            this.availFiles.addItem(emptyString);
        
        if (filesFoundInDir!=null && filesFoundInDir.length > 0) {
            String[] fileNames = new String[filesFoundInDir.length];
            
            for (int i=0; i<filesFoundInDir.length; i++){
                fileNames[i] = filesFoundInDir[i].getName().endsWith(this.propertiesFileEnding)?filesFoundInDir[i].getName().substring(0,filesFoundInDir[i].getName().lastIndexOf(this.propertiesFileEnding)):filesFoundInDir[i].getName();
                this.availFiles.addItem(fileNames[i]);
            }
            this.availFiles.setEnabled(true);
            this.loadButton.setEnabled(true);
        } else {
            this.availFiles.setEnabled(false);
            this.loadButton.setEnabled(false);
        }
    }
    
    protected PropertiesHandler loadProperties(String fileName){
        if (!fileName.toLowerCase().endsWith(propertiesFileEnding)){
            fileName = fileName + propertiesFileEnding;
        }
        PropertiesHandler propHandler = new PropertiesHandler(this.dirToLookIn.getName() + File.separator + fileName);
        try {
            propHandler.load();
        } catch (IOException e) {
            e.printStackTrace();
            propHandler = null;
        }
        
        return propHandler;
    }
    
    public void actionPerformed(ActionEvent event) {
        JComponent source = (JComponent)event.getSource();
        
        if (source.equals(this.loadButton) ) {
            if (!this.availFiles.getSelectedItem().toString().equals(emptyString)){
                ph = this.loadProperties(this.availFiles.getSelectedItem().toString());
                this.fireLoadPropertiesEvent(ph);
            } else {
                this.fireLoadPropertiesEvent(null);
            }
        }
    }

    public void fireLoadPropertiesEvent(PropertiesHandler ph){
        for (int i=0; i<this.loadPropertiesListener.size(); i++){
            ((LoadPropertiesListener)this.loadPropertiesListener.get(i)).propertiesLoaded(new LoadPropertiesEvent(ph));
        }
    }
    
    public boolean addLoadPropertiesListener(LoadPropertiesListener listener) {
        return loadPropertiesListener.add(listener);
    }

    public boolean removeLoadPropertiesListener(LoadPropertiesListener listener) {
        return loadPropertiesListener.remove(listener);
    }
    
    

}
