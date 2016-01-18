-- Tabla para definicion de tipos. notas o actuaciones.
CREATE TABLE civil_work_warnings_types
(
	id_type integer,
	description text,
	CONSTRAINT civil_work_warnings_types_pkey PRIMARY KEY (id_type)
)
WITHOUT OIDS;
ALTER TABLE civil_work_warnings_types OWNER TO postgres;

INSERT INTO civil_work_warnings_types(id_type,description) VALUES (1,'Notas');
INSERT INTO civil_work_warnings_types(id_type,description) VALUES (2,'Intervenciones');
-- Se crea la tabla por defecto para obtener las notas o intervenciones en general.
CREATE TABLE civil_work_warnings
(
  id_warning integer,
  description text,
  start_warning date not null,
  user_creator integer not null,
  id_municipio integer not null,
  id_type integer,
  CONSTRAINT civil_work_warnings_pkey PRIMARY KEY (id_warning),
  CONSTRAINT civil_work_warnings_id_type_fk FOREIGN KEY (id_type)
      REFERENCES civil_work_warnings_types (id_type) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITHOUT OIDS;
ALTER TABLE civil_work_warnings OWNER TO postgres;

CREATE TABLE civil_work_intervention
(
  id_warning integer,
  actuation_type text,
  intervention_type text,
  pattern text,
  next_warning date,
  foreseen_budget numeric(15,2),
  work_percentage numeric(5,2),
  causes text,
  assigned_user integer,
  CONSTRAINT civil_work_intervention_pkey PRIMARY KEY (id_warning),
  CONSTRAINT civil_work_intervention_id_warning_fk FOREIGN KEY (id_warning)
      REFERENCES civil_work_warnings (id_warning) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE
)
WITHOUT OIDS;
ALTER TABLE civil_work_intervention OWNER TO postgres;

CREATE TABLE civil_work_layer_feature_reference
(
  id_warning integer NOT NULL,
  id_layer integer NOT NULL,
  id_feature integer NOT NULL,
  CONSTRAINT civil_work_layer_feature_reference_pkey PRIMARY KEY (id_warning, id_layer, id_feature),
  CONSTRAINT civil_work_layer_feature_reference_fk FOREIGN KEY (id_warning)
      REFERENCES civil_work_warnings (id_warning) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE
)
WITHOUT OIDS;
ALTER TABLE civil_work_layer_feature_reference OWNER TO postgres;

CREATE SEQUENCE civil_work_id_warning_sequence
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE civil_work_id_warning_sequence OWNER TO postgres;

CREATE TABLE civil_work_documents
(
  id_warning int4 NOT NULL,
  id_document int4 NOT NULL,
  document_type text,
  document_extension text,
  document_name text,
  document_file bytea,
  CONSTRAINT civil_work_documents_pkey PRIMARY KEY (id_document, id_warning),
  CONSTRAINT civil_work_documents_fk FOREIGN KEY (id_warning) REFERENCES civil_work_warnings (id_warning) ON UPDATE NO ACTION ON DELETE CASCADE
) 
WITHOUT OIDS;
ALTER TABLE civil_work_documents OWNER TO postgres;


CREATE TABLE civil_work_document_thumbnail
(
  id_document int4 NOT NULL,
  id_warning int4 NOT NULL,
  document_thumbnail bytea NOT NULL,
  CONSTRAINT civil_work_document_thumbnail_pkey PRIMARY KEY (id_document, id_warning),
  CONSTRAINT civil_work_document_thumbnail_fk FOREIGN KEY (id_document, id_warning) REFERENCES civil_work_documents (id_document, id_warning) ON UPDATE NO ACTION ON DELETE CASCADE
) 
WITHOUT OIDS;
ALTER TABLE civil_work_document_thumbnail OWNER TO postgres;

CREATE SEQUENCE civil_work_id_document_sequence
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE civil_work_id_document_sequence OWNER TO postgres;

INSERT INTO ACL VALUES (nextval('seq_acl'),'Gestion de Espacio Publico');
-- Agrega los permisos para la aplicacion de espacio publico
INSERT INTO USRGROUPERM VALUES(nextval('seq_acl_perm'),'localgis.espaciopublico.login',NULL);
INSERT INTO R_ACL_PERM VALUES(currval('seq_acl_perm'),currval('seq_acl'));
INSERT INTO R_USR_PERM VALUES(100,currval('seq_acl_perm'),currval('seq_acl'),1);
INSERT INTO USRGROUPERM VALUES(nextval('seq_acl_perm'),'localgis.espaciopublico.administracion',NULL);
INSERT INTO R_ACL_PERM VALUES(currval('seq_acl_perm'),currval('seq_acl'));
INSERT INTO R_USR_PERM VALUES(100,currval('seq_acl_perm'),currval('seq_acl'),1);
INSERT INTO USRGROUPERM VALUES(nextval('seq_acl_perm'),'localgis.espaciopublico.notas.consulta',NULL);
INSERT INTO R_ACL_PERM VALUES(currval('seq_acl_perm'),currval('seq_acl'));
INSERT INTO USRGROUPERM VALUES(nextval('seq_acl_perm'),'localgis.espaciopublico.notas.alta',NULL);
INSERT INTO R_ACL_PERM VALUES(currval('seq_acl_perm'),currval('seq_acl'));
INSERT INTO USRGROUPERM VALUES(nextval('seq_acl_perm'),'localgis.espaciopublico.notas.eliminar',NULL);
INSERT INTO R_ACL_PERM VALUES(currval('seq_acl_perm'),currval('seq_acl'));
INSERT INTO USRGROUPERM VALUES(nextval('seq_acl_perm'),'localgis.espaciopublico.intervenciones.consulta',NULL);
INSERT INTO R_ACL_PERM VALUES(currval('seq_acl_perm'),currval('seq_acl'));
INSERT INTO USRGROUPERM VALUES(nextval('seq_acl_perm'),'localgis.espaciopublico.intervenciones.alta',NULL);
INSERT INTO R_ACL_PERM VALUES(currval('seq_acl_perm'),currval('seq_acl'));
INSERT INTO USRGROUPERM VALUES(nextval('seq_acl_perm'),'localgis.espaciopublico.intervenciones.modificacion',NULL);
INSERT INTO R_ACL_PERM VALUES(currval('seq_acl_perm'),currval('seq_acl'));
INSERT INTO USRGROUPERM VALUES(nextval('seq_acl_perm'),'localgis.espaciopublico.intervenciones.postponer',NULL);
INSERT INTO R_ACL_PERM VALUES(currval('seq_acl_perm'),currval('seq_acl'));
INSERT INTO USRGROUPERM VALUES(nextval('seq_acl_perm'),'localgis.espaciopublico.intervenciones.descartar',NULL);
INSERT INTO R_ACL_PERM VALUES(currval('seq_acl_perm'),currval('seq_acl'));
INSERT INTO USRGROUPERM VALUES(nextval('seq_acl_perm'),'localgis.espaciopublico.intervenciones.eliminar',NULL);
INSERT INTO R_ACL_PERM VALUES(currval('seq_acl_perm'),currval('seq_acl'));

-- Consultas para Gestion Ciudad
insert into query_catalog values ('GestionCiudadObtenerTiposActuacion','select id_node,id_description,parentdescription,parentpattern from  (select dn.id as id_node,dn.id_description as id_description,d1.traduccion as parentdescription,dn.pattern as parentpattern,d2.traduccion as description,dn2.pattern as pattern from domainnodes dn left join domainnodes dn2 on dn.id = dn2.parentdomain left join dictionary d1 on d1.id_vocablo = dn.id_description and d1.locale = ? left join dictionary d2 on d2.id_vocablo = dn2.id_description and d2.locale = ? where dn.parentdomain = (SELECT id FROM DOMAINNODES,DICTIONARY WHERE DOMAINNODES.ID_DESCRIPTION = DICTIONARY.ID_VOCABLO AND DICTIONARY.LOCALE =? and DICTIONARY.traduccion = ?)) d group by parentdescription,parentpattern,id_description,id_node',1,9205);
insert into query_catalog values ('GestionCiudadObtenerTraduccionesTiposActuacion','select parentdescription from  (select d1.traduccion as parentdescription,dn.pattern as parentpattern,d2.traduccion as description,dn2.pattern as pattern from domainnodes dn left join domainnodes dn2 on dn.id = dn2.parentdomain left join dictionary d1 on d1.id_vocablo = dn.id_description and d1.locale = ? left join dictionary d2 on d2.id_vocablo = dn2.id_description and d2.locale = ? where dn.parentdomain = (SELECT id FROM DOMAINNODES,DICTIONARY WHERE DOMAINNODES.ID_DESCRIPTION = DICTIONARY.ID_VOCABLO AND DICTIONARY.LOCALE = ? and DICTIONARY.traduccion = ?)) d  where parentpattern=? group by parentdescription,parentpattern',1,9205);
insert into query_catalog values ('GestionCiudadObtenerTiposIntervencion','select id_node,id_description,description,pattern from (select dn2.id as id_node,dn2.id_description as id_description,d1.traduccion as parentdescription,dn.pattern as parentpattern,d2.traduccion as description,dn2.pattern as pattern from domainnodes dn left join domainnodes dn2 on dn.id = dn2.parentdomain left join dictionary d1 on d1.id_vocablo = dn.id_description and d1.locale = ? left join dictionary d2 on d2.id_vocablo = dn2.id_description and d2.locale = ? where dn.parentdomain = (SELECT id FROM DOMAINNODES,DICTIONARY WHERE DOMAINNODES.ID_DESCRIPTION = DICTIONARY.ID_VOCABLO AND DICTIONARY.LOCALE = ? and DICTIONARY.traduccion = ?)) d where parentpattern = ?',1,9205);
insert into query_catalog values ('GestionCiudadObtenerTraduccionesTiposIntervencion','select dn2.id as id_node,dn2.id_description as id_description,d1.traduccion as parentdescription,dn.pattern as parentpattern,d2.traduccion as description,dn2.pattern as pattern from domainnodes dn left join domainnodes dn2 on dn.id = dn2.parentdomain left join dictionary d1 on d1.id_vocablo = dn.id_description and d1.locale =? left join dictionary d2 on d2.id_vocablo = dn2.id_description and d2.locale =? where dn.parentdomain = (SELECT id FROM DOMAINNODES,DICTIONARY WHERE DOMAINNODES.ID_DESCRIPTION = DICTIONARY.ID_VOCABLO AND DICTIONARY.LOCALE =? and DICTIONARY.traduccion =?) and dn2.pattern=?',1,9205);
insert into query_catalog values ('GestionCiudadObtenerNombreViaConTipo','select tipoviaine,nombreviaine,tipovianormalizadocatastro,nombrecatastro,nombreviacortoine from vias where id_municipio=? and id=?',1,9205);
