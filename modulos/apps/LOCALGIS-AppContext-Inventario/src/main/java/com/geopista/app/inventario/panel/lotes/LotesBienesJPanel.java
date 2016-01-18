/**
 * LotesBienesJPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.inventario.panel.lotes;


import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.inventario.Constantes;
import com.geopista.app.inventario.panel.BienesJPanel;
import com.geopista.app.inventario.panel.MuebleJDialog;
import com.geopista.protocol.inventario.Const;
import com.geopista.protocol.inventario.InventarioClient;
import com.geopista.protocol.inventario.Lote;
import com.geopista.protocol.inventario.MuebleBean;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;





public class LotesBienesJPanel extends JPanel {
	private static final Log logger = LogFactory.getLog(LotesBienesJPanel.class);
	private static final long serialVersionUID = 1L;
	private String locale="es_ES";
	private AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	private JFrame frame;
    BienesJPanel listaBienes;
    private InventarioClient inventarioClient; 
    
	public LotesBienesJPanel (String locale, JFrame frame, Lote lote){
		this.locale=locale;
		this.frame=frame;
		initComponents();
		try{
			listaBienes.loadListaBienes(lote==null?null:lote.getBienes());
			}  catch (Exception e) {
			logger.error("Error al cargar el listado de bienes", e);
		}
	    inventarioClient= new InventarioClient(aplicacion.getString(UserPreferenceConstants.LOCALGIS_SERVER_ADMCAR_SERVLET_URL) +
	    		Constantes.INVENTARIO_SERVLET_NAME);
		 
	}
	private void initComponents(){
	
		listaBienes = new BienesJPanel(locale,false,false);
		this.setLayout(new BorderLayout());
		this.add(listaBienes,BorderLayout.NORTH);
		listaBienes.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(ActionEvent e){
            	if (BienesJPanel.DOBLE_CLICK.equals(e.getActionCommand())){
            		bienesJPanel_dobleClick();
            	}
            }
		 });
	}
		
    private void bienesJPanel_dobleClick(){
    	try{
    		MuebleBean muebleBean=(MuebleBean)listaBienes.getBienSeleccionado();
    		muebleBean=(MuebleBean)inventarioClient.getBienInventario(Const.ACTION_GET_MUEBLE, Const.SUPERPATRON_BIENES,muebleBean.getTipo(), muebleBean.getId(), muebleBean.getRevisionActual(), muebleBean.getRevisionExpirada());
    		abrirDialogoMueble(muebleBean);
    	}catch(Exception ex){
    		logger.error("Error al mostrar el mueble", ex);
     	    ErrorDialog.show(this, "ERROR", aplicacion.getI18nString("inventario.lote.error"), StringUtil.stackTrace(ex));
     	}
    }
/**
 * Abre el dialogo de un bien mueble
 * @param operacion operacion que realiza el usuario
 * @param tipo de mueble (Hist. Artistico)
 * @throws Exception
 */
private void abrirDialogoMueble(MuebleBean mueble) throws Exception{
    if (mueble == null) return;
    this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

    try{
        MuebleJDialog muebleJDialog= new MuebleJDialog(frame, locale,mueble.getTipo());
        muebleJDialog.setOperacion(Constantes.OPERACION_CONSULTAR);
        try{muebleJDialog.setTitle(aplicacion.getI18nString("inventario.dialogo.tag3"));}catch (Exception e){}
        muebleJDialog.load((MuebleBean)mueble, false);
        
        muebleJDialog.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(ActionEvent e){
               ((MuebleJDialog)e.getSource()).setVisible(false);
            }
        });
        GUIUtil.centreOnWindow(muebleJDialog);
        muebleJDialog.setVisible(true);
    }finally{
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }
}

	
}
