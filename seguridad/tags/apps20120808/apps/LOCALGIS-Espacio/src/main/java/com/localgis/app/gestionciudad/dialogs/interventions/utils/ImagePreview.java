/**
 * 
 */
package com.localgis.app.gestionciudad.dialogs.interventions.utils;

/**
 * @author javieraragon
 *
 */
import javax.swing.*;
import java.beans.*;
import java.awt.*;
import java.io.File;

public class ImagePreview extends JComponent 
                           implements PropertyChangeListener {
    ImageIcon thumbnail = null;
    File f = null;
                             
    public ImagePreview(JFileChooser fc) {
        setPreferredSize(new Dimension(100, 50));
        fc.addPropertyChangeListener(this);
    }

    public void loadImage() {
        if (f == null)
            return;
 
        ImageIcon tmpIcon = new ImageIcon(f.getPath());
        if (tmpIcon.getIconWidth() > 90) {
            thumbnail = new ImageIcon(tmpIcon.getImage().
                                      getScaledInstance(90, -1,
                                                        Image.SCALE_DEFAULT));
        } else {
            thumbnail = tmpIcon;
        }
    }

    public void propertyChange(PropertyChangeEvent e) {
        String prop = e.getPropertyName();
        if (prop.equals(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY)) {
            f = (File) e.getNewValue();
            if(isShowing()) {
                loadImage();
                repaint();
            }
        }
    }

    public void paint(Graphics g) {
        if (thumbnail == null) {
            loadImage();
        }
        if (thumbnail != null) {
            int x = getWidth()/2 - thumbnail.getIconWidth()/2;
            int y = getHeight()/2 - thumbnail.getIconHeight()/2;

            if (y < 0) {
                y = 0;
            }

            if (x < 5) {
                x = 5;
            }
            thumbnail.paintIcon(this, g, x, y);
        }
    }
}
