package com.geopista.app.cementerios;

import com.geopista.protocol.cementerios.CampoFiltro;

import javax.swing.*;


public class CamposFiltroJComboBox extends JComboBox{
    
	boolean conBlanco=false;

    public CamposFiltroJComboBox (Object[] lista, java.awt.event.ActionListener accion){
        this(lista, accion, true);
    }

    public CamposFiltroJComboBox (Object[] lista, java.awt.event.ActionListener accion, boolean conBlanco){
        super();

        this.conBlanco=conBlanco;
        if (conBlanco){
            this.addItem(new CampoFiltro());

        }
        if (lista!=null){
            for (int i=0; i<lista.length;i++){
                  this.addItem(lista[i]);
            }
        }
        this.setRenderer(new CamposFiltroComboBoxRenderer());
        if (accion!=null)
            this.addActionListener(accion);
  }
  public void setSelected(String descripcion){
      if (descripcion==null){
          if (conBlanco) this.setSelectedIndex(0);
          return;
      }
      int iSize=this.getModel().getSize();
      for (int i=0; i<iSize;i++){
          Object obj= this.getModel().getElementAt(i);
          if (obj instanceof CampoFiltro){
              CampoFiltro campo= (CampoFiltro)obj;
              if (descripcion==campo.getDescripcion()){
                  this.getModel().setSelectedItem(campo);
                  return;
              }
          }
      }
      if (conBlanco) this.setSelectedIndex(0);
  }
  public Object getSelected(){
       if (getSelectedIndex()<0) return null;
       if (getSelectedIndex()==0 && conBlanco)return null;
       return getSelectedItem();
  }

    public void removeAllItems(){
        super.removeAllItems();
    }


}
