/**
 * PredefinedParametersEditor.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.reports.parameters.editors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.geopista.app.AppContext;
import com.geopista.app.reports.ReportsContext;

public class PredefinedParametersEditor implements ReportParametersEditor {

	public Map getFilledParameters() {
		HashMap parameterValueMap = new HashMap();


		//parameterValueMap.put("ID_ENTIDAD",
		//		AppContext.getApplicationContext().getString("geopista.DefaultEntityId"));
		parameterValueMap.put("ID_ENTIDAD",
				String.valueOf(AppContext.getIdEntidad()));
		parameterValueMap.put("SUBREPORT_DIR",
				ReportsContext.getInstance().getBaseCompiledReportsPath());
		
		return parameterValueMap;
	}

	public List getParameterEditionPanels() {
		return new ArrayList();
	}

}
