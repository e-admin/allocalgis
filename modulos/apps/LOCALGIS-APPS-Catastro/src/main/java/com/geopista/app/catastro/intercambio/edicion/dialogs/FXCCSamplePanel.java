/**
 * FXCCSamplePanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.intercambio.edicion.dialogs;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.FileOutputStream;

import javax.swing.JButton;
import javax.swing.JDesktopPane;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.model.beans.FX_CC;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

public class FXCCSamplePanel {
	FxccPanel panelDXF;
	ASCPanel panelASC;
	FX_CC fxcc=null;
	
	private static final double BUFFERDISTANCEINCREMENT = 10.0;
    
	private JButton jButtonGrabarDXF = null;
	
	public FXCCSamplePanel(){
    	javax.swing.JFrame fr=new javax.swing.JFrame();
    	fr.setSize(800, 500);
    	JDesktopPane desktopPane= new JDesktopPane();

        desktopPane.setMinimumSize(null);
		desktopPane.setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
        fr.getContentPane().add(desktopPane, java.awt.BorderLayout.CENTER);
        panelDXF=new FxccPanel();     
        panelDXF.setDxfData("8791801UN8189S",211);
        panelASC=new ASCPanel();     
        panelASC.setDxfData("8791801UN8189S",211);
    	fr.getContentPane().add(panelDXF);    	
    	fr.show();
    	panelDXF.insertarCapasGeopista=false;
    	panelASC.enter();
    	panelDXF.initDXF();
    	
    	
		panelDXF.add(this.getJButtonGrabarDXF(fxcc), 
				new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.WEST,
						GridBagConstraints.NONE, new Insets (0,0,0,0), 0,0));

		//Desactivamos las capas que inicialmente no deseamos que se muestren
		//para facilitar la visualizacion.
		//Planta General
		String tipoPlantasGen[]={"AA","LI","AS","LP","LF","------"};
		for (int j=0;j<tipoPlantasGen.length;j++){
			String nameLayer="PG-"+tipoPlantasGen[j];		
			Layer layer=panelDXF.geopistaEditor.getLayerManager().getLayer(nameLayer);
			if (layer!=null)layer.setVisible(false);
		}
		
		
		//Plantas significativas
		for (int i=1;i<9;i++){
			if (i==1)continue;
			String filledLayer=this.fillWithCeros(i);
			String tipoPlantasSign[]={"LP","LI","AU","AS","AL","TL","TP","TO","CO"};
			for (int j=0;j<tipoPlantasSign.length;j++){
				String nameLayer="PS"+filledLayer+"-"+tipoPlantasSign[j];
				Layer layer=panelDXF.geopistaEditor.getLayerManager().getLayer(nameLayer);
				if (layer!=null)layer.setVisible(false);				
			}
		}
		Layer superficies=panelDXF.geopistaEditor.getLayerManager().getLayer("PS01-AS");	
	    FeatureCollection capaSuperficies = superficies.getFeatureCollectionWrapper().getUltimateWrappee();

		
		/*Layer textos=panelDXF.geopistaEditor.getLayerManager().getLayer("PS01-AU");	
	    List listTextos = textos.getFeatureCollectionWrapper().getFeatures();
    	Iterator capaTextosIter = listTextos.iterator();
    	while (capaTextosIter.hasNext()){
    		Feature sourceFeature = (Feature) capaTextosIter.next();
    		Feature actualNearestFeature = nearestFeaturesBrute(sourceFeature, capaSuperficies);
    	}*/
	}
	

	
	
    /**
     * BORRAR
     * @param fxcc 
     * @return
     */
    public JButton getJButtonGrabarDXF(FX_CC fxcc)
    {
    	
    	if (jButtonGrabarDXF  == null)
    	{
    		jButtonGrabarDXF = new JButton();
    		jButtonGrabarDXF.setText(I18N.get("Expedientes", "GRABAR(BORRAR)"));

    		jButtonGrabarDXF.addActionListener(new java.awt.event.ActionListener()
    		{
    			public void actionPerformed(java.awt.event.ActionEvent e)
    			{

    				final TaskMonitorDialog progressDialog = new TaskMonitorDialog(AppContext.getApplicationContext().getMainFrame(), null);
    				//progressDialog.setTitle(I18N.get("Expedientes","fxcc.panel.CargandoFicheroDXF"));
    				progressDialog.setTitle("TaskMonitorDialog.Wait");
                    progressDialog.report(I18N.get("Expedientes","fxcc.panel.CargandoPlantillaDXF"));
    				progressDialog.addComponentListener(new ComponentAdapter()
    				{
    					public void componentShown(ComponentEvent e)
    					{   
    						new Thread(new Runnable()
    						{
    							public void run()
    							{
    								try
    								{       				
    									FX_CC fxcc=new FX_CC();
                                    	panelDXF.guardarFXCC(fxcc);
                                    	panelASC.guardarASC(panelDXF.textoAscTemp,fxcc,panelDXF.geopistaEditor);
                            	        FileOutputStream fos = new FileOutputStream("c:\\tmp\\5155102\\5155102-MODIF.asc" );
                            	        fos.write( fxcc.getASC().getBytes() );
                            	        fos.close();

    								} 
    								catch (Exception e)
    								{
    									e.printStackTrace();
    								} 
    								finally
    								{
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

    		jButtonGrabarDXF.setName("_nuevodxf");
    	}
    	return jButtonGrabarDXF;
    }
    	
    private String fillWithCeros(int id){
    	String filledLayer=String.valueOf(id);    
    	if (id<10)
    		filledLayer="0"+id;
    	return filledLayer;	
    }	
	 /**
     * 
     * @param args
     */
    public static void main(String args[]){    
    	new FXCCSamplePanel();
    }
}
