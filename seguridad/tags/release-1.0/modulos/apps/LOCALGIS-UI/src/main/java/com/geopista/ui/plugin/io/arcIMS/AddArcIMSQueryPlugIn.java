/**
 * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
USA.
 *
 * For more information, contact:
 *
 *
 * www.geopista.com
 *
 *
*/

/**
 *
 * <p>Title: AddArcIMSQueryPlugIn</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author Juan Pablo de Castro based on Jan Ruzicka's plugin jan.ruzicka@vsb.cz
 * @version 0.1
 */

package com.geopista.ui.plugin.io.arcIMS;

import java.io.IOException;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.ui.wizard.WizardDialog;
import com.geopista.ui.wizard.WizardPanel;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.LayerManager;
import com.vividsolutions.jump.workbench.model.StandardCategoryNames;
import com.vividsolutions.jump.workbench.model.UndoableCommand;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.MenuNames;

import cz.vsb.gisak.jump.plugin.arcims.ServiceWizardPanel;
import cz.vsb.gisak.jump.plugin.arcims.URLWizardPanel;



public class AddArcIMSQueryPlugIn extends AbstractPlugIn {
	/**
	 * Logger for this class
	 */
	private static final Log	logger	= LogFactory
												.getLog(AddArcIMSQueryPlugIn.class);

//    private String cachedURL = "http://gisova.mmo.cz/servlet/com.esri.esrimap.Esrimap";
	 private String cachedURL = "http://212.22.38.20/servlet/com.esri.esrimap.Esrimap";
	    private String cachedService = "";
    //final Envelope cachedEnvelope;
    //final Image cachedImage;
    //private Image cachedImage;
    
    public AddArcIMSQueryPlugIn() {

    }

    private boolean addArcIMSQuery(final WorkbenchContext context, final String url) {
	final String serviceName = cachedService;

	 final ArcIMSLayer layer;
	try
		{
		layer = new ArcIMSLayer((LayerManager)context.getLayerManager(),url,serviceName);
		}
	catch (IOException e)
		{
		// TODO Auto-generated catch block
		e.printStackTrace();
		return false;
		}
    execute(new UndoableCommand(getName()) {
            public void execute() {
                Collection selectedCategories = context.getLayerNamePanel()
                                                       .getSelectedCategories();
                context.getLayerManager().addLayerable(selectedCategories.isEmpty()
                    ? StandardCategoryNames.WORKING
                    : selectedCategories.iterator().next().toString(), layer);
            }

            public void unexecute() {
                context.getLayerManager().remove(layer);
            }
        }, context);
    
	return(true);	
    }

    public void initialize(PlugInContext context) throws Exception {
    EnableCheckFactory checkFactory = new EnableCheckFactory(context.getWorkbenchContext());
    I18N.setPlugInRessource(this.getName(),"language.AddArcIMS");
        context.getFeatureInstaller().addMainMenuItem(this,
        		new String[]{MenuNames.LAYER,
        				"ImageCoverage"},
						I18N.get(this.getName(),"AddArcIMS"), 
			false, null, checkFactory.createTaskWindowMustBeActiveCheck());
        context.getFeatureInstaller().addPopupMenuItem(context.getWorkbenchContext().getIWorkbench()
                .getGuiComponent()
                .getCategoryPopupMenu(),
				this,
        		  I18N.get(this.getName(),"AddArcIMS") ,
        		  false,null,null);
    }


    public boolean execute(final PlugInContext context)
        throws Exception {
        reportNothingToUndoYet(context);

        WizardDialog d = new WizardDialog(context.getWorkbenchFrame(),
        		AppContext.getMessage( "Add ArcIMS Query to Task"), context.getErrorHandler());
        d.init(new WizardPanel[] {
                new URLWizardPanel(cachedURL), new ServiceWizardPanel(cachedURL)
            });

        //Set size after #init, because #init calls #pack. [Jon Aquino]
        d.setSize(500, 400);
        GUIUtil.centreOnWindow(d);
        d.setVisible(true);
        if (!d.wasFinishPressed()) {
            return false;
        }

        cachedURL = (String) d.getData(URLWizardPanel.URL_KEY);
        cachedService = (String) d.getData(ServiceWizardPanel.SERVICE_KEY);

        addArcIMSQuery(context.getWorkbenchContext(), cachedURL + "ServiceName=" + cachedService);
        
        return true;
    }
	public String getCachedService()
	{
	return cachedService;
	}
	public void setCachedService(String cachedService)
	{
	this.cachedService = cachedService;
	}
}
