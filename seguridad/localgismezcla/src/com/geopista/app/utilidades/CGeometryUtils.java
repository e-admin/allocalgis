/**
 * The GEOPISTA project is a set of tools and applications to manage
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
 USA.
 *
 * For more information, contact:
 *
 *
 * www.geopista.com
 *
 *
 */
package com.geopista.app.utilidades;

import com.geopista.model.GeopistaLayer;
import com.vividsolutions.jump.feature.Feature;

import java.util.Iterator;
import java.io.StringWriter;
import java.io.PrintWriter;

import org.apache.log4j.Logger;

/**
 * @author SATEC
 * @version $Revision: 1.1 $
 *          <p/>
 *          Autor:$Author: miriamperez $
 *          Fecha Ultima Modificacion:$Date: 2009/07/03 12:31:31 $
 *          $Name:  $
 *          $RCSfile: CGeometryUtils.java,v $
 *          $Revision: 1.1 $
 *          $Locker:  $
 *          $State: Exp $
 *          <p/>
 *          To change this template use Options | File Templates.
 */
public class CGeometryUtils {

	public static Logger logger = Logger.getLogger(CGeometryUtils.class);

	public static boolean listFeatureAttributes(GeopistaLayer layer, boolean justOnce) {

		try {


			logger.info("layer.getName(): " + layer.getName());
			logger.info("layer.getSystemId(): " + layer.getSystemId());


			java.util.List featureList = layer.getFeatureCollectionWrapper().getFeatures();
			logger.info("featureList.size(): " + featureList.size());

			Iterator it = featureList.iterator();
			while (it.hasNext()) {

				Feature feature = (Feature) it.next();
				int count = feature.getSchema().getAttributeCount();


				for (int i = 0; i < count; i++) {

					String attributeName = feature.getSchema().getAttributeName(i);
					String attributeType = feature.getSchema().getAttributeType(i).toJavaClass().getName();
					String attributeValue = feature.getString(attributeName);
					logger.info(attributeName + ": " + attributeValue);

				}

				logger.info("\n");
				if (justOnce) {
					return true;
				}

			}

			return true;

		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return false;
		}
	}

}
