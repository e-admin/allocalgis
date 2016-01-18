package com.geopista.ui.plugin.importer.dialogs;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.Hashtable;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.geopista.feature.Domain;
import com.geopista.ui.plugin.importer.panels.DomainAssociationPanel;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;



public class DomainAssociationDialog extends JDialog
{

	private DomainAssociationPanel domainAssociationPanel = null;
	private OKCancelPanel _okCancelPanel = null;
	private PlugInContext context;
	private Domain domain;
	private String userLocale;

	public static final int DIM_X = 600;
	public static final int DIM_Y = 650;
	

	private OKCancelPanel getOkCancelPanel()
	{
		if (_okCancelPanel == null)
		{
			_okCancelPanel = new OKCancelPanel();
			_okCancelPanel.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{                    
					if (_okCancelPanel.wasOKPressed())
					{
						if(getDomainAssociationPanel().isAllDataRelated())
						{
							String errorMessage = getDomainAssociationPanel().validateInput();

							if (errorMessage != null)
							{
								JOptionPane
								.showMessageDialog(
										DomainAssociationDialog.this,
										errorMessage,
										I18N.get("ImporterPlugIn","ImporterPlugIn.DomainAssociationDialog.mensaje.novalidado"),
										JOptionPane.ERROR_MESSAGE);
								return;
							} else
							{							
								try
								{
									getDomainAssociationPanel().okPressed();
								}
								catch (Exception e1)
								{
									JOptionPane
									.showMessageDialog(
											DomainAssociationDialog.this,
											errorMessage,
											I18N.get("ImporterPlugIn","ImporterPlugIn.DomainAssociationDialog.mensaje.norecuperado"), 
											JOptionPane.ERROR_MESSAGE);
								}
							}
						}
						else
						{
							JOptionPane.showMessageDialog(DomainAssociationDialog.this,
									I18N.get("ImporterPlugIn","ImporterPlugIn.DomainAssociationDialog.mensaje.asociartodos")									
									);
							return;
						}
					}
					
					dispose();                    
				}
			});
		}
		return _okCancelPanel;
	}

	/**
	 * This method initializes
	 * 
	 */    

	public DomainAssociationDialog(PlugInContext context, Domain domain, String locale)
	{
		this.context = context;
		this.domain = domain;
		this.userLocale = locale;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize()
	{		
		this.setModal(true);
		this.setContentPane(getDomainAssociationPanel());
		this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		this.setSize(DIM_X, DIM_Y);
		this.setTitle(I18N.get("ImporterPlugIn","ImporterPlugIn.DomainAssociationDialog.pantalla.titulo"));
		this.setResizable(true);
		this.getOkCancelPanel().setVisible(true);
		GUIUtil.centreOnScreen(this);
		this.addWindowListener(new java.awt.event.WindowAdapter()
		{
			public void windowClosing(java.awt.event.WindowEvent e)
			{
				dispose();
			}
		});
	}

	private DomainAssociationPanel getDomainAssociationPanel()
	{
		if (domainAssociationPanel == null)
		{
			domainAssociationPanel = new DomainAssociationPanel(context, domain, userLocale);
			domainAssociationPanel.add(getOkCancelPanel(), 
					new GridBagConstraints(0, 5, 2, 1, 1, 0,
							GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
							new Insets(0, 5, 0, 5), 0, 0));
		}
		return domainAssociationPanel;
	}

	public Hashtable getListRelations()
	{
		return getDomainAssociationPanel().getHtRelations();
	}

}
