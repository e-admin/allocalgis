DROP TABLE session_app;
CREATE TABLE session_app(
  id character varying(20) NOT NULL,
  access_order numeric(3,0),
  appid numeric(10,0) NOT NULL,
  CONSTRAINT pk_session_app PRIMARY KEY (id, access_order),
  CONSTRAINT fk_sessionapp_reference_appgeopi FOREIGN KEY (appid)
      REFERENCES appgeopista (appid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_sessionapp_reference_iusercnt FOREIGN KEY (id)
      REFERENCES iusercnt(id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=TRUE
);
ALTER TABLE session_app OWNER TO geopista;


-- queries para insertar en la BD
update query_catalog set query = 'select distinct(iusercnt.id), appgeopista.def, iusercnt.userid, iusercnt.timestamp , iusercnt.timeclose, 
array_to_string (ARRAY(select distinct(appgeopista.def) from appgeopista, session_app where appgeopista.appid = session_app.appid and session_app.id=iusercnt.id ), '', '')
from iusercnt LEFT JOIN session_app ON iusercnt.id = session_app.id, appgeopista
where iusercnt.appid = appgeopista.appid and iusercnt.userid=?
order by iusercnt.timeclose desc' 
where id  = 'conexiones';

insert into query_catalog (id, query, acl, idperm) 
values ('SM_LayerOperationsBySession', 'select h.* from iusercnt c, appgeopista a, history h 
where a.appid=c.appid and c.userid = h.user_id and c.id=? and h.user_id=? and h.ts BETWEEN c.timestamp AND c.timeclose', 1, 9205);

insert into query_catalog (id, query, acl, idperm) 
values ('SM_TableOperationsBySession', 'select v.* from iusercnt c, versionesalfa v 
where c.userid = v.id_autor and c.id = ? and v.id_autor = ? and v.fecha BETWEEN c.timestamp AND c.timeclose', 1, 9205);


DROP INDEX IF EXISTS history_by_user;
CREATE INDEX history_by_user ON history(user_id);

DROP INDEX IF EXISTS versionesalfa_by_user;
CREATE INDEX versionesalfa_by_user ON versionesalfa(id_autor);


insert into usrgrouperm (idperm,def) values ('4031',	'Geopista.Map.CrearMapaGlobal');	
insert into r_acl_perm (idperm,	idacl) values (4031,	12);

insert into usrgrouperm (idperm,def) values ('4032',	'Geopista.Map.PublicarGlobal');	
insert into r_acl_perm (idperm,	idacl) values (4032,	12);

insert into usrgrouperm (idperm,def) values ('4033',	'Geopista.Map.BorrarMapasGlobales');	
insert into r_acl_perm (idperm,	idacl) values (4033,	12);

insert into usrgrouperm (idperm,def) values ('4034',	'Geopista.Administracion.VerSesiones');	
insert into r_acl_perm (idperm,	idacl) values (4034,	1);


DROP INDEX IF EXISTS parcelas_ref_catastral_lower;
CREATE INDEX parcelas_ref_catastral_lower ON parcelas(lower(referencia_catastral));