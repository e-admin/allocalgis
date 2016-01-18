/**
 * GeoMarketingOT2.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.webservices.geomarketing;


import java.io.Serializable;

public class GeoMarketingOT2 implements Serializable{
	
		/**
		 * 
		 */
		private static final long serialVersionUID = -3698476635979670868L;
		private Integer numHabitants;
		private Integer numMales;
		private Integer numFemales;
		private String ranges; 
		private String s10;
		private String s20;
		private String s30;
		private String s40;
		private Integer spanishHabitants;
		private Integer foreignHabitants;
		private Integer externalValue;
		private String municipio;
		private String attName;
		private Integer[] idFeature;
		public String getMunicipio() {
			return municipio;
		}

		public void setMunicipio(String municipio) {
			this.municipio = municipio;
		}

		public String getAttName() {
			return attName;
		}

		public void setAttName(String attName) {
			this.attName = attName;
		}

		public String getS10() {
			return s10;
		}

		public void setS10(String s10) {
			this.s10 = s10;
		}

		public String getS20() {
			return s20;
		}

		public void setS20(String s20) {
			this.s20 = s20;
		}

		public String getS30() {
			return s30;
		}

		public void setS30(String s30) {
			this.s30 = s30;
		}

		public String getS40() {
			return s40;
		}

		public void setS40(String s40) {
			this.s40 = s40;
		}

		public void setRanges(String ranges) {
			this.ranges = ranges;
		}
		public String getRanges() {
			return this.ranges;
		}
		
		
		public Integer getNumFemales() {
			return numFemales;
		}

		public void setNumFemales(Integer numFemales) {
			this.numFemales = numFemales;
		}

		public Integer getNumHabitants() {
			return numHabitants;
		}

		public void setNumHabitants(Integer numHabitants) {
			this.numHabitants = numHabitants;
		}

		public void setNumMales(Integer numMales) {
			this.numMales = numMales;
		}

		public Integer getNumMales() {
			return numMales;
		}

		public Integer getSpanishHabitants(){
			return this.spanishHabitants;
		}
		public void setSpanishHabitants(Integer spanishHabitants) {
			this.spanishHabitants = spanishHabitants;
			
		}
		public Integer getForeignHabitants(){
			return this.foreignHabitants;
		}
		public void setForeignHabitants(Integer foreignHabitants) {
			this.foreignHabitants = foreignHabitants;
		}

		public void setExternalValue(Integer externalValue) {
			this.externalValue = externalValue;
		}

		public Integer getExternalValue() {
			return externalValue;
		}
		public Integer[] getIdFeature(){
			return this.idFeature;
		}
		public void setIdFeature(Integer[] idFeature) {
			this.idFeature = idFeature;
			
		}
	}


