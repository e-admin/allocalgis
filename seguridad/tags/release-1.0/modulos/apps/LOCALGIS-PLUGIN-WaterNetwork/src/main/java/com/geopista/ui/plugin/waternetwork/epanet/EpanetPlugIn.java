package com.geopista.ui.plugin.waternetwork.epanet;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.MenuNames;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;
import com.geopista.app.AppContext;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.ui.plugin.waternetwork.images.IconLoader;
import com.geopista.ui.plugin.waternetwork.toolbox.WaterNetworkPlugIn;
import com.geopista.util.ApplicationContext;

public class EpanetPlugIn extends AbstractPlugIn{

    private boolean epanetPlugIn = false;
    public String getName() {return I18N.get("WaterNetworkPlugIn","Epanet");}
    private EpanetTools epanetTools = new EpanetTools();
    private ApplicationContext app = AppContext.getApplicationContext();
    
    @SuppressWarnings("unchecked")
	public void initialize(PlugInContext context) throws Exception{
    	Locale loc=Locale.getDefault();
    	ResourceBundle bundle2 = ResourceBundle.getBundle("com.geopista.ui.plugin.waternetwork.languages.WaterNetworkPlugIni18n",loc,
    			this.getClass().getClassLoader());
    	I18N.plugInsResourceBundle.put("WaterNetworkPlugIn",bundle2);
        String pathMenuNames[] =new String[] { MenuNames.EDIT };
		String name = I18N.get("WaterNetworkPlugIn",getName());
        context.getFeatureInstaller().addMainMenuItem(this, pathMenuNames, name,
    			false, null, createEnableCheck(context.getWorkbenchContext()));
        WaterNetworkPlugIn moduloAguasPI = (WaterNetworkPlugIn) (context.getWorkbenchContext().getBlackboard().get(WaterNetworkPlugIn.KEY));
        moduloAguasPI.addAditionalPlugIn(this);
    }

	@SuppressWarnings("unchecked")
	public boolean execute(final PlugInContext context) throws Exception{
    	reportNothingToUndoYet(context);
        LayerViewPanel layerViewPanel = (LayerViewPanel) context.getWorkbenchContext().getLayerViewPanel();
        final ArrayList<Feature> selectedFeatures = new ArrayList<Feature>();
        layerViewPanel.getSelectionManager().clear();
        Collection<Layer> layers;
        layers = (Collection<Layer>) context.getWorkbenchContext().getLayerNamePanel().getLayerManager().getLayers();
        for(Layer layer : layers){
            if (layer.getName().equals(I18N.get("WaterNetworkPlugIn","Epanet.Layer"))){
                FeatureCollection featureCollection = layer.getFeatureCollectionWrapper();
                for (Iterator<Feature> i = featureCollection.iterator(); i.hasNext();)
                    selectedFeatures.add(i.next());
            }
        }
        if (selectedFeatures.size() > 0){               
	        final TaskMonitorDialog progressDialog= new TaskMonitorDialog(context.getWorkbenchFrame().getMainFrame(), null);
	        progressDialog.setTitle(I18N.get("WaterNetworkPlugIn","Epanet.Loading"));
	        progressDialog.addComponentListener(new ComponentAdapter(){
	            public void componentShown(ComponentEvent e){
	                new Thread(new Runnable(){
						public void run(){
	                        try{
	                        	progressDialog.report(I18N.get("WaterNetworkPlugIn","Epanet.Create"));
	                        	final ViewEpanetFrame frame = new ViewEpanetFrame(context,selectedFeatures);
	                    		((WorkbenchGuiComponent)context.getWorkbenchGuiComponent()).addInternalFrame( frame);
	                    		frame.position(); 	                    		
	                        }catch(Exception e){
	                            ErrorDialog.show(context.getWorkbenchFrame().getMainFrame(), "Epanet", I18N.get("WaterNetworkPlugIn","Epanet.Error"), StringUtil.stackTrace(e));
	                            return;
	                        }finally{
	                            progressDialog.setVisible(false);
	                        }
	                    }
	              }).start();
	          }
	       });
	       GUIUtil.centreOnWindow(progressDialog);
	       progressDialog.setVisible(true);
       }
        else
    		ErrorDialog.show(context.getWorkbenchFrame().getMainFrame(), I18N.get("WaterNetworkPlugIn","Epanet.TitleError"), I18N.get("WaterNetworkPlugIn","Epanet.Error2"),"");
       return true;
    }
	
	//Frame editable donde se visualiza el fichero compatible con Epanet antes de guardarlo
	private class ViewEpanetFrame extends JInternalFrame{
		private static final long serialVersionUID = 1L;
		private JTextArea area;
		private JScrollPane scrollpane;
		private JTextField jPath;
		PlugInContext context;
		ViewEpanetFrame(final PlugInContext context, final ArrayList<Feature> selectedFeatures) {
			this.context=context;
			jbInit(selectedFeatures);
			context.getTask().getTaskComponent().addInternalFrameListener(
					new InternalFrameAdapter(){
						public void internalFrameClosed(InternalFrameEvent e){
							ViewEpanetFrame.this.dispose();
						}
					});
		}
		private void jbInit(final ArrayList<Feature> selectedFeatures)
		{
			setResizable(true);
			setClosable(true);
			setMaximizable(true);
			setIconifiable(true);
			getContentPane().setLayout(new BorderLayout());
			area = new JTextArea();
			area.setText(epanetTools.epanetString(selectedFeatures));
			scrollpane=new JScrollPane();
			updateTitle();
			scrollpane.getViewport().add(area);
			getContentPane().add(scrollpane,BorderLayout.CENTER);
			JLabel label = new JLabel(I18N.get("WaterNetworkPlugIn","Epanet.Label"));
			JPanel buttons=new JPanel();
			JButton open=new JButton(I18N.get("WaterNetworkPlugIn","Epanet.Save"));
			jPath = new JTextField(30);
			jPath.setEditable(false);
			open.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					JFileChooser fileChooser = new JFileChooser();
					FileNameExtensionFilter filter = new FileNameExtensionFilter("Epanet Input File (*.inp)", "inp");
					fileChooser.setFileFilter(filter);
					Component temporaryLostComponent = null;
					int seleccion = fileChooser.showSaveDialog(temporaryLostComponent) ;
					if (seleccion == JFileChooser.APPROVE_OPTION){
						String path = fileChooser.getSelectedFile().getAbsolutePath();
						if(!path.contains(".inp"))path=path+".inp";
							jPath.setText(path);						
					}
						File selectedFile = new File(jPath.getText());
	                    if (selectedFile.exists()) {
	                        int response = JOptionPane.showConfirmDialog(app.getMainFrame(),
	                        		I18N.get("WaterNetworkPlugIn","Epanet.TheFile") + " " + selectedFile.getName() +
	                                 " " + I18N.get("WaterNetworkPlugIn","Epanet.AlreadyExists"), I18N.get("WaterNetworkPlugIn","Epanet.GeopistaName"),
	                                JOptionPane.YES_NO_OPTION);

	                        if (response != JOptionPane.YES_OPTION) {
	                            return;
	                        }
	                    }
	                    final TaskMonitorDialog progressDialog= new TaskMonitorDialog(context.getWorkbenchFrame().getMainFrame(), null);
	        	        progressDialog.setTitle(I18N.get("WaterNetworkPlugIn","Epanet.SavingFile"));
	        	        progressDialog.addComponentListener(new ComponentAdapter(){
	        	            public void componentShown(ComponentEvent e){
	        	                new Thread(new Runnable(){
	        						public void run(){
	        	                        try{
	        	                        	 progressDialog.report(I18N.get("WaterNetworkPlugIn","Epanet.Saving"));
											 epanetTools.newEpanetFile(jPath.getText(),selectedFeatures);                       	
	        	                        }catch(Exception e){
	        	                            ErrorDialog.show(context.getWorkbenchFrame().getMainFrame(), "Epanet", I18N.get("WaterNetworkPlugIn","Epanet.SaveError"), StringUtil.stackTrace(e));
	        	                            return;
	        	                        }finally{
	        	                            progressDialog.setVisible(false);
	        	                        }                        
	        	                    }
	        	              }).start();
	        	          }
	        	       });
	        	       GUIUtil.centreOnWindow(progressDialog);
	        	       progressDialog.setVisible(true);
				}

			});
			buttons.add(label);
			buttons.add(jPath);
			buttons.add(open);
			getContentPane().add(buttons,BorderLayout.NORTH);
			getContentPane().repaint();
			setSize(900, 500);
		}
		public void position(){
			
			Rectangle rect=null;
			if (context.getTask().getTaskComponent() instanceof JInternalFrame){
				rect = ((JInternalFrame)context.getTask().getTaskComponent()).getBounds();
				int x=(int) (context.getWorkbenchGuiComponent().getDesktopPane().getWidth()-rect.getMaxX());
				if (x < 200)
					this.setLocation(context.getWorkbenchGuiComponent().getDesktopPane().getWidth()-getWidth(),0);
				else
					this.setLocation((int) rect.getMaxX(),0);
			}
		}		
		private void updateTitle(){
			this.setTitle(I18N.get("WaterNetworkPlugIn","Epanet.File"));
		}
	}	

    public MultiEnableCheck createEnableCheck(final WorkbenchContext workbenchContext){
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);
        return new MultiEnableCheck().add(checkFactory.createWindowWithLayerViewPanelMustBeActiveCheck())
        		.add(checkFactory.createAtLeastNLayersMustExistCheck(1));
    }

    public void addButton(final ToolboxDialog toolbox){
        if (!epanetPlugIn){
        	EpanetPlugIn select = new EpanetPlugIn();
            toolbox.addPlugIn(select, null, select.getIcon());
            toolbox.finishAddingComponents();
            toolbox.validate();
            epanetPlugIn = true;
        }
    }

    public Icon getIcon(){
        return IconLoader.icon("file_epanet.png");
    }
}