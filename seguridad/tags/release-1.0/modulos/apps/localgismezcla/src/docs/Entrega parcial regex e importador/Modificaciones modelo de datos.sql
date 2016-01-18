--DROP TABLE Bien_Inmueble CASCADE;
--DROP TABLE Bloque_Reparto;
--DROP TABLE Catastro_Temporal CASCADE;
--DROP TABLE CNTPonUrb CASCADE;
--DROP TABLE Configuracion CASCADE;
--DROP TABLE Construccion CASCADE;
--DROP TABLE Cultivos CASCADE;
--DROP TABLE Derechos CASCADE;
--DROP TABLE Domicilio_Localizacion CASCADE;
--DROP TABLE Entidad_Generadora CASCADE;
--DROP TABLE Estado_Expediente CASCADE;
--DROP TABLE Estado_Siguiente CASCADE;
--DROP TABLE Expediente CASCADE;
--DROP TABLE Expediente_Bieninmueble CASCADE;
--DROP TABLE Expediente_Fichero CASCADE;
--DROP TABLE Expediente_Finca_Catastro CASCADE;
--DROP TABLE Ficheros CASCADE;
--DROP TABLE FXCC CASCADE;
--DROP TABLE Paraje CASCADE;
--DROP TABLE Persona CASCADE;
--DROP TABLE Ponencia_Poligono CASCADE;
--DROP TABLE Ponencia_Tramos CASCADE;
--DROP TABLE Ponencia_Urbanistica CASCADE;
--DROP TABLE Ponencia_ZonaValor CASCADE;
--DROP TABLE RepartosConsBI CASCADE;
--DROP TABLE RepartosConstrucciones CASCADE;
--DROP TABLE RepartosCultivos CASCADE;
--DROP TABLE Representante CASCADE;
--DROP TABLE RuEvaluatorio CASCADE;
--DROP TABLE Suelo CASCADE;
--DROP TABLE Unidad_Constructiva CASCADE;
--DROP TABLE Tipo_Expediente CASCADE;


---


CREATE TABLE Bien_Inmueble (
	IDentificador VARCHAR(20) NOT NULL,
	Anno_Expediente NUMERIC(4) NULL,
	Referencia_Expediente VARCHAR(13) NULL,
	Codigo_Entidad_Generadora VARCHAR(3) NULL,
	Tipo_Movimiento VARCHAR(1) NULL,
	Clase_BienInmueble VARCHAR(2) NULL,
	Parcela_Catastral VARCHAR(14) NULL,
	Numero_Cargo VARCHAR(4) NULL,
	Primer_Caracter_Control VARCHAR(1) NULL,
	Segundo_Caracter_Control VARCHAR(1) NULL,
	Numero_Fijo_Inmueble NUMERIC(8) NULL,
	Identificacion_BienInmueble VARCHAR(15) NULL,
	Numero_FincaRegistral VARCHAR(19) NULL,
	Codigo_MunicipioDGC VARCHAR(3) NULL,
	Nombre_Entidad_Menor VARCHAR(30) NULL,
	ID_Via NUMERIC(8) NULL,
	Primer_Numero NUMERIC(4) NULL,
	Primera_Letra VARCHAR(1) NULL,
	Segundo_Numero NUMERIC(4) NULL,
	Segunda_Letra VARCHAR(1) NULL,
	Kilometro NUMERIC(5,2) NULL,
	Bloque VARCHAR(4) NULL,
	Escalera VARCHAR(2) NULL,
	Planta VARCHAR(3) NULL,
	Puerta VARCHAR(3) NULL,
	Direccion_No_Estructurada VARCHAR(25) NULL,
	Codigo_Postal NUMERIC(5) NULL,
	Distrito VARCHAR(2) NULL,
	Municipio_Origen_Agregacion VARCHAR(3) NULL,
	Zona_Concentracion VARCHAR(3) NULL,
	Poligono_Rustico VARCHAR(3) NULL,
	Parcela VARCHAR(5) NULL,
	Paraje VARCHAR(5) NULL,
	Nombre_Paraje VARCHAR(30) NULL,
	Precio_Declarado NUMERIC(12) NULL,
	Origen_Precio VARCHAR(1) NULL,
	Precio_Venta NUMERIC(12) NULL,
	Anno_Finalizacion_Valoracion NUMERIC(4) NULL,
	Tipo_Propiedad VARCHAR(1) NULL,
	Numero_Orden_Horizontal VARCHAR(4) NULL,
	Anno_Antiguedad NUMERIC(4) NULL,
	Anio_Valor_Catastral NUMERIC(4) NULL,
	Valor_Catastral NUMERIC(12) NULL,
	Valor_Catastral_Suelo NUMERIC(12) NULL,
	Valor_Catastral_Construccion NUMERIC(12) NULL,
	Base_Liquidable NUMERIC(12) NULL,
	Clave_Uso_DGC VARCHAR(1) NULL,
	Importe_Bonificacion NUMERIC(12) NULL,
	Clave_Bonificacion VARCHAR(1) NULL,
	Superficie_Elementos_Constructivos NUMERIC(10) NULL,
	Superficie_Finca NUMERIC(10) NULL,
	Coeficiente_Propiedad NUMERIC(9,6) NULL,
	Valor_Base NUMERIC(12) NULL,
	Procedencia_ValorBase VARCHAR(1) NULL,
	Ejercicio_IBI NUMERIC(4) NULL,
	Valor_Catastral_IBI NUMERIC(12) NULL,
	Ejercicio_Revision NUMERIC(4) NULL,
	Ejercicio_Revision_Parcial NUMERIC(4) NULL,
	Periodo_Total NUMERIC(2) NULL,
	ID_Representante VARCHAR(9) NULL
);

CREATE TABLE Catastro_Temporal (
	ID_Expediente numeric(10) NOT NULL,
	ID_Elemento_Temporal numeric(10) NOT NULL,
	ID_Dialogo varchar(50),
	XML text NOT NULL
)
;

CREATE TABLE CNTPonUrb (
	IDentificador VARCHAR(14) NOT NULL,
	ID_Municipio NUMERIC(5) NOT NULL,
	Codigo_DelegacionMEH VARCHAR(2) NULL,
	Codigo_Municipio_MEH VARCHAR(3) NULL,
	Codigo_ProvinciaINE VARCHAR(2) NULL,
	Codigo_Municipio_INE VARCHAR(3) NULL,
	Anno_Aprobacion NUMERIC(4) NULL,
	Tipo_Ponencia VARCHAR(2) NULL,
	Anno_Efectos NUMERIC(4) NULL,
	Anno_AprobacionTotal NUMERIC(4) NULL,
	Anno_EfectosTotal NUMERIC(4) NULL,
	Anno_Normas NUMERIC(4) NULL,
	Anno_CuadroMarco NUMERIC(4) NULL,
	Aplicacion_Formula VARCHAR(1) NULL,
	Estado_Ponencia VARCHAR(1) NULL,
	Fecha_Ponencia DATE NULL,
	Boe_Publicacion NUMERIC(4) NULL,
	Fecha_BOP DATE NULL,
	BOE_PublicacionIncremento NUMERIC(4) NULL,
	Texto_libre VARCHAR(200) NULL,
	Fecha_CierreRevision DATE NULL,
	Asignacion_SIGCA VARCHAR(1) NULL,
	Finca_Infraedificada NUMERIC(3,2) NULL,
	Antiguedad_Infraedificada NUMERIC(4) NULL,
	Suelo_SinDesarrollar VARCHAR(1) NULL,
	Ruinosa VARCHAR(1) NULL,
	Incremento_Medio NUMERIC(4,2) NULL,
	ValorCatastral_anterior NUMERIC(14,2) NULL,
	ValorCatastral_Nuevo NUMERIC(14,2) NULL,
	Incremento_Rustica NUMERIC(4,2) NULL,
	Propiedad_Vertical1 VARCHAR(1) NULL
);

CREATE TABLE Configuracion (
	Frecuencia_Actualizacion numeric(3) NOT NULL,
	Frecuencia_Envio numeric(3) NOT NULL,
	Convenio varchar(5),
	Modo_Trabajo varchar(1),
	Tipo_Convenio varchar(1),
	Mostrar_Aviso_Act numeric(1),
	Mostrar_Aviso_Envio numeric(1),
	Ultima_Fecha_Actualizacion  DATE,
	Ultima_Fecha_Envio  DATE
)
;

CREATE TABLE Construccion (
	IDentificador VARCHAR(22) NOT NULL,
	Anno_Expediente NUMERIC(4) NULL,
	Referencia_Expediente VARCHAR(13) NULL,
	Codigo_Entidad_Colaboradora NUMERIC(3) NULL,
	Tipo_Movimiento VARCHAR(1) NULL,
	Parcela_Catastral VARCHAR(14) NULL,
	Numero_Orden_Construccion NUMERIC(4) NULL,
	Numero_Orden_BienInmueble VARCHAR(4) NULL,
	Codigo_UnidadConstructiva VARCHAR(4) NULL,
	Bloque VARCHAR(4) NULL,
	Escalera VARCHAR(2) NULL,
	Planta VARCHAR(3) NULL,
	Puerta VARCHAR(3) NULL,
	Codigo_Destino_DGC VARCHAR(3) NULL,
	Indicador_Tipo_Reforma VARCHAR(1) NULL,
	Anno_Reforma NUMERIC(4) NULL,
	Anno_Antiguedad NUMERIC(4) NULL,
	Indicador_Local_Interior BOOLEAN NULL,
	Superficie_Total_Local NUMERIC(7) NULL,
	Superficie_Terrazas_Local NUMERIC(7) NULL,
	Superficie_Imputable_Local NUMERIC(7) NULL,
	Tipologia_Constructiva VARCHAR(5) NULL,
	Codigo_Uso_Predominante VARCHAR(1) NULL,
	Codigo_Categoria_Predominante VARCHAR(1) NULL,
	Codigo_Modalidad_Reparto VARCHAR(3) NULL,
	Codigo_Tipo_Valor VARCHAR(1) NULL,
	Corrector_Apreciacion_Economica NUMERIC(3,2) NULL,
	Corrector_Vivienda BOOLEAN NULL
);

CREATE TABLE Cultivos (
	IDentificador VARCHAR(22) NOT NULL,
	Anno_Expediente NUMERIC(4) NULL,
	Referencia_Expediente VARCHAR(13) NULL,
	Codigo_Entidad_Colaboradora NUMERIC(3) NULL,
	Tipo_Movimiento VARCHAR(1) NULL,
	Naturaleza_Suelo VARCHAR(2) NULL,
	Parcela_Catastral VARCHAR(14) NULL,
	Codigo_Subparcela VARCHAR(4) NULL,
	Numero_Orden VARCHAR(4) NULL,
	Tipo_Subparcela VARCHAR(1) NULL,
	Superficie_Subparcela NUMERIC(10) NULL,
	Calificacion_Cultivo VARCHAR(2) NULL,
	Denominacion_Cultivo VARCHAR(40) NULL,
	Intensidad_Productiva NUMERIC(2) NULL,
	Bonificacion_Subparcela VARCHAR(1) NULL,
	Ejercicio_Finalizacion NUMERIC(4) NULL,
	Valor_CAtastral NUMERIC(12) NULL,
	Modalidad_Reparto VARCHAR(3) NULL
);

CREATE TABLE Derechos (
	ID_BienInmueble VARCHAR(20) NOT NULL,
	Codigo_Derecho VARCHAR(2) NULL,
	Porcentaje_Derecho NUMERIC(5,2) NULL,
	Ordinal_Derecho NUMERIC(3) NULL,
	NIFTitular VARCHAR(9) NOT NULL,
	primary key (id_bieninmueble, niftitular)
);

CREATE TABLE Domicilio_Localizacion (
	ID_Localizacion NUMERIC(8) NOT NULL,
	Provincia_INE VARCHAR(2) NULL,
	Municipio_INE VARCHAR(3) NULL,
	Codigo_MunicipioDGC VARCHAR(3) NULL,
	Nombre_Entidad_Menor VARCHAR(30) NULL,
	Codigo_Via NUMERIC(5) NULL,
	Primer_Numero NUMERIC(4) NULL,
	Primera_Letra VARCHAR(1) NULL,
	Segundo_Numero NUMERIC(4) NULL,
	Segunda_Letra VARCHAR(1) NULL,
	Kilometro NUMERIC(5) NULL,
	Bloque VARCHAR(4) NULL,
	Escalera VARCHAR(2) NULL,
	Planta VARCHAR(3) NULL,
	Puerta VARCHAR(3) NULL,
	Direccion_No_Estructurada VARCHAR(25) NULL,
	Codigo_Postal NUMERIC(5) NULL,
	Nombre_Via VARCHAR(25) NULL,
	Apartado_Correos VARCHAR(5) NULL,
	Tipo_Via VARCHAR(5) NULL,
	Nombre_Provincia VARCHAR(25) NULL,
	Nombre_Municipio VARCHAR(40) NULL
);


CREATE TABLE Entidad_Generadora (
	Codigo numeric(3) NOT NULL,
	Tipo varchar(2),
	Descripcion varchar(50),
	Nombre varchar(50),
	ID_Entidad_Generadora numeric(8) NOT NULL
)
;

CREATE TABLE Estado_Expediente (
	ID_Estado NUMERIC(10) NOT NULL,
	Nombre_Estado VARCHAR(50) NOT NULL,
	Descripcion VARCHAR(50) NULL
);

CREATE TABLE Estado_Siguiente (
	ID_Estado NUMERIC(10) NOT NULL,
	ID_Estado_Siguiente NUMERIC(10) NOT NULL,
	Modo VARCHAR(1) NOT NULL
);


CREATE TABLE Expediente (
	ID_Expediente numeric(10) NOT NULL,
	ID_Entidad_Generadora numeric(8) NOT NULL,
	ID_Estado numeric(10) NOT NULL,
	ID_Tecnico_Catastro varchar(10) NOT NULL,
	ID_Localizacion numeric(8),
	ID_Municipio numeric(5) NOT NULL,
	Numero_Expediente varchar(13) NOT NULL,
	ID_Tipo_Expediente numeric(5) NOT NULL,
	Fecha_Alteracion date,
	Anio_Expediente_Gerencia numeric(4),
	Referencia_Expediente_Gerencia varchar(8),
	Codigo_Entidad_Registro_DGC_Origen_Alteracion numeric(3),
	Anio_Expediente_Admin_Origen_Alteracion numeric(4),
	Referencia_Expediente_Admin_Origen varchar(13),
	Fecha_Registro date,
	Fecha_Movimiento date,
	Hora_Movimiento time without time zone,
	Tipo_Documento_Origen_Alteracion varchar(1),
	Info_Documento_Origen_Alteracion varchar(50),
	Codigo_descriptivo_Alteracion varchar(3),
	Descripcion_Alteracion varchar(400),
	NIF_Presentador varchar(9),
	Nombre_Completo_Presentador varchar(60),
	Num_BienesInmuebles_Urb numeric(4),
	Num_BienesInmuebles_Rus numeric(4),
	Num_BienesInmuebles_Esp numeric(4),
	Fecha_De_Cierre date,
	Cod_Provincia_Notaria varchar(2),
	Cod_Poblacion_Notaria varchar(4),
	Cod_Notaria varchar(3),
	Anio_protocolo_Notarial varchar(4),
	Protocolo_notarial varchar(6)
)
;

CREATE TABLE Expediente_Bieninmueble (
	ID_Expediente numeric(10) NOT NULL,
	ID_BienInmueble varchar(20) NOT NULL,
	ID_Dialogo varchar(50)
)
;


CREATE TABLE Expediente_Fichero (
	ID_Expediente NUMERIC(10) NOT NULL,
	ID_Fichero NUMERIC(10) NOT NULL
);

CREATE TABLE Expediente_Finca_Catastro (
	ID_Expediente NUMERIC(10) NOT NULL,
	Ref_Catastral VARCHAR(14) NOT NULL,
	ID_Dialogo VARCHAR(50) NULL
);


CREATE TABLE Ficheros (
	ID_Fichero numeric(10) NOT NULL,
	ID_Tipo_Fichero numeric(10),
	Nombre varchar(21),
	Descripcion varchar(40),
	Fecha_Generacion date NOT NULL,
	Fecha_Intercambio date NOT NULL,
	Contenido text,
	URL varchar(50),
	Fecha_Inicio_Periodo date,
	Fecha_Fin_Periodo date,
	Codigo_Entidad_Destinataria numeric(3),
	Codigo_Entidad_Generadora numeric(3),
	Codigo_Envio varchar(20)
)
;


CREATE TABLE FXCC (
	ID_finca NUMERIC(8) NOT NULL,
	"DXF" TEXT NULL,
	"ASC" TEXT NULL

);

CREATE TABLE Paraje (
	TipoFichero VARCHAR(2) NULL,
	ID_Entidad NUMERIC(9) NULL,
	CodigoParaje NUMERIC(5) NULL,
	DenominacionParaje VARCHAR(30) NULL,
	ID_Paraje NUMERIC(14) NOT NULL
);


CREATE TABLE Persona (
	Anno_Expediente NUMERIC(4) NULL,
	Referencia_Expediente VARCHAR(13) NULL,
	Codigo_Entidad_colaboradora NUMERIC(3) NULL,
	Parcela_Catastral VARCHAR(14) not NULL,
	Numero_Cargo VARCHAR(4) not NULL,
	Digito_Control1 VARCHAR(1) not NULL,
	Digito_Control2 VARCHAR(1) not NULL,
	NIF VARCHAR(9) NOT NULL,
	Razon_Social VARCHAR(60) NULL,
	Ausencia_NIF VARCHAR(1) NULL,
	Codigo_Provincia_INE NUMERIC(2) NULL,
	Codigo_Municipio_DGC NUMERIC(3) NULL,
	Codigo_Municipio_INE NUMERIC(3) NULL,
	Entidad_Menor VARCHAR(30) NULL,
	ID_Via NUMERIC(8) NULL,
	Primer_Numero NUMERIC(4) NULL,
	Primera_Letra VARCHAR(1) NULL,
	Segundo_Numero NUMERIC(4) NULL,
	Segunda_Letra VARCHAR(1) NULL,
	Escalera VARCHAR(2) NULL,
	Kilometro NUMERIC(5,2) NULL,
	BLoque VARCHAR(4) NULL,
	Planta VARCHAR(3) NULL,
	Puerta VARCHAR(3) NULL,
	Direccion_No_Estructurada VARCHAR(25) NULL,
	Codigo_Postal VARCHAR(5) NULL,
	Apartado_Correos NUMERIC(5) NULL,
	NIF_Conyuge VARCHAR(9) NULL,
	NIF_CB VARCHAR(9) NULL,
	Complemento_Titularidad VARCHAR(60) NULL,
	Fecha_Alteracion DATE NULL,
	primary key (nif, parcela_catastral, numero_cargo, digito_control1, digito_control2)
	
) ;


CREATE TABLE Ponencia_Poligono (
	ID_Municipio NUMERIC(5) NOT NULL,
	Codigo_DelegacionMEH VARCHAR(2) NULL,
	Codigo_Municipio_MEH VARCHAR(3) NULL,
	Codigo_ProvinciaINE VARCHAR(2) NULL,
	Codigo_Municipio_INE VARCHAR(3) NULL,
	Anno_Aprobacion NUMERIC(4) NULL,
	Codigo_Poligono VARCHAR(3) NOT NULL,
	CMV NUMERIC(4) NULL,
	AANormas NUMERIC(4) NULL,
	AARevision NUMERIC(4) NULL,
	Efecto_Revision VARCHAR(1) NULL,
	Reforma VARCHAR(1) NULL,
	MBC NUMERIC(13,6) NULL,
	MBR NUMERIC(13,6) NULL,
	Usuario VARCHAR(12) NULL,
	Modificacion DATE NULL,
	Desde DATE NULL,
	Tipo_Movimiento VARCHAR(1) NULL,
	Motivo_Movimiento VARCHAR(4) NULL,
	Estado VARCHAR(1) NULL,
	CodMBC_Plan NUMERIC(1) NULL,
	Grupo_Plan NUMERIC(1) NULL,
	Uso_Plan VARCHAR(1) NULL,
	Modulo_Plan NUMERIC(9,2) NULL,
	Coef_CoordPlan NUMERIC(3,2) NULL,
	Edif_Plan NUMERIC(4,2) NULL,
	VUB NUMERIC(13,6) NULL,
	VRB NUMERIC(13,6) NULL,
	FL_GB NUMERIC(4,2) NULL,
	Zona_VUB VARCHAR(5) NULL,
	Zona_VRB VARCHAR(5) NULL,
	Diseminado VARCHAR(1) NULL,
	Uso_Pred VARCHAR(1) NULL,
	Tipologia_Pred VARCHAR(4) NULL,
	Categoria_Pred VARCHAR(1) NULL,
	Edif_Media NUMERIC(4,2) NULL,
	Fl_Gb_Uni NUMERIC(4,2) NULL
);

CREATE TABLE Ponencia_Tramos (
	ID_Municipio NUMERIC(5) NOT NULL,
	Codigo_DelegacionMEH VARCHAR(2) NULL,
	Codigo_Municipio_MEH VARCHAR(3) NULL,
	Codigo_ProvinciaINE VARCHAR(2) NULL,
	Codigo_Municipio_INE VARCHAR(3) NULL,
	Anno_Aprobacion NUMERIC(4) NULL,
	Codigo_Via VARCHAR(5) NULL,
	Codigo_Tramo VARCHAR(3) NOT NULL,
	Denominacion VARCHAR(30) NULL,
	Codigo_Poligono VARCHAR(3) NULL,
	Codigo_Urbanistica VARCHAR(10) NULL,
	Situacion VARCHAR(10) NULL,
	Maximo_Par NUMERIC(4) NULL,
	Caracter_MaximoPar VARCHAR(1) NULL,
	Minimo_Par NUMERIC(5) NULL,
	Caracter_MinimoPar VARCHAR(1) NULL,
	Maximo_Impar NUMERIC(4) NULL,
	Caracter_MaximoImpar VARCHAR(1) NULL,
	Minimo_Impar NUMERIC(4) NULL,
	Caracter_MinimoImpar VARCHAR(1) NULL,
	Valor_Unitario NUMERIC(11,6) NULL,
	Banda_UsoComercial NUMERIC(4,2) NULL,
	Banda_UsoResidencial NUMERIC(4,2) NULL,
	Banda_UsoOficinas NUMERIC(4,2) NULL,
	Banda_UsoIndustrial NUMERIC(4,2) NULL,
	Banda_UsoTuristico NUMERIC(4,2) NULL,
	Banda_OtrosUsos1 NUMERIC(4,2) NULL,
	Banda_OtrosUsos2 NUMERIC(4,2) NULL,
	Banda_OtrosUsos3 NUMERIC(4,2) NULL,
	Corrector_ApDpSuelo NUMERIC(3,2) NULL,
	Corrector_ApDPSueCons NUMERIC(3,2) NULL,
	Valor_Vuelo VARCHAR(1) NULL,
	Agua VARCHAR(1) NULL,
	Electricidad VARCHAR(1) NULL,
	Alumbrado VARCHAR(1) NULL,
	Desmonte VARCHAR(1) NULL,
	Pavimentacion VARCHAR(1) NULL,
	Alcantarillado VARCHAR(1) NULL,
	Costes_Urbanizacion NUMERIC(11,6) NULL
);

CREATE TABLE Ponencia_Urbanistica (
	ID_Municipio NUMERIC(5) NOT NULL,
	Codigo_DelegacionMEH VARCHAR(2) NULL,
	Codigo_Municipio_MEH VARCHAR(3) NULL,
	Codigo_ProvinciaINE VARCHAR(2) NULL,
	Codigo_Municipio_INE VARCHAR(3) NULL,
	Anno_Aprobacion NUMERIC(4) NULL,
	Codigo_Zona VARCHAR(10) NOT NULL,
	Denominacion VARCHAR(30) NULL,
	Codigo_Calificacion VARCHAR(2) NULL,
	Codigo_Zonificacion VARCHAR(2) NULL,
	Codigo_Ocupacion VARCHAR(2) NULL,
	Codigo_Ordenacion VARCHAR(1) NULL,
	Logitud NUMERIC(4) NULL,
	Fondo NUMERIC(3) NULL,
	Superficie_Minima NUMERIC(5) NULL,
	Numero_Plantas NUMERIC(4,1) NULL,
	NumeroPlantas_SolarInterior NUMERIC(4,1) NULL,
	Edificabilidad_UsoComercial NUMERIC(4,2) NULL,
	Edificabilidad_UsoResidencial NUMERIC(4,2) NULL,
	Edificabilidad_UsoOficinas NUMERIC(4,2) NULL,
	Edificabilidad_UsoIndustrial NUMERIC(4,2) NULL,
	Edificabilidad_UsoTuristico NUMERIC(4,2) NULL,
	Edificabilidad_OtrosUsos1 NUMERIC(4,2) NULL,
	Edificabilidad_OtrosUsos2 NUMERIC(4,2) NULL,
	Edificabilidad_OtrosUsos3 NUMERIC(4,2) NULL,
	Edificabilidad_ZonaVerde NUMERIC(4,2) NULL,
	Edificabilidad_Equipamientos NUMERIC(4,2) NULL
);

CREATE TABLE Ponencia_ZonaValor (
	ID_Municipio NUMERIC(5) NOT NULL,
	Codigo_DelegacionMEH VARCHAR(2) NULL,
	Codigo_Municipio_MEH VARCHAR(3) NULL,
	Codigo_ProvinciaINE VARCHAR(2) NULL,
	Codigo_Municipio_INE VARCHAR(3) NULL,
	Anno_Aprobacion NUMERIC(4) NULL,
	Codigo_ZonaValor VARCHAR(5) NOT NULL,
	Importe_UsoComercial NUMERIC(7,2) NULL,
	Importe_UsoResidencial NUMERIC(7,2) NULL,
	Importe_UsoOficinas NUMERIC(7,2) NULL,
	Importe_UsoIndustrial NUMERIC(7,2) NULL,
	Importe_UsoTuristico NUMERIC(7,2) NULL,
	Importe_OtrosUsos1 NUMERIC(7,2) NULL,
	Importe_OtrosUsos2 NUMERIC(7,2) NULL,
	Importe_OtrosUsos3 NUMERIC(7,2) NULL,
	Importe_ZonaVerde NUMERIC(7,2) NULL,
	Importe_Equipamientos NUMERIC(7,2) NULL,
	Valor_Unitario NUMERIC(7,2) NULL,
	Valor_ZonaVerde NUMERIC(7,2) NULL,
	Valor_Equipamientos NUMERIC(7,2) NULL,
	Valor_SueloSinDesarrollar NUMERIC(7,2) NULL
);


CREATE TABLE RepartosConsBI (
	ID_Reparto NUMERIC(10) NOT NULL,
	Tipo_Registro VARCHAR(2) NULL,
	Anno_Expediente NUMERIC(4) NULL,
	Referencia_Expediente VARCHAR(13) NULL,
	Codigo_Entidad_Colaboradora NUMERIC(3) NULL,
	Tipo_Movimiento VARCHAR(1) NULL,
	IDConstruccion_Origen VARCHAR(18) NULL,
	Porcentaje_Reparto NUMERIC(8,2) NULL,
	ID_BienInmuebleDestino VARCHAR(20) NULL,
	CONSTRAINT "repartosconsbi_pkey" PRIMARY KEY(id_reparto)
);

CREATE TABLE RepartosConstrucciones (
	ID_Reparto NUMERIC(10) NOT NULL,
	Tipo_Registro VARCHAR(2) NULL,
	Anno_Expediente NUMERIC(4) NULL,
	Referencia_Expediente VARCHAR(13) NULL,
	Codigo_Entidad_Colaboradora NUMERIC(3) NULL,
	Tipo_Movimiento VARCHAR(1) NULL,
	ID_ConstruccionOrigen VARCHAR(18) NULL,
	Porcentaje_Reparto NUMERIC(8,2) NULL,
	ID_ConstruccionDestino VARCHAR(18) NULL,
	CONSTRAINT "repartosconstrucciones_pkey" PRIMARY KEY(id_reparto)
);

CREATE TABLE RepartosCultivos (
	ID_Reparto NUMERIC(10) NOT NULL,
	Tipo_Registro VARCHAR(2) NULL,
	Anno_Expediente NUMERIC(4) NULL,
	Referencia_Expediente VARCHAR(13) NULL,
	Codigo_Entidad_Colaboradora NUMERIC(3) NULL,
	Tipo_Movimiento VARCHAR(1) NULL,
	ID_CultivoOrigen VARCHAR(22) NULL,
	Porcentaje_Reparto NUMERIC(8,2) NULL,
	ID_BienInmuebleDestino VARCHAR(20) NULL,
	CONSTRAINT "repartoscultivos_pkey" PRIMARY KEY(id_reparto)
);


CREATE TABLE Representante (
	Anno_Expediente NUMERIC(4) NULL,
	Referencia_Expediente VARCHAR(13) NULL,
	Codigo_Entidad_colaboradora NUMERIC(3) NULL,
	ID_BienInmueble VARCHAR(20) NOT NULL,
	NIFRepresentante VARCHAR(9) NOT NULL,
	RazonSocial_Representante VARCHAR(60) NULL,
	Codigo_Provincia_INE NUMERIC(2) NULL,
	Codigo_Municipio_DGC NUMERIC(3) NULL,
	Codigo_Municipio_INE NUMERIC(3) NULL,
	Entidad_Menor VARCHAR(30) NULL,
	ID_Via NUMERIC(8) NULL,
	Primer_Numero NUMERIC(4) NULL,
	Primera_Letra VARCHAR(1) NULL,
	Segundo_Numero NUMERIC(4) NULL,
	Kilometro NUMERIC(5,2) NULL,
	Segunda_Letra VARCHAR(1) NULL,
	Bloque VARCHAR(4) NULL,
	Escalera VARCHAR(2) NULL,
	Planta VARCHAR(2) NULL,
	Puerta VARCHAR(2) NULL,
	Direccion_No_Estructurada VARCHAR(25) NULL,
	Codigo_Postal VARCHAR(5) NULL,
	Apartado_Correos NUMERIC(5) NULL,
	CONSTRAINT "representante_pkey" PRIMARY KEY(id_bieninmueble, nifrepresentante)
);


CREATE TABLE RuEvaluatorio (
	ID_Municipio NUMERIC(5) NOT NULL,
	Codigo_DelegacionMEH VARCHAR(2) NULL,
	Codigo_Municipio_MEH VARCHAR(3) NULL,
	Codigo_Municipio_Agregado VARCHAR(3) NULL,
	CC VARCHAR(2) NULL,
	IP VARCHAR(2) NULL,
	Tipo NUMERIC(12,6) NULL,
	AATipo NUMERIC(4) NULL,
	PTSConc1 NUMERIC(12,6) NULL,
	EjeConc1 VARCHAR(1) NULL,
	PTSConc2 NUMERIC(12,6) NULL,
	EjeConc2 NUMERIC(4) NULL,
	PTSConc3 NUMERIC(12,6) NULL,
	EjeConc3 NUMERIC(4) NULL,
	J_Teoricas NUMERIC(8,2) NULL,
	Exento VARCHAR(1) NULL,
	Condonado VARCHAR(1) NULL,
	Condonado_JT VARCHAR(1) NULL,
	Vuelo VARCHAR(1) NULL,
	Usuario VARCHAR(12) NULL,
	Modificacion DATE NULL,
	Desde DATE NULL,
	Tipo_Movimiento VARCHAR(1) NULL,
	Motivo_Movimiento VARCHAR(4) NULL,
	Estado VARCHAR(1) NULL,
	IPP NUMERIC(2) NULL,
	Tipo_Ant NUMERIC(12,6) NULL
);

CREATE TABLE Suelo (
	IDentificador VARCHAR(18) NOT NULL,
	Anno_Expediente NUMERIC(4) NULL,
	Referencia_Expediente VARCHAR(13) NULL,
	Codigo_Entidad_Colaboradora NUMERIC(3) NULL,
	Tipo_Movimiento VARCHAR(1) NULL,
	Parcela_Catastral VARCHAR(14) NULL,
	Numero_Orden VARCHAR(4) NULL,
	Longitud_Fachada NUMERIC(5) NULL,
	Tipo_Fachada VARCHAR(2) NULL,
	Superficie_Elemento_Suelo NUMERIC(7) NULL,
	Fondo_Elemento_Suelo NUMERIC(3) NULL,
	Codigo_Via_Ponencia VARCHAR(5) NULL,
	Codigo_Tramo_Ponencia VARCHAR(3) NULL,
	Zona_Valor VARCHAR(5) NULL,
	Zona_Urbanistica VARCHAR(10) NULL,
	Codigo_Tipo_Valor VARCHAR(1) NULL,
	Numero_Fachadas VARCHAR(1) NULL,
	Corrector_Longitud_Fachada BOOLEAN NULL,
	Corrector_Forma_Irregular BOOLEAN NULL,
	Corrector_Desmonte_Excesivo BOOLEAN NULL,
	Corrector_Superficie_Distinta BOOLEAN NULL,
	Corrector_Inedificabilidad BOOLEAN NULL,
	Corrector_VPO BOOLEAN NULL,
	Corrector_Apreciacion_Depreciacion NUMERIC(3) NULL,
	Corrector_Depreciacion_Funcional BOOLEAN NULL,
	Corrector_Cargas_Singulares NUMERIC(3) NULL,
	Corrector_Situaciones_Especiales BOOLEAN NULL,
	Corrector_Uso_No_Lucrativo BOOLEAN NULL
);

CREATE TABLE Unidad_Constructiva (
	IDentificador VARCHAR(18) NOT NULL,
	Anno_Expediente NUMERIC(4) NULL,
	Referencia_Expediente VARCHAR(13) NULL,
	Codigo_Entidad_Colaboradora NUMERIC(3) NULL,
	TipoMovimiento VARCHAR(1) NULL,
	Parcela_Catastral VARCHAR(14) NULL,
	Codigo_Unidad_Constructiva VARCHAR(4) NULL,
	Clase_Unidad VARCHAR(2) NULL,
	Codigo_Municipio_INE VARCHAR(3) NULL,
	Codigo_Municipio_DGC VARCHAR(3) NULL,
	Nombre_Entidad_Menor VARCHAR(30) NULL,
	Primer_Numero NUMERIC(4) NULL,
	Primera_Letra VARCHAR(1) NULL,
	Segundo_Numero NUMERIC(4) NULL,
	Segunda_Letra VARCHAR(1) NULL,
	Kilometro NUMERIC(5,2) NULL,
	Bloque VARCHAR(4) NULL,
	Direccion_No_Estructurada VARCHAR(25) NULL,
	Anio_Construccion NUMERIC(4) NULL,
	Indicador_Exactitud VARCHAR(1) NULL,
	Superficie_Ocupada NUMERIC(7) NULL,
	Longitud_Fachada NUMERIC(5) NULL,
	Codigo_Via_Ponencia VARCHAR(5) NULL,
	Codigo_Tramo_Ponencia VARCHAR(3) NULL,
	Zona_Valor VARCHAR(5) NULL,
	Numero_Fachadas VARCHAR(1) NULL,
	Corrector_Longitud_Fachada BOOLEAN NULL,
	Corrector_Estado_Conservacion VARCHAR(1) NULL,
	Corrector_Depreciacion_Funcional BOOLEAN NULL,
	Corrector_Cargas_Singulares NUMERIC(3) NULL,
	Corrector_Situaciones_Especiales BOOLEAN NULL,
	Corrector_Uso_No_Lucrativo BOOLEAN NULL
);


CREATE TABLE Tipo_Expediente ( 
	ID_Tipo_Expediente numeric(5) NOT NULL,
	Codigo_Tipo_Expediente varchar(5) NOT NULL,
	Descripcion varchar(100),
	Convenio varchar(5)
)
;


ALTER TABLE Bien_Inmueble ADD CONSTRAINT PK_BienInmueble
	PRIMARY KEY (IDentificador);

	
ALTER TABLE Catastro_Temporal ADD CONSTRAINT PK_Catastro_Temporal
	PRIMARY KEY (ID_Elemento_Temporal)
;

ALTER TABLE CNTPonUrb ADD CONSTRAINT PK_Ponencia
	PRIMARY KEY (IDentificador);

ALTER TABLE "public"."construccion"
  ADD PRIMARY KEY ("identificador");




ALTER TABLE Cultivos ADD CONSTRAINT PK_Cultivos
	PRIMARY KEY (IDentificador);

ALTER TABLE Domicilio_Localizacion ADD CONSTRAINT PK_Domicilio_Localizacion
	PRIMARY KEY (ID_Localizacion);

ALTER TABLE Entidad_Generadora ADD CONSTRAINT PK_Entidad_Generadora
	PRIMARY KEY (ID_Entidad_Generadora)
;

ALTER TABLE Estado_Expediente ADD CONSTRAINT PK_Estado
	PRIMARY KEY (ID_Estado);

ALTER TABLE Estado_Siguiente ADD CONSTRAINT PK_Estado_Siguiente
	PRIMARY KEY (ID_Estado, ID_Estado_Siguiente, Modo);

ALTER TABLE Expediente ADD CONSTRAINT PK_Expedientes_Catastrales
	PRIMARY KEY (ID_Expediente);

ALTER TABLE Expediente_Bieninmueble ADD CONSTRAINT PK_Expediente_Bieninmueble
	PRIMARY KEY (ID_Expediente, ID_BienInmueble)
;


ALTER TABLE Expediente_Fichero ADD CONSTRAINT PK_Expediente_Fichero
	PRIMARY KEY (ID_Expediente, ID_Fichero);

ALTER TABLE Expediente_Finca_Catastro ADD CONSTRAINT PK_Expediente_Finca_Catastro
	PRIMARY KEY (ID_Expediente, Ref_Catastral);


ALTER TABLE Ficheros ADD CONSTRAINT PK_Fichero
	PRIMARY KEY (ID_Fichero);



ALTER TABLE "public"."fxcc"
  ADD PRIMARY KEY ("id_finca");




ALTER TABLE Paraje ADD CONSTRAINT PK_Paraje
	PRIMARY KEY (ID_Paraje);


ALTER TABLE Ponencia_Poligono ADD CONSTRAINT PK_Poligono
	PRIMARY KEY (Codigo_Poligono);

ALTER TABLE Ponencia_Tramos ADD CONSTRAINT PK_Tramo
	PRIMARY KEY (Codigo_Tramo);

ALTER TABLE Ponencia_Urbanistica ADD CONSTRAINT PK_ZonaUrbanistica
	PRIMARY KEY (Codigo_Zona);

ALTER TABLE Ponencia_ZonaValor ADD CONSTRAINT PK_ZonaValor
	PRIMARY KEY (Codigo_ZonaValor);


ALTER TABLE "public"."suelo"
  ADD PRIMARY KEY ("identificador");


ALTER TABLE Unidad_Constructiva ADD CONSTRAINT PK_Unidad_Constructiva
	PRIMARY KEY (IDentificador);

ALTER TABLE Tipo_Expediente ADD CONSTRAINT PK_Tipo_Expediente 
	PRIMARY KEY (ID_Tipo_Expediente)
;
	
ALTER TABLE Estado_Siguiente ADD CONSTRAINT FK_Estado_Siguiente_Estado
	FOREIGN KEY (ID_Estado) REFERENCES Estado_Expediente (ID_Estado);

ALTER TABLE Expediente ADD CONSTRAINT FK_Expediente_Entidad_Generadora
	FOREIGN KEY (ID_Entidad_Generadora) REFERENCES Entidad_Generadora (ID_Entidad_Generadora)
;

ALTER TABLE Expediente ADD CONSTRAINT FK_DireccionPresentador_Direccion
	FOREIGN KEY (ID_Localizacion) REFERENCES Domicilio_Localizacion (ID_Localizacion);

ALTER TABLE Expediente ADD CONSTRAINT FK_Expediente_Estado
	FOREIGN KEY (ID_Estado) REFERENCES Estado_Expediente (ID_Estado);

ALTER TABLE Expediente ADD CONSTRAINT FK_Expediente_Tipo_Expediente 
	FOREIGN KEY (ID_Tipo_Expediente) REFERENCES Tipo_Expediente (ID_Tipo_Expediente)
;
	
ALTER TABLE Expediente_Bieninmueble ADD CONSTRAINT FK_Exp_Bien_BienInmueble
	FOREIGN KEY (ID_BienInmueble) REFERENCES Bien_Inmueble (IDentificador)
;

ALTER TABLE Expediente_Bieninmueble ADD CONSTRAINT FK_Expediente_BienI_Expediente
	FOREIGN KEY (ID_Expediente) REFERENCES Expediente (ID_Expediente)
;

ALTER TABLE Catastro_Temporal ADD CONSTRAINT FK_Catastro_Tempora_Expediente 
	FOREIGN KEY (ID_Expediente) REFERENCES Expediente (ID_Expediente)
;

ALTER TABLE Expediente_Fichero ADD CONSTRAINT FK_Expediente_Fichero_Ficheros
	FOREIGN KEY (ID_Fichero) REFERENCES Ficheros (ID_Fichero);

ALTER TABLE Expediente_Fichero ADD CONSTRAINT FK_Expediente_Fiche_Expediente
	FOREIGN KEY (ID_Expediente) REFERENCES Expediente (ID_Expediente);
















ALTER TABLE Expediente_Finca_Catastro ADD CONSTRAINT FK_Expediente_Finca_Expediente
	FOREIGN KEY (ID_Expediente) REFERENCES Expediente (ID_Expediente);


ALTER TABLE Ficheros ADD CONSTRAINT FK_Ficheros_Entidad_Generadora
	FOREIGN KEY (Codigo_Entidad_Generadora) REFERENCES Entidad_Generadora (ID_Entidad_Generadora)
;


ALTER TABLE Ponencia_Tramos ADD CONSTRAINT FK_Poligono
	FOREIGN KEY (Codigo_Poligono) REFERENCES Ponencia_Poligono (Codigo_Poligono);

ALTER TABLE Ponencia_Tramos ADD CONSTRAINT FK_Urbanistica
	FOREIGN KEY (Codigo_Urbanistica) REFERENCES Ponencia_Urbanistica (Codigo_Zona);




ALTER TABLE "public"."repartosconsbi"
  ADD CONSTRAINT "fk_repartosibi_constorigen" FOREIGN KEY ("idconstruccion_origen")
    REFERENCES "public"."construccion"("identificador")
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    NOT DEFERRABLE;




ALTER TABLE "public"."repartosconstrucciones"
  ADD CONSTRAINT "fk_repartos_constorigen" FOREIGN KEY ("id_construccionorigen")
    REFERENCES "public"."construccion"("identificador")
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    NOT DEFERRABLE;



ALTER TABLE "public"."repartosconstrucciones"
  ADD CONSTRAINT "fk_repartos_constdestino" FOREIGN KEY ("id_construcciondestino")
    REFERENCES "public"."construccion"("identificador")
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    NOT DEFERRABLE;






ALTER TABLE RepartosCultivos ADD CONSTRAINT FK_ID_BienInmuebleDestino
	FOREIGN KEY (ID_BienInmuebleDestino) REFERENCES Bien_Inmueble (IDentificador);

ALTER TABLE RepartosCultivos ADD CONSTRAINT FK_Cultivo
	FOREIGN KEY (ID_CultivoOrigen) REFERENCES Cultivos (IDentificador);



ALTER TABLE derechos ADD FOREIGN KEY (id_bieninmueble)
    REFERENCES bien_inmueble(identificador);

--modificaciones

ALTER TABLE "public"."parcelas"
  DROP COLUMN "tipo";

ALTER TABLE "public"."parcelas"
  DROP COLUMN "codigo_entidad_menor";

ALTER TABLE "public"."parcelas"
  DROP COLUMN "aniovalorcatastral";

ALTER TABLE "public"."parcelas"
  DROP COLUMN "codigogerencia";

ALTER TABLE "public"."parcelas"
  DROP COLUMN "codigomunicipiomeh";

ALTER TABLE "public"."parcelas"
  DROP COLUMN "codigonaturalezabien";

ALTER TABLE "public"."parcelas"
  DROP COLUMN "numerocargo";

ALTER TABLE "public"."parcelas"
  DROP COLUMN "primercaractercontrol";

ALTER TABLE "public"."parcelas"
  DROP COLUMN "segundocaractercontrol";

ALTER TABLE "public"."parcelas"
  DROP COLUMN "numerofijo";

ALTER TABLE "public"."parcelas"
  DROP COLUMN "identificadorayuntamiento";

ALTER TABLE "public"."parcelas"
  DROP COLUMN "tipovia";

ALTER TABLE "public"."parcelas"
  DROP COLUMN "nombrevia";

ALTER TABLE "public"."parcelas"
  DROP COLUMN "escalera";

ALTER TABLE "public"."parcelas"
  DROP COLUMN "planta";

ALTER TABLE "public"."parcelas"
  DROP COLUMN "puerta";

ALTER TABLE "public"."parcelas"
  DROP COLUMN "codigomunicipioagregacion";

ALTER TABLE "public"."parcelas"
  DROP COLUMN "id_sujetopasivo";

ALTER TABLE "public"."parcelas"
  DROP COLUMN "valorcatastral";

ALTER TABLE "public"."parcelas"
  DROP COLUMN "valorcatastralsuelo";

ALTER TABLE "public"."parcelas"
  DROP COLUMN "valorcatastralconstruccion";

ALTER TABLE "public"."parcelas"
  DROP COLUMN "baseliquidable";

ALTER TABLE "public"."parcelas"
  DROP COLUMN "clavegrupobienes";

ALTER TABLE "public"."parcelas"
  DROP COLUMN "valorcatastralbonificado";

ALTER TABLE "public"."parcelas"
  DROP COLUMN "clavebonificacion";

ALTER TABLE "public"."parcelas"
  DROP COLUMN "superficieconstruccionescargos";

ALTER TABLE "public"."parcelas"
  DROP COLUMN "superficiesuelo";

ALTER TABLE "public"."parcelas"
  DROP COLUMN "coeficientepropiedad";

ALTER TABLE "public"."parcelas"
  DROP COLUMN "importevalorbase";

ALTER TABLE "public"."parcelas"
  DROP COLUMN "codigovalorbase";

ALTER TABLE "public"."parcelas"
  DROP COLUMN "ejercicioefectosibi";

ALTER TABLE "public"."parcelas"
  DROP COLUMN "valorcatastralefectosibi";

ALTER TABLE "public"."parcelas"
  DROP COLUMN "ejerciciorevisiontotal";

ALTER TABLE "public"."parcelas"
  DROP COLUMN "ejerciciorevisionparcial";

ALTER TABLE "public"."parcelas"
  DROP COLUMN "periodovigencia";


ALTER TABLE "public"."parcelas"
  ADD COLUMN "anno_expediente" NUMERIC(4,0);

ALTER TABLE "public"."parcelas"
  ADD COLUMN "referencia_expediente" VARCHAR(13);

ALTER TABLE "public"."parcelas"
  ADD COLUMN "codigo_entidad_colaboradora" NUMERIC(3);

ALTER TABLE "public"."parcelas"
  ADD COLUMN "tipo_movimiento" VARCHAR(1);

ALTER TABLE "public"."parcelas"
  ADD COLUMN codigo_municipioDGC VARCHAR(3);

ALTER TABLE "public"."parcelas"
  ADD COLUMN BICE VARCHAR(2);

ALTER TABLE "public"."parcelas"
  ADD COLUMN codigo_provinciaINE VARCHAR(2);

ALTER TABLE "public"."parcelas"
  ADD COLUMN codigo_municipio_localizacionDGC VARCHAR(3);

ALTER TABLE "public"."parcelas"
  ADD COLUMN codigo_municipio_localizacionINE VARCHAR(3);

ALTER TABLE "public"."parcelas"
  ADD COLUMN codigo_municipio_origenDGC VARCHAR(3);

ALTER TABLE "public"."parcelas"
  ADD COLUMN "codigo_paraje" VARCHAR(5);

ALTER TABLE "public"."parcelas"
  ADD COLUMN "superficie_finca" NUMERIC(10,0);

ALTER TABLE "public"."parcelas"
  ADD COLUMN "superficie_construida_total" NUMERIC(7,0);

ALTER TABLE "public"."parcelas"
  ADD COLUMN "superficie_const_sobrerasante" NUMERIC(7,0);

ALTER TABLE "public"."parcelas"
  ADD COLUMN "superficie_const_bajorasante" NUMERIC(7,0);

ALTER TABLE "public"."parcelas"
  ADD COLUMN "superficie_cubierta" NUMERIC(7,0);

ALTER TABLE "public"."parcelas"
  ADD COLUMN "coordenada_x" NUMERIC(9,0);

ALTER TABLE "public"."parcelas"
  ADD COLUMN "coordenada_y" NUMERIC(10,0);

ALTER TABLE "public"."parcelas"
  ADD COLUMN "anio_aprobacion" NUMERIC(4,0);

ALTER TABLE "public"."parcelas"
  ADD COLUMN "codigo_calculo_valor" NUMERIC(2,0);

ALTER TABLE "public"."parcelas"
  ADD COLUMN "poligono_catastral_valoracion" VARCHAR(3);

ALTER TABLE "public"."parcelas"
  ADD COLUMN "modalidad_reparto" VARCHAR(1);

ALTER TABLE "public"."parcelas"
  ADD COLUMN "indicador_infraedificabilidad" VARCHAR(1);

ALTER TABLE "public"."parcelas"
  ADD COLUMN "movimiento_baja" VARCHAR(1);


DROP TABLE "public"."sujetopasivo";

DROP TABLE "public"."titulares";


-- Sequence: seq_elementostemporales
-- DROP SEQUENCE seq_elementostemporales;

CREATE SEQUENCE seq_elementostemporales
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE seq_elementostemporales OWNER TO postgres;

-- Sequence: seq_reparto
-- DROP SEQUENCE seq_reparto;

CREATE SEQUENCE seq_reparto
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE seq_reparto OWNER TO postgres;


CREATE SEQUENCE seq_entidadgeneradora
    INCREMENT 1  MINVALUE 1
    MAXVALUE 999999999  START 3  CACHE 1;
ALTER TABLE seq_entidadgeneradora OWNER TO postgres;




-- Sequence: seq_expediente
-- DROP SEQUENCE seq_expediente;

CREATE SEQUENCE seq_expediente
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE seq_expediente OWNER TO postgres;

-- Sequence: seq_Domicilio_localizacion
-- DROP SEQUENCE seq_Domicilio_localizacion;

CREATE SEQUENCE seq_Domicilio_localizacion
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE seq_Domicilio_localizacion OWNER TO postgres;

-- Sequence: seq_ficheros
-- DROP SEQUENCE seq_ficheros;

CREATE SEQUENCE seq_ficheros
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE seq_ficheros OWNER TO postgres;




INSERT INTO query_catalog (id, query)
VALUES 
  ('MCobtenerProvincias','select id, nombreoficial from provincias;');

INSERT INTO query_catalog (id, query)
VALUES 
  ('MCobtenerMunicipios','select id, nombreoficial from municipios where id_provincia=?');

INSERT INTO query_catalog (id, query)
VALUES 
  ('MCobtenerMunicipiosTotal','select id, nombreoficial from municipios');

INSERT INTO query_catalog (id, query)
VALUES 
  ('MCobtenerIdVia','select id from vias where codigocatastro=? and id_municipio=?');

INSERT INTO query_catalog (id, query)
VALUES 
  ('MCexisteFinca','select id from parcelas where referencia_catastral=? and fecha_baja is null');

INSERT INTO query_catalog (id, query)
VALUES 
  ('MCexisteSuelo','select identificador from suelo where identificador=?');

INSERT INTO query_catalog (id, query)
VALUES 
  ('MCactualizarSuelo','update suelo set anno_expediente=?,referencia_expediente=?,codigo_entidad_colaboradora=?,tipo_movimiento=?,\r\nparcela_catastral=?,numero_orden=?,longitud_fachada=?,tipo_fachada=?,superficie_elemento_suelo=?,\r\nfondo_elemento_suelo=?,codigo_via_ponencia=?,codigo_tramo_ponencia=?, zona_valor=?, zona_urbanistica=?,\r\ncodigo_tipo_valor=?, numero_fachadas=?, corrector_longitud_fachada=?, corrector_forma_irregular=?, corrector_desmonte_excesivo=?,\r\ncorrector_superficie_distinta=?, corrector_inedificabilidad=?, corrector_vpo=?, corrector_apreciacion_depreciacion=?, corrector_depreciacion_funcional=?,\r\ncorrector_cargas_singulares=?, corrector_situaciones_especiales=?, corrector_uso_no_lucrativo=?\r\nwhere identificador=?');

INSERT INTO query_catalog (id, query)
VALUES 
  ('MCinsertarSuelo','insert into suelo (anno_expediente,referencia_expediente,codigo_entidad_colaboradora,tipo_movimiento,parcela_catastral,\r\nnumero_orden,longitud_fachada,tipo_fachada,superficie_elemento_suelo,fondo_elemento_suelo,\r\ncodigo_via_ponencia,codigo_tramo_ponencia, zona_valor, zona_urbanistica,codigo_tipo_valor,\r\nnumero_fachadas, corrector_longitud_fachada, corrector_forma_irregular, corrector_desmonte_excesivo,corrector_superficie_distinta,\r\ncorrector_inedificabilidad, corrector_vpo, corrector_apreciacion_depreciacion, corrector_depreciacion_funcional, corrector_cargas_singulares,\r\ncorrector_situaciones_especiales, corrector_uso_no_lucrativo, identificador)\r\nvalues(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)');

INSERT INTO query_catalog (id, query)
VALUES 
  ('MCexisteUC','select identificador from unidad_constructiva where identificador=?');

INSERT INTO query_catalog (id, query)
VALUES 
  ('MCinsertarUC','insert into unidad_constructiva (anno_expediente, referencia_expediente, codigo_entidad_colaboradora, tipomovimiento,\r\nparcela_catastral, codigo_unidad_constructiva, clase_unidad, codigo_municipio_ine, codigo_municipio_dgc,\r\nnombre_entidad_menor, primer_numero, primera_letra, segundo_numero, segunda_letra,\r\nkilometro, bloque, direccion_no_estructurada, anio_construccion, indicador_exactitud,\r\nsuperficie_ocupada, longitud_fachada, codigo_via_ponencia, codigo_tramo_ponencia, zona_valor,\r\nnumero_fachadas, corrector_longitud_fachada, corrector_estado_conservacion,corrector_depreciacion_funcional, corrector_cargas_singulares,\r\ncorrector_situaciones_especiales, corrector_uso_no_lucrativo, identificador)\r\nvalues(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)');

INSERT INTO query_catalog (id, query)
VALUES 
  ('MCactualizarUC','update unidad_constructiva set anno_expediente=?, referencia_expediente=?, codigo_entidad_colaboradora=?, tipomovimiento=?,\r\nparcela_catastral=?, codigo_unidad_constructiva=?, clase_unidad=?, codigo_municipio_ine=?, codigo_municipio_dgc=?,\r\nnombre_entidad_menor=?, primer_numero=?, primera_letra=?, segundo_numero=?, segunda_letra=?,\r\nkilometro=?, bloque=?, direccion_no_estructurada=?, anio_construccion=?, indicador_exactitud=?,\r\nsuperficie_ocupada=?, longitud_fachada=?, codigo_via_ponencia=?, codigo_tramo_ponencia=?, zona_valor=?,\r\nnumero_fachadas=?, corrector_longitud_fachada=?, corrector_estado_conservacion=?,corrector_depreciacion_funcional=?, corrector_cargas_singulares=?,\r\ncorrector_situaciones_especiales=?, corrector_uso_no_lucrativo=?\r\nwhere identificador=?');

INSERT INTO query_catalog (id, query)
VALUES 
  ('MCexisteCultivo','select identificador from cultivos where identificador=?');

INSERT INTO query_catalog (id, query)
VALUES 
  ('MCinsertarCultivo','insert into cultivos (anno_expediente, referencia_expediente, codigo_entidad_colaboradora, tipo_movimiento, naturaleza_suelo,\r\nparcela_catastral, codigo_subparcela, numero_orden, tipo_subparcela, superficie_subparcela,\r\ncalificacion_cultivo, denominacion_cultivo, intensidad_productiva, bonificacion_subparcela, ejercicio_finalizacion,\r\nvalor_catastral, modalidad_reparto, identificador)\r\nvalues (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)');

INSERT INTO query_catalog (id, query)
VALUES 
  ('MCactualizarCultivo','update cultivos set anno_expediente=?, referencia_expediente=?, codigo_entidad_colaboradora=?, tipo_movimiento=?, naturaleza_suelo=?,\r\nparcela_catastral=?, codigo_subparcela=?, numero_orden=?, tipo_subparcela=?, superficie_subparcela=?,\r\ncalificacion_cultivo=?, denominacion_cultivo=?, intensidad_productiva=?, bonificacion_subparcela=?, ejercicio_finalizacion=?,\r\nvalor_catastral=?, modalidad_reparto=?\r\nwhere identificador=?');

INSERT INTO query_catalog (id, query)
VALUES 
  ('MCexisteConstruccion','select identificador from construccion where identificador=?');

INSERT INTO query_catalog (id, query)
VALUES 
  ('MCactualizarConstruccion','update construccion set anno_expediente=?, referencia_expediente=?, codigo_entidad_colaboradora=?, tipo_movimiento=?, parcela_catastral=?,\r\nnumero_orden_construccion=?, numero_orden_bieninmueble=?, codigo_unidadconstructiva=?, bloque=?, escalera=?,\r\nplanta=?, puerta=?, codigo_destino_dgc=?, indicador_tipo_reforma=?, anno_reforma=?,\r\nanno_antiguedad=?, indicador_local_interior=?, superficie_total_local=?, superficie_terrazas_local=?, superficie_imputable_local=?,\r\ntipologia_constructiva=?, codigo_uso_predominante=?, codigo_categoria_predominante=?, codigo_modalidad_reparto=?, codigo_tipo_valor=?,\r\ncorrector_apreciacion_economica=?, corrector_vivienda=?\r\nwhere identificador=?');

INSERT INTO query_catalog (id, query)
VALUES 
  ('MCexisteBI','select identificador from bien_inmueble where identificador=?');

INSERT INTO query_catalog (id, query)
VALUES 
  ('MCexisteFxcc','select id_finca from fxcc where id_finca=?');

INSERT INTO query_catalog (id, query)
VALUES 
  ('MCinsertarFxcc','insert into fxcc (\"DXF\", \"ASC\", id_finca) values (?, ?, ?)');

INSERT INTO query_catalog (id, query)
VALUES 
  ('MCactualizarFxcc','update fxcc set \"DXF\"=?, \"ASC\"=? where id_finca=?');

INSERT INTO query_catalog (id, query)
VALUES 
  ('MCobtenerViasCatastro','select id, tipovianormalizadocatastro,nombrecatastro from vias where id_municipio=? and upper(nombrecatastro) like upper(?)');

INSERT INTO query_catalog (id, query)
VALUES 
  ('MCobtenerParajesCatastro','select nombreparaje from parcelas where id_municipio=? and upper(nombreparaje) like upper(?)');

INSERT INTO query_catalog (id, query)
VALUES 
  ('MCinsertarPersona','insert into persona (anno_expediente, referencia_expediente, codigo_entidad_colaboradora, razon_social, ausencia_nif,\r\ncodigo_provincia_ine, codigo_municipio_dgc, codigo_municipio_ine, entidad_menor, id_via,\r\nprimer_numero, primera_letra, segundo_numero, segunda_letra, escalera,\r\nkilometro, bloque, planta, puerta, direccion_no_estructurada,\r\ncodigo_postal, apartado_correos, nif_conyuge, nif_cb, complemento_titularidad,\r\nfecha_alteracion, nif ,parcela_catastral, numero_cargo, digito_control1,\r\ndigito_control2)\r\nvalues (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)');

INSERT INTO query_catalog (id, query)
VALUES 
  ('MCexisteRepresentante','select nifrepresentante from representante where id_bieninmueble=?');

INSERT INTO query_catalog (id, query)
VALUES 
  ('MCinsertarRepresentante','insert into representante (anno_expediente, referencia_expediente, codigo_entidad_colaboradora, razonsocial_representante, codigo_provincia_ine,\r\ncodigo_municipio_dgc, codigo_municipio_ine, entidad_menor, id_via, primer_numero,\r\nprimera_letra, segundo_numero, segunda_letra, kilometro, bloque,\r\nescalera, planta, puerta, direccion_no_estructurada, codigo_postal,\r\napartado_correos, id_bieninmueble, nifrepresentante)\r\nvalues (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)');

INSERT INTO query_catalog (id, query)
VALUES 
  ('MCinsertarVia','insert into vias (id, codigocatastro, tipovianormalizadocatastro, nombrecatastro, id_municipio)\r\nvalues (?,?,?,?,?)');

INSERT INTO query_catalog (id, query)
VALUES 
  ('MCnextSecuenciaVia','select nextval(''seq_vias'')');

INSERT INTO query_catalog (id, query)
VALUES 
  ('MCinsertarFinca','insert into parcelas (referencia_catastral,\r\nid_municipio, primer_numero, primera_letra, segundo_numero,\r\nsegunda_letra, kilometro, bloque, direccion_no_estructurada, codigo_postal,\r\nfecha_alta, fecha_baja, codigodelegacionmeh, nombreentidadmenor,id_distrito,\r\ncodigozonaconcentracion,codigopoligono,codigoparcela,nombreparaje,id_via,\r\nanno_expediente,referencia_expediente,codigo_entidad_colaboradora,tipo_movimiento,codigo_municipioDGC,\r\nBICE,codigo_provinciaINE, codigo_municipio_localizacionDGC, codigo_municipio_localizacionINE, codigo_municipio_origenDGC,\r\ncodigo_paraje,superficie_finca, superficie_construida_total,superficie_const_sobrerasante,superficie_cubierta,\r\ncoordenada_x, coordenada_y, anio_aprobacion, codigo_calculo_valor, poligono_catastral_valoracion,\r\nmodalidad_reparto, indicador_infraedificabilidad, movimiento_baja, id)\r\nvalues(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)');

INSERT INTO query_catalog (id, query)
VALUES 
  ('MCborrarSituacionInicialTitularidad','delete from persona where parcela_catastral=? and numero_cargo=? and digito_control1=? and digito_control2=?');

INSERT INTO query_catalog (id, query)
VALUES 
  ('MCinsertarDerecho','insert into derechos (codigo_derecho, porcentaje_derecho, ordinal_derecho, niftitular, id_bieninmueble) values (?,?,?,?,?)');

INSERT INTO query_catalog (id, query)
VALUES 
  ('MCactualizarFinca','update parcelas set referencia_catastral=?, id_municipio=?, primer_numero=?, primera_letra=?, segundo_numero=?,\r\nsegunda_letra=?, kilometro=?, bloque=?, direccion_no_estructurada=?, codigo_postal=?,\r\nfecha_alta=?, fecha_baja=?, codigodelegacionmeh=?, nombreentidadmenor=?,id_distrito=?,\r\ncodigozonaconcentracion=?,codigopoligono=?,codigoparcela=?,nombreparaje=?,id_via=?,\r\nanno_expediente=?,referencia_expediente=?,codigo_entidad_colaboradora=?,tipo_movimiento=?,codigo_municipioDGC=?,\r\nBICE=?,codigo_provinciaINE=?, codigo_municipio_localizacionDGC=?, codigo_municipio_localizacionINE=?, codigo_municipio_origenDGC=?,\r\ncodigo_paraje=?,superficie_finca=?, superficie_construida_total=?,superficie_const_sobrerasante=?,superficie_cubierta=?,\r\ncoordenada_x=?, coordenada_y=?, anio_aprobacion=?, codigo_calculo_valor=?, poligono_catastral_valoracion=?,\r\nmodalidad_reparto=?, indicador_infraedificabilidad=?, movimiento_baja=?\r\nwhere id=?');

INSERT INTO query_catalog (id, query)
VALUES 
  ('MCborrarDerechoBI','delete from derechos where id_bieninmueble=?');

INSERT INTO query_catalog (id, query)
VALUES 
  ('MCseleccionarExpediente','select expediente.id_entidad_generadora, codigo, descripcion, nombre, tipo,\r\nid_estado, id_tecnico_catastro,id_localizacion, id_municipio, numero_expediente,\r\ntipo_expediente, fecha_alteracion, anio_expediente_gerencia, referencia_expediente_gerencia, codigo_entidad_registro_dgc_origen_alteracion,\r\nanio_expediente_admin_origen_alteracion, referencia_expediente_admin_origen, fecha_registro, fecha_movimiento, hora_movimiento,\r\ntipo_documento_origen_alteracion, info_documento_origen_alteracion, codigo_descriptivo_alteracion, descripcion_alteracion, nif_presentador,\r\nnombre_completo_presentador, num_bienesinmuebles_urb, num_bienesinmuebles_rus, num_bienesinmuebles_esp, fecha_de_cierre,\r\ncod_provincia_notaria, cod_poblacion_notaria, cod_notaria, anio_protocolo_notarial, protocolo_notarial\r\nfrom expediente inner join entidad_generadora\r\non expediente.id_entidad_generadora=entidad_generadora.id_entidad_generadora\r\nwhere id_expediente=?');

INSERT INTO query_catalog (id, query)
VALUES 
  ('MCexisteExpediente','select id_expediente from expediente where numero_expediente=?');

INSERT INTO query_catalog (id, query)
VALUES 
  ('MCnextSecuenciaEntidadGeneradora','select nextval(''seq_entidadgeneradora'')');

INSERT INTO query_catalog (id, query)
VALUES 
  ('MCactualizarEntidadGeneradora','update entidad_generadora set codigo=?, tipo=?, descripcion=?, nombre=?\r\nwhere id_entidad_generadora=?');

INSERT INTO query_catalog (id, query)
VALUES 
  ('MCinsertarEntidadGeneradora','insert into entidad_generadora(codigo, tipo, descripcion, nombre, id_entidad_generadora)\r\nvalues(?,?,?,?,?)');

INSERT INTO query_catalog (id, query)
VALUES 
  ('MCexisteEntidadGeneradora','select id_entidad_generadora from entidad_generadora where codigo=?');

INSERT INTO query_catalog (id, query)
VALUES 
  ('MCnextSecuenciaExpediente','select nextval(''seq_expediente'')');

INSERT INTO query_catalog (id, query)
VALUES 
  ('MCnextSecuenciaFinca','select nextval(''seq_parcelas'')');

INSERT INTO query_catalog (id, query)
VALUES 
  ('MCexisteRepartoCultivo','select id_reparto from repartoscultivos where id_cultivoorigen=\r\n(select identificador from cultivos where parcela_catastral=?\r\nand codigo_subparcela=? and calificacion_cultivo=?)\r\nand id_bieninmuebledestino=(select identificador from bien_inmueble\r\nwhere parcela_catastral=? and numero_cargo=?)');

INSERT INTO query_catalog (id, query)
VALUES 
  ('MCinsertarRepartoCultivo','insert into repartoscultivos (tipo_registro, anno_expediente, referencia_expediente, codigo_entidad_colaboradora, tipo_movimiento,\r\nid_cultivoorigen, porcentaje_reparto, id_bieninmuebledestino, id_reparto)\r\nvalues (?,?,?,?,?,\r\nselect identificador from cultivos where parcela_catastral=? and codigo_subparcela=? and calificacion_cultivo=?,?,\r\nselect identificador from bien_inmueble where parcela_catastral=? and numero_cargo=?,\r\nnextval(''seq_reparto''))');

INSERT INTO query_catalog (id, query)
VALUES 
  ('MCactualizarRepartoCultivo','update repartoscultivos set tipo_registro=?, anno_expediente=?, referencia_expediente=?, codigo_entidad_colaboradora=?, tipo_movimiento=?,\r\nid_cultivoorigen=(select identificador from cultivos where parcela_catastral=? and codigo_subparcela=? and calificacion_cultivo=?), porcentaje_reparto=?,\r\nid_bieninmuebledestino=(select identificador from bien_inmueble where parcela_catastral=? and numero_cargo=?)\r\nwhere id_reparto=?');

INSERT INTO query_catalog (id, query)
VALUES 
  ('MCexisteRepartoConstruccion','select id_reparto from repartosconstrucciones where id_construccionorigen=\r\n(select identificador from construccion where parcela_catastral=?\r\nand numero_orden_construccion=?)\r\nand id_construcciondestino=(select identificador from construccion\r\nwhere parcela_catastral=?\r\nand numero_orden_construccion=?)');

INSERT INTO query_catalog (id, query)
VALUES 
  ('MCinsertarRepartoConstruccion','insert into repartosconstrucciones (tipo_registro, anno_expediente, referencia_expediente, codigo_entidad_colaboradora, tipo_movimiento,\r\nid_construccionorigen, porcentaje_reparto, id_construcciondestino, id_reparto)\r\nvalues (?,?,?,?,?,\r\n(select identificador from construccion where parcela_catastral=?\r\nand numero_orden_construccion=?),?,\r\n(select identificador from construccion where parcela_catastral=?\r\nand numero_orden_construccion=?),\r\nnextval(''seq_reparto''))');

INSERT INTO query_catalog (id, query)
VALUES 
  ('MCactualizarRepartoConstruccion','update repartosconstrucciones set\r\ntipo_registro=?, anno_expediente=?, referencia_expediente=?, codigo_entidad_colaboradora=?, tipo_movimiento=?,\r\nid_construccionorigen=(select identificador from construccion where parcela_catastral=?\r\nand numero_orden_construccion=?), porcentaje_reparto,\r\nid_construcciondestino=(select identificador from construccion where parcela_catastral=?\r\nand numero_orden_construccion=?) where id_reparto=?');

INSERT INTO query_catalog (id, query)
VALUES 
  ('MCexisteRepartoConsBi','select id_reparto from repartosconsbi where idconstruccion_origen=\r\n(select identificador from construccion where parcela_catastral=?\r\nand numero_orden_construccion=?)\r\nand id_bieninmuebledestino=(select identificador\r\nfrom bien_inmueble where parcela_catastral=? and numero_cargo=?)');

INSERT INTO query_catalog (id, query)
VALUES 
  ('MCinsertarRepartoConsBi','insert into repartosconsbi\r\n(tipo_registro, anno_expediente, referencia_expediente, codigo_entidad_colaboradora, tipo_movimiento,\r\nidconstruccion_origen, porcentaje_reparto, id_bieninmuebledestino, id_reparto)\r\nvalues (?,?,?,?,?,\r\n(select identificador from construccion where parcela_catastral=?\r\nand numero_orden_construccion=?),?,\r\n(select identificador from bien_inmueble where parcela_catastral=?\r\nand numero_cargo=?),\r\nnextval(''seq_reparto''))');

INSERT INTO query_catalog (id, query)
VALUES 
  ('MCactualizarRepartoConsBi','update repartosconsbi set\r\ntipo_registro=?, anno_expediente=?, referencia_expediente=?, codigo_entidad_colaboradora=?, tipo_movimiento=?,\r\nidconstruccion_origen=(select identificador from construccion where parcela_catastral=?\r\nand numero_orden_construccion=?), porcentaje_reparto,\r\nid_bieninmuebledestino=(select identificador from bien_inmueble where parcela_catastral=?\r\nand numero_cargo=?) where id_reparto=?');

INSERT INTO query_catalog (id, query)
VALUES 
  ('MCobtenerParcelasTemporal','select catastro_temporal.id_expediente, numero_expediente, id_elemento_temporal, xml, expediente_finca_catastro.ref_catastral, parcelas.id\r\nfrom expediente,catastro_temporal\r\nleft join expediente_finca_catastro\r\ninner join parcelas\r\non parcelas.referencia_catastral=expediente_finca_catastro.ref_catastral\r\non catastro_temporal.id_expediente=expediente_finca_catastro.id_expediente\r\nwhere parcelas.fecha_baja is null and catastro_temporal.id_expediente = expediente.id_expediente');

INSERT INTO query_catalog (id, query)
VALUES 
  ('MCnextSecuenciaDomicilioLocalizacion','select nextval(''seq_domicilio_localizacion'')');

INSERT INTO query_catalog (id, query)
VALUES 
  ('MCinsertarDomicilioLocalizacion','insert into domicilio_localizacion (id_localizacion, provincia_ine, municipio_ine, codigo_municipiodgc, nombre_entidad_menor,\r\ncodigo_via, primer_numero, primera_letra, segundo_numero, segunda_letra,\r\nkilometro, bloque, escalera, planta, puerta,\r\ndireccion_no_estructurada, codigo_postal, nombre_via, apartado_correos, tipo_via,\r\nnombre_provincia, nombre_municipio)\r\nvalues (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)');

INSERT INTO query_catalog (id, query)
VALUES 
  ('MCinsertarBI','insert into bien_inmueble(anno_expediente, referencia_expediente, codigo_entidad_generadora,tipo_movimiento, clase_bieninmueble,\r\nparcela_catastral, numero_cargo, primer_caracter_control, segundo_caracter_control, numero_fijo_inmueble,\r\nidentificacion_bieninmueble, numero_fincaregistral, codigo_municipiodgc, nombre_entidad_menor, id_via,\r\nprimer_numero, primera_letra, segundo_numero, segunda_letra, kilometro,\r\nbloque, escalera, planta, puerta, direccion_no_estructurada,\r\ncodigo_postal, distrito, municipio_origen_agregacion, zona_concentracion, poligono_rustico,\r\nparcela, paraje, nombre_paraje, precio_declarado, origen_precio,\r\nprecio_venta, anno_finalizacion_valoracion, tipo_propiedad, numero_orden_horizontal, anno_antiguedad,\r\nanio_valor_catastral, valor_catastral, valor_catastral_suelo, valor_catastral_construccion, base_liquidable,\r\nclave_uso_dgc, importe_bonificacion, clave_bonificacion, superficie_elementos_constructivos, superficie_finca,\r\ncoeficiente_propiedad, valor_base, procedencia_valorbase, ejercicio_ibi, valor_catastral_ibi,\r\nejercicio_revision, ejercicio_revision_parcial, periodo_total, id_representante, identificador)\r\nvalues (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)');

INSERT INTO query_catalog (id, query)
VALUES 
  ('MCactualizarBI','update bien_inmueble set anno_expediente=?, referencia_expediente=?, codigo_entidad_generadora=?,tipo_movimiento=?, clase_bieninmueble=?,\r\nparcela_catastral=?, numero_cargo=?, primer_caracter_control=?, segundo_caracter_control=?, numero_fijo_inmueble=?,\r\nidentificacion_bieninmueble=?, numero_fincaregistral=?, codigo_municipiodgc=?, nombre_entidad_menor=?, id_via=?,\r\nprimer_numero=?, primera_letra=?, segundo_numero=?, segunda_letra=?, kilometro=?,\r\nbloque=?, escalera=?, planta=?, puerta=?, direccion_no_estructurada=?,\r\ncodigo_postal=?, distrito=?, municipio_origen_agregacion=?, zona_concentracion=?, poligono_rustico=?,\r\nparcela=?, paraje=?, nombre_paraje=?, precio_declarado=?, origen_precio=?,\r\nprecio_venta=?, anno_finalizacion_valoracion=?, tipo_propiedad=?, numero_orden_horizontal=?, anno_antiguedad=?,\r\nanio_valor_catastral=?, valor_catastral=?, valor_catastral_suelo=?, valor_catastral_construccion=?, base_liquidable=?,\r\nclave_uso_dgc=?, importe_bonificacion=?, clave_bonificacion=?, superficie_elementos_constructivos=?, superficie_finca=?,\r\ncoeficiente_propiedad=?, valor_base=?, procedencia_valorbase=?, ejercicio_ibi=?, valor_catastral_ibi=?,\r\nejercicio_revision=?, ejercicio_revision_parcial=?, periodo_total=?, id_representante=?\r\nwhere identificador=?');

INSERT INTO query_catalog (id, query)
VALUES 
  ('MCinsertarConstruccion','insert into construccion (anno_expediente, referencia_expediente, codigo_entidad_colaboradora, tipo_movimiento, parcela_catastral,\r\nnumero_orden_construccion, numero_orden_bieninmueble, codigo_unidadconstructiva, bloque, escalera,\r\nplanta, puerta, codigo_destino_dgc, indicador_tipo_reforma, anno_reforma,\r\nanno_antiguedad, indicador_local_interior, superficie_total_local, superficie_terrazas_local, superficie_imputable_local,\r\ntipologia_constructiva, codigo_uso_predominante, codigo_categoria_predominante, codigo_modalidad_reparto, codigo_tipo_valor,\r\ncorrector_apreciacion_economica, corrector_vivienda, identificador)\r\nvalues (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)');

INSERT INTO query_catalog (id, query)
VALUES 
  ('MCactualizarRepresentante','update representante set nifrepresentante=?, anno_expediente=?, referencia_expediente=?, codigo_entidad_colaboradora=?, razonsocial_representante=?, codigo_provincia_ine=?,\r\ncodigo_municipio_dgc=?, codigo_municipio_ine=?, entidad_menor=?, id_via=?, primer_numero=?,\r\nprimera_letra=?, segundo_numero=?, segunda_letra=?, kilometro=?, bloque=?,\r\nescalera=?, planta=?, puerta=?, direccion_no_estructurada=?, codigo_postal=?,\r\napartado_correos=? where id_bieninmueble=? and nifrepresentante=?');

INSERT INTO query_catalog (id, query)
VALUES 
  ('MCactualizarExpediente','update expediente set id_entidad_generadora=?,\r\nid_estado=?, id_tecnico_catastro=?,id_localizacion=?, id_municipio=?, numero_expediente=?,\r\nid_tipo_expediente=(select id_tipo_expediente from tipo_expediente where codigo_tipo_expediente=?), fecha_alteracion=?, anio_expediente_gerencia=?, referencia_expediente_gerencia=?, codigo_entidad_registro_dgc_origen_alteracion=?,\r\nanio_expediente_admin_origen_alteracion=?, referencia_expediente_admin_origen=?, fecha_registro=?, fecha_movimiento=?, hora_movimiento=?,\r\ntipo_documento_origen_alteracion=?, info_documento_origen_alteracion=?, codigo_descriptivo_alteracion=?, descripcion_alteracion=?, nif_presentador=?,\r\nnombre_completo_presentador=?, num_bienesinmuebles_urb=?, num_bienesinmuebles_rus=?, num_bienesinmuebles_esp=?, fecha_de_cierre=?,\r\ncod_provincia_notaria=?, cod_poblacion_notaria=?, cod_notaria=?, anio_protocolo_notarial=?, protocolo_notarial=?\r\nwhere id_expediente=?');

INSERT INTO query_catalog (id, query)
VALUES 
  ('MCinsertarExpediente','insert into expediente (id_entidad_generadora,\r\nid_estado, id_tecnico_catastro,id_localizacion, id_municipio, numero_expediente,\r\nid_tipo_expediente, fecha_alteracion, anio_expediente_gerencia, referencia_expediente_gerencia, codigo_entidad_registro_dgc_origen_alteracion,\r\nanio_expediente_admin_origen_alteracion, referencia_expediente_admin_origen, fecha_registro, fecha_movimiento, hora_movimiento,\r\ntipo_documento_origen_alteracion, info_documento_origen_alteracion, codigo_descriptivo_alteracion, descripcion_alteracion, nif_presentador,\r\nnombre_completo_presentador, num_bienesinmuebles_urb, num_bienesinmuebles_rus, num_bienesinmuebles_esp, fecha_de_cierre,\r\ncod_provincia_notaria, cod_poblacion_notaria, cod_notaria, anio_protocolo_notarial, protocolo_notarial,\r\nid_expediente)\r\nvalues (?,?,?,?,?,?,(select id_tipo_expediente from tipo_expediente where codigo_tipo_expediente=?),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)');

INSERT INTO query_catalog (id, query)
VALUES 
  ('MCexistePersona','select nif from persona where nif=? and parcela_catastral=? and numero_cargo=? and digito_control1=? and digito_control2=?');

INSERT INTO query_catalog (id, query)
VALUES 
  ('MCactualizarPersona','update persona set anno_expediente=?, referencia_expediente=?, codigo_entidad_colaboradora=?, razon_social=?, ausencia_nif=?,\r\ncodigo_provincia_ine=?, codigo_municipio_dgc=?, codigo_municipio_ine=?, entidad_menor=?, id_via=?,\r\nprimer_numero=?, primera_letra=?, segundo_numero=?, segunda_letra=?, escalera=?,\r\nkilometro=?, bloque=?, planta=?, puerta=?, direccion_no_estructurada=?,\r\ncodigo_postal=?, apartado_correos=?, nif_conyuge=?, nif_cb=?, complemento_titularidad=?,\r\nfecha_alteracion=?\r\nwhere nif=? and parcela_catastral=? and numero_cargo=? and\r\ndigito_control1=? and digito_control2=?');

INSERT INTO query_catalog (id, query)
VALUES 
  ('MCactualizarDerecho','update derechos set codigo_derecho=?, porcentaje_derecho=?, ordinal_derecho=? where niftitular=? and id_bieninmueble=?');

INSERT INTO query_catalog (id, query)
VALUES 
  ('MCexisteDerecho','select niftitular from derechos where niftitular=? and id_bieninmueble=?');
