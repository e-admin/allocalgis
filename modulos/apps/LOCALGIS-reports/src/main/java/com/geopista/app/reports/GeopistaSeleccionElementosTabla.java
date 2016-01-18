/**
 * GeopistaSeleccionElementosTabla.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 21-jun-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.geopista.app.reports;

/**
 * @author dbaeza
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GeopistaSeleccionElementosTabla
{
    String orden;    //orden del campo en la tabla
    String tipo;  // E si es Espacial, N si no lo es
    String tabla; //Nombre de la TAbla no espacial
    String campo; //Campo de la Tabla no espacial
    String operador; //Operador de los campos no espaciales
    String valor1; // Valor1 del Where del campo no espacial
    String valor2; // Valor2 del Where del campo no espacial
    String tabla1; // Tabla1 de los campos espaciales
    String tabla2; // Tabla2 de los campos espaciales
    String Operador2;	//Operador de los campos espaciales
    String distancia;  // distancia en las consultas espaciales.
/**
 * @return Returns the campo.
 */
public String getCampo()
{
    return campo;
}
/**
 * @param campo The campo to set.
 */
public void setCampo(String campo)
{
    this.campo = campo;
}
/**
 * @return Returns the operador.
 */
public String getOperador()
{
    return operador;
}
/**
 * @param operador The operador to set.
 */
public void setOperador(String operador)
{
    this.operador = operador;
}
/**
 * @return Returns the operador2.
 */
public String getOperador2()
{
    return Operador2;
}
/**
 * @param operador2 The operador2 to set.
 */
public void setOperador2(String operador2)
{
    Operador2 = operador2;
}
/**
 * @return Returns the orden.
 */
public String getOrden()
{
    return orden;
}
/**
 * @param orden The orden to set.
 */
public void setOrden(String orden)
{
    this.orden = orden;
}
/**
 * @return Returns the tabla.
 */
public String getTabla()
{
    return tabla;
}
/**
 * @param tabla The tabla to set.
 */
public void setTabla(String tabla)
{
    this.tabla = tabla;
}
/**
 * @return Returns the tabla1.
 */
public String getTabla1()
{
    return tabla1;
}
/**
 * @param tabla1 The tabla1 to set.
 */
public void setTabla1(String tabla1)
{
    this.tabla1 = tabla1;
}
/**
 * @return Returns the tabla2.
 */
public String getTabla2()
{
    return tabla2;
}
/**
 * @param tabla2 The tabla2 to set.
 */
public void setTabla2(String tabla2)
{
    this.tabla2 = tabla2;
}
/**
 * @return Returns the tipo.
 */
public String getTipo()
{
    return tipo;
}
/**
 * @param tipo The tipo to set.
 */
public void setTipo(String tipo)
{
    this.tipo = tipo;
}
/**
 * @return Returns the valor1.
 */
public String getValor1()
{
    return valor1;
}
/**
 * @param valor1 The valor1 to set.
 */
public void setValor1(String valor1)
{
    this.valor1 = valor1;
}
/**
 * @return Returns the valor2.
 */
public String getValor2()
{
    return valor2;
}
/**
 * @param valor2 The valor2 to set.
 */
public void setValor2(String valor2)
{
    this.valor2 = valor2;
}

 


    /**
     * @return Returns the distancia.
     */
    public String getDistancia()
    {
        return distancia;
    }
    /**
     * @param distancia The distancia to set.
     */
    public void setDistancia(String distancia)
    {
        this.distancia = distancia;
    }
}
