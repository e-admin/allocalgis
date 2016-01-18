/**
 * UriRegexCondition.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package test;

import es.satec.regexp.Pattern;

	public   class UriRegexCondition

	   {

	       private Pattern pattern;

	       public boolean matches(String uri)
	       {


	           if(uri == null)
	               return false;
	           else
	               return pattern.matcher(uri).lookingAt();
	       }

	       public String toString()
	       {
	           return "UriRegexCondition[" + pattern.pattern() + "]";
	       }

	       public UriRegexCondition(Pattern pattern)
	       {
	           this.pattern = pattern;
	       }
	   }
