/**
 * FileValidationException.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is licensed and may be used, modified and redistributed under the terms of the European Public License (EUPL), either version 1.1 or (at your option) any later version as soon as they are approved by the European Commission.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and more details.
 * You should have received a copy of the EUPL1.1 license along with this program; if not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */
package es.satec.localgismobile.fw.validation.exceptions;


public class FileValidationException extends ValidationException {

	 public FileValidationException(String message) {
         // Constructor.  Create a ParseError object containing
         // the given message as its error message.
      super(message);
	 }	
	 
		 
	 public FileValidationException(Exception e){
			super(e.getMessage());
		}	
	
	 public FileValidationException() {
         // Constructor.  Create a ParseError object containing
         // the given message as its error message.
      super("Se ha producido un error con los ficheros de validación");
   }		

}
