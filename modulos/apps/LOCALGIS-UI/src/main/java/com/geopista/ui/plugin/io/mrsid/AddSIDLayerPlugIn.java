/**
 * AddSIDLayerPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.geopista.ui.plugin.io.mrsid;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import com.geopista.app.AppContext;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.model.StandardCategoryNames;
import com.vividsolutions.jump.workbench.model.UndoableCommand;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.MenuNames;
import com.vividsolutions.wms.MapLayer;

//modeled after the AddWMSQueryPlugIn
public class AddSIDLayerPlugIn extends AbstractPlugIn
{
    public static String WORKING_DIR;
    public static String ETC_PATH;
    public static String TMP_PATH;
    public static String MRSIDDECODE;
    public static String MRSIDINFO;
    private String cachedFilename = "/";
    AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    public void initialize(PlugInContext context) throws Exception
    {
    //System.out.println("loader:"+I18N.getInstance().getClass().getClassLoader());
    Locale loc=Locale.getDefault();
	 
	ResourceBundle bundle2 = ResourceBundle.getBundle("language.AddMrSID",loc,this.getClass().getClassLoader());
	
    I18N.plugInsResourceBundle.put(this.getName(),bundle2);
   // I18N.setPlugInRessource(this.getName(),"language.AddMrSID");
    
    String msg=I18N.get(this.getName(),"AddMrSIDLayer");
    
//    context.getFeatureInstaller().addMainMenuItem(this,
//        new String[] { MenuNames.LAYER,
//        		I18N.get(this.getName(),"ImageCoverage")}, msg, false, null, null);
    context.getFeatureInstaller().addLayerNameViewMenuItem(this,
        		new String[]{
        				MenuNames.LAYER,
						"ImageCoverage"}
    					,msg);
    
    context.getFeatureInstaller().addPopupMenuItem(context.getWorkbenchContext().getIWorkbench()
            .getGuiComponent()
            .getCategoryPopupMenu(),
			this,
    		  I18N.get(this.getName(),"AddMrSIDLayer") ,
    		  false,null,null);
    	String dirBase = aplicacion.getPath("dirBase");
        File empty = new File(dirBase);
        String sep = File.separator;
        WORKING_DIR = empty.getAbsoluteFile().getParent() + sep;
        ETC_PATH = WORKING_DIR + "etc" + sep;
        TMP_PATH = WORKING_DIR + "tmp" + sep;
        MRSIDDECODE = ETC_PATH + "mrsiddecode.exe";
        MRSIDINFO = ETC_PATH + "mrsidinfo.exe";
        //System.out.println("MrSID-etc:"+ETC_PATH);
    }
    
    private List toLayerNames(List mapLayers) 
    {
        ArrayList names = new ArrayList();
        for (Iterator i = mapLayers.iterator(); i.hasNext();) 
        {
            MapLayer layer = (MapLayer) i.next();
            names.add(layer.getName());
        }
        return names;
    }
    
    public boolean execute(final PlugInContext context) throws Exception
    {
        reportNothingToUndoYet(context);
        try
        {
            context.getWorkbenchFrame().getOutputFrame().createNewDocument();
            
            if (!new File(MRSIDDECODE).exists())
            {
                context.getWorkbenchFrame().warnUser("Error: see output window");
                context.getWorkbenchFrame().getOutputFrame().addText(MRSIDDECODE + " not installed.");
                return false;
            }
            
            if (!new File(MRSIDINFO).exists())
            {
                context.getWorkbenchFrame().warnUser("Error: see output window");
                context.getWorkbenchFrame().getOutputFrame().addText(MRSIDINFO + " not installed.");
                return false;
            }
            
            JFileChooser fileChooser = new JFileChooser();
            fileChooser = GUIUtil.createJFileChooserWithExistenceChecking();
            fileChooser.setDialogTitle("Open MrSID file");
            fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setMultiSelectionEnabled(true);
            GUIUtil.removeChoosableFileFilters(fileChooser);
            FileFilter fileFilter = GUIUtil.createFileFilter("MrSID Files", new String[]{"sid"});
            fileChooser.addChoosableFileFilter(fileFilter);
            fileChooser.setFileFilter(fileFilter);
            
            if (JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(context.getWorkbenchFrame()))
            {
                List imageFilenames = new ArrayList();
                File selectedFile = fileChooser.getSelectedFile();
                File[] files = fileChooser.getSelectedFiles();
                for(int i = 0; i < files.length; i++)
                {
                    String filename = files[i].getCanonicalPath();
                    imageFilenames.add(filename);
                }
                
                final SIDLayer layer = new SIDLayer(context, imageFilenames);
                execute(new UndoableCommand(getName())
                {
                    public void execute()
                    {
                        Collection selectedCategories = context.getLayerNamePanel().getSelectedCategories();
                        context.getLayerManager().addLayerable(selectedCategories.isEmpty()
                        ? StandardCategoryNames.WORKING
                        : selectedCategories.iterator().next().toString(), layer);
                    }
                    
                    public void unexecute()
                    {
                        context.getLayerManager().remove(layer);
                    }
                }, context);
                
                return true;
            }
            else
            {
                return false;
            }
        }
        
        catch (Exception e)
        {
            context.getWorkbenchFrame().warnUser("Error: see output window");
            context.getWorkbenchFrame().getOutputFrame().addText("AddSIDLayerPlugIn Exception:" + e.toString());
            return false;
        }
    }
}

