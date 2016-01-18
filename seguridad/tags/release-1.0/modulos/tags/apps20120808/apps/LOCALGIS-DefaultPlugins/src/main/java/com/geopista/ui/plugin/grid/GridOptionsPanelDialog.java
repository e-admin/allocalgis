package com.geopista.ui.plugin.grid;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import com.geopista.app.AppContext;
import com.vividsolutions.jts.util.Assert;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.OptionsPanel;
import com.vividsolutions.jump.workbench.ui.snap.GridRenderer;
import com.vividsolutions.jump.workbench.ui.snap.SnapToGridPolicy;

public class GridOptionsPanelDialog extends JPanel implements OptionsPanel{

    AppContext appContext = (AppContext) AppContext.getApplicationContext();

    private BorderLayout borderLayout1 = new BorderLayout();
    private JPanel jPanel1 = new JPanel();    
    private JPanel jPanel2 = new JPanel();    
    private JPanel jPanel4 = new JPanel();
    private JCheckBox snapToGridCheckBox = new JCheckBox();    
    private JPanel jPanel5 = new JPanel();
    private Border border2;
    private TitledBorder titledBorder2;
    private GridBagLayout gridBagLayout4 = new GridBagLayout();
    private GridBagLayout gridBagLayout6 = new GridBagLayout();       
    private JTextField gridSizeTextField = new JTextField(7);    
    private JPanel jPanel7 = new JPanel();
    private JRadioButton showGridDotsRadioButton = new JRadioButton();
    private JRadioButton showGridLinesRadioButton = new JRadioButton();
    private JPanel jPanel6 = new JPanel();
    private Blackboard blackboard;
    private ButtonGroup buttonGroup = new ButtonGroup();    
    private JCheckBox showGridCheckBox = new JCheckBox();    
    private JLabel showGridUnitsLabel = new JLabel();
    
    
    public GridOptionsPanelDialog(Blackboard blackboard) {
        this.blackboard = blackboard;

        try {
            jbInit();
        } catch (Exception e) {
            Assert.shouldNeverReachHere(e.toString());
        }

        buttonGroup.add(showGridDotsRadioButton);
        buttonGroup.add(showGridLinesRadioButton);
        
        showGridCheckBox.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
                    updateEnabled();
                }
            });
        
        gridSizeTextField.addKeyListener(new KeyAdapter()
        {
           public void keyTyped(KeyEvent e)
           {
              char caracter = e.getKeyChar();
              // Verificar si la tecla pulsada no es un digito
              if(((caracter < '0') ||
                 (caracter > '9')) &&
                 (caracter != KeyEvent.VK_BACK_SPACE) && (caracter != '.'))
              {
            	  e.consume();  // ignorar el evento de teclado                 
              }              
        	  
           }
        });
        
        Locale loc=Locale.getDefault();      	 
    	ResourceBundle bundle2 = ResourceBundle.getBundle("com.geopista.ui.plugin.grid.languages.GridOptionsPaneli18n",loc,this.getClass().getClassLoader());    	
        I18N.plugInsResourceBundle.put("GridOptionsPanel",bundle2);
    }

    private void updateEnabled() {    
        
        gridSizeTextField.setEnabled(showGridCheckBox.isSelected());
        showGridDotsRadioButton.setEnabled(showGridCheckBox.isSelected());
        showGridLinesRadioButton.setEnabled(showGridCheckBox.isSelected());
    }

    public String validateInput() {
        
        try {
        	if(Double.isNaN(Double.parseDouble(gridSizeTextField.getText()))){
        		String errorMessage = "\"" + gridSizeTextField.getText() +
                "\" " + I18N.get("GridOptionsPanel","errorMessage02");
        		return errorMessage;
        	}
        	
            if (Double.parseDouble(gridSizeTextField.getText()) <= 0) { 
            	String errorMessage = "\"" + gridSizeTextField.getText() +
                "\" " + I18N.get("GridOptionsPanel","errorMessage01");
            	
                return errorMessage;
            }
        } catch (NumberFormatException e) {        	
        	
        	String errorMessage = "\"" + gridSizeTextField.getText() +
            "\" " + appContext.getI18nString(e.getMessage());
        	
            return errorMessage;
        }

        return null;
    }

    public void okPressed() {
        
        blackboard.put(SnapToGridPolicy.GRID_SIZE_KEY,
            Double.parseDouble(gridSizeTextField.getText()));
        blackboard.put(GridRenderer.DOTS_ENABLED_KEY,
            showGridDotsRadioButton.isSelected());
        blackboard.put(GridRenderer.LINES_ENABLED_KEY,
            showGridLinesRadioButton.isSelected());
        blackboard.put(GridRenderer.ENABLED_KEY, showGridCheckBox.isSelected());
 
    }

    public void init() {
      
        snapToGridCheckBox.setSelected(blackboard.get(
                SnapToGridPolicy.ENABLED_KEY, false));
        gridSizeTextField.setText("" +
            blackboard.get(SnapToGridPolicy.GRID_SIZE_KEY, 20d));
        showGridCheckBox.setSelected(blackboard.get(GridRenderer.ENABLED_KEY,
                false));
        showGridDotsRadioButton.setSelected(blackboard.get(
                GridRenderer.DOTS_ENABLED_KEY, false));
        showGridLinesRadioButton.setSelected(blackboard.get(
                GridRenderer.LINES_ENABLED_KEY, true));
        updateEnabled();
    }

    private void jbInit() throws Exception {
        
        border2 = BorderFactory.createEtchedBorder(Color.white,
                new Color(148, 145, 140));        		
        titledBorder2 = new TitledBorder(border2, I18N.get("GridOptionsPanel","plugin.gridoptionspanel.gridSnapOptions"));
        this.setLayout(borderLayout1);       
        jPanel5.setBorder(titledBorder2);
        jPanel5.setLayout(gridBagLayout4);
        gridSizeTextField.setText("20");
        gridSizeTextField.setHorizontalAlignment(SwingConstants.TRAILING);
        jPanel7.setLayout(gridBagLayout6);
        showGridDotsRadioButton.setSelected(true);
        showGridDotsRadioButton.setText(I18N.get("GridOptionsPanel","plugin.gridoptionspanel.snapDotsSnapOptions"));
        showGridLinesRadioButton.setText(I18N.get("GridOptionsPanel","plugin.gridoptionspanel.snapLinesSnapOptions"));
        showGridCheckBox.setToolTipText("");
        showGridCheckBox.setText(I18N.get("GridOptionsPanel","plugin.gridoptionspanel.snapSizeSnapOptions"));
        showGridCheckBox.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    showGridCheckBox_actionPerformed(e);
                }
            });        
        showGridUnitsLabel.setText(I18N.get("GridOptionsPanel","plugin.gridoptionspanel.unitsModelSnapOptions"));
        this.add(jPanel1, BorderLayout.CENTER);
        jPanel1.add(jPanel2,          
        		 new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,        			
                new Insets(10, 10, 10, 10), 0, 0));        
        jPanel4.add(snapToGridCheckBox,
            new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));        
        jPanel1.add(jPanel5,
            new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 10, 10, 10), 0, 0));
        jPanel5.add(jPanel7,
             new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        jPanel7.add(gridSizeTextField,
                new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 5), 0, 0));
        jPanel7.add(showGridCheckBox,   new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        jPanel5.add(showGridDotsRadioButton,
            new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        jPanel5.add(showGridLinesRadioButton,
            new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        jPanel1.add(jPanel6,
            new GridBagConstraints(0, 2, 1, 1, 0.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                new Insets(0, 0, 0, 0), 0, 0));
        jPanel7.add(showGridUnitsLabel, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        
        init();
        updateEnabled();
    }

    void showGridCheckBox_actionPerformed(ActionEvent e) {
        updateEnabled();
    }
}
