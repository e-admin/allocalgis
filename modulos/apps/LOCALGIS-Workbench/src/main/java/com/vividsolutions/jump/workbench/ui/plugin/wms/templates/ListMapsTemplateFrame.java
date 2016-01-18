/**
 * ListMapsTemplateFrame.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vividsolutions.jump.workbench.ui.plugin.wms.templates;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.geopista.util.GeopistaFunctionUtils;


/**Cuadro de diálogo que se muestra cuando el usuario pretende borrar o editar una plantilla asociada a una lista de mapas.
 * En esta ventana se muestra el listado de mapas asociado a la misma y se pregunta al usuario si desea continuar con la operación.
 * @author Silvia García
 */
public class ListMapsTemplateFrame extends JDialog{
	//¿Se trata de una edición o de un borrado?
	private JPanel jpQuestion;
	private JLabel jlQuestion1;
	private JLabel jlQuestion2;
	private JLabel jlQuestion3;
	private boolean isEdition;
	
	
	//tabla de mapas
	private JScrollPane jspMaps;
	private JTable jtMaps;
	private Object data[][];
	private String[] columnNames={""};
	
	//botones
	private JPanel jpButtons;
	private JButton jbYes;
	private JButton jbNo;
	
	//Respuesta del usuario
	private boolean answerYes;
	private HandleTemplatesFrame parent;
	
	
	/**Constructor
	 */
	public ListMapsTemplateFrame(LinkedList maps, boolean isEdition,HandleTemplatesFrame parent){
		this.isEdition=isEdition;
		this.parent=parent;
		this.setModal(true);
		
		//rellenamos la matriz de datos con la lista de mapas
		data=new String[maps.size()][1];
		for (int i=0;i<maps.size();i++){
			data[i][0]="  "+maps.get(i);
		}//fin for
		
		try{
			jbInit();
		}catch(Exception e){
			System.out.println("Se ha producido un error en la inicialización del panel de mapas asociados a una plantilla WMS");
		}
	}//fin constructor
	
	
	/**Inicializa los componentes del panel
	 */
	private void jbInit ()throws Exception{
		this.setTitle(GeopistaFunctionUtils.i18n_getname("ListMapsTemplateFrame.title"));
		this.setPreferredSize(new Dimension(300,300));
		
		//panel superior con la pregunta realizada al usuario
		String question1;
		String question2;
		String question3;
		if(isEdition)
			question1="   "+GeopistaFunctionUtils.i18n_getname("ListMapsTemplateFrame.edition1");
		else
			question1="   "+GeopistaFunctionUtils.i18n_getname("ListMapsTemplateFrame.delete1");
			
		question2="   "+GeopistaFunctionUtils.i18n_getname("ListMapsTemplateFrame.edition2");
		question3="   "+GeopistaFunctionUtils.i18n_getname("ListMapsTemplateFrame.edition3");
				
		jlQuestion1=new JLabel(question1);
		jlQuestion2=new JLabel(question2);
		jlQuestion3=new JLabel(question3);
		jpQuestion=new JPanel();
		GridLayout gl=new GridLayout();
		gl.setColumns(1);
		gl.setRows(5);
		jpQuestion.setLayout(gl);
		jpQuestion.add(new JLabel());
		jpQuestion.add(jlQuestion1);
		jpQuestion.add(jlQuestion2);
		jpQuestion.add(jlQuestion3);
		jpQuestion.add(new JLabel());
		add(jpQuestion,BorderLayout.NORTH);
		
		//tabla de mapas
		jtMaps=new JTable(data, columnNames);
		jtMaps.setEnabled(false);
		jspMaps=new JScrollPane(jtMaps);
		add(jspMaps,BorderLayout.CENTER);
		
		
		//panel de botones
		jpButtons=new JPanel();
		add(jpButtons,BorderLayout.SOUTH);
		FlowLayout fl=new FlowLayout();
		fl.setAlignment(FlowLayout.RIGHT);
		jpButtons.setLayout(fl);
		
		
		//botón No
		jbNo=new JButton(GeopistaFunctionUtils.i18n_getname("ListMapsTemplateFrame.No"));
		jbNo.addActionListener(new java.awt.event.ActionListener() {
	        public void actionPerformed(java.awt.event.ActionEvent evt) {
	        	jbNoActionPerformed(evt);
	        }
	    });
		jpButtons.add(jbNo);
		
		//botón Sí
		jbYes=new JButton(GeopistaFunctionUtils.i18n_getname("ListMapsTemplateFrame.Yes"));
		jbYes.addActionListener(new java.awt.event.ActionListener() {
	        public void actionPerformed(java.awt.event.ActionEvent evt) {
	        	jbYesActionPerformed(evt);
	        }
	    });
		jpButtons.add(jbYes);
		
			
		
	}//fin jbInit
	
	
	//Acción realizada al pulsar el botón Sí
	private void jbYesActionPerformed(ActionEvent e){
		answerYes=true;
		dispose();
		if(this.isEdition)
			parent.continueEditOperationAfterShowMapList(this);
		else
			parent.continueDeleteOperationAfterShowMapList(this);
	}
	
	
	//Acción realizada al pulsar el botón No
	private void jbNoActionPerformed(ActionEvent e){
		answerYes=false;
		dispose();
		if(this.isEdition)
			parent.continueEditOperationAfterShowMapList(this);
		else
			parent.continueDeleteOperationAfterShowMapList(this);
	}
	
	
	public boolean getUserAnswer_IsYes(){
		return answerYes;
	}
	

}//fin clase
