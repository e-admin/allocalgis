/**
 * EntidadesTreeListener.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.gestorfip.app.planeamiento.dialogs.gestorplaneamiento.panels;

import java.util.Enumeration;
import java.util.HashMap;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.geopista.app.AppContext;
import com.geopista.util.ApplicationContext;
import com.gestorfip.app.planeamiento.beans.panels.tramite.DeterminacionPanelBean;
import com.gestorfip.app.planeamiento.beans.panels.tramite.EntidadPanelBean;
import com.gestorfip.app.planeamiento.beans.panels.tramite.GrupoAplicacionPanelBean;
import com.gestorfip.app.planeamiento.beans.panels.tramite.TramitePanelBean;
import com.gestorfip.app.planeamiento.beans.tramite.DeterminacionBean;
import com.gestorfip.app.planeamiento.dialogs.TreeRenderer;
import com.gestorfip.app.planeamiento.utils.ConstantesGestorFIP;
import com.gestorfip.app.planeamiento.ws.cliente.ClientGestorFip;

public class EntidadesTreeListener implements TreeSelectionListener{
	GestorFipGestorPlaneamientoPanel panel;
	
	private ApplicationContext aplicacion = (AppContext)AppContext.getApplicationContext();
	
	public EntidadesTreeListener(GestorFipGestorPlaneamientoPanel panel){
		this.panel= panel;
	}

	public void valueChanged(TreeSelectionEvent arg0) {
		int entidadesSeleccionadasArboles = 0;
		JTree treeEntidadesSeleccionado = (JTree)arg0.getSource();
		DefaultMutableTreeNode nodeEntidades = (DefaultMutableTreeNode) (treeEntidadesSeleccionado).getLastSelectedPathComponent();
		
		if(nodeEntidades != null){
			Object nodeInfoEntidades = nodeEntidades.getUserObject();	
			
			
			if(nodeInfoEntidades instanceof EntidadPanelBean){
				if(treeEntidadesSeleccionado.getSelectionCount() == 1){
				
					panel.getBtnVerDeterminacionesAsociadas().setEnabled(true);
					panel.getBtnVerPropiedadesEntidad().setEnabled(true);
				}
				else{
					panel.getBtnVerDeterminacionesAsociadas().setEnabled(false);
					panel.getBtnVerPropiedadesEntidad().setEnabled(false);
				}
			}
			else{
				panel.getBtnVerDeterminacionesAsociadas().setEnabled(false);
				panel.getBtnVerPropiedadesEntidad().setEnabled(false);

			}
			
	
			panel.seleccionarEntidadTreeToMapa(treeEntidadesSeleccionado);
			
			//se deseleccionana los arboles que no han sido pulsados
			// para que solo haya una determinacion seleccionada en los arboles que es la
			// que se esta mostrando su informacion en los paneles
			if(treeEntidadesSeleccionado != null && treeEntidadesSeleccionado.getName() != null){
//			
				if(treeEntidadesSeleccionado.getName().equals(ConstantesGestorFIP.TRAMITE_PLANEAMIENTO_VIGENTE_ENTIDADES)){

					panel.setNodeSeleccionadoEntidades(nodeEntidades);
					panel.setArboSeleccionadoEntidades(ConstantesGestorFIP.TRAMITE_PLANEAMIENTO_VIGENTE_ENTIDADES);
				}

			}
			


		}
		if(entidadesSeleccionadasArboles == 1){
			DefaultMutableTreeNode node = panel.getNodeSeleccionadoEntidades();
			if(node.getUserObject() instanceof EntidadPanelBean){
				EntidadPanelBean entidad = (EntidadPanelBean)node.getUserObject();	
				marcarDeterminacionesAplicables(entidad.getId());
			}
		}

			
	}
	public void limpiarArbolDeterminaciones(DefaultMutableTreeNode nodo) { 
		
		if(nodo.getUserObject() instanceof DeterminacionPanelBean){
			((DeterminacionPanelBean)nodo.getUserObject()).setAplicableEntidad(false);
		}
		
		if (nodo.getChildCount() >= 0) { 
			for (Enumeration e=nodo.children(); e.hasMoreElements(); ) { 
				DefaultMutableTreeNode n = (DefaultMutableTreeNode)e.nextElement(); 
				limpiarArbolDeterminaciones(n); 
			
			} 
		}
	}
	
	private void marcarDeterminacionesAplicables(int idEntidad) {
		try{
			HashMap hashDeterAplicable = new HashMap();
			
			DeterminacionBean[] lstDetBean =  ClientGestorFip.obtenerDetAplicablesEntidad(idEntidad, aplicacion);
			for(int i=0; i<lstDetBean.length; i++)
			{
				if(lstDetBean[i] != null){
					hashDeterAplicable.put(lstDetBean[i].getId(), lstDetBean[i]);
				}
			}
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			
		}
	}
	
	
	
	public void recorrerArbol(DefaultMutableTreeNode nodo, HashMap hashDeterAplicable) { 
	
		if(nodo.getUserObject() instanceof DeterminacionPanelBean){
			DeterminacionPanelBean det = (DeterminacionPanelBean)nodo.getUserObject();
			GrupoAplicacionPanelBean grupo = det.getGrupoAplicacionPanelBean();
			((DeterminacionPanelBean)nodo.getUserObject()).setAplicableEntidad(false);
			if(grupo != null && grupo.getLstDeterminacionesAsoc() != null){
				for (int j=0; j< grupo.getLstDeterminacionesAsoc().length; j++){
					DeterminacionPanelBean detGrupo = grupo.getLstDeterminacionesAsoc()[j];
					
					if(hashDeterAplicable.containsKey(detGrupo.getId())){
						boolean encontrado = true;
						
						((DeterminacionPanelBean)nodo.getUserObject()).setAplicableEntidad(true);

					}

				}
			}
		}
		
		if (nodo.getChildCount() >= 0) { 
			for (Enumeration e=nodo.children(); e.hasMoreElements(); ) { 
				DefaultMutableTreeNode n = (DefaultMutableTreeNode)e.nextElement(); 
				recorrerArbol(n, hashDeterAplicable); 
			
			} 
		}
	}
	
	
	private boolean isSelectedNodoTramiteEnArbolEntidades(){
		
		boolean nodoTramiteSelected = false;
		
		return nodoTramiteSelected;
	}
	
	private boolean isSelectedNodoTramiteEnArbolDeterminaciones(){
		
		boolean nodoTramiteSelected = false;
		
		return nodoTramiteSelected;
	}
	
}
