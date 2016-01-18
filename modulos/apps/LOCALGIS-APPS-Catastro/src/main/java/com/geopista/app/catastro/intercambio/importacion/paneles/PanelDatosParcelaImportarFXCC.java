/**
 * PanelDatosParcelaImportarFXCC.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.intercambio.importacion.paneles;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import com.geopista.app.catastro.registroExpedientes.paneles.PanelMapa;

public class PanelDatosParcelaImportarFXCC extends JPanel{

	private JSplitPane jSplitPane = null;
	private JPanel jPanelEditingInfo = null;  
    private JPanel jPanelGraphicEditor = null;

    private PanelMapa editorMapaPanel;
    
   // public static final int DIM_X=900;
    //public static final int DIM_Y=400;
    
    /**
     * Constructor de la clase. Se le pasa por parametro el label que se mostrara como borde en el panel. El label es
     * un elemento del ResourceBundle de la clase que le llama.
     *
     * @param label Etiqueta del borde del panel
     * @param numExp El numero del expediente
     * @param convenioExp El convenio del Expediente
     */
    public PanelDatosParcelaImportarFXCC()
    {
    	super();
        inicializaPanel();

    }
    
    /**
     *  Inicializa todos los elementos del panel y los coloca en su posicion.
     *
     * @param numExp Numero de expediente
     */
    private void inicializaPanel()
    {
    	this.setLayout(new GridBagLayout());

    	this.setPreferredSize(new Dimension(830,500));
        
        jSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				getJPanelEditingInfo(),getJPanelGraphicEditor());
        jSplitPane.setOneTouchExpandable(true);
        jSplitPane.setDividerSize(10);
        this.add(jSplitPane, 
                new GridBagConstraints(0, 0, 1, 1, 1, 1,
                        GridBagConstraints.WEST, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));
    }
    
    
    public JButton getJButtonMapaATabla(){
    	return ((EditingImportarFXCCPanel)getJPanelEditingInfo()).getParcelaMapaATablaJButton();	
    }
    public JButton getJButtonTablaAMapa(){
    	return ((EditingImportarFXCCPanel)getJPanelEditingInfo()).getParcelaTablaAMapeJButton();	
    }
    public JButton getJButtonEliminarTabla(){
    	return ((EditingImportarFXCCPanel)getJPanelEditingInfo()).getElimarParcelaTablaJButton();	
    }
    
    public JButton getJButtonBuscarRefCatastral(){
    	return ((EditingImportarFXCCPanel)getJPanelEditingInfo()).getLinkBotonBuscarRefCatastral();	
    }
    public JButton getJButtonBuscarRefCatastralPorDir(){
    	return ((EditingImportarFXCCPanel)getJPanelEditingInfo()).getLinkBotonBuscarRefCatastralPorDir();
    }
    public JButton getJButtonBuscarRefCatastralPorPoligono(){
    	return ((EditingImportarFXCCPanel)getJPanelEditingInfo()).getLinkBotonBuscarRefCatastralPorPoligono();	
    }
    
    public JPanel getJPanelEditingInfo()
    {
        if (jPanelEditingInfo == null)
        {
        	jPanelEditingInfo = new EditingImportarFXCCPanel();      	
        }
        return jPanelEditingInfo;
    }

    
	public JPanel getJPanelGraphicEditor()
    {
        if (jPanelGraphicEditor == null)
        {
        	editorMapaPanel= new PanelMapa("Catastro.RegistroExpedientes.AsociarParcelas.editorMapaPanel");
        	editorMapaPanel.load(false,editorMapaPanel);
        	jPanelGraphicEditor = editorMapaPanel;
        	
        }
		
        return jPanelGraphicEditor;
    }
	
	public void cargarMapa(){
		
		((PanelMapa)jPanelGraphicEditor).load(false,editorMapaPanel);
		
	}
	
	public ArrayList getListaParcelas(){
		
		return (ArrayList)((EditingImportarFXCCPanel)getJPanelEditingInfo()).getReferenciasCatastrales();
		
	}
	
}
