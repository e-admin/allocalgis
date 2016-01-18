/**
 * InternalFrameTest.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vividsolutions.jump.workbench.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;


public class InternalFrameTest {
    public static InternalFrameListener listener = new InternalFrameListener() {
                 public void internalFrameOpened(InternalFrameEvent e) {
                     System.out.println(new Date() +
                         "             public void internalFrameOpened(InternalFrameEvent e) {");
                 }

                 public void internalFrameClosing(InternalFrameEvent e) {
                     System.out.println(new Date() +
                         "             public void internalFrameClosing(InternalFrameEvent e) {");
                 }

                 public void internalFrameClosed(InternalFrameEvent e) {
                     System.out.println(new Date() +
                         "             public void internalFrameClosed(InternalFrameEvent e) {");
                 }

                 public void internalFrameIconified(InternalFrameEvent e) {
                     System.out.println(new Date() +
                         "             public void internalFrameIconified(InternalFrameEvent e) {");
                 }

                 public void internalFrameDeiconified(InternalFrameEvent e) {
                     System.out.println(new Date() +
                         "             public void internalFrameDeiconified(InternalFrameEvent e) {");
                 }

                 public void internalFrameActivated(InternalFrameEvent e) {
                     System.out.println(new Date() +
                         "             public void internalFrameActivated(InternalFrameEvent e) {");
                 }

                 public void internalFrameDeactivated(InternalFrameEvent e) {
                     System.out.println(new Date() +
                         "             public void internalFrameDeactivated(InternalFrameEvent e) {");
                 }
             };    
    public static void main(String[] args) {
        JInternalFrame internalFrame = new JInternalFrame("Test", true, true,
                true, true);
        internalFrame.setSize(100, 100);

        JDesktopPane desktopPane = new JDesktopPane();
        desktopPane.add(internalFrame);

        JFrame frame = new JFrame();
        frame.setSize(400, 400);
        frame.getContentPane().add(desktopPane);
        frame.setVisible(true);
        internalFrame.setVisible(true);
     
//internalFrame.addInternalFrameListener(listener);

                GUIUtil.addInternalFrameListener(desktopPane, GUIUtil.toInternalFrameListener(new ActionListener() {
        			public void actionPerformed(ActionEvent e) {
                        System.out.println(new Date());
        			}}));
    }
}
