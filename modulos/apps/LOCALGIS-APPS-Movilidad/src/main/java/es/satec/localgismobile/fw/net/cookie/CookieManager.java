/**
 * CookieManager.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is licensed and may be used, modified and redistributed under the terms of the European Public License (EUPL), either version 1.1 or (at your option) any later version as soon as they are approved by the European Commission.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and more details.
 * You should have received a copy of the EUPL1.1 license along with this program; if not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */
package es.satec.localgismobile.fw.net.cookie;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class CookieManager {

	private Hashtable domainCookies;

	private static CookieManager cm;

	private CookieManager() {
		domainCookies = new Hashtable();
	}

	/* Añade una cookie nueva, la almacena en en memoria */
	public void add(Cookie cookie, String domain, String path) {
		Hashtable domainPaths = (Hashtable) domainCookies.get(domain);
		if (domainPaths == null){
			domainPaths = new Hashtable();
			domainCookies.put(domain, domainPaths);
		}
		Vector cookies = (Vector) domainPaths.get(path);
		if (domainPaths.get(path) == null){
			cookies = new Vector();			
			domainPaths.put(path, cookies);
		}
		cookies.addElement(cookie);
	}

	/*
	 * Obtiene una cookie para un dominio pasado como parametro, si la cookie no existe
	 *  devolver null
	 */
	public Vector get(String domain, String uri) {
		Hashtable domainPaths = (Hashtable) domainCookies.get(domain);
		
		if (domainPaths == null)
			return null;
		
		Enumeration pathList = domainPaths.keys();
		
		int domainLength = domain.length();
		String path;
		int pathLength;
		String currentMatchedPath = null;
		int currentMatchedPathLength = -1; 

		Vector matchedPathsList = new Vector();
		boolean pathMatched;
		
		while(pathList.hasMoreElements()){
			path = (String) pathList.nextElement();
			
			pathLength = path.length();

			pathMatched = uri.startsWith(path, domainLength);
			if (pathMatched && (uri.length() > domainLength + pathLength) && !path.equals("/")){
				if (uri.charAt(domainLength + pathLength) != '/'){
					pathMatched = false;
				}
			}
			if (pathMatched){
				matchedPathsList.addElement(path);
			}
		}
		
		int numPathsMatched = matchedPathsList.size(); 
		if (numPathsMatched > 0){
			Vector cookies = new Vector();
			for (int i = 0; i < numPathsMatched; i++){
				String matchedPath = (String) matchedPathsList.elementAt(i);
				Vector pathCookies = (Vector) domainPaths.get(matchedPath);
				int numPathCookies = pathCookies.size();
				for (int j = 0; j < numPathCookies; j++){
					cookies.addElement(pathCookies.elementAt(j));
				}
			}
			return cookies;
		}
		else {
			return null;
		}
	}

	public void clear() {
		domainCookies.clear();
	}
	
	public void clearSiteCookies(String domain){
		domainCookies.put(domain, new Hashtable());
	}
	
	public void clearSitePathCookies(String domain, String path){
		Hashtable domainPaths = (Hashtable) domainCookies.get(domain);
		if (domainPaths != null){
			domainPaths.put(path, new Vector());
		}
	}

	/* Metodo de instancia para Singleton */
	public static CookieManager getInstance() {
		if (cm == null) {
			cm = new CookieManager();
		}
		return cm;
	}
}