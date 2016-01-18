/**
 * Alegacion.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.licencias;

import java.util.Date;
import java.util.Vector;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 15-abr-2005
 * Time: 17:17:04
 */
public class Alegacion {
       long idAlegacion;
       long idSolicitud;
       String numExpediente;
       String observaciones;
       Date fecha;
       Vector anexos;

       public Alegacion() {
           fecha= new Date();
       }

        public Alegacion(long idAlegacion, long idSolicitud, String numExpediente, String observaciones,Date fecha) {
            this.fecha = fecha;
            this.idAlegacion = idAlegacion;
            this.idSolicitud = idSolicitud;
            this.numExpediente = numExpediente;
            this.observaciones = observaciones;
        }

        public Alegacion(long idSolicitud, String numExpediente, String observaciones) {
            this.idSolicitud = idSolicitud;
            this.numExpediente = numExpediente;
            this.observaciones = observaciones;
            this.fecha= new Date();
        }

        public Date getFecha() {
            return fecha;
        }

        public void setFecha(Date fecha) {
            this.fecha = fecha;
        }

        public long getIdAlegacion() {
            return idAlegacion;
        }

        public void setIdAlegacion(long idAlegacion) {
            this.idAlegacion = idAlegacion;
        }

        public long getIdSolicitud() {
            return idSolicitud;
        }

        public void setIdSolicitud(long idSolicitud) {
            this.idSolicitud = idSolicitud;
        }

        public String getNumExpediente() {
            return numExpediente;
        }

        public void setNumExpediente(String numExpediente) {
            this.numExpediente = numExpediente;
        }

        public String getObservaciones() {
            return observaciones;
        }

        public void setObservaciones(String observaciones) {
            this.observaciones = observaciones;
        }

        public Vector getAnexos() {
            return anexos;
        }

        public void setAnexos(Vector anexos) {
            this.anexos = anexos;
        }
    }
    