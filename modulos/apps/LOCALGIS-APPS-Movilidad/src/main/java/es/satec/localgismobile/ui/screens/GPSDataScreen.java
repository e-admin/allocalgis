/**
 * GPSDataScreen.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is licensed and may be used, modified and redistributed under the terms of the European Public License (EUPL), either version 1.1 or (at your option) any later version as soon as they are approved by the European Commission.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and more details.
 * You should have received a copy of the EUPL1.1 license along with this program; if not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */

package es.satec.localgismobile.ui.screens;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import es.satec.localgismobile.fw.Config;
import es.satec.localgismobile.fw.Messages;
import es.satec.localgismobile.session.SessionInfo;
import es.satec.localgismobile.ui.LocalGISWindow;
import es.satec.svgviewer.localgis.GPSLocation;


public class GPSDataScreen extends LocalGISWindow {
	
	private GPSLocation gpsLocation=null;
	
	private Group groupGPSLocation = null;
	private Button buttonRefrescar = null;
	private Text textLongitude=null;
	private Text textLatitud=null;
	private Text textAltitude=null;
	private Text textSpeed=null;
	private Text textDirection=null;
	private Text textQuality=null;
	private Text textNumberOfSatellites=null;
	
	public GPSDataScreen(Shell parent) {
		super(parent);
		
		/*Se coje de la sesión en GPSProvieder y de ahí el GPSLocation*/
		
		gpsLocation=SessionInfo.getInstance().getGPSProvider().getGPSLocation();
		init();
		show();
	}

	private void init() {	
		GridData gridData1 = new GridData();
		gridData1.horizontalAlignment = GridData.CENTER;
		gridData1.verticalAlignment = GridData.CENTER;
		shell.setLayout(new GridLayout());
		shell.setBackground(Config.COLOR_APLICACION);
		createGroup();
		
		buttonRefrescar = new Button(shell, SWT.NONE);
		buttonRefrescar.setText(Messages.getMessage("GPSScreenData_Refrescar"));
		buttonRefrescar.setLayoutData(gridData1);
		buttonRefrescar.addListener(SWT.Selection,new Listener(){
			public void handleEvent(Event event) {

				//Se vuelve a pedir el Location
				gpsLocation=SessionInfo.getInstance().getGPSProvider().getGPSLocation();
				if(gpsLocation!=null){
					//Reyeno los datos
					textLongitude.setText(gpsLocation.getLongitude()+"");
					textLatitud.setText(gpsLocation.getLatitude()+"");
					textAltitude.setText(gpsLocation.getAltitude()+"");
					textSpeed.setText(gpsLocation.getSpeed()+"");
					textDirection.setText(gpsLocation.getDirection()+"");
					textQuality.setText(gpsLocation.getDirection()+"");
					textNumberOfSatellites.setText(gpsLocation.getDirection()+"");
				}
				else{
					MessageBox mb = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
					mb.setMessage(Messages.getMessage("errores.conexionGPS"));
					mb.open();
				}
			}});
	}
	
	/**
	 * This method initializes group	
	 *
	 */
	private void createGroup() {
		
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.verticalAlignment = GridData.FILL;
		groupGPSLocation = new Group(shell, SWT.NONE);
		groupGPSLocation.setBackground(Config.COLOR_APLICACION);
		groupGPSLocation.setLayout(gridLayout);
		groupGPSLocation.setLayoutData(gridData);
		groupGPSLocation.setText(Messages.getMessage("GPSScreenData_TituloGroupGPSLocation"));
		añadirInformacionGPS();
	}

	private void añadirInformacionGPS() {

		/*Se inserta la informacion del GPS en el grupo*/
		GridData gridData3 = new GridData();
		gridData3.horizontalAlignment = GridData.FILL;
		gridData3.grabExcessHorizontalSpace = true;
		gridData3.grabExcessVerticalSpace = true;
		gridData3.verticalAlignment = GridData.FILL;
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.numColumns = 3;
		gridLayout1.verticalSpacing = 0;
		gridLayout1.marginWidth = 0;
		gridLayout1.marginHeight = 0;
		gridLayout1.horizontalSpacing = 0;
		
		Font font = new Font(shell.getDisplay(), "Arial", 7, SWT.NORMAL);
		if(gpsLocation!=null){
		
			//Longitud
			Label labelLongitude = new Label(groupGPSLocation, SWT.NONE);
			labelLongitude.setText(Messages.getMessage("GPSScreenData_Longitude"));
			labelLongitude.setFont(font);
			labelLongitude.setBackground(Config.COLOR_APLICACION);
			textLongitude = new Text(groupGPSLocation, SWT.BORDER);
			textLongitude.setText(gpsLocation.getLongitude()+"");
			textLongitude.setFont(font);
			textLongitude.setEditable(false);
			
			//Latitud
			Label labelLatitud = new Label(groupGPSLocation, SWT.NONE);
			labelLatitud.setText(Messages.getMessage("GPSScreenData_Latitud"));
			labelLatitud.setFont(font);
			labelLatitud.setBackground(Config.COLOR_APLICACION);
			textLatitud = new Text(groupGPSLocation, SWT.BORDER);
			textLatitud.setText(gpsLocation.getLatitude()+"");
			textLatitud.setFont(font);
			textLatitud.setEditable(false);
			
			//Altitude
			Label labelAltitude = new Label(groupGPSLocation, SWT.NONE);
			labelAltitude.setText(Messages.getMessage("GPSScreenData_Altitude"));
			labelAltitude.setFont(font);
			labelAltitude.setBackground(Config.COLOR_APLICACION);
			textAltitude = new Text(groupGPSLocation, SWT.BORDER);
			textAltitude.setText(gpsLocation.getAltitude()+"");
			textAltitude.setFont(font);
			textAltitude.setEditable(false);
			
			//Speed
			Label labelSpeed = new Label(groupGPSLocation, SWT.NONE);
			labelSpeed.setText(Messages.getMessage("GPSScreenData_Speed"));
			labelSpeed.setFont(font);
			labelSpeed.setBackground(Config.COLOR_APLICACION);
			textSpeed = new Text(groupGPSLocation, SWT.BORDER);
			textSpeed.setText(gpsLocation.getSpeed()+"");
			textSpeed.setFont(font);
			textSpeed.setEditable(false);
			
			//Direction
			Label labelDirection = new Label(groupGPSLocation, SWT.NONE);
			labelDirection.setText(Messages.getMessage("GPSScreenData_Direction"));
			labelDirection.setFont(font);
			labelDirection.setBackground(Config.COLOR_APLICACION);
			textDirection = new Text(groupGPSLocation, SWT.BORDER);
			textDirection.setText(gpsLocation.getDirection()+"");
			textDirection.setFont(font);
			textDirection.setEditable(false);
			
			//Quality
			Label labelQuality = new Label(groupGPSLocation, SWT.NONE);
			labelQuality.setText(Messages.getMessage("GPSScreenData_Quality"));
			labelQuality.setFont(font);
			labelQuality.setBackground(Config.COLOR_APLICACION);
		    textQuality = new Text(groupGPSLocation, SWT.BORDER);
			textQuality.setText(gpsLocation.getDirection()+"");
			textQuality.setFont(font);
			textQuality.setEditable(false);
			
			//NumberOfSatellites
			Label labelNumberOfSatellites = new Label(groupGPSLocation, SWT.NONE);
			labelNumberOfSatellites.setText(Messages.getMessage("GPSScreenData_NumberOfSatellites"));
			labelNumberOfSatellites.setFont(font);
			labelNumberOfSatellites.setBackground(Config.COLOR_APLICACION);
			textNumberOfSatellites = new Text(groupGPSLocation, SWT.BORDER);
			textNumberOfSatellites.setText(gpsLocation.getDirection()+"");
			textNumberOfSatellites.setFont(font);
			textNumberOfSatellites.setEditable(false);
		}
		else{
			MessageBox mb = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
			mb.setMessage(Messages.getMessage("errores.conexionGPS"));
			mb.open();
		}
		
	}

}
