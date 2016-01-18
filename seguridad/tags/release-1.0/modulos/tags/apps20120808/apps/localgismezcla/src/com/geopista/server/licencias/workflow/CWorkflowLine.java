package com.geopista.server.licencias.workflow;

/**
 * @author SATEC
 * @version $Revision: 1.1 $
 *          <p/>
 *          Autor:$Author: miriamperez $
 *          Fecha Ultima Modificacion:$Date: 2009/07/03 12:32:01 $
 *          $Name:  $
 *          $RCSfile: CWorkflowLine.java,v $
 *          $Revision: 1.1 $
 *          $Locker:  $
 *          $State: Exp $
 *          <p/>
 *          To change this template use Options | File Templates.
 */
public class CWorkflowLine {

	private int idEstado;
	private int idNextEstado;
	private int plazo;
	private int idPlazoEstado;
    private String eventText;
	private String hitoText;
    private String notifText;


	public CWorkflowLine(int idEstado, int idNextEstado, int plazo, int idPlazoEstado,String eventText,String hitoText) {
		this.idEstado = idEstado;
		this.idNextEstado = idNextEstado;
		this.plazo = plazo;
		this.idPlazoEstado = idPlazoEstado;
		this.eventText=eventText;
		this.hitoText=hitoText;
	}

    public CWorkflowLine(int idEstado, int idNextEstado, int plazo, int idPlazoEstado,String eventText,String hitoText, String notifText) {
        this.idEstado = idEstado;
        this.idNextEstado = idNextEstado;
        this.plazo = plazo;
        this.idPlazoEstado = idPlazoEstado;
        this.eventText=eventText;
        this.hitoText=hitoText;
        this.notifText=notifText;
    }


	public String getEventText() {
		return eventText;
	}

	public void setEventText(String eventText) {
		this.eventText = eventText;
	}

	public int getIdEstado() {
		return idEstado;
	}

	public void setIdEstado(int idEstado) {
		this.idEstado = idEstado;
	}

	public int getIdNextEstado() {
		return idNextEstado;
	}

	public void setIdNextEstado(int idNextEstado) {
		this.idNextEstado = idNextEstado;
	}

	public int getPlazo() {
		return plazo;
	}

	public void setPlazo(int plazo) {
		this.plazo = plazo;
	}

	public int getIdPlazoEstado() {
		return idPlazoEstado;
	}

	public void setIdPlazoEstado(int idPlazoEstado) {
		this.idPlazoEstado = idPlazoEstado;
	}

	public String getHitoText() {
		return hitoText;
	}

	public void setHitoText(String hitoText) {
		this.hitoText = hitoText;
	}

    public String getNotifText() {
        return notifText;
    }

    public void setNotifText(String notifText) {
        this.notifText = notifText;
    }


}
