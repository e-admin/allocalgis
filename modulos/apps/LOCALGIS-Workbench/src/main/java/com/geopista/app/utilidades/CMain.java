/**
 * CMain.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.utilidades;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import com.geopista.ui.images.IconLoader;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.SplashPanel;
import com.vividsolutions.jump.workbench.ui.SplashWindow;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 22-jul-2004
 * Time: 15:26:56
 */
public class CMain extends javax.swing.JFrame implements WindowListener{
    public static final String VERSION_TEXT = "Version 3.0";
    protected static ImageIcon splashImage;
    protected static Class progressMonitorClass = SingleLineProgressMonitor.class;
    protected SplashWindow showSplash()
    {
        try
        {
            ProgressMonitor progressMonitor = (ProgressMonitor) progressMonitorClass.newInstance();
            SplashPanel splashPanel = new SplashPanel(splashImage(), VERSION_TEXT);
            splashPanel.add(progressMonitor,
                new GridBagConstraints(0, 10, 1, 1, 1, 0,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 10), 0, 0));
            SplashWindow splashWindow = new SplashWindow(splashPanel);
            splashWindow.setVisible(true);
            try {Thread.sleep(1000);}catch(Exception exa){};
            return splashWindow;
        }catch(Exception e)
        {
            StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
            System.out.println("ERROR:"+sw.toString());
            return null;
        }
    }
    // End of variables declaration//GEN-END:variables


private static class SingleLineProgressMonitor extends ProgressMonitor {
      public SingleLineProgressMonitor() {
          super(new JLabel(" "));
          ((JLabel) getComponent()).setFont(((JLabel) getComponent()).getFont()
                                             .deriveFont(Font.BOLD));
          ((JLabel) getComponent()).setHorizontalAlignment(JLabel.LEFT);
      }

      protected void addText(String s) {
          ((JLabel) getComponent()).setText(s);
      }
  }
    private static abstract class ProgressMonitor extends JPanel
            implements TaskMonitor {
            private Component component;

            public ProgressMonitor(Component component) {
                this.component = component;
                setLayout(new BorderLayout());
                add(component, BorderLayout.CENTER);
                setOpaque(false);
            }

            protected Component getComponent() {
                return component;
            }

            protected abstract void addText(String s);

            public void report(String description) {
                addText(description);
            }

            public void report(int itemsDone, int totalItems, String itemDescription) {
                addText(itemsDone + " / " + totalItems + " " + itemDescription);
            }

            public void report(Exception exception) {
                addText(StringUtil.toFriendlyName(exception.getClass().getName()));
            }

            public void allowCancellationRequests() {
            }

            public boolean isCancelRequested() {
                return false;
            }
        }
        public static ImageIcon splashImage() {
           if (splashImage == null)
                    splashImage = IconLoader.icon("splash.png");
           return splashImage;
       }

    public void windowActivated(WindowEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void windowClosed(WindowEvent e) {
        System.out.println("Sistema cerrado");
        try
        {
            //com.geopista.security.SecurityManager.logout();
        }catch(Exception ex)
        {
            StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
        }
    }

    public void windowClosing(WindowEvent e) {
        System.out.println("Cerrando el sistema");
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void windowDeactivated(WindowEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void windowDeiconified(WindowEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void windowIconified(WindowEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void windowOpened(WindowEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
    protected static void initLookAndFeel() throws Exception {
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		System.setProperty("apple.awt.showGrowBox", "true");
		if (UIManager.getLookAndFeel() != null
				&& UIManager.getLookAndFeel().getClass().getName().equals(UIManager.getSystemLookAndFeelClassName())) {
			return;
		}
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	}

}
