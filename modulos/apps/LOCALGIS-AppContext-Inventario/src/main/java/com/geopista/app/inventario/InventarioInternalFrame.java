/**
 * InventarioInternalFrame.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.inventario;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.apache.log4j.Logger;

import com.geopista.app.AppContext;
import com.geopista.app.inventario.panel.BotoneraAppJPanel;
import com.geopista.app.inventario.panel.InventarioJPanel;
import com.geopista.app.inventario.panel.MapaJPanel;
import com.geopista.editor.GeopistaEditor;
import com.geopista.protocol.inventario.Const;
import com.geopista.util.ApplicationContext;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 31-ago-2006
 * Time: 9:42:48
 * To change this template use File | Settings | File Templates.
 */
public class InventarioInternalFrame extends javax.swing.JInternalFrame implements IMultilingue{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Logger logger= Logger.getLogger(InventarioInternalFrame.class);
    private JFrame desktop;

    private MapaJPanel mapaJPanel;
    private InventarioJPanel inventarioJPanel;
    private BotoneraAppJPanel botoneraAppJPanel;
    private JPanel versionPanel;
    private JPanel auxJPanel;
    private ApplicationContext aplicacion;
    private JLabel fechaLabel;
    
    public MapaJPanel getJPanelMap(){
    	return mapaJPanel;
    }

    public InventarioInternalFrame(JFrame desktop) {
        this.desktop = desktop;
        aplicacion= (AppContext)AppContext.getApplicationContext();
        initComponents();
    }

    private void initComponents(){
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        getContentPane().setLayout(new java.awt.BorderLayout());

        auxJPanel= new JPanel();
        auxJPanel.setLayout(new java.awt.BorderLayout());

        /** panel botonera */
        botoneraAppJPanel= new BotoneraAppJPanel(desktop);
        botoneraAppJPanel.setEnabled(false);

        /** panel de inventario */
        inventarioJPanel= new InventarioJPanel(false);

        inventarioJPanel.setPreferredSize(new Dimension(500,750));
        inventarioJPanel.setMinimumSize(new Dimension(500,750));
        
        /** panel para el versionado **/
        
        auxJPanel.add(getPanelVersion(), BorderLayout.NORTH);
        auxJPanel.add(inventarioJPanel, BorderLayout.CENTER);
        auxJPanel.add(botoneraAppJPanel, BorderLayout.SOUTH);

        /** panel mapa */
        mapaJPanel= new MapaJPanel(desktop,this);
        mapaJPanel.add(mapaJPanel.getGeopistaEditor());
        
        mapaJPanel.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(ActionEvent e){
				mapaJPanel_actionPerformed();
			}
		});

        inventarioJPanel.setMapaJPanel(mapaJPanel);
        inventarioJPanel.setBotoneraAppJPanel(botoneraAppJPanel);

        getContentPane().add(auxJPanel, BorderLayout.WEST);
        getContentPane().add(mapaJPanel, BorderLayout.CENTER);

        /** annadimos un splitPane que nos permita redimensionar el panel de la lista de bienes horizontalmente */
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                                               auxJPanel, mapaJPanel);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(525);
        getContentPane().add(splitPane, BorderLayout.CENTER);

        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }


    private void mapaJPanel_actionPerformed(){
       
//    	if (!inventarioJPanel.getTipoBienesPanel().isModoAsociacion())
		if (!(inventarioJPanel.getAsociacionDialog() != null && inventarioJPanel
				.getAsociacionDialog().isVisible())) {
			inventarioJPanel.getTipoBienesPanel().setModoSeleccion(true);
			inventarioJPanel.getTipoBienesPanel().fireActionPerformed();
		} else if (inventarioJPanel.getBienesPanel().getBienesSeleccionados() != null
				&& inventarioJPanel.getBienesPanel().getBienesSeleccionados()
						.size() > 0) {
			inventarioJPanel.getAsociacionDialog().setEnabledButtons(false);
		} else if (inventarioJPanel.getBienesPanel().getBienesReversibles() != null
				&& inventarioJPanel.getBienesPanel().getBienesReversiblesSeleccionados()
						.size() > 0) {
			inventarioJPanel.getAsociacionDialog().setEnabledButtons(false);
		} else {
			inventarioJPanel.getAsociacionDialog().setEnabledButtons(true);
		}
    }


    public void renombrarComponentes() {
        try {
            setTitle(aplicacion.getI18nString("inventario.app.tag0"));
            inventarioJPanel.renombrarComponentes();
            mapaJPanel.renombrarComponentes();
            botoneraAppJPanel.renombrarComponentes();
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
        }
    }


    private GeopistaEditor getBotoneraVersiones(){
    	GeopistaEditor geopistaEditor= new GeopistaEditor("workbench-properties-inventario-versionado.xml");
        geopistaEditor.showLayerName(false);
        geopistaEditor.setVisible(true);    
        return geopistaEditor;
    }


    private JPanel getPanelVersion(){
    	if (versionPanel == null){
	        versionPanel= new JPanel();
	        versionPanel.setPreferredSize(new Dimension(100,73));
	        versionPanel.setLayout(new java.awt.GridLayout(1,4,0,0));
	        fechaLabel = new JLabel();
	        String hora = "";
	        try{
	        	hora = (String)inventarioJPanel.getInventarioClient().getHora(Const.ACTION_GET_HORA);
	        } catch (Exception ex) {
	            StringWriter sw = new StringWriter();
	            PrintWriter pw = new PrintWriter(sw);
	            ex.printStackTrace(pw);
	            logger.error("Exception: " + sw.toString());
	        }
	        Date date = new Date();
	        fechaLabel.setText((String) new SimpleDateFormat("dd-MM-yyyy").format(date)+" "+hora);
	        versionPanel.add(new JLabel());
	        versionPanel.add(fechaLabel);
	        versionPanel.add(getBotoneraVersiones());
	        versionPanel.add(new JLabel());
	        versionPanel.setBorder(new javax.swing.border.TitledBorder(aplicacion.getI18nString("Versionado")));
	        Const.fechaVersion = "";//(String) new SimpleDateFormat("yyyy-MM-dd").format(date)+" "+hora;
    	}
        return versionPanel;
    }
    
    public String getFecha(){
    	return fechaLabel.getText();
    }
    

    public void setFecha(String fecha){
    	fechaLabel.setText(fecha);
    }
    
    public InventarioJPanel getInventarioJPanel(){
    	return inventarioJPanel;
    }
}
