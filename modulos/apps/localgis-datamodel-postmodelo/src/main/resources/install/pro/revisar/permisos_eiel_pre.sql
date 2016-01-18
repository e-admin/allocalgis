delete from r_group_perm where groupid=151

--Captacion (Hay 2 acl de captacion, no tengo claro cual es el bueno)
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4000,(select idacl from acl where name ='Captacion' and idacl=138));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4010,(select idacl from acl where name ='Captacion' and idacl=138));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4020,(select idacl from acl where name ='Captacion' and idacl=138));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4030,(select idacl from acl where name ='Captacion' and idacl=138));

INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4000,(select idacl from acl where name ='Captacion' and idacl=141));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4010,(select idacl from acl where name ='Captacion' and idacl=141));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4020,(select idacl from acl where name ='Captacion' and idacl=141));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4030,(select idacl from acl where name ='Captacion' and idacl=141));

--Deposito (Hay 2 acl de deposito, no tengo claro cual es el bueno)
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4000,(select idacl from acl where name ='Deposito' and idacl=139));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4010,(select idacl from acl where name ='Deposito' and idacl=139));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4020,(select idacl from acl where name ='Deposito' and idacl=139));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4030,(select idacl from acl where name ='Deposito' and idacl=139));

INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4000,(select idacl from acl where name ='Deposito' and idacl=142));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4010,(select idacl from acl where name ='Deposito' and idacl=142));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4020,(select idacl from acl where name ='Deposito' and idacl=142));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4030,(select idacl from acl where name ='Deposito' and idacl=142));

--Depuradora
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4000,(select idacl from acl where name ='Depuradora'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4010,(select idacl from acl where name ='Depuradora'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4020,(select idacl from acl where name ='Depuradora'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4030,(select idacl from acl where name ='Depuradora'));
--Red de ramales
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4000,(select idacl from acl where name ='Red de ramales'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4010,(select idacl from acl where name ='Red de ramales'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4020,(select idacl from acl where name ='Red de ramales'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4030,(select idacl from acl where name ='Red de ramales'));
--Punto de vertido
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4000,(select idacl from acl where name ='Punto de vertido'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4010,(select idacl from acl where name ='Punto de vertido'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4020,(select idacl from acl where name ='Punto de vertido'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4030,(select idacl from acl where name ='Punto de vertido'));
--Instalacion deportiva
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4000,(select idacl from acl where name ='Instalacion deportiva'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4010,(select idacl from acl where name ='Instalacion deportiva'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4020,(select idacl from acl where name ='Instalacion deportiva'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4030,(select idacl from acl where name ='Instalacion deportiva'));
--Parques jardines y áreas naturales
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4000,(select idacl from acl where name ='Parques jardines y áreas naturales'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4010,(select idacl from acl where name ='Parques jardines y áreas naturales'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4020,(select idacl from acl where name ='Parques jardines y áreas naturales'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4030,(select idacl from acl where name ='Parques jardines y áreas naturales'));
--Lonjas, mercados y recintos feriales
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4000,(select idacl from acl where name ='Lonjas, mercados y recintos feriales'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4010,(select idacl from acl where name ='Lonjas, mercados y recintos feriales'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4020,(select idacl from acl where name ='Lonjas, mercados y recintos feriales'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4030,(select idacl from acl where name ='Lonjas, mercados y recintos feriales'));
--Mataderos
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4000,(select idacl from acl where name ='Mataderos'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4010,(select idacl from acl where name ='Mataderos'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4020,(select idacl from acl where name ='Mataderos'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4030,(select idacl from acl where name ='Mataderos'));
--Cementerios
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4000,(select idacl from acl where name ='Cementerios'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4010,(select idacl from acl where name ='Cementerios'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4020,(select idacl from acl where name ='Cementerios'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4030,(select idacl from acl where name ='Cementerios'));
--Tanatorios
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4000,(select idacl from acl where name ='Tanatorios'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4010,(select idacl from acl where name ='Tanatorios'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4020,(select idacl from acl where name ='Tanatorios'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4030,(select idacl from acl where name ='Tanatorios'));
--Centros Sanitarios
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4000,(select idacl from acl where name ='Centros Sanitarios'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4010,(select idacl from acl where name ='Centros Sanitarios'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4020,(select idacl from acl where name ='Centros Sanitarios'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4030,(select idacl from acl where name ='Centros Sanitarios'));
--Centros Asistenciales
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4000,(select idacl from acl where name ='Centros Asistenciales'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4010,(select idacl from acl where name ='Centros Asistenciales'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4020,(select idacl from acl where name ='Centros Asistenciales'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4030,(select idacl from acl where name ='Centros Asistenciales'));
--Centros de enseñanza
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4000,(select idacl from acl where name ='Centros de enseñanza'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4010,(select idacl from acl where name ='Centros de enseñanza'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4020,(select idacl from acl where name ='Centros de enseñanza'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4030,(select idacl from acl where name ='Centros de enseñanza'));
--Centro de protección civil
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4000,(select idacl from acl where name ='Centro de protección civil'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4010,(select idacl from acl where name ='Centro de protección civil'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4020,(select idacl from acl where name ='Centro de protección civil'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4030,(select idacl from acl where name ='Centro de protección civil'));
--Casas consistoriales
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4000,(select idacl from acl where name ='Casas consistoriales'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4010,(select idacl from acl where name ='Casas consistoriales'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4020,(select idacl from acl where name ='Casas consistoriales'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4030,(select idacl from acl where name ='Casas consistoriales'));
--Edificios de titularidad publica sin uso
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4000,(select idacl from acl where name ='Edificios de titularidad publica sin uso'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4010,(select idacl from acl where name ='Edificios de titularidad publica sin uso'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4020,(select idacl from acl where name ='Edificios de titularidad publica sin uso'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4030,(select idacl from acl where name ='Edificios de titularidad publica sin uso'));
--Infraestructura viaria
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4000,(select idacl from acl where name ='Infraestructura viaria'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4010,(select idacl from acl where name ='Infraestructura viaria'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4020,(select idacl from acl where name ='Infraestructura viaria'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4030,(select idacl from acl where name ='Infraestructura viaria'));

--Tramos de carretera 
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4000,(select idacl from acl where name ='Tramos de carretera'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4010,(select idacl from acl where name ='Tramos de carretera'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4020,(select idacl from acl where name ='Tramos de carretera'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4030,(select idacl from acl where name ='Tramos de carretera'));

--Cuadro de mando
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4000,(select idacl from acl where name ='Cuadro de mando'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4010,(select idacl from acl where name ='Cuadro de mando'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4020,(select idacl from acl where name ='Cuadro de mando'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4030,(select idacl from acl where name ='Cuadro de mando'));
--Estabilizador
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4000,(select idacl from acl where name ='Estabilizador'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4010,(select idacl from acl where name ='Estabilizador'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4020,(select idacl from acl where name ='Estabilizador'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4030,(select idacl from acl where name ='Estabilizador'));
--Punto de luz
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4000,(select idacl from acl where name ='Punto de luz'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4010,(select idacl from acl where name ='Punto de luz'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4020,(select idacl from acl where name ='Punto de luz'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4030,(select idacl from acl where name ='Punto de luz'));
--Vertedero
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4000,(select idacl from acl where name ='Vertedero'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4010,(select idacl from acl where name ='Vertedero'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4020,(select idacl from acl where name ='Vertedero'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4030,(select idacl from acl where name ='Vertedero'));
--Parroquias
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4000,(select idacl from acl where name ='Parroquias'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4010,(select idacl from acl where name ='Parroquias'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4020,(select idacl from acl where name ='Parroquias'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4030,(select idacl from acl where name ='Parroquias'));
--Edificaciones singulares
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4000,(select idacl from acl where name ='Edificaciones singulares'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4010,(select idacl from acl where name ='Edificaciones singulares'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4020,(select idacl from acl where name ='Edificaciones singulares'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4030,(select idacl from acl where name ='Edificaciones singulares'));
--Comarcas EIEL
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4000,(select idacl from acl where name ='Comarcas EIEL'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4010,(select idacl from acl where name ='Comarcas EIEL'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4020,(select idacl from acl where name ='Comarcas EIEL'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4030,(select idacl from acl where name ='Comarcas EIEL'));
--Municipios EIEL
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4000,(select idacl from acl where name ='Municipios EIEL'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4010,(select idacl from acl where name ='Municipios EIEL'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4020,(select idacl from acl where name ='Municipios EIEL'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4030,(select idacl from acl where name ='Municipios EIEL'));
--Municipios EIEL puntos
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4000,(select idacl from acl where name ='Municipios EIEL puntos'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4010,(select idacl from acl where name ='Municipios EIEL puntos'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4020,(select idacl from acl where name ='Municipios EIEL puntos'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4030,(select idacl from acl where name ='Municipios EIEL puntos'));
--Nucleos Poblacion EIEL
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4000,(select idacl from acl where name ='Nucleos Poblacion EIEL'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4010,(select idacl from acl where name ='Nucleos Poblacion EIEL'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4020,(select idacl from acl where name ='Nucleos Poblacion EIEL'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4030,(select idacl from acl where name ='Nucleos Poblacion EIEL'));
--Nucleos Poblacion EIEL Puntos
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4000,(select idacl from acl where name ='Nucleos Poblacion EIEL Puntos'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4010,(select idacl from acl where name ='Nucleos Poblacion EIEL Puntos'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4020,(select idacl from acl where name ='Nucleos Poblacion EIEL Puntos'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4030,(select idacl from acl where name ='Nucleos Poblacion EIEL Puntos'));
--Parcelas EIEL
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4000,(select idacl from acl where name ='Parcelas EIEL'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4010,(select idacl from acl where name ='Parcelas EIEL'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4020,(select idacl from acl where name ='Parcelas EIEL'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4030,(select idacl from acl where name ='Parcelas EIEL'));
--Provincias EIEL
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4000,(select idacl from acl where name ='Provincias EIEL'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4010,(select idacl from acl where name ='Provincias EIEL'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4020,(select idacl from acl where name ='Provincias EIEL'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4030,(select idacl from acl where name ='Provincias EIEL'));

--Tramos de conduccion (Hay 2 acl de tramos, no tengo claro cual es el bueno)
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4000,(select idacl from acl where name ='Tramos de Conduccion' and idacl=140));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4010,(select idacl from acl where name ='Tramos de Conduccion' and idacl=140));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4020,(select idacl from acl where name ='Tramos de Conduccion' and idacl=140));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4030,(select idacl from acl where name ='Tramos de Conduccion' and idacl=140));

INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4000,(select idacl from acl where name ='Tramos de Conduccion' and idacl=143));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4010,(select idacl from acl where name ='Tramos de Conduccion' and idacl=143));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4020,(select idacl from acl where name ='Tramos de Conduccion' and idacl=143));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4030,(select idacl from acl where name ='Tramos de Conduccion' and idacl=143));


--Tratamientos de Potabilizacion
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4000,(select idacl from acl where name ='Tratamientos de Potabilizacion'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4010,(select idacl from acl where name ='Tratamientos de Potabilizacion'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4020,(select idacl from acl where name ='Tratamientos de Potabilizacion'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4030,(select idacl from acl where name ='Tratamientos de Potabilizacion'));

--Red de distribucion domiciliaria
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4000,(select idacl from acl where name ='Red de distribucion domiciliaria'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4010,(select idacl from acl where name ='Red de distribucion domiciliaria'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4020,(select idacl from acl where name ='Red de distribucion domiciliaria'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4030,(select idacl from acl where name ='Red de distribucion domiciliaria'));

--Elemento puntual de abastecimiento
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4000,(select idacl from acl where name ='Elemento puntual de abastecimiento'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4010,(select idacl from acl where name ='Elemento puntual de abastecimiento'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4020,(select idacl from acl where name ='Elemento puntual de abastecimiento'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4030,(select idacl from acl where name ='Elemento puntual de abastecimiento'));

--Emisario
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4000,(select idacl from acl where name ='Emisario'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4010,(select idacl from acl where name ='Emisario'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4020,(select idacl from acl where name ='Emisario'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4030,(select idacl from acl where name ='Emisario'));

--Colector
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4000,(select idacl from acl where name ='Colector'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4010,(select idacl from acl where name ='Colector'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4020,(select idacl from acl where name ='Colector'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4030,(select idacl from acl where name ='Colector'));

--Elementos puntuales
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4000,(select idacl from acl where name ='Elementos puntuales'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4010,(select idacl from acl where name ='Elementos puntuales'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4020,(select idacl from acl where name ='Elementos puntuales'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4030,(select idacl from acl where name ='Elementos puntuales'));


--Centros culturales o de esparcimiento
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4000,(select idacl from acl where name ='Centros culturales o de esparcimiento'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4010,(select idacl from acl where name ='Centros culturales o de esparcimiento'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4020,(select idacl from acl where name ='Centros culturales o de esparcimiento'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (151,4030,(select idacl from acl where name ='Centros culturales o de esparcimiento'));

-- Permiso de Login (para el usuario satec_dev
insert into r_usr_perm (userid,idperm,idacl,aplica)values ((select id from iuseruserhdr where name ='SATEC_DEV'),(select idperm from usrgrouperm where def='LocalGis.EIEL.Login'),(select idacl from acl where name='EIEL'),1)
insert into r_usr_perm (userid,idperm,idacl,aplica)values ((select id from iuseruserhdr where name ='SATEC_DEV'),(select idperm from usrgrouperm where def='LocalGis.edicion.EIEL'),(select idacl from acl where name='EIEL'),1)

