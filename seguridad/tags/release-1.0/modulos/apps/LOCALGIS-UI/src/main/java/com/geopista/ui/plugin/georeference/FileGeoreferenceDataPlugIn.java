package com.geopista.ui.plugin.georeference;

import java.awt.Frame;
import java.util.Locale;
import java.util.ResourceBundle;

import reso.jump.joinTable.JoinTable;

import com.geopista.app.AppContext;
import com.geopista.model.GeopistaLayer;
import com.geopista.ui.plugin.GeopistaEnableCheckFactory;
import com.geopista.ui.plugin.georeference.panel.FileGeoreferencePanel00;
import com.geopista.ui.plugin.georeference.panel.FileGeoreferencePanel01;
import com.geopista.ui.plugin.georeference.panel.FileGeoreferencePanel02;
import com.geopista.ui.plugin.georeference.panel.FileGeoreferencePanel03;
import com.geopista.ui.plugin.georeference.panel.FileGeoreferencePanel04;
import com.geopista.ui.plugin.georeference.panel.FileGeoreferencePanel05;
import com.geopista.ui.plugin.georeference.panel.FileGeoreferencePanel06;
import com.geopista.ui.wizard.WizardDialog;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.GeopistaFunctionUtils;
import com.geopista.util.GeopistaUtil;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedBasePlugIn;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;
/**
 * 
 * @author jvaca
 * Modificaciones rubengomez
 */
public class FileGeoreferenceDataPlugIn extends ThreadedBasePlugIn 
{
    

    private static AppContext aplicacion = (AppContext) AppContext
            .getApplicationContext();
    private Blackboard blackboard = aplicacion.getBlackboard();
//    private GeopistaListaMapasDialog ListaMapasDialog = null;
    public static final String CAPAPLANTILLA = "SeriePrintPlugIn.CapaPlantilla";

    //VARIABLES NECESARIAS QUE SE PASAN DESDE LOS PANELES
    GeopistaLayer capa=null;
    String select=null;
    JoinTable jt=null;
    //ArrayList localizado;
    //int indexLayer=-1;
    //indices de la georeferenciacion
    //int keynumDirec=-1;
    //int keynumCalle=-1;
    //int keynumeroPortal=-1;
    //FIN
    Frame owner = null;
    //HashMap aa=new HashMap();
    //Hashtable bb =new Hashtable();

    
    public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext)
    {
        GeopistaEnableCheckFactory checkFactory = new GeopistaEnableCheckFactory(workbenchContext);
        return new MultiEnableCheck()
                .add(checkFactory.createWindowWithLayerManagerMustBeActiveCheck())
                .add(checkFactory.createWindowWithAssociatedTaskFrameMustBeActiveCheck())
                .add(checkFactory.createAtLeastNLayersMustExistCheck(1));
    }
    public void initialize(PlugInContext context) throws Exception
    {
    	Locale loc=I18N.getLocaleAsObject();      
    	ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.ui.plugin.georeference.language.Georreferenciacioni18n",loc,this.getClass().getClassLoader());
    	I18N.plugInsResourceBundle.put("Georreferenciacion",bundle);
        FeatureInstaller featureInstaller = new FeatureInstaller(context.getWorkbenchContext());
        featureInstaller.addMainMenuItem(this,
                new String[]{"Tools", AppContext.getApplicationContext().getI18nString("ui.MenuNames.TOOLS.ANALYSIS"), I18N.get("Georreferenciacion","georeference.panel00.labelGeoreference")},
                I18N.get("Georreferenciacion","georeference.panel00.labelComplete"), 
                false,
                null,
                FileGeoreferenceDataPlugIn.createEnableCheck(context.getWorkbenchContext()));
    }

    public boolean execute(PlugInContext context) throws Exception
    {
    	
    	
    	Locale loc=I18N.getLocaleAsObject();      
    	ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.ui.plugin.georeference.language.Georreferenciacioni18n",loc,this.getClass().getClassLoader());
    	I18N.plugInsResourceBundle.put("Georreferenciacion",bundle);

    	
    	
        reportNothingToUndoYet(context);
        blackboard.put("keynumPortal",null);//Ya no es necesaria.
        blackboard.put("JoinTableObjet",null);
        
        WizardDialog dialog = new WizardDialog(GeopistaFunctionUtils.getFrame(context.getWorkbenchGuiComponent()),
        		I18N.get("Georreferenciacion","georeference.panel.wizardTitledata"), context.getErrorHandler());
        
        dialog.init(new WizardPanel[] {new FileGeoreferencePanel01("1","3",context),
                                  new FileGeoreferencePanel03("3","6",context),
                                  new FileGeoreferencePanel06("6",null,context)
                                  });        

        dialog.setSize(650,550);
        dialog.getContentPane().remove(dialog.getInstructionTextArea());
        GUIUtil.centreOnWindow(dialog);
        dialog.setVisible(true);
        
        if (!dialog.wasFinishPressed()) {
            return false;
        }
        return true;
    }
	public void run(TaskMonitor monitor, PlugInContext context) throws Exception {
		// TODO Auto-generated method stub
		
	}
 
}