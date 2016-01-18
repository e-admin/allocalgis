/**
 * ExpiredActuationsThread.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * 
 */
package com.localgis.app.gestionciudad.expiredactuations;

import java.awt.Frame;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.util.config.UserPreferenceStore;
import com.localgis.app.gestionciudad.beans.LocalGISIntervention;
import com.localgis.app.gestionciudad.dialogs.interventions.dialogs.AvisosCaducadosDialog;
import com.localgis.app.gestionciudad.dialogs.interventions.utils.NotesInterventionsEditionTypes;
import com.localgis.app.gestionciudad.webservicesclient.wrapper.WSInterventionsWrapper;

/**
 * @author javieraragon
 *
 */
public class ExpiredActuationsThread extends Thread{
    
	private boolean finishThread = false;
	private ArrayList<LocalGISIntervention> expiredinterventions = new ArrayList<LocalGISIntervention>();
	
	/**
	 * @return the finishThread
	 */
	public boolean isFinishThread() {
		return finishThread;
	}

	/**
	 * @param finishThread the finishThread to set
	 */
	public void finishThread(boolean finishThread) {
		this.finishThread = finishThread;
	}

	public ExpiredActuationsThread(String str) {
        super(str);
    }
    
    public void run() {
    	this.expiredinterventions = getExpiredsInterventionsFromDataBase();
    	

    	if (expiredinterventions != null && !expiredinterventions.isEmpty()){
    		Frame iThreadAvisosCaducadosFrame = new Frame("Actuaciones Caducadas");
    		AvisosCaducadosDialog dialog = new AvisosCaducadosDialog(iThreadAvisosCaducadosFrame,NotesInterventionsEditionTypes.EDIT,expiredinterventions);
    		dialog.dispose();
    		iThreadAvisosCaducadosFrame.dispose();
    	}
    	
    	
        while (!isFinishThread()){
        	try {
				this.sleep(50000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        	
			if (newExpiredDocuments(expiredinterventions)){
				Frame iThreadAvisosCaducadosFrame = new Frame("Actuaciones Caducadas");
	    		AvisosCaducadosDialog dialog = new AvisosCaducadosDialog(iThreadAvisosCaducadosFrame,NotesInterventionsEditionTypes.EDIT,this.expiredinterventions);
	    		dialog.dispose();
	    		iThreadAvisosCaducadosFrame.dispose();
			}
        }
        System.out.println("Termina thread " + getName());
    }

	private boolean newExpiredDocuments(ArrayList<LocalGISIntervention> expiredInterventions) {
		ArrayList<LocalGISIntervention> newExpiredInterventions = getExpiredsInterventionsFromDataBase();
		if (newExpiredInterventions!= null && !newExpiredInterventions.isEmpty()){
			if (expiredInterventions != null){
				Iterator<LocalGISIntervention> itnewexpired = newExpiredInterventions.iterator();
				while (itnewexpired.hasNext()){
					if (!expiredInterventions.contains(itnewexpired.next())){
						expiredinterventions = newExpiredInterventions;
						return true;
					}
				}
			}
		}
		return false;
	}
    
	
	
	private ArrayList<LocalGISIntervention> getExpiredsInterventionsFromDataBase(){
		if (!AppContext.getApplicationContext().isLogged() || !AppContext.getApplicationContext().isOnline()){
			AppContext.getApplicationContext().login();
		} else{
		}
		String userName = AppContext.getApplicationContext().getString(UserPreferenceConstants.LOCALGIS_LOGIN_LAST_USERNAME);
		int idMunicipio = AppContext.getIdMunicipio();
		if (userName!=null && !userName.equals("") && idMunicipio > 0){
			try {
				return WSInterventionsWrapper.getExpiredInterventions(userName, idMunicipio, new GregorianCalendar());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return new ArrayList<LocalGISIntervention>();
			}
		}
    	return new ArrayList<LocalGISIntervention>();
	}
    


}
