package com.geopista.app.catastro.intercambio.edicion.dialogs;

import com.geopista.app.catastro.model.beans.ImagenCatastro;


public interface ImagenInterface
{
    public void seleccionar(ImagenCatastro imagen);

    public void seleccionar(ImagenCatastro imagen, int indicePanel);
}
