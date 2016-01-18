package com.geopista.app.ocupaciones;

import com.geopista.protocol.ListaEstructuras;
import com.geopista.protocol.CConstantesComando;


/**
 * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
 USA.
 *
 * For more information, contact:
 *
 *
 * www.geopista.com
 *
 *
 */


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 01-oct-2004
 * Time: 11:20:53
 */
public class Estructuras {
	private static boolean cargada = false;
	private static boolean iniciada = false;
	private static ListaEstructuras listaTiposAnexo = new ListaEstructuras("TIPO_ANEXO");
	private static ListaEstructuras listaViasNotificacion = new ListaEstructuras("TIPO_VIA_NOTIFICACION");
	private static ListaEstructuras listaVias = new ListaEstructuras("TIPO_VIA");
	private static ListaEstructuras listaTiposFinalizacion = new ListaEstructuras("TIPO_FINALIZACION");
	private static ListaEstructuras listaTiposTramitacion = new ListaEstructuras("Tipo de tramitación de expedientes");
	private static ListaEstructuras listaEstadosNotificacion = new ListaEstructuras("TIPO_ESTADO_NOTIFICACION");
	private static ListaEstructuras listaEstadosOcupacion = new ListaEstructuras("ESTADOS_OCUPACION");
	private static ListaEstructuras listaTipoOcupacion = new ListaEstructuras("TIPO_OCUPACION");

	private static ListaEstructuras listaFormatosSalida = new ListaEstructuras("FORMATO_SALIDA");
	private static ListaEstructuras listaTiposNotificacion = new ListaEstructuras("TIPO_NOTIFICACION");

    /** Necesario para la busqueda de una licencia de obra asociada a la ocupacion */
    private static ListaEstructuras listaLicencias=new ListaEstructuras("TIPO_LICENCIA");
    private static ListaEstructuras listaEstados= new ListaEstructuras("ESTADOS");
    private static ListaEstructuras listaTiposViaINE= new ListaEstructuras("Tipos de via normalizados del INE");
    private static ListaEstructuras listaTiposObra=new ListaEstructuras("TIPO_OBRA");
    private static ListaEstructuras listaTiposObraMenor=new ListaEstructuras("TIPO_OBRA_MENOR");



	public static ListaEstructuras getListaTiposAnexo() {
		return listaTiposAnexo;
	}

	public static ListaEstructuras getListaViasNotificacion() {
		return listaViasNotificacion;
	}

	public static ListaEstructuras getListaVias() {
		return listaVias;
	}

	public static ListaEstructuras getListaTiposFinalizacion() {
		return listaTiposFinalizacion;
	}

	public static ListaEstructuras getListaTiposTramitacion() {
		return listaTiposTramitacion;
	}

	public static ListaEstructuras getListaEstadosNotificacion() {
		return listaEstadosNotificacion;
	}


	public static ListaEstructuras getListaEstadosOcupacion() {
		return listaEstadosOcupacion;
	}


	public static ListaEstructuras getListaTipoOcupacion() {
		return listaTipoOcupacion;
	}

	public static ListaEstructuras getListaFormatosSalida() {
		return listaFormatosSalida;
	}

	public static ListaEstructuras getListaTiposNotificacion() {
		return listaTiposNotificacion;
	}

    public static ListaEstructuras getListaLicencias() {
        return listaLicencias;
    }

    public static ListaEstructuras getListaEstados() {
        return listaEstados;
    }

    public static ListaEstructuras getListaTiposViaINE() {
        return listaTiposViaINE;
    }

    public static ListaEstructuras getListaTiposObra() {
        return listaTiposObra;
    }

    public static ListaEstructuras getListaTiposObraMenor() {
        return listaTiposObraMenor;
    }



	public static void cargarEstructuras() {
		iniciada = true;
		try {
			cargarLista(listaTiposAnexo);
			cargarLista(listaViasNotificacion);
			cargarLista(listaVias);
			cargarLista(listaTiposFinalizacion);
			cargarLista(listaTiposTramitacion);
			cargarLista(listaEstadosNotificacion);
			cargarLista(listaEstadosOcupacion);
			cargarLista(listaTipoOcupacion);
			cargarLista(listaFormatosSalida);
			cargarLista(listaTiposNotificacion);
            cargarLista(listaLicencias);
            cargarLista(listaEstados);
            cargarLista(listaTiposViaINE);
            cargarLista(listaTiposObra);
            cargarLista(listaTiposObraMenor);

		} catch (Exception e) {
		}
		cargada = true;
	}

	public static void cargarLista(ListaEstructuras lista) {
		if (!lista.loadDB(CConstantesComando.loginLicenciasUrl))
			lista.loadFile();
		lista.saveFile();
	}

	public static boolean isCargada() {
		return cargada;
	}

	public static void setCargada(boolean cargada) {
		Estructuras.cargada = cargada;
	}

	public static boolean isIniciada() {
		return iniciada;
	}

	public static void setIniciada(boolean iniciada) {
		Estructuras.iniciada = iniciada;
	}


}

