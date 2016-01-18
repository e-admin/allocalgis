package com.geopista.app.utilidades;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ResourceBundle;

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

import javax.swing.*;


public class TextPane extends JTextArea{

    private int maxLength;
    private JPopupMenu jPopupMenu;
    public TextPane(int maxLength)
    {
         super();
         this.maxLength=maxLength;
         this.addKeyListener(
              new KeyAdapter(){
                 public void keyTyped(KeyEvent keyEvent){
                     if (controlaLength())
                     {
                         char c = keyEvent.getKeyChar();
                         if((c==KeyEvent.VK_BACK_SPACE)||
                                (c==KeyEvent.VK_DELETE) ||
                                (c==KeyEvent.VK_ENTER)||
                                (c==KeyEvent.VK_TAB)||
                                 keyEvent.isActionKey())
                                 return;
                              else {
                                 keyEvent.consume();
                                 }

                     }
                 }
              }
          );
        initComponent();
    }
    private void initComponent()
    {
        this.setLineWrap(true);
        this.setWrapStyleWord(true);
                
        jPopupMenu= new JPopupMenu();
        JMenuItem jMenuCopy = new JMenuItem();
        jMenuCopy.setText("Copiar");
        jMenuCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
             copiar();
            }
           });
        jPopupMenu.add(jMenuCopy);

        JMenuItem jMenuPegar = new JMenuItem();
        jMenuPegar.setText("Pegar");
        jMenuPegar.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                     pegar();
                    }
                   });
        jPopupMenu.add(jMenuPegar);

        JMenuItem jMenuCortar = new JMenuItem();
               jMenuCortar.setText("Cortar");
               jMenuCortar.addActionListener(new java.awt.event.ActionListener() {
                           public void actionPerformed(java.awt.event.ActionEvent evt) {
                            cortar();
                           }
                          });
       jPopupMenu.add(jMenuCortar);

       JMenuItem jMenuBorrar = new JMenuItem();
       jMenuBorrar.setText("Borrar");
       jMenuBorrar.addActionListener(new java.awt.event.ActionListener() {
                                  public void actionPerformed(java.awt.event.ActionEvent evt) {
                                   borrar();
                                  }
                                 });
       jPopupMenu.add(jMenuBorrar);
       jPopupMenu.addSeparator();
       JMenuItem jMenuSeleccionar = new JMenuItem();
       jMenuSeleccionar.setText("Seleccionar todo");
       jMenuSeleccionar.addActionListener(new java.awt.event.ActionListener() {
                                  public void actionPerformed(java.awt.event.ActionEvent evt) {
                                   seleccionarTodo();
                                  }
                                 });
       jPopupMenu.add(jMenuSeleccionar);

        this.addMouseListener (
                new MouseAdapter () {
                   public void mouseReleased( MouseEvent e ) {
                      if ( e.isPopupTrigger()) {
                          jPopupMenu.show( (JComponent)e.getSource(), e.getX(), e.getY() );
                          }
                      }
                   }
                );

    }
    private void copiar()
    {
        this.copy();
    }
    private void pegar()
    {
        this.paste();
    }
    private void cortar()
    {
          this.cut();
    }
    private void borrar()
    {
        if (this.isEditable()&&this.isEnabled())

         this.setText("");
    }
    private void seleccionarTodo()
    {
        this.selectAll();
    }
     public boolean controlaLength()
   {
        if (super.getText().length() >=  maxLength)
            return true;
        return false;
  }
    public String getText()
    {
        if (maxLength==0) return super.getText();
        if (super.getText().length()<=maxLength)  return super.getText();
        else
            return super.getText().substring(0,maxLength-1);
    }



}
