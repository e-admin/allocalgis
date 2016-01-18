/*
 * Package reso.JumpPlugIn.PrintLayoutPlugIn.PrintLayoutInterface.Elements.Options pour JUMP
 *
 * Copyright (C) 2004
 * Olivier Bedel, ingénieur informaticien laboratoire Reso UMR ESO 6590, Bassin versant du Jaudy-Guindy-Bizien.
 * Céline Foureau, stagiaire MIAGE, laboratoire Reso UMR ESO 6590.
 * Erwan Bocher, doctorant en géographie, laboratoire Reso UMR ESO 6590, Bassin versant du Jaudy-Guindy-Bizien
 *
 * Date de création : 8 sept. 2004
 *
 * Développé dans le cadre du Projet APARAD 
 *  (Laboratoire Reso UMR ESO 6590 CNRS / Bassin Versant du Jaudy-Guindy-Bizien)
 *    Responsable : Erwan BOCHER
 *    Développeurs : Céline FOUREAU, Olivier BEDEL
 *
 * olivier.bedel@uhb.fr ou olivier.bedel@yahoo.fr
 * erwan.bocher@uhb.fr ou erwan.bocher@free.fr
 * celine.foureau@uhb.fr ou celine.foureau@wanadoo.fr
 * 
 * Ce package hérite de la licence GPL de JUMP. Il est régi par la licence CeCILL soumise au droit français et
 * respectant les principes de diffusion des logiciels libres. (http://www.cecill.info)
 * 
 */

package reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.graphicText;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;

import reso.jumpPlugIn.printLayoutPlugIn.I18NPlug;
import reso.jumpPlugIn.printLayoutPlugIn.PrintLayoutPlugIn;

import com.geopista.util.UtilsPrintPlugin;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;

/**
 * @author FOUREAU_C
 */
public class TextStyleChooserDialog extends JDialog {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3546643200656945977L;

	private GraphicText text;

	private JPanel textoEjemploPanel = new JPanel();
	private JLabel textoEjemploLabel = new JLabel();
	
	//Indica estilos marcados en checks: subrayado y mayus
	private boolean isUnderline = false;
	private boolean isUppercase = false;
	
	private String currentText=null;
	private TextPanel textPanel = new TextPanel();
	private FontPanel fontPanel = new FontPanel();
	private BorderPanel borderPanel = new BorderPanel();
	private TramePanel tramePanel = new TramePanel();

	private OKCancelPanel okCancelPanel = new OKCancelPanel();

	public TextStyleChooserDialog(GraphicText gt) {
		this.text = gt;

		setName(I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.options.TextStyleChooserDialog.Name"));
		setTitle(I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.options.TextStyleChooserDialog.Name"));
		//setIconImage(IconLoader.icon("app-icon.gif").getImage());

		init(gt);

		okCancelPanel.setPreferredSize(new Dimension(400, 30));
		okCancelPanel.addActionListener(new OkCancelActionListener());

		getContentPane().setLayout(new GridBagLayout());
		getContentPane().add(
				textPanel,
				new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.REMAINDER,
						new Insets(2, 2, 2, 2), 0, 0));
		getContentPane().add(
				fontPanel,
				new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.REMAINDER,
						new Insets(2, 2, 2, 2), 0, 0));
		getContentPane().add(
				borderPanel,
				new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.REMAINDER,
						new Insets(2, 2, 2, 2), 0, 0));
		getContentPane().add(
				tramePanel,
				new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.REMAINDER,
						new Insets(2, 2, 2, 2), 0, 0));
		getContentPane().add(
				textoEjemploPanel,
				new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.REMAINDER,
						new Insets(2, 2, 2, 2), 0, 0));
		getContentPane().add(
				okCancelPanel,
				new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.NONE,
						new Insets(2, 2, 2, 2), 0, 0));
		pack();
		setModal(true);
		setVisible(true);
	}

	public JLabel getTextoEjemploLabel() {
		return textoEjemploLabel;
	}

	private class OkCancelActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (okCancelPanel.wasOKPressed()) {
				text.setUnderline(isUnderline);
				text.setUpperCase(isUppercase);
				//Obtener texto real establecido por el usuario
				text.setText(currentText);
				//Fijar estilos establecidos obteniendo datos del elemento de ejemplo
				text.setVerticalAlignment(textoEjemploLabel.getVerticalAlignment());
				text.setHorizontalAlignment(textoEjemploLabel.getHorizontalAlignment());
				text.setFont(textoEjemploLabel.getFont());
				text.setFontColor(textoEjemploLabel.getForeground());
				text.setBorder(textoEjemploLabel.getBorder());
				text.setOpaque(textoEjemploLabel.isOpaque());
				text.setBackgroundColor(textoEjemploLabel.getBackground());
			}
			termine();
		}
	}

	private void termine() {
		dispose();
	}

	private void init(GraphicText gt) {
		isUppercase = gt.getUpperCase();
		isUnderline = gt.getUnderline();
		
		textoEjemploLabel.setText(gt.getText());
		
		//Panel con texto parametrizado: este SIEMPRE sin formato subrayado
		textPanel.yourTextArea.setText(UtilsPrintPlugin.formatTextStyleUnderline(gt.getText(), false));

		//initialisation de la position du texte
		switch (gt.getVerticalAlignment()) {
		case SwingConstants.TOP:
			switch (gt.getHorizontalAlignment()) {
			case SwingConstants.LEFT:
				textPanel.positionPanel.topLeft.setState(true);
				break;
			case SwingConstants.CENTER:
				textPanel.positionPanel.topCenter.setState(true);
				break;
			case SwingConstants.RIGHT:
				textPanel.positionPanel.topRight.setState(true);
				break;
			}
			break;
		case SwingConstants.CENTER:
			switch (gt.getHorizontalAlignment()) {
			case SwingConstants.LEFT:
				textPanel.positionPanel.centerLeft.setState(true);
				break;
			case SwingConstants.CENTER:
				textPanel.positionPanel.centerCenter.setState(true);
				break;
			case SwingConstants.RIGHT:
				textPanel.positionPanel.centerRight.setState(true);
				break;
			}
			break;
		case SwingConstants.BOTTOM:
			switch (gt.getHorizontalAlignment()) {
			case SwingConstants.LEFT:
				textPanel.positionPanel.bottomLeft.setState(true);
				break;
			case SwingConstants.CENTER:
				textPanel.positionPanel.bottomCenter.setState(true);
				break;
			case SwingConstants.RIGHT:
				textPanel.positionPanel.bottomRight.setState(true);
				break;
			}
			break;
		}
		textoEjemploLabel.setVerticalAlignment(gt.getVerticalAlignment());
		textoEjemploLabel.setHorizontalAlignment(gt.getHorizontalAlignment());

		//initialisation de la police
		fontPanel.fontNameComboBox.setSelectedItem(gt.getFont().getFamily());
		fontPanel.fontSizeTextField.setText(new Integer(gt.getFont().getSize()).toString());
		fontPanel.color.setBackground(gt.getFontColor());
		fontPanel.boldCheckBox.setSelected(gt.getFont().isBold());
		fontPanel.italicCheckBox.setSelected(gt.getFont().isItalic());
		fontPanel.underlineCheckBox.setSelected(gt.getUnderline());
		fontPanel.upperCaseCheckBox.setSelected(gt.getUpperCase());

		textoEjemploLabel.setFont(new Font(gt.getFont().getName(), gt.getFont().getStyle(), gt.getFont().getSize()));

		textoEjemploLabel.setForeground(fontPanel.color.getBackground());

		//initialisation de la bordure
		if (gt.getBorder() instanceof LineBorder) {
		    borderPanel.yesCheckBox.setState(true);
		    borderPanel.setEnabled(true);
			borderPanel.color.setBackground(((LineBorder) gt.getBorder()).getLineColor());
			switch (((LineBorder) gt.getBorder()).getThickness()) {
			case 2:
				borderPanel.twoCheckBox.setState(true);
				break;
			case 3:
				borderPanel.threeCheckBox.setState(true);
				break;
			case 4:
				borderPanel.fourCheckBox.setState(true);
				break;
			case 5:
				borderPanel.fiveCheckBox.setState(true);
				break;
			case 6:
				borderPanel.sixCheckBox.setState(true);
				break;
			default:
				borderPanel.oneCheckBox.setState(true);
				break;
			}
		}else if (gt.getBorder() == null){
		    borderPanel.noCheckBox.setState(true);
		    borderPanel.setEnabled(false);
		}
		textoEjemploLabel.setBorder(gt.getBorder());

		//initialisation de la trame de fond
		if (gt.getOpaque()) {
			tramePanel.opaque.setState(true);
			tramePanel.tramePanel.setEnabled(true);
			textoEjemploLabel.setOpaque(gt.getOpaque());
			tramePanel.tramePanel.color.setBackground(gt.getBackgroundColor());
			textoEjemploLabel.setBackground(tramePanel.tramePanel.color.getBackground());
		} else {
			tramePanel.transparent.setState(true);
			tramePanel.tramePanel.setEnabled(false);
			textoEjemploLabel.setOpaque(gt.getOpaque());
		}

		textoEjemploPanel
				.setBorder(BorderFactory
						.createTitledBorder(I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.options.TextStyleChooserDialog.Preview")));
		textoEjemploLabel.setPreferredSize(new Dimension(400, 40));
		textoEjemploPanel.setLayout(new GridBagLayout());
		textoEjemploPanel.add(textoEjemploLabel, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.REMAINDER, new Insets(0, 0, 0, 0), 0, 0));

	}

	private class TextPanel extends JPanel {
		private JPanel textPanel = new JPanel();

		private JPanel yourText = new JPanel();

		private JTextArea yourTextArea = new JTextArea();
        
        private JScrollPane jScroll = new JScrollPane();

		private PositionPanel positionPanel = new PositionPanel();

		public TextPanel() {
			setBorder(BorderFactory
					.createTitledBorder(I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.options.TextStyleChooserDialog.TextPanel.Name")));

			yourTextArea.append(textoEjemploLabel.getText());
			yourTextArea.addFocusListener(new FocusListener() {
				public void focusGained(FocusEvent e) {
				}

				public void focusLost(FocusEvent e) {
					textoEjemploLabel.setText(yourTextArea.getText()); 
					currentText=yourTextArea.getText();
				}
			});

			/*yourText
					.setBorder(BorderFactory
							.createTitledBorder(I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.options.TextStyleChooserDialog.TextPanel.YourText")));*/
			yourText.setPreferredSize(new Dimension(320, 70));
			yourText.setLayout(new BorderLayout());
            jScroll.add(yourTextArea);
            jScroll.setViewportView(yourTextArea);
            
			yourText.add(jScroll, BorderLayout.CENTER);
            
			textPanel.setPreferredSize(new Dimension(400, 75));
			textPanel.setSize(textPanel.getPreferredSize());

			textPanel.setLayout(new GridBagLayout());
			textPanel
					.add(yourText, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
							GridBagConstraints.NORTHWEST,
							GridBagConstraints.REMAINDER,
							new Insets(2, 2, 2, 2), 0, 0));
			textPanel
					.add(positionPanel, new GridBagConstraints(2, 1, 1, 1, 0.0,
							0.0, GridBagConstraints.CENTER,
							GridBagConstraints.REMAINDER,
							new Insets(0, 0, 0, 0), 0, 0));
			setLayout(new GridBagLayout());
			add(textPanel, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
					GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
					new Insets(0, 0, 0, 0), 0, 0));
		}

		private class PositionPanel extends JPanel {
			private JPanel positionPanel = new JPanel();

			private CheckboxGroup alignment = new CheckboxGroup();

			private Checkbox topLeft = new Checkbox("", alignment, false);

			private Checkbox topCenter = new Checkbox("", alignment, false);

			private Checkbox topRight = new Checkbox("", alignment, false);

			private Checkbox centerLeft = new Checkbox("", alignment, false);

			private Checkbox centerCenter = new Checkbox("", alignment, true);

			private Checkbox centerRight = new Checkbox("", alignment, false);

			private Checkbox bottomLeft = new Checkbox("", alignment, false);

			private Checkbox bottomCenter = new Checkbox("", alignment, false);

			private Checkbox bottomRight = new Checkbox("", alignment, false);

			public PositionPanel() {
				setBorder(BorderFactory
						.createTitledBorder(I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.options.TextStyleChooserDialog.PositionPanel.Name")));

				positionPanel.setPreferredSize(new Dimension(60, 45));
				positionPanel.setSize(getPreferredSize());

				positionPanel.setLayout(new GridLayout(3, 3));
				positionPanel.add(topLeft);
				topLeft.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						textoEjemploLabel.setVerticalAlignment(SwingConstants.TOP);
						textoEjemploLabel.setHorizontalAlignment(SwingConstants.LEFT);
					}
				});
				positionPanel.add(topCenter);
				topCenter.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						textoEjemploLabel.setVerticalAlignment(SwingConstants.TOP);
						textoEjemploLabel
								.setHorizontalAlignment(SwingConstants.CENTER);
					}
				});
				positionPanel.add(topRight);
				topRight.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						textoEjemploLabel.setVerticalAlignment(SwingConstants.TOP);
						textoEjemploLabel
								.setHorizontalAlignment(SwingConstants.RIGHT);
					}
				});

				positionPanel.add(centerLeft);
				centerLeft.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						textoEjemploLabel.setVerticalAlignment(SwingConstants.CENTER);
						textoEjemploLabel.setHorizontalAlignment(SwingConstants.LEFT);
					}
				});
				positionPanel.add(centerCenter);
				centerCenter.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						textoEjemploLabel.setVerticalAlignment(SwingConstants.CENTER);
						textoEjemploLabel
								.setHorizontalAlignment(SwingConstants.CENTER);
					}
				});
				positionPanel.add(centerRight);
				centerRight.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						textoEjemploLabel.setVerticalAlignment(SwingConstants.CENTER);
						textoEjemploLabel
								.setHorizontalAlignment(SwingConstants.RIGHT);
					}
				});

				positionPanel.add(bottomLeft);
				bottomLeft.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						textoEjemploLabel.setVerticalAlignment(SwingConstants.BOTTOM);
						textoEjemploLabel.setHorizontalAlignment(SwingConstants.LEFT);
					}
				});
				positionPanel.add(bottomCenter);
				bottomCenter.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						textoEjemploLabel.setVerticalAlignment(SwingConstants.BOTTOM);
						textoEjemploLabel
								.setHorizontalAlignment(SwingConstants.CENTER);
					}
				});
				positionPanel.add(bottomRight);
				bottomRight.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						textoEjemploLabel.setVerticalAlignment(SwingConstants.BOTTOM);
						textoEjemploLabel
								.setHorizontalAlignment(SwingConstants.RIGHT);
					}
				});

				setLayout(new GridBagLayout());
				add(positionPanel, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.NONE,
						new Insets(0, 0, 0, 0), 0, 0));
			}
		}
	}

	private class FontPanel extends JPanel {
		private JPanel fontPanel = new JPanel();

		private JLabel name = new JLabel(
				I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.options.TextStyleChooserDialog.FontPanel.FontName"));

		private JLabel size = new JLabel(
				I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.options.TextStyleChooserDialog.FontPanel.FontSize"));

		private JPanel color = new JPanel();

		private JLabel fontColor = new JLabel(
				I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.options.TextStyleChooserDialog.FontPanel.FontColor"));

		private ColorButton fontColorButton = new ColorButton(
				"/reso/jumpPlugIn/printLayoutPlugIn/images/ColorPalette.gif",
				I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.options.TextStyleChooserDialog.FontPanel.ColorPalette"));;

		private JPanel fontStyle = new JPanel();

		private JComboBox fontNameComboBox = new JComboBox();

		private JTextField fontSizeTextField = new JTextField(2);

		private JCheckBox boldCheckBox = new JCheckBox(
				I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.options.TextStyleChooserDialog.FontPanel.Bold"));

		private JCheckBox underlineCheckBox = new JCheckBox(
				UtilsPrintPlugin.formatTextStyleUnderline(
						I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.options.TextStyleChooserDialog.FontPanel.Underline"), 
						true));

		private JCheckBox italicCheckBox = new JCheckBox(
				I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.options.TextStyleChooserDialog.FontPanel.Italic"));

		private JCheckBox upperCaseCheckBox = new JCheckBox(
				I18NPlug.get(PrintLayoutPlugIn.name,
								"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.options.TextStyleChooserDialog.FontPanel.UpperCase")
						.toUpperCase());

		public FontPanel() {
			initFontNameComboBox();
			initFontSize();
			initFontStyle();
			initFontColor();

			setBorder(BorderFactory
					.createTitledBorder(I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.options.TextStyleChooserDialog.FontPanel.Name")));

			fontPanel.setPreferredSize(new Dimension(400, 125));

			fontPanel.setLayout(new GridBagLayout());
			fontPanel.add(name, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
					GridBagConstraints.WEST, GridBagConstraints.NONE,
					new Insets(0, 0, 0, 0), 0, 0));
			fontPanel.add(fontNameComboBox, new GridBagConstraints(2, 1, 2, 1,
					0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
					new Insets(0, 0, 0, 0), 0, 0));
			fontPanel.add(size, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
					GridBagConstraints.WEST, GridBagConstraints.NONE,
					new Insets(0, 0, 0, 0), 0, 0));
			fontPanel.add(fontSizeTextField, new GridBagConstraints(2, 2, 2, 1,
					0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
					new Insets(0, 0, 0, 0), 0, 0));
			fontPanel.add(fontColor, new GridBagConstraints(1, 3, 1, 1, 0.0,
					0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
					new Insets(0, 0, 0, 0), 0, 0));
			fontPanel.add(color, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0,
					GridBagConstraints.WEST, GridBagConstraints.NONE,
					new Insets(0, 0, 0, 0), 0, 0));
			fontPanel.add(fontColorButton, new GridBagConstraints(3, 3, 1, 1,
					0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			fontPanel.add(fontStyle, new GridBagConstraints(4, 1, 1, 3, 0.0,
					0.0, GridBagConstraints.EAST, GridBagConstraints.REMAINDER,
					new Insets(0, 0, 0, 0), 0, 0));

			setLayout(new GridBagLayout());
			add(fontPanel, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.REMAINDER,
					new Insets(0, 0, 0, 0), 0, 0));
		}

		private void initFontNameComboBox() {
			int i;
			String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
			for (i = 0; i < fonts.length; i++) {
				fontNameComboBox.addItem(fonts[i]);
			}
			fontNameComboBox.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					Font fuente = textoEjemploLabel.getFont();
					textoEjemploLabel.setFont(new Font(fontNameComboBox.getSelectedItem().toString(), fuente.getStyle(), fuente.getSize()));
				}
			});
		}

		private void initFontSize() {
			fontSizeTextField.setText(Integer.toString(textoEjemploLabel.getFont().getSize()));
			fontSizeTextField.addFocusListener(new FocusListener() {
				public void focusGained(FocusEvent e) {
				}

				public void focusLost(FocusEvent e) {
					Font fuente = textoEjemploLabel.getFont();
					textoEjemploLabel.setFont(new Font(fuente.getName(), fuente.getStyle(), Integer.valueOf(fontSizeTextField.getText()).intValue()));
				}
			});
		}

		private void styleItemStateChanged () {
			//Obtener datos fuente actual
			Font fuente = textoEjemploLabel.getFont(); 
			
			int style = Font.PLAIN;
			//Sobre texto plano establecer estilos marcados
			if (boldCheckBox.isSelected() && italicCheckBox.isSelected())
				style = Font.BOLD + Font.ITALIC;
			else if (boldCheckBox.isSelected())
				style = Font.BOLD;
			else if (italicCheckBox.isSelected())
				style = Font.ITALIC;
			textoEjemploLabel.setFont(new Font(fuente.getName(), style, fuente.getSize()));
			
			//Mayusculas
			currentText = textPanel.yourTextArea.getText();
			String texto = currentText;
			isUppercase = upperCaseCheckBox.isSelected();
			if (isUppercase) 
				texto = texto.toUpperCase();
			textoEjemploLabel.setText(texto);
			
			//Subrayado
			isUnderline = underlineCheckBox.isSelected();
			textoEjemploLabel.setText(UtilsPrintPlugin.formatTextStyleUnderline(texto, underlineCheckBox.isSelected()));
		}
		
		private void initFontStyle() {
			boldCheckBox.setFont(new Font(boldCheckBox.getFont().getName(), Font.BOLD, boldCheckBox.getFont().getSize()));
			italicCheckBox.setFont(new Font(italicCheckBox.getFont().getName(), Font.ITALIC, italicCheckBox.getFont().getSize()));
			fontStyle.setBorder(BorderFactory .createTitledBorder(I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.options.TextStyleChooserDialog.FontPanel.FontStyle")));
			fontStyle.setLayout(new GridLayout(4, 1));
			fontStyle.add(boldCheckBox);
			boldCheckBox.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					styleItemStateChanged();
				}
			});
			fontStyle.add(underlineCheckBox);
			underlineCheckBox.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					styleItemStateChanged();
				}
			});
			fontStyle.add(italicCheckBox);
			italicCheckBox.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					styleItemStateChanged();
				}
			});
			fontStyle.add(upperCaseCheckBox);
			upperCaseCheckBox.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					styleItemStateChanged();
				}
			});
		}

		private void initFontColor() {
			color.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
			color.setOpaque(true);
			color.setPreferredSize(new Dimension(160, 20));
			color.setBackground(textoEjemploPanel.getForeground());
			color.addPropertyChangeListener(new PropertyChangeListener() {
				public void propertyChange(PropertyChangeEvent evt) {
					textoEjemploLabel.setForeground(color.getBackground());
				}
			});
			fontColorButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					new ColorChooserDialog("Couleur de la police", color);
				}
			});
		}
	}

	private class BorderPanel extends JPanel {
		private JPanel borderPanel = new JPanel();
		
		private JPanel choisePanel = new JPanel();
		
		private CheckboxGroup borderChoise = new CheckboxGroup();

		private Checkbox yesCheckBox = new Checkbox(
				I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.options.TextStyleChooserDialog.BorderPanel.Yes"),
						borderChoise, false);

		private Checkbox noCheckBox = new Checkbox(
				I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.options.TextStyleChooserDialog.BorderPanel.No"),
						borderChoise, true);
		

		private JLabel borderColor = new JLabel(
				I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.options.TextStyleChooserDialog.BorderPanel.Color"));

		private JPanel color = new JPanel();

		private ColorButton borderColorButton = new ColorButton(
				"/reso/jumpPlugIn/printLayoutPlugIn/images/ColorPalette.gif",
				I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.options.TextStyleChooserDialog.BorderPanel.ColorPalette"));

		private JPanel borderThicknessPanel = new JPanel();

		private CheckboxGroup borderThickness = new CheckboxGroup();

		private Checkbox oneCheckBox = new Checkbox(
				I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.options.TextStyleChooserDialog.BorderPanel.One"),
				borderThickness, true);

		private Checkbox twoCheckBox = new Checkbox(
				I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.options.TextStyleChooserDialog.BorderPanel.Two"),
				borderThickness, false);

		private Checkbox threeCheckBox = new Checkbox(
				I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.options.TextStyleChooserDialog.BorderPanel.Three"),
				borderThickness, false);

		private Checkbox fourCheckBox = new Checkbox(
				I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.options.TextStyleChooserDialog.BorderPanel.Four"),
				borderThickness, false);

		private Checkbox fiveCheckBox = new Checkbox(
				I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.options.TextStyleChooserDialog.BorderPanel.Five"),
				borderThickness, false);

		private Checkbox sixCheckBox = new Checkbox(
				I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.options.TextStyleChooserDialog.BorderPanel.Six"),
				borderThickness, false);

		public BorderPanel() {
			setBorder(BorderFactory
					.createTitledBorder(I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.options.TextStyleChooserDialog.BorderPanel.Name")));
			choisePanel.setPreferredSize(new Dimension(400, 20));
			choisePanel.setLayout(new GridLayout(1,2));
			choisePanel.add(yesCheckBox);
			yesCheckBox.addItemListener(new ItemListener(){
                public void itemStateChanged(ItemEvent arg0) {
                    setEnabled(true);
                }});
			choisePanel.add(noCheckBox);
			noCheckBox.addItemListener(new ItemListener(){
                public void itemStateChanged(ItemEvent arg0) {
                    setEnabled(false);
                    textoEjemploLabel.setBorder(null);
                }});
			
			color.setOpaque(true);
			color.setBorder(BorderFactory
					.createEtchedBorder(EtchedBorder.RAISED));
			color.setPreferredSize(new Dimension(160, 20));
			color.setBackground(Color.WHITE);
			color.addPropertyChangeListener(new PropertyChangeListener() {
				public void propertyChange(PropertyChangeEvent evt) {
					textoEjemploLabel.setBorder(BorderFactory.createLineBorder(color
							.getBackground(), Integer.valueOf(
							borderThickness.getSelectedCheckbox().getLabel())
							.intValue()));
				}
			});

			borderColorButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					new ColorChooserDialog(
							I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.options.TextStyleChooserDialog.BorderPanel.BorderColor"),
							color);
				}
			});

			borderThicknessPanel
					.setBorder(BorderFactory
							.createTitledBorder(I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.options.TextStyleChooserDialog.BorderPanel.BorderThickness")));
			borderThicknessPanel.setLayout(new GridLayout(1, 6));
			borderThicknessPanel.add(oneCheckBox);
			oneCheckBox.addItemListener(new ItemChange());
			borderThicknessPanel.add(twoCheckBox);
			twoCheckBox.addItemListener(new ItemChange());
			borderThicknessPanel.add(threeCheckBox);
			threeCheckBox.addItemListener(new ItemChange());
			borderThicknessPanel.add(fourCheckBox);
			fourCheckBox.addItemListener(new ItemChange());
			borderThicknessPanel.add(fiveCheckBox);
			fiveCheckBox.addItemListener(new ItemChange());
			borderThicknessPanel.add(sixCheckBox);
			sixCheckBox.addItemListener(new ItemChange());

			borderThicknessPanel.setPreferredSize(new Dimension(395, 45));

			borderPanel.setPreferredSize(new Dimension(400, 70));
			borderPanel.setLayout(new GridBagLayout());
			
			borderPanel.add(borderColor, new GridBagConstraints(1, 1, 1, 1,
					0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
					new Insets(0, 0, 0, 0), 0, 0));
			borderPanel.add(color, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
					GridBagConstraints.WEST, GridBagConstraints.NONE,
					new Insets(0, 0, 0, 0), 0, 0));
			borderPanel.add(borderColorButton, new GridBagConstraints(3, 1, 1,
					1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			borderPanel
					.add(borderThicknessPanel, new GridBagConstraints(1, 2, 3,
							1, 0.0, 0.0, GridBagConstraints.WEST,
							GridBagConstraints.REMAINDER,
							new Insets(0, 0, 0, 0), 0, 0));
			setLayout(new GridBagLayout()); 
			
			add(choisePanel, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.REMAINDER,
					new Insets(0, 0, 0, 0), 0, 0));
			
			add(borderPanel, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.REMAINDER,
					new Insets(0, 0, 0, 0), 0, 0));
		}
		
		public void setEnabled(boolean isEnabled){
		    borderPanel.setEnabled(isEnabled);
            color.setEnabled(isEnabled);
            borderColor.setEnabled(isEnabled);
            borderColorButton.setEnabled(isEnabled);
            borderThicknessPanel.setEnabled(isEnabled);
            oneCheckBox.setEnabled(isEnabled);
            twoCheckBox.setEnabled(isEnabled);
            threeCheckBox.setEnabled(isEnabled);
            fourCheckBox.setEnabled(isEnabled);
            fiveCheckBox.setEnabled(isEnabled);
            sixCheckBox.setEnabled(isEnabled);
		}

		private class ItemChange implements ItemListener {
			public void itemStateChanged(ItemEvent e) {
				textoEjemploLabel.setBorder(BorderFactory.createLineBorder(color
						.getBackground(), Integer.valueOf(
						borderThickness.getSelectedCheckbox().getLabel())
						.intValue()));
			}
		}
	}

	private class TramePanel extends JPanel {
		private JPanel stylePanel = new JPanel();

		private TrameColorPanel tramePanel = new TrameColorPanel();

		private CheckboxGroup cbg = new CheckboxGroup();

		private Checkbox transparent = new Checkbox(
				I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.options.TextStyleChooserDialog.TramePanel.Transparent"),
				cbg, true);

		private Checkbox opaque = new Checkbox(
				I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.options.TextStyleChooserDialog.TramePanel.Opaque"),
				cbg, false);

		public TramePanel() {
			setBorder(BorderFactory
					.createTitledBorder(I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.options.TextStyleChooserDialog.TramePanel.Name")));

			stylePanel.setPreferredSize(new Dimension(400, 20));
			stylePanel.setLayout(new GridLayout(1, 2));
			stylePanel.add(transparent);
			transparent.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					textoEjemploLabel.setOpaque(false);
					tramePanel.setEnabled(textoEjemploLabel.isOpaque());
				}
			});
			stylePanel.add(opaque);
			opaque.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					textoEjemploLabel.setOpaque(true);
					tramePanel.setEnabled(textoEjemploLabel.isOpaque());
				}
			});
			setLayout(new GridBagLayout());
			add(stylePanel, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
					GridBagConstraints.WEST, GridBagConstraints.REMAINDER,
					new Insets(0, 0, 0, 0), 0, 0));
			add(tramePanel, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
					GridBagConstraints.WEST, GridBagConstraints.REMAINDER,
					new Insets(0, 0, 0, 0), 0, 0));
		}

		private class TrameColorPanel extends JPanel {
			private JLabel borderColor = new JLabel(
					I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.options.TextStyleChooserDialog.TramePanel.Color"));

			private JPanel color = new JPanel();

			private ColorButton trameColorButton = new ColorButton(
					"/reso/jumpPlugIn/printLayoutPlugIn/images/ColorPalette.gif",
					I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.options.TextStyleChooserDialog.TramePanel.ColorPalette"));

			public TrameColorPanel() {
				color.setOpaque(true);
				color.setBorder(BorderFactory
						.createEtchedBorder(EtchedBorder.RAISED));
				color.setPreferredSize(new Dimension(160, 20));
				color.setBackground(textoEjemploLabel.getBackground());
				color.addPropertyChangeListener(new PropertyChangeListener() {
					public void propertyChange(PropertyChangeEvent evt) {
						textoEjemploLabel.setBackground(color.getBackground());
					}
				});

				trameColorButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						new ColorChooserDialog(
								I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.options.TextStyleChooserDialog.TramePanel.TrameColor"),
								color);
					}
				});

				setPreferredSize(new Dimension(400, 30));

				setLayout(new GridBagLayout());
				add(borderColor, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
						GridBagConstraints.EAST, GridBagConstraints.REMAINDER,
						new Insets(0, 0, 0, 5), 0, 0));
				add(color, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
						GridBagConstraints.EAST, GridBagConstraints.REMAINDER,
						new Insets(0, 0, 0, 0), 0, 0));
				add(trameColorButton, new GridBagConstraints(3, 1, 1, 1, 0.0,
						0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
						new Insets(0, 0, 0, 0), 0, 0));
				setEnabled(false);
			}

			public void setEnabled(boolean isEnabled) {
				super.setEnabled(isEnabled);
				borderColor.setEnabled(isEnabled);
				color.setEnabled(isEnabled);
				trameColorButton.setEnabled(isEnabled);
			}
		}
	}

	private class ColorButton extends JButton {
		private final Insets margins = new Insets(0, 0, 0, 0);

		public ColorButton(String imageFile, String text) {
			URL imageURL = reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.graphicText.TextStyleChooserDialog.class
					.getResource(imageFile);
			setActionCommand(text);
			setToolTipText(text);

			if (imageURL != null) { //image found
				setIcon(new ImageIcon(imageURL, text));
			} else { //no image found
				setText(text);
				System.err.println("Resource not found: " + imageFile);
			}
			setMargin(margins);
			setVerticalTextPosition(BOTTOM);
			setHorizontalTextPosition(CENTER);
		}
	}
}