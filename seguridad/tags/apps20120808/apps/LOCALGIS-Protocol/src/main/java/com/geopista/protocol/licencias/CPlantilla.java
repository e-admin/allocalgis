package com.geopista.protocol.licencias;

import com.geopista.protocol.licencias.tipos.CTipoAnexo;

import java.io.File;

/**
 * @author SATEC
 * @version $Revision: 1.1 $
 *          <p/>
 *          Autor:$Author: miriamperez $
 *          Fecha Ultima Modificacion:$Date: 2009/07/03 12:31:55 $
 *          $Name:  $
 *          $RCSfile: CPlantilla.java,v $
 *          $Revision: 1.1 $
 *          $Locker:  $
 *          $State: Exp $
 *          <p/>
 *          To change this template use Options | File Templates.
 */
public class CPlantilla implements java.io.Serializable {

	private String fileName;
    private String path;
	private byte[] content;

	public CPlantilla() {
	}

	public CPlantilla(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path= path;
    }

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

}
