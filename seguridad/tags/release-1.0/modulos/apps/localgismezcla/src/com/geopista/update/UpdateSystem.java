/*
 * Created on 09-feb-2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.geopista.update;

import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.jdom.JDOMException;
import com.geopista.app.AppContext;
import com.geopista.update.dialogs.UpdateSystemPanel;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.FlowLayout;

/**
 * @author jalopez
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class UpdateSystem extends JFrame
{

    private AppContext aplicacion = (AppContext) AppContext.getApplicationContext();

    private UpdateSystemPanel updateSystemPanel = null;

    private OKCancelPanel _okCancelPanel = null;

    private JButton closeJButton = null;

    private JPanel closeJPanel = null;

    private OKCancelPanel getOkCancelPanel()
    {
        if (_okCancelPanel == null)
        {
            _okCancelPanel = new OKCancelPanel();
            _okCancelPanel.setBounds(187, 504, 237, 36);
            _okCancelPanel.addActionListener(new java.awt.event.ActionListener()
                {
                    public void actionPerformed(java.awt.event.ActionEvent e)
                    {

                        if (_okCancelPanel.wasOKPressed())
                        {
                            String errorMessage = getUpdateSystemPanel().validateInput();

                            if (errorMessage != null)
                            {
                                JOptionPane
                                        .showMessageDialog(
                                                UpdateSystem.this,
                                                errorMessage,
                                                aplicacion
                                                        .getI18nString("UpdateSystemPanel.ActualizadorGEOPISTA"),
                                                JOptionPane.ERROR_MESSAGE);
                                return;
                            } else
                            {
                                try
                                {
                                    getUpdateSystemPanel().okPressed();
                                } catch (JDOMException e1)
                                {
                                    JOptionPane
                                            .showMessageDialog(
                                                    UpdateSystem.this,
                                                    aplicacion
                                                            .getI18nString("UpdateSystemPanel.ErrorXMLActualizacion"),
                                                    aplicacion
                                                            .getI18nString("UpdateSystemPanel.ActualizadorGEOPISTA"),
                                                    JOptionPane.ERROR_MESSAGE);
                                }
                                catch (MalformedURLException e1)
                                {
                                    JOptionPane
                                            .showMessageDialog(
                                                    UpdateSystem.this,
                                                    aplicacion
                                                            .getI18nString("UpdateSystemPanel.ErrorXMLActualizacion"),
                                                    aplicacion
                                                            .getI18nString("UpdateSystemPanel.ActualizadorGEOPISTA"),
                                                    JOptionPane.ERROR_MESSAGE);
                                }
                                catch (IOException e1)
                                {
                                    JOptionPane
                                            .showMessageDialog(
                                                    UpdateSystem.this,
                                                    aplicacion
                                                            .getI18nString("UpdateSystemPanel.ErrorXMLActualizacion"),
                                                    aplicacion
                                                            .getI18nString("UpdateSystemPanel.ActualizadorGEOPISTA"),
                                                    JOptionPane.ERROR_MESSAGE);
                                } catch (SQLException e1)
                                {
                                    JOptionPane
                                            .showMessageDialog(
                                                    UpdateSystem.this,
                                                    aplicacion
                                                            .getI18nString("UpdateSystemPanel.ErrorejecucionSentenciasSQL"),
                                                    aplicacion
                                                            .getI18nString("UpdateSystemPanel.ActualizadorGEOPISTA"),
                                                    JOptionPane.ERROR_MESSAGE);
                                } catch (ClassNotFoundException e1)
                                {
                                    JOptionPane
                                            .showMessageDialog(
                                                    UpdateSystem.this,
                                                    aplicacion
                                                            .getI18nString("UpdateSystemPanel.DriverbasedatosNoENcontrado"),
                                                    aplicacion
                                                            .getI18nString("UpdateSystemPanel.ActualizadorGEOPISTA"),
                                                    JOptionPane.ERROR_MESSAGE);
                                }

                                // setVisible(false);
                                // System.exit(0);
                                getOkCancelPanel().setVisible(false);
                                getCloseJPanel().setVisible(true);
                                return;
                            }
                        }
                        setVisible(false);
                        System.exit(0);

                    }
                });
        }
        return _okCancelPanel;
    }

    /**
     * This method initializes
     * 
     */
    public UpdateSystem()
        {
            super();
            initialize();
        }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize()
    {
        this.setContentPane(getUpdateSystemPanel());
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.setSize(621, 582);
        this.setTitle(aplicacion
                .getI18nString("UpdateSystemPanel.ActualizadorVersionesGeopista"));
        this.setResizable(false);
        this.getOkCancelPanel().setVisible(true);
        this.getCloseJPanel().setVisible(false);
        this.addWindowListener(new java.awt.event.WindowAdapter()
            {
                public void windowClosing(java.awt.event.WindowEvent e)
                {
                    System.exit(0);
                }
            });
        aplicacion.setMainFrame(this);

    }

    /**
     * This method initializes jButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getCloseJButton()
    {
        if (closeJButton == null)
        {
            closeJButton = new JButton();
            closeJButton.setText(aplicacion.getI18nString("UpdateSystemPanel.Cerrar"));
            closeJButton.addActionListener(new java.awt.event.ActionListener()
                {
                    public void actionPerformed(java.awt.event.ActionEvent e)
                    {
                        System.exit(0);
                    }
                });
        }
        return closeJButton;
    }

    /**
     * This method initializes jPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getCloseJPanel()
    {
        if (closeJPanel == null)
        {
            closeJPanel = new JPanel();
            FlowLayout flowLayout1 = new FlowLayout();
            closeJPanel.setLayout(flowLayout1);
            flowLayout1.setHgap(5);
            flowLayout1.setVgap(2);
            closeJPanel.setBounds(187, 504, 237, 36);
            closeJPanel.add(getCloseJButton(), null);
        }
        return closeJPanel;
    }

    public static void main(String[] args)
    {
        UpdateSystem cfg = new UpdateSystem();
        cfg.setVisible(true);

    }

    private UpdateSystemPanel getUpdateSystemPanel()
    {
        if (updateSystemPanel == null)
        {
            updateSystemPanel = new UpdateSystemPanel();
            updateSystemPanel.add(getOkCancelPanel(), null);
            updateSystemPanel.add(getCloseJPanel(), null);
        }
        return updateSystemPanel;
    }

} // @jve:decl-index=0:visual-constraint="10,10"
