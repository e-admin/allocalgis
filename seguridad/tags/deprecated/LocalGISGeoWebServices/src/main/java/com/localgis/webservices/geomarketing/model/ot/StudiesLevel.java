package com.localgis.webservices.geomarketing.model.ot;

import java.io.Serializable;

public class StudiesLevel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4034771698387044633L;
	public static String N10_NO_SABE_LEER_NI_ESCRIBIR = "10";
	public static String N11_NO_SABE_LEER_NI_ESCRIBIR = "11";
	public static String N20_TITULACION_INFERIOR_AL_GRADO_DE_ESCOLARIDAD = "20";
	public static String N21_SIN_ESTUDIOS = "21";
	public static String N22_ENSENANZA_PRIMARIA_INCOMPLETA_CINCO_CURSOS_DE_EGB_O_EQUIVALENTE = "22";
	public static String N30_GRADUADO_ESCOLAR_O_EQUIVALENTE = "30";
	public static String N31_BACHILLER_ELEMENTAL_GRADUADO_ESCOLAR_EGB_COMPLETA_PRIMARIA_COMPLETA_CERTIFICADO_DE_ESCOLARIDAD_O_EQUIVALENTE = "31";
	public static String N32_FORMACION_PROFESIONAL_PRIMER_GRADO_OFICIALIA_INDUSTRIAL = "32";
	public static String N40_BACHILLER_FORMACION_PROFESIONAL_DE_2_GRADO_O_TITULOS_EQUIVALENTES_O_SUPERIORES = "40";
	public static String N41_FORMACION_PROFESIONAL_SEGUNDO_GRADO_MAESTRIA_INDUSTRIAL = "41";
	public static String N42_BACHILLER_SUPERIOR_BUP = "42";
	public static String N43_OTROS_TITULADOS_MEDIOS_AUXILIAR_DE_CLINICA_SECRETARIADO_PROGRAMADOR_INFORMATICO_AUXILIAR_DE_VUELO_DIPLOMADO_EN_ARTES_Y_OFICIOS_ETC = "43";
	public static String N44_DIPLOMADO_DE_ESCUELAS_UNIVERSITARIAS_EMPRESARIALES_PROFESORADO_DE_EGB_ATS_Y_SIMILARES = "44";
	public static String N45_ARQUITECTO_O_INGENIERO_TECNICO = "45";
	public static String N46_LICENCIADO_UNIVERSITARIO_ARQUITECTO_O_INGENIERO_SUPERIOR = "46";
	public static String N47_TITULADOS_DE_ESTUDIOS_SUPERIORES_NO_UNIVERSITARIOS = "47";
	public static String N48_DOCTORADO_Y_ESTUDIOS_DE_POSTGRADO_O_ESPECIALIZACION_PARA_LICENCIADOS = "48";
	
	public static String S10_NO_SABE_LEER_NI_ESCRIBIR = "S10";
	public static String S11_NO_SABE_LEER_NI_ESCRIBIR = "S11";
	public static String S20_TITULACION_INFERIOR_AL_GRADO_DE_ESCOLARIDAD = "S20";
	public static String S21_SIN_ESTUDIOS = "S21";
	public static String S22_ENSENANZA_PRIMARIA_INCOMPLETA_CINCO_CURSOS_DE_EGB_O_EQUIVALENTE = "S22";
	public static String S30_GRADUADO_ESCOLAR_O_EQUIVALENTE = "S30";
	public static String S31_BACHILLER_ELEMENTAL_GRADUADO_ESCOLAR_EGB_COMPLETA_PRIMARIA_COMPLETA_CERTIFICADO_DE_ESCOLARIDAD_O_EQUIVALENTE = "S31";
	public static String S32_FORMACION_PROFESIONAL_PRIMER_GRADO_OFICIALIA_INDUSTRIAL = "S32";
	public static String S40_BACHILLER_FORMACION_PROFESIONAL_DE_2_GRADO_O_TITULOS_EQUIVALENTES_O_SUPERIORES = "S40";
	public static String S41_FORMACION_PROFESIONAL_SEGUNDO_GRADO_MAESTRIA_INDUSTRIAL = "S41";
	public static String S42_BACHILLER_SUPERIOR_BUP = "S42";
	public static String S43_OTROS_TITULADOS_MEDIOS_AUXILIAR_DE_CLINICA_SECRETARIADO_PROGRAMADOR_INFORMATICO_AUXILIAR_DE_VUELO_DIPLOMADO_EN_ARTES_Y_OFICIOS_ETC = "S43";
	public static String S44_DIPLOMADO_DE_ESCUELAS_UNIVERSITARIAS_EMPRESARIALES_PROFESORADO_DE_EGB_ATS_Y_SIMILARES = "S44";
	public static String S45_ARQUITECTO_O_INGENIERO_TECNICO = "S45";
	public static String S46_LICENCIADO_UNIVERSITARIO_ARQUITECTO_O_INGENIERO_SUPERIOR = "S46";
	public static String S47_TITULADOS_DE_ESTUDIOS_SUPERIORES_NO_UNIVERSITARIOS = "S47";
	public static String S48_DOCTORADO_Y_ESTUDIOS_DE_POSTGRADO_O_ESPECIALIZACION_PARA_LICENCIADOS = "S48";
	
	
	private Integer i10NoSabeLeerNiEscribir;
	private Integer i11NoSabeLeerNiEscribir;
	private Integer i20TitulacioninferiorAlGradoDeEscolaridad;
	private Integer i21SinEstudios;
	private Integer i22EnsenanzaPrimariaIncompletaCincoCursosDeEgbOEquivalente;
	private Integer i30GraduadoEscolarOEquivalente;
	private Integer i31BachillerElemental;
	private Integer i32FormacionProfesionalPrimerGradoOficialIndustrial;
	private Integer i40BachillerFormacionProfesionalDe2GradoOTitulosEquivalentesOSuperiores;
	private Integer i41FormacionProfesionalSegundoGradoMaestriaIndustrial;
	private Integer i42BachillerSuperiorBup;
	private Integer i43OtrosTituladosMediosAuxiliarDeClinicaSecretariadoProgramadorInformaticoAuxiliarDeVueloDiplomadoEnArtesYOficiosEtc;
	private Integer i44DiplomadoDeEscuelasUniversitariasEmpresarialesProfesoradoDeEgbAtsYSimilares;
	private Integer i45ArquitectoOIngenieroTecnico;
	private Integer i46LicenciadoUniversitarioArquitectoOIngenieroSuperior;
	private Integer i47TituladosDeEstudiosSuperioresNoUniversitarios;
	private Integer i48DoctoradoYEstudiosDePostgradoOEspecializacionParaLicenciados;
	
	public String getColumnsQuery(){
		String sqlColumnsQuery = "";
		sqlColumnsQuery += "SUM(CASE WHEN habitantes.ocupacion = " + StudiesLevel.N10_NO_SABE_LEER_NI_ESCRIBIR + "::text THEN 1 ELSE 0 END) AS " + StudiesLevel.S10_NO_SABE_LEER_NI_ESCRIBIR+",";
		sqlColumnsQuery += "SUM(CASE WHEN habitantes.ocupacion = " + StudiesLevel.N11_NO_SABE_LEER_NI_ESCRIBIR + "::text THEN 1 ELSE 0 END) AS " + StudiesLevel.S11_NO_SABE_LEER_NI_ESCRIBIR+",";
		sqlColumnsQuery += "SUM(CASE WHEN habitantes.ocupacion = " + StudiesLevel.N20_TITULACION_INFERIOR_AL_GRADO_DE_ESCOLARIDAD + "::text THEN 1 ELSE 0 END) AS " + StudiesLevel.S20_TITULACION_INFERIOR_AL_GRADO_DE_ESCOLARIDAD+",";
		sqlColumnsQuery += "SUM(CASE WHEN habitantes.ocupacion = " + StudiesLevel.N21_SIN_ESTUDIOS + "::text THEN 1 ELSE 0 END) AS " + StudiesLevel.S21_SIN_ESTUDIOS+",";
		sqlColumnsQuery += "SUM(CASE WHEN habitantes.ocupacion = " + StudiesLevel.N22_ENSENANZA_PRIMARIA_INCOMPLETA_CINCO_CURSOS_DE_EGB_O_EQUIVALENTE + "::text THEN 1 ELSE 0 END) AS " + StudiesLevel.S22_ENSENANZA_PRIMARIA_INCOMPLETA_CINCO_CURSOS_DE_EGB_O_EQUIVALENTE+",";
		sqlColumnsQuery += "SUM(CASE WHEN habitantes.ocupacion = " + StudiesLevel.N30_GRADUADO_ESCOLAR_O_EQUIVALENTE + "::text THEN 1 ELSE 0 END) AS " + StudiesLevel.S30_GRADUADO_ESCOLAR_O_EQUIVALENTE+",";
		sqlColumnsQuery += "SUM(CASE WHEN habitantes.ocupacion = " + StudiesLevel.N31_BACHILLER_ELEMENTAL_GRADUADO_ESCOLAR_EGB_COMPLETA_PRIMARIA_COMPLETA_CERTIFICADO_DE_ESCOLARIDAD_O_EQUIVALENTE + "::text THEN 1 ELSE 0 END) AS " + StudiesLevel.S31_BACHILLER_ELEMENTAL_GRADUADO_ESCOLAR_EGB_COMPLETA_PRIMARIA_COMPLETA_CERTIFICADO_DE_ESCOLARIDAD_O_EQUIVALENTE+",";
		sqlColumnsQuery += "SUM(CASE WHEN habitantes.ocupacion = " + StudiesLevel.N32_FORMACION_PROFESIONAL_PRIMER_GRADO_OFICIALIA_INDUSTRIAL + "::text THEN 1 ELSE 0 END) AS " + StudiesLevel.S32_FORMACION_PROFESIONAL_PRIMER_GRADO_OFICIALIA_INDUSTRIAL+",";
		sqlColumnsQuery += "SUM(CASE WHEN habitantes.ocupacion = " + StudiesLevel.N40_BACHILLER_FORMACION_PROFESIONAL_DE_2_GRADO_O_TITULOS_EQUIVALENTES_O_SUPERIORES + "::text THEN 1 ELSE 0 END) AS " + StudiesLevel.S40_BACHILLER_FORMACION_PROFESIONAL_DE_2_GRADO_O_TITULOS_EQUIVALENTES_O_SUPERIORES+",";
		sqlColumnsQuery += "SUM(CASE WHEN habitantes.ocupacion = " + StudiesLevel.N41_FORMACION_PROFESIONAL_SEGUNDO_GRADO_MAESTRIA_INDUSTRIAL + "::text THEN 1 ELSE 0 END) AS " + StudiesLevel.S41_FORMACION_PROFESIONAL_SEGUNDO_GRADO_MAESTRIA_INDUSTRIAL+",";
		sqlColumnsQuery += "SUM(CASE WHEN habitantes.ocupacion = " + StudiesLevel.N42_BACHILLER_SUPERIOR_BUP + "::text THEN 1 ELSE 0 END) AS " + StudiesLevel.S42_BACHILLER_SUPERIOR_BUP+",";
		sqlColumnsQuery += "SUM(CASE WHEN habitantes.ocupacion = " + StudiesLevel.N43_OTROS_TITULADOS_MEDIOS_AUXILIAR_DE_CLINICA_SECRETARIADO_PROGRAMADOR_INFORMATICO_AUXILIAR_DE_VUELO_DIPLOMADO_EN_ARTES_Y_OFICIOS_ETC + "::text THEN 1 ELSE 0 END) AS " + StudiesLevel.S43_OTROS_TITULADOS_MEDIOS_AUXILIAR_DE_CLINICA_SECRETARIADO_PROGRAMADOR_INFORMATICO_AUXILIAR_DE_VUELO_DIPLOMADO_EN_ARTES_Y_OFICIOS_ETC+",";
		sqlColumnsQuery += "SUM(CASE WHEN habitantes.ocupacion = " + StudiesLevel.N44_DIPLOMADO_DE_ESCUELAS_UNIVERSITARIAS_EMPRESARIALES_PROFESORADO_DE_EGB_ATS_Y_SIMILARES + "::text THEN 1 ELSE 0 END) AS " + StudiesLevel.S44_DIPLOMADO_DE_ESCUELAS_UNIVERSITARIAS_EMPRESARIALES_PROFESORADO_DE_EGB_ATS_Y_SIMILARES+",";
		sqlColumnsQuery += "SUM(CASE WHEN habitantes.ocupacion = " + StudiesLevel.N45_ARQUITECTO_O_INGENIERO_TECNICO + "::text THEN 1 ELSE 0 END) AS " + StudiesLevel.S45_ARQUITECTO_O_INGENIERO_TECNICO+",";
		sqlColumnsQuery += "SUM(CASE WHEN habitantes.ocupacion = " + StudiesLevel.N46_LICENCIADO_UNIVERSITARIO_ARQUITECTO_O_INGENIERO_SUPERIOR + "::text THEN 1 ELSE 0 END) AS " + StudiesLevel.S46_LICENCIADO_UNIVERSITARIO_ARQUITECTO_O_INGENIERO_SUPERIOR+",";
		sqlColumnsQuery += "SUM(CASE WHEN habitantes.ocupacion = " + StudiesLevel.N47_TITULADOS_DE_ESTUDIOS_SUPERIORES_NO_UNIVERSITARIOS + "::text THEN 1 ELSE 0 END) AS " + StudiesLevel.S47_TITULADOS_DE_ESTUDIOS_SUPERIORES_NO_UNIVERSITARIOS+",";
		sqlColumnsQuery += "SUM(CASE WHEN habitantes.ocupacion = " + StudiesLevel.N48_DOCTORADO_Y_ESTUDIOS_DE_POSTGRADO_O_ESPECIALIZACION_PARA_LICENCIADOS + "::text THEN 1 ELSE 0 END) AS " + StudiesLevel.S48_DOCTORADO_Y_ESTUDIOS_DE_POSTGRADO_O_ESPECIALIZACION_PARA_LICENCIADOS;
		return sqlColumnsQuery;
	}
	
	public Integer getI10NoSabeLeerNiEscribir() {
		return i10NoSabeLeerNiEscribir;
	}
	public void setI10NoSabeLeerNiEscribir(Integer i10NoSabeLeerNiEscribir) {
		this.i10NoSabeLeerNiEscribir = i10NoSabeLeerNiEscribir;
	}
	public Integer getI11NoSabeLeerNiEscribir() {
		return i11NoSabeLeerNiEscribir;
	}
	public void setI11NoSabeLeerNiEscribir(Integer i11NoSabeLeerNiEscribir) {
		this.i11NoSabeLeerNiEscribir = i11NoSabeLeerNiEscribir;
	}
	public Integer getI20TitulacioninferiorAlGradoDeEscolaridad() {
		return i20TitulacioninferiorAlGradoDeEscolaridad;
	}
	public void setI20TitulacioninferiorAlGradoDeEscolaridad(
			Integer i20TitulacioninferiorAlGradoDeEscolaridad) {
		this.i20TitulacioninferiorAlGradoDeEscolaridad = i20TitulacioninferiorAlGradoDeEscolaridad;
	}
	public Integer getI21SinEstudios() {
		return i21SinEstudios;
	}
	public void setI21SinEstudios(Integer i21SinEstudios) {
		this.i21SinEstudios = i21SinEstudios;
	}
	public Integer getI22EnsenanzaPrimariaIncompletaCincoCursosDeEgbOEquivalente() {
		return i22EnsenanzaPrimariaIncompletaCincoCursosDeEgbOEquivalente;
	}
	public void setI22EnsenanzaPrimariaIncompletaCincoCursosDeEgbOEquivalente(
			Integer i22EnsenanzaPrimariaIncompletaCincoCursosDeEgbOEquivalente) {
		this.i22EnsenanzaPrimariaIncompletaCincoCursosDeEgbOEquivalente = i22EnsenanzaPrimariaIncompletaCincoCursosDeEgbOEquivalente;
	}
	public Integer getI30GraduadoEscolarOEquivalente() {
		return i30GraduadoEscolarOEquivalente;
	}
	public void setI30GraduadoEscolarOEquivalente(
			Integer i30GraduadoEscolarOEquivalente) {
		this.i30GraduadoEscolarOEquivalente = i30GraduadoEscolarOEquivalente;
	}
	public Integer getI31BachillerElemental() {
		return i31BachillerElemental;
	}
	public void setI31BachillerElemental(Integer i31BachillerElemental) {
		this.i31BachillerElemental = i31BachillerElemental;
	}
	public Integer getI32FormacionProfesionalPrimerGradoOficialIndustrial() {
		return i32FormacionProfesionalPrimerGradoOficialIndustrial;
	}
	public void setI32FormacionProfesionalPrimerGradoOficialIndustrial(
			Integer i32FormacionProfesionalPrimerGradoOficialIndustrial) {
		this.i32FormacionProfesionalPrimerGradoOficialIndustrial = i32FormacionProfesionalPrimerGradoOficialIndustrial;
	}
	public Integer getI40BachillerFormacionProfesionalDe2GradoOTitulosEquivalentesOSuperiores() {
		return i40BachillerFormacionProfesionalDe2GradoOTitulosEquivalentesOSuperiores;
	}
	public void setI40BachillerFormacionProfesionalDe2GradoOTitulosEquivalentesOSuperiores(
			Integer i40BachillerFormacionProfesionalDe2GradoOTitulosEquivalentesOSuperiores) {
		this.i40BachillerFormacionProfesionalDe2GradoOTitulosEquivalentesOSuperiores = i40BachillerFormacionProfesionalDe2GradoOTitulosEquivalentesOSuperiores;
	}
	public Integer getI41FormacionProfesionalSegundoGradoMaestriaIndustrial() {
		return i41FormacionProfesionalSegundoGradoMaestriaIndustrial;
	}
	public void setI41FormacionProfesionalSegundoGradoMaestriaIndustrial(
			Integer i41FormacionProfesionalSegundoGradoMaestriaIndustrial) {
		this.i41FormacionProfesionalSegundoGradoMaestriaIndustrial = i41FormacionProfesionalSegundoGradoMaestriaIndustrial;
	}
	public Integer getI42BachillerSuperiorBup() {
		return i42BachillerSuperiorBup;
	}
	public void setI42BachillerSuperiorBup(Integer i42BachillerSuperiorBup) {
		this.i42BachillerSuperiorBup = i42BachillerSuperiorBup;
	}
	public Integer getI43OtrosTituladosMediosAuxiliarDeClinicaSecretariadoProgramadorInformaticoAuxiliarDeVueloDiplomadoEnArtesYOficiosEtc() {
		return i43OtrosTituladosMediosAuxiliarDeClinicaSecretariadoProgramadorInformaticoAuxiliarDeVueloDiplomadoEnArtesYOficiosEtc;
	}
	public void setI43OtrosTituladosMediosAuxiliarDeClinicaSecretariadoProgramadorInformaticoAuxiliarDeVueloDiplomadoEnArtesYOficiosEtc(
			Integer i43OtrosTituladosMediosAuxiliarDeClinicaSecretariadoProgramadorInformaticoAuxiliarDeVueloDiplomadoEnArtesYOficiosEtc) {
		this.i43OtrosTituladosMediosAuxiliarDeClinicaSecretariadoProgramadorInformaticoAuxiliarDeVueloDiplomadoEnArtesYOficiosEtc = i43OtrosTituladosMediosAuxiliarDeClinicaSecretariadoProgramadorInformaticoAuxiliarDeVueloDiplomadoEnArtesYOficiosEtc;
	}
	public Integer getI44DiplomadoDeEscuelasUniversitariasEmpresarialesProfesoradoDeEgbAtsYSimilares() {
		return i44DiplomadoDeEscuelasUniversitariasEmpresarialesProfesoradoDeEgbAtsYSimilares;
	}
	public void setI44DiplomadoDeEscuelasUniversitariasEmpresarialesProfesoradoDeEgbAtsYSimilares(
			Integer i44DiplomadoDeEscuelasUniversitariasEmpresarialesProfesoradoDeEgbAtsYSimilares) {
		this.i44DiplomadoDeEscuelasUniversitariasEmpresarialesProfesoradoDeEgbAtsYSimilares = i44DiplomadoDeEscuelasUniversitariasEmpresarialesProfesoradoDeEgbAtsYSimilares;
	}
	public Integer getI45ArquitectoOIngenieroTecnico() {
		return i45ArquitectoOIngenieroTecnico;
	}
	public void setI45ArquitectoOIngenieroTecnico(
			Integer i45ArquitectoOIngenieroTecnico) {
		this.i45ArquitectoOIngenieroTecnico = i45ArquitectoOIngenieroTecnico;
	}
	public Integer getI46LicenciadoUniversitarioArquitectoOIngenieroSuperior() {
		return i46LicenciadoUniversitarioArquitectoOIngenieroSuperior;
	}
	public void setI46LicenciadoUniversitarioArquitectoOIngenieroSuperior(
			Integer i46LicenciadoUniversitarioArquitectoOIngenieroSuperior) {
		this.i46LicenciadoUniversitarioArquitectoOIngenieroSuperior = i46LicenciadoUniversitarioArquitectoOIngenieroSuperior;
	}
	public Integer getI47TituladosDeEstudiosSuperioresNoUniversitarios() {
		return i47TituladosDeEstudiosSuperioresNoUniversitarios;
	}
	public void setI47TituladosDeEstudiosSuperioresNoUniversitarios(
			Integer i47TituladosDeEstudiosSuperioresNoUniversitarios) {
		this.i47TituladosDeEstudiosSuperioresNoUniversitarios = i47TituladosDeEstudiosSuperioresNoUniversitarios;
	}
	public Integer getI48DoctoradoYEstudiosDePostgradoOEspecializacionParaLicenciados() {
		return i48DoctoradoYEstudiosDePostgradoOEspecializacionParaLicenciados;
	}
	public void setI48DoctoradoYEstudiosDePostgradoOEspecializacionParaLicenciados(
			Integer i48DoctoradoYEstudiosDePostgradoOEspecializacionParaLicenciados) {
		this.i48DoctoradoYEstudiosDePostgradoOEspecializacionParaLicenciados = i48DoctoradoYEstudiosDePostgradoOEspecializacionParaLicenciados;
	}
	public Integer get10NoSabeLeerNiEscribir(){
		return getI10NoSabeLeerNiEscribir()+getI11NoSabeLeerNiEscribir();
	}
	public Integer get20TitulacionInferiorAlGradoDeEscolaridad(){
		return getI20TitulacioninferiorAlGradoDeEscolaridad()+getI21SinEstudios()+getI22EnsenanzaPrimariaIncompletaCincoCursosDeEgbOEquivalente();
	}
	public Integer get30GraduadoEscolarOEquivalente(){
		return getI30GraduadoEscolarOEquivalente()+getI31BachillerElemental()+getI32FormacionProfesionalPrimerGradoOficialIndustrial();
	}
	public Integer get40BachillerFormacionProfesionalDe2GradoOTitulosEquivalentesOSuperiores(){
		return getI40BachillerFormacionProfesionalDe2GradoOTitulosEquivalentesOSuperiores()+getI41FormacionProfesionalSegundoGradoMaestriaIndustrial()+getI42BachillerSuperiorBup()+getI43OtrosTituladosMediosAuxiliarDeClinicaSecretariadoProgramadorInformaticoAuxiliarDeVueloDiplomadoEnArtesYOficiosEtc()+getI44DiplomadoDeEscuelasUniversitariasEmpresarialesProfesoradoDeEgbAtsYSimilares()+getI45ArquitectoOIngenieroTecnico()+getI46LicenciadoUniversitarioArquitectoOIngenieroSuperior()+getI47TituladosDeEstudiosSuperioresNoUniversitarios()+getI48DoctoradoYEstudiosDePostgradoOEspecializacionParaLicenciados();
	}
}
