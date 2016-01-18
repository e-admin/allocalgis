update queries set selectquery='SELECT DistritosCensales.id, DistritosCensales.nombre, DistritosCensales.codigoine, DistritosCensales.id_municipio, transform(DistritosCensales."GEOMETRY",?T) as "GEOMETRY", DistritosCensales.area, DistritosCensales.length, Municipios.NombreOficial FROM Municipios INNER JOIN DistritosCensales ON (Municipios.ID=DistritosCensales.ID_Municipio) WHERE Municipios.ID in (?M)'
where id_layer in (select id_layer from layers where name='distritoscensales');

update queries set selectquery='SELECT SubseccionesCensales.id, SubseccionesCensales.codigoine, SubseccionesCensales.id_seccion, transform(SubseccionesCensales."GEOMETRY", ?T) as "GEOMETRY", SubseccionesCensales.area, SubseccionesCensales.length, SeccionesCensales.codigoine FROM (DistritosCensales INNER JOIN SeccionesCensales ON (DistritosCensales.ID=SeccionesCensales.ID_Distrito)) INNER JOIN SubseccionesCensales ON (SeccionesCensales.ID=SubseccionesCensales.ID_Seccion) WHERE DistritosCensales.ID_Municipio in (?M)'
where id_layer in (select id_layer from layers where name='subseccionescensales');

update queries set selectquery='SELECT SeccionesCensales.id, SeccionesCensales.id_distrito, SeccionesCensales.codigoine, transform(SeccionesCensales."GEOMETRY",?T) as "GEOMETRY", SeccionesCensales.nombre, SeccionesCensales.area, SeccionesCensales.length, DistritosCensales.Nombre, DistritosCensales.CodigoINE FROM DistritosCensales INNER JOIN SeccionesCensales ON (DistritosCensales.ID=SeccionesCensales.ID_Distrito) WHERE DistritosCensales.ID_Municipio in (?M)'
where id_layer in (select id_layer from layers where name='seccionescensales');

update queries set insertquery='INSERT INTO SeccionesCensales("GEOMETRY",ID,ID_Distrito,codigoine,nombre,Area,Length) VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK,?3,?4,?5,area2d(GeometryFromText(text(?1),?S)),perimeter(GeometryFromText(?1,?S)))'
where id_layer in (select id_layer from layers where name='seccionescensales');

update queries set insertquery='INSERT INTO SubseccionesCensales("GEOMETRY",ID,CodigoINE,ID_Seccion,Area,Length) VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK,?3,?4,area2d(GeometryFromText(text(?1),?S)),perimeter(GeometryFromText(text(?1),?S)))'
where id_layer in (select id_layer from layers where name='subseccionescensales');

update tables set geometrytype=11 where name='subseccionescensales';

update queries set insertquery='INSERT INTO clasificacion_suelo("GEOMETRY",id,id_municipio,idplan,idusoglo,idclasiftipo,categoria,numero,normativa,descripcion,area,length) VALUES(transform(GeometryFromText(?1,?S), ?T),?PK,?M,?4,?5,?6,?7,?8,?9,?10,area2d(GeometryFromText(text(?1),?S)),perimeter(GeometryFromText(text(?1),?S)))' 
where id_layer in (select id_layer from layers where name='clasificacion_suelo');