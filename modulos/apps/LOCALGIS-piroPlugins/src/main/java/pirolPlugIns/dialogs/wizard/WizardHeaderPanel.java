/**
 * WizardHeaderPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 19.05.2005 for PIROL
 * 
 * CVS header information:
 * $RCSfile: WizardHeaderPanel.java,v $
 * $Revision: 1.1 $
 * $Date: 2009/07/03 12:31:55 $
 * $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/dialogs/wizard/WizardHeaderPanel.java,v $
 */
package pirolPlugIns.dialogs.wizard;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * This is the <code>Panel</code> shown at the top of the 
 * <code>{@link WizardDialog}</code>. It shows a decribing text at the top left
 * corner and an image at the top right corner just behind the text.
 * @author Carsten Schulze
 * @author FH Osnabr&uuml;ck - University of Applied Sciences Osnabr&uuml;ck,
 * Project: PIROL (2005),
 * Subproject: Daten- und Wissensmanagement 
 */
public class WizardHeaderPanel extends JPanel {
    private static final ImageIcon DEFAULT_ICON = 
        new ImageIcon("pirolPlugIns/dialogs/wizard/wizards_hat_and_wand.png");
    
    private static final String DEFAULT_STRING = new String("Wizard");
    /**
     * For debugging and testing this class.
     * @param args ignored
     */
    public static void main(String[] args){
        WizardHeaderPanel hp = new WizardHeaderPanel("Beschreibung",null);
        
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(hp);
        frame.setSize(hp.getSize());
        frame.setVisible(true);      
    }
    private JLabel imageLabel;
    private JTextArea textArea;
    /**
     * The Panel will have the size of 400 x 75 Pixel.
     * @param description the description text to be printed in the top left 
     * corner of this panel.
     * @param image the image that will be setteled in the top right corner of 
     * this panel.
     */
    public WizardHeaderPanel(String description, ImageIcon image){
        if(description == null){
            description = DEFAULT_STRING;
        }
        if(image == null){
            image = DEFAULT_ICON;
        }
        
        setLayout(null);
        setBackground(null);
        setSize(400,75);
        setPreferredSize(getSize());
        
        imageLabel = new JLabel(image);
        imageLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        imageLabel.setAlignmentY(Component.TOP_ALIGNMENT);
        
        JPanel imagePanel = new JPanel();
        imagePanel.setOpaque(false);
        imagePanel.setSize(this.getSize());
        imagePanel.setLocation(0,0);
        imagePanel.setLayout(new FlowLayout(FlowLayout.RIGHT,0,0));
        imagePanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        imagePanel.setAlignmentY(Component.TOP_ALIGNMENT);
        imagePanel.add(imageLabel);
        
        textArea = new JTextArea(description);
        textArea.setOpaque(false);
        textArea.setFont(new Font("SansSerif",Font.BOLD,14));
        textArea.setSize(this.getSize().width-10,this.getSize().height-10);
        textArea.setLocation(5,5);
        textArea.setFocusable(false);
        
        add(textArea);
        add(imagePanel);
    }
    
    /**
     * Specifies the background color of this panel.
     * @param color the new background color. If this parameter is 
     * <code>null, Color.WHITE</code> is used. 
     */
    public void setBackground(Color color) {
        if(color == null){
            super.setBackground(Color.WHITE);
        }
        else{
            super.setBackground(color);
        }
    }
    /**
     * This method can be used to change the description text.
     * @param text the new text.
     */
    public void setDescriptionText(String text){
        if(text == null){
            text = DEFAULT_STRING;
        }
        textArea.setText(text);
    }
    /**
     * This method can be used to change the image.
     * @param image the image used as icon.
     */
    public void setImage(ImageIcon image) {
        if(image == null){
            image = DEFAULT_ICON;
            
        }
        imageLabel.setIcon(image);
    }
}
