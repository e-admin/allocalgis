/**
 * SolicitarAnioHistoricoAmortDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.inventario;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JDialog;

import org.apache.log4j.Logger;

import com.geopista.app.AppContext;
import com.geopista.protocol.inventario.Const;
import com.geopista.util.config.UserPreferenceStore;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;



public class SolicitarAnioHistoricoAmortDialog extends JDialog{
	
	static Logger logger = Logger.getLogger(SolicitarAnioHistoricoAmortDialog.class);
	private SolicitarAnioHistoricoAmortPanel solicitarAnioHistoricoAmortPanel = null;    
    private OKCancelPanel _okCancelPanel = null;
    
    public static final int DIM_X = 400;
    public static final int DIM_Y = 180;
    private boolean ok=false;
        
    public boolean wasPressedOk() {
		return ok;
	}


	public void setOk(boolean ok) {
		this.ok = ok;
	}


	private OKCancelPanel getOkCancelPanel(){
        if (_okCancelPanel == null){
            _okCancelPanel = new OKCancelPanel();
            _okCancelPanel.addActionListener(new java.awt.event.ActionListener(){


				public void actionPerformed(java.awt.event.ActionEvent e){
                	            		                    
	                if (_okCancelPanel.wasOKPressed()){
			        	ok=true;
			    		UserPreferenceStore.setUserPreference(Const.KEY_ANIO_AMORTIZACION,getSolicitarAnioPanel().getAnioEncuesta());
			        	
	                }
	                _okCancelPanel.setVisible(false);
	                dispose();
                }  	
            });        	
           
    }//ok
        return _okCancelPanel;
}
    
    
/* CONSTRUCTORES */    
          
    public SolicitarAnioHistoricoAmortDialog(){
    	super(AppContext.getApplicationContext().getMainFrame());
   	 initialize(I18N.get("LocalGISEIEL", "inventario.historicoamortizacion.dialog.anio.title"));         
       this.setLocationRelativeTo(null);
    }
   
    
    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize(String title){
//        Locale loc=I18N.getLocaleAsObject();         
//        ResourceBundle bundle = AppContext.getApplicationContext().getI18NResource();
        this.setModal(true);
        this.setSize(DIM_X, DIM_Y);
        this.setContentPane(getSolicitarAnioPanel());
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.setTitle(title);
//        this.setResizable(false);
        this.getOkCancelPanel().setVisible(true);
        this.addWindowListener(new java.awt.event.WindowAdapter(){
            public void windowClosing(java.awt.event.WindowEvent e){
                dispose();
            }
        });              
    }    
   
    public static void main(String[] args){
        SolicitarAnioHistoricoAmortDialog dialog = new SolicitarAnioHistoricoAmortDialog();
        dialog.setVisible(true);
    }
    
    /* PANEL */
    public SolicitarAnioHistoricoAmortPanel getSolicitarAnioPanel(){
        if (solicitarAnioHistoricoAmortPanel == null){
        	solicitarAnioHistoricoAmortPanel = new SolicitarAnioHistoricoAmortPanel(new GridBagLayout());
        	solicitarAnioHistoricoAmortPanel.add(getOkCancelPanel(), 
                    new GridBagConstraints(0, 7, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 5, 0, 5), 0, 0));           
        }
        return solicitarAnioHistoricoAmortPanel;
    }
    
   
}
