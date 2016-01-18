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
