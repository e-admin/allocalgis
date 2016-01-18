/**
 * CementeriosInternalFrame.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.cementerios;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.apache.log4j.Logger;

import com.geopista.app.AppContext;
import com.geopista.app.cementerios.panel.BotoneraAppJPanel;
import com.geopista.app.cementerios.panel.CementeriosJPanel;
import com.geopista.app.cementerios.panel.MapaJPanel;
import com.geopista.util.ApplicationContext;



public class CementeriosInternalFrame extends javax.swing.JInternalFrame implements IMultilingue{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Logger logger= Logger.getLogger(CementeriosInternalFrame.class);
    
	private JFrame desktop;

    private MapaJPanel mapaJPanel;
    private CementeriosJPanel cementeriosJPanel;
    private BotoneraAppJPanel botoneraAppJPanel;
    private JPanel auxJPanel;
    private ApplicationContext aplicacion;
    private JLabel fechaLabel;
    
    public MapaJPanel getJPanelMap(){
    	return mapaJPanel;
    }

    public CementeriosInternalFrame(JFrame desktop) {
        this.desktop = desktop;
        aplicacion= (AppContext)AppContext.getApplicationContext();
        initComponents();
    }

    /**
     * Método que inicializa los componentes
     */
    private void initComponents(){
    	
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        getContentPane().setLayout(new java.awt.BorderLayout());

        auxJPanel= new JPanel();
        auxJPanel.setLayout(new java.awt.BorderLayout());

        /** panel botonera */
        botoneraAppJPanel= new BotoneraAppJPanel(desktop);
        botoneraAppJPanel.setEnabled(false);

        /** panel de cementerio */
        cementeriosJPanel= new CementeriosJPanel(false);

        cementeriosJPanel.setPreferredSize(new Dimension(500,750));
        cementeriosJPanel.setMinimumSize(new Dimension(500,750));
        
        /** panel para el versionado **/
        
//        auxJPanel.add(getPanelVersion(), BorderLayout.NORTH);
        auxJPanel.add(cementeriosJPanel, BorderLayout.CENTER);
        auxJPanel.add(botoneraAppJPanel, BorderLayout.SOUTH);

        /** panel mapa */
        mapaJPanel= new MapaJPanel(desktop);
        mapaJPanel.add(mapaJPanel.getGeopistaEditor());
        
        mapaJPanel.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(ActionEvent e){
				mapaJPanel_actionPerformed();
			}
		});

        cementeriosJPanel.setMapaJPanel(mapaJPanel);
        cementeriosJPanel.setBotoneraAppJPanel(botoneraAppJPanel);
        
        getContentPane().add(auxJPanel, BorderLayout.WEST);
        getContentPane().add(mapaJPanel, BorderLayout.CENTER);

        /** annadimos un splitPane que nos permita redimensionar el panel de la lista de elementos horizontalmente */
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                                               auxJPanel, mapaJPanel);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(450);
        getContentPane().add(splitPane, BorderLayout.CENTER);

        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }


    private void mapaJPanel_actionPerformed(){
       
    	cementeriosJPanel.getTipoElemCementeriosJPanel().fireActionPerformed();
    }


    public void renombrarComponentes() {
        try {
            setTitle(aplicacion.getI18nString("cementerio.app.tag0"));
            cementeriosJPanel.renombrarComponentes();
            mapaJPanel.renombrarComponentes();
            botoneraAppJPanel.renombrarComponentes();
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
        }
    }

    public String getFecha(){
    	return fechaLabel.getText();
    }
    

    public void setFecha(String fecha){
    	fechaLabel.setText(fecha);
    }
    
    public CementeriosJPanel getCemenenerioJPanel(){
    	return cementeriosJPanel;
    }
}
