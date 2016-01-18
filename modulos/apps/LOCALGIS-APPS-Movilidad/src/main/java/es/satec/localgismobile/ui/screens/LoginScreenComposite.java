/**
 * LoginScreenComposite.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is licensed and may be used, modified and redistributed under the terms of the European Public License (EUPL), either version 1.1 or (at your option) any later version as soon as they are approved by the European Commission.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and more details.
 * You should have received a copy of the EUPL1.1 license along with this program; if not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */
package es.satec.localgismobile.ui.screens;

import java.io.InputStream;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import es.satec.localgismobile.core.LocalGISMobile;
import es.satec.localgismobile.fw.Config;
import es.satec.localgismobile.fw.Global;
import es.satec.localgismobile.fw.Messages;
import es.satec.localgismobile.fw.validation.ValidationFacade;
import es.satec.localgismobile.fw.validation.exceptions.LoginCancelException;
import es.satec.localgismobile.fw.validation.exceptions.RolesException;
import es.satec.localgismobile.fw.validation.exceptions.ValidationException;
import es.satec.localgismobile.session.SessionInfo;
import es.satec.localgismobile.ui.LocalGISMainWindow;
import es.satec.localgismobile.ui.utils.ScreenUtils;

public class LoginScreenComposite extends Composite {

	private Label username = null;
	private Text textUsername = null;
	private Label password = null;
	private Text textPassword = null;
	private Button buttonLogin = null;
	private Composite composite = null;
	
	private static Logger logger = Global.getLoggerFor(LoginScreenComposite.class);

	public LoginScreenComposite(Composite parent, int style) {
		super(parent, style);
		//InputStream fondois = this.getClass().getClassLoader().getResourceAsStream(Config.prResources.getProperty("LoginScreenComposite.ImagenUsuario"));
		//Image imagenFondo = new Image(Display.getCurrent(), fondois);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		setLayout(gridLayout);
		setBackground(Config.COLOR_APLICACION);
		init();
		  
	}

	private void init() {
		
		composite = new Composite(this,SWT.NONE);
		GridData gridData2 = new GridData();
		gridData2.horizontalAlignment = GridData.CENTER;
		gridData2.grabExcessHorizontalSpace = true;
		gridData2.grabExcessVerticalSpace = true;
		gridData2.verticalAlignment = GridData.CENTER;
		GridData gridData1 = new GridData();
		gridData1.horizontalSpan = 2;
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.numColumns = 2;
		composite.setLayout(gridLayout1);
		composite.setLayoutData(gridData2);
		composite.setBackground(Config.COLOR_APLICACION);
		
		GridData gridData3 = new GridData();
		gridData3.horizontalSpan = 2;
		gridData3.horizontalAlignment = GridData.FILL;
		gridData3.verticalAlignment = GridData.CENTER;
		gridData3.grabExcessHorizontalSpace = true;
		
		Label imagenUsername = new Label(composite, SWT.NONE);
		
		
		InputStream useris = this.getClass().getClassLoader().getResourceAsStream(Config.prResources.getProperty("LoginScreenComposite.ImagenUsuario"));
		Image imagenUser = new Image(Display.getCurrent(), useris);
		imagenUsername.setImage(imagenUser);
		imagenUsername.setBackground(Config.COLOR_APLICACION);
		username = new Label(composite, SWT.NONE);
		username.setText(Messages.getMessage("Username"));
		username.setBackground(Config.COLOR_APLICACION);
		//username.setBackgroundImage(imagenFondo);

		textUsername = new Text(composite, SWT.BORDER);
		textUsername.setLayoutData(gridData3);
		if (Global.AUTOLOGIN)
			textUsername.setText("pda");
		
		Label imagenPassword = new Label(composite, SWT.NONE);
		InputStream passwordis = this.getClass().getClassLoader().getResourceAsStream(Config.prResources.getProperty("LoginScreenComposite.ImagenPassword"));
		Image imagPassword = new Image(Display.getCurrent(), passwordis);
		imagenPassword.setImage(imagPassword);
		imagenPassword.setBackground(Config.COLOR_APLICACION);
		password = new Label(composite, SWT.NONE);
		password.setText(Messages.getMessage("Password"));
		password.setBackground(Config.COLOR_APLICACION);
		//password.setBackgroundImage(imagenFondo);

		textPassword = new Text(composite, SWT.BORDER);
		textPassword.setEchoChar('*');
		textPassword.setLayoutData(gridData3);
		if (Global.AUTOLOGIN)
			textPassword.setText("pda");
		
		
//		BORRAR
		//textUsername.setText("satec_pro");
		//textPassword.setText("");
		
		GridData gridData4 = new GridData();
		gridData4.horizontalSpan = 2;
		gridData4.horizontalAlignment = GridData.CENTER;
		
		buttonLogin = new Button(composite, SWT.NONE);
		buttonLogin.setText(Messages.getMessage("LogIn"));
		buttonLogin.setLayoutData(gridData4);
		//Cuando seleccion el boton. pasara a otra ventana
		
		buttonLogin.addListener(SWT.Selection, new Listener(){

			public void handleEvent(Event event) {
				Shell mainShell = LocalGISMobile.getMainWindow().getShell();
				ScreenUtils.startHourGlass(mainShell);
				try {
					logger.info("Conectando contra:"+Config.prLocalgis.getProperty(Config.PROPERTY_SERVER_HOST)+":"+Config.prLocalgis.getPropertyAsInt(Config.PROPERTY_SERVER_PORT, 80));
					ValidationFacade validationFacade = new ValidationFacade();
					validationFacade.valida(Config.prLocalgis.getProperty(Config.PROPERTY_SERVER_PROTOCOL),
						Config.prLocalgis.getProperty(Config.PROPERTY_SERVER_HOST),
						Config.prLocalgis.getPropertyAsInt(Config.PROPERTY_SERVER_PORT, 80),
						Config.prLocalgis.getProperty(Config.PROPERTY_SERVER_CONTEXT),
						textUsername.getText(), textPassword.getText());
					SessionInfo.getInstance().setValidationBean(Global.getValidationBean());
					goToNextScreen();
					
				} catch (RolesException e) {
					logger.error(e);
					ScreenUtils.stopHourGlass(mainShell);
					MessageBox mb = new MessageBox(LocalGISMobile.getMainWindow().getShell(), SWT.ICON_ERROR);
					mb.setMessage(Messages.getMessage("errores.rolesException"));
					mb.open();
				} catch (LoginCancelException e) {
					logger.error(e);
					ScreenUtils.stopHourGlass(mainShell);
					MessageBox mb = new MessageBox(LocalGISMobile.getMainWindow().getShell(), SWT.ICON_ERROR);
					mb.setMessage(Messages.getMessage("errores.loginCancelException"));
					mb.open();
				} catch (ValidationException e) {
					logger.error(e);
					ScreenUtils.stopHourGlass(mainShell);
					MessageBox mb = new MessageBox(LocalGISMobile.getMainWindow().getShell(), SWT.ICON_ERROR);
					mb.setMessage(Messages.getMessage("errores.validationException"));
					mb.open();
				}
			}
			
		});
		
		//TEST
		//textUsername.setText("satec_dev");
		//textPassword.setText("satec12345");
	}
	public Button getCheck(){
		return buttonLogin;
	}
	
	private void goToNextScreen() {
		Shell currentShell = this.getShell();
		Shell mainShell = LocalGISMobile.getMainWindow().getShell();
		if (currentShell == mainShell) {
			LocalGISMobile.getMainWindow().go(LocalGISMainWindow.MENU_SCREEN_COMPOSITE);
		}
		else {
			currentShell.close();
			ScreenUtils.stopHourGlass(mainShell);
		}
	}
}
