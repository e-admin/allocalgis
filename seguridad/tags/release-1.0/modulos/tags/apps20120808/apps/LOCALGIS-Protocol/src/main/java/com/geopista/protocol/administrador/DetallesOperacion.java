package com.geopista.protocol.administrador;

import java.util.ArrayList;

public class DetallesOperacion implements java.io.Serializable, Cloneable {
	
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(DetallesOperacion.class);
	
	private static final long serialVersionUID = -7823470766069132710L;
	
	public static int listaColumnas = 1;
	public static int listaVersionPrevia = 2;
	public static int listaVersionModificada = 3;
	
	private Operacion operacion;
	private DatosCapa datosCapa;
	
	private ArrayList nombresColumnasDatosAfectados;
	private ArrayList datosAfectadosVersionPrevia;
	private ArrayList datosAfectadosVersionModificada;

	public DetallesOperacion(Operacion op) { 
        this.operacion = op;
        nombresColumnasDatosAfectados = new ArrayList();
		datosAfectadosVersionPrevia = new ArrayList();
		datosAfectadosVersionModificada = new ArrayList();
    }
	
	public DetallesOperacion() {
		  nombresColumnasDatosAfectados = new ArrayList();
		  datosAfectadosVersionPrevia = new ArrayList();
		  datosAfectadosVersionModificada = new ArrayList();
	}

	public DatosCapa getDatosCapa() {
		return datosCapa;
	}

	public void setDatosCapa(DatosCapa datosCapa) {
		this.datosCapa = datosCapa;
	}

	public Operacion getOperacion() {
		return operacion;
	}

	public void setOperacion(Operacion operacion) {
		this.operacion = operacion;
	}
    
	public ArrayList getNombresColumnasDatosAfectados() {
		return nombresColumnasDatosAfectados;
	}

	public void setNombresColumnasDatosAfectados(ArrayList nombresColumnasDatosAfectados) {
		this.nombresColumnasDatosAfectados = nombresColumnasDatosAfectados;
	}

	public ArrayList getDatosAfectadosVersionPrevia() {
		return datosAfectadosVersionPrevia;
	}

	public void setDatosAfectadosVersionPrevia(ArrayList datosAfectadosVersionPrevia) {
		this.datosAfectadosVersionPrevia = datosAfectadosVersionPrevia;
	}
	
	public ArrayList getDatosAfectadosVersionModificada() {
		return datosAfectadosVersionModificada;
	}

	public void setDatosAfectadosVersionModificada(ArrayList datosModificados) {
		this.datosAfectadosVersionModificada = datosModificados;
	}
	
	public void addItemList(int lista, String item){
		switch(lista) {
			case 1: nombresColumnasDatosAfectados.add(item);
			break;
			case 2: datosAfectadosVersionPrevia.add(item);
			break;
			case 3: datosAfectadosVersionModificada.add(item);
			break;
		}
	}
	
	public Object clone() {
		DetallesOperacion obj = null;
		try {
			obj = (DetallesOperacion) super.clone();
			obj.setOperacion((Operacion) this.getOperacion().clone());
			obj.setDatosCapa((DatosCapa) this.getDatosCapa().clone());
			obj.setNombresColumnasDatosAfectados((ArrayList)this.getNombresColumnasDatosAfectados().clone());
			obj.setDatosAfectadosVersionPrevia((ArrayList)this.getDatosAfectadosVersionPrevia().clone());
			obj.setDatosAfectadosVersionModificada((ArrayList)this.getDatosAfectadosVersionModificada().clone());
		} 
		catch (CloneNotSupportedException ex) {
			 logger.error("Error al clonar el objeto DetallesOperacion: "+ex.toString());
		}
		return obj;
	}
}
