package com.geopista.app.eiel;



import java.util.ArrayList;
import java.util.Collections;

import com.geopista.app.filter.CampoFiltro;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 06-oct-2006
 * Time: 11:46:58
 * To change this template use File | Settings | File Templates.
 */
public class CamposBusquedaFiltro {

    private static ArrayList campos;

    public static final int UsoJuridico=0;
    public static final int FormaAdquisicion=1;
    public static final int Propiedad=2;
    public static final int EstadoConservacion=3;
    public static final int TipoConstruccion=4;
    public static final int Cubierta=5;
    public static final int Carpinteria=6;
    public static final int Fachada=7;
    public static final int Aprovechamiento=8;
    public static final int TipoAmortizacion=9;
    public static final int UsoFuncional=10;
    public static final int Documentos=11;
    public static final int Concepto=12;
    public static final int Raza=13;
    public static final int CodigoVia=14;
    public static final int TipoVehiculo=15;
    public static final int Traccion=16;
    public static final int ClaseValorMobiliario=17;
    public static final int Arrendamiento=18;
    public static final int CatTransmision=19;
    public static final int ClaseCredito=20;
    public static final int SubclaseCredito=21;
    public static final int ClaseDchoReales=22;
    public static final int ClaseMuebles=23;
    public static final int ClaseUrbano=24;
    public static final int ClaseRustico=25;
    /* Si se añade alguna constante para algun dominio, mirar la clase FiltroJPanel para añadirlo */
    
    public static ArrayList getCamposCaptaciones(){
        campos= new ArrayList();

        CampoFiltro campo= new CampoFiltro();
        campo.setNombre("fecha_adquisicion");
        campo.setTabla("vehiculo");
        campo.setDate();
        campos.add(campo);
        
        campo= new CampoFiltro();
        campo.setNombre("adquisicion");
        campo.setTabla("vehiculo");
        campo.setIsDominio();
        campo.setDominio(FormaAdquisicion);
        campos.add(campo);
        campo= new CampoFiltro();
        campo.setNombre("matricula_vieja");
        campo.setTabla("vehiculo");
        campo.setVarchar();
        campos.add(campo);
        campo= new CampoFiltro();
        campo.setNombre("matricula_nueva");
        campo.setTabla("vehiculo");
        campo.setVarchar();
        campos.add(campo);
        campo= new CampoFiltro();
        campo.setNombre("num_bastidor");
        campo.setTabla("vehiculo");
        campo.setVarchar();
        campos.add(campo);
        campo= new CampoFiltro();
        campo.setNombre("marca");
        campo.setTabla("vehiculo");
        campo.setVarchar();
        campos.add(campo);
        campo= new CampoFiltro();
        campo.setNombre("motor");
        campo.setTabla("vehiculo");
        campo.setVarchar();
        campos.add(campo);
        campo= new CampoFiltro();
        campo.setNombre("fuerza");
        campo.setTabla("vehiculo");
        campo.setVarchar();
        campos.add(campo);
        campo= new CampoFiltro();
        campo.setNombre("servicio");
        campo.setTabla("vehiculo");
        campo.setVarchar();
        campos.add(campo);
        campo= new CampoFiltro();
        campo.setNombre("destino");
        campo.setTabla("vehiculo");
        campo.setVarchar();
        campos.add(campo);
        campo= new CampoFiltro();
        campo.setNombre("tipo");
        campo.setDescripcion("tipo de vehiculo");
        campo.setTabla("vehiculo");
        campo.setIsDominio();
        campo.setDominio(TipoVehiculo);
        campos.add(campo);
        campo= new CampoFiltro();
        campo.setNombre("estado_conservacion");
        campo.setTabla("vehiculo");
        campo.setIsDominio();
        campo.setDominio(EstadoConservacion);
        campos.add(campo);
        campo= new CampoFiltro();
        campo.setNombre("traccion");
        campo.setTabla("vehiculo");
        campo.setIsDominio();
        campo.setDominio(Traccion);
        campos.add(campo);
        campo= new CampoFiltro();
        campo.setNombre("propiedad");
        campo.setTabla("vehiculo");
        campo.setIsDominio();
        campo.setDominio(Propiedad);
        campos.add(campo);
        campo= new CampoFiltro();
        campo.setNombre("frutos");
        campo.setTabla("vehiculo");
        campo.setVarchar();
        campos.add(campo);
        campo= new CampoFiltro();
        campo.setNombre("importe_frutos");
        campo.setTabla("vehiculo");
        campo.setDouble();
        campos.add(campo);
        campo= new CampoFiltro();
        campo.setNombre("coste_adquisicion");
        campo.setTabla("vehiculo");
        campo.setDouble();
        campos.add(campo);
        campo= new CampoFiltro();
        campo.setNombre("valor_actual");
        campo.setTabla("vehiculo");
        campo.setDouble();
        campos.add(campo);

        Collections.sort(campos);
        return campos;
    }   
}
