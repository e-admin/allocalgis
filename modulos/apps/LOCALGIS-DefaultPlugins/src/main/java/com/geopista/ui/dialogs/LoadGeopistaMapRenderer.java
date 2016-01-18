/**
 * LoadGeopistaMapRenderer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.dialogs;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import com.geopista.app.AppContext;
import com.geopista.model.GeopistaMap;

/**
 * TODO Documentación
 * 
 * @author juacas
 * 
 */
public class LoadGeopistaMapRenderer extends JPanel implements ListCellRenderer {
	JLabel lblThumb = new JLabel();

	JLabel lblEstado = new JLabel();

	JLabel lblFecha = new JLabel();

	JLabel lblDescripcion = new JLabel();

	JLabel lblTitulo = new JLabel();

	JLabel lblMunicipio = new JLabel();

	private static AppContext aplicacion = (AppContext) AppContext
			.getApplicationContext();

	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			java.awt.GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
			java.awt.GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			java.awt.GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
			java.awt.GridBagConstraints gridBagConstraints19 = new GridBagConstraints();

			java.awt.GridBagConstraints gridBagConstraintsMunic = new GridBagConstraints();

			jPanel = new JPanel();
			jPanel.setLayout(new GridBagLayout());
			jPanel.setOpaque(false);
			gridBagConstraints19.gridx = 0;
			gridBagConstraints19.gridy = 0;
			gridBagConstraints19.gridwidth = 2;
			gridBagConstraints19.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints20.gridx = 0;
			gridBagConstraints20.gridy = 1;
			gridBagConstraints20.gridwidth = 2;
			gridBagConstraints20.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints20.insets = new java.awt.Insets(5, 0, 0, 0);
			gridBagConstraints21.gridx = 0;
			gridBagConstraints21.gridy = 2;
			gridBagConstraints21.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints21.insets = new java.awt.Insets(5, 0, 0, 0);
			gridBagConstraints22.gridx = 1;
			gridBagConstraints22.gridy = 2;
			gridBagConstraints22.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints22.insets = new java.awt.Insets(7, 9, 0, 0);

			gridBagConstraintsMunic.gridx = 0;
			gridBagConstraintsMunic.gridy = 4;
			gridBagConstraintsMunic.gridwidth = 2;
			gridBagConstraintsMunic.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraintsMunic.insets = new java.awt.Insets(8, 20, 8, 0);

			jPanel.add(lblTitulo, gridBagConstraints19);
			jPanel.add(lblDescripcion, gridBagConstraints20);
			jPanel.add(lblEstado, gridBagConstraints21);
			jPanel.add(lblFecha, gridBagConstraints22);
			jPanel.add(lblMunicipio, gridBagConstraintsMunic);

		}
		return jPanel;
	}

	/**
	 * This is the default constructor
	 */
	public LoadGeopistaMapRenderer() {
	}

	private String filePath = null;

	private String basePath = null;

	private JPanel jPanel = null;

	public String getFilePath() {
		return this.filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList,
	 *      java.lang.Object, int, boolean, boolean)
	 */
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {

		GeopistaMap mapGeopista = (GeopistaMap) value;
		String fechaMapa = null;
		String estadoMapa = null;

		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT,
				DateFormat.SHORT);
		if (mapGeopista.getTimeStamp() != null)
			fechaMapa = (df.format(mapGeopista.getTimeStamp()));
		else
			fechaMapa = "";
		
		if (mapGeopista.isExtracted())
			estadoMapa = aplicacion.getI18nString("Mapa.Extraido");
		else if (mapGeopista.isSystemMap())
			estadoMapa = aplicacion.getI18nString("Mapa.BaseDatos");
		else
			estadoMapa = aplicacion.getI18nString("Mapa.Local");

		java.awt.FlowLayout flowLayout18 = new FlowLayout();
		java.awt.GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
		java.awt.GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
		java.awt.GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
		java.awt.GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
		java.awt.GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
		this.setLayout(flowLayout18);
		setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

		lblThumb.setIcon(new ImageIcon(mapGeopista.getThumbnail()));

		/*try {			
			java.awt.Image newImg = mapGeopista.getThumbnail();
		     int imgWidth = 20;
	        int imgHeight = 20;
	        java.awt.image.BufferedImage bim = new java.awt.image.BufferedImage(imgWidth,
	                imgHeight, java.awt.image.BufferedImage.TYPE_INT_ARGB);
			ImageIO.write(bim, "jpg",new File("C:\\tmp\\out.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		// CREAMOS LAS ETIQUETAS
		// THUMBNAIL
		lblThumb.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		lblThumb.setOpaque(false);
		// lblThumb.setPreferredSize(new java.awt.Dimension(100,80));
		// ESTADO
		lblEstado.setText(aplicacion.getI18nString("Mapa.Estado") + " "	+ estadoMapa);
		lblEstado.setOpaque(false);

		if (estadoMapa.equals(aplicacion.getI18nString("Mapa.Local")))
			lblEstado.setIcon(com.geopista.ui.images.IconLoader
					.icon("Data.gif")); // Generated
		else
			lblEstado.setIcon(com.geopista.ui.images.IconLoader
					.icon("online.png")); // Generated

		lblFecha.setText(aplicacion.getI18nString("Mapa.Fecha") + " "+ fechaMapa);
		lblFecha.setOpaque(false);

		lblDescripcion.setText(aplicacion.getI18nString("Mapa.Descripcion")
				+ " " + mapGeopista.getDescription());

		lblDescripcion.setText(aplicacion.getI18nString("Mapa.Titulo") + " "
				+ mapGeopista.getName()+" ("+mapGeopista.getSystemId()+")");
		lblDescripcion.setOpaque(false);

		lblMunicipio.setText("Identificador de entidad:" + " "
				+ mapGeopista.getIdEntidad());

		gridBagConstraints12.gridx = 0;
		gridBagConstraints12.gridy = 0;
		gridBagConstraints12.fill = java.awt.GridBagConstraints.NONE;
		gridBagConstraints12.gridheight = 3;
		gridBagConstraints13.gridx = 1;
		gridBagConstraints13.gridy = 0;
		gridBagConstraints13.gridwidth = 2;
		gridBagConstraints13.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints13.insets = new java.awt.Insets(5, 5, 0, 5);
		gridBagConstraints15.gridx = 1;
		gridBagConstraints15.gridy = 2;
		gridBagConstraints15.insets = new java.awt.Insets(0, 5, 5, 0);
		gridBagConstraints16.gridx = 1;
		gridBagConstraints16.gridy = 1;
		gridBagConstraints16.gridwidth = 2;
		gridBagConstraints16.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints16.insets = new java.awt.Insets(5, 5, 5, 5);
		gridBagConstraints17.gridx = 2;
		gridBagConstraints17.gridy = 2;
		gridBagConstraints17.insets = new java.awt.Insets(0, 0, 5, 5);
		flowLayout18.setAlignment(java.awt.FlowLayout.LEFT);
		lblTitulo.setPreferredSize(new java.awt.Dimension(150, 21));
		this.add(lblThumb, null);
		this.add(getJPanel(), null);

		this.setBackground(isSelected ? Color.lightGray : Color.white);
		this.setForeground(isSelected ? Color.white : Color.black);
		return this;
	}
} // @jve:decl-index=0:visual-constraint="85,19"
