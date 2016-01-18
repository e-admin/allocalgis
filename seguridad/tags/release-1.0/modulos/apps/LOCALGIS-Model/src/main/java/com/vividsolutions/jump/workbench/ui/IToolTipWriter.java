package com.vividsolutions.jump.workbench.ui;

import java.awt.geom.Point2D;

public interface IToolTipWriter {

	public abstract boolean isEnabled();

	public abstract void setEnabled(boolean enabled);

	public abstract String write(String template, Point2D mouseLocation);

}