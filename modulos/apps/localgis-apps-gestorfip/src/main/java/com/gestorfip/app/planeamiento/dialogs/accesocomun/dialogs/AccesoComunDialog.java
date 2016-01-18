/**
 * AccesoComunDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.gestorfip.app.planeamiento.dialogs.accesocomun.dialogs;

import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import com.geopista.util.ApplicationContext;
import com.gestorfip.app.planeamiento.dialogs.accesocomun.images.IconLoader;
import com.gestorfip.app.planeamiento.utils.GestorFipUtils;
import com.vividsolutions.jump.I18N;

public class AccesoComunDialog extends JInternalFrame{

	 private JFrame desktop;
	 private ApplicationContext application;
	 private JButton importFipConsoleButton;
	 private JButton importFipArchivoButton;
	 private JButton gestorFipButton;
	 private JPanel butonsPanel;


	/**
	     * Constructor de la clase. Inicializa todos los paneles de la pantalla y asocia los eventos para ser tratados.
	     * Se le pasa por parametro un JFrame y la lista de expedientes a mostrar.
	     *
	     * @param desktop  JFrame
	     * @param exp    lista de expedientes
	     */
	 public AccesoComunDialog(JFrame desktop,
			 ApplicationContext application){
		 
		 this.desktop= desktop;
		 this.application = application;
		
		 this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		 GestorFipUtils.menuBarSetEnabled(false, this.desktop);

		 inicializaElementos();

	     addInternalFrameListener(new javax.swing.event.InternalFrameListener()
        {
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt)
            {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt)
            {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt)
            {
                cierraInternalFrame();
                
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt)
            {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt)
            {
            }
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt)
            {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt)
            {
            }
        });
//        this.setTitle(I18N.get("LocalGISGestorFip","gestorFip.abrirfip.busquedaFicheroFip1.title"));
        setClosable(true);
        

        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	 }
	    
	

   /**
    * Inicializa los elementos del panel.
    */
    private void inicializaElementos()
    {
        getContentPane().add(getButonsPanel());

    }
    
    
    public JFrame getDesktop() {
    	return desktop;
    }

    
    /**
     * Cierra la ventana y habilita el menu de la aplicacion.
     */
    public void cierraInternalFrame()
    {
        try
        {
        	this.setClosed(true);
        }
        catch(Exception e){e.printStackTrace();}
        GestorFipUtils.menuBarSetEnabled(true, this.desktop);
       
    }    
    

	public JButton getImportFipConsoleButton() {
		if(importFipConsoleButton ==null){
			importFipConsoleButton = new JButton();
			importFipConsoleButton.setIcon(IconLoader.icon("importconsola.png"));
			importFipConsoleButton.setToolTipText(I18N.get("LocalGISGestorFip","gestorFip.submenu.importarFicheroFipConsola"));
		}
		return importFipConsoleButton;
	}

	public void setImportFipConsoleButton(JButton importFipConsoleButton) {
		this.importFipConsoleButton = importFipConsoleButton;
	}

	public JButton getImportFipArchivoButton() {
		if(importFipArchivoButton ==null){
			importFipArchivoButton = new JButton();
			importFipArchivoButton.setIcon(IconLoader.icon("importarchivo.png"));
			importFipArchivoButton.setToolTipText(I18N.get("LocalGISGestorFip","gestorFip.submenu.importarFicheroFipArchivo"));
		}
		return importFipArchivoButton;
	}

	public void setImportFipArchivoButton(JButton importFipArchivoButton) {
		this.importFipArchivoButton = importFipArchivoButton;
	}

	public JButton getGestorFipButton() {
		if(gestorFipButton ==null){
			gestorFipButton = new JButton();
			gestorFipButton.setIcon(IconLoader.icon("abrirfip.png"));
			gestorFipButton.setToolTipText(I18N.get("LocalGISGestorFip","gestorFip.submenu.sincronizarabrirfip"));
		}
		return gestorFipButton;
	}

	public void setGestorFipButton(JButton gestorFipButton) {
		this.gestorFipButton = gestorFipButton;
	}

	public JPanel getButonsPanel() {
		if(butonsPanel == null){
			butonsPanel = new JPanel();
			butonsPanel.setLayout(new GridBagLayout());
			
			butonsPanel.add(getImportFipConsoleButton(), 
					new GridBagConstraints(0,0,1,1, 1, 1,GridBagConstraints.CENTER,
							GridBagConstraints.NONE, new Insets(0,5,0,5),0,0));
			
			butonsPanel.add(getImportFipArchivoButton(), 
					new GridBagConstraints(1,0,1,1, 1, 1,GridBagConstraints.CENTER,
							GridBagConstraints.NONE, new Insets(0,5,0,5),0,0));
			
			butonsPanel.add(getGestorFipButton(), 
					new GridBagConstraints(2,0,1,1, 1, 1,GridBagConstraints.CENTER,
							GridBagConstraints.NONE, new Insets(0,5,0,5),0,0));
		}
		return butonsPanel;
	}

	public void setButonsPanel(JPanel butonsPanel) {
		this.butonsPanel = butonsPanel;
	}


		
}

