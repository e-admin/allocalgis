/**
 * PintarImagenCatastro.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.intercambio.edicion.dialogs;


import java.awt.BorderLayout;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import com.geopista.app.catastro.model.beans.ImagenCatastro;

public class PintarImagenCatastro extends JPanel
{

	private ImagenCatastro imagenCatastro = null;
    private ImagenInterface imagenInterface = null;

    private DibujarImagenPanel dibujarImagenPanel = null;
    private PintarImagenCatastro lastSelected = null;
    private int indicePanel = 0;

    /* constructor de la clase */
    public PintarImagenCatastro(ImagenInterface imagenInterface)
    {        
        setBorder(new EtchedBorder());

        setLayout(new BorderLayout());
        this.dibujarImagenPanel = new DibujarImagenPanel();        

        this.add(this.dibujarImagenPanel);
        
        this.imagenInterface = imagenInterface;
        
        this.addMouseListener(new java.awt.event.MouseAdapter()
        {            

			public void mousePressed(MouseEvent evt) {
				
				if (PintarImagenCatastro.this.imagenInterface != null && imagenCatastro!=null){
                	PintarImagenCatastro.this.imagenInterface.seleccionar(imagenCatastro, indicePanel);
                    if (lastSelected!=null) lastSelected.setSelected(false);
                    lastSelected= (PintarImagenCatastro)evt.getSource();
                    lastSelected.setSelected(true);
                }
			}

        });
    }
    
    public void setIndicePanel(int indicePanel)
    {
        this.indicePanel = indicePanel;
    }
    
    public void setSelected(boolean selected)
    {
        if (selected)
            this.setBorder(new javax.swing.border.MatteBorder(new java.awt.Insets(5, 5, 5, 5), new java.awt.Color(255, 0, 0)));
        else
            this.setBorder(new EtchedBorder());
    }
    
    public void setImagen(ImagenCatastro imagen) throws Exception{
    	this.imagenCatastro = imagen;
    	
    	if (imagen == null){
    		dibujarImagenPanel.setImage((java.awt.Image)null);
    	}
    	else if (dibujarImagenPanel != null){
    		dibujarImagenPanel.setImage(imagen.getImagen());
    	}
    }
    
//    public void pintarThumbnail(ImagenCatastro imagen) throws Exception{
//
//    	if(lastSelected != null){
//    		lastSelected.setImagen(imagen);
//    	}
//    }
}
