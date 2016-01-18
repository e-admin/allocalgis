/**
 * DeterminacionesTreeListener.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.gestorfip.app.planeamiento.dialogs.gestorplaneamiento.panels;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.gestorfip.app.planeamiento.beans.panels.tramite.DeterminacionPanelBean;
import com.gestorfip.app.planeamiento.beans.panels.tramite.TramitePanelBean;
import com.gestorfip.app.planeamiento.utils.ConstantesGestorFIP;

public class DeterminacionesTreeListener implements TreeSelectionListener{
	private GestorFipGestorPlaneamientoPanel panel;
	
	private JTree treeSeleccionado = null;


	public DeterminacionesTreeListener(GestorFipGestorPlaneamientoPanel panel){
		this.panel= panel;
	}
	

	public void valueChanged(TreeSelectionEvent arg0) {
		
		treeSeleccionado = (JTree)arg0.getSource();

		DefaultMutableTreeNode nodeDeterminaciones = (DefaultMutableTreeNode) (treeSeleccionado).getLastSelectedPathComponent();

		if(nodeDeterminaciones != null){
			Object nodeInfo = nodeDeterminaciones.getUserObject();	
			
			if(nodeInfo instanceof DeterminacionPanelBean){
				if(treeSeleccionado.getSelectionCount() == 1){
					
					panel.getBtnVerEntidadesAsociadas().setEnabled(true);
					panel.getBtnVerPropiedadesDeterminacion().setEnabled(true);
				}
				else{
					panel.getBtnVerEntidadesAsociadas().setEnabled(false);
					panel.getBtnVerPropiedadesDeterminacion().setEnabled(false);
				}
				
			}
			else{
				panel.getBtnVerEntidadesAsociadas().setEnabled(false);
				panel.getBtnVerPropiedadesDeterminacion().setEnabled(false);
				//panel.getBtnAsociar().setEnabled(false);
			}
			
			
			//se deseleccionana los arboles que no han sido pulsados
			// para que solo haya una determinacion seleccionada en los arboles que es la
			// que se esta mostrando su informacion en los paneles
			if(treeSeleccionado != null && treeSeleccionado.getName() != null){

				if(treeSeleccionado.getName().equals(ConstantesGestorFIP.TRAMITE_PLANEAMIENTO_VIGENTE_DETERMINACIONES)){
					panel.setNodeSeleccionadoDeterminaciones(nodeDeterminaciones);
					panel.setArboSeleccionadoDeterminaciones(ConstantesGestorFIP.TRAMITE_PLANEAMIENTO_VIGENTE_DETERMINACIONES);
				}
			}
		}
	}

	
	public JTree getTreeSeleccionado() {
		return treeSeleccionado;
	}

	public void setTreeSeleccionado(JTree treeSeleccionado) {
		this.treeSeleccionado = treeSeleccionado;
	}
}
