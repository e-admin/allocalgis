/**
 * SearchResultsScreen.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is licensed and may be used, modified and redistributed under the terms of the European Public License (EUPL), either version 1.1 or (at your option) any later version as soon as they are approved by the European Commission.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and more details.
 * You should have received a copy of the EUPL1.1 license along with this program; if not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */
package es.satec.localgismobile.ui.screens;

import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.tinyline.svg.SVGNode;

import es.satec.localgismobile.fw.Config;
import es.satec.localgismobile.fw.Messages;
import es.satec.localgismobile.ui.LocalGISWindow;

public class SearchResultsScreen extends LocalGISWindow{

	private Table table = null;
	private Button buttonVerMapa = null;
	private Vector inforBusq=null;
	private Text text = null;
	private String layer=null;
 
	
	public SearchResultsScreen(Shell parent, Vector infor, String string){
		super(parent);
		this.inforBusq=infor;
		this.layer=string;
		shell.setBackground(Config.COLOR_APLICACION);
		 
		init();
		show();	
	}
	
	/**
	 * This method initializes sShell
	 */
	private void init() {
		GridData gridData2 = new GridData();
		gridData2.horizontalAlignment = GridData.CENTER;
		gridData2.grabExcessHorizontalSpace = true;
		gridData2.verticalAlignment = GridData.CENTER;
		GridData gridData1 = new GridData();
		gridData1.horizontalAlignment = GridData.CENTER;
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.verticalAlignment = GridData.CENTER;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalSpan = 2;
		gridData.verticalAlignment = GridData.FILL;
		shell.setText(layer);
		shell.setLayout(gridLayout);
		//shell.setSize(Display.getDefault().getClientArea().width,Display.getDefault().getClientArea().height);
		table = new Table(shell, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		table.setHeaderVisible(true);
		table.setLayoutData(gridData);
		buttonVerMapa = new Button(shell, SWT.NONE);
		buttonVerMapa.setLayoutData(gridData2);
		buttonVerMapa.setText(Messages.getMessage("SearchResultsScreen_VerMapa"));
		 
		cargaDatos();
	}
	public Button getButtonVisualizar(){
		return buttonVerMapa;
	}

	private void cargaDatos() {

		if(inforBusq.size()!=0){
			//selecciono el primer nodo para ver en su padre el nombre de los atributos
			SVGNode node = (SVGNode)inforBusq.elementAt(0);
			//Se va a sacar la infomacion de los atributos de sus padre
			Vector parentAtts=node.parent.nameAtts;
			for(int par=0;par<parentAtts.size();par++){
				TableColumn column1 = new TableColumn(table, SWT.NONE);
				column1.setText((String) parentAtts.elementAt(par));
				column1.pack();
			}
			for(int i=0;i<inforBusq.size();i++){
				SVGNode node1 = (SVGNode)inforBusq.elementAt(i);
				Vector atts=node1.nameAtts;
				TableItem fila = new TableItem(table, SWT.NONE);
				String [] datos= new String [atts.size()];
				for(int par=0;par<atts.size();par++){
					datos[par]=(String) atts.elementAt(par);
				}
				fila.setText(datos);
			}
		}
		else{
			GridData gridData21 = new GridData();
			gridData21.horizontalAlignment = GridData.FILL;
			gridData21.horizontalSpan = 2;
			gridData21.verticalAlignment = GridData.CENTER;
			buttonVerMapa.setEnabled(true);
			text = new Text(shell, SWT.BORDER);
			text.setLayoutData(gridData21);
			text.setText(Messages.getMessage("SearchResultsScreen_Mensaje"));
			buttonVerMapa.setEnabled(false);
		}
	}

}
