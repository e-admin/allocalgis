/*
 * The Unified Mapping Platform (JUMP) is an extensible, interactive GUI
 * for visualizing and manipulating spatial features with geometry and attributes.
 *
 * Copyright (C) 2003 Vivid Solutions
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * For more information, contact:
 *
 * Vivid Solutions
 * Suite #1A
 * 2328 Government Street
 * Victoria BC  V8T 5G5
 * Canada
 *
 * (250)385-6040
 * www.vividsolutions.com
 */
package com.vividsolutions.jump.workbench.ui.plugin.wms.templates;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import com.geopista.app.AppContext;
import com.geopista.util.GeopistaFunctionUtils;
import com.vividsolutions.jump.workbench.plugin.EnableCheck;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.TransparencyPanel;
import com.vividsolutions.jump.workbench.ui.plugin.wms.MapLayerPanel;
import com.vividsolutions.jump.workbench.ui.plugin.wms.MapLayerTreeModel.LayerNode;
import com.vividsolutions.jump.workbench.ui.plugin.wms.MapLayerWizardPanel;
import com.vividsolutions.jump.workbench.ui.plugin.wms.SRSUtils;
import com.vividsolutions.wms.MapLayer;
import com.vividsolutions.wms.Style;
import com.vividsolutions.wms.WMService;


public class EditWMSTemplatePanel extends JPanel {
	private int index=0;
	private WMService service;
    private GridBagLayout gridBagLayout1 = new GridBagLayout();
    private MapLayerPanel mapLayerPanel = new MapLayerPanel();
    private DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel();
    private Border border1;
    private JLabel urlLabel = new JLabel();
    private JTextField urlTextField = new JTextField();
    private JPanel jpTopSettings;
    private JPanel jpDownSettings;
    private JLabel srsLabel = new JLabel();
    private JComboBox srsComboBox = new JComboBox(comboBoxModel);
    private JLabel transparencyLabel = new JLabel();
    private TransparencyPanel transparencyPanel = new TransparencyPanel();
    private JLabel jlFormat;
    private JComboBox jcbFormat;
    private JPanel jpStyle;
    private JLabel jlStyle;
    private JComboBox jcbStyle;
    private DefaultComboBoxModel jcbStyleModel = new DefaultComboBoxModel();
    private Map selectedStyles;
    private String description;//añadimos la descripción de la plantilla
   
    
    private String SRS;
    private String format;
   
    
    private JPanel jpStylesButtons;
    private JButton jbEditStyle;
    private JButton jbSaveStyle;
    
    //para una plantilla añadimos la descripción
    private JPanel jpDescription;
    private JLabel jlDescription;
    private JTextField jtfDescription;
    
    
    /**Constructor de la clase
     * @param service
     * @param initialChosenMapLayers
     * @param initialSRS
     * @param alpha
     * @param selectedStyles
     * @param description
     */
    public EditWMSTemplatePanel(
            WMService service,
            List initialChosenMapLayers,
            String initialSRS,
            int alpha,
            String initialFormat,
            Map selectedStyles,
            String description) {
        	this.service=service;
        	this.format=initialFormat;
        	this.selectedStyles=selectedStyles;
        	this.description=description;
        	        	
            try {
                jbInit();
                String url = service.getServerUrl();
                if (url.endsWith("?") || url.endsWith("&")) {
                    url = url.substring(0, url.length() - 1);
                }
                urlTextField.setText(url);
                mapLayerPanel.init(service, initialChosenMapLayers);
                updateComboBox();
                String srsName = SRSUtils.getName( initialSRS );
                srsComboBox.setSelectedItem(srsName);
                SRS=srsName;

                mapLayerPanel.add(new InputChangedListener() {
                    public void inputChanged() {
                        updateComboBox();
                        updateEnabledButtons();
                    }
                });
                setAlpha(alpha);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }//fin del constructor

    
 
    
    
    public List getChosenMapLayers() {
        return mapLayerPanel.getChosenMapLayers();
    }
    
    
    public Map getChoosenStyles(){
    	return this.selectedStyles;
    }
   
   
    public int getAlpha() {
        return 255 - transparencyPanel.getSlider().getValue();
    }

    private void setAlpha(int alpha) {
        transparencyPanel.getSlider().setValue(255 - alpha);
    }

    public String getSRS() {
        return this.SRS;
    }
    
    public String getEPSG(){
        int index = srsComboBox.getSelectedIndex();
        String srsCode = (String) mapLayerPanel.commonSRSList().get( index );
        return srsCode;
    	
    }
    public int getChosenSRS() {
        return srsComboBox.getSelectedIndex();
    }    
    
    public String getFormat(){
    	return this.format;
    }
    
  public String getDescription(){
	  return this.jtfDescription.getText();
  }


   
    public void updateStyles(){
    List removedElements=this.mapLayerPanel.getRemovedElements();
    
    Iterator it=removedElements.iterator();
    while(it.hasNext()){
    	LayerNode layerNode=(LayerNode) it.next();
    	String layerName=layerNode.getLayer().getName();
    	selectedStyles.remove(layerName);
    }//fin del while
   }//fin del método
    	
    	
    
    /**Inicializa los componentes
     * @throws Exception
     */
    void jbInit() throws Exception {    	
        border1 = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        this.setLayout(gridBagLayout1);
        
        //descripción
        jpDescription=new JPanel();
        FlowLayout flay=new FlowLayout();
        flay.setAlignment(FlowLayout.LEFT);
        jpDescription.setLayout(flay);
        jlDescription=new JLabel(AppContext.getApplicationContext().getI18nString("EditWMSTemplatePanel.Des"));
        jtfDescription=new JTextField(description);
        jtfDescription.setPreferredSize(new Dimension(195,21));
        jlDescription.setLabelFor(jtfDescription);
        jpDescription.add(jlDescription);
        jpDescription.add(jtfDescription);
       
        
        //SRS
        srsLabel.setText(AppContext.getApplicationContext().getI18nString("EditWMSTemplatePanel.SRS"));
        this.setBorder(border1);
        this.setToolTipText("");
        srsComboBox.setMinimumSize(new Dimension(125, 21));
        srsComboBox.setToolTipText("");
        srsComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	srsComboBoxActionPerformed(evt);
            }
        });
        //transparencia
        transparencyLabel.setText(AppContext.getApplicationContext().getI18nString("EditWMSTemplatePanel.transparencia"));
        //URL
        urlLabel.setText("URL:");
        urlTextField.setBorder(null);
        urlTextField.setOpaque(false);
        urlTextField.setEditable(false);
        
 
        jpTopSettings=new JPanel();
        FlowLayout fl=new FlowLayout();
        fl.setAlignment(FlowLayout.LEFT);
        jpTopSettings.setLayout(fl);
        jpDownSettings=new JPanel();
        FlowLayout fl1=new FlowLayout();
        fl1.setAlignment(FlowLayout.LEFT);
        jpDownSettings.setLayout(fl1);
        jpStyle=new JPanel();
        jpStyle.setPreferredSize(new Dimension(300,100));
        TitledBorder tbStyle = BorderFactory.createTitledBorder(AppContext.getApplicationContext().getI18nString("EditWMSTemplatePanel.estilo"));
        jpStyle.setBorder(tbStyle);
        jlStyle=new JLabel(AppContext.getApplicationContext().getI18nString("estilo")+":");
        jcbStyle=new JComboBox(jcbStyleModel);
        jlStyle.setLabelFor(jcbStyle);
        jcbStyle.setEnabled(false);
        jlStyle.setEnabled(false);
        jlFormat=new JLabel(AppContext.getApplicationContext().getI18nString("getFeatureInfo.formatString"));
        jcbFormat=new JComboBox(service.getCapabilities().getMapFormats());
        jcbFormat.setSelectedItem(format);
        jcbFormat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	jcbFormatActionPerformed(evt);
            }
        });
        
  
        jlFormat.setLabelFor(jcbFormat);
        jpTopSettings.add(srsLabel);
        jpTopSettings.add(srsComboBox);
        jpTopSettings.add(transparencyLabel);
        jpTopSettings.add(transparencyPanel);
        jpDownSettings.add(jlFormat);
        jpDownSettings.add(jcbFormat);
        jpDownSettings.add(jpStyle);
        jpStyle.add(jlStyle,BorderLayout.CENTER);
        jpStyle.add(jcbStyle,BorderLayout.CENTER);
        jpStylesButtons=new JPanel();
        GridLayout gb=new GridLayout(2,1);
        jpStylesButtons.setLayout(gb);
        jbEditStyle=new JButton(AppContext.getApplicationContext().getI18nString("style.edit"));
        jbSaveStyle=new JButton(AppContext.getApplicationContext().getI18nString("style.save"));
        jbEditStyle.setEnabled(false);
        jbSaveStyle.setEnabled(false);
        jpStylesButtons.add(jbEditStyle);
        jpStylesButtons.add(jbSaveStyle);
        jpStyle.add(jpStylesButtons,BorderLayout.EAST);
        
        
        jbEditStyle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	jbEditStyleActionPerformed(evt);
            }
        });
        jbSaveStyle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	jbSaveStyleActionPerformed(evt);
            }
        });
        
 
                
        this.add(
            mapLayerPanel,
            new GridBagConstraints(
                1,
                2,
                3,
                1,
                1.0,
                1.0,
                GridBagConstraints.CENTER,
                GridBagConstraints.BOTH,
                new Insets(10, 0, 10, 0),
                0,
                0));
        
        this.add(
                jpTopSettings,
                new GridBagConstraints(
                    1,
                    3,
                    2,
                    1,
                    0.0,
                    0.0,
                    GridBagConstraints.WEST,
                    GridBagConstraints.NONE,
                    new Insets(0, 0, 10, 5),
                    0,
                    0));
        this.add(
                jpDownSettings,
                new GridBagConstraints(
                    1,
                    6,
                    2,
                    1,
                    0.0,
                    0.0,
                    GridBagConstraints.WEST,
                    GridBagConstraints.NONE,
                    new Insets(0, 0, 10, 5),
                    0,
                    0));
        this.add(
                jpDescription,
                new GridBagConstraints(
                    1,
                    8,
                    2,
                    1,
                    0.0,
                    0.0,
                    GridBagConstraints.WEST,
                    GridBagConstraints.NONE,
                    new Insets(0, 0, 10, 5),
                    0,
                    0));
        
        this.add(
            urlLabel,
            new GridBagConstraints(
                1,
                0,
                1,
                1,
                0.0,
                0.0,
                GridBagConstraints.WEST,
                GridBagConstraints.NONE,
                new Insets(0, 0, 0, 5),
                0,
                0));
        this.add(
            urlTextField,
            new GridBagConstraints(
                2,
                0,
                2,
                1,
                0.0,
                0.0,
                GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0),
                0,
                0));
    }//fin del método jbInit
    
    
    
    
    /**
     * Method updateComboBox.
     */
     private void updateComboBox() {
         String selectedSRS = (String) srsComboBox.getSelectedItem();
         comboBoxModel.removeAllElements();

         for (Iterator i = mapLayerPanel.commonSRSList().iterator(); i.hasNext();) {
             String commonSRS = (String) i.next();
             String srsName = SRSUtils.getName( commonSRS );
             comboBoxModel.addElement( srsName );
         }

         //selectedSRS might no longer be in the combobox, in which case nothing will be selected. [Jon Aquino]
         srsComboBox.setSelectedItem(selectedSRS);
         if ((srsComboBox.getSelectedItem() == null)
             && (srsComboBox.getItemCount() > 0)) {
             srsComboBox.setSelectedIndex(0);
         }
     }//fin del método updateComboBox
     
     
    
    /**Obtiene la lista de estilos disponibles para una capa determinada
     * @param Nombre de la capa de la que deseamos conocer sus estilos
     * @return Lista de estilos
     */
    private List getLayerStyles(String layerName){
    	ArrayList styles=null;
    	Map map=service.getCapabilities().getStyles();
    	styles=(ArrayList) map.get(layerName);
    	return styles;
    }//fin del método getLayerStyles
    
    
    
	 /**Acción realizada al seleccionar un formato en el combo de los mismos
     */
	private void jcbFormatActionPerformed(ActionEvent evt) {
		this.format=(String) jcbFormat.getSelectedItem();
	}//fin del método
	
	

	 /**Acción realizada al seleccionar un SRS en el combo de los mismos
    */
	private void srsComboBoxActionPerformed(ActionEvent evt) {
		this.SRS=(String) srsComboBox.getSelectedItem();
	}//fin del método
	
	
	
    /**Acción realizada al pulsar el botón "Editar Estilo"
     */
	private void jbEditStyleActionPerformed(ActionEvent evt) {
		jbEditStyle.setEnabled(false);
		jlStyle.setEnabled(true);
		jcbStyle.setEnabled(true);
		jbSaveStyle.setEnabled(true);
		updatejcbStyles();
	}//fin del método
	
	
	  /**Acción realizada al pulsar el botón "Guardar Estilo"
     */
	private void jbSaveStyleActionPerformed(ActionEvent evt) {
		saveStyle();
		  JOptionPane.showMessageDialog(this, 
				  GeopistaFunctionUtils.i18n_getname("EditWMSQuery.stylesSaved"), 
				  GeopistaFunctionUtils.i18n_getname("EditWMSQuery.stylesSaved"),
                  JOptionPane.INFORMATION_MESSAGE);
		 
	
	}//fin del método
	
	
	 /**Almacena el estilo seleccionado por el usuario
     */
	private void saveStyle() {
		String style=(String) jcbStyle.getSelectedItem();
		if(style!=null){
			List selectedLayer=this.mapLayerPanel.getSelectedItemsFromRigthList();
			if(selectedLayer!=null){
	     	LayerNode node= (LayerNode) selectedLayer.get(0);
	     	MapLayer layer=node.getLayer();
	     	String layerName=layer.getName();
	     	selectedStyles.put(layerName,style);
			}//fin if
		}//fin if
	}//fin del método
	
	
	 /**Actualiza el comboBox de estilos con los estilos disponibles para la capa seleccionada
     */
    private void updatejcbStyles(){    	 
    	List selectedLayer=this.mapLayerPanel.getSelectedItemsFromRigthList();
    	if(selectedLayer.size()==1){
    	LayerNode node= (LayerNode) selectedLayer.get(0);
    	MapLayer layer=node.getLayer();
    	
    	
    	List styles=getLayerStyles(layer.getName());
    	Iterator it=styles.iterator();
    	
    	while(it.hasNext()){
    		Style style=(Style) it.next();
    		jcbStyleModel.addElement(style.getName());
    	}//fin del while
    	
    	if(selectedStyles!=null){
    	String style=(String) selectedStyles.get(layer.getName());
    	if(style!=null)
    	jcbStyle.setSelectedItem(style);
    	}
    	}
    }//fin del método
    
    
    
    
    
    private void updateEnabledButtons(){
    	try{
    	jcbStyleModel.removeAllElements();
    	 if(mapLayerPanel.getSelectedItemsFromRigthList().size()==1){
    		 //cuando hay una capa seleccionada
             jbEditStyle.setEnabled(true);
             jbSaveStyle.setEnabled(false);
             jcbStyle.setEnabled(false);
             jlStyle.setEnabled(false);
    	 }
    	 else {//cuando hay más de una capa seleccionada
    		  jbEditStyle.setEnabled(false);
              jbSaveStyle.setEnabled(false);
              jcbStyle.setEnabled(false);
              jlStyle.setEnabled(false); 
    	 }
    	}catch(ArrayIndexOutOfBoundsException e){
    		//cuando no hay ninguna capa seleccionada
    		  jbEditStyle.setEnabled(false);
              jbSaveStyle.setEnabled(false);
              jcbStyle.setEnabled(false);
              jlStyle.setEnabled(false);     		
    	}//fin del catch
    }//fin del método
    
    
    
    
    
    private EnableCheck[] enableChecks =
        new EnableCheck[] {
            new EnableCheck() { public String check(JComponent component) {
                return mapLayerPanel.getChosenMapLayers().isEmpty()
                    ? "Debe seleccionar al menos una capa"
                    : null;
            }
        }, new EnableCheck() {
            public String check(JComponent component) {
                return srsComboBox.getSelectedItem() == null
                    ? MapLayerWizardPanel.NO_COMMON_SRS_MESSAGE
                    : null;
            }
        }
    };
    
    
    public EnableCheck[] getEnableChecks() {
        return enableChecks;
    }
    
 
    
}//fin de la clase
