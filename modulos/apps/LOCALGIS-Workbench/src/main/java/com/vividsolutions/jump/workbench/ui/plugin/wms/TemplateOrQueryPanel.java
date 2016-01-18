/**
 * TemplateOrQueryPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vividsolutions.jump.workbench.ui.plugin.wms;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Iterator;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.geopista.app.AppContext;
import com.vividsolutions.jump.workbench.model.WMSTemplate;

public class TemplateOrQueryPanel extends JPanel{
	private JPanel jpNewWmsQueryExternal;
	private JPanel jpNewWmsQuery;
	private JLabel jlNewWmsQuery;
	private JCheckBox jcbNewWmsQuery;
	private JPanel jpUseTemplateExternal;
	private JPanel jpUseTemplate;
	private JLabel jlUseTemplate;
	private JCheckBox jcbUseTemplate;
	private JComboBox jcbTemplates;
	//lista de plantillas
	private List wmsTemplates;
	
	//elecciones realizadas por el usuario
	private boolean newQuery;
	private int templateId;
	
	
	
	public TemplateOrQueryPanel(List wmsTemplates){
		try{
			this.wmsTemplates=wmsTemplates;
			jbInit();
		}catch(Exception e){
		}
	}//fin constructor
	
	
	//inicialización de componentes
	private void jbInit(){
		GridLayout gl=new GridLayout();
		gl.setColumns(1);
		gl.setRows(2);
		this.setLayout(gl);
		
		//panel superior
		jpNewWmsQueryExternal=new JPanel();
		jpNewWmsQueryExternal.setPreferredSize(new Dimension(300,50));
		jpNewWmsQuery=new JPanel();
		FlowLayout fl1=new FlowLayout();
		fl1.setAlignment(FlowLayout.LEFT);
		jpNewWmsQuery.setLayout(fl1);
		jlNewWmsQuery=new JLabel();
		jlNewWmsQuery.setText(AppContext.getApplicationContext().getI18nString("templateOrQueryPanel.newWmsQuery"));
		jcbNewWmsQuery=new JCheckBox();
		jcbNewWmsQuery.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	jcbNewWmsQueryActionPerformed();
            }
        });
		jlNewWmsQuery.setLabelFor(jcbNewWmsQuery);
		jpNewWmsQuery.add(this.jcbNewWmsQuery);
		jpNewWmsQuery.add(this.jlNewWmsQuery);
		jpNewWmsQueryExternal.add(jpNewWmsQuery,BorderLayout.CENTER);
		
		//panel inferior
		jpUseTemplateExternal=new JPanel();
		jpUseTemplateExternal.setPreferredSize(new Dimension(300,50));
		jpUseTemplate=new JPanel();
		FlowLayout fl2=new FlowLayout();
		fl2.setAlignment(FlowLayout.LEFT);
		jpUseTemplate.setLayout(fl2);
		jlUseTemplate=new JLabel();
		jcbUseTemplate=new JCheckBox();
		jcbUseTemplate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	jcbUseTemplatesActionPerformed();
            }
        });
		jlUseTemplate.setLabelFor(jcbUseTemplate);
		jlUseTemplate.setText(AppContext.getApplicationContext().getI18nString("templateOrQueryPanel.useTemplate"));
		jcbTemplates=new JComboBox();
		//rellenamos el combo de plantillas
		if(wmsTemplates!=null){
			Iterator it=wmsTemplates.iterator();
			while(it.hasNext()){
				WMSTemplate wmsTemplate=(WMSTemplate)it.next();
				jcbTemplates.addItem(wmsTemplate);
			}//fin while
			
			if(wmsTemplates.size()==0)
				jcbUseTemplate.setEnabled(false);
			
			else{
				templateId=((WMSTemplate)jcbTemplates.getItemAt(0)).getId();
			}
		}//fin if		
		jcbTemplates.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	jcbTemplatesActionPerformed();
            }
        });
		jcbTemplates.setEnabled(false);
		jpUseTemplate.add(this.jcbUseTemplate);
		jpUseTemplate.add(this.jlUseTemplate);
		jpUseTemplate.add(jcbTemplates);
		jpUseTemplateExternal.add(jpUseTemplate,BorderLayout.CENTER);
		
		ButtonGroup bg=new ButtonGroup();
		bg.add(jcbNewWmsQuery);
		bg.add(jcbUseTemplate);		
		
		//inclusión de los paneles superior e inferior en el panel general
		this.add(jpNewWmsQueryExternal);
		this.add(jpUseTemplateExternal);
	
	}//fin jbInit
	
	
	
	//maneja la evento provocado cuando se pulsa sobre el checkbox: "Nueva consulta"
	private void jcbNewWmsQueryActionPerformed(){
		newQuery=true;
		jcbTemplates.setEnabled(false);
	}
	
	//maneja la evento provocado cuando se pulsa sobre el checkbox: "Usar plantilla"
	private void jcbUseTemplatesActionPerformed(){
		newQuery=false;
		jcbTemplates.setEnabled(true);
	}
	
	
	//maneja el evento provocado al seleccionar una plantilla en el combo de las mimas
	private void jcbTemplatesActionPerformed(){
		templateId=((WMSTemplate)jcbTemplates.getSelectedItem()).getId();
	}
	
	protected boolean isNewQuery(){
		return newQuery;
	}//fin método isNewQuery
	
	
	protected int getTemplateId(){
		return templateId;
	}//fin del método getTemplateId
	
	
	

}//fin clase
