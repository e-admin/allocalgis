package com.geopista.app.inventario;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import java.text.SimpleDateFormat;

import com.geopista.protocol.document.DocumentBean;
import com.geopista.protocol.inventario.*;
import com.geopista.ui.plugin.edit.DocumentManagerPlugin;

public class ListaRenderer extends DefaultListCellRenderer
{
    private String locale;
    public ListaRenderer(){
    }

    public ListaRenderer(String locale){
        this.locale= locale;
    }


    public Component getListCellRendererComponent(JList l,
       Object value, int i, boolean s, boolean f){
       String literal= "";
       if (value instanceof CompanniaSeguros){
            literal= ((CompanniaSeguros)value).getNombre()+" - " +((CompanniaSeguros)value).getDescripcion();
       }else if (value instanceof Observacion){
           String sFecha="";
           try{sFecha= Constantes.df.format(((Observacion)value).getFecha());}catch(Exception e){}
           literal= sFecha+" - " +((Observacion)value).getDescripcion();
       }else if (value instanceof Mejora){
           String sFecha="";
           try{sFecha= Constantes.df.format(((Mejora)value).getFechaEjecucion());}catch(Exception e){}
           literal= "Fecha Ejecucion:"+sFecha+" - Descripcion:" +((Mejora)value).getDescripcion();
       }else if (value instanceof ReferenciaCatastral){
           literal= ((ReferenciaCatastral)value).getRefCatastral()+" - " +((ReferenciaCatastral)value).getDescripcion();
       }else if (value instanceof UsoFuncional){
           //try{literal= Estructuras.getListaUsosFuncionales().getDomainNode(((UsoFuncional)value).getUso()).getTerm(locale)+" - " +((UsoFuncional)value).getSuperficie();}catch(Exception e){}
           try{literal= ((UsoFuncional)value).getUso()+" - Superficie:" +((UsoFuncional)value).getSuperficie();}catch(Exception e){}
       }else if (value instanceof CampoFiltro){
           if (((CampoFiltro)value).isDominio())
            try{literal= (((CampoFiltro)value).getTabla()!=null?"("+((CampoFiltro)value).getTabla()+") - ":"")+(((CampoFiltro)value).getDescripcion()==null?((CampoFiltro)value).getNombre():((CampoFiltro)value).getDescripcion())+" " +((CampoFiltro)value).getOperador()+" "+((CampoFiltro)value).getValorTerm();}catch(Exception e){}
           else try{literal= (((CampoFiltro)value).getTabla()!=null?"("+((CampoFiltro)value).getTabla()+" ) - ":"")+(((CampoFiltro)value).getDescripcion()==null?((CampoFiltro)value).getNombre():((CampoFiltro)value).getDescripcion())+" " +((CampoFiltro)value).getOperador()+" "+((CampoFiltro)value).getValorVarchar();}catch(Exception e){}
       }

       JLabel label =(JLabel) super.getListCellRendererComponent(l,literal,i, s, f);
       return label;
    }
}

