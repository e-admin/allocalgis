/**
 * InsertUpdateCustomStylePanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * InsertUpdateCustomStylePanel.java
 *
 * Created on 28 de julio de 2004, 12:30
 */
package com.geopista.style.sld.ui.impl.panels;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JOptionPane;

import org.deegree.graphics.sld.Rule;
import org.deegree.graphics.sld.Symbolizer;
import org.deegree.graphics.sld.UserStyle;

import com.geopista.app.AppContext;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.IGeopistaLayer;
import com.geopista.style.sld.model.ScaleRange;
import com.geopista.style.sld.ui.impl.RuleTableModel;
import com.geopista.style.sld.ui.impl.SymbolizerRenderer;
import com.geopista.style.sld.ui.impl.UIUtils;

import es.enxenio.util.controller.Action;
import es.enxenio.util.controller.ActionForward;
import es.enxenio.util.controller.FrontController;
import es.enxenio.util.controller.FrontControllerFactory;
import es.enxenio.util.controller.Request;
import es.enxenio.util.controller.Session;
import es.enxenio.util.ui.impl.AbstractPanel;

/**
 *
 * @author  Enxenio S.L.
 */
public class InsertUpdateCustomStylePanel extends AbstractPanel implements ActionForward {
/**/    
    
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	public void configure(Request request) {
		Double zero = new Double(0);
		Double infinity = new Double(9E99);
		Double maxValue = new Double(Double.MAX_VALUE);
		ScaleRange aScaleRange; 
		Session session = FrontControllerFactory.getSession();
		styleNameTxt.setText((String)session.getAttribute("StyleName"));
		_scaleRangeLstModel = new DefaultListModel();
		_scaleRangeLstSelectionModel = new DefaultListSelectionModel();				
		List scaleRangeList = (List)session.getAttribute("ScaleRangeList");
               
		Object o = FrontControllerFactory.getSession().getAttribute("Layer");
		if( o!=null && o instanceof GeopistaLayer)
		{
            //Si no hay lista de rangos de escala, se trata de una inserción de nuevo estilo:
            //Se inicializa el nombre del estilo con el nombre de la capa:
		    if (scaleRangeList.isEmpty())
		    {
		        styleNameTxt.setText("");
		    }
            else
            {
                if(styleNameTxt.getText().startsWith(((IGeopistaLayer)o).getSystemId()+DisplayStylesPanel.STYLE_SEPARATOR))
                {
                    styleNameTxt.setText(styleNameTxt.getText().substring(((IGeopistaLayer)o).getSystemId().length()+DisplayStylesPanel.STYLE_SEPARATOR.length()));
                }
            }		    
		}
        
		Iterator scaleRangeIterator = scaleRangeList.iterator();
		while (scaleRangeIterator.hasNext()) {
			String scaleRangeName;
			aScaleRange = (ScaleRange)scaleRangeIterator.next(); 
			if ((aScaleRange.getMinScale().compareTo(zero) == 0) && ((aScaleRange.getMaxScale().compareTo(infinity) == 0) || (aScaleRange.getMaxScale().compareTo(maxValue) == 0))) {
				scaleRangeName = aplicacion.getI18nString("SLDStyle.siemprevisible"); 
			}
			else if ((aScaleRange.getMinScale().compareTo(zero) != 0) && ((aScaleRange.getMaxScale().compareTo(infinity) == 0) || (aScaleRange.getMaxScale().compareTo(maxValue) == 0))) {
				scaleRangeName = aplicacion.getI18nString("SLDStyle.desde")+" " + aScaleRange.getMinScale();
			}
			else if ((aScaleRange.getMinScale().compareTo(zero) == 0) && !((aScaleRange.getMaxScale().compareTo(infinity) == 0) || (aScaleRange.getMaxScale().compareTo(maxValue) == 0))) {
				scaleRangeName = aplicacion.getI18nString("SLDStyle.hasta")+" " + aScaleRange.getMaxScale();
			}
			else {
				scaleRangeName = aplicacion.getI18nString("SLDStyle.desde")+" " + aScaleRange.getMinScale() + " " +aplicacion.getI18nString("SLDStyle.hasta")+" " + aScaleRange.getMaxScale();
			}
			_scaleRangeLstModel.addElement(scaleRangeName);
		}

		_pointTableModel = new RuleTableModel();
		_lineTableModel = new RuleTableModel();
		_polygonTableModel = new RuleTableModel();
		_textTableModel = new RuleTableModel();

		Integer scaleRangePosition = (Integer)session.getAttribute("ScaleRangePosition");
		if (scaleRangePosition != null)  {
			if (scaleRangeList.size() > scaleRangePosition.intValue()) {
				_scaleRangeLstSelectionModel.setSelectionInterval(scaleRangePosition.intValue(), scaleRangePosition.intValue());
				aScaleRange = (ScaleRange)scaleRangeList.get(scaleRangePosition.intValue());
				Iterator pointListIterator = aScaleRange.getPointList().iterator();		 
				while (pointListIterator.hasNext()) {
					Rule pointRule = (Rule)pointListIterator.next();
					Object[] tableRow = new Object[3];
					//tableRow[0] = UIUtils.createColorFromSymbolizer(pointRule.getSymbolizers()[0]);
					tableRow[0] = pointRule.getSymbolizers()[0];
					tableRow[1] = pointRule.getName();
					tableRow[2] = UIUtils.createStringFromFilter(pointRule.getFilter());
					_pointTableModel.insertRow(0,tableRow);
				}		
			
				Iterator lineListIterator = aScaleRange.getLineList().iterator();		 
				while (lineListIterator.hasNext()) {
					Rule lineRule = (Rule)lineListIterator.next();
					Object[] tableRow = new Object[3];
					//tableRow[0] = UIUtils.createColorFromSymbolizer(lineRule.getSymbolizers()[0]);
					tableRow[0] = lineRule.getSymbolizers()[0];
					tableRow[1] = lineRule.getName();
					tableRow[2] = UIUtils.createStringFromFilter(lineRule.getFilter());
					_lineTableModel.insertRow(0,tableRow);
				}
	
				Iterator polygonListIterator = aScaleRange.getPolygonList().iterator();		 
				while (polygonListIterator.hasNext()) {
					Rule polygonRule = (Rule)polygonListIterator.next();
					Object[] tableRow = new Object[3];
					//tableRow[0] = UIUtils.createColorFromSymbolizer(polygonRule.getSymbolizers()[0]);
					tableRow[0] = polygonRule.getSymbolizers()[0];
					tableRow[1] = polygonRule.getName();
					tableRow[2] = UIUtils.createStringFromFilter(polygonRule.getFilter());
					_polygonTableModel.insertRow(0,tableRow);
				}
				
				Iterator textListIterator = aScaleRange.getTextList().iterator();		 
				while (textListIterator.hasNext()) {
					Rule textRule = (Rule)textListIterator.next();
					Object[] tableRow = new Object[3];
					//tableRow[0] = UIUtils.createColorFromSymbolizer(textRule.getSymbolizers()[0]);
					tableRow[0] = textRule.getSymbolizers()[0];
					tableRow[1] = textRule.getName();
					tableRow[2] = UIUtils.createStringFromFilter(textRule.getFilter());
					_textTableModel.insertRow(0,tableRow);
				}
			}
		}
		else {
			// No hay ScaleRangePosition
			_scaleRangeLstSelectionModel.setSelectionInterval(-1,-1); 
		}
		
		// Es importante que el modelo de la lista se añada al final, 
		// después de seleccionar el elemento de la
		// lista, para evitar que se lance el evento ants de tener 
		// el cuadro de dialogo listo.
		scaleRangeLst.setModel(_scaleRangeLstModel);
		scaleRangeLst.setSelectionModel(_scaleRangeLstSelectionModel);		
		configureScaleRangeButtons();
		int size = 0;
		pointListTable.setModel(_pointTableModel); 
		pointListTable.setDefaultRenderer(Symbolizer.class, new SymbolizerRenderer(true));
		size = pointListTable.getRowCount();
		if (size != 0) {
			setSelectedListIndex(0);
		}		
		pointListTable.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
			public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
				configureRuleButtons();
			}
		});

		lineListTable.setModel(_lineTableModel); 
		lineListTable.setDefaultRenderer(Symbolizer.class, new SymbolizerRenderer(true));
		size = lineListTable.getRowCount();
		if (size != 0) {
			setSelectedListIndex(1);
		}
		lineListTable.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
			public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
				configureRuleButtons();
			}
		});

		polygonListTable.setModel(_polygonTableModel); 
		polygonListTable.setDefaultRenderer(Symbolizer.class, new SymbolizerRenderer(true));
		size = polygonListTable.getRowCount();
		if (size != 0) {
			setSelectedListIndex(2);
		}
		polygonListTable.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
			public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
				configureRuleButtons();
			}
		});

		textListTable.setModel(_textTableModel); 
		textListTable.setDefaultRenderer(Symbolizer.class, new SymbolizerRenderer(true));
		size = textListTable.getRowCount();
		if (size != 0) {
			setSelectedListIndex(3);
		}
		textListTable.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
			public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
				configureRuleButtons();
			}
		});

		String symbolizerType = (String)session.getAttribute("SymbolizerType");
		if (symbolizerType != null) {
			if (symbolizerType.equals("point")) {
				ruleListsPanel.setSelectedIndex(0);
			}
			else if (symbolizerType.equals("line")) {
				ruleListsPanel.setSelectedIndex(1);
			}
			else if (symbolizerType.equals("polygon")) {
				ruleListsPanel.setSelectedIndex(2);
			}
			else if (symbolizerType.equals("text")) {
				ruleListsPanel.setSelectedIndex(3);
			}
		}
		configureRuleButtons();
	}

	public boolean windowClosing() {
		Request theRequest = FrontControllerFactory.createRequest();
		FrontController fc =  FrontControllerFactory.getFrontController();
		Action theAction = fc.getAction("GetBack"); 
		ActionForward theActionForward = theAction.doExecute(theRequest);
		_container.forward(theActionForward, theRequest);
		return false;
	}


	public String getTitle() {
			return aplicacion.getI18nString("UserStyle");		
	}
    
    /** Creates new form InsertUpdateCustomStylePanel */
    public InsertUpdateCustomStylePanel() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        stylePanel = new javax.swing.JPanel();
        styleNameLbl = new javax.swing.JLabel();
        styleNameTxt = new javax.swing.JTextField();
        scaleRangePanel = new javax.swing.JPanel();
        scaleRange = new javax.swing.JPanel();
        scaleRangeScroll = new javax.swing.JScrollPane();
        scaleRangeLst = new javax.swing.JList();
        scaleRangeButtonsPanel = new javax.swing.JPanel();
        newScaleRangeBtn = new javax.swing.JButton();
        removeScaleRangeBtn = new javax.swing.JButton();
        changeScaleRangeBtn = new javax.swing.JButton();
        rulePanel = new javax.swing.JPanel();
        ruleButtonsPanel = new javax.swing.JPanel();
        newRuleBtn = new javax.swing.JButton();
        newThematicRuleBtn = new javax.swing.JButton();
        changeRuleBtn = new javax.swing.JButton();
        removeRuleBtn = new javax.swing.JButton();
        moveUpBtn = new javax.swing.JButton();
        moveDownBtn = new javax.swing.JButton();
        ruleListsPanel = new javax.swing.JTabbedPane();
        pointListPanel = new javax.swing.JPanel();
        pointListTableScroll = new javax.swing.JScrollPane();
        pointListTable = new javax.swing.JTable();
        lineListPanel = new javax.swing.JPanel();
        lineListTableScroll = new javax.swing.JScrollPane();
        lineListTable = new javax.swing.JTable();
        polygonListPanel = new javax.swing.JPanel();
        polygonListTableScroll = new javax.swing.JScrollPane();
        polygonListTable = new javax.swing.JTable();
        textListPanel = new javax.swing.JPanel();
        textListTableScroll = new javax.swing.JScrollPane();
        textListTable = new javax.swing.JTable();
        buttonPanel = new javax.swing.JPanel();
        okBtn = new javax.swing.JButton();
        cancelBtn = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        setPreferredSize(new java.awt.Dimension(450, 400));
        stylePanel.setLayout(new java.awt.GridBagLayout());

        styleNameLbl.setText(aplicacion.getI18nString("SLDStyle.Nombre")+" ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        stylePanel.add(styleNameLbl, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        stylePanel.add(styleNameTxt, gridBagConstraints);

        scaleRangePanel.setLayout(new java.awt.BorderLayout());

        scaleRangePanel.setBorder(new javax.swing.border.TitledBorder(aplicacion.getI18nString("SLDStyle.Rangos")+" "));
        scaleRange.setLayout(new java.awt.GridBagLayout());

        scaleRangeLst.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                scaleRangeLstValueChanged(evt);
            }
        });

        scaleRangeScroll.setViewportView(scaleRangeLst);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        scaleRange.add(scaleRangeScroll, gridBagConstraints);

        scaleRangePanel.add(scaleRange, java.awt.BorderLayout.CENTER);

        scaleRangeButtonsPanel.setLayout(new java.awt.GridBagLayout());

        newScaleRangeBtn.setText(aplicacion.getI18nString("SLDStyle.Nuevo"));
        newScaleRangeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newScaleRangeBtnActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        scaleRangeButtonsPanel.add(newScaleRangeBtn, gridBagConstraints);

        removeScaleRangeBtn.setText(aplicacion.getI18nString("SLDStyle.Borrar"));
        removeScaleRangeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeScaleRangeBtnActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        scaleRangeButtonsPanel.add(removeScaleRangeBtn, gridBagConstraints);

        changeScaleRangeBtn.setText("Modificar");
        changeScaleRangeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeScaleRangeBtnActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        scaleRangeButtonsPanel.add(changeScaleRangeBtn, gridBagConstraints);

        scaleRangePanel.add(scaleRangeButtonsPanel, java.awt.BorderLayout.EAST);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        stylePanel.add(scaleRangePanel, gridBagConstraints);

        rulePanel.setLayout(new java.awt.BorderLayout());

        rulePanel.setBorder(new javax.swing.border.TitledBorder(aplicacion.getI18nString("SLDStyle.ReglasPintado")+" "));
        ruleButtonsPanel.setLayout(new java.awt.GridBagLayout());

        newRuleBtn.setText(aplicacion.getI18nString("SLDStyle.Nuevo"));
        newRuleBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newRuleBtnActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        ruleButtonsPanel.add(newRuleBtn, gridBagConstraints);

        newThematicRuleBtn.setText(aplicacion.getI18nString("SLDStyle.Tematico"));
        newThematicRuleBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newThematicRuleBtnActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        ruleButtonsPanel.add(newThematicRuleBtn, gridBagConstraints);

        changeRuleBtn.setText(aplicacion.getI18nString("SLDStyle.Modificar"));
        changeRuleBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeRuleBtnActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        ruleButtonsPanel.add(changeRuleBtn, gridBagConstraints);

        removeRuleBtn.setText(aplicacion.getI18nString("SLDStyle.Borrar"));
        removeRuleBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeRuleBtnActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        ruleButtonsPanel.add(removeRuleBtn, gridBagConstraints);

        moveUpBtn.setText("^");
        moveUpBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moveUpBtnActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        ruleButtonsPanel.add(moveUpBtn, gridBagConstraints);

        moveDownBtn.setText("V");
        moveDownBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moveDownBtnActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        ruleButtonsPanel.add(moveDownBtn, gridBagConstraints);

        rulePanel.add(ruleButtonsPanel, java.awt.BorderLayout.EAST);

        ruleListsPanel.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                ruleListsPanelStateChanged(evt);
            }
        });

        pointListPanel.setLayout(new java.awt.BorderLayout());

        pointListTableScroll.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        pointListTableScroll.setViewportView(pointListTable);

        pointListPanel.add(pointListTableScroll, java.awt.BorderLayout.CENTER);

        ruleListsPanel.addTab(aplicacion.getI18nString("SLDStyle.Puntos"), pointListPanel);

        lineListPanel.setLayout(new java.awt.BorderLayout());

        lineListTableScroll.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        lineListTableScroll.setViewportView(lineListTable);

        lineListPanel.add(lineListTableScroll, java.awt.BorderLayout.CENTER);

        ruleListsPanel.addTab(aplicacion.getI18nString("SLDStyle.Lineas"), lineListPanel);

        polygonListPanel.setLayout(new java.awt.BorderLayout());

        polygonListTableScroll.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        polygonListTableScroll.setViewportView(polygonListTable);

        polygonListPanel.add(polygonListTableScroll, java.awt.BorderLayout.CENTER);

        ruleListsPanel.addTab(aplicacion.getI18nString("SLDStyle.Poligonos"), polygonListPanel);

        textListPanel.setLayout(new java.awt.BorderLayout());

        textListTableScroll.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        textListTableScroll.setViewportView(textListTable);

        textListPanel.add(textListTableScroll, java.awt.BorderLayout.CENTER);

        ruleListsPanel.addTab(aplicacion.getI18nString("SLDStyle.Textos"), textListPanel);

        rulePanel.add(ruleListsPanel, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        stylePanel.add(rulePanel, gridBagConstraints);

        add(stylePanel, java.awt.BorderLayout.CENTER);

        okBtn.setText(aplicacion.getI18nString("SLDStyle.Aceptar"));
        okBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okBtnActionPerformed(evt);
            }
        });

        buttonPanel.add(okBtn);

        cancelBtn.setText(aplicacion.getI18nString("SLDStyle.Cancelar"));
        cancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtnActionPerformed(evt);
            }
        });

        buttonPanel.add(cancelBtn);

        add(buttonPanel, java.awt.BorderLayout.SOUTH);        
        
        

    }//GEN-END:initComponents

    private void changeScaleRangeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeScaleRangeBtnActionPerformed
		if (scaleRangeLst.getSelectedIndex() != -1) {
			Request theRequest = FrontControllerFactory.createRequest();
			theRequest.setAttribute("Position", new Integer(scaleRangeLst.getSelectedIndex()));
			theRequest.setAttribute("StyleName", styleNameTxt.getText());			
			FrontController fc =  FrontControllerFactory.getFrontController();
			Action theAction = fc.getAction("StartChangeScaleRange");
			ActionForward theActionForward = theAction.doExecute(theRequest);
			_container.forward(theActionForward, theRequest);
		}	
    }//GEN-LAST:event_changeScaleRangeBtnActionPerformed


	private void moveDownBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moveDownBtnActionPerformed
		int position = getSelectedListIndex()[0];
		int size = getSelectedListSize();
		Session session = FrontControllerFactory.getSession();
		Request theRequest = FrontControllerFactory.createRequest();
		int newSelectedIndex = size-position-1;
		switch (ruleListsPanel.getSelectedIndex()) {
			case 0:
				theRequest.setAttribute("SymbolizerType", "point");
				session.setAttribute("PointSelectedIndex",new Integer(newSelectedIndex+1));
				break;
			case 1:
				theRequest.setAttribute("SymbolizerType", "line");
				session.setAttribute("LineSelectedIndex",new Integer(newSelectedIndex+1));
				break;
			case 2:
				theRequest.setAttribute("SymbolizerType", "polygon");
				session.setAttribute("PolygonSelectedIndex",new Integer(newSelectedIndex+1));
				break;
			case 3:
				theRequest.setAttribute("SymbolizerType", "text");
				session.setAttribute("TextSelectedIndex",new Integer(newSelectedIndex+1));
				break;
		}
		// Recuerda que el orden de las reglas en la vista es el inverso al del modelo
		theRequest.setAttribute("InitialPosition", new Integer(position));
		theRequest.setAttribute("FinalPosition", new Integer(position - 1));
		FrontController fc =  FrontControllerFactory.getFrontController();
		Action theAction = fc.getAction("MoveCustomRule"); 
		ActionForward theActionForward = theAction.doExecute(theRequest);
		_container.forward(theActionForward, theRequest);
	}//GEN-LAST:event_moveDownBtnActionPerformed

	private void moveUpBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moveUpBtnActionPerformed
		int position = getSelectedListIndex()[0];
		Session session = FrontControllerFactory.getSession();
		Request theRequest = FrontControllerFactory.createRequest();
		int size = getSelectedListSize();
		int newSelectedIndex = size-position-1;
		switch (ruleListsPanel.getSelectedIndex()) {
			case 0:
				theRequest.setAttribute("SymbolizerType", "point");
				session.setAttribute("PointSelectedIndex",new Integer(newSelectedIndex-1));
				break;
			case 1:
				theRequest.setAttribute("SymbolizerType", "line");
				session.setAttribute("LineSelectedIndex",new Integer(newSelectedIndex-1));
				break;
			case 2:
				theRequest.setAttribute("SymbolizerType", "polygon");
				session.setAttribute("PolygonSelectedIndex",new Integer(newSelectedIndex-1));
				break;
			case 3:
				theRequest.setAttribute("SymbolizerType", "text");
				session.setAttribute("TextSelectedIndex",new Integer(newSelectedIndex-1));
				break;
		}
		// Recuerda que el orden de las reglas en la vista es el inverso al del modelo
		theRequest.setAttribute("InitialPosition", new Integer(position));
		theRequest.setAttribute("FinalPosition", new Integer(position + 1));
		FrontController fc =  FrontControllerFactory.getFrontController();
		Action theAction = fc.getAction("MoveCustomRule"); 
		ActionForward theActionForward = theAction.doExecute(theRequest);
		_container.forward(theActionForward, theRequest);
	}//GEN-LAST:event_moveUpBtnActionPerformed

    private void ruleListsPanelStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_ruleListsPanelStateChanged
		configureRuleButtons();
    }//GEN-LAST:event_ruleListsPanelStateChanged

	private void removeScaleRangeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeScaleRangeBtnActionPerformed
		if (scaleRangeLst.getSelectedIndex() != -1) {
			Request theRequest = FrontControllerFactory.createRequest();
			theRequest.setAttribute("Position", new Integer(scaleRangeLst.getSelectedIndex()));
			theRequest.setAttribute("StyleName", styleNameTxt.getText());			
			FrontController fc =  FrontControllerFactory.getFrontController();
			Action theAction = fc.getAction("DeleteScaleRange");
			ActionForward theActionForward = theAction.doExecute(theRequest);
			_container.forward(theActionForward, theRequest);
		}	
	}//GEN-LAST:event_removeScaleRangeBtnActionPerformed

	private void newScaleRangeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newScaleRangeBtnActionPerformed
		Request theRequest = FrontControllerFactory.createRequest();
		FrontController fc =  FrontControllerFactory.getFrontController();
		theRequest.setAttribute("StyleName", styleNameTxt.getText());
		Action theAction = fc.getAction("InsertScaleRange"); 
		ActionForward theActionForward = theAction.doExecute(theRequest);
		_container.forward(theActionForward, theRequest);
	}//GEN-LAST:event_newScaleRangeBtnActionPerformed


    private void scaleRangeLstValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_scaleRangeLstValueChanged
		if (scaleRangeLst.getSelectedIndex() != -1) {
			Request theRequest = FrontControllerFactory.createRequest();
			FrontController fc =  FrontControllerFactory.getFrontController();
			theRequest.setAttribute("StyleName", styleNameTxt.getText());			
			Action theAction = fc.getAction("SelectScaleRange");
			theRequest.setAttribute("Position", new Integer(scaleRangeLst.getSelectedIndex()));
			ActionForward theActionForward = theAction.doExecute(theRequest);
			_container.forward(theActionForward, theRequest);	
		}
		configureScaleRangeButtons();
    }//GEN-LAST:event_scaleRangeLstValueChanged

    private void removeRuleBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeRuleBtnActionPerformed   
		int[] selectedRows;
		Session session = FrontControllerFactory.getSession();
		selectedRows = getSelectedListIndex();
		if (selectedRows.length == 0) {
			JOptionPane.showMessageDialog(null, aplicacion.getI18nString("SLDStyle.SelecRegla"));			
		}
		else {
			for (int i = 0; i<selectedRows.length;i++) {
				int position = selectedRows[i];
				int size = getSelectedListSize();
				Request theRequest = FrontControllerFactory.createRequest();
				theRequest.setAttribute("StyleName", styleNameTxt.getText());			
				theRequest.setAttribute("Position", new Integer(position));
				Integer selectedIndexAsInteger = null;
				int selectedIndex = 0;
				switch (ruleListsPanel.getSelectedIndex()) {
					case 0:
						theRequest.setAttribute("SymbolizerType", "point");
						selectedIndexAsInteger = (Integer)session.getAttribute("PointSelectedIndex");
						if (selectedIndexAsInteger != null) { 
							selectedIndex = selectedIndexAsInteger.intValue();
							if (selectedIndex != 0) {
								if ((size-position-1) <= selectedIndex) {
									session.setAttribute("PointSelectedIndex",new Integer(selectedIndex-1));
								}
							}
						}
						break;
					case 1:
						theRequest.setAttribute("SymbolizerType", "line");
						selectedIndexAsInteger = (Integer)session.getAttribute("LineSelectedIndex");
						if (selectedIndexAsInteger != null) { 
							selectedIndex = selectedIndexAsInteger.intValue();
							if (selectedIndex != 0) {
								if ((size-position-1) <= selectedIndex) {
									session.setAttribute("LineSelectedIndex",new Integer(selectedIndex-1));
								}
							}
						}
						break;
					case 2:
						theRequest.setAttribute("SymbolizerType", "polygon");
						selectedIndexAsInteger = (Integer)session.getAttribute("PolygonSelectedIndex");
						if (selectedIndexAsInteger != null) {
							selectedIndex = selectedIndexAsInteger.intValue();
							if (selectedIndex != 0) {
								if ((size-position-1) <= selectedIndex) {
									session.setAttribute("PolygonSelectedIndex",new Integer(selectedIndex-1));
								}
							}
						}
						break;
					case 3:
						theRequest.setAttribute("SymbolizerType", "text");
						selectedIndexAsInteger = (Integer)session.getAttribute("TextSelectedIndex");
						if (selectedIndexAsInteger != null) {
							selectedIndex = selectedIndexAsInteger.intValue();
							if (selectedIndex != 0) {
								if ((size-position-1) <= selectedIndex) {
									session.setAttribute("TextSelectedIndex",new Integer(selectedIndex-1));
								}
							}
						}
						break;
				}
				FrontController fc =  FrontControllerFactory.getFrontController();
				Action theAction = fc.getAction("DeleteCustomRule"); 
				ActionForward theActionForward = theAction.doExecute(theRequest);
				_container.forward(theActionForward, theRequest);
			}
		}
    }//GEN-LAST:event_removeRuleBtnActionPerformed

    private void changeRuleBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeRuleBtnActionPerformed
		int[] selectedRows = getSelectedListIndex();  
		Session session = FrontControllerFactory.getSession();  	
		if (selectedRows.length == 0) {
			JOptionPane.showMessageDialog(null, aplicacion.getI18nString("SLDStyle.SelecRegla"));			
		}
		else {
			Request theRequest = FrontControllerFactory.createRequest();
			theRequest.setAttribute("StyleName", styleNameTxt.getText());	
			int position = selectedRows[0];		
			theRequest.setAttribute("Position", new Integer(position));
			int size = getSelectedListSize();
			Integer selectedIndexAsInteger = null;
			int selectedIndex = 0;
			switch (ruleListsPanel.getSelectedIndex()) {
				case 0:
					theRequest.setAttribute("SymbolizerType", "point");
					selectedIndexAsInteger = (Integer)session.getAttribute("PointSelectedIndex");
					if (selectedIndexAsInteger != null) { 
						selectedIndex = selectedIndexAsInteger.intValue();
						if ((size-position-1) != selectedIndex) {
							session.setAttribute("PointSelectedIndex",new Integer(size-position-1));
						}
					}
					break;
				case 1:
					theRequest.setAttribute("SymbolizerType", "line");
					selectedIndexAsInteger = (Integer)session.getAttribute("LineSelectedIndex");
					if (selectedIndexAsInteger != null) {
						selectedIndex = selectedIndexAsInteger.intValue();
						if ((size-position-1) != selectedIndex) {
							session.setAttribute("LineSelectedIndex",new Integer(size-position-1));
						}
					}
					break;
				case 2:
					theRequest.setAttribute("SymbolizerType", "polygon");
					selectedIndexAsInteger = (Integer)session.getAttribute("PolygonSelectedIndex");
					if (selectedIndexAsInteger != null) { 
						selectedIndex = selectedIndexAsInteger.intValue();
						if ((size-position-1) != selectedIndex) {
							session.setAttribute("PolygonSelectedIndex",new Integer(size-position-1));
						}
					}
					break;
				case 3:
					theRequest.setAttribute("SymbolizerType", "text");
					selectedIndexAsInteger = (Integer)session.getAttribute("TextSelectedIndex");
					if (selectedIndexAsInteger != null) {
						selectedIndex = selectedIndexAsInteger.intValue();
						if ((size-position-1) != selectedIndex) {
							session.setAttribute("TextSelectedIndex",new Integer(size-position-1));
						}
					}
				try {
					int[] selectedRow = textListTable.getSelectedRows();		
					int pos=0;
					if (selectedRow.length != 0) {
						for (int i = 0;i<selectedRow.length; i++) {
							pos = selectedRow[i];
							break;
						}						
					}	 					
					Vector v=_textTableModel.getDataVector();
					Vector vectorRule=(Vector)v.get(pos);
					String nombreRegla=(String)vectorRule.get(1);
					if ((nombreRegla.equals("Revision expirada Publicable Textos")) ||
							(nombreRegla.equals("Revision expirada Temporal Textos"))){
						JOptionPane.showMessageDialog(null, "Este estilo es interno y no se puede modificar");			
						return;
					}
				} catch (Exception e) {
				}
					break;
			}
			FrontController fc =  FrontControllerFactory.getFrontController();
			Action theAction = fc.getAction("UpdateCustomRule"); 
			ActionForward theActionForward = theAction.doExecute(theRequest);
			_container.forward(theActionForward, theRequest);
		}
    }//GEN-LAST:event_changeRuleBtnActionPerformed

    private void newThematicRuleBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newThematicRuleBtnActionPerformed
		Request theRequest = FrontControllerFactory.createRequest();
		Session session = FrontControllerFactory.getSession();
		switch (ruleListsPanel.getSelectedIndex()) {
			case 0:
				theRequest.setAttribute("SymbolizerType", "point");
				session.setAttribute("PointSelectedIndex", new Integer(0));
				break;
			case 1:
				theRequest.setAttribute("SymbolizerType", "line");
				session.setAttribute("LinePointSelectedIndex", new Integer(0));
				break;
			case 2:
				theRequest.setAttribute("SymbolizerType", "polygon");
				session.setAttribute("PolygonSelectedIndex", new Integer(0));
				break;
			case 3:
				theRequest.setAttribute("SymbolizerType", "text");
				session.setAttribute("TextSelectedIndex", new Integer(0));
				break;
		}
		FrontController fc =  FrontControllerFactory.getFrontController();
		theRequest.setAttribute("StyleName", styleNameTxt.getText());			
		Action theAction = fc.getAction("InsertThematicRule"); 
		ActionForward theActionForward = theAction.doExecute(theRequest);
		_container.forward(theActionForward, theRequest);
    }//GEN-LAST:event_newThematicRuleBtnActionPerformed

    private void newRuleBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newRuleBtnActionPerformed
		Request theRequest = FrontControllerFactory.createRequest();
		Session session = FrontControllerFactory.getSession();
		switch (ruleListsPanel.getSelectedIndex()) {
			case 0:
				theRequest.setAttribute("SymbolizerType", "point");
				session.setAttribute("PointSelectedIndex", new Integer(0));
				break;
			case 1:
				theRequest.setAttribute("SymbolizerType", "line");
				session.setAttribute("LineSelectedIndex", new Integer(0));
				break;
			case 2:
				theRequest.setAttribute("SymbolizerType", "polygon");
				session.setAttribute("PolygonSelectedIndex", new Integer(0));
				break;
			case 3:
				theRequest.setAttribute("SymbolizerType", "text");
				session.setAttribute("TextSelectedIndex", new Integer(0));
				break;
		}
		
		FrontController fc =  FrontControllerFactory.getFrontController();
        theRequest.setAttribute("StyleName", styleNameTxt.getText());			
		Action theAction = fc.getAction("InsertCustomRule"); 
		ActionForward theActionForward = theAction.doExecute(theRequest);
		_container.forward(theActionForward, theRequest);
    }//GEN-LAST:event_newRuleBtnActionPerformed

    private void okBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okBtnActionPerformed
		if (checkValues()) {
			Request theRequest = FrontControllerFactory.createRequest();
            
            String styleName = styleNameTxt.getText();
            
            Object o = FrontControllerFactory.getSession().getAttribute("Layer");
            if(o!=null && o instanceof GeopistaLayer)
            {
                if (!styleName.startsWith(((IGeopistaLayer)o).getSystemId()+DisplayStylesPanel.STYLE_SEPARATOR))
                {
                    styleName = ((IGeopistaLayer)o).getSystemId()+DisplayStylesPanel.STYLE_SEPARATOR+ styleName;
                }                  
            }
                
			theRequest.setAttribute("StyleName", styleName);
			FrontController fc =  FrontControllerFactory.getFrontController();
			Action theAction = fc.getAction("CreateCustomUserStyle"); 
			ActionForward theActionForward = theAction.doExecute(theRequest);
			_container.forward(theActionForward, theRequest);
		}
    }//GEN-LAST:event_okBtnActionPerformed

	private boolean checkValues() {
		
		boolean valuesAreCorrect = true;
		StringBuffer errorMessage = new StringBuffer();
		
		Session session = FrontControllerFactory.getSession();
		List scaleRangeList = (List)session.getAttribute("ScaleRangeList");
       
		List inserts = (List)session.getAttribute("Insert");
		Integer insert = (Integer)inserts.get(0);
		
        if (styleNameTxt.getText().trim().equals(""))
        {
            errorMessage.append(aplicacion.getI18nString("SLDStyle.NecesarioNombre"));
            valuesAreCorrect = false;
        }
        if (insert.intValue() == 1) {
			ArrayList userStyleList = (ArrayList)session.getAttribute("UserStyleList");
			Iterator userStyleListIterator = userStyleList.iterator();
			while (userStyleListIterator.hasNext()) {
				UserStyle userStyle = (UserStyle)userStyleListIterator.next();
				if (styleNameTxt.getText().equals(userStyle.getName())) {
					errorMessage.append(aplicacion.getI18nString("SLDStyle.YaExiste"));
					valuesAreCorrect = false;
				}
			}
		}
		if (scaleRangeList.isEmpty()) {
			errorMessage.append(aplicacion.getI18nString("SLDStyle.DefEscala"));
			valuesAreCorrect = false;
		}
		else {
			Iterator scaleRangeIterator = scaleRangeList.iterator();
			while (scaleRangeIterator.hasNext()) {
				ScaleRange scaleRange = (ScaleRange)scaleRangeIterator.next();
				if ((scaleRange.getLineList().isEmpty())&&(scaleRange.getPointList().isEmpty())&&
					(scaleRange.getPolygonList().isEmpty())&&(scaleRange.getTextList().isEmpty())) {
					errorMessage.append(aplicacion.getI18nString("SLDStyle.NecesariaRegla"));
					valuesAreCorrect = false;							
				}
			}
		}
        
		if (!valuesAreCorrect) {
			JOptionPane.showMessageDialog(null, aplicacion.getI18nString("SLDStyle.ValoresIncorrectos") + errorMessage.toString());
		}
		return valuesAreCorrect;
	}

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
		Request theRequest = FrontControllerFactory.createRequest();
		FrontController fc =  FrontControllerFactory.getFrontController();
		Action theAction = fc.getAction("GetBack"); 
		ActionForward theActionForward = theAction.doExecute(theRequest);
		_container.forward(theActionForward, theRequest);
    }//GEN-LAST:event_cancelBtnActionPerformed
		
	private void configureScaleRangeButtons() {
		if (scaleRangeLst.getSelectedIndex() == -1) {
			removeScaleRangeBtn.setEnabled(false);
		}
		else {
			removeScaleRangeBtn.setEnabled(true);
		}
	}
	
	private void configureRuleButtons() {
		if (scaleRangeLst.getSelectedIndex() == -1) {
			newRuleBtn.setEnabled(false);
			newThematicRuleBtn.setEnabled(false);
			changeRuleBtn.setEnabled(false);
			removeRuleBtn.setEnabled(false);
			moveUpBtn.setEnabled(false);
			moveDownBtn.setEnabled(false);
		} 
		else {
			int[] selectedRow = getSelectedListIndex();
			int size = getSelectedListSize();
			if (selectedRow.length == 0) {
				newRuleBtn.setEnabled(true);
				newThematicRuleBtn.setEnabled(true);
				changeRuleBtn.setEnabled(false);
				removeRuleBtn.setEnabled(false);
				moveUpBtn.setEnabled(false);
				moveDownBtn.setEnabled(false);
			}
			else {
				newRuleBtn.setEnabled(true);
				newThematicRuleBtn.setEnabled(true);
				changeRuleBtn.setEnabled(true);
				removeRuleBtn.setEnabled(true);
				// Recuerda que el orden de las reglas en la vista es el inverso al del modelo
				if (selectedRow.length == 1) {
					moveUpBtn.setEnabled(true);
					moveDownBtn.setEnabled(true);
					int pos = selectedRow[0];
					if (pos == size - 1) {
						moveUpBtn.setEnabled(false);
					}
					if (pos == 0) {
						moveDownBtn.setEnabled(false);
					}
				}
				else {
					moveUpBtn.setEnabled(false);
					moveDownBtn.setEnabled(false);					
				}
			}
		}
	}

	private void setSelectedListIndex(int list) {
		Session session = FrontControllerFactory.getSession();
		Integer selectedIndex = null;
		switch (list) {
			case 0:
				selectedIndex = (Integer)session.getAttribute("PointSelectedIndex");
				if (selectedIndex != null) {
					pointListTable.setRowSelectionInterval(selectedIndex.intValue(),selectedIndex.intValue());
				}
				break;
			case 1:
				selectedIndex = (Integer)session.getAttribute("LineSelectedIndex");
				if (selectedIndex != null) {
					lineListTable.setRowSelectionInterval(selectedIndex.intValue(),selectedIndex.intValue());
				}
				break;
			case 2:
				selectedIndex = (Integer)session.getAttribute("PolygonSelectedIndex");
				if (selectedIndex != null) {
					polygonListTable.setRowSelectionInterval(selectedIndex.intValue(),selectedIndex.intValue());
				}
				break;
			case 3:
				selectedIndex = (Integer)session.getAttribute("TextSelectedIndex");
				if (selectedIndex != null) {
					textListTable.setRowSelectionInterval(selectedIndex.intValue(),selectedIndex.intValue());
				}
				break;
		}
	}

	private int[] getSelectedListIndex() {
		int[] selectedRow;
		int size;
		
		switch (ruleListsPanel.getSelectedIndex()) {
			case 0:
				selectedRow = pointListTable.getSelectedRows();
				size = pointListTable.getRowCount();
				break;
			case 1:
				selectedRow = lineListTable.getSelectedRows();
				size = lineListTable.getRowCount();
				break;
			case 2:
				selectedRow = polygonListTable.getSelectedRows();
				size = polygonListTable.getRowCount();
				break;
			case 3:
				selectedRow = textListTable.getSelectedRows();
				size = textListTable.getRowCount();
				break;
			default:
				selectedRow = new int[] {};
				size = -1;
				break;
		}
		if (selectedRow.length != 0) {
			for (int i = 0;i<selectedRow.length; i++) {
				int pos = selectedRow[i];
				selectedRow[i] = size - pos - 1; 	
			}
			return selectedRow;
		}	 
		else
			return selectedRow;
	}
	
	private int getSelectedListSize() {
		int size;

		switch (ruleListsPanel.getSelectedIndex()) {
			case 0:
				size = pointListTable.getRowCount();
				break;
			case 1:
				size = lineListTable.getRowCount();
				break;
			case 2:
				size = polygonListTable.getRowCount();
				break;
			case 3:
				size = textListTable.getRowCount();
				break;
			default:
				size = -1;
				break;
		}
		return size;
	}
	
	private DefaultListModel _scaleRangeLstModel;
	private DefaultListSelectionModel _scaleRangeLstSelectionModel;
	private RuleTableModel _pointTableModel;
	private RuleTableModel _polygonTableModel;
	private RuleTableModel _lineTableModel;
	private RuleTableModel _textTableModel;
	private Integer selectedIndex;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton cancelBtn;
    private javax.swing.JButton changeRuleBtn;
    private javax.swing.JButton changeScaleRangeBtn;
    private javax.swing.JPanel lineListPanel;
    private javax.swing.JTable lineListTable;
    private javax.swing.JScrollPane lineListTableScroll;
    private javax.swing.JButton moveDownBtn;
    private javax.swing.JButton moveUpBtn;
    private javax.swing.JButton newRuleBtn;
    private javax.swing.JButton newScaleRangeBtn;
    private javax.swing.JButton newThematicRuleBtn;
    private javax.swing.JButton okBtn;
    private javax.swing.JPanel pointListPanel;
    private javax.swing.JTable pointListTable;
    private javax.swing.JScrollPane pointListTableScroll;
    private javax.swing.JPanel polygonListPanel;
    private javax.swing.JTable polygonListTable;
    private javax.swing.JScrollPane polygonListTableScroll;
    private javax.swing.JButton removeRuleBtn;
    private javax.swing.JButton removeScaleRangeBtn;
    private javax.swing.JPanel ruleButtonsPanel;
    private javax.swing.JTabbedPane ruleListsPanel;
    private javax.swing.JPanel rulePanel;
    private javax.swing.JPanel scaleRange;
    private javax.swing.JPanel scaleRangeButtonsPanel;
    private javax.swing.JList scaleRangeLst;
    private javax.swing.JPanel scaleRangePanel;
    private javax.swing.JScrollPane scaleRangeScroll;
    private javax.swing.JLabel styleNameLbl;
    private javax.swing.JTextField styleNameTxt;
    private javax.swing.JPanel stylePanel;
    private javax.swing.JPanel textListPanel;
    private javax.swing.JTable textListTable;
    private javax.swing.JScrollPane textListTableScroll;
    // End of variables declaration//GEN-END:variables
    
}
