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
 * Created on 23-jun-2004 by juacas
 *
 * 
 */
package com.geopista.util;


/**
 * @author juacas
 *
 * Avisa al panel que se inserta en un FeatureDialog
 * que se está entrando o saliendo en el TabbedPane.
 * 
 * @see FeatureDialog
 * @see FeatureExtendedForm 
 * @see FeatureDialogHome
 * */
public interface FeatureExtendedPanel 
{
public abstract void enter();
public abstract void exit();
}
