/*
 * * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * For more information, contact:
 *
 * 
 * www.geopista.com
 * 
 * Created on 15-jun-2004 by juacas
 *
 * 
 */
package com.localgis.app.gestionciudad.dialogs.documents.dialogs.utils;

import com.vividsolutions.jump.feature.Feature;

/**
 * Interfaz del Diálogo que puede albergar nuevos paneles de formulario
 * @author juacas
 */
public interface FeatureDialogHome
{
public void addPanel(FeatureExtendedPanel form);
public void addPanel(FeatureExtendedPanel form,int pos);
public void removePanel(FeatureExtendedPanel form);
public void setPanelEnabled (int pos, boolean act);
/**
 * Devuelve el Feature asociado a este formulario.
 * Si la instancia es de GeopistaFeature existen algunas funcionalidades nuevas.
 * 
 * @return Feature asociado al formulario
 * @see Feature, GeopistaFeature
 */
public Feature getFeature();
}
