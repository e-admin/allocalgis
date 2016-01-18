package com.geopista.app.document;

import com.geopista.protocol.document.DocumentBean;

/**
 * Created by IntelliJ IDEA.
 * User: lara
 * Date: 03-may-2006
 * Time: 12:14:42
 * To change this template use File | Settings | File Templates.
 */

public interface DocumentInterface
{
    public void seleccionar(DocumentBean documento);

    public void seleccionar(DocumentBean documentBean, int indicePanel);
}
