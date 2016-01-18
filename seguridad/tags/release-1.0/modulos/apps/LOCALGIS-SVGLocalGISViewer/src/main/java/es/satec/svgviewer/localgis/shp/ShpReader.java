package es.satec.svgviewer.localgis.shp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class ShpReader {

	private static Logger logger = (Logger) Logger.getInstance(ShpReader.class);

	protected static Color TransparentColor = null;
	private static final String delimiter = "@";

	private int count;

	protected static Legend legend;
	protected static double scale;
	
	private ShpRenderer renderer;

	public ShpReader() {

		TransparentColor = Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);

		count = 0;
		legend = new Legend();
		scale = 1000D;
		
		renderer = new ShpRenderer();
	}
	
	public Vector getLoadedFiles() {
		return legend.theme_vector;
	}

	public Theme loadShp(String shpFilePath) throws ShpException {
		if (shpFilePath == null) throw new IllegalArgumentException();

		File f = new File(shpFilePath);
		if (!f.exists()) throw new ShpException("El fichero no existe");

		return readShp(shpFilePath);
	}
	
	public void unloadShp(int index) {
		if (index >= 0 && index < legend.theme_vector.size()) {
			legend.theme_vector.removeElementAt(index);
			count--;
		}
	}
	
	public void setVisible(int index, boolean visible) {
		if (index >= 0 && index < legend.theme_vector.size()) {
			Theme theme = (Theme) legend.theme_vector.elementAt(index);
			theme.enable = visible;
		}
		
	}
	
	public Color getForeColor() {
		return renderer.getForeColor();
	}
	
	public Color getBackColor() {
		return renderer.getBackColor();
	}

	public Color getLabelColor() {
		return renderer.getLabelColor();
	}

	public void setColor(Color color) {
		if (color == null) {
			throw new IllegalArgumentException("Color nulo");
		}
		renderer.setColor(color);
	}
	
	public double getScale() {
		return scale;
	}
	
	public boolean somethingToDisplay() {
		if (count >= 0) {
			for (int i=0; i<count; i++) {
				Theme theme = (Theme) legend.theme_vector.elementAt(i);
				if (theme.enable) return true;
			}
		}
		return false;
	}

	private Theme readShp(String shpFilePath) throws ShpException {
		double d4 = 1000D;
		double d5 = 1000D;
		double d6 = 1000D;
		double d7 = 1000D;

		Theme currentTheme = load_layer(shpFilePath);
		if(currentTheme == null)
			return null;
		legend.theme_vector.addElement(currentTheme);
		count++;

		boolean flag1 = true;
		for(int j1 = 0; j1 < count; j1++) {
			Theme theme = (Theme)legend.theme_vector.elementAt(j1);
			if(theme.theme_box != null)
				if(flag1) {
					flag1 = false;
					d4 = theme.theme_box.min.dx;
					d5 = theme.theme_box.min.dy;
					d6 = theme.theme_box.max.dx;
					d7 = theme.theme_box.max.dy;
				} else {
					if(d4 > theme.theme_box.min.dx)
						d4 = theme.theme_box.min.dx;
					if(d5 > theme.theme_box.min.dy)
						d5 = theme.theme_box.min.dy;
					if(d6 < theme.theme_box.max.dx)
						d6 = theme.theme_box.max.dx;
					if(d7 < theme.theme_box.max.dy)
						d7 = theme.theme_box.max.dy;
				}
		}

		if(scale == 1000D) {
			double d8 = Math.abs(d4 / scale);
			double d9 = Math.abs(d5 / scale);
			if(d8 < d9)
				d8 = d9;
			d9 = Math.abs(d6 / scale);
			if(d8 < d9)
				d8 = d9;
			d9 = Math.abs(d7 / scale);
			if(d8 < d9)
				d8 = d9;
			//            if(d8 < 1.0D)
				//                logger.warn("Warning : please set the scale factor to 1000000 or higher");
			//            if(d8 > 500000D)
				//                logger.warn("Warning : please set the scale factor to 1 or lower");
		}
		legend.full_extent_box = new DRectangle(new DPoint(d4, d5), new DPoint(d6, d7));

		//        if(!flag)
			//            legend.map_box = legend.full_extent_box;
		//        else
		//            legend.map_box = new DRectangle(new DPoint(d * scale, d1 * scale), new DPoint(d2 * scale, d3 * scale));
		if(legend.full_extent_box.height != 0)
			legend.whratio = legend.full_extent_box.dwidth / legend.full_extent_box.dheight;
		//        else
		//            legend.whratio = 0.0D;
		//        legend.load_flag = true;

		return currentTheme;
	}

	private Theme load_layer(String shpFilePath) throws ShpException {
		Theme theme = null;
		String dbfFilePath = null;

		if(shpFilePath.toLowerCase().endsWith(".shp")) {
			theme = load_vector_theme(shpFilePath);

			theme.filePath = shpFilePath;
			//theme.set_label_color(color);
			//theme.set_foreground(color1);
			//theme.set_background(color2);
			//theme.enable = true;
			//theme.set_label(true);
			//theme.set_symbol_size(j);
			//theme.label_field = k;

			dbfFilePath = shpFilePath.substring(0, shpFilePath.length()-3) + "dbf";
		}

		// Leer el fichero dbf
		File fdbf = new File(dbfFilePath);
		if(fdbf.exists()) {
			logger.debug("Leyendo el fichero " + dbfFilePath + "...");
			FileInputStream fis = null;
			NetInputStream netinputstream = null;
			Vector vector = new Vector(1);
			Vector vector1 = new Vector(1);
			Vector vector2 = new Vector(1);
			try {
				fis = new FileInputStream(dbfFilePath);
				netinputstream = new NetInputStream(fis);
				netinputstream.readByte();
				netinputstream.readByte();
				netinputstream.readByte();
				netinputstream.readByte();
				netinputstream.readLInt();
				int i1 = netinputstream.readLShort();
				netinputstream.readLShort();
				netinputstream.skip(20);
				for(i1 -= 33; i1 > 0; i1 -= 32) {
					byte abyte0[] = new byte[11];
					netinputstream.read(abyte0);
					boolean flag2 = false;
					for(int k1 = 0; k1 < 11; k1++)
						if(!flag2) {
							if(abyte0[k1] == 0)
								flag2 = true;
						} else {
							abyte0[k1] = 0;
						}

					String s11 = new String(abyte0);
					s11 = s11.trim();
					vector.addElement(s11);
					byte abyte2[] = new byte[1];
					netinputstream.read(abyte2);
					String s13 = new String(abyte2);
					s13 = s13.trim();
					vector1.addElement(s13);
					netinputstream.skip(4);
					byte byte1 = netinputstream.readByte();
					vector2.addElement(new Integer(byte1));
					netinputstream.skip(15);
				}

				netinputstream.skip(1);
				do {
					byte byte0;
					try {
						byte0 = netinputstream.readByte();
					} catch(Exception _ex) {
						byte0 = 26;
					}
					if(byte0 == 26)
						break;
					if(byte0 != 42) {
						String s8 = "";
						for(int j1 = 0; j1 < vector.size(); j1++) {
							int l1 = ((Integer)vector2.elementAt(j1)).intValue();
							byte abyte1[] = new byte[l1];
							netinputstream.readFully(abyte1);
							String s12 = new String(abyte1);
							s12 = s12.trim();
							if(s12.length() == 8 && ((String)vector1.elementAt(j1)).equals("D"))
								s12 = s12.substring(0, 4) + "/" + s12.substring(4, 6) + "/" + s12.substring(6, 8);
							if(j1 == 0)
								s8 = s8 + s12;
							else
								s8 = s8 + delimiter + s12;
						}

						theme.info_vector.addElement(s8);
					}
				} while(true);
				netinputstream.close();
			} catch(Exception e) {
				logger.error("Error al leer el dbf", e);
			} finally {
				if (fis != null) {
					try {
						fis.close();
					} catch (IOException e) {}
				}
				if (netinputstream != null) {
					netinputstream.close();
				}
			}
		}

		return theme;
	}

	private Theme load_vector_theme(String shpFilePath) throws ShpException {
		logger.debug("Loading " + shpFilePath + "...");

		Theme theme = null;
		FileInputStream fis = null;
		NetInputStream netinputstream = null;
		try {
			fis = new FileInputStream(shpFilePath);

			netinputstream = new NetInputStream(fis);

			int i = netinputstream.readBInt();
			if(i != 9994)
				throw new ShpException("Error al leer fichero SHP");
			netinputstream.skip(20);
			int j = netinputstream.readBInt();
			int k = netinputstream.readLInt();
			if(k != 1000)
				throw new ShpException("Error al leer fichero SHP");
			int l = netinputstream.readLInt();
			DRectangle drectangle = netinputstream.readDRectangle();
			theme = new Theme(l, drectangle);
			netinputstream.skip(32);
			switch(l) {
			default:
				break;

			case 1: // '\001'
				do
				{
					int i1 = netinputstream.readBInt();
					if(netinputstream.eof())
						break;
					int l1 = netinputstream.readBInt();
					int i3 = netinputstream.readLInt();
					if(l != i3)
						throw new ShpException("Error al leer fichero SHP");
					DPoint dpoint = netinputstream.readDPoint();
					PTFeature ptfeature = new PTFeature(dpoint);
					theme.feature_vector.addElement(ptfeature);
				} while(true);
				break;

			case 3: // '\003'
				do
				{
					int j1 = netinputstream.readBInt();
					if(netinputstream.eof())
						break;//throw new ShpException("Error al leer fichero SHP");
					int i2 = netinputstream.readBInt();
					int j3 = netinputstream.readLInt();
					if(l != j3)
						throw new ShpException("Error al leer fichero SHP");
					DRectangle drectangle1 = netinputstream.readDRectangle();
					ArcFeature arcfeature = new ArcFeature(drectangle1);
					int j4 = netinputstream.readLInt();
					int l4 = netinputstream.readLInt();
					for(int k6 = 0; k6 < j4; k6++)
					{
						int k5 = netinputstream.readLInt();
						arcfeature.part_vector.addElement(new Integer(k5));
					}

					for(int i7 = 0; i7 < l4; i7++)
					{
						DPoint dpoint1 = netinputstream.readDPoint();
						arcfeature.point_vector.addElement(dpoint1);
					}

					theme.feature_vector.addElement(arcfeature);
				} while(true);

			case 5: // '\005'
				do
				{
					int k1 = netinputstream.readBInt();
					if(netinputstream.eof())
						break;//throw new ShpException("Error al leer fichero SHP");
					int j2 = netinputstream.readBInt();
					int k3 = netinputstream.readLInt();
					if(l != k3)
						throw new ShpException("Error al leer fichero SHP");
					DRectangle drectangle2 = netinputstream.readDRectangle();
					PGFeature pgfeature = new PGFeature(drectangle2);
					int k4 = netinputstream.readLInt();
					int i5 = netinputstream.readLInt();
					for(int l6 = 0; l6 < k4; l6++)
					{
						int l5 = netinputstream.readLInt();
						pgfeature.part_vector.addElement(new Integer(l5));
					}

					for(int j7 = 0; j7 < i5; j7++)
					{
						DPoint dpoint2 = netinputstream.readDPoint();
						pgfeature.point_vector.addElement(dpoint2);
					}

					theme.feature_vector.addElement(pgfeature);
				} while(true);
			}
		} catch (FileNotFoundException e) {
			throw new ShpException("Fichero SHP no encontrado");
		} catch (ShpException e) {
			throw e;
		} catch (Exception e) {
			throw new ShpException("Error al leer fichero SHP: " + e.getMessage());
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {}
			}
			if (netinputstream != null) {
				netinputstream.close();
			}
		}
		return theme;
	}

	private void setMapBox(double xmin, double ymin, double xmax, double ymax) {
		legend.map_box = new DRectangle(new DPoint(xmin * scale, ymin * scale), new DPoint(xmax * scale, ymax * scale));
	}

	public Image paint(double xmin, double ymin, double xmax, double ymax, int width, int height) {
		logger.debug("Dibuja shp en (" + xmin + ", " + ymin + ") - (" + xmax + ", " + ymax + ") Tamaño: " + width + "x" + height);
		setMapBox(xmin, ymin, xmax, ymax);
		renderer.setSize(width, height);
		if (count == 0) return null;
		return renderer.paint(count);
	}

}
