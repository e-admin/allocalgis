package com.geopista.ui.plugin.print.elements;

import java.awt.Graphics;

public interface IMapFrameOnScreen {

	public abstract double getNormalizedScale(double scale);


	public abstract void setReescalado(boolean reescalado);

	public abstract double getEscala();

	public abstract void paint(Graphics g);

	public abstract void setResizedOnView(int zoomActif);

	public abstract int getResizedOnView();

}