/**
 * ArbolEntry.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.metadatos.componentes;

import javax.swing.JComponent;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 02-sep-2004
 * Time: 18:15:09
 */
public class ArbolEntry {
    private String nombre;
    private int tipo;
    private int carpeta;
    private JComponent componente;


    public ArbolEntry() {

    }

    public ArbolEntry( String nombre, int tipo,int carpeta) {
        this.carpeta = carpeta;
        this.nombre = nombre;
        this.tipo = tipo;
    }

    public ArbolEntry( String nombre, int tipo,int carpeta, JComponent componente) {
        this.carpeta = carpeta;
        this.componente = componente;
        this.nombre = nombre;
        this.tipo = tipo;
    }

    public int getCarpeta() {
        return carpeta;
    }

    public void setCarpeta(int carpeta) {
        this.carpeta = carpeta;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
    public JComponent getComponente() {
         return componente;
     }

     public void setComponente(JComponent componente) {
         this.componente = componente;
     }

}
