/**
 * DatosImportarActividades.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.acteconomicas;
import org.apache.poi.hssf.usermodel.HSSFRow;

import com.vividsolutions.jts.geom.Geometry;


/**
 * Creado por SATEC.
 * User: angeles
 * Date: 05-jun-2006
 * Time: 11:45:49
 */
/**
 * Clase que contiene los datos a importar
 */
public class DatosImportarActividades {
    private String tipoviaine;
    private String nombreviaine;
    private String rotulo;
    private Geometry geometria;
    private String nif;
    private String sexo;
    private String nombre_empresa;
    private String nombre_comercial;
    private String codigo_postal;
    private String telefono;
    private String fax;
    private String forma_juridica;
    private String actividad_principal;
    private String actividades;
    private String sede_social;
    private Double sucursales;
    private String link_empresa;
    private String fecha_constitucion;
    private String empleados;
    private String volumen_negocio;
    private String exporta_importa;
    private String cargo1;
    private String sexo1;
    private String nombre1;
    private String cargo2;
    private String sexo2;
    private String nombre2;
    private String cargo3;
    private String sexo3;
    private String nombre3;
    private String cargo4;
    private String sexo4;
    private String nombre4;
    private String cargo5;
    private String sexo5;
    private String nombre5;
    private final String NO_ESPECIFICADO="NE";

    public DatosImportarActividades(HSSFRow row)
    {
             if (row==null)return;
             setNif(row.getCell(COL_NIF)==null?null:row.getCell(COL_NIF).getStringCellValue());
             setSexo(row.getCell(COL_SEXO)==null?null:row.getCell(COL_SEXO).getStringCellValue());
             setNombre_empresa(row.getCell(COL_NOMBRE_EMPRESA)==null?null:row.getCell(COL_NOMBRE_EMPRESA).getStringCellValue());
             setNombre_comercial(row.getCell(COL_NOMBRE_COMERCIAL)==null?null:row.getCell(COL_NOMBRE_COMERCIAL).getStringCellValue());
             setDireccion(row.getCell(COL_DIRECCION)==null?null:row.getCell(COL_DIRECCION).getStringCellValue());
             setCodigo_postal(row.getCell(COL_CODIGO_POSTAL)==null?null:row.getCell(COL_CODIGO_POSTAL).getStringCellValue());
             setTelefono(row.getCell(COL_TELEFONO)==null?null:row.getCell(COL_TELEFONO).getStringCellValue());
             setFax(row.getCell(COL_FAX)==null?null:row.getCell(COL_FAX).getStringCellValue());
             setForma_juridica(row.getCell(COL_FORMA_JURIDICA)==null?null:row.getCell(COL_FORMA_JURIDICA).getStringCellValue());
             setActividad_principal(row.getCell(COL_ACTIVIDAD_PRINCIPAL)==null?null:row.getCell(COL_ACTIVIDAD_PRINCIPAL).getStringCellValue());
             setActividades(row.getCell(COL_ACTIVIDADES)==null?null:row.getCell(COL_ACTIVIDADES).getStringCellValue());
             setSede_social(row.getCell(COL_SEDE_SOCIAL)==null?null:row.getCell(COL_SEDE_SOCIAL).getStringCellValue());
             setSucursales(row.getCell(COL_SUCURSALES)==null?null:new Double(row.getCell(COL_SUCURSALES).getStringCellValue()));
             setLink_empresa(row.getCell(COL_LINK)==null?null:row.getCell(COL_LINK).getStringCellValue());
             setFecha_constitucion(row.getCell(COL_FECHA_CONSTITUCION)==null?null:row.getCell(COL_FECHA_CONSTITUCION).getStringCellValue());
             setEmpleados(row.getCell(COL_EMPLEADOS)==null?null:row.getCell(COL_EMPLEADOS).getStringCellValue());
             setVolumen_negocio(row.getCell(COL_VOLUMEN)==null?null:row.getCell(COL_VOLUMEN).getStringCellValue());
             setExporta_importa(row.getCell(COL_EXP_IMP)==null?null:row.getCell(COL_EXP_IMP).getStringCellValue());
             setCargo1(row.getCell(COL_CARGO1)==null?null:row.getCell(COL_CARGO1).getStringCellValue());
             setSexo1(row.getCell(COL_SEXO1)==null?null:row.getCell(COL_SEXO1).getStringCellValue());
             setNombre1(row.getCell(COL_NOMBRE1)==null?null:row.getCell(COL_NOMBRE1).getStringCellValue());
             setCargo2(row.getCell(COL_CARGO2)==null?null:row.getCell(COL_CARGO2).getStringCellValue());
             setSexo2(row.getCell(COL_SEXO2)==null?null:row.getCell(COL_SEXO2).getStringCellValue());
             setNombre2(row.getCell(COL_NOMBRE2)==null?null:row.getCell(COL_NOMBRE2).getStringCellValue());
             setCargo3(row.getCell(COL_CARGO3)==null?null:row.getCell(COL_CARGO3).getStringCellValue());
             setSexo3(row.getCell(COL_SEXO3)==null?null:row.getCell(COL_SEXO3).getStringCellValue());
             setNombre3(row.getCell(COL_NOMBRE3)==null?null:row.getCell(COL_NOMBRE3).getStringCellValue());
             setCargo4(row.getCell(COL_CARGO4)==null?null:row.getCell(COL_CARGO4).getStringCellValue());
             setSexo4(row.getCell(COL_SEXO4)==null?null:row.getCell(COL_SEXO4).getStringCellValue());
             setNombre4(row.getCell(COL_NOMBRE4)==null?null:row.getCell(COL_NOMBRE4).getStringCellValue());
             setCargo5(row.getCell(COL_CARGO5)==null?null:row.getCell(COL_CARGO5).getStringCellValue());
             setSexo5(row.getCell(COL_SEXO5)==null?null:row.getCell(COL_SEXO5).getStringCellValue());
             setNombre5(row.getCell(COL_NOMBRE5)==null?null:row.getCell(COL_NOMBRE5).getStringCellValue());
    }


    public Geometry getGeometria() {
        return geometria;
    }

    public void setGeometria(Geometry geometria) {
        this.geometria = geometria;
    }

    /**
     * El formato de dirección que llega es el siguiente
     * CL CIUDAD ENCANTADA, S/N
     * Este metodo búcara el tipo de via al principio, el resto
     * hasta la coma será el nombre de la vía, y lo que hay
     * detras del numero será el rotulo
     * @param direccion
     */

    public void setDireccion(String direccion) {
       if (direccion==null) return;
       String[] tiposVias={"CL","PZ","AV",NO_ESPECIFICADO,"CM","CR","RD","TR"};
       //setTipoviaine(NO_ESPECIFICADO);
       for (int i=0;i<tiposVias.length;i++)
       {
           if (direccion.startsWith(tiposVias[i]+" "))
           {
               setTipoviaine(tiposVias[i]);
               direccion=direccion.substring((tiposVias[i]+" ").length());
               break;
           }
       }
       int posComa=direccion.indexOf(',');
       if (posComa>=0)
       {
           try{setRotulo(direccion.substring(posComa+1).trim());}catch(Exception e){}
           try{setNombreviaine(direccion.substring(0,posComa).trim());}catch(Exception e){}
       }else
           setNombreviaine(direccion.trim());
    }

    public String getNombreviaine() {
        return nombreviaine;
    }

    public void setNombreviaine(String nombreviaine) {
        this.nombreviaine = nombreviaine;
    }

    public String getRotulo() {
        return rotulo;
    }

    public void setRotulo(String rotulo) {
        this.rotulo = rotulo;
    }

    public String getTipoviaine() {
        return tipoviaine;
    }

    public void setTipoviaine(String tipoviaine) {
        this.tipoviaine = tipoviaine;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getNombre_comercial() {
        return nombre_comercial;
    }

    public void setNombre_comercial(String nombre_comercial) {
        this.nombre_comercial = nombre_comercial;
    }

    public String getForma_juridica() {
        return forma_juridica;
    }

    public void setForma_juridica(String forma_juridica) {
        this.forma_juridica = forma_juridica;
    }

    public String getLink_empresa() {
        return link_empresa;
    }

    public void setLink_empresa(String link_empresa) {
        this.link_empresa = link_empresa;
    }

    public String getNombre_empresa() {
        return nombre_empresa;
    }

    public void setNombre_empresa(String nombre_empresa) {
        this.nombre_empresa = nombre_empresa;
    }

    public String getCodigo_postal() {
        return codigo_postal;
    }

    public void setCodigo_postal(String codigo_postal) {
        this.codigo_postal = codigo_postal;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getActividad_principal() {
        return actividad_principal;
    }

    public void setActividad_principal(String actividad_principal) {
        this.actividad_principal = actividad_principal;
    }

    public String getActividades() {
        return actividades;
    }

    public void setActividades(String actividades) {
        this.actividades = actividades;
    }

    public String getSede_social() {
        return sede_social;
    }

    public void setSede_social(String sede_social) {
        this.sede_social = sede_social;
    }

    public Double getSucursales() {
        return sucursales;
    }

    public void setSucursales(Double sucursales) {
        this.sucursales = sucursales;
    }

    public String getFecha_constitucion() {
        return fecha_constitucion;
    }

    public void setFecha_constitucion(String fecha_constitucion) {
        this.fecha_constitucion = fecha_constitucion;
    }

    public String getEmpleados() {
        return empleados;
    }

    public void setEmpleados(String empleados) {
        this.empleados = empleados;
    }

    public String getVolumen_negocio() {
        return volumen_negocio;
    }

    public void setVolumen_negocio(String volumen_negocio) {
        this.volumen_negocio = volumen_negocio;
    }

    public String getExporta_importa() {
        return exporta_importa;
    }

    public void setExporta_importa(String exporta_importa) {
        this.exporta_importa = exporta_importa;
    }

    public String getCargo1() {
        return cargo1;
    }

    public void setCargo1(String cargo1) {
        this.cargo1 = cargo1;
    }

    public String getSexo1() {
        return sexo1;
    }

    public void setSexo1(String sexo1) {
        this.sexo1 = sexo1;
    }

    public String getNombre1() {
        return nombre1;
    }

    public void setNombre1(String nombre1) {
        this.nombre1 = nombre1;
    }

    public String getCargo2() {
        return cargo2;
    }

    public void setCargo2(String cargo2) {
        this.cargo2 = cargo2;
    }

    public String getSexo2() {
        return sexo2;
    }

    public void setSexo2(String sexo2) {
        this.sexo2 = sexo2;
    }

    public String getNombre2() {
        return nombre2;
    }

    public void setNombre2(String nombre2) {
        this.nombre2 = nombre2;
    }

    public String getCargo3() {
        return cargo3;
    }

    public void setCargo3(String cargo3) {
        this.cargo3 = cargo3;
    }

    public String getSexo3() {
        return sexo3;
    }

    public void setSexo3(String sexo3) {
        this.sexo3 = sexo3;
    }

    public String getNombre3() {
        return nombre3;
    }

    public void setNombre3(String nombre3) {
        this.nombre3 = nombre3;
    }

    public String getCargo4() {
        return cargo4;
    }

    public void setCargo4(String cargo4) {
        this.cargo4 = cargo4;
    }

    public String getSexo4() {
        return sexo4;
    }

    public void setSexo4(String sexo4) {
        this.sexo4 = sexo4;
    }

    public String getNombre4() {
        return nombre4;
    }

    public void setNombre4(String nombre4) {
        this.nombre4 = nombre4;
    }

    public String getCargo5() {
        return cargo5;
    }

    public void setCargo5(String cargo5) {
        this.cargo5 = cargo5;
    }

    public String getSexo5() {
        return sexo5;
    }

    public void setSexo5(String sexo5) {
        this.sexo5 = sexo5;
    }

    public String getNombre5() {
        return nombre5;
    }

    public void setNombre5(String nombre5) {
        this.nombre5 = nombre5;
    }

    public String toString()
    {
        //return (getTipoviaine()!=null?getTipoviaine():NO_ESPECIFICADO+" ")+
        //        getNombreviaine() + " " + (getRotulo()!=null?getRotulo():"");
        return getNombreviaine();
    }
    public String toStringNew()
    {
        return (getTipoviaine()!=null?getTipoviaine():NO_ESPECIFICADO)+" "+
                getNombreviaine() + " " + (getRotulo()!=null?getRotulo():"");
    }

    public String toStringAll() {
        return "DatosImportarActividades{" +
                "tipoviaine='" + tipoviaine + "'" +
                ", nombreviaine='" + nombreviaine + "'" +
                ", rotulo='" + rotulo + "'" +
                ", geometria=" + geometria +
                ", nif='" + nif + "'" +
                ", sexo='" + sexo + "'" +
                ", nombre_empresa='" + nombre_empresa + "'" +
                ", nombre_comercial='" + nombre_comercial + "'" +
                ", codigo_postal='" + codigo_postal + "'" +
                ", telefono='" + telefono + "'" +
                ", fax='" + fax + "'" +
                ", forma_juridica='" + forma_juridica + "'" +
                ", actividad_principal='" + actividad_principal + "'" +
                ", actividades='" + actividades + "'" +
                ", sede_social='" + sede_social + "'" +
                ", sucursales=" + sucursales +
                ", link_empresa='" + link_empresa + "'" +
                ", fecha_constitucion='" + fecha_constitucion + "'" +
                ", empleados='" + empleados + "'" +
                ", volumen_negocio='" + volumen_negocio + "'" +
                ", exporta_importa='" + exporta_importa + "'" +
                ", cargo1='" + cargo1 + "'" +
                ", sexo1='" + sexo1 + "'" +
                ", nombre1='" + nombre1 + "'" +
                ", cargo2='" + cargo2 + "'" +
                ", sexo2='" + sexo2 + "'" +
                ", nombre2='" + nombre2 + "'" +
                ", cargo3='" + cargo3 + "'" +
                ", sexo3='" + sexo3 + "'" +
                ", nombre3='" + nombre3 + "'" +
                ", cargo4='" + cargo4 + "'" +
                ", sexo4='" + sexo4 + "'" +
                ", nombre4='" + nombre4 + "'" +
                ", cargo5='" + cargo5 + "'" +
                ", sexo5='" + sexo5 + "'" +
                ", nombre5='" + nombre5 + "'" +
                "}";
    }

    public static final short COL_NIF=2;
    public static final short COL_SEXO=3;
    public static final short COL_NOMBRE_EMPRESA=4;
    public static final short COL_NOMBRE_COMERCIAL=5;
    public static final short COL_DIRECCION=6;
    public static final short COL_CODIGO_POSTAL=7;
    public static final short COL_TELEFONO=10;
    public static final short COL_FAX=11;
    public static final short COL_FORMA_JURIDICA=12;
    public static final short COL_ACTIVIDAD_PRINCIPAL=13;
    public static final short COL_ACTIVIDADES=14;
    public static final short COL_SEDE_SOCIAL=15;
    public static final short COL_SUCURSALES=16;
    public static final short COL_LINK=17;
    public static final short COL_FECHA_CONSTITUCION=18;
    public static final short COL_EMPLEADOS=19;
    public static final short COL_VOLUMEN=20;
    public static final short COL_EXP_IMP=21;
    public static final short COL_CARGO1=22;
    public static final short COL_SEXO1=23;
    public static final short COL_NOMBRE1=24;
    public static final short COL_CARGO2=25;
    public static final short COL_SEXO2=26;
    public static final short COL_NOMBRE2=27;
    public static final short COL_CARGO3=28;
    public static final short COL_SEXO3=29;
    public static final short COL_NOMBRE3=30;
    public static final short COL_CARGO4=31;
    public static final short COL_SEXO4=32;
    public static final short COL_NOMBRE4=33;
    public static final short COL_CARGO5=34;
    public static final short COL_SEXO5=35;
    public static final short COL_NOMBRE5=36;
}
