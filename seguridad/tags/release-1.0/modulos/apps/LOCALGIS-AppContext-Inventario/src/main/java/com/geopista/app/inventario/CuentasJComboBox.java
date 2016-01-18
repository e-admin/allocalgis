package com.geopista.app.inventario;

import com.geopista.protocol.inventario.CuentaContable;
import com.geopista.protocol.inventario.CuentaAmortizacion;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 01-ago-2006
 * Time: 15:43:29
 * To change this template use File | Settings | File Templates.
 */
public class CuentasJComboBox extends JComboBox{
    boolean conBlanco=false;

    public CuentasJComboBox (Object[] lista, java.awt.event.ActionListener accion){
        this(lista, accion, true);
    }

    public CuentasJComboBox (Object[] lista, java.awt.event.ActionListener accion, boolean conBlanco){
        super();

        this.conBlanco=conBlanco;
        if (conBlanco){
            this.addItem(new CuentaContable());

        }
        if (lista!=null){
            for (int i=0; i<lista.length;i++){
                  this.addItem(lista[i]);
            }
        }
        this.setRenderer(new com.geopista.app.inventario.CuentasComboBoxRenderer());
        if (accion!=null)
            this.addActionListener(accion);
  }
  public void setSelected(long id){
      if (id==-1){
          if (conBlanco) this.setSelectedIndex(0);
          return;
      }
      int iSize=this.getModel().getSize();
      for (int i=0; i<iSize;i++){
          Object obj= this.getModel().getElementAt(i);
          if (obj instanceof CuentaContable){
              CuentaContable cc= (CuentaContable)obj;
              if (id==cc.getId()){
                  this.getModel().setSelectedItem(cc);
                  return;
              }
          }else if (obj instanceof CuentaAmortizacion){
              CuentaAmortizacion ca= (CuentaAmortizacion)obj;
              if (id==ca.getId()){
                  this.getModel().setSelectedItem(ca);
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
