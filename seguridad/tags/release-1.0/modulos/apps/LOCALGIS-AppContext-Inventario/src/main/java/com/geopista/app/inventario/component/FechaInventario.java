package com.geopista.app.inventario.component;
import java.text.DateFormat;

public class FechaInventario {
	java.util.Date fecha;
	java.text.DateFormat df;
	public FechaInventario(java.util.Date fecha){
		this.fecha=fecha;
	}
	public FechaInventario(java.sql.Timestamp fecha){
		this.fecha=fecha;
	}
	public FechaInventario(java.util.Date fecha, DateFormat df){
		this.fecha=fecha;
		this.df=df;
	}
	public FechaInventario(java.sql.Timestamp fecha, DateFormat df){
		this.fecha=fecha;
		this.df=df;
	}
	
	public String toString(){
		if (fecha==null) return "";
		if (df==null)
			return com.geopista.app.inventario.Constantes.df.format(fecha);
		else
			return df.format(fecha);
	
	}

}
