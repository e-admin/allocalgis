/**
 * CantidadNodosPegarDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.intercambio.edicion.dialogs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.tree.DefaultMutableTreeNode;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.intercambio.edicion.utils.EdicionUtils;
import com.geopista.app.catastro.intercambio.edicion.utils.HideableNode;
import com.geopista.app.catastro.model.beans.BienInmuebleJuridico;
import com.geopista.app.catastro.model.beans.ConstruccionCatastro;
import com.geopista.app.catastro.model.beans.Cultivo;
import com.geopista.app.catastro.model.beans.FincaCatastro;
import com.geopista.app.catastro.model.beans.SueloCatastro;
import com.geopista.app.catastro.model.beans.Titular;
import com.geopista.app.catastro.model.beans.UnidadConstructivaCatastro;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;

public class CantidadNodosPegarDialog extends javax.swing.JDialog {

	
	private JPanel cantidadPanel;
	private JTextField cantidadField;
	private OKCancelPanel aceptarButton;
	private JLabel cantidadLabel;
	
	private static AppContext aplicacion = (AppContext) AppContext
	.getApplicationContext();
	private Object nodoAPegar;
	private GestionExpedientePanel gestionExpedientesPanel;
	private HideableNode nodoOrigen;

	/**
     * Constructor de la calse. Inicializa el panel y lo muestra. Necesia el parametro parent (Frame) y modal (boolean)
     * para pasarselo al padre.
     *
     * @param parent JFrame
     * @param modal boolean
     * @param convenioExpediente String
     */
    public CantidadNodosPegarDialog(java.awt.Frame parent, boolean modal,
    		Object nodoAPegar, GestionExpedientePanel gestionExpedientesPanel,HideableNode nodoOrigen)
    {
		super(parent, modal);
		this.gestionExpedientesPanel = gestionExpedientesPanel;
		this.nodoAPegar = nodoAPegar;
		this.nodoOrigen = nodoOrigen;
		this.setTitle(I18N.get("Expedientes",
				"Catastro.Intercambio.Edicion.Dialogs.CantidadNodos.Pegar"));
        inicializaDialogo();
	}
    
    /**
     * Inicializa los elemento de la gui con sus determinados valores y eventos.
     */
    private void inicializaDialogo()
    {

    	getContentPane().add(getCantidadPanel());
    }
    
    
	public JPanel getCantidadPanel() {
		if(cantidadPanel == null){
			cantidadPanel = new JPanel();
			cantidadPanel.setLayout(new GridBagLayout());

			cantidadLabel = new JLabel();
			cantidadLabel.setText(I18N.get("Expedientes",
				"Catastro.Intercambio.Edicion.Dialogs.CantidadNodos.valor.Pegar"));
			
			cantidadPanel.add(cantidadLabel, 
					new GridBagConstraints(0,0,1,1, 0.1, 1,GridBagConstraints.EAST,
						GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
			
			cantidadPanel.add(getCantidadField(), 
					new GridBagConstraints(0,1,1,1, 0.1, 1,GridBagConstraints.EAST,
						GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
			
			cantidadPanel.add(getAceptar(), 
					new GridBagConstraints(0,2,1,1, 0.1, 1,GridBagConstraints.EAST,
						GridBagConstraints.NONE, new Insets(0,5,0,5),0,0));
			
		}
		return cantidadPanel;
	}

	public void setCantidadPanel(JPanel cantidadPanel) {
		this.cantidadPanel = cantidadPanel;
	}
	
	public JTextField getCantidadField() {
		
		 if (cantidadField == null)
	        {
			 cantidadField = new JTextField();//new JNumberTextField(JNumberTextField.NUMBER);//, new Integer(9999));
			 cantidadField.addCaretListener(new CaretListener()
	                    {
	                        public void caretUpdate(CaretEvent evt)
	                        {
	                            EdicionUtils.chequeaLongYNumCampoEdit(cantidadField,3, aplicacion.getMainFrame());
	                        }
	                    });
	        }
		return cantidadField;
	}

	public void setCantidadField(JTextField cantidadField) {
		this.cantidadField = cantidadField;
	}
	
	public OKCancelPanel getAceptar() {

        if (aceptarButton == null)
        {
        	aceptarButton = new OKCancelPanel();
       	aceptarButton.addActionListener(new java.awt.event.ActionListener()
                    {
                public void actionPerformed(java.awt.event.ActionEvent e)
                { 
                   
                    if (aceptarButton.wasOKPressed())
                    {
                    	int cantidadNodos =Integer.valueOf(getCantidadField().getText());    
                    	
                    	DefaultMutableTreeNode node = (DefaultMutableTreeNode) gestionExpedientesPanel.getTree()
    					.getLastSelectedPathComponent();
    					Object nodeInfo = node.getUserObject();
    					
    					if(nodoAPegar instanceof Titular){
    						if (nodeInfo instanceof BienInmuebleJuridico) {
    							for (int i=0; i<cantidadNodos; i++){
    								gestionExpedientesPanel.getParcelaPanel().annadeTitular((Titular) nodoAPegar ,nodoOrigen);
    							}
    
    						}
    					
    					}
    					else if(nodoAPegar instanceof BienInmuebleJuridico){
    						if(nodeInfo instanceof FincaCatastro){
    							for (int i=0; i<cantidadNodos; i++){
    								BienInmuebleJuridico bij = (BienInmuebleJuridico)nodoAPegar;
    								gestionExpedientesPanel.getParcelaPanel().annadeBI(bij ,nodoOrigen);
    							}
    						}
    						
    					}
    					
    					else if(nodoAPegar instanceof Cultivo){
    						
    						Cultivo cultivo = (Cultivo) nodoAPegar;
    						// cultivo comun
    						if ((cultivo.getIdCultivo().getNumOrden()==null ||
    								cultivo.getIdCultivo().getNumOrden().trim().equals("")) &&
    								cultivo.getCodModalidadReparto()!=null)
    						{  
    							if(nodeInfo instanceof FincaCatastro){
    								for (int i=0; i<cantidadNodos; i++){
    									gestionExpedientesPanel.getParcelaPanel().annadeCultivo(cultivo, true, nodoOrigen);
    								}
    							}
    							
    						}	
    						else{
    							if (nodeInfo instanceof BienInmuebleJuridico) {
    								for (int i=0; i<cantidadNodos; i++){
    									gestionExpedientesPanel.getParcelaPanel().annadeCultivo(cultivo, false, nodoOrigen);
    								}
    							}
    							
    						}
    					}
    					else if(nodoAPegar instanceof ConstruccionCatastro){
    						
    						ConstruccionCatastro cons = (ConstruccionCatastro)nodoAPegar;
    						if (cons.getDatosEconomicos().getCodTipoValor()!=null
    								&& !cons.getDatosEconomicos().getCodTipoValor().equals("")
    								&& cons.getDatosEconomicos().getCodModalidadReparto()!=null)
    						{   
    							if(nodeInfo instanceof FincaCatastro){
    								for (int i=0; i<cantidadNodos; i++){
    									gestionExpedientesPanel.getParcelaPanel().annadeConstruccion(cons, true, nodoOrigen);
    								}
    							}
    							
    						
    						}
    						else{
    							if (nodeInfo instanceof BienInmuebleJuridico) {
    								for (int i=0; i<cantidadNodos; i++){
    									gestionExpedientesPanel.getParcelaPanel().annadeConstruccion(cons, false, nodoOrigen);
    								}
    							}
    							
    						}
    					}
    					else if(nodoAPegar instanceof UnidadConstructivaCatastro) {
    						if(nodeInfo instanceof FincaCatastro){
    							for (int i=0; i<cantidadNodos; i++){
    								UnidadConstructivaCatastro unidadConstructivaCatastro = (UnidadConstructivaCatastro) nodoAPegar;
    								gestionExpedientesPanel.getParcelaPanel().annadeUnidadConstructivaCatastro(unidadConstructivaCatastro, nodoOrigen);
    							}
    						}
    						
    					}
    					else if(nodoAPegar instanceof SueloCatastro) {
    						if(nodeInfo instanceof FincaCatastro){
    							for (int i=0; i<cantidadNodos; i++){
    								SueloCatastro suelo = (SueloCatastro)nodoAPegar;
    								gestionExpedientesPanel.getParcelaPanel().annadeSueloCatastro(suelo, nodoOrigen);
    							}
    						}
    						
    					}	
                    	
                    	
                    	
                    }
                    dispose();                    
                }
                    });
        }
        	
		return aceptarButton;
	}

	public void setAceptar(OKCancelPanel aceptarButton) {
		this.aceptarButton = aceptarButton;
	}
	
}
