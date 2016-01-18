package com.geopista.app.catastro.intercambio.edicion.dialogs;


import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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
