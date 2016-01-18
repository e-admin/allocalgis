package com.geopista.app.cementerios;

import org.apache.log4j.Logger;
import javax.swing.*;

import com.geopista.app.cementerios.CementeriosInternalFrame;
import com.geopista.app.cementerios.IMultilingue;
import com.geopista.app.cementerios.panel.BotoneraAppJPanel;
import com.geopista.app.cementerios.panel.CementeriosJPanel;
import com.geopista.app.cementerios.panel.MapaJPanel;

import com.geopista.app.AppContext;
import com.geopista.util.ApplicationContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.StringWriter;
import java.io.PrintWriter;



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
