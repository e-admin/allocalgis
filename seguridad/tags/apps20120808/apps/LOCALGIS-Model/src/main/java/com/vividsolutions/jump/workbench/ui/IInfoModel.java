package com.vividsolutions.jump.workbench.ui;

import java.util.Collection;
import java.util.List;

import com.vividsolutions.jump.workbench.model.ILayer;

public interface IInfoModel {

	/**
	 * Releases references to the data, to facilitate garbage collection.
	 * Important for MDI apps like the JCS Workbench.
	 */
	public abstract void dispose();

	public abstract Collection getLayerTableModels();

	public abstract void add(ILayer layer, Collection features);

	public abstract void remove(ILayer layer);

	public abstract void clear();

	public abstract ILayerTableModel getTableModel(ILayer layer);

	public abstract List getLayers();

	public abstract void addListener(InfoModelListener listener);

}