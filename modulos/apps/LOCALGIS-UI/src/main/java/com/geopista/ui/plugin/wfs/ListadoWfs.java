/**
 * ListadoWfs.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.wfs;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.util.config.UserPreferenceStore;
import com.vividsolutions.jump.I18N;

public class ListadoWfs{
	private JDialog padre;
	private JTextField jTextNombre = new JTextField();
	private JTextField jTextUrl = new JTextField();
	private JDialog dialogLayers;
	

	public ListadoWfs(JDialog padre){
		this.padre = padre;
	}
	
	/**
	 * Saco un panel para permitir al usuario añadir un servicio de gazeteer o nomenclátor sobre el que 
	 * poder realizar búsquedas
	 */
	public void addWfs(){
		createPanel(false);
	}
	
	/**
	 * Saco un panel para permitir al usuario modificar un servicio de gazeteer o nomenclátor sobre el que 
	 * poder realizar búsquedas
	 */
	public void modifyWfs(){
		createPanel(true);
	}
	
	private void createPanel(boolean modificar){
    	dialogLayers = new JDialog(padre,I18N.get("WfsDialog","Datos Servicio"),true);
    	dialogLayers.getContentPane().setLayout(null);
    	JLabel jlabelNombre = new JLabel(I18N.get("WfsDialog","NombreWfs"));
    	jlabelNombre.setBounds(20,20,200,20);
    	dialogLayers.getContentPane().add(jlabelNombre);
    	jTextNombre.setBounds(150,20,200,20);
    	dialogLayers.getContentPane().add(jTextNombre);
    	JLabel jlabelUrl = new JLabel(I18N.get("WfsDialog","UrlWfs"));
    	jlabelUrl.setBounds(20,60,200,20);
    	dialogLayers.getContentPane().add(jlabelUrl);
    	jTextUrl.setBounds(150,60,200,20);
    	dialogLayers.getContentPane().add(jTextUrl);
    	if (modificar){
    		String entradaSeleccionada = (String)((WfsDialog)padre).wfsList.getSelectedValue();
    		jTextNombre.setText(entradaSeleccionada);
    		jTextNombre.setEnabled(false);
    		jTextUrl.setText(searchHost());
    	}else{
    		jTextNombre.setText("");
    		jTextNombre.setEnabled(true);
    		jTextUrl.setText("");
    	}
    	JButton jButton = new JButton(AppContext.getApplicationContext().getI18nString("btnAceptar"));
    	jButton.setBounds(200,100,100,20);
    	jButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
            	if (jTextNombre.isEnabled()){
	            	addHost(jTextNombre.getText(), jTextUrl.getText());
	    			if (((WfsDialog)padre).radioMne.isSelected()){
		            	((WfsDialog)padre).wfsmne.listaMnes.add(jTextNombre.getText());
		            	((WfsDialog)padre).wfsmne.listaURLs.add(jTextUrl.getText());
		            	((WfsDialog)padre).wfsList.setListData(((WfsDialog)padre).wfsmne.listaMnes.toArray());
	    			}else{
		            	((WfsDialog)padre).wfsg.listaWfsG.add(jTextNombre.getText());
		            	((WfsDialog)padre).wfsg.listaURLs.add(jTextUrl.getText());
		            	((WfsDialog)padre).wfsList.setListData(((WfsDialog)padre).wfsg.listaWfsG.toArray());
	    			}
            	}else{
	            	modifyHost(jTextUrl.getText());
	        		int index = ((WfsDialog)padre).wfsList.getSelectedIndex();
	    			if (((WfsDialog)padre).radioMne.isSelected()){
		            	((WfsDialog)padre).wfsmne.listaMnes.set(index, jTextNombre.getText());
		            	((WfsDialog)padre).wfsmne.listaURLs.set(index, jTextUrl.getText());
		            	((WfsDialog)padre).wfsList.setListData(((WfsDialog)padre).wfsmne.listaMnes.toArray());
	    			}else{
		            	((WfsDialog)padre).wfsg.listaWfsG.set(index, jTextNombre.getText());
		            	((WfsDialog)padre).wfsg.listaURLs.set(index, jTextUrl.getText());
		            	((WfsDialog)padre).wfsList.setListData(((WfsDialog)padre).wfsg.listaWfsG.toArray());
	    			}
            	}
	            dialogLayers.dispose();
            }
        });
    	dialogLayers.getContentPane().add(jButton);
    	dialogLayers.setSize(new Dimension(400,250));
    	dialogLayers.setVisible(true);
		
	}
	
	/**
	 * Elimino un servicio de la lista 
	 */
	public void eliminateWfs(){
		int index = ((WfsDialog)padre).wfsList.getSelectedIndex();
		if (((WfsDialog)padre).wfsList.getSelectedIndex() == -1){
			String errorMessage = I18N.get("WfsDialog","SeleccionWfs");
	        JOptionPane.showMessageDialog(this.padre,errorMessage,
		            AppContext.getMessage("GeopistaName"),JOptionPane.ERROR_MESSAGE);
		}else{
			eliminateHost();
			if (((WfsDialog)padre).radioMne.isSelected()){
				((WfsDialog)padre).wfsmne.listaMnes.remove(index);
				((WfsDialog)padre).wfsmne.listaURLs.remove(index);
				((WfsDialog)padre).wfsList.setListData(((WfsDialog)padre).wfsmne.listaMnes.toArray());		
			}else{
				((WfsDialog)padre).wfsg.listaWfsG.remove(index);
				((WfsDialog)padre).wfsg.listaURLs.remove(index);
				((WfsDialog)padre).wfsList.setListData(((WfsDialog)padre).wfsg.listaWfsG.toArray());		
			}
		}
	}
	
	//Se busca el host correspondiente al servicio WFS que se está seleccionando en este momento
	private String searchHost(){
		String entradas[] = null;
		if (((WfsDialog)padre).radioMne.isSelected())
			entradas = UserPreferenceStore.getUserPreference(UserPreferenceConstants.LISTA_MNE,"",false).split(";");
		else
			entradas = UserPreferenceStore.getUserPreference(UserPreferenceConstants.LISTA_WFS_G,"",false).split(";");
		int n = entradas.length;
		String entradaSeleccionada = (String)((WfsDialog)padre).wfsList.getSelectedValue();
		for (int i=0;i<n;i++){
			if (entradas[i].startsWith(entradaSeleccionada+",")){
				String[] entradaActual = entradas[i].split(","); 
				return entradaActual[1];
			}
		}
		return "";
	}

	//Se modifican datos del host correspondiente al servicio WFS que se está seleccionando en este momento
	private void modifyHost(String stNuevoValor){
		String entradas[] = null;
		if (((WfsDialog)padre).radioMne.isSelected())
			entradas = UserPreferenceStore.getUserPreference(UserPreferenceConstants.LISTA_MNE,"",false).split(";");
		else
			entradas = UserPreferenceStore.getUserPreference(UserPreferenceConstants.LISTA_WFS_G,"",false).split(";");
		int n = entradas.length;
		String entradaSeleccionada = (String)((WfsDialog)padre).wfsList.getSelectedValue();
		StringBuffer st = new StringBuffer();
		for (int i=0;i<n;i++){
			if (!entradas[i].startsWith(entradaSeleccionada+","))
				st.append(entradas[i]);
			else{
				if (i>0)
					st.append(";");
				String[] entradaActual = entradas[i].split(",");
				st.append(entradaActual[0]+","+stNuevoValor+";");
			}
		}
		if (((WfsDialog)padre).radioMne.isSelected())
			UserPreferenceStore.setUserPreference(UserPreferenceConstants.LISTA_MNE,st.toString());
		else
			UserPreferenceStore.setUserPreference(UserPreferenceConstants.LISTA_WFS_G,st.toString());
	}
	
	//Se elimina el host correspondiente al servicio WFS que se está seleccionando en este momento
	private void eliminateHost(){
		String entradas[] = null;
		if (((WfsDialog)padre).radioMne.isSelected())
			entradas = UserPreferenceStore.getUserPreference(UserPreferenceConstants.LISTA_MNE,"",false).split(";");
		else
			entradas = UserPreferenceStore.getUserPreference(UserPreferenceConstants.LISTA_WFS_G,"",false).split(";");
		int n = entradas.length;
		String entradaSeleccionada = (String)((WfsDialog)padre).wfsList.getSelectedValue();
		StringBuffer st = new StringBuffer();
		for (int i=0;i<n;i++){
			if (!entradas[i].startsWith(entradaSeleccionada+",")){
				if (i > 0)
					st.append(";");
				st.append(entradas[i]);
			}
		}
		if (((WfsDialog)padre).radioMne.isSelected())
			UserPreferenceStore.setUserPreference(UserPreferenceConstants.LISTA_MNE,st.toString());
		else
			UserPreferenceStore.setUserPreference(UserPreferenceConstants.LISTA_WFS_G,st.toString());
	}
	
	//Se añado datos de un nuevo host para el servicio WFS que se está seleccionando en este momento
	private void addHost(String host, String value){
		String entradas = null;
		if (((WfsDialog)padre).radioMne.isSelected())
			entradas = UserPreferenceStore.getUserPreference(UserPreferenceConstants.LISTA_MNE,"",false);
		else
			entradas = UserPreferenceStore.getUserPreference(UserPreferenceConstants.LISTA_WFS_G,"",false);
		if (!entradas.equals(""))
			entradas = entradas.concat(";");
		entradas = entradas.concat(host+","+value);
		if (((WfsDialog)padre).radioMne.isSelected())
			UserPreferenceStore.setUserPreference(UserPreferenceConstants.LISTA_MNE,entradas);
		else
			UserPreferenceStore.setUserPreference(UserPreferenceConstants.LISTA_WFS_G,entradas);
	}
	
	/**
	 * Añado los Mnes que tengo almacenados en el registro del usuario
	 */
	public void addWfsRegistry(){
		//Añado los valores que tengo almacenados en el registro para el WFS-MNE
		String valores1[] = UserPreferenceStore.getUserPreference(UserPreferenceConstants.LISTA_MNE,"",false).split(";");
		int n = valores1.length;
		for (int i=0;i<n;i++){
			if (!valores1[i].equals("")){
				String[] valor = valores1[i].split(","); 
		        ((WfsDialog)padre).wfsmne.listaMnes.add(valor[0]);
		        ((WfsDialog)padre).wfsmne.listaURLs.add(valor[1]);
			}
		}
		//Añado los valores que tengo almacenados en el registro para el WFS-G

		String valores2[] = UserPreferenceStore.getUserPreference(UserPreferenceConstants.LISTA_WFS_G,"",false).split(";");
		n = valores2.length;
		for (int i=0;i<n;i++){
			if (!valores2[i].equals("")){
				String[] valor = valores2[i].split(","); 
		        ((WfsDialog)padre).wfsg.listaWfsG.add(valor[0]);
		        ((WfsDialog)padre).wfsg.listaURLs.add(valor[1]);
			}
		}
		if (((WfsDialog)padre).radioMne.isSelected())
	        ((WfsDialog)padre).wfsList.setListData(((WfsDialog)padre).wfsmne.listaMnes.toArray());
		else
	        ((WfsDialog)padre).wfsList.setListData(((WfsDialog)padre).wfsg.listaWfsG.toArray());
	}

}