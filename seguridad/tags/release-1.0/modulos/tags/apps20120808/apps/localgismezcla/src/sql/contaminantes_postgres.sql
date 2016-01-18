insert into query_catalog values ('allarbolados','select ID,observaciones,extension_verde from zonas_arboladas where id_municipio=?');
insert into query_catalog values ('getcontaminante','select c.id as id_actividad,c.id_tipo_actividad as tipo_actividad,
c.id_razon_estudio as razon_estudio,c.num_administrativo as numeroadm,c.asunto as asunto,c.fecha_inicio as fechaini,
c.fecha_fin as fechafin,c.tipo_via_afecta as via,c.nombre_via_afecta as nombre_via,c.numero_via_afecta as numero_via,
c.cpostal_afecta as cpostal,i.id as id_inspeccion,i.id_responsable as id_responsable,i.num_folios as num_folios,
i.fecha_inicio as fecha_inicio,i.fecha_fin as fecha_fin,i.fecha_inicio_rec_datos as fecha_inicio_rec_datos,
i.fecha_fin_rec_datos as fecha_fin_rec_datos,i.num_dias_rec_datos as num_dias_rec_datos,i.puntos_fijos_medicion,i.puntos_moviles_medicion, i.sustancias_contaminantes,
i.concentracion_min, i.concentracion_max, i.es_zona_latente, i.motivos_zona_latente, i.es_zona_saturada,
i.motivos_zona_saturada, i.factores_de_riesgo, i.medidas_a_adoptar,i.resultados, i.observaciones
from actividad_contaminante as c
LEFT OUTER JOIN inspeccion as i on (c.id=i.id_actividad)
where c.id_municipio=? and c.id=?');

insert into query_catalog values ('getAnexos','select id_anexo, id_tipo_anexo, url_fichero, observacion from anexo where id_inspeccion=?');




INSERT INTO SEQUENCES ( ID_SEQUENCE, TABLENAME, FIELD, VALUE, INCREMENTVALUE, MINIMUMVALUE,
MAXIMUMVALUE, CIRCULAR ) VALUES ( 
28, 'RESPONSABLE_INSPECTOR', 'ID_RESPONSABLE', 10001, 1, 1, 999999999, 'T');
INSERT INTO SEQUENCES ( ID_SEQUENCE, TABLENAME, FIELD, VALUE, INCREMENTVALUE, MINIMUMVALUE,
MAXIMUMVALUE, CIRCULAR ) VALUES ( 
29, 'RESIDUO_MUNICIPAL', 'ID_CONTENEDOR', 10001, 1, 1, 999999999, 'T');
INSERT INTO SEQUENCES ( ID_SEQUENCE, TABLENAME, FIELD, VALUE, INCREMENTVALUE, MINIMUMVALUE,
MAXIMUMVALUE, CIRCULAR ) VALUES ( 
30, 'INSPECCION', 'ID', 10001, 1, 1, 999999999, 'T');

UPDATE DOMAINs SET NAME='RAZON_ESTUDIO' WHERE ID=71;
UPDATE DOMAINs SET NAME='TIPO_ACTIVIDAD_CONTAMINANTE' WHERE ID=72;

CREATE TABLE zonas_arboladas
(
  ID numeric(10) NOT NULL,
  id_municipio numeric(5) NOT NULL,
  extension_verde numeric(14,2),
  observaciones varchar(1000),
  CONSTRAINT zona_arbolada_pkey PRIMARY KEY (ID)
);
select AddGeometryColumn ('pista', 'zonas_arboladas', 'GEOMETRY', 23030, 'GEOMETRY', 2);

//CREAR MAPA ZONAS ARBOLADAS
insert into dictionary (id_vocablo,locale,traduccion) values (8888,'es_ES','Arboleda');
insert into layers (id_layer,id_name,acl,name,id_styles) values (8888,8888,12,'Arboleda',9);
insert into queries (id, id_layer,databasetype,selectquery,updatequery,insertquery,deletequery)
 values (8888,8888,1,
'select zonas_arboladas.ID,
       zonas_arboladas.extension_verde,
       zonas_arboladas.observaciones ,
       zonas_arboladas."GEOMETRY" from zonas_arboladas where zonas_arboladas.id_municipio=?M',
'update zonas_arboladas set "GEOMETRY"=GeometryFromText(?4,23030), extension_verde=?2
, observaciones=?3 where ID=?1','INSERT INTO zonas_arboladas("GEOMETRY",ID,ID_Municipio,extension_verde,observaciones) VALUES
(GeometryFromText(?4,23030),(nextval(''SEQ_ZONAS_ARBOLADAS'')),?M,?2,?3)','TODO');

insert into tables(id_table,name) values (8888,'zonas_arboladas');
insert into dictionary (id_vocablo,locale,traduccion) values (8889,'es_ES','Id');
insert into dictionary (id_vocablo,locale,traduccion) values (8890,'es_ES','Extension Verde');
insert into dictionary (id_vocablo,locale,traduccion) values (8891,'es_ES','Observaciones');


insert into columns (id,name,id_table,"Type","Scale",id_domain) values (88880,'ID',8888,2,0,118);
insert into columns (id,name,id_table,"Type","Scale",id_domain) values (88881,'extension_verde',8888,2,2,117);
insert into columns (id,name,id_table,"Type","Scale",id_domain) values (88882,'observaciones',8888,3,0,118);
insert into columns (id,name,id_table,"Type",id_domain) values (88883,'GEOMETRY',8888,1,118);


insert into attributes (id, id_alias,id_layer,id_column, position,editable) values
                        (88881,8889,8888,88880,1,false);
insert into attributes (id, id_alias,id_layer,id_column, position,editable) values
                        (88882,8890,8888,88881,2,true);
insert into attributes (id, id_alias,id_layer,id_column, position,editable) values
                        (88883,8891,8888,88882,3,true);
insert into attributes (id, id_alias,id_layer,id_column, position,editable) values
                        (88884,2001,8888,88883,4,false);
                        
                        
insert into dictionary (id_vocablo,locale,traduccion) values (8892,'es_ES','zonas_arboladas');
insert into dictionary (id_vocablo,locale,traduccion) values (8893,'es_ES','Descripción mapa zonas arboladas');

insert into layerfamilies (id_layerfamily,id_name, id_description) values (100,8892,8893);

insert into layerfamilies_layers_relations (id_layer, id_layerfamily) values (8888,100);
        


insert into maps(id_map,id_name )values(100,8892);
insert into maps_layerfamilies_relations (id_map,id_layerfamily, position) values (100,100,1);
create sequence SEQ_ZONAS_ARBOLADAS; 

  
//Tablas para vertederos
CREATE TABLE vertedero
(
  ID numeric(10) NOT NULL,
  id_municipio numeric(5) NOT NULL,
  tipo varchar(50),
  titularidad varchar(50),
  gestion_administrativa varchar(50),
  problemas_existentes varchar(1000),
  capacidad numeric(10),
  grado_ocupacion numeric(8,2),
  posibilidad_ampliacion varchar(50),
  estado_conservacion varchar(50),
  vida_util numeric(4),
  CONSTRAINT vertedero_pkey PRIMARY KEY (ID)
);
select AddGeometryColumn ('pista', 'vertedero', 'GEOMETRY', 23030, 'GEOMETRY', 2);

create table residuo_municipal
(
	id_contenedor numeric(10) not null,
	id_vertedero numeric(10) not null,
	tipo varchar(50),
	ratio numeric (8,2),
	situacion varchar(255),
	media_recoleccion_diaria numeric(10),
	media_recoleccion_anual numeric(10),
	CONSTRAINT contenedor_pkey PRIMARY KEY (id_contenedor)
)

alter table residuo_municipal add  foreign key (id_vertedero) references vertedero(ID);


insert into query_catalog  (id,query) values ('allvertederos', '
select vertedero.ID as id_vertedero,vertedero.tipo as tipovertedero,
       vertedero.titularidad as titularidad, vertedero.gestion_administrativa as gadministrativa, 
       vertedero.problemas_existentes as problemas, vertedero.capacidad as capacidad,
       vertedero.grado_ocupacion as ocupacion, vertedero.posibilidad_ampliacion as ampliacion,
       vertedero.estado_conservacion as estado, vertedero.vida_util as vida_util,
       residuo_municipal.id_contenedor as id_contenedor, residuo_municipal.tipo as tipo_residuo,
       residuo_municipal.ratio as ratio, residuo_municipal.situacion as situacion,
       residuo_municipal.media_recoleccion_diaria as diaria, residuo_municipal.media_recoleccion_anual as anual
       from vertedero 
       LEFT OUTER JOIN residuo_municipal on (vertedero.ID=residuo_municipal.id_vertedero) 
       where vertedero.id_municipio=? order by vertedero.ID');
       
      

//CREAR MAPA VERTEDERO
insert into dictionary (id_vocablo,locale,traduccion) values (7777,'es_ES','Vertedero');

insert into layers (id_layer,id_name,acl,name,id_styles) values (7777,7777,12,'Vertedero',9);


insert into queries (id, id_layer,databasetype,selectquery,updatequery,insertquery,deletequery)
 values (7777,7777,1,
'select vertedero.ID,vertedero.tipo,vertedero.titularidad,vertedero.gestion_administrativa,vertedero.problemas_existentes,
vertedero.capacidad,vertedero.grado_ocupacion,vertedero.posibilidad_ampliacion,vertedero.estado_conservacion,vertedero.vida_util, vertedero."GEOMETRY" from vertedero where vertedero.id_municipio=?M',
'update vertedero set "GEOMETRY"=GeometryFromText(?11,23030), tipo=?2, titularidad=?3,gestion_administrativa=?4,
problemas_existentes=?5,capacidad=?6,grado_ocupacion=?7,posibilidad_ampliacion=?8,estado_conservacion=?9,
vida_util=?10 where ID=?1',
'insert into vertedero (ID,tipo,titularidad,gestion_administrativa,problemas_existentes,
capacidad, grado_ocupacion, posibilidad_ampliacion, estado_conservacion, vida_util, "GEOMETRY", id_municipio)
values ((nextval(''SEQ_VERTEDERO'')), ?2,?3,?4,?5,?6,?7,?8,?9,?10,(GeometryFromText(?11,23030)),?M)','TODO');


insert into tables(id_table,name) values (7777,'vertedero');

insert into dictionary (id_vocablo,locale,traduccion) values (7701,'es_ES','Id');
insert into dictionary (id_vocablo,locale,traduccion) values (7702,'es_ES','tipo');
insert into dictionary (id_vocablo,locale,traduccion) values (7703,'es_ES','titularidad');
insert into dictionary (id_vocablo,locale,traduccion) values (7704,'es_ES','gestion_administrativa');
insert into dictionary (id_vocablo,locale,traduccion) values (7705,'es_ES','problemas_existentes');
insert into dictionary (id_vocablo,locale,traduccion) values (7706,'es_ES','capacidad');
insert into dictionary (id_vocablo,locale,traduccion) values (7707,'es_ES','grado_ocupacion');
insert into dictionary (id_vocablo,locale,traduccion) values (7708,'es_ES','posibilidad_ampliacion');
insert into dictionary (id_vocablo,locale,traduccion) values (7709,'es_ES','estado_conservacion');
insert into dictionary (id_vocablo,locale,traduccion) values (7710,'es_ES','vida_util');
insert into dictionary (id_vocablo,locale,traduccion) values (7711,'es_ES','geometria');



insert into columns (id,name,id_table,"Type","Scale",id_domain) values (7701,'ID',7777,2,2,118);
insert into columns (id,name,id_table,"Type","Scale",id_domain) values (7702,'tipo',7777,3,3,118);
insert into columns (id,name,id_table,"Type","Scale",id_domain) values (7703,'titularidad',7777,3,0,118);
insert into columns (id,name,id_table,"Type","Scale",id_domain) values (7704,'gestion_administrativa',7777,3,0,118);
insert into columns (id,name,id_table,"Type","Scale",id_domain) values (7705,'problemas_existentes',7777,3,0,118);
insert into columns (id,name,id_table,"Type","Scale",id_domain) values (7706,'capacidad',7777,2,0,118);
insert into columns (id,name,id_table,"Type","Scale",id_domain) values (7707,'grado_ocupacion',7777,3,0,118);
insert into columns (id,name,id_table,"Type","Scale",id_domain) values (7708,'posibilidad_ampliacion',7777,3,0,118);
insert into columns (id,name,id_table,"Type","Scale",id_domain) values (7709,'estado_conservacion',7777,3,0,118);
insert into columns (id,name,id_table,"Type","Scale",id_domain) values (7710,'vida_util',7777,3,0,118);
insert into columns (id,name,id_table,"Type",id_domain) values (7711,'GEOMETRY',7777,1,118);


insert into attributes (id, id_alias,id_layer,id_column, position,editable) values (7701,7701,7777,7701,1,false);
insert into attributes (id, id_alias,id_layer,id_column, position,editable) values (7702,7702,7777,7702,2,true);
insert into attributes (id, id_alias,id_layer,id_column, position,editable) values (7703,7703,7777,7703,3,true);
insert into attributes (id, id_alias,id_layer,id_column, position,editable) values (7704,7704,7777,7704,4,true);
insert into attributes (id, id_alias,id_layer,id_column, position,editable) values (7705,7705,7777,7705,5,true);
insert into attributes (id, id_alias,id_layer,id_column, position,editable) values (7706,7706,7777,7706,6,true);
insert into attributes (id, id_alias,id_layer,id_column, position,editable) values (7707,7707,7777,7707,7,true);
insert into attributes (id, id_alias,id_layer,id_column, position,editable) values (7708,7708,7777,7708,8,true);
insert into attributes (id, id_alias,id_layer,id_column, position,editable) values (7709,7709,7777,7709,9,true);
insert into attributes (id, id_alias,id_layer,id_column, position,editable) values (7710,7710,7777,7710,10,true);
insert into attributes (id, id_alias,id_layer,id_column, position,editable) values (7711,2001,7777,7711,11,false);

create sequence SEQ_VERTEDERO; 

//Tablas para actividades contaminantes
CREATE TABLE responsable_inspector
(
   id_responsable numeric(10) NOT NULL,
   id_municipio numeric(5) NOT NULL,
   apellido1 varchar(60),
   apellido2 varchar(60),
   nombre varchar(60),
   empresa varchar(50),
   puesto varchar (50),
   direccion varchar(250),
   telefono varchar(20),
   otrosdatos varchar(500),  
   constraint responsable_inspector_pkey PRIMARY KEY (id_responsable)
);

CREATE TABLE actividad_contaminante
(
	ID numeric(10) NOT NULL,
	ID_MUNICIPIO numeric(5) NOT null,
	ID_TIPO_ACTIVIDAD numeric(3),
	ID_RAZON_ESTUDIO numeric(3),
	NUM_ADMINISTRATIVO varchar(32),
	ASUNTO varchar(1000),
	FECHA_INICIO DATE,
	FECHA_FIN DATE,
	TIPO_VIA_AFECTA varchar(20),
	NOMBRE_VIA_AFECTA varchar(128),
	NUMERO_VIA_AFECTA varchar(10),
	CPOSTAL_AFECTA varchar(10),  
        constraint actividad_coontaminante_pkey PRIMARY KEY (ID)
)
select AddGeometryColumn ('pista', 'actividad_contaminante', 'GEOMETRY', 23030, 'GEOMETRY', 2);

CREATE TABLE inspeccion
(
	ID numeric(10) NOT NULL,
	ID_ACTIVIDAD numeric(10) NOT NULL,
	ID_RESPONSABLE numeric(10),
	NUM_FOLIOS numeric (4),
	FECHA_INICIO date,
	FECHA_FIN date,
	FECHA_INICIO_REC_DATOS date,
	FECHA_FIN_REC_DATOS date,
	NUM_DIAS_REC_DATOS numeric(3),
	PUNTOS_FIJOS_MEDICION varchar(500),
	PUNTOS_MOVILES_MEDICION varchar(500),
	SUSTANCIAS_CONTAMINANTES varchar(500),
	CONCENTRACION_MIN varchar(255),
	CONCENTRACION_MAX varchar(255),
	ES_ZONA_LATENTE numeric(1),
	MOTIVOS_ZONA_LATENTE varchar(500),
	ES_ZONA_SATURADA numeric(1),
	MOTIVOS_ZONA_SATURADA varchar(500),
	FACTORES_DE_RIESGO varchar(500),
	MEDIDAS_A_ADOPTAR varchar(500),
	RESULTADOS varchar(500),
	OBSERVACIONES varchar(500),
        constraint inspeccion_pkey PRIMARY KEY (ID)
)
alter table inspeccion add  foreign key (ID_ACTIVIDAD) references actividad_contaminantes(ID);
alter table inspeccion add  foreign key (ID_RESPONSABLE) references responsable_inspector(ID_RESPONSABLE);


//CREAR MAPA ACTIVIDADES CONTAMINANTES
insert into dictionary (id_vocablo,locale,traduccion) values (6666,'es_ES','actividad_contaminante');

insert into layers (id_layer,id_name,acl,name,id_styles) values (6666,6666,12,'actividad_contaminante',9);


insert into queries (id, id_layer,databasetype,selectquery,updatequery,insertquery,deletequery)
 values (6666,6666,1,
'select ID, id_tipo_actividad, id_razon_estudio,  num_administrativo, asunto, fecha_inicio, fecha_fin, tipo_via_afecta,  
nombre_via_afecta, numero_via_afecta, cpostal_afecta, "GEOMETRY" from actividad_contaminante where id_municipio=?M',
'update actividad_contaminante set "GEOMETRY"=(GeometryFromText(?6,23030)) where ID=?1',
'insert into actividad_contaminante (ID, id_tipo_actividad, id_razon_estudio,  num_administrativo, asunto,    
 "GEOMETRY", id_municipio) values ((nextval(''SEQ_ACTIVIDAD_CONTAMINANTE'')),
?2,?3,?4,?5,(GeometryFromText(?6,23030)),?M)','TODO');

insert into tables(id_table,name) values (6666,'actividad_contaminante');

insert into dictionary (id_vocablo,locale,traduccion) values (6601,'es_ES','Id');
insert into dictionary (id_vocablo,locale,traduccion) values (6602,'es_ES','Tipo actividad');
insert into dictionary (id_vocablo,locale,traduccion) values (6603,'es_ES','Razon estudio');
insert into dictionary (id_vocablo,locale,traduccion) values (6604,'es_ES','Num administrativo');
insert into dictionary (id_vocablo,locale,traduccion) values (6605,'es_ES','Asunto');
insert into dictionary (id_vocablo,locale,traduccion) values (6606,'es_ES','Fecha Inicio');
insert into dictionary (id_vocablo,locale,traduccion) values (6607,'es_ES','Fecha Fin');
insert into dictionary (id_vocablo,locale,traduccion) values (6608,'es_ES','Tipo via');
insert into dictionary (id_vocablo,locale,traduccion) values (6609,'es_ES','Nombre via');
insert into dictionary (id_vocablo,locale,traduccion) values (6610,'es_ES','Numero via');
insert into dictionary (id_vocablo,locale,traduccion) values (6611,'es_ES','Codigo postal');
insert into dictionary (id_vocablo,locale,traduccion) values (6612,'es_ES','Geometria');


insert into columns (id,name,id_table,"Type","Scale",id_domain) values (6601,'ID',6666,2,2,118);
insert into columns (id,name,id_table,"Type","Scale",id_domain) values (6602,'id_tipo_actividad',6666,3,3,118);
insert into columns (id,name,id_table,"Type","Scale",id_domain) values (6603,'id_razon_estudio',6666,3,0,118);
insert into columns (id,name,id_table,"Type","Scale",id_domain) values (6604,'num_administrativo',6666,3,0,118);
insert into columns (id,name,id_table,"Type","Scale",id_domain) values (6605,'asunto',6666,3,0,118);
insert into columns (id,name,id_table,"Type","Scale",id_domain) values (6606,'fecha_inicio',6666,4,0,125);
insert into columns (id,name,id_table,"Type","Scale",id_domain) values (6607,'fecha_fin',6666,4,0,125);
insert into columns (id,name,id_table,"Type","Scale",id_domain) values (6608,'tipo_via_afecta',6666,3,0,118);
insert into columns (id,name,id_table,"Type","Scale",id_domain) values (6609,'nombre_via_afecta',6666,3,0,118);
insert into columns (id,name,id_table,"Type","Scale",id_domain) values (6610,'numero_via_afecta',6666,2,0,118);
insert into columns (id,name,id_table,"Type","Scale",id_domain) values (6611,'cpostal_afecta',6666,2,0,118);
insert into columns (id,name,id_table,"Type",id_domain) values (6612,'GEOMETRY',6666,1,118);

insert into attributes (id, id_alias,id_layer,id_column, position,editable) values (6601,6601,6666,6601,1,false);
insert into attributes (id, id_alias,id_layer,id_column, position,editable) values (6602,6602,6666,6602,2,true);
insert into attributes (id, id_alias,id_layer,id_column, position,editable) values (6603,6603,6666,6603,3,true);
insert into attributes (id, id_alias,id_layer,id_column, position,editable) values (6604,6604,6666,6604,4,true);
insert into attributes (id, id_alias,id_layer,id_column, position,editable) values (6605,6605,6666,6605,5,true);
--insert into attributes (id, id_alias,id_layer,id_column, position,editable) values (6606,6606,6666,6606,6,true);
--insert into attributes (id, id_alias,id_layer,id_column, position,editable) values (6607,6607,6666,6607,7,true);
--insert into attributes (id, id_alias,id_layer,id_column, position,editable) values (6608,6608,6666,6608,8,true);
--insert into attributes (id, id_alias,id_layer,id_column, position,editable) values (6609,6609,6666,6609,9,true);
--insert into attributes (id, id_alias,id_layer,id_column, position,editable) values (6610,6610,6666,6610,10,true);
--insert into attributes (id, id_alias,id_layer,id_column, position,editable) values (6611,6611,6666,6611,11,true);
insert into attributes (id, id_alias,id_layer,id_column, position,editable) values (6612,2001,6666,6612,8,false);

create sequence SEQ_ACTIVIDAD_CONTAMINANTE; 



//CREAR Layer ocupaciones

CREATE TABLE geometria_ocupacion
(
  id numeric(10) NOT NULL,
  id_solicitud numeric(14),
  id_municipio numeric(5) NOT NULL,
  CONSTRAINT geopmetria_pkey PRIMARY KEY (id)
) ;

update layers set name ='numeros_policia' where id_layer=12;
select AddGeometryColumn ('pista', 'geometria_ocupacion', 'GEOMETRY', 23030, 'GEOMETRY', 2);

insert into tables(id_table,name) values (5555,'geometria_ocupacion');
insert into dictionary (id_vocablo,locale,traduccion) values (5555,'es_ES','ocupaciones');
insert into layers (id_layer,id_name,acl,name,id_styles) values (5555,5555,12,'ocupaciones',9);

create sequence SEQ_GEOMETRIA_OCUPACION;

insert into queries (id, id_layer,databasetype,selectquery,updatequery,insertquery,deletequery)
 values (5555,5555,1,
'select geometria_ocupacion.ID, geometria_ocupacion.id_solicitud, geometria_ocupacion."GEOMETRY" from 
geometria_ocupacion LEFT JOIN solicitud_licencia s ON s.id_solicitud=geometria_ocupacion.id_solicitud 
where geometria_ocupacion.id_municipio=?M',
'update geometria_ocupacion set "GEOMETRY"=GeometryFromText(?3,23030), id_solicitud=?2
 where ID=?1',
'INSERT INTO geometria_ocupacion("GEOMETRY",ID,ID_Solicitud, id_municipio) VALUES
(GeometryFromText(?3,23030),(nextval(''SEQ_GEOMETRIA_OCUPACION'')),?2,?M)','delete from geometria_ocupacion where id=?1');

insert into dictionary (id_vocablo,locale,traduccion) values (5501,'es_ES','Id');
insert into dictionary (id_vocablo,locale,traduccion) values (5502,'es_ES','Id_solicitud');

insert into columns (id,name,id_table,"Type","Scale",id_domain) values (5501,'ID',5555,2,2,118);
insert into columns (id,name,id_table,"Type","Scale",id_domain) values (5502,'id_solicitud',5555,3,3,118);
insert into columns (id,name,id_table,"Type",id_domain) values (5511,'GEOMETRY',5555,1,118);



insert into attributes (id, id_alias,id_layer,id_column, position,editable) values (5501,5501,5555,5501,1,false);
insert into attributes (id, id_alias,id_layer,id_column, position,editable) values (5502,5502,5555,5502,2,true);
insert into attributes (id, id_alias,id_layer,id_column, position,editable) values (5511,2001,5555,5511,3,false);



