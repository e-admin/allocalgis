/**
 * HTMLPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vividsolutions.jump.workbench.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;


public class HTMLPanel extends JPanel  implements RecordPanelModel  {
    private ArrayList history = new ArrayList();
    protected JButton okButton = new JButton();
    private RecordPanel recordPanel = new RecordPanel(this);
    private JPanel southPanel = new JPanel();
    private JScrollPane scrollPane = new JScrollPane();
    private JEditorPane editorPane = new JEditorPane();
    private int currentIndex = -1;

    public HTMLPanel() {
        try {
            jbInit();            
        } catch (Exception e) {
            e.printStackTrace();
        }

        okButton.setVisible(false);
    }

    public RecordPanel getRecordPanel() {
        return recordPanel;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void addField(String label, String value) {
        addField(label, value, "");
    }

    private void setLatestDocument(String document) {
        history.set(history.size() - 1, document);
    }

    private String getLatestDocument() {
        return (String) history.get(history.size() - 1);
    }

    protected void setEditorPaneText() {
        final String document = (String) history.get(currentIndex);

            SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        editorPane.setText("<HTML>" + document + "</HTML>");
                    }
                });
            scrollToTop();
    }

    /**
     * Appends HTML text to the frame.
     * @param html the HTML to append
     */
    public void append(final String html) {
        setLatestDocument(getLatestDocument() + html);
        goToLatestDocument();
    }

    /**
     * Appends non-HTML text to the frame.  Text is assumed to be non-HTML, and is
     * HTML-escaped to avoid control-char conflict.
     * @param text
     */
    public void addText(String text) {
        append(GUIUtil.escapeHTML(text, false, true) + " <BR>\n");
    }

    public void addField(String label, String value, String units) {
        append("<B> " + label + " </B>" + value + " " + units + " <BR>\n");
    }

    /**
     *@param  level  1, 2, 3, ...
     */
    public void addHeader(int level, String text) {
        append("<H" + level + "> " + GUIUtil.escapeHTML(text, false, false) +
            " </H" + level + ">\n");
    }
    private JPanel fillerPanel = new JPanel();    
    public JButton getOKButton() { return okButton; }
    private GridBagLayout gridBagLayout1 = new GridBagLayout();    

    private void jbInit() throws Exception {
        setLayout(new BorderLayout());
        editorPane.setEditable(false);
        editorPane.setContentType("text/html");
        southPanel.setLayout(gridBagLayout1);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        okButton.setText("OK");

        add(scrollPane, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
        southPanel.add(fillerPanel,
            new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        southPanel.add(recordPanel,
            new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        southPanel.add(okButton,
            new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        scrollPane.getViewport().add(editorPane, null);
    }

    public void setRecordNavigationControlVisible(boolean visible) {
        southPanel.setVisible(visible);
    }
    
    public void scrollToTop() {
        //The text is set using #invokeLater, so the scroll-to-top must
        //also be done using #invokeLater. [Jon Aquino]
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                editorPane.setCaretPosition(0);
            }
        });
    }    
    
    private void goToLatestDocument() {
        setCurrentIndex(history.size() - 1);
    }    

    public void createNewDocument() {
        history.add("");
        goToLatestDocument();
        recordPanel.updateAppearance();
    }

    public void setCurrentIndex(int index) {
        this.currentIndex = index;
        setEditorPaneText();
    }

    public int getRecordCount() {
        return history.size();
    }

    public Color getBackgroundColor() {
        return editorPane.getBackground();
    }

    public void setBackgroundColor(Color color) {
        editorPane.setBackground(color);
    }
}
